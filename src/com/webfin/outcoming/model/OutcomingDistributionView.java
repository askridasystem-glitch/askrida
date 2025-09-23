/***********************************************************************
 * Module:  com.webfin.entity.model.EntityAddressView
 * Author:  Denny Mahendra
 * Created: Nov 8, 2005 2:03:05 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.outcoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;

public class OutcomingDistributionView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ent_address
(
  ent_addr_id int8 NOT NULL,
  ent_id int8,
  addr_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  primary_flag varchar(1),
  mailing_flag varchar(1),
  city_id int8,
  postal_code varchar(10),
  province_id int8,
  sub_region_id int8,
  address varchar(255),
  CONSTRAINT ent_address_pk PRIMARY KEY (ent_addr_id)
)
   
   
   CREATE TABLE outcoming_dist
(
  ref_no character varying(32) NOT NULL,
  id_dist character varying(32) NOT NULL,
  create_who character varying(32) NOT NULL,
  create_date timestamp without time zone NOT NULL,
  change_who character varying(32),
  change_date timestamp without time zone,
  receiver character varying(50),
  CONSTRAINT pk_outcoming_dist PRIMARY KEY (ref_no, id_dist)
)


   public static String tableName = "outcoming_dist";

   public static String fieldMap[][] = {
      {"stEntityAddressID","ent_addr_id*pk"},
      {"stEntityID","ent_id"},
      {"stAddressType","addr_type"},
      {"stPrimaryFlag","primary_flag"},
      {"stMailingFlag","mailing_flag"},
      {"stCityID","city_id"},
      {"stPostalCode","postal_code"},
      {"stProvinceID","province_id"},
      {"stSubRegionID","sub_region_id"},
      {"stAddress","address"},
      {"stRegionalID1","regional_id1"},
      {"stRegionalID2","regional_id2"},
      {"stRegionalID3","regional_id3"},
      {"stRegionalID4","regional_id4"},
      {"stCountryID","country_id"},
      {"stOwnershipCode","ownership_code"},
      {"dtOccupiedDate","occupied_date"},
      {"stPhone","phone"},
      {"stPhoneMobile","phone_mobile"},
      {"stPhoneFax","phone_fax"},
      {"stEmail","email"},
      {"stWebsite","website"},
      {"stPredefRiskCode","predef_code"},
      {"stPredefRiskName","predef_name"},
      {"stRegionMapID","region_map_id"},
   };
*/
/*
      CREATE TABLE outcoming_dist
(
  ref_no character varying(32) NOT NULL,
  id_dist character varying(32) NOT NULL,
  create_who character varying(32) NOT NULL,
  create_date timestamp without time zone NOT NULL,
  change_who character varying(32),
  change_date timestamp without time zone,
  receiver character varying(50),
  CONSTRAINT pk_outcoming_dist PRIMARY KEY (ref_no, id_dist)
)
*/

   public static String tableName = "outcoming_dist";

   public static String fieldMap[][] = {
      {"stOutID","out_id"},
      {"stIdDist","id_dist*pk"},
      {"stReceiver","receiver"},
      
   };

   public static String comboFields[] = {"index","receiver"};

    private String stOutID;
   private String stIdDist;
   private String stReceiver;
   
   
   public String getStOutID() {
      return stOutID;
   }

   public void setStOutID(String stOutID) {
      this.stOutID = stOutID;
   }
   
   public String getStIdDist() {
      return stIdDist;
   }

   public void setStIdDist(String stIdDist) {
      this.stIdDist = stIdDist;
   }
   
   public String getStReceiver() {
      return stReceiver;
   }

   public void setStReceiver(String stReceiver) {
      this.stReceiver = stReceiver;
   }
   
    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

//   private String stEntityAddressID;
//   private String stEntityID;
//   private String stRegionMapID;
//   private String stAddressType;
//   private String stPrimaryFlag;
//   private String stMailingFlag;
//   private String stCityID;
//   private String stPostalCode;
//   private String stProvinceID;
//   private String stSubRegionID;
//   private String stAddress;
//
//   private String stRegionalID1;
//   private String stRegionalID2;
//   private String stRegionalID3;
//   private String stRegionalID4;
//   private String stCountryID;
//   private String stOwnershipCode;
//   private String dtOccupiedDate;
//   private String stPhone;
//   private String stPhoneMobile;
//   private String stPhoneFax;
//   private String stEmail;
//   private String stWebsite;
//   private String stPredefRiskCode;
//   private String stPredefRiskName;

