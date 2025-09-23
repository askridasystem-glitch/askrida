/***********************************************************************
 * Module:  com.crux.common.filter.LanguageFilter
 * Author:  Denny Mahendra
 * Created: Jun 11, 2006 1:40:10 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.util.LogManager;
import com.crux.util.ThreadContext;
import com.crux.common.parameter.Parameter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.util.Stack;

public class LanguageFilter implements Filter {

   private final static transient LogManager logger = LogManager.getInstance(LanguageFilter.class);
   private static String defaultLang;

   public void init(FilterConfig filterConfig) throws ServletException {
      defaultLang = Parameter.readString("SYS_DEFAULT_LANG");
   }

   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

      final HttpServletRequest rq = (HttpServletRequest) servletRequest;

      final HttpSession session = rq.getSession();

      String chgLang = rq.getParameter("clangx");

      if (chgLang!=null) {
         session.setAttribute("LANG", chgLang);
      }

      if (session.getAttribute("LANG")==null) {
         session.setAttribute("LANG", getSysLanguage());
      }

      final ThreadContext tctx = ThreadContext.getInstance();

      tctx.put("SYS_LANG", session.getAttribute("LANG"));

      if (servletRequest.getAttribute("LANG_FILTER") != null) {
         filterChain.doFilter(servletRequest, servletResponse);
         return;
      }

      servletRequest.setAttribute("LANG_FILTER", "Y");

      final HttpServletRequest request = (HttpServletRequest) servletRequest;

      String ovLang = request.getParameter("xlang");

      //if (ovLang==null) ovLang = (String) session.getAttribute("LANG");

      if (ovLang!=null)
         ThreadContext.getInstance().put("SYS_LANG", ovLang);

      //logger.logDebug("doFilter: Language Filter active"); tes mark logger
      final LanguageFilterResponseWrapper lw = new LanguageFilterResponseWrapper((HttpServletResponse) servletResponse);
      filterChain.doFilter(servletRequest, lw);

      lw.flushBuffer();
      lw.finishResponse();

   }

   public void destroy() {
   }

   public static class LanguageFilterResponseWrapper extends HttpServletResponseWrapper {
      protected HttpServletResponse origResponse = null;
      protected ServletOutputStream stream = null;
      protected PrintWriter writer = null;
      private boolean disabled = false;
      private OutputStreamWriter osw;

      private final static transient LogManager logger = LogManager.getInstance(GZIPResponseWrapper.class);
      private HttpServletRequest request;
      private boolean stripHeaders;

      public boolean isStripHeaders() {
         return stripHeaders;
      }

      public void setStripHeaders(boolean stripHeaders) {
         this.stripHeaders = stripHeaders;
      }

      public ServletOutputStream createOutputStream() throws IOException {
         //logger.logDebug("createOutputStream: ");
         if (disabled) return origResponse.getOutputStream();
         return (new LanguageFilterOutputStream(origResponse));
      }

      public void addHeader(String s, String s1) {

         if (stripHeaders) return;

         super.addHeader(s, s1);
      }

      public void setHeader(String s, String s1) {

         if (stripHeaders) return;

         super.setHeader(s, s1);
      }

      public void setContentType(String s) {
         if (stripHeaders) return;
         origResponse.setContentType(s);
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
         } catch (IOException e) {
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

      public LanguageFilterResponseWrapper(HttpServletResponse response) {
         super(response);
         origResponse = (HttpServletResponse) response;
      }
   }

   public static class LanguageFilterByteArrayOutputStream extends ByteArrayOutputStream {

      public byte[] getResult() throws Exception {

         Stack p = new Stack();

         final ByteArrayOutputStream xbaos = new ByteArrayOutputStream();

         Token active = null;

         for (int i = 0; i < count; i++) {
            final byte c = buf[i];

            if (
                    (c == '{') &&
                    (i + 8 < buf.length) &&
                    (buf[i + 1] == 'L') &&
                    (buf[i + 2] == '-')
            ) {

               final Token token = new Token();

               active = token;

               token.pos = i;

               token.lang = new String(buf, i + 3, 3);

               String effectiveLanguage = (String) ThreadContext.getInstance().get("SYS_LANG");

               if (effectiveLanguage == null) effectiveLanguage = LanguageFilter.getSysLanguage();

               token.display = (token.lang.equalsIgnoreCase(effectiveLanguage));

               i += 5;

               p.push(token);
            } else if (
                    (c == '-') &&
                    (i + 3 < buf.length) &&
                    (buf[i + 1] == 'L') &&
                    (buf[i + 2] == '}')
            ) {
               if (!p.empty()) p.pop();

               i += 2;

               active = p.empty() ? null : (Token) p.peek();

            } else {

               if (active == null || active.display) {

                  xbaos.write(c);

               }

            }

         }

         xbaos.flush();

         return xbaos.toByteArray();
      }

   }

   public static String getSysLanguage() {
      return defaultLang;

   }

   public static class Token {
      public int pos;
      public String lang;
      public boolean display;
   }

   public static class LanguageFilterOutputStream extends ServletOutputStream {
      private ServletOutputStream outputStream;
      private LanguageFilterByteArrayOutputStream buffer;
      private boolean closed;

      public LanguageFilterOutputStream(HttpServletResponse resp) {
         try {
            outputStream = resp.getOutputStream();
            buffer = new LanguageFilterByteArrayOutputStream();

            //print("<!-- CruxLang1.0-->");
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      public void write(int b) throws IOException {
         buffer.write(b);
      }

      public void write(byte b[], int off, int len) throws IOException {
         buffer.write(b, off, len);
      }

      public void close() throws IOException {

         if (closed) return;

         try {
            //print("<!-- CruxLang1.0-->");

            closed = true;

            buffer.flush();
            final byte[] bfx = buffer.getResult();
            buffer.close();

            //logger.logDebug("close: "+new String(bfx));

            outputStream.write(bfx, 0, bfx.length);

            outputStream.flush();
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
   }
}
