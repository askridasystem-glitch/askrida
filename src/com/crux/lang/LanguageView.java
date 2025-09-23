/***********************************************************************
 * Module:  com.crux.lang.LanguageView
 * Author:  Denny Mahendra
 * Created: Jun 11, 2006 7:17:24 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.lang;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class LanguageView extends DTO implements RecordAudit {
   /*
   CREATE TABLE s_lang
(
  lang_id varchar(3) NOT NULL,
  lang_name  varchar(128),
  active_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_lang_pk PRIMARY KEY (lang_id)
) without oids;
   */

   public static String tableName = "s_lang";

   public static String fieldMap[][] = {
      {"stLanguageID","lang_id"},
      {"stLanguageName","lang_name"},
      {"stActiveFlag","active_flag"},
      {"stLangOrder","lang_order"},
   };

   private String stLanguageID;
   private String stLanguageName;
   private String stActiveFlag;
   private String stLangOrder;

   public String getStLangOrder() {
      return stLangOrder;
   }

   public void setStLangOrder(String stLangOrder) {
      this.stLangOrder = stLangOrder;
   }

   public String getStLanguageID() {
      return stLanguageID;
   }

   public void setStLanguageID(String stLanguageID) {
      this.stLanguageID = stLanguageID;
   }

   public String getStLanguageName() {
      return stLanguageName;
   }

   public void setStLanguageName(String stLanguageName) {
      this.stLanguageName = stLanguageName;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }


}
