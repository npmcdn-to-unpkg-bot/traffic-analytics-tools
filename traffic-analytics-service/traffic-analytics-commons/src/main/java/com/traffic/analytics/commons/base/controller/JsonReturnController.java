package com.traffic.analytics.commons.base.controller;

import com.traffic.analytics.commons.constants.SystemMessageCode;
import com.traffic.analytics.commons.returntype.JsonReturn;

/**
 * Controller统一从此类继承，提供Controller层的公共方法
 * @author SEAN
 *
 */
public abstract class JsonReturnController {

	/**
	 * 获取成功状态的JsonReturn对象
	 * @return 成功状态的JsonReturn对象
	 */
	protected JsonReturn getJsonReturn(){
		return new JsonReturn(SystemMessageCode.SUCCESS);
	}
	
}
