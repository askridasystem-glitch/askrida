/***********************************************************************
 * Module:  com.webfin.gl.form.GLJournalList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.gl.form;

import com.crux.common.model.Filter;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.AccountMarketingView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.JournalSyariahView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.RKAPGroupView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class GLJournalList extends Form {

    private DTOList list;
    private JournalView journal;
    private String memorialID;
    private String description;
    private String branch;

    private String syariahID;
    private String transNumber;
    private Date transDate;
    private DTOList listSyariah;

    private String rkapID;
    private DTOList listRkap;
    private String years;

    private DTOList cashbankList;

    private String accountCode;
    private Date transdate;
    private Date transdatefrom;
    private Date transdateto;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    private final static transient LogManager logger = LogManager.getInstance(GLJournalList.class);

    public DTOList getList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (list == null) {
            list = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }

        sqa.addSelect(" b.accountno,substr(b.accountno,14,2) as cc_code,a.* ");

        sqa.addQuery(" from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause(" ref_trx_type = 'MEMORIAL' ");

        if (description != null) {
            sqa.addClause("upper(a.description) like ?");
            sqa.addPar("%" + description.toUpperCase() + "%");
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("trx_id desc");

        sqa.setLimit(300);

        list = sqa.getList(JournalView.class);

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

   public void clickUploadExcel() throws Exception {
      final GLJournalForm form = (GLJournalForm)newForm("memorialform", this);

      form.createNewUpload();

      form.show();

   }

    /**
     * @return the memorialID
     */
    public String getMemorialID() {
        return memorialID;
    }

    /**
     * @param memorialID the memorialID to set
     */
    public void setMemorialID(String memorialID) {
        this.memorialID = memorialID;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the journal
     */
    public JournalView getJournal() {
        journal = (JournalView) DTOPool.getInstance().getDTO(JournalView.class, memorialID);
        return journal;
    }

    /**
     * @param journal the journal to set
     */
    public void setJournal(JournalView journal) {
        this.journal = journal;
    }

    public String getSyariahID() {
        return syariahID;
    }

    /**
     * @param syariahID the syariahID to set
     */
    public void setSyariahID(String syariahID) {
        this.syariahID = syariahID;
    }

    /**
     * @return the listSyariah
     */
    public DTOList getListSyariah() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listSyariah == null) {
            listSyariah = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listSyariah.setFilter(filter.activate());
        }

        sqa.addSelect(" b.accountno,substr(b.accountno,14,2) as cc_code,a.* ");

        sqa.addQuery(" from gl_je_detail_syariah a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        if (transNumber != null) {
            sqa.addClause("a.trx_no like ?");
            sqa.addPar("%" + transNumber.toUpperCase() + "%");
        }

        if (transDate != null) {
            sqa.addClause("a.period_no = ? ");
            sqa.addClause("a.fiscal_year = ? ");
            sqa.addPar(DateUtil.getMonthDigit(transDate));
            sqa.addPar(DateUtil.getYear(transDate));
        }

        sqa.addFilter(listSyariah.getFilter());

        sqa.addOrder("trx_id desc");

        sqa.setLimit(300);

        listSyariah = sqa.getList(JournalSyariahView.class);

        return listSyariah;
    }

    public void btnSearchSyariah() throws Exception {
        getListSyariah().getFilter().setCurrentPage(0);
    }

    public void setListSyariah(DTOList listSyariah) {
        this.listSyariah = listSyariah;
    }

    /**
     * @return the transNumber
     */
    public String getTransNumber() {
        return transNumber;
    }

    /**
     * @param transNumber the transNumber to set
     */
    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    /**
     * @return the transDate
     */
    public Date getTransDate() {
        return transDate;
    }

    /**
     * @param transDate the transDate to set
     */
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public void clickUploadExcelSyariah() throws Exception {
        final GLJournalForm form = (GLJournalForm) newForm("syariahform", this);

        form.createNewUploadSyariah();

        form.show();

    }

    public void btnSearchRKAP() throws Exception {
        getListRkap().getFilter().setCurrentPage(0);
    }

    /**
     * @return the rkapID
     */
    public String getRkapID() {
        return rkapID;
    }

    /**
     * @param rkapID the rkapID to set
     */
    public void setRkapID(String rkapID) {
        this.rkapID = rkapID;
    }

    /**
     * @return the listRkap
     */
    public DTOList getListRkap() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listRkap == null) {
            listRkap = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listRkap.setFilter(filter.activate());
        }

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from gl_rkap_group a ");
        sqa.addClause("a.active_flag = 'Y'");

        if (years != null) {
            sqa.addClause("a.years = ?");
            sqa.addPar(years);
        }

        sqa.addFilter(listRkap.getFilter());

        sqa.addOrder(" a.years desc,a.rkap_group_id asc ");

        sqa.setLimit(300);

        listRkap = sqa.getList(RKAPGroupView.class);

        return listRkap;
    }

    /**
     * @param listRkap the listRkap to set
     */
    public void setListRkap(DTOList listRkap) {
        this.listRkap = listRkap;
    }

    /**
     * @return the years
     */
    public String getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(String years) {
        this.years = years;
    }

    public void clickUploadExcelRKAP() throws Exception {
        final GLJournalForm form = (GLJournalForm) newForm("rkapform", this);

        form.createNewUploadRKAP();

        form.show();

    }

    /**
     * @return the cashbankList
     */
    public DTOList getCashbankList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (cashbankList == null) {
            cashbankList = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            cashbankList.setFilter(filter.activate());
        }

        sqa.addSelect(" b.accountno,substr(b.accountno,14,2) as cc_code,a.* ");

        sqa.addQuery(" from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause(" ref_trx_type = 'CASHBANK' ");

        if (description != null) {
            sqa.addClause("upper(a.description) like ?");
            sqa.addPar("%" + description.toUpperCase() + "%");
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        sqa.addFilter(cashbankList.getFilter());

        sqa.addOrder("trx_id desc");

        sqa.setLimit(300);

        cashbankList = sqa.getList(JournalView.class);

        return cashbankList;
    }

    /**
     * @param cashbankList the cashbankList to set
     */
    public void setCashbankList(DTOList cashbankList) {
        this.cashbankList = cashbankList;
    }

    public void clickUploadCashBank() throws Exception {
      final GLJournalForm form = (GLJournalForm)newForm("cashbank_upload_form", this);

      form.createNewUploadCashBank();

      form.show();

   }

    private DTOList accEntList;
    private DTOList accProList;

    /**
     * @return the accountlist
     */
    public DTOList getAccEntList() throws Exception {

        if (accEntList == null) {
            accEntList = new DTOList();
            final Filter filter = new Filter();
            accEntList.setFilter(filter.activate());
        }

        final SQLAssembler sqa = getSQAMkt();

        sqa.addFilter(accEntList.getFilter());

        accEntList = sqa.getList(AccountMarketingView.class);

        return accEntList;
    }

    /**
     * @param accountlist the accountlist to set
     */
    public void setAccEntList(DTOList accEntList) {
        this.setAccEntList(accEntList);
    }

    public void clickEditMkt() throws Exception {

        GLJournalForm x = (GLJournalForm) super.newForm("accountEntertainForm", this);

        x.setAttribute("memorialID", memorialID);

        x.edit();

        x.show();
    }

    public void clickViewMkt() throws Exception {
        GLJournalForm x = (GLJournalForm) super.newForm("accountEntertainForm", this);

        x.setAttribute("memorialID", memorialID);

        x.view();

        x.show();

    }

    /**
     * @return the accProList
     */
    public DTOList getAccProList() throws Exception {

        if (accProList == null) {
            accProList = new DTOList();
            final Filter filter = new Filter();
            accProList.setFilter(filter.activate());
        }

        final SQLAssembler sqa = getSQAPro();

        sqa.addFilter(accProList.getFilter());

        accProList = sqa.getList(AccountMarketingView.class);

        return accProList;
    }

    /**
     * @param accProList the accProList to set
     */
    public void setAccProList(DTOList accProList) {
        this.accProList = accProList;
    }

    public void clickEditPro() throws Exception {

        GLJournalForm x = (GLJournalForm) super.newForm("accountPromosiForm", this);

        x.setAttribute("memorialID", memorialID);

        x.edit();

        x.show();
    }

    public void clickViewPro() throws Exception {
        GLJournalForm x = (GLJournalForm) super.newForm("accountPromosiForm", this);

        x.setAttribute("memorialID", memorialID);

        x.view();

        x.show();

    }

    /**
     * @return the accountCode
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * @param accountCode the accountCode to set
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    /**
     * @return the transdate
     */
    public Date getTransdate() {
        return transdate;
    }

    /**
     * @param transdate the transdate to set
     */
    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    /**
     * @return the transdatefrom
     */
    public Date getTransdatefrom() {
        return transdatefrom;
    }

    /**
     * @param transdatefrom the transdatefrom to set
     */
    public void setTransdatefrom(Date transdatefrom) {
        this.transdatefrom = transdatefrom;
    }

    /**
     * @return the transdateto
     */
    public Date getTransdateto() {
        return transdateto;
    }

    /**
     * @param transdateto the transdateto to set
     */
    public void setTransdateto(Date transdateto) {
        this.transdateto = transdateto;
    }

    public void btnSearchEnt() throws Exception {
        getAccEntList().getFilter().setCurrentPage(0);
    }

    public void btnSearchPro() throws Exception {
        getAccProList().getFilter().setCurrentPage(0);
    }

    public void btnPrintMkt() {
        super.redirect("/pages/gl/report/rpt_mkt.fop");
    }

    public void btnPrintPro() {
        super.redirect("/pages/gl/report/rpt_prom.fop");
    }

    public SQLAssembler getSQAMkt() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from gl_acc_marketing a ");

        sqa.addClause(" a.subtype = 'MARKETING' ");

        if (accountCode != null) {
            sqa.addClause("a.accountno like ?");
            sqa.addPar(accountCode.toUpperCase() + "%");
        }

        if (description != null) {
            sqa.addClause("to_tsvector('english', a.descno) @@ to_tsquery('english',?)");
            sqa.addPar(description.toUpperCase().replaceAll(" ", "&"));
        }

        if (branch != null) {
            sqa.addClause("substr(a.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        if (transNumber != null) {
            sqa.addClause("a.nobuk = ?");
            sqa.addPar(transNumber.toUpperCase());
        }

        if (transdatefrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(transdatefrom);
        }

        if (transdateto != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(transdateto);
        }

        sqa.addOrder("a.trx_id desc");

        if (description == null && transNumber == null && accountCode == null && transdatefrom == null && transdateto == null) {
            sqa.setLimit(100);
        }

        return sqa;
    }

    public SQLAssembler getSQAPro() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from gl_acc_marketing a ");

        sqa.addClause(" a.subtype = 'PROMOSI' ");

        if (accountCode != null) {
            sqa.addClause("a.accountno like ?");
            sqa.addPar(accountCode.toUpperCase() + "%");
        }

        if (description != null) {
            sqa.addClause("to_tsvector('english', a.descno) @@ to_tsquery('english',?)");
            sqa.addPar(description.toUpperCase().replaceAll(" ", "&"));
        }

        if (branch != null) {
            sqa.addClause("substr(a.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        if (transNumber != null) {
            sqa.addClause("a.nobuk = ?");
            sqa.addPar(transNumber.toUpperCase());
        }

        if (transdatefrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(transdatefrom);
        }

        if (transdateto != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(transdateto);
        }

        sqa.addOrder("a.trx_id desc");

        if (description == null && transNumber == null && accountCode == null && transdatefrom == null && transdateto == null) {
            sqa.setLimit(100);
        }

        return sqa;
    }

    public String getBranchDescription() {
        GLCostCenterView glv = (GLCostCenterView) DTOPool.getInstance().getDTORO(GLCostCenterView.class, branch);

        return glv == null ? null : glv.getStDescription();
    }

}
