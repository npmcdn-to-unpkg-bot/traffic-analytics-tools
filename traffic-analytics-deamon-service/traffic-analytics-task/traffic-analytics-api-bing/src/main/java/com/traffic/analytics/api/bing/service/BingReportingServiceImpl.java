package com.traffic.analytics.api.bing.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.bingads.AuthorizationData;
import com.microsoft.bingads.ServiceClient;
import com.microsoft.bingads.reporting.AccountThroughAdGroupReportScope;
import com.microsoft.bingads.reporting.ArrayOfCampaignReportScope;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.CampaignReportScope;
import com.microsoft.bingads.reporting.Date;
import com.microsoft.bingads.reporting.DeviceTypeReportFilter;
import com.microsoft.bingads.reporting.IReportingService;
import com.microsoft.bingads.reporting.KeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.KeywordPerformanceReportFilter;
import com.microsoft.bingads.reporting.KeywordPerformanceReportRequest;
import com.microsoft.bingads.reporting.KeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.PollGenerateReportRequest;
import com.microsoft.bingads.reporting.ReportAggregation;
import com.microsoft.bingads.reporting.ReportFormat;
import com.microsoft.bingads.reporting.ReportRequestStatus;
import com.microsoft.bingads.reporting.ReportRequestStatusType;
import com.microsoft.bingads.reporting.ReportTime;
import com.microsoft.bingads.reporting.SortOrder;
import com.microsoft.bingads.reporting.SubmitGenerateReportRequest;
import com.microsoft.bingads.v10.campaignmanagement.Campaign;
import com.microsoft.bingads.v10.campaignmanagement.CampaignType;
import com.microsoft.bingads.v10.campaignmanagement.GetCampaignsByAccountIdRequest;
import com.microsoft.bingads.v10.campaignmanagement.GetCampaignsByAccountIdResponse;
import com.microsoft.bingads.v10.campaignmanagement.ICampaignManagementService;
import com.traffic.analytics.api.bing.model.BingAccount;
import com.traffic.analytics.api.bing.model.BingReportData;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.ApiAuthManager;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.commons.exception.ServiceException;

@Service("bingReportingServiceImpl")
public class BingReportingServiceImpl implements ReportingService<BingReportData>{

	@Autowired
	@Qualifier("bingApiAuthManagerImpl")
	private ApiAuthManager<AuthorizationData> apiAuthManager;

	private Log log = LogFactory.getLog(getClass());

	private ServiceClient<IReportingService> reportingService;

	@Override
	public boolean download(String reportDownloadPath, String date, ReportType reportType, Account account) throws Exception {

		boolean downloadStatus = false;

		BingAccount bingAccount = (BingAccount) account;
		if (null == bingAccount) {
			log.info("No Bing Account found");
			return downloadStatus;
		}
		try {
			AuthorizationData authorizationData = apiAuthManager.generateApiAccessAuth(bingAccount);
			reportingService = new ServiceClient<IReportingService>(authorizationData, IReportingService.class);
			Set<Long> campaignIds = this.getCampaignByAccountId(authorizationData, Long.parseLong(bingAccount.getAccountId()));
			log.info("Bing API : get campaigns size : " + campaignIds.size());
			Set<Long> bufferdCampaignIdsParam = new HashSet<Long>();

			int campaignIdCount = 0;
			int campaignLimit = 300;

			for (Long campaignId : campaignIds) {
				bufferdCampaignIdsParam.add(campaignId);
				campaignIdCount++;
				if (campaignIdCount % campaignLimit == 0) {
					String reportDownloadUrl = this.createDownloadKeywordReportUrl(bufferdCampaignIdsParam, bingAccount.getAccountId(), date);
					this.downloadReportZip(reportDownloadUrl, reportDownloadPath + ".zip", reportDownloadPath);
					bufferdCampaignIdsParam.clear();
					log.info("Bing API : download " + campaignIdCount + " success!");
				}
			}

			if (bufferdCampaignIdsParam.size() > 0) {
				String reportDownloadUrl = this.createDownloadKeywordReportUrl(bufferdCampaignIdsParam, bingAccount.getAccountId(), date);
				this.downloadReportZip(reportDownloadUrl, reportDownloadPath + ".zip", reportDownloadPath);
				bufferdCampaignIdsParam.clear();
			}
			log.info("Bing API : download " + campaignIdCount + " success!");
			downloadStatus = true;
		} catch (Exception e) {
			throw new ServiceException("Download Bing Keyword Report Exception : " + e.getMessage());
		}
		return downloadStatus;
	}

