package com.traffic.analytics.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;
import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.service.GaGoalService;
import com.traffic.analytics.commons.base.dao.DaoImpl;
import com.traffic.analytics.core.dao.utils.PerformanceAggregationGroupBuilder;
import com.traffic.analytics.core.dao.utils.PerformanceAggregationProjectBuilder;
import com.traffic.analytics.core.dao.utils.PerformanceCollectionNameBuilder;
import com.traffic.analytics.core.model.Performance;
import com.traffic.analytics.core.model.PerformanceLevel;

@Repository
public class PerformanceDaoImpl extends DaoImpl<Performance> implements PerformanceDao {

	@Autowired
	private GaGoalService gaGoalService;

	@Override
	public void deleteKeywordsByWebsiteIdAndDate(String websiteId, String date) {
		this.deleteByWebsiteIdAndDate(PerformanceLevel.Keyword, websiteId, date);
	}

	@Override
	public void deleteAdGroupsByWebsiteIdAndDate(String websiteId, String date) {
		this.deleteByWebsiteIdAndDate(PerformanceLevel.AdGroup, websiteId, date);
	}

	@Override
	public void deleteCampaignsByWebsiteIdAndDate(String websiteId, String date) {
		this.deleteByWebsiteIdAndDate(PerformanceLevel.Campaign, websiteId, date);
	}
	
	@Override
	public void deleteDailyByWebsiteIdAndDate(String websiteId, String date) {
		this.deleteByWebsiteIdAndDate(PerformanceLevel.Daily, websiteId, date);
	}
	
	private void deleteByWebsiteIdAndDate(PerformanceLevel performanceLevel, String websiteId, String date){
		String collectionName = new PerformanceCollectionNameBuilder(performanceLevel).build();
		Query query = new Query();
		query.addCriteria(Criteria.where("websiteId").is(websiteId).and("date").is(date));
		super.mongoTemplate.remove(query, collectionName);
	}
	
	@Override
	public void insertKeyword(DBObject keyword) {
		this.insert(PerformanceLevel.Keyword, keyword);
	}

	@Override
	public void insertAdGroup(DBObject adgroup) {
		this.insert(PerformanceLevel.AdGroup, adgroup);		
	}

	@Override
	public void insertCampaign(DBObject campaign) {
		this.insert(PerformanceLevel.Campaign, campaign);		
	}

	@Override
	public void insertDaily(DBObject daily) {
		this.insert(PerformanceLevel.Daily, daily);		
	}
	
	private void insert(PerformanceLevel performanceLevel, DBObject dbObj){
		String collectionName = new PerformanceCollectionNameBuilder(performanceLevel).build();
		super.mongoTemplate.insert(dbObj, collectionName);
	}
	
	
	private List<AggregationOperation> getAggregationOperations(PerformanceLevel performanceLevel, String websiteId, String date) {
        List<GaGoal> gaGoals = gaGoalService.getGaGoalsByWebsiteId(websiteId);
        String[] projectFields = new PerformanceAggregationProjectBuilder(performanceLevel, gaGoals).build();
        String[] groupFields = new PerformanceAggregationGroupBuilder(performanceLevel).build();

        AggregationOperation project = Aggregation.project(projectFields);
        Criteria criteria = Criteria.where("websiteId").is(websiteId).and("date").is(date);
        AggregationOperation match = Aggregation.match(criteria);
        GroupOperation group = Aggregation.group(groupFields).sum("cost").as("cost").sum("clicks").as("clicks").sum("impressions").as("impressions").sum("conversions").as("conversions");

        List<AggregationOperation> aggregationOperations = new ArrayList<AggregationOperation>();
        aggregationOperations.add(project);
        aggregationOperations.add(match);
        aggregationOperations.add(group);
		return aggregationOperations;
	}

	@Override
	public List<DBObject> aggregateKeywordsConvertToAdGroup(String websiteId, String date) {
		String collectionName = new PerformanceCollectionNameBuilder(PerformanceLevel.Keyword).build();
        List<AggregationOperation> aggregationOperations = this.getAggregationOperations(PerformanceLevel.AdGroup, websiteId, date);
		List<DBObject> list = super.aggregate(collectionName, aggregationOperations);
		return list;
	}

	@Override
	public List<DBObject> aggregateKeywordsConvertToCampaign(String websiteId, String date) {
		String collectionName = new PerformanceCollectionNameBuilder(PerformanceLevel.Keyword).build();
        List<AggregationOperation> aggregationOperations = this.getAggregationOperations(PerformanceLevel.Campaign, websiteId, date);
		List<DBObject> list = super.aggregate(collectionName, aggregationOperations);
		return list;
	}

	@Override
	public List<DBObject> aggregateKeywordsConvertToDaily(String websiteId, String date) {
		String collectionName = new PerformanceCollectionNameBuilder(PerformanceLevel.Keyword).build();
        List<AggregationOperation> aggregationOperations = this.getAggregationOperations(PerformanceLevel.Daily, websiteId, date);
		List<DBObject> list = super.aggregate(collectionName, aggregationOperations);
		return list;
	}
}