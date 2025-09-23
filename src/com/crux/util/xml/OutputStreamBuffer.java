/***********************************************************************
 * Module:  com.crux.util.xml.OutputStreamBuffer
 * Author:  Denny Mahendra
 * Created: Jul 17, 2004 10:09:45 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.xml;

import java.io.OutputStream;
import java.io.IOException;

public class OutputStreamBuffer {
   private OutputStream os;

   public OutputStreamBuffer(OutputStream os) {
      this.os = os;
   }

   public OutputStreamBuffer append(int x) throws IOException {
      append(String.valueOf(x));
      return this;
   }

   public OutputStreamBuffer append(String x) throws IOException {
      for (int i=0;i<x.length();i++)
         os.write(x.charAt(i));
      return this;
   }
}
