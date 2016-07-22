package com.traffic.analytics.core.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.traffic.analytics.core.model.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class WebsiteServiceTestCase {

	@Autowired
	private WebsiteService websiteService;

	@Test
	public void testFindAllWebsites(){
		List<Website> websites = websiteService.findAllWebsites();
		for (Website website : websites) {
			System.out.println(website.getDomain());
		}
	}
	
}
