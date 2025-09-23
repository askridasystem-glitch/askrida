/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyTreatyView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 1:01:42 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.crux.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class InsurancePolicyTreatyView extends DTO implements RecordAudit {
    private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyView.class);
    
    public static String tableName = "ins_pol_treaty";
    
    private String stRateMethod;
    
    public static String fieldMap[][] = {
        {"stInsurancePolicyTreatyID", "ins_pol_treaty_id*pk*nd"},
        {"stInsuranceTreatyID", "ins_treaty_id"},
        {"stPolicyID", "policy_id"},
        {"stInsurancePolicyObjectID", "ins_pol_obj_id"},
            /*{"dbTSIAmount","tsi_amount"},
            {"dbPremiAmount","premi_amount"},*/
        {"dbTSIAmount","tsi_amount*n"},
    };
    
    private String stInsurancePolicyTreatyID;
    private String stInsuranceTreatyID;
    private String stPolicyID;
    private String stInsurancePolicyObjectID;
    /*private BigDecimal dbTSIAmount;
  private BigDecimal dbPremiAmount;*/
    private DTOList details;
    private DTOList shares;
    private InsuranceTreatyView treaty;
    private BigDecimal dbTSIAmount;
    
    private DTOList list1;
    private DTOList list2;
    private String stTreatyType1;
    private BigDecimal persenMaipark = new BigDecimal(0);
    private BigDecimal ratioZone = new BigDecimal(0);
    private InsuranceRiskCategoryView riskcat;
    private InsurancePolicyObjectView object;
    int scale = 0;
    
    public String getStInsurancePolicyObjectID() {
        return stInsurancePolicyObjectID;
    }
    
    public void setStInsurancePolicyObjectID(String stInsurancePolicyObjectID) {
        this.stInsurancePolicyObjectID = stInsurancePolicyObjectID;
    }
    
    public InsuranceTreatyView getTreaty() {
        if (treaty == null)
            treaty = (InsuranceTreatyView) DTOPool.getInstance().getDTO(InsuranceTreatyView.class, stInsuranceTreatyID);
        return treaty;
    }
    
    /*
    public InsurancePolicyObjectView getObject() throws Exception {
       final InsurancePolicyObjectView object = (InsurancePolicyObjectView) DTOPool.getInstance().getDTO(InsurancePolicyObjectView.class, stInsurancePolicyObjectID);
     
       return object;
    }*/
    
    public InsurancePolicyObjectView getObject() throws Exception {
        return object;
    }
    
    public void setObject(InsurancePolicyObjectView object) {
        this.object = object;
    }
    
    public void setTreaty(InsuranceTreatyView treaty) {
        this.treaty = treaty;
    }
    
    public DTOList getDetails() {
        loadDetails();
        return details;
    }
    
    private void loadDetails() {
        try {
            if (details == null) {
                details =
                        ListUtil.getDTOListFromQuery(
                        "select a.*,b.treaty_type from ins_pol_treaty_detail a inner join ins_treaty_detail b on b.ins_treaty_detail_id = a.ins_treaty_detail_id where ins_pol_treaty_id = ? order by a.ins_pol_tre_det_id",
                        new Object[]{stInsurancePolicyTreatyID},
                        InsurancePolicyTreatyDetailView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    /*
    public DTOList getShares() {
       loadShares();
       return shares;
    }
     
    private void loadShares() {
       try {
          if (shares==null)
             shares = ListUtil.getDTOListFromQuery(
                     " select * from ins_pol_ri where ins_pol_tre_det_id=?",
                     new Object [] {stInsurancePolicyTreatyDetailID},
                     InsurancePolicyReinsView.class
             );
       } catch (Exception e) {
          throw new RuntimeException(e);
       }
    }*/
    
    public void setDetails(DTOList details) {
        this.details = details;
    }
    
    public String getStInsurancePolicyTreatyID() {
        return stInsurancePolicyTreatyID;
    }
    
    public void setStInsurancePolicyTreatyID(String stInsurancePolicyTreatyID) {
        this.stInsurancePolicyTreatyID = stInsurancePolicyTreatyID;
    }
    
    public String getStInsuranceTreatyID() {
        return stInsuranceTreatyID;
    }
    
    public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
        this.stInsuranceTreatyID = stInsuranceTreatyID;
    }
    
    public String getStPolicyID() {
        return stPolicyID;
    }
    
    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }
    
    public BigDecimal getDbTSIAmount() {
       return dbTSIAmount;
    }
     
    public void setDbTSIAmount(BigDecimal dbTSIAmount) {
       this.dbTSIAmount = dbTSIAmount;
    }
     
    /*public BigDecimal getDbPremiAmount() {
       return dbPremiAmount;
    }
     
    public void setDbPremiAmount(BigDecimal dbPremiAmount) {
       this.dbPremiAmount = dbPremiAmount;
    }*/
    
    public String getStInsuranceTreatyDesc() {
        if (getTreaty() == null) return null;
        return getTreaty().getStTreatyName();
    }
    
    public void raiseToTSI(String policyType, BigDecimal dbInsuredAmount, BigDecimal tlr, BigDecimal dbCurrencyRate, String currencyCode, BigDecimal tlrCover) throws Exception {

        if(getObject().getPolicy().isManualReinsuranceFlag())
            if(BDUtil.isZeroOrNull(dbInsuredAmount))
                dbInsuredAmount = new BigDecimal(1);

        getDetails();
        
        if (!currencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        while (true) {
            
            BigDecimal t = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dtl = (InsurancePolicyTreatyDetailView) details.get(i);
                
                if (dtl.getStParentID() != null && !dtl.getTreatyDetail().isOR()) continue;
                
                if (dtl.getTreatyDetail().isBPDAN()) continue;
                
                final BigDecimal tl = dtl.getDbTreatyLimit();
                
                t = BDUtil.add(t, tl);
                
                final boolean over = Tools.compare(t, BDUtil.mul(dbInsuredAmount,dbCurrencyRate,scale))>=0;

                //final boolean over = Tools.compare(t, dbInsuredAmount) >= 0;

                if (over) {
                    
                    for (int j = i + 1; j < details.size(); j++) {
                        
                        final InsurancePolicyTreatyDetailView trdd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trdd.getStParentID() == null) trdd.select();
                        
                        if (trdd.getStParentID() != null) {
                            
                            for (int k = 0; k < details.size(); k++) {
                                InsurancePolicyTreatyDetailView trdx = (InsurancePolicyTreatyDetailView) details.get(k);
                                
                                if (Tools.isEqual(trdd.getStParentID(), trdx.getStInsuranceTreatyDetailID())) {
                                    if (trdx.isSelected()) {
                                        trdd.select();
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }
                    
                    for (int j = details.size() - 1; j >= 0; j--) {
                        
                        final InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trd.isSelected()) details.delete(j);
                        
                    }
                    
                    return;
                }
            }
            
            t = new BigDecimal(0);
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dt = (InsurancePolicyTreatyDetailView) details.get(i);
                
                t = BDUtil.add(t, dt.getDbTreatyLimit());
            }
            
            if (!raise1Level(policyType, t, tlr, dbCurrencyRate, currencyCode, tlrCover)) {
                break;
            }
        }
    }
    
     public void raiseToTSI2(String policyType, BigDecimal dbInsuredAmount, BigDecimal tlr, BigDecimal dbCurrencyRate, String currencyCode) throws Exception {
        
        getDetails();
        
        if (!currencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        while (true) {
            
            BigDecimal t = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dtl = (InsurancePolicyTreatyDetailView) details.get(i);
                
                if (dtl.getStParentID() != null && !dtl.getTreatyDetail().isOR()) continue;
                
                if (dtl.getTreatyDetail().isBPDAN()) continue;
                
                final BigDecimal tl = dtl.getDbTreatyLimit();
                
                t = BDUtil.add(t, tl);
                
                //final boolean over = Tools.compare(t, BDUtil.mul(dbInsuredAmount,dbCurrencyRate,scale))>=0;
                
                final boolean over = Tools.compare(t, dbInsuredAmount) >= 0;
                
                /*if (over) {
                    
                    for (int j = i + 1; j < details.size(); j++) {
                        
                        final InsurancePolicyTreatyDetailView trdd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trdd.getStParentID() == null) trdd.select();
                        
                        if (trdd.getStParentID() != null) {
                            
                            for (int k = 0; k < details.size(); k++) {
                                InsurancePolicyTreatyDetailView trdx = (InsurancePolicyTreatyDetailView) details.get(k);
                                
                                if (Tools.isEqual(trdd.getStParentID(), trdx.getStInsuranceTreatyDetailID())) {
                                    if (trdx.isSelected()) {
                                        trdd.select();
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }
                    
                    for (int j = details.size() - 1; j >= 0; j--) {
                        
                        final InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trd.isSelected()) details.delete(j);
                        
                    }
                    
                    return;
                }*/
            }
            
            t = new BigDecimal(0);
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dt = (InsurancePolicyTreatyDetailView) details.get(i);
                
                t = BDUtil.add(t, dt.getDbTreatyLimit());
            }
            
            if (!raise1Level(policyType, t, tlr, dbCurrencyRate, currencyCode,null)) {
                break;
            }
        }
    }
     
    public void raise(String policyType, BigDecimal dbInsuredAmount, BigDecimal tlr, BigDecimal dbCurrencyRate, String currencyCode) throws Exception {
        raise1Level(policyType, null, tlr, dbCurrencyRate, currencyCode,null);
    }
    
    public void raiseToTSIEarthquake(String policyType, BigDecimal dbInsuredAmount, BigDecimal tlr, BigDecimal tlrMaipark, BigDecimal dbCurrencyRate, String currencyCode) throws Exception {
        
        getDetails();
        
        if (!currencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        while (true) {
            
            BigDecimal t = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dtl = (InsurancePolicyTreatyDetailView) details.get(i);
                
                if (dtl.getStParentID() != null && !dtl.getTreatyDetail().isOR()) continue;
                
                //if(dtl.getTreatyDetail().isBPDAN()) continue;
                
                final BigDecimal tl = dtl.getDbTreatyLimit();
                
                t = BDUtil.add(t, tl);
                
                //final boolean over = Tools.compare(t, BDUtil.mul(dbInsuredAmount,dbCurrencyRate,scale))>=0;
                
                final boolean over = Tools.compare(t, dbInsuredAmount) >= 0;
                
                if (over) {
                    
                    for (int j = i + 1; j < details.size(); j++) {
                        
                        final InsurancePolicyTreatyDetailView trdd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trdd.getStParentID() == null) trdd.select();
                        
                        if (trdd.getStParentID() != null) {
                            
                            for (int k = 0; k < details.size(); k++) {
                                InsurancePolicyTreatyDetailView trdx = (InsurancePolicyTreatyDetailView) details.get(k);
                                
                                if (Tools.isEqual(trdd.getStParentID(), trdx.getStInsuranceTreatyDetailID())) {
                                    if (trdx.isSelected()) {
                                        trdd.select();
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }
                    
                    for (int j = details.size() - 1; j >= 0; j--) {
                        
                        final InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(j);
                        
                        if (trd.isSelected()) details.delete(j);
                        
                    }
                    
                    return;
                }
            }
            
            t = new BigDecimal(0);
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyTreatyDetailView dt = (InsurancePolicyTreatyDetailView) details.get(i);
                
                t = BDUtil.add(t, dt.getDbTreatyLimit());
            }
            
            if (!raise1LevelEarthquake(policyType, t, tlr, tlrMaipark, dbCurrencyRate, currencyCode)) {
                break;
            }
        }
    }
    
    private BigDecimal getTSILimit() {
        
        BigDecimal t = null;
        
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView dtl = (InsurancePolicyTreatyDetailView) details.get(i);
            
            t = BDUtil.add(t, dtl.getDbTreatyLimit());
        }
        
        return t;
    }
    
    public boolean raise1Level(String policyType, BigDecimal currentCapacity, BigDecimal tlRatio, BigDecimal dbCurrencyRate, String stCurrencyCode, BigDecimal tlrCover) throws Exception {
        getTreaty();
        if (!stCurrencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        if (treaty == null) throw new RuntimeException("Treaty tidak ditemukan, cek periode awal polis apakah sudah betul?");
        
        final DTOList treatyDetails = treaty.getDetails(policyType);
        
        if (treatyDetails == null || treatyDetails.size() < 1) throw new RuntimeException("Treaty details "+ treaty.getStTreatyName()+ " jenis : "+policyType+" tidak ditemukan");
        
/*
      final int cz = getDetails().size();
 
      if (cz>=treatyDetails.size()) return false;
 */
        
        final DTOList polTreatyDetails = getDetails();
        
        final HashMap existingTreatyDetail = polTreatyDetails.getMapOf("ins_treaty_detail_id", true);
        
        InsuranceTreatyDetailView nextLevel = null;
        
        for (int i = 0; i < treatyDetails.size(); i++) {
            InsuranceTreatyDetailView td = (InsuranceTreatyDetailView) treatyDetails.get(i);
            
            if (existingTreatyDetail.containsKey(td.getStInsuranceTreatyDetailID())) continue;
            
            final boolean freeTSI = Tools.isYes(td.getTreatyType().getStFreeTSIFlag());
            
            if (td.getDbTreatyLimit() == null && !freeTSI && td.getDbTSIMax() == null) continue;
            
            final BigDecimal actualLimit = BDUtil.mul(BDUtil.div(td.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlRatio), scale);
            
            //if (Tools.compare(actualLimit,currentCapacity)>=0)
            {
                nextLevel = td;
                break;
            }
            
            //currentCapacity = BDUtil.sub(currentCapacity,actualLimit);
        }
        
        //if (nextLevel == null) throw new RuntimeException("Sum insured amount is over total treaty limit");
        
        if (nextLevel == null) return false;
        
        final DTOList nextLevels = new DTOList();
        
        nextLevels.add(nextLevel);
        
        /**
         * recursively add all child treaties
         */
        for (int i = 0; i < nextLevels.size(); i++) {
            InsuranceTreatyDetailView tdn = (InsuranceTreatyDetailView) nextLevels.get(i);
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsuranceTreatyDetailView td = (InsuranceTreatyDetailView) treatyDetails.get(j);
                
                if (td.getStParentID() == null) continue;
                
                final boolean isChild = Tools.isEqual(td.getStParentID(), tdn.getStInsuranceTreatyDetailID());
                
                if (isChild)
                    nextLevels.add(td);
            }
        }
        
/*
      nextLevel = (InsuranceTreatyDetailView)treatyDetails.get(cz);
 
      if (details.size()==0) {
         if (!nextLevel.isOR() && !nextLevel.isQS()) throw new RuntimeException("Invalid treaty ! first level should be [OR] or [QS]");
      }
 */
        
        for (int i = 0; i < nextLevels.size(); i++) {
            InsuranceTreatyDetailView nl = (InsuranceTreatyDetailView) nextLevels.get(i);
            
            final InsurancePolicyTreatyDetailView det = new InsurancePolicyTreatyDetailView();
            
            det.markNew();
            
            det.setStInsuranceTreatyDetailID(nl.getStInsuranceTreatyDetailID());
            
            det.setTreaty(this);
            
            final boolean isCaptive = getObject().getPolicy().isCaptivePolicy();
            
            if (isCaptive) det.setDbComissionRate(nl.getDbCommissionRateCaptivePct());
            else det.setDbComissionRate(nl.getDbCommissionRatePct());
            
            
            if (nl.getDbTreatyLimit() != null) {
                det.setDbTreatyLimitRatio(tlRatio);
                det.setDbTreatyLimit(BDUtil.mul(BDUtil.div(nl.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlRatio), scale));
                if(!BDUtil.isZeroOrNull(tlrCover)) det.setDbTreatyLimit(BDUtil.mul(det.getDbTreatyLimit(), BDUtil.getRateFromPct(tlrCover), scale));
                if (nl.isBPDAN()) det.setDbTreatyLimit(BDUtil.div(nl.getDbTreatyLimit(), dbCurrencyRate));
            }
            det.setStParentID(nl.getStParentID());
            det.setDbTSIPct(nl.getDbTSIPct());
            det.setDbPremiRatePct(nl.getDbPremiRatePct());
            det.setStEditFlag("N");
            
            det.initShares();
            
            details.add(det);
        }
        
        return true;
    }
    
    public boolean raise1LevelEarthquake(String policyType, BigDecimal currentCapacity, BigDecimal tlRatio, BigDecimal tlRatioMaipark, BigDecimal dbCurrencyRate, String stCurrencyCode) throws Exception {
        getTreaty();
        if (!stCurrencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        if (treaty == null) throw new RuntimeException("Treaty tidak ditemukan, cek periode awal polis apakah sudah betul?");
        
        final DTOList treatyDetails = treaty.getDetails(policyType);
        
        if (treatyDetails == null || treatyDetails.size() < 1) throw new RuntimeException("Treaty details not found");
        
/*
      final int cz = getDetails().size();
 
      if (cz>=treatyDetails.size()) return false;
 */
        
        final DTOList polTreatyDetails = getDetails();
        
        final HashMap existingTreatyDetail = polTreatyDetails.getMapOf("ins_treaty_detail_id", true);
        
        InsuranceTreatyDetailView nextLevel = null;
        
        for (int i = 0; i < treatyDetails.size(); i++) {
            InsuranceTreatyDetailView td = (InsuranceTreatyDetailView) treatyDetails.get(i);
            
            if (existingTreatyDetail.containsKey(td.getStInsuranceTreatyDetailID())) continue;
            
            final boolean freeTSI = Tools.isYes(td.getTreatyType().getStFreeTSIFlag());
            
            if (td.getDbTreatyLimit() == null && !freeTSI && td.getDbTSIMax() == null) continue;
            
            final BigDecimal actualLimit = BDUtil.mul(BDUtil.div(td.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlRatio), scale);
            
            //if (Tools.compare(actualLimit,currentCapacity)>=0)
            {
                nextLevel = td;
                break;
            }
            
            //currentCapacity = BDUtil.sub(currentCapacity,actualLimit);
        }
        
        //if (nextLevel == null) throw new RuntimeException("Sum insured amount is over total treaty limit");
        
        if (nextLevel == null) return false;
        
        final DTOList nextLevels = new DTOList();
        
        nextLevels.add(nextLevel);
        
        /**
         * recursively add all child treaties
         */
        for (int i = 0; i < nextLevels.size(); i++) {
            InsuranceTreatyDetailView tdn = (InsuranceTreatyDetailView) nextLevels.get(i);
            
            for (int j = 0; j < treatyDetails.size(); j++) {
                InsuranceTreatyDetailView td = (InsuranceTreatyDetailView) treatyDetails.get(j);
                
                if (td.getStParentID() == null) continue;
                
                final boolean isChild = Tools.isEqual(td.getStParentID(), tdn.getStInsuranceTreatyDetailID());
                
                if (isChild)
                    nextLevels.add(td);
            }
        }
        
/*
      nextLevel = (InsuranceTreatyDetailView)treatyDetails.get(cz);
 
      if (details.size()==0) {
         if (!nextLevel.isOR() && !nextLevel.isQS()) throw new RuntimeException("Invalid treaty ! first level should be [OR] or [QS]");
      }
 */
        
        for (int i = 0; i < nextLevels.size(); i++) {
            InsuranceTreatyDetailView nl = (InsuranceTreatyDetailView) nextLevels.get(i);
            
            final InsurancePolicyTreatyDetailView det = new InsurancePolicyTreatyDetailView();
            
            det.markNew();
            
            det.setStInsuranceTreatyDetailID(nl.getStInsuranceTreatyDetailID());
            det.setTreaty(this);
            
            final boolean isCaptive = getObject().getPolicy().isCaptivePolicy();
            
            if (isCaptive) det.setDbComissionRate(nl.getDbCommissionRateCaptivePct());
            else det.setDbComissionRate(nl.getDbCommissionRatePct());
            
            if (nl.getDbTreatyLimit() != null) {
                //if(!nl.isMaipark()){
                //det.setDbTreatyLimitRatio(tlRatioMaipark);
                //det.setDbTreatyLimit(BDUtil.mul(BDUtil.div(nl.getDbTreatyLimit(),dbCurrencyRate), BDUtil.getRateFromPct(tlRatioMaipark),scale));
                //}else{
                if (nl.isMaipark()) {
                    det.setDbTreatyLimitRatio(tlRatioMaipark);
                    det.setDbTreatyLimit(BDUtil.mul(BDUtil.div(nl.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlRatioMaipark), scale));
                    if(getObject().getPolicy().getDbSharePct()!=null)
                        det.setDbTreatyLimit(BDUtil.mul(det.getDbTreatyLimit(), BDUtil.getRateFromPct(getObject().getPolicy().getDbSharePct()), scale));

                } else {
                    det.setDbTreatyLimitRatio(tlRatio);
                    det.setDbTreatyLimit(BDUtil.mul(BDUtil.div(nl.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlRatio), scale));
                }
                //}
                
                if (nl.isBPDAN()) det.setDbTreatyLimit(BDUtil.div(nl.getDbTreatyLimit(), dbCurrencyRate));
            }
            det.setStParentID(nl.getStParentID());
            det.setDbTSIPct(nl.getDbTSIPct());
            det.setDbPremiRatePct(nl.getDbPremiRatePct());
            det.setStEditFlag("N");
            
            det.initShares();
            
            details.add(det);
        }
        
        return true;
    }
    
    
    public void lower() {
        getTreaty();
        getDetails();
        
        if (details.size() < 2) return;
        
        details.delete(details.size() - 1);
    }
/*
   public void calculate(InsurancePolicyObjectView ipo, BigDecimal dbInsuredAmount, BigDecimal dbPremi) {
 
      getDetails();
 
      final BigDecimal totalInsured = dbInsuredAmount;
 
      final HashMap detailMapByInsTreatyDetailID = details.getMapOf("ins_treaty_detail_id");
 
      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
 
         if (Tools.compare(trd.getDbTreatyLimit(), dbInsuredAmount)>=0) {
            trd.setDbBaseTSIAmount(dbInsuredAmount);
         } else {
            trd.setDbBaseTSIAmount(trd.getDbTreatyLimit());
         }
 
         InsurancePolicyTreatyDetailView parent=null;
         if (trd.getStParentID()!=null) {
            parent = (InsurancePolicyTreatyDetailView)detailMapByInsTreatyDetailID.get(trd.getStParentID());
         }
 
         if (parent!=null) {
 
            trd.setDbTSIAmount(
                            BDUtil.mul(
                                    parent.getDbBaseTSIAmount(),
                                    BDUtil.getRateFromPct(trd.getDbTSIPct())
                            )
            );
 
         } else {
            trd.setDbTSIAmount(
                            BDUtil.mul(
                                    trd.getDbBaseTSIAmount(),
                                    BDUtil.getRateFromPct(trd.getDbTSIPct())
                            )
            );
         }
 
         dbInsuredAmount = BDUtil.sub(dbInsuredAmount, trd.getDbTSIAmount());
 
 
         if (BDUtil.biggerThanZero(totalInsured))
            trd.setDbPremiAmount(BDUtil.div(BDUtil.mul(dbPremi, trd.getDbTSIAmount()), totalInsured));
         else
            trd.setDbPremiAmount(BDUtil.zero);
 
         if (parent!=null) {
            if (trd.getDbPremiRatePct()!=null){
               trd.setDbPremiAmount(
                       BDUtil.mul(
                               parent.getDbPremiAmount(),
                               BDUtil.getRateFromPct(trd.getDbPremiRatePct())
                       )
               );
            }
         }
 
 
         trd.setDbComission(BDUtil.mul(trd.getDbPremiAmount(), BDUtil.getRateFromPct(trd.getDbComissionRate())));
 
         final DTOList shares = trd.getShares();
 
         final BigDecimal preminet = trd.getDbPremiAmount();//BDUtil.sub(trd.getDbPremiAmount(), trd.getDbComission());
 
         BigDecimal premitot = null;
         BigDecimal commtot = null;
 
         final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();
 
         for (int j = 0; j < shares.size(); j++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);
 
            final String auto_premi = (String)ri.getControlMap().get("AUTO_PREMI");
            final String PREMI_FACTOR = (String)ri.getControlMap().get("PREMI_FACTOR");
 
            final boolean isTSIPremiFactor = "TSI".equalsIgnoreCase(PREMI_FACTOR);
 
            final BigDecimal spct = BDUtil.getRateFromPct(ri.getDbSharePct());
 
            ri.setDbTSIAmount(BDUtil.mul(trd.getDbTSIAmount(), spct));
 
            //if (ri.isAutoRate()) ri.setDbPremiRate();
 
            if (auto_premi != null) {
               if (ri.isAutoRate()) {
                  ri.setDbPremiRate((BigDecimal)ipo.getProperty(auto_premi));
               }
            }
 
            if (nonProportional) {
               if (isTSIPremiFactor) {
                  ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate())));
 
               } else {
                  if (ri.isUseRate()) {
                     if (ri.isAutoRate()) {
                        ri.setDbPremiRate(null);
                        ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct())));
                     } else
                        ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate())));
                  }
               }
            } else {
               if (trd.getTreatyDetail().isOR()) {
 
               } else {
                  ri.setStAutoRateFlag("Y");
                  ri.setStUseRateFlag("Y");
                  ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct())));
                  //ri.setDbRICommRate(trd.getDbComissionRate());
               }
            }
 
            if (ri.getDbRICommRate()==null) ri.setDbRICommRate(trd.getDbComissionRate());
 
            ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate())));
 
 
            premitot = BDUtil.add(premitot, ri.getDbPremiAmount());
            commtot = BDUtil.add(commtot, ri.getDbRICommAmount());
         }
 
         if (premitot!=null)
            trd.setDbPremiAmount(premitot);
 
         if(commtot!=null)
            trd.setDbComission(commtot);
 
         //if (Tools.compare(dbInsuredAmount, BDUtil.zero)<=0) break;
      }
 
   }
 */
    
    public void adjustRatio(BigDecimal tlr, BigDecimal dbCurrencyRate, String stCurrencyCode, BigDecimal tlrCover) throws Exception{
        
        getDetails();
        
        if (!stCurrencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            
            final InsuranceTreatyDetailView trdd = trd.getTreatyDetail();
            
            trd.setDbTreatyLimitRatio(tlr);
            
            if (!trdd.isFreeTSI()) {
                trd.setDbTreatyLimit(BDUtil.mul(BDUtil.div(trdd.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlr), scale));
                if(!BDUtil.isZeroOrNull(tlrCover)) trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(tlrCover), scale));
                if (trdd.isBPDAN()) trd.setDbTreatyLimit(BDUtil.div(trdd.getDbTreatyLimit(), dbCurrencyRate));

                if(getObject().getPolicy().isStatusInward()){
                    if(trdd.getDbInwardCapacityPct()!=null){ 
                        trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(trdd.getDbInwardCapacityPct())));
                    }
                }
                
            }

        }
    }
    
    public void adjustRatioEarthquake(InsurancePolicyView policy, BigDecimal tlrMaipark, BigDecimal tlr, BigDecimal dbCurrencyRate, String stCurrencyCode) throws Exception{
        
        getDetails();
        
        if (!stCurrencyCode.equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            
            final InsuranceTreatyDetailView trdd = trd.getTreatyDetail();
            
            trd.setDbTreatyLimitRatio(tlr);
            if (trdd.isMaipark()) trd.setDbTreatyLimitRatio(tlrMaipark);
            
            if (!trdd.isFreeTSI()) {
                trd.setDbTreatyLimit(BDUtil.mul(BDUtil.div(trdd.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlrMaipark), scale));
               
                if(getObject().getPolicy().getDbSharePct()!=null)
                    trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(getObject().getPolicy().getDbSharePct()), scale));
                
                if (!trdd.isMaipark())
                    trd.setDbTreatyLimit(BDUtil.mul(BDUtil.div(trdd.getDbTreatyLimit(), dbCurrencyRate), BDUtil.getRateFromPct(tlr), scale));
                
                if (trdd.isBPDAN()) trd.setDbTreatyLimit(BDUtil.div(trdd.getDbTreatyLimit(), dbCurrencyRate));

                if(getObject().getPolicy().isStatusInward()){
                    if(trdd.getDbInwardCapacityPct()!=null){
                        trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(trdd.getDbInwardCapacityPct())));
                    }
                }
            }
        }
    }
    
    private String p(BigDecimal db) {
        return ConvertUtil.removeTrailing(ConvertUtil.print(db, 4));
    }
    
    public String getStRateMethod() {
        return stRateMethod;
    }
    
    public void setStRateMethod(String stRateMethod) {
        this.stRateMethod = stRateMethod;
    }
    
    public void calculateBackup(InsurancePolicyObjectView ipo, BigDecimal dbInsuredAmount, BigDecimal dbPremi, String stPolicyTypeGroupID, BigDecimal dbCurrencyRate, DTOList coin, boolean isCoas) throws Exception {
        
        getDetails();
        
        if (!ipo.getPolicy().getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        final BigDecimal totalInsured = dbInsuredAmount;
        
        BigDecimal dbInsuredAmountEndorse = new BigDecimal(0);
        
        if (ipo.getPolicy().getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE))
            dbInsuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
        
        BigDecimal tsiTemp = new BigDecimal(0);
        
        BigDecimal sisa_persen = new BigDecimal(0);
        BigDecimal persen_co = new BigDecimal(0);
        BigDecimal bppdanTsiPct = new BigDecimal(0);
        BigDecimal bppdanLimit = null;
        
        if (isCoas == true) {
            for (int l = 0; l < coin.size(); l++) {
                InsurancePolicyCoinsView coas = (InsurancePolicyCoinsView) coin.get(l);
                
                if (ipo.getPolicy().getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                    if (coas.getStPositionCode().equalsIgnoreCase("MEM"))
                        persen_co = coas.getDbSharePct();
                    break;
                } else {
                    if (coas.getStPositionCode().equalsIgnoreCase("LDR")) {
                        persen_co = coas.getDbSharePct();
                        break;
                    }
                }
            }
            
        }
        
        final HashMap detailMapByInsTreatyDetailID = details.getMapOf("ins_treaty_detail_id");
        
        try {
            list1 = ListUtil.getDTOListFromQuery(
                    "select treaty_limit from ins_treaty_detail" +
                    " where ins_treaty_id=? and policy_type_id=? " +
                    " and treaty_type = 'OR'",
                    new Object[]{getStInsuranceTreatyID(), stPolicyTypeGroupID},
                    InsuranceTreatyDetailView.class
                    
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        InsuranceTreatyDetailView tred = (InsuranceTreatyDetailView) list1.get(0);
        
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            if (isCoas) {
                try {
                    trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(ipo.getDbCoinsSessionPct())));
                    
                } catch (Exception e) {
                }
            }
            
            if (trd.getDbTreatyLimitRatio() == null) ratioZone = ratioZone;
            else ratioZone = trd.getDbTreatyLimitRatio();
            
            stTreatyType1 = trd.getTreatyDetail().getStTreatyTypeID();
            String group = stPolicyTypeGroupID;
            
            if (Tools.compare(trd.getDbTreatyLimit(), dbInsuredAmount) >= 0) {
                trd.setDbBaseTSIAmount(dbInsuredAmount);
            } else {
                trd.setDbBaseTSIAmount(trd.getDbTreatyLimit());
            }
            
            InsurancePolicyTreatyDetailView parent = null;
            if (trd.getStParentID() != null) {
                parent = (InsurancePolicyTreatyDetailView) detailMapByInsTreatyDetailID.get(trd.getStParentID());
                
            }
            
            if (parent != null) {
                tsiTemp = BDUtil.mul(
                        parent.getDbBaseTSIAmount(),
                        BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                        );
            } else {
                tsiTemp = BDUtil.mul(
                        trd.getDbBaseTSIAmount(),
                        BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                        );
            }
            
            if (SessionManager.getInstance().getSession().isOREdit()) {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
                else trd.setDbTSIAmount(tsiTemp);
            } else {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
                else trd.setDbTSIAmount(tsiTemp);
            }
            
            if (trd.getTreatyDetail().isOR()) {
                
                BigDecimal premRat = BDUtil.div(trd.getDbTSIAmount(), ipo.getDbObjectInsuredAmount(), 19);
                
                //BigDecimal orPremi = BDUtil.mul(premRat,ipo.getDbObjectPremiTotalAmount());
                BigDecimal orPremi = BDUtil.mul(premRat, ipo.getDbObjectPremiTotalAmount(), scale);
                
                //trd.setDbPremiAmount(orPremi);
                
                BigDecimal premiRate = BDUtil.div(orPremi, trd.getDbTSIAmount(), 18);
                
                //trd.setDbPremiRatePct(premiRate);
                
                DTOList shares = trd.getShares();
                
                for (int k = 0; k < shares.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                    BigDecimal rateActual = new BigDecimal(0);
                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("N");
                    
                    if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT))
                        rateActual = BDUtil.getPctFromRate(premiRate);
                    else if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL))
                        rateActual = BDUtil.getMileFromRate(premiRate);
                    
                    
                    ri.setDbPremiRate(rateActual);
                    ri.setDbPremiAmount(orPremi);
                }
            }
            //end tes aja
            
            dbInsuredAmount = BDUtil.sub(dbInsuredAmount, trd.getDbTSIAmount());
            BigDecimal premiOR = BDUtil.div(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale), totalInsured);
            
            if (BDUtil.biggerThanZero(totalInsured)) {
                trd.setDbPremiAmount(premiOR);
                
            } else {
                trd.setDbPremiAmount(BDUtil.zero);
            }
            
            if (parent != null) {
                if (trd.getDbPremiRatePct() != null) {
                    trd.setDbPremiAmount(
                            BDUtil.mul(
                            parent.getDbPremiAmount(),
                            BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale
                            )
                            );
                }
            }
            
            trd.setDbComission(BDUtil.mul(trd.getDbPremiAmount(), BDUtil.getRateFromPct(trd.getDbComissionRate()), scale));
            
            final DTOList shares = trd.getShares();
            
            final BigDecimal preminet = trd.getDbPremiAmount();//BDUtil.sub(trd.getDbPremiAmount(), trd.getDbComission());
            
            BigDecimal premitot = null;
            BigDecimal commtot = null;
            
            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();
            
            for (int j = 0; j < shares.size(); j++) {
                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);
                
                final String auto_premi = (String) ri.getControlMap().get("AUTO_PREMI");
                final String PREMI_FACTOR = (String) ri.getControlMap().get("PREMI_FACTOR");
                
                final boolean isTSIPremiFactor = "TSI".equalsIgnoreCase(PREMI_FACTOR);
                
                final BigDecimal spct = BDUtil.getRateFromPct(ri.getDbSharePct());
                
                if (ri.getDbTSIAmount() != null && ri.getDbSharePct() == null)
                    ri.setDbTSIAmount(ri.getDbTSIAmount());
                else
                    ri.setDbTSIAmount(BDUtil.mul(trd.getDbTSIAmount(), spct, 2));
                
                //if (ri.isAutoRate()) ri.setDbPremiRate();
                
                if (auto_premi != null) {
                    if (ri.isAutoRate()) {
                        ri.setDbPremiRate((BigDecimal) ipo.getProperty(auto_premi));
                    }
                }
                
                if (nonProportional) {
                    if (isTSIPremiFactor) {
                        ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                        
                    } else {
                        if (ri.isUseRate()) {
                            if (ri.isAutoRate()) {
                                ri.setDbPremiRate(null);
                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                            } else
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                        }
                    }
                } else {
                    if (trd.getTreatyDetail().isOR()) {
                        
                    } else {
                        ri.setStAutoRateFlag("Y");
                        ri.setStUseRateFlag("Y");
                        ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                        //ri.setDbRICommRate(trd.getDbComissionRate());
                    }
                }
                
                if (ri.getDbRICommRate() == null) ri.setDbRICommRate(trd.getDbComissionRate());
                
                ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));
                
                premitot = BDUtil.add(premitot, ri.getDbPremiAmount());
                commtot = BDUtil.add(commtot, ri.getDbRICommAmount());
                
                //perhitungan cover ri terbaru
                try {
                    final DTOList coverPolis = ipo.getCoverage();
                    for (int l = 0; l < coverPolis.size(); l++) {
                        InsurancePolicyCoverView coverPol = (InsurancePolicyCoverView) coverPolis.get(l);
                        
                        BigDecimal rateActual = new BigDecimal(0);
                        if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT))
                            rateActual = coverPol.getDbRatePct();
                        else if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL))
                            rateActual = coverPol.getDbRateMile();
                        
                        if (l == 0) {
                            ri.setStInsuranceCoverID1(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover1(ri.getDbTSIAmount());
                            ri.setDbRateCover1(coverPol.getDbRate());
                            ri.setDbPremiumCover1(BDUtil.mul(ri.getDbTSICover1(), rateActual, scale));
                        }
                        
                        if (l == 1) {
                            ri.setStInsuranceCoverID2(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover2(ri.getDbTSIAmount());
                            ri.setDbRateCover2(coverPol.getDbRate());
                            ri.setDbPremiumCover2(BDUtil.mul(ri.getDbTSICover2(), rateActual, scale));
                        }
                        
                        if (l == 2) {
                            ri.setStInsuranceCoverID3(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover3(ri.getDbTSIAmount());
                            ri.setDbRateCover3(coverPol.getDbRate());
                            ri.setDbPremiumCover3(BDUtil.mul(ri.getDbTSICover3(), rateActual, scale));
                        }
                        
                        if (l == 3) {
                            ri.setStInsuranceCoverID4(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover4(ri.getDbTSIAmount());
                            ri.setDbRateCover4(coverPol.getDbRate());
                            ri.setDbPremiumCover4(BDUtil.mul(ri.getDbTSICover4(), rateActual, scale));
                        }
                        
                        if (l == 4) {
                            ri.setStInsuranceCoverID5(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover5(ri.getDbTSIAmount());
                            ri.setDbRateCover5(coverPol.getDbRate());
                            ri.setDbPremiumCover5(BDUtil.mul(ri.getDbTSICover5(), rateActual, scale));
                        }
                        
                        if (l == 5) {
                            ri.setStInsuranceCoverID6(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover6(ri.getDbTSIAmount());
                            ri.setDbRateCover6(coverPol.getDbRate());
                            ri.setDbPremiumCover6(BDUtil.mul(ri.getDbTSICover6(), rateActual, scale));
                        }
                        
                        if (l == 6) {
                            ri.setStInsuranceCoverID7(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover7(ri.getDbTSIAmount());
                            ri.setDbRateCover7(coverPol.getDbRate());
                            ri.setDbPremiumCover7(BDUtil.mul(ri.getDbTSICover7(), rateActual, scale));
                        }
                        
                        if (l == 7) {
                            ri.setStInsuranceCoverID8(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover8(ri.getDbTSIAmount());
                            ri.setDbRateCover8(coverPol.getDbRate());
                            ri.setDbPremiumCover8(BDUtil.mul(ri.getDbTSICover8(), rateActual, scale));
                        }
                        
                        if (l == 8) {
                            ri.setStInsuranceCoverID9(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover9(ri.getDbTSIAmount());
                            ri.setDbRateCover9(coverPol.getDbRate());
                            ri.setDbPremiumCover9(BDUtil.mul(ri.getDbTSICover9(), rateActual, scale));
                        }
                        
                        if (l == 9) {
                            ri.setStInsuranceCoverID10(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover10(ri.getDbTSIAmount());
                            ri.setDbRateCover10(coverPol.getDbRate());
                            ri.setDbPremiumCover10(BDUtil.mul(ri.getDbTSICover10(), rateActual, scale));
                        }
                        
                        if (l == 10) {
                            ri.setStInsuranceCoverID11(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover11(ri.getDbTSIAmount());
                            ri.setDbRateCover11(coverPol.getDbRate());
                            ri.setDbPremiumCover11(BDUtil.mul(ri.getDbTSICover11(), rateActual, scale));
                        }
                        
                        if (l == 11) {
                            ri.setStInsuranceCoverID12(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover12(ri.getDbTSIAmount());
                            ri.setDbRateCover12(coverPol.getDbRate());
                            ri.setDbPremiumCover12(BDUtil.mul(ri.getDbTSICover12(), rateActual, scale));
                        }
                        
                        if (l == 12) {
                            ri.setStInsuranceCoverID13(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover13(ri.getDbTSIAmount());
                            ri.setDbRateCover13(coverPol.getDbRate());
                            ri.setDbPremiumCover13(BDUtil.mul(ri.getDbTSICover13(), rateActual, scale));
                        }
                        
                        if (l == 13) {
                            ri.setStInsuranceCoverID14(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover14(ri.getDbTSIAmount());
                            ri.setDbRateCover14(coverPol.getDbRate());
                            ri.setDbPremiumCover14(BDUtil.mul(ri.getDbTSICover14(), rateActual, scale));
                        }
                        
                        if (l == 14) {
                            ri.setStInsuranceCoverID15(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover15(ri.getDbTSIAmount());
                            ri.setDbRateCover15(coverPol.getDbRate());
                            ri.setDbPremiumCover15(BDUtil.mul(ri.getDbTSICover15(), rateActual, scale));
                        }
                        
                        if (l == 15)
                            throw new RuntimeException("Jumlah Cover Lebih Dari 15 !");
                        
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
            if (premitot != null)
                trd.setDbPremiAmount(premitot);
            
            if (commtot != null)
                trd.setDbComission(commtot);
        }
        
    }

    public void calculate(InsurancePolicyObjectView ipo, BigDecimal dbInsuredAmount, BigDecimal dbPremi, String stPolicyTypeGroupID, BigDecimal dbCurrencyRate, BigDecimal dbInsuranceCoinsScale) throws Exception {
        
        getDetails();

        InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) ipo;
        
        final InsurancePolicyView policy = ipo.getPolicy();
        boolean nextEndorse = false;

        if(policy.getStNextStatus()!=null) nextEndorse = policy.getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE);
        final boolean isEndorse = policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE) || nextEndorse;
        
        //if (!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        //else scale = 0;

        scale = 2;
        
        BigDecimal totalInsured = dbInsuredAmount;
        BigDecimal dbInsuredAmountEndorse = null;
        BigDecimal premiSaldo = dbPremi;
        BigDecimal premiTotal = dbPremi;
        BigDecimal lossRatio = null;

        BigDecimal premiBelumTersesi = dbPremi;

        BigDecimal komisiPct = policy.getTotalCommPCT();

        if(isEndorse || policy.isStatusEndorseRI())
            komisiPct = policy.getTotalCommPCTInduk();
 
        if (isEndorse){
            dbInsuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
            dbInsuredAmount = dbInsuredAmountEndorse;
            totalInsured = dbInsuredAmount;
        }

        if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)){
            DTOList covers = objx.getCoverage();
            if (covers!=null && covers.size()>0)
            {
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);
                lossRatio = cv.getCoverPolType().getDbTSIFactor1();
            }
             dbInsuredAmount = BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(lossRatio), scale);
        }

        BigDecimal tsiTemp = new BigDecimal(0);
        BigDecimal rateActual2 = new BigDecimal(0);
        
        final HashMap detailMapByInsTreatyDetailID = details.getMapOf("ins_treaty_detail_id");
        
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            if (policy.hasCoIns()) {
                try {
                    trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(dbInsuranceCoinsScale)));
                } catch (Exception e) {
                }
            }
            
            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE))
                ratioZone = ipo.getDbTreatyLimitRatioMaipark();

            if (Tools.compare(trd.getDbTreatyLimit(), dbInsuredAmount) >= 0) {
                trd.setDbBaseTSIAmount(dbInsuredAmount);
            } else {
                trd.setDbBaseTSIAmount(trd.getDbTreatyLimit());
            }

            if(BDUtil.isNegative(dbInsuredAmount)){
                if (Tools.compare(trd.getDbTreatyLimit(), BDUtil.negate(dbInsuredAmount)) >= 0) {
                    trd.setDbBaseTSIAmount(dbInsuredAmount);
                } else {
                    trd.setDbBaseTSIAmount(BDUtil.negate(trd.getDbTreatyLimit()));
                }
            }
       
            InsurancePolicyTreatyDetailView parent = null;
            if (trd.getStParentID() != null) {
                parent = (InsurancePolicyTreatyDetailView) detailMapByInsTreatyDetailID.get(trd.getStParentID());
            }
            
            if (parent != null) {
                tsiTemp = BDUtil.mul(
                        parent.getDbBaseTSIAmount(),
                        BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                        );
                
           } else {
                persenMaipark = new BigDecimal(100).subtract(ratioZone);
                                
                if (trd.getTreatyDetail().getDbTSIMax() != null) {
                    trd.setDbTreatyLimit(BDUtil.div(trd.getTreatyDetail().getDbTSIMax(), dbCurrencyRate, scale));
                    if(policy.hasCoIns()){
                        trd.setDbTreatyLimit(BDUtil.mul(trd.getTreatyDetail().getDbTSIMax(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct())));
                    }
                    
                    if(policy.getDbSharePct()!=null)
                        if(trd.getTreatyDetail().isMaipark())
                            trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(policy.getDbSharePct()), scale));

                    if(policy.isStatusInward()){
                        if(trd.getTreatyDetail().getDbInwardCapacityPct()!=null){
                            trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(trd.getTreatyDetail().getDbInwardCapacityPct())));
                        }
                    }

                    if (BDUtil.isZeroOrNull(trd.getDbTSIPct())) trd.setDbTSIPct(persenMaipark);
                    
                    tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShare(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );

                    if(isEndorse||policy.isStatusEndorseRI()){
                        tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShareEndorse(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                    }
                    
                    if (Tools.compare(tsiTemp, trd.getDbTreatyLimit()) > 0)
                        tsiTemp = trd.getDbTreatyLimit();
                    
                    if(BDUtil.isNegative(tsiTemp)){
                        if (Tools.compare(tsiTemp, BDUtil.negate(trd.getDbTreatyLimit())) < 0){
                            tsiTemp = BDUtil.negate(trd.getDbTreatyLimit());
                        }  
                    }

                    if(!BDUtil.isZeroOrNull(trd.getDbTSIFactor1())){
                            tsiTemp = BDUtil.mul(
                                        tsiTemp,
                                        BDUtil.getRateFromPct(trd.getDbTSIFactor1()), scale
                                        );
                    }

                } else {
                    tsiTemp = BDUtil.mul(
                            trd.getDbBaseTSIAmount(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                }
            }

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
            else trd.setDbTSIAmount(tsiTemp);         
            
            if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                if(policy.isStatusPolicy()){

                    if(!Tools.isYes(trd.getStEditFlag())){
                        if(!trd.getTreatyDetail().isExcessOfLoss()){
                            if(trd.getTreatyDetail().isOR()){
                                if(objx.getDbReference1()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference1()); //OR
                            }else if(trd.getTreatyDetail().isFacultativeObligatory1()){
                                if(objx.getDbReference2()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference2()); //FACO1
                            }else if(trd.getTreatyDetail().isFacultativeObligatory3()){
                                if(objx.getDbReference3()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference3()); //FACO2
                            }else if(trd.getTreatyDetail().isFacultative()){
                                if(objx.getDbReference4()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference4()); //FAC
                            }
                        }
                    }                  
                }
            }
            
            dbInsuredAmount = BDUtil.sub(dbInsuredAmount, trd.getDbTSIAmount());
            
            BigDecimal premiOR = BDUtil.div(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale), totalInsured);
            
            if(BDUtil.isZero(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale)) && BDUtil.isZero(totalInsured)){
                premiOR = BDUtil.zero;
            }

            if(isEndorse||policy.isStatusEndorseRI())
                if(BDUtil.isZeroOrNull(trd.getDbTSIAmount()) && !BDUtil.isZeroOrNull(dbPremi)){
                    premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiSaldo, scale);

                    if (parent != null){
                        premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiTotal, scale);
                    }
                }

            //perhitungan ONR
            if(trd.getTreatyDetail().isPremiumCommFactor()){
                if(BDUtil.biggerThan(komisiPct, trd.getTreatyDetail().getDbPremiumCommFactorLimit())){
                    premiOR = BDUtil.sub(premiOR, BDUtil.mul(premiOR, BDUtil.getRateFromPct(komisiPct)));
                }
            }

            //logger.logDebug("############## object = "+ ipo.getStObjectDescription());
            //logger.logDebug("############## premi awal treaty detail "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" = "+ premiOR);

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbPremiAmount(trd.getDbPremiAmount());
            else trd.setDbPremiAmount(premiOR);
            
            premiSaldo = BDUtil.sub(premiSaldo, trd.getDbPremiAmount());
            
            if (parent != null) {
                if (trd.getDbPremiRatePct() != null) {
                    trd.setDbPremiAmount(
                            BDUtil.mul(
                            parent.getDbPremiAmount(),
                            BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale
                            )
                            );
                }
            }

            //logger.logDebug("############## premi awal treaty detail "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" = "+ trd.getDbPremiAmount());

            trd.setDbComission(BDUtil.mul(trd.getDbPremiAmount(), BDUtil.getRateFromPct(trd.getDbComissionRate()), scale));
            
            final DTOList shares = trd.getShares();
            
            final BigDecimal preminet = trd.getDbPremiAmount();//BDUtil.sub(trd.getDbPremiAmount(), trd.getDbComission());
            
            BigDecimal premitot = null;
            BigDecimal commtot = null;
            
            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();
            
            for (int j = 0; j < shares.size(); j++) {
                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);
                
                //final String auto_premi = (String) ri.getControlMap().get("AUTO_PREMI");
                //final String PREMI_FACTOR = (String) ri.getControlMap().get("PREMI_FACTOR");
                
                //final boolean isTSIPremiFactor = "TSI".equalsIgnoreCase(PREMI_FACTOR);
                
                final BigDecimal spct = BDUtil.getRateFromPct(ri.getDbSharePct());
                
                if (ri.getDbTSIAmount() != null && ri.getDbSharePct() == null)
                    ri.setDbTSIAmount(ri.getDbTSIAmount());
                else
                    ri.setDbTSIAmount(BDUtil.mul(trd.getDbTSIAmount(), spct, 2));

                if(!policy.isStatusEndorseRI()){
                    /*if (auto_premi != null) {
                        if (ri.isAutoRate()) {
                            ri.setDbPremiRate((BigDecimal) ipo.getProperty(auto_premi));
                        }
                    }*/
                    //ri.setDbPremiAmount(null);
                    
                    if (nonProportional) {
                        if (ri.isUseRate()) {
                                if (ri.isAutoRate()) {
                                    ri.setDbPremiRate(null);
                                    ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                                } else{
                                    
                                    ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                                    
                                        if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                                             final DTOList cov = ipo.getCoverage();
                                             InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(0);
                                                if(cover.getStInsuranceCoverID().equalsIgnoreCase("143")){
                                                    if(trd.getTreatyDetail().isFacultativeObligatory1()||trd.getTreatyDetail().isFacultativeObligatory3())
                                                        ri.setDbPremiAmount(BDUtil.divRoundUp(ri.getDbPremiAmount(),new BigDecimal(365), scale));
                                                }
                                        }  
                                }
                                    
                         }
                    } else {
                        if (trd.getTreatyDetail().isOR()) {
                            BigDecimal premRat = BDUtil.div(trd.getDbTSIAmount(), ipo.getDbObjectInsuredAmount(), 19);
                            
                            BigDecimal orPremi = BDUtil.mul(premRat, ipo.getDbObjectPremiTotalAmount(), scale);
                            
                            BigDecimal premiRate = BDUtil.div(orPremi, trd.getDbTSIAmount(), 18);
                            
                            if(!ri.isAutoRate()&&!ri.isUseRate()){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }else if(ri.isAutoRate()&&ri.isUseRate()){
                                rateActual2 = BDUtil.getPctFromRate(premiRate);
                                ri.setDbPremiRate(rateActual2);

                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));

                            }else if(ri.isUseRate()){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }
                            
                            if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                                //if(policy.isStatusPolicy()){
                                if(ri.isUseRate()){
                                    rateActual2 = BDUtil.getPctFromRate(premiRate);
                                    ri.setDbPremiRate(rateActual2);
                                    ri.setStAutoRateFlag(null);
                                    ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                                }else if(!ri.isAutoRate()&&!ri.isUseRate()){
                                    ri.setDbPremiAmount(ri.getDbPremiAmount());
                                }
                            }
                        } else {
                            boolean autoRate = false;
                            
                            if(ri.getStAutoRateFlag()!=null)
                                autoRate = ri.getStAutoRateFlag().equalsIgnoreCase("Y")?true:false;
                            
                            if(ri.getDbPremiRate()==null && !autoRate){
                                ri.setStUseRateFlag("Y");
                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                            }else if(ri.getDbPremiRate()!=null){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }else if(autoRate && ri.getDbPremiRate()==null){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }
                         }
                    }
                }

                //logger.logDebug("############## premi 1 ri  "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" "+ ri.getEntity().getStEntityName()+" = "+ ri.getDbPremiAmount());

                //perhitungan jika EQ maka di kali scale pct
                if(!BDUtil.isZeroOrNull(trd.getDbTSIFactor1())){
                    ri.setDbPremiAmount(BDUtil.div(ri.getDbPremiAmount(),BDUtil.getRateFromPct(trd.getDbTSIFactor1()),15));
                }

                //logger.logDebug("############## premi 2 ri  "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" "+ ri.getEntity().getStEntityName()+" = "+ ri.getDbPremiAmount());


                if(!policy.isStatusEndorseRI() && !policy.isStatusEndorse()){
                    //cek jika ada diskon premi
                    if(ri.getTreatyShares()!=null){
                        if(ri.getTreatyShares().isMaxCommissionFlags()){
                            if(ri.getTreatyShares().getDbMaxCommissionPct()!=null){
                                //if(BDUtil.biggerThanEqual(komisiPct, ri.getTreatyShares().getDbMaxCommissionPct()))
                                {

                                    BigDecimal discountPct = komisiPct;

                                    if(ri.getTreatyShares().getDbDiscountPct()!=null)
                                            discountPct = ri.getTreatyShares().getDbDiscountPct();

                                    //BigDecimal discount = BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getTreatyShares().getDbDiscountPct()), scale);
                                    BigDecimal discount = BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(discountPct), scale);

                                    BigDecimal premiAfterDiscount = BDUtil.sub(ri.getDbPremiAmount(), discount);

                                    ri.setDbPremiAmount(premiAfterDiscount);
                                }
                            }
                        }
                    }

                }

                //logger.logDebug("############## premi 3 ri  "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" "+ ri.getEntity().getStEntityName()+" = "+ ri.getDbPremiAmount());


                if(policy.isStatusEndorseRI()){
                    if(!ri.isAutoRate() && !ri.isUseRate()) ri.setDbPremiAmount(ri.getDbPremiAmount());
                    else ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(),BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                }

                //logger.logDebug("############## premi 4 ri  "+ trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName() +" "+ ri.getEntity().getStEntityName()+" = "+ ri.getDbPremiAmount());


                if (ri.getDbRICommRate() == null) ri.setDbRICommRate(trd.getDbComissionRate());

                if(trd.getTreatyDetail().isPremiumCommFactor()){
                    if(BDUtil.biggerThan(komisiPct, trd.getTreatyDetail().getDbPremiumCommFactorLimit())){
                        ri.setDbRICommRate(ri.getTreatyShares().getDbRICommONRRate());
                    }else{
                        ri.setDbRICommRate(ri.getTreatyShares().getDbRICommRate());
                    }
                }

                //cek limit outgo askred, jika outgo > maks total komisi, maka pakai limit ini
                 if(ri.getTreatyShares()!=null){
                     if(ri.getTreatyShares().isMaxCommissionFlags()){
                         if(ri.getTreatyShares().getDbRICommMoreThanMaxCommRate()!=null){
                            if(BDUtil.biggerThan(komisiPct, ri.getTreatyShares().getDbMaxCommissionPct()))
                                ri.setDbRICommRate(ri.getTreatyShares().getDbRICommMoreThanMaxCommRate());
                            else
                                ri.setDbRICommRate(ri.getTreatyShares().getDbRICommRate());
                        }
                     }
                 }

                if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));

                //FORMULA HITUNG SISA PREMI BUANGAN
                if(!BDUtil.isZeroOrNull(ri.getDbSharePct()))
                    premiBelumTersesi = BDUtil.sub(premiBelumTersesi, ri.getDbPremiAmount());               
       
                //logger.logWarning("#################### premi sisa = "+ premiBelumTersesi);
                //END FORMULA HITUNG SISA PREMI BUANGAN

                //PENOMORAN SURAT HUTANG
                ri.setStStatementOfAccountNo(generateSTOANo(policy,ri,trd.getTreatyDetail()));
                //END SURAT HUTANG

                //SET TANGGAL BPPDAN
                if(trd.getTreatyDetail().isBPDAN())
                    ri.setDtValidReinsuranceDate(getTanggalReas(policy, objx, j));

                //PENOMORAN RI SLIP OTOMATIS
                if(ri.getTreatyShares()!=null)
                    if(ri.getTreatyShares().getStRISlipNoFormat()!=null){
                        String riSlipNo = generateRISlipNo(policy,ri);

                        if(riSlipNo!=null)
                            ri.setStRISlipNo(riSlipNo);
                    }
                        
                
                //perhitungan cover ri terbaru
                if(trd.getTreatyDetail().getStInsuranceCoverID()!=null){
                    trd.setDbTSIAmount(BDUtil.zero);        
                    trd.setDbPremiAmount(BDUtil.zero);
                    trd.setDbComission(BDUtil.zero);
                    ri.setDbTSIAmount(BDUtil.zero);
                    ri.setDbPremiAmount(BDUtil.zero);
                    ri.setDbRICommAmount(BDUtil.zero);
                }
                
                try {
                    final DTOList coverPolis = ipo.getCoverage();
                    for (int l = 0; l < coverPolis.size(); l++) {
                        InsurancePolicyCoverView coverPol = (InsurancePolicyCoverView) coverPolis.get(l);
                        
                        final String coverID = trd.getTreatyDetail().getStInsuranceCoverID()!=null?trd.getTreatyDetail().getStInsuranceCoverID():"";
                        
                        if(coverID.equalsIgnoreCase(coverPol.getStInsuranceCoverID())){
                            trd.setDbTSIAmount(tsiTemp);
                            ri.setDbTSIAmount(BDUtil.mul(coverPol.getDbInsuredAmount(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbTSIAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(coverPol.getDbPremi(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            if(!trd.getDbTSIAmount().equals(ri.getDbTSIAmount())) trd.setDbTSIAmount(ri.getDbTSIAmount());
                            
                            if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                            else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));
                        }

                        //HITUNG PREMI PER PERILS PER TREATY DETIL
                        BigDecimal premiPerilsPerTreaty = BDUtil.mul(BDUtil.div(trd.getDbPremiAmount(), ipo.getDbObjectPremiTotalAmount(),5), coverPol.getDbPremiNew());

                        if (l == 0) {
                            ri.setStInsuranceCoverID1(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover1(ri.getDbTSIAmount());
                            ri.setDbRateCover1(coverPol.getDbRate());
                            ri.setDbPremiumCover1(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 1) {
                            ri.setStInsuranceCoverID2(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover2(ri.getDbTSIAmount());
                            ri.setDbRateCover2(coverPol.getDbRate());
                            ri.setDbPremiumCover2(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 2) {
                            ri.setStInsuranceCoverID3(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover3(ri.getDbTSIAmount());
                            ri.setDbRateCover3(coverPol.getDbRate());
                            ri.setDbPremiumCover3(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 3) {
                            ri.setStInsuranceCoverID4(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover4(ri.getDbTSIAmount());
                            ri.setDbRateCover4(coverPol.getDbRate());
                            ri.setDbPremiumCover4(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 4) {
                            ri.setStInsuranceCoverID5(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover5(ri.getDbTSIAmount());
                            ri.setDbRateCover5(coverPol.getDbRate());
                            ri.setDbPremiumCover5(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 5) {
                            ri.setStInsuranceCoverID6(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover6(ri.getDbTSIAmount());
                            ri.setDbRateCover6(coverPol.getDbRate());
                            ri.setDbPremiumCover6(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 6) {
                            ri.setStInsuranceCoverID7(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover7(ri.getDbTSIAmount());
                            ri.setDbRateCover7(coverPol.getDbRate());
                            ri.setDbPremiumCover7(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 7) {
                            ri.setStInsuranceCoverID8(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover8(ri.getDbTSIAmount());
                            ri.setDbRateCover8(coverPol.getDbRate());
                            ri.setDbPremiumCover8(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 8) {
                            ri.setStInsuranceCoverID9(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover9(ri.getDbTSIAmount());
                            ri.setDbRateCover9(coverPol.getDbRate());
                            ri.setDbPremiumCover9(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 9) {
                            ri.setStInsuranceCoverID10(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover10(ri.getDbTSIAmount());
                            ri.setDbRateCover10(coverPol.getDbRate());
                            ri.setDbPremiumCover10(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 10) {
                            ri.setStInsuranceCoverID11(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover11(ri.getDbTSIAmount());
                            ri.setDbRateCover11(coverPol.getDbRate());
                            ri.setDbPremiumCover11(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 11) {
                            ri.setStInsuranceCoverID12(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover12(ri.getDbTSIAmount());
                            ri.setDbRateCover12(coverPol.getDbRate());
                            ri.setDbPremiumCover12(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 12) {
                            ri.setStInsuranceCoverID13(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover13(ri.getDbTSIAmount());
                            ri.setDbRateCover13(coverPol.getDbRate());
                            ri.setDbPremiumCover13(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 13) {
                            ri.setStInsuranceCoverID14(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover14(ri.getDbTSIAmount());
                            ri.setDbRateCover14(coverPol.getDbRate());
                            ri.setDbPremiumCover14(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        if (l == 14) {
                            ri.setStInsuranceCoverID15(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover15(ri.getDbTSIAmount());
                            ri.setDbRateCover15(coverPol.getDbRate());
                            ri.setDbPremiumCover15(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }
                        
                        //if (l == 15)
                            //throw new RuntimeException("Jumlah Cover Lebih Dari 15 !");
                        
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                //hitung cicilan premi reas
                ri.reCalculateInstallment(policy.getDtPolicyDate());  
                
                premitot = BDUtil.add(premitot, ri.getDbPremiAmount());
                commtot = BDUtil.add(commtot, ri.getDbRICommAmount());
            }

            //SET NILAI SISA PREMI BUANGAN

            //if(!isEndorse){
                if(policy.getStPolicyTypeID().equalsIgnoreCase("59") || policy.getStPolicyTypeID().equalsIgnoreCase("80")
                    || policy.getStPolicyTypeID().equalsIgnoreCase("87") ||policy.getStPolicyTypeID().equalsIgnoreCase("88")){

                    for (int j = 0; j < shares.size(); j++) {
                        InsurancePolicyReinsView riJ = (InsurancePolicyReinsView) shares.get(j);

                        if(riJ.getTreatyShares()!=null){
                            if(riJ.getTreatyShares().isPremiExcessFlag()){
                                riJ.setStAutoRateFlag("Y");
                                riJ.setDbPremiAmount(premiBelumTersesi);
                                riJ.setDbRICommAmount(premiBelumTersesi);

                                //premitot = BDUtil.add(premitot, riJ.getDbPremiAmount());
                                //commtot = BDUtil.add(commtot, riJ.getDbRICommAmount());
                            }
                        }
                        //END FORMULA HITUNG SISA PREMI BUANGAN
                    }
                }
            //}

            if (premitot != null)
                trd.setDbPremiAmount(premitot);
            
            if (commtot != null)
                trd.setDbComission(commtot);
        }
        
    }
    
    public void calculateEndorsemen(InsurancePolicyObjectView ipo, BigDecimal dbInsuredAmount, BigDecimal dbPremi, String stPolicyTypeGroupID, BigDecimal dbCurrencyRate, BigDecimal dbInsuranceCoinsScale) throws Exception {
        
        getDetails();
        
        final InsurancePolicyView policy = ipo.getPolicy();
        boolean nextEndorse = false;
        if(policy.getStNextStatus()!=null) nextEndorse = policy.getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE);
        final boolean isEndorse = policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE) || nextEndorse;
        
        if (!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        BigDecimal totalInsured = dbInsuredAmount;
        BigDecimal dbInsuredAmountEndorse = null;
        BigDecimal premiSaldo = dbPremi;
        BigDecimal premiTotal = dbPremi;
 
        if (isEndorse){
            dbInsuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
            dbInsuredAmount = dbInsuredAmountEndorse;
            totalInsured = dbInsuredAmount;
        } 
        
        BigDecimal tsiTemp = new BigDecimal(0);
        BigDecimal rateActual2 = new BigDecimal(0);
        
        final HashMap detailMapByInsTreatyDetailID = details.getMapOf("ins_treaty_detail_id");
        
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            if (policy.hasCoIns()) {
                try {
                    trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(dbInsuranceCoinsScale)));
                } catch (Exception e) {
                }
            }
            
            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE))
                ratioZone = ipo.getDbTreatyLimitRatioMaipark();

            if (Tools.compare(trd.getDbTreatyLimit(), dbInsuredAmount) >= 0) {
                trd.setDbBaseTSIAmount(dbInsuredAmount);
            } else {
                trd.setDbBaseTSIAmount(trd.getDbTreatyLimit());
            }
                        
            InsurancePolicyTreatyDetailView parent = null;
            if (trd.getStParentID() != null) {
                parent = (InsurancePolicyTreatyDetailView) detailMapByInsTreatyDetailID.get(trd.getStParentID());
            }
            
            if (parent != null) {
                tsiTemp = BDUtil.mul(
                        parent.getDbBaseTSIAmount(),
                        BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                        );
                
           } else {
                persenMaipark = new BigDecimal(100).subtract(ratioZone);
                                
                if (trd.getTreatyDetail().getDbTSIMax() != null) {
                    trd.setDbTreatyLimit(BDUtil.div(trd.getTreatyDetail().getDbTSIMax(), dbCurrencyRate, scale));
                    if(policy.hasCoIns()){
                        trd.setDbTreatyLimit(BDUtil.mul(trd.getTreatyDetail().getDbTSIMax(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct())));
                    }
                    //trd.setDbTreatyLimit(BDUtil.mul(trd.getTreatyDetail().getDbTSIMax(), BDUtil.getRateFromPct(dbInsuranceCoinsScale)));
                    if(policy.getDbSharePct()!=null)
                        if(trd.getTreatyDetail().isMaipark())
                            trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(policy.getDbSharePct()), scale));

                    if (BDUtil.isZeroOrNull(trd.getDbTSIPct())) trd.setDbTSIPct(persenMaipark);
                    tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShare(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                    
                    if(isEndorse||policy.isStatusEndorseRI()){
                        tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShareEndorse(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                    }
                    
                     if (Tools.compare(tsiTemp, trd.getDbTreatyLimit()) > 0)
                        tsiTemp = trd.getDbTreatyLimit();
                } else {
                    tsiTemp = BDUtil.mul(
                            trd.getDbBaseTSIAmount(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                }
            }

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
            else trd.setDbTSIAmount(tsiTemp);

            /*
            if (SessionManager.getInstance().getSession().hasResource("MANUAL_TREATY")) {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
                else trd.setDbTSIAmount(tsiTemp);
            } else {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
                else trd.setDbTSIAmount(tsiTemp);
            }*/
            
            dbInsuredAmount = BDUtil.sub(dbInsuredAmount, trd.getDbTSIAmount());
            
            BigDecimal premiOR = BDUtil.div(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale), totalInsured);
            
            if(BDUtil.isZero(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale)) && BDUtil.isZero(totalInsured)){
                premiOR = BDUtil.zero;
            }

            if(isEndorse||policy.isStatusEndorseRI())
                if(BDUtil.isZeroOrNull(trd.getDbTSIAmount()) && !BDUtil.isZeroOrNull(dbPremi)){
                    premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiSaldo, scale);
                    if (parent != null){
                        premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiTotal, scale);
                    }
                }

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbPremiAmount(trd.getDbPremiAmount());
            else trd.setDbPremiAmount(premiOR);

            /*
            if (SessionManager.getInstance().getSession().hasResource("MANUAL_TREATY")) {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbPremiAmount(trd.getDbPremiAmount());
                else trd.setDbPremiAmount(premiOR);
            } else {
                if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbPremiAmount(trd.getDbPremiAmount());
                else trd.setDbPremiAmount(premiOR);
            }*/
            
            premiSaldo = BDUtil.sub(premiSaldo, trd.getDbPremiAmount());
            
            if (parent != null) {
                if (trd.getDbPremiRatePct() != null) {
                    trd.setDbPremiAmount(
                            BDUtil.mul(
                            parent.getDbPremiAmount(),
                            BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale
                            )
                            );
                }
               
            }
            
            trd.setDbComission(BDUtil.mul(trd.getDbPremiAmount(), BDUtil.getRateFromPct(trd.getDbComissionRate()), scale));
            
            final DTOList shares = trd.getShares();
            
            final BigDecimal preminet = trd.getDbPremiAmount();//BDUtil.sub(trd.getDbPremiAmount(), trd.getDbComission());
            
            BigDecimal premitot = null;
            BigDecimal commtot = null;
            
            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();
            
            for (int j = 0; j < shares.size(); j++) {
                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);
                
                //final String auto_premi = (String) ri.getControlMap().get("AUTO_PREMI");
                //final String PREMI_FACTOR = (String) ri.getControlMap().get("PREMI_FACTOR");
                
                //final boolean isTSIPremiFactor = "TSI".equalsIgnoreCase(PREMI_FACTOR);
                
                final BigDecimal spct = BDUtil.getRateFromPct(ri.getDbSharePct());
                
                if (ri.getDbTSIAmount() != null && ri.getDbSharePct() == null)
                    ri.setDbTSIAmount(ri.getDbTSIAmount());
                else
                    ri.setDbTSIAmount(BDUtil.mul(trd.getDbTSIAmount(), spct, 2));
                
                if(!policy.isStatusEndorseRI()){
                    /*if (auto_premi != null) {
                        if (ri.isAutoRate()) {
                            ri.setDbPremiRate((BigDecimal) ipo.getProperty(auto_premi));
                        }
                    }*/
                    
                    if (nonProportional) {
                        if (ri.isUseRate()) {
                                if (ri.isAutoRate()) {
                                    ri.setDbPremiRate(null);
                                    ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                                } else
                                    ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                         }
                    } else {
                        if (trd.getTreatyDetail().isOR()) {
                            BigDecimal premRat = BDUtil.div(trd.getDbTSIAmount(), ipo.getDbObjectInsuredAmount(), 19);
                            
                            BigDecimal orPremi = BDUtil.mul(premRat, ipo.getDbObjectPremiTotalAmount(), scale);
                            
                            BigDecimal premiRate = BDUtil.div(orPremi, trd.getDbTSIAmount(), 18);
                            
                            if(!ri.isAutoRate()&&!ri.isUseRate()){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }else if(ri.isAutoRate()&&ri.isUseRate()){
                                rateActual2 = BDUtil.getPctFromRate(premiRate);
                                ri.setDbPremiRate(rateActual2);
                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                            }else if(ri.isUseRate()){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }
                        } else {
                            boolean autoRate = false;
                            
                            if(ri.getStAutoRateFlag()!=null)
                                autoRate = ri.getStAutoRateFlag().equalsIgnoreCase("Y")?true:false;
                            
                            if(ri.getDbPremiRate()==null && !autoRate){
                                ri.setStUseRateFlag("Y");
                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                            }else if(ri.getDbPremiRate()!=null){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }else if(autoRate && ri.getDbPremiRate()==null){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }
                         }
                    }
                }

                if(policy.isStatusEndorseRI()){
                    BigDecimal rateActual3 = BDUtil.zero;
                    
                    if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT))
                        rateActual3 = BDUtil.getRateFromPct(ri.getDbPremiRate());
                    else if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL))
                        rateActual3 = BDUtil.getRateFromMile(ri.getDbPremiRate());

                    if(!ri.isAutoRate() && !ri.isUseRate()) ri.setDbPremiAmount(ri.getDbPremiAmount());
                    else ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(),BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                    
                    /*
                    if (parent != null){
                        if (trd.getDbPremiRatePct() != null)
                            if(ri.getDbTSIAmount()==null||BDUtil.isZero(ri.getDbTSIAmount()))
                                ri.setDbPremiAmount(BDUtil.mul(parent.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                        ri.setDbPremiAmount(BDUtil.mul(ri.getDbPremiAmount(),BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                        
                    }*/
                }

                if (ri.getDbRICommRate() == null) ri.setDbRICommRate(trd.getDbComissionRate());
                
                if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));

                //perhitungan cover ri terbaru
                if(trd.getTreatyDetail().getStInsuranceCoverID()!=null){
                    trd.setDbTSIAmount(BDUtil.zero);        
                    trd.setDbPremiAmount(BDUtil.zero);
                    trd.setDbComission(BDUtil.zero);
                    ri.setDbTSIAmount(BDUtil.zero);
                    ri.setDbPremiAmount(BDUtil.zero);
                    ri.setDbRICommAmount(BDUtil.zero);
                }
                
                try {
                    final DTOList coverPolis = ipo.getCoverage();
                    for (int l = 0; l < coverPolis.size(); l++) {
                        InsurancePolicyCoverView coverPol = (InsurancePolicyCoverView) coverPolis.get(l);
                        
                        final String coverID = trd.getTreatyDetail().getStInsuranceCoverID()!=null?trd.getTreatyDetail().getStInsuranceCoverID():"";
                        
                        if(coverID.equalsIgnoreCase(coverPol.getStInsuranceCoverID())){
                            trd.setDbTSIAmount(tsiTemp);
                            ri.setDbTSIAmount(BDUtil.mul(coverPol.getDbInsuredAmount(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbTSIAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(coverPol.getDbPremi(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            if(!trd.getDbTSIAmount().equals(ri.getDbTSIAmount())) trd.setDbTSIAmount(ri.getDbTSIAmount());
                            
                            if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                            else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));
                        }
                        
                        BigDecimal rateActual = new BigDecimal(0);
                        if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PCT))
                            rateActual = coverPol.getDbRatePct();
                        else if (getStRateMethod().equalsIgnoreCase(FinCodec.RateScale.PMIL))
                            rateActual = coverPol.getDbRateMile();
                        
                        if (l == 0) {
                            ri.setStInsuranceCoverID1(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover1(ri.getDbTSIAmount());
                            ri.setDbRateCover1(coverPol.getDbRate());
                            ri.setDbPremiumCover1(BDUtil.mul(ri.getDbTSICover1(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover1(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 1) {
                            ri.setStInsuranceCoverID2(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover2(ri.getDbTSIAmount());
                            ri.setDbRateCover2(coverPol.getDbRate());
                            ri.setDbPremiumCover2(BDUtil.mul(ri.getDbTSICover2(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover2(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 2) {
                            ri.setStInsuranceCoverID3(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover3(ri.getDbTSIAmount());
                            ri.setDbRateCover3(coverPol.getDbRate());
                            ri.setDbPremiumCover3(BDUtil.mul(ri.getDbTSICover3(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover3(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 3) {
                            ri.setStInsuranceCoverID4(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover4(ri.getDbTSIAmount());
                            ri.setDbRateCover4(coverPol.getDbRate());
                            ri.setDbPremiumCover4(BDUtil.mul(ri.getDbTSICover4(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover4(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 4) {
                            ri.setStInsuranceCoverID5(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover5(ri.getDbTSIAmount());
                            ri.setDbRateCover5(coverPol.getDbRate());
                            ri.setDbPremiumCover5(BDUtil.mul(ri.getDbTSICover5(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover5(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 5) {
                            ri.setStInsuranceCoverID6(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover6(ri.getDbTSIAmount());
                            ri.setDbRateCover6(coverPol.getDbRate());
                            ri.setDbPremiumCover6(BDUtil.mul(ri.getDbTSICover6(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover6(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 6) {
                            ri.setStInsuranceCoverID7(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover7(ri.getDbTSIAmount());
                            ri.setDbRateCover7(coverPol.getDbRate());
                            ri.setDbPremiumCover7(BDUtil.mul(ri.getDbTSICover7(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover7(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 7) {
                            ri.setStInsuranceCoverID8(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover8(ri.getDbTSIAmount());
                            ri.setDbRateCover8(coverPol.getDbRate());
                            ri.setDbPremiumCover8(BDUtil.mul(ri.getDbTSICover8(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover8(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 8) {
                            ri.setStInsuranceCoverID9(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover9(ri.getDbTSIAmount());
                            ri.setDbRateCover9(coverPol.getDbRate());
                            ri.setDbPremiumCover9(BDUtil.mul(ri.getDbTSICover9(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover9(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 9) {
                            ri.setStInsuranceCoverID10(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover10(ri.getDbTSIAmount());
                            ri.setDbRateCover10(coverPol.getDbRate());
                            ri.setDbPremiumCover10(BDUtil.mul(ri.getDbTSICover10(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover10(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 10) {
                            ri.setStInsuranceCoverID11(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover11(ri.getDbTSIAmount());
                            ri.setDbRateCover11(coverPol.getDbRate());
                            ri.setDbPremiumCover11(BDUtil.mul(ri.getDbTSICover11(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover11(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 11) {
                            ri.setStInsuranceCoverID12(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover12(ri.getDbTSIAmount());
                            ri.setDbRateCover12(coverPol.getDbRate());
                            ri.setDbPremiumCover12(BDUtil.mul(ri.getDbTSICover12(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover12(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 12) {
                            ri.setStInsuranceCoverID13(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover13(ri.getDbTSIAmount());
                            ri.setDbRateCover13(coverPol.getDbRate());
                            ri.setDbPremiumCover13(BDUtil.mul(ri.getDbTSICover13(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover13(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 13) {
                            ri.setStInsuranceCoverID14(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover14(ri.getDbTSIAmount());
                            ri.setDbRateCover14(coverPol.getDbRate());
                            ri.setDbPremiumCover14(BDUtil.mul(ri.getDbTSICover14(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover14(coverPol.getDbPremiNew());
                        }
                        
                        if (l == 14) {
                            ri.setStInsuranceCoverID15(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover15(ri.getDbTSIAmount());
                            ri.setDbRateCover15(coverPol.getDbRate());
                            ri.setDbPremiumCover15(BDUtil.mul(ri.getDbTSICover15(), rateActual, scale));
                            if(isEndorse||policy.isStatusEndorseRI()) ri.setDbPremiumCover15(coverPol.getDbPremiNew());
                        }
                        
                        //if (l == 15)
                            //throw new RuntimeException("Jumlah Cover Lebih Dari 15 !");
                        
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
                premitot = BDUtil.add(premitot, ri.getDbPremiAmount());
                commtot = BDUtil.add(commtot, ri.getDbRICommAmount());
            }
            
            if (premitot != null)
                trd.setDbPremiAmount(premitot);
            
            if (commtot != null)
                trd.setDbComission(commtot);
        }
        
    }
    
    private String generateSTOANo(InsurancePolicyView policy, InsurancePolicyReinsView ri,InsuranceTreatyDetailView tredet){
        //  FORMAT -> 6/ASEI/QS/1/2011/IDR
        Date tanggal = policy.getDtPolicyDate();
        
        if(policy.isStatusClaim()){
            tanggal = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();

            if(policy.isStatusClaimDLA())
                if(policy.getDtDLADate()==null)
                    throw new RuntimeException("Tanggal LKP tidak boleh kosong");
        }
            
        
        String STOANo = policy.getStPolicyTypeID() + "/" +
                        ri.getStMemberEntityID() + "/" +
                        tredet.getStTreatyTypeID()+ "/" +
                        DateUtil.getQuartal(tanggal) + "/" +
                        DateUtil.getMonthDigit(tanggal) + "/" +
                        DateUtil.getYear(tanggal) + "/" +
                        policy.getStCurrencyCode();
        
        return STOANo;
    }

    public void setTanggalReas() throws Exception{

        InsurancePolicyView polis = getObject().getPolicy();

        //if(polis.isStatusEndorse() || polis.isStatusRenewal() || polis.isStatusPolicy()){
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) getObject();

            //divide r/i by years
            Date policyDateStart = polis.getDtPeriodStart();

            if(polis.getStPolicyTypeGroupID().equalsIgnoreCase("1")){
                if(obj.getDtReference1()!=null) policyDateStart = obj.getDtReference1();
            }

               final DTOList treatyDetails = getDetails();

               for (int i = 0; i < treatyDetails.size(); i++) {
                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treatyDetails.get(i);

                    //if(!tredet.getTreatyDetail().isDivideByYears()) continue;

                    if(!tredet.getTreatyDetail().isBPDAN()) continue;

                    final DTOList shares = tredet.getShares();
                       for (int j = 0; j < shares.size(); j++) {
                           InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);

                           if(tredet.getTreatyDetail().isBPDAN()){
                                ri.setDtValidReinsuranceDate(DateUtil.advance(policyDateStart,j));

                                if(j==0){
                                    Date policyDate = polis.getDtPolicyDate();

                                    boolean withinCurrentMonth = DateUtil.getDateStr(policyDate, "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(policyDateStart, "yyyyMM"));

                                    if(!withinCurrentMonth)
                                        ri.setDtValidReinsuranceDate(policyDate);
                                    else
                                        ri.setDtValidReinsuranceDate(policyDateStart);
                                }
                           }
                       }
              }
        //}

        
    }

    private Date getTanggalReas(InsurancePolicyView polis,InsurancePolicyObjDefaultView obj, int counter) throws Exception{

            Date tanggalBppdan;

            //divide r/i by years
            Date policyDateStart = polis.getDtPeriodStart();

            if(polis.getStPolicyTypeGroupID().equalsIgnoreCase("1")){
                if(obj.getDtReference1()!=null) policyDateStart = obj.getDtReference1();
            }

              tanggalBppdan = DateUtil.advance(policyDateStart,counter);

              if(counter==0){
                    Date policyDate = polis.getDtPolicyDate();

                    boolean withinCurrentMonth = DateUtil.getDateStr(policyDate, "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(policyDateStart, "yyyyMM"));

                    if(!withinCurrentMonth)
                        tanggalBppdan = policyDate;
                    else
                        tanggalBppdan = policyDateStart;
                }

              return tanggalBppdan;
    }

    private String generateRISlipNo(InsurancePolicyView policy, InsurancePolicyReinsView ri){
        //  FORMAT -> 05001/04R/8/17/Obl
        Date tanggal = policy.getDtPolicyDate();

        InsuranceTreatySharesView share = ri.getTreatyShares();

        if(share.getStRISlipNoFormat()==null) return null;

        if(policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusClaimEndorse())
            return null;

        String RISlipNo = null;

        if(share.getStRISlipNoFormat()!=null)
            RISlipNo = share.getStRISlipNoFormat();

        RISlipNo = RISlipNo.replaceAll("M", String.valueOf(DateUtil.getMonthDigit(tanggal)));

        RISlipNo = RISlipNo.replaceAll("YY", DateUtil.getYear2Digit(tanggal));

        return RISlipNo;
    }

    public DTOList getDetailsInterkoneksi() {
        loadDetailsInterkoneksi();
        return details;
    }

    private void loadDetailsInterkoneksi() {
        try {
            if (details == null) {
                details =
                        ListUtil.getDTOListFromQueryDS(
                        "select a.*,b.treaty_type from ins_pol_treaty_detail a inner join ins_treaty_detail b on b.ins_treaty_detail_id = a.ins_treaty_detail_id where ins_pol_treaty_id = ? order by a.ins_pol_tre_det_id",
                        new Object[]{stInsurancePolicyTreatyID},
                        InsurancePolicyTreatyDetailView.class,"GATEWAY"
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void calculateUploadSpreading(InsurancePolicyObjectView ipo, BigDecimal dbInsuredAmount, BigDecimal dbPremi, String stPolicyTypeGroupID, BigDecimal dbCurrencyRate, BigDecimal dbInsuranceCoinsScale) throws Exception {

        getDetails();

        InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) ipo;

        final InsurancePolicyView policy = ipo.getPolicy();
        boolean nextEndorse = false;

        if(policy.getStNextStatus()!=null) nextEndorse = policy.getStNextStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE);
        final boolean isEndorse = policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE) || nextEndorse;

        //if (!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        //else scale = 0;

        scale = 2;

        BigDecimal totalInsured = dbInsuredAmount;
        BigDecimal dbInsuredAmountEndorse = null;
        BigDecimal premiSaldo = dbPremi;
        BigDecimal premiTotal = dbPremi;
        BigDecimal lossRatio = null;

        BigDecimal premiBelumTersesi = dbPremi;

        BigDecimal komisiPct = policy.getTotalCommPCT();

        if(isEndorse || policy.isStatusEndorseRI())
            komisiPct = policy.getTotalCommPCTInduk();

        if (isEndorse){
            dbInsuredAmountEndorse = ipo.getDbObjectInsuredAmountShareEndorse();
            dbInsuredAmount = dbInsuredAmountEndorse;
            totalInsured = dbInsuredAmount;
        }

        if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE)){
            DTOList covers = objx.getCoverage();
            if (covers!=null && covers.size()>0)
            {
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);
                lossRatio = cv.getCoverPolType().getDbTSIFactor1();
            }
             dbInsuredAmount = BDUtil.mul(dbInsuredAmount,BDUtil.getRateFromPct(lossRatio), scale);
        }

        BigDecimal tsiTemp = new BigDecimal(0);
        BigDecimal rateActual2 = new BigDecimal(0);

        final HashMap detailMapByInsTreatyDetailID = details.getMapOf("ins_treaty_detail_id");

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) details.get(i);
            if (policy.hasCoIns()) {
                try {
                    trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(dbInsuranceCoinsScale)));
                } catch (Exception e) {
                }
            }

            if (stPolicyTypeGroupID.equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.EARTHQUAKE))
                ratioZone = ipo.getDbTreatyLimitRatioMaipark();

            if (Tools.compare(trd.getDbTreatyLimit(), dbInsuredAmount) >= 0) {
                trd.setDbBaseTSIAmount(dbInsuredAmount);
            } else {
                trd.setDbBaseTSIAmount(trd.getDbTreatyLimit());
            }

            if(BDUtil.isNegative(dbInsuredAmount)){
                if (Tools.compare(trd.getDbTreatyLimit(), BDUtil.negate(dbInsuredAmount)) >= 0) {
                    trd.setDbBaseTSIAmount(dbInsuredAmount);
                } else {
                    trd.setDbBaseTSIAmount(BDUtil.negate(trd.getDbTreatyLimit()));
                }
            }

            InsurancePolicyTreatyDetailView parent = null;
            if (trd.getStParentID() != null) {
                parent = (InsurancePolicyTreatyDetailView) detailMapByInsTreatyDetailID.get(trd.getStParentID());
            }

            if (parent != null) {
                tsiTemp = BDUtil.mul(
                        parent.getDbBaseTSIAmount(),
                        BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                        );

           } else {
                persenMaipark = new BigDecimal(100).subtract(ratioZone);

                if (trd.getTreatyDetail().getDbTSIMax() != null) {
                    trd.setDbTreatyLimit(BDUtil.div(trd.getTreatyDetail().getDbTSIMax(), dbCurrencyRate, scale));
                    if(policy.hasCoIns()){
                        trd.setDbTreatyLimit(BDUtil.mul(trd.getTreatyDetail().getDbTSIMax(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct())));
                    }

                    if(policy.getDbSharePct()!=null)
                        if(trd.getTreatyDetail().isMaipark())
                            trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(policy.getDbSharePct()), scale));

                    if(policy.isStatusInward()){
                        if(trd.getTreatyDetail().getDbInwardCapacityPct()!=null){
                            trd.setDbTreatyLimit(BDUtil.mul(trd.getDbTreatyLimit(), BDUtil.getRateFromPct(trd.getTreatyDetail().getDbInwardCapacityPct())));
                        }
                    }

                    if (BDUtil.isZeroOrNull(trd.getDbTSIPct())) trd.setDbTSIPct(persenMaipark);

                    tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShare(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );

                    if(isEndorse||policy.isStatusEndorseRI()){
                        tsiTemp = BDUtil.mul(
                            ipo.getDbObjectInsuredAmountShareEndorse(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                    }

                    if (Tools.compare(tsiTemp, trd.getDbTreatyLimit()) > 0)
                        tsiTemp = trd.getDbTreatyLimit();

                    if(BDUtil.isNegative(tsiTemp)){
                        if (Tools.compare(tsiTemp, BDUtil.negate(trd.getDbTreatyLimit())) < 0){
                            tsiTemp = BDUtil.negate(trd.getDbTreatyLimit());
                        }
                    }

                    if(!BDUtil.isZeroOrNull(trd.getDbTSIFactor1())){
                            tsiTemp = BDUtil.mul(
                                        tsiTemp,
                                        BDUtil.getRateFromPct(trd.getDbTSIFactor1()), scale
                                        );
                    }

                } else {
                    tsiTemp = BDUtil.mul(
                            trd.getDbBaseTSIAmount(),
                            BDUtil.getRateFromPct(trd.getDbTSIPct()), scale
                            );
                }
            }

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbTSIAmount(trd.getDbTSIAmount());
            else trd.setDbTSIAmount(tsiTemp);

            if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                if(policy.isStatusPolicy()){

                    if(!Tools.isYes(trd.getStEditFlag())){
                        if(!trd.getTreatyDetail().isExcessOfLoss()){
                            if(trd.getTreatyDetail().isOR()){
                                if(objx.getDbReference1()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference1()); //OR
                            }else if(trd.getTreatyDetail().isFacultativeObligatory1()){
                                if(objx.getDbReference2()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference2()); //FACO1
                            }else if(trd.getTreatyDetail().isFacultativeObligatory3()){
                                if(objx.getDbReference3()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference3()); //FACO2
                            }else if(trd.getTreatyDetail().isFacultative()){
                                if(objx.getDbReference4()!=null)
                                    trd.setDbTSIAmount(objx.getDbReference4()); //FAC
                            }
                        }
                    }
                }
            }

            dbInsuredAmount = BDUtil.sub(dbInsuredAmount, trd.getDbTSIAmount());

            BigDecimal premiOR = BDUtil.div(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale), totalInsured);

            if(BDUtil.isZero(BDUtil.mul(dbPremi, trd.getDbTSIAmount(), scale)) && BDUtil.isZero(totalInsured)){
                premiOR = BDUtil.zero;
            }

            if(isEndorse||policy.isStatusEndorseRI())
                if(BDUtil.isZeroOrNull(trd.getDbTSIAmount()) && !BDUtil.isZeroOrNull(dbPremi)){
                    premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiSaldo, scale);

                    if (parent != null){
                        premiOR = BDUtil.mul(BDUtil.getRateFromPct(trd.getDbTSIPct()), premiTotal, scale);
                    }
                }

            //perhitungan ONR
            if(trd.getTreatyDetail().isPremiumCommFactor()){
                if(BDUtil.biggerThan(komisiPct, trd.getTreatyDetail().getDbPremiumCommFactorLimit())){
                    premiOR = BDUtil.sub(premiOR, BDUtil.mul(premiOR, BDUtil.getRateFromPct(komisiPct)));
                }
            }

            if (trd.getStEditFlag().equalsIgnoreCase("Y")) trd.setDbPremiAmount(trd.getDbPremiAmount());
            else trd.setDbPremiAmount(premiOR);

            premiSaldo = BDUtil.sub(premiSaldo, trd.getDbPremiAmount());

            if (parent != null) {
                if (trd.getDbPremiRatePct() != null) {
                    trd.setDbPremiAmount(
                            BDUtil.mul(
                            parent.getDbPremiAmount(),
                            BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale
                            )
                            );
                }
            }

            trd.setDbComission(BDUtil.mul(trd.getDbPremiAmount(), BDUtil.getRateFromPct(trd.getDbComissionRate()), scale));

            final DTOList shares = trd.getShares();

            final BigDecimal preminet = trd.getDbPremiAmount();//BDUtil.sub(trd.getDbPremiAmount(), trd.getDbComission());

            BigDecimal premitot = null;
            BigDecimal commtot = null;

            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

            for (int j = 0; j < shares.size(); j++) {
                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(j);

                //final String auto_premi = (String) ri.getControlMap().get("AUTO_PREMI");
                //final String PREMI_FACTOR = (String) ri.getControlMap().get("PREMI_FACTOR");

                //final boolean isTSIPremiFactor = "TSI".equalsIgnoreCase(PREMI_FACTOR);

                final BigDecimal spct = BDUtil.getRateFromPct(ri.getDbSharePct());

                if (ri.getDbTSIAmount() != null && ri.getDbSharePct() == null)
                    ri.setDbTSIAmount(ri.getDbTSIAmount());
                else
                    ri.setDbTSIAmount(BDUtil.mul(trd.getDbTSIAmount(), spct, 2));

                if(!policy.isStatusEndorseRI()){
                    /*if (auto_premi != null) {
                        if (ri.isAutoRate()) {
                            ri.setDbPremiRate((BigDecimal) ipo.getProperty(auto_premi));
                        }
                    }*/
                    //ri.setDbPremiAmount(null);

                    if (nonProportional) {
                        if (ri.isUseRate()) {
                                if (ri.isAutoRate()) {
                                    ri.setDbPremiRate(null);
                                    ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                                } else{

                                    ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));

                                        if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                                             final DTOList cov = ipo.getCoverage();
                                             InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(0);
                                                if(cover.getStInsuranceCoverID().equalsIgnoreCase("143")){
                                                    if(trd.getTreatyDetail().isFacultativeObligatory1()||trd.getTreatyDetail().isFacultativeObligatory3())
                                                        ri.setDbPremiAmount(BDUtil.divRoundUp(ri.getDbPremiAmount(),new BigDecimal(365), scale));
                                                }
                                        }
                                }

                         }
                    } else {
                        if (trd.getTreatyDetail().isOR()) {
                            BigDecimal premRat = BDUtil.div(trd.getDbTSIAmount(), ipo.getDbObjectInsuredAmount(), 19);

                            BigDecimal orPremi = BDUtil.mul(premRat, ipo.getDbObjectPremiTotalAmount(), scale);

                            BigDecimal premiRate = BDUtil.div(orPremi, trd.getDbTSIAmount(), 18);

                            if(!ri.isAutoRate()&&!ri.isUseRate()){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }else if(ri.isAutoRate()&&ri.isUseRate()){
                                rateActual2 = BDUtil.getPctFromRate(premiRate);
                                ri.setDbPremiRate(rateActual2);

                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));

                            }else if(ri.isUseRate()){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                                //if(policy.isStatusPolicy()){
                                if(ri.isUseRate()){
                                    rateActual2 = BDUtil.getPctFromRate(premiRate);
                                    ri.setDbPremiRate(rateActual2);
                                    ri.setStAutoRateFlag(null);
                                    ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                                }else if(!ri.isAutoRate()&&!ri.isUseRate()){
                                    ri.setDbPremiAmount(ri.getDbPremiAmount());
                                }
                            }
                        } else {
                            boolean autoRate = false;

                            if(ri.getStAutoRateFlag()!=null)
                                autoRate = ri.getStAutoRateFlag().equalsIgnoreCase("Y")?true:false;

                            if(ri.getDbPremiRate()==null && !autoRate){
                                ri.setStUseRateFlag("Y");
                                ri.setDbPremiAmount(BDUtil.mul(preminet, BDUtil.getRateFromPct(ri.getDbSharePct()), scale));
                            }else if(ri.getDbPremiRate()!=null){
                                ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                            }else if(autoRate && ri.getDbPremiRate()==null){
                                ri.setDbPremiAmount(ri.getDbPremiAmount());
                            }
                         }
                    }
                }

                //perhitungan jika EQ maka di kali scale pct
                if(!BDUtil.isZeroOrNull(trd.getDbTSIFactor1())){
                    ri.setDbPremiAmount(BDUtil.div(ri.getDbPremiAmount(),BDUtil.getRateFromPct(trd.getDbTSIFactor1()),15));
                }

                if(!policy.isStatusEndorseRI()){
                    //cek jika ada diskon premi
                    if(ri.getTreatyShares()!=null){
                        if(ri.getTreatyShares().isMaxCommissionFlags()){
                            if(ri.getTreatyShares().getDbMaxCommissionPct()!=null){
                                //if(BDUtil.biggerThanEqual(komisiPct, ri.getTreatyShares().getDbMaxCommissionPct()))
                                {

                                    BigDecimal discountPct = komisiPct;

                                    if(ri.getTreatyShares().getDbDiscountPct()!=null)
                                            discountPct = ri.getTreatyShares().getDbDiscountPct();

                                    //BigDecimal discount = BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getTreatyShares().getDbDiscountPct()), scale);
                                    BigDecimal discount = BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(discountPct), scale);

                                    BigDecimal premiAfterDiscount = BDUtil.sub(ri.getDbPremiAmount(), discount);

                                    ri.setDbPremiAmount(premiAfterDiscount);
                                }
                            }
                        }
                    }

                }


                if(policy.isStatusEndorseRI()){
                    if(!ri.isAutoRate() && !ri.isUseRate()) ri.setDbPremiAmount(ri.getDbPremiAmount());
                    else ri.setDbPremiAmount(BDUtil.mul(ri.getDbTSIAmount(),BDUtil.getRateFromPct(ri.getDbPremiRate()), scale));
                }

                if (ri.getDbRICommRate() == null) ri.setDbRICommRate(trd.getDbComissionRate());

                if(trd.getTreatyDetail().isPremiumCommFactor()){
                    if(BDUtil.biggerThan(komisiPct, trd.getTreatyDetail().getDbPremiumCommFactorLimit())){
                        ri.setDbRICommRate(ri.getTreatyShares().getDbRICommONRRate());
                    }else{
                        ri.setDbRICommRate(ri.getTreatyShares().getDbRICommRate());
                    }
                }

                //cek limit outgo askred, jika outgo > maks total komisi, maka pakai limit ini
                 if(ri.getTreatyShares()!=null){
                     if(ri.getTreatyShares().isMaxCommissionFlags()){
                         if(ri.getTreatyShares().getDbRICommMoreThanMaxCommRate()!=null){
                            if(BDUtil.biggerThan(komisiPct, ri.getTreatyShares().getDbMaxCommissionPct()))
                                ri.setDbRICommRate(ri.getTreatyShares().getDbRICommMoreThanMaxCommRate());
                            else
                                ri.setDbRICommRate(ri.getTreatyShares().getDbRICommRate());
                        }
                     }
                 }

                if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));

                //FORMULA HITUNG SISA PREMI BUANGAN
                if(!BDUtil.isZeroOrNull(ri.getDbSharePct()))
                    premiBelumTersesi = BDUtil.sub(premiBelumTersesi, ri.getDbPremiAmount());

                //logger.logWarning("#################### premi sisa = "+ premiBelumTersesi);
                //END FORMULA HITUNG SISA PREMI BUANGAN

                //PENOMORAN SURAT HUTANG
                ri.setStStatementOfAccountNo(generateSTOANo(policy,ri,trd.getTreatyDetail()));
                //END SURAT HUTANG

                //SET TANGGAL BPPDAN
                if(trd.getTreatyDetail().isBPDAN())
                    ri.setDtValidReinsuranceDate(getTanggalReas(policy, objx, j));

                //PENOMORAN RI SLIP OTOMATIS
                if(ri.getTreatyShares()!=null)
                    if(ri.getTreatyShares().getStRISlipNoFormat()!=null){
                        String riSlipNo = generateRISlipNo(policy,ri);

                        if(riSlipNo!=null)
                            ri.setStRISlipNo(riSlipNo);
                    }


                //perhitungan cover ri terbaru
                if(trd.getTreatyDetail().getStInsuranceCoverID()!=null){
                    trd.setDbTSIAmount(BDUtil.zero);
                    trd.setDbPremiAmount(BDUtil.zero);
                    trd.setDbComission(BDUtil.zero);
                    ri.setDbTSIAmount(BDUtil.zero);
                    ri.setDbPremiAmount(BDUtil.zero);
                    ri.setDbRICommAmount(BDUtil.zero);
                }

                try {
                    final DTOList coverPolis = ipo.getCoverage();
                    for (int l = 0; l < coverPolis.size(); l++) {
                        InsurancePolicyCoverView coverPol = (InsurancePolicyCoverView) coverPolis.get(l);

                        final String coverID = trd.getTreatyDetail().getStInsuranceCoverID()!=null?trd.getTreatyDetail().getStInsuranceCoverID():"";

                        if(coverID.equalsIgnoreCase(coverPol.getStInsuranceCoverID())){
                            trd.setDbTSIAmount(tsiTemp);
                            ri.setDbTSIAmount(BDUtil.mul(coverPol.getDbInsuredAmount(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbTSIAmount(BDUtil.mul(ri.getDbTSIAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(coverPol.getDbPremi(), BDUtil.getRateFromPct(trd.getDbPremiRatePct()), scale));
                            ri.setDbPremiAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
                            if(!trd.getDbTSIAmount().equals(ri.getDbTSIAmount())) trd.setDbTSIAmount(ri.getDbTSIAmount());

                            if(BDUtil.isZeroOrNull(ri.getDbRICommRate())) ri.setDbRICommAmount(ri.getDbRICommAmount());
                            else ri.setDbRICommAmount(BDUtil.mul(ri.getDbPremiAmount(), BDUtil.getRateFromPct(ri.getDbRICommRate()), scale));
                        }

                        //HITUNG PREMI PER PERILS PER TREATY DETIL
                        BigDecimal premiPerilsPerTreaty = BDUtil.mul(BDUtil.div(trd.getDbPremiAmount(), ipo.getDbObjectPremiTotalAmount(),5), coverPol.getDbPremiNew());

                        if (l == 0) {
                            ri.setStInsuranceCoverID1(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover1(ri.getDbTSIAmount());
                            ri.setDbRateCover1(coverPol.getDbRate());
                            ri.setDbPremiumCover1(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 1) {
                            ri.setStInsuranceCoverID2(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover2(ri.getDbTSIAmount());
                            ri.setDbRateCover2(coverPol.getDbRate());
                            ri.setDbPremiumCover2(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 2) {
                            ri.setStInsuranceCoverID3(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover3(ri.getDbTSIAmount());
                            ri.setDbRateCover3(coverPol.getDbRate());
                            ri.setDbPremiumCover3(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 3) {
                            ri.setStInsuranceCoverID4(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover4(ri.getDbTSIAmount());
                            ri.setDbRateCover4(coverPol.getDbRate());
                            ri.setDbPremiumCover4(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 4) {
                            ri.setStInsuranceCoverID5(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover5(ri.getDbTSIAmount());
                            ri.setDbRateCover5(coverPol.getDbRate());
                            ri.setDbPremiumCover5(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 5) {
                            ri.setStInsuranceCoverID6(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover6(ri.getDbTSIAmount());
                            ri.setDbRateCover6(coverPol.getDbRate());
                            ri.setDbPremiumCover6(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 6) {
                            ri.setStInsuranceCoverID7(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover7(ri.getDbTSIAmount());
                            ri.setDbRateCover7(coverPol.getDbRate());
                            ri.setDbPremiumCover7(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 7) {
                            ri.setStInsuranceCoverID8(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover8(ri.getDbTSIAmount());
                            ri.setDbRateCover8(coverPol.getDbRate());
                            ri.setDbPremiumCover8(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 8) {
                            ri.setStInsuranceCoverID9(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover9(ri.getDbTSIAmount());
                            ri.setDbRateCover9(coverPol.getDbRate());
                            ri.setDbPremiumCover9(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 9) {
                            ri.setStInsuranceCoverID10(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover10(ri.getDbTSIAmount());
                            ri.setDbRateCover10(coverPol.getDbRate());
                            ri.setDbPremiumCover10(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 10) {
                            ri.setStInsuranceCoverID11(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover11(ri.getDbTSIAmount());
                            ri.setDbRateCover11(coverPol.getDbRate());
                            ri.setDbPremiumCover11(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 11) {
                            ri.setStInsuranceCoverID12(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover12(ri.getDbTSIAmount());
                            ri.setDbRateCover12(coverPol.getDbRate());
                            ri.setDbPremiumCover12(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 12) {
                            ri.setStInsuranceCoverID13(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover13(ri.getDbTSIAmount());
                            ri.setDbRateCover13(coverPol.getDbRate());
                            ri.setDbPremiumCover13(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 13) {
                            ri.setStInsuranceCoverID14(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover14(ri.getDbTSIAmount());
                            ri.setDbRateCover14(coverPol.getDbRate());
                            ri.setDbPremiumCover14(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        if (l == 14) {
                            ri.setStInsuranceCoverID15(coverPol.getStInsuranceCoverID());
                            ri.setDbTSICover15(ri.getDbTSIAmount());
                            ri.setDbRateCover15(coverPol.getDbRate());
                            ri.setDbPremiumCover15(BDUtil.mul(BDUtil.div(ri.getDbPremiAmount(),trd.getDbPremiAmount(),5), premiPerilsPerTreaty));
                        }

                        //if (l == 15)
                            //throw new RuntimeException("Jumlah Cover Lebih Dari 15 !");

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                //hitung cicilan premi reas
                ri.reCalculateInstallment(policy.getDtPolicyDate());

                premitot = BDUtil.add(premitot, ri.getDbPremiAmount());
                commtot = BDUtil.add(commtot, ri.getDbRICommAmount());
            }

            //SET NILAI SISA PREMI BUANGAN

            //if(!isEndorse){
                if(policy.getStPolicyTypeID().equalsIgnoreCase("59") || policy.getStPolicyTypeID().equalsIgnoreCase("80")){

                    for (int j = 0; j < shares.size(); j++) {
                        InsurancePolicyReinsView riJ = (InsurancePolicyReinsView) shares.get(j);

                        if(riJ.getTreatyShares()!=null){
                            if(riJ.getTreatyShares().isPremiExcessFlag()){
                                riJ.setStAutoRateFlag("Y");
                                riJ.setDbPremiAmount(premiBelumTersesi);
                                riJ.setDbRICommAmount(premiBelumTersesi);

                                premitot = BDUtil.add(premitot, riJ.getDbPremiAmount());
                                commtot = BDUtil.add(commtot, riJ.getDbRICommAmount());
                            }
                        }
                        //END FORMULA HITUNG SISA PREMI BUANGAN
                    }
                }
            //}

            if (premitot != null)
                trd.setDbPremiAmount(premitot);

            if (commtot != null)
                trd.setDbComission(commtot);
        }

    }


}


