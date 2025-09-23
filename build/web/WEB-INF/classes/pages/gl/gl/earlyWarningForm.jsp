<%@ page import="com.crux.web.controller.SessionManager" %>
<%

    final String userID = SessionManager.getInstance().getSession().getStUserID();
    final String branch = SessionManager.getInstance().getSession().getStBranch();

%>
<html>

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>JSP Page</title>

    </head>

    <body>
        <table>
            <tr>
                <td>user :</td> <td><%=userID%></td>
                <td>cabang : </td><td><%=branch%></td>
            </tr>
        </table>
       <iframe src="http://helpdesk.askrida.co.id:8080/ews_deploy/index.jsp" width=100% height=100% frameborder="0"  noresize=noresize   > </iframe>

    </body>

</html>