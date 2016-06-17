package com.traffic.analytics.api.ga.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.api.model.Account;

@Document(collection="GaAccount")
public class GaAccount extends Account {

	private static final long serialVersionUID = -3365878827389941246L;
    private String domain;
    private GaView gaview;
    
	public String getWebsiteId() {
		return websiteId;
	}
	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public GaView getGaview() {
		return gaview;
	}
	public void setGaview(GaView gaview) {
		this.gaview = gaview;
	}
}
