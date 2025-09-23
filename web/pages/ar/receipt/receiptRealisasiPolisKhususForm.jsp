<%@ page import="com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.forms.ReceiptPembayaranRealisasiTitipanForm,
         com.webfin.ar.model.ARReceiptLinesView,
         com.crux.util.DTOList,
         com.crux.util.Tools,
         com.webfin.ar.model.ARAPSettlementView,
         com.crux.util.JSPUtil,
         com.crux.util.BDUtil,
         java.math.BigDecimal,
         com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%
                boolean showNotes = true;

                final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm) request.getAttribute("FORM");

                int jumlah = form.getListInvoices().size();

                final ARReceiptView receipt = form.getReceipt();

                final boolean masterCurrency = receipt.isMasterCurrency();

                final boolean isAP = receipt.isAP();
                final boolean isAR = receipt.isAR();

                final boolean isUsingEntityID = receipt.isUsingEntityID();

                final boolean canNavigateBranch = form.isCanNavigateBranch();

                final boolean isNote = receipt.isNote();

                final boolean hasReceiptClass = receipt.getStReceiptClassID() != null;

                final boolean hasBankType = receipt.getStBankType() != null;

                showNotes = isNote && hasReceiptClass;
                boolean showInvoices = true && hasReceiptClass;
                boolean showXC = false;
                boolean suratHutang = false;
                boolean useCheckFlag = false;
                boolean useJournalType = false;

                final ARAPSettlementView settlement = receipt.getSettlement();

                if (settlement != null) {
                    showXC = settlement.checkProperty("EN_XC", "Y");
                    useCheckFlag = settlement.checkProperty("CHK_FLG", "Y");
                    useJournalType = settlement.checkProperty("JOURNAL_TYPE","Y");
                }

                showInvoices &= settlement.checkProperty("EN_SL_IV", "Y");
                showNotes &= settlement.isNote();
                suratHutang = settlement.checkProperty("SH", "Y");

                final String ccy = receipt.getStCurrencyCode();

                /*
                stEntityID
                stARSettlementID
                 */

                int phase = 0;

                if (receipt.getStCostCenterCode() != null) {
                    phase = 1;
                }
                if (receipt.getStReceiptClassID() != null) {
                    phase = 2;
                }
    //if (receipt.getStPaymentMethodID()!=null) phase=3;
                if (receipt.getStBankType() != null) {
                    phase = 3;
                }
    //if(receipt.getStBankType()==null&&receipt.getStReceiptClassID()!=null) phase=3;

                if (phase == 2 && receipt.getStReceiptClassID() != null) {
                    phase = 3;
                }

                if (phase == 2 && showNotes && receipt.getStCostCenterCode() != null) {
                    phase = 3;
                }

                final boolean developmentMode = Config.isDevelopmentMode();

                boolean readOnly = form.isReadOnly();
                boolean canAdd = readOnly;

                boolean titipan = false;
                if (receipt.getStReceiptClassID() != null) {
                    if (receipt.getStReceiptClassID().equals("8") || receipt.getStReceiptClassID().equals("9") || receipt.getStReceiptClassID().equals("10")) {
                        titipan = true;
                    }
                }

                boolean hasClaim = false;

                if (receipt.getStARSettlementID().equalsIgnoreCase("10")) {
                    hasClaim = true;
                }

                boolean isPembayaranKomisi = false;

                if (receipt.getStARSettlementID().equalsIgnoreCase("33")) {
                    isPembayaranKomisi = true;
                }

    %>


    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="parinvoiceid" type="string" hidden="true"/>
                <c:field name="nosurathutang" type="string" hidden="true"/>
                <c:field name="artitipanid" type="string" hidden="true"/>

                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="receipt.stReceiptNo" width="150" mandatory="true" caption="No Bukti" type="string|64" presentation="standard" readonly="true" show="<%=receipt.getStReceiptNo() != null%>" />
                                <c:field lov="LOV_CostCenter" width="200" name="receipt.stCostCenterCode" mandatory="true" caption="Cabang" type="string" presentation="standard" readonly="<%=receipt.getStCostCenterCode() != null%>" changeaction="changeCostCenter" />
                                <c:field readonly="<%=hasReceiptClass%>" lov="LOV_ReceiptClass" width="230" changeaction="changeReceiptClass" name="receipt.stReceiptClassID" mandatory="true" caption="Metode" type="string" presentation="standard">
                                    <c:param name="invoice_type" value="<%=receipt.getStInvoiceType()%>" />
                                </c:field>

                                <c:evaluate when="<%=titipan%>">
                                    <c:evaluate when="<%=phase >= 2%>">
                                        <c:field readonly="<%=hasBankType%>" lov="LOV_BankType" width="150" changeaction="changeBankType" name="receipt.stBankType" mandatory="true" caption="Bank Type" type="string" presentation="standard"/>
                                    </c:evaluate>
                                </c:evaluate>

                                <c:evaluate when="<%=phase >= 3%>" >
                                    <c:evaluate when="<%=!isNote%>">
                                        <c:evaluate when="<%=!isUsingEntityID%>">
                                            <c:field lov="LOV_PaymentMethod" width="400" name="receipt.stPaymentMethodID" mandatory="true" caption="Account" type="string"  presentation="standard" readonly="<%=receipt.getStPaymentMethodID() != null%>" changeaction="changemethod" >
                                                <c:lovLink name="rc" link="receipt.stReceiptClassID" clientLink="false"/>
                                                <c:param name="ccbanktype" value="<%=receipt.getStCostCenterCode() + "|" + receipt.getStBankType()%>" />
                                            </c:field>
                                        </c:evaluate>
                                        
                                    </c:evaluate>
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 3%>">
                                    <c:field name="receipt.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" readonly="<%=receipt.getStMonths() != null%>" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth"/>
                                    <c:field name="receipt.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" readonly="<%=receipt.getStYears() != null%>" lov="LOV_GL_Years3" mandatory="true" presentation="standard" changeaction="setDate"/>
                                   <c:field show="<%=useJournalType%>" name="receipt.stJournalType" type="string" caption="{L-ENGPayment Type-L}{L-INACara Pembayaran-L}" readonly="<%=receipt.getStJournalType()!=null%>" lov="LOV_JournalType" mandatory="true" presentation="standard" changeaction="setDate"/>
                                    <c:field name="receipt.dtReceiptDate" mandatory="true" caption="{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}" type="date" presentation="standard" />

                                    <c:evaluate when="<%=isUsingEntityID%>">
                                        <c:field mandatory="true" caption="{L-ENGPayment Entity-L}{L-INABank-L}" name="receipt.stAccountEntityID" popuplov="true" lov="LOV_EntityFinance" type="string" width="250" presentation="standard" >
                                                <c:param name="month" value="<%=receipt.getStMonths()+"|"+receipt.getStYears()%>"  />
                                            <c:lovLink name="cc_code" link="receipt.stCostCenterCode" clientLink="false"/>
                                       
                                            </c:field>
                                        </c:evaluate>
                                                                        <c:field lov="LOV_Currency" changeaction="chgCurrency" name="receipt.stCurrencyCode" caption="Mata Uang" type="string" presentation="standard"/>
                                    <c:field readonly="<%=masterCurrency%>" mandatory="true" name="receipt.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                                    <c:field readonly="<%=isNote%>" name="receipt.dbEnteredAmount" mandatory="true" caption="Jumlah Dibayar" type="money16.2" presentation="standard" width="200"/>
                                    <c:field include="<%=!masterCurrency%>"  name="receipt.dbAmount" readonly="true" caption="Jumlah(IDR)" type="money16.2" presentation="standard" width="200"/>
                                </c:evaluate>

                            </table>
                        </td>
                        <td>
                            <c:evaluate when="<%=phase >= 3%>">
                                <table cellpadding=2 cellspacing=1>
                                    <c:field name="receipt.stInvoiceType" type="string" hidden="true" />
                                    <c:field name="receipt.stNoteType" type="string" hidden="true" />
                                    <c:field name="receipt.stInvoiceTypeDesc" readonly="true" caption="Jenis Transaksi" type="string" presentation="standard"/>
                                    <c:evaluate when="<%=!isNote%>">
                                        <c:field name="receipt.dbAmountApplied" readonly="true" caption="Jumlah Tagihan" type="money16.2"  width="200" presentation="standard"/>
                                        <c:field name="receipt.dbRateDiffAmount" readonly="true" caption="Selisih" type="money16.2" width="200" presentation="standard"/>
                                    </c:evaluate>
                                    <c:field name="receipt.dbAmountRemain" readonly="true" caption="Sisa" type="money16.2" width="200" presentation="standard"/>
                                    <c:field name="receipt.stPostedFlag" caption="Posted" type="check" presentation="standard" readonly="true"/>

                                    <c:field name="receipt.stIDRFlag" caption="{L-ENGIDR Flag-L}{L-INAIDR Flag-L}" type="check" presentation="standard"/>
                                    
