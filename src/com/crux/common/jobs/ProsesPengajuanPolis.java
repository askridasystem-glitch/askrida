/*
 * AHMAD RHODONI
 *
 * Created on 20-10-2014
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.crux.common.jobs;

import com.crux.common.model.RecordAudit;
import com.crux.common.model.RecordBackup;
import com.crux.common.parameter.Parameter;
import com.crux.ff.model.FlexTableView;
import com.crux.file.FileView;
import com.crux.jobs.util.JobUtil;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.MailUtil2;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.h2h.model.PengajuanPolisKBGView;
import com.webfin.h2h.model.PengajuanPolisKonstruksiView;
import com.webfin.h2h.model.PengajuanPolisTanggungGugat;
import com.webfin.h2h.model.WSDokumenFireView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsuranceCoverPolTypeView;
import com.webfin.insurance.model.InsuranceCoverSourceView;
import com.webfin.insurance.model.InsuranceItemsView;
import com.webfin.insurance.model.InsurancePolicyClausulesView;
import com.webfin.insurance.model.InsurancePolicyCoinsView;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsurancePolicyDeductibleView;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyInstallmentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.interkoneksi.model.PengajuanPolisCISView;
import com.webfin.interkoneksi.model.PengajuanPolisCITView;
import com.webfin.interkoneksi.model.PengajuanPolisFireView;
import com.webfin.system.ftp.model.DataGatewayView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author doni
 */

