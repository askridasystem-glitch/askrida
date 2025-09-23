<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
com.crux.util.Tools,
java.math.BigDecimal,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String otorized = (String) request.getAttribute("authorized");
String objectName = null;
String objectID = null;
String claimLoss = null;
String coinsName = null;
Date tglMulaiKreasi=null;
Date tglAkhirKreasi=null;

String cekLKP = pol.getStDLANo();

boolean effective = pol.isEffective();

//if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            
            <fo:region-body margin-top="4.5cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">
            
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-INA Klaim Pasti Asuransi-L}<%=pol.getStPolicyTypeDesc2()%>{L-ENG Definitive Loss Advice-L}
            </fo:block>
            
            <fo:block font-size="12pt" line-height="10pt" space-before.optimum="5pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=pol.getStDLANo()%></fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt" ></fo:block>
            
            <% if (cekLKP!=null) { %>            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <% 
                        final DTOList objects3 = pol.getObjectsClaim();
                        for(int j = 0;j<objects3.size();j++){
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects3.get(j);
                            
                            tglMulaiKreasi = obj.getDtReference2();
                            
                            tglAkhirKreasi = obj.getDtReference3();
                        }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%> Hari</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <%if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(tglMulaiKreasi,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }else{%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }%>           
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <%if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(tglAkhirKreasi,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }else{%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }%>           
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <%
            final FlexFieldHeaderView polClaimMap = pol.getClaimFF();
            
            final DTOList polClaimMapDetails = polClaimMap==null?null:polClaimMap.getDetails();
            
            if (polClaimMapDetails !=null) {
            
            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                        
                        
                        for (int j = 0; j < polClaimMapDetails.size(); j++) {
                            FlexFieldDetailView iomd = (FlexFieldDetailView) polClaimMapDetails.get(j);
                            
                            final Object desc = iomd.getDesc(pol);
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% } %>
            
            
            
            
            
            
            <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt">
                {L-ENG INTEREST INSURED-L}{L-INA OBJEK PERTANGGUNGAN-L}
            </fo:block>
            
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>
            
            <%
            final DTOList objects = pol.getObjectsClaim();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);
                
                final FlexFieldHeaderView objectMap = io.getClaimFF();
                
                final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();
                
                claimLoss = io.getClaimLossDesc();
            
            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                        
                        if (objectMapDetails!=null)
                            for (int j = 0; j < objectMapDetails.size(); j++) {
                    FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);
                    
                    final Object desc = iomd.getDesc(io);
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                            }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="70mm"/>
                                            <fo:table-body>
                                                <%
                                                final DTOList suminsureds = io.getSuminsureds();
                                                
                                                for (int j = 0; j < suminsureds.size(); j++) {
                                                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                                                %>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(tsi.getStInsuranceTSIDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%
                                                
                                                }
                                                %>
                                                
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Coverage -L}{L-INA Jaminan-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column />
                                            <fo:table-body>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>
                                                            <%
                                                            final DTOList coverage = io.getCoverage();
                                                            
                                                            for (int j = 0; j < coverage.size(); j++) {
                                                                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);
                                                                
                                                                if (j>0) out.print("; ");
                                                                out.print(JSPUtil.print(cover.getStInsCoverDesc()));
                                                            
                                                            %>
                                                            
                                                            <%
                                                            
                                                            }
                                                            %>                                                        
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                    
                                    <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
                                    
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                final DTOList deductibles = io.getDeductibles();
                                                
                                                for (int j = 0; j < deductibles.size(); j++) {
                                                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                                                
                                                %>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStAutoDesc())%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                   <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                                <%
                                                
                                                }
                                                %>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"> </fo:block>
            <%} %>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="-2mm"/>
                                        <fo:table-column column-width="71mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-body>
                                            <%
                                            
                                            final DTOList details = pol.getDetails();
                                            
                                            %>
                                            
                                            
                                            <%
                                            
                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                if (!item.isDiscount()) continue;
                                                
                                                String desc = item.getStDescription2();
                                                
                                                if (item.isEntryByRate()) desc+=JSPUtil.printX(item.getDbRate()+" %");
                                            
                                            %>
                                            <% } %>
                                            
                                            
                                            
                                            <%
                                            
                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                
                                                if (!item.isFee()) continue;
                                            %>
                                            <% } %>
                                            
                                            
                                            
                                            
                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>
            
            <!-- GARIS  -->
            <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.50pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <% 
                        final DTOList objects2 = pol.getObjectsClaim();
                        for(int j = 0;j<objects2.size();j++){
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);
                            
                            if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10"))
                                objectName = obj.getStReference2();
                            else
                                objectName = obj.getStReference1();
                            
                            objectID = obj.getStPolicyObjectID();
                        }
                        %>
                        
                        <% if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Credit Type-L}{L-INA Jenis Kredit-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStKreasiTypeDesc().replaceAll(pol.getStKreasiTypeDesc().substring(0,4)," ")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%}%>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Object-L}{L-INA Objek Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=objectName%> Object ID : <%=objectID%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG PLA No.-L}{L-INA No. LKS-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPLANo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG DLA Remark-L}{L-INA Ket. LKP-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStDLARemark()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Date-L}{L-INA Tanggal Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% DTOList cause = pol.getClaimCause();
                        for(int k = 0; k < cause.size(); k++) {
                            InsuranceClaimCauseView cau = (InsuranceClaimCauseView) cause.get(k);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Cause-L}{L-INA Penyebab Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cau.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Event Location-L}{L-INA Lokasi Terjadi Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Person Name-L}{L-INA Nama Pengaju Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Person Address-L}{L-INA Alamat Pengaju Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Estimated Amount-L}{L-INA Jumlah Perkiraan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmountEstimate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Amount Currency-L}{L-INA Mata Uang-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Loss Status-L}{L-INA Status Kerugian-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(claimLoss)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Benefit-L}{L-INA Pembayaran Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimBenefitDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Documents-L}{L-INA Dokumen Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimDocuments())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                        
                        final DTOList claimItems = pol.getClaimItems();
                        BigDecimal amt3 = new BigDecimal(0);
                        for (int i = 0; i < claimItems.size(); i++) {
                            InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);
                            
                            final boolean negative = ci.getInsItem().getARTrxLine().isNegative();
                            
                            if (negative) amt3 = BDUtil.sub(amt3,ci.getDbAmount());
                            else amt3 = BDUtil.add(amt3,ci.getDbAmount());
                            
                            String amt = JSPUtil.printX(ci.getDbAmount(),2);
                            
                            if (negative) amt="("+amt+")";
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ci.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=amt%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %>
                        
                        <%
                        
                        BigDecimal tax3 = new BigDecimal(0);
                        for (int i = 0; i < claimItems.size(); i++) {
                            InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);
                            
                            if (!ci.getStInsItemID().equalsIgnoreCase("70")) continue;
                                                        
                            tax3 = ci.getDbTaxAmount();
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>PPh 23 - Jasa Bengkel</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">(<%=JSPUtil.printX(tax3,2)%>)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %>
                        
                        <fo:table-row>		
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt" text-align="end"><%=JSPUtil.printX(BDUtil.sub(amt3,tax3),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <%--<% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUTSELF")) { %>	--%>
            
            <% 
            final DTOList coins = pol.getCoins2();
            if (coins.size()>1) {
            %>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed" space-before.optimum="10pt" space-after.optimum="10pt">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" >No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Nama</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Share</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center" >Klaim</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	   
                        
                        <% 
                        //final DTOList coins = pol.getCoins2();
                        for (int f = 0; f < coins.size(); f++) {
                            InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(f);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" ><%= String.valueOf(f+1) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= coi.getStEntityName() %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%= JSPUtil.printX(coi.getDbSharePct(),2)%> %</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%= JSPUtil.printX(coi.getDbClaimAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	  
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <% } %>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintCode:<%=pol.getStPrintCode()%> PrintStamp:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="15pt"></fo:block>   
            
            <% if (otorized.equalsIgnoreCase("kasie")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <%       
                        if (SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%       
                        } else { 
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">MENYETUJUI,</fo:block>    
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else { %>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% } %>    
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">IMRAN</fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% } %>
            
            <% if (otorized.equalsIgnoreCase("kabag")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <%       
                        if (SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%       
                        } else { 
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else { %>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% } %>    
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
            <% if (otorized.equalsIgnoreCase("kadiv")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>             
                            <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN BONDING,</fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell> 
                            <% } %>    
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% } %>
                            </fo:table-cell>             
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
            <% if (otorized.equalsIgnoreCase("dirtek")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENGETAHUI,</fo:block></fo:table-cell>  
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>           
                            <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN BONDING,</fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell> 
                            <% } %>    
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% } %>
                            </fo:table-cell>    
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">RIA GAYATRI</fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>        
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% } %>
            
            <% } %>
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>



