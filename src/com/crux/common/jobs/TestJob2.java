/***********************************************************************
 * Module:  com.crux.common.jobs.TestJob2
 * Author:  Denny Mahendra
 * Created: Jul 15, 2004 3:13:03 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.jobs;

import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;

public class TestJob2 implements StatefulJob {
    private final static transient LogManager logger = LogManager.getInstance(TestJob2.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.logDebug("execute: entering");
        logger.logDebug("execute: throwing exception");
        throw new JobExecutionException(new IllegalAccessException("dummy"));
    }
}
