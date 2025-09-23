<html>
<head>
<%= charset %>
<title>Open ChatRoom</title>
</head>
<body bgcolor="#FFFFFF" leftmargin="1" topmargin="1" marginwidth="1" marginheight="1">
<form method="post" action="<%= action %>" name="chat">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr align="center">
      <td><font face="Verdana, Arial, Helvetica, sans-serif"><b><font color="#000099">Open
        ChatRoom</font></b></font></td>
    </tr>
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
                          </b></font><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">Name
                          :</font></b></font> </td>
                        <td nowrap>
                          <input type="text" name="roomname" size="16" maxlength="16">
                        </td>
                      </tr>
                      <tr>
                        <td nowrap> <font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">Subject
                          :</font></b></font> <font face="Verdana, Arial, Helvetica, sans-serif" size="-2">
                          </font></td>
                        <td nowrap>
                          <input type="text" name="subject" size="16" maxlength="16">
                        </td>
                      </tr>
                      <tr>
                        <td nowrap><font face="Verdana, Arial, Helvetica, sans-serif" size="-1"><b><font color="#000099">Max.
                          Users :</font></b></font> <font face="Verdana, Arial, Helvetica, sans-serif" size="-2">
                          </font></td>
                        <td nowrap>
                          <select name="maxusers">
                            <option value="5">5</option>
                            <option value="10">10</option>
                            <option value="15">15</option>
                            <option value="20">20</option>
                            <option value="25">25</option>
                            <option value="30">30</option>
                            <option value="35">35</option>
                            <option value="40">40</option>
                            <option value="45">45</option>
                            <option value="50" selected>50</option>
                            <option value="55">55</option>
                            <option value="60">60</option>
                            <option value="65">65</option>
                            <option value="70">70</option>
                            <option value="75">75</option>
                            <option value="80">80</option>
                            <option value="85">85</option>
                            <option value="90">90</option>
                            <option value="95">95</option>
                            <option value="100">100</option>
                          </select>
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
    <input type="submit" name="Submit" value="Open">
    <input type="reset" value="Reset">
    <input type="hidden" name="todo" value="open">
  </div>
</form>
</body>
</html>
