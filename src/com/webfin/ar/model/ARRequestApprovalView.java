/***********************************************************************
 * Module:  com.webfin.ar.model.ARRequestApprovalView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import java.util.Date;

public class ARRequestApprovalView extends DTO implements RecordAudit {

    public static String tableName = "ar_request_approval";
    public static String comboFields[] = {"app_in_id"};
    public static String fieldMap[][] = {
        {"stApprovalInID", "app_in_id*pk"},
        {"stInID", "in_id"},
        {"stApprovalWho", "approval_who"},
        {"dtApprovalDate", "approval_date"},
        {"stEffectiveFlag", "eff_flag"},};
    private String stApprovalInID;
    private String stInID;
    private String stApprovalWho;
    private Date dtApprovalDate;
    private String stEffectiveFlag = "N";

    public String getStInID() {
        return stInID;
    }

    public void setStInID(String stInID) {
        this.stInID = stInID;
    }

    /**
     * @return the stApprovalInID
     */
    public String getStApprovalInID() {
        return stApprovalInID;
    }

    /**
     * @param stApprovalInID the stApprovalInID to set
     */
    public void setStApprovalInID(String stApprovalInID) {
        this.stApprovalInID = stApprovalInID;
    }

    /**
     * @return the stApprovalWho
     */
    public String getStApprovalWho() {
        return stApprovalWho;
    }

    /**
     * @param stApprovalWho the stApprovalWho to set
     */
    public void setStApprovalWho(String stApprovalWho) {
        this.stApprovalWho = stApprovalWho;
    }

    /**
     * @return the dtApprovalDate
     */
    public Date getDtApprovalDate() {
        return dtApprovalDate;
    }

    /**
     * @param dtApprovalDate the dtApprovalDate to set
     */
    public void setDtApprovalDate(Date dtApprovalDate) {
        this.dtApprovalDate = dtApprovalDate;
    }

    /**
     * @return the stEffective
     */
    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    /**
     * @param stEffective the stEffective to set
     */
    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public boolean isEffective() {
        return Tools.isYes(stEffectiveFlag);
    }
}
