package com.traffic.analytics.api.adwords.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traffic.analytics.api.adwords.dao.AdwordsAccountDao;
import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.service.AccountService;

@Service("adwordsAccountServiceImpl")
public class AdwordsAccountServiceImpl implements AccountService<AdwordsAccount> {

	@Autowired
	private AdwordsAccountDao adwordsAccountDao;
	
	@Override
	public List<AdwordsAccount> getAllAccounts() {
		return adwordsAccountDao.getAllAdwordsAccounts();
	}

	@Override
	public List<AdwordsAccount> getAccountsByWebsiteId(String websiteId) {
		return adwordsAccountDao.getAdwordsAccountByWebsiteId(websiteId);
	}

	@Override
	public AdwordsAccount getAccountById(String accountId) {
		return adwordsAccountDao.getAdwordsAccountById(accountId);
	}

}
