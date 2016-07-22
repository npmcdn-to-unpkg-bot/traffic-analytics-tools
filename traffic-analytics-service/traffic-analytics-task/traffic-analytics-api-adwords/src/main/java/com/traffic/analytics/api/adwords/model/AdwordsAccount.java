package com.traffic.analytics.api.adwords.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.api.model.Account;

@Document(collection="AdwordsAccount")
public class AdwordsAccount extends Account {

	private static final long serialVersionUID = -5264605205876562516L;

    private String accountId;

	private String clientId;

	private String clientSecret;

	private String refreshToken;

	private Boolean refreshOAuth2Token = false;

	private String email;

	private String password;

	private String userAgent = "SEMTool";

	private String developerToken;

	private Boolean skipHeader = false;

	private Boolean skipSummary = false;

	private String reportFields = "CampaignId,CampaignName,AdGroupId,AdGroupName,Id,Criteria,FinalUrls,Clicks,Cost,Impressions";

	private String reportName = "adwords keywords report";

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Boolean getRefreshOAuth2Token() {
		return refreshOAuth2Token;
	}

	public void setRefreshOAuth2Token(Boolean refreshOAuth2Token) {
		this.refreshOAuth2Token = refreshOAuth2Token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getDeveloperToken() {
		return developerToken;
	}

	public void setDeveloperToken(String developerToken) {
		this.developerToken = developerToken;
	}

	public Boolean getSkipHeader() {
		return skipHeader;
	}

	public void setSkipHeader(Boolean skipHeader) {
		this.skipHeader = skipHeader;
	}

	public Boolean getSkipSummary() {
		return skipSummary;
	}

	public void setSkipSummary(Boolean skipSummary) {
		this.skipSummary = skipSummary;
	}

	public String getReportFields() {
		return reportFields;
	}

	public void setReportFields(String reportFields) {
		this.reportFields = reportFields;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
