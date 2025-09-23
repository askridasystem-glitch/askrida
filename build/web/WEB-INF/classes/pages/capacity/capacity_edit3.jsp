<%@ page import="com.webfin.master.capacity.CapacityMasterForm,
         com.crux.util.DTOList,
         com.crux.web.controller.SessionManager,
         com.crux.ff.model.FlexTableView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                CapacityMasterForm f = (CapacityMasterForm) frame.getForm();

                final boolean ro = f.isReadOnly();

                String mode = f.getMode();

                String aptype = f.getRef2();

                final boolean isCreateMode = f.isCreateMode();

                final boolean isEditMode = f.isEditMode();

                final boolean isShowButton = f.isShowButton();

                final boolean canNavigateBranch = f.getStBranch() != null ? false : true;

                final boolean canNavigateRegion = SessionManager.getInstance().getSession().getStDivisionID() != null ? false : true;

    %>
    <c:evaluate when="<%="rkap".equalsIgnoreCase(mode)%>" >
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <c:field name="stBranch" caption="{L-ENGBranch-L}{L-INACabang-L}" width="200" lov="LOV_CostCenter" type="string" presentation="standard" readonly="<%=!canNavigateBranch%>" changeaction="select_branch"/>
                        <c:field show="<%=f.getStBranch() != null%>" lov="LOV_RKAP_UnitKerja" mandatory="false" readonly="<%=!canNavigateRegion%>" changeaction="refresh"
                                 width="200" name="stRegion" caption="Unit Kerja" type="string" presentation="standard">
                            <c:lovLink name="param" link="stBranch" clientLink="false"/>
                        </c:field>
                        <c:field show="<%=f.getStRegion() != null%>" width="100" changeaction="refresh" lov="LOV_GL_Years3" caption="Tahun"
                        name="ref6" type="string" readonly="<%=f.getRef6() != null%>" presentation="standard"/>

                        <%--<c:field show="<%=f.getRef6() != null%>" width="200" changeaction="refresh" lov="LOV_RKAP_Identifikasi"
                                 caption="Identifikasi" name="stIdentifikasi" type="string" presentation="standard">
                           <c:param name="param" value="<%= f.getRef2() + "|" + f.getRef6()%>"/>
                        </c:field>--%>

                        <c:field show="<%=f.getRef6() != null%>" width="200" changeaction="refresh" lov="LOV_RKAP_Identifikasi"
                                 caption="Program Kerja" name="stProgramKerja" type="string" presentation="standard" changeaction="view_sistemrkap">
                           <c:param name="param" value="<%= f.getStRegion() + "|" + f.getRef6()%>"/>
                        </c:field>

                        <%--<c:field width="200" show="<%=f.getRef6() != null%>"
                                 changeaction="refresh" lov="LOV_RKAP_ProgramKerja"
                                 caption="Program Kerja" name="stProgramKerja" type="string"
                                 presentation="standard" changeaction="view_sistemrkap">
                            <c:lovLink name="param" link="stIdentifikasi" clientLink="false"/>
                        </c:field>--%>

                        <c:evaluate when="<%=f.getListRKAP() != null%>" >
                            <tr>
                                <td>
                                    <c:button text="Refresh" validate="true" event="view_sistemrkap"/>
                                </td>
                            </tr>
                        </c:evaluate>


                        <table cellpadding=2 cellspacing=1>
                            <c:evaluate when="<%=isShowButton%>">
                                <tr>
                                    <td>
                                        <c:button text="View" validate="true" event="view_sistemrkap" />
                                    </td>
                                </tr>
                            </c:evaluate>
                        </table>
                    </table>
                </td>
            </tr>
            <c:evaluate when="<%=f.getListRKAP() != null%>" >

                <tr>
                    <td>
                        <c:listbox name="listRKAP">
                            <%
                                        //final FlexTableView items = ((FlexTableView) details.get(index.intValue()));
%>
                            <%--
<c:listcol title="" columnClass="header">
                                <c:button text="+" event="addLineRKAP" validate="false" defaultRO="true"/>
                            </c:listcol>
                            <c:listcol title="" columnClass="detail">
                                <c:button text="-" event="onDeleteDetail"
                                          clientEvent="f.detailIndex.value='$index$';" validate="false" />
                            </c:listcol>
                            --%>
                            <c:listcol title="Rencana Anggaran" >
                                <c:field name="listRKAP.[$index$].stReference3" lov="LOV_RKAP_RencanaAnggaran" popuplov="true" caption="Kode" type="string" width="400" />
                            </c:listcol>
                            <c:listcol title="Kertas Kerja" >
                                <c:field name="listRKAP.[$index$].stReference4" lov="LOV_RKAP_KertasKerja" popuplov="true" caption="Kode" type="string" width="400" />
                            </c:listcol>
                            <c:listcol title="Anggaran RKAP" >
                                <c:field name="listRKAP.[$index$].dbReference1" caption="Limit 1" type="money16.2" width="100" readonly="true" />
                            </c:listcol>
                            <c:listcol title="Sisa Anggaran" >
                                <c:field name="listRKAP.[$index$].dbReference2" caption="Limit 1" type="money16.2" width="100" readonly="true" />
                            </c:listcol>
                        </c:listbox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" validate="false" event="cancel" />
                        <%--<c:button text="{L-ENGPrint-L}{L-INAPrint-L}" validate="false" event="print" />--%>
                    </td>
                </tr>
            </c:evaluate>
        </table>
    </c:evaluate>

</c:frame>