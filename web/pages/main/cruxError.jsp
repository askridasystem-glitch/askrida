<%@ page import="com.crux.web.form.ErrorForm,
                 com.crux.util.JSPUtil"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
   <%
      final ErrorForm form = (ErrorForm)frame.getForm();

      final String state = request.getParameter("state");
      final String ticket = request.getParameter("ticket");

      Throwable t = (Throwable)form.getErrorDesc();

      while (true) {
         if (t.getClass().getName().equalsIgnoreCase("org.apache.jasper.JasperException")) {
            t = (Throwable) t.getClass().getMethod("getRootCause",null).invoke(t,null);
         }
         else if (t.getCause()!=null) {
            t=t.getCause();
         }
         else break;
      }

      String msg = t.getMessage();

      if (msg==null) msg=t.toString();

      final boolean pop = request.getParameter("pop")!=null;

   %>
<script>
   function clip() {
      window.clipboardData.setData("Text", docEl('msgx').innerText);   
   }

</script>

   <table cellpadding=2 cellspacing=1 width="100%" height="100%">
      <tr>
         <td align=center valign=center>
            <c:evaluate when="<%=state==null%>" >
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td align=center>
                        <div id="msgx">
                        <%=JSPUtil.print(msg)%><br><br><br></div>
                        <c:evaluate when="<%=pop%>"><button onclick="window.close();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;&nbsp;</button></c:evaluate>
                        <button onclick="window.location='error.crux?ticket=<%=ticket%>&state=details<%=pop?"&pop=y":""%>'">Details &gt;&gt;</button>
                        <button onclick="clip()">Clipboard</button>
                     </td>
                  </tr>
               </table>
            </c:evaluate>
            <c:evaluate when="<%="details".equalsIgnoreCase(state)%>" >
            <script>
               window.parent.dialogHeight="600px";
               window.parent.dialogWidth="700px";

            </script>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                  <td align=center>
                     <div id="msgx">
                     Please take note error details below for problem diagnosis
                     <br>
                     <br>
                     <%=JSPUtil.print(msg)%><br>
                     <br>
                     <br>
                     <b><font color=red>
            <%

               //while (t.getCause()!=null) t=t.getCause();

               out.print("Error Description : "+t.toString());
               out.print("<br>");


               final StackTraceElement[] stackTrace = t.getStackTrace();

               int l=0;

               for (int i = 0; i < stackTrace.length; i++) {
                  StackTraceElement stackTraceElement = stackTrace[i];

                  final String cls = stackTraceElement.getClassName();

                  if (
                          (cls.indexOf("crux")>=0) ||
                          (cls.indexOf("webfin")>=0) ||
                          (cls.indexOf(".jsp")>=0)
                  ) {
                     l++;
                     out.print(l+". ");
                     out.print(stackTraceElement.getFileName()+":"+stackTraceElement.getLineNumber());
                     out.print("<br>");

                     //if (l>0) break;
                  }


               }
                     %>

                 </font></b><br><br></div>
                  <c:evaluate when="<%=pop%>"><button onclick="window.close();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;&nbsp;</button>
                  <button onclick="clip()">Clipboard</button>
                  </c:evaluate>
                  </td>
                  </tr>
               </table>

            </c:evaluate>
         </td>
      </tr>
   </table>
</c:frame>