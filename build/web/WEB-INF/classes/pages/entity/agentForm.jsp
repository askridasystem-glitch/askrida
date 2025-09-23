<%@ page import="com.webfin.entity.forms.EntityAgentForm,
         com.crux.util.JSPUtil,
         com.crux.web.controller.SessionManager,
         com.crux.ff.model.FlexTableView,
         com.webfin.entity.model.EntityView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="DATA AGEN" >
    <%
                final EntityAgentForm form = (EntityAgentForm) request.getAttribute("FORM");
                final boolean entityMandatory = false;

                final EntityView ent = form.getEntity();

                final boolean individual = ent.isIndividual();
                final boolean institutional = ent.isInstitutional();

                boolean ro = form.isReadOnly();

                /*
                boolean bpdReadOnly = false;
                String bpd = "Y";
                if (ent.getStCategory1() != null) {
                if (ent.getStCategory1().equalsIgnoreCase("4")) {
                bpdReadOnly = true;
                bpd = "N";
                }
                }
                 */

                final boolean isSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");


    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="entity.stEntityID" caption="Entity ID" type="integer" readonly="true" presentation="standard" flags="auto3"/>
                                

                                <%--<c:field lov="LOV_CustCategory1" readonly="<%=ent.getStCategory1() != null%>" changeaction="onCategoryChange" mandatory="true" name="entity.stCategory1" caption="{L-ENGCategory-L}{L-INAKategori-L}" type="string" width="200" presentation="standard" />
                                <c:evaluate when="<%=ent.getStCategory1() != null%>">
                                    <c:field lov="LOV_CompType" width="230" readonly="<%=ent.getStGLCode() != null%>" mandatory="true" name="entity.stRef1" changeaction="changeJenisPerusahaan" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255" presentation="standard" >
                                        <c:param name="custcat" value="<%=bpd%>" />
                                    </c:field>
                                    <c:field popuplov="true" lov="LOV_COMPANYGROUP" width="230" clientchangeaction="cekGroup()" mandatory="true" name="entity.stRef2" caption="{L-ENGCompany Group-L}{L-INAGrup Perusahaan-L}" type="string|255" presentation="standard" >
                                        <c:param name="custcat" value="<%=bpd%>" />
                                    </c:field>
                                </c:evaluate>
                                <c:field name="entity.stReferenceEntityID" popuplov="true" lov="LOV_Entity" mandatory="false" caption="{L-ENGReference Entity-L}{L-INACustomer Induk-L}" width="250" type="string|50" presentation="standard" />
                                --%>

                                <c:field lov="LOV_CustCategory1" readonly="true" mandatory="true" name="entity.stCategory1" caption="{L-ENGCategory-L}{L-INAKategori-L}" type="string" width="200" presentation="standard" />
                                <c:field lov="LOV_CompType" width="230" readonly="true" mandatory="true" name="entity.stRef1" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255" presentation="standard" />
                                <c:field lov="LOV_COMPANYGROUP" width="230" readonly="true" mandatory="true" name="entity.stRef2" caption="{L-ENGCompany Group-L}{L-INAGrup Perusahaan-L}" type="string|255" presentation="standard" />
                                <c:field lov="LOV_Branch" mandatory="true" width="200" caption="Branch" name="entity.stCostCenterCode" readonly="<%=ent.getStCostCenterCode() != null%>" type="string" presentation="standard" changeaction="onChangeBranchGroup"/>
                                <c:field show="<%=ent.getStCostCenterCode() != null%>" width="200" caption="Region" lov="LOV_Region" name="entity.stRegionID" readonly="<%=ent.getStRegionID() != null%>" type="string" presentation="standard" mandatory="true" changeaction="select_approval">
                                    <c:lovLink name="cc_code" link="entity.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:field lov="LOV_CustomerClass" mandatory="true" changeaction="onClassChange" name="entity.stEntityClass" caption="{L-ENGAgent Class-L}{L-INAKelas Agen-L}" type="string|32" width="200" presentation="standard" />
                                 <c:field name="entity.stAgentID" mandatory="false" caption="{L-ENGAgent ID-L}{L-INAID Agen-L}" width="250" type="string|32" presentation="standard"/>
                                <c:field name="entity.stEntityName" rows="3" mandatory="true" caption="{L-ENGAgent Name-L}{L-INANama Agen-L}" width="250" type="string|255" presentation="standard"/>
                                <c:field lov="LOV_EntityLevel" mandatory="true" name="entity.stEntityLevel" caption="{L-ENGAgent Level-L}{L-INALevel Agen-L}" type="string" width="200" presentation="standard" />
                                <%--
                                <c:field width="200" name="entity.stBusinessLine" caption="{L-ENGLine Of Business-L}{L-INAJenis Bisnis-L}" type="string|32" presentation="standard" include="<%=institutional%>"/>
                                <c:field lov="VS_ENT_CUST_STATUS" name="entity.stCustomerStatus" caption="{L-ENGCustomer Status-L}{L-INAStatus Customer-L}" type="string|255" width="200" presentation="standard" />
                                <c:field name="entity.stFunctionaryName" mandatory="false" caption="{L-ENGFunctionary Name-L}{L-INANama Pejabat-L}" width="230" type="string|50" presentation="standard" include="<%=institutional%>"/>
                                <c:field name="entity.stFunctionaryPosition" mandatory="false" caption="{L-ENGFunctionary Position-L}{L-INAJabatan-L}" width="230" type="string|50" presentation="standard" include="<%=institutional%>"/>
                                <c:field name="entity.stRcNo" caption="{L-ENGBank RC No.-L}{L-INANomor Rekening-L}" width="230" type="string|50" presentation="standard" include="<%=institutional%>"/>
                                
                                <tr>
                                    <td>
                                        <c:button text="Refresh" validate="true" event="select_approval"/>
                                    </td>
                                </tr>
                                --%>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                               
                                <c:field name="entity.stSupervisiCode" popuplov="true" lov="LOV_Entity" mandatory="false" caption="{L-ENGSupervisi Agen-L}{L-INASupervisi Agen-L}" width="250" type="string|32" presentation="standard"/>
                                <c:field lov="VS_TAX_CODE" name="entity.stTaxCode" caption="Alokasi Pajak" type="string" mandatory="true" presentation="standard" />
                                <c:field name="entity.stTaxFile" width="250" caption="{L-ENGTax Number-L}{L-INANPWP-L}" type="string|32" presentation="standard" />
                                <%--<c:field name="entity.stARTermID" lov="lovPaymentTerm" caption="Payment Term" type="integer" presentation="standard" />
                                <c:field name="entity.stAPTermID" lov="lovPaymentTerm" caption="Claim Payment Term" type="integer" presentation="standard" />
                                
                                <c:field lov="LOV_Country" name="entity.stCountryID" caption="{L-ENGNationality-L}{L-INAKewarganegaraan-L}" width="250" type="string" presentation="standard" />--%>
                                <c:field lov="VS_ENT_ID_TYPE" width="100" name="entity.stIdentificationType" caption="{L-ENGIdentification-L}{L-INAPengenal-L}" type="string|10" presentation="standard" />
                                <c:field name="entity.stIdentificationNumber" caption="{L-ENGIdentification Number-L}{L-INANo. Pengenal-L}" type="string" width="250" presentation="standard"/>
                                <c:field name="entity.dtActiveDate" caption="Tanggal Perekrutan" type="date" presentation="standard" />
                                <%--<c:field lov="VS_ENT_MARITAL" name="entity.stMaritalStatus" caption="{L-ENGMarital Status-L}{L-INAStatus Perkawinan-L}" type="string|10" presentation="standard" include="<%=individual%>"/>
                                <c:field name="entity.lgDependentNum" caption="Number of Dependent" width="50" type="integer" presentation="standard" include="<%=individual%>"/>
                                <c:field lov="LOV_Religion" name="entity.stReligionCode" caption="{L-ENGReligion-L}{L-INAAgama-L}" type="string|10" presentation="standard" include="<%=individual%>"/>
                                <c:field name="entity.stSalesTax" caption="Sales Tax" width="60" type="string|10" presentation="standard" />--%>
                                <%--<c:field name="entity.dtBirthDate" caption="{L-ENGBirth Date-L}{L-INATanggal Lahir-L}" type="date" presentation="standard" include="<%=individual%>"/>
                                
                                <c:field name="entity.stGLCode" readonly="true" caption="GL Code" type="string" presentation="standard"/>
                                <c:field name="entity.stInsInwardFlag" caption="Inward Enabled" type="check" presentation="standard" include="<%=institutional%>"/>
                                <c:field name="entity.stInsOutwardFlag" caption="Outward Enabled" type="check" presentation="standard" include="<%=institutional%>"/>
                                <c:field lov="LOV_CustomerShareLevel" name="entity.stShareLevel" caption="Share Level" type="string" presentation="standard"/>
                                <c:field name="entity.stInsCompanyFlag" caption="{L-ENGInsurance Company-L}{L-INAPerusahaan Asuransi-L}" type="check" presentation="standard" include="<%=institutional%>"/>
                                <c:field name="entity.stFinanceFlag" caption="{L-ENGFinance-L}{L-INAKeuangan (Active)-L}" type="check" presentation="standard" />
                                <c:field name="entity.stCaptiveFlag" caption="Captive" type="check" presentation="standard" include="<%=institutional%>"/>--%>
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
                                    <c:button text="+" event="doNewAddress" enabled="<%=!ro%>" />
                                    <c:button text="-" event="doDeleteAddress" enabled="<%=!ro%>"/>
                                </td>
                            </tr>
                            <c:evaluate when="<%=form.getAddress() != null%>">
                                <tr>
                                    <td colspan=3 class=header>
                                        {L-ENGADDRESS-L}{L-INAALAMAT-L}
                                        <table cellpadding=2 cellspacing=1 class=row0>
                                            <tr>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <%--<c:field name="address.stPredefRiskCode" caption="Predefined Risk Address" type="string" presentation="standard" />--%>
                                                        <c:field mandatory="true" name="address.stAddress" rows="3" width="300" caption="{L-ENGStreet Address-L}{L-INAAlamat Jalan-L}" type="string|255" presentation="standard" />
                                                        <%--<c:field lov="LOV_Branch" width="200" mandatory="true" name="address.stCostCenterCode" caption="{L-ENGBranch Author-L}{L-INAData Cabang-L}" type="string|32" presentation="standard" />

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
                                                        <c:field lov="LOV_PostalCode" mandatory="false" readonly="false" width="300" name="address.stRegionMapID" caption="{L-ENGPost Code-L}{L-INAKode Pos-L}" popuplov="true" type="string|10" presentation="standard" />

                                                        <c:field lov="VS_ENT_ADDR_TYPE" name="address.stAddressType" caption="{L-ENGAddress Type-L}{L-INAJenis Alamat-L}" type="string" presentation="standard" />
                                                        <c:field name="address.stPrimaryFlag" caption="{L-ENGPrimary Address-L}{L-INAAlamat Utama-L}" type="check" presentation="standard" />
                                                        <%--<c:field name="address.stMailingFlag" caption="{L-ENGMailing Address-L}{L-INAAlamat Surat-L}" type="check" presentation="standard" />
                                                        --%>


                                                    </table>
                                                </td>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <c:field name="address.stPhone" width="200" caption="{L-ENGPhone-L}{L-INANo. Telpon-L}" type="string|32" presentation="standard" />

                                                        <c:field name="address.stPhoneMobile" width="200" caption="{L-ENGMobile-L}{L-INAHp-L}" type="string|32" presentation="standard" />
                                                        <c:field name="address.stPhoneFax" width="200" caption="Fax" type="string|32" presentation="standard" />
                                                        <c:field name="address.stEmail" width="200" caption="Email" type="email|128" presentation="standard" />
                                                        <c:field name="address.stWebsite" width="200" caption="Website" type="string|255" presentation="standard" />
                                                        <%--<c:field lov="VS_ENT_OWNERSHIP" name="address.stOwnershipCode" caption="Property Ownership" type="string" presentation="standard"/>
                                                        <c:field name="address.dtOccupiedDate" caption="Occupied From" type="date" presentation="standard"/>--%>
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
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td colspan=3 class=header>
                                    {L-ENGPRODUCT TYPE-L}{L-INAJENIS PRODUK-L}
                                    <table cellpadding=2 cellspacing=1 class=row0>
                                        <tr>
                                            <td>
                                                <table cellpadding=2 cellspacing=1>
                                                    <tr>
                                                        <td>
                                                            <c:evaluate when="<%=form.getList() != null%>" >
                                                                <c:listbox name="list">
                                                                    <%
                                                                                final FlexTableView ff = (FlexTableView) current;
                                                                    %>
                                                                    <c:listcol title="" columnClass="header">
                                                                        <c:button text="+" event="addLineClaim" validate="false" defaultRO="true"/>
                                                                    </c:listcol>
                                                                    <c:listcol title="" columnClass="detail">

                                                                    </c:listcol>
                                                                    <c:listcol title="Jenis" >
                                                                        <c:field name="list.[$index$].stReference3" lov="LOV_PolicyTypeSgl" popuplov="true" clientchangeaction="refresh()" caption="Kode" type="string" width="200" />
                                                                    </c:listcol>
                                                                    <c:listcol title="Target" >
                                                                        <c:field name="list.[$index$].dbReference1" caption="Limit1" type="money16.2" width="150" />
                                                                    </c:listcol>
                                                                    <c:listcol title="Tahun" >
                                                                        <c:field name="list.[$index$].stReference4" lov="LOV_GL_Years2" caption="Active Date" type="string" readonly="false" />
                                                                    </c:listcol>
                                                                </c:listbox>
                                                            </c:evaluate>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </c:tabpage>

                    <c:tabpage name="TAB3">
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td colspan=3 class=header>
                                    {L-ENGDOCUMENTS-L}{L-INDOKUMEN-L}
                                    <table cellpadding=2 cellspacing=1 class=row0>
                                        <tr>
                                            <td>
                                                <table cellpadding=2 cellspacing=1>
                                                    <c:field name="entity.stIDCard"  width="200" caption="Kartu ID Keagenan" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                                    <c:field name="entity.stPKSCard" width="200" caption="PKS (Perjanjian Kerjasama)" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                                    <c:field name="entity.stOJKCard" width="200" caption="Tanda Daftar Di OJK" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
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

                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="doSaveAgent" validate="true"/>
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

    function refresh() {
        f.action_event.value = 'refresh';
        f.submit();
    }
</script>
