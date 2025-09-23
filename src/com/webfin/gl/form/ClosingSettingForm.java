/***********************************************************************
 * Module:  com.webfin.gl.currencys.forms.CurrencyForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.common.model.HashDTO;
import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.ejb.*;
import com.webfin.gl.model.*;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ClosingSettingForm extends Form {
    
    private ClosingHeaderView posting;
    private boolean noValidation = false;
    private boolean reOpenMode = false;
    private boolean finalMode = false;
    private String notesindex;

    private AccountView2 account;
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        setPosting(new ClosingHeaderView());
        
        getPosting().markNew();

        getPosting().setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        final DTOList details = new DTOList();

        posting.setDetails(details);

        //doNewDetail();

        setTitle("CLOSING SETTING");
        
    }

    public void doNewDetail() {
        Date startDate = DateUtil.getDate("01/"+ getPosting().getStPeriodNum() +"/"+ getPosting().getStFiscalYear());
        DateTime dateMulai = new DateTime(startDate);

        DateTime endOfMonth = dateMulai.dayOfMonth().withMaximumValue().plusDays(1);

        for (DateTime date = dateMulai; date.isBefore(endOfMonth); date = date.plusDays(1))
        {
            final ClosingDetailView detil = new ClosingDetailView();

            detil.markNew();

            detil.setDtStartDate(date.withTime(0, 0, 0, 0).toDate());
            detil.setDtEndDate(date.withTime(23, 59, 59, 0).toDate());

            detil.setDtEditStartDate(date.withTime(0, 0, 0, 0).toDate());
            detil.setDtEditEndDate(date.withTime(17, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStEditEndTime("17:00");
            if(getPosting().isWITA()) detil.setStEditEndTime("16:00");
            if(getPosting().isWIT()) detil.setStEditEndTime("15:00");

            detil.setDtReverseStartDate(date.withTime(0, 0, 0, 0).toDate());
            detil.setDtReverseEndDate(date.withTime(20, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStReverseEndTime("20:00");
            if(getPosting().isWITA()) detil.setStReverseEndTime("19:00");
            if(getPosting().isWIT()) detil.setStReverseEndTime("18:00");

            detil.setDtClosedDate(date.withTime(0, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStCloseTime("23:59");
            if(getPosting().isWITA()) detil.setStCloseTime("22:59");
            if(getPosting().isWIT()) detil.setStCloseTime("21:59");

            getPosting().getDetails().add(detil);
        }

    }

    public void doNewDetailNew() {
//        Date startDate = DateUtil.getDate("01/"+ getPosting().getStPeriodNum() +"/"+ getPosting().getStFiscalYear());
//        DateTime dateMulai = new DateTime(startDate);
//
//        DateTime endOfMonth = dateMulai.dayOfMonth().withMaximumValue().plusDays(1);
//
//        for (DateTime date = dateMulai; date.isBefore(endOfMonth); date = date.plusDays(1))
//        {
            final ClosingDetailView detil = new ClosingDetailView();

            detil.markNew();

//            detil.setDtStartDate(date.withTime(0, 0, 0, 0).toDate());
//            detil.setDtEndDate(date.withTime(23, 59, 59, 0).toDate());
//
//            detil.setDtEditStartDate(date.withTime(0, 0, 0, 0).toDate());
//            detil.setDtEditEndDate(date.withTime(17, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStEditEndTime("17:00");
            if(getPosting().isWITA()) detil.setStEditEndTime("16:00");
            if(getPosting().isWIT()) detil.setStEditEndTime("15:00");

//            detil.setDtReverseStartDate(date.withTime(0, 0, 0, 0).toDate());
//            detil.setDtReverseEndDate(date.withTime(20, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStReverseEndTime("20:00");
            if(getPosting().isWITA()) detil.setStReverseEndTime("19:00"); 
            if(getPosting().isWIT()) detil.setStReverseEndTime("18:00");

//            detil.setDtClosedDate(date.withTime(0, 0, 0, 0).toDate());
            if(getPosting().isWIB()) detil.setStCloseTime("23:59");
            if(getPosting().isWITA()) detil.setStCloseTime("22:59");
            if(getPosting().isWIT()) detil.setStCloseTime("21:59");

            getPosting().getDetails().add(detil);
        //}

    }
    
    public void edit() {
        final String glpostingid = (String)getAttribute("glpostingid");
        
        posting = (ClosingHeaderView) DTOPool.getInstance().getDTO(ClosingHeaderView.class, glpostingid);

        posting.getDetails().markAllUpdate();

        posting.markUpdate();
        
        setTitle("UBAH CLOSING");
    }
    
    public void view() {
        final String glpostingid = (String)getAttribute("glpostingid");
        
        posting = (ClosingHeaderView) DTOPool.getInstance().getDTO(ClosingHeaderView.class, glpostingid);
        
        setReadOnly(true);
        
        setTitle("LIHAT POSTING");
    }
    
    public void save() throws Exception {

        formatDate();

        getRemoteGeneralLedger().saveClosingSetting(posting);

        close();
    }

    public void saveFinalisasi() throws Exception {

        String errorMsg = "Data bulan dan tahun tsb sudah pernah di posting.<br> Cek jika status belum effective maka ubah, centang posting flag, lalu simpan untuk posting ulang. <br>Jika status sudah effective maka hub. Akuntansi Kantor Pusat untuk membukakan posting";

        if(!noValidation)
            if(isPosted())
                throw new RuntimeException(errorMsg);

        getRemoteGeneralLedger().saveClosingSetting(posting);

        close();
    }
    
    public void close() {
        super.close();
    }


    public ClosingHeaderView getPosting() {
        return posting;
    }

    public void setPosting(ClosingHeaderView posting) {
        this.posting = posting;
    }

    public boolean isPosted() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? ";

                if(posting.getStCostCenterCode()!=null)
                    cek = cek + " and cc_code = ?";

                if(posting.getStCostCenterCode()==null)
                    cek = cek + " and cc_code is null";

                PreparedStatement PS = S.setQuery(cek);
//                PS.setString(1, posting.getStMonths());
//                PS.setString(2, posting.getStYears());

                if(posting.getStCostCenterCode()!=null)
                      PS.setString(3, posting.getStCostCenterCode());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }


        return isPosted;
    }

    public void openPosting() throws Exception{

        final String glpostingid = (String)getAttribute("glpostingid");

        posting = (ClosingHeaderView) DTOPool.getInstance().getDTO(ClosingHeaderView.class, glpostingid);

//        if(posting.getStCostCenterCode()!=null)
//            if(isPostedNasional())
//                throw new RuntimeException("Posting Nasional sudah dilakukan, tidak bisa dilakukan Open Posting");

        //posting.setStPostedFlag("N");
        posting.markUpdate();

        noValidation = true;
        reOpenMode = true;

        setTitle("BUKA POSTING");
    }

    public void clickNeraca() throws Exception {

        account = new AccountView2();

        final DTOList cabang = account.getCabang();

        for (int i = 0; i < cabang.size(); i++) {
            GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

            //calculateLabaBersih(cab.getStCostCenterCode());

        }
    }

   

    public BigDecimal updateLabaBersih(BigDecimal labaBersih, long months, long years, String flag,String cccode) throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.account_id::text ");

        sqa.addQuery(" from gl_accounts a ");

        sqa.addClause(" a.accountno like '51611%'  ");
        sqa.addClause(" a.acctype is null ");
        sqa.addClause(" a.enabled = 'Y' ");

        if (cccode != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(cccode);
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class);

        if (flag.equalsIgnoreCase("N")) {
            flag = null;
        }

        HashDTO h = (HashDTO) l.get(0);

        getRemoteGeneralLedger().AccountLabaBersih(h.getFieldValueByFieldNameST("account_id"), months, years, labaBersih, flag);

        return labaBersih;
    }

    

    public void finalNeraca() throws Exception{

        final String glpostingid = (String)getAttribute("glpostingid");

        posting = (ClosingHeaderView) DTOPool.getInstance().getDTO(ClosingHeaderView.class, glpostingid);

//        if(posting.getStCostCenterCode()!=null)
//            if(isPostedNasional())
//                throw new RuntimeException("Posting Nasional sudah dilakukan, tidak bisa dilakukan Open Posting");

        posting.markUpdate();

        noValidation = true;

        finalMode = true;

        setTitle("FINALISASI NERACA");
    }

    /**
     * @return the finalMode
     */
    public boolean isFinalMode() {
        return finalMode;
    }

    /**
     * @param finalMode the finalMode to set
     */
    public void setFinalMode(boolean finalMode) {
        this.finalMode = finalMode;
    }

    /**
     * @return the notesindex
     */
    public String getNotesindex() {
        return notesindex;
    }

    /**
     * @param notesindex the notesindex to set
     */
    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public DTOList getDetails() throws Exception {
        return posting.getDetails();
    }

    public void formatDate() throws Exception{
        SimpleDateFormat cdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        /*
        final DTOList details = getDetails();
        for (int i = 0; i < details.size(); i++) {
            ClosingDetailView det = (ClosingDetailView) details.get(i);

            det.setDtEditEndDate(cdf.parse((DateUtil.getDateStr(det.getDtEditEndDate()) + " " + det.getStEditEndTime()).trim()));

            det.setDtReverseEndDate(cdf.parse((DateUtil.getDateStr(det.getDtReverseEndDate()) + " " + det.getStReverseEndTime()).trim()));

            det.setDtClosedDate(cdf.parse((DateUtil.getDateStr(det.getDtClosedDate()) + " " + det.getStCloseTime()).trim()));

        }*/

    }

    public void changeBranch(){
        doNewDetailNew();
    }


}
