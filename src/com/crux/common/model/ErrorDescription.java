/***********************************************************************
 * Module:  com.crux.common.model.ErrorDescription
 * Author:  Denny Mahendra
 * Created: Mar 29, 2004 1:39:48 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import java.util.ArrayList;

public class ErrorDescription extends ArrayList {
   public void add(Object key, String stDescription) {
      final ErrorDetail d = new ErrorDetail(key,stDescription);
      add(d);
   }

   public void add(ErrorDescription e) {
      if (e!=null)
         super.addAll(e);
   }

   public ErrorDescription() {
   }

   public String toString() {
      final StringBuffer sz = new StringBuffer();
      for (int i = 0; i < this.size(); i++) {
         ErrorDetail ed = (ErrorDetail) this.get(i);
         sz.append(ed.getStMessage());sz.append(" \n ");
      }
      return sz.toString();
   }
}
