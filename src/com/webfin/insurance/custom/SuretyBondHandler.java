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

public class SuretyBondHandler extends DefaultCustomHandler {

   private final static transient LogManager logger = LogManager.getInstance(EarthquakeHandler.class);

   public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
      try {
         
         final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
         
         int scale = 0;
		 
		 if(policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
		 else scale = 0;
		 
		    int jmlHari = Integer.parseInt(policy.getStPeriodLength());
            
            DTOList cover = objx.getCoverage();
            BigDecimal totalPremi = null;
            BigDecimal minServiceCharge = null;
            
            for(int j=0; j < cover.size(); j++){
            	InsurancePolicyCoverView covers = (InsurancePolicyCoverView) cover.get(j);
            	
            	    String ref = null;
            
		            if(jmlHari>0 && jmlHari<=90) ref = "rate1";
		           	else if(jmlHari>90 && jmlHari<=180) ref = "rate2";
		           	else if(jmlHari>180 && jmlHari<=270) ref = "rate3";
		           	else if(jmlHari>270 && jmlHari<=360) ref = "rate4";
		           	
		           	final SQLUtil S3 = new SQLUtil();
		           	
		           	BigDecimal rateKBG = null;
		           	
		           	try {
		            	final PreparedStatement PS3 = S3.setQuery(
		            		"select "+ ref + ", rate0 "+
							" from ins_rates_big"+
							" where rate_class = 'SB_RATE'"+
							" and ref1 = ? "+
							" and ref2 = ? AND period_start<=? and period_end>=?");
							
						   int n3=1;
						
						   PS3.setString(n3++,policy.getStPolicyTypeID());
			               PS3.setString(n3++,policy.getStCostCenterCode()); //kode cabang
			               S3.setParam(n3++,policy.getDtPolicyDate()); //period start
			               S3.setParam(n3++,policy.getDtPolicyDate()); //period start
			               
			               final ResultSet RS3 = PS3.executeQuery();
		
		               if (RS3.next()){
		               		rateKBG = RS3.getBigDecimal(1);
		               		minServiceCharge = RS3.getBigDecimal(2);
		               }
		            	
		            } finally {
		               S3.release();
		            }
            	
 				if(covers.isAutoRate()){       	
		            if(rateKBG!=null) covers.setDbRate(rateKBG);
		            covers.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(rateKBG), covers.getDbInsuredAmount(), scale));
            	}
            
            	if(BDUtil.lesserThan(covers.getDbPremiNew(),minServiceCharge)){
            		covers.setDbPremiNew(minServiceCharge);
            		covers.setDbRate(null);
            		final StringBuffer szCalc = new StringBuffer();
            		szCalc.append(covers.getDbPremiNew());
            		covers.setStCalculationDesc(szCalc.toString());
            	}
		            	 	
		        covers.setDbPremi(covers.getDbPremiNew());
		        totalPremi = BDUtil.add(totalPremi,covers.getDbPremi());	        
            }
            
            objx.setDbObjectPremiTotalAmount(totalPremi);
		 
		        final SQLUtil S = new SQLUtil();
		        BigDecimal biayaAdminAskrida = null;
		        BigDecimal biayaMateraiAskrida = null;
		        BigDecimal pctJasaJaminanAskrida = null;
            try {
            	final PreparedStatement PS = S.setQuery(
	            	"SELECT RATE1 AS PCOST,(SELECT RATE1 AS SFEE "+
					" FROM INS_RATES_BIG "+
					" WHERE RATE_CLASS = 'SFEESB_ASKRIDA' AND "+
					" REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS SFEE,(SELECT RATE1 AS SFEE "+
					" FROM INS_RATES_BIG "+
					" WHERE RATE_CLASS = 'JASAJAMINAN_SB_ASKRIDA' AND "+
					" REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS SFEE "+
					" FROM INS_RATES_BIG "+
					" WHERE RATE_CLASS = 'PCOSTSB_ASKRIDA' AND REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1;");
				
				   int n=1;
				
	               PS.setString(n++,policy.getStCostCenterCode()); //kode cabang
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               
	               PS.setString(n++,policy.getStCostCenterCode()); //kode cabang
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               
	               PS.setString(n++,policy.getStCostCenterCode()); //kode cabang
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               S.setParam(n++,policy.getDtPolicyDate()); //period start
	               
               final ResultSet RS = PS.executeQuery();

               if (RS.next()){
               		biayaAdminAskrida = RS.getBigDecimal(1);
                  	biayaMateraiAskrida  = RS.getBigDecimal(2);
                  	pctJasaJaminanAskrida = RS.getBigDecimal(3);
               }
              
            } finally {
               S.release();
            }
         
