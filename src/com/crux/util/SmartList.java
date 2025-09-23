/***********************************************************************
 * Module:  com.crux.util.SmartList
 * Author:  Denny Mahendra
 * Created: Feb 12, 2006 5:33:11 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.ArrayList;

public class SmartList extends ArrayList {
   public Object set(int index, Object element) {
      while (index>=size()) super.add(null);
      return super.set(index, element);
   }

   public Object get(int index) {
      if (index>=size()) return null;
      return super.get(index);
   }
}
