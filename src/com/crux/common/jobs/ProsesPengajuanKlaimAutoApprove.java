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
import com.crux.util.LookUpUtil;
import com.crux.util.SQLUtil;
import com.crux.web.form.Form;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.Part;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.h2h.model.ProsesKlaimH2HView;
import com.webfin.h2h.model.WSDokumenKlaimView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.interkoneksi.model.PengajuanKlaimView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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

public class ProsesPengajuanKlaimAutoApprove extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanKlaimAutoApprove.class);

    //private String downloadPath = Parameter.readString("SYS_FTP_DOWNLOAD_FOLDER");
    //private String historyPath = Parameter.readString("SYS_FTP_HISTORY_FOLDER");
    private String stClaimObject;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
 
                //if(JobUtil.isServerProduction())
                { 

                    //prosesDataMenjadiLKS();

                    //prosesSubrogasi();

                    prosesKlaimH2HMenjadiLKP();

                }
                         

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void prosesDataMenjadiLKS() throws Exception{

        DTOList listPreClaim = null;

        //CARI PENGAJUAN KLAIM YANG BELUM DI PROSES
        listPreClaim = ListUtil.getDTOListFromQueryDS(
                " select * "+
                "  from ins_policy_preclaim  "+
                "  where effective_flag = 'Y' and  "+
                "  status = 'PRECLAIM' and coalesce(gateway_data_f,'N') <> 'Y' "+
                "  order by poL_id", InsurancePolicyView.class,"GATEWAY");

        for (int i = 0; i < listPreClaim.size(); i++) {
            InsurancePolicyView data = (InsurancePolicyView) listPreClaim.get(i);

            System.out.println("Bikin LKS pre claim : "+ data.getStPLANo());
            
            editCreateClaimPLA(data);

            //UPDATE DATA PRE CLAIM YANG SUDAH DI PROSES

            final SQLUtil S2 = new SQLUtil("GATEWAY");

            PreparedStatement PS = S2.setQuery("update ins_policy_preclaim set gateway_data_f = 'Y', status_other='TRANSFERED' where pol_id = ?");

            PS.setObject(1, data.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStPLANo() +" ++++++++++++++++++");

            
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

    public void editCreateClaimPLA(InsurancePolicyView data) throws Exception {
        superEdit(data);

        setStClaimObject(String.valueOf(Integer.parseInt(policy.getStAccountno()) - 1));

        checkActiveEffective();

        //policy.checkLastPolicyNo(policy.getStPolicyNo());

        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");

//        if(blockPremiNotPaid)
//            if(!policy.isPremiPaidInOldSystem())
//                throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");

        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        //policy.setStReference3(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);

        policy.setDtClaimDate(data.getDtClaimDate());
        policy.setDtPLADate(new Date());

        policy.setStClaimEventLocation(data.getStClaimEventLocation());
        policy.setStClaimPersonName(data.getStClaimPengajuName());
        policy.setStClaimPersonAddress(data.getStClaimPersonAddress());
        policy.setStClaimPersonStatus(data.getStClaimPersonStatus());

        policy.setStClaimDocuments("-");
        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(policy.getStCurrencyCode());
        policy.setDbCurrencyRateClaim(policy.getDbCurrencyRate());

        policy.setStEntityID(data.getStEntityID());
        policy.setStCustomerName(data.getStCustomerName());
        policy.setStCustomerAddress(data.getStCustomerAddress());

        if(data.getStClaimCauseID()!=null)
            policy.setStClaimCauseID(data.getStClaimCauseID());

        if(data.getStClaimPersonName()!=null)
            policy.setStClaimPersonName(data.getStClaimPersonName());

        if(data.getStClaimChronology()!=null)
            policy.setStClaimChronology(data.getStClaimChronology());

        if(data.getStClaimEventLocation()!=null)
            policy.setStClaimEventLocation(data.getStClaimEventLocation());

        if(data.getStClaimBenefit()!=null)
            policy.setStClaimBenefit(data.getStClaimBenefit());

        if(data.getStAdminNotes()!=null)
            if(data.getStAdminNotes().equalsIgnoreCase("AUTO_APPROVE"))
                policy.setStEffectiveFlag("Y");

        policy.setStCostCenterCode(data.getStCostCenterCode());
        policy.setStRegionID(data.getStRegionID());

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            if(objx.getStOrderNo()==null){

                objx.setStOrderNo(String.valueOf(i+1));

                SQLUtil S = new SQLUtil();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ objx.getStOrderNo() +" where ins_pol_obj_id = ?");

                PS.setObject(1,obj.getStPolicyObjectID());

                int j = PS.executeUpdate();

                S.release();
            }


        }

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

        }

        //PILIH OBJEK KLAIM
        selectClaimObject(data);
 
        getPolicy().recalculateInterkoneksi2();

        //simpen
        getRemoteInsurance().saveLKSToGateway(policy,policy.getStNextStatus(),false);


    }

    
    public void selectClaimObject(InsurancePolicyView data) throws Exception {

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

            final DTOList tsi = o.getSuminsureds();

            BigDecimal totalTSI = null;
            for (int i = 0; i < tsi.size(); i++) {
                InsurancePolicyTSIView suminsured = (InsurancePolicyTSIView) tsi.get(i);

                totalTSI = BDUtil.add(totalTSI, suminsured.getDbInsuredAmount());
            }

            policy.setDbClaimAmountEstimate(totalTSI);
        }

        if(data.getStClaimLossID()!=null)
            o.setStClaimLossID(data.getStClaimLossID());

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

    public void superEdit(String policyID) throws Exception {
        view(policyID);

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

                //final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();

                final DTOList share = trdi.getShares();
                for (int k = 0; k < share.size(); k++) {
                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(k);

                    ri.getInstallment().markAllUpdate();

                }
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

    public void view(String policyID) throws Exception {

        if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getRemoteInsurance().getInsurancePolicy(policyID);

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        //policy.reCalculateInstallment();

        //policy.reCalculateInstallment2();

        super.setReadOnly(true);

        //productionModeTabs();

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

    private String getClaimBrutoInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='CLAIMG' and active_flag='Y'");

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

    private void prosesKlaimH2HMenjadiLKP() throws Exception{

        DTOList listPreClaim = null;

        //CARI PENGAJUAN KLAIM YANG BELUM DI PROSES

        //terakhir proses sd batch 11 & 12, lanjut batch 21 & 22
        listPreClaim = ListUtil.getDTOListFromQuery(
                " SELECT *, no_urut as accountno "+
                " FROM proses_klaim_h2h_auto "+
                " where batch in ('17','18') "+
                " and proses_flag is null "+
                " order by tgl_pengajuan", ProsesKlaimH2HView.class);

        Date tanggalLKP = DateUtil.getDate("24/06/2025");

        for (int i = 0; i < listPreClaim.size(); i++) {
            ProsesKlaimH2HView data = (ProsesKlaimH2HView) listPreClaim.get(i);

            System.out.println("############ Bikin LKP no rek pinjaman : "+ data.getStNomorRekeningPinjaman());

            //get polis by nopol,nama, no rek pinjaman
            InsurancePolicyView polis = getInsurancePolicyWithOrderNo(data.getStPolicyNo(), data.getStNoUrut());

            if(polis!=null){

                //simpan LKS nya
                String policyID = editCreateClaimPLAH2H(polis, data);

                //update LKS yang setujui
                 final SQLUtil S4 = new SQLUtil();

                PreparedStatement PS4 = S4.setQuery("update ins_policy set approved_who = ?, approved_date = ?, claim_approved_date = ? where pol_id = ?");

                PS4.setObject(1, "04700751");
                PS4.setObject(2, tanggalLKP);
                PS4.setObject(3, tanggalLKP);
                PS4.setObject(4, policyID);

                int l = PS4.executeUpdate();

                S4.release();

                //buat jadi LKP
                String polIDDLA = editCreateClaimDLA(policyID, data);

                //update LKP yang setujui
                 final SQLUtil S3 = new SQLUtil();

                PreparedStatement PS3 = S3.setQuery("update ins_policy set approved_who = ?, approved_date = ?, claim_approved_date = ? where pol_id = ?");

                PS3.setObject(1, "04700751");
                PS3.setObject(2, tanggalLKP);
                PS3.setObject(3, tanggalLKP);
                PS3.setObject(4, polIDDLA);

                int k = PS3.executeUpdate();

                S3.release();
                
                //UPDATE DATA  CLAIM YANG SUDAH DI PROSES

                final SQLUtil S2 = new SQLUtil();

                PreparedStatement PS = S2.setQuery("update proses_klaim_h2h_auto set proses_flag = 'Y', pol_id = ?, dla_no=? where no_rek_pinjaman = ? and no_perjanjian_kredit = ?");

                PS.setObject(1, polIDDLA);
                PS.setObject(2, dla_no);
                PS.setObject(3, data.getStNomorRekeningPinjaman());
                PS.setObject(4, data.getStNomorPK());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStNomorRekeningPinjaman() +" ++++++++++++++++++");

                S2.release();
            }
            

        }

    }
    
    private String dla_no;

    public String editCreateClaimDLA(String policyID, ProsesKlaimH2HView klaim) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        //final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");


        //if(blockPremiNotPaid)
        //    if(!policy.isPremiPaidForClaim())
        //        throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");


        if(!policy.isStatusClaimPLA()){
            throw new RuntimeException("DLA hanya bisa dibuat dari PLA");
        }

        if (!policy.isEffective()) {

            throw new RuntimeException("Policy not yet effective");
        }

      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/

        policy.generateDLANo();
        dla_no = policy.getStDLANo();


        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStCoverNoteFlag("Y");
        //policy.setStReference3(null);
        policy.setStApprovedWho("04700751");
        policy.setDtApprovedDate(klaim.getDtDLADate());
        policy.setDtApprovedClaimDate(klaim.getDtDLADate());
        policy.setStEffectiveClaimFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setDtDLADate(new Date());
        //policy.setStReference3(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setDbClaimAmountApproved(klaim.getDbClaimAmount());
        policy.setStRIFinishFlag("Y");

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem2(insItemID);

            item.setStChargableFlag("Y");

            //item.setStROFlag("Y");
        }

        onChgCurrencyClaim();

        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        getPolicy().setClaimObject(objectClaim);
        getPolicy().recalculate();
        policy.setStRIFinishFlag("Y");

        //simpen
        String policyIDDLA = getRemoteInsurance().saveKlaimH2H(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        getRemoteInsurance().saveNotaToCare(policy, true);

        return policyIDDLA;


    }


    public void onChgCurrencyClaim() throws Exception {
        policy.setDbCurrencyRateClaim(
                CurrencyManager.getInstance().getRate(
                policy.getStClaimCurrency(),
                policy.getDtPolicyDate()
                )
                );

        policy.setStClaimCurrency(policy.getStClaimCurrency());

    }

    public InsurancePolicyItemsView onNewClaimItem2(String insItemID) throws Exception {
        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.CLAIM);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStChargableFlag("Y");

        getClaimItems().add(item);


        //policy.recalculate();

        return item;
    }

    public String saveDocumentClaim(WSDokumenKlaimView doc) throws Exception{
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


    public String editCreateClaimPLAH2H(InsurancePolicyView data, ProsesKlaimH2HView klaim) throws Exception {
        superEditH2H(data,klaim);

        //setStClaimObject(String.valueOf(Integer.parseInt(policy.getStAccountno()) - 1));
        setStClaimObject("0");

        checkActiveEffective();

        //final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");

//        if(blockPremiNotPaid)
//            if(!policy.isPremiPaidInOldSystem())
//                throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");

        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho("04700751");
        policy.setDtApprovedDate(klaim.getDtDLADate());
        policy.setDtApprovedClaimDate(klaim.getDtDLADate());
        policy.setStEffectiveClaimFlag("Y");
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        policy.setStRIFinishFlag("Y");

        policy.setDtPLADate(new Date());
        policy.setDtClaimProposeDate(klaim.getDtTanggalPengajuan());

        EntityView customer = policy.getEntity();

        policy.setDtClaimDate(klaim.getDtClaimDate());

        policy.setStClaimEventLocation("-");
        policy.setStClaimPersonName(customer.getStEntityName());
        policy.setStClaimPersonAddress(customer.getStAddress());
        policy.setStClaimPersonStatus("KREDITUR");
        policy.setStClaimCauseID("3872");
        policy.setStClaimSubCauseID("112");
        policy.setStClaimBenefit("4");
        policy.setStClaimDocuments("TERLAMPIR");
        policy.setStClaimChronology("-");

        policy.setStGatewayDataFlag("Y");
        policy.setStGatewayPolID(data.getStPolicyID());
        policy.setStClaimCurrency(policy.getStCurrencyCode());
        policy.setDbCurrencyRateClaim(policy.getDbCurrencyRate());

        //new
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setDtClaimPaymentDate(null);
        policy.setStPaymentNotes(null);

        //if(klaim.getStTransactionNo()!=null)
            //policy.setStReferenceNo(klaim.getStTransactionNo());

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            if(objx.getStOrderNo()==null){

                objx.setStOrderNo(String.valueOf(i+1));

                SQLUtil S = new SQLUtil();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ objx.getStOrderNo() +" where ins_pol_obj_id = ?");

                PS.setObject(1,obj.getStPolicyObjectID());

                int j = PS.executeUpdate();

                S.release();
            }

        }

        final DTOList claimItems = policy.getClaimItems();

        if (claimItems.size()==0) {

            String insItemID = getClaimBrutoInsItemID();

            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);

            item.setStChargableFlag("Y");

        }

        //PILIH OBJEK KLAIM
        selectClaimObjectH2H(klaim);

        getPolicy().recalculate();
        getPolicy().recalculateTreaty();
        policy.setStRIFinishFlag("Y");

        //simpen
        String policyID = getRemoteInsurance().saveKlaimH2H(policy,policy.getStNextStatus(),false);

        return policyID;
    }

    public InsurancePolicyView getInsurancePolicy(String stPolicyNo, String nama, String noAplikasi) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and trim(upper(b.ref1)) = ? and trim(upper(b.ref16)) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, nama.trim().toUpperCase(),noAplikasi.trim().toUpperCase()},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public void superEditH2H(InsurancePolicyView data, ProsesKlaimH2HView klaim) throws Exception {
        viewH2H(data, klaim);

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStNoUrut());

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

    public void viewH2H(InsurancePolicyView data, ProsesKlaimH2HView klaim) throws Exception {

        //if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        
        policy = getInsurancePolicyWithOrderNo(data.getStPolicyNo(), klaim.getStNoUrut());

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    public void selectClaimObjectH2H(ProsesKlaimH2HView klaim) throws Exception {

        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjects().get(Integer.parseInt(getStClaimObject()));

        boolean canNotClaim = policy.validateClaimObject(o);

        if(canNotClaim){
            setStClaimObject(null);
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim "+ klaim.getStPolicyNo() +" "+ klaim.getStNama() +" sudah nol, tidak bisa dilakukan pembuatan klaim");
        }

        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());

            policy.setObjIndex(getStClaimObject());

            policy.setDbClaimAmountEstimate(klaim.getDbClaimAmount());

        }

        o.setStClaimLossID("181");

        final DTOList objects = policy.getObjectsByOrderNo(klaim.getStNoUrut());
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        policy.setClaimObject(objectClaim);

    }

    public InsurancePolicyView getInsurancePolicyWithOrderNo(String stPolicyNo, String noUrut) throws Exception {

        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ?  and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    private void resendDocumentClaim() throws Exception{

        DTOList listDocuments = null;

        //CARI dokumen KLAIM YANG di replace, status 0
        listDocuments = ListUtil.getDTOListFromQueryDS(
                "select a.*,b.no_urut as no_urut_pengajuan "+
                " from ws_dokumen_klaim a "+
                " left join pengajuan_klaim b on a.nomor_loan = b.no_aplikasi "+
                " where coalesce(document_status,'') = '3' and doc_transfer_flag is null order by a.create_date", WSDokumenKlaimView.class,"GATEWAY");

        for (int i = 0; i < listDocuments.size(); i++) {
            WSDokumenKlaimView doc = (WSDokumenKlaimView) listDocuments.get(i);

            //save ulang document nya
            String fileID = saveDocumentClaim(doc);

            //dapetin lks/lkp nya di core
            //by nopol & no rek dulu
            InsurancePolicyView klaimCore = getClaimWithPolNo(doc.getStNomorPolis(), doc.getStNomorLoan());

            //jika klaimCore tidak ditemukan, cari by nopol & no urut
            if(klaimCore==null){
                if(doc.getStNoUrutPengajuan()!=null){
                    klaimCore = getClaimWithOrderNo(doc.getStNomorPolis(), doc.getStNoUrutPengajuan());
                }
            }

            //jika klaim nya ada, baru inject ulang dokumen nya
            if(klaimCore!=null){

                //dapetin doc klaim nya
                final DTOList listDocClaim = klaimCore.getClaimDocuments();

                for (int j = 0; j < listDocClaim.size(); j++) {
                    InsurancePolicyDocumentView docKlaim = (InsurancePolicyDocumentView) listDocClaim.get(j);

                    if(docKlaim.getStInsuranceDocumentTypeID().equalsIgnoreCase(doc.getStKodeDokumenAskrida())){

                        //jika sudah ada, update dokumen nya
                        if(docKlaim.getStInsurancePolicyDocumentID()!=null){
                            final SQLUtil S2 = new SQLUtil();

                            PreparedStatement PS = S2.setQuery("update ins_pol_documents set file_physic = ?, change_date='now', change_who='sys' where ins_pol_document_id = ? ");

                            PS.setObject(1, fileID);
                            PS.setObject(2, docKlaim.getStInsurancePolicyDocumentID());

                            int k = PS.executeUpdate();

                            S2.release();
                        }

                        //jika belum ada, insert record dokumen
                        if(docKlaim.getStInsurancePolicyDocumentID()==null){
                            docKlaim.markNew();

                            docKlaim.setStSelectedFlag("Y");
                            docKlaim.setStFilePhysic(fileID);
                            docKlaim.store();
                        }

                    }

                }

                    //UPDATE DATA doc klaim yang sudah di proses
                    final SQLUtil S2 = new SQLUtil("GATEWAY");

                    PreparedStatement PS = S2.setQuery("update ws_dokumen_klaim set doc_transfer_flag = 'Y' where nomor_loan = ? and kode_dokumen = ?");

                    PS.setObject(1, doc.getStNomorLoan());
                    PS.setObject(2, doc.getStKodeDokumen());

                    int j = PS.executeUpdate();

                    S2.release();

                    if (j!=0) logger.logInfo("+++++++ UPDATE STATUS dokumen : "+ doc.getStNomorLoan() +" ++++++++++++++++++");
            }
            

        }

    }

    public InsurancePolicyView getClaimWithOrderNo(String stPolicyNo, String noUrut) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('CLAIM') and a.active_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and b.order_no = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noUrut},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public InsurancePolicyView getClaimWithPolNo(String stPolicyNo, String noAplikasi) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select a.*,b.order_no as accountno"+
                                        " from ins_policy a "+
                                        " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                        " where a.status in ('CLAIM')  and a.active_flag = 'Y' "+
                                        " and substr(a.pol_no, 0, 17) = ? and trim(upper(b.ref16)) = ? order by a.pol_id desc limit 1",
                new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16):stPolicyNo, noAplikasi.trim().toUpperCase()},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    private void prosesSubrogasi() throws Exception{

        DTOList listPreClaim = null;

        //CARI PENGAJUAN KLAIM YANG BELUM DI PROSES

        //terakhir proses sd batch 11 & 12, lanjut batch 21 & 22
        listPreClaim = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM proses_subrogasi "+
                " where proses_flag is null "+
                " order by dla_no", ProsesKlaimH2HView.class);

        Date tanggalLKP = DateUtil.getDate("23/06/2025");

        for (int i = 0; i < listPreClaim.size(); i++) {
            ProsesKlaimH2HView data = (ProsesKlaimH2HView) listPreClaim.get(i);

            System.out.println("########### create subrogasi no lkp : "+ data.getStDLANo());

            //get DLA nya
            InsurancePolicyView dla = getDLA(data.getStDLANo());

            if(dla!=null){

                
                //buat jadi LKP endorse
                String polIDDLA = editCreateClaimDLAEndorse(dla.getStPolicyID(), data);

                //update LKP yang setujui
                 final SQLUtil S3 = new SQLUtil();

                PreparedStatement PS3 = S3.setQuery("update ins_policy set approved_who = ?, approved_date = ?, claim_approved_date = ?  where pol_id = ?");

                PS3.setObject(1, "05800434");
                PS3.setObject(2, tanggalLKP);
                PS3.setObject(3, tanggalLKP);
                PS3.setObject(4, polIDDLA);

                int k = PS3.executeUpdate();

                S3.release();

                //UPDATE DATA  CLAIM YANG SUDAH DI PROSES

                final SQLUtil S2 = new SQLUtil();

                PreparedStatement PS = S2.setQuery("update proses_subrogasi set proses_date = 'now', proses_flag = 'Y', pol_id = ?, dla_no_subrogasi =? where dla_no = ?");

                PS.setObject(1, polIDDLA);
                PS.setObject(2, dla_no);
                PS.setObject(3, data.getStDLANo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS subrogasi : "+ data.getStDLANo() +" ++++++++++++++++++");

                S2.release();
            }


        }

    }

    public InsurancePolicyView getDLA(String stDLANo) throws Exception {

        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy a "+
                                        " where a.status in ('CLAIM') and effective_flag = 'Y' "+
                                        " and dla_no = ? ",
                new Object[]{stDLANo},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    public String editCreateClaimDLAEndorse(String policyID, ProsesKlaimH2HView klaim) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        //final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");


        //if(blockPremiNotPaid)
        //    if(!policy.isPremiPaidForClaim())
        //        throw new RuntimeException("Premi Belum Dibayar, Konfirmasi Ke Bagian Keuangan");


        if(!policy.isStatusClaimDLA()){
            throw new RuntimeException("DLA endorse hanya bisa dibuat dari DLA");
        }

        if (!policy.isEffective()) {

            throw new RuntimeException("Policy not yet effective");
        }

      /*if (!FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(policy.getStStatus())) {
         throw new RuntimeException("Policy not in claimable state");
      }*/

        policy.generateClaimEndorseDLA();

        dla_no = policy.getStDLANo();


        policy.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSECLAIM);
        policy.setStPostedFlag("Y");
        policy.setStEffectiveFlag("Y");
        policy.setStCoverNoteFlag("Y");
        //policy.setStReference3(null);
        policy.setStApprovedWho("05800434");
        policy.setDtApprovedDate(new Date());
        policy.setDtApprovedClaimDate(new Date());
        policy.setStEffectiveClaimFlag("Y");
        policy.setStActiveClaimFlag("Y");
        policy.setDtDLADate(new Date());
        //policy.setStReference3(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag("Y");
        //policy.setDbClaimAmountApproved(klaim.getDbClaimAmount());
        policy.setStRIFinishFlag("Y");
        policy.setStEndorseNotes("Pembentukan Subrogasi Klaim");
        policy.setStReference13("Y");
        //policy.setDbClaimSubroPaid(new BigDecimal(100));
        policy.setDbClaimAmountEndorse(policy.getDbClaimAmountApproved());

        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setDtClaimPaymentDate(null);
        policy.setStPaymentNotes(null);

        final DTOList claimItems = policy.getClaimItems();

        //hapus dulu item klaim
        claimItems.deleteAll();

        //tambahkan item klaim subrogasi
        if (claimItems.size()==0) {

            final InsurancePolicyItemsView item = onNewClaimItem2("48");

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbSubrogasiAmount());

            //item.setStROFlag("Y");
        }

        //tambahkan item fee subrogasi
        if (!BDUtil.isZeroOrNull(klaim.getDbFeeSubrogasiAmount())) {

            final InsurancePolicyItemsView item = onNewClaimItem2("73");

            item.setStChargableFlag("Y");

            item.setDbAmount(klaim.getDbFeeSubrogasiAmount());

            //item.setStROFlag("Y");
        }

        //onChgCurrencyClaim();
        /*
        final DTOList objects = policy.getObjects();
        InsurancePolicyObjectView objectClaim = (InsurancePolicyObjectView) objects.get(0);

        getPolicy().setClaimObject(objectClaim);
        */

        getPolicy().recalculate();
        policy.setStRIFinishFlag("Y");

        //simpen
        String policyIDDLA = getRemoteInsurance().saveKlaimH2H(policy,policy.getStNextStatus(), true);

        //Simpan nota ke sistem care via API
        getRemoteInsurance().saveNotaToCare(policy, true);

        return policyIDDLA;


    }


}
