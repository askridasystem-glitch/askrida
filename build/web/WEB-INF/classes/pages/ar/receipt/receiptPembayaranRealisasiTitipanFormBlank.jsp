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

                final ARAPSettlementView settlement = receipt.getSettlement();

                if (settlement != null) {
                    showXC = settlement.checkProperty("EN_XC", "Y");
                    useCheckFlag = settlement.checkProperty("CHK_FLG", "Y");
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
                FORM NOT FOUND !
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