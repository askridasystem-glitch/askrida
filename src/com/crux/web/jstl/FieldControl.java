/***********************************************************************
 * Module:  com.crux.web.jstl.FieldControl
 * Author:  Denny Mahendra
 * Created: Aug 29, 2006 11:24:57 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class FieldControl extends BaseTag {
   private boolean when;
   private boolean readonly;
   private int cfields;

   /**
    * test wether readonly field is overriden
    * @return
    */
   public boolean isOVReadonly() {
      return when && (cfields & 1) != 0;
   }

   public boolean isReadonly() {
      return readonly;
   }

   public void setReadonly(boolean readonly) {
      cfields |= 1;
      this.readonly = readonly;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public boolean isWhen() {
      return when;
   }

   public void setWhen(boolean when) {
      this.when = when;
   }

   public int startTag() throws JspException {
      return EVAL_BODY_INCLUDE; 
   }

   public int endTag() throws JspException {
      return 0;
   }
}
