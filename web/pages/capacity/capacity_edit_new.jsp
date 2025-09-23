<%@ page import="com.webfin.master.capacity.CapacityMasterForm,
         com.crux.util.DTOList,
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

                final boolean canApproveTSI4 = f.isEnableApprovalTSI4();

                final boolean isCreateMode = f.isCreateMode();

                final boolean isEditMode = f.isEditMode();

                final boolean isShowButton = f.isShowButton();

                final boolean isApprovalMode = f.isApprovalMode();

                final boolean canApproveKomisi1 = f.isEnableApprovalKomisi1();

                final boolean canApproveKomisi2 = f.isEnableApprovalKomisi2();

                final boolean canApproveKomisi3 = f.isEnableApprovalKomisi3();

                final boolean canApproveKomisi4 = f.isEnableApprovalKomisi4();

                final boolean canApproveClaim1 = f.isEnableApprovalClaim1();

                final boolean canApproveClaim2 = f.isEnableApprovalClaim2();

                final boolean canApproveClaim3 = f.isEnableApprovalClaim3();

                final boolean canApproveClaim4 = f.isEnableApprovalClaim4();

    %>
    <c:evaluate when="<%="approval".equalsIgnoreCase(mode)%>" >
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <c:field name="ref2" changeaction="selectType" caption="{L-ENGApproval Type-L}{L-INAJenis Persetujuan-L}" width="200" lov="VS_CAPA_TYPE" type="string" presentation="standard" readonly="<%=f.getRef2() != null%>" />
                        <c:field name="ref1" caption="Role" width="350" lov="LOV_Role" type="string" popuplov="true" presentation="standard" readonly="<%=f.getRef1() != null%>" changeaction="select_approval_tsi" />


                        <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                                 caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                                 name="stPolicyTypeGroupID" type="string" presentation="standard"/>

                        <c:field width="300" include="<%=f.getStPolicyTypeGroupID() != null%>"
                                 lov="LOV_PolicyType"
                                 caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" type="string"
                                 presentation="standard">
                            <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false"/>
                        </c:field>
                        <c:field width="300" 
                                 caption="Level Persetujuan"
                                 name="stLevelApproval" type="string" lov="LOV_APPROVAL_LEVEL" presentation="standard"/>

                    </table>
                </td>
            </tr>

            <c:evaluate when="<%="ACCEPT".equalsIgnoreCase(aptype)%>" >
                <tr>
                    <td>
                        <c:button text="Refresh" validate="true" event="select_approval_tsi"/>
                    </td>
                </tr>
            </c:evaluate>
            <c:evaluate when="<%="CLAIM".equalsIgnoreCase(aptype)%>" >
                <tr>
                    <td>
                        <c:button text="Refresh" validate="true" event="select_approval_tsi"/>
                    </td>
                </tr>
            </c:evaluate>
            <c:evaluate when="<%=f.getList() != null%>" >
                <c:evaluate when="<%="ACCEPT".equalsIgnoreCase(aptype)%>" >

                    <tr>
                        <td>

                            <c:listbox name="list">
                                <%
                                            final FlexTableView ff = (FlexTableView) current;

                                            final DTOList details = f.getList();
                                            final FlexTableView item = ((FlexTableView) details.get(index.intValue()));

                                            boolean readonly1 = item != null;

                                            boolean rod = false;

                                            if (item != null) {
                                                rod = item.getStFFTID() != null ? true : false;
                                            }
                                %>
                                <c:fieldcontrol when="<%=isApprovalFlag3Mode || isApprovalFlag1Mode || isApprovalFlag2Mode%>" readonly="true">
                                    <%--
                                    <c:listcol name="stReference3Desc" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                                    --%>
                                    <c:listcol title="" columnClass="header">
                                        <c:button text="+" event="addLine" validate="false" defaultRO="true"/>
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail">

                                    </c:listcol>
                                    <c:listcol title="Approval<br>Operator" name="stApprovedFlag1" flag="true">
                                        <%--<c:field name="list.[$index$].stApprovedFlag1" caption="Period End" type="check" readonly="true"  />--%>
                                    </c:listcol>
                                    <c:listcol title="Approval<br>Kabag"  name="stApprovedFlag2" flag="true">
                                        <%--<c:field name="list.[$index$].stApprovedFlag3" caption="Period End" type="check"  readonly="true"  />--%>
                                    </c:listcol>
                                    <c:listcol title="Approval<br>Kadiv"  name="stApprovedFlag3" flag="true">
                                        <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check"  readonly="true"  />--%>
                                    </c:listcol>
                                    <c:listcol title="Aktivasi<br>TI"  name="stApprovedFlag4" flag="true">
                                        <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check"  readonly="true"  />--%>
                                    </c:listcol>
                                    <c:listcol title="Active" name="stActiveFlag" flag="true">
                                        <%--<c:field name="list.[$index$].stActiveFlag" readonly="true" caption="Active" type="check" />--%>
                                    </c:listcol>
                                    <c:listcol title="Jenis" >
                                        <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSgl" popuplov="true" clientchangeaction="refresh()" caption="Kode" type="string" width="200" readonly="<%=rod%>" />
                                    </c:listcol>
                                    <c:listcol title="Tingkat Resiko" >
                                        <c:field name="list.[$index$].stReference6" lov="LOV_LEVEL_RISK" popuplov="true" caption="Kode" type="string" width="200" readonly="<%=rod%>">
                                            <c:param name="poltype" value="<%=ff.getStReference3()%>"  />
                                        </c:field>
                                    </c:listcol>
                                    <c:listcol title="Kode Resiko" >
                                        <c:field name="list.[$index$].stReference4" lov="LOV_RiskCategoryCode" popuplov="true" caption="Kode" type="string" width="100" readonly="<%=rod%>">
                                            <c:param name="poltype" value="<%=ff.getStReference3()%>"  />
                                        </c:field>
                                    </c:listcol>


                                    <c:listcol title="{L-ENGCompany Group-L}{L-INAGroup Perusahaan-L}" >
                                        <c:field name="list.[$index$].stReference5" lov="LOV_COMPANYGROUP" popuplov="true" caption="Kode" type="string" width="150" readonly="<%=rod%>">
                                            <c:param name="poltype" value="<%=ff.getStReference3()%>"  />
                                        </c:field>
                                    </c:listcol>

                                    <c:listcol title="Limit 1" >
                                        <c:field name="list.[$index$].dbReference1" caption="Limit1" type="money16.2" width="150" readonly="<%=rod%>" />
                                    </c:listcol>
                                    <c:listcol title="Limit 2" >
                                        <c:field name="list.[$index$].dbReference2" caption="Limit2" type="money16.2" width="150" readonly="<%=rod%>" />
                                    </c:listcol>
                                    <c:listcol title="Limit 3" >
                                        <c:field name="list.[$index$].dbReference3" caption="Limit3" type="money16.2" width="150" readonly="<%=rod%>" />
                                    </c:listcol>
                                    <c:listcol title="Active Date" >
                                        <c:field name="list.[$index$].dtActiveDate" caption="Active Date" type="date" readonly="false"  />
                                    </c:listcol>
                                    <c:listcol title="Period Start" >
                                        <c:field name="list.[$index$].dtPeriodStart" caption="Period Start" type="date" readonly="<%=rod%>"  />
                                    </c:listcol>
                                    <c:listcol title="Period End" >
                                        <c:field name="list.[$index$].dtPeriodEnd" caption="Period End" type="date" readonly="false"  />
                                    </c:listcol>
                                </c:fieldcontrol>

                                

                                <c:listcol title="Dokumen" >
                                    <c:field name="list.[$index$].stFileID" caption="Dokumen" type="file" thumbnail="true" readonly="false"  />
                                </c:listcol>


                            </c:listbox>


                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:evaluate when="<%=!isApprovalMode && isEditMode%>" >
                                <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
                            </c:evaluate>
                            <c:evaluate when="<%=isApprovalMode && isEditMode%>" >
                                <c:button text="{L-ENGApprove-L}{L-INASetujui-L}" validate="true" event="save" />
                            </c:evaluate>

                            <c:evaluate when="<%=isEditMode%>" >
                                <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                                <c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />
                            </c:evaluate>
                        </td>
                    </tr>
                    <tr>
                    <table cellpadding=2 cellspacing=1>
                        <c:evaluate when="<%=f.getList() != null%>" >
                            <c:evaluate when="<%=isShowButton%>">
                                <tr>
                                    <%--
                                    <td>
                                        <c:button text="View" validate="true" event="view_approval" />
                                    </td>--%>
                                    <c:evaluate when="<%=!isEditMode%>">

                                        <c:evaluate when="<%=canEditCapacityApproval%>" >
                                            <td><c:button text="Edit/Add" validate="true" event="select_edit_tsi" /></td>
                                        </c:evaluate>

                                        <c:evaluate when="<%=canApproveTSI1%>" >
                                            <td>
                                                <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Operator" validate="true" event="clickApproval1TSI" />
                                            </td>
                                        </c:evaluate>

                                        <c:evaluate when="<%=canApproveTSI2%>" >
                                            <td>
                                                <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Bagian" validate="true" event="clickApproval2TSI" />
                                            </td>
                                        </c:evaluate>
                                        <c:evaluate when="<%=canApproveTSI3%>" >
                                            <td>
                                                <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Divisi" validate="true" event="clickApproval3TSI" />
                                            </td>
                                        </c:evaluate>

                                        <c:evaluate when="<%=canApproveTSI4%>" >
                                            <td>
                                                <c:button text="Aktivasi TI" validate="true" event="clickApproval4TSI" />
                                            </td>
                                        </c:evaluate>
                                        <td>
                                            <c:button text="Print Limit Cabang" validate="true" event="print_limit" />
                                        </td>
                                        <td>
                                            <c:button text="Excel Limit" validate="true" event="excel_limit" />
                                        </td>
                                    </c:evaluate>
                                </tr>
                            </c:evaluate>
                        </c:evaluate>
                    </table>
                </tr>
            </c:evaluate>



            <c:evaluate when="<%="CLAIM".equalsIgnoreCase(aptype)%>" >
                <tr>
                    <td>
                        <c:listbox name="list">
                            <%
                                        final FlexTableView ffC = (FlexTableView) current;

                                        final DTOList detailsC = f.getList();
                                        final FlexTableView itemC = ((FlexTableView) detailsC.get(index.intValue()));

                                        boolean rodC = false;

                                        if (itemC != null) {
                                            rodC = itemC.getStFFTID() != null ? true : false;
                                        }
                            %>
                            <c:fieldcontrol when="<%=isApprovalFlag3Mode || isApprovalFlag1Mode || isApprovalFlag2Mode%>" readonly="true">
                                <c:listcol title="" columnClass="header">
                                    <c:button text="+" event="addLineClaim" validate="false" defaultRO="true"/>
                                </c:listcol>
                                <c:listcol title="" columnClass="detail"/>

                                <c:listcol title="Approval<br>Operator" name="stApprovedFlag1" flag="true" >
                                    <%--<c:field name="list.[$index$].stApprovedFlag1" caption="Period End" type="check" readonly="true"  />--%>
                                </c:listcol>
                                <c:listcol title="Approval<br>Kabag"  name="stApprovedFlag2" flag="true" >
                                    <%--<c:field name="list.[$index$].stApprovedFlag3" caption="Period End" type="check"  readonly="true"  />--%>
                                </c:listcol>
                                <c:listcol title="Approval<br>Kadiv"  name="stApprovedFlag3" flag="true" >
                                    <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check"  readonly="true"  />--%>
                                </c:listcol>
                                <c:listcol title="Aktivasi<br>TI"  name="stApprovedFlag4" flag="true">
                                        <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check"  readonly="true"  />--%>
                                    </c:listcol>
                                <c:listcol title="Active"  name="stActiveFlag" flag="true" >
                                    <%--<c:field name="list.[$index$].stActiveFlag" readonly="true" caption="Active" type="check" />--%>
                                </c:listcol>

                                <c:listcol title="Jenis" >
                                    <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSgl" popuplov="true" clientchangeaction="refresh()" caption="Kode" type="string" width="200" />
                                </c:listcol>
                                <c:listcol title="Penyebab Klaim" >
                                    <c:field name="list.[$index$].stReference4" caption="Penyebab Klaim" lov="LOV_ClaimCause" type="string" readonly="<%=rodC%>" width="200" >
                                        <c:param name="poltype" value="<%=ffC.getStReference3()%>"  />
                                    </c:field>
                                </c:listcol>
                                <c:listcol title="Limit" >
                                    <c:field name="list.[$index$].dbReference1" caption="Limit1" type="money16.2" width="150" readonly="<%=rodC%>" />
                                </c:listcol>
                                <c:listcol title="Active Date" >
                                    <c:field name="list.[$index$].dtActiveDate" caption="Active Date" type="date" readonly="false"  />
                                </c:listcol>
                                <c:listcol title="Period Start" >
                                    <c:field name="list.[$index$].dtPeriodStart" caption="Period Start" type="date" readonly="<%=rodC%>"  />
                                </c:listcol>
                                <c:listcol title="Period End" >
                                    <c:field name="list.[$index$].dtPeriodEnd" caption="Period End" type="date" readonly="false"  />
                                </c:listcol>


                            </c:fieldcontrol>
                            
                            <c:listcol title="Dokumen" >
                                <c:field name="list.[$index$].stFileID" caption="Dokumen" type="file" thumbnail="true" readonly="false"  />
                            </c:listcol>
                        </c:listbox>

                    </td>
                </tr>
                <tr>
                    <td>
                        <c:evaluate when="<%=!isApprovalMode && isEditMode%>" >
                            <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
                        </c:evaluate>
                        <c:evaluate when="<%=isApprovalMode && isEditMode%>" >
                            <c:button text="{L-ENGApprove-L}{L-INASetujui-L}" validate="true" event="save" />
                        </c:evaluate>
                        <c:evaluate when="<%=isEditMode%>" >
                            <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                            <c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />
                        </c:evaluate>
                    </td>
                </tr>

                <table cellpadding=2 cellspacing=1>
                    <c:evaluate when="<%=f.getList() != null%>" >
                        <c:evaluate when="<%=isShowButton%>">
                            <tr>
                                <%--
                                <td>
                                    <c:button text="View" validate="true" event="view_approval" />
                                </td>--%>
                                <c:evaluate when="<%=!isEditMode%>">

                                    <c:evaluate when="<%=canEditCapacityClaimApproval%>" >
                                        <td><c:button text="Edit/Add" validate="true" event="select_edit_tsi" /></td>
                                    </c:evaluate>

                                    <c:evaluate when="<%=canApproveClaim1%>" >
                                        <td>
                                            <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Operator" validate="true" event="clickApproval1TSI" />
                                        </td>
                                    </c:evaluate>

                                    <c:evaluate when="<%=canApproveClaim2%>" >
                                        <td>
                                            <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Bagian" validate="true" event="clickApproval2TSI" />
                                        </td>
                                    </c:evaluate>
                                    <c:evaluate when="<%=canApproveClaim3%>" >
                                        <td>
                                            <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Divisi" validate="true" event="clickApproval3TSI" />
                                        </td>
                                    </c:evaluate>
                                    <c:evaluate when="<%=canApproveClaim4%>" >
                                        <td>
                                            <c:button text="Aktivasi TI" validate="true" event="clickApproval4TSI" />
                                        </td>
                                    </c:evaluate>
                                    <td>
                                        <c:button text="Print Limit Cabang" validate="true" event="print_limit" />
                                    </td>
                                    <td>
                                        <c:button text="Excel Limit" validate="true" event="excel_limit" />
                                    </td>
                                </c:evaluate>
                            </tr>
                        </c:evaluate>
                    </c:evaluate>
                </table>
            </c:evaluate>


            <c:evaluate when="<%="FINANCE".equalsIgnoreCase(aptype)%>" >
                <tr>
                    <td>
                        <b> <font color="red" size="1">WARNING !!! Pastikan Active Flag Yang di Centang Hanya 1 Per Jenis Pertanggungan</font>  </b>
                    </td>
                </tr>
                <tr>
                    <td>

                        <c:listbox name="list">
                            <%
                                        final FlexTableView ff = (FlexTableView) current;
                            %>
                            <c:fieldcontrol when="<%=isApprovalFlag3Mode || isApprovalFlag1Mode || isApprovalFlag2Mode%>" readonly="true">
                                <c:listcol title="" columnClass="header">
                                    <c:button text="+" event="addLine" validate="false" defaultRO="true"/>
                                </c:listcol>
                                <c:listcol title="" columnClass="detail">

                                </c:listcol>
                                <c:listcol name="stReference3Desc" title="Jenis" />

                                <c:listcol title="Limit 1" >
                                    <c:field name="list.[$index$].dbReference1" caption="Limit1" type="money16.2" width="150" />
                                </c:listcol>
                                <c:listcol title="Active Date" >
                                    <c:field name="list.[$index$].dtPeriodStart" caption="Active Date" type="date" readonly="false"  />
                                </c:listcol>

                            </c:fieldcontrol>

                            <c:listcol title="Active" >
                                <c:field name="list.[$index$].stActiveFlag" readonly="false" caption="Active" type="check" />
                            </c:listcol>

                        </c:listbox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:evaluate when="<%=canEditCapacityApproval && isEditMode%>" >
                            <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
                        </c:evaluate>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />

                        <c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />
                    </td>
                </tr>
            </c:evaluate>

        </c:evaluate>
    </table>
