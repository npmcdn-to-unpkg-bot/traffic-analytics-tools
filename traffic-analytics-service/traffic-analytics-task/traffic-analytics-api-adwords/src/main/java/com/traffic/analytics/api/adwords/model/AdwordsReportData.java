package com.traffic.analytics.api.adwords.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.api.model.ReportData;

@Document(collection="AdwordsReportData")
public class AdwordsReportData extends ReportData{

	private static final long serialVersionUID = -6933284905877847711L;

    private String source;

    private String finalUrls;

    private Float cost;

    private Integer clicks;

    private Float ctr;

    private Integer impressions;

    private Float maxcpc;

    private Float avgPosition;

    private String reportType;

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getFinalUrls() {
        return finalUrls;
    }

    public void setFinalUrls(String finalUrls) {
        this.finalUrls = finalUrls;
    }

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Float getCtr() {
        return ctr;
    }

    public void setCtr(Float ctr) {
        this.ctr = ctr;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public String getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}

    public Float getMaxcpc() {
        return maxcpc;
    }

    public void setMaxcpc(Float maxcpc) {
        this.maxcpc = maxcpc;
    }

    public Float getAvgPosition() {
        return avgPosition;
    }

    public void setAvgPosition(Float avgPosition) {
        this.avgPosition = avgPosition;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
	
}
