<%@ page import="com.crux.util.JSPUtil,
                 com.ots.codec.OTSCodec,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.LookUpUtil,
                 com.crux.util.DTOList,
                 com.crux.util.DateUtil,
                 com.crux.common.codedecode.Codec,
                 java.util.Date"%>
<html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>

<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<% final DTOList dlCurrency =  (DTOList) request.getAttribute("curr_cb");%>
<body>
      <form name=f method=POST action="ctl.ctl" onSubmit="processReport();return false;">
         <input type=hidden name=EVENT value="ORDER_TRACKING_LIST">


         <table cellpadding=2 cellspacing=1 width="100%">
            <tr>
               <td>
                  <%=jspUtil.getHeader("LAPORAN REALISASI KONTRAK")%>
               </td>
            </tr>
            <tr>
                <td>&nbsp;<br></td>
            </tr>
            <tr>
                <td class=title>Lengkapi informasi di bawah ini : </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                 <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td class=row0>Nomor Kontrak</td>
                        <td class=detail><%=jspUtil.getInputText("nomor_kontrak",new FieldValidator("nomor_kontrak", "Nomor Kontrak","string",64,JSPUtil.MANDATORY),null,200,JSPUtil.MANDATORY)%></td>
                    </tr>
                    <tr>
                        <td class=row1>Rekanan Pengadaan</td>
                        <td class=detail><%=jspUtil.getInputText("rekanan_pengadaan",new FieldValidator("rekanan_pengadaan", "Rekanan Pengadaan","string",240,JSPUtil.MANDATORY),null,300,JSPUtil.MANDATORY)%> <%=jspUtil.getButtonNormal("btnVendor","...","selectVendor()")%><%=jspUtil.getHiddenText("vendor_id",null,null,200)%></td>
                    </tr>
                    <tr>
                        <td class=row0>Mata Uang</td>
                        <td class=detail><%=jspUtil.getInputSelect("mata_uang",new FieldValidator("mata_uang", "Mata Uang","string",5,JSPUtil.MANDATORY),dlCurrency.getComboContent(),200,JSPUtil.MANDATORY)%></td>
                    </tr>
                    <tr>
                        <td class=row1>AWB</td>
                        <td class=detail><%=jspUtil.getInputText("awb",new FieldValidator("awb", "AWB","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANDATORY)%></td>
                    </tr>
                    <tr>
                        <td class=row0>Tanggal Tiba</td>
                        <td class=detail><%=jspUtil.getInputText("tanggal_tiba",new FieldValidator("tanggal_tiba", "Tanggal Tiba","date",-1,JSPUtil.MANDATORY),null,200,JSPUtil.MANDATORY)%></td>
                    </tr>
                 </table>
                </td>
             </tr>
             <tr>
                <td>
                <%=jspUtil.getButtonSubmit("bGenerate","Tampilkan Laporan Realisasi Kontrak")%>
<%--                <%=jspUtil.getButtonNormal("bGenerate","Tampilkan try","window.open('rpt_try.rpt?user_id=admin','','toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480');")%>--%>
                </td>
             </tr>
         </table>
      </form>
   </body>
   <script>
    function processReport(){
         var stUrl = "rpt_real_kontrak.rpt?"
         stUrl=stUrl+"nomor_kontrak="+f.nomor_kontrak.value;
         stUrl=stUrl+"&rekanan_pengadaan="+f.vendor_id.value;
         stUrl=stUrl+"&mata_uang="+f.mata_uang.value;
         stUrl=stUrl+"&awb="+f.awb.value;
         stUrl=stUrl+"&tanggal_tiba="+f.tanggal_tiba.value;

         window.open(stUrl,"","toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480");
       }

       function selectVendorCallBack(o) {
          if (o!=null) {
             f.rekanan_pengadaan.value = o.DESC;
             f.vendor_id.value = o.VENDORID;
          }
       }

       function selectVendor() {
          openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
       }


   </script>
</html>
