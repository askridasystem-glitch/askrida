/***********************************************************************
 * Module:  com.crux.common.exception.LoginTimeOutException
 * Author:  Denny Mahendra
 * Created: Aug 16, 2004 1:26:38 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.exception;

public class LoginTimeOutException extends RuntimeException {
   public LoginTimeOutException() {
   }

   public LoginTimeOutException(String message) {
      super(message);
   }

   public LoginTimeOutException(String message, Throwable cause) {
      super(message, cause);
   }

   public LoginTimeOutException(Throwable cause) {
      super(cause);
   }
}
