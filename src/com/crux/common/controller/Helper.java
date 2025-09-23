/***********************************************************************
 * Module:  com.crux.common.controller.Helper
 * Author:  Denny Mahendra
 * Created: Mar 8, 2004 2:46:00 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.common.config.Config;
import com.crux.common.exception.LoginTimeOutException;
import com.crux.common.model.Filter;
import com.crux.common.model.UserSession;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.lov.LOVManager;
import com.webfin.WebFinLOVRegistry;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Enumeration;
import com.webfin.insurance.ejb.*;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class Helper extends ConvertUtil
{
    
    private final static transient LogManager logger = LogManager.getInstance(Helper.class);
    
    public final static transient boolean useFlowGuard = Parameter.readBoolean("SYS_FLOW_GUARD", true);
    
    public static void processFlowCard(HttpServletRequest rq)
    {
        
        if ("Y".equals(rq.getAttribute("FLOW_CARD_CHECKED"))) return;
        
        final Long fc2 = getLong(rq.getParameter("flowCard"));
        
        if (fc2!=null)
        {
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
            
            while (map.size()>50)
            { // keep 50 entries max per user
                map.remove(0);
            }
            
            //fc = new Long(fc.longValue()+1);
            //rq.getSession().setAttribute("FLOW_CARD", fc);
        }
    }
    
    public static ArrayList getFlowCardMap(HttpServletRequest rq)
    {
        ArrayList st = (ArrayList)rq.getSession().getAttribute("FLOW_CARD_MAP");
        
        if (st==null )
        {
            st= new ArrayList();
            rq.getSession().setAttribute("FLOW_CARD_MAP", st);
        }
        return st;
    }
    
    public static long getFlowCard(HttpServletRequest rq)
    {
        Long fc = (Long) rq.getSession().getAttribute("FLOW_CARD");
        
        if (fc==null)
        {
            fc = new Long(1);
        }
        else
        {
            fc = new Long(fc.longValue()+1);
        }
        
        rq.getSession().setAttribute("FLOW_CARD", fc);
        
        return fc.longValue();
    }
    
    public String getKeyword(String st)
    {
        final String s = getString(st);
        if (s!=null) return s.replace('*','%');
        return s;
    }
    
    public void frameGenerator(HttpServletRequest rq)  throws Exception
    {
    }
    
    public static UserSession getUserSession(HttpServletRequest rq)
    {
        UserSessionView uv = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
        
      /*if (uv == null) {
         uv = new UserSessionView();
         uv.setStUserID("admin");
         uv.setStBranchID("JKT");
         uv.setStWarehouseID("W0001");
         uv.setStOutletID("OKBPCARJKT00004");
      }*/
        
        //if (uv == null) throw new LoginTimeOutException("Login timeout");
        if (uv == null) return null;
        
        final Date td = new Date((System.currentTimeMillis()/100)*100);
        
        uv.setDtTransactionDate(td);
        
        return uv;
        
        //return (UserSession) ObjectCloner.deepCopy(uv);
    }
    
   /*public static boolean isDealer(HttpServletRequest rq) {
      UserSessionView uv = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
      return uv.isDealer();
   }
    
   public static boolean isRetail(HttpServletRequest rq) {
      UserSessionView uv = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
      return uv.isRetail();
   }*/
    
    public HashMap getSession(HttpServletRequest rq)
    {
        HashMap hm = (HashMap) rq.getSession().getAttribute(this.getClass().getName());
        
        if (hm==null)
        {
            hm = new HashMap();
            
            rq.getSession().setAttribute(this.getClass().getName(), hm);
        }
        
        return hm;
    }
    
    public static HelperAction forward(String stForwardID)
    {
        return HelperAction.forward(stForwardID);
    }
    
    public Object get(HttpServletRequest rq, String stKey)
    {
        return getMap(rq).get(stKey);
    }
    
    public void remove(HttpServletRequest rq, String stKey)
    {
        getMap(rq).remove(stKey);
    }
    
    private HashMap getMap(HttpServletRequest rq)
    {
        HashMap m;
        m = (HashMap)rq.getSession().getAttribute(this.getClass().getName());
        if (m==null)
        {
            m = new HashMap();
            rq.getSession().setAttribute(this.getClass().getName(),m);
        }
        
        return m;
    }
    
    public void put(HttpServletRequest rq, String stKey, Object o)
    {
        getMap(rq).put(stKey, o);
    }
    
    public static boolean hasResource(HttpServletRequest rq,String stResourceID)
    {
        final DTOList l = (DTOList)rq.getSession().getAttribute("USER_RESOURCES");
        
        if (stResourceID == null) throw new IllegalArgumentException("you must supply resource id");
        
        if (l==null) throw new LoginTimeOutException("Resource table is not loaded");
        
        for (int i = 0; i < l.size(); i++)
        {
            FunctionsView functionsView = (FunctionsView) l.get(i);
            
            if (stResourceID.equalsIgnoreCase(functionsView.getStResourceID()))
                return true;
        }
        
        return false;
    }
    
    public void updatePaging(HttpServletRequest rq, DTOList l)
    {
        final String gPg = getString(rq.getParameter("gPg"+l.getName()));
        if (gPg != null)
            l.setCurrentPage(Integer.valueOf(gPg).intValue());
        
        if (l.getRowPerPage()<1)
            l.setRowPerPage(Config.ROW_PER_PAGE);
        
        final String sort = getString(rq.getParameter("sPg"+"List"));
        if (sort!=null)
        {
            l.setOrderKey(sort);
            if (sort.equalsIgnoreCase(l.getOrderKey())) l.setOrderDir(l.getOrderDir()*-1);
            l.setCurrentPage(0);
        }
        else
        {
        }
        
        
        
      /*final Long order = getLong(rq.getParameter("dPg"+"List"));
      if (order!=null) l.setOrderDir(order.intValue());*/
        
        logger.logDebug("updatePaging: goto page = "+l.getCurrentPage());
    }
    
    public void updatePaging(HttpServletRequest rq, Filter l)
    {
        final String pg = getString(rq.getParameter("gPg"+"List"));
        
        if (l.rowPerPage<1)
            l.rowPerPage=Config.ROW_PER_PAGE;
        
        if (pg != null)
        {
            l.setCurrentPage(Integer.valueOf(pg).intValue());
        }
        else
        {
            l.setCurrentPage(0);
        }
        
        final String sort = getString(rq.getParameter("sPg"+"List"));
        if (sort!=null)
        {
            if (sort.equalsIgnoreCase(l.orderKey)) l.orderDir *= -1;
            l.orderKey = sort;
        }
        else
        {
            
        }
        
        //final Long order = getLong(rq.getParameter("dPg"+"List"));
        //if (order!=null) l.orderDir = order.intValue();
        
        logger.logDebug("updatePaging: goto page = "+l.getCurrentPage());
    }
    
    public void getPagingParameters()
    {
        
    }
    
    public static String getBranchID()
    {
        return Parameter.readString("SYNC_MACHINE_ID");
    }
    
    public static MultiPartWrapper getMultiPart(HttpServletRequest rq)
    {
        return (MultiPartWrapper) rq.getAttribute("MULTIPART");
    }
    
    public static boolean getChecked(String x)
    {
        return "on".equalsIgnoreCase(x);
    }
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException
    {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
    public void lovPop(HttpServletRequest rq)  throws Exception
    {
        rq.getParameter("cap");
        String lov = rq.getParameter("lov");
        String search = rq.getParameter("search");
        
        String street = rq.getParameter("street");
        String kabupaten2 = rq.getParameter("kabupaten");
        String grup = rq.getParameter("grup");
        
        if(lov.equalsIgnoreCase("LOV_PostalCode")||lov.equalsIgnoreCase("LOV_PostalCode_Maipark")){
            final LOV kabupaten = getRemoteInsurance().LOV_Kabupaten();
            rq.setAttribute("kabupaten",kabupaten);
        }

        HashMap lovParam2 = new HashMap();
        lovParam2.put("grup", grup);
      
        if(lov.toUpperCase().startsWith("LOV_ENTITY")){
            final LOV grupcompany = WebFinLOVRegistry.getInstance().LOV_CompType(lovParam2);
            rq.setAttribute("grupcompany", grupcompany);
        }

        if (kabupaten2!=null && street!=null)
        {
            HashMap lovParam = new HashMap();
            lovParam.put("street", street);
            lovParam.put("kabupaten2", kabupaten2);
            
            Enumeration nms = rq.getParameterNames();
            
            while (nms.hasMoreElements())
            {
                String nm = (String) nms.nextElement();
                String v = rq.getParameter(nm);
                
                if ("LVPvalue".equals(nm)) continue;
                
                if (nm.indexOf("LVP")==0)
                {
                    lovParam.put(nm.substring(3), v);
                }
            }
            
            LOV list = LOVManager.getInstance().getLOV(lov, lovParam);
            rq.setAttribute("LIST",list);
        }
        
        if (search!=null)
        {
            HashMap lovParam = new HashMap();
            lovParam.put("search", search);
            
            if(grup!=null && !grup.equalsIgnoreCase("")) lovParam.put("grup", grup);
            
            Enumeration nms = rq.getParameterNames();
            
            while (nms.hasMoreElements())
            {
                String nm = (String) nms.nextElement();
                String v = rq.getParameter(nm);
                
                if ("LVPvalue".equals(nm)) continue;
                
                if (nm.indexOf("LVP")==0)
                {
                    lovParam.put(nm.substring(3), v);
                }
            }
            
            LOV list = LOVManager.getInstance().getLOV(lov, lovParam);
            rq.setAttribute("LIST",list);
        }
 
    }
}
