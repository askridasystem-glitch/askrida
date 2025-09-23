/***********************************************************************
 * Module:  com.crux.util.LookUpUtil
 * Author:  Denny Mahendra
 * Created: Mar 18, 2004 11:07:30 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import java.util.*;
import java.io.Serializable;

public class LookUpUtil implements Serializable,LOV {

   /**
    * code-value pair storage array
    */
   private Vector codeList = new Vector();
   private boolean bNoNull = false; // if true, [select one] is not displayed for combo boxes
   private boolean bRelaxed = false; // if true, accept null codes returns null values
   public String selectedValue;
   public static String[] attributes = new String[] {};
   private String nullText;

   public LookUpUtil() {

   }

   public LookUpUtil(LookUpUtil lu) {
      this.codeList = (Vector) lu.codeList.clone();
   }

   /**
    * Use this constructor to create code-value pairs from a '|' delimited string
    * @param stCodeList
    */
   public LookUpUtil(String stCodeList) {
      this(stCodeList, "|");
   }

   /**
    * Use this constructor to create code-value pairs from a delimited string
    * @param stCodeList
    */
   public LookUpUtil(String stCodeList,String delim) {
      StringTokenizer stkCodeList = new StringTokenizer(stCodeList,delim);

      while (stkCodeList.hasMoreElements()) {
         codeList.add(((String)stkCodeList.nextElement()).trim().toUpperCase());
         codeList.add(stkCodeList.nextElement());
      }
   }

   /**
    * retrieve the code based on given index
    * @param iIndex
    * @return
    */
   public String getCode(int iIndex) throws IllegalArgumentException {
      return (String) codeList.get(iIndex*2);
   }

   /**
    * invoke this method to search a \value from its code
    * @param stCode
    * @return
    */
   public Object getValue(String stCode,int iValueIndex) throws IllegalArgumentException {

      if ((stCode != null) && (!stCode.equals(""))) {
         stCode = stCode.trim();

         for (int i=0;i<codeList.size()/2;i++) {
            String cd = (String)codeList.get(i*2);

            if (cd.equalsIgnoreCase(stCode)) {
               Object val = codeList.get(i*2+1);

               if (val instanceof Object[])
                  val=((Object[])val)[iValueIndex];

               return val;
            }
         }
      }
      else if (bRelaxed) {
         return null;
      }

      return null;

      //throw new IllegalArgumentException("Code not found : "+stCode);
   }

   public int getIndex(String stCode) throws IllegalArgumentException {

      if ((stCode != null) && (!stCode.equals(""))) {
         stCode = stCode.trim();

         for (int i=0;i<codeList.size()/2;i++) {
            String cd = (String)codeList.get(i*2);

            if (cd.equalsIgnoreCase(stCode)) {
               return i;
            }
         }
      }
      else if (bRelaxed) {
         return -1;
      }

      throw new IllegalArgumentException("Code not found : "+stCode);

      //throw new IllegalArgumentException("Code not found : "+stCode);
   }

   /**
    * Get value from Code, for S_CODE_DECODE: get CODE_TEXT from CODE_VALUE
    * @param stCode
    * @return
    */
   public Object getValue(Object stCode) throws IllegalArgumentException {
      return getValue((String)stCode,0);
   }

   public Object getValue(int iIndex) {
      return codeList.get(iIndex*2+1);
   }

   /**
    * get the first field (description)
    * @return
    * @throws Exception
    */
   public Object getField() throws Exception {
      return getField((String) codeList.get(0));
   }

   /**
    * get field from Code, for S_CODE_DECODE: get CODE_DESCRIPTION from CODE_VALUE
    * @param stCode
    * @return
    */
   public Object getField(String stCode) throws IllegalArgumentException {
      return getValue(stCode,1);
   }

   /**
    * get field from Code, for S_CODE_DECODE: get CODE_DESCRIPTION from CODE_VALUE
    * @param stCode
    * @return
    * @throws IllegalArgumentException
    */
   public Object getDescription(String stCode) throws IllegalArgumentException {
      return getValue(stCode,1);
   }

   /**
    * adds new entry
    * @param stID
    * @param oValue
    */
   public LookUpUtil add(String stID, Object oValue) {
   //updated by Agung Aditya Kristiawan on July 18,2003
      //codeKeys.add(stID);
      codeList.add(stID);
      codeList.add(oValue);

      return this;
   }

   public Enumeration getCodes(){
      return new CodeEnumerator();
       //return codeKeys.elements();
   }

   private class CodeEnumerator implements Enumeration, Serializable {
      int iCurrentIndex = 0;

      public boolean hasMoreElements() {
         return LookUpUtil.this.codeList.size() > iCurrentIndex;
      }

      public Object nextElement() {
         final Object o = LookUpUtil.this.codeList.get(iCurrentIndex);
         iCurrentIndex += 2;
         return o;
      }

      public String toString() {
         final StringBuffer sz = new StringBuffer("[");

         while (hasMoreElements()) {
            sz.append(nextElement());
            if (hasMoreElements()) sz.append(", ");
         }

         sz.append("]");

         return sz.toString();
      }
   }

   /**
    * call this if you dont want [select one] appear in the LOV
    * @return
    */
   public LOV setNoNull() {
      bNoNull = true;

      return this;
   }

   public LOV setNullText(String s) {
      nullText = s;
      return this;
   }

   /**
    * call this if you dont want exception throwns upon null codes
    * @return
    */
   public LookUpUtil setRelaxed() {
      bRelaxed = true;
      return this;
   }

   public int size() {
      return codeList.size()/2;
   }

   public static String getSQLEscapedLiteral(String st) {
      if (st == null) return null;

      if (st.length() == 0) return st;

      if (
            (st.indexOf('&') < 0) &&
            (st.indexOf('\'') < 0)
      )
         return st;

      final char[] sta = st.toCharArray();

      final StringBuffer osz = new StringBuffer();

      char c;

      for (int i = 0; i < sta.length; i++) {
         c = sta[i];

         switch (c) {
            case '&': osz.append("\\&");break;
            case '\'': osz.append("''");break;
            default:
               osz.append(c);
         }
      }

      return osz.toString();
   }

   /**
    * Generate String of ORACLE DECODE function, based on certain Code Group that is needed
    * to decode a field in SQL syntax
    * Format of Result is :
    * "DECODE( FIELD_NAME, field_value1, decode_value1,  field_value1, decode_value1, ....,  field_valueN, decode_valueN, '' ) FIELD_ALIAS "
    * @param stFieldName - field name that will be decoded
    * @param stFieldAlias - field alias
    * @return
    */
   public String getOracleDecodeFunctionStr( String stFieldName, String stFieldAlias )
   {
      StringBuffer sbResult = new StringBuffer();

      sbResult.append( "DECODE( " ).append( stFieldName ).append( ", " );
      if ( size() > 0 )
      {
         int iSize = size();
         for( int i = 0; i < iSize; i++ ){
            String stCode  = getCode( i );
            String stValue = getSQLEscapedLiteral( ( String ) getValue( stCode ) );
            sbResult.append( "'" ).append( stCode ).append( "', " )
                    .append( "'" ).append( stValue ).append( "', " );
         }
         sbResult.append("'' " );
      }
      else
      {
         sbResult.append( "'', '', '' " );
      }
      sbResult.append(" ) ").append( stFieldAlias ).append( " " );
      return sbResult.toString();
   }

   /**
    * get a subset of code/value by given array of codes
    * @param stCodes
    * @return
    */
   public LookUpUtil getSubSetOf(String [] stCodes) {
      final LookUpUtil lu = new LookUpUtil();

      for (int i = 0; i < stCodes.length; i++) {
         String s = stCodes[i];

         lu.add(s, getValue(s));
      }

      return lu;
   }

   public void delete(String stCode) {
      for (int i = 0; i < codeList.size()/2; i++) {
         String s = (String) codeList.elementAt(i*2);

         if (s.equals(stCode)) {
            codeList.remove(i*2);
            codeList.remove(i*2);
            break;
         }
      }
   }

   public String getXML() {
      final StringBuffer sz = new StringBuffer();

      sz.append("<DATA-LIST>");
      for (int i = 0; i < codeList.size()/2; i++) {
         sz.append("<DATA>");

         String cd = (String) codeList.elementAt(i*2);

         sz.append("<ID>").append(cd).append("</ID>");

         final Object o = codeList.elementAt(i*2+1);

         if (o instanceof String []) {
            final String [] cdt = (String []) o;
            sz.append("<VALUE>").append(cdt[0]).append("</VALUE>");
            sz.append("<DESC>").append(cdt[1]).append("</DESC>");
         } else {
            sz.append("<VALUE>").append(String.valueOf(o)).append("</VALUE>");
         }

         sz.append("</DATA>");
      }

      sz.append("</DATA-LIST>");

      return sz.toString();
   }

   public String getComboContent(String stDefaultValue) {
      StringBuffer sb = new StringBuffer();

      //System.out.println("enter getComboContent: " + stDefaultValue);


      if (!bNoNull)
         sb.append("<OPTION VALUE=\"\">[").append(nullText==null?"select one":nullText).append("]</OPTION>");

      for (int i=0;i<codeList.size()/2;i++) {

         Object sCDText = codeList.get(i*2+1);

         if (sCDText instanceof Object [])
            sCDText = ((Object [])sCDText)[0];

         sb.append("<OPTION "+(((String)codeList.get(i*2)).equalsIgnoreCase(stDefaultValue)?"SELECTED":" ")+" VALUE=\"" + ((String) codeList.get(i*2)) + "\">" + sCDText + "</OPTION>");
      }
      //System.out.println(sb.toString());

      return sb.toString();
   }

   public String getComboDesc(String stValue) {
      return (String) getValue(stValue);
   }

   public LOV setLOValue(String stValue) {
      selectedValue = stValue;
      return this;
   }

   public String getLOValue() {
      return selectedValue;
   }

   public String getComboContent() throws Exception {
      return getComboContent(selectedValue);
   }

   public String getComboDesc() throws Exception {
      return getComboDesc(selectedValue);
   }

   public Iterator getCodeIterator() {
      return getIterator();
   }

   public Iterator getIterator() {
      return new Iterator() {
         public void remove() {

         }

         public boolean hasNext() {
            return LookUpUtil.this.codeList.size() > iCurrentIndex;
         }

         public Object next() {
            final Object o = LookUpUtil.this.codeList.get(iCurrentIndex);
            iCurrentIndex += 2;
            return o;
         }

         int iCurrentIndex = 0;
      };
   }

   public String getJSObject() throws Exception {
      StringBuffer sb = new StringBuffer();

      //System.out.println("enter getComboContent: " + stDefaultValue);

      sb.append('[');

      if (!bNoNull)
         sb.append("{text:\"[").append(nullText==null?"select one":nullText).append("]\",value:\"\"}");

      for (int i=0;i<codeList.size()/2;i++) {

         Object sCDText = codeList.get(i*2+1);

         if (sCDText instanceof Object [])
            sCDText = ((Object [])sCDText)[0];

         final String code = (String)codeList.get(i*2);
         final String text = (String) codeList.get(i*2+1);

         if (sb.length()>1) sb.append(",\n");
         sb.append("{text:\""+text+"\",value:\""+code+"\"}");
      }
      //System.out.println(sb.toString());

      sb.append(']');

      return sb.toString();
   }

   public String[] getAttributeNames() {
      return attributes;
   }
}
