/***********************************************************************
 * Module:  com.crux.common.parameter.ParameterPool
 * Author:  Denny Mahendra
 * Created: Jul 16, 2004 5:18:41 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter;

import java.util.HashMap;

public class ParameterPool {

   private HashMap pool = new HashMap();

   private static ParameterPool staticinstance;

   public static ParameterPool getInstance() {
      if (staticinstance == null) staticinstance = new ParameterPool();
      return staticinstance;
   }

   private ParameterPool() {
   }

   public Object get(String stKey, long seq) {
      if (seq>1) stKey+=seq;
      return pool.get(stKey);
   }

   public void put(String stKey, long seq, Object value) {
      if (seq>1) stKey+=seq;
      pool.put(stKey, value);
   }
}
