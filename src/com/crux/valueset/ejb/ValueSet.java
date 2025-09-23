/**
 * Created by IntelliJ IDEA.
 * User: Andi Adhyaksa
 * Date: Nov 22, 2004
 * Time: 4:17:00 PM
 * To change this template use Options | File Templates.
 */
package com.crux.valueset.ejb;

import com.crux.util.DTOList;
import com.crux.valueset.filter.ValueSetFilter;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;



public interface ValueSet extends EJBObject{
    public DTOList ListValueSet(ValueSetFilter vf)throws Exception,RemoteException;
    public DTOList getValueSet(String stGroupName)throws Exception,RemoteException;
     public void save(DTOList l)throws Exception,RemoteException;
}
