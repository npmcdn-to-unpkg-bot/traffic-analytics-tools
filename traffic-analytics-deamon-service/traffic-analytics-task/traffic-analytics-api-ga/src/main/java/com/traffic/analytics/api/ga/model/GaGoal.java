package com.traffic.analytics.api.ga.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.commons.base.model.Model;

@Document(collection="GaGoal")
public class GaGoal extends Model {
	
	private static final long serialVersionUID = 4468315722135876550L;
	
	private String websiteId;
	
	private String accountId;
	
	private String propertyId;
	
	private String profileId;
	
	private String goalId;
	
	private String goalName;

	private Float goalValue;

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

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getGoalId() {
		return goalId;
	}

	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public Float getGoalValue() {
		return goalValue;
	}

	public void setGoalValue(Float goalValue) {
		this.goalValue = goalValue;
	}
}