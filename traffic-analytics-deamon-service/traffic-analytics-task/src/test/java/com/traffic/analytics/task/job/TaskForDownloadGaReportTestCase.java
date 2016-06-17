package com.traffic.analytics.task.job;

import org.junit.Test;

import com.traffic.analytics.task.model.TaskTriger;

public class TaskForDownloadGaReportTestCase {

	@Test
	public void testTaskForDownloadGaReport(){
		String args[] = new String[]{"applicationContext-ga.xml","ga_report_download"};
		try {
			TaskTriger.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
