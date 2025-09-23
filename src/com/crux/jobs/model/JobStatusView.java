/***********************************************************************
 * Module:  com.crux.jobs.model.JobStatusView
 * Author:  Denny Mahendra
 * Created: Aug 24, 2004 4:49:16 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.jobs.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.util.Date;

public class JobStatusView extends DTO implements RecordAudit {
    private String stJobLogID;
    private String stJobID;
    private String stJobStatus;
    private String stStatusMessage;
    private Date dtJobDate;

    public static String tableName = "job_log";

    public static String fieldMap[][] = {
        {"stJobLogID", "job_log_id*pk"},
        {"stJobID", "job_id"},
        {"stJobStatus", "job_status"},
        {"stStatusMessage", "status_message"},
        {"dtJobDate", "job_date"},
    };

    public String getStJobLogID() {
        return stJobLogID;
    }

    public void setStJobLogID(String stJobLogID) {
        this.stJobLogID = stJobLogID;
    }

    public String getStJobID() {
        return stJobID;
    }

    public void setStJobID(String stJobID) {
        this.stJobID = stJobID;
    }

    public String getStJobStatus() {
        return stJobStatus;
    }

    public void setStJobStatus(String stJobStatus) {
        this.stJobStatus = stJobStatus;
    }

    public String getStStatusMessage() {
        return stStatusMessage;
    }

    public void setStStatusMessage(String stStatusMessage) {
        this.stStatusMessage = stStatusMessage;
    }

    public Date getDtJobDate() {
        return dtJobDate;
    }

    public void setDtJobDate(Date dtJobDate) {
        this.dtJobDate = dtJobDate;
    }
}
