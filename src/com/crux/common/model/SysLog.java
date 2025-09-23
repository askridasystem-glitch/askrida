/***********************************************************************
 * Module:  com.crux.common.model.SysLog
 * Author:  Denny Mahendra
 * Created: Aug 13, 2004 1:13:30 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class SysLog extends DTO implements RecordAudit {
   /*
   syslogid int8 NOT NULL,
  reftype varchar(20),
  refid varchar(20),
  logmsg text,
  msgdetail text,
   */

   private String stSysLogID;
   private String stRefType;
   private String stRefID;
   private String stMessage;
   private String stMessageDetail;
   private Long lgLogLevel ;

   public static String tableName = "sys_log";

   public static String fieldMap[][] = {
      {"stSysLogID","syslogid*pk"},
      {"stRefType","reftype"},
      {"stRefID","refid"},
      {"stMessage","logmsg"},
      {"stMessageDetail","msgdetail"},
      {"lgLogLevel","log_level"},
   };

   public String getStSysLogID() {
      return stSysLogID;
   }

   public void setStSysLogID(String stSysLogID) {
      this.stSysLogID = stSysLogID;
   }

   public String getStRefType() {
      return stRefType;
   }

   public void setStRefType(String stRefType) {
      this.stRefType = stRefType;
   }

   public String getStRefID() {
      return stRefID;
   }

   public void setStRefID(String stRefID) {
      this.stRefID = stRefID;
   }

   public String getStMessage() {
      return stMessage;
   }

   public void setStMessage(String stMessage) {
      this.stMessage = stMessage;
   }

   public String getStMessageDetail() {
      return stMessageDetail;
   }

   public void setStMessageDetail(String stMessageDetail) {
      this.stMessageDetail = stMessageDetail;
   }

   public Long getLgLogLevel() {
      return lgLogLevel;
   }

   public void setLgLogLevel(Long lgLogLevel) {
      this.lgLogLevel = lgLogLevel;
   }
}
