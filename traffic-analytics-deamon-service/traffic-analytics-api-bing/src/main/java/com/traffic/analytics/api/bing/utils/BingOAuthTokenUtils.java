package com.traffic.analytics.api.bing.utils;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * @author yuhuibin.
 */
public class BingOAuthTokenUtils {

    private static final Log LOG = LogFactory.getLog(BingOAuthTokenUtils.class);

    /**
     * String refreshToken = BingOAuthTokenUtils.getBingOAuthRefreshToken(bingAccount.getClientId(), bingAccount.getClientSecret(), bingAccount.getRefreshToken());
	            if (StringUtils.isBlank(refreshToken)) {
	                return downloadStatus;
	            }
	            bingAccount.setRefreshToken(refreshToken);
	            bingAccountDao.updateBingAccount(bingAccount);
	            */
    public static synchronized String getBingOAuthRefreshToken(String clientId, String clientSecret, String lastRefreshToken) throws Exception {
        HttpClient client = new DefaultHttpClient();
        String bingOAuthUriTemplate = "https://login.live.com/oauth20_token.srf?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
        String bingOAuthUri = bingOAuthUriTemplate.replace("CLIENT_ID", clientId).replace("CLIENT_SECRET", clientSecret).replace("REFRESH_TOKEN", lastRefreshToken);
        URI uri = new URI(bingOAuthUri);
        // 2.获取http response
        HttpGet httpget = new HttpGet(uri);
        httpget.setHeader("Connection", "keep-alive");
        httpget.setHeader("Host", "login.live.com");
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
        HttpResponse response = client.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String entityContent = EntityUtils.toString(entity);
            JSONObject json = new JSONObject(entityContent);
            String refreshToken = json.getString("refresh_token");
            LOG.info("Bing OAuth refresh_token:" + refreshToken);
            return refreshToken;
        }
        return null;
    }

}
