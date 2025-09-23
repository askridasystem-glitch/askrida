<html>
<head>
<%= charset %>
<script language="Javascript"><!--
function ready()
{
	if (typeof(self.parent.room) != "undefined")
	{
		self.parent.location.href="<%= action %>";
	}
	document.chat.nickname.focus();
}
function enter()
{
	document.chat.todo.value="login";
	document.chat.submit();
}
function info()
{
	document.chat.todo.value="info";
	document.chat.submit();
}
//--></script>
<title>Chat Login</title>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1" onLoad="ready()">
<form method="post" action="<%= action %>" name="chat">
  <table width="96%" border="0" cellspacing="1" cellpadding="0">
    <tr>
      <td>
        <table width="96%" border="0" cellspacing="1" cellpadding="1" align="center">
          <tr>
            <td colspan="2" nowrap>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">NickName
                    :&nbsp;</font></b></font>
                    <input type="text" name="nickname" size="10" maxlength="<%= maxnicknamelength %>">
                    <font face="Verdana, Arial, Helvetica, sans-serif" size="-2" color="#990000">&nbsp;&nbsp;<br>
                    <%= errorMsg %></font>
                  </td>
                  <td align="right" valign="top">
                    <table border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap><font size="-2" face="Verdana, Arial, Helvetica, sans-serif" color="#999999">Look&amp;Feel
                          :</font></td>
                        <td>
                          <input type="radio" name="look" value="classic">
                        </td>
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="-1" color="#333333">classic</font><font face="Verdana, Arial, Helvetica, sans-serif" size="-1">&nbsp;</font></td>
                        <td>
                          <input type="radio" name="look" value="mirc" checked>
                        </td>
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="-1" color="#333333">mIRC</font></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>

            </td>
          </tr>
          <tr>
            <td colspan="2" nowrap><b><a href="javascript:enter()"><font face="Verdana, Arial, Helvetica, sans-serif" size="-1">Enter</font></a></b>
              <b><font face="Verdana, Arial, Helvetica, sans-serif" size="-1">chatroom</font></b>
              <select name="chatroom">
                <option value="1" selected><%= chatroomName %></option>
              </select>
              <font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> <a href="javascript:info()">info</a>
              <input type="hidden" name="todo" value="login">
              </font> </td>
          </tr>
        </table>
      </td>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr bgcolor="#999999">
            <td>
              <table width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr bgcolor="#FFFFFF">
                  <td nowrap><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><u>ChatRoom</u>
                    : <font color="#FF0000"><b><%= chatroomName %></b></font> (<%= chatroomTotalUsers %>/<%= chatroomMaxUsers %>)<br>
                    <u>Opened</u> :<b> <%= chatroomDate %><br>
                    </b><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><u>Subject</u>
                    :</font><b><font size="-2" face="Verdana, Arial, Helvetica, sans-serif">
                    <font color="#000066"><b><%= chatroomSubject %></b></font></font></b></font></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</form>
<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="0">
  <tr align="center">
    <td valign="top" width="40%">
      <table width="50%" border="0" cellspacing="0" cellpadding="0">
        <tr bgcolor="#999999">
          <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr bgcolor="#FFFFFF">
                <td>
                  <p><font size="-1" face="Verdana, Arial, Helvetica, sans-serif"><b><font color="#666666">jzChat
                    V1.12</font></b><br>
                    <font size="-2" color="#999999">This chatroom (100% HTML)
                    is performed by our jzChat servlet. Guest users just have
                    to enter a nickname for chatting. Root user choose chatroom's
                    name and subject. Root also controls the chatroom : max. users,
                    open, close, transcript, list or kickoff users, clear blacklist features are
                    available. jzChat is protected against cross scripting attacks
                    (testers welcome). Only root user could use &quot;direct-HTML&quot;.<br>
                    <a href="http://www.javazoom.net/jzservlets/jzchat10/jzchat.html" target="_blank">Have
                    a look to the support and FAQ web page before asking how to
                    install it.</a></font><br>
                    </font></p>
                  <p align="right"><font size="-1" face="Verdana, Arial, Helvetica, sans-serif">
                    <font size="-1" face="Verdana, Arial, Helvetica, sans-serif"><font size="-2" color="#999999"><a href="http://www.javazoom.net" target="_blank">&copy;
                    JavaZOOM - 2000</a></font></font><font size="-2" color="#999999">
                    </font></font></p>
                </td>
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
