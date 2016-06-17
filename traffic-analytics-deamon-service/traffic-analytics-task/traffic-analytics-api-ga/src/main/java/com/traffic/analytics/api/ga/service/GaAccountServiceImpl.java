package com.traffic.analytics.api.ga.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traffic.analytics.api.ga.dao.GaAccountDao;
import com.traffic.analytics.api.ga.model.GaAccount;
import com.traffic.analytics.api.service.AccountService;

@Service("gaAccountServiceImpl")
public class GaAccountServiceImpl implements AccountService<GaAccount>{

	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private GaAccountDao gaAccountDao;
		
	@Override
	public List<GaAccount> getAllAccounts() {
		return gaAccountDao.findAllGaAccounts();
	}

	@Override
	public List<GaAccount> getAccountsByWebsiteId(String websiteId) {
		log.warn("this method [getAccountsByWebsiteId] will return a single ga-account .");
		List<GaAccount> list = new ArrayList<GaAccount>();
		GaAccount singleGaAccount = gaAccountDao.getGaAccountByWebsiteId(websiteId);
		if(singleGaAccount != null){
			list.add(singleGaAccount);
		}
		return list;
	}

	@Override
	public GaAccount getAccountById(String accountId) {
		return gaAccountDao.getGaAccountById(accountId);
	}

}
