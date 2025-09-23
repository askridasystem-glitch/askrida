/***********************************************************************
 * Module:  com.webfin.insurance.model.PrintingCodesView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2006 12:10:10 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;

public class PrintingCodesView extends DTO {


   public static String tableName = "";

   public static String fieldMap[][] = {
      {"stVSGroup","vs_group"},
      {"stVSDesc","vs_description"},
      {"stVSCode","vs_code"},
      {"stRef2","ref2"},
      {"stDefaultFlag","default_flag"},
   };

   public static String comboFields[] = {"vs_code","vs_description","ref2"};

   private String stVSGroup;
   private String stVSCode;
   private String stVSDesc;
   private String stRef2;
   private String stDefaultFlag;

   public String getStDefaultFlag() {
      return stDefaultFlag;
   }

   public void setStDefaultFlag(String stDefaultFlag) {
      this.stDefaultFlag = stDefaultFlag;
   }

   public String getStVSDesc() {
      return stVSDesc;
   }

   public void setStVSDesc(String stVSDesc) {
      this.stVSDesc = stVSDesc;
   }

   public String getStVSGroup() {
      return stVSGroup;
   }

   public void setStVSGroup(String stVSGroup) {
      this.stVSGroup = stVSGroup;
   }

   public String getStVSCode() {
      return stVSCode;
   }

   public void setStVSCode(String stVSCode) {
      this.stVSCode = stVSCode;
   }

   public String getStRef2() {
      return stRef2;
   }

   public void setStRef2(String stRef2) {
      this.stRef2 = stRef2;
   }
}
