package com.traffic.analytics.commons.model;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.commons.base.model.Model;

@Document(collection="User")
public class User extends Model {

	private static final long serialVersionUID = -6597074432001176134L;

	private String username;
	
	private String account;
	
	private String password;

	private Set<String> websiteIds;

	public Set<String> getWebsiteIds() {
		return websiteIds;
	}

	public void setWebsiteIds(Set<String> websiteIds) {
		this.websiteIds = websiteIds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
