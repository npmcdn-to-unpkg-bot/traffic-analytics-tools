package com.traffic.analytics.api.model;

public enum ReportType {
    
	Keyword("keyword"),Content("content"),Campaign("campaign");
	
	private String type;
	
	private ReportType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}
}