	/**
	 * 查询账号下的所有的Campaign，并返回CampaignID的集合
	 *
	 * @param authorizationData
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	private Set<Long> getCampaignByAccountId(AuthorizationData authorizationData, Long accountId) throws Exception {
		Set<Long> campaignIdSet = null;

		ServiceClient<ICampaignManagementService> campaignService = new ServiceClient<ICampaignManagementService>(authorizationData, ICampaignManagementService.class);
		GetCampaignsByAccountIdRequest campaignsRequest = new GetCampaignsByAccountIdRequest();
		campaignsRequest.setAccountId(accountId);
		List<CampaignType> list = new ArrayList<CampaignType>();
		list.add(CampaignType.SEARCH_AND_CONTENT);
		campaignsRequest.setCampaignType(list);

		GetCampaignsByAccountIdResponse response = campaignService.getService().getCampaignsByAccountId(campaignsRequest);
		List<Campaign> campaignList = response.getCampaigns().getCampaigns();
		if (campaignList != null && campaignList.size() > 0) {
			campaignIdSet = new HashSet<Long>();
			for (Campaign c : campaignList) {
				campaignIdSet.add(c.getId());
			}
		}

		return campaignIdSet;
	}

	private String createDownloadKeywordReportUrl(Set<Long> campaignIds, String accountId, String date) throws Exception {
		KeywordPerformanceReportRequest report = new KeywordPerformanceReportRequest();

		report.setFormat(ReportFormat.TSV);
		report.setReportName("Bing Keyword Performance Report");
		report.setReturnOnlyCompleteData(false);
		report.setAggregation(ReportAggregation.DAILY);

		report.setScope(new AccountThroughAdGroupReportScope());
		report.getScope().setAccountIds(null);
		report.getScope().setAdGroups(null);

		ArrayOfCampaignReportScope campaignReportScopes = getArrayOfCampaignReportScope(campaignIds, accountId);

		report.getScope().setCampaigns(campaignReportScopes);

		report.setTime(new ReportTime());
		log.info("==============download bing keyword report date:==============" + date);
		// You may either use a custom date range or predefined time.
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		report.getTime().setCustomDateRangeStart(new Date());
		report.getTime().getCustomDateRangeStart().setMonth(month);
		report.getTime().getCustomDateRangeStart().setDay(day);
		report.getTime().getCustomDateRangeStart().setYear(year);
		report.getTime().setCustomDateRangeEnd(new Date());
		report.getTime().getCustomDateRangeEnd().setMonth(month);
		report.getTime().getCustomDateRangeEnd().setDay(day);
		report.getTime().getCustomDateRangeEnd().setYear(year);
		report.getTime().setPredefinedTime(null);
		// report.getTime().setPredefinedTime(com.microsoft.bingads.reporting.ReportTimePeriod.LAST_MONTH);

		report.setFilter(new KeywordPerformanceReportFilter());
		ArrayList<DeviceTypeReportFilter> deviceTypes = new ArrayList<DeviceTypeReportFilter>();
		deviceTypes.add(DeviceTypeReportFilter.COMPUTER);
		deviceTypes.add(DeviceTypeReportFilter.SMART_PHONE);
		deviceTypes.add(DeviceTypeReportFilter.TABLET);
		deviceTypes.add(DeviceTypeReportFilter.NON_SMART_PHONE);
		report.getFilter().setDeviceType(deviceTypes);

		// Specify the attribute and data report columns.
		ArrayOfKeywordPerformanceReportColumn keywordPerformanceReportColumns = new ArrayOfKeywordPerformanceReportColumn();
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_ID);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_NAME);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_NAME);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_ID);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AD_GROUP_NAME);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AD_GROUP_ID);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_ID);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AD_DISTRIBUTION);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_STATUS);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.DESTINATION_URL);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CURRENT_MAX_CPC);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.QUALITY_SCORE);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.IMPRESSIONS);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CLICKS);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CTR);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AVERAGE_CPC);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.SPEND);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AVERAGE_POSITION);
		// keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_MATCH_TYPE_ID);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.BID_MATCH_TYPE);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSIONS);
		keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSION_RATE);

		report.setColumns(keywordPerformanceReportColumns);

		// You may optionally sort by any KeywordPerformanceReportColumn,
		// and optionally
		// specify the maximum number of rows to return in the sorted
		// report.

		KeywordPerformanceReportSort keywordPerformanceReportSort = new KeywordPerformanceReportSort();
		keywordPerformanceReportSort.setSortColumn(KeywordPerformanceReportColumn.CLICKS);
		keywordPerformanceReportSort.setSortOrder(SortOrder.ASCENDING);
		ArrayOfKeywordPerformanceReportSort keywordPerformanceReportSorts = new ArrayOfKeywordPerformanceReportSort();
		keywordPerformanceReportSorts.getKeywordPerformanceReportSorts().add(keywordPerformanceReportSort);
		report.setSort(keywordPerformanceReportSorts);

		report.setMaxRows(Integer.MAX_VALUE);

		// SubmitGenerateReport helper method calls the corresponding Bing
		// Ads ReportingService.getService() operation
		// to request the report identifier. The identifier is used to check
		// report generation status
		// before downloading the report.
		SubmitGenerateReportRequest submitGenerateReportRequest = new SubmitGenerateReportRequest();
		submitGenerateReportRequest.setReportRequest(report);
		String reportRequestId = reportingService.getService().submitGenerateReport(submitGenerateReportRequest).getReportRequestId();

		log.info("Report Request ID: " + reportRequestId + "\n");

		ReportRequestStatus reportRequestStatus = null;

		// This sample polls every 30 seconds up to 5 minutes.
		// In production you may poll the status every 1 to 2 minutes for up
		// to one hour.
		// If the call succeeds, stop polling. If the call or
		// download fails, the call throws a fault.
		String reportDownloadUrl = null;
		for (int i = 0; i < 100; i++) {
			PollGenerateReportRequest pollGenerateReportRequest = new PollGenerateReportRequest();
			pollGenerateReportRequest.setReportRequestId(reportRequestId);
			reportRequestStatus = reportingService.getService().pollGenerateReport(pollGenerateReportRequest).getReportRequestStatus();
			if (reportRequestStatus.getStatus() == ReportRequestStatusType.SUCCESS) {
				reportDownloadUrl = reportRequestStatus.getReportDownloadUrl();
				log.info("Bing API " + i + ": buffered downloading from " + reportDownloadUrl);
				break;
			} else if (reportRequestStatus.getStatus() == ReportRequestStatusType.ERROR) {
				log.error("Bing API " + i + ": get daily report response status return error");
				// waiting bing report ready
				Thread.sleep(2000);
			} else {
				log.error("Bing API " + i + ": save the report ID and try again later." + reportRequestId);
				// waiting bing report ready
				Thread.sleep(2000);
			}
		}
		return reportDownloadUrl;
	}

	private ArrayOfCampaignReportScope getArrayOfCampaignReportScope(Set<Long> campaignIds, String accountId) {
		ArrayOfCampaignReportScope campaignReportScopes = new ArrayOfCampaignReportScope();
		log.info(campaignIds.toArray().toString());
		for (long campaignId : campaignIds) {
			CampaignReportScope campaignReportScope = new CampaignReportScope();
			campaignReportScope.setCampaignId(campaignId);
			campaignReportScope.setAccountId(Long.parseLong(accountId));
			campaignReportScopes.getCampaignReportScopes().add(campaignReportScope);
		}
		return campaignReportScopes;
	}

	private void downloadReportZip(String reportDownloadUrl, String zipFilePath, String tsvFilePath) throws Exception {
		File file = new File(zipFilePath);
		// 如果目录不存在则创建
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
		URL url = new URL(reportDownloadUrl);
		URLConnection request = url.openConnection();
		BufferedInputStream input = new BufferedInputStream(request.getInputStream());
		int count = 0;
		final int bufferSize = 100 * 1024;
		byte[] buffer = new byte[bufferSize];

		try {
			while (-1 != (count = input.read(buffer, 0, bufferSize))) {
				output.write(buffer, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			input.close();
			output.flush();
			output.close();

		}
		appendReportZipToTsv(zipFilePath, tsvFilePath);
	}

	private File appendReportZipToTsv(String zipFilePath, String filePath) throws Exception {
		File r = new File(filePath);
		r.getParentFile().mkdirs();
		ZipInputStream zipStream = null;
		ZipEntry csvEntry = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			zipStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFilePath)));
			csvEntry = zipStream.getNextEntry();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(r), "UTF8"));
			if (csvEntry != null) {
				reader = new BufferedReader(new InputStreamReader(zipStream, "UTF8"));
				String line = reader.readLine();
				while (line != null) {
					writer.write(line);
					writer.newLine();
					line = reader.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			reader.close();
			writer.flush();
			writer.close();
			zipStream.close();
		}
		return r;
	}

	@Override
	public List<BingReportData> parse(String reportDownloadPath, String date, ReportType reportType, Account account) {
		// TODO Auto-generated method stub
		return null;
	}


}
