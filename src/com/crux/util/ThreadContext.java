/***********************************************************************
 * Module:  com.crux.util.ThreadContext
 * Author:  Denny Mahendra
 * Created: Nov 20, 2005 9:35:18 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.HashMap;

public class ThreadContext extends HashListMap {
   private static HashMap instances = new HashMap();
   private Thread trd;

   public static ThreadContext getInstance() {
      final String threadName = Thread.currentThread().getName();

      ThreadContext ctx = (ThreadContext)instances.get(threadName);

      if (ctx==null) {
         ctx = new ThreadContext();
         instances.put(threadName, ctx);
      }

      return ctx;
   }

   private ThreadContext() {
      trd = Thread.currentThread();
   }
}