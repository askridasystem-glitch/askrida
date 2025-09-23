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
import com.crux.util.ListUtil;
import com.webfin.entity.model.EntityAddressView;

public class ClosingHeaderView extends DTO implements RecordAudit {
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

   private DTOList details;

   private String stClosingPeriodID;
   private String stFiscalYear;
   private String stPeriodNum;
   private String stCostCenterCode;
   private String stRegionID;
   private String stCostCenter;
   private String stTimeZone;
   private String stActiveFlag;

   public static String tableName = "closing_period";

   public static String fieldMap[][] = {
      {"stClosingPeriodID","closing_period_id*pk"},
      {"stFiscalYear","fiscal_year"},
      {"stPeriodNum","period_num"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"stTimeZone","timezone"},
      {"stCostCenter","cost_center*n"},
      //{"stActiveFlag","active_flag"},
   };

   public DTOList getDetails() {
      loadDetail();
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }


   private void loadDetail() {
      try {
         if (details == null)
            details = ListUtil.getDTOListFromQuery(
                    "select * from closing_period_det where closing_period_id = ? order by closing_period_detail_id",
                    new Object [] {stClosingPeriodID},
                    ClosingDetailView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

    /**
     * @return the stPeriodID
     */

    /**
     * @return the stFiscalYear
     */
    public String getStFiscalYear() {
        return stFiscalYear;
    }

    /**
     * @param stFiscalYear the stFiscalYear to set
     */
    public void setStFiscalYear(String stFiscalYear) {
        this.stFiscalYear = stFiscalYear;
    }

    /**
     * @return the stPeriodNum
     */
    public String getStPeriodNum() {
        return stPeriodNum;
    }

    /**
     * @param stPeriodNum the stPeriodNum to set
     */
    public void setStPeriodNum(String stPeriodNum) {
        this.stPeriodNum = stPeriodNum;
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
     * @return the stCostCenter
     */
    public String getStCostCenter() {
        return stCostCenter;
    }

    /**
     * @param stCostCenter the stCostCenter to set
     */
    public void setStCostCenter(String stCostCenter) {
        this.stCostCenter = stCostCenter;
    }

    /**
     * @return the stTimeZone
     */
    public String getStTimeZone() {
        return stTimeZone;
    }

    /**
     * @param stTimeZone the stTimeZone to set
     */
    public void setStTimeZone(String stTimeZone) {
        this.stTimeZone = stTimeZone;
    }

    public boolean isWIB(){
        return "WIB".equalsIgnoreCase(stTimeZone);
    }

    public boolean isWITA(){
        return "WITA".equalsIgnoreCase(stTimeZone);
    }

    public boolean isWIT(){
        return "WIT".equalsIgnoreCase(stTimeZone);
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }
}
