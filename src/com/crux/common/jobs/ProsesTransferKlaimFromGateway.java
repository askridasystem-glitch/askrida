/*
 * AHMAD RHODONI
 *
 * Created on 20-10-2014
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.crux.common.jobs;

import com.crux.common.model.RecordAudit;
import com.crux.common.model.RecordBackup;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.IDFactory;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.LookUpUtil;
import com.crux.util.SQLUtil;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsurancePolicyItemsView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyTSIView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.system.ftp.model.DataGatewayView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author doni
 */

public class ProsesTransferKlaimFromGateway extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesPengajuanPolis.class);

    private String downloadPath = Parameter.readString("SYS_FTP_DOWNLOAD_FOLDER");
    private String historyPath = Parameter.readString("SYS_FTP_HISTORY_FOLDER");
    private String stClaimObject;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                //insertDataFromFTP();
                if(JobUtil.isServerProduction())
                        prosesDataMenjadiLKS();

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void insertDataFromFTP() throws Exception {

        DTOList listGateway = null;

        //CARI KUMPULAN GRUP DOKUMEN YANG BELUM DI PROSES
        listGateway = ListUtil.getDTOListFromQuery(
                " select * "+
                "     from s_data_gateway "+
                "     where active_flag = 'Y' "+
                "     order by gateway_id",
               DataGatewayView.class);

        for (int h = 0; h < listGateway.size(); h++) {
                DataGatewayView gateway = (DataGatewayView) listGateway.get(h);

                FTPClient ftpClient = new FTPClient();
                FileOutputStream fos = null;
                FileOutputStream fosHistory = null;

                try {

                    // INISIALISASI & CONNECT KE FTP
                    ftpClient.connect(gateway.getStFTPAddress(),Integer.parseInt(gateway.getStFTPPort()));

                    // pass username and password, returned true jika sukses
                    boolean login = ftpClient.login(gateway.getStFTPUserID(), gateway.getStFTPPassword());

                    if (login) {
                        System.out.println("berhasil konek & login ke ftp : "+ gateway.getStDescription() +" ......");

                        // PINDAH KE FOLDER TEKS FILE DI FTP
                        boolean success = ftpClient.changeWorkingDirectory(gateway.getStDataClaimDirectory());

                        if (success) {
                            System.out.println("sukses pindah ke folder claim "+ gateway.getStDataClaimDirectory() +" ......");
                        }

                        //CEK LIST FILE APA AJA YG ADA DI FOLDER
                        FTPFile[] listOfFiles = ftpClient.listFiles(ftpClient.printWorkingDirectory());

                        //LOOPING & INJECT DATA TEKS KE POSTGRESQL CORE TABLE DATA_TEKS_MASUK
                        for (int i = 0; i < listOfFiles.length; i++) {
                            if (listOfFiles[i].isFile()) {

                                if(!listOfFiles[i].getName().toUpperCase().endsWith(".TXT")) continue;

                                System.out.println("DAPET FILE : " + listOfFiles[i].getName() + " ......");
                                System.out.println(" folder : " + ftpClient.printWorkingDirectory());

                                fos = new FileOutputStream(downloadPath + listOfFiles[i].getName());
                                fosHistory = new FileOutputStream(historyPath + gateway.getStHistoryDirectory() + "/" + listOfFiles[i].getName());

                                boolean download = ftpClient.retrieveFile(listOfFiles[i].getName(), fos);
                                
                                //COPY FILE TEXT KE HISTORY SERVER GATEWAY UTK BACKUP
                                ftpClient.retrieveFile(listOfFiles[i].getName(), fosHistory);

                                if (download) 
                                {

                                        System.out.println("File downloaded successfully !");

                                        try {
                                                File file = new File(downloadPath + listOfFiles[i].getName());

                                                FileReader fileReader = new FileReader(file);
                                                BufferedReader bufferedReader = new BufferedReader(fileReader);
                                                String line;

                                                String groupID = String.valueOf(IDFactory.createNumericID("DATATEXTGRP"));

                                                //LOOPING INJECT DATA PER BARIS
                                                while ((line = bufferedReader.readLine()) != null) {
                                                    String[] data = line.split(";");

                                                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                                    DataTeksMasukView teks = new DataTeksMasukView();

                                                    teks.markNew();

                                                    teks.setStGroupID(groupID);
                                                    teks.setStCostCenterCode(gateway.getStCostCenterCode());
                                                    teks.setStRegionID(gateway.getStRegionID());
                                                    teks.setStPolicyTypeID(data[0]);
                                                    teks.setStKategori(FinCodec.PolicyStatus.CLAIM);
                                                    teks.setStPolicyNo(data[1]);
                                                    teks.setStNama(data[2]);
                                                    teks.setDtTanggalLahir(df2.parse(data[5]));
                                                    teks.setStUsia(data[4]);
                                                    teks.setDbClaimAmount(new BigDecimal(data[6]));
                                                    teks.setDtTanggalProses(new Date());

                                                    getRemoteInsurance().saveDataTeks(teks);

                                                }

                                                fos.close();
                                                fosHistory.close();
                                                fileReader.close();

                                                //HAPUS FILE TEXT DI LOCAL SERVER CORE
                                                boolean del = file.delete();

                                                if (del) {
                                                    System.out.println("delete file di core server " + listOfFiles[i].getName() + "...");
                                                }else{
                                                    System.out.println("gagal delete file di core server " + listOfFiles[i].getName() + "...");
                                                }

                                                System.out.println("INJECT DATA FILE : " + listOfFiles[i].getName());

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            logger.logWarning("Error baca file teks : "+ e.toString());
                                        }
                                } else {
                                    System.out.println("Error in downloading file !");
                                }

                                //DELETE FILE TEXT DI FOLDER PENAMPUNGAN SERVER GATEWAY
                                boolean delete = ftpClient.deleteFile(listOfFiles[i].getName());

                                if (delete) {
                                    System.out.println("delete file on FTP server " + listOfFiles[i].getName() + "...");
                                }

                            }
                        }

                        // logout the user, returned true if logout successfully
                        boolean logout = ftpClient.logout();
                        if (logout) {
                            System.out.println("Connection close...");
                        }

                    } else {
                        System.out.println("Connection fail...");
                        logger.logWarning("Koneksi ke ftp server gagal : "+ gateway.getStFTPUserID());
                    }

                } catch (SocketException e) {
                        e.printStackTrace();
                        logger.logWarning("error socket ftp : "+ e.toString());
                } catch (IOException e) {
                        e.printStackTrace();
                        logger.logWarning("error io ftp : "+ e.toString());
                } finally {
                    try {
                            ftpClient.disconnect();
                    } catch (IOException e) {
                            e.printStackTrace();
                    }
                }
        }
        
    }

    private void prosesDataMenjadiLKS() throws Exception{

        final SQLUtil S = new SQLUtil();

        DTOList listLKS = null;

        //CARI LKS YANG SUDAH DI SETUJUI DI OLEH BANK
        listLKS = ListUtil.getDTOListFromQuery(
                "select * from ins_policy "+
                " where claim_status = 'PLA' and effective_flag = 'Y' order by pol_id",
                InsurancePolicyView.class);

        for (int i = 0; i < listLKS.size(); i++) {
            InsurancePolicyView lks = (InsurancePolicyView) listLKS.get(i);

            System.out.println("Bikin LKS no : "+ lks.getStPLANo());
            
            editCreateClaimPLA(lks.getStPolicyID());

            //UPDATE DATA TEKS MASUK YANG SUDAH DI PROSES
//            PreparedStatement PS = S.setQuery("update data_teks_masuk set proses_flag = 'Y' where data_id = ?");
//
//            PS.setObject(1, data.getStDataID());
//
//            int j = PS.executeUpdate();
//
//            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS GROUP : "+ data.getStGroupID() +" ++++++++++++++++++");

            
        }

    }


    private InsurancePolicyView policy;

   

    private InsurancePolicyObjDefaultView selectedDefaultObject;

    public InsurancePolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;
    }

    public void setSelectedDefaultObject(InsurancePolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

    /**
     * @return the policy
     */
    public InsurancePolicyView getPolicy() {
        return policy;
    }

    /**
     * @param policy the policy to set
     */
    public void setPolicy(InsurancePolicyView policy) {
        this.policy = policy;
    }

    public void editCreateClaimPLA(String policyID) throws Exception {
        superEdit(policyID);

        checkActiveEffective();

        final boolean blockPremiNotPaid = Parameter.readBoolean("BLOCK_CLAIM_PREMINOTPAID");


        if(policy.isStatusClaimPLA() || policy.isStatusClaimDLA()){
            throw new RuntimeException("PLA Can Only Be Created From Polis");
        }

        if (!policy.isEffective()) {
            throw new RuntimeException("Polis Belum Disetujui");
        }

        policy.setStClaimStatus(FinCodec.ClaimStatus.PLA);
        policy.setStNextStatus(FinCodec.PolicyStatus.CLAIM);
        policy.setStDocumentBranchingFlag("Y");
        policy.setStPostedFlag("N");
        policy.setStEffectiveFlag("N");
        policy.setStActiveClaimFlag("Y");
        policy.setStCoverNoteFlag("N");
        policy.setStApprovedWho(null);
        policy.setDtApprovedDate(null);
        //policy.setStReference3(null);
        policy.setStReference12(null);
        policy.setStClientIP(null);
        policy.setStPassword(null);
        policy.setStReadyToApproveFlag(null);

        final DTOList objects = policy.getObjects();


        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

            policy.invokeCustomCriteria(obj);

            if(objx.getStOrderNo()==null){

                objx.setStOrderNo(String.valueOf(i+1));

                SQLUtil S = new SQLUtil();

                PreparedStatement PS = S.setQuery("update ins_pol_obj set order_no = "+ objx.getStOrderNo() +" where ins_pol_obj_id = ?");

                PS.setObject(1,obj.getStPolicyObjectID());

                int j = PS.executeUpdate();

                S.release();
            }


        }

//        final DTOList claimItems = policy.getClaimItems();
//
//        if (claimItems.size()==0) {
//
//            String insItemID = getClaimBrutoInsItemID();
//
//            final InsurancePolicyItemsView item = onNewClaimItem(insItemID);
//
//            item.setStChargableFlag("Y");
//
//        }

        //PILIH OBJEK KLAIM
        //selectClaimObject();
 
        getPolicy().recalculate();

        //simpen
        getRemoteInsurance().saveLKSToGateway(policy,policy.getStNextStatus(),false);


    }

    
    public void selectClaimObject() throws Exception {

        InsurancePolicyObjectView o = (InsurancePolicyObjectView) policy.getObjects().get(Integer.parseInt(getStClaimObject()));

        boolean canNotClaim = policy.validateClaimObject(o);

        if(canNotClaim){
            setStClaimObject(null);
            policy.setDbClaimAmountEstimate(BDUtil.zero);
            throw new RuntimeException("TSI objek klaim sudah nol, tidak bisa dilakukan pembuatan klaim");
        }

        if(!canNotClaim){
            policy.setStClaimObjectID(o.getStPolicyObjectID());

            policy.setObjIndex(getStClaimObject());

            final DTOList tsi = o.getSuminsureds();

            BigDecimal totalTSI = null;
            for (int i = 0; i < tsi.size(); i++) {
                InsurancePolicyTSIView suminsured = (InsurancePolicyTSIView) tsi.get(i);

                totalTSI = BDUtil.add(totalTSI, suminsured.getDbInsuredAmount());
            }

            policy.setDbClaimAmountEstimate(totalTSI);
        }

    }
    

    public InsurancePolicyItemsView onNewClaimItem(String insItemID) throws Exception {
        if (insItemID == null) throw new Exception("Please select item to be added");

        final InsurancePolicyItemsView item = new InsurancePolicyItemsView();

        item.setStItemClass(FinCodec.PolicyItemClass.CLAIM);

        item.markNew();

        item.setStInsItemID(insItemID);

        item.setStChargableFlag("Y");

        getClaimItems().add(item);

        return item;
    }

    public DTOList getClaimItems() {
        return policy.getClaimItems();
    }

    public void superEdit(String policyID) throws Exception {
        view(policyID);

        super.setReadOnly(false);

        policy.setObjects(null);

        policy.markUpdateO();

        final boolean inward = policy.getCoverSource().isInward();

        final DTOList objects = policy.getObjects();

        for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            obj.getSuminsureds().markAllUpdate();
            obj.getDeductibles().markAllUpdate();
            obj.getCoverage().markAllUpdate();

            obj.markUpdate();

            obj.getTreaties().markAllUpdate();

            final DTOList treatyDetails = obj.getTreatyDetails();
            treatyDetails.markAllUpdate();

            for (int j = 0; j < treatyDetails.size(); j++) {
                InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                trdi.getShares().markAllUpdate();
            }

            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null){
                if(policy.getDtPeriodStart()!=null)
                    obj.setStInsuranceTreatyID(policy.getInsuranceTreatyID(policy.getDtPeriodStart()));
                else
                    obj.setStInsuranceTreatyID(Parameter.readString(inward?"UWRIT_DEF_ITREATY":"UWRIT_DEF_OTREATY"));
                }

            if(policy.isStatusPolicy()||policy.isStatusClaim()||policy.isStatusEndorse()||policy.isStatusRenewal()||policy.isStatusHistory()||policy.isStatusEndorseRI()||policy.isStatusTemporaryPolicy()||policy.isStatusTemporaryEndorsemen())
                if (obj.getStInsuranceTreatyID()==null) throw new RuntimeException("Unable to find suitable treaty for this policy !!");

        }

        policy.getCoins2().markAllUpdate();
        policy.getDetails().markAllUpdate();
        policy.getClaimItems().markAllUpdate();
        policy.getDeductibles().markAllUpdate();
        policy.getInstallment().markAllUpdate();
        policy.getCoinsCoverage().markAllUpdate();

        policy.showItemsAccount();

    }

    public void view(String policyID) throws Exception {

        if (policyID==null) throw new RuntimeException("ID Data Belum Dipilih");

        policy = getInsurancePolicy(policyID);

        if (policy==null) throw new RuntimeException("Policy not found");

        policy.loadObjects();
        policy.loadClausules();
        policy.loadEntities();
        policy.loadDetails();
        policy.loadCoins();
        policy.loadInstallment();

        super.setReadOnly(true);

    }

    private void checkActiveEffective() {
        if (!policy.isEffective())
            throw new RuntimeException("Please approve the document");

        if (!policy.isActive())
            throw new RuntimeException("Document is not active, please refer to the last active document");
    }

    private DTOList historypolicy;
    
    public DTOList getHistoryPolicy(String stPolicyNo) {
        loadHistoryPolicy(stPolicyNo);
        return historypolicy;
    }

    public void loadHistoryPolicy(String stPolicyNo) {
        try {
            if (historypolicy == null)
                historypolicy = ListUtil.getDTOListFromQuery(
                        " select * "+
                        " from ins_policy "+
                        " where status in ('POLICY','RENEWAL','ENDORSE') and effective_flag = 'Y' and substr(pol_no,0,17) = ? order by pol_id desc limit 1",
                        new Object[]{stPolicyNo.length()>16?stPolicyNo.substring(0,16)+"%":stPolicyNo+"%"},
                        InsurancePolicyView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InsurancePolicyView getInsurancePolicy(String stPolicyID) throws Exception {
        final InsurancePolicyView pol = (InsurancePolicyView) ListUtil.getDTOListFromQuery("select * from ins_policy where pol_id = ?",
                new Object[]{stPolicyID},
                InsurancePolicyView.class).getDTO();

        return pol;
    }

    private String getClaimBrutoInsItemID() throws Exception {

        final LookUpUtil lu = ListUtil.getLookUpFromQuery("select ins_item_id,ins_item_id from ins_items where item_type='CLAIMG' and active_flag='Y'");

        return lu.getCode(0);
    }

    /**
     * @return the stClaimObject
     */
    public String getStClaimObject() {
        return stClaimObject;
    }

    /**
     * @param stClaimObject the stClaimObject to set
     */
    public void setStClaimObject(String stClaimObject) {
        this.stClaimObject = stClaimObject;
    }


}
