/***********************************************************************
 * Module:  com.webfin.insurance.model.BiayaOperasionalDetail
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 9:54:40 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import java.math.BigDecimal;

public class BiayaOperasionalDetail extends DTO implements RecordAudit {
    /*
    CREATE TABLE s_biaop_detail
    (
    biaop_dtl_id bigint NOT NULL,
    description character varying(255),
    active_flag character varying(1),
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    biaop_grp_id bigint,
    account character varying(32),
    account2 character varying(32),
    biaop_grp_desc character varying(32),
    amount_rkap numeric,
    CONSTRAINT s_biaop_detail_pkey PRIMARY KEY (biaop_dtl_id)
    )
     */

    public static String tableName = "s_biaop_detail";
    public static String comboFields[] = {"biaop_dtl_id", "description", "biaop_grp_desc"};
    public static String fieldMap[][] = {
        {"stBiaopDetailID", "biaop_dtl_id*pk"},
        {"stDescription", "description"},
        {"stActiveFlag", "active_flag"},
        {"stBiaopGroupID", "biaop_grp_id"},
        {"stBiaopGroupDesc", "biaop_grp_desc"},
        {"stAccount", "account"},
        {"staccount2", "account2"},
        {"dbAmountRKAP", "amount_rkap"},
        {"stRKAPGroupID", "rkap_group_id"},
        {"stHeadOfficeFlag", "ho_flag"},
        {"stEmail", "email"},
    };
    private String stBiaopDetailID;
    private String stDescription;
    private String stActiveFlag;
    private String stBiaopGroupID;
    private String stBiaopGroupDesc;
    private String stAccount;
    private String staccount2;
    private BigDecimal dbAmountRKAP;
    private String stRKAPGroupID;
    private String stHeadOfficeFlag;
    private String stEmail;

    /**
     * @return the stBiaopDetailID
     */
    public String getStBiaopDetailID() {
        return stBiaopDetailID;
    }

    /**
     * @param stBiaopDetailID the stBiaopDetailID to set
     */
    public void setStBiaopDetailID(String stBiaopDetailID) {
        this.stBiaopDetailID = stBiaopDetailID;
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

    /**
     * @return the dbAmountRKAP
     */
    public BigDecimal getDbAmountRKAP() {
        return dbAmountRKAP;
    }

    /**
     * @param dbAmountRKAP the dbAmountRKAP to set
     */
    public void setDbAmountRKAP(BigDecimal dbAmountRKAP) {
        this.dbAmountRKAP = dbAmountRKAP;
    }

    /**
     * @return the stBiaopGroupID
     */
    public String getStBiaopGroupID() {
        return stBiaopGroupID;
    }

    /**
     * @param stBiaopGroupID the stBiaopGroupID to set
     */
    public void setStBiaopGroupID(String stBiaopGroupID) {
        this.stBiaopGroupID = stBiaopGroupID;
    }

    /**
     * @return the stBiaopGroupDesc
     */
    public String getStBiaopGroupDesc() {
        return stBiaopGroupDesc;
    }

    /**
     * @param stBiaopGroupDesc the stBiaopGroupDesc to set
     */
    public void setStBiaopGroupDesc(String stBiaopGroupDesc) {
        this.stBiaopGroupDesc = stBiaopGroupDesc;
    }

    /**
     * @return the stRKAPGroupID
     */
    public String getStRKAPGroupID() {
        return stRKAPGroupID;
    }

    /**
     * @param stRKAPGroupID the stRKAPGroupID to set
     */
    public void setStRKAPGroupID(String stRKAPGroupID) {
        this.stRKAPGroupID = stRKAPGroupID;
    }

    /**
     * @return the stHeadOfficeFlag
     */
    public String getStHeadOfficeFlag() {
        return stHeadOfficeFlag;
    }

    /**
     * @param stHeadOfficeFlag the stHeadOfficeFlag to set
     */
    public void setStHeadOfficeFlag(String stHeadOfficeFlag) {
        this.stHeadOfficeFlag = stHeadOfficeFlag;
    }

    public boolean isHeadOfficeFlag() {
        return Tools.isYes(stHeadOfficeFlag);
    }

    /**
     * @return the stEmail
     */
    public String getStEmail() {
        return stEmail;
    }

    /**
     * @param stEmail the stEmail to set
     */
    public void setStEmail(String stEmail) {
        this.stEmail = stEmail;
    }
}
