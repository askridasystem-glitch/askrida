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
import com.crux.jobs.util.JobUtil;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsuranceCoverPolTypeView;
import com.webfin.insurance.model.InsuranceCoverSourceView;
import com.webfin.insurance.model.InsuranceItemsView;
import com.webfin.insurance.model.InsurancePolicyClausulesView;
import com.webfin.insurance.model.InsurancePolicyCoinsView;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsurancePolicyInstallmentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.system.ftp.model.DataGatewayView;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author doni
 */

public class ProsesPengajuanPolisFromFTP extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanPolisFromFTP.class);

    //private String downloadPath = Parameter.readString("SYS_FTP_DOWNLOAD_FOLDER");
    //private String historyPath = Parameter.readString("SYS_FTP_HISTORY_FOLDER");
    private static String serverIP = Parameter.readString("SYS_SERVER_IP");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction())
                {
                    //PROSES DATA HOST TO HOST JADI POLIS APPROVE PER POLIS
                    prosesDataMenjadiPolis();

                    //PROSES PENGAJUAN POLIS NON KREDIT AUTO APPROVE
                    transferDataPengajuanPolis();

                    //PROSES DATA INTERKONEKSI
                    prosesDataInterkoneksi();

                    //PROSES DATA HOST TO HOST JADI POLIS KUMPULAN APPROVE PER POLIS
                    prosesBanyakDataJadiSatuPolis();
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

    private void prosesDataInterkoneksi() throws Exception{

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

    private void uploadKreditPolis(DTOList objek, String treatyID)throws Exception{

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

            if(object.getDtTanggalSTNC()!=null){
                getSelectedDefaultObject().setDtReference6(object.getDtTanggalSTNC());
                getSelectedDefaultObject().setStReference10("Y");
            }

            if(object.getStPersetujuanPusat()!=null)
                getSelectedDefaultObject().setStReference10(object.getStPersetujuanPusat());
                

            //tambah tsi
            doAddLampiranSumInsured("488",object.getDbInsuredAmount());

            //tambah cover
            if(getPolicy().isBankJatengH2H()){
                if(!BDUtil.isZeroOrNull(object.getDbRate()))
                    doAddLampiranCoverKreasiRateOri("444",object.getDbRate(),object.getDbPremiTotal());
            }else{
                doAddLampiranCoverKreasi("444",null,object.getDbPremiTotal()); 
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


    private void prosesDataMenjadiPolis() throws Exception{

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
                    " and usia >= 17 and usia < 75 and entity_id is not null "+
                    " )"+
                    " order by data_id", 
                    DataTeksMasukView.class,"GATEWAY"); 

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN data ID
            
             listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'HOST' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' "+
                     "    and data_id = ? "+
                     "    order by data_id",
                     new Object [] {grup.getStDataID()},
                   DataTeksMasukView.class,"GATEWAY");

            if(listObjek.size()==0) continue;

            System.out.println("Bikin POLIS HOST TO HOST grup : "+ grup.getStGroupID());

            //Buat SPPA otomatis
            buatSPPAOtomatis(listObjek);

            //BUAT polis otomatis
            buatPolisOtomatis(listObjek);



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

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            if(data1.getStCostCenterCodeCore()!=null)
                getPolicy().setStCostCenterCode(data1.getStCostCenterCodeCore());

            if(data1.getStRegionIDCore()!=null)
                getPolicy().setStRegionID(data1.getStRegionIDCore());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(data1.getDtTanggalPolis());
            getPolicy().setDtPeriodStart(data1.getDtTanggalAwal());
            getPolicy().setDtPeriodEnd(data1.getDtTanggalAkhir());
            getPolicy().setStPolicyTypeID(data1.getStPolicyTypeID());
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            getPolicy().setStPolicyNo(data1.getStPolicyNo());
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

            //cek limit outgo h2h
            validateLimitOutgo(data1);
 
            //add fee base bank tanpa ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add fee base bank include ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add fee base bank exclude ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add komisi
            if(data1.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1");
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
            uploadKreditPolis(listObjek, null);

            //apply pilihan klausula
            applyClausules(data1);

            //apply warranty
            if(data1.getStWarranty()!=null){
                getPolicy().setStWarranty(data1.getStWarranty());
            }

            //hitung ulang
            getPolicy().recalculateInterkoneksi();
            
            getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            getPolicy().recalculateTreaty();

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

    public void onNewDetailAutomatic(String insItemID, String stReadOnly, String entityID, BigDecimal pct, String taxCode) throws Exception {

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
            onNewDetailByChildID(itemCat.getStInsuranceItemChildID());

    }

    public void onNewDetailByChildID(String itemID) throws Exception {

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

    private void prosesBanyakDataJadiSatuPolis() throws Exception{

        DTOList listGroup = null;

        //CARI KUMPULAN GRUP NOMOR POLIS YANG BELUM DI PROSES

        listGroup = ListUtil.getDTOListFromQueryDS(
                "select pol_no,kode_bank,count(data_id) as jumlah "+
                " from data_teks_masuk a "+
                " where kategori = 'HOST2' and status = 'POLIS' "+
                " and proses_flag is null  "+
                " group by pol_no,kode_bank "+
                " order by pol_no",
                DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN GRUP ID
            listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'HOST2' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' "+
                     "    and pol_no = ? and kode_bank = ?"+
                     "    order by data_id",
                     new Object [] {grup.getStPolicyNo(), grup.getStKodeBank()},
                   DataTeksMasukView.class,"GATEWAY");

            if(listObjek.size()==0) continue;

            System.out.println("Bikin polis H2H kumpulan nomor polis : "+ grup.getStPolicyNo());

            //buat sppa
            buatSPPAOtomatis(listObjek);

            //BUAT POLIS BERDASARKAN NOMOR POLIS
            buatPolisOtomatis(listObjek);

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

    private void transferDataPengajuanPolis() throws Exception{

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

        EntityView entity = policy.getEntity();

        if(entity.getStRegionID()!=null)
            policy.setStRegionID(entity.getStRegionID());

        //add komisi
        if(policy.getDbNDComm1()!=null)
            onNewDetailAutomatic("11","N", policy.getStCommEntityID1(), policy.getDbNDComm1(),"1");

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

    public void getLimitOutgo() throws Exception {

        setDbLimitTotalOutgoPct(BDUtil.zero);
        setDbLimitKomisiPct(BDUtil.zero);
        setDbLimitFeeBasePct(BDUtil.zero);

        FlexTableView ft = (FlexTableView) ListUtil.getDTOListFromQuery(
                "   select " +
                "      b.*" +
                "   from " +
                "      ff_table b " +
                "   where b.ref3=? and b.ref2 ='999997' and b.fft_group_id='COMM'" +
                "   and (b.ref4 = ? or b.ref4 is null) "+ //--cari by grup sumbis
                "   and active_flag = 'Y'"+
                "   and period_start <= ? and period_end >= ?"+
                "   order by ref5,ref4,period_start desc "+
                "   limit 1",
                new Object [] {policy.getStPolicyTypeID(), policy.getEntity().getStRef2(), policy.getDtPeriodStart(), policy.getDtPeriodStart()},
                FlexTableView.class
                ).getDTO();

        if(ft!=null){

            if(ft.getDbReference1()!=null)
                setDbLimitTotalOutgoPct(ft.getDbReference1());

            if(ft.getDbReference2()!=null)
                setDbLimitKomisiPct(ft.getDbReference2());

            if(ft.getDbReference4()!=null)
                setDbLimitFeeBasePct(ft.getDbReference4());
        }
    }

    public void validateLimitOutgo(DataTeksMasukView data)throws Exception{
        //baca limit h2h dari sistem core
        getLimitOutgo();

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

            onNewInstallment();

            //ISI OTOMATIS HEADER
            DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

            if(data1.getStCostCenterCodeCore()!=null)
                getPolicy().setStCostCenterCode(data1.getStCostCenterCodeCore());

            if(data1.getStRegionIDCore()!=null)
                getPolicy().setStRegionID(data1.getStRegionIDCore());

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

            //cek limit outgo h2h
            validateLimitOutgo(data1);

            //add fee base bank tanpa ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add fee base bank include ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()==null && data1.getDbFeeBasePPN1Include()!=null){
                onNewDetailAutomatic("96","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add fee base bank exclude ppn
            if(data1.getDbFeeBase1Pct()!=null && data1.getDbFeeBasePPN1Exclude()!=null && data1.getDbFeeBasePPN1Include()==null){
                onNewDetailAutomatic("92","N", policy.getStEntityID(),data1.getDbFeeBase1Pct(), null);
            }

            //add komisi
            if(data1.getDbKomisi1Pct()!=null){
                onNewDetailAutomatic("11","N", data1.getStKomisi1EntityID(),data1.getDbKomisi1Pct(),"1");
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
            uploadKreditPolis(listObjek, null);

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
