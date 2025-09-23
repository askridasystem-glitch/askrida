/***********************************************************************
 * Module:  com.crux.web.resource.Resource
 * Author:  Denny Mahendra
 * Created: Jul 6, 2005 2:59:19 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.resource;

import com.crux.util.LogManager;

import java.io.InputStream;

public class Resource {
   private static byte[] readingBuffer = new byte[256*1024];
   private byte[] res;
   private String contentType;
   private boolean encoded;

   private final static transient LogManager logger = LogManager.getInstance(Resource.class);

   public Resource(String stResourcePath,String ct) throws Exception{
      final InputStream is = this.getClass().getClassLoader().getResourceAsStream(stResourcePath);

      //encoded = ct.indexOf("text")<0;

      encoded = false;

      if (is==null) throw new RuntimeException("Unable to retrieve resource : "+stResourcePath);

      contentType = ct;

      try {
         //final StringBuffer sz = new StringBuffer();

         /*int n;
         int c=0;
         byte[] buf = new byte[10*1024];
         while ((n=is.read(buf))>=0) {
            sz.append(new String(buf,0,n));
            c+=n;
         }

         res = sz;

         if (c!=res.length()) throw new RuntimeException("Error reading "+stResourcePath);*/

         int n=0;

         n=is.read(readingBuffer);

         if (n==readingBuffer.length) throw new RuntimeException("Failed to precache resource "+stResourcePath+" : (size is too large for cache buffer)");

         res = new byte[n];

         System.arraycopy(readingBuffer,0,res,0,n);

      } finally {
         is.close();
      }
   }

   public String getContentType() {
      return contentType;
   }


   public byte [] getBytes() {
      return res;
   }
}
