/***********************************************************************
 * Module:  com.webfin.insurance.ejb.PostProcessorManager
 * Author:  Denny Mahendra
 * Created: Oct 8, 2006 11:32:16 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.ejb;

import com.crux.util.SQLUtil;

import java.sql.PreparedStatement;

public class PostProcessorManager {
   private static PostProcessorManager staticinstance;

   public static PostProcessorManager getInstance() {
      if (staticinstance == null) staticinstance = new PostProcessorManager();
      return staticinstance;
   }

   private PostProcessorManager() {
   }

   public void runPolicyProcess() throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement pS = S.setQuery("select summ(dc_update_policy(pol_id)) from ins_policy where nd_update is null;");

         pS.executeQuery();


      } finally {
         S.release();
      }
   }
}
