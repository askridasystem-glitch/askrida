/***********************************************************************
 * Module:  com.crux.file.FileManager
 * Author:  Denny Mahendra
 * Created: Jul 28, 2006 12:07:44 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.file;

import com.crux.util.ListUtil;
import com.crux.util.DTOList;
import com.crux.util.SQLUtil;
import com.crux.pool.DTOPool;

public class FileManager {
   private static FileManager staticinstance;

   public static FileManager getInstance() {
      if (staticinstance == null) staticinstance = new FileManager();
      return staticinstance;
   }

   private FileManager() {
   }


   public DTOList getDocuments(String group) throws Exception {

      return ListUtil.getDTOListFromQuery(
              "select * from s_files where file_group = ? order by file_id",
              new Object [] {group},
              FileView.class
      );
   }

   public String getFileDesc(String id) {

      if (id==null) return null;

      FileView fi = (FileView)DTOPool.getInstance().getDTORO(FileView.class, id);

      if (fi==null) return null;

      return fi.getStDescription();
   }

   public FileView getFile(String id) {

      if (id==null) return null;

      FileView fi = (FileView)DTOPool.getInstance().getDTORO(FileView.class, id);

      return fi;
   }
   
   
}
