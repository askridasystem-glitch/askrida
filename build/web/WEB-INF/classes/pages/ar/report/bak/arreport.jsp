<%@ page import="com.crux.util.JSPUtil,
				 com.crux.lov.LOVManager,
				 com.crux.util.Tools,
                 com.webfin.ar.forms.ARReportForm,
                 java.util.HashMap,
                 com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="AR REPORT">
<%
   final ARReportForm form = (ARReportForm) frame.getForm();
   
   //final boolean enableRiskFilter = form.isEnableRiskFilter();

   //final boolean rptSelected = form.getStPrintForm()!=null;

   /*final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_PRINTING",form.getStPrintForm()));



   final boolean PFL_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")),"O")>=0;
   final boolean PFL_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"O")>=0;
   final boolean PFL_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")),"O")>=0;
   final boolean PFL_COVER_TYPE = StringTools.indexOf(((String) r1.get("COVER_TYPE")),"O")>=0;
   final boolean PFL_CATEGORY = StringTools.indexOf(((String) r1.get("CATEGORY")),"O")>=0;
   final boolean PFL_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"O")>=0;
   final boolean PFL_POLICY_NO = StringTools.indexOf(((String) r1.get("POLICY_NO")),"O")>=0;
   final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"O")>=0;

   final boolean PFM_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")),"M")>=0;
   final boolean PFM_PERIOD = StringTools.indexOf(((String) r1.get("PERIOD")),"M")>=0;
   final boolean PFM_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")),"M")>=0;
   final boolean PFM_COVER_TYPE = StringTools.indexOf(((String) r1.get("COVER_TYPE")),"M")>=0;
   final boolean PFM_CATEGORY = StringTools.indexOf(((String) r1.get("CATEGORY")),"M")>=0;
   final boolean PFM_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"M")>=0;
   final boolean PFM_POLICY_NO = StringTools.indexOf(((String) r1.get("POLICY_NO")),"M")>=0;
   final boolean PFM_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"M")>=0;
	*/
%>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         Print Report : <%=JSPUtil.print(form.getMode())%> <br>
      </td>
   </tr>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
          <tr>
           <td>Policy Date</td>
           <td>:</td>
           <td>
             <c:field name="policyDateFrom" type="date" caption="Period From"  />
          To <c:field name="policyDateTo" type="date" caption="Period To" />
           </td>
          </tr>

          

          <tr>
           <td>Period</td>
           <td>:</td>
           <td>
             <c:field name="periodFrom" type="date" caption="Period From"  />
          To <c:field name="periodTo" type="date" caption="Period To" />
           </td>
          </tr>

          

          <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyClass" type="string" mandatory="false" presentation="standard" loveall="true"/>
          <c:field width="300" show="<%=form.getStPolicyClass()!=null%>" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyType" descfield="stPolicyTypeDesc" type="string" mandatory="false" presentation="standard">
              <c:lovLink name="polgroup" link="stPolicyClass" clientLink="false" />
          </c:field>

          

          <c:field name="stBranch" type="string" width="200" descfield="stBranchDesc" caption="Branch" presentation="standard" lov="LOV_Branch" loveall="true"/>

               
      
		  <c:field name="stCoverType" type="string" width="200" descfield="stFltCoverTypeDesc" caption="Cover type" presentation="standard" lov="LOV_InsuranceCoverType" loveall="true"/>

          
       
          <c:field name="stBussiness" type="string" width="200" descfield="stCustCategory1Desc" caption="Bussiness Source" presentation="standard" lov="LOV_CustCategory1" loveall="true"/>

      
          <c:field name="stCustomer" type="string" width="200" descfield="stEntityName" caption="Customer" presentation="standard" lov="LOV_Entity" popuplov="true" mandatory="false"/>

          

          <c:field name="stPolicyNo" type="string" width="200" mandatory="false" caption="Policy No" presentation="standard" />

                  
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <%--<table cellpadding=2 cellspacing=1>
            <c:field name=""  caption="" type="" mandatory="true" />
         </table>--%>

         <c:button text="Print" event="clickPrint" />
      </td>
   </tr>
</table>
</c:frame>