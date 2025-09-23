/***********************************************************************
 * Module:  com.crux.report.helper.SampleDataSource
 * Author:  Denny Mahendra
 * Created: Jun 17, 2005 10:18:20 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.report.helper;

import com.crux.common.model.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class SampleDataSource extends DataSource {
   int c=1;

   public void initialize() throws Exception {
      getDate(getParam("ddd"));
   }

   public void release() throws Exception {
   }

   public boolean next() throws JRException {
      return (c-->=0);
   }

   public Object getFieldValue(JRField jrField) throws JRException {
      return getParam(jrField.getName());
   }
}
