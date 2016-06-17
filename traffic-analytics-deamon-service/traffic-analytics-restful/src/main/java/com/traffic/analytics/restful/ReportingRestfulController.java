package com.traffic.analytics.restful;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traffic.analytics.commons.base.controller.JsonReturnController;
import com.traffic.analytics.commons.returntype.JsonReturn;

@RestController
@RequestMapping("/report")
public class ReportingRestfulController extends JsonReturnController {

	
	@RequestMapping("/download/{websiteId}/{date}")
	public JsonReturn downloadGoogleAnalyticsReport(String date){
		
		return super.getJsonReturn();
	}

}
