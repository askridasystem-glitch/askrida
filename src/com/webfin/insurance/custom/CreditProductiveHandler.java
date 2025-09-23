/***********************************************************************
 * Module:  com.webfin.insurance.custom.CashManagementHandler
 * Author:  Denny Mahendra
 * Created: Sep 29, 2006 12:19:13 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.crux.util.*;
import com.webfin.FinCodec;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CreditProductiveHandler extends DefaultCustomHandler {
    private final static transient LogManager logger = LogManager.getInstance(CreditProductiveHandler.class);

    final boolean penerapanKreditBaru = false;
    
    public void applyClausules(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
        try {

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
            applyExtraPremi(obj, policy);
            //END

            //otomatis set stnc to all object sesuai objek 1 kecuali endorse
            final InsurancePolicyObjDefaultView obj1 = (InsurancePolicyObjDefaultView) policy.getObjects().get(0);
            if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
                 boolean persetujuanPusat = Tools.isYes(obj.getStReference19());

                 if(!persetujuanPusat)
                        obj.setDtReference5(obj1.getDtReference5());
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

            DateTime policyDateLastDay = new DateTime(policy.getDtPolicyDate()).dayOfMonth().withMaximumValue();

            if(obj.getStReference5()==null)
                obj.setStReference5(String.valueOf(year));

            if(obj.getStReference22()==null)
                obj.setStReference22(String.valueOf(mon));

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

                //CEK JIKA ADA EKSTRA PREMI
                obj.setDbReference9(null);
                
                if (obj.getDbReference8() != null){
                    for(int i=0;i<covers.size();i++){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

                        int scale = 0;
                        if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
                        else scale = 0;

                        if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("596")){
                            cv.setDbPremiNew(BDUtil.mul(totalPremi, BDUtil.getRateFromPct(obj.getDbReference8()),scale));
                            cv.setDbPremi(cv.getDbPremiNew());
                            cv.setStCalculationDesc(ConvertUtil.removeTrailing(ConvertUtil.print(totalPremi,4)) +" x "+ ConvertUtil.removeTrailing(ConvertUtil.print(obj.getDbReference8(),4))+ policy.getStRateMethodDesc());

                            obj.setDbReference9(cv.getDbPremiNew());
                            //totalPremi = BDUtil.add(totalPremi,cv.getDbPremi());
                        }
                    }
                }

                if(!policy.isStatusEndorse()){
                    obj.setDbReference3(totalTSI);
                    obj.setDbReference4(totalPremi);
                }

            recalculateRestitusi(policy, obj);

            if(penerapanKreditBaru){
                applyWarranty(obj,policy);
                validateLimitPKS(policy, obj, validate);
                validateRateJual(String.valueOf(year), obj, policy, covers);
            }
            

            validateApproval(policy, obj, validate); 
            

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

    private void recalculateRestitusi(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj) throws Exception{
        final DTOList covers = obj.getCoverage();
        final DTOList suminsured = obj.getSuminsureds();
        final Date perStart = obj.getDtReference2();
        final Date perEnd = obj.getDtReference3();

        int scale = 0;

        if(!policy.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;

        if(!policy.isStatusEndorse()){
            obj.setDbReference1(obj.getDbObjectPremiTotalAmount());

            if(!BDUtil.isZeroOrNull(obj.getDbReference9()))
                obj.setDbReference1(BDUtil.add(obj.getDbObjectPremiTotalAmount(),obj.getDbReference9()));
        }

        if(policy.isStatusEndorse() && obj.getDtReference7()!=null) {

            for (int i = 0; i < suminsured.size(); i++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView)suminsured.get(i);

                if(!BDUtil.isZero(tsi.getDbInsuredAmount())){
                    throw new RuntimeException("Debitur "+ obj.getStReference1() +" Harus Nol TSI nya untuk Restitusi");
                }

            }

                BigDecimal premiSisa = null;
                int sisaJangkaWaktu = 0;
                DateTime tglLunas = new DateTime(obj.getDtReference7());
                DateTime tglAwalKredit = new DateTime(obj.getDtReference2());
                Months bulanBerjalanM = Months.monthsBetween(tglAwalKredit, tglLunas);
                int bulanBerjalan = bulanBerjalanM.getMonths();

                DateTime startDate = new DateTime(perStart);
                DateTime endDate = new DateTime(perEnd);
                Months m = Months.monthsBetween(startDate, endDate);
                int mon = m.getMonths();

                sisaJangkaWaktu = mon - bulanBerjalan;

                if(obj.getStReference18()==null)
                    obj.setStReference18(String.valueOf(sisaJangkaWaktu));

                //final BigDecimal premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(obj.getDbReference5()),scale);
                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) covers.get(0);

                BigDecimal premi = null;
                premi = BDUtil.negate(obj.getDbReference1());
                if(BDUtil.isZeroOrNull(obj.getDbReference1()))
                    premi = BDUtil.mul(obj.getDbObjectInsuredAmount(),BDUtil.getRateFromMile(cover.getDbRate()),scale);

                premiSisa = BDUtil.mul(BDUtil.mul(premi,policy.getDbPeriodRateBeforeFactor(), scale),new BigDecimal(obj.getStReference18()), scale);
                premiSisa = BDUtil.div(premiSisa,new BigDecimal(mon));
                premiSisa = BDUtil.roundUp(premiSisa);

                //obj.setDbReference6(premiSisa);

                //BigDecimal totalRate = new BigDecimal(0);
                //BigDecimal totalPremi = new BigDecimal(0);
                BigDecimal premiBruto = null;
                if (covers!=null) {
                    if(covers.size()==1){
                        InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(0);

                        final InsurancePolicyCoverView refCover = cv.getRefCover();

                        if(!BDUtil.isZeroOrNull(refCover.getDbInsuredAmount())){
                            //cv.setStEntryRateFlag(obj.getStReference9());
                            cv.setDbPremiNew(premiSisa);
                            cv.setDbPremi(premiSisa);

                            obj.setDbObjectPremiTotalAmount(premiSisa);
                            obj.setDbObjectPremiTotalBeforeCoinsuranceAmount(premiSisa);

                            //final String curCalc = cv.getStCalculationDesc();

                            final StringBuffer szCalc = new StringBuffer();
                            szCalc.append(premi);

                            if (!BDUtil.isEqual(policy.getDbPeriodRateBeforeFactor(),BDUtil.one,0))
                                szCalc.append(" x "+policy.getStPeriodRateBeforeDesc());

                            szCalc.append(" x "+obj.getStReference18()+" / "+mon);

                            String calc = szCalc.toString();

                            cv.setStCalculationDesc(calc);

                            premiBruto = BDUtil.add(premiBruto,cv.getDbPremi());
                        }

                        
                    }

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

            }
    }

    public void applyClausules2(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
        try {

            //if(!validate) return;

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
        boolean persetujuanPusat = Tools.isYes(obj.getStReference19());

        int jumlahHariMaks = 60;

        if(policy.getStCostCenterCode().equalsIgnoreCase("32"))
            jumlahHariMaks = 90;


        if(!policy.isStatusClaim() && !policy.isStatusClaimEndorse()){
            final DTOList objects = policy.getObjects();

            String stPaketCoverageCurrent = "";
            String stPaketCoverageAfter = "";
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView objectCurrent = (InsurancePolicyObjDefaultView) objects.get(i);

                if(i==(objects.size()-1)) break;

                InsurancePolicyObjDefaultView objectAfter = (InsurancePolicyObjDefaultView) objects.get(i+1);

                stPaketCoverageCurrent = objectCurrent.getStReference10();

                stPaketCoverageAfter = objectAfter.getStReference10();

                //if(!stPaketCoverageCurrent.equalsIgnoreCase(stPaketCoverageAfter))
                    //throw new RuntimeException("Paket Coverage Harus Sama Dalam 1 Polis : Cek Objek No "+objectAfter.getStOrderNo());
            }
        }
        

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

                        /*
                        BigDecimal usiaMaksimal = new BigDecimal(65);
                        BigDecimal usiaMaksimalDB = getUsiaLimit("ACCEPT", policy.getStPolicyTypeID(), SessionManager.getInstance().getUserID());
                        BigDecimal limitUsia = new BigDecimal(55);

                        if (!BDUtil.isZeroOrNull(usiaMaksimalDB)) {
                            usiaMaksimal = BDUtil.add(usiaMaksimalDB, new BigDecimal(15));
                            limitUsia = usiaMaksimalDB;
                        }*/

                        if(!persetujuanPusat){
                            /*if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(BDUtil.biggerThan(obj.getDbObjectInsuredAmount(), new BigDecimal(500000000)))
                                    throw new RuntimeException("Maks. TSI Rp. 500.000.000 pada debitur "+obj.getStReference1());

                            if(policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(BDUtil.biggerThan(obj.getDbObjectInsuredAmount(), new BigDecimal(1000000000)))
                                    throw new RuntimeException("Maks. TSI 1 Milyar pada debitur "+obj.getStReference1());

                            
                            if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(!persetujuanPusat)
                                    if(usia > Integer.parseInt(String.valueOf(limitUsia)))
                                        throw new RuntimeException("Umur Debitur "+obj.getStReference1()+" Tidak Boleh > "+ limitUsia +" Tahun");

                            if(!policy.getStCostCenterCode().equalsIgnoreCase("22"))
                                if(usiaMaks > Integer.parseInt(String.valueOf(usiaMaksimal)) && year > 15)
                                    throw new RuntimeException("Usia maks. "+ usiaMaksimal +" dan lama 15 tahun pada debitur "+obj.getStReference1());
                            */

                            String str = fmt.print(batasWaktu);

                            if(!policy.isEditReasOnlyMode())
                                if(jumlahHariKerja > jumlahHariMaks)
                                    if(!persetujuanPusat)
                                        throw new RuntimeException("Debitur "+ obj.getStReference1() +" melebihi batas waktu 60 hari kerja, maks. tanggal "+ str);


                        }

                        if(!policy.isEditReasOnlyMode())
                                if(jumlahHariKerja > jumlahHariMaks)
                                    if(persetujuanPusat)
                                        if(obj.getDtReference5()==null)
                                            throw new RuntimeException("Debitur "+ obj.getStReference1() +" melebihi batas waktu 60 hari kerja dan belum di input tanggal STNC ");
                        
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
                                    "   and ref1 = ?  "+ //--jenis kredit
                                    "   and ref2 = ?  "+ //--cabang
                                    "   and ref3 = ?  "+ // --usia
                                    "   and ref4 = ?  "+ //--pekerjaan
                                    "   and ? between rate0 and rate1"); //tsi

                            S.setParam(1, policy.getStKreasiTypeID()); //jenis kredit
                            S.setParam(2, policy.getStCostCenterCode()); // CABANG
                            S.setParam(3, String.valueOf(usiaTerdekat)); //USIA
                            S.setParam(4, obj.getStReference19()); // pekerjaan
                            S.setParam(5, obj.getDbObjectInsuredAmount()); //TSI

                            String message = "usia:"+usiaTerdekat+",pekerjaan:"+obj.getStReference19()+",tsi:"+ JSPUtil.printAutoPrec(obj.getDbObjectInsuredAmount());

                            final ResultSet RS = S.executeQuery();

                            if (RS.next()){

                                obj.setStReference21(RS.getString(3));
                                dokumenID = RS.getString(4);

                                if(policy.isStatusPolicy() || policy.isStatusRenewal()){
                                    if(policy.getStEffectiveFlag()!=null){
                                         if(policy.getStEffectiveFlag().equalsIgnoreCase("Y"))
                                            if(Tools.isNo(RS.getString(1)))
                                                throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+" tidak termasuk automatic cover");
                                    }
                                }
                            }else{
                                throw new RuntimeException("Debitur an "+obj.getStReference1() +" ("+ message +") No. "+ obj.getStOrderNo()+" tidak ditemukan limit kewenangan, konfirmasi ke u/w kantor pusat");
                            }

                        }finally{
                            S.release();
                        }
                    //}
                //}
                
                 //validasi dokumen kesehatan
                 validateDokumen(obj, dokumenID);
                
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

            String coverWajib = "595";

            boolean apply = (!policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse());

            if(apply){

                    //APPLY PAKET JIKA PILIHAN PAKET TIDAK KOSONG
                    if (obj.getDbReference8() != null) {

                        //DELETE COVER tambahan
                        DTOList coverr = obj.getCoverage();
                        if (obj.getCoverage().size()>1) {
                            while (obj.getCoverage().size()>1) coverr.delete(obj.getCoverage().size()-1);
                        }

                        //CEK JIKA ADA EKSTRA PREMI
                         if (obj.getDbReference8() != null){
                             coverWajib = "596";
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


                            if(cv.getStInsuranceCoverPolTypeID().equalsIgnoreCase("596")){
                                cv.setStEntryPremiFlag("Y");
                            }
                        }

                    }


            }
    }

    public void validateRateJual(String lama, InsurancePolicyObjDefaultView obj, InsurancePolicyView policy,DTOList covers) throws Exception{

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        BigDecimal rateJual1 = BDUtil.zero;

        if(Integer.parseInt(lama)==0) lama = String.valueOf(1);

        int usiaTerdekat = DateUtil.getUsiaUltahTerdekat(obj.getDtReference1(), obj.getDtReference2(),1);

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            if(!persetujuanPusat){
                //get rate jual
                final SQLUtil S = new SQLUtil();
                    try {

                        String sql =  " select rate"+ lama +
                                      " from ins_rates_big "+
                                      "  where ref1= ? "+ //--jenis kredit
                                      "  and ref2 = ? "+ //--cabang
                                      "  and ref3 = ? "+ //--tipe coverage
                                      "  and ref5 = ? "+ //--usia
                                      "  and ref4 = ? "+ //--pekerjaan
                                      "  and period_start <= ? and period_end >= ? "+
                                      "  and rate_class = 'OM_CREDIT_RATE'";

                        final PreparedStatement PS = S.setQuery(sql);

                        int n=1;

                        PS.setString(n++, policy.getStKreasiTypeID()); //jenis kredit
                        PS.setString(n++, policy.getStCostCenterCode()); //cabang
                        PS.setString(n++, obj.getStReference13()); //coverage
                        PS.setString(n++, String.valueOf(usiaTerdekat)); //usia
                        PS.setString(n++, obj.getStReference19()); //pekerjaan

                        S.setParam(n++,policy.getDtPolicyDate()); //period start
                        S.setParam(n++,policy.getDtPolicyDate()); //period end

                        logger.logInfo("############# jenis kredit : "+ policy.getStKreasiTypeID());
                        logger.logInfo("############# cabang : "+ policy.getStCostCenterCode());
                        logger.logInfo("############# coverage : "+ obj.getStReference13());
                        logger.logInfo("############# usia : "+ usiaTerdekat);
                        logger.logInfo("############# pekerjaan : "+ obj.getStReference19());
                        logger.logInfo("############# periode : "+ policy.getDtPolicyDate());

                        final ResultSet RS = PS.executeQuery();

                        if (RS.next()){
                            rateJual1 = RS.getBigDecimal(1);
                        }else{
                            rateJual1 = BDUtil.zero;
                            throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+" tidak ditemukan rate jual, konfirmasi ke u/w kantor pusat");
                        }

                    } finally {
                        S.release();
                    }

                   if (covers!=null) {
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
                                throw new RuntimeException("Debitur an "+obj.getStReference1() +" No. "+ obj.getStOrderNo()+ " Rate jual "+ totalRate +" tidak boleh < "+rateJual1);
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


    public void applyWarranty(InsurancePolicyObjDefaultView obj, InsurancePolicyView policy) throws Exception{

        boolean persetujuanPusat = Tools.isYes(obj.getStReference10());

        String warranty = "";

        if(!policy.isStatusHistory() && !policy.isStatusEndorse() && !policy.isStatusClaim() && !policy.isStatusClaimEndorse()){

            if(!persetujuanPusat){
                if(policy.getStWarranty()==null){
                    //get warranty
                    final SQLUtil S = new SQLUtil();
                        try {

                            String sql =  " select notes"+
                                          " from ins_rates_big "+
                                          "  where ref2 = ? "+ //--cabang
                                          "  and ref3 = ? "+ //--jenis asuransi
                                          "  and ref4 = ? "+ //--group company
                                          "  and period_start <= ? and period_end >= ? "+
                                          "  and rate_class = 'OM_WARRANTY'";

                            final PreparedStatement PS = S.setQuery(sql);

                            int n=1;

                            PS.setString(n++, policy.getStCostCenterCode()); //cabang
                            PS.setString(n++, policy.getStPolicyTypeID()); //jenis asuransi
                            PS.setString(n++, policy.getEntity().getStRef2()); //group company
                            S.setParam(n++,policy.getDtPolicyDate()); //period start
                            S.setParam(n++,policy.getDtPolicyDate()); //period end

                            final ResultSet RS = PS.executeQuery();

                            if (RS.next()){
                                warranty = RS.getString(1);
                            }

                        } finally {
                            S.release();
                        }

                     if(policy.getStWarranty()==null)
                         policy.setStWarranty(warranty);
                }
                

            }
        }




    }

    public void validateDokumen(InsurancePolicyObjDefaultView obj, String dokumenID) throws Exception{

        final DTOList dokumen = obj.getDetailDocuments();

        String [] dokumenIDArray = dokumenID.split(",");

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
