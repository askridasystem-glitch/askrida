/***********************************************************************
 * Module:  com.crux.common.jobs.ProsesPolisRefund
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.webfin.FinCodec;
import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.joda.time.DateTime;

public class ProsesPolisRefund extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPolisRefund.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                execute1();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: proses refund debitur otomatis");

            long t = System.currentTimeMillis();

            final SQLUtil S = new SQLUtil();

            //PROSES DATA (JUMLAH POLIS)
            DTOList listPolicy = null;
            listPolicy = ListUtil.getDTOListFromQuery(
                    " select b.ref17 as pol_no "
                    + "from ins_policy a "
                    + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                    + "where a.status = 'POLICY' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                    + "and a.pol_type_id = '59' and b.ref17 is not null and b.ref22 = 'N' and a.gateway_data_f = 'Y' "
                    + "group by b.ref17 order by b.ref17 ",
                    InsurancePolicyView.class);

            for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView polObj = (InsurancePolicyView) listPolicy.get(i);

                //PROSES DATA ENDORSE
                DTOList listObject = null;
                listObject = ListUtil.getDTOListFromQuery(
                        " select a.pol_id,a.pol_no,a.cc_code,a.region_id,a.period_start,a.period_end,a.pol_type_id,a.entity_id,"
                        + "b.ins_pol_obj_id as master_policy_id,b.ref1,b.ref17 as ref2,b.ref3,b.refn8 as refn1,b.refd2 "
                        + "from ins_policy a "
                        + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                        + "where a.status = 'POLICY' and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                        + "and a.pol_type_id = '59' and b.ref17 is not null and b.ref22 = 'N' and a.gateway_data_f = 'Y' "
                        + "and b.ref17 = ? "
                        + "ORDER BY b.ins_pol_obj_id ",
                        new Object[]{polObj.getStPolicyNo()},
                        InsurancePolicyView.class);

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                        " SELECT * "
                        + " FROM INS_POLICY "
                        + " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "
                        + " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,1,16) = ? "
                        + " ORDER BY POL_ID DESC LIMIT 1",
                        new Object[]{polObj.getStPolicyNo().substring(0, 16)},
                        InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView) listEndorsemen.get(0);

                editCreateUploadEndorsePolis(polis.getStPolicyID(), listObject);
                recalculate();
                save();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set ref22 = 'Y' where ins_pol_obj_id = ?");

                PS.setObject(1, polObj.getStMasterPolicyID());

                int j = PS.executeUpdate();

                if (j != 0) {
                    logger.logInfo("+++++++ UPDATE STATUS POLIS : " + polObj.getStReference2() + " ++++++++++++++++++");
                }

                t = System.currentTimeMillis() - t;

                logger.logInfo("proses 2 selesai dalam " + t + " ms");

            }
        } finally {
            conn.close();
        }
    }
    private InsurancePolicyView policy;

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

    public void editCreateUploadEndorsePolis(String policyID, DTOList object) throws Exception {
        superEdit(policyID);

        policy.generateEndorseNo();
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStPeriodBaseBeforeID(null);
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
        policy.setStPeriodBaseBeforeID("2");
        policy.setDtPaymentDate(null);
        policy.setStPaymentNotes(null);
        policy.setDbClaimAdvancePaymentAmount(null);
        policy.setStReceiptNo(null);
        policy.setDtPaymentDate(null);
        policy.setStPrintCounter(null);
        policy.setStSignCode(null);
        policy.setStReinsuranceApprovedWho(null);
        policy.setStApprovedReinsName(null);

        InsurancePolicyView objectUpload = (InsurancePolicyView) object.get(0);
        policy.setStDescription("Endorse Restitusi, TopUp pada polis " + objectUpload.getStPolicyNo());

        DateTime startDate = new DateTime(policy.getDtPeriodStart());
        DateTime endDate = new DateTime(policy.getDtPeriodEnd());
        if (startDate.isEqual(endDate)) {
            policy.setStAllowSamePeriodFlag("Y");
        }

        policy.getDetails().deleteAll();

        policy.setStEndorseNotes("DENGAN INI DICATAT DAN DISETUJUI, BAHWA :\n\n"
                + "1). PINJAMAN KREDIT ATAS NAMA YANG TERCANTUM PADA LAMPIRAN POLIS INI TELAH DILUNASI.\n"
                + "2). ATAS HAL TERSEBUT, MAKA DILAKUKAN RESTITUSI PREMI SESUAI DENGAN SYARAT DAN KETENTUAN YANG BERLAKU");

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

            policy.invokeCustomCriteria(obj);

            for (int j = 0; j < object.size(); j++) {
                InsurancePolicyView objectUploadDet = (InsurancePolicyView) object.get(j);

                if (objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference1().trim().toUpperCase())
                        || objx.getStReference3().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference3().trim().toUpperCase())) {
                    if (objectUploadDet.getDtReference2() != null) {
                        objx.setDtReference5(objectUploadDet.getDtReference2());
                    }
                    policy.setDbPeriodRateBefore(new BigDecimal(30));

                }

            }

            final DTOList coverage = obj.getCoverage();

            for (int j = 0; j < coverage.size(); j++) {
                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                cov.setStInsurancePolicyCoverRefID(cov.getStInsurancePolicyCoverID());

                if (cov.isEntryPremi()) {
                    cov.setStEntryPremiFlag(null);
                }

                for (int k = 0; k < object.size(); k++) {
                    InsurancePolicyView objectUploadDet = (InsurancePolicyView) object.get(k);

                    if (objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference1().trim().toUpperCase())
                            || objx.getStReference3().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference3().trim().toUpperCase())) {
                        if (objectUploadDet.getDbReference1() != null) {
                            cov.setDbPremi(objectUploadDet.getDbReference1());
                            cov.setDbPremiNew(objectUploadDet.getDbReference1());

                            if (!cov.isEntryRate()) {
                                cov.setStEntryPremiFlag("Y");
                            }
                        }

                    }
                }
            }

            final DTOList suminsureds = obj.getSuminsureds();

            for (int j = 0; j < suminsureds.size(); j++) {
                InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

                tsi.setStInsurancePolicyTSIRefID(tsi.getStInsurancePolicyTSIID());

                for (int k = 0; k < object.size(); k++) {
                    InsurancePolicyView objectUploadDet = (InsurancePolicyView) object.get(k);

                    if (objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference1().trim().toUpperCase())
                            || objx.getStReference3().trim().toUpperCase().equalsIgnoreCase(objectUploadDet.getStReference3().trim().toUpperCase())) {
                        tsi.setDbInsuredAmount(BDUtil.zero);
                    }
                }
            }
        }

        policy.setStNextStatus(FinCodec.PolicyStatus.ENDORSE);

    }

    public void recalculate() throws Exception {

        policy.recalculateInterkoneksi();
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public void save() throws Exception {
        getRemoteInsurance().saveFromTeks(policy, policy.getStNextStatus(), false);
    }

    public void superEdit(String policyID) throws Exception {
        view(policyID);

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
            }

            if (policy.isStatusPolicy() || policy.isStatusClaim() || policy.isStatusEndorse() || policy.isStatusRenewal() || policy.isStatusHistory() || policy.isStatusEndorseRI() || policy.isStatusTemporaryPolicy() || policy.isStatusTemporaryEndorsemen()) {
                if (obj.getStInsuranceTreatyID() == null) {
                    if (policy.getDtPeriodStart() != null) {
                        obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                    } else {
                        obj.setStInsuranceTreatyID(Parameter.readString(inward ? "UWRIT_DEF_ITREATY" : "UWRIT_DEF_OTREATY"));
                    }
                }
            }

            if (policy.isStatusPolicy() || policy.isStatusClaim() || policy.isStatusEndorse() || policy.isStatusRenewal() || policy.isStatusHistory() || policy.isStatusEndorseRI() || policy.isStatusTemporaryPolicy() || policy.isStatusTemporaryEndorsemen()) {
                if (obj.getStInsuranceTreatyID() == null) {
                    throw new RuntimeException("Unable to find suitable treaty for this policy !!");
                }
            }

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();

        policy.showItemsAccount();
    }

    public void view(String policyID) throws Exception {

        if (policyID == null) {
            throw new RuntimeException("ID Data Belum Dipilih");
        }

        policy = getRemoteInsurance().getInsurancePolicy(policyID);

        if (policy == null) {
            throw new RuntimeException("Policy not found");
        }

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();
    }
}
