<%@ taglib prefix="c" uri="crux" %><c:frame title="Technical Account Statement" >
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field name="treatytype" type="string" width="400" loveall="true"  caption="Treaty Type" lov="LOV_TreatyType" presentation="standard" />
               
               <%--  
               <c:field name="policydatefrom" type="date" caption="Policy Date From" presentation="standard" />
               <c:field name="policydateto" type="date" caption="Policy Date To" presentation="standard" />
               --%>
               <c:field name="periodFrom" type="string" descfield="period_desc" caption="Bulan" lov="LOV_MONTH_Period" presentation="standard" />
               <c:field name="yearFrom" type="string" caption="Tahun" lov="LOV_GL_Years" presentation="standard"/>
               <c:field name="entity" type="string" descfield="entity_desc" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Entity" presentation="standard" />
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