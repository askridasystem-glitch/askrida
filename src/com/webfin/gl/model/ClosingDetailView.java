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

public class ClosingDetailView extends DTO implements RecordAudit {

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

   private String stClosingPeriodDetailID;
   private String stClosingPeriodID;
   private String stPeriodNo;
   private String stClosedFlag;
   private Date dtEditStartDate;
   private Date dtEditEndDate;
   private Date dtReverseStartDate;
   private Date dtReverseEndDate;
   private Date dtClosedDate;
   private Date dtStartDate;
   private Date dtEndDate;
   private String stEditEndTime;
   private String stReverseEndTime;
   private String stCloseTime;

   public static String tableName = "closing_period_det";

   public static String fieldMap[][] = {
      {"stClosingPeriodDetailID","closing_period_detail_id*pk"},
      {"stClosingPeriodID","closing_period_id"},
      {"stPeriodNo","period_no"},
      {"dtEditStartDate","edit_startdate"},
      {"dtEditEndDate","edit_enddate"},
      {"dtReverseStartDate","reverse_startdate"},
      {"dtReverseEndDate","reverse_enddate"},
      {"stClosedFlag","closed_flag"},
      {"dtClosedDate","closed_date"},
      {"dtStartDate","startdate"},
      {"dtEndDate","enddate"},
      {"stEditEndTime","edit_endtime"},
      {"stReverseEndTime","reverse_endtime"},
      {"stCloseTime","close_time"},

   };

   

   public boolean isOpen() {
      return Tools.isNo(getStClosedFlag());
   }

    /**
     * @return the stClosingPeriodDetailID
     */
    public String getStClosingPeriodDetailID() {
        return stClosingPeriodDetailID;
    }

    /**
     * @param stClosingPeriodDetailID the stClosingPeriodDetailID to set
     */
    public void setStClosingPeriodDetailID(String stClosingPeriodDetailID) {
        this.stClosingPeriodDetailID = stClosingPeriodDetailID;
    }

    /**
     * @return the stClosingPeriodID
     */
    public String getStClosingPeriodID() {
        return stClosingPeriodID;
    }

    /**
     * @param stClosingPeriodID the stClosingPeriodID to set
     */
    public void setStClosingPeriodID(String stClosingPeriodID) {
        this.stClosingPeriodID = stClosingPeriodID;
    }

    /**
     * @return the stPeriodNo
     */
    public String getStPeriodNo() {
        return stPeriodNo;
    }

    /**
     * @param stPeriodNo the stPeriodNo to set
     */
    public void setStPeriodNo(String stPeriodNo) {
        this.stPeriodNo = stPeriodNo;
    }

    /**
     * @return the stClosedFlag
     */
    public String getStClosedFlag() {
        return stClosedFlag;
    }

    /**
     * @param stClosedFlag the stClosedFlag to set
     */
    public void setStClosedFlag(String stClosedFlag) {
        this.stClosedFlag = stClosedFlag;
    }

    /**
     * @return the dtEditStartDate
     */
    public Date getDtEditStartDate() {
        return dtEditStartDate;
    }

    /**
     * @param dtEditStartDate the dtEditStartDate to set
     */
    public void setDtEditStartDate(Date dtEditStartDate) {
        this.dtEditStartDate = dtEditStartDate;
    }

    /**
     * @return the dtEditEndDate
     */
    public Date getDtEditEndDate() {
        return dtEditEndDate;
    }

    /**
     * @param dtEditEndDate the dtEditEndDate to set
     */
    public void setDtEditEndDate(Date dtEditEndDate) {
        this.dtEditEndDate = dtEditEndDate;
    }

    /**
     * @return the dtReverseStartDate
     */
    public Date getDtReverseStartDate() {
        return dtReverseStartDate;
    }

    /**
     * @param dtReverseStartDate the dtReverseStartDate to set
     */
    public void setDtReverseStartDate(Date dtReverseStartDate) {
        this.dtReverseStartDate = dtReverseStartDate;
    }

    /**
     * @return the dtReverseEndDate
     */
    public Date getDtReverseEndDate() {
        return dtReverseEndDate;
    }

    /**
     * @param dtReverseEndDate the dtReverseEndDate to set
     */
    public void setDtReverseEndDate(Date dtReverseEndDate) {
        this.dtReverseEndDate = dtReverseEndDate;
    }

    /**
     * @return the dtClosedDate
     */
    public Date getDtClosedDate() {
        return dtClosedDate;
    }

    /**
     * @param dtClosedDate the dtClosedDate to set
     */
    public void setDtClosedDate(Date dtClosedDate) {
        this.dtClosedDate = dtClosedDate;
    }

    /**
     * @return the dtStartDate
     */
    public Date getDtStartDate() {
        return dtStartDate;
    }

    /**
     * @param dtStartDate the dtStartDate to set
     */
    public void setDtStartDate(Date dtStartDate) {
        this.dtStartDate = dtStartDate;
    }

    /**
     * @return the dtEndDate
     */
    public Date getDtEndDate() {
        return dtEndDate;
    }

    /**
     * @param dtEndDate the dtEndDate to set
     */
    public void setDtEndDate(Date dtEndDate) {
        this.dtEndDate = dtEndDate;
    }

    /**
     * @return the stEditEndTime
     */
    public String getStEditEndTime() {
        return stEditEndTime;
    }

    /**
     * @param stEditEndTime the stEditEndTime to set
     */
    public void setStEditEndTime(String stEditEndTime) {
        this.stEditEndTime = stEditEndTime;
    }

    /**
     * @return the stReverseEndTime
     */
    public String getStReverseEndTime() {
        return stReverseEndTime;
    }

    /**
     * @param stReverseEndTime the stReverseEndTime to set
     */
    public void setStReverseEndTime(String stReverseEndTime) {
        this.stReverseEndTime = stReverseEndTime;
    }

    /**
     * @return the stCloseTime
     */
    public String getStCloseTime() {
        return stCloseTime;
    }

    /**
     * @param stCloseTime the stCloseTime to set
     */
    public void setStCloseTime(String stCloseTime) {
        this.stCloseTime = stCloseTime;
    }
}
