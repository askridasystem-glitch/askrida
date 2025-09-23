/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.crux.util;

/**
 *
 * @author doni
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;


public class VerifyRecaptcha {

    public static final String url = "https://www.google.com/recaptcha/api/siteverify";
    public static final String secret = "6LdvtB8TAAAAAJNM5xSPZdZN1w1R5D5CIzUDMGOY";
    private final static String USER_AGENT = "Mozilla/5.0";

    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        try{
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String postParams = "secret=" + secret + "&response="
                + gRecaptchaResponse;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

        //parse JSON response and return 'success' value
        String result = response.toString();
        JSONArray ja = new JSONArray();
        ja.add(result);
        JSONObject jo = ja.getJSONObject(0);


        boolean verifyjs = jo.getBoolean("success");
            
        return verifyjs;

 
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {


    String jsonString  = "{  \"success\": true,  \"challenge_ts\": \"2016-05-13T08:01:51Z\",  \"hostname\": \"localhost\"}";

    JSONArray ja = new JSONArray();
    ja.add(jsonString);
    JSONObject jo = ja.getJSONObject(0);
   

    boolean verifyjs = jo.getBoolean("success");
    String chalenge  = jo.getString("challenge_ts");

    System.out.println("verify : "+ verifyjs);
    System.out.println("challenge_ts : "+ chalenge);
}


}
