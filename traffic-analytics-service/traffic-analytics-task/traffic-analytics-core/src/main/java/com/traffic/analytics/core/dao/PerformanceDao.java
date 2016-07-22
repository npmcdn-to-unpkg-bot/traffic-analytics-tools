package com.traffic.analytics.core.dao;

import java.util.List;

import com.mongodb.DBObject;

public interface PerformanceDao {
 	
	void deleteKeywordsByWebsiteIdAndDate(String websiteId, String date);
	void deleteAdGroupsByWebsiteIdAndDate(String websiteId, String date);
	void deleteCampaignsByWebsiteIdAndDate(String websiteId, String date);
	void deleteDailyByWebsiteIdAndDate(String websiteId, String date);

	void insertKeyword(DBObject keyword);
	void insertAdGroup(DBObject adgroup);
	void insertCampaign(DBObject campaign);
	void insertDaily(DBObject campaign);
	
	List<DBObject> aggregateKeywordsConvertToAdGroup(String websiteId, String date);
	List<DBObject> aggregateKeywordsConvertToCampaign(String websiteId, String date);
	List<DBObject> aggregateKeywordsConvertToDaily(String websiteId, String date);
}
