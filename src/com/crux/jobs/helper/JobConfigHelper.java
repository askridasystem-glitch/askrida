/***********************************************************************
 * Module:  com.crux.jobs.helper.JobConfigHelper
 * Author:  Denny Mahendra
 * Created: Jun 15, 2004 6:28:49 PM
 * Purpose:
 ***********************************************************************/

package com.crux.jobs.helper;

import com.crux.common.controller.Helper;
import com.crux.jobs.ejb.JobConfig;
import com.crux.jobs.ejb.JobConfigHome;
import com.crux.jobs.model.JobConfigView;
import com.crux.jobs.util.JobUtil;
import com.crux.jobs.filter.JobLogFilter;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.LookUpUtil;
import com.crux.util.NumberUtil;
import com.crux.jobs.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JobConfigHelper extends Helper {
    private final static transient LogManager logger = LogManager.getInstance(JobConfigHelper.class);


    private JobConfig getRemoteJob() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((JobConfigHome) JNDIUtil.getInstance().lookup("JobConfigEJB", JobConfigHome.class.getName()))
                .create();
    }

    public void listJob(HttpServletRequest rq) throws Exception {
        final DTOList l = getRemoteJob().getAllJobs();
        rq.setAttribute("JOB_LIST", l);
    }

    public void saveJob(HttpServletRequest rq) throws Exception {
        JobConfigView view = getJobConfigFromRequest(rq);
        String result = getRemoteJob().saveJob(view);
        boolean enabled = "Y".equals(view.getStEnabled());
        boolean emptyStartedJob = view.getStStarterJobID() == null || "".equals(view.getStStarterJobID());
        final SchedulerServlet scheduler = SchedulerServlet.getInstance();

        if (scheduler != null)
            if (enabled && emptyStartedJob) {
                JobUtil.scheduleJob(view, view.isUpdate());
            } else {
                JobUtil.deleteJob(view.getStJobID());
            }
        rq.setAttribute("ACTION", "SAVE");
        rq.setAttribute("RESULT", result);
    }

    public void createJob(HttpServletRequest rq) throws Exception {
        final JobConfigView view = new JobConfigView();

        view.markNew();
        view.setStEnabled("N");

        put(rq, "JOB", view);
        view.setAttribute("ACTION", "CREATE");
        populateJob(rq);
    }

    public void editJob(HttpServletRequest rq) throws Exception {
        final String jobId = getString(rq.getParameter("jobId"));
        final JobConfigView view = getRemoteJob().getJobConfigView(jobId);

        view.markUpdate();
        put(rq, "JOB", view);
        view.setAttribute("ACTION", "EDIT");
        populateJob(rq);
    }


    public void populateJob(HttpServletRequest rq) throws Exception {
        final JobConfigView view = (JobConfigView) get(rq, "JOB");

        final Class jobClass = (Class) JobConfigView.Jobs.getLookUp().getDescription(view.getStJobEvent());

        if (jobClass != null) {
            for (int i = 1; i <= 5; i++) {
                final String k = "param" + i + "Validator";
                view.setAttribute(k, null);
                try {
                    final Field f = jobClass.getField(k);
                    view.setAttribute(k, f.get(null));
                } catch (NoSuchFieldException e) {
                }
            }
        }

        if (jobClass != null) {
            for (int i = 1; i <= 5; i++) {
                final String k = "getLOVparam" + i;
                view.setAttribute("param" + i + "LOV", null);
                try {
                    final Method m = jobClass.getMethod(k, null);
                    final LookUpUtil lu = (LookUpUtil) m.invoke(null, null);
                    view.setAttribute("param" + i + "LOV", lu);
                } catch (NoSuchMethodException e) {
                }
            }
        }

        rq.setAttribute("ACTION", view.getAttribute("ACTION"));
        rq.setAttribute("JOB_CONFIG", view);
    }

    public void changeEvent(HttpServletRequest rq) throws Exception {
        getJobConfigFromRequest(rq);
        populateJob(rq);
    }

    public void deleteJob(HttpServletRequest rq) throws Exception {
        final String jobId = getString(rq.getParameter("jobId"));
        getRemoteJob().deleteJob(jobId);

        final SchedulerServlet sched = SchedulerServlet.getInstance();

        if (sched != null)
            JobUtil.deleteJob(jobId);

        rq.setAttribute("ACTION", "DELETE");
    }


    private JobConfigView getJobConfigFromRequest(HttpServletRequest rq) throws Exception {
        final JobConfigView view = (JobConfigView) get(rq, "JOB");

        view.setStJobID(getString(rq.getParameter("jobId")));
        view.setStJobEvent(getString(rq.getParameter("jobEvent")));
        view.setStTriggerMode(getString(rq.getParameter("triggerMode")));

        view.setStParam1(getString(rq.getParameter("param1")));
        view.setStParam2(getString(rq.getParameter("param2")));
        view.setStParam3(getString(rq.getParameter("param3")));
        view.setStParam4(getString(rq.getParameter("param4")));
        view.setStParam5(getString(rq.getParameter("param5")));


        String sStartDate = getString(rq.getParameter("startdate"));
        String sEndDate = rq.getParameter("enddate");
        String sStartTime = rq.getParameter("starttime");
        String sEndTime = rq.getParameter("endtime");
        sStartTime = sStartTime == null ? "" : sStartTime;
        sEndTime = sEndTime == null ? "" : sEndTime;

        SimpleDateFormat cdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date startDate;
        if (sStartDate == null || "".equals(sStartDate)) {
            startDate = null;
        } else {
            if ("".equals(sStartTime)) {
                startDate = DateUtil.getDate(sStartDate);
            } else {
                startDate = cdf.parse((sStartDate + " " + sStartTime).trim());
            }
        }

        Date endDate;
        if (sEndDate == null || "".equals(sEndDate)) {
            endDate = null;
        } else {
            if ("".equals(sEndTime)) {
                endDate = DateUtil.getDate(sEndDate);
            } else {
                endDate = cdf.parse((sEndDate + " " + sEndTime).trim());
            }
        }

        view.setDtStart(startDate);
        view.setDtEnd(endDate);
        view.setStEnabled(getString(rq.getParameter("enabled")));
        view.setLgRepeatNum(getLong(rq.getParameter("repetition")));

        view.setLgInterval(getLong(rq.getParameter("interval")));
        view.setStCRONExpression(getString(rq.getParameter("cronExpression")));
        view.setStEnabled(getString(rq.getParameter("status")));
        view.setItRetry(NumberUtil.getInteger(rq.getParameter("retry")));
        view.setStStarterJobID(getString(rq.getParameter("job_starter")));
        view.setItRetryInterval(NumberUtil.getInteger(rq.getParameter("retry_interval")));
        view.setItStarterJobInterval(NumberUtil.getInteger(rq.getParameter("job_starter_interval")));

        return view;
    }

    public void triggerJob(HttpServletRequest rq) throws Exception {
        final String jobId = getString(rq.getParameter("jobId"));
        final JobConfigView view = getRemoteJob().getJobConfigView(jobId);
        JobUtil.triggerJob(view);

        rq.setAttribute("ACTION", "TRIGGER");
    }

    public static LookUpUtil availableJobLookUp() throws Exception {
        return ListUtil.getLookUpFromQuery("  select job_id, job_id as job_id2 from " + JobConfigView.tableName +
                "  where enabled_flag = 'Y' order by job_id");
    }

    public JobConfigView getJobConfigView(String jobId) throws Exception {
        return JobUtil.getJobConfigView(jobId);
    }


    
	public void invokeJob(HttpServletRequest rq)  throws Exception {
	
	final String jobId = getString(rq.getParameter("jobId"));
	final JobConfigView view = getRemoteJob().getJobConfigView(jobId);
	
	final Class Cls = (Class) JobConfigView.Jobs.getLookUp().getDescription(view.getStJobEvent());
	
	logger.logDebug("invokeJob: class = "+Cls);
	
	if (Cls == null) throw new Exception("Job not found");
	
	final Executable job = (Executable) Cls.newInstance();
	
	job.execute();
	
	}

    public void viewJobLog(HttpServletRequest rq) throws Exception {
        JobLogFilter f = (JobLogFilter) get(rq, "FILTER");

        if (f == null) {
            f = new JobLogFilter();
            f.orderKey = "job_date";
            f.orderDir = -1;
            put(rq, "FILTER", f);
        }

        updatePaging(rq, f);

        final DTOList l = getRemoteJob().viewJobLog(f);

        rq.setAttribute("LIST", l);
    }
}
