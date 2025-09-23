 /***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyTreatyDetailView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 2:54:13 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.parameter.Parameter;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.BDUtil;
import com.crux.util.DateUtil;
import com.crux.util.LogManager;
import com.crux.util.Tools;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;

public class InsurancePolicyTreatyDetailView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_treaty_detail
(
  ins_pol_tre_det_id int8 NOT NULL,
  ins_pol_treaty_id int8,
  tsi_amount numeric,
  premi_amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_treaty_detail_pk PRIMARY KEY (ins_pol_tre_det_id)
)
WITHOUT OIDS;
    */

    private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyTreatyDetailView.class);

    public static String tableName = "ins_pol_treaty_detail";
    
    public static String fieldMap[][] = {
        {"stInsurancePolicyTreatyDetailID","ins_pol_tre_det_id*pk*nd"},
        {"stInsurancePolicyTreatyID","ins_pol_treaty_id"},
        {"stInsuranceTreatyDetailID","ins_treaty_detail_id"},
        {"dbTSIAmount","tsi_amount"},
        {"dbPremiAmount","premi_amount"},
        {"dbComissionRate","comm_rate"},
        {"dbComission","comm_amt"},
        {"dbTreatyLimit","treaty_limit"},
        {"dbTreatyLimitRatio","treaty_limit_ratio"},
        {"dbPremiRatePct","premi_rate"},
        {"stParentID","parent_id"},
        {"dbTSIPct","tsi_pct"},
        {"dbBaseTSIAmount","base_tsi_amount"},
        {"dbClaimAmount","claim_amount"},
        {"stEditFlag","edit_flag"},
        {"stTreatyType","treaty_type*n"},
        {"dbTSIFactor1","tsi_factor1"},
        {"dbTSIFactor2","tsi_factor2"},
        {"dbTSIFactor3","tsi_factor3"},
        {"dbPremiFactor1","premi_factor1"},
        {"dbPremiFactor2","premi_factor2"},
        {"dbPremiFactor3","premi_factor3"},
        {"stAutoRateFlag","auto_rate_flag*n"},
        {"stUseRateFlag","use_rate_flag*n"},
        {"stApprovedFlag","f_approve*n"},
        {"stManualClaimFlag","manual_claim_f"},
        {"dbTSIPctCalc","tsi_pct_calc"},
        {"dbTSIAmountCalc","tsi_amount_calc"},

    };
    
    public InsurancePolicyTreatyDetailView() {
    }
    
    private String stInsurancePolicyTreatyDetailID;
    private String stInsurancePolicyTreatyID;
    private String stInsuranceTreatyDetailID;
    private String stParentID;
    private BigDecimal dbClaimAmount;
    private BigDecimal dbClaimAmountCalc;
    private BigDecimal dbTSIAmount;
    private BigDecimal dbPremiAmount;
    private BigDecimal dbBaseTSIAmount;
    private BigDecimal dbComissionRate;
    private BigDecimal dbComission;
    private BigDecimal dbTreatyLimit;
    private BigDecimal dbTreatyLimitRatio;
    private BigDecimal dbPremiRatePct;
    private BigDecimal dbTSIPct;
    private InsuranceTreatyDetailView treatyDetail;
    private InsurancePolicyTreatyView polTreaty;
    private DTOList shares;
    private DTOList details;
    private DTOList objects;
    private DTOList coverage;
    private String stEditFlag;
    private String stTreatyType;
    private BigDecimal dbMemberTreatyPremiTotal;
    private InsurancePolicyTreatyView treaty;
    private BigDecimal dbTSIFactor1;
    private BigDecimal dbTSIFactor2;
    private BigDecimal dbTSIFactor3;
    private BigDecimal dbPremiFactor1;
    private BigDecimal dbPremiFactor2;
    private BigDecimal dbPremiFactor3;
    private String stAutoRateFlag;
    private String stUseRateFlag;
    private String stApprovedFlag;
    private String stManualClaimFlag;
    private BigDecimal dbPremiPerilsPerTreaty;
    private BigDecimal dbTSIPctCalc;
    private BigDecimal dbTSIAmountCalc;

    public void setStTreatyType(String stTreatyType) {
        this.stTreatyType = stTreatyType;
    }
    
    public String getStTreatyType() {
        return stTreatyType;
    }
    
    public void setStEditFlag(String stEditFlag) {
        this.stEditFlag = stEditFlag;
    }
    
    public String getStEditFlag() {
        return stEditFlag;
    }
    
    public BigDecimal getDbClaimAmountCalc() {
        return dbClaimAmountCalc;
    }
    
    public void setDbClaimAmountCalc(BigDecimal dbClaimAmountCalc) {
        this.dbClaimAmountCalc = dbClaimAmountCalc;
    }
    
    public BigDecimal getDbTSIPct() {
        return dbTSIPct;
    }
    
    public void setDbTSIPct(BigDecimal dbTSIPct) {
        this.dbTSIPct = dbTSIPct;
    }
    
    public String getStParentID() {
        return stParentID;
    }
    
    public void setStParentID(String stParentID) {
        this.stParentID = stParentID;
    }
    
    public BigDecimal getDbPremiRatePct() {
        return dbPremiRatePct;
    }
    
    public void setDbPremiRatePct(BigDecimal dbPremiRatePct) {
        this.dbPremiRatePct = dbPremiRatePct;
    }
    
    public DTOList getShares() {
        loadShares();
        return shares;
    }
    
    private void loadSharesBackup() {
        try {
            if (shares==null)
                shares = ListUtil.getDTOListFromQuery(
                        " select * from ins_pol_ri where ins_pol_tre_det_id=? order by ins_pol_ri_id",
                        new Object [] {stInsurancePolicyTreatyDetailID},
                        InsurancePolicyReinsView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadShares() {
        try {
            if (shares==null)
                shares = ListUtil.getDTOListFromQuery(
                        " select * from ins_pol_ri where ins_pol_tre_det_id=?",
                        new Object [] {stInsurancePolicyTreatyDetailID},
                        InsurancePolicyReinsView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public DTOList getCoverage() {
        loadCoverage();
        return coverage;
    }
    
    private void loadCoverage() {
        try {
            if (coverage==null)
                coverage = ListUtil.getDTOListFromQuery(
                        " select * from ins_pol_cover_ri where ins_pol_ri_id=? ",
                        new Object [] {stInsurancePolicyTreatyDetailID},
                        InsurancePolicyCoverReinsView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public DTOList getCoverage2() {
        loadCoverage2();
        return coverage;
    }
    
    private void loadCoverage2() {
        try {
            if (coverage==null)
                coverage = ListUtil.getDTOListFromQuery(
                        " select * from ins_pol_cover_ri where ins_pol_tre_det_id=?",
                        new Object [] {stInsurancePolicyTreatyDetailID},
                        InsurancePolicyCoverReinsView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public BigDecimal getDbTreatyLimitRatio() {
        return dbTreatyLimitRatio;
    }
    
    public void setDbTreatyLimitRatio(BigDecimal dbTreatyLimitRatio) {
        this.dbTreatyLimitRatio = dbTreatyLimitRatio;
    }
    
    public void setShares(DTOList shares) {
        this.shares = shares;
    }
    
    public BigDecimal getDbTreatyLimit() {
        return dbTreatyLimit;
    }
    
    public void setDbTreatyLimit(BigDecimal dbTreatyLimit) {
        this.dbTreatyLimit = dbTreatyLimit;
    }
    
    public BigDecimal getDbTreatyLimitDefault() {
        return getTreatyDetail().getDbTreatyLimit();
    }
    
    public InsuranceTreatyDetailView getTreatyDetail() {
        if (treatyDetail==null) treatyDetail = (InsuranceTreatyDetailView) DTOPool.getInstance().getDTO(InsuranceTreatyDetailView.class, stInsuranceTreatyDetailID);
        return treatyDetail;
    }
    
    public void setTreatyDetail(InsuranceTreatyDetailView treatyDetail) {
        this.treatyDetail = treatyDetail;
    }
    
    
    public String getStInsurancePolicyTreatyDetailID() {
        return stInsurancePolicyTreatyDetailID;
    }
    
    public void setStInsurancePolicyTreatyDetailID(String stInsurancePolicyTreatyDetailID) {
        this.stInsurancePolicyTreatyDetailID = stInsurancePolicyTreatyDetailID;
    }
    
    public String getStInsurancePolicyTreatyID() {
        return stInsurancePolicyTreatyID;
    }
    
    public void setStInsurancePolicyTreatyID(String stInsurancePolicyTreatyID) {
        this.stInsurancePolicyTreatyID = stInsurancePolicyTreatyID;
    }
    
    public BigDecimal getDbTSIAmount() {
        return dbTSIAmount;
    }
    
    public void setDbTSIAmount(BigDecimal dbTSIAmount) {
        this.dbTSIAmount = dbTSIAmount;
    }
    
    public BigDecimal getDbPremiAmount() {
        return dbPremiAmount;
    }
    
    public void setDbPremiAmount(BigDecimal dbPremiAmount) {
        this.dbPremiAmount = dbPremiAmount;
    }
    
    public BigDecimal getDbComissionRate() {
        return dbComissionRate;
    }
    
    public void setDbComissionRate(BigDecimal dbComissionRate) {
        this.dbComissionRate = dbComissionRate;
    }
    
    public BigDecimal getDbComission() {
        return dbComission;
    }
    
    public void setDbComission(BigDecimal dbComission) {
        this.dbComission = dbComission;
    }
    
    public String getStInsuranceTreatyDetailID() {
        return stInsuranceTreatyDetailID;
    }
    
    public void setStInsuranceTreatyDetailID(String stInsuranceTreatyDetailID) {
        this.stInsuranceTreatyDetailID = stInsuranceTreatyDetailID;
    }
    
    public String getStTreatyClassDesc() {
        if (getTreatyDetail()==null) return null;
        
        if (treatyDetail.getTreatyType()==null) return null;
        
        return treatyDetail.getTreatyType().getStInsuranceTreatyTypeName();
    }

    public void initShares()throws Exception {

        getDetails();

        getTreatyDetail();

        final DTOList masterShares = treatyDetail.getShares();

        getShares();

        final boolean isCaptive = getTreaty().getObject().getPolicy().isCaptivePolicy();

        if (treatyDetail.isOR()) {
            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

            ri.markNew();

            ri.setStMemberEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
            ri.setDbSharePct(new BigDecimal("100"));
            ri.setStAutoRateFlag("Y");
            ri.setStUseRateFlag("Y");
            ri.setDbPremiRate(null);
            ri.setStInsuranceTreatyDetailID(treatyDetail.getStInsuranceTreatyDetailID());
            ri.setStApprovedFlag("Y");

            shares.add(ri);

            return;

        }

        for (int i = 0; i < masterShares.size(); i++) {
            InsuranceTreatySharesView shrm = (InsuranceTreatySharesView) masterShares.get(i);

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) getTreaty().getObject();
            //divide r/i by years
            Date policyDateStart = getTreaty().getObject().getPolicy().getDtPeriodStart();
            Date policyDateEnd = getTreaty().getObject().getPolicy().getDtPeriodEnd();

            if(getTreaty().getObject().getPolicy().getStPolicyTypeGroupID().equalsIgnoreCase("1")){
                if(obj.getDtReference1()!=null) policyDateStart = obj.getDtReference1();
                if(obj.getDtReference2()!=null) policyDateEnd = obj.getDtReference2();
            }

             if(treatyDetail.isDivideByYears()){
                DateTime startDate = new DateTime(policyDateStart);
                DateTime endDate = new DateTime(policyDateEnd);
                Years y = Years.yearsBetween(startDate, endDate);
                int years = y.getYears();

                Date policyDate = getTreaty().getObject().getPolicy().getDtPolicyDate();
                DateTime polDate = new DateTime(policyDate);

                Months m = Months.monthsBetween(startDate, endDate);

                int bulan = m.getMonths();

                BigDecimal sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                BigDecimal totalPct = null;

                if(years >= 1){

                    if((bulan % 12) != 0) {
                        years = years + 1;
                        sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                    }

                        for(int j = 0; j < years; j++){
                            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                            ri.markNew();

                            if(j==(years-1)){
                                sharePct = BDUtil.sub(BDUtil.hundred,totalPct);
                            }

                            ri.setDtValidReinsuranceDate(advance(policyDateStart,j));

                            if(j==0){

                                boolean withinCurrentMonth = DateUtil.getDateStr(policyDate, "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(policyDateStart, "yyyyMM"));

                                if(!withinCurrentMonth)
                                    ri.setDtValidReinsuranceDate(policyDate);
                                else
                                    ri.setDtValidReinsuranceDate(policyDateStart);
                            }

                            if((bulan % 12) != 0){
                                if(j==(years-1)){
                                    ri.setDtValidReinsuranceDate(policyDateEnd);
                                }
                            }

                            ri.setStMemberEntityID(shrm.getStMemberEntityID());

                            ri.setDbSharePct(sharePct);
                            ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                            ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                            ri.setDbPremiRate(shrm.getDbPremiRate());

                            if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                            else	ri.setDbRICommRate(shrm.getDbRICommRate());
                            ri.setStUseRateFlag(shrm.getStUseRateFlag());
                            ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                            ri.setStControlFlags(shrm.getStControlFlags());
                            ri.setStApprovedFlag("Y");
                            ri.setStReinsuranceEntityID(shrm.getStReinsuranceEntityID());

                            totalPct = BDUtil.add(totalPct,ri.getDbSharePct());

                            shares.add(ri);
                        }
                }else if(years < 1){
                        final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                        ri.markNew();

                        ri.setDtValidReinsuranceDate(policyDateStart);
                        ri.setStMemberEntityID(shrm.getStMemberEntityID());
                        ri.setDbSharePct(shrm.getDbSharePct());
                        ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                        ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                        ri.setDbPremiRate(shrm.getDbPremiRate());

                        if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                        else	ri.setDbRICommRate(shrm.getDbRICommRate());
                        ri.setStUseRateFlag(shrm.getStUseRateFlag());
                        ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                        ri.setStControlFlags(shrm.getStControlFlags());
                        ri.setStApprovedFlag("Y");
                        ri.setStReinsuranceEntityID(shrm.getStReinsuranceEntityID());

                        shares.add(ri);
                }
            }else{
                final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                ri.markNew();

                ri.setStMemberEntityID(shrm.getStMemberEntityID());
                ri.setDbSharePct(shrm.getDbSharePct());
                ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                ri.setDbPremiRate(shrm.getDbPremiRate());

                if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                else	ri.setDbRICommRate(shrm.getDbRICommRate());
                ri.setStUseRateFlag(shrm.getStUseRateFlag());
                ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                ri.setStControlFlags(shrm.getStControlFlags());
                ri.setStApprovedFlag("Y");
                ri.setStReinsuranceEntityID(shrm.getStReinsuranceEntityID());

                shares.add(ri);
            }


        }
    }
    
    public void initSharesTrial()throws Exception {
        
        getDetails();
        
        getTreatyDetail();
        
        final DTOList masterShares = treatyDetail.getShares();
        
        getShares();
        
        final boolean isCaptive = getTreaty().getObject().getPolicy().isCaptivePolicy();
        
        if (treatyDetail.isOR()) {
            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();
            
            ri.markNew();
            
            ri.setStMemberEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
            ri.setDbSharePct(new BigDecimal("100"));
            ri.setStAutoRateFlag("Y");
            ri.setStUseRateFlag("Y");
            ri.setDbPremiRate(null);
            ri.setStInsuranceTreatyDetailID(treatyDetail.getStInsuranceTreatyDetailID());
            ri.setStApprovedFlag("Y");
            
            shares.add(ri);
            
            return;
            
        }
        
        for (int i = 0; i < masterShares.size(); i++) {
            InsuranceTreatySharesView shrm = (InsuranceTreatySharesView) masterShares.get(i);
            
            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) getTreaty().getObject();
            //divide r/i by years
            Date policyDateStart = getTreaty().getObject().getPolicy().getDtPeriodStart();
            Date policyDateEnd = getTreaty().getObject().getPolicy().getDtPeriodEnd();
            
            if(getTreaty().getObject().getPolicy().getStPolicyTypeGroupID().equalsIgnoreCase("1")){
                if(obj.getDtReference1()!=null) policyDateStart = obj.getDtReference1();
                if(obj.getDtReference2()!=null) policyDateEnd = obj.getDtReference2();
            }

             if(treatyDetail.isDivideByYears()){
                DateTime startDate = new DateTime(policyDateStart);
                DateTime endDate = new DateTime(policyDateEnd);
                Date policyDate = getTreaty().getObject().getPolicy().getDtPolicyDate();
                DateTime polDate = new DateTime(policyDate);

                //if(startDate.isBefore(polDate))
                    //startDate = polDate;

                Years y = Years.yearsBetween(startDate, endDate);
                int years = y.getYears();

                Months m = Months.monthsBetween(startDate, endDate);

                int bulan = m.getMonths();
            
                BigDecimal sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                BigDecimal totalPct = null;

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) getTreaty().getObject();
                logger.logWarning("############# reas objek : "+ objx.getStOrderNo());
                logger.logWarning("############# jumlah tahun : "+ years);

                if(years >= 1){

                    if((bulan % 12) != 0) {
                        //years = years + 1;
                        sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                    }

                        for(int j = 0; j < years; j++){
                            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                            ri.markNew();

                            if(j==(years-1)){
                                sharePct = BDUtil.sub(BDUtil.hundred,totalPct);
                            }

                            ri.setDtValidReinsuranceDate(advance(policyDateStart,j));

                            if(j==0){

                                boolean withinCurrentMonth = DateUtil.getDateStr(policyDate, "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(policyDateStart, "yyyyMM"));

                                if(!withinCurrentMonth)
                                    ri.setDtValidReinsuranceDate(policyDate);
                                else
                                    ri.setDtValidReinsuranceDate(policyDateStart);
                            }

                            if((bulan % 12) != 0){
                                if(j==(years-1)){
                                    ri.setDtValidReinsuranceDate(policyDateEnd);
                                }
                            }

                            ri.setStMemberEntityID(shrm.getStMemberEntityID());

                            ri.setDbSharePct(sharePct);
                            ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                            ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                            ri.setDbPremiRate(shrm.getDbPremiRate());

                            if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                            else	ri.setDbRICommRate(shrm.getDbRICommRate());
                            ri.setStUseRateFlag(shrm.getStUseRateFlag());
                            ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                            ri.setStControlFlags(shrm.getStControlFlags());
                            ri.setStApprovedFlag("Y");

                            totalPct = BDUtil.add(totalPct,ri.getDbSharePct());

                            shares.add(ri);
                        }
                }else if(years < 1){
                        final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                        ri.markNew();

                        ri.setDtValidReinsuranceDate(policyDateStart);
                        ri.setStMemberEntityID(shrm.getStMemberEntityID());
                        ri.setDbSharePct(shrm.getDbSharePct());
                        ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                        ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                        ri.setDbPremiRate(shrm.getDbPremiRate());

                        if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                        else	ri.setDbRICommRate(shrm.getDbRICommRate());
                        ri.setStUseRateFlag(shrm.getStUseRateFlag());
                        ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                        ri.setStControlFlags(shrm.getStControlFlags());
                        ri.setStApprovedFlag("Y");

                        shares.add(ri);
                }
            }else{
                final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();
                
                ri.markNew();
                
                ri.setStMemberEntityID(shrm.getStMemberEntityID());
                ri.setDbSharePct(shrm.getDbSharePct());
                ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                ri.setDbPremiRate(shrm.getDbPremiRate());
                
                if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                else	ri.setDbRICommRate(shrm.getDbRICommRate());
                ri.setStUseRateFlag(shrm.getStUseRateFlag());
                ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                ri.setStControlFlags(shrm.getStControlFlags());
                ri.setStApprovedFlag("Y");
                
                shares.add(ri);
            }
            
            
        }
    }
    
    public Date advance(Date perDate, int addYear) {

      //final int perlen = lgPeriodLength.intValue();

      final Calendar cld = Calendar.getInstance();

      cld.setTime(perDate);

      cld.add(Calendar.YEAR, addYear);

      return cld.getTime();

   }
    
    public DTOList getDetails() {
        loadDetails();
        return details;
    }
    
    private void loadDetails() {
        try {
            if (details==null) {
                details =
                        ListUtil.getDTOListFromQuery(
                        "select * from ins_pol_treaty_detail where ins_pol_treaty_id = ?",
                        new Object [] {stInsurancePolicyTreatyID},
                        InsurancePolicyTreatyDetailView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public void setDetails(DTOList details) {
        this.details = details;
    }
    
    public void calculate() {
        
        
    }
    
    public BigDecimal getDbBaseTSIAmount() {
        return dbBaseTSIAmount;
    }
    
    public void setDbBaseTSIAmount(BigDecimal dbBaseTSIAmount) {
        this.dbBaseTSIAmount = dbBaseTSIAmount;
    }
    
    public BigDecimal getDbClaimAmount() {
        return dbClaimAmount;
    }
    
    public void setDbClaimAmount(BigDecimal dbClaimAmount) {
        this.dbClaimAmount = dbClaimAmount;
    }
    
    public BigDecimal getDbMemberTreatyTSITotal() {
        
        getDetails();
        
        getTreatyDetail();
        
        DTOList shares = getShares();
        
        BigDecimal tot = null;
        
        BigDecimal totMemberPremiTotal = null;
        
        for (int i = 0; i < shares.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(i);
            
            //if(treatyDetail.isQSKR())
            //continue;
            //else
            tot = BDUtil.add(tot,ri.getDbTSIAmount());
            totMemberPremiTotal = BDUtil.add(totMemberPremiTotal,ri.getDbPremiAmount());
            //if(treatyDetail.isQSKR()){
            //	tot = BDUtil.sub(tot,ri.getDbTSIAmount());
            //}
        }
        setDbMemberTreatyPremiTotal(totMemberPremiTotal);
        return tot;
    }
    
    public BigDecimal getDbMemberTreatyPremiTotal() {
        return dbMemberTreatyPremiTotal;
    }
    
    public void setDbMemberTreatyPremiTotal(BigDecimal dbMemberTreatyPremiTotal) {
        this.dbMemberTreatyPremiTotal = dbMemberTreatyPremiTotal;
    }
    
   /*
   public InsurancePolicyTreatyView getTreaty() {
      final InsurancePolicyTreatyView treaty = (InsurancePolicyTreatyView) DTOPool.getInstance().getDTO(InsurancePolicyTreatyView.class, stInsurancePolicyTreatyID);
    
      return treaty;
   }*/
    
    public InsurancePolicyTreatyView getTreaty() throws Exception {
        return treaty;
    }
    
    public void setTreaty(InsurancePolicyTreatyView treaty) {
        this.treaty = treaty;
    }

    /**
     * @return the dbTSIFactor1
     */
    public BigDecimal getDbTSIFactor1() {
        return dbTSIFactor1;
    }

    /**
     * @param dbTSIFactor1 the dbTSIFactor1 to set
     */
    public void setDbTSIFactor1(BigDecimal dbTSIFactor1) {
        this.dbTSIFactor1 = dbTSIFactor1;
    }

    /**
     * @return the dbTSIFactor2
     */
    public BigDecimal getDbTSIFactor2() {
        return dbTSIFactor2;
    }

    /**
     * @param dbTSIFactor2 the dbTSIFactor2 to set
     */
    public void setDbTSIFactor2(BigDecimal dbTSIFactor2) {
        this.dbTSIFactor2 = dbTSIFactor2;
    }

    /**
     * @return the dbTSIFactor3
     */
    public BigDecimal getDbTSIFactor3() {
        return dbTSIFactor3;
    }

    /**
     * @param dbTSIFactor3 the dbTSIFactor3 to set
     */
    public void setDbTSIFactor3(BigDecimal dbTSIFactor3) {
        this.dbTSIFactor3 = dbTSIFactor3;
    }

    /**
     * @return the dbPremiFactor1
     */
    public BigDecimal getDbPremiFactor1() {
        return dbPremiFactor1;
    }

    /**
     * @param dbPremiFactor1 the dbPremiFactor1 to set
     */
    public void setDbPremiFactor1(BigDecimal dbPremiFactor1) {
        this.dbPremiFactor1 = dbPremiFactor1;
    }

    /**
     * @return the dbPremiFactor2
     */
    public BigDecimal getDbPremiFactor2() {
        return dbPremiFactor2;
    }

    /**
     * @param dbPremiFactor2 the dbPremiFactor2 to set
     */
    public void setDbPremiFactor2(BigDecimal dbPremiFactor2) {
        this.dbPremiFactor2 = dbPremiFactor2;
    }

    /**
     * @return the dbPremiFactor3
     */
    public BigDecimal getDbPremiFactor3() {
        return dbPremiFactor3;
    }

    /**
     * @param dbPremiFactor3 the dbPremiFactor3 to set
     */
    public void setDbPremiFactor3(BigDecimal dbPremiFactor3) {
        this.dbPremiFactor3 = dbPremiFactor3;
    }

    /**
     * @return the stAutoRateFlag
     */
    public String getStAutoRateFlag() {
        return stAutoRateFlag;
    }

    /**
     * @param stAutoRateFlag the stAutoRateFlag to set
     */
    public void setStAutoRateFlag(String stAutoRateFlag) {
        this.stAutoRateFlag = stAutoRateFlag;
    }

    /**
     * @return the stUseRateFlag
     */
    public String getStUseRateFlag() {
        return stUseRateFlag;
    }

    /**
     * @param stUseRateFlag the stUseRateFlag to set
     */
    public void setStUseRateFlag(String stUseRateFlag) {
        this.stUseRateFlag = stUseRateFlag;
    }

    /**
     * @return the stApprovedFlag
     */
    public String getStApprovedFlag() {
        return stApprovedFlag;
    }

    /**
     * @param stApprovedFlag the stApprovedFlag to set
     */
    public void setStApprovedFlag(String stApprovedFlag) {
        this.stApprovedFlag = stApprovedFlag;
    }

    public void initSharesProses(InsurancePolicyView policy, InsurancePolicyObjDefaultView obj2)throws Exception {

        getDetails();

        getTreatyDetail();

        final DTOList masterShares = treatyDetail.getShares();

        getShares();

        final boolean isCaptive = policy.isCaptivePolicy();

        if (treatyDetail.isOR()) {
            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

            ri.markNew();

            ri.setStMemberEntityID(Parameter.readString("UWRIT_CURRENT_COMPANY"));
            ri.setDbSharePct(new BigDecimal("100"));
            ri.setStAutoRateFlag("Y");
            ri.setStUseRateFlag("Y");
            ri.setDbPremiRate(null);
            ri.setStInsuranceTreatyDetailID(treatyDetail.getStInsuranceTreatyDetailID());
            ri.setStApprovedFlag("Y");

            shares.add(ri);

            return;

        }

        for (int i = 0; i < masterShares.size(); i++) {
            InsuranceTreatySharesView shrm = (InsuranceTreatySharesView) masterShares.get(i);

            final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) obj2;
            //divide r/i by years
            Date policyDateStart = policy.getDtPeriodStart();
            Date policyDateEnd = policy.getDtPeriodEnd();

            if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("1")){
                if(obj.getDtReference1()!=null) policyDateStart = obj.getDtReference1();
                if(obj.getDtReference2()!=null) policyDateEnd = obj.getDtReference2();
            }

            /*
             if(treatyDetail.isDivideByYears()){
                DateTime startDate = new DateTime(policyDateStart);
                DateTime endDate = new DateTime(policyDateEnd);
                Years y = Years.yearsBetween(startDate, endDate);
                int years = y.getYears();

                Date policyDate = policy.getDtPolicyDate();
                DateTime polDate = new DateTime(policyDate);

                Months m = Months.monthsBetween(startDate, endDate);

                int bulan = m.getMonths();

                BigDecimal sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                BigDecimal totalPct = null;

                if(years >= 1){

                    if((bulan % 12) != 0) {
                        years = years + 1;
                        sharePct = BDUtil.div(shrm.getDbSharePct(), new BigDecimal(years));
                    }

                        for(int j = 0; j < years; j++){
                            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                            ri.markNew();

                            if(j==(years-1)){
                                sharePct = BDUtil.sub(BDUtil.hundred,totalPct);
                            }

                            ri.setDtValidReinsuranceDate(advance(policyDateStart,j));

                            if(j==0){

                                boolean withinCurrentMonth = DateUtil.getDateStr(policyDate, "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(policyDateStart, "yyyyMM"));

                                if(!withinCurrentMonth)
                                    ri.setDtValidReinsuranceDate(policyDate);
                                else
                                    ri.setDtValidReinsuranceDate(policyDateStart);
                            }

                            if((bulan % 12) != 0){
                                if(j==(years-1)){
                                    ri.setDtValidReinsuranceDate(policyDateEnd);
                                }
                            }

                            ri.setStMemberEntityID(shrm.getStMemberEntityID());

                            ri.setDbSharePct(sharePct);
                            ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                            ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                            ri.setDbPremiRate(shrm.getDbPremiRate());

                            if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                            else	ri.setDbRICommRate(shrm.getDbRICommRate());
                            ri.setStUseRateFlag(shrm.getStUseRateFlag());
                            ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                            ri.setStControlFlags(shrm.getStControlFlags());
                            ri.setStApprovedFlag("Y");

                            totalPct = BDUtil.add(totalPct,ri.getDbSharePct());

                            shares.add(ri);
                        }
                }else if(years < 1){
                        final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                        ri.markNew();

                        ri.setDtValidReinsuranceDate(policyDateStart);
                        ri.setStMemberEntityID(shrm.getStMemberEntityID());
                        ri.setDbSharePct(shrm.getDbSharePct());
                        ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                        ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                        ri.setDbPremiRate(shrm.getDbPremiRate());

                        if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                        else	ri.setDbRICommRate(shrm.getDbRICommRate());
                        ri.setStUseRateFlag(shrm.getStUseRateFlag());
                        ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                        ri.setStControlFlags(shrm.getStControlFlags());
                        ri.setStApprovedFlag("Y");

                        shares.add(ri);
                }
            }else{
                final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                ri.markNew();

                ri.setStMemberEntityID(shrm.getStMemberEntityID());
                ri.setDbSharePct(shrm.getDbSharePct());
                ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                ri.setDbPremiRate(shrm.getDbPremiRate());

                if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                else	ri.setDbRICommRate(shrm.getDbRICommRate());
                ri.setStUseRateFlag(shrm.getStUseRateFlag());
                ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                ri.setStControlFlags(shrm.getStControlFlags());
                ri.setStApprovedFlag("Y");

                shares.add(ri);
            }
            */

            final InsurancePolicyReinsView ri = new InsurancePolicyReinsView();

                ri.markNew();

                ri.setStMemberEntityID(shrm.getStMemberEntityID());
                ri.setDbSharePct(shrm.getDbSharePct());
                ri.setStInsuranceTreatySharesID(shrm.getStInsuranceTreatySharesID());
                ri.setStInsuranceTreatyDetailID(shrm.getStInsuranceTreatyDetailID());
                ri.setDbPremiRate(shrm.getDbPremiRate());

                if(isCaptive)	ri.setDbRICommRate(shrm.getDbRICommCaptiveRate());
                else	ri.setDbRICommRate(shrm.getDbRICommRate());
                ri.setStUseRateFlag(shrm.getStUseRateFlag());
                ri.setStAutoRateFlag(shrm.getStAutoRateFlag());
                ri.setStControlFlags(shrm.getStControlFlags());
                ri.setStApprovedFlag("Y");
                ri.setStReinsuranceEntityID(shrm.getStReinsuranceEntityID());

                shares.add(ri);


        }
    }

    /**
     * @return the stManualClaimFlag
     */
    public String getStManualClaimFlag() {
        return stManualClaimFlag;
    }

    /**
     * @param stManualClaimFlag the stManualClaimFlag to set
     */
    public void setStManualClaimFlag(String stManualClaimFlag) {
        this.stManualClaimFlag = stManualClaimFlag;
    }

    public boolean isManualClaimSpreading(){
        return Tools.isYes(stManualClaimFlag);
    }

    /**
     * @return the dbPremiPerilsPerTreaty
     */
    public BigDecimal getDbPremiPerilsPerTreaty() {
        return dbPremiPerilsPerTreaty;
    }

    /**
     * @param dbPremiPerilsPerTreaty the dbPremiPerilsPerTreaty to set
     */
    public void setDbPremiPerilsPerTreaty(BigDecimal dbPremiPerilsPerTreaty) {
        this.dbPremiPerilsPerTreaty = dbPremiPerilsPerTreaty;
    }

    /**
     * @return the dbTSIPctCalc
     */
    public BigDecimal getDbTSIPctCalc() {
        return dbTSIPctCalc;
    }

    /**
     * @param dbTSIPctCalc the dbTSIPctCalc to set
     */
    public void setDbTSIPctCalc(BigDecimal dbTSIPctCalc) {
        this.dbTSIPctCalc = dbTSIPctCalc;
    }

    /**
     * @return the dbTSIAmountCalc
     */
    public BigDecimal getDbTSIAmountCalc() {
        return dbTSIAmountCalc;
    }

    /**
     * @param dbTSIAmountCalc the dbTSIAmountCalc to set
     */
    public void setDbTSIAmountCalc(BigDecimal dbTSIAmountCalc) {
        this.dbTSIAmountCalc = dbTSIAmountCalc;
    }

    public DTOList getSharesInterkoneksi() {
        loadSharesInterkoneksi();
        return shares;
    }

    private void loadSharesInterkoneksi() {
        try {
            if (shares==null)
                shares = ListUtil.getDTOListFromQueryDS(
                        " select * from ins_pol_ri where ins_pol_tre_det_id=?",
                        new Object [] {stInsurancePolicyTreatyDetailID},
                        InsurancePolicyReinsView.class,"GATEWAY"
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
