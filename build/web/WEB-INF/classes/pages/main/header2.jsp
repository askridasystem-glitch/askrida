<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.crux.util.JSPUtil"%><html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Busy City</title>
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=iso-8859-1" />
	<meta name="author" content="Jenna Smith" />
	<meta name="copyright"	content="Copyright 2006 growldesign" />	
	<meta name="keywords" content="" />
	<meta name="description" content="" />	
	<meta http-equiv="imagetoolbar" content="no" />
	<link href="bc-stylesheet.css" rel="stylesheet" type="text/css" />
</head>

<div id="header">

<div >
		<ul>
			<li><a href="#" onclick="window.parent.basefrm.location='policy_list.proposal.crux';">Marketing</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='policy_list.underwrit.crux';">Underwriting</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='policy_list.reas.crux';">Reinsurance</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='policy_list.claim.crux';">Claim</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='receiptlist.crux?arsid=1';">Finance</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='gl_list.crux';">Accounting</a></li>
			<li><a href="#" onclick="window.parent.basefrm.location='http://www.askrida.co.id';">Askrida</a></li>
		</ul>
		<b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#de1010"><h1>&nbsp;</h1>
		</font></b><h1>&nbsp;</h1>
	    <div id="linknih"><b><font color="#de1010" size="1" face="Verdana, Arial, Helvetica, sans-serif"><a href="#"> Home</a>
	      | <a href="#" onclick="window.parent.basefrm.location='<%=jspUtil.getControllerURL("CHANGE_PASSWORD")%>';">Change Password</a> | <a href="#" onclick="window.parent.location='<%=jspUtil.getControllerURL("LOGOUT")%>';">Log Out</a></font></b></div>
</div>
</div>
<div id="background">
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="1010" height="181" align="left">
		<param name="allowScriptAccess" value="sameDomain" />
		<param name="movie" value="<%=jspUtil.getImagePath()%>/header.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="ffffff" />
		<param name="wmode" value="transparent" />
		<embed src="<%=jspUtil.getImagePath()%>/header.swf" quality="high" bgcolor="#000000" width="1010" height="181" align="left" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
</object>
</div>
</html>
