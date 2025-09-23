<%@ page import="com.webfin.insurance.model.InsurancePolicyObjDefaultView,
				com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="VEHICLE SEARCH" >

<%
   final PolicyListForm form = (PolicyListForm) frame.getForm();
   
   final boolean canNavigateBranch = form.isKreasiNavigateBranch();

%>

   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
             <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="stPolicy" type="string" caption="{L-ENGPolicy No.-L}{L-INANo. Polis-L}" presentation="standard" width="200" />
                        <c:field name="stPoliceNo" type="string" caption="{L-ENGPolice No-L}{L-INANomor Polisi-L}" presentation="standard" width="200" />
                        <c:field name="stYearBuilt" type="string" caption="{L-ENGYear of Built-L}{L-INATahun Pembuatan-L}" presentation="standard" width="200" />
                        <c:field name="stChassisNo" type="string" caption="{L-ENGChassis No-L}{L-INANo Rangka-L}" presentation="standard" width="200" />
                        <c:field name="stMachineNo" type="string" caption="{L-ENGMachine No-L}{L-INANomor Mesin-L}" presentation="standard" width="200" />
                       	<c:field name="stPolicyLevel" type="string" caption="Level" lov="LOV_PolicyLevel" presentation="standard" width="200" />
                     
                     </table>
                  </td>
                   <td>
                     <table cellpadding=2 cellspacing=1>
                        <tr>
                            <c:field name="stUsage" type="string" caption="{L-ENGUsage-L}{L-INAPenggunaan-L}" presentation="standard" width="200" />
                            <c:field name="stTypeOfVehicle" type="string" caption="{L-ENGType-L}{L-INAJenis Kendaraan-L}" presentation="standard" width="200" />
                             <c:field name="stName" type="string" caption="{L-ENGName-L}{L-INANama-L}" presentation="standard" width="200" />
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="btnRefresh" />
                        </tr>
                     </table>
                  </td>
                  <td><c:button text="Refresh" event="btnRefreshVehicle" />

                            <c:button text="Print Excel" event="EXCEL_VEHICLE" />
                  </td>
               </tr>
            </table>
            
         </td>
      </tr>
     
      <tr>
         <td>
         
         
         <c:listbox name="vehicleList" paging="true" selectable="true" view="com.webfin.insurance.model.InsurancePolicyObjDefaultView">
            <c:listcol name="stPolisNo" title="{L-ENGPolicy No-L}{L-INANo Polis-L}" />
            <c:listcol name="stReference1" title="{L-ENGPolice No-L}{L-INANomor Polisi-L}" />
            <c:listcol name="stReference3" title="{L-ENGYear of Built-L}{L-INATahun Pembuatan-L}" />
            <c:listcol name="stReference4" title="{L-ENGChassis No-L}{L-INANo Rangka-L}" />
            <c:listcol name="stReference5" title="{L-ENGMachine No-L}{L-INANomor Mesin-L}" />
            <c:listcol name="stReference7" title="{L-ENGUsage-L}{L-INAPenggunaan-L}" />
            <c:listcol name="stReference9" title="{L-ENGName-L}{L-INANama-L}" />
         </c:listbox>
      </td>
   </tr>
     
</table>



</c:frame>