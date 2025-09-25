/***********************************************************************
 * Module:  com.crux.util.DateUtil
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 2:40:32 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;


//import com.google.common.base.Optional;
//import com.google.common.io.Files;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.webfin.h2h.model.KalkulatorPremiResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
public class DateUtil {

    private final static transient LogManager logger = LogManager.getInstance(DateUtil.class);

   private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
   private static SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
   private static SimpleDateFormat df4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
   private static SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
   private static SimpleDateFormat df5 = new SimpleDateFormat("MM/dd/yyyy");
   private static SimpleDateFormat df6 = new SimpleDateFormat("yyMMdd");
   private static SimpleDateFormat df7 = new SimpleDateFormat("yyyy-MM-dd");

   private static SimpleDateFormat df8 = new SimpleDateFormat("yyyyMMdd");
   private static SimpleDateFormat df9 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public static Date getDate(String stDate) {
      try {
         if ((stDate == null) || (stDate.trim().equals("")))
            return null;
         else {
            if (stDate.indexOf(":")>=0)
               return df4.parse(stDate);
            else
               return df.parse(stDate);
         }
      } catch (ParseException e) {
         throw new RuntimeException(e);
      }
   }
   
   public static String getDate2(String stDate) {
         String date = "";
         String dateArray[] = stDate.split("/");
         if(dateArray[1].toLowerCase().equalsIgnoreCase("jan")) date = dateArray[0]+"/01/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("feb")) date = dateArray[0]+"/02/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("mar")) date = dateArray[0]+"/03/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("apr")) date = dateArray[0]+"/04/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("may")) date = dateArray[0]+"/05/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("jun")) date = dateArray[0]+"/06/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("jul")) date = dateArray[0]+"/07/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("aug")) date = dateArray[0]+"/08/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("sep")) date = dateArray[0]+"/09/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("oct")) date = dateArray[0]+"/10/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("nov")) date = dateArray[0]+"/11/"+dateArray[2];
         if(dateArray[1].toLowerCase().equalsIgnoreCase("dec")) date = dateArray[0]+"/12/"+dateArray[2];
		 return date;
   }

   public static Date getDateTime(Date d) throws Exception {
      if (d==null) return null;
      return df2.parse(getDateTimeStr(d));
   }

   public static String getDateStr(Date d) {
      if (d==null) return null;
      return df.format(d);
   }
   
   public static String getDateStr2(Date d) {
      if (d==null) return null;
      return "20" + df6.format(d);
   }

   public static String getDateTimeStr(Date d) {
      if (d==null) return null;
      return df2.format(d);
   }

   public static String getDateTimeStr2(Date d) {
      if (d==null) return "";
      return df4.format(d);
   }
   
   public static String getDateTimeStr3(String d) {
       String arrayDate [] = d.split(" ");//1 0 5
       String date="";
       
       if(arrayDate.length<=2){
       		date = arrayDate[0].substring(8,10)+"/"+arrayDate[0].substring(5,7)+"/"+arrayDate[0].substring(0,4);

       }else if(arrayDate.length==7){
       		date = getDate2(arrayDate[2]+"/"+arrayDate[1]+"/"+arrayDate[6]);
       }else{
       		date = getDate2(arrayDate[2]+"/"+arrayDate[1]+"/"+arrayDate[5]);
       }
       
       return date;
   }

   public static String getTimeStr(Date d) {
      if (d==null) return "";
      return df3.format(d);
   }

   public static Date add(Date d1, Date d2) throws Exception {
      if (d1==null) return d2;
      if (d2==null) return d1;

      return new Date(d1.getTime() + d2.getTime());
   }

   public static Date getNewDate() throws Exception {
      return DateUtil.getDate(DateUtil.getDateStr(new Date()));
   }

   public static Date getNewDateTime() throws Exception {
      return DateUtil.getDateTime(new Date());
   }

   public static String getMonth2Digit(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;

      if (month<10) return "0"+month; else return String.valueOf(month);
   }
   
   public static String getMonthRomawi(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;
      
      String bulanRomawi = null;
      
      switch (month) {
	  	case 1: bulanRomawi = "I"; break;
	  	case 2: bulanRomawi = "II"; break;
	  	case 3: bulanRomawi = "III"; break;
	  	case 4: bulanRomawi = "IV"; break;
	  	case 5: bulanRomawi = "V"; break;
	  	case 6: bulanRomawi = "VI"; break;
	  	case 7: bulanRomawi = "VII"; break;
	  	case 8: bulanRomawi = "VIII"; break;
	  	case 9: bulanRomawi = "IX"; break;
	  	case 10: bulanRomawi = "X"; break;
	  	case 11: bulanRomawi = "XI"; break;
	  	case 12: bulanRomawi = "XII"; break;
	  	default: bulanRomawi= "ERROR";
	  }
	  
	  return bulanRomawi;
   }

   public static String getYear2Digit(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int year = cld.get(Calendar.YEAR);

      year = year % 100;

      if (year<10) return "0"+year; else return String.valueOf(year);
   }
   
   public static String getYear(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int year = cld.get(Calendar.YEAR);

      //year = year % 100;

      //if (year<10) return "0"+year; else 
      return String.valueOf(year);
   }

   public static long sub(Date dtPeriodStart, Date dtPeriodEnd) {

      final long d1 = dtPeriodStart==null?0:dtPeriodStart.getTime();
      final long d2 = dtPeriodEnd==null?0:dtPeriodEnd.getTime();

      return d2-d1;
   }

   private static HashMap sdfPool = new HashMap();
   private static HashMap monthPool = new HashMap();

   {
      monthPool.put("jan","jan");
      monthPool.put("feb","feb");
      monthPool.put("mar","mar");
      monthPool.put("apr","apr");
      monthPool.put("may","mei");
      monthPool.put("jun","jun");
      monthPool.put("jul","jul");
      monthPool.put("aug","ags");
      monthPool.put("sep","sep");
      monthPool.put("oct","oct");
      monthPool.put("nov","nov");
      monthPool.put("dec","dec");

      monthPool.put("january","januari");
      monthPool.put("february","februari");
      monthPool.put("march","maret");
      monthPool.put("april","april");
      monthPool.put("may","mei");
      monthPool.put("june","juni");
      monthPool.put("july","juli");
      monthPool.put("august","agustus");
      monthPool.put("september","september");
      monthPool.put("october","oktober");
      monthPool.put("november","november");
      monthPool.put("december","desember");
   }

   private static String [] xlangshortMonth = {
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "{L-ENGMay-L}{L-INAMei-L}",
      "Jun",
      "Jul",
      "{L-ENGAug-L}{L-INAAgs-L}",
      "Sep",
      "{L-ENGOct-L}{L-INAOkt-L}",
      "Nov",
      "{L-ENGDec-L}{L-INADes-L}",
   };

   private static String [] xlanglongMonth = {
      "{L-ENGJanuary-L}{L-INAJanuari-L}",
      "{L-ENGFebruary-L}{L-INAFebruari-L}",
      "{L-ENGMarch-L}{L-INAMaret-L}",
      "April",
      "{L-ENGMay-L}{L-INAMei-L}",
      "{L-ENGJune-L}{L-INAJuni-L}",
      "{L-ENGJuly-L}{L-INAJuli-L}",
      "{L-ENGAugust-L}{L-INAAgustus-L}",
      "{L-ENGSeptember-L}{L-INASeptember-L}",
      "{L-ENGOctober-L}{L-INAOktober-L}",
      "November",
      "{L-ENGDecember-L}{L-INADesember-L}",
   };

   public static String getDateStr(Date dtDate, String dateFormat) {
      if (dtDate==null) return "";

      SimpleDateFormat sdf = (SimpleDateFormat)sdfPool.get(dateFormat);

      if (sdf==null) {
         sdf = new SimpleDateFormat(dateFormat, new Locale("id","ID",""));
         sdfPool.put(dateFormat, sdf);
      }

      String z = sdf.format(dtDate);

      if (z.indexOf("^^")>=0) {
         final int n = z.indexOf("^^");

         final Calendar cld = Calendar.getInstance();
         cld.setTime(dtDate);

         final int month = cld.get(Calendar.MONTH);

         final String bimonth = xlanglongMonth[month];

         final StringBuffer sz = new StringBuffer(z);

         sz.delete(n,n+2);

         sz.insert(n,bimonth);

         z=sz.toString();
      };

      if (z.indexOf("^")>=0) {
         final int n = z.indexOf("^");

         final Calendar cld = Calendar.getInstance();
         cld.setTime(dtDate);

         final int month = cld.get(Calendar.MONTH);

         final String bimonth = xlangshortMonth[month];

         final StringBuffer sz = new StringBuffer(z);

         sz.delete(n,n+1);

         sz.insert(n,bimonth);

         z=sz.toString();
      };

      return z;
   }

   public static long dayTrim = 1000l*60l*60l*24l;

   /**
    * returns H+N
    * @param N
    * @return
    */
   public static Date getCurrentDate(long N) {

      Calendar cld = Calendar.getInstance();

      cld.setTime(new Date());

      Calendar cld2 = Calendar.getInstance();

      cld2.set(
              cld.get(Calendar.YEAR),
              cld.get(Calendar.MONTH),
              cld.get(Calendar.DATE)
      );

      cld2.add(Calendar.DATE, (int)N);

      //return new Date((System.currentTimeMillis()/dayTrim+N)*dayTrim);

      return cld2.getTime();
   }

   public static Date truncDate(Date dt) {
      if (dt==null) return null;

      Calendar cld = Calendar.getInstance();

      cld.setTime(dt);

      Calendar cld2 = Calendar.getInstance();

      cld2.set(
              cld.get(Calendar.YEAR),
              cld.get(Calendar.MONTH),
              cld.get(Calendar.DATE),
              0,
              0,
              0

      );

      return cld2.getTime();
   }

   public static Date ceilDate(Date dt) {
      if (dt==null) return null;

      Calendar cld = Calendar.getInstance();

      cld.setTime(dt);

      Calendar cld2 = Calendar.getInstance();

      cld2.set(
              cld.get(Calendar.YEAR),
              cld.get(Calendar.MONTH),
              cld.get(Calendar.DATE)+1,
              0,
              0,
              0

      );

      return cld2.getTime();
   }

   public static boolean isEqual(Date d1, Date d2) {
      truncDate(d1);
      truncDate(d2);
      return false;
   }

   public static int getUsiaUltahTerdekat(Date dtTanggalLahir, Date dtPeriodeAwal, int MaxHariLebih) throws Exception{

        int usia = 0;

        DateTime tglLahir = new DateTime(dtTanggalLahir);
        DateTime tglAwal = new DateTime(dtPeriodeAwal);

        Years y = Years.yearsBetween(tglLahir, tglAwal);
        int usiaTahun = y.getYears();

        int bulanLebih = getKelebihanBulan(dtTanggalLahir, dtPeriodeAwal);
        //int hariLebih = getKelebihanHari(dtTanggalLahir, dtPeriodeAwal);

        //if(MaxHariLebih > 0)
            //if(hariLebih >= MaxHariLebih) bulanLebih = bulanLebih + 1;

        if(bulanLebih >= 6) usia = usiaTahun + 1;
        else usia = usiaTahun;

        return usia;
    }

   public static boolean isValidDate(String format, String inDate) {
       //19810101
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(inDate.trim());
    } catch (ParseException pe) {
      return false;
    }
    return true;
  }

   public static String getComputerName()
{
    Map<String, String> env = System.getenv();
    if (env.containsKey("COMPUTERNAME"))
        return env.get("COMPUTERNAME");
    else if (env.containsKey("HOSTNAME"))
        return env.get("HOSTNAME");
    else
        return "Unknown Computer";
}


   public static void test() throws Exception {

       System.out.println("nopol "+ "0459222212221051000".length());
       System.out.println("nopol "+ "045922221222105100".length());

       System.out.println("tgl = "+ getDate("31/01/2024"));

       getYearsBetweenHUTB(getDate("20/01/2024"), getDate("21/01/2025"));

       System.out.println("jumlah hari = "+getDaysAmount(getDate("01/08/2024"), new Date()));

       try {

            URL url = new URL("http://192.168.1.83:8099/SistemInterkoneksiWS/RequestPolisSequence");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"pol_type_id\":\"" + "86" + "\",\"cc_code\":\"" + "23" + "\"}";
            System.out.println("%%%%%%%%%%%%%%%%% " + input);

            // For POST only - START
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = conn.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println("response = "+ response.toString());

                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(response.toString());
                        System.out.println("status = "+ json.get("status"));
                        System.out.println("sequence = "+ json.get("polis_sequence"));
                        System.out.println("keterangan = "+ json.get("keterangan"));

		} else {
			System.out.println("POST request did not work.");
		}

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }

            conn.disconnect();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
   }

   public static void main(String [] args) throws Exception {

       //getStatusBayarCaretech();
       
       //String fn = "\\\\webapps.askrida.co.id\\fin-repository\\POLIS-PAJ\\145921211124000500.pdf";
       
       //File fileCek = new File(fn);

          //if(!fileCek.exists())
              //throw new RuntimeException("Gagal dapat polis asuransi Jiwa");

       System.out.println("jumlah bulan = "+ getMonthsBetween(getDate("01/01/2024"), getDate("01/04/2025")));

       String nopol = "0488434306250007100";

      

       System.out.println("lenght no pol = "+ "048843430625000700".length());
       System.out.println("no pol = "+ "045943430625000700".substring(0,16));
       System.out.println("no pol = "+ "0488434306250007100".substring(0,17));

        //System.out.println("pol = "+ nopol.substring(0, 16)+"1"+ nopol.substring(17, 19));
       //System.out.println("pol = "+ nopol.substring(0, 16)+"2"+ nopol.substring(17, 19));
       //System.out.println("pol = "+ nopol.substring(0, 16)+"3"+ nopol.substring(17, 19));

       System.out.println("date = "+ getDateStr7(getFirstDate(new Date())));

   }
   

   public static void getStatusBayarCaretech() throws Exception{
        try {

                        logger.logDebug("############### HIT API STATUS PEMBAYARAN DI CARE ####################### ");

                        URL url = new URL("http://192.168.250.104/API_ASKRIDA_FA/B2B/ListPaidByForNotes");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("x-api-key", "nrD17nT3UHBE8JWqZr7TGdMbNfUFWN5yEUzg4RjDQwK5AczgLgebuSPLCNPqGvJiHmUq6waxvK1UuNgBh69pv6wjJ5vDPa5iWpe");

                        /*
                        {
                            "VoucherNo": "",
                            "SL_Cat": "N",
                            "DocNo": "",
                            "Type": "BD",
                            "RefNo": "",
                            "PolicyNo": "045952520824138300"
                        }*/

                        String jsonRequest = "{"+
                                       "\"VoucherNo\": \""+ "" +"\","+
                                       "\"SL_Cat\": \""+ "N" +"\","+
                                       "\"DocNo\": \""+ "" +"\","+
                                       "\"Type\": \""+ "DI" +"\","+
                                       "\"RefNo\": \""+ "" +"\","+
                                       "\"PolicyNo\": \""+ "045952520824138300" +"\""+
                                       "}";

                        logger.logDebug("############## JSON REQUEST ASKRIDA = " + jsonRequest);

                        // For POST only - START
                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(jsonRequest.getBytes());
                            os.flush();
                            os.close();
                            // For POST only - END

                            int responseCode = conn.getResponseCode();
                            logger.logDebug("POST Response Code :: " + responseCode);

                            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                    }
                                    in.close();

                                    // print result
                                    logger.logDebug("############### BALIKAN CARETECH ####################### ");
                                    logger.logDebug("############### RESPONSE CARETECH = "+ response.toString());

                                    JSONParser parser = new JSONParser();
                                    JSONObject json = (JSONObject) parser.parse(response.toString());
                                    logger.logDebug("code = "+ json.get("Code"));
                                    logger.logDebug("status_hit = "+ json.get("Status"));
                                    logger.logDebug("data = "+ json.get("Data"));
                                    logger.logDebug("ErrorMsg = "+ json.get("ErrorMsg"));

                                    JSONArray slideContent = (JSONArray) json.get("Data");
                                    Iterator i = slideContent.iterator();

                                    while (i.hasNext()) {
                                        JSONObject slide = (JSONObject) i.next();
                                        String osPremi = (String) slide.get("Outstanding");
                                        String Payment = (String) slide.get("Payment");
                                        String Payment_Date = (String) slide.get("Payment_Date");

                                        logger.logDebug("############### DATA DETIL BAYAR CARETECH ####################### ");

                                        logger.logDebug("outstanding = "+ osPremi);
                                        logger.logDebug("Payment = "+ Payment);
                                        logger.logDebug("Payment_Date = "+ Payment_Date);
                                    }

                            } else {
                                    System.out.println("POST request did not work.");
                            }

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP Error code : "
                                    + conn.getResponseCode());
                        }

                        conn.disconnect();

                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
    }



   private void executeSqlFile() {
     try {

         //select * from ins_policy where claim_status = 'PLA' and effective_flag = 'Y'
         Runtime rt = Runtime.getRuntime();
         String executeSqlCommand = "psql -U postgres -h (domain) -f (script_name) (dbName)";
         Process pr = rt.exec(executeSqlCommand);
         int exitVal = pr.waitFor();
         System.out.println("Exited with error code " + exitVal);
      } catch (Exception e) {
        System.out.println(e.toString());
      }
}

   public static Date dateBracketLow(Date dt) {
      return truncDate(dt);
   }

   public static Date dateBracketHigh(Date dt) {
      return ceilDate(dt);
   }
   
   public static String getMonth(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;
      
      String bulan = null;
      
      switch (month) {
	  	case 1: bulan = "Januari"; break;
	  	case 2: bulan = "Februari"; break;
	  	case 3: bulan = "Maret"; break;
	  	case 4: bulan = "April"; break;
	  	case 5: bulan = "Mei"; break;
	  	case 6: bulan = "Juni"; break;
	  	case 7: bulan = "Juli"; break;
	  	case 8: bulan = "Agustus"; break;
	  	case 9: bulan = "September"; break;
	  	case 10: bulan = "Oktober"; break;
	  	case 11: bulan = "November"; break;
	  	case 12: bulan = "Desember"; break;
	  	default: bulan= "ERROR";
	  }
	  
	  return bulan;
   }
   
   public static String getDateStr7(Date d) {
      if (d==null) return null;
      return df7.format(d);
   }
   
   public static int getMonthDigit(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;
      
      return month;
   }
   
   public static String getQuartal(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;
      
      String quartal = null;
      
      switch (month) {
	  	case 1: quartal = "1"; break;
	  	case 2: quartal = "1"; break;
	  	case 3: quartal = "1"; break;
	  	case 4: quartal = "2"; break;
	  	case 5: quartal = "2"; break;
	  	case 6: quartal = "2"; break;
	  	case 7: quartal = "3"; break;
	  	case 8: quartal = "3"; break;
	  	case 9: quartal = "3"; break;
	  	case 10: quartal = "4"; break;
	  	case 11: quartal = "4"; break;
	  	case 12: quartal = "4"; break;
	  	default: quartal = "0";
	  }
      
      return quartal;
   }
   
   public static Date truncDate2(Date dt) {
      if (dt==null) return null;

      Calendar cld = Calendar.getInstance();

      cld.setTime(dt);

      Calendar cld2 = Calendar.getInstance();

      cld2.set(
              cld.get(Calendar.YEAR),
              cld.get(Calendar.MONTH),
              0,
              0,
              0,
              0

      );

      return cld2.getTime();
   }
   
   public static Date truncDateFirstDay(Date dt) {
      if (dt==null) return null;

      Calendar cld = Calendar.getInstance();

      cld.setTime(dt);

      Calendar cld2 = Calendar.getInstance();

      cld2.set(
              cld.get(Calendar.YEAR),
              cld.get(Calendar.MONTH),
              1,
              0,
              0,
              0

      );

      return cld2.getTime();
   }
   
   public static String getQuartalRomawi(Date dtDate) {
      Calendar cld = Calendar.getInstance();

      cld.setTime(dtDate);

      int month = cld.get(Calendar.MONTH)+1;
      
      String quartal = null;
      
      switch (month) {
	  	case 1: quartal = "I"; break;
	  	case 2: quartal = "I"; break;
	  	case 3: quartal = "I"; break;
	  	case 4: quartal = "II"; break;
	  	case 5: quartal = "II"; break;
	  	case 6: quartal = "II"; break;
	  	case 7: quartal = "III"; break;
	  	case 8: quartal = "III"; break;
	  	case 9: quartal = "III"; break;
	  	case 10: quartal = "IV"; break;
	  	case 11: quartal = "IV"; break;
	  	case 12: quartal = "IV"; break;
	  	default: quartal = "0";
	  }
      
      return quartal;
   }

    public static String getDays(Date dtDate) {
        Calendar cld = Calendar.getInstance();
        
        cld.setTime(dtDate);
        
        int days = cld.get(Calendar.DAY_OF_MONTH);
        
        return String.valueOf(days);
    }

    public static String getDays2Digit(Date dtDate) {
        Calendar cld = Calendar.getInstance();

        cld.setTime(dtDate);

        int days = cld.get(Calendar.DAY_OF_MONTH);

        if (days<10) return "0"+days; else return String.valueOf(days);
    }

    public static int dateDiff(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            long hari = ((date2.getTime() - date1.getTime()) / (24 * 3600 * 1000));
            return Math.abs((Integer.parseInt(String.valueOf(hari))));
        }
        return 0;
    }

    public static long daysWithoutWeekend(Date start, Date end){
        //Ignore argument check

        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime(start);
        int w1 = c1.get(Calendar.DAY_OF_WEEK);
        c1.add(Calendar.DAY_OF_WEEK, -w1 + 1);

        Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime(end);
        int w2 = c2.get(Calendar.DAY_OF_WEEK);
        c2.add(Calendar.DAY_OF_WEEK, -w2 + 1);

        //end Saturday to start Saturday
        long days = (c2.getTimeInMillis()-c1.getTimeInMillis())/(1000*60*60*24);
        long daysWithoutSunday = days-(days*2/7);

        if (w1 == Calendar.SUNDAY) {
            w1 = Calendar.MONDAY;
        }
        if (w2 == Calendar.SUNDAY) {
            w2 = Calendar.MONDAY;
        }
        return daysWithoutSunday-w1+w2;
    }

    public static long weekendDays(Date start,Date end){
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        int weekendDays = 0;
        do {
            if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                ++weekendDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        return weekendDays;
    }

    public static int getKelebihanHari(Date startDate, Date endDate){
        DateTime tglawal = new DateTime(startDate);
        DateTime tglakhir = new DateTime(endDate);

        int month = Months.monthsBetween(tglawal, tglakhir).getMonths();
        DateTime tglCek = tglawal.plusMonths(month);

        int kelebihanHari = Days.daysBetween(tglCek, tglakhir).getDays();

        return kelebihanHari;
    }

    public static int getDaysAmount(Date startDate, Date endDate){
        DateTime tglawal = new DateTime(startDate);
        DateTime tglakhir = new DateTime(endDate);

        int days = Days.daysBetween(tglawal, tglakhir).getDays();

        return days;
    }

    public static int getKelebihanBulan(Date startDate, Date endDate){
        DateTime tglawal = new DateTime(startDate);
        DateTime tglakhir = new DateTime(endDate);

        int year = Years.yearsBetween(tglawal, tglakhir).getYears();
        DateTime tglCek = tglawal.plusYears(year);

        int kelebihanBulan = Months.monthsBetween(tglCek, tglakhir).getMonths();

        return kelebihanBulan;
    }

    public static Date advance(Date perDate, int addYear) {

      final Calendar cld = Calendar.getInstance();

      cld.setTime(perDate);

      cld.add(Calendar.YEAR, addYear);

      return cld.getTime();

   }

    public static String getMonth2(int month) {

      String bulanDigit = null;

      switch (month) {
	  	case 1: bulanDigit = "Januari"; break;
	  	case 2: bulanDigit = "Februari"; break;
	  	case 3: bulanDigit = "Maret"; break;
	  	case 4: bulanDigit = "April"; break;
	  	case 5: bulanDigit = "Mei"; break;
	  	case 6: bulanDigit = "Juni"; break;
	  	case 7: bulanDigit = "Juli"; break;
	  	case 8: bulanDigit = "Agustus"; break;
	  	case 9: bulanDigit = "September"; break;
	  	case 10: bulanDigit = "Oktober"; break;
	  	case 11: bulanDigit = "November"; break;
	  	case 12: bulanDigit = "Desember"; break;
	  	default: bulanDigit= "NULL";
	  }
        return bulanDigit;
   }
    
    public static String getMonth1Digit(String month) {
        return String.valueOf(Integer.parseInt(month));
   }

    public static int getYearBetween(Date perStart, Date perEnd){

            DateTime startDate = new DateTime(perStart);
            DateTime endDate = new DateTime(perEnd);

            Years y = Years.yearsBetween(startDate, endDate);

            int year = y.getYears();

            return year;
    }

    public static Age calculateAge(Date birthDate, Date currentDate)
{
   int years = 0;
   int months = 0;
   int days = 0;

   //create calendar object for birth day
   Calendar birthDay = Calendar.getInstance();
   birthDay.setTimeInMillis(birthDate.getTime());

   //create calendar object for current day
   //long currentTime = System.currentTimeMillis();
   //Calendar now = Calendar.getInstance();
   //now.setTimeInMillis(currentTime);

   Calendar now = Calendar.getInstance();
   now.setTimeInMillis(currentDate.getTime());

   //Get difference between years
   years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
   int currMonth = now.get(Calendar.MONTH) + 1;
   int birthMonth = birthDay.get(Calendar.MONTH) + 1;

   //Get difference between months
   months = currMonth - birthMonth;

   //if month difference is in negative then reduce years by one
   //and calculate the number of months.
   if (months < 0)
   {
      years--;
      months = 12 - birthMonth + currMonth;
      if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
         months--;
   } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
   {
      years--;
      months = 11;
   }

   //Calculate the days
   if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
      days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
   else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
   {
      int today = now.get(Calendar.DAY_OF_MONTH);
      now.add(Calendar.MONTH, -1);
      days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
   }
   else
   {
      days = 0;
      if (months == 12)
      {
         years++;
         months = 0;
      }
   }
   //Create new Age object
   return new Age(days, months, years);
}

 public static int getYearsBetweenHUTB(Date startDate, Date endDate) {

	Age usiaHUTB = DateUtil.calculateAge(startDate, endDate);

	logger.logDebug(">>>>>>>>>> hitung tahun = "+ usiaHUTB.getYears() +" Tahun "+ usiaHUTB.getMonths() + " Bulan "+ usiaHUTB.getDays() +" Hari");

	int usia = usiaHUTB.getYears();

	if(usiaHUTB.getMonths()>0 || usiaHUTB.getDays()>0)
		usia = usia+1;

        logger.logDebug("######### lama tahun HUTB = "+ usia);

	return usia;
}

 public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

 public static boolean isBefore(Date dateStart, Date dateEnd){
     DateTime periodStart = new DateTime(dateStart);
     DateTime periodEnd = new DateTime(dateEnd);

     if(periodStart.isBefore(periodEnd))
         return true;

     return false;
 }

 public static boolean isAfter(Date dateObject, Date dateHeader){
     DateTime periodEndObject = new DateTime(dateObject);
     DateTime periodEndHeader = new DateTime(dateHeader);

     if(periodEndObject.isAfter(periodEndHeader))
         return true;

     return false;
 }

 public static Date getEarliestDate(Date dateStart, Date dateEnd){
     DateTime periodStart = new DateTime(dateStart);
     DateTime periodEnd = new DateTime(dateEnd);

     if(periodEnd.isBefore(periodStart))
         return periodEnd.toDate();

     return periodStart.toDate();
 }

public static String getDateJson(Date d) {
      if (d==null) return null;
      return df8.format(d);
   }

public static Date getDateCare(String stDate) {
      try {

          if ((stDate == null) || (stDate.trim().equals("")))
            return null;
         else
            return df5.parse(stDate);

      } catch (ParseException e) {
         throw new RuntimeException(e);
      }
   }

public static Date getFirstDate(Date dtDate){

     DateTime tanggalPalingAwal = new DateTime(dtDate);
     tanggalPalingAwal = tanggalPalingAwal.withDayOfMonth(1);

     return tanggalPalingAwal.toDate();
     
 }

public static int getMonthsBetween(Date dtDate1, Date dtDate2){

     DateTime tanggalAwal = new DateTime(dtDate1);
     DateTime tanggalAkhir = new DateTime(dtDate2);

     int monthsBetween = Months.monthsBetween(tanggalAwal, tanggalAkhir).getMonths();

     return monthsBetween;

 }

public static Date addYear(Date date, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year); //minus number would decrement the days
        return cal.getTime();
    }

public static String getDateStr9(Date d) {
      if (d==null) return null;
      return df9.format(d);
   }

   
}
