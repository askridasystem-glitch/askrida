/***********************************************************************
 * Module:  com.crux.util.fop.FOPUtil
 * Author:  Denny Mahendra
 * Created: Jun 5, 2006 2:37:47 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.fop;

import com.crux.util.JSPUtil;

import java.util.ArrayList;
import java.math.BigDecimal;

public class FOPUtil {
   public static String printAutoBlock(String stTxt) {

      if (stTxt==null) return null;

      if(stTxt.indexOf('\n')<0) return stTxt;

      final String[] lns = stTxt.split("\n");

      final StringBuffer sz = new StringBuffer();

      for (int i = 0; i < lns.length; i++) {
         String ln = lns[i];

         sz.append("<fo:block>");
         sz.append(JSPUtil.xmlEscape(ln));
         sz.append("</fo:block>");

      }

      return sz.toString();
   }

   public static String printColumnWidth(ArrayList columnRatios, double width, int precision, String measure) {
      ArrayList cw = computeColumnWidth(columnRatios, width,precision,measure);

      StringBuffer sz = new StringBuffer();

      for (int i = 0; i < cw.size(); i++) {
         String cs = (String) cw.get(i);
         sz.append("<fo:table-column column-width=\""+cs+"\"/>");

      }

      return sz.toString();
   }

   public static ArrayList computeColumnWidth(ArrayList columnRatios, double width, int precision, String measure) {
      double tw=0;
      for (int i = 0; i < columnRatios.size(); i++) {
         String x = String.valueOf(columnRatios.get(i));
         double w = Double.parseDouble(x);
         tw+=w;
      }

      ArrayList rl = new ArrayList();

      for (int i = 0; i < columnRatios.size(); i++) {
         String x = String.valueOf(columnRatios.get(i));
         double w = Double.parseDouble(x);
         double nw = w*width/tw;
         BigDecimal nbd = new BigDecimal(nw).setScale(precision,BigDecimal.ROUND_HALF_UP);
         rl.add(nbd.toString()+measure);
      }

      return rl;
   }


}
