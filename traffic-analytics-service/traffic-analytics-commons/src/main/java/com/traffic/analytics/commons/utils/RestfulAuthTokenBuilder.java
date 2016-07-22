/**
 *
 */
package com.traffic.analytics.commons.utils;

import java.util.Date;
import java.util.UUID;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * 使用jasypt生成的Token，缓存在redis中，用于作为key保存用户的登陆信息
 *
 * @author SEAN
 */
public class RestfulAuthTokenBuilder {

    /**
     * 相当于在token中加了"盐"
     */
    private static final String projectAuthToken = "AztehcxAuthenticationToken";

    /**
     * 用于jasypt的解密
     */
    private static final String jasyptPassword = "aztechx1.semToOls.Pwd";

    /**
     * 创建Token
     *
     * @param account
     *            用户的登陆账户
     * @return 一个字符串token
     */
    public String build(String account) {
        String key = UUID.randomUUID().toString().toUpperCase() + "|" + projectAuthToken + "|" + account + "|" + new Date();
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptPassword);
        return encryptor.encrypt(key);
    }

}
