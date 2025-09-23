<%@ page import="com.webfin.gl.model.GLCurrencyHistoryView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.form.CurrencyForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    CurrencyForm form = (CurrencyForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final GLCurrencyHistoryView currency = form.getCurrency();
    
    %>
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="100"  caption="Currency ID" name="currency.stCurrencyHistID" type="string" readonly="true" presentation="standard"/>
                    <c:field width="250"  mandatory="true" lov="LOV_Currency" caption="Valuta" name="currency.stCurrencyCode" type="string" presentation="standard"/>
                    <c:field width="250"  mandatory="true" caption="Description" name="currency.stCurrencyDesc" type="string" type="string" presentation="standard" />
                    <c:field width="100" mandatory="true" readonly="false" caption="Kurs" name="currency.dbRate" type="money16.2" mandatory="true" presentation="standard"/>
                    <c:field width="100"  mandatory="true" caption="Periode Awal" name="currency.dtPeriodStart" type="date" presentation="standard" />
                    <c:field width="100"  mandatory="true" caption="Periode Akhir" name="currency.dtPeriodEnd" type="date" presentation="standard" />
                    <c:field caption="Active" name="currency.stActiveFlag" type="check" presentation="standard" />
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>