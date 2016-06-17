package com.traffic.analytics.task.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.traffic.analytics.core.model.Website;
import com.traffic.analytics.core.service.PerformanceGenerateContext;
import com.traffic.analytics.core.service.WebsiteService;
import com.traffic.analytics.task.model.Task;

/**
 *
 * @author yuhuibin
 */
public class TaskForGeneratePerformance extends Task {

    @Autowired
	private PerformanceGenerateContext performanceGenerateContext;
	
	@Autowired
	protected WebsiteService websiteService;

	@Override
	public void run() throws Exception {
		List<Website> websites = websiteService.findAllWebsites();
		for (Website website : websites) {
			performanceGenerateContext.generatePerformanceReport(website.getId(), super.getDate());	
		}
	}
	
}
