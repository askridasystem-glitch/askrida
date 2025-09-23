<%@ page import="com.crux.util.JSPUtil,
com.crux.common.controller.Helper,
com.webfin.gl.model.GLCostCenterView,
com.crux.common.parameter.Parameter,
com.crux.util.DTOList,
com.crux.util.DateUtil,
java.util.Date,
com.webfin.insurance.model.InsurancePolicyTypeView,
com.crux.login.model.UserSessionView"%><% final JSPUtil jspUtil = new JSPUtil(request, response,"WELCOME"); %><html>
<%

final UserSessionView uv = (UserSessionView)Helper.getUserSession(request);

final String encoding = request.getHeader("accept-encoding");

final boolean compressionEnabled = (encoding != null) && (encoding.indexOf("gzip")>=0);

final DTOList branch = (DTOList) uv.getBranch();

final DTOList policy = (DTOList) uv.getPolicy();

String isi =  Parameter.readString("BROADCAST_MESSAGE")!=null?Parameter.readString("BROADCAST_MESSAGE"):"";

int sisaHari = DateUtil.getDaysAmount(new Date(),uv.getDtInActiveDate());

%>

<%if(isi!=null){%>
    <table>
    <tr>
        <td colspan="4">
            <br>
            <br>
             {L-ENG Welcome To-L}{L-INA Selamat Datang-L}  <strong><%=uv.getStUserName()%></strong>, password akan expired pada <strong><%=jspUtil.print(uv.getDtInActiveDate())%> (<%=sisaHari%> Hari)</strong>, segera ubah password sebelum expired
            <br>
        </td>
        <td></td>
    </tr>
     <tr>
            <td colspan="4">
                <br>
                 <strong>NOTIFICATION</strong>
                <br>
            </td>
            <td></td>
        </tr>

        <tr>
            <td colspan="4">
                <br>
                 <font style="font-family:tahoma; font-weight:bold; font-size:13px; color:red"><%=isi%></font>
                <br>
                <br>
            </td>
            <td></td>
        </tr>
     </table>
<%}%>
<table >
    
    <tr>
        <td valign="top">
            <table cellpadding=2 cellspacing=1 >      
                <tr class=header><td colspan="4" align="center">DAFTAR DAERAH</td></tr>
                <tr class=header>
                    <td>No</td>
                    <td>Kode</td>
                    <td>Daerah</td>
                    <td>Tanggal Mulai</td>
                </tr>
                <%
                for(int i = 0; i < branch.size(); i++){
    GLCostCenterView cc = (GLCostCenterView) branch.get(i);
                %>        
                <tr class=row<%=i%2%>>
                    <td><%=jspUtil.print(String.valueOf(i+1))%></td>
                    <td><%=jspUtil.print(cc.getStCostCenterCode())%></td>
                    <td><%=jspUtil.print(cc.getStDescription())%></td>
                    <td><%if(!cc.getStCostCenterCode().equalsIgnoreCase("00")){%><%=jspUtil.print(cc.getDtValidDate())%><%}%></td>
                </tr>
                <%    
                }
                %>
                
                
            </table>
        </td>
        <td>
            <table cellpadding=2 cellspacing=1 >
                <tr class=header><td colspan="4" align="center">JENIS ASURANSI</td></tr>
                <tr class=header>
                    <td>No</td>
                    <td>Kode</td>
                    <td>Jenis</td>
                    <td>Grup</td>
                </tr>
                <%
                for(int j = 0; j < policy.size(); j++){
                InsurancePolicyTypeView pol = (InsurancePolicyTypeView) policy.get(j);
                %>        
                <tr class=row<%=j%2%>>
                    <td><%=jspUtil.print(String.valueOf(j+1))%></td>
                    <td><%=jspUtil.print(pol.getStPolicyTypeID())%></td>
                    <td><%=jspUtil.print(pol.getStDescription())%></td>
                    <td><%=jspUtil.print(pol.getStGroupName())%></td>
                </tr>
                <%    
                }
                %>
                
                
            </table>
        </td>
    </tr>
</table>
<%=jspUtil.release()%>


