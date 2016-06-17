package com.traffic.analytics.core.service.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.ga.model.GaReportData;
import com.traffic.analytics.api.ga.service.GaGoalService;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.AccountService;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.api.utils.ReportDownloadPathBuilder;
import com.traffic.analytics.core.dao.PerformanceDao;
import com.traffic.analytics.core.service.WebsiteService;
import com.traffic.analytics.core.service.strategy.PerformanceGenerateStrategy;

public abstract class PerformanceGenerateInterpreterAbstract implements PerformanceGenerateInterpreter {

	@Autowired
	protected PerformanceDao performanceDao;
	
	@Autowired
	protected WebsiteService websiteService;

	@Autowired
	@Qualifier("gaGoalServiceImpl")
	protected GaGoalService gaGoalServiceImpl;
	
    @Autowired
    @Qualifier("gaReportingServiceImpl")
    protected ReportingService<GaReportData> gaReportingServiceImpl;

	@Autowired
	@Qualifier("gaAccountServiceImpl")
	protected AccountService<GaAccount> gaAccountServiceImpl;
	

	@Autowired
	protected PerformanceGenerateStrategy performanceGenerateStrategy;
	
	protected List<GaReportData> getGaReportData(String websiteId, String date){
		List<GaReportData> r = new ArrayList<GaReportData>();
		List<GaAccount> gaAccounts = gaAccountServiceImpl.getAccountsByWebsiteId(websiteId);

        if(CollectionUtils.isEmpty(gaAccounts)){
        	return r;
        }
        
        String gaReportDownloadPath = new ReportDownloadPathBuilder(websiteId, gaAccounts.get(0).getId(), ReportType.Keyword, websiteId).build();
        r = gaReportingServiceImpl.parse(gaReportDownloadPath, date, ReportType.Keyword, gaAccounts.get(0));
        return r;
	}

}
