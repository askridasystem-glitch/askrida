/***********************************************************************
 * Module:  com.crux.common.model.ErrorDetail
 * Author:  Denny Mahendra
 * Created: Mar 29, 2004 1:41:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import java.io.Serializable;

public class ErrorDetail implements Serializable {
   private Object key;
   private Object stMessage;

   public ErrorDetail(Object key, Object stMessage) {
      this.key = key;
      this.stMessage = stMessage;
   }

   public Object getKey() {
      return key;
   }

   public void setKey(Object key) {
      this.key = key;
   }

   public String getStMessage() {
      return String.valueOf(stMessage);
   }

   public void setStMessage(String stMessage) {
      this.stMessage = stMessage;
   }

   public Object getStMessageObject() {
      return (String) stMessage;
   }

   public void setStMessageObject(Object stMessage) {
      this.stMessage = stMessage;
   }
}
