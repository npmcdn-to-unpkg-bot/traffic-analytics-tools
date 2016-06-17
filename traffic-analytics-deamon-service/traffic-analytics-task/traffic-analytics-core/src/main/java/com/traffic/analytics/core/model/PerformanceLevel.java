package com.traffic.analytics.core.model;

public enum PerformanceLevel {

	Keyword("Keyword"), AdGroup("AdGroup"), Campaign("Campaign"), Daily("Daily");

	private String level;

	private PerformanceLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}
}
