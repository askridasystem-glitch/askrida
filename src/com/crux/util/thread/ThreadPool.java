/***********************************************************************
 * Module:  com.crux.util.thread.ThreadPool
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 2:27:38 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.thread;

import com.crux.common.exception.RTException;
import com.crux.util.LogManager;

import java.util.ArrayList;

public class ThreadPool {
   private final static transient LogManager logger = LogManager.getInstance(ThreadPool.class);

   private static ThreadPool staticinstance;
   private boolean rpt;

   public static ThreadPool getInstance() {
      if (staticinstance == null) staticinstance = new ThreadPool();
      return staticinstance;
   }

   private ThreadPool() {
      new Thread(
         new Runnable() {
            public void run() {
               while (true) {


                  cleanUp();

                  try {
                     Thread.sleep(2*1000);      // 60 seconds
                  } catch (InterruptedException e) {

                  }
               }
            }
         }
      ).start();
   }

   private ArrayList threads = new ArrayList();

   public int getSize() {
      if (threads==null) return 0;
      return threads.size();
   }

   public int getFree() {
      int f=0;

      for (int i = 0; i < threads.size(); i++) {
         ThreadBean tb = (ThreadBean) threads.get(i);
         if (!tb.isBusy()) f++;
      }

      return f;
   }

   public synchronized void run(Runnable x) {
      ThreadBean trd = getThread();

      logger.logDebug("run: thread = "+trd);

      //trd.tbRunnable.busy=false;

      trd.touch();

      trd.run(x);
   }

   private synchronized ThreadBean getThread() {

      try {
         for (int i = 0; i < threads.size(); i++) {
            ThreadBean trd = (ThreadBean) threads.get(i);

            if (!trd.isBusy()) {
               trd.tbRunnable.busy=true;
               return trd;
            }
         }

         final ThreadBean trd = new ThreadBean();

         threads.add(trd);

         trd.tbRunnable.busy=true;
         return trd;

      } finally {
         //cleanUp();
      }
   }

   public synchronized void cleanUp() {

      if (true) return;

      long lt = System.currentTimeMillis()-3*1000;// 3 secs idle


      rpt = false;

      do {
         for (int i = 0; i < threads.size(); i++) {
            ThreadBean threadBean = (ThreadBean) threads.get(i);

            if (threadBean.isBusy()) continue;

            if (threadBean.stamp<lt) {
               threadBean.exit();
               break;
            }
         }
      } while (rpt);
   }

   private static class TBRunnable implements Runnable {
      private Runnable runnable;
      public boolean busy = true;
      public boolean running = true;
      public boolean exit = false;

      public void setRunnable(Runnable runnable) {
         this.runnable = runnable;
      }

      public boolean isRunning() {
         return running;
      }

      public boolean isBusy() {
         return busy;
      }

      public void run() {
         while (true)
            try {
               //System.out.println("waiting ...");
               synchronized (this){
                  busy = false;
                  running = false;
                  this.wait();
               }

               if (exit) break;

               //System.out.println("running ...");
               running = true;
               busy = true;
               runnable.run();
               runnable=null;
            } catch (Exception e) {
               e.printStackTrace();
            }
      }
   }

   private static class ThreadBean {
      private TBRunnable tbRunnable;
      private Thread thread;
      private long stamp= System.currentTimeMillis();

      public boolean isBusy() {
         return tbRunnable.busy;
      }

      public Thread getThread() {
         return thread;
      }

      public ThreadBean() {
         System.out.println("creating thread");
         tbRunnable = new TBRunnable();
         thread = new Thread(tbRunnable);
         thread.start();
      }

      public void run(Runnable x) {
         try {
            tbRunnable.setRunnable(x);
            while (tbRunnable.isRunning()) Thread.sleep(100);
            synchronized (tbRunnable) {
               tbRunnable.notify();
            }
         } catch (Exception e) {
            throw new RTException(e);
         }
      }

      public void touch() {
         stamp = System.currentTimeMillis();
      }

      public void exit() {
         logger.logDebug("exit: Exiting");
         tbRunnable.busy=true;
         tbRunnable.exit=true;
         synchronized (tbRunnable) {
            tbRunnable.notify();
         }
      }
   }

   public static void main(String [] args) throws Exception {
      ThreadPool.getInstance().run(
              new Runnable() {
                 public void run() {
                    try {
                       while (true) {
                       System.out.println("tpool: size:"+ThreadPool.getInstance().getSize()+" free:"+ThreadPool.getInstance().getFree());
                       Thread.sleep(500);
                       }
                    } catch (InterruptedException e) {
                       throw new RTException(e);
                    }
                 }
              }
      );

      Thread.sleep(2000);

      ThreadPool.getInstance().run(
              new Runnable() {
                 public void run() {
                    try {
                       Thread.sleep(3000);
                    } catch (InterruptedException e) {
                       throw new RTException(e);
                    }
                 }
              }
      );

      Thread.sleep(4000);

      ThreadPool.getInstance().run(
              new Runnable() {
                 public void run() {
                    try {
                       Thread.sleep((long)(Math.random()*5000));
                    } catch (InterruptedException e) {
                       throw new RTException(e);
                    }
                 }
              }
      );
   }
}


