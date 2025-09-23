/***********************************************************************
 * Module:  com.crux.util.LongUtil
 * Author:  Denny Mahendra
 * Created: Apr 12, 2004 1:58:07 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

public class LongUtil {
   public static Long zero = new Long(0);

   public static Long add(Long a, Long b) {
      if (a==null) {
         if (b==null)
            return new Long(0);
         else
            return b;
      } else {
         if (b==null)
            return a;
         else
            return new Long(a.longValue() + b.longValue());
      }
   }

   public static Long sub(Long a, Long b) {
      if (a==null) {
         if (b==null)
            return new Long(0);
         else
            return new Long(-b.longValue());
      } else {
         if (b==null)
            return a;
         else
            return new Long(a.longValue() - b.longValue());
      }
   }

   public static long getLong(Long l) {
      return l==null?0:l.longValue();
   }
}
