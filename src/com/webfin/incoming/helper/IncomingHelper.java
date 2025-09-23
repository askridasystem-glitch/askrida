/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.incoming.helper;

import com.crux.common.controller.Helper;
import com.crux.common.filter.FOPResponseWrapper;
import com.crux.common.filter.LanguageFilter;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.webfin.incoming.filter.IncomingFilter;
import com.webfin.incoming.model.ApprovalBODView;
import com.webfin.incoming.model.IncomingView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

public class IncomingHelper extends Helper {
   private IncomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      IncomingFilter f;

      f=(IncomingFilter) get(rq,"FILTER");

      if (f==null) {
         f = new IncomingFilter();
         put(rq,"FILTER",f);
      }
      updatePaging(rq,f);

      final DTOList l = getRemoteEntityManager().listEntities(f);

      rq.setAttribute("LIST",l);
   }

   public void create(HttpServletRequest rq)  throws Exception {
   }

   public void edit(HttpServletRequest rq)  throws Exception {
   }

   public void view(HttpServletRequest rq)  throws Exception {
   }

   public void retrieve(HttpServletRequest rq)  throws Exception {
   }

   public void populate(HttpServletRequest rq)  throws Exception {
   }

   public void save(HttpServletRequest rq)  throws Exception {
   }
   
   public void print(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
        
        final String incomingid = rq.getParameter("inid");
        
        final IncomingView inbox = getRemoteEntityManager().loadEntity(incomingid);
        
        rq.setAttribute("INCOMING",inbox);
        rq.setAttribute("LOCK_PDF","N");
        
        final String urx = "/pages/incoming/report/incoming.fop";

        rq.getRequestDispatcher(urx).forward(rq,rp);
    }

   public void print2(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String incomingid = rq.getParameter("inid");

        final ApprovalBODView inbox = getRemoteEntityManager().loadEntity2(incomingid);

        String polid = inbox.getStPolicyID();

        rq.setAttribute("APPROVAL", inbox);

        rq.setAttribute("SAVE_TO_FILE", "Y");
        rq.setAttribute("FILE_NAME", inbox.getStRefNo().replace("/", ""));

        String urx = "/pages/incoming/report/approval.fop";

        final DTOList file = ListUtil.getDTOListFromQuery(
                " select a.ref_no,a.out_id,a.approval_type,a.pol_id,a.letter_no,count(a.in_id) as cc,"
                + "(select count(b.out_id) from uploadbod_dist b where b.out_id::text = a.out_id) as cc_id "
                + "from approvalbod_letter a "
                + "where a.out_id = ? "
                + "and a.approval_type is not null "
                + "group by a.ref_no,a.sender,a.subject,a.out_id,a.approval_type,a.pol_id,a.letter_no "
                + "order by a.out_id,a.approval_type desc limit 1 ",
                new Object[]{inbox.getStOutID()},
                ApprovalBODView.class);

        for (int i = 0; i < file.size(); i++) {
            ApprovalBODView entity = (ApprovalBODView) file.get(i);

            if (Tools.isEqual(entity.getStCC(), entity.getStCCID())) {
                if (entity.getStApprovalType().equalsIgnoreCase("SETUJUI")) {

                    FOPResponseWrapper res1 = new FOPResponseWrapper(rp, rq);
                    final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);

                    respx.setStripHeaders(true);
                    String x = urx + ".jsp";

                    RequestDispatcher view = rq.getRequestDispatcher(x);

                    view.forward(rq, respx);

                    respx.finishResponse();

                    res1.save2(polid);

                }
            }
        }

        rq.getRequestDispatcher(urx).forward(rq, rp);

        updatePrint(inbox);
    }

    public void updatePrint(ApprovalBODView entityView) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement P = S.setQuery("update approvalbod_letter set print_flag = 'Y' where in_id = ? ");

            P.setObject(1, entityView.getStInID());

            int i = P.executeUpdate();

        } finally {
            S.release();
        }
    }

}
