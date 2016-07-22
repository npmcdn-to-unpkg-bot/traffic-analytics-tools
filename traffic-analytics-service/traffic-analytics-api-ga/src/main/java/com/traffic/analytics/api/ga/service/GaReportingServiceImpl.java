package com.traffic.analytics.api.ga.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.GaData;
import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaReportData;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.ApiAuthManager;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.commons.utils.DateUtils;

@Service("gaReportingServiceImpl")
public class GaReportingServiceImpl implements ReportingService<GaReportData> {

	@Autowired
	@Qualifier("gaApiAuthManagerImpl")
	private ApiAuthManager<Analytics> apiAuthManager;

	@Autowired
	private GaGoalService gaGoalService;
	
	private Log log = LogFactory.getLog(getClass());

	private static final String Ga_Report_Line_Delimiter = ",";

	@Override
	public boolean download(String reportDownloadPath, String date, ReportType reportType, com.traffic.analytics.api.model.Account account) throws Exception {
		// 1.google analytics oauth2验证
		Analytics analytics = apiAuthManager.generateApiAccessAuth(null);

		// 2.获取所有的GaGoal
		List<GaGoal> gaGoalList = gaGoalService.getGaGoalsByGaAccount(account);

		log.info("======== download google analytics conversion report [start]========");
		Map<String, List<GaGoal>> dataMap = new HashMap<String, List<GaGoal>>();
		String metricsPattern = "ga:goalNUMBERCompletions";

		StringJoiner title = new StringJoiner(Ga_Report_Line_Delimiter);
		title.add("Campaign");
		title.add("AdGroup");
		title.add("Keyword");
		title.add("Source");

		Integer singleRequestCount = 5000;
		for (GaGoal gaGoal : gaGoalList) {
			title.add(gaGoal.getGoalId());

			// 获取单个GaGoal的GaData
			int startIndex = 1;
			String metrics = metricsPattern.replace("NUMBER", gaGoal.getGoalId());
			GaData gaData = executeDataQuery(analytics, gaGoal.getProfileId(), date, startIndex, metrics, singleRequestCount);
			int totalResult = gaData.getTotalResults();
			startIndex += singleRequestCount;
			while (startIndex < totalResult) {
				gaData = executeDataQuery(analytics, gaGoal.getProfileId(), date, startIndex, metrics, singleRequestCount);
				gaData.getRows().addAll(gaData.getRows());
				startIndex += singleRequestCount;
			}

			if (gaData.getRows() == null || gaData.getRows().isEmpty()) {
				continue;
			}

			for (List<String> rowValues : gaData.getRows()) {

				String campaign = rowValues.get(0);
				String adgroup = rowValues.get(1);
				String keyword = rowValues.get(2);
				String source = rowValues.get(3);
				Float gaGoalValue = Float.valueOf(rowValues.get(4));

				StringJoiner row = new StringJoiner(Ga_Report_Line_Delimiter);
				row.add(campaign);
				row.add(adgroup);
				row.add(keyword);
				row.add(source);

				String key = row.toString();

				if (null == dataMap.get(key)) {
					List<GaGoal> rowGaGoals = new ArrayList<GaGoal>();
					dataMap.put(key, rowGaGoals);
				}

				GaGoal rowGaGoal = new GaGoal();
				rowGaGoal.setGoalId(gaGoal.getGoalId());
				rowGaGoal.setGoalValue(gaGoalValue);
				dataMap.get(key).add(rowGaGoal);
			}
		}

		this.writeToCsvFile(reportDownloadPath, title.toString(), dataMap);
		log.info("======== download google analytics conversion report [end]========");
		return true;
	}
	
	private GaData executeDataQuery(Analytics analytics, String profileId, String date, Integer startIndex, String metrics, Integer singleRequestCount) throws Exception {
		date = DateUtils.convert("yyyyMMdd", "yyyy-MM-dd", date);
		GaData gaData = analytics.data().ga()
				.get("ga:" + profileId, date, // Start date.
						date, // End date.
						metrics) // Metrics.
				.setDimensions("ga:campaign,ga:adGroup,ga:keyword,ga:source") // Dimensions
				.setSort("-ga:source").setStartIndex(startIndex).setMaxResults(singleRequestCount).execute();
		return gaData;
	}
	

	private void writeToCsvFile(String reportDownloadPath, String title, Map<String, List<GaGoal>> contents) throws Exception {
		// 1.delete history file
		File gaReportFile = new File(reportDownloadPath);
		if (gaReportFile.exists()) {
			gaReportFile.delete();
			log.info("delete google analytics history report file");
		} else {
			gaReportFile.getParentFile().mkdirs();
		}
		// 2.write to csv file
		BufferedWriter writer = new BufferedWriter(new FileWriter(reportDownloadPath, true));
		CsvWriter wr = new CsvWriter(writer, ',');
		try {
			// title
			String[] titleContens = title.split(Ga_Report_Line_Delimiter);
			wr.writeRecord(titleContens);
			// content
			for (Entry<String, List<GaGoal>> entry : contents.entrySet()) {
				String[] fixedColumns = entry.getKey().split(Ga_Report_Line_Delimiter);

				List<GaGoal> gaGoalList = entry.getValue();
				String[] gaGoalColumns = new String[gaGoalList.size()];

				for (int x = 0; x < gaGoalList.size(); x++) {
					gaGoalColumns[x] = gaGoalList.get(x).getGoalValue().toString();
				}

				String[] content = (String[]) ArrayUtils.addAll(fixedColumns, gaGoalColumns);

				wr.writeRecord(content);
			}
		} catch (Exception e) {
			log.info("writer ga report to file Exception : " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			writer.flush();
			writer.close();
			wr.close();
		}
	}

	@Override
	public List<GaReportData> parse(String reportDownloadPath, String date, ReportType reportType, Account account) {
		List<GaReportData> gaReportDatas = new ArrayList<GaReportData>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(reportDownloadPath)));
			String title = reader.readLine();
			String titleFields[] = title.split(Ga_Report_Line_Delimiter);
			String line;
			while((line = reader.readLine()) != null){
				String[] fields = line.split(Ga_Report_Line_Delimiter);
				GaReportData gaReportData = new GaReportData();
				gaReportData.setWebsiteId(account.getWebsiteId());
				gaReportData.setDate(date);
				gaReportData.setAccountId(account.getId());
				gaReportData.setCampaign(fields[0]);
				gaReportData.setAdGroup(fields[1]);
				gaReportData.setKeyword(fields[2]);
				gaReportData.setSource(fields[3]);
				gaReportData.setGaGoals(new ArrayList<GaGoal>());
				for (int i = 4; i < fields.length; i++) {
					GaGoal gaGoal = new GaGoal();
					gaGoal.setGoalId(titleFields[i]);
					gaGoal.setGoalValue(Float.parseFloat(fields[i]));
					gaReportData.getGaGoals().add(gaGoal);
				}
				gaReportDatas.add(gaReportData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
		return gaReportDatas;
	}

}
