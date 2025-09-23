/***********************************************************************
 * Module:  com.crux.common.parameter.lov.SU1_SYNC
 * Author:  Denny Mahendra
 * Created: Aug 25, 2004 4:05:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.LookUpUtil;
import com.crux.util.ListUtil;

public class SU1_SYNC extends LookUpUtil {
   public SU1_SYNC() throws Exception {
      super(ListUtil.getLookUpFromQuery("select config_id, config_id from warehouse_ftp order by config_id"));
   }
}
