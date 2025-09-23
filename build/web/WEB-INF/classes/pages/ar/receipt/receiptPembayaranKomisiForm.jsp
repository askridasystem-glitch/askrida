<%@ page import="com.webfin.ar.model.ARReceiptView,
com.webfin.ar.forms.ReceiptForm,
com.webfin.ar.model.ARReceiptLinesView,
com.crux.util.DTOList,
com.crux.util.Tools,
com.webfin.ar.model.ARAPSettlementView,
com.crux.util.JSPUtil,
com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
<%
boolean showNotes = true;

final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

final ARReceiptView receipt = form.getReceipt();

int jumlah = form.getListInvoices().size();

final boolean masterCurrency = receipt.isMasterCurrency();

final boolean isAP = receipt.isAP();
final boolean isAR = receipt.isAR();

final boolean isUsingEntityID = receipt.isUsingEntityID();

final boolean isNote = receipt.isNote();

final boolean hasReceiptClass = receipt.getStReceiptClassID()!=null;

final boolean hasBankType = receipt.getStBankType()!=null;

showNotes = isNote && hasReceiptClass;
boolean showInvoices = true && hasReceiptClass;
boolean showXC = false;
boolean suratHutang = false;
boolean useCheckFlag = false;
boolean only1Line = false;
boolean useJournalType = false;

final ARAPSettlementView settlement = receipt.getSettlement();

if (settlement!=null) {
    showXC = settlement.checkProperty("EN_XC","Y");
    useCheckFlag = settlement.checkProperty("CHK_FLG","Y");
    only1Line = settlement.checkProperty("ONE_LINE","Y");
    useJournalType = settlement.checkProperty("JOURNAL_TYPE","Y");
    
}

showInvoices &= settlement.checkProperty("EN_SL_IV","Y");
showNotes &= settlement.isNote();
suratHutang = settlement.checkProperty("SH","Y");

final String ccy = receipt.getStCurrencyCode();

/*
stEntityID
stARSettlementID
*/

int phase=0;

if (receipt.getStCostCenterCode()!=null) phase=1;
if (receipt.getStReceiptClassID()!=null) phase=2;
//if (receipt.getStPaymentMethodID()!=null) phase=3;
if (receipt.getStBankType()!=null) phase=3;
//if(receipt.getStBankType()==null&&receipt.getStReceiptClassID()!=null) phase=3;

if(phase==2 && receipt.getStReceiptClassID()!=null) phase=3;

if (phase==2 && showNotes && receipt.getStCostCenterCode()!=null) phase=3;

final boolean developmentMode = Config.isDevelopmentMode();

boolean readOnly = form.isReadOnly();
boolean canAdd = readOnly;

boolean titipan=false;
if (receipt.getStReceiptClassID()!=null)
    if(receipt.getStReceiptClassID().equals("8")) titipan=true;

boolean showTglBayar = true;

