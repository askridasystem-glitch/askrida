<%@ page import="com.crux.util.JSPUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl" enctype="multipart/form-data">
         <input type=hidden name=EVENT value="FILEMAN_DO_UPLOAD">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("UPLOAD FILES")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>File 1</td><td>:</td><td><input type=file name=file1></td>
                        <td>File 2</td><td>:</td><td><input type=file name=file2></td>
                        <td>File 3</td><td>:</td><td><input type=file name=file3></td>
                        <td>File 4</td><td>:</td><td><input type=file name=file4></td>
                        <td>File 5</td><td>:</td><td><input type=file name=file5></td>
                     </tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bUpload","Upload")%>
                  <%=jspUtil.getButtonNormal("bCancel","Cancel","")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>