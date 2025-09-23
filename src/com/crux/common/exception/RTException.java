/***********************************************************************
 * Module:  com.crux.common.exception.RTException
 * Author:  Denny Mahendra
 * Created: Nov 9, 2005 2:43:32 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.exception;

import com.crux.common.config.Config;

public class RTException extends RuntimeException {
   public RTException() {
   }

   public RTException(String message) {
      super(message);
   }

   public RTException(Throwable cause) {
      super(cause.toString());
      if (Config.JRE_1_4)
         initCause(cause);
      else
         cause.printStackTrace();
   }

   public RTException(String message, Throwable cause) {
      super(message + cause.toString());

      if (Config.JRE_1_4)
         initCause(cause);
      else
         cause.printStackTrace();
   }

   public Throwable getCause() {
      if (Config.JRE_1_4)
         return super.getCause();

      return null;
   }
}
