/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyObjDefaultView
 * Author:  Denny Mahendra
 * Created: Feb 5, 2006 11:51:50 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.acceptance.model;

import com.webfin.insurance.model.*;
import com.crux.util.Tools;
import com.crux.util.DTOList;
import com.crux.pool.DTOPool;
import com.webfin.entity.model.EntityView;

import java.util.Date;
import java.math.BigDecimal;

public class AcceptanceObjDefaultView extends AcceptanceObjectView {
   /*
   CREATE TABLE ins_pol_obj
(
  ins_pol_obj_id int8 NOT NULL,
  period_start timestamp,
  period_end timestamp,
  pol_id int8,
  ins_risk_cat_id int8,
  insured_amount numeric,
  premi_rate numeric,
  premi_amount numeric,
  premi_total numeric,
  policy_no varchar(64),
  description   varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_obj_pk PRIMARY KEY (ins_pol_obj_id)
) without oids;

ALTER TABLE ins_pol_obj ADD COLUMN risk_location varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN zipcode varchar(16);
ALTER TABLE ins_pol_obj ADD COLUMN riskcardno varchar(32);
ALTER TABLE ins_pol_obj ADD COLUMN riskcode varchar(32);

ALTER TABLE ins_pol_obj ADD COLUMN ins_pol_obj_ref_root_id int8;


   */

   public static String tableName = "ins_pol_acceptance_obj";

   public static String fieldMap[][] = {
      {"stPolicyObjectID","ins_pol_obj_id*pk*nd"},
      {"stPolicyObjectRefID","ins_pol_obj_ref_id"},
      {"stPolicyObjectRefRootID","ins_pol_obj_ref_root_id"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"stObjectDescription","description"},
      //{"stObjectDescription2","description2"},
      {"stVoidFlag","void_flag"},

      {"stReference1Desc","ref1d"},
      {"stReference2Desc","ref2d"},
      {"stReference3Desc","ref3d"},
      {"stReference4Desc","ref4d"},
      {"stReference5Desc","ref5d"},
      {"stReference6Desc","ref6d"},
      {"stReference7Desc","ref7d"},
      {"stReference8Desc","ref8d"},
      {"stReference9Desc","ref9d"},

      {"stReference1","ref1"},
      {"stReference2","ref2"},
      {"stReference3","ref3"},
      {"stReference4","ref4"},
      {"stReference5","ref5"},
      {"stReference6","ref6"},
      {"stReference7","ref7"},
      {"stReference8","ref8"},
      {"stReference9","ref9"},
      {"stReference10","ref10"},
      {"stReference11","ref11"},
      {"stReference12","ref12"},
      {"stReference13","ref13"},
      {"stReference14","ref14"},
      {"stReference15","ref15"},
      {"stReference16","ref16"},
      {"stReference17","ref17"},
      {"stReference18","ref18"},
      {"stReference19","ref19"},
      {"stReference20","ref20"},
      {"stReference21","ref21"},
      {"stReference22","ref22"},
      {"stReference23","ref23"},
      {"stReference24","ref24"},
      {"stReference25","ref25"},
      {"stReference26","ref26"},
      {"stReference27","ref27"},
      {"stReference28","ref28"},
      {"stReference29","ref29"},
      {"stReference30","ref30"},
      {"stReference31","ref31"},
      {"stReference32","ref32"},
      {"stReference33","ref33"},
      {"stReference34","ref34"},
      {"stReference35","ref35"},
      {"stReference36","ref36"},
      {"stReference37","ref37"},
      {"stReference38","ref38"},
      {"stReference39","ref39"},
      {"stReference40","ref40"},
      {"stReference41","ref41"},
      {"stReference42","ref42"},
      {"stReference43","ref43"},
      {"stReference44","ref44"},
      {"stReference45","ref45"},
      {"stReference46","ref46"},
      {"stReference47","ref47"},
      {"stReference48","ref48"},
      {"stReference49","ref49"},
      {"stReference50","ref50"},
      {"stReference51","ref51"},
      {"stReference52","ref52"},
      {"stReference53","ref53"},
      {"stReference54","ref54"},
      {"stReference55","ref55"},
      {"stReference56","ref56"},
      {"stReference57","ref57"},
      {"stReference58","ref58"},
      {"stReference59","ref59"},
      {"stReference60","ref60"},
      {"stReference61","ref61"},
      {"stReference62","ref62"},
      {"stReference63","ref63"},
      {"stReference64","ref64"},
      {"stReference65","ref65"},
      {"stReference66","ref66"},
      {"stReference67","ref67"},
      {"stReference68","ref68"},
      {"stReference69","ref69"},
      {"stReference70","ref70"},
      {"stReference71","ref71"},
      {"stReference72","ref72"},
      {"stReference73","ref73"},
      {"stReference74","ref74"},
      {"stReference75","ref75"},
      {"stReference76","ref76"},
      {"stReference77","ref77"},
      {"stReference78","ref78"},
      {"stReference79","ref79"},
      {"stReference80","ref80"},

      {"dtReference1","refd1"},
      {"dtReference2","refd2"},
      {"dtReference3","refd3"},
      {"dtReference4","refd4"},
      {"dtReference5","refd5"},
      {"dtReference6","refd6"},
      {"dtReference7","refd7"},

      {"dbReference1","refn1"},
      {"dbReference2","refn2"},
      {"dbReference3","refn3"},
      {"dbReference4","refn4"},
      {"dbReference5","refn5"},
      {"dbReference6","refn6"},
      {"dbReference7","refn7"},
      {"dbReference8","refn8"},
      {"dbReference9","refn9"},
      {"dbReference10","refn10"},
      {"dbReference11","refn11"},
      {"dbReference12","refn12"},
      {"dbReference13","refn13"},
      {"dbReference14","refn14"},
      {"dbReference15","refn15"},
      {"dbReference16","refn16"},
      {"dbReference17","refn17"},
      {"dbReference18","refn18"},
      {"dbReference19","refn19"},
      {"dbReference20","refn20"},
      {"dbReference21","refn21"},
      {"dbReference22","refn22"},
      {"dbReference23","refn23"},
      {"dbReference24","refn24"},
      {"dbReference25","refn25"},
      {"dbReference26","refn26"},
      {"dbReference27","refn27"},
      {"dbReference28","refn28"},
      {"dbReference29","refn29"},
      {"dbReference30","refn30"},


      {"dbObjectInsuredAmount","insured_amount"},
      {"dbObjectPremiRate","premi_rate"},
      {"dbObjectPremiAmount","premi_amount"},
      {"dbObjectPremiTotalAmount","premi_total"},
      {"stSubPolicyNo","policy_no"},
      {"stPolicyID","pol_id"},
      {"stRiskCategoryID","ins_risk_cat_id"},
      {"stRiskClass","risk_class"},

      {"stRiskLocation","risk_location"},
      {"stZipCode","zipcode"},
      {"stRiskCardNo","riskcardno"},
      {"stRiskCode","riskcode"},
      {"stPolicyNo","policy_no"},
      {"dbObjectPremiSettled","premi_settled"},
      {"stPremiSettledFlag","premi_settled_flag"},
      {"stInsuranceTreatyID","ins_treaty_id"},
      {"stClaimLossID","claim_loss_id"},
      {"dbObjectPremiTotalBeforeCoinsuranceAmount","premi_total_bcoins"},
      {"stPolisNo","pol_no*n"},
      {"stPolicyObjectPolicyRootID","ins_pol_obj_policy_root_id"},
      {"stOrderNo","order_no"},
      {"stRekapKreasi","rekap_kreasi"},
      
      {"stRiskCategoryID1","ins_risk_cat_id1"},
      {"stRiskCategoryID2","ins_risk_cat_id2"},
      {"stRiskCategoryID3","ins_risk_cat_id3"},
      {"stRiskCategoryCode1","ins_risk_cat_code1"},
      {"stRiskCategoryCode2","ins_risk_cat_code2"},
      {"stRiskCategoryCode3","ins_risk_cat_code3"},

      {"dbPeriodRateBefore", "period_rate_before"},
      {"stPeriodBaseBeforeID", "ins_period_base_b4"},
      {"dbPeriodRate", "period_rate"},
      {"stPeriodBaseID", "ins_period_base_id"},
      {"stPremiumFactorID", "ins_premium_factor_id"},
      {"stPeriodFactorObjectFlag", "period_factor_obj_flag"},
      {"dbLimitOfLiability", "limit_of_liability"},
      {"stDataID", "data_id"},
      {"dbLimitOfLiability2", "limit_of_liability2"},
      {"stRiskNotes", "risk_notes"},
      {"stRiskApproved", "risk_approved_f"},
      {"stRiskFile", "risk_file"},

//      {"stValidasiCabang", "validasi_cabang_f"},
//      {"stValidasiCabangInduk", "validasi_induk_f"},
//      {"stValidasiKantorPusat", "validasi_headoffice_f"},

   };

