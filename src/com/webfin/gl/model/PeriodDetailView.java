/***********************************************************************
 * Module:  com.webfin.gl.model.PeriodDetailView
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:39:17 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.util.Date;

public class PeriodDetailView extends DTO implements RecordAudit {

   /*
   CREATE TABLE gl_period_det
(
  gl_period_id int8 NOT NULL,
  period_no int4 NOT NULL,
  startdate timestamp,
  enddate timestamp,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_period_det_pk PRIMARY KEY (gl_period_id, period_no)
)
   */

   private Long lgPeriodID;
   private Long lgPeriodNo;
   private Date dtStartDate;
   private Date dtEndDate;
   private String stClosedFlag;
   private String stPeriodDetailID;
   private Long lgFiscalYear;

   public static String tableName = "gl_period_det";

   public static String fieldMap[][] = {
      {"lgPeriodID","gl_period_id"},
      {"lgPeriodNo","period_no"},
      {"dtStartDate","startdate"},
      {"dtEndDate","enddate"},
      {"lgFiscalYear","fiscal_year*n"},
      {"stClosedFlag","close_flag"},
      {"stPeriodDetailID","gl_period_detail_id*pk"},
   };

   public Long getLgPeriodID() {
      return lgPeriodID;
   }

   public void setLgPeriodID(Long lgPeriodID) {
      this.lgPeriodID = lgPeriodID;
   }

   public Long getLgPeriodNo() {
      return lgPeriodNo;
   }

   public void setLgPeriodNo(Long lgPeriodNo) {
      this.lgPeriodNo = lgPeriodNo;
   }

   public Date getDtStartDate() {
      return dtStartDate;
   }

   public void setDtStartDate(Date dtStartDate) {
      this.dtStartDate = dtStartDate;
   }

   public Date getDtEndDate() {
      return dtEndDate;
   }

   public void setDtEndDate(Date dtEndDate) {
      this.dtEndDate = dtEndDate;
   }

   public void setLgFiscalYear(Long lgFiscalYear) {
      this.lgFiscalYear = lgFiscalYear;
   }

   public Long getLgFiscalYear() {
      return lgFiscalYear;
   }

   public void setStPeriodDetailID(String stPeriodDetailID) {
      this.stPeriodDetailID = stPeriodDetailID;
   }

   public String getStPeriodDetailID() {
      return stPeriodDetailID;
   }

   public void setStClosedFlag(String stClosedFlag) {
      this.stClosedFlag = stClosedFlag;
   }

   public String getStClosedFlag() {
      return stClosedFlag;
   }

   public boolean isOpen() {
      return Tools.isNo(getStClosedFlag());
   }
}
