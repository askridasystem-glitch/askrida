<%@ taglib prefix="c" uri="crux" %><c:frame title="Transaction Detail Report" >
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field name="trxtype" type="string" width="400" loveall="true"  caption="Trx Type" lov="LOV_ARTrxType" presentation="standard" />
               <c:field name="policyno" type="string" caption="Policy No" presentation="standard" />
               <c:field name="policydatefrom" type="date" caption="Policy Date From" presentation="standard" />
               <c:field name="policydateto" type="date" caption="Policy Date To" presentation="standard" />
               <c:field name="customer" type="string" descfield="customer_desc" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Customer" presentation="standard" />
               <c:field name="entity" type="string" descfield="entity_desc" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Entity" presentation="standard" />
               <c:field name="account" type="string" caption="Account" presentation="standard" />
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:button event="print" text="Print" />
         </td>
      </tr>
   </table>
</c:frame>