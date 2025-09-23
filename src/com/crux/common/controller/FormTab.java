/***********************************************************************
 * Module:  com.crux.common.controller.FormTab
 * Author:  Denny Mahendra
 * Created: Nov 3, 2005 2:05:09 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.util.Tools;
import com.crux.util.LOV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;

public class FormTab implements Serializable {
   public void add(TabBean tabBean) {
      tabs.add(tabBean);
      tabMap.put(tabBean.tabID,tabBean);
      if (activeTab==null) setActiveTab(tabBean.tabID);
   }

   public void setActiveTab(String tabPremi) {
      final TabBean tb = (TabBean)tabMap.get(tabPremi);
      activeTab = tb;
   }

   public boolean isActive(String tbn) {
      if(activeTab==null)return false;

      return Tools.isEqual(activeTab.tabID, tbn);
   }

   private TabBean getTab(String tbn) {
      final TabBean tb = (TabBean)tabMap.get(tbn);

      return tb;
   }

   public void enable(String s, boolean en) {
      final TabBean tb = (TabBean)tabMap.get(s);
      tb.enabled = en;
   }

   public FormTab() {
   }

   public FormTab(LOV lov) {

      Iterator it = lov.getCodeIterator();

      while (it.hasNext()) {
         String code = (String) it.next();

         TabBean tb = new TabBean(code, lov.getComboDesc(code), true);

         add(tb);
      }
   }


   public static class TabBean implements Serializable {
      public String tabID;
      public String caption;
      public boolean enabled;

      public TabBean(String tabID, String caption, boolean enabled) {
         this.tabID = tabID;
         this.caption = caption;
         this.enabled = enabled;
      }
   }

   public ArrayList tabs = new ArrayList();
   public HashMap tabMap = new HashMap();
   public TabBean activeTab;
   public String tabName;
}
