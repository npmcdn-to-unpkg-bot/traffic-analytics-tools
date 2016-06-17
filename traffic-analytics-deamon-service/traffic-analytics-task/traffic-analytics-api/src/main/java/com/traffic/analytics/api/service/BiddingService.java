package com.traffic.analytics.api.service;

import com.traffic.analytics.api.model.Account;

public interface BiddingService {

	public boolean bidding(Account account, String campaignId, String adgroupId, String keywordId, Float bid) throws Exception;
	
	public boolean pause(Account account, String campaignId, String adgroupId, String keywordId) throws Exception;
	
	public boolean active(Account account, String campaignId, String adgroupId, String keywordId) throws Exception;
	
}
