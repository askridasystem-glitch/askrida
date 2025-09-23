/***********************************************************************
 * Module:  com.crux.common.jobs.PostgresVacuumJob
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.jobs;

import com.crux.file.FileView;
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

public class PostgresVacuumJob implements StatefulJob {
    private final static transient LogManager logger = LogManager.getInstance(PostgresVacuumJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            //execute();
            execute0_Konversi();
            execute1_Konversi();
            execute2_Konversi();
            execute3_Konversi();
            //execute4();
            //execute5();
            
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing vacuum analyze");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final boolean b = S.execute("vacuum analyze");

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("vacuum done in " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
 /*   --CARI DATA POLIS YANG BELUM DI INDEX, SIMPAN KE TEMP TABLE
select pol_id into temp proses_flag_kreasi
from ins_policy
where pol_type_id = 21
and status in ('POLICY','ENDORSE','RENEWAL')
and effective_flag = 'Y' and checking_flag is null limit 50;

--UPDATE DATA INDEX CHECKING DEBITUR
update ins_pol_obj
set checking = case when substr(ref1,1,1) in ('1','2','3','4','5','6','7','8','9')
then ltrim(replace(ref1, split_part(ref1, ' ', 1), ''))
else ref1 end || 
coalesce(refd1,refd2::timestamp without time zone - (ref2||' years')::interval)::date || refd2::date
where pol_id in
(
select pol_id
from proses_flag_kreasi
);

--UPDATE FLAG INS_POLICY
update ins_policy set checking_flag = 'Y'
where pol_id in
( select pol_id
 from proses_flag_kreasi
);

--HAPUS TABLE TEMP
drop table proses_flag_kreasi;

select 1;
*/
    public void execute0() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 0");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
            
            final String proses1 = "drop table if exists proses_flag_kreasi;";

            final boolean b = S.execute(proses1);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 0 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 1");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
            
            final String proses1 = "select pol_id into temp proses_flag_kreasi "+
                                    " from ins_policy "+
                                    " where pol_type_id = 21 "+
                                    " and status in ('POLICY','ENDORSE','RENEWAL') "+
                                    " and effective_flag = 'Y' and checking_flag is null limit 100;";

            final boolean b = S.execute(proses1);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 1 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute2() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 2 ");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
            
            final String proses = "insert into ins_pol_kreasi_obj select b.ins_pol_obj_id,b.pol_id,a.pol_no,b.ref1,b.ref2,b.refd1, "+
                                  "  b.refd2,b.refd3 "+
                                  "    from ins_policy a "+
                                  "    inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                                  "    where a.pol_id in "+
                                  "    (select pol_id "+
                                  "     from proses_flag_kreasi "+
                                  "    )";

            final boolean b = S.execute(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute3() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 3 ");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
            
            final String proses = "update ins_pol_kreasi_obj "+
                                  "  set checking = case when substr(ref1,1,1) in ('1','2','3','4','5','6','7','8','9') "+
                                  "    then ltrim(replace(ref1, split_part(ref1, ' ', 1), '')) "+
                                  "    else ref1 end ||  "+
                                  "    coalesce(refd1,refd2::timestamp without time zone - (ref2||' years')::interval)::date || refd2::date "+
                                  "    where checking is null "+
                                  "    and pol_id in( "+
                                  "    select pol_id "+
                                  "     from proses_flag_kreasi "+
                                  "    )";

            final boolean b = S.execute(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 3 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute4() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 4 ");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
           
            final String proses = "update ins_policy set checking_flag = 'Y' "+
                                  "  where pol_id in "+
                                  "    ( select pol_id "+
                                  "     from proses_flag_kreasi "+
                                  "    );";

            final boolean b = S.execute(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 4 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute5() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses 5 ");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();
            
            final String proses = "drop table proses_flag_kreasi; ";

            final boolean b = S.execute(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 5 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
    
    public void execute0_Konversi() throws Exception
    {
        final Connection conn = ConnectionCache.getInstance().getConnection();
        
        try
        {
            logger.logInfo("execute: performing proses delete konversi file 0");
            
            long t = System.currentTimeMillis();
            
            final Statement S = conn.createStatement();
            
            final String proses1 = "drop table if exists konversi_delete_temp;";
            
            final boolean b = S.execute(proses1);
            
            S.close();
            
            t = System.currentTimeMillis() - t;
            
            logger.logInfo("proses 0 selesai dalam " + t + " ms");
            
        }
        finally
        {
            conn.close();
        }
    }
    
    public void execute1_Konversi() throws Exception
    {
        final Connection conn = ConnectionCache.getInstance().getConnection();
        
        try
        {
            logger.logInfo("execute: performing proses delete konversi file 1");
            
            long t = System.currentTimeMillis();
            
            final Statement S = conn.createStatement();
            
            final String proses1 = "select * into konversi_delete_temp "+
                    " from s_files "+
                    " where description = 'KONVERSI' "+
                    " order by file_id desc "+
                    " LIMIT 10;";
            
            final boolean b = S.execute(proses1);
            
            S.close();
            
            t = System.currentTimeMillis() - t;
            
            logger.logInfo("proses 1 selesai dalam " + t + " ms");
            
        }
        finally
        {
            conn.close();
        }
    }
    
    public void execute2_Konversi() throws Exception
    {
        final Connection conn = ConnectionCache.getInstance().getConnection();
        
        try
        {
            logger.logInfo("execute: performing proses delete konversi file 2");
            
            long t = System.currentTimeMillis();
            
            final Statement S = conn.createStatement();
            
            final DTOList file = ListUtil.getDTOListFromQuery("select * from konversi_delete_temp", FileView.class);
            
            for (int i = 0; i < file.size(); i++)
            {
                FileView files = (FileView) file.get(i);
                
                deleteFile(files.getStFilePath());
                
            }

            t = System.currentTimeMillis() - t;
            
            logger.logInfo("proses 2 selesai dalam " + t + " ms");
            
        }
        finally
        {
            conn.close();
        }
    }
    
    public void execute3_Konversi() throws Exception
    {
        final Connection conn = ConnectionCache.getInstance().getConnection();
        
        try
        {
            logger.logInfo("execute: performing proses delete konversi file 3");
            
            long t = System.currentTimeMillis();
            
            final Statement S = conn.createStatement();
            
            final String proses1 = "delete from s_files" +
                    "   where file_id in" +
                    "(select file_id" +
                    " from konversi_delete_temp)" +
                    "and description = 'KONVERSI'; ";
            
            final boolean b = S.execute(proses1);
            
            S.close();
            
            t = System.currentTimeMillis() - t;
            
            logger.logInfo("proses 3 selesai dalam " + t + " ms");
            
        }
        finally
        {
            conn.close();
        }
    }
    
    public void deleteFile(String file)
    {
        String fileName = file;
        // A File object to represent the filename
        File f = new File(fileName);
        
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists())
            logger.logInfo("Delete: no such file or directory: " + fileName);
            //throw new IllegalArgumentException(
                    //"Delete: no such file or directory: " + fileName);
        
        if (!f.canWrite())
            throw new IllegalArgumentException("Delete: write protected: "
                    + fileName);
        
        // If it is a directory, make sure it is empty
        if (f.isDirectory())
        {
            String[] files = f.list();
            if (files.length > 0)
                throw new IllegalArgumentException(
                        "Delete: directory not empty: " + fileName);
        }
        
        // Attempt to delete it
        boolean success = f.delete();
        
        if(success)
            logger.logInfo("Delete: deletion success : "+ fileName);
            
        if (!success)
            logger.logInfo("Delete: deletion failed");
            //throw new IllegalArgumentException("Delete: deletion failed");
    }
    
}
