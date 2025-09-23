/***********************************************************************
 * Module:  com.crux.lang.LanguageManager
 * Author:  Denny Mahendra
 * Created: Aug 18, 2006 5:03:16 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.lang;

import com.crux.common.filter.LanguageFilter;
import com.crux.common.parameter.Parameter;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.ThreadContext;

import java.util.Stack;

public class LanguageManager {
   private static LanguageManager staticinstance;
   private String sysLang;

   public static LanguageManager getInstance() {
      if (staticinstance == null) staticinstance = new LanguageManager();
      return staticinstance;
   }

   private LanguageManager() {
      sysLang = Parameter.readString("SYS_DEFAULT_LANG");
   }

   public String getSysLang() {
      return sysLang;
   }

   public String getActiveLang() {
      return (String) ThreadContext.getInstance().get("SYS_LANG");
   }

   public DTOList getLanguages() throws Exception {
      return ListUtil.getDTOListFromQuery("select * from s_lang order by lang_order,lang_name",
              LanguageView.class);
   }

   public String translate(String x) {

      if(x==null) return null;

      if (x.indexOf("{L-")<0) return x;

      Stack p = new Stack();

      final char[] buf = x.toCharArray();

      final StringBuffer szo = new StringBuffer();

      Token active = null;

      for (int i = 0; i < buf.length; i++) {
         final char c = buf[i];

         if (
                 (c == '{') &&
                 (i + 8 < buf.length) &&
                 (buf[i + 1] == 'L') &&
                 (buf[i + 2] == '-')
         ) {

            final Token token = new Token();

            active = token;

            token.pos = i;

            token.lang = new String(buf, i + 3, 3);

            String effectiveLanguage = (String) ThreadContext.getInstance().get("SYS_LANG");

            if (effectiveLanguage == null) effectiveLanguage = LanguageFilter.getSysLanguage();

            token.display = (token.lang.equalsIgnoreCase(effectiveLanguage));

            i += 5;

            p.push(token);
         } else if (
                 (c == '-') &&
                 (i + 3 < buf.length) &&
                 (buf[i + 1] == 'L') &&
                 (buf[i + 2] == '}')
         ) {
            final Token top = (Token) p.pop();

            i += 2;

            active = p.empty() ? null : (Token) p.peek();

         } else {

            if (active == null || active.display) {

               szo.append(c);

            }

         }

      }
	  
      String result;
      result = szo.toString();
      if(result.endsWith("-L}")){
      	int a = result.indexOf("-L}");
      	result = result.substring(0,a);
      }
            
      return result;
   }
   
   public String translate(String x, String language) {

      if(x==null) return null;

      if (x.indexOf("{L-")<0) return x;

      Stack p = new Stack();

      final char[] buf = x.toCharArray();

      final StringBuffer szo = new StringBuffer();

      Token active = null;

      for (int i = 0; i < buf.length; i++) {
         final char c = buf[i];

         if (
                 (c == '{') &&
                 (i + 8 < buf.length) &&
                 (buf[i + 1] == 'L') &&
                 (buf[i + 2] == '-')
         ) {

            final Token token = new Token();

            active = token;

            token.pos = i;

            token.lang = new String(buf, i + 3, 3);

            String effectiveLanguage = language;

            if (effectiveLanguage == null) effectiveLanguage = LanguageFilter.getSysLanguage();

            token.display = (token.lang.equalsIgnoreCase(effectiveLanguage));

            i += 5;

            p.push(token);

         } else if (
                 (c == '-') &&
                 (i + 3 < buf.length) &&
                 (buf[i + 1] == 'L') &&
                 (buf[i + 2] == '}')
         ) {
            final Token top = (Token) p.pop();

            i += 2;

            active = p.empty() ? null : (Token) p.peek();

         } else {

            if (active == null || active.display) {

               szo.append(c);
            }
         }

      }
      
      String result;
      result = szo.toString();
      if(result.endsWith("-L}")){
      	int a = result.indexOf("-L}");
      	result = result.substring(0,a);
      }
            
      return result;
   }


   public static class Token {
      public int pos;
      public String lang;
      public boolean display;
   }
   
   /*
   public static void main(String []args) throws Exception{
   		System.out.println(translate("{L-ENGProduction Report-L}{L-INALaporan Produksi-L}"));
   }*/

}

