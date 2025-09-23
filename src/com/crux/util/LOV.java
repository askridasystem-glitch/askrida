/***********************************************************************
 * Module:  com.crux.util.LOV
 * Author:  Denny Mahendra
 * Created: Nov 21, 2004 7:02:03 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import java.util.Iterator;

public interface LOV {
   public LOV emptyLOV  = new LookUpUtil();

   public LOV setLOValue(String stValue);

   public String getLOValue();

   String getComboContent(String stDefaultValue) throws Exception;

   String getComboContent() throws Exception;

   String getComboDesc(String stValue);

   String getComboDesc() throws Exception;

   Iterator getCodeIterator();

   String getJSObject() throws Exception;

   String [] getAttributeNames();

   LOV setNoNull();

   LOV setNullText(String s);

   int size();
}
