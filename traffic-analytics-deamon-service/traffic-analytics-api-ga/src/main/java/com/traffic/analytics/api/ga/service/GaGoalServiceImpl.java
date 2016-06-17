package com.traffic.analytics.api.ga.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Goal;
import com.google.api.services.analytics.model.Goals;
import com.google.api.services.analytics.model.Profile;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;
import com.traffic.analytics.api.ga.dao.GaGoalDao;
import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.ga.model.GaGoal;
import com.traffic.analytics.api.ga.model.GaView;
import com.traffic.analytics.api.service.ApiAuthManager;

@Service
public class GaGoalServiceImpl implements GaGoalService {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	@Qualifier("gaApiAuthManagerImpl")
	private ApiAuthManager<Analytics> apiAuthManager;

	@Autowired
	private GaGoalDao gaGoalDao;

	@Override
	public List<GaGoal> getGaGoalsByGaAccount(com.traffic.analytics.api.model.Account account) {
		List<GaGoal> r = new ArrayList<GaGoal>();
		Analytics analytics;
		try {
			analytics = apiAuthManager.generateApiAccessAuth(null);
			GaView gaView = ((GaAccount) account).getGaview();
			List<Goal> goalList = null;

			Goals goals = analytics.management().goals().list(gaView.getAccountId(), gaView.getPropertyId(), gaView.getProfileId()).execute();
			if (null != goals && goals.getItems().size() > 0) {
				goalList = goals.getItems();
			}
			if (CollectionUtils.isEmpty(goalList)) {
				log.warn("No goal found");
				return null;
			}
			// 插入最新的GaGoal
			for (Goal goal : goalList) {
				GaGoal gaGoal = new GaGoal();
				gaGoal.setWebsiteId(account.getWebsiteId());
				gaGoal.setAccountId(goal.getAccountId());
				gaGoal.setPropertyId(goal.getWebPropertyId());
				gaGoal.setProfileId(goal.getProfileId());
				gaGoal.setGoalId(goal.getId());
				gaGoal.setGoalName(goal.getName());
				r.add(gaGoal);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}

		this.refreshGaGoals(((GaAccount)account).getWebsiteId(), r);
		return r;
	}

	@Override
	public List<GaGoal> getGaGoalsByWebsiteId(String websiteId) {
		return gaGoalDao.getGaGoalsByWebsiteId(websiteId);
	}

	private void refreshGaGoals(String websiteId, List<GaGoal> gaGoals) {
		gaGoalDao.deleteGaGoalsByWebsiteId(websiteId);
		for (GaGoal gaGoal : gaGoals) {
			gaGoalDao.insertGaGoal(gaGoal);
		}		
	}

	@Override
	public List<GaView> getGaViewsByDomain(String domain) {
		List<GaView> gaViewList = new ArrayList<GaView>();
		try {
			// 1.google analytics oauth2验证
			Analytics analytics = apiAuthManager.generateApiAccessAuth(null);

			// 2.获取所有的account
			List<Account> accountList = null;
			Accounts accounts = analytics.management().accounts().list().execute();
			if (accounts.getItems().isEmpty()) {
				log.info("No accounts found");
			} else {
				accountList = accounts.getItems();
			}

			// 3.循环每个account下面的web property
			for (Account account : accountList) {
				String accountId = account.getId();
				Webproperties properties = analytics.management().webproperties().list(accountId).execute();
				if (properties.getItems().isEmpty()) {
					log.info("No Webproperties found");
					continue;
				}

				// 4.循环每个web property下面的profile
				for (Webproperty webproperty : properties.getItems()) {
					if (!webproperty.getWebsiteUrl().contains(domain)) {
						continue;
					}
					String webpropertyId = webproperty.getId();
					Profiles profiles = analytics.management().profiles().list(accountId, webpropertyId).execute();
					if (profiles.getItems().isEmpty()) {
						log.info("No views (profiles) found");
						continue;
					}
					List<Profile> profileList = profiles.getItems();
					if (null == profileList || profileList.size() == 0) {
						continue;
					}
					// 5.循环每个profile(profile就相当于google analytics view)
					for (Profile profile : profileList) {
						GaView gaView = new GaView();
						gaView.setAccountId(profile.getAccountId());
						gaView.setPropertyId(profile.getWebPropertyId());
						gaView.setProfileId(profile.getId());
						gaView.setName(profile.getName());
						gaViewList.add(gaView);
					}
				}
			}
		} catch (Exception e) {
			log.info("Get GaView Exception : " + e.getMessage());
			throw new RuntimeException();
		}
		return gaViewList;
	}

}
