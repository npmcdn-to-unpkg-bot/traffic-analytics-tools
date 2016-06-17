package com.traffic.analytics.commons.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletResponse;

import com.alibaba.fastjson.JSON;
import com.traffic.analytics.commons.returntype.JsonReturn;

/**
 * HTTP响应的工具类
 * 
 * @author SEAN
 */
public class ResponseUtils {

	/**
	 * 将Json对象写出到客户端
	 * 
	 * @param response
	 *            {@link ServletResponse}
	 * @param json
	 *            {@link JsonReturn}
	 * @throws IOException
	 *             IO异常
	 */
	public static void write2Client(ServletResponse response, JsonReturn json) throws IOException {
		response.setContentType("text/json; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(JSON.toJSONString(json));
		pw.flush();
		pw.close();
	}

}
