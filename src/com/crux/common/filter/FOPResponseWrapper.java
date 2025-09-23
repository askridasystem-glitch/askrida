/***********************************************************************
 * Module:  com.crux.common.filter.FOPResponseWrapper
 * Author:  Denny Mahendra
 * Created: Apr 9, 2006 11:29:54 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.common.parameter.Parameter;
//import org.apache.fop.apps.Driver;
import com.crux.file.FileView;
import com.crux.util.IDFactory;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import org.apache.avalon.framework.logger.Logger;

import com.crux.util.LogManager;
import com.crux.util.PDFMerge;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;
//import org.apache.fop.messaging.MessageHandler;

public class FOPResponseWrapper extends HttpServletResponseWrapper {
   private HttpServletResponse orig;
   private FOPStream fopStream;
   private PrintWriter pw;
   static int ser=1234;
   //private FopFactory fopFactory = FopFactory.newInstance();
   Logger log = null;
   private boolean hideLogger = false;
   private boolean lockPDF = true;
   private HttpServletRequest req;

   private boolean lockPrintPDF = SessionManager.getInstance().getSession().hasResource("USER_LOCK_PRINT_PDF");

   private final static transient LogManager logger = LogManager.getInstance(FOPResponseWrapper.class);

   public FOPResponseWrapper(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
      super(httpServletResponse);

      orig = httpServletResponse;

      req = httpServletRequest;

   }

   public void addHeader(String s, String s1) {
   }

   public void setHeader(String s, String s1) {
   }

   public void setContentType(String s) {
   }

   public void setContentLength(int i) {
   }

   public void finishAll() {
      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();

         if (pw!=null)
            pw.flush();
         
         if (fopStream!=null)
            fopStream.flush();

         final byte[] stx = fopStream.getStream().toByteArray();

         logger.logDebug("finish: input doc is "+stx.length+" bytes");

         final ByteArrayInputStream bis = new ByteArrayInputStream(stx);
         
         //Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out); 
         
         Driver driver = new Driver(new InputSource(bis), out);
         
         if(hideLogger){
             Logger logg = new ConsoleLogger(ConsoleLogger.LEVEL_FATAL);
             MessageHandler.setScreenLogger(logg);
             driver.setLogger(logg);
         }

        driver.setRenderer(Driver.RENDER_PDF);
        
        if(isLockPDF()){
            if(Parameter.readString("PDF_PASSWORD")!=null){
                Map rendererOptions = new java.util.HashMap();
                rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                //rendererOptions.put("allowCopyContent", "FALSE");
                rendererOptions.put("allowEditContent", "FALSE");

                if(lockPrintPDF){
                    rendererOptions.put("allowPrint", "FALSE");
                }
                
                driver.getRenderer().setOptions(rendererOptions);
            }
        }

         try {
            driver.run();
         } catch (Exception e) {
            logger.logDebug("finish: XML Doc : \n"+new String(stx));
            throw e;
         }

         byte[] content = out.toByteArray();

         logger.logDebug("finish: streaming pdf ("+content.length+" bytes)");

         orig.setContentType("application/pdf");
         orig.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
         orig.setContentLength(content.length);
         orig.getOutputStream().write(content);
         orig.getOutputStream().flush();

         fopStream=null;
         orig=null;

      } catch (Exception e) {
         throw new RuntimeException(e);
      } 
   }

   public ServletOutputStream getOutputStream() throws IOException {

      if (fopStream==null)
         fopStream = new FOPStream();

      return fopStream;
   }

   public PrintWriter getWriter() throws IOException {

      if (pw==null)
         pw = new PrintWriter(getOutputStream());

      return pw;
   }

   public static class FOPStream extends ServletOutputStream {
      private ByteArrayOutputStream bos;

      public FOPStream() {
         bos = new ByteArrayOutputStream();
      }

      public void write(int b) throws IOException {
         bos.write((byte)b);
      }

      public ByteArrayOutputStream getStream() {
         return bos;
      }
   }

    public boolean isLockPDF() {
        return lockPDF;
    }

    public void setLockPDF(boolean lockPDF) {
        this.lockPDF = lockPDF;
    }

    public void finish2() {
      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();

         if (pw!=null)
            pw.flush();

         if (fopStream!=null)
            fopStream.flush();

         final byte[] stx = fopStream.getStream().toByteArray();

         logger.logDebug("finish: input doc is "+stx.length+" bytes");

         final ByteArrayInputStream bis = new ByteArrayInputStream(stx);

         Driver driver = new Driver(new InputSource(bis), out);

         if(hideLogger){
             Logger logg = new ConsoleLogger(ConsoleLogger.LEVEL_FATAL);
             MessageHandler.setScreenLogger(logg);
             driver.setLogger(logg);
         }

        driver.setRenderer(Driver.RENDER_PDF);

        if(isLockPDF()){
            if(Parameter.readString("PDF_PASSWORD")!=null){
                Map rendererOptions = new java.util.HashMap();
                rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                //rendererOptions.put("allowCopyContent", "FALSE");
                rendererOptions.put("allowEditContent", "FALSE");

                if(lockPrintPDF){
                    rendererOptions.put("allowPrint", "FALSE");
                }
                
                driver.getRenderer().setOptions(rendererOptions);
            }
        }

         try {
            driver.run();
         } catch (Exception e) {
            logger.logDebug("finish: XML Doc : \n"+new String(stx));
            throw e;
         }

         byte[] content = out.toByteArray();

         //SAVE TO FILE PDF

         String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");
         String fileName = (String) req.getAttribute("FILE_NAME");

         File fo = new File("C:/");

         String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

         String sf = sdf.format(new Date());
         String tempPath = fileFOlder+File.separator+ "report_temp"+ File.separator+ sf;
         String path1 = fileFOlder+File.separator+ "report_temp"+ File.separator;
         String pathTemp = tempPath+File.separator+ fileName + "_temp.pdf";
         String pathFinal = tempPath+File.separator+ fileName + ".pdf";

         if(Tools.isYes(saveToFile)){

               try {
                        new File(path1).mkdir();
                        new File(tempPath).mkdir();
               } catch (Exception e) {
               }

              fo = new File(pathTemp);

              FileOutputStream fop=new FileOutputStream(fo.getCanonicalPath());

              try {
                    fop.write(content);
                    fop.close();

              } catch (Exception e) {

                 fop.close();
                 fo.delete();

                 throw e;
              }
         }

         List<InputStream> list = new ArrayList<InputStream>();

         list.add(new FileInputStream(new File(pathTemp)));
         list.add(new FileInputStream(new File("D:/fin-repository/report/20131115/PSAKI  2005 - Wording.pdf")));

         // Resulting pdf
         OutputStream out2 = new FileOutputStream(new File(pathFinal));

         //GABUNGIN POLIS & WORDING
         PDFMerge merge = new PDFMerge();
         merge.doMerge(list, out2);

         FileInputStream str = new FileInputStream(new File(pathFinal));

         byte[] content2 = merge.readFully2(str);

         logger.logDebug("finish: streaming pdf ("+content2.length+" bytes)");

         orig.setContentType("application/pdf");
         orig.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
         orig.setContentLength(content2.length);
         orig.getOutputStream().write(content2);
         orig.getOutputStream().flush();

         out2.close();
         str.close();

         File file = new File(pathTemp);

         File fileAbsolute = new File(file.getAbsolutePath());
         fileAbsolute.delete();

         logger.logWarning("########## PATH TEMP : "+pathTemp);
         logger.logWarning("########## PATH ABSOLUT : "+file.getAbsolutePath());

         fopStream=null;
         orig=null;

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

    public void finish() {
      try {

          //finishAll();

          String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");

          if(Tools.isYes(saveToFile)){
              finish2();
          }else{
              finishAll();
          }
          

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

    public void save() {
      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();

         if (pw!=null)
            pw.flush();

         if (fopStream!=null)
            fopStream.flush();

         final byte[] stx = fopStream.getStream().toByteArray();

         logger.logDebug("finish: input doc is "+stx.length+" bytes");

         final ByteArrayInputStream bis = new ByteArrayInputStream(stx);

         Driver driver = new Driver(new InputSource(bis), out);

        driver.setRenderer(Driver.RENDER_PDF);

        if(isLockPDF()){
            if(Parameter.readString("PDF_PASSWORD")!=null){
                Map rendererOptions = new java.util.HashMap();
                
                //rendererOptions.put("userPassword", Parameter.readString("PDF_PASSWORD"));
                rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                //rendererOptions.put("allowCopyContent", "FALSE");
                rendererOptions.put("allowEditContent", "FALSE");
                //rendererOptions.put("allowPrint", "FALSE");
                driver.getRenderer().setOptions(rendererOptions);
            }
        }

         try {
            driver.run();
         } catch (Exception e) {
            logger.logDebug("finish: XML Doc : \n"+new String(stx));
            throw e;
         }

         byte[] content = out.toByteArray();

         //SAVE TO FILE PDF

         String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");
         String fileName = (String) req.getAttribute("FILE_NAME");
         InsurancePolicyView policy = (InsurancePolicyView) req.getAttribute("POLICY");

         File fo = new File("C:/");

         String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

         String sf = sdf.format(new Date());
         String tempPath = fileFOlder+File.separator+ "report_temp"+ File.separator+ sf;
         String path1 = fileFOlder+File.separator+ "report_temp"+ File.separator;
         String pathTemp = tempPath+File.separator+ fileName + "_temp.pdf";
         String pathFinal = fileFOlder + File.separator + "report"+ File.separator + fileName + ".pdf";

         if(Tools.isYes(saveToFile)){

               try {
                        new File(path1).mkdir();
                        new File(tempPath).mkdir();
               } catch (Exception e) {
               }

              fo = new File(pathTemp);

              FileOutputStream fop=new FileOutputStream(fo.getCanonicalPath());

              try {
                    fop.write(content);
                    fop.close();

              } catch (Exception e) {

                 fop.close();
                 fo.delete();

                 throw e;
              }
         }

         List<InputStream> list = new ArrayList<InputStream>();

         list.add(new FileInputStream(new File(pathTemp)));
         //list.add(new FileInputStream(new File("D:/fin-repository/report/20131115/PSAKI  2005 - Wording.pdf")));
         list.add(new FileInputStream(new File(policy.getPolicyType().getStWordingPath())));

         // Resulting pdf
         OutputStream out2 = new FileOutputStream(new File(pathFinal));

         //GABUNGIN POLIS & WORDING
         if(policy.getPolicyType().getStWordingPath()!=null){
                PDFMerge merge = new PDFMerge();
                merge.doMerge(list, out2);
         }
         

//         FileInputStream str = new FileInputStream(new File(pathFinal));
//
//         byte[] content2 = merge.readFully2(str);
//
//         logger.logDebug("finish: streaming pdf ("+content2.length+" bytes)");
//
//         orig.setContentType("application/pdf");
//         orig.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");
//         orig.setContentLength(content2.length);
//         orig.getOutputStream().write(content2);
//         orig.getOutputStream().flush();
//
//         out2.close();
//         str.close();

//         fopStream=null;
//         orig=null;

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

    public void save2(String polid) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (pw != null) {
                pw.flush();
            }

            if (fopStream != null) {
                fopStream.flush();
            }

            final byte[] stx = fopStream.getStream().toByteArray();

            logger.logDebug("finish: input doc is " + stx.length + " bytes");

            final ByteArrayInputStream bis = new ByteArrayInputStream(stx);

            Driver driver = new Driver(new InputSource(bis), out);

            driver.setRenderer(Driver.RENDER_PDF);

            if (isLockPDF()) {
                if (Parameter.readString("PDF_PASSWORD") != null) {
                    Map rendererOptions = new java.util.HashMap();
                    rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                    rendererOptions.put("allowEditContent", "FALSE");
                    driver.getRenderer().setOptions(rendererOptions);
                }
            }

            try {
                driver.run();
            } catch (Exception e) {
                logger.logDebug("finish: XML Doc : \n" + new String(stx));
                throw e;
            }

            byte[] content = out.toByteArray();

            //SAVE TO FILE PDF

            String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");
            String fileName = (String) req.getAttribute("FILE_NAME");
            //InsurancePolicyView policy = (InsurancePolicyView) req.getAttribute("POLICY");

            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            String sf = sdf.format(new Date());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + "_temp.pdf";
            String pathFinal = fileFOlder + File.separator + "report" + File.separator + fileName + ".pdf";

            if (Tools.isYes(saveToFile)) {

                try {
                    new File(path1).mkdir();
                    new File(tempPath).mkdir();
                } catch (Exception e) {
                }

                fo = new File(pathTemp);

                FileOutputStream fop = new FileOutputStream(fo.getCanonicalPath());

                try {
                    fop.write(content);
                    fop.close();

                } catch (Exception e) {

                    fop.close();
                    fo.delete();

                    throw e;
                }

                final SQLUtil S = new SQLUtil();

                try {
                    FileView fv = new FileView();

                    fv.markNew();

                    fv.setStFileID(String.valueOf(IDFactory.createNumericID("FILE")));
                    fv.setStOriginalName(fileName + ".pdf");
                    fv.setStFilePath(fo.getCanonicalPath());
                    fv.setDbFileSize(fv.getDbOriginalSize());
                    fv.setDtFileDate(new Date(fo.lastModified()));
                    fv.setStFileType("PDF");
                    fv.setStMimeType("application/pdf");
                    fv.setDbOriginalSize(new BigDecimal(fo.length()));
                    fv.setStDescription("File");
                    fv.setStImageFlag("N");

                    fv.determineFileType();

                    S.store(fv);

                    InsurancePolicyDocumentView doc = new InsurancePolicyDocumentView();

                    doc.markNew();

                    doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                    doc.setStInsuranceDocumentTypeID("94");
                    doc.setStDocumentClass("POLICY");
                    doc.setStFilePhysic(fv.getStFileID());
                    doc.setStPolicyID(polid);

                    S.store(doc);


//                    PreparedStatement P = S.setQuery("update s_files set file_size = ? where orig_name = ? ");
//
//                    P.setObject(1, fv.getDbOriginalSize());
//                    P.setObject(2, fileName + ".pdf");
//
//                    int i = P.executeUpdate();
//
//                    if (i == 0) {
//                        S.releaseResource();
//
//                        P = S.setQuery("insert into s_files(file_id,orig_name,file_path,file_size,file_date,file_type,mime_type,orig_size,description,image_flag,create_date,create_who) values(?,?,?,?,?,?,?,?,?,?,?,?)");
//
//                        P.setObject(1, String.valueOf(IDFactory.createNumericID("FILE")));
//                        P.setObject(2, fileName + ".pdf");
//                        P.setObject(3, fo.getCanonicalPath());
//                        P.setObject(4, fv.getDbOriginalSize());
//                        P.setObject(5, new Date(fo.lastModified()));
//                        P.setObject(6, "PDF");
//                        P.setObject(7, "application/pdf");
//                        P.setObject(8, new BigDecimal(fo.length()));
//                        P.setObject(9, "File");
//                        P.setObject(10, "N");
//                        P.setObject(11, new Date());
//                        P.setObject(12, SessionManager.getInstance().getSession().getStUserID());
//
//                        i = P.executeUpdate();
//
//                        if (i == 0) {
//                            throw new Exception("Failed to update gl account balance");
//                        }
//                    }
//
//                    fv.determineFileType();
//
//                    PreparedStatement PS = S.setQuery("update ins_pol_documents set document_class = 'POLICY' where policy_id = ? and ins_document_type_id = 94 and document_class = 'POLICY' ");
//
//                    PS.setObject(1, polid);
//
//                    int j = PS.executeUpdate();
//
//                    if (j == 0) {
//                        S.releaseResource();
//
//                        PS = S.setQuery("insert into ins_pol_documents(ins_pol_document_id,ins_document_type_id,document_class,file_physic,policy_id) values(?,?,?,?,?)");
//
//                        PS.setObject(1, String.valueOf(IDFactory.createNumericID("POLDOC")));
//                        PS.setObject(2, "94");
//                        PS.setObject(3, "POLICY");
//                        PS.setObject(4, fv.getStFileID());
//                        PS.setObject(5, polid);
//
//                        j = PS.executeUpdate();
//
//                        if (j == 0) {
//                            throw new Exception("Failed to update gl account balance");
//                        }
//                    }

                } finally {
                    S.release();
                }
            }

            List<InputStream> list = new ArrayList<InputStream>();

            list.add(new FileInputStream(new File(pathTemp)));

            // Resulting pdf
            OutputStream out2 = new FileOutputStream(new File(pathFinal));

            PDFMerge merge = new PDFMerge();
            merge.doMerge(list, out2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save3() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (pw != null) {
                pw.flush();
            }

            if (fopStream != null) {
                fopStream.flush();
            }

            final byte[] stx = fopStream.getStream().toByteArray();

            logger.logDebug("finish: input doc is " + stx.length + " bytes");

            final ByteArrayInputStream bis = new ByteArrayInputStream(stx);

            Driver driver = new Driver(new InputSource(bis), out);

            driver.setRenderer(Driver.RENDER_PDF);

            if (isLockPDF()) {
                if (Parameter.readString("PDF_PASSWORD") != null) {
                    Map rendererOptions = new java.util.HashMap();
                    //rendererOptions.put("userPassword", Parameter.readString("PDF_PASSWORD"));
                    rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                    //rendererOptions.put("allowCopyContent", "FALSE");
                    rendererOptions.put("allowEditContent", "FALSE");
                    //rendererOptions.put("allowPrint", "FALSE");
                    driver.getRenderer().setOptions(rendererOptions);
                }
            }

            try {
                driver.run();
            } catch (Exception e) {
                logger.logDebug("finish: XML Doc : \n" + new String(stx));
                throw e;
            }

            byte[] content = out.toByteArray();

            //SAVE TO FILE PDF

            String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");
            String fileName = (String) req.getAttribute("FILE_NAME");

            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            String sf = sdf.format(new Date());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + "_temp.pdf";
            String pathFinal = fileFOlder + File.separator + "report" + File.separator + fileName + ".pdf";

            if (Tools.isYes(saveToFile)) {

                try {
                    new File(path1).mkdir();
                    new File(tempPath).mkdir();
                } catch (Exception e) {
                }

                fo = new File(pathTemp);

                FileOutputStream fop = new FileOutputStream(fo.getCanonicalPath());

                try {
                    fop.write(content);
                    fop.close();

                } catch (Exception e) {

                    fop.close();
                    fo.delete();

                    throw e;
                }
            }

            List<InputStream> list = new ArrayList<InputStream>();
            list.add(new FileInputStream(new File(pathTemp)));

            // Resulting pdf
            OutputStream out2 = new FileOutputStream(new File(pathFinal));

            PDFMerge merge = new PDFMerge();
            merge.doMerge(list, out2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEdocument() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (pw != null) {
                pw.flush();
            }

            if (fopStream != null) {
                fopStream.flush();
            }

            final byte[] stx = fopStream.getStream().toByteArray();

            logger.logDebug("finish: input doc is " + stx.length + " bytes");

            final ByteArrayInputStream bis = new ByteArrayInputStream(stx);

            Driver driver = new Driver(new InputSource(bis), out);

            driver.setRenderer(Driver.RENDER_PDF);

            if (isLockPDF()) {
                if (Parameter.readString("PDF_PASSWORD") != null) {
                    Map rendererOptions = new java.util.HashMap();
                    //rendererOptions.put("userPassword", Parameter.readString("PDF_PASSWORD"));
                    rendererOptions.put("ownerPassword", Parameter.readString("PDF_PASSWORD"));
                    //rendererOptions.put("allowCopyContent", "FALSE");
                    rendererOptions.put("allowEditContent", "FALSE");
                    //rendererOptions.put("allowPrint", "FALSE");
                    driver.getRenderer().setOptions(rendererOptions);
                }
            }

            try {
                driver.run();
            } catch (Exception e) {
                logger.logDebug("finish: XML Doc : \n" + new String(stx));
                throw e;
            }

            byte[] content = out.toByteArray();

            //SAVE TO FILE PDF

            String saveToFile = (String) req.getAttribute("SAVE_TO_FILE");
            String fileName = (String) req.getAttribute("FILE_NAME");

            File fo = new File("C:/");

            String fileFolder = Parameter.readString("SYS_FILES_FOLDER");

            String tempPath = fileFolder + File.separator + "report_temp" + File.separator + "edocument";
            String path1 = fileFolder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + "_temp.pdf";
            String pathFinal = fileFolder + File.separator + "report" + File.separator + fileName + ".pdf";

            if (Tools.isYes(saveToFile)) {

                try {
                    new File(path1).mkdir();
                    new File(tempPath).mkdir();
                } catch (Exception e) {
                }

                fo = new File(pathTemp);

                FileOutputStream fop = new FileOutputStream(fo.getCanonicalPath());

                try {
                    fop.write(content);
                    fop.close();

                } catch (Exception e) {

                    fop.close();
                    fo.delete();

                    throw e;
                }
            }

            List<InputStream> list = new ArrayList<InputStream>();
            list.add(new FileInputStream(new File(pathTemp)));

            // Resulting pdf
            OutputStream out2 = new FileOutputStream(new File(pathFinal));

            //GABUNGIN POLIS & WORDING
            PDFMerge merge = new PDFMerge();
            merge.doMerge(list, out2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   
}
