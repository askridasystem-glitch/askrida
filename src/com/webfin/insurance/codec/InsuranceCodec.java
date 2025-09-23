/***********************************************************************
 * Module:  com.webfin.insurance.codec.InsuranceCodec
 * Author:  Denny Mahendra
 * Created: Oct 23, 2005 2:39:37 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.codec;

import com.crux.util.LookUpUtil;

public class InsuranceCodec {
   public static final class InsuraneEntityType {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String AGENT = "AGENT";
      public final static transient String BROKER = "BRKER";
      public final static transient String COASS = "COASS";
      
      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }
   }
}
