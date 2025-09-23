/***********************************************************************
 * Module:  com.crux.test.Test
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 12:34:21 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.test;

public class Test {
   public static void main(String [] args) {
      final String S = "PGR[select count(1) from all_sequences where sequence_name=?]" +
                          "ORA[select count(1) from pg_class where relname=? and relkind='S']";

      final String[] s = S.split("[\\]]");

      for (int i = 0; i < s.length; i++) {
         String s1 = s[i];
         final String[] s2 = s1.split("[\\[]");
         for (int j = 0; j < s2.length; j++) {
            String s3 = s2[j];
            System.out.print(s3+" - ");
         }
         System.out.println();
      }
   }
}
