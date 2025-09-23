<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.LOV,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.postalcode.model.PostalCodeView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%

	final PostalCodeView jh = (PostalCodeView)request.getAttribute("JH");
        final LOV kabupaten = (LOV) request.getAttribute("kabupaten");
	
%>
</head>
<body>
<form name=f action="ctl.ctl" method=POST>
<input type=hidden name=EVENT value="POSTCODE_SAVE">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("{L-ENGCREATE POST CODE-L}{L-INAINPUT KODE POS-L}")%>
<tr>
	<td width="30%"> <br></td>
</tr>

<tr><td colspan=2>
<table cellpadding=1 cellspacing=1>
<tr>
   <td width=500 colspan=4></td>
</tr>
   <tr><td>{L-ENGKabupaten-L}{L-INAKabupaten-L}</td><td>:</td><td><%=jspUtil.getInputSelect("kabupaten|Kabupaten|string",null,kabupaten, 200, JSPUtil.MANDATORY|JSPUtil.NOTEXTMODE)%></td></tr>
   <tr><td>{L-ENGPost Code-L}{L-INAKode Pos-L}</td><td>:</td><td><%=jspUtil.getInputText("postcode",new FieldValidator("","Post Code","string",64),jh.getStPostalCode(), 100, JSPUtil.MANDATORY)%></td></tr>
   <tr><td>{L-ENGCity-L}{L-INANama Kota-L}</td><td>:</td><td><%=jspUtil.getInputText("cityname",new FieldValidator("","City Name","string",64),jh.getStCityName(), 200, JSPUtil.MANDATORY)%></td></tr>
   <tr><td>{L-ENGStreet-L}{L-INANama Jalan-L}</td><td>:</td><td><%=jspUtil.getInputTextArea("regionname",new FieldValidator("","Region Name","string",128),jh.getStRegionName(), 4,4,200, JSPUtil.MANDATORY)%></td></tr>
   <tr><td>{L-ENGBuilding-L}{L-INANama Bangunan-L}</td><td>:</td><td><%=jspUtil.getInputText("buildingdesc",new FieldValidator("","Building Name","string",64),jh.getStBuildingDescription(), 200)%></td></tr>
   <tr><td></td><td></td>
   		
   		<td>
      	<%=jspUtil.getButtonSubmit("bsave","SAVE","window.close()") %> 
		
		
      	<%=jspUtil.getButtonNormal("bc","Cancel","window.close()") %>
		</td>

    </tr>
          
</table>
</td></tr>
</table>

</form>
</body>
<script>

</script>

</html>

