package com.traffic.analytics.api.adwords.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.commons.base.dao.DaoImpl;

@Repository
public class AdwordsAccountDaoImpl extends DaoImpl<AdwordsAccount> implements AdwordsAccountDao {

	@Override
	public List<AdwordsAccount> getAllAdwordsAccounts() {
		return super.findAll();
	}

	@Override
	public List<AdwordsAccount> getAdwordsAccountByWebsiteId(String websiteId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("websiteId").is(websiteId));
		return super.find(query);
	}

	@Override
	public AdwordsAccount getAdwordsAccountById(String accountId) {
		return super.get(accountId);
	}
}
