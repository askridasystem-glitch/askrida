/***********************************************************************
 * Module:  com.webfin.insurance.custom.EarthquakeHandler
 * Author:  Achmad Rhodoni
 * Created: Dec 04, 2008 14:00:00 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.crux.pool.DTOPool;
import com.webfin.insurance.model.*;
import com.crux.util.*;
import com.webfin.postalcode.model.PostalCodeMaiparkView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class EarthquakeHandler extends DefaultCustomHandler
{
    
    private final static transient LogManager logger = LogManager.getInstance(EarthquakeHandler.class);
    
    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate)
    {
        try
        {
            
            if(policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusClaimEndorse()) return;
            
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            
            //boolean autoRate = Tools.isYes(policy.getCoverageView().getStAutoRateFlag());
            int scale = 0;
            
            if(policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            else scale = 0;
            
            final BigDecimal rateOJK = getRate(policy,objx,validate);

            DTOList covers = obj.getCoverage();

            if (covers!=null && covers.size()>0)
            {
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);
                boolean autorate = Tools.isYes(cv.getStAutoRateFlag());
                boolean userate	= Tools.isYes(cv.getStEntryRateFlag());

                /*
                if(autorate)
                {
                    cv.setStEntryRateFlag("Y");
                    cv.setDbRate(premiRate2);
                }*/

                obj.reCalculate();

                if (cv.getDbPremi().longValue()!=0) {

                        final StringBuffer szc = new StringBuffer();

                        if(cv.getDbRate()!=null){
                            szc.append(p(cv.getDbInsuredAmount())+" x "+p(cv.getDbRate())+policy.getStRateMethodDesc());
                            if (!isOne(policy.getDbPeriodRateFactor()))
                                szc.append(" x "+policy.getDbPeriodRateDesc());
                            if (!isOne(policy.getDbPremiumFactorValue()))
                                szc.append(" x "+policy.getStPremiumFactorDesc());

                            cv.setStCalculationDesc(szc.toString());
                        }

                    }

                    DTOList policyDocuments = policy.getPolicyDocuments();

                    boolean check = true;
                    if(policyDocuments!=null){
                        for (int i = 0; i < policyDocuments.size(); i++) {
                            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                            if(doc.getStSelectedFlag()!=null){
                                if(doc.getStSelectedFlag().equalsIgnoreCase("Y") && doc.getStFilePhysic()!=null)
                                    check = false;
                            }

                        }
                    }

                    if(!policy.isBypassValidation()){
                        if(check){
                            //if(userate && !autorate){
                            if(cv.getDbRate()!=null){
                                if(!BDUtil.isEqual(cv.getDbRate(), rateOJK, scale))
                                    throw new RuntimeException("Objek No : "+ obj.getStOrderNo() +" - Rate jual ("+ cv.getDbRate() +") tidak sesuai rate OJK ("+ rateOJK +")");
                            }
                        }
                    }
            }

            String kodePos = getPostalCode(obj.getStReference9()).getStPostalCode();
            String kodePos3Digit = kodePos.substring(0,3);

            String zonaByDaerah = getZona(obj.getStReference2()).getStReference1();
            String zonaByKodePos = getZonaByKodePos(kodePos, kodePos3Digit, zonaByDaerah);
            
            if(!policy.isBypassValidation())
                if(zonaByKodePos!=null)
                    if(!zonaByKodePos.trim().equalsIgnoreCase(zonaByDaerah.trim()))
                        throw new RuntimeException("Objek No : "+ obj.getStOrderNo() +" - Kode Pos "+ kodePos +" (Zona "+ zonaByKodePos +") Tidak Sesuai Zona Gempa (Zona "+ zonaByDaerah +")");

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String getRangkaCode(String stReference3) throws Exception
    {
        final SQLUtil S = new SQLUtil();
        
        try
        {
            S.setQuery(
                    "   select " +
                    "     vs_code " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_EQ_CONSTR_CL' and vs_code = ?");
            
            S.setParam(1,stReference3);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return null;
        }finally{
            S.release();
        }
    }
    
    public String getStoreyCode(String stReference10) throws Exception
    {
        final SQLUtil S = new SQLUtil();
        
        try
        {
            S.setQuery(
                    "   select " +
                    "      vs_code " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_EQ_STOREY' and vs_code = ?");
            
            S.setParam(1,stReference10);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return null;
        }finally{
            S.release();
        }
    }

    public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx)
    {
        try
        {
            if(policy.isStatusEndorseRI() || policy.isStatusEndorse() || policy.isStatusClaim() || policy.isStatusClaimEndorse()) return;
            
            int scale = 0;
            
            if(policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            else scale = 0;
            
            BigDecimal rate = getRate(policy,objx,true);
                    
            final DTOList rein = objx.getTreaties();
            
            InsurancePolicyTreatyView reas = (InsurancePolicyTreatyView) rein.get(0);
            
            final DTOList det = reas.getDetails();

            BigDecimal tsiTreaty = null;

            DTOList covers = objx.getCoverage();
            BigDecimal lossRatio = null;

            if (covers!=null && covers.size()>0)
            {
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);
                lossRatio = cv.getCoverPolType().getDbTSIFactor1();

                rate = cv.getDbRate();
            }
            
            for (int i = 0; i < det.size(); i++)
            {
                InsurancePolicyTreatyDetailView detail = (InsurancePolicyTreatyDetailView) det.get(i);
                
                final InsuranceTreatyDetailView trdd = detail.getTreatyDetail();

                tsiTreaty = detail.getDbTSIAmount();
                
                if(trdd.isFacultative() || trdd.isExcessOfLoss()) continue;

                detail.setDbTSIFactor1(lossRatio);
                detail.setDbPremiFactor1(policy.getDbPeriodRateFactor());

                final DTOList ri = detail.getShares();
                
                for (int j = 0; j < ri.size(); j++)
                {
                    InsurancePolicyReinsView member = (InsurancePolicyReinsView) ri.get(j);

                    if(member.isAutoRate() && member.isUseRate()){
                        member.setDbPremiRate(BDUtil.getPctFromMile(rate));

                        member.setDbPremiRate(BDUtil.mul(member.getDbPremiRate(), policy.getDbPeriodRateFactor()));
                        member.setDbPremiRate(BDUtil.mul(member.getDbPremiRate(), policy.getDbPremiumFactorValue()));
                    }


                    //member.setDbPremiRate(BDUtil.mul(member.getDbPremiRate(), policy.getDbPremiumFactorValue(),scale));

                    /*
                    if(member.getDbPremiRate()!=null){
                        member.setDbPremiAmount(BDUtil.mul(member.getDbTSIAmount(), BDUtil.getRateFromPct(member.getDbPremiRate()), scale));
                        member.setDbPremiAmount(BDUtil.mul(member.getDbPremiAmount(), policy.getDbPeriodRateFactor(),scale));
                        member.setDbPremiAmount(BDUtil.mul(member.getDbPremiAmount(), policy.getDbPremiumFactorValue(),scale));
                        
                        if (member.getDbRICommRate() == null) member.setDbRICommRate(detail.getDbComissionRate());
                
                        if(BDUtil.isZeroOrNull(member.getDbRICommRate())) member.setDbRICommAmount(member.getDbRICommAmount());
                        else member.setDbRICommAmount(BDUtil.mul(member.getDbPremiAmount(), BDUtil.getRateFromPct(member.getDbRICommRate()), scale));
                    }*/
                        
                }

                
 
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView objx)
    {
        try
        {
            
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public BigDecimal getRate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) throws Exception
    {
        
        final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
        
        if(obj.getStReference2()==null) throw new RuntimeException("Objek No ("+ obj.getStOrderNo()+") Zona Gempa Belum Diisi");
        if(obj.getStReference3()==null) throw new RuntimeException("Objek No ("+ obj.getStOrderNo()+") Kelas Konstruksi Belum Diisi");
        if(obj.getStReference10()==null) throw new RuntimeException("Objek No ("+ obj.getStOrderNo()+") Jumlah Lantai Belum Diisi");
        
        final SQLUtil S = new SQLUtil();
        
        String zone   = obj.getRiskCategoryMaipark().getStReference1();
        String rangka = getRangkaCode(obj.getStReference3());
        String lantai = getStoreyCode(obj.getStReference10());
        
        logger.logDebug("onCalculate: risk code = "+obj.getRiskCategory().getStInsuranceRiskCategoryCode());
        logger.logDebug("onCalculate: zone = "+ zone);
        logger.logDebug("onCalculate: rangka = "+ rangka);
        logger.logDebug("onCalculate: lantai = "+ lantai);
        
        BigDecimal premiRate2=null;
        
        try
        {
            String query = "select rate1 from ins_rates_big where rate_class= 'EQVET2011' " +
                    " and ref1 = ?  "+
                    " and ref2 = ?  "+
                    " and ref3 = ?  "+
                    " and rate0 = ? "+
                    " and period_start <= ? and period_end >= ?";
            
            final PreparedStatement PS = S.setQuery(query);
            
            int n=1;
            
            PS.setString(n++, zone);
            PS.setString(n++,obj.getRiskCategory().getStInsuranceRiskCategoryCode());
            PS.setString(n++, rangka);
            PS.setInt(n++, Integer.parseInt(lantai));

            S.setParam(n++,policy.getDtPeriodStart()); //period start
            S.setParam(n++,policy.getDtPeriodStart()); //period end
            
            
            final ResultSet RS = PS.executeQuery();
            
            if (RS.next())
            {
                premiRate2 = RS.getBigDecimal(1);
                logger.logInfo("Get rate use kode resiko : "+ premiRate2);
            }
            else
            {
                String query2 = "select rate1 from ins_rates_big where rate_class= 'EQVET2011' " +
                    " and ref1 = ?  "+
                    " and ref2 is null  "+
                    " and ref3 = ?  "+
                    " and rate0 = ? "+
                    " and period_start <= ? and period_end >= ?";
            
                final PreparedStatement PS2 = S.setQuery(query2);

                int n2=1;

                PS2.setString(n2++, zone);
                PS2.setString(n2++, rangka);
                PS2.setString(n2++, lantai);

                S.setParam(n2++,policy.getDtPeriodStart()); //period start
                S.setParam(n2++,policy.getDtPeriodStart()); //period end
            
                final ResultSet RS2 = PS2.executeQuery();
                
                if(RS2.next())
                {
                    premiRate2 = RS2.getBigDecimal(1);
                    logger.logInfo("Get rate not use kode resiko : "+ premiRate2);
                }
            }
            
        }finally{
            S.release();
        }
        
        return premiRate2;
        
    }
    
    private String p(BigDecimal db) {
        return  ConvertUtil.removeTrailing(ConvertUtil.print(db,4));
    }
    
    private boolean isOne(BigDecimal dbx) {
        if (dbx==null) return false;
        return dbx.movePointRight(4).longValue()==10000;
    }

    public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PostalCodeMaiparkView getPostalCode(String stRegionMapID) {
        return (PostalCodeMaiparkView) DTOPool.getInstance().getDTO(PostalCodeMaiparkView.class, stRegionMapID);
    }
    
    public InsuranceRiskCategoryView getZona(String stInsRiskCatID) {
        return (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, stInsRiskCatID);
    }

    public String getZonaByKodePos(String kodePos, String kodePos3Digit, String zona) throws Exception
    {
        final SQLUtil S = new SQLUtil();

        try
        {
            S.setQuery(
                    "   select * "+
                    " from ( "+
                    " select *,2 as rank "+
                    " from s_postal_code "+
                    " where postal_code_3_digit = ? and zone_code = ? "+
                    " )x order by rank limit 1");

            //S.setParam(1,kodePos);
            S.setParam(1,kodePos3Digit);
            S.setParam(2,zona);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getString("zone_code");

            return null;
        }finally{
            S.release();
        }
    }

}
