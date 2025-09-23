/***********************************************************************
 * Module:  com.webfin.insurance.model.RincianOJKReport
 * Author:  Denny Mahendra
 * Created: Mar 10, 2006 5:18:26 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class RincianOJKReport extends DTO implements RecordAudit {

    public static String tableName = "s_report_ojk";
    
    public static String fieldMap[][] = {
        {"stOJKID", "ojk_id*pk"},
        {"stReference1", "descriptionref1"},
        {"stDescription", "description"},
        {"stActiveFlag", "active_flag"},
        {"stAccount", "account"},
    };
    
    private String stOJKID;
    private String stDescription;
    private String stReference1;
    private String stActiveFlag;
    private String stAccount;

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stAccount
     */
    public String getStAccount() {
        return stAccount;
    }

    /**
     * @param stAccount the stAccount to set
     */
    public void setStAccount(String stAccount) {
        this.stAccount = stAccount;
    }

    /**
     * @return the stOJKID
     */
    public String getStOJKID() {
        return stOJKID;
    }

    /**
     * @param stOJKID the stOJKID to set
     */
    public void setStOJKID(String stOJKID) {
        this.stOJKID = stOJKID;
    }

    /**
     * @return the stReference1
     */
    public String getStReference1() {
        return stReference1;
    }

    /**
     * @param stReference1 the stReference1 to set
     */
    public void setStReference1(String stReference1) {
        this.stReference1 = stReference1;
    }
}
