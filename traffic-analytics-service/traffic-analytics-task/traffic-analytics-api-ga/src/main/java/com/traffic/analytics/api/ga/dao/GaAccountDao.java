package com.traffic.analytics.api.ga.dao;

import java.util.List;

import com.traffic.analytics.api.ga.model.GaAccount;

public interface GaAccountDao {

	public GaAccount getGaAccountByWebsiteId(String websiteId);
	
	public GaAccount getGaAccountById(String id);
	
	public List<GaAccount> findAllGaAccounts();
	
}
