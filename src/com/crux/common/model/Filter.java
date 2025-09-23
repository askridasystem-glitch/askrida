/***********************************************************************
 * Module:  com.crux.common.model.Filter
 * Author:  Denny Mahendra
 * Created: Mar 23, 2004 1:34:51 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.model;

import com.crux.base.BaseClass;
import com.crux.common.config.Config;

import java.io.Serializable;

public class Filter extends BaseClass implements Serializable {
   private int currentPage = 0;
   public int rowPerPage = 0;

   public String orderKey = null;
   public int orderDir = 1;
     public boolean enabled = false;

   public String afField;
   public String afMode;
   public String afValue;

   public final static transient String AFMODE_EXACT = "1";
   public final static transient String AFMODE_CONTAINS = "2";
   public final static transient String AFMODE_GREATER = "3";
   public final static transient String AFMODE_LESS = "4";

   public int getStartRow() {
      return currentPage*rowPerPage;
   }

   public int getEndRow() {
      return getStartRow() + rowPerPage;
   }

   public int getCurrentPage() {
      return currentPage;
   }

   public void setCurrentPage(int currentPage) {
      this.currentPage = currentPage;
   }

   public Filter activate() {
      rowPerPage = Config.ROW_PER_PAGE;
      return this;
   }
}
