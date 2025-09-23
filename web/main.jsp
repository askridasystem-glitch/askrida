<%@ page import="com.crux.util.JSPUtil"%><html>
<% JSPUtil jspUtil = new JSPUtil(request, response);

   String sudahUbahPassword = jspUtil.cekSudahUbahPassword();
   String welcome = sudahUbahPassword.equalsIgnoreCase("false")?"CHANGE_PASSWORD":"MAIN_WELCOME";
   boolean sudahUbah = sudahUbahPassword.equalsIgnoreCase("false")?false:true;
%>

<frameset rows="130,*,1" cols="*" frameborder="NO" border="1" framespacing="0">
  <frame name="topFrame" target="mainFrame" scrolling="NO" src="<%=jspUtil.getPageURL("FRAME_TOP")%>">
  <%if(sudahUbah){%>
            <frameset id="frmMenu" cols="185,*" frameborder="NO" border="1" framespacing="0">
            <frame name="leftFrame" target="basefrm" scrolling="yes" src="<%=jspUtil.getPageURL("FRAME_LEFT")%>">
       <%}%>
    <frameset id="frmMain" rows="25,*" cols="*" frameborder="NO" border="1" framespacing="0">
        <frame scrolling="NO"  name="bottomFrame" src="<%=jspUtil.getPageURL("FOOTER")%>">
        <frame name="basefrm" src="<%=jspUtil.getPageURL(welcome)%>">
    </frameset> 
  </frameset>
</frameset>

<script language=JavaScript1.2>
   window.title = "";
   
</script>

</html>


