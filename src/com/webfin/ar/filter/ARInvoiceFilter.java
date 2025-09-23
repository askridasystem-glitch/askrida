/***********************************************************************
 * Module:  com.webfin.ar.filter.ARInvoiceFilter
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 11:40:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.filter;

import com.crux.common.model.Filter;
import java.util.Date;

public class ARInvoiceFilter extends Filter {
   public String key;
   public String norek;
   public String trxID;
   public Date start_date;
}
