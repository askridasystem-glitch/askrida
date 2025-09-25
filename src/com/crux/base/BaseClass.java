/***********************************************************************
 * Module:  com.crux.base.BaseClass
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:22:03 PM
 * Purpose: 
 ***********************************************************************/

//masuk ga nih tes sekali lagidasd

package com.crux.base;

import com.crux.util.ConvertUtil;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public abstract class BaseClass {
   private static HashMap reflectionCache = new HashMap();

   private HashMap attributes;
   public long timeStamp;
   public static Method NAcache;

   public Object getProperty(String propname) {
      try {		
         final int dotIndex = propname.indexOf('.');
         Method method;
         if (dotIndex<0)
            method = getGetterMethod(propname);
         else {
            final String mxProp = propname.substring(0,dotIndex).trim();
            final BaseClass subObject;

            if ((mxProp.charAt(0)=='[') && (mxProp.charAt(mxProp.length()-1)==']')) {
               final int index = Integer.parseInt(mxProp.substring(1,mxProp.length()-1));
               subObject = (BaseClass) ((List) this).get(index);
            } else {
               subObject = (BaseClass) getGetterMethod(mxProp).invoke(this,null);
            }

            if (subObject==null) return null;
            return subObject.getProperty(propname.substring(dotIndex+1,propname.length()));
         }

         if (method==null) return null;

         return method.invoke(this,null);
      } catch (Exception e) {
         e.printStackTrace();
         return getAttribute(propname);
         //throw new RuntimeException("Error accessing property : "+this.getClass()+"."+propname,e);
      }
   }

   public void NA() {

   }

   public Method getGetterMethod(String propname) throws Exception {
      final String key = this.getClass().getName()+"/"+propname+"/get";
      			
      Method method = (Method)reflectionCache.get(key);
      if (method==null) {
         try {
            method = this.getClass().getMethod(getGetterName(propname),null);
         } catch (Exception e) {
            method = getNA();
            reflectionCache.put(key,method);

            throw e;
         }
         reflectionCache.put(key,method);
      }

      if (getNA() == method) return null;

      return method;
   }

   private Method getNA() throws NoSuchMethodException {
      if (NAcache==null)
         NAcache = this.getClass().getMethod("NA",null);
      return NAcache;
   }

   public String getGetterName(String propname) {
      final char[] pch = propname.toCharArray();
      pch[0]=Character.toUpperCase(pch[0]);
      return "get"+new String(pch);
   }

   public void setProperty(String propname, Object value) {
      try {
         final int dotIndex = propname.indexOf('.');
         if (dotIndex<0) {
            Method method = getSetterMethod(propname);
            if (method==null) {
               setAttribute(propname, value);
               return;
            }
            method.invoke(this,new Object [] {autoConvert(value,method.getParameterTypes()[0])});
         } else {
            final String mxProp = propname.substring(0,dotIndex);
//            final BaseClass subObject = (BaseClass) getGetterMethod(mxProp).invoke(this,null);

            final BaseClass subObject;
            if ((mxProp.charAt(0)=='[') && (mxProp.charAt(mxProp.length()-1)==']')) {
               final int index = Integer.parseInt(mxProp.substring(1,mxProp.length()-1));
               subObject = (BaseClass) ((List) this).get(index);
            } else {
               subObject = (BaseClass) getGetterMethod(mxProp).invoke(this,null);
            }

            if (subObject==null) {
               setAttribute(propname, value);
               return;
               //throw new RuntimeException("Unable to set property for "+propname+" (null subobject at prop "+mxProp+")");
            }

            subObject.setProperty(propname.substring(dotIndex+1,propname.length()),value);
            return;
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private Object autoConvert(Object value, Class aClass) {
      if (value==null) return null;
      if (aClass.isAssignableFrom(value.getClass())) return value;
      if (value instanceof String) {
         final String st = (String) value;
         if (Date.class.isAssignableFrom(aClass)) return ConvertUtil.getDate(st);
         else if (BigDecimal.class.isAssignableFrom(aClass)) return ConvertUtil.getNum(st);
         else if (Long.class.isAssignableFrom(aClass)) return ConvertUtil.getLong(st);
         else if (Integer.class.isAssignableFrom(aClass)) return ConvertUtil.getInteger(st);
         else throw new RuntimeException("Unknown property type : "+aClass);
      }

      return null;
   };

   public Method getSetterMethod(String propname) throws NoSuchMethodException {
      final String key = this.getClass().getName()+"/"+propname+"/set";
      final Object o = reflectionCache.get(key);
      if ("NONE".equals(o)) return null;
      Method method = (Method)o;
      if (method==null) {
         final Method[] methods = this.getClass().getMethods();
         final String setterName = getSetterName(propname);
         for (int i = 0; i < methods.length; i++) {
            Method method1 = methods[i];
            if (method1.getName().equalsIgnoreCase(setterName)) {
               method = method1;break;
            }
         }
//         if (method==null) throw new RuntimeException("Method not found : "+setterName);
         if (method==null)
            reflectionCache.put(key,"NONE");
         else
            reflectionCache.put(key,method);
      }
      return method;
   }

   public String getSetterName(String propname) {
      final char[] pch = propname.toCharArray();
      pch[0]=Character.toUpperCase(pch[0]);
      return "set"+new String(pch);
   }

   public void setAttribute(String stKey, Object o) {
      if (attributes==null) attributes = new HashMap();
      attributes.put(stKey, o);
   }

   public Object getAttribute(String stKey) {
      if (attributes==null) attributes = new HashMap();
      return attributes.get(stKey);
   }
}

