<%@ page import="com.webfin.gl.model.JournalView,
                 com.webfin.gl.model.JournalHeaderView,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.*,
                 com.webfin.gl.ejb.CostCenterManager,
                 com.webfin.gl.accounts.model.SelectAccountForm,
                 com.webfin.ar.model.ARReceiptClassView,
                 com.webfin.ar.model.ARPaymentMethodView,
                 com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
<%
  final DTOList cbJournalType = (DTOList)request.getAttribute("CBJT");
   final LOV cbCcy = (LOV)request.getAttribute("CCYCB");
   final JournalHeaderView jh = (JournalHeaderView)request.getAttribute("JH");
   final DTOList details = jh.getDetails();
   final LOV CC = (LOV) request.getAttribute("CC");
   final LOV METHOD2 = (LOV) request.getAttribute("METHOD2");
   final LOV GL_ACCT_ID = (LOV) request.getAttribute("GL_ACCT_ID");
  
   
   final char cf = (jh.isModified()?0:JSPUtil.READONLY);
   
   final char readonly = (JSPUtil.READONLY);

   final char cfNew = (jh.isNew()?0:JSPUtil.READONLY);

   final boolean isnew = jh.isNew();

   final boolean ro = jh.isUnModified();

   final boolean editMode = jh.isUpdate();

   final boolean isForex = !Tools.isEqual(jh.getDbCurrencyRate(), BDUtil.one);

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(jh.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;
   
   int phase=0;

   if (jh.getStCostCenter()!=null) phase=2;

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
               <table cellpadding=2 cellspacing=1>
                  <c:field name="jh.stTransactionNo" width="130" mandatory="true" caption="Payment No" type="string|64" presentation="standard" readonly="true" />
                  <c:field lov="LOV_CostCenter" width="200" name="jh.stCostCenter" mandatory="true" caption="Branch" type="string" presentation="standard"  changeaction="changeCostCenter" />
                  <c:field readonly="<%=hasReceiptClass%>" lov="LOV_ReceiptClass" width="150" changeaction="changeReceiptClass" name="jh.stReceiptClassID" mandatory="true" caption="Method" type="string" presentation="standard">
                     <c:param name="invoice_type" value=" " />
                  </c:field>
                  <c:evaluate when="<%=phase>=2%>" >
                        <c:field lov="LOV_PaymentMethod" width="400" name="jh.stPaymentMethodID" mandatory="true" caption="Account" type="string"  presentation="standard"  changeaction="changemethod" >
                           <c:lovLink name="rc" link="jh.stReceiptClassID" clientLink="false"/>
                           <c:param name="cc" value="<%=jh.getStCostCenter()%>" />
                        </c:field>
                  </c:evaluate>
                  
               </table>
               </td>
               
            </tr>
            
         </table>

      </td>
   </tr>

   <tr>
      <td align=center>
         <c:evaluate when="<%=!readOnly%>" >
            <c:evaluate when="<%=phase>=3%>">
               <c:button text="Recalculate" event="doRecalculate"/>
               <c:button text="Save" event="doSave" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
            <c:button text="Generate Payment No" event="generatRNo" />
         </c:evaluate>
         <c:evaluate when="<%=readOnly%>" >
            <c:button text="Close" event="doClose"/>
         </c:evaluate>
         <c:button text="Approve" event="doApprove" confirm="Do you want to approve ?" />

      </td>
   </tr>
</table>

</c:frame>
<script>
</script>