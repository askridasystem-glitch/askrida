/***********************************************************************
 * Module:  com.crux.common.controller.ReportHandler
 * Author:  Denny Mahendra
 * Created: Jun 29, 2005 9:27:43 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import java.util.HashMap;

public abstract class ReportHandler extends Helper {
   private HashMap params;

   public void setParams(HashMap params) {
      this.params = params;
   }

   public String getParam(String paramName) {
      return (String) params.get(paramName);
   }

   public void setParam(String paramName, Object value) {
      if (params==null) params= new HashMap();
      params.put(paramName, value);
   }

   public Object getParamObject(String paramName) {
      return params.get(paramName);
   }

   public HashMap getParams() {
      return params;
   }

   public abstract void initialize() throws Exception ;

   public abstract void release() throws Exception ;
}
