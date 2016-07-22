package com.traffic.analytics.api.ga.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.commons.base.dao.DaoImpl;

@Repository
public class GaAccountDaoImpl extends DaoImpl<GaAccount> implements GaAccountDao {

	@Override
	public GaAccount getGaAccountByWebsiteId(String websiteId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("websiteId").is(websiteId));
		List<GaAccount> gaAccounts = super.find(query);
		return (!CollectionUtils.isEmpty(gaAccounts)) ? gaAccounts.get(0) : null;
	}

	@Override
	public GaAccount getGaAccountById(String id) {
		return super.get(id);
	}

	@Override
	public List<GaAccount> findAllGaAccounts() {
		return super.findAll();
	}

}
