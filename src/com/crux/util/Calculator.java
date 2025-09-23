/***********************************************************************
 * Module:  com.crux.util.Calculator
 * Author:  Denny Mahendra
 * Created: Nov 7, 2007 11:10:34 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.math.BigDecimal;
import java.util.HashMap;

public class Calculator extends HashMap {

   public BigDecimal add(String var, BigDecimal n) {

      BigDecimal v = BDUtil.add(n, (BigDecimal) get(var));

      put(var,v);

      return v;
   }

   public BigDecimal getBD(String var) {
      return (BigDecimal) get(var);
   }
}
