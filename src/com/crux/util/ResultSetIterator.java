/***********************************************************************
 * Module:  com.crux.util.ResultSetIterator
 * Author:  Denny Mahendra
 * Created: Oct 19, 2004 1:22:26 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordBackup;
import com.crux.common.model.HashDTO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;

public class ResultSetIterator implements Iterator {
   private final static transient LogManager logger = LogManager.getInstance(ResultSetIterator.class);

   private ResultSet rs;
   private Class DTOClass;
   private int start;
   private int rownum;
   boolean bFirst = true;

   String[] stColumnName = null;
   DTOField[] mFields = null;
   int[] columnTypes = null;
   int[] columnPrec = null;
   int iColumnSize = 0;
   private ResultSetMetaData rsmd;
   private boolean enablePaging;
   private String stClassName;
   SQLUtil sqlUtil;
   int fetched = 0;
   private boolean lazymode;
   private DTO tempDTO;
   public int[] storeFlag;

   public boolean isEnablePaging() {
      return enablePaging;
   }

   public ResultSetIterator(ResultSet _rs, Class _DTOClass, int _start, int _rownum) throws Exception {
      this(_rs,_DTOClass, _start, _rownum, false);
   }

   public ResultSetIterator(ResultSet _rs, Class _DTOClass, int _start, int _rownum, boolean _lazymode) throws Exception {
      rs = _rs;
      DTOClass = _DTOClass;
      start = _start;
      rownum = _rownum;
      lazymode = _lazymode;

      if (lazymode) logger.logDebug("ResultSetIterator: lazy mode :)");

      rsmd = rs.getMetaData();

      stClassName = DTOClass.getName();

      enablePaging = (rownum>0);

      if (enablePaging && (start>0))
      {
         rs.first();
         rs.relative(start-1);
      }
   }

   public boolean hasNext() {
      try {
         if ((enablePaging) && (fetched>=rownum)) return false;
         //logger.logDebug("hasNext: next (fetched="+fetched+")");
         return (rs != null && rs.next());
      }
      catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public Object next() {

      if (HashDTO.class.isAssignableFrom(DTOClass)) return nextHashDTO();

      iterated = true;
      try {
         DTO R = getDTOInstance();

         //get column name
         if (bFirst) {
            iColumnSize = rsmd.getColumnCount();
            stColumnName = new String[iColumnSize];
            columnTypes = new int[iColumnSize];
            columnPrec = new int[iColumnSize];
            storeFlag = new int[iColumnSize];
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
         DTO old=null;

         final boolean useOld = (R instanceof RecordBackup);

         if(useOld) {
            old = getDTOInstance();
            old.startUpdate();
            R.setOld(old);
         }

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
               else if (storeFlag[i]==1) {
                  R.setAttribute(stColumnName[i],rs.getObject(i+1));
               } else {

                  try {
                     R.setFieldValueByFieldName(stColumnName[i],rs.getObject(i+1));
                  } catch (Exception e) {
                     storeFlag[i]=1;
                     R.setAttribute(stColumnName[i],rs.getObject(i+1));
                     if (i==0) {
                        logger.logError("next: "+e);
                     }
                  }
               }

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

            if(useOld)
               f.getSetter().invoke(old,p);
         }

         R.endUpdate();

         if (useOld)
            R.endUpdate();

         fetched++;

         return R;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private Object nextHashDTO() {
      iterated = true;
      try {
         DTO R = getDTOInstance();

         //get column name
         if (bFirst) {
            iColumnSize = rsmd.getColumnCount();
            stColumnName = new String[iColumnSize];
            columnTypes = new int[iColumnSize];
            columnPrec = new int[iColumnSize];

            for (int i = 1; i <= iColumnSize; i++) {
               stColumnName[i - 1] = rsmd.getColumnName(i).toLowerCase();
               final int ctype = rsmd.getColumnType(i);
               columnTypes[i - 1] = ctype;

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
         DTO old=null;

         final boolean useOld = (R instanceof RecordBackup);

         if(useOld) {
            old = getDTOInstance();
            old.startUpdate();
            R.setOld(old);
         }

         Object [] p = new Object [1];

         for (int i = 0; i < iColumnSize; i++) {

            Class t = null;

            switch (columnTypes[i]) {
               case Types.NUMERIC:
               case Types.BIGINT:
               case Types.REAL:
               case Types.INTEGER:
               case Types.TINYINT:
               case Types.DECIMAL:
               case Types.DOUBLE:
                  t=BigDecimal.class;break;
               case Types.CHAR:
               case Types.VARCHAR:
               case Types.CLOB:
                  t=String.class;break;
               case Types.DATE:
               case Types.TIME:
               case Types.TIMESTAMP:
                  t=Date.class;break;
               default:
                  throw new RuntimeException("Unsupported type : "+columnTypes[i]);
            }


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

            R.setFieldValueByFieldName(stColumnName[i],o);
         }

         R.endUpdate();

         if (useOld)
            R.endUpdate();

         fetched++;

         return R;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private DTO getDTOInstance() throws InstantiationException, IllegalAccessException {
      if (lazymode) {
         if (tempDTO==null) tempDTO = (DTO) DTOClass.newInstance();
         return tempDTO;
      }
      return (DTO) DTOClass.newInstance();
   }

   public void remove() {
      throw new RuntimeException("Not supported");
   }

   public void release() {
      try {
         if (rs!=null) {
            rs.close();
            rs=null;
         }
      }
      catch (SQLException e) {
         e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         logger.logError("Error while releasing : "+e.getMessage());
      }
   }

   boolean iterated = false;

   public ResultSetIterator newIterator() {
      if (iterated) throw new RuntimeException("This iterator can only be iterated once");
      iterated = true;
      return this;
   }
}
