<%@ page import="com.crux.web.controller.SessionManager,
com.webfin.entity.forms.EntityListForm,
com.webfin.entity.model.EntityView,
java.util.ArrayList,
com.crux.util.DTOList,
com.crux.util.SQLAssembler,
com.crux.util.JSPUtil,
com.crux.util.fop.FOPUtil,
com.crux.lang.LanguageManager"%><?xml version="1.0" ?>


<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="2cm"/> 
            <fo:region-before extent="2cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first">     
        
        <%
        
        EntityListForm form =  (EntityListForm) request.getAttribute("FORM");
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(10));
        colW.add(new Integer(20));
        colW.add(new Integer(100));
        colW.add(new Integer(140));
        //colW.add(new Integer(20));
        
        SQLAssembler sqa = form.getSQA();
        
        sqa.clearOrder();
        sqa.addOrder("ent_id asc");
        
        DTOList list = sqa.getList(EntityListForm.class);
        
        %>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">DATA CUSTOMER</fo:block>
            <%--
            <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="8pt">
                
                <% if (form.getBranch()!=null) {%>
                <fo:block>
                    Cabang : <%=JSPUtil.printX(form.getBranch())%>
                </fo:block>
                <% } %>
                
                <% if (form.getStCustomerStatus()!=null) {%>
                <fo:block>
                    Status : <%=JSPUtil.printX(form.getStCustomerStatus())%>
                </fo:block>
                <% } %>
                
            </fo:block>
            --%>
            <fo:block space-after.optimum="14pt"/>
            
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="14mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">ID</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NAMA CUSTOMER</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">ALAMAT CUSTOMER</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KATEGORI</fo:block></fo:table-cell>                    
                        </fo:table-row>
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,20,2,"cm")%>
                    <fo:table-body>
                        
                        <%
                        for (int i = 0; i < list.size(); i++) {
                EntityView jv = (EntityView) list.get(i);
                        %>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.print(jv.getStEntityID())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.print(jv.getStEntityName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.print(jv.getStAddress())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(LanguageManager.getInstance().translate(jv.getValueSet(jv.getStCategory1()).getStVDesc()))%></fo:block></fo:table-cell>
                        </fo:table-row  >
                        
                        <% } %>
                        
                    </fo:table-body>
                </fo:table> 
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

