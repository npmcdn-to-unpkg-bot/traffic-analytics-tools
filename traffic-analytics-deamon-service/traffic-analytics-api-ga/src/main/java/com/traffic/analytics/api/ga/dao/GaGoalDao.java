package com.traffic.analytics.api.ga.dao;

import java.util.List;

import com.traffic.analytics.api.ga.model.GaGoal;

public interface GaGoalDao {

	public GaGoal insertGaGoal(GaGoal gaGoal);
	
	public List<GaGoal> getGaGoalsByWebsiteId(String websiteId);

	public void deleteGaGoalsByWebsiteId(String websiteId);

	
}
