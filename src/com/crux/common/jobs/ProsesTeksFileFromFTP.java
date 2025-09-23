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
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.web.form.Form;
import com.webfin.FinCodec;
import com.webfin.datatext.model.DataTeksMasukLogView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.system.ftp.model.DataGatewayView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ProsesTeksFileFromFTP extends Form implements StatefulJob, RecordAudit, RecordBackup   {

    private final static transient LogManager logger = LogManager.getInstance(ProsesTeksFileFromFTP.class);

    private String downloadPath = Parameter.readString("SYS_FTP_DOWNLOAD_FOLDER");
    private String historyPath = Parameter.readString("SYS_FTP_HISTORY_FOLDER");
    private String logPath = Parameter.readString("SYS_FTP_LOG_FOLDER");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

                if(JobUtil.isServerProduction())
                        insertDataFromFTP();


        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void insertDataFromFTP() throws Exception {

        DTOList listGateway = null;

        //CARI FILE TEKS YANG BELUM DI PROSES
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
                        logger.logWarning("berhasil konek & login ke ftp : "+ gateway.getStDescription() +" ......");

                        // PINDAH KE FOLDER TEKS FILE DI FTP
                        boolean success = ftpClient.changeWorkingDirectory(gateway.getStDataDirectory());

                        if (success) {
                            logger.logWarning("sukses pindah ke folder pengajuan polis "+ gateway.getStDataDirectory() +" ......");
                        }

                        //CEK LIST FILE APA AJA YG ADA DI FOLDER
                        FTPFile[] listOfFiles = ftpClient.listFiles(ftpClient.printWorkingDirectory());

                        //LOOPING & INJECT DATA TEKS KE POSTGRESQL GATEWAY TABLE DATA_TEKS_MASUK
                        for (int i = 0; i < listOfFiles.length; i++) {
                            if (listOfFiles[i].isFile()) {

                                if(!listOfFiles[i].getName().toUpperCase().endsWith(".TXT")) continue;

                                logger.logWarning("dapat file teks : " + listOfFiles[i].getName() + " ......");
                                logger.logWarning(" folder : " + ftpClient.printWorkingDirectory());

                                fos = new FileOutputStream(downloadPath + listOfFiles[i].getName());
                                fosHistory = new FileOutputStream(historyPath + gateway.getStHistoryDirectory() + "/" + listOfFiles[i].getName());

                                boolean download = ftpClient.retrieveFile(listOfFiles[i].getName(), fos);
                                
                                //COPY FILE TEXT KE HISTORY SERVER GATEWAY UTK BACKUP
                                ftpClient.retrieveFile(listOfFiles[i].getName(), fosHistory);

                                if (download) 
                                {

                                        logger.logWarning("File downloaded successfully !");

                                        try {
                                                File file = new File(downloadPath + listOfFiles[i].getName());

                                                FileReader fileReader = new FileReader(file);
                                                BufferedReader bufferedReader = new BufferedReader(fileReader);

                                                //lock file hanya di 1 proses
//                                                FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
//
//                                                FileLock lock = channel.lock();

                                                String line;

                                                String groupID = String.valueOf(IDFactory.createNumericID("DATATEXTGRP"));

                                                //LOG DATA TEKS
                                                DataTeksMasukLogView log = new DataTeksMasukLogView();
                                                String logDescription = "";
                                                log.markNew();
                                                log.setStFileName(listOfFiles[i].getName());
                                                log.setDtProcessDate(new Date());
                                                log.setStCostCenterCode(gateway.getStCostCenterCode());
                                                log.setStRegionID(gateway.getStRegionID());

                                                int recordAmount = 0;
                                                int recordSuccess = 0;
                                                int recordFailed = 0;

                                                //CEK JUMLAH RECORD
                                                FileReader fileReaderCek = new FileReader(file);
                                                BufferedReader bufferedReaderCek = new BufferedReader(fileReaderCek);
                                                String lineCek;
                                                while ((lineCek = bufferedReaderCek.readLine()) != null){
                                                    recordAmount = recordAmount + 1;
                                                }

                                                //LOOPING INJECT DATA PER BARIS
                                                while ((line = bufferedReader.readLine()) != null) {
                                                    String[] data = line.split(";");
                                                    
                                                    DataTeksMasukView teks = new DataTeksMasukView();

                                                    //cek double data dulu
                                                    boolean sudahAda = sudahAdaData("no_rek_pinjaman", data[7].trim().toUpperCase());

                                                    //sudahAda = sudahAdaData("no_perjanjian_kredit", data[8].trim().toUpperCase());

                                                    if(sudahAda){
                                                        logDescription = logDescription + ""+data[3].trim()+" ("+ data[7].trim().toUpperCase() +") Gagal, Sudah Ada;";
                                                        continue;
                                                    }
 
                                                    //inject data
                                                    teks = prosesDataKredit(gateway, data, groupID);
                                                    
//                                                    if(data[0].trim().equalsIgnoreCase("59")) teks = prosesDataKredit(gateway, data, groupID);
//                                                    else continue;
                                                    
                                                    getRemoteInsurance().saveDataTeks(teks);
                                                    recordSuccess = recordSuccess + 1;
                                                    logDescription = logDescription + ""+data[3].trim()+" ("+ data[7].trim().toUpperCase() +") Sukses;";

                                                }
                                                
                                                recordFailed = recordAmount - recordSuccess;

                                                log.setStRecordAmount(String.valueOf(recordAmount));
                                                log.setStRecordSuccess(String.valueOf(recordSuccess));
                                                log.setStRecordFailed(String.valueOf(recordFailed));
                                                log.setStDescription(logDescription);
                                                 
                                                //save log & tulis ke dlm teks juga
                                                getRemoteInsurance().saveDataTeksLog(log);
                                                writeTeksFile(log);

                                                fos.close();
                                                fosHistory.close();
                                                fileReader.close();

//                                                //buka kuncian file teks
//                                                if( lock != null ) {
//                                                    lock.release();
//                                                }
//
//                                                // Close the file
//                                                channel.close();


                                                //HAPUS FILE TEXT DI LOCAL SERVER CORE
                                                boolean del = file.delete();

                                                if (del) {
                                                    logger.logWarning("delete file di core server " + listOfFiles[i].getName() + "...");
                                                }else{
                                                    logger.logWarning("gagal delete file di core server " + listOfFiles[i].getName() + "...");
                                                }

                                                logger.logWarning("INJECT DATA FILE : " + listOfFiles[i].getName());

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            logger.logWarning("Error baca file teks : "+ e.toString());
                                        }
                                } else {
                                    logger.logWarning("Error in downloading file !");
                                }

                                //DELETE FILE TEXT DI FOLDER PENAMPUNGAN SERVER GATEWAY
                                boolean delete = ftpClient.deleteFile(listOfFiles[i].getName());

                                if (delete) {
                                    logger.logWarning("delete file on FTP server " + listOfFiles[i].getName() + "...");
                                }

                            }
                        }

                        // logout the user, returned true if logout successfully
                        boolean logout = ftpClient.logout();
                        if (logout) {
                            logger.logWarning("Connection close...");
                        }

                    } else {
                        logger.logWarning("Connection fail...");
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

    DataTeksMasukView prosesDataKredit(DataGatewayView gateway, String[] data, String groupID)throws Exception{
            DataTeksMasukView teks = new DataTeksMasukView();

            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

            teks.markNew();

            teks.setStGroupID(groupID);
            teks.setStCostCenterCode(gateway.getStCostCenterCode());
            teks.setStRegionID(gateway.getStRegionID());
            teks.setStKodeBank(data[1].trim());
            
            String regionID = getRegionIDFromEntity(teks.getStKodeBank());
            
            if(regionID!=null)
                    teks.setStRegionID(regionID);

            teks.setStPolicyTypeID("59");
            teks.setStValid("Y");
            
            teks.setStJenisKredit(data[2].trim());
            teks.setStNama(data[3].trim());
            teks.setStJenisIdentitas(data[4].trim());
            teks.setStNomorIdentitas(data[5].trim());
            teks.setStJenisPekerjaan(data[6].trim());

            if(teks.getStJenisPekerjaan()==null)
                teks.setStValid("N");

            if(teks.getStJenisPekerjaan().equalsIgnoreCase(""))
                teks.setStValid("N");

            teks.setStNoRekeningPinjaman(data[7].trim());
            teks.setStNoPerjanjianKredit(data[8].trim());
            teks.setStUsia(data[9].trim());

            int usia = Integer.parseInt(data[9].trim());

            if(usia>80) teks.setStValid("N");
            if(usia<0) teks.setStValid("N");

            if(DateUtil.isValidDate("yyyyMMdd", data[10].trim()))
                 teks.setDtTanggalLahir(df2.parse(data[10].trim()));
            else
                 teks.setStValid("N");
 
            teks.setDtTanggalAwal(df2.parse(data[11].trim()));
            teks.setDtTanggalAkhir(df2.parse(data[12].trim()));
            teks.setDbInsuredAmount(new BigDecimal(data[13].trim()));

            BigDecimal premiBank = BDUtil.zero;

            if(!data[14].trim().equalsIgnoreCase("NULL"))
                premiBank = new BigDecimal(data[14].trim());

            teks.setDbPremiBank(premiBank);
            //teks.setStSPK(data[15].trim());
            teks.setDtTanggalTransfer(new Date());
            teks.setStKategori(FinCodec.PolicyStatus.POLICY);
            teks.setStStatus("TRANSFERED");
            teks.setStEntityID(getEntityID(teks.getStKodeBank()));

            if(data.length>16){
                //teks.setStKodeTarif(data[16].trim());
                teks.setStPaketCoverage(data[16].trim());
            }

            if(teks.getStEntityID()==null)
                teks.setStValid("N");

            teks.setStStatusKerja(getStatusKerja(gateway.getStCostCenterCode(), teks.getStJenisPekerjaan()));

            String statusKerjaByJenisKredit = getStatusKerjaByJenisKredit(gateway.getStCostCenterCode(), teks.getStJenisKredit());

            if(statusKerjaByJenisKredit!=null)
                teks.setStStatusKerja(statusKerjaByJenisKredit);

            String jenisKreditABAByJenisKredit = getJenisKreditAskridaByJenisKredit(gateway.getStCostCenterCode(), teks.getStJenisKredit());

            if(jenisKreditABAByJenisKredit!=null)
                teks.setStJenisKreditAskrida(jenisKreditABAByJenisKredit);
            
            teks.calculatePremi();

            return teks; 
    }

    public String getEntityID(String stKodeBank) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     ent_id " +
                    "   from " +
                    "         ent_master " +
                    "   where" +
                    "      ref_gateway_code = ?");

            S.setParam(1,stKodeBank);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getStatusKerja(String cabang, String pekerjaan) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     ref2 " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'WORKING_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,cabang);
            S.setParam(2,pekerjaan);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getStatusKerjaByJenisKredit(String cabang, String jenisKredit) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     pekerjaan " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'BANKCREDIT_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,cabang);
            S.setParam(2,jenisKredit);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }

    private void writeTeksFile(DataTeksMasukLogView log){
        try {

			String content = log.getStFileName()+";"+log.getStCostCenterCode()+";"+log.getStRecordAmount()+";"+
                                         log.getStRecordSuccess()+";"+log.getStRecordFailed();

			File file = new File("//"+ logPath +"/FTP/FTP"+ log.getStCostCenterCode() +"/data_text/log/log_"+ log.getStFileName());

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public boolean sudahAdaData(String keyFilter, String keyValue) throws Exception{

        final SQLUtil S = new SQLUtil("GATEWAY");

        try {

                boolean sudahAda = false;
                final PreparedStatement PS = S.setQuery("select "+ keyFilter +
                                                        " from data_teks_masuk "+
                                                        " where "+ keyFilter +" = ?");
                PS.setString(1,keyValue);

                final ResultSet RS = PS.executeQuery();

                if (RS.next()) {
                    sudahAda = true;
                }

                return sudahAda;

            }finally{
                S.release();
            }
    }

    public String getRegionIDFromEntity(String stKodeBank) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     region_id " +
                    "   from " +
                    "         ent_master " +
                    "   where" +
                    "      ref_gateway_code = ?");

            S.setParam(1,stKodeBank);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public String getJenisKreditAskridaByJenisKredit(String cabang, String jenisKredit) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            S.setQuery(
                    "   select " +
                    "     jenis_kredit " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'BANKCREDIT_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,cabang);
            S.setParam(2,jenisKredit);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public boolean isServerProduction(){
        String hostname = "Unknown";
        boolean isServer = false;

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

            System.out.println("Hostname : "+ hostname);
            System.out.println("ip : "+ addr.getHostAddress());

            if(addr.getHostAddress().equalsIgnoreCase("192.168.250.53"))
                isServer = true;
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }

        return isServer;
    }
    
}
