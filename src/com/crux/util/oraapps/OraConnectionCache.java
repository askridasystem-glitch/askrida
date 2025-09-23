/***********************************************************************
 * Module:  com.crux.util.oraapps.OraConnectionCache
 * Author:  Denny Mahendra
 * Created: Apr 8, 2004 9:16:31 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.oraapps;

import com.crux.common.config.DataSourceNames;
import com.crux.util.ConnectionCache;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class OraConnectionCache {
   private static OraConnectionCache staticinstance;

   public static OraConnectionCache getInstance() throws Exception {
      if (staticinstance == null) staticinstance = new OraConnectionCache();
      return staticinstance;
   }

   private OraConnectionCache() throws Exception {
   }

   public Connection getConnection() throws Exception {
      return ConnectionCache.getInstance().getConnection(DataSourceNames.SECONDARY_DS);
   }
}
