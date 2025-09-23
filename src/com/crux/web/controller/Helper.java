/***********************************************************************
 * Module:  com.crux.web.controller.Helper
 * Author:  Denny Mahendra
 * Created: Mar 8, 2004 2:46:00 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.util.LogManager;
import com.crux.util.DateUtil;
import com.crux.util.DTOList;
import com.crux.util.validation.FieldValidator;
import com.crux.common.parameter.Parameter;
import com.crux.common.model.UserSession;
import com.crux.common.model.Filter;
import com.crux.common.exception.LoginTimeOutException;
import com.crux.common.config.Config;
import com.crux.login.model.UserSessionView;
import com.crux.login.model.FunctionsView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class Helper {

   private final static transient LogManager logger = LogManager.getInstance(Helper.class);

   public final static transient boolean useFlowGuard = Parameter.readBoolean("SYS_FLOW_GUARD", true);

   public static void processFlowCard(HttpServletRequest rq) {

      if ("Y".equals(rq.getAttribute("FLOW_CARD_CHECKED"))) return;

      final Long fc2 = getLong(rq.getParameter("flowCard"));

      if (fc2!=null) {
         //Long fc = (Long) rq.getSession().getAttribute("FLOW_CARD");

         //logger.logDebug("processFlowCard: fc="+fc+"; fc2="+fc2);

         /*if (fc.longValue()>fc2.longValue()) {
            throw new LoginTimeOutException("Refresh and Back buttons are illegal");
         }*/

         final ArrayList map = getFlowCardMap(rq);

         if (useFlowGuard)
            if (map.contains(fc2)) throw new LoginTimeOutException("Refresh and Back buttons are illegal");

         rq.setAttribute("FLOW_CARD_CHECKED","Y");

         map.add(fc2);

         while (map.size()>50) { // keep 50 entries max per user
            map.remove(0);
         }

         //fc = new Long(fc.longValue()+1);
         //rq.getSession().setAttribute("FLOW_CARD", fc);
      }
   }

   public static ArrayList getFlowCardMap(HttpServletRequest rq) {
       ArrayList st = (ArrayList)rq.getSession().getAttribute("FLOW_CARD_MAP");

      if (st==null ) {
         st= new ArrayList();
         rq.getSession().setAttribute("FLOW_CARD_MAP", st);
      }
      return st;
   }

   public static long getFlowCard(HttpServletRequest rq) {
      Long fc = (Long) rq.getSession().getAttribute("FLOW_CARD");

      if (fc==null) {
         fc = new Long(1);
      } else {
         fc = new Long(fc.longValue()+1);
      }

      rq.getSession().setAttribute("FLOW_CARD", fc);

      return fc.longValue();
   }

   public static String deBlank(String st) {
      if ("".equals(st)) return null;
      return st==null?null:st;
   }

   public String getKeyword(String st) {
      final String s = getString(st);
      if (s!=null) return s.replace('*','%');
      return s;
   }

   public void frameGenerator(HttpServletRequest rq)  throws Exception {
   }

   public static String getString(String st) {
      if ("".equals(st)) return null;
      return st==null?null:st;
   }

   public static String getString(String st, FieldValidator fv) {
      return getString(st , fv, 0);
   }

   public static String getString(String st, FieldValidator fv, int flags) {
      if ("".equals(st)) st = null;
      fv.validate(st, flags);
      return st;
   }

   public static Long getLong(String st) {
      st=deBlank(st);
      return (st==null)?null:new Long(st);
   }

   public static Long getLong(String st, FieldValidator fv) {
      return getLong(st, fv, 0);
   }

   public static Long getLong(String st, FieldValidator fv, int flags) {
      st=deBlank(st);
      Long x = (st==null)?null:new Long(st);
      fv.validate(x, flags);
      return x;
   }

   public static String removeComma(String s) {
      if (s == null) return null;

      if (s.indexOf(',') >= 0) {
         final StringBuffer sz = new StringBuffer();

         char c;

         for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (c != ',') sz.append(c);
         }

         return sz.toString();
      }

      return s;
   }

   public long getlong(String st) {
      st=deBlank(st);
      if (st==null) return 0; else return Long.parseLong(removeComma(st));
   }

   public static Date getDate(String st) throws Exception {
      st=deBlank(st);
      if (st==null) return null; else return DateUtil.getDate(st);
   }

   public BigDecimal getNum(String st) {
      try {
         st=deBlank(st);
         return (st==null)?null:new BigDecimal(removeComma(st));
      }
      catch (RuntimeException e) {
         logger.logDebug("getNum: error processing number : ["+st+"]");
         throw e;
      }
      catch (Error e) {
         logger.logDebug("getNum: error processing number : ["+st+"]");
         throw e;
      }
   }

   public static UserSession getUserSession(HttpServletRequest rq) {
      UserSessionView uv = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");

      /*if (uv == null) {
         uv = new UserSessionView();
         uv.setStUserID("admin");
         uv.setStBranchID("JKT");
         uv.setStWarehouseID("W0001");
         uv.setStOutletID("OKBPCARJKT00004");
      }*/

      if (uv == null) throw new LoginTimeOutException("Login timeout");

      uv.setDtTransactionDate(new Date());

      return uv;

      //return (UserSession) ObjectCloner.deepCopy(uv);
   }

   public HashMap getSession(HttpServletRequest rq) {
      HashMap hm = (HashMap) rq.getSession().getAttribute(this.getClass().getName());

      if (hm==null) {
         hm = new HashMap();

         rq.getSession().setAttribute(this.getClass().getName(), hm);
      }

      return hm;
   }

   public static HelperAction forward(String stForwardID) {
      return HelperAction.forward(stForwardID);
   }

   public Object get(HttpServletRequest rq, String stKey) {
      return getMap(rq).get(stKey);
   }

   public void remove(HttpServletRequest rq, String stKey) {
      getMap(rq).remove(stKey);
   }

   private HashMap getMap(HttpServletRequest rq) {
      HashMap m;
      m = (HashMap)rq.getSession().getAttribute(this.getClass().getName());
      if (m==null) {
         m = new HashMap();
         rq.getSession().setAttribute(this.getClass().getName(),m);
      }

      return m;
   }

   public void put(HttpServletRequest rq, String stKey, Object o) {
      getMap(rq).put(stKey, o);
   }

   public static boolean hasResource(HttpServletRequest rq,String stResourceID) {
      final DTOList l = (DTOList)rq.getSession().getAttribute("USER_RESOURCES");

      if (stResourceID == null) throw new IllegalArgumentException("you must supply resource id");

      if (l==null) throw new LoginTimeOutException("Resource table is not loaded");

      for (int i = 0; i < l.size(); i++) {
         FunctionsView functionsView = (FunctionsView) l.get(i);

         if (stResourceID.equalsIgnoreCase(functionsView.getStResourceID()))
            return true;
      }

      return false;
   }

   public void updatePaging(HttpServletRequest rq, DTOList l) {
      final String gPg = getString(rq.getParameter("gPg"+l.getName()));

      try {
         if (gPg != null)
            l.setCurrentPage(Integer.valueOf(gPg).intValue());
      }
      catch (NumberFormatException e) {
         l.setCurrentPage(0);
      }

      if (l.getRowPerPage()<1)
         l.setRowPerPage(Config.ROW_PER_PAGE);

      final String sort = getString(rq.getParameter("sPg"+"List"));
      if (sort!=null) {
         l.setOrderKey(sort);
         if (sort.equalsIgnoreCase(l.getOrderKey())) l.setOrderDir(l.getOrderDir()*-1);
         l.setCurrentPage(0);
      } else {
      }



      /*final Long order = getLong(rq.getParameter("dPg"+"List"));
      if (order!=null) l.setOrderDir(order.intValue());*/

      logger.logDebug("updatePaging: goto page = "+l.getCurrentPage());
   }

   public void updatePaging(HttpServletRequest rq, Filter l) {
      final String pX = getString(rq.getParameter("ePg"+"List"));

      if (l.rowPerPage<1)
         l.rowPerPage=Config.ROW_PER_PAGE;

      l.setCurrentPage(0);

      if (!"y".equalsIgnoreCase(pX)) {
         return; // not a paging event
      }

      final String pg = getString(rq.getParameter("gPg"+"List"));



      try {
         if (pg != null) {
            l.setCurrentPage(Integer.valueOf(pg).intValue());
         }
      }
      catch (NumberFormatException e) {
      }

      final String sort = getString(rq.getParameter("sPg"+"List"));
      if (sort!=null) {
         if (sort.equalsIgnoreCase(l.orderKey)) l.orderDir *= -1;
         l.orderKey = sort;
      } else {

      }

      l.afField = getString(rq.getParameter("affield"+"List"));
      l.afMode = getString(rq.getParameter("afmode"+"List"));
      l.afValue = getString(rq.getParameter("afvalue"+"List"));

      //final Long order = getLong(rq.getParameter("dPg"+"List"));
      //if (order!=null) l.orderDir = order.intValue();

      logger.logDebug("updatePaging: goto page = "+l.getCurrentPage());
   }

   public void getPagingParameters() {

   }

   public static String getBranchID() {
      return Parameter.readString("SYNC_MACHINE_ID");
   }
}
