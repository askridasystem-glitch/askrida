/***********************************************************************
 * Module:  com.webfin.insurance.custom.PAKreasiHandler
 * Author:  Denny Mahendra
 * Created: Sep 21, 2006 12:56:37 AM
 * Purpose:
 ***********************************************************************/
 
package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.webfin.FinCodec;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.model.ARInvoiceView;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import org.joda.time.Months;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PAKreasiHandler extends DefaultCustomHandler {

    private final static transient LogManager logger = LogManager.getInstance(PAKreasiHandler.class);

    private String jenisKredit;
    private String jenisRate;

    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {

               calculate(policy, objx, validate);

               //calculatePenerapanRateJual(policy, objx, validate);

               //calculateProsesReas(policy, objx, validate);
   
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String p(BigDecimal db) {
        return  ConvertUtil.removeTrailing(ConvertUtil.print(db,4));
    }
 
    public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            BigDecimal selisihPremi = null;

            selisihPremi = BDUtil.sub(obj.getDbReference6(),obj.getDbReference2());

            //objx.setDbObjectPremiTotalAmount(selisihPremi);
            objx.setDbObjectPremiTotalAmount(obj.getDbReference6());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView objx) {
        try {
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            if(!policy.isStatusClaim()){
                obj.setDtReference5(null);
                obj.setStReference15(null);
                obj.setStReference16(null);
                obj.setStReference14(null);
                obj.setDbReference6(BDUtil.zero);
                obj.setDbReference9(BDUtil.zero);

                if(!BDUtil.isZeroOrNull(obj.getDbReference7())){
                    obj.setStReference11("Y");
                }

                final DTOList covers = objx.getCoverage();
                for(int i=0;i<covers.size();i++){
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                    cv.setStEntryPremiFlag(null);
                }
            }

            if(obj.getStReference78()!=null){
                obj.setStReference8(obj.getStReference78());
                //if(obj.getStReference78().equalsIgnoreCase("2000"))      
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateApproval(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj) throws Exception{
        boolean persetujuanPusat = Tools.isYes(obj.getStReference21());

        if(policy.getStEffectiveFlag()!=null)
        {
            if(policy.getStEffectiveFlag().equalsIgnoreCase("Y")){
                    if(policy.isStatusPolicy() || policy.isStatusRenewal())
                    {
                            if (!Tools.isYes(obj.getStReference10())){
                                    if(BDUtil.isZeroOrNull(obj.getDbReference1()))
                                        if(!obj.getStReference8().equalsIgnoreCase("1"))
                                            throw new RuntimeException("Rate Koas Debitur "+obj.getStReference1()+" Tidak Ditemukan, Konfirmasikan Ke Bag Underwriting Pusat");
                            }

                            //final boolean blockPremiCoinsuranceMinus = Parameter.readBoolean("BLOCKING_COINS_PREMI");
                            //if(blockPremiCoinsuranceMinus)

                             if(BDUtil.biggerThan(obj.getDbReference2(), obj.getDbReference6()))
                                    throw new RuntimeException("Premi Koas Debitur "+obj.getStReference1()+ " Lebih Besar Dari Premi Askrida");

                            if(policy.getStCostCenterCode().equalsIgnoreCase("22")) return;

                            BigDecimal usiaMaksimal = new BigDecimal(55);
                            BigDecimal usiaMaksimalDB = getUsiaLimit("ACCEPT", policy.getStPolicyTypeID(),SessionManager.getInstance().getUserID());

                            if(!BDUtil.isZeroOrNull(usiaMaksimalDB)) usiaMaksimal = usiaMaksimalDB;

                            if(!persetujuanPusat){
                                if(Integer.parseInt(obj.getStReference2())> Integer.parseInt(String.valueOf(usiaMaksimal)) && BDUtil.biggerThan(obj.getDbReference4(), new BigDecimal(100000000)))
                                        throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh > "+ usiaMaksimal +" Tahun dan TSI > Rp. 100.000.000");

                                DateTime startDate = new DateTime(obj.getDtReference2());
                                DateTime polDate = new DateTime(policy.getDtPolicyDate());

                                if((polDate.getYear() - startDate.getYear()) > 10)
                                    throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());
                            }

                            int jumlahHariKerja = Integer.parseInt(String.valueOf(DateUtil.daysWithoutWeekend(obj.getDtReference2(), new Date())));
                            int jumlahHariWeekend = Integer.parseInt(String.valueOf(DateUtil.weekendDays(obj.getDtReference2(), new Date())));
                            DateTime batasWaktu = new DateTime(obj.getDtReference2());
                            batasWaktu = batasWaktu.plusDays(jumlahHariKerja);
                            batasWaktu = batasWaktu.plusDays(jumlahHariWeekend - 1);
                            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

                            String str = fmt.print(batasWaktu);

//                            if(!policy.isEditReasOnlyMode())
//                                if(jumlahHariKerja > 60)
//                                    if(!persetujuanPusat)
//                                        throw new RuntimeException("Debitur "+ obj.getStReference1() +" melebihi batas waktu 60 hari kerja, maks. tanggal "+ str);

                    }

                    if(policy.isStatusEndorse()){
                            //final String check = policy.getStKreasiTypeID()+policy.getStCoinsID()+obj.getStReference4()+obj.getStReference2();
                              if(obj.getStReference18()==null)
                                if(!BDUtil.isZeroOrNull(obj.getDbReference6()))
                                    if(BDUtil.isZeroOrNull(obj.getDbReference2()))
                                        if(!obj.getStReference8().equalsIgnoreCase("1"))
                                            throw new RuntimeException("Premi Koas Debitur "+obj.getStReference1()+" Nol, Konfirmasikan Ke Bag Underwriting Pusat");
                                        //if(!check.equalsIgnoreCase(obj.getStReference17()))
                                            //throw new RuntimeException("Premi Koas Debitur "+obj.getStReference1()+" Nol, Konfirmasikan Ke Bag Underwriting Pusat");
                    }
            }
        }
   }
 
    public void calculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
        try{
    
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            final DTOList covers = obj.getCoverage();
            final Date perStart = obj.getDtReference2();
            final Date perEnd = obj.getDtReference3();
            int scale = 0;

            if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            policy.setStLockCoinsFlag("Y");

            boolean useRate = Tools.isYes(obj.getStReference9());
            boolean manualRIRate = Tools.isYes(obj.getStReference10());
            boolean manualPremiKoas = Tools.isYes(obj.getStReference14());
            boolean manualRIComRate = Tools.isYes(obj.getStReference11());
            final Date birth = obj.getDtReference1();// birth date
            boolean manualReinsurer = Tools.isYes(obj.getStReference19());

            //otomatis set stnc to all object sesuai objek 1 kecuali endorse
            final InsurancePolicyObjDefaultView obj1 = (InsurancePolicyObjDefaultView) policy.getObjects().get(0);

            if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

                boolean persetujuanPusat = Tools.isYes(obj.getStReference21());

                if(!persetujuanPusat)
                    obj.setDtReference6(obj1.getDtReference6());
            }

            if(!policy.isStatusEndorse()){
                if(validate){
                    if (birth!=null) {
                        
                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));

                    }
                }
            }

            if(policy.isStatusEndorse()){
                if(obj.getStReference2()==null){
                    if (birth!=null) {
                        //long age = System.currentTimeMillis()-(birth.getTime());
                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));
                    }
                }
            }

            if(Integer.parseInt(obj.getStReference2()) < 17)
                throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

            //if(Integer.parseInt(obj.getStReference2()) > 75)
                //throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Melebihi 75 Tahun");

            boolean validPeriod = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()))<=0;

            if (!validPeriod) throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah Debitur "+obj.getStReference1());
            
            int tgl = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()));
            
            if(tgl == 0) throw new RuntimeException("Tanggal Mulai dan Akhir Kredit Debitur "+obj.getStReference1()+" Tidak Boleh Sama");

            DateTime newDate = new DateTime(new Date());
            DateTime errorDate2 = newDate.plusYears(1);
            
            //boolean validPeriod3 = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(errorDate2.toDate()))<=0;

            obj.setStReference4(null);

            if (perStart!=null && perEnd!=null) {

                long age = perEnd.getTime()-perStart.getTime();
                //long agemonth = age / (1000l*60l*60l*24l*30l);
                long ageyear = age / (1000l*60l*60l*24l*365l);

                obj.setStReference4(String.valueOf(ageyear));

                DateTime birthDate = new DateTime(birth);
                DateTime startDate = new DateTime(perStart);
                DateTime endDate = new DateTime(perEnd);
                Months m = Months.monthsBetween(startDate, endDate);
                Years y = Years.yearsBetween(startDate, endDate);
                int mon = m.getMonths();
                int year = y.getYears();
                obj.setStReference6(String.valueOf(mon));

                DateTime polDate = new DateTime(policy.getDtPolicyDate());

                if(endDate.isBefore(startDate))
                    throw new RuntimeException("Tanggal akhir kredit Debitur "+obj.getStReference1()+" tidak boleh kurang dari tanggal mulai");
                
                if(startDate.isEqual(birthDate))
                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal lahir");

                if(startDate.isEqual(endDate))
                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal akhir");
                    
                if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
                    if(year>40)
                        throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah, Debitur "+obj.getStReference1());

                    if(Integer.parseInt(obj.getStReference2())>100)
                        throw new RuntimeException("Usia Debitur "+obj.getStReference1() + " Salah");

                    if(Integer.parseInt(obj.getStReference2()) < 17)
                            throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

                    if(Integer.parseInt(obj.getStReference2()) > 75)
                        throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Melebihi 75 Tahun");

                    if(startDate.getYear() > polDate.getYear())
                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());

                    long ageCek = perStart.getTime()-(birth.getTime());

                    ageCek = ageCek / (1000l*60l*60l*24l*365l);

                    if(ageCek > 75 || ageCek < 17)
                            throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Salah, Cek Tanggal Lahir");

