/***********************************************************************
 * Module:  com.crux.common.parameter.lov.UWRIT_DEF_ITREATY
 * Author:  Denny Mahendra
 * Created: Jul 13, 2006 4:54:33 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.LookUpUtil;
import com.crux.util.ListUtil;

public class UWRIT_DEF_ITREATY extends LookUpUtil {
   public UWRIT_DEF_ITREATY() throws Exception {
      super(ListUtil.getLookUpFromQuery("select ins_treaty_id, treaty_name from ins_treaty where retro_cess_flag='Y' order by treaty_name"));
   }
}
