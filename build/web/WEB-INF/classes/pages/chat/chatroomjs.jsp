<html>
<head>
<%= charset %>
<title>Chat</title>
</head>
<body onBlur="doBlur()" onFocus="doFocus()" onKeyPress="cancelrefresh()" bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1" onLoad="ready()">
<form method="get" action="<%= action %>" name="chat">
<script language="JavaScript"><!--
function ready()
{
	if (typeof(top.autofocus) != "undefined")
	{
		if (top.autofocus==true)
		{
			document.chat.msg.focus();
		}
	}
	else
	{
		document.chat.msg.focus();
	}
	autorefresh();
}
function chat()
{
	document.chat.todo.value="chat";
	document.chat.submit();
}
function privatemsg(user)
{
 window.open('<%= action %>?todo=privatetext&to='+escape(user)+'','PrivateMessage','resizable=yes,width=600,height=70');
}
function refresh()
{
	document.chat.todo.value="refresh";
	document.chat.submit();
}
function logout()
{
	document.chat.todo.value="logout";
	document.chat.submit();
}
function autorefresh()
{
	if (doit==true)
	{
		refresh();
	}
	else
	{
		doit=true;
		setTimeout("autorefresh()",<%= autorefresh %>*1000);
	}
}
function cancelrefresh()
{
	doit=false;
	if (typeof(top.autofocus) != "undefined")
	{
		top.autofocus=true;
	}
}
function doBlur()
{
	if (typeof(top.autofocus) != "undefined")
	{
		top.autofocus=false;
	}
}
function doFocus()
{
	if (typeof(top.autofocus) != "undefined")
	{
		doTextFocus();
		document.chat.msg.focus();
	}
}
function doTextFocus()
{
	if (typeof(top.autofocus) != "undefined")
	{
		top.autofocus=true;
	}
}
var doit=false;
if ( (navigator.appName == "Netscape") && ((navigator.appVersion).substring(0,1) > 3))
{
	document.captureEvents(Event.KEYPRESS);
	document.onkeypress=cancelrefresh;
}
//--></script>
  <table width="98%" border="0" cellspacing="1" cellpadding="0">
    <tr>
      <td>
        <table width="98%" border="0" cellspacing="1" cellpadding="1" align="center">
          <tr>
            <td colspan="2" nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b>
              </b></font><font face="Verdana, Arial" size="-1"><b><font color="#000099">Message
              :</font></b></font>
              <input type="text" name="msg" value="<%= currentmsg %>" size="46" maxlength="120" onFocus="doTextFocus()" onBlur="doBlur()">
              <input type="hidden" name="to" value="all">
            </td>
          </tr>
          <tr>
            <td nowrap><font face="Verdana, Arial" size="-1"><a href="javascript:chat()"><b>Send</b></a></font>
              <b><font face="Verdana, Arial" size="-1">to All Users</font></b>
              <input type="hidden" name="todo" value="chat">
            </td>
            <td align="right" nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><a href="javascript:refresh()" target="_self">refresh</a></font>
              <font face="Verdana, Arial" size="-1">&nbsp;<a href="javascript:logout()" target="_self">logout</a></font>&nbsp;
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