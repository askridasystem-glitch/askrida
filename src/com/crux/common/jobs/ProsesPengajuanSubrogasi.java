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
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.LookUpUtil;
import com.crux.util.SQLUtil;
import com.crux.web.form.Form;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.Part;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.h2h.model.WSDokumenKlaimView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.interkoneksi.model.PengajuanKlaimView;
import com.webfin.interkoneksi.model.PengajuanSubrogasiiView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author doni
 */

public class ProsesPengajuanSubrogasi extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanPolis.class);

    private String stClaimObject;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction())
                {
                    prosesSubrogasiH2H();
                }
                         

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
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

    public InsurancePolicyItemsView onNewClaimItem(String insItemID) throws Exception {
        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.CLAIM);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStChargableFlag("Y");

        getClaimItems().add(item);

        return item;
    }

    public DTOList getClaimItems() {
        return policy.getClaimItems();
    }

    public void superEdit(InsurancePolicyView data) throws Exception {
        view(data);

        super.setReadOnly(false);

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

        policy.showItemsAccount();

    }

    public void view(InsurancePolicyView data) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(data.getStPolicyNo(), data.getStClaimPersonName());

        //if(!data.getStPolicyTypeID().equalsIgnoreCase("59") && !data.getStPolicyTypeID().equalsIgnoreCase("21"))
            
        if(!data.getStPolicyTypeID().equalsIgnoreCase("59") && !data.getStPolicyTypeID().equalsIgnoreCase("21"))
             policy = getInsurancePolicyByObjectID(data.getStPolicyNo(), data.getStClaimObjectID());
        
        if(policy == null)
            policy = getInsurancePolicyByObjectID(data.getStPolicyNo(), data.getStClaimObjectID());

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
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and upper(b.ref1) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, nama.toUpperCase()},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public InsurancePolicyView getInsurancePolicyByObjectID(String stPolicyNo, String insPolicyObjectID) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and b.ins_pol_obj_id = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, insPolicyObjectID},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    private String getClaimSubrogasiInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='SUBROGATION' and active_flag='Y'");

        return lu.getCode(0);
    }

    private String getClaimFeeSubrogasiInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where ins_item_id='73' and active_flag='Y'");

        return lu.getCode(0);
    }

    /**
     * @return the stClaimObject
     */
    public String getStClaimObject() {
        return stClaimObject;
    }

    /**
     * @param stClaimObject the stClaimObject to set
     */
    public void setStClaimObject(String stClaimObject) {
        this.stClaimObject = stClaimObject;
    }

    private void prosesSubrogasiH2H() throws Exception{

        DTOList listPreClaim = null;

        //CARI PENGAJUAN KLAIM YANG BELUM DI PROSES
        listPreClaim = ListUtil.getDTOListFromQueryDS(
                " select * "+
                " from ws_subrogasi  "+
                " where proses_flag is null and tgl_proses is null "+
                " order by id", PengajuanSubrogasiiView.class,"GATEWAY");

        for (int i = 0; i < listPreClaim.size(); i++) {
            PengajuanSubrogasiiView data = (PengajuanSubrogasiiView) listPreClaim.get(i);

            System.out.println("Bikin subrogasi aplikasi kredit : "+ data.getStNomorLoan());

            InsurancePolicyView klaim = getInsurancePolicy(data.getStNomorPolis(), data.getStNamaDebitur(), data.getStNomorUrut());

            if(klaim!=null){

                //if(data.isIncludeDocumentClaim()&& !data.isDocumentCompleted()) continue;

                //simpan LKP ENDORSE nya
                String policyID = editCreateSubrogasiH2H(klaim, data);

                //UPDATE DATA PRE CLAIM YANG SUDAH DI PROSES

                final SQLUtil S2 = new SQLUtil("GATEWAY");

                PreparedStatement PS = S2.setQuery("update ws_subrogasi set proses_flag = 'Y', tgl_proses = 'now' where nomor_loan = ?");

                PS.setObject(1, data.getStNomorLoan());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStNomorLoan() +" ++++++++++++++++++");
            }
            

        }

    }

    public String editCreateSubrogasiH2H(InsurancePolicyView data, PengajuanSubrogasiiView klaim) throws Exception {
        superEditH2H(data,klaim);

        setStClaimObject(String.valueOf(Integer.parseInt(policy.getStAccountno()) - 1));

        checkActiveEffective();

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.generateClaimEndorseDLA();
        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);

        
        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(policy.getStCurrencyCode());
        policy.setDbCurrencyRateClaim(policy.getDbCurrencyRate());

        policy.setDtDLADate(new Date());
        policy.setDbClaimAmountApproved(klaim.getDbNilaiSubrogasi());
        policy.setDbClaimSubroPaid(new BigDecimal(100));

        if(klaim.getStTransactionNo()!=null)
            policy.setStReferenceNo(klaim.getStTransactionNo());

        policy.setStEndorseNotes("DENGAN INI DICATAT DAN DISETUJUI BERDASARKAN SETORAN KE REKENING PT. ASURANSI BANGUN ASKRIDA TERDAPAT TRANSFER RECOVERY SEBESAR Rp. "+ klaim.getDbNilaiSubrogasi() +" DARI DEBITUR AN. "+ klaim.getStNamaDebitur() +" MAKA DARI ITU KAMI MELAKUKAN ENDORSEMENT SUBROGASI SEBESAR NILAI TERSEBUT.");

        final DTOList claimItems = policy.getClaimItems();

        //hapus item klaim bruto
        claimItems.deleteAll();

        //tambah subrogasi
        if (claimItems.size()==0) {

            String insItemID = getClaimSubrogasiInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbNilaiSubrogasi());
        }

        //tambah fee
        if (true) {

            String insItemID = getClaimFeeSubrogasiInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbJumlahFee());
        }

        getPolicy().recalculate();

        //simpen
        String policyID = getRemoteInsurance().saveKlaimH2H(policy,policy.getStNextStatus(),false);

        return policyID;
    }

    public InsurancePolicyView getInsurancePolicy(String stPolicyNo, String nama, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno "+
                                            " from ins_policy a "+
                                            " inner join ins_pol_obj b on a.pol_id = b.pol_id  "+
                                            " where a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' and effective_flag = 'Y'  "+
                                            " and substr(a.pol_no, 0, 17) = ? and trim(upper(b.ref1)) = ?  "+
                                            " and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, nama.trim().toUpperCase(),noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public void superEditH2H(InsurancePolicyView data, PengajuanSubrogasiiView klaim) throws Exception {
        viewH2H(data, klaim);

        super.setReadOnly(false);

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

        policy.showItemsAccount();

    }

    public void viewH2H(InsurancePolicyView data, PengajuanSubrogasiiView klaim) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(klaim.getStNomorPolis(), klaim.getStNamaDebitur(),klaim.getStNomorUrut());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public void selectClaimObjectH2H(PengajuanKlaimView klaim) throws Exception {

        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjects().get(Integer.parseInt(getStClaimObject()));

        boolean canNotClaim = policy.validateClaimObject(o);

        if(canNotClaim){
            setStClaimObject(null);
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim sudah nol, tidak bisa dilakukan pembuatan klaim");
        }

        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());

            policy.setObjIndex(getStClaimObject());

            policy.setDbClaimAmountEstimate(klaim.getDbJumlahKlaim());

        }

        o.setStClaimLossID(klaim.getStClaimLossID());

    }


}
