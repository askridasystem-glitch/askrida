/***********************************************************************
 * Module:  com.crux.util.IDPrefixBuffer
 * Author:  Denny Mahendra
 * Created: May 28, 2004 9:45:32 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;



//000000000011111111112222222
   //012345678901234567890123456
   //INV.001.NO.04.01.01.H.00001

   //000000000011111111112222222
   //012345678901234567890123456
   //INV001NO040101H00001

public class IDPrefixBuffer {
   private StringBuffer sz = new StringBuffer();

   public static String getBrandSegment(String stID) {
      if (stID.length()==27) {
         return stID.substring(8,10);
      }
      else if (stID.length()==20) {
         return stID.substring(6,8);
      } else throw new IllegalArgumentException("Unrecognized ID length : "+stID);
   }

   public static String getTypeSegment(String stID) {
      if (stID.length()==27) {
         return stID.substring(20,21);
      }
      else if (stID.length()==20) {
         return stID.substring(14,15);
      } else throw new IllegalArgumentException("Unrecognized ID length : "+stID);
   }

   public static String getStBrandPrefixFromItemCode(String stItemCode) {
      if (stItemCode != null) {
         return StringTools.clip(StringTools.rightPad(stItemCode, '0', 2), 2);
      }
      return "00";
   }

   public static String getStTypePrefixFromItemCode(String stItemCode) {
      if (stItemCode != null) {
         final String bs = stItemCode.substring(2,3);
         return bs;
      }
      return "0";
   }

   public static String getStBranchIDFromID(String stID) {
      if (stID.length()==20) {
         return stID.substring(3,6);
      } else throw new IllegalArgumentException("Unrecognized ID length : "+stID);
   }

   public IDPrefixBuffer append(char x) {
      sz.append(x);
      return this;
   }

   public IDPrefixBuffer append(int x) {
      sz.append(x);
      return this;
   }

   public IDPrefixBuffer append(long x) {
      sz.append(x);
      return this;
   }

   public IDPrefixBuffer append(Object x) {
      sz.append(x);
      return this;
   }

   public String toString() {
      return sz.toString();
   }

   public String getID() throws Exception {
      return IDFactory.createID(toString(), 20);
   }
}
