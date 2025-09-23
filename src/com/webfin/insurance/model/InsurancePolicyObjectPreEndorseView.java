/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyObjectView
 * Author:  Denny Mahendra
 * Created: Nov 7, 2005 12:52:21 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.RecordAudit;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.util.*;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;

public abstract class InsurancePolicyObjectPreEndorseView extends DTO implements RecordAudit {
    public abstract String getStObjectDescription();
    public abstract BigDecimal getDbObjectInsuredAmount();
    public abstract void setDbObjectInsuredAmount(BigDecimal dbObjectInsuredAmount);
    public abstract BigDecimal getDbObjectPremiRate();
    public abstract void setDbObjectPremiRate2(BigDecimal dbObjectPremiRate2);
    public abstract BigDecimal getDbObjectPremiAmount();
    public abstract void setDbObjectPremiAmount(BigDecimal amt);
    public abstract BigDecimal getDbObjectPremiTotalAmount();
    public abstract void setDbObjectPremiTotalAmount(BigDecimal amt);
    public abstract BigDecimal getDbObjectPremiTotalBeforeCoinsuranceAmount();
    public abstract void setDbObjectPremiTotalBeforeCoinsuranceAmount(BigDecimal amt);
    public abstract String getStObjectDescriptionWithoutCounter();
    
    public abstract String getStReference2();
    
    public abstract String getStReference11();
    
    public abstract Date getDtReference5();
    
    public abstract String getStPolicyObjectID();
    
    public abstract void setStPolicyObjectID(String stPolicyObjectID);
    
    public abstract String getStPolicyID();
    
    public abstract void setStPolicyID(String stPolicyID);
    
