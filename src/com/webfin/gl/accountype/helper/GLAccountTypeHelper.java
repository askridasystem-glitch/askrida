/***********************************************************************
 * Module:  com.webfin.gl.accountype.helper.GLAccountTypeHelper
 * Author:  Denny Mahendra
 * Created: Jul 18, 2005 5:37:55 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.accountype.helper;

import com.crux.common.controller.Helper;
import com.crux.util.JNDIUtil;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class GLAccountTypeHelper extends Helper {

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
       return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
             .create();
    }

    public void listAccountTypes(HttpServletRequest rq)  throws Exception {
        rq.setAttribute("LIST",getRemoteGeneralLedger().listAccountTypes());
    }

}
