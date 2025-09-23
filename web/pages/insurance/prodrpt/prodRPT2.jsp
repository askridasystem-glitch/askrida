<%@ page import="com.webfin.insurance.form.ProductionReportForm,
                 com.crux.lov.LOVManager,
                 com.crux.util.Tools,
                 java.util.HashMap,
                 com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   final ProductionReportForm f = (ProductionReportForm) frame.getForm();

   final boolean enableRiskFilter = f.isEnableRiskFilter();

   final boolean rptSelected = f.getStPrintForm()!=null;

   final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_PRINTING",f.getStPrintForm()));

   /*
POLICY_DATE=ON|PERIOD=ON|PERIOD_EXP=ON|POLICY_CLASS=ON|COVER_TYPE=ON|CATEGORY=ON|CUSTOMER=ON|POLICY_NO=ON|RISK_LOCATION=ON|POST_CODE=ON|RISK_CARD=ON|RISK_CODE=ON

   */

   final boolean PFL_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")),"O")>=0;
   final boolean PFL_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"O")>=0;
   final boolean PFL_PERIOD_EXP = StringTools.indexOf(((String) r1.get("PERIOD_EXP")),"O")>=0;
   final boolean PFL_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")),"O")>=0;
   final boolean PFL_COVER_TYPE = StringTools.indexOf(((String) r1.get("COVER_TYPE")),"O")>=0;
   final boolean PFL_CATEGORY = StringTools.indexOf(((String) r1.get("CATEGORY")),"O")>=0;
   final boolean PFL_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"O")>=0;
   final boolean PFL_INSCOMPANY = StringTools.indexOf(((String) r1.get("INSCOMPANY")),"O")>=0;
   final boolean PFL_POLICY_NO = StringTools.indexOf(((String) r1.get("POLICY_NO")),"O")>=0;
   final boolean PFL_RISK_LOCATION = StringTools.indexOf(((String) r1.get("RISK_LOCATION")),"O")>=0;
   final boolean PFL_POST_CODE = StringTools.indexOf(((String) r1.get("POST_CODE")),"O")>=0;
   final boolean PFL_RISK_CARD = StringTools.indexOf(((String) r1.get("RISK_CARD")),"O")>=0;
   final boolean PFL_RISK_CODE = StringTools.indexOf(((String) r1.get("RISK_CODE")),"O")>=0;
   final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"O")>=0;
   final boolean PFL_TREATYTYPE = StringTools.indexOf(((String) r1.get("TREATYTYPE")),"O")>=0;
   final boolean PFL_CLAIMTYPE = StringTools.indexOf(((String) r1.get("CLAIMTYPE")),"O")>=0;

   final boolean PFM_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")),"M")>=0;
   final boolean PFM_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"M")>=0;
   final boolean PFM_PERIOD_EXP = StringTools.indexOf(((String) r1.get("PERIOD_EXP")),"M")>=0;
   final boolean PFM_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")),"M")>=0;
   final boolean PFM_COVER_TYPE = StringTools.indexOf(((String) r1.get("COVER_TYPE")),"M")>=0;
   final boolean PFM_CATEGORY = StringTools.indexOf(((String) r1.get("CATEGORY")),"M")>=0;
   final boolean PFM_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"M")>=0;
   final boolean PFM_INSCOMPANY = StringTools.indexOf(((String) r1.get("INSCOMPANY")),"M")>=0;
   final boolean PFM_POLICY_NO = StringTools.indexOf(((String) r1.get("POLICY_NO")),"M")>=0;
   final boolean PFM_RISK_LOCATION = StringTools.indexOf(((String) r1.get("RISK_LOCATION")),"M")>=0;
   final boolean PFM_POST_CODE = StringTools.indexOf(((String) r1.get("POST_CODE")),"M")>=0;
   final boolean PFM_RISK_CARD = StringTools.indexOf(((String) r1.get("RISK_CARD")),"M")>=0;
   final boolean PFM_RISK_CODE = StringTools.indexOf(((String) r1.get("RISK_CODE")),"M")>=0;
   final boolean PFM_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"M")>=0;
   final boolean PFM_TREATYTYPE = StringTools.indexOf(((String) r1.get("TREATYTYPE")),"M")>=0;
   final boolean PFM_CLAIMTYPE = StringTools.indexOf(((String) r1.get("CLAIMTYPE")),"M")>=0;




