/***********************************************************************
 * Module:  com.crux.data.Date
 * Author:  Denny Mahendra
 * Created: May 12, 2005 9:08:37 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.data;

import java.sql.Timestamp;

public class Date extends java.util.Date {

   public Date(Timestamp tm) {
      super(tm==null?0:tm.getTime());
   }

   public Date(long date) {
      super(date);
   }
}
