/***********************************************************************
 * Module:  com.webfin.gl.model.BungaDepositoView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 6:29:56 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.ObjectCloner;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.webfin.ar.model.ARInvestmentBungaView;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class BungaDepositoView extends DTO implements RecordAudit {
    
    private final static transient LogManager logger = LogManager.getInstance(BungaDepositoView.class);
    
    public static String tableName = "ar_bunga_deposito";
    
    public transient static String comboFields[] = {"trx_hdr_id","trx_hdr_no","hdr_accountid","hdr_accountno","hdr_accountmaster"};
    
    public static String fieldMap[][] = {
        {"stTransactionHeaderID","trx_hdr_id"},
        {"stTransactionHeaderNo","trx_hdr_no"},
        {"stTransactionID","trx_id*pk*nd"},
        {"stTransactionNo","trx_no"},
        {"stAccountID","accountid"},
        {"stAccountNo","accountno*n"},
        {"stDescription","description"},
        {"dtApplyDate","applydate"},
        {"lgFiscalYear","fiscal_year"},
        {"lgPeriodNo","period_no"},
        {"stCurrencyCode","ccy_code"},
        {"dbCurrencyRate","ccy_rate"},
        {"stRefTrxType","ref_trx_type"},
        {"stMethodCode","method_code*n"},
        {"stGlAccountID","gl_acct_id*n"},
        {"stApproved","approved"},
        {"stHeaderAccountID","hdr_accountid"},
        {"stHeaderAccountNo","hdr_accountno"},
        {"stHeaderAccountMaster","hdr_accountmaster"},
        {"stActiveFlag","active_flag"},
        {"stCostCenter","cc_code"},
        {"dbAmount","amount"},
        {"stYears","years"},
        {"stMonths","months"},
        {"stUserName","user_name*n"},
        {"stFilePhysic","file_physic"},
        {"stARBungaID","ar_bunga_id"},
        
    };
    
    private String stRefTrxType;
    private String stCurrencyCode;
    private BigDecimal dbCurrencyRate;
    private String stARBungaID;
    
    private String stTransactionHeaderID;
    private String stTransactionHeaderNo;
    private String stTransactionID;
    private String stTransactionNo;
    
    private String stAccountID;
    private String stAccountNo;
    private String stDescription;
    private Date dtApplyDate;
    private Long lgFiscalYear;
    private Long lgPeriodNo;
    private String stCostCenter;
    private String stMethodCode;
    private String stGlAccountID;
    
    private Date dtCreateDate;
    private boolean readOnly;
    
    private String stApproved;
    private String stHeaderAccountID;
    private String stHeaderAccountNo;
    private String stHeaderAccountMaster;
    private String stActiveFlag;
    private BigDecimal dbAmount;
    private BigDecimal dbJumlah;
    
    private String stMonths;
    private String stYears;
    private String stUserName;
    private String stFilePhysic;
    private boolean Posted;
    
    public String getStApproved() {
        return stApproved;
    }
    
    public void setStApproved(String stApproved) {
        this.stApproved = stApproved;
    }
    
    public boolean getReadOnly(){
        return readOnly;
    }
    
    public void setReadOnly(boolean read){
        this.readOnly = read;
    }
    
    public String getStMethodCode() {
        return stMethodCode;
    }
    
    public void setStMethodCode(String stMethodCode) {
        this.stMethodCode = stMethodCode;
    }
    
    public String getStCostCenter() {
        return stCostCenter;
    }
    
    public void setStCostCenter(String stCostCenter) {
        this.stCostCenter = stCostCenter;
    }
    
    public String getStRefTrxType() {
        return stRefTrxType;
    }
    
    public void setStRefTrxType(String stRefTrxType) {
        this.stRefTrxType = stRefTrxType;
    }
    
    public String getStCurrencyCode() {
        return stCurrencyCode;
    }
    
    public void setStCurrencyCode(String stCurrencyCode) {
        this.stCurrencyCode = stCurrencyCode;
    }
    
    public BigDecimal getDbCurrencyRate() {
        return dbCurrencyRate;
    }
    
    public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
        this.dbCurrencyRate = dbCurrencyRate;
    }
    
    public String getStTransactionNo() {
        return stTransactionNo;
    }
    
    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }
    
    public String getStAccountID() {
        return stAccountID;
    }
    
    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }
    
    public String getStDescription() {
        return stDescription;
    }
    
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }
    
    public Date getDtApplyDate() {
        return dtApplyDate;
    }
    
    public void setDtApplyDate(Date dtApplyDate) {
        this.dtApplyDate = dtApplyDate;
    }
    
    public Long getLgFiscalYear() {
        return lgFiscalYear;
    }
    
    public void setLgFiscalYear(Long lgFiscalYear) {
        this.lgFiscalYear = lgFiscalYear;
    }
    
    public Long getLgPeriodNo() {
        return lgPeriodNo;
    }
    
    public void setLgPeriodNo(Long lgPeriodNo) {
        this.lgPeriodNo = lgPeriodNo;
    }
    
    public String getStAccountNo() {
        return stAccountNo;
    }
    
    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }
    
    public TitipanPremiView copy() {
        return (TitipanPremiView) ObjectCloner.deepCopy(this);
    }
    
    public String getStTransactionHeaderID() {
        return stTransactionHeaderID;
    }
    
    public void setStTransactionHeaderID(String stTransactionHeaderID) {
        this.stTransactionHeaderID = stTransactionHeaderID;
    }
    
    public String getStTransactionID() {
        return stTransactionID;
    }
    
    public void setStTransactionID(String stTransactionID) {
        this.stTransactionID = stTransactionID;
    }
    
    public void loadAccountNo() {
        AccountView2 acc = getAccount();
        
        if (acc!=null)
            setStAccountNo(acc.getStAccountNo());
    }
    
    public AccountView2 getAccount() {
        if (stAccountID==null) return null;
        return (AccountView2) DTOPool.getInstance().getDTORO(AccountView2.class, stAccountID);
    }
    
    public ARInvestmentBungaView getBunga() {
        if (stARBungaID==null) return null;
        return (ARInvestmentBungaView) DTOPool.getInstance().getDTORO(ARInvestmentBungaView.class, stARBungaID);
    }
    
    public void setStAccountIDNotNull(String stAccountID, String errMsg) {
        if (stAccountID==null) throw new RuntimeException(errMsg+" null account ID");
        setStAccountID(stAccountID);
        loadAccountNo();
        if (stAccountNo==null) throw new RuntimeException(errMsg+" null account NO");
    }
    
    public Date getDtCreateDate() {
        return dtCreateDate;
    }
    
    public void setDtCreateDate(Date dtCreateDate) {
        this.dtCreateDate = dtCreateDate;
    }
    
    public String getStHeaderAccountID() {
        return stHeaderAccountID;
    }
    
    public void setStHeaderAccountID(String stHeaderAccountID) {
        this.stHeaderAccountID = stHeaderAccountID;
    }
    
    public String getStHeaderAccountNo() {
        return stHeaderAccountNo;
    }
    
    public void setStHeaderAccountNo(String stHeaderAccountNo) {
        this.stHeaderAccountNo = stHeaderAccountNo;
    }
    
    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenter);
        
        return costcenter;
    }
    
    public boolean isApproved() {
        return Tools.isYes(stApproved);
    }
    
    public String getStActiveFlag() {
        return stActiveFlag;
    }
    
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }
    
    public BigDecimal getDbAmount() {
        return dbAmount;
    }
    
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }
    
    public String getStMonths() {
        return stMonths;
    }
    
    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }
    
    public String getStYears() {
        return stYears;
    }
    
    public void setStYears(String stYears) {
        this.stYears = stYears;
    }
    
    public String getStGlAccountID() {
        return stGlAccountID;
    }
    
    public void setStGlAccountID(String stGlAccountID) {
        this.stGlAccountID = stGlAccountID;
    }
    
    public String getStUserName() {
        return stUserName;
    }
    
    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }
    
    public String getStFilePhysic() {
        return stFilePhysic;
    }
    
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
    
    public String getStHeaderAccountMaster() {
        return stHeaderAccountMaster;
    }
    
    public void setStHeaderAccountMaster(String stHeaderAccountMaster) {
        this.stHeaderAccountMaster = stHeaderAccountMaster;
    }
    
    public void reCalculate() {
        dbAmount = dbAmount;
    }
    
    public String getStTransactionHeaderNo() {
        return stTransactionHeaderNo;
    }
    
    public void setStTransactionHeaderNo(String stTransactionHeaderNo) {
        this.stTransactionHeaderNo = stTransactionHeaderNo;
    }    

    public String getStARBungaID() {
        return stARBungaID;
    }

    public void setStARBungaID(String stARBungaID) {
        this.stARBungaID = stARBungaID;
    }
    
    public boolean isPosted() throws Exception {
        
        SQLUtil S = new SQLUtil();
        
        boolean isPosted = false;
        
        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";
            
            if(getStCostCenter()!=null)
                cek = cek + " and cc_code = ?";
            
            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStMonths());
            PS.setString(2, getStYears());
            
            if(getStCostCenter()!=null)
                PS.setString(3, getStCostCenter());
            
            ResultSet RS = PS.executeQuery();
            
            if (RS.next()){
                isPosted = true;
            }
            
        } finally {
            S.release();
        }
        
        return isPosted;
    }
    
    public void setPosted(boolean Posted) {
        this.Posted = Posted;
    }
}