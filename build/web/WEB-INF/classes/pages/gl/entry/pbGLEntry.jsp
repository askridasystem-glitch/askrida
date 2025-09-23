<%@ page import="com.webfin.gl.model.JournalView,
                 com.webfin.gl.model.JournalHeaderView,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.*,
                 com.webfin.gl.ejb.CostCenterManager,
                 com.webfin.gl.accounts.model.SelectAccountForm,
                 com.webfin.ar.model.ARReceiptClassView,
                 com.webfin.ar.model.ARPaymentMethodView,
                 com.webfin.gl.ejb.CurrencyManager"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
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
   
   final boolean readOnly = jh.getReadOnly();

   final boolean isForex = !Tools.isEqual(jh.getDbCurrencyRate(), BDUtil.one);

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(jh.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;
   
   int phase=0;

   if (jh.getStCostCenter()!=null) phase=1;

%>
   <body>
      <form name=f method=POST action="ctl.ctl" onsubmit="return dovalidate();">
         <input type=hidden name=EVENT value="PB_GL_ENTRY_EDIT_REFRESH">
         <input type=hidden name=rn value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeaderOnly("PEMBINAAN BISNIS")%>
               </td>
            </tr>
            <tr>
               <td><table cellpadding=2 cellspacing=1>
                  <tr><td>NO. BUKTI</td><td>:</td><td><%=jspUtil.getInputText("trxno",new FieldValidator("","Transaction Number","string",64),jh.getStTransactionNo(), 100, JSPUtil.READONLY|cf)%></td></tr>
                  <%--<tr><td>ENTRY DATE</td><td>:</td><td><%=jspUtil.getInputText("trxdate",new FieldValidator("","Transaction Date","date",-1),jh.getDtApplyDate(), 200, JSPUtil.MANDATORY|cf)%></td></tr>--%>
                  <tr><td>ENTRY DATE</td><td>:</td><td><%=jspUtil.getInputText("createdate",new FieldValidator("","Create Date","date",-1),jh.getDtCreateDate(), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>CURRENCY</td><td>:</td><td><%=jspUtil.getInputSelect("ccy|Currency|string||onChange:GL_ENTRY_CHG_CCY",null,cbCcy.setLOValue(jh.getStCurrencyCode()), 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>KURS</td><td>:</td><td><%=jspUtil.getInputText("rate|Rate|money16.2",null,jh.getDbCurrencyRate(), 80, JSPUtil.MANDATORY|cf|rateflags|JSPUtil.NOTEXTMODE)%></td></tr>
                  <tr><td>JOURNAL TYPE</td><td>:</td><td><%=jspUtil.getInputSelect("jt|Journal Type|string",null,cbJournalType.setLOValue("AOG"), 100, JSPUtil.READONLY)%></td></tr>   
              	  <tr><td>BRANCH</td><td>:</td><td><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CC.setLOValue(jh.getStCostCenter()),200,JSPUtil.MANDATORY|cf)%></td></tr>
              	  <tr><td>METHOD</td><td>:</td><td><%=jspUtil.getInputSelect("method2|Method|string",null,METHOD2.setLOValue(jh.getStMethodCode()), 200, JSPUtil.READONLY)%></td></tr>            
               	   <%--  
               	  <tr><td>ACCOUNT</td><td>:</td><td><%=jspUtil.getInputSelect("gl_acct_id|Account|string",null,jh.getStGlAccountID(), 100, JSPUtil.MANDATORY|cf)%></td></tr>                	       
                  --%> 
               </table>																																			
               </td>
            </tr>
            <tr>
            
            
         <%-- AKUN LAWAN --%>
          <td>
          
          
                  <table cellpadding=2 cellspacing=1>
                  <% if(!readOnly){ %>
                     <tr class=header>
                        <%--<td>JT</td>--%>
                        <td colspan=2>ACCOUNT BANK</td>
                        <td>KETERANGAN</td>
                        <td>DESCRIPTION</td>
                        <%--
                        <td>DEBIT</td>
                        <td>CREDIT</td>
                        --%>
                     </tr>

                     <tr class=row0>																						
                        <%--<td><%=jspUtil.getInputSelect("jt"+i,new FieldValidator("","Journal Type","string",5),cbJournalType.setLOValue(jv.getStJournalCode()), 80, JSPUtil.MANDATORY|cf)%></td>--%>
                        <td><input type=hidden name=acidmaster value=<%=jspUtil.print(jh.getLgAccountIDMaster())%>><%=jspUtil.getInputText("acnomaster",new FieldValidator("accountno","Account Number","string",32),jh.getStAccountNoMaster(), 150, JSPUtil.MANDATORY|JSPUtil.NOTEXTMODE|JSPUtil.READONLY)%></td>
                        <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","selectAccountByBranch();")%></td>
                        <td><%=jspUtil.getInputText("keterangan",new FieldValidator("accountno","Keterangan","string",128),jh.getStKetMaster(), 250, cf)%></td>
                        <td><%=jspUtil.getInputText("descmaster",new FieldValidator("accountno","Description","string",128),jh.getStDescriptionMaster(), 250, cf)%></td>
                                                
                                        
                        <%--
                        <td>
                           <%=jspUtil.getInputText("debitmaster",new FieldValidator("debit","Debit Value","money16.2",-1),jh.getDbEnteredDebitMaster(), 110, cf|JSPUtil.NOTEXTMODE)%>
                           <%=!isForex?"":jspUtil.getInputText("rdebitmaster",new FieldValidator("debit","Debit Value","money16.2",-1),jh.getDbDebitMaster(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        <td>
                           <%=jspUtil.getInputText("creditmaster",new FieldValidator("credit","Credit Value","money16.2",-1),jh.getDbEnteredCreditMaster(), 110, cf|JSPUtil.NOTEXTMODE)%>
                           <%=!isForex?"":jspUtil.getInputText("rcreditmaster",new FieldValidator("credit","Credit Value","money16.2",-1),jh.getDbCreditMaster(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        --%>
                       
                     </tr>

                     <tr>
                        <%--<td></td>--%>
                        <td colspan=2></td>
                        <td></td>
                     </tr>
                     <% } %>
                  </table>
                  
                  
              </td>
           
            
      <%-- END AKUN LAWAN --%>  
      	<tr>
      	<td>
      		RINCIAN
      	</td>
      	</tr>
      	    
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <%--<td>JT</td>--%>
                        <td colspan=2>ACCOUNT #</td>
                        <td>KETERANGAN</td>
                        <td>DESCRIPTION</td>
                        <td>TANGGAL</td>
                        <td>DEBIT</td>
                        <td>CREDIT</td>
                        <td></td>
                     </tr>
<%
   final boolean allowDelete = !ro && (details.size()>1);
   for (int i = 0; i < details.size(); i++) {
   	
      JournalView jv = (JournalView) details.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <%--<td><%=jspUtil.getInputSelect("jt"+i,new FieldValidator("","Journal Type","string",5),cbJournalType.setLOValue(jv.getStJournalCode()), 80, JSPUtil.MANDATORY|cf)%></td>--%>
                        <td><input type=hidden name=acid<%=i%> value=<%=jspUtil.print(jv.getLgAccountID())%>><%=jspUtil.getInputText("acno"+i,new FieldValidator("accountno","Account Number","string",32),jv.getStAccountNo(), 150, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE|JSPUtil.READONLY)%></td>
                        <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+i+";selectAccountByBranch2();")%></td>
                        <td><%=jspUtil.getInputText("keterangan"+i,new FieldValidator("accountno","Keterangan","string",128),jv.getStKet(), 150, cf)%></td>
                        <td><%=jspUtil.getInputText("desc"+i,new FieldValidator("accountno","Description","string",128),jv.getStDescription(), 150, cf)%></td>
                        <td><%=jspUtil.getInputText("trxdate"+i,new FieldValidator("trxdate","Tanggal","date",-1),jv.getDtApplyDate(), 150, cf)%></td>
                        <td>
                           <%=jspUtil.getInputText("debit"+i,new FieldValidator("debit","Debit Value","money16.2",-1),jv.getDbEnteredDebit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
                           <%=!isForex?"":jspUtil.getInputText("rdebit"+i,new FieldValidator("debit","Debit Value","money16.2",-1),jv.getDbDebit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        <td>
                           <%=jspUtil.getInputText("credit"+i,new FieldValidator("credit","Credit Value","money16.2",-1),jv.getDbEnteredCredit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
                           <%=!isForex?"":jspUtil.getInputText("rcredit"+i,new FieldValidator("credit","Credit Value","money16.2",-1),jv.getDbCredit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        <td width=25><%=!allowDelete?"":jspUtil.getButtonNormal("bx","-","document.all.rn.value="+i+";submitEvent('PB_GL_ENTRY_DEL_LINE')")%></td>
                     </tr>
                     
                  <% 
                  		
                  
                   %>   
                     
<% } %>
<%--
                     <tr>
                        <td colspan=4></td>
                        <td></td>
                        <td align=right>Balance:</td>
                        <td>
                           <table cellpadding=2 cellspacing=1 width="100%">
                              <tr><td align=right id=balcap></td></tr>
<% if (isForex) {%>
                              <tr><td align=right id=balcap2></td></tr>
<% } %>
                           </table>
                        </td>
                        <td></td>
                     </tr>
 --%>                    
 
                  </table>
               </td>
               
            </tr>
            <tr>
               <td align=right>
                  <%=ro?"":jspUtil.getButtonNormal("crt","+Line","submitEvent('PB_GL_ENTRY_ADD_LINE')")%>
                  <br>
                  <br>
               </td>
            </tr>
<% if (!ro) {%>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonSubmit("bsave","Save","dialogReturn('yay');f.EVENT.value='PB_GL_ENTRY_SAVE'")%>
                  <%=jspUtil.getButtonNormal("bc","Cancel","window.close()")%>
                  <%--  
                  <%=jspUtil.getButtonSubmit("transno","Generate Trans No","dialogReturn('yay');f.EVENT.value='CB_GL_GEN_NO'")%>
               	  --%>
               </td>
            </tr>
<% } else {%>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonNormal("bc","Close","window.close()")%>
               </td>
            </tr>
<% } %>
         </table>
      </form>
   </body>
</html>
<script>
   var rnc;
   function selectAccount(o) {
      if (o==null) return;
      document.getElementById('acid'+rnc).value=o.acid;
      document.getElementById('acno'+rnc).value=o.acno;
      document.getElementById('keterangan'+rnc).value=o.desc;
      document.getElementById('desc'+rnc).value=o.desc; 
   }
   
  var cccode = document.getElementById('costcenter');
  function sendCcCode(o){
  		alert("tes");
  		if (o==null) return;
  		o.costcenter2.value = cccode;
  		
  }
	
   var rncmaster;
   function selectAccount2(o) {
      if (o==null) return;
      document.getElementById('acidmaster').value=o.acid;
      document.getElementById('acnomaster').value=o.acno;
      document.getElementById('keterangan').value=o.desc;
      document.getElementById('descmaster').value=o.desc;  
   }

   balcapctl = document.getElementById('balcap');
   balcapctl2 = document.getElementById('balcap2');

   function displayBalance() {
      var t=0;
      var t2=0;
      var c=<%=details.size()%>
      var rate = getFloat(document.getElementById('rate').value);
      var tHeader = 0;
      
      if (document.getElementById('creditmaster')!=null)
        tHeader = getFloat(document.getElementById('debitmaster').value) - getFloat(document.getElementById('creditmaster').value);
      
      if (document.getElementById('rcredit0')!=null) {
         for (var i=0;i<c;i++) {
            document.getElementById('rcredit'+i).value = floattomoney(getFloat(document.getElementById('credit'+i).value)*rate,2);
            document.getElementById('rdebit'+i).value = floattomoney(getFloat(document.getElementById('debit'+i).value)*rate,2);
            t2+=getFloat(document.getElementById('rcredit'+i).value)-getFloat(document.getElementById('rdebit'+i).value);
            t+=getFloat(document.getElementById('credit'+i).value)-getFloat(document.getElementById('debit'+i).value);
         
         }
      } else {
         for (var i=0;i<c;i++) {
            t+=getFloat(document.getElementById('credit'+i).value)-getFloat(document.getElementById('debit'+i).value);
         }
         t2=t;
      }
      t = t - tHeader;
      t2 = t2 - tHeader;
      balcapctl.innerText=floattomoney(t,2);
      if (balcapctl2 != null)
         balcapctl2.innerText=floattomoney(t2,2);
         
      window.setTimeout(displayBalance,300);
   }

   function dovalidate() {
      var c=<%=details.size()%>
      for (var i=0;i<c;i++) {
         if (document.getElementById('acid'+i).value.trim()=='') {alert('Kode akun harus diisi'); return false;}
         if (document.getElementById('trxdate'+i).value.trim()=='') {alert('Tanggal harus diisi'); return false;}
         if (document.getElementById('keterangan'+i).value.trim()=='') {alert('Keterangan harus diisi'); return false;}
         if (document.getElementById('desc'+i).value.trim()=='') {alert('Deskripsi harus diisi'); return false;}
      }

      return true;
   }
   
   function selectAccountByBranch(o){
   		
   	var cabang = document.getElementById('costcenter').value;
        var receiptclass = document.getElementById('method2').value;
        
        var acccode;
        
        if(receiptclass=='A') acccode = '12100';
        else if(receiptclass=='B') acccode = '12110';
        else if(receiptclass=='C') acccode = '12210';
        else if(receiptclass=='D') acccode = '12220';
        
   	openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);
   		
   }
   
   function selectAccountByBranch2(o){
   		
        var cabang = document.getElementById('costcenter').value;
        var receiptclass = document.getElementById('method2').value;
        var acccode = '791';
        
        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }
   
   
    //displayBalance();
  
</script>