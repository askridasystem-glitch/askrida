/***********************************************************************
 * Module:  com.webfin.gl.codes.GLCodes
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 10:17:13 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.codes;

import com.crux.util.LookUpUtil;

import java.util.Calendar;

public class GLCodes {
   public static final class JournalType {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String REVERSING = "V";
      public final static transient String RECURRING = "R";
      public final static transient String GENERAL = "G";
      public final static transient String SYSTEM = "S";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil()
                    .add(REVERSING, "Reversing")
                    .add(RECURRING, "Recurring")
                    .add(GENERAL, "General")
                    .add(SYSTEM, "System")
                    ;
         }

         return lookUp;
      }
   }

   public static final class JournalFreq {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String MONTHLY = "1";
      public final static transient String BIMONTHLY = "2";
      public final static transient String QUARTERLY = "3";
      public final static transient String SEMIANUALLY = "4";
      public final static transient String YEARLY = "5";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil()
                    .add(MONTHLY,"Monthly")
                    .add(BIMONTHLY,"Bi-monthly")
                    .add(QUARTERLY,"Quarterly")
                    .add(SEMIANUALLY,"Semi Anually")
                    .add(YEARLY,"Yearly")
                    ;
         }

         return lookUp;
      }
   }

   public static final class Years {
      public final static transient String CODEGROUP = "CODE";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();

            final Calendar cld = Calendar.getInstance();

            final int cyear = cld.get(Calendar.YEAR);

            for (int i=cyear-15;i<=cyear;i++) {
               lookUp.add(String.valueOf(i),String.valueOf(i));
            }
         }

         return lookUp;
      }
   }

   public static final class GLPeriods {
      public final static transient String CODEGROUP = "CODE";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();

            for (int i=1;i<=13;i++) {
               lookUp.add(String.valueOf(i),String.valueOf(i));
            }
         }

         return lookUp;
      }
   }

   public static final class YearsForAccounting {
      public final static transient String CODEGROUP = "CODE";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();

            final Calendar cld = Calendar.getInstance();

            final int cyear = cld.get(Calendar.YEAR);

            for (int i=2014;i<=cyear;i++) {
               lookUp.add(String.valueOf(i),String.valueOf(i));
            }
         }

         return lookUp;
      }
   }

}