if(receipt.getStARSettlementID().equalsIgnoreCase("2")){
    showTglBayar = false;
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
                             <c:field name="receipt.stReceiptNo" width="160" mandatory="true" caption="{L-ENGPayment No-L}{L-INANomor Bukti-L}" type="string|64" presentation="standard" readonly="true" show="<%=receipt.getStReceiptNo()!=null%>" />
                            <c:field lov="LOV_CostCenter" width="200" name="receipt.stCostCenterCode" mandatory="true" caption="{L-ENGBranch-L}{L-INACabang-L}" type="string" presentation="standard" readonly="<%=receipt.getStCostCenterCode()!=null%>" changeaction="changeCostCenter" />
                            <c:field readonly="<%=hasReceiptClass%>" lov="LOV_ReceiptClass" width="230" changeaction="changeReceiptClass" name="receipt.stReceiptClassID" mandatory="true" caption="{L-ENGMethod-L}{L-INAMetode-L}" type="string" presentation="standard">
                                <c:param name="invoice_type" value="<%=receipt.getStInvoiceType()%>" />
                            </c:field>
                            
                            <c:evaluate when="<%=titipan%>">
                                <c:evaluate when="<%=phase>=2%>">
                                    <c:field readonly="<%=hasBankType%>" lov="LOV_BankType" width="150" changeaction="changeBankType" name="receipt.stBankType" mandatory="true" caption="Bank Type" type="string" presentation="standard"/> 
                                </c:evaluate>
                            </c:evaluate>
                            
                            <c:evaluate when="<%=phase>=3%>" >
                                <c:evaluate when="<%=!isNote%>">
                                    <c:evaluate when="<%=!isUsingEntityID%>">
                                        <c:field lov="LOV_PaymentMethod" width="400" name="receipt.stPaymentMethodID" mandatory="true" caption="{L-ENGAccount-L}{L-INARekening-L}" type="string"  presentation="standard" readonly="<%=receipt.getStPaymentMethodID()!=null%>" changeaction="changemethod" >
                                            <c:lovLink name="rc" link="receipt.stReceiptClassID" clientLink="false"/>
                                            <c:param name="ccbanktype" value="<%=receipt.getStCostCenterCode()+"|"+receipt.getStBankType()%>" />
                                        </c:field>
                                    </c:evaluate>
                                    
                                </c:evaluate>
                            </c:evaluate>
                            <c:evaluate when="<%=phase>=3%>">
                                <c:field name="receipt.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" readonly="<%=receipt.getStMonths()!=null%>" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth"/>
                                <c:field name="receipt.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" readonly="<%=receipt.getStYears()!=null%>" lov="LOV_GL_Years3" mandatory="true" presentation="standard" changeaction="setDate"/>

                                <c:field show="<%=useJournalType%>" name="receipt.stJournalType" type="string" caption="{L-ENGPayment Type-L}{L-INACara Pembayaran-L}" readonly="<%=receipt.getStJournalType()!=null%>" lov="LOV_JournalType" mandatory="true" presentation="standard" changeaction="setDate"/>
                                <c:field show="true" name="receipt.dtReceiptDate" mandatory="true" caption="{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}" type="date" presentation="standard" />

                                 <c:evaluate when="<%=isUsingEntityID%>">
                                    <c:field mandatory="true" caption="{L-ENGPayment Entity-L}{L-INABank-L}" name="receipt.stAccountEntityID" popuplov="true" lov="LOV_EntityFinance" type="string" width="200" presentation="standard" >
                                            <c:param name="month" value="<%=receipt.getStMonths()+"|"+receipt.getStYears()%>"  />
                                            <c:lovLink name="cc_code" link="receipt.stCostCenterCode" clientLink="false"/>
                                        </c:field>
                                    </c:evaluate>
                               
                                                    <c:field lov="LOV_Currency" changeaction="chgCurrency" name="receipt.stCurrencyCode" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" type="string" presentation="standard"/>
                                <c:field readonly="<%=masterCurrency%>" mandatory="true" name="receipt.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                                <c:field readonly="<%=isNote%>" name="receipt.dbEnteredAmount" mandatory="true" caption="{L-ENGAmount(ORG)-L}{L-INAJumlah Dibayar-L}" type="money16.2" presentation="standard" width="200"/>
                                <c:field include="<%=!masterCurrency%>"  name="receipt.dbAmount" readonly="true" caption="{L-ENGAmount(IDR)-L}{L-INAJumlah(IDR)-L}" type="money16.2" presentation="standard" width="200"/>                    
                            </c:evaluate>
                            
                        </table>
                    </td>
                    <td>
                        <c:evaluate when="<%=phase>=3%>">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="receipt.stInvoiceType" type="string" hidden="true" />
                                <c:field name="receipt.stNoteType" type="string" hidden="true" />
                                <c:field name="receipt.stInvoiceTypeDesc" readonly="true" caption="{L-ENGInvoice Type-L}{L-INAJenis Pembayaran-L}" type="string" presentation="standard"/>
                                <c:evaluate when="<%=!isNote%>">
                                    <c:field name="receipt.dbAmountApplied" readonly="true" caption="{L-ENGApplied-L}{L-INAJumlah Tagihan-L}" type="money16.2"  width="200" presentation="standard"/>
                                    <c:field name="receipt.dbRateDiffAmount" readonly="true" caption="Rate Diff" type="money16.2" width="200" presentation="standard"/>
                                </c:evaluate>
                                <c:field name="receipt.dbAmountRemain" readonly="true" caption="{L-ENGRemaining-L}{L-INASelisih-L}" type="money16.2" width="200" presentation="standard"/>
                                <c:field name="receipt.stPostedFlag" caption="{L-ENGPosted-L}{L-INAPosted-L}" type="check" presentation="standard" readonly="true"/>
                             
                            <c:field name="receipt.stIDRFlag" caption="{L-ENGIDR Flag-L}{L-INAIDR Flag-L}" type="check" presentation="standard"/>
                            <c:field name="receipt.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                     <tr>
                                         <td colspan="2"></td>
                                        <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                        </td>
                                     </tr>
                            </table>
                            <table>
                                <c:evaluate when="<%=titipan%>">
                                    <tr>
                                        <td>
                                            Titipan Premi : <c:field readonly="false" name="receipt.stARTitipanID" type="string" width="150" mandatory="true" caption="Titipan Premi" />
                                        </td>
                                        <td>
                                            <c:button text="..." clientEvent="searchTitipan()" validate="false" enabled="<%=!readOnly%>"/>
                                        </td>
                                    </tr>
                                </c:evaluate>
                            </table>
                        </c:evaluate>
                    </td>
                    
                    <tr>
                        
                    </tr>
                    
                </tr>
                <tr>
                    <td colspan=2>
                        <c:evaluate when="<%=phase>=3%>">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="receipt.stShortDescription" mandatory="false" caption="{L-ENGDescription-L}{L-INAKeterangan-L}" type="string|128" width="400" presentation="standard" />
                                <c:field name="receipt.stDescription" mandatory="false" caption="{L-ENGRemarks-L}{L-INACatatan-L}" type="string|255" rows="2" width="400" presentation="standard" />
                                <c:field name="receipt.stARSettlementID" hidden="true" caption="stARSettlementID" type="string" presentation="standard"/>
                            </table>
                        </c:evaluate>
                    </td>
                </tr>
                
            </table>
            
        </td>
    </tr>
    <c:evaluate when="<%=phase>=3%>">
        <c:evaluate when="<%=showNotes%>">
            <tr>
                <td>
                    <%=isAP?"Receivable":"Payable"%>
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
            <%=isAR?"Receivable":"Payable"%>
            &nbsp;Jumlah Komisi :&nbsp;<%=jumlah%>
            <%
                if(only1Line && jumlah ==1) canAdd = true;
            %>
            <table cellpadding=2 cellspacing=1>
                <tr>
                      <td>
                          <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!canAdd%>"/>
                      </td>
                  </tr>
            <tr>
                <td>
                <c:field name="invoicesindex" hidden="true" type="string"/>
                <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                <c:listbox name="listInvoices" selectable="true" paging="true" >
                <%
                final ARReceiptLinesView currentLine = (ARReceiptLinesView)form.getListInvoices().get(index.intValue());
                
                final boolean expanded = currentLine!=null && Tools.isYes(currentLine.getStExpandedFlag());
                %> 
                <%--<c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!readOnly%>"/>
                --%>  

                <c:evaluate when="<%=suratHutang%>" >
                    <c:listcol title="" columnClass="header" >
                        <c:button text="+ SH" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                    </c:listcol>
                </c:evaluate>
                <c:evaluate when="<%=!suratHutang%>" >
                    <c:listcol title="" columnClass="header" >
                        <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!canAdd%>"/>
                    </c:listcol>
                </c:evaluate>
                <c:listcol title="" columnClass="detail" >
                    <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>" />
                    
                </c:listcol>
                <c:listcol name="invoice.stARInvoiceID" title="ID" />
                <c:listcol title="Tanggal Bayar" >
                     <c:field name="<%="listInvoices.[$index$].dtReceiptDate"%>" caption="Tanggal Bayar" type="date" />
                </c:listcol> 
                <c:listcol name="stInvoiceNo" title="NO" />
                <c:listcol align="right" title="TOTAL(ORG)" ></c:listcol>
                <c:listcol align="right" title="Outstanding (ORG)" ></c:listcol>
                <c:listcol align="right" title="SETTLED(ORG)" ></c:listcol>
                <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>
                <c:listcol align="right" title="Payment(ORG)" >
                    
                    
                </c:listcol>
                <c:listcol align="right" title="<%="Payment("+ccy+")"%>" />
                <%--  <c:listcol  title="O/S" />--%>
                <c:listcol title="Bayar" />
                <c:evaluate when="<%=currentLine!=null && currentLine.getDetails().size()>0%>" >
                
                <%
                final DTOList commissions = currentLine.getDetails();
                
                for (int i = 0; i < commissions.size(); i++) {
                    ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);
                    
                    boolean isLock = rcl.isLock();

                    if(rcl.getInvoiceDetail().isCommission2()||rcl.getInvoiceDetail().isBrokerage2()||rcl.getInvoiceDetail().isHandlingFee2())
                        isLock = true;
                %>
            </tr><tr>
                <%--  <td class=row1><c:button text="-" event="onDeleteInvoiceComissionItem" clientEvent="<%="f.invoicesindex.value='$index$';f.invoicecomissionindex.value='"+i+"';"%>" validate="false" enabled="<%=!readOnly%>"/></td>
                --%><td class=row1></td>
                <td class=row1></td>
                <td class=row1></td>
                <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(),2)%></td>
                <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(),2)%></td>
                <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(),2)%></td>
                <td class=row1><c:field readonly="true" name="<%="listInvoices.[$index$].details.["+i+"].dbEnteredAmount"%>" caption="Amount" type="money16.2" /></td>
                <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmount(),2)%></td>
                <td class=row1><c:field name="<%="listInvoices.[$index$].details.["+i+"].stCheck"%>" readonly="<%=isLock%>" caption="Bayar" type="check" /></td>
                <%
                }
                %>
            </tr>
            <tr>
                        <td class=row1 colspan="5"/>
                        <td class=row1 colspan="2" align="right">
                            <strong>Subtotal</strong> 
                        </td>
                        <td class=row1 align="right">
                            <strong> <%=jspUtil.print(currentLine.getDbTotalAmountPerLine(),2)%></strong> 
                        </td> 
                        <td class=row1 colspan="2">
                             <c:button show="<%=!readOnly%>" text="Hitung" event="doRecalculate"/>
                             <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!canAdd%>"/>
                        </td>
                   </tr>

                    <c:evaluate when="<%=showXC%>">
                       <tr class="row1">
                           <td colspan="10">
                               <table>
                                   <tr class="header">
                                       <td colspan="2">
                                           Selisih Bayar
                                       </td>
                                       <td colspan="2">
                                           {L-ENGAccount-L}{L-INARekening-L}
                                       </td>
                                       <td colspan="2">
                                           {L-ENGDescription-L}{L-INAKeterangan-L}
                                       </td>
                                       <td colspan="4">
                                           Jumlah
                                       </td>

                                   </tr>
                                    <tr class="row1">
                                       <td colspan="2">
                                           <c:field name="listInvoices.[$index$].stARSettlementExcessID" width="150" type="string" caption="Item" lov="LOV_SettlementXC" >
                                               <c:param name="arsettlementid" value="<%=receipt.getStARSettlementID()%>" />
                                            </c:field>
                                       </td>
                                       <td colspan="2">
                                           <c:field  width="150" name="listInvoices.[$index$].stExcessAccountID" readonly="false" popuplov="true" lov="LOV_Account" caption="Excess" type="string" >
                                               <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                                           </c:field>
                                       </td>
                                       <td colspan="2">
                                           <c:field  width="250" name="listInvoices.[$index$].stExcessDescription" readonly="false" caption="Excess" type="string" />
                                       </td>
                                       <td colspan="4">
                                           <c:field name="listInvoices.[$index$].dbExcessAmount" readonly="false" caption="Excess" type="money16.2" />
                                       </td>
                                   </tr>
                               </table>
                           </td>
                       </tr>

                   </c:evaluate>
                   
            <tr>
            <td colspan=7>
                &nbsp;
            </td>
        </c:evaluate>
        </c:listbox>
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
            Selisih Bayar :
            <c:field name="stGLSelect" width="150" type="string" caption="Item" lov="LOV_SettlementXC" />
               
            <c:button text="+" event="addGL" validate="false" enabled="<%=!readOnly%>"/>
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
                            <c:listcol title="Account" >
                            <c:field  width="160" name="listGLs.[$index$].stExcessAccountID" lov="LOV_ExcessAccount" popuplov="true" clientchangeaction="selectAccount2($index$)" caption="Excess" type="string" >
                               <c:param name="cc_code" value="<%=receipt.getStCostCenterCode()%>" />
                           </c:field>
                          <%-- <c:button text="+" clientEvent="selectAccountByBranch2($index$)" validate="false" enabled="true"/>
                           --%></c:listcol>
                           <c:listcol title="Description" >
                                <c:field name="listGLs.[$index$].stExcessDescription" width="310" caption="Description" type="string" />
                            </c:listcol>
                            <c:listcol title="Amount(ORG)" >
                                <c:field name="listGLs.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                            </c:listcol>
                            <c:listcol name="dbAmount" title="Amount(IDR)" />
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
            <c:evaluate when="<%=phase>=3%>">
                <c:button text="Hitung Ulang" event="doRecalculate"/>
                <c:button text="Simpan" event="doSavePembayaranKomisi" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />
            <%--  <c:button text="Generate Payment No" event="generatRNo" show="<%=developmentMode%>" />
         	--%>
        </c:evaluate>
        <c:evaluate when="<%=form.isReverseMode()%>" >
            <c:button text="Reverse" event="reverse"/>
        </c:evaluate>
        <c:evaluate when="<%=readOnly%>" >
            <c:button text="Batal" event="doClose"/>
        </c:evaluate>
        <c:evaluate when="<%=form.isApproveMode()%>">
            <c:button text="Setujui"  event="doApprovePembayaranKomisi" confirm="Do you want to approve ?" />
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
                  +'&rcpdate='+docEl('receipt.dtReceiptDate').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&journaltype='+docEl('receipt.stJournalType').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  600,450,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewInvoicePembayaranKomisi';
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
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
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
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&paymentmethodid='+docEl('receipt.stPaymentMethodID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  500,400,
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
   
   function selectAccount2(i) {
        var o = window.lovPopResult;
        //document.getElementById('listGLs.[' + i + '].stExcessDescription').value = o.description;
        f.glindex.value = i;
        f.action_event.value='createAccount';
        f.submit();
    }
</script>