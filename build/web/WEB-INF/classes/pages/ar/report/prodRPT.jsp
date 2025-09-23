<%@ page import="com.webfin.ar.forms.FRRPTrptArAPDetailForm,
         com.crux.lov.LOVManager,
         com.crux.util.Tools,
         java.util.HashMap,
         com.crux.lang.LanguageManager,
         com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                final FRRPTrptArAPDetailForm f = (FRRPTrptArAPDetailForm) frame.getForm();

                final String reporttype = f.getStReportType();

                final boolean rptSelected = f.getStReport() != null;

                final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_INWARD", f.getStReport()));

                final boolean PFL_MUTATION_DATE = StringTools.indexOf(((String) r1.get("MUTATION_DATE")), "O") >= 0;
                final boolean PFL_DLA_DATE = StringTools.indexOf(((String) r1.get("DLA_DATE")), "O") >= 0;
                final boolean PFL_APPROVED_DATE = StringTools.indexOf(((String) r1.get("APPROVED_DATE")), "O") >= 0;
                final boolean PFL_INSCOMPANY = StringTools.indexOf(((String) r1.get("INSCOMPANY")), "O") >= 0;
                final boolean PFL_POLICY_TYPE = StringTools.indexOf(((String) r1.get("POLICY_TYPE")), "O") >= 0;
                final boolean PFL_TREATY = StringTools.indexOf(((String) r1.get("TREATY_TYPE")), "O") >= 0;
                final boolean PFL_INDEX = StringTools.indexOf(((String) r1.get("INDEX")), "O") >= 0;
                final boolean PFL_APPROVED = StringTools.indexOf(((String) r1.get("APPROVED")), "O") >= 0;
                final boolean PFL_BUSSPOLTYPE = StringTools.indexOf(((String) r1.get("BUSSPOLTYPE")), "O") >= 0;
                final boolean PFL_BUSSPOLTYPECOB = StringTools.indexOf(((String) r1.get("BUSSPOLTYPECOB")), "O") >= 0;

                final boolean PFM_MUTATION_DATE = StringTools.indexOf(((String) r1.get("MUTATION_DATE")), "M") >= 0;
                final boolean PFM_DLA_DATE = StringTools.indexOf(((String) r1.get("DLA_DATE")), "M") >= 0;
                final boolean PFM_APPROVED_DATE = StringTools.indexOf(((String) r1.get("APPROVED_DATE")), "M") >= 0;
                final boolean PFM_INSCOMPANY = StringTools.indexOf(((String) r1.get("INSCOMPANY")), "M") >= 0;
                final boolean PFM_POLICY_TYPE = StringTools.indexOf(((String) r1.get("POLICY_TYPE")), "M") >= 0;
                final boolean PFM_TREATY = StringTools.indexOf(((String) r1.get("TREATY_TYPE")), "M") >= 0;
                final boolean PFM_INDEX = StringTools.indexOf(((String) r1.get("INDEX")), "M") >= 0;
                final boolean PFM_APPROVED = StringTools.indexOf(((String) r1.get("APPROVED")), "M") >= 0;
                final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")), "O") >= 0;
                final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")), "O") >= 0;
                final boolean PFM_BUSSPOLTYPE = StringTools.indexOf(((String) r1.get("BUSSPOLTYPE")), "M") >= 0;
                final boolean PFM_BUSSPOLTYPECOB = StringTools.indexOf(((String) r1.get("BUSSPOLTYPECOB")), "O") >= 0;

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_INWARD" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <br>
                <br>
            </td>
        </tr>

        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                    <c:field width="450" changeaction="onChangeReport" caption="Report" lov="LOV_INWARDREPORT" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                    </c:field>
                    <c:evaluate when="<%=rptSelected%>" >
                        <c:evaluate when="<%=PFL_MUTATION_DATE%>" >      
                            <tr>
                                <td>Mutation Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="policyDateFrom" type="date" caption="Mutation From"  />
                                    To <c:field name="policyDateTo" type="date" caption="Mutation To" />
                                </td>
                            </tr>
                        </c:evaluate>            
                        <c:evaluate when="<%=PFL_DLA_DATE%>" >      
                            <tr>
                                <td>DLA Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="DLADateFrom" type="date" caption="DLA From"  />
                                    To <c:field name="DLADateTo" type="date" caption="DLA To" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_APPROVED_DATE%>" >
                            <tr>
                                <td>Approved Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="approvedDateFrom" type="date" caption="Approved From"  />
                                    To <c:field name="approvedDateTo" type="date" caption="Approved To" />
                                </td>
                            </tr>
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_BUSSPOLTYPECOB%>" >
                            <c:field name="stBussinessPolTypeCob" type="string" width="200" caption="Policy Group" lov="LOV_BussinessTypeCob" mandatory="false" presentation="standard"/>
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_POLICY_TYPE%>" >    
                            <c:field name="stPolicyTypeID" descfield="stPolicyTypeName" type="string" width="200" caption="Policy Type" presentation="standard" lov="LOV_PolicyType" mandatory="false" />                
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_BUSSPOLTYPE%>" >
                            <c:field name="stBussinessPolType" type="string" width="200" caption="Bussiness Type" lov="LOV_BussinessTypeGroup" mandatory="false" presentation="standard"/>
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_TREATY%>" >
                            <c:field name="stFltTreatyType" descfield="stFltTreatyTypeDesc" type="string" width="200" caption="Treaty" presentation="standard" lov="LOV_TreatyType" loveall="true"/>
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_INSCOMPANY%>" >
                            <c:field name="stCompanyID" descfield="stCompanyName" type="string" width="200" caption="Company" presentation="standard" lov="LOV_Entity" mandatory="false" popuplov="true" />
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_INDEX%>" >
                            <c:field caption="{L-ENG Index -L}{L-INA Index -L} " name="stIndex" type="check" mandatory="false" presentation="standard" />
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_APPROVED%>" >
                            <c:field caption="{L-ENG Approved -L}{L-INA Disetujui -L} " name="stApproved" type="check" mandatory="false" presentation="standard" />
                        </c:evaluate>
                    </c:evaluate>


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
