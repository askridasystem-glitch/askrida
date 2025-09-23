/***********************************************************************
 * Module:  com.crux.common.model.RecordAudit
 * Author:  Denny Mahendra
 * Created: Mar 16, 2004 2:06:59 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

/**
 * Views must implement this interface and fill up userid & transaction date in the dto to
 * obtain automatic fill of audit fields (create_who, create_date, change_who, change_date)
 */
public interface RecordAudit {
}
