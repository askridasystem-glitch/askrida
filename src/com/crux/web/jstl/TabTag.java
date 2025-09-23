/***********************************************************************
 * Module:  com.crux.web.jstl.TabTag
 * Author:  Denny Mahendra
 * Created: Dec 29, 2005 8:56:49 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.common.controller.FormTab;
import com.crux.util.JSPUtil;

import javax.servlet.jsp.JspException;

public class TabTag extends BaseTag {
   private String name;
   public FormTab formTab;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
      formTab = (FormTab)parentFrame.getForm().getProperty(name);
   }

   public int startTag() throws JspException {
      final JSPUtil jspUtil = parentFrame.jspUtil;
      //print(jspUtil.formTabOpen(formTab));

      final StringBuffer sz = new StringBuffer();

      if (formTab.activeTab!=null) {
         sz.append("<input type=hidden name="+name+"_ctab value=\""+jspUtil.print(formTab.activeTab.tabID)+"\">");
      }
      sz.append("<table cellpadding=0 cellspacing=0 width=\"100%\" >");
      sz.append("<tr>");
      sz.append("<td background=\""+jspUtil.getImagePath()+"/tab-top.gif\">");

      sz.append("<table cellpadding=0 cellspacing=0>");
      sz.append("<tr style=\"cursor:hand\">");
      for (int i = 0; i < formTab.tabs.size(); i++) {
         FormTab.TabBean tb = (FormTab.TabBean) formTab.tabs.get(i);

         if (!tb.enabled) continue;
         
         final boolean isActive = formTab.activeTab==tb;

         sz.append("<td><img src=\""+jspUtil.getImagePath()+"/tab-"+(isActive?"A":"I")+"L.gif\"></td>");
         sz.append("<td onclick=\"f."+name+"_ctab.value='"+jspUtil.print(tb.tabID)+"';f.defaultArg1.value='"+name+"';f.action_event.value='TabChange'; f.submit();\" background=\""+jspUtil.getImagePath()+"/tab-"+(isActive?"A":"I")+"C.gif\">"+jspUtil.print(tb.caption)+"</td>");
         sz.append("<td><img src=\""+jspUtil.getImagePath()+"/tab-"+(isActive?"A":"I")+"R.gif\"></td>");

      }

      sz.append("</tr>");
      sz.append("</table>");


      sz.append(" </td>");
      sz.append(" </tr><tr><td>");

      sz.append("<table cellpadding=0 cellspacing=0 width=\"100%\" >");

      sz.append("<tr height=10>");
      sz.append("<td style=\"background-repeat:repeat\" background=\""+jspUtil.getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
      sz.append("<td width=\"10\"></td>");
      sz.append("<td></td>");
      sz.append("<td style=\"background-repeat:repeat\" background=\""+jspUtil.getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
      sz.append("</tr>");

      sz.append("<tr>");
      sz.append("<td style=\"background-repeat:repeat\" background=\""+jspUtil.getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
      sz.append("<td></td>");
      sz.append("<td>");


      print(sz.toString());

      return EVAL_BODY_INCLUDE;
   }

   public int endTag() throws JspException {
      print(parentFrame.jspUtil.formTabClose(formTab));
      return EVAL_PAGE;
   }
}
