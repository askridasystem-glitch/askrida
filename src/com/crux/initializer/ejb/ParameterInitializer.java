/***********************************************************************
 * Module:  com.crux.initializer.ejb.ParameterInitializer
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 2:02:05 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb;

import com.crux.util.SQLUtil;
import com.crux.util.LogManager;
import com.crux.configure.model.ParameterView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Date;

public class ParameterInitializer {
   public final static transient Object [] parameters = {
      new Object [] {"SYS","1","SYS_LOCK_PROTECT","1",new Boolean(true),"Concurrency Protection"},
      new Object [] {"SYS","2","SYS_FLOW_GUARD","1",new Boolean(true),"Flow Guard"},
      new Object [] {"SYS","3","SYS_SERVER_MODE","1",new Boolean(false),"Server is in development mode"},
      new Object [] {"SYS","4","SYS_MENU_AUTO_UPDATE","1",new Boolean(true),"Auto update menu upon deployment"},
      new Object [] {"SYS","5","SYS_HTTP_COMPRESSION","1",new Boolean(true),"Enable Http Compression"},
      new Object [] {"SYS","6","SYS_SAX_PARSER","1",new String("com.bluecast.xml.JAXPSAXParserFactory"),"SAX Parser"},
      new Object [] {"SYS","7","SYS_DEFAULT_LANG","1",new String("INA"),"Default Language"},
      new Object [] {"SYS","8","SYS_FILES_FOLDER","1",new String("/fin-repository"),"File storage folder"},

      new Object [] {"GL_ACCT_MASK","1","GL1_ACCT_MASK","1",new String("XXXXX XXX XX XXXXX"),"Account Code Mask"},
      new Object [] {"GL_ACCT_MASK","2","GL1_DEPT_ST","1",new Integer(7),"Dept Code Start pos"},
      new Object [] {"GL_ACCT_MASK","3","GL1_DEPT_END","1",new Integer(9),"Dept Code End pos"},
      new Object [] {"GL_ACCT_MASK","4","GL1_DEPT_RPT","1",new Boolean(false),"Departemental Reporting"},
      new Object [] {"GL_ACCT_MASK","5","GL1_CONS_ST","1",new Integer(0),"Consolidation Pos Start"},
      new Object [] {"GL_ACCT_MASK","6","GL1_CONS_END","1",new Integer(0),"Consolidation Pos End"},

      new Object [] {"GL_FISCAL_YR","1","GL2_CUR_YEAR","1",new Integer(2005),"Current Fiscal Year"},
      new Object [] {"GL_FISCAL_YR","2","GL2_CUR_PERIOD","1",new Integer(10),"Current Fiscal Period"},
      new Object [] {"GL_FISCAL_YR","3","GL2_LAST_PER","1",new Integer(1),"Last Period Closed"},
      new Object [] {"GL_FISCAL_YR","4","GL2_WAS_ALLOCATING","1",new Boolean(false),"Was Account Allocating Run"},
      new Object [] {"GL_FISCAL_YR","5","GL2_NUM_ACT_PER","1",new Integer(12),"Number of Accounting Periods"},
      new Object [] {"GL_FISCAL_YR","6","GL2_YEAR_END","1",new Date(2005,12,31),"Fiscal Year End Date"},
      new Object [] {"GL_FISCAL_YR","7","GL2_AUTOCALC","1",new Boolean(false),"Auto calculate on entry"},

      new Object [] {"AR_AP","1","COMISSION_AR_TRX","1","11","AR Trascation ID for commission payable"},
      new Object [] {"AR_AP","2","AP_COMM_ACRUAL","1","4300000000","AP Journal Accrual for commission"},
      new Object [] {"AR_AP","2","AR_AP_DEF_TAX_ENTITY","1","","Default Tax Entity"},

      new Object [] {"UWRIT","1","UWRIT_DEF_OTREATY","1",new String(""),"Default Outward Treaty"},
      new Object [] {"UWRIT","2","UWRIT_DEF_ITREATY","1",new String(""),"Default Inward Treaty"},
      new Object [] {"UWRIT","3","UWRIT_CURRENT_COMPANY","1",new String(""),"Holding Company"},
      new Object [] {"UWRIT","4","UWRIT_DEFAULT_INSTALLMENT","1",new String("1"),"Default Installment Period ID"},

      new Object [] {"GL_CCY","7","GL_CURRENCY","1",new String("IDR"),"Main Currency"},

      /*new Object [] {"INS_GENERAL","1","INS_COM_CLR_Y","1","","Kodasi BADHP tahun pertama"},
      new Object [] {"INS_GENERAL","2","INS_COM_CLR_YX","1","","Kodasi BADHP tahun lanjutan"},*/

      new Object [] {"GENERAL","1","GEN_SHOW_LOGO","1",new Boolean(true),"Show Logo"},

      new Object [] {"NOT_EMAIL","1","NOT_MAIL_SMTP_HOST","1",new String("unknown"),"SMTP Host"},
      new Object [] {"NOT_EMAIL","2","NOT_MAIL_SMTP_PORT","1",new Integer(25),"SMTP Port"},
      new Object [] {"NOT_EMAIL","3","NOT_MAIL_FROM","1",new String("shinta@telkomsel.co.id"),"Admin Address"},
   };

   private final static transient LogManager logger = LogManager.getInstance(ParameterInitializer.class);
   private String ds;

   public ParameterInitializer(String stDataSource) {
      ds = stDataSource;
   }

   public void doInitialize() throws Exception {
      logger.logDebug("doInitialize: starting ...");
      final SQLUtil S = new SQLUtil(ds);

      try {
         final PreparedStatement PS = S.setQuery("select param_id from s_parameter");

         final ResultSet RS = PS.executeQuery();

         HashSet x = new HashSet();

         while (RS.next()) {
            final String stParamID = RS.getString(1);
            x.add(stParamID);
         }

         S.reset();

         for (int i = 0; i < parameters.length; i++) {
            Object [] p = (Object []) parameters[i];

            if (x.contains(p[2])) continue;

            final ParameterView par = new ParameterView();

            par.setStParamGroup((String) p[0]);
            par.setLgParamSeq(new Long((String) p[3]));
            par.setStParamID((String) p[2]);
            par.setLgParamOrder(new Long((String) p[1]));

            final Object val = p[4];

            if (val instanceof String) {
               par.setStValueString((String)val);
               par.setStParamType("STRING");
            }
            else if (val instanceof Integer) {
               par.setLgValueNumber(new Long(((Integer)val).longValue()));
               par.setStParamType("INTEGER");
            }
            else if (val instanceof Long) {
               par.setLgValueNumber((Long)val);
               par.setStParamType("INTEGER");
            }
            else if (val instanceof BigDecimal) {
               par.setLgValueNumber(new Long(((BigDecimal) val).longValue()));
               par.setStParamType("INTEGER");
            }
            else if (val instanceof Date) {
               par.setDtValueDate((Date)val);
               par.setStParamType("DATE");
            }
            else if (val instanceof Boolean) {
               final long v = ((Boolean) val).booleanValue()?1:0;
               par.setLgValueNumber(new Long(v));
               par.setStParamType("BOOLEAN");
            }
            else
               throw new Exception("Unknown object type : "+val.getClass());

            par.setStParamDesc((String) p[5]);

            par.markNew();

            S.store(par);
         }
      }
      finally {
         S.release();
      }
   }


}