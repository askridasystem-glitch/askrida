/***********************************************************************
 * Module:  com.webfin.ar.forms.InvoiceForm
 * Author:  Denny Mahendra
 * Created: Jan 8, 2006 11:02:38 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.util.ListUtil;
import com.webfin.FinCodec;

import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.ar.model.ARTransactionTypeView;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.crux.util.SQLAssembler;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.LogManager;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyInwardDetailView;
import com.webfin.insurance.model.InsurancePolicyView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.util.Date;

public class InvoiceClaimForm extends Form {

    private InsurancePolicyInwardView invoice;
    private String parentTrxID;
    private DTOList list;
    private String stEntityID;
    private boolean cek = false;
    private boolean approvedMode;
    private boolean isClose = false;
    private String detailindex;
    private final static transient LogManager logger = LogManager.getInstance(InvoiceForm.class);
    private String stNextStatus;
    private boolean reverseMode = false;

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB", AccountReceivableHome.class.getName())).create();
    }

    public LOV getLovARTrxType() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select ar_trx_type_id,description from ar_trx_type where parent_trx_id = ?",
                new Object[]{parentTrxID},
                ARTransactionTypeView.class);
    }

    public DTOList getDetails() {
        return invoice.getDetails();
    }

    public InsurancePolicyInwardView getInvoice() {
        return invoice;
    }

    public void setInvoice(InsurancePolicyInwardView invoice) {
        this.invoice = invoice;
    }

    public InvoiceClaimForm() {
    }

    public void clickCancel() {
        setIsClose(true);
        close();
    }

    public void changeEntity() throws Exception {
        getList();
    }

    public void clickSave() throws Exception {

        if(!invoice.getStCurrencyCode().equalsIgnoreCase("IDR")){
            if(BDUtil.lesserThanEqual(invoice.getDbCurrencyRate(), BDUtil.one))
                throw new RuntimeException("Cek kurs mata uang apakah sudah diisi dengan benar");
        }

        if (invoice.isPLA()) {

            invoice.setDtMutationDate(invoice.getDtDueDate());
            invoice.setDtInvoiceDate(invoice.getDtPLADate());
            
            if (invoice.isApproved()) {
                invoice.setDtInvoiceDate(new Date());
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStApprovedFlag("Y");
                //invoice.setStPostedFlag("Y");
            }
            

        } else if (invoice.isDLA()) {

            invoice.setDtMutationDate(invoice.getDtDueDate());
            //invoice.setDtInvoiceDate(invoice.getDtDueDate());

            if (invoice.isApproved()) {
                invoice.setDtInvoiceDate(new Date());
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStApprovedFlag("Y");
                //invoice.setStPostedFlag("Y");
            } 
            
        }

        getRemoteAccountReceivable().saveClaimInward(invoice, stNextStatus);

        close();
    }

    public void clickSaveSuratHutang() throws Exception {
        invoice.setList(list);
        //invoice.markUpdate();
        getRemoteAccountReceivable().saveSuratHutangInward(invoice, list);
        close();
    }

    public void create() throws Exception {
        invoice = new InsurancePolicyInwardView();
        invoice.setDetails(new DTOList());
        invoice.markNew();

        invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        invoice.setDbCurrencyRate(new BigDecimal(1));
        invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        invoice.setStActiveFlag("Y");

    }

    public DTOList getList() throws Exception {
        //getDetails();
        if (list == null) {
            list = new DTOList();

            list.getFilter().activate();
        }

        final SQLAssembler sqa = new SQLAssembler();

        /*
         *select *
        from
        ar_invoice a,ent_master b,ins_policy c where b.ent_id = a.ent_id and a.attr_pol_no = c.pol_no
        and refid0 like 'REINS%' and upper(b.ent_id) like '200' and attr_pol_per_0>= and attr_pol_per_1<=
        order by a.create_date desc
         */

        sqa.addSelect("*");
        sqa.addQuery(
                "   from "
                + "      ar_invoice a,"
                + "         ent_master b,ins_policy c");
        sqa.addClause(" b.ent_id = a.ent_id and a.attr_pol_no = c.pol_no and refid0 like 'REINS%'");
        sqa.addOrder("a.create_date desc");

        if (invoice.getStEntityID() != null) {
            sqa.addClause("upper(b.ent_id) like ?");
            sqa.addPar(invoice.getStEntityID().toUpperCase());
        }

        if (invoice.getStNoSuratHutang() != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(invoice.getStNoSuratHutang().toUpperCase());
        }

