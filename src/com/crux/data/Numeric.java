/***********************************************************************
 * Module:  com.crux.data.Numeric
 * Author:  Denny Mahendra
 * Created: May 12, 2005 9:09:44 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Numeric extends BigDecimal{
   public Numeric(double val) {
      super(val);
   }

   public Numeric(String val) {
      super(val);
   }

   public Numeric(BigInteger val) {
      super(val);
   }

   public Numeric(BigInteger unscaledVal, int scale) {
      super(unscaledVal, scale);
   }

   public static Numeric valueOf(String value) {
      return (value == null)?null:new Numeric(value);
   }
}
