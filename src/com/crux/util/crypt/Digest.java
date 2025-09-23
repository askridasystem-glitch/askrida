/***********************************************************************
 * Module:  com.crux.util.crypt.Digest
 * Author:  Denny Mahendra
 * Created: Jun 13, 2004 10:18:13 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util.crypt;

import java.security.MessageDigest;

public class Digest {
   public static String computeDigest(String msg) throws Exception {
      if (msg==null) return null;

      MessageDigest algorithm = null;

      algorithm = MessageDigest.getInstance("MD5");

      byte[] content = msg.getBytes();

      if (content != null) {
         algorithm.reset();
         algorithm.update(content);

         byte[] digest = algorithm.digest();
         StringBuffer hexString = new StringBuffer();
         int digestLength = digest.length;

         for (int i = 0; i < digestLength; i++) {
            hexString.append(hexDigit(digest[i]));
         }

         return hexString.toString();
      }

      return null;
   }

   public static byte [] computeDigest(byte [] content) throws Exception {
      if (content==null) return null;

      MessageDigest algorithm = null;

      algorithm = MessageDigest.getInstance("MD5");

      if (content != null) {
         algorithm.reset();
         algorithm.update(content);

         byte[] digest = algorithm.digest();

         return digest;
      }

      return null;
   }

   static private String hexDigit(byte x) {
      StringBuffer sb = new StringBuffer();
      char c;

      // First nibble
      c = (char) ((x >> 4) & 0xf);

      if (c > 9) {
         c = (char) ((c - 10) + 'a');
      }
      else {
         c = (char) (c + '0');
      }

      sb.append(c);

      // Second nibble
      c = (char) (x & 0xf);

      if (c > 9) {
         c = (char) ((c - 10) + 'a');
      }
      else {
         c = (char) (c + '0');
      }

      sb.append(c);

      return sb.toString();
   }

   public static String computeDigest(String stUserID, String stPassword) throws Exception {
      if (stPassword==null) return null;

      return computeDigest(stUserID+stPassword);
   }
}
