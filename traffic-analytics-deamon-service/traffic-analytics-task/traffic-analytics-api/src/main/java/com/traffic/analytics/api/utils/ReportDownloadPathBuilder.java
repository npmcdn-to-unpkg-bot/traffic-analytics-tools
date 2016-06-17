package com.traffic.analytics.api.utils;

import com.traffic.analytics.api.model.ReportType;

public class ReportDownloadPathBuilder {

	private String reportDownloadPath;
    private String reportDownloadPathTemplate = "/adeaz/sem/semdata/WEBSITE_ID/adwords/REPORT_TYPE/ACCOUNT_ID/DATE";
    
	public ReportDownloadPathBuilder(String websiteId, String accountId, ReportType reportType, String date){
		this.reportDownloadPath = reportDownloadPathTemplate.replace("WEBSITE_ID", websiteId).replace("ACCOUNT_ID", accountId).replace("REPORT_TYPE", "keyword").replace("DATE",date);
	}
	
	public String build(){
		return this.reportDownloadPath; 
	}
	
}
