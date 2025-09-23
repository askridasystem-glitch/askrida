/***********************************************************************
 * Module:  com.crux.login.model.FuncRoleView
 * Author:  Denny Mahendra
 * Created: Apr 29, 2004 2:24:04 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;

public class FuncRoleView extends DTO implements RecordAudit {

   private String stRoleID;
   private String stFuncID;
   private String stFuncName;

   public static String tableName = "s_func_roles";

   public static String fieldMap[][] = {
      {"stRoleID", "role_id*pk"},
      {"stFuncID", "function_id*pk"},
      {"stFuncName", "function_name*n"},
   };

   public String getStFuncName() {
      return stFuncName;
   }

   public void setStFuncName(String stFuncName) {
      this.stFuncName = stFuncName;
   }

   public String getStRoleID() {
      return stRoleID;
   }

   public void setStRoleID(String stRoleID) {
      this.stRoleID = stRoleID;
   }

   public String getStFuncID() {
      return stFuncID;
   }

   public void setStFuncID(String stFuncID) {
      this.stFuncID = stFuncID;
   }

   /**
    * return indent level of function id, ranges from 1-6
    * @return
    */
   public int getIndentLevel() {

      int zi = stFuncID.indexOf("00");

      if (zi<0) zi=stFuncID.length();

      int n = (zi+1)/3;

      return n;

      //00.00.00.00.00.00
      //012345678901234567

   }
}