/***********************************************************************
 * Module:  com.crux.web.form.Form
 * Author:  Denny Mahendra
 * Created: May 18, 2005 3:24:26 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.form;

import com.crux.util.LogManager;
import com.crux.util.Tools;
import com.crux.util.ConvertUtil;
import com.crux.util.DTOList;
import com.crux.base.BaseClass;
import com.crux.session.Session;
import com.crux.web.controller.SessionManager;
import com.crux.web.controller.CruxController;
import com.crux.common.controller.FormTab;
import com.crux.common.controller.Helper;
import com.crux.common.config.Config;
import com.crux.common.model.Filter;

import java.util.HashSet;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

public class Form extends BaseClass {
   private static int formIDSerial = 1;
   private String formID;
   private FormMeta formMeta;
   private String stActionEvent;
   private String stAlert;
   private String stErrorTicket;
   private String title=null;
   private String defaultArg1;
   private String defaultArg2;
   private String defaultArg3;
   private String defaultArg4;
   private String scrollTop;
   private String formMode;
   private boolean readOnly = false;
   protected Form opener;
   private String tempRedirect;
   private long stamp;
   private boolean autoRefresh = false;
   private boolean useHeader = true;

   public String getFormMode() {
      return formMode;
   }

   public void setFormMode(String formMode, String title) {
      setFormMode(formMode);
      setTitle(title);
   }
   public void setFormMode(String formMode) {
      this.formMode = formMode;
   }

   public void touch() {
      stamp = System.currentTimeMillis();
   }

   public String getStErrorTicket() {
      return stErrorTicket;
   }

   public void setStErrorTicket(String stErrorTicket) {
      this.stErrorTicket = stErrorTicket;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void sysFormChangeLiztPage() {

      if ("null".equalsIgnoreCase(defaultArg1)) defaultArg1=defaultArg2;

      //Object o = this.getProperty(defaultArg1);

      Object o = getAttribute("CachedList"+defaultArg1);

      /*if (o instanceof DTOList) {
         final DTOList l = (DTOList)o;

         l.getFilter();

         final String gPg = (String) getAttribute("gPg"+l.getName());
         if (gPg != null)
            l.getFilter().setCurrentPage(Integer.valueOf(gPg).intValue());

         final String sort = (String) getAttribute("sPg"+l.getName());
         if (sort!=null) {
            l.getFilter().orderKey = sort;
            if (sort.equalsIgnoreCase(l.getOrderKey())) l.getFilter().orderDir = (l.getOrderDir()*-1);
            l.getFilter().setCurrentPage(0);
         } else {
         }

         logger.logDebug("updatePaging: goto page = "+l.getCurrentPage());
      }*/

      if (o instanceof DTOList) {
         o=((DTOList)o).getFilter();
      }

      if (o instanceof Filter) {
         final Filter f = (Filter)o;

         final String gPg = (String) getAttribute("gPg"+defaultArg2);
         if (gPg != null)
            f.setCurrentPage(Integer.valueOf(gPg).intValue());

         final String sort = (String) getAttribute("sPg"+defaultArg2);
         if (sort!=null) {
            f.orderKey = sort;
            if (sort.equalsIgnoreCase(f.orderKey)) f.orderDir *= -1;
            //f.setCurrentPage(0);
         } else {
         }

         f.afField = (String)getAttribute("affield"+defaultArg2);
         f.afMode = (String)getAttribute("afmode"+defaultArg2);
         f.afValue = (String)getAttribute("afvalue"+defaultArg2);
      }
   }

   public String getScrollTop() {
      return scrollTop;
   }

   public void setScrollTop(String scrollTop) {
      this.scrollTop = scrollTop;
   }

   public String getDefaultArg1() {
      return defaultArg1;
   }

   public void setDefaultArg1(String defaultArg1) {
      this.defaultArg1 = defaultArg1;
   }

   private final static transient LogManager logger = LogManager.getInstance(Form.class);
   private HashMap propMap = new HashMap();

   public static synchronized int createFormID() {
      logger.logDebug("createFormID: "+formIDSerial);
      return formIDSerial++;
   }

   public String getFormID() {
      return formID;
   }

   public void setFormID(String formID) {
      this.formID = formID;
   }

   public Form() {
      setFormID(String.valueOf(createFormID()));
   }

   public void close() {
      SessionManager.getInstance().close(this);
   }

   public void onFormCreate() {

   }

   public FormMeta getFormMeta() {
      return formMeta;
   }

   public void setFormMeta(FormMeta formMeta) {
      this.formMeta = formMeta;
   }

   public void enableProperty(String name) {
      enableProperty(name,"default");
   }

   public HashMap getPropMap() {
      return propMap;
   }

   public void enableProperty(String name, String type) {
      propMap.put(name,type);
   }

   public void disableProperties() {
      propMap.clear();
   }

   public String getStActionEvent() {
      return stActionEvent;
   }

   public void setStActionEvent(String stActionEvent) {
      this.stActionEvent = stActionEvent;
   }

   public void setProperty(String propname, Object value) {
      if (value instanceof String) {
         value=ConvertUtil.getString((String)value);
      };

      //logger.logDebug("setProperty: ("+propname+","+value+")");
      if ("action_event".equalsIgnoreCase(propname)) {
         setStActionEvent((String)value);
      } else if ("formid".equalsIgnoreCase(propname)) {
         if (!Tools.isEqual((Comparable)value,formID)) throw new RuntimeException("Invalid Form ID : "+value);
      } else if (propMap.containsKey(propname)) {
         final String propType = (String)propMap.get(propname);
         if ("check".equalsIgnoreCase(propType)) {
            super.setProperty(propname, ConvertUtil.getFlag((String)value));
         } else
            super.setProperty(propname, value);
      }
      else {
         setAttribute(propname, value);
         /*else
         throw new RuntimeException("Unrecognizable property: "+propname);*/
         //logger.logDebug("setProperty: Unrecognizable property: "+propname);
      }
   }

   public void beforeUpdateForm() {
      stActionEvent = null;
      stAlert = null;
   }

   public void afterUpdateForm() {

   }

   public void executeActionEvent() throws Exception {
      try {
         if (stActionEvent!=null) {
            logger.logDebug("executeActionEvent: invoking "+stActionEvent); 
            if (stActionEvent.equals("TabChange")) {
               final FormTab formTab = (FormTab)this.getProperty(defaultArg1);
               formTab.setActiveTab((String)getAttribute(defaultArg1+"_ctab"));
               return;
            }
            this.getClass().getMethod(stActionEvent,null).invoke(this,null);
         }
      } catch (Throwable e) {
         final String ticket = CruxController.putError(e);
         setStErrorTicket(ticket);
         //setError(e);
      }
   }

   public void setError(Throwable e) {
      while (e.getCause()!=null) e=e.getCause();
      e.printStackTrace();
      stAlert=e.toString();
   }

   public String getStAlert() {
      return stAlert;
   }

   public void setStAlert(String stAlert) {
      this.stAlert = stAlert;
   }

   public Form newForm(String formid, Form opener) throws Exception {
      return FormManager.getInstance().newForm(formid, opener);
   }

   public void show() {
      SessionManager.getInstance().show(this);
   }

   public Session getSession() {
      return SessionManager.getInstance().getSession();
   }

   public String getUserId() {
      return getSession().getStUserID();
   }

   public boolean isPropertyEnabled(String p) {
      return propMap.containsKey(p);
   }

   public Form getOpener() {
      return opener;
   }

   public void setOpener(Form opener) {
      this.opener = opener;
   }

   public void initialize() {
   }

   public boolean isReadOnly() {
      return readOnly;
   }

   public void setReadOnly(boolean readOnly) {
      this.readOnly = readOnly;
   }

   public String getDefaultArg2() {
      return defaultArg2;
   }

   public void setDefaultArg2(String defaultArg2) {
      this.defaultArg2 = defaultArg2;
   }

   public String getDefaultArg3() {
      return defaultArg3;
   }

   public void setDefaultArg3(String defaultArg3) {
      this.defaultArg3 = defaultArg3;
   }

   public String getDefaultArg4() {
      return defaultArg4;
   }

   public void setDefaultArg4(String defaultArg4) {
      this.defaultArg4 = defaultArg4;
   }

   public void redirect(String url) {
      tempRedirect = url;
   }

   public String getPresentation() {
      if (tempRedirect!=null) {
         try {
            return tempRedirect;
         } finally {
            tempRedirect=null;
         }
      }
      return formMeta.getStPresentation();
   }

   public long getStamp() {
      return stamp;
   }

   public void expire() {
      stamp=-1;
   }
   
   public boolean isAutoRefresh() {
      return autoRefresh;
   }

   public void setAutoRefresh(boolean autoRefresh) {
      this.autoRefresh = autoRefresh;
   }

    /**
     * @return the useHeader
     */
    public boolean isUseHeader() {
        return useHeader;
    }

    /**
     * @param useHeader the useHeader to set
     */
    public void setUseHeader(boolean useHeader) {
        this.useHeader = useHeader;
    }
}
