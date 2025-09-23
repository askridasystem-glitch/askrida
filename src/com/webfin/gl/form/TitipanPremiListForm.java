/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;
import com.webfin.gl.model.TitipanPremiView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class TitipanPremiListForm extends Form {
   private DTOList titipanPremiList;
   private String entityid;
   private String branch;

   public void initialize() {
      branch = SessionManager.getInstance().getSession().getStBranch();
   }

   public String getBranch() {
      return branch;
   }

   public void setBranch(String branch) {
      this.branch = branch;
   }

   public String getEntityid() {
      return entityid;
   }

   public void setEntityid(String entityid) {
      this.entityid = entityid;
   }

   private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB",EntityManagerHome.class.getName()))
            .create();
   }

   public void clickCreate() throws Exception {
      final TitipanPremiForm form = (TitipanPremiForm) super.newForm("entity_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final TitipanPremiForm form = (TitipanPremiForm) super.newForm("entity_edit",this);

      form.setAttribute("ent_id",entityid);

      form.edit();

      form.show();
   }

   public void clickView() throws Exception {
      final TitipanPremiForm form = (TitipanPremiForm) super.newForm("entity_edit",this);

      form.setAttribute("ent_id",entityid);

      form.view();

      form.show();
   }

   public void refresh() {
   }

    /**
     * @return the titipanPremiList
     */
    public DTOList getTitipanPremiList()throws Exception {
        if (getTitipanPremiList()==null) {
            setTitipanPremiList(new DTOList());
            getTitipanPremiList().getFilter().activate();
        }

        final SQLAssembler sqa = getSQATitipanPremi();

        sqa.addFilter(getTitipanPremiList().getFilter());

        setTitipanPremiList(sqa.getList(TitipanPremiView.class));

        return getTitipanPremiList();
    }

    /**
     * @param titipanPremiList the titipanPremiList to set
     */
    public void setTitipanPremiList(DTOList titipanPremiList) {
        this.titipanPremiList = titipanPremiList;
    }

    public SQLAssembler getSQATitipanPremi() {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.*,b.accountno ");

        sqa.addQuery(" from ar_titipan_premi a " +
                " inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause(" a.balance <> 0 ");

        /*
        if(transdatefrom!=null) {
            sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
            sqa.addPar(transdatefrom);
        }

        if(transdateto!=null) {
            sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
            sqa.addPar(transdateto);
        }

        if(entrydatefrom!=null) {
            sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
            sqa.addPar(entrydatefrom);
        }

        if(entrydateto!=null) {
            sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
            sqa.addPar(entrydateto);
        }


        if (stEntityID!=null) {
            sqa.addClause("a.hdr_accountid = ?");
            sqa.addPar(stEntityID);
        }

        if(transNumber!=null) {
            sqa.addClause("a.trx_no like ?");
            sqa.addPar("%"+transNumber+"%");
        }

        if(accountCode!=null) {
            sqa.addClause("b.accountno = ?");
            sqa.addPar(accountCode);
        }

        if(description!=null) {
            sqa.addClause("a.description like ?");
            sqa.addPar("%"+description+"%");
        }*/

        if (branch!=null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(branch);
        }

        sqa.addOrder("a.trx_id desc"); 
        sqa.setLimit(300);

        return sqa;
    }

}
