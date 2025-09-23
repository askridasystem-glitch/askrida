/***********************************************************************
 * Module:  com.crux.web.jstl.FrameTag
 * Author:  Denny Mahendra
 * Created: May 18, 2005 11:06:37 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.web.form.Form;
import com.crux.util.JSPUtil;
import com.crux.util.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrameTag extends BaseTag {
   private String title;
   private Form form;
   private PageContext ctx;
   private String path;
   private String flags;
   private boolean nobody = false;
   public JSPUtil jspUtil;
   private boolean autoRefresh = false;

   private final static transient LogManager logger = LogManager.getInstance(FrameTag.class);

   public void setFlags(String flags) {
      this.flags = flags;
      if (flags!=null) {
         final String[] fl = flags.split(",");

         for (int i = 0; i < fl.length; i++) {
            String s = fl[i];
            if ("nobody".equalsIgnoreCase(s)) nobody=true;
         }
      }
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);
      ctx= pageContext;
      path = ((HttpServletRequest)ctx.getRequest()).getContextPath();
      form = (Form) pageContext.getRequest().getAttribute("FORM");
      //logger.logDebug("setPageContext: FORM = "+form);

      ctx.setAttribute("frame",this);
      ctx.setAttribute("form",form);

      this.title=null;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int startTag() throws JspException {
      super.clearTagStack();
      super.push();
      try {
         form.disableProperties();
         if (form.getTitle()!=null) title=form.getTitle();
         if (title==null) title="Untitled";
         if (form.isAutoRefresh()) autoRefresh=true;

         if (form.isUseHeader())
            jspUtil = new JSPUtil((HttpServletRequest)ctx.getRequest(),(HttpServletResponse)ctx.getResponse(), title, autoRefresh,true);
         else
             jspUtil = new JSPUtil((HttpServletRequest)ctx.getRequest(),(HttpServletResponse)ctx.getResponse(), title, autoRefresh,false);

         ctx.setAttribute("jspUtil",jspUtil);
         out.println("<input type=hidden name=scrollTop id=scrollTop value="+jspUtil.print(form.getScrollTop())+">");
         out.print("<script>");
         out.println(" document.body.onscroll=function() {f.scrollTop.value=document.body.scrollTop;};");
         out.println("document.body.onload=function() {document.body.scrollTop=f.scrollTop.value}");
         out.print(" f.action='"+form.getFormMeta().getStName()+".crux';");
         out.print(" mform=f;");
         out.print("</script>");
         out.println("<input type=hidden name=action_event id=action_event>");
         out.println("<input type=hidden name=defaultArg1 id=defaultArg1>");
         out.println("<input type=hidden name=defaultArg2 id=defaultArg2>");
         out.println("<input type=hidden name=defaultArg3 id=defaultArg3>");
         out.println("<input type=hidden name=defaultArg4 id=defaultArg4>");
         out.println("<input type=hidden name=formid id=formid value=\""+form.getFormID()+"\">");
         if (form.getStAlert()!=null) {
            out.println("<script>window.alert('"+JSPUtil.jsEscape(form.getStAlert())+"')</script>");
         }

         if (form.getStErrorTicket()!=null) {
            out.println("<script>openDialog('error.crux?ticket="+form.getStErrorTicket()+"',400,200,null)</script>");
            form.setStErrorTicket(null);
         }

         form.enableProperty("defaultArg1");
         form.enableProperty("defaultArg2");
         form.enableProperty("defaultArg3");
         form.enableProperty("defaultArg4");
         form.enableProperty("scrollTop");
         return EVAL_BODY_INCLUDE;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public int endTag() throws JspException {
      try {
         /*if (!nobody)
            out.println("</form></body>");
         out.println("</html>");*/
         out.print(jspUtil.release());
         return 0;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public Form getForm() {
      //form = (Form) pc.getRequest().getAttribute("FORM");
      return form;
   }

   public String getTitle() {
      return title;
   }

   public static class FrameTEI extends TagExtraInfo {
      public VariableInfo[] getVariableInfo(TagData tagData) {
         return new VariableInfo [] {
            //new VariableInfo("frame_form","com.crux.web.form.Form",true,VariableInfo.AT_BEGIN),
            new VariableInfo("frame","com.crux.web.jstl.FrameTag",true,VariableInfo.AT_BEGIN),
            new VariableInfo("jspUtil","com.crux.util.JSPUtil",true,VariableInfo.AT_BEGIN),
         };
      }
   }
}
