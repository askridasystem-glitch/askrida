/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.approval.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.insurance.model.InsuranceEntityView;

import java.util.Date;

public class ApprovalView extends DTO implements RecordAudit {
  

/*
	CREATE TABLE incoming_letter
(
  ref_no character varying(32) NOT NULL,
  sender character varying(100),
  letter_date timestamp without time zone,
  subject character varying(80),
  create_who character varying(32) NOT NULL,
  create_date timestamp without time zone NOT NULL,
  change_who character varying(32),
  change_date timestamp without time zone,
  note character varying(200),
  CONSTRAINT pk_incoming_letter PRIMARY KEY (ref_no)
)
   
*/
   public static String tableName = "ins_approval_notification";

   public static String comboFields[] = {"in_id","sender","receiver"};

   public static String fieldMap[][] = {
      {"stInID","in_id*pk"},
      {"stRefNo","ref_no"},
      {"stSender","sender"},
      {"dtLetterDate","letter_date"},
      {"stSubject","subject"},
      {"stNote","note"},
      {"stReceiver","receiver"},
      {"stFilePhysic","file_physic"},
      {"stReadFlag","read_flag"},
      {"stDeleteFlag","delete_flag"},
      {"stSenderName","sender_name"},
      {"stCC","cc"},
      {"stCostCenterCode","cc_code"},
      {"stRiskAnalysisEffectiveFlag","risk_analysis_eff_f"},
      {"stValidasiCabang", "validasi_cabang_f"},
      {"stValidasiCabangInduk", "validasi_induk_f"},
      {"stValidasiKantorPusat", "validasi_headoffice_f"},
      {"stPolicyID","pol_id"},
      {"stStatus","status"},
      {"stUnreadMail","unread_mail*n"},
      {"stTotalMail","total_mail*n"},
      {"stJam","jam*n"},
      
   };

   private String stRefNo;
   private String stSender;
   private String stSubject;
   private String stNote;
   private Date dtLetterDate;
   private String stReceiver;
   private String stInID;
   private String stFilePhysic;
   private String stReadFlag;
   private String stUnreadMail;
   private String stDeleteFlag;
   private String stSenderName;
   private String stTotalMail;
   private String stCC;
   private String stJam;
   private String stCostCenterCode;
   private String stRiskAnalysisEffectiveFlag;
   private String stPolicyID;
   private String stValidasiCabang;
   private String stValidasiCabangInduk;
   private String stValidasiKantorPusat;
   private String stStatus;
   private String stLimit;

   private DTOList distributions;
   private IncomingDistributionView primaryAddress;
   private DTOList documents;
   
   
   public String getStTotalMail() {
      return stTotalMail;
   }

   public void setStTotalMail(String stTotalMail) {
      this.stTotalMail = stTotalMail;
   }
   
   public String getStSenderName() {
      return stSenderName;
   }

   public void setStSenderName(String stSenderName) {
      this.stSenderName = stSenderName;
   }
   
   public String getStUnreadMail() {
      return stUnreadMail;
   }

   public void setStUnreadMail(String stUnreadMail) {
      this.stUnreadMail = stUnreadMail;
   }
   
   public String getStFilePhysic() {
      return stFilePhysic;
   }

   public void setStFilePhysic(String stFilePhysic) {
      this.stFilePhysic = stFilePhysic;
   }
   
   public String getStReadFlag() {
      return stReadFlag;
   }

   public void setStReadFlag(String stReadFlag) {
      this.stReadFlag = stReadFlag;
   }
   
   public String getStDeleteFlag() {
      return stDeleteFlag;
   }

   public void setStDeleteFlag(String stDeleteFlag) {
      this.stDeleteFlag = stDeleteFlag;
   }
   
   public String getStInID() {
      return stInID;
   }

   public void setStInID(String stInID) {
      this.stInID = stInID;
   }
   
   public String getStRefNo() {
      return stRefNo;
   }

   public void setStRefNo(String stRefNo) {
      this.stRefNo = stRefNo;
   }
   
   public String getStReceiver() {
      return stReceiver;
   }

   public void setStReceiver(String stReceiver) {
      this.stReceiver = stReceiver;
   }
   
   public String getStNote() {
      return stNote;
   }

   public void setStNote(String stNote) {
      this.stNote = stNote;
   }
   
   public String getStSender() {
      return stSender;
   }

   public void setStSender(String stSender) {
      this.stSender = stSender;
   }
   
