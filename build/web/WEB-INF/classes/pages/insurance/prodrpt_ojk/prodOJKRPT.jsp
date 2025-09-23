<%@ page import="com.webfin.insurance.form.ProductionFinanceOJKReportForm,
         com.crux.lov.LOVManager,
         com.crux.util.Tools,
         java.util.HashMap,
         com.crux.lang.LanguageManager,
         com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="LAPORAN OJK">
    <%

                ProductionFinanceOJKReportForm f = (ProductionFinanceOJKReportForm) request.getAttribute("FORM");

                final String reporttype = f.getStReportType();

                final boolean canNavigateBranch = f.isCanNavigateBranch();

                final boolean canNavigateRegion = f.isCanNavigateRegion();

    %>

    <table cellpadding=2 cellspacing=1>

        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_PRINTING" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                Font <c:field width="60" name="stFontSize" type="string" lov="LOV_FONTSIZE" />
                <br>
                <br>
            </td>
        </tr>

        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                                    <c:field width="300" changeaction="onChangeReport" caption="Report" lov="LOV_MARKETINGREPORT" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
                                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                                    </c:field>
                                    <c:field width="300" caption="Jenis Rincian" lov="LOV_Prod_OJK" caption="Laporan OJK" name="stProdOJKID" descfield="stProdOJKDesc" type="string" presentation="standard" loveall="true"/>
                                    <%--
                                     <tr>
                                        <td>Policy Date</td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="policyDateFrom" type="date" caption="Period From"  />
                                            To <c:field name="policyDateTo" type="date" caption="Period To" />
                                        </td>
                                    </tr
                                    <tr>
                                        <td>Period Start</td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="periodFrom" type="date" caption="Period From"  />
                                            To <c:field name="periodTo" type="date" caption="Period To" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Approved Date</td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="appDateFrom" type="date" caption="Approved From"  />
                                            To <c:field name="appDateTo" type="date" caption="Approved To" />
                                        </td>
                                    </tr>
                                    <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyTypeGroupID" descfield="stPolicyTypeGroupDesc" type="string" presentation="standard" loveall="true"/>
                                    <c:field width="300" show="<%=f.getStPolicyTypeGroupID() != null%>" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard">
                                        <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                                    </c:field>
                                    <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="onChangeBranchGroup" />
                                    <c:field show="<%=f.getStBranch() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" descfield="stRegionDesc" type="string" readonly="<%=!canNavigateRegion%>" presentation="standard" >
                                        <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                                    </c:field>
                                    <c:field caption="{L-ENG Approved -L}{L-INA Disetujui -L} " name="stIndex" type="check" mandatory="false" presentation="standard" />
                                    --%>
                                    <c:field name="stTriwulan" type="string" width="200" caption="Triwulan" lov="LOV_Triwulan" mandatory="false" presentation="standard"/>
                                    <c:field name="stYear" type="string" caption="Year" lov="LOV_GL_Years2" mandatory="false" presentation="standard"/>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <c:button text="Print" event="clickPrint" />
                </td>
            </tr>
        </table>
    </c:frame>