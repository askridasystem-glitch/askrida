<%@ page import="com.crux.web.form.Form,
com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.web.controller.SessionManager,
com.crux.lang.LanguageManager"%>
<%@ taglib prefix="c" uri="crux" %>
<%
final Form formx = (Form)request.getAttribute("FORM");

boolean hasNavigateBranch = SessionManager.getInstance().hasResource("ARINVOICE_NAVBR");

boolean canApproved1 = SessionManager.getInstance().getSession().hasResource("AR_TRX_17_APPROVE");

boolean canCreateDLA = SessionManager.getInstance().getSession().hasResource("AR_TRX_17_CREATE_DLA")||
        SessionManager.getInstance().getSession().hasResource("AR_TRX_18_CREATE_DLA")||
        SessionManager.getInstance().getSession().hasResource("AR_TRX_19_CREATE_DLA");

boolean canCreateFac = SessionManager.getInstance().getSession().hasResource("AR_TRX_17_CREATE_DLA");
boolean canCreateInward = SessionManager.getInstance().getSession().hasResource("AR_TRX_23_CREATE");
boolean canCreateExGratia = SessionManager.getInstance().getSession().hasResource("AR_TRX_24_CREATE");

String trxID = (String) formx.getProperty("trxId");
boolean isClaim = trxID.equalsIgnoreCase("17")||trxID.equalsIgnoreCase("18")||trxID.equalsIgnoreCase("19")||trxID.equalsIgnoreCase("23")||trxID.equalsIgnoreCase("24");

boolean isExGratia = trxID.equalsIgnoreCase("24");
%>
<c:frame title="<%="TRANSACTIONS"+formx.getProperty("trxId")%>" >
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="customer" caption="Customer" type="string" presentation="standard" width="200" />
                                <c:field name="invoiceno" caption="Nomor Transaksi" type="string" presentation="standard" width="200" />
                                <c:field name="trxno" caption="Nomor Bukti" type="string" presentation="standard" width="200" />
                                <%--<c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" changeaction="btnSearch" readonly="<%=!hasNavigateBranch%>" />  --%>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="transdatefrom" type="date" caption="Tanggal Mutasi" presentation="standard" width="200" />
                                <c:field name="transdateto" type="date" caption="Tanggal Mutasi" presentation="standard" width="200" />
                                <c:field lov="LOV_Entity" width="200" name="stEntityID" caption="Customer" type="string" presentation="standard" popuplov="true" />
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Search" event="btnSearch" />
            </td>
        </tr>
        <tr>
            <td>
                <c:field name="invoiceid" hidden="true"/>
                <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsurancePolicyInwardView">
                    <%
                       final InsurancePolicyInwardView pol = (InsurancePolicyInwardView) current;
                    %>
                    <c:listcol name="stARInvoiceID" title="ID" selectid="invoiceid"/>
                    <c:listcol name="stARInvoiceID" title="ID"/>
                    <%--<c:listcol name="stActiveFlag" title="Act" flag="true"/>--%>
                    <c:listcol name="stApprovedFlag" title="Eff" flag="true"/>
                    <c:listcol name="stClaimStatus" title="Status" filterable="true" />
                     <c:evaluate when="<%=!isExGratia%>">
                        <c:listcol name="stEntityName" title="Reasuransi" filterable="true" />
                     </c:evaluate>
                    <c:listcol name="stInvoiceType" title="Type" />
                    <c:listcol name="dtMutationDate" title="Tanggal Mutasi" />
                    <c:listcol name="stInvoiceNo" title="Nomor Bukti" filterable="true" />
                    <c:listcol name="stAttrPolicyNo" title="Nomor Polis" filterable="true" />
                    <c:listcol name="stPLANo" title="Nomor PLA" filterable="true" />
                    <c:listcol name="stDLANo" title="Nomor DLA" filterable="true" />
                    <c:listcol name="stAttrPolicyTypeID" title="Jenis" filterable="true" />
                    <c:evaluate when="<%=!isExGratia%>">
                        <c:listcol name="stRefID0" title="Treaty" filterable="true" />
                    </c:evaluate>
                    <c:listcol name="stCurrencyCode" title="Ccy" />
                    <c:listcol name="dbEnteredAmount" title="Amount" />
                    <c:listcol name="stCreateWho" title="User Entry" filterable="true" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>

                <c:evaluate when="<%=canCreateInward%>">
                    <c:button text="Buat LKS" event="clickCreateLKS" />
                </c:evaluate>

                <%--
                <c:evaluate when="<%=canCreateFac && trxID.equalsIgnoreCase("17")%>">
                    <c:button text="Buat LKS" event="clickCreatePLAFromReas" />
                </c:evaluate>
                <c:evaluate when="<%=canCreateInward && trxID.equalsIgnoreCase("23")%>">
                    <c:button text="Buat LKS" event="clickCreate" />
                </c:evaluate>
                <c:evaluate when="<%=canCreateExGratia && trxID.equalsIgnoreCase("24")%>">
                    <c:button text="Buat LKS" event="clickCreate" />
                </c:evaluate>--%>
                    <c:evaluate when="<%=canCreateDLA%>">
                        <c:button text="Buat LKP" event="clickCreateDLA" />
                    </c:evaluate>
                    <c:button text="Endorse" event="clickCreateEndorse" />
                <c:button text="Ubah" event="clickEdit" />
                <c:button text="Lihat" event="clickView" />
                <c:evaluate when="<%=canApproved1%>">
                    <c:button text="Setujui" event="clickApprove" />
                </c:evaluate>
                    <c:button text="Reverse" event="clickReverse" />
                    <c:button text="Ubah No Konfirmasi" event="clickEditNoSurat" />
                <%--<c:button text="{L-ENGReApprove-L}{L-INAApprove Ulang-L}" event="clickReApproval" />--%>
            </td>
        </tr>
        <c:evaluate when="<%=isClaim || isExGratia%>">
            <tr>
                <td>
                    Print <c:field name="printLang" width="250" type="string" lov="LOV_CLMINWARD_PRINTING" ><c:param name="vs" value="<%=trxID%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" /><c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
                </td>
            </tr>
        </c:evaluate>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>
<script>
   var frmx = docEl('frmx');

   function getSelectedAttr(c,ref) {
      return c.options[c.selectedIndex].getAttribute(ref);
   }

   function dynPrintClick() {

      if (f.invoiceid.value=='') {
         alert('Please select a transaction');
         return;
      }

      if (true) {
         frmx.src='x.fpc?EVENT=AR_INVOICE_PRT&invoiceid='+f.invoiceid.value+'&alter='+f.printLang.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
         return;
      }
   }
</script>