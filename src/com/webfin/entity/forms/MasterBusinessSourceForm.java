/***********************************************************************
 * Module:  com.webfin.entity.forms.MasterBusinessSourceForm
 * Author:  Denny Mahendra
 * Created: Jan 21, 2008 10:24:42 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.forms;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.IDFactory;
import com.crux.ff.model.FlexTableView;
import com.crux.pool.DTOPool;

public class MasterBusinessSourceForm extends Form {
   private DTOList list;
   private FlexTableView fft;
   private String selected;

   public String getSelected() {
      return selected;
   }

   public void setSelected(String selected) {
      this.selected = selected;
   }

   public FlexTableView getFft() {
      return fft;
   }

   public void setFft(FlexTableView fft) {
      this.fft = fft;
   }

   public DTOList getList() throws Exception {
      if (list==null) {
         list = new DTOList();
         list.activateFilter();
      }

      SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");

      sqa.addQuery("from ff_table");

      sqa.addOrder("ref1");

      sqa.addClause("fft_group_id='FFT_BUSSRC'");

      sqa.addFilter(list.getFilter());

      list = sqa.getList(FlexTableView.class);

      return list;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public void initialize() {
      super.initialize();    //To change body of overridden methods use File | Settings | File Templates.
      setFormMode("LIST","BUSINESS SOURCE MASTER");
   }

   public void btAdd() {
      fft = new FlexTableView();
      fft.markNew();

      fft.setStFFTGroupID("FFT_BUSSRC");

      setFormMode("EDIT","NEW BUSINESS SOURCE");
   }

   public void btEdit() {
      fft = (FlexTableView)DTOPool.getInstance().getDTO(FlexTableView.class, selected );

      fft.markUpdate();

      setFormMode("EDIT","EDIT BUSINESS SOURCE");
   }

   public void btnSave() throws Exception {

      if (fft.isNew()) fft.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT")));

      SQLUtil.qstore(fft);
      btnCancel();
   }

   public void btnCancel() {
      setFormMode("LIST","BUSINESS SOURCE MASTER");
   }
}
