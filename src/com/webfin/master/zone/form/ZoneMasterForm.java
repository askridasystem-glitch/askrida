/***********************************************************************
 * Module:  com.webfin.master.treaty.form.TreatyMasterForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.master.zone.form;

import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.SQLUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.common.controller.FormTab;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.WebFinLOVRegistry;
import com.webfin.insurance.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class ZoneMasterForm extends Form {

   private InsuranceZoneLimitView zone;

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public void createNew() {
      zone= new InsuranceZoneLimitView();

      zone.markNew();

      setTitle("CREATE BLOCK RISK");

   }

   public void edit() {
   	  final String zone_id = (String)getAttribute("zoneid");
      
      zone = (InsuranceZoneLimitView) DTOPool.getInstance().getDTO(InsuranceZoneLimitView.class, zone_id);

      zone.markUpdate();

      setTitle("EDIT BLOCK RISK");
   }

   public void view() {
   	  final String zone_id = (String)getAttribute("zoneid");
   	  
      zone = (InsuranceZoneLimitView) DTOPool.getInstance().getDTO(InsuranceZoneLimitView.class, zone_id);

      setReadOnly(true);

      setTitle("VIEW BLOCK RISK");
   }

   public void save() throws Exception {

      getRemoteInsurance().save(zone);

      close();
   }

   public void close() {
      super.close();
   }

   public InsuranceZoneLimitView getZone() {
      return zone;
   }

   public void setZone(InsuranceZoneLimitView zone) {
      this.zone = zone;
   }

}
