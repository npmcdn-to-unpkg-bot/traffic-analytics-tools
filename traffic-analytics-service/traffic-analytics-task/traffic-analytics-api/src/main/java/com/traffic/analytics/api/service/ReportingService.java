package com.traffic.analytics.api.service;

import java.util.List;

import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.model.ReportData;
import com.traffic.analytics.api.model.ReportType;

public interface ReportingService<T extends ReportData> {
	
	public boolean download(String reportDownloadPath, String date, ReportType reportType, Account account) throws Exception;
	
	public List<T> parse(String reportDownloadPath, String date, ReportType reportType, Account account);
	
}