/***********************************************************************
 * Module:  com.webfin.gl.model.GLReportLineView
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 1:01:30 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.util.ArrayList;
import java.math.BigDecimal;

public class GLReportLineView extends DTO implements RecordAudit{
   /*
   CREATE TABLE gl_rpt_lin
(
  gl_rpt_lin_id int8 NOT NULL,
  gl_rpt_id varchar(32) NOT NULL,
  line_no int8,
  line_type varchar(8),
  acct_from varchar(32),
  acct_to varchar(32),
  summarize_flag varchar(1),
  blank_line int4,
  description varchar(255),
  indent int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_rpt_lin_pk PRIMARY KEY (gl_rpt_lin_id)
)
   */

   private String stGLReportLineID;
   private String stGLReportID;
   private Long lgLineNo;
   private String stLineType;
   private String stAccountFrom;
   private String stAccountTo;
   private String stSummarizeFlag;
   private Long lgBlankLine;
   private String stDescription;
   private Long lgIndent;
   private Long lgColumnNo;
   private String stNegateFlag;
   private String stPrintFlag;
   private String stPrintCRFlag;
   private Object [] columns;
   private String stVariable;
   private String stFormula;
   private String stFormat;

   public static String tableName = "gl_rpt_lin";

   public static String fieldMap[][] = {
      {"stGLReportLineID","gl_rpt_lin_id*pk"},
      {"stGLReportID","gl_rpt_id"},
      {"lgLineNo","line_no"},
      {"stLineType","line_type"},
      {"stAccountFrom","acct_from"},
      {"stAccountTo","acct_to"},
      {"stSummarizeFlag","summarize_flag"},
      {"lgBlankLine","blank_line"},
      {"stDescription","description"},
      {"lgIndent","indent"},
      {"stNegateFlag","negate_flag"},
      {"lgColumnNo","col_no"},
      {"stPrintFlag","print_flag"},
      {"stPrintCRFlag","prt_cr"},
      {"stVariable","vrbl"},
      {"stFormula","formula"},
      {"stFormat","fmt"},
   };

    public Long getLgColumnNo() {
        return lgColumnNo;
    }

    public void setLgColumnNo(Long lgColumnNo) {
        this.lgColumnNo = lgColumnNo;
    }

    public String getStPrintFlag() {
        return stPrintFlag;
    }

    public void setStPrintFlag(String stPrintFlag) {
        this.stPrintFlag = stPrintFlag;
    }

   public String getStGLReportLineID() {
      return stGLReportLineID;
   }

   public void setStGLReportLineID(String stGLReportLineID) {
      this.stGLReportLineID = stGLReportLineID;
   }

   public String getStGLReportID() {
      return stGLReportID;
   }

   public void setStGLReportID(String stGLReportID) {
      this.stGLReportID = stGLReportID;
   }

   public Long getLgLineNo() {
      return lgLineNo;
   }

   public void setLgLineNo(Long lgLineNo) {
      this.lgLineNo = lgLineNo;
   }

   public String getStLineType() {
      return stLineType;
   }

   public void setStLineType(String stLineType) {
      this.stLineType = stLineType;
   }

   public String getStAccountFrom() {
      return stAccountFrom;
   }

   public void setStAccountFrom(String stAccountFrom) {
      this.stAccountFrom = stAccountFrom;
   }

   public String getStAccountTo() {
      return stAccountTo;
   }

   public void setStAccountTo(String stAccountTo) {
      this.stAccountTo = stAccountTo;
   }

   public String getStSummarizeFlag() {
      return stSummarizeFlag;
   }

   public void setStSummarizeFlag(String stSummarizeFlag) {
      this.stSummarizeFlag = stSummarizeFlag;
   }

   public Long getLgBlankLine() {
      return lgBlankLine;
   }

   public void setLgBlankLine(Long lgBlankLine) {
      this.lgBlankLine = lgBlankLine;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Long getLgIndent() {
      return lgIndent;
   }

   public void setLgIndent(Long lgIndent) {
      this.lgIndent = lgIndent;
   }

   public Object[] getColumns() {
      return columns;
   }

   public void setColumns(Object[] columns) {
      this.columns = columns;
   }

   public void setStNegateFlag(String stNegateFlag) {
      this.stNegateFlag = stNegateFlag;
   }

   public String getStNegateFlag() {
      return stNegateFlag;
   }

   public void setStPrintCRFlag(String stPrintCRFlag) {
      this.stPrintCRFlag = stPrintCRFlag;
   }

   public String getStPrintCRFlag() {
      return stPrintCRFlag;
   }

   public boolean isSummarized() {
      return Tools.isYes(stSummarizeFlag);
   }

   public String getStVariable() {
      return stVariable;
   }

   public void setStVariable(String stVariable) {
      this.stVariable = stVariable;
   }

   public String getStFormula() {
      return stFormula;
   }

   public void setStFormula(String stFormula) {
      this.stFormula = stFormula;
   }

   public String getStFormat() {
      return stFormat;
   }

   public void setStFormat(String stFormat) {
      this.stFormat = stFormat;
   }

   public boolean isCR() {
      return Tools.isYes(stPrintCRFlag);
   }
}
