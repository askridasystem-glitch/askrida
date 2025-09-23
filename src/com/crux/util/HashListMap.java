/***********************************************************************
 * Module:  com.crux.util.HashListMap
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 1:56:38 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.ArrayList;
import java.util.HashMap;

public class HashListMap extends HashMap {
   public void add(String stKey, Object x) {
      ArrayList l = (ArrayList) get(stKey);
      if (l==null) {
         l = new ArrayList();
         put(stKey, l);
      }
      l.add(x);
   }
}
