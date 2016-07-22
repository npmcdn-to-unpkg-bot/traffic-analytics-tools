package com.traffic.analytics.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traffic.analytics.core.dao.WebsiteDao;
import com.traffic.analytics.core.model.Website;

@Service
public class WebsiteServiceImpl implements WebsiteService {

	@Autowired
	private WebsiteDao websiteDao;
	
	@Override
	public List<Website> findAllWebsites() {
		return websiteDao.findAllWebsite();
	}

	@Override
	public Website getWebsiteById(String websiteId) {
		return websiteDao.getWebsiteById(websiteId);
	}

}
