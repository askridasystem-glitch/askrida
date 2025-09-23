<%@ page import="com.crux.util.JSPUtil"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% final JSPUtil jspUtil = new JSPUtil(request, response); %><html>
<head>
<title>Telkomsel</title>
<link rel="stylesheet" href="<%=jspUtil.getStyleSheetPath()%>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script language="JavaScript" type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" MarginWidth="0" MarginHeight="0" onLoad="MM_preloadImages('<%=jspUtil.getImagePath()%>/loginA_bt.jpg')">
<table width="780" border="0" cellspacing="0" cellpadding="0" align="center" valign="middle">
  <tr>
    <td width="362" height="104" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="237" height="104" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="181" height="104" align="left" valign="top" bgcolor="#FFFFFF"></td>
  </tr>
  <tr>
    <td width="362" height="17" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="237" height="17" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="181" height="17" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m1a.jpg" width="181" height="17"></td>
  </tr>
  <tr>
    <td colspan="2" rowspan="2" align="left" valign="top" background="<%=jspUtil.getImagePath()%>/m.jpg" bgcolor="#FFFFFF"></td>
    <td width="181" height="30" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m1b.jpg" width="181" height="30"></td>
  </tr>
  <tr>
    <td width="181" height="110" align="center" valign="middle" background="<%=jspUtil.getImagePath()%>/m1.jpg"><table width="164" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="56" height="27" align="left"><span class="style1">User ID: </span></td>
        <td width="108" align="right"><input type="text" value="" size="15" name="text"></td>
      </tr>
      <tr>
        <td height="29" align="left"><span class="style1">Password:</span></td>
        <td align="right"><input type="text" value="" size="15" name="text2"></td>
      </tr>
      <tr>
        <td height="30">&nbsp;</td>
        <td align="right"><img src="<%=jspUtil.getImagePath()%>/login_bt.jpg" alt="Submit" name="submit" width="65" height="22" border="0"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td width="362" height="70" align="left" valign="top" background="<%=jspUtil.getImagePath()%>/m2.jpg"></td>
    <td width="237" height="70" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m3.jpg" width="237" height="70"></td>
    <td width="181" height="70" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m4.jpg" width="181" height="70"></td>
  </tr>
  <tr>
    <td width="362" height="35" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m5.jpg" width="362" height="35"></td>
    <td height="35" colspan="2" align="left" valign="top"><img src="<%=jspUtil.getImagePath()%>/m6.jpg" width="418" height="35"></td>
  </tr>
  <tr>
    <td width="362" height="135" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="237" height="135" align="left" valign="top" bgcolor="#FFFFFF"></td>
    <td width="181" height="135" align="left" valign="top" bgcolor="#FFFFFF"></td>
  </tr>
</table>
Deploymet Date : <%@ include file="ts.jsp"%>
</body>
</html>
