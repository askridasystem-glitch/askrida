<%@ page import="com.webfin.master.capacity.CapacityMasterForm,
         com.crux.util.DTOList,
         com.webfin.insurance.model.InsuranceRatesBigView,
         com.crux.ff.model.FlexTableView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <% 
                CapacityMasterForm f = (CapacityMasterForm) frame.getForm();

                final boolean ro = f.isReadOnly();

                String mode = f.getMode();

                String aptype = f.getRef2();

                final boolean canEditCapacityApproval = f.isEnableEditApprovalTSILimit();

                final boolean canEditCapacityComission = f.isEnableEditApprovalComissionLimit();

                final boolean canEditCapacityComissionGroup = f.isEnableEditApprovalComissionGroupLimit();

                final boolean canEditCapacityClaimApproval = f.isEnableEditApprovalClaimLimit();

                final boolean isApprovalFlag1Mode = f.isIsApprovalFlag1Mode();

                final boolean isApprovalFlag2Mode = f.isIsApprovalFlag2Mode();

                final boolean isApprovalFlag3Mode = f.isIsApprovalFlag3Mode();

                final boolean canApproveTSI1 = f.isEnableApprovalTSI1();

                final boolean canApproveTSI2 = f.isEnableApprovalTSI2();

                final boolean canApproveTSI3 = f.isEnableApprovalTSI3();

                final boolean isCreateMode = f.isCreateMode(); 
 
                final boolean isEditMode = f.isEditMode();

                final boolean isShowButton = f.isShowButton();

    %>
    

    <c:evaluate when="<%="warranty".equalsIgnoreCase(mode)%>" >
        <table cellpadding=2 cellspacing=1>
            <tr>

                <td>
                    <table cellpadding=2 cellspacing=1>
                        <c:field name="ref2" caption="{L-ENGBranch-L}{L-INACabang-L}" width="200" lov="LOV_CostCenter" type="string" presentation="standard" readonly="<%=f.getRef2() != null%>" />
                        

                        <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                          caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                         name="stPolicyTypeGroupID" type="string" presentation="standard"/>

                         <c:field width="300" include="<%=f.getStPolicyTypeGroupID()!=null%>"
                                  lov="LOV_PolicyType"
                                 caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" type="string"
                                  presentation="standard">
                            <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false"/>
                        </c:field>

                        <c:evaluate when="<%=f.getList() != null%>" >
                            <tr>
                                    <td>
                                        <c:button text="Refresh" validate="true" event="selectWarranty"/>
                                    </td>
                            </tr>
                        </c:evaluate>
                        

                        <table cellpadding=2 cellspacing=1>
                            <c:evaluate when="<%=isShowButton%>">
                                <tr>
                                    <td>
                                        <c:button text="View" validate="true" event="selectWarranty" />
                                    </td>
                                    <c:evaluate when="<%=!isEditMode%>">
                                        <td>
                                            <c:button text="Edit/Add" validate="true" event="selectWarranty" />
                                        </td>
                                    </c:evaluate>
                                </tr>
                            </c:evaluate>
                        </table>

                    </table>
                </td>
            </tr>
            <c:evaluate when="<%=f.getList() != null%>" >

                <tr>
                    <td>
                        <c:listbox name="list">
                            <%
                                        final InsuranceRatesBigView rates = (InsuranceRatesBigView) current;
                            %>
                            
                                <c:listcol title="" columnClass="header">
                                    <c:button text="+" event="addLineWarranty" validate="false" defaultRO="true"/>
                                </c:listcol>
                                <c:listcol title="" columnClass="detail">
                                    <c:button text="-" event="onDeleteDetail"
                                              clientEvent="f.detailIndex.value='$index$';" validate="false" />
                                </c:listcol>

                                <c:listcol title="Jenis Asuransi" >
                                    <c:field name="list.[$index$].stReference3" mandatory="true" lov="LOV_PolicyTypeSearch" popuplov="true" caption="Kode" type="string" width="150" />
                                </c:listcol>
                                <c:listcol title="Grup Sumber Bisnis" >
                                    <c:field name="list.[$index$].stReference4" caption="{L-ENGCompany Group-L}{L-INAGroup Perusahaan-L}" width="200" popuplov="true" lov="LOV_COMPANYGROUP" type="string" />
                                </c:listcol>

                                <c:listcol title="Jenis Kredit" >
                                    <c:field name="list.[$index$].stReference1" caption="Jenis Kredit" width="200" lov="LOV_TypeOfCredit" type="string" >
                                             <c:lovLink name="cc_code" link="ref2" clientLink="false"/>
                                    </c:field>
                                </c:listcol>
                                <c:listcol title="Pemasar" >
                                    <c:field name="list.[$index$].stReference5" lov="LOV_Entity" popuplov="true" caption="Pemasar" type="string" width="250" />
                                </c:listcol>
                               <c:listcol title="Keterangan" >
                                   <c:field name="list.[$index$].stNotes" mandatory="true" rows="3" caption="Kode" type="string" width="400" />
                                </c:listcol>

                                <c:listcol title="Period Start" >
                                    <c:field name="list.[$index$].dtPeriodStart" mandatory="true" caption="Period Start" type="date" readonly="false"  />
                                </c:listcol>
                               
                            
                            <c:listcol title="Period End" >
                                    <c:field name="list.[$index$].dtPeriodEnd" mandatory="true" caption="Period End" type="date" readonly="false"  />
                            </c:listcol>
                            <c:listcol title="Active" >
                                    <c:field name="list.[$index$].stActiveFlag" caption="Active Flag" type="check" readonly="false"  />
                                </c:listcol>

                            <c:listcol title="Dokumen" >
                                    <c:field name="list.[$index$].stFileID" caption="Dokumen" type="file" thumbnail="true" readonly="false"  />
                                </c:listcol>
                            <c:listcol title="Last Changed" >
                                    <c:field name="list.[$index$].dtChangeDate" caption="Last Changed" type="date" readonly="true"  />
                                </c:listcol>
                        </c:listbox>
                    </td>
                </tr>
                <tr>
                    <td>

                        <c:evaluate when="<%=isEditMode%>" >
                            <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="saveApprovalKonsumtif" />
                        </c:evaluate>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                        <%--<c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />--%>
                    </td>
                </tr>
            </c:evaluate>
        </table>
    </c:evaluate>

    

    

</c:frame>
<script>
    
    function refresh() {
        f.action_event.value = 'refresh';
        f.submit();
    }
    
    function switchRate(i) {
        try {
            var add = docEl('list.[' + i + '].stAddFlag').checked;
            

            VM_switchReadOnly('list.[' + i + '].dbReference2', add);
            VM_switchReadOnly('list.[' + i + '].dtPeriodEnd', add);
            VM_switchReadOnly('list.[' + i + '].dbReference1', add);
           
        } catch(e) {
        }
    }
    
    function switchTes(i) {
        try {
            var add = docEl('list.[' + i + '].stAddFlag').checked;
            

            VM_switchReadOnly('list.[' + i + '].dbReference2', add);
           
        } catch(e) {
        }
    }
    
    function displayCronHelp() {
        window.open('<%= jspUtil.getPagesPath()%>' + '/capacity/help.html', 'Help','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=640,height=480');
    }
</script>