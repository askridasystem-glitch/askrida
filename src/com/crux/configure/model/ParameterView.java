/***********************************************************************
 * Module:  com.crux.configure.model.ParameterView
 * Author:  Denny Mahendra
 * Created: Jun 15, 2004 11:55:13 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.configure.model;

import com.crux.common.model.DTO;

import java.util.Date;

public class ParameterView extends DTO {
   private String stParamID;
   private Long lgParamSeq;
   private Date dtValueDate;
   private String stValueString;
   private Long lgValueNumber;
   private Date dtLastUpdateDate;
   private String stParamDesc;
   private String stParamType;
   private String stParamGroup;
   private Long lgParamOrder;

   public static String tableName = "s_parameter";

   public static String fieldMap[][] = {
      {"stParamID","param_id*pk"},
      {"lgParamSeq","param_seq*pk"},
      {"dtValueDate","value_date"},
      {"stValueString","value_string"},
      {"lgValueNumber","value_number"},
      {"dtLastUpdateDate","last_update_date"},
      {"stParamDesc","param_desc"},
      {"stParamType","param_type"},
      {"stParamGroup","param_group"},
      {"lgParamOrder","param_order"},
   };

   public String getStParamID() {
      return stParamID;
   }

   public void setStParamID(String stParamID) {
      this.stParamID = stParamID;
   }

   public Long getLgParamSeq() {
      return lgParamSeq;
   }

   public void setLgParamSeq(Long lgParamSeq) {
      this.lgParamSeq = lgParamSeq;
   }

   public Date getDtValueDate() {
      return dtValueDate;
   }

   public void setDtValueDate(Date dtValueDate) {
      this.dtValueDate = dtValueDate;
   }

   public String getStValueString() {
      return stValueString;
   }

   public void setStValueString(String stValueString) {
      this.stValueString = stValueString;
   }

   public Long getLgValueNumber() {
      return lgValueNumber;
   }

   public void setLgValueNumber(Long lgValueNumber) {
      this.lgValueNumber = lgValueNumber;
   }

   public Date getDtLastUpdateDate() {
      return dtLastUpdateDate;
   }

   public void setDtLastUpdateDate(Date dtLastUpdateDate) {
      this.dtLastUpdateDate = dtLastUpdateDate;
   }

   public String getStParamDesc() {
      return stParamDesc;
   }

   public void setStParamDesc(String stParamDesc) {
      this.stParamDesc = stParamDesc;
   }

   public String getStParamType() {
      return stParamType;
   }

   public void setStParamType(String stParamType) {
      this.stParamType = stParamType;
   }

   public String getStParamGroup() {
      return stParamGroup;
   }

   public void setStParamGroup(String stParamGroup) {
      this.stParamGroup = stParamGroup;
   }

   public Long getLgParamOrder() {
      return lgParamOrder;
   }

   public void setLgParamOrder(Long lgParamOrder) {
      this.lgParamOrder = lgParamOrder;
   }
}
