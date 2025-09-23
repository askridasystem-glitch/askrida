/***********************************************************************
 * Module:  com.crux.util.PropertyMap
 * Author:  Denny Mahendra
 * Created: Apr 14, 2007 11:59:27 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.HashMap;

public class PropertyMap extends HashMap {

   private static HashMap cache = new HashMap();

   public static PropertyMap getInstance(String input, String propDelimiter, String assignDelimiter) {
      String k = input+"/\\"+propDelimiter+"/\\"+assignDelimiter;
      PropertyMap x = (PropertyMap) cache.get(k);
      if (x==null) {
         x=new PropertyMap(input, propDelimiter, assignDelimiter);
         cache.put(k,x);
      }

      return x;
   }

   public PropertyMap( String input, String propDelimiter, String assignDelimiter) {

      if (input==null) return;

      String[] propz = input.split("[\\"+propDelimiter+"]");

      for (int i = 0; i < propz.length; i++) {
         String s = propz[i];

         String[] p = s.split("[\\"+assignDelimiter+"]");

         if (p.length==1) {
            put(p[0], new Boolean(true));
         }
         else if (p.length==2) {
            put(p[0], p[1]);
         }
      }
   }
}
