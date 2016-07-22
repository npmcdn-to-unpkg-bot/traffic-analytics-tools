package com.traffic.analytics.api.ga.model;

import java.util.List;

import com.traffic.analytics.api.model.ReportData;

public class GaReportData extends ReportData{

	private static final long serialVersionUID = -6515647771726445301L;


	private String websiteId;
	private String date;
	private String campaign;
    private String adGroup;
    private String keyword;
    private String source;
    private List<GaGoal> gaGoals;
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
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getAdGroup() {
		return adGroup;
	}
	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<GaGoal> getGaGoals() {
		return gaGoals;
	}
	public void setGaGoals(List<GaGoal> gaGoals) {
		this.gaGoals = gaGoals;
	}
}
