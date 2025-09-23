/***********************************************************************
 * Module:  com.crux.web.jstl.PrintTag
 * Author:  Denny Mahendra
 * Created: Jul 8, 2005 1:29:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class PrintTag extends BaseTag {
   private String name;

   public void setName(String name) {
      this.name = name;
   }

   public int startTag() throws JspException {
      return SKIP_BODY;
   }

   public int endTag() throws JspException {
      try {
         out.print(String.valueOf(this.form.getAttribute(name)));
         return EVAL_PAGE;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
