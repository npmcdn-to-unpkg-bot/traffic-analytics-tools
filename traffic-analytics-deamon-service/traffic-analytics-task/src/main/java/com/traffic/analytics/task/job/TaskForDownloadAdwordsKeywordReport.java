package com.traffic.analytics.task.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.AccountService;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.api.utils.ReportDownloadPathBuilder;
import com.traffic.analytics.core.model.Website;
import com.traffic.analytics.core.service.WebsiteService;
import com.traffic.analytics.task.model.Task;

/**
 *
 * @author yuhuibin
 */
public class TaskForDownloadAdwordsKeywordReport extends Task {

    @Autowired
	private ReportingService<AdwordsReportData> adwordsReportingServiceImpl;

	@Autowired
	@Qualifier("adwordsAccountServiceImpl")
	private AccountService<AdwordsAccount> adwordsAccountService;

    @Autowired
    private WebsiteService websiteService;
    
	@Override
	public void run() throws Exception {
        List<Website> websites = websiteService.findAllWebsites();
        if(CollectionUtils.isEmpty(websites)){
        	return;
        }

        for (Website website : websites) {
            String websiteId = website.getId();
            List<AdwordsAccount> adwordsAccounts = adwordsAccountService.getAccountsByWebsiteId(websiteId);
            for (AdwordsAccount adwordsAccount : adwordsAccounts) {
            	
            	String reportDownloadPath = new ReportDownloadPathBuilder(websiteId, adwordsAccount.getId(), ReportType.Keyword, super.getDate()).build();
            	// 下载报表,保存数据文件
                boolean downloadStatus = adwordsReportingServiceImpl.download(reportDownloadPath, websiteId, ReportType.Campaign, adwordsAccount);
                if (!downloadStatus) {
                    continue;
                }
            }
        }
	}
}
