/***********************************************************************
 * Module:  com.crux.lov.CruxLOVRegistry
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 7:34:09 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.lov;

import com.crux.util.ListUtil;
import com.crux.util.LOV;

import java.util.Map;

public class CruxLOVRegistry {
   public LOV LOV_Role(Map parameter) throws Exception{
      String key = (String) parameter.get("search");
      String val = (String) parameter.get("value");

      if (val!=null)
         return ListUtil.getLookUpFromQuery(
                 "select ROLE_ID, ROLE_ID || ' ' || ROLE_NAME from S_ROLES " +
                 "where role_id=? order by role_id",
                 new Object [] {val}
         );

      if (key==null) {
         return ListUtil.getLookUpFromQuery(
                 "select ROLE_ID, ROLE_ID || ' ' || ROLE_NAME from S_ROLES order by role_id"
         );
      }

      return ListUtil.getLookUpFromQuery(
              "select ROLE_ID, ROLE_ID || ' ' || ROLE_NAME from S_ROLES " +
              "where upper(ROLE_ID) like upper('%"+key+"%') or "+"upper(ROLE_NAME) like upper('%"+key+"%') order by role_id"
      );
   }
}
