<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.validation.FieldValidator,
                 com.ots.item.model.ItemView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <%

      final DTOList items   = (DTOList) request.getAttribute("LIST_ITEM");
      final String vendorId = (String) request.getAttribute("VENDOR");
   %>
   <body>
   <script>
    var bgdefault;
    var itemIds,itemDescs,itemCodes;
    var i=0;
      function doSelect() {
       if (f.itemid!=null)
       {
            if (!f.itemid.length)
            {
               dialogReturn({ITEMID:f.itemid.value,DESC:f.itemdesc.value,ITEMCODE:f.itemcode.value});
            }else
            {
               for (i=0;i<f.itemid.length;i++)
               {
                     if (i==0)
                     {
                        if (f.itemc[0].checked)
                        {
                           itemIds     = f.itemid[0].value;
                           itemDescs   = f.itemdesc[0].value;
                           itemCodes   = f.itemcode[0].value;
                        }
                     } else
                     {
                        if (f.itemc[i].checked)
                        {
                           itemIds     = itemIds   +';' +  f.itemid[i].value;
                           itemDescs   = itemDescs + ';' + f.itemdesc[i].value;
                           itemCodes   = itemCodes + ';' + f.itemcode[i].value;
                        }
                     }
               }
               dialogReturn({ITEMID:itemIds,DESC:itemDescs,ITEMCODE:itemCodes});
            }
         self.close();

        }
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
         <input type=hidden name=EVENT value="ITEM_SEARCH">
         <input type=hidden name=vendorID value="<%=vendorId%>">
         <input type=hidden name=idxItem>
         <input type=hidden name=itemIds>
         <input type=hidden name=itemDescs>
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("SEARCH ITEM")%>
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
                        <td>ITEM CODE</td>
                        <td>DESCRIPTION</td>
                   </tr>
<%
   if (items!=null)
   {
      for (int i = 0; i < items.size(); i++) {
         ItemView itv = (ItemView) items.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=checkbox name=itemc><%=jspUtil.getHiddenText("itemid",null,itv.getLgItemId(),100)%></td>
                        <td><%=jspUtil.getInputText("itemcode",null,itv.getStItemCode(),100,JSPUtil.READONLY)%></td>
                        <td><%=jspUtil.getInputText("itemdesc",null,itv.getStItemDesc(),150,JSPUtil.READONLY)%></td>
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