/***********************************************************************
 * Module:  com.crux.util.validation.FieldValidator
 * Author:  Denny Mahendra
 * Created: Mar 18, 2004 3:21:13 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.validation;

import com.crux.util.DateUtil;
import com.crux.util.LogManager;

import java.util.Date;
import java.util.HashMap;

public class FieldValidator implements Cloneable {

   private final static transient LogManager logger = LogManager.getInstance(FieldValidator.class);

   private String stFieldID;
   private String stFieldName;
   private String stFieldType;
   private String stDescField;
   private HashMap attributes;
   private int fieldLen;
   private int fieldMinLen = 0;
   private int flags = 0;
   private Object rangeMin;
   private Object rangeMax;

   public String getStDescField() {
      return stDescField;
   }

   public void setStDescField(String stDescField) {
      this.stDescField = stDescField;
   }

   public HashMap getAttributes() {
      return attributes;
   }

   public void setAttributes(HashMap attributes) {
      this.attributes = attributes;
   }

   public Object validate(Object x) {
      return null;
   }

   public Object validate(Object x, int flags) {
      return null;
   }

   public FieldValidator setRange(Object min, Object max) {
      setRangeMin(min);
      setRangeMax(max);
      return this;
   }

   public FieldValidator(String stFieldName, String stFieldType, int fieldLen) {
      this(null,stFieldName, stFieldType,fieldLen);
   }

   public FieldValidator(String stFieldID, String stFieldName, String stFieldType, int fieldLen) {
      this(stFieldID,  stFieldName, stFieldType, fieldLen, 0);
   }

   public FieldValidator(String stFieldID, String stFieldName, String stFieldType, int fieldLen, int flags) {
      this.stFieldID = stFieldID;
      this.stFieldName = stFieldName;
      this.stFieldType = stFieldType;
      this.fieldLen = fieldLen;
      this.flags = flags;
   }

   public String getStFieldID() {
      return stFieldID;
   }

   public void setStFieldID(String stFieldID) {
      this.stFieldID = stFieldID;
   }

   public String getStFieldName() {
      return stFieldName;
   }

   public void setStFieldName(String stFieldName) {
      this.stFieldName = stFieldName;
   }

   public String getStFieldType() {
      return stFieldType;
   }

   public void setStFieldType(String stFieldType) {
      this.stFieldType = stFieldType;
   }

   public int getFieldLen() {
      return fieldLen;
   }

   public void setFieldLen(int fieldLen) {
      this.fieldLen = fieldLen;
   }

   public Object getRangeMin() {
      return rangeMin;
   }

   public FieldValidator setRangeMin(Object rangeMin) {
      this.rangeMin = rangeMin;
      return this;
   }

   public Object getRangeMax() {
      return rangeMax;
   }

   public FieldValidator setRangeMax(Object rangeMax) {
      this.rangeMax = rangeMax;
      return this;
   }

   public FieldValidator setMinLen(int minLen) {
      this.fieldMinLen = minLen;
      return this;
   }

   public int getFlags() {
      return flags;
   }

   public void setFlags(int flags) {
      this.flags = flags;
   }

   public boolean isMandatory() {
      return (flags & Validator.MANDATORY) != 0;
   }

   protected Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public FieldValidator getClone() {
      try {
         return (FieldValidator) clone();
      }
      catch (CloneNotSupportedException e) {
         throw new RuntimeException(e);
      }
   }

   public int getFieldMinLen() {
      return fieldMinLen;
   }

   public void setAttributes(String xattr) {
      if (attributes==null) this.attributes = new HashMap();

      final String[] sz = xattr.split(";");

      for (int i = 0; i < sz.length; i++) {
         String sx = sz[i];

         final String[] sy = sx.split(":");

         if (sy.length>=2) attributes.put(sy[0],sy[1]); else attributes.put(sy[0],null);
      }

      logger.logDebug("setAttributes: "+attributes);

   }
}
