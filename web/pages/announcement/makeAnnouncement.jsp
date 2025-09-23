<%@ page import="com.crux.util.JSPUtil,
                com.crux.common.parameter.Parameter"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
<table>
  <tr>
    <td >{L-ENG Message-L}{L-INA Pesan-L}</td>
    <td >:</td>
    <td ><c:field name="stAnnouncement" caption="Message" rows="5" width="500" type="string" presentation="standad" />
    </td> 
  </tr>
  <tr>
    <td colspan="3"><c:button text="Broadcast" event="execute" confirm="Are you sure ?" />
         <c:button text="Clear Broadcast" event="clear" confirm="Are you sure ?" />
    </tr>
</table>
</c:frame>
<script>
	function selectCustomer2() {
      var o= window.lovPopResult;
      document.getElementById('stUserID').value=o.value;
      
  	 }
  	 
  	 function tes(){
  	 	//f.EVENT.value='FOOTER';
  	 	//f.submit();
  	 	 window.Location.Reload(true);
  	 }
  	 

  	 
</script>