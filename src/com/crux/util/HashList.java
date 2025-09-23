/***********************************************************************
 * Module:  com.crux.util.HashList
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 1:57:58 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class HashList extends HashMap {
   private ArrayList list;

   public Object put(Object key, Object value) {
      if (list==null) list=new ArrayList();
      list.add(value);
      return super.put(key, value);
   }

   public void putAll(Map m) {
      if (list==null) list=new ArrayList();
      list.addAll(m.values());
      super.putAll(m);
   }

   public ArrayList getList() {
      return list;
   }
}
