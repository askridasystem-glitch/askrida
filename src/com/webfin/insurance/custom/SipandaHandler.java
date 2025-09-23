/***********************************************************************
 * Module:  com.webfin.insurance.custom.SipandaHandler
 * Author:  Denny Mahendra
 * Created: Sep 29, 2006 12:19:13 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.crux.util.*;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.ff.model.FlexFieldDetailView;

import java.util.Iterator;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SipandaHandler extends DefaultCustomHandler {
    private final static transient LogManager logger = LogManager.getInstance(SipandaHandler.class);
    
    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
        try {
            
            if(policy.isStatusClaim()) return;
            
            final InsurancePolicyObjDefaultView o = (InsurancePolicyObjDefaultView) obj;
            
            final FlexFieldHeaderView objectFF = policy.getPolicyType().getObjectMap();
            
            final DTOList details = objectFF.getDetails();
            
            BigDecimal sum=null;
            
            BigDecimal strata[]= new BigDecimal[21];
            
            final SQLUtil S = new SQLUtil();
            
            try {
                final PreparedStatement PS = S.setQuery(
                        "select * from ins_rates_big where rate_class='SIPANDA' and ref1 = ? and ref2 = ?");
                
                int n=1;
                
                PS.setString(n++,policy.getStCostCenterCodeSource()); //ref2
                PS.setString(n++,o.getStReference3());
                
                int j=0;
                
                final ResultSet RS = PS.executeQuery();
                
                if (RS.next()){
                    strata[0] = RS.getBigDecimal(9);
                    strata[1] = RS.getBigDecimal(10);
                    strata[2] = RS.getBigDecimal(11);
                    strata[3] = RS.getBigDecimal(12);
                    strata[4] = RS.getBigDecimal(13);
                    strata[5] = RS.getBigDecimal(14);
                    strata[6] = RS.getBigDecimal(15);
                    strata[7] = RS.getBigDecimal(16);
                    strata[8] = RS.getBigDecimal(17);
                    strata[9] = RS.getBigDecimal(18);
                    strata[10] = RS.getBigDecimal(19);
                    strata[11] = RS.getBigDecimal(20);
                    strata[12] = RS.getBigDecimal(21);
                    strata[13] = RS.getBigDecimal(22);
                    strata[14] = RS.getBigDecimal(23);
                    strata[15] = RS.getBigDecimal(24);
                    strata[16] = RS.getBigDecimal(25);
                    strata[17] = RS.getBigDecimal(26);
                    strata[18] = RS.getBigDecimal(27);
                    strata[19] = RS.getBigDecimal(28);
                    strata[20] = RS.getBigDecimal(29);
                }
                
            } finally {
                S.release();
            }
            
            for (int i = 0; i < details.size(); i++) {
                FlexFieldDetailView ffd = (FlexFieldDetailView) details.get(i);
                
                final boolean isSSField = Tools.isEqual(ffd.getStFieldID(),"SIPANDA_SS");
                
                if (!isSSField) continue;
                
                int k = Integer.parseInt(ffd.getStFieldRef().substring(ffd.getStFieldRef().length()-1));
                
                //logger.logDebug("k= "+ String.valueOf(k));
                //final BigDecimal ratio = ConvertUtil.getNum(ffd.getStReference1());
                
                final BigDecimal ratio = strata[k];
                
                final BigDecimal saldo = (BigDecimal)o.getProperty(ffd.getStFieldRef());
                
                final BigDecimal tot = BDUtil.mul(saldo, ratio);
                
                sum = BDUtil.add(tot,sum);
            }
            
            final DTOList suminsureds = o.getSuminsureds();
            
            if (suminsureds.size()>0) {
                final InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(0);
                
                if("Y".equalsIgnoreCase(tsi.getStAutoFlag()))
                    tsi.setDbInsuredAmount(sum);
                
                policy.recalculateBasic();
            }
            
            final DTOList coins = policy.getCoins2();
            
            if(coins.size()>0){
                for (int i = 0; i<coins.size(); i++) {
                    InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);
                    
                    if(co.isHoldingCompany()){
                        if(co.isAutoPremi()){
                            co.setDbAmount(policy.getDbInsuredAmount());
                            co.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(co.getDbSharePct()),policy.getDbPremiTotal()));
                        }else{
                            co.setDbPremiAmount(co.getDbPremiAmount());
                        }
                   }else{
                        if(co.isEntryByPctRate()){
                            co.setDbAmount(BDUtil.zero);
                            co.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(co.getDbSharePct()),policy.getDbPremiTotal()));
                        }else{
                            co.setDbPremiAmount(co.getDbPremiAmount());
                        }
                    }
                    
                }
            }
            
            final DTOList covers = obj.getCoverage();
            for (int i = 0; i < covers.size(); i++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(i);
                
                if(cov.isAutoRate()){
                    cov.setDbRate(strata[0]);
                }
                
            }
            
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean isLockTSI() {
        return false;
    }
    
    
   /*public void onNewObject(InsurancePolicyView policy, InsurancePolicyObjectView o) {
    
      try {
         final LookUpUtil lsi = ListUtil.getLookUpFromQuery(
                 "   select" +
                 "      b.ins_tsi_cat_id, b.description" +
                 "   from  " +
                 "      ins_tsicat_poltype a " +
                 "      inner join ins_tsi_cat b on b.ins_tsi_cat_id = a.ins_tsi_cat_id" +
                 "   where" +
                 "      a.pol_type_id=?" +
                 "   order by ins_tcpt_id" +
                 "   limit 1" ,
                 new Object [] {policy.getStPolicyTypeID()}
         );
    
    
         final Iterator it = lsi.getIterator();
    
         while (it.hasNext()) {
            String siid = (String) it.next();
    
            final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();
    
            ptsi.setStInsuranceTSIID(siid);
    
            ptsi.markNew();
    
            o.getSuminsureds().add(ptsi);
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }*/
    
    public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {
            
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

    public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
