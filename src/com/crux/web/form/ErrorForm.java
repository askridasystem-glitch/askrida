/***********************************************************************
 * Module:  com.crux.web.form.ErrorForm
 * Author:  Denny Mahendra
 * Created: May 28, 2006 4:10:27 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.form;

import com.crux.web.controller.CruxController;

public class ErrorForm extends Form {
   private Object errorDesc;

   public Object getErrorDesc() {
      return errorDesc;
   }

   public void setErrorDesc(Object errorDesc) {
      this.errorDesc = errorDesc;
   }

   public void initialize() {
      super.initialize();

      final String ticket = (String)getAttribute("ticket");

      errorDesc = CruxController.getError(ticket);
   }
}
