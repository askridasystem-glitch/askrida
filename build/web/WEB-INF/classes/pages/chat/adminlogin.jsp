<html>
<head>
<%= charset %>
<title>Chat Login(Admin)</title>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1">
<form method="post" action="<%= action %>" name="chat">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr align="center">
      <td><font face="Verdana, Arial, Helvetica, sans-serif"><b><font color="#000099">Admin Access</font></b></font></td>
    </tr>
    <tr align="center">
         <td nowrap> <font face="Verdana, Arial, Helvetica, sans-serif" size="-2" color="#990000">&nbsp;<%= errorMsg %></font></td>
   </tr>
  </table>
</table>
  <table width="40%" border="0" cellspacing="1" cellpadding="0" align="center">
    <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr bgcolor="#999999">
            <td>
              <table width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr bgcolor="#FFFFFF">
                  <td nowrap>
                    <table width="100%" border="0" cellspacing="1" cellpadding="1" align="center">
                      <tr>
                        <td nowrap> <font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b>
                          </b></font><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">Login
                          :</font></b></font> </td>
                        <td nowrap>
                          <input type="text" name="nickname" size="10" maxlength="<%= maxnicknamelength %>">
                        </td>
                      </tr>
                      <tr>
                        <td nowrap> <font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">Password
                          :</font></b></font> <font face="Verdana, Arial, Helvetica, sans-serif" size="-2">
                          </font></td>
                        <td nowrap>
                          <input type="password" name="password" size="10" maxlength="10">
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <div align="center"></div>
  <div align="center"><br>
    <input type="submit" name="Submit" value="Submit">
    <input type="reset" value="Reset">
    <input type="hidden" name="todo" value="<%= adminlogincommand %>">
  </div>
</form>
</body>
</html>
