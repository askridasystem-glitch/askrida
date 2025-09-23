/***********************************************************************
 * Module:  com.webfin.ar.forms.BungaDepositoForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.BungaDepositoHeaderView;
import com.webfin.gl.model.BungaDepositoView;

import com.webfin.insurance.ejb.*;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class BungaDepositoForm extends Form {

    private BungaDepositoHeaderView titipan;
    private BungaDepositoView budep;
    private String notesindex;
    private String glindex;
    private String stGLSelect;
    private String invoicesindex;
    private String invoicecomissionindex;
    private String parinvoiceid;
    private String nosurathutang;
    private String cc_code;
    private String artitipanid;
    private String stReportType;
    public DTOList objects;
    public String objectindex;
    private boolean drillMode = false;
    private BigDecimal dbTotalTitipan;
    private String stHeaderAccountID;
    private String stHeaderAccountNo;
    private String stARBungaID;
    public boolean voidMode;
    private boolean approveMode;
    private boolean editMode;
    private boolean saveMode;
    private boolean inputMode;
    private boolean viewMode;
    private boolean reverseMode;
    private boolean deleteMode;
    private final static transient LogManager logger = LogManager.getInstance(BungaDepositoForm.class);

    public BungaDepositoHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(BungaDepositoHeaderView titipan) {
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

    public void tes() throws Exception {
        logger.logDebug("line= ");
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

    public BungaDepositoForm() {
    }

    public void onNewNoteItem() {
    }

    public void createNew() throws Exception {
        titipan = new BungaDepositoHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        final BungaDepositoView jv = new BungaDepositoView();

        jv.markNew();

        details.add(jv);

        titipan.setDetails(details);

        titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Input Bunga Deposito");

        setEditMode(true);
    }

    public void edit(String titipanID) throws Exception {
        logger.logDebug("++++++++++++++ titipanID : " + titipanID);
        masterEdit(titipanID);

        //if (titipan.isPosted()) throw new RuntimeException("titipan not editable (Posted)");
        //if (titipan.isCancel()) throw new RuntimeException("titipan not editable (VOID)");

        setEditMode(true);
    }

    public void superEdit(String titipanID) throws Exception {
        artitipanid = titipanID;

        masterEdit(titipanID);

        setEditMode(true);
    }

    public void masterEdit(String titipanID) throws Exception {

        final DTOList details = getRemoteGeneralLedger().getBungaDeposito(titipanID);

        titipan = new BungaDepositoHeaderView();

        titipan.markUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

        BungaDepositoView titip = (BungaDepositoView) details.get(0);

        titipan.setStTransactionHeaderNo(titip.getStTransactionHeaderNo());
        titipan.setStCostCenter(titip.getStTransactionNo().substring(5, 7));
        titipan.setStMonths(titip.getStMonths());
        titipan.setStYears(titip.getStYears());
        titipan.setStMethodCode(titip.getStTransactionNo().substring(0, 1));
        titipan.setStHeaderAccountNo(titip.getStHeaderAccountNo());
        titipan.setStHeaderAccountMaster(titip.getStHeaderAccountMaster());

    }

    public void doSave() throws Exception {

        getRemoteGeneralLedger().saveBungaDeposito(titipan, titipan.getDetails());

        super.close();
    }

    public void doRecalculate() throws Exception {
        //titipan.recalculate();

        BigDecimal total = BDUtil.zero;

        final DTOList details = getDetails();
        for (int i = 0; i < details.size(); i++) {
            BungaDepositoView titip = (BungaDepositoView) details.get(i);

            titip.setStHeaderAccountID(titipan.getStHeaderAccountID());
            titip.setStHeaderAccountNo(titipan.getStHeaderAccountNo());
            titip.setStMonths(titipan.getStMonths());
            titip.setStCostCenter(titipan.getStCostCenter());

            total = BDUtil.add(total, titip.getDbAmount());

        }

        dbTotalTitipan = total;

    }

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

    public void doApprove() throws Exception {

        String bunga_id = titipan.getStARBungaID();

        if (titipan.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + titipan.getStMonths() + " dan tahun " + titipan.getStYears() + " tsb sudah diposting");
        }

        getRemoteGeneralLedger().approveBungaDeposito(titipan, titipan.getDetails());

        super.close();

    }

    public boolean isVoidMode() {
        return voidMode;
    }

    public void setVoidMode(boolean voidMode) {
        this.voidMode = voidMode;
    }

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

        final BungaDepositoView jv = new BungaDepositoView();

        jv.markNew();

        details.add(jv);

        notesindex = String.valueOf(titipan.getDetails().size() - 1);

    }
    private DTOList detailsTitipan;

    public DTOList getDetailsTitipan(String titipanID) {
        logger.logWarning("++++++++++++++ titipan id : " + titipanID);
        loadTreaties(titipanID);
        return detailsTitipan;
    }

    private void loadTreaties(String titipanID) {
        try {
            if (detailsTitipan == null) {

                detailsTitipan = ListUtil.getDTOListFromQuery(
                        "select * from ar_titipan_premi where trx_no = ?",
                        new Object[]{getBungaDeposito(titipanID).getStTransactionNo()},
                        BungaDepositoView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BungaDepositoView getBungaDeposito(String stArTitipanID) {
        if (stArTitipanID == null) {
            return null;
        }
        return (BungaDepositoView) DTOPool.getInstance().getDTO(BungaDepositoView.class, stArTitipanID);
    }

    public void createNewUpload() throws Exception {
        titipan = new BungaDepositoHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        //final TitipanPremiView  jv = new TitipanPremiView ();

        //jv.markNew();

        //details.add(jv);

        titipan.setDetails(details);

        titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Bunga Deposito");

        setSaveMode(true);
    }

    public void uploadExcel() throws Exception {
        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("budep");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        int counter = 1;

        for (int r = 12; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellCabang = row.getCell(1);//cabang
            HSSFCell cellMetode = row.getCell(2);//cabang
            HSSFCell cellBulan = row.getCell(3);//cabang
            HSSFCell cellTahun = row.getCell(4);//cabang
            HSSFCell cellAccountHeaderID = row.getCell(5);//cabang
            HSSFCell cellAccountHeaderNo = row.getCell(6);//cabang
            HSSFCell cellAccountHeaderName = row.getCell(7);//cabang
            HSSFCell cellAccountID = row.getCell(8);//cabang
            HSSFCell cellAccountNo = row.getCell(9);//cabang
            HSSFCell cellAccountName = row.getCell(10);//cabang
            //HSSFCell cellCounter  = row.getCell(11);//cabang
            HSSFCell cellDescription = row.getCell(11);//cabang
            //HSSFCell cellCause  = row.getCell(13);//cabang
            HSSFCell cellTanggalTitipan = row.getCell(12);//cabang
            HSSFCell cellJumlahTitipan = row.getCell(13);//cabang

            BungaDepositoView jv = new BungaDepositoView();
            jv.markNew();

            if (r == 12) {
                titipan = getTitipan();

                titipan.setStCostCenter(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
                titipan.setStMonths(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());
                titipan.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
                titipan.setStMethodCode(cellMetode.getCellType() == cellMetode.CELL_TYPE_STRING ? cellMetode.getStringCellValue() : new BigDecimal(cellMetode.getNumericCellValue()).toString());
                titipan.setStHeaderAccountID(cellAccountHeaderID.getCellType() == cellAccountHeaderID.CELL_TYPE_STRING ? cellAccountHeaderID.getStringCellValue() : new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
                titipan.setStHeaderAccountNo(cellAccountHeaderNo.getCellType() == cellAccountHeaderNo.CELL_TYPE_STRING ? cellAccountHeaderNo.getStringCellValue() : new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
                titipan.setStHeaderAccountMaster(cellAccountHeaderName.getCellType() == cellAccountHeaderName.CELL_TYPE_STRING ? cellAccountHeaderName.getStringCellValue() : new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());

            }



            jv.setStActiveFlag("Y");
            jv.setStCostCenter(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            jv.setStMethodCode(cellMetode.getCellType() == cellMetode.CELL_TYPE_STRING ? cellMetode.getStringCellValue() : new BigDecimal(cellMetode.getNumericCellValue()).toString());
            jv.setStMonths(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());
            jv.setStYears(cellTahun.getCellType() == cellTahun.CELL_TYPE_STRING ? cellTahun.getStringCellValue() : new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStHeaderAccountID(cellAccountHeaderID.getCellType() == cellAccountHeaderID.CELL_TYPE_STRING ? cellAccountHeaderID.getStringCellValue() : new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
            jv.setStHeaderAccountNo(cellAccountHeaderNo.getCellType() == cellAccountHeaderNo.CELL_TYPE_STRING ? cellAccountHeaderNo.getStringCellValue() : new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
            jv.setStHeaderAccountMaster(cellAccountHeaderName.getCellType() == cellAccountHeaderName.CELL_TYPE_STRING ? cellAccountHeaderName.getStringCellValue() : new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());
            jv.setStAccountID(cellAccountID.getCellType() == cellAccountID.CELL_TYPE_STRING ? cellAccountID.getStringCellValue() : new BigDecimal(cellAccountID.getNumericCellValue()).toString());
            jv.setStAccountNo(cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString());
            jv.setStDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());
            if (!titipan.getStMonths().equalsIgnoreCase(DateUtil.getMonth2Digit(cellTanggalTitipan.getDateCellValue()).toString())) throw new RuntimeException("Bulan pada "+jv.getStDescription()+" Tidak Sama Dengan Header");
            if (!titipan.getStYears().equalsIgnoreCase(DateUtil.getYear(cellTanggalTitipan.getDateCellValue()).toString())) throw new RuntimeException("Tahun pada "+jv.getStDescription()+" Tidak Sama Dengan Header");
            jv.setDtApplyDate(cellTanggalTitipan.getDateCellValue());
            jv.setDbAmount(new BigDecimal(cellJumlahTitipan.getNumericCellValue()));

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

    public String getStHeaderAccountID() {
        return stHeaderAccountID;
    }

    public void setStHeaderAccountID(String stHeaderAccountID) {
        this.stHeaderAccountID = stHeaderAccountID;
    }

    public String getStHeaderAccountNo() {
        return stHeaderAccountNo;
    }

    public void setStHeaderAccountNo(String stHeaderAccountNo) {
        this.stHeaderAccountNo = stHeaderAccountNo;
    }

    public BungaDepositoView getBudep() {
        return budep;
    }

    public void setBudep(BungaDepositoView budep) {
        this.budep = budep;
    }

    public String getStARBungaID() {
        return stARBungaID;
    }

    public void setStARBungaID(String stARBungaID) {
        this.stARBungaID = stARBungaID;
    }

    public void editBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        setEditMode(true);

        titipan.markUpdate();

        setTitle("REALISASI BUNGA DEPOSITO");
    }

    public void saveBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        setApproveMode(true);

        titipan.markUpdate();

        setTitle("SETUJUI BUNGA DEPOSITO");
    }

    public void inputBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        setInputMode(true);

        titipan.markUpdate();

        setTitle("REALISASI BUNGA DEPOSITO");
    }

    public void viewBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        super.setReadOnly(true);

        setViewMode(true);

        setTitle("LIHAT BUNGA DEPOSITO");
    }

    public void reverseBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        setReverseMode(true);

        titipan.markUpdate();

        setTitle("REVERSE BUNGA DEPOSITO");
    }

    public void deleteBudep(String arbudepid) throws Exception {

        masterEdit(arbudepid);

        setDeleteMode(true);

        titipan.markUpdate();

        setTitle("DELETE BUNGA DEPOSITO");
    }

    public void doReverse() throws Exception {

        getRemoteGeneralLedger().reverseBungaDeposito(titipan, titipan.getDetails());

        super.close();
    }

    public void doDelete() throws Exception {

        getRemoteGeneralLedger().deleteBungaDeposito(titipan.getStTransactionHeaderNo(), titipan.getDetails());

        super.close();
    }

    /**
     * @return the approveMode
     */
    public boolean isApproveMode() {
        return approveMode;
    }

    /**
     * @param approveMode the approveMode to set
     */
    public void setApproveMode(boolean approveMode) {
        this.approveMode = approveMode;
    }

    /**
     * @return the editMode
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * @param editMode the editMode to set
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * @return the saveMode
     */
    public boolean isSaveMode() {
        return saveMode;
    }

    /**
     * @param saveMode the saveMode to set
     */
    public void setSaveMode(boolean saveMode) {
        this.saveMode = saveMode;
    }

    /**
     * @return the viewMode
     */
    public boolean isViewMode() {
        return viewMode;
    }

    /**
     * @param viewMode the viewMode to set
     */
    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    /**
     * @return the reverseMode
     */
    public boolean isReverseMode() {
        return reverseMode;
    }

    /**
     * @param reverseMode the reverseMode to set
     */
    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    /**
     * @return the deleteMode
     */
    public boolean isDeleteMode() {
        return deleteMode;
    }

    /**
     * @param deleteMode the deleteMode to set
     */
    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    /**
     * @return the inputMode
     */
    public boolean isInputMode() {
        return inputMode;
    }

    /**
     * @param inputMode the inputMode to set
     */
    public void setInputMode(boolean inputMode) {
        this.inputMode = inputMode;
    }

    public void doSaveInput() throws Exception {

        getRemoteGeneralLedger().saveInputBilyetDeposito(titipan, titipan.getDetails());

        super.close();
    }
}
