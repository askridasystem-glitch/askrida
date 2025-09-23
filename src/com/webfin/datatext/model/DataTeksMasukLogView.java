/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIView
 * Author:  Denny Mahendra
 * Created: Jan 27, 2006 5:33:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.datatext.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.util.Date;

public class DataTeksMasukLogView extends DTO implements RecordAudit {
/*
CREATE TABLE data_teks_log
(
  teks_log_id bigint,
  file_name character varying(255),
  cc_code character varying(4),
  region_id bigint,
  record_amount bigint,
  record_success bigint,
  record_failed bigint,
  process_date timestamp without time zone,
  create_who character varying(32),
  create_date timestamp without time zone,
  change_who character varying(32),
  change_date timestamp without time zone
)
WITH (
  OIDS=FALSE
);
ALTER TABLE data_teks_log
  OWNER TO postgres;
*/

   public static String tableName = "data_teks_log";

   public static String fieldMap[][] = {
      {"stTeksLogID","teks_log_id*pk"},
      {"stFileName","file_name"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"stRecordAmount","record_amount"},
      {"stRecordSuccess","record_success"},
      {"stRecordFailed","record_failed"},
      {"dtProcessDate","process_date"},
      {"stDescription","description"},
   };

   private String stTeksLogID;
   private String stFileName;
   private String stCostCenterCode;
   private String stRegionID;
   private String stRecordAmount;
   private String stRecordSuccess;
   private String stRecordFailed;
   private Date dtProcessDate;
   private String stDescription;

    /**
     * @return the stTeksLogID
     */
    public String getStTeksLogID() {
        return stTeksLogID;
    }

    /**
     * @param stTeksLogID the stTeksLogID to set
     */
    public void setStTeksLogID(String stTeksLogID) {
        this.stTeksLogID = stTeksLogID;
    }

    /**
     * @return the stFileName
     */
    public String getStFileName() {
        return stFileName;
    }

    /**
     * @param stFileName the stFileName to set
     */
    public void setStFileName(String stFileName) {
        this.stFileName = stFileName;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stRegionID
     */
    public String getStRegionID() {
        return stRegionID;
    }

    /**
     * @param stRegionID the stRegionID to set
     */
    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the stRecordAmount
     */
    public String getStRecordAmount() {
        return stRecordAmount;
    }

    /**
     * @param stRecordAmount the stRecordAmount to set
     */
    public void setStRecordAmount(String stRecordAmount) {
        this.stRecordAmount = stRecordAmount;
    }

    /**
     * @return the stRecordSuccess
     */
    public String getStRecordSuccess() {
        return stRecordSuccess;
    }

    /**
     * @param stRecordSuccess the stRecordSuccess to set
     */
    public void setStRecordSuccess(String stRecordSuccess) {
        this.stRecordSuccess = stRecordSuccess;
    }

    /**
     * @return the stRecordFailed
     */
    public String getStRecordFailed() {
        return stRecordFailed;
    }

    /**
     * @param stRecordFailed the stRecordFailed to set
     */
    public void setStRecordFailed(String stRecordFailed) {
        this.stRecordFailed = stRecordFailed;
    }

    /**
     * @return the dtProcessDate
     */
    public Date getDtProcessDate() {
        return dtProcessDate;
    }

    /**
     * @param dtProcessDate the dtProcessDate to set
     */
    public void setDtProcessDate(Date dtProcessDate) {
        this.dtProcessDate = dtProcessDate;
    }

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

}
