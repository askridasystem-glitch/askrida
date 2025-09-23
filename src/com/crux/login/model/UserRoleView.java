/***********************************************************************
 * Module:  com.crux.login.model.UserRoleView
 * Author:  Denny Mahendra
 * Created: May 5, 2004 3:15:26 PM
 * Purpose:
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;
import com.crux.util.DTOList;

public class UserRoleView extends DTO implements RecordAudit {

   private String stUserID;
   private String stRoleID;
   private String stRoleName;
   private DTOList roles;

   public static String tableName = "s_user_roles";

   public static String fieldMap[][] = {
      {"stUserID", "user_id*pk*nd"},
      {"stRoleID", "role_id*pk"},
      {"stRoleName", "role_name*n"}
   };

   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }

   public String getStRoleID() {
      return stRoleID;
   }

   public void setStRoleID(String stRoleID) {
      this.stRoleID = stRoleID;
   }

   public DTOList getRoles() {
      return roles;
   }

   public void setRoles(DTOList roles) {
      this.roles = roles;
   }

   public String getStRoleName() {
      return stRoleName;
   }

   public void setStRoleName(String stRoleName) {
      this.stRoleName = stRoleName;
   }
}


