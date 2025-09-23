/***********************************************************************
 * Module:  com.webfin.WebFin
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:07:57 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin;

import com.crux.common.controller.ControllerServlet;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.FunctionsView;
import com.crux.lov.CruxLOVRegistry;
import com.crux.lov.LOVManager;
import com.crux.util.*;
import com.crux.valueset.model.ValueSetView;
import com.crux.web.application.Application;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableEJB;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.gl.model.GLReportView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class WebFin extends Application {
   private final static transient LogManager logger = LogManager.getInstance(WebFin.class);

   public void initialize() throws Exception {
      AccountReceivableEJB.initialize();

      LOVManager.getInstance().register(WebFinLOVRegistry.class);
      LOVManager.getInstance().register(CruxLOVRegistry.class);

      try { 
         synchronizeARTRXToMenu();
      } catch (Exception e) {
         logger.log(e);
      }

      try {
         synchronizeARSetToMenu();
      } catch (Exception e) {
         logger.log(e);
      }

      try {
         synchronizeARSetReinsuranceToMenu();
      } catch (Exception e) {
         logger.log(e);
      }

	  /*
      try {
         synchronizeReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }*/
      	
      try {
         synchronizeFinReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }



      try {
         synchronizeFinReportToMenu2();
      } catch (Exception e) {
         logger.log(e);
      }

      try {
         synchronizeRKAPReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }
      
      
      try {
         //synchronizeClaimReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }
      
      try {
         //synchronizeMarketingReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }
      
      try {
         //synchronizeReinsuranceReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }
      
      try {
         //synchronizeAccountingReportToMenu();
      } catch (Exception e) {
         logger.log(e);
      }
      

      try {
         //synchronizeGLRptToMenu();
      } catch (Exception e) {
         logger.log(e);
      }

   }

   private void synchronizeReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and active_flag = 'Y' order by orderseq ",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("33.10."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }
   
   private void synchronizeFinReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and active_flag = 'Y' order by orderseq ",
                 new Object [] {"FIN_RPT"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("40.16."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/fin_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }

   private void synchronizeGLRptToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from gl_rpt where resource_id is not null",
                       GLReportView.class
               );

         for (int i = 0; i < l.size(); i++) {
            GLReportView rpt = (GLReportView) l.get(i);

            final FunctionsView func = new FunctionsView();

            func.setStFunctionID(rpt.getStResourceID());
            func.setStFunctionName(rpt.getStReportTitle());
            func.setStURL("/gl_rpt_1.pdf.rpt?glreportid="+rpt.getStReportID());

            func.markNew();

            S.store(func);
         }
      } finally {
         S.release();
      }

   }

   private void synchronizeARSetToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from ar_settlement order by order_id",
                       ARAPSettlementView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ARAPSettlementView ars = (ARAPSettlementView) l.get(i);

            final FunctionsView func = new FunctionsView();

            final String n = StringTools.leftPad(String.valueOf(i+1),'0',2);

            func.setStFunctionID("35.05.10."+n+".00.00");
            func.setStFunctionName(ars.getStDescription());
            func.setStURL("/"+ars.getStMenuID()+".crux?arsid="+ars.getStARSettlementID());

            func.markNew();

            S.store(func);
         }
      } finally {
         S.release();
      }

   }

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }

   private void synchronizeARTRXToMenu() throws Exception {
      getRemoteAccountReceivable().updateARTrxMenu();
   }
   
    private void synchronizeMarketingReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and division like 'marketing' and active_flag = 'Y' order by orderseq ",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("15.20."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }
   
   private void synchronizeFinanceReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and division like 'finance' and active_flag = 'Y' order by orderseq ",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("36.35."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }
   
   private void synchronizeClaimReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and division like 'claim' and active_flag = 'Y' order by orderseq ",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("30.30."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }
   
   private void synchronizeReinsuranceReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and division like 'reinsurance' and active_flag = 'Y' order by orderseq",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("25.30."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }
   
   private void synchronizeAccountingReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and division like 'accounting' and active_flag = 'Y' order by orderseq",
                 new Object [] {"PROD_PRINTING"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("40.16.12.00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/prod_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }


   private void synchronizeRKAPReportToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and active_flag = 'Y' order by orderseq ",
                 new Object [] {"PROD_RKAP"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("40.19."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/rkap_report.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }

   private void synchronizeFinReportToMenu2() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from s_valueset where vs_group=? and active_flag = 'Y' order by orderseq ",
                 new Object [] {"FIN_RPT"},
                       ValueSetView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ValueSetView vs = (ValueSetView) l.get(i);

            final FunctionsView func = new FunctionsView();

               func.setStFunctionID("46.30."+StringTools.leftPad(String.valueOf(i+1),'0',2)+".00.00.00");
               func.setStFunctionName(vs.getStVDesc());
               func.setStURL("/fin_report2.go.crux?rpt="+vs.getStVCode());

               func.markNew();

               S.store(func);
         }
      } finally {
         S.release();
      }
   }

   private void synchronizeARSetReinsuranceToMenu() throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         final DTOList l = ListUtil.getDTOListFromQuery(
                       "select * from ar_settlement where ar_settlement_id in (9,30,11,46,47,49) order by order_id",
                       ARAPSettlementView.class
               );

         for (int i = 0; i < l.size(); i++) {
            ARAPSettlementView ars = (ARAPSettlementView) l.get(i);

            final FunctionsView func = new FunctionsView();

            final String n = StringTools.leftPad(String.valueOf(i+1),'0',2);

            func.setStFunctionID("35.15.10."+n+".00.00");
            func.setStFunctionName(ars.getStDescription());
            func.setStURL("/"+ars.getStMenuID()+".crux?arsid="+ars.getStARSettlementID());

            func.markNew();

            S.store(func);
         }
      } finally {
         S.release();
      }

   }

}