public class ProsesPengajuanPolis extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanPolis.class);

    //private String downloadPath = Parameter.readString("SYS_FTP_DOWNLOAD_FOLDER");
    //private String historyPath = Parameter.readString("SYS_FTP_HISTORY_FOLDER");
    private static String serverIP = Parameter.readString("SYS_SERVER_IP");
    private static boolean currentMonthLock = Parameter.readBoolean("SYS_H2H_CURRENT_MONTH_LOCK");
    private String tanggalPolis = DateUtil.getDateStr7(DateUtil.getFirstDate(new Date())) + " 00:00:00"; //"2025-02-01 00:00:00";

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction())
                {
                    //cekPolisH2HPendingToCore();

                    //PROSES DATA HOST TO HOST AKSRED POJK20 JADI POLIS APPROVE PER POLIS
                    prosesPengajuanPolisSerbagunaMacet();

                    //PROSES DATA HOST TO HOST JADI POLIS APPROVE PER POLIS
                    prosesPengajuanPolisSatuanH2H();

                    //PROSES PENGAJUAN POLIS NON KREDIT AUTO APPROVE
                    prosesPengajuanPolisNonKredit();

                    //PROSES DATA HOST TO HOST JADI POLIS KUMPULAN APPROVE PER POLIS
                    prosesPengajuanPolisKumpulanH2H();

                    //PROSES POLIS FIRE
                    prosesPengajuanPolisFireH2H();

                    //PROSES POLIS KONSTRUKSI
                    prosesPolisKonstruksiH2H();

                    //PROSES POLIS CIT
                    //prosesPolisKumpulanCIT();

                    //PROSES POLIS CIS
                    //prosesPolisKumpulanCIS();

                    //PROSES POLIS KBG
                    prosesPolisKbgH2H();

                    //PROSES POLIS TANGGUNG GUGAT
                    prosesPolisTanggungGugatH2H();

                }  
  
        } catch (Exception e) {
            throw new JobExecutionException(e);
        } 
    } 

    private void prosesDataMenjadiProposal(String cabang) throws Exception{

        //final SQLUtil S = new SQLUtil("GATEWAY");
        //final SQLUtil S = new SQLUtil("GATEWAY_SQL");

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP DOKUMEN YANG BELUM DI PROSES
        /*
        listGroup = ListUtil.getDTOListFromQueryDS(
                "select kode_bank,group_id,status_kerja "+
                    " from data_teks_masuk a "+
                    " where kategori = 'POLICY' and proses_flag is null "+
                    " group by kode_bank,group_id,status_kerja "+
                    " order by group_id, kode_bank", DataTeksMasukView.class,"GATEWAY");
        */

        // buat proposal per orang/debitur
        listGroup = ListUtil.getDTOListFromQueryDS(
                "select kode_bank,group_id,data_id "+
                    " from data_teks_masuk a "+
                    " where kategori = 'POLICY' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and cc_code = ? and status <> 'CONFIRM' "+
                    " order by data_id", new Object [] {cabang},
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN data ID
            listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'POLICY' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and status <> 'CONFIRM' "+
                     "    and data_id = ? and kode_bank = ? "+
                     "    order by data_id",
                     new Object [] {grup.getStDataID(), grup.getStKodeBank()},
                   DataTeksMasukView.class,"GATEWAY");

            if(listObjek.size()==0) continue;

            System.out.println("Bikin proposal grup : "+ grup.getStGroupID());

            //BUAT PROPOSAL BERDASARKAN GROUP ID
            buatPenawaranOtomatis(listObjek);

        }

    }

    private void prosesBanyakDataMenjadiSatuProposal(String cabang) throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP DOKUMEN YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select kode_bank,group_id,jenis_kredit,status_kerja "+
                    " from data_teks_masuk a "+
                    " where kategori = 'POLICY' and proses_flag is null and cc_code = ? and status <> 'CONFIRM'"+
                    " group by kode_bank,group_id,jenis_kredit,status_kerja "+
                    " order by group_id, kode_bank",
                    new Object [] {cabang},
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
            listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'POLICY' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and status <> 'CONFIRM' "+
                     "    and group_id = ? and kode_bank = ? and status_kerja = ? and jenis_kredit = ?"+
                     "    order by data_id",
                     new Object [] {grup.getStGroupID(), grup.getStKodeBank(), grup.getStStatusKerja(), grup.getStJenisKredit()},
                   DataTeksMasukView.class,"GATEWAY");

            if(listObjek.size()==0) continue;

            System.out.println("Bikin proposal grup : "+ grup.getStGroupID());

            //BUAT PROPOSAL BERDASARKAN GROUP ID
            buatPenawaranOtomatis(listObjek);

        }

    }

    private void prosesPengajuanPolisInterkoneksi() throws Exception{

        DTOList listGateway = null;

        //CARI FILE TEKS YANG BELUM DI PROSES
        listGateway = ListUtil.getDTOListFromQuery(
                " select * "+
                "     from s_data_gateway "+
                "     where active_flag = 'Y' "+
                "     order by gateway_id",
               DataGatewayView.class);

        for (int h = 0; h < listGateway.size(); h++) {
                DataGatewayView gateway = (DataGatewayView) listGateway.get(h);

                if(gateway.getStCostCenterCode().equalsIgnoreCase("23")){
                    prosesBanyakDataMenjadiSatuProposalJogja(gateway.getStCostCenterCode());
                }else{
                    if(gateway.isPolisDetails()) prosesBanyakDataMenjadiSatuProposal(gateway.getStCostCenterCode());
                    else prosesDataMenjadiProposal(gateway.getStCostCenterCode());
                }
        }

    }

    private InsurancePolicyView policy;

    private void buatPenawaranOtomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.DRAFT);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStDataSourceID("2");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());
            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(new Date());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));
            
            if(data1.getStJenisKreditAskrida()!=null)
                getPolicy().setStKreasiTypeID(data1.getStJenisKreditAskrida());

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());

            //logger.logWarning("############## ent id : "+data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            getPolicy().setStGatewayDataFlag("Y");

            //ADD OBJEK
            if(getPolicy().getStPolicyTypeID().equalsIgnoreCase("59")){
                if (getPolicy().getStCostCenterCode().equalsIgnoreCase("43")) {
                    uploadKreditMKS(listObjek, null);
                }else {
                    uploadKredit(listObjek, null);
                }
            }else if (getPolicy().getStPolicyTypeID().equalsIgnoreCase("80")){
                uploadKreditProduktif80(listObjek, null);
            }else{
                uploadKredit(listObjek, null);
            }

            getPolicy().recalculateInterkoneksi();

            //simpen
            getRemoteInsurance().saveFromTeks(policy,policy.getStNextStatus(),false);

            final SQLUtil S = new SQLUtil("GATEWAY");

            for (int i = 0; i < listObjek.size(); i++) {
                DataTeksMasukView data = (DataTeksMasukView) listObjek.get(i);

                //UPDATE DATA TEKS MASUK YANG SUDAH DI PROSES
                PreparedStatement PS = S.setQuery("update data_teks_masuk set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

                PS.setObject(1, data.getStDataID());
                PS.setObject(2, data.getStKodeBank());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStDataID() +" ++++++++++++++++++");

            }

            S.release();
            
    }

    public void onNewInstallment() throws Exception {
        final InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();

        inst.markNew();

        getInstallment().add(inst);

        String n = String.valueOf(getInstallment().size());

        getPolicy().setStInstallmentPeriods(n);

        getPolicy().reCalculateInstallment();
    }

    public DTOList getInstallment() throws Exception {
        return policy.getInstallment();
    }

    public void doNewLampiranObject() throws Exception {
//        final InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getClObjectClass().newInstance();
//        o.markNew();
//        o.setPolicy(policy);
//        o.setCoverage(new DTOList());
//
//        o.setSuminsureds(new DTOList());
//        o.setDeductibles(new DTOList());
//
//        getObjects().add(o);

        //InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) o;
        InsurancePolicyObjDefaultView obj = new InsurancePolicyObjDefaultView();
        //o.markNew();
        obj.markNew();
        obj.setPolicy(policy);
        obj.setCoverage(new DTOList());

        obj.setSuminsureds(new DTOList());
        obj.setDeductibles(new DTOList());

        getObjects().add(obj);

        setSelectedDefaultObject(obj);

    }

    private InsurancePolicyObjDefaultView selectedDefaultObject;

    public InsurancePolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;
    }

    public void setSelectedDefaultObject(InsurancePolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;
    }

    public DTOList getObjects() {
        return policy.getObjects();
    }

    private void uploadKredit(DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek grup : "+ object.getStGroupID() +" debitur "+ object.getStNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(object.getStDataID());
            getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
            getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
            //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
            getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
            getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
            getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
            getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
            getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

            if(object.getStPaketCoverageAskrida()!=null)
                getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());
            
            if(object.getStStatusKerja()!=null)
                getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
            else
                getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());

            //tambah tsi
            doAddLampiranSumInsured("488",object.getDbInsuredAmount());

            //tambah cover
            doAddLampiranCoverKreasi("444",object.getDbRate(),object.getDbPremiTotal());
           
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    private void uploadKreditPolis(String polTypeID, DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek grup : "+ object.getStGroupID() +" debitur "+ object.getStNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            //kredit konsumtif
            if(polTypeID.equalsIgnoreCase("59")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
                getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
                getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
                getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
                else
                    getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());

                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference10("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference10("Y");

                if(object.getStKodeTransaksi()!=null){
                    getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set periode 100%
                if(object.getDtTanggalAwal100()!=null)
                    getSelectedDefaultObject().setDtReference7(object.getDtTanggalAwal100());

                if(object.getDtTanggalAkhir100()!=null)
                    getSelectedDefaultObject().setDtReference8(object.getDtTanggalAkhir100());

                //set premi 100%
                if(object.getDbPremi100()!=null)
                    getSelectedDefaultObject().setDbReference11(object.getDbPremi100());

                //set premi sisa
                if(object.getDbPremiSisa()!=null)
                    getSelectedDefaultObject().setDbReference15(object.getDbPremiSisa());

                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference6(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference10("Y");
                    }


                //tambah tsi
                doAddLampiranSumInsured("488",object.getDbInsuredAmount());

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri("444",object.getDbRate(),object.getDbPremiTotal());
                    else
                        doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal());
                }

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        doAddLampiranCoverKreasi("573",null,object.getDbPremiRefund());
                    }
                }
            }

            //kredit produktif
            if(polTypeID.equalsIgnoreCase("80")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference4(object.getStStatusKerja());
                else
                    getSelectedDefaultObject().setStReference4(object.getKodePekerjaan());

                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference7(object.getStNoPerjanjianKredit()); // no pk
                getSelectedDefaultObject().setStReference8(object.getStNoRekeningPinjaman()); // no rek pinjaman

                if(object.getStJenisKreditAskrida()!=null)
                    getSelectedDefaultObject().setStReference9(object.getStJenisKreditAskrida()); // jenis kredit
                else
                    getSelectedDefaultObject().setStReference9("2"); // jenis kredit


                if(object.getStSumberPembayaran()!=null)
                    getSelectedDefaultObject().setStReference11(object.getStSumberPembayaran());
                else
                    getSelectedDefaultObject().setStReference11("1"); //sumber pembayaran

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPaketCoverageAskrida()); //coverage

                
                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference5(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference19("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference19(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference19("Y");

                if(object.getStKodeTransaksi()!=null){
                    //getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference5(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference19("Y");
                    }


                //tambah tsi
                doAddLampiranSumInsured("577",object.getDbInsuredAmount());

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri("595",object.getDbRate(),object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi("595",null,object.getDbPremiTotal());
                }

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        doAddLampiranCoverKreasi("617",null,object.getDbPremiRefund());
                    }
                }
            }

            //kredit macet only & Serbaguna
            if(polTypeID.equalsIgnoreCase("87") || polTypeID.equalsIgnoreCase("88")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
                getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
                getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
                getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
                else
                    getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());

                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference10("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference10("Y");

                if(object.getStKodeTransaksi()!=null){
                    getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set periode 100%
                if(object.getDtTanggalAwal100()!=null)
                    getSelectedDefaultObject().setDtReference7(object.getDtTanggalAwal100());

                if(object.getDtTanggalAkhir100()!=null)
                    getSelectedDefaultObject().setDtReference8(object.getDtTanggalAkhir100());

                //set premi 100%
                if(object.getDbPremi100()!=null)
                    getSelectedDefaultObject().setDbReference11(object.getDbPremi100());

                //set premi sisa
                if(object.getDbPremiSisa()!=null)
                    getSelectedDefaultObject().setDbReference15(object.getDbPremiSisa());

                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference6(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference10("Y");
                    }


                //tambah tsi
                if(polTypeID.equalsIgnoreCase("87"))
                    doAddLampiranSumInsured("600",object.getDbInsuredAmount());

                if(polTypeID.equalsIgnoreCase("88"))
                    doAddLampiranSumInsured("601",object.getDbInsuredAmount());


                String cvptID = polTypeID.equalsIgnoreCase("87")?"647":"654";

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri(cvptID,object.getDbRate(),object.getDbPremiTotal());
                    else
                        doAddLampiranCoverKreasi(cvptID,null,object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi(cvptID,null,object.getDbPremiTotal());
                }


                String cvptRestitusiID = polTypeID.equalsIgnoreCase("87")?"648":"655";

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        doAddLampiranCoverKreasi(cvptRestitusiID,null,object.getDbPremiRefund());
                    }
                }
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
            
        }


    }

    public void doAddLampiranSumInsured(String instsipolid,BigDecimal tsi) throws Exception {

        //if(sumInsuredAddID==null) throw new RuntimeException("Please select Item to add");

        final InsurancePolicyTSIView ptsi = new InsurancePolicyTSIView();

        ptsi.setStInsuranceTSIPolTypeID(instsipolid);

        ptsi.initializeDefaults();

        ptsi.setDbInsuredAmount(tsi);

        ptsi.markNew();

        selectedDefaultObject.getSuminsureds().add(ptsi);

    }

    public void doAddLampiranCoverKreasi(String cvptid,BigDecimal rate,BigDecimal premi) throws Exception{

        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244

        cv.initializeDefaults();

        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

        if(rate!=null){
            cv.setDbRate(BDUtil.getMileFromPCT(rate)); 
            //cv.setDbRate(rate);
            cv.setStEntryRateFlag("Y");
        }
        if(rate==null){
            cv.setStAutoRateFlag("N");
            cv.setStEntryRateFlag("N");
            cv.setStEntryPremiFlag("Y");
            cv.setDbPremiNew(premi);
            cv.setDbPremi(cv.getDbPremiNew());
        }

        cv.markNew();

        selectedDefaultObject.getCoverage().add(cv);

    }

    public void doAddLampiranCoverKreasiRateOri(String cvptid,BigDecimal rate,BigDecimal premi) throws Exception{

        logger.logDebug("################## rate convert pct to pmill = "+ rate);

        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244

        cv.initializeDefaults();

        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

        if(rate!=null){
            cv.setDbRate(rate);
            //cv.setDbRate(rate);
            cv.setStEntryRateFlag("Y");
        }
        if(rate==null){
            cv.setStAutoRateFlag("N");
            cv.setStEntryRateFlag("N");
            cv.setStEntryPremiFlag("Y");
            cv.setDbPremiNew(premi);
            cv.setDbPremi(cv.getDbPremiNew());
        }

        cv.markNew();

        selectedDefaultObject.getCoverage().add(cv);

    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

    /**
     * @return the policy
     */
    public InsurancePolicyView getPolicy() {
        return policy;
    }

    /**
     * @param policy the policy to set
     */
    public void setPolicy(InsurancePolicyView policy) {
        this.policy = policy;
    }

    private void addMyCompany() throws Exception {

        final InsuranceCoverSourceView cs = policy.getCoverSource();

        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

        co.markNew();

        co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);

        co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));

        co.setStHoldingCompanyFlag("Y");

        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINS);

        getCoins().add(co);
    }

    private void addMyCompanyCoverage() throws Exception {
        boolean canCreate = false;

        if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) canCreate = true;
        else if(policy.getStPolicyTypeID().equalsIgnoreCase("98")) canCreate = true;
        else if(policy.getStPolicyTypeID().equalsIgnoreCase("99")) canCreate = true;

        if(!canCreate) return;

        final InsuranceCoverSourceView cs = policy.getCoverSource();

        final InsurancePolicyCoinsView co = new InsurancePolicyCoinsView();

        co.markNew();

        co.setStPositionCode(cs.isLeader()?FinCodec.CoInsurancePosition.LEADER:FinCodec.CoInsurancePosition.MEMBER);

        co.setStEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));

        co.setStHoldingCompanyFlag("Y");

        co.setStCoinsuranceType(FinCodec.CoinsuranceType.COINSCOVER);

        getCoinsCoverage().add(co);
    }

    public DTOList getCoins() throws Exception {
        return policy.getCoins2();
    }

    public DTOList getCoinsCoverage() throws Exception {
        return policy.getCoinsCoverage();
    }

    DataTeksMasukView prosesDataKredit(DataGatewayView gateway, String[] data, String groupID)throws Exception{
            DataTeksMasukView teks = new DataTeksMasukView();

            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

            teks.markNew();

            teks.setStGroupID(groupID);
            teks.setStCostCenterCode(gateway.getStCostCenterCode());
            teks.setStRegionID(gateway.getStRegionID());
            teks.setStPolicyTypeID(data[0]);
            teks.setStKodeBank(data[1]);
            teks.setStJenisKredit(data[2]);
            teks.setStNama(data[3]);
            teks.setStJenisIdentitas(data[4]);
            teks.setStNomorIdentitas(data[5]);
            teks.setStJenisPekerjaan(data[6]);
            teks.setStNoRekeningPinjaman(data[7]);
            teks.setStNoPerjanjianKredit(data[8]);
            teks.setStUsia(data[9]);
            teks.setDtTanggalLahir(df2.parse(data[10]));
            teks.setDtTanggalAwal(df2.parse(data[11]));
            teks.setDtTanggalAkhir(df2.parse(data[12]));
            teks.setDbInsuredAmount(new BigDecimal(data[13]));
            teks.setDbPremiTotal(new BigDecimal(data[14]));
            teks.setDtTanggalProses(new Date());
            teks.setStKategori(FinCodec.PolicyStatus.POLICY);

            teks.setStEntityID(getEntityID(teks.getStKodeBank()));

            teks.setStStatus("TRANSFERED");

            return teks;
    }

    public String getEntityID(String stKodeBank) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     ent_id " +
                    "   from " +
                    "         ent_master " +
                    "   where" +
                    "      ref_gateway_code = ?");

            S.setParam(1,stKodeBank);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getDefaultPemasar(String cabang) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     default_producer_id " +
                    "   from " +
                    "         s_data_gateway " +
                    "   where" +
                    "      cc_code = ?");

            S.setParam(1,cabang);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }

    }


    private void prosesPengajuanPolisSatuanH2H() throws Exception{

        DTOList listGroup = null;

        // buat proposal per orang/debitur
        String param = "";

        if(currentMonthLock){
            param = " and date_trunc('month',tgl_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone ";
        }

        /*
        listGroup = ListUtil.getDTOListFromQueryDS(
                    "select a.* "+
                    " from data_teks_masuk a "+
                    " left join ent_master b on a.entity_id = b.ent_id"+
                    " where kategori = 'HOST' and status in ('POLIS','CBC') and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and pol_type_id in ('59','80')"+
                    " and b.ref2::bigint in (select vs_code from s_company_group where host_to_host_flag = 'Y')  "+
                    " and (no_rek_pinjaman is not null and no_perjanjian_kredit is not null and no_identitas is not null "+
                    " and paket_coverage_askrida is not null and tgl_awal < tgl_akhir "+
                    " and usia >= 17 and usia <= 75 and entity_id is not null and upper(nama) not like '%#TES%' and length(pol_no) >= 18"+
                    " ) and tgl_polis >= '"+ tanggalPolis +"' "+
                    param +
                    " and date_trunc('day',tgl_awal) <= '2024-12-13 00:00:00' "+ //POJK
                    " order by data_id",
                    DataTeksMasukView.class,"GATEWAY");
        */

        listGroup = ListUtil.getDTOListFromQueryDS(
                    "select a.* "+
                    " from data_teks_masuk a "+
                    " left join ent_master b on a.entity_id = b.ent_id"+
                    " where kategori = 'HOST' and status in ('POLIS','CBC') and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and pol_type_id in ('59','80')"+
                    " and b.ref2::bigint in (select vs_code from s_company_group where host_to_host_flag = 'Y')  "+
                    " and (no_rek_pinjaman is not null and no_perjanjian_kredit is not null and no_identitas is not null "+
                    " and paket_coverage_askrida is not null and tgl_awal < tgl_akhir "+
                    " and usia >= 17 and usia <= 75 and entity_id is not null and upper(nama) not like '%#TES%' and length(pol_no) >= 18 and substr(pol_no, 3,2) = pol_type_id"+
                    " ) and tgl_polis >= '"+ tanggalPolis +"' "+
                    param +
                    " order by data_id",
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(grup.getStPolicyNo());

            //jika belum ada di core, inject to core
            if(polisExisting==null){

                DTOList listObjek = null;

                //CARI DAFTAR OBJEK BERDASARKAN data ID

                 listObjek = ListUtil.getDTOListFromQueryDS(
                        " select * "+
                         "    from data_teks_masuk "+
                         "    where kategori = 'HOST' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and pol_type_id in ('59','80')"+
                         "    and data_id = ? "+
                         "    order by data_id",
                         new Object [] {grup.getStDataID()},
                       DataTeksMasukView.class,"GATEWAY");

                if(listObjek.size()==0) continue;

                System.out.println("######### Bikin POLIS HOST TO HOST NO POLIS : "+ grup.getStPolicyNo());

                //Buat SPPA otomatis
                //buatSPPAOtomatis(listObjek);

                //BUAT polis otomatis
                buatPolisOtomatis(listObjek);
                
            }
        }
    }

    private void buatPolisOtomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            if(data1.getStCostCenterCodeCore()!=null)
                getPolicy().setStCostCenterCode(data1.getStCostCenterCodeCore());

            if(data1.getStRegionIDCore()!=null)
                getPolicy().setStRegionID(data1.getStRegionIDCore());

            getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
            getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(data1.getStPolicyNo().trim());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(data1.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            if(data1.getStJenisKreditAskrida()!=null)
                getPolicy().setStKreasiTypeID(data1.getStJenisKreditAskrida());

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());

            if(listObjek.size()==1){
                getPolicy().setStCustomerName(bank.getStEntityName()+" QQ "+ data1.getStNama().trim());
            }

            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            if(data1.getStTransactionNo()!=null)
                getPolicy().setStReferenceNo(data1.getStTransactionNo());

            //Cek outgo dulu, jika sesuai maka proses, jika tidak maka skip
            boolean isLimitOutgoSesuai = isSesuaiLimitOutgo(data1);

            if(isLimitOutgoSesuai){

                    //cek limit outgo h2h
                    validateLimitOutgo(data1);

                    //add fee base bank tanpa ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                        onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add fee base bank include ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                        onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add fee base bank exclude ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                        onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add komisi
                    if(data1.getDbKomisi1Pct()!=null){
                        onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1", null);
                    }

                    //add diskon
                    if(data1.getDbDiscountPct()!=null){
                        onNewDetailAutomatic("16","N", null,data1.getDbDiscountPct(),null, null);
                    }

                    //add brokerfee include ppn
                    if(data1.getDbBrokerageIncludePPNPct()!=null && data1.getDbBrokerageIncludePPNAmount()!=null){
                        if(!BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNAmount()))
                                onNewDetailAutomatic("88","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageIncludePPNPct(), "5", null);
                    }

                    //add brokerfee exclude ppn
                    if(data1.getDbBrokerageExcludePPNPct()!=null && data1.getDbBrokerageExcludePPNAmount()!=null){
                        if(!BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNAmount()))
                                onNewDetailAutomatic("12","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageExcludePPNPct(), "5", null);
                    }

                    if(data1.getStNomorBuktiBayar()!=null){
                        getPolicy().setStNomorBuktiBayar(data1.getStNomorBuktiBayar());
                    }

                    if(data1.getDtTanggalBayar()!=null){
                        getPolicy().setDtTanggalBayar(data1.getDtTanggalBayar());
                    }

                    if(data1.getStNomorReferensiPembayaran()!=null){
                        getPolicy().setStNomorReferensiPembayaran(data1.getStNomorReferensiPembayaran());
                    }

                    getPolicy().setStGatewayDataFlag("Y");

                    //ADD OBJEK
                    uploadKreditPolis(data1.getStPolicyTypeID(), listObjek, null);

                    //apply warranty
                    if(data1.getStWarranty()!=null){
                        getPolicy().setStWarranty(data1.getStWarranty());
                    }

                    //hitung ulang
                    getPolicy().recalculateInterkoneksi();

                    getPolicy().defineTreaty();

                    getPolicy().recalculateInterkoneksi();
                    getPolicy().recalculateTreatyUploadSpreading();

                    //getPolicy().invokeCustomHandlersPolicyAuto(false, getSelectedDefaultObject());

                    //apply pilihan klausula
                    applyClausules(data1);

                    //simpan
                    if(data1.isCaseByCase()){
                        getPolicy().setStPostedFlag(null);
                        getPolicy().setStEffectiveFlag(null);
                        getPolicy().setStApprovedWho(null);
                        getPolicy().setDtApprovedDate(null);
                        getPolicy().setStClientIP(null);
                        getPolicy().setStReadyToApproveFlag(null);

                        getRemoteInsurance().saveFromTeks(policy,policy.getStNextStatus(),false);

                    }else{
                        String policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

                        //getRemoteInsurance().saveNotaToCare(policy, true);
                        
                    }


                    final SQLUtil S = new SQLUtil("GATEWAY");

                    for (int i = 0; i < listObjek.size(); i++) {
                        DataTeksMasukView data = (DataTeksMasukView) listObjek.get(i);

                        //UPDATE DATA TEKS MASUK YANG SUDAH DI PROSES
                        PreparedStatement PS = S.setQuery("update data_teks_masuk set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

                        PS.setObject(1, data.getStDataID());
                        PS.setObject(2, data.getStKodeBank());

                        int j = PS.executeUpdate();

                        if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStDataID() +" ++++++++++++++++++");

                    }

                    S.release();
            }

    }

    public void onNewDetailAutomatic(String insItemID, String stReadOnly, String entityID, BigDecimal pct, String taxCode, BigDecimal ppnRate) throws Exception {

        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(insItemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStROFlag(stReadOnly);

        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        item.setDbRate(pct);
        item.setStFlagEntryByRate("Y");

        if(taxCode!=null)
            item.setStTaxCode(taxCode);

        if(entityID!=null) item.setStEntityID(entityID);

        getPolicy().getDetails().add(item);

        if(itemCat.getStInsuranceItemChildID()!=null)
            onNewDetailByChildID(itemCat.getStInsuranceItemChildID(), ppnRate);

    }

    public void onNewDetailByChildID(String itemID, BigDecimal ppnRate) throws Exception {

        //if (insItemID == null) throw new Exception("Please select item to be added");

        final InsuranceItemsView itemCat = getInsItemCat(itemID);

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(itemID);

        item.setStInsuranceItemCategory(itemCat.getStItemCategory());

        if(itemCat.getDbDefaultValue()!=null){

            item.setStFlagEntryByRate("Y");
            item.setDbRate(itemCat.getDbDefaultValue());

            if(ppnRate!=null){
                if(!BDUtil.isZero(ppnRate)){
                    item.setStFlagEntryByRate("Y");
                    item.setDbRate(ppnRate);
                }
            }

        }

        getPolicy().getDetails().add(item);

    }

    public void onNewDetailAutomatic2(String insItemID, String stReadOnly, String entityID, BigDecimal amount, String taxCode) throws Exception {

        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.PREMI);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStROFlag(stReadOnly);

        item.setStInsuranceItemCategory(getInsItemCat(insItemID).getStItemCategory());

        item.setDbAmount(amount);

        if(taxCode!=null)
            item.setStTaxCode(taxCode);

        if(entityID!=null) item.setStEntityID(entityID);

        getPolicy().getDetails().add(item);

    }

    public InsuranceItemsView getInsItemCat(String stInsItemID) {
        return (InsuranceItemsView) DTOPool.getInstance().getDTO(InsuranceItemsView.class, stInsItemID);
    }

    private void prosesPengajuanPolisKumpulanH2H() throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP NOMOR POLIS YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select pol_no,entity_id,count(data_id) as jumlah "+
                " from data_teks_masuk a "+
                " left join ent_master b on a.entity_id = b.ent_id"+
                " where kategori = 'HOST2' and status = 'POLIS' and pol_type_id in ('59')"+
                " and b.ref2::bigint in (select vs_code from s_company_group where host_to_host_flag = 'Y')  "+
                " and proses_flag is null and substr(pol_no,3,2) <> '80' and tgl_polis >= '"+ tanggalPolis +"' "+
                " and date_trunc('day',a.tgl_awal) <= '2024-12-13 00:00:00' "+ //POJK
                " group by pol_no,entity_id "+
                " order by pol_no",
                DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(grup.getStPolicyNo());

            //jika belum ada di core, inject to core
            if(polisExisting==null){

                DTOList listObjek = null;

                //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
                listObjek = ListUtil.getDTOListFromQueryDS(
                        " select * "+
                         "    from data_teks_masuk "+
                         "    where kategori = 'HOST2' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' "+
                         "    and pol_no = ? and entity_id = ?"+
                         "    order by data_id",
                         new Object [] {grup.getStPolicyNo(), grup.getStEntityID()},
                       DataTeksMasukView.class,"GATEWAY");

                if(listObjek.size()==0) continue;

                System.out.println("Bikin polis H2H kumpulan nomor polis : "+ grup.getStPolicyNo());

                //buat sppa
                //buatSPPAOtomatis(listObjek);

                //BUAT POLIS BERDASARKAN NOMOR POLIS
                buatPolisOtomatis(listObjek);
            }

            

        }

    }

    private void prosesBanyakDataMenjadiSatuProposalJogja(String cabang) throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP DOKUMEN YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select kode_bank,group_id,jenis_kredit,status_kerja ,kode_transaksi,jenis_data,count(data_id) as jumlah_data "+
                " from data_teks_masuk a  "+
                " where kategori = 'POLICY' and proses_flag is null and status <> 'CONFIRM'  "+
                " and cc_code = ? "+
                " group by kode_bank,group_id, jenis_kredit, status_kerja, kode_transaksi, jenis_data "+
                " order by group_id, kode_bank",
                    new Object [] {cabang},
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
            listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'POLICY' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and status <> 'CONFIRM' "+
                     "    and group_id = ? and kode_bank = ? and status_kerja = ? and jenis_kredit = ? and kode_transaksi = ? and jenis_data = ? "+
                     "    order by data_id",
                     new Object [] {grup.getStGroupID(), grup.getStKodeBank(), grup.getStStatusKerja(), grup.getStJenisKredit(), grup.getStKodeTransaksi(), grup.getStJenisData()},
                   DataTeksMasukView.class,"GATEWAY"); 

            if(listObjek.size()==0) continue;

            System.out.println("Bikin proposal grup : "+ grup.getStGroupID());

            //BUAT PROPOSAL BERDASARKAN GROUP ID

            if(grup.getStJenisData().equalsIgnoreCase("2")){
                    buatPenawaranOtomatisJogja(listObjek);
            }else{
                    buatPenawaranOtomatis(listObjek);
            }
        }
    }

    private void buatPenawaranOtomatisJogja(DTOList listObjek) throws Exception{

        for (int i = 0; i < listObjek.size(); i++) {
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(i);

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.DRAFT);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStDataSourceID("2");

            onNewInstallment();

            //ISI OTOMATIS HEADER

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());
            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(new Date());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            if(data1.getStJenisKreditAskrida()!=null)
                getPolicy().setStKreasiTypeID(data1.getStJenisKreditAskrida());

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            getPolicy().setStGatewayDataFlag("Y");

            //ADD OBJEK

            uploadKreditJogja(data1, null);

            getPolicy().recalculateInterkoneksi();

            //simpen
            getRemoteInsurance().saveFromTeks(policy,policy.getStNextStatus(),false);

            final SQLUtil S = new SQLUtil("GATEWAY");

            //UPDATE DATA TEKS MASUK YANG SUDAH DI PROSES
            PreparedStatement PS = S.setQuery("update data_teks_masuk set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

            PS.setObject(1, data1.getStDataID());
            PS.setObject(2, data1.getStKodeBank());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data1.getStDataID() +" ++++++++++++++++++");

            S.release();
 
        }
            
    }

    private void uploadKreditJogja(DataTeksMasukView objek, String treatyID)throws Exception{

            final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

            DataTeksMasukView object = (DataTeksMasukView) objek;

            System.out.println("add objek grup : "+ object.getStGroupID() +" debitur "+ object.getStNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(object.getStDataID());
            getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
            getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
            //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
            getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
            getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
            getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
            getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
            getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

            if(object.getStPaketCoverageAskrida()!=null)
                getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());

            if(object.getStStatusKerja()!=null)
                getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
            else
                getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());

            //tambah tsi
            doAddLampiranSumInsured("488",object.getDbInsuredAmount());

            //tambah cover
            doAddLampiranCoverKreasi("444",object.getDbRate(),object.getDbPremiTotal());

            /*
            //add biaya polis
            if(objek.getDbBiayaPolis()!=null){
                onNewDetailAutomatic2("15","N", null,objek.getDbBiayaPolis(),null);
            }

            //add biaya materai
            if(objek.getDbBiayaMaterai()!=null){
                onNewDetailAutomatic2("14","N", null,objek.getDbBiayaMaterai(),null);
            }
            */

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

    }

    private void uploadKreditMKS(DTOList objek, String treatyID) throws Exception {

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(), getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek grup : " + object.getStGroupID() + " debitur " + object.getStNama() + " ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(object.getStDataID());
            getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
            getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
            //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
            getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
            getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
            getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
            getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
            getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

            if (object.getStPaketCoverageAskrida() != null) {
                getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());
            }

            if (object.getStStatusKerja() != null) {
                getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
            } else {
                getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());
            }

            //tambah tsi
            doAddLampiranSumInsured("488", object.getDbInsuredAmount());

            //tambah cover
            doAddLampiranCoverKreasi("444", object.getDbRate(), object.getDbPremiTotal());

            //polis refund MKS
            DTOList listObject = null;
            listObject = ListUtil.getDTOListFromQuery(
                    " select b.* from ins_policy a "
                    + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                    + "where a.pol_no = ? and b.ref1 = ? "
                    + "order by 1",
                    new Object[]{object.getStPolicyNo(), object.getStNama()},
                    InsurancePolicyObjDefaultView.class);

            InsurancePolicyObjDefaultView polObj = (InsurancePolicyObjDefaultView) listObject.get(0);

            if (!object.getStNoPolisInduk().equalsIgnoreCase("NULL")) {
                getSelectedDefaultObject().setStReference17(object.getStNoPolisInduk());
                getSelectedDefaultObject().setStReference22("N");
                getSelectedDefaultObject().setDbReference8(object.getDbPremiRefund());
                doAddLampiranCoverKreasi("573", null, object.getDbPremiRefund());
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    private void prosesPengajuanPolisNonKredit() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listProposal = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB CEK PENGAJUAN AUTO POLIS YANG BELUM DI TRANSFER");

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        listProposal = ListUtil.getDTOListFromQueryDS(
                "select * from ins_policy "+
                " where status = 'PROPOSAL' and active_flag = 'Y' and effective_flag = 'Y' and coalesce(admin_notes,'') <> 'OK' and coalesce(f_ready_to_approve,'') = 'Y' order by pol_id",
                InsurancePolicyView.class,"GATEWAY");

        for (int i = 0; i < listProposal.size(); i++) {
            InsurancePolicyView proposal = (InsurancePolicyView) listProposal.get(i);

            logger.logInfo("transfer data polis no : "+ proposal.getStPolicyID());

            //COPY RECORD LKS NYA
            simpanPolisToCore(proposal.getStPolicyID());

            //UPDATE DATA LKS YANG SUDAH DI PROSES
            PreparedStatement PS = S.setQuery("update ins_policy set status_other ='POLICY', admin_notes = 'OK' where pol_id = ?");

            PS.setObject(1, proposal.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS proposal : "+ proposal.getStPolicyID() +" ++++++++++++++++++");

        }

    }

    public void simpanPolisToCore(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        policy.setStStatus(FinCodec.PolicyStatus.POLICY);
        policy.setStStatus(FinCodec.PolicyStatus.POLICY);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho("00000002");
        policy.setDtApprovedDate(new Date());
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(policyID);
        policy.setStDataSourceID("1");

        EntityView entity = policy.getEntity();

        if(entity.getStRegionID()!=null)
            policy.setStRegionID(entity.getStRegionID());

        getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
        getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

        //add komisi
        if(policy.getDbNDComm1()!=null)
            onNewDetailAutomatic("11","N", policy.getStCommEntityID1(), policy.getDbNDComm1(),"1", null);

        //add biaya polis
        if(policy.getDbNDPCost()!=null)
            onNewDetailAutomatic2("15","N", null, policy.getDbNDPCost(),null);

        //add biaya materai
        if(policy.getDbNDSFee()!=null)
            onNewDetailAutomatic2("14","N", null, policy.getDbNDSFee(),null);


        getPolicy().recalculateInterkoneksiOnly();

        getPolicy().recalculateInterkoneksiOnly();
        getPolicy().recalculateTreaty();

        getPolicy().generatePersetujuanPrinsipNo();
        getPolicy().generatePolicyNoFromPersetujuanPrinsip();

        //simpen
        getRemoteInsurance().savePolisToInterkoneksi(policy,policy.getStNextStatus(),true);

 
    }

    public void superEdit(String policyID) throws Exception {
        view(policyID); 

        String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markNew();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjectsInterkoneksi();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.setStObjectDescription(obj.getStReference1Desc());
            policy.setStReference5(obj.getStReference1Desc()); //nama principal
            policy.setStReference7(obj.getStReference2());//id principal
            policy.setStReference8(obj.getStReference3());//alamat principal

            obj.getSuminsuredsNotNull().markAllNew();
            obj.getDeductiblesInterkoneksi().markAllNew();
            obj.getCoverageNotNull().markAllNew();

            obj.markNew();

            obj.setStInsuranceTreatyID(treatyID);

            /*
            obj.getTreatiesInterkoneksi().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetailsInterkoneksi();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.getSharesInterkoneksi().markAllUpdate();
            }

            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }

            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");
                */
        }

        policy.getCoinsInterkoneksi().markAllNew();
        policy.getDetailsInterkoneksi().markAllNew();
        policy.getClaimItemsInterkoneksi().markAllNew();
        //policy.getDeductibles().markAllUpdate();
        policy.getInstallmentInterkoneksi().markAllNew();
        policy.getCoinsCoverageInterkoneksi().markAllNew();

        policy.showItemsAccount();

    }

    public void view(String policyID) throws Exception {

        if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(policyID);

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjectsInterkoneksi();
        policy.loadClausulesInterkoneksi();
        policy.loadEntitiesInterkoneksi();
        policy.loadDetailsInterkoneksi();
        policy.loadCoinsInterkoneksi();
        policy.loadInstallmentInterkoneksi();

        super.setReadOnly(true);

    }

    private void checkActiveEffective() {
        if (!policy.isEffective())
            throw new RuntimeException("Please approve the document");

        if (!policy.isActive())
            throw new RuntimeException("Document is not active, please refer to the last active document");
    }

    public InsurancePolicyView getInsurancePolicy(String stPolicyID) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQueryDS("select * from ins_policy where pol_id = ?",
                new Object[]{stPolicyID},
                InsurancePolicyView.class,"GATEWAY").getDTO();

        return pol;
    }

    private void uploadKreditProduktif80(DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek grup : "+ object.getStGroupID() +" debitur "+ object.getStNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(object.getStDataID());
            getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
            getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
            //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
            getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
            getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
            getSelectedDefaultObject().setStReference7(object.getStNoPerjanjianKredit());
            getSelectedDefaultObject().setStReference8(object.getStNoRekeningPinjaman());
            getSelectedDefaultObject().setStReference9(object.getStJenisKreditDebitur());
            getSelectedDefaultObject().setStReference11(object.getStSumberPembayaran());

            if(object.getStPaketCoverageAskrida()!=null)
                getSelectedDefaultObject().setStReference10(object.getStPaketCoverageAskrida());


            /*
            if(object.getStStatusKerja()!=null)
                getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
            else
                getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());
            */

            //tambah tsi
            doAddLampiranSumInsured("577",object.getDbInsuredAmount());

            //tambah cover
            doAddLampiranCoverKreasi("595",object.getDbRate(),object.getDbPremiTotal());

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);
        }


    }

    private BigDecimal dbLimitTotalOutgoPct;
    private BigDecimal dbLimitKomisiPct;
    private BigDecimal dbLimitFeeBasePct;
    private BigDecimal dbLimitBrokeragePct;

    public BigDecimal getDbLimitBrokeragePct() {
        return dbLimitBrokeragePct;
    }

    public void setDbLimitBrokeragePct(BigDecimal dbLimitBrokeragePct) {
        this.dbLimitBrokeragePct = dbLimitBrokeragePct;
    }

    public BigDecimal getDbLimitFeeBasePct() {
        return dbLimitFeeBasePct;
    }

    public void setDbLimitFeeBasePct(BigDecimal dbLimitFeeBasePct) {
        this.dbLimitFeeBasePct = dbLimitFeeBasePct;
    }

    public BigDecimal getDbLimitKomisiPct() {
        return dbLimitKomisiPct;
    }

    public void setDbLimitKomisiPct(BigDecimal dbLimitKomisiPct) {
        this.dbLimitKomisiPct = dbLimitKomisiPct;
    }

    public BigDecimal getDbLimitTotalOutgoPct() {
        return dbLimitTotalOutgoPct;
    }

    public void setDbLimitTotalOutgoPct(BigDecimal dbLimitTotalOutgoPct) {
        this.dbLimitTotalOutgoPct = dbLimitTotalOutgoPct;
    }

    public void getLimitOutgo(DataTeksMasukView data) throws Exception {

        setDbLimitTotalOutgoPct(BDUtil.zero);
        setDbLimitKomisiPct(BDUtil.zero);
        setDbLimitFeeBasePct(BDUtil.zero);
        setDbLimitBrokeragePct(BDUtil.zero);

        String kodePekerjaan = "XX";

        if(data!=null){
            kodePekerjaan = data.getStStatusKerja();
        }

        logger.logDebug("################### limit outgo by kode pekerjaan = "+ kodePekerjaan);

        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 ='999997' and b.fft_group_id='COMM'" +
                "   and (b.ref4 = ? or b.ref4 is null) "+ //--cari by grup sumbis
                "   and (b.ref6 = ? or b.ref6 is null) "+ //--cari by jenis kredit
                "   and (b.ref7 = ? or b.ref7 is null) "+
                "   and active_flag = 'Y'"+
                "   and period_start <= ? and period_end >= ?"+
                "   order by ref7,ref6,ref5,ref4,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), policy.getEntity().getStRef2(), policy.getStKreasiTypeID(), kodePekerjaan, policy.getDtPeriodStart(), policy.getDtPeriodStart()},
                FlexTableView.class
                ).getDTO();

        if(ft!=null){

            if(ft.getDbReference1()!=null)
                setDbLimitTotalOutgoPct(ft.getDbReference1());

            if(ft.getDbReference2()!=null)
                setDbLimitKomisiPct(ft.getDbReference2());

            if(ft.getDbReference4()!=null)
                setDbLimitFeeBasePct(ft.getDbReference4());

            if(ft.getDbReference5()!=null)
                setDbLimitBrokeragePct(ft.getDbReference5());
        }
    }

    public void validateLimitOutgo(DataTeksMasukView data)throws Exception{
        //baca limit h2h dari sistem core
        getLimitOutgo(data);

        if(data.isPromo()){
            getLimitOutgoPromo();
        }

        //cek feebase
        if(data.getDbFeeBase1Pct()!=null){
            
            boolean sesuaiLimit = BDUtil.isEqual(data.getDbFeeBase1Pct(), getDbLimitFeeBasePct(), 2);
            
            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStNama() + " ("+ data.getDbFeeBase1Pct() +") melebihi limit feebase ("+ getDbLimitFeeBasePct()+")");

       }

        //cek komisi
        if(data.getDbKomisi1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbKomisi1Pct(), getDbLimitKomisiPct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStNama() + " ("+ data.getDbKomisi1Pct() +") melebihi limit komisi ("+ getDbLimitKomisiPct()+")");

        }

        //cek brokerfee include ppn
        if(data.getDbBrokerageIncludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageIncludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStNama() + " ("+ data.getDbBrokerageIncludePPNPct() +") melebihi limit Brokerfee Include PPN ("+ getDbLimitBrokeragePct()+")");

        }

        //cek brokerfee exclude ppn
        if(data.getDbBrokerageExcludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageExcludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStNama() + " ("+ data.getDbBrokerageExcludePPNPct() +") melebihi limit Brokerfee Exclude PPN ("+ getDbLimitBrokeragePct()+")");

        }
    }

    private void applyClausules(DataTeksMasukView data) throws Exception {

        String klausula = null;

        if (data.getStKlausula() != null) {
            klausula = data.getStKlausula().trim();
        }

        if (klausula != null && data.getStKlausula() != null) {

            String Clause[] = klausula.split("[\\,]");

            DTOList clausulaPolis1 = getPolicy().getClausules();
            for (int h = 0; h < clausulaPolis1.size(); h++) {
                InsurancePolicyClausulesView claus = (InsurancePolicyClausulesView) clausulaPolis1.get(h);

                //uncek semua klausula
                claus.setStSelectedFlag(null);

                //cek klausula
                for (int k = 0; k < Clause.length; k++) {

                    if (claus.getStClauseID2().equalsIgnoreCase(Clause[k])) {
                        claus.setStSelectedFlag("Y");
                    }
                }

            }

        }


    }

    private void buatSPPAOtomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.SPPA);
            getPolicy().setStActiveFlag("N");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            if(data1.getStCostCenterCodeCore()!=null)
                getPolicy().setStCostCenterCode(data1.getStCostCenterCodeCore());

            if(data1.getStRegionIDCore()!=null)
                getPolicy().setStRegionID(data1.getStRegionIDCore());

            getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
            getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            //getPolicy().setStPolicyNo(data1.getStPolicyNo());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(data1.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");

            if(data1.getStJenisKreditAskrida()!=null)
                getPolicy().setStKreasiTypeID(data1.getStJenisKreditAskrida());

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());

            if(listObjek.size()==1){
                getPolicy().setStCustomerName(bank.getStEntityName()+" QQ "+ data1.getStNama().trim());
            }

            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            //Cek outgo dulu, jika sesuai maka proses, jika tidak maka skip
            boolean isLimitOutgoSesuai = isSesuaiLimitOutgo(data1);

            if(isLimitOutgoSesuai){

                        //cek limit outgo h2h
                        validateLimitOutgo(data1);

                        //add fee base bank tanpa ppn
                        if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                            onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, null);
                        }

                        //add fee base bank include ppn
                        if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                            onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                        }

                        //add fee base bank exclude ppn
                        if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                            onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                        }

                        //add komisi
                        if(data1.getDbKomisi1Pct()!=null){
                            onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1", null);
                        }

                        //add diskon
                        if(data1.getDbDiscountAmount()!=null){
                            onNewDetailAutomatic2("16","N", null,data1.getDbDiscountAmount(),null);
                        }

                        if(data1.getStNomorBuktiBayar()!=null){
                            getPolicy().setStNomorBuktiBayar(data1.getStNomorBuktiBayar());
                        }

                        if(data1.getDtTanggalBayar()!=null){
                            getPolicy().setDtTanggalBayar(data1.getDtTanggalBayar());
                        }

                        if(data1.getStNomorReferensiPembayaran()!=null){
                            getPolicy().setStNomorReferensiPembayaran(data1.getStNomorReferensiPembayaran());
                        }

                        getPolicy().setStGatewayDataFlag("Y");

                        //ADD OBJEK
                        uploadKreditPolis(data1.getStPolicyTypeID(), listObjek, null);

                        //apply warranty
                        if(data1.getStWarranty()!=null){
                            getPolicy().setStWarranty(data1.getStWarranty());
                        }

                        //hitung ulang
                        getPolicy().recalculateInterkoneksi();

                        //getPolicy().defineTreaty();

                        getPolicy().recalculateInterkoneksi();
                        //getPolicy().recalculateTreaty();

                        //getPolicy().invokeCustomHandlersPolicyAuto(false, getSelectedDefaultObject());

                        //simpan
                        if(data1.isCaseByCase()){
                            getPolicy().setStPostedFlag(null);
                            getPolicy().setStEffectiveFlag(null);
                            getPolicy().setStApprovedWho(null);
                            getPolicy().setDtApprovedDate(null);
                            getPolicy().setStClientIP(null);
                            getPolicy().setStReadyToApproveFlag(null);

                            getRemoteInsurance().saveFromTeks(policy,policy.getStNextStatus(),false);

                        }else{
                            getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);
                        }
            }

            


    }

    private void prosesPengajuanPolisFireH2H() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listPolisFire = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB CEK PENGAJUAN AUTO POLIS FIRE YANG BELUM DI TRANSFER");

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        /*
        listPolisFire = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from pengajuan_polis_kebakaran"+
                " where proses_flag is null"+
                " and date_trunc('month',tgl_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone "+
                " and tgl_polis >= '"+ tanggalPolis +"' "+
                " order by data_id",
                PengajuanPolisFireView.class,"GATEWAY");
        */

        listPolisFire = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from pengajuan_polis_kebakaran"+
                " where proses_flag is null"+
                "  "+
                " and tgl_polis >= '"+ tanggalPolis +"' "+
                " order by data_id",
                PengajuanPolisFireView.class,"GATEWAY");

        for (int i = 0; i < listPolisFire.size(); i++) {
            PengajuanPolisFireView polis = (PengajuanPolisFireView) listPolisFire.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getStPolicyNo());

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(polis.getStPolicyNo());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = buatPolisFireOtomatis(polis);

                //if(policyID!=null)
                    //getRemoteInsurance().saveNotaToCare(policy, true);

                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){

                             //UPDATE DATA LKS YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update pengajuan_polis_kebakaran set proses_flag ='Y', tgl_proses = 'now' where data_id = ?");

                            PS.setObject(1, polis.getStDataID());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS polis : "+ polis.getStPolicyNo() +" ++++++++++++++++++");

                            S.release();

                            //simpan dulu document fire nya jika ada

                            //cari dokumen fire dari interkoneksi
                                DTOList listDokumen = ListUtil.getDTOListFromQueryDS(
                                " select * "+
                                " from ws_dokumen_fire  "+
                                " where no_polis = ? "+
                                " order by id",  new Object[]{ polis.getStPolicyNo()}, WSDokumenFireView.class,"GATEWAY");

                            if(listDokumen.size()>0){

                                //simpan dokumen ke LKS
                                for (int k = 0; k < listDokumen.size(); k++) {
                                    WSDokumenFireView doc = (WSDokumenFireView) listDokumen.get(k);

                                    String fileID = saveDocumentFire(doc);

                                    //tambahin dokumen di ins_poL_document
                                    InsurancePolicyDocumentView document = new InsurancePolicyDocumentView();

                                    document.markNew();

                                    document.setStSelectedFlag("Y");
                                    document.setStInsuranceDocumentTypeID("140");
                                    document.setStDocumentClass("POLICY");
                                    document.setStFilePhysic(fileID);
                                    document.setStPolicyID(policyID);
                                    document.setDtUpdateDate(new Date());

                                    document.store();

                                    //UPDATE DATA doc klaim yang sudah di proses
                                    final SQLUtil S2 = new SQLUtil("GATEWAY");

                                    PreparedStatement PS2 = S2.setQuery("update ws_dokumen_fire set doc_transfer_flag = 'Y', doc_transfer_date = 'now' where no_polis = ? ");

                                    PS2.setObject(1, doc.getStNomorPolis());

                                    int m = PS.executeUpdate();

                                    S2.release();

                                    if (m!=0) logger.logInfo("+++++++ UPDATE STATUS dokumen : "+ doc.getStNomorLoan() +" ++++++++++++++++++");

                                }
                            }

                    }
                }
            }

        }

    }

    private String buatPolisFireOtomatis(PengajuanPolisFireView polis) throws Exception{

            boolean premiSesuai = true;

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            //getPolicy().setStCostCenterCode(polis.getStCostCenterCode());
            //getPolicy().setStRegionID(polis.getStRegionID());

            getPolicy().setStCostCenterCode(polis.getStCostCenterCodeSource());
            getPolicy().setStRegionID(polis.getStRegionIDSource());

            getPolicy().setStCostCenterCodeSource(polis.getStCostCenterCodeSource());
            getPolicy().setStRegionIDSource(polis.getStRegionIDSource());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(polis.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(polis.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(polis.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(polis.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(polis.getStPolicyNo());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(polis.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCheckingFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            int tahun = DateUtil.getYearBetween(polis.getDtTanggalAwal(), polis.getDtTanggalAkhir());

            getPolicy().setPeriods("YEAR",tahun);

            if(polis.getStJangkaWaktuBulan()!=null){
                getPolicy().setPeriods("MONTH", Integer.parseInt(polis.getStJangkaWaktuBulan()));
            }

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(polis.getStEntityID());
            getPolicy().setStEntityID(polis.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(polis.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(polis.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            //cek limit outgo h2h
            //validateLimitOutgo(data1);

            //add fee base bank tanpa ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank exclude ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()!=null && polis.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add komisi
            if(polis.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", polis.getStKomisi1EntityID(),polis.getDbKomisi1Pct(),"1", null);
            }

            //add diskon
            if(polis.getDbDiscountAmount()!=null){
                onNewDetailAutomatic2("16","N", null,polis.getDbDiscountAmount(),null);
            }

            //add brokerfee include ppn
            if(polis.getDbBrokerageIncludePPNPct()!=null && polis.getDbBrokerageIncludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNAmount()))
                        onNewDetailAutomatic("88","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageIncludePPNPct(), "5", null);
            }

            //add brokerfee exclude ppn
            if(polis.getDbBrokerageExcludePPNPct()!=null && polis.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageExcludePPNPct(), "5", null);
            }

            //add biaya polis
            if(polis.getDbBiayaPolis()!=null){
                onNewDetailAutomatic2("14","N", null,polis.getDbBiayaPolis(),null);
            }

            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            uploadFirePolis(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            getPolicy().recalculateTreaty();

            //cek premi dulu sesuai gak dengan hitungan ojk core
            BigDecimal selisihPremi = BDUtil.sub(polis.getDbPremiFlexas(), getPolicy().getDbPremiTotal());

            boolean isSelisihPremi = false;

            if (BDUtil.biggerThan(selisihPremi, new BigDecimal(10))) {
                isSelisihPremi = true;
            }
            if (BDUtil.lesserThan(selisihPremi, new BigDecimal(-10))) {
                isSelisihPremi = true;
            }
            if(!BDUtil.isEqual(getPolicy().getDbPremiTotal(), polis.getDbPremiFlexas(),2)){
                //premiSesuai = false;

                logger.logWarning("################ premi web = "+ getPolicy().getDbPremiTotal());
                logger.logWarning("################ premi h2h = "+ polis.getDbPremiFlexas());

            }

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

            if(!isSelisihPremi){
                 //policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);
            }

            return policyID;


            //simpan
            //getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

    }

    private void uploadFirePolis(PengajuanPolisFireView polis, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getStPolicyNo() +" debitur "+ polis.getStNamaDebitur()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(polis.getStDataID());
            getSelectedDefaultObject().setStReference1(polis.getStPenggunaan()); //penggunaan
            getSelectedDefaultObject().setStRiskClass(polis.getStKelasKonstruksi());
            getSelectedDefaultObject().setStReference4(polis.getStPenerangan());
            getSelectedDefaultObject().setStReference5(polis.getStAlamatRisiko());
            getSelectedDefaultObject().setStReference16(polis.getStProvinsi());


            getSelectedDefaultObject().setStReference9(polis.getStKodePos());

            if(polis.getStKodePos().equalsIgnoreCase("-"))
                getSelectedDefaultObject().setStReference9("00000");

            getSelectedDefaultObject().setDtReference1(polis.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference2(polis.getDtTanggalAkhir());
            getSelectedDefaultObject().setStRiskCategoryCode1(polis.getStKodeRisiko());
            getSelectedDefaultObject().setStReference11(polis.getStNamaDebitur());
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

            //batas kiri
            if(polis.getStBatasKiri()!=null)
                getSelectedDefaultObject().setStReference22(polis.getStBatasKiri());

            //batas kanan
            if(polis.getStBatasKanan()!=null)
                getSelectedDefaultObject().setStReference23(polis.getStBatasKanan());

            //batas depan
            if(polis.getStBatasDepan()!=null)
                getSelectedDefaultObject().setStReference24(polis.getStBatasDepan());

            //batas belakang
            if(polis.getStBatasBelakang()!=null)
                getSelectedDefaultObject().setStReference25(polis.getStBatasBelakang());

            //tambah tsi
            doAddLampiranSumInsured("2",polis.getDbSumInsured());

            //add coverage
            logger.logDebug("############# rate asli dr oji = "+ polis.getDbRatePct());
            //doAddLampiranCoverKreasiRateOri("512", BDUtil.round(BDUtil.getMileFromPCT(polis.getDbRatePct()),3),polis.getDbPremiFlexas());
            doAddLampiranCoverKreasiRateOri("512", null,polis.getDbPremiFlexas());


            //tambah deductible flexas
            if(polis.getDbDeductiblePctOfClaim()!=null){
                onNewObjDeductible("3910", polis.getDbDeductiblePctOfClaim(), "DED_PCT_CLAIM", polis.getDbDeductibleMinimum(), polis.getDbDeductibleMaximum());
            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


    }

    public InsurancePolicyView getPolicyExisting(String stPolicyNo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select pol_id, pol_no "+
                                        " from ins_policy  "+
                                        " where status in ('POLICY','RENEWAL') and active_flag = 'Y' "+
                                        " and pol_no = ?",
                new Object[]{stPolicyNo},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    private void prosesPolisKumpulanCIT() throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP NOMOR POLIS CIT YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select pol_no,entity_id,count(data_id) as jumlah "+
                " from pengajuan_polis_cit  "+
                " where proses_flag is null  "+
                " group by pol_no,entity_id "+
                " order by pol_no",
                PengajuanPolisCITView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            PengajuanPolisCITView grup = (PengajuanPolisCITView) listGroup.get(i);

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(grup.getStPolicyNo());

            //jika belum ada di core, inject to core
            if(polisExisting==null){

                DTOList listObjek = null;

                //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
                listObjek = ListUtil.getDTOListFromQueryDS(
                        " select * "+
                         "    from pengajuan_polis_cit "+
                         "    where proses_flag is null "+
                         "    and pol_no = ? and entity_id = ?"+
                         "    order by data_id",
                         new Object [] {grup.getStPolicyNo(), grup.getStEntityID()},
                       PengajuanPolisCITView.class,"GATEWAY");

                if(listObjek.size()==0) continue;

                logger.logDebug("Bikin polis H2H CIT kumpulan nomor polis : "+ grup.getStPolicyNo());

                //BUAT POLIS BERDASARKAN NOMOR POLIS
                buatPolisCITOtomatis(listObjek);
            }
        }

    }

    private void buatPolisCITOtomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            PengajuanPolisCITView data1 = (PengajuanPolisCITView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            getPolicy().setStCostCenterCode("80");
            getPolicy().setStRegionID("85");

            if(data1.getStCostCenterCode()!=null)
                getPolicy().setStCostCenterCodeSource(data1.getStCostCenterCode());

            if(data1.getStRegionID()!=null)
                getPolicy().setStRegionIDSource(data1.getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtTanggalBerangkat());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalBerangkat());
            getPolicy().setStPolicyTypeID(data1.getStJenisAsuransi());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(data1.getStPolicyNo());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(data1.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            //if(data1.getStTransactionNo()!=null)
                //getPolicy().setStReferenceNo(data1.getStTransactionNo());

            //cek limit outgo h2h
            validateLimitOutgoCIT(data1);

            //add fee base bank tanpa ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null,null);
            }

            //add fee base bank exclude ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null,null);
            }

            //add komisi
            if(data1.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1",null);
            }

            //add diskon
            if(data1.getDbDiscountAmount()!=null){
                //onNewDetailAutomatic2("16","N", null,data1.getDbDiscountAmount(),null);
            }

            //add diskon
            if(data1.getDbDiscountPct()!=null){
                onNewDetailAutomatic("16","N", null,data1.getDbDiscountPct(),null,null);
            }

            //add brokerfee include ppn
            if(data1.getDbBrokerageIncludePPNPct()!=null && data1.getDbBrokerageIncludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNAmount()))
                        onNewDetailAutomatic("88","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageIncludePPNPct(), "5",null);
            }

            //add brokerfee exclude ppn
            if(data1.getDbBrokerageExcludePPNPct()!=null && data1.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageExcludePPNPct(), "5",null);
            }

            if(data1.getStNoReferensiBayar()!=null){
                //getPolicy().setStNomorBuktiBayar(data1.getStNoReferensiBayar());
            }

            if(data1.getDtTanggalBayarPremi()!=null){
                getPolicy().setDtTanggalBayar(data1.getDtTanggalBayarPremi());
            }

            if(data1.getStNoReferensiBayar()!=null){
                getPolicy().setStNomorReferensiPembayaran(data1.getStNoReferensiBayar());
            }

            getPolicy().setStGatewayDataFlag("Y");

            //ADD OBJEK
            uploadCIT(data1.getStJenisAsuransi(), listObjek, null);

            //apply warranty
//            if(data1.getStWarranty()!=null){
//                getPolicy().setStWarranty(data1.getStWarranty());
//            }

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            getPolicy().defineTreaty(); 

            getPolicy().recalculateInterkoneksi();
            getPolicy().recalculateTreaty();

            //getPolicy().invokeCustomHandlersPolicyAuto(false, getSelectedDefaultObject());

            //apply pilihan klausula
//            applyClausules(data1);

            //simpan
            String policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

            //getRemoteInsurance().saveNotaToCare(policy, true);

            final SQLUtil S = new SQLUtil("GATEWAY");

            for (int i = 0; i < listObjek.size(); i++) {
                PengajuanPolisCITView data = (PengajuanPolisCITView) listObjek.get(i);

                //UPDATE DATA YANG SUDAH DI PROSES
                PreparedStatement PS = S.setQuery("update pengajuan_polis_cit set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

                PS.setObject(1, data.getStDataID());
                PS.setObject(2, data.getStKodeBank());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS detil : "+ data.getStDataID() +" ++++++++++++++++++");

            }

            S.release();

    }

    public void validateLimitOutgoCIT(PengajuanPolisCITView data)throws Exception{
        //baca limit h2h dari sistem core
        getLimitOutgo(null);

        //cek feebase
        if(data.getDbFeeBase1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbFeeBase1Pct(), getDbLimitFeeBasePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbFeeBase1Pct() +") melebihi limit feebase ("+ getDbLimitFeeBasePct()+")");

       }

        //cek komisi
        if(data.getDbKomisi1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbKomisi1Pct(), getDbLimitKomisiPct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbKomisi1Pct() +") melebihi limit komisi ("+ getDbLimitKomisiPct()+")");

        }

        //cek brokerfee include ppn
        if(data.getDbBrokerageIncludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageIncludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbBrokerageIncludePPNPct() +") melebihi limit komisi ("+ getDbLimitBrokeragePct()+")");

        }

        //cek brokerfee exclude ppn
        if(data.getDbBrokerageExcludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageExcludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbBrokerageExcludePPNPct() +") melebihi limit komisi ("+ getDbLimitBrokeragePct()+")");

        }
    }

    private void uploadObjectCITPolis(DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            PengajuanPolisCITView object = (PengajuanPolisCITView) objek.get(i);

            System.out.println("add objek grup : "+ object.getStPolicyNo() +" debitur "+ object.getStNamaDebitur()+" ...........");

            //tambah objek
            doNewLampiranObject();
            /*

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(object.getStDataID());
            getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
            getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
            //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
            getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
            getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
            getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
            getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
            getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
            getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

            if(object.getStPaketCoverageAskrida()!=null)
                getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());

            if(object.getStStatusKerja()!=null)
                getSelectedDefaultObject().setStReference7(object.getStStatusKerja());
            else
                getSelectedDefaultObject().setStReference7(object.getKodePekerjaan());

            if(object.getDtTanggalSTNC()!=null){
                getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                getSelectedDefaultObject().setStReference10("Y");
            }

            if(object.getStPersetujuanPusat()!=null)
                getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());

            if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                if(object.getStPersetujuanPusat()==null)
                    getSelectedDefaultObject().setStReference10("Y");

            if(object.getStKodeTransaksi()!=null){
                getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
            }

            //tambah tsi
            doAddLampiranSumInsured("488",object.getDbInsuredAmount());

            //tambah cover kredit
            if(getPolicy().isBankJatengH2H()){
                if(!BDUtil.isZeroOrNull(object.getDbRate()))
                    doAddLampiranCoverKreasiRateOri("444",object.getDbRate(),object.getDbPremiTotal());
            }else{
                doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal());
            }

            //tambah cover restitusi jika ada premi restitusi nya
            if(object.isTopUp()){
                if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                    doAddLampiranCoverKreasi("573",null,object.getDbPremiRefund());
                }
            }
            */

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

    public void onNewObjDeductible(String insuranceClaimCauseID, BigDecimal pct, String jenis, BigDecimal min, BigDecimal max) {
        final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

        ded.setStInsuranceClaimCauseID(insuranceClaimCauseID);

        if(!BDUtil.isZeroOrNull(pct)) ded.setDbPct(pct);

        if(jenis!=null) ded.setStDeductType(jenis);

        if(!BDUtil.isZeroOrNull(min)) ded.setDbAmountMin(min);

        if(!BDUtil.isZeroOrNull(max)) ded.setDbAmountMax(max);

        ded.markNew();

        selectedDefaultObject.getDeductibles().add(ded);

    }

    public String saveDocumentFire(WSDokumenFireView doc) throws Exception{
            //simpan file nya
           String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

           String sf = sdf.format(new Date());

           String tempPath = fileFOlder+File.separator+sf;
           String path1 = fileFOlder+File.separator;

           try {
              new File(path1).mkdir();
              new File(tempPath).mkdir();
           } catch (Exception e) {
           }

           File source = new File(doc.getStInterkoneksiFilePath());

           String destination = tempPath+File.separator+System.currentTimeMillis();

           File dest = new File(destination);

            //COPY FILE softcopy nya
            copyFileUsingApacheCommonsIO(source, dest,destination);

            //simpen ke tabel s_file
            FileView file = new FileView();

            file.markNew();
            file.setStOriginalName(doc.getStFileName());
            file.setDbFileSize(doc.getDbFileSize());
            file.setDtFileDate(new Date());
            file.setStMimeType(doc.getStFileType());
            file.setDbOriginalSize(doc.getDbFileSize());
            file.setStDescription(doc.getStNomorLoan() + " " + doc.getStKodeDokumen());
            file.setStImageFlag("N");
            file.determineFileType();

            file.setStFilePath(destination);

            file.store();

            return file.getStFileID();
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest,String pathDest) throws IOException {

            try {
                    int index = pathDest.lastIndexOf("\\")+ 1 ;
                    new File(pathDest.substring(0, index)).mkdir();
                    System.out.println ("bikin folder : "+ pathDest.substring(0, index));

               } catch (Exception e) {
               }


            FileUtils.copyFile(source, dest);

    }

    private void cekPolisH2HPendingToCore() throws Exception{

        DTOList listGroup = null;

        // buat proposal per orang/debitur

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select a.* "+
                    " from data_teks_masuk a "+
                    " left join ent_master b on a.entity_id = b.ent_id"+
                    " where kategori = 'HOST' and status in ('POLIS','CBC') and proses_flag is null and coalesce(valid_f,'Y') = 'Y' "+
                    " and b.ref2::bigint in (select vs_code from s_company_group where host_to_host_flag = 'Y')  "+
                    " and (no_rek_pinjaman is not null and no_perjanjian_kredit is not null and no_identitas is not null "+
                    " and paket_coverage_askrida is not null and tgl_awal < tgl_akhir "+
                    " and usia >= 17 and usia <= 75 and entity_id is not null and upper(nama) not like '%#TES%'"+
                    " ) and EXTRACT(EPOCH FROM ('now' - tgl_transfer)) > 7200"+
                    " order by data_id",
                    DataTeksMasukView.class,"GATEWAY");

        if(listGroup!=null){
            if(listGroup.size()!=0){

                 String receiver = "doni@askrida.co.id";
                    String subject = "WARNING MESSAGE SCHEDULER PENGAJUAN POLIS H2H Tanggal " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
                    String text = "Kepada Yth.\n"
                            + "-  Administrator\n"
                            + "-  Sistem WebCore Askrida\n"
                            + "Di Tempat\n\n\n"
                            + "Dengan hormat,\n\n"
                            + "Bersama ini kami beritahukan Cek Data Pengajuan Polis H2H, mungkin ada trouble sehingga terjadi pending antrian "+ listGroup.size() +" data masuk ke sistem core\n"
                            + "Demikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                            + "Hormat kami,\n"
                            + "Administrator";

                    MailUtil2 mail = new MailUtil2();

                    //mail.sendEmailNoSender(receiver, subject, text);

            }
        }

    }

    private void prosesPolisKonstruksiH2H() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listPolisKonstruksi = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB CEK AUTO POLIS KONSTRUKSI YANG BELUM DI TRANSFER");

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        listPolisKonstruksi = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from pengajuan_polis_konstruksi"+
                " where proses_flag is null"+
                " and date_trunc('month',tanggal_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone "+
                " and tanggal_polis >= '"+ tanggalPolis +"' "+
                " order by id_pengajuan_polis",
                PengajuanPolisKonstruksiView.class,"GATEWAY");

        for (int i = 0; i < listPolisKonstruksi.size(); i++) {
            PengajuanPolisKonstruksiView polis = (PengajuanPolisKonstruksiView) listPolisKonstruksi.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getStPolicyNo());

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(polis.getStPolicyNo());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = createPolisKonstruksi(polis);

                //if(policyID!=null)
                    //getRemoteInsurance().saveNotaToCare(policy, true);

                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){

                             //UPDATE DATA LKS YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update pengajuan_polis_konstruksi set proses_flag ='Y', tgl_proses = 'now' where id_pengajuan_polis = ?");

                            PS.setObject(1, polis.getStDataID());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS polis : "+ polis.getStPolicyNo() +" ++++++++++++++++++");

                            S.release();
                    }
                }
            }

        }

    }

     private String createPolisKonstruksi(PengajuanPolisKonstruksiView polis) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(polis.getStCostCenterCode());
            getPolicy().setStRegionID(polis.getStRegionID());

            getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
            getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(polis.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(polis.getDtTanggalRealisasiKredit());
            getPolicy().setDtPeriodEnd(polis.getDtTanggalJatuhTempo());
            getPolicy().setStPolicyTypeID("60");
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(polis.getStPolicyNo());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(polis.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            int tahun = DateUtil.getYearBetween(polis.getDtTanggalRealisasiKredit(), polis.getDtTanggalJatuhTempo());

            getPolicy().setPeriods("YEAR",tahun);

            //if(polis.getStJangkaWaktuBulan()!=null){
                //getPolicy().setPeriods("MONTH", Integer.parseInt(polis.getStJangkaWaktuBulan()));
            //}

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(polis.getStEntityID());
            getPolicy().setStEntityID(polis.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(polis.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            /*
            String defaultPemasarID = getDefaultPemasar(polis.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }*/

            //cek limit outgo h2h
            //validateLimitOutgo(data1);

            //add fee base bank tanpa ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank exclude ppn
            if(polis.getDbFeeBasePPN1ExcludePct()!=null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),polis.getDbFeeBasePPN1ExcludePct(), null, null);
            }

            //add komisi
            if(polis.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", polis.getStKomisi1EntityID(),polis.getDbKomisi1Pct(),"1", null);
            }

            //add diskon
            if(polis.getDbDiscountAmount()!=null){
                onNewDetailAutomatic2("16","N", null,polis.getDbDiscountAmount(),null);
            }

            //add brokerfee include ppn
            if(polis.getDbBrokerageIncludePPNPct()!=null && polis.getDbBrokerageIncludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNAmount()))
                        onNewDetailAutomatic("88","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageIncludePPNPct(), "5", null);
            }

            //add brokerfee exclude ppn
            if(polis.getDbBrokerageExcludePPNPct()!=null && polis.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageExcludePPNPct(), "5", null);
            }

            //add biaya polis
            if(polis.getDbBiayaAdmin()!=null){
                onNewDetailAutomatic2("15","N", null,polis.getDbBiayaAdmin(),null);
            }

            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            uploadKonstruksi(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksiKonstruksi();

            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksiKonstruksi();
            getPolicy().recalculateTreaty();

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

            return policyID;


            //simpan
            //getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

    }

     private void uploadKonstruksi(PengajuanPolisKonstruksiView polis, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getStPolicyNo() +" debitur "+ polis.getStNamaPrincipal()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(polis.getStDataID());
            getSelectedDefaultObject().setStReference2(polis.getStEntityID()); //tertanggung

            EntityView tertanggung = polis.getEntity(polis.getStEntityID());

            getSelectedDefaultObject().setStReference1Desc(tertanggung.getStEntityName()); //alamat
            getSelectedDefaultObject().setStReference2Desc(tertanggung.getStEntityName()); //alamat
            getSelectedDefaultObject().setStReference3(tertanggung.getStAddress()); //alamat
            getSelectedDefaultObject().setStReference4(polis.getStPejabatBank());  //pejabat
            getSelectedDefaultObject().setStReference5(polis.getStJabatanPejabat()); //jabatan

            EntityView penanggung = polis.getEntity(polis.getStPenanggungEntityID());

            getSelectedDefaultObject().setStReference7(penanggung.getStEntityID()); //penanggung
            getSelectedDefaultObject().setStReference7Desc(penanggung.getStEntityName()); //penanggung
            getSelectedDefaultObject().setStReference8(penanggung.getStAddress()); //alamat penanggung
            getSelectedDefaultObject().setStReference9(penanggung.getStFunctionaryName());  //pejabat penanggung
            getSelectedDefaultObject().setStReference10(penanggung.getStFunctionaryPosition()); //jabatan penanggung


            getSelectedDefaultObject().setStReference11(polis.getStNoPK()); //no pk
            getSelectedDefaultObject().setDtReference1(polis.getDtTanggalPK()); //tgl pk

            EntityView debitur = polis.getEntity(polis.getStKodePrincipal());

            getSelectedDefaultObject().setStReference14(debitur.getStEntityID()); //nama debitur
            getSelectedDefaultObject().setStReference3Desc(debitur.getStEntityName()); //alamat debitur
            getSelectedDefaultObject().setStReference15(debitur.getStAddress()); //alamat debitur
            getSelectedDefaultObject().setStReference16(debitur.getStFunctionaryName());  //pejabat debitur
            getSelectedDefaultObject().setStReference17(debitur.getStFunctionaryPosition()); //jabatan debitur

            getSelectedDefaultObject().setDbReference1(polis.getDbPlafonKredit()); //plafond
            getSelectedDefaultObject().setStReference19(polis.getStNamaProyek()); //penggunaan
            getSelectedDefaultObject().setDtReference4(polis.getDtTanggalRealisasiKredit()); //tgl awal
            getSelectedDefaultObject().setDtReference5(polis.getDtTanggalJatuhTempo());

            getSelectedDefaultObject().setStReference23(polis.getStNamaObligee()); //nama obligee

            String agunan = "";

            if(polis.getStJenisAgunan().equalsIgnoreCase("1")) agunan = "CASH COLLATERAL";
            else if(polis.getStJenisAgunan().equalsIgnoreCase("2")) agunan = "FIXED ASET";


            getSelectedDefaultObject().setStReference25(agunan); //agunan
            getSelectedDefaultObject().setDbReference2(polis.getDbNilaiTaksasiAgunan()); //nilai agunan

            String lol="";

            if(polis.getStCoverage().equalsIgnoreCase("1")) lol = "75";
            else if(polis.getStCoverage().equalsIgnoreCase("2")) lol = "65";
            else if(polis.getStCoverage().equalsIgnoreCase("3")) lol = "55";
            else if(polis.getStCoverage().equalsIgnoreCase("4")) lol = "45";
            else if(polis.getStCoverage().equalsIgnoreCase("5")) lol = "35";


            getSelectedDefaultObject().setDbLimitOfLiability(new BigDecimal(lol));


            //tambah tsi
            doAddLampiranSumInsured("498",polis.getDbPlafonKredit());

            //add coverage
            doAddLampiranCoverKreasiRateOri("469", polis.getDbRate(),polis.getDbPremi());

            //tambah deductible flexas
//            if(polis.getDbDeductiblePctOfClaim()!=null){
//                onNewObjDeductible("3910", polis.getDbDeductiblePctOfClaim(), "DED_PCT_CLAIM", polis.getDbDeductibleMinimum(), polis.getDbDeductibleMaximum());
//            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


    }

     public void getLimitOutgoPromo() throws Exception {

        setDbLimitTotalOutgoPct(BDUtil.zero);
        setDbLimitKomisiPct(BDUtil.zero);
        setDbLimitFeeBasePct(BDUtil.zero);
        setDbLimitBrokeragePct(BDUtil.zero);

        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 ='999997' and b.fft_group_id='COMM'" +
                "   and (b.ref4 = ? or b.ref4 is null) "+ //--cari by grup sumbis
                "   and (b.ref6 = ? or b.ref6 is null) "+ //--cari by jenis kredit
                "   and active_flag = 'Y'"+
                "   and period_start <= ? and period_end >= ?"+
                "   order by ref6,ref5,ref4,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), policy.getEntity().getStRef2(), "999", policy.getDtPeriodStart(), policy.getDtPeriodStart()},
                FlexTableView.class
                ).getDTO();

        if(ft!=null){

            if(ft.getDbReference1()!=null)
                setDbLimitTotalOutgoPct(ft.getDbReference1());

            if(ft.getDbReference2()!=null)
                setDbLimitKomisiPct(ft.getDbReference2());

            if(ft.getDbReference4()!=null)
                setDbLimitFeeBasePct(ft.getDbReference4());

            if(ft.getDbReference5()!=null)
                setDbLimitBrokeragePct(ft.getDbReference5());
        }
    }

     public boolean isSesuaiLimitOutgo(DataTeksMasukView data)throws Exception{

         boolean cekLimitOke = true;

        //baca limit h2h dari sistem core
        getLimitOutgo(data);

        if(data.isPromo()){
            getLimitOutgoPromo();
        }

        logger.logDebug("#################### CEK KOMISI POLIS "+ data.getStPolicyNo() +"##################");
        logger.logDebug("############ param limit komisi = "+ getDbLimitKomisiPct());
        logger.logDebug("############ param limit feebase  = "+ getDbLimitFeeBasePct());

        logger.logDebug("############ komisi polis "+ data.getStPolicyNo() +" = "+ data.getDbKomisi1Pct());
        logger.logDebug("########### feebase polis "+ data.getStPolicyNo() +" = "+ data.getDbFeeBase1Pct());
        logger.logDebug("#################### END #######################");

        //cek feebase
        if(data.getDbFeeBase1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbFeeBase1Pct(), getDbLimitFeeBasePct(), 2);

            if(!sesuaiLimit) cekLimitOke = false;

       }

        //cek komisi
        if(data.getDbKomisi1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbKomisi1Pct(), getDbLimitKomisiPct(), 2);

            if(!sesuaiLimit) cekLimitOke = false;

        }

        //cek brokerfee include ppn
        if(data.getDbBrokerageIncludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageIncludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) cekLimitOke = false;

        }

        //cek brokerfee exclude ppn
        if(data.getDbBrokerageExcludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageExcludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) cekLimitOke = false;

        }

        return cekLimitOke;
    }

     private void prosesPolisKbgH2H() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listPolisKBG = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB CEK AUTO POLIS KBG YANG BELUM DI TRANSFER");

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        listPolisKBG = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from pengajuan_polis_kbg"+
                " where proses_flag is null"+
                " and date_trunc('month',tanggal_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone "+
                " and tanggal_polis >= '"+ tanggalPolis +"' "+
                " order by id_pengajuan_polis",
                PengajuanPolisKBGView.class,"GATEWAY");

        for (int i = 0; i < listPolisKBG.size(); i++) {
            PengajuanPolisKBGView polis = (PengajuanPolisKBGView) listPolisKBG.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getStPolicyNo());

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(polis.getStPolicyNo());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = createPolisKBG(polis);

                //if(policyID!=null)
                    //getRemoteInsurance().saveNotaToCare(policy, true);

                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){

                             //UPDATE DATA YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update pengajuan_polis_kbg set proses_flag ='Y', tgl_proses = 'now' where id_pengajuan_polis = ?");

                            PS.setObject(1, polis.getStDataID());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS polis : "+ polis.getStPolicyNo() +" ++++++++++++++++++");

                            S.release();
                    }
                }
            }

        }

    }

     private String createPolisKBG(PengajuanPolisKBGView polis) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(polis.getStCostCenterCode());
            getPolicy().setStRegionID(polis.getStRegionID());

            getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
            getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(polis.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(polis.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(polis.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(polis.getStJenisAsuransi());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(polis.getStPolicyNo());
            getPolicy().setStReference1(polis.getStPolicyNo().substring(2));//no pp
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(polis.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            int tahun = DateUtil.getYearBetween(polis.getDtTanggalAwal(), polis.getDtTanggalAkhir());

            getPolicy().setPeriods("YEAR",tahun);

            //if(polis.getStJangkaWaktuBulan()!=null){
                //getPolicy().setPeriods("MONTH", Integer.parseInt(polis.getStJangkaWaktuBulan()));
            //}

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(polis.getStEntityID());
            getPolicy().setStEntityID(polis.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(polis.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            /*
            String defaultPemasarID = getDefaultPemasar(polis.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }*/

            //cek limit outgo h2h
            //validateLimitOutgo(data1);

            //add fee base bank tanpa ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank exclude ppn
            if(polis.getDbFeeBasePPN1ExcludePct()!=null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),polis.getDbFeeBasePPN1ExcludePct(), null, null);
            }

            //add komisi
            if(polis.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", polis.getStKomisi1EntityID(),polis.getDbKomisi1Pct(),"1", null);
            }

            //add diskon
            if(polis.getDbDiscountAmount()!=null){
                onNewDetailAutomatic2("16","N", null,polis.getDbDiscountAmount(),null);
            }

            //add brokerfee include ppn
            if(polis.getDbBrokerageIncludePPNPct()!=null && polis.getDbBrokerageIncludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageIncludePPNAmount()))
                        onNewDetailAutomatic("88","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageIncludePPNPct(), "5", null);
            }

            //add brokerfee exclude ppn
            if(polis.getDbBrokerageExcludePPNPct()!=null && polis.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageExcludePPNPct(), "5", null);
            }

            //add biaya polis
            if(polis.getDbBiayaAdmin()!=null){
                onNewDetailAutomatic2("15","N", null,polis.getDbBiayaAdmin(),null);
            }

            //add biaya materai
            if(polis.getDbBiayaMaterai()!=null){
                onNewDetailAutomatic2("14","N", null,polis.getDbBiayaMaterai(),null);
            }

            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            uploadKBG(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            getPolicy().recalculateTreaty();

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

            //getRemoteInsurance().saveNotaToCare(policy, true);

            return policyID;


    }

     private void uploadKBG(PengajuanPolisKBGView polis, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getStPolicyNo() +" debitur "+ polis.getStNamaPrincipal()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(polis.getStDataID());

            EntityView principal = polis.getEntity(polis.getStKodePrincipal());

            getSelectedDefaultObject().setStReference2(polis.getStKodePrincipal()); //principal ent id
            getSelectedDefaultObject().setStReference1(principal.getStEntityName()); //nama desc
            getSelectedDefaultObject().setStReference1Desc(principal.getStEntityName()); //nama desc
            getSelectedDefaultObject().setStReference2Desc(principal.getStEntityName()); //nama desc

            getSelectedDefaultObject().setStReference17(polis.getStPejabatPrincipal());  //pejabat
            getSelectedDefaultObject().setStReference3(principal.getStAddress()); //alamat

            getSelectedDefaultObject().setStReference4(polis.getStNomorSPKBG()); //no spkbg
            getSelectedDefaultObject().setDtReference1(polis.getDtTanggalSPKBG()); //tgl spkbg
            getSelectedDefaultObject().setStReference5(polis.getStNamaObligee()); //nama obligee
            getSelectedDefaultObject().setStReference6(polis.getStNamaProyek()); //nama proyek
            getSelectedDefaultObject().setStReference7(polis.getStNomorSPK()); //no spk
            getSelectedDefaultObject().setDtReference2(polis.getDtTanggalSPK()); //tgl spk
            getSelectedDefaultObject().setStReference11(polis.getStNomorKontrak()); //no kontrak
            getSelectedDefaultObject().setDtReference3(polis.getDtTanggalKontrak()); //tgl kontrak
            getSelectedDefaultObject().setStReference12(polis.getStNomorBG()); //no bg
            getSelectedDefaultObject().setDtReference4(polis.getDtTanggalBG()); //tgl bg
            getSelectedDefaultObject().setStReference13(polis.getStNomorUndangan()); //no und
            getSelectedDefaultObject().setDtReference5(polis.getDtTanggalUndangan()); //tgl und

            getSelectedDefaultObject().setDbReference3(polis.getDbNilaiProyek()); //nilai proyek
            getSelectedDefaultObject().setStReference8(polis.getStJenisAgunan()); //jenis agunan
            getSelectedDefaultObject().setDbReference4(polis.getDbNilaiTaksasiAgunan()); //nilai agunan

            EntityView bank = polis.getEntity(polis.getStEntityID());

            getSelectedDefaultObject().setStReference9(bank.getStEntityID());  //bank

            getSelectedDefaultObject().setStReference9Desc(bank.getStEntityName());  //bank

            getSelectedDefaultObject().setStReference10(polis.getStNomorRekeningBank()); //no rek bank
            getSelectedDefaultObject().setDbReference6(polis.getDbBiayaAdmin()); //admin
            getSelectedDefaultObject().setDbReference7(polis.getDbBiayaMaterai()); //materai

            
            /*
            String agunan = "";

            if(polis.getStJenisAgunan().equalsIgnoreCase("1")) agunan = "CASH COLLATERAL";
            else if(polis.getStJenisAgunan().equalsIgnoreCase("2")) agunan = "FIXED ASET";


            getSelectedDefaultObject().setStReference25(agunan); //agunan
            getSelectedDefaultObject().setDbReference2(polis.getDbNilaiTaksasiAgunan()); //nilai agunan

            String lol="";

            if(polis.getStCoverage().equalsIgnoreCase("1")) lol = "75";
            else if(polis.getStCoverage().equalsIgnoreCase("2")) lol = "65";
            else if(polis.getStCoverage().equalsIgnoreCase("3")) lol = "55";
            else if(polis.getStCoverage().equalsIgnoreCase("4")) lol = "45";
            else if(polis.getStCoverage().equalsIgnoreCase("5")) lol = "35";

            getSelectedDefaultObject().setDbLimitOfLiability(new BigDecimal(lol));
            */


            //tambah tsi
            if(polis.getStJenisAsuransi().equalsIgnoreCase("55")){
                doAddLampiranSumInsured("445",polis.getDbNilaiKBG());

                //add coverage
                doAddLampiranCoverKreasiRateOri("169", polis.getDbRate(),polis.getDbPremi());
            }

            if(polis.getStJenisAsuransi().equalsIgnoreCase("56")){
                doAddLampiranSumInsured("446",polis.getDbNilaiKBG());

                //add coverage
                doAddLampiranCoverKreasiRateOri("170", polis.getDbRate(),polis.getDbPremi());
            }

            if(polis.getStJenisAsuransi().equalsIgnoreCase("57")){
                doAddLampiranSumInsured("447",polis.getDbNilaiKBG());

                //add coverage
                doAddLampiranCoverKreasiRateOri("240", polis.getDbRate(),polis.getDbPremi());
            }

            if(polis.getStJenisAsuransi().equalsIgnoreCase("58")){
                doAddLampiranSumInsured("448",polis.getDbNilaiKBG());

                //add coverage
                doAddLampiranCoverKreasiRateOri("241", polis.getDbRate(),polis.getDbPremi());
            }
            

            //tambah deductible flexas
//            if(polis.getDbDeductiblePctOfClaim()!=null){
//                onNewObjDeductible("3910", polis.getDbDeductiblePctOfClaim(), "DED_PCT_CLAIM", polis.getDbDeductibleMinimum(), polis.getDbDeductibleMaximum());
//            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


    }

     private void prosesPolisTanggungGugatH2H() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listPolis = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB POLIS H2H TANGGUNG GUGAT YANG BELUM DI TRANSFER");

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        listPolis = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from pengajuan_polis_tanggung_gugat"+
                " where proses_flag is null"+
                " and date_trunc('month',tgl_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone "+
                " and tgl_polis >= '"+ tanggalPolis +"' "+
                " order by data_id",
                PengajuanPolisTanggungGugat.class,"GATEWAY");

        for (int i = 0; i < listPolis.size(); i++) {
            PengajuanPolisTanggungGugat polis = (PengajuanPolisTanggungGugat) listPolis.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getPolNo());

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(polis.getPolNo());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = createPolisTanggungGugat(polis);

                //if(policyID!=null)
                    //getRemoteInsurance().saveNotaToCare(policy, true);

                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){

                             //UPDATE DATA LKS YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update pengajuan_polis_tanggung_gugat set proses_flag ='Y', tgl_proses = 'now' where data_id = ?");

                            PS.setObject(1, polis.getDataId());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS polis : "+ polis.getPolNo() +" ++++++++++++++++++");

                            S.release();
                    }
                }
            }

        }

    }

     private String createPolisTanggungGugat(PengajuanPolisTanggungGugat polis) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));

            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(polis.getCcCode());
            getPolicy().setStRegionID(polis.getRegionId());

            getPolicy().setStCostCenterCodeSource(polis.getCcCodeSource());
            getPolicy().setStRegionIDSource(polis.getRegionIdSource());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(polis.getTglPolis());
            getPolicy().setDtPeriodStart(polis.getPeriodeAwal());
            getPolicy().setDtPeriodEnd(polis.getPeriodeAkhir());
            getPolicy().setStPolicyTypeID("86");
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(polis.getPolNo());

            /*
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(polis.getTglPolis());
            */

            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            int tahun = DateUtil.getYearBetween(polis.getPeriodeAwal(), polis.getPeriodeAkhir());

            getPolicy().setPeriods("YEAR",tahun);

            //if(polis.getStJangkaWaktuBulan()!=null){
                //getPolicy().setPeriods("MONTH", Integer.parseInt(polis.getStJangkaWaktuBulan()));
            //}

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2("947356");
            getPolicy().setStEntityID("947356");
            getPolicy().setStCustomerName(polis.getNama());
            getPolicy().setStCustomerAddress(polis.getAlamat());

            EntityView prod = getPolicy().getEntity2(polis.getMarketerEntId());

            getPolicy().setStProducerID(prod.getStEntityID());
            getPolicy().setStProducerName(prod.getStEntityName());
            getPolicy().setStProducerAddress(prod.getStAddress());

            /*
            String defaultPemasarID = getDefaultPemasar(polis.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }*/

            //cek limit outgo h2h
            //validateLimitOutgo(data1);

            //add biaya polis
            if(polis.getBiayaPolis()!=null){
                onNewDetailAutomatic2("15","N", null, polis.getBiayaPolis(),null);
            }

            //add biaya materai
            if(polis.getBiayaMaterai()!=null){
                onNewDetailAutomatic2("14","N", null, polis.getBiayaMaterai(),null);
            }

            //add brokerfee
            if(polis.getBrokeragePct()!=null){
                onNewDetailAutomatic("105","N", polis.getEntityId(),new BigDecimal(20), "5", null);
            }

            /*
            //add fee base bank tanpa ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(polis.getDbFeeBase1Pct()!=null && polis.getDbFeeBasePPN1Exclude()==null && polis.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),polis.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank exclude ppn
            if(polis.getDbFeeBasePPN1ExcludePct()!=null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),polis.getDbFeeBasePPN1ExcludePct(), null, null);
            }

            //add komisi
            if(polis.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", polis.getStKomisi1EntityID(),polis.getDbKomisi1Pct(),"1", null);
            }

            //add diskon
            if(polis.getDbDiscountAmount()!=null){
                onNewDetailAutomatic2("16","N", null,polis.getDbDiscountAmount(),null);
            }



            //add brokerfee exclude ppn
            if(polis.getDbBrokerageExcludePPNPct()!=null && polis.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(polis.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", polis.getStBrokerageEntityID(),polis.getDbBrokerageExcludePPNPct(), "5", null);
            }*/



            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            uploadTanggungGugat(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksiKonstruksi();

            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksiKonstruksi();
            getPolicy().recalculateTreaty();

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), false);

            return policyID;


            //simpan
            //getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

    }

     private void uploadTanggungGugat(PengajuanPolisTanggungGugat polis, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getPolNo() +" debitur "+ polis.getNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(polis.getDataId());

            getSelectedDefaultObject().setStReference1(polis.getNama()); //nama
            getSelectedDefaultObject().setStReference2(polis.getAlamat());
            getSelectedDefaultObject().setDtReference1(polis.getTglLahir());
            getSelectedDefaultObject().setStReference3(polis.getUsia());
            getSelectedDefaultObject().setStReference4(polis.getNik());
            getSelectedDefaultObject().setStReference5(polis.getNoHp());
            getSelectedDefaultObject().setStReference6(polis.getEmail());
            getSelectedDefaultObject().setStReference7(polis.getNamaDarurat());
            getSelectedDefaultObject().setStReference8(polis.getNoHpDarurat());
            getSelectedDefaultObject().setStReference9(polis.getKategoriData());
            getSelectedDefaultObject().setStReference10(polis.getProfesi());
            getSelectedDefaultObject().setStReference11(polis.getPlanCoverage());
            getSelectedDefaultObject().setDtReference2(polis.getPeriodeAwal());
            getSelectedDefaultObject().setDtReference3(polis.getPeriodeAkhir());

            getSelectedDefaultObject().setStReference12(polis.getNoStr());
            getSelectedDefaultObject().setStReference13(polis.getStatusStr());
            getSelectedDefaultObject().setDtReference4(polis.getPeriodeAwalStr());
            getSelectedDefaultObject().setDtReference5(polis.getPeriodeAkhirStr());

            getSelectedDefaultObject().setStReference14(polis.getNoSip1());
            getSelectedDefaultObject().setDtReference6(polis.getPeriodeAwalSip1());
            getSelectedDefaultObject().setDtReference7(polis.getPeriodeAkhirSip1());
            getSelectedDefaultObject().setStReference15(polis.getPenerbitSip1());
            getSelectedDefaultObject().setStReference16(polis.getTempatPraktikSip1());

            getSelectedDefaultObject().setStReference17(polis.getNoSip2());
            getSelectedDefaultObject().setDtReference8(polis.getPeriodeAwalSip2());
            getSelectedDefaultObject().setDtReference9(polis.getPeriodeAkhirSip2());
            getSelectedDefaultObject().setStReference18(polis.getPenerbitSip2());
            getSelectedDefaultObject().setStReference19(polis.getTempatPraktikSip2());

            getSelectedDefaultObject().setStReference20(polis.getNoSip3());
            getSelectedDefaultObject().setDtReference10(polis.getPeriodeAwalSip3());
            getSelectedDefaultObject().setDtReference11(polis.getPeriodeAkhirSip3());
            getSelectedDefaultObject().setStReference21(polis.getPenerbitSip3());
            getSelectedDefaultObject().setStReference22(polis.getTempatPraktikSip3());

            getSelectedDefaultObject().setStReference23(polis.getJenisKelamin());


            //tambah tsi
            doAddLampiranSumInsured("591",polis.getSumInsured());

            //add coverage
            doAddLampiranCoverKreasiRateOri("618", polis.getRate(),polis.getPremi());

            //tambah deductible flexas
//            if(polis.getDbDeductiblePctOfClaim()!=null){
//                onNewObjDeductible("3910", polis.getDbDeductiblePctOfClaim(), "DED_PCT_CLAIM", polis.getDbDeductibleMinimum(), polis.getDbDeductibleMaximum());
//            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


    }

     private void prosesPengajuanPolisSerbagunaMacet() throws Exception{

        DTOList listGroup = null;

        // buat proposal per orang/debitur
        String param = "";

        if(currentMonthLock){
            param = " and date_trunc('month',tgl_polis) >= (date_part('year','now'::timestamp without time zone) || '-' ||date_part('month','now'::timestamp without time zone)||'-'||'01 00:00:00')::timestamp without time zone ";
        }

        listGroup = ListUtil.getDTOListFromQueryDS(
                    "select a.* "+
                    " from data_teks_masuk a "+
                    " left join ent_master b on a.entity_id = b.ent_id"+
                    " where kategori = 'HOST' and status in ('POLIS','CBC') and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and pol_type_id in ('87','88')"+
                    " and b.ref2::bigint in (select vs_code from s_company_group where host_to_host_flag = 'Y')  "+
                    " and (no_rek_pinjaman is not null and no_perjanjian_kredit is not null and no_identitas is not null "+
                    " and paket_coverage_askrida is not null and tgl_awal < tgl_akhir "+
                    " and usia >= 17 and usia <= 75 and entity_id is not null and upper(nama) not like '%#TES%' and length(pol_no) >= 18 and substr(pol_no, 3,2) = pol_type_id"+
                    " ) and tgl_polis >= '"+ tanggalPolis +"' "+
                    param +
                    " and date_trunc('day',tgl_awal) > '2024-12-13 00:00:00' "+ //POJK
                    " order by data_id",
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(grup.getStPolicyNo());

            //proteksi maks 5 tahun jk kredit
            int jangkaWaktuTahun = DateUtil.getYearsBetweenHUTB(grup.getDtTanggalAwal(), grup.getDtTanggalAkhir());

            System.out.println("######### Bikin POLIS HOST TO HOST NO POLIS : "+ grup.getStPolicyNo() + " PERIODE "+ jangkaWaktuTahun +" tahun");

            if(jangkaWaktuTahun > 5) continue;

            //jika belum ada di core, inject to core
            if(polisExisting==null){

                DTOList listObjek = null;

                //CARI DAFTAR OBJEK BERDASARKAN data ID

                 listObjek = ListUtil.getDTOListFromQueryDS(
                        " select * "+
                         "    from data_teks_masuk "+
                         "    where kategori = 'HOST' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and pol_type_id in ('87','88')"+
                         "    and data_id = ? "+
                         "    order by data_id",
                         new Object [] {grup.getStDataID()},
                       DataTeksMasukView.class,"GATEWAY");

                if(listObjek.size()==0) continue;

                System.out.println("######### Bikin POLIS HOST TO HOST NO POLIS : "+ grup.getStPolicyNo());

                //Buat SPPA otomatis
                //buatSPPAOtomatis(listObjek);

                //BUAT polis otomatis
                buatPolisPOJK20Otomatis(listObjek);

            }
        }
    }

     private void buatPolisPOJK20Otomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            if(data1.getStCostCenterCodeCore()!=null)
                getPolicy().setStCostCenterCode(data1.getStCostCenterCodeCore());

            if(data1.getStRegionIDCore()!=null)
                getPolicy().setStRegionID(data1.getStRegionIDCore());

            getPolicy().setStCostCenterCodeSource(getPolicy().getStCostCenterCode());
            getPolicy().setStRegionIDSource(getPolicy().getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(data1.getStPolicyNo().trim());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(data1.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCacCbcFlag("1");

            if(data1.getStJenisKreditAskrida()!=null)
                getPolicy().setStKreasiTypeID(data1.getStJenisKreditAskrida());

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());

            if(listObjek.size()==1){
                getPolicy().setStCustomerName(bank.getStEntityName()+" QQ "+ data1.getStNama().trim());
            }

            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            if(data1.getStTransactionNo()!=null)
                getPolicy().setStReferenceNo(data1.getStTransactionNo());

            //Cek outgo dulu, jika sesuai maka proses, jika tidak maka skip
            boolean isLimitOutgoSesuai = isSesuaiLimitOutgo(data1);

            if(isLimitOutgoSesuai){

                    //cek limit outgo h2h
                    validateLimitOutgo(data1);

                    //add fee base bank tanpa ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                        onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add fee base bank include ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                        onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add fee base bank exclude ppn
                    if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                        onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, data1.getDbFeeBase1PPNPct());
                    }

                    //add komisi
                    if(data1.getDbKomisi1Pct()!=null && data1.getDbKomisi1IncludePPNPct()==null){

                        EntityView entKomisi = getEntity(data1.getStKomisi1EntityID());

                        String taxCode = entKomisi.getStTaxCode().equalsIgnoreCase("PPH23")?"2":"1";

                        if(!BDUtil.isZeroOrNull(data1.getDbKomisi1Pct()))
                            onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(), taxCode, null);
                    }

                    //add komisi include ppn
                    if(data1.getDbKomisi1Pct()!=null && data1.getDbKomisi1IncludePPNPct()!=null){

                        EntityView entKomisi = getEntity(data1.getStKomisi1EntityID());

                        String taxCode = entKomisi.getStTaxCode().equalsIgnoreCase("PPH23")?"2":"1";

                        if(!BDUtil.isZeroOrNull(data1.getDbKomisi1Pct()))
                            onNewDetailAutomatic("112","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(), taxCode, data1.getDbKomisi1IncludePPNPct());
                    }

                    //add diskon
                    if(data1.getDbDiscountPct()!=null){
                        onNewDetailAutomatic("16","N", null,data1.getDbDiscountPct(),null, null);
                    }

                    //add brokerfee include ppn
                    if(data1.getDbBrokerageIncludePPNPct()!=null && data1.getDbBrokerageIncludePPNAmount()!=null){
                        if(!BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNAmount()))
                                onNewDetailAutomatic("88","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageIncludePPNPct(), "5", null);
                    }

                    //add brokerfee exclude ppn
                    if(data1.getDbBrokerageExcludePPNPct()!=null && data1.getDbBrokerageExcludePPNAmount()!=null){
                        if(!BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNAmount()))
                                onNewDetailAutomatic("12","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageExcludePPNPct(), "5", null);
                    }

                    if(data1.getStNomorBuktiBayar()!=null){
                        getPolicy().setStNomorBuktiBayar(data1.getStNomorBuktiBayar());
                    }

                    if(data1.getDtTanggalBayar()!=null){
                        getPolicy().setDtTanggalBayar(data1.getDtTanggalBayar());
                    }

                    if(data1.getStNomorReferensiPembayaran()!=null){
                        getPolicy().setStNomorReferensiPembayaran(data1.getStNomorReferensiPembayaran());
                    }

                    getPolicy().setStGatewayDataFlag("Y");

                    //ADD OBJEK
                    uploadKreditPolisPOJK20(data1.getStPolicyTypeID(), listObjek, null);

                    //apply warranty
                    if(data1.getStWarranty()!=null){
                        getPolicy().setStWarranty(data1.getStWarranty());
                    }

                    //hitung ulang
                    getPolicy().recalculateInterkoneksi();

                    getPolicy().defineTreaty();

                    getPolicy().recalculateInterkoneksi();
                    getPolicy().recalculateTreatyUploadSpreading();

                    //getPolicy().invokeCustomHandlersPolicyAuto(false, getSelectedDefaultObject());

                    //apply pilihan klausula
                    applyClausules(data1);

                    //simpan
                    if(data1.isCaseByCase()){
                        getPolicy().setStPostedFlag(null);
                        getPolicy().setStEffectiveFlag(null);
                        getPolicy().setStApprovedWho(null);
                        getPolicy().setDtApprovedDate(null);
                        getPolicy().setStClientIP(null);
                        getPolicy().setStReadyToApproveFlag(null);

                        getRemoteInsurance().saveFromTeks(policy,policy.getStNextStatus(),false);

                    }else{
                        String policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

                        //getRemoteInsurance().saveNotaToCare(policy, true);

                    }


                    final SQLUtil S = new SQLUtil("GATEWAY");

                    for (int i = 0; i < listObjek.size(); i++) {
                        DataTeksMasukView data = (DataTeksMasukView) listObjek.get(i);

                        //UPDATE DATA TEKS MASUK YANG SUDAH DI PROSES
                        PreparedStatement PS = S.setQuery("update data_teks_masuk set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

                        PS.setObject(1, data.getStDataID());
                        PS.setObject(2, data.getStKodeBank());

                        int j = PS.executeUpdate();

                        if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStDataID() +" ++++++++++++++++++");

                    }

                    S.release();
            }

    }

     private void uploadKreditPolisPOJK20(String polTypeID, DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek grup : "+ object.getStGroupID() +" debitur "+ object.getStNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            //kredit konsumtif
            if(polTypeID.equalsIgnoreCase("59")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama().trim()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
                getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
                getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
                getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida().trim());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference7(object.getStStatusKerja().trim());
                else
                    getSelectedDefaultObject().setStReference7(object.getKodePekerjaan().trim());

                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference10("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference10("Y");

                if(object.getStKodeTransaksi()!=null){
                    getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set periode 100%
                if(object.getDtTanggalAwal100()!=null)
                    getSelectedDefaultObject().setDtReference7(object.getDtTanggalAwal100());

                if(object.getDtTanggalAkhir100()!=null)
                    getSelectedDefaultObject().setDtReference8(object.getDtTanggalAkhir100());

                //set premi 100%
                if(object.getDbPremi100()!=null)
                    getSelectedDefaultObject().setDbReference11(object.getDbPremi100());

                //set premi sisa
                if(object.getDbPremiSisa()!=null)
                    getSelectedDefaultObject().setDbReference15(object.getDbPremiSisa());

                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference6(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference10("Y");
                    }


                //tambah tsi
                doAddLampiranSumInsured("488",object.getDbInsuredAmount());

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri("444",object.getDbRate(),object.getDbPremiTotal());
                    else
                        doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal());
                }

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        doAddLampiranCoverKreasi("573",null,object.getDbPremiRefund());
                    }
                }
            }

            //kredit produktif
            if(polTypeID.equalsIgnoreCase("80")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference4(object.getStStatusKerja());
                else
                    getSelectedDefaultObject().setStReference4(object.getKodePekerjaan());

                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference7(object.getStNoPerjanjianKredit()); // no pk
                getSelectedDefaultObject().setStReference8(object.getStNoRekeningPinjaman()); // no rek pinjaman

                if(object.getStJenisKreditAskrida()!=null)
                    getSelectedDefaultObject().setStReference9(object.getStJenisKreditAskrida()); // jenis kredit
                else
                    getSelectedDefaultObject().setStReference9("2"); // jenis kredit


                if(object.getStSumberPembayaran()!=null)
                    getSelectedDefaultObject().setStReference11(object.getStSumberPembayaran());
                else
                    getSelectedDefaultObject().setStReference11("1"); //sumber pembayaran

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPaketCoverageAskrida()); //coverage


                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference5(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference19("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference19(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference19("Y");

                if(object.getStKodeTransaksi()!=null){
                    //getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference5(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference19("Y");
                    }


                //tambah tsi
                doAddLampiranSumInsured("577",object.getDbInsuredAmount());

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri("595",object.getDbRate(),object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi("595",null,object.getDbPremiTotal());
                }

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        doAddLampiranCoverKreasi("617",null,object.getDbPremiRefund());
                    }
                }
            }

            //kredit macet only & Serbaguna
            if(polTypeID.equalsIgnoreCase("87") || polTypeID.equalsIgnoreCase("88")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());
                getSelectedDefaultObject().setStReference1(object.getStNama().trim()); //nama
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalLahir()); //tgl lahir
                //getSelectedDefaultObject().setStReference3(object.getStUsia()); //usia
                getSelectedDefaultObject().setDtReference2(object.getDtTanggalAwal());
                getSelectedDefaultObject().setDtReference3(object.getDtTanggalAkhir());
                getSelectedDefaultObject().setStReference3(object.getStNomorIdentitas());
                getSelectedDefaultObject().setStReference4(object.getStNoPerjanjianKredit());
                getSelectedDefaultObject().setStReference16(object.getStNoRekeningPinjaman());
                getSelectedDefaultObject().setStReference8(object.getStJenisIdentitas());

                if(object.getStPaketCoverageAskrida()!=null)
                    getSelectedDefaultObject().setStReference13(object.getStPaketCoverageAskrida());

                if(object.getStStatusKerja()!=null)
                    getSelectedDefaultObject().setStReference7(object.getStStatusKerja().trim());
                else
                    getSelectedDefaultObject().setStReference7(object.getKodePekerjaan().trim());

                if(object.getDtTanggalSTNC()!=null){
                    getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                    getSelectedDefaultObject().setStReference10("Y");
                }

                if(object.getStPersetujuanPusat()!=null)
                    getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());

                if(BDUtil.biggerThan(object.getDbInsuredAmount(), new BigDecimal(500000000)))
                    if(object.getStPersetujuanPusat()==null)
                        getSelectedDefaultObject().setStReference10("Y");

                if(object.getStKodeTransaksi()!=null){
                    getSelectedDefaultObject().setStReference22(object.getStKodeTransaksi());
                }

                //set periode 100%
                if(object.getDtTanggalAwal100()!=null)
                    getSelectedDefaultObject().setDtReference7(object.getDtTanggalAwal100());

                if(object.getDtTanggalAkhir100()!=null)
                    getSelectedDefaultObject().setDtReference8(object.getDtTanggalAkhir100());

                //set premi 100%
                if(object.getDbPremi100()!=null)
                    getSelectedDefaultObject().setDbReference11(object.getDbPremi100());

                //set premi sisa
                if(object.getDbPremiSisa()!=null)
                    getSelectedDefaultObject().setDbReference15(object.getDbPremiSisa());

                //set premi restitusi
                if(object.getDbPremiRefund()!=null)
                    getSelectedDefaultObject().setDbReference25(object.getDbPremiRefund());

                //sumber pembayaran
                if(object.getStSumberPembayaran()!=null)
                    getSelectedDefaultObject().setStReference26(object.getStSumberPembayaran());
                else
                    getSelectedDefaultObject().setStReference26("1"); //sumber pembayaran

                // pembayaran premi
                if(object.getStPembayaranPremi()!=null)
                    getSelectedDefaultObject().setStReference27(object.getStPembayaranPremi());


                //set tgl stnc
                int jumlahHariKalender = DateUtil.getDaysAmount(object.getDtTanggalAwal(), policy.getDtPolicyDate());
                boolean persetujuanPusat = Tools.isYes(object.getStPersetujuanPusat());

                if(!persetujuanPusat)
                    if(jumlahHariKalender > 60){
                        getSelectedDefaultObject().setDtReference6(policy.getDtPolicyDate());
                        getSelectedDefaultObject().setStReference10("Y");
                    }


                //tambah tsi
                if(polTypeID.equalsIgnoreCase("87"))
                    doAddLampiranSumInsured("600",object.getDbInsuredAmount());

                if(polTypeID.equalsIgnoreCase("88"))
                    doAddLampiranSumInsured("601",object.getDbInsuredAmount());


                String cvptID = polTypeID.equalsIgnoreCase("87")?"647":"654";

                //tambah cover kredit
                if(getPolicy().isBankJatengH2H()){
                    if(!BDUtil.isZeroOrNull(object.getDbRate()))
                        doAddLampiranCoverKreasiRateOri(cvptID,object.getDbRate(),object.getDbPremiTotal());
                    else
                        doAddLampiranCoverKreasi(cvptID,null,object.getDbPremiTotal());
                }else{
                    doAddLampiranCoverKreasi(cvptID,null,object.getDbPremiTotal());
                }


                String cvptRestitusiID = polTypeID.equalsIgnoreCase("87")?"648":"655";

                //tambah cover restitusi jika ada premi restitusi nya
                if(object.isTopUp()){
                    if(!BDUtil.isZeroOrNull(object.getDbPremiRefund())){
                        //doAddLampiranCoverKreasi(cvptRestitusiID,null,object.getDbPremiRefund());
                    }
                }
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

     public EntityView getEntity(String stEntID) {

        return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);
    }

     private void prosesPolisKumpulanCIS() throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP NOMOR POLIS CIS YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select pol_no,entity_id,count(data_id) as jumlah "+
                " from pengajuan_polis_cis  "+
                " where proses_flag is null  "+
                " group by pol_no,entity_id "+
                " order by pol_no",
                PengajuanPolisCISView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            PengajuanPolisCISView grup = (PengajuanPolisCISView) listGroup.get(i);

            // CEK POLIS SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getPolicyExisting(grup.getStPolicyNo());

            //jika belum ada di core, inject to core
            if(polisExisting==null){

                DTOList listObjek = null;

                //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
                listObjek = ListUtil.getDTOListFromQueryDS(
                        " select * "+
                         "    from pengajuan_polis_cis "+
                         "    where proses_flag is null "+
                         "    and pol_no = ? and entity_id = ?"+
                         "    order by data_id",
                         new Object [] {grup.getStPolicyNo(), grup.getStEntityID()},
                       PengajuanPolisCISView.class,"GATEWAY");

                if(listObjek.size()==0) continue;

                logger.logDebug("Bikin polis H2H CIS kumpulan nomor polis : "+ grup.getStPolicyNo());

                //BUAT POLIS BERDASARKAN NOMOR POLIS
                buatPolisCISOtomatis(listObjek);
            }
        }

    }

     private void buatPolisCISOtomatis(DTOList listObjek) throws Exception{

            policy = new InsurancePolicyView();

            getPolicy().markNew();

            getPolicy().setDetails(new DTOList());
            getPolicy().setObjects(new DTOList());
            getPolicy().setCovers(new DTOList());
            getPolicy().setSuminsureds(new DTOList());
            getPolicy().setCoins(new DTOList());
            getPolicy().setDeductibles(new DTOList());
            getPolicy().setInstallment(new DTOList());
            getPolicy().setStInstallmentPeriods("1");
            getPolicy().setStStatus(FinCodec.PolicyStatus.POLICY);
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            getPolicy().setStPostedFlag("Y");
            getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("1");

            onNewInstallment();

            //ISI OTOMATIS HEADER
            PengajuanPolisCISView data1 = (PengajuanPolisCISView) listObjek.get(0);

            getPolicy().setStCostCenterCode("80");
            getPolicy().setStRegionID("85");

            if(data1.getStCostCenterCode()!=null)
                getPolicy().setStCostCenterCodeSource(data1.getStCostCenterCode());

            if(data1.getStRegionID()!=null)
                getPolicy().setStRegionIDSource(data1.getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtPeriodeAwal());
            getPolicy().setDtPeriodEnd(data1.getDtPeriodeAkhir());
            getPolicy().setStPolicyTypeID(data1.getStJenisAsuransi());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(data1.getStPolicyNo());
            getPolicy().setStApprovedWho("00000002");
            getPolicy().setDtApprovedDate(data1.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            getPolicy().setStReadyToApproveFlag("Y");

            getPolicy().defaultPeriods();

            if (policy.getStCoverTypeCode()!=null) {
                try {
                        addMyCompany();

                        addMyCompanyCoverage();

                } catch (Exception e) {
                    policy.setStCoverTypeCode(null);
                    throw e;
                }
            }

            EntityView bank = getPolicy().getEntity2(data1.getStEntityID());
            getPolicy().setStEntityID(data1.getStEntityID());
            getPolicy().setStCustomerName(bank.getStEntityName());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(data1.getStEntityID());
            getPolicy().setStProducerName(bank.getStEntityName());
            getPolicy().setStProducerAddress(bank.getStAddress());

            String defaultPemasarID = getDefaultPemasar(data1.getStCostCenterCode());

            if(defaultPemasarID!=null){
                EntityView pemasar = getPolicy().getEntity2(defaultPemasarID);

                if(pemasar!=null){
                        getPolicy().setStProducerID(defaultPemasarID);
                        getPolicy().setStProducerName(pemasar.getStEntityName());
                        getPolicy().setStProducerAddress(pemasar.getStAddress());
                }
            }

            //if(data1.getStTransactionNo()!=null)
                //getPolicy().setStReferenceNo(data1.getStTransactionNo());

            //cek limit outgo h2h
            validateLimitOutgoCIS(data1);

            //add fee base bank tanpa ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null, null);
            }

            //add fee base bank include ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null,null);
            }

            //add fee base bank exclude ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null,null);
            }

            //add komisi
            if(data1.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1",null);
            }

            //add diskon
            if(data1.getDbDiscountAmount()!=null){
                //onNewDetailAutomatic2("16","N", null,data1.getDbDiscountAmount(),null);
            }

            //add diskon
            if(data1.getDbDiscountPct()!=null){
                onNewDetailAutomatic("16","N", null,data1.getDbDiscountPct(),null,null);
            }

            //add brokerfee include ppn
            if(data1.getDbBrokerageIncludePPNPct()!=null && data1.getDbBrokerageIncludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageIncludePPNAmount()))
                        onNewDetailAutomatic("88","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageIncludePPNPct(), "5",null);
            }

            //add brokerfee exclude ppn
            if(data1.getDbBrokerageExcludePPNPct()!=null && data1.getDbBrokerageExcludePPNAmount()!=null){
                if(!BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNPct()) && !BDUtil.isZeroOrNull(data1.getDbBrokerageExcludePPNAmount()))
                        onNewDetailAutomatic("12","N", data1.getStBrokerageEntityID(),data1.getDbBrokerageExcludePPNPct(), "5",null);
            }

            if(data1.getStNoReferensiBayar()!=null){
                //getPolicy().setStNomorBuktiBayar(data1.getStNoReferensiBayar());
            }

            if(data1.getDtTanggalBayarPremi()!=null){
                getPolicy().setDtTanggalBayar(data1.getDtTanggalBayarPremi());
            }

            if(data1.getStNoReferensiBayar()!=null){
                getPolicy().setStNomorReferensiPembayaran(data1.getStNoReferensiBayar());
            }

            getPolicy().setStGatewayDataFlag("Y");

            //ADD OBJEK
            uploadCIS(data1.getStJenisAsuransi(), listObjek, null);

            //apply warranty
