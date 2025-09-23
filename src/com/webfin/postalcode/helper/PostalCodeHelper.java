/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 22, 2004
 * Time: 4:17:43 PM
 * To change this template use Options | File Templates.
 */
package com.webfin.postalcode.helper;
import com.crux.common.controller.Helper;
import com.webfin.postalcode.ejb.PostalCode;
import com.webfin.postalcode.ejb.PostalCodeHome;
import com.webfin.postalcode.filter.PostalCodeFilter;
import com.webfin.postalcode.model.PostalCodeView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.IDFactory;
import com.crux.common.model.UserSession;
import com.crux.common.model.RecordAudit;
import com.crux.common.parameter.Parameter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.ejb.*;
import com.crux.util.LOV;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;

public class PostalCodeHelper extends Helper{
    private PostalCode getRemotePostalCode()throws NamingException, CreateException, ClassNotFoundException, RemoteException{
        return((PostalCodeHome) JNDIUtil.getInstance().lookup("PostalCodeEJB",PostalCodeHome.class.getName())).create();
    }
    
    protected UserSession userSession;
    
        /*
    public void listValueSetGo(HttpServletRequest rq)throws Exception{
       final ValueSetFilter vf = new ValueSetFilter();
       vf.orderDir=1;
       vf.orderKey="VS_GROUP";
       put(rq,"VS_FILTER",vf);
       updatePaging(rq,vf);
       final DTOList l = getRemoteValueSet().ListValueSet(vf);
         
       rq.setAttribute("VALUE_LIST",l);
    }
    public void listValueSet(HttpServletRequest rq)throws Exception{
        final ValueSetFilter vf = (ValueSetFilter)get(rq,"VS_FILTER");
        updatePaging(rq,vf);
       final DTOList l = getRemoteValueSet().ListValueSet(vf);
         
       rq.setAttribute("VALUE_LIST",l);
    }
    public void createValueSet(HttpServletRequest rq)throws Exception{
        final DTOList vl = new DTOList();
        put(rq,"VS_LIST",vl);
        put(rq,"ACTION","CREATE");
        rq.setAttribute("VS_LIST",vl);
        rq.setAttribute("ACTION","CREATE");
    }
         
    public void editValueSet(HttpServletRequest rq)throws Exception{
        final String vsid = getString(rq.getParameter("vsid"));
        final DTOList vl = getRemoteValueSet().getValueSet(vsid);
         
        put(rq,"VS_LIST",vl);
        put(rq,"ACTION","EDIT");
        rq.setAttribute("VS_LIST",vl);
        rq.setAttribute("ACTION","EDIT");
    }
    public void viewValueSet(HttpServletRequest rq)throws Exception{
        final String vsid = getString(rq.getParameter("vsid"));
        final DTOList vl = getRemoteValueSet().getValueSet(vsid);
         
        //put(rq,"VS_LIST",vl);
        put(rq,"ACTION","VIEW");
        rq.setAttribute("VS_LIST",vl);
        rq.setAttribute("ACTION","CREATE");
    }
         
    public void addValue(HttpServletRequest rq)throws Exception{
         
        final DTOList vl = (DTOList)get(rq,"VS_LIST");
         
        getValueSetFromRequest(rq);
        populateValue(rq);
    }
    public void delValue(HttpServletRequest rq)throws Exception{
        final DTOList vl = (DTOList) get(rq,"VS_LIST");
        final int inDelIdx = getLong(rq.getParameter("DelVal")).intValue();
         vl.delete(inDelIdx);
         populateValue(rq);
    }
    public void getValueSetFromRequest(HttpServletRequest rq) throws Exception{
        final DTOList vl = (DTOList)get(rq,"VS_LIST");
        final String groupname = getString(rq.getParameter("groupname"));
        final ValueSetView vstemp = new ValueSetView();
        for (int i=0;i<vl.size();i++){
            ValueSetView vs = (ValueSetView)vl.get(i);
            String codex = getString(rq.getParameter("code"+(i+1)));
            vs.setStVDesc(getString(rq.getParameter("description"+(i+1))));
            vs.setLgVOrder(getLong(rq.getParameter("order"+(i+1))));
            vstemp.setStVCode(codex);
            final int found = vl.find(vstemp);
         
            if(vs.isNew()){
                vs.setStVGroup(groupname);
                if((found>-1)&&(found!=i))
                   throw new Exception("Code can't be same in one group!!!");
         
                vs.setStVCode(codex);
         
            }else{
                vs.markUpdate();
         
            }
            vs.setUserSession(getUserSession(rq));
        }
         
        final String codeNew = getString(rq.getParameter("code0"));
         
        if(codeNew!=null){
            vstemp.setStVCode(codeNew);
            final int found = vl.find(vstemp);
             if((found>-1))
                    throw new Exception("Code can't be same in one group!!!");
         
            final ValueSetView vs = new ValueSetView();
            vs.markNew();
            vs.setStVGroup(groupname);
               vs.setStVCode(codeNew);
            vs.setStVDesc(getString(rq.getParameter("description0")));
            vs.setLgVOrder(getLong(rq.getParameter("order0")));
            vs.setUserSession(getUserSession(rq));
            vl.add(vs);
        }
         
    }
    public void populateValue(HttpServletRequest rq)throws Exception{
        final DTOList vl = (DTOList)get(rq,"VS_LIST");
         
        rq.setAttribute("VS_LIST",vl);
        rq.setAttribute("ACTION",(String)get(rq,"ACTION"));
         
    }*/
    
    public void createPostalCode(HttpServletRequest rq)throws Exception{
        
        PostalCodeView view = new PostalCodeView();
        
        view.markNew();
        
        rq.setAttribute("JH",view);
        
        final LOV kabupaten = getRemoteInsurance().LOV_Kabupaten();
        
        rq.setAttribute("kabupaten",kabupaten);
        
        populate(rq,view);
    }
    
    public void savePostalCode(HttpServletRequest rq)throws Exception{
        
        final PostalCodeView view = retrieve(rq);
        
        getRemotePostalCode().save(view);
        
    }
    
    private PostalCodeView retrieve(HttpServletRequest rq) throws Exception {
        final PostalCodeView jh = (PostalCodeView)get(rq,"JH");
        
        jh.setStRegionMapID(String.valueOf(IDFactory.createNumericID("POSTCODEID")));
        jh.setStPostalCode(getString(rq.getParameter("postcode")));
        jh.setStCityName(getString(rq.getParameter("cityname")));
        jh.setStRegionName(getString(rq.getParameter("regionname")));
        jh.setStRegionMapDesc(getString(rq.getParameter("regionname")));
        jh.setStBuildingDescription(getString(rq.getParameter("buildingdesc")));
        jh.setStReference1(getString(rq.getParameter("kabupaten")));
        jh.setStCreateWho(SessionManager.getInstance().getUserID());
        jh.setDtCreateDate(new java.util.Date());
        
        return jh;
    }
    
    private void populate(HttpServletRequest rq, PostalCodeView jh) throws Exception {
        if (jh==null) jh=(PostalCodeView) get(rq,"JH");
        put(rq,"JH",jh);
        
        rq.setAttribute("JH",jh);
        
    }
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
}
