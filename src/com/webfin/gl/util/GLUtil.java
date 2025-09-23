/***********************************************************************
 * Module:  com.webfin.gl.util.GLUtil
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 10:55:23 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.util;

import com.webfin.gl.ejb.*;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.GLChartView;
import com.webfin.gl.model.JournalView;
import com.crux.util.*;
import com.crux.util.stringutil.StringUtil;
import com.crux.common.parameter.Parameter;
import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.webfin.gl.model.TitipanPremiView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Iterator;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public class GLUtil {
   private final static transient LogManager logger = LogManager.getInstance(GLUtil.class);

   private static String mask = Parameter.readString("GL1_ACCT_MASK");

   public static int getMaskPosition(char code) {
      return mask.indexOf(code);
   }

   public static int getMaskLength(char code) {
      int pos = mask.indexOf(code);
      int last = mask.lastIndexOf(code);

      return last-pos+1;
   }


   public static String applyCode(String stGLAccountCode, char c, String entityGLCode) {

      if (entityGLCode==null) return stGLAccountCode;

      if (stGLAccountCode==null) return null;

      if (stGLAccountCode.indexOf(c)<0) return stGLAccountCode;

      stGLAccountCode = stGLAccountCode.split("[\\|]")[0];

      //if (entityGLCode==null) return stGLAccountCode;

      final char[] r = stGLAccountCode.toCharArray();

      int p=0;

      for (int i = 0; i < r.length; i++) {
         if (r[i]==c) p++;
      }

      while (entityGLCode.length()<p) entityGLCode='0'+entityGLCode;

      int j=0;

      for (int i = 0; i < r.length; i++) {
         char c1 = r[i];

         if (c1==c) {
            if (entityGLCode==null || j>=entityGLCode.length()) c1='?'; else c1=entityGLCode.charAt(j);
            j++;
         }

         r[i]=c1;
      }

      return new String(r);
   }

   public static BigDecimal getBalance(DTOList l) {
 
      BigDecimal tot=BDUtil.zero;

      for (int i = 0; i < l.size(); i++) {
         JournalView j = (JournalView) l.get(i);
         tot = BDUtil.add(tot,j.getDbCredit()); 
        
         tot = BDUtil.sub(tot,j.getDbDebit());  
         
      }

      return tot;
   }

   public static BigDecimal getBalanceTest(DTOList l) {

      BigDecimal tot=BDUtil.zero;

      BigDecimal totPerPolicyDebit = BDUtil.zero;
      BigDecimal totPerPolicyCredit = BDUtil.zero;

      for (int i = 0; i < l.size(); i++) {
         JournalView j = (JournalView) l.get(i);

         JournalView j2 = (JournalView) l.get(i+1);

         if(j.getStPolicyNo().equalsIgnoreCase(j2.getStPolicyNo())){
             totPerPolicyDebit = BDUtil.add(totPerPolicyDebit,j.getDbDebit());
             totPerPolicyCredit = BDUtil.add(totPerPolicyCredit,j.getDbCredit());
         }

         if(!j.getStPolicyNo().equalsIgnoreCase(j2.getStPolicyNo())){
             if(!BDUtil.isEqual(totPerPolicyDebit, totPerPolicyCredit, 0))
                 throw new RuntimeException("Selisih di polis "+ j.getStPolicyNo()+ " debit : "+totPerPolicyDebit+" credit : "+ totPerPolicyCredit);
         }

      }

      return tot;
   }
   
    public static BigDecimal getBalanceTitipanPremi(DTOList l) {
 
      BigDecimal tot=BDUtil.zero;

      for (int i = 0; i < l.size(); i++) {
         TitipanPremiView j = (TitipanPremiView) l.get(i);
         tot = BDUtil.add(tot,j.getDbCredit()); 
        
         tot = BDUtil.sub(tot,j.getDbDebit());  
         
      }

      return tot;
   }


   public static AccountView getAccountByCode(String accountCode) throws Exception {

      AccountView ac = (AccountView) ListUtil.getDTOListFromQuery(
              "   select " +
              "      * " +
              "   from " +
              "      gl_accounts " +
              "   where accountno=? and acctype is null",
              new Object [] {accountCode},
              AccountView.class).getDTO();

      return ac;
   }

   public static void fillBalances(DTOList list) throws Exception {

      JournalView fj = (JournalView) list.get(0);

      String plist = getPeriodList(0, fj.getLgPeriodNo().longValue(), "bal", "+");

      DTOList l = ListUtil.getDTOListFromQuery(
                    "select ("+plist+") as bal from gl_acct_bal2 where account_id=? and period_year=?",
              new Object [] {fj.getLgAccountID(), fj.getLgFiscalYear()},
                    HashDTO.class
            );

      DTOList l2 = ListUtil.getDTOListFromQuery(
                    "select sum(credit-debit) as cpbal from gl_je_detail where accountid=? and fiscal_year=? and period_no=? and applydate<=? and trx_id<>? ",
              new Object [] {fj.getLgAccountID(), fj.getLgFiscalYear(), fj.getLgPeriodNo(), fj.getDtApplyDate(), fj.getStTransactionID()},
                    HashDTO.class
            );

      HashDTO b = (HashDTO) l.getDTO();
      HashDTO b2 = (HashDTO) l2.getDTO();

      BigDecimal bal = b.getFieldValueByFieldNameBD("bal");
      BigDecimal balx = b2.getFieldValueByFieldNameBD("cpbal");

      bal=BDUtil.add(bal,balx);
      bal=BDUtil.add(bal,fj.getDbBalanceAmount());

      fj.setDbBalance(bal);

      for (int i = 1; i < list.size(); i++) {
         JournalView j1 = (JournalView) list.get(i);
         JournalView j0 = (JournalView) list.get(i-1);

         BigDecimal nb = BDUtil.add(j0.getDbBalance(), j1.getDbBalanceAmount());

         j1.setDbBalance(nb);
      }
   }

   public static String getPeriodList(long periodFrom, long periodTo, String fld, String opr) {

      StringBuffer sz = new StringBuffer();

      for (long i=periodFrom;i<=periodTo;i++) {

         if (i>periodFrom) sz.append(opr);
         sz.append("coalesce(").append(fld).append(String.valueOf(i)).append(",0)");
      }

      return sz.toString();
   }

   public static class GLAccountCache extends HashMap {
      private final static transient LogManager logger = LogManager.getInstance(GLUtil.GLAccountCache.class);
      public String errMsg;

      private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
         return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
               .create();
      }
      
      public AccountView getAccountByAccountNoUsingLike(String stAccountNo) throws Exception
    {
          
          //12220GGGGG00 BB
          //012345678901234
          //12210YYYYY00
          //12221GGGGG00
          /*
          logger.logDebug("++++++++++++++++++++ SEARCH AKUN BANK +++++++++++++++++++++++++");
          logger.logDebug("++++++++++++++++++++ PARAM : "+ stAccountNo);
          logger.logDebug("++++++++++++++++++++ HEADER : "+ stAccountNo.substring(0,3));
          logger.logDebug("++++++++++++++++++++ GL CODE : "+ stAccountNo.substring(5));
          logger.logDebug("++++++++++++++++++++ END AKUN BANK +++++++++++++++++++++++++");
          */
         
        return (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where coalesce(deleted,'') <> 'Y' and accountno like ? order by account_id asc limit 1",
                new Object [] {stAccountNo.substring(0,3) + "%"+ stAccountNo.substring(5)+"%"},
                AccountView.class).getDTO();
    }

      public AccountView getAccountByAccountNo(String stAccountNo) throws Exception {
         AccountView ac = (AccountView) super.get(stAccountNo);
         if (ac==null) {
            ac=getRemoteGeneralLedger().getAccountByAccountNo(stAccountNo);
            put(stAccountNo,ac);
         }
         return ac;
      }

      public AccountView getAccountByAccountID(String stAccountID) {
         try {
            AccountView ac = (AccountView) super.get(stAccountID);
            if (ac==null) {
               ac=getRemoteGeneralLedger().getAccount(stAccountID);
               put(stAccountID,ac);
            }
            return ac;
         } catch (Exception e) {
            logger.logError("getAccountByAccountID: "+e);
            return null;
         }
      }

      public String getAccountIDFromAccountNo(String acno) throws Exception {
         final AccountView ac = getAccountByAccountNo(acno);
         return ac.getLgAccountID().toString();
      }

      public AccountView autoCreateAccount2(String stGLAccountCode, String desc) {
         try {

            if (!GLUtil.validateAccountCode(stGLAccountCode)) {
               errMsg = "autoCreateAccount2 failed : code:"+stGLAccountCode+" desc:"+desc;
               return null;
            }

            AccountView acc = getAccountByAccountNo(stGLAccountCode);

            if (acc==null) {

               if (desc!=null)
                  if (!GLUtil.validateAccountDesc(desc)) {
                     errMsg = "autoCreateAccount2 failed : code:"+stGLAccountCode+" desc:"+desc;
                     return null;
                  }

               logger.logDebug("autoCreateAccount: auto creating "+stGLAccountCode+" / "+desc);

               final GLChartView chart = Chart.getInstance().findChart(stGLAccountCode);

               final String chartSeg = Chart.getInstance().getChartSegmentOnly(stGLAccountCode);

               final boolean isMatching = Tools.isEqual(chart.getStAccountNo(),chartSeg);

               if (desc==null) desc = chart.getStDescription();

               final AccountView ac = new AccountView();

               ac.setStAccountNo(stGLAccountCode);
               ac.setStAccountType(chart.getStAccountType());
               ac.setStDescription(desc);
               ac.setStEnabled("Y");
               
               ac.markNew();

               getRemoteGeneralLedger().saveAccount(ac);

               acc = getAccountByAccountNo(stGLAccountCode);
            }

            if (acc==null) throw new RuntimeException("Unable to establish account for "+stGLAccountCode+" (unknown reason)");

            return acc;
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
      
      public AccountView autoCreateAccountUsingLike(String stGLAccountCode, String desc) {
         try {

            if (!GLUtil.validateAccountCode(stGLAccountCode)) {
               errMsg = "autoCreateAccount2 failed : code:"+stGLAccountCode+" desc:"+desc;
               return null;
            }

            AccountView acc = getAccountByAccountNoUsingLike(stGLAccountCode);

            if (acc==null) {

               // logger.logDebug("########################## AUTO CREATE AKUN #################################");

               if (desc!=null)
                  if (!GLUtil.validateAccountDesc(desc)) {
                     errMsg = "autoCreateAccount2 failed : code:"+stGLAccountCode+" desc:"+desc;
                     return null;
                  }

               logger.logDebug("autoCreateAccount: auto creating "+stGLAccountCode+" / "+desc);

               final GLChartView chart = Chart.getInstance().findChart(stGLAccountCode);

               final String chartSeg = Chart.getInstance().getChartSegmentOnly(stGLAccountCode);

               final boolean isMatching = Tools.isEqual(chart.getStAccountNo(),chartSeg);

               if (desc==null) desc = chart.getStDescription();

               final AccountView ac = new AccountView();

               ac.setStAccountNo(stGLAccountCode);
               ac.setStAccountType(chart.getStAccountType());
               ac.setStDescription(desc);
               ac.setStEnabled("Y");
               
               ac.markNew();

               getRemoteGeneralLedger().saveAccount(ac);

               acc = getAccountByAccountNoUsingLike(stGLAccountCode);
            }

            if (acc==null) throw new RuntimeException("Unable to establish account for "+stGLAccountCode+" (unknown reason)");

            return acc;
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
      
   }

   private static boolean validateAccountDesc(String desc) {

      if (desc==null) return true;

      for (int i=0;i<desc.length();i++) {
         final char c = desc.charAt(i);

         if (c=='%') return false;
      }

      return true;
   }

   public static boolean validateAccountCode(String stGLAccountCode) {

      if (stGLAccountCode==null) return false;

      for (int i=0;i<stGLAccountCode.length();i++) {
         final char c = stGLAccountCode.charAt(i);

         if ((c>='0') && (c<='9')) continue;
         if (c==' ') continue;

         logger.logError("validateAccountCode: Invalid Account Code : "+stGLAccountCode);

         //throw new RuntimeException("Invalid Account Code : "+stGLAccountCode);
         return false;
      }

      return true;
   }

   public static class Chart {
      private static Chart staticinstance;
      public char[] caMask;
      private int chartSt;
      private int chartEnd;
      public DTOList charts;

      public static Chart getInstance() {
         if (staticinstance == null) staticinstance = new Chart();
         return staticinstance;
      }

      private Chart() {
         caMask = mask.toCharArray();
         chartSt = mask.indexOf('A');
         chartEnd = mask.lastIndexOf('A');

         loadCharts();
      }

      private void loadCharts() {
         try {
            charts = ListUtil.getDTOListFromQuery(
                             "select * from gl_chart order by accountno",
                             GLChartView.class
                     );
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      public String getChartSegmentOnly(String acc) {
         String ac = getChartCodeOnly(acc);
        
         ac = ac.substring(chartSt, chartEnd+1);

         {
            final char[] ca = ac.toCharArray();

            for (int i = 0; i < ca.length; i++) {
               char c = ca[i];
               if (c<'0' ||c > '9') c='0';
               ca[i]=c;
            }

            ac = new String(ca);
         }

         return ac;
      }

      public String getChartCodeOnly(String acc) {
         if (acc==null) return null;

         final char[] acx = new char [caMask.length];

         for (int i = 0; i < caMask.length; i++) {
            char c = caMask[i];

            if (c=='A') {
               if (i>=acc.length())
                  c='0';
               else
                  c=acc.charAt(i);
            }

            acx[i]=c;
         }

         return new String(acx);
      };

      public String getChartDescription(String stGLAccountCode) {

         if (stGLAccountCode==null) return null;

         final GLChartView chart = findChart(stGLAccountCode);

         if (chart==null) throw new RuntimeException("Unable to find suitable GL chart for account : "+stGLAccountCode);

         return chart.getStDescription();
      }

      public GLChartView findChart(String stGLAccountCode) {

         String ac = getChartSegmentOnly(stGLAccountCode);
        
         final HashMap accountMap = charts.getMapOf("accountno");

         GLChartView chart = (GLChartView)accountMap.get(ac);

         if (chart==null) {
            for (int i = 0; i < charts.size(); i++) {
               GLChartView ch = (GLChartView) charts.get(i);

               final int n = Tools.compare(ch.getStAccountNo(), ac);

               if (n>=0) { // greater or equal ?

                  if (n>0) i--; // if greater then reverse one step

                  if(i>=0) chart = (GLChartView) charts.get(i);

                  break;
               }
            }

            if (chart==null)
               if (charts.size()>0) // if stuck to the end of the loop then use the last account
                  chart = (GLChartView) charts.get(charts.size()-1);
         }

         return chart;
      }
   }

   public static class Applicator {
      private HashMap codeMap = new HashMap();
      private HashMap descMap = new HashMap();
      private GLAccountCache cache;
      private String previewDesc;
      public String errMsg;
      private String glDesc;

      public Applicator() {
         cache = new GLAccountCache();
      }

      public void setCode(char c, String entityGLCode) {
         codeMap.put(new Character(c),entityGLCode);
      }

      public void setDesc(String s, String stShortName) {
         descMap.put('%'+s+'%', stShortName);
      }

      public String getAccountID(String stGLAccountCode) {

         if (stGLAccountCode==null) return null;

         final String[] sc = stGLAccountCode.split("[\\|]");

         stGLAccountCode = sc[0];
         String stGLDescCode = sc.length>1?sc[1]:null;

         stGLAccountCode = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         final boolean enableAutoSuffix = stGLDescCode==null;

         if (stGLDescCode == null) {
            stGLDescCode = Chart.getInstance().getChartDescription(stGLAccountCode);
         }

         final Iterator codeit = codeMap.keySet().iterator();

         while (codeit.hasNext()) {
            Character c = (Character) codeit.next();

            if (enableAutoSuffix)
               if (stGLAccountCode.indexOf(c.charValue())>=0) {
                  final String substCode = "%"+c+"%";

                  if (descMap.containsKey(substCode)) {
                     stGLDescCode+=" - "+substCode;
                  }
               }

            stGLAccountCode = applyCode(stGLAccountCode, c.charValue(), (String) codeMap.get(c));
         }

         final Iterator descit = descMap.keySet().iterator();

         while (descit.hasNext()) {
            String k = (String) descit.next();

            stGLDescCode = StringTools.replace(stGLDescCode, k, (String) descMap.get(k));

            stGLDescCode = LanguageManager.getInstance().translate(stGLDescCode);
            
            glDesc = stGLDescCode;
         }



         final AccountView ac = cache.autoCreateAccount2(stGLAccountCode, stGLDescCode);

         errMsg = cache.errMsg;

         previewDesc = stGLAccountCode+" / "+stGLDescCode;

         return ac==null?null:ac.getStAccountID();
      }
      
      public String getAccountIDUsingLike(String stGLAccountCode) {

         if (stGLAccountCode==null) return null;

         final String[] sc = stGLAccountCode.split("[\\|]");

         stGLAccountCode = sc[0];
         String stGLDescCode = sc.length>1?sc[1]:null;

         stGLAccountCode = GLUtil.Chart.getInstance().getChartCodeOnly(stGLAccountCode);

         final boolean enableAutoSuffix = stGLDescCode==null;

         if (stGLDescCode == null) {
            stGLDescCode = Chart.getInstance().getChartDescription(stGLAccountCode);
         }

         final Iterator codeit = codeMap.keySet().iterator();

         while (codeit.hasNext()) {
            Character c = (Character) codeit.next();

            if (enableAutoSuffix)
               if (stGLAccountCode.indexOf(c.charValue())>=0) {
                  final String substCode = "%"+c+"%";

                  if (descMap.containsKey(substCode)) {
                     stGLDescCode+=" - "+substCode;
                  }
               }

            stGLAccountCode = applyCode(stGLAccountCode, c.charValue(), (String) codeMap.get(c));
         }

         final Iterator descit = descMap.keySet().iterator();

         while (descit.hasNext()) {
            String k = (String) descit.next();

            stGLDescCode = StringTools.replace(stGLDescCode, k, (String) descMap.get(k));

            stGLDescCode = LanguageManager.getInstance().translate(stGLDescCode);
            
            glDesc = stGLDescCode;
         }
         
         final AccountView ac = cache.autoCreateAccountUsingLike(stGLAccountCode, stGLDescCode);

         errMsg = cache.errMsg;

         previewDesc = stGLAccountCode+" / "+stGLDescCode;

         return ac==null?null:ac.getStAccountID();
      }

      public String getPreviewDesc() {
         return previewDesc;
      }
      
      public String getStGLDesc(){
      	 return glDesc;
      }
      
      public static void main(String [] args) throws Exception {
      
            

      }
      
    
   }
}
