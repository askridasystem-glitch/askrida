/***********************************************************************
 * Module:  com.crux.common.parameter.lov.LOG_LEVEL
 * Author:  Denny Mahendra
 * Created: Aug 13, 2004 1:46:30 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.LookUpUtil;
import org.apache.log4j.Level;

public class LOG_LEVEL extends LookUpUtil {
   public LOG_LEVEL() {
      add(String.valueOf(Level.FATAL_INT),"FATAL");
      add(String.valueOf(Level.ERROR_INT),"ERROR");
      add(String.valueOf(Level.WARN_INT),"WARNING");
      add(String.valueOf(Level.INFO_INT),"INFO");
      add(String.valueOf(Level.DEBUG_INT),"DEBUG");
   }
}
