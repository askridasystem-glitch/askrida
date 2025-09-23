/***********************************************************************
 * Module:  com.crux.util.DTOMap
 * Author:  Denny Mahendra
 * Created: Oct 4, 2004 3:21:32 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.HashMap;

public class DTOMap extends HashMap {
   private boolean normalDeleteMode;

   public boolean isNormalDeleteMode() {
      return normalDeleteMode;
   }

   public void setNormalDeleteMode(boolean normalDeleteMode) {
      this.normalDeleteMode = normalDeleteMode;
   }
}
