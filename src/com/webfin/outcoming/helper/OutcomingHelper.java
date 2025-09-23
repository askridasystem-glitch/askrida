/***********************************************************************
 * Module:  com.webfin.entity.helper.EntityHelper
 * Author:  Denny Mahendra
 * Created: Dec 26, 2005 12:09:20 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.outcoming.helper;

import com.crux.common.controller.Helper;
import com.crux.common.filter.FOPResponseWrapper;
import com.crux.common.filter.LanguageFilter;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.outcoming.ejb.OutcomingManager;
import com.webfin.outcoming.ejb.OutcomingManagerHome;
import com.webfin.outcoming.filter.OutcomingFilter;
import com.webfin.outcoming.model.OutcomingView;
import com.webfin.outcoming.model.UploadBODView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

public class OutcomingHelper extends Helper {
   private OutcomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((OutcomingManagerHome) JNDIUtil.getInstance().lookup("OutcomingManagerEJB",OutcomingManagerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      OutcomingFilter f;

      f=(OutcomingFilter) get(rq,"FILTER");

      if (f==null) {
         f = new OutcomingFilter();
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
        
        final String outid = rq.getParameter("outid");
        
        final OutcomingView outbox = getRemoteEntityManager().loadEntity(outid);
        
        rq.setAttribute("OUTCOMING",outbox);
        rq.setAttribute("LOCK_PDF","N");
        
        final String urx = "/pages/outcoming/report/outcoming.fop";

        rq.getRequestDispatcher(urx).forward(rq,rp);
    }

   public void print2(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String outid = rq.getParameter("outid");

        final UploadBODView outbox = getRemoteEntityManager().loadEntity2(outid);

        String polid = outbox.getStPolicyID();

        rq.setAttribute("UPLOAD", outbox);
        rq.setAttribute("LOCK_PDF", "N");

        rq.setAttribute("SAVE_TO_FILE", "Y");
        rq.setAttribute("FILE_NAME", outbox.getStRefNo().replace("/", ""));

        String urx = null;
        urx = "/pages/outcoming/report/upload.fop";

        final DTOList file = ListUtil.getDTOListFromQuery(
                " select a.ref_no,a.out_id,a.pol_id,a.letter_no,a.file_physic as note,count(b.in_id) as receiver,a.status_flag "
                + "from uploadbod_letter a "
                + "inner join uploadbod_dist b on b.out_id = a.out_id "
                + "where a.out_id = ? "
                + "and b.approval_type = 'SETUJUI' "
                + "group by a.ref_no,a.out_id,a.pol_id,a.letter_no,a.file_physic,a.status_flag order by out_id ",
                new Object[]{outbox.getStOutID()},
                UploadBODView.class);

        for (int i = 0; i < file.size(); i++) {
            UploadBODView entity = (UploadBODView) file.get(i);

            if (Tools.isYes(entity.getStStatusFlag())) {
                continue;
            }

            if (Tools.isEqual(entity.getStNote(), entity.getStReceiver())) {

                FOPResponseWrapper res1 = new FOPResponseWrapper(rp, rq);
                final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);

                respx.setStripHeaders(true);
                String x = urx + ".jsp";

                RequestDispatcher view = rq.getRequestDispatcher(x);

                view.forward(rq, respx);

                respx.finishResponse();

                res1.save2(polid);

                updateStatus(outbox);
            }

        }

        rq.getRequestDispatcher(urx).forward(rq, rp);

        updatePrint(outbox);
    }

    public void updatePrint(UploadBODView entityView) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement P = S.setQuery("update uploadbod_letter set print_flag = 'Y' where out_id = ? ");

            P.setObject(1, entityView.getStOutID());

            int i = P.executeUpdate();

        } finally {
            S.release();
        }
    }

    public void updateStatus(UploadBODView entityView) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement P = S.setQuery("update uploadbod_letter set status_flag = 'Y' where out_id = ? ");

            P.setObject(1, entityView.getStOutID());

            int i = P.executeUpdate();

        } finally {
            S.release();
        }
    }

}
