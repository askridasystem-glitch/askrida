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

public class AccountView extends DTO implements RecordAudit {

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

   private Long lgAccountID;
   private String stAccountNo;
   private String stAccountNo2;
   private String stDescription;
   private String stAccountType;
   private String stAllocatedFlag;
   private BigDecimal dbBalanceOpen;
   private String stEnabled;
   private String stEntityID;
   private String stEntityName;
   private String stAccountID;
   
   private String stCostCenterCode;
   private String stRekeningNo;
   private String stAnggaran;
   private String stSisaAnggaran;

   private String stDivisionCode;

   public static String tableName = "gl_accounts";

   public static String comboFields[] = {"accountno","description"};
    
   public static String fieldMap[][] = {
      {"lgAccountID","account_id*pk"},
      {"stAccountNo","accountno"},
      {"stAccountNo2","accountno2"},
      {"stAccountType","acctype"},
      {"stAllocatedFlag","allocated_flag"},
      {"dbBalanceOpen","bal_open"},
      {"stDescription","description"},
      {"stEnabled","enabled"},
      {"stAccountID","accountid2*n"},
      {"stCostCenterCode","cc_code"},
      {"stRekeningNo","rekno"},
      {"stDivisionCode","division_code*n"},
      {"stAnggaran","anggaran*n"},
      {"stSisaAnggaran","sisaanggaran*n"},
   };

       public String getStDivisionCode() {
        return stDivisionCode;
    }

    public void setStDivisionCode(String stDivisionCode) {
        this.stDivisionCode = stDivisionCode;
    }
   
   public String getStEntityName() {
      return stEntityName;
   }

   public void setStEntityName(String stEntityName) {
      this.stEntityName = stEntityName;
   }
   
   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }
    
   public String getStEnabled() {
      return stEnabled;
   }

   public void setStEnabled(String stEnabled) {
      this.stEnabled = stEnabled;
   }

   public String getStAccountNo2() {
      return stAccountNo2;
   }

   public void setStAccountNo2(String stAccountNo2) {
      this.stAccountNo2 = stAccountNo2;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Long getLgAccountID() {
      return lgAccountID;
   }

   public void setLgAccountID(Long lgAccountID) {
      this.lgAccountID = lgAccountID;
   }

   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }

   public String getStAccountType() {
      return stAccountType;
   }

   public void setStAccountType(String stAccountType) {
      this.stAccountType = stAccountType;
   }

   public String getStAllocatedFlag() {
      return stAllocatedFlag;
   }

   public void setStAllocatedFlag(String stAllocatedFlag) {
      this.stAllocatedFlag = stAllocatedFlag;
   }

   public BigDecimal getDbBalanceOpen() {
      return dbBalanceOpen;
   }

   public void setDbBalanceOpen(BigDecimal dbBalanceOpen) {
      this.dbBalanceOpen = dbBalanceOpen;
   }

   public String getStAccountID() {
      if (lgAccountID==null) return null;
      return String.valueOf(lgAccountID);
   }
   
   public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
   }

   public String getStDescriptionLong() {

      return stAccountNo + "/" + stDescription;
   }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStRekeningNo() {
        return stRekeningNo;
    }

    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
    }

    /**
     * @return the stAnggaran
     */
    public String getStAnggaran() {
        return stAnggaran;
    }

    /**
     * @param stAnggaran the stAnggaran to set
     */
    public void setStAnggaran(String stAnggaran) {
        this.stAnggaran = stAnggaran;
    }

    /**
     * @return the stSisaAnggaran
     */
    public String getStSisaAnggaran() {
        return stSisaAnggaran;
    }

    /**
     * @param stSisaAnggaran the stSisaAnggaran to set
     */
    public void setStSisaAnggaran(String stSisaAnggaran) {
        this.stSisaAnggaran = stSisaAnggaran;
    }
}
