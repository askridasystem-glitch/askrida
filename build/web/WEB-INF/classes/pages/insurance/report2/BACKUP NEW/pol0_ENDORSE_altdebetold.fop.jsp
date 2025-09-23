<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView,
         com.webfin.ar.model.ARTaxView,
         com.crux.common.parameter.Parameter,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");

            boolean effective = pol.isEffective();

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }
            String originalWatermarkPath = Parameter.readString("DIGITAL_NOTA_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_NOTA_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_NOTA_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");
            String alamatLogoPath = Parameter.readString("DIGITAL_NOTA_ALAMAT_PIC");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_NOTA_ALAMATBW_PIC");

//if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <%
                String watermarkPath = null;
                String logoAskridaPath = null;
                String alamatAskridaPath = null;

                for (int x = 0; x < 3; x++) {

                    if (x == 0) {
                        watermarkPath = originalWatermarkPath;
                        logoAskridaPath = askridaLogoPath;
                        alamatAskridaPath = alamatLogoPath;
                    } else if (x == 1) {
                        watermarkPath = copyWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    } else if (x == 2) {
                        watermarkPath = duplicateWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    }



    %>

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->


    <!-- actual layout -->
    <fo:page-sequence master-reference="first"> 
        <%--  
	<fo:static-content flow-name="xsl-region-before">
		<fo:block text-align="right"
            font-size="16pt"

            line-height="12pt" 
            font-weight="bold">
        	{L-ENG DEBET NOTE -L}{L-INA NOTA DEBET -L}
      	</fo:block>
    </fo:static-content>  	

   <fo:static-content flow-name="xsl-region-before">
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="message"
      retrieve-boundary="page"
      retrieve-position="first-starting-within-page"/>
      </fo:block>
</fo:block-container>
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="term"
      retrieve-boundary="page"
      retrieve-position="last-ending-within-page"/>
      </fo:block>
</fo:block-container>
    </fo:static-content>

  <fo:static-content flow-name="xsl-region-after">
    <fo:block text-align="end"
      font-size="6pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content> --%>


        <fo:flow flow-name="xsl-region-body">


            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>


            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>    

            <!-- ROW -->
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>


            <fo:block-container height="2cm" width="14cm" top="3.5cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=JSPUtil.xmlEscape(pol.getStCustomerName())%>
                </fo:block>
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="1cm" width="5cm" top="1.5cm" left="15cm" padding="1mm" position="absolute">
                <fo:block text-align="right"
                          font-size="16pt"

                          line-height="12pt" 
                          font-weight="bold">
                    {L-ENG DEBET NOTE -L}{L-INA NOTA DEBET -L}
                </fo:block>
            </fo:block-container>

            <fo:block-container  height="1.5cm" width="3.5cm" top="3cm" left="15cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.7cm"/>
                    <fo:table-column column-width="3.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">    

                        <fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start">F<%=pol.getStPolicyNo().substring(2, 4) + "" + pol.getStPolicyNo().substring(10, 12) + "" + pol.getStPolicyNo().substring(0, 2) + "" + pol.getStPolicyNo().substring(4, 6) + "" + pol.getStPolicyNo().substring(12, 16)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"><%=pol.getStProducerID()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>


            <fo:block-container height="0.5cm" width="4.5cm" top="6cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="6cm" top="6cm" left="6cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="2.3cm" top="6cm" left="12cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d-MM-yyyy")%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="2.5cm" top="6cm" left="14.3cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d-MM-yyyy")%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="6cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbInsuredAmountEndorse(), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container> 

            <fo:block-container height="0.5cm" width="4cm" top="7cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container>      

            <fo:block-container height="0.5cm" width="4cm" top="8.5cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDDisc1(), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="10cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDPCost(), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="11.2cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDSFee(), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container>

            <fo:block-container height="1.25cm" width="15cm" top="12cm" left="1.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column />
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">- Mohon dikredit pada rekening kami/dibayar perkas/juru tagih kami</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">- Mohon dicantum No. Polis pada setiap pembayaran</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table> 
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="12.5cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(), pol.getDbNDPPN()), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table> 
            </fo:block-container>  

        </fo:flow>
    </fo:page-sequence>
    <%
                }
    %>
</fo:root>