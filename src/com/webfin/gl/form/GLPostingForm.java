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

public class GLPostingForm extends Form {
    
    private GLPostingView posting;
    private boolean noValidation = false;
    private boolean reOpenMode = false;
    private boolean finalMode = false;

    private AccountView2 account;
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        setPosting(new GLPostingView());
        
        getPosting().markNew();

        getPosting().setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        
        setTitle("POSTING JURNAL");
        
    }
    
    public void edit() {
        final String glpostingid = (String)getAttribute("glpostingid");
        
        posting = (GLPostingView) DTOPool.getInstance().getDTO(GLPostingView.class, glpostingid);

        if(Tools.isYes(posting.getStPostedFlag()))
            throw new RuntimeException("Data sudah di posting");

        noValidation = true;

        posting.markUpdate();
        
        setTitle("UBAH POSTING");
    }
    
    public void view() {
        final String glpostingid = (String)getAttribute("glpostingid");
        
        posting = (GLPostingView) DTOPool.getInstance().getDTO(GLPostingView.class, glpostingid);
        
        setReadOnly(true);
        
        setTitle("LIHAT POSTING");
    }
    
    public void save() throws Exception {

        String errorMsg = "Data bulan dan tahun tsb sudah pernah di posting.<br> Cek jika status belum effective maka ubah, centang posting flag, lalu simpan untuk posting ulang. <br>Jika status sudah effective maka hub. Akuntansi Kantor Pusat untuk membukakan posting";

        if (!noValidation) {
            if (isPosted()) {
                throw new RuntimeException(errorMsg);
            }
        }

        getRemoteGeneralLedger().savePostingGL(posting, reOpenMode);

        if (Tools.isYes(posting.getStPostedFlag())) {
            if (posting.getStCostCenterCode() != null) {
                calculateLabaBersih(posting.getStCostCenterCode());
            } else {
                clickNeraca();
            }
        }

        close();
    }

    public void saveFinalisasi() throws Exception {

        String errorMsg = "Data bulan dan tahun tsb sudah pernah di posting.<br> Cek jika status belum effective maka ubah, centang posting flag, lalu simpan untuk posting ulang. <br>Jika status sudah effective maka hub. Akuntansi Kantor Pusat untuk membukakan posting";

        if(!noValidation)
            if(isPosted())
                throw new RuntimeException(errorMsg);

        getRemoteGeneralLedger().savePostingGLFinalisasi(posting, reOpenMode);

        close();
    }
    
    public void close() {
        super.close();
    }


    public GLPostingView getPosting() {
        return posting;
    }

    public void setPosting(GLPostingView posting) {
        this.posting = posting;
    }

    public boolean isPosted() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {

                //jika posting cabang, cek status posting cabang & nasional
                if(posting.getStCostCenterCode()!=null){

                      String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null"+
                            " union "+
                            " select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                    if(posting.getStCostCenterCode()!=null)
                        cek = cek + " and cc_code = ?";

                    PreparedStatement PS = S.setQuery(cek);
                    PS.setString(1, posting.getStMonths());
                    PS.setString(2, posting.getStYears());

                    PS.setString(3, posting.getStMonths());
                    PS.setString(4, posting.getStYears());

                    if(posting.getStCostCenterCode()!=null)
                          PS.setString(5, posting.getStCostCenterCode());

                    ResultSet RS = PS.executeQuery();

                    if (RS.next()){
                        isPosted = true;
                    }
                }

                //jika posting nasional, cek status nasional saja
                if(posting.getStCostCenterCode()==null){
                    String cek = "select gl_post_id from gl_posting where months = ? and years = ? ";

                            cek = cek + " and cc_code is null";

                    PreparedStatement PS = S.setQuery(cek);
                    PS.setString(1, posting.getStMonths());
                    PS.setString(2, posting.getStYears());

                    ResultSet RS = PS.executeQuery();

                    if (RS.next()){
                        isPosted = true;
                    }
                }

            } finally {
                S.release();
            }


        return isPosted;
    }

    public void openPosting() throws Exception{

        final String glpostingid = (String)getAttribute("glpostingid");

        posting = (GLPostingView) DTOPool.getInstance().getDTO(GLPostingView.class, glpostingid);

        if(posting.getStCostCenterCode()!=null)
            if(isPostedNasional())
                throw new RuntimeException("Posting Nasional sudah dilakukan, tidak bisa dilakukan Open Posting");

        posting.setStPostedFlag("N");
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

            calculateLabaBersih(cab.getStCostCenterCode());

        }

        updateLabaBersih00();
    }

    public void calculateLabaBersih(String cccode) throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());

        GLReportEngine2 glr = new GLReportEngine2();

        //glr.setBranch(posting.getStCostCenterCode());
        glr.setBranch(cccode);

        BigDecimal premi_bruto = glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_reas = glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_kenaikan = glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal klaim_reas = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal kenaikan_klaim = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_und_lain = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_usaha = glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

