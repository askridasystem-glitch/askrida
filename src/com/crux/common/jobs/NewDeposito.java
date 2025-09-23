/***********************************************************************
 * Module:  com.crux.common.jobs.NewDeposito
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.jobs.util.JobUtil;
import java.sql.Statement;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentPencairanView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class NewDeposito implements StatefulJob {

    private boolean approvalMode;
    private final static transient LogManager logger = LogManager.getInstance(NewDeposito.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                execute0();
                execute1();
                execute2();
                execute3();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute0() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses insert perpanjangan");

            long t = System.currentTimeMillis();

            Statement S = conn.createStatement();

            String proses = " INSERT into ar_inv_perpanjangan"
                    + "(ar_depo_id, nodefo, konter, bukti_b, norekdep, kodedepo, create_date,"
                    + " create_who, change_date, change_who, ccy, ccy_rate, nomkurs,"
                    + " nominal, tglawal, tglakhir, bulan, hari, bunga, pajak, koda,"
                    + " comp_type, kdbank, tgldepo, tglmuta, ket, register, nama_depo,"
                    + " nama_bank, effective_flag, rc_id, status, approved_who, approved_date,"
                    + " ar_parent_id, active_flag, account_depo, account_bank, norekening,"
                    + " tglcair, ar_cair_id, tglentrycair, journal_status, years, months,"
                    + " deleted, description) "
                    + " SELECT ar_depo_id, nodefo, konter, bukti_b, norekdep, kodedepo, create_date,"
                    + " create_who, change_date, change_who, ccy, ccy_rate, nomkurs,"
                    + " nominal, tglawal, tglakhir, bulan, hari, bunga, pajak, koda,"
                    + " comp_type, kdbank, tgldepo, tglmuta, ket, register, nama_depo,"
                    + " nama_bank, effective_flag, rc_id, status, approved_who, approved_date,"
                    + " ar_parent_id, active_flag, account_depo, account_bank, norekening,"
                    + " tglcair, ar_cair_id, tglentrycair, journal_status, years, months,"
                    + " deleted, description "
                    + " from ar_inv_deposito a "
                    + " where date_trunc('day',create_date) = '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-" + DateUtil.getDays(new Date()) + " 00:00:00' "
                    + " and journal_status = 'NEW' and status = 'DEPOSITO' "
                    + " and deleted is null and ar_cair_id is null and active_flag = 'Y' and effective_flag = 'Y' order by tgldepo ";

            //logger.logInfo("query sql ---> " + proses);

            S.executeUpdate(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 1 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }

    }

    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses pengecekan pencairan");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select * from ar_inv_pencairan "
                    + " where active_flag = 'Y' and effective_flag = 'Y' and bukti_c is not null "
                    + " and date_trunc('day',change_date) >= '2016-07-11 00:00:00' "
                    + " and date_trunc('day',change_date) <= '2016-07-20 00:00:00' ",
                    //+ " and date_trunc('day',change_date) = '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-" + DateUtil.getDays(new Date()) + " 00:00:00' ",
                    ARInvestmentPencairanView.class);

            for (int i = 0; i < file.size(); i++) {
                ARInvestmentPencairanView cair = (ARInvestmentPencairanView) file.get(i);

                updateCair(cair);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }

    }

    public void updateCair(ARInvestmentPencairanView cair) throws Exception {
        //logger.logDebug("updateBalance: ["+stInvoiceID+","+am+"]"); mark logger

        final SQLUtil S = new SQLUtil();

        try {

            String cek = "update ar_inv_perpanjangan set tglcair = ?, ar_cair_id = ?, tglentrycair = ? "
                    + " where nodefo = ? and norekdep = ? and nominal = ? ";

            if (cair.getStBuktiB() != null) {
                cek = cek + " and bukti_b = ? ";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setObject(1, cair.getDtTglCair());
            PS.setString(2, cair.getStARCairID());
            PS.setObject(3, cair.getDtChangeDate());
            PS.setString(4, cair.getStNodefo());
            PS.setString(5, cair.getStNoRekeningDeposito());
            PS.setBigDecimal(6, cair.getDbNominal());

            if (cair.getStBuktiB() != null) {
                PS.setString(7, cair.getStBuktiB());
            }

            int hasil = PS.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void execute2() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses pengecekan deposito");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select * from ar_inv_deposito "
                    + " where active_flag = 'Y' and effective_flag = 'Y' and admin_notes = 'REVERSE' "
                    + " and date_trunc('day',reverse_date) = '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-" + DateUtil.getDays(new Date()) + " 00:00:00' ",
                    ARInvestmentDepositoView.class);

            for (int i = 0; i < file.size(); i++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) file.get(i);

                updateReverse(depo);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }

    }

    public void updateReverse(ARInvestmentDepositoView depo) throws Exception {
        //logger.logDebug("updateBalance: ["+stInvoiceID+","+am+"]"); mark logger

        final SQLUtil S = new SQLUtil();

        try {

            String cek = "update ar_inv_perpanjangan set "
                    + "effective_flag = 'Y', "
                    + "approved_who = ?, "
                    + "approved_date = ?, "
                    + "norekdep = ?, "
                    + "kdbank = ?, "
                    + "nama_depo = ?, "
                    + "nama_bank = ?, "
                    + "account_depo = ?, "
                    + "account_bank = ?, "
                    + "nominal = ?, "
                    + "nomkurs = ?, "
                    + "where nodefo = ? ";

//            if (depo.getStBuktiB() != null) {
//                cek = cek + " and bukti_b = ? ";
//            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, depo.getStApprovedWho());
            PS.setObject(2, depo.getDtApprovedDate());
            PS.setString(3, depo.getStNoRekeningDeposito());
            PS.setString(4, depo.getStEntityID());
            PS.setString(5, depo.getStDepoName());
            PS.setString(6, depo.getStBankName());
            PS.setString(7, depo.getStAccountDepo());
            PS.setString(8, depo.getStAccountBank());
            PS.setBigDecimal(9, depo.getDbNominal());
            PS.setBigDecimal(10, depo.getDbNominalKurs());
            PS.setString(11, depo.getStNodefo());

//            if (depo.getStBuktiB() != null) {
//                PS.setString(6, depo.getStBuktiB());
//            }

            int hasil = PS.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void execute3() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses pengecekan deposito");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select * from ar_inv_pencairan "
                    + " where active_flag = 'Y' and effective_flag = 'Y' and admin_notes = 'REVERSE' "
                    + " and date_trunc('day',reverse_date) = '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-" + DateUtil.getDays(new Date()) + " 00:00:00' ",
                    ARInvestmentDepositoView.class);

            for (int i = 0; i < file.size(); i++) {
                ARInvestmentPencairanView cair = (ARInvestmentPencairanView) file.get(i);

                updateReverseCair(cair);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }

    }

    public void updateReverseCair(ARInvestmentPencairanView cair) throws Exception {
        //logger.logDebug("updateBalance: ["+stInvoiceID+","+am+"]"); mark logger

        final SQLUtil S = new SQLUtil();

        try {

            String cek = "update ar_inv_perpanjangan set "
                    + "tglcair = ?, "
                    + "ar_cair_id = ?, "
                    + "tglentrycair = ? "
                    + "where nodefo = ? ";

            if (cair.getStBuktiB() != null) {
                cek = cek + " and bukti_b = ? ";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setObject(1, cair.getDtTglCair());
            PS.setString(2, cair.getStARCairID());
            PS.setObject(3, new Date());
            PS.setString(4, cair.getStNodefo());

            if (cair.getStBuktiB() != null) {
                PS.setString(5, cair.getStBuktiB());
            }

            int hasil = PS.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }
}
