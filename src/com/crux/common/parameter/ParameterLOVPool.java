/***********************************************************************
 * Module:  com.crux.common.parameter.ParameterLOVPool
 * Author:  Denny Mahendra
 * Created: Aug 13, 2004 3:24:29 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter;

import com.crux.util.LookUpUtil;

import java.util.HashMap;

public class ParameterLOVPool {
   private static ParameterLOVPool staticinstance;
   private static HashMap hmLOV;

   public static ParameterLOVPool getInstance() {
      if (staticinstance == null) staticinstance = new ParameterLOVPool();
      return staticinstance;
   }

   private ParameterLOVPool() {
   }

   public static LookUpUtil getLOV(String stParameterID) throws Exception{
      if (hmLOV == null)
         hmLOV = new HashMap();

      LookUpUtil lu = (LookUpUtil)hmLOV.get(stParameterID);

      if (lu==null) {
         try {
            final String clsName = "com.crux.common.parameter.lov."+stParameterID;
            System.out.println("loading class :"+clsName);
            final Class cls = Class.forName(clsName);
            lu = (LookUpUtil) cls.newInstance();
         }
         catch (IllegalAccessException e) {
            lu = new LookUpUtil();
         }
         catch (ClassNotFoundException e) {
            lu = new LookUpUtil();
         }
         hmLOV.put(stParameterID, lu);
      }

      if (lu.size() == 0) lu = null;

      return lu;
   }
}
