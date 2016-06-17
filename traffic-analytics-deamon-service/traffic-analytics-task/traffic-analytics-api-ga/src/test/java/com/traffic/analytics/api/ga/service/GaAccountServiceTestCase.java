package com.traffic.analytics.api.ga.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.service.AccountService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GaAccountServiceTestCase {
	
	@Autowired
	@Qualifier("gaAccountServiceImpl")
	private AccountService<GaAccount> gaAccountServiceImpl;
	
	@Test
	public void testGetGaGoalsByGaAccount(){
		List<GaAccount> gaAccounts = gaAccountServiceImpl.getAllAccounts();
		for (GaAccount gaAccount : gaAccounts) {
			System.out.println(gaAccount.getDomain());
		}
	
	}
	
}