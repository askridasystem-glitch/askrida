/***********************************************************************
 * Module:  com.crux.util.CharRenderer
 * Author:  Denny Mahendra
 * Created: Feb 11, 2006 9:53:36 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.ArrayList;

public class CharRenderer {
   private ArrayList scr = new ArrayList();
   private int currentline=-1;

   private final static transient LogManager logger = LogManager.getInstance(CharRenderer.class);

   public CharRenderer() {
      newLine();
   }

   public void print(Long position, String txt) {

      final char[] cl = (char[]) scr.get(currentline);

      txt.getChars(0,txt.length(), cl, position.intValue());
   }

   public void newLine() {
      if (currentline>=0)
         logger.logDebug("newLine: "+getLine(currentline));
      currentline++;

      if(scr.size()<currentline+1) {
         final char[] ln = new char[400];
         scr.add(ln);
      }

      final char[] cl = (char[]) scr.get(currentline);

      for (int i = 0; i < cl.length; i++) {
         cl[i] = ' ';
      }
   }

   public int getLineCount() {

      return currentline;
   }

   public String getLine(int lc) {
      return new String((char[]) scr.get(lc));
   }
}
