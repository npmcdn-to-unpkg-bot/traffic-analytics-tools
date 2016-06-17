package com.traffic.analytics.api.adwords;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.AccountService;
import com.traffic.analytics.api.service.ReportingService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdwordsReportingServiceTestCase {
	
	@Autowired
	@Qualifier("adwordsReportingServiceImpl")
	private ReportingService<AdwordsReportData> adwordsReportingServiceImpl;
	
	@Autowired
	@Qualifier("adwordsAccountServiceImpl")
	private AccountService<AdwordsAccount> adwordsAccountServiceImpl;
	
	@Test
	public void testGetGaGoalsByGaAccount(){
		List<AdwordsAccount> adwordsAccounts = adwordsAccountServiceImpl.getAllAccounts();
		for (AdwordsAccount adwordsAccount : adwordsAccounts) {
			try {
				//adwordsReportingServiceImpl.download("/Users/Sean/Adwords20160610", "20160610", ReportType.Keyword, adwordsAccount);
				List<AdwordsReportData> adwordsReportDatas = adwordsReportingServiceImpl.parse("/Users/Sean/Adwords20160610", "20160610", ReportType.Keyword, adwordsAccount);
				for (AdwordsReportData adwordsReportData : adwordsReportDatas) {
					System.out.println(adwordsReportData.getCost() + "--->" + adwordsReportData.getClicks());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
	}
	
}