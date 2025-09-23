<%@ page import="com.crux.util.JSPUtil"%><html>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<head>
	<title>File Insert</title>
	<LINK href="<%=jspUtil.getStyleSheetPath()%>" type='text/css' rel=STYLESHEET>
   <script language="JavaScript" src="FileInsert.js"></script>
</head>
<script>
var stSrc = "";
var iFileSize = 0;
var paramPic = window.dialogArguments;
</script>

<%
   jspUtil.getScriptURL("validator.js");
   String stPictureId = request.getSession().getAttribute("PICTURE_ID") == null ?"":(String)request.getSession().getAttribute("PICTURE_ID");
   String stPictureSize = request.getSession().getAttribute("PICTURE_SIZE") == null ?"":(String)request.getSession().getAttribute("PICTURE_SIZE");

   if(!stPictureId.equals("") && request.getSession().getAttribute("PICTURE_ID") != null)
   {
       session.removeAttribute("PICTURE_ID");
        %>
        <script>
        if(paramPic.frm.pictureId)
        {
            paramPic.frm.pictureId.value = '<%=stPictureId%>';
        }

        </script>
        <%
   }
%>
<body leftmargin="12" topmargin="15" rightmargin="0" bottommargin="0" class="sclbar" onload="window.status='FileInsert.html'">
  <form class="form" name="FileInsertForm" enctype="multipart/form-data" method=post target="iImg" >
  <input type=hidden name=EVENTNAME value="UPLOAD_IMG">
  <input type=hidden name=CALLOUT_EVENT value ="UPLOAD_IMG">
  <input type=hidden name="requestid" value="UPLOAD_IMG">

  <DIV ID="part2" style="width:350px; height:500px; overflow:auto; vertical-align:top; position:relative; top:1; left:1;">
  <table align="center" width="350" border="0" bordercolor="#A8A8A8" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
    <tr>
    <td rowspan="2" height="22" width="16"><img src="../../../images/kotak.gif" height="22" width="16"></td>
	<td width="330" height="20" valign="bottom" class="Title">&nbsp;&nbsp;Item Master - File Insert</td>
	</tr>
	<tr>
    <td bgcolor="#194D80" height="1" width="330"><img src="../../../images/spacer.gif" height="2" width="330"></td>
	</tr>
  </table>

  <!-- spacer -->
  <table align="center" width="350" bgcolor="#C9D0D8">
   <tr><td height="7"><img src="../../../images/spacer.gif" height="1"></td></tr>
  </table>

  <table align="center" width="350" border="0" bgcolor="#C9D0D8" cellpadding="2" cellspacing="0">
  	<!--tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr-->
	<tr>
	  <td align="center"><input class="text" type="File" style="width:330px;" name="txtFile" value=""></td>
	</tr>
	<tr><td height="5">&nbsp; Note : Image size must be <= 50 KB</td></tr>
  </table>

  <!-- spacer -->
  <table align="center" width="350" bgcolor="#C9D0D8"><tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr></table>

  <!-- footer menu -->
  <table align="center" width="350" border="0" bordercolor="#000000" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
	<tr valign="middle">
	 <td height="30" align="center">
	  <input class="button" type="button" name="upload" style="width=70px;" value="Upload" onclick="save();"><!--opener.document.all.foto.src=FileInsertForm.picFilename.value;-->
	  <input class="button" type="button" name="cancel" style="width=70px;" value="Cancel" onclick="window.close();">
	 </td>
	</tr>
  </table>
  <table align="center" width="350" border="0" bordercolor="#000000" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
	<tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr>
	<tr>
	  <td bgcolor="#194D80" height="2" width="350"><img src="../../../images/spacer.gif" height="2" width="330"></td>
	</tr>
	<tr><td height="15"><img src="../../../images/spacer.gif" height="1"></td></tr>
  </table>

    <!-------------------  -->

  <table align="center" width="350" border="0" bordercolor="#A8A8A8" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
    <tr>
    <td rowspan="2" height="22" width="16"><img src="../../../images/kotak.gif" height="22" width="16"></td>
	<td width="330" height="20" valign="bottom" class="Title">&nbsp;&nbsp;Item - File Insert</td>
	</tr>
	<tr>
    <td bgcolor="#194D80" height="2" width="330"><img src="../../../images/spacer.gif" height="2" width="330"></td>
	</tr>
  </table>

  <!-- spacer -->
  <table align="center" width="350" bgcolor="#C9D0D8">
   <tr><td height="15"><img src="../../../images/spacer.gif" height="1"></td></tr>
  </table>

  <table align="center" width="350" border="0" bgcolor="#C9D0D8" cellpadding="2" cellspacing="0">
  	<tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr>
	<tr>
	  <td align="center" id="namaFile"></td>
	</tr>
	<tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr>
  </table>

  <!-- spacer -->
  <table align="center" width="350" bgcolor="#C9D0D8"><tr><td height="10"><img src="../../../images/spacer.gif" height="1"></td></tr></table>

  <!-- footer menu -->
  <table align="center" width="350" border="0" bordercolor="#000000" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
	<tr valign="middle">
	 <td height="30" align="center">
	  <input class="button" type="button" name="ok" style="width=70px;" value="Close" onclick="processOK();">
	 </td>
	</tr>
  </table>
  <table align="center" width="350" border="0" bordercolor="#000000" bgcolor="#C9D0D8" cellpadding="0" cellspacing="0">
	<tr><td height="5"><img src="../../../images/spacer.gif" height="1"></td></tr>
	<tr>
	  <td bgcolor="#194D80" height="2" width="350"><img src="../../../images/spacer.gif" height="2" width="330"></td>
	</tr>
	<tr><td height="15"><img src="../../../images/spacer.gif" height="1"></td></tr>
  </table>
  </div>
  </form> <!-- END FileInsertForm -->

</body>
</html>
