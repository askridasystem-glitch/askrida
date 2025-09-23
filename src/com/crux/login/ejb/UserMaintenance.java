/***********************************************************************
 * Module:  com.crux.login.ejb.UserMaintenance
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 8:36:37 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.ejb;

import com.crux.util.DTOList;
import com.crux.common.model.UserSession;
import com.crux.login.model.RoleView;
import com.crux.login.model.UserSessionView;
import com.crux.login.model.VendorView;
import com.crux.login.filter.UserMaintenanceFilter;
import com.crux.login.filter.RoleFilter;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface UserMaintenance extends EJBObject {
    public DTOList getRoleCombo() throws Exception, RemoteException;
    public DTOList getVendorCombo() throws Exception,RemoteException;
    public String createRoleID(UserSession us) throws Exception, RemoteException;
    public DTOList  getRoleList(RoleFilter roleFilter) throws Exception, RemoteException;
    public DTOList  getFunctionList() throws Exception, RemoteException;
    public DTOList getUserList(UserMaintenanceFilter g) throws Exception, RemoteException;
    public DTOList  getFuncRoleView(String stRoleID) throws Exception, RemoteException;
    public RoleView getRoleView(String stRoleID) throws Exception, RemoteException;
    public VendorView getVendorView(String stVendorID) throws Exception,RemoteException;
    public UserSessionView getUserView(String stUserID) throws Exception, RemoteException;
    public String deleteUser(UserSessionView usv) throws Exception,RemoteException;
    public String save(RoleView rl) throws Exception, RemoteException;
    public String saveUser(UserSessionView usv) throws Exception, RemoteException;
    public String savePassword(UserSessionView usv) throws Exception, RemoteException;

   boolean isPasswordHasUsed(UserSessionView usv) throws Exception, RemoteException;

   void save(RoleView role, DTOList rolefunctions) throws Exception, RemoteException;
}
