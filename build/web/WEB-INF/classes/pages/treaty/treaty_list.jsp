<%@ page import="com.webfin.master.treaty.form.TreatyListForm,
                 com.webfin.insurance.model.InsuranceTreatyView,
                 com.webfin.insurance.model.InsuranceTreatyDetailView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   TreatyListForm form = (TreatyListForm) request.getAttribute("FORM");
   
   boolean treaty = form.isEnableSuperEdit();
%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
      </td>
   </tr>
   <tr>
      <td>
         <c:listbox name="list" paging="true" selectable="true" >
            <c:listcol name="stInsuranceTreatyID" title="ID" selectid="treatyid" />
            <c:listcol name="stTreatyClass" title="Class" />
            <c:listcol name="stTreatyName" title="Name" sortable="true" filterfield="treaty_name"  />
            <c:listcol name="stProportionalFlag" title="Prop" />
            <c:listcol name="stRetrocessionFlag" title="Retro" />
            <c:listcol name="dtTreatyPeriodStart" title="Active" />
            <c:listcol name="dtTreatyPeriodEnd" title="Expire" />
            <c:listcol name="dbTreatyPriority" title="Priority" />
            <c:listcol name="stActiveFlag" title="Active" flag="true" />
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
