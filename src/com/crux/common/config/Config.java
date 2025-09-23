/**
 * Created by IntelliJ IDEA.
 * User: denny
 * Date: Apr 20, 2004
 * Time: 10:49:13 AM
 * To change this template use Options | File Templates.
 */
package com.crux.common.config;

import com.crux.common.parameter.Parameter;

import java.math.BigDecimal;


public class Config {
   public final static transient int dealerPriceLevel = 4;

   public final static transient boolean useDot = false; // replace with dot character
   public final static transient String dot = useDot?".":"";

   public final static transient String USER_FTP_SYNC_SUNTER1 = "ftp-sunter1";
   public final static transient String HO_MACHINE_ID = "001";

   public final static transient int ROW_PER_PAGE = 20;
   public static final String PARID_PARAMETER_ID = "SYNC_MACHINE_ID";
   public static final boolean JRE_1_4 = true;

   public static String getMachineID() {
      return Parameter.readString(PARID_PARAMETER_ID);
   }

   public static boolean isDevelopmentMode() {
      return Parameter.readBoolean("SYS_SERVER_MODE", false);
   }

   public static int getSchemaVersion() {
      final BigDecimal sv = Parameter.readNum("SCHEMA_VERSION",1);
      return sv==null?0:sv.intValue();
   }

   public static boolean isHO() {
      return HO_MACHINE_ID.equalsIgnoreCase(getMachineID());
   }
}
