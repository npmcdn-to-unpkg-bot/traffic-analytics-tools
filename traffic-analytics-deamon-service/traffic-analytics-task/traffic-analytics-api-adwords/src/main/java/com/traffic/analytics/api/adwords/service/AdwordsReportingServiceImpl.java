package com.traffic.analytics.api.adwords.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.jaxb.v201509.DateRange;
import com.google.api.ads.adwords.lib.jaxb.v201509.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201509.ReportDefinition;
import com.google.api.ads.adwords.lib.jaxb.v201509.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.jaxb.v201509.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.jaxb.v201509.Selector;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.v201509.ReportDownloader;
import com.google.common.collect.Lists;
import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.ApiAuthManager;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.commons.constants.SystemMessageCode;
import com.traffic.analytics.commons.exception.ServiceException;
import com.traffic.analytics.commons.utils.FileUtils;
import com.traffic.analytics.commons.utils.ReportUtils;

@Service("adwordsReportingServiceImpl")
public class AdwordsReportingServiceImpl implements ReportingService<AdwordsReportData> {

	@Autowired
	@Qualifier("adwordsApiAuthManagerImpl")
	private ApiAuthManager<AdWordsSession> apiAuthManager;
	
	private Log log = LogFactory.getLog(getClass());
	
	private final String lineRegex = "^.*Total, --,.*$";

    private final String numberRegex = "^[0-9]+(.[0-9]*)?$";

	private String lineSplit = ",";

	private final String finalUrlPattern = "http://";
	
	@Override
	public boolean download(String reportDownloadPath, String date, ReportType reportType, Account account) throws Exception {
        boolean downloadStatus = false;
		AdwordsAccount adWordsAccount = (AdwordsAccount)account;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			AdWordsSession session = apiAuthManager.generateApiAccessAuth(adWordsAccount);
			if (null == session) {
				return downloadStatus;
			}

			//1.create report definition
			Selector selector = new Selector();
			selector.getFields().addAll(Lists.newArrayList(adWordsAccount.getReportFields().split(",")));
			DateRange dateRange = new DateRange();
			dateRange.setMin(date);
			dateRange.setMax(date);
			selector.setDateRange(dateRange);
			ReportDefinition reportDefinition = new ReportDefinition();
			reportDefinition.setReportName(adWordsAccount.getReportName());
			reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);//
			
			if (reportType.equals(ReportType.Keyword)) {
				reportDefinition.setReportType(ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT);
			}
			
			if (reportType.equals(ReportType.Content)) {
				reportDefinition.setReportType(ReportDefinitionReportType.AD_PERFORMANCE_REPORT);
			}
			
			if (reportType.equals(ReportType.Campaign)) {
	            reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
	        }
			
			reportDefinition.setDownloadFormat(DownloadFormat.CSV);
			// Enable to allow rows with zero impressions to show.
			reportDefinition.setIncludeZeroImpressions(false);
			ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder().build();
			session.setReportingConfiguration(reportingConfiguration);
			reportDefinition.setSelector(selector);
			
			
			//2.requesting adwords api
			ReportDownloadResponse response = new ReportDownloader(session).downloadReport(reportDefinition);
			File reportFile = new File(reportDownloadPath);
            if (reportFile.exists()) {
                reportFile.delete();
                log.info("delete history bing daily keyword report of [" + reportDownloadPath + "]");
            } else {
                reportFile.getParentFile().mkdirs();
            }

