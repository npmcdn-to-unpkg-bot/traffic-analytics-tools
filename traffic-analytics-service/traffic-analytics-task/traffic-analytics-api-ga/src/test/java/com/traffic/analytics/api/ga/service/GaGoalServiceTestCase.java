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
import com.traffic.analytics.api.ga.model.GaView;
import com.traffic.analytics.api.service.AccountService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GaGoalServiceTestCase {
	
	@Autowired
	private GaGoalService gaGoalService;
	

	@Autowired
	@Qualifier("gaAccountServiceImpl")
	private AccountService<GaAccount> gaAccountServiceImpl;
	
	@Test
	public void testGetGaGoalsByGaAccount(){
		List<GaAccount> gaAccounts = gaAccountServiceImpl.getAllAccounts();
		for (GaAccount gaAccount : gaAccounts) {
			List<GaGoal> gaGoals = gaGoalService.getGaGoalsByGaAccount(gaAccount);
			for (GaGoal gaGoal : gaGoals) {
				System.out.println(gaGoal.getGoalName());
			}
		}
	}
	
	@Test
	public void testGetGaGoalsByWebsiteId(){
		List<GaGoal> gaGoals = gaGoalService.getGaGoalsByWebsiteId("575cdb77e27ca95e6357b6d7");
		for (GaGoal gaGoal : gaGoals) {
			System.out.println(gaGoal.getGoalName());
		}
	}
	
	@Test
	public void testGetGaViewsByDomain(){
		List<GaView> gaViews = gaGoalService.getGaViewsByDomain("www.igvault.fr");
		System.out.println(gaViews.size());
		for (GaView gaView : gaViews) {
			System.out.println(gaView.getName());
		}
	}
	
}