<c:field name="receipt.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                     <tr>
                                         <td colspan="2"></td>
                                        <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                           
                                        </td>
                                        <td>
                                        </td>
                                     </tr>
                                </table>
                                <table>
                                    <c:evaluate when="<%=titipan%>">
                                        <c:field readonly="false" name="receipt.stARTitipanID" type="string" width="150" popuplov="true" lov="LOV_TitipanPremi" mandatory="true" caption="Titipan Premi" presentation="standard" />
                                        <%--<tr>
                                            <td>
                                                Titipan Premi :
                                                <c:field readonly="false" name="receipt.stARTitipanID" type="string" width="150" popuplov="true" lov="LOV_ENTITY" mandatory="true" caption="Titipan Premi" presentation="standard" />
                                            </td>
                                            <td>
                                                <c:button text="..." clientEvent="searchTitipan()" validate="false" enabled="<%=!readOnly%>"/>
                                            </td>
                                        </tr>--%>
                                    </c:evaluate>
                                </table>
                            </c:evaluate>
                        </td>

                    <tr>

                    </tr>

        </tr>
        <tr>
            <td colspan=2>
                <c:evaluate when="<%=phase >= 3%>">
                    <table cellpadding=2 cellspacing=1>
                        <c:field name="receipt.stShortDescription" mandatory="false" caption="Keterangan" type="string|128" width="400" presentation="standard" />
                        <c:field name="receipt.stDescription" mandatory="false" caption="Catatan" type="string|255" rows="2" width="400" presentation="standard" />
                        <c:field name="receipt.stARSettlementID" hidden="true" caption="stARSettlementID" type="string" presentation="standard"/>
                        <c:field name="receipt.stFileSlip" caption="Dokumen" type="file" thumbnail="true"
                                             readonly="false" presentation="standard"/>
                        <c:field name="receipt.stPolicyID" caption="Nomor Polis" type="string" width="180" lov="LOV_POLIS_KHUSUS" popuplov="true"
                                             readonly="true" presentation="standard"/>
                    </table>
                </c:evaluate>
            </td>
        </tr>

    </table>

