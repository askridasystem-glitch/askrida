/***********************************************************************
 * Module:  com.crux.common.jobs.PostgresVacuumJob
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.jobs;

import com.crux.file.FileView;
import com.crux.jobs.util.JobUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class PostgresKillIdleConnection implements StatefulJob {
    private final static transient LogManager logger = LogManager.getInstance(PostgresKillIdleConnection.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if(JobUtil.isServerProduction()){
                executeKill();
            }
                
            
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void executeKill() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logWarning("######################### execute: performing KILL IDLE CONN DB CORE");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final boolean b = S.execute(" SELECT pg_terminate_backend(pid) "+
                                        " FROM pg_stat_activity "+
                                        " WHERE datname = 'db_aba' "+
                                        " and pid <> pg_backend_pid() "+
                                        " and client_addr <> '192.168.200.202' "+
                                        " and state in ('idle', 'idle in transaction', 'idle in transaction (aborted)', 'disabledx')  "+
                                        " and state_change < current_timestamp - INTERVAL '30' MINUTE;");

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("########################## KILL IDLE idle connection DATABASE CORE done in " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
}
