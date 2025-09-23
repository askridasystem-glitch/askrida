/***********************************************************************
 * Module:  com.webfin.ar.ejb.TrgInvoice
 * Author:  Denny Mahendra
 * Created: Feb 23, 2006 4:33:22 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.ejb;

import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.common.model.DTO;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARBalanceView;

public class TrgInvoice extends SQLUtil.Trigger {

   public TrgInvoice() {
      super(ARInvoiceView.class.getName());
   }

   public void onUpdate(String op, DTO dold, DTO dnew) throws Exception {

   }

   public void onUpdate2(String op, DTO dold, DTO dnew) throws Exception {
      final ARInvoiceView oldi = (ARInvoiceView)dold;
      final ARInvoiceView newi = (ARInvoiceView)dnew;

      final boolean hasOld = oldi!=null;
      final boolean isSameEntity = hasOld && Tools.isEqual(oldi.getStEntityID(), newi.getStEntityID());

      ARBalanceView oldBal=null;
      ARBalanceView newBal=null;

      if (hasOld) {
         oldBal = (ARBalanceView)DTOPool.getInstance().getDTO(ARBalanceView.class, oldi.getStEntityID());
         oldBal.markUpdate();
         oldBal.applyReverse(oldi);
      }

      if (isSameEntity) {
         newBal = oldBal;
      } else {
         newBal = (ARBalanceView)DTOPool.getInstance().getDTO(ARBalanceView.class, newi.getStEntityID());
      }

      if(newBal==null) {
         newBal = new ARBalanceView();
         newBal.markNew();

         newBal.setStEntityID(newi.getStEntityID());
      }

      if (newBal.isUnModified())
         newBal.markUpdate();

      newBal.apply(newi);

      final SQLUtil S = new SQLUtil();

      try {
         S.store(newBal);

         if (!isSameEntity)
            S.store(oldBal);
         
      } finally {
         S.release();
      }
   }
}
