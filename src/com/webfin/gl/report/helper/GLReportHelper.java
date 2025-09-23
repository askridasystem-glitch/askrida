/***********************************************************************
 * Module:  com.webfin.gl.report.helper.GLReportHelper
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 12:55:12 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.report.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.LogManager;
import com.crux.util.LogUtil;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GLReportEngine;
import com.webfin.gl.ejb.GLReportParam;
import com.webfin.gl.model.GLReportView;
import com.webfin.gl.model.GLReportLineView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public class GLReportHelper extends Helper {

   private final static transient LogManager logger = LogManager.getInstance(GLReportHelper.class);

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void main(HttpServletRequest rq)  throws Exception {
      final DTOList l = getRemoteGeneralLedger().listReports();

      rq.setAttribute("LIST",l);
   }

   public void generate(HttpServletRequest rq)  throws Exception {
      final String glReportID = getString(rq.getParameter("glreportid"));

      /*GLReportView glrpt = getRemoteGeneralLedger().getReportDefinition(glReportID);

      glrpt = getRemoteGeneralLedger().fillReportData(glrpt);*/



      GLReportParam param = new GLReportParam();

      param.stReportID = glReportID;

      final GLReportEngine gle = new GLReportEngine(param);

      final GLReportView glrpt = gle.fillReportData();

      /*final DTOList lines = glrpt.getLines();

      final GLReportLineView line = (GLReportLineView)lines.get(0);

      logger.logDebug("generate: "+line.getColumns());

      final BigDecimal[] cols = line.getColumns();

      for (int i = 0; i < cols.length; i++) {
         BigDecimal col = cols[i];

         logger.logDebug("generate: "+col);
      }*/

      rq.setAttribute("RPT",glrpt);
   }
}
