package com.traffic.analytics.api.ga.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.commons.base.dao.DaoImpl;

@Repository
public class GaGoalDaoImpl extends DaoImpl<GaGoal> implements GaGoalDao {

	@Override
	public GaGoal insertGaGoal(GaGoal gaGoal) {
		return super.insert(gaGoal);
	}

	@Override
	public List<GaGoal> getGaGoalsByWebsiteId(String websiteId) {
		Query query = new Query();
		query.addCriteria(new Criteria("websiteId").is(websiteId));
		return super.find(query);
	}

	@Override
	public void deleteGaGoalsByWebsiteId(String websiteId) {
		Query query = new Query();
		query.addCriteria(new Criteria("websiteId").is(websiteId));
		super.delete(query);		
	}
}