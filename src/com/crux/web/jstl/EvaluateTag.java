/***********************************************************************
 * Module:  com.crux.web.jstl.EvaluateTag
 * Author:  Denny Mahendra
 * Created: Dec 29, 2005 10:36:07 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.JspException;

public class EvaluateTag extends BaseTag {
   private boolean when;

   public boolean isWhen() {
      return when;
   }

   public void setWhen(boolean when) {
      this.when = when;
   }

   public int startTag() throws JspException {
      if (when) return EVAL_BODY_INCLUDE; else return SKIP_BODY;
   }

   public int endTag() throws JspException {
      return 0;
   }
}
