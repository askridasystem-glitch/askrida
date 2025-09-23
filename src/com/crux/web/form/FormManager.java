/***********************************************************************
 * Module:  com.crux.web.form.FormManager
 * Author:  Denny Mahendra
 * Created: May 20, 2005 5:23:44 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.form;

import com.crux.util.LogManager;
import com.crux.web.controller.SessionManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FormManager {
   private static FormManager staticinstance;
   private ArrayList forms = new ArrayList();
   private HashMap formMap = new HashMap();
   private HashSet formPaths = new HashSet();

   private final static transient LogManager logger = LogManager.getInstance(FormManager.class);

   public static FormManager getInstance() {
      if (staticinstance == null) staticinstance = new FormManager();
      return staticinstance;
   }

   private FormManager() {
   }

   public void loadForms(final String realpath, final String path, boolean recursive) throws Exception {
      final File dir = new File(path);
      System.out.println("scanning : "+path+" :: "+realpath);
      dir.listFiles(
              new FileFilter() {
                 public boolean accept(File pathname) {
                    try {
                       //System.out.println("file : "+pathname.getCanonicalPath());
                       if (pathname.getName().indexOf(".frx")>=0) {
                          String dp = path.substring(realpath.length(),path.length());
                          loadForm(dp, pathname.getAbsolutePath());
                       }
                       if (pathname.isDirectory()) loadForms(realpath, pathname.getAbsolutePath(), true);
                       return false;
                    } catch (Exception e) {
                       throw new RuntimeException(e);
                    }
                 }
              }
      );
   }

   private void loadForm(String dp, String filename) throws Exception {
      System.out.println("loadForm : "+filename);
      SAXParserFactory.newInstance().newSAXParser().parse(new File(filename), new FormParser(dp));
   }

   public void addFormsPath(String realPath) throws Exception {
      if (formPaths.contains(realPath)) return;

      formPaths.add(realPath);

      loadForms(realPath, realPath,true);
   }

   public Form newForm(String rsrcname, Form opener) throws Exception {
      final FormMeta formMeta = (FormMeta)formMap.get(rsrcname);

      if (formMeta == null) throw new RuntimeException("Form not found");

      //logger.logDebug("newForm: creating form .."); mark logger

      final Form f = (Form) formMeta.getFormClass().getDeclaredConstructor(new Class [] {}).newInstance(new Object [] {});
//      final Form f = new LoginForm();

      f.setOpener(opener);

      f.setFormMeta(formMeta);

      f.onFormCreate();

      SessionManager.getInstance().add(f);

      return f;

   }

   public Form getForm(String rsrcname, String formid) throws Exception {
      if (formid==null) return newForm(rsrcname, null);
      final Form form = SessionManager.getInstance().getForm(formid);
      if (form == null) throw new Exception("Form not found : "+formid);
      return form;
   }

   private class FormParser extends DefaultHandler {
      private FormMeta formMeta;
      private String diffpath;

      public FormParser(String path) {
         diffpath = path;
      }

      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         if ("form".equals(qName)) {
            formMeta = new FormMeta();
            formMeta.setStName(attributes.getValue("name"));
            formMeta.setStClassName(attributes.getValue("class"));
            formMeta.setStReturn(attributes.getValue("return"));

            String view = attributes.getValue("view");

            if (view != null && view.length()>1)
               if (view.charAt(0)!=File.separatorChar)
                  view=diffpath+File.separatorChar+view;

            formMeta.setStPresentation(view);
         }
      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         if ("form".equals(qName)) {
            FormManager.this.register(formMeta);
         }
      }
   }

   private void register(FormMeta formMeta) {
      System.out.println("form registered : "+formMeta.getStName());
      forms.add(formMeta);
      formMap.put(formMeta.getStName(),formMeta);
   }
}
