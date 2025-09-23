<html>
<head>
<%= charset %>
<script language="Javascript"><!--
function enter()
{
	document.chat.todo.value="login";
	document.chat.submit();
}
function info()
{
	document.chat.todo.value="admininfo";
	document.chat.submit();
}
function close()
{
	if (confirm("Close the ChatRoom ?"))
	{
	    document.chat.todo.value="close";
	    document.chat.submit();
	}
}
function logout()
{
	document.chat.todo.value="logout";
	document.chat.submit();
}
function transcript()
{
	document.chat.todo.value="transcript";
	document.chat.submit();
}
function clear()
{
	document.chat.todo.value="clear";
	document.chat.submit();
}
function reload()
{
	document.chat.todo.value="adminreload";
	document.chat.submit();
}
function users()
{
	document.chat.todo.value="users";
	document.chat.submit();
}
function kickoff(id)
{
	document.chat.id.value=id;
	document.chat.todo.value="kickoff";
	document.chat.submit();
}
//--></script>
<title>ChatRoom Admin</title>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1">
<form method="post" action="<%= action %>" name="chat">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr align="center">
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="33%">&nbsp;<a href="http://www.javazoom.net" target="_blank"><font face="Arial, Helvetica, sans-serif" size="-2">&copy;
              JavaZOOM - 2000</font></a></td>
            <td align="center" width="34%"><font face="Verdana, Arial, Helvetica, sans-serif"><b><font color="#000099">ChatRoom
              Admin</font></b></font></td>
            <td width="33%" align="right"><a href="http://www.javazoom.net/jzservlets/jzchat10/jzchat.html" target="_blank"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><font face="Arial, Helvetica, sans-serif">jzChat
              support page</font></font></a>&nbsp;</td>
          </tr>
        </table>

      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>
  <table width="96%" border="0" cellspacing="1" cellpadding="0">
    <tr>
      <td>
        <table width="96%" border="0" cellspacing="1" cellpadding="1" align="center">
          <tr>
            <td colspan="3" nowrap> <font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b>
              </b></font><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">NickName
              : <%= nickname %></font></b></font></td>
            <td align="right" nowrap>
              <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td nowrap align="center" valign="middle"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><a href="javascript:enter()">enter</a>&nbsp;&nbsp;</font>
                    <font size="-2" face="Verdana, Arial, Helvetica, sans-serif" color="#999999">
                    </font></td>
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
          <tr>
            <td nowrap colspan="4"> <b><font face="Verdana, Arial, Helvetica, sans-serif" size="-1">Chatroom</font></b>
              <select name="chatroom">
                <option value="1" selected><%= chatroomName %></option>
              </select>
              <font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><a href="javascript:info()">info</a>
              &nbsp;<a href="javascript:users()">users</a>&nbsp; <a href="javascript:clear()">clear
              blacklist</a>&nbsp; <a href="javascript:close()">close</a>&nbsp;
              <a href="javascript:transcript()">transcript</a>&nbsp; <a href="javascript:logout()">logout</a>&nbsp;
              </font> <font face="Verdana, Arial, Helvetica, sans-serif" size="-2">
              </font></td>
          </tr>
        </table>
      </td>
      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr bgcolor="#999999">
            <td>
              <table width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr bgcolor="#FFFFFF">
                  <td nowrap><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><u>ChatRoom</u>
                    : <font color="#FF0000"><b><%= chatroomName %></b></font>
                    (<%= chatroomTotalUsers %>/<%= chatroomMaxUsers %>)<br>
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
  <br>
<center>
    <%= users %> <br>
    <table width="96%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>
          <table width="200" border="0" cellspacing="0" cellpadding="0">
            <tr bgcolor="#CCCCCC">
              <td>
                <table width="100%" border="0" cellspacing="1" cellpadding="2">
                  <tr bgcolor="#FFFFFF">
                    <td nowrap>
                      <p align="center"><font size="-2" face="Verdana, Arial, Helvetica, sans-serif">
                        <font size="-2" face="Verdana, Arial, Helvetica, sans-serif">
                        </font><b><font size="-2" face="Verdana, Arial, Helvetica, sans-serif">
                        <font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#660000">Servlet
                        Administration<br>
                        <br>
                        </font></b></font></font></b></font><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><font size="-2"><a href="javascript:reload()">reload
                        configuration</a></font></font></font></font></p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </center>
  <div align="left">
    <input type="hidden" name="todo" value="info">
    <input type="hidden" name="id">
  </div>
</form>
</body>
</html>
