/***********************************************************************
 * Module:  com.webfin.gl.model.GLReportView
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 12:58:07 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.RecordAudit;
import com.crux.common.model.DTO;
import com.crux.util.DTOList;

import java.util.ArrayList;

public class GLReportView extends DTO implements RecordAudit{
    /*
    CREATE TABLE gl_rpt
(
  report_id varchar(32) NOT NULL,
  title varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_rpt_pk PRIMARY KEY (report_id)
)
    */

   private String stReportID;
   private String stReportTitle;
   private String stResourceID;
   private DTOList columns;
   private DTOList lines;

   public static String tableName = "gl_rpt";

   public static String fieldMap[][] = {
      {"stReportID","report_id*pk"},
      {"stReportTitle","title"},
      {"stResourceID","resource_id"},
   };
   private ArrayList result;

   public String getStReportID() {
      return stReportID;
   }

   public void setStReportID(String stReportID) {
      this.stReportID = stReportID;
   }

   public String getStReportTitle() {
      return stReportTitle;
   }

   public void setStReportTitle(String stReportTitle) {
      this.stReportTitle = stReportTitle;
   }

   public DTOList getLines() {
      return lines;
   }

   public void setLines(DTOList lines) {
      this.lines = lines;
   }

   public DTOList getColumns() {
      return columns;
   }

   public void setColumns(DTOList columns) {
      this.columns = columns;
   }

   public void setResult(ArrayList outputLines) {
      result = outputLines;
   }

   public ArrayList getResult() {
      return result;
   }

   public String getStResourceID() {
      return stResourceID;
   }

   public void setStResourceID(String stResourceID) {
      this.stResourceID = stResourceID;
   }

}
