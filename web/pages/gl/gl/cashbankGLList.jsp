<%@ page import="com.webfin.gl.model.JournalView,
                 com.crux.util.Tools,
                 com.webfin.gl.form.GLListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="JURNAL KAS BANK">
<%

   GLListForm form  =(GLListForm) request.getAttribute("FORM");


%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="accountCode" type="string" caption="Kode Akun" presentation="standard" width="200" />
                        <c:field name="description" type="string" caption="keterangan" presentation="standard" width="200" />
                        <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" presentation="standard" width="200" />
                     </table>
                  </td>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <tr>
                           <c:field name="transNumber" type="string" caption="No Bukti" presentation="standard" width="200" />
                           <%--<c:field name="transdate" type="date" caption="Date" presentation="standard" width="200" />--%>
                           <c:field name="transdatefrom" type="date" caption="Tanggal Mulai" presentation="standard" width="200" />
                           <c:field name="transdateto" type="date" caption="Tanggal Akhir" presentation="standard" width="200" />
                           <c:field name="showReverse" type="check" caption="Show Reverse" presentation="standard" width="200" changeaction="btnSearch" />
                        </tr>
                     </table>
                  </td>
                  <td><c:button text="Refresh" event="btnSearchCashBank" /></td>
               </tr>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:button text="Print" event="btnPrint" /> using template <c:field name="printForm" type="string" lov="LOV_PRT_CASHBANK" />: in <c:field name="printLang" lov="LOV_LANG" type="string" />

         </td>
      </tr>
      <tr>
         <td>
            <%
               JournalView lastjv = new JournalView();
            %>
            <c:field name="trxhdrid" type="string"  hidden="true" />
            <c:listbox name="cashBanklist" paging="true" >
               <%
                  final JournalView jv = (JournalView) current;

                  final boolean isdetail = jv!=null && lastjv!=null && Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());

                  lastjv=jv;
               %>
               <c:evaluate when="<%=jv==null%>" >
                  <c:listcol title="" ></c:listcol>
                  <%--<c:listcol title="Eff" ></c:listcol>--%>
                  <c:listcol title="NO BUKTI" ></c:listcol>
                  <c:listcol title="TANGGAL" ></c:listcol>
                  <c:listcol title="KODE AKUN" ></c:listcol>
                  <c:listcol title="DESKRIPSI" ></c:listcol>
                  <c:listcol title="DEBIT" align="right"></c:listcol>
                  <c:listcol title="KREDIT" align="right"></c:listcol>
                  <c:listcol title="USER"></c:listcol>
                  <c:listcol title="USER"></c:listcol>
               </c:evaluate>
               <c:evaluate when="<%=jv!=null%>" >
                  <c:listcol title="" ><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% } %></c:listcol>
                  <%--<c:listcol name="stApproved" title="Eff" flag="true" />--%>
                  <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail?jv.getStTransactionNo():null)%></c:listcol>
                  <c:listcol title="DATE" ><%=jspUtil.print(!isdetail?jv.getDtApplyDate():null)%></c:listcol>
                  <c:listcol title="ACCOUNT #" ><div style="width:120"><%=jspUtil.print(jv.getStAccountNo())%></div></c:listcol>
                  <c:listcol title="DESC" ><%=jspUtil.print(jv.getStDescription())%></c:listcol>
                  <c:listcol title="DEBIT" align="right" ><%=jspUtil.print(jv.getDbDebit(),2)%></c:listcol>
                  <c:listcol title="CREDIT" align="right" ><%=jspUtil.print(jv.getDbCredit(),2)%></c:listcol>
                  <c:listcol title="USER" ><%=jspUtil.print(jv.getStCreateWho())%></c:listcol>
                  <c:listcol title="USER" ><%=jspUtil.print(jv.getStUserName())%></c:listcol>
               </c:evaluate>
            </c:listbox>
         </td>
      </tr>
      <tr>
         <td>
            <%=jspUtil.getButtonNormal("crt","Buat","openDialog('so.ctl?EVENT=CB_GL_ENTRY_ADD', 1500,650,refreshcb);")%>
            <%=jspUtil.getButtonNormal("edt","Ubah","openDialog('so.ctl?EVENT=CB_GL_ENTRY_EDIT&trxhdrid='+f.trxhdrid.value, 1500,650,refreshcb);")%>
            <%=jspUtil.getButtonNormal("edt","Hapus","openDialog('so.ctl?EVENT=CB_GL_ENTRY_DELETE&trxhdrid='+f.trxhdrid.value, 1500,650,refreshcb);")%>
            <%=jspUtil.getButtonNormal("v","Lihat","openDialog('so.ctl?EVENT=GL_ENTRY_VIEW&trxhdrid='+f.trxhdrid.value, 1500,650,refreshcb);")%>
         </td>
      </tr>
   </table>
<script>
   function refreshcb(o) {
      if (o!=null) f.submit(); 
   }
</script>
<%
   /*if (form.goPrint!=null) {
         out.print("<script>");
         out.print("window.open('pages/gl/report/rpt_"+form.goPrint+".fop?formid="+form.getFormID()+"')");
         out.print("</script>");
         form.goPrint=null;
      }*/

%>
</c:frame>