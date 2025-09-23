/*
 * MacAddress.java
 *
 * Created on March 7, 2012, 8:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.crux.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacAddress {
    public static void main(String[] args) throws Exception{
        try {
            System.out.println(obtainMacAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public static String obtainMacAddress()
throws Exception {
        String theGoodMac = null;
        try{
            Process aProc = Runtime.getRuntime().exec("ipconfig /all");
    InputStream procOut = new DataInputStream(aProc.getInputStream());
    BufferedReader br = new BufferedReader(new InputStreamReader(procOut));

    String aMacAddress = "((\\p{XDigit}\\p{XDigit}-){5}\\p{XDigit}\\p{XDigit})";
    Pattern aPatternMac = Pattern.compile(aMacAddress);
    String aIpAddress = ".*IP.*: (([0-9]*\\.){3}[0-9]).*$";
    Pattern aPatternIp = Pattern.compile(aIpAddress);
    String aNewAdaptor = "[A-Z].*$";
    Pattern aPatternNewAdaptor = Pattern.compile(aNewAdaptor);

    // locate first MAC address that has IP address
    boolean zFoundMac = false;
    boolean zFoundIp = false;
    String foundMac = null;
    

    String strLine;
    while (((strLine = br.readLine()) != null) && !(zFoundIp && zFoundMac)) {
        Matcher aMatcherNewAdaptor = aPatternNewAdaptor.matcher(strLine);
        if (aMatcherNewAdaptor.matches()) {
            zFoundMac = zFoundIp = false;
        }
        Matcher aMatcherMac = aPatternMac.matcher(strLine);
        if (aMatcherMac.find()) {
            foundMac = aMatcherMac.group(0);
            zFoundMac = true;
        }
        Matcher aMatcherIp = aPatternIp.matcher(strLine);
        if (aMatcherIp.matches()) {
            zFoundIp = true;
            if(zFoundMac && (theGoodMac == null)) theGoodMac = foundMac;
        }
    }

    aProc.destroy();
    aProc.waitFor();

    
        }catch(Exception e){
            
        }
    return theGoodMac;
}

}