package com.traffic.analytics.core.dao.utils;

import com.traffic.analytics.core.model.PerformanceLevel;

public class PerformanceCollectionNameBuilder {

	private PerformanceLevel performanceLevel;
	
	public PerformanceCollectionNameBuilder(PerformanceLevel performanceLevel){
		this.performanceLevel = performanceLevel;
	}

	public String build(){
		String collectionName = null;
		switch(performanceLevel.getLevel().toString()){
			case "Keyword" :
				collectionName = "KeywordPerformance";
				break;
			case "AdGroup":
				collectionName = "AdGroupPerformance";
				break;
			case "Campaign":
				collectionName = "CampaignPerformance";
				break;
			case "Daily":
				collectionName = "DailyPerformance";
				break;
		}
		return collectionName;
	}
}
