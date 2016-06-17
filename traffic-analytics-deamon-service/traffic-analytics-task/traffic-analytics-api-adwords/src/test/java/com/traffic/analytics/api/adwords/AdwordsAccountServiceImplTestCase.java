package com.traffic.analytics.api.adwords;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.service.AccountService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdwordsAccountServiceImplTestCase {
	
	@Autowired
	@Qualifier("adwordsAccountServiceImpl")
	private AccountService<AdwordsAccount> adwordsAccountServiceImpl;
	
	@Test
	public void testGetGaGoalsByGaAccount(){
		List<AdwordsAccount> adwordsAccounts = adwordsAccountServiceImpl.getAllAccounts();
		System.out.println(adwordsAccounts.size());
	}
	
}