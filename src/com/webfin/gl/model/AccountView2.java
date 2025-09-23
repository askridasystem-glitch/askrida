/***********************************************************************
 * Module:  com.webfin.gl.model.AccountView2
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 11:37:01 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;

import java.math.BigDecimal;

public class AccountView2 extends DTO implements RecordAudit {
    
   /*
    
   CREATE TABLE gl_accounts
(
  account_id bigint NOT NULL,
  accountno character varying(32) NOT NULL,
  acctype character varying(5),
  allocated_flag character varying(1),
  bal_open numeric,
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  description character varying(255),
  accountno2 character varying(32),
  cc_code character varying(8),
  f_cash_flow character varying(1),
  enabled character varying(1),
  rekno character varying(32),
  noper character varying(32),
  CONSTRAINT accountid_pk PRIMARY KEY (account_id )
)
    */
    
    private String stAccountID;
    private String stAccountNo;
    private String stAccountType;
    private String stAllocatedFlag;
    private BigDecimal dbBalanceOpen;
    private String stDescription;
    private String stAccountNo2;
    private String stCostCenterCode;
    private String stCashFlow;
    private String stEnabled;
    private String stRekeningNo;
    private String stNoper;
    
    private String stEntityID;
    private String stEntityName;

    private String stDeleted;

    private String stAcctLevel;
    
    public static String tableName = "gl_accounts";
    
    public static String comboFields[] = {"account_id","accountno","description","noper","rekno"};
    
    public static String fieldMap[][] = {
        {"stAccountID","account_id*pk"},
        {"stAccountNo","accountno"},
        {"stAccountType","acctype"},
        {"stAllocatedFlag","allocated_flag"},
        {"dbBalanceOpen","bal_open"},
        {"stDescription","description"},
        {"stAccountNo2","accountno2"},
        {"stCostCenterCode","cc_code"},
        {"stCashFlow","f_cash_flow"},
        {"stEnabled","enabled"},
        {"stRekeningNo","rekno"},
        {"stNoper","noper"},
        {"stDeleted","deleted"},
        {"stAcctLevel", "acct_level"},
    };
    
    public String getStDescriptionLong() {
        
        return getStAccountNo() + "/" + getStDescription();
    }

    public String getStAccountID() {
        return stAccountID;
    }

    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    public String getStAccountNo() {
        return stAccountNo;
    }

    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    public String getStAccountType() {
        return stAccountType;
    }

    public void setStAccountType(String stAccountType) {
        this.stAccountType = stAccountType;
    }

    public String getStAllocatedFlag() {
        return stAllocatedFlag;
    }

    public void setStAllocatedFlag(String stAllocatedFlag) {
        this.stAllocatedFlag = stAllocatedFlag;
    }

    public BigDecimal getDbBalanceOpen() {
        return dbBalanceOpen;
    }

    public void setDbBalanceOpen(BigDecimal dbBalanceOpen) {
        this.dbBalanceOpen = dbBalanceOpen;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStAccountNo2() {
        return stAccountNo2;
    }

    public void setStAccountNo2(String stAccountNo2) {
        this.stAccountNo2 = stAccountNo2;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStCashFlow() {
        return stCashFlow;
    }

    public void setStCashFlow(String stCashFlow) {
        this.stCashFlow = stCashFlow;
    }

    public String getStEnabled() {
        return stEnabled;
    }

    public void setStEnabled(String stEnabled) {
        this.stEnabled = stEnabled;
    }

    public String getStRekeningNo() {
        return stRekeningNo;
    }

    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
    }

    public String getStNoper() {
        return stNoper;
    }

    public void setStNoper(String stNoper) {
        this.stNoper = stNoper;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStEntityName() {
        return stEntityName;
    }

    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }
    
    public void checkAccountNo() throws Exception{
        final DTOList account = getHistoryAccount();
        
        for (int i = 0; i < account.size(); i++) {
            AccountView2 acc = (AccountView2) account.get(i);
            
            if(acc.getStAccountNo().equalsIgnoreCase(getStAccountNo()))
                throw new RuntimeException("No. Akun "+ getStAccountNo() + " Sudah Ada");
        }
    }
    
    private DTOList historyaccountno;
    
    public DTOList getHistoryAccount() {
        loadHistoryAccount();
        return historyaccountno;
    }
    
    public void loadHistoryAccount() {
        try {
            if (historyaccountno == null)
                historyaccountno = ListUtil.getDTOListFromQuery(
                        "select accountno "+
                        " from gl_accounts "+
                        " where accountno = ? limit 5 ",
                        new Object[]{stAccountNo},
                        AccountView2.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStDeleted() {
        return stDeleted;
    }

    public void setStDeleted(String stDeleted) {
        this.stDeleted = stDeleted;
    }

    public DTOList getCabang() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from gl_cost_center order by cc_code",
                    GLCostCenterView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GLCostCenterView getCostCenter() {
         return (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);
    }

    public DTOList getGLNeraca() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from gl_neraca_total where active_flag = 'Y' "
//                    + "and gl_ins_id in (1195,1196,1197,1198) "
//                    + "and gl_ins_id in (1056) "
                    + "order by gl_ins_id",
                    GLNeracaTotalView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return the stAcctLevel
     */
    public String getStAcctLevel() {
        return stAcctLevel;
    }

    /**
     * @param stAcctLevel the stAcctLevel to set
     */
    public void setStAcctLevel(String stAcctLevel) {
        this.stAcctLevel = stAcctLevel;
    }
    
}