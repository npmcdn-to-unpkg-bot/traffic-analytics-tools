package com.traffic.analytics.core.service.strategy;

import java.util.List;
import java.util.Map;

import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.ga.model.GaReportData;

public interface PerformanceGenerateStrategy {

	public Map<String,AdwordsReportData> getAdwordsDataMap(List<AdwordsReportData> adwordsReportDatas);
	
	public Map<String,GaReportData> getGaDataMap(List<GaReportData> gaReportDatas);
	
}
