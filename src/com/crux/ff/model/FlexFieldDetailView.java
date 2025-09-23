/***********************************************************************
 * Module:  com.crux.ff.model.FlexFieldDetailView
 * Author:  Denny Mahendra
 * Created: Apr 28, 2006 10:29:23 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.ff.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.LOV;
import com.crux.lov.LOVManager;

import java.util.HashMap;

public class FlexFieldDetailView extends DTO implements RecordAudit{

   public static String tableName = "ff_detail";

   public static String fieldMap[][] = {
      {"stFlexFieldDetailID","ff_detail_id*pk"},
      {"stFlexFieldHeaderID","ff_header_id"},
      {"stFieldNo","field_no"},
      {"stFieldDesc","field_desc"},
      {"stFieldRef","field_ref"},
      {"stFieldType","field_type"},
      {"stMandatoryFlag","field_mandatory"},
      {"lgWidth","field_width"},
      {"lgHeight","field_height"},
      {"stLOV","lov"},
      {"stLOVPopFlag","lov_pop"},
      {"stReadOnlyFlag","read_only"},
      {"stReference1","reference1"},
      {"stReference2","reference2"},
      {"stFieldID","field_id"},
      {"stDescFieldRef","desc_field_ref"},
      {"stHidden","hidden"},
      {"stClientChangeAction","clientchangeaction"},
      {"stRefresh","refresh"},
      {"stLovLink","lov_link"},
      {"stReportHidden","report_hidden"},
     // {"stRule","rule"},
   };

   /*
ALTER TABLE ff_detail ADD COLUMN reference1 varchar(255);
ALTER TABLE ff_detail ADD COLUMN reference2 varchar(255);

   */

   private String stFlexFieldDetailID;
   private String stFlexFieldHeaderID;
   private String stFieldNo;
   private String stFieldDesc;
   private String stFieldRef;
   private String stDescFieldRef;
   private String stFieldType;
   private String stMandatoryFlag;
   private String stLOVPopFlag;
   private String stReference1;
   private String stReference2;
   private String stFieldID;
   private Long lgWidth;
   private Long lgHeight;
   private String stLOV;
   private String stHidden;
   private String stReadOnlyFlag;
   private String stClientChangeAction;
   private String stRefresh;
   private String stLovLink;
   private String stReportHidden;
   
   public String getStLovLink() {
      return stLovLink;
   }

   public void setStLovLink(String stLovLink) {
      this.stLovLink = stLovLink;
   }

   public String getStDescFieldRef() {
      return stDescFieldRef;
   }

   public void setStDescFieldRef(String stDescFieldRef) {
      this.stDescFieldRef = stDescFieldRef;
   }

   public String getStFieldID() {
      return stFieldID;
   }

   public void setStFieldID(String stFieldID) {
      this.stFieldID = stFieldID;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStReference2() {
      return stReference2;
   }

   public void setStReference2(String stReference2) {
      this.stReference2 = stReference2;
   }

   public String getStReadOnlyFlag() {
      return stReadOnlyFlag;
   }

   public void setStReadOnlyFlag(String stReadOnlyFlag) {
      this.stReadOnlyFlag = stReadOnlyFlag;
   }

   public String getStLOVPopFlag() {
      return stLOVPopFlag;
   }

   public void setStLOVPopFlag(String stLOVPopFlag) {
      this.stLOVPopFlag = stLOVPopFlag;
   }

   public String getStFlexFieldDetailID() {
      return stFlexFieldDetailID;
   }

   public void setStFlexFieldDetailID(String stFlexFieldDetailID) {
      this.stFlexFieldDetailID = stFlexFieldDetailID;
   }

   public String getStFlexFieldHeaderID() {
      return stFlexFieldHeaderID;
   }

   public void setStFlexFieldHeaderID(String stFlexFieldHeaderID) {
      this.stFlexFieldHeaderID = stFlexFieldHeaderID;
   }

   public String getStFieldNo() {
      return stFieldNo;
   }

   public void setStFieldNo(String stFieldNo) {
      this.stFieldNo = stFieldNo;
   }

   public String getStFieldDesc() {
      return stFieldDesc;
   }

   public void setStFieldDesc(String stFieldDesc) {
      this.stFieldDesc = stFieldDesc;
   }

   public String getStFieldRef() {
      return stFieldRef;
   }

   public void setStFieldRef(String stFieldRef) {
      this.stFieldRef = stFieldRef;
   }

   public String getStFieldType() {
      return stFieldType;
   }

   public void setStFieldType(String stFieldType) {
      this.stFieldType = stFieldType;
   }

   public String getStMandatoryFlag() {
      return stMandatoryFlag;
   }

   public void setStMandatoryFlag(String stMandatoryFlag) {
      this.stMandatoryFlag = stMandatoryFlag;
   }

   public Long getLgWidth() {
      return lgWidth;
   }

   public void setLgWidth(Long lgWidth) {
      this.lgWidth = lgWidth;
   }

   public String getStLOV() {
      return stLOV;
   }

   public void setStLOV(String stLOV) {
      this.stLOV = stLOV;
   }

   public Long getLgHeight() {
      return lgHeight;
   }

   public void setLgHeight(Long lgHeight) {
      this.lgHeight = lgHeight;
   }
   
   public String getStHidden() {
      return stHidden;
   }

   public void setStHidden(String stHidden) {
      this.stHidden = stHidden;
   }

   public Object getDesc(DTO obj) {
      try {
         final Object val = obj.getProperty(stFieldRef);

         if (stLOV==null) return val;

         HashMap par = new HashMap();

         par.put("value",val);

         final LOV lov = LOVManager.getInstance().getLOV(this.stLOV, par);

         return lov.getComboDesc((String)val);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   public String getStClientChangeAction() {
      return stClientChangeAction;
   }

   public void setStClientChangeAction(String stClientChangeAction) {
      this.stClientChangeAction = stClientChangeAction;
   }
   
   public String getStRefresh() {
      return stRefresh;
   }

   public void setStRefresh(String stRefresh) {
      this.stRefresh = stRefresh;
   }

   public String getStReportHidden() {
        return stReportHidden;
   }

   public void setStReportHidden(String stReportHidden) {
        this.stReportHidden = stReportHidden;
   }
   
}
