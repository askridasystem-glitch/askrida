<%@ page import="com.webfin.gl.form.GLListForm,
com.crux.lov.LOVManager,
com.crux.util.Tools,
java.util.HashMap,
com.crux.lang.LanguageManager,
com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
    final GLListForm f = (GLListForm) frame.getForm();
    
    final String reporttype = f.getStReportType();
    
    final boolean rptSelected = f.getStReport()!=null;
    
    final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_JOURNAL",f.getStReport()));
    
    final boolean PFL_APPLY_DATE = StringTools.indexOf(((String) r1.get("APPLY_DATE")),"O")>=0;
    final boolean PFL_SETTLEMENT = StringTools.indexOf(((String) r1.get("SETTLEMENT")),"O")>=0;
    final boolean PFL_RECEIPT_NO = StringTools.indexOf(((String) r1.get("RECEIPT_NO")),"O")>=0;
    final boolean PFL_PERIOD_FROM = StringTools.indexOf(((String) r1.get("PERIOD_FROM")),"O")>=0;
    final boolean PFL_PERIOD_TO = StringTools.indexOf(((String) r1.get("PERIOD_TO")),"O")>=0;
    final boolean PFL_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"O")>=0;
    final boolean PFL_YEAR_FROM = StringTools.indexOf(((String) r1.get("YEAR_FROM")),"O")>=0;
    final boolean PFL_YEAR_TO = StringTools.indexOf(((String) r1.get("YEAR_TO")),"O")>=0;
    final boolean PFL_YEAR = StringTools.indexOf(((String) r1.get("YEAR")),"O")>=0;
    final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"O")>=0;
    final boolean PFL_CHECK = StringTools.indexOf(((String) r1.get("CHECK")),"O")>=0;
    
    final boolean PFM_APPLY_DATE = StringTools.indexOf(((String) r1.get("APPLY_DATE")),"M")>=0;
    final boolean PFM_SETTLEMENT = StringTools.indexOf(((String) r1.get("SETTLEMENT")),"M")>=0;
    final boolean PFM_RECEIPT_NO = StringTools.indexOf(((String) r1.get("RECEIPT_NO")),"M")>=0;
    final boolean PFM_PERIOD_FROM = StringTools.indexOf(((String) r1.get("PERIOD_FROM")),"M")>=0;
    final boolean PFM_PERIOD_TO = StringTools.indexOf(((String) r1.get("PERIOD_TO")),"M")>=0;
    final boolean PFM_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"M")>=0;
    final boolean PFM_YEAR_FROM = StringTools.indexOf(((String) r1.get("YEAR_FROM")),"M")>=0;
    final boolean PFM_YEAR_TO = StringTools.indexOf(((String) r1.get("YEAR_TO")),"M")>=0;
    final boolean PFM_YEAR = StringTools.indexOf(((String) r1.get("YEAR")),"M")>=0;
    final boolean PFM_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"M")>=0;
    final boolean PFM_CHECK = StringTools.indexOf(((String) r1.get("CHECK")),"M")>=0;
    final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")),"O")>=0;
    final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")),"O")>=0;
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_JOURNAL" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <br>
                <br>
            </td>
        </tr>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                    <c:field width="200" changeaction="onChangeReport" caption="Report" lov="LOV_JOURNALREPORT" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                    </c:field>
                    <c:evaluate when="<%=rptSelected%>" >
                        <c:evaluate when="<%=PFL_APPLY_DATE%>" >      
                            <tr>
                                <td>Payment Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="applyDateFrom" type="date" caption="Apply From"  />
                                    To <c:field name="applyDateTo" type="date" caption="Apply To" />
                                </td>
                            </tr>
                        </c:evaluate> 
                    </c:evaluate>                    
                    
                    <c:evaluate when="<%=PFL_SETTLEMENT%>" >
                        <c:field name="stSettlement" mandatory="<%=PFL_SETTLEMENT%>" type="string" width="200" descfield="stSettlementDesc" caption="Keuangan" presentation="standard" lov="LOV_Settlement" loveall="true"/>
                    </c:evaluate>
                    <c:evaluate when="<%=PFL_RECEIPT_NO%>" >
                        <c:field name="stReceiptNo" type="string" width="200" mandatory="false" caption="Receipt No" presentation="standard" />
                    </c:evaluate>
                    <c:field name="periodFrom" type="string" caption="Period From" lov="LOV_GL_Period" mandatory="<%=PFM_PERIOD_FROM%>" include="<%=PFL_PERIOD_FROM%>" presentation="standard" />
                    <c:field name="periodTo" type="string" caption="Period To" lov="LOV_GL_Period" mandatory="<%=PFM_PERIOD_TO%>" include="<%=PFL_PERIOD_TO%>" presentation="standard"/>
                    <c:field name="yearFrom" type="string" caption="Year From" lov="LOV_GL_Years" mandatory="<%=PFM_YEAR_FROM%>" include="<%=PFL_YEAR_FROM%>" presentation="standard"/>
                    <c:field name="yearTo" type="string" caption="Year To" lov="LOV_GL_Years" mandatory="<%=PFM_YEAR_TO%>" include="<%=PFL_YEAR_TO%>" presentation="standard"/>
                    <c:field name="period" type="string" caption="Period" lov="LOV_GL_Period" mandatory="<%=PFM_PERIOD%>" include="<%=PFL_PERIOD%>" presentation="standard"/>
                    <c:field name="year" type="string" caption="Year" lov="LOV_GL_Years" mandatory="<%=PFM_YEAR%>" include="<%=PFL_YEAR%>" presentation="standard"/>
                    <c:field name="branch" descfield="branchDesc" type="string" caption="Branch" lov="LOV_Branch" mandatory="<%=PFM_BRANCH%>" include="<%=PFL_BRANCH%>" presentation="standard"/>
                    <c:field name="stFlag" type="check" caption="Flag" mandatory="<%=PFM_CHECK%>" include="<%=PFL_CHECK%>" presentation="standard"/>
                    
                    
                </table>
            </td>
        </tr>
        
        <tr>
            <td align="center" >
                <br>
                <br>                
                <c:evaluate when="<%=!PFM_EXCEL_ONLY%>" >
                    <c:button text="Print" name="bprintx"  event="clickPrint"  validate="true" />
                </c:evaluate>
                <c:evaluate when="<%=PFM_EXCEL_BUTTON%>" >
                    <c:button text="EXPORT TO Excel" name="excel"  event="clickPrintExcel" />
                </c:evaluate>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>