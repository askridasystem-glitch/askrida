/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceDocumentView
 * Author:  Denny Mahendra
 * Created: Dec 17, 2006 4:08:04 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsuranceDocumentView extends DTO implements RecordAudit {

   /*
   CREATE TABLE ins_documents
(
  ins_documents_id int8 NOT NULL,
  ins_document_type_id int8,
  pol_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  document_class varchar(8),
  CONSTRAINT ins_documents_pk PRIMARY KEY (ins_documents_id)
)
   */

   public static String tableName = "ins_documents";

   public static String fieldMap[][] = {
      {"stInsuranceDocumentID","ins_documents_id*pk"},
      {"stInsuranceDocumentTypeID","ins_document_type_id"},
      {"stPolicyTypeID","pol_type_id"},
      {"stDocumentClass","document_class"},
   };

   private String stInsuranceDocumentID;
   private String stInsuranceDocumentTypeID;
   private String stPolicyTypeID;
   private String stDocumentClass;

   public String getStInsuranceDocumentID() {
      return stInsuranceDocumentID;
   }

   public void setStInsuranceDocumentID(String stInsuranceDocumentID) {
      this.stInsuranceDocumentID = stInsuranceDocumentID;
   }

   public String getStInsuranceDocumentTypeID() {
      return stInsuranceDocumentTypeID;
   }

   public void setStInsuranceDocumentTypeID(String stInsuranceDocumentTypeID) {
      this.stInsuranceDocumentTypeID = stInsuranceDocumentTypeID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStDocumentClass() {
      return stDocumentClass;
   }

   public void setStDocumentClass(String stDocumentClass) {
      this.stDocumentClass = stDocumentClass;
   }
}
