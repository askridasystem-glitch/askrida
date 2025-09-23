<%@ page import="com.crux.util.JSPUtil"%><html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>

<frameset rows="*" cols="*" frameborder="yes" border="1" framespacing="0">

    <frameset id="frmMain" rows="*,1" cols="*" frameborder="NO" border="1" framespacing="0">
        <frame name="basefrm" src="<%=jspUtil.getControllerURL("policy_list_reas_monitoring.reinsurancemonitoring.crux")%>">
        
    </frameset>
    
  </frameset>
  
	</frameset>
</frameset>

<script language=JavaScript1.2>
   
</script>

</html>


