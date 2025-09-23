/***********************************************************************
 * Module:  com.webfin.gl.ejb.GLReportEngine2
 * Author:  Denny Mahendra
 * Created: Apr 13, 2007 1:44:06 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.gl.ejb;

import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.gl.model.GLCostCenterView3;
import com.webfin.gl.util.GLUtil;
import com.webfin.gl.model.GLInfoView;
import com.webfin.gl.model.JournalView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Date;

public class GLReportEngine2 {

    public HashMap vars = new HashMap();
    private String branch;
    private String stFlag;
    private String stEntityID;
    private Date appDateFrom;
    private Date appDateTo;
    private Date entryDateFrom;
    private Date entryDateTo;
    private String stAccountNo;
    private String stDescription;
    private String stLang;
    private String clAccDet;
    private String bulan;
    private String tahun;
    private String region;
    private String level;

    public boolean isClAccDet() {
        return Tools.isYes(clAccDet);
    }

    public String getClAccDet() {
        return clAccDet;
    }

    public void setClAccDet(String clAccDet) {
        this.clAccDet = clAccDet;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public BigDecimal getSummaryRanged(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfo(flags, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo);

        if (gll == null) {
            return null;
        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");
        }

        GLInfoView gli = (GLInfoView) gll.get(0);

        return gli.getDbAmount();
    }

    ;

    public DTOList getGLInfo(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;
        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */

        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            boolean getMutation = pmap.containsKey("MUT");

            boolean getBalance = pmap.containsKey("BAL");
            boolean getDB = pmap.containsKey("DB");
            boolean getCR = pmap.containsKey("CR");

            boolean getDetail = pmap.containsKey("DET");

            boolean skipZero = pmap.containsKey("SKIPZERO");

            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");

            if ("default".equals(colSet)) {
                colSet = null;
            }

            //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');
            int branchLen = GLUtil.getMaskLength('B');

            if (getBalance) {
                periodFrom = 0;
            }

            /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";

            if (getDB) {
                fld = "db";
            }

            if (getCR) {
                fld = "cr";
            }


            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");
            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno,14,2) = ? ");
                sqa.addPar(branch);
            }

            if (region != null) {
                sqa.addClause("a.region_id = ? ");
                sqa.addPar(region);
            }

//            if (branch != null) {
//                if (region != null) {
//                    if (level.equalsIgnoreCase("1")) {
//                        sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                        sqa.addPar(region);
//                    }
//                    if (level.equalsIgnoreCase("2")) {
//                        sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                        sqa.addPar(branch);
//                    }
//                } else {
//                    sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                    sqa.addPar(branch);
//                }
//            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");
            }

            DTOList gll;

            String sql = sqa.getSQL();

