/***********************************************************************
 * Module:  com.crux.util.xml.XMLEncoder
 * Author:  Denny Mahendra
 * Created: Jun 7, 2004 9:26:07 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util.xml;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOCache;
import com.crux.util.DTOField;
import com.crux.util.DTOList;
import com.crux.util.LogManager;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class XMLEncoder {
   private OutputStream os;
   private OutputStreamBuffer sz;

   private final static transient LogManager logger = LogManager.getInstance(XMLEncoder.class);

   public XMLEncoder(OutputStream os) throws Exception {
      this.os = os;
      logger.logDebug("XMLEncoder: starting to encode ...");
      sz = new OutputStreamBuffer(os);
      sz.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
   }

   public void encode(Object o) throws Exception {
      if (o==null) return;

           if (o instanceof DTO) encode((DTO) o);
      else if (o instanceof DTOList) encode((DTOList) o);
      else throw new RuntimeException("Unknown Object type : "+o.getClass().getName());
   }

   public String encodeValue(Object o) {
      if (o==null) return "";
           if (o instanceof Date) return encodeValue((Date) o);
      else if (o instanceof String) return encodeValue((String) o);
      else if (o instanceof BigDecimal) return encodeValue((BigDecimal) o);
      else if (o instanceof Long) return encodeValue((Long) o);
      else throw new RuntimeException("Unknown Object type : "+o.getClass().getName());
   }

   public String encodeValue(Date dt) {
      if (dt==null) return "";
      return "DT"+String.valueOf(dt.getTime());
   }

   public String encodeValue(String st) {
      if (st==null) return "";
	   return "ST"+ st;
   }

   public String encodeValue(BigDecimal bd) {
      if (bd==null) return "";
      return "BD"+bd.toString();
   }

   public String encodeValue(Long lg) {
      if (lg==null) return "";
      return "LG"+lg.toString();
   }

   public void encode(DTOList l) throws Exception {
      l = DTOList.getDTOListFromProxy(l);

      sz
            .append("<dtolist length=\"")
            .append(String.valueOf(l.size()))
            .append("\" totalrows=\"")
            .append(l.getTotalRows())
            .append("\" currentpage=\"")
            .append(l.getCurrentPage()+"\"");

      final HashMap attr = l.getAttributes();

      if ((attr==null) || (attr.size()==0)) throw new RuntimeException("Attributes not found");

      if (attr != null) {
         final Iterator keys = attr.keySet().iterator();

         while (keys.hasNext()) {
            String key = (String) keys.next();
            sz.append(" at").append(key).append("=\"").append(encodeValue(attr.get(key))).append("\"");
         }
      }

      sz.append(">\n");

      /*for (int i = 0; i < l.size(); i++) {
         Object o = l.get(i);
         encode(o);
      }*/

      final Iterator it = l.iterator();

      int c=0;

      if (l.size()>0)
         logger.logDebug("encode: laZy mode not activated (size="+l.size()+")!");

      while (it.hasNext()) {
         Object o = (Object) it.next();
         encode(o);
         c++;
      }

      logger.logDebug("encode: retrieved "+c+" records");

      sz.append("</dtolist>\n");

      l.release();
   }

   public void encode(DTO dto) throws Exception {
      try {
         sz.append("<dto class=\"").append(dto.getClass().getName()).append("\">");

         final HashMap flds = DTOCache.getInstance().getFields(dto.getClass());

         final Iterator vals = flds.values().iterator();

         Class ft = null;

         while (vals.hasNext()) {
            DTOField fld = (DTOField) vals.next();

            Object val = fld.getGetter().invoke(dto,null);


            ft = fld.getFieldType();

            if (
                  (ft.equals(DTO.class)) || (ft.equals(DTOList.class))
            ) {
               sz.append("<fld_").append(fld.getStDatabaseFieldName()).append(">");
               encode(val);
               sz.append("</fld_").append(fld.getStDatabaseFieldName()).append(">");
            } else {
               sz.append("<fld_").append(fld.getStDatabaseFieldName());
               if(val instanceof String)
                  sz.append("><![CDATA[").append(encodeValue(val)).append("]]></fld_").append(fld.getStDatabaseFieldName()).append(">");
               else
                  sz.append(" val=\""+encodeValue(val)+"\"/>");
            }

         }
	      if (dto.isEnableStoreSync() && (dto instanceof RecordAudit)) {
	         sz.append("<fld_create_date val=\"").append(encodeValue(dto.getDtCreateDate())).append("\"/>");
	         sz.append("<fld_create_who val=\"").append(encodeValue(dto.getStCreateWho())).append("\"/>");
	         sz.append("<fld_change_date val=\"").append(encodeValue(dto.getDtChangeDate())).append("\"/>");
	         sz.append("<fld_change_who val=\"").append(encodeValue(dto.getStChangeWho())).append("\"/>");
	      }

         sz.append("</dto>\n");
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
      catch (InvocationTargetException e) {
         throw new RuntimeException(e.getTargetException());
      }
   }

}
