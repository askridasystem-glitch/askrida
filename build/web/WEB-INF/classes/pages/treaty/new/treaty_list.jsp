<%@ page import="com.webfin.master.treaty.form.TreatyListForm,
                 com.webfin.insurance.model.InsuranceTreatyView,
                 com.webfin.insurance.model.InsuranceTreatyDetailView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   TreatyListForm form = (TreatyListForm) request.getAttribute("FORM");
   
   boolean treaty = form.isEnableSuperEdit();

   boolean isEnableApprove1 = form.isEnableApproval1();
   boolean isEnableApprove2 = form.isEnableApproval2();
   boolean isEnableApprove3 = form.isEnableApproval3();
   boolean isEnableApprove4 = form.isEnableApproval4();
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
            <c:listcol name="stApprovedFlag1" title="Operator" flag="true" />
            <c:listcol name="stApprovedFlag2" title="Kepala<br>Bagian" flag="true" />
            <c:listcol name="stApprovedFlag3" title="Kepala<br>Divisi" flag="true" />
            <c:listcol name="stApprovedFlag4" title="Aktivasi<br>TI" flag="true" />
            <c:listcol name="stActiveFlag" title="Active" flag="true" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>     
         <c:button text="Create" event="clickCreate" />
         <c:button text="Edit" event="clickEdit" />
         <c:button text="View" event="clickView" />
         <c:button show="<%=isEnableApprove1%>" text="Setujui Operator" event="clickApprove1" />
         <c:button show="<%=isEnableApprove2%>" text="Setujui Kepala Bagian" event="clickApprove2" />
         <c:button show="<%=isEnableApprove3%>" text="Setujui Kepala Divisi" event="clickApprove3" />
         <c:button show="<%=isEnableApprove4%>" text="Aktivasi TI" event="clickApprove4" />
      </td>
   </tr>
</table>

</c:frame>
