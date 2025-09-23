/***********************************************************************
 * Module:  com.crux.util.BDUtil
 * Author:  Denny Mahendra
 * Created: Apr 12, 2004 1:02:02 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BDUtil {
   public static BigDecimal zero = new BigDecimal(0);
   public static BigDecimal one = new BigDecimal(1);
   public static BigDecimal hundred = new BigDecimal(100);
   public static BigDecimal thousand = new BigDecimal(1000);

   public static BigDecimal add(BigDecimal a, BigDecimal b) {
      if (a==null) {
         if (b==null) {
            return null;
         } else
            return b;
      } else {
         if (b==null) {
            return a;
         } else {
            return a.add(b);
         }
      }
   }

   public static BigDecimal sub(BigDecimal a, BigDecimal b) {
      if (a==null) {
         if (b==null)
            return new BigDecimal(0);
         else {
            return b.negate();
         }
      } else {
         if (b==null)
            return a;
         else {
            return a.subtract(b);
         }
      }
   }

   public static BigDecimal mul(BigDecimal a, BigDecimal b) {
      if ((a==null) || (b==null)) return new BigDecimal(0);
      return a.multiply(b);
   }
   
   public static BigDecimal mul(BigDecimal a, BigDecimal b,int scale) {
      if ((a==null) || (b==null)) return new BigDecimal(0);
      return a.multiply(b).setScale(scale, BigDecimal.ROUND_HALF_DOWN);
   }
   
   public static BigDecimal mulRound(BigDecimal a, BigDecimal b,int scale) {
      if ((a==null) || (b==null)) return new BigDecimal(0);
      return a.multiply(b).setScale(scale, BigDecimal.ROUND_HALF_UP);
   }

   public static BigDecimal div(BigDecimal a, BigDecimal b) {

      if (Tools.isEqual(a,b)) return new BigDecimal(1);

      if (BDUtil.isZero(b)) return null;

      if ((a==null) || (b==null)) return new BigDecimal(0);
      
      return a.divide(b,2,BigDecimal.ROUND_HALF_DOWN);
   }

   public static BigDecimal div(BigDecimal a, BigDecimal b, int prec) {
      try {
         if ((a==null) || (b==null)) return new BigDecimal(0);
         return a.divide(b,prec,BigDecimal.ROUND_HALF_DOWN);
      } catch (Exception e) {
         return null;
      }
   }

   public static BigDecimal divNR(BigDecimal a, BigDecimal b) {
      if ((a==null) || (b==null)) return new BigDecimal(0);
      return a.divide(b,BigDecimal.ROUND_HALF_DOWN);
   }


   public static BigDecimal negate(BigDecimal bd) {
      if (bd==null) return null;

      return bd.negate();
   }

   public static BigDecimal getRateFromPct(BigDecimal dbR) {
      if (dbR==null) return zero;

      return dbR.movePointLeft(2);
   }

   public static BigDecimal getPctFromRate(BigDecimal dbPCT) {
      if (dbPCT==null) return zero;

      return dbPCT.movePointRight(2);
   }
   
    public static BigDecimal getPctFromRate2(BigDecimal dbPCT) {
      if (dbPCT==null) return zero;

      return dbPCT.movePointRight(4);
   }

   public static boolean biggerThanZero(BigDecimal num) {
      return Tools.compare(num,zero)>0;
   }
   
   public static boolean biggerThan(BigDecimal num,BigDecimal than) {
      return Tools.compare(num,than)>0;
   }
   
   public static boolean lesserThanZero(BigDecimal num) {
      return Tools.compare(num,zero)<0;
   }
   
   public static boolean lesserThan(BigDecimal num,BigDecimal than) {
      return Tools.compare(num,than)<0;
   }

   public static boolean isEqual(BigDecimal amt1, BigDecimal amt2, int digits) {

      if (amt1==null && amt2==null) return true;

      if (amt1==null || amt2==null) return false;

      amt1=amt1.setScale(digits, BigDecimal.ROUND_HALF_DOWN);
      amt2=amt2.setScale(digits, BigDecimal.ROUND_HALF_DOWN);

      return Tools.isEqual(amt1,amt2);
   }

   public static boolean isZero(BigDecimal db) {
      return Tools.compare(db,zero)==0;
   }

   public static boolean isZeroOrNull(BigDecimal db) {
      return db==null || Tools.compare(db,zero)==0;
   }
   
   public static BigDecimal getRateFromMile(BigDecimal dbR) {
      if (dbR==null) return zero;

      return dbR.movePointLeft(3);
   }

   public static BigDecimal getMileFromRate(BigDecimal dbMILE) {
      if (dbMILE==null) return zero;

      return dbMILE.movePointRight(3);
   }
   
   public static double getDouble(BigDecimal num){
   	 if(num==null) return zero.doubleValue();
   	
   	 return num.doubleValue();
   }
   
   public static BigDecimal roundUp(BigDecimal num){
       return num.setScale(0,BigDecimal.ROUND_HALF_UP);
   }

   public static BigDecimal roundUp(BigDecimal num,int scale){
       return num.setScale(scale,BigDecimal.ROUND_HALF_UP);
   }

   public static BigDecimal round(BigDecimal num,int scale){
       return num.setScale(scale,BigDecimal.ROUND_HALF_DOWN);
   }
   
   public static BigDecimal subScale2(BigDecimal a, BigDecimal b) {
      if (a==null) {
         if (b==null)
            return new BigDecimal(0);
         else {
            return b.negate();
         }
      } else {
         if (b==null)
            return a;
         else {
            return a.subtract(b).setScale(2,BigDecimal.ROUND_HALF_DOWN);
         }
      }
   }
   
   public static boolean biggerThanEqual(BigDecimal num,BigDecimal than) {
      return Tools.compare(num,than)>=0;
   }
   
   public static boolean isNegative(BigDecimal num) {
      return Tools.compare(num,zero)<0;
   }
   
   public static boolean isPositive(BigDecimal num) {
      return Tools.compare(num,zero)>0;
   }
   
   public static BigDecimal divRoundUp(BigDecimal a, BigDecimal b, int prec) {
      try {
         if ((a==null) || (b==null)) return new BigDecimal(0);
         return a.divide(b,prec,BigDecimal.ROUND_HALF_UP);
      } catch (Exception e) {
         return null;
      }
   }
    
   public static BigDecimal getPctFromMile(BigDecimal dbR) {
      if (dbR==null) return zero;

      return dbR.movePointLeft(1);
   }
   
   public static boolean lesserThanEqual(BigDecimal num,BigDecimal than) {
      return Tools.compare(num,than)<=0;
   }

   public static BigDecimal getMileFromPCT(BigDecimal dbMILE) {
      if (dbMILE==null) return zero;

      return dbMILE.movePointRight(1);
   }

   public static void main(String [] args) throws Exception {
       
      //System.out.println("test = "+ round(getMileFromPCT(new BigDecimal(0.0311)),3));
      BigDecimal premiBruto = new BigDecimal(1000000);
      BigDecimal premiReas = new BigDecimal(999000);
      BigDecimal selisih = sub(premiBruto, premiReas);

      System.out.println("selisih "+ sub(premiBruto, premiReas));
      System.out.println("cek1 "+ biggerThan(selisih, BDUtil.thousand));
      System.out.println("cek2 "+ lesserThan(selisih, negate(BDUtil.thousand)));
      //System.out.println("selisih "+ sub(premiReas,premiBruto ));

      System.out.println("0459220312221102800".length());

      int tahun = DateUtil.getYearsBetweenHUTB(DateUtil.getDate("18/07/2025"), DateUtil.getDate("18/04/2040"));

        BigDecimal jmlPolis = BDUtil.divRoundUpFix(new BigDecimal(tahun), new BigDecimal(5), 0); //3

        System.out.println("tahun = "+ tahun);
        System.out.println("jmlPolis = "+ jmlPolis);

        if(BDUtil.lesserThan(new BigDecimal(-1639), new BigDecimal(-65579))){
            System.out.println("pph lebih besar = ");
        }

   }

   public static String setMinusToKurung(BigDecimal nilai) {

        String data = moneyToTextUS(nilai);

        String[] a = data.split("-");

        return "(" + a[1] + ")";

    }

    public static String moneyToTextUS(BigDecimal money) {
        if (money == null) {
            return "0.00";
        }

        if (money != null && money.compareTo(new BigDecimal(0)) != 0) {
            boolean isNegative = false;
            if (money.toString().contains("-")) {
                money = money.abs();
                isNegative = true;
            }

            String lsMoney1 = "";
            String lsMoney2 = "";
            //System.out.println(money + " <<<< 1");
            if (!money.toString().contains(".")) {
                lsMoney1 += "00.";
                for (int i = money.toString().length() - 1; i > -1; i--) {
                    lsMoney1 += money.toString().charAt(i);
                }
                //System.out.println(lsMoney1 + " <<ls money");
            } else {
                boolean point = false;
                money.toString().indexOf(".");
                for (int i = money.toString().indexOf(".") + 2; i >= money.toString().indexOf("."); i--) {
                    lsMoney1 += money.toString().charAt(i);
                }

                for (int i = money.toString().length() - 1; i > -1; i--) {
                    if (point) {
                        lsMoney1 += money.toString().charAt(i);
                    }
                    if (money.toString().charAt(i) == '.') {
                        point = true;
                    }
                }
            }

            //System.out.println(lsMoney1 + " ls money lagi");
            for (int i = 0; i < lsMoney1.length(); i++) {
                if (i == 6 || i == 9 || i == 12 || i == 15 || i == 18) {
                    lsMoney2 += ".";
                }
                lsMoney2 += lsMoney1.charAt(i);
            }
            //System.out.println(lsMoney2 + " ls money 2");
            lsMoney1 = "";

            for (int i = lsMoney2.length() - 1; i > -1; i--) {
                lsMoney1 += lsMoney2.toString().charAt(i);
            }

           // System.out.println(lsMoney1 + " ls money terakhir");
            if (isNegative) {
                lsMoney1 = "-" + lsMoney1;
            }
            lsMoney1 = lsMoney1.substring(0, lsMoney1.length() - 3).trim();
            //System.out.println(lsMoney1);
            return (lsMoney1);
        } else {
            return ("0");
        }
    }

    private static void calculatePPH21Progressive2() {

        BigDecimal akumulasiKomisi = new BigDecimal(38000000);
        BigDecimal komisiPolis = new BigDecimal(20500000);
        BigDecimal totalKomisiCurrent = BDUtil.add(akumulasiKomisi, komisiPolis);


        BigDecimal dbPPH21tot = akumulasiKomisi;

        /*
          25.000.000 x 5%     =    1.250.000
          50.000.000 x 10%   =    5.000.000
          100.000.000 x 15%  = 15.000.000
          200.000.000 x 35%  = 70.000.000
          125.000.000 x 35% = 43.750.000
         */

        String[][] taxTab = new String[][]{
            {"50000000", "0.05"},
            {"250000000", "0.15"},
            {"500000000", "0.25"},
            {"-1", "0.30"},
            };

        BigDecimal taxAmount = null;

        BigDecimal dbPPH21totS = dbPPH21tot;

        BigDecimal sisaKomisi = komisiPolis;

        for (int i = 0; i < taxTab.length; i++) {
            String[] t = taxTab[i];

            if(BDUtil.lesserThan(new BigDecimal(t[0]),akumulasiKomisi)) continue;
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            
            BigDecimal amt = komisiPolis;

            BigDecimal lim = new BigDecimal(t[0]);

           

            if(BDUtil.biggerThan(totalKomisiCurrent, lim)){
                amt = BDUtil.sub(lim, akumulasiKomisi);
                komisiPolis = BDUtil.sub(komisiPolis, amt);
            }


            System.out.println(" penghasilan kena pajak : " + amt);
            
            taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), 0), new BigDecimal(1.20),0));



            sisaKomisi = BDUtil.sub(sisaKomisi, amt);


            System.out.println(" pct pajak : " + t[1]);
            System.out.println(" pajak ke "+ (i+1) +" : " + BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), 0), new BigDecimal(1.20),0));
            System.out.println(" sisaKomisi : " + sisaKomisi);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");

            //cek lagi
            /*
            if (hasNPWP()) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(amt, new BigDecimal(t[1]), scale));
            } else if (!hasNPWP()) {
                taxAmount = BDUtil.add(taxAmount, BDUtil.mul(BDUtil.mul(amt, new BigDecimal(t[1]), scale), new BigDecimal(1.20)));
            }*/

            if (BDUtil.isZero(sisaKomisi)) break;
        }

        BigDecimal actPct = BDUtil.div(taxAmount, dbPPH21totS, 5);

        System.out.println("komisi total : "+ akumulasiKomisi);
        System.out.println("pct : "+ actPct);
        System.out.println("total pajak : "+ taxAmount);


    }

    public static BigDecimal divRoundUpFix(BigDecimal a, BigDecimal b, int prec) {
      try {
         if ((a==null) || (b==null)) return new BigDecimal(0);
         return a.divide(b,prec,BigDecimal.ROUND_UP);
      } catch (Exception e) {
         return null;
      }
   }
   
}