%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            Print <c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_PRINTING" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" lov="LOV_LANG" />
            <br>
            <br>
         </td>
      </tr>
      <c:evaluate when="<%=rptSelected%>" >
         <tr>
            <td>
               <table cellpadding=2 cellspacing=1>
                  <c:evaluate when="<%=PFL_POLICY_DATE%>" >
                     <tr>
                        <td>Policy Date</td>
                        <td>:</td>
                        <td>
                           <c:field name="policyDateFrom" type="date" caption="Period From"  />
                           To <c:field name="policyDateTo" type="date" caption="Period To" />
                        </td>
                     </tr>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_PERIOD%>" >
                     <tr>
                        <td>Period</td>
                        <td>:</td>
                        <td>
                           <c:field name="periodFrom" type="date" caption="Period From"  />
                           To <c:field name="periodTo" type="date" caption="Period To" />
                        </td>
                     </tr>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_PERIOD_EXP%>" >
                     <tr>
                        <td>Period Expire</td>
                        <td>:</td>
                        <td>
                           <c:field name="expirePeriodFrom" type="date" caption="Period From"  />
                           To <c:field name="expirePeriodTo" type="date" caption="Period To" />
                        </td>
                     </tr>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POLICY_CLASS%>" >
                     <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyTypeGroupID" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard"/>
                     <c:field width="300" show="<%=f.getStPolicyTypeGroupID()!=null%>" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard">
                        <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                     </c:field>
                  </c:evaluate>

                  <c:evaluate when="<%=PFL_COVER_TYPE%>" >
                     <c:field name="stFltCoverType" type="string" width="200" descfield="stFltCoverTypeDesc" caption="Cover type" presentation="standard" lov="LOV_InsuranceCoverType" loveall="true"/>
                  </c:evaluate>

                  <c:evaluate when="<%=PFL_CLAIMTYPE%>" >
                     <c:field name="stFltClaimStatus" type="string" width="200" descfield="stFltClaimStatusDesc" caption="Claim Status" presentation="standard" lov="LOV_ClaimStatus" loveall="true"/>
                  </c:evaluate>

                  <c:evaluate when="<%=PFL_TREATYTYPE%>" >
                     <c:field name="stFltTreatyType" mandatory="<%=PFM_TREATYTYPE%>" type="string" width="200" descfield="stFltTreatyTypeDesc" caption="Treaty type" presentation="standard" lov="LOV_TreatyType" loveall="true"/>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_CATEGORY%>" >
                     <c:field name="stCustCategory1" descfield="stCustCategory1Desc" type="string" width="200" caption="Category" presentation="standard" lov="LOV_CustCategory1" loveall="true"/>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_CUSTOMER%>" >
                     <c:field name="stEntityID" type="string" descfield="stEntityName" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Customer" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_INSCOMPANY%>" >
                     <c:field name="stEntityID" type="string" descfield="stEntityName" width="200" popuplov="true"  lov="LOV_InsuranceCompany" mandatory="false" caption="Company" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POLICY_NO%>" >
                     <c:field name="stPolicyNo" type="string" width="200" mandatory="false" caption="Policy No" presentation="standard" />
                  </c:evaluate>

                  <c:evaluate when="<%=PFL_RISK_LOCATION%>" >
                     <c:field name="stRiskLocation" type="string" width="200" mandatory="false" caption="Risk Location" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POST_CODE%>" >
                     <c:field name="stPostCode" type="string" width="200" mandatory="false" caption="Post Code" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_RISK_CARD%>" >
                     <c:field name="stRiskCardNo" type="string" width="200" mandatory="false" caption="Risk Card No" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_RISK_CODE%>" >
                     <c:field name="stRiskCode" type="string" width="200" mandatory="false" caption="Risk Code" presentation="standard" />
                  </c:evaluate>

                  <c:evaluate when="<%=PFL_BRANCH%>" >
                     <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" type="string" presentation="standard" />
                  </c:evaluate>
               </table>
            </td>
         </tr>
      </c:evaluate>
      <%--<tr>
         <td>
            <c:button event="btnPrintProd" text="Production Report"  />
            <c:button event="btnPrintReminder" text="Reminder Letters"  />
         </td>
      </tr>--%>
      <tr>
         <td align="center" >
            <br>
            <br>
            <c:button text="Print" name="bprintx"  event="clickPrint"  validate="true" />
         </td>
      </tr>
   </table>
<iframe src="" id=frmx width=1 height=1></iframe>
<script>
   function btnPrint() {
      frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
   }
</script>
</c:frame>
