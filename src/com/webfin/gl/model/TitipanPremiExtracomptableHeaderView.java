/***********************************************************************
 * Module:  com.webfin.gl.model.JournalHeaderView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 8:48:57 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.util.DTOList;

public class TitipanPremiExtracomptableHeaderView extends TitipanPremiExtracomptableView {
   private DTOList details;

   public DTOList getDetails() {
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }

   public void reCalculate() {

   }
}
