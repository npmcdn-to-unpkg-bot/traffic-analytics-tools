package com.traffic.analytics.task.model;

import com.traffic.analytics.commons.utils.ApplicationContextUtils;
import com.traffic.analytics.commons.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author yuhuibin
 */
public class TaskTriger {

    private static Log log = LogFactory.getLog(TaskTriger.class);

    public static void main(String[] args) throws Exception {
        String jobconfig = args[0];
        String reportBeanId = args[1];
        String date = null;
        ApplicationContextUtils.init(jobconfig);
        Task task = (Task) ApplicationContextUtils.getBean(reportBeanId);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(task.getTimezone());
        try {
            date = args[2];
            format.parse(date);
        } catch (IndexOutOfBoundsException e) {
            date = DateUtils.getYesterday(task.getTimezone());
        } catch (IllegalArgumentException | NullPointerException | ParseException e) {
            log.error("date format error: " + date);
            throw e;
        }
        task.setDate(date);
        task.run();
    }

}
