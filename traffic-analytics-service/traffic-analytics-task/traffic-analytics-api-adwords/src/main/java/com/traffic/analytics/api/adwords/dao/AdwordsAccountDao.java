package com.traffic.analytics.api.adwords.dao;

import java.util.List;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;

public interface AdwordsAccountDao {

	public List<AdwordsAccount> getAllAdwordsAccounts();
	public List<AdwordsAccount> getAdwordsAccountByWebsiteId(String websiteId);
    public AdwordsAccount getAdwordsAccountById(String accountId);
}
