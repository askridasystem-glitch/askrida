package com.crux.jobs.ejb;

import com.crux.jobs.model.JobConfigView;
import com.crux.jobs.model.JobStatusView;
import com.crux.jobs.filter.JobLogFilter;
import com.crux.util.*;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <explain class description>
 * <p/>
 * Created: Jun 16, 2004 - 3:05:21 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public class JobConfigEJB implements SessionBean {
    private static final LogManager logger = LogManager.getInstance(JobConfigEJB.class);
    private SessionContext ctx;

    public void setSessionContext(SessionContext sessionContext) {
        ctx = sessionContext;
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbCreate() {
    }

    public DTOList viewJobLog(JobLogFilter f) throws Exception {
        return ListUtil.getDTOListFromQuery("select * from job_log " + ListUtil.getOrderExpression(f),
                JobStatusView.class, f);
    }

    public DTOList getAllJobs() throws Exception {
        DTOList l = null;
        l = ListUtil.getDTOListFromQuery(" select " +
                "    job_config.*" +
                " from " + JobConfigView.tableName +
                "    order by job_id",
                JobConfigView.class);

        return l;
    }

    public JobConfigView getJobConfigView(String jobId) throws Exception {
        DTOList dtoList = ListUtil.getDTOListFromQuery(" select * from " + JobConfigView.tableName +
                " where job_id = ?",
                new Object[]{jobId},
                JobConfigView.class);
        if (dtoList.size() > 0)
            return (JobConfigView) dtoList.get(0);
        else
            return null;
    }

    public DTOList getNextJobs(String jobId) throws Exception {
        DTOList dtoList = ListUtil.getDTOListFromQuery(" select * from " + JobConfigView.tableName +
                " where job_starter = ?",
                new Object[]{jobId},
                JobConfigView.class);
        return dtoList;
    }

    public String saveJob(JobConfigView view) throws Exception {
        final SQLUtil S = new SQLUtil();
        String stJobId = view.getStJobID();
        String result = "";
        try {
            if (view.isNew()) {
                JobConfigView checkView = getJobConfigView(stJobId);
                if (checkView != null)
                    return "Job ID already exist.";
            }
            S.store(view);
            logger.logDebug("\n******* Saved \n");
            return result;
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public String createJobLogID() throws Exception {
        return String.valueOf(IDFactory.createNumericID("JLG"));
    }

    public String save(JobStatusView joblog) throws Exception {
        if (joblog.isNew()) {
            joblog.setStJobLogID(createJobLogID());
        }

        final SQLUtil S = new SQLUtil();

        try {
            S.store(joblog);

            return joblog.getStJobLogID();
        } finally {
            S.release();
        }
    }

    public JobStatusView getJobLog(String stJobLogID) throws Exception {
        final DTOList l = ListUtil.getDTOListFromQuery("select * from job_log where job_log_id = ?",
                new Object[]{stJobLogID},
                JobStatusView.class);

        if (l.size() > 0) return (JobStatusView) l.get(0);

        return null;
    }

    public void deleteJob(String jobId) throws Exception {
        final JobConfigView view = new JobConfigView();
        final SQLUtil s = new SQLUtil();
        view.setStJobID(jobId);

        try {
            s.delete(view);
        } finally {
            s.release();
        }

    }

}

