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
import com.crux.file.FileView;
import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.h2h.model.WSDokumenRestitusiView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyPreEndorseView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.PengajuanEndorsemenView;
import com.webfin.interkoneksi.model.PengajuanEndorseH2HView;
import com.webfin.interkoneksi.model.PengajuanRestitusiView;
import com.webfin.system.ftp.model.DataGatewayView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
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

public class ProsesPengajuanEndorse extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanEndorse.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
 
                if(JobUtil.isServerProduction())
                {
                    //proses restitusi Kumpulan dari h2h
                    prosesRestitusiKumpulanH2H();

                    //proses restitusi dari h2h
                    prosesPengajuanRestitusiH2H();

                    //proses endorsemen top up
                    prosesPengajuanEndorsemenTopUp();

                    //proses endorsemen dari h2h
                    //prosesPengajuanEndorseH2H();
                    prosesEndorsemenKumpulanH2H_V2();

                    //proses endorsemen input di interkoneksi
                    prosesDataMenjadiEndorse();

                    //proses endorse keterangan h2h
                    //prosesPengajuanEndorseKeteranganH2H();

                    //SYNC dokumen restitusi yang baru
                    resendDocumentRestitusi();
                }
                        
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }


    private void prosesDataMenjadiEndorse() throws Exception{

        DTOList listPreClaim = null;
 
        //CARI PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPreClaim = ListUtil.getDTOListFromQueryDS(
                " select * "+
                "  from ins_policy_endorse  "+
                "  where effective_flag = 'Y' and  "+
                "  status = 'PRE ENDORSE' and coalesce(gateway_data_f,'N') <> 'Y' "+
                "  order by poL_id", InsurancePolicyPreEndorseView.class,"GATEWAY");

        for (int i = 0; i < listPreClaim.size(); i++) {
            InsurancePolicyPreEndorseView data = (InsurancePolicyPreEndorseView) listPreClaim.get(i);

            System.out.println("Bikin ENDORSE NO POLIS : "+ data.getStPolicyNo());

            if(data.isBatalTotalEndorseMode())
                    editCreateEndorseBatalTotalMode(data);

            if(data.isRestitutionEndorseMode())
                    editCreateEndorseRestitusiMode(data);

            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update ins_policy_endorse set gateway_data_f = 'Y', status_other='TRANSFERED' where pol_id = ?");

            PS.setObject(1, data.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ data.getStPolicyNo() +" ++++++++++++++++++");

            S2.release();

        }

    }

    public void editCreateEndorseBatalTotalMode(InsurancePolicyPreEndorseView data) throws Exception {
        superEditMode(data);

        //checkActiveEffective();

//        if (!policy.isEffective())
//            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    BigDecimal premi = cov.getDbPremi();

                    if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                        cov.setDbPremi(BDUtil.zero);
                        cov.setDbPremiNew(BDUtil.zero);
                    }
                }
            }
            
            if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())) continue;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

                if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                    cov.setDbPremi(BDUtil.zero);
                    cov.setDbPremiNew(BDUtil.zero);
                }


                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(BDUtil.zero);
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                tsi.setDbInsuredAmount(BDUtil.zero);
            }

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.setStEditFlag("Y");
                trdi.setDbTSIAmount(BDUtil.negate(trdi.getDbTSIAmount()));

                final DTOList share = trdi.getShares();

                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("Y");
                    ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));

                }

            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT PEMBATALAN OBJEK : "+ data.getStClaimPersonName());
        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();

        //simpen
        getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    public void superEditMode(InsurancePolicyPreEndorseView data) throws Exception {
        view(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    private InsurancePolicyView policy;

    private InsurancePolicyObjDefaultView selectedDefaultObject;

    public InsurancePolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;
    }

    public void setSelectedDefaultObject(InsurancePolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;
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

    public void view(InsurancePolicyPreEndorseView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(data.getStPolicyNo(), data.getStClaimPersonName());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    private void checkActiveEffective() {
        if (!policy.isEffective())
            throw new RuntimeException("Please approve the document");

        if (!policy.isActive())
            throw new RuntimeException("Document is not active, please refer to the last active document");
    }

    private DTOList historypolicy;
    
    public DTOList getHistoryPolicy(String stPolicyNo) {
        loadHistoryPolicy(stPolicyNo);
        return historypolicy;
    }

    public void loadHistoryPolicy(String stPolicyNo) {
        try {
            if (historypolicy == null)
                historypolicy = ListUtil.getDTOListFromQuery(
                        " select * "+
                        " from ins_policy "+
                        " where status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' and substr(pol_no,0,17) = ? order by pol_id desc limit 1",
                        new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16)+"%":stPolicyNo+"%"},
                        InsurancePolicyView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InsurancePolicyView getInsurancePolicy(String stPolicyNo, String nama) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') "+
                                        " and substr(a.pol_no, 0, 17) = ? and upper(b.ref1) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, nama.toUpperCase()},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public void editCreateEndorseRestitusiMode(InsurancePolicyPreEndorseView data) throws Exception {
        superEditMode(data);

        //checkActiveEffective();

//        if (!policy.isEffective())
//            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(data.getDbReference1());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    BigDecimal premi = cov.getDbPremi();

                    if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                        cov.setDbPremi(BDUtil.zero);
                        cov.setDbPremiNew(BDUtil.zero);
                    }
                }
            }

            if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())) continue;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));

                objx.setDtReference5(data.getDtReference1());
                objx.setStReference15(data.getStReference1());
                policy.setDbPeriodRateBefore(data.getDbReference1());
            }

            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                objx.setDtReference5(data.getDtReference1());
                objx.setStReference9(data.getStReference1());
                policy.setDbPeriodRateBefore(data.getDbReference1());
            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

                if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                    cov.setDbPremi(BDUtil.zero);
                    cov.setDbPremiNew(BDUtil.zero);
                }


                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(BDUtil.zero);
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                tsi.setDbInsuredAmount(BDUtil.zero);
            }

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.setStEditFlag("Y");
                trdi.setDbTSIAmount(BDUtil.negate(trdi.getDbTSIAmount()));

                final DTOList share = trdi.getShares();

                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("Y");
                    ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));

                }

            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT RESTITUSI OBJEK : "+ data.getStClaimPersonName());
        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();

        //simpen
        getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    
    private void prosesPengajuanEndorseH2H() throws Exception{

        DTOList listPengajuanEndorseH2H = null;

        //CARI PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPengajuanEndorseH2H = ListUtil.getDTOListFromQueryDS(
                " select * "+
                " from pengajuan_endorse  "+
                " where proses_flag is null "+
                " order by tgl_transfer", PengajuanEndorsemenView.class,"GATEWAY");

        for (int i = 0; i < listPengajuanEndorseH2H.size(); i++) {
            PengajuanEndorsemenView data = (PengajuanEndorsemenView) listPengajuanEndorseH2H.get(i);

            System.out.println("Bikin ENDORSE NO POLIS : "+ data.getStPolicyNo());

            String kodeProses = "";
            String statusProses = "";

            if(data.isBatalDebiturEndorseMode()){

                createEndorseBatalDebiturH2H(data);
                kodeProses = "1";
                statusProses = "ON PROCESS";

            }else if(data.isBatalTotalEndorseMode()){

                CreateEndorseBatalTotalModeH2H(data);
                kodeProses = "1";
                statusProses = "ON PROCESS";
            }else{
                kodeProses = "0";
                statusProses = "FAILED DATA NOT VALID";
            }

            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update pengajuan_endorse set proses_flag = 'Y', tgl_proses = 'now', kode_status = '"+ kodeProses +"', status = '"+ statusProses +"' where pol_no = ? and no_urut = ?");

            PS.setObject(1, data.getStPolicyNo());
            PS.setObject(2, data.getStNomorUrut());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ data.getStPolicyNo() +" ++++++++++++++++++");

            S2.release();
        }

    }

    public void CreateEndorseBatalTotalModeH2H(PengajuanEndorsemenView data) throws Exception {
        superEditModeH2H(data);

        //checkActiveEffective();

//        if (!policy.isEffective())
//            throw new RuntimeException("Policy not yet effective");

          if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
          }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    BigDecimal premi = cov.getDbPremi();

                    //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                        //cov.setDbPremi(BDUtil.zero);
                        //cov.setDbPremiNew(BDUtil.zero);
                    //}
                }
            //}

            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())) continue;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            //final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(BDUtil.zero);
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                tsi.setDbInsuredAmount(BDUtil.zero);
            }

            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.setStEditFlag("Y");
                trdi.setDbTSIAmount(BDUtil.negate(trdi.getDbTSIAmount()));

                final DTOList share = trdi.getShares();

                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("Y");
                    ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));

                }

            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT PEMBATALAN TOTAL");
        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();

        //simpen
        getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    public void superEditModeH2H(PengajuanEndorsemenView data) throws Exception {
        viewH2H(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    public void viewH2H(PengajuanEndorsemenView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicyH2H(data.getStPolicyNo());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public InsurancePolicyView getInsurancePolicyH2H(String stPolicyNo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') "+
                                        " and substr(a.pol_no, 0, 17) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public void createEndorseBatalDebiturH2H(PengajuanEndorsemenView data) throws Exception {
        superEditModeEndorseH2H(data);

        InsurancePolicyView parent = getInsuranceLastPolicyH2H(data.getStPolicyNo());

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStDataSourceID("1");

        //set data setujui
        if(data.getStApprovedWho()!=null){
            policy.setStPostedFlag("Y");
            policy.setStEffectiveFlag("Y");
            policy.setDtApprovedDate(new Date());
            policy.setStApprovedWho(data.getStApprovedWho());
            policy.setStReadyToApproveFlag("Y");
        }


        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    //BigDecimal premi = cov.getDbPremi();

                    //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                        //cov.setDbPremi(BDUtil.zero);
                        //cov.setDbPremiNew(BDUtil.zero);
                    //}
                }
            //}

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){

                objx.setStReference14("Y");
                objx.setDbReference2(BDUtil.zero);
                objx.setDtReference5(null);
                objx.setStReference15(null);
            }

            boolean isBatal = false;

            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStNomorUrut())) continue;

            if(objx.getStOrderNo().equalsIgnoreCase(data.getStNomorUrut())) isBatal = true;

            //proses batal debitur yg di ajukan

            if(isBatal){
                if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                    objx.setDbReference4(BDUtil.zero);

                    if(!Tools.isYes(objx.getStReference9()))
                        objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));

                    objx.setStReference14("Y");
                    objx.setDbReference2(BDUtil.zero);
                    objx.setDtReference5(null);
                    objx.setStReference15(null);
                }
            }
            

            //final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if(isBatal){
                    BigDecimal premi = cov.getDbPremi();

                    if(cov.isEntryPremi()){
                        cov.setDbPremi(BDUtil.negate(premi));
                        cov.setDbPremiNew(cov.getDbPremi());
                    }

                    if(!cov.isEntryPremi() && !cov.isEntryRate()){
                        cov.setDbPremi(BDUtil.negate(premi));
                        cov.setDbPremiNew(cov.getDbPremi());
                    }

                    //jika mode endorse pembatalan debitur
                    if(data.isBatalDebiturEndorseMode() && BDUtil.isZeroOrNull(data.getDbPremi())){
                        cov.setStEntryRateFlag("N");
                        cov.setStEntryPremiFlag("Y");
                        cov.setDbPremi(BDUtil.zero);
                        cov.setDbPremiNew(BDUtil.zero);
                    }

                    if(data.isBatalDebiturEndorseMode() && !BDUtil.isZeroOrNull(data.getDbPremi())){
                        cov.setStEntryRateFlag("N");
                        cov.setStEntryPremiFlag("Y");
                        cov.setDbPremi(data.getDbPremi());
                        cov.setDbPremiNew(data.getDbPremi());
                    }

                    if(cov.isEntryInsuredAmount()){
                        cov.setDbInsuredAmount(BDUtil.zero);
                    }
                }else{
                    if(cov.isEntryPremi()){
                        cov.setDbPremi(BDUtil.zero);
                        cov.setDbPremiNew(BDUtil.zero);
                    }
                }
                
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(isBatal){
                    if(tsi.getStAutoFlag()!=null)
                        if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                            tsi.setStAutoFlag(null);

                    tsi.setDbInsuredAmount(BDUtil.zero);
                }
                
            }

            /*
            final DTOList treatyDetails = obj.getTreatyDetails();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.setStEditFlag("Y");
                trdi.setDbTSIAmount(BDUtil.negate(trdi.getDbTSIAmount()));

                final DTOList share = trdi.getShares();

                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.setStUseRateFlag("N");
                    ri.setStAutoRateFlag("Y");
                    ri.setDbPremiAmount(BDUtil.negate(ri.getDbPremiAmount()));

                }

            }*/

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT PEMBATALAN TOTAL");

        if(data.isBatalDebiturEndorseMode()){
            policy.setStEndorseNotes("ENDORSEMENT PEMBATALAN DEBITUR "+ data.getStNama() +" KARENA ADA TOP UP");
        }

        policy.setStGatewayDataFlag("Y");
        policy.setStParentID(parent.getStPolicyID());
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();

        //simpen
        if(data.getStApprovedWho()!=null)
            getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), true);
        else
            getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    private void prosesPengajuanRestitusiH2H() throws Exception{

        DTOList listPengajuanEndorseH2H = null;

        //CARI PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPengajuanEndorseH2H = ListUtil.getDTOListFromQueryDS(
                " select * "+
                " from ws_pengajuan_restitusi  "+
                " where proses_flag is null "+
                " order by id", PengajuanRestitusiView.class,"GATEWAY");

        for (int i = 0; i < listPengajuanEndorseH2H.size(); i++) {
            PengajuanRestitusiView data = (PengajuanRestitusiView) listPengajuanEndorseH2H.get(i);

            System.out.println("Bikin ENDORSE RESTITUSI NO POLIS : "+ data.getStNomorPolis());

            String kodeProses = "";
            String statusProses = "";

            String policyID = createEndorseRestitusiH2H(data);

            //cari dokumen restitusi dari interkoneksi
            DTOList listDokumen = ListUtil.getDTOListFromQueryDS(
            " select * "+
            " from ws_dokumen_restitusi  "+
            " where no_polis = ? and no_urut = ?"+
            " order by id",  new Object[]{ data.getStNomorPolis(), data.getStNomorUrut()}, WSDokumenRestitusiView.class,"GATEWAY");

            //simpen dokumen ke core jika ada
            if(listDokumen!=null){
                if(listDokumen.size()>0){
                    //simpan dokumen ke LKS
                    for (int j = 0; j < listDokumen.size(); j++) {
                        WSDokumenRestitusiView doc = (WSDokumenRestitusiView) listDokumen.get(j);

                        String fileID = saveDocument(doc);

                        //tambahin dokumen di ins_poL_document
                        InsurancePolicyDocumentView document = new InsurancePolicyDocumentView();

                        document.markNew();

                        document.setStSelectedFlag("Y");
                        document.setStInsuranceDocumentTypeID(doc.getStKodeDokumenAskrida());
                        document.setStDocumentClass("POLICY");
                        document.setStFilePhysic(fileID);
                        document.setStPolicyID(policyID);

                        document.store();

                        //UPDATE DATA doc klaim yang sudah di proses
                        final SQLUtil S2 = new SQLUtil("GATEWAY");

                        PreparedStatement PS = S2.setQuery("update ws_dokumen_restitusi set doc_transfer_flag = 'Y' where no_polis = ? and no_urut = ? and kode_dokumen = ?");

                        PS.setObject(1, doc.getStNomorPolis());
                        PS.setObject(2, doc.getStNoUrut());
                        PS.setObject(3, doc.getStKodeDokumen());

                        int k = PS.executeUpdate();

                        S2.release();

                        if (k!=0) logger.logInfo("+++++++ UPDATE STATUS dokumen : "+ doc.getStNomorLoan() +" ++++++++++++++++++");

                    }
                }
            }
            

            kodeProses = "1";
            statusProses = "ON PROCESS";


            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update ws_pengajuan_restitusi set proses_flag = 'Y', tgl_proses = 'now' where nomor_loan = ?");

            PS.setObject(1, data.getStNomorLoan());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS nomor loan : "+ data.getStNomorLoan() +" ++++++++++++++++++");

            S2.release();
        }

    }

    public String createEndorseRestitusiH2H(PengajuanRestitusiView data) throws Exception {
        superEditModeRestitusiH2H(data);

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(data.getDbRestitusiPct());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        if(data.getStTransactionNo()!=null)
            policy.setStReferenceNo(data.getStTransactionNo());

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        
        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);
            items.setDbRate(BigDecimal.ZERO);
            items.setDbRatePct(BigDecimal.ZERO);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        //policy.getDetails().deleteAll();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    //BigDecimal premi = cov.getDbPremi();

                    //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                        //cov.setDbPremi(BDUtil.zero);
                        //cov.setDbPremiNew(BDUtil.zero);
                    //}
                }

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            if(!objx.getStOrderNo().equalsIgnoreCase(data.getStNomorUrut())) continue;

            //proses batal debitur yg di ajukan

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                objx.setDtReference5(data.getDtTanggalRestitusi());
                objx.setStReference9(data.getStSisaJangkaWaktu());
            }

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                //jika mode endorse pembatalan debitur
                    cov.setStEntryRateFlag("N");
                    cov.setStEntryPremiFlag("Y");
                    cov.setDbPremi(BDUtil.negate(data.getDbPremiRestitusi()));
                    cov.setDbPremiNew(BDUtil.negate(data.getDbPremiRestitusi()));

                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(BDUtil.zero);
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                tsi.setDbInsuredAmount(BDUtil.zero);
            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT RESTITUSI DEBITUR");

        if(true){
            policy.setStEndorseNotes("ENDORSEMENT RESTITUSI DEBITUR "+ data.getStNamaDebitur() +"");
        }

        if(data.getStEndorseWording()!=null){
            policy.setStEndorseNotes(data.getStEndorseWording());
        }

        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

        return policyID;

    }

    public void superEditModeRestitusiH2H(PengajuanRestitusiView data) throws Exception {
        viewRestitusiH2H(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    public void viewRestitusiH2H(PengajuanRestitusiView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        //policy = getInsurancePolicyH2H(data.getStNomorPolis());

        if(data.getStNomorPolis().length()>18)
            policy = getInsuranceLastPolicyH2H_19Digit(data.getStNomorPolis());
        else
            policy = getInsuranceLastPolicyH2H(data.getStNomorPolis());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }
    
    private void prosesPengajuanEndorsemenTopUp() throws Exception{

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

                prosesDataMenjadiEndorseTopUp(gateway.getStCostCenterCode());
        }

    }
    
    private void prosesDataMenjadiEndorseTopUp(String cabang) throws Exception{

        DTOList listGroup = null;

        // buat proposal per orang/debitur
        listGroup = ListUtil.getDTOListFromQueryDS(
                "select kode_bank,group_id,data_id "+
                    " from data_teks_masuk a "+
                    " where kategori = 'ENDORSE' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and cc_code = ? and status <> 'CONFIRM' "+
                    " order by data_id", new Object [] {cabang},
                    DataTeksMasukView.class,"GATEWAY");

        for (int i = 0; i < listGroup.size(); i++) {
            DataTeksMasukView grup = (DataTeksMasukView) listGroup.get(i);

            DTOList listObjek = null;

            //CARI DAFTAR OBJEK BERDASARKAN data ID
            listObjek = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                     "    from data_teks_masuk "+
                     "    where kategori = 'ENDORSE' and proses_flag is null and coalesce(valid_f,'Y') = 'Y' and status <> 'CONFIRM' "+
                     "    and data_id = ? and kode_bank = ? "+
                     "    order by data_id",
                     new Object [] {grup.getStDataID(), grup.getStKodeBank()},
                   DataTeksMasukView.class,"GATEWAY");

            if(listObjek.size()==0) continue;

            DataTeksMasukView endorse = (DataTeksMasukView) listObjek.get(0);

            System.out.println("Bikin proposal grup : "+ grup.getStGroupID());

            //BUAT ENDORSE BERDASARKAN GROUP ID
            createEndorseTopUp(endorse);

            //UPDATE DATA TEKS ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update data_teks_masuk set proses_flag = 'Y', tgl_proses = 'now' where data_id = ?");

            PS.setObject(1, endorse.getStDataID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS nomor polis : "+ endorse.getStPolicyNoBefore() +" ++++++++++++++++++");

            S2.release();

        }

    }
    
    public void createEndorseTopUp(DataTeksMasukView data) throws Exception {
        superEditModeTopUp(data);

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        //cekPolisExpire();

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(BDUtil.hundred);
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        if(data.getStTransactionNo()!=null)
            policy.setStReferenceNo(data.getStTransactionNo());

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());

        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                }

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            boolean isDebiturTopUp = false;

            //cek by no rek pinjaman
            if(objx.getStReference16()!=null)
                if(objx.getStReference16().equalsIgnoreCase(data.getStNoRekeningPinjamanLama()))
                    isDebiturTopUp = true;

            //jika no rek null,cek by nama & no ktp
            if(objx.getStReference16()==null){
                if(objx.getStReference1().toUpperCase().trim().equalsIgnoreCase(data.getStNama().toUpperCase().trim())
                    && objx.getStReference3().toUpperCase().trim().equalsIgnoreCase(data.getStNomorIdentitas().toUpperCase().trim()))
                    isDebiturTopUp = true;
            }

            if(!isDebiturTopUp) continue;

            //compare jika no rekening pinjaman
            //if(objx.getStReference16()!=null)
                //if(!objx.getStReference16().equalsIgnoreCase(data.getStNoRekeningPinjamanLama())) continue;


            //proses top up debitur yg di ajukan

            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                objx.setDbReference4(BDUtil.zero);
                if(!Tools.isYes(objx.getStReference9()))
                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
            }

            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                objx.setDtReference2(data.getDtTanggalAwal());
                objx.setDtReference3(data.getDtTanggalAkhir());
                objx.setStReference4(data.getStNoPerjanjianKredit());
                objx.setStReference16(data.getStNoRekeningPinjaman());
                objx.setStDataID(data.getStDataID());
                
                //cek ada perubahan entity id tidak
                if(!policy.getStEntityID().equalsIgnoreCase(data.getStEntityID())){
                    EntityView bank = policy.getEntity2(data.getStEntityID());

                    if(bank!=null){
                        policy.setStEntityID(data.getStEntityID());
                        policy.setStCustomerName(bank.getStEntityName());
                        policy.setStCustomerAddress(bank.getStAddress());
                    }  
                }

            }

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

                if(cov.isEntryPremi()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                if(!cov.isEntryPremi() && !cov.isEntryRate()){
                    cov.setDbPremi(BDUtil.negate(premi));
                    cov.setDbPremiNew(cov.getDbPremi());
                }

                //jika mode endorse pembatalan debitur
                    cov.setStEntryRateFlag("N");
                    cov.setStEntryPremiFlag("Y");
                    cov.setDbPremi(data.getDbPremiTotal());
                    cov.setDbPremiNew(data.getDbPremiTotal());

                if(cov.isEntryInsuredAmount()){
                    cov.setDbInsuredAmount(data.getDbInsuredAmount());
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                if(tsi.getStAutoFlag()!=null)
                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                        tsi.setStAutoFlag(null);

                tsi.setDbInsuredAmount(data.getDbInsuredAmount());
            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT TOP UP DEBITUR");

        if(true){
            policy.setStEndorseNotes("ENDORSEMENT TOP UP DEBITUR "+ data.getStNama() +"");
        }

        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();

        //simpen
        getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    public void superEditModeTopUp(DataTeksMasukView data) throws Exception {
        viewTopUp(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    public void viewTopUp(DataTeksMasukView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicyH2H(data.getStPolicyNoBefore());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    private void prosesPengajuanEndorseKeteranganH2H() throws Exception{

        DTOList listPengajuanEndorseH2H = null;

        //CARI PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPengajuanEndorseH2H = ListUtil.getDTOListFromQueryDS(
                " select * "+
                " from ws_pengajuan_endorse  "+
                " where proses_flag is null "+
                " order by id", PengajuanEndorseH2HView .class,"GATEWAY");

        for (int i = 0; i < listPengajuanEndorseH2H.size(); i++) {
            PengajuanEndorseH2HView data = (PengajuanEndorseH2HView) listPengajuanEndorseH2H.get(i);

            System.out.println("Bikin ENDORSE KETERANGAN NO POLIS : "+ data.getStNoPolisLama());

            String kodeProses = "";
            String statusProses = "";

            superEditModeKeteranganH2H(data);

            kodeProses = "1";
            statusProses = "ON PROCESS";


            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update ws_pengajuan_endorse set proses_flag = 'Y', tgl_proses = 'now' where nomor_loan = ?");

            PS.setObject(1, data.getStNomorLoan());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS nomor loan : "+ data.getStNomorLoan() +" ++++++++++++++++++");

            S2.release();
        }

    }

public void createEndorseKeteranganH2H(PengajuanEndorseH2HView data) throws Exception {
        superEditModeKeteranganH2H(data);

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(policy.getDbPeriodRate());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        if(data.getStTransactionNo()!=null)
            policy.setStReferenceNo(data.getStTransactionNo());

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());


        final DTOList details = policy.getDetails();
        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);
            items.setDbRate(BigDecimal.ZERO);
            items.setDbRatePct(BigDecimal.ZERO);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStOrderNo())){
                final DTOList coverage = obj.getCoverage();

                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());
                }

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            //if(!objx.getStOrderNo().equalsIgnoreCase(data.getStNomorUrut())) continue;

            //proses endorse debitur yg di ajukan

            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                //objx.setDtReference5(data.getDtTanggalRestitusi());
                //objx.setStReference9(data.getStSisaJangkaWaktu());
                //set data yg diubah
            }

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                BigDecimal premi = cov.getDbPremi();

            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

            }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT KETERANGAN DEBITUR");

        if(data.getStKeteranganEndorse()!=null){
            policy.setStEndorseNotes(data.getStKeteranganEndorse());
        }

        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();

        //simpen
        getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

    }

    public void superEditModeKeteranganH2H(PengajuanEndorseH2HView data) throws Exception {
        viewKeteranganH2H(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

    }

    public void viewKeteranganH2H(PengajuanEndorseH2HView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicyH2H(data.getStNoPolisLama());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

        public String saveDocument(WSDokumenRestitusiView doc) throws Exception{
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

            if (source.exists() && source.isFile()){
                //COPY FILE softcopy nya
                copyFileUsingApacheCommonsIO(source, dest,destination);
            }

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

    private void resendDocumentRestitusi() throws Exception{

        DTOList listDocuments = null;

        //CARI dokumen KLAIM YANG di replace, status 0
        listDocuments = ListUtil.getDTOListFromQueryDS(
                "select a.*,b.no_urut as no_urut_pengajuan "+
                " from ws_dokumen_restitusi a "+
                " inner join ws_pengajuan_restitusi b on a.no_polis = b.no_polis and a.no_urut = b.no_urut "+
                " where doc_transfer_flag is null order by a.create_date", WSDokumenRestitusiView.class,"GATEWAY");

        for (int i = 0; i < listDocuments.size(); i++) {
            WSDokumenRestitusiView doc = (WSDokumenRestitusiView) listDocuments.get(i);

            //save ulang document nya
            String fileID = saveDocument(doc);

            //dapetin endorse nya di core
            //by nopol & no rek dulu
            InsurancePolicyView endorseCore = getEndorseWithPolNo(doc.getStNomorPolis(), doc.getStNomorLoan());

            //jika endorse core tidak ditemukan, cari by nopol & no urut
            if(endorseCore==null){
                if(doc.getStNoUrutPengajuan()!=null){
                    endorseCore = getEndorseWithOrderNo(doc.getStNomorPolis(), doc.getStNoUrutPengajuan());
                }
            }

            //jika endorse nya ada, baru inject ulang dokumen nya
            if(endorseCore!=null){

                //dapetin doc klaim nya
                final DTOList listDocPolicy = endorseCore.getPolicyDocuments();

                for (int j = 0; j < listDocPolicy.size(); j++) {
                    InsurancePolicyDocumentView docPolicy = (InsurancePolicyDocumentView) listDocPolicy.get(j);

                    if(docPolicy.getStInsuranceDocumentTypeID().equalsIgnoreCase(doc.getStKodeDokumenAskrida())){

                        //jika sudah ada, update dokumen nya
                        if(docPolicy.getStInsurancePolicyDocumentID()!=null){
                            final SQLUtil S2 = new SQLUtil();

                            PreparedStatement PS = S2.setQuery("update ins_pol_documents set file_physic = ?, change_date='now', change_who='sys' where ins_pol_document_id = ? ");

                            PS.setObject(1, fileID);
                            PS.setObject(2, docPolicy.getStInsurancePolicyDocumentID());

                            int k = PS.executeUpdate();

                            S2.release();
                        }

                        //jika belum ada, insert record dokumen
                        if(docPolicy.getStInsurancePolicyDocumentID()==null){
                            docPolicy.markNew();

                            docPolicy.setStSelectedFlag("Y");
                            docPolicy.setStFilePhysic(fileID);
                            docPolicy.store();
                        }

                    }

                }

                    //UPDATE DATA doc klaim yang sudah di proses
                    final SQLUtil S2 = new SQLUtil("GATEWAY");

                    PreparedStatement PS = S2.setQuery("update ws_dokumen_restitusi set doc_transfer_flag = 'Y' where nomor_loan = ? and kode_dokumen = ?");

                    PS.setObject(1, doc.getStNomorLoan());
                    PS.setObject(2, doc.getStKodeDokumen());

                    int j = PS.executeUpdate();

                    S2.release();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS dokumen : "+ doc.getStNomorLoan() +" ++++++++++++++++++");

                    //send surat masuk ke user web terkait update dokumen
                    /*
                    DTOList listUserNotif = getListUserNotif(endorseCore.getStCostCenterCode());

                    if(listUserNotif!=null){
                        for (int k = 0; k < listUserNotif.size(); k++) {
                           NotificationSettingView user = (NotificationSettingView) listUserNotif.get(k);

                           sendNotifications(klaimCore,doc, user);

                        }
                    }*/
            }
        }

    }

    public InsurancePolicyView getEndorseWithPolNo(String stPolicyNo, String noAplikasi) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('ENDORSE')  and a.active_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and trim(upper(b.ref16)) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noAplikasi.trim().toUpperCase()},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public InsurancePolicyView getEndorseWithOrderNo(String stPolicyNo, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('ENDORSE') and a.active_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public InsurancePolicyView getInsuranceLastPolicyH2H(String stPolicyNo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*"+
                                        " from ins_policy a "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') "+
                                        " and substr(a.pol_no, 0, 17) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo},
                InsurancePolicyView.class).getDTO();


        return pol;
    }

    public InsurancePolicyView getInsuranceLastPolicyH2H_19Digit(String stPolicyNo) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*"+
                                        " from ins_policy a "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') "+
                                        " and substr(a.pol_no, 0, 18) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>18?stPolicyNo.substring(0,17):stPolicyNo},
                InsurancePolicyView.class).getDTO();


        return pol;
    }

    public void superEditModeEndorseH2H(PengajuanEndorsemenView data) throws Exception {
        viewEndorseH2H(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    public void viewEndorseH2H(PengajuanEndorsemenView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsuranceLastPolicyH2H(data.getStPolicyNo());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    private void prosesRestitusiKumpulanH2H() throws Exception{

        DTOList listPengajuanEndorseH2H = null;

        //CARI NOPOL PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPengajuanEndorseH2H = ListUtil.getDTOListFromQueryDS(
                " select no_polis "+
                " from pengajuan_restitusi_kumpulan "+
                " where proses_flag is null "+
                " group by no_polis "+
                " order by no_polis", PengajuanRestitusiView.class,"GATEWAY");

        for (int i = 0; i < listPengajuanEndorseH2H.size(); i++) {
            PengajuanRestitusiView data = (PengajuanRestitusiView) listPengajuanEndorseH2H.get(i);

            System.out.println("Bikin ENDORSE RESTITUSI NO POLIS : "+ data.getStNomorPolis());

            //GET DETIL POLIS
            DTOList detilPolisEndorseH2H = null;

            //CARI NOPOL PENGAJUAN ENDORSE YANG BELUM DI PROSES
            detilPolisEndorseH2H = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                    " from pengajuan_restitusi_kumpulan "+
                    " where proses_flag is null and no_polis = ? "+
                    " order by no_polis,no_urut", new Object[]{ data.getStNomorPolis()}, PengajuanRestitusiView.class,"GATEWAY");

            String policyID = createEndorseRestitusiKumpulanH2H(data, detilPolisEndorseH2H);


            String kodeProses = "";
            String statusProses = "";

            kodeProses = "1";
            statusProses = "ON PROCESS";


            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update pengajuan_restitusi_kumpulan set proses_flag = 'Y', tgl_proses = 'now' where no_polis = ? and proses_flag is null");

            PS.setObject(1, data.getStNomorPolis());

            int j = PS.executeUpdate();

            if (j!=0) logger.logWarning("+++++++ UPDATE STATUS nomor POLIS : "+ data.getStNomorPolis() +" "+ policyID +" ++++++++++++++++++");

            S2.release();
        }

    }

    public String createEndorseRestitusiKumpulanH2H(PengajuanRestitusiView data, DTOList detilObject) throws Exception {
        superEditModeRestitusiH2H(data);

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(data.getDbRestitusiPct());
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        if(data.getStTransactionNo()!=null)
            policy.setStReferenceNo(data.getStTransactionNo());

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());


        final DTOList details = policy.getDetails();

        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);
            items.setDbRate(BigDecimal.ZERO);
            items.setDbRatePct(BigDecimal.ZERO);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }



            //LOOPING ALL OBJECT POLIS
            final DTOList objects = policy.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


                final DTOList coverage = obj.getCoverage();

                for (int k = 0; k < coverage.size(); k++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(k);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    if(cov.isEntryPremi()){
                        cov.setStEntryRateFlag("N");
                         cov.setStEntryPremiFlag("Y");
                         cov.setDbPremi(BDUtil.zero);
                         cov.setDbPremiNew(BDUtil.zero);
                    }

                }

                obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                //Looping object yang restitusi, cek kalau no urut sama, proses endorse retur nya
                for (int i = 0; i < detilObject.size(); i++) {

                        PengajuanRestitusiView det = (PengajuanRestitusiView) detilObject.get(i);

                        //if(!objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())) continue;

                        //proses batal debitur yg di ajukan

                        if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                            policy.setDbPeriodRateBefore(det.getDbRestitusiPct());

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                                objx.setDbReference4(BDUtil.zero);
                                if(!Tools.isYes(objx.getStReference9()))
                                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
                            }

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                                //objx.setDtReference5(det.getDtTanggalRestitusi());
                                objx.setStReference9(det.getStSisaJangkaWaktu());
                            }
                        }

                        final DTOList coverage2 = obj.getCoverage();

                        for (int l = 0; l < coverage2.size(); l++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage2.get(l);

                            cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                            //jika mode endorse pembatalan debitur
                            if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                                cov.setStEntryRateFlag("N");
                                cov.setStEntryPremiFlag("Y");
                                cov.setDbPremi(BDUtil.negate(det.getDbPremiRestitusi()));
                                cov.setDbPremiNew(BDUtil.negate(det.getDbPremiRestitusi()));

                                if(cov.isEntryInsuredAmount()){
                                    cov.setDbInsuredAmount(BDUtil.zero);
                                }
                            }
                                    
                        }

                        final DTOList suminsureds = obj.getSuminsureds();

                        for (int m = 0; m < suminsureds.size(); m++) {
                            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(m);

                            tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                            if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                                if(tsi.getStAutoFlag()!=null)
                                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                                        tsi.setStAutoFlag(null);

                                tsi.setDbInsuredAmount(BDUtil.zero);
                            }
                        }

                }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT RESTITUSI DEBITUR");

        if(true){
            policy.setStEndorseNotes("ENDORSEMENT RESTITUSI "+ detilObject.size() +" DEBITUR");
        }

        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();

        //simpen
        String policyID = getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), false);

        logger.logWarning("################ pol id endorsemen = "+ policyID);

        return policyID;

    }

    private void prosesEndorsemenKumpulanH2H_V2() throws Exception{

        DTOList listPengajuanEndorseH2H = null;

        //CARI NOPOL PENGAJUAN ENDORSE YANG BELUM DI PROSES
        listPengajuanEndorseH2H = ListUtil.getDTOListFromQueryDS(
                " select pol_no "+
                " from pengajuan_endorse "+
                " where proses_flag is null "+
                " group by pol_no "+
                " order by pol_no", PengajuanEndorsemenView.class,"GATEWAY");

        for (int i = 0; i < listPengajuanEndorseH2H.size(); i++) {
            PengajuanEndorsemenView data = (PengajuanEndorsemenView) listPengajuanEndorseH2H.get(i);

            System.out.println("Bikin ENDORSE RESTITUSI NO POLIS : "+ data.getStPolicyNo());

            //GET DETIL POLIS
            DTOList detilPolisEndorseH2H = null;

            //CARI NOPOL PENGAJUAN ENDORSE YANG BELUM DI PROSES
            detilPolisEndorseH2H = ListUtil.getDTOListFromQueryDS(
                    " select * "+
                    " from pengajuan_endorse "+
                    " where proses_flag is null and pol_no = ? "+
                    " order by pol_no,no_urut", new Object[]{ data.getStPolicyNo()}, PengajuanEndorsemenView.class,"GATEWAY");

            String policyID = createEndorseRestitusiKumpulanH2H_V2(data, detilPolisEndorseH2H);


            String kodeProses = "";
            String statusProses = "";

            kodeProses = "1";
            statusProses = "ON PROCESS";


            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update pengajuan_endorse set proses_flag = 'Y', tgl_proses = 'now' where pol_no = ? and proses_flag is null");

            PS.setObject(1, data.getStPolicyNo());

            int j = PS.executeUpdate();

            if (j!=0) logger.logWarning("+++++++ UPDATE STATUS nomor POLIS : "+ data.getStPolicyNo() +" "+ policyID +" ++++++++++++++++++");

            S2.release();
        }

    } 

    public String createEndorseRestitusiKumpulanH2H_V2(PengajuanEndorsemenView data, DTOList detilObject) throws Exception {
        superEditModeRestitusiH2H_V2(data);

        if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(policy.getStStatus()) &&
               !FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(policy.getStStatus())) {
                throw new RuntimeException("Pembatalan Total Hanya Bisa Dibuat Dari Polis, Endorse, Renewal, dan History");
        }

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
        policy.setDbPeriodRateBefore(BDUtil.zero);
        policy.setDtPolicyDate(new Date());
        policy.setDtEndorseDate(new Date());
        policy.setStCoverNoteFlag("N");
        policy.setStPrintCode(null);
        policy.setStPrintStamp(null);
        policy.setStDocumentPrintFlag(null);
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference3(null);
        policy.setStReference4(null);
        policy.setStManualInstallmentFlag(null);
        policy.setStReference12(null);
        policy.setDbPeriodRateBefore(BDUtil.hundred);
        policy.setStPeriodBaseBeforeID(policy.getStPeriodBaseID());
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);

        //set data setujui
        if(data.getStApprovedWho()!=null){
            policy.setStPostedFlag("Y");
            policy.setStEffectiveFlag("Y");
            policy.setDtApprovedDate(new Date());
            policy.setStApprovedWho(data.getStApprovedWho());
            policy.setStReadyToApproveFlag("Y");
        }

        //policy.checkEndorseNoBefore(policy.getStPolicyNo());


        final DTOList details = policy.getDetails();

        for (int l = 0; l < details.size(); l++) {
            InsurancePolicyItemsView items = (InsurancePolicyItemsView) details.get(l);

            items.setDbAmount(BDUtil.zero);
            items.setDbRate(BigDecimal.ZERO);
            items.setDbRatePct(BigDecimal.ZERO);

            if(items.isStampFee()) items.setDbAmount(BDUtil.zero);
            else if(items.isPolicyCost()) items.setDbAmount(BDUtil.zero);
        }



            //LOOPING ALL OBJECT POLIS
            final DTOList objects = policy.getObjects();

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(j);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;


                final DTOList coverage = obj.getCoverage();

                for (int k = 0; k < coverage.size(); k++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(k);

                    cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                    if(cov.isEntryPremi()){
                        cov.setStEntryRateFlag("N");
                         cov.setStEntryPremiFlag("Y");
                         cov.setDbPremi(BDUtil.zero);
                         cov.setDbPremiNew(BDUtil.zero);
                    }

                }

                obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                //Looping object yang restitusi, cek kalau no urut sama, proses endorse retur nya
                for (int i = 0; i < detilObject.size(); i++) {

                        PengajuanEndorsemenView det = (PengajuanEndorsemenView) detilObject.get(i);

                        if(det.getStApprovedWho()!=null){
                            policy.setStPostedFlag("Y");
                            policy.setStEffectiveFlag("Y");
                            policy.setDtApprovedDate(new Date());
                            policy.setStApprovedWho(det.getStApprovedWho());
                            policy.setStReadyToApproveFlag("Y");
                        }

                        //if(!objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())) continue;

                        //proses batal debitur yg di ajukan

                        if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                            //policy.setDbPeriodRateBefore(det.getDbRestitusiPct());

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("21")){
                                objx.setDbReference4(BDUtil.zero);
                                if(!Tools.isYes(objx.getStReference9()))
                                    objx.setDbReference6(BDUtil.negate(objx.getDbReference6()));
                            }

                            if(policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                                //objx.setDtReference5(det.getDtTanggalRestitusi());
                                //objx.setStReference9(det.getStSisaJangkaWaktu());
                            }
                        }

                        final DTOList coverage2 = obj.getCoverage();

                        for (int l = 0; l < coverage2.size(); l++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage2.get(l);

                            cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                            //jika mode endorse pembatalan debitur
                            if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                                cov.setStEntryRateFlag("N");
                                cov.setStEntryPremiFlag("Y");
                                cov.setDbPremi(det.getDbPremi());
                                cov.setDbPremiNew(det.getDbPremi());

                                if(cov.isEntryInsuredAmount()){
                                    cov.setDbInsuredAmount(BDUtil.zero);
                                }
                            }

                        }

                        final DTOList suminsureds = obj.getSuminsureds();

                        for (int m = 0; m < suminsureds.size(); m++) {
                            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(m);

                            tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                            if(objx.getStOrderNo().equalsIgnoreCase(det.getStNomorUrut())){
                                if(tsi.getStAutoFlag()!=null)
                                    if(tsi.getStAutoFlag().equalsIgnoreCase("Y"))
                                        tsi.setStAutoFlag(null);

                                tsi.setDbInsuredAmount(BDUtil.zero);
                            }
                        }

                }

        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

        policy.setStEndorseNotes("ENDORSEMENT RESTITUSI DEBITUR");

        if(true){
            policy.setStEndorseNotes("ENDORSEMENT RESTITUSI "+ detilObject.size() +" DEBITUR");
        }

        policy.setStGatewayDataFlag("Y");
        //policy.setStGatewayPolID(data.getStPolicyID());

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();

        //simpen
        String policyID = "";

         policyID = getRemoteInsurance().saveEndorseFromGateway(policy, policy.getStNextStatus(), true);

        logger.logWarning("################ pol id endorsemen = "+ policyID);

        return policyID;

    }

    public void superEditModeRestitusiH2H_V2(PengajuanEndorsemenView data) throws Exception {
        viewRestitusiH2H_V2(data);

        //super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
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

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();
        //policy.getCoverageReinsurance().markAllUpdate();
        //policy.getClausules().markAllUpdate();

        policy.showItemsAccount();

        //policy.reCalculateInstallment();

        //initTabs();

    }

    public void viewRestitusiH2H_V2(PengajuanEndorsemenView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        //policy = getInsurancePolicyH2H(data.getStNomorPolis());

        policy = getInsuranceLastPolicyH2H(data.getStPolicyNo());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }


}
