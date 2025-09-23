<%@ page import="com.webfin.gl.model.JournalSyariahView,
                 com.webfin.gl.model.JournalHeaderView,
                 com.webfin.gl.codes.GLCodes,
                 com.webfin.FinCodec,
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
   final JournalHeaderView jh = (JournalHeaderView)request.getAttribute("JH");
   final DTOList details = jh.getDetails();
   final LOV CC = (LOV) request.getAttribute("CC");
   final LOV METHOD2 = (LOV) request.getAttribute("METHOD2");
   final LOV SY_GL_ACCT_ID = (LOV) request.getAttribute("SY_GL_ACCT_ID");

   final char cf = (jh.isModified()?0:JSPUtil.READONLY);

   final char cfNew = (jh.isNew()?0:JSPUtil.READONLY);

   final boolean isnew = jh.isNew();

   final boolean ro = jh.isUnModified();

   final boolean editMode = jh.isUpdate();

   final boolean isForex = !Tools.isEqual(jh.getDbCurrencyRate(), BDUtil.one);

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(jh.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;
   
   final boolean approveMode = jh.isApprovedMode();
   
   final LookUpUtil luyears = GLCodes.Years.getLookUp();
   luyears.setLOValue(String.valueOf(jh.getLgFiscalYear()));
   final LookUpUtil luper = FinCodec.MonthPeriods2.getLookUp();
   //GLCodes.GLPeriods.getLookUp();
   luper.setLOValue(String.valueOf(jh.getLgPeriodNo()));

   final String reverse = (String) request.getAttribute("REVERSE");

                boolean isReverse = false;

                if (reverse != null) {
                    if (reverse.equalsIgnoreCase("Y")) {
                        isReverse = true;
                    }
                }

%>
   <body>
      <form name=f method=POST action="ctl.ctl" onsubmit="return dovalidate();">
         <input type=hidden name=EVENT value="SY_SY_GL_ENTRY_EDIT_REFRESH">
         <input type=hidden name=rn value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("JURNAL SYARIAH")%>
               </td>
            </tr>
            <tr>
               <td><table cellpadding=2 cellspacing=1>
               	  <tr><td>{L-ENGTIPE-L}{L-INATIPE-L}</td><td>:</td><td><%=jspUtil.getInputText("type",new FieldValidator("","Transaction Number","string",64),jh.getStRefTrxType(), 100, JSPUtil.READONLY|cf)%></td></tr>
                  <tr><td>TAHUN</td><td>:</td><td><%=jspUtil.getInputSelect("fiscal",new FieldValidator("","Fiscal Year","integer",5),luyears, 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  <tr><td>BULAN</td><td>:</td><td><%=jspUtil.getInputSelect("periodnum",new FieldValidator("","Period No","integer",2),luper, 100, JSPUtil.MANDATORY|cf)%></td></tr>
              </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <%--<td>JT</td>--%>
                        <td>NO.</td>
                        <td>{L-ENGACCOUNT-L}{L-INAREKENING-L} </td>
                        <td>{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L} </td>
                        <td>SALDO</td>
                        <td></td>
                     </tr>
<%
   final boolean allowDelete = !ro && (details.size()>1);
   for (int i = 0; i < details.size(); i++) {
      JournalSyariahView jv = (JournalSyariahView) details.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td><%=(i+1)%></td>
                        <td><%=jspUtil.getInputText("acno"+i,new FieldValidator("accountno","Description","string",128),jv.getStAccountNo(), 350, JSPUtil.READONLY)%></td>
                        <td><%=jspUtil.getInputText("desc"+i,new FieldValidator("accountno","Description","string",128),jv.getStDescription(), 350, JSPUtil.READONLY)%></td>
                        <td>
                           <%=jspUtil.getInputText("debit"+i,new FieldValidator("debit","Debit Value","money16.2",-1),jv.getDbEnteredDebit(), 140, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
                           <%=!isForex?"":jspUtil.getInputText("rdebit"+i,new FieldValidator("debit","Debit Value","money16.2",-1),jv.getDbDebit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
                        </td>
                        <td width=25><%=!allowDelete?"":jspUtil.getButtonNormal("bx","-","document.all.rn.value="+i+";submitEvent('SY_GL_ENTRY_DEL_LINE')")%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td align=right>
                  <%=ro?"":jspUtil.getButtonNormal("crt","+Line","submitEvent('SY_GL_ENTRY_ADD_LINE')")%>
                  <br>
                  <br>
               </td>
            </tr>
<% if (!ro) {%>
            <tr>
               <td align=center>
                  <%if (isReverse) {%>
                        <%=jspUtil.getButtonSubmit("breverse", "Reverse", "dialogReturn('yay');f.EVENT.value='SY_REVERSE'")%>
                        <%} else {%>
                        <%=jspUtil.getButtonSubmit("bsave", "Simpan", "dialogReturn('yay');f.EVENT.value='SY_GL_ENTRY_SAVE'")%>
                        <%}%>
                        <%=jspUtil.getButtonNormal("bc", "Cancel", "window.close()")%>
                  </td>


            </tr>
<% } else {%>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonNormal("bc","Close","window.close()")%>
               <%if(approveMode){%>  
                    <%=jspUtil.getButtonSubmit("bsave","Approve","dialogReturn('yay');f.EVENT.value='SY_GL_ENTRY_APPROVE'")%>
               <%}%>
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
      document.getElementById('desc'+rnc).value=o.desc;
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

      return true;
   }
   
   function selectAccountByBranch(o){
   	
        var cabang = document.getElementById('costcenter').value;

        var acccode = 'undefined';
        
   	openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }

   //displayBalance();
</script>