//                    if((polDate.getYear() - startDate.getYear()) > 10)
//                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());

                    DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();

                    if(startDate.isAfter(policyDateLastDay))
                        throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh > tanggal polis");
                }

            }
            
            // MARK FOR PROSES
            if(!manualReinsurer)
                applyCoinsurance1Des2016(obj, policy);
                //applyCoinsurance19Juni2015(obj, policy);
                    
            if (obj.getDbReference4()!=null){
                DTOList tsiList = obj.getSuminsureds();
                if (tsiList!=null && tsiList.size()==1) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(0);

                    tsi.setDbInsuredAmount(obj.getDbReference4());
                }
            }

            //BigDecimal jualRate=null;
            //BigDecimal grossUp2=null;
            //BigDecimal premiRate=null;
            boolean isOR = false;
            if(obj.getStReference8()!=null)
                if(obj.getStReference8().equalsIgnoreCase("1")) isOR = true;

            boolean getRate = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()) || (policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference1()) && !isOR);
            //boolean getRate = true;

            //String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
            //String jenis_rate = getJenisRate(policy.getStKreasiTypeID());

            
            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if(getRate){
                        getJenisKredit(policy.getStKreasiTypeID());
                        applyRateProRataNew(obj.getStReference4(), getJenisRate(), getJenisKredit(), obj, policy);
                }
            }
            
            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                    /*if(getRate){
                        final SQLUtil S = new SQLUtil();
                        try {

                            String sql =  "select rate"+ obj.getStReference4() +", gross_up" +
                                    " from ins_rates_big where ref4 = ? and ref5 = ? and (ref3 = ? or ref3 is null) and refid1 = ?"+
                                    " and period_start<=? and period_end>=?"+
                                    " and rate_class='PAKREASI_NEW'";

                            final PreparedStatement PS = S.setQuery(sql);

                            int n=1;

                            PS.setString(n++, jenis_rate); //RATA/TABEL
                            PS.setString(n++, jenis_kredit); //BAKI/PLAFON

                            //PS.setString(n++,policy.getStCostCenterCode()); //ref2

                            PS.setString(n++,obj.getStReference2()); //ref3
                            //PS.setString(n++,obj.getStReference22()); //ref3

                            if(obj.getStReference8()!=null) PS.setString(n++,obj.getStReference8()); //ref1
                            else PS.setString(n++,policy.getStCoinsID()); //ref1

                            S.setParam(n++,policy.getDtPolicyDate()); //period start
                            S.setParam(n++,policy.getDtPolicyDate()); //period end

                            final ResultSet RS = PS.executeQuery();

                            if (RS.next()){
                                //premiRate = RS.getBigDecimal(1);
                                //grossUp2  = RS.getBigDecimal(2);
                                if (!manualRIRate) obj.setDbReference1(RS.getBigDecimal(1));
                                if (!manualRIComRate) obj.setDbReference7(RS.getBigDecimal(2));
                            }else{
                                if (!manualRIRate) obj.setDbReference1(BDUtil.zero);
                                if (!manualRIComRate) obj.setDbReference7(BDUtil.zero);
                            }

                        } finally {
                            S.release();
                        }
                    }
                    */
                    
                //}
                //end

                BigDecimal totalRate = new BigDecimal(0);
                BigDecimal totalPremi = new BigDecimal(0);

                //calculate premi 100%
                if (covers!=null && covers.size()>1) {
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        cv.setStEntryRateFlag(obj.getStReference9());

                        if(Tools.isYes(cv.getStAutoRateFlag()))
                            cv.setStAutoRateFlag(null);

                        totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                        totalRate = BDUtil.add(totalRate,cv.getDbRate());

                        if(BDUtil.isZeroOrNull(cv.getDbRate())){
                            cv.setDbRate(BDUtil.getMileFromRate(BDUtil.div(cv.getDbPremiNew(),cv.getDbInsuredAmount(),5)));
                        }
                    }

                    obj.setDbReference5(totalRate);
                    obj.setDbReference6(totalPremi);
                    
                    if(!policy.isStatusHistory()){
                        if(!useRate) obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                    }
               }else if (covers!=null && covers.size()==1) {
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                    cv.setStEntryRateFlag(obj.getStReference9());

                    if(Tools.isYes(cv.getStAutoRateFlag()))
                        cv.setStAutoRateFlag(null);

                    if (useRate) {
                        cv.setDbRate(obj.getDbReference5());
                        if(!policy.isStatusHistory()){
                            obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                        if(policy.isStatusHistory() && BDUtil.isZeroOrNull(obj.getDbReference6())){
                             obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                    }else{
                        cv.setDbPremiNew(obj.getDbReference6());
                        cv.setDbPremi(obj.getDbReference6());
                        
                        obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                        cv.setDbRate(obj.getDbReference5());
                    }

                    if(policy.isStatusEndorse()){
                        obj.setDbReference5(cv.getDbRate());
                        obj.setDbReference6(cv.getDbPremi());
                    }
                }
                //end calculate premi 100%
            }

            objx.reCalculate();

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {

                if (!manualRIRate){
                    //BigDecimal rateGross = BDUtil.div(premiRate,BDUtil.sub(new BigDecimal(100),grossUp2),5);
                    //obj.setDbReference1(premiRate);
                    /*if(BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(premiRate);
                    else if(!BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(BDUtil.getPctFromRate(rateGross));
                    */
                    //BLOCK DATA BANDUNG USIA > 70
                    //if(policy.getStCostCenterCode().equalsIgnoreCase("21"))
                    if(Integer.parseInt(obj.getStReference2())>70)
                        obj.setDbReference1(BDUtil.zero);
                }

                //start endorse calc
                BigDecimal premiAmountDiff = null;
                if(policy.isStatusEndorse()){
                    BigDecimal insAmountDiff = null;
                    BigDecimal totalInsAmountDiff = null;
                    for (int l = 0; l < covers.size(); l++) {
                        InsurancePolicyCoverView icv = (InsurancePolicyCoverView) covers.get(l);

                        final InsurancePolicyCoverView refCover = icv.getRefCover();

                        if(refCover==null){
                            insAmountDiff = obj.getDbReference4();
                        }else{
                            insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                            premiAmountDiff = BDUtil.sub(icv.getDbPremi(),refCover.getDbPremi());
                            totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                        }
                    }
                    obj.setDbObjectInsuredAmount(insAmountDiff);
                    obj.setDbReference11(insAmountDiff);
                }

                //end endrose

                if (manualPremiKoas){
                    obj.setDbReference2(obj.getDbReference2());
                    if(!policy.isStatusEndorse()){
                        obj.setDbReference1(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference2(),obj.getDbReference4(),5)));
                    }
                }else{
                    
                    obj.setDbReference2(
                            BDUtil.mul(BDUtil.getRateFromMile(obj.getDbReference1()), obj.getDbObjectInsuredAmount(),scale)
                            );
                    
                    obj.setDbReference17(obj.getDbReference2());
                    
                    if(!BDUtil.isZeroOrNull(obj.getDbReference7())){
                        obj.setDbReference2(BDUtil.divRoundUp(obj.getDbReference2(),BDUtil.getRateFromPct(BDUtil.sub(new BigDecimal(100),obj.getDbReference7())),scale));
                    }
                    
                     //MARK FOR PROSES 
                    if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse() && !policy.isStatusEndorse()){
                        if(getRate){
                            if(BDUtil.biggerThan(obj.getDbReference2(), obj.getDbReference6())){
                                obj.setStReference8("1");
                                obj.setDbReference1(BDUtil.zero);
                                obj.setDbReference2(BDUtil.zero);
                                obj.setDbReference7(BDUtil.zero);
                            }
                        }
                    }
                    
                    if(policy.isStatusEndorse() && !BDUtil.isZeroOrNull(premiAmountDiff) && BDUtil.isZeroOrNull(obj.getDbReference2())){
                        //BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                        BigDecimal tsi = obj.getDbReference4()!=null?obj.getDbReference4():obj.getDbObjectInsuredAmount();
                        BigDecimal premiKoas = null;
                        BigDecimal premiKoasParent = BDUtil.mul(tsi,BDUtil.getRateFromMile(obj.getDbReference1()));
                        premiKoas = BDUtil.mul(BDUtil.div(obj.getDbReference6(), obj.getDbReference12()),premiKoasParent);
                        premiKoas = BDUtil.roundUp(premiKoas);
                        
                        obj.setDbReference2(premiKoas);
                    }
                    /*
                    if(policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference2()) && !BDUtil.isZeroOrNull(obj.getDbReference6())){
                        obj.setDbReference2(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference1()), scale)); 
                    }*/
                }
            }

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if (!manualRIComRate)
                    obj.setDbReference7(obj.getDbReference7());

                if (manualRIComRate)
                    obj.setDbReference9(
                        BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference7()), obj.getDbReference2(),scale)
                        );       
            }
            
            if(policy.isStatusEndorse() && obj.getDtReference5()!=null) {
                recalculateRestitusi(policy, obj, obj.getDbReference1(), obj.getDbReference7());
            }

            DTOList coinsMurni = policy.getCoins2();
            BigDecimal askridaSharePct = null;
            BigDecimal askridaShareTSI = null;
            BigDecimal askridaSharePremi = null;
            
            for(int i = 0 ; i< coinsMurni.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni.get(i);
                
                if(!coins3.isHoldingCompany()) continue;
                
                if(coins3.isHoldingCompany()){
                    askridaSharePct = coins3.getDbSharePct();
                    askridaShareTSI = coins3.getDbAmount();
                    askridaSharePremi = coins3.getDbPremiAmount();
                }
            }

            DTOList coins = policy.getCoinsCoverage();

            if(!policy.isManualCoins()){
                if (coins.size()>1) {
                    while (coins.size()>1) coins.delete(coins.size()-1);
                }
            }
            

            if (coins.size()==0) throw new RuntimeException("Unknown coinsurance model ... (size==0)");

            //here
            final DTOList oj = policy.getObjects();
            String [] coax = new String[5];
            int counter = 0;
            BigDecimal totalPremiCo94 = null;
            BigDecimal totalKomisiCo94 = null;
            BigDecimal totalPremiCo96= null;
            BigDecimal totalKomisiCo96 = null;
            BigDecimal totalPremiCo2000 = null;
            BigDecimal totalKomisiCo2000 = null;
            BigDecimal totalPremiCo2001 = null;
            BigDecimal totalKomisiCo2001 = null;
            BigDecimal totalPremiCo2002 = null;
            BigDecimal totalKomisiCo2002 = null;

            for (int i = 0; i < oj.size(); i++)
            {
                InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);
                
                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;
                
                if(obje.getStReference8()!=null){
                    if(obje.getStReference8().equalsIgnoreCase("94")){
                        totalPremiCo94 = BDUtil.add(totalPremiCo94, obje.getDbReference2());
                        totalKomisiCo94 = BDUtil.add(totalKomisiCo94, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("96")){
                        totalPremiCo96 = BDUtil.add(totalPremiCo96, obje.getDbReference2());
                        totalKomisiCo96 = BDUtil.add(totalKomisiCo96, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2000")){
                        totalPremiCo2000 = BDUtil.add(totalPremiCo2000, obje.getDbReference2());
                        totalKomisiCo2000 = BDUtil.add(totalKomisiCo2000, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2001")){
                        totalPremiCo2001 = BDUtil.add(totalPremiCo2001, obje.getDbReference2());
                        totalKomisiCo2001 = BDUtil.add(totalKomisiCo2001, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2002")){
                        totalPremiCo2002 = BDUtil.add(totalPremiCo2002, obje.getDbReference2());
                        totalKomisiCo2002 = BDUtil.add(totalKomisiCo2002, obje.getDbReference9());
                    }
                }
                
                if(obje.getStReference8()!=null){
                    if(i!=0){
                        if(coax[0]!=null)
                            if(coax[0].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                        
                        if(coax[1]!=null)
                            if(coax[1].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                        
                        if(coax[2]!=null)
                            if(coax[2].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                        
                        if(coax[3]!=null)
                            if(coax[3].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                        
                        if(coax[4]!=null)
                            if(coax[4].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                    }
                    
                    if(obje.getStReference8().equalsIgnoreCase("1")) continue;
                    coax [counter] = obje.getStReference8();
                    counter++;
                }

            }
            
            if(policy.isStatusClaim() || policy.isStatusClaimEndorse()){
                counter = 0;
                coax = new String[5];
                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) policy.getClaimObject();
                if(obje.getStReference8()!=null)
                    coax[0] = obje.getStReference8();
                
                if(obje.getStReference8()==null) coax[0] = policy.getStCoinsID();
                
                counter++;
            }

            if(!policy.isManualCoins()){
                for (int i = 0; i < counter; i++){
                    final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

                    co.markNew();
                    co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);
                    co.setStAutoPremiFlag("N");
                    co.setStFlagEntryByRate("Y");
                    co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);
                    co.setStEntityID(coax[i]);
                    coins.add(co);
                }
            }

             BigDecimal totPremi = BDUtil.zero;
             BigDecimal totPremiCoas = BDUtil.zero;
            //BACKUP COAS
               if (coins.size()>0){
                  for(int i = 0 ; i< coins.size();i++){
                        InsurancePolicyCoinsView coins2 = (InsurancePolicyCoinsView) coins.get(i);

                        DTOList objects = policy.getObjects();
                        totPremi = (BigDecimal) objects.getTotal("refn6");
                        BigDecimal totRIPremi = (BigDecimal) objects.getTotal("refn2");
                        //BigDecimal total = BDUtil.add(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))), BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct))));

                        if(coins2.isHoldingCompany()){
                            coins2.setDbSharePct(BDUtil.hundred);
                            coins2.setDbAmount(askridaShareTSI);
                            coins2.setStEntityID("1");
                            coins2.setStAutoPremiFlag("N");
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))));
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                            //if(!BDUtil.isEqual(askridaSharePremi, total, 0))
                                //coins2.setDbPremiAmount(BDUtil.sub(askridaSharePremi, BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct)))));
                        }else if(coins2.getStEntityID().equalsIgnoreCase("94")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo94);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo94, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("96")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo96);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo96, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2000")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2000);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2000, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2001")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2001);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2001, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2002")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2002);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2002, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }
                   }     
               }
            
            DTOList coinsMurni2 = policy.getCoins2();
            
            for(int i = 0 ; i< coinsMurni2.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni2.get(i);
                
                if(coins3.isHoldingCompany() && !coins3.isManualPremi()) coins3.setDbPremiAmount(BDUtil.roundUp(totPremiCoas));
                
                if(!coins3.isHoldingCompany() && !coins3.isManualPremi()){
                    coins3.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(coins3.getDbSharePct()), totPremi));
                    coins3.setDbPremiAmount(BDUtil.roundUp(coins3.getDbPremiAmount()));
                }
                
                if(coins3.isManualPremi())
                    coins3.setDbPremiAmount(coins3.getDbPremiAmount());
            }
             
            if(!policy.isStatusEndorse()){
                obj.setDbReference12(obj.getDbReference6());
            }
            
            //VALIDATING APPROVAL
            if(policy.getStEffectiveFlag()!=null){
                if(policy.getStEffectiveFlag().equalsIgnoreCase("Y"))
                     validateApproval(policy, obj);
            }
            
            boolean manualNoRekap = Tools.isYes(obj.getStReference23());

            if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal()){

                if(!manualNoRekap){
                    obj.setStRekapKreasi(generateNoRekap(obj.getStReference8(), policy));
                }

                obj.setStRekapKreasi(obj.getStRekapKreasi().toUpperCase());

            }

            //checkingPremiBalance(obj);
            //PERHITUNGAN RESTITUSI PREMI
            /*if(policy.isStatusEndorse() && isRestitusi) {
                recalculateRestitusiNew(policy, obj, premiRate, grossUp2, coins, askridaSharePct);
            }*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void recalculateRestitusi(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj, BigDecimal premiRate, BigDecimal grossUp2) throws Exception{
        final DTOList covers = obj.getCoverage();    
        final Date perStart = obj.getDtReference2();
        final Date perEnd = obj.getDtReference3();

        int scale = 0;
        //BigDecimal totPremiRIEndorse = null;
        //BigDecimal totCommissionRIEndorse = null;

        if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        
        boolean manualRIRate = Tools.isYes(obj.getStReference10());

        boolean manualPremiKoas = Tools.isYes(obj.getStReference14());

        boolean manualRIComRate = Tools.isYes(obj.getStReference11());
            
        if(policy.isStatusEndorse() && obj.getDtReference5()!=null) {
            if(!BDUtil.isZero(obj.getDbReference4())){
                throw new RuntimeException("Debitur "+ obj.getStReference1() +" Harus Nol TSI nya untuk Restitusi");
            }
            
            //if(obj.getStVoidFlag()!=null)
            //    if(Tools.isYes(obj.getStVoidFlag()))
             //       throw new RuntimeException("Debitur "+ obj.getStReference1() +" Sudah Pernah di buat klaim");
                
            
                BigDecimal premiSisa = null;
                int sisaJangkaWaktu = 0;
                DateTime tglLunas = new DateTime(obj.getDtReference5());
                DateTime tglAwalKredit = new DateTime(obj.getDtReference2());
                Months bulanBerjalanM = Months.monthsBetween(tglAwalKredit, tglLunas);
                int bulanBerjalan = bulanBerjalanM.getMonths();

                DateTime startDate = new DateTime(perStart);
                DateTime endDate = new DateTime(perEnd);
                Months m = Months.monthsBetween(startDate, endDate);
                int mon = m.getMonths();

                sisaJangkaWaktu = mon - bulanBerjalan;
                
                if(obj.getStReference15()==null)
                    obj.setStReference15(String.valueOf(sisaJangkaWaktu));

                //final BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                BigDecimal premi = null;
                premi = BDUtil.negate(obj.getDbReference12());
                if(BDUtil.isZeroOrNull(obj.getDbReference12()))
                    premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);

                premiSisa = BDUtil.mul(BDUtil.mul(premi,policy.getDbPeriodRateBeforeFactor(), scale),new BigDecimal(obj.getStReference15()), scale);
                premiSisa = BDUtil.div(premiSisa,new BigDecimal(mon));
                premiSisa = BDUtil.roundUp(premiSisa);

                obj.setDbReference6(premiSisa);

                BigDecimal totalRate = new BigDecimal(0);
                BigDecimal totalPremi = new BigDecimal(0);
                BigDecimal premiBruto = null;
                if (covers!=null) {
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        final InsurancePolicyCoverView refCover = cv.getRefCover();
                        totalPremi = BDUtil.add(totalPremi,refCover.getDbPremi());
                        totalRate = BDUtil.add(totalRate,refCover.getDbRate());
                    }

                    if(covers.size()>1){
                        for(int i=0;i<covers.size();i++){
                            InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                            final InsurancePolicyCoverView refCover = cv.getRefCover();

                            cv.setStEntryRateFlag(obj.getStReference9());
                            
                            BigDecimal premiRef = BDUtil.mul(refCover.getDbInsuredAmount(), BDUtil.getRateFromMile(refCover.getDbRate()), scale);
                            
                            cv.setDbPremiNew(BDUtil.div(BDUtil.mul(premiSisa,premiRef, scale),totalPremi));
                            cv.setDbPremi(cv.getDbPremiNew());
                            
                            if(BDUtil.isZeroOrNull(refCover.getDbPremi()) || BDUtil.biggerThanZero(cv.getDbPremi())){
                                if(i==0){
                                    cv.setDbPremiNew(obj.getDbReference6());
                                    cv.setDbPremi(cv.getDbPremiNew());
                                }else if(i>0){
                                    cv.setDbPremiNew(BDUtil.zero);
                                    cv.setDbPremi(cv.getDbPremiNew());
                                } 
                            }

                            final String curCalc = cv.getStCalculationDesc();

                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(p(premi));

                            if (!BDUtil.isEqual(policy.getDbPeriodRateBeforeFactor(),BDUtil.one,0))
                                szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());

                            szCalc.append(" x "+obj.getStReference15()+" / "+mon);

                            String calc = szCalc.toString();

                            cv.setStCalculationDesc(calc);

                            premiBruto = BDUtil.add(premiBruto,cv.getDbPremi());
                        }
                    }

                    if(covers.size()==1){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                        final InsurancePolicyCoverView refCover = cv.getRefCover();

                        cv.setStEntryRateFlag(obj.getStReference9());
                        cv.setDbPremiNew(obj.getDbReference6());
                        cv.setDbPremi(obj.getDbReference6());

                        final String curCalc = cv.getStCalculationDesc();

                        final StringBuffer szCalc = new StringBuffer();
                        szCalc.append(premi);

                        if (!BDUtil.isEqual(policy.getDbPeriodRateBeforeFactor(),BDUtil.one,0))
                            szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());

                        szCalc.append(" x "+obj.getStReference15()+" / "+mon);

                        String calc = szCalc.toString();

                        cv.setStCalculationDesc(calc);

                        premiBruto = BDUtil.add(premiBruto,cv.getDbPremi());
                    }

                }

                BigDecimal premiKoas = null;
                if (obj.getStReference2()!=null && obj.getStReference4()!=null) {

                    if (!manualRIRate){
                        //BigDecimal rateGross = BDUtil.div(obj.getDbReference1(),BDUtil.sub(new BigDecimal(100),obj.getdbr),5);
            
                        //if(!BDUtil.isZeroOrNull(obj.getDbReference7())) obj.setDbReference1(BDUtil.getPctFromRate(rateGross));
                    }

                    if (manualPremiKoas){
                        obj.setDbReference2(obj.getDbReference2());
                        obj.setDbReference1(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference2(),obj.getDbReference4(),5)));
                    }else{
                        BigDecimal premiKoasParent = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference1()));
                        premiKoas = BDUtil.mul(BDUtil.div(obj.getDbReference6(), premi),premiKoasParent);
                        premiKoas = BDUtil.roundUp(premiKoas);
                        obj.setDbReference2(premiKoas);
                    }

                /* BUKA BLOCKING TIDAK ADA RATE KOAS
                if (!manualRIRate){
                if(BDUtil.isZeroOrNull(obj.getDbReference1()))
                            throw new RuntimeException("Rate Tidak Ditemukan, Cek Data Debitur "+obj.getStReference1()+" !");
                             }*/
                }

                if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                    obj.setDbReference9(
                            BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference7()), obj.getDbReference2(),scale)
                            );
                }

                /*
                if (coins.size()==0) throw new RuntimeException("Unknown coinsurance model ... (size==0)");

                BigDecimal totalCoinsPremi = null;
                BigDecimal totPremi = null;
                
                final DTOList oj = policy.getObjects();
                String [] coax = new String[10];
                int counter = 0;
                BigDecimal totalPremiCo94 = null;
                BigDecimal totalKomisiCo94 = null;
                BigDecimal totalPremiCo96 = null;
                BigDecimal totalKomisiCo96 = null;
                BigDecimal totalPremiCo2000 = null;
                BigDecimal totalKomisiCo2000 = null;

                for (int i = 0; i < oj.size(); i++)
                {
                    InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);

                    InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;

                    if(obje.getStReference8()!=null){
                        if(obje.getStReference8().equalsIgnoreCase("94")){
                            totalPremiCo94 = BDUtil.add(totalPremiCo94, obje.getDbReference2());
                            totalKomisiCo94 = BDUtil.add(totalKomisiCo94, obje.getDbReference9());
                        }else if(obje.getStReference8().equalsIgnoreCase("96")){
                            totalPremiCo96 = BDUtil.add(totalPremiCo96, obje.getDbReference2());
                            totalKomisiCo96 = BDUtil.add(totalKomisiCo96, obje.getDbReference9());
                        }else if(obje.getStReference8().equalsIgnoreCase("2000")){
                            totalPremiCo2000 = BDUtil.add(totalPremiCo2000, obje.getDbReference2());
                            totalKomisiCo2000 = BDUtil.add(totalKomisiCo2000, obje.getDbReference9());
                        }
                    }
                }

                if (coins.size()>1){
                      for(int i = 0 ; i< coins.size();i++){
                            InsurancePolicyCoinsView coins2 = (InsurancePolicyCoinsView) coins.get(i);

                            DTOList objects = policy.getObjects();
                            totPremi = (BigDecimal) objects.getTotal("refn6");
                            BigDecimal totPremi2 = (BigDecimal) objects.getTotal("refn6");
                            BigDecimal totRIPremi = (BigDecimal) objects.getTotal("refn2");
                            BigDecimal total = BDUtil.add(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))), BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct))));

                            if(coins2.isHoldingCompany()){
                                coins2.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
                                coins2.setDbPremiAmount(BDUtil.mul(BDUtil.sub(totPremi2,totRIPremi),BDUtil.getRateFromPct(askridaSharePct)));
                                logger.logDebug("+++++++++++ PREMI COAS ABA +++++++++++");
                                logger.logDebug("totPremi2 : "+totPremi2);
                                logger.logDebug("totRIPremi : "+totRIPremi);
                                logger.logDebug("askridaSharePct : "+askridaSharePct);
                                logger.logDebug("+++++++++++ PREMI COAS ABA +++++++++++");
                                coins2.setDbPremiAmount(BDUtil.roundUp(coins2.getDbPremiAmount()));
                                totalCoinsPremi = BDUtil.add(totalCoinsPremi, coins2.getDbPremiAmount());
                            }else if(coins2.getStEntityID().equalsIgnoreCase("94")){
                                coins2.setDbSharePct(BDUtil.zero);
                                coins2.setDbPremiAmount(totalPremiCo94);
                                coins2.setDbCommissionAmount(totalKomisiCo94);
                              
                                obj.setDbReference3(coins2.getDbHandlingFeeRate());
                                totalCoinsPremi = BDUtil.add(totalCoinsPremi, coins2.getDbPremiAmount());
                            }else if(coins2.getStEntityID().equalsIgnoreCase("96")){
                                coins2.setDbSharePct(BDUtil.zero);
                                
                                coins2.setDbPremiAmount(totalPremiCo96);
                                coins2.setDbCommissionAmount(totalKomisiCo96);
                                obj.setDbReference3(coins2.getDbHandlingFeeRate());
                                totalCoinsPremi = BDUtil.add(totalCoinsPremi, coins2.getDbPremiAmount());
                            }else if(coins2.getStEntityID().equalsIgnoreCase("2000")){
                                coins2.setDbSharePct(BDUtil.zero);
                                coins2.setDbPremiAmount(totalPremiCo2000);
                                coins2.setDbCommissionAmount(totalKomisiCo2000);
                                obj.setDbReference3(coins2.getDbHandlingFeeRate());
                                totalCoinsPremi = BDUtil.add(totalCoinsPremi, coins2.getDbPremiAmount());
                            }
                       }     
                   }

            
            DTOList coinsMurni2 = policy.getCoins2();
            
            for(int i = 0 ; i< coinsMurni2.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni2.get(i);
                
                if(coins3.isHoldingCompany()) coins3.setDbPremiAmount(BDUtil.roundUp(totalCoinsPremi));
                
                if(!coins3.isHoldingCompany()){
                    coins3.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(coins3.getDbSharePct()), totPremi));
                    coins3.setDbPremiAmount(BDUtil.roundUp(coins3.getDbPremiAmount()));
                }
            }*/

            //validasi sudah pernah klaim belum
            if(!BDUtil.isZeroOrNull(premiSisa)){
                policy.cekSudahPernahKlaim(obj);
            }

            if(policy.getStEndorseNotes()==null){
                policy.setStEndorseNotes("DENGAN INI DICATAT DAN DISETUJUI, BAHWA :\n\n"+
                        "1). PINJAMAN KREDIT ATAS NAMA YANG TERCANTUM PADA LAMPIRAN POLIS INI TELAH DILUNASI.\n"+
                        "2). ATAS HAL TERSEBUT, MAKA DILAKUKAN RESTITUSI PREMI SESUAI DENGAN SYARAT DAN KETENTUAN YANG BERLAKU");
            }
                
            }
    }
    
    private void applyCoinsurance(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy){
        
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
                 
            final boolean isMoreThan500Million = BDUtil.biggerThanEqual(objx.getDbReference4(), new BigDecimal(500000000));
            final boolean lesserThan500Million = BDUtil.lesserThan(objx.getDbReference4(), new BigDecimal(500000000));
            final String stCostCenterCode = policy.getStCostCenterCode();
            
            final boolean alwaysUseJiwasraya = stCostCenterCode.equalsIgnoreCase("20") || stCostCenterCode.equalsIgnoreCase("21") ||
                                                stCostCenterCode.equalsIgnoreCase("43") || stCostCenterCode.equalsIgnoreCase("40") ||
                                                stCostCenterCode.equalsIgnoreCase("70") || stCostCenterCode.equalsIgnoreCase("10") ||
                                                stCostCenterCode.equalsIgnoreCase("13") || stCostCenterCode.equalsIgnoreCase("17") ||
                                                stCostCenterCode.equalsIgnoreCase("22");
            boolean alwaysUseJiwaSementara = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(100000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=40 &&
                                            Integer.parseInt(objx.getStReference4())<=3;
                                                /*
                                                stCostCenterCode.equalsIgnoreCase("11") || stCostCenterCode.equalsIgnoreCase("15") ||
                                                || stCostCenterCode.equalsIgnoreCase("24") ||
                                                stCostCenterCode.equalsIgnoreCase("50") ||  ;
                                                */

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                //if(isMoreThan500Million) objx.setStReference8("96");
                //else if(lesserThan500Million) objx.setStReference8("2000");

                objx.setStReference8("96");
                if(alwaysUseJiwaSementara) objx.setStReference8("2000");
                if(alwaysUseJiwasraya) objx.setStReference8("96");
                
                policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

        }
    
    private void checkingPremiBalance(InsurancePolicyObjDefaultView obj) throws Exception{
        final DTOList covers = obj.getCoverage();
        BigDecimal totalPremi = null;
        for (int i = 0; i < covers.size(); i++){
            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(i);
            
            totalPremi = BDUtil.add(totalPremi, cov.getDbPremi());   
        }
         
        
        if(!BDUtil.isEqual(totalPremi, obj.getDbReference6(),0)){
            if(covers.size()==1){
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);
            
                BigDecimal premi = obj.getDbReference6();
                obj.setDbReference6(cv.getDbPremi());
                cv.setDbPremi(premi);
                cv.setDbPremiNew(premi);

                throw new RuntimeException("Premi Debitur "+obj.getStReference1()+" Tidak sama dengan yang tertera pada premi di tab harga pertanggungan");

            }
         }
    }

    /*
    private void applyCoinsurance1April2012(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy){
        
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
                
            final String stCostCenterCode = policy.getStCostCenterCode();
            
            final boolean alwaysUseJiwasraya = stCostCenterCode.equalsIgnoreCase("11") || stCostCenterCode.equalsIgnoreCase("21") ||
                                                stCostCenterCode.equalsIgnoreCase("22") || stCostCenterCode.equalsIgnoreCase("12") ||
                                                stCostCenterCode.equalsIgnoreCase("50");
            boolean alwaysUseJiwaSementara2 = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(300000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=4;
                                               

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                if(objx.getStReference8().equalsIgnoreCase("1")){
                    objx.setStReference8("96");
                    if(alwaysUseJiwaSementara2) objx.setStReference8("2001");
                }else if(objx.getStReference8().equalsIgnoreCase("2002")){
                    objx.setStReference8("2002");
                }
                
                policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }
            
            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                objx.setStReference8("96");
                if(alwaysUseJiwaSementara2) objx.setStReference8("2001");
                
                policy.setStCoinsID(null);
            }
   }
    */

    /*
    private void applyCoinsurance1Mei2012(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
        
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());
            boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
            boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
            boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
            int UsiaPlusLama = Integer.parseInt(objx.getStReference2()) + Integer.parseInt(objx.getStReference4());
            
             
            boolean alwaysUseMegalifeBakiDebet1 = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(100000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=64 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet;
            
            boolean alwaysUseMegalifeBakiDebet2 = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(200000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=60 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet;
             
            boolean alwaysUseMegalifeBakiDebet3 = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(250000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet;
            
            boolean alwaysUseMegalifePlafonAwal = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(50000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=50 &&
                                            Integer.parseInt(objx.getStReference4())<=5 &&
                                            UsiaPlusLama <= 65 &&
                                            isPlafonAwal;
            
            final String stCostCenterCode = policy.getStCostCenterCode(); 
            final boolean alwaysUseMegalife = stCostCenterCode.equalsIgnoreCase("11") || stCostCenterCode.equalsIgnoreCase("21") ||
                                                stCostCenterCode.equalsIgnoreCase("20") || stCostCenterCode.equalsIgnoreCase("12");

            
            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                objx.setStReference8("1");
                if(isTabelUsia){
                        if(alwaysUseMegalifeBakiDebet1) objx.setStReference8("2002");
                        if(alwaysUseMegalifeBakiDebet2) objx.setStReference8("2002");
                        if(alwaysUseMegalifeBakiDebet3) objx.setStReference8("2002");
                        if(alwaysUseMegalifePlafonAwal) objx.setStReference8("2002");
                }
                
                if(policy.getStCostCenterCode().equalsIgnoreCase("22"))
                    objx.setStReference8("1");
                
                policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }
            
            if(policy.isStatusEndorse() && objx.getStReference8()==null){             
                policy.setStCoinsID(null);
            }
   }
    */
    
    public void getJenisKredit(String stVsCode) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ref2,division " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_KREASI_KREDIT' and vs_code = ?");
            
            S.setParam(1,stVsCode);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()){
                //return RS.getString(1);
                setJenisKredit(RS.getString(1));
                setJenisRate(RS.getString(2));
            }
            
            //return null;
        }finally{
            S.release();
        }
    }
    
    public String getJenisRate(String stVsCode) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "     division " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_KREASI_KREDIT' and vs_code = ?");
            
            S.setParam(1,stVsCode);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return null;
        }finally{
            S.release();
        }
    }
    
    public String getRef3(String stVsCode) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "     coalesce(ref3,'Y') " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'INSOBJ_KREASI_KREDIT' and vs_code = ?");
            
            S.setParam(1,stVsCode);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getString(1);
            
            return "Y";
        }finally{
            S.release();
        }
    }
    
    public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    private void applyCoinsurance1Oktober2013(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
            //BERLAKU PER OKTOBER 2013
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());
            boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
            boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
            boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
            int UsiaPlusLama = Integer.parseInt(objx.getStReference2()) + Integer.parseInt(objx.getStReference4());


            boolean alwaysUseMegalifeBakiDebet1 = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(100000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=10 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebet2 = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(250000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=10 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifePlafonAwal = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(50000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=5 &&
                                            UsiaPlusLama <= 65 &&
                                            isPlafonAwal && isTabelUsia;

            boolean alwaysUseJiwasrayaBakiDebet1 = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(200000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && !isTabelUsia;

            boolean alwaysUseJiwasrayaBakiDebet2 = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(250000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(500000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && !isTabelUsia;

            boolean alwaysUseJiwasrayaPlafonAwal = BDUtil.biggerThan(objx.getDbReference4(), new BigDecimal(50000000)) &&
                                            BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(500000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isPlafonAwal;

            boolean masihMasukJSBakiDebet = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(500000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && !isTabelUsia;

            boolean masihMasukJSPlafonAwal = BDUtil.lesserThanEqual(objx.getDbReference4(), new BigDecimal(500000000)) &&
                                            Integer.parseInt(objx.getStReference2())<=55 &&
                                            Integer.parseInt(objx.getStReference4())<=15 &&
                                            UsiaPlusLama <= 65 &&
                                            isPlafonAwal;

            final String stCostCenterCode = policy.getStCostCenterCode();
            boolean alwaysUseOR = stCostCenterCode.equalsIgnoreCase("22");

            if(Integer.parseInt(objx.getStReference2()) > 70)
                alwaysUseOR = true;

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifeBakiDebet1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebet2) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwal) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebet1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebet2) objx.setStReference8("96");

                    //CEK JIKA TIDAK MASUK MEGALIFE & JS APAKAH MASIH BISA MASUK KE JS
                    if(objx.getStReference8().equalsIgnoreCase("1")){
                          if(masihMasukJSBakiDebet) objx.setStReference8("96");
                    }

                    if(alwaysUseOR) objx.setStReference8("1");

                    policy.setStCoinsID(null);

            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                policy.setStCoinsID(null);
            }

   }
    */

    public String generateNoRekap(String entityID, InsurancePolicyView pol) throws Exception{
        ARInvoiceView invoice = new ARInvoiceView();

        return invoice.generateNoSuratHutangCoas(entityID,pol);
    }

    public BigDecimal getUsiaLimit(String cat, String poltypeid, String userID) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "      max(c.refn4) " +
                    "   from " +
                    "         s_user_roles b " +
                    "         inner join ff_table c on c.fft_group_id='CAPA' and c.ref1=b.role_id and c.ref2=? and c.ref3=?" +
                    "   where" +
                    " c.active_flag = 'Y' "+
                    " and b.user_id=? and ref4 is null");

            S.setParam(1,cat);
            S.setParam(2,poltypeid);
            S.setParam(3,userID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) return RS.getBigDecimal(1);

            return null;
        } finally {

            S.release();
        }
    }

    public void applyRateProRataNew(String lama, String jenis_rate, String jenis_kredit, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{
        // Premi Sekaligus Usia 20 tahun  masa asuransi 2 tahun 3 bulan = 0.97 + (3/12) x ( 1.45 - 0.97 )
        BigDecimal rateKoas1 = BDUtil.zero;
        BigDecimal komisiKoas1 = BDUtil.zero;
        BigDecimal rateKoas2 = BDUtil.zero;
        BigDecimal komisiKoas2 = BDUtil.zero;

        if(Integer.parseInt(lama)==0) lama = String.valueOf(1);

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
        int lamaPlus1 = Integer.parseInt(lama) + 1;


        //get rate 1
        final SQLUtil S = new SQLUtil();
            try {

                //logger.logWarning("############# objek : "+ obj.getStReference1());
                //logger.logWarning("############# lama : "+ lama);
                String sql =  "select rate"+ lama +", gross_up, rate" + String.valueOf(lamaPlus1) +
                        " from ins_rates_big where ref4 = ? and ref5 = ? and (ref3 = ? or ref3 is null) and refid1 = ?"+
                        " and period_start <= ? and period_end >= ?"+
                        " and rate_class = 'PAKREASI_NEW' order by ref3 limit 1";

                final PreparedStatement PS = S.setQuery(sql);

                int n=1;

                PS.setString(n++, jenis_rate); //RATA/TABEL
                PS.setString(n++, jenis_kredit); //BAKI/PLAFON

                PS.setString(n++, String.valueOf(usiaTerdekat)); //ref3

                if(obj.getStReference8()!=null) PS.setString(n++,obj.getStReference8()); //ref1
                else PS.setString(n++,policy.getStCoinsID()); //ref1

                S.setParam(n++,policy.getDtPolicyDate()); //period start
                S.setParam(n++,policy.getDtPolicyDate()); //period end

                final ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    rateKoas1 = RS.getBigDecimal(1);
                    komisiKoas1 = RS.getBigDecimal(2);
                    rateKoas2 = RS.getBigDecimal(3);
                    komisiKoas2 = RS.getBigDecimal(2);
                }else{
                    rateKoas1 = BDUtil.zero;
                    komisiKoas1 = BDUtil.zero;
                    rateKoas2 = BDUtil.zero;
                    komisiKoas2 = BDUtil.zero;
                }

            } finally {
                S.release();
            }
     
          //calculate rate
          // Premi Sekaligus Usia 20 tahun  masa asuransi 2 tahun 3 bulan = 0.97 + (3/12) x ( 1.45 - 0.97 )

          int KelebihanBulan = 0;
          
          //if(Integer.parseInt(obj.getStReference6()) > 12){
              int kelebihanHari = DateUtil.getKelebihanHari(obj.getDtReference2(), obj.getDtReference3());
              int maxKelebihan = 1;
              int lamaBulan = Integer.parseInt(obj.getStReference6());

              if(obj.getStReference8().equalsIgnoreCase("2002")){
                  if(kelebihanHari >= maxKelebihan)
                        lamaBulan = lamaBulan + 1;
              }
              KelebihanBulan = lamaBulan % 12;  
          //}

           BigDecimal a =  rateKoas1;
           BigDecimal b =  rateKoas2; 
           BigDecimal lebihBulan = new BigDecimal(KelebihanBulan);
           BigDecimal Bulan = new BigDecimal(12);

           if(Integer.parseInt(obj.getStReference6()) < 12){
               lebihBulan = new BigDecimal(obj.getStReference6());
               if(obj.getStReference8().equalsIgnoreCase("2002")){
                  if(kelebihanHari >= maxKelebihan)
                        lebihBulan = BDUtil.add(lebihBulan, BigDecimal.ONE);
               }
           }

           BigDecimal one = BDUtil.div(lebihBulan, new BigDecimal(12),6);
           BigDecimal two = BDUtil.mulRound(one, (BDUtil.sub(b, a)),2);
           BigDecimal rateProRataFinal2 = BDUtil.add(a, two);

           if(Integer.parseInt(obj.getStReference6()) < 12){
                 rateProRataFinal2 = BDUtil.mulRound(one, a,2);
           }
           
           rateProRataFinal2 = rateProRataFinal2.setScale(2, BigDecimal.ROUND_HALF_UP);

           boolean manualRIRate = Tools.isYes(obj.getStReference10());
           boolean manualRIComRate = Tools.isYes(obj.getStReference11());

           if (!manualRIRate) obj.setDbReference1(rateProRataFinal2);
           if (!manualRIComRate) obj.setDbReference7(komisiKoas1);

    }

    public void applyRateProRata(String lama, String jenis_rate, String jenis_kredit, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception
    {
        BigDecimal rateKoas1 = BDUtil.zero;
        BigDecimal komisiKoas1 = BDUtil.zero;
        BigDecimal rateKoas2 = BDUtil.zero;
        BigDecimal komisiKoas2 = BDUtil.zero;

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
        int lamaPlus1 = Integer.parseInt(lama) + 1;

        SQLUtil S = new SQLUtil();
        try {
            String sql = "select rate" + lama + ", gross_up, rate" + String.valueOf(lamaPlus1) + " from ins_rates_big where ref4 = ? and ref5 = ? and (ref3 = ? or ref3 is null) and refid1 = ?" + " and period_start <= ? and period_end >= ?" + " and rate_class = 'PAKREASI_NEW'";

            PreparedStatement PS = S.setQuery(sql);

            int n = 1;

            PS.setString(n++, jenis_rate);
            PS.setString(n++, jenis_kredit);
            PS.setString(n++, String.valueOf(usiaTerdekat));

            if (obj.getStReference8() != null) {
                PS.setString(n++, obj.getStReference8());
            } else {
                PS.setString(n++, policy.getStCoinsID());
            }
            S.setParam(n++, policy.getDtPolicyDate());
            S.setParam(n++, policy.getDtPolicyDate());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                rateKoas1 = RS.getBigDecimal(1);
                komisiKoas1 = RS.getBigDecimal(2);
                rateKoas2 = RS.getBigDecimal(3);
                komisiKoas2 = RS.getBigDecimal(2);
            } else {
                rateKoas1 = BDUtil.zero;
                komisiKoas1 = BDUtil.zero;
                rateKoas2 = BDUtil.zero;
                komisiKoas2 = BDUtil.zero;
            }
        } finally {
            S.release();
        }

        int KelebihanBulan = 0;

        if (Integer.parseInt(obj.getStReference6()) > 12) {
            int kelebihanHari = DateUtil.getKelebihanHari(obj.getDtReference2(), obj.getDtReference3());
            int maxKelebihan = 12;
            int lamaBulan = Integer.parseInt(obj.getStReference6());

            if ((obj.getStReference8().equalsIgnoreCase("2002")) && (kelebihanHari > maxKelebihan)) {
                lamaBulan = lamaBulan + 1;
            }
            KelebihanBulan = lamaBulan % 12;
        }

        BigDecimal a = rateKoas1;
        BigDecimal b = rateKoas2;
        BigDecimal lebihBulan = new BigDecimal(KelebihanBulan);
        BigDecimal Bulan = new BigDecimal(12);

        BigDecimal one = BDUtil.div(lebihBulan, new BigDecimal(12), 6);
        BigDecimal two = BDUtil.mul(one, BDUtil.sub(b, a));
        BigDecimal rateProRataFinal2 = BDUtil.add(a, two);
        rateProRataFinal2 = rateProRataFinal2.setScale(2, BigDecimal.ROUND_HALF_UP);

        boolean manualRIRate = Tools.isYes(obj.getStReference10());
        boolean manualRIComRate = Tools.isYes(obj.getStReference11());

        if (!manualRIRate) {
            obj.setDbReference1(rateProRataFinal2);
        }
        if (!manualRIComRate) {
            obj.setDbReference7(komisiKoas1);
        }
    }

    /*
    private void applyCoinsurance1April2014(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
            //BERLAKU PER APRIL 2014
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());
            boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
            boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
            boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
            boolean isRataUsia = jenis_rate.equalsIgnoreCase("RATA");
            
            BigDecimal tsi = objx.getDbReference4();
            //int usia = Integer.parseInt(objx.getStReference2());

            int usia = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
            int lama = Integer.parseInt(objx.getStReference4());
            int UsiaPlusLama = usia + lama;

            boolean entityBisaJS = false;

            if(policy.getEntity().isBPD() || policy.getEntity().isBankUmum()) entityBisaJS = true;

            int jumlahHariDariPengajuan = DateUtil.getDaysAmount(obj.getDtReference2(), policy.getDtPolicyDate());

            boolean alwaysUseMegalifePlafonAwalTabelUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 60 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isTabelUsia;

            boolean alwaysUseMegalifePlafonAwalRataUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 55 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isRataUsia;


            boolean alwaysUseMegalifeBakiDebetTabelUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 69 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(250000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(500000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            String stCostCenterCode = policy.getStCostCenterCode();
            boolean alwaysUseOR = stCostCenterCode.equalsIgnoreCase("22") || stCostCenterCode.equalsIgnoreCase("11");
            //boolean alwaysUseOR = stCostCenterCode.equalsIgnoreCase("22");

            if(Integer.parseInt(objx.getStReference2()) > 70)
                alwaysUseOR = true;

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");

                    policy.setStCoinsID(null);

            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                if(policy.getStCoinsID()==null){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");
                }

                policy.setStCoinsID(null);
            }
   }
    */

    private void applyCoinsurance26November2014(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
            //BERLAKU PER APRIL 2014
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            getJenisKredit(policy.getStKreasiTypeID());

//            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
//            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());
            String jenis_kredit = getJenisKredit();
            String jenis_rate = getJenisRate();
            boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
            boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
            boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
            boolean isRataUsia = jenis_rate.equalsIgnoreCase("RATA");

            BigDecimal tsi = objx.getDbReference4();
            //int usia = Integer.parseInt(objx.getStReference2());

            int usia = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
            int lama = Integer.parseInt(objx.getStReference4());
            int UsiaPlusLama = usia + lama;

            boolean entityBisaJS = false;

            if(policy.getEntity().isBPD() || policy.getEntity().isBankUmum()) entityBisaJS = true;

            int jumlahHariDariPengajuan = DateUtil.getDaysAmount(obj.getDtReference2(), policy.getDtPolicyDate());

            boolean alwaysUseMegalifePlafonAwalTabelUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 60 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isTabelUsia;

            boolean alwaysUseMegalifePlafonAwalRataUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 55 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isRataUsia;


            boolean alwaysUseMegalifeBakiDebetTabelUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 69 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(250000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(500000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            String stCostCenterCode = policy.getStCostCenterCode();
            boolean alwaysUseOR = stCostCenterCode.equalsIgnoreCase("22");

            if(Integer.parseInt(objx.getStReference2()) > 70)
                alwaysUseOR = true;

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");

                    policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                if(policy.getStCoinsID()==null){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");
                }

                policy.setStCoinsID(null);
            }

   }

     private void applyCoinsurance19Juni2015(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
            //BERLAKU PER APRIL 2014
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            getJenisKredit(policy.getStKreasiTypeID());

//            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
//            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());
            String jenis_kredit = getJenisKredit();
            String jenis_rate = getJenisRate();
            boolean isBakiDebet = jenis_kredit.equalsIgnoreCase("BAKIDEBET");
            boolean isPlafonAwal = jenis_kredit.equalsIgnoreCase("PLAFONAWAL");
            boolean isTabelUsia = jenis_rate.equalsIgnoreCase("TABEL");
            boolean isRataUsia = jenis_rate.equalsIgnoreCase("RATA");

            BigDecimal tsi = objx.getDbReference4();
            //int usia = Integer.parseInt(objx.getStReference2());

            int usia = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
            int lama = Integer.parseInt(objx.getStReference4());
            int UsiaPlusLama = usia + lama;

            boolean entityBisaJS = false;

            if(policy.getEntity().isBPD() || policy.getEntity().isBankUmum()) entityBisaJS = true;

            int jumlahHariDariPengajuan = DateUtil.getDaysAmount(obj.getDtReference2(), policy.getDtPolicyDate());

            boolean alwaysUseMegalifePlafonAwalTabelUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 60 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isTabelUsia;

            boolean alwaysUseMegalifePlafonAwalRataUsia = BDUtil.lesserThanEqual(tsi, new BigDecimal(50000000)) &&
                                            usia <= 55 &&
                                            lama <= 5 &&
                                            UsiaPlusLama <= 60 &&
                                            isPlafonAwal && isRataUsia;


            boolean alwaysUseMegalifeBakiDebetTabelUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetTabelUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isTabelUsia;

            boolean alwaysUseMegalifeBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 64 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 60 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseMegalifeBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(200000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(250000000)) &&
                                            usia <= 55 &&
                                            lama <= 15 &&
                                            UsiaPlusLama <= 65 &&
                                            isBakiDebet && isRataUsia && lama < 1;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia1 = BDUtil.lesserThanEqual(tsi, new BigDecimal(100000000)) &&
                                            usia <= 69 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia2 = BDUtil.biggerThan(tsi, new BigDecimal(100000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(200000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            boolean alwaysUseJiwasrayaBakiDebetRataUsia3 = BDUtil.biggerThan(tsi, new BigDecimal(250000000)) &&
                                            BDUtil.lesserThanEqual(tsi, new BigDecimal(500000000)) &&
                                            usia <= 55 &&
                                            lama <= 25 &&
                                            UsiaPlusLama <= 70 &&
                                            isBakiDebet && isRataUsia && lama >= 1 && entityBisaJS &&
                                            jumlahHariDariPengajuan <= 80;

            String stCostCenterCode = policy.getStCostCenterCode();
            boolean alwaysUseOR = stCostCenterCode.equalsIgnoreCase("22");

            if(stCostCenterCode.equalsIgnoreCase("11"))
                alwaysUseOR = true;

            if(Integer.parseInt(objx.getStReference2()) > 70)
                alwaysUseOR = true;

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");

                    policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                if(policy.getStCoinsID()==null){
                    objx.setStReference8("1");

                    if(alwaysUseMegalifePlafonAwalTabelUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifePlafonAwalRataUsia) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetTabelUsia3) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia1) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia2) objx.setStReference8("2002");
                    if(alwaysUseMegalifeBakiDebetRataUsia3) objx.setStReference8("2002");

                    if(alwaysUseJiwasrayaBakiDebetRataUsia1) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia2) objx.setStReference8("96");
                    if(alwaysUseJiwasrayaBakiDebetRataUsia3) objx.setStReference8("96");

                    if(alwaysUseOR) objx.setStReference8("1");
                }

                policy.setStCoinsID(null);
            }

   }


     private void applyCoinsurance1Des2016(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy)throws Exception
    {
            //BERLAKU PER DESEMBER 2016
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            boolean alwaysUseOR = true;

            if(!policy.isStatusHistory()&&!policy.isStatusEndorse()&&!policy.isStatusClaim()&&!policy.isStatusClaimEndorse()&&!policy.isStatusEndorseRI()){

                    if(alwaysUseOR) objx.setStReference8("1");

                    policy.setStCoinsID(null);
            }

            if(policy.isStatusHistory() || policy.isStatusEndorse()){
                if(policy.getStCoinsID()!=null)
                    if(objx.getStReference8()==null) objx.setStReference8(policy.getStCoinsID());
            }

            if(policy.isStatusEndorse() && objx.getStReference8()==null){
                if(policy.getStCoinsID()==null){
                    objx.setStReference8("1");

                    if(alwaysUseOR) objx.setStReference8("1");
                }

                policy.setStCoinsID(null);
            }

   }


    public void calculateProsesReas(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
        try{

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            final DTOList covers = obj.getCoverage();
            final Date perStart = obj.getDtReference2();
            final Date perEnd = obj.getDtReference3();
            int scale = 0;

            if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            policy.setStLockCoinsFlag("Y");

            boolean useRate = Tools.isYes(obj.getStReference9());
            boolean manualRIRate = Tools.isYes(obj.getStReference10());
            boolean manualPremiKoas = Tools.isYes(obj.getStReference14());
            boolean manualRIComRate = Tools.isYes(obj.getStReference11());
            final Date birth = obj.getDtReference1();// birth date
            boolean manualReinsurer = Tools.isYes(obj.getStReference19());

            if(!policy.isStatusEndorse()){
                if(validate){
                    if (birth!=null) {

                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));

                    }
                }
            }

            if(policy.isStatusEndorse()){
                if(obj.getStReference2()==null){
                    if (birth!=null) {
                        //long age = System.currentTimeMillis()-(birth.getTime());
                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));
                    }
                }
            }

            if(Integer.parseInt(obj.getStReference2()) < 17)
                throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

            //if(Integer.parseInt(obj.getStReference2()) > 75)
                //throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Melebihi 75 Tahun");

            boolean validPeriod = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()))<=0;

            if (!validPeriod) throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah Debitur "+obj.getStReference1());

            int tgl = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()));

            if(tgl == 0) throw new RuntimeException("Tanggal Mulai dan Akhir Kredit Debitur "+obj.getStReference1()+" Tidak Boleh Sama");

            DateTime newDate = new DateTime(new Date());
            DateTime errorDate2 = newDate.plusYears(1);

            boolean validPeriod3 = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(errorDate2.toDate()))<=0;

            obj.setStReference4(null);

            if (perStart!=null && perEnd!=null) {

                long age = perEnd.getTime()-perStart.getTime();
                //long agemonth = age / (1000l*60l*60l*24l*30l);
                long ageyear = age / (1000l*60l*60l*24l*365l);

                obj.setStReference4(String.valueOf(ageyear));

                DateTime birthDate = new DateTime(birth);
                DateTime startDate = new DateTime(perStart);
                DateTime endDate = new DateTime(perEnd);
                Months m = Months.monthsBetween(startDate, endDate);
                Years y = Years.yearsBetween(startDate, endDate);
                int mon = m.getMonths();
                int year = y.getYears();
                obj.setStReference6(String.valueOf(mon));

                DateTime polDate = new DateTime(policy.getDtPolicyDate());

//                if(endDate.isBefore(startDate))
//                    throw new RuntimeException("Tanggal akhir kredit Debitur "+obj.getStReference1()+" tidak boleh kurang dari tanggal mulai");
//
//                if(startDate.isEqual(birthDate))
//                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal lahir");
//
//                if(startDate.isEqual(endDate))
//                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal akhir");

                if(!policy.isStatusHistory() && !policy.isStatusEndorse()){
//                    if(year>40)
//                        throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah, Debitur "+obj.getStReference1());
//
//                    if(Integer.parseInt(obj.getStReference2())>100)
//                        throw new RuntimeException("Usia Debitur "+obj.getStReference1() + " Salah");
//
//                    if(startDate.getYear() > polDate.getYear())
//                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());
//
//                    if((polDate.getYear() - startDate.getYear()) > 10)
//                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());
//
//                    DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();
//
//                    if(startDate.isAfter(policyDateLastDay))
//                        throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh > tanggal polis");
                }

            }

            // MARK FOR PROSES
            if(!manualReinsurer)
                applyCoinsurance26November2014(obj, policy);

            if (obj.getDbReference4()!=null){
                DTOList tsiList = obj.getSuminsureds();
                if (tsiList!=null && tsiList.size()==1) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(0);

                    tsi.setDbInsuredAmount(obj.getDbReference4());
                }
            }

            //BigDecimal jualRate=null;
            //BigDecimal grossUp2=null;
            //BigDecimal premiRate=null;
            boolean isOR = false;
            if(obj.getStReference8()!=null)
                if(obj.getStReference8().equalsIgnoreCase("1")) isOR = true;

            boolean getRate = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()) || (policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference1()) && !isOR);
            //boolean getRate = true;

