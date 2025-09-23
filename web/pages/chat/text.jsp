<html>
<head>
<%= charset %>
<title>Chat</title>
<script language="JavaScript"><!--
function ready()
{
	document.chat.msg.focus();
}
function SendMessage()
{
	document.chat.submit();
	document.chat.msg.value = "";
	document.chat.reset();
	document.chat.msg.focus();
}
function chat()
{
	document.chat.todo.value="chat";
	SendMessage();
}
//--></script>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1" onLoad="ready()">
<form method="get" action="<%= action %>" name="chat" target="room" OnSubmit='SendMessage();return false;'>
  <table width="98%" border="0" cellspacing="1" cellpadding="0">
    <tr>
      <td>
        <table width="98%" border="0" cellspacing="1" cellpadding="1" align="center">
          <tr>
            <td colspan="2" nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b>
              </b></font><font face="Verdana, Arial" size="-1"><b><font color="#000099">Message
              :</font></b></font>
              <input type="text" name="msg" size="46" maxlength="120">
              <input type="hidden" name="to" value="all">
            </td>
          </tr>
          <tr>
            <td nowrap><font face="Verdana, Arial" size="-1"><a href="javascript:chat()"><b>Send</b></a></font>
              <b><font face="Verdana, Arial" size="-1">to All Users</font></b>
              <input type="hidden" name="todo" value="chat">
            </td>
            <td align="right" nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><a href="<%= action %>?todo=refresh" target="room">refresh</a></font>
              <font face="Verdana, Arial" size="-1">&nbsp;<a href="<%= action %>?todo=logout" target="_parent">logout</a></font>&nbsp;
            </td>
          </tr>
        </table>
      </td>
      <td valign="middle">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr bgcolor="#999999">
            <td>
              <table width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr bgcolor="#FFFFFF">
                  <td nowrap><font size="-2" face="Verdana, Arial"><u><%= chatroomName %></u> : <br><b><font color="#CC0000"><%= chatroomDate%></font></b></font></td>
                </tr>
                <tr bgcolor="#FFFFFF">
                  <td nowrap><font size="-2" face="Verdana, Arial"><u>Subject</u>
                    :<br>
                    <font color="#000066"><b><%= chatroomSubject %></b></font></font></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</form>
</body>
</html>