</c:evaluate>

<c:evaluate when="<%="commission".equalsIgnoreCase(mode)%>" >
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="ref2" caption="Role" width="350" lov="LOV_Role" popuplov="true" type="string" presentation="standard" readonly="<%=f.getRef4() != null%>" changeaction="select_comission" />

                    <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                             caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                             name="stPolicyTypeGroupID" type="string" presentation="standard"/>

                    <c:field width="300" include="<%=f.getStPolicyTypeGroupID() != null%>"
                             lov="LOV_PolicyType"
                             caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" type="string"
                             presentation="standard">
                        <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false"/>
                    </c:field>

                    <c:field width="300"
                             caption="Level Persetujuan"
                             name="stLevelApproval" type="string" lov="LOV_APPROVAL_LEVEL" presentation="standard"/>
                    <tr>
                        <td>
                            <c:button text="Refresh" validate="true" event="select_approval_komisi"/>
                        </td>
                    </tr>
                    
                </table>
            </td>
        </tr>
        <c:evaluate when="<%=f.getList() != null%>" >

            <tr>
                <td>
                    <c:listbox name="list">
                        <%
                                    final DTOList details = f.getList();
                                    final FlexTableView item = ((FlexTableView) details.get(index.intValue()));

                                    boolean readonly1 = item != null;

                                    boolean rod = false;

                                    if (item != null) {
                                        rod = item.getStFFTID() != null ? true : false;
                                    }
                        %>
                        <c:fieldcontrol when="<%=rod%>" readonly="true">
                            <c:listcol title="" columnClass="header">
                                <c:button text="+" event="addLineComm" validate="false" defaultRO="true"/>
                            </c:listcol>
                            <c:listcol title="" columnClass="detail">
                                <c:button text="-" event="onDeleteDetail"
                                          clientEvent="f.detailIndex.value='$index$';" validate="false" />
                            </c:listcol>

                            <c:listcol title="Approval<br>Operator" name="stApprovedFlag1" flag="true" >
                                <%--<c:field name="list.[$index$].stApprovedFlag1" caption="Period End"  type="check" readonly="true"  />--%>
                            </c:listcol>

                            <c:listcol title="Approval<br>Kabag"  name="stApprovedFlag2" flag="true" >
                                <%--<c:field name="list.[$index$].stApprovedFlag3" caption="Period End"  type="check" readonly="true"  />--%>
                            </c:listcol>
                            <c:listcol title="Approval<br>Kadiv"  name="stApprovedFlag3" flag="true" >
                                <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check" readonly="true"  />--%>
                            </c:listcol>
                            <c:listcol title="Aktivasi<br>TI"  name="stApprovedFlag4" flag="true" >
                                <%--<c:field name="list.[$index$].stApprovedFlag4" caption="Period End" type="check" readonly="true"  />--%>
                            </c:listcol>
                            <c:listcol title="Active"  name="stActiveFlag" flag="true" >
                                <%--<c:field name="list.[$index$].stActiveFlag" caption="Active" type="check" readonly="true" />--%>
                            </c:listcol>

                            <c:listcol title="Jenis Asuransi" >
                                <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSearch" popuplov="true" caption="Kode" type="string" width="200" />
                            </c:listcol>


                            <c:listcol title="Grup Sumber Bisnis" >
                                <c:field name="list.[$index$].stReference4" caption="{L-ENGCompany Group-L}{L-INAGroup Perusahaan-L}" width="200" popuplov="true" lov="LOV_COMPANYGROUP" type="string" />
                            </c:listcol>

                            <c:listcol title="Pemasar" >
                                <c:field name="list.[$index$].stReference5" lov="LOV_Entity" popuplov="true" caption="Pemasar" type="string" width="200" />
                            </c:listcol>
                            <c:listcol title="Limit Total" >
                                <c:field name="list.[$index$].dbReference1" caption="Limit" type="money16.2" width="70" suffix="%" readonly="<%=rod%>" />
                            </c:listcol>
                            <c:listcol title="Limit Komisi 1" >
                                <c:field name="list.[$index$].dbReference2" caption="Limit" type="money16.2" width="70" suffix="%" readonly="<%=rod%>" />
                            </c:listcol>
                            <c:listcol title="Limit Komisi 2" >
                                <c:field name="list.[$index$].dbReference3" caption="Limit" type="money16.2" width="70" suffix="%" readonly="<%=rod%>" />
                            </c:listcol>
                            <c:listcol title="Limit Feebase" >
                                <c:field name="list.[$index$].dbReference4" caption="Limit" type="money16.2" width="70" suffix="%" readonly="<%=rod%>" />
                            </c:listcol>
                            <c:listcol title="Limit Brokerfee" >
                                <c:field name="list.[$index$].dbReference5" caption="Limit" type="money16.2" width="70" suffix="%" readonly="<%=rod%>" />
                            </c:listcol>
                            <c:listcol title="Active Date" >
                                <c:field name="list.[$index$].dtActiveDate" caption="Active Date" type="date" readonly="false"  />
                            </c:listcol>
                            <c:listcol title="Period Start" >
                                <c:field name="list.[$index$].dtPeriodStart" caption="Period Start" type="date" readonly="false"  />
                            </c:listcol>
                        </c:fieldcontrol>
                        <c:listcol title="Period End" >
                            <c:field name="list.[$index$].dtPeriodEnd" caption="Period End" type="date" readonly="false"  />
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
            <table cellpadding=2 cellspacing=1>
                <c:evaluate when="<%=f.getList() != null%>" >
                    <c:evaluate when="<%=isShowButton%>">
                        <tr><%--
                            <td>
                                <c:button text="View" validate="true" event="view_comission2" />
                            </td>--%>
                            <c:evaluate when="<%=!isEditMode%>">
                                <c:evaluate when="<%=canEditCapacityComission%>" >
                                    <td>
                                        <c:button text="Edit/Add" validate="true" event="select_edit_komisi" />
                                    </td>
                                </c:evaluate>

                                <c:evaluate when="<%=canApproveKomisi1%>" >
                                    <td>
                                        <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Operator" validate="true" event="clickApproval1Komisi" />
                                    </td>
                                </c:evaluate>

                                <c:evaluate when="<%=canApproveKomisi2%>" >
                                    <td>
                                        <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Bagian" validate="true" event="clickApproval2Komisi" />
                                    </td>
                                </c:evaluate>
                                <c:evaluate when="<%=canApproveKomisi3%>" >
                                    <td>
                                        <c:button text="{L-ENGApprove-L}{L-INASetujui-L} Kepala Divisi" validate="true" event="clickApproval3Komisi" />
                                    </td>
                                </c:evaluate>
                                <c:evaluate when="<%=canApproveKomisi4%>" >
                                    <td>
                                        <c:button text="Aktivasi TI" validate="true" event="clickApproval4Komisi" />
                                    </td>
                                </c:evaluate>
                            </c:evaluate>
                        </tr>
                    </c:evaluate>
                </c:evaluate>
            </table>
        </tr>
        <tr>
            <td>

                <c:evaluate when="<%=!isApprovalMode && isEditMode%>" >
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
                </c:evaluate>

                <c:evaluate when="<%=isApprovalMode && isEditMode%>" >
                    <c:button text="{L-ENGApprove-L}{L-INASetujui-L}" validate="true" event="save" />
                </c:evaluate>
                <c:evaluate when="<%=isEditMode%>" >
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                    <c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />
                </c:evaluate>

            </td>
        </tr>
    </c:evaluate>
