/***********************************************************************
 * Module:  com.webfin.insurance.custom.EarthquakeHandler
 * Author:  Achmad Rhodoni
 * Created: Dec 04, 2008 14:00:00 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.webfin.FinCodec;
import com.crux.util.*;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class MarineHullHandler extends DefaultCustomHandler {
    
    private final static transient LogManager logger = LogManager.getInstance(EarthquakeHandler.class);
    
    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int getRangkaCode(String stReference3) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      ref1 " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_EQ_CONSTR_CL' and vs_code = ?");
            
            S.setParam(1,stReference3);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return Integer.parseInt(RS.getString(1));
            
            return -1;
        } finally {
            
            S.release();
        }
    }
    
    public int getStoreyCode(String stReference10) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      ref1 " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_EQ_STOREY' and vs_code = ?");
            
            S.setParam(1,stReference10);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return Integer.parseInt(RS.getString(1));
            
            return -1;
        } finally {
            
            S.release();
        }
    }
    
    public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {
            
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            
            //if(!obj.getStReference8().equalsIgnoreCase("3")) return;
            if(obj.getStReference8()==null) throw new RuntimeException("Kelas Kapal Belum Diisi");
            
            final SQLUtil S = new SQLUtil();
            BigDecimal rate = null;
            
            final DTOList cov = objx.getCoverage();
            
            for (int i = 0; i < cov.size(); i++) {
                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) cov.get(i);
                
                try {
                    String query = "select rate1 from ins_rates_big where rate_class= 'MARINE_CLASS' and ref1=?";
                    
                    final PreparedStatement PS = S.setQuery(query);
                    
                    int n=1;
                    
                    PS.setString(n++,cover.getStInsuranceCoverID());
                    
                    final ResultSet RS = PS.executeQuery();
                    
                    if (RS.next()){
                        rate = RS.getBigDecimal(1);
                    }
                    
                } finally {
                    S.release();
                }
            }
            
            if(!obj.getStReference8().equalsIgnoreCase("3")) rate = BDUtil.hundred;

            int scale = 0;
            
            if (!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            
            final DTOList treaties = objx.getTreaties();
            final InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(0);
            
        
            tre.adjustRatio(objx.getDbTreatyLimitRatio(), policy.getDbCurrencyRateTreaty(), policy.getStCurrencyCode(),rate);
            tre.raiseToTSI(policy.getStPolicyTypeID(), objx.getDbObjectInsuredAmountShare(), objx.getDbTreatyLimitRatio(), policy.getDbCurrencyRateTreaty(), policy.getStCurrencyCode(),rate);
            /*
            if (policy.hasCoIns()) {
                objx.setDbObjectPremiTotalBeforeCoinsuranceAmount(objx.getDbObjectPremiTotalAmount());
                objx.setDbObjectPremiTotalAmount(BDUtil.mul(objx.getDbObjectPremiTotalAmount(), BDUtil.getRateFromPct(policy.getHoldingCoin().getDbSharePct()), scale));
            }*/

            
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
