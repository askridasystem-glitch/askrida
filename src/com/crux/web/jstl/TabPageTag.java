/***********************************************************************
 * Module:  com.crux.web.jstl.TabPageTag
 * Author:  Denny Mahendra
 * Created: Dec 31, 2005 9:27:05 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class TabPageTag extends BaseTag {
   private String name;
   public TabTag tab;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);    
      tab = (TabTag)findInStack(TabTag.class);
   }

   public int startTag() throws JspException {
      if (tab.formTab.isActive(name))
         return EVAL_BODY_INCLUDE;

      return SKIP_BODY;
   }
}
