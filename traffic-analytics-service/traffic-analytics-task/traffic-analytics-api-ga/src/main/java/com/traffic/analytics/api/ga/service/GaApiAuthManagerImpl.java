package com.traffic.analytics.api.ga.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.traffic.analytics.api.model.Account;
import com.traffic.analytics.api.service.ApiAuthManager;

@Service("gaApiAuthManagerImpl")
public class GaApiAuthManagerImpl implements ApiAuthManager<Analytics> {

	private String APPLICATION_NAME = "Aztechx-Google-Analytics";
    private JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String FS = File.separator;
    private String KEY_FILE_LOCATION = System.getProperty("user.home") + FS + "/My_Ga_API_9d71e2a88099.p12";
    private String SERVICE_ACCOUNT_EMAIL = "mygadumper@my-ga-api.iam.gserviceaccount.com";
	
	
	@Override
	public Analytics generateApiAccessAuth(Account account) throws Exception {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(JSON_FACTORY).setServiceAccountId(SERVICE_ACCOUNT_EMAIL).setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION)).setServiceAccountScopes(AnalyticsScopes.all()).build();
		return new Analytics.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

}