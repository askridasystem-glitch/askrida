/***********************************************************************
 * Module:  com.crux.util.NumberSpell
 * Author:  Denny Mahendra
 * Created: Jun 1, 2006 5:19:33 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.math.BigDecimal;

public class NumberSpell {

   public static final transient String INTERNATIONAL="int";
   public static final transient String INDONESIA="ind";
   
   public static final transient String IDR ="RUPIAH";
   public static final transient String USD ="DOLLAR";


   public static String readNumber(String number){
       return readNumber(number,null,INDONESIA);
   }
   public static String readNumber(String number,String currency_desc){
      return "{L-ENG"+readNumber(number,currency_desc,INTERNATIONAL)+"-L}{L-INA"+readNumber(number,currency_desc,INDONESIA)+"-L}";
   }
   public static String readNumber(String number,String currency_desc,String lang) {

       final boolean isIndonesia = (INDONESIA.equalsIgnoreCase(lang));
       final boolean isMinus = number.startsWith("-");
       
       if(isMinus){
       		number = number.substring(number.indexOf("-")+1);
       }
       number = formattingNumber(number);
       String centSpell = "";


       if (number.indexOf(".") > -1) {
           String cent  = number.substring(number.indexOf(".")+1);
           number = number.substring(0,number.indexOf("."));
           //System.out.println(cent);
           centSpell = centSpell(cent,isIndonesia);
       }

       int lengthNumber = number == null ? 0 : number.length();
       double lengthCatDigit = lengthNumber / 3;
       long newCatDigit = java.lang.Math.round(lengthCatDigit) + 1;
       String spell = "";
       String tmpspell = "";

       for (long i = 0; i < newCatDigit; i++) {
           if (number != null) {
               String tmpNumber = "";
               if (number.length() >= 3) {
                   tmpNumber = number.substring(lengthNumber - 3);
                   number = number.substring(0, lengthNumber - 3);
                   lengthNumber = number.length();
                   tmpspell = readNumberThreeDigit(tmpNumber,isIndonesia);
                   //System.out.println("track1 : " +lengthNumber+ "| "+ tmpNumber + " | " + number + " | " + tmpspell);
               } else if (number.length() == 2) {
                   tmpspell = readNumberTwoDigit(number,isIndonesia);
                   tmpNumber = number;
                   number = "";
                   //System.out.println("track2 : " + lengthNumber+ "| "+tmpNumber + " | " + tmpspell);
               } else if (number.length() == 1) {
                   tmpspell = readNumberOneDigit(number,isIndonesia);
                   tmpNumber = number;
                   number = "";
                   //System.out.println("track3 : " + lengthNumber+ "| "+ tmpNumber + " | " + tmpspell);
               }

               if (i == 0) {
                   spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + spell);
               } else if (i == 1) {
                   if(isIndonesia)
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " RIBU " + spell);
                   else
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " THOUSANDS " + spell);
               } else if (i == 2) {
                   if(isIndonesia)
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " JUTA " + spell);
                   else
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " MILLION " + spell);
               } else if (i == 3) {
                   if(isIndonesia)
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " MILYAR " + spell);
                   else
                      spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " BILLION " + spell);
               } else if (i == 4) {
                   if(isIndonesia)
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " TRILYUN " + spell);
                   else
                       spell = isAllDigitZero(tmpNumber) ? spell : (tmpspell + " TRILLION " + spell);
               }
           }
           //System.out.println(i + " spelllll: " + spell);
       }
       
       if(spell.startsWith("SATU RIBU")) spell = spell.replaceAll("SATU RIBU","SERIBU");
       
       if(isMinus) spell = "MINUS "+ spell;
       
       if(currency_desc!=null){
           spell = spell +" "+  currency_desc +" "+ centSpell;
           
       }else{
           if(centSpell!=""){
               spell = spell+" KOMA "+centSpell;
           }
       }
       
      
       
       return spell;
   }
   
   private static String readNumberOneDigit(String stOneDigitNumber,boolean isIndonesia) {
       if ("1".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "SATU";
           else
               return "ONE";
       } else if ("2".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "DUA";
           else
               return "TWO";
       } else if ("3".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "TIGA";
           else
               return "THREE";
       } else if ("4".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "EMPAT";
           else
               return "FOUR";
       } else if ("5".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "LIMA";
           else
               return "FIVE";
       } else if ("6".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "ENAM";
           else
               return "SIX";
       } else if ("7".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "TUJUH";
           else
               return "SEVEN";
       } else if ("8".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "DELAPAN";
           else
               return "EIGHT";
       } else if ("9".equals(stOneDigitNumber)) {
           if(isIndonesia)
               return "SEMBILAN";
           else
               return "NINE";
       }

       return "";
   }

   private static String readNumberTwoDigit(String stTwoDigitNumber, boolean isIndonesia) {
      
       if (stTwoDigitNumber == null || stTwoDigitNumber.length() > 2) {
           return "";
       } else {
           String firstTwoDigit = stTwoDigitNumber.substring(0, 1);
           String secondTwoDigit = stTwoDigitNumber.substring(1);
            //System.out.println("firstTwoDigit= " +firstTwoDigit );
             //System.out.println("secondTwoDigit= " +secondTwoDigit );
           if ("0".equals(firstTwoDigit)) {
               return readNumberOneDigit(secondTwoDigit,isIndonesia);
           } else if ("1".equals(firstTwoDigit)) {
               if ("10".equals(stTwoDigitNumber)) {
                   if (isIndonesia)
                       return "SEPULUH";
                   else
                       return "TEN";
               } else if ("11".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "SEBELAS";
                   else
                       return "ELEVEN";
               } else if ("12".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "DUA BELAS";
                   else
                       return "TWELVE";
               } else if ("13".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "TIGA BELAS";
                   else
                       return "THIRTEEN";
               } else if ("14".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "EMPAT BELAS";
                   else
                       return "FOURTEEN";
               } else if ("15".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "LIMA BELAS";
                   else
                       return "FIFTEEN";
               } else if ("16".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "ENAM BELAS";
                   else
                       return"SIXTEEN";
               } else if ("17".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "TUJUH BELAS";
                   else
                       return "SEVENTEEN";
               } else if ("18".equals(stTwoDigitNumber)) {
                   if(isIndonesia)
                       return "DELAPAN BELAS";
                   else
                       return "EIGHTEEN";
               } else if ("19".equals(stTwoDigitNumber)) {
                   if (isIndonesia)
                        return "SEMBILAN BELAS";
                   else
                       return "NINETEEN";

               }
           } else {
               if (isIndonesia)
                   return readNumberOneDigit(firstTwoDigit,isIndonesia) + " PULUH " + readNumberOneDigit(secondTwoDigit,isIndonesia);
               else{
                   String firstTwoDigitSpell ="";
                   if("2".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="TWENTY";
                   }else if("3".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="THIRTY";
                   }else if("4".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="FORTY";
                   }else if("5".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="FIFTY";
                   }else if("6".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="SIXTY";
                   }else if("7".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="SEVENTY";
                   }else if("8".equalsIgnoreCase(firstTwoDigit)){
                       firstTwoDigitSpell="EIGHTY";
                   }else if("9".equalsIgnoreCase(firstTwoDigit))
                       firstTwoDigitSpell="NINETY";
                   return firstTwoDigitSpell+" "+readNumberOneDigit(secondTwoDigit,isIndonesia);
               }
           }
       }
       return "";
   }

   private static String readNumberThreeDigit(String stThreeDigitNumber,boolean isIndonesia) {
       if (stThreeDigitNumber == null || stThreeDigitNumber.length() > 3) {
           return "";
       } else {
           String firstDigit = stThreeDigitNumber.substring(0, 1);
           String secondTwoDigit = stThreeDigitNumber.substring(1);
           if ("0".equals(firstDigit)) {
               return readNumberTwoDigit(secondTwoDigit,isIndonesia);
           } else if(isIndonesia){
               if ("1".equals(firstDigit)) {
                   return "SERATUS " + readNumberTwoDigit(secondTwoDigit,isIndonesia);
               } else {
                   return readNumberOneDigit(firstDigit,isIndonesia) + " RATUS " + readNumberTwoDigit(secondTwoDigit,isIndonesia);
               }
           }else{
               return readNumberOneDigit(firstDigit,isIndonesia) + " HUNDRED " + readNumberTwoDigit(secondTwoDigit,isIndonesia);
           }
       }
   }

   private static boolean isAllDigitZero(String number) {
       if (number != null) {
           if (number.length() == 0) {
               return true;
           } else {
               if (number.length() == 1) {
                   if ("0".equals(number)) {
                       return true;
                   }
               } else if (number.length() == 2) {
                   if ("00".equals(number)) {
                       return true;
                   }
               } else if (number.length() == 3) {
                   if ("000".equals(number)) {
                       return true;
                   }
               }
           }
       } else {
           return true;
       }
       return false;
   }

   private static String formattingNumber(String number) {
       if (number != null) {
           //remove all comma in number
           number = number.replaceAll(",", "");

           //formatting number with maximum 2 digit number after period
           int idxLastPeriod = number.lastIndexOf(".");
           String digitNumberNumerik = "";
           String digitNumberCent = "";

           if (idxLastPeriod == -1) {
               digitNumberNumerik = number;
               digitNumberCent = null;
               return digitNumberNumerik;
           } else {
               digitNumberNumerik = number.substring(0, idxLastPeriod);
               digitNumberCent = number.substring(idxLastPeriod + 1);
           }
           if (digitNumberCent != null) {
               if (digitNumberCent.length() > 2) {
                   digitNumberCent = digitNumberCent.substring(0, 2);
               }
           }

           return digitNumberNumerik + "." + digitNumberCent;

       }
       return null;
   }

   private static String centSpell(String cent, boolean isIndonesia) {
       //String cent = number.substring(number.indexOf(".") + 1);
       //System.out.println(cent);

       if(("00".equalsIgnoreCase(cent))||("0".equalsIgnoreCase(cent)))
           return "";
       else
           if (isIndonesia)
               return readNumberTwoDigit(cent,isIndonesia) + " SEN ";
           else
               return "AND "+ readNumberTwoDigit(cent,isIndonesia) + " CENT ";

   }
   
   public static void main(String [] args) throws Exception {
      

   }

}
