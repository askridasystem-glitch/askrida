/***********************************************************************
 * Module:  com.webfin.insurance.custom.CashManagementHandler
 * Author:  Denny Mahendra
 * Created: Sep 29, 2006 12:19:13 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.crux.util.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;

public class CashManagementHandler extends DefaultCustomHandler {
   private final static transient LogManager logger = LogManager.getInstance(SipandaHandler.class);

   public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
      try {
         if(!validate) return; 
         
         if(policy.isStatusPolicy()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusEndorseIntern())
       {   
         final InsurancePolicyObjDefaultView o = (InsurancePolicyObjDefaultView) obj;
         
         final boolean inward = policy.getCoverSource().isInward(); 
                 
         final DTOList cov = o.getCoverage();
         for (int i = 0; i < cov.size(); i++) {
             InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(i);
             
             if(cover.getStInsuranceCoverID().equalsIgnoreCase("139")){
                  if(policy.getDtPeriodStart()!=null)
                        obj.getTreaties().deleteAll();
                        o.setStInsuranceTreatyID(getInsuranceTreatyID(policy.getDtPeriodStart(), "139"));
             }
             
             if(cover.getStInsuranceCoverID().equalsIgnoreCase("143")){
                  if(policy.getDtPeriodStart()!=null)
                      obj.getTreaties().deleteAll();
                      o.setStInsuranceTreatyID(getInsuranceTreatyID(policy.getDtPeriodStart(), "143"));
             }
             
         }
      }

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public boolean isLockTSI() {
      return false;
   }

   
   public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {
                if(policy.isStatusEndorseRI()) return;
            
            int scale = 0;
            
            if(policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            else scale = 0;
            
            //final BigDecimal rate = getRate(policy,objx,true);
                    
            final DTOList rein = objx.getTreaties();
            
            InsurancePolicyTreatyView reas = (InsurancePolicyTreatyView) rein.get(0);
            
            final DTOList det = reas.getDetails();
            
            for (int i = 0; i < det.size(); i++)
            {
                InsurancePolicyTreatyDetailView detail = (InsurancePolicyTreatyDetailView) det.get(i);
                
                final InsuranceTreatyDetailView trdd = detail.getTreatyDetail();
                
                if(trdd.isFacultative() || trdd.isExcessOfLoss()) continue;
                
                final DTOList ri = detail.getShares();
                
                for (int j = 0; j < ri.size(); j++)
                {
                    InsurancePolicyReinsView member = (InsurancePolicyReinsView) ri.get(j);

                    
                    
                    //member.setDbPremiRate(BDUtil.mul(member.getDbPremiRate(), policy.getDbPremiumFactorValue(),scale));

                    logger.logDebug("++++++++++++++ Masuk custom +++++++++++++");
                    if(member.getDbPremiRate()!=null){
                        member.setDbPremiAmount(BDUtil.mul(member.getDbTSIAmount(), BDUtil.getRateFromPct(member.getDbPremiRate()), scale));
                        //member.setDbPremiAmount(BDUtil.mul(member.getDbPremiAmount(), policy.getDbPeriodRateFactor(),scale));
                        //member.setDbPremiAmount(BDUtil.mul(member.getDbPremiAmount(), policy.getDbPremiumFactorValue(),scale));
                        member.setDbPremiAmount(BDUtil.divRoundUp(member.getDbPremiAmount(),new BigDecimal(365), scale));
                        
                        if (member.getDbRICommRate() == null) member.setDbRICommRate(detail.getDbComissionRate());
                
                        if(BDUtil.isZeroOrNull(member.getDbRICommRate())) member.setDbRICommAmount(member.getDbRICommAmount());
                        else member.setDbRICommAmount(BDUtil.mul(member.getDbPremiAmount(), BDUtil.getRateFromPct(member.getDbRICommRate()), scale));
                    }
                        
                }
 
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
   public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
    private String getInsuranceTreatyID(Date per_start, String ins_cover_id) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      ins_treaty_id,treaty_name " +
                    "   from " +
                    "         ins_treaty" +
                    "   where" +
                    "      treaty_period_start <= ? " +
                    "   and treaty_period_end >= ? " +
                    "   and ref1 = ? ");
            
            S.setParam(1, per_start);
            S.setParam(2, per_start);
            S.setParam(3, ins_cover_id);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }

    public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
}
