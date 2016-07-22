package com.traffic.analytics.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.traffic.analytics.commons.base.dao.DaoImpl;
import com.traffic.analytics.core.model.Website;

@Repository
public class WebsiteDaoImpl extends DaoImpl<Website> implements WebsiteDao {

	@Override
	public List<Website> findAllWebsite() {
		return super.findAll();
	}

	@Override
	public Website getWebsiteById(String websiteId) {
		return super.get(websiteId);
	}

}
