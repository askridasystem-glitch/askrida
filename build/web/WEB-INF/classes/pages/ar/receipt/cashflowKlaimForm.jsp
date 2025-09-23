<%@ page import="com.webfin.ar.model.ARCashflowView,
         com.webfin.ar.forms.CashflowKlaimForm,
         com.webfin.ar.model.ARCashflowDetailsView,
         com.crux.util.DTOList,
         com.crux.util.Tools,
         com.webfin.ar.model.ARAPSettlementView,
         com.crux.util.JSPUtil,
         com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%
                boolean showNotes = true;

                final CashflowKlaimForm form = (CashflowKlaimForm) request.getAttribute("FORM");

                int jumlah = form.getListInvoices().size();

                final ARCashflowView cashflow = form.getCashflow();

                final boolean masterCurrency = cashflow.isMasterCurrency();

                final boolean isAP = cashflow.isAP();
                final boolean isAR = cashflow.isAR();

                final boolean isUsingEntityID = cashflow.isUsingEntityID();

                final boolean canNavigateBranch = form.isCanNavigateBranch();

                final boolean isNote = cashflow.isNote();

                final boolean hasReceiptClass = cashflow.getStReceiptClassID() != null;

                final boolean hasBankType = cashflow.getStBankType() != null;

                showNotes = isNote && hasReceiptClass;
                boolean showInvoices = true && hasReceiptClass;
                boolean showXC = false;
                boolean suratHutang = false;
                boolean useCheckFlag = false;
                boolean only1Line = false;
                boolean useTitipanFlag = false;
                boolean realisasiKlaim = false;

                final ARAPSettlementView settlement = cashflow.getSettlement();

                if (settlement != null) {
                    showXC = settlement.checkProperty("EN_XC", "Y");
                    useCheckFlag = settlement.checkProperty("CHK_FLG", "Y");
                    only1Line = settlement.checkProperty("ONE_LINE", "Y");
                    useTitipanFlag = settlement.checkProperty("TITIP_FLG", "Y");
                    realisasiKlaim = settlement.checkProperty("REALISASI_FLG", "Y");
                }

                showInvoices &= settlement.checkProperty("EN_SL_IV", "Y");
                showNotes &= settlement.isNote();
                suratHutang = settlement.checkProperty("SH", "Y");

                final String ccy = cashflow.getStCurrencyCode();

                int phase = 0;

                if (cashflow.getStCostCenterCode() != null) {
                    phase = 1;
                }
    //if (cashflow.getStReceiptClassID()!=null) phase=2;
    //if (cashflow.getStPaymentMethodID()!=null) phase=3;
    //if (cashflow.getStBankType()!=null) phase=3;
    //if(cashflow.getStBankType()==null&&cashflow.getStReceiptClassID()!=null) phase=3;

    //if(phase==2 && cashflow.getStReceiptClassID()!=null) phase=3;

    //if (phase==2 && showNotes && cashflow.getStCostCenterCode()!=null) phase=3;

                final boolean developmentMode = Config.isDevelopmentMode();

                boolean readOnly = form.isReadOnly();
                boolean canAdd = readOnly;

                boolean hasClaim = true;

                boolean canUseSuratHutang = true;

    %>


    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="parinvoiceid" type="string" hidden="true"/>
                <c:field name="nosurathutang" type="string" hidden="true"/>
                <c:field name="artitipanid" type="string" hidden="true"/>
                <c:field name="invoiceid" type="string" hidden="true"/>

                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="cashflow.stReceiptNo" width="160" mandatory="true" caption="{L-ENGPayment No-L}{L-INANomor Bukti-L}" type="string|64" presentation="standard" readonly="true" show="<%=cashflow.getStReceiptNo() != null%>" />
                                <c:field lov="LOV_CostCenter" width="200" name="cashflow.stCostCenterCode" mandatory="true" caption="{L-ENGBranch-L}{L-INACabang-L}" type="string" presentation="standard" readonly="<%=cashflow.getStCostCenterCode() != null%>" changeaction="changeCostCenter" />



                                <c:evaluate when="<%=phase >= 1%>">
                                    <c:field name="cashflow.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" readonly="<%=cashflow.getStMonths() != null%>" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth"/>
                                    <c:field name="cashflow.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" readonly="<%=cashflow.getStYears() != null%>" lov="LOV_GL_Years2" mandatory="true" presentation="standard" changeaction="setDate"/>
                                    <c:evaluate when="<%=isUsingEntityID%>">
                                        <c:field caption="{L-ENGPayment Entity-L}{L-INABank-L}" name="cashflow.stAccountEntityID" popuplov="true" lov="LOV_EntityFinance" type="string" width="200" presentation="standard" >
                                            <c:param name="month" value="<%=cashflow.getStMonths() + "|" + cashflow.getStYears()%>"  />
                                            <c:lovLink name="cc_code" link="cashflow.stCostCenterCode" clientLink="false"/>
                                        </c:field>
                                    </c:evaluate>


                                    <c:field show="true" name="cashflow.dtReceiptDate" mandatory="true" caption="{L-ENGPayment Date-L}{L-INATanggal Input-L}" type="date" presentation="standard" />

                                    <c:field lov="LOV_Currency" readonly="<%=cashflow.getStCurrencyCode() != null%>" changeaction="chgCurrency" name="cashflow.stCurrencyCode" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" type="string" presentation="standard"/>
                                    <c:field readonly="<%=masterCurrency%>" readonly="true" mandatory="true" name="cashflow.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                                    <c:field readonly="<%=isNote%>" name="cashflow.dbEnteredAmount" mandatory="true" caption="{L-ENGAmount(ORG)-L}{L-INAJumlah(ORG)-L}" type="money16.2" presentation="standard" width="200"/>
                                    <c:field include="<%=!masterCurrency%>"  name="cashflow.dbAmount" readonly="true" caption="{L-ENGAmount(IDR)-L}{L-INAJumlah(IDR)-L}" type="money16.2" presentation="standard" width="200"/>
                                </c:evaluate>

                            </table>
                        </td>
                        <td>
                            <c:evaluate when="<%=phase >= 1%>">
                                <table cellpadding=2 cellspacing=1>
                                    <c:field name="cashflow.stInvoiceType" type="string" hidden="true" />
                                    <c:field name="cashflow.stNoteType" type="string" hidden="true" />

                                    <c:evaluate when="<%=!isNote%>">
                                        <c:field name="cashflow.dbAmountApplied" readonly="true" caption="{L-ENGApplied-L}{L-INAJumlah Tagihan-L}" type="money16.2"  width="200" presentation="standard"/>
                                    </c:evaluate>

                                    <c:field name="cashflow.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                             readonly="false" presentation="standard"/>
                                    <tr>
                                        <td colspan="2"></td>
                                        <td><c:button show="true" text="Konversi" event="uploadKonversi"/>
                                        </td>
                                    </tr>

                                </table>

                            </c:evaluate>
                        </td>

                    <tr>

                    </tr>

        </tr>
        <tr>
            <td colspan=2>
                <c:evaluate when="<%=phase >= 1%>">
                    <table cellpadding=2 cellspacing=1>
                        <c:field name="cashflow.stShortDescription" mandatory="false" caption="{L-ENGDescription-L}{L-INAKeterangan-L}" type="string|128" width="400" presentation="standard" />
                        <c:field name="cashflow.stDescription" mandatory="false" caption="{L-ENGRemarks-L}{L-INACatatan-L}" type="string|255" rows="2" width="400" presentation="standard" />
                        <c:field name="cashflow.stARSettlementID" hidden="true" caption="stARSettlementID" type="string" presentation="standard"/>
                    </table>
                </c:evaluate>
            </td>
        </tr>

    </table>

