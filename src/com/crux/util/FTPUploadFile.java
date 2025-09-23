/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Dec 7, 2004
 * Time: 3:01:33 PM
 * To change this template use Options | File Templates.
 */
package com.crux.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class FTPUploadFile {

    private static String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
    private static int port = 21;
    //example1
//    private static String host = "192.168.200.19";
//    private static String user = "dinal";
//    private static String pass = "askrida00";
//    private static String file = "D:/fin-repository/report/rpt_kas_perApril2017.pdf";
//    private static String upload = "/rpt_kas_perApril2017.pdf";
//    private static final int BUFFER_SIZE = 4096;
    //example2
//    private static String file = "D:/fin-repository/report/inwardrecap_Januari2017_Mei2017.pdf";
//    private static String upload = "inwardrecap_Januari2017_Mei2017.pdf";
    //example3
    private static String host = "192.168.250.74";
    private static String user = "statistik"; //"akuntansi"; //"statistik";
    private static String pass = "St@t1st!k234"; //"Akunt@n$1234"; //"St@t1st!k234";
    private static String file = "D:/exportdb/statistik/klaim_statistik_jan21.csv";
    private static String upload = "/laporan/klaim_statistik_jan21.csv";
    private static final int BUFFER_SIZE = 4096;

//    public static void main(String[] args) {
//
//        copyWithFTP(file, upload);
//
//    }
//
//    public static void copyWithFTP(String filePath, String uploadPath) {
//
//        ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
//        System.out.println("Upload URL: " + ftpUrl);
//
//        try {
//            URL url = new URL(ftpUrl);
//            URLConnection conn = url.openConnection();
//            OutputStream outputStream = conn.getOutputStream();
//            FileInputStream inputStream = new FileInputStream(filePath);
//
//            byte[] buffer = new byte[BUFFER_SIZE];
//            int bytesRead = -1;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            inputStream.close();
//            outputStream.close();
//
//            System.out.println("File uploaded");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
    public static void main(String[] args) {

        copyWithFTP(file, upload);
    }

    public static void copyWithFTP(String filePath, String uploadPath) {

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(file);

            String firstRemoteFile = upload;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

            // APPROACH #2: uploads second file using an OutputStream
//            File secondLocalFile = new File("E:/Test/Report.doc");
//            String secondRemoteFile = "test/Report.doc";
//            inputStream = new FileInputStream(secondLocalFile);
//
//            System.out.println("Start uploading second file");
//            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
//            byte[] bytesIn = new byte[4096];
//            int read = 0;
//
//            while ((read = inputStream.read(bytesIn)) != -1) {
//                outputStream.write(bytesIn, 0, read);
//            }
//            inputStream.close();
//            outputStream.close();
//
//            boolean completed = ftpClient.completePendingCommand();
//            if (completed) {
//                System.out.println("The second file is uploaded successfully.");
//            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void copyWithFTPUser(String user, String pass, String filePath, String uploadPath) {

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(filePath);

            String firstRemoteFile = uploadPath;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
