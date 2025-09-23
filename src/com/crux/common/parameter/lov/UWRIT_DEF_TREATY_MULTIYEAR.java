/***********************************************************************
 * Module:  com.crux.common.parameter.lov.UWRIT_DEF_OTREATY
 * Author:  Denny Mahendra
 * Created: Jul 13, 2006 4:54:21 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.ListUtil;
import com.crux.util.LookUpUtil;

public class UWRIT_DEF_TREATY_MULTIYEAR extends LookUpUtil {
   public UWRIT_DEF_TREATY_MULTIYEAR() throws Exception {
      super(ListUtil.getLookUpFromQuery("select ins_treaty_id, treaty_name from ins_treaty where not(retro_cess_flag='Y') order by treaty_name"));
   }
}