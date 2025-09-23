package com.crux.jobs.util;

import com.crux.jobs.constant.JobConstant;
import com.crux.jobs.ejb.JobConfig;
import com.crux.jobs.ejb.JobConfigHome;
import com.crux.jobs.model.JobConfigView;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.NumberUtil;
import com.crux.util.LogManager;
import com.crux.common.model.UserSession;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import org.joda.time.DateTime;

/**
 * <explain class description>
 * <p/>
 * Created: Jul 6, 2004 - 4:45:36 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public class JobUtil {
   private final static transient LogManager logger = LogManager.getInstance(JobUtil.class);
   private static Scheduler sched;
   private static JobConfig jobConfig;

   private static String serverIP = Parameter.readString("SYS_SERVER_IP");

   public static Scheduler getScheduler() throws SchedulerException {
      if (sched == null) {
         SchedulerFactory schedFact = new StdSchedulerFactory();
         sched = schedFact.getScheduler();
         sched.addGlobalJobListener(new SyncJobListener());
      }
      return sched;
   }

   private static JobConfig getRemoteJob() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      if (jobConfig == null)
         jobConfig = ((JobConfigHome) JNDIUtil.getInstance().lookup("JobConfigEJB", JobConfigHome.class.getName()))
                 .create();
      return jobConfig;
   }

   public static void executeAllSchedules() throws Exception {
      DTOList dtoList = getRemoteJob().getAllJobs();
      for (int i = 0; i < dtoList.size(); i++) {
         JobConfigView view = (JobConfigView) dtoList.get(i);
         boolean enabled = JobConfigView.Enabled.ENABLED.equalsIgnoreCase(view.getStEnabled());
         boolean emptyStartedJob = view.getStStarterJobID() == null || "".equals(view.getStStarterJobID());
         if (enabled && emptyStartedJob) {
            JobUtil.scheduleJob(view, false);
         }
      }
   }

   public static void scheduleJob(JobConfigView view, boolean isRescheduled) throws Exception {
      logger.logInfo("scheduling job : " + view.getStJobID());

      String jobId = view.getStJobID();
      String jobEvent = view.getStJobEvent();
      String triggerMode = view.getStTriggerMode();
      String cronExpression = view.getStCRONExpression();
      long interval = NumberUtil.getValue(view.getLgInterval());
      int repetition = (int) NumberUtil.getValue(view.getLgRepeatNum());
      Date startDate = view.getDtStart();
      Date endDate = view.getDtEnd();

      Class jobClass = (Class) JobConfigView.Jobs.getLookUp().getDescription(jobEvent);

      JobDetail jobDetail = new JobDetail(jobId, Scheduler.DEFAULT_GROUP, jobClass);
      jobDetail.setDescription(jobEvent);

      JobDataMap jobDataMap = new JobDataMap();

      jobDataMap.put(JobConstant.RETRY, NumberUtil.getValue(view.getItRetry()));
      jobDataMap.put(JobConstant.RETRY_INTERVAL, NumberUtil.getValue(view.getItRetryInterval()));
      jobDataMap.put("JOB", view);
      jobDataMap.put("US", getUserSession());

      jobDetail.setJobDataMap(jobDataMap);

      Trigger trigger;

      if (JobConfigView.TriggerMode.SIMPLE_TRIGGER.equals(triggerMode)) {
         if (startDate == null) startDate = new Date();

         trigger = new SimpleTrigger(jobId + "trigger", Scheduler.DEFAULT_GROUP,
                 startDate, endDate,
                 repetition, interval * 60L * 1000L);
         trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
      } else {
         trigger = new CronTrigger(jobId + "trigger", Scheduler.DEFAULT_GROUP,
                 jobId, Scheduler.DEFAULT_GROUP,
                 startDate, endDate,
                 cronExpression);
         trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY);
      }

      if (isRescheduled) {
         deleteJob(jobId);
      }

      getScheduler().scheduleJob(jobDetail, trigger);
   }

   public static UserSession getUserSession() {
      UserSessionView us = new UserSessionView();

      us.setStUserID("daemon");
      us.setDtTransactionDate(new Date());

      return us;
   }


   public static void triggerJob(JobConfigView view) throws Exception {
      String jobEvent = view.getStJobEvent();
      Class jobClass = (Class) JobConfigView.Jobs.getLookUp().getDescription(jobEvent);

      String jobId = view.getStJobID() + "invoke";
      JobDetail jobDetail = new JobDetail(jobId, Scheduler.DEFAULT_GROUP, jobClass);

      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put(JobConstant.RETRY, NumberUtil.getValue(view.getItRetry()));
      jobDataMap.put(JobConstant.RETRY_INTERVAL, NumberUtil.getValue(view.getItRetryInterval()));
      jobDataMap.put("JOB", view);
      jobDataMap.put("US", getUserSession());
      jobDetail.setJobDataMap(jobDataMap);

      getScheduler().deleteJob(jobId, Scheduler.DEFAULT_GROUP);
      getScheduler().unscheduleJob(jobId + "trigger", Scheduler.DEFAULT_GROUP);
      Trigger trigger = new SimpleTrigger(jobId + "trigger", Scheduler.DEFAULT_GROUP,
              new Date(), null, 0, 0);
      trigger.setMisfireInstruction(Trigger.INSTRUCTION_DELETE_TRIGGER);
      getScheduler().scheduleJob(jobDetail, trigger);
      //if (getScheduler().getJobDetail(jobId, Scheduler.DEFAULT_GROUP) == null) {
      //scheduleJob(view, false);
      //}

      //getScheduler().triggerJob(jobId, Scheduler.DEFAULT_GROUP);
   }

   public static void deleteJob(String jobId) throws Exception {
      logger.logInfo("deleting job: " + jobId);
      getScheduler().deleteJob(jobId, Scheduler.DEFAULT_GROUP);
   }

   public static DTOList getNextJobs(String jobId) {
      DTOList dtoList = new DTOList();
      try {
         dtoList = getRemoteJob().getNextJobs(jobId);
      } catch (Exception e) {
         logger.logError(e.getMessage());
      }
      return dtoList;
   }

   public static JobConfigView getJobConfigView(String jobId) {
      try {
         return getRemoteJob().getJobConfigView(jobId);
      } catch (Exception e) {
         logger.logError(e.getMessage());
      }
      return null;
   }

   public static JobConfigView getJob(JobExecutionContext jobExecutionContext) {
      return (JobConfigView) jobExecutionContext.getJobDetail().getJobDataMap().get("JOB");
   }

   public static UserSession getUserSession(JobExecutionContext jobExecutionContext) {
      return (UserSession) jobExecutionContext.getJobDetail().getJobDataMap().get("US");
   }

   public static boolean isServerProduction(){
        String hostname = "Unknown";
        boolean isServer = false;

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

            logger.logDebug("Hostname : "+ hostname);
            logger.logDebug("ip : "+ addr.getHostAddress());

            if(addr.getHostAddress().trim().equalsIgnoreCase(serverIP))
                isServer = true;
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }

        return isServer;
    }

   public static void triggerEndorseJob(JobConfigView view) throws Exception {
      String jobEvent = view.getStJobEvent();
      Class jobClass = (Class) JobConfigView.Jobs.getLookUp().getDescription(jobEvent);

      String jobId = view.getStJobID() + "invoke";
      JobDetail jobDetail = new JobDetail(jobId, Scheduler.DEFAULT_GROUP, jobClass);

      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put(JobConstant.RETRY, NumberUtil.getValue(view.getItRetry()));
      jobDataMap.put(JobConstant.RETRY_INTERVAL, NumberUtil.getValue(view.getItRetryInterval()));
      jobDataMap.put("JOB", view);
      jobDataMap.put("US", getUserSession());
      jobDetail.setJobDataMap(jobDataMap);

      getScheduler().deleteJob(jobId, Scheduler.DEFAULT_GROUP);
      getScheduler().unscheduleJob(jobId + "trigger", Scheduler.DEFAULT_GROUP);
      Trigger trigger = new SimpleTrigger(jobId + "trigger", Scheduler.DEFAULT_GROUP,
              new Date(), null, 0, 0);
      trigger.setMisfireInstruction(Trigger.INSTRUCTION_DELETE_TRIGGER);
      getScheduler().scheduleJob(jobDetail, trigger);
      //if (getScheduler().getJobDetail(jobId, Scheduler.DEFAULT_GROUP) == null) {
      //scheduleJob(view, false);
      //}

      //getScheduler().triggerJob(jobId, Scheduler.DEFAULT_GROUP);
   }

}

