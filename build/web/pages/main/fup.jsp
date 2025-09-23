<%@ page import="com.crux.util.JSPUtil"%><html><%

   String title = request.getParameter("title");
   String note = request.getParameter("note");
   String docName = request.getParameter("docName");
   String uc = request.getParameter("upload_count");
   String clrBut = request.getParameter("clearbutton");
   String file_id = request.getParameter("file_id");

   String fileDesc = (String)request.getAttribute("FILEDESC");

   if (fileDesc==null) fileDesc = docName;

   boolean useClearButton = "Y".equalsIgnoreCase(clrBut);

   int nuc = uc==null?1:Integer.parseInt(uc);

   Object uf = request.getAttribute("UPLOADED_FILE");
   Object ufd = request.getAttribute("UPLOADED_DESC");
   Object uff = request.getAttribute("IMAGE");
   boolean img = "Y".equals(request.getAttribute("IMAGE"));

   request.setAttribute("ENCTYPE","multipart/form-data");

   if (title==null) {
      out.print("<script>dialogReturn({id:'"+uf+"',text:'"+ufd+"', image:'"+uff+"'});window.close();</script>");
   }

   final JSPUtil jspUtil = new JSPUtil(request, response,title); %>
<script>
   f.EVENT.value='FILE_UPLOAD';
</script>
      <br>
      <br>
      <%=note%>
      <br>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td align=center>
                  <INPUT TYPE="hidden" NAME="group" value="<%=jspUtil.print(request.getParameter("group"))%>"/>
                  <%
                  for (int i=0;i<nuc;i++) {
                     %>
                     <%=file_id==null?"":("ID:"+file_id)%> Description: <%=jspUtil.getInputText("oFilen"+i+"|Description|string|255",null,fileDesc,150,JSPUtil.MANDATORY)%> File : <INPUT class=input TYPE="file" NAME="oFile<%=i%>"/><br>

                     <%
                  }
                  %>
                  <br>
                  <%=useClearButton?"<input type=button value=\"Clear\" onclick=\"dialogReturn({id:'',text:''});window.close();\">":""%>
                  <INPUT TYPE="submit" VALUE="Upload File">
                  <input type=button value="Cancel" onclick="window.close();">
               </td>
            </tr>
         </table>
<%= jspUtil.release()%>