/***********************************************************************
 * Module:  com.crux.util.oraapps.SQLUtilOra
 * Author:  Denny Mahendra
 * Created: Apr 24, 2004 1:30:32 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.oraapps;

import com.crux.util.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class SQLUtilOra extends SQLUtil {
   public Connection getConnection() throws Exception {
      if (connection == null)
         connection = OraConnectionCache.getInstance().getConnection();

      return connection;
   }

   public SQLUtilOra() throws Exception {
   }

   public static String getSequence(String stSequenceName) throws Exception {
      final SQLUtilOra S = new SQLUtilOra();

      try {
         final PreparedStatement PS = S.setQuery("select "+stSequenceName+".nextval from dual");

         final ResultSet RS = PS.executeQuery();

         if (RS.next())
            return RS.getString(1);

         return null;
      }
      finally {
         S.release();
      }
   }
}
