/***********************************************************************
 * Module:  com.crux.login.model.uploadEndorsemenView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.reinsurance.model;

import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.UserSession;
import com.crux.file.FileView;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.UserLogView;
import com.crux.login.model.UserRoleView;
import com.webfin.gl.model.GLCostCenterView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.system.region.model.RegionView;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

public class ReinsuranceValidationDetailView extends DTO implements RecordAudit {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    CREATE TABLE ins_policy_approval_upload_detail
    (
    ins_policy_approval_upload_dtl_id bigint NOT NULL,
    ins_policy_approval_upload_id bigint,
    pol_id bigint,
    pol_no character varying(32),
    description text,
    status character varying(32),
    file_id character varying(255),
    recap_no character varying(255),
    file_physic bigint,
    create_date timestamp without time zone,
    create_who character varying(32),
    change_date timestamp without time zone,
    change_who character varying(32),
    auto_approve_flag character varying(1),
    approved_who character varying(32),
    bypass_validasi_f character varying(1),
    CONSTRAINT ins_policy_approval_upload_detail_pkey PRIMARY KEY (ins_policy_approval_upload_dtl_id)
    )
     */
    public static String tableName = "ins_policy_reins_validation_detail";

    public transient static String fieldMap[][] = {
        {"stInsPolicyReinsuranceValidationDetailID", "ins_policy_reins_vld_dtl_id*pk*nd"},
        {"stInsurancePolicyReinsuranceValidationID", "ins_policy_reins_vld_id"},
        {"stPolicyID", "pol_id"},
        {"stPolicyNo", "pol_no"},
        {"stAutoApproveFlag", "auto_approve_flag"},
        {"stApprovedWho", "approved_who"},
        {"stStatus", "status"},
        {"stDescription", "description"},};

    private String stInsPolicyReinsuranceValidationDetailID;
    private String stInsurancePolicyReinsuranceValidationID;
    private String stPolicyID;
    private String stPolicyNo;
    private String stAutoApproveFlag;
    private String stApprovedWho;
    private String stDescription;
    private String stStatus;

    public String getStInsPolicyReinsuranceValidationDetailID() {
        return stInsPolicyReinsuranceValidationDetailID;
    }

    public void setStInsPolicyReinsuranceValidationDetailID(String stInsPolicyReinsuranceValidationDetailID) {
        this.stInsPolicyReinsuranceValidationDetailID = stInsPolicyReinsuranceValidationDetailID;
    }

    public String getStInsurancePolicyReinsuranceValidationID() {
        return stInsurancePolicyReinsuranceValidationID;
    }

    public void setStInsurancePolicyReinsuranceValidationID(String stInsurancePolicyReinsuranceValidationID) {
        this.stInsurancePolicyReinsuranceValidationID = stInsurancePolicyReinsuranceValidationID;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

    public String getStAutoApproveFlag() {
        return stAutoApproveFlag;
    }

    public void setStAutoApproveFlag(String stAutoApproveFlag) {
        this.stAutoApproveFlag = stAutoApproveFlag;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }
}
