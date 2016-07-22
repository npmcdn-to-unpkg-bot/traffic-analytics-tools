package com.traffic.analytics.api.model;

public enum Vendor {
	
	GoogleAnalytics("Ga"),Baidu("Baidu"),AdWords("AdWords"),Bing("Bing");
	
	private String name;
	
	private Vendor(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
