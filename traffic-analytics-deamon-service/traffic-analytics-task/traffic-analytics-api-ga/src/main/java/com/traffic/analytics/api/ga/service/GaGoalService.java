package com.traffic.analytics.api.ga.service;

import java.util.List;

import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaView;
import com.traffic.analytics.api.model.Account;

public interface GaGoalService {
	
	public List<GaGoal> getGaGoalsByGaAccount(Account account);
	
	public List<GaGoal> getGaGoalsByWebsiteId(String websiteId);
	
	public List<GaView> getGaViewsByDomain(String domain);
}