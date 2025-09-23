/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePrintLogView
 * Author:  Denny Mahendra
 * Created: Jul 23, 2006 6:11:22 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsurancePrintLogView extends DTO implements RecordAudit {
   /*

   CREATE TABLE ins_prt_log
(
  ins_prt_log_id int8 NOT NULL,
  policy_id int8,
  print_type varchar(64),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_prt_log_pk PRIMARY KEY (ins_prt_log_id)
) without oids;
   */

   public static String tableName = "ins_prt_log";


   public static String fieldMap[][] = {
      {"stInsurancePrintLogID","ins_prt_log_id*pk"},
      {"stPolicyID","policy_id"},
      {"stPrintType","print_type"},
      {"stSerialNumber","serial_number"},
   };

   private String stInsurancePrintLogID;
   private String stPolicyID;
   private String stPrintType;
   private String stSerialNumber;

   public String getStSerialNumber() {
      return stSerialNumber;
   }

   public void setStSerialNumber(String stSerialNumber) {
      this.stSerialNumber = stSerialNumber;
   }

   public String getStInsurancePrintLogID() {
      return stInsurancePrintLogID;
   }

   public void setStInsurancePrintLogID(String stInsurancePrintLogID) {
      this.stInsurancePrintLogID = stInsurancePrintLogID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStPrintType() {
      return stPrintType;
   }

   public void setStPrintType(String stPrintType) {
      this.stPrintType = stPrintType;
   }
}
