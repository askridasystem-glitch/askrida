/***********************************************************************
 * Module:  com.webfin.gl.ejb.PeriodManager
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 10:57:30 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.crux.util.ListUtil;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.util.LogManager;
import com.crux.common.model.DTO;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.model.PeriodDetailView;

import java.util.Date;
import java.util.Iterator;

public class PeriodManager {
   private static PeriodManager staticinstance;
   private DTOList cache;

   private final static transient LogManager logger = LogManager.getInstance(PeriodManager.class);

   public static PeriodManager getInstance() throws Exception {
      if (staticinstance == null) staticinstance = new PeriodManager();
      return staticinstance;
   }

   private PeriodManager() throws Exception {
      load();
   }

   private void load() throws Exception {
      if (cache==null) {
         cache = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*,a.fiscal_year " +
                 "   from " +
                 "      gl_period a" +
                 "      inner join gl_period_det b on b.gl_period_id = a.gl_period_id",
                 PeriodDetailView.class
         );
         //logger.logDebug("load: "+cache);
      }

   }

   public PeriodDetailView getPeriodFromDate(Date dtApplyDate) throws Exception {
      load();

      //logger.logDebug("getPeriodFromDate: "+dtApplyDate);

      final Iterator it = cache.iterator();

      while (it.hasNext()) {
         PeriodDetailView pdd = (PeriodDetailView) it.next();

         //logger.logDebug("getPeriodFromDate: Tools.compare start date("+pdd.getDtStartDate()+", "+dtApplyDate+") : "+Tools.compare(pdd.getDtStartDate(), dtApplyDate));
         //logger.logDebug("getPeriodFromDate: Tools.compare end date("+pdd.getDtEndDate()+", "+dtApplyDate+") : "+Tools.compare(pdd.getDtEndDate(), dtApplyDate));

         if ((Tools.compare(pdd.getDtStartDate(), dtApplyDate)<=0) && (Tools.compare(pdd.getDtEndDate(), dtApplyDate)>=0)) return pdd;
      }

      return null;
   }

   public void clearCache() {
      cache = null;
   }

   public PeriodDetailView getPeriod(String periodTo, String year) throws Exception {

      return (PeriodDetailView) ListUtil.getDTOListFromQuery(

              "   select " +
              "      b.* " +
              "   from " +
              "      gl_period a " +
              "         inner join gl_period_det b on period_no=? and b.gl_period_id=a.gl_period_id" +
              "   where" +
              "      a.fiscal_year = ?",

              new Object [] {periodTo, year},

              PeriodDetailView.class
              
      ).getDTO();

   }

   public ClosingDetailView getClosing(String tahun,  String stCostCenter) throws Exception {

      return (ClosingDetailView) ListUtil.getDTOListFromQuery(

              "   select "+
              "     b.*  "+
              "  from  "+
              "     closing_period a  "+
              "        inner join closing_period_det b on b.closing_period_id=a.closing_period_id "+
              "  where "+
              "     cc_code = ? and fiscal_year = ? ",
              new Object [] {stCostCenter, tahun},
              ClosingDetailView.class

      ).getDTO();

   }

}
