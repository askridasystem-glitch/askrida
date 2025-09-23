/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 22, 2004
 * Time: 4:17:43 PM
 * To change this template use Options | File Templates.
 */
package com.crux.valueset.helper;
import com.crux.common.controller.Helper;
import com.crux.valueset.ejb.ValueSet;
import com.crux.valueset.ejb.ValueSetHome;
import com.crux.valueset.filter.ValueSetFilter;
import com.crux.valueset.model.ValueSetView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;

public class ValueSetHelper extends Helper{
    private ValueSet getRemoteValueSet()throws NamingException, CreateException, ClassNotFoundException, RemoteException{
        return((ValueSetHome) JNDIUtil.getInstance().lookup("ValueSetEJB",ValueSetHome.class.getName())).create();
    }

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

    }
    public void saveValueSets(HttpServletRequest rq)throws Exception{
            final DTOList vl = (DTOList)get(rq,"VS_LIST");

             if (vl.size()<=0)
                throw new Exception("There is no value on this value set!!!");
                getValueSetFromRequest(rq);

            getRemoteValueSet().save(vl);

    }
}
