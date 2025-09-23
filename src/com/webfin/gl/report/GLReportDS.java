/***********************************************************************
 * Module:  com.webfin.gl.report.GLReportDS
 * Author:  Denny Mahendra
 * Created: Feb 11, 2006 10:52:02 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.report;

import com.crux.common.model.DataSource;
import com.crux.util.DTOList;
import com.crux.util.CharRenderer;
import com.crux.util.BDUtil;
import com.webfin.gl.ejb.GLReportParam;
import com.webfin.gl.ejb.GLReportEngine;
import com.webfin.gl.model.GLReportView;
import com.webfin.gl.model.GLReportColumnView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.ArrayList;
import java.math.BigDecimal;

public class GLReportDS extends DataSource {
   private int cl;
   private CharRenderer charrend;

   public void initialize() throws Exception {
      final String glReportID = getString(getParam("glreportid"));

      GLReportParam param = new GLReportParam();

      param.stReportID = glReportID;

      final GLReportEngine gle = new GLReportEngine(param);

      final GLReportView glrpt = gle.fillReportData();

      final DTOList columns = glrpt.getColumns();

      charrend = new CharRenderer();

      /*for (int i = 0; i < columns.size(); i++) {
         GLReportColumnView col = (GLReportColumnView) columns.get(i);

         charrend.print(col.getLgColumnPosition(), col.getStColumnHeader());
      }*/

      charrend.newLine();

      final ArrayList lines = glrpt.getResult();

      for (int i = 0; i < lines.size(); i++) {
      Object[] line = (Object []) lines.get(i);

      for (int j = 0; j < columns.size(); j++) {
         GLReportColumnView col = (GLReportColumnView) columns.get(j);

         Object val = j>=line.length?null:line[j];

         if (val==null) val="";

         if (val instanceof BigDecimal) {
            if (col.getStColumnFormat()!=null) {
               val = col.format(val);
            }
         }

         charrend.print(col.getLgColumnPosition(), String.valueOf(val));
      }

      charrend.newLine();
   }

      cl = -1;
   }

   public void release() throws Exception {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public boolean next() throws JRException {
      cl++;
      return cl<charrend.getLineCount();
   }

   public Object getFieldValue(JRField jrField) throws JRException {
      final String fldName = jrField.getName();
      if ("dtltext".equalsIgnoreCase(fldName)) {
         return charrend.getLine(cl);
      } else if ("sumtext".equalsIgnoreCase(fldName)) {
         return "Summary Text";
      }

      return "";
   }
}
