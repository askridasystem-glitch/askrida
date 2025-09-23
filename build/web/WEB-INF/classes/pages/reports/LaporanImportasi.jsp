<%@ page import="com.crux.util.JSPUtil,
                 com.ots.codec.OTSCodec,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.LookUpUtil,
                 com.crux.util.DTOList,
                 com.crux.util.DateUtil,
                 com.crux.common.codedecode.Codec,
                 java.util.Date"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
    LookUpUtil luYear = new LookUpUtil();
    String stThisDay =  DateUtil.getDateStr(DateUtil.getNewDate());
    int iYear = Integer.parseInt(stThisDay.substring(stThisDay.lastIndexOf("/")+1))-10;

    for(int i =0; i<11;i++){
        luYear.add(Integer.toString(iYear+i),new Integer(iYear+i));
    }
%>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   <body>
      <form name=f method=POST action="ctl.ctl" onSubmit="processReport();return false;">
         <input type=hidden name=EVENT value="ORDER_TRACKING_LIST">


         <table cellpadding=2 cellspacing=1 width="100%">
            <tr>
               <td>
                  <%=jspUtil.getHeader("LAPORAN IMPORTASI")%>
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
                        <td class=row0>Periode (Bulan)</td>
                        <td class=detail><%=jspUtil.getInputSelect("bulan",new FieldValidator("bulan", "Bulan","string",2,JSPUtil.MANDATORY),Codec.MonthCombo.getLookUp().getComboContent(),200,JSPUtil.MANDATORY)%></td>
                    </tr>
                    <tr>
                        <td class=row1>Periode (Tahun)</td>
                        <td class=detail><%=jspUtil.getInputSelect("tahun",new FieldValidator("tahun", "Tahun","string",4,JSPUtil.MANDATORY),luYear.getComboContent(),200,JSPUtil.MANDATORY)%></td>
                    </tr>
                    </tr>

                 </table>
                </td>
             </tr>
             <tr>
                <td>
                <%=jspUtil.getButtonSubmit("bGenerate","Tampilkan Laporan Importasi")%>
<%--                <%=jspUtil.getButtonNormal("bGenerate","Tampilkan try","window.open('rpt_try.rpt?user_id=admin','','toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480');")%>--%>
                </td>
             </tr>
         </table>
      </form>
   </body>
   <script>
    function processReport(){

         var stUrl = "rpt_importasi.rpt?"
         stUrl=stUrl+"bulan="+f.bulan.options[f.bulan.selectedIndex].text;
         stUrl=stUrl+"&tahun="+f.tahun.value;
         stUrl=stUrl+"&month="+f.bulan.value;


         window.open(stUrl,"","toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480");


    }


   </script>
</html>