    private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyObjectView.class);
    
    private InsurancePolicyPreEndorseView policy;
    
    private InsuranceTreatyView treaty;
    
    private CoverNoteView note;
    
    private String stInsuranceTreatyID;
    
    int scale = 0;
    
    public String getStInsuranceTreatyID() {
        return stInsuranceTreatyID;
    }
    
    public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
        this.stInsuranceTreatyID = stInsuranceTreatyID;
    }
    
    public String getStRiskClass() {
        throw new RuntimeException("Not implemented");
    }
    
    public void setStRiskClass(String stRiskClass) {
        throw new RuntimeException("Not implemented");
    }
    
    public InsurancePolicyPreEndorseView getPolicy() throws Exception {
        return policy;
    }
    
    public void setPolicy(InsurancePolicyPreEndorseView policy) {
        this.policy = policy;
    }
    
    public CoverNoteView getNote() {
        return note;
    }
    
    public void setPolicy(CoverNoteView note) {
        this.note = note;
    }
    
    public DTOList getClausules() {
        loadClausules();
        return clausules;
    }
    
    private void loadClausules() {
        try {
            if (clausules==null) {
                clausules = ListUtil.getDTOListFromQuery(
                        "   select " +
                        "      a.*,b.description,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2 " +
                        "   from " +
                        "      ins_clausules b " +
                        "      left join ins_pol_clausules a on " +
                        "         a.ins_clause_id = b.ins_clause_id" +
                        "         and a.ins_pol_obj_id = ?" +
                        "   where b.pol_type_id = ?"+
                        "	and b.cc_code = ? " ,
                        new Object [] {getStPolicyObjectID(),policy.getStPolicyTypeID(),policy.getStCostCenterCode()},
                        InsurancePolicyClausulesView.class
                        );
                
                for (int i = 0; i < clausules.size(); i++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                    if (icl.getStPolicyID()!=null) icl.select(); else icl.markNew();
                }
                
                if (isNew()) {
                    for (int i = 0; i < clausules.size(); i++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                        
                        if (Tools.isYes(icl.getStDefaultFlag())) icl.select();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setClausules(DTOList clausules) {
        this.clausules = clausules;
    }
    
    private DTOList clausules;
    private DTOList deductibles;
    private DTOList coverageVec;
    
    private BigDecimal dbInsuredAmountSharedEndorse;
    
    public DTOList getDeductibles() {
        loadDeductibles();
        return deductibles;
    }
    
    private void loadDeductibles() {
        try {
            if (deductibles==null) {
                deductibles = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_deduct where ins_pol_obj_id=? order by ins_pol_deduct_id",
                        new Object [] {getStPolicyObjectID()},
                        InsurancePolicyDeductibleView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setDeductibles(DTOList deductibles) {
        this.deductibles = deductibles;
    }
    
/*
   public void reCalculate() {
 
      try {
         loadSumInsureds();
         loadCoverage();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
 
      if (getDbObjectPremiRate()!=null) {
         setDbObjectPremiAmount(BDUtil.mul(getDbObjectInsuredAmount(), getDbObjectPremiRate()));
      }
 
      setDbObjectPremiTotalAmount(getDbObjectPremiAmount());
 
      if (clausules!=null)
         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
 
            if (icl.getDbRate()!=null)
               icl.setDbAmount(BDUtil.mul(icl.getDbRate(), getDbObjectInsuredAmount()));
 
            setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), icl.getDbAmount()));
         }
 
      setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), BDUtil.zero));
 
      setDbObjectInsuredAmount(null);
 
      if (suminsureds!=null)
         for (int i = 0; i < suminsureds.size(); i++) {
            InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(i);
 
            setDbObjectInsuredAmount(BDUtil.add(getDbObjectInsuredAmount(), itsi.getDbInsuredAmount()));
         }
 
      BigDecimal periodRate = getPolicy().getDbPeriodRateFactor();
 
      if (coverage!=null)
         for (int i = 0; i < coverage.size(); i++) {
            InsurancePolicyCoverView icv = (InsurancePolicyCoverView) coverage.get(i);
 
            //icv.setStCalculationDesc(null);
 
            if (!icv.isEntryInsuredAmount())
               icv.setDbInsuredAmount(getDbObjectInsuredAmount());
 
            if (icv.isEntryRate()) {
               icv.setDbPremi(BDUtil.mul(icv.getDbRatePct(), icv.getDbInsuredAmount()));
               icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRate));
               icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getDbPremiumFactorValue()));
               icv.setDbPremiNew(icv.getDbPremi());
 
               icv.setStCalculationDesc(null);
 
               if (icv.getDbPremi().longValue()!=0) {
 
                  final StringBuffer szc = new StringBuffer();
 
                  szc.append(p(icv.getDbInsuredAmount())+" x "+p(icv.getDbRate())+"%");
                  if (!isOne(getPolicy().getDbPeriodRateFactor()))
                     szc.append(" x "+getPolicy().getDbPeriodRateDesc());
                  if (!isOne(getPolicy().getDbPremiumFactorValue()))
                     szc.append(" x "+policy.getStPremiumFactorDesc());
 
                  icv.setStCalculationDesc(szc.toString());
               }
 
            } else {
               //icv.setDbRatePct(BDUtil.div(icv.getDbPremi(), icv.getDbInsuredAmount(),15));
               //icv.setDbRatePct(BDUtil.mul(icv.getDbRatePct(), periodRate));
 
               //if (policy.isStatusEndorse())
                  icv.setDbPremi(icv.getDbPremiNew());
 
            }
 
            if (icv.isEntryRate())
               if (policy.isStatusEndorse()) {
                  if (icv.getStInsurancePolicyCoverRefID()!=null)
                     if (icv.getRefCover().isEntryRate()) {
 
                        final InsurancePolicyCoverView refCover = icv.getRefCover();
 
                        final BigDecimal insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
 
                        if (insAmountDiff.longValue()!=0) {
 
                           BigDecimal m = BDUtil.mul(icv.getDbRatePct(), insAmountDiff);
 
                           m = BDUtil.mul(m, policy.getDbPeriodRateBeforeFactor());
                           m = BDUtil.mul(m, policy.getParentPolicy().getDbPremiumFactorValue());
 
                           if (m.longValue()!=0) {
 
                              icv.setDbPremi(BDUtil.add(m, icv.getDbPremiNew()));
 
                              final String curCalc = icv.getStCalculationDesc();
 
                              final StringBuffer szCalc = new StringBuffer();
 
                              //szCalc.append(" ( "+p(icv.getDbInsuredAmount())+" - "+p(refCover.getDbInsuredAmount())+" ) * ");
                              szCalc.append(p(insAmountDiff)+" x ");
                              szCalc.append(p(icv.getDbRate())+"%");
 
                              if (!isOne(policy.getDbPeriodRateBeforeFactor()))
                                 szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());
 
                              if (!isOne(policy.getParentPolicy().getDbPremiumFactorValue()))
                                 szCalc.append(" x "+policy.getParentPolicy().getStPremiumFactorDesc());
 
                              String calc = szCalc.toString();
 
                              if (curCalc!=null) calc+=" + ( "+curCalc+" )";
 
                              icv.setStCalculationDesc(calc);
                           }
                        }
                     }
               }
 
            setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), icv.getDbPremi()));
         }
   }
 */
    
    public void reCalculateNOW() throws Exception{
        
        try {
            loadSumInsureds();
            loadCoverage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        if(!getPolicy().getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        if (getDbObjectPremiRate()!=null) {
            //setDbObjectPremiAmount(BDUtil.mul(getDbObjectInsuredAmount(), getDbObjectPremiRate()));
            setDbObjectPremiAmount(BDUtil.mul(getDbObjectInsuredAmount(), getDbObjectPremiRate(),scale));
        }
        
        setDbObjectPremiTotalAmount(getDbObjectPremiAmount());
        setDbObjectPremiTotalBeforeCoinsuranceAmount(getDbObjectPremiTotalAmount());
        
      /*
      if (clausules!=null)
         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
       
            if (icl.getDbRate()!=null)
                icl.setDbAmount(BDUtil.mul(icl.getDbRate(), getDbObjectInsuredAmount(),scale));
       
            setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), icl.getDbAmount()));
         }*/
        
        setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), BDUtil.zero));
        
        setDbObjectInsuredAmount(null);
        
        if (suminsureds!=null)
            for (int i = 0; i < suminsureds.size(); i++) {
                InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(i);
            
                setDbObjectInsuredAmount(BDUtil.add(getDbObjectInsuredAmount(), itsi.getDbInsuredAmount()));
                
            }
        
        BigDecimal periodRate = getPolicy().getDbPeriodRateFactor();

        BigDecimal totalrate = null;
        BigDecimal tsiDiff = null;
        if (coverage!=null)
            for (int i = 0; i < coverage.size(); i++) {
            InsurancePolicyCoverView icv = (InsurancePolicyCoverView) coverage.get(i);
            
            if (!icv.isEntryInsuredAmount()){
                icv.setDbInsuredAmount(getDbObjectInsuredAmount());
            }  
            
            BigDecimal rateActual = new BigDecimal(0);
            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual = icv.getDbRatePct();
            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual = icv.getDbRateMile();
            
            totalrate = BDUtil.add(totalrate,rateActual);
            
            if (icv.isEntryRate()) {
                icv.setDbPremi(BDUtil.mul(rateActual, icv.getDbInsuredAmount(),scale));
                icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRate,scale));
                icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getDbPremiumFactorValue(),scale));
                icv.setDbPremiNew(icv.getDbPremi());
                icv.setStCalculationDesc(null);
                
                if (icv.getDbPremi().longValue()!=0) {
                    
                    final StringBuffer szc = new StringBuffer();
                    
                    szc.append(p(icv.getDbInsuredAmount())+" x "+p(icv.getDbRate())+getPolicy().getStRateMethodDesc());
                    if (!isOne(getPolicy().getDbPeriodRateFactor()))
                        szc.append(" x "+getPolicy().getDbPeriodRateDesc());
                    if (!isOne(getPolicy().getDbPremiumFactorValue()))
                        szc.append(" x "+policy.getStPremiumFactorDesc());
                    
                    icv.setStCalculationDesc(szc.toString());
                }
            } else {
                icv.setDbPremi(icv.getDbPremiNew());
            }
            
            //if (icv.isEntryRate())
            if (policy.isStatusEndorse()||policy.isStatusEndorseRI()) {
             //{
                
                icv.setStEntryRateFlag(icv.getStEntryRateFlag());
                if (icv.getStInsurancePolicyCoverRefID()!=null){
                    final InsurancePolicyCoverView refCover = icv.getRefCover();
                    
                    final BigDecimal insAmountDiff = BDUtil.subScale2(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                    
                    final BigDecimal insRateDiff = BDUtil.sub(icv.getDbRate(),refCover.getDbRate());
                    
                    BigDecimal periodRateEndorse = getPolicy().getDbPeriodRateBeforeFactor();
                    
                    tsiDiff = BDUtil.add(tsiDiff, insAmountDiff);
                   
                    setDbObjectInsuredAmountShareEndorse(insAmountDiff);
                                       
                    //if (icv.isEntryRate()) {
                    if (!icv.isEntryPremi()) {

                    if (insAmountDiff.longValue()!=0) {
                        //logger.logDebug("KENAIKAN TSI");
                        //BigDecimal m = BDUtil.mul(icv.getDbRatePct(), insAmountDiff);
                        BigDecimal rateActual2 = new BigDecimal(0);
                        if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual2 = icv.getDbRatePct();
                        else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual2 = icv.getDbRateMile();
                        
                        BigDecimal m = BDUtil.mul(rateActual2, insAmountDiff,scale);
                        
                        m = BDUtil.mul(m, policy.getDbPeriodRateBeforeFactor(),scale);
                        m = BDUtil.mul(m, policy.getParentPolicy().getDbPremiumFactorValue(),scale);
                        
                        if (m.longValue()!=0) {
                            
                            BigDecimal rateActual3 = new BigDecimal(0);
                            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = icv.getDbRatePct();
                            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = icv.getDbRateMile();
                            
                            if(insRateDiff.longValue()!=0){
                                //rateActual3 = insRateDiff;
                                if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = BDUtil.div(insRateDiff, BDUtil.hundred,10);
                                else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = BDUtil.div(insRateDiff, BDUtil.thousand,10);
                            }
                            
                            icv.setDbPremi(BDUtil.mul(insAmountDiff,rateActual3,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRateEndorse,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getParentPolicy().getDbPremiumFactorValue(),scale));
                            
                            //icv.setDbPremiNew(BDUtil.mul(icv.getDbPremiNew(),policy.getParentPolicy().getStPremiumFactorDesc()));
                            icv.setDbPremiNew(icv.getDbPremi());
                            
                            final String curCalc = icv.getStCalculationDesc();
                            
                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(p(insAmountDiff)+" x ");
                            szCalc.append(p(icv.getDbRate())+getPolicy().getStRateMethodDesc());
                            
                            if (!isOne(policy.getDbPeriodRateBeforeFactor()))
                                szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());
                            
                            if (!isOne(policy.getParentPolicy().getDbPremiumFactorValue()))
                                szCalc.append(" x "+policy.getParentPolicy().getStPremiumFactorDesc());
                            
                            String calc = szCalc.toString();
                            
                            
                            icv.setStCalculationDesc(calc);
                        }
                    }else if(BDUtil.biggerThanZero(insRateDiff)||BDUtil.lesserThanZero(insRateDiff)){
                        BigDecimal rateActual2 = new BigDecimal(0);
                        if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual2 = icv.getDbRatePct();
                        else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual2 = icv.getDbRateMile();
                        
                        BigDecimal m = BDUtil.mul(rateActual2, refCover.getDbInsuredAmount(),scale);
                        
                        m = BDUtil.mul(m, policy.getDbPeriodRateBeforeFactor(),scale);
                        m = BDUtil.mul(m, policy.getParentPolicy().getDbPremiumFactorValue(),scale);
                        //logger.logDebug("Kenaikan Rate= "+insRateDiff);
                        if (m.longValue()!=0) {
                            
                            BigDecimal rateActual3 = new BigDecimal(0);
                            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = BDUtil.getRateFromPct(insRateDiff);
                            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = BDUtil.getRateFromMile(insRateDiff);
                            
                            icv.setDbPremi(BDUtil.mul(refCover.getDbInsuredAmount(),rateActual3,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRateEndorse,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getParentPolicy().getDbPremiumFactorValue(),scale));
                            icv.setDbPremiNew(icv.getDbPremi());
                            
                            final String curCalc = icv.getStCalculationDesc();
                            
                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(p(refCover.getDbInsuredAmount())+" x ");
                            szCalc.append(p(insRateDiff)+getPolicy().getStRateMethodDesc());
                            
                            if (!isOne(policy.getDbPeriodRateBeforeFactor()))
                                szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());
                            
                            if (!isOne(policy.getParentPolicy().getDbPremiumFactorValue()))
                                szCalc.append(" x "+policy.getParentPolicy().getStPremiumFactorDesc());
                            
                            String calc = szCalc.toString();
                            
                            icv.setStCalculationDesc(calc);
                        }
                    }else if(BDUtil.isZero(insAmountDiff)&&BDUtil.isZero(insRateDiff)){
                        icv.setDbPremi(BDUtil.zero);
                        icv.setDbPremiNew(BDUtil.zero);
                        icv.setStCalculationDesc(null);
                    }else{
                        icv.setDbPremi(icv.getDbPremi());
                        icv.setDbPremiNew(icv.getDbPremiNew());
                        icv.setStCalculationDesc(icv.getStCalculationDesc());
                    }
                   }
                
                if(icv.isEntryPremi()){
                    icv.setDbPremi(icv.getDbPremi());
                    icv.setDbPremiNew(icv.getDbPremiNew());
                    icv.setStCalculationDesc(icv.getStCalculationDesc());
                }
               }else if(icv.getStInsurancePolicyCoverRefID()==null){
                    setDbObjectInsuredAmountShareEndorse(icv.getDbInsuredAmount());
               }
             //}
                //setDbObjectInsuredAmountShareEndorse(tsiDiff);
            }
            
            setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), icv.getDbPremi()));
            setDbObjectPremiTotalBeforeCoinsuranceAmount(getDbObjectPremiTotalAmount());
          }
    }
    
    private boolean isOne(BigDecimal dbx) {
        if (dbx==null) return false;
        return dbx.movePointRight(4).longValue()==10000;
    }
    
    private String p(BigDecimal db) {
        return  ConvertUtil.removeTrailing(ConvertUtil.print(db,4));
    }
    
    private DTOList suminsureds;
    private DTOList coverage;
    private DTOList treaties;
    private DTOList coveragereins;
    
    public DTOList getTreaties() {
        loadTreaties();
        return treaties;
    }
    
    private void loadTreaties() {
        try {
            if (treaties==null) {
                treaties = ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_treaty where ins_pol_obj_id=?",
                        new Object [] {getStPolicyObjectID()},
                        InsurancePolicyTreatyView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setTreaties(DTOList treaties) {
        this.treaties = treaties;
    }
    
    public DTOList getSuminsureds() throws Exception {
        loadSumInsureds();
        return suminsureds;
    }
    
    private void loadSumInsureds() throws Exception {
        if (suminsureds==null) {
            suminsureds = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.*" +
                    "   from  " +
                    "      ins_pol_tsi a" +
                    "   where" +
                    "      a.ins_pol_obj_id=? order by a.ins_tsi_cat_id asc" ,
                    new Object [] {getStPolicyObjectID()},
                    InsurancePolicyTSIView.class
                    );
            
            for (int i = 0; i < suminsureds.size(); i++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(i);
                
                InsuranceTSIView tsi2 = tsi.getInsuranceTSI();
                if (tsi.getStInsuranceTSIPolTypeID()==null) {
                    tsi.setStInsuranceTSIPolTypeID(policy.findTSIPolTypeID(getPolicy().getStPolicyTypeID(),tsi.getStInsuranceTSIID()));
                }
                
            }
        }
        
    }
    
    public void setSuminsureds(DTOList suminsureds) {
        this.suminsureds = suminsureds;
    }
    
    public DTOList getCoverage() throws Exception {
        loadCoverage();
        return coverage;
    }
    
    private void loadCoverage() throws Exception {
        if (coverage==null) {
         /*coverage = ListUtil.getDTOListFromQuery(
                 "   select" +
                 "      b.ins_cover_id, b.description as ins_cover_desc, b.cover_category" +
                 "   from" +
                 "      ins_cover_poltype a" +
                 "      inner join ins_cover b on b.ins_cover_id = a.ins_cover_id" +
                 "   where " +
                 "      a.pol_type_id = ?" ,
                 new Object [] {policy.getStPolicyTypeID()},
                 InsurancePolicyCoverView.class
         );*/  //todo
            
            coverage = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.*" +
                    "   from  " +
                    "      ins_pol_cover a" +
                    "   where" +
                    "      a.ins_pol_obj_id=? order by ins_pol_cover_id" ,
                    new Object [] {getStPolicyObjectID()},
                    InsurancePolicyCoverView.class
                    );
        }
    }
    
    public DTOList getCoverageReinsurance() throws Exception {
        loadCoverageReinsurance();
        return coveragereins;
    }
    
    private void loadCoverageReinsurance() throws Exception {
        if (coveragereins==null) {
         /*coverage = ListUtil.getDTOListFromQuery(
                 "   select" +
                 "      b.ins_cover_id, b.description as ins_cover_desc, b.cover_category" +
                 "   from" +
                 "      ins_cover_poltype a" +
                 "      inner join ins_cover b on b.ins_cover_id = a.ins_cover_id" +
                 "   where " +
                 "      a.pol_type_id = ?" ,
                 new Object [] {policy.getStPolicyTypeID()},
                 InsurancePolicyCoverView.class
         );*/  //todo
            
            coveragereins = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.*" +
                    "   from  " +
                    "      ins_pol_cover_ri a" +
                    "   where" +
                    "      a.ins_pol_obj_id=?" ,
                    new Object [] {getStPolicyObjectID()},
                    InsurancePolicyCoverReinsView.class
                    );
        }
    }
    
    public void setCoverage(DTOList coverage) {
        this.coverage = coverage;
    }
    
    public DTOList getCoveragereins() throws Exception {
        loadCoveragereins();
        return coveragereins;
    }
    
    private void loadCoveragereins() throws Exception {
        if (coveragereins==null) {
         /*coverage = ListUtil.getDTOListFromQuery(
                 "   select" +
                 "      b.ins_cover_id, b.description as ins_cover_desc, b.cover_category" +
                 "   from" +
                 "      ins_cover_poltype a" +
                 "      inner join ins_cover b on b.ins_cover_id = a.ins_cover_id" +
                 "   where " +
                 "      a.pol_type_id = ?" ,
                 new Object [] {policy.getStPolicyTypeID()},
                 InsurancePolicyCoverView.class
         );*/  //todo
            
            coveragereins = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "      a.*, b.ent_name as ent_name" +
                    "   from  " +
                    "      ins_pol_cover_ri a" +
                    "		inner join ent_master b on b.ent_id = a.member_ent_id" +
                    "   where" +
                    "      a.ins_pol_obj_id=?" ,
                    new Object [] {getStPolicyObjectID()},
                    InsurancePolicyCoverReinsView.class
                    );
        }
    }
    
    public void setCoveragereins(DTOList coveragereins) {
        this.coveragereins = coveragereins;
    }
    
    public abstract String getStSubPolicyNo();
    
    public abstract void setStSubPolicyNo(String stSubPolicyNo);
    
    public abstract String getStRiskCategoryID();
    
    public abstract void setStRiskCategoryID(String stRiskCategoryID);
    
    public FlexFieldHeaderView getSPPAFF() {
        final String pt = policy.getStPolicyTypeID();
        if (pt==null) return null;
        return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "SPPA_"+pt);
    }
    
    public FlexFieldHeaderView getSPPAFF2() {
        final String pt = note.getStPolicyTypeID();
        if (pt==null) return null;
        return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "SPPA_"+pt);
    }
    
    public FlexFieldHeaderView getClaimFF() {
        final String pt = policy.getStPolicyTypeID();
        if (pt==null) return null;
        return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, "CLAIMD_"+pt);
    }
    
    public String getStVoidFlag() {
        return "N";
    }
    
    public void setStVoidFlag(String stVoidFlag) {
        
    }
    
    public boolean isVoid() {
        return Tools.isYes(getStVoidFlag());
    }
    
    public abstract String getStPolicyObjectRefID();
    
    public abstract void setStPolicyObjectRefID(String stPolicyObjectRefID);
    
    public InsuranceRiskCategoryView getRiskCategory() {
        return (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, getStRiskCategoryID());
    }
    
    public void setDbTreatyLimitRatio(BigDecimal x) {
    }
    
    public BigDecimal getDbTreatyLimitRatio() {
        final BigDecimal tlr = getDbTreatyLimitRatio1();
        
        if (tlr==null) return new BigDecimal(100);
        
        return tlr;
    }
    
    public BigDecimal getDbTreatyLimitRatio1() {
        final BigDecimal a100 = new BigDecimal(100);
        
        if (getStRiskCategoryID()==null) return a100;
        
        final InsuranceRiskCategoryView rc = getRiskCategory();
        
        if (rc==null) return a100;
        
        int rcl=0;
        
        if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());
        
        switch (rcl) {
            case 0: return rc.getDbTreatyLimit0();
            case 1: return rc.getDbTreatyLimit1();
            case 2: return rc.getDbTreatyLimit2();
            case 3: return rc.getDbTreatyLimit3();
            default:
                throw new RuntimeException("Invalid Risk Class : "+getStRiskClass());
        }
    }
    
    public String getStRiskCategoryDesc() {
        final InsuranceRiskCategoryView rc = getRiskCategory();
        
        if (rc==null) return "";
        
        return rc.getStDescription();
    }
    
    public String getStRiskCategoryCode() {
        final InsuranceRiskCategoryView rc = getRiskCategory();
        
        if (rc==null) return "";
        
        return rc.getStInsuranceRiskCategoryCode();
    }
    
    public String getStRiskCategoryExcluded() {
        final InsuranceRiskCategoryView rc = getRiskCategory();
        String flag = "No";
        
        if (rc==null) return "";
        
        if(rc.getStExcRiskFlag()!=null)
            if(rc.getStExcRiskFlag().equalsIgnoreCase("Y")) flag = "Yes";

        return flag;
    }
    
    public InsurancePolicyCoverView getCover(int i) throws Exception {
        
        loadCoverage();
        
        if (coverage.size()<=i) return null;
        
        return (InsurancePolicyCoverView)coverage.get(i);
    }
    
    public InsurancePolicyCoverReinsView getCoverReins(int i) throws Exception {
        
        loadCoveragereins();
        
        if (coveragereins.size()<=i) return null;
        
        return (InsurancePolicyCoverReinsView)coveragereins.get(i);
    }
    
    public BigDecimal getDbPremiRate(int i) throws Exception {
        
        final InsurancePolicyCoverView cover = getCover(i);
        
        if (cover == null) return null;
        
        return cover.getDbRatePct();
    }
    
    public BigDecimal getDbPremi(int i) throws Exception {
        
        final InsurancePolicyCoverView cover = getCover(i);
        
        if (cover == null) return null;
        
        return cover.getDbPremi();
    }
    
    public DTOList getTreatyDetails() {
        
        DTOList l = new DTOList();
        
        if (getTreaties()!=null && getTreaties().size()>0) {
            final InsurancePolicyTreatyView tty = (InsurancePolicyTreatyView)getTreaties().get(0);
            
            l = tty.getDetails();
        }
        
        return l;
    }
    
    public BigDecimal getDbObjectInsuredAmountShare() {
        final InsurancePolicyCoinsView holdingCoin = policy.getHoldingCoin();
        
        if (holdingCoin==null) return null;
        
        BigDecimal tes = null;

        tes = BDUtil.mul(getDbObjectInsuredAmount(), BDUtil.getRateFromPct(holdingCoin.getDbSharePct()),2);
        
        return tes;
    }
    
    
    public BigDecimal getDbObjectInsuredAmountShareEndorse() {
        return dbInsuredAmountSharedEndorse;
    }
    
    public void setDbObjectInsuredAmountShareEndorse(BigDecimal dbInsuredAmountSharedEndorse){
        this.dbInsuredAmountSharedEndorse = dbInsuredAmountSharedEndorse;
    }
    
    public Object getStRiskDetailDescription(String fieldName) throws Exception{
        return getPolicy().getPolicyType().getObjectMap().getDesc(fieldName, this);
    }
    
    public Object getStSPPADescription(String fieldName) {
        return getSPPAFF().getDesc(fieldName, this);
    }
    
    public Object getStClaimDescription(String fieldName) {
        return getClaimFF().getDesc(fieldName, this);
    }
    
    public BigDecimal getDbCoverSumInsuredAmount(String stInsCoverID) throws Exception {
        final DTOList coverage = getCoverage();
        
        BigDecimal tot = null;
        
        for (int i = 0; i < coverage.size(); i++) {
            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(i);
            
            if (!Tools.isEqual(cov.getStInsuranceCoverID(), stInsCoverID)) continue;
            
            tot = BDUtil.add(tot, cov.getDbInsuredAmount());
        }
        
        return tot;
    }
    
    public BigDecimal getDbClaimRatio() throws Exception{
        
        if (getPolicy()==null) return null;
        
        return BDUtil.div(policy.getDbClaimAmount(), getDbObjectPremiTotalAmount(),10);
    }
    
    public BigDecimal getDbCoinsSessionPct()throws Exception{
        final SQLUtil S = new SQLUtil();
        BigDecimal session_pct = null;
        try {
            final InsurancePolicyCoinsView holdingCoin = policy.getHoldingCoin();
            
            
            
            final PreparedStatement PS = S.setQuery("select * "+
                    "from ins_co_scale "+
                    "where ( ? between scale_lower and scale_upper) and pol_type_id = ? "+
                    "and ( ? >= scale_period_start and ? <= scale_period_end);");
            
            if(policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")){
                if(policy.getDbSharePct()==null) throw new RuntimeException("Share Askrida Belum Diisi");
                else PS.setBigDecimal(1, policy.getDbSharePct());
            }else{
                PS.setBigDecimal(1, holdingCoin.getDbSharePct());
            }
            
            PS.setString(2,policy.getStPolicyTypeID());
            S.setParam(3,policy.getDtPeriodStart());
            S.setParam(4,policy.getDtPeriodStart());
            
            final ResultSet RS = PS.executeQuery();
            
            if (RS.next()) {
                session_pct = RS.getBigDecimal("session_pct");
            }
            
            return session_pct;
            
        }finally{
            S.release();
        }
        
        
    }
    
    public InsuranceTreatyView getTreaty() {
        final DTOList treaties = getTreaties();
        InsurancePolicyTreatyView view = (InsurancePolicyTreatyView) treaties.get(0);
        if (treaty==null)
            treaty = (InsuranceTreatyView) DTOPool.getInstance().getDTO(InsuranceTreatyView.class, view.getStInsuranceTreatyID());
        return treaty;
    }
    
    public InsuranceRiskCategoryView getRiskCategoryMaipark() {
        return (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, getStReference2());
    }
    
    public void setDbTreatyLimitRatioMaipark(BigDecimal x) {
    }
    
    public BigDecimal getDbTreatyLimitRatioMaipark() throws Exception {
        final BigDecimal tlr = getDbTreatyLimitRatio1Maipark();
        
        if (tlr==null) return new BigDecimal(100);
        
        return tlr;
    }
    
    public BigDecimal getDbTreatyLimitRatio1Maipark() throws Exception{
        final BigDecimal a100 = new BigDecimal(100);
        
        if (getStRiskCategoryID()==null) return a100;
        
        final InsuranceRiskCategoryView rc = getRiskCategoryMaiparkByDate(policy.getDtPeriodStart());
        
        if (rc==null) return a100;
        
        int rcl=0;
        
        if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());
        
        switch (rcl) {
            case 0: return rc.getDbTreatyLimit0();
            case 1: return rc.getDbTreatyLimit1();
            case 2: return rc.getDbTreatyLimit1();
            case 3: return rc.getDbTreatyLimit1();
            default:
                throw new RuntimeException("Invalid Risk Class : "+getStRiskClass());
        }
    }
    
    public DTOList getCoverVehicle() throws Exception {
        loadCoverageVehicle();
        return coverageVec;
    }
    
    private void loadCoverageVehicle() throws Exception {
        if (coverageVec==null) {

            coverageVec = ListUtil.getDTOListFromQuery(
                    "   select" +
                    "	sum(checkreas(a.cover_category='MAIN',a.insured_amount)) as insured_amount,"+    
                    "   sum(checkreas(a.cover_category='MAIN',a.premi)) as rate,"+      
                    "   sum(checkreas(a.ins_cover_id=101,a.insured_amount)) as premi,"+      
                    "   sum(checkreas(a.ins_cover_id=101,a.premi)) as premi_new,"+       
                    "   sum(checkreas(a.cover_category='EXT' and a.ins_cover_id <> 101,a.premi)) as premi_not,"+       
                    "   sum(a.premi) as premi_tot   "+
                    "   from  " +
                    "      ins_pol_cover a" +
                    "   where" +
                    "      a.ins_pol_obj_id=?" ,
                    new Object [] {getStPolicyObjectID()},
                    InsurancePolicyCoverView.class
                    );
        }
    }
    
    public void setCoverVehicle(DTOList coverageVec) {
        this.coverageVec = coverageVec;
    }
    
    public abstract String getStClaimLossID();
    
    public abstract void setStClaimLossID(String stClaimLossID);
    
    public InsuranceClaimLossView getClaimLoss() {
        return (InsuranceClaimLossView) DTOPool.getInstance().getDTO(InsuranceClaimLossView.class, getStClaimLossID());
    }
    
    public String getClaimLossDesc() {
        final InsuranceClaimLossView clv = getClaimLoss();
        
        if (clv==null) return "";
        
        return clv.getStLossDesc();
    }
    /*
    public BigDecimal getDbObjectInsuredAmountShareEndorse() throws Exception{
        
        try {
            loadCoverage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        BigDecimal totalDiff = null;
        if (coverage!=null)
            for (int i = 0; i < coverage.size(); i++) {
            InsurancePolicyCoverView icv = (InsurancePolicyCoverView) coverage.get(i);

                if (icv.getStInsurancePolicyCoverRefID()!=null){
                    final InsurancePolicyCoverView refCover = icv.getRefCover();
                    
                    final BigDecimal insAmountDiff = BDUtil.subScale2(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                    
                    final BigDecimal insRateDiff = BDUtil.sub(icv.getDbRate(),refCover.getDbRate());
                    
                    BigDecimal periodRateEndorse = getPolicy().getDbPeriodRateBeforeFactor();
                    
                    totalDiff = BDUtil.add(totalDiff, insAmountDiff);
               }
            }
        return totalDiff;
    }*/
    
    public BigDecimal getDbTreatyLimitRatioMarineHull() {
        final BigDecimal tlr = getDbTreatyLimitRatio1();
        
        if (tlr==null) return new BigDecimal(100);
        
        return tlr;
    }
    
    public BigDecimal getDbTreatyLimitRatio1MarineHull() {
        final BigDecimal a100 = new BigDecimal(100);
        
        if (getStRiskCategoryID()==null) return a100;
        
        final InsuranceRiskCategoryView rc = getRiskCategory();
        
        if (rc==null) return a100;
        
        int rcl=0;
        
        if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());
        
        switch (rcl) {
            case 0: return rc.getDbTreatyLimit0();
            case 1: return rc.getDbTreatyLimit1();
            case 2: return rc.getDbTreatyLimit2();
            case 3: return rc.getDbTreatyLimit3();
            default:
                throw new RuntimeException("Invalid Risk Class : "+getStRiskClass());
        }
    }

    private DTOList SumCoverage;

    public DTOList getSumCoverage() throws Exception {
        loadSumCoverage();
        return SumCoverage;
    }

    private void loadSumCoverage() throws Exception {
        if (SumCoverage==null) {
            SumCoverage = ListUtil.getDTOListFromQuery(
                    "   select a.ins_cover_id,sum(a.insured_amount) as insured_amount " +
                    "   from ins_pol_cover a" +
                    "   where a.pol_id = ? group by a.ins_cover_id order by a.ins_cover_id " ,
                    new Object [] {getStPolicyID()},
                    InsurancePolicyCoverView.class
                    );
        }
    }

    private DTOList sumTSIAmount;

    public DTOList getSumTSIAmount() throws Exception {
        loadSumTSIAmount();
        return sumTSIAmount;
    }

    private void loadSumTSIAmount() throws Exception {
        if (sumTSIAmount==null) {
            sumTSIAmount = ListUtil.getDTOListFromQuery(
                    "   select a.* " +
                    "   from ins_pol_tsi a" +
                    "   where a.pol_id = ? order by a.ins_pol_tsi_id,a.ins_tsi_cat_id " ,
                    new Object [] {getStPolicyID()},
                    InsurancePolicyTSIView.class
                    );
        }
    }
    

    public void validateRateOJK() throws Exception{

        if(policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusEndorseRI() || policy.isStatusHistory())
            return;

        final InsurancePolicyTypeView polType = policy.getPolicyType();

        if(polType.getStControlFlags()==null) return;

        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        //validateBolehKomisi(objx);
        
        boolean useKodeOkupasi = false;
        boolean useCoverageId = false;
        boolean useRiskCatCode = false;
        boolean useLowestTsi = false;
        boolean useTopTsi = false;

        String kodePlat = StringTools.getFirstLetterOnly(objx.getStReference1().toUpperCase());

        if(objx.getStReference1().toUpperCase().trim().equalsIgnoreCase("TBA"))
            kodePlat = objx.getStReference1().toUpperCase().trim();

        String levelWilayah = getLevelWilayahPlatNo(kodePlat);

        boolean cekBatasAtas = true;

        if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){
            int tahunSekarang = Integer.parseInt(DateUtil.getYear(objx.getDtReference1()));
            int tahunPembuatan = Integer.parseInt(objx.getStReference3());
            int usiaKendaraan = tahunSekarang - tahunPembuatan;

            if( usiaKendaraan > 5) cekBatasAtas = false;
        }

        if (polType!=null){
            useKodeOkupasi = polType.checkProperty("OKUPASI_CODE","Y");
            useCoverageId = polType.checkProperty("COVER_ID","Y");
            useRiskCatCode = polType.checkProperty("RISK_CODE","Y");
            useLowestTsi = polType.checkProperty("TSI_LOW1","Y");
            useTopTsi = polType.checkProperty("TSI_TOP1","Y");
        }

        DateTime tanggalAwal = new DateTime(policy.getDtPeriodStart());
        DateTime tanggalAkhir = new DateTime(policy.getDtPeriodEnd());

        BigDecimal timeExcess = BDUtil.zero;
        String excessUnit = "";

        BigDecimal tsiObject = getDbObjectInsuredAmount();

        //if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){
            final DTOList suminsured = getSuminsureds();

            for (int i = 0; i < suminsured.size(); i++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsured.get(i);

//                System.out.println("########### MASUK TSI #################");
//
//                System.out.println("########### cat id = "+ tsi.getStInsuranceTSIID());
//                System.out.println("########### desc =  "+ tsi.getStTSICategoryDescription());
//                System.out.println("########### MASUK TSI #################");
                
                if(tsi.getStInsuranceTSIID().equalsIgnoreCase("110"))
                    tsiObject = tsi.getDbInsuredAmount();

                if(tsi.getStInsuranceTSIID().equalsIgnoreCase("108")){
                    if(tsi.getStTSICategoryDescription()==null)
                        throw new RuntimeException("Deskripsi Kategori TSI tidak boleh kosong jika Others");

                    if(tsi.getStTSICategoryDescription()!=null)
                        if(tsi.getStTSICategoryDescription().equalsIgnoreCase(""))
                            throw new RuntimeException("Deskripsi Kategori TSI tidak boleh kosong jika Others");
                }    
            }

        final DTOList coverage = getCoverage();
        
        for (int i = 0; i < coverage.size(); i++) {
                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(i);
            

                final InsuranceRiskCategoryView rc = getRiskCategory();

                int rcl=0;

                if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());

                if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){

                    if(levelWilayah==null)
                        throw new RuntimeException("Kendaraan no ("+ objx.getStOrderNo()+") "+objx.getStReference1().toUpperCase() + " salah! Kode plat no "+ kodePlat +" tidak ditemukan, cek kembali no plat kendaraan");

                    rcl = Integer.parseInt(levelWilayah);
                }
                    

                if(rcl == 0) return;

                String riskCode = rc.getStInsuranceRiskCategoryCode();
                //29341
                //0123456
                //if(riskCode.length()<= 5) riskCode = riskCode;
                //else riskCode = riskCode.substring(0, 5);

                //int kodeOkupasiLevel = riskCode.length();

                //riskCode = "'"+ riskCode + "%'";

                /*
                SELECT *
                FROM INS_RATES_SETTING
                WHERE kd_okupasi_4_digit like '2976%' -- KODE OKUPASI
                and '27' = ANY (ins_cover_id_array) --COVERAGE ID
                AND NULL = ANY (risk_cat_code_array) --RISK KODE
                AND TSI_LOWEST1 >= 0   -- BATAS BAWAH TSI
                AND TSI_TOP1 <= 0      -- BATAS ATAS TSI
                */
                final SQLAssembler sqa = new SQLAssembler();

                sqa.addSelect("time_excess, excess_unit, unit, limit_by, rate_lowest"+ rcl+", rate_top"+rcl);
                sqa.addQuery("from ins_rates_setting");

                String kodeOkupasiParameter = null;

                String param5digit = "";
                String param4digit = "";
                String param3digit = "";
                String param2digit = "";

                //29341
                //01234

                if(riskCode.length()==4 ) riskCode = riskCode + "X";
                if(riskCode.length()==3 ) riskCode = riskCode + "XX";
                if(riskCode.length()==2 ) riskCode = riskCode + "XXX";

                param5digit = riskCode;
                param4digit = riskCode.substring(0, 4);
                param3digit = riskCode.substring(0, 3);
                param2digit = riskCode.substring(0, 2);

                if(useKodeOkupasi){
                    kodeOkupasiParameter = riskCode;
                    //sqa.addClause(" (kd_okupasi_"+ kodeOkupasiLevel +"_digit like "+ kodeOkupasiParameter+" or (kd_okupasi_"+ kodeOkupasiLevel +"_digit) is null) ");
                     sqa.addClause("(kd_okupasi = '"+ param5digit +"' or kd_okupasi = '"+ param4digit +"' or kd_okupasi = '"+ param3digit +"' or kd_okupasi = '"+ param2digit +"' or kd_okupasi is null)");
                }

                if(useCoverageId){
                    sqa.addClause("'"+ cover.getStInsuranceCoverID() + "'= ANY (ins_cover_id_array)");
                }

                if(useRiskCatCode){
                    sqa.addClause("'"+ getRiskCategory().getStInsuranceRiskCategoryCode() + "'= ANY (risk_cat_code_array)");
                }

                if(useLowestTsi){
                    sqa.addClause(" TSI_LOWEST1 <= ?");
                    sqa.addPar(tsiObject);
                }

                if(useTopTsi){
                    sqa.addClause(" TSI_TOP1 >= ?");
                    sqa.addPar(tsiObject);
                }

                if(!cover.getStCoverCategory().equalsIgnoreCase("MAIN")){
                    sqa.addClause(" (ins_cover_id_parent is null or ins_cover_id_parent = ?)");
                    sqa.addPar(getMainCoverage().getStInsuranceCoverID());
                }

                sqa.addClause("pol_type_id = ?");
                sqa.addPar(policy.getStPolicyTypeID());

