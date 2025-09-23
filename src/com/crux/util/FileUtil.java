/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.crux.util;

import com.crux.common.parameter.Parameter;
import com.crux.file.FileView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author doni
 */
public class FileUtil {

    private final static transient LogManager logger = LogManager.getInstance(FileUtil.class);

     public static String saveDocument(File doc, String fileName) throws Exception{
            //simpan file nya
           String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

           String sf = sdf.format(new Date());

           String tempPath = fileFOlder+File.separator+sf;
           String path1 = fileFOlder+File.separator;

           try {
              new File(path1).mkdir();
              new File(tempPath).mkdir();
           } catch (Exception e) {
           }

           File source = doc;

           String destination = tempPath+File.separator+System.currentTimeMillis();

           File dest = new File(destination);

            if (source.exists() && source.isFile()){
                //COPY FILE softcopy nya
                //Files.copy(stream, destination);
                copyFileUsingApacheCommonsIO(source, dest,destination);
            }

           long bytes = doc.length();

           logger.logWarning("size file : "+ bytes);

            //simpen ke tabel s_file
            FileView file = new FileView();

            file.markNew();
            file.setStOriginalName(doc.getName());
            file.setDbFileSize(new BigDecimal(bytes));
            file.setDtFileDate(new Date());
            file.setStMimeType("application/pdf");
            file.setDbOriginalSize(new BigDecimal(bytes));
            file.setStDescription(fileName);
            file.setStImageFlag("N");
            file.determineFileType();

            file.setStFilePath(destination);

            file.store();

            return file.getStFileID(); 
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest,String pathDest) throws IOException {

            try {
                    int index = pathDest.lastIndexOf("\\")+ 1 ;
                    new File(pathDest.substring(0, index)).mkdir();
                    System.out.println ("bikin folder : "+ pathDest.substring(0, index));

               } catch (Exception e) {
               }

            FileUtils.copyFile(source, dest);

    }

}
