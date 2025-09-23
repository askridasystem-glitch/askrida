/***********************************************************************
 * Module:  com.crux.web.jstl.FlexFieldTag
 * Author:  Denny Mahendra
 * Created: Apr 28, 2006 10:51:54 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.ff.model.FlexFieldDetailView;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import java.io.IOException;

public class FlexFieldTag extends BaseTag implements BodyTag {
   private String ffid;
   private String prefix;
   private BodyContent bc;

   public String getFfid() {
      return ffid;
   }

   public void setFfid(String ffid) {
      this.ffid = ffid;
   }

   public String getPrefix() {
      return prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public void setBodyContent(BodyContent bodyContent) {
      bc = bodyContent;
   }

   public void doInitBody() throws JspException {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public int doAfterBody() throws JspException {
      return EVAL_BODY_INCLUDE;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public int startTag() throws JspException {
      try {

         return EVAL_BODY_INCLUDE;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private FlexFieldHeaderView getFF() {
      return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, ffid);
   }

   public int endTag() throws JspException {
      try {
         final FlexFieldHeaderView om = getFF();

         if (om!=null) {
            final DTOList details = om.getDetails();

            for (int i = 0; i < details.size(); i++) {
               FlexFieldDetailView mapd = (FlexFieldDetailView) details.get(i);

               final int width = mapd.getLgWidth()==null?100:mapd.getLgWidth().intValue();

               String height = mapd.getLgHeight()==null?null:mapd.getLgHeight().toString();

               final Field fld = new Field();

               fld.setPageContext(pc);

               fld.setName(prefix+mapd.getStFieldRef());
               fld.setCaption(mapd.getStFieldDesc());
               fld.setType(mapd.getStFieldType());
               fld.setWidth(width);
               fld.setRows(height);
               fld.setMandatory(Tools.isYes(mapd.getStMandatoryFlag()));
               fld.setLov(mapd.getStLOV());
               fld.setPresentation("standard");
               fld.setPopuplov(Tools.isYes(mapd.getStLOVPopFlag()));
               fld.setReadonly(Tools.isYes(mapd.getStReadOnlyFlag()));
               fld.setRefresh(Tools.isYes(mapd.getStRefresh()));
               fld.setHidden(Tools.isYes(mapd.getStHidden()));
               
               if(mapd.getStLovLink()!=null){
               	    final LOVLinkTag link = new LOVLinkTag();
               	    link.setPageContext(pc);
               	    link.setName(mapd.getStLovLink());
               		link.setLink(mapd.getStLovLink());
               		link.setClientLink(false);
               		DTOList child = new DTOList();
               		child.add(link);
               		fld.setChilds(child);
               		fld.setLink(false);
                }

               //if(mapd.getStFlexFieldHeaderID().equalsIgnoreCase("OM_KREASI") || mapd.getStFlexFieldHeaderID().equalsIgnoreCase("OM_CREDIT")){
                      if(SessionManager.getInstance().getSession().getStBranch()!=null){
                           if(SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00"))
                                   fld.setHidden(false);
                      }else if(SessionManager.getInstance().getSession().getStBranch()==null){
                                fld.setHidden(false);
                      }
               //}
              
               		
               
               fld.setClientchangeaction(mapd.getStClientChangeAction());

               if (mapd.getStDescFieldRef()!=null)
                  fld.setDescfield(prefix+mapd.getStDescFieldRef());

               fld.startTag();
               fld.endTag();


               //out.print("<c:field name=\""+"selectedObject."+mapd.getStFieldRef()+"\" caption=\""+mapd.getStFieldDesc()+"\" type=\""+mapd.getStFieldType()+"\" width=\""+width+"\" rows=\""+height+"\" mandatory=\""+Tools.isYes(mapd.getStMandatoryFlag())+"\" lov=\""+mapd.getStLOV()+"\"  presentation=\"standard\" />");
            }
         }
         return EVAL_BODY_INCLUDE;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
