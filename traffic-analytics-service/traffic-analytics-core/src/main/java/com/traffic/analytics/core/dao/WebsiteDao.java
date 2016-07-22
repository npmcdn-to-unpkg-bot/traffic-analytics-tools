package com.traffic.analytics.core.dao;

import java.util.List;

import com.traffic.analytics.core.model.Website;

public interface WebsiteDao {

	public List<Website> findAllWebsite();
	
	public Website getWebsiteById(String websiteId);
	
}
