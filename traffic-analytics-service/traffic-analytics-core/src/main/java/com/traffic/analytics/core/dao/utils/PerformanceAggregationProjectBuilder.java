package com.traffic.analytics.core.dao.utils;

import java.util.ArrayList;
import java.util.List;

import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.core.model.PerformanceLevel;

public class PerformanceAggregationProjectBuilder {

	private PerformanceLevel performanceLevel;
	private List<GaGoal> gaGoals;
	public PerformanceAggregationProjectBuilder(PerformanceLevel performanceLevel, List<GaGoal> gaGoals){
		this.performanceLevel = performanceLevel;
		this.gaGoals = gaGoals;
	}
	
	public String[] build(){
		List<String> fields = new ArrayList<String>();
		fields.add("websiteId");
		fields.add("date");
		fields.add("accountId");
		fields.add("campaignId");
		fields.add("campaign");
		fields.add("cost");
		fields.add("clicks");
		fields.add("impressions");
		fields.add("conversions");
        fields.add("vender");

        if(performanceLevel.equals(PerformanceLevel.AdGroup)){
			fields.add("adgroupId");
			fields.add("adgroup");	
		}
		
		if(performanceLevel.equals(PerformanceLevel.Keyword)){
			fields.add("adgroupId");
			fields.add("adgroup");
			fields.add("keywordId");
			fields.add("keyword");
			fields.add("source");
			fields.add("destinationUrl");
            fields.add("maxcpc");
            fields.add("avgPosition");
		}
		
		for (GaGoal gaGoal : gaGoals) {
			fields.add(gaGoal.getGoalId());
		}
		
		return fields.toArray(new String[fields.size()]);

	}
	
	
}
