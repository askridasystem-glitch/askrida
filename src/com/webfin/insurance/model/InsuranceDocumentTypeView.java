/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceDocumentTypeView
 * Author:  Denny Mahendra
 * Created: Dec 17, 2006 4:04:27 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsuranceDocumentTypeView  extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_document_type
(
  ins_document_type_id int8 NOT NULL,
  description varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_document_pk PRIMARY KEY (ins_document_type_id)
)
   */

   public static String tableName = "ins_document_type";

   public static String fieldMap[][] = {
      {"stInsuranceDocumentTypeID","ins_document_type_id*pk"},
      {"stDescription","description"},
   };

   public static String comboFields[] = {"ins_document_type_id","description"};

   private String stInsuranceDocumentTypeID;
   private String stDescription;

   public String getStInsuranceDocumentTypeID() {
      return stInsuranceDocumentTypeID;
   }

   public void setStInsuranceDocumentTypeID(String stInsuranceDocumentTypeID) {
      this.stInsuranceDocumentTypeID = stInsuranceDocumentTypeID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
}
