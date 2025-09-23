/***********************************************************************
 * Module:  com.crux.common.filter.GZIPResponseWrapper
 * Author:  Denny Mahendra
 * Created: Jul 23, 2004 10:35:34 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.util.LogManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {
   protected HttpServletResponse origResponse = null;
   protected ServletOutputStream stream = null;
   protected PrintWriter writer = null;
   private boolean disabled = false;
   private OutputStreamWriter osw;

   public void setHeader(String s, String s1) {
      //logger.logDebug("setHeader("+s+","+s1+")");
      super.setHeader(s, s1);
   }

   public void addHeader(String s, String s1) {
      //logger.logDebug("addHeader("+s+","+s1+")");
      super.addHeader(s, s1);
   }

   private final static transient LogManager logger = LogManager.getInstance(GZIPResponseWrapper.class);
   private HttpServletRequest request;

   public GZIPResponseWrapper(HttpServletRequest req, HttpServletResponse response) {
      super(response);
      origResponse = response;
      request = req;
   }

   public ServletOutputStream createOutputStream() throws IOException {
      //logger.logDebug("createOutputStream: ");
      if (disabled) return origResponse.getOutputStream();
      return (new GZIPResponseStream(request, origResponse));
   }

   public void setContentType(String s) {
      //logger.logDebug("setContentType: "+s);
      if (s.indexOf("pdf")>=0) disabled = true;
      super.setContentType(s);
   }

   public void finishResponse() {
      try {
         if (writer != null) {
            osw.flush();
            osw.close();
            writer.flush();
            writer.close();
         }
         flushBuffer();
         if (stream != null) {
            stream.flush();
            stream.close();
         }
      }
      catch (IOException e) {
      }
   }

   public void flushBuffer() throws IOException {
      super.flushBuffer();
      /*if (stream!=null)
         stream.flush();*/
   }

   public ServletOutputStream getOutputStream() throws IOException {
      if (writer != null) {
         throw new IllegalStateException("getWriter() has already been called!");
      }

      if (stream == null)
         stream = createOutputStream();
      return (stream);
   }

   public PrintWriter getWriter() throws IOException {
      if (writer != null) {
         return (writer);
      }

      if (stream != null) {
         //throw new IllegalStateException("getOutputStream() has already been called!");
      } else
         stream = createOutputStream();

      osw = new OutputStreamWriter(stream, "UTF-8");
      writer = new PrintWriter(osw);
      return (writer);
   }

   public void setContentLength(int length) {
   }
}