         final BigDecimal jasaJaminan = BDUtil.mul(totalPremi,BDUtil.getRateFromPct(pctJasaJaminanAskrida), scale);
         
         obj.setDbReference5(jasaJaminan);
         obj.setDbReference6(biayaAdminAskrida);
         obj.setDbReference7(biayaMateraiAskrida);
         
         	final SQLUtil S2 = new SQLUtil();
		        BigDecimal biayaAdminTertanggung = null;
		        BigDecimal biayaMateraiTertanggung = null;
		        BigDecimal pctJasaJaminanTertanggung = null;
            try {
            	final PreparedStatement PS2 = S2.setQuery(
	            	"SELECT RATE1 AS PCOST,(SELECT RATE1 AS SFEE "+
					" FROM INS_RATES_BIG "+
					" WHERE RATE_CLASS = 'SFEESB' AND "+
					" REF2 = ? AND period_start<=? and period_end>=? ) AS SFEE "+
					" FROM INS_RATES_BIG "+
					" WHERE RATE_CLASS = 'PCOSTSB' AND REF2 = ? AND period_start<=? and period_end>=?;");
				
				   int n2=1;
				
	               PS2.setString(n2++,policy.getStCostCenterCode()); //kode cabang
	               S2.setParam(n2++,policy.getDtPolicyDate()); //period start
	               S2.setParam(n2++,policy.getDtPolicyDate()); //period start
	               
	               PS2.setString(n2++,policy.getStCostCenterCode()); //kode cabang
	               S2.setParam(n2++,policy.getDtPolicyDate()); //period start
	               S2.setParam(n2++,policy.getDtPolicyDate()); //period start
	               
               final ResultSet RS2 = PS2.executeQuery();

               if (RS2.next()){
               		biayaAdminTertanggung = RS2.getBigDecimal(1);
                  	biayaMateraiTertanggung  = RS2.getBigDecimal(2);
               }
               
               DTOList details = policy.getDetails();
               
               for (int i = 0; i < details.size(); i++){
               		InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(i);
               		
               		if(items.isPolicyCost()) items.setDbAmount(biayaAdminTertanggung);
               		
               		if(items.isStampFee()) items.setDbAmount(biayaMateraiTertanggung);
               }
              
            } finally {
               S2.release();
            }
        
        if(BDUtil.isEqual(totalPremi, minServiceCharge, 2)){
        	policy.setDbPremiTotal(objx.getDbObjectPremiTotalAmount());
        	policy.setDbPremiNetto(BDUtil.add(policy.getDbPremiTotal(),policy.getDbPremiNetto()));
        }
        
        BigDecimal totalDiscount = null;
        final DTOList details = policy.getDetails();
        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

            if (!item.isDiscount()) continue;

            item.calculateRateAmount(policy.getDbPremiTotal(), scale);

            totalDiscount = BDUtil.add(totalDiscount, item.getDbAmount());
        }

        final BigDecimal totalAfterDiscount = BDUtil.sub(policy.getDbPremiTotal(), totalDiscount);

        policy.setDbPremiTotalAfterDisc(totalAfterDiscount);
        		

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
   
   public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
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
