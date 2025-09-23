/**
 * Created by IntelliJ IDEA.
 * User: Andi Adhyaksa
 * Date: Nov 22, 2004
 * Time: 4:17:00 PM
 * To change this template use Options | File Templates.
 */
package com.webfin.postalcode.ejb;

import com.crux.util.DTOList;
import com.webfin.postalcode.filter.PostalCodeFilter;
import com.webfin.postalcode.model.PostalCodeView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;



public interface PostalCode extends EJBObject{
    //public DTOList ListValueSet(ValueSetFilter vf)throws Exception,RemoteException;
    //public DTOList getValueSet(String stGroupName)throws Exception,RemoteException;
     public void save(PostalCodeView l)throws Exception,RemoteException;
}
