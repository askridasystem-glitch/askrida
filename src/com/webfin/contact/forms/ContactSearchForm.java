/***********************************************************************
 * Module:  com.webfin.entity.forms.EntitySearchForm
 * Author:  Denny Mahendra
 * Created: Feb 14, 2006 5:10:40 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.contact.forms;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLAssembler;
import com.webfin.contact.model.ContactView;

public class ContactSearchForm extends Form {

   private String doSearch;
   private String stKey;
   private String entityid;
   private String stInsuranceCompanyFlag;
   private DTOList entities;

   public ContactSearchForm() {

   }

   public String getStKey() {
      return stKey;
   }

   public void setStKey(String stKey) {
      this.stKey = stKey;
   }

   public String getStInsuranceCompanyFlag() {
      return stInsuranceCompanyFlag;
   }

   public void setStInsuranceCompanyFlag(String stInsuranceCompanyFlag) {
      this.stInsuranceCompanyFlag = stInsuranceCompanyFlag;
   }

   public DTOList getEntities() throws Exception {
      if (doSearch!=null) {
         final SQLAssembler sqa = new SQLAssembler();

         sqa.addQuery("from customer_master");
         if (stInsuranceCompanyFlag!=null) {
            sqa.addClause("ins_company_flag = ?");
            sqa.addPar(stInsuranceCompanyFlag);
         }
         sqa.addSelect("customer_master.*");
         sqa.addOrder("ent_name");

         entities = sqa.getList(ContactView.class);
      }
      if (entities==null) entities=new DTOList();
      return entities;
   }

   public void setEntities(DTOList entities) {
      this.entities = entities;
   }

   public String getEntityid() {
      return entityid;
   }

   public void setEntityid(String entityid) {
      this.entityid = entityid;
   }

   public String getDoSearch() {
      return doSearch;
   }

   public void setDoSearch(String doSearch) {
      this.doSearch = doSearch;
   }
}
