<%@ page import="com.webfin.gl.model.TitipanPremiView,
                 com.webfin.gl.model.TitipanPremiHeaderView,
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
   final TitipanPremiHeaderView jh = (TitipanPremiHeaderView)request.getAttribute("JH");
   final DTOList details = jh.getDetails();
   final LOV CC = (LOV) request.getAttribute("CC");
   final LOV METHOD2 = (LOV) request.getAttribute("METHOD2");
   final LOV GL_ACCT_ID = (LOV) request.getAttribute("GL_ACCT_ID");
   final LOV MONTH = (LOV) request.getAttribute("MONTH");
   final LOV YEAR = (LOV) request.getAttribute("YEAR");
   final LOV CAUSE = (LOV) request.getAttribute("CAUSE");
  
   
   char cf = (jh.isModified()?0:JSPUtil.READONLY);
   
   final char readonly = (JSPUtil.READONLY);

   final char cfNew = (jh.isNew()?0:JSPUtil.READONLY);

   final boolean isnew = jh.isNew();

   boolean ro = jh.isUnModified();

   final boolean editMode = jh.isUpdate();
   
   final boolean readOnly = jh.getReadOnly();
   
   final boolean isApproved = jh.isApproved();

   final String reverse = (String) request.getAttribute("REVERSE");

   boolean isReverse = false;

   if(reverse!=null)
       if(reverse.equalsIgnoreCase("Y"))
           isReverse = true;
   
   if(isApproved){
       cf = JSPUtil.READONLY;
       ro = true;
   }

   final boolean isForex = !Tools.isEqual(jh.getDbCurrencyRate(), BDUtil.one);

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(jh.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;
   
   int phase=0;

   if (jh.getStCostCenter()!=null) phase=1;

%>
   <body>
      <form name=f method=POST action="ctl.ctl" onsubmit="return dovalidate();">
         <input type=hidden name=EVENT value="TP_GL_ENTRY_EDIT_REFRESH">
         <input type=hidden name=rn value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeaderOnly("TITIPAN PREMI")%>
               </td>
            </tr>
            <tr>
               <td><table cellpadding=2 cellspacing=1>
                  <tr><td>NO. BUKTI</td><td>:</td><td colspan="2"><%=jspUtil.getInputText("trxno",new FieldValidator("","Transaction Number","string",64),jh.getStTransactionNo(), 100, JSPUtil.READONLY|cf)%></td></tr>
                  <%--<tr><td>ENTRY DATE</td><td>:</td><td><%=jspUtil.getInputText("trxdate",new FieldValidator("","Transaction Date","date",-1),jh.getDtApplyDate(), 200, JSPUtil.MANDATORY|cf)%></td></tr>--%>
                  
                  <tr><td>BULAN</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("month|Month|string",null,MONTH.setLOValue(jh.getStMonths()), 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>TAHUN</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("year|Year|string",null,YEAR.setLOValue(jh.getStYears()), 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  <%--<tr><td>TANGGAL ENTRY</td><td>:</td><td colspan="2"><%=jspUtil.getInputText("createdate",new FieldValidator("","Create Date","date",-1),jh.getDtCreateDate(), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                  
                  <tr><td>MATA UANG</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("ccy|Currency|string||onChange:GL_ENTRY_CHG_CCY",null,cbCcy.setLOValue(jh.getStCurrencyCode()), 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>KURS</td><td>:</td><td colspan="2"><%=jspUtil.getInputText("rate|Rate|money16.2",null,jh.getDbCurrencyRate(), 80, JSPUtil.MANDATORY|cf|rateflags|JSPUtil.NOTEXTMODE)%></td></tr>
                  
                   <tr><td>JENIS JURNAL</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("jt|Journal Type|string",null,cbJournalType.setLOValue("AOG"), 100, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%></td></tr>
              	  --%>
                  <tr><td>CABANG</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CC.setLOValue(jh.getStCostCenter()),170,JSPUtil.MANDATORY|cf)%></td></tr>
              	  <tr><td>METODE</td><td>:</td><td colspan="2"><%=jspUtil.getInputSelect("method2|Method|string||onChange:TP_GL_ENTRY_CHG_YEAR",null,METHOD2.setLOValue(jh.getStMethodCode()), 220, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>REKENING</td><td>:</td><td colspan="2"><input type=hidden name=acidmaster value=<%=jspUtil.print(jh.getStAccountIDMaster())%>><%=jspUtil.getInputText("acnomaster",new FieldValidator("accountno","Account Number","string",32), jh.getStAccountNoMaster(), 150, JSPUtil.MANDATORY|JSPUtil.NOTEXTMODE|JSPUtil.READONLY)%>
                        <%=ro?"":jspUtil.getButtonNormal("bx","...","selectAccountByBranch();")%></td>
                  </tr>
                  <tr><td>KETERANGAN</td><td>:</td><td colspan="2"><%=jspUtil.getInputText("descmaster",new FieldValidator("accountno","Description","string",128),jh.getStDescriptionMaster(), 350, cf)%></td>
               	  <tr><td>IDR Flag</td><td>:</td><td colspan="2"><%=jspUtil.getInputCheck("idrflag",null,jh.getStIDRFlag(), 100)%></td></tr> 
                
               </table>																																			
               </td> 
            </tr>
            <tr>
      	<tr>
      	<td>
            <strong>DAFTAR TITIPAN PREMI </strong>
      	</td>
      	</tr>
      	    
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                         <td></td>
                         <td>CTR</td>
                        <td colspan=2>REKENING</td>
                        <td>KETERANGAN</td>
                        <td>PENYEBAB</td>
                        <td colspan=2>NOMOR POLIS</td>
                        <td>TANGGAL</td>
                        <%--<td>DEBIT</td>
                        <td>KREDIT</td>
                        --%>
                        <td>NILAI</td>
                        <td>SELISIH</td>
                        <td>SISA</td>
                        <td>NO REFERENSI</td>
                     </tr>
<%
   final boolean allowDelete = !ro && (details.size()>0);
   for (int i = 0; i < details.size(); i++) {
   	
      TitipanPremiView jv = (TitipanPremiView) details.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td width=25> <%=!allowDelete?"":jspUtil.getButtonNormal("bx","-","document.all.rn.value="+i+";submitEvent('TP_GL_ENTRY_DEL_LINE')")%></td>
                        <td><%=jspUtil.getInputText("counter"+i,new FieldValidator("counter","Counter","string",128),jv.getStCounter(), 50, JSPUtil.READONLY)%></td>
 
                        <td><input type=hidden name=acid<%=i%> value=<%=jspUtil.print(jv.getStAccountID())%>><%=jspUtil.getInputText("acno"+i,new FieldValidator("accountno","Account Number","string",32),jv.getStAccountNo(), 110, cf|JSPUtil.NOTEXTMODE|JSPUtil.READONLY)%></td>
                        <td width=25><%if(i==0){%><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+i+";selectAccountByBranch2();")%><%}%></td>
                        <td><%=jspUtil.getInputText("desc"+i,new FieldValidator("accountno","Description","string",128),jv.getStDescription(), 220, cf)%></td>
                        <td><%=jspUtil.getInputSelect("cause"+i,new FieldValidator("cause","Penyebab","string",128),CAUSE.setLOValue(jv.getStCause()), 140, cf)%></td>
                        <%--<td><%=jspUtil.getInputText("polno"+i,new FieldValidator("polno","Nomor Polis","string",18),jv.getStPolicyNo(), 140, cf)%></td>
                        --%>

                        <td><input type=hidden name=polid<%=i%> value=<%=jspUtil.print(jv.getStPolicyID())%>><%=jspUtil.getInputText("polno"+i,new FieldValidator("polno","Nomor Polis","string",32),jv.getStPolicyNo(), 140, cf|JSPUtil.NOTEXTMODE|JSPUtil.READONLY)%></td>
                        <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+i+";selectPolicyByBranch2();")%></td>
                        <td><%=jspUtil.getInputText("trxdate"+i,new FieldValidator("trxdate","Tanggal","date",-1),jv.getDtApplyDate(), 150, cf)%></td>
                        <td><%=jspUtil.getInputText("amount"+i,new FieldValidator("amount","Amount","money16.2",-1),jv.getDbAmount(), 110, cf|JSPUtil.NOTEXTMODE)%>

                        </td>
                        <td>
                            <%=jspUtil.getInputText("excess"+i,new FieldValidator("excess","Excess","money16.2",-1),jv.getDbExcessAmount(), 110, cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        <td><%=jspUtil.getInputText("balance"+i,new FieldValidator("balance","Balance","money16.2",-1),jv.getDbBalance(), 110, JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%>

                        </td>
                        <td><%=jspUtil.getInputText("referenceno"+i,new FieldValidator("referenceno","No Reference","string",128),jv.getStReferenceNo(), 110, cf)%></td>
                     </tr>
                  <% 
                  		
                  
                   %>   
                     
<% } %>

                     <tr>
                        <td colspan=4></td>
                        <td></td>
                        <td></td>
                        <td align=right>
                          <%=ro?"":jspUtil.getButtonNormal("crt","+ Titipan","submitEvent('TP_GL_ENTRY_ADD_LINE')")%>
                        </td>
                        <td></td>
                        <td align=right>
                          Total :
                        </td>
                     <b><td align=right id=balcap2 >
                        </td></b>
                     </tr>
                  </table>
               </td>
               
            </tr>
            
<% if (!ro) {%>
            <tr>
               <td align=center>
                  <%if(isReverse){%>
                       <%=jspUtil.getButtonSubmit("breverse","Reverse","dialogReturn('yay');f.EVENT.value='TP_GL_REVERSE'")%>
                  <%}else{%>
                        <%=jspUtil.getButtonSubmit("bsave","Simpan","dialogReturn('yay');f.EVENT.value='TP_GL_ENTRY_SAVE'")%>
                  <%}%>
                  <%=jspUtil.getButtonNormal("bc","Batal","window.close()")%>
               </td>
            </tr>
<% } else {%>
            <tr>
               <td align=center>
                  <%if(isApproved){%> 
                    <%=jspUtil.getButtonSubmit("bsave","Setujui","dialogReturn('yay');f.EVENT.value='TP_GL_ENTRY_SAVE'")%>
                  <%}%>
                  <%=jspUtil.getButtonNormal("bc","Tutup","window.close()")%>
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
      //document.getElementById('keterangan'+rnc).value=o.desc;
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
      //document.getElementById('keterangan').value=o.desc;
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
            t2+=getFloat(document.getElementById('rcredit'+i).value)+getFloat(document.getElementById('rdebit'+i).value);
            t+=getFloat(document.getElementById('credit'+i).value)+getFloat(document.getElementById('debit'+i).value);
         
            document.getElementById('balance'+i).value = t2;
         }
      } else {
         for (var i=0;i<c;i++) {
            t+=getFloat(document.getElementById('credit'+i).value)+getFloat(document.getElementById('debit'+i).value);
            document.getElementById('balance'+i).value = t;
         }
         t2=t;
      }
      t = t + tHeader;
      t2 = t2 + tHeader;
      //balcapctl.innerText=floattomoney(t,2);
      //if (balcapctl2 != null)
         //balcapctl2.innerText=floattomoney(t2,2);
         
      window.setTimeout(displayBalance,300);
   }

   function dovalidate() {
      var c=<%=details.size()%>

      //if(c==1) return;

      for (var i=0;i<c;i++) {
         if (document.getElementById('acid'+i).value.trim()=='') {alert('Kode akun harus diisi'); return false;}
         if (document.getElementById('desc'+i).value.trim()=='') {alert('Keterangan harus diisi'); return false;}
         if (document.getElementById('cause'+i).value.trim()=='') {alert('Penyebab harus diisi'); return false;}
         if (document.getElementById('trxdate'+i).value.trim()=='') {alert('Tanggal harus diisi'); return false;}
         if (document.getElementById('amount'+i).value.trim()=='') {alert('Jumlah harus diisi'); return false;}

         if (document.getElementById('cause'+i).value.trim()=='1' || document.getElementById('cause'+i).value.trim()=='2')
                if (document.getElementById('polid'+i).value.trim()=='') {alert('Nomor polis harus diisi'); return false;}
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
        else if(receiptclass=='D') acccode = '122';

        if(receiptclass == ''){ 
            alert('Pilih dulu METODE titipan premi');
            return false;
        }

        var month = document.getElementById('month').value;
        var year = document.getElementById('year').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount2);

        
   	//openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);
   		
   }
   
   function selectAccountByBranch2(o){
   		
        var cabang = document.getElementById('costcenter').value;
        var receiptclass = document.getElementById('method2').value;
        //122108923800 43
        var rekening =  document.getElementById('acnomaster').value.substring(3,4);
        var acccode;

        if(receiptclass=='A') acccode = '48920';
        else if(receiptclass=='B') acccode = '48920';
        else if(receiptclass=='C') acccode = '4892'+ rekening;
        else if(receiptclass=='D') acccode = '4892'+ rekening;

        var glcode = document.getElementById('acnomaster').value.substring(5,10);
        if(glcode!='')
            acccode = acccode + glcode;

        var month = document.getElementById('month').value;
        var year = document.getElementById('year').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount);

        
        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }
   
   
   function displayBalance2() {
      var t=0;
      var t2=0;
      var c=<%=details.size()%>
      //var rate = getFloat(document.getElementById('rate').value);
      //var tHeader = 0;
            
      if (document.getElementById('amount0')!=null) {
         for (var i=0;i<c;i++) {
            //document.getElementById('amount'+i).value = floattomoney(getFloat(document.getElementById('amount'+i).value)*rate,2);
            t+=getFloat(document.getElementById('amount'+i).value);
          
            document.getElementById('balance'+i).value = getFloat(document.getElementById('amount'+i).value);

            if(document.getElementById('balance'+i).value > document.getElementById('amount'+i).value)
                document.getElementById('balance'+i).value = getFloat(document.getElementById('amount'+i).value);
         }
      } 

      balcap2.innerText=floattomoney(t,2);
      window.setTimeout(displayBalance2,300);
   }

   function selectPolicyByBranch2(o){

        var cabang = document.getElementById('costcenter').value;

        var month = document.getElementById('month').value;
        var year = document.getElementById('year').value;

        openDialog('so.ctl?EVENT=INS_POLICY_SEARCH&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectPolicy);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }

   var rnc;
   function selectPolicy(o) {
      if (o==null) return;
      document.getElementById('polid'+rnc).value=o.POL_ID;
      document.getElementById('polno'+rnc).value=o.POL_NO;
   }
   
    displayBalance2();
  
</script>