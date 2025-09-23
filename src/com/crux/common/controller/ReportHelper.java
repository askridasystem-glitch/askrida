/***********************************************************************
 * Module:  com.crux.common.controller.ReportHelper
 * Author:  Denny Mahendra
 * Created: Jun 17, 2005 10:16:36 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import java.util.HashMap;


public abstract class ReportHelper extends ReportHandler {
   private ReportHelperListener listener;

   public void setListener(ReportHelperListener listener) {
      this.listener = listener;
   }

   public boolean isEvaluate() { return true; };

   public void afterEvaluate() {}

   public void concat(String stReportName) {
      listener.concat(stReportName, getParams());
   }

   public void concat(String stReportName, HashMap params) {
      listener.concat(stReportName, params);
   }
}
