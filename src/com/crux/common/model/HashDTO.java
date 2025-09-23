/***********************************************************************
 * Module:  com.crux.common.model.HashDTO
 * Author:  Denny Mahendra
 * Created: Nov 21, 2004 7:35:14 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import com.crux.util.DTOList;

import java.util.HashMap;
import java.util.Date;
import java.util.Set;
import java.math.BigDecimal;

public class HashDTO extends DTO {

   private HashMap m = new HashMap();

   public Object setFieldValueByFieldName(String stFieldName, Object value) {
      m.put(stFieldName.toLowerCase(), value);
      return value;
   }

   public Object getFieldValueByFieldName(String stFieldName) {
      return m.get(stFieldName.toLowerCase());
   }

   public BigDecimal getFieldValueByFieldNameBD(String stFieldName) {
      return (BigDecimal)m.get(stFieldName.toLowerCase());
   }

   public String getFieldValueByFieldNameST(String stFieldName) {
      return (String)m.get(stFieldName.toLowerCase());
   }

   public Date getFieldValueByFieldNameDT(String stFieldName) {
      return (Date)m.get(stFieldName.toLowerCase());
   }

   public static HashMap getMapOf(DTOList l1, String s) {

      HashMap m = new HashMap();

      for (int i = 0; i < l1.size(); i++) {
         HashDTO d = (HashDTO) l1.get(i);

         m.put(d.getFieldValueByFieldName(s), d);
      }

      return m;
   }

   public Set getKeys() {
      return m.keySet();
   }
}
