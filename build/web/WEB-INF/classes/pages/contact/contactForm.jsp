<%@ page import="com.webfin.contact.forms.ContactMasterForm,
                 com.crux.util.JSPUtil,
                 com.webfin.contact.model.ContactView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Contact Form" >
<%
   final ContactMasterForm form = (ContactMasterForm)request.getAttribute("FORM");
   final boolean entityMandatory = false;

   final ContactView ent = form.getEntity();

   final boolean individual = ent.isIndividual();
   final boolean institutional = ent.isInstitutional();

   boolean ro = form.isReadOnly();

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
				  <%--<c:field hidden="true" name="userIndex"/>--%>
                  <c:field name="entity.stEntityID" caption="Contact ID" type="integer" readonly="true" presentation="standard" flags="auto3"/>
                  <c:field lov="LOV_CustomerClass" mandatory="true" changeaction="onClassChange"  name="entity.stEntityClass" caption="Contact Class" type="string|32"  presentation="standard" />
                  <%--c:field lov="VS_ENT_CUST_STATUS" name="entity.stCustomerStatus" caption="Contact Status" type="string|255"  presentation="standard" />--%>                  
                   <c:field name="entity.stFrontOfficeCode" width="50" caption="Front Office Code" type="string|10"  presentation="standard" include="<%=individual%>"/>
                  <c:field lov="VS_ENT_CUST_TITLE" name="entity.stTitle" width="80" caption="Title" type="string|32"  presentation="standard" include="<%=individual%>" />
                  <c:field name="entity.stEntityName" mandatory="true" caption="Full Name" width="200" type="string|255"  presentation="standard"/>
                  <c:field name="entity.stShortName" mandatory="true" caption="Short Name" width="200" type="string|128"  presentation="standard" include="<%=individual%>" />
                  <c:field name="entity.stContactPerson" caption="Contact Person" width="200" type="string|128"  presentation="standard" include="<%=institutional%>" />
                  <%--<c:field name="entity.stAddress" caption="Address" type="string|255"  width="200" readonly="true" rows="2" presentation="standard" />--%>
                  <%--<c:field name="entity.stTaxFile" caption="Tax File Number" type="string|32"  presentation="standard" />--%>
                  <%--<c:field name="entity.stARTermID" lov="lovPaymentTerm" caption="Payment Term" type="integer"  presentation="standard" />--%>
                  <%--<c:field name="entity.stAPTermID" lov="lovPaymentTerm" caption="Claim Payment Term" type="integer"  presentation="standard" />--%>
                  <c:field name="entity.dtBirthDate" caption="Birth Date" type="date"  presentation="standard" include="<%=individual%>"/>
                  <%--<c:field lov="LOV_Country" name="entity.stCountryID" caption="Nationality" type="string"  presentation="standard" />--%>
            	  <c:field lov="LOV_Religion" name="entity.stReligionCode" caption="Religion" type="string|10"  presentation="standard" include="<%=individual%>"/>
				  <c:field name="entity.stComment" caption="Comment" rows="3" width="200" type="string|128"  presentation="standard"/>
                  
				</table>
               </td>
               <td>
                  <table cellpadding=2 cellspacing=1>
                       <%-- <c:field lov="VS_ENT_ID_TYPE" width="60" name="entity.stIdentificationType" caption="Identification" type="string|10"  presentation="standard" />                 
                 <c:field name="entity.stIdentificationNumber" caption="Identification Number" type="string"  presentation="standard"/>
                  <c:field lov="VS_ENT_MARITAL" name="entity.stMaritalStatus" caption="Marital Status" type="string|10"  presentation="standard" include="<%=individual%>"/>
                  <c:field name="entity.lgDependentNum" caption="Number of Dependent" width="50" type="integer"  presentation="standard" include="<%=individual%>"/>--%>  
                            <%--<c:field name="entity.stSalesTax" caption="Sales Tax" width="60" type="string|10"  presentation="standard" />
                  <c:field width="200" name="entity.stBusinessLine" caption="Line Of Business" type="string|32"  presentation="standard" include="<%=institutional%>"/>
                  <c:field name="entity.dtIncorporateDate" caption="Incorporated On" type="date"  presentation="standard" include="<%=institutional%>"/>
                  <c:field name="entity.stGLCode" caption="GL Code" type="string"  presentation="standard"/>
                  <%--<c:field name="entity.stInsInwardFlag" caption="Inward Enabled" type="check"  presentation="standard" include="<%=institutional%>"/>--%>
                  <%--<c:field name="entity.stInsOutwardFlag" caption="Outward Enabled" type="check"  presentation="standard" include="<%=institutional%>"/>--%> 
                  <%--<c:field name="entity.stInsCompanyFlag" caption="Insurance Company" type="check"  presentation="standard" include="<%=institutional%>"/>--%> 
                  <%--<c:field lov="LOV_CustCategory1" name="entity.stCategory1" caption="Category" type="string"  presentation="standard" include="<%=institutional%>"/>--%> 
                  <%--<c:field lov="LOV_CustomerShareLevel" name="entity.stShareLevel" caption="Share Level" type="string"  presentation="standard"/> --%> 
                  <%--<c:field name="entity.stCaptiveFlag" caption="Captive" type="check"  presentation="standard" include="<%=institutional%>"/>--%> 
                  </table>
               </td>
            </tr>
         </table>

         <c:tab name="tabs">
            <c:tabpage name="TAB1">
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>Addresses</td>
                     <td>:</td>
                     <td>
                        <c:field width="400" lov="lovAddresses" name="stSelectedAddress" caption="Selected Address" type="string" changeaction="selectAddress" overrideRO="true" />
                        <c:button text="+" event="doNewAddress" enabled="<%=!ro%>"  />
                        <c:button text="-" event="doDeleteAddress" enabled="<%=!ro%>" />
                     </td>
                  </tr>
                  <c:evaluate when="<%=form.getAddress()!=null%>">
                     <tr>
                        <td colspan=3 class=header>
                           ADDRESS
                           <table cellpadding=2 cellspacing=1 class=row0>
                              <tr>
                                 <td>
                                    <table cellpadding=2 cellspacing=1>
                                       <%--<c:field name="address.stPredefRiskCode" caption="Predefined Risk Address" type="string" presentation="standard" />--%>
                                       <c:field lov="VS_ENT_ADDR_TYPE" name="address.stAddressType" caption="Address Type" type="string" presentation="standard" />
                                       <c:field name="address.stPrimaryFlag" caption="Primary Address" type="check" presentation="standard" />
                                       <c:field name="address.stMailingFlag" caption="Mailing Address" type="check" presentation="standard" />
                                       <c:field lov="LOV_Country" name="address.stCountryID" caption="Country" type="string" presentation="standard" />
                                       <c:field width="300" name="address.stRegionMapID" caption="Region Desc" lov="LOV_PostalCode" popuplov="true" type="string" presentation="standard" />
                                       <c:field mandatory="true" name="address.stAddress" rows="3" width="300" caption="Street Address" type="string|255" presentation="standard" />
                                       <%--<c:field name="address.stPostalCode" caption="Postal Code" type="string" presentation="standard" />--%>
                                       <%--<c:field lov="LOV_RegionLevel1" name="address.stProvinceID" width="200" caption="Province" type="string" presentation="standard" >
                                          <c:lovLink name="country" link="address.stCountryID"/>
                                       </c:field>
                                       <c:field lov="LOV_RegionLevel2" name="address.stRegionalID1" width="200" caption="Kabupaten/Kotamadya" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stProvinceID"/>
                                       </c:field>
                                       <c:field lov="LOV_RegionLevel3" name="address.stRegionalID2" caption="Kecamatan" width="200" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stRegionalID1"/>
                                       </c:field>
                                       <c:field lov="LOV_RegionLevel4" name="address.stRegionalID3" caption="Kelurahan" width="200" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stRegionalID2"/>
                                       </c:field>--%>

                                    </table>
                                 </td>
                                 <td>
                                    <table cellpadding=2 cellspacing=1>
                                       <c:field name="address.stPhone" caption="Phone" type="string|32" presentation="standard" />
                                       <c:field name="address.stPhoneMobile" caption="Mobile" type="string|32" presentation="standard" />
                                       <c:field name="address.stPhoneFax" caption="Fax" type="string|32" presentation="standard" />
                                       <c:field name="address.stEmail" caption="Email" type="email|128" presentation="standard" />
                                       <c:field name="address.stWebsite" caption="Website" type="string|255" presentation="standard" />
                                       <%--<c:field lov="VS_ENT_OWNERSHIP" name="address.stOwnershipCode" caption="Property Ownership" type="string" presentation="standard"/>--%>
                                       <%--<c:field name="address.dtOccupiedDate" caption="Occupied From" type="date" presentation="standard"/>--%>
                                    </table>
                                 </td>
                              </tr>
                           </table>
                        </td>
                     </tr>
                  </c:evaluate>
               </table>
            </c:tabpage>
            <%--<c:tabpage name="TAB2">
               Not yet implemented
            </c:tabpage>--%>
         </c:tab>
      </td>
   </tr>
   <tr>
      <td>
         <c:evaluate when="<%=!ro%>" >
            <c:button text="Save" event="doSave" validate="true"/>
            <c:button text="Cancel" event="doClose" validate="false"/>
         </c:evaluate>
         <c:evaluate when="<%=ro%>" >
            <c:button text="Close" event="doClose" validate="false"/>
         </c:evaluate>

      </td>
   </tr>
</table>
</c:frame>
