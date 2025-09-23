<%@ page import="com.crux.web.form.Form,
                 com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %>
<%
   final Form formx = (Form)request.getAttribute("FORM");

   //boolean hasNavigateBranch = SessionManager.getInstance().hasResource("ARINVOICE_NAVBR");

%>
<c:frame title="SURAT HUTANG" >
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field name="customer"  caption="Customer" type="string" presentation="standard" width="200" />
            <c:field name="nosurathutang2"  caption="No Surat Hutang" type="string" presentation="standard" width="200" />
            <c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" changeaction="btnSearch" readonly="false" />
         	<c:field name="treatytype" type="string" caption="Treaty" lov="LOV_TreatyType" presentation="standard" width="200" changeaction="btnSearch" readonly="false" />
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Search" event="btnSearch" />
      </td>
   </tr>
   <tr>
      <td>
      	<c:field name="nosurathutang" hidden="true"/>
         <c:listbox name="list2" selectable="true" paging="true">
            <c:listcol name="stNoSuratHutang" title="ID" selectid="nosurathutang"/>
            <c:listcol name="stNoSuratHutang" title="No Surat Hutang" />
            <c:listcol name="stEntityName" title="Customer" />
            <c:listcol name="dbTagihanAmount" title="Amount" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Create" event="clickCreateSuratHutang" />
         <c:button text="Edit" event="clickEditSuratHutang" />
         <c:button text="View" event="clickViewSuratHutang" />
      </td>
   </tr>
</table>
</c:frame>