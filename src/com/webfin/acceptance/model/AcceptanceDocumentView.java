/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyDocumentView
 * Author:  Denny Mahendra
 * Created: Dec 17, 2006 4:02:34 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.acceptance.model;

import com.webfin.insurance.model.*;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

public class AcceptanceDocumentView extends DTO implements RecordAudit {

   /*

CREATE TABLE ins_pol_documents
(
  ins_pol_document_id int8 NOT NULL,
  ins_document_type_id int8,
  document_class varchar(8),
  file_physic int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_documents_pk PRIMARY KEY (ins_pol_document_id)
)

ALTER TABLE ins_pol_documents ADD COLUMN policy_id int8;
ALTER TABLE ins_pol_documents ADD COLUMN ins_pol_obj_id int8;

   */

   public static String tableName = "ins_pol_acceptance_documents";

   public static String fieldMap[][] = {
      {"stInsurancePolicyDocumentID","ins_pol_document_id*pk*nd"},
      {"stInsuranceDocumentTypeID","ins_document_type_id"},
      {"stDocumentClass","document_class"},
      {"stFilePhysic","file_physic"},
      {"stPolicyID","policy_id"},
      {"stInsurancePolicyObjectID","ins_pol_obj_id"},
      {"stDescription","description*n"},
   };

   private String stInsurancePolicyDocumentID;
   private String stInsuranceDocumentTypeID;
   private String stDocumentClass;
   private String stFilePhysic;
   private String stPolicyID;
   private String stInsurancePolicyObjectID;
   private String stDescription;
   private String stSelectedFlag;

   public String getStSelectedFlag() {
      return stSelectedFlag;
   }

   public void setStSelectedFlag(String stSelectedFlag) {
      this.stSelectedFlag = stSelectedFlag;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStInsurancePolicyObjectID() {
      return stInsurancePolicyObjectID;
   }

   public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
      this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
   }

   public String getStInsurancePolicyDocumentID() {
      return stInsurancePolicyDocumentID;
   }

   public void setStInsurancePolicyDocumentID(String stInsurancePolicyDocumentID) {
      this.stInsurancePolicyDocumentID = stInsurancePolicyDocumentID;
   }

   public String getStInsuranceDocumentTypeID() {
      return stInsuranceDocumentTypeID;
   }

   public void setStInsuranceDocumentTypeID(String stInsuranceDocumentTypeID) {
      this.stInsuranceDocumentTypeID = stInsuranceDocumentTypeID;
   }

   public String getStDocumentClass() {
      return stDocumentClass;
   }

   public void setStDocumentClass(String stDocumentClass) {
      this.stDocumentClass = stDocumentClass;
   }

   public String getStFilePhysic() {
      return stFilePhysic;
   }

   public void setStFilePhysic(String stFilePhysic) {
      this.stFilePhysic = stFilePhysic;
   }

   public boolean isMarked() {
      return Tools.isYes(stSelectedFlag);
   }
}
