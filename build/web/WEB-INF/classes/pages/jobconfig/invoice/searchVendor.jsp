<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.validation.FieldValidator,
                 com.ots.vendor.model.VendorView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <%

      final DTOList vendors = (DTOList) request.getAttribute("VENDOR_LIST");
   %>
   <body>
   <script>
    var bgdefault;
      function doSelect() {
         if (f.vendorid)
         {
            if (!f.vendorid.length)
            {
               dialogReturn({VENDORID:f.vendorid.value,DESC:f.vendordesc.value});
            }else
            {
               dialogReturn({VENDORID:f.vendorid[f.idxItem.value].value,DESC:f.vendordesc[f.idxItem.value].value});
            }
            self.close();
         }
      }

      function selectVendor(a)
      {
         f.idxItem.value=a;
      }

      function overlight(othis,backg){
        bgdefault = backg;
        othis.style.backgroundColor='cyan';
        othis.style.cursor = 'hand';
      }
      function outlight(othis){
         othis.style.backgroundColor =bgdefault;
      }
   </script>
   <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="VENDOR_SEARCH">
         <input type=hidden name=idxItem>
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("SEARCH VENDOR")%>
            <tr>
               <td>
                  <%=jspUtil.getInputText("keyword",new FieldValidator("kw","Keyword","string",50),null,200)%>
                  <%=jspUtil.getButtonSubmit("bsearch","Search")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>VENDOR ID</td>
                        <td>VENDOR NAME</td>
                   </tr>
<%
   if (vendors!=null)
   {
      for (int i = 0; i < vendors.size(); i++) {
         VendorView vnv = (VendorView) vendors.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=itemc onclick="selectVendor('<%=i%>')" ondblclick="selectVendor('<%=i%>');doSelect();"></td>
                        <td><%=jspUtil.getInputText("vendorid",null,vnv.getStVendorID(),100,JSPUtil.READONLY)%></td>
                        <td><%=jspUtil.getInputText("vendordesc",null,vnv.getStVendorName(),150,JSPUtil.READONLY)%></td>
                     </tr>
<%
      }
   }
%>
                  </table>
                  <%=jspUtil.getButtonNormal("bselect","Select","doSelect()")%>
               </td>
            </tr>
            <tr>
               <td>

               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   enablebutton(f.bselect, true);
</script>