//                System.out.println("#################################################################");
//                System.out.println("********** cover type : "+ cover.getStInsuranceCoverID() + " | "+cover.getInsuranceCoverage().getStDescription());
//                System.out.println("********** SQL : "+sqa.getSQL());
//                System.out.println("********** rcl : "+rcl);
//                System.out.println("********** rate factor : "+getPolicy().getDbPeriodRateFactor());
//                System.out.println("********** premium factor : "+getPolicy().getDbPremiumFactorValue());
                
                String sql = sqa.getSQL();

                if(useKodeOkupasi)
                    sql = sqa.getSQL() + " order by length(kd_okupasi) desc limit 1 ";

                final DTOList l = ListUtil.getDTOListFromQuery(
                        sql,
                        sqa.getPar(),
                        HashDTO.class
                        );

                if(l.size()> 0){
                        HashDTO h = (HashDTO) l.get(0);
                        BigDecimal batasBawah = h.getFieldValueByFieldNameBD("rate_lowest"+rcl);
                        BigDecimal batasAtas = h.getFieldValueByFieldNameBD("rate_top"+rcl);
                        String limitBy = h.getFieldValueByFieldNameST("limit_by");
                        String unit = h.getFieldValueByFieldNameST("unit");

                        boolean limitByRate = limitBy.equalsIgnoreCase("RATE");
                        boolean limitByTSI = limitBy.equalsIgnoreCase("TSI");

                        timeExcess = h.getFieldValueByFieldNameBD("time_excess");
                        excessUnit = h.getFieldValueByFieldNameST("excess_unit");

                        DateTime periodeAwal = new DateTime(policy.getDtPeriodStart());
                        DateTime periodeAkhir = new DateTime(policy.getDtPeriodEnd());
                        Days day = Days.daysBetween(periodeAwal, periodeAkhir);
                        int jumlahHari = day.getDays();

//                        System.out.println("**************  batas bawah : "+ batasBawah);
//                        System.out.println("**************  batas atas  : "+ batasAtas);

                        BigDecimal batasBawahDisplay = null;
                        BigDecimal batasAtasDisplay = null;
                        if(!polType.getStRateMethod().equalsIgnoreCase(unit)){
                            if(polType.getStRateMethod().equalsIgnoreCase("%")){
                                batasBawah = BDUtil.getPctFromMile(batasBawah);
                                batasAtas = BDUtil.getPctFromMile(batasAtas);
                            }else if(polType.getStRateMethod().equalsIgnoreCase("PMIL")){
                                batasBawah = BDUtil.getMileFromPCT(batasBawah);
                                batasAtas = BDUtil.getMileFromPCT(batasAtas);
                            }
                        }

                        batasBawahDisplay = batasBawah;
                        batasAtasDisplay = batasAtas;
                        

                        //batasAtas = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasAtas);
                        //batasBawah = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasBawah);

                        //System.out.println("**************  limit by  : "+ limitBy);
//                        System.out.println("**************  jumlah hari  : "+ jumlahHari);
//                        System.out.println("**************  batas bawah pro rata : "+ batasBawah);
//                        System.out.println("**************  batas atas pro rata : "+ batasAtas);

                        String message ="";
                        boolean masihMasukBatas = true;

                        BigDecimal rateCoverage = BDUtil.zero;
                        BigDecimal rateCover = BDUtil.zero;
                        if(BDUtil.isZeroOrNull(cover.getDbRate()) && !BDUtil.isZeroOrNull(cover.getDbPremiNew())){
                            rateCoverage = BDUtil.div(cover.getDbPremiNew(), cover.getDbInsuredAmount(), 6);

                            if(polType.getStRateMethod().equalsIgnoreCase("%")) rateCoverage = BDUtil.mul(rateCoverage, BDUtil.hundred);
                            else rateCoverage = BDUtil.mul(rateCoverage, BDUtil.thousand);
                        }else{
                            rateCoverage = cover.getDbRate();
                        }

                        //rateCover = BDUtil.mul(rateCoverage, getPolicy().getDbPeriodRateFactor());
                        rateCover = rateCoverage;
                        
                        if(BDUtil.isZeroOrNull(cover.getDbRate()) && !BDUtil.isZeroOrNull(cover.getDbPremiNew())){
                            rateCover = BDUtil.div(rateCoverage, getPolicy().getDbPeriodRateFactor());
                        }

//                        System.out.println("**************  rate Cover pro rata: "+ rateCover);
                        if(!cekBatasAtas)
                            batasAtas = BDUtil.hundred;

                        if(limitByRate){

                            if(BDUtil.lesserThan(rateCover, batasBawah) || BDUtil.biggerThan(rateCover, batasAtas)){
                                masihMasukBatas = false;
                                message = "Rate Coverage "+ cover.getInsuranceCoverage().getStDescription() +" ("+ ConvertUtil.removeTrailing(String.valueOf(rateCover)) + ") tidak sesuai ketentuan OJK";
                                message = message +"<br><b>------------------- DESKRIPSI --------------------";
//                                message = message +"<br>Rate Cover Per Tahun : "+  NumberUtil.getMoneyStr(rateCoverage.doubleValue(), 5) +" <br>Rate Cover Pro Rata : "+
//                                        NumberUtil.getMoneyStr(rateCover.doubleValue(), 5)+" <br>Rate Cover Batasan OJK : "+
//                                        batasBawahDisplay +" s/d "+ batasAtasDisplay + " "+ polType.getStRateMethod()+
//                                        "<br> Pro Rata Hari : "+ jumlahHari +"/365";
                                 
                                message =  message + "<table>"
                                        +"<tr>"
                                        +"<td>Rate Cover Batasan OJK</td><td>:</td><td>"+  batasBawahDisplay +" s/d "+ batasAtasDisplay + " "+ polType.getStRateMethod()+"</td>"
                                        + "</tr>"
//                                        +"<tr>"
//                                        +"<td>Rate Cover Per Tahun</td><td>:</td><td>"+  NumberUtil.getMoneyStr(rateCoverage.doubleValue(), 5) +"</td>"
//                                        + "</tr>"
                                        +"<tr>"
                                        +"<td>Rate Cover</td><td>:</td><td>"+  NumberUtil.getMoneyStr(rateCover.doubleValue(), 5) +"</td>"
                                        + "</tr>"
//                                        +"<tr>"
//                                        +"<td>Pro Rata Hari</td><td>:</td><td>"+  + jumlahHari +"/365"+" -> "+ getPolicy().getDbPeriodRateFactor() +"</td>"
//                                        + "</tr>"
                                        + "</table>";
                            } 
 
                        }else{
                            BigDecimal limitTSI = BDUtil.mul(BDUtil.getRateFromPct(batasBawah), getDbObjectInsuredAmount());
                            if(!BDUtil.isEqual(limitTSI, cover.getDbInsuredAmount(), 2)){
                                masihMasukBatas = false;
                                message = "TSI Coverage "+ cover.getInsuranceCoverage().getStDescription() + " harusnya bernilai "+ ConvertUtil.removeTrailing(String.valueOf(limitTSI)) + " ("+ ConvertUtil.removeTrailing(String.valueOf(batasBawah)) +"% from TSI)";
                                //message = message +"<br><b> Rate Cover/tahun : "+ConvertUtil.removeTrailing(String.valueOf(cover.getDbRate())) +" <br>Pro Rate : "+ ConvertUtil.removeTrailing(String.valueOf(rateCover))+" <br>Batas Bawah OJK : "+ ConvertUtil.removeTrailing(String.valueOf(h.getFieldValueByFieldNameBD("rate_lowest"+rcl))) +" <br>Batas Atas OJK : "+ ConvertUtil.removeTrailing(String.valueOf(h.getFieldValueByFieldNameBD("rate_top"+rcl)))+"</b>";

                            }

                        }

                        if(!masihMasukBatas)
                            throw new RuntimeException("Objek No. "+ objx.getStOrderNo()+ " Salah.<br>"+message);
                }

