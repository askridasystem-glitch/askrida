/***********************************************************************
 * Module:  com.crux.web.jstl.PopSelectTag
 * Author:  Denny Mahendra
 * Created: Jan 10, 2006 10:51:40 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import javax.servlet.jsp.PageContext;


public class PopSelectTag extends BaseTag {
   private String name;
   private String form;
   private String title;
   public Field field;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getForm() {
      return form;
   }

   public void setForm(String form) {
      this.form = form;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);
      field = (Field) findInStack(Field.class);

      if (field!=null)
         field.getChilds().add(this);   
   }
}
