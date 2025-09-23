<%@ page import="com.webfin.ar.forms.ReceiptListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Print Pengajuan" >

<%--  
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>

         </td>
      </tr>
     
      <tr>
         <td>
         
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="{L-ENGSearch-L}{L-INACari-L}" event="clickCreate2" />
        
      </td>
   </tr>

</table>--%>
<script>
        f.action_event.value='clickCreateClaim';
        f.submit();
</script>

</c:frame>