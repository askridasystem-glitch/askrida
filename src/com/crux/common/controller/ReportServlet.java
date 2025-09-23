/***********************************************************************
 * Module:  com.crux.common.controller.ReportServlet
 * Author:  Denny Mahendra
 * Created: Jun 17, 2005 10:12:08 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.common.model.DataSource;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.jasper.JasperUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * ReportServlet responsible for serving reports from http request
 */
public class ReportServlet extends HttpServlet {

   private final static transient LogManager logger = LogManager.getInstance(ReportServlet.class);

   int running = 1;

   private static ReportServlet staticinstance;
   private ServletContext servletContext;
   private HashMap contentTypeMap = new HashMap();

   public static ReportServlet getInstance() {
      if (staticinstance == null) staticinstance = new ReportServlet();
      return staticinstance;
   }

   public ReportServlet() {
      staticinstance = this;
      logger.logDebug("ReportServlet: instance created");

      contentTypeMap.put("pdf",CHPDF.class);
      contentTypeMap.put("xls",CHXLS.class);
      contentTypeMap.put("csv",CHCSV.class);
      contentTypeMap.put("html",CHHTML.class);
      contentTypeMap.put("xml",CHXML.class);
   }

   protected static abstract class ContentHandler {
      ServletOutputStream outputStream;
      FileInputStream inputStream;
      HashMap params;
      DataSource dataSource;
      Connection connection;

      public abstract String getContentType();

      public boolean useInlineMode() {return true;}

      public JasperPrint runReport() throws Exception {
         return
                 dataSource!=null?
                 JasperFillManager.fillReport(inputStream,params,dataSource):
                 JasperFillManager.fillReport(inputStream,params,connection);
      }

      public abstract void exportReport(JasperPrint jasperPrint) throws Exception;
   }

   protected static class CHPDF extends ContentHandler {
      private static final String PDF_CT = "application/pdf";

      public String getContentType() {
         return PDF_CT;
      }

      public boolean useInlineMode() {
         return true;
      }

      public void exportReport(JasperPrint p) throws Exception {
         JRPdfExporter jrPdfExporter = new JRPdfExporter();

         jrPdfExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, p);
         jrPdfExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,outputStream);

