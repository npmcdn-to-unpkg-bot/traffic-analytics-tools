package com.traffic.analytics.core.service.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.adwords.model.AdwordsReportData;
import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaReportData;
import com.traffic.analytics.api.model.ReportType;
import com.traffic.analytics.api.service.AccountService;
import com.traffic.analytics.api.service.ReportingService;
import com.traffic.analytics.api.utils.ReportDownloadPathBuilder;
import com.traffic.analytics.core.model.Performance;

@Service("performanceGenerateInterpreterImplByAdwords")
public class PerformanceGenerateInterpreterImplByAdwords extends PerformanceGenerateInterpreterAbstract {

	@Autowired
	@Qualifier("adwordsAccountServiceImpl")
	private AccountService<AdwordsAccount> adwordsAccountService;

    @Autowired
    @Qualifier("adwordsReportingServiceImpl")
	private ReportingService<AdwordsReportData> adwordsReportingServiceImpl;
	
	private String GaSource = "google";
	
	@Override
	public void generatePerformanceByDiffSource(String websiteId, String date) {
		List<GaReportData> allGaReportDatas = super.getGaReportData(websiteId, date);
		List<GaReportData> gaReportDatas = new ArrayList<GaReportData>();
		for (GaReportData gaReportData : allGaReportDatas) {
			if(gaReportData.getSource().equals(GaSource)){
				gaReportDatas.add(gaReportData);
			}
		}
		
        List<AdwordsReportData> adwordsReportDatas = this.getAdwordsReportData(websiteId, date);
        
        Map<String,GaReportData> gaCAKMap = super.performanceGenerateStrategy.getGaDataMap(gaReportDatas);
		Map<String,AdwordsReportData> adwordsCAKMap = super.performanceGenerateStrategy.getAdwordsDataMap(adwordsReportDatas);
		List<GaGoal> fillGaGoals = super.gaGoalServiceImpl.getGaGoalsByWebsiteId(websiteId);
		
		List<Performance> keywords = new ArrayList<Performance>();
		for (Entry<String, AdwordsReportData> entry : adwordsCAKMap.entrySet()) {
			String key = entry.getKey();
			AdwordsReportData adwordsReportData = entry.getValue();
			GaReportData gaReportData = gaCAKMap.get(key);
			//TODO
			Performance performance = new Performance();
			performance.setWebsiteId(adwordsReportData.getWebsiteId());
			performance.setAccountId(adwordsReportData.getAccountId());
			performance.setDate(adwordsReportData.getDate());
			performance.setSource(GaSource);
			performance.setCampaignId(adwordsReportData.getCampaignId());
			performance.setCampaign(adwordsReportData.getCampaignName());
			performance.setAdgroupId(adwordsReportData.getAdgroupId());
			performance.setAdgroup(adwordsReportData.getAdgroupName());
			performance.setKeywordId(adwordsReportData.getKeywordId());
			performance.setKeyword(adwordsReportData.getKeyword());
			performance.setClicks(adwordsReportData.getClicks());
			performance.setCost(adwordsReportData.getCost());
			performance.setImpressions(adwordsReportData.getImpressions());
            performance.setMaxCpc(adwordsReportData.getMaxcpc());
            performance.setAvgPosition(adwordsReportData.getAvgPosition());

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

        // 删除之前的数据
		performanceDao.deleteKeywordsByWebsiteIdAndDate(websiteId, date);
		// 循环list并把KeywordPerformance保存入库
		for (Performance performance : keywords) {
			performanceDao.insertKeyword(performance.getDBObject());
		}

	}

	
	private List<AdwordsReportData> getAdwordsReportData(String websiteId, String date){
        List<AdwordsReportData> r = new ArrayList<AdwordsReportData>();
        
        List<AdwordsAccount> adwordsAccounts = adwordsAccountService.getAccountsByWebsiteId(websiteId);

        if(CollectionUtils.isEmpty(adwordsAccounts)){
        	return r;
        }
        
        List<AdwordsReportData> adwordsReportDatas = new ArrayList<AdwordsReportData>();
        
        for (AdwordsAccount adwordsAccount : adwordsAccounts) {
        	String adwordsReportDownloadPath = new ReportDownloadPathBuilder(websiteId, adwordsAccount.getId(), ReportType.Keyword, websiteId).build();
        	List<AdwordsReportData> singleAdwordsReportData = adwordsReportingServiceImpl.parse(adwordsReportDownloadPath, date, ReportType.Keyword, adwordsAccount);
        	adwordsReportDatas.addAll(singleAdwordsReportData);
        }
        return r;
	}
	
}