//                System.out.println("#################################################################");
                
          }

    }

    public void validateDeductibleOJK() throws Exception{

        if(policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusEndorseRI() || policy.isStatusHistory())
            return;

        final InsurancePolicyTypeView polType = policy.getPolicyType();

        if(polType.getStControlFlags()==null) return;

        boolean useKodeOkupasi = false;
        boolean useCoverageId = false;
        boolean useRiskCatCode = false;
        boolean useLowestTsi = false;
        boolean useTopTsi = false;

        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        String kodePlat = StringTools.getFirstLetterOnly(objx.getStReference1());

        String levelWilayah = getLevelWilayahPlatNo(kodePlat);

        if (polType!=null){
            useKodeOkupasi = polType.checkProperty("OKUPASI_CODE","Y");
            useCoverageId = polType.checkProperty("COVER_ID","Y");
            useRiskCatCode = polType.checkProperty("RISK_CODE","Y");
            useLowestTsi = polType.checkProperty("TSI_LOW1","Y");
            useTopTsi = polType.checkProperty("TSI_TOP1","Y");
        }

        final DTOList deductible = getDeductibles();

        //BigDecimal rateTotalPerObject = null;
        for (int i = 0; i < deductible.size(); i++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductible.get(i);

                //rateTotalPerObject = BDUtil.add(rateTotalPerObject, cover.getDbRate());

                final InsuranceRiskCategoryView rc = getRiskCategory();

                int rcl=0;

                if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());

                if(polType.getStPolicyTypeID().equalsIgnoreCase("3"))
                    rcl = Integer.parseInt(levelWilayah);

                if(rcl == 0) return;

                String riskCode = rc.getStInsuranceRiskCategoryCode();
                //29341
                //0123456
                if(riskCode.length()<= 5) riskCode = riskCode;
                else riskCode = riskCode.substring(0, 5);

                int kodeOkupasiLevel = riskCode.length();

                riskCode = "'"+ riskCode + "%'";

                /*
                SELECT *
                FROM INS_RATES_SETTING
                WHERE kd_okupasi_4_digit like '2976%' -- KODE OKUPASI
                and '27' = ANY (ins_cover_id_array) --COVERAGE ID
                AND NULL = ANY (risk_cat_code_array) --RISK KODE
                AND TSI_LOWEST1 >= 0   -- BATAS BAWAH TSI
                AND TSI_TOP1 <= 0      -- BATAS ATAS TSI
                */
                final SQLAssembler sqa = new SQLAssembler();

                sqa.addSelect("tsi_lowest1,tsi_top1,rate_lowest1,limit_by ");
                sqa.addQuery("from ins_rates_setting");

                if(useCoverageId){
                    sqa.addClause("'"+ ded.getStInsuranceClaimCauseID() + "'= ANY (ins_cover_id_array)");
                }

                sqa.addClause("pol_type_id = ?");
                sqa.addPar(policy.getStPolicyTypeID());

//                System.out.println("#################################################################");
//                System.out.println("********** deductible type : "+ ded.getStInsuranceClaimCauseID() + " | "+ ded.getStClaimCauseDesc());
//                System.out.println("********** SQL : "+sqa.getSQL());


                final String sql = sqa.getSQL();

                final DTOList l = ListUtil.getDTOListFromQuery(
                        sql,
                        sqa.getPar(),
                        HashDTO.class
                        );

                if(l.size()> 0){
                        HashDTO h = (HashDTO) l.get(0);
                        BigDecimal batasBawahTSI = h.getFieldValueByFieldNameBD("tsi_lowest1");
                        BigDecimal batasAtasTSI = h.getFieldValueByFieldNameBD("tsi_top1");
                        BigDecimal batasBawahPCT = h.getFieldValueByFieldNameBD("rate_lowest1");
                        String limitBy = h.getFieldValueByFieldNameST("limit_by");

                        boolean limitByRate = limitBy.equalsIgnoreCase("RATE");
                        boolean limitByTSI= limitBy.equalsIgnoreCase("TSI");

//                        System.out.println("**************  batas bawah tsi : "+ batasBawahTSI);
//                        System.out.println("**************  batas atas  tsi : "+ batasAtasTSI);
//                        System.out.println("**************  batas bawah  pct : "+ batasBawahPCT);

                        String message ="";
                        boolean masihMasukBatas = true;

                        if(!BDUtil.isZeroOrNull(batasBawahTSI)){
                            if(BDUtil.lesserThan(ded.getDbAmountMin(), batasBawahTSI)){
                                masihMasukBatas = false;
                                message = "Nilai Minimum DEDUCTIBLE "+ ded.getStClaimCauseDesc() +" yaitu Rp. "+ NumberUtil.getMoneyStr(batasBawahTSI.doubleValue(), 0);
                            }
                        }
                        
                        if(!BDUtil.isZeroOrNull(batasBawahTSI)){
                            if(BDUtil.lesserThan(ded.getDbPct(), batasBawahPCT)){
                                masihMasukBatas = false;
                                message = "Minimal PCT DEDUCTIBLE "+ ded.getStClaimCauseDesc() +" yaitu : "+ batasBawahPCT + "%";
                            }
                        }

                        if(!masihMasukBatas)
                            throw new RuntimeException("Objek No. "+ objx.getStOrderNo()+ " Salah.<br>"+message);
                }

                //System.out.println("#################################################################");

          }

        validateTimeExcess();
    }

    public InsurancePolicyCoverView getMainCoverage() throws Exception{
        final DTOList coverage = getCoverage();

        InsurancePolicyCoverView mainCover = new InsurancePolicyCoverView();

        for (int i = 0; i < coverage.size(); i++){
            InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(i);

            if(cover.getStCoverCategory().equalsIgnoreCase("MAIN")){
                 mainCover = cover;
                 break; 
            }
        }

        return mainCover;
    }

    public String getLevelWilayahPlatNo(String stVsCode) throws Exception {
        
        if(policy.isStatusRenewal())
            if(stVsCode.toUpperCase().equalsIgnoreCase("TBA"))
                throw new RuntimeException("Polis renewal tidak boleh TBA");
        
        if(stVsCode.toUpperCase().equalsIgnoreCase("TBA"))
            return policy.getCostCenter(policy.getStCostCenterCode()).getStMachineVehicleZone();

        final SQLUtil S = new SQLUtil();
 
        try {
            S.setQuery(
                    "   select " +
                    "     ref1 " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_MV_PLAT' and vs_code = ?");

            S.setParam(1,stVsCode.toUpperCase());

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString(1);

            return null;
        }finally{
            S.release();
        }
    }

    public void applyNewRiskCode(Date dtPeriodStart) throws Exception{
        final SQLUtil S = new SQLUtil();
        String ins_risk_cat_id = null;
        try {
                S.setQuery(
                        " select * "+
                        " from ins_risk_cat "+
                        " where poltype_id = ? and ins_risk_cat_code = ? "+
                        " and period_start <= ? and period_end >= ?");

                S.setParam(1,policy.getStPolicyTypeID());
                S.setParam(2,getRiskCategory().getStInsuranceRiskCategoryCode());
                S.setParam(3,dtPeriodStart);
                S.setParam(4,dtPeriodStart);

                final ResultSet RS = S.executeQuery();

                if (RS.next()) ins_risk_cat_id = RS.getString("ins_risk_cat_id");

        }finally{
            S.release(); 
        }

        if(ins_risk_cat_id == null){
            //setStRiskCategoryID(null);
            //throw new RuntimeException("Kode resiko terbaru tidak ditemukan");
            return;
        }

        if(ins_risk_cat_id.equalsIgnoreCase("null")) throw new RuntimeException("Kode resiko terbaru tidak ditemukan");

        setStRiskCategoryID(ins_risk_cat_id);

    }

    public InsuranceRiskCategoryView getRiskCategoryMaiparkByDate(Date periodStart) throws Exception {

        final InsuranceRiskCategoryView cat = (InsuranceRiskCategoryView)ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_risk_cat "+
                " where poltype_id = 99 and ins_risk_cat_code = ? "+
                " and period_start <= ? and period_end >= ?",
                new Object [] {getRiskCategoryMaipark().getStInsuranceRiskCategoryCode(), periodStart, periodStart},
                InsuranceRiskCategoryView.class
                ).getDTO();

        return cat;
    }

    public String applyNewZoneEQ(Date dtPeriodStart) throws Exception{
        final SQLUtil S = new SQLUtil();
        String ins_risk_cat_id = null;
        try {
                S.setQuery(
                        " select * "+
                        " from ins_risk_cat "+
                        " where poltype_id = 99 and ins_risk_cat_code = ? "+
                        " and period_start <= ? and period_end >= ?");

                S.setParam(1,getRiskCategoryMaipark().getStInsuranceRiskCategoryCode());
                S.setParam(2,dtPeriodStart);
                S.setParam(3,dtPeriodStart);

                final ResultSet RS = S.executeQuery();

                if (RS.next()) ins_risk_cat_id = RS.getString("ins_risk_cat_id");

        }finally{
            S.release();
        }

        if(ins_risk_cat_id == null) throw new RuntimeException("Kode resiko gempa terbaru tidak ditemukan");

        if(ins_risk_cat_id.equalsIgnoreCase("null")) throw new RuntimeException("Kode resiko gempa terbaru tidak ditemukan");

        return ins_risk_cat_id;

    }
    
    
    public BigDecimal getDbTreatyLimitRatioByParam(String risk_cat_id) {
        BigDecimal tlr = getDbTreatyLimitRatio1ByParam(risk_cat_id);
        
        if (tlr==null) return new BigDecimal(100);
        
        return tlr;
    }
    
    public BigDecimal getDbTreatyLimitRatio1ByParam(String risk_cat_id) {
        final BigDecimal a100 = new BigDecimal(100);
        
        if (getStRiskCategoryID()==null) return a100;
        
        //final InsuranceRiskCategoryView rc = getRiskCategoryByParam(risk_cat_id);
        DTOList riskCateg = getRiskCat(risk_cat_id);

        InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);

