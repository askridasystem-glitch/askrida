<%@ page import="com.webfin.insurance.model.*, 
                 com.crux.ff.model.FlexFieldHeaderView, 
                 com.crux.ff.model.FlexFieldDetailView, 
                 com.crux.util.*, 
                 com.crux.util.fop.FOPUtil, 
                 java.math.BigDecimal, 
                 com.crux.web.form.FormManager, 
                 com.crux.web.controller.SessionManager, 
                 com.webfin.insurance.form.ProductionReportForm, 
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 
 
   final DTOList l = (DTOList)request.getAttribute("RPT");
   
   final DTOList m = (DTOList)request.getAttribute("RPT");
 
   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm(); 
 	
 	BigDecimal tax = new BigDecimal(0);
 	
 	BigDecimal tax1;
 	
 	BigDecimal tax_baru = new BigDecimal(0);
 	
 	String ent_id2 = new String("");
 	
 	BigDecimal premi_coins = new BigDecimal(0);
 	
 	BigDecimal hfee_coins = new BigDecimal(0);
 	
 	BigDecimal tsi_coins = new BigDecimal(0);
 	
 	BigDecimal bypol_coins = new BigDecimal(0);
 	
 	BigDecimal sfee_coins = new BigDecimal(0);
 	
 	BigDecimal disc_coins = new BigDecimal(0);
 	
 	BigDecimal comm_coins = new BigDecimal(0);
 	
 	BigDecimal broker_coins = new BigDecimal(0);
 	
 	BigDecimal tax_coins = new BigDecimal(0);
 	
 	BigDecimal netpremi_coins = new BigDecimal(0);
 	
 	
 	BigDecimal total_premi_coins = new BigDecimal(0);
 	
 	BigDecimal total_hfee_coins = new BigDecimal(0);
 	
 	BigDecimal total_tsi_coins = new BigDecimal(0);
 	
 	BigDecimal total_bypol_coins = new BigDecimal(0);
 	
 	BigDecimal total_sfee_coins = new BigDecimal(0);
 	
 	BigDecimal total_disc_coins = new BigDecimal(0);
 	
 	BigDecimal total_comm_coins = new BigDecimal(0);
 	
 	BigDecimal total_broker_coins = new BigDecimal(0);
 	
 	BigDecimal total_tax_coins = new BigDecimal(0);
 	
 	BigDecimal total_netpremi_coins = new BigDecimal(0);
 
   //if (true) throw new NullPointerException(); 
 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
 
  <!-- defines page layout --> 
  <fo:layout-master-set> 
 
    <!-- layout for the first page --> 
    <fo:simple-page-master master-name="only" 
                  page-height="21cm"
                  page-width="50cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">
 
      <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/> 
      <fo:region-before extent="3cm"/> 
      <fo:region-after extent="1.5cm"/> 
    </fo:simple-page-master> 
 
  </fo:layout-master-set> 
  <!-- end: defines page layout --> 
 
  <!-- actual layout --> 
  <fo:page-sequence master-reference="only" initial-page-number="1"> 
 
    <!-- usage of page layout --> 
    <!-- header --> 
    <fo:static-content flow-name="xsl-region-before"> 
      <fo:block text-align="end" 
            font-size="6pt"
            font-family="serif"
            line-height="12pt">
        Production Report MKT00 - PT. Asuransi Bangun Askrida Page:<fo:page-number/> 
      </fo:block> 
    </fo:static-content> 
 
    <fo:flow flow-name="xsl-region-body"> 
 
      <!-- defines text title level 1--> 
 
      <fo:block font-size="18pt"
      		font-family="tahoma"
            line-height="16pt"
            space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       <%--{L-ENG Production Premium Report-L}{L-INA Laporan Produksi Premi-L}--%>
       <%=form.getStReportTitle()%>
      </fo:block>
 
      <!-- Normal text -->  
  
  	 <%--  
      <fo:block font-size="16pt">
       <% if (form.getExpirePeriodFrom()!=null || form.getExpirePeriodTo()!=null) {%> 
         Expire Date From : <%=JSPUtil.printX(form.getExpirePeriodFrom())%> To <%=JSPUtil.printX(form.getExpirePeriodTo())%>
       <% } %>
      </fo:block>
      --%>  
      
      <fo:block font-size="14pt" text-align="center">
       <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
         Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
       <% } %>
      </fo:block> 

      <fo:block font-size="14pt" text-align="center">
       <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
         Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
       <% } %>
      </fo:block>
      
      <fo:block font-size="14pt" text-align="center">
       <% if (form.getEntryDateFrom()!=null || form.getEntryDateTo()!=null) {%> 
         Tanggal Entry : <%=JSPUtil.printX(form.getEntryDateFrom())%> S/D <%=JSPUtil.printX(form.getEntryDateTo())%>
       <% } %>
      </fo:block>  
      
      <fo:block font-size="14pt">
       <% if (form.getStBranchDesc()!=null) {%> 
         Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
       <% } %>
      </fo:block>
  
      <fo:block font-size="14pt">
       <% if (form.getStPolicyTypeDesc()!=null) {%> 
         Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStFltCoverTypeDesc()!=null) {%> 
         Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="14pt">
       <% if (form.getStCustCategory1Desc()!=null) {%> 
         Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="14pt">
       <% if (form.getStRiskLocation()!=null) {%> 
         Risk Location :<%=JSPUtil.printX(form.getStRiskLocation())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStPostCode()!=null) {%> 
         Post Code :<%=JSPUtil.printX(form.getStPostCode())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStEntityName()!=null) {%> 
         Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCardNo()!=null) {%> 
         Risk Card No. :<%=JSPUtil.printX(form.getStRiskCardNo())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCode()!=null) {%> 
         Risk Code :<%=JSPUtil.printX(form.getStRiskCode())%>  
       <% } %>
      </fo:block> 
      
      
  
   <!-- GARIS  -->  
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block>  
      
       
      <fo:block font-size="8pt"> 
        <fo:table table-layout="fixed">  
         <fo:table-column column-width="10mm"/><!-- No -->
         <fo:table-column column-width="17mm"/><!-- Policy Date -->
         <fo:table-column column-width="17mm"/><!-- Period Start--> 
         <fo:table-column column-width="35mm"/><!-- Policy No -->
         <fo:table-column column-width="50mm"/><!-- The Insured -->
         <fo:table-column column-width="35mm"/><!-- TSI -->
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="30mm"/><!-- Policy Cost --> 
         <fo:table-column column-width="30mm"/><!-- Stamp Duty --> 
         <fo:table-column column-width="30mm"/><!-- Discount 2 -->
         <fo:table-column column-width="30mm"/><!-- Total Comm --> 
         <fo:table-column column-width="30mm"/><!-- HFee 1 -->
         <fo:table-column column-width="30mm"/><!-- Broker 1 -->
         <fo:table-column column-width="30mm"/><!-- Tax -->
         <fo:table-column column-width="30mm"/><!-- Premi Net -->
         <fo:table-column column-width="20mm"/><!-- Kode Broker --> 
         <fo:table-column column-width="15mm"/><!-- Kode Entry --> 

         <fo:table-column />
         <fo:table-header> 
          <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Entry Date.-L}{L-INA Tgl. Entry -L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
           	 <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Total Sum Insured-L}{L-INA Total Sum Insured-L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>  
       		 <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Stamp Duty-L}{L-INA Bea Materai-L}</fo:block></fo:table-cell>  
        	 <fo:table-cell ><fo:block text-align="center">{L-ENG Premium Discount-L}{L-INA Diskon Premi-L}</fo:block></fo:table-cell>  
      		 <fo:table-cell ><fo:block text-align="center">{L-ENG Total Comm-L}{L-INA Total Komisi-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Handling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>  
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG Brokerage Fee-L}{L-INA Brokerage Fee-L}</fo:block></fo:table-cell>  
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG Tax-L}{L-INA Pajak-L}</fo:block></fo:table-cell>
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG Premi Nett-L}{L-INA Tagihan-L}</fo:block></fo:table-cell>  
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG Relation Code-L}{L-INA Kode Relasi-L}</fo:block></fo:table-cell>  
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG KDE-L}{L-INA KDE-L}</fo:block></fo:table-cell> 
            </fo:table-row>  
           </fo:table-header>
         <fo:table-body>   
           
           
           
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="17">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
<%   
   
   
   BigDecimal [] t = new BigDecimal[30];
   
   
   int no=0;
   int no_sblm=0;
   for (int i = 0; i < l.size(); i++) { 
     
	    no=no+1;
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
       	
      //String pol_id1 = pol.getStPolicyID();
      //String pol_id2 = InsurancePolicyItemsView.getStPolicyID();
       		
      //if(pol_id1.equalsIgnoreCase(pol_id2))
      //	 tax1 = InsurancePolicyItemsView.getDbTaxAmount();
     // else	
     //  	 tax1 = new BigDecimal(3);
       		
       //tax1 = InsurancePolicyItemsView.getDbTaxAmount();
       	
      int n=0;   
      t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());   //total tsi 0
      t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());      // total premi bruto 1
      t[n] = BDUtil.add(t[n++], pol.getDbTotalItemAmount("PCOST"));   //total biaya polis 2
      t[n] = BDUtil.add(t[n++], pol.getDbTotalItemAmount("SFEE"));   //total biaya materai 3
      t[n] = BDUtil.add(t[n++], pol.getDbTotalFee());   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalDisc());   //total biaya diskon 5
      t[n] = BDUtil.add(t[n++], pol.getDbTotalComm());   //total komisi 6
      t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());   //total premi netto 7
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi3());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi4());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1Pct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc2Pct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1Pct());
      
      BigDecimal BFee;
      BigDecimal HFee;
      
      if(pol.getDbNDBrok1() == null){
      	t[n] = BDUtil.add(t[n++], new BigDecimal(0));   //21 Total Broker Fee
      	BFee = new BigDecimal(0);
      }
      else{
      	t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());   //21 Total Broker Fee
      	BFee = pol.getDbNDBrok1();
      	}
      

      t[n] = BDUtil.add(t[n++], pol.getDbNDHFeePct());   
      
      if(pol.getDbNDHFee() == null){
      	t[n] = BDUtil.add(t[n++], new BigDecimal(0));   //21 Total Broker HFee
      	HFee = new BigDecimal(0);
      }
      else{
      	t[n] = BDUtil.add(t[n++], pol.getDbNDHFee());   //21 Total Broker HFee
      	HFee = pol.getDbNDHFee();
      	}
      
      t[n] = BDUtil.add(t[n++], pol.getDbTaxAmount3()); //total pajak 24
      
      String no_polis = pol.getStPolicyNo();
      
      String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
         
   		t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount()); //total tsi 25
   	  
   	
      int p=-1;   
      
      final DTOList coins = pol.getCoins();;
      
      //int tes = objects.size();   
   
      while (true) {   
         p++;   
         //no++;
         
         no_sblm = no;
         
         if(p>0) no=no+1;
         
         //if(p==0) no=no;
         
         //if(coins.size()==1) no=i+1;
        
         
        if (p>=coins.size()) break;   
   
         final InsurancePolicyCoinsView obj = (InsurancePolicyCoinsView)coins.get(p);  
         
         if(obj.getStPositionCode().equals("LDR")&& obj.getDbPremiAmount()!=null){
         		premi_coins = obj.getDbPremiAmount();
         		hfee_coins = HFee;
         		tsi_coins = obj.getDbAmount();
         		disc_coins = obj.getDbDiscountAmount();
         		comm_coins = obj.getDbCommissionAmount();
         		broker_coins = obj.getDbBrokerageAmount();
         		//tax_coins = BDUtil.zero;
         }if(obj.getStPositionCode().equals("LDR")&& obj.getDbPremiAmount()==null){
         		premi_coins = pol.getDbPremiTotal();
         		 hfee_coins = HFee;
         		 tsi_coins = pol.getDbInsuredAmount();
         		 disc_coins = pol.getDbTotalDisc();
         		 comm_coins = pol.getDbTotalComm();
         		 broker_coins = BFee;
         		 //tax_coins = pol.getDbTaxAmount3();
         }
         else if(obj.getStPositionCode().equals("MEM") && obj.getDbPremiAmount()!=null){
         		premi_coins = obj.getDbPremiAmount();
         		hfee_coins = obj.getDbHandlingFeeAmount();
         		tsi_coins = obj.getDbAmount();
         		disc_coins = obj.getDbDiscountAmount();
         		comm_coins = obj.getDbCommissionAmount();
         	    broker_coins = obj.getDbBrokerageAmount();
         	    //tax_coins = BDUtil.zero;
         }else if(obj.getStPositionCode().equals("MEM") && obj.getDbPremiAmount()==null){
         		 premi_coins = pol.getDbPremiTotal();
         		 hfee_coins = HFee;
         		 tsi_coins = pol.getDbInsuredAmount();
         		 disc_coins = pol.getDbTotalDisc();
         		 comm_coins = pol.getDbTotalComm();
         		 broker_coins = BFee;
         		 //tax_coins = pol.getDbTaxAmount3();
         }
         
         if(obj.isHoldingCompany()){
         		bypol_coins = pol.getDbTotalItemAmount("PCOST");
         		sfee_coins = pol.getDbTotalItemAmount("SFEE");
         		tax_coins = pol.getDbTaxAmount3();
         }else{
         		bypol_coins = BDUtil.zero;
         		sfee_coins = BDUtil.zero;
         		tax_coins = BDUtil.zero;
   		 }
   		 
   		 netpremi_coins = BDUtil.sub(BDUtil.sub(premi_coins,disc_coins),BDUtil.add(broker_coins,comm_coins));

   		 
   		  	 total_premi_coins = BDUtil.add(total_premi_coins,premi_coins);  
 	
 			 total_hfee_coins = BDUtil.add(total_hfee_coins,hfee_coins);
 	
		 	 total_tsi_coins = BDUtil.add(total_tsi_coins,tsi_coins);
		 	
		 	 total_bypol_coins = BDUtil.add(total_bypol_coins,bypol_coins);
		 	
		 	 total_sfee_coins = BDUtil.add(total_sfee_coins,sfee_coins);
		 	
		 	 total_disc_coins = BDUtil.add(total_disc_coins,disc_coins);
		 	
		 	 total_comm_coins = BDUtil.add(total_comm_coins,comm_coins);
		 	
		 	 total_broker_coins = BDUtil.add(total_broker_coins,broker_coins);
		 	
		 	 total_tax_coins = BDUtil.add(total_tax_coins,tax_coins);
		 	
		 	 total_netpremi_coins = BDUtil.add(total_netpremi_coins,netpremi_coins);
   
%>   
			
   			
   			<fo:table-row>   
             <fo:table-cell ><fo:block text-align="center"><%=no%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtCreateDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
      		 <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
			 <fo:table-cell ><fo:block><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
			 <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(premi_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(bypol_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy Cost --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(sfee_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Stamp Duty --> 
       		 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(disc_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Discount --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(comm_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm --> 
       		 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(hfee_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- HFee 1 --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(broker_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tax_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(netpremi_coins,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premi Net --> 
			 <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStEntId2())%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 --> 
			 <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCreateWho())%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->               
           </fo:table-row>  
   			
<%   
      }
       no=no-1;  
   
      }
      %>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="17">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
   
           <fo:table-row>   
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>    
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell> 
             <fo:table-cell number-columns-spanned="2"><fo:block> TOTAL : </fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_tsi_coins,0)%></fo:block></fo:table-cell><!-- Total Premi --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_premi_coins,0)%></fo:block></fo:table-cell><!-- Total Premi --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_bypol_coins,0)%></fo:block></fo:table-cell><!-- Total Fee --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_sfee_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
 			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_disc_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
 			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_comm_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
 			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_hfee_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
 			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_broker_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_tax_coins,0)%></fo:block></fo:table-cell><!-- Total Disc -->
 		 	 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total_netpremi_coins,0)%></fo:block></fo:table-cell><!-- Total Disc --> 
 			 <fo:table-cell ><fo:block> </fo:block></fo:table-cell>			 
 			 <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="17" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   

         </fo:table-body>   
       </fo:table>   
       </fo:block> 
       
       <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block>    
    
    </fo:flow>   

  </fo:page-sequence>   
</fo:root>   