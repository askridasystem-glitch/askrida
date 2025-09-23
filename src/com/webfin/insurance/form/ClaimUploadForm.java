/***********************************************************************
 * Module:  com.webfin.gl.form.InsuranceUploadForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.FileUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.NumberUtil;
import com.crux.util.StringTools;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;

import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.UploadClaimHeaderView;
import com.webfin.insurance.model.UploadSubrogasiHeaderView;
import com.webfin.insurance.model.uploadClaimDetailView;
import com.webfin.insurance.model.uploadSubrogasiDetailView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ClaimUploadForm extends Form {

    //private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
    private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadForm.class);
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

    private UploadClaimHeaderView titipan;

    private UploadSubrogasiHeaderView subrogasi;

    public UploadClaimHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(UploadClaimHeaderView titipan) {
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

    public ClaimUploadForm() {
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

    public boolean isApproveMode() {
        return approveMode;
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

    public void uploadExcel() throws Exception {


        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_KLAIM tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellNoUrut = row.getCell(2);
            HSSFCell cellDeskripsi = row.getCell(3);
            HSSFCell cellTanggalPengajuanKlaim = row.getCell(4);
            HSSFCell cellTanggalKlaim = row.getCell(5);
            HSSFCell cellTanggalLKS = row.getCell(6);
            HSSFCell cellPenyebabKlaim = row.getCell(22);
            HSSFCell cellLokasiKlaim = row.getCell(8);
            HSSFCell cellNamaPengajuKlaim = row.getCell(9);
            HSSFCell cellAlamatPengajuKlaim = row.getCell(10);
            HSSFCell cellStatusPengajuKlaim = row.getCell(11);
            HSSFCell cellStatusKerugian = row.getCell(23);
            HSSFCell cellKlaimBenefit = row.getCell(24);
            HSSFCell cellDokumenKlaim = row.getCell(14);
            HSSFCell cellKronologis = row.getCell(15);
            HSSFCell cellMataUang = row.getCell(25);
            HSSFCell cellKurs = row.getCell(17);
            HSSFCell cellKlaimEstimated = row.getCell(18);
            HSSFCell cellDeductible = row.getCell(19);
            HSSFCell cellPotensiSubro = row.getCell(20);
 
            uploadClaimDetailView data = new uploadClaimDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellDeskripsi!=null)
                data.setStDescription(cellDeskripsi.getStringCellValue());

            if(cellTanggalPengajuanKlaim!=null)
                if(!cellTanggalPengajuanKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalPengajuanKlaim(df2.parse(cellTanggalPengajuanKlaim.getStringCellValue()));

            if(cellTanggalKlaim!=null)
                if(!cellTanggalKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalKlaim(df2.parse(cellTanggalKlaim.getStringCellValue()));

            if(cellTanggalLKS!=null)
                if(!cellTanggalLKS.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLKS(df2.parse(cellTanggalLKS.getStringCellValue()));

            if (cellPenyebabKlaim.getCellType() == cellPenyebabKlaim.CELL_TYPE_STRING) {
                data.setStClaimCauseID(cellPenyebabKlaim.getStringCellValue());
            } else {
                data.setStClaimCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaim.getNumericCellValue())));
            }

            if(cellLokasiKlaim!=null)
                data.setStClaimEventLocation(cellLokasiKlaim.getStringCellValue());

            if(cellNamaPengajuKlaim!=null)
                data.setStClaimPersonName(cellNamaPengajuKlaim.getStringCellValue());

            if(cellAlamatPengajuKlaim!=null)
                data.setStClaimPersonAddress(cellAlamatPengajuKlaim.getStringCellValue());

            if(cellStatusPengajuKlaim!=null)
                data.setStClaimPersonStatus(cellStatusPengajuKlaim.getStringCellValue());
            
            if (cellStatusKerugian.getCellType() == cellStatusKerugian.CELL_TYPE_STRING) {
                data.setStClaimLossID(cellStatusKerugian.getStringCellValue()); 
            } else {
                data.setStClaimLossID(ConvertUtil.removeTrailing(String.valueOf(cellStatusKerugian.getNumericCellValue()))); 
            }

            if (cellKlaimBenefit.getCellType() == cellKlaimBenefit.CELL_TYPE_STRING) {
                data.setStClaimBenefit(cellKlaimBenefit.getStringCellValue());
            } else {
                data.setStClaimBenefit(ConvertUtil.removeTrailing(String.valueOf(cellKlaimBenefit.getNumericCellValue())));
            }

            if(cellDokumenKlaim!=null)
                data.setStClaimDocument(cellDokumenKlaim.getStringCellValue());

            if(cellKronologis!=null)
                data.setStClaimChronology(cellKronologis.getStringCellValue());

            if(cellMataUang!=null)
                data.setStClaimCurrency(cellMataUang.getStringCellValue());

            if(cellKurs!=null)
                    data.setDbClaimCurrencyRate(new BigDecimal(cellKurs.getNumericCellValue()));

            if(cellKlaimEstimated!=null)
                    data.setDbClaimAmountEstimate(new BigDecimal(cellKlaimEstimated.getNumericCellValue()));

            if(cellDeductible!=null)
                    data.setDbClaimDeductibleAmount(new BigDecimal(cellDeductible.getNumericCellValue()));

            if(cellPotensiSubro!=null)
                data.setStPotensiSubrogasi(cellPotensiSubro.getStringCellValue());

            details.add(data);

        }

        titipan.setDetails(details);

        processZipFile();

    }

    public void uploadExcelEEI() throws Exception {


        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_KLAIM tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellNoUrut = row.getCell(2);
            HSSFCell cellDeskripsi = row.getCell(3);
            HSSFCell cellTanggalPengajuanKlaim = row.getCell(4);
            HSSFCell cellTanggalKlaim = row.getCell(5);
            HSSFCell cellTanggalLKS = row.getCell(6);
            HSSFCell cellPenyebabKlaim = row.getCell(22);
            HSSFCell cellLokasiKlaim = row.getCell(8);
            HSSFCell cellNamaPengajuKlaim = row.getCell(9);
            HSSFCell cellAlamatPengajuKlaim = row.getCell(10);
            HSSFCell cellStatusPengajuKlaim = row.getCell(11);
            HSSFCell cellStatusKerugian = row.getCell(23);
            HSSFCell cellKlaimBenefit = row.getCell(24);
            HSSFCell cellDokumenKlaim = row.getCell(14);
            HSSFCell cellKronologis = row.getCell(15);
            HSSFCell cellMataUang = row.getCell(25);
            HSSFCell cellKurs = row.getCell(17);
            HSSFCell cellKlaimEstimated = row.getCell(18);
            HSSFCell cellDeductible = row.getCell(19);
            HSSFCell cellPotensiSubro = row.getCell(20);

            uploadClaimDetailView data = new uploadClaimDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellDeskripsi!=null)
                data.setStDescription(cellDeskripsi.getStringCellValue());

            if(cellTanggalPengajuanKlaim!=null)
                if(!cellTanggalPengajuanKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalPengajuanKlaim(df2.parse(cellTanggalPengajuanKlaim.getStringCellValue()));

            if(cellTanggalKlaim!=null)
                if(!cellTanggalKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalKlaim(df2.parse(cellTanggalKlaim.getStringCellValue()));

            if(cellTanggalLKS!=null)
                if(!cellTanggalLKS.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLKS(df2.parse(cellTanggalLKS.getStringCellValue()));

            if (cellPenyebabKlaim.getCellType() == cellPenyebabKlaim.CELL_TYPE_STRING) {
                data.setStClaimCauseID(cellPenyebabKlaim.getStringCellValue());
            } else {
                data.setStClaimCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaim.getNumericCellValue())));
            }

            if(cellLokasiKlaim!=null)
                data.setStClaimEventLocation(cellLokasiKlaim.getStringCellValue());

            if(cellNamaPengajuKlaim!=null)
                data.setStClaimPersonName(cellNamaPengajuKlaim.getStringCellValue());

            if(cellAlamatPengajuKlaim!=null)
                data.setStClaimPersonAddress(cellAlamatPengajuKlaim.getStringCellValue());

            if(cellStatusPengajuKlaim!=null)
                data.setStClaimPersonStatus(cellStatusPengajuKlaim.getStringCellValue());

            if (cellStatusKerugian.getCellType() == cellStatusKerugian.CELL_TYPE_STRING) {
                data.setStClaimLossID(cellStatusKerugian.getStringCellValue());
            } else {
                data.setStClaimLossID(ConvertUtil.removeTrailing(String.valueOf(cellStatusKerugian.getNumericCellValue())));
            }

            if (cellKlaimBenefit.getCellType() == cellKlaimBenefit.CELL_TYPE_STRING) {
                data.setStClaimBenefit(cellKlaimBenefit.getStringCellValue());
            } else {
                data.setStClaimBenefit(ConvertUtil.removeTrailing(String.valueOf(cellKlaimBenefit.getNumericCellValue())));
            }

            if(cellDokumenKlaim!=null)
                data.setStClaimDocument(cellDokumenKlaim.getStringCellValue());

            if(cellKronologis!=null)
                data.setStClaimChronology(cellKronologis.getStringCellValue());

            if(cellMataUang!=null)
                data.setStClaimCurrency(cellMataUang.getStringCellValue());

            if(cellKurs!=null)
                    data.setDbClaimCurrencyRate(new BigDecimal(cellKurs.getNumericCellValue()));

            if(cellKlaimEstimated!=null)
                    data.setDbClaimAmountEstimate(new BigDecimal(cellKlaimEstimated.getNumericCellValue()));

            if(cellDeductible!=null)
                    data.setDbClaimDeductibleAmount(new BigDecimal(cellDeductible.getNumericCellValue()));

            if(cellPotensiSubro!=null)
                data.setStPotensiSubrogasi(cellPotensiSubro.getStringCellValue());

            details.add(data);

        }

        titipan.setDetails(details);

        processZipFile();

    }

    public void btnUploadExcel() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        //for (int r = 6; r < 7; r++) {
            HSSFRow row = sheet.getRow(6);

            HSSFCell cellNoPolis = row.getCell(1);

            //04592020
            //01234567
            String polTypeID = cellNoPolis.getStringCellValue().substring(2, 4);

            if(polTypeID.equalsIgnoreCase("59")) uploadExcelKreditKonsumtif();

            if(polTypeID.equalsIgnoreCase("10")) uploadExcelEEI();

        //}
    }


    public void uploadExcelKreditKonsumtif() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_KLAIM tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellNoUrut = row.getCell(2);
            HSSFCell cellDeskripsi = row.getCell(3);
            HSSFCell cellTanggalPengajuanKlaim = row.getCell(4);
            HSSFCell cellTanggalKlaim = row.getCell(5);
            HSSFCell cellTanggalLKS = row.getCell(6);
            HSSFCell cellPenyebabKlaim = row.getCell(23);
            HSSFCell cellLokasiKlaim = row.getCell(9);
            HSSFCell cellNamaPengajuKlaim = row.getCell(10);
            HSSFCell cellAlamatPengajuKlaim = row.getCell(11);
            HSSFCell cellStatusPengajuKlaim = row.getCell(12);
            HSSFCell cellStatusKerugian = row.getCell(24);
            HSSFCell cellKlaimBenefit = row.getCell(25);
            HSSFCell cellDokumenKlaim = row.getCell(15);
            HSSFCell cellKronologis = row.getCell(16);
            HSSFCell cellMataUang = row.getCell(26);
            HSSFCell cellKurs = row.getCell(18);
            HSSFCell cellKlaimEstimated = row.getCell(19);
            HSSFCell cellDeductible = row.getCell(20);
            HSSFCell cellPotensiSubro = row.getCell(21);
            HSSFCell cellPenyebabKlaimSpesifik = row.getCell(27);

            uploadClaimDetailView data = new uploadClaimDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellDeskripsi!=null)
                data.setStDescription(cellDeskripsi.getStringCellValue());

            if(cellTanggalPengajuanKlaim!=null)
                if(!cellTanggalPengajuanKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalPengajuanKlaim(df2.parse(cellTanggalPengajuanKlaim.getStringCellValue()));

            if(cellTanggalKlaim!=null)
                if(!cellTanggalKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalKlaim(df2.parse(cellTanggalKlaim.getStringCellValue()));

            if(cellTanggalLKS!=null)
                if(!cellTanggalLKS.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLKS(df2.parse(cellTanggalLKS.getStringCellValue()));

            if (cellPenyebabKlaim.getCellType() == cellPenyebabKlaim.CELL_TYPE_STRING) {
                data.setStClaimCauseID(cellPenyebabKlaim.getStringCellValue());
            } else {
                data.setStClaimCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaim.getNumericCellValue())));
            }

            if(cellPenyebabKlaimSpesifik!=null){
                if (cellPenyebabKlaimSpesifik.getCellType() == cellPenyebabKlaimSpesifik.CELL_TYPE_STRING) {
                    data.setStClaimSubCauseID(cellPenyebabKlaimSpesifik.getStringCellValue());
                } else {
                    data.setStClaimSubCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaimSpesifik.getNumericCellValue())));
                }
            }

            if(cellLokasiKlaim!=null)
                data.setStClaimEventLocation(cellLokasiKlaim.getStringCellValue());

            if(cellNamaPengajuKlaim!=null)
                data.setStClaimPersonName(cellNamaPengajuKlaim.getStringCellValue());

            if(cellAlamatPengajuKlaim!=null)
                data.setStClaimPersonAddress(cellAlamatPengajuKlaim.getStringCellValue());

            if(cellStatusPengajuKlaim!=null)
                data.setStClaimPersonStatus(cellStatusPengajuKlaim.getStringCellValue());

            if (cellStatusKerugian.getCellType() == cellStatusKerugian.CELL_TYPE_STRING) {
                data.setStClaimLossID(cellStatusKerugian.getStringCellValue());
            } else {
                data.setStClaimLossID(ConvertUtil.removeTrailing(String.valueOf(cellStatusKerugian.getNumericCellValue())));
            }

            if (cellKlaimBenefit.getCellType() == cellKlaimBenefit.CELL_TYPE_STRING) {
                data.setStClaimBenefit(cellKlaimBenefit.getStringCellValue());
            } else {
                data.setStClaimBenefit(ConvertUtil.removeTrailing(String.valueOf(cellKlaimBenefit.getNumericCellValue())));
            }

            if(cellDokumenKlaim!=null)
                data.setStClaimDocument(cellDokumenKlaim.getStringCellValue());

            if(cellKronologis!=null)
                data.setStClaimChronology(cellKronologis.getStringCellValue());

            if(cellMataUang!=null)
                data.setStClaimCurrency(cellMataUang.getStringCellValue());

            if(cellKurs!=null)
                    data.setDbClaimCurrencyRate(new BigDecimal(cellKurs.getNumericCellValue()));

            if(cellKlaimEstimated!=null)
                    data.setDbClaimAmountEstimate(new BigDecimal(cellKlaimEstimated.getNumericCellValue()));

            if(cellDeductible!=null)
                    data.setDbClaimDeductibleAmount(new BigDecimal(cellDeductible.getNumericCellValue()));

            if(cellPotensiSubro!=null)
                data.setStPotensiSubrogasi(cellPotensiSubro.getStringCellValue());

            details.add(data);

        }

        titipan.setDetails(details);

        processZipFile();

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


    public void Edit(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadClaimDetail(titipanID);

        titipan = (UploadClaimHeaderView) DTOPool.getInstance().getDTO(UploadClaimHeaderView.class, titipanID);

        if(titipan.isEffective())
            throw new Exception("Data tidak bisa diubah karena sudah disetujui");

        titipan.markUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

    }

    public void recalculate() throws Exception{

        cekData();

    }

    public void onChangeBranch(){

    }

    public void delAll() throws Exception {
        titipan.getDetails().deleteAll();
    }

    public void validate() throws Exception{
        DTOList details = titipan.getDetails();

    }

    public void clickPrint() throws Exception {

        SessionManager.getInstance().getRequest().setAttribute("DATATEXT", titipan.getDetails());

        String urx="/pages/insurance/prodrpt/upload.fop";

        if (urx==null) throw new RuntimeException("Unable to find suitable print form");

        super.redirect(urx);

    }

    public void cekData() throws Exception{

        BigDecimal totalKlaim = null;

        DTOList details = titipan.getDetails();

        boolean dataIsValid = true;
        
        String pesan = "";
        boolean cek = true;

        if(details.size()>100)
            throw new RuntimeException("Jumlah data maksimal 100 baris");

        for (int i = 0; i < details.size(); i++) {
            uploadClaimDetailView det = (uploadClaimDetailView) details.get(i);

            //if(det.isBypassValidasi()) continue;

            //CARI OBJEK ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listObjek = ListUtil.getDTOListFromQuery(
                " SELECT b.*, "+
                " (select insured_amount from ins_pol_tsi x where x.ins_pol_obj_id = b.ins_pol_obj_id) as total_tsi,"+
                " ( select "+
                " a.amount_settled::character varying||a.receipt_date::character varying"+
                " from "+
                " ar_invoice   a"+
                " where "+
                " coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id in (5,6,7) and attr_pol_no = ? limit 1) as payment"+
                " FROM INS_POLICY a "+
                " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE')  "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,0,17) = ? and cc_code = ?"+
                " and order_no = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{det.getStPolicyNo().substring(0, 16)+"00", det.getStPolicyNo().substring(0, 16), titipan.getStCostCenterCode(), det.getStOrderNo()},
                InsurancePolicyObjDefaultView.class);

                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) listObjek.get(0);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                //compare data upload dengan data real

                String msg = "";
                cek = true;

                if(objx==null){
                    dataIsValid = false;
                    cek = false;
                    msg = msg + " [Data Tidak Ada]";
                }

                if(objx!=null){

                     boolean sudahBayar = false;

                        if(objx.getStPayment()!=null){
                            sudahBayar = true;
                        }

                        if(!sudahBayar){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Belum Bayar]";
                        }

                         if(objx.isVoid()){
                                if(det.getClaimLoss().getStRepeatedClaimFlag()==null){
                                    dataIsValid = false;
                                    cek = false;
                                    msg = msg + " [Sudah Klaim]";
                                }
                            }

                        if(!det.getStPolicyNo().substring(2, 4).equalsIgnoreCase("10") && !det.getStPolicyNo().substring(2, 4).equalsIgnoreCase("59")){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [COB Hanya Boleh EEI & Kredit Konsumtif]";
                        }

                     /*
                     if(!objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(det.getStDescription().trim().toUpperCase())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Deskripsi/Nama Berbeda]";
                        }*/
                }
                
                if(!cek){
                    pesan =  pesan+ "- Polis "+ det.getStPolicyNo()+" ["+det.getStOrderNo()+" "+ det.getStDescription() +"] Data tidak valid : " + msg;
                }

                totalKlaim = BDUtil.add(totalKlaim, det.getDbClaimAmountEstimate());

                if(!pesan.equalsIgnoreCase(""))
                    pesan = pesan +"<br>";

        }

        titipan.setDbClaimTotal(totalKlaim);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }

    public void createNew() throws Exception {
        titipan = new UploadClaimHeaderView();

        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        titipan.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("UPLOAD KLAIM (LKS)"); 

        editMode = true;
    }

    public void doSave() throws Exception {

        recalculate();

        //validate();

        getRemoteInsurance().saveUploadClaim(titipan, titipan.getDetails());

        super.close();

    }

    public void view(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadClaimDetail(titipanID);

        titipan = (UploadClaimHeaderView) DTOPool.getInstance().getDTO(UploadClaimHeaderView.class, titipanID);

        titipan.setDetails(details);

        super.setReadOnly(true);

        viewMode = true;
    }

    public void approve(String titipanID) throws Exception {
        Edit(titipanID);

        titipan.setStEffectiveFlag("Y");

        super.setReadOnly(true);

        approveMode = true;
    }

    public void doApprove() throws Exception {

        doSave();
    }

    // Function to read and print the file names.
    public void processZipFile() throws Exception
    {

        if(titipan.getStClaimDocument()!=null){
            
            String fileUpload = titipan.getStClaimDocument();

            FileView file = FileManager.getInstance().getFile(fileUpload);

            FileInputStream fs = new FileInputStream(file.getStFilePath());

            // Creating objects for the classes and
            // initializing them to null
            //FileInputStream fs = null;
            ZipInputStream Zs = new ZipInputStream(new BufferedInputStream(fs));

            ZipEntry ze = null;

            //Baca file di zip, tampuung ke array list of file
            List<File> files = new ArrayList();

            while((ze = Zs.getNextEntry()) != null) {

                File fileZ = new File(ze.getName());

                FileOutputStream  os = new FileOutputStream(fileZ);
                for (int c = Zs.read(); c != -1; c = Zs.read()) {
                    os.write(c);
                }
                os.close();
                files.add(fileZ);
            }

            //sudah dapat file nya, proses ke pengajuan klaim

            for (int i = 0; i < files.size(); i++) {
                File file1 = (File) files.get(i);

                 String fileName [] = file1.getName().split("_");

                    String noPol = fileName[0];
                    String noUrut = fileName[1];
                    String docTypeID = fileName[2];

                    docTypeID = docTypeID.replaceAll(".pdf", "");

                    logger.logWarning("noPol nya : " + noPol);
                    logger.logWarning("noUrut nya : " + noUrut);

                    //simpan file nya ke server
                    String fileID = FileUtil.saveDocument(file1, noPol +"_"+ noUrut + "_"+docTypeID);

                    DTOList detail = titipan.getDetails();

                    for (int j = 0; j < detail.size(); j++) {
                        uploadClaimDetailView det = (uploadClaimDetailView) detail.get(j);

                        if(det.getStPolicyNo().equalsIgnoreCase(noPol) && det.getStOrderNo().equalsIgnoreCase(noUrut)){
                            det.setStFilePhysic(fileID);
                        }

                    }
            }
        }
        
        
    }

    // Function to read and print the file names.
    public void processClaimSheetZipFile() throws Exception
    {

        if(titipan.getStClaimAttachment()!=null){

            String fileUpload = titipan.getStClaimAttachment();

            FileView file = FileManager.getInstance().getFile(fileUpload);

            FileInputStream fs = new FileInputStream(file.getStFilePath());

            // Creating objects for the classes and
            // initializing them to null
            //FileInputStream fs = null;
            ZipInputStream Zs = new ZipInputStream(new BufferedInputStream(fs));

            ZipEntry ze = null;

            //Baca file di zip, tampuung ke array list of file
            List<File> files = new ArrayList();

            while((ze = Zs.getNextEntry()) != null) {

                File fileZ = new File(ze.getName());

                FileOutputStream  os = new FileOutputStream(fileZ);
                for (int c = Zs.read(); c != -1; c = Zs.read()) {
                    os.write(c);
                }
                os.close();
                files.add(fileZ);
            }

            //sudah dapat file nya, proses ke pengajuan klaim

            for (int i = 0; i < files.size(); i++) {
                File file1 = (File) files.get(i);

                 String fileName [] = file1.getName().split("_");

                    String noPol = fileName[0];
                    String noUrut = fileName[1];
                    String docTypeID = fileName[2];

                    docTypeID = docTypeID.replaceAll(".pdf", "");

                    logger.logWarning("noPol nya : " + noPol);
                    logger.logWarning("noUrut nya : " + noUrut);

                    //simpan file nya ke server
                    String fileID = FileUtil.saveDocument(file1, noPol +"_"+ noUrut + "_"+docTypeID);

                    DTOList detail = titipan.getDetails();

                    for (int j = 0; j < detail.size(); j++) {
                        uploadClaimDetailView det = (uploadClaimDetailView) detail.get(j);

                        if(det.getStPolicyNo().equalsIgnoreCase(noPol) && det.getStOrderNo().equalsIgnoreCase(noUrut)){
                            det.setStFilePhysic(fileID);
                        }

                    }
            }
        }


    }

    public void btnUploadExcelProcessKlaim() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        //for (int r = 6; r < 7; r++) {
            HSSFRow row = sheet.getRow(6);

            HSSFCell cellNoPolis = row.getCell(1);

            //04592020
            //01234567
            String polTypeID = cellNoPolis.getStringCellValue().substring(2, 4);

            if(polTypeID.equalsIgnoreCase("59")) uploadExcelKreditKonsumtifHO();

        //}
    }

    public void uploadExcelKreditKonsumtifHO() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_KLAIM");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_KLAIM tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellNoUrut = row.getCell(2);
            HSSFCell cellDeskripsi = row.getCell(3);
            HSSFCell cellTanggalPengajuanKlaim = row.getCell(4);
            HSSFCell cellTanggalKlaim = row.getCell(5);
            HSSFCell cellTanggalLKS = row.getCell(6);
            HSSFCell cellPenyebabKlaim = row.getCell(30);
            HSSFCell cellLokasiKlaim = row.getCell(9);
            HSSFCell cellNamaPengajuKlaim = row.getCell(10);
            HSSFCell cellAlamatPengajuKlaim = row.getCell(11);
            HSSFCell cellStatusPengajuKlaim = row.getCell(12);
            HSSFCell cellStatusKerugian = row.getCell(31);
            HSSFCell cellKlaimBenefit = row.getCell(32);
            HSSFCell cellDokumenKlaim = row.getCell(15);
            HSSFCell cellKronologis = row.getCell(16);
            HSSFCell cellMataUang = row.getCell(33);
            HSSFCell cellKurs = row.getCell(18);
            HSSFCell cellKlaimEstimated = row.getCell(19);
            HSSFCell cellDeductible = row.getCell(20);
            HSSFCell cellSurvey = row.getCell(21);
            HSSFCell cellPPN = row.getCell(22);
            HSSFCell cellSurveyNotes = row.getCell(23);
            HSSFCell cellSurveyorID = row.getCell(24);
            HSSFCell cellPotensiSubro = row.getCell(25);
            HSSFCell cellApprovedWho = row.getCell(26);
            HSSFCell cellProcessType = row.getCell(35);
            HSSFCell cellPenyebabKlaimSpesifik = row.getCell(34);
            HSSFCell cellRejectNotes = row.getCell(28);

            uploadClaimDetailView data = new uploadClaimDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellDeskripsi!=null)
                data.setStDescription(cellDeskripsi.getStringCellValue());

            if(cellTanggalPengajuanKlaim!=null)
                if(!cellTanggalPengajuanKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalPengajuanKlaim(df2.parse(cellTanggalPengajuanKlaim.getStringCellValue()));

            if(cellTanggalKlaim!=null)
                if(!cellTanggalKlaim.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalKlaim(df2.parse(cellTanggalKlaim.getStringCellValue()));

            if(cellTanggalLKS!=null)
                if(!cellTanggalLKS.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLKS(df2.parse(cellTanggalLKS.getStringCellValue()));

            if (cellPenyebabKlaim.getCellType() == cellPenyebabKlaim.CELL_TYPE_STRING) {
                data.setStClaimCauseID(cellPenyebabKlaim.getStringCellValue());
            } else {
                data.setStClaimCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaim.getNumericCellValue())));
            }

            if(cellPenyebabKlaimSpesifik!=null){
                if (cellPenyebabKlaimSpesifik.getCellType() == cellPenyebabKlaimSpesifik.CELL_TYPE_STRING) {
                    data.setStClaimSubCauseID(cellPenyebabKlaimSpesifik.getStringCellValue());
                } else {
                    data.setStClaimSubCauseID(ConvertUtil.removeTrailing(String.valueOf(cellPenyebabKlaimSpesifik.getNumericCellValue())));
                }
            }

            if(cellLokasiKlaim!=null)
                data.setStClaimEventLocation(cellLokasiKlaim.getStringCellValue());

            if(cellNamaPengajuKlaim!=null)
                data.setStClaimPersonName(cellNamaPengajuKlaim.getStringCellValue());

            if(cellAlamatPengajuKlaim!=null)
                data.setStClaimPersonAddress(cellAlamatPengajuKlaim.getStringCellValue());

            if(cellStatusPengajuKlaim!=null)
                data.setStClaimPersonStatus(cellStatusPengajuKlaim.getStringCellValue());

            if (cellStatusKerugian.getCellType() == cellStatusKerugian.CELL_TYPE_STRING) {
                data.setStClaimLossID(cellStatusKerugian.getStringCellValue());
            } else {
                data.setStClaimLossID(ConvertUtil.removeTrailing(String.valueOf(cellStatusKerugian.getNumericCellValue())));
            }

            if (cellKlaimBenefit.getCellType() == cellKlaimBenefit.CELL_TYPE_STRING) {
                data.setStClaimBenefit(cellKlaimBenefit.getStringCellValue());
            } else {
                data.setStClaimBenefit(ConvertUtil.removeTrailing(String.valueOf(cellKlaimBenefit.getNumericCellValue())));
            }

            if(cellDokumenKlaim!=null)
                data.setStClaimDocument(cellDokumenKlaim.getStringCellValue());

            if(cellKronologis!=null)
                data.setStClaimChronology(cellKronologis.getStringCellValue());

            if(cellMataUang!=null)
                data.setStClaimCurrency(cellMataUang.getStringCellValue());

            if(cellKurs!=null)
                    data.setDbClaimCurrencyRate(new BigDecimal(cellKurs.getNumericCellValue()));

            if(cellKlaimEstimated!=null)
                    data.setDbClaimAmountEstimate(new BigDecimal(cellKlaimEstimated.getNumericCellValue()));

            if(cellDeductible!=null)
                    data.setDbClaimDeductibleAmount(new BigDecimal(cellDeductible.getNumericCellValue()));

            if(cellPotensiSubro!=null)
                data.setStPotensiSubrogasi(cellPotensiSubro.getStringCellValue());

            if(cellSurvey!=null)
                data.setDbSurveyCostAmount(new BigDecimal(cellSurvey.getNumericCellValue()));

            if(cellPPN!=null)
                data.setDbPPNAmount(new BigDecimal(cellPPN.getNumericCellValue()));

            if(cellSurveyNotes!=null)
                data.setStSurveyNotes(cellSurveyNotes.getStringCellValue());

            if(cellApprovedWho!=null)
                data.setStApprovedWho(cellApprovedWho.getStringCellValue());

            if(cellProcessType!=null)
                data.setStProcessType(cellProcessType.getStringCellValue());

            if(cellSurveyorID!=null){
                if (cellSurveyorID.getCellType() == cellSurveyorID.CELL_TYPE_STRING) {
                    data.setStSurveyorEntID(cellSurveyorID.getStringCellValue());
                } else {
                    data.setStSurveyorEntID(ConvertUtil.removeTrailing(String.valueOf(cellSurveyorID.getNumericCellValue())));
                }
            }

            if(cellRejectNotes!=null)
                data.setStRejectNotes(cellRejectNotes.getStringCellValue());

            details.add(data);

        }

        titipan.setDetails(details);

        processZipFile();

    }

    public void recalculateProcess() throws Exception{

        cekDataProcess();

    }

    public void cekDataProcess() throws Exception{

        BigDecimal totalKlaim = null;

        DTOList details = titipan.getDetails();

        boolean dataIsValid = true;

        String pesan = "";
        boolean cek = true;

        if(details.size()>100)
            throw new RuntimeException("Jumlah data maksimal 100 baris");

        for (int i = 0; i < details.size(); i++) {
            uploadClaimDetailView det = (uploadClaimDetailView) details.get(i);

            //if(det.isBypassValidasi()) continue;

            //CARI OBJEK ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listObjek = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ( SELECT b.*, "+
                " (select insured_amount from ins_pol_tsi x where x.ins_pol_obj_id = b.ins_pol_obj_id) as total_tsi,"+
                " ( select "+
                " a.amount_settled::character varying||a.receipt_date::character varying"+
                " from "+
                " ar_invoice   a"+
                " where "+
                " coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id in (5,6,7) and attr_pol_no = ? limit 1) as payment"+
                " FROM INS_POLICY a "+
                " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE')  "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,0,17) = ?"+
                " and order_no = ? "+
                " ORDER BY POL_ID DESC "+
                " ) x order by pol_id desc limit 1",
                new Object[]{det.getStPolicyNo().substring(0, 16)+"00", det.getStPolicyNo().substring(0, 16), det.getStOrderNo()},
                InsurancePolicyObjDefaultView.class);

                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) listObjek.get(0);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                //compare data upload dengan data real

                String msg = "";
                cek = true;

                if(objx==null){
                    dataIsValid = false;
                    cek = false;
                    msg = msg + " [Data Tidak Ada]";
                }

                if(objx!=null){

                     boolean sudahBayar = false;

                        if(objx.getStPayment()!=null){
                            sudahBayar = true;
                        }

                        if(!sudahBayar){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Belum Bayar]";
                        }

                     /*
                         if(objx.isVoid()){
                                if(det.getClaimLoss().getStRepeatedClaimFlag()==null){
                                    dataIsValid = false;
                                    cek = false;
                                    msg = msg + " [Sudah Klaim]";
                                }
                            }*/

                        if(!det.getStPolicyNo().substring(2, 4).equalsIgnoreCase("10") && !det.getStPolicyNo().substring(2, 4).equalsIgnoreCase("59")){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [COB Hanya Boleh EEI & Kredit Konsumtif]";
                        }

                     /*
                     if(!objx.getStReference1().trim().toUpperCase().equalsIgnoreCase(det.getStDescription().trim().toUpperCase())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Deskripsi/Nama Berbeda]";
                        }*/
                }

                if(!cek){
                    pesan =  pesan+ "- Polis "+ det.getStPolicyNo()+" ["+det.getStOrderNo()+" "+ det.getStDescription() +"] Data tidak valid : " + msg;
                }

                totalKlaim = BDUtil.add(totalKlaim, det.getDbClaimAmountEstimate());

                if(!pesan.equalsIgnoreCase(""))
                    pesan = pesan +"<br>";

        }

        titipan.setDbClaimTotal(totalKlaim);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }

    public void doSaveProcess() throws Exception {

        recalculateProcess();

        //validate();

        getRemoteInsurance().saveUploadClaim(titipan, titipan.getDetails());

        super.close();

    }

    public void doApproveProcess() throws Exception {

        doSaveProcess();
    }

    public void createNewProcess() throws Exception {

        titipan = new UploadClaimHeaderView();

        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        titipan.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("PROSES KLAIM");

        editMode = true;
    }

    public void createNewSubrogasi() throws Exception {

        subrogasi = new UploadSubrogasiHeaderView();

        subrogasi.markNew();

        final DTOList details = new DTOList();

        subrogasi.setDetails(details);

        subrogasi.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("PROSES SUBROGASI");

        editMode = true;
    }

    public UploadSubrogasiHeaderView getSubrogasi() {
        return subrogasi;
    }

    public void setSubrogasi(UploadSubrogasiHeaderView subrogasi) {
        this.subrogasi = subrogasi;
    }

    public DTOList getSubrogasiDetails() throws Exception {
        return subrogasi.getDetails();
    }

    public void btnUploadSubrogasi() throws Exception {

        uploadExcelSubrogasi();
    }

    public void uploadExcelSubrogasi() throws Exception {

        String fileID = subrogasi.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_SUBROGASI");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_SUBROGASI tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        subrogasi.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            //HSSFCell cellNoUrut = row.getCell(2);
            HSSFCell cellNoLKP = row.getCell(2);
            HSSFCell cellDeskripsi = row.getCell(3);
            HSSFCell cellTanggalLKP = row.getCell(4);
            HSSFCell cellTanggalByrSubrogasi = row.getCell(5);
            HSSFCell cellKetEndorse = row.getCell(6);
            HSSFCell cellMataUang = row.getCell(15);
            HSSFCell cellKurs = row.getCell(8);
            HSSFCell cellNilaiSubrogasi = row.getCell(9);
            HSSFCell cellFeeRecovery = row.getCell(10);
            HSSFCell cellApprovedWho = row.getCell(11);
            HSSFCell cellProcessType = row.getCell(16);
            HSSFCell cellKetKlaim = row.getCell(13);

            uploadSubrogasiDetailView data = new uploadSubrogasiDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            //cek polis
            InsurancePolicyView polis = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                    " from ins_policy "+
                                    " where status in ('POLICY','ENDORSE','RENEWAL') and pol_no = ?",
            new Object[]{data.getStPolicyNo()},InsurancePolicyView.class).getDTO();

            if(polis==null){
                throw new RuntimeException("Polis "+ data.getStPolicyNo()+" tidak ditemukan");
            }

            /*
            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }*/

            data.setStDLANo(cellNoLKP.getStringCellValue());

            //cek lkp
            InsurancePolicyView lkp = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where dla_no = ?",
                new Object[]{data.getStDLANo()},InsurancePolicyView.class).getDTO();

            if(lkp==null){
                throw new RuntimeException("LKP "+ data.getStDLANo()+" tidak ditemukan");
            }

            if(cellDeskripsi!=null)
                data.setStDescription(cellDeskripsi.getStringCellValue());

            /*
            if(cellTanggalLKP!=null)
                if(!cellTanggalLKP.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLKP(df2.parse(cellTanggalLKP.getStringCellValue()));
             * 
             */

            if(cellTanggalLKP!=null)
                    data.setDtTanggalLKP(cellTanggalLKP.getDateCellValue());

            /*
            if(cellTanggalByrSubrogasi!=null)
                if(!cellTanggalByrSubrogasi.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalBayarSubrogasi(df2.parse(cellTanggalByrSubrogasi.getStringCellValue()));
             * *
             */

            if(cellTanggalByrSubrogasi!=null)
                    data.setDtTanggalBayarSubrogasi(cellTanggalByrSubrogasi.getDateCellValue());

            if(cellKetEndorse!=null)
                data.setStEndorseNotes(cellKetEndorse.getStringCellValue());

            if(cellMataUang!=null)
                data.setStClaimCurrency(cellMataUang.getStringCellValue());

            if(cellKurs!=null)
                    data.setDbClaimCurrencyRate(new BigDecimal(cellKurs.getNumericCellValue()));

            if(cellNilaiSubrogasi!=null)
                    data.setDbSubrogasiAmount(new BigDecimal(cellNilaiSubrogasi.getNumericCellValue()));

            if(cellFeeRecovery!=null)
                    data.setDbFeeRecovery(new BigDecimal(cellFeeRecovery.getNumericCellValue()));

            if(cellApprovedWho!=null)
                data.setStApprovedWho(cellApprovedWho.getStringCellValue());

            if(cellProcessType!=null)
                data.setStProcessType(cellProcessType.getStringCellValue());

            if(cellKetKlaim!=null)
                data.setStClaimNotes(cellKetKlaim.getStringCellValue());



            details.add(data);

        }

        subrogasi.setDetails(details);

    }

    public void doSaveSubrogasi() throws Exception {

        recalculateSubrogasi();

        getRemoteInsurance().saveUploadSubrogasi(subrogasi, subrogasi.getDetails());

        super.close();

    }

    public void doApproveSubrogasi() throws Exception {

        doSaveSubrogasi();
    }

    public void EditSubrogasi(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadSubrogasiDetail(titipanID);

        subrogasi = (UploadSubrogasiHeaderView) DTOPool.getInstance().getDTO(UploadSubrogasiHeaderView.class, titipanID);

        if(subrogasi.isEffective())
            throw new Exception("Data tidak bisa diubah karena sudah disetujui");

        subrogasi.markUpdate();

        subrogasi.setDetails(details);

        subrogasi.getDetails().markAllUpdate();

    }

    public void viewSubrogasi(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadSubrogasiDetail(titipanID);

        subrogasi = (UploadSubrogasiHeaderView) DTOPool.getInstance().getDTO(UploadSubrogasiHeaderView.class, titipanID);

        subrogasi.setDetails(details);

        super.setReadOnly(true);

        viewMode = true;
    }

    public void approveSubrogasi(String titipanID) throws Exception {
        EditSubrogasi(titipanID);

        subrogasi.setStEffectiveFlag("Y");

        super.setReadOnly(true);

        approveMode = true;
    }

    public void delAllSubrogasi() throws Exception {
        subrogasi.getDetails().deleteAll();
    }

    public void delLineSubrogasi() throws Exception {
        subrogasi.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void recalculateSubrogasi() throws Exception{

        BigDecimal totalKlaim = BDUtil.zero;

        DTOList details = subrogasi.getDetails();

        boolean dataIsValid = true;

        String pesan = "";
        boolean cek = true;

        if(details.size()>100)
            throw new RuntimeException("Jumlah data maksimal 100 baris");

        for (int i = 0; i < details.size(); i++) {
            uploadSubrogasiDetailView det = (uploadSubrogasiDetailView) details.get(i);

            //if(det.isBypassValidasi()) continue;

                //compare data upload dengan data real

                String msg = "";
                cek = true;


                InsurancePolicyView lkpSpecimen = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where coalesce(effective_flag,'N') <> 'Y' and dla_no = ?",
                new Object[]{det.getStDLANo()},InsurancePolicyView.class).getDTO();

                if(det.isApprovedSubrogasi()){
                    if(lkpSpecimen==null){
                        dataIsValid = false;
                        cek = false;
                        msg = msg + " [LKP Tidak Ditemukan]";
                    }
                }

                if(det.isCreateSubrogasiApproved()||det.isCreateSubrogasiNotApproved()){

                    //cek nilai subro dgn nilai klaim disetujui
                    InsurancePolicyView lkpInduk = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where effective_flag = 'Y' and dla_no = ?",
                    new Object[]{det.getStDLANo().substring(0,19)},InsurancePolicyView.class).getDTO();

                    if(lkpInduk==null){
                        dataIsValid = false;
                        cek = false;
                        msg = msg + " [LKP Induk Tidak Ditemukan]";
                    }else{
                        //BigDecimal selisihKlaim = BDUtil.sub(det.getDbSubrogasiAmount(), lkpInduk.getDbClaimAmountApproved());

                        boolean isSelisihKlaim = false;
                        /*
                        if (BDUtil.biggerThan(selisihKlaim, new BigDecimal(5))) {
                            isSelisihKlaim = true;
                        }
                        if (BDUtil.lesserThan(selisihKlaim, new BigDecimal(-5))) {
                            isSelisihKlaim = true;
                        }*/

                        if(BDUtil.biggerThan(BDUtil.round(det.getDbSubrogasiAmount(), 2), BDUtil.round(lkpInduk.getDbClaimAmountApproved(),2))){
                            isSelisihKlaim = true;
                        }
                            
                        if(isSelisihKlaim){
                            dataIsValid = false;
                            cek = false;
                            logger.logDebug("######### SELISIH NILAI SUBROGASI = "+ det.getDbSubrogasiAmount());
                            logger.logDebug("######### SELISIH KLAIM DISETUJUI = "+ lkpInduk.getDbClaimAmountApproved());

                            msg = msg + " [Nilai Subrogasi "+ NumberUtil.getMoneyStr(det.getDbSubrogasiAmount(),2) +" Melebihi Nilai Klaim Disetujui ("+ NumberUtil.getMoneyStr(lkpInduk.getDbClaimAmountApproved(), 2) +") di "+ det.getStDLANo().substring(0,19) +"]";
                        }
                    }

                    //cek kesesuaian no polis dgn no lkp
                    String nopolLike = det.getStPolicyNo().length()==18?""+det.getStPolicyNo().substring(0,16)+"%":""+det.getStPolicyNo().substring(0,17)+"%";

                    InsurancePolicyView cekKlaim = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where dla_no = ? and pol_no like ?",
                    new Object[]{det.getStDLANo().substring(0,19), nopolLike},InsurancePolicyView.class).getDTO();

                    if(cekKlaim==null){
                        dataIsValid = false;
                        cek = false;
                        msg = msg + " [Data Tidak Ditemukan, Cek Kesesuaian No Polis & No LKP]";
                    }

                }

                //cek polis
                InsurancePolicyView polis = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * "+
                                        " from ins_policy "+
                                        " where pol_no = ?",
                new Object[]{det.getStPolicyNo()},InsurancePolicyView.class).getDTO();

                if(polis==null){
                    dataIsValid = false;
                    cek = false;
                    msg = msg + " [Polis Tidak Ada]";
                }

                

                if(!cek){
                    pesan =  pesan+ "- Polis "+ det.getStPolicyNo()+" ["+det.getStDLANo()+" "+ det.getStDescription() +"] Data tidak valid : " + msg + "<br>";
                }

                totalKlaim = BDUtil.add(totalKlaim, det.getDbSubrogasiAmount());

                //if(!pesan.equalsIgnoreCase(""))
                    //pesan = pesan +"<br>";

        }

        subrogasi.setDbClaimTotal(totalKlaim);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }
    

}
