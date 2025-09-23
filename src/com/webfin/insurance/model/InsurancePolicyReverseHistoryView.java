/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceCoverView
 * Author:  Denny Mahendra
 * Created: Jan 28, 2006 5:07:58 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.util.Date;

public class InsurancePolicyReverseHistoryView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_reverse_history
(
  ins_pol_reverse_id bigint NOT NULL,
  pol_id bigint,
  pol_no character varying(32),
  reverse_who character varying(32),
  reverse_date timestamp without time zone,
  reverse_notes text,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  CONSTRAINT ins_pol_reverse_history_pkey PRIMARY KEY (ins_pol_reverse_id)
)

   */

   public static String tableName = "ins_pol_reverse_history";

   public static String fieldMap[][] = {
      {"stInsurancePolicyReverseID","ins_pol_reverse_id*pk"},
      {"stPolicyID","pol_id"},
      {"stPolicyNo","pol_no"},
      {"stReverseWho","reverse_who"},
      {"stReverseName","reverse_name"},
      {"dtReverseDate","reverse_date"},
      {"stReverseNotes","reverse_notes"},
      {"stReverseTime","reverse_time*n"},
   };


   private String stInsurancePolicyReverseID;
   private String stPolicyID;
   private String stPolicyNo;
   private String stReverseWho;
   private String stReverseNotes;
   private String stReverseName;

   private Date dtReverseDate;

   private String stReverseTime;

    public String getStReverseTime() {
        return stReverseTime;
    }

    public void setStReverseTime(String stReverseTime) {
        this.stReverseTime = stReverseTime;
    }

    public String getStReverseName() {
        return stReverseName;
    }

    public void setStReverseName(String stReverseName) {
        this.stReverseName = stReverseName;
    }
 
   public String getStInsurancePolicyReverseID() {
        return stInsurancePolicyReverseID;
    }

    public void setStInsurancePolicyReverseID(String stInsurancePolicyReverseID) {
        this.stInsurancePolicyReverseID = stInsurancePolicyReverseID;
    }

    public Date getDtReverseDate() {
        return dtReverseDate;
    }

    public void setDtReverseDate(Date dtReverseDate) {
        this.dtReverseDate = dtReverseDate;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public String getStPolicyNo() {
        return stPolicyNo;
    }

    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    public String getStReverseNotes() {
        return stReverseNotes;
    }

    public void setStReverseNotes(String stReverseNotes) {
        this.stReverseNotes = stReverseNotes;
    }

    public String getStReverseWho() {
        return stReverseWho;
    }

    public void setStReverseWho(String stReverseWho) {
        this.stReverseWho = stReverseWho;
    }



}
