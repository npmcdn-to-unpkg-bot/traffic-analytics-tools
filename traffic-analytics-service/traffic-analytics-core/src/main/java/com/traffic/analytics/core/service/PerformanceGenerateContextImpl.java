package com.traffic.analytics.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.traffic.analytics.core.dao.PerformanceDao;
import com.traffic.analytics.core.service.interpreter.PerformanceGenerateInterpreter;

@Service
public class PerformanceGenerateContextImpl implements PerformanceGenerateContext {

	@Autowired
	private PerformanceDao performanceDao;
	
	@Autowired
	@Qualifier("performanceGenerateInterpreterImplByAdwords")
	private PerformanceGenerateInterpreter performanceGenerateInterpreterImplByAdwords;
	
	@Autowired
	@Qualifier("performanceGenerateInterpreterImplByOthers")
	private PerformanceGenerateInterpreter performanceGenerateInterpreterImplByOthers;
	
	@Override
	public void generatePerformanceReport(String websiteId, String date) {
		// 删除之前的数据
		performanceDao.deleteKeywordsByWebsiteIdAndDate(websiteId, date);
		performanceGenerateInterpreterImplByAdwords.generatePerformanceByDiffSource(websiteId, date);
		performanceGenerateInterpreterImplByOthers.generatePerformanceByDiffSource(websiteId, date);
		
		this.generateAdGroupPerformanceReport(websiteId, date);
		this.generateCampaignPerformanceReport(websiteId, date);
		this.generateDailyPerformanceReport(websiteId, date);
	}
	
	private void generateAdGroupPerformanceReport(String websiteId, String date) {
		List<DBObject> adgroups = performanceDao.aggregateKeywordsConvertToAdGroup(websiteId, date);
		performanceDao.deleteAdGroupsByWebsiteIdAndDate(websiteId, date);
		for (DBObject adgroup : adgroups) {
			performanceDao.insertAdGroup(adgroup);
		}
	}

	private void generateCampaignPerformanceReport(String websiteId, String date) {
		List<DBObject> campaigns = performanceDao.aggregateKeywordsConvertToCampaign(websiteId, date);
		performanceDao.deleteCampaignsByWebsiteIdAndDate(websiteId, date);
		for (DBObject campaign : campaigns) {
			performanceDao.insertCampaign(campaign);
		}
	}

	private void generateDailyPerformanceReport(String websiteId, String date) {
		List<DBObject> campaigns = performanceDao.aggregateKeywordsConvertToDaily(websiteId, date);
		performanceDao.deleteDailyByWebsiteIdAndDate(websiteId, date);
		performanceDao.insertDaily(campaigns.get(0));
	}

}
