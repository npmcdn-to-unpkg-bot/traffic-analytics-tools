package com.traffic.analytics.api.adwords.service;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.traffic.analytics.api.adwords.model.AdwordsAccount;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.service.ApiAuthManager;

@Service("adwordsApiAuthManagerImpl")
public class AdwordsApiAuthManagerImpl implements ApiAuthManager<AdWordsSession> {

	private Log log = LogFactory.getLog(getClass());
	
	@Override
	public AdWordsSession generateApiAccessAuth(Account account) throws Exception {
		if (!(account instanceof AdwordsAccount)) {
			throw new RuntimeException("account is not a adwords account");
		}
		AdwordsAccount adwordsAccount = (AdwordsAccount) account;
		Credential oAuth2Credential = null;
		AdWordsSession session = null;
		try {
			// 1、对oAuth进行验证,文件中保存的是oAuth验证需要的配置信息
			Configuration config = new BaseConfiguration();
			config.setProperty("api.adwords.clientId", adwordsAccount.getClientId());
			config.setProperty("api.adwords.clientSecret", adwordsAccount.getClientSecret());
			config.setProperty("api.adwords.refreshOAuth2Token", adwordsAccount.getRefreshOAuth2Token());
			config.setProperty("api.adwords.refreshToken", adwordsAccount.getRefreshToken());
			config.setProperty("api.adwords.email", adwordsAccount.getEmail());
			config.setProperty("api.adwords.password", adwordsAccount.getPassword());
			oAuth2Credential = new OfflineCredentials.Builder().forApi(OfflineCredentials.Api.ADWORDS).from(config).build().generateCredential();
			// 2、获取AdWordsSession,此处是验证通过后,获取adwords session需要的配置参数
			config.clear();
			config.setProperty("api.adwords.clientCustomerId", adwordsAccount.getAccountId());
			config.setProperty("api.adwords.userAgent", adwordsAccount.getUserAgent());
			config.setProperty("api.adwords.developerToken", adwordsAccount.getDeveloperToken());
			config.setProperty("api.adwords.reporting.skipHeader", adwordsAccount.getSkipHeader());
			config.setProperty("api.adwords.reporting.skipSummary", adwordsAccount.getSkipSummary());
			session = new AdWordsSession.Builder().from(config).withOAuth2Credential(oAuth2Credential).build();
		} catch (OAuthException | ValidationException e) {
			// 输出错误信息
			log.error("AdWords Authorization validate error : " + e.getMessage());
		}
		return session;
	}

}