//        logger.logWarning("########## KODE RESIKO NIH : "+rc.getStInsuranceRiskCategoryCode());
//        logger.logWarning("########## KODE RESIKO NIH : "+rc.getStInsuranceRiskCategoryID());
//        logger.logWarning("########## KODE RESIKO NIH limit 1 : "+rc.getDbTreatyLimit1());
//        logger.logWarning("########## KODE RESIKO NIH limit 2 : "+rc.getDbTreatyLimit2());
//        logger.logWarning("########## KODE RESIKO NIH limit 3 : "+rc.getDbTreatyLimit3());
        
        if (rc==null) return a100;
        
        int rcl=0;
        
        if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());
        
        switch (rcl) {
            case 0: return rc.getDbTreatyLimit0();
            case 1: return rc.getDbTreatyLimit1();
            case 2: return rc.getDbTreatyLimit2();
            case 3: return rc.getDbTreatyLimit3();
            default:
                throw new RuntimeException("Invalid Risk Class : "+getStRiskClass());
        }
    }
    
    public InsuranceRiskCategoryView getRiskCategoryByParam(String risk_cat_id) {
        return (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, risk_cat_id);
    }

    public void applyHighestRiskCode() throws Exception {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        if(objx.getStRiskCategoryCode1()==null && objx.getStRiskCategoryCode2()==null && objx.getStRiskCategoryCode3()==null)
            return;

        if(policy.isStatusClaim() || policy.isStatusHistory())
            return;

        int levelRiskCode1 = 101;
        int levelRiskCode2 = 101;
        int levelRiskCode3 = 101;

        BigDecimal rateRiskCode1 = BDUtil.zero;
        BigDecimal rateRiskCode2 = BDUtil.zero;
        BigDecimal rateRiskCode3 = BDUtil.zero;

        if(objx.getStRiskCategoryCode1()!=null){
            levelRiskCode1 = Integer.parseInt(String.valueOf(getDbTreatyLimitRatioByParam(objx.getStRiskCategoryCode1())));
            rateRiskCode1 = getDbRateOJK(objx.getStRiskCategoryCode1());
        }
        if(objx.getStRiskCategoryCode2()!=null){
            levelRiskCode2 = Integer.parseInt(String.valueOf(getDbTreatyLimitRatioByParam(objx.getStRiskCategoryCode2())));
            rateRiskCode2 = getDbRateOJK(objx.getStRiskCategoryCode2());
        }
        if(objx.getStRiskCategoryCode3()!=null){
            levelRiskCode3 = Integer.parseInt(String.valueOf(getDbTreatyLimitRatioByParam(objx.getStRiskCategoryCode3())));
            rateRiskCode3 = getDbRateOJK(objx.getStRiskCategoryCode3());
        }

//        System.out.println("################ BANDINGKAN RISK CODE ##################");
//        System.out.println("################ levelRiskCode1: " + levelRiskCode1 +" RATE : "+rateRiskCode1);
//        System.out.println("################ levelRiskCode2: " + levelRiskCode2+" RATE : "+rateRiskCode2);
//        System.out.println("################ levelRiskCode3: " + levelRiskCode3+" RATE : "+rateRiskCode3);
//        System.out.println("################ BANDINGKAN RISK CODE ##################");

        String highestRiskCode = null;

        if (levelRiskCode1 < levelRiskCode2 && levelRiskCode1 < levelRiskCode3) {
//            System.out.println("First number is lowest.");

            DTOList riskCateg = getRiskCat(objx.getStRiskCategoryCode1());
            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        } else if (levelRiskCode2 < levelRiskCode1 && levelRiskCode2 < levelRiskCode3) {
//            System.out.println("Second number is lowest.");
            DTOList riskCateg = getRiskCat(objx.getStRiskCategoryCode2());
            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        } else if (levelRiskCode3 < levelRiskCode1 && levelRiskCode3 < levelRiskCode2) {
//            System.out.println("Third number is lowest.");
            DTOList riskCateg = getRiskCat(objx.getStRiskCategoryCode3());
            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        } else if(levelRiskCode1 == levelRiskCode2 &&  levelRiskCode1 < levelRiskCode3){
//            System.out.println("1 & 2 sama, kurang dari 3");
            if(BDUtil.biggerThan(rateRiskCode1, rateRiskCode2))
                highestRiskCode = objx.getStRiskCategoryCode1();
            else
                highestRiskCode = objx.getStRiskCategoryCode2();

//            logger.logWarning("kode tertinggi : "+highestRiskCode);

            DTOList riskCateg = getRiskCat(highestRiskCode);

            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        } else if(levelRiskCode1 == levelRiskCode3 &&  levelRiskCode1 < levelRiskCode2){
//            System.out.println("1 & 3 sama, kurang dari 2");
            if(BDUtil.biggerThan(rateRiskCode1, rateRiskCode3))
                highestRiskCode = objx.getStRiskCategoryCode1();
            else
                highestRiskCode = objx.getStRiskCategoryCode3();

//            logger.logWarning("kode tertinggi : "+highestRiskCode);
            DTOList riskCateg = getRiskCat(highestRiskCode);

            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        }else if(levelRiskCode2 == levelRiskCode3 &&  levelRiskCode2 < levelRiskCode1){
//            System.out.println("2 & 3 sama, kurang dari 1");
            if(BDUtil.biggerThan(rateRiskCode2, rateRiskCode3))
                highestRiskCode = objx.getStRiskCategoryCode2();
            else
                highestRiskCode = objx.getStRiskCategoryCode3();

            DTOList riskCateg = getRiskCat(highestRiskCode);

            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
      }else if(levelRiskCode1 == levelRiskCode2 &&  levelRiskCode1 == levelRiskCode3){

            double maxValue = Math.max(rateRiskCode1.doubleValue(),Math.max(rateRiskCode2.doubleValue(),rateRiskCode3.doubleValue()));

            if(Tools.isEqual(rateRiskCode1.doubleValue(), maxValue)) highestRiskCode = objx.getStRiskCategoryCode1();
            else if(Tools.isEqual(rateRiskCode2.doubleValue(), maxValue)) highestRiskCode = objx.getStRiskCategoryCode2();
            else if(Tools.isEqual(rateRiskCode3.doubleValue(), maxValue)) highestRiskCode = objx.getStRiskCategoryCode3();

            DTOList riskCateg = getRiskCat(highestRiskCode);

            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        }else{
            DTOList riskCateg = getRiskCat(objx.getStRiskCategoryCode1());
            final InsuranceRiskCategoryView rc = (InsuranceRiskCategoryView) riskCateg.get(0);
            setStRiskCategoryID(rc.getStInsuranceRiskCategoryID());
        }
    }
    
    public void validateBolehKomisi(InsurancePolicyObjDefaultView obj) throws Exception {

        String nama = "";
        String jenis = policy.getStPolicyTypeID();
        if(jenis.equalsIgnoreCase("3"))
            if(obj.getStReference9()!=null)
                nama = obj.getStReference9().toUpperCase();

        if(jenis.equalsIgnoreCase("1") || jenis.equalsIgnoreCase("81"))
            if(obj.getStReference11()!=null)
                nama = obj.getStReference11().toUpperCase();

        DateTime bulanOJKMV = new DateTime(DateUtil.getDate("01/03/2014"));
        DateTime bulanOJKHartaBenda = new DateTime(DateUtil.getDate("01/02/2014"));
        DateTime periodeAwal = new DateTime(policy.getDtPeriodStart());


        //cek kesamaan nama sumbis dgn insured name
        //if(!nama.equalsIgnoreCase("")){
            //if(nama.equalsIgnoreCase(policy.getEntity().getStEntityName().toUpperCase())){
                final DTOList details = policy.getDetails();

                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                    if(!nama.equalsIgnoreCase(""))
                        if(item.isComission())
                            if(nama.equalsIgnoreCase(item.getEntity().getStEntityName().toUpperCase()))
                                throw new RuntimeException("Tertanggung tidak boleh menerima komisi/fee base");

                    if(item.isDiscount()){
                        if(jenis.equalsIgnoreCase("3")){

                            if(!policy.isStatusRenewal())
                                if(periodeAwal.isAfter(bulanOJKMV))
                                    throw new RuntimeException("Polis tidak boleh terdapat diskon");

                            if(!policy.isStatusRenewal()) throw new RuntimeException("Diskon hanya boleh saat polis perpanjangan");

                            if(policy.isStatusRenewal()){
                                if(!canClaimAgain(obj.getStPolicyObjectID()))
                                    throw new RuntimeException("Objek sudah pernah klaim sebelumnya, tidak boleh diskon");
                            }

                        }else if(jenis.equalsIgnoreCase("1") || jenis.equalsIgnoreCase("81")){
                            if(!policy.isStatusRenewal())
                                if(periodeAwal.isAfter(bulanOJKHartaBenda))
                                    throw new RuntimeException("Polis tidak boleh terdapat diskon");

                            if(!policy.isStatusRenewal()) throw new RuntimeException("Diskon hanya boleh saat polis perpanjangan");

                            if(policy.isStatusRenewal()){
                                if(!canClaimAgain(obj.getStPolicyObjectID()))
                                    throw new RuntimeException("Objek sudah pernah klaim sebelumnya, tidak boleh diskon");
                            }
                        }


                    }

                }
            //}
        //}
        
    }

    public void validateTimeExcess() throws Exception{


        final InsurancePolicyTypeView polType = policy.getPolicyType();

        if(polType.getStControlFlags()==null) return;

        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        boolean useKodeOkupasi = false;
        boolean useCoverageId = false;
        boolean useRiskCatCode = false;
        boolean useLowestTsi = false;
        boolean useTopTsi = false;


        String kodePlat = StringTools.getFirstLetterOnly(objx.getStReference1());

        String levelWilayah = getLevelWilayahPlatNo(kodePlat);

        if (polType!=null){
            useKodeOkupasi = polType.checkProperty("OKUPASI_CODE","Y");
            useCoverageId = polType.checkProperty("COVER_ID","Y");
            useRiskCatCode = polType.checkProperty("RISK_CODE","Y");
            useLowestTsi = polType.checkProperty("TSI_LOW1","Y");
            useTopTsi = polType.checkProperty("TSI_TOP1","Y");
        }

        DateTime tanggalAwal = new DateTime(policy.getDtPeriodStart());
        DateTime tanggalAkhir = new DateTime(policy.getDtPeriodEnd());

        BigDecimal timeExcess = BDUtil.zero;
        String excessUnit = "";

        final DTOList deductible = getDeductibles();

        //BigDecimal rateTotalPerObject = null;
        for (int i = 0; i < deductible.size(); i++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductible.get(i);

                if(ded.getClaimCause().getStDescription()!=null)
                    if(!ded.getClaimCause().getStDescription().toUpperCase().contains("INTERRUPTION")) continue;

                final InsuranceRiskCategoryView rc = getRiskCategory();

                int rcl=0;

                if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());

                if(polType.getStPolicyTypeID().equalsIgnoreCase("3"))
                    rcl = Integer.parseInt(levelWilayah);

                if(rcl == 0) return;

                String riskCode = rc.getStInsuranceRiskCategoryCode();

                /*
                SELECT *
                FROM INS_RATES_SETTING
                WHERE kd_okupasi_4_digit like '2976%' -- KODE OKUPASI
                and '27' = ANY (ins_cover_id_array) --COVERAGE ID
                AND NULL = ANY (risk_cat_code_array) --RISK KODE
                AND TSI_LOWEST1 >= 0   -- BATAS BAWAH TSI
                AND TSI_TOP1 <= 0      -- BATAS ATAS TSI
                */
                final SQLAssembler sqa = new SQLAssembler();

                sqa.addSelect("time_excess, excess_unit, unit, limit_by, rate_lowest"+ rcl+", rate_top"+rcl);
                sqa.addQuery("from ins_rates_setting");

                String kodeOkupasiParameter = null;

                String param5digit = "";
                String param4digit = "";
                String param3digit = "";
                String param2digit = "";

                //29341
                //01234

                if(riskCode.length()==4 ) riskCode = riskCode + "X";
                if(riskCode.length()==3 ) riskCode = riskCode + "XX";
                if(riskCode.length()==2 ) riskCode = riskCode + "XXX";

                param5digit = riskCode;
                param4digit = riskCode.substring(0, 4);
                param3digit = riskCode.substring(0, 3);
                param2digit = riskCode.substring(0, 2);

                if(useKodeOkupasi){
                    kodeOkupasiParameter = riskCode;
                    //sqa.addClause(" (kd_okupasi_"+ kodeOkupasiLevel +"_digit like "+ kodeOkupasiParameter+" or (kd_okupasi_"+ kodeOkupasiLevel +"_digit) is null) ");
                     sqa.addClause("(kd_okupasi = '"+ param5digit +"' or kd_okupasi = '"+ param4digit +"' or kd_okupasi = '"+ param3digit +"' or kd_okupasi = '"+ param2digit +"' or kd_okupasi is null)");
                }

                sqa.addClause("pol_type_id = ?");
                sqa.addPar(policy.getStPolicyTypeID());

                sqa.addClause("time_excess is not null");

//                System.out.println("#################################################################");

                String sql = sqa.getSQL();

                if(useKodeOkupasi)
                    sql = sqa.getSQL() + " order by length(kd_okupasi) desc limit 1 ";

                final DTOList l = ListUtil.getDTOListFromQuery(
                        sql,
                        sqa.getPar(),
                        HashDTO.class
                        );

                if(l.size()> 0){
                        HashDTO h = (HashDTO) l.get(0);
                        BigDecimal batasBawah = h.getFieldValueByFieldNameBD("rate_lowest"+rcl);
                        BigDecimal batasAtas = h.getFieldValueByFieldNameBD("rate_top"+rcl);
                        String limitBy = h.getFieldValueByFieldNameST("limit_by");
                        String unit = h.getFieldValueByFieldNameST("unit");

                        boolean limitByRate = limitBy.equalsIgnoreCase("RATE");
                        boolean limitByTSI = limitBy.equalsIgnoreCase("TSI");

                        timeExcess = h.getFieldValueByFieldNameBD("time_excess");
                        excessUnit = h.getFieldValueByFieldNameST("excess_unit");

                        DateTime periodeAwal = new DateTime(policy.getDtPeriodStart());
                        DateTime periodeAkhir = new DateTime(policy.getDtPeriodEnd());
                        Days day = Days.daysBetween(periodeAwal, periodeAkhir);
                        int jumlahHari = day.getDays();

                        BigDecimal batasBawahDisplay = null;
                        BigDecimal batasAtasDisplay = null;
                        if(!polType.getStRateMethod().equalsIgnoreCase(unit)){
                            if(polType.getStRateMethod().equalsIgnoreCase("%")){
                                batasBawah = BDUtil.getPctFromMile(batasBawah);
                            }else if(polType.getStRateMethod().equalsIgnoreCase("PMIL")){
                                batasBawah = BDUtil.getMileFromPCT(batasBawah);
                                batasAtas = BDUtil.getMileFromPCT(batasAtas);
                            }
                        }

                        batasBawahDisplay = batasBawah;
                        batasAtasDisplay = batasAtas;


                        batasAtas = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasAtas);
                        batasBawah = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasBawah);

                        //System.out.println("**************  limit by  : "+ limitBy);