</td>
</tr>
<c:evaluate when="<%=phase >= 3%>">
    <c:evaluate when="<%=showNotes%>">
        <tr>
            <td>
                <%=isAP ? "Receivable" : "Payable"%>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="listNotes" >
                                <c:listcol title="" columnClass="header" >
                                    <c:button text="+" clientEvent="addnewnote()" validate="false" enabled="<%=!readOnly%>" />
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="onDeleteNoteItem" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol name="stInvoiceNo" title="INVOICE NO" />
                                <c:listcol name="dbActInvAmount" title="TOTAL(ORG)" />
                                <c:listcol name="dbEnteredInvoiceAmount" title="AMOUNT(ORG)" />
                                <c:listcol name="dbInvoiceAmount" title="AMOUNT(IDR)" />
                                <c:listcol title="APPLY(ORG)" >
                                    <c:field name="listNotes.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                                </c:listcol>
                                <c:listcol name="dbAmount" title="APPLY(IDR)" />
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </c:evaluate>
    <c:evaluate when="<%=showInvoices%>">
        <tr>
            <td>
                <%=isAR ? "Receivable" : "Payable"%>&nbsp;Jumlah Polis :&nbsp;<%=jumlah%>
                <table cellpadding=2 cellspacing=1>
                    <%--<tr>
                          <td>
                              <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!readOnly%>"/>

                          <c:evaluate when="<%=suratHutang%>">
                              <c:button text="Surat Hutang" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                          </c:evaluate>
                          <c:button text="Titipan Tanpa No Polis" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>
                          <c:button text="Titipan Dengan No Polis" clientEvent="addnewtitipan()" validate="false" enabled="<%=!readOnly%>"/>
                          <c:button text="Lainnya" event="onNewLawanTitipan" validate="false" enabled="<%=!readOnly%>"/>
                          &nbsp;Jumlah Polis :&nbsp;<%=jumlah%>
                      </td>
                  </tr>
                    --%>

                    <tr>
                        <td>
                            <c:field name="invoicesindex" hidden="true" type="string"/>
                            <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                            <c:listbox name="listInvoices" paging="true" selectable="true" >
                                <%
                                            final ARReceiptLinesView currentLine = (ARReceiptLinesView) form.getListInvoices().get(index.intValue());

                                            final boolean expanded = currentLine != null && Tools.isYes(currentLine.getStExpandedFlag());

                                            boolean titipanbebas = currentLine != null && currentLine.getStInvoiceID() == null;
                                %>
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>" />

                                </c:listcol>
                                <c:listcol name="invoice.stARInvoiceID" title="ID" />
                                <c:listcol title="Tanggal Bayar" >
                                    <c:field name="<%="listInvoices.[$index$].dtReceiptDate"%>" caption="Tanggal Bayar" type="date" />
                                </c:listcol>
                                <c:listcol title="No Polis" >
                                    <c:field name="<%="listInvoices.[$index$].stPolicyID"%>" readonly="true" caption="Tanggal Bayar" type="string" width="150" lov="LOV_POLIS_KHUSUS" popuplov="true"/>
                                </c:listcol>

                                <c:evaluate when="<%=hasClaim%>">
                                    <c:listcol name="invoice.stRefID2" title="DLA NO" />
                                </c:evaluate>
                                <c:listcol align="right" title="TOTAL(ORG)" ></c:listcol>
                                <c:listcol align="right" title="Outstanding (ORG)" ></c:listcol>
                                <c:listcol align="right" title="SETTLED(ORG)" ></c:listcol>
                                <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>
                                <c:listcol align="right" title="Payment(ORG)" >


                                </c:listcol>
                                <c:listcol align="right" title="<%="Payment(" + ccy + ")"%>" />
                                <%--  <c:listcol  title="O/S" />--%>
                                <c:evaluate when="<%=useCheckFlag%>" >
                                    <c:listcol title="Bayar" />
                                </c:evaluate>

                                <c:evaluate when="<%=currentLine != null && currentLine.getDetails().size() > 0%>" >

                                    <%
                                                final DTOList commissions = currentLine.getDetails();

                                                for (int i = 0; i < commissions.size(); i++) {
                                                    ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                                                    boolean isLock = rcl.isLock();

                                                    if (settlement.getStARSettlementID().equalsIgnoreCase("10") || settlement.getStARSettlementID().equalsIgnoreCase("13")) {
                                                        isLock = true;
                                                    }

                                                    //isLock = false;
                                    %>
                            </tr>

                            <tr>
                                <%--  <td class=row1><c:button text="-" event="onDeleteInvoiceComissionItem" clientEvent="<%="f.invoicesindex.value='$index$';f.invoicecomissionindex.value='"+i+"';"%>" validate="false" enabled="<%=!readOnly%>"/></td>
                                --%><td class=row1></td>
                                <td class=row1></td>
                                <td class=row1></td>
                                <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                                <%--<c:evaluate when="<%=rcl.getStInvoiceID()==null%>">
                                    <td class=row1>
                                    <c:field name="<%="listInvoices.[$index$].details.["+i+"].stAccountID"%>" width="150" caption="Rekening" type="string" popuplov="true" lov="LOV_Account" >
                                        <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                    </c:field>
                                    </td>
                                </c:evaluate>--%>
                                <c:evaluate when="<%=hasClaim%>">
                                    <td class=row1></td>
                                </c:evaluate>
                                <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(), 2)%></td>
                                <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(), 2)%></td>
                                <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(), 2)%></td>
                                <td class=row1><c:field readonly="false" name="<%="listInvoices.[$index$].details.[" + i + "].dbEnteredAmount"%>" caption="Amount" type="money16.2" /></td>
                                <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmount(), 2)%></td>
                                <c:evaluate when="<%=useCheckFlag%>" >
                                    <td class=row1><c:field name="<%="listInvoices.[$index$].details.[" + i + "].stCheck"%>" readonly="<%=isLock%>" caption="Bayar" type="check" /></td>
                                </c:evaluate>
                                <%
                                            }
                                %>
                            </tr>
                            <tr>
                                <c:evaluate when="<%=!hasClaim%>">
                                    <td class=row1 colspan="4">
                                        <c:button text="Titipan yang Akan direalisasi" event="addGL" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                    </td>
                                </c:evaluate>
                                <c:evaluate when="<%=hasClaim%>">
                                    <td class=row1 colspan="5">

                                    </td>
                                </c:evaluate>


                                <td class=row1 colspan="3" align="right">
                                    <strong>Subtotal</strong>
                                </td>
                                <td class=row1 align="right">
                                    <strong> <%=jspUtil.print(currentLine.getDbTotalAmountPerLine(), 2)%></strong>
                                </td>
                                <td class=row1 colspan="2">
                                    <c:button show="<%=!readOnly%>" text="Hitung" event="doRecalculate"/>
                                    
                                </td>
                            </tr>
                            <%--
                            <tr>
                                <td class=row1 colspan="2">
                                    Titipan Premi :
                                </td>
                                <td class=row1 colspan="2">
                                    <c:field  width="200" clientchangeaction="selectTitipanPremi($index$)" name="listInvoices.[$index$].stTitipanPremiID" readonly="false" popuplov="true" lov="LOV_TitipanPremi2" caption="Titipan" type="string" >
                                        <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                    </c:field>
                                </td>
                                <td class=row1>
                                    <c:field name="listInvoices.[$index$].dbTitipanPremiAmount" readonly="false" caption="Titipan" type="money16.2" />
                                </td>
                                <td class=row1 colspan="4"></td>
                            </tr>--%>
                            <c:evaluate when="<%=showXC%>">
                                <tr class="row1">
                                    <td colspan="10">
                                        <table>
                                            <tr class="header">
                                                <td colspan="2">
                                                    Selisih Bayar
                                                </td>
                                                <td colspan="2">
                                                    {L-ENGAccount-L}{L-INAKode Akun-L}
                                                </td>
                                                <td colspan="5">
                                                    {L-ENGDescription-L}{L-INAKeterangan-L}
                                                </td>
                                                <td colspan="3">
                                                    Jumlah
                                                </td>

                                            </tr>
                                            <tr class="row1">
                                                <td colspan="2">
                                                    <c:field name="listInvoices.[$index$].stARSettlementExcessID" width="200" type="string" caption="Item" lov="LOV_SettlementXC" changeaction="setAccountExcess" clientchangeaction="f.invoicesindex.value='$index$';" >
                                                       <c:param name="arsettlementid" value="<%=receipt.getStARSettlementID()%>" />
                                                    </c:field>
                                                </td>
                                                <td colspan="2">
                                                    <c:field  width="130" name="listInvoices.[$index$].stExcessAccountID" readonly="false" popuplov="true" lov="LOV_Account" caption="Excess" type="string" >
                                                        <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                                    </c:field>
                                                </td>
                                                <td colspan="5">
                                                    <c:field  width="250" name="listInvoices.[$index$].stExcessDescription" readonly="false" caption="Excess" type="string" />
                                                </td>
                                                <td colspan="3">
                                                    <c:field name="listInvoices.[$index$].dbExcessAmount" readonly="false" caption="Excess" type="money16.2" />
                                                </td>
                                                <td></td>
                                            </tr> 
                                        </table>
                                    </td>
                                </tr>

                            </c:evaluate>
                            <c:evaluate when="<%=currentLine != null && currentLine.getListTitipan().size() > 0%>">
                                <tr class="row1">

                                    <td colspan="3">
                                        <strong>NO BUKTI REALISASI</strong>
                                    </td>
                                    <td colspan="7">
                                        <%
                                                    ARReceiptLinesView rclTitipReal = (ARReceiptLinesView) currentLine.getListTitipan().get(0);

                                                    String noBuktiReal = rclTitipReal.getStReceiptNo();
                                        %>
                                        <strong>: &nbsp;<%=noBuktiReal%></strong>
                                    </td>

                                </tr>
                                <tr class="row1">
                                    <td colspan="10">
                                        <table>
                                            <tr class="header">
                                                <td >
                                                    No
                                                </td>
                                                <td >
                                                </td>
                                                <td >
                                                    No Titipan Premi
                                                </td>
                                                 <td >
                                                    Counter
                                                </td>
                                                <td colspan="3">
                                                    Keterangan
                                                </td>
                                                <td>
                                                    Sisa
                                                </td>
                                                <td>
                                                    Terpakai
                                                </td>
                                                <td />
                                            </tr>
                                            <%
                                                        BigDecimal subTotalTitipan = BDUtil.zero;
                                                        final DTOList titipanDet = currentLine.getListTitipan();

                                                        for (int i = 0; i < titipanDet.size(); i++) {
                                                            ARReceiptLinesView rclTitip = (ARReceiptLinesView) titipanDet.get(i);

                                                            subTotalTitipan = BDUtil.add(subTotalTitipan, rclTitip.getDbTitipanPremiUsedAmount());
                                            %>
                                            
                                            <tr class="row1">
                                                <td><%=i+1%></td>
                                                <td class=row1><c:button text="-" event="onDeleteInvoiceTitipanItem" clientEvent="<%="f.invoicesindex.value='$index$';f.invoicecomissionindex.value='" + i + "';"%>" validate="false" enabled="<%=!readOnly%>" /></td>
                                                <c:evaluate when="<%=!isPembayaranKomisi%>">
                                                <td class=row1 >
                                                    <c:field  width="160" clientchangeaction="<%="selectTitipanPremi($index$," + i + ")"%>" name="<%="listInvoices.[$index$].listTitipan.[" + i + "].stTitipanPremiID"%>" readonly="false" popuplov="true" lov="LOV_TitipanPremiPolisKhusus" caption="Titipan" type="string" >
                                                        <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                                    </c:field>
                                                </td>
                                                </c:evaluate>
                                                <c:evaluate when="<%=isPembayaranKomisi%>">
                                                <td class=row1 >
                                                    <c:field  width="160" clientchangeaction="<%="selectTitipanPremi($index$," + i + ")"%>" name="<%="listInvoices.[$index$].listTitipan.[" + i + "].stTitipanPremiID"%>" readonly="false" popuplov="true" lov="LOV_TitipanPremiMinus" caption="Titipan" type="string" >
                                                        <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                                    </c:field>
                                                </td>
                                                </c:evaluate>
                                                <td align="center" class=row1>
                                                    <b><%=rclTitip.getTitipanPremiPolisKhusus()!=null?rclTitip.getTitipanPremiPolisKhusus().getStCounter():""%></b>
                                                </td>
                                                <td class=row1 colspan="3">
                                                    <c:field name="<%="listInvoices.[$index$].listTitipan.[" + i + "].stDescription"%>" readonly="false" caption="Titipan"  width="300" type="string" />
                                                </td>
                                                <td align="right" class=row1 >
                                                    <c:field name="<%="listInvoices.[$index$].listTitipan.[" + i + "].dbTitipanPremiAmount"%>" readonly="false" caption="Titipan" width="100" type="money16.2" />
                                                </td>
                                                <td align="right" class=row1 >
                                                    <c:field name="<%="listInvoices.[$index$].listTitipan.[" + i + "].dbTitipanPremiUsedAmount"%>" readonly="false" mandatory="true" caption="Titipan" width="100" type="money16.2" />
                                                </td>
                                                <%if (BDUtil.biggerThan(rclTitip.getDbTitipanPremiUsedAmount(), currentLine.getDbTotalAmountPerLine())) {%>

                                                <td class="row1" colspan="2">
                                                </td>
                                               
                                             <%}%>
                                            </tr>

                                            <%

                                              }
                                            %>
                                            <tr>
                                    <td colspan="7" class=row1 align="right">
                                        <strong>Subtotal</strong>
                                    </td>
                                    <td class=row1 align="right">
                                        <strong> <%=jspUtil.print(currentLine.getDbTitipanPremiTotalAmount(), 2)%></strong>
                                    </td >
                                    <td class=row1 align="right">
                                        <strong> <%=jspUtil.print(subTotalTitipan, 2)%></strong>
                                    </td>
                                </tr>
                                        </table>
                                    </td>
                                </tr>

                                


                            </c:evaluate>

                            <%--
                           <tr>
                                <td class=row1 colspan="4">

                                    <table cellpadding=2 cellspacing=1>
                                        <tr>
                                            <td class=row1 >
                                                <c:field name="glindex" hidden="true" type="string"/>
                                                <c:listbox name="listInvoices.listTitipan" >
                                                    <c:listcol title="" columnClass="header" >

                                            </c:listcol>
                                            <c:listcol title="" columnClass="detail" >
                                                <c:button text="-" event="onDeleteGLItem" clientEvent="f.glindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                            </c:listcol>
                                            <c:listcol name="stInvoiceNo" title="Description" />
                                            <c:listcol title="Titipan Premi" >
                                                <c:field  width="200" clientchangeaction="selectTitipanPremi($index$)" name="listInvoices.listTitipan.[$index$].stTitipanPremiID" readonly="false" popuplov="true" lov="LOV_TitipanPremi2" caption="Titipan" type="string" >
                                                   <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                               </c:field>
                                            </c:listcol>
                                            <c:listcol title="APPLY(ORG)" >

                                                <c:field name="listInvoices.listTitipan.[$index$].dbTitipanPremiAmount" readonly="false" caption="Titipan" type="money16.2" />
                                            </c:listcol>

                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>--%>
                            <tr>
                            </c:evaluate>
                        </c:listbox>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!readOnly%>"/>
           
                            <c:evaluate when="<%=suratHutang%>">
                                <c:button text="Surat Hutang" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                            </c:evaluate>
                            <%--<c:button text="Titipan Tanpa No Polis" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>--%>
                            <c:evaluate when = "<%=!isPembayaranKomisi%>">
                                <%--<c:button text="Titipan Dengan No Polis" clientEvent="addnewtitipan()" validate="false" enabled="<%=!readOnly%>"/>--%>
                                <c:button text="Lainnya" event="onNewLawanTitipan" validate="false" enabled="<%=!readOnly%>"/>
                            </c:evaluate>
                            <c:evaluate when = "<%=isPembayaranKomisi%>">
                                <c:button text="Titipan Dengan No Polis" clientEvent="addnewtitipankomisi()" validate="false" enabled="<%=!readOnly%>"/>
                            </c:evaluate>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </c:evaluate>

    <%--   end of tes --%>

    <c:evaluate when="<%=false%>">
        <tr>
            <td>
                Miscellaneous :
                <c:field name="stGLSelect" width="150" type="string" caption="Item" lov="LOV_SettlementXC" >
                    <c:param name="arsettlementid" value="<%=receipt.getStARSettlementID()%>" />
                </c:field>
                <c:button text="+" event="addGL" clientEvent="f.parinvoiceid.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field name="glindex" hidden="true" type="string"/>
                            <c:listbox name="listGLs" >
                                <c:listcol title="" columnClass="header" >

                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="onDeleteGLItem" clientEvent="f.glindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol name="stInvoiceNo" title="Description" />
                                <c:listcol title="APPLY(ORG)" >
                                    <c:field name="listGLs.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                                </c:listcol>
                                <c:listcol name="dbAmount" title="APPLY(IDR)" />
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

    </c:evaluate>
