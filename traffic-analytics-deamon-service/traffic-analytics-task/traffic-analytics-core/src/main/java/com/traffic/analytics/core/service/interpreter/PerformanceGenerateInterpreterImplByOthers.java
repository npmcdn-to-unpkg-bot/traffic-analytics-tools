package com.traffic.analytics.core.service.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaReportData;
import com.traffic.analytics.core.model.Performance;

@Service("performanceGenerateInterpreterImplByAdwords")
public class PerformanceGenerateInterpreterImplByOthers extends PerformanceGenerateInterpreterAbstract {

	private String others = "others";
	
	@Override
	public void generatePerformanceByDiffSource(String websiteId, String date) {
		List<GaReportData> allGaReportDatas = super.getGaReportData(websiteId, date);
		List<GaReportData> gaReportDatas = new ArrayList<GaReportData>();
		
		for (GaReportData gaReportData : allGaReportDatas) {
			if(!gaReportData.getSource().equals("google") && !gaReportData.getSource().equals("bing") && !gaReportData.getSource().equals("baidu") ){
				gaReportDatas.add(gaReportData);
			}
		}
        
		List<GaGoal> fillGaGoals = super.gaGoalServiceImpl.getGaGoalsByWebsiteId(websiteId);
		
		List<Performance> keywords = new ArrayList<Performance>();
		for (GaReportData gaReportData : gaReportDatas) {
			Performance performance = new Performance();
			performance.setWebsiteId(websiteId);
			performance.setAccountId(others);
			performance.setDate(date);
			performance.setSource(others);
			performance.setCampaignId(others);
			performance.setCampaign(others);
			performance.setAdgroupId(others);
			performance.setAdgroup(others);
			performance.setKeywordId(others);
			performance.setKeyword(others);
			performance.setClicks(0);
			performance.setCost(0F);
			performance.setImpressions(0);
            performance.setMaxCpc(0F);
            performance.setAvgPosition(0F);

			Float conversions = 0F;
			// 对Ga的数据进行转换
			if (gaReportData != null) {
				List<GaGoal> goals = gaReportData.getGaGoals();
				if (!CollectionUtils.isEmpty(goals)) {
					for (GaGoal gaGoal : goals) {
						performance.setFieldValue(gaGoal.getGoalId(), gaGoal.getGoalValue());
						conversions += gaGoal.getGoalValue();
					}
				}
			} else {
				// 填充GaGoal
				for (GaGoal gaGoal : fillGaGoals) {
					performance.setFieldValue(gaGoal.getGoalId(), 0F);
				}
			}

			performance.setConversions(conversions);
			keywords.add(performance);
		}

		// 循环list并把KeywordPerformance保存入库
		for (Performance performance : keywords) {
			performanceDao.insertKeyword(performance.getDBObject());
		}

	}
}
