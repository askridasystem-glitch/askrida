/***********************************************************************
 * Module:  com.crux.util.EzFormat
 * Author:  Denny Mahendra
 * Created: Mar 7, 2006 9:09:55 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EzFormat {
   private String fmt;
   private String fmt_pad;
   private char padDir;
   private int padSize;
   private String fmt_df;
   private DecimalFormat df;

   // sample : PADR20|DF[##0.00]

   public EzFormat(String formatString) {
      fmt = formatString;

      final String[] fmtx = fmt.split("[\\|]");

      for (int i = 0; i < fmtx.length; i++) {
         String s = fmtx[i];

         if (s.indexOf("PAD")==0) {
            fmt_pad = s.substring(3,s.length());
            padDir = fmt_pad.charAt(0);
            padSize = Integer.parseInt(fmt_pad.substring(1,fmt_pad.length()));
         }
         else if (s.indexOf("DF")==0) {
            fmt_df = s.substring(2,s.length());
         }
         else {
            fmt_df=s;
         }
      }
   }

   public String format(Object x){

      if (x instanceof BigDecimal)
         if (fmt_df!=null) {
            x = getDecimalFormat().format(((BigDecimal)x).doubleValue());
         }

      if (fmt_pad!=null) {
         if (padDir == 'R') x = StringTools.rightPad((String)x,' ',padSize);
         else if (padDir == 'L') x = StringTools.leftPad((String)x,' ',padSize);
      }

      return (String) x;
   }

   private DecimalFormat getDecimalFormat() {
      if (df==null)
         df = new DecimalFormat(fmt_df);

      return df;
   }

   public static void main(String [] args) {
      final EzFormat ezFormat = new EzFormat("PADL20|DF0.00;(0.00)");

      System.out.println(ezFormat.format(new BigDecimal(-432.43323)));
   }
}