			reader = new BufferedReader(new InputStreamReader(response.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportDownloadPath)));
			// skip title line
			reader.readLine();
			for (String line; (line = reader.readLine()) != null;) {
				writer.write(line + "\r\n");
			}
			log.info("AdWords Report successfully downloaded: " + reportDownloadPath);
            downloadStatus = true;
		} catch (Exception e) {
			log.info("AdWords Report was not downloaded. " + e);
			throw new ServiceException(SystemMessageCode.ADWORDS_REPORT_DOWNLOAD_FAILURE);
		} finally {
			if (null == reader && null == writer) {
				return downloadStatus;
			}
            writer.flush();
			writer.close();
			reader.close();
		}
        return downloadStatus;
	
	}

	@Override
	public List<AdwordsReportData> parse(String reportDownloadPath, String date, ReportType reportType, Account account) {
		List<AdwordsReportData> adwordsReportDatas = new ArrayList<AdwordsReportData>();
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(reportDownloadPath)));
            String title = reader.readLine();
            Map<String, Integer> titleMap = FileUtils.getLineTitleByPattern(title, lineSplit);
            for (String line; (line = reader.readLine()) != null;) {
                // fill AdWordsDataModel
                AdwordsReportData adwordsReportData = fillAdwordsReportData(line, titleMap, date, account.getWebsiteId(), account.getId() , reportType.getType());
                // data to db
                if (null != adwordsReportData) {
                	adwordsReportDatas.add(adwordsReportData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Read Report File Exception : " + e.getMessage());
        } finally {
            try {
				reader.close();
			} catch (IOException e) {
				reader = null;
			}
        }
        return adwordsReportDatas;
	}
	
	/**
	 * 封装 AdWordsDataModel对象
	 *
	 * @param line
	 * @return
	 */
	private AdwordsReportData fillAdwordsReportData(String line, Map<String, Integer> titleMap, String date, String websiteId,
			String accountId, String reportType) {
		AdwordsReportData adWordsData = null;
		if (StringUtils.isBlank(line) || line.matches(lineRegex)) {
			return adWordsData;
		}
		adWordsData = new AdwordsReportData();
		adWordsData.setWebsiteId(websiteId);
		adWordsData.setAccountId(accountId);
		String[] contents = line.split(lineSplit);
		String campaignId = contents[titleMap.get("campaignid")];
		String campaignName = contents[titleMap.get("campaign")];
		String adgroupId = contents[titleMap.get("adgroupid")];
		String adgroupName = contents[titleMap.get("adgroup")];
		String keywordId = contents[titleMap.get("keywordid")];
		String keyword = null;
        String finalUrls = null;
        Float maxcpc = 0F;
        Float avgPosition = 0F;
		if (reportType.equals(ReportUtils.SEARCH)) {
			keyword = contents[titleMap.get("keyword".toLowerCase())];
            finalUrls = contents[titleMap.get("finalurl")];
            // 去除url开头的"[""和结尾的""]"
            if (finalUrls.contains(finalUrlPattern)) {
                finalUrls = finalUrls.substring(4, finalUrls.length() - 4);
            }
            if (contents[titleMap.get("max.cpc")].matches(numberRegex)) {
                maxcpc = Float.valueOf(contents[titleMap.get("max.cpc")]) / 1000000;
            }
            avgPosition = Float.valueOf(contents[titleMap.get("avg.position")]);
		}
        Integer clicks = Integer.valueOf(contents[titleMap.get("clicks")]);
		Float cost = Float.valueOf(contents[titleMap.get("cost")]) / 1000000;
        Integer impressions = Integer.valueOf(contents[titleMap.get("impressions")]);

		adWordsData.setCampaignId(campaignId);
		adWordsData.setCampaignName(campaignName);
		adWordsData.setAdgroupId(adgroupId);
		adWordsData.setAdgroupName(adgroupName);
		adWordsData.setKeywordId(keywordId);
		adWordsData.setKeyword(keyword);
		adWordsData.setFinalUrls(finalUrls);
		adWordsData.setClicks(clicks);
		adWordsData.setCost(cost);
		adWordsData.setImpressions(impressions);
        adWordsData.setMaxcpc(maxcpc);
        adWordsData.setAvgPosition(avgPosition);
		adWordsData.setDate(date);
        adWordsData.setReportType(reportType);
		return adWordsData;
	}


}
