package com.traffic.analytics.commons.constants;

/**
 * 系统返回信息码的常亮类,对返回的Code和Message做一个数据字典的管理，各个模块需要认领各自开头的Code 比如用户模块统一开头是1,关键词优化模块开头统一是2，不同的开头对应不同的模块
 * 
 * 配置文件在docs/SystemMessageCode.csv下
 * 
 * @author SEAN
 *
 */
public final class SystemMessageCode {
	// TODO---------------------系统提示---------------------start
	// 系统错误
	public static final String ERROR = "000001";

	// 操作成功
	public static final String SUCCESS = "000002";

	// GA API访问失败
	public static final String GA_API_ACCESSI_ERROR = "000003";

	// Baidu API访问失败
	public static final String BAIDU_API_ACCESS_ERROR = "000004";

	// Baidu API账号结构下载失败
	public static final String BAIDU_ACCOUNT_STUCTURE_DOWNLOAD_FAILURE = "000005";

	// Adwords API访问失败
	public static final String ADWORDS_API_ACCESS_ERROR = "000006";

	// Adwords API报表下载失败
	public static final String ADWORDS_REPORT_DOWNLOAD_FAILURE = "000007";

	// 日期格式不正确
	public static final String DATE_FORMAT_ERROR = "000008";

	// 日期范围输入有误
	public static final String DATE_RANGE_ERROR = "000009";
	// -----------------------系统提示-----------------------end

	// TODO---------------------用户模块---------------------start
	// 当前用户没有访问权限
	public static final String USER_ACCESS_REFUSE = "100000";

	// 登陆成功
	public static final String USER_LOGIN_SUCCESS = "100001";

	// 用户名为空
	public static final String USER_NAME_IS_EMPTY = "100002";

	// 用户账号不可以为空
	public static final String USER_ACCOUNT_IS_EMPTY = "100003";

	// 用户密码不可以为空
	public static final String USER_PASSWORD_IS_EMPTY = "100004";

	// 用户账号或密码错误
	public static final String USER_ACCOUNT_IS_NOT_EXIST_OR_PWD_WRONG = "100005";

	// 确认密码为空
	public static final String USER_PASSWORDED_IS_EMPTY = "100006";

	// 确认密码和密码不相同
	public static final String USER_PASSWORD_IS_NOT_EQUALS_PASSWORDED = "100007";

	// 用户注册成功
	public static final String USER_REGISTER_SUCCESS = "100008";

	// 用户账号已存在
	public static final String USER_ACCOUNT_IS_EXIST = "100009";

	// 原始密码为空
	public static final String USER_QUONDAM_PASSWORD_IS_EMPTY = "100010";

    // 用户不存在
	public static final String USER_IS_NOT_EXIST = "100011";

    //密码错误
    public static final String USER_PASSWORD_IS_WRONG = "100012";

    // --------------------------用户模块----------------------end

    // TODO ---------------------站点模块---------------------start
    //站点已存在
    public static final String WEBSITE_IS_EXIST = "200000";

    // 账户为空
    public static final String WEBSITE_ACCOUNT_IS_EMPTY = "200001";

    // 账户已绑定
    public static final String WEBSITE_ACCOUNT_IS_BIND = "200002";

    // domain找不到
    public static final String GA_DOMAIN_IS_NOT_FOUND = "200003";

    // 站点不存在
    public static final String WEBSITE_IS_EMPTY = "200004";

	// ---------------------站点模块---------------------end

	// TODO---------------------报表模块---------------------start
	public static final String PERFORMANCE_REPORT_QUERY_ERROR = "300001";

}
