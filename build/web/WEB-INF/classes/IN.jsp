<%@ page import="com.crux.util.JSPUtil,
                 com.crux.lang.LanguageManager,
                 com.crux.util.DTOList,
                 com.crux.lang.LanguageView,
                 com.crux.util.Tools,
                 com.crux.common.parameter.Parameter"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<%
   final boolean firstTime = !"Y".equalsIgnoreCase((String) request.getSession().getAttribute("FIRST_TIME"));

   final DTOList languages = LanguageManager.getInstance().getLanguages();

    final boolean showLogo = Parameter.readBoolean("GEN_SHOW_LOGO");
%>

<html>
<head>
<title>....::::Askrida ::::...</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

</head>
<body bgcolor="#000000" topmargin=0 leftmargin=0>
<form name=loginform method=POST action="login.ctl">
<input type=hidden name=EVENT value=LOGIN_MAIN>
<table width="798" border="0" align="center" cellpadding="0" cellspacing="0">

  <tr>
   <td><img src="spacer.gif" width="48" height="1" border="0"></td>
   <td><img src="spacer.gif" width="252" height="1" border="0"></td>
   <td><img src="spacer.gif" width="188" height="1" border="0"></td>
   <td><img src="spacer.gif" width="44" height="1" border="0"></td>
   <td><img src="spacer.gif" width="266" height="1" border="0"></td>
   <td><img src="spacer.gif" width="1" height="1" border="0"></td>
  </tr>

  <tr>
   <td rowspan="2"><img name="in_r1_c1" src="images/askrida/in_r1_c1.jpg" width="48" height="168" border="0"></td>
   <td rowspan="3"><a href="/fin/pages/Web-Askrida8/HOLE.HTM">
   <img name="in_r1_c2" src="images/askrida/in_r1_c2.jpg" width="252" height="182" border="0"></a></td>
   <td colspan="2" bgcolor="#6884B8"><img src="images/askrida/in_r1_c1.jpg" alt="bolong" name="in_r1_c1" width="58" height="132" border="0"><img src="images/askrida/in_r1_c1.jpg" alt="bolong" name="in_r1_c1" width="58" height="132" border="0"><img src="images/askrida/in_r1_c1.jpg" alt="bolong" name="in_r1_c1" width="58" height="132" border="0"><img src="images/askrida/in_r1_c1.jpg" alt="bolong" name="in_r1_c1" width="58" height="132" border="0"></td>
   <td rowspan="4">
   <img name="in_r1_c5" src="images/askrida/in_r1_c5.jpg" width="266" height="226" border="0"></td>
   <td><img src="spacer.gif" width="1" height="132" border="0"></td>
  </tr>
  <tr>
   <td rowspan="3" colspan="2">
   <img name="in_r2_c3" src="images/askrida/in_r2_c3.jpg" width="232" height="94" border="0"></td>
   <td><img src="spacer.gif" width="1" height="36" border="0"></td>
  </tr>
  <tr>
   <td rowspan="5">
   <img name="in_r3_c1" src="images/askrida/in_r3_c1.jpg" width="48" height="232" border="0"></td>
   <td><img src="spacer.gif" width="1" height="14" border="0"></td>
  </tr>
  <tr>
   <td rowspan="3">
   <img name="in_r4_c2" src="images/askrida/in_r4_c2.jpg" width="252" height="178" border="0"></td>
   <td><img src="spacer.gif" width="1" height="44" border="0"></td>
  </tr>
  <tr>
   <td rowspan="2">
   <img name="in_r5_c3" src="images/askrida/in_r5_c3.jpg" width="188" height="134" border="0"></td>
   <td colspan="2">
<center>
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
  codebase="rahmansyah"
  id="Movie3" width="309" height="76">
  <param name="movie" value="images/askrida/Movie3.swf">
  <param name="quality" value="High">
  <param name="bgcolor" value="">
  <param name="_cx" value="8176">
  <param name="_cy" value="2011">
  <param name="FlashVars" value>
  <param name="Src" value="images/askrida/Movie3.swf">
  <param name="WMode" value="Window">
  <param name="Play" value="-1">
  <param name="Loop" value="-1">
  <param name="SAlign" value>
  <param name="Menu" value="-1">
  <param name="Base" value>
  <param name="AllowScriptAccess" value="always">
  <param name="Scale" value="ShowAll">
  <param name="DeviceFont" value="0">
  <param name="EmbedMovie" value="0">
  <param name="SWRemote" value>
  <param name="MovieData" value>
  <param name="SeamlessTabbing" value="1">
  <embed name="Movie3" src="images/askrida/Movie3.swf" quality="high" bgcolor="#000000"
    width="309" height="76"
    type="application/x-shockwave-flash"
    pluginspage="como">  </embed></object>
</center>   </td>
   <td><img src="spacer.gif" width="1" height="76" border="0"></td>
  </tr>
  <tr>
   <td colspan="2">
   <img name="in_r6_c4" src="images/askrida/in_r6_c4.jpg" width="310" height="58" border="0"></td>
   <td><img src="spacer.gif" width="1" height="58" border="0"></td>
  </tr>
  <tr>
   <td colspan="4" bgcolor="#000000">
    <p align="center"><font color="#FFFFFF">Copyright 2009 Askrida Allright Reserved</font></p>   </td>
   <td><img src="spacer.gif" width="1" height="40" border="0"></td>
  </tr>
</table>
<table width="350" border="0" align="center">
  <tr>
    <th width="112" scope="col">&nbsp;</th>
  </tr>
  <tr><td align=right>{L-ENGUser-L}{L-INANama-L} :</td>
  <td width="162"><input type="text" name="userid" value=""></td>
  <td width="62">&nbsp;</td>
  </tr>
   <tr><td align="right">{L-ENGPassword-L}{L-INASandi-L} :</td>
   <td><input type="password" name="password"></td>
   <td>&nbsp;</td>
   </tr>
    <tr><td></td><td align=left><input type=image alt="Submit" name="submit" src="images/askrida/loginA_bt.jpg" width="54" height="21"></td>
      <td align=left>&nbsp;</td>
    </tr> 
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td height="31" colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="3" align="center">&nbsp;</td>
    </tr>
</table>
<script>
   document.getElementById('userid').focus();
</script>
</form>
</body>
</html>