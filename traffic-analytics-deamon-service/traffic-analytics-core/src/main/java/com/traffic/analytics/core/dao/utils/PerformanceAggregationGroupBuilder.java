package com.traffic.analytics.core.dao.utils;

import java.util.ArrayList;
import java.util.List;

import com.traffic.analytics.core.model.PerformanceLevel;

public class PerformanceAggregationGroupBuilder {

	private PerformanceLevel performanceLevel;
	
	public PerformanceAggregationGroupBuilder(PerformanceLevel performanceLevel){
		this.performanceLevel = performanceLevel;
	}
	
	public String[] build(){
		List<String> fields = new ArrayList<String>();
		fields.add("websiteId");
		fields.add("date");
		
		if(performanceLevel.equals(PerformanceLevel.Campaign)){
			fields.add("accountId");
			fields.add("campaignId");
			fields.add("campaign");	
		}
		
		if(performanceLevel.equals(PerformanceLevel.AdGroup)){
			fields.add("accountId");
			fields.add("campaignId");
			fields.add("campaign");	
			fields.add("adgroupId");
			fields.add("adgroup");	
		}
		
		if(performanceLevel.equals(PerformanceLevel.Keyword)){
			fields.add("accountId");
			fields.add("campaignId");
			fields.add("campaign");	
			fields.add("adgroupId");
			fields.add("adgroup");
			fields.add("keywordId");
			fields.add("keyword");
			fields.add("source");
			fields.add("destinationUrl");
            fields.add("maxcpc");
            fields.add("avgPosition");
        }
		
		return fields.toArray(new String[fields.size()]);
	}
	
	
}
