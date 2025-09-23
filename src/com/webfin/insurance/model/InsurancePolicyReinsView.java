/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyReinsView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 1:07:58 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.pool.DTOPool;
import com.webfin.entity.model.EntityView;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class InsurancePolicyReinsView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_ri
(
  ins_pol_ri_id int8 NOT NULL,
  ins_pol_treaty_id int8,
  ins_pol_tre_det_id int8,
  ins_treaty_detail_id int8,
  ins_treaty_shares_id int8,
  member_ent_id int8,
  sharepct numeric,
  premi_amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_ri_pk PRIMARY KEY (ins_pol_ri_id)
  tambahan
  ins_cover_id1 bigint,
  tsi_cover_id1 numeric,
  rate_cover1 numeric,
  premi_cover1 numeric,
  ins_cover_id2 bigint,
  tsi_cover2 numeric,
  rate_cover2 numeric,
  premi_cover2 numeric,
  ins_cover_id3 bigint,
  tsi_cover3 numeric,
  rate_cover3 numeric,
  premi_cover3 numeric,
  ins_cover_id4 bigint,
  tsi_cover4 numeric,
  rate_cover4 numeric,
  premi_cover4 numeric,
  ins_cover_id5 bigint,
  tsi_cover5 numeric,
  rate_cover5 numeric,
  premi_cover5 numeric
)
WITHOUT OIDS;
   */

   public static String tableName = "ins_pol_ri";

   public static String fieldMap[][] = {
      {"stInsurancePolicyReinsID","ins_pol_ri_id*pk*nd"},
      {"stInsurancePolicyTreatyID","ins_pol_treaty_id"},
      {"stInsurancePolicyTreatyDetailID","ins_pol_tre_det_id"},
      {"stInsuranceTreatyDetailID","ins_treaty_detail_id"},
      {"stInsuranceTreatySharesID","ins_treaty_shares_id"},
      {"stMemberEntityID","member_ent_id"},
      {"dbSharePct","sharepct"},
      {"dbPremiAmount","premi_amount"},
      {"stORFlag","or_flag"},
      {"dbTSIAmount","tsi_amount"},
      {"dbPremiRate","premi_rate"},
      {"stAutoRateFlag","auto_rate_flag"},
      {"stUseRateFlag","use_rate_flag"},
      {"dbRICommRate","ricomm_rate"},
      {"dbRICommAmount","ricomm_amt"},
      {"stNotes","notes"},
      {"stRISlipNo","ri_slip_no"},
      {"stControlFlags","control_flags"},
      {"dbClaimAmount","claim_amount"},
      {"stApprovedFlag","f_approve"},
      {"dtValidReinsuranceDate","valid_ri_date"},
      {"stInsuranceCoverID1","ins_cover_id1"},
      {"dbTSICover1","tsi_cover1"},
      {"dbRateCover1","rate_cover1"},
      {"dbPremiumCover1","premi_cover1"},
      {"stInsuranceCoverID2","ins_cover_id2"},
      {"dbTSICover2","tsi_cover2"},
      {"dbRateCover2","rate_cover2"},
      {"dbPremiumCover2","premi_cover2"},
      {"stInsuranceCoverID3","ins_cover_id3"},
      {"dbTSICover3","tsi_cover3"},
      {"dbRateCover3","rate_cover3"},
      {"dbPremiumCover3","premi_cover3"},
      {"stInsuranceCoverID4","ins_cover_id4"},
      {"dbTSICover4","tsi_cover4"},
      {"dbRateCover4","rate_cover4"},
      {"dbPremiumCover4","premi_cover4"},
      {"stInsuranceCoverID5","ins_cover_id5"},
      {"dbTSICover5","tsi_cover5"},
      {"dbRateCover5","rate_cover5"},
      {"dbPremiumCover5","premi_cover5"},
      {"stInsuranceCoverID5","ins_cover_id6"},
      {"dbTSICover5","tsi_cover6"},
      {"dbRateCover5","rate_cover6"},
      {"dbPremiumCover5","premi_cover6"},
      {"stInsuranceCoverID5","ins_cover_id7"},
      {"dbTSICover5","tsi_cover7"},
      {"dbRateCover5","rate_cover7"},
      {"dbPremiumCover5","premi_cover7"},
      {"stInsuranceCoverID5","ins_cover_id8"},
      {"dbTSICover5","tsi_cover8"},
      {"dbRateCover5","rate_cover8"},
      {"dbPremiumCover5","premi_cover8"},
      {"stInsuranceCoverID5","ins_cover_id9"},
      {"dbTSICover5","tsi_cover9"},
      {"dbRateCover5","rate_cover9"},
      {"dbPremiumCover5","premi_cover9"},
      {"stInsuranceCoverID5","ins_cover_id10"},
      {"dbTSICover5","tsi_cover10"},
      {"dbRateCover5","rate_cover10"},
      {"dbPremiumCover5","premi_cover10"},
      {"stInsuranceCoverID5","ins_cover_id11"},
      {"dbTSICover5","tsi_cover11"},
      {"dbRateCover5","rate_cover11"},
      {"dbPremiumCover5","premi_cover11"},
      {"stInsuranceCoverID5","ins_cover_id12"},
      {"dbTSICover5","tsi_cover12"},
      {"dbRateCover5","rate_cover12"},
      {"dbPremiumCover5","premi_cover12"},
      {"stInsuranceCoverID5","ins_cover_id13"},
      {"dbTSICover5","tsi_cover13"},
      {"dbRateCover5","rate_cover13"},
      {"dbPremiumCover5","premi_cover13"},
      {"stInsuranceCoverID5","ins_cover_id14"},
      {"dbTSICover5","tsi_cover14"},
      {"dbRateCover5","rate_cover14"},
      {"dbPremiumCover5","premi_cover14"},
      {"stInsuranceCoverID5","ins_cover_id15"},
      {"dbTSICover5","tsi_cover15"},
      {"dbRateCover5","rate_cover15"},
      {"dbPremiumCover5","premi_cover15"},
      {"stStatementOfAccountNo","stoa_no"},
      {"dbPremiAmountEdited","premi_amount_e"},
      {"dbTSIAmountEdited","tsi_amount_e"},
      {"dbRICommAmountEdited","ricomm_amt_e"},
      {"stPolicyNo","pol_no*n"},
      {"stPolicyID","pol_id*n"},
      {"stClaimRISlipNo","claim_ri_slip_no"},
      {"stInstallmentFlag","installment_f"},
      {"stInstallmentOption","installment_option"},
      {"stInstallmentCount","installment_count"},
      {"dtBindingDate","binding_date"},
      {"stReinsuranceEntityID","reins_ent_id"},

      {"stInstallmentNumber","installment_no*n"},
      {"dtInstallmentDate","inst_date*n"},
  
   };


   
   private String stInsuranceCoverID1;
   private BigDecimal dbTSICover1;
   private BigDecimal dbRateCover1;
   private BigDecimal dbPremiumCover1;
   
   private String stInsuranceCoverID2;
   private BigDecimal dbTSICover2;
   private BigDecimal dbRateCover2;
   private BigDecimal dbPremiumCover2;
   
   private String stInsuranceCoverID3;
   private BigDecimal dbTSICover3;
   private BigDecimal dbRateCover3;
   private BigDecimal dbPremiumCover3;
   
   private String stInsuranceCoverID4;
   private BigDecimal dbTSICover4;
   private BigDecimal dbRateCover4;
   private BigDecimal dbPremiumCover4;
   
   private String stInsuranceCoverID5;
   private BigDecimal dbTSICover5;
   private BigDecimal dbRateCover5;
   private BigDecimal dbPremiumCover5;
   
   private String stInsuranceCoverID6;
   private BigDecimal dbTSICover6;
   private BigDecimal dbRateCover6;
   private BigDecimal dbPremiumCover6;
   
   private String stInsuranceCoverID7;
   private BigDecimal dbTSICover7;
   private BigDecimal dbRateCover7;
   private BigDecimal dbPremiumCover7;
   
   private String stInsuranceCoverID8;
   private BigDecimal dbTSICover8;
   private BigDecimal dbRateCover8;
   private BigDecimal dbPremiumCover8;
   
   private String stInsuranceCoverID9;
   private BigDecimal dbTSICover9;
   private BigDecimal dbRateCover9;
   private BigDecimal dbPremiumCover9;
   
   private String stInsuranceCoverID10;
   private BigDecimal dbTSICover10;
   private BigDecimal dbRateCover10;
   private BigDecimal dbPremiumCover10;
   
   private String stInsuranceCoverID11;
   private BigDecimal dbTSICover11;
   private BigDecimal dbRateCover11;
   private BigDecimal dbPremiumCover11;
   
   private String stInsuranceCoverID12;
   private BigDecimal dbTSICover12;
   private BigDecimal dbRateCover12;
   private BigDecimal dbPremiumCover12;
   
   private String stInsuranceCoverID13;
   private BigDecimal dbTSICover13;
   private BigDecimal dbRateCover13;
   private BigDecimal dbPremiumCover13;
   
   private String stInsuranceCoverID14;
   private BigDecimal dbTSICover14;
   private BigDecimal dbRateCover14;
   private BigDecimal dbPremiumCover14;
   
   private String stInsuranceCoverID15;
   private BigDecimal dbTSICover15;
   private BigDecimal dbRateCover15;
   private BigDecimal dbPremiumCover15;

   private BigDecimal dbTSIAmount;
   private BigDecimal dbPremiRate;
   private String stAutoRateFlag;
   private String stUseRateFlag;
   private BigDecimal dbRICommRate;
   private BigDecimal dbRICommAmount;
   private String stNotes;
   private String stApprovedFlag;
   private String stRISlipNo;
   private String stControlFlags;
   private BigDecimal dbClaimAmount;

   private String stInsurancePolicyReinsID;
   private String stInsurancePolicyTreatyID;
   private String stInsurancePolicyTreatyDetailID;
   private String stInsuranceTreatyDetailID;
   private String stInsuranceTreatySharesID;
   private String stMemberEntityID;
   private String stORFlag;
   private BigDecimal dbSharePct;
   private BigDecimal dbPremiAmount;
   private HashMap propMap;
   private DTOList coverage;
   private Date dtValidReinsuranceDate;
   private String stStatementOfAccountNo;
   
   private BigDecimal dbPremiAmountEdited;
   private BigDecimal dbTSIAmountEdited;
   private BigDecimal dbRICommAmountEdited;

   private String stPolicyNo;
   private String stPolicyID;
   private String stClaimRISlipNo;

   private DTOList installment;

   private String stInstallmentFlag;
   private String stInstallmentOption;
   private String stInstallmentCount;

   private String stInstallmentNumber;
   private Date dtInstallmentDate;
   private Date dtBindingDate;
   private String stReinsuranceEntityID;

    public String getStReinsuranceEntityID() {
        return stReinsuranceEntityID;
    }

    public void setStReinsuranceEntityID(String stReinsuranceEntityID) {
        this.stReinsuranceEntityID = stReinsuranceEntityID;
    }

    public Date getDtBindingDate() {
        return dtBindingDate;
    }

    public void setDtBindingDate(Date dtBindingDate) {
        this.dtBindingDate = dtBindingDate;
    }

    public Date getDtInstallmentDate() {
        return dtInstallmentDate;
    }

    public void setDtInstallmentDate(Date dtInstallmentDate) {
        this.dtInstallmentDate = dtInstallmentDate;
    }

    public String getStInstallmentNumber() {
        return stInstallmentNumber;
    }

    public void setStInstallmentNumber(String stInstallmentNumber) {
        this.stInstallmentNumber = stInstallmentNumber;
    }

    public String getStInstallmentCount() {
        return stInstallmentCount;
    }

    public void setStInstallmentCount(String stInstallmentCount) {
        this.stInstallmentCount = stInstallmentCount;
    }

    public String getStInstallmentFlag() {
        return stInstallmentFlag;
    }

    public void setStInstallmentFlag(String stInstallmentFlag) {
        this.stInstallmentFlag = stInstallmentFlag;
    }

    public String getStInstallmentOption() {
        return stInstallmentOption;
    }

    public void setStInstallmentOption(String stInstallmentOption) {
        this.stInstallmentOption = stInstallmentOption;
    }

    public boolean isInstallment() {
        return Tools.isYes(stInstallmentFlag);
    }

    public DTOList getInstallment() {
        loadInstallment();
        return installment;
    }

    public void setInstallment(DTOList installment) {
        this.installment = installment;
    }

    private void loadInstallment() {
      try {
         if (installment==null)
            installment = ListUtil.getDTOListFromQuery(
                    " select * from ins_pol_ri_installment where ins_pol_ri_id=?",
                    new Object [] {stInsurancePolicyReinsID},
                    InsurancePolicyReinsInstallmentView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   
   public Date getDtValidReinsuranceDate() {
      return dtValidReinsuranceDate;
   }

   public void setDtValidReinsuranceDate(Date dtValidReinsuranceDate) {
      this.dtValidReinsuranceDate = dtValidReinsuranceDate;
   }
   
   public String getStInsuranceCoverID1() {
      return stInsuranceCoverID1;
   }

   public void setStInsuranceCoverID1(String stInsuranceCoverID1) {
      this.stInsuranceCoverID1 = stInsuranceCoverID1;
   }
   
   public BigDecimal getDbTSICover1() {
      return dbTSICover1;
   }

   public void setDbTSICover1(BigDecimal dbTSICover1) {
      this.dbTSICover1 = dbTSICover1;
   }
   
   public BigDecimal getDbRateCover1() {
      return dbRateCover1;
   }

   public void setDbRateCover1(BigDecimal dbRateCover1) {
      this.dbRateCover1 = dbRateCover1;
   }
   

   public void setDbPremiumCover1(BigDecimal dbPremiumCover1) {
      this.dbPremiumCover1 = dbPremiumCover1;
   }

   public BigDecimal getDbPremiumCover1() {
      return dbPremiumCover1;
   }
   
   //2
      public String getStInsuranceCoverID2() {
      return stInsuranceCoverID2;
   }

   public void setStInsuranceCoverID2(String stInsuranceCoverID2) {
      this.stInsuranceCoverID2 = stInsuranceCoverID2;
   }
   
   public BigDecimal getDbTSICover2() {
      return dbTSICover2;
   }

   public void setDbTSICover2(BigDecimal dbTSICover2) {
      this.dbTSICover2 = dbTSICover2;
   }
   
   public BigDecimal getDbRateCover2() {
      return dbRateCover2;
   }

   public void setDbRateCover2(BigDecimal dbRateCover2) {
      this.dbRateCover2 = dbRateCover2;
   }

   public void setDbPremiumCover2(BigDecimal dbPremiumCover2) {
      this.dbPremiumCover2 = dbPremiumCover2;
   }

   public BigDecimal getDbPremiumCover2() {
      return dbPremiumCover2;
   }
   //end
   
   //3
      public String getStInsuranceCoverID3() {
      return stInsuranceCoverID3;
   }

   public void setStInsuranceCoverID3(String stInsuranceCoverID3) {
      this.stInsuranceCoverID3 = stInsuranceCoverID3;
   }
   
   public BigDecimal getDbTSICover3() {
      return dbTSICover3;
   }

   public void setDbTSICover3(BigDecimal dbTSICover3) {
      this.dbTSICover3 = dbTSICover3;
   }
   
   public BigDecimal getDbRateCover3() {
      return dbRateCover3;
   }

   public void setDbRateCover3(BigDecimal dbRateCover3) {
      this.dbRateCover3 = dbRateCover3;
   }

   public void setDbPremiumCover3(BigDecimal dbPremiumCover3) {
      this.dbPremiumCover3 = dbPremiumCover3;
   }

   public BigDecimal getDbPremiumCover3() {
      return dbPremiumCover3;
   }
   //end
   
   //4
      public String getStInsuranceCoverID4() {
      return stInsuranceCoverID4;
   }

   public void setStInsuranceCoverID4(String stInsuranceCoverID4) {
      this.stInsuranceCoverID4 = stInsuranceCoverID4;
   }
   
   public BigDecimal getDbTSICover4() {
      return dbTSICover4;
   }

   public void setDbTSICover4(BigDecimal dbTSICover4) {
      this.dbTSICover4 = dbTSICover4;
   }
   
   public BigDecimal getDbRateCover4() {
      return dbRateCover4;
   }

   public void setDbRateCover4(BigDecimal dbRateCover4) {
      this.dbRateCover4 = dbRateCover4;
   }

   public void setDbPremiumCover4(BigDecimal dbPremiumCover4) {
      this.dbPremiumCover4 = dbPremiumCover4;
   }

   public BigDecimal getDbPremiumCover4() {
      return dbPremiumCover4;
   }
   //end
   
   //5
      public String getStInsuranceCoverID5() {
      return stInsuranceCoverID5;
   }

   public void setStInsuranceCoverID5(String stInsuranceCoverID5) {
      this.stInsuranceCoverID5 = stInsuranceCoverID5;
   }
   
   public BigDecimal getDbTSICover5() {
      return dbTSICover5;
   }

   public void setDbTSICover5(BigDecimal dbTSICover5) {
      this.dbTSICover5 = dbTSICover5;
   }
   
   public BigDecimal getDbRateCover5() {
      return dbRateCover5;
   }

   public void setDbRateCover5(BigDecimal dbRateCover5) {
      this.dbRateCover5 = dbRateCover5;
   }

   public void setDbPremiumCover5(BigDecimal dbPremiumCover5) {
      this.dbPremiumCover5 = dbPremiumCover5;
   }

   public BigDecimal getDbPremiumCover5() {
      return dbPremiumCover5;
   }
   //end
   
   public String getStInsuranceCoverID6() {
      return stInsuranceCoverID6;
   }

   public void setStInsuranceCoverID6(String stInsuranceCoverID6) {
      this.stInsuranceCoverID6 = stInsuranceCoverID6;
   }
   
   public BigDecimal getDbTSICover6() {
      return dbTSICover6;
   }

   public void setDbTSICover6(BigDecimal dbTSICover6) {
      this.dbTSICover6 = dbTSICover6;
   }
   
   public BigDecimal getDbRateCover6() {
      return dbRateCover6;
   }

   public void setDbRateCover6(BigDecimal dbRateCover6) {
      this.dbRateCover6 = dbRateCover6;
   }
   

   public void setDbPremiumCover6(BigDecimal dbPremiumCover6) {
      this.dbPremiumCover6 = dbPremiumCover6;
   }

   public BigDecimal getDbPremiumCover6() {
      return dbPremiumCover6;
   }
   
   public String getStInsuranceCoverID7() {
      return stInsuranceCoverID7;
   }

   public void setStInsuranceCoverID7(String stInsuranceCoverID7) {
      this.stInsuranceCoverID7 = stInsuranceCoverID7;
   }
   
   public BigDecimal getDbTSICover7() {
      return dbTSICover7;
   }

   public void setDbTSICover7(BigDecimal dbTSICover7) {
      this.dbTSICover7 = dbTSICover7;
   }
   
   public BigDecimal getDbRateCover7() {
      return dbRateCover7;
   }

   public void setDbRateCover7(BigDecimal dbRateCover7) {
      this.dbRateCover7 = dbRateCover7;
   }
   

   public void setDbPremiumCover7(BigDecimal dbPremiumCover7) {
      this.dbPremiumCover7 = dbPremiumCover7;
   }

   public BigDecimal getDbPremiumCover7() {
      return dbPremiumCover7;
   }
   
   public String getStInsuranceCoverID8() {
      return stInsuranceCoverID8;
   }

   public void setStInsuranceCoverID8(String stInsuranceCoverID8) {
      this.stInsuranceCoverID8 = stInsuranceCoverID8;
   }
   
   public BigDecimal getDbTSICover8() {
      return dbTSICover8;
   }

   public void setDbTSICover8(BigDecimal dbTSICover8) {
      this.dbTSICover8 = dbTSICover8;
   }
   
   public BigDecimal getDbRateCover8() {
      return dbRateCover8;
   }

   public void setDbRateCover8(BigDecimal dbRateCover8) {
      this.dbRateCover8 = dbRateCover8;
   }
   

   public void setDbPremiumCover8(BigDecimal dbPremiumCover8) {
      this.dbPremiumCover8 = dbPremiumCover8;
   }

   public BigDecimal getDbPremiumCover8() {
      return dbPremiumCover8;
   }
   
   public String getStInsuranceCoverID9() {
      return stInsuranceCoverID9;
   }

   public void setStInsuranceCoverID9(String stInsuranceCoverID9) {
      this.stInsuranceCoverID9 = stInsuranceCoverID9;
   }
   
   public BigDecimal getDbTSICover9() {
      return dbTSICover9;
   }

   public void setDbTSICover9(BigDecimal dbTSICover9) {
      this.dbTSICover9 = dbTSICover9;
   }
   
   public BigDecimal getDbRateCover9() {
      return dbRateCover9;
   }

   public void setDbRateCover9(BigDecimal dbRateCover9) {
      this.dbRateCover9 = dbRateCover9;
   }
   

   public void setDbPremiumCover9(BigDecimal dbPremiumCover9) {
      this.dbPremiumCover9 = dbPremiumCover9;
   }

   public BigDecimal getDbPremiumCover9() {
      return dbPremiumCover9;
   }
   
   public String getStInsuranceCoverID10() {
      return stInsuranceCoverID10;
   }

   public void setStInsuranceCoverID10(String stInsuranceCoverID10) {
      this.stInsuranceCoverID10 = stInsuranceCoverID10;
   }
   
   public BigDecimal getDbTSICover10() {
      return dbTSICover10;
   }

   public void setDbTSICover10(BigDecimal dbTSICover10) {
      this.dbTSICover10 = dbTSICover10;
   }
   
   public BigDecimal getDbRateCover10() {
      return dbRateCover10;
   }

   public void setDbRateCover10(BigDecimal dbRateCover10) {
      this.dbRateCover10 = dbRateCover10;
   }
   

   public void setDbPremiumCover10(BigDecimal dbPremiumCover10) {
      this.dbPremiumCover10 = dbPremiumCover10;
   }

   public BigDecimal getDbPremiumCover10() {
      return dbPremiumCover10;
   }
   
   public String getStInsuranceCoverID11() {
      return stInsuranceCoverID11;
   }

   public void setStInsuranceCoverID11(String stInsuranceCoverID11) {
      this.stInsuranceCoverID11 = stInsuranceCoverID11;
   }
   
   public BigDecimal getDbTSICover11() {
      return dbTSICover11;
   }

   public void setDbTSICover11(BigDecimal dbTSICover11) {
      this.dbTSICover11 = dbTSICover11;
   }
   
   public BigDecimal getDbRateCover11() {
      return dbRateCover11;
   }

   public void setDbRateCover11(BigDecimal dbRateCover11) {
      this.dbRateCover11 = dbRateCover11;
   }
   

   public void setDbPremiumCover11(BigDecimal dbPremiumCover11) {
      this.dbPremiumCover11 = dbPremiumCover11;
   }

   public BigDecimal getDbPremiumCover11() {
      return dbPremiumCover11;
   }
   
   public String getStInsuranceCoverID12() {
      return stInsuranceCoverID12;
   }

   public void setStInsuranceCoverID12(String stInsuranceCoverID12) {
      this.stInsuranceCoverID12 = stInsuranceCoverID12;
   }
   
   public BigDecimal getDbTSICover12() {
      return dbTSICover12;
   }

   public void setDbTSICover12(BigDecimal dbTSICover12) {
      this.dbTSICover12 = dbTSICover12;
   }
   
   public BigDecimal getDbRateCover12() {
      return dbRateCover12;
   }

   public void setDbRateCover12(BigDecimal dbRateCover12) {
      this.dbRateCover12 = dbRateCover12;
   }
   

   public void setDbPremiumCover12(BigDecimal dbPremiumCover12) {
      this.dbPremiumCover12 = dbPremiumCover12;
   }

   public BigDecimal getDbPremiumCover12() {
      return dbPremiumCover12;
   }
   
   public String getStInsuranceCoverID13() {
      return stInsuranceCoverID13;
   }

   public void setStInsuranceCoverID13(String stInsuranceCoverID13) {
      this.stInsuranceCoverID13 = stInsuranceCoverID13;
   }
   
   public BigDecimal getDbTSICover13() {
      return dbTSICover13;
   }

   public void setDbTSICover13(BigDecimal dbTSICover13) {
      this.dbTSICover13 = dbTSICover13;
   }
   
   public BigDecimal getDbRateCover13() {
      return dbRateCover13;
   }

   public void setDbRateCover13(BigDecimal dbRateCover13) {
      this.dbRateCover13 = dbRateCover13;
   }
   

   public void setDbPremiumCover13(BigDecimal dbPremiumCover13) {
      this.dbPremiumCover13 = dbPremiumCover13;
   }

   public BigDecimal getDbPremiumCover13() {
      return dbPremiumCover13;
   }
   
   public String getStInsuranceCoverID14() {
      return stInsuranceCoverID14;
   }

   public void setStInsuranceCoverID14(String stInsuranceCoverID14) {
      this.stInsuranceCoverID14 = stInsuranceCoverID14;
   }
   
   public BigDecimal getDbTSICover14() {
      return dbTSICover14;
   }

   public void setDbTSICover14(BigDecimal dbTSICover14) {
      this.dbTSICover14 = dbTSICover14;
   }
   
   public BigDecimal getDbRateCover14() {
      return dbRateCover14;
   }

   public void setDbRateCover14(BigDecimal dbRateCover14) {
      this.dbRateCover14 = dbRateCover14;
   }
   

   public void setDbPremiumCover14(BigDecimal dbPremiumCover14) {
      this.dbPremiumCover14 = dbPremiumCover14;
   }

   public BigDecimal getDbPremiumCover14() {
      return dbPremiumCover14;
   }
   
   public String getStInsuranceCoverID15() {
      return stInsuranceCoverID15;
   }

   public void setStInsuranceCoverID15(String stInsuranceCoverID15) {
      this.stInsuranceCoverID15 = stInsuranceCoverID15;
   }
   
   public BigDecimal getDbTSICover15() {
      return dbTSICover15;
   }

   public void setDbTSICover15(BigDecimal dbTSICover15) {
      this.dbTSICover15 = dbTSICover15;
   }
   
   public BigDecimal getDbRateCover15() {
      return dbRateCover15;
   }

   public void setDbRateCover15(BigDecimal dbRateCover15) {
      this.dbRateCover15 = dbRateCover15;
   }
   

   public void setDbPremiumCover15(BigDecimal dbPremiumCover15) {
      this.dbPremiumCover15 = dbPremiumCover15;
   }

   public BigDecimal getDbPremiumCover15() {
      return dbPremiumCover15;
   }

   public void setDbTSIAmount(BigDecimal dbTSIAmount) {
      this.dbTSIAmount = dbTSIAmount;
   }
   
    public BigDecimal getDbTSIAmount() {
      return dbTSIAmount;
   }

   public BigDecimal getDbPremiRate() {
      return dbPremiRate;
   }

   public void setDbPremiRate(BigDecimal dbPremiRate) {
      this.dbPremiRate = dbPremiRate;
   }

   public String getStAutoRateFlag() {
      return stAutoRateFlag;
   }

   public void setStAutoRateFlag(String stAutoRateFlag) {
      this.stAutoRateFlag = stAutoRateFlag;
   }

   public String getStUseRateFlag() {
      return stUseRateFlag;
   }

   public void setStUseRateFlag(String stUseRateFlag) {
      this.stUseRateFlag = stUseRateFlag;
   }

   public BigDecimal getDbRICommRate() {
      return dbRICommRate;
   }

   public void setDbRICommRate(BigDecimal dbRICommRate) {
      this.dbRICommRate = dbRICommRate;
   }

   public BigDecimal getDbRICommAmount() {
      return dbRICommAmount;
   }

   public void setDbRICommAmount(BigDecimal dbRICommAmount) {
      this.dbRICommAmount = dbRICommAmount;
   }

   public String getStNotes() {
      return stNotes;
   }

   public void setStNotes(String stNotes) {
      this.stNotes = stNotes;
   }

   public String getStORFlag() {
      return stORFlag;
   }

   public void setStORFlag(String stORFlag) {
      this.stORFlag = stORFlag;
   }

   public String getStInsurancePolicyTreatyDetailID() {
      return stInsurancePolicyTreatyDetailID;
   }

   public void setStInsurancePolicyTreatyDetailID(String stInsurancePolicyTreatyDetailID) {
      this.stInsurancePolicyTreatyDetailID = stInsurancePolicyTreatyDetailID;
   }

   public String getStInsurancePolicyReinsID() {
      return stInsurancePolicyReinsID;
   }

   public void setStInsurancePolicyReinsID(String stInsurancePolicyReinsID) {
      this.stInsurancePolicyReinsID = stInsurancePolicyReinsID;
   }

   public String getStInsurancePolicyTreatyID() {
      return stInsurancePolicyTreatyID;
   }

   public void setStInsurancePolicyTreatyID(String stInsurancePolicyTreatyID) {
      this.stInsurancePolicyTreatyID = stInsurancePolicyTreatyID;
   }

   public String getStInsuranceTreatySharesID() {
      return stInsuranceTreatySharesID;
   }

   public void setStInsuranceTreatySharesID(String stInsuranceTreatySharesID) {
      this.stInsuranceTreatySharesID = stInsuranceTreatySharesID;
   }

   public String getStMemberEntityID() {
      return stMemberEntityID;
   }

   public void setStMemberEntityID(String stMemberEntityID) {
      this.stMemberEntityID = stMemberEntityID;
   }

   public BigDecimal getDbSharePct() {
      return dbSharePct;
   }

   public void setDbSharePct(BigDecimal dbSharePct) {
      this.dbSharePct = dbSharePct;
   }

   public BigDecimal getDbPremiAmount() {
      return dbPremiAmount;
   }

   public void setDbPremiAmount(BigDecimal dbPremiAmount) {
      this.dbPremiAmount = dbPremiAmount;
   }

   public String getStInsuranceTreatyDetailID() {
      return stInsuranceTreatyDetailID;
   }

   public void setStInsuranceTreatyDetailID(String stInsuranceTreatyDetailID) {
      this.stInsuranceTreatyDetailID = stInsuranceTreatyDetailID;
   }

   public boolean isUseRate() {
      return Tools.isYes(stUseRateFlag);
   }

   public boolean isAutoRate() {
      return Tools.isYes(stAutoRateFlag);
   }

   public EntityView getEntity() {
      return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stMemberEntityID);
   }

   public BigDecimal getDbPremiNet() {
      return BDUtil.sub(getDbPremiAmount(), getDbRICommAmount());
   }

   public String getStRISlipNo() {
      return stRISlipNo;
   }

   public void setStRISlipNo(String stRISlipNo) {
      this.stRISlipNo = stRISlipNo;
   }

   public String getStControlFlags() {
      return stControlFlags;
   }

   public void setStControlFlags(String stControlFlags) {
      this.stControlFlags = stControlFlags;
   }

   public InsurancePolicyReinsView() {
   }

   public HashMap getControlMap() {
      if (propMap==null)
         propMap = Tools.getPropMap(stControlFlags);
      return propMap;
   }

   public BigDecimal getDbClaimAmount() {
      return dbClaimAmount;
   }

   public void setDbClaimAmount(BigDecimal dbClaimAmount) {
      this.dbClaimAmount = dbClaimAmount;
   }

   public String getStApprovedFlag() {
      return stApprovedFlag;
   }

   public void setStApprovedFlag(String stApprovedFlag) {
      this.stApprovedFlag = stApprovedFlag;
   }

   public boolean isApproved() {
      return Tools.isYes(stApprovedFlag);
   }
   
   public DTOList getCoverage() {
      loadCoverage();
      return coverage;
   }
   
   public void setCoverage(DTOList coverage){
   	  this.coverage = coverage;
   }
      

   private void loadCoverage() {
      try {
         if (coverage==null)
            coverage = ListUtil.getDTOListFromQuery(
                    " select * from ins_pol_cover_ri where ins_pol_ri_id=?",
                    new Object [] {stInsurancePolicyReinsID},
                    InsurancePolicyCoverReinsView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   private InsuranceTreatyDetailView treatyDetail;
   
    public InsuranceTreatyDetailView getTreatyDetail() {
        if (treatyDetail==null) treatyDetail = (InsuranceTreatyDetailView) DTOPool.getInstance().getDTO(InsuranceTreatyDetailView.class, stInsuranceTreatyDetailID);
        return treatyDetail;
    }

    public String getStStatementOfAccountNo()
    {
        return stStatementOfAccountNo;
    }

    public void setStStatementOfAccountNo(String stStatementOfAccountNo)
    {
        this.stStatementOfAccountNo = stStatementOfAccountNo;
    }
    
    public InsuranceCoverView getCoverageView() {
        return (InsuranceCoverView) DTOPool.getInstance().getDTO(InsuranceCoverView.class, stInsuranceCoverID1);
    }

    public BigDecimal getDbPremiAmountEdited() {
        return dbPremiAmountEdited;
    }

    public void setDbPremiAmountEdited(BigDecimal dbPremiAmountEdited) {
        this.dbPremiAmountEdited = dbPremiAmountEdited;
    }

    public BigDecimal getDbTSIAmountEdited() {
        return dbTSIAmountEdited;
    }

    public void setDbTSIAmountEdited(BigDecimal dbTSIAmountEdited) {
        this.dbTSIAmountEdited = dbTSIAmountEdited;
    }

    public BigDecimal getDbRICommAmountEdited() {
        return dbRICommAmountEdited;
    }

    public void setDbRICommAmountEdited(BigDecimal dbRICommAmountEdited) {
        this.dbRICommAmountEdited = dbRICommAmountEdited;
    }

    public InsuranceTreatySharesView getTreatyShares() {
        return (InsuranceTreatySharesView) DTOPool.getInstance().getDTO(InsuranceTreatySharesView.class, stInsuranceTreatySharesID);
    }

    /**
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
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
     * @return the stClaimRISlipNo
     */
    public String getStClaimRISlipNo() {
        return stClaimRISlipNo;
    }

    /**
     * @param stClaimRISlipNo the stClaimRISlipNo to set
     */
    public void setStClaimRISlipNo(String stClaimRISlipNo) {
        this.stClaimRISlipNo = stClaimRISlipNo;
    }
    
    public InsurancePeriodView getInstallmentPeriod() {
        return (InsurancePeriodView) DTOPool.getInstance().getDTO(InsurancePeriodView.class, "4");
    }

    public void reCalculateInstallment(Date dtPolicyDate) throws Exception {
        getInstallment();

        if (installment == null) {
            installment = new DTOList();
        }

        if(!isInstallment()){
            if (installment != null) deleteReinsInstallmentAll();
            return;
        }
 
        final InsurancePeriodView instPeriod = getInstallmentPeriod();

        final BigDecimal periodPremiAmount = BDUtil.div(getDbPremiAmount(), new BigDecimal(installment.size()));
        final BigDecimal periodRICommAmount = BDUtil.div(getDbRICommAmount(), new BigDecimal(installment.size()));

        final BigDecimal roundingErrPremi = BDUtil.sub(getDbPremiAmount(), BDUtil.mul(periodPremiAmount, new BigDecimal(installment.size())));
        final BigDecimal roundingErrRIComm = BDUtil.sub(getDbRICommAmount(), BDUtil.mul(periodRICommAmount, new BigDecimal(installment.size())));

        Date perDate = dtPolicyDate;

        if (perDate == null) {
            return;
        }

        BigDecimal total = null;
        for (int i = 0; i < installment.size(); i++) {
            InsurancePolicyReinsInstallmentView inst = (InsurancePolicyReinsInstallmentView) installment.get(i);

            inst.setStInstallmentNumber(String.valueOf(i+1));
            
            if (!inst.isManualCicilan()){
                inst.setDbPremiAmount(periodPremiAmount);
                inst.setDbRICommAmount(periodRICommAmount);
                inst.setDtDueDate(perDate);

                if (i == 0) {
                    inst.setDbPremiAmount(BDUtil.add(inst.getDbPremiAmount(), roundingErrPremi));
                    inst.setDbRICommAmount(BDUtil.add(inst.getDbRICommAmount(), roundingErrRIComm));
                }

                if (instPeriod != null) {
                    perDate = instPeriod.advance(perDate);
                }
            }

            inst.setDbAmount(BDUtil.sub(inst.getDbPremiAmount(), inst.getDbRICommAmount()));

            total = BDUtil.add(total, inst.getDbAmount());
        }

    }

    public void deleteReinsInstallmentAll() {
        getInstallment().deleteAll();
    }

}
