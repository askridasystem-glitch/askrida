/***********************************************************************
 * Module:  com.crux.util.StringTools
 * Author:  Denny Mahendra
 * Created: May 28, 2004 9:33:44 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.sql.CHAR;

public class StringTools {
   public static String rightPad(String x, char pad, int len) {
      if (x.length()>=len) return x;
      final StringBuffer sz = new StringBuffer();
      sz.append(x);
      while (sz.length()<len) sz.append(pad);
      return sz.toString();
   }

   public static String leftPad(String x, char pad, int len) {
      if (x==null) x=pad+"";
      if (x.length()>=len) return x;
      final int n = len-x.length();

      final char[] chars = new char[n];

      for (int i = 0; i < chars.length; i++) {
         chars[i] = pad;
      }

      return new String(chars)+x;
   }

   public static String clip(String x, int len) {
      if (x.length()>len) return x.substring(0,len); else return x;
   }

   public static String rightTrim(String s) {
      for (int i=s.length()-1;i>=0;i--) {
         if (s.charAt(i)!=' ') {
            return s.substring(0,i+1);
         }
      }
      return "";
   }

   public static String delete(String stText, int position, int count) {
      return stText.substring(0,position)+stText.substring(position+count, stText.length());
   }

   public static String insert(String stText, int position, String stInsertedText) {
      return
              stText.substring(0,position)+
              stInsertedText+
              stText.substring(position,stText.length());
   }

   /**
    * Will replace all occurence of key with replaceWith in txt
    * @param txt
    * @param key
    * @param replaceWith
    * @return
    */
   public static String replace(String txt, String key, String replaceWith) {

      while (true) {
         final int n = txt.indexOf(key);

         if(n>=0) {
            txt = delete(txt,n, key.length());
            txt = insert(txt,n,replaceWith);
            continue;
         }

         break;
      }

      return txt;
   }

   public static boolean isEqualIgnoreCaseTrim(String s1, String s2) {
      if (s1 == s2) return true;

      if (s1==null || s2==null) return false;

      s1=s1.toLowerCase().trim();
      s2=s2.toLowerCase().trim();

      return s1.equals(s2);
   }

   public static int indexOf(String s, String s1) {
      if (s==null) return -1;

      return s.indexOf(s1);
   }

   public static String getString(byte[] b) {

      StringBuffer sz = new StringBuffer();

      for (int i = 0; i < b.length; i++) {
         byte b1 = b[i];
         sz.append(((char) b[i]));

         System.out.println(((char)b1));
      }

      return sz.toString();
   }

   public static byte[] getBytes(String s) {
      byte[] bs = new byte [s.length()];
      for (int i=0;i<s.length();i++) {
         bs[i]=(byte) s.charAt(i);
      }

      return bs;
   }
   
   public static boolean contain(String txt, char key){
      boolean search = false;
      
      for (int i=0;i<txt.length();i++) {
    
         if(txt.charAt(i)==key){
             search = true;
             break;
         }

      }
      
      return search;
   }
   
   public static boolean containNumeric(String txt){
      boolean search = false;
      
      for (int i=0;i<txt.length();i++) {
    
          switch (txt.charAt(i)){
              case '0' : search = true; break;
              case '1' : search = true; break;
              case '2' : search = true; break;
              case '3' : search = true; break;
              case '4' : search = true; break;
              case '5' : search = true; break;
              case '6' : search = true; break;
              case '7' : search = true; break;
              case '8' : search = true; break;
              case '9' : search = true; break;
          }
      }
      
      return search;
   }

   public static boolean validatePassword(String password) throws Exception{

       if (password.length() < 8) {
           System.out.println("password < 8 digit");
           throw new Exception("Password Salah, panjang password minimal 8 karakter dan mengandung kombinasi karakter numerik");
            //return false;
        } else {
            char c;
            int countNumerik = 0;
            int countChar = 0;
            for (int i = 0; i < password.length(); i++) {
                c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    //throw new RuntimeException("password harus mengandung kombinasi karakter numerik");
                    //return false;
                } else if (Character.isDigit(c)) {
                    countNumerik++;
                } else if (Character.isLetter(c)) {
                    countChar++;
                }
            }
            if (countNumerik < 1)   {
                System.out.println("password harus mengandung min 1 numeric");
                throw new Exception("Password Salah, password harus mengandung minimal 1 numerik");
                //return false;
            }
            if (countChar < 1)   {
                System.out.println("password harus mengandung min 1 char ");
                throw new Exception("Password Salah, password harus mengandung minimal 1 karakter");
                //return false;
            }
        }
        return true;
    }

   public static String getFirstLetterOnly(String value) throws Exception{

       String valueCheck = value.trim().replaceAll(" ", "");

       if(valueCheck.contains("-"))
           valueCheck = valueCheck.trim().replaceAll("-", "");

       char c;
       String firstLetter = "";

       for (int i = 0; i < valueCheck.length(); i++) {
            c = valueCheck.charAt(i);

            if (Character.isLetter(c)){
                firstLetter = firstLetter + c;
            }else if (Character.isDigit(c)){
                 break;
            }
        }

       return firstLetter.trim().toUpperCase();
    }

   public static boolean onlyDigits(String str)
    {
        // Regex to check string
        // contains only digits
        String regex = "[0-9]+";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Find match between given string
        // and regular expression
        // using Pattern.matcher()
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

   public static void main(String [] args) throws Exception {
       //validatePassword("1bcd@fg");
       //System.out.println("cek numeric = "+ onlyDigits("ab"));
       //System.out.println("panjang LKP/59/11/1122/0001 "+"LKP/59/11/1122/0001".length());


       System.out.println("045920200924123401".substring(16,18));
       System.out.println("045920200924123401".length());

       if("045920200924123401".length()==18){
           System.out.println("045920200924123401".substring(0,16)+ "00");

       }

       if("0459202009241234501".length()==19){
           System.out.println("0459202009241234501".substring(0,17)+ "00");

       }

   }
       
}

