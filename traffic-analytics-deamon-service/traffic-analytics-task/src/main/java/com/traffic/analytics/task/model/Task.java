package com.traffic.analytics.task.model;

import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.traffic.analytics.api.model.Vendor;

public abstract class Task {

	protected Log log = LogFactory.getLog(getClass());
	
    protected String date;

    protected TimeZone timezone;
    
    protected Vendor vendor;

    public abstract void run() throws Exception;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

}
