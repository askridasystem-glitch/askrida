<%@ page import="com.webfin.ar.forms.ReceiptPembayaranRealisasiTitipanListForm,
com.crux.lang.LanguageManager,
com.webfin.ar.model.ARReceiptView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    
    <%
    
    ReceiptPembayaranRealisasiTitipanListForm form  =(ReceiptPembayaranRealisasiTitipanListForm) request.getAttribute("FORM");
    
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="receiptNo" type="string" caption="No Bukti" presentation="standard" width="200" />
                                <c:field name="description" type="string" caption="Keterangan" presentation="standard" width="200" />
                                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" presentation="standard" width="200" />
                                <c:field name="printFlag" type="check" caption="{L-ENGPrint Flag-L}{L-INAPrint Flag-L}"  presentation="standard"/>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    
                                    <%--<c:field name="transdate" type="date" caption="Date" presentation="standard" width="200" />--%>
                                    <c:field name="rcpDateFrom" type="date" caption="Tanggal Bayar" presentation="standard" width="200" />
                                    <c:field name="rcpDateTo" type="date" caption="Tanggal Bayar" presentation="standard" width="200" />
                                    <c:field caption="{L-ENGShow Pending only-L}{L-INAData Belum Disetujui-L} " name="stNotApproved" type="check" presentation="standard" />
                                </tr>
                            </table>
                        </td>
                        <td><c:button text="Refresh" event="btnSearch" /></td>
                    </tr>
                </table>
            </td>
        </tr>
        
        <tr>
            <td>
                
                <c:field name="receiptID" hidden="true"/>
                <c:field name="stSettlementID" hidden="true"/>
                <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.ar.model.ARReceiptView">
                    <c:listcol name="stARReceiptID" title="ID" selectid="receiptID"/>
                    <c:listcol name="stPostedFlag" title="Eff" flag="true" />
                    <c:listcol name="stPrintFlag" title="Print" flag="true" />
                    <c:listcol filterable="true" name="stReceiptNo" title="No Bukti" />
                    
                    <c:listcol filterable="true" name="stPolicyNo" title="No Polis" />
                    <c:listcol name="dtCreateDate" title="Tanggal Entry" />
                    <c:listcol name="stCurrencyCode" title="Ccy" />
                    <c:listcol filterable="true" name="stCostCenterCode" title="Cabang" />
                    <c:listcol filterable="true" name="stEntityName" title="Bank" />
                    <c:listcol filterable="true" name="stShortDescription" title="Keterangan" />
                    <c:listcol name="dbEnteredAmount" align="right" title="Jumlah" />
                    <c:listcol filterable="true" name="stCreateWho" title="User" />
                    <c:listcol filterable="true" name="stUserName" title="User" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button  text="Buat" event="clickCreatePolisKhusus" />
                <c:button  text="Ubah" event="clickEditPolisKhusus" />
                <c:button  text="Lihat" event="clickViewPolisKhusus" />
                <%--<c:button  show="<%=form.isEnableSuperEdit()%>" text="Super Edit" event="clickSuperEdit" />--%>
                <c:button  show="<%=form.isEnableApprovePolisKhusus()%>" text="Setujui" event="clickApprovePolisKhusus" />
                <c:button  show="<%=form.isEnableApprovePolisKhusus()%>" text="Reverse" event="clickReversePolisKhusus" />
                <%--<c:button  text="Ubah No Polis" event="clickEditNoPolisKhusus" />--%>
            </td>
        </tr>
        <tr>
            <td>
                Print <c:field name="printLang" width="250" type="string" lov="LOV_RECEIPT_PRINTING" ><c:param name="vs" value="<%=form.getStSettlementID()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" /><c:button text="Print Excel" name="exprintx" clientEvent="dynPrintClickExcel();" />
            </td>
        </tr>
    </table>
    
    <%--
   if (form.goPrint.equalsIgnoreCase("Y")) {
         out.print("<script>");
         out.print("window.open('pages/ar/report/rpt_ARAP_default.fop')");
         out.print("</script>");
         form.goPrint=null;
      }
    --%>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
   var frmx = docEl('frmx');

   function getSelectedAttr(c,ref) {
      return c.options[c.selectedIndex].getAttribute(ref);
   }

   function dynPrintClick() {

      if (f.receiptID.value=='') {
         alert('Please select a transaction');
         return;
      }

      if (true) {
         frmx.src='x.fpc?EVENT=AR_RECEIPT_PRT&receiptid='+f.receiptID.value+'&alter='+f.printLang.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
         return;
      }
   }
   
   function dynPrintClickExcel() {

      if (f.receiptID.value=='') {
         alert('Please select a transaction');
         return;
      }

      if (true) {
         frmx.src='x.fpc?EVENT=AR_RECEIPT_EXCEL&receiptid='+f.receiptID.value+'&settlement='+f.stSettlementID.value;
         return;
      }
   }

</script>