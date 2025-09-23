/***********************************************************************
 * Module:  com.crux.util.session.SessionDataMgr
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:42:21 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.session;

import java.util.HashMap;

public class SessionDataMgr {
   private Class helperClass;

   private HashMap dataMap = new HashMap();

   public SessionDataMgr(Class helperClass) {
      this.helperClass = helperClass;
   }

   public void put(String stAttribute, Object o) {
      SessionData sessionData = (SessionData)dataMap.get(stAttribute);

      if (sessionData == null) {
         sessionData = new SessionData();
         dataMap.put(stAttribute, sessionData);
      }

      sessionData.data = o;
   }
}
