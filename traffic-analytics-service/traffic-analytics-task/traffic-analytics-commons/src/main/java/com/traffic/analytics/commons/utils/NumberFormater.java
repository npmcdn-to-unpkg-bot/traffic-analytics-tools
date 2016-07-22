/**
 * 
 */
package com.traffic.analytics.commons.utils;

import java.math.BigDecimal;

/**
 * 数值类型的格式化工具类
 * 
 * @author SEAN
 *
 */
public class NumberFormater {

	private Number number;
	
	private Integer scale;
	
	public NumberFormater(Number number, Integer scale){
		this.number = number;
		this.scale = scale;
	}
	
	public Float format(){
		BigDecimal bd = new BigDecimal(this.number.doubleValue());
		bd.setScale(this.scale, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	} 
	
}
