/***********************************************************************
 * Module:  com.crux.sqi.SQIForm
 * Author:  Denny Mahendra
 * Created: Jan 21, 2008 7:36:30 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.sqi.*;
import com.crux.web.form.Form;
import com.crux.common.parameter.Parameter;
import com.crux.util.LogManager;
import com.crux.util.stringutil.StringUtil;
import com.crux.util.StringTools;
import com.crux.util.SQLUtil;
import com.crux.util.LogUtil;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

public class GLRoundedForm extends Form {
   private String sql;
   private String rpt;
   private String status;
   private String example = "(1 No Bukti) C211215151512700079 atau ( >1 No Bukti) C211215151512700079,C211218181512700014,C211211111100100192";

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example =  example;
    }

   private final static transient LogManager logger = LogManager.getInstance(GLRoundedForm.class);

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   public String getSql() {
      return sql;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public void execute() throws Throwable {

      if(sql!=null){

              String noBuktiArray[] = sql.split("[\\,]");
              String noBukti = "";

                if (noBuktiArray.length == 1) {
                    noBukti = "'"+ sql.trim().toUpperCase() +"'";
                } else if (noBuktiArray.length > 1) {

                    // C123456,C678904,C98765
                    for (int k = 0; k < noBuktiArray.length; k++) {

                        if(k>0) noBukti = noBukti + ",";

                        noBukti = noBukti + "'" + noBuktiArray[k] + "'";

                    }
                }

              logger.logInfo("+++++++ ROUNDED JURNAL NO BUKTI : "+ noBukti +" ++++++++");

              final SQLUtil S = new SQLUtil();

              String queryUpdate = "update gl_je_detail set debit = round(debit,2), credit = round(credit,2),"+
                                                " entered_debit = round(entered_debit,2),entered_credit = round(entered_credit,2) "+
                                                " where trx_no in ("+ noBukti  +")";

              try {
                 final PreparedStatement PS = S.setQuery(queryUpdate);

                 logger.logInfo("+++++++ ROUNDED JURNAL QUERY UPDATE : "+ queryUpdate +" +++++++++");

                 int j = PS.executeUpdate();

                  if (j!=0){
                      logger.logWarning("######## ROUNDED JURNAL BERHASIL QUERY UPDATE : "+ queryUpdate +" ++++++++");

                      setStatus("Proses Berhasil");
                  }else{
                      setStatus("Proses Gagal, Cek Nomor Bukti");
                  }

              } finally {
                 S.release();
              }

              sql="";
      }else{
          setStatus("Proses Gagal, Nomor Bukti belum di input");
      }
      
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

