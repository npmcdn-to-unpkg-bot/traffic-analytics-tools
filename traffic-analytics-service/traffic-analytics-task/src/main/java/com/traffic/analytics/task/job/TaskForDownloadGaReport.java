package com.traffic.analytics.task.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.ga.model.GaReportData;
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
public class TaskForDownloadGaReport extends Task {

    Log log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("gaReportingServiceImpl")
    private ReportingService<GaReportData> gaReportingServiceImpl;

	@Autowired
	@Qualifier("gaAccountServiceImpl")
	private AccountService<GaAccount> gaAccountServiceImpl;
    
    @Autowired
    private WebsiteService websiteService;

    @Override
    public void run() throws Exception {
        List<Website> websites = websiteService.findAllWebsites();
        if(CollectionUtils.isEmpty(websites)){
        	return;
        }
        
        log.info("websites:" + websites);

        for (Website website : websites) {
            String websiteId = website.getId();
            List<GaAccount> gaAccounts = gaAccountServiceImpl.getAccountsByWebsiteId(websiteId);
            for (GaAccount gaAccount : gaAccounts) {
            	
            	String reportDownloadPath = new ReportDownloadPathBuilder(websiteId, gaAccount.getId(), ReportType.Keyword, super.getDate()).build();
            	// 下载报表,保存数据文件
                boolean downloadStatus = gaReportingServiceImpl.download(reportDownloadPath, websiteId, null, gaAccount);
                if (!downloadStatus) {
                    continue;
                }
            }
        }
    }

}