//            if(data1.getStWarranty()!=null){
//                getPolicy().setStWarranty(data1.getStWarranty());
//            }

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            getPolicy().recalculateTreaty();

            //getPolicy().invokeCustomHandlersPolicyAuto(false, getSelectedDefaultObject());

            //apply pilihan klausula
//            applyClausules(data1);

            //simpan
            String policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), true);

            //getRemoteInsurance().saveNotaToCare(policy, true);

            final SQLUtil S = new SQLUtil("GATEWAY");

            for (int i = 0; i < listObjek.size(); i++) {
                PengajuanPolisCISView data = (PengajuanPolisCISView) listObjek.get(i);

                //UPDATE DATA YANG SUDAH DI PROSES
                PreparedStatement PS = S.setQuery("update pengajuan_polis_cis set proses_flag = 'Y',tgl_proses= 'now' where data_id = ? and kode_bank = ?");

                PS.setObject(1, data.getStDataID());
                PS.setObject(2, data.getStKodeBank());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS detil : "+ data.getStDataID() +" ++++++++++++++++++");

            }

            S.release();

    }

     public void validateLimitOutgoCIS(PengajuanPolisCISView data)throws Exception{
        //baca limit h2h dari sistem core
        getLimitOutgo(null);

        //cek feebase
        if(data.getDbFeeBase1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbFeeBase1Pct(), getDbLimitFeeBasePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbFeeBase1Pct() +") melebihi limit feebase ("+ getDbLimitFeeBasePct()+")");

       }

        //cek komisi
        if(data.getDbKomisi1Pct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbKomisi1Pct(), getDbLimitKomisiPct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbKomisi1Pct() +") melebihi limit komisi ("+ getDbLimitKomisiPct()+")");

        }

        //cek brokerfee include ppn
        if(data.getDbBrokerageIncludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageIncludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbBrokerageIncludePPNPct() +") melebihi limit komisi ("+ getDbLimitBrokeragePct()+")");

        }

        //cek brokerfee exclude ppn
        if(data.getDbBrokerageExcludePPNPct()!=null){

            boolean sesuaiLimit = BDUtil.isEqual(data.getDbBrokerageExcludePPNPct(), getDbLimitBrokeragePct(), 2);

            if(!sesuaiLimit) throw new RuntimeException("Data "+ data.getStUniqueKey() + " ("+ data.getDbBrokerageExcludePPNPct() +") melebihi limit komisi ("+ getDbLimitBrokeragePct()+")");

        }
    }

     private void uploadCIS(String polTypeID, DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            PengajuanPolisCISView object = (PengajuanPolisCISView) objek.get(i);

            System.out.println("add objek debitur "+ object.getStBisnis()+" ...........");

            //tambah objek
            doNewLampiranObject();

            //kredit konsumtif
            if(polTypeID.equalsIgnoreCase("5")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());

                getSelectedDefaultObject().setStReference1(object.getStBisnis()); //bisnis
                getSelectedDefaultObject().setStReference2(object.getStBuatan()); //buatan
                getSelectedDefaultObject().setStReference3(object.getStAlamatResiko()); //alamat
                getSelectedDefaultObject().setStReference4(object.getStKondisi()); //alamat
                getSelectedDefaultObject().setDtReference1(object.getDtPeriodeAwal()); //tgl awal
                getSelectedDefaultObject().setDtReference2(object.getDtPeriodeAkhir()); //tgl akhir

                //tambah tsi
                doAddLampiranSumInsured("42",object.getDbSumInsured());

                //tambah coverage
                doAddLampiranCoverKreasiRateOri("44",object.getDbRate(),object.getDbPremi());

                //tambah deductible
                onNewObjDeductible(object.getStDeductibleID1(), object.getDbDeductiblePctOfClaim1(), "DED_PCT_CLAIM", object.getDbDeductibleMin1(), object.getDbDeductibleMax1());

            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

     private void uploadCIT(String polTypeID, DTOList objek, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        for (int i = 0; i < objek.size(); i++) {
            PengajuanPolisCITView object = (PengajuanPolisCITView) objek.get(i);

            System.out.println("add objek debitur "+ object.getStBankAsal()+" ...........");

            //tambah objek
            doNewLampiranObject();

            //kredit konsumtif
            if(polTypeID.equalsIgnoreCase("14")){

                getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
                getSelectedDefaultObject().setStDataID(object.getStDataID());

                getSelectedDefaultObject().setStReference1(object.getStBankAsal()); //dari
                getSelectedDefaultObject().setStReference2(object.getStBankTujuan()); //tujuan
                getSelectedDefaultObject().setDtReference1(object.getDtTanggalBerangkat()); //tgl brgkt
                getSelectedDefaultObject().setStReference3(object.getStWaktu()); //waktu
                getSelectedDefaultObject().setStReference4(object.getStNoDeklarasi()); //no deklarasi

                String alatAngkut = object.getStAlatAngkut1();

                if(object.getStAlatAngkut2()!=null)
                    alatAngkut = alatAngkut +" "+ object.getStAlatAngkut2();

                if(object.getStAlatAngkut3()!=null)
                    alatAngkut = alatAngkut +" "+ object.getStAlatAngkut3();

                getSelectedDefaultObject().setStReference5(alatAngkut); //alat angkut
                getSelectedDefaultObject().setStReference6(object.getStAccountOfficer()); //ao
                getSelectedDefaultObject().setStReference8(object.getStPengawal()); //pengawal
                getSelectedDefaultObject().setStReference9(object.getStAccountOfficer()); //petugas bank

                //tambah tsi
                doAddLampiranSumInsured("182",object.getDbSumInsured());

                //tambah coverage
                doAddLampiranCoverKreasiRateOri("109",object.getDbRate(),object.getDbPremi());

                //tambah deductible
                //if(object.getStDeductibleID1()!=null){
                    onNewObjDeductible(object.getStDeductibleID1(), object.getDbDeductiblePctOfClaim1(), "DED_PCT_CLAIM", object.getDbDeductibleMin1(), object.getDbDeductibleMax1());
                //}

            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

}
