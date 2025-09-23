/***********************************************************************
 * Module:  com.webfin.system.region.model.RegionView
 * Author:  Denny Mahendra
 * Created: Aug 3, 2006 12:07:18 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.system.region.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class RegionView extends DTO implements RecordAudit {
   /*
   CREATE TABLE s_region
(
  region_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  cc_code varchar(8),
  region_code varchar(8),
  CONSTRAINT s_region_pk PRIMARY KEY (region_id)
)
   */

   public static String tableName = "s_region";

   public static String fieldMap[][] = {
      {"stRegionID","region_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"stCostCenterCode","cc_code"},
      {"stRegionCode","region_code"},
      {"stCostCenterCode3","cc_code3"},
      {"stRegionCode3","region_code3"},
      {"stLevel","level"},
   };

   private String stRegionID;
   private String stDescription;
   private String stActiveFlag;
   private String stCostCenterCode;
   private String stRegionCode;
   private String stCostCenterCode3;
   private String stRegionCode3;
   private String stLevel;

   public String getStRegionID() {
      return stRegionID;
   }

   public void setStRegionID(String stRegionID) {
      this.stRegionID = stRegionID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStCostCenterCode() {
      return stCostCenterCode;
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
   }

   public String getStRegionCode() {
      return stRegionCode;
   }

   public void setStRegionCode(String stRegionCode) {
      this.stRegionCode = stRegionCode;
   }



    /**
     * @return the stCostCenterCode3
     */
    public String getStCostCenterCode3() {
        return stCostCenterCode3;
    }

    /**
     * @param stCostCenterCode3 the stCostCenterCode3 to set
     */
    public void setStCostCenterCode3(String stCostCenterCode3) {
        this.stCostCenterCode3 = stCostCenterCode3;
    }

    /**
     * @return the stRegionCode3
     */
    public String getStRegionCode3() {
        return stRegionCode3;
    }

    /**
     * @param stRegionCode3 the stRegionCode3 to set
     */
    public void setStRegionCode3(String stRegionCode3) {
        this.stRegionCode3 = stRegionCode3;
    }

    /**
     * @return the stLevel
     */
    public String getStLevel() {
        return stLevel;
    }

    /**
     * @param stLevel the stLevel to set
     */
    public void setStLevel(String stLevel) {
        this.stLevel = stLevel;
    }
    
}