//            if (getBranch() != null) {
//                if (getRegion() != null) {
//                    sql = sqa.getSQL();
//                    if (level.equalsIgnoreCase("1")) {
//                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
//                        sqa.addPar(region);
//                    }
//                    if (level.equalsIgnoreCase("2")) {
//                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
//                        sqa.addPar(branch);
//                    }
//                } else {
//                    String subcode[] = getCostCenter().getStSubCode().split("[\\|]");
//                    if (subcode.length == 1) {
//                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
//                        sqa.addPar(branch);
//                    } else if (subcode.length > 1) {
//                        sql = sql + " and (substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[0] + "'";
//                        for (int k = 1; k < subcode.length; k++) {
//                            sql = sql + " or substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[k] + "'";
//                        }
//                        sql = sql + ")";
//                    }
//                }
//            }

            if (getDetail) {

                gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sql);

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);
            }

            BigDecimal tot = null;

            for (int i = 0; i < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();

                if (negated) {
                    bd = BDUtil.negate(bd);
                }

                tot = BDUtil.add(tot, bd);
            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");

                for (int j = 0; j < varz.length; j++) {
                    String v = varz[j];

                    if (colSet != null) {
                        v += "/" + colSet;
                    }

                    BigDecimal x = (BigDecimal) vars.get(v);

                    for (int i = 0; i < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);
                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);
                }
            }

            return gll;

        } finally {

            S.release();

        }
    }

    public BigDecimal getDbVariable(String varname) {
        return getDbVariable(varname, null);


    }

    public BigDecimal getDbVariable(String varname, String colSet) {
        if ("default".equals(colSet)) {
            colSet = null;


        }
        if (colSet != null) {
            varname += "/" + colSet;


        }
        return (BigDecimal) vars.get(varname);


    }

    public HashMap tes() {
        return vars;


    }

    public void setVariable(String varName, Object x) {
        setVariable(varName, null, x);


    }

    public void setVariable(String varName, String colSet, Object x) {
        if ("default".equals(colSet)) {
            colSet = null;


        }
        if (colSet != null) {
            varName += "/" + colSet;


        }
        vars.put(varName, x);


    }

    public void getDetailRanged(String s, String acctFrom, String acctTo, long lPeriodFrom, long lPeriodTo, long lYearFrom, long lYearTo) {
    }

    public BigDecimal getSummaryRangedExcluded(
            String flags,
            String fromAcct, String toAcct,
            String excAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithExcluded(flags, fromAcct, toAcct, excAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithExcluded(
            String flags,
            String fromAcct, String toAcct,
            String excAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            String excAccount[] = excAcct.split("[\\|]");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            if (excAccount != null) {
                for (int i = 0; i
                        < excAccount.length; i++) {
                    m = excAccount[i].length();
                    sqa.addClause("((substr(b.accountno,1," + m + ")) not like ?)");
                    sqa.addPar(excAccount[i]);
                }
            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");
            }

//            if (branch != null) {
//                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                sqa.addPar(branch);
//            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");
            }

            DTOList gll;

            String sql = sqa.getSQL();

            if (getBranch() != null) {
                if (getRegion() != null) {
                    sql = sqa.getSQL();
                    if (level.equalsIgnoreCase("1")) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(region);
                    }
                    if (level.equalsIgnoreCase("2")) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(branch);
                    }
                } else {
                    String subcode[] = getCostCenter().getStSubCode().split("[\\|]");
                    if (subcode.length == 1) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(branch);
                    } else if (subcode.length > 1) {
                        sql = sql + " and (substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[0] + "'";
                        for (int k = 1; k < subcode.length; k++) {
                            sql = sql + " or substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[k] + "'";
                        }
                        sql = sql + ")";
                    }
                }
            }

            if (getDetail) {

                gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sql);

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
                
                PS.setString(1,fromAcct);
                
                PS.setString(2,toAcct);
                
                PS.setLong(3,yearFrom);
                
                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public DTOList getGLJeDetail(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            //boolean getMutation = pmap.containsKey("MUT");

            //boolean getBalance = pmap.containsKey("BAL");
            //boolean getDB = pmap.containsKey("DB");
            //boolean getCR = pmap.containsKey("CR");

            //boolean getDetail = pmap.containsKey("DET");

            //boolean skipZero = pmap.containsKey("SKIPZERO");

            //boolean negated = pmap.containsKey("NEG");

            //String colSet = (String) pmap.get("COLSET");

            //if ("default".equals(colSet)) colSet=null;

            //if (colSet==null) colSet="DEFAULT";



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            //if (getBalance) periodFrom = 0;

            //String fld = "bal";

            //if (getDB) fld="db";

            //if (getCR) fld="cr";

            //String plist = GLUtil.getPeriodList(periodFrom,periodTo,fld,"+");

            String func = null;

            //if (getMutation) func="sum";

            //if (getBalance) func="sum";

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            /* select a.*,b.accountno,b.description
            from gl_je_detail a
            left join gl_accounts b  on a.accountid = b. account_id*/

            sqa.addSelect(" a.*,b.accountno,b.description");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from " + gljedetail + " a "
                    + "            left join gl_accounts b  on a.accountid = b.account_id\n"
                    + "           left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.fiscal_year between ? and ?)");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            /*
            if (getDetail) {
            sqa.addSelect("b.accountno as account_no");
            sqa.addSelect("b.description as account_name");
            sqa.addGroup("b.accountno");
            sqa.addGroup("b.description");
            }*/



            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (stEntityID != null) {
                sqa.addClause("c.account_entity_id = ?");
                sqa.addPar(stEntityID);


            }

            DTOList gll;

            //if (getDetail) {

            String sq = sqa.getSQL();

            /*if (skipZero) {
            sq = "select * from ("+sq+") x where bx<>0";
            }*/

            gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), JournalView.class);

            //} else {
            /*PreparedStatement PS = S.setQuery(sqa.getSQL());
            
            S.setParams(sqa.getPar());*/

            /*PreparedStatement PS = S.setQuery(
            "   select "+func+"("+plist+") as bx " +
            "      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n" +
            "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
            
            PS.setString(1,fromAcct);
            
            PS.setString(2,toAcct);
            
            PS.setLong(3,yearFrom);
            
            PS.setLong(4,yearTo);*/

            /*ResultSet RS = PS.executeQuery();
            
            RS.next();
            
            BigDecimal bd = RS.getBigDecimal("bx");
            
            GLInfoView gli = new GLInfoView();
            
            gli.setDbAmount(bd);
            
            gll = new DTOList();
            
            gll.add(gli);*/
            //}

            /*
            BigDecimal tot = null;
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            
            BigDecimal bd = gli.getDbAmount();
            
            if (negated) bd = BDUtil.negate(bd);
            
            tot = BDUtil.add(tot, bd);
            }
            
            if (pmap.containsKey("ADD")) {
            String vn = (String) pmap.get("ADD");
            
            String[] varz = vn.split(",");
            
            for (int j = 0; j < varz.length; j++) {
            String v = varz[j];
            
            if (colSet!=null) v += "/"+colSet;
            
            BigDecimal x = (BigDecimal) vars.get(v);
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            }
            
            x = BDUtil.add(x,tot);
            
            vars.put(v,x);
            }
            }*/





            return gll;

        } finally {

            S.release();



        }
    }

    public BigDecimal getGLNew(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            //boolean getMutation = pmap.containsKey("MUT");

            //boolean getBalance = pmap.containsKey("BAL");
            //boolean getDB = pmap.containsKey("DB");
            //boolean getCR = pmap.containsKey("CR");

            //boolean getDetail = pmap.containsKey("DET");

            //boolean skipZero = pmap.containsKey("SKIPZERO");

            //boolean negated = pmap.containsKey("NEG");

            //String colSet = (String) pmap.get("COLSET");

            //if ("default".equals(colSet)) colSet=null;

            //if (colSet==null) colSet="DEFAULT";



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            //if (getBalance) periodFrom = 0;

            //String fld = "bal";

            //if (getDB) fld="db";

            //if (getCR) fld="cr";

            //String plist = GLUtil.getPeriodList(periodFrom,periodTo,fld,"+");

            String func = null;

            //if (getMutation) func="sum";

            //if (getBalance) func="sum";

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            /* select a.*,b.accountno,b.description
            from gl_je_detail a
            left join gl_accounts b  on a.accountid = b. account_id*/

            sqa.addSelect(" SUM(coalesce(A.CREDIT,a.debit)) as total");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from " + gljedetail + " a "
                    + "            left join gl_accounts b  on a.accountid = b.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.fiscal_year between ? and ?)");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            /*
            if (getDetail) {
            sqa.addSelect("b.accountno as account_no");
            sqa.addSelect("b.description as account_name");
            sqa.addGroup("b.accountno");
            sqa.addGroup("b.description");
            }*/



            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            //DTOList gll;

            //if (getDetail) {

            String sq = sqa.getSQL();

            /*if (skipZero) {
            sq = "select * from ("+sq+") x where bx<>0";
            }*/

            //gll = ListUtil.getDTOListFromQuery(sq,sqa.getPar(), JournalView.class);


            PreparedStatement PS = S.setQuery(sqa.getSQL());

            S.setParams(sqa.getPar());

            /*PreparedStatement PS = S.setQuery(
            "   SUM(coalesce(A.CREDIT,a.debit)) as total " +
            "      from \n" +
            "          gl_je_detail a \n" +
            "            inner join gl_accounts b on a.accountid = b.account_id\n" +
            "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");*/

            PS.setString(1, fromAcct);

            PS.setString(2, toAcct);

            PS.setLong(3, yearFrom);

            PS.setLong(4, yearTo);

            ResultSet RS = PS.executeQuery();

            RS.next();

            BigDecimal bd = RS.getBigDecimal("total");


            //} else {
            /*PreparedStatement PS = S.setQuery(sqa.getSQL());
            
            S.setParams(sqa.getPar());*/

            /*PreparedStatement PS = S.setQuery(
            "   select "+func+"("+plist+") as bx " +
            "      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n" +
            "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
            
            PS.setString(1,fromAcct);
            
            PS.setString(2,toAcct);
            
            PS.setLong(3,yearFrom);
            
            PS.setLong(4,yearTo);*/

            /*ResultSet RS = PS.executeQuery();
            
            RS.next();
            
            BigDecimal bd = RS.getBigDecimal("bx");
            
            GLInfoView gli = new GLInfoView();
            
            gli.setDbAmount(bd);
            
            gll = new DTOList();
            
            gll.add(gli);*/
            //}

            /*
            BigDecimal tot = null;
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            
            BigDecimal bd = gli.getDbAmount();
            
            if (negated) bd = BDUtil.negate(bd);
            
            tot = BDUtil.add(tot, bd);
            }
            
            if (pmap.containsKey("ADD")) {
            String vn = (String) pmap.get("ADD");
            
            String[] varz = vn.split(",");
            
            for (int j = 0; j < varz.length; j++) {
            String v = varz[j];
            
            if (colSet!=null) v += "/"+colSet;
            
            BigDecimal x = (BigDecimal) vars.get(v);
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            }
            
            x = BDUtil.add(x,tot);
            
            vars.put(v,x);
            }
            }*/



            return bd;



        } finally {

            S.release();



        }
    }

    public DTOList getGLAcctCashBank(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            //boolean getMutation = pmap.containsKey("MUT");

            //boolean getBalance = pmap.containsKey("BAL");
            //boolean getDB = pmap.containsKey("DB");
            //boolean getCR = pmap.containsKey("CR");

            //boolean getDetail = pmap.containsKey("DET");

            //boolean skipZero = pmap.containsKey("SKIPZERO");

            //boolean negated = pmap.containsKey("NEG");

            //String colSet = (String) pmap.get("COLSET");

            //if ("default".equals(colSet)) colSet=null;

            //if (colSet==null) colSet="DEFAULT";



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            //if (getBalance) periodFrom = 0;

            //String fld = "bal";

            //if (getDB) fld="db";

            //if (getCR) fld="cr";

            //String plist = GLUtil.getPeriodList(periodFrom,periodTo,fld,"+");

            String func = null;
            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            //if (getMutation) func="sum";

            //if (getBalance) func="sum";



            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" a.*,b.accountno");

            sqa.addQuery(" from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) not between ? and ?) and (a.fiscal_year between ? and ?) and a.period_no = ?");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));
            sqa.addPar(String.valueOf(periodTo));

            sqa.addClause("a.summary_flag is null");
            sqa.addClause("a.ref_trx_type = 'CASHBANK'");



            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) >= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (stAccountNo != null) {
                sqa.addClause("a.hdr_accountno = ? ");
                sqa.addPar(stAccountNo);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.flag = 'Y'");


            }

            sqa.addOrder(" a.trx_id ");

            DTOList gll;

            //if (getDetail) {

            String sq = sqa.getSQL();

            /*if (skipZero) {
            sq = "select * from ("+sq+") x where bx<>0";
            }*/

            gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), JournalView.class);

            //} else {
            /*PreparedStatement PS = S.setQuery(sqa.getSQL());
            
            S.setParams(sqa.getPar());*/

            /*PreparedStatement PS = S.setQuery(
            "   select "+func+"("+plist+") as bx " +
            "      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n" +
            "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
            
            PS.setString(1,fromAcct);
            
            PS.setString(2,toAcct);
            
            PS.setLong(3,yearFrom);
            
            PS.setLong(4,yearTo);*/

            /*ResultSet RS = PS.executeQuery();
            
            RS.next();
            
            BigDecimal bd = RS.getBigDecimal("bx");
            
            GLInfoView gli = new GLInfoView();
            
            gli.setDbAmount(bd);
            
            gll = new DTOList();
            
            gll.add(gli);*/
            //}

            /*
            BigDecimal tot = null;
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            
            BigDecimal bd = gli.getDbAmount();
            
            if (negated) bd = BDUtil.negate(bd);
            
            tot = BDUtil.add(tot, bd);
            }
            
            if (pmap.containsKey("ADD")) {
            String vn = (String) pmap.get("ADD");
            
            String[] varz = vn.split(",");
            
            for (int j = 0; j < varz.length; j++) {
            String v = varz[j];
            
            if (colSet!=null) v += "/"+colSet;
            
            BigDecimal x = (BigDecimal) vars.get(v);
            
            for (int i = 0; i < gll.size(); i++) {
            GLInfoView gli = (GLInfoView) gll.get(i);
            }
            
            x = BDUtil.add(x,tot);
            
            vars.put(v,x);
            }
            }*/





            return gll;

        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryTotalCashBank(
            String flags, String fromAcct,
            String toAcct, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLAcctCashBankAwal(flags, fromAcct, toAcct, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLAcctCashBankAwal(
            String flags, String fromAcct,
            String toAcct, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String func = null;

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            String fld = "bal";

            String plist = GLUtil.getPeriodList(0, periodTo - 1, fld, "+");

            func = "sum";

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" sum(coalesce(bal0,0)) as bx ");

            sqa.addQuery(" from gl_acct_bal2 a  "
                    + " left join gl_accounts b on b.account_id = a.account_id ");

            sqa.addClause("b.acctype is null");
            sqa.addClause("a.idr_flag = 'Y'");
            sqa.addClause("a.period_year = ? ");
            sqa.addPar(String.valueOf(yearTo));



            if (stAccountNo != null) {
                sqa.addClause("b.accountno = ?");
                sqa.addPar(stAccountNo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno,14,2)=?");
                sqa.addPar(branch);


            }

            DTOList gll;
            String sql = null;

            sql = " select sum(bx) as bx from ( " + sqa.getSQL()
                    + " union all select sum(credit-debit) as bx "
                    + " from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid "
                    + " where b.acctype is null and a.ref_trx_type = 'CASHBANK' and a.flag = 'Y'";

            sql = sql + " and a.fiscal_year = ?";
            sqa.addPar(String.valueOf(yearFrom));
            sql = sql + " and ((substr(b.accountno,1," + l + ")) not between ? and ?)";
            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);



            if (getAppDateFrom() != null) {
                sql = sql + " and date_trunc('day',a.applydate) >= '" + DateUtil.getYear(appDateFrom) + "-01-01'";


            }

            if (getAppDateFrom() != null) {
                sql = sql + " and date_trunc('day',a.applydate) < '" + appDateFrom + "'";


            }

            if (getBranch() != null) {
                sql = sql + " and substr(b.accountno,14,2) = '" + branch + "'";


            }

            if (getStAccountNo() != null) {
                sql = sql + " and a.hdr_accountno = '" + stAccountNo + "'";


            }

            sql = sql + " ) a ";

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public String getStDescription() {
        return stDescription;


    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;


    }

    public String getStAccountNo() {
        return stAccountNo;


    }

    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;


    }

    public String getStEntityID() {
        return stEntityID;


    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;


    }

    public BigDecimal getSummaryRanged2(
            String flags, String fromAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLPeriod(flags, fromAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLPeriod(
            String flags, String fromAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        //if (fromAcct.length()<1) return null;

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */
            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }
            //int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(func + "(" + plist + ") as bx ");

            sqa.addQuery("from gl_acct_bal2 a  "
                    + "left join gl_accounts b on b.account_id = a.account_id");

            sqa.addClause("a.period_year between ? and ?");

            sqa.addClause("b.acctype is null");
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (stAccountNo != null) {
                sqa.addClause("b.accountno = ?");
                sqa.addPar(stAccountNo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            sqa.addClause("substr(b.accountno,1,5) = ? ");
            sqa.addPar(String.valueOf(fromAcct));



            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                /*if (skipZero) {
                sq = "select * from ("+sq+") x where bx<>0";
                }*/

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
                
                PS.setString(1,fromAcct);
                
                PS.setString(2,toAcct);
                
                PS.setLong(3,yearFrom);
                
                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public String getStFlag() {
        return stFlag;


    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;


    }

    public BigDecimal getSummaryRangedWithGroupPolType(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithGroupPolType(flags, fromAcct, toAcct, polType, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithGroupPolType(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id "
                    + " left join ins_pol_types c on c.vx = substr(b.accountno,11,2) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (polType != null) {
                sqa.addClause("c.ins_policy_type_grp_id = ?");
                sqa.addPar(polType);


            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedOnePeriod(
            String flags,
            String fromAcct, String toAcct, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoOnePeriod(flags, fromAcct, toAcct, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoOnePeriod(
            String flags,
            String fromAcct, String toAcct, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String fld = "bal";

            if (getDB) {
                fld = "db";

            }

            if (getCR) {
                fld = "cr";

            }

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");
            }

//            if (branch != null) {
//                if (region != null) {
//                    if (level.equalsIgnoreCase("1")) {
//                        sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                        sqa.addPar(region);
//                    }
//                    if (level.equalsIgnoreCase("2")) {
//                        sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                        sqa.addPar(branch);
//                    }
//                } else {
//                    sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
//                    sqa.addPar(branch);
//                }
//            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");
            }

            DTOList gll;

            String sql = sqa.getSQL();

            if (getBranch() != null) {
                if (getRegion() != null) {
                    sql = sqa.getSQL();
                    if (level.equalsIgnoreCase("1")) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(region);
                    }
                    if (level.equalsIgnoreCase("2")) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(branch);
                    }
                } else {
                    String subcode[] = getCostCenter().getStSubCode().split("[\\|]");
                    if (subcode.length == 1) {
                        sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                        sqa.addPar(branch);
                    } else if (subcode.length > 1) {
                        sql = sql + " and (substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[0] + "'";
                        for (int k = 1; k < subcode.length; k++) {
                            sql = sql + " or substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = '" + subcode[k] + "'";
                        }
                        sql = sql + ")";
                    }
                }
            }


            if (getDetail) {

                gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sql);

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedWithGroupPolTypeOnePeriod(
            String flags,
            String fromAcct, String toAcct,
            String polType, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithGroupPolTypeOnePeriod(flags, fromAcct, toAcct, polType, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithGroupPolTypeOnePeriod(
            String flags,
            String fromAcct, String toAcct,
            String polType, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id "
                    + " left join ins_pol_types c on c.vx = substr(b.accountno,11,2) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (polType != null) {
                sqa.addClause("c.ins_policy_type_grp_id = ?");
                sqa.addPar(polType);


            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public Date getAppDateFrom() {
        return appDateFrom;


    }

    public void setAppDateFrom(Date appDateFrom) {
        this.appDateFrom = appDateFrom;


    }

    public Date getAppDateTo() {
        return appDateTo;


    }

    public void setAppDateTo(Date appDateTo) {
        this.appDateTo = appDateTo;


    }

    public String getStLang() {
        return stLang;


    }

    public void setStLang(String stLang) {
        this.stLang = stLang;


    }

    public BigDecimal getSummaryRangedSyariah(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoSyariah(flags, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoSyariah(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }


            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery("      from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is not null");
            sqa.addClause("a.idr_flag = 'Y'");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedExcludedSyariah(
            String flags,
            String fromAcct, String toAcct,
            String excAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithExcludedSyariah(flags, fromAcct, toAcct, excAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithExcludedSyariah(
            String flags,
            String fromAcct, String toAcct,
            String excAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");

            String excAccount[] = excAcct.split("[\\|]");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");

            sqa.addClause("b.acctype is not null");
            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (excAccount != null) {
                for (int i = 0; i
                        < excAccount.length; i++) {
                    m = excAccount[i].length();
                    sqa.addClause("((substr(b.accountno,1," + m + ")) not like ?)");
                    sqa.addPar(excAccount[i]);


                }
            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public DTOList getGLAcctPremi(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String func = null;

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" date_trunc('day',a.applydate) as applydate,sum(a.debit) as debit,sum(a.credit) as credit,"
                    + "substr(b.accountno,1,5)||'0000000'||substr(b.accountno,13,3) as accountno,a.trx_no,d.value_string as description,c.details_size as pol_id ");

            sqa.addQuery(" from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid "
                    + " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint "
                    + " left join gl_accounts_header d on substr(d.acc_hdr_id,9,5) = substr(b.accountno,1,5) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) not between ? and ?) and (a.fiscal_year between ? and ?) and a.period_no = ?");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));
            sqa.addPar(String.valueOf(periodTo));



            if (isClAccDet()) {
                sqa.addClause("a.summary_flag is not null");


            }

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) = ?");
                sqa.addPar(appDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ?");
                sqa.addPar(branch);


            }

            if (stAccountNo != null) {
                sqa.addClause("a.hdr_accountno = ? ");
                sqa.addPar(stAccountNo);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.flag = 'Y'");


            }

            DTOList gll;

            String sq = " select * from ( " + sqa.getSQL() + " group by date_trunc('day',a.applydate),a.trx_no,d.value_string,c.details_size,substr(b.accountno,1,5),substr(b.accountno,13,3) ) a "
                    + " where debit <> credit order by trx_no,accountno";

            gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), JournalView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedTerms(
            String flags, String terms,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoTerms(flags, terms, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoTerms(
            String flags, String terms,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*
        
        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }


            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?) and b.terms = ?");


            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));
            sqa.addPar(terms);
            sqa.addClause("b.acctype is null");



            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                /*if (skipZero) {
                sq = "select * from ("+sq+") x where bx<>0";
                }*/

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");
                
                PS.setString(1,fromAcct);
                
                PS.setString(2,toAcct);
                
                PS.setLong(3,yearFrom);
                
                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getCashBankSummaryAcctBal(
            String flags, String fromAcct,
            long periodFrom, long periodTo,
            long yearFrom) throws Exception {

        DTOList gll = getCashBankPeriodAcctBal(flags, fromAcct, periodFrom, periodTo, yearFrom);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getCashBankPeriodAcctBal(
            String flags, String fromAcct,
            long periodFrom, long periodTo,
            long yearFrom)
            throws Exception {

        //if (fromAcct.length()<1) return null;

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            //int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(func + "(" + plist + ") as bx ");

            sqa.addQuery("from gl_acct_bal2 a  "
                    + "left join gl_accounts b on b.account_id = a.account_id");

            sqa.addClause("a.period_year = ? ");
            sqa.addPar(String.valueOf(yearFrom));

            sqa.addClause("b.acctype is null");



            if (stAccountNo != null) {
                sqa.addClause("b.accountno = ?");
                sqa.addPar(stAccountNo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            sqa.addClause("substr(b.accountno,1,5) = ? ");
            sqa.addPar(String.valueOf(fromAcct));



            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public DTOList getCashBankGLJeDetails(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, String lSort)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String func = null;
            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" a.*,b.accountno");

            sqa.addQuery(" from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) not between ? and ?) and a.fiscal_year = ? and (a.period_no between ? and ?)");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(periodFrom));
            sqa.addPar(String.valueOf(periodTo));

            if (isClAccDet()) {
                sqa.addClause("a.summary_flag is null");


            }

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) >= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (stAccountNo != null) {
                sqa.addClause("a.hdr_accountno = ? ");
                sqa.addPar(stAccountNo);


            }

            //sqa.addOrder(" a.applydate,a.trx_no,a.trx_id ");

            DTOList gll;

            String sq = sqa.getSQL() + " order by " + lSort;

            gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), JournalView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public DTOList getCashBankGLJeDetailsRekap(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, String lSort)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String func = null;
            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" date_trunc('day',a.applydate) as applydate,sum(a.debit) as debit,sum(a.credit) as credit,"
                    + "substr(b.accountno,1,5)||'0000000'||substr(b.accountno,13,3) as accountno,a.trx_no,d.value_string as description,c.details_size as pol_id ");

            sqa.addQuery(" from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid "
                    + " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint "
                    + " left join gl_accounts_header d on substr(d.acc_hdr_id,9,5) = substr(b.accountno,1,5) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) not between ? and ?) and a.fiscal_year = ? and (a.period_no between ? and ?)");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(periodFrom));
            sqa.addPar(String.valueOf(periodTo));



            if (isClAccDet()) {
                sqa.addClause("a.summary_flag is not null");


            }

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) >= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateTo);


            }

            if (entryDateFrom != null) {
                sqa.addClause("date_trunc('day',a.create_date) >= ?");
                sqa.addPar(entryDateFrom);


            }

            if (entryDateTo != null) {
                sqa.addClause("date_trunc('day',a.create_date) <= ?");
                sqa.addPar(entryDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (stAccountNo != null) {
                sqa.addClause("a.hdr_accountno = ? ");
                sqa.addPar(stAccountNo);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.flag = 'Y'");


            }

            DTOList gll;

            String sql = " select * from ( " + sqa.getSQL() + " group by date_trunc('day',a.applydate),a.trx_no,d.value_string,c.details_size,substr(b.accountno,1,5),substr(b.accountno,13,3) ) a "
                    + " where debit <> credit order by " + lSort;

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public Date getEntryDateFrom() {
        return entryDateFrom;


    }

    public void setEntryDateFrom(Date entryDateFrom) {
        this.entryDateFrom = entryDateFrom;


    }

    public Date getEntryDateTo() {
        return entryDateTo;


    }

    public void setEntryDateTo(Date entryDateTo) {
        this.entryDateTo = entryDateTo;


    }

    public DTOList getCashBankGLJeDetailsRekap2(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, String lSort)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String func = null;

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" date_trunc('day',a.applydate) as applydate,sum(a.debit) as debit,sum(a.credit) as credit,"
                    + "substr(b.accountno,1,5)||'0000000'||substr(b.accountno,13,3) as accountno,a.trx_no,d.value_string as description,c.details_size as pol_id ");

            sqa.addQuery(" from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid "
                    + " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint "
                    + " left join gl_accounts_header d on substr(d.acc_hdr_id,9,5) = substr(b.accountno,1,5) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) not between ? and ?) and a.fiscal_year = ? and (a.period_no between ? and ?)");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(periodFrom));
            sqa.addPar(String.valueOf(periodTo));



            if (isClAccDet()) {
                sqa.addClause("a.summary_flag is not null");


            }

            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) >= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateTo);


            }

            if (entryDateFrom != null) {
                sqa.addClause("date_trunc('day',a.create_date) >= ?");
                sqa.addPar(entryDateFrom);


            }

            if (entryDateTo != null) {
                sqa.addClause("date_trunc('day',a.create_date) <= ?");
                sqa.addPar(entryDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (stAccountNo != null) {
                sqa.addClause("a.hdr_accountno = ? ");
                sqa.addPar(stAccountNo);


            }

            DTOList gll;

            String sql = " select * from ( "
                    + " select * from ( " + sqa.getSQL() + " group by date_trunc('day',a.applydate),a.trx_no,"
                    + " d.value_string,c.details_size,substr(b.accountno,1,5),substr(b.accountno,13,3) ) a "
                    + " where debit <> credit "
                    + " union all "
                    + " select a.applydate,a.debit,a.credit,b.accountno,a.trx_no,a.description,a.pol_id "
                    + " from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid ";

            sql = sql + " and ((substr(b.accountno,1," + l + ")) not between ? and ?) and a.fiscal_year = ? and (a.period_no between ? and ?)";

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(periodFrom));
            sqa.addPar(String.valueOf(periodTo));



            if (isClAccDet()) {
                sql = sql + " and a.summary_flag is null ";


            }

            if (getAppDateFrom() != null) {
                sql = sql + " and date_trunc('day',a.applydate) >= ? ";
                sqa.addPar(appDateFrom);


            }

            if (getAppDateTo() != null) {
                sql = sql + " and date_trunc('day',a.applydate) <= ? ";
                sqa.addPar(appDateTo);


            }

            if (getEntryDateFrom() != null) {
                sql = sql + " and date_trunc('day',a.create_date) >= ? ";
                sqa.addPar(entryDateFrom);


            }

            if (getEntryDateTo() != null) {
                sql = sql + " and date_trunc('day',a.create_date) <= ? ";
                sqa.addPar(entryDateTo);


            }

            if (getBranch() != null) {
                sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=? ";
                sqa.addPar(branch);


            }

            if (getStAccountNo() != null) {
                sql = sql + " and a.hdr_accountno = ? ";
                sqa.addPar(stAccountNo);


            }

            sql = sql + " ) a order by " + lSort;

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedNew(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, Date applyDate) throws Exception {

        DTOList gll = getGLInfoNew(flags, fromAcct, toAcct, periodFrom, periodTo, yearFrom, applyDate);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoNew(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, Date applyDate)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            //if (getBalance) periodFrom = 0;

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }


            String plist = GLUtil.getPeriodList(0, periodFrom, fld, "+");

            String func = null;

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            if (getMutation) {
                func = "sum";
            }

            if (getBalance) {
                func = "sum";
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" coalesce(" + func + "(" + plist + "),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and a.period_year = ? ");
            sqa.addClause("b.acctype is null");
            sqa.addClause("a.idr_flag = 'Y'");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));



            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            String sql = "select sum(bx) as bx from ( " + sqa.getSQL() + " union all "
                    + "select sum(a.debit-a.credit) as bx "
                    + "from " + gljedetail + " a "
                    + "inner join gl_accounts b on b.account_id = a.accountid "
                    + "where a.flag = 'Y' and b.acctype is null";

            sql = sql + " and ((substr(b.accountno,1," + l + ")) between ? and ?) and a.fiscal_year = ? ";
            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));

            sql = sql + " and date_trunc('day',a.applydate) >= ?";
            sqa.addPar(DateUtil.getYear(applyDate) + "-" + DateUtil.getMonth2Digit(applyDate) + "-01");

            sql = sql + " and date_trunc('day',a.applydate) <= ?";
            sqa.addPar(applyDate);



            if (getBranch() != null) {
                sql = sql + " and substr(b.accountno," + (branchPos + 1) + "," + branchLen + ") = ? ";
                sqa.addPar(branch);


            }

            sql = sql + " ) a ";

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sql);

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedMutation(
            String flags,
            String fromAcct, String toAcct) throws Exception {

        DTOList gll = getGLInfoMutation(flags, fromAcct, toAcct);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoMutation(
            String flags,
            String fromAcct, String toAcct)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String func = null;

            String gljedetail = null;
            String gljedetailYear = DateUtil.getYear(appDateFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            if (getMutation) {
                func = "sum";
            }

            if (getBalance) {
                func = "sum";
            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(SUM(A.DEBIT-A.CREDIT),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join " + gljedetail + " a on b.account_id = a.accountid ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?)");
            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);



            if (appDateFrom != null) {
                sqa.addClause("date_trunc('day',a.applydate) >= ?");
                sqa.addPar(appDateFrom);


            }

            if (appDateTo != null) {
                sqa.addClause("date_trunc('day',a.applydate) <= ?");
                sqa.addPar(appDateTo);


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedOnePeriodSyariah(
            String flags, String terms,
            String fromAcct, String toAcct, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoOnePeriodSyariah(flags, terms, fromAcct, toAcct, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoOnePeriodSyariah(
            String flags, String terms,
            String fromAcct, String toAcct, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");

            sqa.addClause("b.acctype is not null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (terms != null) {
                sqa.addClause("b.terms = ? ");
                sqa.addPar(terms);


            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedWithPolType(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithPolType(flags, fromAcct, toAcct, polType, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithPolType(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id "
                    + " left join ins_pol_types c on c.vx = substr(b.accountno,11,2) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (polType != null) {
                sqa.addClause("c.pol_type_id = ?");
                sqa.addPar(polType);


            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedWithPolTypeOnePeriod(
            String flags,
            String fromAcct, String toAcct,
            String polType, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoWithPolTypeOnePeriod(flags, fromAcct, toAcct, polType, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoWithPolTypeOnePeriod(
            String flags,
            String fromAcct, String toAcct,
            String polType, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            }

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();


            int m = 0;

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(coalesce(bal" + periodTo + ",0)),0) as bx ");

            sqa.addQuery(" from gl_accounts b "
                    + " left join gl_acct_bal2 a on b.account_id=a.account_id "
                    + " left join ins_pol_types c on c.vx = substr(b.accountno,11,2) ");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));



            if (polType != null) {
                sqa.addClause("c.pol_type_id = ?");
                sqa.addPar(polType);


            }

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getProduksi(
            String bulan, String tahun) throws Exception {

        DTOList gll = getProduksiInfo(bulan, tahun);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getProduksiInfo(
            String bulan, String tahun)
            throws Exception {

        SQLUtil S = new SQLUtil();



        try {

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) as premi,"
                    + "sum(getpremiend(d.entity_id,((coalesce(a.nd_disc1,0)*a.ccy_rate) + (coalesce(a.nd_disc2,0)*a.ccy_rate)),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

            sqa.addQuery(" from ins_policy a "
                    + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

            sqa.addClause("a.status in ('POLICY','RENEWAL','ENDORSE')");
            sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");



            if (branch != null) {
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(branch);


            }

            if (region != null) {
                sqa.addClause("a.region_id = ?");
                sqa.addPar(region);


            }

            if (bulan != null) {
                sqa.addClause("substr(a.policy_date::text,6,2) = ?");
                sqa.addPar(bulan);


            }

            if (tahun != null) {
                sqa.addClause("substr(a.policy_date::text,1,4) = ?");
                sqa.addPar(tahun);


            }

            DTOList gll;

            String sql = " select sum(premi-diskon) as bx from ( "
                    + sqa.getSQL() + " ) a ";

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    /**
     * @return the bulan
     */
    public String getBulan() {
        return bulan;


    }

    /**
     * @param bulan the bulan to set
     */
    public void setBulan(String bulan) {
        this.bulan = bulan;


    }

    /**
     * @return the tahun
     */
    public String getTahun() {
        return tahun;


    }

    /**
     * @param tahun the tahun to set
     */
    public void setTahun(String tahun) {
        this.tahun = tahun;


    }

    public BigDecimal getProduksiTriwulan(
            String bulan1, String bulan2, String tahun) throws Exception {

        DTOList gll = getProduksiTriwulanInfo(bulan1, bulan2, tahun);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getProduksiTriwulanInfo(
            String bulan1, String bulan2, String tahun)
            throws Exception {

        SQLUtil S = new SQLUtil();



        try {

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("sum(getpremiend(d.entity_id,a.premi_total*a.ccy_rate,d.premi_amt*a.ccy_rate*-1)) as premi,"
                    + "sum(getpremiend(d.entity_id,((coalesce(a.nd_disc1,0)*a.ccy_rate) + (coalesce(a.nd_disc2,0)*a.ccy_rate)),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

            sqa.addQuery(" from ins_policy a "
                    + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

            sqa.addClause("a.status in ('POLICY','RENEWAL','ENDORSE')");
            sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
            sqa.addClause("a.active_flag = 'Y'");
            sqa.addClause("a.effective_flag = 'Y'");



            if (branch != null) {
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(branch);


            }

            if (region != null) {
                sqa.addClause("a.region_id = ?");
                sqa.addPar(region);


            }

            if (bulan1 != null) {
                sqa.addClause("substr(a.policy_date::text,6,2) between ? and ?");
                sqa.addPar(bulan1);
                sqa.addPar(bulan2);


            }

            if (tahun != null) {
                sqa.addClause("substr(a.policy_date::text,1,4) = ?");
                sqa.addPar(tahun);


            }

            DTOList gll;

            String sql = " select sum(premi-diskon) as bx from ( "
                    + sqa.getSQL() + " ) a ";

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), GLInfoView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    public DTOList getProduksiTriwulanInfo2(
            String bulan1, String bulan2)
            throws Exception {

        SQLUtil S = new SQLUtil();



        try {

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect(" sum(a.prod_goal_setting) as debit,sum(a.prod_rkap) as credit ");

            sqa.addQuery(" from s_goal_setting_rkap a ");



            if (branch != null) {
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(branch);


            }

            if (region != null) {
                sqa.addClause("a.region_id = ?");
                sqa.addPar(region);


            }

            if (bulan1 != null) {
                sqa.addClause("a.months between ? and ?");
                sqa.addPar(bulan1);
                sqa.addPar(bulan2);


            }

            if (tahun != null) {
                sqa.addClause("a.years = ?");
                sqa.addPar(tahun);


            }

            DTOList gll;

            String sql = sqa.getSQL();

            gll = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);





            return gll;

        } finally {

            S.release();



        }
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;


    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;


    }

    public BigDecimal getSummaryRangedBranch(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoBranch(flags, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoBranch(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;
        }

        SQLUtil S = new SQLUtil();

        /*

        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */

        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");
            boolean getMutation = pmap.containsKey("MUT");
            boolean getBalance = pmap.containsKey("BAL");
            boolean getDB = pmap.containsKey("DB");
            boolean getCR = pmap.containsKey("CR");
            boolean getDetail = pmap.containsKey("DET");
            boolean skipZero = pmap.containsKey("SKIPZERO");
            boolean negated = pmap.containsKey("NEG");
            String colSet = (String) pmap.get("COLSET");

            if ("default".equals(colSet)) {
                colSet = null;
            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');
            /*
            if (getBalance) {
            periodFrom = 0;
            }
             */

            if (periodFrom == 1) {
                periodFrom = 0;
            }

            String fld = "bal";

            if (getDB) {
                fld = "db";
            }

            if (getCR) {
                fld = "cr";
            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");
            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);
            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                /*if (skipZero) {
                sq = "select * from ("+sq+") x where bx<>0";
                }*/

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");

                PS.setString(1,fromAcct);

                PS.setString(2,toAcct);

                PS.setLong(3,yearFrom);

                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    public BigDecimal getSummaryRangedTermsSyariah(
            String flags, String terms,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        DTOList gll = getGLInfoTermsSyariah(flags, terms, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo);



        if (gll == null) {
            return null;


        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");


        }

        GLInfoView gli = (GLInfoView) gll.get(0);



        return gli.getDbAmount();


    }

    ;

    public DTOList getGLInfoTermsSyariah(
            String flags, String terms,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;


        }

        SQLUtil S = new SQLUtil();

        /*

        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */



        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");



            boolean getMutation = pmap.containsKey("MUT");



            boolean getBalance = pmap.containsKey("BAL");


            boolean getDB = pmap.containsKey("DB");


            boolean getCR = pmap.containsKey("CR");



            boolean getDetail = pmap.containsKey("DET");



            boolean skipZero = pmap.containsKey("SKIPZERO");



            boolean negated = pmap.containsKey("NEG");

            String colSet = (String) pmap.get("COLSET");



            if ("default".equals(colSet)) {
                colSet = null;


            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');



            if (getBalance) {
                periodFrom = 0;


            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";



            if (getDB) {
                fld = "db";


            }

            if (getCR) {
                fld = "cr";


            }


            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?) and b.terms = ?");


            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));
            sqa.addPar(terms);
            sqa.addClause("b.acctype is not null");



            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");


            }

            if (branch != null) {
                sqa.addClause("substr(b.accountno," + (branchPos + 1) + "," + branchLen + ")=?");
                sqa.addPar(branch);


            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                /*if (skipZero) {
                sq = "select * from ("+sq+") x where bx<>0";
                }*/

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");

                PS.setString(1,fromAcct);

                PS.setString(2,toAcct);

                PS.setLong(3,yearFrom);

                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;


    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;


    }

    public GLCostCenterView3 getCostCenter() {
        final GLCostCenterView3 costcenter = (GLCostCenterView3) DTOPool.getInstance().getDTO(GLCostCenterView3.class, branch);

        return costcenter;
    }

    public String getYearPosting() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(" select years from gl_posting where final_flag = 'Y' "
                    + "order by gl_post_id desc limit 1 ");

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getSummaryPolTypeHasilUW(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, String sumbis) throws Exception {

        DTOList gll = getGLInfoWithGroupPolTypeHasilUW(flags, fromAcct, toAcct, polType, periodFrom, periodTo, yearFrom, sumbis);

        if (gll == null) {
            return null;
        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");
        }

        GLInfoView gli = (GLInfoView) gll.get(0);

        return gli.getDbAmount();
    }

    ;

    public DTOList getGLInfoWithGroupPolTypeHasilUW(
            String flags,
            String fromAcct, String toAcct,
            String polType,
            long periodFrom, long periodTo,
            long yearFrom, String sumbis)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;
        }

        SQLUtil S = new SQLUtil();

        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");
            boolean getMutation = pmap.containsKey("MUT");
            boolean getBalance = pmap.containsKey("BAL");
            boolean getDB = pmap.containsKey("DB");
            boolean getCR = pmap.containsKey("CR");
            boolean getDetail = pmap.containsKey("DET");
            boolean skipZero = pmap.containsKey("SKIPZERO");
            boolean negated = pmap.containsKey("NEG");
            String colSet = (String) pmap.get("COLSET");

            if ("default".equals(colSet)) {
                colSet = null;
            }

            int branchPos = GLUtil.getMaskPosition('B');
            int branchLen = GLUtil.getMaskLength('B');

            if (getBalance) {
                periodFrom = 0;
            } /*
            if (periodFrom == 1) {
            periodFrom = 0;
            }
             */

            String fld = "bal";
            if (getDB) {
                fld = "db";
            }

            if (getCR) {
                fld = "cr";
            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            String gljedetail = null;
            String gljedetailYear = Long.toString(yearFrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }

            int l = fromAcct.length();

            int m = 0;

//            SQLAssembler sqa = new SQLAssembler();
//
//            sqa.addSelect(" coalesce(sum(debit-credit),0) as bx ");
//
//            sqa.addQuery(" from " + gljedetail + " a "
//                    + "left join gl_accounts b on b.account_id = a.accountid ");
//
//            String clauseAkun = null;
//            if (toAcct != null) {
//                clauseAkun = " in ('" + fromAcct + "','" + toAcct + "')";
//            } else {
//                clauseAkun = " = '" + fromAcct+"'";
//            }
//
//            sqa.addClause("substr(b.accountno,1," + l + ") " + clauseAkun);
//            sqa.addClause("a.fiscal_year = ? ");
//            sqa.addClause("a.period_no between ? and ? ");
//            sqa.addClause("b.acctype is null ");
//
//            sqa.addPar(String.valueOf(yearFrom));
//            sqa.addPar(String.valueOf(periodFrom));
//            sqa.addPar(String.valueOf(periodTo));
//
//            if (polType != null) {
//                sqa.addClause("substr(b.accountno,11,2) = ? ");
//                sqa.addPar(polType);
//            }
//
//            if (sumbis != null) {
//                sqa.addClause("substr(a.pol_no,2,1) = ? ");
//                sqa.addPar(sumbis);
//            }else{
//                sqa.addClause("((substr(a.pol_no,2,1) is null) or (substr(a.pol_no,2,1) not in ('1','2','3','4')))");
//            }
//
//            DTOList gll;

            SQLAssembler sqa = new SQLAssembler();

            String clauseAkun = null;
            if (toAcct != null) {
                clauseAkun = " in ('" + fromAcct + "','" + toAcct + "')";
            } else {
                clauseAkun = " = '" + fromAcct + "'";
            }

            sqa.addSelect(" coalesce(sum(bx),0) as bx ");

            String sql = " select substr(a.pol_no,2,1) as sumbis,coalesce(sum(debit-credit),0) as bx "
                    + " from " + gljedetail + " a "
                    + " left join gl_accounts b on b.account_id = a.accountid "
                    + " where b.acctype is null "
                    + " and a.fiscal_year = '" + String.valueOf(yearFrom) + "' "
                    + " and a.period_no between " + String.valueOf(periodFrom) + " and " + String.valueOf(periodTo)
                    + " and substr(b.accountno,1," + l + ") " + clauseAkun;

            if (polType != null) {
                sql = sql + "and substr(b.accountno,11,2) = '" + polType + "'";
                //sqa.addClause("substr(b.accountno,11,2) = ? ");
                //sqa.addPar(polType);
            }

            sql = sql + " group by 1 ";

            sqa.addQuery(" from (" + sql + ") a ");

            if (sumbis != null) {
                sqa.addClause("a.sumbis = '" + sumbis + "'");
            } else {
                sqa.addClause("(sumbis is null or sumbis not in ('1','2','3','4'))");
            }

            DTOList gll;

            if (getDetail) {

                String sq = sqa.getSQL();

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);
            }

            BigDecimal tot = null;

            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();

                if (negated) {
                    bd = BDUtil.negate(bd);
                }

                tot = BDUtil.add(tot, bd);
            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");

                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];

                    if (colSet != null) {
                        v += "/" + colSet;
                    }

                    BigDecimal x = (BigDecimal) vars.get(v);

                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);
                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);
                }
            }

            return gll;

        } finally {

            S.release();

        }
    }

    public BigDecimal distribusiBiayaPBNasional(
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        if (fromAcct.length() < 1) {
            return null;
        }

        final SQLUtil S = new SQLUtil();

        try {

            String fld = "bal";
            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");
            String func = "sum";
            int l = fromAcct.length();

            S.setQuery(
                    " select coalesce(" + func + "(" + plist + "),0) as bx "
                    + "from gl_accounts b "
                    + "left join gl_acct_bal2 a on b.account_id=a.account_id "
                    + "where ((substr(b.accountno,1," + l + ")) between ? and ?) "
                    + "and (a.period_year between ? and ?) and b.acctype is null and a.idr_flag = 'Y'");

            S.setParam(1, fromAcct);
            S.setParam(2, toAcct);
            S.setParam(3, String.valueOf(yearFrom));
            S.setParam(4, String.valueOf(yearTo));

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal distribusiBiayaLB00(
            long periodFrom, long periodTo,
            long yearFrom, long yearTo) throws Exception {

        long lPeriodFrom = periodFrom;
        long lPeriodTo = periodTo;
        long lYearFrom = yearFrom;
        long lYearTo = yearTo;

//        GLReportEngine2 glr = new GLReportEngine2();
//        glr.setBranch("00");

        setBranch("00");

        BigDecimal premi_bruto = BDUtil.roundUp(getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal premi_reas = BDUtil.roundUp(getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal premi_kenaikan = BDUtil.roundUp(getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal klaim_reas = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal kenaikan_klaim = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal beban_und_lain = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = BDUtil.roundUp(getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal beban_usaha = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

        BigDecimal pajakPenghasilan = BDUtil.roundUp(getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
        BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(BDUtil.roundUp(hasilUnderWriting), BDUtil.roundUp(investasi));
        laba_usaha = BDUtil.add(laba_usaha, BDUtil.roundUp(beban_usaha));

        BigDecimal penghasilanBeban69 = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal penghasilanBeban89 = BDUtil.roundUp(getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));
        BigDecimal acc_laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        acc_laba_bersih = BDUtil.add(acc_laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan));

        BigDecimal laba_bersih = BDUtil.roundUp(getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearTo));
        BigDecimal selisih_nilai = BDUtil.sub(BDUtil.negate(laba_bersih), acc_laba_bersih);
        BigDecimal labaSebelumPajakFix = BDUtil.sub(labaSebelumPajak, BDUtil.negate(selisih_nilai));

        return labaSebelumPajakFix;
    }

    public BigDecimal getSummaryRangedBranch2(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo, String stBranch) throws Exception {

        DTOList gll = getGLInfoBranch2(flags, fromAcct, toAcct, periodFrom, periodTo, yearFrom, yearTo, stBranch);

        if (gll == null) {
            return null;
        }

        if (gll.size() != 1) {
            throw new RuntimeException("getGLInfo returning " + gll.size() + " result, should be only one !");
        }

        GLInfoView gli = (GLInfoView) gll.get(0);

        return gli.getDbAmount();
    }

    ;

    public DTOList getGLInfoBranch2(
            String flags,
            String fromAcct, String toAcct,
            long periodFrom, long periodTo,
            long yearFrom, long yearTo, String stBranch)
            throws Exception {

        if (fromAcct.length() < 1) {
            return null;
        }

        SQLUtil S = new SQLUtil();

        /*

        select * from
        gl_acct_bal2 a
        inner join gl_accounts b on b.account_id=a.account_id
        where (substr(b.accountno,1,7)) between 1000000 and 1112301
         */

        try {

            PropertyMap pmap = PropertyMap.getInstance(flags, "|", "=");
            boolean getMutation = pmap.containsKey("MUT");
            boolean getBalance = pmap.containsKey("BAL");
            boolean getDB = pmap.containsKey("DB");
            boolean getCR = pmap.containsKey("CR");
            boolean getDetail = pmap.containsKey("DET");
            boolean skipZero = pmap.containsKey("SKIPZERO");
            boolean negated = pmap.containsKey("NEG");
            String colSet = (String) pmap.get("COLSET");

            if ("default".equals(colSet)) {
                colSet = null;
            } //if (colSet==null) colSet="DEFAULT";

            int branchPos = GLUtil.getMaskPosition('B');


            int branchLen = GLUtil.getMaskLength('B');

            if (getBalance) {
                periodFrom = 0;
            }

//            if (periodFrom == 1) {
//                periodFrom = 0;
//            }

            String fld = "bal";

            if (getDB) {
                fld = "db";
            }

            if (getCR) {
                fld = "cr";
            }

            String plist = GLUtil.getPeriodList(periodFrom, periodTo, fld, "+");

            String func = "sum";

//            if (getMutation) {
//                func = "sum";
//            }
//
//            if (getBalance) {
//                func = "sum";
//            }

            int l = fromAcct.length();

            SQLAssembler sqa = new SQLAssembler();

            sqa.addSelect("coalesce(" + func + "(" + plist + "),0) as bx ");
            /*sqa.addQuery("      from \n" +
            "         gl_acct_bal2 a \n" +
            "            inner join gl_accounts b on b.account_id=a.account_id\n"
            );*/

            sqa.addQuery("      from \n"
                    + "         gl_accounts b \n"
                    + "            left join gl_acct_bal2 a on b.account_id=a.account_id\n");

            sqa.addClause("((substr(b.accountno,1," + l + ")) between ? and ?) and (a.period_year between ? and ?)");
            sqa.addClause("b.acctype is null");

            sqa.addPar(fromAcct);
            sqa.addPar(toAcct);
            sqa.addPar(String.valueOf(yearFrom));
            sqa.addPar(String.valueOf(yearTo));

            if (getDetail) {
                sqa.addSelect("b.accountno as account_no");
                sqa.addSelect("b.description as account_name");
                sqa.addGroup("b.accountno");
                sqa.addGroup("b.description");
            }

            if (stBranch != null) {
                sqa.addClause("substr(b.accountno,14,2) = ? ");
                sqa.addPar(stBranch);
            }

            if (Tools.isYes(stFlag)) {
                sqa.addClause("a.idr_flag = 'Y'");


            }

            DTOList gll;



            if (getDetail) {

                String sq = sqa.getSQL();

                /*if (skipZero) {
                sq = "select * from ("+sq+") x where bx<>0";
                }*/

                gll = ListUtil.getDTOListFromQuery(sq, sqa.getPar(), GLInfoView.class);

            } else {
                PreparedStatement PS = S.setQuery(sqa.getSQL());

                S.setParams(sqa.getPar());

                /*PreparedStatement PS = S.setQuery(
                "   select "+func+"("+plist+") as bx " +
                "      from \n" +
                "         gl_acct_bal2 a \n" +
                "            inner join gl_accounts b on b.account_id=a.account_id\n" +
                "      where ((substr(b.accountno,1,"+l+")) between ? and ?) and (a.period_year between ? and ?)");

                PS.setString(1,fromAcct);

                PS.setString(2,toAcct);

                PS.setLong(3,yearFrom);

                PS.setLong(4,yearTo);*/

                ResultSet RS = PS.executeQuery();

                RS.next();

                BigDecimal bd = RS.getBigDecimal("bx");

                GLInfoView gli = new GLInfoView();

                gli.setDbAmount(bd);

                gll = new DTOList();

                gll.add(gli);


            }

            BigDecimal tot = null;



            for (int i = 0; i
                    < gll.size(); i++) {
                GLInfoView gli = (GLInfoView) gll.get(i);

                BigDecimal bd = gli.getDbAmount();



                if (negated) {
                    bd = BDUtil.negate(bd);


                }

                tot = BDUtil.add(tot, bd);


            }

            if (pmap.containsKey("ADD")) {
                String vn = (String) pmap.get("ADD");

                String[] varz = vn.split(",");



                for (int j = 0; j
                        < varz.length; j++) {
                    String v = varz[j];



                    if (colSet != null) {
                        v += "/" + colSet;


                    }

                    BigDecimal x = (BigDecimal) vars.get(v);



                    for (int i = 0; i
                            < gll.size(); i++) {
                        GLInfoView gli = (GLInfoView) gll.get(i);


                    }

                    x = BDUtil.add(x, tot);

                    vars.put(v, x);


                }
            }

            return gll;



        } finally {

            S.release();



        }
    }
}
