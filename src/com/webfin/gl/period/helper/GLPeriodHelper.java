/***********************************************************************
 * Module:  com.webfin.gl.period.helper.GLPeriodHelper
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:30:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.period.helper;

import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.PeriodHeaderView;
import com.webfin.gl.model.PeriodDetailView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.common.controller.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class GLPeriodHelper extends Helper {

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final DTOList list = getRemoteGeneralLedger().listGLPeriods();
      rq.setAttribute("LIST",list);
   }

   public void add(HttpServletRequest rq)  throws Exception {
      final PeriodHeaderView ph = new PeriodHeaderView();

      ph.markNew();
      ph.setLgPeriodNum(new Long(12));

      final DTOList details = new DTOList();

      ph.setDetails(details);

      for (int i=0;i<13;i++) {
         final PeriodDetailView pd = new PeriodDetailView();
         pd.markNew();
         pd.setLgPeriodNo(new Long(i+1));
         details.add(pd);
      }

      populate(rq,ph);
   }

   private void populate(HttpServletRequest rq, PeriodHeaderView ph) {
      put(rq,"PH",ph);
      rq.setAttribute("PERIOD",ph);
   }

   public void openClose(HttpServletRequest rq)  throws Exception {
      final PeriodHeaderView ph = view(rq);

      //ph.markUpdate();
   }

   public void toggle(HttpServletRequest rq)  throws Exception {
      final PeriodHeaderView ph = (PeriodHeaderView)get(rq,"PH");

      final int row = getNum(rq.getParameter("rowid")).intValue();

      final DTOList details = ph.getDetails();

      final PeriodDetailView pdd = (PeriodDetailView)details.get(row);

      if (pdd.isOpen()) {
         getRemoteGeneralLedger().closePeriod(pdd);
      } else {
         getRemoteGeneralLedger().openPeriod(pdd);
      }

      view(rq);
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final PeriodHeaderView ph = view(rq);

      ph.markUpdate();

   }

   public PeriodHeaderView view(HttpServletRequest rq)  throws Exception {
      final Long periodid = getLong(rq.getParameter("periodid"));

      final PeriodHeaderView period = getRemoteGeneralLedger().getPeriod(periodid);

      populate(rq,period);

      return period;
   }

   public void save(HttpServletRequest rq)  throws Exception {
      final PeriodHeaderView ph = (PeriodHeaderView)get(rq,"PH");

      ph.setStFiscalYear(getString(rq.getParameter("fiscal")));
      ph.setLgPeriodNum(getLong(rq.getParameter("periodnum")));

      final DTOList details = ph.getDetails();

      for (int i = 0; i < details.size(); i++) {
         PeriodDetailView pd = (PeriodDetailView) details.get(i);

         pd.setDtStartDate(DateUtil.truncDate(getDate(rq.getParameter("pstart"+i))));

         if (i<ph.getLgPeriodNum().longValue()-1) {
            Date d = getDate(rq.getParameter("pstart"+(i+1)));

            if (d!=null) d=new Date(d.getTime()-1);

            pd.setDtEndDate(d);
         } else
            pd.setDtEndDate(getDate(rq.getParameter("pend"+i)));
         if (pd.isUnModified()) pd.markUpdate();
      }

      populate(rq,ph);

      getRemoteGeneralLedger().savePeriod(ph);
   }
}
