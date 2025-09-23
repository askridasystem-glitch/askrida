<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.crux.lov.LOVManager,
         com.webfin.gl.ejb.CurrencyManager,
         com.crux.util.LOV,
         com.crux.util.*,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.forms.RequestFeeForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
                RequestFeeForm form = (RequestFeeForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final ARRequestFee arrequest = form.getArrequest();
                final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(arrequest.getStCurrency());

                boolean effective = arrequest.isEffective();

                int phase = 0;
                if (arrequest.getStRegionID() != null) {
                    phase = 1;
                }
                if (arrequest.getStBiaopTypeID() != null) {
                    phase = 2;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="arrequest.stCostCenterCode" type="string"
                                readonly="<%=arrequest.getStCostCenterCode() != null%>" presentation="standard" changeaction="refresh"/>
                                <c:field show="<%=arrequest.getStCostCenterCode() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="arrequest.stRegionID" type="string" readonly="<%=arrequest.getStRegionID() != null%>" presentation="standard" changeaction="refresh" >
                                    <c:lovLink name="cc_code" link="arrequest.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:evaluate when="<%=phase >= 1%>">
                                    <c:field width="300" changeaction="onChangeRKAPGroup" lov="LOV_RKAPGroup"
                                             caption="RKAP" name="arrequest.stRKAPGroupID" type="string" presentation="standard"/>
                                    <c:field width="300" include="<%=arrequest.getStRKAPGroupID() != null%>"
                                             changeaction="onChangeBiaopGroup" lov="LOV_BiaopGroup"
                                             caption="Grup Operasional" name="arrequest.stBiaopGroupID" type="string"
                                             presentation="standard">
                                        <c:lovLink name="groupbiaop" link="arrequest.stRKAPGroupID" clientLink="false"/>
                                    </c:field>
                                    <c:field width="300" include="<%=arrequest.getStBiaopGroupID() != null%>"
                                             changeaction="onChangeBiaopType" lov="LOV_BiaopType"
                                             caption="Detil Operasional" name="arrequest.stBiaopTypeID" type="string"
                                             presentation="standard">
                                        <c:lovLink name="polgroup" link="arrequest.stBiaopGroupID" clientLink="false"/>
                                    </c:field>
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field width="150" lov="LOV_Account2" popuplov="true" clientchangeaction="selectEntity()"
                                             caption="No. Akun" name="arrequest.stAccountID" descfield="arrequest.stAccountNo" type="string" mandatory="true" presentation="standard">
                                        <c:param name="param" value="<%=arrequest.getStCostCenterCode() + "|" + arrequest.getStBiaopTypeID()%>"/>
                                    </c:field>
                                    <c:field caption="Nama Akun" width="300" rows="3" name="arrequest.stAccountDesc" type="string" presentation="standard"/>
                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field width="120" caption="No. Permintaan" name="arrequest.stRequestNo" type="string" presentation="standard" readonly="true"/>
                                    <c:field caption="Tanggal Permintaan" name="arrequest.dtTglRequest" type="date" mandatory="true" presentation="standard"/>
                                    <c:field caption="Estimasi Biaya" width="120" name="arrequest.dbNominal" type="money16.2" mandatory="true" presentation="standard"/>
                                    <c:field caption="Keterangan Biaya" width="300" rows="4" name="arrequest.stDescription" type="string" mandatory="true" presentation="standard"/>
                                    <c:field caption="Lampiran" width="120" name="arrequest.stFilePhysic" type="file" thumbnail="true" presentation="standard"/>
                                </c:evaluate>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:evaluate when="<%=phase >= 1 && phase >= 2%>">
                        <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="clickSave" confirm="Yakin Mau Disimpan ?" validate="true"/>
                    </c:evaluate>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=!effective && form.isApprovalMode()%>">
                    <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="clickApprove" validate="true"
                              confirm="Yakin Mau Disetujui ?"/>
                </c:evaluate>
                <c:evaluate when="<%=form.isReverseMode()%>" >
                    <c:button text="Reverse" event="clickReverse"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>
        </tr>
    </table>
</c:frame>

<script>       
    function selectEntity() {
        var o = window.lovPopResult;
        document.getElementById('arrequest.stAccountDesc').value = o.description;
    }
</script>