/***********************************************************************
 * Module:  com.crux.util.ReflectionHelper
 * Author:  Denny Mahendra
 * Created: Jun 19, 2006 10:56:25 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import java.util.HashMap;
import java.lang.reflect.Method;

public class ReflectionHelper {
   private Class aClass;
   private HashMap methodMap;
   private static HashMap pool = new HashMap();

   public ReflectionHelper(Object x) {
      aClass = x.getClass();
      Method[] methods = aClass.getMethods();

      methodMap = new HashMap();

      for (int i = 0; i < methods.length; i++) {
         Method method = methods[i];

         methodMap.put(method.getName(), method);
      }
   }

   public Method findMethod(String methodName) {
      return (Method) methodMap.get(methodName);
   }

   public static ReflectionHelper getInstance(Object x) {

      ReflectionHelper rfx = (ReflectionHelper) pool.get(x.getClass());

      if (rfx == null) {
         rfx = new ReflectionHelper(x);

         pool.put(x.getClass(), rfx);
      }

      return rfx;
   }

}