   /*
ALTER TABLE ins_pol_obj ADD COLUMN refn9 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn10 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn11 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn12 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn13 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn14 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn15 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn16 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn17 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn18 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn19 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn20 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn21 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn22 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn23 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn24 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn25 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn26 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn27 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn28 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn29 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn30 numeric;

ALTER TABLE ins_pol_obj ADD COLUMN risk_location varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN zipcode varchar(16);
ALTER TABLE ins_pol_obj ADD COLUMN riskcardno varchar(32);
ALTER TABLE ins_pol_obj ADD COLUMN riskcode varchar(32);

   */
   private String stInsuranceTreatyID;
   private String stReference1Desc;
   private String stReference2Desc;
   private String stReference3Desc;
   private String stReference4Desc;
   private String stReference5Desc;
   private String stReference6Desc;
   private String stReference7Desc;
   private String stReference8Desc;
   private String stReference9Desc;

   private String stRiskLocation;
   private String stZipCode;
   private String stRiskCardNo;
   private String stRiskCode;
   private String stPolicyObjectRefRootID;
   private String stPolicyNo;
   private String stClaimLossID;
   
   private String stPremiSettledFlag;
   
   private BigDecimal dbObjectPremiSettled;
   private BigDecimal dbObjectPremiTotalBeforeCoinsuranceAmount;
   
   private String stPolisNo;
   private String stOrderNo;
   private String stRekapKreasi;
   private String stObjectDescription2;

   private String stRiskCategoryID1;
   private String stRiskCategoryID2;
   private String stRiskCategoryID3;
   private String stRiskCategoryCode1;
   private String stRiskCategoryCode2;
   private String stRiskCategoryCode3;

   private BigDecimal dbPeriodRateBefore;
   private String stPeriodBaseBeforeID;
   private BigDecimal dbPeriodRate;
   private String stPeriodBaseID;
   private String stPremiumFactorID;
   private String stPeriodFactorObjectFlag;
   private BigDecimal dbLimitOfLiability;
   private BigDecimal dbLimitOfLiability2;
   private String stDataID;
   private String stRiskNotes;
   private String stRiskApproved;
   private String stRiskFile;

   private String stValidasiCabang;
   private String stValidasiCabangInduk;
   private String stValidasiKantorPusat;
   private String stChecking;
   
   public String getStInsuranceTreatyID() {
      return stInsuranceTreatyID;
   }

