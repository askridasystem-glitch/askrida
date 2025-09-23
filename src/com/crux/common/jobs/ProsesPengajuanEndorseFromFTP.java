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
import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyPreEndorseView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.PengajuanEndorsemenView;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
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

public class ProsesPengajuanEndorseFromFTP extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanEndorseFromFTP.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction()){
                    //proses endorsemen input di interkoneksi
                    prosesDataMenjadiEndorse();

                    //proses endorsemen dari h2h
                    prosesPengajuanEndorseH2H();
                }
                        

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }


    private void prosesDataMenjadiEndorse() throws Exception{

        final SQLUtil S = new SQLUtil();

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

        final SQLUtil S = new SQLUtil();

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

            CreateEndorseBatalTotalModeH2H(data);

            //UPDATE DATA PRE ENDORSE YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update pengajuan_endorse set proses_flag = 'Y', tgl_proses ='now', kode_status = '1', status= 'ON PROCESS' where pol_no = ?");

            PS.setObject(1, data.getStPolicyNo());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ data.getStPolicyNo() +" ++++++++++++++++++");


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

}
