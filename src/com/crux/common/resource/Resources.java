/***********************************************************************
 * Module:  com.crux.common.resource.Resources
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 9:11:17 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.resource;

import com.crux.util.LookUpUtil;

public class Resources {
   public static final class SalesOrder {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String SO_LIST = "SO_LIST";
      public final static transient String SO_CREATE = "SO_CREATE";
      public final static transient String SO_EDIT = "SO_EDIT";
      public final static transient String SO_APPROVE = "SO_APPROVE";
      public final static transient String SO_VIEW = "SO_VIEW";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }
   }

}
