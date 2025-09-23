/***********************************************************************
 * Module:  com.webfin.ar.forms.BungaDepositoList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.BungaDepositoHeaderView;
import com.webfin.gl.model.BungaDepositoView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class BungaDepositoList extends Form {
    private BungaDepositoView budep;
    private BungaDepositoHeaderView header;
    private DTOList list;
    private String ARBungaID;
    private Date transdatefrom;
    private Date transdateto;
    private String transNumber;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("BUDEP_NAVBR");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("BUDEP_APRV");
    private String stEntityID;
    private String stEntityName;
    private String stDescription;
    private String transBudep;
    private String stTransactionHeaderID;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }
    
    public DTOList getList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        
        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ar_bunga_id desc";
        }
        
        sqa.addSelect(" * ");
        
        sqa.addQuery(
                " from ar_bunga_deposito ");
        
        if (transdatefrom!=null) {
            sqa.addClause("date_trunc('day',applydate) >= ?");
            sqa.addPar(transdatefrom);
        }
        
        if (transdateto!=null) {
            sqa.addClause("date_trunc('day',applydate) <= ?");
            sqa.addPar(transdateto);
        }

        if (transBudep!=null){
            sqa.addClause("trx_no like ?");
            sqa.addPar(transBudep+"%");
        }
        
        if (transNumber!=null){
            sqa.addClause("trx_hdr_no like ?");
            sqa.addPar("%"+transNumber+"%");
        }
        
        if (stEntityID!=null){
            sqa.addClause("hdr_accountid = ?");
            sqa.addPar(stEntityID);
        }
        
        if (branch!=null){
            sqa.addClause("cc_code = ?");
            sqa.addPar(branch);
        }
        
        sqa.addFilter(list.getFilter());
        
        sqa.addOrder("trx_id desc");
        
        sqa.setLimit(300);
        
        list = sqa.getList(BungaDepositoView.class);
        
        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickRefresh() {
    }
    
    public void btnSearch() throws Exception {
        getList().getFilter().setCurrentPage(0);
    }
    
    public Date getTransdatefrom() {
        return transdatefrom;
    }
    
    public void setTransdatefrom(Date transdatefrom) {
        this.transdatefrom = transdatefrom;
    }
    
    public Date getTransdateto() {
        return transdateto;
    }
    
    public void setTransdateto(Date transdateto) {
        this.transdateto = transdateto;
    }
    
    public String getTransNumber() {
        return transNumber;
    }
    
    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getStEntityName() {
        return stEntityName;
    }
    
    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }
    
    public String getStDescription() {
        return stDescription;
    }
    
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }
    
    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }
    
    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }
        
    public String getStEntityID() {
        return stEntityID;
    }
    
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }    

    public String getARBungaID() {
        return ARBungaID;
    }

    public void setARBungaID(String ARBungaID) {
        this.ARBungaID = ARBungaID;
    }
    
    public void clickUploadExcel() throws Exception {
        final BungaDepositoForm form = (BungaDepositoForm)newForm("budepform", this);
        
        form.createNewUpload();
        
        form.show();
    }

    public void clickEdit() throws Exception {

        //if (getBungaDeposito().isApproved()) {
        //    throw new Exception("Data Sudah Disetujui");
        //}

        validateBudepID();

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.editBudep(ARBungaID);

        form.show();
    }

    public void clickApprove() throws Exception {

        //if (getBungaDeposito().isApproved()) {
        //    throw new Exception("Data Sudah Disetujui");
        //}
        validateBudepID();

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.saveBudep(ARBungaID);

        form.show();
    }

    public void clickInput() throws Exception {

        //if (!getBungaDeposito().isApproved()) {
        //    throw new Exception("Data Belum Disetujui");
        //}
        validateBudepID();

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.inputBudep(ARBungaID);

        form.show();
    }

    public void clickView() throws Exception {

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.viewBudep(ARBungaID);

        form.show();
    }

    public void clickReverse() throws Exception {

        //if (!getBungaDeposito().isApproved()) {
        //    throw new Exception("Data Belum Disetujui");
        //}
        validateBudepID();

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.reverseBudep(ARBungaID);

        form.show();
    }

    public void clickDelete() throws Exception {

        //if (getBungaDeposito().isApproved()) {
        //    throw new Exception("Data Sudah Disetujui");
        //}

        validateBudepID();

        BungaDepositoForm form = (BungaDepositoForm) super.newForm("budepform",this);

        form.deleteBudep(ARBungaID);

        form.show();
    }

    /**
     * @return the transBudep
     */
    public String getTransBudep() {
        return transBudep;
    }

    /**
     * @param transBudep the transBudep to set
     */
    public void setTransBudep(String transBudep) {
        this.transBudep = transBudep;
    }

    /**
     * @return the canApprove
     */
    public boolean isCanApprove() {
        return canApprove;
    }

    /**
     * @param canApprove the canApprove to set
     */
    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    private void validateBudepID() {
        if (ARBungaID == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }
}