//                        System.out.println("**************  jumlah hari  : "+ jumlahHari);
//                        System.out.println("**************  batas bawah pro rata : "+ batasBawah);
//                        System.out.println("**************  batas atas pro rata : "+ batasAtas);

                        String message ="";
                        boolean masihMasukBatas = true;

                        if(!BDUtil.isZeroOrNull(timeExcess)){
                            if(BDUtil.lesserThan(ded.getDbTimeExcess(), timeExcess)){
                                masihMasukBatas = false;
                                message = "Minimal time excess "+ ded.getStClaimCauseDesc() +" yaitu "+ timeExcess + " "+ excessUnit;
                            }
                        }


                        if(!masihMasukBatas)
                            throw new RuntimeException(message);
                }

//                System.out.println("#################################################################");

          }

    }



    public boolean canClaimAgain(String insPolObjId) throws Exception {
        final SQLUtil S = new SQLUtil();

        boolean canClaim = true;
        try {
            final PreparedStatement PS = S.setQuery("select claim_loss_id " +
                    "FROM ins_pol_obj " +
                    "WHERE ins_pol_obj_ref_id = ? limit 1");

            PS.setString(1, insPolObjId);

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                final String lossID = RS.getString("claim_loss_id");

                if(lossID!=null){
                    canClaim = false;
                }else{
                    canClaim = true;
                }
            }

            return canClaim;

        } finally {
            S.release();
        }
    }

    public DTOList riskCat;

    public DTOList getRiskCat(String riskCode) {
        loadRiskCat(riskCode);
        return riskCat;
    }

    public void loadRiskCat(String riskCode) {
        try {
            //if (riskCat == null)
                riskCat = ListUtil.getDTOListFromQuery(
                        "select * from ins_risk_cat where active_flag ='Y' and poltype_id = ? and ins_risk_cat_code = ?",
                        new Object[]{policy.getStPolicyTypeID(), riskCode},
                        InsuranceRiskCategoryView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getDbRateOJK(String stKodeResiko) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement PS = S.setQuery("select rate_lowest1 "+
                                        " from ins_rates_setting "+
                                        " where pol_type_id = ? and kd_okupasi = ?");

            PS.setString(1, policy.getStPolicyTypeID());
            PS.setString(2, stKodeResiko);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) return RS.getBigDecimal(1);

            return null;

        } finally {
            S.release();
        }
    }

    private PeriodBaseView getPeriodBase() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, objx.getStPeriodBaseID());
    }
    
    public BigDecimal getDbPeriodRateFactorObject() {
        
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        
        final PeriodBaseView periodBase = getPeriodBase();
        
        if (periodBase == null) return null;
        
        final BigDecimal baseu = periodBase.getDbBaseUnit();
        
        if (objx.getDbPeriodRate() == null || baseu == null) return null;
        
        return objx.getDbPeriodRate() .divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }
    
    public BigDecimal getDbPremiumFactorValue() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        
        if (objx.getStPremiumFactorID() == null) return BDUtil.one;
        return getPremiumFactor().getDbPremiumFactor();
    }
    
    public InsurancePremiumFactorView getPremiumFactor() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        return (InsurancePremiumFactorView) DTOPool.getInstance().getDTO(InsurancePremiumFactorView.class, objx.getStPremiumFactorID());
    }
    
    public String getDbPeriodRateDesc() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        final BigDecimal baseu = getPeriodBase().getDbBaseUnit();
        
        String pr = String.valueOf(objx.getDbPeriodRate());
        
        pr = ConvertUtil.removeTrailing(pr);
        
        if (baseu.longValue() == 100) return pr + " %";
        
        return pr + " / " + baseu;
    }
    
    public String getStPremiumFactorDesc() {
        
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        
        if (objx.getStPremiumFactorID() == null) return "100%";
        
        return getPremiumFactor().getStPremiumFactorDesc();
    }
    
    private PeriodBaseView getPeriodBaseBefore() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        
        return (PeriodBaseView) DTOPool.getInstance().getDTO(PeriodBaseView.class, objx.getStPeriodBaseBeforeID());
    }

    public BigDecimal getDbPeriodRateBeforeFactor() {

        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        final PeriodBaseView pbb = getPeriodBaseBefore();

        if (pbb == null) return null;

        final BigDecimal baseu = pbb.getDbBaseUnit();

        if (objx.getDbPeriodRateBefore() == null || baseu == null) return null;

        return objx.getDbPeriodRateBefore().divide(baseu, 15, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getStPeriodRateBeforeDesc() {
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        final PeriodBaseView periodBaseBefore = getPeriodBaseBefore();

        if (periodBaseBefore == null) return null;

        final BigDecimal baseu = periodBaseBefore.getDbBaseUnit();
        String be4 = String.valueOf(objx.getDbPeriodRateBefore());

        be4 = ConvertUtil.removeTrailing(be4);

        if (baseu.longValue() == 100) {
            return be4 + " %";
        }

        return be4 + " / " + baseu;

    }

    public void reCalculate() throws Exception{
        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;
        
        if(objx.isUsingPeriodFactorPerObject()) reCalculatePeriodFactorPerObject();
        else reCalculateNOW();
    }


    public void reCalculatePeriodFactorPerObject() throws Exception{
    //PERHITUNGAN PERIOD FACTOR PER OBJEK
        try {
            loadSumInsureds();
            loadCoverage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(!getPolicy().getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;

        if (getDbObjectPremiRate()!=null) {
            setDbObjectPremiAmount(BDUtil.mul(getDbObjectInsuredAmount(), getDbObjectPremiRate(),scale));
        }

        setDbObjectPremiTotalAmount(getDbObjectPremiAmount());
        setDbObjectPremiTotalBeforeCoinsuranceAmount(getDbObjectPremiTotalAmount());

        setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), BDUtil.zero));

        setDbObjectInsuredAmount(null);

        if (suminsureds!=null)
            for (int i = 0; i < suminsureds.size(); i++) {
                InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(i);

                setDbObjectInsuredAmount(BDUtil.add(getDbObjectInsuredAmount(), itsi.getDbInsuredAmount()));

            }

        BigDecimal periodRate = getDbPeriodRateFactorObject();

        BigDecimal totalrate = null;
        BigDecimal tsiDiff = null;
        if (coverage!=null)
            for (int i = 0; i < coverage.size(); i++) {
            InsurancePolicyCoverView icv = (InsurancePolicyCoverView) coverage.get(i);

            if (!icv.isEntryInsuredAmount()){
                icv.setDbInsuredAmount(getDbObjectInsuredAmount());
            }

            BigDecimal rateActual = new BigDecimal(0);
            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual = icv.getDbRatePct();
            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual = icv.getDbRateMile();

            totalrate = BDUtil.add(totalrate,rateActual);

            if (icv.isEntryRate()) {
                icv.setDbPremi(BDUtil.mul(rateActual, icv.getDbInsuredAmount(),scale));
                icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRate,scale));
                icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), getDbPremiumFactorValue(),scale));
                icv.setDbPremiNew(icv.getDbPremi());
                icv.setStCalculationDesc(null);

                if (icv.getDbPremi().longValue()!=0) {

                    final StringBuffer szc = new StringBuffer();

                    szc.append(p(icv.getDbInsuredAmount())+" x "+p(icv.getDbRate())+getPolicy().getStRateMethodDesc());
                    if (!isOne(getDbPeriodRateFactorObject()))
                        szc.append(" x "+ getDbPeriodRateDesc());
                    if (!isOne(getDbPremiumFactorValue()))
                        szc.append(" x "+ getStPremiumFactorDesc());

                    icv.setStCalculationDesc(szc.toString());
                }
            } else {
                icv.setDbPremi(icv.getDbPremiNew());
            }

            //if (icv.isEntryRate())
            if (policy.isStatusEndorse()||policy.isStatusEndorseRI()) {
             //{

                icv.setStEntryRateFlag(icv.getStEntryRateFlag());
                
                if (icv.getStInsurancePolicyCoverRefID()!=null){
                    final InsurancePolicyCoverView refCover = icv.getRefCover();

                    final BigDecimal insAmountDiff = BDUtil.subScale2(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());

                    final BigDecimal insRateDiff = BDUtil.sub(icv.getDbRate(),refCover.getDbRate());

                    BigDecimal periodRateEndorse = getDbPeriodRateBeforeFactor();

                    tsiDiff = BDUtil.add(tsiDiff, insAmountDiff);

                    setDbObjectInsuredAmountShareEndorse(insAmountDiff);

                    //if (icv.isEntryRate()) {
                    if (!icv.isEntryPremi()) {

                    if (insAmountDiff.longValue()!=0) {
                        //logger.logDebug("KENAIKAN TSI");
                        //BigDecimal m = BDUtil.mul(icv.getDbRatePct(), insAmountDiff);
                        BigDecimal rateActual2 = new BigDecimal(0);
                        if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual2 = icv.getDbRatePct();
                        else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual2 = icv.getDbRateMile();

                        BigDecimal m = BDUtil.mul(rateActual2, insAmountDiff,scale);

                        m = BDUtil.mul(m, getDbPeriodRateBeforeFactor(),scale);
                        m = BDUtil.mul(m, policy.getParentPolicy().getDbPremiumFactorValue(),scale);

                        if (m.longValue()!=0) {

                            BigDecimal rateActual3 = new BigDecimal(0);
                            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = icv.getDbRatePct();
                            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = icv.getDbRateMile();

                            if(insRateDiff.longValue()!=0){
                                //rateActual3 = insRateDiff;
                                if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = BDUtil.div(insRateDiff, BDUtil.hundred,10);
                                else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = BDUtil.div(insRateDiff, BDUtil.thousand,10);
                            }

                            icv.setDbPremi(BDUtil.mul(insAmountDiff,rateActual3,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRateEndorse,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getParentPolicy().getDbPremiumFactorValue(),scale));

                            //icv.setDbPremiNew(BDUtil.mul(icv.getDbPremiNew(),policy.getParentPolicy().getStPremiumFactorDesc()));
                            icv.setDbPremiNew(icv.getDbPremi());

                            final String curCalc = icv.getStCalculationDesc();

                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(p(insAmountDiff)+" x ");
                            szCalc.append(p(icv.getDbRate())+getPolicy().getStRateMethodDesc());

                            if (!isOne(getDbPeriodRateBeforeFactor()))
                                szCalc.append(" x "+ getStPeriodRateBeforeDesc());

                            if (!isOne(policy.getParentPolicy().getDbPremiumFactorValue()))
                                szCalc.append(" x "+policy.getParentPolicy().getStPremiumFactorDesc());

                            String calc = szCalc.toString();


                            icv.setStCalculationDesc(calc);
                        }
                    }else if(BDUtil.biggerThanZero(insRateDiff)||BDUtil.lesserThanZero(insRateDiff)){
                        BigDecimal rateActual2 = new BigDecimal(0);
                        if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual2 = icv.getDbRatePct();
                        else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual2 = icv.getDbRateMile();

                        BigDecimal m = BDUtil.mul(rateActual2, refCover.getDbInsuredAmount(),scale);

                        m = BDUtil.mul(m, policy.getDbPeriodRateBeforeFactor(),scale);
                        m = BDUtil.mul(m, policy.getParentPolicy().getDbPremiumFactorValue(),scale);
                        //logger.logDebug("Kenaikan Rate= "+insRateDiff);
                        if (m.longValue()!=0) {

                            BigDecimal rateActual3 = new BigDecimal(0);
                            if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT)) rateActual3 = BDUtil.getRateFromPct(insRateDiff);
                            else if(getPolicy().getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL)) rateActual3 = BDUtil.getRateFromMile(insRateDiff);

                            icv.setDbPremi(BDUtil.mul(refCover.getDbInsuredAmount(),rateActual3,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), periodRateEndorse,scale));
                            icv.setDbPremi(BDUtil.mul(icv.getDbPremi(), policy.getParentPolicy().getDbPremiumFactorValue(),scale));
                            icv.setDbPremiNew(icv.getDbPremi());

                            final String curCalc = icv.getStCalculationDesc();

                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(p(refCover.getDbInsuredAmount())+" x ");
                            szCalc.append(p(insRateDiff)+getPolicy().getStRateMethodDesc());

                            if (!isOne(getDbPeriodRateBeforeFactor()))
                                szCalc.append(" x "+ getStPeriodRateBeforeDesc());

                            if (!isOne(policy.getParentPolicy().getDbPremiumFactorValue()))
                                szCalc.append(" x "+policy.getParentPolicy().getStPremiumFactorDesc());

                            String calc = szCalc.toString();

                            icv.setStCalculationDesc(calc);
                        }
                    }else if(BDUtil.isZero(insAmountDiff)&&BDUtil.isZero(insRateDiff)){
                        icv.setDbPremi(BDUtil.zero);
                        icv.setDbPremiNew(BDUtil.zero);
                        icv.setStCalculationDesc(null);
                    }else{
                        icv.setDbPremi(icv.getDbPremi());
                        icv.setDbPremiNew(icv.getDbPremiNew());
                        icv.setStCalculationDesc(icv.getStCalculationDesc());
                    }
                   }

                if(icv.isEntryPremi()){
                    icv.setDbPremi(icv.getDbPremi());
                    icv.setDbPremiNew(icv.getDbPremiNew());
                    icv.setStCalculationDesc(icv.getStCalculationDesc());
                }
               }else if(icv.getStInsurancePolicyCoverRefID()==null){
                    setDbObjectInsuredAmountShareEndorse(icv.getDbInsuredAmount());
               }
             //}
                //setDbObjectInsuredAmountShareEndorse(tsiDiff);
            }

            setDbObjectPremiTotalAmount(BDUtil.add(getDbObjectPremiTotalAmount(), icv.getDbPremi()));
            setDbObjectPremiTotalBeforeCoinsuranceAmount(getDbObjectPremiTotalAmount());
          }
    }

    public abstract String getStRiskCategoryCode1();

    public abstract void setStRiskCategoryCode1(String stRiskCategoryCode1);

    public void validateHighestRateOJK() throws Exception{

        if(policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusEndorseRI() || policy.isStatusHistory())
            return;

        final InsurancePolicyTypeView polType = policy.getPolicyType();

        if(polType.getStControlFlags()==null) return;

        InsurancePolicyObjDefaultPreEndorseView objx = (InsurancePolicyObjDefaultPreEndorseView) this;

        //validateBolehKomisi(objx);

        boolean useKodeOkupasi = false;
        boolean useCoverageId = false;
        boolean useRiskCatCode = false;
        boolean useLowestTsi = false;
        boolean useTopTsi = false;

        String kodePlat = StringTools.getFirstLetterOnly(objx.getStReference1().toUpperCase());

        if(objx.getStReference1().toUpperCase().trim().equalsIgnoreCase("TBA"))
            kodePlat = objx.getStReference1().toUpperCase().trim();

        String levelWilayah = getLevelWilayahPlatNo(kodePlat);

        boolean cekBatasAtas = true;

        if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){
            int tahunSekarang = Integer.parseInt(DateUtil.getYear(objx.getDtReference1()));
            int tahunPembuatan = Integer.parseInt(objx.getStReference3());
            int usiaKendaraan = tahunSekarang - tahunPembuatan;

            if( usiaKendaraan > 5) cekBatasAtas = false;
        }

        if (polType!=null){
            useKodeOkupasi = polType.checkProperty("OKUPASI_CODE","Y");
            useCoverageId = polType.checkProperty("COVER_ID","Y");
            useRiskCatCode = polType.checkProperty("RISK_CODE","Y");
            useLowestTsi = polType.checkProperty("TSI_LOW1","Y");
            useTopTsi = polType.checkProperty("TSI_TOP1","Y");
        }

        DateTime tanggalAwal = new DateTime(policy.getDtPeriodStart());
        DateTime tanggalAkhir = new DateTime(policy.getDtPeriodEnd());

        BigDecimal timeExcess = BDUtil.zero;
        String excessUnit = "";

        BigDecimal tsiObject = getDbObjectInsuredAmount();

        //if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){
            final DTOList suminsured = getSuminsureds();

            for (int i = 0; i < suminsured.size(); i++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsured.get(i);

//                System.out.println("########### MASUK TSI #################");
//
//                System.out.println("########### cat id = "+ tsi.getStInsuranceTSIID());
//                System.out.println("########### desc =  "+ tsi.getStTSICategoryDescription());
//                System.out.println("########### MASUK TSI #################");

                if(tsi.getStInsuranceTSIID().equalsIgnoreCase("110"))
                    tsiObject = tsi.getDbInsuredAmount();

                if(tsi.getStInsuranceTSIID().equalsIgnoreCase("108")){
                    if(tsi.getStTSICategoryDescription()==null)
                        throw new RuntimeException("Deskripsi Kategori TSI tidak boleh kosong jika Others");

                    if(tsi.getStTSICategoryDescription()!=null)
                        if(tsi.getStTSICategoryDescription().equalsIgnoreCase(""))
                            throw new RuntimeException("Deskripsi Kategori TSI tidak boleh kosong jika Others");
                }
            }

        final DTOList coverage = getCoverage();

        for (int i = 0; i < coverage.size(); i++) {
                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(i);


                final InsuranceRiskCategoryView rc = getRiskCategory();

                int rcl=0;

                if (getStRiskClass()!=null) rcl=Integer.parseInt(getStRiskClass());

                if(polType.getStPolicyTypeID().equalsIgnoreCase("3")){

                    if(levelWilayah==null)
                        throw new RuntimeException("Kendaraan no ("+ objx.getStOrderNo()+") "+objx.getStReference1().toUpperCase() + " salah! Kode plat no "+ kodePlat +" tidak ditemukan, cek kembali no plat kendaraan");

                    rcl = Integer.parseInt(levelWilayah);
                }


                if(rcl == 0) return;

                String riskCode = rc.getStInsuranceRiskCategoryCode();

                final SQLAssembler sqa = new SQLAssembler();

                sqa.addSelect("kd_okupasi,time_excess, excess_unit, unit, limit_by, rate_lowest"+ rcl+", rate_top"+rcl);
                sqa.addQuery("from ins_rates_setting");

                String kodeOkupasiParameter = null;

                String param5digit = "";
                String param4digit = "";
                String param3digit = "";
                String param2digit = "";

                if(riskCode.length()==4 ) riskCode = riskCode + "X";
                if(riskCode.length()==3 ) riskCode = riskCode + "XX";
                if(riskCode.length()==2 ) riskCode = riskCode + "XXX";

                param5digit = riskCode;
                param4digit = riskCode.substring(0, 4);
                param3digit = riskCode.substring(0, 3);
                param2digit = riskCode.substring(0, 2);

                String kodeOkupasi1 = objx.getStRiskCategoryCode1();
                String kodeOkupasi2 = objx.getStRiskCategoryCode2();
                String kodeOkupasi3 = objx.getStRiskCategoryCode3();

                if(useKodeOkupasi){
                        kodeOkupasiParameter = riskCode;
                        //(kd_okupasi = '2946' or kd_okupasi = '2934')
                        String clauseOkupasi =  "(kd_okupasi = '"+ kodeOkupasi1 +"'";

                        if(kodeOkupasi2!=null)
                            clauseOkupasi = clauseOkupasi + " or kd_okupasi = '"+ kodeOkupasi2 +"'";

                        if(kodeOkupasi3!=null)
                            clauseOkupasi = clauseOkupasi + " or kd_okupasi = '"+ kodeOkupasi3 +"'";

                        clauseOkupasi = clauseOkupasi + " )";

                        sqa.addClause(clauseOkupasi);

                }

                if(useCoverageId){
                    sqa.addClause("'"+ cover.getStInsuranceCoverID() + "'= ANY (ins_cover_id_array)");
                }

                if(useRiskCatCode){
                    sqa.addClause("'"+ getRiskCategory().getStInsuranceRiskCategoryCode() + "'= ANY (risk_cat_code_array)");
                }

                if(useLowestTsi){
                    sqa.addClause(" TSI_LOWEST1 <= ?");
                    sqa.addPar(tsiObject);
                }

                if(useTopTsi){
                    sqa.addClause(" TSI_TOP1 >= ?");
                    sqa.addPar(tsiObject);
                }

                if(!cover.getStCoverCategory().equalsIgnoreCase("MAIN")){
                    sqa.addClause(" (ins_cover_id_parent is null or ins_cover_id_parent = ?)");
                    sqa.addPar(getMainCoverage().getStInsuranceCoverID());
                }

                sqa.addClause("pol_type_id = ?");
                sqa.addPar(policy.getStPolicyTypeID());

//                System.out.println("#################################################################");
//                System.out.println("********** cover type : "+ cover.getStInsuranceCoverID() + " | "+cover.getInsuranceCoverage().getStDescription());
//                System.out.println("********** SQL : "+sqa.getSQL());
//                System.out.println("********** rcl : "+rcl);
//                System.out.println("********** rate factor : "+getPolicy().getDbPeriodRateFactor());
//                System.out.println("********** premium factor : "+getPolicy().getDbPremiumFactorValue());

                String sql = sqa.getSQL();

                if(useKodeOkupasi)
                    sql = sqa.getSQL() + " order by rate_lowest"+ rcl+" desc limit 1 ";

                final DTOList l = ListUtil.getDTOListFromQuery(
                        sql,
                        sqa.getPar(),
                        HashDTO.class
                        );

//                logger.logWarning("##################### SQL OJK ####################");
//                logger.logWarning("##################### SQL OJK : "+ sql);
//                logger.logWarning("##################### SQL OJK ####################");

                if(l.size()> 0){
                        HashDTO h = (HashDTO) l.get(0);
                        BigDecimal batasBawah = h.getFieldValueByFieldNameBD("rate_lowest"+rcl);
                        BigDecimal batasAtas = h.getFieldValueByFieldNameBD("rate_top"+rcl);
                        String limitBy = h.getFieldValueByFieldNameST("limit_by");
                        String unit = h.getFieldValueByFieldNameST("unit");
                        String okupasi = h.getFieldValueByFieldNameST("kd_okupasi");

                        boolean limitByRate = limitBy.equalsIgnoreCase("RATE");
                        boolean limitByTSI = limitBy.equalsIgnoreCase("TSI");

                        timeExcess = h.getFieldValueByFieldNameBD("time_excess");
                        excessUnit = h.getFieldValueByFieldNameST("excess_unit");

                        DateTime periodeAwal = new DateTime(policy.getDtPeriodStart());
                        DateTime periodeAkhir = new DateTime(policy.getDtPeriodEnd());
                        Days day = Days.daysBetween(periodeAwal, periodeAkhir);
                        int jumlahHari = day.getDays();

//                        System.out.println("**************  batas bawah : "+ batasBawah);
//                        System.out.println("**************  batas atas  : "+ batasAtas);

                        BigDecimal batasBawahDisplay = null;
                        BigDecimal batasAtasDisplay = null;
                        if(!polType.getStRateMethod().equalsIgnoreCase(unit)){
                            if(polType.getStRateMethod().equalsIgnoreCase("%")){
                                batasBawah = BDUtil.getPctFromMile(batasBawah);
                                batasAtas = BDUtil.getPctFromMile(batasAtas);
                            }else if(polType.getStRateMethod().equalsIgnoreCase("PMIL")){
                                batasBawah = BDUtil.getMileFromPCT(batasBawah);
                                batasAtas = BDUtil.getMileFromPCT(batasAtas);
                            }
                        }

                        batasBawahDisplay = batasBawah;
                        batasAtasDisplay = batasAtas;


                        //batasAtas = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasAtas);
                        //batasBawah = BDUtil.mul(BDUtil.div(new BigDecimal(jumlahHari), new BigDecimal(365), 6), batasBawah);

                        //System.out.println("**************  limit by  : "+ limitBy);
//                        System.out.println("**************  jumlah hari  : "+ jumlahHari);
//                        System.out.println("**************  batas bawah pro rata : "+ batasBawah);
//                        System.out.println("**************  batas atas pro rata : "+ batasAtas);

                        String message ="";
                        boolean masihMasukBatas = true;

                        BigDecimal rateCoverage = BDUtil.zero;
                        BigDecimal rateCover = BDUtil.zero;
                        if(BDUtil.isZeroOrNull(cover.getDbRate()) && !BDUtil.isZeroOrNull(cover.getDbPremiNew())){
                            rateCoverage = BDUtil.div(cover.getDbPremiNew(), cover.getDbInsuredAmount(), 6);

                            if(polType.getStRateMethod().equalsIgnoreCase("%")) rateCoverage = BDUtil.mul(rateCoverage, BDUtil.hundred);
                            else rateCoverage = BDUtil.mul(rateCoverage, BDUtil.thousand);
                        }else{
                            rateCoverage = cover.getDbRate();
                        }

                        //rateCover = BDUtil.mul(rateCoverage, getPolicy().getDbPeriodRateFactor());
                        rateCover = rateCoverage;

                        if(BDUtil.isZeroOrNull(cover.getDbRate()) && !BDUtil.isZeroOrNull(cover.getDbPremiNew())){
                            rateCover = BDUtil.div(rateCoverage, getPolicy().getDbPeriodRateFactor());
                        }

//                        System.out.println("**************  rate Cover pro rata: "+ rateCover);
                        if(!cekBatasAtas)
                            batasAtas = BDUtil.hundred;

                        if(limitByRate){

                            if(BDUtil.lesserThan(rateCover, batasBawah) || BDUtil.biggerThan(rateCover, batasAtas)){
                                masihMasukBatas = false;
                                message = "Rate Coverage "+ cover.getInsuranceCoverage().getStDescription() +" ("+ ConvertUtil.removeTrailing(String.valueOf(rateCover)) + ") tidak sesuai ketentuan OJK";
                                message = message +"<br><b>------------------- DESKRIPSI --------------------";
//                                message = message +"<br>Rate Cover Per Tahun : "+  NumberUtil.getMoneyStr(rateCoverage.doubleValue(), 5) +" <br>Rate Cover Pro Rata : "+
//                                        NumberUtil.getMoneyStr(rateCover.doubleValue(), 5)+" <br>Rate Cover Batasan OJK : "+
//                                        batasBawahDisplay +" s/d "+ batasAtasDisplay + " "+ polType.getStRateMethod()+
//                                        "<br> Pro Rata Hari : "+ jumlahHari +"/365";

                                message =  message + "<table>"
                                        +"<tr>"
                                        +"<td>Rate Cover Batasan OJK</td><td>:</td><td>"+  batasBawahDisplay +" s/d "+ batasAtasDisplay + " "+ polType.getStRateMethod()+"</td>"
                                        + "</tr>"
//                                        +"<tr>"
//                                        +"<td>Rate Cover Per Tahun</td><td>:</td><td>"+  NumberUtil.getMoneyStr(rateCoverage.doubleValue(), 5) +"</td>"
//                                        + "</tr>"
                                        +"<tr>"
                                        +"<td>Rate Cover</td><td>:</td><td>"+  NumberUtil.getMoneyStr(rateCover.doubleValue(), 5) +"</td>"
                                        + "</tr>"
//                                        +"<tr>"
//                                        +"<td>Pro Rata Hari</td><td>:</td><td>"+  + jumlahHari +"/365"+" -> "+ getPolicy().getDbPeriodRateFactor() +"</td>"
//                                        + "</tr>"
                                        + "</table>";
                            }

                        }else{
                            BigDecimal limitTSI = BDUtil.mul(BDUtil.getRateFromPct(batasBawah), getDbObjectInsuredAmount());
                            if(!BDUtil.isEqual(limitTSI, cover.getDbInsuredAmount(), 2)){
                                masihMasukBatas = false;
                                message = "TSI Coverage "+ cover.getInsuranceCoverage().getStDescription() + " harusnya bernilai "+ ConvertUtil.removeTrailing(String.valueOf(limitTSI)) + " ("+ ConvertUtil.removeTrailing(String.valueOf(batasBawah)) +"% from TSI)";
                                //message = message +"<br><b> Rate Cover/tahun : "+ConvertUtil.removeTrailing(String.valueOf(cover.getDbRate())) +" <br>Pro Rate : "+ ConvertUtil.removeTrailing(String.valueOf(rateCover))+" <br>Batas Bawah OJK : "+ ConvertUtil.removeTrailing(String.valueOf(h.getFieldValueByFieldNameBD("rate_lowest"+rcl))) +" <br>Batas Atas OJK : "+ ConvertUtil.removeTrailing(String.valueOf(h.getFieldValueByFieldNameBD("rate_top"+rcl)))+"</b>";

                            }

                        }

                        if(!masihMasukBatas)
                            throw new RuntimeException("Objek No. "+ objx.getStOrderNo()+ " Salah.<br>"+message);
                }

//                System.out.println("#################################################################");

          }

    }
    

}
