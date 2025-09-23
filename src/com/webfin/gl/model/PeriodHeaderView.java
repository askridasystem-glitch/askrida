/***********************************************************************
 * Module:  com.webfin.gl.model.PeriodHeaderView
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:35:04 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;

public class PeriodHeaderView extends DTO implements RecordAudit {
   /*
   CREATE TABLE gl_period
(
  gl_period_id int8 NOT NULL,
  fiscal_year varchar(5),
  period_num int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_period_id_pk PRIMARY KEY (gl_period_id)
)
   */

   private Long lgPeriodID;
   private String stFiscalYear;
   private Long lgPeriodNum;
   private DTOList details;

   public static String tableName = "gl_period";

   public static String fieldMap[][] = {
      {"lgPeriodID","gl_period_id*pk"},
      {"stFiscalYear","fiscal_year"},
      {"lgPeriodNum","period_num"},
   };

   public Long getLgPeriodID() {
      return lgPeriodID;
   }

   public void setLgPeriodID(Long lgPeriodID) {
      this.lgPeriodID = lgPeriodID;
   }

   public String getStFiscalYear() {
      return stFiscalYear;
   }

   public void setStFiscalYear(String stFiscalYear) {
      this.stFiscalYear = stFiscalYear;
   }

   public Long getLgPeriodNum() {
      return lgPeriodNum;
   }

   public void setLgPeriodNum(Long lgPeriodNum) {
      this.lgPeriodNum = lgPeriodNum;
   }

   public DTOList getDetails() {
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }
}
