/*
 * $Header: /cvs/webfin/ejbx/src/com/crux/jobs/ejb/JobConfig.java,v 1.1.1.1 2005/12/26 03:07:30 cvsdenny Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2005/12/26 03:07:30 $
 *
 * JobConfig.java
 *
 * Copyright &#169; 2004 PineappleTech
 * All rights reserved.
 */
package com.crux.jobs.ejb;

import com.crux.jobs.model.JobConfigView;
import com.crux.jobs.model.JobStatusView;
import com.crux.jobs.filter.JobLogFilter;
import com.crux.util.DTOList;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * <explain class description>
 * <p/>
 * Created: Jun 16, 2004 - 3:04:26 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public interface JobConfig extends EJBObject {
    DTOList getAllJobs() throws Exception, RemoteException;

    public String saveJob(JobConfigView view) throws Exception, RemoteException;

    public void deleteJob(String jobId) throws Exception, RemoteException;

    public JobConfigView getJobConfigView(String jobId) throws Exception, RemoteException;

    public DTOList getNextJobs(String jobId) throws Exception, RemoteException;

    JobStatusView getJobLog(String stJobLogID) throws Exception, RemoteException;

    String save(JobStatusView joblog) throws Exception, RemoteException;

    DTOList viewJobLog(JobLogFilter f) throws Exception, RemoteException;
}
