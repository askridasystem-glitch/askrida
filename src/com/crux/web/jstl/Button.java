/***********************************************************************
 * Module:  com.crux.web.jstl.Button
 * Author:  Denny Mahendra
 * Created: May 20, 2005 1:38:53 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.util.JSPUtil;
import com.crux.web.controller.SessionManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class Button extends BaseTag {
   private String event;
   private String clientEvent;
   private boolean validate;
   private boolean disabled;
   private boolean show=true;
   private String action;
   private String confirm;
   private String name;
   private String resourceid;
   private String flags;
   private String text;
   private String size;
   private boolean defaultRO;

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);

      disabled=false;
   }

   public boolean isDefaultRO() {
      return defaultRO;
   }

   public void setDefaultRO(boolean defaultRO) {
      this.defaultRO = defaultRO;
   }

   public String getConfirm() {
      return confirm;
   }

   public void setConfirm(String confirm) {
      this.confirm = confirm;
   }

   public boolean isShow() {
      return show;
   }

   public void setShow(boolean show) {
      this.show = show;
   }

   public String getResourceid() {
      return resourceid;
   }

   public void setResourceid(String resourceid) {
      this.resourceid = resourceid;
   }

   public boolean isEnabled() {
      return !disabled;
   }

   public void setEnabled(boolean enabled) {
      this.disabled = !enabled;
   }

   public boolean isDisabled() {
      return disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   public String getClientEvent() {
      return clientEvent;
   }

   public void setClientEvent(String clientEvent) {
      this.clientEvent = tlMacro(clientEvent);
   }

   public boolean isValidate() {
      return validate;
   }

   public void setValidate(boolean validate) {
      this.validate = validate;
   }

   public String getEvent() {
      return event;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   private void checkRO() {
      final Boolean dRO = getDefaultRO();

      if (dRO!=null) this.disabled = dRO.booleanValue();
   }

   public int startTag() throws JspException {
      if (defaultRO) checkRO();

      final StringBuffer sz= new StringBuffer();

      if (!SessionManager.getInstance().hasResource(resourceid)) return 0;

      if (!show) return 0;

      sz.append("<button class=\"button\"");

      if (size!=null) sz.append(" style=\"width:"+size+"\"");

      if (name!=null) sz.append(" name=\""+name+"\"");

      if (disabled) sz.append(" disabled");

      sz.append(" onclick=\"");

      if (confirm!=null) {
         sz.append("if (confirm('"+JSPUtil.jsEscape(confirm)+"')) {");
      }

      if (clientEvent!=null) {
         sz.append(clientEvent).append(';');
      }

      if (event!=null) {
         sz.append("mform.action_event.value='").append(event).append("';");
         if (validate) sz.append("if (mform.onsubmit()) ");
         sz.append("mform.submit();");
      }

      if (confirm!=null) sz.append("}");

      sz.append("\"");

      sz.append(">");

      if (text!=null) sz.append(text);

      sz.append("</button>");

      /*if (validate)
         println("<input class=button type=submit onclick=\"mform.action_event.value='"+event+"'\" value=\""+text+"\">");
      else
         println("<button onclick=\"mform.action_event.value='"+event+"';mform.submit();\" value=\"\">"+text+"</button>");*/

      println(sz.toString());

      return 0;
   }

   public int endTag() throws JspException {
      //println("</input>");
      return 0;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getFlags() {
      return flags;
   }

   public void setFlags(String flags) {
      this.flags = flags;
   }

   public String getSize() {
      return size;
   }

   public void setSize(String size) {
      this.size = size;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