         jrPdfExporter.exportReport();
      }
   }

   protected static class CHXLS extends ContentHandler {
      private static final String CT = "application/vnd.ms-excel";

      public String getContentType() {
         return CT;
      }

      public boolean useInlineMode() {
         return false;
      }

      public JasperPrint runReport() throws Exception {
         return
                 dataSource!=null?
                 JasperFillManager.fillReport(inputStream,params,dataSource):
                 JasperFillManager.fillReport(inputStream,params,connection);
      }

      public void exportReport(JasperPrint jasperPrint) throws Exception {
         JRXlsExporter jrXlsExporter = new JRXlsExporter();

         jrXlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
         jrXlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);

         jrXlsExporter.exportReport();
      }

   }

   protected static class CHCSV extends ContentHandler {
      private static final String CT = "text/plain";

      public String getContentType() {
         return CT;
      }

      public boolean useInlineMode() {
         return false;
      }

      public void exportReport(JasperPrint jasperPrint) throws Exception {
         JRCsvExporter jrCsvExporter = new JRCsvExporter();

         jrCsvExporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
         jrCsvExporter.setParameter(JRCsvExporterParameter.OUTPUT_STREAM,outputStream);

         jrCsvExporter.exportReport();
      }
   }

   protected static class CHHTML extends ContentHandler {
      private static final String CT = "text/html";

      public String getContentType() {
         return CT;
      }

      public boolean useInlineMode() {
         return true;
      }

      public void exportReport(JasperPrint jasperPrint) throws Exception {
         JRHtmlExporter jrHtmlExporter = new JRHtmlExporter();

         jrHtmlExporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
         jrHtmlExporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,outputStream);

         jrHtmlExporter.exportReport();
      }
   }

   protected static class CHXML extends ContentHandler {
      private static final String CT = "text/plain";

      public String getContentType() {
         return CT;
      }

      public boolean useInlineMode() {
         return false;
      }

      public void exportReport(JasperPrint jasperPrint) throws Exception {
         JRXmlExporter jrXmlExporter = new JRXmlExporter();

         jrXmlExporter.setParameter(JRXmlExporterParameter.JASPER_PRINT, jasperPrint);
         jrXmlExporter.setParameter(JRXmlExporterParameter.OUTPUT_STREAM,outputStream);

         jrXmlExporter.exportReport();
      }
   }

   public void init(ServletConfig servletConfig) throws ServletException {
      super.init(servletConfig);
      servletContext = servletConfig.getServletContext();
      System.setProperty(
              "jasper.reports.compile.class.path",
              (String)servletContext.getAttribute(
                      "org.apache.catalina.jsp_classpath"
              )
      );
   }

   protected void doGet(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
      doPost(rq, rp);
   }

   protected void doPost(HttpServletRequest rq, final HttpServletResponse rp) throws ServletException, IOException {
      try {

         String uri = rq.getRequestURI();
         String mode = "pdf";

         int n = uri.lastIndexOf('/');
         uri = uri.substring(n+1,uri.length());
         n = uri.lastIndexOf('.');
         uri = uri.substring(0,n).toLowerCase();

         n = uri.lastIndexOf('.');
         if (n>=0) {
            mode = uri.substring(n+1,uri.length());
            uri = uri.substring(0,n).toLowerCase();
         }
         final HashMap params = getParameterFormRequest(rq);
         final ServletOutputStream out = rp.getOutputStream();
         final JasperPrint jrp[] = new JasperPrint [] {null};

         class ReportProcessor {
            public ContentHandler dogenerate(final String uri, final String mode, HashMap params) throws Exception {

               logger.logDebug("doPost: report = "+uri+"."+mode);

               ReportMeta rpt = ControllerServlet.getInstance().getEvents().getReport(uri.toLowerCase());

               if (rpt==null) {
                  logger.logError("Report not found : "+uri);
                  return null;
               }

               String jasperFile = compileJasper(rpt);

               logger.logDebug("doPost: rpt = "+rpt);

               ArrayList subReports = rpt.getSubReports();

               if (subReports!=null) {
                  for (int i = 0; i < subReports.size(); i++) {
                     ReportMeta subrpt = (ReportMeta) subReports.get(i);
                     compileJasper(subrpt);
                     params.put(subrpt.getStName(),servletContext.getRealPath(subrpt.getStJasperFile()));
                  }
               }

               logger.logDebug("doPost: params = "+params);

               String filename = uri + '_' +(running++) + '.' +mode;

               logger.logDebug("doPost: loading "+jasperFile);

               FileInputStream fiJasper = new FileInputStream(jasperFile);

               Class chClass = (Class)contentTypeMap.get(mode);

               logger.logDebug("doPost: using handler : "+chClass);

               ContentHandler ch = (ContentHandler) chClass.newInstance();

               logger.logDebug("doPost: filename:"+filename+" contenttype:"+ch.getContentType()+" inline:"+ch.useInlineMode());

               //filename+='.'+String.valueOf(System.currentTimeMillis());

               rp.setContentType(ch.getContentType());
               if (!ch.useInlineMode())
                  rp.setHeader("Content-Disposition","attachment; filename=" +filename+';');
               else
                  rp.setHeader("Content-Disposition","inline; filename=" +filename+';');

               rp.setHeader("Content-Disposition","attachment; filename=" +filename+';');

               ReportHelper reportHelper=null;

               if (rpt.getStHelperFullClassName()!=null) {
                  reportHelper = (ReportHelper)rpt.getClHelperClass().newInstance();
                  reportHelper.setParams(params);
                  reportHelper.initialize();

                  reportHelper.setListener(
                          new ReportHelperListener() {
                             public void concat(String stReportName, Map params) {
                                try {
                                   new ReportProcessor().dogenerate(stReportName,mode,(HashMap)params);
                                } catch (RuntimeException e) {
                                   throw e;
                                } catch (Exception e) {
                                   throw new RuntimeException(e);
                                }
                             }
                          }
                  );
               }

               ch.inputStream = fiJasper;
               ch.outputStream = out;
               ch.params = params;

               SQLUtil S = null;

               try {
                  if (reportHelper==null || reportHelper.isEvaluate()) {
                     if (rpt.getStDataSourceFullClassName()!=null) {
                        Class clDataSourceClass = rpt.getClDataSourceClass();
                        DataSource ds = (DataSource)clDataSourceClass.newInstance();
                        ds.setParams(params);
                        ds.initialize();
                        ch.dataSource = ds;
                     } else {
                        S = new SQLUtil(rpt.getStDataSourceName());
                        ch.connection = S.getConnection();
                     }
                     if (jrp[0]==null)
                        jrp[0] = ch.runReport();
                     else {
                        Iterator pages = ch.runReport().getPages().iterator();
                        while (pages.hasNext()) {
                           JRPrintPage pg = (JRPrintPage) pages.next();
                           jrp[0].addPage(pg);
                        }
                     }
                     fiJasper.close();
                  }

                  if (reportHelper!=null)
                     reportHelper.afterEvaluate();

                  if (reportHelper!=null) reportHelper.release();
               } finally {
                  if (ch.dataSource!=null) ch.dataSource.release();
                  if (S!=null) S.release();
               }

               return ch;
            }
         }

         ContentHandler ch = new ReportProcessor().dogenerate(uri,mode,params);

         ch.exportReport(jrp[0]);

         out.flush();
         out.close();

         //super.doPost(rq, rp);
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   private String compileJasper(ReportMeta rpt) throws Exception {
      String jasperDesign = servletContext.getRealPath(rpt.getStJasperDesignFile());

      String jasperFile = rpt.getStJasperFile();

      if (jasperFile == null) {
         jasperFile = rpt.getStJasperDesignFile()+".jasper";
         rpt.setStJasperFile(jasperFile);
      }

      jasperFile = servletContext.getRealPath(jasperFile);

      File fJasperDesign = new File(jasperDesign);
      File fJasper = new File(jasperFile);

      /*Enumeration attributeNames = servletContext.getAttributeNames();
      while (attributeNames.hasMoreElements()) {
         String an = (String) attributeNames.nextElement();

         logger.logDebug("doPost: "+an+" = "+servletContext.getAttribute(an));
      }*/

      if (!fJasper.exists() || (fJasper.lastModified()!=fJasperDesign.lastModified())) {
         JasperUtil.compile(jasperDesign, jasperFile);
         fJasper.setLastModified(fJasperDesign.lastModified());
      }
      return jasperFile;
   }

   private HashMap getParameterFormRequest(HttpServletRequest rq) {
      HashMap map = new HashMap();
      Enumeration names = rq.getParameterNames();
      while (names.hasMoreElements()) {
         String pn = (String) names.nextElement();
         map.put(pn,rq.getParameter(pn));
      }
      return map;
   }
}

