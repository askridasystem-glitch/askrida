/***********************************************************************
 * Module:  com.webfin.gl.model.AccountView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 11:37:01 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class AccountDivisionView extends DTO implements RecordAudit {

   /*

   CREATE TABLE gl_accounts
(
   int8 NOT NULL,
   varchar(32) NOT NULL,
   varchar(5) NOT NULL,
   varchar(1),
   numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT accountid_pk PRIMARY KEY (account_id)
)
   */

   public static String tableName = "gl_accounts_division";

   public static String comboFields[] = {"accountno","description"};

   public static String fieldMap[][] = {
      {"stAccountHeaderNo","account_header"},
      {"stDivisionCode","division_code"},
      {"stDescription","description"},
      {"stCostCenterCode","cc_code"},
      {"stRekeningNo","rekno"},
   };


   private String stAccountHeaderNo;
   private String stDivisionCode;
   private String stDescription;
   private String stCostCenterCode;
   private String stRekeningNo;

    public String getStAccountHeaderNo() {
        return stAccountHeaderNo;
    }

    public void setStAccountHeaderNo(String stAccountHeaderNo) {
        this.stAccountHeaderNo = stAccountHeaderNo;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStDivisionCode() {
        return stDivisionCode;
    }

    public void setStDivisionCode(String stDivisionCode) {
        this.stDivisionCode = stDivisionCode;
    }

    public String getStRekeningNo() {
        return stRekeningNo;
    }

    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
    }


   
}
