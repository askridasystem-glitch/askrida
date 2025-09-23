/***********************************************************************
 * Module:  com.crux.session.Session
 * Author:  Denny Mahendra
 * Created: May 25, 2005 1:36:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.session;

import java.util.Date;

public interface Session {
   String getStUserID();

   void setStUserID(String stUserID);
   
   String getStUserName();

   void setStUserName(String stUserName);

   Date getDtTransactionDate();

   void setDtTransactionDate(Date dtTransactionDate);
   
   String getStShortName();

   void setStShortName(String stShortName);
  
}
