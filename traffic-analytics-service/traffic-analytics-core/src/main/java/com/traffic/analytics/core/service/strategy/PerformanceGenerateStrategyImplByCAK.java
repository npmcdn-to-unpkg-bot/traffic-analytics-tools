package com.traffic.analytics.core.service.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.ga.model.GaReportData;

@Service
public class PerformanceGenerateStrategyImplByCAK implements PerformanceGenerateStrategy {

	@Override
	public Map<String, AdwordsReportData> getAdwordsDataMap(List<AdwordsReportData> adwordsReportDatas) {
		Map<String, AdwordsReportData> map = new HashMap<String, AdwordsReportData>();
		for (AdwordsReportData adwordsReportData : adwordsReportDatas) {
			String key = adwordsReportData.getCampaignName() + adwordsReportData.getAdgroupName() + adwordsReportData.getKeyword();
			map.put(key, adwordsReportData);
		}
		return map;
	}

	@Override
	public Map<String, GaReportData> getGaDataMap(List<GaReportData> gaReportDatas) {
		Map<String, GaReportData> map = new HashMap<String, GaReportData>();
		for (GaReportData gaReportData : gaReportDatas) {
			String key = gaReportData.getCampaign() + gaReportData.getAdGroup() + gaReportData.getKeyword();
			map.put(key, gaReportData);
		}
		return map;
	}
}
