/***********************************************************************
 * Module:  com.crux.util.JSPUtil
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 11:59:39 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.codedecode.Codec;
import com.crux.common.config.Config;
import com.crux.common.controller.*;
import com.crux.common.model.Filter;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import com.crux.util.fop.FOPUtil;
import com.crux.util.validation.FieldValidator;
import com.crux.util.stringutil.StringUtil;
import com.webfin.insurance.ejb.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class JSPUtil {
    private HttpServletRequest rq;
    private HttpServletResponse rp;
    
    public final static transient char MANDATORY = 1;
    public final static transient char READONLY = 2;
    public final static transient char DISABLED = 4;
    public final static transient char MANUAL = 8;
    public final static transient char MANUAL_1 = 8;
    public final static transient char MANUAL_2 = 16;
    public final static transient char MANUAL_3 = 32;
    public final static transient char MULTIPLE_SELECT = 64;
    public final static transient char NOTEXTMODE = 128;
    public static final transient char NOBORDER = 128 * 2;
    public static final transient char EXTENDED_STYLE = 128 * 4;
    public static final transient char CURSOR_CLICKABLE = 128 * 8;
    
    public final static transient long defaultFlags = 0;
    private boolean isPopUp;
    private boolean isNullTitle = true;

    private final static transient LogManager logger = LogManager.getInstance(JSPUtil.class);
    
    
    public static String jsEscape(String st) {
        if (st == null) return null;
        
        final StringBuffer sz = new StringBuffer();
        
        for (int i = 0; i < st.length(); i++) {
            final char a = st.charAt(i);
            switch (a) {
                case 13:
                    sz.append("\\r");
                    break;
                case 10:
                    sz.append("\\n");
                    break;
                case '\'':
                    sz.append("\\'");
                    break;
                default:
                    sz.append(a);
            }
        }
        return sz.toString();
    }
    
    public static String xmlEscape(String st) {
        if (st == null) return null;
        
        final StringBuffer sz = new StringBuffer();
        
        for (int i = 0; i < st.length(); i++) {
            final char a = st.charAt(i);
            switch (a) {
                case '&':
                    sz.append("&amp;");
                    break;
                case '<':
                    sz.append("&lt;");
                    break;
                case '>':
                    sz.append("&gt;'");
                    break;
                case '\"':
                    sz.append("&quot;");
                    break;
                    
                default:
                    sz.append(a);
            }
        }
        return sz.toString();
    }
    
    public static String translateMessage(Throwable e) {
        if (Config.JRE_1_4)
            while (e.getCause() != null) e = e.getCause();
        
        if (e == null) return "(No error)";
        if (e.getMessage() == null) return e.toString();
        return e.getMessage();
    }
    
    /**
     * @param rq
     * @param rp
     * @throws Exception
     * @deprecated
     */
    public JSPUtil(HttpServletRequest rq, HttpServletResponse rp) throws Exception {
        this.rq = rq;
        this.rp = rp;
        
        //ControllerServlet.disableCache(rp);
        
        final Throwable e = (Throwable) rq.getAttribute("ERROR_MESSAGE");
        
        if (e != null) {
            rprintln("<script>alert('Error:" + jsEscape(translateMessage(e)) + "');</script>");
        }
        
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath() + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("calendar-win2k-cold-1.css") + "\" type=\"text/css\">");
        //rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("bc-stylesheet.css") + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("style.css") + "\" type=\"text/css\">");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-lang/calendar-en.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-setup.js") + "\"></script>");
    }
    
    public JSPUtil(HttpServletRequest rq, HttpServletResponse rp, String stTitle) throws Exception {
        
        this.rq = rq;
        this.rp = rp;
        
        isPopUp = rq.getParameter("pop") != null;
        isNullTitle = (stTitle == null);
        //ControllerServlet.disableCache(rp);
        
        final Throwable e = (Throwable) rq.getAttribute("ERROR_MESSAGE");
        
        if (e != null) {
            rp.getOutputStream().print("<script>alert('Error:" + jsEscape(translateMessage(e)) + "');</script>");
        }
        
        String encType = (String)rq.getAttribute("ENCTYPE");
        
        rprintln("<html>");
        rprintln("<head>");
        rprintln("<title>" + (stTitle == null ? "" : stTitle) + "</title>");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath() + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("calendar-win2k-cold-1.css") + "\" type=\"text/css\">");
        //rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("bc-stylesheet.css") + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("style.css") + "\" type=\"text/css\">");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("validator.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-lang/calendar-en.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-setup.js") + "\"></script>");
        rprintln("</head>");
        rprintln("<body style=\"background:#ffffff url(images/maria_bg1.gif) no-repeat fixed bottom right>\">");
        rprintln("<form name=f method=POST action=\"ctl.ctl\" "+(encType==null?"":("ENCTYPE=\""+encType+"\""))+">");
        rprintln("<input type=hidden name=EVENT>");
      /*rprintln("<div style=\"height:100\">");
      rprintln("<a href=\"#\" onclick=\"window.parent.location='"+getControllerURL("LOGOUT")+"';\">Log Out</a>&nbsp;");
      rprintln("<br>");
      rprintln("<br>");
      rprintln("<br>");
      //rprintln("<img src=\""+getImagePath()+"/bar1.gif"+"\">");
      rprintln("</div>");*/
        
        if (!isPopUp) {
            rprintln("<table width=\"100%\" cellpadding=0 cellspacing=0>");
            if (stTitle != null) {
                //rp.getWriter().println("<tr><td><font size=8>DJP</font></td></tr>");
                rq.getRequestDispatcher("/pages/main/inhdr.jsp").include(rq,rp);
                rprintln(getHeader(stTitle));
                //rprintln("<script>f.EVENT.value='FOOTER';f.submit();</script>");
            }
            rprintln("</table>");
        } else {
            rprintln("<table width=\"100%\" cellpadding=0 cellspacing=0>");
            rprintln(getHeader(stTitle));
            rprintln("</table>");
            rprintln("<input type=hidden name=pop value=true>");
            rprintln("<script>window.parent.title='" + (stTitle == null ? "" : stTitle) + "';</script>");
            //rprintln("<script>f.EVENT.value='FOOTER';f.submit();</script>");
        }
        //rprintln("<table width=\"100%\" height=\"100%\" style=\"background:white url(images/maria_bg1.gif) no-repeat\" cellspacing=0 cellpadding=0><tr><td>");
        rprintln("<div width=\"100%\" align=center>");
        //rprintln("<script>anim_Cursor(document);</script>");
    }
    
    public JSPUtil(HttpServletRequest rq, HttpServletResponse rp, String stTitle, boolean autoRefresh, boolean header) throws Exception {
        
        this.rq = rq;
        this.rp = rp;
        
        isPopUp = rq.getParameter("pop") != null;
        isNullTitle = (stTitle == null);
        //ControllerServlet.disableCache(rp);
        
        final Throwable e = (Throwable) rq.getAttribute("ERROR_MESSAGE");
       
        if (e != null) {
            rp.getOutputStream().print("<script>alert('Error:" + jsEscape(translateMessage(e)) + "');</script>");
        }
        
        String encType = (String)rq.getAttribute("ENCTYPE");
        
        rprintln("<html>");
        if(autoRefresh) rprintln("<meta http-equiv=refresh content=300 >");
        rprintln("<head>");
        rprintln("<title>" + (stTitle == null ? "" : stTitle) + "</title>");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath() + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("calendar-win2k-cold-1.css") + "\" type=\"text/css\">");
        //rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("bc-stylesheet.css") + "\" type=\"text/css\">");
        rprintln("<link rel=\"stylesheet\" href=\"" + getStyleSheetPath("style.css") + "\" type=\"text/css\">");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("validator.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-lang/calendar-en.js") + "\"></script>");
        rprintln("<script language=\"JavaScript\" src=\"" + getScriptURL("calendar-setup.js") + "\"></script>");
        rprintln("</head>");
        rprintln("<body style=\"background:#ffffff url(images/maria_bg1.gif) no-repeat fixed bottom right>\">");
        rprintln("<form name=f method=POST action=\"ctl.ctl\" "+(encType==null?"":("ENCTYPE=\""+encType+"\""))+">");
        rprintln("<input type=hidden name=EVENT>");
      /*rprintln("<div style=\"height:100\">");
      rprintln("<a href=\"#\" onclick=\"window.parent.location='"+getControllerURL("LOGOUT")+"';\">Log Out</a>&nbsp;");
      rprintln("<br>");
      rprintln("<br>");
      rprintln("<br>");
      //rprintln("<img src=\""+getImagePath()+"/bar1.gif"+"\">");
      rprintln("</div>");*/
        
        if (!isPopUp) {
            rprintln("<table width=\"100%\" cellpadding=0 cellspacing=0>");
            if (stTitle != null) {
                //rp.getWriter().println("<tr><td><font size=8>DJP</font></td></tr>");
                rq.getRequestDispatcher("/pages/main/inhdr.jsp").include(rq,rp);
                if(header) rprintln(getHeader(stTitle));
                //rprintln("<script>f.EVENT.value='FOOTER';f.submit();</script>");
            }
            rprintln("</table>");
        } else {
            rprintln("<table width=\"100%\" cellpadding=0 cellspacing=0>");
            if(header) rprintln(getHeader(stTitle));
            rprintln("</table>");
            rprintln("<input type=hidden name=pop value=true>");
            rprintln("<script>window.parent.title='" + (stTitle == null ? "" : stTitle) + "';</script>");
            //rprintln("<script>f.EVENT.value='FOOTER';f.submit();</script>");
        }
        //rprintln("<table width=\"100%\" height=\"100%\" style=\"background:white url(images/maria_bg1.gif) no-repeat\" cellspacing=0 cellpadding=0><tr><td>");
        rprintln("<div width=\"100%\" align=center>");
        //rprintln("<script>anim_Cursor(document);</script>");
    }
    
    private void rprintln(String s) throws IOException {
        
        rp.getWriter().println(s);
    }
    
    public String release() throws Exception {
        //rp.flushBuffer();
        //rp.getWriter().flush();
        //rp.getOutputStream().flush();
        
        StringBuffer sz = new StringBuffer();
        
        //sz.append("</td></tr></table>");
        sz.append("</div></form>");
        if ((!isPopUp) && (!isNullTitle)) {
            //sz.append("<script language=\"JavaScript\"> ez_codePath = \"" + rq.getContextPath() + "/script/" + "\" </script>");
            //sz.append("<script language=\"JavaScript\" src=\"" + getScriptURL("ezloader.js") + "\"></script>");
        }
        sz.append("</body>");
        sz.append("</html>");
        //sz.append("<script language=\"JavaScript\">menusGo()</script>");
        //sz.append("<script language=\"JavaScript\">showPermPanel('Main', 20, 25)</script>");
        return sz.toString();
      /*rp.getWriter().println(
      "<script language=\"JavaScript\">menusGo()</script>\n" +
      "<script> showPermPanel('Main', 20, 25) </script>"
      );*/
    }
    
    
    public String getImagePath() {
        return rq.getContextPath() + "/images";
    }
    
    public String getStyleSheetPath() {
        return rq.getContextPath() + "/css/crux.css";
    }
    
    public String getStyleSheetPath(String css) {
        return rq.getContextPath() + "/css/" + css;
    }
    
    public String getRootPath() {
        return rq.getContextPath();
    }
    
    public String getPagesPath() {
        return rq.getContextPath() + "/pages";
    }
    
    public String getPageURL(String stEventID) {
        return "page.ctl?EVENT=" + stEventID;
        //final Events events = (Events) rq.getSession().getServletContext().getAttribute("EVENTCOLLECTION");
        //return events.getEvent(stEventID).getDefaultAction().getStPageURL();
    }
    
    public String getScriptURL(String stScriptName) {
        return rq.getContextPath() + "/script/" + stScriptName;
    }
    
    public static String print(Long lg) {
        return lg == null ? "0" : lg.toString();
    }
    
    public static String print(BigDecimal lg) {
        return lg==null?"0":lg.toString();
    }
    
    public static String print(BigDecimal lg, int prec) {
        if (lg == null) return "0";
        return NumberUtil.getMoneyStr(lg.doubleValue(), prec);
    }
    
    public static String print(String st) {
        return st == null ? "" : st;
    }
    
    public static String print(Date st) {
        return st == null ? "" : DateUtil.getDateStr(st);
    }
    
    public static String print(Object st) {
        if (st instanceof Date) return print((Date) st);
        if (st instanceof Long) return print((Long) st);
        if (st instanceof BigDecimal) return printAutoPrec((BigDecimal) st);
        return st == null ? "" : st.toString();
    }
    
    public static String printAutoPrec(BigDecimal bigDecimal) {
        if (bigDecimal==null) return "";
        return print(bigDecimal, bigDecimal.scale());
    }
    
    public static String getScriptValueOfObject(Object o) {
        if (o instanceof String)
            return "'" + o + "'";
        else if (o instanceof Date)
            return "'" + DateUtil.getDateStr((Date) o) + "'";
        else
            return String.valueOf(o);
    }
    
    public String flowGuard() {
        if (rq.getAttribute("FLOW_GUARD") != null) return "";
        rq.setAttribute("FLOW_GUARD", "OK");
        return "<input type=hidden name=flowCard value=\"" + Helper.getFlowCard(rq) + "\">";
    }
    
    public String getValidationScript(String stFieldName, FieldValidator fv, Object value, int width, long lFlags) {
        final boolean isMandatory = ((lFlags & MANDATORY) != 0);
        final boolean isRO = ((lFlags & READONLY) != 0);
        final boolean isDisabled = ((lFlags & DISABLED) != 0);
        
        if (isDisabled) return "";
        
        //if (fv==null)
        //   if (isMandatory)
        //      fv = new FieldValidator(stFieldName,stFieldName,"string",0);
        
        if (fv == null) return "";
        
        final StringBuffer sz = new StringBuffer();
        
        sz.append("<SCRIPT>");
        sz.append("VA_re");
        
        sz.append(isMandatory ? 'q' : 'g').append("(document.getElementById('" + stFieldName + "'),'" + fv.getStFieldName() + "','" + fv.getStFieldType() + "'");
        if (fv.getFieldLen() > 0) {
            if (fv.getFieldMinLen() > 0)
                sz.append(",[" + fv.getFieldMinLen() + "," + fv.getFieldLen() + "]");
            else
                sz.append("," + fv.getFieldLen());
        }
        sz.append(")");
        
        if ((lFlags & MANUAL) != 0) {
            sz.append(".setgroup(1)");
        } else if ((lFlags & MANUAL_1) != 0) {
            sz.append(".setgroup(1)");
        } else if ((lFlags & MANUAL_2) != 0) {
            sz.append(".setgroup(2)");
        } else if ((lFlags & MANUAL_3) != 0) {
            sz.append(".setgroup(3)");
        }
        
        if ((fv.getRangeMin() != null) || (fv.getRangeMax() != null)) {
            sz.append(".setrange(" + getScriptValueOfObject(fv.getRangeMin()) + "," + getScriptValueOfObject(fv.getRangeMax()) + ")");
        }
        
        if (fv.getStFieldType().indexOf("money") >= 0)
            sz.append(".allowzero()");
        
        if (fv.getStDescField()!=null ){
            sz.append(".setDescField('"+fv.getStDescField()+"')");
        }
        
        sz.append(";");
        
        if (fv.getAttributes()!=null) {
            final String onchg = (String)fv.getAttributes().get("onChange");
            
            if (onchg!=null) {
                sz.append("document.getElementById('"+stFieldName+"').VE_onchange=function() {f.EVENT.value='"+onchg+"';f.submit();};");
            }
        }
        
        
        sz.append("</SCRIPT>");
        
        return sz.toString();
    }
    
    public String getInputText(String stFieldName, FieldValidator fv, Object value, int width) {
        return getInputText(stFieldName, fv, value, width, defaultFlags);
    }
    
    public String getInputText(String stFieldName, FieldValidator fv, Object value, int width, long lFlags) {
        return getInputText(stFieldName, fv, value, width, lFlags, null);
    }
    
    protected String getInputTextDate(String stFieldName, FieldValidator fvx, Object value, int width, long lFlags, String stAttributes) {
        if (value == "") value = null;
        final FieldValidator fv = fvx.getClone();
        fv.setStFieldType("date");
        final Calendar cld = Calendar.getInstance();
        if (value != null)
            cld.setTime((Date) value);
        String stDay = value == null ? "" : String.valueOf(cld.get(Calendar.DAY_OF_MONTH));
        String stMonth = value == null ? "" : String.valueOf(cld.get(Calendar.MONTH) + 1);
        String styear = value == null ? "" : String.valueOf(cld.get(Calendar.YEAR));
        
        String s0 =
                "<input " + ((lFlags & READONLY) != 0 ? "readonly " : "") + "type=hidden class=input name=" + stFieldName + " id=" + stFieldName + " value=\"" + JSPUtil.printX(value) + "\" style=\"width:" + width + "\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName, fv, value, width, lFlags);
        fv.setStFieldType("dateday");
        
        String s1 =
                "<input " + ((lFlags & READONLY) != 0 ? "readonly " : "") + "type=text class=input name=" + stFieldName + "d id=" + stFieldName + "d value=\"" + stDay + "\" style=\"width:30\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName + "d", fv, value, width, lFlags);
        fv.setStFieldType("datemonth");
        String s2 =
                "<input " + ((lFlags & READONLY) != 0 ? "readonly " : "") + "type=text class=input name=" + stFieldName + "m id=" + stFieldName + "m value=\"" + stMonth + "\" style=\"width:30\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName + "m", fv, value, width, lFlags);
        fv.setStFieldType("dateyear");
        String s3 =
                "<input " + ((lFlags & READONLY) != 0 ? "readonly " : "") + "type=text class=input name=" + stFieldName + "y id=" + stFieldName + "y value=\"" + styear + "\" style=\"width:30\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName + "y", fv, value, width, lFlags);
        
        return s1 + " / " + s2 + " / " + s3 + s0 + flowGuard();
    }
    
    public String getInputTextDate2(String stFieldName, FieldValidator fv, Object value, int width, long lFlags, String stAttributes) {
        width = 80;
        
        final boolean isReadOnly = (lFlags & READONLY) != 0;
        
        return
                "<input " + (isReadOnly ? "readonly " : "") + "type=text title=\"DD/MM/YYYY\" class=input name=" + stFieldName + "  id=" + stFieldName + " value=\"" + JSPUtil.print(value) + "\" style=\"width:" + width + "\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName, fv, value, width, lFlags) +
                (isReadOnly?"":("<image align=top width=20 height=18 id=" + stFieldName + "_trig style=\"cursor: pointer;\" src=\"" + getImagePath() + "/cal.gif" + "\">")) +
                //"<br><span name=\"spdp"+stFieldName+"\" id=\"spdp"+stFieldName+"\" class=\"DatePicker\" style=\"overflow:hidden;visibility:hidden\"></span>"+
                flowGuard() +
                (isReadOnly?"":"<script>Calendar.setup({inputField:'" + stFieldName + "',ifFormat:'%d/%m/%Y',button:'" + stFieldName + "_trig',align:'Bl',singleClick:true});</script>");
    }
    
    public String getInputTextDateTime(String stFieldName, FieldValidator fv, Object value, int width, long lFlags, String stAttributes) {
        width = 100;
        
        final boolean isReadOnly = (lFlags & READONLY) != 0;
        
        return
                "<input " + (isReadOnly ? "readonly " : "") + "type=text title=\"DD/MM/YYYY HH:MM:SS\" class=input name=" + stFieldName + "  id=" + stFieldName + " value=\"" + DateUtil.getDateTimeStr2((Date) value) + "\" style=\"width:" + width + "\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName, fv, value, width, lFlags) +
                "<image width=20 height=18 id=" + stFieldName + "_trig style=\"cursor: pointer;\" src=\"" + getImagePath() + "/cal.gif" + "\">" +
                //"<br><span name=\"spdp"+stFieldName+"\" id=\"spdp"+stFieldName+"\" class=\"DatePicker\" style=\"overflow:hidden;visibility:hidden\"></span>"+
                flowGuard() +
                "<script>Calendar.setup({inputField:'" + stFieldName + "',ifFormat:'%d/%m/%Y %H:%M',button:'" + stFieldName + "_trig',align:'Bl',singleClick:true,showsTime:true,timeFormat:'24'});</script>";
    }
    
    public String getInputText(String stFieldName, FieldValidator fv, Object value, int width, long lFlags, String stAttributes) {
        if (width < 1) width = 100;
        
        fv = fvMacro(stFieldName, fv);
        if (fv != null)
            stFieldName = fv.getStFieldID();
        
        final boolean isReadOnly = (lFlags & READONLY) != 0;
        
        final boolean notextmode = (lFlags & NOTEXTMODE) != 0;
        
        if (value instanceof BigDecimal) value=value.toString();
        
        if (!notextmode)
            if (isReadOnly)
                return getDisplayText(stFieldName, fv, value, width);
        
        boolean pw = false;
        
        if (fv != null) {
            if ("date".equalsIgnoreCase(fv.getStFieldType()))
                return getInputTextDate2(stFieldName, fv, value, width, lFlags, stAttributes);
            if ("datetime".equalsIgnoreCase(fv.getStFieldType()))
                return getInputTextDateTime(stFieldName, fv, value, width, lFlags, stAttributes);
            if ("password".equalsIgnoreCase(fv.getStFieldType())) pw = true;
        }
        
        final String noborder = ((lFlags & JSPUtil.NOBORDER) != 0)?";border:0px":"";
        final String cursor = ((lFlags & JSPUtil.CURSOR_CLICKABLE) != 0)?";cursor:hand":"";
        boolean isNum = value instanceof BigDecimal;
        
        return "<input " + (isReadOnly ? "readonly " : "") + "type=" + (pw ? "password" : "text") + " class="+(isNum?"r":"")+"input name=" + stFieldName + " id=" + stFieldName + " value=\"" + JSPUtil.print(value) + "\" style=\"width:" + width + noborder + "\" " + (stAttributes == null ? "" : stAttributes) + " >" +
                getValidationScript(stFieldName, fv, value, width, lFlags) + flowGuard();
    }
    
    public String getHiddenText(String stFieldName, FieldValidator fv, Object value, long lFlags) {
        return "<input type=hidden name=" + stFieldName + " id=" + stFieldName + " value=\"" + JSPUtil.print(value) + "\" >" +
                getValidationScript(stFieldName, fv, value, 0, lFlags) + flowGuard();
    }
    
    public String getInputTextArea(String stFieldName, FieldValidator fv, Object value, int rows, int cols, int width, long lFlags) {
        if (width < 1) width = 100;
        fv = fvMacro(stFieldName, fv);
        if (fv != null)
            stFieldName = fv.getStFieldID();
        return "<textarea " + ((lFlags & READONLY) != 0 ? "readonly " : "") + "type=text class=input name=" + stFieldName + " id=" + stFieldName + " rows=" + rows + " cols=" + cols + " style=\"width:" + width + "\">" + JSPUtil.print(value) + "</textarea>" +
                getValidationScript(stFieldName, fv, value, width, lFlags);
    }
    
    public String getButtonNormal(String stButtonName, String stButtonCaption, String onClickScript, String onClickScript2) {
        return "<input type=button class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonCaption + "\" onclick=\"" + onClickScript + "\">" + flowGuard();
    }
    
    public String getButtonNormal(String stButtonName, String stButtonCaption, String onClickScript) {
        return "<input type=button class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonCaption + "\" onclick=\"" + onClickScript + "\">" + flowGuard();
    }
    
    public String getButtonSubmit(String stButtonName, String stButtonCaption) {
        return "<input type=submit class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonCaption + "\">" + flowGuard();
    }
    
    public String getButtonSubmit(String stButtonName, String stButtonCaption, String onClickScript) {
        return "<input type=submit class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonCaption + "\" onclick=\"" + onClickScript + "\">" + flowGuard();
    }
    
    public String getDisplayText(String stFieldName, FieldValidator fv, Object value, int width) {
        return getDisplayText(stFieldName, fv, value, width, READONLY);
    }
    
    public String getDisplayText(String stFieldName, FieldValidator fv, Object value, int width, int lFlags) {
        if (width < 1) width = 100;
        final StringBuffer sz = new StringBuffer();
        sz.append("<input type=hidden readonly class=");
        if (value instanceof BigDecimal) {
            sz.append("r");
            value = new BigDecimal(StringUtil.removeTraillingZeroes(value.toString()));
        }
        sz.append("input name=").append(stFieldName).append(" id=").append(stFieldName).append(" value=\"").append(JSPUtil.print(value)).append("\" style=\"width:").append(width).append("\">");
        sz.append(JSPUtil.print(value));
        sz.append(getValidationScript(stFieldName, fv, value, width, lFlags));
        sz.append(flowGuard());
        return sz.toString();
    }
    
    public String getDisplayText(String stFieldName, FieldValidator fv, Object value1, Object value, int width, int lFlags) {
        if (width < 1) width = 100;
        final StringBuffer sz = new StringBuffer();
        sz.append("<input type=hidden readonly class=");
        if (value instanceof BigDecimal) sz.append("r");
        sz.append("input name=").append(stFieldName).append(" id=").append(stFieldName).append(" value=\"").append(JSPUtil.print(value1)).append("\" style=\"width:").append(width).append("\">");
        sz.append(JSPUtil.printX(value));
        sz.append(getValidationScript(stFieldName, fv, value, width, lFlags));
        sz.append(flowGuard());
        return sz.toString();
    }
    
    public String getDisplayText2(String stFieldName, FieldValidator fv, Object value, int width) {
        if (width < 1) width = 100;
        final StringBuffer sz = new StringBuffer();
        sz.append("<input type=text readonly class=");
        if (value instanceof BigDecimal) sz.append("r");
        sz.append("input name=").append(stFieldName).append(" id=").append(stFieldName).append(" value=\"").append(JSPUtil.printX(value)).append("\" style=\"width:").append(width).append("\">");
        sz.append(flowGuard());
        return sz.toString();
    }
    
    public FieldValidator fvMacro(String stFieldName, FieldValidator fv) {
        String descField=null;
        if (fv==null) {
            final String[] ar = stFieldName.split("[\\|]");
            
            if (ar.length<3) return fv;
            
            int x = -1;
            
            if (ar.length>3) {
                
                ar[3]="".equalsIgnoreCase(ar[3])?null:ar[3];
                
                if (ar[3]!=null)
                    x = ar.length>3?Integer.parseInt(ar[3]):-1;
                
            }
            fv = new FieldValidator(ar[0], ar[1], ar[2], x);
            
            stFieldName = ar[0];
            
            if (ar.length>=5) fv.setAttributes(ar[4]);
            
            if (stFieldName.indexOf('?')>=0) {
                final String[] spr = stFieldName.split("[\\?]");
                stFieldName = spr[0];
                descField = spr[1];
            }
            
            
        }
        
        fv.setStFieldID(stFieldName);
        
        fv.setStDescField(descField);
        
        return fv;
    }
    
    public String getInputSelect(String stFieldName, FieldValidator fv, Object value, int width) throws Exception {
        return getInputSelect(stFieldName, fv, value, width, 0);
    }
    
    public String getInputSelect(String stFieldName, FieldValidator fv, Object value, int width, long lFlags) {
        try {
            if ((lFlags & EXTENDED_STYLE) != 0)
                return getInputSelect2(stFieldName,fv,value,width,lFlags);
            
            if (width < 1) width = 100;
            
            fv = fvMacro(stFieldName, fv);
            if (fv != null)
                stFieldName = fv.getStFieldID();
            
            final boolean multiple = (lFlags & MULTIPLE_SELECT) != 0;
            
            final boolean ro = (lFlags & READONLY) != 0;
            
            LOV lov = null;
            
            if (value instanceof LOV) lov = (LOV) value;
            
            if (ro) {
                
                return getDisplayText(stFieldName, fv, lov.getLOValue(), lov.getComboDesc(), width, 0);
            }
            
            if (value instanceof LOV) value = ((LOV) value).getComboContent();
            
            return "<select " + (multiple ? "multiple" : "") + " class=select name=" + stFieldName + " id=" + stFieldName + " style=\"width:" + width + "\">" + JSPUtil.print(value) + "</select>" + getValidationScript(stFieldName, fv, value, width, lFlags);
        } catch (Exception e) {
            throw new RuntimeException("Error processing field : "+stFieldName,e);
        }
    }
    
    public String getInputSelect2(String stFieldName, FieldValidator fv, Object value, int width, long lFlags) throws Exception {
        if (width < 1) width = 100;
        
        fv = fvMacro(stFieldName, fv);
        if (fv != null)
            stFieldName = fv.getStFieldID();
        
        final boolean multiple = (lFlags & MULTIPLE_SELECT) != 0;
        
        final boolean ro = (lFlags & READONLY) != 0;
        
        LOV lov = null;
        
        if (value instanceof LOV) lov = (LOV) value;
        
        if (ro) {
            
            return getDisplayText(stFieldName, fv, lov.getLOValue(), lov.getComboDesc(), width, 0);
        }
        
        if (value instanceof LOV) value = ((LOV) value).getComboContent();
        
        final StringBuffer sz = new StringBuffer();
        
        final String bFn = stFieldName+"b";
        sz.append("<input type=hidden name="+stFieldName+" value=\"\">");
        sz.append(getInputText(bFn,fv,null,width,lFlags,"onclick=\"event.cancelBubble=1;f."+bFn+".dropDown();\""));
        sz.append("<img src=\""+getImagePath()+"drop.jpg\" width=17 height=19 align=top onclick=\"event.cancelBubble=1;f."+bFn+".dropDown();\">");
        sz.append("<script>");
        sz.append("document.getElementById('"+bFn+"').selitems=");
        sz.append(lov.getJSObject());
        sz.append(";\n");
        sz.append("DD_initialize(document.getElementById('"+bFn+"'));");
        sz.append("</script>");
        sz.append(getValidationScript(bFn, fv, value, width, lFlags));
        
        return sz.toString();
    }
    
    public String getControllerURL(String stEventID) {
        return getControllerURL(stEventID, null);
    }
    
    public String getControllerURL(String stEventID, String stParameters) {
        return "so.ctl?EVENT=" + stEventID + (stParameters == null ? "" : "&" + stParameters);
    }
    
    public String getHTMLAttributesFromMap(Map x) {
        final Iterator it = x.keySet().iterator();
        
        final StringBuffer sz = new StringBuffer();
        
        while (it.hasNext()) {
            final Object o = it.next();
            if (o != null) {
                String s = String.valueOf(o);
                sz.append(s).append('=').append('"').append(x.get(o)).append('"').append(' ');
            }
        }
        
        return sz.toString();
    }
    
    public boolean hasResource(String stResourceID) {
        return Helper.hasResource(rq, stResourceID);
    }
    
    public String getPager(HttpServletRequest rq, DTOList l) {
        return getPager(l);
    }
    
    public String getPager(DTOList l) {
        int size = l.getTotalRows();
        
        if (size < 0) size = l.size();
        
        int n = size / l.getRowPerPage();
        
        if (size % l.getRowPerPage() > 0) n++;
        
        final String stPagerName = l.getName();
        
        final StringBuffer sz = new StringBuffer();
        
        final boolean pagerDefined = "y".equalsIgnoreCase((String) rq.getAttribute("gPg" + l.getName()));
        
        if (!pagerDefined) {
            rq.setAttribute("gPg" + l.getName(), "Y");
            sz.append("[ <input type=hidden name=ePg" + stPagerName + " id=ePg" + stPagerName + ">");
            sz.append("<input type=hidden name=cPg" + stPagerName + " id=cPg" + stPagerName + " value=\"" + l.getCurrentPage() + "\">");
            sz.append("<input type=hidden name=gPg" + stPagerName + " id=gPg" + stPagerName + " value=\"" + l.getCurrentPage() + "\">");
            sz.append("<input type=hidden name=maxPg" + stPagerName + " id=maxPg" + stPagerName + " value=\"" + n + "\">");
        } else {
            sz.append("[ ");
        }
        
        final int m = l.getCurrentPage();
        
        final boolean showPrevTen = m >= 10;
        
        final int prevTen = m - 10;
        
        if (showPrevTen) {
            sz.append("<a href=\"#\" onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append(prevTen).append(");\">&lt;&lt;</a>");
        }
        
        if (m > 0)
            sz.append("<a href=\"#\" onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append(m - 1).append(");\">&lt;</a> ");
        else
            sz.append("<a href=\"#\"><font color=red>&lt;</font></a> ");
        
        int q = m - 5;
        if (q < 0) q = 0;
        int w = q + 9;
        if (w > n - 1) {
            w = n - 1;
            q = w - 10;
            if (q < 0) q = 0;
        }
        
        final int t = q + 10;
        final boolean showNextTen = (w < n - 1);
        int nextTen = m + 10;
        if (nextTen > n - 1) nextTen = n - 1;
        
        for (int i = q; i <= w; i++) {
            if (m != i)
                sz.append("<a href=\"#\" onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append(i).append(");\">").append(i + 1).append("</a> ");
            else
                sz.append("<a href=\"#\"><font color=red>").append(i + 1).append("</font></a> ");
        }
        
        if (m + 1 < n)
            sz.append("<a href=\"#\" onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append(m + 1).append(");\">&gt;</a>");
        else
            sz.append("<a href=\"#\"><font color=red>&gt;</font></a>");
        
        if (showNextTen) {
            sz.append("<a href=\"#\" onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append(nextTen).append(");\">&gt;&gt;</a>");
        }
        
        if (!pagerDefined)
            if (n > 1) {
            sz.append(" <a href=\"#\"  onclick=\"switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append("document.getElementById('igPage" + stPagerName + "').value-1").append(");\">")
            .append("\nGoto page </a><input class=input type=text style=\"text-align:center;width:30\" onEnterKey=\"if (VM_dovalidate(null,'paging')) switchPg(document.getElementById('gPg" + stPagerName + "'),'").append(stPagerName).append("',").append("document.getElementById('igPage" + stPagerName + "').value-1").append(");\" name=igPage" + stPagerName + " value=\"" + (m + 1) + "\">");
            sz.append(" of " + (n));
            sz.append("\n<script>VA_reg(document.getElementById('igPage" + stPagerName + "'),'Page number','integer',4).setgroup('paging');");
            sz.append("</script>");
            }
        
        sz.append(" ]");
        
        sz.append(flowGuard());
        
        return sz.toString();
    }
    
    public String getSortHeader(DTOList l, String stColumnText, String stColumnName) {
        return getSortHeader(rq, l, stColumnText, stColumnName);
    }
    
    public String getSortHeader(HttpServletRequest rq, DTOList l, String stColumnText, String stColumnName) {
        final StringBuffer sz = new StringBuffer();
        final String stPagerName = l.getName();
        
        if (!"y".equalsIgnoreCase((String) rq.getAttribute("sPg" + stPagerName))) {
            sz.append("<input type=hidden name=sPg" + stPagerName + " id=sPg" + stPagerName + " value=\"" + print(l.getOrderKey()) + "\">");
            int d = l.getOrderDir();
            sz.append("<input type=hidden name=dPg" + stPagerName + " id=dPg" + stPagerName + " value=\"" + d + "\">");
            rq.setAttribute("sPg" + stPagerName, "Y");
        }
        sz.append("<a href=\"#\" onclick=\"switchSort(document.getElementById('sPg" + stPagerName + "'),'" + stPagerName + "','" + stColumnName + "')\"><div style=header><font color=white>" + stColumnText + "</font></div></a>");
        
        if ("ON".equalsIgnoreCase((String) rq.getAttribute("AUTO_FILTER"))) {
            sz.append("<script>addOpt(document.getElementById('affield" + stPagerName + "'),'" + stColumnName + "','" + stColumnText + "');");
            
            final Filter f = l.getFilter();
            
            if (f != null && f.afField != null)
                if (f.afField.equalsIgnoreCase(stColumnName)) {
                sz.append("\ndocument.getElementById('affield" + stPagerName + "').value='" + stColumnName + "';");
                sz.append("\ndocument.getElementById('afmode" + stPagerName + "').value='" + l.getFilter().afMode + "';");
                }
            
            sz.append("</script>");
        }
        
        return sz.append(flowGuard()).toString();
    }
    
    public String getHeader3(String stTitle) {
        final UserSessionView us = (UserSessionView) Helper.getUserSession(rq);
        
        isPopUp = rq.getParameter("pop") != null;
        
        final StringBuffer sz = new StringBuffer();
        
        sz.append(
                "<tr>" +
                "<td style=\"height:24; background:white url(images/maria_tb1.gif)\">" +
                "<table width=\"100%\"><tr>" +
                "<td><font color=white><b>"+stTitle+"</b></font></td>" +
                "<td align=right>");
        
        if (!isPopUp)
            sz.append("<font color=yellow>User Name : "+us.getStUserName()+", Last Login : "+us.getDtLastLogin()+"</font>");
        
        sz.append("</td>" +
                "</tr></table>" +
                "</td>" +
                "</tr>"
                );
        
        if (ControllerServlet.getInstance().isBlockUsers()) {
            sz.append("<tr><td><blink><big><big><b><font color=red>" + ControllerServlet.getInstance().getStBlockReason() + ", please log-out as soon as possible</font></b></big></big></blink></td></tr>");
        }
        
        return sz.toString();
    }         
            
    public String getHeader(String stTitle) {
        UserSessionView us = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
        String mail="";
        String total ="";
        
        try{
            final SQLUtil S = new SQLUtil();
            final PreparedStatement PS = S.setQuery(
                    "select count(x) as total_mail,(select count(in_id) as unread_mail "+
                    " from incoming_letter where receiver = ? and read_flag='N' and delete_flag='N' limit 1) as unread_mail "+
                    " from (select count(in_id) from incoming_letter "+
                    " where receiver = ?  and delete_flag='N' limit 1) as x");
            
            int n=1;
            
            PS.setString(n++,us.getStUserID());
            PS.setString(n++,us.getStUserID());
            
            final ResultSet RS = PS.executeQuery();
            
            if(RS.next()){
                total = RS.getBigDecimal(1).toString();
                mail  = RS.getBigDecimal(2).toString();
            }
            
            S.release();
        }catch(Exception e){
        }
        
        final String userID = ControllerServlet.getInstance().getStUserID();
        
        final StringBuffer sz = new StringBuffer();
        
        isPopUp = rq.getParameter("pop") != null;
        
        String warn = "";
        if(Integer.parseInt(mail)>0){
            warn = "&nbsp;"+ mail +" Surat Baru";
        }

       if (us!=null)
       sz.append("<tr>");
       sz.append("  <td colspan=\"2\" height=\"16\" align=\"right\" valign=\"middle\"><span class=\"style2\"><strong>User ID</strong> : " + us.getStUserID() + ", <strong>Nama</strong> : " + us.getStUserName() + ",<font color=red><strong> Surat Masuk</strong> : " + total + " ("+mail+")&nbsp</font>"+"</span></td>" +
              "</tr>");
        
        if (ControllerServlet.getInstance().isBlockUsers()) {
            sz.append("<tr><td colspan=\"2\"><marquee behavior=slide scrollamount=2000 loop=30000><big><big><b><font color=red> Server akan di restart, user harap segera log-out</font></b></big></big></marquee></td></tr>");
        }

        if (ControllerServlet.getInstance().isAnnouncement()&&"ALL".equalsIgnoreCase(userID)){
            sz.append("<tr>");
            sz.append("<td colspan=\"2\" width=500><font color=red  style=\"font-family:Arial; font-weight:bold; font-size:18px; color:red\"><strong><marquee behavior=slide scrollamount=2000 loop=30000>... PENGUMUMAN, HARAP DIBACA ...</strong></font></marquee></td>");
            sz.append("</tr>");

            sz.append("<tr>");
            sz.append("<td colspan=\"2\"><marquee scrolldelay=\"150\" onmouseover=\"this.stop()\" onmouseout=\"this.start()\"><font style=\"font-family:Arial; font-weight:bold; font-size:16px; color:red\">" + ControllerServlet.getInstance().getStAnnouncement() + "</font></marquee></td>");
            sz.append("</tr>");
        }
       
       if (!isPopUp)
            sz.append("<tr>" +
                    "  <td width=\"1000\" height=\"25\" align=\"left\" valign=\"middle\" colspan=2 background=\"" + getImagePath() + "/bg_hor.jpg\">" +
                    "  <span class=\"title\">").append(stTitle).append("</span>" +
                    "  </td>" +
                    "</tr>");
       
       if(Integer.parseInt(mail)>0){
            sz.append("<tr>");
            sz.append("<td colspan=\"2\" height=\"16\"><font color=red><strong> "+ warn +"</strong></font></td>");
            sz.append("</tr>");
       }

       return sz.toString();
    }
    
    public String getHeader2(String stTitle) {
        UserSessionView us = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
 
        final String userID = ControllerServlet.getInstance().getStUserID();
        
        final StringBuffer sz = new StringBuffer();
        
        isPopUp = rq.getParameter("pop") != null;

       if (us!=null)
       sz.append("<tr>" +
              "  <td height=\"20\" colspan=\"2\" align=\"right\" valign=\"bottom\"><span class=\"style2\"><strong>User ID</strong> : " + us.getStUserID() + ", <strong>Login since</strong> : " + us.getDtLastLogin() + " </span></td>" +
              "</tr>");

        if (!isPopUp)
            sz.append("<tr>" +
                    "  <td width=\"1000\" height=\"25\" align=\"left\" valign=\"middle\" colspan=2 background=\"" + getImagePath() + "/bg_hor.jpg\">" +
                    "  <span class=\"title\">").append(stTitle).append("</span>" +
                    "  </td>" +
                    "</tr>");

        return sz.toString();
    }
    
    public String getForwardEvent(String stForwardID) {
        final Event event = (Event) rq.getAttribute("CURRENT_EVENT");
        final EventAction ea = (EventAction) event.getHmActions().get(stForwardID);
        if (ea == null) throw new RuntimeException("Action " + stForwardID + " not found from " + event.getStEventID());
        final String forwardevent = ea.getStEventID();
        if (forwardevent == null) throw new RuntimeException("Event not found in forward action (" + event.getStEventID() + "." + stForwardID + ")");
        return forwardevent;
    }
    
    public String getButtonAutoLink(String stButtonName, String stButtonText) {
        return "<input type=submit class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonText + "\" onclick=\"document.getElementById('EVENT').value='" + getForwardEvent(stButtonName) + "'\">" + flowGuard();
    }
    
    public String getButtonAutoLinkNoValidate(String stButtonName, String stButtonText) {
        return "<input type=button class=button name=" + stButtonName + " id=" + stButtonName + " value=\"" + stButtonText + "\" onclick=\"document.getElementById('EVENT').value='" + getForwardEvent(stButtonName) + "';document.getElementById('EVENT').form.submit();\">" + flowGuard();
    }
    
    public String getInputCheck(String stFieldName, Object fieldValidator, boolean fieldValue) {
        return getInputCheck(stFieldName, fieldValidator, fieldValue, null);
    }
    
    public String getInputCheck(String stFieldName, Object fieldValidator, boolean fieldValue, long lFlags) {
        return getInputCheck(stFieldName, fieldValidator, fieldValue, null, lFlags);
    }
    
    public String getInputCheck(String stFieldName, Object fieldValidator, boolean fieldValue, String stOnClickEvent) {
        return getInputCheck(stFieldName, fieldValidator, fieldValue, stOnClickEvent, 0);
    }
    
    public String getInputCheck(String stFieldName, Object fieldValidator, boolean fieldValue, String stOnClickEvent, long lFlags) {
        final boolean ro = (lFlags & JSPUtil.READONLY) != 0;
        return "<input type=checkbox name=" + stFieldName + " " + (fieldValue ? "checked" : "") + " " + (ro ? "disabled" : "") + " " + (stOnClickEvent == null ? "" : "onclick=\"" + stOnClickEvent + "\"") + ">";
    }
    
    public String getInputCheck(String stFieldName, String fieldValue, String stOnClickEvent, long lFlags) {
        final boolean ro = (lFlags & JSPUtil.READONLY) != 0;
        return "<input type=checkbox name=" + stFieldName + " " + ("y".equalsIgnoreCase(fieldValue) ? "checked" : "") + " " + (ro ? "disabled" : "") + " " + (stOnClickEvent == null ? "" : "onclick=\"" + stOnClickEvent + "\"") + ">";
    }
    
    public char getUIFlags(String uimode) {
        if (Codec.UIMode.READONLY.equalsIgnoreCase(uimode))
            return JSPUtil.READONLY;
        else if (Codec.UIMode.MANDATORY.equalsIgnoreCase(uimode)) return JSPUtil.MANDATORY;
        return 0;
    }
    
    public void downloadMode(String s) {
        rp.setHeader("Content-Disposition", "attachment; filename=" + s + ';');
    }
    
    public String printesc(Object stItemCode) {
        return xmlEscape(print(stItemCode));
    }
    
    public static String printDateTime(Date d) {
        if (d == null) return "";
        return DateUtil.getDateTimeStr(d);
    }
    
    public static String printFlag(String stFlag) {
        return "<input type=radio disabled " + ("y".equalsIgnoreCase(stFlag) ? "checked" : "") + ">";
    }
    
    public static String print(boolean online) {
        return online ? "Y" : "N";
    }
    
    public String getAutoFilter(HttpServletRequest request, DTOList l) {
        return getAutoFilter(l);
    }
    
    public String getAutoFilter(DTOList l) {
        final String stPagerName = l.getName();
        
        rq.setAttribute("AUTO_FILTER", "ON");
        
        final Filter f = l.getFilter();
        
        final String val = f == null ? null : f.afValue;
        
        return
                //"<div id=af"+stPagerName+">" +
                "\n Filter : <select class=select id=affield" + stPagerName + " name=affield" + stPagerName + "></select>" +
                " <select class=select id=afmode" + stPagerName + " name=afmode" + stPagerName + ">" +  
                "  <option value=" + Filter.AFMODE_CONTAINS + ">Contains</option>" +
                "  <option value="+Filter.AFMODE_EXACT+">Exact</option>" +
                //"  <option value="+Filter.AFMODE_GREATER+">Greater</option>" +
                //"  <option value="+Filter.AFMODE_LESS+">Less</option>" +
                "</select>" +
                " <input type=text class=input onEnterKey=\"if (VM_dovalidate(null,'afilter')) switchPg(document.getElementById('gPg" + stPagerName + "'),'" + stPagerName + "',0);\" id=afvalue" + stPagerName + " name=afvalue" + stPagerName + " value=\"" + (val == null ? "" : val) + "\">" +
                "<script>VA_reg(document.getElementById('afvalue" + stPagerName + "'),'Value','string',40).setgroup('afilter');" +
                "</script>" +
                //"</div>" +
                "";
    }
    
    public String formTabClose(FormTab tab) {
        
        final StringBuffer sz = new StringBuffer();
        sz.append("</td>");
        sz.append("<td id=tabClose style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td></tr>");
        
        sz.append("<tr height=10>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
        sz.append("<td width=\"10\"></td>");
        sz.append("<td></td>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
        sz.append("</tr>");
        
        sz.append("<tr>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" ></td><td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" ></td>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" ></td><td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" ></td>");
        sz.append("</tr></table>");
        sz.append("</td></tr></table>");
        
        return sz.toString();
    }
    
    public String formTabOpen(FormTab tab,String chgEvent) {
        
        final StringBuffer sz = new StringBuffer();
        
        if (tab.activeTab!=null) {
            sz.append("<input type=hidden name=ctab_changetab value="+print(tab.activeTab.tabID)+"\">");
        }
        sz.append("<table cellpadding=0 cellspacing=0 width=\"100%\">");
        sz.append("<tr>");
        sz.append("<td background=\""+getImagePath()+"/tab-top.gif\">");
        sz.append("<table cellpadding=0 cellspacing=0>");
        sz.append("<tr style=\"cursor:hand\">");
        for (int i = 0; i < tab.tabs.size(); i++) {
            FormTab.TabBean tb = (FormTab.TabBean) tab.tabs.get(i);
            final boolean isActive = tab.activeTab==tb;
            
            sz.append("<td><img src=\""+getImagePath()+"/tab-"+(isActive?"A":"I")+"L.gif\"></td>");
            sz.append("<td onclick=\"f.ctab_changetab.value='"+print(tb.tabID)+"';f.EVENT.value='"+chgEvent+"'; f.submit();\" background=\""+getImagePath()+"/tab-"+(isActive?"A":"I")+"C.gif\">"+print(tb.caption)+"</td>");
            sz.append("<td><img src=\""+getImagePath()+"/tab-"+(isActive?"A":"I")+"R.gif\"></td>");
            
        }
        
        sz.append("</tr>");
        sz.append("</table>");
        sz.append(" </td>");
        sz.append(" </tr><tr><td>");
        sz.append("<table cellpadding=0 cellspacing=0 width=\"100%\">");
        sz.append("<tr height=10>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
        sz.append("<td width=\"10\"></td>");
        sz.append("<td></td>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
        sz.append("</tr>");
        sz.append("<tr>");
        sz.append("<td style=\"background-repeat:repeat\" background=\""+getImagePath()+"/tab-border.gif\" width=\"1\"></td>");
        sz.append("<td></td>");
        sz.append("<td>");
        
        
        return sz.toString();
    }
    
    public boolean tabStart(FormTab tab, String tabPremi) {
        if (tab.activeTab == null) return false;
        return tab.activeTab.tabID.equalsIgnoreCase(tabPremi);
    }
    
    public String tabStop(FormTab tab, String tabPremi) {
        return "&nbsp;";
    }
    
    public String getButtonEvent(String stCaption, String stEvent) {
        return getButtonNormal("b",stCaption,"f.EVENT.value='"+stEvent+"';f.submit();");
    }
    
    public static Object getParameter(Map parameters, String s) {
        
        if (parameters==null) return null;
        
        Object o = parameters.get(s);
        
        if (o instanceof String []) {
            final String[] strings = (String [])o;
            if (strings.length>=1) o=strings[0]; else return null;
        }
        
        if (o instanceof String) {
            String sz = (String)o;
            sz=sz.trim();
            if (sz.length()<1) sz=null;
            o=sz;
        }
        
        return o;
    }
    
    public static String printPct(Object o, int i) {
        if (o instanceof BigDecimal) {
            final BigDecimal bd = (BigDecimal) o;
            return print(bd.movePointRight(2),i);
        }
        return "?";
    }
    
    public static String printFX(Object st) {
        if (st instanceof String)
            return FOPUtil.printAutoBlock((String)st);
        
        return printX(st);
    }
    
    public static String printX(Object st) {
        
        if (st==null) st="";
        
        final String s = print(st);
        
        return JSPUtil.xmlEscape(s);
    }
    
    public static String printX(BigDecimal dbx, int i) {
        return print(dbx,i);
    }
    
    public static String displayProgress(long pgrs) {
        return "";
    }
    
    public static Object printNVL(Object x, Object s) {
        if (x==null) return JSPUtil.print(s);
        
        return JSPUtil.print(x);
    }
    
    public static String serialize(HashMap map, String delim1, String delim2, String prefix) {
        
        if (map==null) return null;
        
        Iterator it = map.keySet().iterator();
        
        StringBuffer z = new StringBuffer();
        
        while (it.hasNext()) {
            Object o = (Object) it.next();
            Object v = map.get(o);
            
            if (z.length()>0)
                z.append(delim2);
            
            if (prefix!=null)
                z.append(prefix);
            
            z.append(o);
            z.append(delim1);
            z.append(v);
        }
        
        return z.toString();
    }
    
    public String getPagesPath2() {
        return rq.getContextPath() + "/pages";
    }
    
    public JSPUtil(HttpServletRequest rq, HttpServletResponse rp, boolean tes) throws Exception {
        this.rq = rq;
        this.rp = rp;
    }
    
    public String getHeaderOnly(String stTitle) {
        UserSessionView us = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
 
        final String userID = ControllerServlet.getInstance().getStUserID();
        
        final StringBuffer sz = new StringBuffer();
        
        isPopUp = rq.getParameter("pop") != null;

       if (us!=null)
       sz.append("<tr>" +
              "  <td height=\"20\" colspan=\"2\" align=\"right\" valign=\"bottom\"><span class=\"style2\"><strong>User ID</strong> : " + us.getStUserID() + ", <strong>Login since</strong> : " + us.getDtLastLogin() + " </span></td>" +
              "</tr>");

            sz.append("<tr>" +
                    "  <td width=\"1000\" height=\"25\" align=\"left\" valign=\"middle\" colspan=2 background=\"" + getImagePath() + "/bg_hor.jpg\">" +
                    "  <span class=\"title\">").append(stTitle).append("</span>" +
                    "  </td>" +
                    "</tr>");

        return sz.toString();
    }

    public String getToken() throws Exception{
        final SQLUtil S = new SQLUtil();

        try {
            final PreparedStatement PS = S.setQuery("select * "+
                                                    " from s_token "+
                                                    " ORDER BY RANDOM() "+
                                                    " LIMIT 1");

            ResultSet RS = PS.executeQuery();

            if (RS.next()) return RS.getString("token");

            return null;

        } finally {
            S.release();
        }
    }

    public String cekSudahUbahPassword() throws Exception{
        final SQLUtil S = new SQLUtil();
        UserSessionView us = (UserSessionView) rq.getSession().getAttribute("USER_SESSION");
        String userID = us.getStUserID();
        String cek = "false";
        try {
            /*
            PreparedStatement PS = S.setQuery("select user_id "+
                                        " from s_user_log "+
                                        " where user_id = '"+ userID +"' "+
                                        " and create_date > '2014-02-01 00:00:00' "+
                                        " and user_action = 'CPASS' "+
                                        " limit 1");
            */

            PreparedStatement PS = S.setQuery("select user_id "+
                                        " from s_users "+
                                        " where user_id = '"+ userID +"' "+
                                        " and last_password_change is not null");

            ResultSet RS = PS.executeQuery();

            if (RS.next()) cek = RS.getString("user_id");

            PreparedStatement PS2 = S.setQuery(" select user_id "+
                                                " from s_users "+
                                                " where user_id = '"+ userID +"' and inactive_date <= 'now'");

            ResultSet RS2 = PS2.executeQuery();

            if (RS2.next()) cek = "false";

            return cek;

        } finally {
            S.release();
        }
    }



}
