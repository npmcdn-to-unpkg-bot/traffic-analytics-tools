package com.traffic.analytics.commons.base.dao;

/**
 * @author yuhuibin.
 */
public enum  Vender {
    BAIDU("baidu"),
    GOOGLE("google"),
    GA("ga");

    private String value;

    private Vender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
