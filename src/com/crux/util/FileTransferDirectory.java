/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Dec 7, 2004
 * Time: 3:01:33 PM
 * To change this template use Options | File Templates.
 */
package com.crux.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FileTransferDirectory {

    /* Change these settings before running this class. */
    /** The file to be copied. */
    public static final String INPUT_FILE = "D:\\Kalkulator\\Kalkulator DIY\\Daftar Peserta PHK.xls";
    /**
    The name of the copy to be created by this class.
    If this file doesn't exist, it will be created, along with any
    needed parent directories.
     */
    //public static final String COPY_FILE_TO = "D:\\data\\rpp_rekap_cab_012017_012017_tempxxx.pdf";
    public static final String COPY_FILE_TO = "W:\\statistik\\Daftar Peserta PHK.xls";

    /** Run the example. */
    public static void main(String[] args) throws IOException {
        File source = new File(INPUT_FILE);
        File target = new File(COPY_FILE_TO);
        FileTransferDirectory test = new FileTransferDirectory();
        //test.copyWithChannels(source, target, false);
        test.copyWithStreams(source, target, false);
        log(INPUT_FILE);
        log(COPY_FILE_TO);
        log("Done.");
    }

    public void copyWithStreams(File aSourceFile, File aTargetFile, boolean aAppend) {
        log("Copying files with streams.");
        ensureTargetDirectoryExists(aTargetFile.getParentFile());
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            try {
                byte[] bucket = new byte[32 * 1024];
                inStream = new BufferedInputStream(new FileInputStream(aSourceFile));
                outStream = new BufferedOutputStream(new FileOutputStream(aTargetFile, aAppend));
                int bytesRead = 0;
                while (bytesRead != -1) {
                    bytesRead = inStream.read(bucket); //-1, 0, or more
                    if (bytesRead > 0) {
                        outStream.write(bucket, 0, bytesRead);
                    }
                }
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            }
        } catch (FileNotFoundException ex) {
            log("File not found: " + ex);
        } catch (IOException ex) {
            log(ex);
        }
    }

    private void ensureTargetDirectoryExists(File aTargetDir) {
        if (!aTargetDir.exists()) {
            aTargetDir.mkdirs();
        }
    }

    private static void log(Object aThing) {
        System.out.println(String.valueOf(aThing));
    }
}
