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
    <c:evaluate when="<%="approvalkonsumtif".equalsIgnoreCase(mode)%>" >
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table>
                                    <c:field name="ref2" caption="{L-ENGBranch-L}{L-INACabang-L}" width="200" lov="LOV_CostCenter" type="string" presentation="standard" readonly="<%=f.getRef1() != null%>" changeaction="select_comission_group" />
                                    <c:field name="dtPeriodEnd" caption="Tanggal Akhir" width="200"  type="date" presentation="standard" readonly="<%=f.getRef1() != null%>" />
                                    <c:field name="stFileID" caption="File Excel" width="200"  type="file" thumbnail="true"  presentation="standard"  />
                                </table>
                                 
                            </td>
                            <td>
                                <table>
                                    <c:field name="stGrupSumbis" caption="Grup Sumbis" width="200" lov="LOV_COMPANYGROUP" popuplov="true" type="string" presentation="standard" />
                                    <c:field name="ref4" caption="Pekerjaan" width="200" lov="VS_INSOBJ_PEKERJAAN_DEBITUR" type="string" presentation="standard" />
                                    <c:field name="stUsia" caption="Usia" width="200" type="string" presentation="standard" />
                                    
                                </table>
                                
                            </td>
                        </tr>

                        <table cellpadding=2 cellspacing=1>
                            <c:evaluate when="<%=f.getRef2() != null%>">
                                <tr>
                                    <td>
                                        <c:button text="Buat" validate="true" event="createApprovalKonsumtif" />
                                    </td>
                                    <td>
                                        <c:button text="Lihat" validate="true" event="view_approval" />
                                    </td>

                                        <td>
                                            <c:button text="Ubah" validate="true" event="selectApprovalKonsumtif" />
                                        </td>
                                        <td>
                                            <c:button text="Konversi" validate="true" event="uploadExcel" />
                                        </td>


                                </tr>
                            </c:evaluate>
                        </table>
                    </table>
                </td>
            </tr>
            <c:evaluate when="<%=f.getList() != null%>" >

                    <tr>
                        <td>

                            <c:listbox name="list" paging="true" selectable="true" >
                                <%
                                    final InsuranceRatesBigView rates = (InsuranceRatesBigView) current;
                                %>

                                    <c:listcol title="" columnClass="header">
                                        <c:button text="+" event="addLineApprovalKonsumtif" validate="false" defaultRO="true"/>
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail">

                                    </c:listcol>
                                    <c:listcol title="Keterangan" >
                                        <c:field name="list.[$index$].stDescription"  caption="Kode" type="string" width="200" />
                                    </c:listcol>
                                    
                                    <c:listcol title="Grup Sumbis" >
                                        <c:field name="list.[$index$].stReference1" lov="LOV_COMPANYGROUP" popuplov="true" caption="Kode" type="string" width="200" />
                                    </c:listcol>
                                    <c:listcol title="Pekerjaan" >
                                        <c:field name="list.[$index$].stReference4"  lov="VS_INSOBJ_PEKERJAAN_DEBITUR" caption="Kode" type="string" width="200" />
                                    </c:listcol>
                                    <c:listcol title="Usia" >
                                        <c:field name="list.[$index$].stReference3"  caption="Kode" type="string" width="40" />
                                    </c:listcol>
                                    <c:listcol title="s/d Usia" >
                                        <c:field name="list.[$index$].stReference6"  caption="Kode" type="string" width="40" />
                                    </c:listcol>
                                    <c:listcol title="Auto Cover" >
                                        <c:field name="list.[$index$].stReference5"  caption="Kode" type="check" width="200" />
                                    </c:listcol>
                                    <c:listcol title="TSI" >
                                        <c:field name="list.[$index$].dbRate0" caption="Limit1" type="money16.2" width="120" />
                                    </c:listcol>
                                    <c:listcol title="TSI s/d" >
                                        <c:field name="list.[$index$].dbRate1" caption="Limit1" type="money16.2" width="120" />
                                    </c:listcol>
                                    <c:listcol title="Dokumen Kesehatan" >
                                        <c:field name="list.[$index$].stNotes"  caption="Kode" type="string" width="200" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Awal" >
                                        <c:field name="list.[$index$].dtPeriodStart"  caption="Kode" type="date" width="200" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Akhir" >
                                        <c:field name="list.[$index$].dtPeriodEnd"  caption="Kode" type="date" width="200" />
                                    </c:listcol>
                                <c:listcol title="Active" >
                                        <c:field name="list.[$index$].stActiveFlag"  caption="Kode" type="check" width="200" />
                                    </c:listcol>
                                    <c:listcol title="" >
                                        <c:button text="Apply All" validate="false" event="applyAll" />
                                    </c:listcol>

                            </c:listbox>


                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:evaluate when="<%=canEditCapacityApproval && isEditMode%>" >
                                <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="saveApprovalKonsumtif" />
                            </c:evaluate>
                            <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />

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