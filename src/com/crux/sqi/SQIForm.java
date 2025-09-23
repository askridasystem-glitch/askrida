/***********************************************************************
 * Module:  com.crux.sqi.SQIForm
 * Author:  Denny Mahendra
 * Created: Jan 21, 2008 7:36:30 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.sqi;

import com.crux.web.form.Form;
import com.crux.common.parameter.Parameter;
import com.crux.util.stringutil.StringUtil;
import com.crux.util.StringTools;
import com.crux.util.SQLUtil;
import com.crux.util.LogUtil;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

public class SQIForm extends Form {
   private String sql;
   private String rpt;

   public String getSql() {
      return sql;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public void execute() throws Throwable {
      StringReader sr = new StringReader(sql);

      BufferedReader br = new BufferedReader(sr);

      try {
         runScript(br);
      } finally {
         br.close();
         sr.close();
      }

      sql="";
   }

   protected void runScript(final BufferedReader rdr) throws Throwable {
      String s;
      StringBuffer q = new StringBuffer();
      int sci = 0;
      int sco = 0;

      boolean enterBlockMode = false;
      boolean exitBlockMode = false;

      StringBuffer report = new StringBuffer();

      int n;

      while ((s = rdr.readLine()) != null) {
         final int cmt = s.indexOf("--");
         if (cmt >= 0) {
            s = s.substring(0, cmt);
         }

         String[] qz = StringUtil.split(s, '\n');

         s = qz[0];
//         final String q0 = s.split("\n")[0];

         n = s.indexOf("<<<");
         if (n >= 0) {
            s = StringTools.delete(s, n, 3);
            enterBlockMode = true;
         }

         if (enterBlockMode) {
            n = s.indexOf(">>>");
            if (n >= 0) {
               s = StringTools.delete(s, n, 3);
               exitBlockMode = true;
            }
         }

         if (s.length() > 0)
            q.append(s).append('\n');

         boolean evaluate =
                 (!enterBlockMode && (s.indexOf(';') >= 0)) ||
                 (enterBlockMode && exitBlockMode);

         if (evaluate) {
            String s2 = q.toString().trim();

            if (!enterBlockMode) s2 = StringUtil.split(q.toString().trim(), ';')[0];

            enterBlockMode = false;
            exitBlockMode = false;
            evaluate = false;

            if (s2.length() > 0) {
               sci++;
               if (sci > sco) {
                  try {
                     execQuery(s2);
                  } catch (Throwable e) {
                     e.printStackTrace();

                     throw new RuntimeException("Error in statement #"+sci+"("+e+")",e);
                  }
               }
            }
            q = new StringBuffer();
         }
      }

   }

   protected void execQuery(String q) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery(q);

         PS.execute();
      } finally {
         S.release();
      }
   }
}

