package com.traffic.analytics.api.model;

import com.traffic.analytics.commons.base.model.Model;

public abstract class Account extends Model {

	private static final long serialVersionUID = -1066858012516413895L;

	protected String websiteId;

	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	
}
