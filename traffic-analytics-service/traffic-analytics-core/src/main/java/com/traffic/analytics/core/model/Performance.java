package com.traffic.analytics.core.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.traffic.analytics.commons.base.model.Model;

public class Performance extends Model {

	private DBObject dbObj = new BasicDBObject();

	private static final long serialVersionUID = 1L;

	public String getWebsiteId() {
		return this.dbObj.get("websiteId").toString();
	}

	public void setWebsiteId(String websiteId) {
		this.dbObj.put("websiteId", websiteId);
	}

	public String getAccountId() {
		return this.dbObj.get("accountId").toString();
	}

	public void setAccountId(String accountId) {
		this.dbObj.put("accountId", accountId);
	}

	public String getDate() {
		return this.dbObj.get("date").toString();
	}

	public void setDate(String date) {
		this.dbObj.put("date", date);
	}

	public Float getConversions() {
		return (Float) this.dbObj.get("conversions");
	}

	public void setConversions(Float conversions) {
		this.dbObj.put("conversions", conversions);
	}

	public String getCampaignId() {
		return this.dbObj.get("campaignId").toString();
	}

	public void setCampaignId(String campaignId) {
		this.dbObj.put("campaignId", campaignId);
	}

	public String getCampaign() {
		return this.dbObj.get("campaign").toString();
	}

	public void setCampaign(String campaign) {
		this.dbObj.put("campaign", campaign);
	}

	public String getAdgroupId() {
		return this.dbObj.get("adgroupId").toString();
	}

	public void setAdgroupId(String adgroupId) {
		this.dbObj.put("adgroupId", adgroupId);
	}

	public String getAdgroup() {
		return this.dbObj.get("adgroup").toString();
	}

	public void setAdgroup(String adgroup) {
		this.dbObj.put("adgroup", adgroup);
	}

	public String getKeywordId() {
		return this.dbObj.get("keywordId").toString();
	}

	public void setKeywordId(String keywordId) {
		this.dbObj.put("keywordId", keywordId);
	}

	public String getKeyword() {
		return this.dbObj.get("keyword").toString();
	}

	public void setKeyword(String keyword) {
		this.dbObj.put("keyword", keyword);
	}

	public String getStatus() {
		return this.dbObj.get("status").toString();
	}

	public void setStatus(String status) {
		this.dbObj.put("status", status);
	}

	public String getSource() {
		return this.dbObj.get("source").toString();
	}

	public void setSource(String source) {
		this.dbObj.put("source", source);
	}

	public String getDestinationUrl() {
		return this.dbObj.get("destinationUrl").toString();
	}

	public void setDestinationUrl(String destinationUrl) {
		this.dbObj.put("destinationUrl", destinationUrl);
	}

	public Float getCost() {
		return (Float) this.dbObj.get("cost");
	}

	public void setCost(Float cost) {
		this.dbObj.put("cost", cost);
	}

	public Integer getClicks() {
		return (Integer) this.dbObj.get("clicks");
	}

	public void setClicks(Integer clicks) {
		this.dbObj.put("clicks", clicks);
	}

	public Float getCpc() {
		return (Float) this.dbObj.get("cpc");
	}

	public void setCpc(Float cpc) {
		this.dbObj.put("cpc", cpc);
	}

	public Float getCtr() {
		return (Float) this.dbObj.get("ctr");
	}

	public void setCtr(Float ctr) {
		this.dbObj.put("ctr", ctr);
	}

	public Integer getImpressions() {
		return (Integer) this.dbObj.get("impressions");
	}

	public void setImpressions(Integer impressions) {
		this.dbObj.put("impressions", impressions);
	}

	public Float getMaxCpc() {
		return (Float) this.dbObj.get("maxcpc");
	}

	public void setMaxCpc(Float maxcpc) {
		this.dbObj.put("maxcpc", maxcpc);
	}

	public Float getAvgPosition() {
		return (Float) this.dbObj.get("avgPosition");
	}

	public void setAvgPosition(Float avgPosition) {
		this.dbObj.put("avgPosition", avgPosition);
	}

	public Object getFieldValue(String key) {
		return this.dbObj.get(key);
	}

	public void setFieldValue(String key, Object value) {
		this.dbObj.put(key, value);
	}

	public DBObject getDBObject() {
		return this.dbObj;
	}

}
