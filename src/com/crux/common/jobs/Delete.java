/*
 * Delete.java
 *
 * Created on October 27, 2011, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.crux.common.jobs;

import java.io.File;

/**
 *
 * @author doni
 */
public class Delete
{
    
    /** Creates a new instance of Delete */
    public Delete()
    {
         
    }
    
     public static void main(String[] args) {
    String fileName = "E:/fin-repository/00/20111004/1317723146532";
    // A File object to represent the filename
    File f = new File(fileName);

    // Make sure the file or directory exists and isn't write protected
    if (!f.exists())
      throw new IllegalArgumentException(
          "Delete: no such file or directory: " + fileName);

    if (!f.canWrite())
      throw new IllegalArgumentException("Delete: write protected: "
          + fileName);

    // If it is a directory, make sure it is empty
    if (f.isDirectory()) {
      String[] files = f.list();
      if (files.length > 0)
        throw new IllegalArgumentException(
            "Delete: directory not empty: " + fileName);
    }

    // Attempt to delete it
    boolean success = f.delete();

    if (!success)
      throw new IllegalArgumentException("Delete: deletion failed");
  }
    
}
