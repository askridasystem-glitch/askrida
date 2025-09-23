<%@ page import="com.crux.util.JSPUtil,
                 com.ots.codec.OTSCodec,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.LookUpUtil,
                 com.crux.util.DTOList,
                 com.ots.asn.model.ASNView,
                 com.ots.report.filter.OrderTrackingFilter,
                 com.ots.invoice.validation.InvoiceValidator,
                 com.crux.util.DateUtil,
                 com.ots.invoice.model.InvoiceView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final OrderTrackingFilter f = (OrderTrackingFilter) request.getAttribute("FILTER");

   final LookUpUtil ordTrackkeys = OrderTrackingFilter.SearchKeys.getLookUp();
   final LookUpUtil locStatuskeys = OTSCodec.LocationStatus.getLookUp();

   ordTrackkeys.setLOValue(f.stSearchKey);
   locStatuskeys.setLOValue(f.stLocationStatus);

   final DTOList ordTrackList = (DTOList)request.getAttribute("LIST_ORDER_TRACKING");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="ORDER_TRACKING_LIST">

         <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("ORDER TRACKING")%>
               </td>
            </tr>
            <tr>
            <td>
            <table>
            <tr><td>
            Find :
            </td></tr>
            <tr>
               <td weight="25%">
                  <%=jspUtil.getInputSelect("skey",null,ordTrackkeys,100)%>
               </td>
               <td>:</td>
               <td>
               <%=jspUtil.getInputText("sval|Keyword|string|50",null,f.stSearchWord,200)%>
               </td>
            </tr>
            <tr>
               <td weight="25%">
                  Status Location
               </td><td>:</td>
               <td><%=jspUtil.getInputSelect("locstat",null,locStatuskeys,200)%></td>
            </tr>
            <tr>
               <td weight="25%">
                  AW Date
               </td>
               <td>:</td>
               <td><%=jspUtil.getInputText("awDate", InvoiceValidator.vfInvoiceDate, f.stAirWayBillDate, 150,JSPUtil.MANUAL)%></td>
               <td><%=jspUtil.getButtonSubmit("bsearch","Search")%></td>
            </tr>
            </table>
            </td>
            </tr>
            <tr height=4>
               <td bgcolor="000040">
               </td>
            </tr>
<% if (ordTrackList!=null) {%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request, ordTrackList)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td><%=jspUtil.getSortHeader(request, ordTrackList, "Vendor Name", "vendor_name")%></td>
                        <td><%=jspUtil.getSortHeader(request, ordTrackList, "AW Number", "aw_number")%></td>
                        <td><%=jspUtil.getSortHeader(request, ordTrackList, "AW Date", "aw_date")%></td>
                        <td>Invoice Number</td>
                        <td>PL Number</td>
                        <td><%=jspUtil.getSortHeader(request, ordTrackList, "Status", "location_status")%></td>
                     </tr>
<%
   for (int i = 0; i < ordTrackList.size(); i++) {
      ASNView asn = (ASNView) ordTrackList.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><%=jspUtil.print(asn.getStVendorName())%></td>
                        <td><%=jspUtil.print(asn.getStAWNumber())%></td>
                        <td><%=jspUtil.print(asn.getDtAWDate())%></td>
                        <td>
                           <%
                              DTOList invoices = asn.getInvoices();
                              if (invoices!=null)
                              {
                                 for (int j=0;j<invoices.size();j++)
                                 {
                                    InvoiceView inv = (InvoiceView)invoices.get(j);
                                    out.println(jspUtil.print(inv.getStInvoiceNumber()));
                                    if (j<invoices.size()-1 && invoices.size()>1)
                                    {
                                       out.println(",");
                                    }
                                 }
                              }
                           %>
                        </td>
                        <td>
                           <%
                              if (invoices!=null)
                              {
                                 for (int j=0;j<invoices.size();j++)
                                 {
                                    InvoiceView inv = (InvoiceView)invoices.get(j);
                                    out.println(jspUtil.print(inv.getStPLNumber()));
                                    if (j<invoices.size()-1 && (inv.getStPLNumber()!=null && !"".equals(inv.getStPLNumber())))
                                    {
                                       out.println(",");
                                    }
                                 }
                              }
                           %>
                        </td>
                        <td><%=OTSCodec.LocationStatus.getLookUp().getValue(jspUtil.print(asn.getStLocationStatus()))%></td>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr height=4>
               <td bgcolor="000040">
               </td>
            </tr>
<%}%>
            <tr>
               <td>
                  <input type=hidden name=asnid value="">
               </td>
            </tr>
         </table>
      </form>
   </body>
   <script>
   function changePageList() {
      f.EVENT.value='ORDER_TRACKING_LIST';
      f.submit();
   }
   </script>
</html>
