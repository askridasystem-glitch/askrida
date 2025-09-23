/***********************************************************************
 * Module:  com.crux.configure.helper.ParameterHelper
 * Author:  Denny Mahendra
 * Created: Jun 15, 2004 11:59:59 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.configure.helper;

import com.crux.common.controller.Helper;
import com.crux.common.codedecode.Codec;
import com.crux.common.config.Config;
import com.crux.configure.ejb.ParamConfigHome;
import com.crux.configure.ejb.ParamConfig;
import com.crux.configure.model.ParameterView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class ParameterHelper extends Helper {
   private ParamConfig getRemoteParamConfig() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((ParamConfigHome) JNDIUtil.getInstance().lookup("ParamConfigEJB",ParamConfigHome.class.getName()))
            .create();
   }
   public void editParam(HttpServletRequest rq)  throws Exception {
      final String paramGroup = getString(rq.getParameter("GROUP"));
      final DTOList l = getRemoteParamConfig().getParams(paramGroup);
      put(rq,"PARAMS",l);
      rq.setAttribute("PARAMS", l);
      rq.setAttribute("GRP", paramGroup);
   }

   public void saveParams(HttpServletRequest rq)  throws Exception {
      final DTOList l = (DTOList) get(rq,"PARAMS");

      for (int i = 0; i < l.size(); i++) {
         ParameterView p = (ParameterView) l.get(i);

         if (Codec.ParameterType.STRING.equalsIgnoreCase(p.getStParamType())) {
            p.setStValueString(getString(rq.getParameter("vs"+i)));
         }
         else if (Codec.ParameterType.DATE.equalsIgnoreCase(p.getStParamType())) {
            p.setDtValueDate(getDate(rq.getParameter("vd"+i)));
         }
         else if (Codec.ParameterType.INTEGER.equalsIgnoreCase(p.getStParamType())) {
            p.setLgValueNumber(getLong(rq.getParameter("vi"+i)));
         }
         else if (Codec.ParameterType.BOOLEAN.equalsIgnoreCase(p.getStParamType())) {
            p.setLgValueNumber("on".equalsIgnoreCase(rq.getParameter("vb"+i)) ? new Long(1): new Long(0));
         }

         p.markUpdate();
      }

      getRemoteParamConfig().saveParams(l);
   }
}
