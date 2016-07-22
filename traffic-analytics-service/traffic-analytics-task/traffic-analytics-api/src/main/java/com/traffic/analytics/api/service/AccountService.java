package com.traffic.analytics.api.service;

import java.util.List;

import com.traffic.analytics.api.model.Account;

public interface AccountService<T extends Account> {

	public List<T> getAllAccounts();
	
	public List<T> getAccountsByWebsiteId(String websiteId);
	
	public T getAccountById(String accountId);

}