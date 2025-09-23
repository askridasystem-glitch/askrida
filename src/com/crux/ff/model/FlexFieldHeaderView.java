/***********************************************************************
 * Module:  com.crux.ff.model.FlexFieldHeaderView
 * Author:  Denny Mahendra
 * Created: Apr 28, 2006 10:28:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.ff.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;

import java.util.HashMap;

public class FlexFieldHeaderView extends DTO implements RecordAudit {

   public static String tableName = "ff_header";
   public static String fieldMap[][] = {
      {"stFlexFieldHeaderID","ff_header_id*pk"},
      {"stDescription","description"},
      {"stDescField","desc_field"},
      {"stReference1","ref1"},
   };

   private String stFlexFieldHeaderID;
   private String stDescription;
   private String stDescField;
   private String stReference1;
   private DTOList details;
   private DTOList reportHidden;
   private DTOList flexibleOrder;

   public Object getDesc(String stFieldName, DTO obj) {
      final HashMap fieldRefMap = getDetails().getMapOf("field_ref");

      final FlexFieldDetailView ffd = (FlexFieldDetailView)fieldRefMap.get(stFieldName);

      if (ffd==null) return "?";

      return ffd.getDesc(obj);
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStDescField() {
      return stDescField;
   }

   public void setStDescField(String stDescField) {
      this.stDescField = stDescField;
   }

   public String getStFlexFieldHeaderID() {
      return stFlexFieldHeaderID;
   }

   public void setStFlexFieldHeaderID(String stFlexFieldHeaderID) {
      this.stFlexFieldHeaderID = stFlexFieldHeaderID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public DTOList getDetails() {

      if (details==null) {
         try {
            details = ListUtil.getDTOListFromQuery(
                    "select * from ff_detail where ff_header_id = ? order by field_no",
                    new Object [] {stFlexFieldHeaderID},
                    FlexFieldDetailView.class
            );
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }
   
   public DTOList getReportHidden() {

      if (reportHidden==null) {
         try {
            reportHidden = ListUtil.getDTOListFromQuery(
                    "select * from ff_detail where ff_header_id = ? and report_hidden is null order by field_no",
                    new Object [] {stFlexFieldHeaderID},
                    FlexFieldDetailView.class
            );
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      return reportHidden;
   }

   public void setReportHidden(DTOList reportHidden) {
      this.reportHidden = reportHidden;
   }

   public DTOList getFlexibleOrder() {

      if (flexibleOrder==null) {
         try {
            flexibleOrder = ListUtil.getDTOListFromQuery(
                    "select * from ff_detail where ff_header_id = ? order by order_no",
                    new Object [] {stFlexFieldHeaderID},
                    FlexFieldDetailView.class
            );
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      return flexibleOrder;
   }

   public void setFlexibleOrder(DTOList flexibleOrder) {
      this.flexibleOrder = flexibleOrder;
   }

}
