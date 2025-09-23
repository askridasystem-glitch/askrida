/***********************************************************************
 * Module:  com.crux.web.jstl.LOVLinkTag
 * Author:  Denny Mahendra
 * Created: Dec 31, 2005 10:56:46 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class LOVLinkTag extends BaseTag {
   private String name;
   private String link;
   private String value;
   private boolean clientLink = true;
   public Field field;

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLink() {
      return link;
   }

   public void setLink(String link) {
      this.link = link;
   }
   
   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);    //To change body of overridden methods use File | Settings | File Templates.

      field = (Field)findInStack(Field.class);
   }

   public int startTag() throws JspException {
      return super.startTag();    //To change body of overridden methods use File | Settings | File Templates.
   }

   public int endTag() throws JspException {
      field.getChilds().add(this);
      return super.endTag();
   }

   public boolean isClientLink() {
      return clientLink;
   }

   public void setClientLink(boolean clientLink) {
      this.clientLink = clientLink;
   }
}