   public String getStSubject() {
      return stSubject;
   }

   public void setStSubject(String stSubject) {
      this.stSubject = stSubject;
   }
   
   public Date getDtLetterDate() {
      return dtLetterDate;
   }

   public void setDtLetterDate(Date dtLetterDate) {
      this.dtLetterDate = dtLetterDate;
   }

   public DTOList getDistributions() {
      loadDistributions();
      return distributions;
   }

   private void loadDistributions() {
      try {
         if (distributions == null)
            distributions = ListUtil.getDTOListFromQuery(
                    "select * from incoming_dist where in_id = ? and delete_flag is null",
                    new Object [] {stInID},
                    IncomingDistributionView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setDistributions(DTOList distributions) {
      this.distributions = distributions;
   }

   public IncomingDistributionView getPrimaryAddress() {
      return primaryAddress;
   }

   public void setPrimaryAddress(IncomingDistributionView primaryAddress) {
      this.primaryAddress = primaryAddress;
   }

   public DTOList getDocuments() {
      loadDocuments();
      return documents;
   }

   private void loadDocuments() {
      try {
         if (documents == null)
            documents = ListUtil.getDTOListFromQuery(
                    "select * from incoming_documents where in_id = ?",
                    new Object [] {stInID},
                    IncomingDocumentsView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setDocuments(DTOList documents) {
      this.documents = documents;
   }

    public String getStCC() {
        return stCC;
    }

    public void setStCC(String stCC) {
        this.stCC = stCC;
    }

    /**
     * @return the stJam
     */
    public String getStJam() {
        return stJam;
    }

    /**
     * @param stJam the stJam to set
     */
    public void setStJam(String stJam) {
        this.stJam = stJam;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stRiskAnalysisEffectiveFlag
     */
    public String getStRiskAnalysisEffectiveFlag() {
        return stRiskAnalysisEffectiveFlag;
    }

    /**
     * @param stRiskAnalysisEffectiveFlag the stRiskAnalysisEffectiveFlag to set
     */
    public void setStRiskAnalysisEffectiveFlag(String stRiskAnalysisEffectiveFlag) {
        this.stRiskAnalysisEffectiveFlag = stRiskAnalysisEffectiveFlag;
    }

    /**
     * @return the stPolicyID
     */
    public String getStPolicyID() {
        return stPolicyID;
    }

    /**
     * @param stPolicyID the stPolicyID to set
     */
    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    /**
     * @return the stValidasiCabang
     */
    public String getStValidasiCabang() {
        return stValidasiCabang;
    }

    /**
     * @param stValidasiCabang the stValidasiCabang to set
     */
    public void setStValidasiCabang(String stValidasiCabang) {
        this.stValidasiCabang = stValidasiCabang;
    }

    /**
     * @return the stValidasiCabangInduk
     */
    public String getStValidasiCabangInduk() {
        return stValidasiCabangInduk;
    }

    /**
     * @param stValidasiCabangInduk the stValidasiCabangInduk to set
     */
    public void setStValidasiCabangInduk(String stValidasiCabangInduk) {
        this.stValidasiCabangInduk = stValidasiCabangInduk;
    }

    /**
     * @return the stValidasiKantorPusat
     */
    public String getStValidasiKantorPusat() {
        return stValidasiKantorPusat;
    }

    /**
     * @param stValidasiKantorPusat the stValidasiKantorPusat to set
     */
    public void setStValidasiKantorPusat(String stValidasiKantorPusat) {
        this.stValidasiKantorPusat = stValidasiKantorPusat;
    }

    /**
     * @return the stStatus
     */
    public String getStStatus() {
        return stStatus;
    }

    /**
     * @param stStatus the stStatus to set
     */
    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    /**
     * @return the stLimit
     */
    public String getStLimit() {
        if(Tools.isYes(stValidasiCabang) && Tools.isNo(stValidasiCabangInduk) && Tools.isNo(stValidasiKantorPusat) && Tools.isNo(stRiskAnalysisEffectiveFlag))
            return "INDUK";

        if(Tools.isYes(stValidasiCabang) && Tools.isYes(stValidasiCabangInduk) && Tools.isNo(stRiskAnalysisEffectiveFlag))
            return "HEAD OFFICE";

        return "FINISH";
    }

    /**
     * @param stLimit the stLimit to set
     */
    public void setStLimit(String stLimit) {
        this.stLimit = stLimit;
    }
}
