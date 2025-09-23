<%@ taglib prefix="c" uri="crux" %><c:frame>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field name="sql" caption="SQL" rows="40" width="500" type="string" presentation="standadr" />
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Execute" event="execute" confirm="Are you sure ?" />
      </td>
   </tr>
</table>
</c:frame>