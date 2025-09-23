<%@ page import="com.webfin.master.zone.form.ZoneMasterForm,
                 com.webfin.insurance.model.InsuranceZoneLimitView"%>
<%@ taglib prefix="c" uri="crux" %>
<c:frame>
<%
   ZoneMasterForm form = (ZoneMasterForm) request.getAttribute("FORM");
%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="200" readonly="true" caption="Zone ID" name="zone.stZoneID" type="string" mandatory="true" presentation="standard"/>
            <c:field width="200" readonly="false" caption="Zone Code" name="zone.stZoneCode" type="string" mandatory="true" presentation="standard"/>
            <c:field width="300" rows="3" readonly="false" caption="Description" name="zone.stDescription" type="string" mandatory="true" presentation="standard"/>
    		<c:field width="200" readonly="false" caption="Limit" name="zone.dbLimit1" type="money5.0" mandatory="true" presentation="standard"/>
         </table>
      </td>
   </tr>
      <tr>
         <td align=center>
            <c:button text="Save" event="save" />
            <c:button text="Cancel" event="close" />
         </td>
      </tr>
</table>
</c:frame>
