/***********************************************************************
 * Module:  com.crux.initializer.ejb.wds.SchemaUpdater
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 5:43:32 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb.wds;

import com.crux.common.parameter.Parameter;
import com.crux.util.SQLUtil;
import com.crux.util.LogManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

public class SchemaUpdater {
   private String dsOverride;

   private final static transient LogManager logger = LogManager.getInstance(SchemaUpdater.class);

   public SchemaUpdater(String stDS) {
      dsOverride = stDS;
   }

   public void doUpdateSchema(String SEQ_ID) throws Exception {

      final String [] scriplist = (String []) this.getClass().getField("scripts").get(null);

      final BigDecimal sv = Parameter.readNum(dsOverride, SEQ_ID,1);

      int schemaVersion = sv==null?0:sv.intValue();

      schemaVersion++;

      while (schemaVersion<=scriplist.length) {
         try {
            execQuery(scriplist[schemaVersion-1]);
         }
         catch (Exception e) {
            logger.logError("doUpdateSchema: "+e);
            e.printStackTrace();
         }
         Parameter.setNumber(dsOverride, SEQ_ID ,1,new BigDecimal(schemaVersion));
         schemaVersion++;
      }
   }

   public void execQuery(String q) throws Exception {
      final SQLUtil S = new SQLUtil(dsOverride);
      PreparedStatement PS = null;

      try {
         PS = S.setQuery(q);

         PS.execute();

      }
      finally {
         S.release();
      }
   }

   public final static transient String [] scripts = {
   };


}

/*























*/