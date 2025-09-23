<%@ page import="com.webfin.insurance.form.ProductionReportForm,
                 com.crux.lov.LOVManager,
                 com.crux.util.Tools,
                 java.util.HashMap,
                 com.crux.lang.LanguageManager,
                 com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   final ProductionReportForm f = (ProductionReportForm) frame.getForm();
   
   final String reporttype = f.getStReportType();

   final boolean enableRiskFilter = f.isEnableRiskFilter();

   final boolean rptSelected = f.getStReport()!=null;

   final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_PRINTING",f.getStReport()));

   /*
POLICY_DATE=ON|PERIOD=ON|PERIOD_EXP=ON|POLICY_CLASS=ON|COVER_TYPE=ON|CATEGORY=ON|CUSTOMER=ON|POLICY_NO=ON|RISK_LOCATION=ON|POST_CODE=ON|RISK_CARD=ON|RISK_CODE=ON

   */

   final boolean PFL_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")),"O")>=0;
   final boolean PFL_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"O")>=0;
   final boolean PFL_ENTRY_DATE = StringTools.indexOf(((String) r1.get("ENTRY_DATE")),"O")>=0;
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
   final boolean PFL_MARKETER = StringTools.indexOf(((String) r1.get("MARKETER")),"O")>=0; 
   final boolean PFL_YEAR_FROM = StringTools.indexOf(((String) r1.get("YEAR_FROM")),"O")>=0;
   final boolean PFL_YEAR_TO = StringTools.indexOf(((String) r1.get("YEAR_TO")),"O")>=0;
   final boolean PFL_YEAR = StringTools.indexOf(((String) r1.get("YEAR")),"O")>=0;
   final boolean PFL_ZONE_CODE = StringTools.indexOf(((String) r1.get("ZONE_CODE")),"O")>=0;
   final boolean PFL_ZONE_EQUAKE = StringTools.indexOf(((String) r1.get("ZONE_EQUAKE")),"O")>=0;
   final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")),"O")>=0;
   final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")),"O")>=0;   
   final boolean PFM_TRANSFER_BUTTON = StringTools.indexOf(((String) r1.get("TRANSFER_BUTTON")),"O")>=0;
   final boolean PFL_TREATY = StringTools.indexOf(((String) r1.get("TREATY")),"O")>=0;
   final boolean PFL_NO = StringTools.indexOf(((String) r1.get("NO")),"O")>=0;
   final boolean PFL_NAMA = StringTools.indexOf(((String) r1.get("NAMA")),"O")>=0;
   final boolean PFL_CREATE_WHO = StringTools.indexOf(((String) r1.get("WHO")),"O")>=0;
   final boolean PFL_CLAIM_DATE = StringTools.indexOf(((String) r1.get("CLAIM_DATE")),"O")>=0;
   final boolean PFL_NORUT = StringTools.indexOf(((String) r1.get("NORUT")),"O")>=0;
   final boolean PFL_AUTHORIZED = StringTools.indexOf(((String) r1.get("AUTO")),"O")>=0;
   final boolean PFL_POST_CODE_ALL = StringTools.indexOf(((String) r1.get("POST_CODE_ALL")),"O")>=0;
   final boolean PFL_STATUS = StringTools.indexOf(((String) r1.get("STATUS_POL")),"O")>=0;
   final boolean PFL_YEAR_TREATY = StringTools.indexOf(((String) r1.get("YEAR_TREATY")),"O")>=0;
   final boolean PFL_NO_MUST = StringTools.indexOf(((String) r1.get("NOMUST")),"O")>=0;


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
   final boolean PFM_MARKETER = StringTools.indexOf(((String) r1.get("MARKETER")),"M")>=0; 
   final boolean PFM_YEAR_FROM = StringTools.indexOf(((String) r1.get("YEAR_FROM")),"M")>=0;
   final boolean PFM_YEAR_TO = StringTools.indexOf(((String) r1.get("YEAR_TO")),"M")>=0;
   final boolean PFM_YEAR = StringTools.indexOf(((String) r1.get("YEAR")),"M")>=0;
   final boolean PFM_ZONE_CODE = StringTools.indexOf(((String) r1.get("ZONE_CODE")),"M")>=0;
   final boolean PFM_ZONE_EQUAKE = StringTools.indexOf(((String) r1.get("ZONE_EQUAKE")),"M")>=0;
   final boolean PFM_CREATE_WHO = StringTools.indexOf(((String) r1.get("WHO")),"M")>=0;
   final boolean PFM_CLAIM_DATE = StringTools.indexOf(((String) r1.get("CLAIM_DATE")),"M")>=0;
   final boolean PFM_NORUT = StringTools.indexOf(((String) r1.get("NORUT")),"M")>=0;
   final boolean PFM_AUTHORIZED = StringTools.indexOf(((String) r1.get("AUTO")),"M")>=0;
   final boolean PFM_POST_CODE_ALL = StringTools.indexOf(((String) r1.get("POST_CODE_ALL")),"M")>=0;
   final boolean PFM_STATUS = StringTools.indexOf(((String) r1.get("STATUS_POL")),"M")>=0;
   final boolean PFM_NO_MUST = StringTools.indexOf(((String) r1.get("NOMUST")),"M")>=0;
   
   
   //String cantNavigate = (String) r1.get("NAVIGATEBRANCH");
   
   boolean cabang = true; 
   
   if(f.getStBranch()!=null){
   		if(f.getStBranch().equalsIgnoreCase("00")){
   			cabang = false;
        }
   }
   
   if(f.getStBranch()==null){
   		cabang = false;
   }
   
   
   //final boolean NAVIGATEBRANCH = "Y".equalsIgnoreCase((String) r1.get("NAVIGATEBRANCH"));




