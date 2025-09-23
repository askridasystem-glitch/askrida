/***********************************************************************
 * Module:  com.crux.web.jstl.Script
 * Author:  Denny Mahendra
 * Created: May 20, 2005 10:33:16 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;


public class Script implements Tag {
   private Tag parent;
   private ListBox listBox;

   public void setPageContext(PageContext pageContext) {

   }

   public void setParent(Tag tag) {
      parent = tag;
   }

   public Tag getParent() {
      return null;
   }

   public int doStartTag() throws JspException {
      return 0;
   }

   public int doEndTag() throws JspException {
      return 0;
   }

   public void release() {

   }
}
