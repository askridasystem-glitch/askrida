<%@ page import="com.webfin.entity.forms.EntityMasterForm,
                 com.crux.util.JSPUtil,
                 com.crux.web.controller.SessionManager,
                 com.webfin.entity.model.EntityView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="DATA CUSTOMER" >
<%
   final EntityMasterForm form = (EntityMasterForm)request.getAttribute("FORM");
   final boolean entityMandatory = false;

   final EntityView ent = form.getEntity();

   final boolean individual = ent.isIndividual();
   final boolean institutional = ent.isInstitutional();

   boolean ro = form.isReadOnly();
   
   boolean bpdReadOnly = false;
   String bpd = "Y";
   if(ent.getStCategory1()!=null)
       if(ent.getStCategory1().equalsIgnoreCase("4")){
                bpdReadOnly = true;
                bpd = "N";
           }

   final boolean isSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");

   boolean isAgen = false;

   if(ent.getStBusinessSourceCode()!=null)
       if(ent.getStBusinessSourceCode().equalsIgnoreCase("2"))
           isAgen = true;
           

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                  <c:field name="entity.stEntityID" caption="Entity ID" type="integer" readonly="true" presentation="standard" flags="auto3"/>
                  <c:field lov="LOV_Branch" width="200" mandatory="true" readonly="<%=ent.getStCostCenterCode()!=null%>" changeaction="onChangeBranch" name="entity.stCostCenterCode" caption="{L-ENGBranch Author-L}{L-INACabang-L}" type="string|32"  presentation="standard" />
                  <c:field lov="LOV_CustCategory1" readonly="<%=ent.getStCategory1()!=null%>" changeaction="onCategoryChange" mandatory="true" name="entity.stCategory1" caption="{L-ENGCategory-L}{L-INAKategori-L}" type="string"  width="200" presentation="standard" />
                  <c:evaluate when="<%=ent.getStCategory1()!=null%>">
                      <c:field lov="LOV_CompType" width="230" readonly="<%=ent.getStGLCode()!=null%>" mandatory="true" name="entity.stRef1" changeaction="changeJenisPerusahaan" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255"  presentation="standard" >
                          <c:param name="custcat" value="<%=bpd%>"  />
                      </c:field>
                      <c:field popuplov="true" lov="LOV_COMPANYGROUP" width="230" clientchangeaction="cekGroup()" mandatory="true" name="entity.stRef2" caption="{L-ENGCompany Group-L}{L-INAGrup Perusahaan-L}" type="string|255"  presentation="standard" >
                          <c:param name="custcat" value="<%=bpd%>"  />
                      </c:field>
                  </c:evaluate>
                  <c:field name="entity.stReferenceEntityID" popuplov="true" lov="LOV_Entity" mandatory="false" caption="{L-ENGReference Entity-L}{L-INACustomer Induk-L}" width="250" type="string|50"  presentation="standard" />
                  <c:field lov="LOV_CustomerClass" mandatory="true" changeaction="onClassChange"  name="entity.stEntityClass" caption="{L-ENGCustomer Class-L}{L-INAKelas Customer-L}" type="string|32"  width="200" presentation="standard" />
                  <c:field lov="VS_BUSINESS_SOURCE_CODE" mandatory="true" changeaction="onClassChange" name="entity.stBusinessSourceCode" caption="Klasifikasi Sumber Bisnis" type="string|32"  width="200" presentation="standard" />
                  <c:field lov="VS_ENT_CUST_TITLE" name="entity.stTitle" width="80" caption="{L-ENGTitle-L}{L-INAGelar-L}" type="string|32"  presentation="standard" include="<%=individual%>" />
                  <c:field name="entity.stEntityName" mandatory="true" caption="{L-ENGFull Name-L}{L-INANama Lengkap-L}" width="250" rows="2" type="string|255"  presentation="standard"/>
                  <c:field name="entity.stShortName" mandatory="true" caption="{L-ENGShort Name-L}{L-INANama Panggilan-L}" width="250" rows="2" type="string|128"  presentation="standard"/>
                  <%--<c:field lov="LOV_BusinessSource" name="entity.stRef1" caption="Bussiness Source" type="string|255"  presentation="standard" />--%>
                  <c:field width="200" name="entity.stBusinessLine" caption="{L-ENGLine Of Business-L}{L-INAJenis Bisnis-L}" type="string|32"  presentation="standard" /> <%-- include="<%=institutional%>"/> --%>
                  <c:field lov="VS_ENT_CUST_STATUS" name="entity.stCustomerStatus" caption="{L-ENGCustomer Status-L}{L-INAStatus Customer-L}" type="string|255"  width="200" presentation="standard" />
                  
                  <%--  <c:field name="entity.stFrontOfficeCode" width="50" caption="Front Office Code" type="string|10"  presentation="standard" include="<%=individual%>"/>
                  --%>
                  <c:field name="entity.stFunctionaryName" mandatory="false" caption="{L-ENGFunctionary Name-L}{L-INANama Pejabat-L}" width="230" type="string|50"  presentation="standard" include="<%=institutional%>"/>
                  <c:field name="entity.stFunctionaryPosition" mandatory="false" caption="{L-ENGFunctionary Position-L}{L-INAJabatan-L}" width="230" type="string|50"  presentation="standard" include="<%=institutional%>"/>
           	  <c:field name="entity.stRcNo" caption="{L-ENGBank RC No.-L}{L-INANomor Rekening-L}" width="230" type="string|50"  presentation="standard" />

                                                 <%--<c:field name="entity.stAddress" caption="Address" type="string|255"  width="200" readonly="true" rows="2" presentation="standard" />--%>
                  <c:field name="entity.stBirthPlace" caption="{L-ENGBirth Place-L}{L-INATempat Lahir-L}" type="string" width="250"  mandatory="true" presentation="standard" include="<%=individual%>"/>
                  <c:field name="entity.dtBirthDate" caption="{L-ENGBirth Date-L}{L-INATanggal Lahir-L}" type="date" mandatory="true" presentation="standard" include="<%=individual%>"/>

                  <%--<c:field lov="LOV_Marketter" name="entity.stRef2" caption="Marketing" type="string|255"  presentation="standard" />--%>
                  </table>
               </td>
               <td> 
                  <table cellpadding=2 cellspacing=1>
                  <c:field lov="VS_TAX_CODE" name="entity.stTaxCode" caption="Alokasi Pajak" type="string" mandatory="true" presentation="standard" />    
                  <c:field name="entity.stTaxFile" width="250" caption="{L-ENGTax Number-L}{L-INANPWP-L}" type="string|32"  presentation="standard" mandatory="<%=institutional || isAgen%>" />
                  <%--<c:field name="entity.stARTermID" lov="lovPaymentTerm" caption="Payment Term" type="integer"  presentation="standard" />
                  <c:field name="entity.stAPTermID" lov="lovPaymentTerm" caption="Claim Payment Term" type="integer"  presentation="standard" />
                  --%>
                  <c:field lov="LOV_Country" name="entity.stCountryID" caption="{L-ENGNationality-L}{L-INAKewarganegaraan-L}" width="250" type="string"  presentation="standard" />
                  <c:field lov="VS_ENT_ID_TYPE" width="100" name="entity.stIdentificationType" caption="{L-ENGIdentification-L}{L-INAPengenal-L}" type="string|10"  presentation="standard" />
                  <c:field name="entity.stIdentificationNumber" mandatory="<%=individual || isAgen%>" caption="{L-ENGIdentification Number-L}{L-INANo. Pengenal-L}" type="string" width="250" presentation="standard"/>
                  <c:field lov="VS_ENT_MARITAL" name="entity.stMaritalStatus" caption="{L-ENGMarital Status-L}{L-INAStatus Perkawinan-L}" type="string|10"  presentation="standard" include="<%=individual%>"/>
                  <c:field name="entity.lgDependentNum" caption="Number of Dependent" width="50" type="integer"  presentation="standard" include="<%=individual%>"/>
                  <c:field lov="LOV_Religion" name="entity.stReligionCode" caption="{L-ENGReligion-L}{L-INAAgama-L}" type="string|10"  presentation="standard" include="<%=individual%>"/>
                  <%--  <c:field name="entity.stSalesTax" caption="Sales Tax" width="60" type="string|10"  presentation="standard" />--%>
                  <c:field name="entity.dtActiveDate" caption="Active Date" type="date"  presentation="standard" />
                  <c:field name="entity.stGLCode" readonly="true" caption="GL Code" type="string"  presentation="standard"/>
                  <%--<c:field name="entity.stInsInwardFlag" caption="Inward Enabled" type="check"  presentation="standard" include="<%=institutional%>"/>
                  <c:field name="entity.stInsOutwardFlag" caption="Outward Enabled" type="check"  presentation="standard" include="<%=institutional%>"/>--%>
                  <c:field lov="LOV_CustomerShareLevel" name="entity.stShareLevel" caption="Share Level" type="string"  presentation="standard"/>
                  <c:field name="entity.stInsCompanyFlag" caption="{L-ENGInsurance Company-L}{L-INAPerusahaan Asuransi-L}" type="check"  presentation="standard" include="<%=institutional%>"/>
                  <c:field name="entity.stFinanceFlag" caption="{L-ENGFinance-L}{L-INAKeuangan (Active)-L}" type="check"  presentation="standard" />
                  <%--  <c:field name="entity.stCaptiveFlag" caption="Captive" type="check"  presentation="standard" include="<%=institutional%>"/>
                  --%>
                  <c:evaluate when="<%=isSuperEdit%>">
                        <c:field name="entity.stRefEntityID" caption="Kode Koas" type="string" width="50"  presentation="standard" />
                        <c:field name="entity.stReasEntityID" caption="Kode Reas" type="string" width="50"  presentation="standard" />
                    </c:evaluate>
                  </table>
               </td>
            </tr>
         </table>

         <c:tab name="tabs">
            <c:tabpage name="TAB1">
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>{L-ENGAddresses-L}{L-INAAlamat-L}</td>
                     <td>:</td>
                     <td>
                        <c:field width="400" lov="lovAddresses" name="stSelectedAddress" caption="Selected Address" type="string" changeaction="selectAddress" overrideRO="true" />
                        <c:button text="+" event="doNewAddress" enabled="<%=!ro%>"  />
                        <c:button text="-" event="doDeleteAddress" enabled="<%=!ro%>"/>
                     </td>
                  </tr>
                  <c:evaluate when="<%=form.getAddress()!=null%>">
                     <tr>
                        <td colspan=3 class=header>
                           {L-ENGADDRESS-L}{L-INAALAMAT-L}
                           <table cellpadding=2 cellspacing=1 class=row0>
                              <tr>
                                 <td>
                                    <table cellpadding=2 cellspacing=1>
                                       <%--<c:field name="address.stPredefRiskCode" caption="Predefined Risk Address" type="string" presentation="standard" />--%>
                                       <c:field mandatory="true" name="address.stAddress" rows="3" width="300" caption="{L-ENGStreet Address-L}{L-INAAlamat Jalan-L}" type="string|255" presentation="standard" />
                                       <%--<c:field lov="LOV_Branch" width="200" mandatory="true" name="address.stCostCenterCode" caption="{L-ENGBranch Author-L}{L-INAData Cabang-L}" type="string|32"  presentation="standard" />
                                       
                                       <c:field readonly="false" mandatory="true" width="300" name="address.stRegionalID1" caption="Kotamadya" lov="LOV_Kota" popuplov="true" type="string" presentation="standard" />
                             		   <c:field readonly="false" mandatory="true" width="300" name="address.stRegionalID2" caption="Kecamatan" lov="LOV_Kecamatan" popuplov="true" type="string" presentation="standard" />
			                           <c:field readonly="false" mandatory="true" width="300" name="address.stRegionalID3" caption="Kelurahan" lov="LOV_Kelurahan" popuplov="true" type="string" presentation="standard" />
                                       --%>
                                       <%--  <c:field lov="LOV_Country" name="address.stCountryID" caption="Country" type="string" presentation="standard" />
                                              <c:field name="address.stPostalCode" caption="Postal Code" type="string" presentation="standard" />--%>
                                       
                                       <%--  <c:field lov="LOV_RegionLevel1" name="address.stProvinceID" width="200" caption="{L-ENGProvince-L}{L-INAProvinsi-L}" type="string" presentation="standard" />
                                        --%> <%--  
                                          <c:lovLink name="country" link="address.stCountryID"/>
                                       </c:field>--%> 
                                       
                                       <%--  <c:field lov="LOV_RegionLevel2" name="address.stRegionalID1" width="200" caption="Kabupaten/Kotamadya" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stProvinceID"/>
                                       </c:field>
                                       <c:field lov="LOV_RegionLevel3" name="address.stRegionalID2" caption="Kecamatan" width="200" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stRegionalID1"/>
                                       </c:field>
                                       <c:field lov="LOV_RegionLevel4" name="address.stRegionalID3" caption="Kelurahan" width="200" type="string" presentation="standard" >
                                          <c:lovLink name="parent" link="address.stRegionalID2"/>
                                       </c:field>
                                       --%>
                                       
                                       <c:field name="address.stRegionalID4" caption="RT/RW" type="string|255" presentation="standard" />
                                       <c:field lov="LOV_PostalCode" mandatory="false" readonly="false" width="300" name="address.stRegionMapID" caption="{L-ENGPost Code-L}{L-INAKode Pos-L}"  popuplov="true" type="string|10" presentation="standard" />
                                     
                                       <c:field lov="VS_ENT_ADDR_TYPE" name="address.stAddressType" caption="{L-ENGAddress Type-L}{L-INAJenis Alamat-L}" type="string" presentation="standard" />
                                       <c:field name="address.stPrimaryFlag" caption="{L-ENGPrimary Address-L}{L-INAAlamat Utama-L}" type="check" presentation="standard" />
                                       <c:field name="address.stMailingFlag" caption="{L-ENGMailing Address-L}{L-INAAlamat Surat-L}" type="check" presentation="standard" />
                             
                                                          

                                    </table>
                                 </td>
                                 <td>
                                    <table cellpadding=2 cellspacing=1>
                                    <c:field name="address.stPhone" caption="{L-ENGPhone-L}{L-INANo. Telpon-L}" type="string|32" presentation="standard" />
                
                                    	              <c:field name="address.stPhoneMobile" caption="{L-ENGMobile-L}{L-INAHp-L}" type="string|32" presentation="standard" />
                                       <c:field name="address.stPhoneFax" caption="Fax" type="string|32" presentation="standard" />
                                       <c:field name="address.stEmail" caption="Email" type="email|128" presentation="standard" />
                                       <c:field name="address.stWebsite" caption="Website" type="string|255" presentation="standard" />
                                       <c:field lov="VS_ENT_OWNERSHIP" name="address.stOwnershipCode" caption="Property Ownership" type="string" presentation="standard"/>
                                       <c:field name="address.dtOccupiedDate" caption="Occupied From" type="date" presentation="standard"/>
                                    </table>
                                 </td>
                              </tr>
                           </table>
                        </td>
                     </tr>
                  </c:evaluate>
               </table>
            </c:tabpage>

            <c:tabpage name="TAB2">
                <table cellpadding=2 cellspacing=1 class=row0>
                    <tr>
                        <td colspan=3 class=header>KLAIM</td>
                    </tr>
                    <c:field lov="LOV_ARTax" name="entity.stClaimTaxCode" caption="Pajak Jasa Bengkel" width="200" type="string" mandatory="true" presentation="standard" />
                    <c:field name="entity.stFilePhysic" caption="File" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                </table>
               
            </c:tabpage>

            <c:tabpage name="TAB_ENT_DOCUMENTS">

                <table cellpadding=2 cellspacing=1 class=header width="100%">
                    <tr>
                        <td>
                            DOKUMEN
                        </td>
                    </tr>
                    <tr>
                        <td class=row0>
                            <c:listbox name="entity.entityDocuments">
                                <c:listcol title="">
                                    <c:field name="entity.entityDocuments.[$index$].stSelectedFlag" type="check"
                                             mandatory="false" readonly="false"/>
                                </c:listcol>
                                <c:listcol name="stDescription" title="Keterangan">
                                </c:listcol>
                                <c:listcol title="Dokumen">
                                    <c:field name="entity.entityDocuments.[$index$].stFilePhysic" type="file"
                                             thumbnail="true" caption="File"/>
                                </c:listcol>
                            </c:listbox>
                        </td>
                    </tr>
                </table>

            </c:tabpage>
         </c:tab>
      </td>
   </tr>
   <tr>
      <td>
         <c:evaluate when="<%=!ro%>" >
            
            <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="doSave" validate="true"/>
            <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="doClose" validate="false"/>
         </c:evaluate>
         <c:evaluate when="<%=ro%>" >
            <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="doClose" validate="false"/>
         </c:evaluate>
      </td>
   </tr>
</table>
</c:frame>
<script>
function cekGroup() {
        f.action_event.value = 'changeGroupPerusahaan';
        f.submit();
    }
</script>
