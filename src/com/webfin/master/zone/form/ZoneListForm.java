/***********************************************************************
 * Module:  com.webfin.master.treaty.form.TreatyListForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.master.zone.form;

import com.crux.web.form.Form;
import com.crux.web.form.FormManager;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.model.InsuranceZoneLimitView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ZoneListForm extends Form {

   private DTOList list;
   private String zoneid;

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public String getZoneid() {
      return zoneid;
   }

   public void setZoneid(String zoneid) {
      this.zoneid = zoneid;
   }

   public DTOList getList() throws Exception {

      if (list==null) {
         list=new DTOList();
         list.getFilter().activate();
         list.getFilter().orderKey="zone_id";
      }

      SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");

      sqa.addQuery(
              "from ins_zone_limit"
      );

      sqa.addFilter(list.getFilter());

      list = sqa.getList(InsuranceZoneLimitView.class);

      return list;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public void clickCreate() throws Exception {
      ZoneMasterForm x = (ZoneMasterForm) super.newForm("zone_form",this);

      x.createNew();

      x.show();

   }

   public void clickEdit() throws Exception {

      ZoneMasterForm x = (ZoneMasterForm) super.newForm("zone_form",this);

      x.setAttribute("zoneid",zoneid);
      
      x.edit();

      x.show();
   }

   public void clickView() throws Exception {
      ZoneMasterForm x = (ZoneMasterForm) super.newForm("zone_form",this);

      x.setAttribute("zoneid",zoneid);
      
      x.view();

      x.show();

   }

   public void list() {

   }
}