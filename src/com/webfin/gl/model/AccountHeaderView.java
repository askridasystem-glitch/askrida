/***********************************************************************
 * Module:  com.webfin.gl.model.AccountHeaderView
 * Author:  Denny Mahendra
 * Created: Jun 15, 2004 11:55:13 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;


public class AccountHeaderView extends DTO {
   private String stAccountHeaderID;
   private Long lgParamSeq;
   private String stValueString;
   private String stParamType;

   public static String tableName = "gl_accounts_header";

   public static String fieldMap[][] = {
      {"stAccountHeaderID","acc_hdr_id*pk"},
      {"lgParamSeq","param_seq"},
      {"stValueString","value_string"},
      {"stParamType","param_type"},
   };

    public String getStAccountHeaderID() {
        return stAccountHeaderID;
    }

    public void setStAccountHeaderID(String stAccountHeaderID) {
        this.stAccountHeaderID = stAccountHeaderID;
    }

    public Long getLgParamSeq() {
        return lgParamSeq;
    }

    public void setLgParamSeq(Long lgParamSeq) {
        this.lgParamSeq = lgParamSeq;
    }

    public String getStValueString() {
        return stValueString;
    }

    public void setStValueString(String stValueString) {
        this.stValueString = stValueString;
    }

    public String getStParamType() {
        return stParamType;
    }

    public void setStParamType(String stParamType) {
        this.stParamType = stParamType;
    }
}
