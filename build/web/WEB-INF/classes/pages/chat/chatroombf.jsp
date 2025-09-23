<html>
<head>
<%= charset %>
<title>ChatRoom</title>
<script language="JavaScript"><!--
function privatemsg(user)
{
 window.open('<%= action %>?todo=privatetext&to='+escape(user)+'','PrivateMessage','resizable=yes,width=600,height=70');
}
//--></script>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1">
<table width="98%" border="0" cellspacing="1" cellpadding="0">
  <tr>
    <td align="center">
	 <%= chat %>
	</td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr bgcolor="#999999">
          <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr bgcolor="#777777" align="center">
                <td nowrap><font size="-2" face="Verdana, Arial" color="#FFFFFF"><b>Users : <%= chatroomTotalUsers %>/<%= chatroomMaxUsers %></b></font></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td nowrap><font size="-2" face="Verdana, Arial" color="#000000">
				<%= chatusers %>
                </font></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
