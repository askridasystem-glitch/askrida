/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.AccountForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.accounts.forms;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.webfin.gl.ejb.*;
import com.webfin.gl.model.*;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.InsurancePolicyTypeView;
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

public class AccountForm extends Form {
    
    private AccountView2 account;
    private String stEntID;
    private String stEntName;
    private String stGlCode;
    private String stAccountHeader;
    private String stTypeCode;

    private boolean EditMode;

    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String accountsindex;
    private JournalHeaderView titipan;
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        account= new AccountView2();

        account.markNew();

        account.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        setStTypeCode("00");
        account.setStEnabled("Y");

        setTitle("CREATE ACCOUNT CATEGORY");

    }
    
    public void edit() {

         setEditMode(true);

        final String account_id = (String)getAttribute("accountid");
        
        account = (AccountView2) DTOPool.getInstance().getDTO(AccountView2.class, account_id);
        
        account.markUpdateO();
        
        setTitle("EDIT ACCOUNT CATEGORY");
    }
    
    public void view() {
        final String account_id = (String)getAttribute("accountid");
        
        account = (AccountView2) DTOPool.getInstance().getDTO(AccountView2.class, account_id);
        
        setReadOnly(true);
        
        setTitle("VIEW ACCOUNT CATEGORY");
    }
    
    public void save() throws Exception {
        if (account.getStAccountID()==null){
            account.checkAccountNo();            
        }
        
        getRemoteGeneralLedger().save2(account);

        close();
    }
    
    public void close() {
        super.close();
    }
    
    public AccountView2 getAccount() {
        return account;
    }
    
    public void setAccount(AccountView2 account) {
        this.account = account;
    }
    
    public String getStEntName() {
        return stEntName;
    }
    
    public void setStEntName(String stEntName) {
        this.stEntName = stEntName;
    }
    
    public String getStGlCode() {
        if (account.getStAccountNo()!=null)
            stGlCode = account.getStAccountNo().substring(5,10);
        return stGlCode;
    }
    
    public void setStGlCode(String stGlCode) {
        this.stGlCode = stGlCode;
    }
    
    public String getStAccountHeader() {
        if (account.getStAccountNo()!=null)
            stAccountHeader = account.getStAccountNo().substring(0,5);
        return stAccountHeader;
    }
    
    public void setStAccountHeader(String stAccountHeader) {
        this.stAccountHeader = stAccountHeader;
    }
    
    public String getStEntID() {
        return stEntID;
    }
    
    public void setStEntID(String stEntID) {
        this.stEntID = stEntID;
    }

    public void refresh() {
        account.setStAccountNo(stAccountHeader + stGlCode + stTypeCode + " " + account.getStCostCenterCode());

        String jenpol = null;
        if (getStTypeCode().equalsIgnoreCase("00")) {
            jenpol = "";
        } else {
            jenpol = " - " + getPolicyType().getStShortDescription();
        }

        if (stEntID != null) {
            account.setStDescription(getAccountHeader().getStValueString() + " " + getStEntName() + " " + jenpol);
        } else {
            account.setStDescription(getAccountHeader().getStValueString() + " " + jenpol);
        }
//        if (account.getStDescription()==null) {
//            if (stAccountHeader!=null) {
//                if (("1111").equalsIgnoreCase(stAccountHeader.substring(0,4))) {
//                    account.setStDescription("DEPOSITO WAJIB "+getStEntName());
//                } else if (("1112").equalsIgnoreCase(stAccountHeader.substring(0,4))) {
//                    account.setStDescription("DEPOSITO BERJANGKA "+getStEntName());
//                } else {
//                    account.setStDescription(accountHeader.getStValueString()+" "+getStEntName());
//                }
//            }
//        }


    }
    
    public String getStTypeCode() {
        if (account.getStAccountNo()!=null)
            stTypeCode = account.getStAccountNo().substring(10,12);
        return stTypeCode;
    }

    public void setStTypeCode(String stTypeCode) {
        this.stTypeCode = stTypeCode;
    }

    public boolean isEditMode() {
        return EditMode;
    }

    public void setEditMode(boolean EditMode) {
        this.EditMode = EditMode;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    /**
     * @return the accountsindex
     */
    public String getAccountsindex() {
        return accountsindex;
    }

    /**
     * @param accountsindex the accountsindex to set
     */
    public void setAccountsindex(String accountsindex) {
        this.accountsindex = accountsindex;
    }

    public void delLine() throws Exception {
        titipan.getDetails().delete(Integer.parseInt(accountsindex));
    }

    public JournalHeaderView getTitipan() {
        return titipan;
    }

    public DTOList getDetails() throws Exception {
        return titipan.getDetails();
    }

    public void doSave() throws Exception {

        getRemoteGeneralLedger().saveAccounts(titipan, titipan.getDetails());

        super.close();

    }

    public void uploadExcelAccounts() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Account");

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
            HSSFCell cellAccountNo = row.getCell(4);

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) != null) {
                //throw new Exception("No Akun " + accountno + " tidak ada di Web ");
                adaAkunKosong = true;
                listAkunKosong = listAkunKosong + "<br>" + accountno;
            }
        }

        if (adaAkunKosong) {

            throw new Exception("No Akun <br>" + listAkunKosong + " <br><br>sudah ada di sistem Web ");
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

            HSSFCell cellNoper = row.getCell(1);
            HSSFCell cellCabang = row.getCell(2);
            HSSFCell cellDescription = row.getCell(3);
            HSSFCell cellAccountNo = row.getCell(4);

            AccountView2 jv = new AccountView2();
            jv.markNew();

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) != null) {
                throw new Exception("No Akun " + accountno + " sudah ada di Web ");
            }

            jv.setStNoper(cellNoper.getCellType() == cellNoper.CELL_TYPE_STRING ? cellNoper.getStringCellValue() : new BigDecimal(cellNoper.getNumericCellValue()).toString());
            jv.setStCostCenterCode(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            //jv.setStAccountID(GLUtil.getAccountByCode(accountno).getStAccountID());
            jv.setStAccountNo(accountno);
            jv.setStDescription(cellDescription.getCellType() == cellDescription.CELL_TYPE_STRING ? cellDescription.getStringCellValue() : new BigDecimal(cellDescription.getNumericCellValue()).toString());

            details.add(jv);

        }

        titipan.setDetails(details);

    }

    public AccountHeaderView getAccountHeader() {
        return (AccountHeaderView) DTOPool.getInstance().getDTO(AccountHeaderView.class, "ACCOUNT_" + stAccountHeader);
    }

    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stTypeCode);
    }

    public void createNewUpload() throws Exception {
        titipan = new JournalHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        setTitle("Upload Account");
    }

    public void uploadExcelNoper() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Noper");

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
            HSSFCell cellAccountNo = row.getCell(2);

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) == null) {
                //throw new Exception("No Akun " + accountno + " tidak ada di Web ");
                adaAkunKosong = true;
                listAkunKosong = listAkunKosong + "<br>" + accountno;
            }
        }

        if (adaAkunKosong) {

            throw new Exception("No Akun <br>" + listAkunKosong + " <br><br>belum ada di sistem Web ");
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

            HSSFCell cellNoper = row.getCell(1);
            HSSFCell cellAccountNo = row.getCell(2);
            HSSFCell cellAccountID = row.getCell(3);

            AccountView2 jv = new AccountView2();
            jv.markNew();

            accountno = cellAccountNo.getCellType() == cellAccountNo.CELL_TYPE_STRING ? cellAccountNo.getStringCellValue() : new BigDecimal(cellAccountNo.getNumericCellValue()).toString();

            if (GLUtil.getAccountByCode(accountno) == null) {
                throw new Exception("No Akun " + accountno + " belum ada di Web ");
            }

            jv.setStNoper(cellNoper.getCellType() == cellNoper.CELL_TYPE_STRING ? cellNoper.getStringCellValue() : new BigDecimal(cellNoper.getNumericCellValue()).toString());
            //jv.setStAccountID(GLUtil.getAccountByCode(accountno).getStAccountID());
            jv.setStAccountNo(accountno);
            jv.setStAccountID(cellAccountID.getCellType() == cellAccountID.CELL_TYPE_STRING ? cellAccountID.getStringCellValue() : new BigDecimal(cellAccountID.getNumericCellValue()).toString());

            details.add(jv);

        }

        titipan.setDetails(details);

    }

    public void doSaveNoper() throws Exception {

        getRemoteGeneralLedger().saveNoper(titipan, titipan.getDetails());

        super.close();

    }

}
