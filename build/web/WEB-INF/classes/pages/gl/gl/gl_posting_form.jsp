<%@ page import="com.webfin.gl.model.GLPostingView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.form.GLPostingForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    GLPostingForm form = (GLPostingForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final GLPostingView posting = form.getPosting();

    boolean isFinal = form.isFinalMode();
    
    %>
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="70" caption="GL Post ID" name="posting.stGLPostingID" type="string" readonly="true" presentation="standard"/>
                    <c:field width="120" mandatory="true" caption="Bulan" name="posting.stMonths" readonly="<%=isFinal%>" lov="LOV_MONTH_Period" type="string" presentation="standard"/>
                    <c:field width="120" mandatory="true" caption="Tahun" name="posting.stYears" readonly="<%=isFinal%>" lov="LOV_GL_Years2" type="string" type="string" presentation="standard" />
                    <c:field width="200"  caption="Cabang" name="posting.stCostCenterCode" readonly="<%=isFinal%>" lov="LOV_CostCenter" loveall="true" type="string" presentation="standard"/>
                    <%--<c:field width="200"  caption="Status" name="posting.stStatus" type="string" readonly="true" presentation="standard"/>
                    --%>
                    <tr>
                        <td>Posting Flag</td>
                        <td>:</td>
                        <td><c:field width="100"  caption="Posting" readonly="<%=isFinal%>" name="posting.stPostedFlag" type="string" type="check" /> *Centang untuk posting</td>
                    </tr>
                    <c:evaluate when="<%=isFinal%>">
                        <tr>
                            <td>Finalisasi Flag</td>
                            <td>:</td>
                            <td><c:field width="100" caption="Posting" name="posting.stFinalFlag" type="string" type="check" /> *Centang untuk final</td>
                        </tr>
                    </c:evaluate>
                    
                    <c:field width="400" rows="3" caption="Keterangan" name="posting.stNotes" type="string" presentation="standard"/>
                    
                </table>
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