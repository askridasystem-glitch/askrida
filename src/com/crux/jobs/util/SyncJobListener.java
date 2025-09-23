package com.crux.jobs.util;

import com.crux.jobs.constant.JobConstant;
import com.crux.jobs.model.JobConfigView;
import com.crux.jobs.model.JobStatusView;
import com.crux.jobs.ejb.JobConfigHome;
import com.crux.jobs.ejb.JobConfig;
import com.crux.util.*;
import com.crux.common.model.UserSession;
import com.crux.common.codedecode.Codec;
import com.crux.login.model.UserSessionView;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.util.Date;
import java.rmi.RemoteException;

/**
 * <explain class description>
 * <p/>
 * Created: Jul 6, 2004 - 6:02:10 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public class SyncJobListener implements JobListener {
    private static final LogManager logger = LogManager.getInstance(SyncJobListener.class);
    private UserSessionView us;

    public SyncJobListener() {
        us = new UserSessionView();
        us.setStUserID("daemon");
    }

    public String getName() {
        return JobConstant.SYNC_JOB_LISTENER;
    }

    private JobConfig getRemoteJobConfig() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((JobConfigHome) JNDIUtil.getInstance().lookup("JobConfigEJB", JobConfigHome.class.getName()))
                .create();
    }

    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();

        boolean isRunning = false;
        try {
            isRunning = dataMap.getBoolean(JobConstant.JOB_RUNNING_STATUS);
        } catch (Exception e) {
            isRunning = false;
        }

        if (isRunning) {
            logger.logDebug("\nJob " + jobDetail.getDescription() + " is still running");
            return;
        }

        us.setDtTransactionDate(new Date());
        try {
            final JobConfigView jobconfig = JobUtil.getJob(jobExecutionContext);
            final JobStatusView joblog = new JobStatusView();
            joblog.setStJobID(jobconfig.getStJobID());
            joblog.setStJobStatus(Codec.JobLogStatus.RUNNING);
            joblog.setStStatusMessage("Running");
            joblog.setDtJobDate(us.getDtTransactionDate());
            joblog.setUserSession(us);
            joblog.markNew();
            final String stJobLogID = getRemoteJobConfig().save(joblog);
            dataMap.put("JOB_LOG_ID", stJobLogID);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            logger.logError("jobToBeExecuted: failed to record job log");
        }

        dataMap.put(JobConstant.JOB_RUNNING_STATUS, true);
        jobDetail.setJobDataMap(dataMap);
    }

    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        try {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            JobDataMap dataMap = jobDetail.getJobDataMap();

            final String stJobLogID = (String) dataMap.get("JOB_LOG_ID");

            if (stJobLogID != null) {
                final JobStatusView joblog = getRemoteJobConfig().getJobLog(stJobLogID);
                if (joblog != null) {
                    us.setDtTransactionDate(new Date());
                    joblog.markUpdate();
                    joblog.setUserSession(us);
                    joblog.setDtJobDate(us.getDtTransactionDate());
                    if (e == null) {
                        joblog.setStJobStatus(Codec.JobLogStatus.OK);
                        joblog.setStStatusMessage("Success");
                    } else {
                        joblog.setStJobStatus(Codec.JobLogStatus.ERROR);
                        joblog.setStStatusMessage(e.getMessage());
                    }
                    getRemoteJobConfig().save(joblog);
                }
            }

            String jobId = jobDetail.getName();
            int pos = jobId.indexOf("invoke");
            if (pos != -1)
                jobId = jobId.substring(0, pos);

            if (e == null) {
                dataMap.put(JobConstant.CURRENT_RETRY, 0);

                DTOList nextJobs = JobUtil.getNextJobs(jobId);
                for (int i = 0; i < nextJobs.size(); i++) {
                    JobConfigView nextJob = (JobConfigView) nextJobs.get(i);
                    try {
                        if (JobConfigView.Enabled.ENABLED.equals(nextJob.getStEnabled())) {
                            int starterInterval;
                            try {
                                starterInterval = nextJob.getItStarterJobInterval().intValue();
                            } catch (Exception es) {
                                starterInterval = 0;
                            }

                            Date startDate = new Date(System.currentTimeMillis() + starterInterval * 60 * 1000);
                            nextJob.setDtStart(startDate);

                            boolean inSchedule = JobUtil.getScheduler().getJobDetail(jobId, Scheduler.DEFAULT_GROUP) != null;
                            JobUtil.scheduleJob(nextJob, inSchedule);
                        }
                    } catch (Exception ej) {
                        logger.logError("Cannot trigger job : " + nextJob.getStJobID() + "\n" + e);
                    }
                }
                dataMap.put(JobConstant.JOB_RUNNING_STATUS, false);
                jobDetail.setJobDataMap(dataMap);

            } else {
                int currentRetry;

                try {
                    currentRetry = dataMap.getInt(JobConstant.CURRENT_RETRY);
                } catch (Exception en) {
                    currentRetry = 0;
                }

                int retryAllowed = dataMap.getInt(JobConstant.RETRY);
                int retryInterval = dataMap.getInt(JobConstant.RETRY_INTERVAL);

                logger.logDebug("\n Retry jobId = " + jobId + ", current retry = " + currentRetry);

                if (currentRetry >= retryAllowed) {
                    logger.logDebug("\nSystem has retried " + currentRetry + " times. Stop retry.\n");
                    return;
                }

                currentRetry++;
                dataMap.put(JobConstant.CURRENT_RETRY, currentRetry);

                JobDataMap jobDataMap = new JobDataMap();

                Date startDate = new Date(System.currentTimeMillis() + retryInterval * 60 * 1000);

                pos = jobId.indexOf("retry");
                JobConfigView currentJob = JobUtil.getJobConfigView(pos != -1 ? jobId.substring(0, pos) : jobId);
                Class jobClass = (Class) JobConfigView.Jobs.getLookUp().getDescription(currentJob.getStJobEvent());

                try {

                    jobDataMap.put(JobConstant.RETRY, NumberUtil.getValue(currentJob.getItRetry()));
                    jobDataMap.put(JobConstant.RETRY_INTERVAL, NumberUtil.getValue(currentJob.getItRetryInterval()));
                    jobDataMap.put(JobConstant.CURRENT_RETRY, currentRetry);

                    jobId = jobId + "retry";
                    JobUtil.deleteJob(jobId);
                    JobUtil.getScheduler().unscheduleJob(jobId + "trigger", Scheduler.DEFAULT_GROUP);

                    JobDetail retryJobDetail = new JobDetail(jobId, Scheduler.DEFAULT_GROUP, jobClass);
                    retryJobDetail.setJobDataMap(jobDataMap);

                    Trigger trigger = new SimpleTrigger(jobId + "trigger", Scheduler.DEFAULT_GROUP,
                            startDate, null,
                            0, 0);

                    logger.logDebug("Reschedule.....");
                    JobUtil.getScheduler().scheduleJob(retryJobDetail, trigger);

                } catch (Exception et) {
                    logger.logError("Cannot retry job : " + jobId + "\n" + et);

                }
            }
        } catch (Exception le) {
            logger.logError("Error while executing method jobWasExecuted" + e);
        }
    }
}


//
/*
try {
	Thread.sleep(retryInterval * 60 * 1000);
	JobUtil.getScheduler().triggerJob(jobId, Scheduler.DEFAULT_GROUP);
} catch (Exception et) {
	logger.logError("Error trigger job: " + e);
}
--- */

