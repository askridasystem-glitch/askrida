/***********************************************************************
 * Module:  com.crux.util.stringutil.StringUtil
 * Author:  Denny Mahendra
 * Created: Nov 9, 2005 3:28:30 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.stringutil;

import com.crux.util.StringTools;

import java.util.ArrayList;

public class StringUtil {

   static String [] cx = new String [] {};

   public static String [] split(String x, char splitchar) {
      if (x==null) return new String [] {};
      int j=0;
      final ArrayList l = new ArrayList();
      for (int i=0;i<x.length();i++) {
         if (x.charAt(i)==splitchar) {
            l.add(x.substring(j,i));
            j=i+1;
         }
      }

      int i = x.length();

      if (i-j>=0) l.add(x.substring(j,i));

      return (String[]) l.toArray(cx);
   }

   public static void main(String [] args) {
      final String[] sz = split("dsf",'/');

      for (int i = 0; i < sz.length; i++) {
         String s = sz[i];

         System.out.println(s);
      }
   }

   public static String truncate(String message, int n) {
      if (message!=null)
         if (message.length()>n)
            message = message.substring(0,n);

      return message;
   }

   public static String deBlank(String stDescription) {
      return stDescription==null?"":stDescription;
   }

   public static String removeTraillingZeroes(String s) {
      int p=0;

      x:
      for (int i=s.length()-1;i>=0;i--) {
         char c = s.charAt(i);
         switch (c) {
            case '0':
            case ' ':
               continue x;
            default:
               p=i;
               break x;
         }
      }

      if (p>0)
         if (s.charAt(p-1)=='.') p--;

      return s.substring(0,p);
   }
}
