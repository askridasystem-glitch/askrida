<%@ page import="com.webfin.gl.model.TitipanPremiReinsuranceView,
                 com.webfin.gl.model.TitipanPremiReinsuranceHeaderView,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.*,
                 com.webfin.gl.ejb.CurrencyManager"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%

   final DTOList cbJournalType = (DTOList)request.getAttribute("CBJT");
   final LOV cbCcy = (LOV)request.getAttribute("CCYCB");
   final TitipanPremiReinsuranceHeaderView jh = (TitipanPremiReinsuranceHeaderView)request.getAttribute("JH");
   final DTOList details = jh.getDetails();
   final LOV CC = (LOV) request.getAttribute("CC");
   final LOV METHOD2 = (LOV) request.getAttribute("METHOD2");
   final LOV GL_ACCT_ID = (LOV) request.getAttribute("GL_ACCT_ID");

   final char cf = (jh.isModified()?0:JSPUtil.READONLY);

   final char cfNew = (jh.isNew()?0:JSPUtil.READONLY);

   final boolean isnew = jh.isNew();

   final boolean ro = jh.isUnModified();

   final boolean editMode = jh.isUpdate();

   final boolean isForex = !Tools.isEqual(jh.getDbCurrencyRate(), BDUtil.one);

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(jh.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;

%>
   <body>
      <form name=f method=POST action="ctl.ctl" onsubmit="return dovalidate();">
         <input type=hidden name=EVENT value="TP_REINS_GL_EDIT_REFRESH">
         <input type=hidden name=rn value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("EDIT TITIPAN PREMI REASURANSI")%>
               </td>
            </tr>
            <tr>
               <td><table cellpadding=2 cellspacing=1>
               	  <tr><td>{L-ENGTRANS. NO-L}{L-INANO. BUKTI-L}</td><td>:</td><td><%=jspUtil.getInputText("trxno",new FieldValidator("","Transaction Number","string",64),jh.getStTransactionNo(), 100, JSPUtil.READONLY|cf)%></td></tr>
                  <tr><td>{L-ENGENTRY DATE-L}{L-INATANGGAL ENTRY-L}</td><td>:</td><td><%=jspUtil.getInputText("createdate",new FieldValidator("","Create Date","date",-1),jh.getDtCreateDate(), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>{L-ENGCURRENCY-L}{L-INAMATA UANG-L}</td><td>:</td><td><%=jspUtil.getInputSelect("ccy|Currency|string||onChange:GL_ENTRY_CHG_CCY",null,cbCcy.setLOValue(jh.getStCurrencyCode()), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>{L-ENGKURS-L}{L-INAKURS-L}</td><td>:</td><td><%=jspUtil.getInputText("rate|Rate|money16.2",null,jh.getDbCurrencyRate(), 80, JSPUtil.MANDATORY|cf|rateflags|JSPUtil.NOTEXTMODE)%></td></tr>
                  <tr><td>{L-ENGJOURNAL TYPE-L}{L-INAJENIS JURNAL-L}</td><td>:</td><td><%=jspUtil.getInputSelect("jt|Journal Type|string",null,cbJournalType.setLOValue(jh.getStJournalCode()), 100, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%></td></tr>
               	  <tr><td>{L-ENGBRANCH-L}{L-INACABANG-L}</td><td>:</td><td><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CC.setLOValue(jh.getStCostCenter()),200,JSPUtil.MANDATORY|cf)%></td></tr>
              	  <tr><td>{L-ENGMETHOD-L}{L-INAMETHOD-L}</td><td>:</td><td><%=jspUtil.getInputSelect("method2|Method|string",null,METHOD2.setLOValue(jh.getStMethodCode()), 100, JSPUtil.MANDATORY|cf)%></td></tr>            
                  <tr><td>{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}</td><td>:</td><td><input type=text name=desc size="80" readonly="true"></td></tr>            
               </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <%--<td>JT</td>--%>
                        <td colspan=2>{L-ENGACCOUNT NO-L}{L-INANO AKUN-L}</td>
                        <td>{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L} </td>
                        <td>{L-ENGDATE-L}{L-INATANGGAL-L}</td>
                        <td>DEBIT</td>
                        <td>CREDIT</td>
                        <td></td>
                     </tr>
<%
   final boolean allowDelete = !ro && (details.size()>1);
   for (int i = 0; i < details.size(); i++) {
      TitipanPremiReinsuranceView jv = (TitipanPremiReinsuranceView) details.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <%--<td><%=jspUtil.getInputSelect("jt"+i,new FieldValidator("","Journal Type","string",5),cbJournalType.setLOValue(jv.getStJournalCode()), 80, JSPUtil.MANDATORY|cf)%></td>--%>
                        <td><input type=hidden name=acid<%=i%> value=<%=jspUtil.print(jv.getLgAccountID())%>><%=jspUtil.getInputText("acno"+i,new FieldValidator("accountno","Account Number","string",32),jv.getStAccountNo(), 150, JSPUtil.MANDATORY|JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td>
                        <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+i+";selectAccountByBranch();")%></td>
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
                        <td width=25><%=!allowDelete?"":jspUtil.getButtonNormal("bx","-","document.all.rn.value="+i+";submitEvent('TP_GL_EDIT_DEL_LINE')")%></td>
                     </tr>
<% } %>
                     <tr>
                        <%--<td></td>--%>
                        <td colspan=2></td>
                        <td></td>
                        <td align=right><%--Balance:--%></td>
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
                  </table>
               </td>
            </tr>
            <tr>
               <td align=right>
                  <%=ro?"":jspUtil.getButtonNormal("crt","+Line","submitEvent('TP_REINS_GL_EDIT_ADD_LINE')")%>
                  <br>
                  <br>
               </td>
            </tr>
<% if (!ro) {%>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonSubmit("bsave","Save","dialogReturn('yay');f.EVENT.value='TP_REINS_GL_EDIT_SAVE'")%>
                  <%=jspUtil.getButtonNormal("bc","Cancel","window.close()")%>
                  </td>
            </tr>
<% } else {%>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonNormal("bc","Close","window.close()")%>
                  <%=jspUtil.getButtonSubmit("bsave","Approve","dialogReturn('yay');f.EVENT.value='GL_ENTRY_APPROVE'")%>

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
      document.getElementById('desc').value=o.desc;
   }

   balcapctl = document.getElementById('balcap');
   balcapctl2 = document.getElementById('balcap2');

   function displayBalance() {
      var t=0;
      var t2=0;
      var c=<%=details.size()%>
      var rate = getFloat(document.getElementById('rate').value);
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
      balcapctl.innerText=floattomoney(t,2);
      if (balcapctl2 != null)
         balcapctl2.innerText=floattomoney(t2,2);
      window.setTimeout(displayBalance,300);
   }

   function dovalidate() {
      var c=<%=details.size()%>
      for (var i=0;i<c;i++) {
         if (document.getElementById('acid'+i).value.trim()=='') {alert('You must select account'); return false;}
      }

      return true;
   }
   
   function selectAccountByBranch(o){
   	
        var cabang = document.getElementById('costcenter').value;
        var method = document.getElementById('method2').value;
        var receiptclass = document.getElementById('method2').value;

        var acccode = 'undefined';
        
   	openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }

   displayBalance();
</script>


