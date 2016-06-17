package com.traffic.analytics.api.service;

import com.traffic.analytics.api.model.Account;

public interface ApiAuthManager<T> {

	public T generateApiAccessAuth(Account account) throws Exception;
	
}