//   public String getStRegionMapID() {
//      return stRegionMapID;
//   }
//
//   public void setStRegionMapID(String stRegionMapID) {
//      this.stRegionMapID = stRegionMapID;
//   }
//
//   public String getStEntityAddressID() {
//      return stEntityAddressID;
//   }
//
//   public void setStEntityAddressID(String stEntityAddressID) {
//      this.stEntityAddressID = stEntityAddressID;
//   }
//
//   public String getStEntityID() {
//      return stEntityID;
//   }
//
//   public void setStEntityID(String stEntityID) {
//      this.stEntityID = stEntityID;
//   }
//
//   public String getStAddressType() {
//      return stAddressType;
//   }
//
//   public void setStAddressType(String stAddressType) {
//      this.stAddressType = stAddressType;
//   }
//
//   public String getStPrimaryFlag() {
//      return stPrimaryFlag;
//   }
//
//   public void setStPrimaryFlag(String stPrimaryFlag) {
//      this.stPrimaryFlag = stPrimaryFlag;
//   }
//
//   public String getStMailingFlag() {
//      return stMailingFlag;
//   }
//
//   public void setStMailingFlag(String stMailingFlag) {
//      this.stMailingFlag = stMailingFlag;
//   }
//
//   public String getStCityID() {
//      return stCityID;
//   }
//
//   public void setStCityID(String stCityID) {
//      this.stCityID = stCityID;
//   }
//
//   public String getStPostalCode() {
//      return stPostalCode;
//   }
//
//   public void setStPostalCode(String stPostalCode) {
//      this.stPostalCode = stPostalCode;
//   }
//
//   public String getStProvinceID() {
//      return stProvinceID;
//   }
//
//   public void setStProvinceID(String stProvinceID) {
//      this.stProvinceID = stProvinceID;
//   }
//
//   public String getStSubRegionID() {
//      return stSubRegionID;
//   }
//
//   public void setStSubRegionID(String stSubRegionID) {
//      this.stSubRegionID = stSubRegionID;
//   }
//
//   public String getStAddress() {
//      return stAddress;
//   }
//
//   public void setStAddress(String stAddress) {
//      this.stAddress = stAddress;
//   }
//
//   public String getStRegionalID1() {
//      return stRegionalID1;
//   }
//
//   public void setStRegionalID1(String stRegionalID1) {
//      this.stRegionalID1 = stRegionalID1;
//   }
//
//   public String getStRegionalID2() {
//      return stRegionalID2;
//   }
//
//   public void setStRegionalID2(String stRegionalID2) {
//      this.stRegionalID2 = stRegionalID2;
//   }
//
//   public String getStRegionalID3() {
//      return stRegionalID3;
//   }
//
//   public void setStRegionalID3(String stRegionalID3) {
//      this.stRegionalID3 = stRegionalID3;
//   }
//
//   public String getStRegionalID4() {
//      return stRegionalID4;
//   }
//
//   public void setStRegionalID4(String stRegionalID4) {
//      this.stRegionalID4 = stRegionalID4;
//   }
//
//   public String getStCountryID() {
//      return stCountryID;
//   }
//
//   public void setStCountryID(String stCountryID) {
//      this.stCountryID = stCountryID;
//   }
//
//   public String getStOwnershipCode() {
//      return stOwnershipCode;
//   }
//
//   public void setStOwnershipCode(String stOwnershipCode) {
//      this.stOwnershipCode = stOwnershipCode;
//   }
//
//   public String getDtOccupiedDate() {
//      return dtOccupiedDate;
//   }
//
//   public void setDtOccupiedDate(String dtOccupiedDate) {
//      this.dtOccupiedDate = dtOccupiedDate;
//   }
//
//   public String getStPhone() {
//      return stPhone;
//   }
//
//   public void setStPhone(String stPhone) {
//      this.stPhone = stPhone;
//   }
//
//   public String getStPhoneMobile() {
//      return stPhoneMobile;
//   }
//
//   public void setStPhoneMobile(String stPhoneMobile) {
//      this.stPhoneMobile = stPhoneMobile;
//   }
//
//   public String getStPhoneFax() {
//      return stPhoneFax;
//   }
//
//   public void setStPhoneFax(String stPhoneFax) {
//      this.stPhoneFax = stPhoneFax;
//   }
//
//   public String getStEmail() {
//      return stEmail;
//   }
//
//   public void setStEmail(String stEmail) {
//      this.stEmail = stEmail;
//   }
//
//   public String getStWebsite() {
//      return stWebsite;
//   }
//
//   public void setStWebsite(String stWebsite) {
//      this.stWebsite = stWebsite;
//   }
//
//   public String getStPredefRiskCode() {
//      return stPredefRiskCode;
//   }
//
//   public void setStPredefRiskCode(String stPredefRiskCode) {
//      this.stPredefRiskCode = stPredefRiskCode;
//   }
//
//   public String getStPredefRiskName() {
//      return stPredefRiskName;
//   }
//
//   public void setStPredefRiskName(String stPredefRiskName) {
//      this.stPredefRiskName = stPredefRiskName;
//   }
//
//   public boolean isPrimary() {
//      return Tools.isYes(stPrimaryFlag);
//   }
}
