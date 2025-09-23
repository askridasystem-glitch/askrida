/***********************************************************************
 * Module:  com.webfin.gl.journalmaster.helper.JournalMasterHelper
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 8:28:46 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.journalmaster.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.model.JournalMasterView;

import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class JournalMasterHelper extends Helper {
   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void listJournalMaster(HttpServletRequest rq)  throws Exception {
      rq.setAttribute("LIST",getRemoteGeneralLedger().listJournalMaster());
   }

   public void add(HttpServletRequest rq)  throws Exception {
      final JournalMasterView jm = new JournalMasterView();

      jm.markNew();

      put(rq,"JM", jm);

      rq.setAttribute("JM",jm);
   }

   public JournalMasterView view(HttpServletRequest rq)  throws Exception {
      final String jmID = getString(rq.getParameter("jmid"));

      final JournalMasterView jm = getRemoteGeneralLedger().getJournalMaster(jmID);

      put(rq,"JM", jm);

      rq.setAttribute("JM",jm);

      return jm;
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final JournalMasterView jm = view(rq);

      jm.markUpdate();

   }

   public void save(HttpServletRequest rq)  throws Exception {
      final JournalMasterView jm = (JournalMasterView)get(rq,"JM");

      jm.setStJournalCode(getString(rq.getParameter("journalcode")));
      jm.setStDescription(getString(rq.getParameter("desc")));
      jm.setStJournalType(getString(rq.getParameter("jt")));
      jm.setStJournalFreq(getString(rq.getParameter("jf")));
      jm.setDtEndDate(getDate(rq.getParameter("enddate")));
      jm.setDtLastPosted(getDate(rq.getParameter("lastposted")));

      getRemoteGeneralLedger().saveJournalMaster(jm);
   }

}
