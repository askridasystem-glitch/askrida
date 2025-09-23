/***********************************************************************
 * Module:  com.webfin.gl.accounts.helper.AccountsHelper
 * Author:  Denny Mahendra
 * Created: Jul 19, 2005 2:48:58 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.accounts.helper;

import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.CostCenterManager;
import com.webfin.gl.accounts.filter.AccountFilter;
import com.webfin.gl.accounts.model.SelectAccountForm;
import com.webfin.gl.model.AccountView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.common.controller.Helper;
import com.crux.pool.DTOPool;
import com.crux.util.DateUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.BiayaOperasionalDetail;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Date;

public class AccountsHelper extends Helper {
    private final static transient LogManager logger = LogManager.getInstance(AccountsHelper.class);
    
   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final AccountFilter f = new AccountFilter();
      updatePaging(rq,f);
      final DTOList list = getRemoteGeneralLedger().listAccounts(f);
      put(rq,"FILTER",f);
      rq.setAttribute("LIST",list);
   }

   public void refresh(HttpServletRequest rq)  throws Exception {
      final AccountFilter f = (AccountFilter)get(rq,"FILTER");
      updatePaging(rq, f);
      final DTOList list = getRemoteGeneralLedger().listAccounts(f);
      rq.setAttribute("LIST",list);
   }

   public void create(HttpServletRequest rq)  throws Exception {
      final AccountView account = new AccountView();
      account.markNew();
      populate(rq, account);
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final AccountView account = view(rq);
      account.markUpdate();
   }

   public AccountView view(HttpServletRequest rq)  throws Exception {
      final String accountID = getString(rq.getParameter("accountid"));
      final AccountView account = getRemoteGeneralLedger().getAccount(accountID);
      return populate(rq, account);
   }

   private AccountView populate(HttpServletRequest rq, final AccountView account) throws Exception {
      put(rq,"ACCOUNT",account);
      rq.setAttribute("ACCOUNT",account);
      rq.setAttribute("CBAT",getRemoteGeneralLedger().getAccountTypesCombo());
      return account;
   }

   public void save(HttpServletRequest rq)  throws Exception {
      final AccountView account = (AccountView)get(rq,"ACCOUNT");

      account.setStAccountNo(getString(rq.getParameter("accountno")));
      account.setStDescription(getString(rq.getParameter("desc")));
      account.setStAccountType(getString(rq.getParameter("actype")));
      account.setStAllocatedFlag(getFlag(rq.getParameter("allocflag")));
      account.setDbBalanceOpen(getNum(rq.getParameter("ob")));

      getRemoteGeneralLedger().saveAccount(account);
   }

   /*
   public void select(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      String costcenter = getString(rq.getParameter("costcenter"));
      String method = getString(rq.getParameter("method"));

      final SelectAccountForm form = new SelectAccountForm ();

      if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();
      
      form.setStKey(key);
      form.setStCostCenter(costcenter);
      form.setStMethod(method);

      if (key!=null) {
         final DTOList list = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      coalesce(b.accountno, a.accountno) as accountno, a.description, a.acctype, b.account_id,a.enabled " +
                 "   from " +
                 "      gl_chart a" +
                 "      left join gl_accounts b on b.accountno = (a.accountno)" +
                 "   where " +
                 "      upper(a.accountno || a.description) like ? " +
                 "   limit 100",
                 new Object [] {'%'+key.toUpperCase()+'%'},
                 AccountView.class);

         rq.setAttribute("LIST",list);
      }

      rq.setAttribute("FORM",form);
   }*/
   
  public void select(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      String costcenter = getString(rq.getParameter("costcenter"));
      String acccode = getString(rq.getParameter("acccode"));
      final String accountno = getString(rq.getParameter("accountno"));
      String month = getString(rq.getParameter("month"));
      String year = getString(rq.getParameter("year"));

      if(month==null||month.equalsIgnoreCase("null"))
          month = String.valueOf(DateUtil.getMonthDigit(new Date()) - 1);
      else
          month = String.valueOf(Integer.parseInt(month) - 1);

      if(year==null||year.equalsIgnoreCase("null"))
          year = DateUtil.getYear(new Date());

      final SelectAccountForm form = new SelectAccountForm ();

      if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

      form.setStKey(key);
      form.setStCostCenter(costcenter);
      form.setStMethod(acccode);
      form.setStAccountNo(accountno);

      final String saldo = GLUtil.getPeriodList(0,Long.parseLong(month),"bal","+");

//      if(acccode!=null)
//            acccode = acccode.replaceAll("#", "%");

         DTOList list = null;
         if(!acccode.equalsIgnoreCase("undefined")){
             
             final SQLAssembler sqa = new SQLAssembler();
             
             sqa.addSelect("* ,(select round(sum("+ saldo +"),2)"+
                         " from gl_acct_bal2 x "+
                         " where x.idr_flag = 'Y' and x.account_id = a.account_id "+
                         " and x.period_year = "+ year +"  limit 1 ) as bal_open");
             sqa.addQuery( " from " +
                 " gl_accounts a");
             
             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }
             
             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             }
             
             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             }

             if(acccode.equalsIgnoreCase("")){
                 sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ?)");
                 sqa.addPar(costcenter);
             }             
             
             if(!acccode.equalsIgnoreCase("")){
                 sqa.addClause("a.accountno like ?");
                 sqa.addPar(acccode+"%");
             }

             sqa.addClause("coalesce(deleted,'N') <> 'Y' ");
             sqa.addClause("a.acctype is null");

             sqa.addOrder("a.accountno");

             sqa.setLimit(100);
             
             list = sqa.getList(AccountView.class);
             
         }else{
             final SQLAssembler sqa = new SQLAssembler();
             
             sqa.addSelect("* ,(select bal"+ month +
                         " from gl_acct_bal2 x "+
                         " where x.idr_flag = 'Y' and x.account_id = a.account_id "+
                         " and x.period_year = "+ year +"  limit 1) as bal_open");

             sqa.addQuery( " from " +
                 " gl_accounts a");
             
             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }
             
             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             }
             
             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             }
             
             sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ?)");
             sqa.addPar(costcenter);

             //sqa.addClause("substr(accountno,0,4) not in('121','121','122')");

             sqa.addClause("coalesce(deleted,'N') <> 'Y'");
             sqa.addClause("a.acctype is null");
             
             sqa.addOrder("a.accountno");

             sqa.setLimit(100);
             
             list = sqa.getList(AccountView.class);
             
         }

         rq.setAttribute("LIST",list);
      

      rq.setAttribute("FORM",form);
   }
  

   public void autoCreate(HttpServletRequest rq)  throws Exception {
      final String acno = getString(rq.getParameter("acno"));
      final String costcenter = getString(rq.getParameter("costcenter"));
	  //final String acid = getString(rq.getParameter("acid"));
      
      //if(acid!=null){
      	  final AccountView ac = getRemoteGeneralLedger().autoCreateAccount(acno,costcenter);

          rq.setAttribute("AC",ac);
      //}
      
   }
   
   public void createAccountCode(HttpServletRequest rq)throws Exception{
      
        String costcenter = getString(rq.getParameter("costcenter"));
        
        AccountView view = new AccountView();
        
        view.markNew();

        rq.setAttribute("JH",view);
        
        rq.setAttribute("costcenter",costcenter);
 
        populate(rq,view);
    }
    
  
  public void select2(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      String costcenter = getString(rq.getParameter("costcenter"));

      final SelectAccountForm form = new SelectAccountForm ();

      if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

      form.setStKey(key);
      form.setStCostCenter(costcenter);

      if (key!=null) {
         final DTOList list = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      coalesce(b.accountno, a.accountno) as accountno, a.description, a.acctype, b.account_id " +
                 "   from " +
                 "      gl_chart a" +
                 "      left join gl_accounts b on b.accountno = (a.accountno || ' ' || ?)" +
                 "   where " +
                 "      upper(a.accountno || a.description) like ?" +
                 "   limit 100",
                 new Object [] {costcenter, '%'+key.toUpperCase()+'%'},
                 AccountView.class);

         rq.setAttribute("LIST2",list);
      }

      rq.setAttribute("FORM",form);
   }
   
    public void saveAccount(HttpServletRequest rq)throws Exception{

      final AccountView account = (AccountView)get(rq,"ACCOUNT");

      //final AccountView account = new AccountView();

      account.setStAccountNo(getString(rq.getParameter("accountnew")));
      account.setStDescription(getString(rq.getParameter("description")));
      account.setStCostCenterCode(getString(rq.getParameter("costcenter")));
      
      account.setStCreateWho(SessionManager.getInstance().getUserID());
      account.setDtCreateDate(new java.util.Date());
 
      account.markNew();

      getRemoteGeneralLedger().saveAccount(account);

    }
    
    public void select3(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        String costcenter = getString(rq.getParameter("costcenter"));

        final SelectAccountForm form = new SelectAccountForm();

        if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

        form.setStKey(key);
        form.setStCostCenter(costcenter);

        if (key!=null) {
            DTOList list = null;

                list = ListUtil.getDTOListFromQuery(
                        "   select " +
                        "      * " +
                        "   from " +
                        "      gl_accounts a" +
                        "   where " +
                        "      upper(a.accountno || a.description) like ?" +
                        "    and substring(a.accountno,14) = ? "+
                        " and (accountno like ('122%') or accountno like ('121%')) and coalesce(a.deleted,'N') <> 'Y' "+
                        "   limit 100",
                        new Object [] {'%'+key.toUpperCase()+'%',costcenter},
                        AccountView.class);

            rq.setAttribute("LIST",list);
        }

        rq.setAttribute("FORM",form);
    }
    
    public void autoCreateCashBank(HttpServletRequest rq)  throws Exception {
        final String acno = getString(rq.getParameter("stAccountNo"));
        final String costcenter = getString(rq.getParameter("costcenter"));
        //final String acid = getString(rq.getParameter("acid"));
        
        //if(acid!=null){
        final AccountView ac = getRemoteGeneralLedger().autoCreateAccount(acno,costcenter);
        
        rq.setAttribute("AC",ac);
        //}
        
    }
    
    public void selectPB(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      String costcenter = getString(rq.getParameter("costcenter"));
      String acccode = getString(rq.getParameter("acccode"));
      final String accountno = getString(rq.getParameter("accountno"));

      final SelectAccountForm form = new SelectAccountForm ();

      if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

      form.setStKey(key);
      form.setStCostCenter(costcenter);
      form.setStMethod(acccode);
      form.setStAccountNo(accountno);

      /*
      if (key!=null) {
         final DTOList list = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      coalesce(b.accountno, a.accountno) as accountno, a.description, a.acctype, b.account_id " +
                 "   from " +
                 "      gl_chart a" +
                 "      left join gl_accounts b on b.accountno = (a.accountno || ' ' || ?)" +
                 "   where " +
                 "      upper(a.accountno || a.description) like ?" +
                 "   limit 100",
                 new Object [] {costcenter, '%'+key.toUpperCase()+'%'},
                 AccountView.class);

         rq.setAttribute("LIST",list);
      }*/
      
         DTOList list = null;
         if(!acccode.equalsIgnoreCase("undefined")){
             
             final SQLAssembler sqa = new SQLAssembler();
             
             sqa.addSelect("*");
             sqa.addQuery( " from " +
                 " gl_accounts a");
             
             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }
             
             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             }
             
             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             }
             
             sqa.addClause("a.accountno like ?");
             sqa.addPar(acccode+"%");
             
             sqa.setLimit(100);
             
             list = sqa.getList(AccountView.class);
             
         }else{
             final SQLAssembler sqa = new SQLAssembler();
             
             sqa.addSelect("*");
             sqa.addQuery( " from " +
                 " gl_accounts a");
             
             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }
             
             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             }
             
             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             }
             sqa.addClause("(upper(a.accountno) like '1221%' or upper(a.accountno) like '791%')");
             
             sqa.setLimit(100);
             
             list = sqa.getList(AccountView.class);
             
         }

         rq.setAttribute("LIST",list);
      

      rq.setAttribute("FORM",form);
   }

    public void selectAccountNotKasBank(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      String costcenter = getString(rq.getParameter("costcenter"));
      String acccode = getString(rq.getParameter("acccode"));
      final String accountno = getString(rq.getParameter("accountno"));
      String month = getString(rq.getParameter("month"));
      String year = getString(rq.getParameter("year"));
      String bulanTrx = "";


      if(month==null||month.equalsIgnoreCase("null"))
          bulanTrx = String.valueOf(DateUtil.getMonthDigit(new Date()));
      else
          bulanTrx = String.valueOf(Integer.parseInt(month));

      if(month==null||month.equalsIgnoreCase("null"))
          month = String.valueOf(DateUtil.getMonthDigit(new Date()) - 1);
      else
          month = String.valueOf(Integer.parseInt(month) - 1);

      if(year==null||year.equalsIgnoreCase("null"))
          year = DateUtil.getYear(new Date());

      final SelectAccountForm form = new SelectAccountForm ();

      if (costcenter==null) costcenter = CostCenterManager.getInstance().getCurrentCostCenter();

      form.setStKey(key);
      form.setStCostCenter(costcenter);
      form.setStMethod(acccode);
      form.setStAccountNo(accountno);
      form.setStMonth(month);
      form.setStYear(year);

      final String saldo = GLUtil.getPeriodList(0,Long.parseLong(month),"bal","+");

//      if(acccode!=null)
//            acccode = acccode.replaceAll("#", "%");

         DTOList list = null;
         if(!acccode.equalsIgnoreCase("undefined")){

             final SQLAssembler sqa = new SQLAssembler();

             sqa.addSelect("* ,(select round(sum("+ saldo +"),2)"+
                         " from gl_acct_bal2 x "+
                         " where x.idr_flag = 'Y' and x.account_id = a.account_id "+
                         " and x.period_year = "+ year +"  limit 1 ) as bal_open"+
                         " ,(select division_code from gl_accounts_division y where y.account_header like substr(a.accountno,0,6)||'%' limit 1) as division_code ");
             sqa.addQuery( " from " +
                 " gl_accounts a");

             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }

             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             } 

             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             }

             /*
             if(acccode.equalsIgnoreCase("")){
                 sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ? "+
                           " and extract('year' from period_start) <= ? "+
                           " and extract('month' from period_start) <= ?)");

                 sqa.addPar(costcenter);
                 sqa.addPar(year);
                 sqa.addPar(bulanTrx);
             }*/

             if(acccode.equalsIgnoreCase("")){
                 sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ? )");

                 sqa.addPar(costcenter);
             }

             if(!acccode.equalsIgnoreCase("")){
                 sqa.addClause("a.accountno like ?");
                 sqa.addPar(acccode+"%");
             }

             sqa.addClause("coalesce(deleted,'N') <> 'Y'");
             sqa.addClause("a.acctype is null");

             sqa.addOrder("a.accountno");

             sqa.setLimit(100);

             list = sqa.getList(AccountView.class);

         }else{
             final SQLAssembler sqa = new SQLAssembler();

             sqa.addSelect("* ,(select bal"+ month +
                         " from gl_acct_bal2 x "+
                         " where x.idr_flag = 'Y' and x.account_id = a.account_id "+
                         " and x.period_year = "+ year +"  limit 1) as bal_open"+
                         " ,(select division_code from gl_accounts_division y where y.account_header like substr(a.accountno,0,6)||'%' limit 1) as division_code ");

             sqa.addQuery( " from " +
                 " gl_accounts a");

             if(key!=null){
                 sqa.addClause("upper(a.description) like ?");
                 sqa.addPar("%"+ key.toUpperCase() +"%");
             }

             if(accountno!=null){
                 sqa.addClause("upper(a.accountno) like ?");
                 sqa.addPar(accountno.toUpperCase() +"%");
             }

             if(costcenter!=null){
                 sqa.addClause("substring(a.accountno,14) = ?");
                 sqa.addPar(costcenter);
             } 

             /*
             sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ?"+
                           " and extract('year' from period_start) <= ? "+
                           " and extract('month' from period_start) <= ?)");

             sqa.addPar(costcenter);
             sqa.addPar(year);
             sqa.addPar(bulanTrx);
             */

             sqa.addClause("substr(accountno,0,6) not in "+
                           " (select accountno "+
                           " from "+
                           " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ?)");

             sqa.addPar(costcenter);

             //sqa.addClause("substr(accountno,0,4) not in('121','121','122')");

             sqa.addClause("coalesce(deleted,'N') <> 'Y'");
             sqa.addClause("a.acctype is null");

             sqa.addOrder("a.accountno");

             sqa.setLimit(100);

             list = sqa.getList(AccountView.class);

         }

         rq.setAttribute("LIST",list);


      rq.setAttribute("FORM",form);
   }

   public void select4(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        String costcenter = getString(rq.getParameter("costcenter"));
        String biaoptype = getString(rq.getParameter("biaoptype"));
        final SelectAccountForm form = new SelectAccountForm();

        if (costcenter == null) {
            costcenter = CostCenterManager.getInstance().getCurrentCostCenter();
        }

        form.setStKey(key);
        form.setStCostCenter(costcenter);

        DTOList list = null;
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from gl_accounts a");

        if (key != null) {
            sqa.addClause("upper(a.description) like ?");
            sqa.addPar("%" + key.toUpperCase() + "%");
        }

        if (costcenter != null) {
            sqa.addClause("substring(a.accountno,14,2) = ?");
            sqa.addPar(costcenter);
        }

        sqa.addClause("coalesce(a.deleted,'N') <> 'Y'");
        sqa.addClause("a.enabled2 = 'Y'");
        sqa.addClause("a.acctype is null");

//        sqa.addClause("substr(accountno,0,6) not in "
//                + " (select accountno "
//                + " from "
//                + " gl_accounts_insurance2 where active_flag = 'Y' and cc_code = ?)");
//        sqa.addPar(costcenter);

        final BiayaOperasionalDetail polType = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, biaoptype);

        if (polType.getStAccount() == null) {
            throw new RuntimeException("Akun tidak ada");
        }

        String sql = sqa.getSQL();

        if (biaoptype != null) {
            String biaopType2[] = polType.getStAccount().split("[\\|]");
            if (biaopType2.length == 1) {
                sql = sqa.getSQL() + " and (substr(a.accountno,1," + polType.getStAccount().length() + ") between '" + polType.getStAccount() + "' and '" + polType.getStaccount2() + "')";
            } else if (biaopType2.length > 1) {
                sql = sqa.getSQL() + " and (a.accountno like '" + biaopType2[0] + "%'";
                for (int k = 1; k < biaopType2.length; k++) {
                    sql = sql + " or a.accountno like '" + biaopType2[k] + "%'";
                }
                sql = sql + " ) ";
            }
        }

        sql = sql + " order by a.accountno ";

        list = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), AccountView.class);

        rq.setAttribute("LIST", list);

        rq.setAttribute("FORM", form);
    }

    public void selectRKAP(HttpServletRequest rq) throws Exception {
        String key = getString(rq.getParameter("key"));
        String costcenter = getString(rq.getParameter("costcenter"));
        String unit = getString(rq.getParameter("unit"));
        String akun = getString(rq.getParameter("akun"));
        String type = getString(rq.getParameter("type"));

        String years = getString(rq.getParameter("years"));
        if (years == null || years.equalsIgnoreCase("null")) {
            years = DateUtil.getYear(new Date());
        }

        final SelectAccountForm form = new SelectAccountForm();

        if (costcenter == null) {
            costcenter = CostCenterManager.getInstance().getCurrentCostCenter();
        }

        int tahun = Integer.parseInt(years) - 1;
        String jenis = null;
        if (type == null) {
            jenis = "N";
        } else {
            if (type.equalsIgnoreCase("1")) {
                jenis = "N";
            } else if (type.equalsIgnoreCase("2")) {
                jenis = "Y";
            }
        }

        logger.logDebug("@@@@@@@@@@@@@@@@@@@1 " + costcenter);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@2 " + unit);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@3 " + akun);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@4 " + type);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@5 " + years);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@6 " + jenis);
        logger.logDebug("@@@@@@@@@@@@@@@@@@@7 " + key);

        form.setStKey(key);
        form.setStCostCenter(costcenter);
        form.setStUnit(unit);
        form.setStAccountNo(akun);
        form.setStType(jenis);
        form.setStYear(String.valueOf(tahun));

        DTOList list = null;
        final SQLAssembler sqa = new SQLAssembler();

        /*
         *  f.id_kertas_kerja as account_id,f.no_account as accountno,f.rencana_kerja as description,
        coalesce(f.jumlah_pusat,0) as anggaran,f.kode as rekno,coalesce(f.jumlah_pusat,0)-coalesce(f.anggaran_terpakai,0) as sisaanggaran
        from kebijakan_perusahaan a
        join identifikasi_masalah b on (a.id=b.id_kebijakan)
        join cabang c on (b.kode_cabang=c.kode_cabang)
        join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah)
        join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja)
        join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran)
        join gl_chart g on (f.no_account=g.no_account)
        join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya)
        where c.cc_code = '00' and b.tahun = '2020' and c.kode_cabang = 55 and h.id_jenis_biaya = 2
        order by f.id_kertas_kerja
         */

        String select = null;
        String from = null;
        String order = null;
        if (akun.equalsIgnoreCase("14")) {
            select = "e.id_sub_barang as account_id,f.kode_akun as accountno,f.nama_barang as description,"
                    + "coalesce(e.jumlah_pusat,0) as anggaran,e.id_sub_barang as rekno,coalesce(e.jumlah_pusat,0)-coalesce(e.anggaran_terpakai,0) as sisaanggaran ";

            from = "from kebijakan_perusahaan a "
                    + "join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                    + "join cabang c on (b.kode_cabang=c.kode_cabang) "
                    + "join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                    + "join sub_barang e on (d.id_program_kerja=e.id_program_kerja) "
                    + "join barang f on (e.id_barang=f.id_barang) "
                    + "join gl_chart g on (f.kode_akun=g.no_account) "
                    + "join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya) ";

            order = " order by e.id_sub_barang limit 100 ";
        } else {
            select = "f.id_kertas_kerja as account_id,f.no_account as accountno,f.rencana_kerja as description,"
                    + "coalesce(f.jumlah_pusat,0) as anggaran,f.kode as rekno,coalesce(f.jumlah_pusat,0)-coalesce(f.anggaran_terpakai,0) as sisaanggaran ";

            from = " from kebijakan_perusahaan a "
                    + "join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                    + "join cabang c on (b.kode_cabang=c.kode_cabang) "
                    + "join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                    + "join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja) "
                    + "join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran) "
                    + "join gl_chart g on (f.no_account=g.no_account) "
                    + "join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya) ";

            order = " order by f.id_kertas_kerja limit 100 ";
        }

        sqa.addSelect(select);

        sqa.addQuery(from);

        if (key != null) {
            if (akun.equalsIgnoreCase("14")) {
                sqa.addClause("upper(f.nama_barang) like ?");
                sqa.addPar("%" + key.toUpperCase() + "%");
            } else {
                sqa.addClause("upper(f.rencana_kerja) like ?");
                sqa.addPar("%" + key.toUpperCase() + "%");
            }
        }

        if (costcenter != null) {
            sqa.addClause("c.cc_code = ?");
            sqa.addPar(costcenter);
        }

        if (years != null) {
            sqa.addClause("b.tahun = ?");
            sqa.addPar(tahun);
        }

        if (unit != null) {
            sqa.addClause("c.kode_cabang = ?");
            sqa.addPar(unit);
        }

        if (!akun.equalsIgnoreCase("14")) {
            if (akun != null) {
                sqa.addClause("h.id_jenis_biaya = ?");
                sqa.addPar(akun);
            }

            if (Tools.isYes(jenis)) {
                sqa.addClause("f.biaya_rutin = 'Y'");
            } else {
                sqa.addClause("f.biaya_rutin = 'N'");
            }
        }

        String sql = sqa.getSQL() + order;

        list = ListUtil.getDTOListFromQueryDS(sql, sqa.getPar(), AccountView.class, "RKAPDB");

        rq.setAttribute("LIST", list);

        rq.setAttribute("FORM", form);
    }
    
}