   public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
      this.stInsuranceTreatyID = stInsuranceTreatyID;
   }
   
   public String getStPremiSettledFlag() {
      return stPremiSettledFlag;
   }

   public void setStPremiSettledFlag(String stPremiSettledFlag) {
      this.stPremiSettledFlag = stPremiSettledFlag;
   }

   public boolean isPremiSettled() {
      return Tools.isYes(stPremiSettledFlag);
   }
   
   public String getStPolicyNo() {
      return stPolicyNo;
   }

   public void setStPolicyNo(String stPolicyNo) {
      this.stPolicyNo = stPolicyNo;
   }
   
   public String getStPolicyObjectRefRootID() {
      return stPolicyObjectRefRootID;
   }

   public void setStPolicyObjectRefRootID(String stPolicyObjectRefRootID) {
      this.stPolicyObjectRefRootID = stPolicyObjectRefRootID;
   }


   public String getStRiskLocation() {
      return stRiskLocation;
   }

   public void setStRiskLocation(String stRiskLocation) {
      this.stRiskLocation = stRiskLocation;
   }

   public String getStZipCode() {
      return stZipCode;
   }

   public void setStZipCode(String stZipCode) {
      this.stZipCode = stZipCode;
   }

   public String getStRiskCardNo() {
      return stRiskCardNo;
   }

   public void setStRiskCardNo(String stRiskCardNo) {
      this.stRiskCardNo = stRiskCardNo;
   }

   public String getStRiskCode() {
      return stRiskCode;
   }

   public void setStRiskCode(String stRiskCode) {
      this.stRiskCode = stRiskCode;
   }

   public static String comboFields[] = {"index","description"};

   private String stPolicyObjectRefID;
   private String stPolicyObjectID;
   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private String stPolicyObjectPolicyRootID;

   private BigDecimal dbObjectInsuredAmount;
   private BigDecimal dbObjectPremiRate;
   private BigDecimal dbObjectPremiRate2;
   private BigDecimal dbObjectPremiAmount;
   private BigDecimal dbObjectPremiTotalAmount;
   private String stSubPolicyNo;
   private String stVoidFlag;
   private String stObjectDescription;
   private String stPolicyID;
   private String stRiskCategoryID;
   private String stRiskClass;
   private String stReference1;
   private String stReference2;
   private String stReference3;
   private String stReference4;
   private String stReference5;
   private String stReference6;
   private String stReference7;
   private String stReference8;
   private String stReference9;
   private String stReference10;
   private String stReference11;
   private String stReference12;
   private String stReference13;
   private String stReference14;
   private String stReference15;
   private String stReference16;
   private String stReference17;
   private String stReference18;
   private String stReference19;
   private String stReference20;
   private String stReference21;
   private String stReference22;
   private String stReference23;
   private String stReference24;
   private String stReference25;
   private String stReference26;
   private String stReference27;
   private String stReference28;
   private String stReference29;
   private String stReference30;
   private String stReference31;
   private String stReference32;
   private String stReference33;
   private String stReference34;
   private String stReference35;
   private String stReference36;
   private String stReference37;
   private String stReference38;
   private String stReference39;
   private String stReference40;
   private String stReference41;
   private String stReference42;
   private String stReference43;
   private String stReference44;
   private String stReference45;
   private String stReference46;
   private String stReference47;
   private String stReference48;
   private String stReference49;
   private String stReference50;
   private String stReference51;
   private String stReference52;
   private String stReference53;
   private String stReference54;
   private String stReference55;
   private String stReference56;
   private String stReference57;
   private String stReference58;
   private String stReference59;
   private String stReference60;
   private String stReference61;
   private String stReference62;
   private String stReference63;
   private String stReference64;
   private String stReference65;
   private String stReference66;
   private String stReference67;
   private String stReference68;
   private String stReference69;
   private String stReference70;
   private String stReference71;
   private String stReference72;
   private String stReference73;
   private String stReference74;
   private String stReference75;
   private String stReference76;
   private String stReference77;
   private String stReference78;
   private String stReference79;
   private String stReference80;

   private Date dtReference1;
   private Date dtReference2;
   private Date dtReference3;
   private Date dtReference4;
   private Date dtReference5;
   
   private Date dtReference6;
   private Date dtReference7;

   private BigDecimal dbReference1;
   private BigDecimal dbReference2;
   private BigDecimal dbReference3;
   private BigDecimal dbReference4;
   private BigDecimal dbReference5;
   private BigDecimal dbReference6;
   private BigDecimal dbReference7;
   private BigDecimal dbReference8;
   private BigDecimal dbReference9;
   private BigDecimal dbReference10;
   private BigDecimal dbReference11;
   private BigDecimal dbReference12;
   private BigDecimal dbReference13;
   private BigDecimal dbReference14;
   private BigDecimal dbReference15;
   private BigDecimal dbReference16;
   private BigDecimal dbReference17;
   private BigDecimal dbReference18;
   private BigDecimal dbReference19;
   private BigDecimal dbReference20;
   private BigDecimal dbReference21;
   private BigDecimal dbReference22;
   private BigDecimal dbReference23;
   private BigDecimal dbReference24;
   private BigDecimal dbReference25;
   private BigDecimal dbReference26;
   private BigDecimal dbReference27;
   private BigDecimal dbReference28;
   private BigDecimal dbReference29;
   private BigDecimal dbReference30;
   
   private EntityView entity;
   
   /*
   public String getStObjectDescription2() {
      final String mapref1 = getPolicy().getPolicyType().getObjectMap().getStReference1();

      if (mapref1==null) return null;

      stObjectDescription2 = (String) getProperty(mapref1);

      return stObjectDescription2;
   }

   public void setStObjectDescription2(String stObjectDescription2) {
      this.stObjectDescription2 = stObjectDescription2;
   }*/

   public String getStRiskClass() {
      return stRiskClass;
   }

   public void setStRiskClass(String stRiskClass) {                     
      this.stRiskClass = stRiskClass;
   }

   public String getStPolicyObjectID() {
      return stPolicyObjectID;
   }

   public void setStPolicyObjectID(String stPolicyObjectID) {
      this.stPolicyObjectID = stPolicyObjectID;
   }

   public Date getDtPeriodStart() {
      return dtPeriodStart;
   }

   public void setDtPeriodStart(Date dtPeriodStart) {
      this.dtPeriodStart = dtPeriodStart;
   }

   public Date getDtPeriodEnd() {
      return dtPeriodEnd;
   }

   public void setDtPeriodEnd(Date dtPeriodEnd) {
      this.dtPeriodEnd = dtPeriodEnd;
   }

   public BigDecimal getDbObjectInsuredAmount() {
      return dbObjectInsuredAmount;
   }

   public void setDbObjectInsuredAmount(BigDecimal dbObjectInsuredAmount) {
      this.dbObjectInsuredAmount = dbObjectInsuredAmount;
   }

   public BigDecimal getDbObjectPremiRate() {
      return dbObjectPremiRate;
   }

   public void setDbObjectPremiRate(BigDecimal dbObjectPremiRate) {
      this.dbObjectPremiRate = dbObjectPremiRate;
   }
   
   public void setDbObjectPremiRate2(BigDecimal dbObjectPremiRate2) {
      this.dbObjectPremiRate2 = dbObjectPremiRate2;
   }

   public BigDecimal getDbObjectPremiAmount() {
      return dbObjectPremiAmount;
   }

   public void setDbObjectPremiAmount(BigDecimal dbObjectPremiAmount) {
      this.dbObjectPremiAmount = dbObjectPremiAmount;
   }

   public BigDecimal getDbObjectPremiTotalAmount() {
      return dbObjectPremiTotalAmount;
   }

   public void setDbObjectPremiTotalAmount(BigDecimal dbObjectPremiTotalAmount) {
      this.dbObjectPremiTotalAmount = dbObjectPremiTotalAmount;
   }
   
   public BigDecimal getDbObjectPremiSettled() {
      return dbObjectPremiSettled;
   }

   public void setDbObjectPremiSettled(BigDecimal dbObjectPremiSettled) {
      this.dbObjectPremiSettled = dbObjectPremiSettled;
   }

   public String getStSubPolicyNo() {
      return stSubPolicyNo;
   }

   public void setStSubPolicyNo(String stSubPolicyNo) {
      this.stSubPolicyNo = stSubPolicyNo;
   }

   public String getStObjectDescription() {
      try {
         stObjectDescription = (String) getProperty(getPolicy().getPolicyType().getObjectMap().getStDescField());
      } catch (Exception e) {
         stObjectDescription = null;
      }
      
      if(getStOrderNo()!=null) stObjectDescription = stOrderNo + ". " + stObjectDescription;

      if (isVoid()) stObjectDescription+=" (VOID)";

      return stObjectDescription;
   }

   public void setStObjectDescription(String stObjectDescription) {
      this.stObjectDescription = stObjectDescription;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStRiskCategoryID() {
      return stRiskCategoryID;
   }

   public void setStRiskCategoryID(String stRiskCategoryID) {
      this.stRiskCategoryID = stRiskCategoryID;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStReference2() {
      return stReference2;
   }

   public void setStReference2(String stReference2) {
      this.stReference2 = stReference2;
   }

   public String getStReference3() {
      return stReference3;
   }

   public void setStReference3(String stReference3) {
      this.stReference3 = stReference3;
   }

   public String getStReference4() {
      return stReference4;
   }

   public void setStReference4(String stReference4) {
      this.stReference4 = stReference4;
   }

   public String getStReference5() {
      return stReference5;
   }

   public void setStReference5(String stReference5) {
      this.stReference5 = stReference5;
   }

   public String getStReference6() {
      return stReference6;
   }

   public void setStReference6(String stReference6) {
      this.stReference6 = stReference6;
   }

   public String getStReference7() {
      return stReference7;
   }

   public void setStReference7(String stReference7) {
      this.stReference7 = stReference7;
   }

   public String getStReference8() {
      return stReference8;
   }

   public void setStReference8(String stReference8) {
      this.stReference8 = stReference8;
   }

   public String getStReference9() {
      return stReference9;
   }

   public void setStReference9(String stReference9) {
      this.stReference9 = stReference9;
   }

   public String getStReference10() {
      return stReference10;
   }

   public void setStReference10(String stReference10) {
      this.stReference10 = stReference10;
   }

   public Date getDtReference1() {
      return dtReference1;
   }

   public void setDtReference1(Date dtReference1) {
      this.dtReference1 = dtReference1;
   }

   public Date getDtReference2() {
      return dtReference2;
   }

   public void setDtReference2(Date dtReference2) {
      this.dtReference2 = dtReference2;
   }

   public Date getDtReference3() {
      return dtReference3;
   }

   public void setDtReference3(Date dtReference3) {
      this.dtReference3 = dtReference3;
   }

   public Date getDtReference4() {
      return dtReference4;
   }

   public void setDtReference4(Date dtReference4) {
      this.dtReference4 = dtReference4;
   }

   public BigDecimal getDbReference1() {
      return dbReference1;
   }
   
   public Date getDtReference5() {
      return dtReference5;
   }

   public void setDtReference5(Date dtReference5) {
      this.dtReference5 = dtReference5;
   }

   public void setDbReference1(BigDecimal dbReference1) {
      this.dbReference1 = dbReference1;
   }

   public BigDecimal getDbReference2() {
      return dbReference2;
   }

   public void setDbReference2(BigDecimal dbReference2) {
      this.dbReference2 = dbReference2;
   }

   public BigDecimal getDbReference3() {
      return dbReference3;
   }

   public void setDbReference3(BigDecimal dbReference3) {
      this.dbReference3 = dbReference3;
   }

   public BigDecimal getDbReference4() {
      return dbReference4;
   }

   public void setDbReference4(BigDecimal dbReference4) {
      this.dbReference4 = dbReference4;
   }

   public String getStReference11() {
      return stReference11;
   }

   public void setStReference11(String stReference11) {
      this.stReference11 = stReference11;
   }

   public String getStReference12() {
      return stReference12;
   }

   public void setStReference12(String stReference12) {
      this.stReference12 = stReference12;
   }

   public String getStReference13() {
      return stReference13;
   }

   public void setStReference13(String stReference13) {
      this.stReference13 = stReference13;
   }

   public String getStReference14() {
      return stReference14;
   }

   public void setStReference14(String stReference14) {
      this.stReference14 = stReference14;
   }

   public String getStReference15() {
      return stReference15;
   }

   public void setStReference15(String stReference15) {
      this.stReference15 = stReference15;
   }

   public String getStReference16() {
      return stReference16;
   }

   public void setStReference16(String stReference16) {
      this.stReference16 = stReference16;
   }

   public String getStReference17() {
      return stReference17;
   }

   public void setStReference17(String stReference17) {
      this.stReference17 = stReference17;
   }

   public String getStReference18() {
      return stReference18;
   }

   public void setStReference18(String stReference18) {
      this.stReference18 = stReference18;
   }

   public String getStReference19() {
      return stReference19;
   }

   public void setStReference19(String stReference19) {
      this.stReference19 = stReference19;
   }

   public String getStReference20() {
      return stReference20;
   }

   public void setStReference20(String stReference20) {
      this.stReference20 = stReference20;
   }

   public BigDecimal getDbReference5() {
      return dbReference5;
   }

   public void setDbReference5(BigDecimal dbReference5) {
      this.dbReference5 = dbReference5;
   }

   public BigDecimal getDbReference6() {
      return dbReference6;
   }

   public void setDbReference6(BigDecimal dbReference6) {
      this.dbReference6 = dbReference6;
   }

   public BigDecimal getDbReference7() {
      return dbReference7;
   }

   public void setDbReference7(BigDecimal dbReference7) {
      this.dbReference7 = dbReference7;
   }

   public BigDecimal getDbReference8() {
      return dbReference8;
   }

   public void setDbReference8(BigDecimal dbReference8) {
      this.dbReference8 = dbReference8;
   }

   public String getStReference21() {
      return stReference21;
   }

   public void setStReference21(String stReference21) {
      this.stReference21 = stReference21;
   }

   public String getStReference22() {
      return stReference22;
   }

   public void setStReference22(String stReference22) {
      this.stReference22 = stReference22;
   }

   public String getStReference23() {
      return stReference23;
   }

   public void setStReference23(String stReference23) {
      this.stReference23 = stReference23;
   }

   public String getStReference24() {
      return stReference24;
   }

   public void setStReference24(String stReference24) {
      this.stReference24 = stReference24;
   }

   public String getStReference25() {
      return stReference25;
   }

   public void setStReference25(String stReference25) {
      this.stReference25 = stReference25;
   }

   public String getStReference26() {
      return stReference26;
   }

   public void setStReference26(String stReference26) {
      this.stReference26 = stReference26;
   }

   public String getStReference27() {
      return stReference27;
   }

   public void setStReference27(String stReference27) {
      this.stReference27 = stReference27;
   }

   public String getStReference28() {
      return stReference28;
   }

   public void setStReference28(String stReference28) {
      this.stReference28 = stReference28;
   }

   public String getStReference29() {
      return stReference29;
   }

   public void setStReference29(String stReference29) {
      this.stReference29 = stReference29;
   }

   public String getStReference30() {
      return stReference30;
   }

   public void setStReference30(String stReference30) {
      this.stReference30 = stReference30;
   }

   public String getStReference31() {
      return stReference31;
   }

   public void setStReference31(String stReference31) {
      this.stReference31 = stReference31;
   }

   public String getStReference32() {
      return stReference32;
   }

   public void setStReference32(String stReference32) {
      this.stReference32 = stReference32;
   }

   public String getStReference33() {
      return stReference33;
   }

   public void setStReference33(String stReference33) {
      this.stReference33 = stReference33;
   }

   public String getStReference34() {
      return stReference34;
   }

   public void setStReference34(String stReference34) {
      this.stReference34 = stReference34;
   }

   public String getStReference35() {
      return stReference35;
   }

   public void setStReference35(String stReference35) {
      this.stReference35 = stReference35;
   }

   public String getStReference36() {
      return stReference36;
   }

   public void setStReference36(String stReference36) {
      this.stReference36 = stReference36;
   }

   public String getStReference37() {
      return stReference37;
   }

   public void setStReference37(String stReference37) {
      this.stReference37 = stReference37;
   }

   public String getStReference38() {
      return stReference38;
   }

   public void setStReference38(String stReference38) {
      this.stReference38 = stReference38;
   }

   public String getStReference39() {
      return stReference39;
   }

   public void setStReference39(String stReference39) {
      this.stReference39 = stReference39;
   }

   public String getStReference40() {
      return stReference40;
   }

   public void setStReference40(String stReference40) {
      this.stReference40 = stReference40;
   }

   public String getStPolicyObjectRefID() {
      return stPolicyObjectRefID;
   }

   public void setStPolicyObjectRefID(String stPolicyObjectRefID) {
      this.stPolicyObjectRefID = stPolicyObjectRefID;
   }

   public String getStVoidFlag() {
      return stVoidFlag;
   }

   public void setStVoidFlag(String stVoidFlag) {
      this.stVoidFlag = stVoidFlag;
   }

   public String getStDeductibleDesc() {
      final DTOList deductibles = getDeductibles();

      final StringBuffer sz = new StringBuffer();

      for (int i = 0; i < deductibles.size(); i++) {
         InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);

         if (i>0) sz.append(",");

         sz.append(ded.getStCombinedDescription());
      }

      return sz.toString();
   }

   public BigDecimal getDbReference9() {
      return dbReference9;
   }

   public void setDbReference9(BigDecimal dbReference9) {
      this.dbReference9 = dbReference9;
   }

   public BigDecimal getDbReference10() {
      return dbReference10;
   }

   public void setDbReference10(BigDecimal dbReference10) {
      this.dbReference10 = dbReference10;
   }

   public BigDecimal getDbReference11() {
      return dbReference11;
   }

   public void setDbReference11(BigDecimal dbReference11) {
      this.dbReference11 = dbReference11;
   }

   public BigDecimal getDbReference12() {
      return dbReference12;
   }

   public void setDbReference12(BigDecimal dbReference12) {
      this.dbReference12 = dbReference12;
   }

   public BigDecimal getDbReference13() {
      return dbReference13;
   }

   public void setDbReference13(BigDecimal dbReference13) {
      this.dbReference13 = dbReference13;
   }

   public BigDecimal getDbReference14() {
      return dbReference14;
   }

   public void setDbReference14(BigDecimal dbReference14) {
      this.dbReference14 = dbReference14;
   }

   public BigDecimal getDbReference15() {
      return dbReference15;
   }

   public void setDbReference15(BigDecimal dbReference15) {
      this.dbReference15 = dbReference15;
   }

   public BigDecimal getDbReference16() {
      return dbReference16;
   }

   public void setDbReference16(BigDecimal dbReference16) {
      this.dbReference16 = dbReference16;
   }

   public BigDecimal getDbReference17() {
      return dbReference17;
   }

   public void setDbReference17(BigDecimal dbReference17) {
      this.dbReference17 = dbReference17;
   }

   public BigDecimal getDbReference18() {
      return dbReference18;
   }

   public void setDbReference18(BigDecimal dbReference18) {
      this.dbReference18 = dbReference18;
   }

   public BigDecimal getDbReference19() {
      return dbReference19;
   }

   public void setDbReference19(BigDecimal dbReference19) {
      this.dbReference19 = dbReference19;
   }

   public BigDecimal getDbReference20() {
      return dbReference20;
   }

   public void setDbReference20(BigDecimal dbReference20) {
      this.dbReference20 = dbReference20;
   }

   public BigDecimal getDbReference21() {
      return dbReference21;
   }

   public void setDbReference21(BigDecimal dbReference21) {
      this.dbReference21 = dbReference21;
   }

   public BigDecimal getDbReference22() {
      return dbReference22;
   }

   public void setDbReference22(BigDecimal dbReference22) {
      this.dbReference22 = dbReference22;
   }

   public BigDecimal getDbReference23() {
      return dbReference23;
   }

   public void setDbReference23(BigDecimal dbReference23) {
      this.dbReference23 = dbReference23;
   }

   public BigDecimal getDbReference24() {
      return dbReference24;
   }

   public void setDbReference24(BigDecimal dbReference24) {
      this.dbReference24 = dbReference24;
   }

   public BigDecimal getDbReference25() {
      return dbReference25;
   }

   public void setDbReference25(BigDecimal dbReference25) {
      this.dbReference25 = dbReference25;
   }

   public BigDecimal getDbReference26() {
      return dbReference26;
   }

   public void setDbReference26(BigDecimal dbReference26) {
      this.dbReference26 = dbReference26;
   }

   public BigDecimal getDbReference27() {
      return dbReference27;
   }

   public void setDbReference27(BigDecimal dbReference27) {
      this.dbReference27 = dbReference27;
   }

   public BigDecimal getDbReference28() {
      return dbReference28;
   }

   public void setDbReference28(BigDecimal dbReference28) {
      this.dbReference28 = dbReference28;
   }

   public BigDecimal getDbReference29() {
      return dbReference29;
   }

   public void setDbReference29(BigDecimal dbReference29) {
      this.dbReference29 = dbReference29;
   }

   public BigDecimal getDbReference30() {
      return dbReference30;
   }

   public void setDbReference30(BigDecimal dbReference30) {
      this.dbReference30 = dbReference30;
   }

   public String getStReference41() {
      return stReference41;
   }

   public void setStReference41(String stReference41) {
      this.stReference41 = stReference41;
   }

   public String getStReference42() {
      return stReference42;
   }

   public void setStReference42(String stReference42) {
      this.stReference42 = stReference42;
   }

   public String getStReference43() {
      return stReference43;
   }

   public void setStReference43(String stReference43) {
      this.stReference43 = stReference43;
   }

   public String getStReference44() {
      return stReference44;
   }

   public void setStReference44(String stReference44) {
      this.stReference44 = stReference44;
   }

   public String getStReference45() {
      return stReference45;
   }

   public void setStReference45(String stReference45) {
      this.stReference45 = stReference45;
   }

   public String getStReference46() {
      return stReference46;
   }

   public void setStReference46(String stReference46) {
      this.stReference46 = stReference46;
   }

   public String getStReference47() {
      return stReference47;
   }

   public void setStReference47(String stReference47) {
      this.stReference47 = stReference47;
   }

   public String getStReference48() {
      return stReference48;
   }

   public void setStReference48(String stReference48) {
      this.stReference48 = stReference48;
   }

   public String getStReference49() {
      return stReference49;
   }

   public void setStReference49(String stReference49) {
      this.stReference49 = stReference49;
   }

   public String getStReference50() {
      return stReference50;
   }

   public void setStReference50(String stReference50) {
      this.stReference50 = stReference50;
   }

   public String getStReference51() {
      return stReference51;
   }

   public void setStReference51(String stReference51) {
      this.stReference51 = stReference51;
   }

   public String getStReference52() {
      return stReference52;
   }

   public void setStReference52(String stReference52) {
      this.stReference52 = stReference52;
   }

   public String getStReference53() {
      return stReference53;
   }

   public void setStReference53(String stReference53) {
      this.stReference53 = stReference53;
   }

   public String getStReference54() {
      return stReference54;
   }

   public void setStReference54(String stReference54) {
      this.stReference54 = stReference54;
   }

   public String getStReference55() {
      return stReference55;
   }

   public void setStReference55(String stReference55) {
      this.stReference55 = stReference55;
   }

   public String getStReference56() {
      return stReference56;
   }

   public void setStReference56(String stReference56) {
      this.stReference56 = stReference56;
   }

   public String getStReference57() {
      return stReference57;
   }

   public void setStReference57(String stReference57) {
      this.stReference57 = stReference57;
   }

   public String getStReference58() {
      return stReference58;
   }

   public void setStReference58(String stReference58) {
      this.stReference58 = stReference58;
   }

   public String getStReference59() {
      return stReference59;
   }

   public void setStReference59(String stReference59) {
      this.stReference59 = stReference59;
   }

   public String getStReference60() {
      return stReference60;
   }

   public void setStReference60(String stReference60) {
      this.stReference60 = stReference60;
   }

   public String getStReference61() {
      return stReference61;
   }

   public void setStReference61(String stReference61) {
      this.stReference61 = stReference61;
   }

   public String getStReference62() {
      return stReference62;
   }

   public void setStReference62(String stReference62) {
      this.stReference62 = stReference62;
   }

   public String getStReference63() {
      return stReference63;
   }

   public void setStReference63(String stReference63) {
      this.stReference63 = stReference63;
   }

   public String getStReference64() {
      return stReference64;
   }

   public void setStReference64(String stReference64) {
      this.stReference64 = stReference64;
   }

   public String getStReference65() {
      return stReference65;
   }

   public void setStReference65(String stReference65) {
      this.stReference65 = stReference65;
   }

   public String getStReference66() {
      return stReference66;
   }

   public void setStReference66(String stReference66) {
      this.stReference66 = stReference66;
   }

   public String getStReference67() {
      return stReference67;
   }

   public void setStReference67(String stReference67) {
      this.stReference67 = stReference67;
   }

   public String getStReference68() {
      return stReference68;
   }

   public void setStReference68(String stReference68) {
      this.stReference68 = stReference68;
   }

   public String getStReference69() {
      return stReference69;
   }

   public void setStReference69(String stReference69) {
      this.stReference69 = stReference69;
   }

   public String getStReference70() {
      return stReference70;
   }

   public void setStReference70(String stReference70) {
      this.stReference70 = stReference70;
   }

   public String getStReference71() {
      return stReference71;
   }

   public void setStReference71(String stReference71) {
      this.stReference71 = stReference71;
   }

   public String getStReference72() {
      return stReference72;
   }

   public void setStReference72(String stReference72) {
      this.stReference72 = stReference72;
   }

   public String getStReference73() {
      return stReference73;
   }

   public void setStReference73(String stReference73) {
      this.stReference73 = stReference73;
   }

   public String getStReference74() {
      return stReference74;
   }

   public void setStReference74(String stReference74) {
      this.stReference74 = stReference74;
   }

   public String getStReference75() {
      return stReference75;
   }

   public void setStReference75(String stReference75) {
      this.stReference75 = stReference75;
   }

   public String getStReference76() {
      return stReference76;
   }

   public void setStReference76(String stReference76) {
      this.stReference76 = stReference76;
   }

   public String getStReference77() {
      return stReference77;
   }

   public void setStReference77(String stReference77) {
      this.stReference77 = stReference77;
   }

   public String getStReference78() {
      return stReference78;
   }

   public void setStReference78(String stReference78) {
      this.stReference78 = stReference78;
   }

   public String getStReference79() {
      return stReference79;
   }

   public void setStReference79(String stReference79) {
      this.stReference79 = stReference79;
   }

   public String getStReference80() {
      return stReference80;
   }

   public void setStReference80(String stReference80) {
      this.stReference80 = stReference80;
   }

   public Object getDesc(String s) throws Exception{
      return getPolicy().getPolicyType().getObjectMap().getDesc(s,this);
   }

   public String getStReference1Desc() {
      return stReference1Desc;
   }

   public void setStReference1Desc(String stReference1Desc) {
      this.stReference1Desc = stReference1Desc;
   }

   public String getStReference2Desc() {
      return stReference2Desc;
   }

   public void setStReference2Desc(String stReference2Desc) {
      this.stReference2Desc = stReference2Desc;
   }

   public String getStReference3Desc() {
      return stReference3Desc;
   }

   public void setStReference3Desc(String stReference3Desc) {
      this.stReference3Desc = stReference3Desc;
   }

   public String getStReference4Desc() {
      return stReference4Desc;
   }

   public void setStReference4Desc(String stReference4Desc) {
      this.stReference4Desc = stReference4Desc;
   }

   public String getStReference5Desc() {
      return stReference5Desc;
   }

   public void setStReference5Desc(String stReference5Desc) {
      this.stReference5Desc = stReference5Desc;
   }

   public String getStReference6Desc() {
      return stReference6Desc;
   }

   public void setStReference6Desc(String stReference6Desc) {
      this.stReference6Desc = stReference6Desc;
   }

   public String getStReference7Desc() {
      return stReference7Desc;
   }

   public void setStReference7Desc(String stReference7Desc) {
      this.stReference7Desc = stReference7Desc;
   }

   public String getStReference8Desc() {
      return stReference8Desc;
   }

   public void setStReference8Desc(String stReference8Desc) {
      this.stReference8Desc = stReference8Desc;
   }

   public String getStReference9Desc() {
      return stReference9Desc;
   }

   public void setStReference9Desc(String stReference9Desc) {
      this.stReference9Desc = stReference9Desc;
   }
   
   public String getStClaimLossID() {
      return stClaimLossID;
   }

   public void setStClaimLossID(String stClaimLossID) {
      this.stClaimLossID = stClaimLossID;
   }
   
   public String getStCoverageDesc() throws Exception{
      final DTOList coverage = getCoverage();

      final StringBuffer sz = new StringBuffer();

      for (int i = 0; i < coverage.size(); i++) {
         InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(i);

         if (i>0) sz.append(",");

         sz.append(cov.getStInsuranceCoverDesc());
      }

      return sz.toString();
   }

    public BigDecimal getDbObjectPremiTotalBeforeCoinsuranceAmount() {
        return dbObjectPremiTotalBeforeCoinsuranceAmount;
    }

    public void setDbObjectPremiTotalBeforeCoinsuranceAmount(BigDecimal dbObjectPremiTotalBeforeCoinsuranceAmount) {
        this.dbObjectPremiTotalBeforeCoinsuranceAmount = dbObjectPremiTotalBeforeCoinsuranceAmount;
    }

    public String getStPolisNo() {
        return stPolisNo;
    }

    public void setStPolisNo(String stPolisNo) {
        this.stPolisNo = stPolisNo;
    }
    
    public EntityView getEntity(String stEntID) {
        
        if (entity != null)
            if (!Tools.isEqual(entity.getStEntityID(), stEntID)) entity = null;
        
        if (entity == null)
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);
        
        return entity;
    }
    
    public Date getDtReference6() {
        return dtReference6;
    }

    public void setDtReference6(Date dtReference6) {
        this.dtReference6 = dtReference6;
    }

    public Date getDtReference7() {
        return dtReference7;
    }

    public void setDtReference7(Date dtReference7) {
        this.dtReference7 = dtReference7;
    }

    public String getStPolicyObjectPolicyRootID()
    {
        return stPolicyObjectPolicyRootID;
    }

    public void setStPolicyObjectPolicyRootID(String stPolicyObjectPolicyRootID)
    {
        this.stPolicyObjectPolicyRootID = stPolicyObjectPolicyRootID;
    }

    public String getStOrderNo()
    {
        return stOrderNo;
    }

    public void setStOrderNo(String stOrderNo)
    {
        this.stOrderNo = stOrderNo;
    }
    
    public String getStRekapKreasi() {
        return stRekapKreasi;
    }

    public void setStRekapKreasi(String stRekapKreasi) {
        this.stRekapKreasi = stRekapKreasi;
    }

    public EntityView getEntityCoins(String stReference8) {

        if (entity != null)
            if (!Tools.isEqual(entity.getStEntityID(), stReference8)) entity = null;
        
        if (entity == null)
            entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stReference8);

        return entity;
    }

    /**
     * @return the stObjectDescription2
     */
    public String getStObjectDescription2() {
        return stObjectDescription2;
    }

    /**
     * @param stObjectDescription2 the stObjectDescription2 to set
     */
    public void setStObjectDescription2(String stObjectDescription2) {
        this.stObjectDescription2 = stObjectDescription2;
    }

    public String getStObjectDescriptionWithoutCounter() {
      try {
         stObjectDescription = (String) getProperty(getPolicy().getPolicyType().getObjectMap().getStDescField());
      } catch (Exception e) {
         stObjectDescription = null;
      }

      return stObjectDescription;
   }

    /**
     * @return the stRiskCategoryID1
     */
    public String getStRiskCategoryID1() {
        return stRiskCategoryID1;
    }

    /**
     * @param stRiskCategoryID1 the stRiskCategoryID1 to set
     */
    public void setStRiskCategoryID1(String stRiskCategoryID1) {
        this.stRiskCategoryID1 = stRiskCategoryID1;
    }

    /**
     * @return the stRiskCategoryID2
     */
    public String getStRiskCategoryID2() {
        return stRiskCategoryID2;
    }

    /**
     * @param stRiskCategoryID2 the stRiskCategoryID2 to set
     */
    public void setStRiskCategoryID2(String stRiskCategoryID2) {
        this.stRiskCategoryID2 = stRiskCategoryID2;
    }

    /**
     * @return the stRiskCategoryID3
     */
    public String getStRiskCategoryID3() {
        return stRiskCategoryID3;
    }

    /**
     * @param stRiskCategoryID3 the stRiskCategoryID3 to set
     */
    public void setStRiskCategoryID3(String stRiskCategoryID3) {
        this.stRiskCategoryID3 = stRiskCategoryID3;
    }

    /**
     * @return the stRiskCategoryCode1
     */
    public String getStRiskCategoryCode1() {
        return stRiskCategoryCode1;
    }

    /**
     * @param stRiskCategoryCode1 the stRiskCategoryCode1 to set
     */
    public void setStRiskCategoryCode1(String stRiskCategoryCode1) {
        this.stRiskCategoryCode1 = stRiskCategoryCode1;
    }

    /**
     * @return the stRiskCategoryCode2
     */
    public String getStRiskCategoryCode2() {
        return stRiskCategoryCode2;
    }

    /**
     * @param stRiskCategoryCode2 the stRiskCategoryCode2 to set
     */
    public void setStRiskCategoryCode2(String stRiskCategoryCode2) {
        this.stRiskCategoryCode2 = stRiskCategoryCode2;
    }

    /**
     * @return the stRiskCategoryCode3
     */
    public String getStRiskCategoryCode3() {
        return stRiskCategoryCode3;
    }

    /**
     * @param stRiskCategoryCode3 the stRiskCategoryCode3 to set
     */
    public void setStRiskCategoryCode3(String stRiskCategoryCode3) {
        this.stRiskCategoryCode3 = stRiskCategoryCode3;
    }

    /**
     * @return the dbPeriodRateBefore
     */
    public BigDecimal getDbPeriodRateBefore() {
        return dbPeriodRateBefore;
    }

    /**
     * @param dbPeriodRateBefore the dbPeriodRateBefore to set
     */
    public void setDbPeriodRateBefore(BigDecimal dbPeriodRateBefore) {
        this.dbPeriodRateBefore = dbPeriodRateBefore;
    }

    /**
     * @return the stPeriodBaseBeforeID
     */
    public String getStPeriodBaseBeforeID() {
        return stPeriodBaseBeforeID;
    }

    /**
     * @param stPeriodBaseBeforeID the stPeriodBaseBeforeID to set
     */
    public void setStPeriodBaseBeforeID(String stPeriodBaseBeforeID) {
        this.stPeriodBaseBeforeID = stPeriodBaseBeforeID;
    }

    /**
     * @return the dbPeriodRate
     */
    public BigDecimal getDbPeriodRate() {
        return dbPeriodRate;
    }

    /**
     * @param dbPeriodRate the dbPeriodRate to set
     */
    public void setDbPeriodRate(BigDecimal dbPeriodRate) {
        this.dbPeriodRate = dbPeriodRate;
    }

    /**
     * @return the stPeriodBaseID
     */
    public String getStPeriodBaseID() {
        return stPeriodBaseID;
    }

    /**
     * @param stPeriodBaseID the stPeriodBaseID to set
     */
    public void setStPeriodBaseID(String stPeriodBaseID) {
        this.stPeriodBaseID = stPeriodBaseID;
    }

    /**
     * @return the stPremiumFactorID
     */
    public String getStPremiumFactorID() {
        return stPremiumFactorID;
    }

    /**
     * @param stPremiumFactorID the stPremiumFactorID to set
     */
    public void setStPremiumFactorID(String stPremiumFactorID) {
        this.stPremiumFactorID = stPremiumFactorID;
    }

    public boolean isUsingPeriodFactorPerObject(){
        return Tools.isYes(getStPeriodFactorObjectFlag());
    }

    /**
     * @return the stPeriodFactorObjectFlag
     */
    public String getStPeriodFactorObjectFlag() {
        return stPeriodFactorObjectFlag;
    }

    /**
     * @param stPeriodFactorObjectFlag the stPeriodFactorObjectFlag to set
     */
    public void setStPeriodFactorObjectFlag(String stPeriodFactorObjectFlag) {
        this.stPeriodFactorObjectFlag = stPeriodFactorObjectFlag;
    }

    /**
     * @return the dbLimitOfLiability
     */
    public BigDecimal getDbLimitOfLiability() {
        return dbLimitOfLiability;
    }

    /**
     * @param dbLimitOfLiability the dbLimitOfLiability to set
     */
    public void setDbLimitOfLiability(BigDecimal dbLimitOfLiability) {
        this.dbLimitOfLiability = dbLimitOfLiability;
    }

    /**
     * @return the stDataID
     */
    public String getStDataID() {
        return stDataID;
    }

    /**
     * @param stDataID the stDataID to set
     */
    public void setStDataID(String stDataID) {
        this.stDataID = stDataID;
    }

    /**
     * @return the dbLimitOfLiability2
     */
    public BigDecimal getDbLimitOfLiability2() {
        return dbLimitOfLiability2;
    }

    /**
     * @param dbLimitOfLiability2 the dbLimitOfLiability2 to set
     */
    public void setDbLimitOfLiability2(BigDecimal dbLimitOfLiability2) {
        this.dbLimitOfLiability2 = dbLimitOfLiability2;
    }

    /**
     * @return the stRiskNotes
     */
    public String getStRiskNotes() {
        return stRiskNotes;
    }

    /**
     * @param stRiskNotes the stRiskNotes to set
     */
    public void setStRiskNotes(String stRiskNotes) {
        this.stRiskNotes = stRiskNotes;
    }

    /**
     * @return the stRiskApproved
     */
    public String getStRiskApproved() {
        return stRiskApproved;
    }

    /**
     * @param stRiskApproved the stRiskApproved to set
     */
    public void setStRiskApproved(String stRiskApproved) {
        this.stRiskApproved = stRiskApproved;
    }

    /**
     * @return the stRiskFile
     */
    public String getStRiskFile() {
        return stRiskFile;
    }

    /**
     * @param stRiskFile the stRiskFile to set
     */
    public void setStRiskFile(String stRiskFile) {
        this.stRiskFile = stRiskFile;
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
     * @return the stChecking
     */
    public String getStChecking() {
        return stChecking;
    }

    /**
     * @param stChecking the stChecking to set
     */
    public void setStChecking(String stChecking) {
        this.stChecking = stChecking;
    }

    public boolean isDataTopUp(){
        return "3".equalsIgnoreCase(getStReference20());
    }

    
}
