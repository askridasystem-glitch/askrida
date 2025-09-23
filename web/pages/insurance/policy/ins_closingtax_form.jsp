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
                    <%--
<c:field width="200" readonly="<%=closing.getStClosingType() != null%>" caption="Jenis Closing" mandatory="true" name="closing.stClosingType" changeaction="selectClosingType" lov="VS_CLOSING_TYPE_TAX" type="string" presentation="standard" />
                    <c:evaluate when="<%=closing.getStClosingType() != null%>">
                    --%>
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

                        <c:field width="200" readonly="true" caption="Produksi Pajak pph21" name="closing.dbTSITotal" type="money16.2" presentation="standard" />
                        <c:field width="200" readonly="true" caption="OS Pajak pph21" name="closing.dbPremiReinsuranceTotal" type="money16.2" presentation="standard" />
                        <c:field width="200" readonly="true" caption="Produksi Pajak pph23" name="closing.dbComissionReinsuranceTotal" type="money16.0" presentation="standard" />
                        <c:field width="200" readonly="true" caption="OS Pajak pph23" name="closing.dbClaimReinsuranceTotal" type="money16.0" presentation="standard" />
                        <c:field width="200" mandatory="true" caption="Rekap Surat Hutang" name="closing.stNoSuratHutang" type="string" presentation="standard" />
                        <c:field width="120" mandatory="true" caption="Closing Pajak" name="closing.stReinsuranceClosingStatus" type="check" presentation="standard" />

                        <c:evaluate when="<%=closing.getStClosingID() != null%>">
                            <c:field width="120" caption="Tanggal Pembukuan" name="closing.dtInvoiceDate"  type="date" presentation="standard" />
                            <c:field width="120" mandatory="true" caption="Closing Keuangan" name="closing.stFinanceClosingStatus" type="check" presentation="standard" />
                        </c:evaluate>
<%--
                    </c:evaluate>
--%>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGRetrieve Data-L}{L-INACek Data-L}" event="retrieveDataTotalPajak" validate="true"/>
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGSave-L}{L-INASimpan-L}" event="saveTax" confirm="Yakin ingin di simpan" validate="true"/>
                    <c:button show="<%=form.isReverseMode()%>" text="{L-ENGReverse-L}{L-INAReverse-L}" event="doReversePajak" confirm="Yakin ingin di reverse" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>