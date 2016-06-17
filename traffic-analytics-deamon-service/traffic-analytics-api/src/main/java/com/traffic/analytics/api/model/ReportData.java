package com.traffic.analytics.api.model;

import com.traffic.analytics.commons.base.model.Model;

public abstract class ReportData extends Model {

	private static final long serialVersionUID = -6888959448008720440L;

	protected String websiteId;
	
	protected String date;
	
	protected String accountId;

	protected String campaignId;
	
	protected String campaignName;
	
	protected String adgroupId;
	
	protected String adgroupName;
	
	protected String keywordId;
	
	protected String keyword;
	
	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getAdgroupId() {
		return adgroupId;
	}

	public void setAdgroupId(String adgroupId) {
		this.adgroupId = adgroupId;
	}

	public String getAdgroupName() {
		return adgroupName;
	}

	public void setAdgroupName(String adgroupName) {
		this.adgroupName = adgroupName;
	}

	public String getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
