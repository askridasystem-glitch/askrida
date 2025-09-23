/***********************************************************************
 * Module:  com.webfin.entity.forms.EntitySearchForm
 * Author:  Denny Mahendra
 * Created: Feb 14, 2006 5:10:40 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.forms;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLAssembler;
import com.webfin.entity.model.EntityView;

public class EntitySearchForm extends Form {

   private String doSearch;
   private String stKey;
   private String entityid;
   private String stInsuranceCompanyFlag;
   private DTOList entities;

   public EntitySearchForm() {

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

         sqa.addQuery("from ent_master a "
                 + " inner join ent_address b on b.ent_id = a.ent_id ");

         if (stInsuranceCompanyFlag!=null) {
            sqa.addClause("a.ins_company_flag = ?");
            sqa.addPar(stInsuranceCompanyFlag);
         }

         sqa.addSelect(" a.ent_id,a.ent_name,b.address ");
         sqa.addOrder("a.ent_name");

         sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");
         
        if(stKey!=null)
        {
                sqa.addClause("upper(a.ent_name||coalesce(b.address,'')) like ? ");
                final String key = "%"+((String) stKey).toUpperCase()+"%";
                sqa.addPar(key);
                sqa.setLimit(100);
        }


         entities = sqa.getList(EntityView.class);
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
