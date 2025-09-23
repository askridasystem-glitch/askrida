/***********************************************************************
 * Module:  com.webfin.ar.forms.BungaList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.*;
import java.math.BigDecimal;
import java.util.Date;
import jxl.write.DateTime;

public class BungaList extends Form {

    private ARInvestmentBungaView bunga;
    private DTOList list;
    private String arbungaid;
    private String stNodefo;
    private String stNoBukti;
    private String stActiveFlag = "Y";
    private String stEffectiveFlag = "Y";
    private BigDecimal dbNominal;
    private String stCurrencyDesc;
    private Date dtBungaDateFrom;
    private Date dtBungaDateTo;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("BUNGA_NAVBR");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("BUNGA_APRV");
    private final static transient LogManager logger = LogManager.getInstance(BungaList.class);

    public DTOList getList() throws Exception {

        if (list == null) {
            list = new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ar_bunga_id desc";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery(
                "from ar_inv_bunga");

//        sqa.addClause("ar_izinbng_id is null");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("active_flag='Y'");
            sqa.addClause("delete_flag is null");
        }

        if (Tools.isNo(stActiveFlag)) {
            sqa.addClause("delete_flag is null");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("(effective_flag='N' or effective_flag is null)");
        }

        if (dtBungaDateFrom != null) {
            sqa.addClause("date_trunc('day',tglbunga) >= ?");
            sqa.addPar(dtBungaDateFrom);
        }

        if (dtBungaDateTo != null) {
            sqa.addClause("date_trunc('day',tglbunga) <= ?");
            sqa.addPar(dtBungaDateTo);
        }

        if (stNodefo != null) {
            sqa.addClause("nodefo = ?");
            sqa.addPar(stNodefo);
        }

        if (stNoBukti != null) {
            sqa.addClause("nobuk = ?");
            sqa.addPar(stNoBukti);
        }

        if (stBranch != null) {
            sqa.addClause("koda = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("ar_bunga_id desc");

        sqa.setLimit(100);

        list = sqa.getList(ARInvestmentBungaView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreate() throws Exception {
        validateBungaID();

        if (getBunga().getStNoBuktiD() != null) {
            throw new RuntimeException("Bunga Tidak Bisa Dibuat, No. Bukti Bunga Sudah Ada");
        }

        BungaForm form = (BungaForm) super.newForm("bunga_form", this);

        form.edit(arbungaid);

        form.show();
    }

    public void clickEdit() throws Exception {
        validateBungaID();

        if (getBunga().getStNoBuktiD() == null) {
            throw new RuntimeException("No. Bukti Bunga Belum Ada");
        }

        BungaForm form = (BungaForm) super.newForm("bunga_form", this);

        form.edit(arbungaid);

        form.show();
    }

    public void clickView() throws Exception {
        validateBungaID();

        BungaForm form = (BungaForm) super.newForm("bunga_form", this);

        form.view(arbungaid);

        form.show();

    }

    public void clickApproval() throws Exception {
        validateBungaID();

        if (getBunga().getStNoBuktiD() == null) {
            throw new RuntimeException("No. Bukti Bunga Belum Ada");
        }

        if (getBunga().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        BungaForm form = (BungaForm) super.newForm("bunga_form", this);

        form.approval(arbungaid);

        form.setApprovalMode(true);

        form.show();
    }

    public void list() {
    }

    public void refresh() {
    }

    public String getStNodefo() {
        return stNodefo;
    }

    public void setStNodefo(String stNodefo) {
        this.stNodefo = stNodefo;
    }

    public BigDecimal getDbNominal() {
        return dbNominal;
    }

    public void setDbNominal(BigDecimal dbNominal) {
        this.dbNominal = dbNominal;
    }

    public String getStCurrencyDesc() {
        return stCurrencyDesc;
    }

    public void setStCurrencyDesc(String stCurrencyDesc) {
        this.stCurrencyDesc = stCurrencyDesc;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public String getArbungaid() {
        return arbungaid;
    }

    public void setArbungaid(String arbungaid) {
        this.arbungaid = arbungaid;
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    private void validateBungaID() {
        if (arbungaid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    public Date getDtBungaDateFrom() {
        return dtBungaDateFrom;
    }

    public void setDtBungaDateFrom(Date dtBungaDateFrom) {
        this.dtBungaDateFrom = dtBungaDateFrom;
    }

    public Date getDtBungaDateTo() {
        return dtBungaDateTo;
    }

    public void setDtBungaDateTo(Date dtBungaDateTo) {
        this.dtBungaDateTo = dtBungaDateTo;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    private ARInvestmentBungaView getBunga() {
        bunga = (ARInvestmentBungaView) DTOPool.getInstance().getDTO(ARInvestmentBungaView.class, arbungaid);
        return bunga;
    }

    public String getStNoBukti() {
        return stNoBukti;
    }

    public void setStNoBukti(String stNoBukti) {
        this.stNoBukti = stNoBukti;
    }

    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public void clickReverse() throws Exception {

        validateBungaID();

        if (!getBunga().isEffective()) {
            throw new Exception("Data Belum Disetujui");
        }

        final BungaForm form = (BungaForm) newForm("bunga_form", this);

        form.view(arbungaid);

        form.setReverseMode(true);

        form.show();
    }
}
