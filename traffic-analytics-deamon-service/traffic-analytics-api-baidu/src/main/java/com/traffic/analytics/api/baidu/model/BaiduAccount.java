package com.traffic.analytics.api.baidu.model;

import com.traffic.analytics.api.model.Account;

/**
 *
 * @author yuhuibin
 */
public class BaiduAccount extends Account {

	private static final long serialVersionUID = -5697105315811368931L;

	private String username;

    private String password;

    private String token;

    private String target;

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