//            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
//            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if(getRate){
                        getJenisKredit(policy.getStKreasiTypeID());
                        applyRateProRataNew(obj.getStReference4(), getJenisRate(), getJenisKredit(), obj, policy);
                }
            }

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                    /*if(getRate){
                        final SQLUtil S = new SQLUtil();
                        try {

                            String sql =  "select rate"+ obj.getStReference4() +", gross_up" +
                                    " from ins_rates_big where ref4 = ? and ref5 = ? and (ref3 = ? or ref3 is null) and refid1 = ?"+
                                    " and period_start<=? and period_end>=?"+
                                    " and rate_class='PAKREASI_NEW'";

                            final PreparedStatement PS = S.setQuery(sql);

                            int n=1;

                            PS.setString(n++, jenis_rate); //RATA/TABEL
                            PS.setString(n++, jenis_kredit); //BAKI/PLAFON

                            //PS.setString(n++,policy.getStCostCenterCode()); //ref2

                            PS.setString(n++,obj.getStReference2()); //ref3
                            //PS.setString(n++,obj.getStReference22()); //ref3

                            if(obj.getStReference8()!=null) PS.setString(n++,obj.getStReference8()); //ref1
                            else PS.setString(n++,policy.getStCoinsID()); //ref1

                            S.setParam(n++,policy.getDtPolicyDate()); //period start
                            S.setParam(n++,policy.getDtPolicyDate()); //period end

                            final ResultSet RS = PS.executeQuery();

                            if (RS.next()){
                                //premiRate = RS.getBigDecimal(1);
                                //grossUp2  = RS.getBigDecimal(2);
                                if (!manualRIRate) obj.setDbReference1(RS.getBigDecimal(1));
                                if (!manualRIComRate) obj.setDbReference7(RS.getBigDecimal(2));
                            }else{
                                if (!manualRIRate) obj.setDbReference1(BDUtil.zero);
                                if (!manualRIComRate) obj.setDbReference7(BDUtil.zero);
                            }

                        } finally {
                            S.release();
                        }
                    }
                    */

                //}
                //end

                BigDecimal totalRate = new BigDecimal(0);
                BigDecimal totalPremi = new BigDecimal(0);

                //calculate premi 100%
                if (covers!=null && covers.size()>1) {
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        cv.setStEntryRateFlag(obj.getStReference9());

                        if(Tools.isYes(cv.getStAutoRateFlag()))
                            cv.setStAutoRateFlag(null);

                        totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                        totalRate = BDUtil.add(totalRate,cv.getDbRate());

                        if(BDUtil.isZeroOrNull(cv.getDbRate())){
                            cv.setDbRate(BDUtil.getMileFromRate(BDUtil.div(cv.getDbPremiNew(),cv.getDbInsuredAmount(),5)));
                        }
                    }

                    obj.setDbReference5(totalRate);
                    obj.setDbReference6(totalPremi);

                    if(!policy.isStatusHistory()){
                        if(!useRate) obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                    }
               }else if (covers!=null && covers.size()==1) {
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                    cv.setStEntryRateFlag(obj.getStReference9());

                    if(Tools.isYes(cv.getStAutoRateFlag()))
                        cv.setStAutoRateFlag(null);

                    if (useRate) {
                        cv.setDbRate(obj.getDbReference5());
                        if(!policy.isStatusHistory()){
                            obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                        if(policy.isStatusHistory() && BDUtil.isZeroOrNull(obj.getDbReference6())){
                             obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                    }else{
                        cv.setDbPremiNew(obj.getDbReference6());
                        cv.setDbPremi(obj.getDbReference6());
                        //BigDecimal sukuPremi = BDUtil.getPctFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4()));
                        obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                        cv.setDbRate(obj.getDbReference5());
                    }

                    if(policy.isStatusEndorse()){
                        obj.setDbReference5(cv.getDbRate());
                        obj.setDbReference6(cv.getDbPremi());
                    }
                }
                //end calculate premi 100%
            }

            objx.reCalculate();

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {

                if (!manualRIRate){
                    //BigDecimal rateGross = BDUtil.div(premiRate,BDUtil.sub(new BigDecimal(100),grossUp2),5);
                    //obj.setDbReference1(premiRate);
                    /*if(BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(premiRate);
                    else if(!BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(BDUtil.getPctFromRate(rateGross));
                    */
                    //BLOCK DATA BANDUNG USIA > 70
                    //if(policy.getStCostCenterCode().equalsIgnoreCase("21"))
                    if(Integer.parseInt(obj.getStReference2())>70)
                        obj.setDbReference1(BDUtil.zero);
                }

                //start endorse calc
                BigDecimal premiAmountDiff = null;
                if(policy.isStatusEndorse()){
                    BigDecimal insAmountDiff = null;
                    BigDecimal totalInsAmountDiff = null;
                    for (int l = 0; l < covers.size(); l++) {
                        InsurancePolicyCoverView icv = (InsurancePolicyCoverView) covers.get(l);

                        final InsurancePolicyCoverView refCover = icv.getRefCover();

                        if(refCover==null){
                            //continue;
                            insAmountDiff = obj.getDbReference4();
                        }else{
                            insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                            premiAmountDiff = BDUtil.sub(icv.getDbPremi(),refCover.getDbPremi());
                            totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                        }
                    }
                    obj.setDbObjectInsuredAmount(insAmountDiff);
                    obj.setDbReference11(insAmountDiff);
                }

                //end endrose

                if (manualPremiKoas){
                    obj.setDbReference2(obj.getDbReference2());
                    if(!policy.isStatusEndorse()){
                        obj.setDbReference1(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference2(),obj.getDbReference4(),5)));
                    }
                }else{

                    obj.setDbReference2(
                            BDUtil.mul(BDUtil.getRateFromMile(obj.getDbReference1()), obj.getDbObjectInsuredAmount(),scale)
                            );

                    obj.setDbReference17(obj.getDbReference2());

                    if(!BDUtil.isZeroOrNull(obj.getDbReference7())){
                        obj.setDbReference2(BDUtil.divRoundUp(obj.getDbReference2(),BDUtil.getRateFromPct(BDUtil.sub(new BigDecimal(100),obj.getDbReference7())),scale));
                    }

                     //MARK FOR PROSES
                    if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse() && !policy.isStatusEndorse()){
                        if(getRate){
                            if(BDUtil.biggerThan(obj.getDbReference2(), obj.getDbReference6())){
                                obj.setStReference8("1");
                                obj.setDbReference1(BDUtil.zero);
                                obj.setDbReference2(BDUtil.zero);
                                obj.setDbReference7(BDUtil.zero);
                            }
                        }
                    }

                    if(policy.isStatusEndorse() && !BDUtil.isZeroOrNull(premiAmountDiff) && BDUtil.isZeroOrNull(obj.getDbReference2())){
                        //BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                        BigDecimal tsi = obj.getDbReference4()!=null?obj.getDbReference4():obj.getDbObjectInsuredAmount();
                        BigDecimal premiKoas = null;
                        BigDecimal premiKoasParent = BDUtil.mul(tsi,BDUtil.getRateFromMile(obj.getDbReference1()));
                        premiKoas = BDUtil.mul(BDUtil.div(obj.getDbReference6(), obj.getDbReference12()),premiKoasParent);
                        premiKoas = BDUtil.roundUp(premiKoas);

                        obj.setDbReference2(premiKoas);
                    }
                    /*
                    if(policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference2()) && !BDUtil.isZeroOrNull(obj.getDbReference6())){
                        obj.setDbReference2(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference1()), scale));
                    }*/
                }
            }

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if (!manualRIComRate)
                    obj.setDbReference7(obj.getDbReference7());

                obj.setDbReference9(
                        BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference7()), obj.getDbReference2(),scale)
                        );
            }

            if(policy.isStatusEndorse() && obj.getDtReference5()!=null) {
                recalculateRestitusi(policy, obj, obj.getDbReference1(), obj.getDbReference7());
            }

            DTOList coinsMurni = policy.getCoins2();
            BigDecimal askridaSharePct = null;
            BigDecimal askridaShareTSI = null;
            BigDecimal askridaSharePremi = null;

            for(int i = 0 ; i< coinsMurni.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni.get(i);

                if(!coins3.isHoldingCompany()) continue;

                if(coins3.isHoldingCompany()){
                    askridaSharePct = coins3.getDbSharePct();
                    askridaShareTSI = coins3.getDbAmount();
                    askridaSharePremi = coins3.getDbPremiAmount();
                }
            }

            DTOList coins = policy.getCoinsCoverage();
            if (coins.size()>1) {
                while (coins.size()>1) coins.delete(coins.size()-1);
            }

            if (coins.size()==0) throw new RuntimeException("Unknown coinsurance model ... (size==0)");

            //here
            final DTOList oj = policy.getObjects();
            String [] coax = new String[5];
            int counter = 0;
            BigDecimal totalPremiCo94 = null;
            BigDecimal totalKomisiCo94 = null;
            BigDecimal totalPremiCo96= null;
            BigDecimal totalKomisiCo96 = null;
            BigDecimal totalPremiCo2000 = null;
            BigDecimal totalKomisiCo2000 = null;
            BigDecimal totalPremiCo2001 = null;
            BigDecimal totalKomisiCo2001 = null;
            BigDecimal totalPremiCo2002 = null;
            BigDecimal totalKomisiCo2002 = null;

            for (int i = 0; i < oj.size(); i++)
            {
                InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);

                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;

                if(obje.getStReference8()!=null){
                    if(obje.getStReference8().equalsIgnoreCase("94")){
                        totalPremiCo94 = BDUtil.add(totalPremiCo94, obje.getDbReference2());
                        totalKomisiCo94 = BDUtil.add(totalKomisiCo94, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("96")){
                        totalPremiCo96 = BDUtil.add(totalPremiCo96, obje.getDbReference2());
                        totalKomisiCo96 = BDUtil.add(totalKomisiCo96, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2000")){
                        totalPremiCo2000 = BDUtil.add(totalPremiCo2000, obje.getDbReference2());
                        totalKomisiCo2000 = BDUtil.add(totalKomisiCo2000, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2001")){
                        totalPremiCo2001 = BDUtil.add(totalPremiCo2001, obje.getDbReference2());
                        totalKomisiCo2001 = BDUtil.add(totalKomisiCo2001, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2002")){
                        totalPremiCo2002 = BDUtil.add(totalPremiCo2002, obje.getDbReference2());
                        totalKomisiCo2002 = BDUtil.add(totalKomisiCo2002, obje.getDbReference9());
                    }
                }

                if(obje.getStReference8()!=null){
                    if(i!=0){
                        if(coax[0]!=null)
                            if(coax[0].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[1]!=null)
                            if(coax[1].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[2]!=null)
                            if(coax[2].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[3]!=null)
                            if(coax[3].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[4]!=null)
                            if(coax[4].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                    }

                    if(obje.getStReference8().equalsIgnoreCase("1")) continue;
                    coax [counter] = obje.getStReference8();
                    counter++;
                }

            }

            if(policy.isStatusClaim() || policy.isStatusClaimEndorse()){
                counter = 0;
                coax = new String[5];
                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) policy.getClaimObject();
                if(obje.getStReference8()!=null)
                    coax[0] = obje.getStReference8();

                if(obje.getStReference8()==null) coax[0] = policy.getStCoinsID();

                counter++;
            }

            for (int i = 0; i < counter; i++){
                final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

                co.markNew();
                co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);
                co.setStAutoPremiFlag("N");
                co.setStFlagEntryByRate("Y");
                co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);
                co.setStEntityID(coax[i]);
                coins.add(co);
            }


             BigDecimal totPremi = BDUtil.zero;
             BigDecimal totPremiCoas = BDUtil.zero;
            //BACKUP COAS
               if (coins.size()>0){
                  for(int i = 0 ; i< coins.size();i++){
                        InsurancePolicyCoinsView coins2 = (InsurancePolicyCoinsView) coins.get(i);

                        DTOList objects = policy.getObjects();
                        totPremi = (BigDecimal) objects.getTotal("refn6");
                        BigDecimal totRIPremi = (BigDecimal) objects.getTotal("refn2");
                        //BigDecimal totRIPremiBAJ = (BigDecimal) objects.getTotal("refn13");
                        //BigDecimal totRIPremiJS = (BigDecimal) objects.getTotal("refn14");
                        //BigDecimal totCommissionBAJ = (BigDecimal) objects.getTotal("refn15");
                        //BigDecimal totCommissionJS = (BigDecimal) objects.getTotal("refn16");
                        BigDecimal total = BDUtil.add(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))), BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct))));

                        if(coins2.isHoldingCompany()){
                            coins2.setDbSharePct(BDUtil.hundred);
                            coins2.setDbAmount(askridaShareTSI);
                            coins2.setStEntityID("1");
                            coins2.setStAutoPremiFlag("N");
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))));
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                            //if(!BDUtil.isEqual(askridaSharePremi, total, 0))
                                //coins2.setDbPremiAmount(BDUtil.sub(askridaSharePremi, BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct)))));
                        }else if(coins2.getStEntityID().equalsIgnoreCase("94")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo94);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo94, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("96")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo96);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo96, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2000")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2000);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2000, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2001")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2001);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2001, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2002")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2002);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2002, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }
                   }
               }

            DTOList coinsMurni2 = policy.getCoins2();

            for(int i = 0 ; i< coinsMurni2.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni2.get(i);

                if(coins3.isHoldingCompany() && !coins3.isManualPremi()) coins3.setDbPremiAmount(BDUtil.roundUp(totPremiCoas));

                if(!coins3.isHoldingCompany() && !coins3.isManualPremi()){
                    coins3.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(coins3.getDbSharePct()), totPremi));
                    coins3.setDbPremiAmount(BDUtil.roundUp(coins3.getDbPremiAmount()));
                }

                if(coins3.isManualPremi())
                    coins3.setDbPremiAmount(coins3.getDbPremiAmount());
            }

            if(!policy.isStatusEndorse()){
                obj.setDbReference12(obj.getDbReference6());
            }

            //VALIDATING APPROVAL
            if(policy.getStEffectiveFlag()!=null){
                //if(policy.getStEffectiveFlag().equalsIgnoreCase("Y"))
                     //validateApproval(policy, obj);
            }

            boolean manualNoRekap = Tools.isYes(obj.getStReference23());

            if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal()){

                if(!manualNoRekap){
                    obj.setStRekapKreasi(generateNoRekap(obj.getStReference8(), policy));
                }

                obj.setStRekapKreasi(obj.getStRekapKreasi().toUpperCase());

            }

            //checkingPremiBalance(obj);
            //PERHITUNGAN RESTITUSI PREMI
            /*if(policy.isStatusEndorse() && isRestitusi) {
                recalculateRestitusiNew(policy, obj, premiRate, grossUp2, coins, askridaSharePct);
            }*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void calculatePenerapanRateJual(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
        try{

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;
            final DTOList covers = obj.getCoverage();
            final Date perStart = obj.getDtReference2();
            final Date perEnd = obj.getDtReference3();
            int scale = 0;

            if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
            policy.setStLockCoinsFlag("Y");

            boolean useRate = Tools.isYes(obj.getStReference9());
            boolean manualRIRate = Tools.isYes(obj.getStReference10());
            boolean manualPremiKoas = Tools.isYes(obj.getStReference14());
            boolean manualRIComRate = Tools.isYes(obj.getStReference11());
            final Date birth = obj.getDtReference1();// birth date
            boolean manualReinsurer = Tools.isYes(obj.getStReference19());

            if(!policy.isStatusEndorse()){
                if(validate){
                    if (birth!=null) {

                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));

                    }
                }
            }

            if(policy.isStatusEndorse()){
                if(obj.getStReference2()==null){
                    if (birth!=null) {
                        //long age = System.currentTimeMillis()-(birth.getTime());
                        long age = perStart.getTime()-(birth.getTime());

                        age = age / (1000l*60l*60l*24l*365l);

                        if(obj.getStReference2()==null)
                            obj.setStReference2(String.valueOf(age));

                        obj.setStReference22(String.valueOf(DateUtil.getUsiaUltahTerdekat(birth, perStart,1)));
                    }
                }
            }

            if(Integer.parseInt(obj.getStReference2()) < 17)
                throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

            boolean validPeriod = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()))<=0;

            if (!validPeriod) throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah Debitur "+obj.getStReference1());

            int tgl = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(obj.getDtReference3()));

            if(tgl == 0) throw new RuntimeException("Tanggal Mulai dan Akhir Kredit Debitur "+obj.getStReference1()+" Tidak Boleh Sama");

            DateTime newDate = new DateTime(new Date());
            DateTime errorDate2 = newDate.plusYears(1);

            boolean validPeriod3 = Tools.compare(DateUtil.truncDate(obj.getDtReference2()), DateUtil.truncDate(errorDate2.toDate()))<=0;

            obj.setStReference4(null);

            if (perStart!=null && perEnd!=null) {

                long age = perEnd.getTime()-perStart.getTime();
                //long agemonth = age / (1000l*60l*60l*24l*30l);
                long ageyear = age / (1000l*60l*60l*24l*365l);

                obj.setStReference4(String.valueOf(ageyear));

                DateTime birthDate = new DateTime(birth);
                DateTime startDate = new DateTime(perStart);
                DateTime endDate = new DateTime(perEnd);
                Months m = Months.monthsBetween(startDate, endDate);
                Years y = Years.yearsBetween(startDate, endDate);
                int mon = m.getMonths();
                int year = y.getYears();
                obj.setStReference6(String.valueOf(mon));

                DateTime polDate = new DateTime(policy.getDtPolicyDate());

                if(endDate.isBefore(startDate))
                    throw new RuntimeException("Tanggal akhir kredit Debitur "+obj.getStReference1()+" tidak boleh kurang dari tanggal mulai");

                if(startDate.isEqual(birthDate))
                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal lahir");

                if(startDate.isEqual(endDate))
                    throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal akhir");

                if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
                    if(year>40)
                        throw new RuntimeException("Tanggal Mulai Dan Akhir Kredit Salah, Debitur "+obj.getStReference1());

                    if(Integer.parseInt(obj.getStReference2())>100)
                        throw new RuntimeException("Usia Debitur "+obj.getStReference1() + " Salah");

                    if(startDate.getYear() > polDate.getYear())
                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());

                    if((polDate.getYear() - startDate.getYear()) > 10)
                        throw new RuntimeException("Tanggal Mulai Salah, Debitur "+obj.getStReference1());

                    DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();

                    if(startDate.isAfter(policyDateLastDay))
                        throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh > tanggal polis");
                }

            }

            // MARK FOR PROSES
            if(!manualReinsurer)
                applyCoinsurance26November2014(obj, policy);

            if (obj.getDbReference4()!=null){
                DTOList tsiList = obj.getSuminsureds();
                if (tsiList!=null && tsiList.size()==1) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(0);

                    tsi.setDbInsuredAmount(obj.getDbReference4());
                }
            }

            //BigDecimal jualRate=null;
            //BigDecimal grossUp2=null;
            //BigDecimal premiRate=null;
            boolean isOR = false;
            if(obj.getStReference8()!=null)
                if(obj.getStReference8().equalsIgnoreCase("1")) isOR = true;

            boolean getRate = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()) || (policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference1()) && !isOR);
            //boolean getRate = true;

//            String jenis_kredit = getJenisKredit(policy.getStKreasiTypeID());
//            String jenis_rate = getJenisRate(policy.getStKreasiTypeID());

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if(getRate){
                        getJenisKredit(policy.getStKreasiTypeID());
                        applyRateProRataNew(obj.getStReference4(), getJenisRate(), getJenisKredit(), obj, policy);
                }
            }

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                    /*if(getRate){
                        final SQLUtil S = new SQLUtil();
                        try {

                            String sql =  "select rate"+ obj.getStReference4() +", gross_up" +
                                    " from ins_rates_big where ref4 = ? and ref5 = ? and (ref3 = ? or ref3 is null) and refid1 = ?"+
                                    " and period_start<=? and period_end>=?"+
                                    " and rate_class='PAKREASI_NEW'";

                            final PreparedStatement PS = S.setQuery(sql);

                            int n=1;

                            PS.setString(n++, jenis_rate); //RATA/TABEL
                            PS.setString(n++, jenis_kredit); //BAKI/PLAFON

                            //PS.setString(n++,policy.getStCostCenterCode()); //ref2

                            PS.setString(n++,obj.getStReference2()); //ref3
                            //PS.setString(n++,obj.getStReference22()); //ref3

                            if(obj.getStReference8()!=null) PS.setString(n++,obj.getStReference8()); //ref1
                            else PS.setString(n++,policy.getStCoinsID()); //ref1

                            S.setParam(n++,policy.getDtPolicyDate()); //period start
                            S.setParam(n++,policy.getDtPolicyDate()); //period end

                            final ResultSet RS = PS.executeQuery();

                            if (RS.next()){
                                //premiRate = RS.getBigDecimal(1);
                                //grossUp2  = RS.getBigDecimal(2);
                                if (!manualRIRate) obj.setDbReference1(RS.getBigDecimal(1));
                                if (!manualRIComRate) obj.setDbReference7(RS.getBigDecimal(2));
                            }else{
                                if (!manualRIRate) obj.setDbReference1(BDUtil.zero);
                                if (!manualRIComRate) obj.setDbReference7(BDUtil.zero);
                            }

                        } finally {
                            S.release();
                        }
                    }
                    */

                //}
                //end

                BigDecimal totalRate = new BigDecimal(0);
                BigDecimal totalPremi = new BigDecimal(0);

                //calculate premi 100%
                if (covers!=null && covers.size()>1) {
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        cv.setStEntryRateFlag(obj.getStReference9());

                        if(Tools.isYes(cv.getStAutoRateFlag()))
                            cv.setStAutoRateFlag(null);

                        totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                        totalRate = BDUtil.add(totalRate,cv.getDbRate());

                        if(BDUtil.isZeroOrNull(cv.getDbRate())){
                            cv.setDbRate(BDUtil.getMileFromRate(BDUtil.div(cv.getDbPremiNew(),cv.getDbInsuredAmount(),5)));
                        }
                    }

                    obj.setDbReference5(totalRate);
                    obj.setDbReference6(totalPremi);

                    if(!policy.isStatusHistory()){
                        if(!useRate) obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                    }
               }else if (covers!=null && covers.size()==1) {
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                    cv.setStEntryRateFlag(obj.getStReference9());

                    if(Tools.isYes(cv.getStAutoRateFlag()))
                        cv.setStAutoRateFlag(null);

                    if (useRate) {
                        cv.setDbRate(obj.getDbReference5());
                        if(!policy.isStatusHistory()){
                            obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                        if(policy.isStatusHistory() && BDUtil.isZeroOrNull(obj.getDbReference6())){
                             obj.setDbReference6(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference5()), scale));
                        }
                    }else{
                        cv.setDbPremiNew(obj.getDbReference6());
                        cv.setDbPremi(obj.getDbReference6());
                        //BigDecimal sukuPremi = BDUtil.getPctFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4()));
                        obj.setDbReference5(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference6(),obj.getDbReference4(),5)));
                        cv.setDbRate(obj.getDbReference5());
                    }

                    if(policy.isStatusEndorse()){
                        obj.setDbReference5(cv.getDbRate());
                        obj.setDbReference6(cv.getDbPremi());
                    }
                }

                if(getRate){
                        applyRateJual(obj.getStReference4(), getJenisRate(), getJenisKredit(), obj, policy, covers);
                }
                //end calculate premi 100%
            }

            objx.reCalculate();

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {

                if (!manualRIRate){
                    //BigDecimal rateGross = BDUtil.div(premiRate,BDUtil.sub(new BigDecimal(100),grossUp2),5);
                    //obj.setDbReference1(premiRate);
                    /*if(BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(premiRate);
                    else if(!BDUtil.isZeroOrNull(grossUp2)) obj.setDbReference1(BDUtil.getPctFromRate(rateGross));
                    */
                    //BLOCK DATA BANDUNG USIA > 70
                    //if(policy.getStCostCenterCode().equalsIgnoreCase("21"))
                    if(Integer.parseInt(obj.getStReference2())>70)
                        obj.setDbReference1(BDUtil.zero);
                }

                //start endorse calc
                BigDecimal premiAmountDiff = null;
                if(policy.isStatusEndorse()){
                    BigDecimal insAmountDiff = null;
                    BigDecimal totalInsAmountDiff = null;
                    for (int l = 0; l < covers.size(); l++) {
                        InsurancePolicyCoverView icv = (InsurancePolicyCoverView) covers.get(l);

                        final InsurancePolicyCoverView refCover = icv.getRefCover();

                        if(refCover==null){
                            //continue;
                            insAmountDiff = obj.getDbReference4();
                        }else{
                            insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                            premiAmountDiff = BDUtil.sub(icv.getDbPremi(),refCover.getDbPremi());
                            totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                        }
                    }
                    obj.setDbObjectInsuredAmount(insAmountDiff);
                    obj.setDbReference11(insAmountDiff);
                }

                //end endrose

                if (manualPremiKoas){
                    obj.setDbReference2(obj.getDbReference2());
                    if(!policy.isStatusEndorse()){
                        obj.setDbReference1(BDUtil.getMileFromRate(BDUtil.div(obj.getDbReference2(),obj.getDbReference4(),5)));
                    }
                }else{

                    obj.setDbReference2(
                            BDUtil.mul(BDUtil.getRateFromMile(obj.getDbReference1()), obj.getDbObjectInsuredAmount(),scale)
                            );

                    obj.setDbReference17(obj.getDbReference2());

                    if(!BDUtil.isZeroOrNull(obj.getDbReference7())){
                        obj.setDbReference2(BDUtil.divRoundUp(obj.getDbReference2(),BDUtil.getRateFromPct(BDUtil.sub(new BigDecimal(100),obj.getDbReference7())),scale));
                    }

                     //MARK FOR PROSES
                    if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse() && !policy.isStatusEndorse()){
                        if(getRate){
                            if(BDUtil.biggerThan(obj.getDbReference2(), obj.getDbReference6())){
                                obj.setStReference8("1");
                                obj.setDbReference1(BDUtil.zero);
                                obj.setDbReference2(BDUtil.zero);
                                obj.setDbReference7(BDUtil.zero);
                            }
                        }
                    }

                    if(policy.isStatusEndorse() && !BDUtil.isZeroOrNull(premiAmountDiff) && BDUtil.isZeroOrNull(obj.getDbReference2())){
                        //BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                        BigDecimal tsi = obj.getDbReference4()!=null?obj.getDbReference4():obj.getDbObjectInsuredAmount();
                        BigDecimal premiKoas = null;
                        BigDecimal premiKoasParent = BDUtil.mul(tsi,BDUtil.getRateFromMile(obj.getDbReference1()));
                        premiKoas = BDUtil.mul(BDUtil.div(obj.getDbReference6(), obj.getDbReference12()),premiKoasParent);
                        premiKoas = BDUtil.roundUp(premiKoas);

                        obj.setDbReference2(premiKoas);
                    }
                    /*
                    if(policy.isStatusEndorse() && BDUtil.isZeroOrNull(obj.getDbReference2()) && !BDUtil.isZeroOrNull(obj.getDbReference6())){
                        obj.setDbReference2(BDUtil.mul(obj.getDbReference4(),BDUtil.getRateFromMile(obj.getDbReference1()), scale));
                    }*/
                }
            }

            if (obj.getStReference2()!=null && obj.getStReference4()!=null) {
                if (!manualRIComRate)
                    obj.setDbReference7(obj.getDbReference7());

                obj.setDbReference9(
                        BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference7()), obj.getDbReference2(),scale)
                        );
            }

            if(policy.isStatusEndorse() && obj.getDtReference5()!=null) {
                recalculateRestitusi(policy, obj, obj.getDbReference1(), obj.getDbReference7());
            }

            DTOList coinsMurni = policy.getCoins2();
            BigDecimal askridaSharePct = null;
            BigDecimal askridaShareTSI = null;
            BigDecimal askridaSharePremi = null;

            for(int i = 0 ; i< coinsMurni.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni.get(i);

                if(!coins3.isHoldingCompany()) continue;

                if(coins3.isHoldingCompany()){
                    askridaSharePct = coins3.getDbSharePct();
                    askridaShareTSI = coins3.getDbAmount();
                    askridaSharePremi = coins3.getDbPremiAmount();
                }
            }

            DTOList coins = policy.getCoinsCoverage();
            if (coins.size()>1) {
                while (coins.size()>1) coins.delete(coins.size()-1);
            }

            if (coins.size()==0) throw new RuntimeException("Unknown coinsurance model ... (size==0)");

            //here
            final DTOList oj = policy.getObjects();
            String [] coax = new String[5];
            int counter = 0;
            BigDecimal totalPremiCo94 = null;
            BigDecimal totalKomisiCo94 = null;
            BigDecimal totalPremiCo96= null;
            BigDecimal totalKomisiCo96 = null;
            BigDecimal totalPremiCo2000 = null;
            BigDecimal totalKomisiCo2000 = null;
            BigDecimal totalPremiCo2001 = null;
            BigDecimal totalKomisiCo2001 = null;
            BigDecimal totalPremiCo2002 = null;
            BigDecimal totalKomisiCo2002 = null;

            for (int i = 0; i < oj.size(); i++)
            {
                InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);

                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;

                if(obje.getStReference8()!=null){
                    if(obje.getStReference8().equalsIgnoreCase("94")){
                        totalPremiCo94 = BDUtil.add(totalPremiCo94, obje.getDbReference2());
                        totalKomisiCo94 = BDUtil.add(totalKomisiCo94, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("96")){
                        totalPremiCo96 = BDUtil.add(totalPremiCo96, obje.getDbReference2());
                        totalKomisiCo96 = BDUtil.add(totalKomisiCo96, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2000")){
                        totalPremiCo2000 = BDUtil.add(totalPremiCo2000, obje.getDbReference2());
                        totalKomisiCo2000 = BDUtil.add(totalKomisiCo2000, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2001")){
                        totalPremiCo2001 = BDUtil.add(totalPremiCo2001, obje.getDbReference2());
                        totalKomisiCo2001 = BDUtil.add(totalKomisiCo2001, obje.getDbReference9());
                    }else if(obje.getStReference8().equalsIgnoreCase("2002")){
                        totalPremiCo2002 = BDUtil.add(totalPremiCo2002, obje.getDbReference2());
                        totalKomisiCo2002 = BDUtil.add(totalKomisiCo2002, obje.getDbReference9());
                    }
                }

                if(obje.getStReference8()!=null){
                    if(i!=0){
                        if(coax[0]!=null)
                            if(coax[0].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[1]!=null)
                            if(coax[1].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[2]!=null)
                            if(coax[2].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[3]!=null)
                            if(coax[3].equalsIgnoreCase(obje.getStReference8()))
                                continue;

                        if(coax[4]!=null)
                            if(coax[4].equalsIgnoreCase(obje.getStReference8()))
                                continue;
                    }

                    if(obje.getStReference8().equalsIgnoreCase("1")) continue;
                    coax [counter] = obje.getStReference8();
                    counter++;
                }

            }

            if(policy.isStatusClaim() || policy.isStatusClaimEndorse()){
                counter = 0;
                coax = new String[5];
                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) policy.getClaimObject();
                if(obje.getStReference8()!=null)
                    coax[0] = obje.getStReference8();

                if(obje.getStReference8()==null) coax[0] = policy.getStCoinsID();

                counter++;
            }

            for (int i = 0; i < counter; i++){
                final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

                co.markNew();
                co.setStPositionCode(FinCodec.CoInsurancePosition.MEMBER);
                co.setStAutoPremiFlag("N");
                co.setStFlagEntryByRate("Y");
                co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);
                co.setStEntityID(coax[i]);
                coins.add(co);
            }


             BigDecimal totPremi = BDUtil.zero;
             BigDecimal totPremiCoas = BDUtil.zero;
            //BACKUP COAS
               if (coins.size()>0){
                  for(int i = 0 ; i< coins.size();i++){
                        InsurancePolicyCoinsView coins2 = (InsurancePolicyCoinsView) coins.get(i);

                        DTOList objects = policy.getObjects();
                        totPremi = (BigDecimal) objects.getTotal("refn6");
                        BigDecimal totRIPremi = (BigDecimal) objects.getTotal("refn2");
                        //BigDecimal totRIPremiBAJ = (BigDecimal) objects.getTotal("refn13");
                        //BigDecimal totRIPremiJS = (BigDecimal) objects.getTotal("refn14");
                        //BigDecimal totCommissionBAJ = (BigDecimal) objects.getTotal("refn15");
                        //BigDecimal totCommissionJS = (BigDecimal) objects.getTotal("refn16");
                        BigDecimal total = BDUtil.add(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))), BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct))));

                        if(coins2.isHoldingCompany()){
                            coins2.setDbSharePct(BDUtil.hundred);
                            coins2.setDbAmount(askridaShareTSI);
                            coins2.setStEntityID("1");
                            coins2.setStAutoPremiFlag("N");
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(BDUtil.sub(totPremi,totRIPremi),BDUtil.getRateFromPct(askridaSharePct))));
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                            //if(!BDUtil.isEqual(askridaSharePremi, total, 0))
                                //coins2.setDbPremiAmount(BDUtil.sub(askridaSharePremi, BDUtil.roundUp(BDUtil.mul(totRIPremi, BDUtil.getRateFromPct(askridaSharePct)))));
                        }else if(coins2.getStEntityID().equalsIgnoreCase("94")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo94);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo94, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("96")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo96);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo96, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2000")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2000);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2000, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2001")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2001);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2001, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }else if(coins2.getStEntityID().equalsIgnoreCase("2002")){
                            coins2.setDbSharePct(BDUtil.zero);
                            coins2.setDbPremiAmount(totalPremiCo2002);
                            coins2.setDbPremiAmount(BDUtil.roundUp(BDUtil.mul(coins2.getDbPremiAmount(), BDUtil.getRateFromPct(askridaSharePct))));
                            coins2.setDbCommissionAmount(BDUtil.mul(totalKomisiCo2002, BDUtil.getRateFromPct(askridaSharePct)));
                            obj.setDbReference3(coins2.getDbHandlingFeeRate());
                            totPremiCoas = BDUtil.add(totPremiCoas, coins2.getDbPremiAmount());
                        }
                   }
               }

            DTOList coinsMurni2 = policy.getCoins2();

            for(int i = 0 ; i< coinsMurni2.size();i++){
                InsurancePolicyCoinsView coins3 = (InsurancePolicyCoinsView) coinsMurni2.get(i);

                if(coins3.isHoldingCompany() && !coins3.isManualPremi()) coins3.setDbPremiAmount(BDUtil.roundUp(totPremiCoas));

                if(!coins3.isHoldingCompany() && !coins3.isManualPremi()){
                    coins3.setDbPremiAmount(BDUtil.mul(BDUtil.getRateFromPct(coins3.getDbSharePct()), totPremi));
                    coins3.setDbPremiAmount(BDUtil.roundUp(coins3.getDbPremiAmount()));
                }

                if(coins3.isManualPremi())
                    coins3.setDbPremiAmount(coins3.getDbPremiAmount());
            }

            if(!policy.isStatusEndorse()){
                obj.setDbReference12(obj.getDbReference6());
            }

            //VALIDATING APPROVAL
            if(policy.getStEffectiveFlag()!=null){
                if(policy.getStEffectiveFlag().equalsIgnoreCase("Y"))
                     validateApproval(policy, obj);
            }

            boolean manualNoRekap = Tools.isYes(obj.getStReference23());

            if(policy.isStatusPolicy() || policy.isStatusEndorse() || policy.isStatusRenewal()){

                if(!manualNoRekap){
                    obj.setStRekapKreasi(generateNoRekap(obj.getStReference8(), policy));
                }

                obj.setStRekapKreasi(obj.getStRekapKreasi().toUpperCase());

            }

            //checkingPremiBalance(obj);
            //PERHITUNGAN RESTITUSI PREMI
            /*if(policy.isStatusEndorse() && isRestitusi) {
                recalculateRestitusiNew(policy, obj, premiRate, grossUp2, coins, askridaSharePct);
            }*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void applyRateJual(String lama, String jenis_rate, String jenis_kredit, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy,DTOList covers) throws Exception{
        // Premi Sekaligus Usia 20 tahun  masa asuransi 2 tahun 3 bulan = 0.97 + (3/12) x ( 1.45 - 0.97 )
        BigDecimal rateJual1 = BDUtil.zero;
        BigDecimal rateJual2 = BDUtil.zero;

        if(Integer.parseInt(lama)==0) lama = String.valueOf(1);

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);
        int lamaPlus1 = Integer.parseInt(lama) + 1;


        //get rate 1
        final SQLUtil S = new SQLUtil();
            try {

                //logger.logWarning("############# objek : "+ obj.getStReference1());
                //logger.logWarning("############# lama : "+ lama);
                String sql =  "select rate"+ lama +", rate" + String.valueOf(lamaPlus1) +
                              " from ins_rates_big where ref1= ? and ref4 = ? and ref2 = ? "+
                              " and period_start <= ? and period_end >= ? "+
                              " and rate_class = 'PAKREASI_JUAL'";

                final PreparedStatement PS = S.setQuery(sql);

                int n=1;

                PS.setString(n++, policy.getStKreasiTypeID()); //jenis kredit
                PS.setString(n++, jenis_rate); //RATA/TABEL
                PS.setString(n++, policy.getStCostCenterCode()); //cabang
                S.setParam(n++,policy.getDtPolicyDate()); //period start
                S.setParam(n++,policy.getDtPolicyDate()); //period end

                logger.logWarning("############# jenis : "+ policy.getStKreasiTypeID());
                logger.logWarning("############# jns rate : "+ jenis_rate);
                logger.logWarning("############# cabang : "+ policy.getStCostCenterCode());
                logger.logWarning("############# tgl polis : "+ policy.getDtPolicyDate());

                final ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    logger.logWarning("############# dapet dong !!!!! ");
                    rateJual1 = RS.getBigDecimal(1);
                    rateJual2 = RS.getBigDecimal(2);
                }else{
                    logger.logWarning("############# gak dapet :( !!!!! ");
                    rateJual1 = BDUtil.zero;
                    rateJual2 = BDUtil.zero;
                }

            } finally {
                S.release();
            }

          //calculate rate
          // Premi Sekaligus Usia 20 tahun  masa asuransi 2 tahun 3 bulan = 0.97 + (3/12) x ( 1.45 - 0.97 )

          int KelebihanBulan = 0;

          //if(Integer.parseInt(obj.getStReference6()) > 12){
              int kelebihanHari = DateUtil.getKelebihanHari(obj.getDtReference2(), obj.getDtReference3());
              int maxKelebihan = 1;
              int lamaBulan = Integer.parseInt(obj.getStReference6());

              if(obj.getStReference8().equalsIgnoreCase("2002")){
                  if(kelebihanHari >= maxKelebihan)
                        lamaBulan = lamaBulan + 1;
              }
              KelebihanBulan = lamaBulan % 12;
          //}

           BigDecimal a =  rateJual1;
           BigDecimal b =  rateJual2;
           BigDecimal lebihBulan = new BigDecimal(KelebihanBulan);
           BigDecimal Bulan = new BigDecimal(12);

           if(Integer.parseInt(obj.getStReference6()) < 12){
               lebihBulan = new BigDecimal(obj.getStReference6());
               if(obj.getStReference8().equalsIgnoreCase("2002")){
                  if(kelebihanHari >= maxKelebihan)
                        lebihBulan = BDUtil.add(lebihBulan, BigDecimal.ONE);
               }
           }

           BigDecimal one = BDUtil.div(lebihBulan, new BigDecimal(12),6);
           BigDecimal two = BDUtil.mulRound(one, (BDUtil.sub(b, a)),2);
           BigDecimal rateProRataFinal2 = BDUtil.add(a, two);

           if(Integer.parseInt(obj.getStReference6()) < 12){
                 rateProRataFinal2 = BDUtil.mulRound(one, a,2);
           }

           rateProRataFinal2 = rateProRataFinal2.setScale(2, BigDecimal.ROUND_HALF_UP);

           boolean manualRIRate = Tools.isYes(obj.getStReference10());
           boolean manualRIComRate = Tools.isYes(obj.getStReference11());

           //if (!manualRIRate) obj.setDbReference1(rateProRataFinal2);
           //if (!manualRIComRate) obj.setDbReference7(komisiKoas1);

           if (covers!=null) {
               BigDecimal totalRate = null;
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        logger.logWarning("############# objek : "+ obj.getStReference1());
                        logger.logWarning("############# rate cabang : "+ cv.getDbRate());
                        logger.logWarning("############# rate limit : "+ rateJual1);

                        totalRate = BDUtil.add(totalRate, cv.getDbRate());
                    }

                    if(BDUtil.lesserThan(totalRate, rateJual1)){
                        throw new RuntimeException("Rate jual tidak boleh < "+rateJual1);
                    }
               }

    }

    /**
     * @return the jenisKredit
     */
    public String getJenisKredit() {
        return jenisKredit;
    }

    /**
     * @param jenisKredit the jenisKredit to set
     */
    public void setJenisKredit(String jenisKredit) {
        this.jenisKredit = jenisKredit;
    }

    /**
     * @return the jenisRate
     */
    public String getJenisRate() {
        return jenisRate;
    }

    /**
     * @param jenisRate the jenisRate to set
     */
    public void setJenisRate(String jenisRate) {
        this.jenisRate = jenisRate;
    }



    
}











