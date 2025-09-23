/***********************************************************************
 * Module:  com.webfin.gl.model.GLChartView
 * Author:  Denny Mahendra
 * Created: Nov 13, 2005 4:20:14 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.webfin.gl.util.GLUtil;

public class GLChartView extends DTO implements RecordAudit {
   /*
   CREATE TABLE gl_chart
(
  account_id int8 NOT NULL,
  account_no varchar(32),
  description varchar(128),
  account_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_char_pk PRIMARY KEY (account_id)
)
WITHOUT OIDS;
   */

   public static String tableName = "gl_chart";

   public static String fieldMap[][] = {
      {"stAccountID","account_id*pk"},
      {"stAccountNo","accountno"},
      {"stDescription","description"},
      {"stAccountType","account_type"},
      {"stEnabled","enabled"},
   };

   private String stAccountID;
   private String stAccountNo;
   private String stDescription;
   private String stAccountType;
   public String stAccountCharCode;
   private String stEnabled;
   
   public String getStEnabled() {
      return stEnabled;
   }

   public void setStEnabled(String stEnabled) {
      this.stEnabled = stEnabled;
   }

   public String getStAccountID() {
      return stAccountID;
   }

   public void setStAccountID(String stAccountID) {
      this.stAccountID = stAccountID;
   }

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

   public String getStAccountType() {
      return stAccountType;
   }

   public void setStAccountType(String stAccountType) {
      this.stAccountType = stAccountType;
   }

   public String getStAccountChartCode() {
      if (stAccountCharCode==null)
         stAccountCharCode = GLUtil.Chart.getInstance().getChartCodeOnly(stAccountNo);

      return stAccountCharCode;
   }
}
