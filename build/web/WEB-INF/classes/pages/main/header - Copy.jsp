<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.crux.util.JSPUtil,
         com.crux.util.DTOList,
         com.crux.web.controller.SessionManager,
         com.crux.login.model.FunctionsView"%>
<html>
    <% final JSPUtil jspUtil = new JSPUtil(request, response);%>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <%
                    boolean marketing = false;
                    boolean underwriting = false;
                    boolean reas = false;
                    boolean claim = false;
                    boolean finance = false;
                    boolean accounting = false;
                    boolean laporanManajemen = SessionManager.getInstance().getSession().hasResource("MANAGEMENT_REPORT");

                    boolean enableManualBook = SessionManager.getInstance().getSession().hasResource("USER_MANUAL_VIEW");

                    final DTOList menulist = (DTOList) request.getAttribute("MENU");
                    for (int i = 0; i < menulist.size(); i++) {
                        FunctionsView f = (FunctionsView) menulist.get(i);

                        if (f.getStFunctionID().equalsIgnoreCase("15.10.00.00.00.00")) {
                            marketing = true;
                        }

                        if (f.getStFunctionID().equalsIgnoreCase("20.10.00.00.00.00")) {
                            underwriting = true;
                        }

                        if (f.getStFunctionID().equalsIgnoreCase("25.10.00.00.00.00")) {
                            reas = true;
                        }

                        if (f.getStFunctionID().equalsIgnoreCase("30.10.00.00.00.00")) {
                            claim = true;
                        }

                        if (f.getStFunctionID().equalsIgnoreCase("35.00.00.00.00.00")) {
                            finance = true;
                        }

                        if (f.getStFunctionID().equalsIgnoreCase("40.10.00.00.00.00")) {
                            accounting = true;
                        }

                        
                    }



        %>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <link href="style.css" rel="stylesheet" type="text/css" />
            <title>Askrida</title></head>
        <title>Untitled Document</title>
        </head>

        <body>
            <table cellpadding=0 cellspacing=0 width="100%">
                <tr>
                    <td width="274" height="129" bgcolor="#e7e9ea">

                        <div id="flash_logo">
                            <img width="274" height="129" src="<%=jspUtil.getImagePath()%>/logo_static.gif">
                        </div>


                        <div id="flash_logo">
                            <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="274" height="129" align="left">
                                <param name="allowScriptAccess" value="sameDomain" />
                                <param name="movie" value="<%=jspUtil.getImagePath()%>/red_header.swf" />
                                <param name="quality" value="high" />
                                <param name="bgcolor" value="ffffff" />
                                <param name="wmode" value="transparent" />
                                <embed src="<%=jspUtil.getImagePath()%>/red_header.swf" quality="high" bgcolor="#000000" width="274" height="129" align="left" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
                            </object>
                        </div>

                    </td>


                    <td width="*" height="129">
                        <div id="topheader">

                            <div align="left" class="bodytext">
                                <br />
                            </div>
                            <div id="toplinks" class="smallgraytext">
                                <a href="http://www.askrida.com" target="_new">Home</a> | <a href="#" onclick="window.parent.basefrm.location='<%=jspUtil.getControllerURL("CHANGE_PASSWORD")%>';">Change Password </a> | <a href="#" onclick="window.parent.location='<%=jspUtil.getControllerURL("LOGOUT")%>';">Logout</a>				</div>
                        </div>
                        <div id="menu">
                            <div align="right" class="smallwhitetext" style="padding:9px;">
                                <%if(laporanManajemen){%><a style="color:yellow" href="http://mdireksi.askrida.com/md/?token=<%=jspUtil.getToken()%>" target="_blank"><b>Laporan Manajemen</b></a> |<% } %> <a href="http://e-learning.askrida.com/" target="_new">E-Learning</a> <%if (enableManualBook) {%>| <a href="#" onclick="window.parent.basefrm.location='printmanualbook.crux';">User Manual</a><% }%> | <a href="#" onclick="window.parent.basefrm.location='printmanualbook.download.crux';">Download</a> 
                            </div>

                        </div>
                    </td>
                </tr>

            </table>



        </body>
    </html>