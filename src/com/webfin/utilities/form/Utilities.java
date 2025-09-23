/***********************************************************************
 * Module:  com.webfin.manual.form.PrintManualBook
 * Author:  Achmad Rhodoni
 * Created: 18 Mei 2009
 * Purpose: 
 ***********************************************************************/

package com.webfin.utilities.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.LogManager;
import com.crux.util.StringTools;
import com.crux.util.crypt.Crypt;
import com.crux.web.form.Form;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class Utilities extends Form {
    private final static transient LogManager logger = LogManager.getInstance(Utilities.class);
    
    
   private String stManualBookTypeID;
   private String stEncryptedCode;
   private String stDecryptedCode;
   private String stDecryptedCode2;
   
   public void setStManualBookTypeID(String stManualBookTypeID){
   	this.stManualBookTypeID = stManualBookTypeID;
   }
   
   public String getStManualBookTypeID(){
   	return stManualBookTypeID;
   }

   public void initialize() {
      setTitle("ENCRYPT CODE");
   }
   
   public void onChangeManualBookType() {

   }

   public void btnPrint() {
//      LOVManager.getInstance().getLOV(getDocLOVName(), null).getComboDesc(stFileID);
	   if(stManualBookTypeID!=null){
	   		super.redirect("/pages/utilities/"+ stManualBookTypeID);
	   }else{
	   		throw new RuntimeException("Jenis Panduan Belum Dipilih");
	   }
   }
   
   private String stFilePhysic;
   
   public String getStFilePhysic() {
        return stFilePhysic;
    }
    
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
    
    private String stMethod;
   
    public String getStMethod() {
        return stMethod;
    }
    
    public void setStMethod(String stMethod) {
        this.stMethod = stMethod;
    }
    
    public void execute() throws Exception{
        if(stMethod==null)
            throw new RuntimeException("Method Belum Diisi");
        
        final Method m = this.getClass().getMethod(stMethod,null);
            
        m.invoke(this, null);
    }
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

    
    public void reaproveByExcel()throws Exception{
        
        final String fileID = getRemoteInsurance().getID();
        
        FileView file = FileManager.getInstance().getFile(fileID);
        
        FileInputStream fis = new FileInputStream(file.getStFilePath());
        
        reapproveWithExcel(fis);
        
    }
    
    private void reapproveWithExcel(FileInputStream fis)throws Exception{
        try{
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("sheet");
            
            if(sheet==null)
                throw new RuntimeException("Sheet sheet Tidak Ditemukan !");
            
            int rows  = sheet.getPhysicalNumberOfRows();
            
            for (int r = 0; r < rows; r++){
                HSSFRow row   = sheet.getRow(r);
                
                HSSFCell cellPolId  = row.getCell(Short.parseShort("0"));//pol_id
                
                if(cellPolId.getCellType()!=cellPolId.CELL_TYPE_STRING){
                    double polidDouble = cellPolId.getNumericCellValue();
                    BigDecimal polidBD = new BigDecimal(polidDouble);
                    String polid = polidBD.toString();
                    clickReApprovalByPolicyID(polid);
                }else if(cellPolId.getCellType()!=cellPolId.CELL_TYPE_NUMERIC){
                    clickReApprovalByPolicyID(cellPolId.getStringCellValue());
                }
            }
        }catch (Exception e) {
            throw new RuntimeException("error reapprove using excel = "+ e);
        }
    }
    
    public void clickReApprovalByPolicyID(String pol_id) throws Exception {
        
        final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol_id);
        
        //reverse dulu
        final InsurancePolicyView polis = (InsurancePolicyView) pol;
        getRemoteInsurance().reverse(polis);
        
        //ubah treaty n simpan
        
        final InsurancePolicyView pol2 = getRemoteInsurance().getInsurancePolicy(pol_id);
               
        pol2.setObjects(null);
        
        pol2.markUpdateO();

        final boolean inward = pol2.getCoverSource().isInward();
        
        final DTOList objects = pol2.getObjects();
        
        for (int j = 0; j < objects.size(); j++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
            
            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();
            
            obj.markUpdate();
            
            obj.getTreaties().markAllUpdate();
            
            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();
            
            for (int k = 0; k < treatyDetails.size(); k++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);
                
                final DTOList share = trdi.getShares();
                trdi.getShares().markAllUpdate();
            }
            
             obj.setStInsuranceTreatyID(null);
             obj.getTreaties().deleteAll();
            
             if(polis.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(polis.getInsuranceTreatyID(polis.getDtPeriodStart()));
            
        }    
        
        pol2.getCoins2().markAllUpdate();
        pol2.getDetails().markAllUpdate();
        pol2.getClaimItems().markAllUpdate();
        pol2.getDeductibles().markAllUpdate();
        pol2.getInstallment().markAllUpdate();
        
        pol2.reCalculateInstallment();
        pol2.recalculate();
        pol2.recalculateTreatyReverse();
        
        getRemoteInsurance().save(pol2, pol2.getStNextStatus(), false);
        
        final InsurancePolicyView pol3 = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol_id);
        //simpan sekaligus approval
        pol3.markUpdate();
        pol3.setStPostedFlag("Y");
        pol3.setStEffectiveFlag("Y");
        getRemoteInsurance().approveAfterReverse(pol3, pol3.getStNextStatus(), true);
        
    }

    /**
     * @return the stEncryptedCode
     */
    public String getStEncryptedCode() {
        return stEncryptedCode;
    }

    /**
     * @param stEncryptedCode the stEncryptedCode to set
     */
    public void setStEncryptedCode(String stEncryptedCode) {
        this.stEncryptedCode = stEncryptedCode;
    }

    /**
     * @return the stDecryptedCode
     */
    public String getStDecryptedCode() {
        return stDecryptedCode;
    }

    /**
     * @param stDecryptedCode the stDecryptedCode to set
     */
    public void setStDecryptedCode(String stDecryptedCode) {
        this.stDecryptedCode = stDecryptedCode;
    }

    public void decrypt()throws Exception{
//        byte[] de = decrypt(generateMD5Key(generateKey(DEFAULT_PASS_KEY)), en);
//		System.out.println("decrypted : " + new String(de));
        stDecryptedCode =  Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes(stEncryptedCode)));
    }

    /**
     * @return the stDecryptedCode2
     */
    public String getStDecryptedCode2() {
        return stDecryptedCode2;
    }

    /**
     * @param stDecryptedCode2 the stDecryptedCode2 to set
     */
    public void setStDecryptedCode2(String stDecryptedCode2) {
        this.stDecryptedCode2 = stDecryptedCode2;
    }

}
