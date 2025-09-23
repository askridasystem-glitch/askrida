<%@ page import="com.crux.web.controller.SessionManager" %>
<%

    final String userID = SessionManager.getInstance().getSession().getStUserID();
    final String branch = SessionManager.getInstance().getSession().getStBranch()!=null?SessionManager.getInstance().getSession().getStBranch():"00";

    String url = "https://helpdesk.askrida.co.id/mandiri_app/index.php?userid="+ userID +"&cabang="+branch;

%>
<html>

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>JSP Page</title>

    </head>

    <body>
       <iframe src="<%=url%>" width=100% height=100% frameborder="0"  noresize=noresize   > </iframe>

    </body>

</html>