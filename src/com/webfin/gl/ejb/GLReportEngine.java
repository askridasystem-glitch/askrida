/***********************************************************************
 * Module:  com.webfin.gl.ejb.GLReportEngine
 * Author:  Denny Mahendra
 * Created: Dec 23, 2005 11:15:52 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.crux.util.*;
import com.crux.common.parameter.Parameter;
import com.webfin.gl.model.*;
import com.webfin.gl.util.GLUtil;
import com.webfin.FinCodec;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public class GLReportEngine {
   public GLReportParam param;
   public GLReportView reportDef;

   private final static transient LogManager logger = LogManager.getInstance(GLReportEngine.class);
   public DTOList charts;
   public GLChartMaster glChartMaster;
   private String actMax;
   private String actMin;
   private Long maxyear;
   private Long minyear;
   private HashMap balanceMap;
   private int MAX_COLUMN = 15;

   private boolean useNewBalance = true;
   private boolean enableSummarize = true;

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
              .create();
   }

   public GLReportEngine(GLReportParam _param) throws Exception {
      param = _param;

      reportDef = getRemoteGeneralLedger().getReportDefinition(param.stReportID);

      glChartMaster = new GLChartMaster();

      findActRange();

      if (useNewBalance)
         loadBalanceMap2();
      else
         loadBalanceMap();

      expandLines();
   }

   private void expandLines() throws Exception {

      for (long y = minyear.longValue(); y <= maxyear.longValue(); y++) {

         final DTOList balances = (DTOList) balanceMap.get(String.valueOf(y));
      }

      final DTOList lines = reportDef.getLines();

      final DTOList charts = glChartMaster.charts;

      final DTOList newLines = new DTOList();

      final HashSet acSet = new HashSet();
      final ArrayList accounts = new ArrayList();

      {
         final Iterator it = balanceMap.values().iterator();

         while (it.hasNext()) {
            DTOList bals = (DTOList) it.next();
            for (int i = 0; i < bals.size(); i++) {
               GLBalanceView gbl = (GLBalanceView) bals.get(i);
               if (!acSet.contains(gbl.getStAccountNo())) {
                  acSet.add(gbl.getStAccountNo());
                  accounts.add(new String[]{gbl.getStAccountNo(), gbl.getStDescription()});
               }
            }
         }
      }


      for (int i = 0; i < lines.size(); i++) {
         GLReportLineView line = (GLReportLineView) lines.get(i);

         boolean summarized = line.isSummarized();

         if (!enableSummarize)
            summarized = false;

         if (!summarized) {
            newLines.add(line);
            continue;
         }

         int j;

         for (j=i+1;j<lines.size();j++) {
            GLReportLineView linej = (GLReportLineView) lines.get(j);
            if (!linej.isSummarized()) {
               throw new RuntimeException("Fatal Error, Line "+linej.getLgLineNo()+" should be summarized");
            }

            if (linej.isCR()) {
               break;
            }
         }

         /*final String acFrom = GLUtil.Chart.getInstance().getChartCodeOnly(line.getStAccountFrom());
         final String acTo = GLUtil.Chart.getInstance().getChartCodeOnly(line.getStAccountTo());*/

         final String acFrom = line.getStAccountFrom();
         final String acTo = line.getStAccountTo();

         int matched = 0;

         /*if (acFrom!=null && acTo!=null) {
            for (int j = 0; j < charts.size(); j++) {
               GLChartView ch = (GLChartView) charts.get(j);

               final String ac = ch.getStAccountChartCode();

               if (
                       (Tools.compare(acFrom,ac)<=0) &&
                       (Tools.compare(acTo,ac)>=0)
               ) {
                  line = (GLReportLineView) line.clone();
                  line.setStAccountFrom(ac);
                  line.setStAccountTo(ac);

                  line.setStDescription(ch.getStDescription());

                  newLines.add(line);

                  matched++;
               }
            }
         }*/

         if (acFrom != null && acTo != null) {

            final Iterator it = accounts.iterator();

            while (it.hasNext()) {
               String[] acx = (String[]) it.next();

               final String ac = acx[0];

               if (
                       (Tools.compare(acFrom, ac) <= 0) &&
                       (Tools.compare(acTo, ac) >= 0)
               ) {
                  for (int k=i;k<=j;k++) {
                     line = (GLReportLineView) ((GLReportLineView) lines.get(k)).clone();
                     line.setStAccountFrom(ac);
                     line.setStAccountTo(ac);

                     line.setStDescription(acx[1]);

                     if (line.getStDescription()==null) line.setStDescription(ac);

                     //final GLChartView chart = (GLChartView)glChartMaster.getGLChartOfAccountNo(ac);
                     //if (chart!=null)
                     //   line.setStDescription(chart.getStDescription());


                     newLines.add(line);

                     matched++;
                  }

               }
            }
         }

         i=j;

         if (matched == 0) newLines.add(line);
      }

      reportDef.setLines(newLines);
   }

   public GLReportView fillReportData() throws Exception {

      final ArrayList output = new ArrayList();

      final DTOList lines = reportDef.getLines();

      final DTOList columns = reportDef.getColumns();

      final SmartList currentLine = new SmartList();

      final SmartList balBuffer = new SmartList();

      final HashMap totalMap = new HashMap();
      final HashMap varMap = new HashMap();

      final FormulaEvaluator fe = new FormulaEvaluator(new FormulaEvaluator.VariableConnector() {
         public Object getVariable(String name) {
            if (varMap.containsKey(name)) return varMap.get(name);
            if (totalMap.containsKey(name)) return totalMap.get(name);
            return BDUtil.zero;
         }
      });

      for (int i = 0; i < lines.size(); i++) {
         GLReportLineView line = (GLReportLineView) lines.get(i);

         logger.logDebug("fillReportData: processing line " + i + " of " + lines.size() + " (" + (100 * i / lines.size()) + "%)");

         final long colNo = line.getLgColumnNo().longValue();

         final boolean zeroColumn = colNo == 0;

         final long colStart = zeroColumn ? 0 : (colNo - 1);
         final long colStop = zeroColumn ? (columns.size() - 1) : colNo - 1;

         final String lineType = line.getStLineType();

         final boolean ltAccount = "ACCT".equalsIgnoreCase(lineType);
         final boolean ltDesc = "DESC".equalsIgnoreCase(lineType);
         final boolean ltCalc = "VCAL".equalsIgnoreCase(lineType);
         final boolean ltTotal = lineType.indexOf("TOT") == 0;

         final boolean negate = Tools.isYes(line.getStNegateFlag());
         final boolean print = Tools.isYes(line.getStPrintFlag());

         balBuffer.clear();

         if (ltTotal) {
            final int totWhich = Integer.parseInt(lineType.substring(3, lineType.length()));
            for (int l = (int) colStart; l <= colStop; l++) {
               final String totIndex = "TOT" + totWhich + '.' + l;
               final BigDecimal b = (BigDecimal) totalMap.get(totIndex);
               if (print)
                  currentLine.set(l, b);
               totalMap.remove(totIndex);
            }
         }

         if (ltCalc) {
            final Object value = fe.evaluate(line.getStFormula());

            if (print)
               for (int l = (int) colStart; l <= colStop; l++) {
                  balBuffer.set(l, value);
                  if (print)
                     currentLine.set(l, value);
               }
         }

         if (ltDesc) {
            if (print)
               for (int l = (int) colStart; l <= colStop; l++) {
                  currentLine.set(l, line.getStDescription());
               }
         }

         if (ltAccount) {

            for (long y = minyear.longValue(); y <= maxyear.longValue(); y++) {

               final DTOList balances = (DTOList) balanceMap.get(String.valueOf(y));

               if (useNewBalance) {
                  for (int k = 0; k < balances.size(); k++) {

                     GLBalanceView bal = (GLBalanceView) balances.get(k);

                     final String act = bal.getStAccountNo();

                     if (line.getStAccountFrom() != null)
                        if (Tools.compare(act, line.getStAccountFrom()) < 0) continue;

                     if (line.getStAccountTo() != null)
                        if (Tools.compare(act, line.getStAccountTo()) > 0) continue;

                     //final BigDecimal cbx = bal.getDbBalance();
                     //BigDecimal cb = negate ? BDUtil.negate(cbx) : cbx;

                     for (int l = (int) colStart; l <= colStop; l++) {
                        GLReportColumnView colx = (GLReportColumnView) columns.get(l);

                        BigDecimal[] val = null;

                        if (colx.isValCR()) val = bal.getCredit();
                        else if (colx.isValDB()) val = bal.getDebit();
                        else if (colx.isValBalance()) val = bal.getBalance();
                        else
                           throw new RuntimeException("Cannot determine required balance type");

                        if (colx.isBalance()) {
                           for(int j=0;j<=colx.getLgPeriod().longValue();j++) {
                              balBuffer.set(l, BDUtil.add(val[j], (BigDecimal) balBuffer.get(l)));
                           }
                        } else if (colx.isSummary()) {
                           balBuffer.set(l, BDUtil.add(val[colx.getLgPeriod().intValue()], (BigDecimal) balBuffer.get(l)));
                        }
                     }
                  }
               } else {
                  for (int k = 0; k < balances.size(); k++) {

                     GLBalanceView bal = (GLBalanceView) balances.get(k);

                     final String act = bal.getStAccountNo();

                     if (line.getStAccountFrom() != null)
                        if (Tools.compare(act, line.getStAccountFrom()) < 0) continue;

                     if (line.getStAccountTo() != null)
                        if (Tools.compare(act, line.getStAccountTo()) > 0) continue;

                     final BigDecimal cbx = bal.getDbBalance();
                     BigDecimal cb = negate ? BDUtil.negate(cbx) : cbx;

                     for (int l = (int) colStart; l <= colStop; l++) {
                        GLReportColumnView colx = (GLReportColumnView) columns.get(l);

                        if (colx.isBalance()) {
                           if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) >= 0) {
                              balBuffer.set(l, BDUtil.add(cb, (BigDecimal) balBuffer.get(l)));
                           }
                        } else if (colx.isSummary()) {
                           if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) == 0) {
                              balBuffer.set(l, BDUtil.add(cb, (BigDecimal) balBuffer.get(l)));
                           }
                        }
                     }
                  }
               }

            }

            for (int l = (int) colStart; l <= colStop; l++) {
               final BigDecimal bal = (BigDecimal) balBuffer.get(l);

               if (print)
                  currentLine.set(l, bal);

               for (int ti = 0; ti < 5; ti++) {
                  final String totKey = "TOT" + ti + '.' + l;

                  totalMap.put(totKey,
                          BDUtil.add(bal, (BigDecimal) totalMap.get(totKey)));
               }
            }
         }

         if (line.getStVariable() != null) {
            for (int l = (int) colStart; l <= colStop; l++) {
               varMap.put(line.getStVariable(), balBuffer.get(l));
            }
         }

         if (Tools.isYes(line.getStPrintCRFlag())) {
            final Object[] cl = currentLine.toArray();
            logger.logDebug("fillReportData: line = " + currentLine.toString());
            output.add(cl);

            currentLine.clear();

         }

      }

      reportDef.setResult(output);

      return reportDef;
   }

   public GLReportView fillReportData2() throws Exception {

      /*for (int i = 0; i < columns.size(); i++) {
         GLReportColumnView col = (GLReportColumnView) columns.get(i);

         final DTOList balances = (DTOList) balanceMap.get(col.getLgYear().toString());

         final int periodIdx = i+1;*/

      HashMap totalMap = new HashMap();

      ArrayList outputLines = new ArrayList();


      final DTOList lines = reportDef.getLines();

      final DTOList columns = reportDef.getColumns();

      {
         Object[] currentOutputLine = null;

         for (long y = minyear.longValue(); y <= maxyear.longValue(); y++) {

            final DTOList balances = (DTOList) balanceMap.get(String.valueOf(y));

            for (int k = 0; k < balances.size(); k++) {

               GLBalanceView bal = (GLBalanceView) balances.get(k);

               //logger.logDebug("bal:[ac:"+bal.getStAccountNo()+",per:"+bal.getLgPeriondNo()+",year:"+bal.getLgPeriodYear()+",bal:"+bal.getDbBalance()+"]");

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

                  if (lastline != null)
                     if (Tools.isYes(lastline.getStPrintCRFlag())) {

                        /*for (int i = 0; i < currentOutputLine.length; i++) {
                        Object o = currentOutputLine[i];

                        if (o instanceof BigDecimal) {
                        final BigDecimal cb = (BigDecimal) o;

                        for (int m=1;m<=5;m++) {
                        final String totKey = "TOT/"+m+"/"+(i-1);
                        totalMap.put(totKey, BDUtil.add((BigDecimal)totalMap.get(totKey),cb));
                        }
                        }
                        }*/
                        currentOutputLine = null;
                     }

                  lastline = line;

                  if (currentOutputLine == null) {
                     outputLineNo++;
                     if (outputLines.size() < outputLineNo) {
                        currentOutputLine = new Object[MAX_COLUMN];
                        outputLines.add(currentOutputLine);
                     }
                     currentOutputLine = (Object[]) outputLines.get(outputLineNo - 1);
                  }

                  final boolean negate = Tools.isYes(line.getStNegateFlag());

                  BigDecimal cb = negate ? BDUtil.negate(cbx) : cbx;

                  final long colNo = line.getLgColumnNo() == null ? 0 : (line.getLgColumnNo().longValue());

                  if (colNo < 0) throw new RuntimeException("Invalid column number");

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

                        /*} else if (lineType.indexOf("TOT")==0) {
                           final int m = lineType.charAt(3)-'0';
                           final String totKey = "TOT/"+m+"/"+(l);
                           currentOutputLine[l+1] = totalMap.get(totKey);
                           totalMap.remove(totKey);*/

                     } else if (lineType.indexOf("TOT") == 0) {
                        currentOutputLine[l + 1] = "***" + lineType;
                     } else if (lineType.indexOf("DESC") == 0) {
                        currentOutputLine[l + 1] = line.getStDescription();
                     } else if (lineType.indexOf("ACCT") == 0) {

                        if (line.getStAccountFrom() != null)
                           if (Tools.compare(act, line.getStAccountFrom()) < 0) continue;
                        if (line.getStAccountTo() != null)
                           if (Tools.compare(act, line.getStAccountTo()) > 0) continue;

                        if (colx.isBalance()) {
                           if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) >= 0) {
                              //logger.logDebug("BAL: "+cb+" to line "+line.getLgLineNo()+" per "+colx.getLgColumnNumber());
                              currentOutputLine[l + 1] = BDUtil.add((BigDecimal) currentOutputLine[l + 1], cb);
//                           line.getColumns()[l+1] = BDUtil.add((BigDecimal)line.getColumns()[l+1], cb);
                           }
                        } else if (colx.isSummary()) {
                           if (Tools.compare(colx.getLgPeriod(), bal.getLgPeriondNo()) == 0) {
                              //logger.logDebug("SUM: "+cb+" to line "+line.getLgLineNo()+" per "+colx.getLgColumnNumber());
                              currentOutputLine[l + 1] = BDUtil.add((BigDecimal) currentOutputLine[l + 1], cb);
//                           line.getColumns()[l+1] = BDUtil.add((BigDecimal)line.getColumns()[l+1], cb);
                           }
                        }

                     }
                  }
               }
            }

         }
      }


      {
         for (int i = 0; i < MAX_COLUMN; i++) {
            totalMap.clear();

            for (int j = 0; j < outputLines.size(); j++) {
               Object[] line = (Object[]) outputLines.get(j);

               int p = i;


               if (line[p] instanceof BigDecimal) {
                  final BigDecimal bal = (BigDecimal) line[p];
                  for (int k = 0; k < 9; k++) {
                     final String totKey = "TOT/" + i + "/" + k;

                     final BigDecimal curtot = (BigDecimal) totalMap.get(totKey);
                     totalMap.put(totKey, BDUtil.add(bal, curtot));
                  }
               }

               if (line[p] instanceof String) {
                  final String s = (String) line[p];

                  if (s.indexOf("***") == 0) {
                     final int k = Integer.parseInt(s.substring(6, s.length()));
                     final String totKey = "TOT/" + i + "/" + k;
                     final BigDecimal curtot = (BigDecimal) totalMap.get(totKey);
                     line[p] = curtot;
                     totalMap.remove(totKey);
                  }
               }
            }
         }
      }

      reportDef.setResult(outputLines);

      return reportDef;
   }

   private void findActRange() {
      final DTOList lines = reportDef.getLines();

      for (int i = 0; i < lines.size(); i++) {
         GLReportLineView gll = (GLReportLineView) lines.get(i);

         actMax = (String) Tools.max(gll.getStAccountTo(), actMax);
         actMin = (String) Tools.min(gll.getStAccountFrom(), actMin);
      }

      logger.logDebug("fillReportData: actrange : [" + actMin + "," + actMax + "]");
      final long cyear = getCurrentYear();
      long lastyear = cyear - 1;
      final long cper = getCurrentPeriod();

      final Long cYear = new Long(cyear);

      final DTOList columns = reportDef.getColumns();

      for (int i = 0; i < lines.size(); i++) {
         GLReportLineView line = (GLReportLineView) lines.get(i);

         line.setColumns(new BigDecimal[15]);
      }

      Long permax = null;
      Long permin = null;

      for (int i = 0; i < columns.size(); i++) {
         GLReportColumnView col = (GLReportColumnView) columns.get(i);

         if (col.getLgPeriod() == null) col.setLgPeriod(new Long(cper));

         if (Tools.compare(col.getLgPeriod(), LongUtil.zero) <= 0) col.setLgPeriod(LongUtil.add(col.getLgPeriod(), new Long(cper)));

         if (col.getLgPeriodTo() == null) col.setLgPeriodTo(col.getLgPeriod());

         if (Tools.compare(col.getLgPeriodTo(), LongUtil.zero) <= 0) col.setLgPeriodTo(LongUtil.add(col.getLgPeriodTo(), new Long(cper)));

         permax = (Long) Tools.max(col.getLgPeriod(), permax);
         permin = (Long) Tools.min(col.getLgPeriod(), permin);

         permax = (Long) Tools.max(col.getLgPeriodTo(), permax);
         permin = (Long) Tools.min(col.getLgPeriodTo(), permin);

         //col.setStColumnHeader(col.getLgPeriod().toString());

         //logger.logDebug("fillReportData: col:"+col.getLgColumnNumber()+" from:"+col.getLgPeriod()+" to:"+col.getLgPeriodTo());

         if (Tools.compare(col.getLgYear(), LongUtil.zero) <= 0) col.setLgYear(LongUtil.add(col.getLgYear(), cYear));

         maxyear = (Long) Tools.max(maxyear, col.getLgYear());
         minyear = (Long) Tools.min(minyear, col.getLgYear());

      }

//      logger.logDebug("fillReportData: permin="+permin+" permax="+permax);

      logger.logDebug("fillReportData: year min:" + minyear + " max:" + maxyear);

   }

   private void loadBalanceMap() {
      balanceMap = new HashMap() {
         public Object get(Object key) {
            Object o = super.get(key);
            try {

               if (o == null) {
                  final long year = Long.valueOf((String) key).longValue();
                  //final long period = LongUtil.getLong(col.getLgPeriodFrom());

                  final DTOList balances = ListUtil.getDTOListFromQuery("   select " +
                          "      a.bal,a.period_no,b.accountno,b.description " +
                          "   from " +
                          "      gl_acct_bal a" +
                          "         inner join gl_accounts b on b.account_id=a.account_id" +
                          "   where " +
                          "      a.period_year = ?" +
                          "      and b.accountno>=? and b.accountno<=?" +
                          "   order by a.account_id, a.period_no",
                          new Object[]{
                             new Long(year),
                             actMin, actMax
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
   }


   private void loadBalanceMap2() {
      balanceMap = new HashMap() {
         public Object get(Object key) {
            Object o = super.get(key);
            try {

               if (o == null) {
                  final long year = Long.valueOf((String) key).longValue();
                  //final long period = LongUtil.getLong(col.getLgPeriodFrom());

                  final DTOList balances = ListUtil.getDTOListFromQuery(
                          "   select " +
                          "      a.*,b.accountno,b.description " +
                          "   from " +
                          "      gl_acct_bal2 a" +
                          "         inner join gl_accounts b on b.account_id=a.account_id" +
                          "   where " +
                          "      a.period_year = ?" +
                          "      and b.accountno>=? and b.accountno<=?" +
                          "   order by a.account_id",
                          new Object[]{
                             new Long(year),
                             actMin, actMax
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
   }

   private long getCurrentPeriod() {
      return Parameter.readNum("GL2_CUR_PERIOD").longValue();
   }

   private long getCurrentYear() {
      return Parameter.readNum("GL2_CUR_YEAR").longValue();
   }
   ;
}
