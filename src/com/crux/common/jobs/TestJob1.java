/***********************************************************************
 * Module:  com.crux.common.jobs.TestJob1
 * Author:  Denny Mahendra
 * Created: Jul 15, 2004 3:12:02 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.jobs;

import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;

public class TestJob1 implements StatefulJob {
    private final static transient LogManager logger = LogManager.getInstance(TestJob1.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.logDebug("execute: job 1 run successfull");
    }
}
