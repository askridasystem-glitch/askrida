/***********************************************************************
 * Module:  com.crux.login.model.RoleView
 * Author:  Denny Mahendra
 * Created: Apr 27, 2004 1:39:58 PM
 * Purpose:
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;
import com.crux.util.DTOList;

import java.util.Date;
import java.math.BigDecimal;

public class RoleView extends DTO implements RecordAudit {
   private String stRoleID;
   private String stRoleName;
   private Date dtActiveDate;
   private Date dtInActiveDate;
   private BigDecimal dbTransactionLimit;
   private BigDecimal dbReference1;
   private BigDecimal dbReference2;
   private BigDecimal dbReference3;

   private DTOList checbox1;
   private DTOList checbox2;
   private DTOList checbox3;
   private DTOList checbox4;

   public static String comboFields[] = {"role_id", "role_name"};


   public static String tableName = "s_roles";

   public static String fieldMap[][] = {
      {"stRoleID", "role_id*pk"},
      {"stRoleName", "role_name"},
      {"dtActiveDate", "active_date"},
      {"dtInActiveDate", "inactive_date"},
      {"dbTransactionLimit", "TRANSACTION_LIMIT"},
      {"dbReference1", "refn1"},
      {"dbReference2", "refn2"},
      {"dbReference3", "refn3"},
   };
   
   public BigDecimal getDbReference1() {
      return dbReference1;
   }

   public void setDbReference1(BigDecimal dbReference1) {
      this.dbReference1 = dbReference1;
   }

   public BigDecimal getDbReference2() {
      return dbReference2;
   }

   public void setDbReference2(BigDecimal dbReference2) {
      this.dbReference2 = dbReference2;
   }

   public BigDecimal getDbReference3() {
      return dbReference3;
   }

   public void setDbReference3(BigDecimal dbReference3) {
      this.dbReference3 = dbReference3;
   }

   public BigDecimal getDbTransactionLimit() {
      return dbTransactionLimit;
   }

   public void setDbTransactionLimit(BigDecimal dbTransactionLimit) {
      this.dbTransactionLimit = dbTransactionLimit;
   }

   public String getStRoleID() {
      return stRoleID;
   }

   public void setStRoleID(String stRoleID) {
      this.stRoleID = stRoleID;
   }

   public String getStRoleName() {
      return stRoleName;
   }

   public void setStRoleName(String stRoleName) {
      this.stRoleName = stRoleName;
   }

   public Date getDtActiveDate() {
      return dtActiveDate;
   }

   public void setDtActiveDate(Date dtActiveDate) {
      this.dtActiveDate = dtActiveDate;
   }

   public Date getDtInActiveDate() {
      return dtInActiveDate;
   }

   public void setDtInActiveDate(Date dtInActiveDate) {
      this.dtInActiveDate = dtInActiveDate;
   }

   public DTOList getChecbox1() {
      return checbox1;
   }

   public void setChecbox1(DTOList checbox1) {
      this.checbox1 = checbox1;
   }

   public DTOList getChecbox2() {
      return checbox2;
   }

   public void setChecbox2(DTOList checbox2) {
      this.checbox2 = checbox2;
   }

   public DTOList getChecbox3() {
      return checbox3;
   }

   public void setChecbox3(DTOList checbox3) {
      this.checbox3 = checbox3;
   }

   public DTOList getChecbox4() {
      return checbox4;
   }

   public void setChecbox4(DTOList checbox4) {
      this.checbox4 = checbox4;
   }
}
