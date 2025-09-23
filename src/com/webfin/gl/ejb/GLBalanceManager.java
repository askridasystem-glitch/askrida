/***********************************************************************
 * Module:  com.webfin.gl.ejb.GLBalanceManager
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 10:33:39 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.webfin.gl.model.GLBalanceView;
import com.crux.util.SQLUtil;
import com.crux.util.BDUtil;

import java.util.HashMap;
import java.math.BigDecimal;

public class GLBalanceManager {
   private static GLBalanceManager staticinstance;
   public HashMap cache;

   public static GLBalanceManager getInstance() {
      if (staticinstance == null) staticinstance = new GLBalanceManager();
      return staticinstance;
   }

   private GLBalanceManager() {
      cache = new HashMap();
   }

   public void updateBalance(Long lgAccountID, Long lgPeriodNo, BigDecimal adj) throws Exception {
      final String key = lgAccountID.toString()+"/"+lgPeriodNo.toString();

      GLBalanceView glb = (GLBalanceView) cache.get(key);

      if (glb==null) {
         glb = new GLBalanceView();

         glb.setLgAccountID(lgAccountID);
         glb.setLgPeriondNo(lgPeriodNo);
         glb.setDbBalance(adj);

         glb.markNew();

         store(glb);

         glb.markUnmodified();

         cache.put(key,glb);

      } else {

         final GLBalanceView refGLB = glb;

         glb = new GLBalanceView();

         glb.setLgAccountID(lgAccountID);
         glb.setLgPeriondNo(lgPeriodNo);
         glb.setDbBalance(BDUtil.add(refGLB.getDbBalance(), adj));

         glb.markNew();

         store(glb);

         refGLB.setDbBalance(glb.getDbBalance());
      }
   }

   private void store(GLBalanceView glb) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         S.store(glb);
      } finally {
         S.release();
      }
   }
}
