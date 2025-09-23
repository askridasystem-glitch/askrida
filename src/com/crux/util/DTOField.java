/***********************************************************************
 * Module:  com.crux.util.DTOField
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 1:12:04 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.lang.reflect.Method;
import java.io.Serializable;

public class DTOField implements Serializable {
   private String stFieldName;
   private Class fieldType;
   private String stDatabaseFieldName;
   private String stStoreMode;
   private java.sql.Types databaseFieldType;

   private Method getter;
   private Method setter;

   int flags = 0;

   public void markPrimaryKey() {
      flags |= 4;
   }

   public boolean isPrimaryKey() {
      return (flags & 4) != 0;
   }

   public void markReadOnly() {
      flags |= 1;
   }

   public void markNonDatabaseField() {
      flags |= 2;
   }

   public boolean isReadOnly() {
      return (flags & 1) != 0;
   }

   public boolean isDatabaseField() {
      return (flags & 2) == 0;
   }

   public Method getGetter() {
      return getter;
   }

   public void setGetter(Method getter) {
      this.getter = getter;
   }

   public Method getSetter() {
      return setter;
   }

   public void setSetter(Method setter) {
      this.setter = setter;
   }

   public String getStDatabaseFieldName() {
      return stDatabaseFieldName;
   }

   public void setStDatabaseFieldName(String stDatabaseFieldName) {
      this.stDatabaseFieldName = stDatabaseFieldName;
   }

   public String getStFieldName() {
      return stFieldName;
   }

   public void setStFieldName(String stFieldName) {
      this.stFieldName = stFieldName;
   }

   public java.sql.Types getDatabaseFieldType() {
      return databaseFieldType;
   }

   public void setDatabaseFieldType(java.sql.Types databaseFieldType) {
      this.databaseFieldType = databaseFieldType;
   }

   public Class getFieldType() {
      return fieldType;
   }

   public void setFieldType(Class fieldType) {
      this.fieldType = fieldType;
   }

   public String getStStoreMode() {
      return stStoreMode;
   }

   public void setStStoreMode(String stStoreMode) {
      this.stStoreMode = stStoreMode;
   }

   public boolean isCLOB() {
      return "clob".equalsIgnoreCase(stStoreMode);
   }

   public boolean isCLOB2() {
      return "clob2".equalsIgnoreCase(stStoreMode);
   }
}
