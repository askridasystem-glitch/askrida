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
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.h2h.model.SPPAOnlineFire;
import com.webfin.h2h.model.SPPAOnlineKendaraan;
import com.webfin.h2h.model.SPPAOnlineKendaraanDetailPremi;
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
import com.webfin.insurance.model.InsurancePolicyInstallmentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.interkoneksi.model.PengajuanPolisCISView;
import com.webfin.interkoneksi.model.PengajuanPolisCITView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ProsesSPPAOnlineToCore extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesSPPAOnlineToCore.class);


    private static String serverIP = Parameter.readString("SYS_SERVER_IP");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction())
                {
                    prosesPengajuanSPPAOnlineFire();

                    prosesPengajuanSPPAOnlineKendaraan();

                    prosesPolisKumpulanCIT();

                    prosesPolisKumpulanCIS();
                }  
  
        } catch (Exception e) {
            throw new JobExecutionException(e);
        } 
    }

    private void prosesPengajuanSPPAOnlineFire() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listSPPAFire = null;

        logger.logInfo("++++++++++++++++++ PROSES SCHEDULER SPPA ONLINE FIRE YANG BELUM DI TRANSFER");

        //CARI SPPA YANG SUDAH DI SETUJUI DI OLEH BANK
        listSPPAFire = ListUtil.getDTOListFromQueryDS(
                " select * "+
                " from sppa_kebakaran_pengajuan_polis"+
                " where proses_flag is null"+
                " order by data_id",
                SPPAOnlineFire.class,"GATEWAY");

        for (int i = 0; i < listSPPAFire.size(); i++) {
            SPPAOnlineFire polis = (SPPAOnlineFire) listSPPAFire.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getNoPengajuan());

            // CEK NO PENGAJUAN SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getSPPAOnlineExisting(polis.getNoPengajuan());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = createSPPAFire(polis);
                
                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){
                        
                            //UPDATE DATA SPPA YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update sppa_kebakaran_pengajuan_polis set proses_flag ='Y', tgl_proses = 'now' where data_id = ?");

                            PS.setObject(1, polis.getStDataID());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS SPPA No PENGAJUAN : "+ polis.getNoPengajuan() +" ++++++++++++++++++");

                            S.release();
                    }
                }
            }
        }

    }
    
    private String createSPPAFire(SPPAOnlineFire polis) throws Exception{

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
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            //getPolicy().setStPostedFlag("Y");
            //getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("5");
            getPolicy().setStReferenceNo(polis.getNoPengajuan());

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            //masuk Non AKS
            //getPolicy().setStCostCenterCode("80");
            //getPolicy().setStRegionID("85");

            getPolicy().setStCostCenterCode(polis.getStCostCenterCode());
            getPolicy().setStRegionID(polis.getStRegionID());

            getPolicy().setStCostCenterCodeSource(polis.getStCostCenterCode());
            getPolicy().setStRegionIDSource(polis.getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(new Date());
            getPolicy().setDtPeriodStart(polis.getPeriodeAwal());
            getPolicy().setDtPeriodEnd(polis.getPeriodeAkhir());
            getPolicy().setStPolicyTypeID("1");
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            //getPolicy().setStPolicyNo(polis.getStPolicyNo());
            //getPolicy().setStApprovedWho("00000002");
            //getPolicy().setDtApprovedDate(polis.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            //getPolicy().setStReadyToApproveFlag("Y");
            //getPolicy().setStCheckingFlag("Y");

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

            EntityView bank = getPolicy().getEntity2(polis.getEntID());
            getPolicy().setStEntityID(polis.getEntID());
            getPolicy().setStCustomerName(bank.getStEntityName()+" QQ "+ polis.getNama().trim().toUpperCase());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(polis.getEntID());
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
            if(polis.getFeebase1Pct()!=null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getFeebase1Pct(), null, null);
            }
            
            /*

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
            }*/

            //add biaya polis
            if(polis.getBiayaPolis()!=null){
                onNewDetailAutomatic2("15","N", null,polis.getBiayaPolis(),null);
            }

            //biaya materai
            if(polis.getBiayaMaterai()!=null){
                onNewDetailAutomatic2("14","N", null,polis.getBiayaMaterai(),null);
            }
            

            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            addObjectFire(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            //getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            //getPolicy().recalculateTreaty();

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), false);


            return policyID;

    }

    private InsurancePolicyView policy;


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

    public void getLimitOutgo() throws Exception {

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
                new Object [] {policy.getStPolicyTypeID(), policy.getEntity().getStRef2(), policy.getStKreasiTypeID(), policy.getDtPeriodStart(), policy.getDtPeriodStart()},
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
        getLimitOutgo();

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

    private void addObjectFire(SPPAOnlineFire polis, String treatyID)throws Exception{

        final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getNoPengajuan() +" debitur "+ polis.getNama()+" ...........");

            //tambah objek
            doNewLampiranObject();

            getSelectedDefaultObject().setStRiskCategoryID(stRiskCategoryID);
            getSelectedDefaultObject().setStDataID(polis.getId());
            getSelectedDefaultObject().setStReference1(polis.getPenggunaan()); //penggunaan
            getSelectedDefaultObject().setStRiskClass(polis.getKelasKontruksi());

            String penerangan = polis.getPenerangan();
            String kodePenerangan = "";

            if(penerangan.toUpperCase().equalsIgnoreCase("LISTRIK")) kodePenerangan = "1";
            else if(penerangan.toUpperCase().equalsIgnoreCase("GENERATOR")) kodePenerangan = "2";
            else if(penerangan.toUpperCase().equalsIgnoreCase("MINYAK TANAH")) kodePenerangan = "3";
            else kodePenerangan = penerangan;

            getSelectedDefaultObject().setStReference4(kodePenerangan);
            getSelectedDefaultObject().setStReference5(polis.getAlamatRisiko());
            getSelectedDefaultObject().setStReference16(polis.getPropinsi());

            getSelectedDefaultObject().setStReference9(polis.getKodePos());

            getSelectedDefaultObject().setDtReference1(polis.getPeriodeAwal());
            getSelectedDefaultObject().setDtReference2(polis.getPeriodeAkhir());
            getSelectedDefaultObject().setStRiskCategoryCode1(polis.getKodeOkupasi());
            getSelectedDefaultObject().setStReference11(polis.getNama());
            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

            //batas kiri
            if(polis.getOkupasiKiri()!=null)
                getSelectedDefaultObject().setStReference22(polis.getKeteranganKiri());

            //batas kanan
            if(polis.getOkupasiKanan()!=null)
                getSelectedDefaultObject().setStReference23(polis.getKeteranganKanan());

            //batas depan
            if(polis.getOkupasiDepan()!=null)
                getSelectedDefaultObject().setStReference24(polis.getKeteranganDepan());

            //batas belakang
            if(polis.getOkupasiBelakang()!=null)
                getSelectedDefaultObject().setStReference25(polis.getKeteranganBelakang());

            //tambah tsi bangunan
            doAddLampiranSumInsured("2",polis.getNilaiPertanggungan());

            //add coverage
            logger.logDebug("############# rate asli = "+ polis.getRate());
            doAddLampiranCoverKreasiRateOri("512", polis.getRateDasar(), null);

            //jika ada perluasan huru hara
            if(polis.getRatePerluasan1()!=null){
                doAddLampiranCoverKreasiRateOri("514", polis.getRatePerluasan1(), null);
            }

            //jika ada perluasan tanah longsor
            if(polis.getRatePerluasan2()!=null){
                doAddLampiranCoverKreasiRateOri("518", polis.getRatePerluasan2(), null);
            }


            //doAddLampiranCoverKreasiRateOri("512", null,polis.getPremiDasar());

            //tambah deductible
            if(polis.getStDeductibleID1()!=null){
                onNewObjDeductible(polis.getStDeductibleID1(), polis.getDbDeductiblePctOfClaim1(), "DED_PCT_CLAIM", polis.getDbDeductibleMin1(), polis.getDbDeductibleMax1());
            }

            //tambah deductible
            if(polis.getStDeductibleID2()!=null){
                onNewObjDeductible(polis.getStDeductibleID2(), polis.getDbDeductiblePctOfClaim2(), "DED_PCT_CLAIM", polis.getDbDeductibleMin2(), polis.getDbDeductibleMax2());
            }

            //tambah deductible
            if(polis.getStDeductibleID3()!=null){
                onNewObjDeductible(polis.getStDeductibleID3(), polis.getDbDeductiblePctOfClaim3(), "DED_PCT_CLAIM", polis.getDbDeductibleMin3(), polis.getDbDeductibleMax3());
            }

            //tambah deductible
            if(polis.getStDeductibleID4()!=null){
                onNewObjDeductible(polis.getStDeductibleID4(), polis.getDbDeductiblePctOfClaim4(), "DED_PCT_CLAIM", polis.getDbDeductibleMin4(), polis.getDbDeductibleMax4());
            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


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
        getLimitOutgo();

        if(data.isPromo()){
            getLimitOutgoPromo();
        }

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

     private void prosesPengajuanSPPAOnlineKendaraan() throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        DTOList listSPPAKendaraan = null;

        logger.logInfo("++++++++++++++++++ PROSES CRON JOB SPPA ONLINE KENDARAAN YANG BELUM DI TRANSFER");

        //CARI SPPA YANG SUDAH DI SETUJUI DI OLEH BANK
        listSPPAKendaraan = ListUtil.getDTOListFromQueryDS(
                "select * "+
                " from sppa_kendaraan_pengajuan_polis"+
                " where proses_flag is null"+
                " order by id_pengajuan_polis",
                SPPAOnlineKendaraan.class,"GATEWAY");

        for (int i = 0; i < listSPPAKendaraan.size(); i++) {
            SPPAOnlineKendaraan polis = (SPPAOnlineKendaraan) listSPPAKendaraan.get(i);

            logger.logInfo("transfer data polis no : "+ polis.getNoPengajuan());

            // CEK NO PENGAJUAN SUDAH ADA/BELUM DI CORE
            InsurancePolicyView polisExisting = getSPPAOnlineExisting(polis.getNoPengajuan());

            //jika tidak ada, inject
            if(polisExisting==null){

                //COPY RECORD POLIS
                String policyID = createSPPAKendaraan(polis);

                if(policyID!=null){
                    if(!policyID.equalsIgnoreCase("")){

                            //UPDATE DATA SPPA YANG SUDAH DI PROSES
                            PreparedStatement PS = S.setQuery("update sppa_kendaraan_pengajuan_polis set proses_flag ='Y', tgl_proses = 'now' where id_pengajuan_polis = ?");

                            PS.setObject(1, polis.getIdPengajuanPolis());

                            int j = PS.executeUpdate();

                            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS SPPA No PENGAJUAN : "+ polis.getNoPengajuan() +" ++++++++++++++++++");

                            S.release();
                    }
                }
            }

        }

    }

     private String createSPPAKendaraan(SPPAOnlineKendaraan polis) throws Exception{

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
            getPolicy().setStActiveFlag("Y");
            getPolicy().setStCoverNoteFlag("N");
            getPolicy().setStCurrencyCode("IDR");
            getPolicy().setDbCurrencyRate(BDUtil.one);
            getPolicy().setDbCurrencyRateTreaty(BDUtil.one);
            getPolicy().setDbPeriodRate(new BigDecimal(100));
            getPolicy().setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));
            //getPolicy().setStPostedFlag("Y");
            //getPolicy().setStEffectiveFlag("Y");
            getPolicy().setStDataSourceID("5");
            getPolicy().setStReferenceNo(polis.getNoPengajuan());

            onNewInstallment();

            //ISI OTOMATIS HEADER
            //DataTeksMasukView data1 = (DataTeksMasukView) listObjek.get(0);

            //masuk Non AKS
            //getPolicy().setStCostCenterCode("80");
            //getPolicy().setStRegionID("85");

            getPolicy().setStCostCenterCode(polis.getStCostCenterCode());
            getPolicy().setStRegionID(polis.getStRegionID());

            getPolicy().setStCostCenterCodeSource(polis.getStCostCenterCode());
            getPolicy().setStRegionIDSource(polis.getStRegionID());

            getPolicy().setStCoverTypeCode("DIRECT");
            getPolicy().setDtPolicyDate(new Date());
            getPolicy().setDtPeriodStart(polis.getTanggalAwal());
            getPolicy().setDtPeriodEnd(polis.getTanggalAkhir());
            getPolicy().setStPolicyTypeID("3");
            getPolicy().setStPolicyTypeGroupID(getPolicy().getPolicyType().getStGroupID());
            getPolicy().setStRateMethod(getPolicy().getPolicyType().getStRateMethod());
            getPolicy().setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(getPolicy().getStRateMethod()));

            //getPolicy().setStPolicyNo(polis.getStPolicyNo());
            //getPolicy().setStApprovedWho("00000002");
            //getPolicy().setDtApprovedDate(polis.getDtTanggalPolis());
            getPolicy().setStClientIP(serverIP);
            //getPolicy().setStReadyToApproveFlag("Y");
            getPolicy().setStCheckingFlag("Y");

            int tahun = DateUtil.getYearBetween(polis.getTanggalAwal(), polis.getTanggalAkhir());

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

            EntityView bank = getPolicy().getEntity2(polis.getEntID());
            getPolicy().setStEntityID(polis.getEntID());
            getPolicy().setStCustomerName(bank.getStEntityName()+" QQ "+ polis.getNamaDebitur().trim().toUpperCase());
            getPolicy().setStCustomerAddress(bank.getStAddress());

            getPolicy().setStProducerID(polis.getEntID());
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
            */

            //add fee base bank tanpa ppn
            if(polis.getFeebase1Pct()!=null){
                onNewDetailAutomatic("66","N", policy.getStEntityID(),polis.getFeebase1Pct(), null, null);
            }


            //add biaya polis
            if(polis.getBiayaPolis()!=null){
                onNewDetailAutomatic2("15","N", null,polis.getBiayaPolis(),null);
            }

            //biaya materai
            if(polis.getBiayaMaterai()!=null){
                onNewDetailAutomatic2("14","N", null,polis.getBiayaMaterai(),null);
            }

            getPolicy().setStGatewayDataFlag("Y");

            String treatyID = policy.getDtPeriodStart()!=null?policy.getInsuranceTreatyID(policy.getDtPeriodStart()):"";

            //ADD OBJEK
            addObjectKendaraan(polis, treatyID);

            //hitung ulang
            getPolicy().recalculateInterkoneksi();

            //getPolicy().defineTreaty();

            getPolicy().recalculateInterkoneksi();
            //getPolicy().recalculateTreaty();

            String policyID = "";

            policyID = getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), false);


            return policyID;

    }

     private void addObjectKendaraan(SPPAOnlineKendaraan polis, String treatyID)throws Exception{

        //final String stRiskCategoryID = getPolicy().getRiskCategoryID(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart());

        //for (int i = 0; i < objek.size(); i++) {
            //DataTeksMasukView object = (DataTeksMasukView) objek.get(i);

            System.out.println("add objek  : "+ polis.getNoPengajuan() +" debitur "+ polis.getNamaDebitur()+" ...........");

            //tambah objek
            doNewLampiranObject();

            String kodeJenis = polis.getJenisKendaraan();

            String kodeJenisCore = "";

            if(kodeJenis.equalsIgnoreCase("1")) kodeJenisCore = getPolicy().getRiskCategoryIDByCode(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart(), "501003");
            if(kodeJenis.equalsIgnoreCase("2")) kodeJenisCore = getPolicy().getRiskCategoryIDByCode(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart(), "501015");
            if(kodeJenis.equalsIgnoreCase("3")) kodeJenisCore = getPolicy().getRiskCategoryIDByCode(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart(), "501011");
            if(kodeJenis.equalsIgnoreCase("4")) kodeJenisCore = getPolicy().getRiskCategoryIDByCode(getPolicy().getStPolicyTypeID(),getPolicy().getDtPeriodStart(), "501013");

            getSelectedDefaultObject().setStRiskCategoryID(kodeJenisCore);

            getSelectedDefaultObject().setStDataID(polis.getIdPengajuanPolis());
            getSelectedDefaultObject().setStReference1(polis.getNoPolisi()); //plat nopol

            String kodeMerk = "";

            kodeMerk = polis.getCodeMerk(polis.getMerk());

            if(!kodeMerk.equalsIgnoreCase(""))
                    getSelectedDefaultObject().setStReference2(kodeMerk); //merk

            getSelectedDefaultObject().setStReference8(polis.getTipe());
            getSelectedDefaultObject().setStReference3(polis.getTahunPembuatan());
            getSelectedDefaultObject().setStReference4(polis.getNomorRangka());
            getSelectedDefaultObject().setStReference5(polis.getNomorMesin());
            getSelectedDefaultObject().setStReference6(polis.getTempatDuduk());
            getSelectedDefaultObject().setStReference9(polis.getNamaDebitur());

            String penggunaan = "";

            if(polis.getPenggunaan().equalsIgnoreCase("1")) penggunaan = "10";
            if(polis.getPenggunaan().equalsIgnoreCase("2")) penggunaan = "11";
            if(polis.getPenggunaan().equalsIgnoreCase("3")) penggunaan = "12";

            getSelectedDefaultObject().setStReference7(penggunaan);
            
            getSelectedDefaultObject().setDtReference1(polis.getTanggalAwal());
            getSelectedDefaultObject().setDtReference2(polis.getTanggalAkhir());

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

            //tambah tsi
            //xxx ubah jadi per tahun by depresiasi
            doAddLampiranSumInsured("542",polis.getHargaPertanggungan());

            //add coverage
            logger.logDebug("############# rate asli = "+ polis.getRate());
            //doAddLampiranCoverKreasiRateOri("512", BDUtil.round(BDUtil.getMileFromPCT(polis.getDbRatePct()),3),polis.getDbPremiFlexas());
            //280 kompre 281 tlo

            DTOList detailPremi = polis.getDetailPremi();

            //set metode depresiasi
            if(detailPremi.size()>0){
                getSelectedDefaultObject().setStReference11("Y");
                getPolicy().setPeriods("YEAR", 1);
            }
                 

            for (int i = 0; i < detailPremi.size(); i++) {
                SPPAOnlineKendaraanDetailPremi detail = (SPPAOnlineKendaraanDetailPremi) detailPremi.get(i);

                //compre
                if(detail.getMixCoverage().equalsIgnoreCase("1"))
                    doAddCover("534", detail.getMixRate(), null, detail.getHargaPertanggungan(), detail.getTahun());

                //TLO
                if(detail.getMixCoverage().equalsIgnoreCase("2"))
                    doAddCover("535", detail.getMixRate(), null, detail.getHargaPertanggungan(), detail.getTahun());
            }

            
            //tambah deductible
            if(polis.getStDeductibleID1()!=null){
                onNewObjDeductible(polis.getStDeductibleID1(), polis.getDbDeductiblePctOfClaim1(), "DED_PCT_CLAIM", polis.getDbDeductibleMin1(), polis.getDbDeductibleMax1());
            }

            //tambah deductible
            if(polis.getStDeductibleID2()!=null){
                onNewObjDeductible(polis.getStDeductibleID2(), polis.getDbDeductiblePctOfClaim2(), "DED_PCT_CLAIM", polis.getDbDeductibleMin2(), polis.getDbDeductibleMax2());
            }

            //tambah deductible
            if(polis.getStDeductibleID3()!=null){
                onNewObjDeductible(polis.getStDeductibleID3(), polis.getDbDeductiblePctOfClaim3(), "DED_PCT_CLAIM", polis.getDbDeductibleMin3(), polis.getDbDeductibleMax3());
            }

            //tambah deductible
            if(polis.getStDeductibleID4()!=null){
                onNewObjDeductible(polis.getStDeductibleID4(), polis.getDbDeductiblePctOfClaim4(), "DED_PCT_CLAIM", polis.getDbDeductibleMin4(), polis.getDbDeductibleMax4());
            }

            //tambah deductible
            if(polis.getStDeductibleID5()!=null){
                onNewObjDeductible(polis.getStDeductibleID5(), polis.getDbDeductiblePctOfClaim5(), "DED_PCT_CLAIM", polis.getDbDeductibleMin5(), polis.getDbDeductibleMax5());
            }

            //tambah deductible
            if(polis.getStDeductibleID6()!=null){
                onNewObjDeductible(polis.getStDeductibleID6(), polis.getDbDeductiblePctOfClaim6(), "DED_PCT_CLAIM", polis.getDbDeductibleMin6(), polis.getDbDeductibleMax6());
            }

            //tambah deductible
            if(polis.getStDeductibleID7()!=null){
                onNewObjDeductible(polis.getStDeductibleID7(), polis.getDbDeductiblePctOfClaim7(), "DED_PCT_CLAIM", polis.getDbDeductibleMin7(), polis.getDbDeductibleMax7());
            }

            //tambah deductible
            if(polis.getStDeductibleID8()!=null){
                onNewObjDeductible(polis.getStDeductibleID8(), polis.getDbDeductiblePctOfClaim8(), "DED_PCT_CLAIM", polis.getDbDeductibleMin8(), polis.getDbDeductibleMax8());
            }

            //tambah deductible flexas
//            if(polis.getDbDeductiblePctOfClaim()!=null){
//                onNewObjDeductible("3910", polis.getDbDeductiblePctOfClaim(), "DED_PCT_CLAIM", polis.getDbDeductibleMinimum(), polis.getDbDeductibleMaximum());
//            }

            //getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        //}


    }

     public void doAddCover(String cvptid,BigDecimal rate,BigDecimal premi, BigDecimal tsi, String tahun) throws Exception{

        logger.logDebug("################## rate convert pct to pmill = "+ rate);

        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

        cv.setStInsuranceCoverPolTypeID(cvptid);//242,243,244

        cv.initializeDefaults();

        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

        if(tsi!=null){
            cv.setDbInsuredAmount(tsi);
            cv.setStEntryInsuredAmountFlag("Y");
            cv.setStDepreciationYear(tahun);
        }

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

     public InsurancePolicyView getSPPAOnlineExisting(String stReferenceNo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select pol_id, pol_no "+
                                        " from ins_policy  "+
                                        " where status in ('SPPA','POLICY','RENEWAL') and active_flag = 'Y' "+
                                        " and reference_no = ?",
                new Object[]{stReferenceNo},
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

            //getPolicy().setStCostCenterCode("80");
            //getPolicy().setStRegionID("85");

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

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

            getRemoteInsurance().saveNotaToCare(policy, true);

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

                    if(object.getStDeductibleID2()!=null)
                        onNewObjDeductible(object.getStDeductibleID2(), object.getDbDeductiblePctOfClaim2(), "DED_PCT_CLAIM", object.getDbDeductibleMin2(), object.getDbDeductibleMax2());

                    if(object.getStDeductibleID3()!=null)
                        onNewObjDeductible(object.getStDeductibleID3(), object.getDbDeductiblePctOfClaim3(), "DED_PCT_CLAIM", object.getDbDeductibleMin3(), object.getDbDeductibleMax3());

                    if(object.getStDeductibleID4()!=null)
                        onNewObjDeductible(object.getStDeductibleID4(), object.getDbDeductiblePctOfClaim4(), "DED_PCT_CLAIM", object.getDbDeductibleMin4(), object.getDbDeductibleMax4());
                //}

            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


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

            //getPolicy().setStCostCenterCode("80");
            //getPolicy().setStRegionID("85");

            getPolicy().setStCostCenterCode(data1.getStCostCenterCode());
            getPolicy().setStRegionID(data1.getStRegionID());

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

            getRemoteInsurance().saveNotaToCare(policy, true);

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

                if(object.getStDeductibleID2()!=null)
                        onNewObjDeductible(object.getStDeductibleID2(), object.getDbDeductiblePctOfClaim2(), "DED_PCT_CLAIM", object.getDbDeductibleMin2(), object.getDbDeductibleMax2());

                if(object.getStDeductibleID3()!=null)
                    onNewObjDeductible(object.getStDeductibleID3(), object.getDbDeductiblePctOfClaim3(), "DED_PCT_CLAIM", object.getDbDeductibleMin3(), object.getDbDeductibleMax3());

                if(object.getStDeductibleID4()!=null)
                    onNewObjDeductible(object.getStDeductibleID4(), object.getDbDeductiblePctOfClaim4(), "DED_PCT_CLAIM", object.getDbDeductibleMin4(), object.getDbDeductibleMax4());
            }

            getSelectedDefaultObject().setStInsuranceTreatyID(treatyID);

        }


    }

}
