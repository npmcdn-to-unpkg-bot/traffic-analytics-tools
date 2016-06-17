package com.traffic.analytics.api.bing.service;

import java.net.URL;

import org.springframework.stereotype.Service;

import com.microsoft.bingads.AuthorizationData;
import com.microsoft.bingads.OAuthWebAuthCodeGrant;
import com.traffic.analytics.api.bing.model.BingAccount;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.service.ApiAuthManager;

@Service("bingApiAuthManagerImpl")
public class BingApiAuthManagerImpl implements ApiAuthManager<AuthorizationData>{

	@Override
	public AuthorizationData generateApiAccessAuth(Account account) throws Exception {
		if (!(account instanceof BingAccount)) {
			throw new RuntimeException("account is not a bing account");
		}
		BingAccount bingAccount = (BingAccount) account;
		
		AuthorizationData authorizationData = new AuthorizationData();
        OAuthWebAuthCodeGrant oAuthWebAuthCodeGrant = new OAuthWebAuthCodeGrant(bingAccount.getClientId(), bingAccount.getClientSecret(), new URL(bingAccount.getRedirectUrl()), bingAccount.getRefreshToken());
        authorizationData.setDeveloperToken(bingAccount.getDeveloperToken());
        authorizationData.setAuthentication(oAuthWebAuthCodeGrant);
        authorizationData.setCustomerId(Long.parseLong(bingAccount.getCustomerId()));
        authorizationData.setAccountId(Long.parseLong(bingAccount.getAccountId()));
        return authorizationData;
	}

}
