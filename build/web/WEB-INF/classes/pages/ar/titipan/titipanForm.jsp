<%@ page import="com.webfin.ar.model.ARTitipanView,
                 com.webfin.ar.forms.TitipanForm,
                 com.webfin.ar.model.ARReceiptLinesView,
                 com.crux.util.DTOList,
                 com.crux.util.Tools,
                 com.webfin.ar.model.ARAPSettlementView,
                 com.crux.util.JSPUtil,
                 com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TITIPAN PREMI" >
<%
   boolean showNotes = true;

   final TitipanForm form = (TitipanForm) request.getAttribute("FORM");

   final ARTitipanView receipt = form.getReceipt();
   
   final boolean editMode = form.isEditMode();
   
   //receipt.getStARSettlementID();

   //final boolean masterCurrency = receipt.isMasterCurrency();

   //final boolean isAP = receipt.isAP();
   //final boolean isAR = receipt.isAR();

   //final boolean isNote = receipt.isNote();

   //final boolean hasReceiptClass = receipt.getStReceiptClassID()!=null;
   
   //final boolean hasBankType = receipt.getStBankType()!=null;
   

   //showNotes = isNote && hasReceiptClass;
   //boolean showInvoices = true && hasReceiptClass;
   //boolean showXC = false;
   //boolean suratHutang = false;

   //final ARAPSettlementView settlement = receipt.getSettlement();

   //if (settlement!=null) {
   //   showXC = settlement.checkProperty("EN_XC","Y");
   //}


   //showInvoices &= settlement.checkProperty("EN_SL_IV","Y");
   //showNotes &= settlement.isNote();
   //suratHutang = settlement.checkProperty("SH","Y");

   //final String ccy = receipt.getStCurrencyCode();

   /*
   stEntityID
   stARSettlementID
   */

   int phase=0;

   //if (receipt.getStCostCenterCode()!=null) phase=1;
   //if (receipt.getStReceiptClassID()!=null) phase=2;
   //if (receipt.getStBankType()!=null) phase=3;
   
   //if(phase==2 && receipt.getStReceiptClassID()!=null) phase=3;

   //if (phase==2 && showNotes && receipt.getStCostCenterCode()!=null) phase=3;

   //final boolean developmentMode = Config.isDevelopmentMode();

   //boolean readOnly = form.isReadOnly();
   /*
   boolean titipan=false;
   if (receipt.getStReceiptClassID()!=null)
   		if(receipt.getStReceiptClassID().equals("8")) titipan=true;*/ 

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
                  <c:field name="receipt.stARTitipanID" width="130" mandatory="false" caption="AR Titipan ID" type="string|64" presentation="standard" readonly="true" show="true" />
                  <c:field name="receipt.stTransactionNo" width="130" mandatory="false" caption="Trx No" type="string|64" presentation="standard" readonly="true" show="true" />
      			  <c:field lov="LOV_CostCenter" width="200" name="receipt.stCostCenterCode" mandatory="true" caption="Branch" type="string" presentation="standard" readonly="<%=receipt.getStCostCenterCode()!=null%>" changeaction="changeCostCenter" />
		          <c:field name="receipt.stAccountID" width="130" mandatory="true" caption="Account ID" show="false" type="string|64" presentation="standard" readonly="<%=!editMode%>" />
          
		    <tr>
                      <td>Account Titipan</td>
                      <td>:</td>
                      <td>
                        <c:field name="receipt.stAccountNo" width="130" mandatory="true" caption="Account No" type="string|64" readonly="false" show="true" />
                        <c:button text="..." clientEvent="selectAccountByBranch()" />

                      </td>
                   </tr>
      			  <c:field name="receipt.stDescription" width="250" mandatory="true" caption="Description" type="string|128" presentation="standard" readonly="<%=!editMode%>" show="true" />
      			  <c:field name="receipt.stAccountIDVs" width="130" mandatory="true" caption="Account ID" show="false" type="string|64" presentation="standard" readonly="<%=!editMode%>" />
          
		            <tr>
                      <td>Account Lawan</td>
                      <td>:</td>
                      <td>
                        <c:field name="receipt.stAccountNoVs" width="130" mandatory="true" caption="Account No Vs" type="string|64" show="true" />
                        <c:button text="..." clientEvent="selectAccountByBranch2()" />

                      </td>
                   </tr>
      			  <c:field name="receipt.stDescriptionVs" width="250" mandatory="true" caption="Description" type="string|128" presentation="standard" readonly="<%=!editMode%>" show="true" />
    	
    			  <c:field name="receipt.dtApplyDate" width="130" mandatory="true" caption="Apply Date" type="date" presentation="standard" readonly="<%=!editMode%>" show="true" />
   			      <c:field lov="LOV_Currency" changeaction="onChgCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" name="receipt.stCurrencyCode" type="string" mandatory="true" readonly="false" presentation="standard"/>
                  <c:field caption="Kurs" name="receipt.dbCurrencyRate" type="money16.2" mandatory="true" readonly="false" presentation="standard"/>
   			      <c:field name="receipt.dbBalance" width="130" mandatory="true" caption="Balance" type="money16.2" presentation="standard" readonly="<%=!editMode%>" show="true" />
                  
               </table>
               </td>
               
               
             <tr>
               
            </tr>
               
            </tr>
    
         </table>

      </td>
   </tr>
   <tr>
      <td align=center>
                 <c:button text="Recalculate" event="doRecalculate"/>
               <c:button text="Save" event="doSave" validate="true" confirm="Do you want to save ?" />
            <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
             
            <c:button text="Close" event="doClose"/>
         <%--  <c:button text="Approve" event="doApprove" confirm="Do you want to approve ?" />
		--%>
      </td>
   </tr>

</table>

</c:frame>
<script>
   function addnewinvoice() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  500,400,
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
   
      function selectAccountByBranch(o){
   		//alert(document.getElementById('costcenter').value);
   	    //alert(document.getElementById('method2').value);
   	    var cabang = document.getElementById('receipt.stCostCenterCode').value;
            var acccode = '489';
   	    //var method = document.getElementById('method2').value;
   		openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }
   
   var rnc;
   function selectAccount(o) {
      if (o==null) return;
      document.getElementById('receipt.stAccountID').value=o.acid;
      document.getElementById('receipt.stAccountNo').value=o.acno;
      document.getElementById('receipt.stDescription').value=o.desc;
   }
   
   function selectAccountByBranch2(o){
   		//alert(document.getElementById('costcenter').value);
   	    //alert(document.getElementById('method2').value);
   	    var cabang = document.getElementById('receipt.stCostCenterCode').value;
            var acccode = '1221';
   	    //var method = document.getElementById('method2').value;
   		openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);
   }
   
      var rnc;
   function selectAccount2(o) {
      if (o==null) return;
      document.getElementById('receipt.stAccountIDVs').value=o.acid;
      document.getElementById('receipt.stAccountNoVs').value=o.acno;
      document.getElementById('receipt.stDescriptionVs').value=o.desc;
   }
</script>