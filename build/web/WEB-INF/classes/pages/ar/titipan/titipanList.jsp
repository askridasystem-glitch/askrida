<%@ page import="com.webfin.ar.forms.TitipanListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TITIPAN PREMI" >

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
                  	<%-- 
                     <table cellpadding=2 cellspacing=1>
                        <tr>
                           <c:field name="receiptNo" type="string" caption="Receipt No" presentation="standard" width="200" />
                           <c:field name="rcpDateFrom" type="date" caption="Date From" presentation="standard" width="200" />
                           <c:field name="rcpDateTo" type="date" caption="Date To" presentation="standard" width="200" />
                        </tr>
                     </table>
                      --%>
                  </td>
                  <td><c:button text="Refresh" event="btnSearch" /></td>
               </tr>
            </table>
         </td>
      </tr>
     
      <tr>
         <td>
         
         <c:field name="titipanID" hidden="true"/>
         <c:listbox name="list" selectable="true" >
            <c:listcol name="stARTitipanID" title="ID" selectid="titipanID"/>
            <c:listcol name="stTransactionNo" title="Transaction No" />
            <c:listcol name="stAccountNo" title="Account No" />
            <c:listcol name="stDescription" title="Description" />
            <c:listcol name="stCurrencyCode" title="Ccy" />
            <c:listcol name="dbBalance" title="Balance" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
           <c:button  text="Create" event="clickCreate" />
         <c:button  text="Edit" event="clickEdit" />
         <c:button  text="View" event="clickEdit" />
         <%--  <c:button  text="Super Edit" event="clickSuperEdit" />
         <c:button  text="Approve" event="clickApprove" />--%>
      </td>
   </tr>
     <%--  <tr>
         <td>
            Print <c:field name="printingLOV" width="150" type="string" lov="LOV_RECEIPT_PRINTING" ></c:field> in <c:field name="printLang" type="string" lov="LOV_LANG" />
            <c:button text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
         </td>
      </tr>--%>
</table>
</c:frame>