</td>
</tr>
<c:evaluate when="<%=phase >= 1%>">
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
    <c:evaluate when="<%=true%>">
        <tr>
            <td>
                <%=isAR ? "Receivable" : "Payable"%>
                <%
                            if (only1Line && jumlah == 1) {
                                canAdd = true;
                            }
                %>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!readOnly%>"/>

                            <c:evaluate when="<%=suratHutang && canUseSuratHutang%>">
                                <c:button text="Surat Hutang" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                            </c:evaluate>
                            <c:evaluate when="<%=!realisasiKlaim%>">
                                <c:button text="+ Klaim" clientEvent="selectPolicy()" validate="false" enabled="<%=!canAdd%>"/>
                            </c:evaluate>

                            &nbsp;Jumlah Data :&nbsp;<%=jumlah%>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:field name="invoicesindex" hidden="true" type="string"/>
                            <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                            <c:listbox name="listInvoices" paging="true" selectable="true" >
                                <%
                                            final ARCashflowDetailsView currentLine = (ARCashflowDetailsView) form.getListInvoices().get(index.intValue());

                                            final boolean expanded = currentLine != null && Tools.isYes(currentLine.getStExpandedFlag());

                                %>
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>" />
                                </c:listcol>
                                <c:listcol name="invoice.stARInvoiceID" title="ID" />
                                <c:listcol title="Tanggal Input" >
                                    <c:field name="<%="listInvoices.[$index$].dtReceiptDate"%>" readonly="true" caption="Tanggal Bayar" type="date" />
                                </c:listcol>

                                <c:listcol name="stInvoiceNo" title="No" />


                                <c:evaluate when="<%=hasClaim%>">
                                    <c:listcol name="stArInvoiceClaim" title="Nomor LKP" />
                                </c:evaluate>
                                <c:listcol align="right" title="Nilai" ></c:listcol>
                                <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>

                                <%--  <c:listcol  title="O/S" />--%>
                                <c:evaluate when="<%=useCheckFlag%>" >
                                    <c:listcol title="" />
                                </c:evaluate>
                                <c:evaluate when="<%=useTitipanFlag%>" >
                                    <c:listcol title="Ttp">
                                        <c:field name="<%="listInvoices.[$index$].stTitipanFlag"%>" caption="Titip" type="check" /></td>
                                    </c:listcol>
                                </c:evaluate>
                                <c:evaluate when="<%=currentLine != null && currentLine.getDetails().size() > 0%>" >

                                <%
                                            final DTOList commissions = currentLine.getDetails();

                                            for (int i = 0; i < commissions.size(); i++) {
                                                ARCashflowDetailsView rcl = (ARCashflowDetailsView) commissions.get(i);

                                                boolean isLock = rcl.isLock();

                                                String invoiceID = rcl.getStInvoiceID();

                                                boolean isOther = rcl.isOthers();

                                %>
                            </tr>
                            <tr>
                                <td class=row1></td>
                                <td class=row1></td>
                                <td class=row1></td>

                                <c:evaluate when="<%=!isOther%>">
                                    <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>

                                </c:evaluate>
                                <c:evaluate when="<%=isOther%>">
                                    <td class=row1></td>
                                    <td class=row1>
                                        <c:field  width="120" name="<%="listInvoices.[$index$].details.[" + i + "].stAccountID"%>" readonly="false" popuplov="true" lov="LOV_Account3" caption="Account" type="string" >
                                            <c:param name="cc_code" value="<%=cashflow.getStCostCenterCode()%>" />
                                        </c:field>
                                    </td>
                                    <td class=row1>
                                        <c:field  width="150" name="<%="listInvoices.[$index$].details.[" + i + "].stPolicyID"%>" readonly="false" popuplov="true" width="130" lov="LOV_CLAIM" caption="Polis" type="string" >
                                            <c:param name="cc_code" value="<%=cashflow.getStCostCenterCode()%>" />
                                        </c:field>
                                    </td>

                                    <td class=row1>
                                        <c:field readonly="false" name="<%="listInvoices.[$index$].details.[" + i + "].stDescription"%>" caption="Deskripsi" type="string" width="300" />
                                    </td>


                                </c:evaluate>

                                <c:evaluate when="<%=hasClaim%>">
                                    <td class=row1></td>
                                </c:evaluate>
                                <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(), 2)%></td>

                                <c:evaluate when="<%=useCheckFlag%>" >
                                    <td class=row1><c:field name="<%="listInvoices.[$index$].details.[" + i + "].stCheck"%>" readonly="<%=isLock%>" caption="Bayar" type="check" /></td>
                                </c:evaluate>
                                <c:evaluate when="<%=useTitipanFlag%>" >
                                    <td class=row1></td>
                                </c:evaluate>
                                <%
                                            }
                                %>
                            </tr>
                            <tr>
                                <c:evaluate when="<%=!hasClaim%>">
                                    <td class=row1 colspan="5"/>
                                </c:evaluate>
                                <c:evaluate when="<%=hasClaim%>">

                                </c:evaluate>

                                <td class=row1 colspan="5" align="right">
                                    <strong>Subtotal</strong>
                                </td>
                                <td class=row1 align="right">
                                    <strong> <%=jspUtil.print(currentLine.getDbTotalAmountPerLine(), 2)%></strong>
                                </td>
                                <td class=row1 colspan="2">
                                    <c:button  show="<%=!readOnly%>" text="Hitung" event="doRecalculate"/>
                                    <c:evaluate when="<%=realisasiKlaim%>">
                                        <c:button text="+" clientEvent="addnewinvoice()" show="<%=!readOnly%>" validate="false" enabled="<%=!canAdd%>"/>
                                    </c:evaluate>
                                    <c:evaluate when="<%=!realisasiKlaim%>">
                                        <c:button text="+" clientEvent="selectPolicy()" show="<%=!readOnly%>" validate="false" enabled="<%=!canAdd%>"/>
                                    </c:evaluate>

                                </td>
                                <c:evaluate when="<%=useTitipanFlag%>" >
                                    <td class=row1></td>
                                </c:evaluate>
                            </tr>

                            <tr>
                                <td colspan=10>
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

