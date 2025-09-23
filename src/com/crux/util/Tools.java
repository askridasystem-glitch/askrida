/***********************************************************************
 * Module:  com.crux.util.Tools
 * Author:  Denny Mahendra
 * Created: Apr 12, 2004 1:22:36 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class Tools {
   public static int compare(Comparable a, Comparable b) {
      if (a==null) {
         if (b==null) return 0; else return -1;
      } else {
         if (b==null) return 1; else return zcompareTo(a,b);
      }
   }

   private static int zcompareTo(Comparable a, Comparable b) {
      if (a instanceof Date) {
         final long a1 = ((Date) a).getTime();
         final long a2 = ((Date) b).getTime();

         if (a1<a2) return -1;
         else if (a1>a2) return 1;
         else return 0;
      }

      if (a instanceof String) {
         return stringCompare((String)a,(String)b);
      }

      return a.compareTo(b);
   }

   private static int stringCompare(String a, String b) {

      if (a.length()<b.length()) return -1;
      else if (a.length()>b.length()) return 1;

      for (int i=0;i<a.length();i++) {
         if (a.charAt(i)==b.charAt(i)) continue;

         if (a.charAt(i)<b.charAt(i)) return -1;
         else if (a.charAt(i)>b.charAt(i)) return 1;
      }

      return 0;
   }

   public static boolean isEqual(Comparable a, Comparable b) {
      return compare(a,b)==0;
   }

   public static boolean isNo(String flag) {
      return !"Y".equalsIgnoreCase(flag);
   }

   public static boolean isYes(String flag) {
      return "Y".equalsIgnoreCase(flag);
   }

   public static Object min(Comparable o1,Comparable o2) {
      if (o1==null) return o2;
      if (o2==null) return o1;
      if (o1.compareTo(o2)>0) return o2; else return o1;
   }

   public static Object max(Comparable o1,Comparable o2) {
      if (o1==null) return o2;
      if (o2==null) return o1;
      if (o1.compareTo(o2)<0) return o2; else return o1;
   }

   public static HashMap getPropMap(String propString) {
      final HashMap m = new HashMap();

      if(propString==null) return m;

      final String[] spx = propString.split("[\\|]");


      for (int i = 0; i < spx.length; i++) {
         String s = spx[i];

         final String[] r = s.split("[\\=]");

         if(r.length!=2) throw new RuntimeException("Invalid prop string :"+propString);

         m.put(r[0],r[1]);
      }

      return m;
   }

   public static String getDigit(String code, int i) {
      if ((code==null) || (code.length()<1)) code="";

      code=code+"000000000000000000";

      code=code.substring(0,i);

      return code;
   }

   public static String getDigitRightJustified(String code, int i) {
      if ((code==null) || (code.length()<1)) code="";

      code = StringTools.leftPad(code,'0',i);

      int p = code.length()-i;

      code=code.substring(p,code.length());

      return code;
   }
   
   public static void main(String args[]){
       BigDecimal ratioKomisi = new BigDecimal(0.3500);
       BigDecimal limit = new BigDecimal(0.3505);
       if(Tools.compare(ratioKomisi, limit)>0) System.out.println("OVER LIMIT");
       //if(Tools.compare(BDUtil.zero, BDUtil.one)<=1) System.out.println("-1 kurang dr 0");
        
      // if(BDUtil.biggerThan(new BigDecimal(2),BDUtil.one)) System.out.println("1 lebih dr 0");
       //if(BDUtil.lesserThan(new BigDecimal(-2),new BigDecimal(-1))) System.out.println("-1 kurang dr 0");
       
       BigDecimal premiTotal = new BigDecimal(24644000);
       BigDecimal premiNetto = new BigDecimal(16018600);
       System.out.println(BDUtil.div(BDUtil.sub(premiTotal, premiNetto), premiTotal,2));
   }
}
