<%@ page import="com.webfin.ar.forms.InvoiceForm,
                 com.webfin.ar.model.ARInvoiceView,
                 com.webfin.ar.model.ARTransactionTypeView,
                 com.crux.util.Tools,
                 com.crux.util.BDUtil,
                 com.webfin.gl.ejb.CurrencyManager,
                 com.webfin.ar.model.ARInvoiceDetailView,
                 com.webfin.ar.model.ARTaxView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SURAT HUTANG" >
<%
   final InvoiceForm form = (InvoiceForm)request.getAttribute("FORM");

   final ARInvoiceView invoice = form.getInvoice();
   
   int phase=0;

   if (form.getStEntityID()!=null||invoice.getStEntityID()!=null) phase=1;
   //if (receipt.getStReceiptClassID()!=null) phase=2;
	
	final boolean cek = form.getCek();
  
%>
<%--  
<c:evaluate when="<%=superType%>">
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <c:field changeaction="onChangeTRXType" readonly="false" lov="lovARTrxType" name="invoice.stARTransactionTypeID" width="200" caption="TRANSACTION TYPE" type="string" presentation="standard" mandatory="true" />
      </td>
   </tr>
</table>
</c:evaluate>
--%>

   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>

               <tr>
                  <td valign=top>
                     <table cellpadding=2 cellspacing=1>
                           <c:field name="invoice.stNoSuratHutang" width="200" caption="NO SURAT HUTANG" type="string|64" presentation="standard" readonly="true" />
                           <c:field name="invoice.dtSuratHutangPeriodFrom" caption="POLICY PERIOD FROM" type="date" presentation="standard" mandatory="true" />
                           <c:field name="invoice.dtSuratHutangPeriodTo" caption="POLICY PERIOD TO" type="date" presentation="standard" mandatory="true" />
            			       <tr>
                                 <td>CUSTOMER</td>
                                 <td>:</td>
                                 <td>
                                    <c:field name="invoice.stEntityID" type="string" hidden="true" />
                                    <c:field width="200" caption="Customer" name="invoice.stEntityName" type="string" mandatory="true" readonly="true" changeaction="changeEntity" />
                                    <c:button text="..." clientEvent="selectCustomer()" />
                                 </td>
                              </tr>
                          
                     </table>
                  </td>
                  
               </tr>
            </table>
         </td>
      </tr>
		<tr>
      <td>
      <c:evaluate when="<%=phase>=1%>">
         <c:listbox name="list" selectable="true" paging="true">
            <c:listcol name="stEntityName" title="Customer" />
            <c:listcol name="stInvoiceType" title="Type" />
            <c:listcol name="stInvoiceNo" title="Invoice Number" />
            <c:listcol name="dtInvoiceDate" title="Date" />
            <c:listcol name="dtDueDate" title="Due Date" />
            <c:listcol name="stCurrencyCode" title="Ccy" />
            <c:listcol name="dbEnteredAmount" title="Amount" />
            <c:listcol name="dbAmountSettled" title="Settled" />
            <c:listcol name="stNoSuratHutang" title="No Surat Hutang" />
         </c:listbox>
       </c:evaluate>
      </td>
   </tr>
            
         <td>
         	<c:evaluate when="<%=!cek%>">
            <c:button text="Save" event="clickSaveSuratHutang" validate="true" />  
            </c:evaluate>       
            <c:button text="Cancel" event="clickCancel" validate="false" />
         </td>
      </tr>
   </table>
   <script>
      function pop2() {
         openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,
            function (o) {
               if (o!=null) {
                  docEl('invoice.stGLARAccountID').value=o.acid;
                  docEl('invoice.stGLARAccountDesc').value=o.acdesc;
               }
            }
         );
      }

      var idx;
      function pop(formName,i) {
         idx=i;
         openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectARAccountDet);
      }

      function selectARAccountDet(o) {
         if (o!=null) {
            docEl('details.['+idx+'].stGLAccountID').value=o.acid;
            docEl('details.['+idx+'].stGLAccountDesc').value=o.acdesc;
         }
      }
   </script>

</c:frame>
<script>
   function selectCustomer(i) {
      openDialog('entity_search.crux', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('invoice.stEntityID').value=o.stEntityID;
               document.getElementById('invoice.stEntityName').value=o.stEntityName;
            }
         }
      );
   }
</script>