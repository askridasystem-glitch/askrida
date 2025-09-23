/***********************************************************************
 * Module:  com.crux.util.QueryProxy
 * Author:  Denny Mahendra
 * Created: Oct 20, 2004 10:08:28 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import com.crux.common.model.Filter;

public class QueryProxy extends DTOList {
   private String stSQL;
   private Class dtoClass;
   private Object params;
   private Filter f;
   private int flags;

   private final static transient LogManager logger = LogManager.getInstance(QueryProxy.class);

   protected QueryProxy() {
   }

   public QueryProxy(String _stSQL, Object _params, Class _DTOClass, Filter _f, int _flags) throws Exception {
      stSQL = _stSQL;
      params = ObjectCloner.deepCopy(_params);
      dtoClass = _DTOClass;
      f = (Filter) ObjectCloner.deepCopy(_f);
      flags = _flags;
   }

   public DTOList getDTOList() throws Exception {
      logger.logDebug("getDTOList: retrieving proxy ...");
      final DTOList l = ListUtil.getDTOListFromQuery(stSQL,params,dtoClass,f,flags);
      l.setAttributes(this.getAttributes());
      return l;
   }
}