</table>
</c:evaluate>

<c:evaluate when="<%="commissiongroup".equalsIgnoreCase(mode)%>" >
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="ref4" caption="{L-ENGCompany Group-L}{L-INAGroup Perusahaan-L}" width="350" popuplov="true" lov="LOV_COMPANYGROUP" type="string" presentation="standard" readonly="<%=f.getRef2() != null%>" changeaction="select_comission" />
                    <%--<c:field name="ref1" caption="Role" width="350" lov="LOV_Role" popuplov="true" type="string" presentation="standard" readonly="<%=f.getRef4() != null%>" changeaction="select_comission" />--%>
                    <c:field name="ref1" caption="{L-ENGBranch-L}{L-INACabang-L}" width="200" lov="LOV_CostCenter" type="string" presentation="standard" readonly="<%=f.getRef1() != null%>" changeaction="select_comission_group" />
                </table>
            </td>
        </tr>
        <c:evaluate when="<%=f.getList() != null%>" >
            <tr>
                <td>
                    <c:listbox name="list">
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="addLineCommGroup" validate="false" defaultRO="true"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteDetail"
                                      clientEvent="f.detailIndex.value='$index$';" validate="false" />
                        </c:listcol>

                        <c:listcol title="Jenis" >
                            <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSgl" popuplov="true" caption="Kode" type="string" width="200" />
                        </c:listcol>
                        <c:listcol title="Pemasar" >
                            <c:field name="list.[$index$].stReference5" lov="LOV_Entity" popuplov="true" caption="Pemasar" type="string" width="250" />
                        </c:listcol>
                        <c:listcol title="Limit" >
                            <c:field name="list.[$index$].dbReference1" caption="Limit" type="money16.2" width="80" suffix="%" />
                        </c:listcol>
                        <c:listcol title="Active Date" >
                            <c:field name="list.[$index$].dtActiveDate" caption="Active Date" type="date" readonly="false"  />
                        </c:listcol>
                        <c:listcol title="Period Start" >
                            <c:field name="list.[$index$].dtPeriodStart" caption="Period Start" type="date" readonly="false"  />
                        </c:listcol>
                        <c:listcol title="Period End" >
                            <c:field name="list.[$index$].dtPeriodEnd" caption="Period End" type="date" readonly="false"  />
                        </c:listcol>


                        <c:listcol title="Active" >
                            <c:field name="list.[$index$].stActiveFlag" caption="Active" type="check" readonly="false" />
                        </c:listcol>
                        <c:listcol title="Dokumen" >
                            <c:field name="list.[$index$].stFileID" caption="Dokumen" type="file" thumbnail="true" readonly="false"  />
                        </c:listcol>
                    </c:listbox>
                </td>
            </tr>
            <tr>
                <td>
                    <c:evaluate when="<%=canEditCapacityComissionGroup%>" >
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
                    </c:evaluate>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                </td>
            </tr>
        </c:evaluate>
    </table>
