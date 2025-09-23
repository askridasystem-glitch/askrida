/***********************************************************************
 * Module:  com.crux.jobs.model.Executable
 * Author:  Denny Mahendra
 * Created: Jun 18, 2004 8:02:43 PM
 * Purpose:
 ***********************************************************************/

package com.crux.jobs.model;

import org.quartz.JobExecutionContext;

public interface Executable {
    Object execute() throws Exception;
}
