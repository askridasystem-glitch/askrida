<%@ page import="com.webfin.master.clausules.form.ClausulesMasterForm,
com.webfin.insurance.model.InsuranceClausulesView"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    ClausulesMasterForm form = (ClausulesMasterForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
               
            </td>
        </tr>
        <tr>
            <td>
                
                <table cellpadding=2 cellspacing=1>
                    <c:field width="200" readonly="true" caption="Clausules ID" name="clausules.stInsuranceClauseID" type="string" mandatory="false" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Cabang" name="clausules.stCostCenterCode" type="string" lov="LOV_Branch" mandatory="false" presentation="standard"/>
                    

                    <c:field width="400" readonly="false" caption="Jenis Polis" name="clausules.stPolicyTypeID" type="string" lov="LOV_PolicyType" mandatory="true" presentation="standard"/>
                    <c:field width="400" readonly="false" caption="Judul" name="clausules.stShortDescription" type="string" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Klausula Wajib" name="clausules.stDefaultFlag" type="check" mandatory="false" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Active" name="clausules.stActiveFlag" type="check" mandatory="false" presentation="standard"/>
                    <%--<c:field width="500" rows="10" readonly="false" caption="Wording 1" name="clausules.stDescription" type="string" readonly="true" presentation="standard"/>
                    --%>
                    <tr>
                        <td></td>
                        <td>
                            
                        </td>
                        <td>
                            <c:button text="{L-ENGCopy Wording 1-L}{L-INACopy Wording 1-L}" event="copyWording1"/>
                            <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
                        </td>
                    </tr>
                    <c:field width="500" rows="35" readonly="false" caption="Wording 2" name="clausules.stDescriptionNew" type="string" mandatory="true" presentation="standard"/>
                    
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>
<iframe src="" id=frmx width=1 height=1></iframe>
<script>
   var frmx = docEl('frmx');

   function getSelectedAttr(c,ref) {
      return c.options[c.selectedIndex].getAttribute(ref);
   }

   function dynPrintClick() {

      if (true) {
         frmx.src='x.fpc?EVENT=INS_PRT_CLAUSULES&clausulesid='+ document.getElementById('clausules.stInsuranceClauseID').value;
         return;
      }
   }

</script>
