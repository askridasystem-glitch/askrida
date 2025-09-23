<%@ page import="com.webfin.ar.forms.ReceiptListForm,
                com.webfin.ar.model.ARReceiptView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >

<%

   ReceiptListForm form  =(ReceiptListForm) request.getAttribute("FORM");

%>

   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="description" type="string" caption="Description" presentation="standard" width="200" />
                        <c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" />
                     </table>
                  </td>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <tr>
                           <c:field name="receiptNo" type="string" caption="Receipt No" presentation="standard" width="200" />
                           <%--<c:field name="transdate" type="date" caption="Date" presentation="standard" width="200" />--%>
                           <c:field name="rcpDateFrom" type="date" caption="Date From" presentation="standard" width="200" />
                           <c:field name="rcpDateTo" type="date" caption="Date To" presentation="standard" width="200" />
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
         <c:listbox name="list" selectable="true" >
            <c:listcol name="stARReceiptID" title="ID" selectid="receiptID"/>
            <c:listcol name="stPostedFlag" title="Eff" flag="true" />
            <c:listcol name="stReceiptNo" title="Receipt No" />
            <c:listcol name="stStatus" title="Status" />
            <c:listcol name="dtReceiptDate" title="Receipt Date" />
            <c:listcol name="stCurrencyCode" title="Ccy" />
            <c:listcol name="stCostCenterCode" title="Branch" />
            <c:listcol name="stShortDescription" title="Description" />
            <c:listcol name="dbEnteredAmount" title="Amount" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="Create" event="clickCreate" />
         <c:button  text="Edit" event="clickEdit" />
         <c:button  text="View" event="clickView" />
         <c:button  show="<%=form.isEnableSuperEdit()%>" text="Super Edit" event="clickSuperEdit" />
         <c:button  text="Approve" event="clickApprove" />
      </td>
   </tr>
     <tr>
         <td>
          Print Penerimaan Premi : in <c:field name="printLang" lov="LOV_LANG" type="string" /><c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
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
         frmx.src='x.fpc?EVENT=AR_RECEIPT_PRT&receiptid='+f.receiptID.value+'&xlang='+f.printLang.value+'&antic='+(new Date().getTime());
         return;
      }


   }

</script>