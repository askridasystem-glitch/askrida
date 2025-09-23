<%@page import="com.webfin.insurance.model.InsurancePolicySOAView"%>
<%@ page import="com.webfin.insurance.model.InsurancePolicySOAView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.insurance.form.InsuranceClosingForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    InsuranceClosingForm form = (InsuranceClosingForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final InsurancePolicySOAView soa = form.getSoa();
    
    %>
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="100" caption="Closing ID" name="soa.stSOAID" type="string" readonly="true" presentation="standard"/>
                    <c:field width="400" caption="Nomor SOA" name="soa.stSOANo" type="string" readonly="false" presentation="standard"/>
                    <c:field width="400" rows="3" caption="Keterangan" name="soa.stDescription" type="string" readonly="false" presentation="standard"/>
                    <c:field width="300" caption="Reasuradur" lov="LOV_EntityOnly" popuplov="true" name="soa.stEntityID" type="string" readonly="false" presentation="standard"/>

                    <c:field width="300" caption="Treaty" name="soa.stTreatyType" type="string" readonly="false" presentation="standard"/>
                    <c:field width="300" caption="Triwulan" name="soa.stTriwulan" type="string" readonly="false" presentation="standard"/>
                    <c:field width="300" caption="Tahun" name="soa.stTahun" type="string" readonly="false" presentation="standard"/>

                    <c:field width="200" readonly="true" caption="Premi Reasuransi" name="soa.dbPremiReinsurance" type="money16.2" presentation="standard" />
                    <c:field width="200" readonly="true" caption="Komisi Reasuransi" name="soa.dbComissionReinsurance" type="money16.2" presentation="standard" />
                    <c:field width="200" readonly="true" caption="Klaim Reasuransi" name="soa.dbClaimReinsurance" type="money16.2" presentation="standard" />



                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGRetrieve Data-L}{L-INACek Data-L}" event="retrieveDataTotal" validate="true"/>
                    <c:button show="<%=!form.isReverseMode()%>" text="{L-ENGSave-L}{L-INASimpan-L}" event="save" confirm="Yakin ingin di simpan" validate="true"/>
                    <c:button show="<%=form.isReverseMode()%>" text="{L-ENGReverse-L}{L-INAReverse-L}" event="doReverse" confirm="Yakin ingin di reverse" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>