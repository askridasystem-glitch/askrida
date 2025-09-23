/***********************************************************************
 * Module:  com.webfin.ar.ejb.TrgReceiptLines
 * Author:  Denny Mahendra
 * Created: Feb 23, 2006 4:36:00 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.ejb;

import com.crux.common.model.DTO;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARReceiptLinesView;

public class TrgReceiptLines extends SQLUtil.Trigger {

   public TrgReceiptLines() {
      super(ARReceiptLinesView.class.getName());
   }

   public void onUpdate(String op, DTO dold, DTO dnew) throws Exception {
      final ARReceiptLinesView arlnew = (ARReceiptLinesView)dnew;
      DTOPool.getInstance().reset(arlnew.getInvoice());
      DTOPool.getInstance().reset(arlnew.getInvoiceDetail());
   }

   public void onUpdate2(String op, DTO dold, DTO dnew) throws Exception {
      final ARReceiptLinesView oldi = (ARReceiptLinesView) dold;
      final ARReceiptLinesView newi = (ARReceiptLinesView) dnew;

      final boolean oldExist = (oldi!=null);
      final boolean sameInvoice = oldExist && (Tools.isEqual(oldi.getStInvoiceID(), newi.getStInvoiceID()));

      ARInvoiceView oldInvoice = null;
      ARInvoiceView newInvoice = null;

      if (oldExist) {
         oldInvoice = (ARInvoiceView)DTOPool.getInstance().getDTO(ARInvoiceView.class, oldi.getStInvoiceID());
         oldInvoice.markUpdate();

         oldInvoice.setDbAmountSettled(BDUtil.sub(oldInvoice.getDbAmountSettled(), oldi.getDbAmount()));
      }

      if (sameInvoice)
         newInvoice = oldInvoice;
      else
         newInvoice = (ARInvoiceView)DTOPool.getInstance().getDTO(ARInvoiceView.class, newi.getStInvoiceID());

      newInvoice.markUpdate();

      newInvoice.setDbAmountSettled(BDUtil.sub(newInvoice.getDbAmountSettled(), oldi.getDbAmount()));

      final SQLUtil S = new SQLUtil();

      try {
         S.store(newInvoice);

         if (!sameInvoice) {
            S.store(oldInvoice);
         }
      } finally {
         S.release();
      }
   }
}
