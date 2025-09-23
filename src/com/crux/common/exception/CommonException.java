/***********************************************************************
 * Module:  com.crux.common.exception.CommonException
 * Author:  Denny Mahendra
 * Created: May 17, 2004 11:02:09 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.exception;

import com.crux.common.model.ErrorDescription;

public class CommonException extends Exception {
   public ErrorDescription errDesc;

   public CommonException() {
   }

   public CommonException(String message) {
      super(message);
   }

   public CommonException(String message, Throwable cause) {
      super(message, cause);
   }

   public CommonException(Throwable cause) {
      super(cause);
   }

   public CommonException(ErrorDescription errDesc) {
      this.errDesc = errDesc;
   }

   public String getMessage() {
      if (errDesc != null) return errDesc.toString();
      return super.getMessage();
   }
}
