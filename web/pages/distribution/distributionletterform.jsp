<%@ page import="com.crux.distribution.form.DistributionForm,
                  com.crux.distribution.model.DistributionRoleView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Distribution" >
<%
   DistributionForm form = (DistributionForm)frame.getForm();

   final boolean isNew = form.getUser().isNew();

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
              
            <c:field hidden="true" name="userIndex"/>
            <c:field width="200" name="user.stIdDist" mandatory="true" caption="Distribution ID" type="string" presentation="standard" />
            <c:field clientchangeaction="selectCustomer2()"  name="user.stUserID" type="string" width="200" popuplov="true"  lov="LOV_Profil" mandatory="true" caption="User ID" presentation="standard" />
            <c:field width="200" name="user.stDistName" mandatory="true" caption="Distribution Name" type="string" presentation="standard" />

            
              
             <%--  
            <c:listbox name="user.letterdist" >
            <%
               IncomingRoleView usr = (IncomingRoleView) current;
            %>
               <c:listcol title="" columnClass="header" ><c:button enabled="true" text="+" event="addRole"/></c:listcol>
               <c:listcol title="" columnClass="detail" ><c:button enabled="true" text="-" event="deleteRole" clientEvent="f.userIndex.value='$index$'"/></c:listcol>
               <c:listcol title="ID Distribusi" >
                  <c:field name="user.letterdist.[$index$].stIdDist" width="200" lov="LOV_Dist" readonly="<%=!usr.isNew()%>" type="string" caption="ID Distribusi" mandatory="true" />
               </c:listcol>
            </c:listbox>
           --%>
         </table>
      </td>
   </tr>
   <tr>
      <td align=center>
         <c:button text="Submit"  event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
         <c:button text="Cancel"  event="doCancel" show="<%=!form.isReadOnly()%>"/>
         <c:button text="Back"  event="doCancel" show="<%=form.isReadOnly()%>"/>
      </td>
   </tr>
</table>
</c:frame>
<script>
	function selectCustomer2() {
      var o= window.lovPopResult;
      document.getElementById('user.stUserID').value=o.value;
      
  	 }
</script>