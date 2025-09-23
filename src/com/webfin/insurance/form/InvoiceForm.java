/***********************************************************************
 * Module:  com.webfin.ar.forms.InvoiceForm
 * Author:  Denny Mahendra
 * Created: Jan 8, 2006 11:02:38 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.common.controller.FormTab;
import com.crux.file.FileManager;
import com.crux.file.FileView;
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
import com.webfin.insurance.model.InsurancePolicyInwardInstallmentView;
import java.io.FileInputStream;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class InvoiceForm extends Form {

    private InsurancePolicyInwardView invoice;
    private String parentTrxID;
    private DTOList list;
    private String stEntityID;
    private boolean cek = false;
    private boolean approvedMode;
    private boolean isClose = false;
    private String detailindex;
    private boolean endorseMode = false;
    private FormTab tabs;
    private String instIndex;

    private boolean reverseMode;
    private boolean editMode;
    
    private final static transient LogManager logger = LogManager.getInstance(InvoiceForm.class);

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }
    
    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
        .create();
    }
    
    public LOV getLovARTrxType() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select ar_trx_type_id,description from ar_trx_type where parent_trx_id = ?",
                new Object [] {parentTrxID},
                ARTransactionTypeView.class
                );
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
    
    public InvoiceForm() {
    }
    
    public void clickCancel() {
        setIsClose(true);
        close();
    }
    
    public void changeEntity() throws Exception{
        getList();
    }
    
    public void clickSave() throws Exception {

        invoice.validate();

        if (!invoice.getStARTransactionTypeID().equalsIgnoreCase("22")) {
            invoice.validate2();
        }

        getRemoteAccountReceivable().saveSaldoAwalInward(invoice);

        //getRemoteAccountReceivable().saveSaldoAwalInwardPajakAcrual(invoice);

        close();
    }

    public void clickReverse() throws Exception {
        if (!invoice.isApproved()) {
            throw new Exception("Tidak bisa di Reverse karena belum disetujui");
        }

        if (!invoice.getStARTransactionTypeID().equalsIgnoreCase("22")) {
            invoice.validate2();
        }
        //boolean canReverse = DateUtil.getDateStr(invoice.getDtMutationDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        //if (!canReverse) throw new Exception("Tidak bisa di Reverse sudah tutup produksi");

        getRemoteAccountReceivable().reverse(invoice);
        close();
    }
    
    public void clickSaveSuratHutang() throws Exception {
        invoice.setList(list);
        //invoice.markUpdate();
        getRemoteAccountReceivable().saveSuratHutangInward(invoice,list);
        close();
    }
    
    public void create() throws Exception {
        invoice = new InsurancePolicyInwardView();
        invoice.setDetails(new DTOList());
        invoice.markNew();
        
        invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
        invoice.setDbCurrencyRate(new BigDecimal(1));
    }
    
    public DTOList getList() throws Exception {
        //getDetails();
        if (list==null) {
            list=new DTOList();
            
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
                "   from " +
                "      ar_invoice a," +
                "         ent_master b,ins_policy c");
        sqa.addClause(" b.ent_id = a.ent_id and a.attr_pol_no = c.pol_no and refid0 like 'REINS%'");
        sqa.addOrder("a.create_date desc");
        
        if (invoice.getStEntityID()!=null) {
            sqa.addClause("upper(b.ent_id) like ?");
            sqa.addPar(invoice.getStEntityID().toUpperCase());
        }
        
        if(invoice.getStNoSuratHutang()!=null){
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
        
        if (invoice.isApproved()) throw new Exception("Data Sudah Disetujui");
        
        invoice.markUpdate();
        
        super.setReadOnly(false);
        
        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);
            
            ivd.markUpdate();
        }

        for (int i = 0; i < invoice.getInstallment().size(); i++) {
            InsurancePolicyInwardInstallmentView inst = (InsurancePolicyInwardInstallmentView) invoice.getInstallment().get(i);

            inst.markUpdate();
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
        
        recalculate();
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
        
        if (trx==null) return;
        
        if (trx.isSuperType()) {
            parentTrxID = trx.getStARTrxTypeID();
            return;
        }
        
        final DTOList artrxitems = trx.getItems();
        
        //invoice.setStGLARAccountCode(trx.getStGLARAccount());
        //invoice.setStGLAPAccountCode(trx.getStGLAPAccount());
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
        
        boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17")||trx.getStARTrxTypeID().equalsIgnoreCase("18")
        ||trx.getStARTrxTypeID().equalsIgnoreCase("19");
        if(isClaim) invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        
    }
    
    public LOV getLOV_Entity() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ent_id, ent_name from ent_master where ins_inward_flag = 'Y'"
                );
    }
    
    public void recalculate() throws Exception {
        invoice.recalculateOld();

        invoice.reCalculateInstallment();
    }
    
    public void onChangeCurrency() throws Exception {
        invoice.setDbCurrencyRate(CurrencyManager.getInstance().getRate(invoice.getStCurrencyCode(), invoice.getDtMutationDate()));
        
        invoice.recalculateOld();
    }
    
    public void afterUpdateForm() {
        if(invoice.getStARTransactionTypeID()!=null){
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
        
        if (entity!=null) return entity.getStEntityName();
        
        return null;
    }
    
    private EntityView getEntity() {
        if (stEntityID==null) return null;
        return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
    }
    
    public boolean trxEnablePolType() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnablePolType();}
    public boolean trxEnablePolis() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnablePolis();}
    public boolean trxEnableUWrit() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableUWrit();}
    public boolean trxEnableFixedItem() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableFixedItem();}
    public boolean trxDisableDetail() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxDisableDetail();}
    public boolean trxEnableReins() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableReins();}
    
    public void approve(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);
        
        if (invoice.isApproved()) throw new Exception("Data Sudah Disetujui");
        
        super.setReadOnly(true);
        
        invoice.setStPostedFlag("Y");
        invoice.setStApprovedFlag("Y");
        
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
        
        if(!invoice.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA))
            throw new RuntimeException("DLA hanya bisa dibuat dari PLA");
        
        invoice.markNew();
        
        super.setReadOnly(false);
        
        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);
            
            ivd.markNew();
        }
        
        invoice.setStClaimStatus(FinCodec.ClaimStatus.DLA);
        invoice.setStPostedFlag(null);
        invoice.setStApprovedFlag(null);
        
    }
    
    public void recalculateInwardTreaty() throws Exception {
        invoice.recalculateInwardTreaty();
    }
    
    public void clickSaveInwardTreaty() throws Exception {

        invoice.validatePolicyType();

        invoice.validate();
        invoice.validate2();

        recalculateInwardTreaty();

        if (invoice.getStARTransactionTypeID().equalsIgnoreCase("1")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("2")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("3")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("20")) {
            if (invoice.isAP()) {
                throw new Exception("Format No Bukti Salah.");
            }
        } else if (invoice.getStARTransactionTypeID().equalsIgnoreCase("21")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("22")) {
            if (invoice.isAR()) {
                throw new Exception("Format No Bukti Salah.");
            }
        }

        getRemoteAccountReceivable().saveInwardTreaty(invoice);

        close();
    }
    
    public void onChangeTRXTypeInwardTreaty() {
        final ARTransactionTypeView trx = invoice.getARTrxType();
        
        if (trx==null) return;
        
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
        
        boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17")||trx.getStARTrxTypeID().equalsIgnoreCase("18")
        ||trx.getStARTrxTypeID().equalsIgnoreCase("19");
        if(isClaim) invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        
    }
    
    public void setHeader(){
        final ARTransactionTypeView trx = invoice.getARTrxType();
        
        if (trx==null) return;
        
        invoice.setStInvoiceType(trx.getStInvoiceType());
    }
    
    public void onNewDetails() {
        
        final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();
        
        ivdHeader.markNew();
        
        invoice.getDetails().add(ivdHeader);
        
        itemIndex = String.valueOf(invoice.getDetails().size()-1);
        
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
                logger.logDebug("detail : "+detail.getStARInvoiceDetailID());
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
    
    public void changeBranch() throws Exception{
        
    }
    
    public boolean trxEnableXOL() {
        return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableXOL();
    }

    public void copy(String invoiceid) throws Exception {

        view(invoiceid);

        invoice.setStInvoiceNo(null);
        invoice.setDtMutationDate(null);
        invoice.setStPostedFlag("N");
        invoice.setStApprovedFlag("N");
        invoice.setDbAttrPolicyTSI(null);
        invoice.setStInstallmentManualFlag(null);
        invoice.setStInstallmentOptions(null);
        invoice.setStInstallmentDaysAmount(null);
        invoice.setStInstallmentPeriodID("1");
        invoice.setStInstallmentPeriods(null);

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.setDbAmount(null);
            ivd.setDbEnteredAmount(null);

            ivd.markNew();
        }

        invoice.getInstallment().deleteAll();
        
    }

    public void endorse(String invoiceid) throws Exception {

        view(invoiceid);

        if (!invoice.isApproved()) throw new Exception("Data belum Disetujui");

        invoice.generateEndorseNo();
        invoice.setDtMutationDate(null);
        invoice.setStPostedFlag("N");
        invoice.setStApprovedFlag("N");
        invoice.setDbAttrPolicyTSITotal(BDUtil.negate(invoice.getDbAttrPolicyTSITotal()));
        invoice.setDbAttrPolicyTSI(BDUtil.negate(invoice.getDbAttrPolicyTSI()));
        invoice.setEndorseMode(true);

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.setDbAmount(BDUtil.negate(ivd.getDbAmount()));
            ivd.setDbEnteredAmount(BDUtil.negate(ivd.getDbEnteredAmount()));

            ivd.markNew();
        }
    }

    /**
     * @return the endorseMode
     */
    public boolean isEndorseMode() {
        return endorseMode;
    }

    /**
     * @param endorseMode the endorseMode to set
     */
    public void setEndorseMode(boolean endorseMode) {
        this.endorseMode = endorseMode;
    }

    public FormTab getTabs()
  {
    if (this.tabs == null)
    {
      this.tabs = new FormTab();

      this.tabs.add(new FormTab.TabBean("TAB1", "ITEM", true));
      this.tabs.add(new FormTab.TabBean("TAB2", "PEMBAYARAN", true));

      this.tabs.setActiveTab("TAB1");
    }
    return this.tabs;
  }

  public void setTabs(FormTab tabs)
  {
    this.tabs = tabs;
  }

  public DTOList getInstallment() throws Exception {
        return invoice.getInstallment();
    }

  public String getInstIndex() {
        return instIndex;
    }

    public void setInstIndex(String instIndex) {
        this.instIndex = instIndex;
    }

    public void onNewInstallment() throws Exception {
        final InsurancePolicyInwardInstallmentView inst = new InsurancePolicyInwardInstallmentView();

        inst.markNew();

        getInstallment().add(inst);

        String n = String.valueOf(getInstallment().size());

        invoice.setStInstallmentPeriods(n);

        invoice.reCalculateInstallment();
    }

    public void onDeleteInstallment() throws Exception{
        getInstallment().delete(Integer.parseInt(instIndex));

        String n = String.valueOf(getInstallment().size());

        invoice.setStInstallmentPeriods(n);

        invoice.reCalculateInstallment();
    }

    public DTOList getInvoices() throws Exception {
        return invoice.getInvoices();
    }

    public void uploadExcel() throws Exception {

        String fileID = invoice.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetInward = wb.getSheet("FACULTATIVE");

        int rows = sheetInward.getPhysicalNumberOfRows();

        for (int r = 4; r <= rows; r++) {
            HSSFRow row = sheetInward.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            //if(cellControl==null) break;

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();

            if (control.equalsIgnoreCase("END")) {
                break;
            }


            HSSFCell cellNomorTrx = row.getCell(1);//nomor trx
            HSSFCell cellEntityID = row.getCell(2);//entity id
            HSSFCell cellReinsuredID = row.getCell(3);//reinsured id
            HSSFCell cellJenisPolis = row.getCell(20);//jenis polis
            HSSFCell cellCcy = row.getCell(21);// ccy
            HSSFCell cellCcyRate = row.getCell(6);// ccy

            HSSFCell cellPremiBruto = row.getCell(7);// premi bruto
            HSSFCell cellKomisi = row.getCell(8);// komisi
            HSSFCell cellFee = row.getCell(9);// fee
            HSSFCell cellPremiNetto = row.getCell(10);// premi netto

            HSSFCell cellDeskripsi = row.getCell(11);// deskripsi
            HSSFCell cellPolicyNumber = row.getCell(12);// pol number
            HSSFCell cellPeriodStart = row.getCell(13);// period start
            HSSFCell cellPeriodEnd = row.getCell(14);// period end
            HSSFCell cellRiskName = row.getCell(15);// risk name
            HSSFCell cellRiskAddress = row.getCell(16);// risk address

            HSSFCell cellTotalTSI = row.getCell(17);// total tsi
            HSSFCell cellShareAskrida = row.getCell(18);// tsi askrida



            if(cellNomorTrx==null)
                throw new RuntimeException("Kolom nomor transaksi harus diisi");

            if(cellEntityID==null)
                throw new RuntimeException("Kolom entity id harus diisi");

            if(cellJenisPolis==null)
                throw new RuntimeException("Kolom jenis polis harus diisi");

            if(cellPremiBruto==null)
                throw new RuntimeException("Kolom premi bruto harus diisi");


            if(BDUtil.isZeroOrNull(new BigDecimal(cellCcyRate.getNumericCellValue())))
                    throw new RuntimeException("Data no transaksi "+ cellNomorTrx.getStringCellValue()+" salah, rate kurs kosong/nol");


            onNewDetailsExcel();

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

                ivd.setStEntityID(cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString());
                ivd.setStReinsuranceEntityID(cellReinsuredID.getCellType() == cellReinsuredID.CELL_TYPE_STRING ? cellReinsuredID.getStringCellValue() : new BigDecimal(cellReinsuredID.getNumericCellValue()).toString());

                ivd.setStAttrPolicyTypeID(cellJenisPolis.getCellType() == cellJenisPolis.CELL_TYPE_STRING ? cellJenisPolis.getStringCellValue() : new BigDecimal(cellJenisPolis.getNumericCellValue()).toString());
                ivd.setStCurrencyCode(cellCcy.getStringCellValue());


                ivd.setDbCurrencyRate(new BigDecimal(cellCcyRate.getNumericCellValue()));
                ivd.setStTransactionNo(cellNomorTrx.getStringCellValue());
                ivd.setStDeskripsi(cellDeskripsi.getStringCellValue());
                ivd.setStAttrPolicyNo(cellPolicyNumber.getStringCellValue());
                ivd.setDtAttrPolicyPeriodStart(cellPeriodStart.getDateCellValue());
                ivd.setDtAttrPolicyPeriodEnd(cellPeriodEnd.getDateCellValue());
                ivd.setStAttrPolicyName(cellRiskName.getStringCellValue());
                ivd.setStAttrPolicyAddress(cellRiskAddress.getStringCellValue());
                ivd.setDbAttrPolicyTSITotal(new BigDecimal(cellTotalTSI.getNumericCellValue()));
                ivd.setDbAttrPolicyTSI(new BigDecimal(cellShareAskrida.getNumericCellValue()));


                if(lt.getStARTrxLineID().equalsIgnoreCase("1")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellPremiBruto.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("2")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellKomisi.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("3")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellFee.getNumericCellValue()));
                }

                ivd.markNew();

                dtl.getDetails().add(ivd);
            }

        }

    }

    public void onNewDetailsExcel() {

        final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();

        //ivdHeader.markNew();

        invoice.getDetails().add(ivdHeader);

        itemIndex = String.valueOf(invoice.getDetails().size()-1);

        //onExpandDetails();
    }

    public void recalculateInwardFacultativeUpload() throws Exception {
        invoice.recalculateInwardTreatyUpload();
    }

    public void clickSaveInwardFacultativeUpload() throws Exception {

        invoice.validate2();

        recalculateInwardFacultativeUpload();

        if (invoice.getStARTransactionTypeID().equalsIgnoreCase("1")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("2")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("3")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("20")) {
            if (invoice.isAP()) {
                throw new Exception("Format No Bukti Salah.");
            }
        } else if (invoice.getStARTransactionTypeID().equalsIgnoreCase("21")
                || invoice.getStARTransactionTypeID().equalsIgnoreCase("22")) {
            if (invoice.isAR()) {
                throw new Exception("Format No Bukti Salah.");
            }
        }

        final DTOList details = invoice.getDetails();

        for (int i = 0; i < details.size(); i++) {
            InsurancePolicyInwardDetailView det = (InsurancePolicyInwardDetailView) details.get(i);

            //tambah header
            InsurancePolicyInwardView invoiceSave = new InsurancePolicyInwardView();
            invoiceSave = (InsurancePolicyInwardView) invoice;
            invoiceSave.setDetails(new DTOList());
            invoiceSave.markNew();

            //tambah detil
            InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();
            ivdHeader = (InsurancePolicyInwardDetailView) det;

            //ivdHeader.markNew();

            invoiceSave.getDetails().add(ivdHeader);
            invoiceSave.getDetails().markAllNew();

            final DTOList rincian = invoiceSave.getDetails();

            for (int j = 0; j < rincian.size(); j++) {
                InsurancePolicyInwardDetailView rinci = (InsurancePolicyInwardDetailView) rincian.get(j);

                final DTOList subDetails = rinci.getDetails();

                for (int k = 0; k < subDetails.size(); k++) {
                    InsurancePolicyInwardDetailView sub = (InsurancePolicyInwardDetailView) subDetails.get(k);

                    if(sub.getStEntityID()!=null)
                        invoiceSave.setStEntityID(sub.getStEntityID());

                    if(sub.getStReinsuranceEntityID()!=null)
                        invoiceSave.setStReinsuranceEntityID(sub.getStReinsuranceEntityID());

                    invoice.setStCurrencyCode(sub.getStCurrencyCode());
                    invoice.setDbCurrencyRate(sub.getDbCurrencyRate());
                    invoice.setStTransactionNoReference(sub.getStTransactionNo());
                    invoice.setStDescription(sub.getStDeskripsi());
                    invoice.setStAttrPolicyNo(sub.getStAttrPolicyNo());
                    invoice.setDtAttrPolicyPeriodStart(sub.getDtAttrPolicyPeriodStart());
                    invoice.setDtAttrPolicyPeriodEnd(sub.getDtAttrPolicyPeriodEnd());
                    invoice.setStAttrPolicyName(sub.getStAttrPolicyName());
                    invoice.setStAttrPolicyAddress(sub.getStAttrPolicyAddress());
                    invoice.setDbAttrPolicyTSITotal(sub.getDbAttrPolicyTSITotal());
                    invoice.setDbAttrPolicyTSI(sub.getDbAttrPolicyTSI());
                    invoice.setStAttrPolicyTypeID(sub.getStAttrPolicyTypeID());
                    invoice.setStEntityID(sub.getStEntityID());
                    invoice.setStReinsuranceEntityID(sub.getStReinsuranceEntityID());
                }

            }

            invoiceSave.recalculateOldUpload(false);

            getRemoteAccountReceivable().saveSaldoAwalInwardUpload(invoiceSave);

        }

            close();

    }

    public boolean trxEnableReinsured() {
        return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableReinsured();
    }



}
