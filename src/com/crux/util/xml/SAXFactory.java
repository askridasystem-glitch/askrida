/***********************************************************************
 * Module:  com.crux.util.xml.SAXFactory
 * Author:  Denny Mahendra
 * Created: Jun 23, 2005 3:36:52 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.xml;

import com.crux.common.parameter.Parameter;
import com.crux.util.LogManager;

import javax.xml.parsers.SAXParserFactory;

public class SAXFactory {
   private final static transient LogManager logger = LogManager.getInstance(SAXFactory.class);
   public static SAXParserFactory getSAXFactory() throws Exception {
      String saxname = Parameter.readString("SYS_SAX_PARSER");
      Class cls = Class.forName(saxname);
      try {
         return (SAXParserFactory) cls.getConstructor(null).newInstance(null);
      } catch (Exception e) {
         logger.logDebug("getSAXFactory: failed to use constructor, trying to use newinstance");
         SAXParserFactory theSax = (SAXParserFactory) cls.getMethod("newInstance",null).invoke(null,null);
         return theSax;
      }
   }
}
