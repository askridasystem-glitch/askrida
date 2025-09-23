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
   <body>
      <form name=f method=POST action="ctl.ctl" onSubmit="processReport();return false;">
         <input type=hidden name=EVENT value="ORDER_TRACKING_LIST">

         <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
         <table cellpadding=2 cellspacing=1 width="100%">
            <tr>
               <td>
                  <%=jspUtil.getHeader("SURAT KUASA")%>
               </td>
            </tr>
            <tr>
                <td>&nbsp;<br></td>
            </tr>
            <tr>
                <td class=title>Pengisian Form Surat Kuasa </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                 <table cellpadding=2 cellspacing=1>
                    <tr class=header>
                        <td colspan=2 >Tipe Surat</td>
                        <td colspan=9 >Nomor Surat</td>
                     </tr>
                     <tr>
                         <td class=row0><%=jspUtil.getInputCheck("chkSk1",null,false)%></td>
                         <td class=row0>Surat Kuasa dan Pernyataan ke Bandara Soekarno Hatta 1</td>
                         <td class=row0><%=jspUtil.getInputText("sk1_1",new FieldValidator("sk1_1","1st Digit of Surat Kuasa 1","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk1_2",new FieldValidator("sk1_2","2nd Digit of Surat Kuasa 1","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk1_3",new FieldValidator("sk1_3","3rd Digit of Surat Kuasa 1","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk1_4",new FieldValidator("sk1_4","4th Digit of Surat Kuasa 1","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk1_5",new FieldValidator("sk1_5","5th Digit of Surat Kuasa 1","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                      <tr>
                         <td class=row1><%=jspUtil.getInputCheck("chkSk2",null,false)%></td>
                         <td class=row1>Surat Kuasa dan Pernyataan ke Bandara Soekarno Hatta 2</td>
                         <td class=row1><%=jspUtil.getInputText("sk2_1",new FieldValidator("sk2_1","1st Digit of Surat Kuasa 2","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk2_2",new FieldValidator("sk2_2","2nd Digit of Surat Kuasa 2","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk2_3",new FieldValidator("sk2_3","3rd Digit of Surat Kuasa 2","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk2_4",new FieldValidator("sk2_4","4th Digit of Surat Kuasa 2","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk2_5",new FieldValidator("sk2_5","5th Digit of Surat Kuasa 2","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                     <tr>
                         <td class=row0><%=jspUtil.getInputCheck("chkSk3",null,false)%></td>
                         <td class=row0>Surat Kuasa dan Pernyataan ke Tanjung Priok 2</td>
                         <td class=row0><%=jspUtil.getInputText("sk3_1",new FieldValidator("sk3_1","1st Digit of Surat Kuasa 3","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk3_2",new FieldValidator("sk3_2","2nd Digit of Surat Kuasa 3","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk3_3",new FieldValidator("sk3_3","3rd Digit of Surat Kuasa 3","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk3_4",new FieldValidator("sk3_4","4th Digit of Surat Kuasa 3","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk3_5",new FieldValidator("sk3_5","5th Digit of Surat Kuasa 3","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                     <tr>
                         <td class=row1><%=jspUtil.getInputCheck("chkSk4",null,false)%></td>
                         <td class=row1>Surat Kuasa dan Pernyataan ke Halim Perdana Kusuma</td>
                         <td class=row1><%=jspUtil.getInputText("sk4_1",new FieldValidator("sk4_1","1st Digit of Surat Kuasa 4","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk4_2",new FieldValidator("sk4_2","2nd Digit of Surat Kuasa 4","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk4_3",new FieldValidator("sk4_3","3rd Digit of Surat Kuasa 4","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk4_4",new FieldValidator("sk4_4","4th Digit of Surat Kuasa 4","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row1>/</td>
                         <td class=row1><%=jspUtil.getInputText("sk4_5",new FieldValidator("sk4_5","5th Digit of Surat Kuasa 4","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                     <tr>
                         <td class=row0><%=jspUtil.getInputCheck("chkSk5",null,false)%></td>
                         <td class=row0>Surat Kuasa Persetujuan Pengeluaran Barang dari Pulo Gadung</td>
                         <td class=row0><%=jspUtil.getInputText("sk5_1",new FieldValidator("sk5_1","1st Digit of Surat Kuasa 5","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk5_2",new FieldValidator("sk5_2","2nd Digit of Surat Kuasa 5","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk5_3",new FieldValidator("sk5_3","3rd Digit of Surat Kuasa 5","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk5_4",new FieldValidator("sk5_4","4th Digit of Surat Kuasa 5","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                         <td class=row0>/</td>
                         <td class=row0><%=jspUtil.getInputText("sk5_5",new FieldValidator("sk5_5","5th Digit of Surat Kuasa 5","string",50,JSPUtil.MANDATORY),null,50,JSPUtil.MANUAL)%></td>
                     </tr>
                 </table>
              </td>
           </tr>
           <tr>
              <td><br></td>
           </tr>
           <tr>
                <td class=title>Lengkapi Informasi Di bawah ini</td>
           </tr>
           <tr><td>&nbsp;</td></tr>
           <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td class=row0>Nama Pemberi Kuasa</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaPemberiKuasa",new FieldValidator("stNamaPemberiKuasa","Nama Pemberi Kuasa","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Jabatan Pemberi Kuasa</td>
                            <td class=detail><%=jspUtil.getInputText("stJabatanPemberiKuasa",new FieldValidator("stJabatanPemberiKuasa","Jabatan Pemberi Kuasa","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row1>Nama Perusahaan yang diberi kuasa</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaDiberiKuasa",new FieldValidator("stNamaDiberiKuasa","Nama Perusahaan yang diberi Kuasa","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>NPWP</td>
                            <td class=detail><%=jspUtil.getInputText("stNPWP",new FieldValidator("stNPWP","NPWP Perusahaan yang diberi kuasa","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Alamat</td>
                            <td class=detail><%=jspUtil.getInputTextArea("stAlamatDiberiKuasa",new FieldValidator("stAlamatDiberiKuasa","Alamat Perusahaan yang diberi kuasa","string",200,JSPUtil.MANDATORY),null,3,50,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Nama orang yang diberi kuasa</td>
                            <td class=detail><%=jspUtil.getInputText("stNmOrangDiberiKuasa",new FieldValidator("stNmOrangDiberiKuasa","Nama orang yang diberi kuasa","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row0>Pengirim(Vendor Import)</td>
                            <td class=detail><%=jspUtil.getInputText("stPengirimImport",new FieldValidator("stPengirimImport","Vendor Import","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Nama Barang</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaBarang",new FieldValidator("stNamaBarang","Nama Barang","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                       <tr>
                            <td class=row0>Jumlah koli/kgs</td>
                            <td class=detail><%=jspUtil.getInputText("dbJmlKoli",new FieldValidator("dbJmlKoli","Jumlah Koli","money10.0",-1,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;Package&nbsp;&nbsp;<%=jspUtil.getInputText("dbJmlKgs",new FieldValidator("dbJmlKgs","Jumlah Kg","money10.2",-1,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;Kg</td>
                        </tr>
                        <tr>
                            <td class=row1>Negara Asal</td>
                            <td class=detail><%=jspUtil.getInputText("stNegaraAsal",new FieldValidator("stNegaraAsal","Negara Asal","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Ex. Pesawat</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaPesawat",new FieldValidator("stNamaPesawat","Nama Pesawat","string",200,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stTglPesawat",new FieldValidator("stTglPesawat","Tanggal Pesawat","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Ex. Kapal</td>
                            <td class=detail><%=jspUtil.getInputText("stNamaKapal",new FieldValidator("stNamaKapal","Nama Kapal","string",200,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stTglKapal",new FieldValidator("stTglKapal","Tanggal Kapal","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Mawb/Hawb</td>
                            <td class=detail><%=jspUtil.getInputText("stMawb",new FieldValidator("stMawb","MAWB","string",200,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stHawb",new FieldValidator("stHawb","HAWB","string",200,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>B/L</td>
                            <td class=detail><%=jspUtil.getInputText("stBL",new FieldValidator("stBL","B/L","string",200,JSPUtil.MANDATORY),null,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Nomor Invoice</td>
                            <td class=detail><%=jspUtil.getInputText("stNomorInvoice1",new FieldValidator("stNomorInvoice1","Nomor Invoice 1","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stNomorInvoice2",new FieldValidator("stNomorInvoice2","Nomor Invoice 2","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=detail>&nbsp;</td>
                            <td class=detail><%=jspUtil.getInputText("stNomorInvoice3",new FieldValidator("stNomorInvoice3","Nomor Invoice 3","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stNomorInvoice4",new FieldValidator("stNomorInvoice4","Nomor Invoice 4","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=detail>&nbsp;</td>
                            <td class=detail><%=jspUtil.getInputText("stNomorInvoice5",new FieldValidator("stNomorInvoice5","Nomor Invoice 5","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stNomorInvoice6",new FieldValidator("stNomorInvoice6","Nomor Invoice 6","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=detail>&nbsp;</td>
                            <td class=detail><%=jspUtil.getInputText("stNomorInvoice7",new FieldValidator("stNomorInvoice7","Nomor Invoice 7","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stNomorInvoice8",new FieldValidator("stNomorInvoice8","Nomor Invoice 8","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=detail>&nbsp;</td>
                            <td class=detail><%=jspUtil.getInputText("stNomorInvoice9",new FieldValidator("stNomorInvoice9","Nomor Invoice 9","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%>&nbsp;&nbsp;<%=jspUtil.getInputText("stNomorInvoice10",new FieldValidator("stNomorInvoice10","Nomor Invoice 10","string",100,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Tanggal Invoice</td>
                            <td class=detail><%=jspUtil.getInputText("dtTglInvoice",new FieldValidator("dtTglInvoice","Tanggal Invoice","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>Nilai Invoice</td>
                            <td class=detail><%=jspUtil.getInputSelect("stCurrency",new FieldValidator("stCurrency","Mata uang","string",5,JSPUtil.MANDATORY),dlCurrency.getComboContent(),100,JSPUtil.MANUAL)%><%=jspUtil.getInputText("dbNilaiInvoice",new FieldValidator("dbNilaiInvoice","Nilai Invoice","money10.2",-1,JSPUtil.MANDATORY),null,100,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row1>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row0>Pengirim (Vendor Lokal)</td>
                            <td class=detail><%=jspUtil.getInputText("stPengirimLokal",new FieldValidator("stPengirimLokal","Nama Vendor Lokal","string",200,JSPUtil.MANDATORY),null,200)%></td>
                        </tr>
                        <tr>
                            <td class=row1>Alamat</td>
                            <td class=detail><%=jspUtil.getInputTextArea("stAlamatLokal",new FieldValidator("stAlamatLokal","Alamat Vendor Lokal","string",200,JSPUtil.MANDATORY),null,3,50,200,JSPUtil.MANUAL)%></td>
                        </tr>
                        <tr>
                            <td class=row0>&nbsp;</td>
                            <td class=detail>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class=row1>Tanggal Cetak Surat</td>
                            <td class=detail><%=jspUtil.getInputText("dtTglCetak",new FieldValidator("dtTglCetak","Tanggal Cetak","date",-1,JSPUtil.MANDATORY),DateUtil.getNewDate(),200,JSPUtil.MANUAL)%></td>
                        </tr>

                  </table>
                </td>
             </tr>
             <tr>
                <td> <%=jspUtil.getButtonSubmit("bGenerate","Tampilkan Surat Kuasa")%>
<%--                <%=jspUtil.getButtonNormal("bGenerate","Tampilkan try","window.open('rpt_try.rpt?user_id=admin','','toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480');")%>--%>
                </td>
             </tr>
         </table>
      </form>
   </body>
   <script>
    function processReport(){
        var sk1,sk2,sk3,sk4,sk5;
        sk1 = f.sk1_1.value+"%2f"+f.sk1_2.value+"%2f"+f.sk1_3.value+"%2f"+f.sk1_4.value+"%2f"+f.sk1_5.value;
        sk2 = f.sk2_1.value+"%2f"+f.sk2_2.value+"%2f"+f.sk2_3.value+"%2f"+f.sk2_4.value+"%2f"+f.sk2_5.value;
        sk3 = f.sk3_1.value+"%2f"+f.sk3_2.value+"%2f"+f.sk3_3.value+"%2f"+f.sk3_4.value+"%2f"+f.sk3_5.value;
        sk4 = f.sk4_1.value+"%2f"+f.sk4_2.value+"%2f"+f.sk4_3.value+"%2f"+f.sk4_4.value+"%2f"+f.sk4_5.value;
        sk5 = f.sk5_1.value+"%2f"+f.sk5_2.value+"%2f"+f.sk5_3.value+"%2f"+f.sk5_4.value+"%2f"+f.sk5_5.value;

        var csk1,csk2,csk3,csk4,csk5;
         if(f.chkSk1.checked)
            csk1="1";
         else
            csk1="0";
         if(f.chkSk2.checked)
            csk2="1";
         else
            csk2="0";
         if(f.chkSk3.checked)
            csk3="1";
         else
            csk3="0";
          if(f.chkSk4.checked)
            csk4="1";
         else
            csk4="0";
          if(f.chkSk5.checked)
            csk5="1";
         else
            csk5="0";
         if((csk1==0)&&(csk2==0)&&(csk3==0)&&(csk4==0)&&(csk5==0)){
            alert('You must check minimum  1 of 5 avalaible forms!! ');
            return false;
         }

         var stNoInvoice=f.stNomorInvoice1.value+"     "+f.stNomorInvoice2.value+"      "+f.stNomorInvoice3.value+"      "+f.stNomorInvoice4.value+"      "+f.stNomorInvoice5.value+"      "+f.stNomorInvoice6.value+"      "+f.stNomorInvoice7.value+"      "+f.stNomorInvoice8.value+"      "+f.stNomorInvoice9.value+"      "+f.stNomorInvoice10.value;

         var stUrl = "rpt_sd.rpt?sk1_check="+csk1+"&sk1_number="+sk1+"&sk2_check="+csk2+"&sk2_number="+sk2+"&sk3_check="+csk3+"&sk3_number="+sk3+"&sk4_check="+csk4+"&sk4_number="+sk4+"&sk5_check="+csk5+"&sk5_number="+sk5;
         stUrl=stUrl+"&nama_pemberi_kuasa="+f.stNamaPemberiKuasa.value;
         stUrl=stUrl+"&jabatan_pemberi_kuasa="+f.stJabatanPemberiKuasa.value;
         stUrl=stUrl+"&nama_diberi_kuasa="+f.stNamaDiberiKuasa.value;
         stUrl=stUrl+"&npwp_diberi_kuasa="+f.stNPWP.value;
         stUrl=stUrl+"&alamat_diberi_kuasa="+escape(f.stAlamatDiberiKuasa.value);
         stUrl=stUrl+"&pengirim="+f.stPengirimImport.value;
         stUrl=stUrl+"&nama_barang="+f.stNamaBarang.value;
         stUrl=stUrl+"&jumlah_koli="+f.dbJmlKoli.value;
         stUrl=stUrl+"&jml_kgs="+f.dbJmlKgs.value;
         stUrl=stUrl+"&negara_asal="+f.stNegaraAsal.value;
         stUrl=stUrl+"&nama_pesawat="+f.stNamaPesawat.value;
         stUrl=stUrl+"&tgl_psw="+f.stTglPesawat.value;
         stUrl=stUrl+"&nama_kapal="+f.stNamaKapal.value;
         stUrl=stUrl+"&tgl_kapal="+f.stTglKapal.value;
         stUrl=stUrl+"&mawb="+f.stMawb.value;
         stUrl=stUrl+"&hawb="+f.stHawb.value;
         stUrl=stUrl+"&bl="+f.stBL.value;
         stUrl=stUrl+"&no_invoice="+stNoInvoice;
         stUrl=stUrl+"&tanggal_invoice="+f.dtTglInvoice.value;
         stUrl=stUrl+"&nilai_invoice="+f.dbNilaiInvoice.value;
         stUrl=stUrl+"&nama_vendor="+f.stPengirimLokal.value;
         stUrl=stUrl+"&alamat_vendor="+escape(f.stAlamatLokal.value);
         stUrl=stUrl+"&tanggal_cetak="+f.dtTglCetak.value;
         stUrl=stUrl+"&currency="+f.stCurrency.value;
         stUrl=stUrl+"&orang_diberi_kuasa="+f.stNmOrangDiberiKuasa.value;


         window.open(stUrl,"","toolbar=yes,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480");


    }


   </script>
</html>
