/***********************************************************************
 * Module:  com.crux.common.jobs.ProsesPolisRefund
 * Author:  Ahmad rodoni
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.webfin.FinCodec;
import com.webfin.insurance.ejb.*;
import com.webfin.insurance.form.PolicyForm;
import com.webfin.insurance.model.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.joda.time.DateTime;

public class ProsesKonversiEndorsemen extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ProsesKonversiEndorsemen.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                execute1();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: proses konversi endorse otomatis");

            long t = System.currentTimeMillis();

            createEndorsemenByNoRecap();

        } finally {
            conn.close();
        }
    }

    public void createEndorsemenByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listHeader = null;
        listHeader = ListUtil.getDTOListFromQuery(
                "select * "+
                " from ins_upload_header   "+
                " where effective_flag = 'Y' and posted_flag is null "+
                " order by ins_upload_id ",
                UploadEndorseHeaderView.class);

        for (int j = 0; j < listHeader.size(); j++) {
            UploadEndorseHeaderView header = (UploadEndorseHeaderView) listHeader.get(j);

            if(header.isPosted())
                throw new RuntimeException("Data sudah pernah diproses");

            //TANDAI SEDANG DI PROSES
            final SQLUtil S2 = new SQLUtil();
            PreparedStatement PS1 = S2.setQuery("update ins_upload_header set status = 'On process' where ins_upload_id = ?");

            PS1.setObject(1, header.getStInsuranceUploadID());

            int j1 = PS1.executeUpdate();

            if (j1!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
            S2.release();

            //PROSES DATA DETAIL ENDORSE
            DTOList listPolicy = null;
            listPolicy = ListUtil.getDTOListFromQuery(
                    "select pol_no "+
                    " from ins_upload_endorse_detail "+
                    " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                    " group by pol_no "+
                    " order by min(ins_upload_dtl_id)",
                    new Object[]{header.getStInsuranceUploadID()},
                    uploadEndorseDetailView.class);

            for (int i = 0; i < listPolicy.size(); i++) {
                    uploadEndorseDetailView pol2 = (uploadEndorseDetailView) listPolicy.get(i);

                    PolicyForm form = new PolicyForm();

                    DTOList listObject = null;
                    listObject = ListUtil.getDTOListFromQuery(
                    " SELECT * "+
                    " from ins_upload_endorse_detail "+
                    " where coalesce(status,'') <> 'Y' "+
                    " and pol_no = ? and ins_upload_id = ? "+
                    " ORDER BY order_no",
                    new Object[]{pol2.getStPolicyNo(), header.getStInsuranceUploadID()},
                    uploadEndorseDetailView.class);

                    //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                    DTOList listEndorsemen = null;
                    listEndorsemen = ListUtil.getDTOListFromQuery(
                    " SELECT * "+
                    " FROM INS_POLICY "+
                    " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                    " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,1,16) = ? "+
                    " ORDER BY POL_ID DESC LIMIT 1",
                    new Object[]{pol2.getStPolicyNo().substring(0, 16)},
                    InsurancePolicyView.class);

                    InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                    form.editCreateUploadEndorsePolis(polis.getStPolicyID(), listObject);

                    form.btnRecalculate();
                    form.getPolicy().recalculateTreaty();

                    form.btnSaveUpload();

                    PreparedStatement PS = S.setQuery("update ins_upload_endorse_detail set status = 'Y' where pol_no = ?");

                    PS.setObject(1,pol2.getStPolicyNo());

                    int k = PS.executeUpdate();

                    if (k!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");


            }


            //TANDAI sudah DI PROSES
            final SQLUtil S3 = new SQLUtil();
            PreparedStatement PS3 = S2.setQuery("update ins_upload_header set status = 'Finished' where ins_upload_id = ?");

            PS3.setObject(1, header.getStInsuranceUploadID());

            int j3 = PS3.executeUpdate();

            if (j3!=0) logger.logInfo("+++++++ UPDATE STATUS running : "+ header.getStRecapNo() +" ++++++++++++++++++");
            S3.release();

        }



        S.release();


    }

}
