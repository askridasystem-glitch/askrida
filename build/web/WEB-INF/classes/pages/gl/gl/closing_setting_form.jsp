<%@ page import="com.webfin.gl.model.ClosingHeaderView,
         com.webfin.gl.model.ClosingDetailView,
         com.crux.util.DateUtil,
com.crux.lov.LOVManager,
java.util.Date,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
 java.text.SimpleDateFormat,
com.webfin.gl.form.ClosingSettingForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    ClosingSettingForm form = (ClosingSettingForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final ClosingHeaderView posting = form.getPosting();

    boolean isFinal = form.isFinalMode();

    SimpleDateFormat cdf = new SimpleDateFormat("dd/MM/yyyy");
    
    %>
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="70" caption="GL Post ID" name="posting.stClosingPeriodID" type="string" readonly="true" presentation="standard"/>
                    <%--<c:field width="120" mandatory="true" caption="Bulan" name="posting.stPeriodNum" readonly="<%=posting.getStPeriodNum()!=null%>" lov="LOV_GL_Period" type="string" presentation="standard"/>
                    --%>
                    <c:field width="120" mandatory="true" caption="Tahun" name="posting.stFiscalYear" readonly="<%=posting.getStFiscalYear()!=null%>" lov="LOV_GL_Years2" type="string" type="string" presentation="standard" />
                    <c:field width="200" mandatory="true" caption="Time Zone" name="posting.stTimeZone" readonly="<%=posting.getStTimeZone()!=null%>" lov="VS_TIMEZONE" type="string" presentation="standard"/>
                    <c:field width="200" mandatory="true" caption="Cabang" name="posting.stCostCenterCode" readonly="<%=posting.getStCostCenterCode()!=null%>" lov="LOV_CostCenter" changeaction="changeBranch" loveall="true" type="string" presentation="standard"/>
                    <c:field width="200" mandatory="true" caption="Aktif" name="posting.stActiveFlag" type="check" presentation="standard"/>
                    <%--<tr>
                        <td colspan="3"><c:button show="<%=posting.getStCostCenterCode()!=null && !ro%>" text="Add Detail" event="doNewDetail" validate="true"/></td>
                    </tr>--%>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:evaluate when="<%=posting.getStCostCenterCode()!=null%>">
                <c:field name="notesindex" hidden="true" type="string"/>
                <c:listbox name="details" >
                    <%
                            final ClosingDetailView detail = (ClosingDetailView) current;

                     %>
                    <c:listcol title="" columnClass="header" >
                    </c:listcol>
                    <c:listcol title="" columnClass="detail" >
                        
                    </c:listcol>
                        <%--
                    <c:listcol title="Start Date" >
                        <c:field mandatory="true" name="details.[$index$].dtStartDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                    </c:listcol>
                    <c:listcol title="End Date" >
                        <c:field mandatory="true" name="details.[$index$].dtEndDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                    </c:listcol>
                    <c:listcol title="Input Start Date" >
                        <c:field mandatory="true" name="details.[$index$].dtEditStartDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                    </c:listcol>
                    <c:listcol title="Input End Date - Time" >
                        <c:field mandatory="true" name="details.[$index$].dtEditEndDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                        <c:field mandatory="true" name="details.[$index$].stEditEndTime" width="50" caption="Deskripsi" type="string" readonly="false" suffix=" WIB " />
                    </c:listcol>
                     <c:listcol title="Approve Start Date" >
                        <c:field mandatory="true" name="details.[$index$].dtReverseStartDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                    </c:listcol>
                     <c:listcol title="Approve End Date - Time" >
                        <c:field mandatory="true" name="details.[$index$].dtReverseEndDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                        <c:field mandatory="true" name="details.[$index$].stReverseEndTime" width="50" caption="Deskripsi" type="string" readonly="false" suffix=" WIB "/>
                    </c:listcol>
                    <c:listcol title="Close Date" >
                        <c:field mandatory="true" name="details.[$index$].dtClosedDate" width="140" caption="Deskripsi" type="date" readonly="false" />
                        <c:field mandatory="true" name="details.[$index$].stCloseTime" width="50" caption="Deskripsi" type="string" readonly="false" />
                    </c:listcol>--%>
                        <c:listcol title="Input End Time" >
                            <c:field mandatory="true" name="details.[$index$].stEditEndTime" width="50" caption="Deskripsi" type="string" readonly="false" suffix=" WIB " />
                        </c:listcol>
                        <c:listcol title="Approve End Time" >
                            <c:field mandatory="true" name="details.[$index$].stReverseEndTime" width="50" caption="Deskripsi" type="string" readonly="false" suffix=" WIB "/>
                        </c:listcol>
                        <c:listcol title="Close Time" >
                            <c:field mandatory="true" name="details.[$index$].stCloseTime" width="50" caption="Deskripsi" type="string" readonly="false" suffix=" WIB " />
                        </c:listcol>
                    
                </c:listbox>
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:evaluate when="<%=!isFinal%>">
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" validate="true"/>
                    </c:evaluate>
                    <c:evaluate when="<%=isFinal%>">
                        <c:button text="{L-ENGSave Final-L}{L-INASimpan Final-L}" event="saveFinalisasi" validate="true"/>
                    </c:evaluate>
                    
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>