/***********************************************************************
 * Module:  com.webfin.gl.ejb.GLChartMaster
 * Author:  Denny Mahendra
 * Created: Dec 23, 2005 11:24:17 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.common.parameter.Parameter;
import com.webfin.gl.model.GLChartView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class GLChartMaster {
   public DTOList charts;
   public String mask;
   public char[] caMask;
   public HashMap mapOfAccountNo;

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public GLChartMaster() throws Exception {
      charts = getRemoteGeneralLedger().getCharts();
      mask = Parameter.readString("GL1_ACCT_MASK");
      caMask = mask.toCharArray();

      for (int i = 0; i < charts.size(); i++) {
         GLChartView ch = (GLChartView) charts.get(i);
         ch.setStAccountNo(generalizeAccountNo(ch.getStAccountNo()));
      }

      mapOfAccountNo = charts.getMapOf("accountno");

   }

   private String generalizeAccountNo(String ac) {
      if (ac==null) return null;

      final char[] caAc = ac.toCharArray();

      final char[] xAC = new char[caMask.length];

      for (int j = 0; j < caMask.length; j++) {
         char c = caMask[j];

         switch (c) {
            case 'A': c=caAc[j]; break;
            case ' ': break;
            case 'B': c='?'; break;
         }

         xAC[j]=c;
      }
      return new String(xAC);
   }

   public GLChartView getGLChartOfAccountNo(String ac) {
      ac = generalizeAccountNo(ac);

      return (GLChartView) mapOfAccountNo.get(ac);
   }

   public static void main(String [] args) throws Exception {
      new GLChartMaster();
   }
}
