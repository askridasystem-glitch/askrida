/***********************************************************************
 * Module:  com.crux.web.form.FormMeta
 * Author:  Denny Mahendra
 * Created: May 20, 2005 5:40:12 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.form;

public class FormMeta {
   private String stName;
   private String stClassName;
   private Class formClass;
   private String stPresentation;
   private String stReturn;

   public Class getFormClass() {
      try {
         if (formClass == null) formClass = Class.forName(stClassName);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
      return formClass;
   }

   public void setFormClass(Class formClass) {
      this.formClass = formClass;
   }

   public String getStName() {
      return stName;
   }

   public void setStName(String stName) {
      this.stName = stName;
   }

   public String getStClassName() {
      return stClassName;
   }

   public void setStClassName(String stClassName) {
      this.stClassName = stClassName;
   }

   public String getStPresentation() {
      return stPresentation;
   }

   public void setStPresentation(String stPresentation) {
      this.stPresentation = stPresentation;
   }

   public String getStReturn() {
      return stReturn;
   }

   public void setStReturn(String stReturn) {
      this.stReturn = stReturn;
   }
}
