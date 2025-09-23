/***********************************************************************
 * Module:  com.crux.common.controller.FOPServlet
 * Author:  Denny Mahendra
 * Created: Apr 1, 2006 11:32:28 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.util.Tools;
import org.xml.sax.InputSource;
import org.apache.fop.messaging.MessageHandler;
import org.apache.fop.apps.XSLTInputHandler;
import org.apache.fop.apps.Driver;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import java.io.*;

import com.crux.common.filter.FOPResponseWrapper;
import com.crux.common.filter.LanguageFilter;
import com.crux.util.LogManager;
import com.crux.util.ThreadContext;
import com.webfin.insurance.model.InsurancePolicyView;
import javax.servlet.RequestDispatcher;

public class FOPServlet extends HttpServlet {
    public static final String FO_REQUEST_PARAM = "fo";
    public static final String XML_REQUEST_PARAM = "xml";
    public static final String XSL_REQUEST_PARAM = "xsl";
    Logger log = null;
     private final static transient LogManager logger = LogManager.getInstance(FOPServlet.class);

   protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      doGet(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public static void enableCache(HttpServletResponse httpServletResponse) {
      // Set to expire far in the past.
      httpServletResponse.setHeader("Expires", null);

      // Set standard HTTP/1.1 no-cache headers.
      httpServletResponse.setHeader("Cache-Control", null);

      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
      httpServletResponse.addHeader("Cache-Control", null);

      // Set standard HTTP/1.0 no-cache header.
      httpServletResponse.setHeader("Pragma", null);
   }

   public void doGetBackup(HttpServletRequest request,HttpServletResponse response) throws ServletException {
       try {
          enableCache(response);

          response.setContentType("application/pdf");
          response.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");

          final String ovLang = request.getParameter("xlang");

          final String lockPDF = (String) request.getAttribute("LOCK_PDF");

          ThreadContext.getInstance().put("SYS_LANG", ovLang);

          FOPResponseWrapper res1 = new FOPResponseWrapper(response, request);

          final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);

          respx.setStripHeaders(true);

          String x = request.getRequestURI()+".jsp";

          x=x.substring(request.getContextPath().length(),x.length());

          request.getRequestDispatcher(x).include(request, respx);

          respx.finishResponse();

          logger.logWarning("################ URI : "+ x );

          if(lockPDF!=null)
              if(!Tools.isYes(lockPDF)) res1.setLockPDF(false);

          res1.finish();

       } catch (Exception e) {
          throw new RuntimeException(e);
       }
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException {
       try {
          enableCache(response);

          response.setContentType("application/pdf");
          response.setHeader("Content-Disposition","attachment; filename=doc"+(System.currentTimeMillis())+".pdf;");

          final String ovLang = request.getParameter("xlang");
          
          final String lockPDF = (String) request.getAttribute("LOCK_PDF");
    
          ThreadContext.getInstance().put("SYS_LANG", ovLang); 

          FOPResponseWrapper res1 = new FOPResponseWrapper(response, request);
          
          final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);

          respx.setStripHeaders(true);

          String x = request.getRequestURI()+".jsp";

          x=x.substring(request.getContextPath().length(),x.length());

          request.getRequestDispatcher(x).include(request, respx);

          respx.finishResponse();

          if(lockPDF!=null)
              if(!Tools.isYes(lockPDF)) res1.setLockPDF(false);

          res1.finishAll();

       } catch (Exception e) {
          throw new RuntimeException(e);
       }
    }

    public void doGet2(HttpServletRequest request,HttpServletResponse response) throws ServletException {
        if (log == null) {
            log = new ConsoleLogger(ConsoleLogger.LEVEL_WARN);
            MessageHandler.setScreenLogger(log);
        }
        try {
            String foParam = request.getParameter(FO_REQUEST_PARAM);
            String xmlParam = request.getParameter(XML_REQUEST_PARAM);
            String xslParam = request.getParameter(XSL_REQUEST_PARAM);

           foParam="H:\\fop\\fop-0.20.5\\examples\\fo\\basic\\border.fo";

            if (foParam != null) {
                File fofile = new File(foParam);
                //log.warn("FO: "+fofile.getCanonicalPath());
                FileInputStream file = new FileInputStream(fofile);
                renderFO(new InputSource(file), response);
            } else if ((xmlParam != null) && (xslParam != null)) {
                XSLTInputHandler input =
                  new XSLTInputHandler(new File(xmlParam),
                                       new File(xslParam));
                renderXML(input, response);
            } else {
                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Error</title></head>\n"+
                            "<body><h1>FopServlet Error</h1><h3>No 'fo' "+
                            "request param given.</body></html>");
            }
        } catch (ServletException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Renders an FO inputsource into a PDF file which is written
     * directly to the response object's OutputStream
     */
    public void renderFO(InputSource foFile,
                         HttpServletResponse response) throws ServletException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            response.setContentType("application/pdf");
            log.info("+++++++++++++++++++++++++ LOG FO NIH +++++++++++++++++++++++");
            Driver driver = new Driver(foFile, out);
            log = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
            driver.setLogger(log);
            driver.setRenderer(Driver.RENDER_PDF);
            driver.run();

            byte[] content = out.toByteArray();
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Renders an XML file into a PDF file by applying a stylesheet
     * that converts the XML to XSL:FO. The PDF is written
     * directly to the response object's OutputStream
     */
    public void renderXML(XSLTInputHandler input,
                          HttpServletResponse response) throws ServletException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            response.setContentType("application/pdf");
log.info("+++++++++++++++++++++++++ LOG XML NIH +++++++++++++++++++++++");
            Driver driver = new Driver();
            log = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
            driver.setLogger(log);
            driver.setRenderer(Driver.RENDER_PDF);
            driver.setOutputStream(out);
            driver.render(input.getParser(), input.getInputSource());

            byte[] content = out.toByteArray();
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void saveFOP(HttpServletRequest request, HttpServletResponse response, String URL) throws ServletException {
        try {
            enableCache(response);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=doc" + (System.currentTimeMillis()) + ".pdf;");

            final String ovLang = request.getParameter("xlang");

            final String lockPDF = (String) request.getAttribute("LOCK_PDF");

            ThreadContext.getInstance().put("SYS_LANG", ovLang);

            FOPResponseWrapper res1 = new FOPResponseWrapper(response, request);

            final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);

            respx.setStripHeaders(true);

            String x = URL + ".jsp";

            //x=x.substring(request.getContextPath().length(),x.length());

            //request.getRequestDispatcher(x).include(request, respx);

            RequestDispatcher view = request.getRequestDispatcher(x);

            view.forward(request, respx);

            respx.finishResponse();

            if (lockPDF != null) {
                if (!Tools.isYes(lockPDF)) {
                    res1.setLockPDF(false);
                }
            }

            String report = (String) request.getAttribute("REPORT_PROD");
            if (Tools.isYes(report)) {
                res1.saveEdocument();
            } else {
                res1.save();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
