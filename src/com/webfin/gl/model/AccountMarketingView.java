/***********************************************************************
 * Module:  com.webfin.gl.model.AccountMarketingView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 11:37:01 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import java.math.BigDecimal;
import java.util.Date;

public class AccountMarketingView extends DTO implements RecordAudit {

    private String stTransactionID;
    private String stMonths;
    private String stYears;
    private String stSubType;
    private String stNoBukti;
    private String stAccountID;
    private String stAccountNo;
    private String stDescription;
    private String stNama;
    private String stAlamat;
    private String stJenisMarketing;
    private String stNamaPenerima;
    private String stNamaPerusahaan;
    private String stJenisUsaha;
    private String stNPWP;
    private String stNobukPotong;
    private BigDecimal dbPphPotong;
    private BigDecimal dbAmount;
    private Date dtApplyDate;
    public static String tableName = "gl_acc_marketing";
    public static String comboFields[] = {"trx_id", "accountid", "accountno", "nobuk", "descno"};
    public static String fieldMap[][] = {
        {"stTransactionID", "trx_id*pk"},
        {"stMonths", "months"},
        {"stYears", "years"},
        {"stSubType", "subtype"},
        {"stNoBukti", "nobuk"},
        {"stAccountID", "accountid"},
        {"stAccountNo", "accountno"},
        {"stDescription", "descno"},
        {"stNama", "nama"},
        {"stAlamat", "alamat"},
        {"stJenisMarketing", "jenis_marketing"},
        {"stNamaPenerima", "nama_penerima"},
        {"stNamaPerusahaan", "nama_perusahaan"},
        {"stJenisUsaha", "jenis_usaha"},
        {"stNPWP", "npwp"},
        {"stNobukPotong", "nobuk_potong"},
        {"dbPphPotong", "pph_potong"},
        {"dbAmount", "amount"},
        {"dtApplyDate", "applydate"},};

    public String getStDescriptionLong() {

        return getStAccountNo() + "/" + getStDescription();
    }

    public String getStAccountNo() {
        return stAccountNo;
    }

    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public void checkAccountNo() throws Exception {
        final DTOList account = getHistoryAccount();

        for (int i = 0; i < account.size(); i++) {
            AccountView2 acc = (AccountView2) account.get(i);

            if (acc.getStAccountNo().equalsIgnoreCase(getStAccountNo())) {
                throw new RuntimeException("No. Akun " + getStAccountNo() + " Sudah Ada");
            }
        }
    }
    private DTOList historyaccountno;

    public DTOList getHistoryAccount() {
        loadHistoryAccount();
        return historyaccountno;
    }

    public void loadHistoryAccount() {
        try {
            if (historyaccountno == null) {
                historyaccountno = ListUtil.getDTOListFromQuery(
                        "select accountno "
                        + " from gl_accounts "
                        + " where accountno = ? limit 5 ",
                        new Object[]{stAccountNo},
                        AccountView2.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stNoBukti
     */
    public String getStNoBukti() {
        return stNoBukti;
    }

    /**
     * @param stNoBukti the stNoBukti to set
     */
    public void setStNoBukti(String stNoBukti) {
        this.stNoBukti = stNoBukti;
    }

    /**
     * @return the stMonths
     */
    public String getStMonths() {
        return stMonths;
    }

    /**
     * @param stMonths the stMonths to set
     */
    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    /**
     * @return the stYears
     */
    public String getStYears() {
        return stYears;
    }

    /**
     * @param stYears the stYears to set
     */
    public void setStYears(String stYears) {
        this.stYears = stYears;
    }

    private Long lgAccountID;

    public Long getLgAccountID() {
        return lgAccountID;
    }

    public void setLgAccountID(Long lgAccountID) {
        this.lgAccountID = lgAccountID;
    }

    /**
     * @return the stTransactionID
     */
    public String getStTransactionID() {
        return stTransactionID;
    }

    /**
     * @param stTransactionID the stTransactionID to set
     */
    public void setStTransactionID(String stTransactionID) {
        this.stTransactionID = stTransactionID;
    }

    /**
     * @return the stAccountID
     */
    public String getStAccountID() {
        return stAccountID;
    }

    /**
     * @param stAccountID the stAccountID to set
     */
    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    /**
     * @return the dbAmount
     */
    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    /**
     * @param dbAmount the dbAmount to set
     */
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    /**
     * @return the stSubType
     */
    public String getStSubType() {
        return stSubType;
    }

    /**
     * @param stSubType the stSubType to set
     */
    public void setStSubType(String stSubType) {
        this.stSubType = stSubType;
    }

    /**
     * @return the stNama
     */
    public String getStNama() {
        return stNama;
    }

    /**
     * @param stNama the stNama to set
     */
    public void setStNama(String stNama) {
        this.stNama = stNama;
    }

    /**
     * @return the stAlamat
     */
    public String getStAlamat() {
        return stAlamat;
    }

    /**
     * @param stAlamat the stAlamat to set
     */
    public void setStAlamat(String stAlamat) {
        this.stAlamat = stAlamat;
    }

    /**
     * @return the stJenisMarketing
     */
    public String getStJenisMarketing() {
        return stJenisMarketing;
    }

    /**
     * @param stJenisMarketing the stJenisMarketing to set
     */
    public void setStJenisMarketing(String stJenisMarketing) {
        this.stJenisMarketing = stJenisMarketing;
    }

    /**
     * @return the stNamaPenerima
     */
    public String getStNamaPenerima() {
        return stNamaPenerima;
    }

    /**
     * @param stNamaPenerima the stNamaPenerima to set
     */
    public void setStNamaPenerima(String stNamaPenerima) {
        this.stNamaPenerima = stNamaPenerima;
    }

    /**
     * @return the stNamaPerusahaan
     */
    public String getStNamaPerusahaan() {
        return stNamaPerusahaan;
    }

    /**
     * @param stNamaPerusahaan the stNamaPerusahaan to set
     */
    public void setStNamaPerusahaan(String stNamaPerusahaan) {
        this.stNamaPerusahaan = stNamaPerusahaan;
    }

    /**
     * @return the stJenisUsaha
     */
    public String getStJenisUsaha() {
        return stJenisUsaha;
    }

    /**
     * @param stJenisUsaha the stJenisUsaha to set
     */
    public void setStJenisUsaha(String stJenisUsaha) {
        this.stJenisUsaha = stJenisUsaha;
    }

    /**
     * @return the stNPWP
     */
    public String getStNPWP() {
        return stNPWP;
    }

    /**
     * @param stNPWP the stNPWP to set
     */
    public void setStNPWP(String stNPWP) {
        this.stNPWP = stNPWP;
    }

    /**
     * @return the stNobukPotong
     */
    public String getStNobukPotong() {
        return stNobukPotong;
    }

    /**
     * @param stNobukPotong the stNobukPotong to set
     */
    public void setStNobukPotong(String stNobukPotong) {
        this.stNobukPotong = stNobukPotong;
    }

    /**
     * @return the dbPphPotong
     */
    public BigDecimal getDbPphPotong() {
        return dbPphPotong;
    }

    /**
     * @param dbPphPotong the dbPphPotong to set
     */
    public void setDbPphPotong(BigDecimal dbPphPotong) {
        this.dbPphPotong = dbPphPotong;
    }

    /**
     * @return the dtApplyDate
     */
    public Date getDtApplyDate() {
        return dtApplyDate;
    }

    /**
     * @param dtApplyDate the dtApplyDate to set
     */
    public void setDtApplyDate(Date dtApplyDate) {
        this.dtApplyDate = dtApplyDate;
    }
}
