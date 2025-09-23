/***********************************************************************
 * Module:  com.webfin.postalcode.model.PostalCodeMaiparkView
 * Author:  Denny Mahendra
 * Created: Jun 30, 2006 1:14:46 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.postalcode.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.webfin.FinCodec;

public class PostalCodeMaiparkView extends DTO implements RecordAudit {
   /*
CREATE TABLE s_region_map2
(
  region_map_id int8 NOT NULL,
  city_name varchar(64),
  region_class varchar(5),
  region_name varchar(128),
  postal_code varchar(10),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_region_map2_pk PRIMARY KEY (region_map_id)
)
WITHOUT OIDS;
ALTER TABLE s_region_map2 OWNER TO postgres;

   */

   public static String tableName = "s_region_map_maipark";

   public static String comboFields[] = {"region_map_id", "postal_code", "region_name", "city_name", "region_map_desc", "building_desc"};

   public static String fieldMap[][] = {
      {"stRegionMapID","region_map_id*pk"},
      {"stCityName","city_name"},
      {"stRegionClass","region_class"},
      {"stRegionName","region_name"},
      {"stPostalCode","postal_code"},
      {"stRegionMapDesc","region_map_desc"},
      {"stReference1","ref1"},
      {"stBuildingDescription","building_desc"},
   };

   private String stRegionMapID;
   private String stCityName;
   private String stRegionClass;
   private String stRegionName;
   private String stPostalCode;
   private String stRegionMapDesc;
   private String stReference1;
   private String stBuildingDescription;

   public String getStRegionClassDesc() {
      return (String) FinCodec.RegionClass.getLookUp().getValue(stRegionClass);
   }

   public String getStRegionMapDesc() {
      return stRegionMapDesc;
   }

   public void setStRegionMapDesc(String stRegionMapDesc) {
      this.stRegionMapDesc = stRegionMapDesc;
   }

   public void setStRegionClassDesc(String stRegionClassDesc) {
   }

   public String getStRegionMapID() {
      return stRegionMapID;
   }

   public void setStRegionMapID(String stRegionMapID) {
      this.stRegionMapID = stRegionMapID;
   }

   public String getStCityName() {
      return stCityName;
   }

   public void setStCityName(String stCityName) {
      this.stCityName = stCityName;
   }

   public String getStRegionClass() {
      return stRegionClass;
   }

   public void setStRegionClass(String stRegionClass) {
      this.stRegionClass = stRegionClass;
   }

   public String getStRegionName() {
      return stRegionName;
   }

   public void setStRegionName(String stRegionName) {
      this.stRegionName = stRegionName;
   }

   public String getStPostalCode() {
      return stPostalCode;
   }

   public void setStPostalCode(String stPostalCode) {
      this.stPostalCode = stPostalCode;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

    public String getStBuildingDescription() {
        return stBuildingDescription;
    }

    public void setStBuildingDescription(String stBuildingDescription) {
        this.stBuildingDescription = stBuildingDescription;
    }

}