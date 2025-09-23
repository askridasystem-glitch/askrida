/*
 * Main.java
 *
 * Created on July 10, 2012, 8:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.webfin;

import java.io.*;

public class Main {
    
    public static void main(String args[]) {
        
        try {
            Runtime rt = Runtime.getRuntime();
            //Process pr = rt.exec("cmd /c dir");
            //Process pr = rt.exec("c:\\helloworld.exe");
            Process pr2 = rt.exec("e:\\FOXPRO2\\FOXPROL.EXE");
            
            
            
            
            int exitVal = pr2.waitFor();
            System.out.println("Exited with error code "+exitVal);
            
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}