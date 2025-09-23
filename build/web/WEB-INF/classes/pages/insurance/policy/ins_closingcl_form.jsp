<%@ page import="com.webfin.insurance.model.InsuranceClosingView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.lang.LanguageManager,
         com.webfin.insurance.form.InsuranceClosingForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
                InsuranceClosingForm form = (InsuranceClosingForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final InsuranceClosingView closing = form.getClosing();

    %>
    <table cellpadding=2 cellspacing=1>

        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="100" caption="Closing ID" name="closing.stClosingID" type="string" readonly="true" presentation="standard"/>
                    <c:field width="200" readonly="<%=closing.getStClosingType() != null%>" caption="Jenis Closing" mandatory="true" name="closing.stClosingType" changeaction="selectClosingType" lov="VS_CLOSING_TYPE_CL" type="string" presentation="standard" />
                    <c:evaluate when="<%=closing.getStClosingType() != null%>">
                        <tr>
                            <td>
                                Tanggal Setujui
                            </td>
                            <td>:</td>
                            <td>
                                <c:field width="120" caption="Tanggal Polis" name="closing.dtPolicyDateStart"  type="date" />
                                s/d
                                <c:field width="120" caption="Tanggal Polis Akhir" name="closing.dtPolicyDateEnd"  type="date" mandatory="true" />
                            </td>
                        </tr>

                        <tr>
                            <td>
                                Periode Awal
                            </td>
                            <td>:</td>
                            <td>
                                <c:field width="120" caption="Periode Awal" name="closing.dtPeriodStartStart"  type="date" />
                                s/d
                                <c:field width="120" caption="Periode Awal Akhir" name="closing.dtPeriodStartEnd"  type="date" />
                            </td>
                        </tr>

                        <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                                 caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                                 name="closing.stPolicyTypeGroupID" type="string" presentation="standard"/>

                        <c:field width="300" include="<%=closing.getStPolicyTypeGroupID() != null%>"
                                 lov="LOV_PolicyType"
                                 caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="closing.stPolicyTypeID" type="string"
                                 presentation="standard">
                            <c:lovLink name="polgroup" link="closing.stPolicyTypeGroupID" clientLink="false"/>
                        </c:field>

                        <c:field width="200"  caption="Exclude" name="closing.stParameter1" lov="LOV_POL_CREDIT" type="string" presentation="standard" />
                        <c:field width="200" caption="Jenis Treaty" name="closing.stTreatyType" lov="LOV_TreatyType" type="string" presentation="standard" />

                        <c:field width="600" rows="4" caption="Nomor LKP" name="closing.stDLANo" mandatory="false" type="string" presentation="standard" />
                    
                        <c:field width="200" readonly="true" caption="Total Klaim" name="closing.dbTSITotal" type="money16.2" presentation="standard" />
                        <c:field width="200" readonly="true" caption="Total Klaim  R/I" name="closing.dbClaimReinsuranceTotal" type="money16.2" presentation="standard" />
                        <c:field width="200" readonly="true" caption="Jumlah Data" name="closing.stDataAmount" type="money16.0" presentation="standard" />
                        <c:field width="200" readonly="true" caption="Jumlah Data Proses" name="closing.stDataProses" type="money16.0" presentation="standard" />
                        <c:field width="200"  caption="No Surat Hutang" name="closing.stNoSuratHutang" mandatory="true" type="string" presentation="standard" />
                        <c:field width="120" mandatory="true" caption="Closing Reasuransi" name="closing.stReinsuranceClosingStatus" type="check" presentation="standard" />

                            <c:field width="120" caption="Tanggal Pembukuan" name="closing.dtInvoiceDate"  type="date" presentation="standard" />
                           <%-- <c:field width="120" mandatory="true" caption="Closing Keuangan" name="closing.stFinanceClosingStatus" type="check" presentation="standard" /> --%>
                    </c:evaluate>

                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGRetrieve Data-L}{L-INACek Data-L}" event="retrieveDataTotalClaim" validate="true"/>
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGSave-L}{L-INASimpan-L}" event="save" confirm="Yakin ingin di simpan" validate="true"/>
                    <c:button show="<%=form.isReverseMode()%>" text="{L-ENGReverse-L}{L-INAReverse-L}" event="doReverseClaim" confirm="Yakin ingin di reverse" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>