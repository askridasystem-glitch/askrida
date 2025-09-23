/***********************************************************************
 * Module:  com.webfin.gl.model.GLReportColumnView
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 12:59:46 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.util.EzFormat;
import com.webfin.FinCodec;

import java.text.DecimalFormat;

public class GLReportColumnView extends DTO implements RecordAudit{
   /*
CREATE TABLE gl_rpt_col
(
  gl_rpt_id varchar(32) NOT NULL,
  col_no int4,
  col_type varchar(32),
  gl_rpt_col_id int8 NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  gl_year int4,
  gl_period int4,
  gl_value varchar(5),
  col_header varchar(255),
  CONSTRAINT gl_rpt_col_pk PRIMARY KEY (gl_rpt_col_id)
)
   */

   private String stGLReportID;
   private Long lgColumnNumber;
   private String stColumnType;
   private String stGLReportColumnID;
   private Long lgYear;
   private Long lgPeriod;
   private Long lgPeriodTo;
   private String stValue;
   private String stColumnHeader;
   private Long lgColumnPosition;
   private String stColumnFormat;

   public static String tableName = "gl_rpt_col";

   public static String fieldMap[][] = {
      {"stGLReportID","gl_rpt_id"},
      {"lgColumnNumber","col_no"},
      {"stColumnType","col_type"},
      {"stGLReportColumnID","gl_rpt_col_id*pk"},
      {"lgYear","gl_year"},
      {"lgPeriod","gl_period"},
      {"lgPeriodTo","gl_period_to"},
      {"stValue","gl_value"},
      {"stColumnHeader","col_header"},
      {"lgColumnPosition","col_pos"},
      {"stColumnFormat","col_fmt"},
   };
   private DecimalFormat decimalFormat;
   private EzFormat ezFormat;

   public Long getLgPeriod() {
      return lgPeriod;
   }

   public void setLgPeriod(Long lgPeriod) {
      this.lgPeriod = lgPeriod;
   }

   public Long getLgPeriodTo() {
      return lgPeriodTo;
   }

   public void setLgPeriodTo(Long lgPeriodTo) {
      this.lgPeriodTo = lgPeriodTo;
   }

   public String getStGLReportID() {
      return stGLReportID;
   }

   public void setStGLReportID(String stGLReportID) {
      this.stGLReportID = stGLReportID;
   }

   public Long getLgColumnNumber() {
      return lgColumnNumber;
   }

   public void setLgColumnNumber(Long lgColumnNumber) {
      this.lgColumnNumber = lgColumnNumber;
   }

   public String getStColumnType() {
      return stColumnType;
   }

   public void setStColumnType(String stColumnType) {
      this.stColumnType = stColumnType;
   }

   public String getStGLReportColumnID() {
      return stGLReportColumnID;
   }

   public void setStGLReportColumnID(String stGLReportColumnID) {
      this.stGLReportColumnID = stGLReportColumnID;
   }

   public Long getLgYear() {
      return lgYear;
   }

   public void setLgYear(Long lgYear) {
      this.lgYear = lgYear;
   }

   public String getStValue() {
      return stValue;
   }

   public void setStValue(String stValue) {
      this.stValue = stValue;
   }

   public void setStColumnHeader(String stColumnHeader) {
      this.stColumnHeader = stColumnHeader;
   }

   public String getStColumnHeader() {
      return stColumnHeader;
   }

   public boolean isBalance() {
      return
              Tools.isEqual(FinCodec.GLReportColType.BALANCE, stValue) ||
              Tools.isEqual(FinCodec.GLReportColType.BALANCE_CREDIT, stValue) ||
              Tools.isEqual(FinCodec.GLReportColType.BALANCE_DEBIT, stValue);

   }

   public boolean isSummary() {
      return
              Tools.isEqual(FinCodec.GLReportColType.SUMMARY, stValue) ||
              Tools.isEqual(FinCodec.GLReportColType.SUMMARY_CREDIT, stValue) ||
              Tools.isEqual(FinCodec.GLReportColType.SUMMARY_DEBIT, stValue);

   }

   public Long getLgColumnPosition() {
      return lgColumnPosition;
   }

   public void setLgColumnPosition(Long lgColumnPosition) {
      this.lgColumnPosition = lgColumnPosition;
   }

   public String getStColumnFormat() {
      return stColumnFormat;
   }

   public void setStColumnFormat(String stColumnFormat) {
      this.stColumnFormat = stColumnFormat;
   }

   public DecimalFormat getDecimalFormat() {
      if (decimalFormat==null)
         decimalFormat = new DecimalFormat(stColumnFormat);
      return decimalFormat;
   }

   public Object format(Object val) {
      return getEzFormat().format(val);
   }

   private EzFormat getEzFormat() {
      if (ezFormat==null) ezFormat = new EzFormat(stColumnFormat);

      return ezFormat;
   }

   public boolean isValCR() {
      return
              FinCodec.GLReportColType.BALANCE_CREDIT.equalsIgnoreCase(stValue) ||
              FinCodec.GLReportColType.SUMMARY_CREDIT.equalsIgnoreCase(stValue);
   }

   public boolean isValDB() {
      return
              FinCodec.GLReportColType.BALANCE_DEBIT.equalsIgnoreCase(stValue) ||
              FinCodec.GLReportColType.SUMMARY_DEBIT.equalsIgnoreCase(stValue);
   }

   public boolean isValBalance() {
      return
              FinCodec.GLReportColType.BALANCE.equalsIgnoreCase(stValue) ||
              FinCodec.GLReportColType.SUMMARY.equalsIgnoreCase(stValue);
   }
}
