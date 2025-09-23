package com.webfin.acceptance.ejb;

import com.webfin.insurance.model.InsuranceClausulesView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsuranceTreatyView;
import com.webfin.insurance.filter.InsurancePolicyFilter;
import com.webfin.entity.filter.EntityFilter;
import com.crux.util.DTOList;
import com.crux.util.LOV;
import com.webfin.acceptance.model.AcceptanceView;
import com.webfin.ar.forms.ReceiptForm;
import com.webfin.datatext.model.DataTeksMasukLogView;
import com.webfin.insurance.model.CoverNoteView;
import com.webfin.insurance.filter.ZoneFilter;
import com.webfin.insurance.form.ProductionMarketingReportForm;
import com.webfin.insurance.form.ProductionReinsuranceReportForm;
import com.webfin.insurance.form.ProductionReportForm;
import com.webfin.insurance.form.ProductionUtilitiesReportForm;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsuranceZoneLimitView;
import com.webfin.insurance.model.InsurancePolicyParentView;
import com.webfin.insurance.model.InsuranceRiskCategoryView;
import com.webfin.insurance.model.UploadEndorseHeaderView;
import com.webfin.insurance.model.UploadHeaderProposalCommView;
import com.webfin.insurance.model.UploadHeaderReinsuranceView;
import com.webfin.insurance.model.UploadHeaderView;
import com.webfin.insurance.model.uploadProposalCommView;
import com.webfin.pks.model.PerjanjianKerjasamaView;
import java.io.FileInputStream;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Oct 9, 2005
 * Time: 9:59:08 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Acceptance extends EJBObject {

    AcceptanceView getInsurancePolicy(String stPolicyID)  throws Exception, RemoteException;
    
    DTOList listPolicies(InsurancePolicyFilter f)  throws Exception, RemoteException;
    
    DTOList getInsItemsList() throws Exception, RemoteException;
    
    void save(AcceptanceView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    LOV getInsuranceTypesLOV() throws Exception, RemoteException;
    
    DTOList searchAgents(EntityFilter f)  throws Exception, RemoteException;
    
    LOV getInsuranceSubTypesLOV(String stPolicyType)  throws Exception, RemoteException;
    
    DTOList getClausules(String stPolicyTypeID, String stPolicySubTypeID) throws Exception, RemoteException;
    
    LOV getBusinessSourceLOV() throws Exception, RemoteException;
    
    LOV getRegionLOV() throws Exception, RemoteException;
    
    DTOList getInsuranceItemLOV(String stCoverTypeCode) throws Exception, RemoteException;
    
    //InsurancePolicyView getInsurancePolicyForPrinting(String policyid, String alter) throws Exception, RemoteException;
    
    void reActivate(String parentPolicy) throws Exception, RemoteException;
    
    void saveAndReverse(InsurancePolicyView policy) throws Exception, RemoteException;
    
    void registerPrintSerial(InsurancePolicyView policy, String nom, String urx)  throws Exception, RemoteException;
    
    void save(InsuranceTreatyView tre) throws Exception, RemoteException;
    
    CoverNoteView getInsurancePolicy2(String stPolicyID)  throws Exception, RemoteException;
    
    DTOList searchZone(ZoneFilter f)  throws Exception, RemoteException;
    
    void reverse(InsurancePolicyView policy) throws Exception, RemoteException;
    
    void setID(String id) throws Exception,RemoteException;
    
    String getID() throws Exception,RemoteException;
    
    void postCoasByDate(Date dateFrom,Date dateTo) throws Exception,RemoteException;
    
    void saveABAProduk(InsurancePolicyView pol) throws Exception, RemoteException;
    
    void saveABAHutang(InsurancePolicyView pol) throws Exception, RemoteException;
    
    void saveABABayar(InsurancePolicyView pol) throws Exception, RemoteException;
    
    void saveABAPajak(InsurancePolicyView pol) throws Exception, RemoteException;
    
    void saveABAByDate(InsurancePolicyView pol) throws Exception,RemoteException;
    
    void saveAfterReverse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void save(InsuranceZoneLimitView zone) throws Exception, RemoteException;
    
    String savePolicyHistory(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    LOV LOV_Kabupaten() throws Exception, RemoteException;
    
    InsurancePolicyParentView getInsurancePolicyParent(String stPolicyID)  throws Exception, RemoteException;
    
    InsurancePolicyView getInsurancePolicyForPreview(String policyid) throws Exception, RemoteException;
    
    void saveInputPaymentDate(InsurancePolicyView pol, boolean approvalMode) throws Exception, RemoteException;
    
    void approveAfterReverse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void saveParent(InsurancePolicyParentView pol, String stNextStatus) throws Exception, RemoteException;

    
    void updateClaimRecap(DTOList data, ReceiptForm form) throws Exception,RemoteException;
    
    void deleteABA(InsurancePolicyView policy) throws Exception, RemoteException;
    
    InsurancePolicyView savePolicyOnly(InsurancePolicyView pol) throws Exception, RemoteException;
    
    String saveAndReturnPolicy(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void saveAndApprove(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    String saveAutoEndorsement(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void saveInvoiceClaimByDate(InsurancePolicyView pol) throws Exception,RemoteException;
    
    String saveEndorseBGSB(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void updateTransferProd(DTOList data, ProductionUtilitiesReportForm form) throws Exception,RemoteException;
    
    void updateTransferProduk(DTOList data, ProductionUtilitiesReportForm form) throws Exception,RemoteException;
    
    void updateTransferBayar1(DTOList data, ProductionReportForm form) throws Exception,RemoteException;
    
    void updateTransferHutang(DTOList data, ProductionReportForm form) throws Exception,RemoteException;
    
    void updateTransferPajak(DTOList data, ProductionReportForm form) throws Exception,RemoteException;
    
    void updateMonitoring(DTOList data, ReceiptForm form) throws Exception,RemoteException;

    void save(InsuranceRiskCategoryView risk) throws Exception, RemoteException;
    
    void save(InsuranceClausulesView clausules) throws Exception, RemoteException;
    
    PerjanjianKerjasamaView getPerjanjianKerjasama(String stPolicyID) throws Exception, RemoteException;
    
    void savePerjanjianKerjasama(PerjanjianKerjasamaView pol, String stNextStatus) throws Exception, RemoteException;

    void saveAutoEndorse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;
    
    void reverseReinsuranceOnly(InsurancePolicyView pol) throws Exception, RemoteException;

    //InsurancePolicyView getInsurancePolicyForPrintingWithDigitalSign(String policyid, String alter) throws Exception, RemoteException;

    void jurnalUlang(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;

    void reverseJurnalBalik(InsurancePolicyView pol) throws Exception, RemoteException;

    void reverseReinsuranceJurnalBalik(InsurancePolicyView pol) throws Exception, RemoteException;

    void saveReinsuranceAfterJurnalBalik(InsurancePolicyView pol) throws Exception, RemoteException;

    void saveReinsuranceOnly(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;

    void updateNoLPHKark(DTOList data, ProductionReinsuranceReportForm form) throws Exception,RemoteException;

    void updateValidateClaim(DTOList data) throws Exception, RemoteException;

    void saveUploadEndorsemen(UploadHeaderView header, DTOList l) throws Exception, RemoteException;

    DTOList getInsuranceUploadDetail(String stUploadID) throws Exception, RemoteException;

    void saveApproveUpload(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;

    void updateCancelValidateClaim(DTOList data) throws Exception, RemoteException;

    void updateCancelClaimRecap(DTOList data, ReceiptForm form) throws Exception,RemoteException;

    void saveDataTeks(DataTeksMasukView teks) throws Exception,RemoteException;

    void saveFromTeks(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception,RemoteException;

    void saveLKSToGateway(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception,RemoteException;

    void saveEndorseFromGateway(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;

    void saveDataTeksLog(DataTeksMasukLogView log) throws Exception, RemoteException;

    InsuranceClosingView getClosingForPrinting(String closingid) throws Exception, RemoteException;

    void saveUploadSpreading(UploadHeaderReinsuranceView header, DTOList l) throws Exception, RemoteException;

    DTOList getInsuranceUploadReins(String stUploadID) throws Exception, RemoteException;

    void saveUploadEndorsePolis(UploadEndorseHeaderView header, DTOList l) throws Exception, RemoteException;

    DTOList getUploadEndorseDetail(String stUploadID) throws Exception, RemoteException;

    void saveUploadProposal(UploadHeaderProposalCommView header, DTOList l) throws Exception, RemoteException;

    DTOList getInsuranceProposalComm(String stUploadID) throws Exception, RemoteException;

    uploadProposalCommView getProposalForPrinting(String rcid) throws Exception, RemoteException;

    void getProposalForPrintingExcel(String rcid) throws Exception, RemoteException;

    uploadProposalCommView getProposalForPrintingMix(String rcid) throws Exception, RemoteException;

}
