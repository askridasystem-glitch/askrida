/***********************************************************************
 * Module:  com.crux.common.jobs.RenewalDeposito
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.jobs.util.JobUtil;
import com.crux.lang.LanguageManager;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLUtil;
import com.webfin.FinCodec;
import com.webfin.gl.ejb.GeneralLedgerHome;
import java.sql.Statement;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.ListUtil;
import com.webfin.ar.model.ARInvestmentBungaView;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentPencairanView;
import java.sql.Connection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import com.webfin.gl.ejb.GeneralLedger;

public class RenewalDeposito implements StatefulJob {

    private boolean approvalMode;
    private final static transient LogManager logger = LogManager.getInstance(RenewalDeposito.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

             if(JobUtil.isServerProduction())
                    execute0();

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute0() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final SQLUtil Q = new SQLUtil();
            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select * "
                    + " from ar_inv_deposito "
                    + " where date_trunc('day',tglakhir) >= ? and date_trunc('day',tglakhir) <= ? and deleted is null "
                    + " and kodedepo = '2' and ar_cair_id is null and active_flag = 'Y' and effective_flag = 'Y' ",
                    new Object[]{DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00", new Date()},
                    ARInvestmentDepositoView.class);

            for (int i = 0; i < file.size(); i++) {
                ARInvestmentDepositoView depo = (ARInvestmentDepositoView) file.get(i);

                createRenewal(depo);
                depo.setStActiveFlag("N");
                depo.markUpdate();
                save(depo);

                final DTOList pencairan = depo.getPencairan();
                for (int j = 0; j < pencairan.size(); j++) {
                    ARInvestmentPencairanView cair = (ARInvestmentPencairanView) pencairan.get(j);

                    cair.setStActiveFlag("N");
                    cair.setStARParentID(cair.getStARCairID());
                    cair.markUpdate();

                }
                Q.store(pencairan);

                final DTOList bunga = depo.getBunga();
                for (int k = 0; k < bunga.size(); k++) {
                    ARInvestmentBungaView bnga = (ARInvestmentBungaView) bunga.get(k);

                    bnga.setStARParentID(bnga.getStARBungaID());
                    bnga.markUpdate();

                }
                Q.store(bunga);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }

    }

    private void save(ARInvestmentDepositoView depo) throws Exception {
        final SQLUtil Q = new SQLUtil();

        try {
            Q.store(depo);
        } finally {
            Q.release();
        }
    }

    public void createRenewal(ARInvestmentDepositoView depo) throws Exception {

        approvalMode = true;

        depo.setStNextStatus(FinCodec.Deposito.RENEWAL);
        depo.setStApprovedWho(depo.getStApprovedWho());

        getRemoteGeneralLedger().saveRenewal(depo, depo.getStNextStatus(), approvalMode);

    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }
}
