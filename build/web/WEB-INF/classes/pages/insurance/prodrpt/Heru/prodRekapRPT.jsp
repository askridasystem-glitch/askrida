<%@ taglib prefix="c" uri="crux" %><c:frame>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>Period</td>
                  <td>:</td>
                  <td>
                     <c:field name="dtPeriodFrom" type="date" caption="From" /> To
                     <c:field name="dtPeriodTo" type="date" caption="To" />
                  </td>
               </tr>
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