package com.crux.jobs.model;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloWorld implements Job {

        public void execute(JobExecutionContext arg0) throws JobExecutionException {
              if(true) throw new RuntimeException("Tes Scheduling");
        }

}