package com.webfin.gl.ejb;

import com.crux.util.*;
import com.crux.common.model.DTO;
import com.crux.common.model.HashDTO;
import com.crux.common.model.UserSession;
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.model.*;
import com.webfin.gl.accounts.filter.AccountFilter;
import com.webfin.ar.model.ARReceiptClassView;import com.webfin.gl.util.GLUtil;

import com.webfin.ar.model.ARPaymentMethodView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARInvestmentBungaView;
import com.webfin.ar.model.ARInvestmentDepositoIndexView;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentIzinBungaView;
import com.webfin.ar.model.ARInvestmentIzinDepositoDetailView;
import com.webfin.ar.model.ARInvestmentIzinDepositoView;
import com.webfin.ar.model.ARInvestmentIzinPencairanDetView;
import com.webfin.ar.model.ARInvestmentIzinPencairanView;
import com.webfin.ar.model.ARInvestmentPencairanView;
import com.webfin.ar.model.ARInvestmentPerpanjanganView;
import com.webfin.ar.model.ARRequestApprovalView;
import com.webfin.ar.model.ARRequestDocumentsView;
import com.webfin.ar.model.ARRequestFee;
import com.webfin.ar.model.ARRequestFeeObj;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.ar.model.BiayaPemasaranDetailView;
import com.webfin.ar.model.BiayaPemasaranDocumentsView;
import com.webfin.ar.model.BiayaPemasaranView;
import com.webfin.insurance.model.InsuranceClausulesView;
import com.webfin.insurance.model.InsuranceClosingReportView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicySOAView;
import com.webfin.insurance.model.InsurancePostingView;
import com.webfin.insurance.model.uploadProposalCommView;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Jul 18, 2005
 * Time: 5:43:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralLedgerEJB implements SessionBean {

    private final static transient LogManager logger = LogManager.getInstance(GeneralLedgerEJB.class);
    private SessionContext ctx;

    public GeneralLedgerEJB() {
    }

    public LOV getCurrencyCodeLOV() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select ccy_code, description from gl_currency",
                GLCurrencyView.class);
    }

    public LOV getCostCenterCodeLOV() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select cc_code, description from gl_cost_center order by cc_code",
                GLCostCenterView.class);
    }

    public LOV getMethodCodeLOV() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select method_code,description from receipt_class",
                ARReceiptClassView.class);
    }

    public LOV getMethodPaymentLOV() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select gl_acct_id,description from payment_method",
                ARPaymentMethodView.class);
    }

    /*
    public DTOList getMethodCodeLOV() throws Exception {
    return ListUtil.getDTOListFromQuery(
    "select rc_id,description from receipt_class",
    ARReceiptClassView.class);
    }*/
    public DTOList listAccountTypes() throws Exception {
        return ListUtil.getDTOListFromQuery("select * from gl_acct_type",
                AccountTypeView.class);
    }

    /*public DTOList searchAccounts(String key) throws Exception {
    return ListUtil.getDTOListFromQuery("select account_id, accountno, description from gl_accounts where upper(accountno || description) like ?",
    new Object [] {'%'+key.toUpperCase()+'%'},
    AccountView.class);
    }*/
    /*
    CREATE TABLE gl_chart
    (
    account_id int8 NOT NULL,
    account_no varchar(32),
    description varchar(128),
    account_type varchar(5),
    create_date timestamp NOT NULL,
    create_who varchar(32) NOT NULL,
    change_date timestamp,
    change_who varchar(32),
    CONSTRAINT gl_char_pk PRIMARY KEY (account_id)
    )
     */
    public DTOList searchAccounts(String key, String costCenter) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      coalesce(b.accountno, a.accountno) as accountno, a.description, a.acctype, b.account_id "
                + "   from "
                + "      gl_chart a"
                + "      left join gl_accounts b on b.accountno = (a.accountno || ' ' || ?)"
                + "   where "
                + "      upper(a.accountno || a.description) like ?",
                new Object[]{costCenter, '%' + key.toUpperCase() + '%'},
                AccountView.class);
    }

    public DTOList getAccountTypesCombo() throws Exception {
        return ListUtil.getDTOListFromQuery("select accttype,description from gl_acct_type order by description",
                AccountTypeView.class);
    }

    public DTOList getJournalTypesCombo() throws Exception {
        return ListUtil.getDTOListFromQuery("select journal_code,description from gl_journal_master order by description",
                JournalMasterView.class);
    }

    public DTOList listJournalMaster() throws Exception {
        return ListUtil.getDTOListFromQuery("select * from gl_journal_master",
                JournalMasterView.class);
    }

    public DTOList listGLPeriods() throws Exception {
        return ListUtil.getDTOListFromQuery("select * from gl_period",
                PeriodHeaderView.class);
    }

    public DTOList listJournalEntry() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from "
                + "      gl_je_detail a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   order "
                + "      by trx_id, applydate",
                JournalView.class);
    }

    public DTOList getJournalEntry(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      gl_je_detail a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id=? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                JournalView.class);
    }

    public void saveJournalEntry(DTOList l) throws Exception {
        //logger.logDebug("saveJournalEntry");

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();
        Long lgHeaderAccountID = fj.getLgAccountID();
        String stHeaderAccountNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        boolean notBalance = false;

        if (!reversing) {
            if (BDUtil.biggerThan(bal, BDUtil.one)) {
                notBalance = true; 
            }
            if (BDUtil.lesserThan(bal, new BigDecimal(-1))) {
                notBalance = true;
            }
 
            if(notBalance) throw new RuntimeException("Jurnal tidak balance (selisih = "+bal+")\n "+l);

            if (Tools.compare(bal, BDUtil.zero) != 0) {
                throw new RuntimeException("Jurnal tidak balance (selisih = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW")); //mark utk besih2x
        }
        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                if (j.getLgHeaderAccountID() == null) {
                    j.setLgHeaderAccountID(lgHeaderAccountID);
                    j.setStHeaderAccountNo(stHeaderAccountNo);
                }

                j.reCalculate();

                if (j.isNew()) {
                    //j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW"))); //mark utk besih2x
                   /*final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per==null) throw new Exception("Periods not being setup correctly !");
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());*/
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Pembukuan bulan");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                String month = DateUtil.getMonth2Digit(j.getDtApplyDate());
                String year = DateUtil.getYear(j.getDtApplyDate());
                // 442002137559 21
                // 0123456789012345
                String cabang = j.getAccount().getStAccountNo().substring(13);

//                if(isPosted(month, year, cabang))
//                    throw new Exception("Transaksi bulan "+ month +" Tahun "+ year +" sudah di posting");
                /*
                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
                */
            }

            S.store(l);
            S.release();
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveJournalEntryTes(DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();
        Long lgHeaderAccountID = fj.getLgAccountID();
        String stHeaderAccountNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        if (!reversing) //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);
        {
            if (stTransactionHeaderID == null) {
                stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
            }
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    /*final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per==null) throw new Exception("Periods not being setup correctly !");
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());*/
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveJournalEntry2(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();


        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        // if (!reversing)
        //  if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }




        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j.getDbDebit();
                    j.setDbDebit(j.getDbCredit());
                    j.setDbCredit(debitOld);
                    j.setStDescription(header.getStDescriptionMaster());
                    debitEnteredOld = j.getDbEnteredDebit();
                    j.setDbEnteredDebit(j.getDbEnteredCredit());
                    j.setDbEnteredCredit(debitEnteredOld);
                    //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }


    }

    public void saveJournalEntry3(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        Long lgHeaderAccountID = header.getLgAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        String tahun = header.getStYears();
        String bulan = header.getStMonths();

        //if (!reversing)
        //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
            header.setStTransactionNo(trxNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                JournalView j2 = (JournalView) l.get(i);

                AccountMarketingView j3 = new AccountMarketingView();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("CASHBANK");
                j.setStIDRFlag("Y");

                if (!Tools.isEqual(DateUtil.getMonth2Digit(j.getDtApplyDate()), bulan)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                }
                if (!Tools.isEqual(DateUtil.getYear(j.getDtApplyDate()), tahun)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                }

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                JournalView viuw = (JournalView) l.get(i);
                S.store(j);

                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionNo(trxNo);
                    j2.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j2.getDbDebit();
                    j2.setDbDebit(j2.getDbCredit());
                    j2.setDbCredit(debitOld);
                    debitEnteredOld = j2.getDbEnteredDebit();
                    j2.setDbEnteredDebit(j2.getDbEnteredCredit());
                    j2.setDbEnteredCredit(debitEnteredOld);
                    j2.setStIDRFlag("Y");
                }

                if (j2.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());


                if (!Tools.isEqual(DateUtil.getMonth2Digit(j2.getDtApplyDate()), bulan)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                }
                if (!Tools.isEqual(DateUtil.getYear(j2.getDtApplyDate()), tahun)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                }

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                JournalView viuw2 = (JournalView) l.get(i);
                S.store(j2);

                if (j2.getStAccountNo().contains("811") || j2.getStAccountNo().contains("813")) {

                    j3.markNew();

                    BigDecimal amount = new BigDecimal(0);
                    if (BDUtil.isZero(j.getDbDebit())) {
                        amount = j.getDbCredit();
                    } else if (BDUtil.isZero(j.getDbCredit())) {
                        amount = BDUtil.negate(j.getDbDebit());
                    }

                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLACCMKT")));
                        j3.setStNoBukti(trxNo);
                        j3.setStAccountID(getAccountByAccountNo(j2.getStAccountNo()).getStAccountID());
                        j3.setStAccountNo(j2.getStAccountNo());
                        j3.setStDescription(j2.getStDescription());
                        j3.setStMonths(j2.getStMonths());
                        j3.setStYears(j2.getStYears());
                        j3.setDbAmount(amount);
                        j3.setDtApplyDate(j2.getDtApplyDate());
                        if (j3.getStAccountNo().contains("811")) {
                            j3.setStSubType("PROMOSI");
                        } else if (j3.getStAccountNo().contains("813")) {
                            j3.setStSubType("MARKETING");
                        }
                    }

                    S.store(j3);
                }

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveJournalEntry4(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        String stTransactionNo = null;

        if (header.isNew()) {
            stTransactionNo = generateTransactionNo(header, l, transNo);
            header.setStTransactionNo(stTransactionNo);
        }

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        if (!reversing) {
            if (Tools.compare(bal, BDUtil.zero) != 0) {
                throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("GENERAL");
                j.setStIDRFlag("Y");

                if (j.isModified()){

                    //get jurnal awal nya
                    final DTOList jurnalHistory = getJournalEntryByTrxNo(j.getStTransactionNo());

                    if(jurnalHistory!=null){
                        final JournalView jurnalAwal = (JournalView) jurnalHistory.get(0);

                        if(jurnalAwal!=null){
                            j.setStRefTrxType(jurnalAwal.getStRefTrxType());
                        }
                    }

                }
                

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveJournalEntry5(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();


        final JournalView fj = (JournalView) l.get(0);

        fj.setStApproved("Y");

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);
                j.setStApproved("Y");
                j.reCalculate();

            }
            l.markAllUpdate();

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void tesGenerateTransNo(JournalHeaderView header, DTOList l) throws Exception {
        //logger.logDebug("saveJournalEntry: "+l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();


        final JournalView fj = (JournalView) l.get(0);

        if (fj.isNew()) {
            // j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
            //header.setStTransactionNo(generateTransactionNo(header,l));
               /*final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
            if (per==null) throw new Exception("Periods not being setup correctly !");
            j.setLgPeriodNo(per.getLgPeriodNo());
            j.setLgFiscalYear(per.getLgFiscalYear());*/
        }


    }

    public void updateBalance(Long lgAccountID, Long lgPeriodYear, Long lgPeriodNo, BigDecimal am) throws Exception, RemoteException {
        logger.logDebug("updateBalance: [" + lgAccountID + "," + lgPeriodNo + "," + am + "]");

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement PS = S.setQuery("update gl_acct_bal set bal=bal+? where account_id=? and period_year = ? and period_no = ?");

            PS.setBigDecimal(1, am);
            PS.setObject(2, lgAccountID);
            PS.setObject(3, lgPeriodYear);
            PS.setObject(4, lgPeriodNo);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                PS = S.setQuery("insert into gl_acct_bal(account_id,period_year,period_no,bal) values(?,?,?,?)");
                PS.setObject(1, lgAccountID);
                PS.setObject(2, lgPeriodYear);
                PS.setObject(3, lgPeriodNo);
                PS.setBigDecimal(4, am);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    public BigDecimal getBalance(Long lgAccountID, Long lgPeriodYear, Long lgPeriodNo, BigDecimal am) throws Exception, RemoteException {
        logger.logDebug("updateBalance: [" + lgAccountID + "," + lgPeriodNo + "," + am + "]");

        final SQLUtil S = new SQLUtil();
        BigDecimal bd = null;

        try {
            PreparedStatement PS = S.setQuery("select bal from gl_acct_bal where account_id=? and period_year = ? and period_no = ?");

            //PS.setBigDecimal(1,am);
            PS.setObject(1, lgAccountID);
            PS.setObject(2, lgPeriodYear);
            PS.setObject(3, lgPeriodNo);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                bd = RS.getBigDecimal("bal");
            }

            return bd;

        } finally {
            S.release();
        }
    }

    public DTOList listAccounts(AccountFilter accountFilter) throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select * from gl_accounts " + ListUtil.getOrderExpression(accountFilter),
                AccountView.class,
                accountFilter);
    }

    public JournalMasterView getJournalMaster(String stJournalMasterID) throws Exception {
        return (JournalMasterView) ListUtil.getDTOListFromQuery("select * from gl_journal_master where journal_code=?",
                new Object[]{stJournalMasterID},
                JournalMasterView.class).getDTO();
    }

    public GLCostCenterView getDepartment(String stDeptID) throws Exception {
        return (GLCostCenterView) ListUtil.getDTOListFromQuery("select * from gl_cost_center where cc_code=?",
                new Object[]{stDeptID},
                GLCostCenterView.class).getDTO();
    }

    public PeriodHeaderView getPeriod(Long lgPeriodID) throws Exception {
        final PeriodHeaderView ph = (PeriodHeaderView) ListUtil.getDTOListFromQuery("select * from gl_period where gl_period_id=?",
                new Object[]{lgPeriodID},
                PeriodHeaderView.class).getDTO();

        ph.setDetails(
                ListUtil.getDTOListFromQuery(
                "select * from gl_period_det where gl_period_id = ? order by period_no",
                new Object[]{lgPeriodID},
                PeriodDetailView.class));

        return ph;
    }

    public AccountView getAccount(String stAccountID) throws Exception {
        return (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where account_id=?",
                new Object[]{stAccountID},
                AccountView.class).getDTO();
    }

    public void saveJournalMaster(JournalMasterView jm) throws Exception {
        saveDTO(jm);
    }

    public void saveDepartment(GLCostCenterView dept) throws Exception {
        saveDTO(dept);
    }

    public void savePeriod(PeriodHeaderView period) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            if (period.isNew()) {
                period.setLgPeriodID(new Long(IDFactory.createNumericID("GLPRD")));
                final DTOList details = period.getDetails();
                for (int i = 0; i < details.size(); i++) {
                    PeriodDetailView pd = (PeriodDetailView) details.get(i);
                    pd.setLgPeriodID(period.getLgPeriodID());

                    if (pd.isNew()) {
                        pd.setStPeriodDetailID(String.valueOf(IDFactory.createNumericID("PER_DTL")));
                    }
                }
            }
            S.store(period);
            S.store(period.getDetails());

            PeriodManager.getInstance().clearCache();
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveAccount(AccountView account) throws Exception {
        if (account.isNew()) {
            account.setLgAccountID(new Long(IDFactory.createNumericID("ACC")));
            account.setDbBalanceOpen(BDUtil.one);
        }
        saveDTO(account);
        /*final SQLUtil S = new SQLUtil();
        try {
        S.execSQL("insert into gl_accounts (account_id,accountno,acctype,"+
        " description,create_who, create_date) values('"+ account.getStAccountID()+"','"+ account.getStAccountNo()+"'"+
        ",'"+account.getStAccountType()+"','"+ account.getStDescription()+"'"+
        ",'"+account.getStCreateWho()+"','"+ account.getDtCreateDate() +"');");
        } finally {
        S.release();
        }*/
    }

    private void saveDTO(DTO dto) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.store(dto);
        } finally {
            S.release();
        }
    }

    public DTOList listDepartments() throws Exception {
        return ListUtil.getDTOListFromQuery("select * from gl_cost_center",
                GLCostCenterView.class);
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public DTOList listReports() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from gl_rpt",
                GLReportView.class);
    }

    public GLReportView getReportDefinition(String glReportID) throws Exception {
        final GLReportView rpt = (GLReportView) ListUtil.getDTOListFromQuery(
                "select * from gl_rpt where report_id = ?",
                new Object[]{glReportID},
                GLReportView.class).getDTO();

        if (rpt != null) {

            rpt.setLines(
                    ListUtil.getDTOListFromQuery(
                    "select * from gl_rpt_lin where gl_rpt_id = ? order by line_no",
                    new Object[]{glReportID},
                    GLReportLineView.class));

            rpt.setColumns(
                    ListUtil.getDTOListFromQuery(
                    "select * from gl_rpt_col where gl_rpt_id = ? order by col_no",
                    new Object[]{glReportID},
                    GLReportColumnView.class));
        }

        return rpt;
    }

    public GLReportView fillReportData(GLReportView glv) throws Exception {
        final DTOList lines = glv.getLines();

        String actfrom = null, actto = null;

        for (int i = 0; i < lines.size(); i++) {
            GLReportLineView gll = (GLReportLineView) lines.get(i);

            actfrom = (String) Tools.min(gll.getStAccountFrom(), actfrom);
            actto = (String) Tools.max(gll.getStAccountTo(), actto);
        }

        logger.logDebug("fillReportData: actrange : [" + actfrom + "," + actto + "]");

        final long cyear = getCurrentYear();
        long lastyear = cyear - 1;
        final long cper = getCurrentPeriod();

        final Long cYear = new Long(cyear);

        final DTOList columns = glv.getColumns();

        /*for (int i = 0; i < columns.size(); i++) {
        GLReportColumnView col = (GLReportColumnView) columns.get(i);

        final String coltype = col.getStColumnType();

        final String[] colkey = coltype.split(" ");

        long year;

        if ("CYR".equalsIgnoreCase(colkey[1])) year=cyear;
        else if ("LYR".equalsIgnoreCase(colkey[1])) year=lastyear;
        else throw new RuntimeException("Unrecognized column type : "+coltype);


        if ("ACT".equalsIgnoreCase(colkey[0])) {

        }
        else if ("BUD".equalsIgnoreCase(colkey[0])) {

        }
        else throw new RuntimeException("Unrecognized column type : "+coltype);

        }*/

        for (int i = 0; i < lines.size(); i++) {
            GLReportLineView line = (GLReportLineView) lines.get(i);

            line.setColumns(new BigDecimal[15]);
        }

        Long permax = null;
        Long permin = null;
        Long maxyear = null;
        Long minyear = null;

        for (int i = 0; i < columns.size(); i++) {
            GLReportColumnView col = (GLReportColumnView) columns.get(i);

            if (col.getLgPeriod() == null) {
                col.setLgPeriod(new Long(cper));
            }

            if (Tools.compare(col.getLgPeriod(), LongUtil.zero) <= 0) {
                col.setLgPeriod(LongUtil.add(col.getLgPeriod(), new Long(cper)));
            }

            if (col.getLgPeriodTo() == null) {
                col.setLgPeriodTo(col.getLgPeriod());
            }

            if (Tools.compare(col.getLgPeriodTo(), LongUtil.zero) <= 0) {
                col.setLgPeriodTo(LongUtil.add(col.getLgPeriodTo(), new Long(cper)));
            }

            permax = (Long) Tools.max(col.getLgPeriod(), permax);
            permin = (Long) Tools.min(col.getLgPeriod(), permin);

            permax = (Long) Tools.max(col.getLgPeriodTo(), permax);
            permin = (Long) Tools.min(col.getLgPeriodTo(), permin);

            //col.setStColumnHeader(col.getLgPeriod().toString());

            //logger.logDebug("fillReportData: col:"+col.getLgColumnNumber()+" from:"+col.getLgPeriod()+" to:"+col.getLgPeriodTo());

            if (Tools.compare(col.getLgYear(), LongUtil.zero) <= 0) {
                col.setLgYear(LongUtil.add(col.getLgYear(), cYear));
            }

            maxyear = (Long) Tools.max(maxyear, col.getLgYear());
            minyear = (Long) Tools.min(minyear, col.getLgYear());

        }

//      logger.logDebug("fillReportData: permin="+permin+" permax="+permax);

        logger.logDebug("fillReportData: year min:" + minyear + " max:" + maxyear);

        final String actfrom1 = actfrom;
        final String actto1 = actto;

        final HashMap balanceMap = new HashMap() {

            public Object get(Object key) {
                Object o = super.get(key);
                try {

                    if (o == null) {
                        final long year = Long.valueOf((String) key).longValue();
                        //final long period = LongUtil.getLong(col.getLgPeriodFrom());

                        final DTOList balances = ListUtil.getDTOListFromQuery(
                                "   select "
                                + "      a.bal,a.period_no,b.accountno "
                                + "   from "
                                + "      gl_acct_bal a"
                                + "         inner join gl_accounts b on b.account_id=a.account_id"
                                + "   where "
                                + "      a.period_year = ?"
                                + "      and b.accountno>=? and b.accountno<=?"
                                + "   order by a.account_id, a.period_no",
                                new Object[]{
                                    new Long(year),
                                    actfrom1, actto1
                                },
                                GLBalanceView.class);

                        /*BigDecimal accum = null;

                        GLBalanceView lastbal = null;

                        for (int i = 0; i < balances.size(); i++) {
                        GLBalanceView glb = (GLBalanceView) balances.get(i);

                        if (lastbal!=null)
                        if (Tools.compare(lastbal.getStAccountNo(),glb.getStAccountNo())!=0) accum=null;

                        glb.setDbEffectiveBalance(BDUtil.add(accum,glb.getDbBalance()));

                        accum = BDUtil.add(glb.getDbBalance(),accum);

                        logger.logDebug("acct:"+glb.getStAccountNo()+" per:"+glb.getLgPeriondNo()+" bal:"+glb.getDbBalance()+" accum:"+accum+" eff:"+glb.getDbEffectiveBalance());

                        lastbal = glb;
                        }*/

                        o = balances;

                        super.put(key, o);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return o;
            }
        };



        /*for (int i = 0; i < columns.size(); i++) {
        GLReportColumnView col = (GLReportColumnView) columns.get(i);

        final DTOList balances = (DTOList) balanceMap.get(col.getLgYear().toString());

        final int periodIdx = i+1;*/

        HashMap totalMap = new HashMap();

        ArrayList outputLines = new ArrayList();

        Object[] currentOutputLine = null;

        for (long y = minyear.longValue(); y <= maxyear.longValue(); y++) {

            final DTOList balances = (DTOList) balanceMap.get(String.valueOf(y));

            for (int k = 0; k < balances.size(); k++) {

                GLBalanceView bal = (GLBalanceView) balances.get(k);

                logger.logDebug("bal:[ac:" + bal.getStAccountNo() + ",per:" + bal.getLgPeriondNo() + ",year:" + bal.getLgPeriodYear() + ",bal:" + bal.getDbBalance() + "]");

                //if (col.getLgPeriod()!=null)
                //   if (Tools.compare(bal.getLgPeriondNo(),col.getLgPeriod())<0) continue;

                /*if (col.getLgPeriodTo()!=null)
                if (Tools.compare(bal.getLgPeriondNo(), col.getLgPeriodTo())>0) continue;*/

                //balancemap.put(bal.getStAccountNo()+"/"+bal.getLgPeriondNo(),bal.getDbBalance());
                //final int periodIdx = ((int) bal.getLgPeriondNo().longValue());
                BigDecimal cbx;

                //if (FinCodec.GLReportColType.BALANCE.equalsIgnoreCase(col.getStValue())) cb = bal.getDbEffectiveBalance();
                //else if (FinCodec.GLReportColType.SUMMARY.equalsIgnoreCase(col.getStValue())) cb = bal.getDbBalance();
                //else throw new RuntimeException("Error processing Column type : "+col.getStValue());

                cbx = bal.getDbBalance();

                final String act = bal.getStAccountNo();

                //logger.logDebug("fillReportData: per = "+periodIdx);

                GLReportLineView lastline = null;

                int outputLineNo = 0;
                currentOutputLine = null;

                for (int j = 0; j < lines.size(); j++) {

                    GLReportLineView line = (GLReportLineView) lines.get(j);

                    if (lastline != null) {
                        if (Tools.isYes(lastline.getStPrintCRFlag())) {
                            currentOutputLine = null;
                        }
                    }

                    lastline = line;

                    if (currentOutputLine == null) {
                        outputLineNo++;
                        if (outputLines.size() < outputLineNo) {
                            currentOutputLine = new Object[15];
                            outputLines.add(currentOutputLine);
                        }
                        currentOutputLine = (Object[]) outputLines.get(outputLineNo - 1);
                    }

                    final boolean negate = Tools.isYes(line.getStNegateFlag());

                    BigDecimal cb = negate ? BDUtil.negate(cbx) : cbx;

                    final long colNo = line.getLgColumnNo() == null ? 0 : (line.getLgColumnNo().longValue());

                    if (colNo < 0) {
                        throw new RuntimeException("Invalid column number");
                    }

                    final boolean zeroColumn = colNo == 0;

                    final long colStart = zeroColumn ? 0 : (colNo - 1);
                    final long colStop = zeroColumn ? (columns.size() - 1) : colNo - 1;

                    for (int l = (int) colStart; l <= colStop; l++) {
                        GLReportColumnView colx = (GLReportColumnView) columns.get(l);

                        if (zeroColumn && (l == 0)) {
                            currentOutputLine[l + 1] = line.getStDescription();
                            continue;
                        }

                        final String lineType = line.getStLineType();

                        if (lineType == null) {
                        } else if (lineType.indexOf("TOT") == 0) {
                        } else if (lineType.indexOf("DESC") == 0) {
                            currentOutputLine[l + 1] = line.getStDescription();
                        } else if (lineType.indexOf("ACCT") == 0) {

                            if (line.getStAccountFrom() != null) {
                                if (Tools.compare(act, line.getStAccountFrom()) < 0) {
                                    continue;
                                }
                            }
                            if (line.getStAccountTo() != null) {
                                if (Tools.compare(act, line.getStAccountTo()) > 0) {
                                    continue;
                                }
                            }

                            for (int m = 1; m <= 9; m++) {
                                final String totKey = "TOT/" + m + "/" + l;
                                totalMap.put(totKey, BDUtil.add((BigDecimal) totalMap.get(totKey), cb));
                            }

                            if (colx.isBalance()) {
                                if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) >= 0) {
                                    logger.logDebug("BAL: " + cb + " to line " + line.getLgLineNo() + " per " + colx.getLgColumnNumber());
                                    currentOutputLine[l + 1] = BDUtil.add((BigDecimal) currentOutputLine[l + 1], cb);
//                           line.getColumns()[l+1] = BDUtil.add((BigDecimal)line.getColumns()[l+1], cb);
                                }
                            } else if (colx.isSummary()) {
                                if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) == 0) {
                                    logger.logDebug("SUM: " + cb + " to line " + line.getLgLineNo() + " per " + colx.getLgColumnNumber());
                                    currentOutputLine[l + 1] = BDUtil.add((BigDecimal) currentOutputLine[l + 1], cb);
//                           line.getColumns()[l+1] = BDUtil.add((BigDecimal)line.getColumns()[l+1], cb);
                                }
                            }
                        }


                    }


                    //logger.logDebug("fillReportData: "+line.getColumns()[periodIdx]);
                }
            }

            /*for (int j = 0; j < lines.size(); j++) {
            GLReportLineView line = (GLReportLineView) lines.get(j);

            if (line.getColumns()[periodIdx]==null) line.getColumns()[periodIdx]=line.getColumns()[periodIdx-1];
            }*/
        }

        //}

        /*for (int i = 0; i < lines.size(); i++) {
        GLReportLineView ln = (GLReportLineView) lines.get(i);
        final Object[] vals = ln.getColumns();

        for (int j = 1; j < vals.length; j++) {
        Object val = vals[j];
        if (val instanceof BigDecimal) {
        vals[j]=BDUtil.add((BigDecimal)vals[j],(BigDecimal)vals[j-1]);
        }
        }
        }*/

        /*for (int i = 0; i < lines.size(); i++) {
        GLReportLineView ln = (GLReportLineView) lines.get(i);

        if (Tools.isYes(ln.getStNegateFlag())) {
        final Object[] vals = ln.getColumns();

        for (int j = 0; j < vals.length; j++) {
        Object val = vals[j];
        if (val instanceof BigDecimal) {
        vals[j]=((BigDecimal)val).negate();
        }
        }
        }
        }*/

        glv.setResult(outputLines);

        return glv;
    }

    private long getCurrentPeriod() {
        return Parameter.readNum("GL2_CUR_PERIOD").longValue();
    }

    private long getCurrentYear() {
        return Parameter.readNum("GL2_CUR_YEAR").longValue();
    }

    public void closePeriod(String stPeriodDetailID) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void openPeriod(String stPeriodDetailID) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void closePeriod(PeriodDetailView pdd) throws Exception {

        pdd.setStClosedFlag("Y");
        pdd.markUpdate();

        save(pdd);
    }

    private void save(PeriodDetailView pdd) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.store(pdd);
        } finally {
            S.release();
        }
    }

    public void openPeriod(PeriodDetailView pdd) throws Exception {
        pdd.setStClosedFlag("N");
        pdd.markUpdate();

        save(pdd);
    }

    public AccountView getAccountByAccountNo(String stAccountNo) throws Exception {
        return (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where accountno=?",
                new Object[]{stAccountNo},
                AccountView.class).getDTO();
    }

    public AccountView getAccountByAccountID(String stAccountID) throws Exception {
        return (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where account_id=?",
                new Object[]{stAccountID},
                AccountView.class).getDTO();
    }

    public AccountView autoCreateAccount(String acno, String costcenter) throws Exception {
        final SQLUtil S = new SQLUtil();
        try {
            final AccountView coa = getGLCOA(acno);
            coa.markNew();
            coa.setLgAccountID(null);

            coa.setStAccountNo(coa.getStAccountNo() + " " + costcenter);

            //coa.setStAccountNo(coa.getStAccountNo());

            saveAccount(coa);

            coa.markUnmodified();

            return coa;
        } finally {
            S.release();
        }
    }

    private AccountView getGLCOA(String acno) throws Exception {
        return (AccountView) ListUtil.getDTOListFromQuery(
                "select * from gl_chart where accountno = ?",
                new Object[]{acno},
                AccountView.class).getDTO();
    }

    public void reverse(String stRefTRX) throws Exception {
        final DTOList lines = ListUtil.getDTOListFromQuery(
                "select * from gl_je_detail where ref_trx = ?",
                new Object[]{stRefTRX},
                JournalView.class);

        final DTOList linesRev = new DTOList();

        for (int i = 0; i < lines.size(); i++) {
            JournalView j0 = (JournalView) lines.get(i);

            JournalView j = j0.copy();

            j.markNew();
            j.setStRefReverse(j.getStTransactionID());
            j.setStRefTRX(j0.getStRefTRX() + "/R1");
            j.setStTransactionNo(j0.getStTransactionNo() + "/R1");
            j.setStTransactionID(null);
            j.setStTransactionHeaderID(null);
            j.setStRefTRX("REV");
            j.setStRefTrxNo(j0.getStTransactionHeaderID());
            j.inverse();
            j.setStReverseFlag("Y");

            j0.markUpdate();
            j0.setStRefTRX(j0.getStRefTRX() + "/R0");
            j0.setStReverseFlag("Y");

            linesRev.add(j);
        }

        saveJournalEntry(lines);
        saveJournalEntry(linesRev);
    }

    public String generateTransactionNo(JournalHeaderView header, DTOList l, String transNo) throws Exception {

        //if (stReceiptNo!=null) return;
        String transactionNo = "";
        final String ccCode = Tools.getDigitRightJustified(header.getStCostCenter(), 2);
        final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(), 1);
        //String stBankCode = getPaymentMethod()==null?"0000":getPaymentMethod().getStBankCode();
        //final String bankCode = Tools.getDigitRightJustified(stBankCode,4);

        JournalView det = (JournalView) l.get(0);

        String counterKey =
                DateUtil.getYear2Digit(det.getDtApplyDate())
                + DateUtil.getMonth2Digit(det.getDtApplyDate());

        //String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //rn = Tools.getDigitRightJustified(rn,3);
        rn = StringTools.leftPad(rn, '0', 5);

        String accountcode = "";
        if (header.getStAccountNoMaster() != null) {
            accountcode = header.getStAccountNoMaster().substring(5, 10);
        } else {
            accountcode = transNo.substring(5, 10);
        }

        //110002700400
        //012345678901
        //no
        //  A0901171000000
        //  01234567890123
        //    A0910202700002

        transactionNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + accountcode
                + rn;

        return transactionNo;

    }

    public String generateAccount(String ac, String cc) throws Exception {
        if (ac == null) {
            return null;
        }
        if (cc == null) {
            return null;
        }
        return autoCreateAccount(ac, cc).getStAccountID();
    }

    public DTOList getCharts() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from gl_chart order by accountno",
                GLChartView.class);
    }

    public void saveReport(GLReportView rpt) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            if (rpt.isNew()) {
                rpt.setStReportID(String.valueOf(IDFactory.createNumericID("GLRPTID")));
            }

            S.store(rpt);

            final DTOList columns = rpt.getColumns();

            for (int i = 0; i < columns.size(); i++) {
                GLReportColumnView col = (GLReportColumnView) columns.get(i);

                if (col.isNew()) {
                    col.setStGLReportColumnID(String.valueOf(IDFactory.createNumericID("GLRPTCOLID")));
                }

                col.setStGLReportID(rpt.getStReportID());
            }

            S.store(columns);

            final DTOList lines = rpt.getLines();

            for (int i = 0; i < lines.size(); i++) {
                GLReportLineView line = (GLReportLineView) lines.get(i);

                if (line.isNew()) {
                    line.setStGLReportLineID(String.valueOf(IDFactory.createNumericID("GLRPTLINEID")));
                }

                line.setStGLReportID(rpt.getStReportID());
            }

            S.store(lines);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateGLACCTBalance(Long lgAccountID, Long lgPeriodYear, Long lgPeriodNo, BigDecimal am) throws Exception, RemoteException {
        logger.logDebug("updateBalance: [" + lgAccountID + "," + lgPeriodNo + "," + am + "]");

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement PS = S.setQuery("update gl_acct_bal set bal=bal+? where account_id=? and period_year = ? and period_no = ?");

            PS.setBigDecimal(1, am);
            PS.setObject(2, lgAccountID);
            PS.setObject(3, lgPeriodYear);
            PS.setObject(4, lgPeriodNo);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                PS = S.setQuery("insert into gl_acct_bal(account_id,period_year,period_no,bal) values(?,?,?,?)");
                PS.setObject(1, lgAccountID);
                PS.setObject(2, lgPeriodYear);
                PS.setObject(3, lgPeriodNo);
                PS.setBigDecimal(4, am);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    /*
    public AccountView getAccountByAccountID(String stAccountID) throws Exception {
    return (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where account_id=?",
    new Object [] {stAccountID},
    AccountView.class).getDTO();
    }*/
    public void saveJournalSaldoAwal(DTOList l) throws Exception {
        //logger.logDebug("saveJournalEntry");

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();


        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        if (!reversing) //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);
        {
            if (stTransactionHeaderID == null) {
                stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
            }
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    /*final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per==null) throw new Exception("Periods not being setup correctly !");
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());*/
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);
            S.release();
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public String saveJournalEntryAutoPayment(DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        //if (l.size()<1) break;

        final SQLUtil S = new SQLUtil();


        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();
        Long lgHeaderAccountID = fj.getLgAccountID();
        String stHeaderAccountNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        if (!reversing) //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);
        {
            if (stTransactionHeaderID == null) {
                stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
            }
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    /*final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per==null) throw new Exception("Periods not being setup correctly !");
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());*/
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);

            return stTransactionHeaderID;
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void AccountLabaBersih(String am, long periodTo, long yearFrom, BigDecimal labaBersih, String flag) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query = "update gl_acct_bal2 set cr" + periodTo + " = 0,db" + periodTo + " = " + labaBersih + ", bal" + periodTo + " = " + labaBersih + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal2 (account_id,period_year,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setBigDecimal(3, labaBersih);
                PS.setBigDecimal(4, labaBersih);
                PS.setObject(5, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    public void AccountRekeningKantor(String am, long periodTo, long yearFrom, BigDecimal balance, String flag) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query = "update gl_acct_bal2 set cr" + periodTo + "=0,db" + periodTo + "=" + balance + ",bal" + periodTo + "=" + balance + " where account_id = ? and period_year = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal2 (account_id,period_year,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setBigDecimal(3, balance);
                PS.setBigDecimal(4, balance);
                PS.setObject(5, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    public LOV getMethodCodeLOVCashBank() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select method_code,description from receipt_class where cash_bank_flag = 'Y'",
                ARReceiptClassView.class);
    }

    public void save(AccountView account) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (account.isNew()) {
                account.setLgAccountID(new Long(IDFactory.createNumericID("ACCOUNT")));
                account.setDbBalanceOpen(BDUtil.one);
            }

            S.store(account);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void saveJournalUangMuka(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        Long lgHeaderAccountID = header.getLgAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        // if (!reversing)
        //  if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();
        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                JournalView j2 = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                final GLUtil.Applicator gla = new GLUtil.Applicator();

                gla.setCode('Y', j.getPolicy().getEntity().getStGLCode());
                gla.setCode('X', j.getPolicy().getStPolicyTypeID());

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("Y", j.getPolicy().getEntity().getStShortName());
                }

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("X", j.getPolicy().getPolicyType().getStShortDescription());
                }

                gla.setCode('B', header.getStCostCenter());

                final LookUpUtil lu = ListUtil.getLookUpFromQuery("select gl_account,gl_account from ar_trx_line where item_class ='ADVPAYMENT' and enabled_flag='Y'");
                final String uangMukaAccount = lu.getCode(0);

                final String accountID = gla.getAccountID(uangMukaAccount);

                j.setStAccountID(accountID);
                j.setStDescription(gla.getStGLDesc());

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(trxNo);
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("CLAIM_ADVPAYMENT");

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                JournalView viuw = (JournalView) l.get(i);
                S.store(j);

                if (j.getStPolicyID() != null) {

                    InsurancePolicyView klaim = j.getPolicy();
                    klaim.setDbClaimAdvancePaymentAmount(BDUtil.isZeroOrNull(j.getDbEnteredCredit()) ? j.getDbEnteredDebit() : j.getDbEnteredCredit());
                    klaim.markUpdate();
                    S.store(klaim);

                    if (klaim.isStatusClaimDLA() && klaim.isEffective()) {
                        ARInvoiceView invoice = getARInvoiceByAttrPolIDAndTrxTypeID(klaim.getStPolicyID(), "12");

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        invoice.getDetails().add(ivd);

                        ivd.setStEntityID(klaim.getStEntityID());
                        ivd.setStARInvoiceID(invoice.getStARInvoiceID());
                        ivd.setStARTrxLineID("86");
                        ivd.loadSettings();
                        ivd.setDbEnteredAmount(klaim.getDbClaimAdvancePaymentAmount());

                        if (ivd.isNew()) {
                            ivd.setStARInvoiceDetailID(String.valueOf(IDFactory.createNumericID("ARINVDNEW")));
                        }

                        invoice.recalculate();

                        S.store(ivd);
                    }
                    logger.logDebug("++++++++++ UPDATE DATA POLIS UANG MUKA : " + klaim.getStPLANo() + " ++++++++++++++++++");
                }

                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionNo(trxNo);
                    j2.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j2.getDbDebit();
                    j2.setDbDebit(j2.getDbCredit());
                    j2.setDbCredit(debitOld);
                    j2.setStDescription(header.getStDescriptionMaster());
                    debitEnteredOld = j2.getDbEnteredDebit();
                    j2.setDbEnteredDebit(j2.getDbEnteredCredit());
                    j2.setDbEnteredCredit(debitEnteredOld);
                    //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j2.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("CLAIM_ADVPAYMENT");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                JournalView viuw2 = (JournalView) l.get(i);
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveJournalUangMukaPremi(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug(" ++++++++++++++++== save uang muka premi : ++++++++++++++ " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        Long lgHeaderAccountID = header.getLgAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        // if (!reversing)
        //  if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();
        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                JournalView j2 = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                final GLUtil.Applicator gla = new GLUtil.Applicator();

                gla.setCode('Y', j.getPolicy().getEntity().getStGLCode());
                gla.setCode('X', j.getPolicy().getStPolicyTypeID());

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("Y", j.getPolicy().getEntity().getStShortName());
                }

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("X", j.getPolicy().getPolicyType().getStShortDescription());
                }

                gla.setCode('B', header.getStCostCenter());

                final LookUpUtil lu = ListUtil.getLookUpFromQuery("select gl_account,gl_account from ar_trx_line where item_class ='PREMIADVPAYMENT' and enabled_flag='Y'");
                final String uangMukaAccount = lu.getCode(0);

                final String accountID = gla.getAccountID(uangMukaAccount);

                j.setStAccountID(accountID);
                j.setStDescription(gla.getStGLDesc());

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(trxNo);
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("PREMIUM_ADVPAYMENT");

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                JournalView viuw = (JournalView) l.get(i);
                S.store(j);

                if (j.getStPolicyID() != null) {

                    InsurancePolicyView policy = j.getPolicy();
                    policy.setDbClaimAdvancePaymentAmount(BDUtil.isZeroOrNull(j.getDbEnteredCredit()) ? j.getDbEnteredDebit() : j.getDbEnteredCredit());
                    policy.markUpdate();
                    S.store(policy);

                    if (policy.isStatusPolicy() && policy.isEffective()) {
                        ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolID(policy.getStPolicyID());

                        if (invoice == null) {
                            throw new RuntimeException("Ar Invoice tidak ditemukan");
                        }

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        invoice.getDetails().add(ivd);

                        ivd.setStEntityID(policy.getStEntityID());
                        ivd.setStARInvoiceID(invoice.getStARInvoiceID());
                        ivd.setStARTrxLineID("87");
                        ivd.loadSettings();
                        ivd.setDbEnteredAmount(policy.getDbClaimAdvancePaymentAmount());

                        if (ivd.isNew()) {
                            ivd.setStARInvoiceDetailID(String.valueOf(IDFactory.createNumericID("ARINVDNEW")));
                        }

                        invoice.recalculate();

                        S.store(ivd);
                    }
                    logger.logDebug("++++++++++ UPDATE DATA POLIS UANG MUKA : " + policy.getStPLANo() + " ++++++++++++++++++");
                }

                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionNo(trxNo);
                    j2.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j2.getDbDebit();
                    j2.setDbDebit(j2.getDbCredit());
                    j2.setDbCredit(debitOld);
                    j2.setStDescription(header.getStDescriptionMaster());
                    debitEnteredOld = j2.getDbEnteredDebit();
                    j2.setDbEnteredDebit(j2.getDbEnteredCredit());
                    j2.setDbEnteredCredit(debitEnteredOld);
                    //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j2.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("PREMIUM_ADVPAYMENT");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                JournalView viuw2 = (JournalView) l.get(i);
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveJournalUangMukaKomisi(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        // if (!reversing)
        //  if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();
        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                JournalView j2 = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                final GLUtil.Applicator gla = new GLUtil.Applicator();

                gla.setCode('Y', j.getPolicy().getEntity().getStGLCode());
                gla.setCode('X', j.getPolicy().getStPolicyTypeID());

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("Y", j.getPolicy().getEntity().getStShortName());
                }

                if (j.getPolicy().getEntity() != null) {
                    gla.setDesc("X", j.getPolicy().getPolicyType().getStShortDescription());
                }

                gla.setCode('B', header.getStCostCenter());

                final LookUpUtil lu = ListUtil.getLookUpFromQuery("select gl_account,gl_account from ar_trx_line where item_class ='COMISADVPAYMENT' and enabled_flag='Y'");
                final String uangMukaAccount = lu.getCode(0);

                final String accountID = gla.getAccountID(uangMukaAccount);

                j.setStAccountID(accountID);
                j.setStDescription(gla.getStGLDesc());

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(trxNo);
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("COMIS_ADVPAYMENT");

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                JournalView viuw = (JournalView) l.get(i);
                S.store(j);

                if (j.getStPolicyID() != null) {

                    InsurancePolicyView policy = j.getPolicy();
                    policy.setDbClaimAdvancePaymentAmount(BDUtil.isZeroOrNull(j.getDbEnteredCredit()) ? j.getDbEnteredDebit() : j.getDbEnteredCredit());
                    policy.markUpdate();
                    S.store(policy);

                    if (policy.isStatusPolicy() && policy.isEffective()) {
                        ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolID(policy.getStPolicyID());

                        if (invoice == null) {
                            throw new RuntimeException("Ar Invoice tidak ditemukan");
                        }

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        invoice.getDetails().add(ivd);

                        ivd.setStEntityID(policy.getStEntityID());
                        ivd.setStARInvoiceID(invoice.getStARInvoiceID());
                        ivd.setStARTrxLineID("88");
                        ivd.loadSettings();
                        ivd.setDbEnteredAmount(policy.getDbClaimAdvancePaymentAmount());

                        if (ivd.isNew()) {
                            ivd.setStARInvoiceDetailID(String.valueOf(IDFactory.createNumericID("ARINVDNEW")));
                        }

                        invoice.recalculate();

                        S.store(ivd);
                    }
                    logger.logDebug("++++++++++ UPDATE DATA POLIS UANG MUKA : " + policy.getStPLANo() + " ++++++++++++++++++");
                }

                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    //j.setStTransactionNo(generateTransactionNo(header,l));
                    j2.setStTransactionNo(trxNo);
                    j2.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j2.getDbDebit();
                    j2.setDbDebit(j2.getDbCredit());
                    j2.setDbCredit(debitOld);
                    j2.setStDescription(header.getStDescriptionMaster());
                    debitEnteredOld = j2.getDbEnteredDebit();
                    j2.setDbEnteredDebit(j2.getDbEnteredCredit());
                    j2.setDbEnteredCredit(debitEnteredOld);
                    //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j2.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("COMIS_ADVPAYMENT");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                JournalView viuw2 = (JournalView) l.get(i);
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public ARInvoiceView getARInvoiceByAttrPolIDAndTrxTypeID(String attrpolid, String trxtype) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where attr_pol_id = ? and ar_trx_type_id = ?",
                new Object[]{attrpolid, trxtype},
                ARInvoiceView.class).getDTO();

        if (iv != null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.attr_pol_id = ? and b.ar_trx_type_id = ?",
                    new Object[]{attrpolid, trxtype},
                    ARInvoiceDetailView.class));
        }

        return iv;
    }

    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB", AccountReceivableHome.class.getName())).create();
    }

    public void saveTitipanPremi(TitipanPremiHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry titipan premi : " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiView fj = (TitipanPremiView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String lgHeaderAccountID = header.getStAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("TPHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = header.getStTransactionNo();
        if (header.isNew()) {
            trxNo = generateTitipanPremiNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiView j = (TitipanPremiView) l.get(i);

                //TitipanPremiView j2 = (TitipanPremiView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setStHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);
                j.setStMonths(header.getStMonths());
                j.setStYears(header.getStYears());
                j.setStMethodCode(header.getStMethodCode());
                j.setStAccountIDMaster(header.getStAccountIDMaster());
                j.setStDescriptionMaster(header.getStDescriptionMaster());
                j.setStCostCenter(header.getStCostCenter());


                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("TPNEW")));

                    j.setStTransactionNo(trxNo);
                }

                //if (j.getDtApplyDate()==null) throw new Exception("Apply Date is not defined !");
                if (j.getDtApplyDate() != null) {
                    final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());
                }

                j.setStRefTrxType("TITIPAN_PREMI");
                j.setStIDRFlag("Y");

                /*
                if (j.isModified())
                {

                final TitipanPremiView old = j.getOldJournal();

                if (old!=null)
                {

                final boolean isSameRecord = Tools.compare(j.getStAccountID(), old.getStAccountID())==0 &&
                Tools.compare(j.getDtApplyDate(), old.getDtApplyDate())==0;

                if (isSameRecord)
                {
                final BigDecimal am = j.getDbAdjustmentAmount();

                if (am.compareTo(BDUtil.zero)!=0)
                {
                updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), am);

                }
                }
                else
                {
                updateBalance(Long.getLong(old.getStAccountID()), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
                }
                else
                {
                updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
                }*/

                TitipanPremiView viuw = (TitipanPremiView) l.get(i);
                //S.store(j);

            }

            S.store(l);

            if (header.isApproved()) {
                saveJournalEntryTitipanPremi(header, l);
            }

           // return trxNo;

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public String generateTitipanPremiNo(TitipanPremiHeaderView header, DTOList l, String transNo) throws Exception {

        //if (stReceiptNo!=null) return;
        String transactionNo = "";
        final String ccCode = Tools.getDigitRightJustified(header.getStCostCenter(), 2);
        final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(), 1);
        //String stBankCode = getPaymentMethod()==null?"0000":getPaymentMethod().getStBankCode();
        //final String bankCode = Tools.getDigitRightJustified(stBankCode,4);

        /*String counterKey =
        DateUtil.getYear2Digit(header.getDtCreateDate())+
        DateUtil.getMonth2Digit(header.getDtCreateDate());*/

        String counterKey = header.getStYears().substring(2) + header.getStMonths();

        //String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //String rn = String.valueOf(IDFactory.createNumericID("TPNO" + counterKey + ccCode,1));

        //rn = Tools.getDigitRightJustified(rn,3);
        rn = StringTools.leftPad(rn, '0', 5);

        String accountcode = "";
        if (header.getStAccountNoMaster() != null) {
            accountcode = header.getStAccountNoMaster().substring(5, 10);
        } else {
            accountcode = transNo.substring(5, 10);
        }

        //110002700400
        //012345678901
        //no
        //  A0901171000000
        //  01234567890123
        //    A0910202700002

        transactionNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + accountcode
                + rn;

        return transactionNo;

    }

    public DTOList getTitipanPremi(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      ar_titipan_premi a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id=? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                TitipanPremiView.class);
    }

    public void saveEditTitipanPremi(TitipanPremiHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiView fj = (TitipanPremiView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalanceTitipanPremi(l);

        if (!reversing) {
            if (Tools.compare(bal, BDUtil.zero) != 0) {
                throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTitipanPremiNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiView j = (TitipanPremiView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("TITIPAN_PREMI");

                if (j.isModified()) {

                    final TitipanPremiView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getStAccountID(), old.getStAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(Long.getLong(old.getStAccountID()), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                //cek apakah input titipan premi
                /*if(stTransactionNo.substring(0,1).equalsIgnoreCase("H"))
                {
                if(j.getStAccountNo().substring(0,3).equalsIgnoreCase(Parameter.readString("TITIPAN_PREMI_ACC")))
                {
                ARTitipanPremiView titipan = new ARTitipanPremiView();

                titipan.markNew();

                titipan.setStARTitipanID(String.valueOf(IDFactory.createNumericID("ARTP")));
                titipan.setStTransactionNo(stTransactionNo);
                titipan.setLgAccountID(j.getLgAccountID());
                titipan.setStAccountNo(j.getStAccountNo());
                titipan.setStDescription(j.getStDescription());
                titipan.setDtApplyDate(j.getDtApplyDate());
                titipan.setStTransactionID(j.getStTransactionID());
                titipan.setStCurrencyCode(j.getStCurrencyCode());
                titipan.setDbCurrencyRate(j.getDbCurrencyRate());
                titipan.setStTransactionHeaderID(j.getStTransactionHeaderID());
                titipan.setDbBalance(j.getDbCredit()==BDUtil.zero?j.getDbDebit():j.getDbCredit());
                titipan.setStCostCenterCode(j.getStCostCenter());

                S.store(titipan);

                ARTitipanPremiDetailsView titipanDetil = new ARTitipanPremiDetailsView();

                titipanDetil.markNew();


                titipanDetil.setStARTitipanDetailsID(String.valueOf(IDFactory.createNumericID("ARTPD")));
                titipanDetil.setStARTitipanID(titipan.getStARTitipanID());
                titipanDetil.setStAccountNo(j.getStAccountNo());
                titipanDetil.setStDescription(j.getStDescription());
                titipanDetil.setDbDebit(j.getDbDebit());
                titipanDetil.setDbCredit(j.getDbCredit());

                S.store(titipanDetil);
                }


                }*/

            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveEditCashBank(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        //boolean reversing = fj.getStReverseFlag()!=null;

        BigDecimal bal = GLUtil.getBalance(l);

        //if (!reversing)
        //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
        }

        String tahun = header.getStYears();
        String bulan = header.getStMonths();

        DTOList jurnal = l;
        DTOList cek = new DTOList();

        cek.addAll(l);
        //cek.addAll(l.getDeleted());

        try {
            for (int i = 0; i < cek.size(); i++) {
                JournalView j = (JournalView) cek.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                //j.setStTransactionNo(stTransactionNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("CASHBANK");
                j.setStIDRFlag("Y");

                if (!Tools.isEqual(DateUtil.getMonth2Digit(j.getDtApplyDate()), bulan)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                }
                if (!Tools.isEqual(DateUtil.getYear(j.getDtApplyDate()), tahun)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                }

                if (j.getLgHeaderAccountID() == null) {
                    j.setLgHeaderAccountID(fj.getLgHeaderAccountID());
                    j.setStHeaderAccountNo(fj.getStHeaderAccountNo());
                }


                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().//updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                //JournalView viuw = (JournalView) cek.get(i);
                //S.store(j);

                //cek jika data baru maka buatkan lawan nya
                if (j.isNew()) {
                    JournalView j2 = new JournalView();

                    j2.markNew();

                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j.reCalculate();

                    j2.setStTransactionNo(fj.getStTransactionNo());
                    j2.setLgAccountID(fj.getLgHeaderAccountID());
                    j2.setStHeaderAccountNo(fj.getStHeaderAccountNo());

                    j2.setStIDRFlag("Y");
                    j2.setDtApplyDate(j.getDtApplyDate());

                    if (j2.getDtApplyDate() == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j2.setLgPeriodNo(per.getLgPeriodNo());
                    j2.setLgFiscalYear(per.getLgFiscalYear());
                    j2.setStRefTrxType("CASHBANK");
                    j2.setStIDRFlag("Y");
                    j2.setStDescription(j.getStDescription());
                    j2.setStJournalCode(j.getStJournalCode());
                    j2.setStCurrencyCode(j.getStCurrencyCode());
                    j2.setDbCurrencyRate(j.getDbCurrencyRate());
                    j2.setLgHeaderAccountID(j.getLgHeaderAccountID());
                    j2.setStMonths(j.getStMonths());
                    j2.setStYears(j.getStYears());

                    j2.setDbDebit(j.getDbCredit());
                    j2.setDbCredit(j.getDbDebit());
                    j2.setDbEnteredDebit(j.getDbEnteredCredit());
                    j2.setDbEnteredCredit(j.getDbEnteredDebit());
                    j2.setStOwnerCode(j.getStOwnerCode());
                    j2.setStUserCode(j.getStUserCode());

                    if (!Tools.isEqual(DateUtil.getMonth2Digit(j2.getDtApplyDate()), bulan)) {
                        throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                    }
                    if (!Tools.isEqual(DateUtil.getYear(j2.getDtApplyDate()), tahun)) {
                        throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                    }

                    if (j2.isModified()) {

                        final JournalView old = j2.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j2.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                            }
                        } else {
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    }
                    //JournalView viuw2 =(JournalView) l.get(i);
                    //S.store(j2);
                    jurnal.add(j2);
                }

            }

            S.store(jurnal);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveJournalEntryTitipanPremi(TitipanPremiHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiView fj = (TitipanPremiView) l.get(0);

        String stTransactionHeaderID = null;//fj.getStTransactionHeaderID();


        boolean reversing = fj.getStReverseFlag() != null;

        String lgHeaderAccountID = header.getStAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        //BigDecimal bal = GLUtil.getBalance(l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = null;
        if (header.isNew()) {
            //trxNo = generateTransactionNo(header,l,transNo);
            //header.setStTransactionNo(trxNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiView titip = (TitipanPremiView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j.setStHeaderAccountNo(titip.getStHeaderAccountNo());
                j.setStAccountID(titip.getStAccountID());

                j.setDbAutoCredit2(titip.getDbAmount());

                j.setStDescription(titip.getStDescription());
                j.setStJournalCode(titip.getStJournalCode());
                j.setStCurrencyCode(titip.getStCurrencyCode());
                j.setDbCurrencyRate(titip.getDbCurrencyRate());
                j.setStRefTrxNo(titip.getStTransactionID());
                j.setStIDRFlag("Y");
                j.setStSummaryFlag("Y");

                if(titip.getStPolicyNo()!=null)
                    j.setStPolicyNo(titip.getStPolicyNo());

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(titip.getStTransactionNo());
                }

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(titip.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("TITIPAN_PREMI");
                j.setDtApplyDate(titip.getDtApplyDate());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(titip.getStTransactionNo());

                    j2.setStAccountID(header.getStAccountIDMaster());

                    j2.setDbAutoDebit2(titip.getDbAmount());

                    j2.setStDescription(titip.getStDescription());
                    j2.setStJournalCode(titip.getStJournalCode());
                    j2.setStCurrencyCode(titip.getStCurrencyCode());
                    j2.setDbCurrencyRate(titip.getDbCurrencyRate());
                    j2.setStRefTrxNo(titip.getStTransactionID());
                    j2.setStIDRFlag(titip.getStIDRFlag());
                    j2.setStIDRFlag("Y");

                    if(titip.getStPolicyNo()!=null)
                        j2.setStPolicyNo(titip.getStPolicyNo());

                }

                j2.setStSummaryFlag("Y");
                j2.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j2.setStHeaderAccountNo(titip.getStHeaderAccountNo());

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + titip.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("TITIPAN_PREMI");
                j2.setDtApplyDate(titip.getDtApplyDate());
                j2.setStIDRFlag(titip.getStIDRFlag());
                //j2.setStSummaryFlag("Y");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                S.store(j2); 

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveJournalEntryPengembanganBisnis(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        Long lgHeaderAccountID = header.getLgAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        //if (!reversing)
        //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
            header.setStTransactionNo(trxNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                JournalView j2 = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("PB");

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                JournalView viuw = (JournalView) l.get(i);
                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    //j.setStTransactionNo(generateTransactionNo(header,l));
                    j2.setStTransactionNo(trxNo);
                    j2.setLgAccountID(header.getLgAccountIDMaster());
                    debitOld = j2.getDbDebit();
                    j2.setDbDebit(j2.getDbCredit());
                    j2.setDbCredit(debitOld);
                    //j2.setStDescription(header.getStDescriptionMaster());
                    debitEnteredOld = j2.getDbEnteredDebit();
                    j2.setDbEnteredDebit(j2.getDbEnteredCredit());
                    j2.setDbEnteredCredit(debitEnteredOld);
                    //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    //if (per==null) throw new Exception("Periods not being setup correctly !");
                    //j.setLgPeriodNo(per.getLgPeriodNo());
                    //j.setLgFiscalYear(per.getLgFiscalYear());
                }

                if (j2.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                JournalView viuw2 = (JournalView) l.get(i);
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveEditPengembanganBisnis(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        if (!reversing) {
            if (Tools.compare(bal, BDUtil.zero) != 0) {
                throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTransactionNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                //j.setStTransactionNo(stTransactionNo);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("PB");

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveJournalSyariah(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalSyariahView fj = (JournalSyariahView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        String stTransactionNo = null;

        if (header.isNew()) {
            stTransactionNo = generateSyariahTransactionNo(header, l, transNo);
            header.setStTransactionNo(stTransactionNo);
        }

        boolean reversing = fj.getStReverseFlag() != null;

        //BigDecimal bal = GLUtil.getBalance(l);

        //if (!reversing)
        //if (Tools.compare(bal,BDUtil.zero)!=0)throw new RuntimeException("Inbalanced jounal (difference = "+bal+")\n "+l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHSY"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalSyariahView j = (JournalSyariahView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLESY")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }


    public String generateSyariahTransactionNo(JournalHeaderView header, DTOList l, String transNo) throws Exception {


        String transactionNo = "";
        final String ccCode = Tools.getDigitRightJustified(header.getStCostCenter(), 2);
        final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(), 1);

        String counterKey =
                header.getLgFiscalYear().toString().substring(1)
                + header.getLgPeriodNo().toString();

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO_SY" + counterKey + ccCode, 1));

        //rn = Tools.getDigitRightJustified(rn,3);
        rn = StringTools.leftPad(rn, '0', 5);

        //110002700400
        //012345678901
        //no
        //  A0901171000000
        //  01234567890123
        //    A0910202700002

        transactionNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + rn;

        return transactionNo;

    }

    public DTOList getSyariahJournalEntry(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      gl_je_detail_syariah a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id=? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                JournalSyariahView.class);
    }

    
    public void save2(AccountView2 account) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (account.isNew()) {
                account.setStAccountID(String.valueOf(IDFactory.createNumericID("ACC")));
                account.setDbBalanceOpen(BDUtil.one);
            }

            account.setStDeleted(account.getStDeleted());

            S.store(account);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void saveCurrency(GLCurrencyHistoryView currency) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (currency.isNew()) {
                currency.setStCurrencyHistID(String.valueOf(IDFactory.createNumericID("CURR")));
            }

            S.store(currency);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

   public String save(ARInvestmentDepositoView deposito) throws Exception {

        //DateTime startDate = new DateTime(deposito.getDtTglawal());
        //DateTime endDate = new DateTime(deposito.getDtTglakhir());

        //boolean checkPeriod = DateUtil.getDateStr(deposito.getDtTglawal(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        boolean checkPeriod = Tools.compare(deposito.getDtTglawal(), new Date()) >= 0;
        if (checkPeriod) {
            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
        }

        //boolean checkPeriod2 = Tools.compare(deposito.getDtTglakhir(), new Date()) <= 0;
        //if (checkPeriod2) throw new RuntimeException("Tanggal Akhir Sudah Lewat");

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
                deposito.setStStatus("DEPOSITO");
                deposito.setStKeterangan("Pembentukan");
                deposito.setStJournalStatus("NEW");
                deposito.setStKonter("0");
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("N");
                deposito.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtCreateDate(new Date());

                deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
                deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));
                BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                //logger.logDebug("###############  "+bunga1);
            }

            //buktib = generateNoBuktiDeposito(deposito);
            //register = generateRegisterBentuk(deposito);

            if (deposito.getStBuktiB().length() <= 14) {
                deposito.setStBuktiB(deposito.getStBuktiB());

                deposito.setStRegister(
                        deposito.getStBuktiB().substring(1, 7) + "00"
                        + deposito.getStBuktiB().substring(11, 14));
            } else {
                deposito.setStBuktiB(
                        deposito.getStBuktiB().substring(0, 1)
                        + DateUtil.getYear2Digit(deposito.getDtTglmuta())
                        + DateUtil.getMonth2Digit(deposito.getDtTglmuta())
                        + deposito.getStBuktiB().substring(5, 9)
                        + deposito.getAccounts().getStAccountNo().substring(5, 10)
                        + deposito.getStBuktiB().substring(14, 19));

                deposito.setStRegister(
                        deposito.getStBuktiB().substring(1, 7)
                        + deposito.getStBuktiB().substring(14, 19));
            }

            if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                deposito.setStHari(deposito.getStHari());
                deposito.setStBulan("0");
            } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                deposito.setStHari("0");
                deposito.setStBulan(deposito.getStBulan());
            }

            deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
            deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));

            deposito.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            deposito.setDtChangeDate(new Date());

            BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

            S.store(deposito);

            final DTOList cair = deposito.getPencairan();

            for (int i = 0; i < cair.size(); i++) {
                ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

                if (pcr.isNew()) {
                    pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                    pcr.setStCreateWho(deposito.getStCreateWho());
                    pcr.setDtCreateDate(deposito.getDtCreateDate());
                    pcr.setStActiveFlag("Y");
                    pcr.setStEffectiveFlag("N");
                }

                pcr.setStARDepoID(deposito.getStARDepoID());
                pcr.setStNodefo(deposito.getStNodefo());
                pcr.setStBuktiB(deposito.getStBuktiB());
                pcr.setStHari(deposito.getStHari());
                pcr.setStBulan(deposito.getStBulan());
                pcr.setStCostCenterCode(deposito.getStCostCenterCode());
                pcr.setStReceiptClassID(deposito.getStReceiptClassID());
                pcr.setStCompanyType(deposito.getStCompanyType());
                pcr.setStCurrency(deposito.getStCurrency());
                pcr.setDbCurrencyRate(deposito.getDbCurrencyRate());
                pcr.setDbPajak(deposito.getDbPajak());
                pcr.setDbBunga(deposito.getDbBunga());
                pcr.setDbNominal(deposito.getDbNominal());
                pcr.setDbNominalKurs(deposito.getDbNominalKurs());
                pcr.setDtTglawal(deposito.getDtTglawal());
                pcr.setDtTglakhir(deposito.getDtTglakhir());
                pcr.setDtTgldepo(deposito.getDtTgldepo());
                pcr.setDtTglmuta(deposito.getDtTglmuta());
                pcr.setStRegisterBentuk(deposito.getStRegister());
                pcr.setStKonter(deposito.getStKonter());
                pcr.setStKodedepo(deposito.getStKodedepo());
                pcr.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                pcr.setStDepoName(deposito.getStDepoName());
                pcr.setStAccountDepo(deposito.getStAccountDepo());
                pcr.setStNoRekening(deposito.getStNoRekening());
                pcr.setStMonths(null);
                pcr.setStYears(null);
                pcr.setStActiveCairFlag("Y");
            }

            S.store(cair);

            //simpan ke ar_inv_perpanjangan
//            final DTOList renewal = deposito.getPerpanjangan2();
//
//            for (int i = 0; i < renewal.size(); i++) {
//                ARInvestmentPerpanjanganView rnl = (ARInvestmentPerpanjanganView) renewal.get(i);
//
//                if (rnl.isNew()) {
//                    rnl.setStARRenewalID(String.valueOf(IDFactory.createNumericID("INVRENEWAL")));
//                    rnl.setStCreateWho(deposito.getStCreateWho());
//                    rnl.setDtCreateDate(deposito.getDtCreateDate());
//                    rnl.setStActiveFlag("Y");
//                    rnl.setStEffectiveFlag("Y");
//                    rnl.setStStatus("DEPOSITO");
//                    rnl.setStJournalStatus("NEW");
//                    rnl.setStKonter("0");
//                }
//
//                rnl.setStARDepoID(deposito.getStARDepoID());
//                rnl.setStNodefo(deposito.getStNodefo());
//                rnl.setStBuktiB(deposito.getStBuktiB());
//                rnl.setStHari(deposito.getStHari());
//                rnl.setStBulan(deposito.getStBulan());
//                rnl.setStCostCenterCode(deposito.getStCostCenterCode());
//                rnl.setStReceiptClassID(deposito.getStReceiptClassID());
//                rnl.setStCompanyType(deposito.getStCompanyType());
//                rnl.setStCurrency(deposito.getStCurrency());
//                rnl.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                rnl.setDbPajak(deposito.getDbPajak());
//                rnl.setDbBunga(deposito.getDbBunga());
//                rnl.setDbNominal(deposito.getDbNominal());
//                rnl.setDbNominalKurs(deposito.getDbNominalKurs());
//                rnl.setDtTglawal(deposito.getDtTglawal());
//                rnl.setDtTglakhir(deposito.getDtTglakhir());
//                rnl.setDtTgldepo(deposito.getDtTgldepo());
//                rnl.setDtTglmuta(deposito.getDtTglmuta());
//                rnl.setStRegister(deposito.getStRegister());
//                rnl.setStKonter(deposito.getStKonter());
//                rnl.setStKodedepo(deposito.getStKodedepo());
//                rnl.setStEntityID(deposito.getStEntityID());
//                rnl.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                rnl.setStDepoName(deposito.getStDepoName());
//                rnl.setStBankName(deposito.getStBankName());
//                rnl.setStAccountDepo(deposito.getStAccountDepo());
//                rnl.setStAccountBank(deposito.getStAccountBank());
//                rnl.setStNoRekening(deposito.getStNoRekening());
//                rnl.setStMonths(deposito.getStMonths());
//                rnl.setStYears(deposito.getStYears());
//                rnl.setStKeterangan(deposito.getStKeterangan());
//
//            }
//
//            S.store(renewal);
            //end of simpan ke ar_inv_perpanjangan

            Date policyDateStart = deposito.getDtTglawal();
            Date policyDateEnd = deposito.getDtTglakhir();

            DateTime startDate = new DateTime(policyDateStart);
            DateTime endDate = new DateTime(policyDateEnd);

            Months y = Months.monthsBetween(startDate, endDate);
            int month = y.getMonths();

            int bulan = Integer.parseInt(deposito.getStBulan());

            Date policyDateEnd2 = advanceMonth(policyDateStart, 1);

            if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                if (bulan > 1) {
                    for (int j = 0; j < bulan; j++) {

                        final DTOList bunga = deposito.getBunga();

                        for (int i = 0; i < bunga.size(); i++) {
                            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                            if (bng.isNew()) {
                                bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                                bng.setStCreateWho(deposito.getStCreateWho());
                                bng.setDtCreateDate(deposito.getDtCreateDate());
                            }

                            //bng.markNew();

                            //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                            bng.setStARDepoID(deposito.getStARDepoID());
                            bng.setStNodefo(deposito.getStNodefo());
                            bng.setStNoBuktiB(deposito.getStBuktiB());
                            bng.setStCostCenterCode(deposito.getStCostCenterCode());
                            bng.setStReceiptClassID(deposito.getStReceiptClassID());
                            bng.setStKodedepo(deposito.getStKodedepo());
                            bng.setStRegisterBentuk(deposito.getStRegister());
                            bng.setStDepoName(deposito.getStDepoName());
                            bng.setStCurrency(deposito.getStCurrency());
                            bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                            bng.setDbNominal(deposito.getDbNominal());
                            bng.setDbNominalKurs(deposito.getDbNominalKurs());
                            bng.setDbPersen(deposito.getDbBunga());
                            bng.setDbPajak(deposito.getDbPajak());
                            bng.setDtTglAwal(advanceMonth(policyDateStart, j));
                            bng.setDtTglAkhir(advanceMonth(policyDateEnd2, j));
                            bng.setStCompanyType(deposito.getStCompanyType());
                            bng.setStActiveFlag("Y");
                            bng.setStEffectiveFlag("N");
                            bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                            bng.setStDepoName(deposito.getStDepoName());
                            bng.setStAccountDepo(deposito.getStAccountDepo());
                            bng.setStNoRekening(deposito.getStNoRekening());
                            bng.setDbAngka1(bunga1);
                            bng.setStMonths(null);
                            bng.setStYears(null);
                            if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                                bng.setStHari(deposito.getStHari());
                            } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                                bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                            }

                        }
                        S.store(bunga);
                    }

                } else if (bulan == 1) {

                    final DTOList bunga = deposito.getBunga();

                    for (int i = 0; i < bunga.size(); i++) {
                        ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                        if (bng.isNew()) {
                            bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                            bng.setStCreateWho(deposito.getStCreateWho());
                            bng.setDtCreateDate(deposito.getDtCreateDate());
                        }

                        //bng.markNew();

                        //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                        bng.setStARDepoID(deposito.getStARDepoID());
                        bng.setStNodefo(deposito.getStNodefo());
                        bng.setStNoBuktiB(deposito.getStBuktiB());
                        bng.setStCostCenterCode(deposito.getStCostCenterCode());
                        bng.setStReceiptClassID(deposito.getStReceiptClassID());
                        bng.setStKodedepo(deposito.getStKodedepo());
                        bng.setStRegisterBentuk(deposito.getStRegister());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStCurrency(deposito.getStCurrency());
                        bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                        bng.setDbNominal(deposito.getDbNominal());
                        bng.setDbNominalKurs(deposito.getDbNominalKurs());
                        bng.setDbPersen(deposito.getDbBunga());
                        bng.setDbPajak(deposito.getDbPajak());
                        bng.setDtTglAwal(deposito.getDtTglawal());
                        bng.setDtTglAkhir(deposito.getDtTglakhir());
                        bng.setStCompanyType(deposito.getStCompanyType());
                        bng.setStActiveFlag("Y");
                        bng.setStEffectiveFlag("N");
                        bng.setStMonths(null);
                        bng.setStYears(null);
                        bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStAccountDepo(deposito.getStAccountDepo());
                        bng.setStNoRekening(deposito.getStNoRekening());
                        bng.setDbAngka1(bunga1);
                        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                            bng.setStHari(deposito.getStHari());
                        } else {
                            bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                        }
                    }

                    S.store(bunga);
                }
            } else if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                final DTOList bunga = deposito.getBunga();

                for (int i = 0; i < bunga.size(); i++) {
                    ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                    if (bng.isNew()) {
                        bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                        bng.setStCreateWho(deposito.getStCreateWho());
                        bng.setDtCreateDate(deposito.getDtCreateDate());
                    }

                    //bng.markNew();

                    //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                    bng.setStARDepoID(deposito.getStARDepoID());
                    bng.setStNodefo(deposito.getStNodefo());
                    bng.setStNoBuktiB(deposito.getStBuktiB());
                    bng.setStCostCenterCode(deposito.getStCostCenterCode());
                    bng.setStReceiptClassID(deposito.getStReceiptClassID());
                    bng.setStKodedepo(deposito.getStKodedepo());
                    bng.setStRegisterBentuk(deposito.getStRegister());
                    bng.setStDepoName(deposito.getStDepoName());
                    bng.setStCurrency(deposito.getStCurrency());
                    bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                    bng.setDbNominal(deposito.getDbNominal());
                    bng.setDbNominalKurs(deposito.getDbNominalKurs());
                    bng.setDbPersen(deposito.getDbBunga());
                    bng.setDbPajak(deposito.getDbPajak());
                    bng.setDtTglAwal(deposito.getDtTglawal());
                    bng.setDtTglAkhir(deposito.getDtTglakhir());
                    bng.setStCompanyType(deposito.getStCompanyType());
                    bng.setStActiveFlag("Y");
                    bng.setStEffectiveFlag("N");
                    bng.setStMonths(null);
                    bng.setStYears(null);
                    bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                    bng.setStDepoName(deposito.getStDepoName());
                    bng.setStAccountDepo(deposito.getStAccountDepo());
                    bng.setStNoRekening(deposito.getStNoRekening());
                    bng.setDbAngka1(bunga1);
                    if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                        bng.setStHari(deposito.getStHari());
                    } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                        bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                    }

                }
                S.store(bunga);

            }

//            final DTOList panjang = deposito.getPerpanjangan();
//
//            for (int i = 0; i < panjang.size(); i++) {
//                ARInvestmentDepositoView pjg = (ARInvestmentDepositoView) panjang.get(i);
//
//                pjg.markUpdate();
//                pjg.setStNodefo(deposito.getStNodefo());
//                pjg.setStBuktiB(deposito.getStBuktiB());
//                pjg.setStHari(deposito.getStHari());
//                pjg.setStBulan(deposito.getStBulan());
//                pjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                pjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                pjg.setStCompanyType(deposito.getStCompanyType());
//                pjg.setStCurrency(deposito.getStCurrency());
//                pjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                pjg.setDbPajak(deposito.getDbPajak());
//                pjg.setDbBunga(deposito.getDbBunga());
//                pjg.setDbNominal(deposito.getDbNominal());
//                pjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                pjg.setStRegister(deposito.getStRegister());
//                pjg.setStKodedepo(deposito.getStKodedepo());
//                pjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                pjg.setStDepoName(deposito.getStDepoName());
//                pjg.setStAccountDepo(deposito.getStAccountDepo());
//                pjg.setStEntityID(deposito.getStEntityID());
//                pjg.setStBankName(deposito.getStBankName());
//                pjg.setStAccountBank(deposito.getStAccountBank());
//                pjg.setStNoRekening(deposito.getStNoRekening());
//            }
//
//            S.store(panjang);
//
//            final DTOList cairpanjang = deposito.getCairnodefo();
//
//            for (int i = 0; i < cairpanjang.size(); i++) {
//                ARInvestmentPencairanView cairpjg = (ARInvestmentPencairanView) cairpanjang.get(i);
//
//                cairpjg.markUpdate();
//                cairpjg.setStNodefo(deposito.getStNodefo());
//                cairpjg.setStBuktiB(deposito.getStBuktiB());
//                cairpjg.setStHari(deposito.getStHari());
//                cairpjg.setStBulan(deposito.getStBulan());
//                cairpjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                cairpjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                cairpjg.setStCompanyType(deposito.getStCompanyType());
//                cairpjg.setStCurrency(deposito.getStCurrency());
//                cairpjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                cairpjg.setDbPajak(deposito.getDbPajak());
//                cairpjg.setDbBunga(deposito.getDbBunga());
//                cairpjg.setDbNominal(deposito.getDbNominal());
//                cairpjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                cairpjg.setStRegisterBentuk(deposito.getStRegister());
//                cairpjg.setStKodedepo(deposito.getStKodedepo());
//                cairpjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                cairpjg.setStDepoName(deposito.getStDepoName());
//                cairpjg.setStAccountDepo(deposito.getStAccountDepo());
//                cairpjg.setStNoRekening(deposito.getStNoRekening());
//                cairpjg.setStMonths(null);
//                cairpjg.setStYears(null);
//                cairpjg.setStActiveCairFlag("Y");
//            }
//
//            S.store(cairpanjang);
//
//            final DTOList bungapanjang = deposito.getBunganodefo();
//
//            for (int i = 0; i < bungapanjang.size(); i++) {
//                ARInvestmentBungaView bngpjg = (ARInvestmentBungaView) bungapanjang.get(i);
//
//                bngpjg.markUpdate();
//                bngpjg.setStNodefo(deposito.getStNodefo());
//                bngpjg.setStNoBuktiB(deposito.getStBuktiB());
//                bngpjg.setStHari(deposito.getStHari());
//                bngpjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                bngpjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                bngpjg.setStCompanyType(deposito.getStCompanyType());
//                bngpjg.setStCurrency(deposito.getStCurrency());
//                bngpjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                bngpjg.setDbPajak(deposito.getDbPajak());
//                bngpjg.setDbPersen(deposito.getDbBunga());
//                bngpjg.setDbNominal(deposito.getDbNominal());
//                bngpjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                bngpjg.setStRegisterBentuk(deposito.getStRegister());
//                bngpjg.setStKodedepo(deposito.getStKodedepo());
//                bngpjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                bngpjg.setStDepoName(deposito.getStDepoName());
//                bngpjg.setStAccountDepo(deposito.getStAccountDepo());
//                bngpjg.setStNoRekening(deposito.getStNoRekening());
//                bngpjg.setStMonths(null);
//                bngpjg.setStYears(null);
//            }
//
//            S.store(bungapanjang);

            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public ARInvestmentDepositoView loadDeposito(String ardepoid) throws Exception {
        final ARInvestmentDepositoView depo = (ARInvestmentDepositoView) ListUtil.getDTOListFromQuery(
                "select * from ar_inv_deposito where ar_depo_id = ? and deleted is null",
                new Object[]{ardepoid},
                ARInvestmentDepositoView.class).getDTO();

        return depo;
    }

    public ARInvestmentPencairanView loadPencairan(String arcairid) throws Exception {
        final ARInvestmentPencairanView depo = (ARInvestmentPencairanView) ListUtil.getDTOListFromQuery(
                "select * from ar_inv_pencairan where ar_cair_id = ? and deleted is null ",
                new Object[]{arcairid},
                ARInvestmentPencairanView.class).getDTO();

        return depo;
    }

     public ARInvestmentBungaView loadBunga(String arbungaid) throws Exception {
        final ARInvestmentBungaView depo = (ARInvestmentBungaView) ListUtil.getDTOListFromQuery(
                "select * from ar_inv_bunga where ar_bunga_id = ? and delete_flag is null ",
                new Object[]{arbungaid},
                ARInvestmentBungaView.class).getDTO();

        return depo;
    }

    public String savePencairan(ARInvestmentPencairanView pencairan) throws Exception {
        //logger.logDebug("save: "+deposito);
        /*
        String buktic = generateNoBuktiC(pencairan);
        String register = generateRegisterCair(pencairan);
         */

        if (pencairan.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pencairan.getStMonths() + " dan tahun " + pencairan.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {
            if (pencairan.isNew()) {
                pencairan.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
            }

            //pencairan.generateNoBuktiC();
            //pencairan.generateRegisterCair();

            if (pencairan.getStBuktiC() == null) {
                pencairan.generateNoBuktiC();
                pencairan.generateRegisterCair();
                pencairan.setStJournalStatus("NEW");
            } else {
                pencairan.setStBuktiC(
                        pencairan.getStBuktiC().substring(0, 1)
                        + DateUtil.getYear2Digit(pencairan.getDtTglCair())
                        + DateUtil.getMonth2Digit(pencairan.getDtTglCair())
                        + pencairan.getStBuktiC().substring(5, 9)
                        + pencairan.getAccounts().getStAccountNo().substring(5, 10)
                        + pencairan.getStBuktiC().substring(14, 19));
            }

            pencairan.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            pencairan.setDtChangeDate(new Date());
            pencairan.setStType(pencairan.getStType());

            pencairan.setStAccountBank(pencairan.findAccountBank(pencairan.getStEntityID()));
            pencairan.setStKeterangan("Pencairan Deposito di " + pencairan.getStBankName());

            S.store(pencairan);

            return pencairan.getStARCairID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public void saveBunga(ARInvestmentBungaView bunga) throws Exception {

        //String register = generateRegisterBunga(bunga);

        //if (bunga.isHaveBunga()) {
        //    throw new RuntimeException("Transaksi Bunga untuk bulan " + bunga.getStMonths() + " dan tahun " + bunga.getStYears() + " sudah terinput");
        //}

        if (bunga.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + bunga.getStMonths() + " dan tahun " + bunga.getStYears() + " tsb sudah diposting");
        }

        SQLUtil S = new SQLUtil();

        try {
            if (bunga.isNew()) {
                bunga.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
            }

            //bunga.generateRegisterBunga();

            if (bunga.getStRegister() == null) {
                bunga.generateNoBuktiD();
                bunga.generateRegisterBunga();
            } else {
                bunga.setStNoBuktiD(
                        bunga.getStNoBuktiD().substring(0, 1)
                        + DateUtil.getYear2Digit(bunga.getDtTglBunga())
                        + DateUtil.getMonth2Digit(bunga.getDtTglBunga())
                        + bunga.getStNoBuktiD().substring(5, 9)
                        + bunga.getAccounts().getStAccountNo().substring(5, 10)
                        + bunga.getStNoBuktiD().substring(14, 19));
            }

            bunga.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            bunga.setDtChangeDate(new Date());

            bunga.setStAccountBank(bunga.findAccountBank(bunga.getStEntityID()));
            bunga.reCalculate();

            S.store(bunga);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void approve(ARInvestmentDepositoView deposito, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final ARInvestmentDepositoView fj = (ARInvestmentDepositoView) l.get(0);

        String stTransactionHeaderID = null;

        Date applyDate = new Date();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) l.get(i);

                //logger.logDebug("########## saveJournalEntry: "+depo.getStARDepoID());
                if (Tools.isEqual(DateUtil.getDateStr(depo.getDtTglmuta(), "dd ^^ yyyy"),
                        DateUtil.getDateStr(depo.getDtTgldepo(), "dd ^^ yyyy"))) {
                    applyDate = depo.getDtTglmuta();
                } else {
                    applyDate = depo.getDtTgldepo();
                }

                depo.markUpdate();
                depo.setStEffectiveFlag("Y");
                depo.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
                depo.setDtApprovedDate(new Date());
                S.store(depo);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(depo.getStEntityID()));
                j.setStHeaderAccountNo(depo.getAccounts().getStAccountNo());
                j.setLgAccountID(new Long(depo.getStNoRekeningDeposito()));
                j.setDbEnteredDebit(depo.getDbNominal());
                j.setDbEnteredCredit(BDUtil.zero);
                j.setDbDebit(depo.getDbNominal());
                j.setDbCredit(BDUtil.zero);
                j.setStDescription("Pembentukan Deposito No: " + depo.getStNodefo() + " " + depo.getStDepoName());
                j.setStCurrencyCode(depo.getStCurrency());
                j.setDbCurrencyRate(depo.getDbCurrencyRate());
                j.setStRefTrxNo(depo.getStARDepoID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(depo.getStBuktiB());
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(applyDate);
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("INV_DEPOSITO");
                j.setDtApplyDate(applyDate);

                //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(depo.getStBuktiB());
                    j2.setLgHeaderAccountID(new Long(depo.getStEntityID()));
                    j2.setStHeaderAccountNo(depo.getAccounts().getStAccountNo());
                    j2.setLgAccountID(new Long(depo.getStEntityID()));
                    j2.setDbDebit(BDUtil.zero);
                    j2.setDbCredit(depo.getDbNominal());
                    j2.setDbEnteredDebit(BDUtil.zero);
                    j2.setDbEnteredCredit(depo.getDbNominal());
                    j2.setStDescription("Pembentukan Deposito No: " + depo.getStNodefo() + " " + depo.getStBankName());
                    j2.setStCurrencyCode(depo.getStCurrency());
                    j2.setDbCurrencyRate(depo.getDbCurrencyRate());
                    j2.setStRefTrxNo(depo.getStARDepoID());
                    j2.setStIDRFlag("Y");

                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + depo.getDtTglawal() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("INV_DEPOSITO");
                j2.setDtApplyDate(applyDate);

                /*
                logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                logger.logDebug("################### "+j2.getLgHeaderAccountID());
                logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                logger.logDebug("################### "+j2.getLgAccountID());
                 */

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

                if (!depo.isIjinPusat()) {
                    if (Tools.compare(depo.getDbNominalDana(), new BigDecimal(0)) > 0) {
                        Date applyDateDana = depo.getDtTansferDate();

                        JournalView j3 = new JournalView();
                        j3.markNew();

                        JournalView j4 = new JournalView();
                        j4.markNew();

                        j3.setStTransactionHeaderID(stTransactionHeaderID);
                        j3.setLgHeaderAccountID(new Long(depo.getStKdBankDana()));
                        j3.setStHeaderAccountNo(depo.getAccountsDana().getStAccountNo());
                        j3.setLgAccountID(new Long(depo.findAccount("210000000005", depo.getStCostCenterCode())));
                        j3.setDbDebit(depo.getDbNominalDana());
                        j3.setDbCredit(BDUtil.zero);
                        j3.setDbEnteredDebit(depo.getDbNominalDana());
                        j3.setDbEnteredCredit(BDUtil.zero);
                        j3.setStDescription("Transfer Dana No: " + depo.getStNodefo() + " " + depo.getStDepoName());
                        j3.setStCurrencyCode(depo.getStCurrency());
                        j3.setDbCurrencyRate(depo.getDbCurrencyRate());
                        j3.setStRefTrxNo(depo.getStARDepoID());
                        j3.setStIDRFlag("Y");

                        j3.reCalculate();

                        if (j3.isNew()) {
                            j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                            j3.setStTransactionNo(depo.getStBuktiB());
                        }

                        if (applyDateDana == null) {
                            throw new Exception("Apply Date is not defined !");
                        }
                        final PeriodDetailView period = PeriodManager.getInstance().getPeriodFromDate(applyDateDana);
                        if (period == null) {
                            throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j3.getDtApplyDate() + ")");
                        }
                        if (!period.isOpen()) {
                            throw new Exception("Period is not open");
                        }
                        j3.setLgPeriodNo(period.getLgPeriodNo());
                        j3.setLgFiscalYear(period.getLgFiscalYear());
                        j3.setStRefTrxType("CASHBANK");
                        j3.setDtApplyDate(applyDateDana);

                        //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                        //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                        //logger.logDebug("################### "+j3.getLgAccountID());

                        if (j3.isModified()) {

                            final JournalView old = j3.getOldJournal();

                            if (old != null) {

                                final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                        && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                                if (isSameRecord) {
                                    final BigDecimal am = j3.getDbAdjustmentAmount();

                                    if (am.compareTo(BDUtil.zero) != 0) {
                                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                        //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
                                    }
                                } else {
                                    updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                                }
                            } else {
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        }

                        S.store(j3);
                        //simpen lawan

                        if (j4.isNew()) {
                            j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                            j4.setStTransactionHeaderID(stTransactionHeaderID);
                            j4.setStTransactionNo(depo.getStBuktiB());
                            j4.setLgHeaderAccountID(new Long(depo.getStKdBankDana()));
                            j4.setStHeaderAccountNo(depo.getAccountsDana().getStAccountNo());
                            j4.setLgAccountID(new Long(depo.getStKdBankDana()));
                            j4.setDbDebit(BDUtil.zero);
                            j4.setDbCredit(depo.getDbNominalDana());
                            j4.setDbEnteredDebit(BDUtil.zero);
                            j4.setDbEnteredCredit(depo.getDbNominalDana());
                            j4.setStDescription("Transfer Dana No: " + depo.getStNodefo() + " " + depo.getStBankNameDana());
                            j4.setStCurrencyCode(depo.getStCurrency());
                            j4.setDbCurrencyRate(depo.getDbCurrencyRate());
                            j4.setStRefTrxNo(depo.getStARDepoID());
                            j4.setStIDRFlag("Y");
                        }

                        if (applyDateDana == null) {
                            throw new Exception("Apply Date is not defined !");
                        }
                        // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j4.getDtApplyDate());
                        if (period == null) {
                            throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + applyDateDana + ")");
                        }
                        if (!period.isOpen()) {
                            throw new Exception("Period is not open");
                        }
                        j4.setLgPeriodNo(period.getLgPeriodNo());
                        j4.setLgFiscalYear(period.getLgFiscalYear());
                        j4.setStRefTrxType("CASHBANK");
                        j4.setDtApplyDate(applyDateDana);

                        /*
                        logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                        logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                        logger.logDebug("################### "+j4.getLgHeaderAccountID());
                        logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                        logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                        logger.logDebug("################### "+j4.getLgAccountID());
                         */

                        if (j4.isModified()) {

                            final JournalView old = j4.getOldJournal();

                            if (old != null) {

                                final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                        && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                                if (isSameRecord) {
                                    final BigDecimal am = j4.getDbAdjustmentAmount();

                                    if (am.compareTo(BDUtil.zero) != 0) {
                                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                        //GLBalanceManager.getInstance().updateBalance(j4.getLgAccountID(), j4.getLgPeriodNo(), am);
                                    }
                                } else {
                                    updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                                }
                            } else {
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        }

                        S.store(j4);
                    }
                }
            }

            approvePerpanjangan(deposito, l);
            //akhir simpen lawan

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void approve(ARInvestmentPencairanView pencairan, DTOList l) throws Exception {
        //logger.logDebug("########## saveJournalEntry: "+l.size());

        if (pencairan.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pencairan.getStMonths() + " dan tahun " + pencairan.getStYears() + " tsb sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final ARInvestmentPencairanView fj = (ARInvestmentPencairanView) l.get(0);

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                ARInvestmentPencairanView cair = (ARInvestmentPencairanView) l.get(i);

                cair.markUpdate();
                cair.setStEffectiveFlag("Y");
                cair.setStActiveCairFlag("Y");
                cair.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
                cair.setDtApprovedDate(new Date());
                S.store(cair);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(cair.getStEntityID()));
                j.setStHeaderAccountNo(cair.getAccounts().getStAccountNo());
                j.setLgAccountID(new Long(cair.getStNoRekeningDeposito()));
                j.setDbEnteredCredit(cair.getDbNominal());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbCredit(cair.getDbNominal());
                j.setDbDebit(BDUtil.zero);
                j.setStDescription("Pencairan Deposito No: " + cair.getStNodefo() + " " + cair.getStDepoName());
                j.setStCurrencyCode(cair.getStCurrency());
                j.setDbCurrencyRate(cair.getDbCurrencyRate());
                j.setStRefTrxNo(cair.getStARCairID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(cair.getStBuktiC());
                }

                if (cair.getDtTglCair() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(cair.getDtTglCair());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("INV_PENCAIRAN");
                j.setDtApplyDate(cair.getDtTglCair());

                //logger.logDebug("******************* "+new Long(cair.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(cair.getStBuktiC());
                    j2.setLgHeaderAccountID(new Long(cair.getStEntityID()));
                    j2.setStHeaderAccountNo(cair.getAccounts().getStAccountNo());
                    j2.setLgAccountID(new Long(cair.getStEntityID()));
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbDebit(cair.getDbNominal());
                    j2.setDbEnteredCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(cair.getDbNominal());
                    j2.setStDescription("Pencairan Deposito No: " + cair.getStNodefo() + " " + cair.getStBankName());
                    j2.setStCurrencyCode(cair.getStCurrency());
                    j2.setDbCurrencyRate(cair.getDbCurrencyRate());
                    j2.setStRefTrxNo(cair.getStARCairID());
                    j2.setStIDRFlag("Y");
                }

                if (cair.getDtTglCair() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + cair.getDtTglCair() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("INV_PENCAIRAN");
                j2.setDtApplyDate(cair.getDtTglCair());

                /*
                logger.logDebug("******************* "+new Long(cair.getStEntityID()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStEntityID());
                logger.logDebug("################### "+j2.getLgHeaderAccountID());
                logger.logDebug("******************* "+new Long(cair.getStNoRekeningDeposito()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStNoRekeningDeposito());
                logger.logDebug("################### "+j2.getLgAccountID());
                 */

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

//                if (Tools.compare(cair.getDbPinalty(), new BigDecimal(0)) > 0) {
                if (cair.getDbPinalty() != null) {
                    if (cair.getStType().equalsIgnoreCase("1")) {

                        JournalView j3 = new JournalView();
                        j3.markNew();

                        JournalView j4 = new JournalView();
                        j4.markNew();

                        j3.setStTransactionHeaderID(stTransactionHeaderID);
                        j3.setLgHeaderAccountID(new Long(cair.getStEntityID()));
                        j3.setStHeaderAccountNo(cair.getAccounts().getStAccountNo());

                        j3.setLgAccountID(new Long(cair.findAccountPinalty(cair.getStCostCenterCode())));
                        j3.setDbDebit(cair.getDbPinalty());
                        j3.setDbCredit(BDUtil.zero);
                        j3.setDbEnteredDebit(cair.getDbPinalty());
                        j3.setDbEnteredCredit(BDUtil.zero);
                        j3.setStDescription("Pinalty Pencairan No: " + cair.getStNodefo() + " " + cair.getStDepoName());

//                    else if (cair.getStType().equalsIgnoreCase("2")) {
//                        j3.setLgAccountID(new Long(cair.findAccountBunga(cair.getStCostCenterCode())));
//                        j3.setDbDebit(BDUtil.zero);
//                        j3.setDbCredit(cair.getDbPinalty());
//                        j3.setDbEnteredDebit(BDUtil.zero);
//                        j3.setDbEnteredCredit(cair.getDbPinalty());
//                        j3.setStDescription("Bunga Harian Pencairan No: " + cair.getStNodefo() + " " + cair.getStBankName());
//                    }

                        j3.setStCurrencyCode(cair.getStCurrency());
                        j3.setDbCurrencyRate(cair.getDbCurrencyRate());
                        j3.setStRefTrxNo(cair.getStARCairID());
                        j3.setStIDRFlag("Y");

                        j3.reCalculate();

                        if (j3.isNew()) {
                            j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                            j3.setStTransactionNo(cair.getStBuktiC());
                        }

                        if (cair.getDtTglCair() == null) {
                            throw new Exception("Apply Date is not defined !");
                        }
                        final PeriodDetailView period = PeriodManager.getInstance().getPeriodFromDate(cair.getDtTglCair());
                        if (period == null) {
                            throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j3.getDtApplyDate() + ")");
                        }
                        if (!period.isOpen()) {
                            throw new Exception("Period is not open");
                        }
                        j3.setLgPeriodNo(period.getLgPeriodNo());
                        j3.setLgFiscalYear(period.getLgFiscalYear());
                        j3.setStRefTrxType("INV_PENCAIRAN");
                        j3.setDtApplyDate(cair.getDtTglCair());

                        //logger.logDebug("******************* "+new Long(cair.getStNoRekeningDeposito()));
                        //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStNoRekeningDeposito());
                        //logger.logDebug("################### "+j3.getLgAccountID());

                        if (j3.isModified()) {

                            final JournalView old = j3.getOldJournal();

                            if (old != null) {

                                final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                        && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                                if (isSameRecord) {
                                    final BigDecimal am = j3.getDbAdjustmentAmount();

                                    if (am.compareTo(BDUtil.zero) != 0) {
                                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                        //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
                                    }
                                } else {
                                    updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                                }
                            } else {
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        }

                        S.store(j3);
                        //simpen lawan

                        if (j4.isNew()) {
                            j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                            j4.setStTransactionHeaderID(stTransactionHeaderID);
                            j4.setStTransactionNo(cair.getStBuktiC());
                            j4.setLgHeaderAccountID(new Long(cair.getStEntityID()));
                            j4.setStHeaderAccountNo(cair.getAccounts().getStAccountNo());
                            j4.setLgAccountID(new Long(cair.getStEntityID()));

                            j4.setDbDebit(BDUtil.zero);
                            j4.setDbCredit(cair.getDbPinalty());
                            j4.setDbEnteredDebit(BDUtil.zero);
                            j4.setDbEnteredCredit(cair.getDbPinalty());
                            j4.setStDescription("Pinalty Pencairan No: " + cair.getStNodefo() + " " + cair.getStBankName());

//                        else if (cair.getStType().equalsIgnoreCase("2")) {
//                            j4.setDbDebit(cair.getDbPinalty());
//                            j4.setDbCredit(BDUtil.zero);
//                            j4.setDbEnteredDebit(cair.getDbPinalty());
//                            j4.setDbEnteredCredit(BDUtil.zero);
//                            j4.setStDescription("Bunga Harian Pencairan No: " + cair.getStNodefo() + " " + cair.getStBankName());
//                        }

                            j4.setStCurrencyCode(cair.getStCurrency());
                            j4.setDbCurrencyRate(cair.getDbCurrencyRate());
                            j4.setStRefTrxNo(cair.getStARCairID());
                            j4.setStIDRFlag("Y");
                        }

                        if (cair.getDtTglCair() == null) {
                            throw new Exception("Apply Date is not defined !");
                        }
                        // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j4.getDtApplyDate());
                        if (period == null) {
                            throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + cair.getDtTglCair() + ")");
                        }
                        if (!period.isOpen()) {
                            throw new Exception("Period is not open");
                        }
                        j4.setLgPeriodNo(period.getLgPeriodNo());
                        j4.setLgFiscalYear(period.getLgFiscalYear());
                        j4.setStRefTrxType("INV_PENCAIRAN");
                        j4.setDtApplyDate(cair.getDtTglCair());

                        /*
                        logger.logDebug("******************* "+new Long(cair.getStEntityID()));
                        logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStEntityID());
                        logger.logDebug("################### "+j4.getLgHeaderAccountID());
                        logger.logDebug("******************* "+new Long(cair.getStNoRekeningDeposito()));
                        logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+cair.getStNoRekeningDeposito());
                        logger.logDebug("################### "+j4.getLgAccountID());
                         */

                        if (j4.isModified()) {

                            final JournalView old = j4.getOldJournal();

                            if (old != null) {

                                final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                        && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                                if (isSameRecord) {
                                    final BigDecimal am = j4.getDbAdjustmentAmount();

                                    if (am.compareTo(BDUtil.zero) != 0) {
                                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                        //GLBalanceManager.getInstance().updateBalance(j4.getLgAccountID(), j4.getLgPeriodNo(), am);
                                    }
                                } else {
                                    updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                                }
                            } else {
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        }

                        S.store(j4);
                    }
                }
            }
            //akhir simpen lawan

            //update tglcair
            final DTOList deposito = pencairan.getDepositoView();

            for (int t = 0; t < deposito.size(); t++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) deposito.get(t);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                depo.markUpdate();
                depo.setStARCairID(pencairan.getStARCairID());
                depo.setDtTglCair(pencairan.getDtTglCair());
                depo.setDtTglEntryCair(new Date());
            }

            S.store(deposito);

            final DTOList bunga = pencairan.getBunga();

            for (int m = 0; m < bunga.size(); m++) {
                ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(m);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                bng.markUpdate();
                bng.setStARCairID(pencairan.getStARCairID());
                bng.setDtTglCair(pencairan.getDtTglCair());
                bng.setDtTglEntryCair(new Date());
            }

            S.store(bunga);

            final DTOList renew = pencairan.getPerpanjangan();

            for (int n = 0; n < renew.size(); n++) {
                ARInvestmentPerpanjanganView ren = (ARInvestmentPerpanjanganView) renew.get(n);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                ren.markUpdate();
                ren.setStARCairID(pencairan.getStARCairID());
                ren.setDtTglCair(pencairan.getDtTglCair());
                ren.setDtTglEntryCair(new Date());
            }

            S.store(renew);

//            if (Tools.compare(pencairan.getDbPinalty(), new BigDecimal(0)) > 0) {
            if (pencairan.getDbPinalty() != null) {
                if (pencairan.getStType().equalsIgnoreCase("2")) {

                    ARInvestmentBungaView dep = new ARInvestmentBungaView();
                    dep.markNew();

                    Date policyDateStart = pencairan.getDtTglawal();
                    Date policyDateEnd = advanceMonth(policyDateStart, 1);

                    if (dep.isNew()) {
                        dep.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                        dep.setStActiveFlag("Y");
                        dep.setStCurrency("IDR");
                        dep.setDbCurrencyRate(BDUtil.one);
                        dep.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                        dep.setDtCreateDate(new Date());
                    }

                    dep.setStNodefo(pencairan.getStNodefo());
                    dep.setStNoRekeningDeposito(pencairan.getStNoRekeningDeposito());
                    dep.setStNoBuktiB(pencairan.getStBuktiB());
                    dep.setStAccountDepo(pencairan.getStAccountDepo());
                    dep.setStNoRekening(pencairan.getStNoRekening());
                    dep.setStARDepoID(pencairan.getStARDepoID());
                    dep.setStYears(pencairan.getStYears());
                    dep.setStMonths(pencairan.getStMonths());
                    dep.setDbPajak(pencairan.getDbPajak());
                    dep.setDtTglBunga(pencairan.getDtTglCair());
                    dep.setDbPersen(pencairan.getDbBunga());
                    dep.setDbNominalKurs(pencairan.getDbNominal());
                    dep.setDbNominal(pencairan.getDbNominal());
                    dep.setStCostCenterCode(pencairan.getStCostCenterCode());
                    dep.setStDepoName(pencairan.getAccountsInvest().getStDescription());
                    dep.setStKodedepo(pencairan.getStKodedepo());
                    dep.setStRegisterBentuk(pencairan.getStRegisterBentuk());
                    dep.setDtTglAwal(policyDateStart);
                    dep.setDtTglAkhir(policyDateEnd);
                    dep.setStReceiptClassID(pencairan.getStReceiptClassID());
                    dep.setStCompanyType(pencairan.getStCompanyType());
                    dep.setStHari(pencairan.getStHari());
                    if (dep.getStKodedepo().equalsIgnoreCase("1")) {
                        dep.setStHari(pencairan.getStHari());
                    } else if (dep.getStKodedepo().equalsIgnoreCase("2")) {
                        dep.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(pencairan.getStBulan()), new BigDecimal(30))));
                    }

                    BigDecimal angka = BDUtil.mul(dep.getDbNominal(), BDUtil.getRateFromPct(dep.getDbPersen()));
                    angka = BDUtil.div(angka, new BigDecimal(365));
                    BigDecimal prorate = BDUtil.mul(new BigDecimal(dep.getStHari()), new BigDecimal(0.8));

                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ bunga " + angka);
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ prorate " + prorate);

//                BigDecimal bunga1 = depo.reCalculateStart(depo.getStKodedepo(), depo.getStBulan(), depo.getDbNominal());
                    BigDecimal bunga1 = BDUtil.mulRound(angka, prorate, 2);
                    dep.setDbAngka1(bunga1);
                    dep.setDbAngka(pencairan.getDbPinalty());
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@ bunga1 " + bunga1);

                    dep.setStEntityID(pencairan.getStEntityID());
                    dep.setStAccountBank(pencairan.getStAccountBank());
                    dep.setStBankName(pencairan.getStBankName());
                    dep.setStNoBuktiC(pencairan.getStBuktiC());

//            bunga.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
//            bunga.setDtChangeDate(new Date());

                    S.store(dep);

                    saveJournalEntryIzinBunga(dep);
                }
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }


    public void approve(ARInvestmentBungaView bnga, DTOList l) throws Exception {
        //logger.logDebug("########## saveJournalEntry: "+l.size());

        //if (bnga.isHaveBunga()) {
        //    throw new RuntimeException("Transaksi Bunga untuk bulan " + bnga.getStMonths() + " dan tahun " + bnga.getStYears() + " sudah terinput");
        //}

        if (bnga.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + bnga.getStMonths() + " dan tahun " + bnga.getStYears() + " tsb sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final ARInvestmentBungaView fj = (ARInvestmentBungaView) l.get(0);

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                ARInvestmentBungaView bunga = (ARInvestmentBungaView) l.get(i);

                bunga.markUpdate();
                bunga.setStEffectiveFlag("Y");
                bunga.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
                bunga.setDtApprovedDate(new Date());
                S.store(bunga);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(bunga.getStEntityID()));
                j.setStHeaderAccountNo(bunga.getAccounts().getStAccountNo());
                j.setLgAccountID(new Long(bunga.findAccountBunga(bunga.getStCostCenterCode())));
                j.setDbEnteredCredit(bunga.getDbAngka());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbCredit(bunga.getDbAngka());
                j.setDbDebit(BDUtil.zero);
                j.setStDescription("Bunga Deposito No: " + bunga.getStNodefo() + " " + bunga.getStDepoName());
                j.setStCurrencyCode(bunga.getStCurrency());
                j.setDbCurrencyRate(bunga.getDbCurrencyRate());
                j.setStRefTrxNo(bunga.getStARBungaID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(bunga.getStNoBuktiD());
                }

                if (bunga.getDtTglBunga() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(bunga.getDtTglBunga());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("INV_BUNGA");
                j.setDtApplyDate(bunga.getDtTglBunga());

                //logger.logDebug("******************* "+new Long(bunga.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(bunga.getStNoBuktiD());
                    j2.setLgHeaderAccountID(new Long(bunga.getStEntityID()));
                    j2.setStHeaderAccountNo(bunga.getAccounts().getStAccountNo());
                    j2.setLgAccountID(new Long(bunga.getStEntityID()));
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbDebit(bunga.getDbAngka());
                    j2.setDbEnteredCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(bunga.getDbAngka());
                    j2.setStDescription("Bunga Deposito No: " + bunga.getStNodefo() + " " + bunga.getStBankName());
                    j2.setStCurrencyCode(bunga.getStCurrency());
                    j2.setDbCurrencyRate(bunga.getDbCurrencyRate());
                    j2.setStRefTrxNo(bunga.getStARBungaID());
                    j2.setStIDRFlag("Y");
                }

                if (bunga.getDtTglBunga() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + bunga.getDtTglBunga() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("INV_BUNGA");
                j2.setDtApplyDate(bunga.getDtTglBunga());

                /*
                logger.logDebug("******************* "+new Long(bunga.getStEntityID()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStEntityID());
                logger.logDebug("################### "+j2.getLgHeaderAccountID());
                logger.logDebug("******************* "+new Long(bunga.getStNoRekeningDeposito()));
                logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStNoRekeningDeposito());
                logger.logDebug("################### "+j2.getLgAccountID());
                 */

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }


   public void reverseDeposito(ARInvestmentDepositoView deposito) throws Exception {
        //CheckReverse(deposito.getStBuktiB());
        //CheckBunga(deposito.getStBuktiB());
        //CheckPencairan(deposito.getStBuktiB());

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_inv_deposito set effective_flag = 'N', approved_who = null, "
                    + "approved_date = null, admin_notes = 'REVERSE', reverse_date = now() "
                    + "where bukti_b = ? ");

            P.setObject(1, deposito.getStBuktiB());
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

            P2.setObject(1, deposito.getStBuktiB());
            int r2 = P2.executeUpdate();
            S.release();

            //PreparedStatement P3 = S.setQuery("update ar_inv_perpanjangan set effective_flag = 'N', approved_who = null, approved_date = null where ar_depo_id = ? ");
            PreparedStatement P3 = S.setQuery("delete from ar_inv_perpanjangan where bukti_b = ?");

            P3.setObject(1, deposito.getStBuktiB());
            int r3 = P3.executeUpdate();
            S.release();

            //PreparedStatement P3 = S.setQuery("update ar_inv_perpanjangan set effective_flag = 'N', approved_who = null, approved_date = null where ar_depo_id = ? ");
            PreparedStatement P4 = S.setQuery("delete from ar_inv_pencairan where bukti_b = ?");

            P4.setObject(1, deposito.getStBuktiB());
            int r4 = P4.executeUpdate();
            S.release();

            //PreparedStatement P3 = S.setQuery("update ar_inv_perpanjangan set effective_flag = 'N', approved_who = null, approved_date = null where ar_depo_id = ? ");
            PreparedStatement P5 = S.setQuery("delete from ar_inv_bunga where bukti_b = ?");

            P5.setObject(1, deposito.getStBuktiB());
            int r5 = P5.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void reverseBunga(ARInvestmentBungaView bunga) throws Exception {
        //CheckReverse(bunga.getStNoBuktiD());
        //CheckPencairan(bunga.getStNoBuktiB());

        if (bunga.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + bunga.getStMonths() + " dan tahun " + bunga.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_inv_bunga set effective_flag = 'N', approved_who = null, "
                    + "approved_date = null "
                    + "where ar_bunga_id = ?");

            P.setObject(1, bunga.getStARBungaID());
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

            P2.setObject(1, bunga.getStNoBuktiD());
            int r2 = P2.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void reversePencairan(ARInvestmentPencairanView pencairan) throws Exception {
        //CheckReverse(pencairan.getStBuktiC());

        if (pencairan.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pencairan.getStMonths() + " dan tahun " + pencairan.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_inv_pencairan set tglcair = null, bukti_c = null, register = null, "
                    + "type = null, pinalty = null, effective_flag = 'N', years = null, months = null, change_date = null, change_who = null, "
                    + "approved_who = null, approved_date = null, tglawaltrans = null, tglakhirtrans = null, "
                    + "admin_notes = 'REVERSE', reverse_date = now(), kdbank = null, account_bank = null, nama_bank = null "
                    + "where ar_cair_id = ?");

            P.setObject(1, pencairan.getStARCairID());
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

            P2.setObject(1, pencairan.getStBuktiC());
            int r2 = P2.executeUpdate();
            S.release();

            PreparedStatement P3 = S.setQuery("update ar_inv_bunga set tglcair = null, ar_cair_id = null, tgl_entrycair = null where ar_cair_id = ?");

            P3.setObject(1, pencairan.getStARCairID());
            int r3 = P3.executeUpdate();
            S.release();

            PreparedStatement P4 = S.setQuery("update ar_inv_deposito set tglcair = null, ar_cair_id = null, tglentrycair = null where ar_cair_id = ?");

            P4.setObject(1, pencairan.getStARCairID());
            int r4 = P4.executeUpdate();
            S.release();

            PreparedStatement P5 = S.setQuery("update ar_inv_perpanjangan set tglcair = null, ar_cair_id = null, tglentrycair = null where ar_cair_id = ?");

            P5.setObject(1, pencairan.getStARCairID());
            int r5 = P5.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }


    public Date advanceMonth(Date perDate, int addMonth) {

        //final int perlen = lgPeriodLength.intValue();

        final Calendar cld = Calendar.getInstance();

        cld.setTime(perDate);

        cld.add(Calendar.MONTH, addMonth);

        return cld.getTime();

    }

    public String saveWithoutJurnal(ARInvestmentDepositoView deposito) throws Exception {

        //DateTime startDate = new DateTime(deposito.getDtTglawal());
        //DateTime endDate = new DateTime(deposito.getDtTglakhir());

        //boolean checkPeriod = DateUtil.getDateStr(deposito.getDtTglawal(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        //boolean checkPeriod = Tools.compare(deposito.getDtTglawal(), new Date()) >= 0;
        //if (checkPeriod) throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");

        //boolean checkPeriod2 = Tools.compare(deposito.getDtTglakhir(), new Date()) <= 0;
        //if (checkPeriod2) throw new RuntimeException("Tanggal Akhir Sudah Lewat");

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));

                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
                deposito.setStStatus("DEPOSITO");
                deposito.setStKeterangan("Pembentukan");
                deposito.setStJournalStatus("HISTORY");
                deposito.setStKonter("0");
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("Y");
                deposito.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtApprovedDate(new Date());
                deposito.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtCreateDate(new Date());

                deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
                deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));
                BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                //logger.logDebug("###############  "+bunga1);
            }

            //buktib = generateNoBuktiDeposito(deposito);
            //register = generateRegisterBentuk(deposito);
            if (deposito.getStBuktiB() != null) {
                if (deposito.getStBuktiB().length() <= 14) {
                    deposito.setStBuktiB(deposito.getStBuktiB());

                    deposito.setStRegister(
                            deposito.getStBuktiB().substring(1, 7) + "00"
                            + deposito.getStBuktiB().substring(11, 14));
                } else {
                    deposito.setStBuktiB(
                            deposito.getStBuktiB().substring(0, 1)
                            + DateUtil.getYear2Digit(deposito.getDtTglmuta())
                            + DateUtil.getMonth2Digit(deposito.getDtTglmuta())
                            + deposito.getStBuktiB().substring(5, 9)
                            + deposito.getAccounts().getStAccountNo().substring(5, 10)
                            + deposito.getStBuktiB().substring(14, 19));

                    deposito.setStRegister(
                            deposito.getStBuktiB().substring(1, 7)
                            + deposito.getStBuktiB().substring(14, 19));
                }
            } else {
                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
            }

            if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                deposito.setStHari(deposito.getStHari());
                deposito.setStBulan("0");
            } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                deposito.setStHari("0");
                deposito.setStBulan(deposito.getStBulan());
            }

            deposito.setStActiveFlag("Y");
            deposito.setStEffectiveFlag("Y");

            deposito.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            deposito.setDtChangeDate(new Date());

            deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
            deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));

            BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

            S.store(deposito);

            ARInvestmentPerpanjanganView pjg = new ARInvestmentPerpanjanganView();
            pjg.markNew();

            pjg.setStARDepoID(deposito.getStARDepoID());
            pjg.setStNodefo(deposito.getStNodefo());
            pjg.setStKonter(deposito.getStKonter());
            pjg.setStBuktiB(deposito.getStBuktiB());
            pjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
            pjg.setStKodedepo(deposito.getStKodedepo());
            pjg.setStCreateWho(deposito.getStCreateWho());
            pjg.setDtCreateDate(deposito.getDtCreateDate());
            pjg.setStCurrency(deposito.getStCurrency());
            pjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
            pjg.setDbNominal(deposito.getDbNominal());
            pjg.setDbNominalKurs(deposito.getDbNominalKurs());
            pjg.setDtTglawal(deposito.getDtTglawal());
            pjg.setDtTglakhir(deposito.getDtTglakhir());
            pjg.setStBulan(deposito.getStBulan());
            pjg.setStHari(deposito.getStHari());
            pjg.setDbBunga(deposito.getDbBunga());
            pjg.setDbPajak(deposito.getDbPajak());
            pjg.setStCostCenterCode(deposito.getStCostCenterCode());
            pjg.setStCompanyType(deposito.getStCompanyType());
            pjg.setStEntityID(deposito.getStEntityID());
            pjg.setDtTgldepo(deposito.getDtTgldepo());
            pjg.setDtTglmuta(deposito.getDtTglmuta());
            pjg.setStKeterangan(deposito.getStKeterangan());
            pjg.setStRegister(deposito.getStRegister());
            pjg.setStDepoName(deposito.getStDepoName());
            pjg.setStBankName(deposito.getStBankName());
            pjg.setStEffectiveFlag(deposito.getStEffectiveFlag());
            pjg.setStReceiptClassID(deposito.getStReceiptClassID());
            pjg.setStStatus(deposito.getStStatus());
            pjg.setStApprovedWho(deposito.getStApprovedWho());
            pjg.setDtApprovedDate(deposito.getDtApprovedDate());
            pjg.setStARParentID(deposito.getStARParentID());
            pjg.setStActiveFlag(deposito.getStActiveFlag());
            pjg.setStAccountDepo(deposito.getStAccountDepo());
            pjg.setStAccountBank(deposito.getStAccountBank());
            pjg.setStNoRekening(deposito.getStNoRekening());
            pjg.setStJournalStatus(deposito.getStJournalStatus());
            pjg.setStMonths(deposito.getStMonths());
            pjg.setStYears(deposito.getStYears());

            S.store(pjg);

            final DTOList cair = deposito.getPencairan();

            for (int i = 0; i < cair.size(); i++) {
                ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

                if (pcr.isNew()) {
                    pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                    pcr.setStCreateWho(deposito.getStCreateWho());
                    pcr.setDtCreateDate(deposito.getDtCreateDate());
                }

                pcr.setStARDepoID(deposito.getStARDepoID());
                pcr.setStNodefo(deposito.getStNodefo());
                pcr.setStBuktiB(deposito.getStBuktiB());
                pcr.setStHari(deposito.getStHari());
                pcr.setStBulan(deposito.getStBulan());
                pcr.setStCostCenterCode(deposito.getStCostCenterCode());
                pcr.setStReceiptClassID(deposito.getStReceiptClassID());
                pcr.setStCompanyType(deposito.getStCompanyType());
                pcr.setStCurrency(deposito.getStCurrency());
                pcr.setDbCurrencyRate(deposito.getDbCurrencyRate());
                pcr.setDbPajak(deposito.getDbPajak());
                pcr.setDbBunga(deposito.getDbBunga());
                pcr.setDbNominal(deposito.getDbNominal());
                pcr.setDbNominalKurs(deposito.getDbNominalKurs());
                pcr.setDtTglawal(deposito.getDtTglawal());
                pcr.setDtTglakhir(deposito.getDtTglakhir());
                pcr.setDtTgldepo(deposito.getDtTgldepo());
                pcr.setDtTglmuta(deposito.getDtTglmuta());
                pcr.setStRegisterBentuk(deposito.getStRegister());
                pcr.setStKonter(deposito.getStKonter());
                pcr.setStKodedepo(deposito.getStKodedepo());
                pcr.setStActiveFlag("Y");
                pcr.setStEffectiveFlag("N");
                pcr.setStMonths(null);
                pcr.setStYears(null);
                pcr.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                pcr.setStDepoName(deposito.getStDepoName());
                pcr.setStAccountDepo(deposito.getStAccountDepo());
                pcr.setStNoRekening(deposito.getStNoRekening());
                pcr.setStActiveCairFlag("Y");
            }

            S.store(cair);

//            final DTOList renew = deposito.getPerpanjangan2();
//
//            for (int n = 0; n < renew.size(); n++) {
//                ARInvestmentPerpanjanganView j = (ARInvestmentPerpanjanganView) renew.get(n);
//
////                if (pcr.isNew()) {
////                    pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
////                    pcr.setStCreateWho(deposito.getStCreateWho());
////                    pcr.setDtCreateDate(deposito.getDtCreateDate());
////                }
//
//                j.markNew();
//                j.setStARDepoID(deposito.getStARDepoID());
//                j.setStNodefo(deposito.getStNodefo());
//                j.setStKonter(deposito.getStKonter());
//                j.setStBuktiB(deposito.getStBuktiB());
//                j.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                j.setStKodedepo(deposito.getStKodedepo());
//                j.setStCreateWho(deposito.getStCreateWho());
//                j.setDtCreateDate(deposito.getDtCreateDate());
//                j.setStCurrency(deposito.getStCurrency());
//                j.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                j.setDbNominal(deposito.getDbNominal());
//                j.setDbNominalKurs(deposito.getDbNominalKurs());
//                j.setDtTglawal(deposito.getDtTglawal());
//                j.setDtTglakhir(deposito.getDtTglakhir());
//                j.setStBulan(deposito.getStBulan());
//                j.setStHari(deposito.getStHari());
//                j.setDbBunga(deposito.getDbBunga());
//                j.setDbPajak(deposito.getDbPajak());
//                j.setStCostCenterCode(deposito.getStCostCenterCode());
//                j.setStCompanyType(deposito.getStCompanyType());
//                j.setStEntityID(deposito.getStEntityID());
//                j.setDtTgldepo(deposito.getDtTgldepo());
//                j.setDtTglmuta(deposito.getDtTglmuta());
//                j.setStKeterangan(deposito.getStKeterangan());
//                j.setStRegister(deposito.getStRegister());
//                j.setStDepoName(deposito.getStDepoName());
//                j.setStBankName(deposito.getStBankName());
//                j.setStEffectiveFlag(deposito.getStEffectiveFlag());
//                j.setStReceiptClassID(deposito.getStReceiptClassID());
//                j.setStStatus(deposito.getStStatus());
//                j.setStApprovedWho(deposito.getStApprovedWho());
//                j.setDtApprovedDate(deposito.getDtApprovedDate());
//                j.setStARParentID(deposito.getStARParentID());
//                j.setStActiveFlag(deposito.getStActiveFlag());
//                j.setStAccountDepo(deposito.getStAccountDepo());
//                j.setStAccountBank(deposito.getStAccountBank());
//                j.setStNoRekening(deposito.getStNoRekening());
//                j.setStJournalStatus(deposito.getStJournalStatus());
//                j.setStMonths(deposito.getStMonths());
//                j.setStYears(deposito.getStYears());
//            }
//
//            S.store(renew);

            Date policyDateStart = deposito.getDtTglawal();
            Date policyDateEnd = deposito.getDtTglakhir();

            DateTime startDate = new DateTime(policyDateStart);
            DateTime endDate = new DateTime(policyDateEnd);

            Months y = Months.monthsBetween(startDate, endDate);
            int month = y.getMonths();

            Date policyDateEnd2 = advanceMonth(policyDateStart, 1);

            //logger.logDebug("############### "+deposito.getStKodedepo());

            if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                if (month > 1) {
                    for (int j = 0; j < month; j++) {

                        final DTOList bunga = deposito.getBunga();

                        for (int i = 0; i < bunga.size(); i++) {
                            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                            if (bng.isNew()) {
                                bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                                bng.setStCreateWho(deposito.getStCreateWho());
                                bng.setDtCreateDate(deposito.getDtCreateDate());
                            }

                            //bng.markNew();

                            //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                            bng.setStARDepoID(deposito.getStARDepoID());
                            bng.setStNodefo(deposito.getStNodefo());
                            bng.setStNoBuktiB(deposito.getStBuktiB());
                            bng.setStCostCenterCode(deposito.getStCostCenterCode());
                            bng.setStReceiptClassID(deposito.getStReceiptClassID());
                            bng.setStKodedepo(deposito.getStKodedepo());
                            bng.setStRegisterBentuk(deposito.getStRegister());
                            bng.setStDepoName(deposito.getStDepoName());
                            bng.setStCurrency(deposito.getStCurrency());
                            bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                            bng.setDbNominal(deposito.getDbNominal());
                            bng.setDbNominalKurs(deposito.getDbNominalKurs());
                            bng.setDbPersen(deposito.getDbBunga());
                            bng.setDbPajak(deposito.getDbPajak());
                            bng.setDtTglAwal(advanceMonth(policyDateStart, j));
                            bng.setDtTglAkhir(advanceMonth(policyDateEnd2, j));
                            bng.setStCompanyType(deposito.getStCompanyType());
                            bng.setStActiveFlag("Y");
                            bng.setStEffectiveFlag("N");
                            bng.setStMonths(null);
                            bng.setStYears(null);
                            bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                            bng.setStDepoName(deposito.getStDepoName());
                            bng.setStAccountDepo(deposito.getStAccountDepo());
                            bng.setStNoRekening(deposito.getStNoRekening());
                            bng.setDbAngka1(bunga1);
                            if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                                bng.setStHari(deposito.getStHari());
                            } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                                bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                            }

                        }
                        S.store(bunga);
                    }

                } else if (month == 1) {

                    final DTOList bunga = deposito.getBunga();

                    for (int i = 0; i < bunga.size(); i++) {
                        ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                        if (bng.isNew()) {
                            bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                            bng.setStCreateWho(deposito.getStCreateWho());
                            bng.setDtCreateDate(deposito.getDtCreateDate());
                        }

                        //bng.markNew();

                        //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                        bng.setStARDepoID(deposito.getStARDepoID());
                        bng.setStNodefo(deposito.getStNodefo());
                        bng.setStNoBuktiB(deposito.getStBuktiB());
                        bng.setStCostCenterCode(deposito.getStCostCenterCode());
                        bng.setStReceiptClassID(deposito.getStReceiptClassID());
                        bng.setStKodedepo(deposito.getStKodedepo());
                        bng.setStRegisterBentuk(deposito.getStRegister());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStCurrency(deposito.getStCurrency());
                        bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                        bng.setDbNominal(deposito.getDbNominal());
                        bng.setDbNominalKurs(deposito.getDbNominalKurs());
                        bng.setDbPersen(deposito.getDbBunga());
                        bng.setDbPajak(deposito.getDbPajak());
                        bng.setDtTglAwal(deposito.getDtTglawal());
                        bng.setDtTglAkhir(deposito.getDtTglakhir());
                        bng.setStCompanyType(deposito.getStCompanyType());
                        bng.setStActiveFlag("Y");
                        bng.setStEffectiveFlag("N");
                        bng.setStMonths(null);
                        bng.setStYears(null);
                        bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStAccountDepo(deposito.getStAccountDepo());
                        bng.setStNoRekening(deposito.getStNoRekening());
                        bng.setDbAngka1(bunga1);
                        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                            bng.setStHari(deposito.getStHari());
                        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                            bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                        }
                    }

                    S.store(bunga);
                }
            } else if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                final DTOList bunga = deposito.getBunga();

                for (int i = 0; i < bunga.size(); i++) {
                    ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                    if (bng.isNew()) {
                        bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                        bng.setStCreateWho(deposito.getStCreateWho());
                        bng.setDtCreateDate(deposito.getDtCreateDate());
                    }

                    //bng.markNew();

                    //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                    bng.setStARDepoID(deposito.getStARDepoID());
                    bng.setStNodefo(deposito.getStNodefo());
                    bng.setStNoBuktiB(deposito.getStBuktiB());
                    bng.setStCostCenterCode(deposito.getStCostCenterCode());
                    bng.setStReceiptClassID(deposito.getStReceiptClassID());
                    bng.setStKodedepo(deposito.getStKodedepo());
                    bng.setStRegisterBentuk(deposito.getStRegister());
                    bng.setStDepoName(deposito.getStDepoName());
                    bng.setStCurrency(deposito.getStCurrency());
                    bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                    bng.setDbNominal(deposito.getDbNominal());
                    bng.setDbNominalKurs(deposito.getDbNominalKurs());
                    bng.setDbPersen(deposito.getDbBunga());
                    bng.setDbPajak(deposito.getDbPajak());
                    bng.setDtTglAwal(deposito.getDtTglawal());
                    bng.setDtTglAkhir(deposito.getDtTglakhir());
                    bng.setStCompanyType(deposito.getStCompanyType());
                    bng.setStActiveFlag("Y");
                    bng.setStEffectiveFlag("N");
                    bng.setStMonths(null);
                    bng.setStYears(null);
                    bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                    bng.setStDepoName(deposito.getStDepoName());
                    bng.setStAccountDepo(deposito.getStAccountDepo());
                    bng.setStNoRekening(deposito.getStNoRekening());
                    bng.setDbAngka1(bunga1);
                    if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                        bng.setStHari(deposito.getStHari());
                    } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                        bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                    }

                }
                S.store(bunga);

            }

//            final DTOList panjang = deposito.getPerpanjangan();
//
//            for (int i = 0; i < panjang.size(); i++) {
//                ARInvestmentDepositoView pjg = (ARInvestmentDepositoView) panjang.get(i);
//
//                pjg.markUpdate();
//                pjg.setStNodefo(deposito.getStNodefo());
//                pjg.setStBuktiB(deposito.getStBuktiB());
//                pjg.setStHari(deposito.getStHari());
//                pjg.setStBulan(deposito.getStBulan());
//                pjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                pjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                pjg.setStCompanyType(deposito.getStCompanyType());
//                pjg.setStCurrency(deposito.getStCurrency());
//                pjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                pjg.setDbPajak(deposito.getDbPajak());
//                pjg.setDbBunga(deposito.getDbBunga());
//                pjg.setDbNominal(deposito.getDbNominal());
//                pjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                pjg.setStRegister(deposito.getStRegister());
//                pjg.setStKodedepo(deposito.getStKodedepo());
//                pjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                pjg.setStDepoName(deposito.getStDepoName());
//                pjg.setStAccountDepo(deposito.getStAccountDepo());
//                pjg.setStEntityID(deposito.getStEntityID());
//                pjg.setStBankName(deposito.getStBankName());
//                pjg.setStAccountBank(deposito.getStAccountBank());
//                pjg.setStNoRekening(deposito.getStNoRekening());
//            }
//
//            S.store(panjang);
//
//            final DTOList cairpanjang = deposito.getCairnodefo();
//
//            for (int i = 0; i < cairpanjang.size(); i++) {
//                ARInvestmentPencairanView cairpjg = (ARInvestmentPencairanView) cairpanjang.get(i);
//
//                cairpjg.markUpdate();
//                cairpjg.setStNodefo(deposito.getStNodefo());
//                cairpjg.setStBuktiB(deposito.getStBuktiB());
//                cairpjg.setStHari(deposito.getStHari());
//                cairpjg.setStBulan(deposito.getStBulan());
//                cairpjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                cairpjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                cairpjg.setStCompanyType(deposito.getStCompanyType());
//                cairpjg.setStCurrency(deposito.getStCurrency());
//                cairpjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                cairpjg.setDbPajak(deposito.getDbPajak());
//                cairpjg.setDbBunga(deposito.getDbBunga());
//                cairpjg.setDbNominal(deposito.getDbNominal());
//                cairpjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                cairpjg.setStRegisterBentuk(deposito.getStRegister());
//                cairpjg.setStKodedepo(deposito.getStKodedepo());
//                cairpjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                cairpjg.setStDepoName(deposito.getStDepoName());
//                cairpjg.setStAccountDepo(deposito.getStAccountDepo());
//                cairpjg.setStNoRekening(deposito.getStNoRekening());
//                cairpjg.setStMonths(null);
//                cairpjg.setStYears(null);
//                cairpjg.setStActiveCairFlag("Y");
//            }
//
//            S.store(cairpanjang);
//
//            final DTOList bungapanjang = deposito.getBunganodefo();
//
//            for (int i = 0; i < bungapanjang.size(); i++) {
//                ARInvestmentBungaView bngpjg = (ARInvestmentBungaView) bungapanjang.get(i);
//
//                bngpjg.markUpdate();
//                bngpjg.setStNodefo(deposito.getStNodefo());
//                bngpjg.setStNoBuktiB(deposito.getStBuktiB());
//                bngpjg.setStHari(deposito.getStHari());
//                bngpjg.setStCostCenterCode(deposito.getStCostCenterCode());
//                bngpjg.setStReceiptClassID(deposito.getStReceiptClassID());
//                bngpjg.setStCompanyType(deposito.getStCompanyType());
//                bngpjg.setStCurrency(deposito.getStCurrency());
//                bngpjg.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                bngpjg.setDbPajak(deposito.getDbPajak());
//                bngpjg.setDbPersen(deposito.getDbBunga());
//                bngpjg.setDbNominal(deposito.getDbNominal());
//                bngpjg.setDbNominalKurs(deposito.getDbNominalKurs());
//                bngpjg.setStRegisterBentuk(deposito.getStRegister());
//                bngpjg.setStKodedepo(deposito.getStKodedepo());
//                bngpjg.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                bngpjg.setStDepoName(deposito.getStDepoName());
//                bngpjg.setStAccountDepo(deposito.getStAccountDepo());
//                bngpjg.setStNoRekening(deposito.getStNoRekening());
//                bngpjg.setStMonths(null);
//                bngpjg.setStYears(null);
//            }
//
//            S.store(bungapanjang);

            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public String saveWithoutJurnalCair(ARInvestmentPencairanView pencairan) throws Exception {
        //logger.logDebug("save: "+deposito);
        /*
        String buktic = generateNoBuktiC(pencairan);
        String register = generateRegisterCair(pencairan);
         */
        final SQLUtil S = new SQLUtil();

        try {
            if (pencairan.isNew()) {
                pencairan.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
            }

            if (pencairan.getStBuktiC() == null) {
                pencairan.generateNoBuktiC();
                pencairan.generateRegisterCair();
            } else {
                pencairan.setStBuktiC(
                        pencairan.getStBuktiC().substring(0, 1)
                        + DateUtil.getYear2Digit(pencairan.getDtTglmuta())
                        + DateUtil.getMonth2Digit(pencairan.getDtTglmuta())
                        + pencairan.getStBuktiC().substring(5, 9)
                        + pencairan.getAccounts().getStAccountNo().substring(5, 10)
                        + pencairan.getStBuktiC().substring(14, 19));
            }

            pencairan.setStActiveFlag("Y");
            pencairan.setStEffectiveFlag("Y");

            pencairan.setStActiveFlag("Y");
            pencairan.setStEffectiveFlag("Y");
            pencairan.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
            pencairan.setDtApprovedDate(new Date());
            pencairan.setStJournalStatus("HISTORY");
            pencairan.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            pencairan.setDtChangeDate(new Date());

            S.store(pencairan);

            final DTOList deposito = pencairan.getDepositoView();

            for (int t = 0; t < deposito.size(); t++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) deposito.get(t);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                depo.markUpdate();
                depo.setStARCairID(pencairan.getStARCairID());
                depo.setDtTglCair(pencairan.getDtTglCair());
                depo.setDtTglEntryCair(new Date());
            }

            S.store(deposito);

            final DTOList bunga = pencairan.getBunga();

            for (int m = 0; m < bunga.size(); m++) {
                ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(m);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                bng.markUpdate();
                bng.setStARCairID(pencairan.getStARCairID());
                bng.setDtTglCair(pencairan.getDtTglCair());
                bng.setDtTglEntryCair(new Date());
            }

            S.store(bunga);

            final DTOList renew = pencairan.getPerpanjangan();

            for (int n = 0; n < renew.size(); n++) {
                ARInvestmentPerpanjanganView ren = (ARInvestmentPerpanjanganView) renew.get(n);

                //if (bng.isNew()) bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));

                ren.markUpdate();
                ren.setStARCairID(pencairan.getStARCairID());
                ren.setDtTglCair(pencairan.getDtTglCair());
                ren.setDtTglEntryCair(new Date());
            }

            S.store(renew);

            return pencairan.getStARCairID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public String saveRenewal(ARInvestmentDepositoView deposito, String stNextStatus, boolean approvalMode) throws Exception {
        deposito = (ARInvestmentDepositoView) ObjectCloner.deepCopy(deposito);

        DateTime startDateNew = new DateTime(deposito.getDtTglakhir());
        DateTime startDate = new DateTime();
        DateTime endDate = new DateTime();

        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            startDate = startDateNew.plusDays(1);
            endDate = startDate.plusDays(Integer.parseInt(deposito.getStHari()));
        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
            startDate = startDateNew;
            endDate = startDate.plusMonths(Integer.parseInt(deposito.getStBulan()));
        }

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {
            if (stNextStatus != null) {

                deposito.markNew();
                deposito.setStARParentID(deposito.getStARDepoID());
                deposito.setStStatus(stNextStatus);
                deposito.setStBuktiB(deposito.getStBuktiB());
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("Y");
                deposito.setDtTglawal(startDate.toDate());
                deposito.setDtTglakhir(endDate.toDate());
                deposito.setStKeterangan("Perpanjangan");
                deposito.setStCreateWho(deposito.getStCreateWho());
                deposito.setDtCreateDate(new Date());
                deposito.setStApprovedWho(deposito.getStApprovedWho());
                deposito.setDtApprovedDate(new Date());
                deposito.setStChangeWho(deposito.getStChangeWho());
                deposito.setDtChangeDate(new Date());
                deposito.getPencairan().convertAllToNew();
                deposito.getBunga().convertAllToNew();
            }

            if (FinCodec.Deposito.RENEWAL.equalsIgnoreCase(stNextStatus)) {
                deposito.generateRenewalNo();
            }

            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
            }

            if (deposito.getStARParentID() == null) {
                deposito.setStARParentID(deposito.getStARDepoID());
            }

            S.store(deposito);

            final DTOList cair = deposito.getPencairan();

            for (int i = 0; i < cair.size(); i++) {
                ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

                if (pcr.isNew()) {
                    pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                    pcr.setStCreateWho(deposito.getStCreateWho());
                    pcr.setDtCreateDate(deposito.getDtCreateDate());
                }

                pcr.setStARDepoID(deposito.getStARDepoID());
                pcr.setStKonter(deposito.getStKonter());
                pcr.setStActiveFlag("Y");
                pcr.setStEffectiveFlag("N");
                pcr.setStBuktiC(null);
                pcr.setDtTglCair(null);
                pcr.setStRegister(null);
                pcr.setStApprovedWho(null);
                pcr.setDtApprovedDate(null);
                pcr.setStChangeWho(null);
                pcr.setDtChangeDate(null);
                pcr.setStMonths(null);
                pcr.setStYears(null);
                if (pcr.getStBuktiB() != null) {
                    pcr.setStActiveCairFlag("Y");
                }
                pcr.setStARParentID(pcr.getStARCairID());
                pcr.setDtTglawal(deposito.getDtTglawal());
                pcr.setDtTglakhir(deposito.getDtTglakhir());
                pcr.setStKeterangan(deposito.getStKeterangan());
            }

            S.store(cair);

            BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

            Date policyDateStart = deposito.getDtTglawal();
            Date policyDateEnd = deposito.getDtTglakhir();

            DateTime startDate1 = new DateTime(policyDateStart);
            DateTime endDate1 = new DateTime(policyDateEnd);

            Months y = Months.monthsBetween(startDate1, endDate1);
            int month = y.getMonths();

            int bulan = Integer.parseInt(deposito.getStBulan());

            Date policyDateEnd2 = advanceMonth(policyDateStart, 1);

            if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                if (bulan > 1) {
                    //for (int j = 0; j < bulan; j++) {

                    final DTOList bunga = deposito.getBunga();

                    for (int i = 0; i < bunga.size(); i++) {
                        ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                        if (bng.isNew()) {
                            bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                            bng.setStCreateWho(deposito.getStCreateWho());
                            bng.setDtCreateDate(deposito.getDtCreateDate());
                        }

                        //bng.markNew();

                        //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                        bng.setStARDepoID(deposito.getStARDepoID());
                        bng.setStNodefo(deposito.getStNodefo());
                        bng.setStNoBuktiB(deposito.getStBuktiB());
                        bng.setStCostCenterCode(deposito.getStCostCenterCode());
                        bng.setStReceiptClassID(deposito.getStReceiptClassID());
                        bng.setStKodedepo(deposito.getStKodedepo());
                        bng.setStRegisterBentuk(deposito.getStRegister());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStCurrency(deposito.getStCurrency());
                        bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                        bng.setDbNominal(deposito.getDbNominal());
                        bng.setDbNominalKurs(deposito.getDbNominalKurs());
                        bng.setDbPersen(deposito.getDbBunga());
                        bng.setDbPajak(deposito.getDbPajak());
                        bng.setDtTglAwal(advanceMonth(policyDateStart, i));
                        bng.setDtTglAkhir(advanceMonth(policyDateEnd2, i));
                        bng.setStCompanyType(deposito.getStCompanyType());
                        bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStAccountDepo(deposito.getStAccountDepo());
                        bng.setStNoRekening(deposito.getStNoRekening());
                        bng.setDbAngka1(bunga1);
                        bng.setStNoBuktiD(null);
                        bng.setDbAngka(null);
                        bng.setDtTglBunga(null);
                        bng.setStChangeWho(null);
                        bng.setDtChangeDate(null);
                        bng.setStMonths(null);
                        bng.setStYears(null);
                        bng.setStRegister(null);
                        bng.setStApprovedWho(null);
                        bng.setDtApprovedDate(null);
                        bng.setStActiveFlag("Y");
                        bng.setStEffectiveFlag("N");
                        //bng.setStARParentID(bng.getStARBungaID());
                        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                            bng.setStHari(deposito.getStHari());
                        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                            bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                        }

                    }
                    S.store(bunga);
                    //}

                } else if (bulan == 1) {

                    final DTOList bungax = deposito.getBunga();

                    for (int i = 0; i < bungax.size(); i++) {
                        ARInvestmentBungaView bng = (ARInvestmentBungaView) bungax.get(i);

                        if (bng.isNew()) {
                            bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                            bng.setStCreateWho(deposito.getStCreateWho());
                            bng.setDtCreateDate(deposito.getDtCreateDate());
                        }

                        //bng.markNew();

                        //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

                        bng.setStARDepoID(deposito.getStARDepoID());
                        bng.setStNodefo(deposito.getStNodefo());
                        bng.setStNoBuktiB(deposito.getStBuktiB());
                        bng.setStCostCenterCode(deposito.getStCostCenterCode());
                        bng.setStReceiptClassID(deposito.getStReceiptClassID());
                        bng.setStKodedepo(deposito.getStKodedepo());
                        bng.setStRegisterBentuk(deposito.getStRegister());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStCurrency(deposito.getStCurrency());
                        bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                        bng.setDbNominal(deposito.getDbNominal());
                        bng.setDbNominalKurs(deposito.getDbNominalKurs());
                        bng.setDbPersen(deposito.getDbBunga());
                        bng.setDbPajak(deposito.getDbPajak());
                        bng.setDtTglAwal(deposito.getDtTglawal());
                        bng.setDtTglAkhir(deposito.getDtTglakhir());
                        bng.setStCompanyType(deposito.getStCompanyType());
                        bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStAccountDepo(deposito.getStAccountDepo());
                        bng.setStNoRekening(deposito.getStNoRekening());
                        bng.setDbAngka1(bunga1);
                        bng.setStNoBuktiD(null);
                        bng.setDbAngka(null);
                        bng.setDtTglBunga(null);
                        bng.setStChangeWho(null);
                        bng.setDtChangeDate(null);
                        bng.setStMonths(null);
                        bng.setStYears(null);
                        bng.setStRegister(null);
                        bng.setStApprovedWho(null);
                        bng.setDtApprovedDate(null);
                        bng.setStActiveFlag("Y");
                        bng.setStEffectiveFlag("N");
                        //bng.setStARParentID(bng.getStARBungaID());
                        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                            bng.setStHari(deposito.getStHari());
                        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                            bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                        }
                    }

                    S.store(bungax);
                }
            }
//            else if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
//                final DTOList bungaz = deposito.getBunga();
//
//                for (int i = 0; i < bungaz.size(); i++) {
//                    ARInvestmentBungaView bng = (ARInvestmentBungaView) bungaz.get(i);
//
//                    if (bng.isNew()) {
//                        bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
//                        bng.setStCreateWho(deposito.getStCreateWho());
//                        bng.setDtCreateDate(deposito.getDtCreateDate());
//                    }
//
//                    //bng.markNew();
//
//                    //bng.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());
//
//                    bng.setStARDepoID(deposito.getStARDepoID());
//                    bng.setStNodefo(deposito.getStNodefo());
//                    bng.setStNoBuktiB(deposito.getStBuktiB());
//                    bng.setStCostCenterCode(deposito.getStCostCenterCode());
//                    bng.setStReceiptClassID(deposito.getStReceiptClassID());
//                    bng.setStKodedepo(deposito.getStKodedepo());
//                    bng.setStRegisterBentuk(deposito.getStRegister());
//                    bng.setStDepoName(deposito.getStDepoName());
//                    bng.setStCurrency(deposito.getStCurrency());
//                    bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
//                    bng.setDbNominal(deposito.getDbNominal());
//                    bng.setDbNominalKurs(deposito.getDbNominalKurs());
//                    bng.setDbPersen(deposito.getDbBunga());
//                    bng.setDbPajak(deposito.getDbPajak());
//                    bng.setDtTglAwal(deposito.getDtTglawal());
//                    bng.setDtTglAkhir(deposito.getDtTglakhir());
//                    bng.setStCompanyType(deposito.getStCompanyType());
//                    bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
//                    bng.setStDepoName(deposito.getStDepoName());
//                    bng.setStAccountDepo(deposito.getStAccountDepo());
//                    bng.setStNoRekening(deposito.getStNoRekening());
//                    bng.setDbAngka1(bunga1);
//                    bng.setStNoBuktiD(null);
//                    bng.setDbAngka(null);
//                    bng.setDtTglBunga(null);
//                    bng.setStChangeWho(null);
//                    bng.setDtChangeDate(null);
//                    bng.setStMonths(null);
//                    bng.setStYears(null);
//                    bng.setStRegister(null);
//                    bng.setStApprovedWho(null);
//                    bng.setDtApprovedDate(null);
//                    bng.setStActiveFlag("Y");
//                    bng.setStEffectiveFlag("N");
//                    //bng.setStARParentID(bng.getStARBungaID());
//                    if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
//                        bng.setStHari(deposito.getStHari());
//                    } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
//                        bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
//                    }
//
//                }
//                S.store(bungaz);
//
//            }

            return deposito.getStARDepoID();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }


    public void savePostingGL(GLPostingView posting, boolean reOpenMode) throws Exception {
        SQLUtil S = new SQLUtil();

        try {

            if (posting.isNew()) {
                posting.setStGLPostingID(String.valueOf(IDFactory.createNumericID("GLPOST")));
            }

            S.store(posting);

            final GLPostingView oldPosting = (GLPostingView) posting.getOld();
            final boolean postFlagChanged = oldPosting==null || !Tools.isEqual(oldPosting.getStPostedFlag(), posting.getStPostedFlag());

            final boolean doUnPost = postFlagChanged && Tools.isNo(posting.getStPostedFlag());

                if (doUnPost) {
                    if(reOpenMode){
    //                    PreparedStatement PS = S.setQuery("BEGIN;update gl_posting set status = 'PROCESS OPEN',change_date='now' where gl_post_id = ?;COMMIT;");
    //
    //                    PS.setObject(1,posting.getStGLPostingID());
    //
    //                    int j = PS.executeUpdate();
    //
    //                    if (j!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
    //
    //                    S.release();

                        updatePostingStatus(posting.getStYears(), posting.getStMonths(), posting.getStCostCenterCode(), false, posting);

    //                    PreparedStatement PS2 = S.setQuery("BEGIN;update gl_posting set status = 'FINISH OPEN',change_date='now', notes = null where gl_post_id = ?;COMMIT;");
    //
    //                    PS2.setObject(1,posting.getStGLPostingID());
    //
    //                    int k = PS2.executeUpdate();
    //
    //                    if (k!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
    //
    //                    S.release();
                    }
                }

                if (Tools.isYes(posting.getStPostedFlag())) {

    //                PreparedStatement PS = S.setQuery("BEGIN;update gl_posting set status = 'PROCESS POSTING',change_date='now' where gl_post_id = ?;COMMIT;");
    //
    //                PS.setObject(1,posting.getStGLPostingID());
    //
    //                int j = PS.executeUpdate();
    //
    //                if (j!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
    //
    //                S.release();

                    updatePostingStatus(posting.getStYears(), posting.getStMonths(), posting.getStCostCenterCode(), true, posting);

    //                PreparedStatement PS2 = S.setQuery("BEGIN;update gl_posting set status = 'FINISH POSTING',change_date='now', notes = null where gl_post_id = ?;COMMIT;");
    //
    //                PS2.setObject(1,posting.getStGLPostingID());
    //
    //                int k = PS2.executeUpdate();
    //
    //                if (k!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
    //
    //                S.release();

                }


        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void savePostingGLFinalisasi(GLPostingView posting, boolean reOpenMode) throws Exception {
        SQLUtil S = new SQLUtil();

        try {

            if (posting.isNew()) {
                posting.setStGLPostingID(String.valueOf(IDFactory.createNumericID("GLPOST")));
            }

            S.store(posting);


        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void updatePostingStatus(String year, String month, String stCostCenter, boolean doPosting, GLPostingView posting) throws Exception, RemoteException {
        logger.logDebug("updatePostingStatus: [" + year + "," + month + "," + stCostCenter + "]");

        final SQLUtil S = new SQLUtil();

        String cekBalance = " select * "
                + " from "
                + "  ( "
                + "  select  "
                + " trx_no,sum(a.debit) as total_debit,sum(a.credit) as total_credit "
                + " from gl_je_detail a "
                + " inner join gl_accounts b on a.accountid = b.account_id "
                + " where a.fiscal_year = ? and a.period_no = ? ";

         if (stCostCenter != null) {
             cekBalance = cekBalance + " and substr(b.accountno,14,2) = ?";
         }

         cekBalance = cekBalance + " group by trx_no "
                + " ) x where total_debit <> total_credit";

        String cekBalanceRekeningAntarKantor = " select * "
                + " from "
                + "  ( "
                + "  select  "
                + " b.accountno,sum(a.debit) as total_debit,sum(a.credit) as total_credit "
                + " from gl_je_detail a "
                + " inner join gl_accounts b on a.accountid = b.account_id "
                + " where a.fiscal_year = ? and a.period_no = ? ";

        if (stCostCenter != null) {
             cekBalanceRekeningAntarKantor = cekBalanceRekeningAntarKantor + " and substr(b.accountno,14,2) = ?";
         }

        cekBalanceRekeningAntarKantor = cekBalanceRekeningAntarKantor + " and b.accountno like '210000000001%' "
                + " group by b.accountno "
                + " ) x where total_debit <> total_credit";


        String postingNol = " update gl_je_detail set posted_flag = null where "
                + "  trx_id in  "
                + "    ( "
                + "    select a.trx_id "
                + "    from gl_je_detail a "
                + "    inner join gl_accounts b on a.accountid = b.account_id "
                + "    where fiscal_year = ? "
                + "    and period_no = ? ";

        if (stCostCenter != null) {
            postingNol = postingNol + "   and substr(b.accountno,14,2) = ? ";
        }

        postingNol = postingNol + " );";

        if(stCostCenter==null){
            postingNol = "update gl_je_detail set posted_flag = null  "+
                         "   where fiscal_year = ?     and period_no = ? ;";
        }

        String nolSaldo = "update gl_acct_bal2 set cr" + Integer.parseInt(month) + " = null,db" + Integer.parseInt(month) + " = null, bal" + Integer.parseInt(month) + " = null "
                + " where period_year = ? and idr_flag is not null and account_id in"
                + "    ( "
                + "    select a.account_id "
                + "    from gl_acct_bal2 a "
                + "    inner join gl_accounts b on a.account_id = b.account_id "
                + "    where a.idr_flag is not null and a.period_year = ? and b.acctype is null";

        if (stCostCenter != null) {
            nolSaldo = nolSaldo + "   and substr(b.accountno,14,2) = ? ";
        }

        nolSaldo = nolSaldo + " group by a.account_id );";

        String updateSaldo = " update gl_je_detail set posted_flag = 'Y' where "
                + "  trx_id in  "
                + "    ( "
                + "    select trx_id "
                + "    from gl_je_detail a "
                + "    inner join gl_accounts b on a.accountid = b.account_id "
                + "    where fiscal_year = ? "
                + "    and period_no = ? ";

        if (stCostCenter != null) {
            updateSaldo = updateSaldo + "   and substr(b.accountno,14,2) = ? ";
        }

        updateSaldo = updateSaldo + " );";

        if(stCostCenter==null){
            updateSaldo = "update gl_je_detail set posted_flag = 'Y'  "+
                         "   where fiscal_year = ?  and period_no = ? ;";
        }

        String postingSaldoNew = "select b.account_id::character varying,a.fiscal_year::character varying,a.period_no::character varying,sum(a.debit) as debit,sum(a.credit) as credit "+
                                 "   from gl_je_detail a "+
                                " inner join gl_accounts b on a.accountid = b.account_id "+
                               " where fiscal_year = ? and period_no = ? and b.acctype is null";
        if (stCostCenter != null) {
            postingSaldoNew = postingSaldoNew + "   and substr(b.accountno,14,2) = '"+ stCostCenter +"' ";
        }

        postingSaldoNew = postingSaldoNew + " group by b.account_id,a.fiscal_year,a.period_no";

        try {
            if (doPosting) {
                //CEK BALANCE DULU
                boolean tidakBalance = false;
                PreparedStatement PSCekBalance = S.setQuery(cekBalance);

                PSCekBalance.setObject(1, year);
                PSCekBalance.setObject(2, month);

                if (stCostCenter != null) {
                    PSCekBalance.setObject(3, stCostCenter);
                }

                ResultSet res = PSCekBalance.executeQuery();

                String noBuktiTidakBalance = "";
                if (res.next()) {
                    tidakBalance = true;

                    res.beforeFirst();
                    while (res.next()) {
                        noBuktiTidakBalance = noBuktiTidakBalance + ", " + res.getString(1);
                    }

//                    PreparedStatement PS = S.setQuery("BEGIN;update gl_posting set posted_flag=null, status = 'FAILED',change_date='now' ,notes = 'Jurnal tidak balance pada nomor bukti : " + noBuktiTidakBalance +"' where gl_post_id = ?;COMMIT;");
//
//                    PS.setObject(1,posting.getStGLPostingID());
//
//                    int j = PS.executeUpdate();
//
//                    if (j!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
//
//                    S.release();

                    throw new RuntimeException("Jurnal tidak balance pada nomor bukti : " + noBuktiTidakBalance);
                    //logger.logDebug("++++++++++++++ berhasil UPDATE FLAG POSTING JURNAL MUTASI : ["+year+","+month+","+ stCostCenter+"]");
                }

                //END

                //CEK BALANCE REKENING ANTAR KANTOR JUGA
                PreparedStatement PSCekBalanceRAK = S.setQuery(cekBalanceRekeningAntarKantor);

                PSCekBalanceRAK.setObject(1, year);
                PSCekBalanceRAK.setObject(2, month);

                if (stCostCenter != null) {
                    PSCekBalanceRAK.setObject(3, stCostCenter);
                }

                ResultSet resRAK = PSCekBalanceRAK.executeQuery();

                String noBuktiTidakBalanceRAK = "";
                if (resRAK!=null && resRAK.next()) {
                    tidakBalance = true;

                    resRAK.beforeFirst();
                    while (resRAK.next()) {
                        noBuktiTidakBalanceRAK = noBuktiTidakBalanceRAK + ", " + resRAK.getString(1);
                    }

//                    PreparedStatement PS = S.setQuery("BEGIN;update gl_posting set posted_flag=null, status = 'FAILED',change_date='now' ,notes = 'Jurnal tidak balance pada nomor bukti : " + noBuktiTidakBalance +"' where gl_post_id = ?;COMMIT;");
//
//                    PS.setObject(1,posting.getStGLPostingID());
//
//                    int j = PS.executeUpdate();
//
//                    if (j!=0) logger.logInfo("+++++++ UPDATE status posting : "+ posting.getStGLPostingID() +" ++++++++++++++++++");
//
//                    S.release();

                    throw new RuntimeException("Jurnal R.A.K tidak balance pada nomor bukti : " + noBuktiTidakBalanceRAK);
                    //logger.logDebug("++++++++++++++ berhasil UPDATE FLAG POSTING JURNAL MUTASI : ["+year+","+month+","+ stCostCenter+"]");
                }

                //END

//                if(tidakBalance){
//                    String message = "Jurnal tidak balance pada nomor bukti : "+ noBuktiTidakBalance;
//
//                    if(!noBuktiTidakBalanceRAK.equalsIgnoreCase("")){
//                        message = message + " & REKENING ANTAR KANTOR : "+ noBuktiTidakBalanceRAK;
//                    }
//
//                    throw new RuntimeException(message);
//                }
            }


            //UBAH FLAG POSTING PADA JURNAL MUTASI
            //if(!doPosting){
            /* MARK TESTING POSTING BARU
                PreparedStatement PSPostingNol = S.setQuery(postingNol);

                PSPostingNol.setObject(1, year);
                PSPostingNol.setObject(2, month);

                if (stCostCenter != null) {
                    PSPostingNol.setObject(3, stCostCenter);
                }

                int i = PSPostingNol.executeUpdate();

                if (i != 0) {
                    logger.logDebug("++++++++++++++ berhasil UPDATE FLAG POSTING JURNAL MUTASI : [" + year + "," + month + "," + stCostCenter + "]");
                }
                */
            //}
            
            //END

            //POSTING NOL DULU
            PreparedStatement PSPostingNol2 = S.setQuery(nolSaldo);

            PSPostingNol2.setObject(1, year);
            PSPostingNol2.setObject(2, year);

            if (stCostCenter != null) {
                PSPostingNol2.setObject(3, stCostCenter);
            }

            int j = PSPostingNol2.executeUpdate();

            if (j != 0) {
                logger.logDebug("++++++++++++++ berhasil posting nol : [" + year + "," + month + "," + stCostCenter + "]");
            }
            //END

            /* MARK TESTING POSTING BARU
            if (doPosting) {
                //POSTING JURNAL
                PreparedStatement PS = S.setQuery(updateSaldo);

                PS.setObject(1, year);
                PS.setObject(2, month);

                if (stCostCenter != null) {
                    PS.setObject(3, stCostCenter);
                }

                int k = PS.executeUpdate();

                if (k != 0) {
                    logger.logDebug("+++++++++++++++++++ berhasil posting jurnal : [" + year + "," + month + "," + stCostCenter + "]");
                }
            }
            */

            
            if(doPosting){
                //KONSEP POSTING NEW
                final DTOList rekap = ListUtil.getDTOListFromQuery(
                        postingSaldoNew,
                    new Object[]{year, month},
                     HashDTO.class
                    );

                    for (int k = 0; k < rekap.size(); k++) {
                        HashDTO dto = (HashDTO) rekap.get(k);

                        //logger.logWarning("############### POSTING AKUN ke : "+ (k+1));

                        String accountID = dto.getFieldValueByFieldNameST("account_id");
                        String fiscal_year = dto.getFieldValueByFieldNameST("fiscal_year");
                        String period_no = dto.getFieldValueByFieldNameST("period_no");
                        BigDecimal debit = dto.getFieldValueByFieldNameBD("debit");
                        BigDecimal credit = dto.getFieldValueByFieldNameBD("credit");

                        PreparedStatement PS = S.setQuery("update gl_acct_bal2 set db"+ period_no +"="+debit+", cr"+ period_no +"="+ credit +", bal"+ period_no +"="+ BDUtil.sub(debit, credit) +" where account_id = "+ accountID +" and period_year= "+ fiscal_year +" and idr_flag is not null");

                        int l = PS.executeUpdate();

                        if (l == 0) {

                            PS = S.setQuery("INSERT INTO gl_acct_bal2(account_id,period_year,idr_flag, db"+ period_no +",cr"+ period_no +",bal"+ period_no+" ) values ("+ accountID+","+ fiscal_year +",'Y',"+ debit +","+ credit +","+ BDUtil.sub(debit, credit) +");");

                            int i = PS.executeUpdate();

                            if (i == 0) {
                                throw new Exception("Failed to update gl account balance");
                            }
                        }
                    }
            }
            

        } finally {
            S.release();
        }
    }

    public void saveJournalDirectTitipanPremi(DTOList l) throws Exception {
        //logger.logDebug("saveJournalEntry: "+l);
        //logger.logDebug("##############saveJournalEntry: "+l.size());

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiView fj = (TitipanPremiView) l.get(0);

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiView titip = (TitipanPremiView) l.get(i);
                titip.markUpdate();
                titip.setStApproved("Y");
                S.store(titip);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(Long.getLong(titip.getStAccountIDMaster()));
                j.setStHeaderAccountNo(titip.getStHeaderAccountNo());
                j.setStAccountID(titip.getStAccountID());

                j.setDbAutoCredit2(titip.getDbAmount());

                j.setStDescription(titip.getStDescription());
                j.setStJournalCode(titip.getStJournalCode());
                j.setStCurrencyCode(titip.getStCurrencyCode());
                j.setDbCurrencyRate(titip.getDbCurrencyRate());
                j.setStRefTrxNo(titip.getStTransactionID());
                j.setStIDRFlag(titip.getStIDRFlag());
                j.setStReference1("APPROVE_TITIPAN");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(titip.getStTransactionNo());
                }

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(titip.getDtApplyDate());
                //if (per==null) throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for "+j.getDtApplyDate()+")");
                //if (!per.isOpen()) throw new Exception("Period is not open");
                j.setLgPeriodNo(titip.getLgPeriodNo());
                j.setLgFiscalYear(titip.getLgFiscalYear());
                j.setStRefTrxType("TITIPAN_PREMI");
                j.setDtApplyDate(titip.getDtApplyDate());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(titip.getStTransactionNo());
                    j2.setLgHeaderAccountID(Long.getLong(titip.getStAccountIDMaster()));
                    j2.setStHeaderAccountNo(titip.getStHeaderAccountNo());
                    j2.setStAccountID(titip.getStAccountIDMaster());

                    j2.setDbAutoDebit2(titip.getDbAmount());

                    j2.setStDescription(titip.getStDescription());
                    j2.setStJournalCode(titip.getStJournalCode());
                    j2.setStCurrencyCode(titip.getStCurrencyCode());
                    j2.setDbCurrencyRate(titip.getDbCurrencyRate());
                    j2.setStRefTrxNo(titip.getStTransactionID());
                    j2.setStIDRFlag(titip.getStIDRFlag());
                    j2.setStReference1("APPROVE_TITIPAN");

                }

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                //if (per==null) throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for "+titip.getDtApplyDate()+")");
                //if (!per.isOpen()) throw new Exception("Period is not open");
                j2.setLgPeriodNo(titip.getLgPeriodNo());
                j2.setLgFiscalYear(titip.getLgFiscalYear());
                j2.setStRefTrxType("TITIPAN_PREMI");
                j2.setDtApplyDate(titip.getDtApplyDate());

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void reverseTitipan(String trx_no) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_titipan_premi set approved = null where trx_no = ?");

            P.setObject(1, trx_no);
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

            P2.setObject(1, trx_no);
            int r2 = P2.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }


    }

    public void reverseJournal(String trx_no) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update gl_je_detail set approved = null where trx_no = ?");

            P.setObject(1, trx_no);
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }


    }

    public void save3(AccountInsuranceView account) throws Exception {

        SQLUtil S = new SQLUtil();

        try {

            if(account.getStCostCenterCode()==null){
                InsuranceClausulesView clausules = new InsuranceClausulesView();

                final DTOList cabang = clausules.getCabang();

                for (int i = 0; i < cabang.size(); i++) {
                    GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

                   if (account.isNew())
                        account.setStGLInsuranceID(String.valueOf(IDFactory.createNumericID("GL_INS")));

                    account.setStCostCenterCode(cab.getStCostCenterCode());

                    S.store(account);
                }
            }else if(account.getStCostCenterCode()!=null){
                if (account.isNew())
                    account.setStGLInsuranceID(String.valueOf(IDFactory.createNumericID("GL_INS")));

                S.store(account);

            }

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public boolean isPosted(String bulan, String tahun, String cabang) throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if (cabang != null) {
                cek = cek + " and cc_code = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, bulan);
            PS.setString(2, tahun);

            if (cabang != null) {
                PS.setString(3, cabang);
            }

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

   public DTOList getBungaDeposito(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      ar_bunga_deposito a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id = ? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                BungaDepositoView.class);
    }

    public String generateBudepNo(BungaDepositoHeaderView header, DTOList l, String transNo) throws Exception {

        //if (stReceiptNo!=null) return;
        String transactionNo = "";
        final String ccCode = Tools.getDigitRightJustified(header.getStCostCenter(), 2);
        final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(), 1);
        //String stBankCode = getPaymentMethod()==null?"0000":getPaymentMethod().getStBankCode();
        //final String bankCode = Tools.getDigitRightJustified(stBankCode,4);

        /*String counterKey =
        DateUtil.getYear2Digit(header.getDtCreateDate())+
        DateUtil.getMonth2Digit(header.getDtCreateDate());*/

        String counterKey = header.getStYears().substring(2) + header.getStMonths();

        //String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //String rn = String.valueOf(IDFactory.createNumericID("TPNO" + counterKey + ccCode,1));

        //rn = Tools.getDigitRightJustified(rn,3);
        rn = StringTools.leftPad(rn, '0', 5);

        String accountcode = "";
        if (header.getStHeaderAccountNo() != null) {
            accountcode = header.getStHeaderAccountNo().substring(5, 10);
        } else {
            accountcode = transNo.substring(5, 10);
        }

        //C13011111ACEH 00684

        //110002700400
        //012345678901
        //no
        //  A0901171000000
        //  01234567890123
        //    A0910202700002

        transactionNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + accountcode
                + rn;

        return transactionNo;

    }

   public void saveJournalEntryBungaDeposito(BungaDepositoHeaderView header, DTOList l, String headerAccountID) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;//fj.getStTransactionHeaderID();

        String lgHeaderAccountID = headerAccountID;
        String stHeaderAccountNo = header.getStHeaderAccountNo();

        //BigDecimal bal = GLUtil.getBalance(l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = null;
        if (header.isNew()) {
            //trxNo = generateTransactionNo(header,l,transNo);
            //header.setStTransactionNo(trxNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView titip = (BungaDepositoView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j.setStHeaderAccountNo(titip.getStHeaderAccountNo());
                j.setStAccountID(titip.getStAccountID());

                j.setDbAutoCredit2(titip.getDbAmount());

                j.setStDescription(titip.getStDescription() + " DEPOSITO BERJANGKA " + titip.getStHeaderAccountMaster());
                j.setStCurrencyCode(titip.getStCurrencyCode());
                j.setDbCurrencyRate(titip.getDbCurrencyRate());
                j.setStRefTrxNo(titip.getStTransactionID());
                //j.setStSummaryFlag("Y");
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(titip.getStTransactionNo());
                }

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(titip.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("INV_BUNGA");
                j.setDtApplyDate(titip.getDtApplyDate());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(titip.getStTransactionNo());

                    j2.setStAccountID(lgHeaderAccountID);

                    j2.setDbAutoDebit2(titip.getDbAmount());

                    j2.setStDescription(titip.getStDescription() + " " + titip.getStHeaderAccountMaster());
                    j2.setStCurrencyCode(titip.getStCurrencyCode());
                    j2.setDbCurrencyRate(titip.getDbCurrencyRate());
                    j2.setStRefTrxNo(titip.getStTransactionID());

                }

                //j.setStSummaryFlag("Y");
                j2.setStIDRFlag("Y");
                j2.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j2.setStHeaderAccountNo(titip.getStHeaderAccountNo());

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + titip.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("INV_BUNGA");
                j2.setDtApplyDate(titip.getDtApplyDate());
                //j2.setStSummaryFlag("Y");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }


     public void approveBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry bunga deposito : " + l);

        if (header.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + header.getStMonths() + " dan tahun " + header.getStYears() + " tsb sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String headerAccountID = GLUtil.getAccountByCode(header.getStHeaderAccountNo()).getStAccountID();

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView j = (BungaDepositoView) l.get(i);

                if (j.getStARBungaID() == null) {
                    throw new Exception("Bunga Deposito wajib diinput semua!");
                }

                if (j.isApproved()) {
                    throw new Exception("Bunga Deposito sudah di Approve");
                }

                j.markUpdate();
                j.setStApproved("Y");

            }

            S.store(l);

            saveJournalEntryBungaDeposito(header, l, headerAccountID);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public ARInvestmentBungaView getInvBunga(String stARBungaID) throws Exception {
        DTOPool.getInstance().clear();

        if (stARBungaID==null) return null;

        return (ARInvestmentBungaView) DTOPool.getInstance().getDTO(ARInvestmentBungaView.class, stARBungaID);
    }

     private void updateInvBunga(SQLUtil S, BungaDepositoView rl, String stARBungaID) throws Exception{
         //logger.logDebug("%%%%%%%%%%%%%%% : "+stARBungaID);
         //logger.logDebug("############### : "+rl);
        //final SQLUtil S = new SQLUtil();
        try{
                ARInvestmentBungaView invbunga = getInvBunga(stARBungaID);

                invbunga.markUpdate();

                invbunga.setDbAngka(rl.getDbAmount());
                invbunga.setDtTglBunga(rl.getDtApplyDate());
                invbunga.setStNoBuktiD(rl.getStTransactionNo());
                invbunga.setStEntityID(rl.getStHeaderAccountID());
                invbunga.setStAccountBank(rl.getStHeaderAccountNo());
                invbunga.setStBankName(rl.getStHeaderAccountMaster());
                invbunga.setStEffectiveFlag("Y");
                invbunga.setDtApprovedDate(new Date());
                invbunga.setStApprovedWho(rl.getStCreateWho());
                invbunga.setDtChangeDate(new Date());
                invbunga.setStChangeWho(rl.getStCreateWho());
                invbunga.setStYears(rl.getStYears());
                invbunga.setStMonths(rl.getStMonths());

                //13121500793
                invbunga.setStRegister(
                        invbunga.getStNoBuktiD().substring(1,7)+
                        invbunga.getStNoBuktiD().substring(14,19));

                S.store(invbunga);
                //S.release();
        }catch(Exception e) {
            ctx.setRollbackOnly();
            throw e;
        }

     }

    public void saveJournalMemorial(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (header.isPosted()) {
            throw new RuntimeException("Transaksi Untuk Kantor Pusat Bulan " + header.getStMonths() + " dan Tahun " + header.getStYears() + " sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        header.setStMethodCode("F");
        header.setStJournalCode("AOG"); 

        final JournalView fj = (JournalView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        String stTransactionNo = null;

        if (header.isNew()) {
            stTransactionNo = generateTransactionNo(header, l, transNo);
            header.setStTransactionNo(stTransactionNo);
        }

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        String tahun = header.getStYears();
        String bulan = header.getStMonths();

        boolean notBalance = false;
        if (!reversing) {
            if (BDUtil.biggerThan(bal, new BigDecimal(2))) {
                notBalance = true;
            }
            if (BDUtil.lesserThan(bal, new BigDecimal(-2))) {
                notBalance = true;
            }

            if(notBalance) throw new RuntimeException("Jurnal tidak balance (selisih = "+bal+")\n "+l);

            if (Tools.compare(bal, BDUtil.zero) != 0) {
                //throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("MEMORIAL");
                j.setStIDRFlag("Y");
                j.setStMethodCode("F");
                j.setStJournalCode("AOG");
                j.setStCurrencyCode("IDR");
                j.setDbCurrencyRate(new BigDecimal(1));

                if (!Tools.isEqual(DateUtil.getMonth2Digit(j.getDtApplyDate()), bulan)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                }
                if (!Tools.isEqual(DateUtil.getYear(j.getDtApplyDate()), tahun)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                }

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveClosing(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSING")));
            }

            S.store(closing);

            if(false){

                if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isNo(closing.getStFinanceClosingStatus())) {
                    if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD")) {

                        final DTOList reinsData = closing.getReinsClaimInwardAll();
                        updateDataInward(reinsData, closing);
                    }
                }

                if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isYes(closing.getStFinanceClosingStatus())) {
                    if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD")) {

                        final DTOList reinsData = closing.getReinsAll();

                        postReasCumullation(reinsData, closing);

                    } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")) {

                        final DTOList reinsData = closing.getReinsClaimAll();

                        postClaimReasCumullation(reinsData, closing);

                    }else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD_TO_OUTWARD")) {

                        final DTOList reinsData = closing.getReinsClaimInwardToOutwardAll();

                        postClaimReasCumullation(reinsData, closing);

                    }else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD")) {

                        final DTOList reinsData = closing.getReinsClaimInwardAll();

                        updateDataInward(reinsData, closing);

                        postClaimInwardCumullation(reinsData, closing);
                    } else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_OUTWARD")) {

                        final DTOList reinsData = closing.getReinsAll();

                        postReasCumullation(reinsData, closing);

                    }else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD_XOL"))
                    {
                      DTOList reinsData = closing.getReinsOutwardXOL();

                      updateDataInward(reinsData, closing);

                      postReinsOutwardXOLCumullation(reinsData, closing);
                    }
                    else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_FAC"))
                    {
                      DTOList reinsData = closing.getReinsInwardFac();

                      updateDataInward(reinsData, closing);

                      postReinsInwardFacCumullation(reinsData, closing);
                    }else if (closing.getStClosingType().equalsIgnoreCase("PROFIT_COMMISION_INWARD"))
                    {
                      DTOList reinsData = closing.getReinsProfitCommision();

                      updateDataInward(reinsData, closing);

                      postReinsProfitCommision(reinsData, closing);
                    }
            }
            
            }

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void saveJournalClosing(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {

            if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isNo(closing.getStFinanceClosingStatus())) {
                if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD")) {

                    final DTOList reinsData = closing.getReinsClaimInwardAll();
                    updateDataInward(reinsData, closing);
                }
            }

            if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isYes(closing.getStFinanceClosingStatus())) {
                if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD")) {

                    final DTOList reinsData = closing.getReinsAllInvoice();

                    postReasCumullation(reinsData, closing);

                } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")) {

                    final DTOList reinsData = closing.getReinsClaimAll();

                    postClaimReasCumullation(reinsData, closing);

                }else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD_TO_OUTWARD")) {

                    final DTOList reinsData = closing.getReinsClaimInwardToOutwardAll();

                    postClaimReasCumullation(reinsData, closing);

                }else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD")) {

                    final DTOList reinsData = closing.getReinsClaimInwardAll();

                    updateDataInward(reinsData, closing);

                    postClaimInwardCumullation(reinsData, closing);
                } else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_OUTWARD")) {

                    final DTOList reinsData = closing.getReinsAll();

                    postReasCumullation(reinsData, closing);

                }else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD_XOL"))
                {
                  DTOList reinsData = closing.getReinsOutwardXOL();

                  updateDataInward(reinsData, closing);

                  postReinsOutwardXOLCumullation(reinsData, closing);
                }
                else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_FAC"))
                {
                  DTOList reinsData = closing.getReinsInwardFac();

                  updateDataInward(reinsData, closing);

                  postReinsInwardFacCumullation(reinsData, closing);
                }else if (closing.getStClosingType().equalsIgnoreCase("PROFIT_COMMISION_INWARD"))
                {
                  DTOList reinsData = closing.getReinsProfitCommision();

                  updateDataInward(reinsData, closing);

                  postReinsProfitCommision(reinsData, closing);
                }
            }

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

     private void postReasCumullation(DTOList reinsAll, InsuranceClosingView closing) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList reins = reinsAll;

        for (int i = 0; i < reins.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) reins.get(i);

            if (ri.getTreatyDetail().isOR()) {
                continue;
            }

//            if (BDUtil.isZeroOrNull(ri.getDbPremiAmount())) {
//                continue;
//            }

            if (ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) {
                continue;
            }

            if (ri.getTreatyDetail().getTreatyType().getStJournalFlag() == null) {
                continue;
            }

            if (ri.getTreatyDetail().getStARTrxLineID() == null) {
                continue;
            }

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, ri.getStPolicyID());

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            if (closing.isClosingRIOutward() || closing.isClosingRIInwardToOutward()) {
                invoice.setStRefID0("REINS");
                invoice.setStInvoiceNo("I" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                // I 01 040120200913001000
            } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")) {
                invoice.setStRefID0("CLAIMREINS");
                invoice.setStInvoiceNo("N" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                // I 01 040120200913001000
            }

            if(ri.isInstallment()) invoice.setStInvoiceNo(invoice.getStInvoiceNo()+"-"+ri.getStInstallmentNumber());

            Date invoiceDate = pol.getDtPolicyDate();

            if (closing.getDtInvoiceDate() != null) {
                invoiceDate = closing.getDtInvoiceDate();
            }

            invoice.setDtInvoiceDate(invoiceDate);
            invoice.setDtDueDate(invoiceDate);

            if(ri.isInstallment()) invoice.setDtDueDate(ri.getDtInstallmentDate());

            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(ri.getStMemberEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());

            invoice.setStARTransactionTypeID(ri.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());

            invoice.setStReferenceD0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
            invoice.setStReferenceD1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceE0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
            invoice.setStReferenceE1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceZ0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode3());
            invoice.setStReferenceZ1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceC0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode6());
            invoice.setStReferenceC1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setDtReferenceDate1(ri.getDtBindingDate());

            invoice.setStReferenceA1(ri.getStRISlipNo());

            invoice.setStReinsEntID(ri.getStReinsuranceEntityID());

            //bikin surat hutang
            //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
            //"218/BPDAN/2/2011"
            if (ri.getDtValidReinsuranceDate() == null) {
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()
                        + "/"
                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
                        + "/"
                        + DateUtil.getQuartalRomawi(invoiceDate)
                        + "/"
                        + DateUtil.getYear(invoiceDate));
            }

            if (ri.getDtValidReinsuranceDate() != null) {
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID() + "."
                        + String.valueOf(i + 1)
                        + "/"
                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
                        + "/"
                        + DateUtil.getQuartalRomawi(invoiceDate)
                        + "/"
                        + DateUtil.getYear(invoiceDate));
            }

            if (closing.getStNoSuratHutang() != null) {
                invoice.setStNoSuratHutang(closing.getStNoSuratHutang());
            }


            invoice.setDtSuratHutangPeriodFrom(invoiceDate);
            invoice.setDtSuratHutangPeriodTo(invoiceDate);
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class);

            {

                for (int v = 0; v < artlines.size(); v++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);

                    if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(ri.getDbPremiAmountEdited());

                        //if(ri.getDbPremiAmountEdited()!=null){
                        //ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbPremiAmount(), ri.getDbPremiAmountEdited()));
                        //}
                    } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {

                        if (BDUtil.isZeroOrNull(ri.getDbRICommAmount())) {
                            //continue;
                        }

                        if (invoice.getStReferenceZ0() == null) {
                            throw new RuntimeException("Comission Account Code not found for " + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        }

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(ri.getDbRICommAmountEdited());

//                        if(ri.getDbRICommAmountEdited()!=null){
//                            ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbRICommAmount(),ri.getDbRICommAmountEdited()));
//                        }
                    } //else throw new RuntimeException("Unknown Item class : " + artl);
                }
            }

            invoice.recalculate();

            getRemoteAccountReceivable().saveInvoiceReins(invoice);

            SQLUtil S = new SQLUtil();

            PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");

            PS.setObject(1, closing.getStClosingID());

            int j = PS.executeUpdate();

            if (j != 0) {
                logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
            }

            S.release();
        }


    }

    public void reverseJournalSyariah(String trx_no) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update gl_je_detail_syariah set approved = null where trx_no = ?");

            P.setObject(1, trx_no);
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }

    }


    public void saveBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry bunga deposito : " + l);

        if (header.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + header.getStMonths() + " dan tahun " + header.getStYears() + " tsb sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final BungaDepositoView fj = (BungaDepositoView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();
        String stTransactionHeaderNo = fj.getStTransactionHeaderNo();

        String lgHeaderAccountID = header.getStHeaderAccountID();
        String stHeaderAccountNo = header.getStHeaderAccountNo();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("BDHNEW"));
            stTransactionHeaderNo = header.getStTransactionHeaderNo();
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = header.getStTransactionNo();
        if (header.isNew()) {
            trxNo = generateBudepNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView j = (BungaDepositoView) l.get(i);

                //TitipanPremiView j2 = (TitipanPremiView) l.get(i);

                j.setStHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);
                j.setStMonths(header.getStMonths());
                j.setStYears(header.getStYears());
                j.setStMethodCode(header.getStMethodCode());
                j.setStHeaderAccountID(header.getStHeaderAccountID());
                j.setStHeaderAccountMaster(header.getStHeaderAccountMaster());
                j.setStCostCenter(header.getStCostCenter());
                j.setStCurrencyCode("IDR");
                j.setDbCurrencyRate(new BigDecimal(1));

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("BDNEW")));
                    j.setStTransactionNo(generateBudepNo(header, l, transNo));
                    j.setStTransactionHeaderID(stTransactionHeaderID);
                    j.setStTransactionHeaderNo(stTransactionHeaderID + "/" + stTransactionHeaderNo);
                }

                //if (j.getDtApplyDate()==null) throw new Exception("Apply Date is not defined !");
                if (j.getDtApplyDate() != null) {
                    final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());
                }

                j.setStRefTrxType("BUDEP");

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void reverseBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception {

        if (header.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + header.getStMonths() + " dan tahun " + header.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView j = (BungaDepositoView) l.get(i);

                if (!j.isApproved()) throw new Exception("Bunga Deposito belum di Setujui");

                j.markUpdate();

                j.setStApproved(null);

                PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

                P2.setObject(1, j.getStTransactionNo());
                int r2 = P2.executeUpdate();
                S.release();

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void deleteBungaDeposito(String trx_no, DTOList l) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView j = (BungaDepositoView) l.get(i);

                if (j.isApproved()) throw new Exception("Bunga Deposito belum di Reverse");

            }

            PreparedStatement P2 = S.setQuery("delete from ar_bunga_deposito where trx_hdr_no = ?");

            P2.setObject(1, trx_no);
            int r2 = P2.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }


    public void saveInputBilyetDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry bunga deposito : " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                BungaDepositoView j = (BungaDepositoView) l.get(i);

                if (j.getStARBungaID() != null) {
                    updateInvBunga(S, j, j.getStARBungaID());
                }

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void reverseClosing(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSING")));
            }

            S.store(closing);

            String nomorSuratHutang = closing.getStNoSuratHutang();

            PreparedStatement P = S.setQuery("delete "+
                                            " from gl_je_detail "+
                                            " where substr(trx_no,1,1) in ('I','M') and recap_no = ?");

            P.setObject(1, nomorSuratHutang);
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from ar_invoice_details "+
                                                " where ar_invoice_id in ( "+
                                                " select ar_invoice_id "+
                                                " from ar_invoice "+
                                                " where no_surat_hutang = ? )");

            P2.setObject(1, nomorSuratHutang);
            int r2 = P2.executeUpdate();
            S.release();

            PreparedStatement P3 = S.setQuery("delete "+
                                              "  from ar_invoice "+
                                              "  where no_surat_hutang = ?");

            P3.setObject(1, nomorSuratHutang);
            int r3 = P3.executeUpdate();
            S.release();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public DTOList getRKAPEntry(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                " select a.* "
                + " from gl_rkap_group a "
                + " a.active_flag = 'Y' "
                + " where a.trx_hdr_id = ? "
                + " order by a.rkap_group_id ",
                new Object[]{trxhdrid},
                RKAPGroupView.class);
    }

    public void saveReportRKAP(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final RKAPGroupView fj = (RKAPGroupView) l.get(0);

        String stTransactionHeaderID = fj.getStRKAPTransactionHeaderID();
        String stYears = fj.getStYears();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHRKAP"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                RKAPGroupView j = (RKAPGroupView) l.get(i);

                j.setStRKAPTransactionHeaderID(stTransactionHeaderID);

                //j.reCalculate();

                if (j.isNew()) {
                    j.setStRKAPTransactionGroupID(String.valueOf(IDFactory.createNumericID("GLERKAP")));
                    j.setStRKAPTransactionNo(stTransactionHeaderID + stYears);
                }
            }

            S.store(l);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveAccounts(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                AccountView2 j = (AccountView2) l.get(i);


                if (j.isNew()) {
                    j.setStAccountID(String.valueOf(IDFactory.createNumericID("ACC")));
                }
            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveNoper(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                AccountView2 j = (AccountView2) l.get(i);

                j.markUpdate();

                PreparedStatement P = S.setQuery("update gl_accounts set noper = ? where account_id = ? ");

                P.setObject(1, j.getStNoper());
                P.setObject(2, j.getStAccountID());
                int r = P.executeUpdate();
                S.release();
            }

            //S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateDataInward(DTOList l, InsuranceClosingView closing) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyInwardView j = (InsurancePolicyInwardView) l.get(i);

                //j.markUpdate();

                if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isNo(closing.getStFinanceClosingStatus())) {
                    PreparedStatement P = S.setQuery("update ins_pol_inward set no_surat_hutang = ? where ar_invoice_id = ? ");
                    P.setObject(1, closing.getStNoSuratHutang());
                    P.setObject(2, j.getStARInvoiceID());
                    int r = P.executeUpdate();
                    S.release();
                }

                if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isYes(closing.getStFinanceClosingStatus())) {

                    PreparedStatement P = S.setQuery("delete "
                            + " from gl_je_detail "
                            + " where trx_no = ? and ref_trx_no = ? ");

                    P.setObject(1, j.getStInvoiceNo());
                    P.setObject(2, j.getStARInvoiceID());
                    int r1 = P.executeUpdate();
                    S.release();

                    PreparedStatement P2 = S.setQuery("delete from ar_invoice_details "
                            + " where ar_invoice_id in ( "
                            + " select ar_invoice_id "
                            + " from ar_invoice "
                            + " where invoice_no = ? and attr_pol_no = ? and attr_underwrit = ? and refid0 = ? )");

                    P2.setObject(1, j.getStInvoiceNo());
                    P2.setObject(2, j.getStAttrPolicyNo());
                    P2.setObject(3, j.getStAttrUnderwriting());
                    P2.setObject(4, j.getStRefID0());
                    int r2 = P2.executeUpdate();
                    S.release();

                    PreparedStatement P3 = S.setQuery("delete "
                            + "  from ar_invoice "
                            + " where invoice_no = ? and attr_pol_no = ? and attr_underwrit = ? and refid0 = ? ");

                    P3.setObject(1, j.getStInvoiceNo());
                    P3.setObject(2, j.getStAttrPolicyNo());
                    P3.setObject(3, j.getStAttrUnderwriting());
                    P3.setObject(4, j.getStRefID0());
                    int r3 = P3.executeUpdate();
                    S.release();

                    PreparedStatement P5 = S.setQuery("update ins_pol_inward set invoice_date = ? where ar_invoice_id = ? ");
                    P5.setObject(1, closing.getDtInvoiceDate());
                    P5.setObject(2, j.getStARInvoiceID());
                    int r5 = P5.executeUpdate();
                    S.release();
                }

                //S.release();
            }

            //S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    private void postClaimInwardCumullation(DTOList reinsAll, InsuranceClosingView closing) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList reins = reinsAll;

        SQLUtil S = new SQLUtil();

        for (int i = 0; i < reins.size(); i++) {
            InsurancePolicyInwardView ri = (InsurancePolicyInwardView) reins.get(i);

            if (ri.getStARTransactionTypeID().equalsIgnoreCase("18")) {
                getRemoteAccountReceivable().saveSaldoAwalInwardClosing(ri, closing);
            } else {
                getRemoteAccountReceivable().saveClaimInwardClosing(ri, closing, null);
            }

            PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");
            PS.setObject(1, closing.getStClosingID());
            int j = PS.executeUpdate();

            if (j != 0) {
                logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
            }

            S.release();

        }
    }

    public void reverseClosingClaimInward(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSING")));
            }

            S.store(closing);

            String nomorSuratHutang = closing.getStNoSuratHutang();

            PreparedStatement P = S.setQuery("delete "
                    + " from gl_je_detail "
                    + " where recap_no = ?");

            P.setObject(1, nomorSuratHutang);
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from ar_invoice_details "
                    + " where ar_invoice_id in ( "
                    + " select ar_invoice_id "
                    + " from ar_invoice "
                    + " where no_surat_hutang = ? )");

            P2.setObject(1, nomorSuratHutang);
            int r2 = P2.executeUpdate();
            S.release();

            PreparedStatement P3 = S.setQuery("delete "
                    + "  from ar_invoice "
                    + "  where no_surat_hutang = ?");

            P3.setObject(1, nomorSuratHutang);
            int r3 = P3.executeUpdate();
            S.release();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

   public void AccountNeracaItem(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query = "update gl_acct_bal_neracatotal set cr" + periodTo + " = 0,db" + periodTo + " = " + neracaItem + ", bal" + periodTo + " = " + neracaItem + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal_neracatotal (account_id,period_year,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setBigDecimal(3, neracaItem);
                PS.setBigDecimal(4, neracaItem);
                PS.setObject(5, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

   public void AccountNeracaItemPerBulan(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query = "update gl_acct_bal_neracaperbulan set cr" + periodTo + " = 0,db" + periodTo + " = " + neracaItem + ", bal" + periodTo + " = " + neracaItem + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal_neracaperbulan (account_id,period_year,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setBigDecimal(3, neracaItem);
                PS.setBigDecimal(4, neracaItem);
                PS.setObject(5, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    private void postClaimReasCumullation(DTOList reinsAll, InsuranceClosingView closing) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList reins = reinsAll;

        for (int i = 0; i < reins.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) reins.get(i);

            if (ri.getTreatyDetail().isOR()) {
                continue;
            }

//            if (BDUtil.isZeroOrNull(ri.getDbPremiAmount())) {
//                continue;
//            }

            if (ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) {
                continue;
            }

            if (ri.getTreatyDetail().getTreatyType().getStJournalFlag() == null) {
                continue;
            }

            if (ri.getTreatyDetail().getStARTrxLineID() == null) {
                continue;
            }

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, ri.getStPolicyID());

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD")) {
                invoice.setStRefID0("REINS");
                invoice.setStInvoiceNo("I" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                // I 01 040120200913001000
            } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")||closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD_TO_OUTWARD")) {
                invoice.setStRefID0("CLAIMREINS");
                invoice.setStInvoiceNo("N" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                // I 01 040120200913001000
            }

            Date invoiceDate = pol.getDtDLADate();

            if (closing.getDtInvoiceDate() != null) {
                invoiceDate = closing.getDtInvoiceDate();
            }

            invoice.setDtInvoiceDate(invoiceDate);
            invoice.setDtDueDate(invoiceDate);
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(ri.getStMemberEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());

            //invoice.setStARTransactionTypeID(ri.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
            invoice.setStARTransactionTypeID(ri.getTreatyDetail().getARTrxLineClaim().getStARTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());

            invoice.setStReferenceD0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
            invoice.setStReferenceD1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceE0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
            invoice.setStReferenceE1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceZ0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode4());
            invoice.setStReferenceZ1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceC0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode5());
            invoice.setStReferenceC1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());


            invoice.setStRefID1(pol.getStPLANo());
            invoice.setStRefID2(pol.getStDLANo());

            invoice.setStReferenceA1(ri.getStRISlipNo());

            //bikin surat hutang
            //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
            //"218/BPDAN/2/2011"
            if (ri.getDtValidReinsuranceDate() == null) {
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()
                        + "/"
                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
                        + "/"
                        + DateUtil.getQuartalRomawi(invoiceDate)
                        + "/"
                        + DateUtil.getYear(invoiceDate));
            }

            if (ri.getDtValidReinsuranceDate() != null) {
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID() + "."
                        + String.valueOf(i + 1)
                        + "/"
                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
                        + "/"
                        + DateUtil.getQuartalRomawi(invoiceDate)
                        + "/"
                        + DateUtil.getYear(invoiceDate));
            }

            if (closing.getStNoSuratHutang() != null) {
                invoice.setStNoSuratHutang(closing.getStNoSuratHutang());
            }


            invoice.setDtSuratHutangPeriodFrom(invoiceDate);
            invoice.setDtSuratHutangPeriodTo(invoiceDate);
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class);

            {

                for (int v = 0; v < artlines.size(); v++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);

                    if ("KLAIM".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(ri.getDbPremiAmountEdited());
                    } //else throw new RuntimeException("Unknown Item class : " + artl);
                }
            }

            invoice.recalculateClaimRIOutward(pol.getDtDLADate());

            getRemoteAccountReceivable().saveInvoiceClaimReins(invoice);

            SQLUtil S = new SQLUtil();

            PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");

            PS.setObject(1, closing.getStClosingID());

            int j = PS.executeUpdate();

            if (j != 0) {
                logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
            }

            S.release();
        }


    }

    public void saveClosingSetting(ClosingHeaderView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {

            if (closing.isNew()) {
                closing.setStClosingPeriodID(String.valueOf(IDFactory.createNumericID("CLOSING_PERIOD_ID")));
            }

            S.store(closing);

            final DTOList details = closing.getDetails();

             for (int i = 0; i < details.size(); i++) {
                ClosingDetailView det = (ClosingDetailView) details.get(i);

                if (det.isNew()) det.setStClosingPeriodDetailID(String.valueOf(IDFactory.createNumericID("CLOSING_PERIOD_DTL_ID")));

                det.setStClosingPeriodID(closing.getStClosingPeriodID());
             }

             S.store(details);


        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public String saveBunga2(ARInvestmentDepositoView deposito, boolean bungaMode) throws Exception {

        if (!bungaMode) {
            if (deposito.isPosted()) {
                throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
            }

            boolean checkPeriod = Tools.compare(deposito.getDtTglawal(), new Date()) >= 0;
            if (checkPeriod) {
                throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
            }
        }

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
                deposito.setStStatus("DEPOSITO");
                deposito.setStJournalStatus("NEW");
                deposito.setStKonter("0");
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("N");
                deposito.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtCreateDate(new Date());

                deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
                deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));
                BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

            }

            BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());


            if (bungaMode) {

                final DTOList bunga = deposito.getBunga();

                for (int i = 0; i < bunga.size(); i++) {
                    ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(i);

                    Date tglAwal = DateUtil.getDate(DateUtil.getDays2Digit(deposito.getDtTglakhir()) + "/" + DateUtil.getMonth2Digit(bng.getDtTglBunga()) + "/" + DateUtil.getYear(bng.getDtTglBunga()));
                    Date tglAkhir = advanceMonth(tglAwal, 1);

                    if (bng.isNew()) {

                        bng.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                        bng.setDtCreateDate(new Date());
                        bng.setStCreateWho(SessionManager.getInstance().getSession().getStUserID());

                        bng.setStARDepoID(deposito.getStARDepoID());
                        bng.setDtTglBunga(bng.getDtTglBunga());
                        bng.setStNodefo(deposito.getStNodefo());
                        bng.setStNoBuktiB(deposito.getStBuktiB());
                        bng.setStCostCenterCode(deposito.getStCostCenterCode());
                        bng.setStReceiptClassID(deposito.getStReceiptClassID());
                        bng.setStKodedepo(deposito.getStKodedepo());
                        bng.setStRegisterBentuk(deposito.getStRegister());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStCurrency(deposito.getStCurrency());
                        bng.setDbCurrencyRate(deposito.getDbCurrencyRate());
                        bng.setDbNominal(deposito.getDbNominal());
                        bng.setDbNominalKurs(deposito.getDbNominalKurs());
                        bng.setDbPersen(deposito.getDbBunga());
                        bng.setDbPajak(deposito.getDbPajak());
                        bng.setDtTglAwal(tglAwal);
                        bng.setDtTglAkhir(tglAkhir);
                        bng.setStCompanyType(deposito.getStCompanyType());
                        bng.setStActiveFlag("Y");
                        bng.setStEffectiveFlag("N");
                        bng.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                        bng.setStDepoName(deposito.getStDepoName());
                        bng.setStAccountDepo(deposito.getStAccountDepo());
                        bng.setStNoRekening(deposito.getStNoRekening());
                        bng.setDbAngka1(bunga1);
                        bng.setStMonths(DateUtil.getMonth2Digit(bng.getDtTglBunga()));
                        bng.setStYears(DateUtil.getYear(bng.getDtTglBunga()));
                        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                            bng.setStHari(deposito.getStHari());
                        } else if (deposito.getStKodedepo().equalsIgnoreCase("2")) {
                            bng.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(deposito.getStBulan()), new BigDecimal(30))));
                        }
                    }
                }

                S.store2(bunga);

            }

            S.store(deposito);

            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

   public void approvePerpanjangan(ARInvestmentDepositoView deposito, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) l.get(i);

                ARInvestmentPerpanjanganView j = new ARInvestmentPerpanjanganView();
                j.markNew();

                j.setStARDepoID(depo.getStARDepoID());
                j.setStNodefo(depo.getStNodefo());
                j.setStKonter(depo.getStKonter());
                j.setStBuktiB(depo.getStBuktiB());
                j.setStNoRekeningDeposito(depo.getStNoRekeningDeposito());
                j.setStKodedepo(depo.getStKodedepo());
                j.setStCreateWho(depo.getStCreateWho());
                j.setDtCreateDate(depo.getDtCreateDate());
                j.setStCurrency(depo.getStCurrency());
                j.setDbCurrencyRate(depo.getDbCurrencyRate());
                j.setDbNominal(depo.getDbNominal());
                j.setDbNominalKurs(depo.getDbNominalKurs());
                j.setDtTglawal(depo.getDtTglawal());
                j.setDtTglakhir(depo.getDtTglakhir());
                j.setStBulan(depo.getStBulan());
                j.setStHari(depo.getStHari());
                j.setDbBunga(depo.getDbBunga());
                j.setDbPajak(depo.getDbPajak());
                j.setStCostCenterCode(depo.getStCostCenterCode());
                j.setStCompanyType(depo.getStCompanyType());
                j.setStEntityID(depo.getStEntityID());
                j.setDtTgldepo(depo.getDtTgldepo());
                j.setDtTglmuta(depo.getDtTglmuta());
                j.setStKeterangan(depo.getStKeterangan());
                j.setStRegister(depo.getStRegister());
                j.setStDepoName(depo.getStDepoName());
                j.setStBankName(depo.getStBankName());
                j.setStEffectiveFlag(depo.getStEffectiveFlag());
                j.setStReceiptClassID(depo.getStReceiptClassID());
                j.setStStatus(depo.getStStatus());
                j.setStApprovedWho(depo.getStApprovedWho());
                j.setDtApprovedDate(depo.getDtApprovedDate());
                j.setStARParentID(depo.getStARParentID());
                j.setStActiveFlag(depo.getStActiveFlag());
                j.setStAccountDepo(depo.getStAccountDepo());
                j.setStAccountBank(depo.getStAccountBank());
                j.setStNoRekening(depo.getStNoRekening());
                j.setStJournalStatus(depo.getStJournalStatus());
                j.setStMonths(depo.getStMonths());
                j.setStYears(depo.getStYears());

                S.store(j);

            }
            //akhir simpen lawan

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

   public void saveJournalCashBank(JournalHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (header.isPosted()) {
            throw new RuntimeException("Transaksi Untuk Kantor Pusat Bulan " + header.getStMonths() + " dan Tahun " + header.getStYears() + " sudah diposting");
        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final JournalView fj = (JournalView) l.get(0);

        header.setStMethodCode(fj.getStMethodCode());
        header.setStJournalCode("AOG");

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        String stTransactionNo = null;

        if (header.isNew()) {
            stTransactionNo = generateTransactionNo(header, l, transNo);
            header.setStTransactionNo(stTransactionNo);
        }

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalance(l);

        String tahun = header.getStYears();
        String bulan = header.getStMonths();

        boolean notBalance = false;
        if (!reversing) {
            if (BDUtil.biggerThan(bal, new BigDecimal(2))) {
                notBalance = true;
            }
            if (BDUtil.lesserThan(bal, new BigDecimal(-2))) {
                notBalance = true;
            }

            if(notBalance) throw new RuntimeException("Jurnal tidak balance (selisih = "+bal+")\n "+l);

            if (Tools.compare(bal, BDUtil.zero) != 0) {
                //throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                JournalView j = (JournalView) l.get(i);

                AccountMarketingView j3 = new AccountMarketingView();

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("CASHBANK");
                j.setStIDRFlag("Y");
                //j.setStMethodCode("F");
                j.setStJournalCode("AOG");
                j.setStCurrencyCode("IDR");
                j.setDbCurrencyRate(new BigDecimal(1));

                if (!Tools.isEqual(DateUtil.getMonth2Digit(j.getDtApplyDate()), bulan)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan bulan transaksi pada Jurnal : " + j.getStDescription());
                }
                if (!Tools.isEqual(DateUtil.getYear(j.getDtApplyDate()), tahun)) {
                    throw new Exception("Tanggal Jurnal tidak sama dengan tahun transaksi pada Jurnal : " + j.getStDescription());
                }

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                if (j.getStAccountNo().contains("811") || j.getStAccountNo().contains("813")) {

                    j3.markNew();

                    BigDecimal amount = new BigDecimal(0);
                    if (BDUtil.isZero(j.getDbCredit())) {
                        amount = j.getDbDebit();
                    } else if (BDUtil.isZero(j.getDbDebit())) {
                        amount = BDUtil.negate(j.getDbCredit());
                    }

                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLACCMKT")));
                        j3.setStNoBukti(j.getStTransactionNo());
                        j3.setStAccountID(getAccountByAccountNo(j.getStAccountNo()).getStAccountID());
                        j3.setStAccountNo(j.getStAccountNo());
                        j3.setStDescription(j.getStDescription());
                        j3.setStMonths(j.getStMonths());
                        j3.setStYears(j.getStYears());
                        j3.setDbAmount(amount);
                        j3.setDtApplyDate(j.getDtApplyDate());
                        if (j3.getStAccountNo().contains("811")) {
                            j3.setStSubType("PROMOSI");
                        } else if (j3.getStAccountNo().contains("813")) {
                            j3.setStSubType("MARKETING");
                        }
                    }

                    S.store(j3);
                }
            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

   public void saveClosingTax(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSING")));
            }

            closing.setStClosingType("TAX_PPH");

            S.store(closing);

            if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isYes(closing.getStFinanceClosingStatus())) {
                final DTOList taxData = closing.getTaxAll();

                updateDataTax(taxData, closing);
            }

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void updateDataTax(DTOList l, InsuranceClosingView closing) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        try {
            for (int i = 0; i < l.size(); i++) {
                ARInvoiceView j = (ARInvoiceView) l.get(i);

                if (Tools.isYes(closing.getStReinsuranceClosingStatus()) && Tools.isYes(closing.getStFinanceClosingStatus())) {

                    PreparedStatement P = S.setQuery("update ar_invoice set approved_flag = 'Y', claim_no = ? "
                            + " where ar_invoice_id = ? ");

                    P.setObject(1, closing.getStNoSuratHutang());
                    P.setObject(2, j.getStARInvoiceID());
                    int r = P.executeUpdate();
                    S.release();
                }
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void reverseClosingTax(InsuranceClosingView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSING")));
            }

            S.store(closing);

            String nomorSuratHutang = closing.getStNoSuratHutang();

            PreparedStatement P = S.setQuery("update ar_invoice set approved_flag = null "
                    + "  where claim_no = ?");

            P.setObject(1, nomorSuratHutang);
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

     public ARRequestFee loadRequest(String arreqid) throws Exception {
        final ARRequestFee req = (ARRequestFee) ListUtil.getDTOListFromQuery(
                "select * from ar_request_fee where req_id = ?",
                new Object[]{arreqid},
                ARRequestFee.class).getDTO();

        return req;
    }

     public void saveRequest(ARRequestFee arrequest, String stNextStatus, boolean approvalMode, DTOList l) throws Exception {

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ status : " + approvalMode);

        if (approvalMode) {
            if (arrequest.isPosted()) {
                throw new RuntimeException("Transaksi bulan " + arrequest.getStMonths() + " dan tahun " + arrequest.getStYears() + " tsb sudah diposting");
            }
        }

        SQLUtil S = new SQLUtil();

        try {

            if (stNextStatus != null) {

                if ("CSB".equalsIgnoreCase(stNextStatus) || "RFD".equalsIgnoreCase(stNextStatus)) {
                    final ARRequestFee oldRequest = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arrequest.getStARRequestID());

                    oldRequest.markUpdate();

                    S.store(oldRequest);

                } else {
                    final ARRequestFee oldRequest = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arrequest.getStARRequestID());

                    oldRequest.markUpdate();

                    oldRequest.setStActiveFlag("N");

                    S.store(oldRequest);
                }

                arrequest.setStParentID(arrequest.getStARRequestID());
                arrequest.markNew();
                arrequest.setStActiveFlag("Y");
                arrequest.setStStatus(stNextStatus);
                arrequest.setStApprovedWho(null);
                arrequest.setDtApprovedDate(null);
                arrequest.setStCashierFlag(null);
                arrequest.setStCashierWho(null);
                arrequest.setDtCashierDate(null);
                arrequest.setStRootID(arrequest.getStRootID());

                arrequest.getReqObject().convertAllToNew();
                arrequest.getDocuments().convertAllToNew();

            }

            if (arrequest.isNew()) {
                arrequest.setStARRequestID(String.valueOf(IDFactory.createNumericID("INVREQUEST")));
            }

            if (arrequest.getStRootID() == null) {
                arrequest.setStRootID(arrequest.getStARRequestID());
            }

            final DTOList documents = arrequest.getDocuments();

            for (int j = 0; j < documents.size(); j++) {
                ARRequestDocumentsView doc = (ARRequestDocumentsView) documents.get(j);

                if (doc.isNew()) {
                    doc.setStDocumentInID(String.valueOf(IDFactory.createNumericID("INVREQUESTDOC")));
                }

                doc.setStInID(arrequest.getStARRequestID());
            }

            S.store(documents);

            final DTOList policyDocuments = arrequest.getPolicyDocuments();

            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                doc.setStPolicyID(arrequest.getStARRequestID());

                final boolean marked = doc.isMarked();

                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null) {
                        doc.markUpdate();
                    } else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }

                if (!marked && doc.getStInsurancePolicyDocumentID() != null) {
                    doc.markDelete();
                }
            }

            S.store(policyDocuments);

            BigDecimal nominalUsed = null;

            final DTOList reqObj = arrequest.getReqObject();

            for (int i = 0; i < reqObj.size(); i++) {
                ARRequestFeeObj obj = (ARRequestFeeObj) reqObj.get(i);

                if (obj.isNew()) {
                    obj.setStARRequestObjID(String.valueOf(IDFactory.createNumericID("INVREQUESTOBJ")));
                }

                nominalUsed = BDUtil.add(nominalUsed, obj.getDbNominalRealisasi());

                obj.setStARRequestID(arrequest.getStARRequestID());
            }

            S.store(reqObj);

            arrequest.setDbNominalUsed(nominalUsed);

            //OLD
//            if ("REQ".equalsIgnoreCase(arrequest.getStStatus())) {
//                if (arrequest.getStRequestNo() == null) {
//                    arrequest.generateNoRequest();
//                }
//            }
//
//            if (arrequest.isStatusApproval() || arrequest.isStatusCashback()) {
//                if (arrequest.isCashierFlag()) {
//                    if (arrequest.getStTransactionNo() == null) {
//                        arrequest.generateNoTransaction();
//                    }
//                }
//            }

            if ("APP".equalsIgnoreCase(arrequest.getStStatus())) {
                if (arrequest.getStRequestNo() == null) {
                    arrequest.generateNoRequest();
                }
            }

//            if ("CSB".equalsIgnoreCase(arrequest.getStStatus())
//                    || "RFD".equalsIgnoreCase(arrequest.getStStatus())) {
//                if (!approvalMode) {
//                    arrequest.generateEndorseNo();
//                }
//            }

            if (arrequest.isCashierFlag()) {
                if (arrequest.getStTransactionNo() == null) {
                    arrequest.generateNoTransaction();
                }
            }

            if (approvalMode && arrequest.isCashierFlag() && arrequest.isStatusApproval()) {
                approveCashier(arrequest);
            }
            if (approvalMode && arrequest.isCashierFlag() && arrequest.isStatusCashback()) {
                approveCashBack(arrequest, l);
            }
            if (approvalMode && arrequest.isCashierFlag() && arrequest.isStatusRefund()) {
                approveRefund(arrequest);
            }

            S.store(arrequest);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void approveCashier(ARRequestFee req) throws Exception {

        if (req.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + req.getStMonths() + " dan tahun " + req.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        if (req.getStPolicyClaimNo() != null) {
            updatePolicyRequest(req.getStARRequestID(), req.getStPolicyClaimNo());
        }

        try {
            JournalView j = new JournalView();
            j.markNew();

            JournalView j2 = new JournalView();
            j2.markNew();

            j.setStTransactionHeaderID(stTransactionHeaderID);
            j.setLgHeaderAccountID(new Long(req.getStAccountID()));
            j.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//            j.setLgAccountID(new Long(req.getStAccountID()));
            j.setLgAccountID(new Long(req.findAccountUangMuka(req.getStCostCenterCode())));
            j.setDbDebit(BDUtil.mul(req.getDbNominal(), req.getDbCurrencyRate()));
            j.setDbCredit(BDUtil.zero);
            j.setDbEnteredDebit(BDUtil.mul(req.getDbNominal(), req.getDbCurrencyRate()));
            j.setDbEnteredCredit(BDUtil.zero);
            j.setStDescription("Permintaan Biaya " + req.getStDescription());
            j.setStCurrencyCode(req.getStCurrency());
            j.setDbCurrencyRate(req.getDbCurrencyRate());
//            j.setStRefTrxNo(req.getStRegionID());
            j.setStYears(req.getStYears());
            j.setStMonths(req.getStMonths());
            j.setStIDRFlag("Y");

            j.reCalculate();

            if (j.isNew()) {
                j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j.setStTransactionNo(req.getStTransactionNo());
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(req.getDtTglRequest());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j.setLgPeriodNo(per.getLgPeriodNo());
            j.setLgFiscalYear(per.getLgFiscalYear());
//            j.setStRefTrxType("INV_REQUEST");
            j.setStRefTrxType("CASHBANK");
            j.setDtApplyDate(req.getDtTglRequest());

            if (j.isModified()) {

                final JournalView old = j.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
            }

            S.store(j);
            //simpen lawan

            BigDecimal debitOld = new BigDecimal(0);
            BigDecimal debitEnteredOld = new BigDecimal(0);

            if (j2.isNew()) {
                j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j2.setStTransactionHeaderID(stTransactionHeaderID);
                j2.setStTransactionNo(req.getStTransactionNo());
                j2.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j2.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
                j2.setLgAccountID(new Long(req.getStAccountID()));
                j2.setDbDebit(BDUtil.zero);
                j2.setDbCredit(BDUtil.mul(req.getDbNominal(), req.getDbCurrencyRate()));
                j2.setDbEnteredDebit(BDUtil.zero);
                j2.setDbEnteredCredit(BDUtil.mul(req.getDbNominal(), req.getDbCurrencyRate()));
                j2.setStDescription("Permintaan Biaya " + req.getStDescription());
                j2.setStCurrencyCode(req.getStCurrency());
                j2.setDbCurrencyRate(req.getDbCurrencyRate());
//            j2.setStRefTrxNo(req.getStRegionID());
                j2.setStYears(req.getStYears());
                j2.setStMonths(req.getStMonths());
                j2.setStIDRFlag("Y");
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + req.getDtTglRequest() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j2.setLgPeriodNo(per.getLgPeriodNo());
            j2.setLgFiscalYear(per.getLgFiscalYear());
//            j2.setStRefTrxType("INV_REQUEST");
            j2.setStRefTrxType("CASHBANK");
            j2.setDtApplyDate(req.getDtTglRequest());

            if (j2.isModified()) {

                final JournalView old = j2.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j2.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                }
            }

            S.store(j2);
            //akhir simpen lawan

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void approveCashBack(ARRequestFee req, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l.size() + " \n" + l);

        if (l.size() < 1) {
            return;
        }

        if (req.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + req.getStMonths() + " dan tahun " + req.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            JournalView j = new JournalView();
            j.markNew();

            JournalView j2 = new JournalView();
            j2.markNew();

            j.setStTransactionHeaderID(stTransactionHeaderID);
            j.setLgHeaderAccountID(new Long(req.getStAccountID()));
            j.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//            j.setLgAccountID(new Long(req.getStAccountID()));
            j.setLgAccountID(new Long(req.findAccountUangMuka(req.getStCostCenterCode())));
            j.setDbCredit(BDUtil.mul(req.getDbNominalUsed(), req.getDbCurrencyRate()));
            j.setDbDebit(BDUtil.zero);
            j.setDbEnteredCredit(BDUtil.mul(req.getDbNominalUsed(), req.getDbCurrencyRate()));
            j.setDbEnteredDebit(BDUtil.zero);
            j.setStDescription("Realisasi Biaya " + req.getStDescription());
            j.setStCurrencyCode(req.getStCurrency());
            j.setDbCurrencyRate(req.getDbCurrencyRate());
//            j.setStRefTrxNo(req.getStRegionID());
            j.setStYears(req.getStYears());
            j.setStMonths(req.getStMonths());
            j.setStIDRFlag("Y");

            j.reCalculate();

            if (j.isNew()) {
                j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j.setStTransactionNo(req.getStTransactionNo());
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }

            final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(new Date());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }

            j.setLgPeriodNo(per.getLgPeriodNo());
            j.setLgFiscalYear(per.getLgFiscalYear());
//            j.setStRefTrxType("INV_REQUESTCASH");
            j.setStRefTrxType("CASHBANK");
            j.setDtApplyDate(new Date());

            if (j.isModified()) {

                final JournalView old = j.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
            }

            S.store(j);

//            //simpan bank
//            if (j2.isNew()) {
//                j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
//                j2.setStTransactionHeaderID(stTransactionHeaderID);
//                j2.setStTransactionNo(req.getStTransactionNo());
//                j2.setLgHeaderAccountID(new Long(req.getStAccountEntityID()));
//                j2.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//                j2.setLgAccountID(new Long(req.getStAccountEntityID()));
//                j2.setDbCredit(BDUtil.zero);
//                j2.setDbDebit(BDUtil.mul(req.getDbNominalUsed(), req.getDbCurrencyRate()));
//                j2.setDbEnteredCredit(BDUtil.zero);
//                j2.setDbEnteredDebit(BDUtil.mul(req.getDbNominalUsed(), req.getDbCurrencyRate()));
//                j2.setStDescription(req.getStDescription());
//                j2.setStCurrencyCode(req.getStCurrency());
//                j2.setDbCurrencyRate(req.getDbCurrencyRate());
////            j2.setStRefTrxNo(req.getStRegionID());
//                j2.setStYears(req.getStYears());
//                j2.setStMonths(req.getStMonths());
//                j2.setStIDRFlag("Y");
//            }
//
//            if (req.getDtTglRequest() == null) {
//                throw new Exception("Apply Date is not defined !");
//            }
//            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
//            if (per == null) {
//                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + new Date() + ")");
//            }
//            if (!per.isOpen()) {
//                throw new Exception("Period is not open");
//            }
//            j2.setLgPeriodNo(per.getLgPeriodNo());
//            j2.setLgFiscalYear(per.getLgFiscalYear());
////            j2.setStRefTrxType("INV_REQUESTCASH");
//            j2.setStRefTrxType("CASHBANK");
//            j2.setDtApplyDate(new Date());
//
//            if (j2.isModified()) {
//
//                final JournalView old = j2.getOldJournal();
//
//                if (old != null) {
//
//                    final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
//                            && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;
//
//                    if (isSameRecord) {
//                        final BigDecimal am = j2.getDbAdjustmentAmount();
//
//                        if (am.compareTo(BDUtil.zero) != 0) {
//                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
//                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
//                        }
//                    } else {
//                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
//                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
//                    }
//                } else {
//                    updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
//                }
//            }
//
//            S.store(j2);

            //akhir simpen lawan
            for (int i = 0; i < l.size(); i++) {
                ARRequestFeeObj reqObj = (ARRequestFeeObj) l.get(i);

                JournalView j3 = new JournalView();
                j3.markNew();

                JournalView j4 = new JournalView();
                j4.markNew();

                j3.setStTransactionHeaderID(stTransactionHeaderID);
                j3.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j3.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//            j3.setLgAccountID(new Long(req.getStAccountID()));
                j3.setLgAccountID(new Long(reqObj.getStAccountID()));
                j3.setDbDebit(BDUtil.mul(reqObj.getDbNominalRealisasi(), req.getDbCurrencyRate()));
                j3.setDbCredit(BDUtil.zero);
                j3.setDbEnteredDebit(BDUtil.mul(reqObj.getDbNominalRealisasi(), req.getDbCurrencyRate()));
                j3.setDbEnteredCredit(BDUtil.zero);
                j3.setStDescription(reqObj.getStDescription());
                j3.setStCurrencyCode(req.getStCurrency());
                j3.setDbCurrencyRate(req.getDbCurrencyRate());
//            j3.setStRefTrxNo(req.getStRegionID());
                j3.setStYears(req.getStYears());
                j3.setStMonths(req.getStMonths());
                j3.setStIDRFlag("Y");

                j3.reCalculate();

                if (j3.isNew()) {
                    j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j3.setStTransactionNo(req.getStTransactionNo());
                }

                if (reqObj.getDtTglCashback() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per2 = PeriodManager.getInstance().getPeriodFromDate(reqObj.getDtTglCashback());
                if (per2 == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + reqObj.getDtTglCashback() + ")");
                }
                if (!per2.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j3.setLgPeriodNo(per2.getLgPeriodNo());
                j3.setLgFiscalYear(per2.getLgFiscalYear());
//            j3.setStRefTrxType("INV_REQUESTCASH");
                j3.setStRefTrxType("CASHBANK");
                j3.setDtApplyDate(reqObj.getDtTglCashback());

                if (j3.isModified()) {

                    final JournalView old = j3.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j3.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                    }
                }

                S.store(j3);

//                //simpan bank
//                if (j4.isNew()) {
//                    j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
//                    j4.setStTransactionHeaderID(stTransactionHeaderID);
//                    j4.setStTransactionNo(req.getStTransactionNo());
//                    j4.setLgHeaderAccountID(new Long(req.getStAccountEntityID()));
//                    j4.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//                    j4.setLgAccountID(new Long(req.getStAccountEntityID()));
//                    j4.setDbCredit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                    j4.setDbDebit(BDUtil.zero);
//                    j4.setDbEnteredCredit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                    j4.setDbEnteredDebit(BDUtil.zero);
//                    j4.setStDescription(reqObj.getStDescription());
//                    j4.setStCurrencyCode(req.getStCurrency());
//                    j4.setDbCurrencyRate(req.getDbCurrencyRate());
////            j4.setStRefTrxNo(req.getStRegionID());
//                    j4.setStYears(req.getStYears());
//                    j4.setStMonths(req.getStMonths());
//                    j4.setStIDRFlag("Y");
//                }
//
//                if (reqObj.getDtTglCashback() == null) {
//                    throw new Exception("Apply Date is not defined !");
//                }
//                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
//                if (per2 == null) {
//                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + reqObj.getDtTglCashback() + ")");
//                }
//                if (!per2.isOpen()) {
//                    throw new Exception("Period is not open");
//                }
//                j4.setLgPeriodNo(per2.getLgPeriodNo());
//                j4.setLgFiscalYear(per2.getLgFiscalYear());
////            j4.setStRefTrxType("INV_REQUESTCASH");
//                j4.setStRefTrxType("CASHBANK");
//                j4.setDtApplyDate(reqObj.getDtTglCashback());
//
//                if (j4.isModified()) {
//
//                    final JournalView old = j4.getOldJournal();
//
//                    if (old != null) {
//
//                        final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
//                                && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;
//
//                        if (isSameRecord) {
//                            final BigDecimal am = j4.getDbAdjustmentAmount();
//
//                            if (am.compareTo(BDUtil.zero) != 0) {
//                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
//                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
//                            }
//                        } else {
//                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
//                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
//                        }
//                    } else {
//                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
//                    }
//                }
//
//                S.store(j4);
//                //akhir simpen lawan
            }

            JournalView j5 = new JournalView();
            j5.markNew();

            JournalView j6 = new JournalView();
            j6.markNew();

            j5.setStTransactionHeaderID(stTransactionHeaderID);
            j5.setLgHeaderAccountID(new Long(req.getStAccountID()));
            j5.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//            j5.setLgAccountID(new Long(req.getStAccountID()));
            j5.setLgAccountID(new Long(req.findAccountUangMuka(req.getStCostCenterCode())));
            j5.setDbCredit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
            j5.setDbDebit(BDUtil.zero);
            j5.setDbEnteredCredit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
            j5.setDbEnteredDebit(BDUtil.zero);
            j5.setStDescription("Pengembalian Biaya " + req.getStDescription());
            j5.setStCurrencyCode(req.getStCurrency());
            j5.setDbCurrencyRate(req.getDbCurrencyRate());
//            j5.setStRefTrxNo(req.getStRegionID());
            j5.setStYears(req.getStYears());
            j5.setStMonths(req.getStMonths());
            j5.setStIDRFlag("Y");

            j5.reCalculate();

            if (j5.isNew()) {
                j5.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j5.setStTransactionNo(req.getStTransactionNo());
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }

            //final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(new Date());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }

            j5.setLgPeriodNo(per.getLgPeriodNo());
            j5.setLgFiscalYear(per.getLgFiscalYear());
//            j5.setStRefTrxType("INV_REQUESTCASH");
            j5.setStRefTrxType("CASHBANK");
            j5.setDtApplyDate(new Date());

            if (j5.isModified()) {

                final JournalView old = j5.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j5.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j5.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j5.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j5.getLgAccountID(), j5.getLgFiscalYear(), j5.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j5.getLgAccountID(), j5.getLgFiscalYear(), j5.getLgPeriodNo(), j5.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j5.getLgAccountID(), j5.getLgFiscalYear(), j5.getLgPeriodNo(), j5.getDbBalanceAmount());
                }
            }

            S.store(j5);

            //simpan bank
            if (j6.isNew()) {
                j6.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j6.setStTransactionHeaderID(stTransactionHeaderID);
                j6.setStTransactionNo(req.getStTransactionNo());
                j6.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j6.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
                j6.setLgAccountID(new Long(req.getStAccountID()));
                j6.setDbCredit(BDUtil.zero);
                j6.setDbDebit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
                j6.setDbEnteredCredit(BDUtil.zero);
                j6.setDbEnteredDebit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
                j6.setStDescription("Pengembalian Biaya " + req.getStDescription());
                j6.setStCurrencyCode(req.getStCurrency());
                j6.setDbCurrencyRate(req.getDbCurrencyRate());
//            j6.setStRefTrxNo(req.getStRegionID());
                j6.setStYears(req.getStYears());
                j6.setStMonths(req.getStMonths());
                j6.setStIDRFlag("Y");
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + new Date() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j6.setLgPeriodNo(per.getLgPeriodNo());
            j6.setLgFiscalYear(per.getLgFiscalYear());
//            j6.setStRefTrxType("INV_REQUESTCASH");
            j6.setStRefTrxType("CASHBANK");
            j6.setDtApplyDate(new Date());

            if (j6.isModified()) {

                final JournalView old = j6.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j6.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j6.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j6.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j6.getLgAccountID(), j6.getLgFiscalYear(), j6.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j6.getLgAccountID(), j6.getLgFiscalYear(), j6.getLgPeriodNo(), j6.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j6.getLgAccountID(), j6.getLgFiscalYear(), j6.getLgPeriodNo(), j6.getDbBalanceAmount());
                }
            }

            S.store(j6);
            //akhir simpen lawan

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void reverseRequest(ARRequestFee req) throws Exception {
        //CheckReverse(pencairan.getStBuktiC());

        if (req.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + req.getStMonths() + " dan tahun " + req.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_request_fee set eff_flag = 'N', cashier_flag = 'N', print_flag = 'N', approved_who = null, approved_date = null, cashier_who = null, cashier_date = null where req_id = ?");

            P.setObject(1, req.getStARRequestID());
            int r = P.executeUpdate();
            S.release();

            if (!req.isStatusRequest()) {
                PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

                P2.setObject(1, req.getStTransactionNo());
                int r2 = P2.executeUpdate();
                S.release();
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }
    
    public void updatePolicyRequest(String req, String pol) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query = "update ins_policy set paid_request = ? where pol_id = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setString(1, req);
            PS.setString(2, pol);

            int i = PS.executeUpdate();
        } finally {
            S.release();
        }
    }

    public DTOList getInsuranceProposalComm(String stSHK) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_proposal_komisi where no_surat_hutang = ?",
                new Object[]{stSHK},
                uploadProposalCommView.class);
    }

    public void saveClosingReport(InsuranceClosingReportView closing) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (closing.isNew()) {
                closing.setStClosingID(String.valueOf(IDFactory.createNumericID("INSCLOSINGRPT")));
            }

            S.store(closing);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void saveMKT(AccountMarketingView account) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (account.isNew()) {
                account.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLACCMKT")));
            }

            S.store(account);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

     public void saveClosingP(InsurancePostingView posting, boolean reOpenMode) throws Exception {
        SQLUtil S = new SQLUtil();

        try {

            if (posting.isNew()) {
                posting.setStGLPostingID(String.valueOf(IDFactory.createNumericID("INSCLOSINGPRODUKSIID")));
            }

            S.store(posting);

//            updateClosingP(posting, posting.getStMonths(), posting.getStYears(), reOpenMode);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void AccountNeracaItemJenas(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag, String poltype) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query;

            if (poltype != null) {
                query = "update gl_acct_bal_neracatotal_jenas set cr" + periodTo + " = 0,db" + periodTo + " = " + neracaItem + ", bal" + periodTo + " = " + neracaItem + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? and pol_type_id = ? ";
            } else {
                query = "update gl_acct_bal_neracatotal_jenas set cr" + periodTo + " = 0,db" + periodTo + " = " + neracaItem + ", bal" + periodTo + " = " + neracaItem + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? ";
            }

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);

            if (poltype != null) {
                PS.setObject(3, poltype);
            }

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal_neracatotal_jenas (account_id,period_year,pol_type_id,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setObject(3, poltype);
                PS.setBigDecimal(4, neracaItem);
                PS.setBigDecimal(5, neracaItem);
                PS.setObject(6, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    public void approveRefund(ARRequestFee req) throws Exception {

        if (req.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + req.getStMonths() + " dan tahun " + req.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            JournalView j = new JournalView();
            j.markNew();

            JournalView j2 = new JournalView();
            j2.markNew();

            j.setStTransactionHeaderID(stTransactionHeaderID);
            j.setLgHeaderAccountID(new Long(req.getStAccountID()));
            j.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//            j.setLgAccountID(new Long(req.getStAccountID()));
            j.setLgAccountID(new Long(req.findAccountUangMuka(req.getStCostCenterCode())));
            j.setDbCredit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
            j.setDbDebit(BDUtil.zero);
            j.setDbEnteredCredit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
            j.setDbEnteredDebit(BDUtil.zero);
            j.setStDescription("Pengembalian Biaya " + req.getStDescription());
            j.setStCurrencyCode(req.getStCurrency());
            j.setDbCurrencyRate(req.getDbCurrencyRate());
//            j.setStRefTrxNo(req.getStRegionID());
            j.setStYears(req.getStYears());
            j.setStMonths(req.getStMonths());
            j.setStIDRFlag("Y");

            j.reCalculate();

            if (j.isNew()) {
                j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j.setStTransactionNo(req.getStTransactionNo());
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }

            final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(new Date());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }

            j.setLgPeriodNo(per.getLgPeriodNo());
            j.setLgFiscalYear(per.getLgFiscalYear());
//            j.setStRefTrxType("INV_REQUESTCASH");
            j.setStRefTrxType("CASHBANK");
            j.setDtApplyDate(new Date());

            if (j.isModified()) {

                final JournalView old = j.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
            }

            S.store(j);

            //simpan bank
            if (j2.isNew()) {
                j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j2.setStTransactionHeaderID(stTransactionHeaderID);
                j2.setStTransactionNo(req.getStTransactionNo());
                j2.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j2.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
                j2.setLgAccountID(new Long(req.getStAccountID()));
                j2.setDbCredit(BDUtil.zero);
                j2.setDbDebit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
                j2.setDbEnteredCredit(BDUtil.zero);
                j2.setDbEnteredDebit(BDUtil.mul(req.getDbNominalBack(), req.getDbCurrencyRate()));
                j2.setStDescription("Pengembalian Biaya " + req.getStDescription());
                j2.setStCurrencyCode(req.getStCurrency());
                j2.setDbCurrencyRate(req.getDbCurrencyRate());
//            j2.setStRefTrxNo(req.getStRegionID());
                j2.setStYears(req.getStYears());
                j2.setStMonths(req.getStMonths());
                j2.setStIDRFlag("Y");
            }

            if (req.getDtTglRequest() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + new Date() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j2.setLgPeriodNo(per.getLgPeriodNo());
            j2.setLgFiscalYear(per.getLgFiscalYear());
//            j2.setStRefTrxType("INV_REQUESTCASH");
            j2.setStRefTrxType("CASHBANK");
            j2.setDtApplyDate(new Date());

            if (j2.isModified()) {

                final JournalView old = j2.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j2.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                }
            }

            S.store(j2);
            //akhir simpen lawan

//            for (int i = 0; i < l.size(); i++) {
//                ARRequestFeeObj reqObj = (ARRequestFeeObj) l.get(i);
//
//                JournalView j3 = new JournalView();
//                j3.markNew();
//
//                JournalView j4 = new JournalView();
//                j4.markNew();
//
//                j3.setStTransactionHeaderID(stTransactionHeaderID);
//                j3.setLgHeaderAccountID(new Long(req.getStAccountEntityID()));
//                j3.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
////            j3.setLgAccountID(new Long(req.getStAccountID()));
//                j3.setLgAccountID(new Long(reqObj.getStAccountID()));
//                j3.setDbDebit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                j3.setDbCredit(BDUtil.zero);
//                j3.setDbEnteredDebit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                j3.setDbEnteredCredit(BDUtil.zero);
//                j3.setStDescription(reqObj.getStDescription());
//                j3.setStCurrencyCode(req.getStCurrency());
//                j3.setDbCurrencyRate(req.getDbCurrencyRate());
////            j3.setStRefTrxNo(req.getStRegionID());
//                j3.setStYears(req.getStYears());
//                j3.setStMonths(req.getStMonths());
//                j3.setStIDRFlag("Y");
//
//                j3.reCalculate();
//
//                if (j3.isNew()) {
//                    j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
//                    j3.setStTransactionNo(req.getStTransactionNo());
//                }
//
//                if (reqObj.getDtTglCashback() == null) {
//                    throw new Exception("Apply Date is not defined !");
//                }
//                final PeriodDetailView per2 = PeriodManager.getInstance().getPeriodFromDate(reqObj.getDtTglCashback());
//                if (per2 == null) {
//                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + reqObj.getDtTglCashback() + ")");
//                }
//                if (!per2.isOpen()) {
//                    throw new Exception("Period is not open");
//                }
//                j3.setLgPeriodNo(per2.getLgPeriodNo());
//                j3.setLgFiscalYear(per2.getLgFiscalYear());
////            j3.setStRefTrxType("INV_REQUESTCASH");
//                j3.setStRefTrxType("CASHBANK");
//                j3.setDtApplyDate(reqObj.getDtTglCashback());
//
//                if (j3.isModified()) {
//
//                    final JournalView old = j3.getOldJournal();
//
//                    if (old != null) {
//
//                        final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
//                                && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;
//
//                        if (isSameRecord) {
//                            final BigDecimal am = j3.getDbAdjustmentAmount();
//
//                            if (am.compareTo(BDUtil.zero) != 0) {
//                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
//                                //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
//                            }
//                        } else {
//                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
//                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
//                        }
//                    } else {
//                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
//                    }
//                }
//
//                S.store(j3);
//
//                //simpan bank
//                if (j4.isNew()) {
//                    j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
//                    j4.setStTransactionHeaderID(stTransactionHeaderID);
//                    j4.setStTransactionNo(req.getStTransactionNo());
//                    j4.setLgHeaderAccountID(new Long(req.getStAccountEntityID()));
//                    j4.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
//                    j4.setLgAccountID(new Long(req.getStAccountEntityID()));
//                    j4.setDbCredit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                    j4.setDbDebit(BDUtil.zero);
//                    j4.setDbEnteredCredit(BDUtil.mul(reqObj.getDbNominal(), req.getDbCurrencyRate()));
//                    j4.setDbEnteredDebit(BDUtil.zero);
//                    j4.setStDescription(reqObj.getStDescription());
//                    j4.setStCurrencyCode(req.getStCurrency());
//                    j4.setDbCurrencyRate(req.getDbCurrencyRate());
////            j4.setStRefTrxNo(req.getStRegionID());
//                    j4.setStYears(req.getStYears());
//                    j4.setStMonths(req.getStMonths());
//                    j4.setStIDRFlag("Y");
//                }
//
//                if (reqObj.getDtTglCashback() == null) {
//                    throw new Exception("Apply Date is not defined !");
//                }
//                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
//                if (per2 == null) {
//                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + reqObj.getDtTglCashback() + ")");
//                }
//                if (!per2.isOpen()) {
//                    throw new Exception("Period is not open");
//                }
//                j4.setLgPeriodNo(per2.getLgPeriodNo());
//                j4.setLgFiscalYear(per2.getLgFiscalYear());
////            j4.setStRefTrxType("INV_REQUESTCASH");
//                j4.setStRefTrxType("CASHBANK");
//                j4.setDtApplyDate(reqObj.getDtTglCashback());
//
//                if (j4.isModified()) {
//
//                    final JournalView old = j4.getOldJournal();
//
//                    if (old != null) {
//
//                        final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
//                                && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;
//
//                        if (isSameRecord) {
//                            final BigDecimal am = j4.getDbAdjustmentAmount();
//
//                            if (am.compareTo(BDUtil.zero) != 0) {
//                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
//                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
//                            }
//                        } else {
//                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
//                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
//                        }
//                    } else {
//                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
//                    }
//                }
//
//                S.store(j4);
//                //akhir simpen lawan
//            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    private void postReinsOutwardXOLCumullation(DTOList reinsAll, InsuranceClosingView closing)throws Exception{
    UserSession us = SessionManager.getInstance().getSession();

    DTOList reins = reinsAll;

    SQLUtil S = new SQLUtil();

    for (int i = 0; i < reins.size(); i++){
      InsurancePolicyInwardView ri = (InsurancePolicyInwardView)reins.get(i);

      getRemoteAccountReceivable().saveSaldoAwalInwardClosing(ri, closing);

      PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");
      PS.setObject(1, closing.getStClosingID());
      int j = PS.executeUpdate();

      if (j != 0) {
        logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
      }

      S.release();
    }

  }

  private void postReinsInwardFacCumullation(DTOList reinsAll, InsuranceClosingView closing)
    throws Exception
  {
    UserSession us = SessionManager.getInstance().getSession();

    DTOList reins = reinsAll;

    SQLUtil S = new SQLUtil();
    for (int i = 0; i < reins.size(); i++)
    {
      InsurancePolicyInwardView ri = (InsurancePolicyInwardView)reins.get(i);

      getRemoteAccountReceivable().saveSaldoAwalInwardClosing(ri, closing);

      PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");
      PS.setObject(1, closing.getStClosingID());
      int j = PS.executeUpdate();
      if (j != 0) {
        logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
      }
      S.release();
    }
  }


   

    public String findAccount(String akun, String koda) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select account_id from gl_accounts where cc_code = ? and accountno like ? ");

            PS.setString(1, koda);
            PS.setString(2, akun + "%");

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;

        } finally {
            S.release();
        }
    }

    
    public void postSOA(DTOList reinsAll, InsurancePolicySOAView soa) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList reins = reinsAll;

        //for (int i = 0; i < reins.size(); i++) {
            //InsurancePolicyReinsView ri = (InsurancePolicyReinsView) reins.get(i);

            //if (ri.getTreatyDetail().isOR()) {
                //continue;
            //}

//            if (BDUtil.isZeroOrNull(ri.getDbPremiAmount())) {
//                continue;
//            }



            //final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, ri.getStPolicyID());

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("REINS");
            invoice.setStInvoiceNo(soa.getStSOANo());

//            if (closing.isClosingRIOutward() || closing.isClosingRIInwardToOutward()) {
//                invoice.setStRefID0("REINS");
//                invoice.setStInvoiceNo("I" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
//                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
//                // I 01 040120200913001000
//            } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")) {
//                invoice.setStRefID0("CLAIMREINS");
//                invoice.setStInvoiceNo("N" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
//                //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
//                // I 01 040120200913001000
//            }

            Date invoiceDate = new Date();

//            if (closing.getDtInvoiceDate() != null) {
//                invoiceDate = closing.getDtInvoiceDate();
//            }

            invoice.setDtInvoiceDate(invoiceDate);
            invoice.setDtDueDate(invoiceDate);

            //if(ri.isInstallment()) invoice.setDtDueDate(ri.getDtInstallmentDate());

            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode("IDR");
            invoice.setDbCurrencyRate(BDUtil.one);
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(soa.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode("00");

            invoice.setStARTransactionTypeID("13");
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID("59");
            //invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            //invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            //invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(soa.getStEntityName());
            //invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            //invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            //invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(soa.getStSOAID());
            invoice.setStPolicyID(soa.getStSOAID());


            invoice.setStReferenceD0(soa.getStReferenceD0());
            invoice.setStReferenceD1(soa.getStReferenceD1());

            invoice.setStReferenceE0(soa.getStReferenceE0());
            invoice.setStReferenceE1(soa.getStReferenceE1());

            invoice.setStReferenceZ0(soa.getStReferenceZ0());
            invoice.setStReferenceZ1(soa.getStReferenceZ1());

            invoice.setStReferenceC0(soa.getStReferenceC0());
            invoice.setStReferenceC1(soa.getStReferenceC1());


            //invoice.setDtReferenceDate1(ri.getDtBindingDate());

            //invoice.setStReferenceA1(ri.getStRISlipNo());

            //bikin surat hutang
            //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
            //"218/BPDAN/2/2011"
//            if (ri.getDtValidReinsuranceDate() == null) {
//                invoice.setStNoSuratHutang(
//                        ri.getStMemberEntityID()
//                        + "/"
//                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
//                        + "/"
//                        + DateUtil.getQuartalRomawi(invoiceDate)
//                        + "/"
//                        + DateUtil.getYear(invoiceDate));
//            }
//
//            if (ri.getDtValidReinsuranceDate() != null) {
//                invoice.setStNoSuratHutang(
//                        ri.getStMemberEntityID() + "."
//                        + String.valueOf(i + 1)
//                        + "/"
//                        + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()
//                        + "/"
//                        + DateUtil.getQuartalRomawi(invoiceDate)
//                        + "/"
//                        + DateUtil.getYear(invoiceDate));
//            }

            if (soa.getStSOANo() != null) {
                invoice.setStNoSuratHutang(soa.getStSOANo());
            }


            invoice.setDtSuratHutangPeriodFrom(invoiceDate);
            invoice.setDtSuratHutangPeriodTo(invoiceDate);
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id in (?,?)",
                    new Object[]{invoice.getStARTransactionTypeID(),"14"},
                    ARTransactionLineView.class);

            {

                for (int v = 0; v < artlines.size(); v++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);

                    if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(soa.getDbPremiReinsurance());

                    } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {

//                        if (BDUtil.isZeroOrNull(ri.getDbRICommAmount())) {
//                            //continue;
//                        }

                        if (invoice.getStReferenceZ0() == null) {
                            throw new RuntimeException("Comission Account Code not found for komisi");
                        }

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(soa.getDbComissionReinsurance());

                    }else if ("KLAIM".equalsIgnoreCase(artl.getStItemClass())) {

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(soa.getDbClaimReinsurance());

//                        if(ri.getDbRICommAmountEdited()!=null){
//                            ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbRICommAmount(),ri.getDbRICommAmountEdited()));
//                        }
                    }

                    //else throw new RuntimeException("Unknown Item class : " + artl);
                }
            }

            invoice.recalculate();

            getRemoteAccountReceivable().saveInvoiceReins(invoice);

            SQLUtil S = new SQLUtil();

            PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_soa set status = 'Y' where soa_id = ?;COMMIT;");

            PS.setObject(1, soa.getStSOAID());

            int j = PS.executeUpdate();

            if (j != 0) {
                logger.logInfo("+++++++ UPDATE counter proses : " + soa.getStSOAID() + " ++++++++++++++++++");
            }

            S.release();
        //}


    }

    public ARInvestmentIzinDepositoView loadIzinDeposito(String ardepoid) throws Exception {
        final ARInvestmentIzinDepositoView depo = (ARInvestmentIzinDepositoView) ListUtil.getDTOListFromQuery(
                "select * from ar_izin_deposito where ar_izin_id = ? ",
                new Object[]{ardepoid},
                ARInvestmentIzinDepositoView.class).getDTO();

        return depo;
    }

    public String saveIzin(ARInvestmentIzinDepositoView izindeposito, boolean approvalMode) throws Exception {

//        boolean checkPeriod = Tools.compare(izindeposito.getDtMutationDate(), new Date()) >= 0;
//        if (checkPeriod) {
//            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
//        }

        //boolean checkPeriod2 = Tools.compare(deposito.getDtTglakhir(), new Date()) <= 0;
        //if (checkPeriod2) throw new RuntimeException("Tanggal Akhir Sudah Lewat");

        final SQLUtil S = new SQLUtil();

        String ccCode = izindeposito.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(izindeposito.getDtMutationDate());

        try {
            if (izindeposito.isNew()) {
                izindeposito.setStARIzinID(String.valueOf(IDFactory.createNumericID("INVIZINDEPO")));
                izindeposito.setStActiveFlag("Y");
                izindeposito.setStPrintFlag("N");
                izindeposito.setStApprovedCabFlag("N");
                izindeposito.setStApprovedPusFlag("N");

                String noSurat = String.valueOf(IDFactory.createNumericID("INVIZINSURAT" + counterKey + ccCode, 1));
                izindeposito.setStNoSurat(noSurat + "/ASKRIDA-" + izindeposito.getCostCenter3().getStShortname().toUpperCase() + "/BENTUK/" + DateUtil.getMonth2Digit(izindeposito.getDtMutationDate()) + "/" + DateUtil.getYear(izindeposito.getDtMutationDate()));

            }

            S.store(izindeposito);

//            final DTOList deposito = izindeposito.getDeposito();
            final DTOList deposito = izindeposito.getDepodetail();

            for (int i = 0; i < deposito.size(); i++) {
//                ARInvestmentDepositoIndexView dep = (ARInvestmentDepositoIndexView) deposito.get(i);
                ARInvestmentIzinDepositoDetailView dep = (ARInvestmentIzinDepositoDetailView) deposito.get(i);

                if (dep.isNew()) {
                    dep.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVIZINDEPODET")));
                    dep.setStActiveFlag("Y");
                    dep.setStCurrency("IDR");
                    dep.setDbCurrencyRate(BDUtil.one);
                }

                dep.setDbNominal(dep.getDbNominalKurs());
                dep.setStCostCenterCode(izindeposito.getStCostCenterCode());
                dep.setStReceiptClassID(izindeposito.getStReceiptClassID());
                dep.setStCompanyType(izindeposito.getStCompanyType());
                dep.setStNodefo(dep.getStNodefoSementara());

                if (dep.getStKodedepo().equalsIgnoreCase("1")) {
                    dep.setStHari(dep.getStHari());
                    dep.setStBulan("0");
                } else if (dep.getStKodedepo().equalsIgnoreCase("2")) {
                    dep.setStHari("0");
                    dep.setStBulan(dep.getStBulan());
                }

                dep.setStARIzinID(izindeposito.getStARIzinID());
            }

            if (approvalMode) {
                for (int j = 0; j < deposito.size(); j++) {
                    ARInvestmentIzinDepositoDetailView dep = (ARInvestmentIzinDepositoDetailView) deposito.get(j);

                    ARInvestmentDepositoIndexView depo = new ARInvestmentDepositoIndexView();
                    depo.markNew();

                    depo.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
                    depo.setStActiveFlag("Y");
                    depo.setStEffectiveFlag("N");
                    depo.setStKonter("0");
                    depo.setStCurrency("IDR");
                    depo.setDbCurrencyRate(BDUtil.one);
                    depo.setStStatus("DEPOSITO");
                    depo.setStJournalStatus("NEW");
                    depo.setDbPajak(new BigDecimal(20));
                    depo.setStMonths(izindeposito.getStMonths());
                    depo.setStYears(izindeposito.getStYears());
                    depo.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                    depo.setDtCreateDate(new Date());

                    depo.setStCostCenterCode(izindeposito.getStCostCenterCode());
                    depo.setStReceiptClassID(izindeposito.getStReceiptClassID());
                    depo.setStCompanyType(izindeposito.getStCompanyType());
                    depo.setDtTgldepo(dep.getDtTglawal());
                    depo.setDtTglmuta(dep.getDtTglawal());
                    depo.setStNodefo(dep.getStNodefoSementara());
                    depo.setStHari(dep.getStHari());
                    depo.setStBulan(dep.getStBulan());

                    depo.setDbNominalKurs(dep.getDbNominalKurs());
                    depo.setDbNominal(dep.getDbNominalKurs());

                    depo.setStKodedepo(dep.getStKodedepo());
                    depo.setDtTglawal(dep.getDtTglawal());
                    depo.setDtTglakhir(dep.getDtTglakhir());
                    depo.setDbBunga(dep.getDbBunga());
                    depo.setStNoRekeningDeposito(dep.getStNoRekeningDeposito());
                    depo.setStDepoName(dep.getStDepoName());
                    depo.setStAccountDepo(dep.getStAccountDepo());
                    depo.setStUnit(dep.getStUnit());
                    depo.setStNodefoSementara(dep.getStNodefoSementara());

                    if (depo.getStKodedepo().equalsIgnoreCase("2")) {
                        BigDecimal nominal = BDUtil.mul(depo.getDbNominal(), BDUtil.getRateFromPct(new BigDecimal(20)));
                        depo.setDbNominalDana(nominal);
                    }

                    if (dep.isDebet()) {
                        depo.setStEntityID(dep.getStEntityID());
                        depo.setStBankName(dep.getStBankName());
                        depo.setStAccountBank(dep.getStAccountBank());
                    } else {
                        if (dep.isAskrida()) {
                            depo.setStEntityID(dep.getStEntityID());
                            depo.setStBankName(dep.getStBankName());
                            depo.setStAccountBank(dep.getStAccountBank());
                        } else {
                            depo.setStEntityID(dep.getStSumberBank());
                            depo.setStBankName(dep.getStNamaSumberBank());
                            depo.setStAccountBank(dep.getStAccountSumberBank());
                        }
                    }

                    depo.setStARIzinID(izindeposito.getStARIzinID());
                    depo.setStARIzinDetID(dep.getStARDepoID());

                    S.store(depo);

                    ARInvestmentPencairanView pcr = new ARInvestmentPencairanView();
                    pcr.markNew();

                    if (pcr.isNew()) {
                        pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                        pcr.setStCreateWho(depo.getStCreateWho());
                        pcr.setDtCreateDate(depo.getDtCreateDate());
                        pcr.setStActiveFlag("Y");
                        pcr.setStEffectiveFlag("N");
                    }

                    pcr.setStARDepoID(depo.getStARDepoID());
                    pcr.setStNodefo(depo.getStNodefo());
                    pcr.setStBuktiB(depo.getStBuktiB());
                    pcr.setStHari(depo.getStHari());
                    pcr.setStBulan(depo.getStBulan());
                    pcr.setStCostCenterCode(depo.getStCostCenterCode());
                    pcr.setStReceiptClassID(depo.getStReceiptClassID());
                    pcr.setStCompanyType(depo.getStCompanyType());
                    pcr.setStCurrency(depo.getStCurrency());
                    pcr.setDbCurrencyRate(depo.getDbCurrencyRate());
                    pcr.setDbPajak(depo.getDbPajak());
                    pcr.setDbBunga(depo.getDbBunga());
                    pcr.setDbNominal(depo.getDbNominal());
                    pcr.setDbNominalKurs(depo.getDbNominalKurs());
                    pcr.setDtTglawal(depo.getDtTglawal());
                    pcr.setDtTglakhir(depo.getDtTglakhir());
                    pcr.setDtTgldepo(depo.getDtTgldepo());
                    pcr.setDtTglmuta(depo.getDtTglmuta());
                    pcr.setStRegisterBentuk(depo.getStRegister());
                    pcr.setStKonter(depo.getStKonter());
                    pcr.setStKodedepo(depo.getStKodedepo());
                    pcr.setStNoRekeningDeposito(depo.getStNoRekeningDeposito());
                    pcr.setStDepoName(depo.getStDepoName());
                    pcr.setStAccountDepo(depo.getStAccountDepo());
                    pcr.setStNoRekening(depo.getStNoRekening());
                    pcr.setStMonths(null);
                    pcr.setStYears(null);
                    pcr.setStActiveCairFlag("Y");

                    S.store(pcr);
                }
            }

            S.store(deposito);

            return izindeposito.getStARIzinID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public String saveIzinBentuk(ARInvestmentDepositoView deposito) throws Exception {

        if (deposito.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
        }

        boolean checkPeriod = Tools.compare(deposito.getDtTglawal(), new Date()) >= 0;
        if (checkPeriod) {
            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
        }

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
            }

//            deposito.generateNoBuktiDeposito();
//            deposito.generateRegisterBentuk();

            if (deposito.getStBuktiB() == null) {
                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
            }

            deposito.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            deposito.setDtChangeDate(new Date());

            S.store(deposito);

            PreparedStatement PS = S.setQuery("update ar_inv_pencairan set bukti_b = ?, regbentuk = ? where ar_depo_id = ? ");

            PS.setObject(1, deposito.getStBuktiB());
            PS.setObject(2, deposito.getStRegister());
            PS.setObject(3, deposito.getStARDepoID());

            int p = PS.executeUpdate();

            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public ARInvestmentIzinPencairanView loadIzinPencairan(String ardepoid) throws Exception {
        final ARInvestmentIzinPencairanView depo = (ARInvestmentIzinPencairanView) ListUtil.getDTOListFromQuery(
                "select * from ar_izin_pencairan where ar_izincair_id = ? ",
                new Object[]{ardepoid},
                ARInvestmentIzinPencairanView.class).getDTO();

        return depo;
    }

    public String saveIzinCairOLD(ARInvestmentIzinPencairanView izinpencairan, boolean approvalMode) throws Exception {

        boolean checkPeriod = Tools.compare(izinpencairan.getDtMutationDate(), new Date()) >= 0;
        if (checkPeriod) {
            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
        }

        final SQLUtil S = new SQLUtil();

        String ccCode = izinpencairan.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(izinpencairan.getDtMutationDate());

        try {
            if (izinpencairan.isNew()) {
                izinpencairan.setStARIzinCairID(String.valueOf(IDFactory.createNumericID("INVIZINCAIR")));
                izinpencairan.setStActiveFlag("Y");
                izinpencairan.setStPrintFlag("N");
                izinpencairan.setStApprovedCabFlag("N");
                izinpencairan.setStApprovedPusFlag("N");

                String noSurat = String.valueOf(IDFactory.createNumericID("INVIZINCAIRSURAT" + counterKey + ccCode, 1));
                izinpencairan.setStNoSurat(noSurat + "/ASKRIDA-" + izinpencairan.getCostCenter3().getStShortname().toUpperCase() + "/CAIR/" + DateUtil.getMonth2Digit(izinpencairan.getDtMutationDate()) + "/" + DateUtil.getYear(izinpencairan.getDtMutationDate()));
            }

            S.store(izinpencairan);

            final DTOList pencairandet = izinpencairan.getPencairandet();

            for (int i = 0; i < pencairandet.size(); i++) {
                ARInvestmentIzinPencairanDetView dep = (ARInvestmentIzinPencairanDetView) pencairandet.get(i);

                if (dep.isNew()) {
                    dep.setStARIzinCairDetID(String.valueOf(IDFactory.createNumericID("INVIZINCAIRDET")));
                    dep.setStActiveFlag("Y");
                }

//                dep.markUpdate();
                dep.setStARIzinCairID(izinpencairan.getStARIzinCairID());

                dep.setStARDepoID(dep.getStARDepoID());
                dep.setStNodefo(dep.getStNodefo());
                dep.setStBuktiB(dep.getStBuktiB());
                dep.setStPencairanKet(dep.getStPencairanKet());
                dep.setStARInvoiceID(dep.getStARInvoiceID());
                dep.setStDLANo(dep.getStDLANo());
                dep.setStJenisCair(dep.getStJenisCair());
                dep.setDbBilyetAmount(dep.getDbBilyetAmount());
                dep.setDbNilai(dep.getDbNilai());

                if (approvalMode) {
                    final DTOList deposito = dep.getDeposito();
                    for (int j = 0; j < deposito.size(); j++) {
                        ARInvestmentDepositoView depo = (ARInvestmentDepositoView) deposito.get(j);

                        ARInvestmentPencairanView cair = new ARInvestmentPencairanView();
                        cair.markNew();

                        if (cair.isNew()) {
                            cair.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                            cair.setStActiveFlag("Y");
                            cair.setStEffectiveFlag("N");
                            cair.setStActiveCairFlag("Y");
                            cair.setStKonter("0");
                            cair.setStCurrency("IDR");
                            cair.setDbCurrencyRate(BDUtil.one);
                            cair.setStJournalStatus("NEW");
                            cair.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                            cair.setDtCreateDate(new Date());
                        }

                        cair.setStARDepoID(depo.getStARDepoID());
                        cair.setStNodefo(depo.getStNodefo());
                        cair.setStBuktiB(depo.getStBuktiB());
                        cair.setStNoRekeningDeposito(depo.getStNoRekeningDeposito());
                        cair.setStCostCenterCode(depo.getStCostCenterCode());
                        cair.setStReceiptClassID(depo.getStReceiptClassID());
                        cair.setStCompanyType(depo.getStCompanyType());
                        cair.setStRegisterBentuk(depo.getStRegister());
                        cair.setStDepoName(depo.getStDepoName());
                        cair.setStAccountDepo(depo.getStAccountDepo());
                        cair.setStNoRekening(depo.getStNoRekening());
                        cair.setStMonths(izinpencairan.getStMonths());
                        cair.setStYears(izinpencairan.getStYears());
                        cair.setStKodedepo(depo.getStKodedepo());
                        cair.setStHari(depo.getStHari());
                        cair.setStBulan(depo.getStBulan());

                        cair.setDbNominal(depo.getDbNominal());
                        cair.setDbNominalKurs(depo.getDbNominalKurs());
                        cair.setDbPajak(depo.getDbPajak());
                        cair.setDbBunga(depo.getDbBunga());

                        cair.setDtTgldepo(depo.getDtTgldepo());
                        cair.setDtTglmuta(depo.getDtTglmuta());
                        cair.setDtTglawal(depo.getDtTglawal());
                        cair.setDtTglakhir(depo.getDtTglakhir());

                        cair.setStEntityID(cair.findAccountBankUtama(cair.getStCostCenterCode()));
                        cair.setStAccountBank(cair.getAccounts().getStAccountNo());
                        cair.setStBankName(cair.getAccounts().getStDescription());

                        cair.setStARIzinCairDetID(dep.getStARIzinCairDetID());
                        cair.setStARIzinCairID(dep.getStARIzinCairID());
                        S.store(cair);
                    }
                }
            }

            S.store(pencairandet);

            return izinpencairan.getStARIzinCairID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public ARInvestmentIzinBungaView loadIzinBunga(String ardepoid) throws Exception {
        final ARInvestmentIzinBungaView depo = (ARInvestmentIzinBungaView) ListUtil.getDTOListFromQuery(
                "select * from ar_izin_bunga where ar_izinbng_id = ? ",
                new Object[]{ardepoid},
                ARInvestmentIzinBungaView.class).getDTO();

        return depo;
    }


    public String saveIzinBunga(ARInvestmentIzinBungaView izinbunga, boolean approvalMode) throws Exception {

        boolean checkPeriod = Tools.compare(izinbunga.getDtMutationDate(), new Date()) >= 0;
        if (checkPeriod) {
            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
        }

        final SQLUtil S = new SQLUtil();

        String ccCode = izinbunga.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(izinbunga.getDtMutationDate());

        try {
            if (izinbunga.isNew()) {
                izinbunga.setStARIzinBngID(String.valueOf(IDFactory.createNumericID("INVIZINBUNGA")));
                izinbunga.setStActiveFlag("Y");
                izinbunga.setStPrintFlag("N");
                izinbunga.setStApprovedCabFlag("N");
                izinbunga.setStApprovedPusFlag("N");

                String noSurat = String.valueOf(IDFactory.createNumericID("INVIZINBUNGASURAT" + counterKey + ccCode, 1));
                izinbunga.setStNoSurat(noSurat + "/ASKRIDA-" + izinbunga.getCostCenter3().getStShortname().toUpperCase() + "/BUNGA/" + DateUtil.getMonth2Digit(izinbunga.getDtMutationDate()) + "/" + DateUtil.getYear(izinbunga.getDtMutationDate()));
            }

            S.store(izinbunga);

//            if (approvalMode) {
            int bulanCodeNext = Integer.parseInt(izinbunga.getStMonths());

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNext), izinbunga.getStYears());
            Date dateStart = pd.getDtStartDate();

            String bulan = DateUtil.getMonth2Digit(dateStart);
            String years = DateUtil.getYear(dateStart);

            final DTOList bungadet = izinbunga.getBungadet();
            for (int i = 0; i < bungadet.size(); i++) {
                ARInvestmentBungaView dep = (ARInvestmentBungaView) bungadet.get(i);

                final ARInvestmentPerpanjanganView depo = (ARInvestmentPerpanjanganView) DTOPool.getInstance().getDTO(ARInvestmentPerpanjanganView.class, dep.getStARDepoID());

                Date policyDateStart = depo.getDtTglawal();
                Date policyDateEnd = advanceMonth(policyDateStart, 1);

                if (dep.isNew()) {
                    dep.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
                    dep.setStActiveFlag("Y");
                    dep.setStEffectiveFlag("N");
                    dep.setStCurrency("IDR");
                    dep.setDbCurrencyRate(BDUtil.one);
                    dep.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                    dep.setDtCreateDate(new Date());
                }

                dep.setStMonths(bulan);
                dep.setStYears(years);
                dep.setDbPajak(depo.getDbPajak());
//                dep.setDbPersen(depo.getDbBunga());
                dep.setDbNominalKurs(depo.getDbNominal());
                dep.setStCostCenterCode(izinbunga.getStCostCenterCode());
                dep.setStDepoName(depo.getAccountsInvest().getStDescription());
                dep.setStKodedepo(depo.getStKodedepo());
                dep.setStRegisterBentuk(depo.getStRegister());
                dep.setDtTglAwal(policyDateStart);
                dep.setDtTglAkhir(policyDateEnd);
                dep.setStReceiptClassID(depo.getStReceiptClassID());
                dep.setStCompanyType(depo.getStCompanyType());
//                dep.setStHari(depo.getStHari());
//                if (dep.getStKodedepo().equalsIgnoreCase("1")) {
//                    dep.setStHari(depo.getStHari());
//                } else if (dep.getStKodedepo().equalsIgnoreCase("2")) {
//                    dep.setStHari(String.valueOf(BDUtil.mul(new BigDecimal(depo.getStBulan()), new BigDecimal(30))));
//                }

                BigDecimal bunga = BDUtil.mul(dep.getDbNominal(), BDUtil.getRateFromPct(dep.getDbPersen()));
                bunga = BDUtil.div(bunga, new BigDecimal(365));
//                BigDecimal prorate = BDUtil.mul(new BigDecimal(dep.getStHari()), new BigDecimal(0.8));
                BigDecimal prorate = BDUtil.mul(new BigDecimal(30), new BigDecimal(0.8));

                logger.logDebug("@@@@@@@@@@@@@@@@@@@@ bunga " + bunga);
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@ prorate " + prorate);

//                BigDecimal bunga1 = depo.reCalculateStart(depo.getStKodedepo(), depo.getStBulan(), depo.getDbNominal());
                BigDecimal bunga1 = BDUtil.mulRound(bunga, prorate, 2);
                dep.setDbAngka1(bunga1);
                logger.logDebug("@@@@@@@@@@@@@@@@@@@@ bunga1 " + bunga1);

                dep.setStARIzinBngID(izinbunga.getStARIzinBngID());

                if (approvalMode) {
//                    PreparedStatement PS2 = S.setQuery("update ar_inv_bunga set active_flag = 'Y' where nodefo = ? and ar_izinbng_id = ? ");
//
//                    PS2.setObject(1, dep.getStNodefo());
//                    PS2.setObject(2, dep.getStARIzinBngID());
//
//                    int p2 = PS2.executeUpdate();

                    saveJournalEntryIzinBunga(dep);
                }
            }
            S.store(bungadet);
//            }

            return izinbunga.getStARIzinBngID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public void saveJournalEntryIzinBunga(ARInvestmentBungaView bunga) throws Exception {
//        //logger.logDebug("########## saveJournalEntry: "+l.size());
//
//        //if (bnga.isHaveBunga()) {
//        //    throw new RuntimeException("Transaksi Bunga untuk bulan " + bnga.getStMonths() + " dan tahun " + bnga.getStYears() + " sudah terinput");
//        //}

        if (bunga.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + bunga.getStMonths() + " dan tahun " + bunga.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        try {
            bunga.markUpdate();
            bunga.setStActiveFlag("Y");
            bunga.setStEffectiveFlag("Y");
            bunga.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            bunga.setDtChangeDate(new Date());
            bunga.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
            bunga.setDtApprovedDate(new Date());
            bunga.generateNoBuktiD();
            bunga.generateRegisterBunga();
            S.store(bunga);

            JournalView j = new JournalView();
            j.markNew();

            JournalView j2 = new JournalView();
            j2.markNew();

            j.setStTransactionHeaderID(stTransactionHeaderID);
            j.setLgHeaderAccountID(new Long(bunga.getStEntityID()));
            j.setStHeaderAccountNo(bunga.getAccounts().getStAccountNo());
            j.setLgAccountID(new Long(bunga.findAccountBunga(bunga.getStCostCenterCode())));
            j.setDbEnteredCredit(bunga.getDbAngka());
            j.setDbEnteredDebit(BDUtil.zero);
            j.setDbCredit(bunga.getDbAngka());
            j.setDbDebit(BDUtil.zero);
            j.setStDescription("Bunga Deposito No: " + bunga.getStNodefo() + " " + bunga.getStDepoName());
            j.setStCurrencyCode(bunga.getStCurrency());
            j.setDbCurrencyRate(bunga.getDbCurrencyRate());
            j.setStRefTrxNo(bunga.getStARBungaID());
            j.setStIDRFlag("Y");

            j.reCalculate();

            if (j.isNew()) {
                j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j.setStTransactionNo(bunga.getStNoBuktiD());
            }

            if (bunga.getDtTglBunga() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(bunga.getDtTglBunga());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j.setLgPeriodNo(per.getLgPeriodNo());
            j.setLgFiscalYear(per.getLgFiscalYear());
            j.setStRefTrxType("INV_BUNGA");
            j.setDtApplyDate(bunga.getDtTglBunga());

            //logger.logDebug("******************* "+new Long(bunga.getStNoRekeningDeposito()));
            //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStNoRekeningDeposito());
            //logger.logDebug("################### "+j.getLgAccountID());

            if (j.isModified()) {

                final JournalView old = j.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                }
            }

            S.store(j);
            //simpen lawan

            BigDecimal debitOld = new BigDecimal(0);
            BigDecimal debitEnteredOld = new BigDecimal(0);

            if (j2.isNew()) {
                j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j2.setStTransactionHeaderID(stTransactionHeaderID);
                j2.setStTransactionNo(bunga.getStNoBuktiD());
                j2.setLgHeaderAccountID(new Long(bunga.getStEntityID()));
                j2.setStHeaderAccountNo(bunga.getAccounts().getStAccountNo());
                j2.setLgAccountID(new Long(bunga.getStEntityID()));
                j2.setDbCredit(BDUtil.zero);
                j2.setDbDebit(bunga.getDbAngka());
                j2.setDbEnteredCredit(BDUtil.zero);
                j2.setDbEnteredDebit(bunga.getDbAngka());
                j2.setStDescription("Bunga Deposito No: " + bunga.getStNodefo() + " " + bunga.getStBankName());
                j2.setStCurrencyCode(bunga.getStCurrency());
                j2.setDbCurrencyRate(bunga.getDbCurrencyRate());
                j2.setStRefTrxNo(bunga.getStARBungaID());
                j2.setStIDRFlag("Y");
            }

            if (bunga.getDtTglBunga() == null) {
                throw new Exception("Apply Date is not defined !");
            }
            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
            if (per == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + bunga.getDtTglBunga() + ")");
            }
            if (!per.isOpen()) {
                throw new Exception("Period is not open");
            }
            j2.setLgPeriodNo(per.getLgPeriodNo());
            j2.setLgFiscalYear(per.getLgFiscalYear());
            j2.setStRefTrxType("INV_BUNGA");
            j2.setDtApplyDate(bunga.getDtTglBunga());

            /*
            logger.logDebug("******************* "+new Long(bunga.getStEntityID()));
            logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStEntityID());
            logger.logDebug("################### "+j2.getLgHeaderAccountID());
            logger.logDebug("******************* "+new Long(bunga.getStNoRekeningDeposito()));
            logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+bunga.getStNoRekeningDeposito());
            logger.logDebug("################### "+j2.getLgAccountID());
             */

            if (j2.isModified()) {

                final JournalView old = j2.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j2.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                }
            }

            S.store(j2);

            //akhir simpen lawan
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveBungaBng(ARInvestmentBungaView bunga) throws Exception {

        SQLUtil S = new SQLUtil();

        try {
            if (bunga.isNew()) {
                bunga.setStARBungaID(String.valueOf(IDFactory.createNumericID("INVBUNGA")));
            }

            String register = null;
            if (bunga.getStNoBuktiB() != null) {
                register =
                        bunga.getStNoBuktiB().substring(1, 7)
                        + bunga.getStNoBuktiB().substring(14, 19);
            }

            bunga.setStRegisterBentuk(register);

            bunga.setStChangeWho(UserManager.getInstance().getUser().getStUserID());
            bunga.setDtChangeDate(new Date());
            bunga.setDbAngka1(bunga.getDbAngka());

            S.store(bunga);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public void AccountNeracaItemCabang(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag, String cabang) throws Exception, RemoteException {
        final SQLUtil S = new SQLUtil();

        try {
            String query;

            query = "update gl_acct_bal_neracatotal_cabang set cr" + periodTo + " = 0,db" + periodTo + " = " + neracaItem + ", bal" + periodTo + " = " + neracaItem + ", idr_flag = '" + flag + "' where account_id = ? and period_year = ? and cc_code = ? ";

            PreparedStatement PS = S.setQuery(query);

            PS.setObject(1, am);
            PS.setLong(2, yearFrom);
            PS.setObject(3, cabang);

            int i = PS.executeUpdate();

            if (i == 0) {
                S.releaseResource();

                String query2 = "insert into gl_acct_bal_neracatotal_cabang (account_id,period_year,cc_code,cr" + periodTo + ",db" + periodTo + ",bal" + periodTo + ",idr_flag) values (?,?,?,0,?,?,?)";

                PS = S.setQuery(query2);

                PS.setObject(1, am);
                PS.setLong(2, yearFrom);
                PS.setObject(3, cabang);
                PS.setBigDecimal(4, neracaItem);
                PS.setBigDecimal(5, neracaItem);
                PS.setObject(6, flag);

                i = PS.executeUpdate();

                if (i == 0) {
                    throw new Exception("Failed to update gl account balance");
                }
            }
        } finally {
            S.release();
        }
    }

    public String saveUpload(ARInvestmentDepositoView deposito) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
            }

            S.store(deposito);


            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public String saveIzinCair(ARInvestmentIzinPencairanView izinpencairan, boolean approvalMode) throws Exception {
        logger.logDebug("####################### " + approvalMode);

        boolean checkPeriod = Tools.compare(izinpencairan.getDtMutationDate(), new Date()) >= 0;
        if (checkPeriod) {
            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
        }

        final SQLUtil S = new SQLUtil();

        String ccCode = izinpencairan.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(izinpencairan.getDtMutationDate());

        try {
            if (izinpencairan.isNew()) {
                izinpencairan.setStARIzinCairID(String.valueOf(IDFactory.createNumericID("INVIZINCAIR")));
                izinpencairan.setStActiveFlag("Y");
                izinpencairan.setStPrintFlag("N");
                izinpencairan.setStApprovedCabFlag("N");
                izinpencairan.setStApprovedPusFlag("N");

                String noSurat = String.valueOf(IDFactory.createNumericID("INVIZINCAIRSURAT" + counterKey + ccCode, 1));
                izinpencairan.setStNoSurat(noSurat + "/ASKRIDA-" + izinpencairan.getCostCenter3().getStShortname().toUpperCase() + "/CAIR/" + DateUtil.getMonth2Digit(izinpencairan.getDtMutationDate()) + "/" + DateUtil.getYear(izinpencairan.getDtMutationDate()));
            }

            S.store(izinpencairan);

            final DTOList details = izinpencairan.getPencairandet();

            final DTOList combined = new DTOList();

            combined.addAll(details);
            combined.addAll(details.getDeleted());

//            final DTOList pencairanInv = izinpencairan.getPencairandet();
            for (int i = 0; i < combined.size(); i++) {
                ARInvestmentIzinPencairanDetView dep = (ARInvestmentIzinPencairanDetView) combined.get(i);

                if (dep.isNew()) {
                    dep.setStARIzinCairDetID(String.valueOf(IDFactory.createNumericID("INVIZINCAIRDET")));
                    dep.setStActiveFlag("Y");
                }

//                dep.markUpdate();
                dep.setStARIzinCairID(izinpencairan.getStARIzinCairID());

                dep.setStARDepoID(dep.getStARDepoID());
                dep.setStNodefo(dep.getStNodefo());
                dep.setStBuktiB(dep.getStBuktiB());
                dep.setDbBilyetAmount(dep.getDbBilyetAmount());
                dep.setStLineType("INVOICE");

                if (approvalMode) {
                    PreparedStatement PS2 = S.setQuery("update ar_inv_pencairan set ar_izincairdet_id = ?, ar_izincair_id = ?, active_cair_flag = 'Y' where nodefo = ? and bukti_b = ? and active_flag = 'Y' ");

                    PS2.setObject(1, dep.getStARIzinCairDetID());
                    PS2.setObject(2, dep.getStARIzinCairID());
                    PS2.setObject(3, dep.getStNodefo());
                    PS2.setObject(4, dep.getStBuktiB());

                    int p2 = PS2.executeUpdate();
                }

                final DTOList pencairanDet = dep.getListRealisasi();
                for (int j = 0; j < pencairanDet.size(); j++) {
                    ARInvestmentIzinPencairanDetView det = (ARInvestmentIzinPencairanDetView) pencairanDet.get(j);

                    if (det.isNew()) {
                        det.setStARIzinCairDetID(String.valueOf(IDFactory.createNumericID("INVIZINCAIRDET")));
                        det.setStActiveFlag("Y");
                    }

//                    det.markUpdate();
                    det.setStARIzinCairID(izinpencairan.getStARIzinCairID());

                    det.setStARDepoID(dep.getStARDepoID());
                    det.setStPencairanKet(det.getStPencairanKet());
                    det.setStARInvoiceID(det.getStARInvoiceID());
                    det.setStDLANo(det.getStDLANo());
                    det.setStJenisCair(det.getStJenisCair());
                    det.setDbNilai(det.getDbNilai());
                    det.setStLineType("REALISASI");

//                    if (approvalMode) {
//                            approveIzinNotKlaim(izinpencairan, det);
//                    }
                }

//                if (approvalMode) {
////                    final DTOList deposito = dep.getDeposito();
//                    final DTOList deposito = dep.getPerpanjangan();
//                    for (int j = 0; j < deposito.size(); j++) {
////                        ARInvestmentDepositoView depo = (ARInvestmentDepositoView) deposito.get(j);
//                        ARInvestmentPerpanjanganView depo = (ARInvestmentPerpanjanganView) deposito.get(j);
//
//                        ARInvestmentPencairanView cair = new ARInvestmentPencairanView();
//                        cair.markNew();
//
//                        if (cair.isNew()) {
//                            cair.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
//                            cair.setStActiveFlag("Y");
//                            cair.setStEffectiveFlag("N");
//                            cair.setStActiveCairFlag("Y");
//                            cair.setStKonter("0");
//                            cair.setStCurrency("IDR");
//                            cair.setDbCurrencyRate(BDUtil.one);
//                            cair.setStJournalStatus("NEW");
//                            cair.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
//                            cair.setDtCreateDate(new Date());
//                        }
//
//                        cair.setStARDepoID(depo.getStARDepoID());
//                        cair.setStNodefo(depo.getStNodefo());
//                        cair.setStBuktiB(depo.getStBuktiB());
//                        cair.setStNoRekeningDeposito(depo.getStNoRekeningDeposito());
//                        cair.setStCostCenterCode(depo.getStCostCenterCode());
//                        cair.setStReceiptClassID(depo.getStReceiptClassID());
//                        cair.setStCompanyType(depo.getStCompanyType());
//                        cair.setStRegisterBentuk(depo.getStRegister());
//                        cair.setStDepoName(depo.getStDepoName());
//                        cair.setStAccountDepo(depo.getStAccountDepo());
//                        cair.setStNoRekening(depo.getStNoRekening());
//                        cair.setStMonths(izinpencairan.getStMonths());
//                        cair.setStYears(izinpencairan.getStYears());
//                        cair.setStKodedepo(depo.getStKodedepo());
//                        cair.setStHari(depo.getStHari());
//                        cair.setStBulan(depo.getStBulan());
//
//                        cair.setDbNominal(depo.getDbNominal());
//                        cair.setDbNominalKurs(depo.getDbNominalKurs());
//                        cair.setDbPajak(depo.getDbPajak());
//                        cair.setDbBunga(depo.getDbBunga());
//
//                        cair.setDtTgldepo(depo.getDtTgldepo());
//                        cair.setDtTglmuta(depo.getDtTglmuta());
//                        cair.setDtTglawal(depo.getDtTglawal());
//                        cair.setDtTglakhir(depo.getDtTglakhir());
//
//                        cair.setStEntityID(cair.findAccountBankUtama(cair.getStCostCenterCode()));
//                        cair.setStAccountBank(cair.getAccounts().getStAccountNo());
//                        cair.setStBankName(cair.getAccounts().getStDescription());
//
//                        cair.setStARIzinCairDetID(dep.getStARIzinCairDetID());
//                        cair.setStARIzinCairID(dep.getStARIzinCairID());
//                        S.store(cair);
//                    }
//                }
                S.store(pencairanDet);
            }

            S.store(combined);

            return izinpencairan.getStARIzinCairID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public void reverseIzinCair(ARInvestmentIzinPencairanView izinpencairan) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            DTOList pencairan = izinpencairan.getPencairan();
            for (int i = 0; i < pencairan.size(); i++) {
                ARInvestmentPencairanView adr = (ARInvestmentPencairanView) pencairan.get(i);

                if (adr.isEffective()) {
                    throw new Exception("Bilyet " + adr.getStNodefo() + " sudah disetujui");
                }
            }

            PreparedStatement PS1 = S.setQuery("update ar_inv_pencairan set active_flag = 'N', active_cair_flag = 'N', deleted = 'Y' where ar_izincair_id = ?");

            PS1.setObject(1, izinpencairan.getStARIzinCairID());

            int p1 = PS1.executeUpdate();

            DTOList pencairandet = izinpencairan.getPencairandet();
            for (int i = 0; i < pencairandet.size(); i++) {
                ARInvestmentIzinPencairanDetView adr = (ARInvestmentIzinPencairanDetView) pencairandet.get(i);

                final DTOList pencairanDet = adr.getListRealisasi();
                for (int j = 0; j < pencairanDet.size(); j++) {
                    ARInvestmentIzinPencairanDetView det = (ARInvestmentIzinPencairanDetView) pencairanDet.get(j);

                    if (!det.getStJenisCair().equalsIgnoreCase("1")) {
                        continue;
                    }

                    PreparedStatement PS2 = S.setQuery("update ar_invoice set no_surat_hutang = null, approved_flag = 'N' where ar_invoice_id = ? and coalesce(cancel_flag,'') <> 'Y' and ar_trx_type_id = 12 ");

                    PS2.setObject(1, det.getStARInvoiceID());

                    int p2 = PS2.executeUpdate();
                }
            }

            PreparedStatement PS = S.setQuery("update ar_izin_pencairan set approvedcab_flag = 'N', approvedcab_who = null, approvedcab_date = null,"
                    + "approvedpus_flag = 'N', approvedpus_who = null, approvedpus_date = null where ar_izincair_id = ?");

            PS.setObject(1, izinpencairan.getStARIzinCairID());

            int p = PS.executeUpdate();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

     public void approveIzinNotKlaim(ARInvestmentIzinPencairanView izincair, ARInvestmentIzinPencairanDetView izincairdet) throws Exception {

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        Date applyDate = new Date();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        izincairdet.generateNoBukti();
        izincairdet.markUpdate();
        S.store(izincairdet);

        try {
            JournalView j3 = new JournalView();
            j3.markNew();

            JournalView j4 = new JournalView();
            j4.markNew();

            j3.setStTransactionHeaderID(stTransactionHeaderID);
//                j3.setLgHeaderAccountID(new Long(depo.getStKdBankDana()));
//                j3.setStHeaderAccountNo(depo.getAccountsDana().getStAccountNo());
            j3.setLgAccountID(new Long(izincairdet.findAccount("2100000000", izincair.getStCostCenterCode())));
            j3.setDbDebit(izincairdet.getDbNilai());
            j3.setDbCredit(BDUtil.zero);
            j3.setDbEnteredDebit(izincairdet.getDbNilai());
            j3.setDbEnteredCredit(BDUtil.zero);
            j3.setStDescription(izincairdet.getStJenisDescription() + " " + izincairdet.getStPencairanKet());
            j3.setStCurrencyCode("IDR");
            j3.setDbCurrencyRate(BDUtil.one);
            j3.setStRefTrxNo(izincairdet.getStARDepoID());
            j3.setStIDRFlag("Y");

            j3.reCalculate();

            if (j3.isNew()) {
                j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j3.setStTransactionNo(izincairdet.getStReceiptNo());
            }

            if (applyDate == null) {
                throw new Exception("Apply Date is not defined !");
            }
            final PeriodDetailView period = PeriodManager.getInstance().getPeriodFromDate(applyDate);
            if (period == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j3.getDtApplyDate() + ")");
            }
            if (!period.isOpen()) {
                throw new Exception("Period is not open");
            }
            j3.setLgPeriodNo(period.getLgPeriodNo());
            j3.setLgFiscalYear(period.getLgFiscalYear());
            j3.setStRefTrxType("CASHBANK");
            j3.setDtApplyDate(applyDate);

            //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
            //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
            //logger.logDebug("################### "+j3.getLgAccountID());

            if (j3.isModified()) {

                final JournalView old = j3.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j3.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                }
            }

            S.store(j3);
            //simpen lawan

            if (j4.isNew()) {
                j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j4.setStTransactionHeaderID(stTransactionHeaderID);
                j4.setStTransactionNo(izincairdet.getStReceiptNo());
//                    j4.setLgHeaderAccountID(new Long(depo.getStKdBankDana()));
//                    j4.setStHeaderAccountNo(depo.getAccountsDana().getStAccountNo());
//                    j4.setLgAccountID(new Long(depo.getStKdBankDana()));
                j4.setDbDebit(BDUtil.zero);
                j4.setDbCredit(izincairdet.getDbNilai());
                j4.setDbEnteredDebit(BDUtil.zero);
                j4.setDbEnteredCredit(izincairdet.getDbNilai());
                j4.setStDescription(izincairdet.getStJenisDescription() + " " + izincairdet.getStPencairanKet());
                j4.setStCurrencyCode("IDR");
                j4.setDbCurrencyRate(BDUtil.one);
                j4.setStRefTrxNo(izincairdet.getStARDepoID());
                j4.setStIDRFlag("Y");
            }

            if (applyDate == null) {
                throw new Exception("Apply Date is not defined !");
            }
            // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j4.getDtApplyDate());
            if (period == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + applyDate + ")");
            }
            if (!period.isOpen()) {
                throw new Exception("Period is not open");
            }
            j4.setLgPeriodNo(period.getLgPeriodNo());
            j4.setLgFiscalYear(period.getLgFiscalYear());
            j4.setStRefTrxType("CASHBANK");
            j4.setDtApplyDate(applyDate);

            /*
            logger.logDebug("******************* "+new Long(depo.getStEntityID()));
            logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
            logger.logDebug("################### "+j4.getLgHeaderAccountID());
            logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
            logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
            logger.logDebug("################### "+j4.getLgAccountID());
             */

            if (j4.isModified()) {

                final JournalView old = j4.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j4.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j4.getLgAccountID(), j4.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                }
            }

            S.store(j4);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void recalculatedet(ARRequestFee arrequest) throws Exception {

        BigDecimal totalKertasKerja = null;
        BigDecimal anggaranInduk = null;
//        BigDecimal anggaranSebelum = null;

        DTOList detKertasKerja = arrequest.getReqKertaskerja();
        for (int i = 0; i < detKertasKerja.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) detKertasKerja.get(i);

//            totalKertasKerja = getTotalRealisasiRKAP(object.getStAccountID2());
            totalKertasKerja = getTotalRealisasiRKAP(object.getStARRequestID(), object.getStAccountID2()); //5jt

            anggaranInduk = getAnggaranIndukRKAP(arrequest, object.getStAccountID2());

//            anggaranSebelum = getTerpakaiSebelumRKAP(object.getStAccountID2());

            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ totalKertasKerja: " + totalKertasKerja);
            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ anggaranInduk: " + anggaranInduk);
//            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ anggaranSebelum: " + anggaranSebelum);
            String warningRKAP = "Total Realisasi untuk Kertas Kerja " + getKertasKerjaRKAP(arrequest, object.getStAccountID2()).toUpperCase() + " (" + JSPUtil.print(totalKertasKerja, 2) + ") melebihi limit Anggaran (" + JSPUtil.print(anggaranInduk, 2) + ")";

            boolean enoughLimit = false;
            enoughLimit = Tools.compare(anggaranInduk, totalKertasKerja) >= 0;
//            enoughLimit = Tools.compare(anggaranInduk, object.getDbTotalNilai()) >= 0;

            if (!enoughLimit) {
                throw new RuntimeException(warningRKAP);
            }
        }
    }

    public BigDecimal getTotalRealisasiRKAP(String reqID, String kertaskerjaID) throws Exception {
        final SQLUtil S = new SQLUtil();

        /*
        select coalesce(a.jumlah_pusat,0)
        from kertas_kerja a
        where a.id_kertas_kerja = 1522
         */

        try {
            S.setQuery(
                    "select sum(coalesce(b.total_nilai,0)) from ar_request_fee a "
                    + "inner join ar_request_fee_obj b on b.req_id = a.req_id "
                    + "where a.status = 'REALIZED' and a.req_id = ? and b.accountid2 = ? and b.delete_flag is null ");

            S.setParam(1, reqID);
            S.setParam(2, kertaskerjaID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

     public BigDecimal getAnggaranIndukRKAP(ARRequestFee arrequest, String kertaskerjaID) throws Exception {
        final SQLUtil S = new SQLUtil("RKAPDB");

        /*
        select coalesce(a.jumlah_pusat,0)
        from kertas_kerja a
        where a.id_kertas_kerja = 1522
         */

        String select =null;
        if(arrequest.getStPilihan().equalsIgnoreCase("14")){
            select = "select sum(coalesce(a.jumlah_pusat,0)) "
                    + "from sub_barang a "
                    + "where a.id_sub_barang = ? ";
        } else{
            select = "select coalesce(a.jumlah_pusat,0) "
                    + "from kertas_kerja a "
                    + "where a.kode = ? ";
        }

        try {
            S.setQuery(select);

            S.setParam(1, kertaskerjaID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

      public BigDecimal getTerpakaiSebelumRKAP(ARRequestFee arrequest, String kertaskerjaID) throws Exception {
        final SQLUtil S = new SQLUtil("RKAPDB");

        /*
        select coalesce(a.jumlah_pusat,0)
        from kertas_kerja a
        where a.id_kertas_kerja = 1522
         */

        String select = null;
        if (arrequest.getStPilihan().equalsIgnoreCase("14")) {
            select = "select sum(coalesce(a.anggaran_terpakai,0)) "
                    + "from sub_barang a "
                    + "where a.id_sub_barang = ? ";
        } else {
            select = "select sum(coalesce(a.anggaran_terpakai,0)) "
                    + "from kertas_kerja a "
                    + "where a.kode = ? ";
        }

        try {
            S.setQuery(select);

            S.setParam(1, kertaskerjaID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public String getKertasKerjaRKAP(ARRequestFee arrequest, String kertaskerjaID) throws Exception {
        final SQLUtil S = new SQLUtil("RKAPDB");

        /*
        select coalesce(a.jumlah_pusat,0)
        from kertas_kerja a
        where a.id_kertas_kerja = 1522
         */

        String select =null;
        if(arrequest.getStPilihan().equalsIgnoreCase("14")){
            select = "select f.nama_barang "
                    + "from sub_barang a "
                    + "join barang f on (a.id_barang=f.id_barang) "
                    + "where a.id_sub_barang = ? ";
        } else{
            select = "select a.rencana_kerja "
                    + "from kertas_kerja a "
                    + "where a.id_kertas_kerja = ? ";
        }

        try {
            S.setQuery(select);

            S.setParam(1, kertaskerjaID);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void approveCashier(ARRequestFee req, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l.size() + " \n" + l);

        if (l.size() < 1) {
            return;
        }

        if (req.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + req.getStMonths() + " dan tahun " + req.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        Date applyDate = new Date();

        try {

            //akhir simpen lawan
            for (int i = 0; i < l.size(); i++) {
                ARRequestFeeObj reqObj = (ARRequestFeeObj) l.get(i);

                JournalView j3 = new JournalView();
                j3.markNew();

                j3.setStTransactionHeaderID(stTransactionHeaderID);
                j3.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j3.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
                j3.setLgAccountID(new Long(reqObj.findAccount(reqObj.getStAccountNo(), req.getStCostCenterCode())));
                j3.setDbDebit(reqObj.getDbNominalRealisasi());
                j3.setDbCredit(BDUtil.zero);
                j3.setDbEnteredDebit(reqObj.getDbNominalRealisasi());
                j3.setDbEnteredCredit(BDUtil.zero);
                j3.setStDescription(reqObj.getStDescription());
                j3.setStCurrencyCode(req.getStCurrency());
                j3.setDbCurrencyRate(req.getDbCurrencyRate());
                j3.setStYears(req.getStYears());
                j3.setStMonths(req.getStMonths());
                j3.setStIDRFlag("Y");

                j3.reCalculate();

                if (j3.isNew()) {
                    j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j3.setStTransactionNo(req.getStTransactionNo());
                }

                if (reqObj.getDtTglCashback() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(reqObj.getDtTglCashback());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + reqObj.getDtTglCashback() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j3.setLgPeriodNo(per.getLgPeriodNo());
                j3.setLgFiscalYear(per.getLgFiscalYear());
                j3.setStRefTrxType("CASHBANK");
                j3.setDtApplyDate(reqObj.getDtTglCashback());

                if (j3.isModified()) {

                    final JournalView old = j3.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j3.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j3.getLgAccountID(), j3.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                    }
                }

                S.store(j3);
            }

//                //simpan bank

            JournalView j4 = new JournalView();
            j4.markNew();

            if (j4.isNew()) {
                j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                j4.setStTransactionHeaderID(stTransactionHeaderID);
                j4.setStTransactionNo(req.getStTransactionNo());
                j4.setLgHeaderAccountID(new Long(req.getStAccountID()));
                j4.setStHeaderAccountNo(req.getAccounts().getStAccountNo());
                j4.setLgAccountID(new Long(req.getStAccountID()));
                j4.setDbCredit(req.getDbNominalUsed());
                j4.setDbDebit(BDUtil.zero);
                j4.setDbEnteredCredit(req.getDbNominalUsed());
                j4.setDbEnteredDebit(BDUtil.zero);
                j4.setStDescription(req.getStDescription());
                j4.setStCurrencyCode(req.getStCurrency());
                j4.setDbCurrencyRate(req.getDbCurrencyRate());
                j4.setStYears(req.getStYears());
                j4.setStMonths(req.getStMonths());
                j4.setStIDRFlag("Y");
            }

            if (applyDate == null) {
                throw new Exception("Apply Date is not defined !");
            }

            final PeriodDetailView per2 = PeriodManager.getInstance().getPeriodFromDate(applyDate);
            if (per2 == null) {
                throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + applyDate + ")");
            }
            if (!per2.isOpen()) {
                throw new Exception("Period is not open");
            }
            j4.setLgPeriodNo(per2.getLgPeriodNo());
            j4.setLgFiscalYear(per2.getLgFiscalYear());
            j4.setStRefTrxType("CASHBANK");
            j4.setDtApplyDate(applyDate);

            if (j4.isModified()) {

                final JournalView old = j4.getOldJournal();

                if (old != null) {

                    final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                            && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                    if (isSameRecord) {
                        final BigDecimal am = j4.getDbAdjustmentAmount();

                        if (am.compareTo(BDUtil.zero) != 0) {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                            //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                        }
                    } else {
                        updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                        updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                    }
                } else {
                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                }
            }
            S.store(j4);
            //akhir simpen lawan
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void updateProposal(ARRequestFee arrequest) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_request_fee set act_flag = 'N' "
                    + "where req_id = ? ");

            P.setObject(1, arrequest.getStParentID());
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

   public void updateKertasKerja(ARRequestFee arrequest) throws Exception {

        BigDecimal totalKertasKerja = null;
        BigDecimal anggaranSebelum = null;

        DTOList detKertasKerja = arrequest.getReqKertaskerja();
        for (int i = 0; i < detKertasKerja.size(); i++) {
            ARRequestFeeObj object = (ARRequestFeeObj) detKertasKerja.get(i);

//            totalKertasKerja = getTotalRealisasiRKAP(object.getStAccountID2());
            totalKertasKerja = getTotalRealisasiRKAP(object.getStARRequestID(), object.getStAccountID2()); //5jt

            anggaranSebelum = getTerpakaiSebelumRKAP(arrequest, object.getStAccountID2());//11jt

            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ totalKertasKerja: " + totalKertasKerja);
            logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ anggaranSebelum: " + anggaranSebelum);

            updateRKAP(object.getStAccountID2(), anggaranSebelum, totalKertasKerja);
//            updateRKAP(object.getStAccountID2(), totalKertasKerja);
        }
    }

    public void updateRKAP(String kertaskerjaid, BigDecimal used, BigDecimal now) throws Exception {

        final SQLUtil S = new SQLUtil("RKAPDB");

        try {

            PreparedStatement P = S.setQuery("update kertas_kerja set anggaran_terpakai = ? "
                    + "where id_kertas_kerja = ? ");

            P.setObject(1, BDUtil.roundUp(BDUtil.add(now, used)));
            P.setObject(2, kertaskerjaid);
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

     public void saveRequest(ARRequestFee arrequest, String stNextStatus, boolean approvalMode) throws Exception {

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ status : " + approvalMode);

        if (approvalMode) {
            if (arrequest.isPosted()) {
                throw new RuntimeException("Transaksi bulan " + arrequest.getStMonths() + " dan tahun " + arrequest.getStYears() + " tsb sudah diposting");
            }
        }

        SQLUtil S = new SQLUtil();

        try {

            if (stNextStatus != null) {

                if ("REALISASI".equalsIgnoreCase(stNextStatus)) {
                    final ARRequestFee oldRequest = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arrequest.getStARRequestID());

                    oldRequest.markUpdate();

                    S.store(oldRequest);

                } else {
                    final ARRequestFee oldRequest = (ARRequestFee) DTOPool.getInstance().getDTO(ARRequestFee.class, arrequest.getStARRequestID());

                    oldRequest.markUpdate();

                    oldRequest.setStActiveFlag("N");

                    S.store(oldRequest);
                }

                arrequest.setStParentID(arrequest.getStARRequestID());
                arrequest.markNew();
                arrequest.setStActiveFlag("Y");
                arrequest.setStStatus(stNextStatus);
                arrequest.setStApprovedWho(null);
                arrequest.setDtApprovedDate(null);
                arrequest.setStCashierFlag("N");
                arrequest.setStCashierWho(null);
                arrequest.setDtCashierDate(null);
                arrequest.setStRootID(arrequest.getStRootID());

                arrequest.getReqObject().convertAllToNew();
                arrequest.getDocuments().convertAllToNew();
                arrequest.getApproval().convertAllToNew();

            }

            if (arrequest.isNew()) {
                arrequest.setStARRequestID(String.valueOf(IDFactory.createNumericID("INVREQUEST")));
            }

            if (arrequest.getStRootID() == null) {
                arrequest.setStRootID(arrequest.getStARRequestID());
            }

            final DTOList documents = arrequest.getDocuments();

            for (int j = 0; j < documents.size(); j++) {
                ARRequestDocumentsView doc = (ARRequestDocumentsView) documents.get(j);

                if (doc.isNew()) {
                    doc.setStDocumentInID(String.valueOf(IDFactory.createNumericID("INVREQUESTDOC")));
                }

                doc.setStInID(arrequest.getStARRequestID());
            }

            S.store(documents);

            final DTOList policyDocuments = arrequest.getPolicyDocuments();

            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                doc.setStPolicyID(arrequest.getStARRequestID());

                final boolean marked = doc.isMarked();

                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null) {
                        doc.markUpdate();
                    } else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }

                if (!marked && doc.getStInsurancePolicyDocumentID() != null) {
                    doc.markDelete();
                }
            }

            S.store(policyDocuments);

            final DTOList approval = arrequest.getApproval();

            for (int j = 0; j < approval.size(); j++) {
                ARRequestApprovalView doc = (ARRequestApprovalView) approval.get(j);

                if (doc.isNew()) {
                    doc.setStApprovalInID(String.valueOf(IDFactory.createNumericID("INVREQUESTAPP")));
                }

                doc.setStInID(arrequest.getStARRequestID());
            }

            S.store(approval);

            BigDecimal nominalUsed = null;

            final DTOList reqObj = arrequest.getReqObject();

            for (int i = 0; i < reqObj.size(); i++) {
                ARRequestFeeObj obj = (ARRequestFeeObj) reqObj.get(i);

                if (obj.isNew()) {
                    obj.setStARRequestObjID(String.valueOf(IDFactory.createNumericID("INVREQUESTOBJ")));
                }

                nominalUsed = BDUtil.add(nominalUsed, obj.getDbTotalNilai());

                obj.setStARRequestID(arrequest.getStARRequestID());
                obj.setDtTglCashback(arrequest.getDtTglRequest());
            }

            S.store(reqObj);

            arrequest.setDbNominalUsed(nominalUsed);

            if ("PENGAJUAN".equalsIgnoreCase(arrequest.getStStatus())) {
                if (arrequest.getStRequestNo() == null) {
                    arrequest.generateNoRequest();
                }
            }

            if ("REALISASI".equalsIgnoreCase(arrequest.getStStatus())) {
                if (!approvalMode) {
                    if (arrequest.getStRequestNo() == null) {
                        arrequest.generateNoRequest();
                    } else {
                        updateProposal(arrequest);
                    }
                }
            }

            recalculatedet(arrequest);

            if (arrequest.isCashierFlag()) {
//            if (approvalMode && arrequest.isStatusRealized()) {
                if (arrequest.getStTransactionNo() == null) {
                    arrequest.generateNoTransaction();
                }
            }

            if (approvalMode && arrequest.isCashierFlag() && arrequest.isStatusRealized()) {
//            if (approvalMode && arrequest.isStatusRealized()) {
                approveCashier(arrequest, reqObj);
                updateKertasKerja(arrequest);
            }

            S.store(arrequest);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    private void postReinsProfitCommision(DTOList reinsAll, InsuranceClosingView closing)
    throws Exception
  {
    UserSession us = SessionManager.getInstance().getSession();

    DTOList reins = reinsAll;

    SQLUtil S = new SQLUtil();
    for (int i = 0; i < reins.size(); i++)
    {
      InsurancePolicyInwardView ri = (InsurancePolicyInwardView)reins.get(i);

      getRemoteAccountReceivable().saveSaldoAwalInwardClosing(ri, closing);

      PreparedStatement PS = S.setQuery("BEGIN;update ins_gl_closing set data_proses = coalesce(data_proses,0) + 1 where closing_id = ?;COMMIT;");
      PS.setObject(1, closing.getStClosingID());
      int j = PS.executeUpdate();
      if (j != 0) {
        logger.logInfo("+++++++ UPDATE counter proses : " + closing.getStClosingID() + " ++++++++++++++++++");
      }
      S.release();
    }
  }

    public String saveUlang(ARInvestmentPencairanView pencairan) throws Exception {
        //logger.logDebug("save: "+deposito);

        final SQLUtil S = new SQLUtil();

        try {
//            if (pencairan.isNew()) {
//                pencairan.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
//            }

            ARInvestmentDepositoView deposito = new ARInvestmentDepositoView();
            deposito.markNew();

            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
                deposito.setStStatus("DEPOSITO");
                deposito.setStKeterangan("Pembentukan");
                deposito.setStJournalStatus("NEW");
                deposito.setStKonter("0");
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("N");
                deposito.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtCreateDate(new Date());

                deposito.setStAccountDepo(pencairan.getStAccountDepo());
                deposito.setStAccountBank(pencairan.getStAccountBank());
            }

            deposito.setStNodefo(pencairan.getStNodefo());
            deposito.setStHari(pencairan.getStHari());
            deposito.setStBulan(pencairan.getStBulan());
            deposito.setStCostCenterCode(pencairan.getStCostCenterCode());
            deposito.setStReceiptClassID(pencairan.getStReceiptClassID());
            deposito.setStCompanyType(pencairan.getStCompanyType());
            deposito.setStCurrency(pencairan.getStCurrency());
            deposito.setDbCurrencyRate(pencairan.getDbCurrencyRate());
            deposito.setDbPajak(pencairan.getDbPajak());
            deposito.setDbBunga(pencairan.getDbBunga());
            deposito.setDbNominal(pencairan.getDbNominal());
            deposito.setDbNominalKurs(pencairan.getDbNominalKurs());
            deposito.setDtTglawal(pencairan.getDtTglawal());
            deposito.setDtTglakhir(pencairan.getDtTglakhir());
            deposito.setDtTgldepo(pencairan.getDtTgldepo());
            deposito.setDtTglmuta(pencairan.getDtTglmuta());
            deposito.setStRegister(pencairan.getStRegister());
            deposito.setStKodedepo(pencairan.getStKodedepo());
            deposito.setStNoRekeningDeposito(pencairan.getStNoRekeningDeposito());
            deposito.setStEntityID(pencairan.getStEntityID());
            deposito.setStDepoName(pencairan.getStDepoName());
            deposito.setStBankName(pencairan.getStBankName());
            deposito.setStAccountDepo(pencairan.getStAccountDepo());
            deposito.setStNoRekening(pencairan.getStNoRekening());
            deposito.setStMonths(pencairan.getStMonths());
            deposito.setStYears(pencairan.getStYears());
            deposito.setStARCairUlangID(pencairan.getStARCairID());
            deposito.generateNoBuktiDeposito();
            deposito.generateRegisterBentuk();

            S.store(deposito);

            return pencairan.getStARCairID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public String savePemasaran(BiayaPemasaranView pemasaran) throws Exception {

//        if (pemasaran.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
//        }

//        boolean checkPeriod = Tools.compare(pemasaran.getDtEntryDate(), new Date()) >= 0;
//        if (checkPeriod) {
//            throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
//        }

        final SQLUtil S = new SQLUtil();

        String ccCode = pemasaran.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(pemasaran.getDtEntryDate());

        try {
            if (pemasaran.isNew()) {
                pemasaran.setStPemasaranID(String.valueOf(IDFactory.createNumericID("ARBIAYAPMS")));

                String noUrutSHK = String.valueOf(IDFactory.createNumericID("SHMNO" + counterKey + ccCode, 1));
                pemasaran.setStNoSPP(noUrutSHK + "/SBP/BIAYA/" + pemasaran.getStCostCenterCode() + "/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date()));
            }

            /*sistem lama
//            if (pemasaran.isNonTunai()) {
//                pemasaran.setStStatus1("Y");
//                pemasaran.setStStatus2("Y");
//                pemasaran.setStStatus3("Y");
//                pemasaran.setStStatus4("Y");
//                pemasaran.setStValidasiF("Y");
//                pemasaran.setStKabagApproved("05870558");
//            }
//
//            if (!pemasaran.isNonTunai()) {
//                if (pemasaran.isExOnePersen()) {
//                    pemasaran.setStValidasiF(null);
//                }
//
//                if (!pemasaran.isExOnePersen()) {
//                    pemasaran.setStValidasiF("Y");
//                }
//            }*/

            if (pemasaran.isExOnePersen()) {
                pemasaran.setStValidasiF(null);
            }

            if (!pemasaran.isExOnePersen()) {
                pemasaran.setStValidasiF("Y");
            }

            S.store(pemasaran);

            final DTOList detail = pemasaran.getPmsDetail();
            for (int i = 0; i < detail.size(); i++) {
                BiayaPemasaranDetailView det = (BiayaPemasaranDetailView) detail.get(i);

                if (det.isNew()) {
                    det.setStPemasaranDetailID(String.valueOf(IDFactory.createNumericID("ARBIAYAPMSDET")));
                }

                det.setStPemasaranID(pemasaran.getStPemasaranID());
            }

            S.store(detail);

            final DTOList documents = pemasaran.getDocuments();
            for (int j = 0; j < documents.size(); j++) {
                BiayaPemasaranDocumentsView doc = (BiayaPemasaranDocumentsView) documents.get(j);

                if (doc.isNew()) {
                    doc.setStDocumentPmsID(String.valueOf(IDFactory.createNumericID("ARBIAYAPMSDOC")));
                }

                doc.setStPemasaranID(pemasaran.getStPemasaranID());
            }

            S.store(documents);

//            if (pemasaran.isNonTunai()) {
//                approvePmsBayar(pemasaran, detail);
//                logger.logDebug("@@@@@@@@@@@ non tunai jurnal");
//            }

            return pemasaran.getStPemasaranID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public void reversePms(BiayaPemasaranView pemasaran) throws Exception {

        if (pemasaran.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
        }

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement P = S.setQuery("delete from gl_je_detail where trx_no = ?");

            //HANYA NOBUKTI BAYAR SAJA
            P.setObject(1, pemasaran.getStNoBuktiBayar());
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public BiayaPemasaranView loadPemasaran(String arpmsid) throws Exception {
        final BiayaPemasaranView depo = (BiayaPemasaranView) ListUtil.getDTOListFromQuery(
                "select * from biaya_pemasaran where pms_id = ? ",
                new Object[]{arpmsid},
                BiayaPemasaranView.class).getDTO();

        return depo;
    }

    public BiayaPemasaranView getPemasaranForPrinting(String rcid) throws Exception {

        final BiayaPemasaranView rcp = (BiayaPemasaranView) ListUtil.getDTOListFromQuery(
                "select a.* from biaya_pemasaran a where pms_id = ?",
                new Object[]{rcid},
                BiayaPemasaranView.class).getDTO();

        //rcp = getARReceipt(rcid);
        return rcp;
    }

    public void getPemasaranForPrintingExcel(String rcid) throws Exception {

        final DTOList l = EXCEL_PEMASARAN(rcid);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_PEMASARAN();

    }

    public DTOList EXCEL_PEMASARAN(String rcid) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pms_id,a.no_spp,a.no_bukti_bayar,a.ket,b.pms_det_id,b.accountno,b.description,b.nilai,b.excess_amount ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id ");
        sqa.addClause("a.pms_id = ?");
        sqa.addPar(rcid);

        final String sql = sqa.getSQL() + " order by a.pms_id,b.pms_det_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_PEMASARAN() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("biayapemasaran");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Lampiran Biaya Pemasaran");

            XSSFRow row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("NO. SBP : " + h.getFieldValueByFieldNameST("no_spp"));
            XSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("NO. Surat Pemasaran : " + h.getFieldValueByFieldNameST("ket"));

            //bikin header
            XSSFRow row3 = sheet.createRow(4);
            row3.createCell(0).setCellValue("no akun");
            row3.createCell(1).setCellValue("deskripsi");
            row3.createCell(2).setCellValue("nilai");
            row3.createCell(3).setCellValue("pembayaran");
            row3.createCell(4).setCellValue("pajak");

            XSSFRow row = sheet.createRow(i + 5);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("description"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("no_bukti_bayar"));
            if (h.getFieldValueByFieldNameBD("excess_amount") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("excess_amount").doubleValue());
            }

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=biayapemasaran" + "_" + System.currentTimeMillis() + ".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public BiayaPemasaranView getPemasaranForPrintingMix(String rcid) throws Exception {

        final BiayaPemasaranView rcp = (BiayaPemasaranView) ListUtil.getDTOListFromQuery(
                " select pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                + " no_spp,ket,jumlah_data,total_biaya,ket,entry_date "
                + " from biaya_pemasaran where pms_id = ? "
                + " order by pms_id ",
                new Object[]{rcid},
                BiayaPemasaranView.class).getDTO();

        //rcp = getARReceipt(rcid);
        return rcp;
    }

    public void approvePmsNew(BiayaPemasaranView pms, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

//        if (pms.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pms.getStMonths() + " dan tahun " + pms.getStYears() + " tsb sudah diposting");
//        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

//        Date applyDate = new Date();
        Date applyDate = pms.getDtEntryDate();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

//        pms.setStStatus4("Y");
        pms.generateNoBukti();
        pms.markUpdate();
        S.store(pms);

        try {
            for (int i = 0; i < l.size(); i++) {
                BiayaPemasaranDetailView det = (BiayaPemasaranDetailView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                JournalView j3 = new JournalView();
                j3.markNew();

                JournalView j4 = new JournalView();
                j4.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                j.setStHeaderAccountNo(pms.getStAccountNo());
//                j.setLgAccountID(new Long(pms.getStAccountID()));
                j.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));
                j.setDbDebit(BDUtil.zero);
                j.setDbCredit(det.getDbNilai());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbEnteredCredit(det.getDbNilai());
//                j.setStDescription(getAccount(pms.getStAccountID()).getStDescription());
                j.setStDescription(getAccount(j.getLgAccountID().toString()).getStDescription());
                j.setStCurrencyCode(pms.getStCurrency());
                j.setDbCurrencyRate(pms.getDbCurr());
                j.setStRefTrxNo(pms.getStPemasaranID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(pms.getStNoBukti());
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(applyDate);
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("BIAYA_PMS");
                j.setDtApplyDate(applyDate);

                //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
//                    j2.setStTransactionNo(pms.getStNoBuktiBayar());
                    j2.setStTransactionNo(pms.getStNoBukti());
                    j2.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                    j2.setStHeaderAccountNo(pms.getStAccountNo());
                    j2.setLgAccountID(new Long(det.getStAccountID()));
                    j2.setDbDebit(det.getDbNilai());
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(det.getDbNilai());
                    j2.setDbEnteredCredit(BDUtil.zero);
                    j2.setStDescription(det.getStDescription());
                    j2.setStCurrencyCode(pms.getStCurrency());
                    j2.setDbCurrencyRate(pms.getDbCurr());
                    j2.setStRefTrxNo(pms.getStPemasaranID());
                    j2.setStIDRFlag("Y");
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("BIAYA_PMS");
                j2.setDtApplyDate(applyDate);

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

                if (!BDUtil.isZero(det.getDbExcAmount())) {
                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j3.setStTransactionHeaderID(stTransactionHeaderID);
//                        j3.setStTransactionNo(pms.getStNoBuktiBayar());
                        j3.setStTransactionNo(pms.getStNoBukti());
                        j3.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j3.setStHeaderAccountNo(pms.getStAccountNo());
//                        j3.setLgAccountID(new Long(pms.getStAccountID()));
                        j3.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));
                        j3.setDbDebit(det.getDbExcAmount());
                        j3.setDbCredit(BDUtil.zero);
                        j3.setDbEnteredDebit(det.getDbExcAmount());
                        j3.setDbEnteredCredit(BDUtil.zero);
//                j3.setStDescription(getAccount(pms.getStAccountID()).getStDescription());
                        j3.setStDescription(getAccount(j.getLgAccountID().toString()).getStDescription());
                        j3.setStCurrencyCode(pms.getStCurrency());
                        j3.setDbCurrencyRate(pms.getDbCurr());
                        j3.setStRefTrxNo(pms.getStPemasaranID());
                        j3.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j3.setLgPeriodNo(per.getLgPeriodNo());
                    j3.setLgFiscalYear(per.getLgFiscalYear());
                    j3.setStRefTrxType("BIAYA_PMS");
                    j3.setDtApplyDate(applyDate);

                    /*
                    logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                    logger.logDebug("################### "+j3.getLgHeaderAccountID());
                    logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                    logger.logDebug("################### "+j3.getLgAccountID());
                     */

                    if (j3.isModified()) {

                        final JournalView old = j3.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j3.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    }

                    S.store(j3);


                    if (j4.isNew()) {
                        j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j4.setStTransactionHeaderID(stTransactionHeaderID);
//                        j4.setStTransactionNo(pms.getStNoBuktiBayar());
                        j4.setStTransactionNo(pms.getStNoBukti());
                        j4.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j4.setStHeaderAccountNo(pms.getStAccountNo());
                        if (det.getStTaxType().equalsIgnoreCase("1")) {
                            j4.setLgAccountID(new Long(findAccount("441100000000 ", pms.getStCostCenterCode())));
                        } else {
                            j4.setLgAccountID(new Long(findAccount("442300000000 ", pms.getStCostCenterCode())));
                        }
                        j4.setDbDebit(BDUtil.zero);
                        j4.setDbCredit(det.getDbExcAmount());
                        j4.setDbEnteredDebit(BDUtil.zero);
                        j4.setDbEnteredCredit(det.getDbExcAmount());
                        j4.setStDescription(getAccount(j4.getLgAccountID().toString()).getStDescription());
                        j4.setStCurrencyCode(pms.getStCurrency());
                        j4.setDbCurrencyRate(pms.getDbCurr());
                        j4.setStRefTrxNo(pms.getStPemasaranID());
                        j4.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j4.setLgPeriodNo(per.getLgPeriodNo());
                    j4.setLgFiscalYear(per.getLgFiscalYear());
                    j4.setStRefTrxType("BIAYA_PMS");
                    j4.setDtApplyDate(applyDate);

                    if (j4.isModified()) {

                        final JournalView old = j4.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j4.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                        }
                    }

                    S.store(j4);

                }
            }
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

   public void approvePmsBayar(BiayaPemasaranView pms, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

//        if (pms.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pms.getStMonths() + " dan tahun " + pms.getStYears() + " tsb sudah diposting");
//        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

//        Date applyDate = new Date();
        Date applyDate = pms.getDtEntryDate();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        pms.generateNoBuktiBayar(applyDate);
        pms.setDtTanggalBayar(applyDate);
        pms.markUpdate();
        S.store(pms);

        try {
            for (int i = 0; i < l.size(); i++) {
                BiayaPemasaranDetailView det = (BiayaPemasaranDetailView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                JournalView j3 = new JournalView();
                j3.markNew();

                JournalView j4 = new JournalView();
                j4.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                j.setStHeaderAccountNo(pms.getStAccountNo());
                j.setLgAccountID(new Long(pms.getStAccountID()));

                /*j.setDbDebit(BDUtil.zero);
                j.setDbCredit(det.getDbNilai());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbEnteredCredit(det.getDbNilai());*/

                j.setDbAutoCredit2(det.getDbNilai());

                j.setStDescription(getAccount(pms.getStAccountID()).getStDescription());
                j.setStCurrencyCode(pms.getStCurrency());
                j.setDbCurrencyRate(pms.getDbCurr());
                j.setStRefTrxNo(pms.getStPemasaranID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(pms.getStNoBuktiBayar());
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(applyDate);
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("BIAYA_PMS");
                j.setDtApplyDate(applyDate);

                //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(pms.getStNoBuktiBayar());
                    j2.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                    j2.setStHeaderAccountNo(pms.getStAccountNo());
                    j2.setLgAccountID(new Long(det.getStAccountID()));

                    /*
                    j2.setDbDebit(det.getDbNilai());
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(det.getDbNilai());
                    j2.setDbEnteredCredit(BDUtil.zero);*/

                    j2.setDbAutoDebit2(det.getDbNilai());

                    j2.setStDescription(det.getStDescription());
                    j2.setStCurrencyCode(pms.getStCurrency());
                    j2.setDbCurrencyRate(pms.getDbCurr());
                    j2.setStRefTrxNo(pms.getStPemasaranID());
                    j2.setStIDRFlag("Y");
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("BIAYA_PMS");
                j2.setDtApplyDate(applyDate);

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

                if (!BDUtil.isZero(det.getDbExcAmount())) {
                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j3.setStTransactionHeaderID(stTransactionHeaderID);
                        j3.setStTransactionNo(pms.getStNoBuktiBayar());
                        j3.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j3.setStHeaderAccountNo(pms.getStAccountNo());
                        j3.setLgAccountID(new Long(pms.getStAccountID()));

                        /*
                        j3.setDbDebit(det.getDbExcAmount());
                        j3.setDbCredit(BDUtil.zero);
                        j3.setDbEnteredDebit(det.getDbExcAmount());
                        j3.setDbEnteredCredit(BDUtil.zero);*/

                        j3.setDbAutoDebit2(det.getDbExcAmount());

                        j3.setStDescription(getAccount(pms.getStAccountID()).getStDescription());
                        j3.setStCurrencyCode(pms.getStCurrency());
                        j3.setDbCurrencyRate(pms.getDbCurr());
                        j3.setStRefTrxNo(pms.getStPemasaranID());
                        j3.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j3.setLgPeriodNo(per.getLgPeriodNo());
                    j3.setLgFiscalYear(per.getLgFiscalYear());
                    j3.setStRefTrxType("BIAYA_PMS");
                    j3.setDtApplyDate(applyDate);

                    /*
                    logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                    logger.logDebug("################### "+j3.getLgHeaderAccountID());
                    logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                    logger.logDebug("################### "+j3.getLgAccountID());
                     */

                    if (j3.isModified()) {

                        final JournalView old = j3.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j3.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    }

                    S.store(j3);


                    if (j4.isNew()) {
                        j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j4.setStTransactionHeaderID(stTransactionHeaderID);
                        j4.setStTransactionNo(pms.getStNoBuktiBayar());
                        j4.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j4.setStHeaderAccountNo(pms.getStAccountNo());
                        if (det.getStTaxType().equalsIgnoreCase("1")) {
                            j4.setLgAccountID(new Long(findAccount("441100000000 ", pms.getStCostCenterCode())));
                        } else {
                            j4.setLgAccountID(new Long(findAccount("442300000000 ", pms.getStCostCenterCode())));
                        }

                        /*
                        j4.setDbDebit(BDUtil.zero);
                        j4.setDbCredit(det.getDbExcAmount());
                        j4.setDbEnteredDebit(BDUtil.zero);
                        j4.setDbEnteredCredit(det.getDbExcAmount());*/

                        j4.setDbAutoCredit2(det.getDbExcAmount());

                        j4.setStDescription(getAccount(j4.getLgAccountID().toString()).getStDescription());
                        j4.setStCurrencyCode(pms.getStCurrency());
                        j4.setDbCurrencyRate(pms.getDbCurr());
                        j4.setStRefTrxNo(pms.getStPemasaranID());
                        j4.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j4.setLgPeriodNo(per.getLgPeriodNo());
                    j4.setLgFiscalYear(per.getLgFiscalYear());
                    j4.setStRefTrxType("BIAYA_PMS");
                    j4.setDtApplyDate(applyDate);

                    if (j4.isModified()) {

                        final JournalView old = j4.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j4.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                        }
                    }

                    S.store(j4);

                }
            }
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

   public void approvePmsAcrual(BiayaPemasaranView pms, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

//        if (pms.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pms.getStMonths() + " dan tahun " + pms.getStYears() + " tsb sudah diposting");
//        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

//        Date applyDate = new Date();
        Date applyDate = pms.getDtEntryDate();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        pms.setStStatus4("Y");
        pms.generateNoBukti();
        pms.markUpdate();
        S.store(pms);

        try {
            for (int i = 0; i < l.size(); i++) {
                BiayaPemasaranDetailView det = (BiayaPemasaranDetailView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                JournalView j3 = new JournalView();
                j3.markNew();

                JournalView j4 = new JournalView();
                j4.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                j.setStHeaderAccountNo(pms.getStAccountNo());
                j.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));

                /*j.setDbDebit(BDUtil.zero);
                j.setDbCredit(det.getDbNilai());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbEnteredCredit(det.getDbNilai());*/

                j.setDbAutoCredit2(det.getDbNilai());

                j.setStDescription(det.getStDescription());
                j.setStCurrencyCode(pms.getStCurrency());
                j.setDbCurrencyRate(pms.getDbCurr());
                j.setStRefTrxNo(pms.getStPemasaranID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(pms.getStNoBukti());
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(applyDate);
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("BIAYA_PMS");
                j.setDtApplyDate(applyDate);

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(pms.getStNoBukti());
                    j2.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                    j2.setStHeaderAccountNo(pms.getStAccountNo());
                    j2.setLgAccountID(new Long(det.getStAccountID()));

                    /*
                    j2.setDbDebit(det.getDbNilai());
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(det.getDbNilai());
                    j2.setDbEnteredCredit(BDUtil.zero);*/

                    j2.setDbAutoDebit2(det.getDbNilai());

                    j2.setStDescription(det.getStDescription());
                    j2.setStCurrencyCode(pms.getStCurrency());
                    j2.setDbCurrencyRate(pms.getDbCurr());
                    j2.setStRefTrxNo(pms.getStPemasaranID());
                    j2.setStIDRFlag("Y");
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("BIAYA_PMS");
                j2.setDtApplyDate(applyDate);

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

                if (!BDUtil.isZero(det.getDbExcAmount())) {
                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j3.setStTransactionHeaderID(stTransactionHeaderID);
                        j3.setStTransactionNo(pms.getStNoBukti());
                        j3.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j3.setStHeaderAccountNo(pms.getStAccountNo());
                        j3.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));

                        /*
                        j3.setDbDebit(det.getDbExcAmount());
                        j3.setDbCredit(BDUtil.zero);
                        j3.setDbEnteredDebit(det.getDbExcAmount());
                        j3.setDbEnteredCredit(BDUtil.zero);*/

                        j3.setDbAutoDebit2(det.getDbExcAmount());

                        j3.setStDescription(det.getStDescription());
                        j3.setStCurrencyCode(pms.getStCurrency());
                        j3.setDbCurrencyRate(pms.getDbCurr());
                        j3.setStRefTrxNo(pms.getStPemasaranID());
                        j3.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j3.setLgPeriodNo(per.getLgPeriodNo());
                    j3.setLgFiscalYear(per.getLgFiscalYear());
                    j3.setStRefTrxType("BIAYA_PMS");
                    j3.setDtApplyDate(applyDate);

                    /*
                    logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                    logger.logDebug("################### "+j3.getLgHeaderAccountID());
                    logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                    logger.logDebug("################### "+j3.getLgAccountID());
                     */

                    if (j3.isModified()) {

                        final JournalView old = j3.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j3.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    }

                    S.store(j3);


                    if (j4.isNew()) {
                        j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j4.setStTransactionHeaderID(stTransactionHeaderID);
                        j4.setStTransactionNo(pms.getStNoBukti());
                        j4.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j4.setStHeaderAccountNo(pms.getStAccountNo());
                        if (det.getStTaxType().equalsIgnoreCase("1")) {
                            j4.setLgAccountID(new Long(findAccount("441100000000 ", pms.getStCostCenterCode())));
                        } else {
                            j4.setLgAccountID(new Long(findAccount("442300000000 ", pms.getStCostCenterCode())));
                        }
                        /*
                        j4.setDbDebit(BDUtil.zero);
                        j4.setDbCredit(det.getDbExcAmount());
                        j4.setDbEnteredDebit(BDUtil.zero);
                        j4.setDbEnteredCredit(det.getDbExcAmount());*/

                        j4.setDbAutoCredit2(det.getDbExcAmount());

                        j4.setStDescription(getAccount(j4.getLgAccountID().toString()).getStDescription());
                        j4.setStCurrencyCode(pms.getStCurrency());
                        j4.setDbCurrencyRate(pms.getDbCurr());
                        j4.setStRefTrxNo(pms.getStPemasaranID());
                        j4.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j4.setLgPeriodNo(per.getLgPeriodNo());
                    j4.setLgFiscalYear(per.getLgFiscalYear());
                    j4.setStRefTrxType("BIAYA_PMS");
                    j4.setDtApplyDate(applyDate);

                    if (j4.isModified()) {

                        final JournalView old = j4.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j4.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                        }
                    }

                    S.store(j4);

                }
            }
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void approvePmsAcrualBayar(BiayaPemasaranView pms, DTOList l) throws Exception {
        logger.logDebug("########## saveJournalEntry: " + l.size());

//        if (pms.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pms.getStMonths() + " dan tahun " + pms.getStYears() + " tsb sudah diposting");
//        }

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String stTransactionHeaderID = null;

        Date applyDate = new Date();

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        pms.generateNoBuktiBayar(applyDate);
        pms.markUpdate();
        S.store(pms);

        try {
            for (int i = 0; i < l.size(); i++) {
                BiayaPemasaranDetailView det = (BiayaPemasaranDetailView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                JournalView j3 = new JournalView();
                j3.markNew();

                JournalView j4 = new JournalView();
                j4.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                j.setStHeaderAccountNo(pms.getStAccountNo());
                j.setLgAccountID(new Long(pms.getStAccountID()));

                /*
                j.setDbDebit(BDUtil.zero);
                j.setDbCredit(det.getDbNilai());
                j.setDbEnteredDebit(BDUtil.zero);
                j.setDbEnteredCredit(det.getDbNilai());*/

                j.setDbAutoCredit2(det.getDbNilai());

                j.setStDescription(getAccount(pms.getStAccountID()).getStDescription());
                j.setStCurrencyCode(pms.getStCurrency());
                j.setDbCurrencyRate(pms.getDbCurr());
                j.setStRefTrxNo(pms.getStPemasaranID());
                j.setStIDRFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(pms.getStNoBuktiBayar());
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(applyDate);
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("BIAYA_PMS");
                j.setDtApplyDate(applyDate);

                //logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                //logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                //logger.logDebug("################### "+j.getLgAccountID());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(pms.getStNoBuktiBayar());
                    j2.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                    j2.setStHeaderAccountNo(pms.getStAccountNo());
                    j2.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));

                    /*
                    j2.setDbDebit(det.getDbNilai());
                    j2.setDbCredit(BDUtil.zero);
                    j2.setDbEnteredDebit(det.getDbNilai());
                    j2.setDbEnteredCredit(BDUtil.zero);*/

                    j2.setDbAutoDebit2(det.getDbNilai());

                    j2.setStDescription(det.getStDescription());
                    j2.setStCurrencyCode(pms.getStCurrency());
                    j2.setDbCurrencyRate(pms.getDbCurr());
                    j2.setStRefTrxNo(pms.getStPemasaranID());
                    j2.setStIDRFlag("Y");
                }

                if (applyDate == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("BIAYA_PMS");
                j2.setDtApplyDate(applyDate);

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }

                S.store(j2);

                if (!BDUtil.isZero(det.getDbExcAmount())) {
                    if (j3.isNew()) {
                        j3.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j3.setStTransactionHeaderID(stTransactionHeaderID);
                        j3.setStTransactionNo(pms.getStNoBuktiBayar());
                        j3.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j3.setStHeaderAccountNo(pms.getStAccountNo());
                        j3.setLgAccountID(new Long(findAccount("461000000000", pms.getStCostCenterCode())));

                        /*
                        j3.setDbDebit(BDUtil.zero);
                        j3.setDbCredit(det.getDbExcAmount());
                        j3.setDbEnteredDebit(BDUtil.zero);
                        j3.setDbEnteredCredit(det.getDbExcAmount());*/

                        j3.setDbAutoCredit2(det.getDbExcAmount());

                        j3.setStDescription(det.getStDescription());
                        j3.setStCurrencyCode(pms.getStCurrency());
                        j3.setDbCurrencyRate(pms.getDbCurr());
                        j3.setStRefTrxNo(pms.getStPemasaranID());
                        j3.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j3.setLgPeriodNo(per.getLgPeriodNo());
                    j3.setLgFiscalYear(per.getLgFiscalYear());
                    j3.setStRefTrxType("BIAYA_PMS");
                    j3.setDtApplyDate(applyDate);

                    /*
                    logger.logDebug("******************* "+new Long(depo.getStEntityID()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStEntityID());
                    logger.logDebug("################### "+j3.getLgHeaderAccountID());
                    logger.logDebug("******************* "+new Long(depo.getStNoRekeningDeposito()));
                    logger.logDebug("$$$$$$$$$$$$$$$$$$$ "+depo.getStNoRekeningDeposito());
                    logger.logDebug("################### "+j3.getLgAccountID());
                     */

                    if (j3.isModified()) {

                        final JournalView old = j3.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j3.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j3.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j3.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j3.getLgAccountID(), j3.getLgFiscalYear(), j3.getLgPeriodNo(), j3.getDbBalanceAmount());
                        }
                    }

                    S.store(j3);


                    if (j4.isNew()) {
                        j4.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                        j4.setStTransactionHeaderID(stTransactionHeaderID);
                        j4.setStTransactionNo(pms.getStNoBuktiBayar());
                        j4.setLgHeaderAccountID(new Long(pms.getStAccountID()));
                        j4.setStHeaderAccountNo(pms.getStAccountNo());
                        if (det.getStTaxType().equalsIgnoreCase("1")) {
                            j4.setLgAccountID(new Long(findAccount("441100000000 ", pms.getStCostCenterCode())));
                        } else {
                            j4.setLgAccountID(new Long(findAccount("442300000000 ", pms.getStCostCenterCode())));
                        }
                        /*
                        j4.setDbDebit(det.getDbExcAmount());
                        j4.setDbCredit(BDUtil.zero);
                        j4.setDbEnteredDebit(det.getDbExcAmount());
                        j4.setDbEnteredCredit(BDUtil.zero);*/

                        j4.setDbAutoDebit2(det.getDbExcAmount());

                        j4.setStDescription(getAccount(j4.getLgAccountID().toString()).getStDescription());
                        j4.setStCurrencyCode(pms.getStCurrency());
                        j4.setDbCurrencyRate(pms.getDbCurr());
                        j4.setStRefTrxNo(pms.getStPemasaranID());
                        j4.setStIDRFlag("Y");

                    }

                    if (applyDate == null) {
                        throw new Exception("Apply Date is not defined !");
                    }
                    // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + pms.getDtEntryDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j4.setLgPeriodNo(per.getLgPeriodNo());
                    j4.setLgFiscalYear(per.getLgFiscalYear());
                    j4.setStRefTrxType("BIAYA_PMS");
                    j4.setDtApplyDate(applyDate);

                    if (j4.isModified()) {

                        final JournalView old = j4.getOldJournal();

                        if (old != null) {

                            final boolean isSameRecord = Tools.compare(j4.getLgAccountID(), old.getLgAccountID()) == 0
                                    && Tools.compare(j4.getDtApplyDate(), old.getDtApplyDate()) == 0;

                            if (isSameRecord) {
                                final BigDecimal am = j4.getDbAdjustmentAmount();

                                if (am.compareTo(BDUtil.zero) != 0) {
                                    updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), am);
                                    //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                                }
                            } else {
                                updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                                updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                            }
                        } else {
                            updateBalance(j4.getLgAccountID(), j4.getLgFiscalYear(), j4.getLgPeriodNo(), j4.getDbBalanceAmount());
                        }
                    }

                    S.store(j4);

                }
            }
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
   
    public void reverseIzinBentuk(ARInvestmentIzinDepositoView izindeposito) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            DTOList deposito = izindeposito.getDeposito();
            for (int i = 0; i < deposito.size(); i++) {
                ARInvestmentDepositoIndexView adr = (ARInvestmentDepositoIndexView) deposito.get(i);

                if (adr.isEffective()) {
                    throw new Exception("Bilyet " + adr.getStNodefo() + " sudah disetujui");
                }
            }

            PreparedStatement PS1 = S.setQuery("update ar_inv_deposito set active_flag = 'N', active_cair_flag = 'N', deleted = 'Y' where ar_izin_id = ?");

            PS1.setObject(1, izindeposito.getStARIzinID());

            int p1 = PS1.executeUpdate();

            PreparedStatement PS = S.setQuery("update ar_izin_deposito set approvedcab_flag = 'N', approvedcab_who = null, approvedcab_date = null,"
                    + "approvedpus_flag = 'N', approvedpus_who = null, approvedpus_date = null where ar_izin_id = ?");

            PS.setObject(1, izindeposito.getStARIzinID());

            int p = PS.executeUpdate();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void reverseIzinBunga(ARInvestmentIzinBungaView izinbunga) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement PS2 = S.setQuery("delete from gl_je_detail where trx_no in ( "
                    + "select bukti_d from ar_inv_bunga where ar_izinbng_id = ? )");
            PS2.setObject(1, izinbunga.getStARIzinBngID());
            int p2 = PS2.executeUpdate();

            PreparedStatement PS1 = S.setQuery("delete from ar_inv_bunga where ar_izinbng_id = ?");
            PS1.setObject(1, izinbunga.getStARIzinBngID());
            int p1 = PS1.executeUpdate();

            PreparedStatement PS = S.setQuery("update ar_izin_bunga set approvedcab_flag = 'N', approvedcab_who = null, approvedcab_date = null,"
                    + "approvedpus_flag = 'N', approvedpus_who = null, approvedpus_date = null where ar_izinbng_id = ?");

            PS.setObject(1, izinbunga.getStARIzinBngID());

            int p = PS.executeUpdate();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public DTOList getJournalEntryByTrxNo(String trxNo) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      gl_je_detail a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_no = ? "
                + "   order by a.trx_id",
                new Object[]{trxNo},
                JournalView.class);
    }

    public void saveEditTitipanPremiReinsurance(TitipanPremiReinsuranceHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiReinsuranceView fj = (TitipanPremiReinsuranceView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String transNo = fj.getStAccountNo();

        boolean reversing = fj.getStReverseFlag() != null;

        BigDecimal bal = GLUtil.getBalanceTitipanPremi(l);

        if (!reversing) {
            if (Tools.compare(bal, BDUtil.zero) != 0) {
                throw new RuntimeException("Inbalanced jounal (difference = " + bal + ")\n " + l);
            }
        }

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String trxNo = null;
        if (header.isNew()) {
            trxNo = generateTitipanPremiReinsuranceNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiReinsuranceView j = (TitipanPremiReinsuranceView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    if (header.getStTransactionNo() != null) {
                        j.setStTransactionNo(header.getStTransactionNo());
                    }
                }

                if (j.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + j.getDtApplyDate() + "Salah)");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("TITIPAN_PREMI");

                if (j.isModified()) {

                    final TitipanPremiReinsuranceView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getStAccountID(), old.getStAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(Long.getLong(old.getStAccountID()), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(Long.getLong(j.getStAccountID()), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }
            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public String generateTitipanPremiReinsuranceNo(TitipanPremiReinsuranceHeaderView header, DTOList l, String transNo) throws Exception {

        //if (stReceiptNo!=null) return;
        String transactionNo = "";
        final String ccCode = Tools.getDigitRightJustified(header.getStCostCenter(), 2);
        final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(), 1);
        //String stBankCode = getPaymentMethod()==null?"0000":getPaymentMethod().getStBankCode();
        //final String bankCode = Tools.getDigitRightJustified(stBankCode,4);

        /*String counterKey =
        DateUtil.getYear2Digit(header.getDtCreateDate())+
        DateUtil.getMonth2Digit(header.getDtCreateDate());*/

        String counterKey = header.getStYears().substring(2) + header.getStMonths();

        //String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //String rn = String.valueOf(IDFactory.createNumericID("TPNO" + counterKey + ccCode,1));

        //rn = Tools.getDigitRightJustified(rn,3);
        rn = StringTools.leftPad(rn, '0', 5);

        String accountcode = "";
        if (header.getStAccountNoMaster() != null) {
            accountcode = header.getStAccountNoMaster().substring(5, 10);
        } else {
            accountcode = transNo.substring(5, 10);
        }

        //110002700400
        //012345678901
        //no
        //  A0901171000000
        //  01234567890123
        //    A0910202700002

        transactionNo =
                methodCode
                + counterKey
                + ccCode
                + ccCode
                + accountcode
                + rn;

        return transactionNo;

    }

    public void saveTitipanPremiReinsurance(TitipanPremiReinsuranceHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry titipan premi reasuransi : " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiReinsuranceView fj = (TitipanPremiReinsuranceView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String lgHeaderAccountID = header.getStAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("TPHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = header.getStTransactionNo();
        if (header.isNew()) {
            trxNo = generateTitipanPremiReinsuranceNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiReinsuranceView j = (TitipanPremiReinsuranceView) l.get(i);

                //TitipanPremiView j2 = (TitipanPremiView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setStHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);
                j.setStMonths(header.getStMonths());
                j.setStYears(header.getStYears());
                j.setStMethodCode(header.getStMethodCode());
                j.setStAccountIDMaster(header.getStAccountIDMaster());
                j.setStDescriptionMaster(header.getStDescriptionMaster());
                j.setStCostCenter(header.getStCostCenter());


                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("TPNEW")));

                    j.setStTransactionNo(trxNo);
                }

                //if (j.getDtApplyDate()==null) throw new Exception("Apply Date is not defined !");
                if (j.getDtApplyDate() != null) {
                    final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());
                }

                j.setStRefTrxType("TITIPAN_PREMI");
                j.setStIDRFlag("Y");

                TitipanPremiReinsuranceView viuw = (TitipanPremiReinsuranceView) l.get(i);
                //S.store(j);

            }

            S.store(l);

            if (header.isApproved()) {
                saveJournalEntryTitipanPremiReinsurance(header, l);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public void saveJournalEntryTitipanPremiReinsurance(TitipanPremiReinsuranceHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveJournalEntry: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiReinsuranceView fj = (TitipanPremiReinsuranceView) l.get(0);

        String stTransactionHeaderID = null;//fj.getStTransactionHeaderID();


        boolean reversing = fj.getStReverseFlag() != null;

        String lgHeaderAccountID = header.getStAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        //BigDecimal bal = GLUtil.getBalance(l);

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("GLHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = null;
        if (header.isNew()) {
            //trxNo = generateTransactionNo(header,l,transNo);
            //header.setStTransactionNo(trxNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiReinsuranceView titip = (TitipanPremiReinsuranceView) l.get(i);

                JournalView j = new JournalView();
                j.markNew();

                JournalView j2 = new JournalView();
                j2.markNew();

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j.setStHeaderAccountNo(titip.getStHeaderAccountNo());
                j.setStAccountID(titip.getStAccountID());

                j.setDbAutoCredit2(titip.getDbAmount());

                j.setStDescription(titip.getStDescription());
                j.setStJournalCode(titip.getStJournalCode());
                j.setStCurrencyCode(titip.getStCurrencyCode());
                j.setDbCurrencyRate(titip.getDbCurrencyRate());
                j.setStRefTrxNo(titip.getStTransactionID());
                j.setStIDRFlag("Y");
                j.setStSummaryFlag("Y");

                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j.setStTransactionNo(titip.getStTransactionNo());
                }

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(titip.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j.setLgPeriodNo(per.getLgPeriodNo());
                j.setLgFiscalYear(per.getLgFiscalYear());
                j.setStRefTrxType("TITIPAN_PREMI");
                j.setDtApplyDate(titip.getDtApplyDate());

                if (j.isModified()) {

                    final JournalView old = j.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j.getLgAccountID(), j.getLgFiscalYear(), j.getLgPeriodNo(), j.getDbBalanceAmount());
                    }
                }

                S.store(j);
                //simpen lawan

                BigDecimal debitOld = new BigDecimal(0);
                BigDecimal debitEnteredOld = new BigDecimal(0);


                if (j2.isNew()) {
                    j2.setStTransactionID(String.valueOf(IDFactory.createNumericID("GLENEW")));
                    j2.setStTransactionHeaderID(stTransactionHeaderID);
                    j2.setStTransactionNo(titip.getStTransactionNo());

                    j2.setStAccountID(header.getStAccountIDMaster());

                    j2.setDbAutoDebit2(titip.getDbAmount());

                    j2.setStDescription(titip.getStDescription());
                    j2.setStJournalCode(titip.getStJournalCode());
                    j2.setStCurrencyCode(titip.getStCurrencyCode());
                    j2.setDbCurrencyRate(titip.getDbCurrencyRate());
                    j2.setStRefTrxNo(titip.getStTransactionID());
                    j2.setStIDRFlag(titip.getStIDRFlag());
                    j2.setStIDRFlag("Y");

                }

                j2.setStSummaryFlag("Y");
                j2.setLgHeaderAccountID(new Long(lgHeaderAccountID));
                j2.setStHeaderAccountNo(titip.getStHeaderAccountNo());

                if (titip.getDtApplyDate() == null) {
                    throw new Exception("Apply Date is not defined !");
                }
                // final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                if (per == null) {
                    throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + titip.getDtApplyDate() + ")");
                }
                if (!per.isOpen()) {
                    throw new Exception("Period is not open");
                }
                j2.setLgPeriodNo(per.getLgPeriodNo());
                j2.setLgFiscalYear(per.getLgFiscalYear());
                j2.setStRefTrxType("TITIPAN_PREMI");
                j2.setDtApplyDate(titip.getDtApplyDate());
                j2.setStIDRFlag(titip.getStIDRFlag());
                //j2.setStSummaryFlag("Y");

                if (j2.isModified()) {

                    final JournalView old = j2.getOldJournal();

                    if (old != null) {

                        final boolean isSameRecord = Tools.compare(j2.getLgAccountID(), old.getLgAccountID()) == 0
                                && Tools.compare(j2.getDtApplyDate(), old.getDtApplyDate()) == 0;

                        if (isSameRecord) {
                            final BigDecimal am = j2.getDbAdjustmentAmount();

                            if (am.compareTo(BDUtil.zero) != 0) {
                                //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), am);
                                //GLBalanceManager.getInstance().updateBalance(j.getLgAccountID(), j.getLgPeriodNo(), am);
                            }
                        } else {
                            //updateBalance(old.getLgAccountID(), old.getLgFiscalYear(), old.getLgPeriodNo(), old.getDbBalanceAmount().negate());
                            //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                        }
                    } else {
                        //updateBalance(j2.getLgAccountID(), j2.getLgFiscalYear(), j2.getLgPeriodNo(), j2.getDbBalanceAmount());
                    }
                }
                S.store(j2);

            }
            //akhir simpen lawan



        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }

    public DTOList getTitipanPremiReinsurance(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      ar_titipan_premi_reinsurance a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id=? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                TitipanPremiReinsuranceView.class);
    }

    public void reverseTitipanReinsurance(String trx_no) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {

            PreparedStatement P = S.setQuery("update ar_titipan_premi_reinsurance set approved = null where trx_no = ?");

            P.setObject(1, trx_no);
            int r = P.executeUpdate();
            S.release();

            PreparedStatement P2 = S.setQuery("delete from gl_je_detail where trx_no = ?");

            P2.setObject(1, trx_no);
            int r2 = P2.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }


    }

    public String saveCair(ARInvestmentDepositoView deposito, boolean cairMode) throws Exception {

        if (!cairMode) {
            if (deposito.isPosted()) {
                throw new RuntimeException("Transaksi bulan " + deposito.getStMonths() + " dan tahun " + deposito.getStYears() + " tsb sudah diposting");
            }

            boolean checkPeriod = Tools.compare(deposito.getDtTglawal(), new Date()) >= 0;
            if (checkPeriod) {
                throw new RuntimeException("Tanggal Awal Harus Sama Dengan Bulan Berjalan");
            }
        }

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
                deposito.generateNoBuktiDeposito();
                deposito.generateRegisterBentuk();
                deposito.setStStatus("DEPOSITO");
                deposito.setStJournalStatus("NEW");
                deposito.setStKonter("0");
                deposito.setStActiveFlag("Y");
                deposito.setStEffectiveFlag("N");
                deposito.setStCreateWho(UserManager.getInstance().getUser().getStUserID());
                deposito.setDtCreateDate(new Date());

                deposito.setStAccountDepo(deposito.findAccountDepo(deposito.getStNoRekeningDeposito()));
                deposito.setStAccountBank(deposito.findAccountBank(deposito.getStEntityID()));
                BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());

            }

            BigDecimal bunga1 = deposito.reCalculateStart(deposito.getStKodedepo(), deposito.getStBulan(), deposito.getDbNominal());


            if (cairMode) {

                final DTOList cair = deposito.getPencairan();

                for (int i = 0; i < cair.size(); i++) {
                    ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);

                    if (pcr.isNew()) {
                        pcr.setStARCairID(String.valueOf(IDFactory.createNumericID("INVCAIR")));
                        pcr.setStCreateWho(deposito.getStCreateWho());
                        pcr.setDtCreateDate(deposito.getDtCreateDate());
                        pcr.setStActiveFlag("Y");
                        pcr.setStEffectiveFlag("N");
                    }

                    pcr.setStARDepoID(deposito.getStARDepoID());
                    pcr.setStNodefo(deposito.getStNodefo());
                    pcr.setStBuktiB(deposito.getStBuktiB());
                    pcr.setStHari(deposito.getStHari());
                    pcr.setStBulan(deposito.getStBulan());
                    pcr.setStCostCenterCode(deposito.getStCostCenterCode());
                    pcr.setStReceiptClassID(deposito.getStReceiptClassID());
                    pcr.setStCompanyType(deposito.getStCompanyType());
                    pcr.setStCurrency(deposito.getStCurrency());
                    pcr.setDbCurrencyRate(deposito.getDbCurrencyRate());
                    pcr.setDbPajak(deposito.getDbPajak());
                    pcr.setDbBunga(deposito.getDbBunga());
                    pcr.setDbNominal(deposito.getDbNominal());
                    pcr.setDbNominalKurs(deposito.getDbNominalKurs());
                    pcr.setDtTglawal(deposito.getDtTglawal());
                    pcr.setDtTglakhir(deposito.getDtTglakhir());
                    pcr.setDtTgldepo(deposito.getDtTgldepo());
                    pcr.setDtTglmuta(deposito.getDtTglmuta());
                    pcr.setStRegisterBentuk(deposito.getStRegister());
                    pcr.setStKonter(deposito.getStKonter());
                    pcr.setStKodedepo(deposito.getStKodedepo());
                    pcr.setStNoRekeningDeposito(deposito.getStNoRekeningDeposito());
                    pcr.setStDepoName(deposito.getStDepoName());
                    pcr.setStAccountDepo(deposito.getStAccountDepo());
                    pcr.setStNoRekening(deposito.getStNoRekening());
                    pcr.setStMonths(null);
                    pcr.setStYears(null);
                    pcr.setStActiveCairFlag("Y");
                }

                S.store2(cair);

            }

            S.store(deposito);

            return deposito.getStARDepoID();

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

    public DTOList getTitipanPremiExtracomptable(String trxhdrid) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "   select "
                + "      a.*, b.accountno"
                + "   from"
                + "      ar_titipan_premi_extracomptable a"
                + "         left join gl_accounts b on b.account_id = a.accountid"
                + "   where "
                + "      a.trx_hdr_id=? "
                + "   order by a.trx_id",
                new Object[]{trxhdrid},
                TitipanPremiExtracomptableView.class);
    }

    public void saveTitipanPremiExtracomptable(TitipanPremiExtracomptableHeaderView header, DTOList l) throws Exception {
        logger.logDebug(">>>>>>>>>>>>>>>> saveJournalEntry titipan premi : " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        final TitipanPremiExtracomptableView fj = (TitipanPremiExtracomptableView) l.get(0);

        String stTransactionHeaderID = fj.getStTransactionHeaderID();

        String lgHeaderAccountID = header.getStAccountIDMaster();
        String stHeaderAccountNo = header.getStAccountNoMaster();

        boolean reversing = fj.getStReverseFlag() != null;

        if (stTransactionHeaderID == null) {
            stTransactionHeaderID = String.valueOf(IDFactory.createNumericID("TPHNEW"));
        }

        String transNo = "";
        transNo = header.getStAccountNo();

        String trxNo = header.getStTransactionNo();
        if (header.isNew()) {
            //trxNo = generateTitipanPremiNo(header, l, transNo);
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                TitipanPremiExtracomptableView j = (TitipanPremiExtracomptableView) l.get(i);

                //TitipanPremiView j2 = (TitipanPremiView) l.get(i);

                j.setStTransactionHeaderID(stTransactionHeaderID);
                j.setStHeaderAccountID(lgHeaderAccountID);
                j.setStHeaderAccountNo(stHeaderAccountNo);
                j.setStMonths(header.getStMonths());
                j.setStYears(header.getStYears());
                j.setStMethodCode(header.getStMethodCode());
                j.setStAccountIDMaster(header.getStAccountIDMaster());
                j.setStDescriptionMaster(header.getStDescriptionMaster());
                j.setStCostCenter(header.getStCostCenter());


                j.reCalculate();

                if (j.isNew()) {
                    j.setStTransactionID(String.valueOf(IDFactory.createNumericID("TPNEW")));

                    j.setStTransactionNo(trxNo);
                }

                //if (j.getDtApplyDate()==null) throw new Exception("Apply Date is not defined !");
                if (j.getDtApplyDate() != null) {
                    final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(j.getDtApplyDate());
                    if (per == null) {
                        throw new Exception("Periods not being setup correctly ! (Unable to retrieve period for " + j.getDtApplyDate() + ")");
                    }
                    if (!per.isOpen()) {
                        throw new Exception("Period is not open");
                    }
                    j.setLgPeriodNo(per.getLgPeriodNo());
                    j.setLgFiscalYear(per.getLgFiscalYear());
                }

                j.setStRefTrxType("TITIPAN_PREMI");
                j.setStIDRFlag("Y");



            }

            S.store(l);

            if (header.isApproved()) {
                //saveJournalEntryTitipanPremi(header, l);
            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }

    }


    public void saveEditBilyet(ARInvestmentDepositoView deposito) throws Exception {

        String nobuk;
        String nobukObj;
        if (deposito.getStBuktiB() != null) {
            nobuk = "bukti_b";
            nobukObj = deposito.getStBuktiB();
        } else {
            nobuk = "nodefo";
            nobukObj = deposito.getStNodefo();
        }

        final SQLUtil S = new SQLUtil();

        try {
            if (deposito.isNew()) {
                deposito.setStARDepoID(String.valueOf(IDFactory.createNumericID("INVDEPO")));
            }

            deposito.setStAdminNotes("d/h " + deposito.getStNodefoOld() + ", " + deposito.getStNoRekeningOld());
            S.store(deposito);

            PreparedStatement PS = S.setQuery("update ar_inv_pencairan set nodefo = ?, norekening = ?, admin_notes = ? where " + nobuk + " = ? and koda = ? ");

            PS.setObject(1, deposito.getStNodefo());
            PS.setObject(2, deposito.getStNoRekening());
            PS.setObject(3, "d/h " + deposito.getStNodefoOld() + ", " + deposito.getStNoRekeningOld());
            PS.setObject(4, nobukObj);
            PS.setObject(5, deposito.getStCostCenterCode());

            int p = PS.executeUpdate();

            PreparedStatement PS2 = S.setQuery("update ar_inv_bunga set nodefo = ?, norekening = ?, admin_notes = ? where " + nobuk + " = ? and koda = ? ");

            PS2.setObject(1, deposito.getStNodefo());
            PS2.setObject(2, deposito.getStNoRekening());
            PS2.setObject(3, "d/h " + deposito.getStNodefoOld() + ", " + deposito.getStNoRekeningOld());
            PS2.setObject(4, nobukObj);
            PS2.setObject(5, deposito.getStCostCenterCode());

            int p2 = PS2.executeUpdate();

            PreparedStatement PS3 = S.setQuery("update ar_inv_perpanjangan set nodefo = ?, norekening = ?, admin_notes = ? where " + nobuk + " = ? and koda = ? ");

            PS3.setObject(1, deposito.getStNodefo());
            PS3.setObject(2, deposito.getStNoRekening());
            PS3.setObject(3, "d/h " + deposito.getStNodefoOld() + ", " + deposito.getStNoRekeningOld());
            PS3.setObject(4, nobukObj);
            PS3.setObject(5, deposito.getStCostCenterCode());

            int p3 = PS3.executeUpdate();
            logger.logDebug("@@@@@@@@@@@@@1 " + deposito.getStNodefoOld());
            logger.logDebug("@@@@@@@@@@@@@2 " + deposito.getStNoRekeningOld());
            logger.logDebug("@@@@@@@@@@@@@3 " + deposito.getStNodefo());
            logger.logDebug("@@@@@@@@@@@@@4 " + deposito.getStNoRekening());

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {

            S.release();

        }
    }

}
