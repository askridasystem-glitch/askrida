/***********************************************************************
 * Module:  com.crux.common.codedecode.Codec
 * Author:  Denny Mahendra
 * Created: Mar 18, 2004 11:12:24 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.codedecode;

import com.crux.util.LookUpUtil;

public class Codec {
   public static final class ParameterType {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String STRING = "STRING";
      public final static transient String INTEGER = "INTEGER";
      public final static transient String DATE = "DATE";
      public final static transient String BOOLEAN = "BOOLEAN";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }
   }

   public static final class ResponseRefType {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String GRN_PO = "GRNPO";
      public final static transient String RECEIPT_CLEARANCE = "RCPT";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }
   }

   public static final class ResponseType {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String MESSAGE = "MSG";
      public final static transient String VALIDATION_OK = "V_OK";
      public final static transient String VALIDATION_NOT_OK = "V_NOK";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }
   }

   public static final class UIMode {
      public final static transient String CODEGROUP = "CODE";

      public final static transient String HIDDEN = "HIDDEN";
      public final static transient String READONLY = "READONLY";
      public final static transient String EDITABLE = "EDITABLE";
      public final static transient String MANDATORY = "MANDATORY";

      private static LookUpUtil lookUp = null;

      public static LookUpUtil getLookUp() {
         if (lookUp == null) {
            lookUp = new LookUpUtil();
         }

         return lookUp;
      }

      public static boolean isEditable(String mode) {
         return EDITABLE.equalsIgnoreCase(mode) || MANDATORY.equalsIgnoreCase(mode);
      }
   }

   public static final class JobLogStatus {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public final static transient String RUNNING = "RUN";
        public final static transient String OK = "OK";
        public final static transient String ERROR = "ERROR";

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }

            return lookUp;
        }
    }

     public static final class MonthCombo {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public final static transient String JANUARY = "0";
        public final static transient String FEBRUARY = "1";
        public final static transient String MARCH = "2";
        public final static transient String APRIL = "3";
        public final static transient String MAY = "4";
        public final static transient String JUNE = "5";
        public final static transient String JULY = "6";
        public final static transient String AUGUST = "7";
        public final static transient String SEPTEMBER = "8";
        public final static transient String OCTOBER = "9";
        public final static transient String NOVEMBER= "10";
        public final static transient String DECEMBER = "11";

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                                .add(JANUARY,"JANUARI")
                                .add(FEBRUARY,"FEBRUARI")
                                .add(MARCH,"MARET")
                                .add(APRIL,"APRIL")
                                .add(MAY,"MEI")
                                .add(JUNE,"JUNI")
                                .add(JULY,"JULI")
                                .add(AUGUST,"AGUSTUS")
                                .add(SEPTEMBER,"SEPTEMBER")
                                .add(OCTOBER,"OKTOBER")
                                .add(NOVEMBER,"NOVEMBER")
                                .add(DECEMBER,"DESEMBER");
            }

            return lookUp;
        }
    }
}
