/***********************************************************************
 * Module:  com.crux.common.jobs.ShellExecutorJob
 * Author:  Denny Mahendra
 * Created: Aug 10, 2004 11:58:46 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.jobs;

import com.crux.util.validation.FieldValidator;
import com.crux.util.LogManager;
import com.crux.jobs.util.JobUtil;
import com.crux.jobs.model.JobConfigView;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.*;

public class ShellExecutorJob implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ShellExecutorJob.class);

    public final static transient FieldValidator param1Validator = new FieldValidator("param1", "Shell Command", "string", 255);
    public final static transient FieldValidator param2Validator = new FieldValidator("param2", "Pipe Arguments", "string", 255);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final JobConfigView job = JobUtil.getJob(jobExecutionContext);

            logger.logDebug("execute: executing shell : " + job.getStParam1() + " | " + job.getStParam2());

            final Runtime rtime = Runtime.getRuntime();
            Process child = rtime.exec(job.getStParam1());

            if (job.getStParam2() != null) {
                BufferedWriter outCommand =
                        new BufferedWriter(new OutputStreamWriter(child.getOutputStream()));
                outCommand.write(job.getStParam2());
                outCommand.newLine();
                outCommand.flush();
                outCommand.close();
            }

            final InputStream is = child.getInputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));

            final StringBuffer sz = new StringBuffer();

            String s;

            while ((s = br.readLine()) != null) {
                logger.logInfo(job.getStJobID() + ":" + s);
            }

            child.waitFor();

            int retCode = child.exitValue();

            logger.logInfo("execute: retcode = " + retCode);

            if (retCode != 0) throw new Exception("Execution failed (" + retCode + ")");

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
