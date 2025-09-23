/***********************************************************************
 * Module:  com.crux.initializer.ejb.DirectInitializer
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 4:25:58 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb;

import com.crux.common.config.DataSourceNames;
import com.crux.util.LogManager;
import com.crux.initializer.ejb.wds.SchemaInitializer;
import com.crux.initializer.ejb.wds.SchemaUpdater;
import com.webfin.menu.MenuInitializer;

public class DirectInitializer {

   private final static transient LogManager logger = LogManager.getInstance(DirectInitializer.class);

   public void initializeDataSource(String stDataSourceName) throws Exception {
      logger.logDebug("initializeDataSource: "+stDataSourceName);

      if (DataSourceNames.PRIMARY_DS.equalsIgnoreCase(stDataSourceName)) {
         new SchemaInitializer(stDataSourceName)
               .doInitialize();

         new ParameterInitializer(stDataSourceName)
               .doInitialize();

         new SchemaUpdater(stDataSourceName)
               .doUpdateSchema("SCHEMA_VERSION");

         new MenuInitializer(stDataSourceName)
               .doInitialize();
      }

      /*
      if (DataSourceNames.DRC_DS.equalsIgnoreCase(stDataSourceName)) {
         new SchemaInitializer(stDataSourceName)
               .doInitialize();

         new SchemaUpdater(stDataSourceName)
               .doUpdateSchema("SCHEMA_VERSION");

      }*/

   }
}