</c:evaluate> 
<tr>
    <td align=center>
        <c:evaluate when="<%=!readOnly%>" >
            <c:evaluate when="<%=phase >= 3%>">
                <c:button text="Hitung Ulang" event="doRecalculate"/>
                <c:button text="Simpan" event="doSavePolisKhusus" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />
            <%--  <c:button text="Generate Payment No" event="generatRNo" show="<%=developmentMode%>" />
         	--%>
        </c:evaluate>
        <c:evaluate when="<%=form.isReverseMode()%>" >
            <c:button text="Reverse" event="reversePolisKhusus"/>
        </c:evaluate>
        <c:evaluate when="<%=readOnly%>" >
            <c:button text="Tutup" event="doClose"/>
        </c:evaluate>
        <c:evaluate when="<%=form.isApproveMode()%>">
            <c:button text="Setujui"  event="doApprovePolisKhusus" confirm="Do you want to approve ?" />
        </c:evaluate>
    </td>
</tr>
</table>

</c:frame>
<script>
    function addnewinvoice() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&rcpdate='+docEl('receipt.dtReceiptDate').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        600,450,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewInvoice';
                f.submit();
            }
        }
    );
    }

    function addnewnote() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&cust='+docEl('receipt.stEntityID').value
            +'&type='+docEl('receipt.stNoteType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewNote';
                f.submit();
            }
        }
    );
    }
   
    function addnewsurathutang() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_SURAT_HUTANG'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.nosurathutang.value=o.NO_SURAT_HUTANG;
                f.action_event.value='onNewSuratHutang';
                f.submit();
            }
        }
    );
    }
   
   
    function searchTitipan() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_TITIPAN'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&paymentmethodid='+docEl('receipt.stPaymentMethodID').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        600,450,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                document.getElementById('receipt.stARTitipanID').value=o.TITIPAN_ID;
                //f.action_event.value='onNewSuratHutang';
                //f.submit();
            }
        }
    );
    }
   
    function selectTitipanPremi(i,j) {
        var o = window.lovPopResult;

        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].stDescription').value = o.description;
        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].dbTitipanPremiAmount').value = o.jumlah;
        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].dbTitipanPremiUsedAmount').value = o.jumlah;
        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].dbTitipanPremiAmount').focus();
        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].dbTitipanPremiUsedAmount').focus();
        document.getElementById('listInvoices.[' + i + '].listTitipan.[' + j + '].stDescription').focus();
        
        f.artitipanid.value = docEl('listInvoices.[' + i + '].listTitipan.[' + j + '].stTitipanPremiID').value;

        f.invoicesindex.value = i;
        f.invoicecomissionindex.value = j;
        f.action_event.value='validateTitipanAlreadyIn';
        f.submit();

        

        //document.getElementById('receipt.stShortDescription').focus();
    }
    
    function addnewtitipan() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_TITIPAN'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        600,450,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                f.action_event.value='onNewTitipanPremi';
                f.submit();
            }
        }
    );
    }

   function addnewtitipankomisi() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_TITIPAN_MINUS'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        600,450,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                f.action_event.value='onNewTitipanPremiKomisi';
                f.submit();
            }
        }
    );
    }
   
    function selectLawanTitipan() {
        //alert('i : '+ (i+1));
        f.artitipanid.value = i+1;
        f.action_event.value = 'selectLawanTitipan';
        f.submit();
    }
    
    /*
    function selectLawanTitipan(i) {
        var o = window.lovPopResult;
        
        document.getElementById('listInvoices.[' + i + '].details.[0].stDescription').value = o.description;
       
    }*/

function addnewsurathutangcomm() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_SURAT_HUTANG_COMM'
            +'&ccy='+docEl('receipt.stCurrencyCode').value
            +'&arsid='+docEl('receipt.stARSettlementID').value
            +'&cc_code='+docEl('receipt.stCostCenterCode').value
            +'&journaltype='+docEl('receipt.stJournalType').value
            +'&type='+docEl('receipt.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.nosurathutang.value=o.NO_SURAT_HUTANG_COMM;
                f.action_event.value='onNewSuratHutangComm';
                f.submit();
            }
        }
    );
    }

</script>