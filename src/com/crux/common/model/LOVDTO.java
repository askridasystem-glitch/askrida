/***********************************************************************
 * Module:  com.crux.common.model.LOVDTO
 * Author:  Denny Mahendra
 * Created: Nov 21, 2004 7:35:52 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

public class LOVDTO extends DTO {

   private String stID;
   private String stValue;

   public static String comboFields[] = {"id","value"};

   public static String fieldMap[][] = {
      {"stID","id"},
      {"stValue","value"},
   };

   public String getStID() {
      return stID;
   }

   public void setStID(String stID) {
      this.stID = stID;
   }

   public String getStValue() {
      return stValue;
   }

   public void setStValue(String stValue) {
      this.stValue = stValue;
   }
}
