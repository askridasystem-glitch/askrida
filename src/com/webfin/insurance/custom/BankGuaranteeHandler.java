/***********************************************************************
 * Module:  com.webfin.insurance.custom.EarthquakeHandler
 * Author:  Achmad Rhodoni
 * Created: Dec 04, 2008 14:00:00 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.FinCodec;
import com.webfin.insurance.model.*;
import com.crux.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

public class BankGuaranteeHandler extends DefaultCustomHandler {
    
    private final static transient LogManager logger = LogManager.getInstance(EarthquakeHandler.class);
    
    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {
            
            if(!validate) return;
            
            if(policy.isStatusEndorse() || policy.isStatusEndorseRI()) return;
            
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            
            int scale = 0;
            
            if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            
            int jmlHari = Integer.parseInt(policy.getStPeriodLength());
            
            DateTime startDate = new DateTime(policy.getDtPeriodStart());
            DateTime endDate = new DateTime(policy.getDtPeriodEnd());
            Days d = Days.daysBetween(startDate, endDate);
            int days = d.getDays();
          
            jmlHari = days;
            
            Months m = Months.monthsBetween(startDate, endDate);
            int month = m.getMonths();
            
            Years y = Years.yearsBetween(startDate, endDate);
            int year = y.getYears();
            
            DTOList cover = objx.getCoverage();
            BigDecimal totalPremi = null;
            BigDecimal minServiceCharge = null;
            
            final SQLUtil S = new SQLUtil();
            BigDecimal biayaAdminAskrida = null;
            BigDecimal biayaMateraiAskrida = null;
            BigDecimal pctJasaJaminanAskrida = null;
            BigDecimal pctBank = null;
            BigDecimal minServiceChargeBank = null;
            BigDecimal rateBank = null;
            
            try {
                final PreparedStatement PS = S.setQuery(
                        "SELECT RATE1 AS PCOST,(SELECT RATE1 AS SFEE "+
                        " FROM INS_RATES_BIG "+
                        " WHERE RATE_CLASS = 'SFEEBG_ASKRIDA' AND "+
                        " REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS SFEE,(SELECT RATE1 AS SFEE "+
                        " FROM INS_RATES_BIG "+
                        " WHERE RATE_CLASS = 'JASAJAMINAN_ASKRIDA' AND "+
                        " REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS SFEE, "+
                        "  (SELECT RATE2 AS SFEE "+
                        " FROM INS_RATES_BIG "+ 
                        " WHERE RATE_CLASS = 'JASAJAMINAN_ASKRIDA' AND "+ 
                        " REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS bank_pct, "+
                        " (SELECT RATE3 AS SFEE "+
                        " FROM INS_RATES_BIG  "+
                        " WHERE RATE_CLASS = 'JASAJAMINAN_ASKRIDA' AND "+ 
                        " REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS bank_min, "+
                        " (SELECT RATE0 AS SFEE  "+ 
                        " FROM INS_RATES_BIG   "+ 
                        " WHERE RATE_CLASS = 'JASAJAMINAN_ASKRIDA' AND  "+  
                        " REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1) AS bank_rate "+ 
                        " FROM INS_RATES_BIG "+
                        " WHERE RATE_CLASS = 'PCOSTBG_ASKRIDA' AND REF2 = ? AND period_start<=? and period_end>=? and refid1 = 1;");
                
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
                    pctBank = RS.getBigDecimal(4);
                    minServiceChargeBank = RS.getBigDecimal(5);
                    rateBank = RS.getBigDecimal(6);
                }
                
            } finally {
                S.release();
            }
            
            for(int j=0; j < cover.size(); j++){
                InsurancePolicyCoverView covers = (InsurancePolicyCoverView) cover.get(j);
                
                String ref = null;
                
                if(jmlHari>0 && jmlHari<=90) ref = "rate1";
                else if(jmlHari>90 && jmlHari<=180) ref = "rate2";
                else if(jmlHari>180 && jmlHari<=270) ref = "rate3";
                else if(jmlHari>270 && jmlHari<=360) ref = "rate4";
                else if(jmlHari>360) ref = "rate4";
                
                if(policy.getStUnits()!=null){
                    if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.DAY)){
                        if(jmlHari>0 && jmlHari<=90) ref = "rate1";
                        else if(jmlHari>90 && jmlHari<=180) ref = "rate2";
                        else if(jmlHari>180 && jmlHari<=270) ref = "rate3";
                        else if(jmlHari>270 && jmlHari<=360) ref = "rate4";
                        else if(jmlHari>360) ref = "rate4";
                    }else if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.MONTH)){
                        if(month>0 && month<=3) ref = "rate1";
                        else if(month>3 && month<=6) ref = "rate2";
                        else if(month>6 && month<=9) ref = "rate3";
                        else if(month>9 && month<=12) ref = "rate4";
                        else if(month>12) ref = "rate4";
                    }else if(policy.getStUnits().equalsIgnoreCase(FinCodec.PeriodUnit.YEAR)){
                        if(year==1) ref = "rate4";
                        else ref="null";
                    }
                }

                //perhitungan proporsional                
                /*
                if(policy.getStCostCenterCode().equalsIgnoreCase("42")){
                    if(jmlHari>360){
                        ref = "rate4";
                    }
                }*/
                
                final SQLUtil S3 = new SQLUtil();
                
                BigDecimal rateKBG = null;
                
                try {
                    final PreparedStatement PS3 = S3.setQuery(
                            "select "+ ref + ", rate0 "+
                            " from INS_RATES_BIG"+
                            " where rate_class = 'KBG_RATE'"+
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
                
                /*
                if(policy.getStCostCenterCode().equalsIgnoreCase("42")){
                    if(jmlHari>360){
                        rateKBG = BDUtil.mul(BDUtil.div(new BigDecimal(jmlHari), new BigDecimal(360),2), rateKBG, 2);
                    }
                }*/
                /*
                logger.logInfo("++++++++++++++++++++++++++++++++++++++++");
                logger.logInfo("jmlHari = "+ jmlHari);
                logger.logInfo("ref = "+ ref);
                logger.logInfo("bagian hari = "+ BDUtil.div(new BigDecimal(jmlHari), new BigDecimal(360),2));
                logger.logInfo("rateKBG = "+rateKBG);
                logger.logInfo("++++++++++++++++++++++++++++++++++++++++");*/
                
                if(covers.isAutoRate()){
                    if(rateKBG!=null) covers.setDbRate(rateKBG);
                    covers.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(rateKBG), covers.getDbInsuredAmount(), scale));
                }
                
                if(covers.isAutoRate()){
                    if(!BDUtil.isZeroOrNull(minServiceCharge)){
                        if(BDUtil.lesserThan(covers.getDbPremiNew(),minServiceCharge)){
                            covers.setDbPremiNew(minServiceCharge);
                            covers.setDbRate(null);
                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(minServiceCharge);
                            covers.setStCalculationDesc(szCalc.toString());
                        }
                    }
                }
                
                if(covers.isEntryPremi()){
                    covers.setDbPremiNew(covers.getDbPremiNew());
                    covers.setDbRate(null);
                    final StringBuffer szCalc = new StringBuffer();
                    szCalc.append(covers.getDbPremiNew());
                    covers.setStCalculationDesc(szCalc.toString());
                }
                
                covers.setDbPremi(covers.getDbPremiNew());
                totalPremi = BDUtil.add(totalPremi,covers.getDbPremi());
            }
            
            final BigDecimal jasaJaminan = BDUtil.mulRound(totalPremi,BDUtil.getRateFromPct(pctJasaJaminanAskrida), scale);
            
            if(cover.size()==1){
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) cover.get(0);
                
                //if(!BDUtil.isZeroOrNull(totalPremi)) 
                if(cv.isAutoRate()){
                    if(BDUtil.isZeroOrNull(rateBank)) obj.setDbReference5(totalPremi);
                    else if(!BDUtil.isZeroOrNull(rateBank)) obj.setDbReference5(BDUtil.mul(BDUtil.getRateFromPct(rateBank), cv.getDbInsuredAmount(), scale));
                }else {
                    obj.setDbReference5(obj.getDbReference5());
                }
                //else if(BDUtil.isZeroOrNull(totalPremi)) obj.setDbReference5(obj.getDbReference5());
                                
                if(cv.isEntryPremi()){
                    cv.setDbPremi(cv.getDbPremi());
                    cv.setDbPremiNew(cv.getDbPremi());
                    final StringBuffer szCalc = new StringBuffer();
                    szCalc.append(cv.getDbPremi());
                    cv.setStCalculationDesc(szCalc.toString());
                }if(cv.isAutoRate()){
                    cv.setDbPremi(jasaJaminan);
                    cv.setDbPremiNew(jasaJaminan);
                    final StringBuffer szCalc = new StringBuffer();
                    szCalc.append(jasaJaminan);
                    cv.setStCalculationDesc(szCalc.toString());
                }
                
                if(!BDUtil.isZeroOrNull(pctBank) && !BDUtil.isZeroOrNull(minServiceChargeBank)){
                    //logger.logDebug("++++++++++++++++++++++++++++++++++++++++");
                        //logger.logDebug("obj.getDbReference5() = "+obj.getDbReference5());
                        //logger.logDebug("minServiceChargeBank = "+minServiceChargeBank);
                        //logger.logDebug("++++++++++++++++++++++++++++++++++++++++");
                    if(BDUtil.lesserThan(BDUtil.sub(obj.getDbReference5(),cv.getDbPremiNew()),minServiceChargeBank)){
                        obj.setDbReference8(minServiceChargeBank);
                        //logger.logDebug("=================== Masuk Custom Nih ====================");
                        cv.setDbPremiNew(BDUtil.sub(obj.getDbReference5(), minServiceChargeBank));
                        cv.setDbPremi(cv.getDbPremiNew());
                        final StringBuffer szCalc = new StringBuffer();
                        szCalc.append(cv.getDbPremiNew());
                        cv.setStCalculationDesc(szCalc.toString());
                    }
                }
            }
            
            final SQLUtil S2 = new SQLUtil();
            BigDecimal biayaAdminTertanggung = null;
            BigDecimal biayaMateraiTertanggung = null;
            BigDecimal pctJasaJaminanTertanggung = null;
            try {
                final PreparedStatement PS2 = S2.setQuery(
                        "SELECT RATE1 AS PCOST,(SELECT RATE1 AS SFEE "+
                        " FROM INS_RATES_BIG "+
                        " WHERE RATE_CLASS = 'SFEEBG' AND "+
                        " REF2 = ? AND period_start<=? and period_end>=? ) AS SFEE "+
                        " FROM INS_RATES_BIG "+
                        " WHERE RATE_CLASS = 'PCOSTBG' AND REF2 = ? AND period_start<=? and period_end>=?;");
                
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
                
            } finally {
                S2.release();
            }
            
            //new
            if(cover.size()==1){
                InsurancePolicyCoverView cv1 = (InsurancePolicyCoverView) cover.get(0);
                
                if(!BDUtil.isZeroOrNull(biayaAdminTertanggung)){
                    if(cv1.isAutoRate()) obj.setDbReference6(biayaAdminTertanggung);
                    else if(!cv1.isAutoRate()) obj.setDbReference6(obj.getDbReference6());
                }else if(BDUtil.isZeroOrNull(biayaAdminTertanggung)){
                    obj.setDbReference6(obj.getDbReference6());
                }
                
                if(!BDUtil.isZeroOrNull(biayaMateraiTertanggung)){
                     if(cv1.isAutoRate()) obj.setDbReference7(biayaMateraiTertanggung);
                     else if(!cv1.isAutoRate()) obj.setDbReference7(obj.getDbReference7());
                }else if(BDUtil.isZeroOrNull(biayaMateraiTertanggung)){
                    obj.setDbReference7(obj.getDbReference7());
                }
                
            }
            
            
            final DTOList details = policy.getDetails();
            for (int i = 0; i < details.size(); i++){
                InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(i);
                
                if(items.isPolicyCost()){

                    if(cover.size()==1){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) cover.get(0);

                        //if(!BDUtil.isZeroOrNull(totalPremi))
                        if(cv.isAutoRate()){
                            if(!BDUtil.isZeroOrNull(biayaAdminAskrida)) items.setDbAmount(biayaAdminAskrida);
                        }else{
                            items.setDbAmount(items.getDbAmount());
                        }
                    }
                    
                    
                }
                
                if(items.isStampFee()){
                    if(cover.size()==1){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) cover.get(0);

                        //if(!BDUtil.isZeroOrNull(totalPremi))
                        if(cv.isAutoRate()){
                             if(!BDUtil.isZeroOrNull(biayaMateraiAskrida)) items.setDbAmount(biayaMateraiAskrida);
                        }else{
                            items.setDbAmount(items.getDbAmount());
                        }
                    }
                }
            }
            //end
            objx.setDbObjectPremiTotalAmount(totalPremi);

            if(BDUtil.isEqual(totalPremi, minServiceCharge, 2)){
                policy.setDbPremiTotal(objx.getDbObjectPremiTotalAmount());
                policy.setDbPremiNetto(BDUtil.add(policy.getDbPremiTotal(),policy.getDbPremiNetto()));
            }
            
            BigDecimal totalDiscount = null;
            final DTOList details2 = policy.getDetails();
            for (int i = 0; i < details2.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details2.get(i);
                
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
