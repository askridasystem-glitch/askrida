package com.crux.jobs.validator;

import com.crux.util.validation.Validator;
import com.crux.util.validation.FieldValidator;

/**
 * Validator for Job Configuration
 * <p/>
 * Created: Jun 16, 2004 - 1:08:29 PM
 *
 * @author Ian Leonardo
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/26 03:07:30 $
 */

public class JobValidator extends Validator {

    public static FieldValidator vfJobID = new FieldValidator("jobId", "Job ID", "string", 20, MANDATORY);
    public static FieldValidator vfJobEvent = new FieldValidator("jobEvent", "Job Event", "string", 20, MANDATORY);
    public static FieldValidator vfJobStarterId = new FieldValidator("job_starter", "Starter Job ID", "string", 20);
    public static FieldValidator vfTriggerMode = new FieldValidator("triggerMode", "Trigger Mode", "string", 5, MANDATORY);
    public static FieldValidator vfRepetition = new FieldValidator("repetition", "Job Repetition", "integer", 5, MANDATORY);
    public static FieldValidator vfInterval = new FieldValidator("interval", "Job Interval", "integer", 10, MANDATORY);
    public static FieldValidator vfStartDate = new FieldValidator("startdate", "Start Date", "date", -1);
    public static FieldValidator vfEndDate = new FieldValidator("enddate", "End Date", "date", -1);
    public static FieldValidator vfStartTime = new FieldValidator("startime", "Start Time", "time", -1);
    public static FieldValidator vfEndTime = new FieldValidator("endtime", "End Time", "time", -1);
    public static FieldValidator vfCronExpression = new FieldValidator("cronExpression", "Cron Expression", "string", 128, MANDATORY);
    public static FieldValidator vfRetry = new FieldValidator("retry", "Retry", "integer", 2);
    public static FieldValidator vfRetryInterval = new FieldValidator("retry_interval", "Retry Interval", "integer", 4);
    public static FieldValidator vfJobStarterInterval = new FieldValidator("job_starter_interval", "Starter Job Interval", "integer", 4);

}


