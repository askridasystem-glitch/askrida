/***********************************************************************
 * Module:  com.crux.util.FileEnumerator
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 4:01:16 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.io.File;
import java.util.Enumeration;

public class FileEnumerator implements Enumeration {
   String[] files;
   int no=0;

   public FileEnumerator(final String stPathName) {
      File dir = new File(stPathName);

      files = dir.list();

      if (files == null) throw new IllegalArgumentException("Invalid path : "+stPathName);
   }

   public boolean hasMoreElements() {
      return no<files.length;
   }

   public Object nextElement() {
      return files[no++];
   }
}
