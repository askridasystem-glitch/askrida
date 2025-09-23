/***********************************************************************
 * Module:  com.webfin.ar.helper.ClausulesHelper
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:30:00 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.master.clausules.helper;

import com.crux.common.controller.Helper;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.model.InsuranceClausulesView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClausulesHelper extends Helper {
    
    private final static transient LogManager logger = LogManager.getInstance(ClausulesHelper.class);
    
    public void printClausules(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
        
        final String clausulesid = rq.getParameter("clausulesid");

        final InsuranceClausulesView clausules = (InsuranceClausulesView) DTOPool.getInstance().getDTO(InsuranceClausulesView.class, clausulesid);
      
        rq.setAttribute("CLAUSULES",clausules);
   
        String urx=null;

        urx = "/pages/clausules/clauses.fop";
        
        if (urx==null) throw new RuntimeException("Unable to find suitable print form");
        
        logger.logDebug("print: forwarding to ########## "+urx);
        
        rq.getRequestDispatcher(urx).forward(rq,rp);
    }

    public void printExcel(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
        
        final String receiptid = rq.getParameter("receiptid");
        final String settlement = rq.getParameter("settlement");
        
        //getRemoteAccountReceivable().getARReceiptForPrintingExcelPremi(receiptid,settlement);
        
    }

    public void printExcelKlaim(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final String settlement = rq.getParameter("settlement");

        //getRemoteAccountReceivable().getARReceiptForPrintingExcelKlaim(receiptid,settlement);

    }
    
}