</c:evaluate>

<c:evaluate when="<%="commissionsetting".equalsIgnoreCase(mode)%>" >
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="ref1" caption="{L-ENGBranch-L}{L-INACabang-L}" width="200" lov="LOV_CostCenter" type="string" presentation="standard" readonly="<%=f.getRef1() != null%>" changeaction="select_branch"/>
                    <c:field show="<%=f.getRef1() != null%>" name="ref2" caption="{L-ENGRegion-L}{L-INADaerah-L}" width="200" lov="LOV_Region" type="string" presentation="standard" readonly="<%=f.getRef2() != null%>" changeaction="select_comission_setting" >
                        <c:lovLink name="cc_code" link="ref1" clientLink="false"/>
                    </c:field>
                </table>
            </td>
        </tr>
        <c:evaluate when="<%=f.getList() != null%>" >
            <tr>
                <td>
                    <c:field name="detailIndex" type="string" hidden="true"/>
                    <c:listbox name="list">
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="addLineCommSetting" validate="false" defaultRO="true"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteDetail"
                                      clientEvent="f.detailIndex.value='$index$';" validate="false" />
                        </c:listcol>
                        <c:listcol title="Jenis" >
                            <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSgl" caption="Kode" type="string" width="200" />
                        </c:listcol>
                        <c:listcol title="Group Perusahaan" >
                            <c:field name="list.[$index$].stReference5" popuplov="true" lov="LOV_COMPANYGROUP" caption="Group Perusahaan" type="string" width="200" />
                        </c:listcol>
                        <c:listcol title="Item" >
                            <c:field name="list.[$index$].stReference4" caption="Item" type="string" width="130" lov="LOV_InsuranceItem2" />
                        </c:listcol>
                        <c:listcol title="Limit" >
                            <c:field name="list.[$index$].dbReference1" caption="Limit 1" type="money16.2" width="80" suffix="%" />
                        </c:listcol>
                        <c:listcol title="Active" >
                            <c:field name="list.[$index$].stActiveFlag" caption="Active" type="check" readonly="false" />
                        </c:listcol>

                    </c:listbox>
                </td>
            </tr>
            <tr align="center">
                <td>
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" validate="true" event="save" />
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