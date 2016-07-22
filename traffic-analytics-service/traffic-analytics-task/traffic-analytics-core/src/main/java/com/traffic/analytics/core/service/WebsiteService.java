package com.traffic.analytics.core.service;

import java.util.List;

import com.traffic.analytics.core.model.Website;

public interface WebsiteService {

	List<Website> findAllWebsites();
	
	Website getWebsiteById(String websiteId);
	
}
