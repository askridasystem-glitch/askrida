/***********************************************************************
 * Module:  com.crux.util.ConnectionCache
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:01:01 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.config.DataSourceNames;
import com.crux.initializer.ejb.DirectInitializer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;

public class ConnectionCache {
   protected static ConnectionCache staticinstance;
   protected InitialContext ic;
   public String JNDIPREFIX = "";
   private HashMap datasources = new HashMap();

   private final static transient LogManager logger = LogManager.getInstance(ConnectionCache.class);

   public static ConnectionCache getInstance() throws Exception {
      if (staticinstance == null) staticinstance = new ConnectionCache();
      return staticinstance;
   }

   protected ConnectionCache() throws Exception {
      try {
         Class.forName("weblogic.jndi.WLInitialContextFactory");
         java.util.Properties aProps = new java.util.Properties();
         aProps.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
         aProps.put(Context.PROVIDER_URL, "t3://localhost:7007/");
         ic = new InitialContext(aProps);
         JNDIPREFIX="";
      } catch(Exception e){
         ic = new InitialContext();
         JNDIPREFIX="java:/";
      }

   }

   public Connection getConnection() throws Exception {
      return this.getConnection(DataSourceNames.PRIMARY_DS);
      //return this.getConnection("java:comp/env/jdbc/LoginModuleEJBDataSource");
   }

   public Connection getConnection(String stDS) throws Exception {

      if (stDS.equalsIgnoreCase("PRIMARY")) stDS = DataSourceNames.PRIMARY_DS;
      else if(stDS.equalsIgnoreCase("DRC")) stDS = DataSourceNames.DRC_DS;
      else if(stDS.equalsIgnoreCase("GATEWAY")) stDS = DataSourceNames.GATEWAY_DS;
      else if(stDS.equalsIgnoreCase("GATEWAY_SQL")) stDS = DataSourceNames.GATEWAY_SQLSERVER_DS;
      else if(stDS.equalsIgnoreCase("MYSQLEDOC")) stDS = DataSourceNames.MYSQL_DS;
      else if(stDS.equalsIgnoreCase("KEAGENAN")) stDS = DataSourceNames.KEAGENAN_DS;
      else if(stDS.equalsIgnoreCase("DBDUMMY")) stDS = DataSourceNames.DUMMY_DS;
      else if(stDS.equalsIgnoreCase("DBHCIS")) stDS = DataSourceNames.HCIS_DS;
      else if(stDS.equalsIgnoreCase("RKAPDB")) stDS = DataSourceNames.RKAP_DS;
      else if(stDS.equalsIgnoreCase("WHOUSEDS")) stDS = DataSourceNames.WHOUSE_DS;
      else if(stDS.equalsIgnoreCase("SPPAONLINE")) stDS = DataSourceNames.SPPAOnline_DS;

      DataSource dsx = (DataSource) datasources.get(stDS);

      if (dsx==null) {
         dsx = (DataSource) ic.lookup(JNDIPREFIX+stDS);
         datasources.put(stDS, dsx);

         new DirectInitializer().initializeDataSource(stDS);
      }

      try {
         return dsx.getConnection();
      }
      catch (Exception e) {
         logger.logWarning("getConnection: Data source factory has failed ("+e+"), trying new lookup ...");
         ic = new InitialContext();
         dsx = (DataSource) ic.lookup(JNDIPREFIX+stDS); // retry once
         final Connection conn = dsx.getConnection();
         datasources.put(stDS, dsx);
         logger.logInfo("getConnection: new lookup success");
         return conn;
      }
   }
}
