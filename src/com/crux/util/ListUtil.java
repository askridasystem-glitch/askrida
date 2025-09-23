/***********************************************************************
 * Module:  com.crux.util.ListUtil
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:24:40 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import com.crux.common.config.Config;
import com.crux.common.model.DTO;
import com.crux.common.model.Filter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class ListUtil {
   private final static transient LogManager logger = LogManager.getInstance(ListUtil.class);
   
   private static boolean hideLogger = false;

   public static SQLUtil selectDataSourceFromSQL(String stSQL) throws Exception {
      if (stSQL.indexOf("!") == 0) {
         //logger.logDebug("selectDataSourceFromSQL: selecting oracle data source");

         final int i = stSQL.indexOf('!',2);

         final SQLUtil S = new SQLUtil(stSQL.substring(1,i));

         S.stAttr1 = stSQL.substring(i+1, stSQL.length());

         return S;
      }

      final SQLUtil S2 = new SQLUtil();

      S2.stAttr1 = stSQL;

      return S2;
   }

   public static void main(String []argds) {
      String s="!RPT_DS!select * from dual";

      System.out.println(s.indexOf('!'));      //0
      System.out.println(s.indexOf('!',2));   //7
      System.out.println(s.substring(8));
      System.out.println(s.substring(1,7));
   }

   public static DTOList getDTOListFromQuery(String stSQL, Class DTOClass, Filter f) throws Exception {
      return getDTOListFromQuery(stSQL, new Object [] {}, DTOClass, f);
   }

   /*public static DTOList getDTOListFromQuery(String stSQL, Object [] params, Class DTOClass, Filter f) throws Exception {
      final SQLUtil S = selectDataSourceFromSQL(stSQL);
      stSQL = S.stAttr1;

      long t = System.currentTimeMillis();

      try {
         final PreparedStatement ps = S.setQuery(stSQL);

         final StringBuffer sz = new StringBuffer();

         sz.append("getDTOListFromQuery: params = [");

         if (params != null)
            for (int i = 0; i < params.length; i++) {
               S.setParam(i+1, params[i]);
               if (i>0) sz.append(',');
               sz.append(params[i]);
            }

         sz.append("]");

         logger.logDebug(sz.toString());

         final ResultSet RS = ps.executeQuery();

         DTOList l;

         if (f==null)
            l = getDTOListFromResultSet(RS,DTOClass,0,-1);
         else {
            l = getDTOListFromResultSet(RS,DTOClass,f.getStartRow(),f.rowPerPage);
            l.setCurrentPage(f.getCurrentPage());
         }

         t = System.currentTimeMillis() - t;

         if (t>100) {
            logger.log(LogManager.WARNING, "Probably High cost query, time = "+t+" ms");
            logger.log(LogManager.WARNING, "Query = "+stSQL);
         }

         return l;
      }
      finally {
         S.release();
      }
   }*/

   public static DTOList getDTOListFromQuery(String stSQL, Class DTOClass) throws Exception {
      return getDTOListFromQuery(stSQL, (ArrayList) null, DTOClass, null );
   }

   public static DTOList getDTOListFromQuery(String stSQL, Object params, Class DTOClass) throws Exception {
      return getDTOListFromQuery(stSQL, params, DTOClass, null);
   }

   public static Enumeration getParamEnumerator(final Object par) {

      if (par instanceof ArrayList) {

         final ArrayList ax = (ArrayList) par;

         final Enumeration e = new Enumeration() {
            Iterator it = ax.iterator();

            public boolean hasMoreElements() {
               return it.hasNext();
            }

            public Object nextElement() {
               return it.next();
            }
         };
         return e;
      }
      else if (par instanceof Object []) {

         final Enumeration e = new Enumeration() {

            Object [] o = (Object []) par;
            int pt=0;

            public boolean hasMoreElements() {
               return pt<o.length;
            }

            public Object nextElement() {
               return o[pt++];
            }
         };

         return e;
      }

      throw new IllegalArgumentException("Unknown parameter type");
   }

   public static DTOList getDTOListFromQuery(String stSQL, Object params, Class DTOClass, Filter f) throws Exception {
      return getDTOListFromQuery(stSQL, params, DTOClass, f, 0);
   }

   public static DTOList getDTOListFromQuery(String stSQL, Object params, Class DTOClass, Filter f, int flags) throws Exception {
      SQLUtil S = selectDataSourceFromSQL(stSQL);
      stSQL = S.stAttr1;

      final StringBuffer sz = new StringBuffer();

      long t = System.currentTimeMillis();

      try {
         final PreparedStatement ps = S.setQuery(stSQL);

         //ps.setFetchSize(100);

         if (params != null) {

            final Enumeration e = getParamEnumerator(params);

            int i=1;

            while (e.hasMoreElements()) {
               Object o = e.nextElement();

               if (i>1) sz.append(',');
               sz.append(o);

               S.setParam(i++, o);
            }
         }

         
         if(!hideLogger) logger.logDebug("getDTOListFromQuery: params = ["+sz.toString()+"]"); //tes mark logger

         final boolean isConservative = (flags & ListUtilMode.CONSERVATIVE) != 0;
         final boolean isSequential = (flags & ListUtilMode.SEQUENTIAL) != 0;

         //if (isConservative)


         final ResultSet RS = ps.executeQuery();

         t = System.currentTimeMillis() - t;

         if (t>100) {
            logger.logWarning("Probably High cost query, time = "+t+" ms - Query : "+stSQL);
         }

         DTOList l;

         if (isConservative) {

            if (!isSequential) throw new RuntimeException("Random access mode not yet supported");

            //logger.logDebug("getDTOListFromQuery: using conservative / sequential mode");  //tes mark logger

            final ResultSetIterator rsi =
                  (f==null)?
                  new ResultSetIterator(RS,DTOClass,0,-1, true) :
                  new ResultSetIterator(RS,DTOClass,f.getStartRow(),f.rowPerPage, true);

            l = new DTOList();
            l.setResultSetIterator(rsi);
            l.setSqlUtil(S);
            S=null;

            l.setFilter(f);

            return l;
         }

         if (f==null)
            l=getDTOListFromResultSet(RS,DTOClass,0,-1);
         else {
            l = getDTOListFromResultSet(RS,DTOClass,f.getStartRow(),f.rowPerPage);
            l.setCurrentPage(f.getCurrentPage());
         }

         l.setFilter(f);

         return l;
      }
      finally {
         if (S!=null)
            S.release();
      }
   }

   public static DTOList getDTOListFromResultSet(ResultSet rs, Class DTOClass, int start, int rownum) throws Exception {
      final ResultSetIterator rsit = new ResultSetIterator(rs, DTOClass, start, rownum);

      final boolean enablePaging = rsit.isEnablePaging();

      final DTOList l = new DTOList();

      while (rsit.hasNext()) {
         DTO dto = (DTO) rsit.next();
         l.add(dto);
      }

      if (enablePaging) {

         int tot = start+l.size();
         rs.last();
         tot = rs.getRow();
         l.setTotalRows(tot);

         if(!hideLogger) logger.logDebug("getDTOListFromResultSet: fetched "+l.size()+" of possibly "+tot+" "+DTOClass.getName()+" records");  //tes mark logger
      } else {
         if(!hideLogger) logger.logDebug("getDTOListFromResultSet: fetched "+l.size()+" "+DTOClass.getName()+" records"); //tes mark logger
      }

      return l;
   }

   public static DTOList getDTOListFromResultSet2(ResultSet rs, Class DTOClass, int start, int rownum) throws Exception {
      DTOList alResult = new DTOList();
      boolean bFirst = true;
      ResultSetMetaData rsmd = rs.getMetaData();
      String[] stColumnName = null;
      DTOField[] mFields = null;
      int[] columnTypes = null;
      int[] columnPrec = null;
      int iColumnSize = 0;

      final String stClassName = DTOClass.getName();

      final boolean enablePaging = (rownum>0);

      if (enablePaging && (start>0))
         rs.relative(start);

      while (rs != null && rs.next()) {

         DTO R = (DTO) DTOClass.newInstance();

         //get column name
         if (bFirst) {
            iColumnSize = rsmd.getColumnCount();
            stColumnName = new String[iColumnSize];
            columnTypes = new int[iColumnSize];
            columnPrec = new int[iColumnSize];
            mFields = new DTOField[iColumnSize];

            for (int i = 1; i <= iColumnSize; i++) {
               stColumnName[i - 1] = rsmd.getColumnName(i).toLowerCase();
               final int ctype = rsmd.getColumnType(i);
               columnTypes[i - 1] = ctype;

               mFields[i-1] = DTOCache.getInstance().getDesc(DTOClass, stColumnName[i-1]);

               if (ctype == Types.NUMERIC) {
                  if (rsmd.getPrecision(i) < 1) columnPrec[ i - 1 ] = 1; // force double when SELECT UNION because all numeric becomes 0.0
                  else columnPrec[ i - 1 ] = rsmd.getScale(i);
               }
            }
            bFirst = false;
         }

         R.startUpdate();

         //get data and insert it to hashtable

         Object o=null;

         Object [] p = new Object [1];

         for (int i = 0; i < iColumnSize; i++) {

            final DTOField f = mFields[i];

            if (f==null) {
                    if ("CREATE_DATE".equalsIgnoreCase(stColumnName[i]))
                  R.setDtCreateDate(rs.getTimestamp(i+1));
               else if ("CHANGE_DATE".equalsIgnoreCase(stColumnName[i]))
                  R.setDtChangeDate(rs.getTimestamp(i+1));
               else if ("CREATE_WHO".equalsIgnoreCase(stColumnName[i]))
                  R.setStCreateWho(rs.getString(i+1));
               else if ("CHANGE_WHO".equalsIgnoreCase(stColumnName[i]))
                  R.setStChangeWho(rs.getString(i+1));
               continue;
            }

            final Class t = f.getFieldType();

            if (t == String.class) {
               o = rs.getString(i+1);
            }
            else if (t == Integer.class) {
               if (rs.getObject(i+1) == null)
                  o = null;
               else
                  o = new Integer(rs.getInt(i+1));
            }
            else if (t == Long.class) {
               if (rs.getObject(i+1) == null)
                  o = null;
               else
                  o = new Long(rs.getLong(i+1));
            }
            else if (t == Date.class) {
               o = rs.getTimestamp(i+1);
            }
            else if (t == Double.class) {
               o = new Double(rs.getDouble(i+1));
            }
            else if (t == BigDecimal.class) {
               o = rs.getString(i+1);

               if ((o != null) && (!"".equals(o)))
                  o = rs.getBigDecimal(i+1);
               else
                  o = null;
            }
            else throw new IllegalArgumentException("Unsupported field type : "+t.getName());

            p[0]=o;

            f.getSetter().invoke(R, p);
         }

         R.endUpdate();

         alResult.add(R);

         if (enablePaging)
            if (alResult.size()>=rownum) break;

      }

      if (enablePaging) {

         int tot = start+alResult.size();

         /*if (rs.next()) {
            tot++;
            if (rs.relative(rownum)) tot+=rownum;
            if (rs.relative(rownum)) tot+=rownum;
         }*/

         rs.last();
         tot = rs.getRow();

         alResult.setTotalRows(tot);

         //alResult.setTotalDataCount(alResult.size());

         if(!hideLogger) logger.logDebug("getDTOListFromResultSet: fetched "+alResult.size()+" of possibly "+tot+" "+DTOClass.getName()+" records"); //tes mark logger
      } else {
         if(!hideLogger) logger.logDebug("getDTOListFromResultSet: fetched "+alResult.size()+" "+DTOClass.getName()+" records"); //tes mark logger
      }

      return alResult;
   }

   public static LookUpUtil getLookUpFromResultSet(ResultSet rs) throws Exception{
      final LookUpUtil lu = new LookUpUtil();

      int columnCount = rs.getMetaData().getColumnCount();

      while (rs.next()) {
         lu.add(rs.getString(1), rs.getString(2));

         if (columnCount>2)
            if ("Y".equalsIgnoreCase(rs.getString(3))) {
               lu.setLOValue(rs.getString(1));
            }
      }

      return lu;
   }

   public static LookUpUtil getLookUpFromQuery(String stSQL) throws Exception {
      return getLookUpFromQuery(stSQL, (ArrayList)null);
   }

   public static LookUpUtil getLookUpFromQuery(String stSQL, ArrayList params) throws Exception {
      final SQLUtil S = selectDataSourceFromSQL(stSQL);
      stSQL = S.stAttr1;

      
      if(!hideLogger) logger.logDebug("getLookUpFromQuery: params = "+params); //tes mark logger

      try {
         final PreparedStatement ps = S.setQuery(stSQL);

         if (params != null)
            for (int i = 0; i < params.size(); i++) {
               S.setParam(i+1, params.get(i));
            }

         final ResultSet RS = ps.executeQuery();

         return getLookUpFromResultSet(RS);
      }
      finally {
         S.release();
      }
   }

   public static LookUpUtil getLookUpFromQuery(String stSQL, Object [] params) throws Exception {
      final SQLUtil S = selectDataSourceFromSQL(stSQL);
      stSQL = S.stAttr1;

      if(!hideLogger) logger.logDebug("getLookUpFromQuery: params = "+params); //tes mark logger

      try {
         final PreparedStatement ps = S.setQuery(stSQL);

         if (params != null)
            for (int i = 0; i < params.length; i++) {
               S.setParam(i+1,params[i]);

            }

         final ResultSet RS = ps.executeQuery();

         return getLookUpFromResultSet(RS);
      }
      finally {
         S.release();
      }
   }

   public static String getOrderExpression(Filter f) {
      if ((f.orderKey != null) && (f.orderDir != 0)) {
         return " order by "+f.orderKey+" "+(f.orderDir>0?"ASC":"DESC");
      }
      return "";
   }

   public static String getOrderExpression(DTOList l) {
      if ((l.getOrderKey() != null) && (l.getOrderDir() != 0)) {
         return " order by "+l.getOrderKey()+" "+(l.getOrderDir()>0?"ASC":"DESC");
      }
      return "";
   }

   public static void appendAutoFilter(Filter p, StringBuffer szClause, ArrayList par) {
      if (p.afValue == null) return;

      if (szClause.length()>0) szClause.append(" and ");
 
      if (Filter.AFMODE_CONTAINS.equalsIgnoreCase(p.afMode)) {
         szClause.append("UPPER("+p.afField+")::TEXT");
         szClause.append(" LIKE ? ");
         par.add("%"+p.afValue.toUpperCase()+"%");
      }
      else if (Filter.AFMODE_EXACT.equalsIgnoreCase(p.afMode)) {
         szClause.append(p.afField);
         szClause.append(" = ? ");
         par.add(p.afValue);
      }
      else if (Filter.AFMODE_GREATER.equalsIgnoreCase(p.afMode)) {
         szClause.append(p.afField);
         szClause.append(" => ? ");
         par.add(p.afValue);
      }
      else if (Filter.AFMODE_LESS.equalsIgnoreCase(p.afMode)) {
         szClause.append(p.afField);
         szClause.append(" <= ? ");
         par.add(p.afValue);
      }
      else throw new RuntimeException("Invalid AF mode : "+p.afMode);
   }

   public static String appendAutoFilter(String stOperator, Filter p) {
      if (p.afValue == null) return "";

      if (Filter.AFMODE_CONTAINS.equalsIgnoreCase(p.afMode)) {
         return stOperator+" UPPER("+p.afField+")::TEXT LIKE '%"+escape(p.afValue.toUpperCase())+"%' ";
      }
      else if (Filter.AFMODE_EXACT.equalsIgnoreCase(p.afMode)) {
         return stOperator+p.afField+" = '"+escape(p.afValue)+"' ";
      }
      else if (Filter.AFMODE_GREATER.equalsIgnoreCase(p.afMode)) {
         return "";
      }
      else if (Filter.AFMODE_LESS.equalsIgnoreCase(p.afMode)) {
         return "";
      }
      else throw new RuntimeException("Invalid AF mode : "+p.afMode);
   }
   private static String escape(String val) {
      if (val.indexOf('\'')>=0) {
         final char[] ar = val.toCharArray();

         final StringBuffer sz = new StringBuffer();

         for (int i = 0; i < ar.length; i++) {
            char c = ar[i];

            switch (c) {
               case '\'':
                  sz.append('\\').append(c);break;
               default:
                  sz.append(c);
            }
         }

         return sz.toString();
      }

      return val;
   }

   public static DTOList getDTOListFromQueryDS(String stSQL, Object params, Class DTOClass,String DS) throws Exception {
      return getDTOListFromQueryDS(stSQL, params, DTOClass, null,DS);
   }

   public static DTOList getDTOListFromQueryDS(String stSQL, Object params, Class DTOClass, Filter f,String DS) throws Exception {
      return getDTOListFromQueryDS(stSQL, params, DTOClass, f, 0, DS);
   }

   public static SQLUtil selectDataSourceFromSQLDS(String stSQL,String DS) throws Exception {
      if (stSQL.indexOf("!") == 0) {
         //logger.logDebug("selectDataSourceFromSQL: selecting oracle data source");

         final int i = stSQL.indexOf('!',2);

         final SQLUtil S = new SQLUtil(stSQL.substring(1,i));

         S.stAttr1 = stSQL.substring(i+1, stSQL.length());

         return S;
      }

      final SQLUtil S2 = new SQLUtil(DS);

      S2.stAttr1 = stSQL;

      return S2;
   }

   public static DTOList getDTOListFromQueryDS(String stSQL, Object params, Class DTOClass, Filter f, int flags, String DS) throws Exception {
      SQLUtil S = selectDataSourceFromSQLDS(stSQL, DS);
      //SQLUtil S = new SQLUtil(DS);
      stSQL = S.stAttr1;

      final StringBuffer sz = new StringBuffer();

      long t = System.currentTimeMillis();

      try {
         final PreparedStatement ps = S.setQuery(stSQL);

         //ps.setFetchSize(100);

         if (params != null) {

            final Enumeration e = getParamEnumerator(params);

            int i=1;

            while (e.hasMoreElements()) {
               Object o = e.nextElement();

               if (i>1) sz.append(',');
               sz.append(o);

               S.setParam(i++, o);
            }
         }


         if(!hideLogger) logger.logDebug("getDTOListFromQuery: params = ["+sz.toString()+"]"); //tes mark logger

         final boolean isConservative = (flags & ListUtilMode.CONSERVATIVE) != 0;
         final boolean isSequential = (flags & ListUtilMode.SEQUENTIAL) != 0;

         //if (isConservative)


         final ResultSet RS = ps.executeQuery();

         t = System.currentTimeMillis() - t;

         if (t>100) {
            logger.logWarning("Probably High cost query, time = "+t+" ms - Query : "+stSQL);
         }

         DTOList l;

         if (isConservative) {

            if (!isSequential) throw new RuntimeException("Random access mode not yet supported");

            //logger.logDebug("getDTOListFromQuery: using conservative / sequential mode");  //tes mark logger

            final ResultSetIterator rsi =
                  (f==null)?
                  new ResultSetIterator(RS,DTOClass,0,-1, true) :
                  new ResultSetIterator(RS,DTOClass,f.getStartRow(),f.rowPerPage, true);

            l = new DTOList();
            l.setResultSetIterator(rsi);
            l.setSqlUtil(S);
            S=null;

            l.setFilter(f);

            return l;
         }

         if (f==null)
            l=getDTOListFromResultSet(RS,DTOClass,0,-1);
         else {
            l = getDTOListFromResultSet(RS,DTOClass,f.getStartRow(),f.rowPerPage);
            l.setCurrentPage(f.getCurrentPage());
         }

         l.setFilter(f);

         return l;
      }
      finally {
         if (S!=null)
            S.release();
      }
   }

   public static DTOList getDTOListFromQueryDS(String stSQL, Class DTOClass, String DS) throws Exception {
      return getDTOListFromQueryDS(stSQL, (ArrayList) null, DTOClass, null, DS );
   }

   public static LookUpUtil getLookUpFromQueryDS(String stSQL, String DS) throws Exception {
        final SQLUtil S = selectDataSourceFromSQLDS(stSQL, DS);
        stSQL = S.stAttr1;

        try {
            final PreparedStatement ps = S.setQuery(stSQL);

            final ResultSet RS = ps.executeQuery();

            return getLookUpFromResultSet(RS);
        } finally {
            S.release();
        }
    }

   public static LookUpUtil getLookUpFromQueryDS(String stSQL, Object[] params, String DS) throws Exception {
        final SQLUtil S = selectDataSourceFromSQLDS(stSQL, DS);
        stSQL = S.stAttr1;

        if (!hideLogger) {
            logger.logDebug("getLookUpFromQuery: params = " + params); //tes mark logger
        }
        try {
            final PreparedStatement ps = S.setQuery(stSQL);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    S.setParam(i + 1, params[i]);

                }
            }

            final ResultSet RS = ps.executeQuery();

            return getLookUpFromResultSet(RS);
        } finally {
            S.release();
        }

    }


}
