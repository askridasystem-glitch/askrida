<%@ page import="com.crux.util.JSPUtil,
                 com.ots.codec.OTSCodec,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.LookUpUtil,
                 com.crux.util.DTOList,

                 com.crux.util.DateUtil
                 ,
                 com.crux.common.codedecode.Codec"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
    DTOList dlCurrency = (DTOList)request.getAttribute("curr_cb");
%>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   <body>
      <form name=f method=POST action="ctl.ctl" onSubmit="processReport();return false;">
         <input type=hidden name=EVENT value="ORDER_TRACKING_LIST">


         <table cellpadding=2 cellspacing=1 width="100%">
            <tr>
               <td>
                  <%=jspUtil.getHeader("NOTA DINAS")%>
               </td>
            </tr>
            <tr>
                <td>&nbsp;<br></td>
            </tr>
            <tr>
                <td class=title>Pengisian Form Nota Dinas</td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                 <table cellpadding=2 cellspacing=1>
                    <tr>
                         <td class=row0>Nomor Nota Dinas</td>
                         <td class=detail><%=jspUtil.getInputText("nd1",new FieldValidator("nd1","Digit pertama nomor Nota Dinas","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%> /
                         <%=jspUtil.getInputText("nd2",new FieldValidator("nd2","Digit kedua nomor Nota Dinas","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%> /
                         <%=jspUtil.getInputText("nd3",new FieldValidator("nd3","Digit ketiga nomor Nota Dinas","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%> /
                         <%=jspUtil.getInputText("nd4",new FieldValidator("nd4","Digit keempat nomor Nota Dinas","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%> /
                         <%=jspUtil.getInputText("nd5",new FieldValidator("nd5","Digit kelima nomor Nota Dinas","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                     <tr>
                            <td class=row1>Dari</td>
                            <td class=detail><%=jspUtil.getInputText("stDari",new FieldValidator("stDari","Dari","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Perihal</td>
                            <td class=detail><%=jspUtil.getInputTextArea("stPerihal",new FieldValidator("stPerihal","Jabatan Pemberi Kuasa","string",100,JSPUtil.MANDATORY),null,3,100,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row0>Nama Vendor</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaVendor",new FieldValidator("stNamaVendor","Nama Vendor","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Nomor Kontrak</td>
                            <td class=detail><%=jspUtil.getInputText("stNoKontrak",new FieldValidator("stNoKontrak","Nomor Kontrak","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>AWB</td>
                            <td class=detail><%=jspUtil.getInputText("stAWB",new FieldValidator("stAWB","AWB","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Nilai Perangkat CIP</td>
                            <td class=detail><%=jspUtil.getInputSelect("stCurrency",new FieldValidator("stCurrency","Mata uang","string",5,JSPUtil.MANDATORY),dlCurrency.getComboContent(),80,JSPUtil.MANUAL)%><%=jspUtil.getInputText("dbNilaiCIP",new FieldValidator("dbNilaiCIP","Nilai Perangkat CIP","money10.2",-1,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Tanggal Tiba</td>
                            <td class=detail><%=jspUtil.getInputText("dtTanggalTiba",new FieldValidator("dtTanggalTiba","Tanggal Tiba","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Nama Penerbangan Pengangkut</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaPenerbangan",new FieldValidator("stNamaPenerbangan","Nama Penerbangan Pengangkut","string",200,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                       <tr>
                            <td class=row0>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row1>Bea Masuk</td>
                            <td class=detail>Rp. <%=jspUtil.getInputText("dbBeaMasuk",new FieldValidator("dbBeaMasuk","Bea Masuk","money18.2",-1,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>PPN</td>
                            <td class=detail>Rp. <%=jspUtil.getInputText("dbPPN",new FieldValidator("dbPPN","PPN","money18.2",-1,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>PPh Ps 22</td>
                            <td class=detail>Rp. <%=jspUtil.getInputText("dbPPH",new FieldValidator("dbPPH","PPh Ps. 22","money18.2",-1,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>PnBp</td>
                            <td class=detail>Rp. <%=jspUtil.getInputText("dbPnbp",new FieldValidator("dbPnbp","PnBp","money18.2",-1,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Total</td>
                            <td class=detail>Rp. <%=jspUtil.getInputText("dbTotal",new FieldValidator("dbTotal","Total","money18.2",-1,JSPUtil.MANDATORY),null,150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row1>Tanggal Cetak</td>
                            <td class=detail><%=jspUtil.getInputText("dtTglCetak",new FieldValidator("dtTglCetak","Tanggal Cetak","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),150,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Nama yang menanda tangani</td>
                            <td class=detail><%=jspUtil.getInputText("stPenandaTangan",new FieldValidator("stPenandaTangan","Nama yang menanda tangani","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>NIK</td>
                            <td class=detail><%=jspUtil.getInputText("stNik",new FieldValidator("stNik","NIK","string",100,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Tembusan</td>
                            <td class=detail><%=jspUtil.getInputTextArea("stTembusan",new FieldValidator("stTembusan","Tembusan","string",100,JSPUtil.MANDATORY),null,5,100,200,JSPUtil.MANUAL)%></td>
                        </tr>

                  </table>
                </td>
             </tr>
             <tr>
                <td>
                <%=jspUtil.getButtonSubmit("bGenerate","Tampilkan Nota Dinas")%>
<%--                <%=jspUtil.getButtonNormal("bGenerate","Tampilkan try","window.open('rpt_try.rpt?user_id=admin','','toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480');")%>--%>
                </td>
             </tr>
         </table>
      </form>
   </body>
   <script>
    function processReport(){
         var nd;
         nd = f.nd1.value+"%2f"+f.nd2.value+"%2f"+f.nd3.value+"%2f"+f.nd4.value+"%2f"+f.nd5.value;

         var stUrl = "rpt_nd.rpt?nomor_nota="+nd;
         stUrl=stUrl+"&dari="+f.stDari.value;
         stUrl=stUrl+"&perihal="+escape(f.stPerihal.value);
         stUrl=stUrl+"&nama_vendor="+f.stNamaVendor.value;
         stUrl=stUrl+"&nomor_kontrak="+f.stNoKontrak.value;
         stUrl=stUrl+"&no_awb="+f.stAWB.value;
         stUrl=stUrl+"&currency="+f.stCurrency.value;
         stUrl=stUrl+"&nilai_cip="+f.dbNilaiCIP.value;
         stUrl=stUrl+"&tanggal_tiba="+f.dtTanggalTiba.value;
         stUrl=stUrl+"&nama_penerbangan="+f.stNamaPenerbangan.value;
         stUrl=stUrl+"&bea_masuk="+f.dbBeaMasuk.value;
         stUrl=stUrl+"&ppn="+f.dbPPN.value;
         stUrl=stUrl+"&pph="+f.dbPPH.value;
         stUrl=stUrl+"&pnbp="+f.dbPnbp.value;
         stUrl=stUrl+"&total="+f.dbTotal.value;
         stUrl=stUrl+"&tanggal_surat="+f.dtTglCetak.value;
         stUrl=stUrl+"&penanda_tangan="+f.stPenandaTangan.value;
         stUrl=stUrl+"&nik_penanda_tangan="+f.stNik.value;
         stUrl=stUrl+"&tembusan="+escape(f.stTembusan.value);


         window.open(stUrl,"","toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480");


    }


   </script>
</html>
