package com.traffic.analytics.core.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.commons.base.model.Model;

@Document(collection="Website")
public class Website extends Model {

	private static final long serialVersionUID = -8206071787869231399L;

	private String userId;
	
	private String domain;
	
	private String industry;
	
	private String introduction;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
}