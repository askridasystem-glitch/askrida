/***********************************************************************
 * Module:  com.crux.web.jstl.ParamTag
 * Author:  Denny Mahendra
 * Created: Sep 3, 2006 12:26:08 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

public class ParamTag extends BaseTag {
   private String name;
   private String value;
   public BaseTag paramParent;

   public static interface ParamAcceptingTag {

   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);    //To change body of overridden methods use File | Settings | File Templates.
      paramParent = (BaseTag)findInStack(ParamAcceptingTag.class);
   }

   public int startTag() throws JspException {
      return super.startTag();    //To change body of overridden methods use File | Settings | File Templates.
   }

   public int endTag() throws JspException {
      paramParent.getChilds().add(this);
      return super.endTag();
   }
}
