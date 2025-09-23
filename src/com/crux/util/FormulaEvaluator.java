/***********************************************************************
 * Module:  com.crux.util.FormulaEvaluator
 * Author:  Denny Mahendra
 * Created: Feb 12, 2006 8:51:01 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.util.ArrayList;
import java.math.BigDecimal;

public class FormulaEvaluator {

   public final static transient int S_VAR = 1;
   private VariableConnector vc;

   public static interface VariableConnector {
      public Object getVariable(String name);
   }

   public FormulaEvaluator(VariableConnector v) {
      vc = v;
   }

   public static class Token {
      Object value;
      char opr;
      int oprLevel;
      int bracketLevel;

      public String toString() {

         if (value==null) return String.valueOf(opr); else return String.valueOf(value);

         /*if (value==null) return opr+"/"+bracketLevel;
         return value+"/"+bracketLevel;*/
      }
   }

   public Object evaluate(String expression) {

      final char[] exc = (expression+"|").toUpperCase().toCharArray();

      int state =0;

      StringBuffer tokenBuf = new StringBuffer();

      int curBracketLevel=0;
      int maxBracketLevel=0;

      ArrayList exprBuf = new ArrayList() {
         public String toString() {
            final StringBuffer sz = new StringBuffer();
            for (int i = 0; i < this.size(); i++) {
               Object o = (Object) this.get(i);
               if (i>0) sz.append(' ');
               sz.append(o);
            }

            return sz.toString();
         }
      };

      for (int i = 0; i < exc.length; i++) {
         char c = exc[i];

         final boolean kw = ((c>='A') && (c<='Z')) ||
                                ((c>='0') && (c<='9')) ||
                                (c=='.') ||
                                (c=='_');

         final boolean opr =
                 (
                 (c=='+') ||
                 (c=='-') ||
                 (c=='*') ||
                 (c=='/')
                 );

         /*

            2*5+2*(3+(4-6))*8

            2 0 Const
            * 0 Op
            5 0 Const
            + 0 Op
            2 0
            * 0
            1 0
            * 0
            8 0


         */

         while (true){
            if (state==0) {
               if (kw) {
                  state=S_VAR;
                  tokenBuf.append(c);
               }
               else if (opr) {
                  final Token t = new Token();

                  t.opr=c;
                  switch (c) {
                     case '+':
                     case '-':
                        t.oprLevel=0;break;
                     case '*':
                     case '/':
                        t.oprLevel=1;break;
                  }

                  t.bracketLevel = curBracketLevel;
                  exprBuf.add(t);
               }
               else if(c=='(') {
                  curBracketLevel++;
                  maxBracketLevel++;
               }
               else if(c==')') curBracketLevel--;
            }
            else if (state==S_VAR) {
               if (kw) {
                  tokenBuf.append(c);
               } else {
                  final Token t = new Token();

                  t.value = tokenBuf.toString();
                  tokenBuf.setLength(0);

                  t.bracketLevel = curBracketLevel;

                  exprBuf.add(t);

                  state = 0;
                  continue;
               }

            }
            else throw new IllegalStateException("Invalid state");
            break;
         }
      }

      System.out.println(exprBuf);

      /*a0:
      while (true) {
         for (int lv=maxBracketLevel;lv>=0;lv--) {
            for (int i = exprBuf.size()-1; i >=0; i--) {
               Token tk = (Token) exprBuf.get(i);

               final char opr = tk.opr;

               switch(opr) {
                  case '*':
                  case '/':
                     {
                        ((Token)exprBuf.get(i-1)).bracketLevel++;
                        tk.bracketLevel++;
                        ((Token)exprBuf.get(i+1)).bracketLevel++;

                        if (maxBracketLevel<tk.bracketLevel) maxBracketLevel = tk.bracketLevel;
                        //continue a0;
                     }
               }
            }
         }
         break;
      }*/



      if (exprBuf.size()==1) {
         Token tk = (Token) exprBuf.get(0);

         tk.value = load(tk);
         //exprBuf.set(0,load(exprBuf.get(0)));
      }

      a1:
      while (true) {
         System.out.println(exprBuf);
         for (int lv=maxBracketLevel;lv>=0;lv--) {
            for (int ol=2;ol>=0;ol--)
               for (int i = 0; i < exprBuf.size(); i++) {
                  Token tk = (Token) exprBuf.get(i);

                  if (tk.bracketLevel==lv)
                     if (tk.oprLevel == ol)
                        if (tk.value==null){
                           BigDecimal a=load(exprBuf.get(i-1));
                           BigDecimal b=load(exprBuf.get(i+1));

                           BigDecimal r = null;

                           switch (tk.opr) {
                              case '+': r=BDUtil.add(a,b); break;
                              case '-': r=BDUtil.sub(a,b); break;
                              case '*': r=BDUtil.mul(a,b); break;
                              case '/': r=BDUtil.divNR(a,b); break;
                           }

                           exprBuf.remove(i-1);
                           exprBuf.remove(i-1);
                           exprBuf.remove(i-1);

                           final Token t2 = new Token();
                           t2.value = r;
                           t2.bracketLevel = tk.bracketLevel-1;

                           exprBuf.add(i-1,t2);

                           continue a1;
                        }

               }
         }

         break;
      }

      System.out.println(exprBuf);

      final Token tk = (Token) exprBuf.get(0);
      return tk.value;
   }

   private BigDecimal load(Object o) {
      while (true) {
         if (o instanceof Token){
            o = ((Token)o).value;
            continue;
         }
         else if (o instanceof String){
            final String kw = ((String)o).trim();

            final char fc = kw.charAt(0);

            final boolean isdigit = (
                    ((fc>='0') && (fc<='9')) ||
                    (fc=='-')
                    );

            if (isdigit) {
               return new BigDecimal(kw);
            } else {
               o = vc.getVariable(kw);
               continue;
            }
         }
         else if (o instanceof BigDecimal) {
            return (BigDecimal) o;
         }
         break;
      }
      return new BigDecimal("0");
   }

   public static void main(String [] args ) {
      final FormulaEvaluator f = new FormulaEvaluator(

              new FormulaEvaluator.VariableConnector() {
                 public Object getVariable(String name) {
                    //System.out.println("GET:"+name);
                    return null;
                 }
              }
      );

      f.evaluate("(V1+V2+V3)-(V4+V5)");
   }
}
