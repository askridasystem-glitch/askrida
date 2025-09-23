/***********************************************************************
 * Module:  com.webfin.gl.model.AccountInsuranceView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 11:37:01 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;

import java.math.BigDecimal;

public class AccountInsuranceView extends DTO implements RecordAudit {
    
   /*
CREATE TABLE gl_accounts_insurance
(
  accountno text NOT NULL,
  description character varying(255),
  active_flag character varying(1),
  CONSTRAINT gl_accounts_insurance_pkey PRIMARY KEY (accountno )
)
    */
    
    private String stAccountNo;
    private String stDescription;
    private String stActiveFlag;
    private String stCostCenterCode;
    private String stGLInsuranceID;
    
    public static String tableName = "gl_accounts_insurance2";
    
    //public static String comboFields[] = {"account_id","accountno","description","noper","rekno"};
    
    public static String fieldMap[][] = {

        {"stGLInsuranceID","gl_ins_id*pk"},
        {"stAccountNo","accountno"},
        {"stDescription","description"},
        {"stActiveFlag","active_flag"},
        {"stCostCenterCode","cc_code"},
    };

    public String getStAccountNo() {
        return stAccountNo;
    }

    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stGLInsuranceID
     */
    public String getStGLInsuranceID() {
        return stGLInsuranceID;
    }

    /**
     * @param stGLInsuranceID the stGLInsuranceID to set
     */
    public void setStGLInsuranceID(String stGLInsuranceID) {
        this.stGLInsuranceID = stGLInsuranceID;
    }
}
