/***********************************************************************
 * Module:  com.crux.util.JNDIUtil
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 10:56:15 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.ejb.EJBHome;
import java.util.Hashtable;

public class JNDIUtil {
   private static transient final JNDIUtil objJndiUtil = new JNDIUtil();
      private LogManager logManager = LogManager.getInstance(JNDIUtil.class);
      private Hashtable ejbHomes = new Hashtable();

      private Context objCtx = null;


      private JNDIUtil() {
      }

      public static JNDIUtil getInstance() {
         return objJndiUtil;
      }

      /**
       * lookup
       * @param stKeyName
       * @return Object
       * @throws javax.naming.NamingException
       * @throws java.lang.ClassNotFoundException
       * method for EJBHome/EJBLocalHome lookup
       * all Homes will be cached
       */

      public synchronized Object lookup(String stJndiName,
                                        String stHomeInterfaceName) throws NamingException, ClassNotFoundException {

         Object objHome = ejbHomes.get(stJndiName);

         if (objHome == null) {
            if (objCtx == null) {
               objCtx = new InitialContext();
            }
            Class homeClass = Class.forName(stHomeInterfaceName);
            Object objTemporary = null;
            objTemporary = objCtx.lookup(stJndiName);

            if(objTemporary instanceof EJBHome){
               objHome = PortableRemoteObject.narrow(objTemporary, homeClass);
               ejbHomes.put(stJndiName, objHome);

            } else {
               objHome = objTemporary;
               ejbHomes.put(stJndiName, objHome);
            }
         }
         return objHome;
      }

}
