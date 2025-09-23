/***********************************************************************
 * Module:  com.webfin.gl.model.JournalMasterView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 8:33:01 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.util.Date;

public class JournalMasterView extends DTO implements RecordAudit {

   private String stJournalCode;
   private String stDescription;
   private String stJournalType;
   private String stJournalFreq;
   private Date dtEndDate;
   private Date dtLastPosted;

   public static String comboFields[] = {"journal_code","description"};

   public static String tableName = "gl_journal_master";

   public static String fieldMap[][] = {
      {"stJournalCode","journal_code*pk"},
      {"stDescription","description"},
      {"stJournalType","journal_type"},
      {"stJournalFreq","journal_freq"},
      {"dtEndDate","enddate"},
      {"dtLastPosted","lastposted"},
   };

   public String getStJournalCode() {
      return stJournalCode;
   }

   public void setStJournalCode(String stJournalCode) {
      this.stJournalCode = stJournalCode;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStJournalType() {
      return stJournalType;
   }

   public void setStJournalType(String stJournalType) {
      this.stJournalType = stJournalType;
   }

   public String getStJournalFreq() {
      return stJournalFreq;
   }

   public void setStJournalFreq(String stJournalFreq) {
      this.stJournalFreq = stJournalFreq;
   }

   public Date getDtEndDate() {
      return dtEndDate;
   }

   public void setDtEndDate(Date dtEndDate) {
      this.dtEndDate = dtEndDate;
   }

   public Date getDtLastPosted() {
      return dtLastPosted;
   }

   public void setDtLastPosted(Date dtLastPosted) {
      this.dtLastPosted = dtLastPosted;
   }
}
