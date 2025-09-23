/***********************************************************************
 * Module:  com.crux.common.parameter.lov.UWRIT_CURRENT_COMPANY
 * Author:  Denny Mahendra
 * Created: Jul 13, 2006 6:13:10 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.parameter.lov;

import com.crux.util.LookUpUtil;
import com.crux.util.ListUtil;

public class UWRIT_COINSKREASI_COMPANY extends LookUpUtil {
   public UWRIT_COINSKREASI_COMPANY() throws Exception {
      super(ListUtil.getLookUpFromQuery("select ent_id, ent_name from ent_master where ins_company_flag='Y' order by ent_name"));
   }
}
