/***********************************************************************
 * Module:  com.crux.common.controller.MultiPartWrapper
 * Author:  Denny Mahendra
 * Created: Jul 5, 2005 3:34:48 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.oreilly.servlet.MultipartRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MultiPartWrapper{
   private MultipartRequest mpr;
   private HashMap files;

   public MultiPartWrapper(MultipartRequest mpr) {
      this.mpr = mpr;

      Enumeration fileNames = mpr.getFileNames();

      files = new HashMap();

      while (fileNames.hasMoreElements()) {
         String fn = (String) fileNames.nextElement();
         files.put(mpr.getOriginalFileName(fn), mpr.getFile(fn));
      }
   }

   public Map getFiles() {
      return files;
   }
}