//      sqa.addClause("c.policy_date>=?");
//      sqa.addPar(DateUtil.dateBracketLow(invoice.getDtSuratHutangPeriodFrom()));
//      
//      sqa.addClause("c.policy_date<=?");
//      sqa.addPar(DateUtil.dateBracketLow(invoice.getDtSuratHutangPeriodTo()));


        sqa.addFilter(list.getFilter());

        list = sqa.getList(InsurancePolicyInwardView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void createSuratHutang() throws Exception {
        invoice = new InsurancePolicyInwardView();
        invoice.setDetails(new DTOList());
        invoice.markNew();

        //invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        //invoice.setDbCurrencyRate(new BigDecimal(1));
    }

    public void edit(String invoiceid) throws Exception {
        view(invoiceid);

        if (invoice.isApproved()) {
            throw new RuntimeException("Data sudah disetujui, tidak bisa diubah");
        }

        invoice.markUpdate();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.markUpdate();
        }
    }

    public void editSuratHutang(String nosurathutang) throws Exception {
        viewSuratHutang(nosurathutang);

        cek = false;

        invoice.markUpdate();

        super.setReadOnly(false);

    }

    public void viewSuratHutang(String nosurathutang) throws Exception {
        invoice = getRemoteAccountReceivable().getSuratHutangInward(nosurathutang);

        super.setReadOnly(true);

        cek = true;

        //recalculate();
    }

    public void view(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);

        super.setReadOnly(true);

        //recalculate();
    }

    public void onNewItem() {
        final InsurancePolicyInwardDetailView det = new InsurancePolicyInwardDetailView();

        det.markNew();
        getDetails().add(det);
    }
    private String itemIndex;

    public String getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
        this.itemIndex = itemIndex;
    }

    public void onDeleteItem() {
        getDetails().delete(Integer.parseInt(itemIndex));
    }

    public void onChangeTRXType() {
        final ARTransactionTypeView trx = invoice.getARTrxType();

        if (trx == null) {
            return;
        }

        if (trx.isSuperType()) {
            parentTrxID = trx.getStARTrxTypeID();
            return;
        }

        final DTOList artrxitems = trx.getItems();

        invoice.setStInvoiceType(trx.getStInvoiceType());

        for (int i = 0; i < artrxitems.size(); i++) {
            ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

            final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

            //ivd.setStGLAccountCode(lt.getStGLAccount());
            ivd.setStARTrxLineID(lt.getStARTrxLineID());

            ivd.setStDescription(lt.getStItemDesc());
            ivd.setStNegativeFlag(lt.getStNegativeFlag());
            ivd.setStComissionFlag(lt.getStComissionFlag());

            ivd.markNew();

            invoice.getDetails().add(ivd);
        }

        boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17") || trx.getStARTrxTypeID().equalsIgnoreCase("18")
                || trx.getStARTrxTypeID().equalsIgnoreCase("19");
        if (isClaim) {
            invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        }

    }

    public LOV getLOV_Entity() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ent_id, ent_name from ent_master where ins_inward_flag = 'Y'");
    }

    public void recalculate() throws Exception {
        invoice.recalculateOld();
    }

    public void onChangeCurrency() throws Exception {
        invoice.setDbCurrencyRate(CurrencyManager.getInstance().getRate(invoice.getStCurrencyCode(), invoice.getDtInvoiceDate()));

        invoice.recalculateSaldoAwal();
    }

    public void afterUpdateForm() {
        if (invoice.getStARTransactionTypeID() != null) {
            //invoice.recalculateSaldoAwal();
            //if(!isClose)
            //invoice.recalculateOld();
        }

    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setCek(boolean cek) {
        this.cek = cek;
    }

    public boolean getCek() {
        return cek;
    }

    public String getStEntityName() {
        final EntityView entity = getEntity();

        if (entity != null) {
            return entity.getStEntityName();
        }

        return null;
    }

    private EntityView getEntity() {
        if (stEntityID == null) {
            return null;
        }
        return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
    }

    public boolean trxEnablePolType() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnablePolType();
    }

    public boolean trxEnablePolis() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnablePolis();
    }

    public boolean trxEnableUWrit() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnableUWrit();
    }

    public boolean trxEnableFixedItem() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnableFixedItem();
    }

    public boolean trxDisableDetail() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxDisableDetail();
    }

    public boolean trxEnableReins() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnableReins();
    }

    public void approve(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);

        //if(invoice.getStEffectiveFlag().equalsIgnoreCase("Y"))
        //throw new RuntimeException("Data sudah disetujui");

        super.setReadOnly(true);

        if (invoice.getStClaimStatus().equalsIgnoreCase("DLA")) {
            invoice.setStPostedFlag("Y");
        }

        invoice.setStApprovedFlag("Y");
        invoice.setStActiveFlag("Y");
        invoice.setStEffectiveFlag("Y");

        invoice.markUpdate();

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.markUpdate();
        }

        recalculate();
    }

    public boolean isApprovedMode() {
        return approvedMode;
    }

    public void setApprovedMode(boolean approvedMode) {
        this.approvedMode = approvedMode;
    }

    public boolean isIsClose() {
        return isClose;
    }

    public void setIsClose(boolean isClose) {
        this.isClose = isClose;
    }

    public void editCreateDLA(String invoiceid) throws Exception {
        view(invoiceid);

        if (!invoice.isPLA()) {
            throw new RuntimeException("LKP hanya bisa dibuat dari LKS");
        }

        if (!invoice.isApproved()) {
            throw new RuntimeException("Data belum disetujui, tidak bisa dibuat LKP");
        }

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.markNew();
        }

        invoice.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        invoice.setStPostedFlag(null);
        invoice.setStApprovedFlag(null);
        invoice.setStEffectiveFlag(null);

        stNextStatus = FinCodec.ClaimStatus.DLA;

    }

    public void recalculateInwardTreaty() throws Exception {
        invoice.recalculateInwardTreaty();
    }

    public void clickSaveInwardTreaty() throws Exception {
        recalculateInwardTreaty();
        getRemoteAccountReceivable().saveSaldoAwalInward(invoice);
        close();
    }

    public void onChangeTRXTypeInwardTreaty() {
        final ARTransactionTypeView trx = invoice.getARTrxType();

        if (trx == null) {
            return;
        }

        if (trx.isSuperType()) {
            parentTrxID = trx.getStARTrxTypeID();
            return;
        }

        final DTOList artrxitems = trx.getItems();

        invoice.setStInvoiceType(trx.getStInvoiceType());

        final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();

        ivdHeader.markNew();

        invoice.getDetails().add(ivdHeader);

        final InsurancePolicyInwardDetailView dtl = (InsurancePolicyInwardDetailView) invoice.getDetails().get(Integer.parseInt(itemIndex));

        for (int i = 0; i < artrxitems.size(); i++) {
            ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

            final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

            ivd.setStARTrxLineID(lt.getStARTrxLineID());

            ivd.setStDescription(lt.getStItemDesc());
            ivd.setStNegativeFlag(lt.getStNegativeFlag());
            ivd.setStComissionFlag(lt.getStComissionFlag());

            ivd.markNew();

            invoice.getDetails().add(ivd);
        }

        boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17") || trx.getStARTrxTypeID().equalsIgnoreCase("18")
                || trx.getStARTrxTypeID().equalsIgnoreCase("19");
        if (isClaim) {
            invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        }

    }

    public void setHeader() {
        final ARTransactionTypeView trx = invoice.getARTrxType();

        if (trx == null) {
            return;
        }

        invoice.setStInvoiceType(trx.getStInvoiceType());
    }

    public void onNewDetails() {

        final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();

        ivdHeader.markNew();

        invoice.getDetails().add(ivdHeader);

        itemIndex = String.valueOf(invoice.getDetails().size() - 1);

        onExpandDetails();
    }

    public void onExpandDetails() {

        final InsurancePolicyInwardDetailView dtl = (InsurancePolicyInwardDetailView) invoice.getDetails().get(Integer.parseInt(itemIndex));

        final ARTransactionTypeView trx = invoice.getARTrxType();
        final DTOList artrxitems = trx.getItems();

        for (int i = 0; i < artrxitems.size(); i++) {
            ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

            final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

            ivd.setStARTrxLineID(lt.getStARTrxLineID());

            ivd.setStDescription(lt.getStItemDesc());
            ivd.setStNegativeFlag(lt.getStNegativeFlag());
            ivd.setStComissionFlag(lt.getStComissionFlag());

            ivd.markNew();

            dtl.getDetails().add(ivd);
        }

    }

    public String getDetailindex() {
        return detailindex;
    }

    public void setDetailindex(String detailindex) {
        this.detailindex = detailindex;
    }

    public void editInwardTreaty(String invoiceid) throws Exception {
        viewInwardTreaty(invoiceid);

        invoice.markUpdate();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);

            ivd.markUpdate();

            final DTOList det = ivd.getDetails();

            for (int j = 0; j < det.size(); j++) {
                InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(j);

                logger.logDebug("+++++++++++++++++++ LOAD DETAIL ++++++++++++++++++++");
                logger.logDebug("detail : " + detail.getStARInvoiceDetailID());
                logger.logDebug("+++++++++++++++++++ LOAD DETAIL ++++++++++++++++++++");
                detail.markUpdate();
            }

            ivd.getDetails().markAllUpdate();

        }
    }

    public void viewInwardTreaty(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);

        super.setReadOnly(true);

        recalculateInwardTreaty();
    }

    public void changeBranch() throws Exception {
    }

    public boolean trxEnableXOL() {
        return invoice.getARTrxType() != null && invoice.getARTrxType().trxEnableXOL();
    }

    /**
     * @return the stNextStatus
     */
    public String getStNextStatus() {
        return stNextStatus;
    }

    /**
     * @param stNextStatus the stNextStatus to set
     */
    public void setStNextStatus(String stNextStatus) {
        this.stNextStatus = stNextStatus;
    }

    public void createPLAFromReins(String invoiceid) throws Exception {
        viewInward(invoiceid);

        if (!invoice.isApproved()) {
            throw new RuntimeException("Data belum disetujui, tidak bisa dibuat LKS");
        }

        invoice.markNew();
        invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        invoice.setStActiveFlag("Y");

        super.setReadOnly(false);


    }

    public void viewInward(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInwardTreatyOnly(invoiceid);

        invoice.setDetails(new DTOList());

        super.setReadOnly(true);

        //recalculate();
    }

    public void clickReverse() throws Exception {
        if (!invoice.isApproved()) {
            throw new Exception("Tidak bisa di Reverse karena belum disetujui");
        }

        invoice.validate3();

        //boolean canReverse = DateUtil.getDateStr(invoice.getDtMutationDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        //if (!canReverse) throw new Exception("Tidak bisa di Reverse sudah tutup produksi");

        getRemoteAccountReceivable().reverse(invoice);
        close();
    }

    public void refresh() throws Exception {
    }

    public void endorse(String invoiceid) throws Exception {

        view(invoiceid);

        if (!invoice.isApproved()) {
            throw new Exception("Data belum Disetujui");
        }

//        invoice.generateEndorseNo();
        invoice.setDtMutationDate(null);
        invoice.setStPostedFlag("N");
        invoice.setStApprovedFlag("N");
        invoice.setEndorseMode(true);

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            //ivd.setDbAmount(BDUtil.negate(ivd.getDbAmount()));
            //ivd.setDbEnteredAmount(BDUtil.negate(ivd.getDbEnteredAmount()));

            ivd.markNew();
        }
    }

    public void retrieveClaim() throws Exception {
        if (invoice.getStDLANo() == null) {
            throw new RuntimeException("Nomor DLA Tidak Boleh Kosong");
        }
        //if(policy.getDtPeriodStart()==null) throw new RuntimeException("Tanggal Periode Awal Tidak Boleh Kosong");
        downloadHistory();
    }

    public void downloadHistory() throws Exception {

        final DTOList abaPolicy = invoice.getInsPolicyClaim();

        if (abaPolicy.size() < 1) {
            throw new RuntimeException("Data Klaim Tidak Ditemukan");
        }

        doDeleteAllObjects();

        for (int i = 0; i < abaPolicy.size(); i++) {
            InsurancePolicyView pol = (InsurancePolicyView) abaPolicy.get(i);

            invoice.setDtDLADate(pol.getDtDLADate());
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());

            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtReference2());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtReference3());

            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmountEndorse());
            invoice.setDtReference2(pol.getDtClaimDate());
            invoice.setStReferenceX1(pol.getStClaimChronology());
        }
    }

    public void doDeleteAllObjects() throws Exception {
        invoice.setDtDLADate(null);
        invoice.setStAttrPolicyTypeID(null);
        invoice.setStCostCenterCode(null);

        invoice.setStAttrPolicyName(null);
        invoice.setStAttrPolicyAddress(null);
        invoice.setDtAttrPolicyPeriodStart(null);
        invoice.setDtAttrPolicyPeriodEnd(null);

        invoice.setDbAttrPolicyTSITotal(null);
        invoice.setDbAttrPolicyTSI(null);
        invoice.setDtReference2(null);
        invoice.setStReferenceX1(null);
    }

    public void retrieveClaimExgratia() throws Exception {
        if (invoice.getStDLANo() == null) {
            throw new RuntimeException("Nomor DLA Tidak Boleh Kosong");
        }
        //if(policy.getDtPeriodStart()==null) throw new RuntimeException("Tanggal Periode Awal Tidak Boleh Kosong");
        downloadExgratia();
    }

    public void downloadExgratia() throws Exception {

        final DTOList abaPolicy = invoice.getInsInwardExgratia();

        if (abaPolicy.size() < 1) {
            throw new RuntimeException("Data Klaim Tidak Ditemukan");
        }

        doDeleteAllObjects();

        for (int i = 0; i < abaPolicy.size(); i++) {
            InsurancePolicyInwardView pol = (InsurancePolicyInwardView) abaPolicy.get(i);

            invoice.setStInvoiceNo(pol.getStInvoiceNo());
            invoice.setDtDLADate(pol.getDtDLADate());
            invoice.setStAttrPolicyTypeID(pol.getStAttrPolicyTypeID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());

            invoice.setStAttrPolicyName(pol.getStAttrPolicyName());
            invoice.setStAttrPolicyAddress(pol.getStAttrPolicyAddress());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtAttrPolicyPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtAttrPolicyPeriodEnd());

            invoice.setDbAttrPolicyTSITotal(pol.getDbAttrPolicyTSITotal());
            invoice.setDbAttrPolicyTSI(pol.getDbAttrPolicyTSI());
            invoice.setDtReference2(pol.getDtReference2());
            invoice.setStReferenceX1(pol.getStReferenceX1());
        }
    }

    public void editNoSurat(String invoiceid) throws Exception {
        view(invoiceid);

        if (invoice.isApproved()) {
            //throw new RuntimeException("Data sudah disetujui, tidak bisa diubah");
        }

        invoice.markUpdate();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            //ivd.markUpdate();
        }
    }

}
