/***********************************************************************
 * Module:  com.webfin.ar.forms.FRRPTrptSOAInsuranceForm
 * Author:  Achmad Rhodoni
 * Created: June 10, 2009 10:55:04 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.web.form.Form;

public class FRRPTrptSOAInsuranceForm extends Form {
   public void print() {
      redirect("/pages/ar/report/rptSOAReinsurance.fop");
   }
}
