/***********************************************************************
 * Module:  com.crux.util.xml.XMLDecoder
 * Author:  Denny Mahendra
 * Created: Jun 7, 2004 9:51:27 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util.xml;

import com.crux.common.model.DTO;
import com.crux.util.DTOCache;
import com.crux.util.DTOField;
import com.crux.util.DTOList;
import com.crux.util.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class XMLDecoder {

	private final static transient LogManager logger = LogManager.getInstance(XMLDecoder.class);

	public static Object decode(String stXML) throws Exception {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		final DocumentBuilder db = dbf.newDocumentBuilder();

		final Document doc = db.parse(new ByteArrayInputStream(stXML.getBytes()));

		final Element root = doc.getDocumentElement();

		return decode(root);
	}

	public static Object decodeByFilename(String filename) throws Exception {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		final DocumentBuilder db = dbf.newDocumentBuilder();

		final Document doc = db.parse(filename);

		final Element root = doc.getDocumentElement();

		return decode(root);
	}


	private static Object decode(Node root) throws Exception {
		if ("dtolist".equalsIgnoreCase(root.getNodeName()))
			return decodeDTOList(root);
		else if ("dto".equalsIgnoreCase(root.getNodeName()))
			return decodeDTO(root);
      else if ("#text".equalsIgnoreCase(root.getNodeName()))
         return null;
      else
			logger.logDebug("decode: Unknown Tag : " + root.getNodeName()+" val = "+root.getNodeValue());
			//throw new Exception("Unknown Tag : " + root.getNodeName()+" val = "+root.getNodeValue());
      return null;
	}

	public static Object decodeValue(String o) {
		if (o.length() < 3) return null;

		final String dt = o.substring(0, 2);
		o = o.substring(2, o.length());

		if ("LG".equalsIgnoreCase(dt))
			return new Long(o);
		else if ("BD".equalsIgnoreCase(dt))
			return new BigDecimal(o);
		else if ("ST".equalsIgnoreCase(dt))
			return o;
		else if ("DT".equalsIgnoreCase(dt))
			return new Date(Long.parseLong(o));
		else {
			logger.logWarning("Unknown data type : [" + dt + "]:" + o);
			return o;
		}
	}

	private static DTO decodeDTO(Node root) throws Exception {

		final String stClassName = root.getAttributes().getNamedItem("class").getNodeValue();

		final Class cl = Class.forName(stClassName);

		final DTO o = (DTO) cl.newInstance();

		final HashMap fields = DTOCache.getInstance().getFields(cl);

		final NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);

         if (node.getNodeName().indexOf("fld_") == 0) {
				final String nodeName = node.getNodeName();

				final String stFieldName = nodeName.substring(4, nodeName.length());

				Object val;
				Node valNode = node.getAttributes().getNamedItem("val");
				if (valNode != null) {
					val = valNode.getNodeValue();
				} else {
               final Node fc = node.getFirstChild();
               val = fc!=null?fc.getNodeValue():null;
				}

				if ("create_date".equalsIgnoreCase(stFieldName))
					o.setDtCreateDate((Date) decodeValue((String) val));
				else if ("create_who".equalsIgnoreCase(stFieldName))
					o.setStCreateWho((String) decodeValue((String) val));
				else if ("change_date".equalsIgnoreCase(stFieldName))
					o.setDtChangeDate((Date) decodeValue((String) val));
				else if ("change_who".equalsIgnoreCase(stFieldName))
					o.setStChangeWho((String) decodeValue((String) val));
				else {
					final DTOField fld = (DTOField) fields.get(stFieldName);

					//final Class ft = fld.getFieldType();

					//logger.logDebug("decodeDTO: "+stFieldName+"="+val);

					/*if ("".equalsIgnoreCase((String)val)) val=null;

					if(val != null) {
					   if (ft.equals(String.class));
					else if (ft.equals(Date.class)) val = decodeDate((String)val);
					else if (ft.equals(BigDecimal.class)) val = new BigDecimal((String) val);
					else if (ft.equals(Long.class)) val = new Long((String) val);
					else if (ft.equals(Integer.class)) val = new Integer((String) val);
					else throw new Exception("Unrecognized field type : "+ft.getClass().getName());
					}*/

               final Class fldType = fld.getFieldType();

               if (
                     (fldType.equals(DTO.class)) ||
                     (fldType.equals(DTOList.class))
               ) {
                  final NodeList nl2 = node.getChildNodes();
                  for (int k=0;k<nl2.getLength();k++) {
                     final Node nd2 = nl2.item(k);
                     if (!"#text".equalsIgnoreCase(nd2.getNodeName())) {
                        fld.getSetter().invoke(o, new Object[]{decode(nd2)});
                        break;
                     }
                  }
               } else {

                  if (fld != null) {
                     fld.getSetter().invoke(o, new Object[]{decodeValue((String) val)});
                  } else {
                     logger.logDebug("Field " + stFieldName + " in class " + stClassName + " not found.");
                  }

               }

				}
			}
         else if (node.getNodeName().indexOf("#text") == 0) {

         }
         else
				throw new Exception("Unrecognized node : " + node.getNodeName());
		}

		return o;
	}

	private static Date decodeDate(String a) {
		if (a == null) return null;
		return new Date(Long.parseLong(a));
	}

	private static DTOList decodeDTOList(Node root) throws Exception {
		final NodeList nodes = root.getChildNodes();

		final NamedNodeMap attrs = root.getAttributes();

		final DTOList l = new DTOList();

		for (int i = 0; i < attrs.getLength(); i++) {
			final Node at = attrs.item(i);
			String n = at.getNodeName();

			if ("totalrows".equalsIgnoreCase(n)) {
				l.setTotalRows(Integer.parseInt(at.getNodeValue()));
			} else if ("currentpage".equalsIgnoreCase(n)) {
				l.setTotalRows(Integer.parseInt(at.getNodeValue()));
			} else if (n.indexOf("at") == 0) {
				n = n.substring(2, n.length());
				l.setAttribute(n, decodeValue(at.getNodeValue()));
			}
		}

		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);
         final Object theNode = decode(node);
         if (theNode!=null)
            l.add(theNode);
		}

		return l;
	}

   public static void main(String [] args) throws Exception {
      XMLDecoder.decodeByFilename(args[0]);
   }
}