</c:evaluate>
<tr>
    <td align=center>
        <c:evaluate when="<%=!readOnly%>" >
            <c:evaluate when="<%=phase >= 1%>">
                <c:button text="Hitung Ulang" event="doRecalculate"/>
                <c:button text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
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
            <c:button text="Setujui"  event="doApprove" confirm="Do you want to approve ?" />
        </c:evaluate>
    </td>
</tr>
</table>

</c:frame>
<script>
    function addnewinvoice() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&arsid='+docEl('cashflow.stARSettlementID').value
            +'&cc_code='+docEl('cashflow.stCostCenterCode').value
            +'&rcpdate='+docEl('cashflow.dtReceiptDate').value
            +'&type='+docEl('cashflow.stInvoiceType').value,
        700,450,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewInvoiceUangMukaKlaim';
                f.submit();
            }
        }
    );
    }

    function addnewnote() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&cust='+docEl('cashflow.stEntityID').value
            +'&type='+docEl('cashflow.stNoteType').value,
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
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&arsid='+docEl('cashflow.stARSettlementID').value
            +'&cc_code='+docEl('cashflow.stCostCenterCode').value
            +'&type='+docEl('cashflow.stInvoiceType').value,
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
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&paymentmethodid='+docEl('cashflow.stPaymentMethodID').value
            +'&arsid='+docEl('cashflow.stARSettlementID').value
            +'&cc_code='+docEl('cashflow.stCostCenterCode').value
            +'&type='+docEl('cashflow.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                document.getElementById('cashflow.stARTitipanID').value=o.TITIPAN_ID;
                //f.action_event.value='onNewSuratHutang';
                //f.submit();
            }
        }
    );
    }
   
    function selectAccountByBranch2(i){
   		
        var cabang = document.getElementById('cashflow.stCostCenterCode').value;
        var acccode = 'undefined';
        
        
        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
        
        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400, 400,
        function (o) {
            if (o != null) {
                document.getElementById('listGLs.[' + i + '].stExcessAccountID').value = o.acid;
                f.submit();
            }
        }
    );
    }
   
    var rnc;
    function selectAccount(o) {
        if (o==null) return;

        var o = window.lovPopResult;
        document.getElementById('listGLs.[' + i + '].stExcessAccountID').value = o.acid;
        f.submit();

    }
   
    function selectAccount2(i) {
        var o = window.lovPopResult;
        //document.getElementById('listGLs.[' + i + '].stExcessDescription').value = o.description;
        f.glindex.value = i;
        f.action_event.value='createAccount';
        f.submit();
    }
    
    function addnewuangmuka() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_LKS'
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&arsid='+docEl('cashflow.stARSettlementID').value
            +'&cc_code='+docEl('cashflow.stCostCenterCode').value
            +'&rcpdate='+docEl('cashflow.dtReceiptDate').value
            +'&type='+docEl('cashflow.stInvoiceType').value,
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
   
    function selectPolicy(o){

        var tglBayar = document.getElementById('cashflow.dtReceiptDate').value;

        if(tglBayar == ''){
            alert('Tanggal bayar klaim belum diisi');
            return;
        }

        var cabang = document.getElementById('cashflow.stCostCenterCode').value;
        openDialog('so.ctl?EVENT=INS_CLAIM_SEARCH_CF&costcenter='+cabang+'',800,500,function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.polid;
                f.invoiceid.value=o.invoiceid;
                //alert('pol_id :'+ o.polid);
                //f.action_event.value='onNewUangMukaKlaim';
                f.action_event.value='onNewKlaimByPolID';
                f.submit();
            }
        });
    }

    function addnewsurathutangklaim() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_SURAT_HUTANG_CLAIM'
            +'&ccy='+docEl('cashflow.stCurrencyCode').value
            +'&arsid='+docEl('cashflow.stARSettlementID').value
            +'&cc_code='+docEl('cashflow.stCostCenterCode').value
            +'&type='+docEl('cashflow.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.nosurathutang.value=o.NO_SURAT_HUTANG_CLAIM;
                f.action_event.value='onNewSuratHutangClaim';
                f.submit();
            }
        }
    );
    }
   

</script>