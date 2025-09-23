<%@ taglib prefix="c" uri="crux" %><c:frame title="BLOCK RISK LIST" >

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
      </td>
   </tr>
   <tr>
      <td>
         <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceZoneLimitView" >
            <c:listcol name="stZoneID" title="" selectid="zoneid" />
            <c:listcol filterable="true" name="stZoneCode" title="Zone Code"/>
            <c:listcol filterable="true" name="stDescription" title="Description" />
            <c:listcol filterable="true" name="dbLimit1" title="Limit"/>
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Create" event="clickCreate" />
         <c:button text="Edit" event="clickEdit" />
         <c:button text="View" event="clickView" />
      </td>
   </tr>
</table>

</c:frame>
