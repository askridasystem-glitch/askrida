/***********************************************************************
 * Module:  com.webfin.insurance.reportds.InsurancePolicyReportDS
 * Author:  Denny Mahendra
 * Created: Feb 19, 2006 11:56:17 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.reportds;

import com.crux.common.model.DataSource;
import com.crux.util.*;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.ff.model.FlexFieldDetailView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;

public class InsurancePolicyReportDS extends DataSource {
   private ArrayList outputLines;
   private ArrayList outputFields;
   private int currentLine;
   private int currentPage;
   private HashMap curMap;

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }



   public void initialize() throws Exception {

      final String policyID = getParam("policyid");

      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(policyID);

      outputLines = new ArrayList();
      outputFields = new ArrayList();

      final LineBuffer lb = new LineBuffer();

      currentPage=1;

      /*lb.print(rpad("Policy No",50));

      outputLines.add(("Policy No",50)+" : "+policy.getStPolicyID());*/
      //outputLines.add("Policy Type : "+policy.getStPolicyTypeDesc());

      newLine();

      addFld("fld1",policy.getStPolicyTypeDesc()+" INSURANCE\n");

      newLine();

      addFld("fld1","POLICY NO. "+policy.getStPolicyNo()+"\n\n");

      newLine();

      addFld("fld2","Whereas the Insured named in the Schedule(s) hereto has made to the :");

      newLine();addFld("fld1","\nPT. ASURANSI BANGUN ASKRIDA\n");

      newLine();

      addFld("fld2","a written proposal by completing the Questionnaire(s) which together with any other statements made in writing by the Insured for the purpose of this policy is deemed to be incorporated herein.\n\n");

      newLine();

      addFld("fld2","Now this Policy of Insurance witnesseth that subject to the Insured having paid to the Insurers the premium mentioned in the Schedule(s) and subject to the terms, exclusions, provisions and conditions contained herein or endorsed hereon the Insurers will indemnify the Insured in the manner and to the extent hereinafter provided.\n\n");

      newPage();

      newLine();addFld("fld1","\nTHE Schedule.\n");

      newLine();addFld("fld2",lbx("Type",policy.getStPolicyTypeDesc()));
      newLine();addFld("fld2",lbx("The Insured",policy.getStCustomerName()));
      newLine();addFld("fld2",lbx("Address",policy.getStCustomerAddress()));
      newLine();addFld("fld2",lbx("Period of Insurance",policy.getStPeriodLength()));
      newLine();addFld("fld2",lbx("Inception Date",policy.getDtPeriodStart()));
      newLine();addFld("fld2",lbx("Expiry Date",policy.getDtPeriodEnd()));

      final DTOList clauses = policy.getClausules();

      final StringBuffer clausules = new StringBuffer();

      for (int j = 0; j < clauses.size(); j++) {
         InsurancePolicyClausulesView cls = (InsurancePolicyClausulesView) clauses.get(j);

         clausules.append("- "+cls.getStDescription()+"\n");
      }

      newLine();addFld("fld2",lbx("Extension Clauses",clausules.toString()));

      final DTOList objects = policy.getObjects();

      FlexFieldHeaderView objectMap = policy.getPolicyType().getObjectMap();
      final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

         newLine();addFld("fld2","\n\n"+lbx("Risk Description",io.getStObjectDescription()));

         //final StringBuffer rd = new StringBuffer();
         /*
         if (io instanceof InsurancePolicyVehicleView) {

            final InsurancePolicyVehicleView veh = (InsurancePolicyVehicleView) io;

            newLine();addFld("fld2",lbx("- Vehicle Type",veh.getStVehicleTypeDesc()));
            newLine();addFld("fld2",lbx("- Reg No",veh.getStPoliceRegNo()));
            newLine();addFld("fld2",lbx("- Year",veh.getLgYearProduction()));
            newLine();addFld("fld2",lbx("- Chassis No",veh.getStChassisNo()));
            newLine();addFld("fld2",lbx("- Engine No",veh.getStEngineNo()));
            newLine();addFld("fld2",lbx("- Seat Num",veh.getLgSeatNumber()));
         }*/

         if (objectMapDetails!=null)
            for (int j = 0; j < objectMapDetails.size(); j++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);

               //rd.append(lbx("- "+iomd.getStFieldDesc(), io.getProperty(iomd.getStFieldRef())));
               newLine();addFld("fld2",lbx("- "+iomd.getStFieldDesc(), io.getProperty(iomd.getStFieldRef())));
            }

         final DTOList suminsureds = io.getSuminsureds();

         final StringBuffer tsis = new StringBuffer();
         final StringBuffer tsix = new StringBuffer();

         for (int j = 0; j < suminsureds.size(); j++) {
            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

            if (j>0) tsis.append('\n');
            tsis.append(lbx("- "+tsi.getStInsuranceTSIDesc(), tsi.getDbInsuredAmount()));

            if (j>0) tsix.append('\n');

            if (tsi.getStDescription()!=null)
               tsix.append("- "+tsi.getStDescription());
         }

         if (tsix.length()>0) {
            newLine();addFld("fld2",lbx("Interest Insured",tsix));
         }

         newLine();addFld("fld2",lbx("Insured Amount",tsis));


         newLine();addFld("fld2",lbx("Deductibles","-"));

         final DTOList coverage = io.getCoverage();

         {
            final StringBuffer covers = new StringBuffer();

            for (int j = 0; j < coverage.size(); j++) {
               InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

               if (j>0) covers.append('\n');
               covers.append("- "+cover.getStInsuranceCoverDesc());
            }

            newLine();addFld("fld2",lbx("Extension Covers",covers.toString()));
         }

         {
            final StringBuffer covers = new StringBuffer();

            for (int j = 0; j < coverage.size(); j++) {
               InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

               if (j>0) covers.append('\n');
               covers.append(lbx("- "+cover.getStInsuranceCoverDesc(), cover.getDbRate()));
            }

            newLine();addFld("fld2",lbx("Rates",covers.toString()));
         }

      }

      newLine();
      newLine();

      final DTOList details = policy.getDetails();

      final StringBuffer szDetail = new StringBuffer();

      szDetail.append(lbx("Total Base Premi", policy.getDbPremiTotal()));

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

         szDetail.append('\n');
         szDetail.append(lbx(item.getInsItem().getStDescription(), item.getDbAmount()));
      }

      newLine();addFld("fld2",lbx("Premium Calculation",szDetail.toString()));

      currentLine = -1;
   }

   private String lbx(String s, Object sz) {

      final LineBuffer lb = new LineBuffer();

      lb.print(rpad(s,20),20);
      lb.print(" : ",4);

      if (sz instanceof BigDecimal) sz=JSPUtil.print((BigDecimal)sz,2);

      lb.print(JSPUtil.print(sz),200);

      return lb.toString();
   }

   private void newPage() {
      currentPage++;
   }

   private void addFld(String k, String s1) {
      curMap.put(k,s1);
   }

   private void newLine() {
      curMap = new HashMap();
      curMap.put("pno",new Integer(currentPage));
      outputFields.add(curMap);
   }

   private String rpad(String s, int i) {
      return StringTools.rightPad(s,' ',i);
   }

   public void release() throws Exception {
   }

   public boolean next() throws JRException {
      currentLine++;
      return (currentLine<outputFields.size());
   }

   public Object getFieldValue(JRField jrField) throws JRException {

      final String fn = jrField.getName();

      //if ("pno".equalsIgnoreCase(fn)) return String.valueOf(currentPage);

      if (true) {
         final Object v = ((HashMap) outputFields.get(currentLine)).get(fn);

         return v==null?"":v;
      }

      if ("fld2".equalsIgnoreCase(fn)) {
         //return outputLines.get(currentLine);
         //return
      }

      return "";
   }

   public static class LineBuffer {
      private ArrayList lines;
      private int x;

      public LineBuffer() {
         lines = new ArrayList();
         x = 0;
      }

      public void print(String p0, int wrapAt) {

         int y=0;

         char[] ln = getLine(y);

         int w=0;

         int curx=x;
         int right=0;

         for (int i=0;i<p0.length();i++) {
            if (curx<ln.length) {
               if (p0.charAt(i)=='\n') {
                  y++;
                  ln =getLine(y);
                  curx=x;
               } else {
                  ln[curx] = p0.charAt(i);
                  curx++;
               }
            }


            if (right<curx) right=curx;

            if (w<i) w=i;

            if (i<p0.length()-1)
               if ((curx-x)>=wrapAt) {
                  y++;
                  ln =getLine(y);
                  curx=x;
               }
         }

         x=right;
      }

      private char[] getLine(int ln) {
         while (lines.size()<(ln+1)) {
            final char[] lx = new char[300];

            for (int i = 0; i < lx.length; i++) {
               lx[i] = ' ';
            }

            lines.add(lx);

            if (lines.size()<(ln+1)); else break;

         }
         return (char []) lines.get(ln);
      }

      public String toString() {

         final StringBuffer sz = new StringBuffer();

         for (int i = 0; i < lines.size(); i++) {
            char[] c = (char[]) lines.get(i);

            if (i>0) sz.append('\n');

            sz.append(StringTools.rightTrim(new String(c)));

         }

         return sz.toString();
      }

      public static void main(String [] args ) {
         final LineBuffer b = new LineBuffer();

         b.print("Policy No : ",20);
         b.print("yayyy aslkdj lqel kjqle jqlwkej qlkj elqkwj elqkwje lqkwj e",20);

         System.out.println(b);
      }
   }
}
