<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.LOV,
                 java.util.Iterator,
                 com.crux.util.LookUpUtil,
                 com.crux.common.model.DTO,
                 java.util.Enumeration"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   String cap = request.getParameter("cap");
   String lov = request.getParameter("lov");
   String grup = request.getParameter("grup");
   String kabupaten2 = request.getParameter("kabupaten2");

   LOV list = (LOV) request.getAttribute("LIST");

   Enumeration nm = request.getParameterNames();
   
   final LOV kabupaten = (LOV) request.getAttribute("kabupaten");
   final LOV grupcompany = (LOV) request.getAttribute("grupcompany");

   boolean entity = lov.equalsIgnoreCase("LOV_ENTITY") || lov.equalsIgnoreCase("LOV_EntityFinance");

   final String[] attributeNames = list==null?null:list.getAttributeNames();
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden id=EVENT name=EVENT value="LOVPOP">
         <input type=hidden id=lov name=lov value="<%=jspUtil.print(lov)%>">
         <input type=hidden id=cap name=cap value="<%=jspUtil.print(cap)%>">
<%
   while (nm.hasMoreElements()) {
      String pf = (String) nm.nextElement();

      if (pf.indexOf("LVP")==0) {
         %>
         <input type=hidden id=<%=pf%> name=<%=pf%> value="<%=jspUtil.print(request.getParameter(pf))%>">
         <%
      }
   }

%>
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader2("Select "+cap)%>
            
            <% if(lov.equalsIgnoreCase("LOV_PostalCode")) 
            	{
            %>
            		<tr>
               			<td>
                  		Kabupaten
               			</td>
               			<td>
               			   : <%--  <%=jspUtil.getInputText("city|Search Key|string",null,null,230,JSPUtil.MANDATORY)%> --%>
               			   <%=jspUtil.getInputSelect("kabupaten|Kabupaten|string",null,kabupaten.setLOValue(kabupaten2), 200, JSPUtil.MANDATORY|JSPUtil.NOTEXTMODE)%>
            			</td>
            		</tr>
            		<tr>
               			<td>
                  		Daerah/Jalan/Kode Pos/Bangunan
               			</td>
               			<td>
               			   :  <%=jspUtil.getInputText("street|Search Key|string",null,null,250,JSPUtil.MANDATORY)%> 
               			</td>
               			<tr>
               			<td align="right">
               			<%=jspUtil.getButtonEvent("Search","LOVPOP")%>
               			</td>
               			<td>
               			<%=jspUtil.getButtonEvent("Create","CREATE_POSTALCODE")%>
               			</td>
               			</tr>
            		</tr>
                        <% } else if (lov.equalsIgnoreCase("LOV_PostalCode_Maipark")) {%>
                <tr>
                    <td>
                  		Provinsi
                    </td>
                    <td>
               			   : <%--  <%=jspUtil.getInputText("city|Search Key|string",null,null,230,JSPUtil.MANDATORY)%> --%>
                        <%=jspUtil.getInputSelect("kabupaten|Kabupaten|string", null, kabupaten.setLOValue(kabupaten2), 200, JSPUtil.MANDATORY | JSPUtil.NOTEXTMODE)%>
                    </td>
                </tr>
                <tr>
                    <td>
                  		Kecamatan/Kelurahan/Kode Pos
                    </td>
                    <td>
               			   :  <%=jspUtil.getInputText("street|Search Key|string", null, null, 250, JSPUtil.MANDATORY)%>
                    </td>
                <tr>
                    <td align="right">
                        <%=jspUtil.getButtonEvent("Search", "LOVPOP")%>
                    </td>
                    <td>
                        <%=jspUtil.getButtonEvent("Create", "CREATE_POSTALCODE")%>
                    </td>
                </tr>
         </tr>
            <% }else if(entity){ %>
                        
            		<tr>
                                <td>
                                     Search Nama/GL Code
                                   </td>
                                   <td>
                                     : <%=jspUtil.getInputText("search|Search Key|string",null,null,250,JSPUtil.MANDATORY)%> <%=jspUtil.getButtonEvent("Search","LOVPOP")%>
                                   </td>
            		</tr>
                        <tr>
                                   <td>
                                       Grup Perusahaan
                                   </td>
                                   <td>
                                      : <%=jspUtil.getInputSelect("grup|Grup|string",null,grupcompany.setLOValue(grup), 250, JSPUtil.NOTEXTMODE)%>
                                   </td>
            		</tr>
            <% } else { %>
                        <tr>
               			<td>
                  		Search <%=cap%>: <%=jspUtil.getInputText("search|Search Key|string",null,null,250,JSPUtil.MANDATORY)%> <%=jspUtil.getButtonEvent("Search","LOVPOP")%>
               			</td>
                                
            		</tr>
            <% } %>
 
<% if (list!=null) {%>
            <tr>
               <td>
<% if (list instanceof LookUpUtil ) {%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
<% for (int i = 0; i < attributeNames.length; i++) {
   String attributeName = attributeNames[i];

%>
                        <td><%=attributeName%></td>
<% } %>
                     </tr>
<%
   Iterator it = list.getCodeIterator();

   int i=0;

   while (it.hasNext()) {
      String code = (String) it.next();
      String desc = list.getComboDesc(code);
      i++;
%>
                     <tr class=row<%=i%2%>>
                        <td><a href="#" onclick="return zselect('<%=jspUtil.print(code)%>','<%=jspUtil.print(JSPUtil.jsEscape(desc))%>');"><%=jspUtil.print(code)%></a></td>
                        <td><a href="#" onclick="return zselect('<%=jspUtil.print(code)%>','<%=jspUtil.print(JSPUtil.jsEscape(desc))%>');"><%=jspUtil.print(desc)%></a></td>
                     </tr>
<% } %>
<% } %>
                  </table>
                  <div id="tblinst"></div>
<% if (list instanceof DTOList ) {
   final DTOList l = (DTOList)list;

   %>
<script>
   list=<%=l.getJSObject()%>;
   var s="";
   s+="<table cellpadding=2 cellspacing=1>";
   s+="<tr class=header>";
<%
   for (int i = 0; i < attributeNames.length; i++) {
      String attributeName = attributeNames[i];
      %>s+="<td><%=attributeName%></td>";<%
   }
%>
   s+="</tr>";

   for (var i=1;i<list.length;i++) {
      s+="<tr class=row"+(i%2)+">";
   <%
      for (int i = 0; i < attributeNames.length; i++) {
         String attributeName = attributeNames[i];

         if (i==0) attributeName = "text";
         if (i==1) attributeName = "value";

         %>s+="<td><a href=\"#\" onclick=\"return wselect("+i+")\">"+list[i]['<%=attributeName%>']+"</a></td>";<%
      }
   %>
      s+="</tr>";
   }

   s+="</table>";

   //alert(s);

   docEl('tblinst').innerHTML=s;
</script>
<% } %>
               </td>
            </tr>
<% } %>
         </table>
      </form>
   </body>
</html>
<script>
   function zselect(c,d) {
      window.returnValue={code:c,desc:d};
      window.close();
      return false;
   }

   function wselect(i) {
      var x=list[i];
      x.code=x.text;
      x.desc=x.value;

      window.returnValue=x;
      window.close();
      return false;
   }
   
   function createPostCode(){
      window.close();
   }
</script>