//        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
//        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
//        BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);
//
//        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
//        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
//        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
//        BigDecimal penghasilanBeban = BDUtil.add(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));
//
//        BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);

        BigDecimal penghasilanBeban69 = glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban89 = glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));
        BigDecimal acc_laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        acc_laba_bersih = BDUtil.add(acc_laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan));

        updateLabaBersih(BDUtil.negate(acc_laba_bersih), lPeriodTo, lYearFrom, cccode);
    }

    public BigDecimal updateLabaBersih(BigDecimal labaBersih, long months, long years, String cccode) throws Exception {
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

        HashDTO h = (HashDTO) l.get(0);

        getRemoteGeneralLedger().AccountLabaBersih(h.getFieldValueByFieldNameST("account_id"), months, years, labaBersih, "Y");

        return labaBersih;
    }

    public boolean isPostedNasional() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? ";

                cek = cek + " and cc_code is null and posted_flag = 'Y'";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, posting.getStMonths());
                PS.setString(2, posting.getStYears());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }


        return isPosted;
    }

    public void finalNeraca() throws Exception{

        final String glpostingid = (String)getAttribute("glpostingid");

        posting = (GLPostingView) DTOPool.getInstance().getDTO(GLPostingView.class, glpostingid);

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

    public void updateLabaBersih00() throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());

        BigDecimal labaBersih00 = calculateLabaBersih00();
        BigDecimal selisih_nilai = calculateSelisihLabaBersih();

//        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ : " + labaBersih00);
//        logger.logDebug("##################### : " + selisih_nilai);

        labaBersih00 = BDUtil.add(labaBersih00, selisih_nilai);

//        logger.logDebug("$$$$$$$$$$$$$$$$$$$$$ : " + labaBersih00);

        getRemoteGeneralLedger().AccountLabaBersih("223379", lPeriodTo, lYearFrom, labaBersih00, "Y");

    }

    public BigDecimal calculateLabaBersih00() throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select bal" + lPeriodTo
                    + "   from gl_acct_bal2 a "
                    + "   where a.account_id = 223379 and a.idr_flag = 'Y' and a.period_year = ?");

            S.setParam(1, lYearFrom);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal calculateSelisihLabaBersih() throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());

        GLReportEngine2 glr = new GLReportEngine2();

        BigDecimal premi_bruto = glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_reas = glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_kenaikan = glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal klaim_reas = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal kenaikan_klaim = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_und_lain = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_usaha = glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);

        BigDecimal penghasilanBeban69 = glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban89 = glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));
        BigDecimal acc_laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        acc_laba_bersih = BDUtil.add(acc_laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan));

        BigDecimal laba_bersih = glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal selisih_nilai = BDUtil.sub(BDUtil.negate(laba_bersih), acc_laba_bersih);

        return selisih_nilai;
    }

}
