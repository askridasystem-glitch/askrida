/***********************************************************************
 * Module:  com.webfin.insurance.model.BiayaOperasionalGroup
 * Author:  Denny Mahendra
 * Created: Mar 10, 2006 5:18:26 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class BiayaOperasionalGroup extends DTO implements RecordAudit {

    public static String tableName = "s_biaop_group";
    
    public static String fieldMap[][] = {
        {"stBiaopGroupID", "biaop_grp_id*pk"},
        {"stDescription", "description"},
        {"stActiveFlag", "active_flag"},
        {"stGroupID", "group_id"},
        {"stGroupDesc", "group_desc"},
        {"stAccount", "account"},
        {"staccount2", "account2"},
    };
    
    private String stBiaopGroupID;
    private String stDescription;
    private String stActiveFlag;
    private String stGroupID;
    private String stGroupDesc;
    private String stAccount;
    private String staccount2;

    /**
     * @return the stInsurancePolicyTypeGroupID
     */
    public String getStBiaopGroupID() {
        return stBiaopGroupID;
    }

    /**
     * @param stInsurancePolicyTypeGroupID the stInsurancePolicyTypeGroupID to set
     */
    public void setStBiaopGroupID(String stBiaopGroupID) {
        this.stBiaopGroupID = stBiaopGroupID;
    }

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
     * @return the stGroupID
     */
    public String getStGroupID() {
        return stGroupID;
    }

    /**
     * @param stGroupID the stGroupID to set
     */
    public void setStGroupID(String stGroupID) {
        this.stGroupID = stGroupID;
    }

    /**
     * @return the stGroupDesc
     */
    public String getStGroupDesc() {
        return stGroupDesc;
    }

    /**
     * @param stGroupDesc the stGroupDesc to set
     */
    public void setStGroupDesc(String stGroupDesc) {
        this.stGroupDesc = stGroupDesc;
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
     * @return the staccount2
     */
    public String getStaccount2() {
        return staccount2;
    }

    /**
     * @param staccount2 the staccount2 to set
     */
    public void setStaccount2(String staccount2) {
        this.staccount2 = staccount2;
    }
}