%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_PRINTING" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
            <br>
            <br>
         </td>
      </tr>
      
         <tr>
            <td>
               <table cellpadding=2 cellspacing=1>
                  <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                  <c:field width="450" changeaction="onChangeReport" caption="Report" lov="LOV_MARKETINGREPORT" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
				  <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
				  </c:field>
			<c:evaluate when="<%=rptSelected%>" >
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
                  <c:evaluate when="<%=PFL_CLAIM_DATE%>" >      
                     <tr>
                        <td>Claim Date</td>
                        <td>:</td>
                        <td>
                           <c:field name="claimDateFrom" type="date" caption="Claim From"  />
                           To <c:field name="claimDateTo" type="date" caption="Claim To" />
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
                  
                  <c:evaluate when="<%=PFL_ENTRY_DATE%>" >
                     <tr>
                        <td>Policy Date</td>
                        <td>:</td>
                        <td>
                           <c:field name="entryDateFrom" type="date" caption="Entry Date From"  />
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
                     <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyTypeGroupID" descfield="stPolicyTypeGroupDesc" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard" loveall="true"/>
                     <c:field width="300" show="<%=f.getStPolicyTypeGroupID()!=null%>" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard">
                        <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                     </c:field>
                  </c:evaluate>
                  
                  <c:evaluate when="<%=PFL_BRANCH%>" >
                     <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" readonly="<%=cabang%>" type="string" presentation="standard" loveall="true"/>
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
                   
                  <c:evaluate when="<%=PFL_TREATY%>" >
                     <c:field name="stFltTreatyType" type="string" width="200" descfield="stFltTreatyTypeDesc" caption="Treaty Type" presentation="standard" lov="LOV_TreatyType2" loveall="true"/>
                  </c:evaluate>
                  
                  <c:evaluate when="<%=PFL_YEAR_TREATY%>" >
                     <c:field name="stYearTreaty" type="string" width="50" caption="Year Treaty" presentation="standard"/>
                  </c:evaluate>
                  
                 <c:evaluate when="<%=PFL_CATEGORY%>" >
                     <c:field name="stCustCategory1" descfield="stCustCategory1Desc" type="string" width="200" caption="Bussiness Source" presentation="standard" lov="LOV_CustCategory1" loveall="true"/>
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_CUSTOMER%>" >
                     <c:field name="stEntityID" type="string" descfield="stEntityName" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Customer" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_MARKETER%>" >
                     <c:field name="stMarketerID" type="string" descfield="stMarketerName" width="200" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Marketer" presentation="standard" />
                  </c:evaluate>                  
                  <c:evaluate when="<%=PFL_INSCOMPANY%>" >
                     <c:field name="stCompanyID" type="string" descfield="stCompanyName" width="200" popuplov="true"  lov="LOV_COMPANYGROUP" mandatory="false" caption="Company" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POLICY_NO%>" >
                     <c:field name="stPolicyNo" type="string" width="200" mandatory="false" caption="Policy No" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_NO%>" >
                     <c:field name="stNoUrut" type="string" width="200" mandatory="false" caption="No." presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_NO_MUST%>" >
                     <c:field name="stNumber" type="string" width="200" mandatory="true" caption="No." presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_NAMA%>" >
                     <c:field name="stNama" type="string" width="200" mandatory="false" caption="Name" presentation="standard" />
                  </c:evaluate>
				  <c:evaluate when="<%=PFL_CREATE_WHO%>" >
                     <c:field name="stCreateID" type="string" descfield="stCreateName" width="200" popuplov="true"  lov="LOV_Profil" mandatory="false" caption="Create Who" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_RISK_LOCATION%>" >
                     <c:field name="stRiskLocation" type="string" width="200" mandatory="false" caption="Risk Location" presentation="standard" />
                  </c:evaluate>                  
                  <c:evaluate when="<%=PFL_RISK_CARD%>" >
                     <c:field name="stRiskCardNo" type="string" width="200" mandatory="false" caption="Risk Card No" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POST_CODE%>" >
                     <c:field name="stPostCode" descfield="stPostCodeDesc" type="string" width="200" mandatory="false" caption="Post Code" popuplov="true" lov="LOV_PostalCode" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_POST_CODE_ALL%>" >
                     <c:field name="stPostCode2" descfield="stPostCodeDesc2" type="string" width="200" mandatory="false" caption="Post Code All" popuplov="true" lov="LOV_PostalCode" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_RISK_CODE%>" >
                     <c:field name="stRiskCode" type="string" descfield="stRiskCodeName" width="200" mandatory="false" caption="Risk Category" popuplov="true" lov="LOV_RiskCategory" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_ZONE_CODE%>" >
                     <c:field name="stZoneCode" type="string" descfield="stZoneCodeName" width="200" mandatory="false" caption="Zone Code" popuplov="true" lov="LOV_ZoneCode" presentation="standard" />
                  </c:evaluate>                  
                  <c:evaluate when="<%=PFL_ZONE_EQUAKE%>" >
                     <c:field name="stZoneEquake" type="string" descfield="stZoneEquakeName" width="200" mandatory="false" caption="Earthquake Zone" popuplov="true" lov="LOV_EquakeZone" presentation="standard" />
                  </c:evaluate>
                  <c:evaluate when="<%=PFL_STATUS%>" >
                     <c:field name="stStatus" type="string" width="200" mandatory="false" caption="Status" presentation="standard" lov="LOV_PolicyLevel" loveall="true"/>
                  </c:evaluate>
                  
                  <c:evaluate when="<%=PFL_NORUT%>" >
                     <tr>
                        <td>No. Urut</td>
                        <td>:</td>
                        <td>
                           <c:field name="noUrutFrom" type="string" width="20"  />
                           s/d <c:field name="noUrutTo" type="string" width="20" />
                        </td>
                     </tr>
                  </c:evaluate>
                    
              <c:evaluate when="<%=PFM_YEAR_FROM%>" >    
               <c:field name="yearFrom" type="string" caption="Year From" lov="LOV_GL_Years" mandatory="false" include="<%=PFL_YEAR_FROM%>" presentation="standard"/>
			  </c:evaluate>
			  <c:evaluate when="<%=PFM_YEAR_TO%>" >               
               <c:field name="yearTo" type="string" caption="Year To" lov="LOV_GL_Years" mandatory="false" include="<%=PFL_YEAR_TO%>" presentation="standard"/>
              </c:evaluate>
              <c:evaluate when="<%=PFM_YEAR%>" >
               <c:field name="year" type="string" caption="Year" lov="LOV_GL_Years" mandatory="false" include="<%=PFL_YEAR%>" presentation="standard"/>
              </c:evaluate>
              <c:evaluate when="<%=PFL_AUTHORIZED%>" >
               <c:field name="stAuthorized" type="string" width="100" mandatory="false" caption="Authorized" presentation="standard" lov="LOV_AUTHORIZED" loveall="true"/>
              </c:evaluate>		
		     </c:evaluate>
				  
				  
                  
               </table>
            </td>
         </tr>
      
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
            <c:evaluate when="<%=!PFM_EXCEL_ONLY%>" >
            <c:button text="Print" name="bprintx"  event="clickPrint"  validate="true" />
            </c:evaluate>
            <c:evaluate when="<%=PFM_EXCEL_BUTTON%>" >
            <c:button text="EXPORT TO Excel" name="excel"  event="clickPrintExcel" />
            </c:evaluate>
            <c:evaluate when="<%=PFM_TRANSFER_BUTTON%>" >
            <c:button text="Transfer Data" name="transfer"  event="clickPrintTransfer" />
            </c:evaluate>
         </td>
      </tr>
   </table>
<iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>
