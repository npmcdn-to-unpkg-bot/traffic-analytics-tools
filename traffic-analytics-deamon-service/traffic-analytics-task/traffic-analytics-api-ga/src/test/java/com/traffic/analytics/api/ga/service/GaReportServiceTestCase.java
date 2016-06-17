package com.traffic.analytics.api.ga.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaReportData;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.AccountService;
import com.traffic.analytics.api.service.ReportingService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GaReportServiceTestCase {

	@Autowired
	@Qualifier("gaReportingServiceImpl")
	private ReportingService<GaReportData> gaReportingServiceImpl;

	@Autowired
	@Qualifier("gaAccountServiceImpl")
	private AccountService<GaAccount> gaAccountServiceImpl;
	
	@Test
	public void testDownload() throws Exception{
		List<GaAccount> gaAccounts = gaAccountServiceImpl.getAllAccounts();
		for (GaAccount gaAccount : gaAccounts) {
			gaReportingServiceImpl.download("/Users/Sean/20160610", "20160610", ReportType.Keyword, gaAccount);
		}
	}
	
	@Test
	public void testParse() throws Exception {
		List<GaAccount> gaAccounts = gaAccountServiceImpl.getAllAccounts();
		for (GaAccount gaAccount : gaAccounts) {
			List<GaReportData> gaReportDatas = gaReportingServiceImpl.parse("/Users/Sean/20160610", "20160610", ReportType.Keyword, gaAccount);
			for (GaReportData gaReportData : gaReportDatas) {
				List<GaGoal> gaGoals = gaReportData.getGaGoals();
				System.out.println("======");
				for (GaGoal gaGoal : gaGoals) {
					System.out.print(gaGoal.getGoalId() + "--->" + gaGoal.getGoalValue() + ",");
				}
			}
		}
	}
}