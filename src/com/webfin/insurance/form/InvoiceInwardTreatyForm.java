/***********************************************************************
 * Module:  com.webfin.ar.forms.InvoiceForm
 * Author:  Denny Mahendra
 * Created: Jan 8, 2006 11:02:38 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.util.ListUtil;
import com.webfin.FinCodec;
//import com.webfin.ar.model.InsurancePolicyInwardView;
//import com.webfin.ar.model.InsurancePolicyInwardDetailView;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.ar.model.ARTransactionTypeView;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.crux.util.SQLAssembler;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DateUtil;
import com.crux.util.LogManager;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyInwardDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.FileInputStream;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.joda.time.DateTime;

public class InvoiceInwardTreatyForm extends Form {
    private InsurancePolicyInwardView invoice;
    private String parentTrxID;
    private DTOList list;
    private String stEntityID;
    private boolean cek = false;
    private boolean approvedMode;
    private boolean isClose = false;
    private String detailindex;
    
    private final static transient LogManager logger = LogManager.getInstance(InvoiceForm.class);
    
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
        return invoice.getDetailsInwardTreaty();
    }
    
    public InsurancePolicyInwardView getInvoice() {
        return invoice;
    }
    
    public void setInvoice(InsurancePolicyInwardView invoice) {
        this.invoice = invoice;
    }
    
    public InvoiceInwardTreatyForm() {
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

        getRemoteAccountReceivable().saveSaldoAwalInward(invoice);

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
                "      ins_pol_inward a," +
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
    }
    
    public void onChangeCurrency() throws Exception {
        invoice.setDbCurrencyRate(CurrencyManager.getInstance().getRate(invoice.getStCurrencyCode(), invoice.getDtInvoiceDate()));
        
        //invoice.recalculateSaldoAwal();
        invoice.recalculateInwardTreaty();
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
        invoice = getRemoteAccountReceivable().getARInvoiceInwardTreaty(invoiceid);
            
        if (invoice.isApproved()) throw new Exception("Data Sudah Disetujui");
        
        super.setReadOnly(true);
        
        invoice.setStPostedFlag("Y");
        invoice.setStApprovedFlag("Y");
        
        invoice.markUpdate();
        
        for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);
            
            ivd.markUpdate();
        }
        
        recalculateInwardTreaty();
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

    public void clickReverse() throws Exception {
        if (!invoice.isApproved()) {
            throw new Exception("Tidak bisa di Reverse karena belum disetujui");
        }

        invoice.validate2();

        getRemoteAccountReceivable().reverse(invoice);
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
            
        if (invoice.isApproved()) throw new Exception("Data Sudah Disetujui");
        
        invoice.markUpdate();
        
        super.setReadOnly(false);
        
        for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);
            
            ivd.markUpdate();
            
            final DTOList det = ivd.getDetails();
            
            for (int j = 0; j < det.size(); j++) {
                InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(j);
                
                detail.markUpdate();
            }
            
            ivd.getDetails().markAllUpdate();
            
        }
    }
    
    public void viewInwardTreaty(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInwardTreaty(invoiceid);
        
        super.setReadOnly(true);
        
        recalculateInwardTreaty();
    }

    public void copyInwardTreaty(String invoiceid) throws Exception {
        viewInwardTreaty(invoiceid);

        //if (invoice.isApproved()) throw new Exception("Data Sudah Disetujui");

        invoice.setStInvoiceNo(null);
        invoice.setDtMutationDate(null);
        invoice.setStPostedFlag("N");
        invoice.setStApprovedFlag("N");
        invoice.setDbAttrPolicyTSI(null);

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);

            ivd.markNew();

            final DTOList det = ivd.getDetails();

            for (int j = 0; j < det.size(); j++) {
                InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(j);

                detail.setDbAmount(null);
                detail.setDbEnteredAmount(null);

                detail.markNew();
            }

            ivd.getDetails().markAllNew();

        }
    }

    public void endorseInwardTreaty(String invoiceid) throws Exception {
        viewInwardTreaty(invoiceid);

        if (!invoice.isApproved()) throw new Exception("Data belum Disetujui");

        invoice.generateEndorseNo();
        invoice.setDtMutationDate(null);
        invoice.setStPostedFlag("N");
        invoice.setStApprovedFlag("N");
        invoice.setDbAttrPolicyTSI(null);
        invoice.setEndorseMode(true);

        invoice.markNew();

        super.setReadOnly(false);

        for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);

            ivd.markNew();

            final DTOList det = ivd.getDetails();

            for (int j = 0; j < det.size(); j++) {
                InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(j);

                detail.setDbAmount(BDUtil.negate(detail.getDbAmount()));
                detail.setDbEnteredAmount(BDUtil.negate(detail.getDbEnteredAmount()));

                detail.markNew();
            }
 
            ivd.getDetails().markAllNew();

        }
    }

    public void uploadExcel() throws Exception {

        String fileID = invoice.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetInward = wb.getSheet("INWARD");

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

            HSSFCell cellJenisPolis = row.getCell(26);//jenis polis
            HSSFCell cellUWYear = row.getCell(5);// u/w year
            HSSFCell cellTreaty = row.getCell(27);// u/w year
            HSSFCell cellCcy = row.getCell(28);// ccy
            HSSFCell cellCcyRate = row.getCell(10);// ccy
            HSSFCell cellPremiBruto = row.getCell(11);// premi bruto
            HSSFCell cellBrokerfee = row.getCell(12);// brokerfee
            HSSFCell cellPPN = row.getCell(13);// ppn
            HSSFCell cellpph = row.getCell(14);// pph
            HSSFCell cellKomisi = row.getCell(15);// komisi
            HSSFCell cellKlaim = row.getCell(16);// klaim
            HSSFCell cellRecoveries = row.getCell(17);// recoveries
            HSSFCell cellFee = row.getCell(18);// fee
            HSSFCell cellHFee = row.getCell(19);// hfee
            HSSFCell cellManagementFee = row.getCell(20);// mgmt fee
            HSSFCell cellOperationalFee = row.getCell(21);// operational fee
            HSSFCell cellGuaranteeFund = row.getCell(22);// guarantee fund
            HSSFCell cellPremiNetto = row.getCell(23);// premi netto
            HSSFCell cellDeskripsi = row.getCell(24);// premi netto
            HSSFCell cellQuartal = row.getCell(29);// quartal
            HSSFCell cellQuartalYear = row.getCell(7);// quartal year

            if(cellNomorTrx==null)
                throw new RuntimeException("Kolom nomor transaksi harus diisi");

            if(cellEntityID==null)
                throw new RuntimeException("Kolom entity id harus diisi");

            if(cellJenisPolis==null)
                throw new RuntimeException("Kolom jenis polis harus diisi");

            if(cellUWYear==null)
                throw new RuntimeException("Kolom UW Year harus diisi");

            if(cellTreaty==null)
                throw new RuntimeException("Kolom treaty harus diisi");

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
                ivd.setStAttrUnderwriting(cellUWYear.getCellType() == cellUWYear.CELL_TYPE_STRING ? cellUWYear.getStringCellValue() : new BigDecimal(cellUWYear.getNumericCellValue()).toString());
                ivd.setStRefID0(cellTreaty.getStringCellValue());
                ivd.setStCurrencyCode(cellCcy.getStringCellValue());

                
                ivd.setDbCurrencyRate(new BigDecimal(cellCcyRate.getNumericCellValue()));
                ivd.setStTransactionNo(cellNomorTrx.getStringCellValue());
                ivd.setStDeskripsi(cellDeskripsi.getStringCellValue());
                ivd.setStAttrQuartal(cellQuartal.getStringCellValue());
                ivd.setStAttrQuartalYear(cellQuartalYear.getCellType() == cellQuartalYear.CELL_TYPE_STRING ? cellQuartalYear.getStringCellValue() : new BigDecimal(cellQuartalYear.getNumericCellValue()).toString());

                if(lt.getStARTrxLineID().equalsIgnoreCase("4")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellPremiBruto.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("127")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellBrokerfee.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("150")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellPPN.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("151")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellpph.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("5")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellKomisi.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("6")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellKlaim.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("128")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellRecoveries.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("152")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellFee.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("153")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellHFee.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("154")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellManagementFee.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("155")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellOperationalFee.getNumericCellValue()));
                }

                if(lt.getStARTrxLineID().equalsIgnoreCase("156")){
                    ivd.setDbEnteredAmount(new BigDecimal(cellGuaranteeFund.getNumericCellValue()));
                }

                ivd.markNew();

                dtl.getDetails().add(ivd);
            }

        }

    }

    public void onNewDetailsExcel() {

        final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();

        ivdHeader.markNew();

        invoice.getDetails().add(ivdHeader);

        itemIndex = String.valueOf(invoice.getDetails().size()-1);

        //onExpandDetails();
    }

    public void onExpandDetailsExcel() {

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

    public void recalculateInwardTreatyUpload() throws Exception {
        invoice.recalculateInwardTreatyUpload();
    }

    public void clickSaveInwardTreatyUpload() throws Exception {

        invoice.validate2();

        recalculateInwardTreatyUpload();

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

            //SET DOKUMEN
            invoiceSave.setStAttachment1(invoice.getStAttachment1());
            invoiceSave.setStAttachment2(invoice.getStAttachment2());
            invoiceSave.setStAttachment3(invoice.getStAttachment3());

            invoiceSave.setDetails(new DTOList());
            invoiceSave.markNew();

            //tambah detil
            InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();
            ivdHeader = (InsurancePolicyInwardDetailView) det;

            ivdHeader.markNew();

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
                    invoice.setStAttrQuartal(sub.getStAttrQuartal());
                    invoice.setStAttrQuartalYear(sub.getStAttrQuartalYear());
                    
                }

            }

            invoiceSave.recalculateInwardTreaty();

            getRemoteAccountReceivable().saveInwardTreaty(invoiceSave);

        }

            close();

    }

    public DTOList getInvoices() throws Exception {
        return invoice.getInvoices();
    }

    public boolean trxEnableReinsured() {
        return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableReinsured();
    }
    
}
