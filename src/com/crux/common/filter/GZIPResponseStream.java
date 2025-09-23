/***********************************************************************
 * Module:  com.crux.common.filter.GZIPResponseStream
 * Author:  Denny Mahendra
 * Created: Jul 23, 2004 10:37:12 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.util.LogManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPResponseStream extends ServletOutputStream {
   protected ByteArrayOutputStream baos = null;
   protected GZIPOutputStream gzipstream = null;
   protected boolean closed = false;
   protected HttpServletResponse response = null;
   //protected ServletOutputStream output = null;
   int szin =0 ;

   private final static transient LogManager logger = LogManager.getInstance(GZIPResponseStream.class);
   private HttpServletRequest request;

   public GZIPResponseStream(HttpServletRequest req, HttpServletResponse response) throws IOException {
      super();
      closed = false;
      this.response = response;
      baos = new ByteArrayOutputStream();
      gzipstream = new GZIPOutputStream(baos);
      request = req;

      response.addHeader("Content-Encoding", "gzip");

   }

   public void close() throws IOException {
      if (closed) {
         throw new IOException("This output stream has already been closed");
      }
      gzipstream.flush();
      gzipstream.finish();

      byte[] bytes = baos.toByteArray();

      //logger.logDebug("encoding gzip["+request.getRequestURI()+"]: "+bytes.length+"/"+szin+" bytes"); mark logger

      //response.addHeader("Content-Length",
      //                   Integer.toString(bytes.length));
      response.setContentLength(bytes.length);

      ServletOutputStream output = response.getOutputStream();
      output.write(bytes);
      output.flush();
      output.close();
      closed = true;
   }

   public void flush() throws IOException {
      if (closed) {
         //throw new IOException("Cannot flush a closed output stream");
         return;
      }
      gzipstream.flush();
   }

   public void write(int b) throws IOException {
      if (closed) {
         throw new IOException("Cannot write to a closed output stream");
      }
      //System.out.print((char)b);
      gzipstream.write((byte) b);
      szin++;
   }

   public void write(byte b[]) throws IOException {
      write(b, 0, b.length);
   }

   public void write(byte b[], int off, int len) throws IOException {
      //System.out.println("writing...");
      if (closed) {
         throw new IOException("Cannot write to a closed output stream");
      }
      //System.out.print(new String(b,off,len));
      gzipstream.write(b, off, len);
      szin+=len;
   }

   public boolean closed() {
      return (this.closed);
   }

   public void reset() {
      //noop
   }
}


