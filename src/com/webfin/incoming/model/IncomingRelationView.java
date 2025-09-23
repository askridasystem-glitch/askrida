/***********************************************************************
 * Module:  com.webfin.entity.model.EntityRelationView
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:04:24 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.incoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class IncomingRelationView extends DTO implements RecordAudit {

   public static String tableName = "ent_rel";

   public static String fieldMap[][] = {
      {"stEntityRelationID","ent_rel_id*pk"},
      {"stRelationCode","rel_code"},
      {"stEmploymentPosition","emp_position"},
      {"stEmploymentIncome","emp_income"},
      {"stEmploymentIncomeCurrency","emp_incm_ccy"},
      {"stEmploymentIncomeAmount","emp_incm_amt"},
      {"stEntityID1","ent_id1"},
      {"stEntityID2","ent_id2"},
   };

   private String stEntityRelationID;
   private String stRelationCode;
   private String stEmploymentPosition;
   private String stEmploymentIncome;
   private String stEmploymentIncomeCurrency;
   private String stEmploymentIncomeAmount;
   private String stEntityID1;
   private String stEntityID2;

   public String getStEntityRelationID() {
      return stEntityRelationID;
   }

   public void setStEntityRelationID(String stEntityRelationID) {
      this.stEntityRelationID = stEntityRelationID;
   }

   public String getStRelationCode() {
      return stRelationCode;
   }

   public void setStRelationCode(String stRelationCode) {
      this.stRelationCode = stRelationCode;
   }

   public String getStEmploymentPosition() {
      return stEmploymentPosition;
   }

   public void setStEmploymentPosition(String stEmploymentPosition) {
      this.stEmploymentPosition = stEmploymentPosition;
   }

   public String getStEmploymentIncome() {
      return stEmploymentIncome;
   }

   public void setStEmploymentIncome(String stEmploymentIncome) {
      this.stEmploymentIncome = stEmploymentIncome;
   }

   public String getStEmploymentIncomeCurrency() {
      return stEmploymentIncomeCurrency;
   }

   public void setStEmploymentIncomeCurrency(String stEmploymentIncomeCurrency) {
      this.stEmploymentIncomeCurrency = stEmploymentIncomeCurrency;
   }

   public String getStEmploymentIncomeAmount() {
      return stEmploymentIncomeAmount;
   }

   public void setStEmploymentIncomeAmount(String stEmploymentIncomeAmount) {
      this.stEmploymentIncomeAmount = stEmploymentIncomeAmount;
   }

   public String getStEntityID1() {
      return stEntityID1;
   }

   public void setStEntityID1(String stEntityID1) {
      this.stEntityID1 = stEntityID1;
   }

   public String getStEntityID2() {
      return stEntityID2;
   }

   public void setStEntityID2(String stEntityID2) {
      this.stEntityID2 = stEntityID2;
   }
}
