/***********************************************************************
 * Module:  com.webfin.gl.report.form.GLRptDesignerForm
 * Author:  Denny Mahendra
 * Created: Feb 5, 2006 10:06:01 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.report.form;

import com.crux.web.form.Form;
import com.crux.common.controller.FormTab;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.webfin.gl.model.GLReportView;
import com.webfin.gl.model.GLReportColumnView;
import com.webfin.gl.model.GLReportLineView;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class GLRptDesignerForm extends Form {
   private FormTab tabs;
   private String glreportid;
   private GLReportView rpt;
   private Long lineIndex;
   private Long colIndex;

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public String getGlreportid() {
      return glreportid;
   }

   public void setGlreportid(String glreportid) {
      this.glreportid = glreportid;
   }

   public void newReport() {
      rpt = new GLReportView();
      rpt.setColumns(new DTOList());
      rpt.setLines(new DTOList());

      rpt.markNew();
   }

   public void editReport() throws Exception {
      viewReport();
      rpt.markUpdate();

      getColumns().markAllUpdate();
      getLines().markAllUpdate();
   }

   public void viewReport() throws Exception {
      rpt = getRemoteGeneralLedger().getReportDefinition((String) getAttribute("glreportid"));
   }

   public FormTab getTabs() {
      if (tabs==null) {
         tabs = new FormTab();

         tabs.add(new FormTab.TabBean("TAB_COLUMN","COLUMNS",true));
         tabs.add(new FormTab.TabBean("TAB_LINES","LINES",true));

         tabs.setActiveTab("TAB_COLUMN");
      }
      return tabs;
   }

   public void setTabs(FormTab tabs) {
      this.tabs = tabs;
   }

   public DTOList getColumns() {
      return rpt.getColumns();
   }

   public DTOList getLines(){
      return rpt.getLines();
   }

   public GLReportView getRpt() {
      return rpt;
   }

   public void setRpt(GLReportView rpt) {
      this.rpt = rpt;
   }

   public void clickColNew() {
      final GLReportColumnView col = new GLReportColumnView();
      col.markNew();
      getColumns().add(col);
   }

   public void clickColDelete() {
      getColumns().delete(colIndex.intValue());
   }

   public void clickLineNew() {
      final GLReportLineView lin = new GLReportLineView();
      lin.markNew();
      getLines().add(lin);
   }

   public void clickLineDelete() {
      getLines().delete(lineIndex.intValue());
   }

   public void clickSave() throws Exception {
      getRemoteGeneralLedger().saveReport(rpt);
      close();
   }

   public void clickCancel() {
      close();
   }

   public Long getLineIndex() {
      return lineIndex;
   }

   public void setLineIndex(Long lineIndex) {
      this.lineIndex = lineIndex;
   }

   public Long getColIndex() {
      return colIndex;
   }

   public void setColIndex(Long colIndex) {
      this.colIndex = colIndex;
   }
}
