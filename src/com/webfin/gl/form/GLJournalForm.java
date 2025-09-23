/***********************************************************************
 * Module:  com.webfin.gl.form.GLJournalForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.gl.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.AccountMarketingView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.JournalHeaderView;
import com.webfin.gl.model.JournalSyariahView;
import com.webfin.gl.model.RKAPGroupView;
import com.webfin.gl.util.GLUtil;

import com.webfin.insurance.ejb.*;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class GLJournalForm extends Form {

    //private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
    private JournalHeaderView titipan;
    private String notesindex;
    private String glindex;
    private String stGLSelect;
    private String invoicesindex;
    private String invoicecomissionindex;
    private String parinvoiceid;
    public boolean approveMode;
    public boolean voidMode;
    public boolean editMode;
    public boolean viewMode;
    private String nosurathutang;
    private String cc_code;
    private String artitipanid;
    private String stReportType;
    public DTOList objects;
    public String objectindex;
    private boolean drillMode = false;
    private BigDecimal dbTotalTitipan;
    private String years;

    public JournalHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(JournalHeaderView titipan) {
        this.titipan = titipan;
    }

    public boolean isDrillMode() {
        return drillMode;
    }

    public void setDrillMode(boolean drillMode) {
        this.drillMode = drillMode;
    }

    public void drillToggle() {
        drillMode = !drillMode;
    }

    public void btnPrint() throws Exception {
    }

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public String getStGLSelect() {
        return stGLSelect;
    }

    public void setStGLSelect(String stGLSelect) {
        this.stGLSelect = stGLSelect;
    }

    public String getGlindex() {
        return glindex;
    }

    public void setGlindex(String glindex) {
        this.glindex = glindex;
    }

    public String getInvoicecomissionindex() {
        return invoicecomissionindex;
    }

    public void setInvoicecomissionindex(String invoicecomissionindex) {
        this.invoicecomissionindex = invoicecomissionindex;
    }

    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB", AccountReceivableHome.class.getName())).create();
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public String getParinvoiceid() {
        return parinvoiceid;
    }

    public void setParinvoiceid(String parinvoiceid) {
        this.parinvoiceid = parinvoiceid;
    }

    public String getArtitipanid() {
        return artitipanid;
    }

    public void setArtitipanid(String artitipanid) {
        this.artitipanid = artitipanid;
    }

    public String getNosurathutang() {
        return nosurathutang;
    }

    public void setNosurathutang(String nosurathutang) {
        this.nosurathutang = nosurathutang;
    }

    public String getCcCode() {
        return cc_code;
    }

    public void setCcCode(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getNotesindex() {
        return notesindex;
    }

    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public String getInvoicesindex() {
        return invoicesindex;
    }

    public void setInvoicesindex(String invoicesindex) {
        this.invoicesindex = invoicesindex;
    }

    public DTOList getDetails() throws Exception {
        return titipan.getDetails();
    }

    public GLJournalForm() {
    }

    public void onNewNoteItem() {
    }
    /*
    public void onDeleteNoteItem() throws Exception {
    getListNotes().delete(Integer.parseInt(notesindex));

    recalculate();
    }

    private void recalculate() throws Exception {
    titipan.recalculate();

    titipan.setStARTitipanID("01");
    }

    public void RecalculatePajak() throws Exception {
    titipan.recalculate2();

    titipan.setStARTitipanID("01");
    }

    public void onDeleteGLItem() throws Exception {
    getListGLs().delete(Integer.parseInt(glindex));
    recalculate();
    }

    public void onNewInvoiceItem() {

    }

    public void onDeleteInvoiceItem() throws Exception {
    getListInvoices().delete(Integer.parseInt(invoicesindex));
    recalculate();
    }

    public void onDeleteInvoiceComissionItem() throws Exception {
    final ARtitipanLinesView iv = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    iv.getDetails().delete(Integer.parseInt(invoicecomissionindex));

    recalculate();
    }

    public void onShrinkInvoiceItem() {


    }

    public void onChangeCust() {

    }

    public void onExpandInvoiceItemSuratHutang(String nosurathutang) throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    rl.setStNosurathutang(nosurathutang);

    final ARInvoiceView invoice = rl.getInvoiceBySuratHutang();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    if (!d.isComission()) continue;

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }
    titipan.recalculate();
    }
     */
    /*
    public void onExpandInvoiceItem() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    /* buat munculin detail premi bruto
    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);
    if(d.getStRefID0()!=null){
    if (!d.getStRefID0().startsWith("PREMIG")) continue;

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    //rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }


    }*/

    /*
    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    //if (!d.isComission()) continue; tampilin semua

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
    rcl.setStARTrxLineID(d.getStARTrxLineID());


    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    rcl.markAsComission();
    rcl.markCommit();


    rl.getDetails().add(rcl);
    }


    titipan.recalculate();
    }


    public void onExpandInvoiceItem() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());

    if(d.isPremiGross2()||d.isCommission2()||d.isBrokerage2()
    ||d.isHandlingFee2()){
    rcl.setStLock(false);
    }

    if (d.isComission()) rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }
    titipan.recalculate();
    }

    public void onExpandInvoiceItemTitipan() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());

    if(d.isPremiGross2()||d.isCommission2()||d.isBrokerage2()
    ||d.isHandlingFee2()){
    rcl.setStLock(false);
    }

    if (d.isComission()) rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }

    DTOList objectsPolicy = invoice.getPolicyObjects();


    for(int i=0;i<objectsPolicy.size();i++){
    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objectsPolicy.get(i);

    obj.setDbObjectPremiSettled(obj.getDbObjectPremiTotalAmount());
    obj.setStPremiSettledFlag("Y");
    }
    objectsPolicy.markAllUpdate();
    rl.setObjects(objectsPolicy);

    titipan.recalculate();
    }

     */
    /*
    public void createNew() throws Exception {
    titipan = new JournalMemorialHeaderView();
    titipan.markNew();

    final DTOList details = new DTOList();

    final TitipanPremiView jv = new TitipanPremiView();

    jv.markNew();

    details.add(jv);

    titipan.setDetails(details);

    titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

    setTitle("Input Titipan Premi");

    editMode = true;
    }

    public void edit(String titipanID) throws Exception {
    logger.logDebug("++++++++++++++ titipanID : " + titipanID);
    masterEdit(titipanID);

    //if (titipan.isPosted()) throw new RuntimeException("titipan not editable (Posted)");
    //if (titipan.isCancel()) throw new RuntimeException("titipan not editable (VOID)");

    editMode = true;
    }

    public void superEdit(String titipanID) throws Exception {
    artitipanid = titipanID;

    masterEdit(titipanID);

    editMode = true;
    }
     */
    public void masterEdit(String titipanID) throws Exception {


        final DTOList details = getRemoteGeneralLedger().getJournalEntry(getTitipanPremi(titipanID).getStTransactionHeaderID());

        titipan = new JournalHeaderView();

        titipan.markUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

        JournalView titip = (JournalView) details.get(0);

        titipan.setStTransactionNo(titip.getStTransactionNo());
        titipan.setStCostCenter(titip.getStTransactionNo().substring(5, 7));
        titipan.setStMonths(titip.getStMonths());
        titipan.setStYears(titip.getStYears());
        titipan.setStMethodCode("F");
        titipan.setStAccountID(titip.getLgAccountID().toString());
        //titipan.setStAccountNoMaster(titip.getStHeaderAccountNo());
        //titipan.setStDescriptionMaster(titip.getStDescriptionMaster());


    }

    public void view(String titipanID) throws Exception {

        super.setReadOnly(true);

        viewMode = true;
    }

    public void doSave() throws Exception {

        getRemoteGeneralLedger().saveJournalMemorial(titipan, titipan.getDetails());

        super.close();

    }

    /*
    public void doRecalculate() throws Exception {
    //titipan.recalculate();

    BigDecimal total = BDUtil.zero;

    final DTOList details = getDetails();
    for (int i = 0; i < details.size(); i++) {
    JournalView titip = (JournalView) details.get(i);

    titip.setStAccountIDMaster(titipan.getStAccountIDMaster());
    titip.setStAccountNoMaster(titipan.getStAccountNoMaster());
    titip.setStMonths(titipan.getStMonths());
    titip.setStCostCenter(titipan.getStCostCenter());

    total = BDUtil.add(total, titip.getDbAmount());

    }
    dbTotalTitipan = total;
    }
     */
    public void doCancel() {
        super.close();
    }

    public void doClose() {
        super.close();
    }

    public void afterUpdateForm() {
        try {
            //titipan.recalculate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void chgCurrency() throws Exception {
        //titipan.setDbCurrencyRate(CurrencyManager.getInstance().getRate(titipan.getStCurrencyCode(), titipan.getDttitipanDate()));
    }

    public void changeBankType() {
        //titipan.setStInvoiceType(titipan.gettitipanClass().getStInvoiceType());
    }

    public void changeCostCenter() {
    }

    public void changemethod() {
    }

    public void generatRNo() throws Exception {
        //titipan.generatetitipanNo();
    }

    public void approve(String titipanID) throws Exception {
        masterEdit(titipanID);

        //if (!titipan.isPosted() && !titipan.isCancel()); else throw new RuntimeException("Invalid State");
        //titipan.setStPostedFlag("Y");

        super.setReadOnly(true);
        approveMode = true;
    }

    public boolean isApproveMode() {
        return approveMode;
    }

    public void doApprove() throws Exception {
        //titipan.setStPostedFlag("Y");
        doSave();
    }

    public void setApproveMode(boolean approveMode) {
        this.approveMode = approveMode;
    }

    public boolean isVoidMode() {
        return voidMode;
    }

    public void setVoidMode(boolean voidMode) {
        this.voidMode = voidMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isViewMode() {
        return viewMode;
    }

    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    /*
    public DTOList getObjects() throws Exception{
    ARtitipanLinesView activetitipanLine = getActivetitipanLine();

    objects = activetitipanLine.getObjects();

    return objects;
    }*/
    public void setObjects(DTOList objects) {
        this.objects = objects;
    }

    public String getObjectindex() {
        return objectindex;
    }

    public void setObjectindex(String objectindex) {
        this.objectindex = objectindex;
    }

    public void addLine() {
        /*
        final DTOList details = titipan.getDetails();
        final ARTitipanPremiDetailView titipanDetails = new ARTitipanPremiDetailView();
        details.add(titipanDetails);
        invoicesindex=String.valueOf(titipan.getDetails().size()-1);*/
    }

    public void delLine() throws Exception {
        titipan.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void doNewTitipan() throws Exception {

        final DTOList details = titipan.getDetails();

        final JournalView jv = new JournalView();

        jv.markNew();

        details.add(jv);

        notesindex = String.valueOf(titipan.getDetails().size() - 1);



    }
    private DTOList detailsTitipan;

    public DTOList getDetailsTitipan(String titipanID) {
        //logger.logWarning("++++++++++++++ titipan id : " + titipanID);
        loadTreaties(titipanID);
        return detailsTitipan;
    }

    private void loadTreaties(String titipanID) {
        try {
            if (detailsTitipan == null) {

                detailsTitipan = ListUtil.getDTOListFromQuery(
                        "select * from gl_je_detail where trx_no = ?",
                        new Object[]{getTitipanPremi(titipanID).getStTransactionNo()},
                        JournalView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JournalView getTitipanPremi(String stArTitipanID) {
        if (stArTitipanID == null) {
            return null;
        }
        return (JournalView) DTOPool.getInstance().getDTO(JournalView.class, stArTitipanID);
    }

    public void createNewUpload() throws Exception {
        titipan = new JournalHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        //final TitipanPremiView  jv = new TitipanPremiView ();

        //jv.markNew();

        //details.add(jv);

        titipan.setDetails(details);

        titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Jurnal Memorial");

        editMode = true;
    }

    public void uploadExcel() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Memorial");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        String accountno = null;
        String accountid = null;

        //cek akun dulu
        boolean adaAkunKosong = false;
        String listAkunKosong = "";
        for (int q = 1; q < rows; q++) {
            HSSFRow row = sheet.getRow(q);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }
            HSSFCell cellAccountNo = row.getCell(8);

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) == null) {
                //throw new Exception("No Akun " + accountno + " tidak ada di Web ");
                adaAkunKosong = true;
                listAkunKosong = listAkunKosong + "<br>" + accountno;
            }
        }

        if (adaAkunKosong) {

            throw new Exception("No Akun <br>" + listAkunKosong + " <br><br>tidak ada di sistem Web ");
        }

        //for (int r = 12; r < rows; r++) {
        for (int r = 1; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellCabang = row.getCell(1);
            HSSFCell cellMetode = row.getCell(2);
            HSSFCell cellBulan = row.getCell(3);
            HSSFCell cellTahun = row.getCell(4);
            HSSFCell cellTanggalTitipan = row.getCell(5);
            HSSFCell cellDescription = row.getCell(6);
            HSSFCell cellPolis = row.getCell(7);
            HSSFCell cellAccountNo = row.getCell(8);
            HSSFCell cellDebit = row.getCell(9);
            HSSFCell cellCredit = row.getCell(10);
            HSSFCell cellPemilik = row.getCell(11);
            HSSFCell cellPengguna = row.getCell(12);

            JournalView jv = new JournalView();
            jv.markNew();

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            AccountView account = GLUtil.getAccountByCode(accountno);

            if (account == null) {
                throw new Exception("No Akun " + accountno + " tidak ada di Web ");
            }

            jv.setStIDRFlag("Y");
            jv.setStCostCenter(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            jv.setStMethodCode(cellMetode.getCellType() == cellMetode.CELL_TYPE_STRING ? cellMetode.getStringCellValue() : new BigDecimal(cellMetode.getNumericCellValue()).toString());
            jv.setStMonths(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());
            jv.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStAccountID(account.getStAccountID());
            jv.setLgAccountID(Long.valueOf(account.getStAccountID()));
            jv.setStAccountNo(accountno);
            jv.setStDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());
            if (cellPolis != null) {
                jv.setStPolicyNo(cellPolis.getCellType() == cellPolis.CELL_TYPE_STRING ? cellPolis.getStringCellValue() : new BigDecimal(cellPolis.getNumericCellValue()).toString());
            }
            jv.setDtApplyDate(cellTanggalTitipan.getDateCellValue());
            if (BDUtil.lesserThanZero(new BigDecimal(cellDebit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbDebit(new BigDecimal(cellDebit.getNumericCellValue()));
            if (BDUtil.lesserThanZero(new BigDecimal(cellCredit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbCredit(new BigDecimal(cellCredit.getNumericCellValue()));

            if (BDUtil.lesserThanZero(new BigDecimal(cellDebit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbEnteredDebit(new BigDecimal(cellDebit.getNumericCellValue()));
            if (BDUtil.lesserThanZero(new BigDecimal(cellCredit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbEnteredCredit(new BigDecimal(cellCredit.getNumericCellValue()));

            if(cellPemilik!=null){
                jv.setStOwnerCode(cellPemilik.getStringCellValue());
            }

            if(cellPengguna!=null){
                jv.setStUserCode(cellPengguna.getStringCellValue());
            }
 

            details.add(jv);

        }

        titipan.setDetails(details);

    }

    /**
     * @return the dbTotalTitipan
     */
    public BigDecimal getDbTotalTitipan() {
        return dbTotalTitipan;
    }

    /**
     * @param dbTotalTitipan the dbTotalTitipan to set
     */
    public void setDbTotalTitipan(BigDecimal dbTotalTitipan) {
        this.dbTotalTitipan = dbTotalTitipan;
    }

    public void createNewUploadSyariah() throws Exception {
        titipan = new JournalHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        //final TitipanPremiView  jv = new TitipanPremiView ();

        //jv.markNew();

        //details.add(jv);

        titipan.setDetails(details);

        titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Laporan Syariah");

        editMode = true;
    }

    public void uploadExcelSyariah() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Syariah");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        String accountno = null;
        String accountid = null;
        String bulan = null;
        String tahun = null;

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellBulan = row.getCell(1);
            HSSFCell cellTahun = row.getCell(2);
            HSSFCell cellAccountID = row.getCell(3);
            HSSFCell cellAccountNo = row.getCell(4);
            HSSFCell cellDescription = row.getCell(5);
            HSSFCell cellDebit = row.getCell(6);


            JournalSyariahView jv = new JournalSyariahView();
            jv.markNew();

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            jv.setStCostCenter("00");
            jv.setStMethodCode("0");
            jv.setLgPeriodNo(Long.valueOf(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString()));
            jv.setLgFiscalYear(Long.valueOf(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString()));
            jv.setLgAccountID(new BigDecimal(cellAccountID.getNumericCellValue()).longValue());
            jv.setStAccountNo(accountno);
            jv.setStDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());
            jv.setDbDebit(new BigDecimal(cellDebit.getNumericCellValue()));
            jv.setDbEnteredDebit(new BigDecimal(cellDebit.getNumericCellValue()));

            details.add(jv);

        }

        titipan.setDetails(details);

    }

    /**
     * @return the years
     */
    public String getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(String years) {
        this.years = years;
    }

    public void createNewUploadRKAP() throws Exception {
        titipan = new JournalHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        setTitle("Upload Input RKAP");

        editMode = true;
    }

    public void uploadExcelRKAP() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("rkap");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellTahun = row.getCell(1);
            HSSFCell cellAccountID = row.getCell(2);
            HSSFCell cellDescription = row.getCell(3);
            HSSFCell cellKonvensional = row.getCell(4);
            HSSFCell cellSyariah = row.getCell(5);

            RKAPGroupView jv = new RKAPGroupView();
            jv.markNew();

            jv.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStRKAPGroupID(cellAccountID.getCellType() == cellAccountID.CELL_TYPE_STRING ? cellAccountID.getStringCellValue() : new BigDecimal(cellAccountID.getNumericCellValue()).toString());
            jv.setStRKAPDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());
            jv.setDbKonvensional(new BigDecimal(cellKonvensional.getNumericCellValue()));
            jv.setDbSyariah(new BigDecimal(cellSyariah.getNumericCellValue()));

            details.add(jv);

        }

        titipan.setDetails(details);

    }

    public void doSaveRKAP() throws Exception {

        getRemoteGeneralLedger().saveReportRKAP(titipan, titipan.getDetails());

        super.close();

    }

    public void doSaveSyariah() throws Exception {

        getRemoteGeneralLedger().saveJournalSyariah(titipan, titipan.getDetails());

        super.close();

    }

    public void createNewUploadCashBank() throws Exception {
        titipan = new JournalHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        //final TitipanPremiView  jv = new TitipanPremiView ();

        //jv.markNew();

        //details.add(jv);

        titipan.setDetails(details);

        titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Jurnal Kas Bank");

        editMode = true;
    }

    public void doSaveCashBank() throws Exception {

        getRemoteGeneralLedger().saveJournalCashBank(titipan, titipan.getDetails());

        super.close();

    }

     public void uploadExcelCashBank() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Cashbank");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        String accountno = null;
        String accountid = null;

        String accountheaderno = null;
        String accountheaderid = null;

        //cek akun dulu
        boolean adaAkunKosong = false;
        String listAkunKosong = "";
        for (int q = 1; q < rows; q++) {
            HSSFRow row = sheet.getRow(q);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }
            HSSFCell cellAccountNo = row.getCell(8);

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) == null) {
                //throw new Exception("No Akun " + accountno + " tidak ada di Web ");
                adaAkunKosong = true;
                listAkunKosong = listAkunKosong + "<br>" + accountno;
            }
        }

        if (adaAkunKosong) {

            throw new Exception("No Akun <br>" + listAkunKosong + " <br><br>tidak ada di sistem Web ");
        }

        //for (int r = 12; r < rows; r++) {
        for (int r = 1; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellCabang = row.getCell(1);
            HSSFCell cellMetode = row.getCell(2);
            HSSFCell cellBulan = row.getCell(3);
            HSSFCell cellTahun = row.getCell(4);
            HSSFCell cellTanggalTitipan = row.getCell(5);
            HSSFCell cellDescription = row.getCell(6);
            HSSFCell cellPolis = row.getCell(7);
            HSSFCell cellAccountNo = row.getCell(8);
            HSSFCell cellDebit = row.getCell(9);
            HSSFCell cellCredit = row.getCell(10);
            HSSFCell cellAkunBankHeader = row.getCell(11);

            HSSFCell cellPemilik = row.getCell(12);
            HSSFCell cellPengguna = row.getCell(13);

            JournalView jv = new JournalView();
            jv.markNew();

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            accountheaderno = cellAkunBankHeader.getCellType() == cellAkunBankHeader.CELL_TYPE_STRING ? cellAkunBankHeader.getStringCellValue() : new BigDecimal(cellAkunBankHeader.getNumericCellValue()).toString();

            AccountView account = GLUtil.getAccountByCode(accountno);

            if (account == null) {
                throw new Exception("No Akun " + accountno + " tidak ada di Web ");
            }

            AccountView accountHeader = GLUtil.getAccountByCode(accountheaderno);

            if (accountHeader == null) {
                throw new Exception("No Akun " + accountheaderno + " tidak ada di Web ");
            }


            jv.setStIDRFlag("Y");
            jv.setStCostCenter(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            jv.setStMethodCode(cellMetode.getCellType() == cellMetode.CELL_TYPE_STRING ? cellMetode.getStringCellValue() : new BigDecimal(cellMetode.getNumericCellValue()).toString());
            jv.setStMonths(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());
            jv.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStAccountID(account.getStAccountID());
            jv.setLgAccountID(Long.valueOf(account.getStAccountID()));
            jv.setStAccountNo(accountno);

            jv.setLgHeaderAccountID(Long.valueOf(accountHeader.getStAccountID()));
            jv.setStHeaderAccountNo(accountheaderno);

            jv.setStDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());
            if (cellPolis != null) {
                jv.setStPolicyNo(cellPolis.getCellType() == cellPolis.CELL_TYPE_STRING ? cellPolis.getStringCellValue() : new BigDecimal(cellPolis.getNumericCellValue()).toString());
            }
            jv.setDtApplyDate(cellTanggalTitipan.getDateCellValue());
            if (BDUtil.lesserThanZero(new BigDecimal(cellDebit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbDebit(new BigDecimal(cellDebit.getNumericCellValue()));
            if (BDUtil.lesserThanZero(new BigDecimal(cellCredit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbCredit(new BigDecimal(cellCredit.getNumericCellValue()));

            if (BDUtil.lesserThanZero(new BigDecimal(cellDebit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbEnteredDebit(new BigDecimal(cellDebit.getNumericCellValue()));
            if (BDUtil.lesserThanZero(new BigDecimal(cellCredit.getNumericCellValue()))) {
                throw new Exception("Nilai harus Positif");
            }
            jv.setDbEnteredCredit(new BigDecimal(cellCredit.getNumericCellValue()));

            if(cellPemilik!=null){
                jv.setStOwnerCode(cellPemilik.getStringCellValue());
            }

            if(cellPengguna!=null){
                jv.setStUserCode(cellPengguna.getStringCellValue());
            }


            details.add(jv);

        }

        titipan.setDetails(details);

    }

     private AccountMarketingView account;

    public void edit() {
        final String memorial_ID = (String)getAttribute("memorialID");

        account = (AccountMarketingView) DTOPool.getInstance().getDTO(AccountMarketingView.class, memorial_ID);

        account.markUpdate();

        setTitle("EDIT");
    }

    public void view() {
        final String memorial_ID = (String)getAttribute("memorialID");

        account = (AccountMarketingView) DTOPool.getInstance().getDTO(AccountMarketingView.class, memorial_ID);

        setReadOnly(true);

        setTitle("VIEW");
    }

    public void save() throws Exception {

        getRemoteGeneralLedger().saveMKT(account);

        close();
    }

    /**
     * @return the account
     */
    public AccountMarketingView getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(AccountMarketingView account) {
        this.account = account;
    }

}
