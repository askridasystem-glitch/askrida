/***********************************************************************
 * Module:  com.webfin.insurance.custom.CashManagementHandler
 * Author:  Denny Mahendra
 * Created: Sep 29, 2006 12:19:13 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.crux.common.parameter.Parameter;
import com.webfin.insurance.model.*;
import com.crux.util.*;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CreditMacetHandler extends DefaultCustomHandler {
    private final static transient LogManager logger = LogManager.getInstance(CreditHandler.class);

    final boolean penerapanKreditBaru = false;
    final int limitHariSTNC = 90; //berubah dari 60 ke 90 per 17 okt 2019

    private String kalkulatorPremiJiwaEndPoint = Parameter.readString("SYS_CALC_QOALA_ENDPOINT");
    final boolean penerapanKreditJiwa = true;

    final boolean penerapanKreditSerbaguna = false;

    BigDecimal pctKomposisi1;
    BigDecimal pctKomposisi2;
    BigDecimal pctKomposisi3;
    BigDecimal pctKomposisi4;
    BigDecimal pctKomposisi5;
    BigDecimal pctKomposisi6;

    public BigDecimal getPctKomposisi1() {
        return pctKomposisi1;
    }

    public void setPctKomposisi1(BigDecimal pctKomposisi1) {
        this.pctKomposisi1 = pctKomposisi1;
    }

    public BigDecimal getPctKomposisi2() {
        return pctKomposisi2;
    }

    public void setPctKomposisi2(BigDecimal pctKomposisi2) {
        this.pctKomposisi2 = pctKomposisi2;
    }

    public BigDecimal getPctKomposisi3() {
        return pctKomposisi3;
    }

    public void setPctKomposisi3(BigDecimal pctKomposisi3) {
        this.pctKomposisi3 = pctKomposisi3;
    }

    public BigDecimal getPctKomposisi4() {
        return pctKomposisi4;
    }

    public void setPctKomposisi4(BigDecimal pctKomposisi4) {
        this.pctKomposisi4 = pctKomposisi4;
    }

    public BigDecimal getPctKomposisi5() {
        return pctKomposisi5;
    }

    public void setPctKomposisi5(BigDecimal pctKomposisi5) {
        this.pctKomposisi5 = pctKomposisi5;
    }

    public BigDecimal getPctKomposisi6() {
        return pctKomposisi6;
    }

    public void setPctKomposisi6(BigDecimal pctKomposisi6) {
        this.pctKomposisi6 = pctKomposisi6;
    }

    public void applyClausules(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
        try {

            if(true) return;

            if(policy.isStatusEndorse()) return;

            //if(!validate) return;
            
            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            boolean manualKlausula = Tools.isYes(objx.getStReference21());

            if(!manualKlausula){

                    DTOList clausulaPolis = policy.getClausules();
                    for (int i = 0; i < clausulaPolis.size(); i++) {
                        InsurancePolicyClausulesView claus = (InsurancePolicyClausulesView) clausulaPolis.get(i);

                        String filterClause = claus.getStChildClausules();
                        //   1=4500,44997|2=46955,46956
                        String Clause[] = filterClause.split("[\\|]");
                        String clausePNS[] = Clause[0].split("[\\,]");
                        String clauseNonPNS[] = Clause[1].split("[\\,]");

                        if(claus.isSelected()){
                             for (int j = 0; j < clausulaPolis.size(); j++){
                                 InsurancePolicyClausulesView claus2 = (InsurancePolicyClausulesView) clausulaPolis.get(j);

                                 for(int k=0; k < clausePNS.length; k++){
                                     if(claus2.getStParentID().equalsIgnoreCase(clausePNS[k]))
                                         claus2.setStSelectedFlag("Y");
                                 }

                             }
                        }

                    }

                    DTOList clausules;

                    if(objx.getStReference7()==null)
                        throw new RuntimeException("Pekerjaan nasabah "+ objx.getStReference1()+" harus diisi");

                    String status = objx.getStReference7();
                    boolean isStatusPNS = status.equalsIgnoreCase("1");

                    if (policy.getStPolicyTypeID() != null) {
                        clausules = ListUtil.getDTOListFromQuery(
                                "   select " +
                                "      a.*,b.description,b.description_new,b.shortdesc,b.clause_level,b.active_flag,b.ins_clause_id as ins_clause_id2,b.rate_type as rate_type2,b.rate as ratedef,b.f_default,b.ref1, b.child_clausules, b.parent_id " +
                                "   from " +
                                "      ins_clausules b " +
                                "      left join ins_pol_clausules a on " +
                                "         a.ins_clause_id = b.ins_clause_id" +
                                "         and a.pol_id = ? " +
                                "         and a.ins_pol_obj_id is null" +
                                "   where b.pol_type_id = ? and (cc_code is null or cc_code=?) and b.active_flag = 'Y'" +
                                "   order by b.shortdesc",
                                new Object[]{policy.getStPolicyID(), policy.getStPolicyTypeID(), policy.getStCostCenterCode()},
                                InsurancePolicyClausulesView.class
                                );



                        String ent_name = "";
                        if(policy.getEntity()!=null)
                            ent_name = policy.getEntity().getStEntityName();

                        for (int i = 0; i < clausules.size(); i++) {
                            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                            icl.setStDescription(icl.getStDescription().replaceAll("%S%",ent_name));

                            if (icl.getStPolicyID() != null) icl.select();
                            else {
                                icl.setDbRate(icl.getDbRateDefault());
                                icl.markNew();
                            }

                            icl.setStSelectedFlag(icl.isSelected() ? "Y" : "N");
                        }

        //                logger.logDebug("++++++++ cek polis baru : "+ policy.isNew());

                        if(policy.getStNextStatus()!=null){
                            if (policy.getStNextStatus().equalsIgnoreCase("POLICY") || policy.getStNextStatus().equalsIgnoreCase("SPPA")) {

                                policy.setClausules(null);
                                policy.setClausules(clausules);

                                for (int i = 0; i < clausules.size(); i++) {
                                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                                    icl.setStDescription(icl.getStDescription().replaceAll("%S%",ent_name));

                                    if (Tools.isYes(icl.getStDefaultFlag())) {
        //                                logger.logDebug("+++++++++++ set default : "+ icl.getStDefaultFlag());
                                        icl.setStSelectedFlag("Y");
                                        icl.select();
                                    }

                                    if(isStatusPNS){
                                        if(icl.getStReference1()!=null)
                                            if(icl.getStReference1().equalsIgnoreCase(status)){
        //                                        logger.logDebug("+++++++++++ deselect ++++++++++++ ");
                                                icl.setStSelectedFlag(null);
                                                icl.deSelect();
                                            }
                                    }
                                }
                            }
                        }

                    }
            }
            
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
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            if(!policy.isStatusClaim()){
                obj.setDtReference5(null);
                obj.setStReference9(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         calculate(policy, objx, validate);
    }

    public void calculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            //OTOMATIS SET COVERAGE
            //applyCoverage(obj,policy);
            //applyExtraPremi(obj, policy);
            //END
            
            if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
                //otomatis set stnc to all object sesuai objek 1 kecuali endorse
                final InsurancePolicyObjDefaultView obj1 = (InsurancePolicyObjDefaultView) policy.getObjects().get(0);

                boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

                int jumlahHariKalender = DateUtil.getDaysAmount(obj.getDtReference2(), policy.getDtPolicyDate());

                if(!persetujuanPusat)
                        obj.setDtReference6(obj1.getDtReference6());

                if(!persetujuanPusat){
                    if(jumlahHariKalender <= 60)
                        obj.setDtReference6(obj.getDtReference2());

                    if(jumlahHariKalender > 60)
                        obj.setDtReference6(policy.getDtPolicyDate());
                }

                //CEK DOKUMEN YG DI UPLOAD
                boolean sudahUploadDocSKP = false;
                Date tanggalSKP = null;

                DTOList policyDocuments = policy.getPolicyDocuments();

                if(policyDocuments!=null){
                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                        if(doc.getStInsuranceDocumentTypeID().equalsIgnoreCase("156")){
                            if(doc.isMarked()&& doc.getStFilePhysic()!=null)
                                sudahUploadDocSKP = true;

                            if(doc.getDtDocumentDate()!=null)
                                tanggalSKP = doc.getDtDocumentDate();
                        }
                    }
                }
                
                //Set tgl stnc by tgl SKP jika diisi
                if(tanggalSKP!=null){
                    obj.setDtReference6(tanggalSKP);
                }


            }

            //HITUNG POJK 20
            //if(policy.isKreditSerbaguna())
            if(!policy.isStatusHistory() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse() && !policy.isStatusRenewal()){

                obj.setDbReference7(null);
                
                if(obj.getDbReference6()!=null)
                    obj.setDbReference7(BDUtil.mul(obj.getDbReference11(), BDUtil.getRateFromPct(obj.getDbReference6()), 0));

                calculateSimulasiPOJK20(obj, policy);
            }
                


            if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

                //proteksi maks 5 tahun jk kredit
                int jangkaWaktuTahun = DateUtil.getYearsBetweenHUTB(obj.getDtReference2(), obj.getDtReference3());

                DateTime periodeAwal = new DateTime(obj.getDtReference2());
                DateTime bulanPOJK20 = new DateTime(DateUtil.getDate("13/12/2024"));

                //if(periodeAwal.isAfter(bulanPOJK20)){
                    if(jangkaWaktuTahun > 5)
                        throw new RuntimeException("Jangka waktu pertanggungan asuransi kredit Debitur "+obj.getStReference1()+" maksimal 5 tahun");

                    if(policy.getObjects().size()>1)
                        throw new RuntimeException("Polis Kredit Serbaguna hanya boleh 1 polis 1 debitur (satuan)");
            }


            Date birth = obj.getDtReference1();
            Date perStart = obj.getDtReference2();
            Date perEnd = obj.getDtReference3();

            long age = 0;

            if(birth!=null) age = perStart.getTime()-(birth.getTime());

            age = age / (1000l*60l*60l*24l*365l);

            if(obj.getStReference2()==null)
                obj.setStReference2(String.valueOf(age));

            if (perStart!=null && perEnd!=null) {

                long lama = perEnd.getTime()-perStart.getTime();
                //long agemonth = age / (1000l*60l*60l*24l*30l);
                long ageyear = lama / (1000l*60l*60l*24l*365l);

                obj.setStReference5(String.valueOf(ageyear));
             }

            DateTime birthDate = new DateTime(birth);
            DateTime startDate = new DateTime(perStart);
            DateTime endDate = new DateTime(perEnd);
            Months m = Months.monthsBetween(startDate, endDate);
            Years y = Years.yearsBetween(startDate, endDate);
            int mon = m.getMonths();
            int year = y.getYears();

            //PERHITUNGAN KOAS JIWA
            //applyTC(String.valueOf(year), obj, policy);
            if(penerapanKreditJiwa){
                if(policy.isCoinsurance()){
                    recalculateJiwaViaAPI(policy, obj);
                    recalculateCoas(policy);
                }
            }
            
            DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();

            if(obj.getStReference5()==null)
                obj.setStReference5(String.valueOf(year));

            if(obj.getStReference11()==null)
                obj.setStReference11(String.valueOf(mon));

             if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

                 if(!policy.isBypassValidation()){
                        if(startDate.isAfter(policyDateLastDay))
                            throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh > tanggal polis");

                        if(endDate.isBefore(startDate))
                                throw new RuntimeException("Tanggal akhir kredit Debitur "+obj.getStReference1()+" tidak boleh kurang dari tanggal mulai");

                            if(startDate.isEqual(birthDate))
                                throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal lahir");

                        if(startDate.isEqual(endDate))
                            throw new RuntimeException("Tanggal mulai kredit Debitur "+obj.getStReference1()+" tidak boleh sama dengan tanggal akhir");

                        if(Integer.parseInt(obj.getStReference2()) < 17)
                            throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Dibawah 17 Tahun");

                        if(Integer.parseInt(obj.getStReference2()) > 75)
                            throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh Melebihi 75 Tahun");

                        if(age > 75 || age < 17)
                            throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Salah, Cek Tanggal Lahir");
                 }

             }

            objx.reCalculate();

            final DTOList covers = obj.getCoverage();

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
                            insAmountDiff = obj.getDbObjectInsuredAmount();
                        }else{
                            insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                            premiAmountDiff = BDUtil.sub(icv.getDbPremi(),refCover.getDbPremi());
                            totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                        }
                    }
                    obj.setDbObjectInsuredAmount(insAmountDiff);
                    obj.setDbReference2(insAmountDiff);
                }

                //end endrose
                BigDecimal totalTSI = null;
                DTOList tsiList = obj.getSuminsureds();
                for(int j=0;j<tsiList.size();j++){
                     InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(j);

                     totalTSI = BDUtil.add(totalTSI, tsi.getDbInsuredAmount());
                }

                BigDecimal totalPremi = null;
                BigDecimal totalRate = null;

                for(int i=0;i<covers.size();i++){
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                    totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                    totalRate = BDUtil.add(totalRate,cv.getDbRate());

                }

                
                if(!policy.isStatusEndorse()){
                    obj.setDbReference3(totalTSI);
                    obj.setDbReference4(totalPremi);
                }

            recalculateRestitusi(policy, obj);

            //CEK JIKA ADA EKSTRA PREMI
            /*
                obj.setDbReference7(null);

                if (obj.getDbReference6() != null){
                    
                    BigDecimal premiAskred = null;

                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        int scale = 0;
                        if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
                        else scale = 0;

                        
                        if(cv.getStInsuranceCoverID().equalsIgnoreCase("238")){
                            premiAskred = cv.getDbPremi();
                        }

                        if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("572")){
                            cv.setDbPremiNew(BDUtil.mul(premiAskred, BDUtil.getRateFromPct(obj.getDbReference6()),scale));
                            cv.setDbPremi(cv.getDbPremiNew());
                            cv.setStCalculationDesc(ConvertUtil.removeTrailing(ConvertUtil.print(premiAskred,4)) +" x "+ ConvertUtil.removeTrailing(ConvertUtil.print(obj.getDbReference6(),4))+ policy.getStRateMethodDesc());

                            obj.setDbReference7(cv.getDbPremiNew());
                            //totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                        }
                    }

                    obj.reCalculate();
                }
            */

            if(penerapanKreditBaru){
                applyWarranty(obj,policy);
                obj.setStReference20(null);
                validateRateJual(String.valueOf(year), obj, policy, covers);
                validateLimitPKS(policy, obj, validate);   
            }

            applyWarranty(obj,policy);
            validateApproval(policy, obj, validate);

            //VALIDASI APPROVAL KALAU ADA KOAS JIWA
            if(policy.isCoinsurance())
                validateApprovalCoas(policy, validate);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doAddLampiranCover(InsurancePolicyObjectView obj, String inscovpolid) throws Exception {

        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

        cv.setStInsuranceCoverPolTypeID(inscovpolid);//242,243,244

        cv.initializeDefaults();

        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

        cv.markNew();

        obj.getCoverage().add(cv);
    }

    public void calculateBuatProses(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            Date birth = obj.getDtReference1();
            Date perStart = obj.getDtReference2();
            Date perEnd = obj.getDtReference3();

            long age = perStart.getTime()-(birth.getTime());

            age = age / (1000l*60l*60l*24l*365l);

            if(obj.getStReference2()==null)
                obj.setStReference2(String.valueOf(age));

            DateTime birthDate = new DateTime(birth);
            DateTime startDate = new DateTime(perStart);
            DateTime endDate = new DateTime(perEnd);
            Months m = Months.monthsBetween(startDate, endDate);
            Years y = Years.yearsBetween(startDate, endDate);
            int mon = m.getMonths();
            int year = y.getYears();

            DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();


            final DTOList covers = obj.getCoverage();

            objx.reCalculate();

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
                            insAmountDiff = obj.getDbObjectInsuredAmount();
                        }else{
                            insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(),refCover.getDbInsuredAmount());
                            premiAmountDiff = BDUtil.sub(icv.getDbPremi(),refCover.getDbPremi());
                            totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                        }
                    }
                    obj.setDbObjectInsuredAmount(insAmountDiff);
                    obj.setDbReference2(insAmountDiff);
                }

                //end endrose
                BigDecimal totalTSI = null;
                DTOList tsiList = obj.getSuminsureds();
                for(int j=0;j<covers.size();j++){
                     InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(j);

                     totalTSI = BDUtil.add(totalTSI, tsi.getDbInsuredAmount());
                }

                BigDecimal totalPremi = null;
                BigDecimal totalRate = null;
                for(int i=0;i<covers.size();i++){
                    InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                    totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                    totalRate = BDUtil.add(totalRate,cv.getDbRate());

                }

                if(!policy.isStatusEndorse()){
                    obj.setDbReference3(totalTSI);
                    obj.setDbReference4(totalPremi);
                }

            recalculateRestitusi(policy, obj);

            //validateApproval(policy, obj);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void recalculateRestitusi(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj) throws Exception{

        if(!policy.isStatusEndorse()){
            obj.setDbReference1(obj.getDbObjectPremiTotalAmount());

            if(!BDUtil.isZeroOrNull(obj.getDbReference7()))
                obj.setDbReference1(BDUtil.add(obj.getDbObjectPremiTotalAmount(),obj.getDbReference7()));
        }

        if(policy.isStatusEndorse() && obj.getDtReference5()!=null) {

                int scale = 0;

                if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;

                final DTOList covers = obj.getCoverage();
                final DTOList suminsured = obj.getSuminsureds();
                final Date perStart = obj.getDtReference2();
                final Date perEnd = obj.getDtReference3();

                for (int i = 0; i < suminsured.size(); i++) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView)suminsured.get(i);

                    if(!BDUtil.isZero(tsi.getDbInsuredAmount())){
                        throw new RuntimeException("Debitur "+ obj.getStReference1() +" Harus Nol TSI nya untuk Restitusi");
                    }

                }

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

                if(obj.getStReference9()==null)
                    obj.setStReference9(String.valueOf(sisaJangkaWaktu));

                //final BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) covers.get(0);

                BigDecimal premi = null;

                premi = BDUtil.negate(obj.getDbReference1());

                if(BDUtil.isZeroOrNull(obj.getDbReference1()))
                    premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(cover.getDbRate()),scale);

                //premi 100 sbgai dasar perhitungan
                premi = BDUtil.negate(obj.getDbReference11());

                premiSisa = BDUtil.mul(BDUtil.mul(premi,policy.getDbPeriodRateBeforeFactor(), scale),new BigDecimal(obj.getStReference9()), scale);
                premiSisa = BDUtil.div(premiSisa,new BigDecimal(mon));
                premiSisa = BDUtil.roundUp(premiSisa);

                //obj.setDbReference6(premiSisa);

                //BigDecimal totalRate = new BigDecimal(0);
                //BigDecimal totalPremi = new BigDecimal(0);
                BigDecimal premiBruto = null;
                if (covers!=null) {
                    for (int i = 0; i < covers.size(); i++) {
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        if(cv.getStInsuranceCoverID().equalsIgnoreCase("238")){
                            final InsurancePolicyCoverView refCover = cv.getRefCover();

                            if(!BDUtil.isZeroOrNull(refCover.getDbInsuredAmount())){
                                //cv.setStEntryRateFlag(obj.getStReference9());
                                cv.setDbPremiNew(premiSisa);
                                cv.setDbPremi(premiSisa);

                                logger.logDebug("######################## premi sisa = "+ premiSisa);

                                obj.setDbObjectPremiTotalAmount(premiSisa);
                                obj.setDbObjectPremiTotalBeforeCoinsuranceAmount(premiSisa);

                                //final String curCalc = cv.getStCalculationDesc();

                                final StringBuffer szCalc = new StringBuffer();
                                szCalc.append(premi);

                                if (!BDUtil.isEqual(policy.getDbPeriodRateBeforeFactor(),BDUtil.one,0))
                                    szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());

                                szCalc.append(" x "+obj.getStReference9()+" / "+mon);

                                String calc = szCalc.toString();

                                cv.setStCalculationDesc(calc);

                                premiBruto = BDUtil.add(premiBruto,cv.getDbPremi());
                            }
                        }

                    }
                    
                    /*
                    if(covers.size()==1){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                    }*/

                }

                //validasi sudah pernah klaim belum
                if(!BDUtil.isZeroOrNull(premiSisa)){
                    policy.cekSudahPernahKlaim(obj);
                }

            if(policy.getStEndorseNotes()==null){
                policy.setStEndorseNotes("DENGAN INI DICATAT DAN DISETUJUI, BAHWA :\n\n"+
                        "1). PINJAMAN KREDIT ATAS NAMA YANG TERCANTUM PADA LAMPIRAN POLIS INI TELAH DILUNASI.\n"+
                        "2). ATAS HAL TERSEBUT, MAKA DILAKUKAN RESTITUSI PREMI SESUAI DENGAN SYARAT DAN KETENTUAN YANG BERLAKU");
            }


                setBlankFieldSimulasi(obj, policy);

            }
    }

    public void applyClausules2(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
        try {

            //if(!validate) return;

            if(true) return;

            if (policy.isStatusEndorse()) {
                return;
            }

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            boolean manualKlausula = Tools.isYes(objx.getStReference21());

            if (objx.getStReference7() == null) {
                throw new RuntimeException("Kategori nasabah PNS/Non PNS" + objx.getStReference1() + " harus diisi");
            }

            String status = objx.getStReference7();
            String bpdJateng = policy.getEntity().getStRef2();
            boolean isStatusPNS = status.equalsIgnoreCase("1");
            boolean isStatusPNSJateng = status.equalsIgnoreCase("1") && bpdJateng.equalsIgnoreCase("211");

            boolean isPHKOnly = false;
            boolean isPAWOnly = false;
            boolean isResignOnly = false;
            boolean isPensiunOnly = false;
            boolean klausulaKosong = true;
            boolean semuaKlausula = false;
            boolean klausulaSelainPHK = false;
            String klausulaWajib = null;

            if(!manualKlausula){
                
                    DTOList clausulaPolis1 = policy.getClausules();
                    for (int h = 0; h < clausulaPolis1.size(); h++) {
                        InsurancePolicyClausulesView claus = (InsurancePolicyClausulesView) clausulaPolis1.get(h);

                        if (claus.isSelected()) {
                            if (claus.getStParentID().equalsIgnoreCase("2")) {
                                isPHKOnly = true;
                                klausulaKosong = false;
                            } else if (claus.getStParentID().equalsIgnoreCase("3")) {
                                isPAWOnly = true;
                                klausulaKosong = false;
                            } else if (claus.getStParentID().equalsIgnoreCase("4")) {
                                isResignOnly = true;
                                klausulaKosong = false;
                            } else if (claus.getStParentID().equalsIgnoreCase("5")) {
                                isPensiunOnly = true;
                                klausulaKosong = false;
                            }
                        }
                    }

                    if (isPHKOnly && isPAWOnly && isResignOnly && isPensiunOnly) {
                        semuaKlausula = true;
                    }

                    if (isPAWOnly && isResignOnly && isPensiunOnly && !isPHKOnly) {
                        klausulaSelainPHK = true;
                    }

                    if (isPAWOnly) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "3,7,8,9,11,13" : "3,6,7,8,10,12,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "3,6,7,8,9,11,13" : "3,6,7,8,10,12,13";
                        }
                    }

                    if (isResignOnly) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "4,7,8,9,11,13" : "4,6,7,8,10,12,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "4,6,7,8,9,11,13" : "4,6,7,8,10,12,13";
                        }
                    }

                    if (isPensiunOnly) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "5,7,8,9,11,13" : "5,6,7,8,10,12,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "5,6,7,8,9,11,13" : "5,6,7,8,10,12,13";
                        }
                    }

                    if (isPHKOnly) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "2,7,8,11,13" : "2,6,7,8,12,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "2,6,7,8,11,13" : "2,6,7,8,12,13";
                        }
                    }

                    if (semuaKlausula) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "2,3,4,5,7,13" : "2,3,4,5,6,7,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "2,3,4,5,6,7,13" : "2,3,4,5,6,7,13";
                        }
                    }

                    if (klausulaKosong) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "7,8,9,11,13" : "6,7,8,10,12,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "6,7,8,9,11,13" : "6,7,8,10,12,13";
                        }
                    }

                    if (klausulaSelainPHK) {
                        if (policy.getStCostCenterCode().equalsIgnoreCase("22")) {
                            klausulaWajib = isStatusPNSJateng ? "3,7,8,9,13" : "3,6,7,8,10,13";
                        } else {
                            klausulaWajib = isStatusPNS ? "3,6,7,8,9,13" : "3,6,7,8,10,13";
                        }
                    }

                    /*
                    logger.logDebug("+++++++++++ isPHK : "+ isPHKOnly);
                    logger.logDebug("+++++++++++ isPAWOnly : "+ isPAWOnly);
                    logger.logDebug("+++++++++++ isResignOnly : "+ isResignOnly);
                    logger.logDebug("+++++++++++ isPensiunOnly : "+ isPensiunOnly);
                    logger.logDebug("+++++++++++ semuaKlausula : "+ semuaKlausula);
                    logger.logDebug("+++++++++++ klausulaKosong : "+ klausulaKosong);
                     */

                    DTOList clausulaPolisp = policy.getClausules();
                    for (int p = 0; p < clausulaPolisp.size(); p++) {
                        InsurancePolicyClausulesView clausp = (InsurancePolicyClausulesView) clausulaPolisp.get(p);

                        if (clausp.getStParentID().equalsIgnoreCase("2") || clausp.getStParentID().equalsIgnoreCase("3")
                                || clausp.getStParentID().equalsIgnoreCase("4") || clausp.getStParentID().equalsIgnoreCase("5")) {
                            continue;
                        }

                        if (clausp.getStChildClausules() != null) {
                            if (clausp.isSelected()) {
                                clausp.setStSelectedFlag(null);
                            }
                        }

                    }

                    //logger.logDebug(">>>>>>>>>>> kalusula wajib : "+ klausulaWajib);

                    DTOList clausulaPolis = policy.getClausules();

                    for (int i = 0; i < clausulaPolis.size(); i++) {
                        InsurancePolicyClausulesView claus = (InsurancePolicyClausulesView) clausulaPolis.get(i);

                        //String filterClause = claus.getStChildClausules();

                        if (claus.getStParentID() != null) {

                            //String Clause[] = filterClause.split("[\\|]");
                            //String clausePNS[] = Clause[0].substring(2).split("[\\,]");
                            //String clauseNonPNS[] = Clause[1].substring(2).split("[\\,]");

                            String clauseApplied[] = klausulaWajib.split("[\\,]");

                            for (int k = 0; k < clauseApplied.length; k++) {
                                if (claus.getStParentID().equalsIgnoreCase(clauseApplied[k])) {
                                    claus.setStSelectedFlag("Y");
                                }
                            }
                        }
                    }
            }

            

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate){
         try {

                //applyClausules2(policy, objx, validate);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateApproval(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj, boolean validate) throws Exception{
        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        if(policy.getStEffectiveFlag()!=null)
        {
            if(policy.getStEffectiveFlag().equalsIgnoreCase("Y")){
                    if(policy.isStatusPolicy() || policy.isStatusRenewal()){

                        int jumlahHariKerja = Integer.parseInt(String.valueOf(DateUtil.daysWithoutWeekend(obj.getDtReference2(), new Date())));
                        int jumlahHariWeekend = Integer.parseInt(String.valueOf(DateUtil.weekendDays(obj.getDtReference2(), new Date())));
                        DateTime batasWaktu = new DateTime(obj.getDtReference2());
                        batasWaktu = batasWaktu.plusDays(jumlahHariKerja);
                        batasWaktu = batasWaktu.plusDays(jumlahHariWeekend - 1);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

                        int jumlahHariKalender = DateUtil.getDaysAmount(obj.getDtReference2(), policy.getDtPolicyDate());
                        DateTime tglMaks = new DateTime(new Date());
                        tglMaks = tglMaks.minusDays(limitHariSTNC);

                        /*
                        BigDecimal usiaMaksimal = new BigDecimal(65);
                        BigDecimal usiaMaksimalDB = getUsiaLimit("ACCEPT", policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID());
                        BigDecimal limitUsia = new BigDecimal(55);

                        if (!BDUtil.isZeroOrNull(usiaMaksimalDB)) {
                            usiaMaksimal = BDUtil.add(usiaMaksimalDB, new BigDecimal(15));
                            limitUsia = usiaMaksimalDB;
                        }*/

                        if(!persetujuanPusat){
                            //if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                //if(BDUtil.biggerThan(obj.getDbObjectInsuredAmount(), new BigDecimal(500000000)))
                                   // throw new RuntimeException("Maks. TSI Rp. 500.000.000 pada debitur "+obj.getStReference1());

                            if(policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(BDUtil.biggerThan(obj.getDbObjectInsuredAmount(), new BigDecimal(1000000000)))
                                    throw new RuntimeException("Maks. TSI 1 Milyar pada debitur "+obj.getStReference1());

                            /*
                            if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(!persetujuanPusat)
                                    if(usia > Integer.parseInt(String.valueOf(limitUsia)))
                                        throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh > "+ limitUsia +" Tahun");

                            if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(usiaMaks > Integer.parseInt(String.valueOf(usiaMaksimal)) && year > 15)
                                    throw new RuntimeException("Usia maks. "+ usiaMaksimal +" dan lama 15 tahun pada debitur "+obj.getStReference1());
                            */

                            String str = fmt.print(tglMaks);

                            if(!policy.isEditReasOnlyMode())
                                if(jumlahHariKalender > limitHariSTNC)
                                    if(!persetujuanPusat)
                                        throw new RuntimeException("Debitur "+ obj.getStReference1() +" melebihi batas waktu "+ limitHariSTNC +" hari kerja, maks tanggal "+ str);


                        }

                        if(!policy.isEditReasOnlyMode())
                                if(jumlahHariKalender > limitHariSTNC)
                                    if(persetujuanPusat)
                                        if(obj.getDtReference6()==null)
                                            throw new RuntimeException("Debitur "+ obj.getStReference1() +" melebihi batas waktu "+ limitHariSTNC +" hari kerja dan belum di input tanggal STNC ");
                        
                    }
            }
        }
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

    public void validateLimitPKS(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj, boolean validate) throws Exception {

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
            if(!persetujuanPusat){
                 //if(policy.getStEffectiveFlag()!=null)
                 //{
                    //if(policy.getStEffectiveFlag().equalsIgnoreCase("Y")){

                obj.setStReference21(null);

                String dokumenID = null;

                        final SQLUtil S = new SQLUtil();

                        try {
                            S.setQuery(
                                    "   select "+
                                    "   ref5,description,notes,ref6 "+
                                    "   from  "+
                                    "   ins_rates_big "+
                                    "   where "+
                                    "   rate_class = 'OM_CREDIT_LIMIT'  "+
                                    "   and ref1 = ?  "+ //--grup sumbis
                                    "   and ref2 = ?  "+ //--cabang
                                    "   and ? >= ref3::bigint and ? <= ref6::bigint "+ //--usia
                                    "   and ref4 = ?  "+ //--pekerjaan
                                    "   and ? between rate0 and rate1 "+ //tsi
                                    "   and period_start <= ? and period_end >= ? and active_flag = 'Y'"); //tsi

                            S.setParam(1, policy.getEntity().getStRef2()); //grup sumbis
                            S.setParam(2, policy.getStCostCenterCode()); // CABANG
                            S.setParam(3, String.valueOf(usiaTerdekat)); //USIA
                            S.setParam(4, String.valueOf(usiaTerdekat)); //USIA
                            S.setParam(5, obj.getStReference19()); // pekerjaan
                            S.setParam(6, obj.getDbObjectInsuredAmount()); //TSI
                            S.setParam(7, policy.getDtPolicyDate()); //tgl polis
                            S.setParam(8, policy.getDtPolicyDate()); //tgl polis

                            String message = "usia:"+usiaTerdekat+",pekerjaan:"+obj.getStReference19()+",tsi:"+ JSPUtil.printAutoPrec(obj.getDbObjectInsuredAmount());

                            final ResultSet RS = S.executeQuery();

                            if (RS.next()){

                                obj.setStReference21(RS.getString(3));
                                dokumenID = RS.getString(4);

                                if(Tools.isNo(RS.getString(1))){
                                    obj.setStReference20(obj.getStReference20() + (obj.getStReference20()!=null?"\n":"") + "Tidak termasuk automatic cover");
                                    //throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+" tidak termasuk automatic cover");
                                 }
  
                            }else{
                                obj.setStReference20(obj.getStReference20() + (obj.getStReference20()!=null?"\n":"") +"Limit kewenangan tidak ditemukan");
                                //throw new RuntimeException("Debitur an "+obj.getStReference1() +" ("+ message +") No. "+ obj.getStOrderNo()+" tidak ditemukan limit kewenangan, konfirmasi ke u/w kantor pusat");
                            }

                        }finally{
                            S.release();
                        }
                    //}
                //}
                
                 //validasi dokumen kesehatan
                 //validateDokumen(obj, dokumenID);
                
            }
        }

    }

    private void applyCoverage(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

            String coverWajib = "444";

            boolean apply = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse());

            if(apply){

                    BigDecimal NDPct = null;
                    BigDecimal PAPct = null;
                    BigDecimal PHKPct = null;
                    BigDecimal macetPct = null;

                    //APPLY PAKET JIKA PILIHAN PAKET TIDAK KOSONG
                    if (obj.getStReference13() != null && obj.getDbReference5()!=null) {

                        //DELETE COVER
                        DTOList coverr = obj.getCoverage();
                        if (obj.getCoverage().size()>0) {
                            while (obj.getCoverage().size()>0) coverr.delete(obj.getCoverage().size()-1);
                        }

                        //CEK PAKET COVERAGE
                        /*
                        if (obj.getStReference13().equalsIgnoreCase("1")) {
                            coverWajib = "568,569,570,571";
                            NDPct = new BigDecimal(0.75);
                            PAPct = new BigDecimal(0.05);
                            PHKPct = new BigDecimal(0.1);
                            macetPct = new BigDecimal(0.1);
                        } else if (obj.getStReference13().equalsIgnoreCase("2")) {
                            coverWajib = "568,569,570";
                            NDPct = new BigDecimal(0.85);
                            PAPct = new BigDecimal(0.05);
                            PHKPct = new BigDecimal(0.1);
                            macetPct = new BigDecimal(0);
                        }else if (obj.getStReference13().equalsIgnoreCase("3")) {
                            coverWajib = "568,569";
                            NDPct = new BigDecimal(0.9);
                            PAPct = new BigDecimal(0.1);
                            PHKPct = new BigDecimal(0);
                            macetPct = new BigDecimal(0);
                        }
                        */

                        //CEK JIKA ADA EKSTRA PREMI
                         if (obj.getDbReference6() != null){
                             coverWajib = coverWajib + ",572";
                         }

                        // APPLY PAKET COVER
                        String coverApplied[] = coverWajib.split("[\\,]");
                        for (int k = 0; k < coverApplied.length; k++) {

                            if (obj.getCoverage().size() < coverApplied.length) {
                                doAddLampiranCover(obj, coverApplied[k]);
                            }
                        }

                        //hitung pembagian rate
                         BigDecimal rateTotal = obj.getDbReference5();
                         DTOList coverage = obj.getCoverage();
                         for (int i = 0; i < coverage.size(); i++) {
                            InsurancePolicyCoverView cv = (InsurancePolicyCoverView) coverage.get(i);

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("444")){ //RATE ALL
                                cv.setStEntryRateFlag("Y");
                                cv.setDbRate(BDUtil.mul(rateTotal, new BigDecimal(1)));
                            }

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("568")){ //RATE ND
                                cv.setStEntryRateFlag("Y");
                                cv.setDbRate(BDUtil.mul(rateTotal, NDPct));
                            }

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("569")){ //RATE PA
                                cv.setStEntryRateFlag("Y");
                                cv.setDbRate(BDUtil.mul(rateTotal, PAPct));
                            }

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("570")){ //RATE PHK/PAW
                                cv.setStEntryRateFlag("Y");
                                cv.setDbRate(BDUtil.mul(rateTotal, PHKPct));
                            }

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("571")){ //RATE KREDIT MACET
                                cv.setStEntryRateFlag("Y");
                                cv.setDbRate(BDUtil.mul(rateTotal, macetPct));
                            }

                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("572")){
                                cv.setStEntryPremiFlag("Y");
                            }
                        }

                    }


            }            
    }

    public void onCalculateSpreading(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

            Date birth = obj.getDtReference1();
            Date perStart = obj.getDtReference2();
            Date perEnd = obj.getDtReference3();

            long age = perStart.getTime() - (birth.getTime());

            age = age / (1000l * 60l * 60l * 24l * 365l);

            if (obj.getStReference2() == null) {
                obj.setStReference2(String.valueOf(age));
            }

            DateTime birthDate = new DateTime(birth);
            DateTime startDate = new DateTime(perStart);
            DateTime endDate = new DateTime(perEnd);
            Months m = Months.monthsBetween(startDate, endDate);
            Years y = Years.yearsBetween(startDate, endDate);
            int mon = m.getMonths();
            int year = y.getYears();

            if (obj.getStReference5() == null) {
                obj.setStReference5(String.valueOf(year));
            }

            if (obj.getStReference11() == null) {
                obj.setStReference11(String.valueOf(mon));
            }

            final DTOList covers = obj.getCoverage();

            objx.reCalculate();

            //start endorse calc
            BigDecimal premiAmountDiff = null;
            if (policy.isStatusEndorse()) {
                BigDecimal insAmountDiff = null;
                BigDecimal totalInsAmountDiff = null;
                for (int l = 0; l < covers.size(); l++) {
                    InsurancePolicyCoverView icv = (InsurancePolicyCoverView) covers.get(l);

                    final InsurancePolicyCoverView refCover = icv.getRefCover();

                    if (refCover == null) {
                        //continue;
                        insAmountDiff = obj.getDbObjectInsuredAmount();
                    } else {
                        insAmountDiff = BDUtil.sub(icv.getDbInsuredAmount(), refCover.getDbInsuredAmount());
                        premiAmountDiff = BDUtil.sub(icv.getDbPremi(), refCover.getDbPremi());
                        totalInsAmountDiff = BDUtil.add(totalInsAmountDiff, insAmountDiff);
                    }
                }
                obj.setDbObjectInsuredAmount(insAmountDiff);
                obj.setDbReference2(insAmountDiff);
            }

            //end endrose
            BigDecimal totalTSI = null;
            DTOList tsiList = obj.getSuminsureds();
            for (int j = 0; j < tsiList.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(j);

                totalTSI = BDUtil.add(totalTSI, tsi.getDbInsuredAmount());
            }

            BigDecimal totalPremi = null;
            BigDecimal totalRate = null;
            for (int i = 0; i < covers.size(); i++) {
                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                totalPremi = BDUtil.add(totalPremi, cv.getDbPremi());
                totalRate = BDUtil.add(totalRate, cv.getDbRate());

            }

            if (!policy.isStatusEndorse()) {
                obj.setDbReference3(totalTSI);
                obj.setDbReference4(totalPremi);
            }

            recalculateRestitusi(policy, obj);

            //validateApproval(policy, obj);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyExtraPremi(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

            String coverWajib = "444";

            boolean apply = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse());

            if(apply){

                    //APPLY PAKET JIKA PILIHAN PAKET TIDAK KOSONG
                    if (obj.getDbReference6() != null) {

                        //DELETE COVER tambahan
                        DTOList coverr = obj.getCoverage();
                        if (obj.getCoverage().size()>1) {
                            while (obj.getCoverage().size()>1) coverr.delete(obj.getCoverage().size()-1);
                        }

                        //CEK JIKA ADA EKSTRA PREMI
                         if (obj.getDbReference6() != null){
                             coverWajib = "572";
                         }

                        // APPLY PAKET COVER
                        String coverApplied[] = coverWajib.split("[\\,]");
                        for (int k = 0; k < coverApplied.length; k++) {

                            //if (obj.getCoverage().size() < coverApplied.length) {
                                doAddLampiranCover(obj, coverApplied[k]);
                            //}
                        }

                        BigDecimal rateTotal = null;
                        DTOList coverageAsli = obj.getCoverage();
                         for (int h = 0; h < coverageAsli.size(); h++) {
                            InsurancePolicyCoverView cvAsli = (InsurancePolicyCoverView) coverageAsli.get(h);

                            rateTotal = BDUtil.add(rateTotal, cvAsli.getDbRate());
                        }

                        //hitung pembagian rate
                         DTOList coverage = obj.getCoverage();
                         for (int i = 0; i < coverage.size(); i++) {
                            InsurancePolicyCoverView cv = (InsurancePolicyCoverView) coverage.get(i);


                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("572")){
                                cv.setStEntryPremiFlag("Y");
                            }
                        }

                    }


            }
    }

    public void validateRateJual(String lama, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy,DTOList covers) throws Exception{

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        BigDecimal rateJual1 = null;

        if(Integer.parseInt(lama)==0) lama = String.valueOf(1);

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            if(!persetujuanPusat){
                //get rate jual
                final SQLUtil S = new SQLUtil();
                    try {

                        String sql =  " select rate"+ lama +
                                      " from ins_rates_big "+
                                      "  where ref1= ? "+ //--grup sumbis
                                      "  and ref2 = ? "+ //--cabang
                                      "  and ref3 = ? "+ //--tipe coverage
                                      "  and ? >= ref5::bigint and ? <= ref6::bigint "+ //--usia
                                      "  and ref4 = ? "+ //--pekerjaan
                                      "  and period_start <= ? and period_end >= ? "+
                                      "  and rate_class = 'OM_CREDIT_RATE' and active_flag = 'Y'";

                        final PreparedStatement PS = S.setQuery(sql);

                        int n=1;

                        PS.setString(n++, policy.getEntity().getStRef2()); //grup sumbis
                        PS.setString(n++, policy.getStCostCenterCode()); //cabang
                        PS.setString(n++, obj.getStReference13()); //coverage
                        PS.setString(n++, String.valueOf(usiaTerdekat)); //usia
                        PS.setString(n++, String.valueOf(usiaTerdekat)); //usia
                        PS.setString(n++, obj.getStReference19()); //pekerjaan

                        S.setParam(n++,policy.getDtPolicyDate()); //period start
                        S.setParam(n++,policy.getDtPolicyDate()); //period end

                        logger.logInfo("############# grup sumbis : "+ policy.getEntity().getStRef2());
                        logger.logInfo("############# cabang : "+ policy.getStCostCenterCode());
                        logger.logInfo("############# coverage : "+ obj.getStReference13());
                        logger.logInfo("############# usia : "+ usiaTerdekat);
                        logger.logInfo("############# pekerjaan : "+ obj.getStReference19());
                        logger.logInfo("############# periode : "+ policy.getDtPolicyDate());

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()){
                            rateJual1 = RS.getBigDecimal(1);
                        }else{
                            logger.logInfo("##################### gak ketemu rate nya.... ");
                            //rateJual1 = BDUtil.zero;
                            //throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+" tidak ditemukan rate jual, konfirmasi ke u/w kantor pusat");
                            obj.setStReference20("Rate jual tidak ditemukan");
                        }

                    } finally {
                        S.release();
                    }

                   if (covers!=null && rateJual1!=null) {
                       BigDecimal totalRate = null;
                            for(int i=0;i<covers.size();i++){
                                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                                if(!cv.getInsuranceCoveragePolType().isMainCoverage()) continue;

                                BigDecimal rateInput = cv.getDbRate(); 

                                if(cv.getDbRate()==null){
                                    rateInput = BDUtil.mul(BDUtil.div(cv.getDbPremi(), obj.getDbObjectInsuredAmount(),5), BDUtil.thousand);
                                }

                                totalRate = BDUtil.add(totalRate, rateInput);
                            }

                            if(BDUtil.lesserThan(totalRate, rateJual1)){
                                //throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+ " Rate jual "+ totalRate +" tidak boleh < "+rateJual1);
                                obj.setStReference20("Rate jual lebih kecil dari rate "+ rateJual1);
                            }
                    }
            }
        }

        //validasi data top up
        if(!persetujuanPusat){
            if(obj.isDataTopUp()){

                boolean isiPremiRestitusi = false;

                if (covers!=null){
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        if(cv.getStInsuranceCoverID().equalsIgnoreCase("310"))
                            if(!BDUtil.isZeroOrNull(cv.getDbPremiNew()))
                                isiPremiRestitusi = true;
                    }
                }

                if(!isiPremiRestitusi)
                    throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+ " (Data Top Up) harus input nilai premi restitusi");
            }
        }
          
    }


    public void applyWarranty(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception {

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        String warranty = "";

        if (!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()
                && !policy.isStatusEndorseRI()) {

            if (!persetujuanPusat) {
                if (policy.getStWarranty() == null) {
                    //get warranty
                    final SQLUtil S = new SQLUtil();
                    try {

                            String sql = " select notes"
                                    + " from ins_rates_big "
                                    + "  where ref2 = ? " + //--cabang
                                    "  and ref3 = ? " + //--jenis asuransi
                                    "  and ref4 = ? " + //--group company
                                    "  and period_start <= ? and period_end >= ? "+
                                    "  and rate_class = 'OM_WARRANTY' and active_flag = 'Y'";

                            final PreparedStatement PS = S.setQuery(sql);

                            int n = 1;

                            PS.setString(n++, policy.getStCostCenterCode()); //cabang
                            PS.setString(n++, policy.getStPolicyTypeID()); //jenis asuransi
                            PS.setString(n++, policy.getEntity().getStRef2()); //group company
                            S.setParam(n++, policy.getDtPolicyDate()); //period start
                            S.setParam(n++, policy.getDtPolicyDate()); //period end

                            final ResultSet RS = PS.executeQuery();

                            if (RS.next()) {
                                warranty = RS.getString(1);
                            }

                    } finally {
                        S.release();
                    }

                    if (policy.getStWarranty() == null) {
                        if(!warranty.equalsIgnoreCase(""))
                                policy.setStWarranty(warranty);
                    }
                }


                boolean validate = true;

                if(policy.isPolisH2H()) validate = false;
                if(policy.isDataInterkoneksi() && policy.isStatusDraft()) validate = false;

                if(validate){
                    if(warranty.equalsIgnoreCase("") && policy.getStWarranty() == null){
                        String notif = "Wajib dientry warranty mengacu pada PKS yang berlaku,<br>Jika terdapat kondisi PKS tersebut belum difinalisasikan (masih dalam proses pembuatan PKS)<br> "
                                + "maka warranty akan mengacu kepada Covernote atau SKP";

                        throw new RuntimeException(notif);
                    }
                }
                

            }
        }

    }

    public void validateDokumen(InsurancePolicyObjDefaultView obj, String dokumenID) throws Exception{

        final DTOList dokumen = obj.getDetailDocuments();

        String [] dokumenIDArray = dokumenID.split(",");

        if(dokumen!=null){
            for (int i = 0; i < dokumen.size(); i++) {
            InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) dokumen.get(i);

            for (int j = 0; j < dokumenIDArray.length; j++) {
                String idDokumen = dokumenIDArray[j];

                if(doc.getStInsuranceDocumentTypeID().equalsIgnoreCase(idDokumen)){
                    if(!doc.isMarked())
                        throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+ " Dokumen "+ doc.getStDescription() + " wajib di centang & upload");
                }


            }

        }
        }
        

    }

    private DTOList tc;

    public DTOList getTC(String pekerjaan) {
        loadTC(pekerjaan);
        return tc;
    }

    public void loadTC(String pekerjaan) {
        try {
            
                tc = ListUtil.getDTOListFromQuery(
                        "select * "+
                        " from ins_rates_tc "+
                        " where kode_pekerjaan = ?",
                        new Object[]{pekerjaan},
                        InsuranceRatesTCView.class);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void applyTC(String lama, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        BigDecimal rateJual1 = null;

        if(Integer.parseInt(lama)==0) lama = String.valueOf(1);

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            if(!persetujuanPusat){
                //get rate jual

                logger.logDebug("############# PEKERJAAN = "+ obj.getStReference7());

                DTOList tcList = getTC(obj.getStReference7());

                final DTOList covers = obj.getCoverage();

                if(tc!=null){
                    //dapatin tc nya
                    InsuranceRatesTCView tc = (InsuranceRatesTCView) tcList.get(0);

                    logger.logDebug("############# TC = "+ tc.getStInsuranceTCID() +" "+ tc.getStDescription() +" " + tc.getDbRatePermil());

                    BigDecimal rateTenor = BDUtil.mul(tc.getDbRatePermil(), new BigDecimal(lama),2);

                    BigDecimal rateKoasTenor = BDUtil.mul(tc.getDbRateKoasPermil(), new BigDecimal(lama),2);

                    if (covers!=null && tc!=null) {

                       BigDecimal totalRate = null;

                            for(int i=0;i<covers.size();i++){
                                InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                                if(cv.isAutoRate()){
                                    cv.setStEntryRateFlag("Y");
                                    cv.setDbRate(rateTenor);
                                }
                            }
                    }

                    //apply hitungan koas
                    /*
                    if(obj.getStReference27()!=null){

                        obj.setDbReference9(rateKoasTenor);

                        final DTOList sumInsured = obj.getSuminsureds();

                        for (int i = 0; i < sumInsured.size(); i++) {
                            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) sumInsured.get(i);

                            BigDecimal premiKoas = BDUtil.mul(tsi.getDbInsuredAmount(), BDUtil.getRateFromMile(rateKoasTenor), 0);

                            obj.setDbReference10(premiKoas);
                        }
                    }*/
                }                
            }
        }

    }

    public void recalculateJiwaViaAPI(InsurancePolicyView pol, InsurancePolicyObjDefaultView obj) throws Exception{


        String koasJiwa = "";

        final DTOList coins = pol.getCoins();

        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

            if(!co.isAskrida()){
                koasJiwa = co.getStEntityID();
            }

        }

        logger.logDebug("############### KODE KOAS JIWA = " + koasJiwa);

        //set koas jiwa ke detail
        if(!koasJiwa.equalsIgnoreCase("")){
            obj.setStReference27(koasJiwa);
        }

        //JIKA KODE KOAS JIWA DIISI, CEK KE API JIWA
        if(obj.getStReference27()!=null){
                try {

                        logger.logDebug("############### HIT API KALKULATOR PREMI QOALA ####################### ");

                        URL url = new URL(kalkulatorPremiJiwaEndPoint);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");

                        EntityView ent = pol.getEntity();

                        final DTOList suminsured = obj.getSuminsureds();

                        BigDecimal plafond = BDUtil.zero;

                        for (int i = 0; i < suminsured.size(); i++) {
                            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsured.get(i);

                            plafond = tsi.getDbInsuredAmount();
                        }

                        String jsonRequest = "{"+
                                       "\"kode_bank\": \""+ ent.getCompany().getStVSCode() +"\","+
                                       "\"kode_bank_cabang\": \""+ ent.getStEntityID() +"\","+
                                       "\"nomor_loan\": \""+ obj.getStReference16() +"\","+
                                       "\"nama_debitur\": \""+ obj.getStReference1() +"\","+
                                       "\"tgl_lahir\": \""+ DateUtil.getDateJson(obj.getDtReference1()) +"\","+
                                       "\"tgl_awal_kredit\": \""+ DateUtil.getDateJson(obj.getDtReference2()) +"\","+
                                       "\"tgl_perjanjian_kredit\": \""+ DateUtil.getDateJson(obj.getDtReference2()) +"\","+
                                       "\"tgl_jatuh_tempo\": \""+ DateUtil.getDateJson(obj.getDtReference3()) +"\","+
                                       "\"plafond_kredit\": "+ plafond +","+
                                       "\"kode_produk_jiwa\": \"M-CRDL-ACME-001\""+
                                       "}";

                        logger.logDebug("############## JSON REQUEST ASKRIDA = " + jsonRequest);

                        // For POST only - START
                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(jsonRequest.getBytes());
                            os.flush();
                            os.close();
                            // For POST only - END

                            int responseCode = conn.getResponseCode();
                            logger.logDebug("POST Response Code :: " + responseCode);

                            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                    }
                                    in.close();

                                    // print result
                                    logger.logDebug("############### BALIKAN QOALA ####################### ");
                                    logger.logDebug("############### RESPONSE QOALA = "+ response.toString());

                                    JSONParser parser = new JSONParser();
                                    JSONObject json = (JSONObject) parser.parse(response.toString());
                                    logger.logDebug("code = "+ json.get("code"));
                                    logger.logDebug("status_hit = "+ json.get("status"));
                                    logger.logDebug("data = "+ json.get("data"));
                                    logger.logDebug("keterangan = "+ json.get("message"));

                                    JSONObject jsonData = (JSONObject) parser.parse(json.get("data").toString());

                                    logger.logDebug("############### DATA DETIL QOALA ####################### ");
                                    logger.logDebug("status = "+ jsonData.get("status"));
                                    logger.logDebug("premi rate = "+ jsonData.get("premi_rate"));
                                    logger.logDebug("premi = "+ jsonData.get("premi"));

                                    BigDecimal rateJiwa = new BigDecimal(jsonData.get("premi_rate").toString());
                                    BigDecimal premiJiwa = new BigDecimal(jsonData.get("premi").toString());
                                    String statusPengajuan = jsonData.get("status").toString();

                                    //apply hitungan koas
                                    if(obj.getStReference27()!=null){

                                        if(statusPengajuan.equalsIgnoreCase("00")){
                                            obj.setDbReference9(rateJiwa);
                                            obj.setDbReference10(premiJiwa);
                                            obj.setStReference28(statusPengajuan);
                                            obj.setStReference29(json.get("message").toString());
                                        }

                                        if(statusPengajuan.equalsIgnoreCase("01")){
                                            obj.setDbReference9(null);
                                            obj.setDbReference10(null);
                                            obj.setStReference28("03");
                                            obj.setStReference29(jsonData.get("keterangan").toString());
                                        }

                                        if(statusPengajuan.equalsIgnoreCase("03")){
                                            obj.setDbReference9(null);
                                            obj.setDbReference10(null);
                                            obj.setStReference28(statusPengajuan);
                                            obj.setStReference29(jsonData.get("keterangan").toString());
                                        }


                                        /*
                                        final DTOList sumInsured = obj.getSuminsureds();

                                        for (int i = 0; i < sumInsured.size(); i++) {
                                            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) sumInsured.get(i);

                                            //BigDecimal premiKoas = BDUtil.mul(tsi.getDbInsuredAmount(), BDUtil.getRateFromMile(rateKoasTenor), 0);

                                            obj.setDbReference10(premiJiwa);
                                        }*/
                                    }

                            } else {
                                    System.out.println("POST request did not work.");
                                    obj.setStReference29("Gagal dapat response dari API Jiwa");

                            }

                            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
                                 System.out.println("POST request did not work.");
                                 obj.setStReference29("HTTP 500 Internal Server Error - Gagal dapat response dari API Kalkulator Premi Asuransi Jiwa");
                            }

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP Error code : "
                                    + conn.getResponseCode());
                        }

                        conn.disconnect();

                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }

        
    }

    public void recalculateCoas(InsurancePolicyView policy) throws Exception{

        if(policy.isCoinsurance()){

            logger.logDebug("################### HITUNG KOAS JENIS KOAS LEADER");

            final DTOList oj = policy.getObjects();

            BigDecimal totalPremiCoas = BDUtil.zero;

            for (int i = 0; i < oj.size(); i++)
            {
                InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);

                InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;

                totalPremiCoas = BDUtil.add(totalPremiCoas, obje.getDbReference10());
            }

            final DTOList coins = policy.getCoins();

            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(i);

                if(!co.isAskrida()){
                    co.setDbPremiAmount(totalPremiCoas);
                }

            }
        }

    }

    private void validateApprovalCoas(InsurancePolicyView policy, boolean validate) throws Exception{

        if(policy.getStEffectiveFlag()!=null)
        {
            if(policy.getStEffectiveFlag().equalsIgnoreCase("Y")){
                    if(policy.isStatusPolicy() || policy.isStatusRenewal()){
                        boolean canApprove = true;

                        final DTOList oj = policy.getObjects();

                        for (int i = 0; i < oj.size(); i++)
                        {
                            InsurancePolicyObjectView oje = (InsurancePolicyObjectView) oj.get(i);

                            InsurancePolicyObjDefaultView obje = (InsurancePolicyObjDefaultView) oje;

                            //jika ada koas jiwa, cek cac atau cbc
                            if(obje.getStReference27()!=null){
                                if(obje.getStReference28()!=null){
                                    //JIKA ADA YG SELAIN CAC, MAKA TIDAK BS SETUJUI
                                    if(!obje.getStReference28().equalsIgnoreCase("00")){
                                        throw new RuntimeException("Debitur "+ obje.getStReference1() +" No Urut "+ obje.getStOrderNo()+ " Status Jiwa Bukan CAC, tidak bisa setujui polis");
                                    }
                                }
                            }
                        }
                    }
            }
        }


    }

    private void calculateSimulasiPOJK20(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        //HITUNG JUMLAH POLIS DR JANGKA WAKTU
        int tahun = DateUtil.getYearsBetweenHUTB(obj.getDtReference7(), obj.getDtReference8());

        BigDecimal jmlPolis = BDUtil.divRoundUpFix(new BigDecimal(tahun), new BigDecimal(5), 0);

        int jumlahPolis = jmlPolis.intValue();

        logger.logDebug("####################### jumlah polis = "+ jumlahPolis);

        BigDecimal premi100 = obj.getDbReference11();
        BigDecimal premiNetto = obj.getDbReference26();

        //tambah extra premi
        if(obj.getDbReference7()!=null)
            premi100 = BDUtil.add(premi100, obj.getDbReference7());

        //tambah premi restitusi
        if(obj.getDbReference25()!=null)
            premi100 = BDUtil.add(premi100, obj.getDbReference25());

        //SET PROSENTASE PREMI PER POLIS
        getKomposisiPremiSerbaguna(String.valueOf(tahun), obj, policy);

        resetFieldSimulasi(obj, policy);
        
        if(jumlahPolis == 1){

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference9(obj.getDtReference2());
            //obj.setDtReference10(obj.getDtReference3());
            obj.setDtReference10(obj.getDtReference8());

            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1
        }

        if(jumlahPolis == 2){
            obj.setDtReference9(obj.getDtReference2());
            obj.setDtReference10(DateUtil.addYear(obj.getDtReference2(), 5));

            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0));//premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference11(obj.getDtReference10());
            //obj.setDtReference12(DateUtil.addYear(obj.getDtReference11(), sisa));
            obj.setDtReference12(obj.getDtReference8());
            obj.setDbReference14(getPctKomposisi2());
            obj.setDbReference15(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference14()), 0));//premi polis ke 2

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference28(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference14()), 0)); //premi net polis ke 2

        }

        if(jumlahPolis == 3){

            obj.setDtReference9(obj.getDtReference2());
            obj.setDtReference10(DateUtil.addYear(obj.getDtReference2(), 5));
            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0));//premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1

            obj.setDtReference11(obj.getDtReference10());
            obj.setDtReference12(DateUtil.addYear(obj.getDtReference11(), 5));
            obj.setDbReference14(getPctKomposisi2());
            obj.setDbReference15(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference14()), 0));//premi polis ke 2

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference28(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference14()), 0)); //premi net polis ke 2

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference13(obj.getDtReference12());
            //obj.setDtReference14(DateUtil.addYear(obj.getDtReference13(), sisa));
            obj.setDtReference14(obj.getDtReference8());
            obj.setDbReference16(getPctKomposisi3());
            obj.setDbReference17(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference16()), 0));//premi polis ke 3

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference29(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference16()), 0)); //premi net polis ke 3

        }

        if(jumlahPolis == 4){

            obj.setDtReference9(obj.getDtReference2());
            obj.setDtReference10(DateUtil.addYear(obj.getDtReference2(), 5));
            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0));//premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1

            obj.setDtReference11(obj.getDtReference10());
            obj.setDtReference12(DateUtil.addYear(obj.getDtReference11(), 5));
            obj.setDbReference14(getPctKomposisi2());
            obj.setDbReference15(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference14()), 0));//premi polis ke 2

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference28(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference14()), 0)); //premi net polis ke 2

            obj.setDtReference13(obj.getDtReference12());
            obj.setDtReference14(DateUtil.addYear(obj.getDtReference13(), 5));
            obj.setDbReference16(getPctKomposisi3());
            obj.setDbReference17(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference16()), 0));//premi polis ke 3

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference29(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference16()), 0)); //premi net polis ke 3

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference15(obj.getDtReference14());
            //obj.setDtReference16(DateUtil.addYear(obj.getDtReference15(), sisa));
            obj.setDtReference16(obj.getDtReference8());
            obj.setDbReference18(getPctKomposisi4());
            obj.setDbReference19(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference18()), 0));//premi polis ke 4

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference30(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference18()), 0)); //premi net polis ke 4

            //JIKA BANK NAGARI & 4 POLIS
            if(policy.getEntity().getStRef2().equalsIgnoreCase("203")){
                BigDecimal premiTotalCek = BDUtil.zero;
                premiTotalCek = BDUtil.add(obj.getDbReference13(), obj.getDbReference15());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference17());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference19());

                BigDecimal selisih = BDUtil.sub(premi100, premiTotalCek);

                if(!BDUtil.isZero(selisih))
                    obj.setDbReference19(BDUtil.add(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference18()), 0),selisih));//premi polis ke 4
            }

        }

        if(jumlahPolis == 5){

            obj.setDtReference9(obj.getDtReference2());
            obj.setDtReference10(DateUtil.addYear(obj.getDtReference2(), 5));
            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0));//premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1

            obj.setDtReference11(obj.getDtReference10());
            obj.setDtReference12(DateUtil.addYear(obj.getDtReference11(), 5));
            obj.setDbReference14(getPctKomposisi2());
            obj.setDbReference15(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference14()), 0));//premi polis ke 2

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference28(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference14()), 0)); //premi net polis ke 2


            obj.setDtReference13(obj.getDtReference12());
            obj.setDtReference14(DateUtil.addYear(obj.getDtReference13(), 5));
            obj.setDbReference16(getPctKomposisi3());
            obj.setDbReference17(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference16()), 0));//premi polis ke 3

             if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference29(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference16()), 0)); //premi net polis ke 3

            obj.setDtReference15(obj.getDtReference14());
            obj.setDtReference16(DateUtil.addYear(obj.getDtReference15(), 5));
            obj.setDbReference18(getPctKomposisi4());
            obj.setDbReference19(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference18()), 0));//premi polis ke 4

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference30(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference18()), 0)); //premi net polis ke 4

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference17(obj.getDtReference16());
            //obj.setDtReference18(DateUtil.addYear(obj.getDtReference17(), sisa));
            obj.setDtReference18(obj.getDtReference8());
            obj.setDbReference20(getPctKomposisi5());
            obj.setDbReference21(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference20()), 0));//premi polis ke 5

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference31(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference20()), 0)); //premi net polis ke 5

            //JIKA BANK NAGARI & 4 POLIS
            if(policy.getEntity().getStRef2().equalsIgnoreCase("203")){

                BigDecimal premiTotalCek = BDUtil.zero;
                premiTotalCek = BDUtil.add(obj.getDbReference13(), obj.getDbReference15());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference17());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference19());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference21());

                BigDecimal selisih = BDUtil.sub(premi100, premiTotalCek);

                if(!BDUtil.isZero(selisih))
                    obj.setDbReference21(BDUtil.add(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference20()), 0),selisih));//premi polis ke 5
            }
        }

        if(jumlahPolis == 6){

            obj.setDtReference9(obj.getDtReference2());
            obj.setDtReference10(DateUtil.addYear(obj.getDtReference2(), 5));
            obj.setDbReference12(getPctKomposisi1());
            obj.setDbReference13(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference12()), 0));//premi polis ke 1

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference27(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference12()), 0)); //premi net polis ke 1

            obj.setDtReference11(obj.getDtReference10());
            obj.setDtReference12(DateUtil.addYear(obj.getDtReference11(), 5));
            obj.setDbReference14(getPctKomposisi2());
            obj.setDbReference15(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference14()), 0));//premi polis ke 2

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference28(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference14()), 0)); //premi net polis ke 2


            obj.setDtReference13(obj.getDtReference12());
            obj.setDtReference14(DateUtil.addYear(obj.getDtReference13(), 5));
            obj.setDbReference16(getPctKomposisi3());
            obj.setDbReference17(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference16()), 0));//premi polis ke 3

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference29(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference16()), 0)); //premi net polis ke 3

            obj.setDtReference15(obj.getDtReference14());
            obj.setDtReference16(DateUtil.addYear(obj.getDtReference15(), 5));
            obj.setDbReference18(getPctKomposisi4());
            obj.setDbReference19(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference18()), 0));//premi polis ke 4

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference30(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference18()), 0)); //premi net polis ke 4

            obj.setDtReference17(obj.getDtReference16());
            obj.setDtReference18(DateUtil.addYear(obj.getDtReference17(), 5));
            obj.setDbReference20(getPctKomposisi5());
            obj.setDbReference21(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference20()), 0));//premi polis ke 5

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference31(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference20()), 0)); //premi net polis ke 5

            int mod = tahun % 5;
            int sisa = 0;

            if(mod==0) sisa = 5;
            else sisa = mod;

            obj.setDtReference19(obj.getDtReference18());
            //obj.setDtReference20(DateUtil.addYear(obj.getDtReference19(), sisa));
            obj.setDtReference20(obj.getDtReference8());
            obj.setDbReference22(getPctKomposisi6());
            obj.setDbReference23(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference22()), 0));//premi polis ke 6

            if(!BDUtil.isZeroOrNull(premiNetto))
                obj.setDbReference32(BDUtil.mul(premiNetto, BDUtil.getRateFromPct(obj.getDbReference22()), 0)); //premi net polis ke 6

            //JIKA BANK NAGARI & 6 POLIS
            if(policy.getEntity().getStRef2().equalsIgnoreCase("203")){

                BigDecimal premiTotalCek = BDUtil.zero;
                premiTotalCek = BDUtil.add(obj.getDbReference13(), obj.getDbReference15());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference17());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference19());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference21());
                premiTotalCek = BDUtil.add(premiTotalCek, obj.getDbReference23());

                BigDecimal selisih = BDUtil.sub(premi100, premiTotalCek);

                if(!BDUtil.isZero(selisih))
                    obj.setDbReference23(BDUtil.add(BDUtil.mul(premi100, BDUtil.getRateFromPct(obj.getDbReference22()), 0),selisih));//premi polis ke 6
            }
        }

        //set periode polis jd 5 thn
        obj.setDtReference2(obj.getDtReference9());
        obj.setDtReference3(obj.getDtReference10());

        final DTOList covers = obj.getCoverage();

        for (int i = 0; i < covers.size(); i++) {
            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(i);

            if(cov.getStInsuranceCoverID().equalsIgnoreCase("238")){
                cov.setStEntryRateFlag(null);
                cov.setStEntryPremiFlag("Y");
                cov.setDbPremi(obj.getDbReference13());
                cov.setDbPremiNew(obj.getDbReference13());
            }
            
        }

        calculateOutgo(policy, obj);

    }

    public void getKomposisiPremiSerbaguna(String tahun, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        //boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        if(!policy.isStatusHistory() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            //if(!persetujuanPusat){
                //get rate jual
                final SQLUtil S = new SQLUtil();
                    try {

                        String sql =  " select rate1, rate2, rate3, rate4, rate5, rate6 "+
                                      " from ins_rates_big "+
                                      "  where rate_class = 'CREDIT_PROSENTASE' and ref1 = ? and (ref2 = ? or ref2 is null) "+
                                      "  and period_start <= ? and period_end >= ? "+
                                      "  and active_flag = 'Y'"+
                                      "  order by ref2 limit 1";

                        final PreparedStatement PS = S.setQuery(sql);

                        int n=1;

                        PS.setString(n++, tahun); //jumlah polis
                        PS.setString(n++, policy.getEntity().getStRef2()); // grup customer
                        S.setParam(n++, obj.getDtReference7()); //period start
                        S.setParam(n++, obj.getDtReference7()); //period end

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()){

                            pctKomposisi1 = RS.getBigDecimal(1);
                            pctKomposisi2 = RS.getBigDecimal(2);
                            pctKomposisi3 = RS.getBigDecimal(3);
                            pctKomposisi4 = RS.getBigDecimal(4);
                            pctKomposisi5 = RS.getBigDecimal(5);
                            pctKomposisi6 = RS.getBigDecimal(6);
                        }

                    } finally {
                        S.release();
                    }

            //}
        }

    }

    public void resetFieldSimulasi(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            obj.setDtReference9(null);
            obj.setDtReference10(null);
            obj.setDbReference12(null);
            obj.setDbReference13(null);//premi polis ke 1
            obj.setDbReference27(null);//premi net polis ke 1

            obj.setDtReference11(null);
            obj.setDtReference12(null);
            obj.setDbReference14(null);
            obj.setDbReference15(null);//premi polis ke 2
            obj.setDbReference28(null);//premi net polis ke 2


            obj.setDtReference13(null);
            obj.setDtReference14(null);
            obj.setDbReference16(null);
            obj.setDbReference17(null);//premi polis ke 3
            obj.setDbReference29(null);//premi net polis ke 3

            obj.setDtReference15(null);
            obj.setDtReference16(null);
            obj.setDbReference18(null);
            obj.setDbReference19(null);//premi polis ke 4
            obj.setDbReference30(null);//premi net polis ke 4

            obj.setDtReference17(null);
            obj.setDtReference18(null);
            obj.setDbReference20(null);
            obj.setDbReference21(null);//premi polis ke 5
            obj.setDbReference31(null);//premi net polis ke 5

            obj.setDtReference19(null);
            obj.setDtReference20(null);
            obj.setDbReference22(null);
            obj.setDbReference23(null);//premi polis ke 6
            obj.setDbReference32(null);//premi net polis ke 6
        }
     
    }

    public void setBlankFieldSimulasi(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        if(policy.isStatusEndorse()){

            obj.setDtReference9(null);
            obj.setDtReference10(null);
            obj.setDbReference12(null);
            obj.setDbReference13(null);//premi polis ke 1
            obj.setDbReference27(null);//premi net polis ke 1

            obj.setDtReference11(null);
            obj.setDtReference12(null);
            obj.setDbReference14(null);
            obj.setDbReference15(null);//premi polis ke 2
            obj.setDbReference28(null);//premi net polis ke 2


            obj.setDtReference13(null);
            obj.setDtReference14(null);
            obj.setDbReference16(null);
            obj.setDbReference17(null);//premi polis ke 3
            obj.setDbReference29(null);//premi net polis ke 3

            obj.setDtReference15(null);
            obj.setDtReference16(null);
            obj.setDbReference18(null);
            obj.setDbReference19(null);//premi polis ke 4
            obj.setDbReference30(null);//premi net polis ke 4

            obj.setDtReference17(null);
            obj.setDtReference18(null);
            obj.setDbReference20(null);
            obj.setDbReference21(null);//premi polis ke 5
            obj.setDbReference31(null);//premi net polis ke 5

            obj.setDtReference19(null);
            obj.setDtReference20(null);
            obj.setDbReference22(null);
            obj.setDbReference23(null);//premi polis ke 6
            obj.setDbReference32(null);//premi net polis ke 6
        }

    }

     public void calculateOutgo(InsurancePolicyView policy,InsurancePolicyObjDefaultView obj) throws Exception{

        BigDecimal premi100 = obj.getDbReference11();

        final DTOList details = policy.getDetails();
        BigDecimal brokerfee = BDUtil.zero;
        BigDecimal feebase = BDUtil.zero;
        BigDecimal komisi = BDUtil.zero;

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView it = (InsurancePolicyItemsView) details.get(i);

            //reset
            it.setDbAmount100(null);
            it.setDbTaxAmount100(null);
            it.setDbAmount1(null);
            it.setDbTaxAmount1(null);
            it.setDbAmount2(null);
            it.setDbTaxAmount2(null);
            it.setDbAmount3(null);
            it.setDbTaxAmount3(null);
            it.setDbAmount4(null);
            it.setDbTaxAmount4(null);
            it.setDbAmount5(null);
            it.setDbTaxAmount5(null);
            it.setDbAmount6(null);
            it.setDbTaxAmount6(null);

            it.setDbAmount100(BDUtil.mul(premi100, it.getDbRatePct(), 0));

            if(it.isBrokerFeeIncludePPN() || it.isPPNInclude()){
                it.setDbAmount100(BDUtil.mul(premi100, it.getDbRatePct(), 0));
                it.setDbAmount100(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount100(), 0));
            }

            if(it.isBrokerFee()||it.isBrokerFeeIncludePPN()){
                brokerfee = BDUtil.add(brokerfee, it.getDbAmount100());
            }

            if(it.isFeeBase()){
                feebase = BDUtil.add(feebase, it.getDbAmount100());
            }

            if(it.isKomisi2()){
                komisi = BDUtil.add(komisi, it.getDbAmount100());
            }

            it.setDbTaxAmount100(BDUtil.mul(it.getDbAmount100(), it.getDbTaxRate(), 0));

            it.setDbAmount1(BDUtil.mul(obj.getDbReference13(), it.getDbRatePct(), 0));

            if(it.isPPNInclude()){
                it.setDbAmount1(BDUtil.mul(obj.getDbReference13(), it.getDbRatePct(), 0));
                it.setDbAmount1(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount1(), 0));
            }

            it.setDbTaxAmount1(BDUtil.mul(it.getDbAmount1(), it.getDbTaxRate(), 0));

            it.setDbAmount2(BDUtil.mul(obj.getDbReference15(), it.getDbRatePct(), 0));
            if(it.isPPNInclude()){
                it.setDbAmount2(BDUtil.mul(obj.getDbReference15(), it.getDbRatePct(), 0));
                it.setDbAmount2(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount2(), 0));
            }
            it.setDbTaxAmount2(BDUtil.mul(it.getDbAmount2(), it.getDbTaxRate(), 0));

            it.setDbAmount3(BDUtil.mul(obj.getDbReference17(), it.getDbRatePct(), 0));
            if(it.isPPNInclude()){
                it.setDbAmount3(BDUtil.mul(obj.getDbReference17(), it.getDbRatePct(), 0));
                it.setDbAmount3(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount3(), 0));
            }
            it.setDbTaxAmount3(BDUtil.mul(it.getDbAmount3(), it.getDbTaxRate(), 0));

            it.setDbAmount4(BDUtil.mul(obj.getDbReference19(), it.getDbRatePct(), 0));
            if(it.isPPNInclude()){
                it.setDbAmount4(BDUtil.mul(obj.getDbReference19(), it.getDbRatePct(), 0));
                it.setDbAmount4(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount4(), 0));
            }
            it.setDbTaxAmount4(BDUtil.mul(it.getDbAmount4(), it.getDbTaxRate(), 0));

            it.setDbAmount5(BDUtil.mul(obj.getDbReference21(), it.getDbRatePct(), 0));
            if(it.isPPNInclude()){
                it.setDbAmount5(BDUtil.mul(obj.getDbReference21(), it.getDbRatePct(), 0));
                it.setDbAmount5(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount5(), 0));
            }
            it.setDbTaxAmount5(BDUtil.mul(it.getDbAmount5(), it.getDbTaxRate(), 0));

            it.setDbAmount6(BDUtil.mul(obj.getDbReference23(), it.getDbRatePct(), 0));
            if(it.isPPNInclude()){
                it.setDbAmount6(BDUtil.mul(obj.getDbReference23(), it.getDbRatePct(), 0));
                it.setDbAmount6(BDUtil.mul(BDUtil.div(it.getInsItem().getDbCalculationFactor1(), it.getInsItem().getDbCalculationFactor2(), 15), it.getDbAmount6(), 0));
            }
            it.setDbTaxAmount6(BDUtil.mul(it.getDbAmount6(), it.getDbTaxRate(), 0));

            //ppn brokerfee
            if(it.isPPN()){
                it.setDbAmount100(BDUtil.mul(brokerfee, it.getDbRatePct(), 0));
                it.setDbAmount1(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference12()), brokerfee,0), it.getDbRatePct(), 0));
                it.setDbAmount2(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference14()), brokerfee,0), it.getDbRatePct(), 0));
                it.setDbAmount3(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference16()), brokerfee,0), it.getDbRatePct(), 0));
                it.setDbAmount4(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference18()), brokerfee,0), it.getDbRatePct(), 0));
                it.setDbAmount5(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference20()), brokerfee,0), it.getDbRatePct(), 0));
                it.setDbAmount6(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference22()), brokerfee,0), it.getDbRatePct(), 0));
            }

            //ppn feebase
            if(it.isPPNFeeBase()){
                it.setDbAmount100(BDUtil.mul(feebase, it.getDbRatePct(), 0));
                it.setDbAmount1(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference12()), feebase,0), it.getDbRatePct(), 0));
                it.setDbAmount2(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference14()), feebase,0), it.getDbRatePct(), 0));
                it.setDbAmount3(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference16()), feebase,0), it.getDbRatePct(), 0));
                it.setDbAmount4(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference18()), feebase,0), it.getDbRatePct(), 0));
                it.setDbAmount5(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference20()), feebase,0), it.getDbRatePct(), 0));
                it.setDbAmount6(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference22()), feebase,0), it.getDbRatePct(), 0));
            }

            //ppn feebase
            if(it.isPPNComission()){
                it.setDbAmount100(BDUtil.mul(komisi, it.getDbRatePct(), 0));
                it.setDbAmount1(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference12()), komisi,0), it.getDbRatePct(), 0));
                it.setDbAmount2(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference14()), komisi,0), it.getDbRatePct(), 0));
                it.setDbAmount3(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference16()), komisi,0), it.getDbRatePct(), 0));
                it.setDbAmount4(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference18()), komisi,0), it.getDbRatePct(), 0));
                it.setDbAmount5(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference20()), komisi,0), it.getDbRatePct(), 0));
                it.setDbAmount6(BDUtil.mul(BDUtil.mul(BDUtil.getRateFromPct(obj.getDbReference22()), komisi,0), it.getDbRatePct(), 0));
            }

        }
    }

    public void setBlankOutgo(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

    }

}
