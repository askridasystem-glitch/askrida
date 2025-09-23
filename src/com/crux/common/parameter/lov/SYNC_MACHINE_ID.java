/***********************************************************************
 * Module:  com.crux.common.parameter.lov.SYNC_MACHINE_ID
 * Author:  Denny Mahendra
 * Created: Aug 13, 2004 3:44:35 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.ListUtil;
import com.crux.util.LookUpUtil;

public class SYNC_MACHINE_ID extends LookUpUtil {
   public SYNC_MACHINE_ID() throws Exception {
      super(ListUtil.getLookUpFromQuery("select branchid, branchname from branch order by branchname"));
      if (true) throw new IllegalAccessException("disabled");
   }
}
