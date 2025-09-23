/************************************************************************
 * Module:  com.webfin.ar.forms.ARReportForm
 * Author:  Denny Mahendra
 * Created: Apr 28, 2006 10:14:07 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.SQLAssembler;
import com.crux.util.DTOList;
import com.crux.util.fop.FOPTableSource;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.ejb.CurrencyManager;
import java.util.Date;

public class ARReportForm extends Form {
	
	private Date policyDateFrom;
   private Date policyDateTo;
   private Date periodFrom;
   private Date periodTo;
   
   private String stEntityName;

   private String stPolicyTypeDesc;
   private String stBranchDesc;
   private String stFltCoverTypeDesc;
   private String stCustCategory1Desc;

   private String stPolicyClass;
   private String stPolicyType;
   private String stBranch;
   private String stCoverType;
   private String stBussiness;
   private String stCustomer;	
   private String stPolicyNo;
   
   private String stPrintForm;
   private boolean enableRiskFilter;
   
	
   private String mode;
   
   public String getStEntityName() {
      return stEntityName;
   }

   public void setStEntityName(String stEntityName) {
      this.stEntityName = stEntityName;
   }
   
   public String getStPolicyClass() {
      return stPolicyClass;
   }

   public void setStPolicyClass(String stPolicyClass) {
      this.stPolicyClass = stPolicyClass;
   }
   
   public String getStPolicyType() {
      return stPolicyType;
   }

   public void setStPolicyType(String stPolicyType) {
      this.stPolicyType = stPolicyType;
   }
   
   public String getStPolicyTypeDesc() {
      return stPolicyTypeDesc;
   }

   public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
      this.stPolicyTypeDesc = stPolicyTypeDesc;
   }
   
   public String getStFltCoverTypeDesc() {
      return stFltCoverTypeDesc;
   }

   public void setStFltCoverTypeDesc(String stFltCoverTypeDesc) {
      this.stFltCoverTypeDesc = stFltCoverTypeDesc;
   }
   
   public String getStCustCategory1Desc() {
      return stCustCategory1Desc;
   }

   public void setStCustCategory1Desc(String stCustCategory1Desc) {
      this.stCustCategory1Desc = stCustCategory1Desc;
   }
   
   public String getStBranch() {
      return stBranch;
   }

   public void setStBranch(String stBranch) {
      this.stBranch = stBranch;
   }
   
   public String getStBranchDesc() {
      return stBranchDesc;
   }

   public void setStBranchDesc(String stBranchDesc) {
      this.stBranchDesc = stBranchDesc;
   }
   
   public String getStCoverType() {
      return stCoverType;
   }

   public void setStCoverType(String stCoverType) {
      this.stCoverType = stCoverType;
   }
   
   public void onChangePolicyTypeGroup() {

   }
   
   public String getStBussiness() {
      return stBussiness;
   }

   public void setStBussiness(String stBussiness) {
      this.stBussiness = stBussiness;
   }
   
   public String getStCustomer() {
      return stCustomer;
   }

   public void setStCustomer(String stCustomer) {
      this.stCustomer = stCustomer;
   }
   
   public String getStPolicyNo() {
      return stPolicyNo;
   }

   public void setStPolicyNo(String stPolicyNo) {
      this.stPolicyNo = stPolicyNo;
   }
   
   public Date getPolicyDateFrom() {
      return policyDateFrom;
   }

   public void setPolicyDateFrom(Date policyDateFrom) {
      this.policyDateFrom = policyDateFrom;
   }
   
   public Date getPolicyDateTo() {
      return policyDateTo;
   }

   public void setPolicyDateTo(Date policyDateTo) {
      this.policyDateTo = policyDateTo;
   }
   
   public Date getPeriodFrom() {
      return periodFrom;
   }

   public void setPeriodFrom(Date periodFrom) {
      this.periodFrom = periodFrom;
   }
   
   public Date getPeriodTo() {
      return periodTo;
   }

   public void setPeriodTo(Date periodTo) {
      this.periodTo = periodTo;
   }
   
   public boolean isEnableRiskFilter() {
      return enableRiskFilter;
   }

   public void setEnableRiskFilter(boolean enableRiskFilter) {
      this.enableRiskFilter = enableRiskFilter;
   }
   
   public String getMode() {
      return mode;
   }

   public void setMode(String mode) {
      this.mode = mode;
   }

   public void initialize() {
      mode = (String)getAttribute("mode");
   }

   public void clickPrint() throws Exception {
      laodReport();
      super.redirect("/pages/ar/report/arreport.fop");
   }
   
   public void uwrit() {
      enableRiskFilter = true;
   }

   private void laodReport() throws Exception {

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("b.gl_code, a.*");
      sqa.addQuery("from ar_invoice a");
      sqa.addQuery(" inner join ent_master b on b.ent_id = a.ent_id");
      sqa.addQuery(" inner join gl_accounts c on c.account_id = a.gl_ar");
      sqa.addQuery(" inner join ins_policy d on d.pol_no = a.attr_pol_no");
	  sqa.addQuery(" inner join ins_policy_subtype e on e.pol_type_id = d.pol_type_id");
      sqa.addOrder("b.gl_code");

      sqa.addClause("a.invoice_type=?");
      sqa.addPar(mode);
      
      sqa.addClause("ar_trx_type_id=?");
      if(mode.equalsIgnoreCase("AP")) sqa.addPar("13");
      else if(mode.equalsIgnoreCase("AR")) sqa.addPar("14");
      
      if(getStCustomer()!=null){
      	 sqa.addClause("b.ent_id=?");
      	 sqa.addPar(stCustomer);
      }
      
      if(getStPolicyNo()!=null){
      	sqa.addClause("a.attr_pol_no=?");
      	sqa.addPar(stPolicyNo);
      }
      
      if(getStPolicyClass()!=null){
      	sqa.addClause("e.pol_type_id=?");
      	sqa.addPar(stPolicyClass);
      }
      
      if(getStPolicyType()!=null){
      	sqa.addClause("a.attr_pol_type_id=?");
      	sqa.addPar(stPolicyType);
      }
      
      if(getStBranch()!=null){
      	sqa.addClause("a.cc_code=?");
      	sqa.addPar(stBranch);
      }
      
      if(getStCoverType()!=null){
      	sqa.addClause("d.cover_type_code=?");
      	sqa.addPar(stCoverType);
      }
      
      if(getStBussiness()!=null){
      	sqa.addClause("b.category1=?");
      	sqa.addPar(stBussiness);
      }
      
      if(getPolicyDateFrom()!=null){
      	sqa.addClause("a.invoice_date>=?");
      	sqa.addPar(policyDateFrom);
      }
      
      if(getPolicyDateTo()!=null){
      	sqa.addClause("a.due_date<=?");
      	sqa.addPar(policyDateTo);
      }
      
      if(getPeriodFrom()!=null){
      	sqa.addClause("a.attr_pol_per_0>=?");
      	sqa.addPar(periodFrom);
      }
      
      if(getPeriodTo()!=null){
      	sqa.addClause("a.attr_pol_per_1<?");
      	sqa.addPar(periodTo);
      }

      final DTOList l = sqa.getList(ARInvoiceView.class);

      final FOPTableSource ts = new FOPTableSource(
              10,
              new int [] {2,6,6,4,16,2,5,2,5,10},
              25.7,
              "cm"
              ) {

         public int getRowCount() {
            return l.size();
         }

         public String getColumnHeader( int columnNo) {
            switch (columnNo) {
               case 0: return "KODE";
               case 1: return "Reasuradur";
               case 2: return "Akunt";
               case 3: return "Tanggal";
               case 4: return "Uraian";
               case 5: return "Valuta";
               case 6: return "Amount";
               case 7: return "Valuta";
               case 8: return "Amount";
               case 9: return "No Surat Hutang";
               //case 10: return "Usia Tgl";
            }

            return "?";
         }

         public String getColumnAlign(int rowNo, int columnNo) {
            if (columnNo==6) return "end";
            if (columnNo==8) return "end";
            return "start";
         }


         public Object getColumnValue(int rowNo, int columnNo) {
            final ARInvoiceView iv = (ARInvoiceView) l.get(rowNo);

            switch (columnNo) {
               case 0: return iv.getStGLCode();
               case 1: return iv.getStEntityName();
               case 2: return iv.getAccount().getStAccountNo();
               case 3: return iv.getDtInvoiceDate();
               case 4: return iv.getAccount().getStDescription();
               case 5: return iv.getStCurrencyCode();
               case 6: return iv.getDbOutstandingAmount();
               case 7: return CurrencyManager.getInstance().getMasterCurrency();
               case 8: return iv.getDbOutstandingAmountIDR();
               case 9: return iv.getStNoSuratHutang();
               //case 10: return (new Date().getTime() - iv.getDtInvoiceDate().getTime()) ;
            }

            return null;
         };
      };

      SessionManager.getInstance().getRequest().setAttribute("RPT", ts);
      SessionManager.getInstance().getRequest().setAttribute("FRX", this);
   }
}
