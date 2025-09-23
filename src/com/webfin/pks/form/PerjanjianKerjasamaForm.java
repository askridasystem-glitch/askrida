/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:53 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.pks.form;

import com.crux.common.parameter.Parameter;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.WebFinLOVRegistry;
import com.webfin.pks.model.PerjanjianKerjasamaView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.util.Date;

public class PerjanjianKerjasamaForm extends Form {

    private final static transient LogManager logger = LogManager.getInstance(PerjanjianKerjasamaForm.class);
    private PerjanjianKerjasamaView policy;
    private boolean approvalMode;
    private boolean closeMode;

    public void changeBranch() {
    }

    public String getStStatus() {
        if (policy == null) {
            return null;
        }
        return policy.getStNextStatus() == null ? policy.getStStatus() : policy.getStNextStatus();
    }

    public void setStStatus(String stStatus) {
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public PerjanjianKerjasamaView getPolicy() {
        return policy;
    }

    public void setPolicy(PerjanjianKerjasamaView policy) {
        this.policy = policy;
    }

     public void createNew() throws Exception {
        policy = new PerjanjianKerjasamaView();

//        policy.setStCostCenterCode(UserManager.getInstance().getUser().getStBranch());

        policy.markNew();

        policy.setStStatus("PKS");
//        policy.setStReference1("1");
        policy.setStActiveFlag("Y");
        policy.setStEffectiveFlag("N");

        policy.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        policy.setDbCurrencyRate(BDUtil.one);
        policy.setDbPeriodRate(new BigDecimal(100));

        policy.setStInstallmentPeriodID(Parameter.readString("UWRIT_DEFAULT_INSTALLMENT"));

        policy.markNew();

    }


    public void afterUpdateForm() {
    }

    public void superEdit(String policyID) throws Exception {
        view(policyID);

        super.setReadOnly(false);

        policy.markUpdateO();

    }

    public void onFormCreate() {
    }

    public void view(String policyID) throws Exception {

        if (policyID == null) {
            throw new RuntimeException("Please select policy");
        }

        policy = getRemoteInsurance().getPerjanjianKerjasama(policyID);

        if (policy == null) {
            throw new RuntimeException("Policy not found");
        }

        super.setReadOnly(true);

    }

    public void approval(String policyID) throws Exception {

        if (policyID == null) {
            throw new RuntimeException("Please select policy");
        }

        policy = getRemoteInsurance().getPerjanjianKerjasama(policyID);

        if (policy == null) {
            throw new RuntimeException("Policy not found");
        }

        if (policy.isEffective()) {
            throw new Exception("This document is not editable, because it has already approved");
        }

        super.setReadOnly(true);

        approvalMode = true;

    }

    public void onChgCurrency() throws Exception {
        policy.setDbCurrencyRate(
                CurrencyManager.getInstance().getRate(
                policy.getStCurrencyCode(),
                policy.getDtPolicyDate()));
    }

    public void onChangePolicyType() throws Exception {
        //policy.setStRateMethod(policy.getPolicyType().getStRateMethod());
        //policy.setStRateMethodDesc((String)FinCodec.RateScale.getLookUp().getValue(policy.getStRateMethod()));
    }

    public void onChangeRegion() throws Exception {
        if (policy.getStRegionID() != null) {

            chgCoverType();
            WebFinLOVRegistry.getInstance().setStCostC(policy.getStCostCenterCode());
            policy.setStCostCenterCode2(policy.getStCostCenterCode());

            policy.setStRateMethod(policy.getPolicyType().getStRateMethod());
            policy.setStRateMethodDesc((String) FinCodec.RateScale.getLookUp().getValue(policy.getStRateMethod()));
        }
    }

    public void chgCoverType() throws Exception {

        if (policy.getStRegionID() == null) {
            policy.setStCoverTypeCode(null);
            throw new RuntimeException("Region cannot be empty");
        }

        policy.setStCoverTypeCode("DIRECT");

    }

    public void btnSave() throws Exception {

        getRemoteInsurance().savePerjanjianKerjasama(policy, policy.getStNextStatus());

        close();
    }

    public void btnApproval() throws Exception {

        policy.setStEffectiveFlag("Y");
        policy.setStApprovedWho(SessionManager.getInstance().getSession().getStUserID());
        policy.setDtApprovedDate(new Date());

        policy.markUpdate();

        getRemoteInsurance().savePerjanjianKerjasama(policy, policy.getStNextStatus());

        close();
    }

    public void btnCancel() {
        close();
    }

    public void onChangePolicyTypeGroup() {
    }

    public void edit(String policyID) throws Exception {
        superEdit(policyID);
        if (!policy.isActive()) {
            throw new RuntimeException("This document is not editable because it is already a historical entry");
        }

        if (policy.isEffective()) {
            throw new Exception("This document is not editable, because it has already approved");
        }
    }

    public void changePeriodBase() {
        policy.calculatePeriods();
    }

    public void changePeriodFactor() {
        policy.calculatePeriods();
    }

    public void calcPeriods() {
        policy.calculatePeriods();
    }

    public void defaultPeriod() {
        policy.defaultPeriods();
    }

    public void chgRateClass() {
    }

    public void editCreateAdd(String arreqid) throws Exception {

        if (arreqid == null) {

            createNew();

            policy.setStStatus("ADD");

            return;
        }

        superEdit(arreqid);

//        logger.logDebug("############# " + policy.getStStatus());

//        if (policy.isStatusProposal()) {
//            if (!policy.isCashflowFlag()) {
//                throw new RuntimeException("Anggaran belum di-Cashflow Divisi Keuangan");
//            }
//        }
//
//        if (policy.isStatusRealized()) {
//            throw new RuntimeException("Anggaran Harus Status PROPOSAL");
//        }

        checkActiveEffective();

        logger.logDebug("############# parentid : " + policy.getStParentID());
        policy.checkLastPolicyNo(policy.getStParentID());

        policy.setStNextStatus("ADD");
        policy.setStActiveFlag("Y");
        policy.setStEffectiveFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        policy.setStPrintFlag(null);
        policy.setStReference1(null);

        setStPolicyNo(policy.getStPolicyNo());
        setStBankNo(policy.getStBankNo());
        setStFilePhysic(policy.getStFilePhysic());
        setDtPolicyDate(policy.getDtPolicyDate());
        setDtReceiveDate(policy.getDtReceiveDate());
        setDtPeriodStart(policy.getDtPeriodStart());
        setDtPeriodEnd(policy.getDtPeriodEnd());

        policy.setStPolicyNo(null);
        policy.setStBankNo(null);
        policy.setStFilePhysic(null);
        policy.setDtPolicyDate(null);
        policy.setDtReceiveDate(null);
        policy.setDtPeriodStart(null);
        policy.setDtPeriodEnd(null);

        policy.checkRealisasiNoBefore(policy.getStPolicyNo());
//        policy.generateNoRealisasi();

        policy.markUpdate();

        setTitle("PERJANJIAN KERJASAMA");
    }

    private void checkActiveEffective() {
        if (!policy.isActive()) {
            throw new RuntimeException("Data Tidak Aktif");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Permintaan Belum Disetujui");
        }
    }
    private String stPolicyNo;
    private String stBankNo;
    private String stFilePhysic;
    private Date dtPolicyDate;
    private Date dtReceiveDate;
    private Date dtPeriodStart;
    private Date dtPeriodEnd;

    /**
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    /**
     * @return the stBankNo
     */
    public String getStBankNo() {
        return stBankNo;
    }

    /**
     * @param stBankNo the stBankNo to set
     */
    public void setStBankNo(String stBankNo) {
        this.stBankNo = stBankNo;
    }

    /**
     * @return the dtPolicyDate
     */
    public Date getDtPolicyDate() {
        return dtPolicyDate;
    }

    /**
     * @param dtPolicyDate the dtPolicyDate to set
     */
    public void setDtPolicyDate(Date dtPolicyDate) {
        this.dtPolicyDate = dtPolicyDate;
    }

    /**
     * @return the dtReceiveDate
     */
    public Date getDtReceiveDate() {
        return dtReceiveDate;
    }

    /**
     * @param dtReceiveDate the dtReceiveDate to set
     */
    public void setDtReceiveDate(Date dtReceiveDate) {
        this.dtReceiveDate = dtReceiveDate;
    }

    /**
     * @return the dtPeriodStart
     */
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    /**
     * @param dtPeriodStart the dtPeriodStart to set
     */
    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    /**
     * @return the dtPeriodEnd
     */
    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    /**
     * @param dtPeriodEnd the dtPeriodEnd to set
     */
    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    /**
     * @return the approvalMode
     */
    public boolean isApprovalMode() {
        return approvalMode;
    }

    /**
     * @param approvalMode the approvalMode to set
     */
    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public void refresh() throws Exception {
    }

    public void close(String policyID) throws Exception {

        if (policyID == null) {
            throw new RuntimeException("Please select policy");
        }

        policy = getRemoteInsurance().getPerjanjianKerjasama(policyID);

        if (policy == null) {
            throw new RuntimeException("Policy not found");
        }

        if (!policy.isEffective()) {
            throw new Exception("PKS belum efektif");
        }

        super.setReadOnly(true);

        closeMode = true;

    }

    /**
     * @return the closeMode
     */
    public boolean isCloseMode() {
        return closeMode;
    }

    /**
     * @param closeMode the closeMode to set
     */
    public void setCloseMode(boolean closeMode) {
        this.closeMode = closeMode;
    }

    public void btnClose() throws Exception {

        policy.setStNonActFlag("Y");
        policy.setStStatus("CLOSE");
        policy.setStNonActWho(SessionManager.getInstance().getSession().getStUserID());
        policy.setDtNonActDate(new Date());

        policy.markUpdate();

        getRemoteInsurance().savePerjanjianKerjasama(policy, policy.getStNextStatus());

        close();
    }
}
