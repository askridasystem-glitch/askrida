/*
 * DeleteKonversiJob.java
 *
 * Created on October 27, 2011, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.crux.common.jobs;

import com.crux.file.FileView;
import com.crux.util.DTOList;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.ListUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author doni
 */
public class DeleteKonversiJob implements StatefulJob
{
    
    private final static transient LogManager logger = LogManager.getInstance(DeleteKonversiJob.class);
    
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        try
        {
            execute0();
            execute1();
            execute2();
            execute3();
            
        }
        catch (Exception e)
        {
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
    
    public void execute0() throws Exception
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
    
    public void execute1() throws Exception
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
                    " order by file_id "+
                    " LIMIT 500;";
            
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
    
    public void execute2() throws Exception
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
                //Process p = Runtime.getRuntime().exec("del /F "+files.getStFilePath());
                
            }

            t = System.currentTimeMillis() - t;
            
            logger.logInfo("proses 2 selesai dalam " + t + " ms");
            
        }
        finally
        {
            conn.close();
        }
    }
    
    public void execute3() throws Exception
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
        if (!f.exists()){
            logger.logInfo("Delete: no such file or directory: " + fileName);
            return;
        }
            
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
