/***********************************************************************
 * Module:  com.crux.common.controller.ReportMeta
 * Author:  Denny Mahendra
 * Created: Jun 17, 2005 11:02:00 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.common.model.DataSource;

import java.util.ArrayList;

public class ReportMeta {
   private String stName;
   private Class clDataSourceClass;
   private String stDataSourceFullClassName;
   private String stHelperFullClassName;
   private Class clHelperClass;
   private String stDataSourceName;
   private String stJasperDesignFile;
   private String stJasperFile;
   private ArrayList subReports;

   public String getStHelperFullClassName() {
      return stHelperFullClassName;
   }

   public void setStHelperFullClassName(String stHelperFullClassName) {
      this.stHelperFullClassName = stHelperFullClassName;
   }

   public Class getClHelperClass() {
      try {
         if (clHelperClass==null) clHelperClass=Class.forName(getStHelperFullClassName());
         if (!ReportHelper.class.isAssignableFrom(clHelperClass)) throw new RuntimeException("Invalid Helper class : "+getStHelperFullClassName());
         return clHelperClass;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setClHelperClass(Class clHelperClass) {
      this.clHelperClass = clHelperClass;
   }

   public Class getClDataSourceClass() {
      if (clDataSourceClass == null) {
         try {
            clDataSourceClass = Class.forName(getStDataSourceFullClassName());
            if (!DataSource.class.isAssignableFrom(clDataSourceClass)) throw new RuntimeException("Invalid Data Source class : "+getStDataSourceFullClassName());
         } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
         }
      }
      return clDataSourceClass;
   }

   public void setClDataSourceClass(Class clDataSourceClass) {
      this.clDataSourceClass = clDataSourceClass;
   }

   public String getStDataSourceName() {
      return stDataSourceName;
   }

   public void setStDataSourceName(String stDataSourceName) {
      this.stDataSourceName = stDataSourceName;
   }

   public String getStJasperFile() {
      return stJasperFile;
   }

   public void setStJasperFile(String stJasperFile) {
      this.stJasperFile = stJasperFile;
   }

   public String getStName() {
      return stName;
   }

   public void setStName(String stName) {
      this.stName = stName;
   }

   public String getStDataSourceFullClassName() {
      return stDataSourceFullClassName;
   }

   public void setStDataSourceFullClassName(String stDataSourceFullClassName) {
      this.stDataSourceFullClassName = stDataSourceFullClassName;
   }

   public String getStJasperDesignFile() {
      return stJasperDesignFile;
   }

   public void setStJasperDesignFile(String stJasperDesignFile) {
      this.stJasperDesignFile = stJasperDesignFile;
   }

   public ArrayList getSubReports() {
      if (subReports==null) subReports = new ArrayList();
      return subReports;
   }

   public void setSubReports(ArrayList subReports) {
      this.subReports = subReports;
   }
}
