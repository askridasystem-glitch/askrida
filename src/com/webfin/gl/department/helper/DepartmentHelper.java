/***********************************************************************
 * Module:  com.webfin.gl.department.helper.DepartmentHelper
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 10:59:32 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.department.helper;

import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.model.GLCostCenterView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.common.controller.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class DepartmentHelper extends Helper {

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final DTOList list = getRemoteGeneralLedger().listDepartments();

      rq.setAttribute("LIST",list);
   }

   public void add(HttpServletRequest rq)  throws Exception {
      final GLCostCenterView dept = new GLCostCenterView();

      dept.markNew();

      put(rq,"DEPT",dept);
      rq.setAttribute("DEPT",dept);
   }

   public GLCostCenterView view(HttpServletRequest rq)  throws Exception {
      final GLCostCenterView dept = getRemoteGeneralLedger().getDepartment(getString(rq.getParameter("deptid")));

      put(rq,"DEPT",dept);
      rq.setAttribute("DEPT",dept);

      return dept;
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final GLCostCenterView dept = view(rq);

      dept.markUpdate();
   }

   public void save(HttpServletRequest rq)  throws Exception {

      final GLCostCenterView dept = (GLCostCenterView)get(rq,"DEPT");

      dept.setStCostCenterCode(getString(rq.getParameter("deptcode")));
      dept.setStDescription(getString(rq.getParameter("desc")));

      getRemoteGeneralLedger().saveDepartment(dept);
   }
}
