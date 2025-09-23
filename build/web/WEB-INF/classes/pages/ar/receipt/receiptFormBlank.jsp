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

int jumlah = form.getListInvoices().size();

final ARReceiptView receipt = form.getReceipt();

final boolean masterCurrency = receipt.isMasterCurrency();

final boolean isAP = receipt.isAP();
final boolean isAR = receipt.isAR();

final boolean isUsingEntityID = receipt.isUsingEntityID();

final boolean canNavigateBranch = form.isCanNavigateBranch();

final boolean isNote = receipt.isNote();

final boolean hasReceiptClass = receipt.getStReceiptClassID()!=null;

final boolean hasBankType = receipt.getStBankType()!=null;

showNotes = isNote && hasReceiptClass;
boolean showInvoices = true && hasReceiptClass;
boolean showXC = false;
boolean suratHutang = false;
boolean useCheckFlag = false;
boolean only1Line = false;
boolean useTitipanFlag = false;
boolean useJournalType = false;
boolean nomorRekap = false;
boolean nomorReferensi = false;

final ARAPSettlementView settlement = receipt.getSettlement();

if (settlement!=null) {
    showXC = settlement.checkProperty("EN_XC","Y");
    useCheckFlag = settlement.checkProperty("CHK_FLG","Y");
    only1Line = settlement.checkProperty("ONE_LINE","Y");
    useTitipanFlag = settlement.checkProperty("TITIP_FLG","Y");
    useJournalType = settlement.checkProperty("JOURNAL_TYPE","Y");
    nomorRekap = settlement.checkProperty("RECAP_NO","Y");
    nomorReferensi = settlement.checkProperty("REF_NO","Y");
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
    if(receipt.getStReceiptClassID().equals("8")||receipt.getStReceiptClassID().equals("9")||receipt.getStReceiptClassID().equals("10")) titipan=true; 
    
boolean hasClaim = false;

if (receipt.getStARSettlementID().equalsIgnoreCase("10")) {
    hasClaim = true;
}

boolean canUseSuratHutang = true;

if(receipt.getStARSettlementID().equalsIgnoreCase("13")){
    if(!canNavigateBranch) canUseSuratHutang = false;
}

boolean showTglBayar = true;

if(receipt.getStARSettlementID().equalsIgnoreCase("1") || receipt.getStARSettlementID().equalsIgnoreCase("13")){
    showTglBayar = false;
}

if(receipt.getStJournalType()!=null)
    if(receipt.isJournalOffset())
        showTglBayar = true;


showTglBayar = true;

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
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  650,400,
         function (o) {
            if (o!=null) {
               f.nosurathutang.value=o.NO_SURAT_HUTANG;
               if(o.TGL_MULAI!='null') f.periodstart.value=o.TGL_MULAI.substring(8,10) + '/' + o.TGL_MULAI.substring(5,7) +'/'+o.TGL_MULAI.substring(0,4);
               if(o.TGL_AKHIR!='null') f.periodend.value=o.TGL_AKHIR.substring(8,10) + '/' + o.TGL_AKHIR.substring(5,7) +'/'+o.TGL_AKHIR.substring(0,4);
               f.action_event.value='onNewSuratHutang';
               f.submit();
            }
         }
      );
   }

   function addnewnomorrekap() {

      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_NOMOR_REKAP'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  650,400,
         function (o) {
            if (o!=null) {
               f.nosurathutang.value=o.NO_SURAT_HUTANG;
               if(o.TGL_MULAI!='null') f.periodstart.value=o.TGL_MULAI.substring(8,10) + '/' + o.TGL_MULAI.substring(5,7) +'/'+o.TGL_MULAI.substring(0,4);
               if(o.TGL_AKHIR!='null') f.periodend.value=o.TGL_AKHIR.substring(8,10) + '/' + o.TGL_AKHIR.substring(5,7) +'/'+o.TGL_AKHIR.substring(0,4);
               f.action_event.value='onNewNomorRekap';
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
   
   function selectAccountByBranch2(i){
   		
        var cabang = document.getElementById('receipt.stCostCenterCode').value;
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
    
    function tes(i) {
        //var o = window.lovPopResult;
        //alert('id : ' + i);
        //listInvoices.[$index$].details.["+i+"].stInvoiceID
        alert('amount : ' + document.getElementById('listInvoices.[' + i + '].details.[' + i + '].stInvoiceID').value);
        //f.invoiceid.value = document.getElementById('listInvoices.[' + i + '].stInvoiceID').value;
        //f.action_event.value='addGLPerLine';
        //f.submit();
    }

    function addnewreferenceno() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_REFERENCE_NO'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&rcpdate='+docEl('receipt.dtReceiptDate').value
                  +'&journaltype='+docEl('receipt.stJournalType').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  700,450,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewInvoice';
               f.submit();
            }
         }
      );
   }


</script>