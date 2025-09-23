/***********************************************************************
 * Module:  com.crux.login.model.FunctionsView
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:11:20 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.codedecode.Codec;

public class FunctionsView extends DTO {
   
   private String stFunctionID;
   private String stFunctionName;
   private String stEventID;
   private String stURL;
   private String stResourceID;
   private String stOrderSequence;

   public static String tableName = "s_functions";

   public static String fieldMap[][] = {
      {"stFunctionID","FUNCTION_ID"},
      {"stFunctionName","FUNCTION_NAME"},
      {"stEventID","CTL_ID"},
      {"stURL","URL"},
      {"stResourceID","resource_id"},
      {"stOrderSequence","orderseq"},

   };

   public String getStResourceID() {
      return stResourceID;
   }

   public void setStResourceID(String stResourceID) {
      this.stResourceID = stResourceID;
   }

   public String getStEventID() {
      return stEventID;
   }

   public void setStEventID(String stEventID) {
      this.stEventID = stEventID;
   }

   public String getStFunctionID() {
      return stFunctionID;
   }

   public void setStFunctionID(String stFunctionID) {
      this.stFunctionID = stFunctionID;
   }

   public String getStFunctionName() {
      return stFunctionName;
   }

   public void setStFunctionName(String stFunctionName) {
      this.stFunctionName = stFunctionName;
   }

   public String getStURL() {
      if ((stEventID != null )  && (stEventID.length()>0)) return "/main.ctl?EVENT="+stEventID;
      return stURL;
   }

   public void setStURL(String stURL) {
      this.stURL = stURL;
   }

   public int getLevel() {
      return stFunctionID.indexOf("00")/3;
      //00.00.00.00
   }

   public void setFuncIndex(int level, String value) {
      final char[] ca = stFunctionID.toCharArray();

      level*=3;

      ca[level++]=value.charAt(0);
      ca[level++]=value.charAt(1);

      stFunctionID = new String(ca);
   }

    /**
     * @return the stOrderSequence
     */
    public String getStOrderSequence() {
        return stOrderSequence;
    }

    /**
     * @param stOrderSequence the stOrderSequence to set
     */
    public void setStOrderSequence(String stOrderSequence) {
        this.stOrderSequence = stOrderSequence;
    }
}
