/***********************************************************************
 * Module:  com.webfin.gl.model.InsuranceClosingView
 * Author:  Ahmad Rhodoni
 * Created: 10 Oktober 2012
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.model.GLCostCenterView;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class InsuranceClosingView extends DTO implements RecordAudit {
   /*
   
CREATE TABLE ins_gl_closing
(
  closing_id bigint NOT NULL,
  policy_date_start date,
  policy_date_end date,
  period_start_start date,
  period_start_end date,
  pol_type_id bigint,
  treaty_type character varying(32),
  reins_closing_status character varying(1),
  finance_closing_status character varying(1),
  CONSTRAINT ins_gl_closing_pkey PRIMARY KEY (closing_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ins_gl_closing
  OWNER TO postgres;

   */
   private final static transient LogManager logger = LogManager.getInstance(InsuranceClosingView.class);

   public static String tableName = "ins_gl_closing";
   
   public transient static String comboFields[] = {"closing_id", "no_surat_hutang"};

   public static String fieldMap[][] = {
      {"stClosingID","closing_id*pk"},
      {"stClosingType","closing_type"},
      {"dtPolicyDateStart","policy_date_start"},
      {"dtPolicyDateEnd","policy_date_end"},
      {"dtPeriodStartStart","period_start_start"},
      {"dtPeriodStartEnd","period_start_end"},
      {"stPolicyTypeGroupID","pol_type_group_id"},
      {"stPolicyTypeID","pol_type_id"},
      {"stTreatyType","treaty_type"},
      {"stReinsuranceClosingStatus","reins_closing_status"},
      {"stFinanceClosingStatus","finance_closing_status"},
      {"dbTSITotal","tsi_total"},
      {"dbPremiReinsuranceTotal","premi_reins_total"},
      {"dbComissionReinsuranceTotal","comm_reins_total"},
      {"dbClaimReinsuranceTotal","claim_reins_total"},
      {"stDataAmount","data_amount"},
      {"stDataProses","data_proses"},
      {"stParameter1","param1"},
      {"stPolicyTypeDescription","policy_desc*n"},
      {"dtInvoiceDate","invoice_date"},
      {"stNoSuratHutang","no_surat_hutang"},
      {"stPolicyNo","pol_no"},
      {"stPolicyNoExclude","pol_no_exclude"},
      {"stDLANo","dla_no"},

   };

   private String stClosingID;
   private String stClosingType;
   private String stPolicyTypeID;
   private String stTreatyType;
   private String stReinsuranceClosingStatus;
   private String stFinanceClosingStatus;
   private String stPolicyTypeGroupID;

   private Date dtPolicyDateStart;
   private Date dtPolicyDateEnd;
   private Date dtPeriodStartStart;
   private Date dtPeriodStartEnd;

   private BigDecimal dbTSITotal;
   private BigDecimal dbPremiReinsuranceTotal;
   private BigDecimal dbComissionReinsuranceTotal;
   private BigDecimal dbClaimReinsuranceTotal;

   private String stDataAmount;
   private String stDataProses;
   private String stParameter1;
   private String stPolicyTypeDescription;
   private Date dtInvoiceDate;
   private String stNoSuratHutang;
   private String stPolicyNo;
   private String stPolicyNoExclude;
   private String stDLANo;

    private String stKasieClosingStatus;
    private String stKabagClosingStatus;
    private String stCostCenterCode = SessionManager.getInstance().getSession().getStBranch();

    /**
     * @return the stClosingID
     */
    public String getStClosingID() {
        return stClosingID;
    }

    /**
     * @param stClosingID the stClosingID to set
     */
    public void setStClosingID(String stClosingID) {
        this.stClosingID = stClosingID;
    }

    /**
     * @return the stClosingType
     */
    public String getStClosingType() {
        return stClosingType;
    }

    /**
     * @param stClosingType the stClosingType to set
     */
    public void setStClosingType(String stClosingType) {
        this.stClosingType = stClosingType;
    }

    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    /**
     * @return the stTreatyType
     */
    public String getStTreatyType() {
        return stTreatyType;
    }

    /**
     * @param stTreatyType the stTreatyType to set
     */
    public void setStTreatyType(String stTreatyType) {
        this.stTreatyType = stTreatyType;
    }

    /**
     * @return the stReinsuranceClosingStatus
     */
    public String getStReinsuranceClosingStatus() {
        return stReinsuranceClosingStatus;
    }

    /**
     * @param stReinsuranceClosingStatus the stReinsuranceClosingStatus to set
     */
    public void setStReinsuranceClosingStatus(String stReinsuranceClosingStatus) {
        this.stReinsuranceClosingStatus = stReinsuranceClosingStatus;
    }

    /**
     * @return the stFinanceClosingStatus
     */
    public String getStFinanceClosingStatus() {
        return stFinanceClosingStatus;
    }

    /**
     * @param stFinanceClosingStatus the stFinanceClosingStatus to set
     */
    public void setStFinanceClosingStatus(String stFinanceClosingStatus) {
        this.stFinanceClosingStatus = stFinanceClosingStatus;
    }

    /**
     * @return the dtPolicyDateStart
     */
    public Date getDtPolicyDateStart() {
        return dtPolicyDateStart;
    }

    /**
     * @param dtPolicyDateStart the dtPolicyDateStart to set
     */
    public void setDtPolicyDateStart(Date dtPolicyDateStart) {
        this.dtPolicyDateStart = dtPolicyDateStart;
    }

    /**
     * @return the dtPolicyDateEnd
     */
    public Date getDtPolicyDateEnd() {
        return dtPolicyDateEnd;
    }

    /**
     * @param dtPolicyDateEnd the dtPolicyDateEnd to set
     */
    public void setDtPolicyDateEnd(Date dtPolicyDateEnd) {
        this.dtPolicyDateEnd = dtPolicyDateEnd;
    }

    /**
     * @return the dtPeriodStartStart
     */
    public Date getDtPeriodStartStart() {
        return dtPeriodStartStart;
    }

    /**
     * @param dtPeriodStartStart the dtPeriodStartStart to set
     */
    public void setDtPeriodStartStart(Date dtPeriodStartStart) {
        this.dtPeriodStartStart = dtPeriodStartStart;
    }

    /**
     * @return the dtPeriodStartEnd
     */
    public Date getDtPeriodStartEnd() {
        return dtPeriodStartEnd;
    }

    /**
     * @param dtPeriodStartEnd the dtPeriodStartEnd to set
     */
    public void setDtPeriodStartEnd(Date dtPeriodStartEnd) {
        this.dtPeriodStartEnd = dtPeriodStartEnd;
    }

    /**
     * @return the stPolicyTypeGroupID
     */
    public String getStPolicyTypeGroupID() {
        return stPolicyTypeGroupID;
    }

    /**
     * @param stPolicyTypeGroupID the stPolicyTypeGroupID to set
     */
    public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
        this.stPolicyTypeGroupID = stPolicyTypeGroupID;
    }

    private DTOList reinsAll;

    public void setReinsAll(DTOList reinsAll) {
        this.reinsAll = reinsAll;
    }

    public void retrieveDataTotal() throws Exception {

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,"+
		  " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
		  " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount_e,2)*a.ccy_rate) as premi_amount_e,"+
		  " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt_e,2)*a.ccy_rate) as ricomm_amt_e,i.ri_slip_no,"+
		  " (getperiod(a.pol_type_id in (21,59),c.refd2,getperiod(a.pol_type_id in (1,3,24,81),c.refd1,a.period_start))) as period_start,"+
		  " (getperiod(a.pol_type_id in (21,59),c.refd3,getperiod(a.pol_type_id in (1,3,24,81),c.refd2,a.period_end))) as period_end");

      sqa.addQuery(" from ins_policy a "+
                   "  inner join ins_pol_obj c on c.pol_id = a.pol_id "+
                   "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "+
                   "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                   "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                   "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                   "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
                   "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

      sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
       sqa.addClause("a.active_flag = 'Y'");
      sqa.addClause("a.effective_flag = 'Y'");
      sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
      sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

      if(getDtPolicyDateStart()!=null){
          sqa.addClause("date_trunc('day',a.policy_date) >= ?");
          sqa.addPar(getDtPolicyDateStart());
      }

      if(getDtPolicyDateEnd()!=null){
          sqa.addClause("date_trunc('day',a.policy_date) <= ?");
          sqa.addPar(getDtPolicyDateEnd());
      }

      if (getStParameter1() != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
        }

      /*
      if(getClosing().getStTreatyType()!=null){
          sqa.addClause("f.treaty_type = ?");
          sqa.addPar(getClosing().getStTreatyType());
      }*/

      if (getStTreatyType() != null) {
            if (getStTreatyType().equalsIgnoreCase("SPL") || getStTreatyType().equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(getStTreatyType());
            }
        }

      if(getStPolicyTypeID()!=null){
          sqa.addClause("a.pol_type_id = ?");
          sqa.addPar(getStPolicyTypeID());
      }

      if(getStPolicyTypeGroupID()!=null){
          sqa.addClause("a.ins_policy_type_grp_id = ?");
          sqa.addPar(getStPolicyTypeGroupID());
      }

      sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no,c.refd2, c.refd1,c.refd3 ");

      String sql = " select * from ( "+sqa.getSQL() + ")a ";

      if(getDtPeriodStartStart()!=null){
          sql = sql + " where date_trunc('day',a.period_start) >= ?";
          sqa.addPar(getDtPeriodStartStart());
      }

      if(getDtPeriodStartEnd()!=null){
          sql = sql + " and date_trunc('day',a.period_start) <= ?";
          sqa.addPar(getDtPeriodStartEnd());
      }

      sql = sql + " order by pol_id,ins_treaty_detail_id";

      final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyReinsView.class);

      //return l;


    }

    public DTOList getReinsAllBackup() throws Exception {

          final SQLAssembler sqa = new SQLAssembler();

          String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

          String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount,2)) as premi_amount_e,"+
                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt,2)) as ricomm_amt_e";

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN"))
                        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(i.tsi_amount_e) as tsi_amount_e,"+
                                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(i.premi_amount) as premi_amount_e,"+
                                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(i.ricomm_amt) as ricomm_amt_e";
          }

          sqlSelect = sqlSelect + amountSelect;

          sqa.addSelect(sqlSelect);

            String clauseObj = "";
            if (getDtPolicyDateStart()  != null) {
                clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + getDtPolicyDateStart()  + "' ";
            }

          sqa.addQuery(" from ins_policy a "+
                       "  inner join ins_pol_obj c on c.pol_id = a.pol_id "+ clauseObj +
                       "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "+
                       "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                       "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                       "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                       "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
                       "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

          //sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

          if(isClosingRIOutward())
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
          else if(isClosingRIInwardToOutward())
                sqa.addClause("a.status = 'INWARD'");

          sqa.addClause("a.active_flag = 'Y'");
          sqa.addClause("a.effective_flag = 'Y'");
          sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
          sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

          if(getDtPolicyDateStart()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) >= ?");
              sqa.addPar(getDtPolicyDateStart());
          }

          if(getDtPolicyDateEnd()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) <= ?");
              sqa.addPar(getDtPolicyDateEnd());
          }

          if (getStParameter1() != null) {
            if (getStParameter1().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
            } else if (getStParameter1().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80)");
            }
          }

          /*
          if(getClosing().getStTreatyType()!=null){
              sqa.addClause("f.treaty_type = ?");
              sqa.addPar(getClosing().getStTreatyType());
          }*/

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("SPL") || getStTreatyType().equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(getStTreatyType());
                }
            }

          if(getStPolicyTypeID()!=null){
              sqa.addClause("a.pol_type_id = ?");
              sqa.addPar(getStPolicyTypeID());
          }

          if(getStPolicyTypeGroupID()!=null){
              sqa.addClause("a.ins_policy_type_grp_id = ?");
              sqa.addPar(getStPolicyTypeGroupID());
          }

          if (getStPolicyNo() != null) {
                String noPolis = "";

                String noPolisGabungan = getStPolicyNo().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no in ("+ noPolis +")");
          }

          if (getStPolicyNoExclude() != null) {
                String noPolis = "";
                String noPolisGabungan = getStPolicyNoExclude().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no not in ("+ noPolis +")");
          }

          if(getDtPeriodStartStart()!=null){
               if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                         sqa.addPar(getDtPeriodStartStart());
                    }else{
                          sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) >= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"+
                                        " else date_trunc('day',a.period_start) >= ? end");
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                    }
              }
          }

          if(getDtPeriodStartEnd()!=null){
              if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                         sqa.addPar(getDtPeriodStartEnd());
                    }else{
                         sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) <= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"+
                                        " else date_trunc('day',a.period_start) <= ? end");
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                    }
              }
          }

//          if (getStTreatyType() != null)
//            if (!getStTreatyType().equalsIgnoreCase("BPDAN"))
//                sqa.addClause("a.policy_date > '2016-01-01 00:00:00'");

          sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no");

          String sql = " select * from ( "+sqa.getSQL() + ")a ";

          /*
          if(getDtPeriodStartStart()!=null){
              sql = sql + " where date_trunc('day',a.period_start) >= ?";
              sqa.addPar(getDtPeriodStartStart());
          }

          if(getDtPeriodStartEnd()!=null){
              sql = sql + " and date_trunc('day',a.period_start) <= ?";
              sqa.addPar(getDtPeriodStartEnd());
          }*/

          sql = sql + " order by pol_id,ins_treaty_detail_id";

          final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    InsurancePolicyReinsView.class);

          return l;
   }

    public DTOList getReinsAll() throws Exception {

          final SQLAssembler sqa = new SQLAssembler();

          String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,"+
                             " i.ri_slip_no,";

          String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount,2)) as premi_amount_e,"+
                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt,2)) as ricomm_amt_e,"+
                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2)) as ricomm_inst,l.inst_date,i.binding_date";

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN"))
                        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(i.tsi_amount_e) as tsi_amount_e,"+
                                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(i.premi_amount) as premi_amount_e,"+
                                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(i.ricomm_amt) as ricomm_amt_e,"+
                                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2))  as ricomm_inst,l.inst_date,i.binding_date";
          }

          sqlSelect = sqlSelect + amountSelect;

          sqa.addSelect(sqlSelect);

            String clauseObj = "";
            if (getDtPolicyDateStart()  != null) {
                clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + getDtPolicyDateStart()  + "' ";
            }

          sqa.addQuery(" from ins_policy a "+
                       "  inner join ins_pol_obj c on c.pol_id = a.pol_id "+ clauseObj +
                       "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "+
                       "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                       "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                       "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                       "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
                       "    inner join ent_master k on k.ent_id = i.member_ent_id "+
                       "    left join ins_pol_ri_installment_test l on i.ins_pol_ri_id = l.ins_pol_ri_id ");

          //sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

          if(isClosingRIOutward())
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
          else if(isClosingRIInwardToOutward())
                sqa.addClause("a.status = 'INWARD'");

          sqa.addClause("a.active_flag = 'Y'");
          sqa.addClause("a.effective_flag = 'Y'");
          sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
          sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

          if(getDtPolicyDateStart()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) >= ?");
              sqa.addPar(getDtPolicyDateStart());
          }

          if(getDtPolicyDateEnd()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) <= ?");
              sqa.addPar(getDtPolicyDateEnd());
          }

          if (getStParameter1() != null) {
            if (getStParameter1().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (getStParameter1().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
          }

          /*
          if(getClosing().getStTreatyType()!=null){
              sqa.addClause("f.treaty_type = ?");
              sqa.addPar(getClosing().getStTreatyType());
          }*/

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("SPL") || getStTreatyType().equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(getStTreatyType());
                }
            }

          if(getStPolicyTypeID()!=null){
              sqa.addClause("a.pol_type_id = ?");
              sqa.addPar(getStPolicyTypeID());
          }

          if(getStPolicyTypeGroupID()!=null){
              sqa.addClause("a.ins_policy_type_grp_id = ?");
              sqa.addPar(getStPolicyTypeGroupID());
          }

          if (getStPolicyNo() != null) {
                String noPolis = "";

                String noPolisGabungan = getStPolicyNo().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no in ("+ noPolis +")");
          }

          if (getStPolicyNoExclude() != null) {
                String noPolis = "";
                String noPolisGabungan = getStPolicyNoExclude().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no not in ("+ noPolis +")");
          }

          if(getDtPeriodStartStart()!=null){
               if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                         sqa.addPar(getDtPeriodStartStart());
                    }else{
                          sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"+
                                        " else date_trunc('day',a.period_start) >= ? end");
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                    }
              }
          }

          if(getDtPeriodStartEnd()!=null){
              if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                         sqa.addPar(getDtPeriodStartEnd());
                    }else{
                         sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"+
                                        " else date_trunc('day',a.period_start) <= ? end");
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                    }
              }
          }

//          if (getStTreatyType() != null)
//            if (!getStTreatyType().equalsIgnoreCase("BPDAN"))
//                sqa.addClause("a.policy_date > '2016-01-01 00:00:00'");

          sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,"+
			" i.ri_slip_no,"+
                        " installment_f,l.installment_no,l.inst_date,binding_date");

          String sql = " select pol_no,pol_id,ins_treaty_detail_id,ins_treaty_shares_id,member_ent_id,member,"+
                        "  ri_slip_no,"+
                       " tsi_amount,tsi_amount_e,premi_amount,"+
                       "  case when installment_f = 'Y' and installment_no is not null and premi_inst is not null "+
                       "          then premi_inst else premi_amount_e end as premi_amount_e,"+
                       "  ricomm_amt,"+
                       "  case when installment_f = 'Y' and installment_no is not null and ricomm_inst is not null "+
                       "          then ricomm_inst else ricomm_amt_e end as ricomm_amt_e,"+
                       "  installment_f,installment_no,premi_inst,ricomm_inst,inst_date,binding_date "+
                       " from ( "+sqa.getSQL() + ")a ";

          /*
          if(getDtPeriodStartStart()!=null){
              sql = sql + " where date_trunc('day',a.period_start) >= ?";
              sqa.addPar(getDtPeriodStartStart());
          }

          if(getDtPeriodStartEnd()!=null){
              sql = sql + " and date_trunc('day',a.period_start) <= ?";
              sqa.addPar(getDtPeriodStartEnd());
          }*/

          sql = sql + " order by pol_id,ins_treaty_detail_id,member_ent_id,installment_no";

          final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    InsurancePolicyReinsView.class);

          return l;
   }

    /** 
     * @return the dbTSITotal
     */
    public BigDecimal getDbTSITotal() {
        return dbTSITotal;
    }

    /**
     * @param dbTSITotal the dbTSITotal to set
     */
    public void setDbTSITotal(BigDecimal dbTSITotal) {
        this.dbTSITotal = dbTSITotal;
    }

    /**
     * @return the dbPremiReinsuranceTotal
     */
    public BigDecimal getDbPremiReinsuranceTotal() {
        return dbPremiReinsuranceTotal;
    }

    /**
     * @param dbPremiReinsuranceTotal the dbPremiReinsuranceTotal to set
     */
    public void setDbPremiReinsuranceTotal(BigDecimal dbPremiReinsuranceTotal) {
        this.dbPremiReinsuranceTotal = dbPremiReinsuranceTotal;
    }

    /**
     * @return the dbComissionReinsuranceTotal
     */
    public BigDecimal getDbComissionReinsuranceTotal() {
        return dbComissionReinsuranceTotal;
    }

    /**
     * @param dbComissionReinsuranceTotal the dbComissionReinsuranceTotal to set
     */
    public void setDbComissionReinsuranceTotal(BigDecimal dbComissionReinsuranceTotal) {
        this.dbComissionReinsuranceTotal = dbComissionReinsuranceTotal;
    }

    /**
     * @return the dbClaimReinsuranceTotal
     */
    public BigDecimal getDbClaimReinsuranceTotal() {
        return dbClaimReinsuranceTotal;
    }

    /**
     * @param dbClaimReinsuranceTotal the dbClaimReinsuranceTotal to set
     */
    public void setDbClaimReinsuranceTotal(BigDecimal dbClaimReinsuranceTotal) {
        this.dbClaimReinsuranceTotal = dbClaimReinsuranceTotal;
    }

    /**
     * @return the stDataAmount
     */
    public String getStDataAmount() {
        return stDataAmount;
    }

    /**
     * @param stDataAmount the stDataAmount to set
     */
    public void setStDataAmount(String stDataAmount) {
        this.stDataAmount = stDataAmount;
    }

    /**
     * @return the stDataProses
     */
    public String getStDataProses() {
        return stDataProses;
    }

    /**
     * @param stDataProses the stDataProses to set
     */
    public void setStDataProses(String stDataProses) {
        this.stDataProses = stDataProses;
    }

    /**
     * @return the stParameter1
     */
    public String getStParameter1() {
        return stParameter1;
    }

    /**
     * @param stParameter1 the stParameter1 to set
     */
    public void setStParameter1(String stParameter1) {
        this.stParameter1 = stParameter1;
    }

    /**
     * @return the stPolicyTypeDescription
     */
    public String getStPolicyTypeDescription() {
        return stPolicyTypeDescription;
    }

    /**
     * @param stPolicyTypeDescription the stPolicyTypeDescription to set
     */
    public void setStPolicyTypeDescription(String stPolicyTypeDescription) {
        this.stPolicyTypeDescription = stPolicyTypeDescription;
    }

    /**
     * @return the dtInvoiceDate
     */
    public Date getDtInvoiceDate() {
        return dtInvoiceDate;
    }

    /**
     * @param dtInvoiceDate the dtInvoiceDate to set
     */
    public void setDtInvoiceDate(Date dtInvoiceDate) {
        this.dtInvoiceDate = dtInvoiceDate;
    }

    /**
     * @return the stNoSuratHutang
     */
    public String getStNoSuratHutang() {
        return stNoSuratHutang;
    }

    /**
     * @param stNoSuratHutang the stNoSuratHutang to set
     */
    public void setStNoSuratHutang(String stNoSuratHutang) {
        this.stNoSuratHutang = stNoSuratHutang;
    }

    public void validate() throws Exception {

        if(Tools.isYes(getStFinanceClosingStatus())){
            if(getDtInvoiceDate()==null)
                throw new RuntimeException("Tanggal pembukuan harus diisi");

            if(Tools.isNo(getStReinsuranceClosingStatus()))
                throw new RuntimeException("Data belum di closing oleh bagian reasuransi");

            if(isPosted())
                throw new RuntimeException("Neraca bulan "+ DateUtil.getMonth2Digit(dtInvoiceDate) + " tahun "+ DateUtil.getYear(dtInvoiceDate) +" sudah di posting secara nasional");
        }

    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, DateUtil.getMonth2Digit(dtInvoiceDate));
            PS.setString(2, DateUtil.getYear(dtInvoiceDate));

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public DTOList getReinsClaimAll() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

        String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate_claim) as tsi_amount,sum(round(i.claim_amount*a.ccy_rate_claim,2)) as premi_amount,sum(round(i.claim_amount,2)) as premi_amount_e";

        if (getStTreatyType() != null) {
            if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                amountSelect = " sum(i.tsi_amount) as tsi_amount,sum(round(i.claim_amount*a.ccy_rate_claim,2)) as premi_amount, sum(i.claim_amount) as premi_amount_e";
            }
        }

        sqlSelect = sqlSelect + amountSelect;

        sqa.addSelect(sqlSelect);

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("coalesce(i.claim_amount,0) <> 0");
        sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) >= ?");
            sqa.addPar(getDtPolicyDateStart());
        }

        if (getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) <= ?");
            sqa.addPar(getDtPolicyDateEnd());
        }

        if (getStParameter1() != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
        }

        if (getStTreatyType() != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(getStTreatyType());
        }

        if (getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getStPolicyTypeID());
        }

        if (getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getStPolicyTypeGroupID());
        }

        if (getDtPeriodStartStart() != null) {
            if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                    sqa.addPar(getDtPeriodStartStart());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"
                            + " else date_trunc('day',a.period_start) >= ? end");
                    sqa.addPar(getDtPeriodStartStart());
                    sqa.addPar(getDtPeriodStartStart());
                    sqa.addPar(getDtPeriodStartStart());
                }
            }
        }

        if (getDtPeriodStartEnd() != null) {
            if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                    sqa.addPar(getDtPeriodStartEnd());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"
                            + " else date_trunc('day',a.period_start) <= ? end");
                    sqa.addPar(getDtPeriodStartEnd());
                    sqa.addPar(getDtPeriodStartEnd());
                    sqa.addPar(getDtPeriodStartEnd());
                }
            }
        }

        if (getStDLANo() != null) {

            String noLKP = "";
            String noLKPGabungan = getStDLANo().replace("\n", "").replace("\r", "");
            String noLKPArray[] = noLKPGabungan.split("[\\,]");

            for (int k = 0; k < noLKPArray.length; k++) {

                    if(k>0) noLKP = noLKP +",";

                    noLKP = noLKP + "'" + noLKPArray[k] + "'";
            }

            sqa.addClause("a.dla_no in ("+ noLKP +")");
        }

        sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no");

        String sql = " select * from ( " + sqa.getSQL() + ") a ";

        sql = sql + " order by pol_id,ins_treaty_detail_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyReinsView.class);

        return l;
    }

    public DTOList getReinsClaimInwardAll() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ins_pol_inward a "
                + "left join ent_master b on b.ent_id = a.ent_id "
                + "left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (17,18,19,25,23,24) ");

        if (getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getDtPolicyDateStart());
        }

        if (getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getDtPolicyDateEnd());
        }

        if (getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(getDtPeriodStartStart());
        }

        if (getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(getDtPeriodStartEnd());
        }

        if (getStParameter1() != null) {
            sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33,87,88)");
        }

        if (getStTreatyType() != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(getStTreatyType());
        }

        if (getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(getStPolicyTypeID());
        }

        String sql = sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date,"
                + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate "
                + "order by a.ar_trx_type_id,a.attr_pol_type_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyInwardView.class);

        return l;

    }

    public boolean isClosingReins() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select * from ins_gl_closing "
                    + "where closing_type = 'PREMIUM_RI_OUTWARD' and reins_closing_status = 'Y' "
                    + "and date_trunc('day',policy_date_end) >= ? "
                    + "and date_trunc('day',policy_date_end) <= ? ";

            if(getStTreatyType()!=null){
                cek = cek + " and treaty_type = ? ";
            }

            /*
            if (getDtPeriodStartStart() != null) {
            cek = cek + " and date_trunc('day',period_start_start) >= ? ";
            //PS.setString(4, dtPeriodStartStart.toString());
            }

            if (getDtPeriodStartEnd() != null) {
            cek = cek + " and date_trunc('day',period_start_end) <= ? ";
            //PS.setString(5, dtPeriodStartEnd.toString());
            }

            if (getStPolicyTypeGroupID() != null) {
            cek = cek + " and pol_type_group_id = ? ";
            //PS.setString(6, stPolicyTypeGroupID);

            if (getStPolicyTypeID() != null) {
            cek = cek + " and pol_type_id = ? ";
            //PS.setString(7, stPolicyTypeID);
            }
            }
             */

            cek = cek + " order by closing_id ";

            String treaty = null;

            if(getStTreatyType()!=null){
                if (stTreatyType.equalsIgnoreCase("SPL")) {
                    treaty = "QS";
                } else {
                    treaty = stTreatyType;
                }
            }

            PreparedStatement PS = S.setQuery(cek);
            
            PS.setString(1, dtPolicyDateStart.toString());
            PS.setString(2, dtPolicyDateEnd.toString());

            if(getStTreatyType()!=null){
                PS.setString(3, treaty);
            }

            /*
            if (getDtPeriodStartStart() != null) {
            PS.setString(4, dtPeriodStartStart.toString());
            }
            if (getDtPeriodStartEnd() != null) {
            PS.setString(5, dtPeriodStartEnd.toString());
            }
            if (getStPolicyTypeGroupID() != null) {
            PS.setString(6, stPolicyTypeGroupID);
            if (getStPolicyTypeID() != null) {
            PS.setString(7, stPolicyTypeID);
            }
            }
             */

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public InsurancePolicyTypeGroupView getPolicyTypeGroup() {
        return (InsurancePolicyTypeGroupView) DTOPool.getInstance().getDTO(InsurancePolicyTypeGroupView.class, stPolicyTypeGroupID);
    }

    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
    }

    private DTOList invoice;

    public DTOList getInvoice() {
        loadInvoice();
        return invoice;
    }

    public void setInvoice(DTOList invoice) {
        this.invoice = invoice;
    }

    public void loadInvoice() {
        try {
            if (invoice == null) {
                invoice = ListUtil.getDTOListFromQuery(
                        "select b.* from ins_gl_closing a "
                        + "inner join ar_invoice b on b.no_surat_hutang = a.no_surat_hutang "
                        + "where a.no_surat_hutang = ? ",
                        new Object[]{stNoSuratHutang},
                        ARInvoiceView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validateReverse() throws Exception {

            if(getDtInvoiceDate()==null)
                throw new RuntimeException("Tanggal pembukuan harus diisi");

            if(Tools.isNo(getStReinsuranceClosingStatus()))
                throw new RuntimeException("Data belum di closing oleh bagian reasuransi");

            if(isPosted())
                throw new RuntimeException("Neraca bulan "+ DateUtil.getMonth2Digit(dtInvoiceDate) + " tahun "+ DateUtil.getYear(dtInvoiceDate) +" sudah di posting secara nasional");
            
    }

    public DTOList getTaxAll() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,a.no_surat_hutang");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is not null ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.invoice_type = 'AP'");
        //sqa.addClause("substr(a.no_surat_hutang,25,7) = '" + DateUtil.getMonth2Digit(dtPolicyDateStart) + "/" + DateUtil.getYear(dtPolicyDateStart) + "'");

        sqa.addClause("a.no_surat_hutang like '%" + DateUtil.getMonth2Digit(dtPolicyDateStart) + "/" + DateUtil.getYear(dtPolicyDateStart) + "'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("substr(a.refid2,0,4) = 'TAX'");
        sqa.addClause("d.ar_trx_line_id in (14,17,20,30,33,36,46,49,52,15,18,19,31,34,35,47,50,51,107)");

        if (getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getDtPolicyDateStart());
        }

        if (getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getDtPolicyDateEnd());
        }

        String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvoiceView.class);

        return l;
    }

    public boolean isClosingRIOutward(){
        return getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD");
    }

    public boolean isClosingRIInwardToOutward(){
        return getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_OUTWARD");
    }

    /**
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    /**
     * @return the stPolicyNoExclude
     */
    public String getStPolicyNoExclude() {
        return stPolicyNoExclude;
    }

    /**
     * @param stPolicyNoExclude the stPolicyNoExclude to set
     */
    public void setStPolicyNoExclude(String stPolicyNoExclude) {
        this.stPolicyNoExclude = stPolicyNoExclude;
    }

    public DTOList getProposalComm() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,a.no_surat_hutang");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40)");

        sqa.addClause("a.no_surat_hutang is null");//PROPOSAL
        sqa.addClause("a.approved_flag is null");//PROPOSAL
        //sqa.addClause("a.surat_hutang_period_from is null");//PROPOSAL
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

        if (getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(getStCostCenterCode());
        }

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) and g.pol_id is not null ";

        if (getDtPolicyDateStart() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + getDtPolicyDateStart() + "'";
        }

        if (getDtPolicyDateEnd() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + getDtPolicyDateEnd() + "'";
        }

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

//        if (getDtPolicyDateStart() != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(getDtPolicyDateStart());
//        }
//
//        if (getDtPolicyDateEnd() != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
//            sqa.addPar(getDtPolicyDateEnd());
//        }

        String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvoiceView.class);

        return l;
    }

    public DTOList getClosingComm() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.ar_invoice_id,a.no_surat_hutang");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40)");

        //sqa.addClause("a.no_surat_hutang is null");
        //sqa.addClause("a.approved_flag = 'Y'");//CLOSING
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

//        sqa.addClause("date_trunc('day',a.surat_hutang_period_from) = ?");//CLOSING
//        sqa.addPar(DateUtil.getDateStr(getDtCreateDate(), "dd-MM-yyyy"));

        sqa.addClause("a.approved_flag is null ");//CLOSING
        sqa.addClause("a.no_surat_hutang = ?");
        sqa.addPar(getStNoSuratHutang());//CLOSING

        if (getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(getStCostCenterCode());
        }

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) and g.pol_id is not null ";

        if (getDtPolicyDateStart() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + getDtPolicyDateStart() + "'";
        }

        if (getDtPolicyDateEnd() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + getDtPolicyDateEnd() + "'";
        }

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

//        if (getDtPolicyDateStart() != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
//            sqa.addPar(getDtPolicyDateStart());
//        }
//
//        if (getDtPolicyDateEnd() != null) {
//            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
//            sqa.addPar(getDtPolicyDateEnd());
//        }

        String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvoiceView.class);

        return l;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public GLCostCenterView getCostCenter(String stCostCenterCode) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return costcenter;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public boolean isReinsuranceFlag() {
        return Tools.isYes(stReinsuranceClosingStatus);
    }

    public boolean isKasieClosingFlag() {
        return Tools.isYes(stKasieClosingStatus);
    }

    public boolean isKabagClosingFlag() {
        return Tools.isYes(stKabagClosingStatus);
    }

    public boolean isFinanceFlag() {
        return Tools.isYes(stFinanceClosingStatus);
    }

    /**
     * @return the stDLANo
     */
    public String getStDLANo() {
        return stDLANo;
    }

    /**
     * @param stDLANo the stDLANo to set
     */
    public void setStDLANo(String stDLANo) {
        this.stDLANo = stDLANo;
    }

    public DTOList getReinsOutwardXOL()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.* ");

    sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

    sqa.addClause("a.approved_flag = 'Y'");
    sqa.addClause("a.ar_trx_type_id in (22) ");
    if (getDtPolicyDateStart() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) >= ?");
      sqa.addPar(getDtPolicyDateStart());
    }
    if (getDtPolicyDateEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) <= ?");
      sqa.addPar(getDtPolicyDateEnd());
    }
    if (getDtPeriodStartStart() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
      sqa.addPar(getDtPeriodStartStart());
    }
    if (getDtPeriodStartEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
      sqa.addPar(getDtPeriodStartEnd());
    }
    if (getStParameter1() != null) {
      sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33,87,88)");
    }
    if (getStTreatyType() != null)
    {
      sqa.addClause("a.refid0 = ?");
      sqa.addPar(getStTreatyType());
    }
    if (getStPolicyTypeID() != null)
    {
      sqa.addClause("a.attr_pol_type_id = ?");
      sqa.addPar(getStPolicyTypeID());
    }
    String sql = sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyInwardView.class);

    return l;
  }

  public DTOList getReinsInwardFac()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.* ");

    sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

    sqa.addClause("a.approved_flag = 'Y'");
    sqa.addClause("a.ar_trx_type_id in (1) ");
    if (getDtPolicyDateStart() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) >= ?");
      sqa.addPar(getDtPolicyDateStart());
    }
    if (getDtPolicyDateEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) <= ?");
      sqa.addPar(getDtPolicyDateEnd());
    }
    if (getDtPeriodStartStart() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
      sqa.addPar(getDtPeriodStartStart());
    }
    if (getDtPeriodStartEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
      sqa.addPar(getDtPeriodStartEnd());
    }
    if (getStParameter1() != null) {
      sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33)");
    }
    if (getStTreatyType() != null)
    {
      sqa.addClause("a.refid0 = ?");
      sqa.addPar(getStTreatyType());
    }
    if (getStPolicyTypeID() != null)
    {
      sqa.addClause("a.attr_pol_type_id = ?");
      sqa.addPar(getStPolicyTypeID());
    }
    String sql = sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyInwardView.class);

    return l;
  }

  private DTOList arinvoices;

    public DTOList getArinvoices() {
        loadARInvoices();
        return arinvoices;
    }

    private void loadARInvoices() {
        try {
            arinvoices = ListUtil.getDTOListFromQuery(
                    " select a.ar_cust_id,b.ent_name as attr_pol_name,a.due_date,sum(a.amount) as amount,"
                    + "sum(a.amount_settled) as  amount_settled,a.receipt_date,a.receipt_no "
                    + "from ar_invoice a inner join ent_master b on b.ent_id = a.ar_cust_id "
                    + "where coalesce(a.cancel_flag,'') <> 'Y' and a.no_surat_hutang = ? "
                    + "group by a.ar_cust_id,a.due_date,a.receipt_date,a.receipt_no,b.ent_name "
                    + "order by a.ar_cust_id ",
                    new Object[]{getStNoSuratHutang()},
                    ARInvoiceView.class);

        } catch (NullPointerException e) {

            arinvoices = new DTOList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setArinvoices(DTOList arinvoices) {
        this.arinvoices = arinvoices;
    }

    public DTOList getReinsProfitCommision() throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.* ");

    sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

    sqa.addClause("a.approved_flag = 'Y'");
    sqa.addClause("a.ar_trx_type_id in (20,21) ");

    if (getDtPolicyDateStart() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) >= ?");
      sqa.addPar(getDtPolicyDateStart());
    }
    if (getDtPolicyDateEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.due_date) <= ?");
      sqa.addPar(getDtPolicyDateEnd());
    }
    if (getDtPeriodStartStart() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
      sqa.addPar(getDtPeriodStartStart());
    }
    if (getDtPeriodStartEnd() != null)
    {
      sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
      sqa.addPar(getDtPeriodStartEnd());
    }
    if (getStParameter1() != null) {
      sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33)");
    }
    if (getStTreatyType() != null)
    {
      sqa.addClause("a.refid0 = ?");
      sqa.addPar(getStTreatyType());
    }
    if (getStPolicyTypeID() != null)
    {
      sqa.addClause("a.attr_pol_type_id = ?");
      sqa.addPar(getStPolicyTypeID());
    }
    String sql = sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsurancePolicyInwardView.class);

    return l;
  }

    public DTOList getReinsClaimInwardToOutwardAll() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

        String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate_claim) as tsi_amount,sum(round(i.claim_amount*a.ccy_rate_claim,2)) as premi_amount,sum(round(i.claim_amount,2)) as premi_amount_e";

        if (getStTreatyType() != null) {
            if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                amountSelect = " sum(i.tsi_amount) as tsi_amount,sum(round(i.claim_amount*a.ccy_rate_claim,2)) as premi_amount, sum(i.claim_amount) as premi_amount_e";
            }
        }

        sqlSelect = sqlSelect + amountSelect;

        sqa.addSelect(sqlSelect);

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

        sqa.addClause("a.status in ('CLAIM INWARD','ENDORSE CLAIM INWARD')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("coalesce(i.claim_amount,0) <> 0");
        sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(getDtPolicyDateStart());
        }

        if (getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(getDtPolicyDateEnd());
        }

        if (getStParameter1() != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,87,88)");
        }

        if (getStTreatyType() != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(getStTreatyType());
        }

        if (getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getStPolicyTypeID());
        }

        if (getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getStPolicyTypeGroupID());
        }

        if (getDtPeriodStartStart() != null) {
            if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                    sqa.addPar(getDtPeriodStartStart());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"
                            + " else date_trunc('day',a.period_start) >= ? end");
                    sqa.addPar(getDtPeriodStartStart());
                    sqa.addPar(getDtPeriodStartStart());
                    sqa.addPar(getDtPeriodStartStart());
                }
            }
        }

        if (getDtPeriodStartEnd() != null) {
            if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                    sqa.addPar(getDtPeriodStartEnd());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"
                            + " else date_trunc('day',a.period_start) <= ? end");
                    sqa.addPar(getDtPeriodStartEnd());
                    sqa.addPar(getDtPeriodStartEnd());
                    sqa.addPar(getDtPeriodStartEnd());
                }
            }
        }

        if (getStDLANo() != null) {

            String noLKP = "";
            String noLKPGabungan = getStDLANo().replace("\n", "").replace("\r", "");
            String noLKPArray[] = noLKPGabungan.split("[\\,]");

            for (int k = 0; k < noLKPArray.length; k++) {

                    if(k>0) noLKP = noLKP +",";

                    noLKP = noLKP + "'" + noLKPArray[k] + "'";
            }

            sqa.addClause("a.dla_no in ("+ noLKP +")");
        }

        sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no");

        String sql = " select * from ( " + sqa.getSQL() + ") a ";

        sql = sql + " order by pol_id,ins_treaty_detail_id";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsurancePolicyReinsView.class);

        return l;
    }

    public DTOList getReinsAllInvoiceWrong() throws Exception {

          final SQLAssembler sqa = new SQLAssembler();

          String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,I.REINS_ENT_ID,"+
                             " M.ENT_NAME AS REINS_NAME,i.ri_slip_no,";

          String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount,2)) as premi_amount_e,"+
                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt,2)) as ricomm_amt_e,"+
                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2)) as ricomm_inst,l.inst_date,i.binding_date";

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN"))
                        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(i.tsi_amount_e) as tsi_amount_e,"+
                                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(i.premi_amount) as premi_amount_e,"+
                                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(i.ricomm_amt) as ricomm_amt_e,"+
                                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2))  as ricomm_inst,l.inst_date,i.binding_date";
          }

          sqlSelect = sqlSelect + amountSelect;

          sqa.addSelect(sqlSelect);

            String clauseObj = "";
            if (getDtPolicyDateStart()  != null) {
                clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + getDtPolicyDateStart()  + "' ";
            }

          sqa.addQuery(" from ins_policy a "+
                       "  inner join ins_pol_obj c on c.pol_id = a.pol_id "+ clauseObj +
                       "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "+
                       "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                       "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                       "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                       "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
                       "    inner join ent_master k on k.ent_id = i.member_ent_id "+
                       "    left join ins_pol_ri_installment l on i.ins_pol_ri_id = l.ins_pol_ri_id  "+
                       "    INNER JOIN ENT_MASTER M ON M.ENT_ID = I.reins_ent_id ");

          //sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

          if(isClosingRIOutward())
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
          else if(isClosingRIInwardToOutward())
                sqa.addClause("a.status = 'INWARD'");

          sqa.addClause("a.active_flag = 'Y'");
          sqa.addClause("a.effective_flag = 'Y'");

          if (getStTreatyType() != null){
              if (!getStTreatyType().equalsIgnoreCase("FAC"))
                  sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
          }
          
          sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

          if(getDtPolicyDateStart()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) >= ?");
              sqa.addPar(getDtPolicyDateStart());
          }

          if(getDtPolicyDateEnd()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) <= ?");
              sqa.addPar(getDtPolicyDateEnd());
          }

          if (getStParameter1() != null) {
            if (getStParameter1().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (getStParameter1().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
          }

          /*
          if(getClosing().getStTreatyType()!=null){
              sqa.addClause("f.treaty_type = ?");
              sqa.addPar(getClosing().getStTreatyType());
          }*/

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("SPL") || getStTreatyType().equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(getStTreatyType());
                }
            }

          if(getStPolicyTypeID()!=null){
              sqa.addClause("a.pol_type_id = ?");
              sqa.addPar(getStPolicyTypeID());
          }

          if(getStPolicyTypeGroupID()!=null){
              sqa.addClause("a.ins_policy_type_grp_id = ?");
              sqa.addPar(getStPolicyTypeGroupID());
          }

          if (getStPolicyNo() != null) {
                String noPolis = "";

                String noPolisGabungan = getStPolicyNo().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no in ("+ noPolis +")");
          }

          if (getStPolicyNoExclude() != null) {
                String noPolis = "";
                String noPolisGabungan = getStPolicyNoExclude().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no not in ("+ noPolis +")");
          }

          if(getDtPeriodStartStart()!=null){
               if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                         sqa.addPar(getDtPeriodStartStart());
                    }else{
                          sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"+
                                        " else date_trunc('day',a.period_start) >= ? end");
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                    }
              }
          }

          if(getDtPeriodStartEnd()!=null){
              if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                         sqa.addPar(getDtPeriodStartEnd());
                    }else{
                         sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"+
                                        " else date_trunc('day',a.period_start) <= ? end");
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                    }
              }
          }

//          if (getStTreatyType() != null)
//            if (!getStTreatyType().equalsIgnoreCase("BPDAN"))
//                sqa.addClause("a.policy_date > '2016-01-01 00:00:00'");

          sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,I.reins_ent_id,"+
			" M.ENT_NAME,i.ri_slip_no,"+
                        " installment_f,l.installment_no,l.inst_date,binding_date");

          String sql = " select pol_no,pol_id,ins_treaty_detail_id,ins_treaty_shares_id,member_ent_id,member,REINS_ENT_ID,"+
                        " REINS_NAME,ri_slip_no,"+
                       " tsi_amount,tsi_amount_e,premi_amount,"+
                       "  case when installment_f = 'Y' and installment_no is not null and premi_inst is not null "+
                       "          then premi_inst else premi_amount_e end as premi_amount_e,"+
                       "  ricomm_amt,"+
                       "  case when installment_f = 'Y' and installment_no is not null and ricomm_inst is not null "+
                       "          then ricomm_inst else ricomm_amt_e end as ricomm_amt_e,"+
                       "  installment_f,installment_no,premi_inst,ricomm_inst,inst_date,binding_date "+
                       " from ( "+sqa.getSQL() + ")a ";

          /*
          if(getDtPeriodStartStart()!=null){
              sql = sql + " where date_trunc('day',a.period_start) >= ?";
              sqa.addPar(getDtPeriodStartStart());
          }

          if(getDtPeriodStartEnd()!=null){
              sql = sql + " and date_trunc('day',a.period_start) <= ?";
              sqa.addPar(getDtPeriodStartEnd());
          }*/

          sql = sql + " order by pol_id,ins_treaty_detail_id,member_ent_id,installment_no";

          final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    InsurancePolicyReinsView.class);

          return l;
   }

    public DTOList getReinsAllInvoice() throws Exception {

          final SQLAssembler sqa = new SQLAssembler();

          String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,"+
                             " i.ri_slip_no,";

          String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount,2)) as premi_amount_e,"+
                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt,2)) as ricomm_amt_e,"+
                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2)) as ricomm_inst,l.inst_date,i.binding_date";

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("BPDAN"))
                        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(i.tsi_amount_e) as tsi_amount_e,"+
                                      " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(i.premi_amount) as premi_amount_e,"+
                                      " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(i.ricomm_amt) as ricomm_amt_e,"+
                                      " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2))  as ricomm_inst,l.inst_date,i.binding_date";
          }

          sqlSelect = sqlSelect + amountSelect;

          sqa.addSelect(sqlSelect);

            String clauseObj = "";
            if (getDtPolicyDateStart()  != null) {
                clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + getDtPolicyDateStart()  + "' ";
            }

          sqa.addQuery(" from ins_policy a "+
                       "  inner join ins_pol_obj c on c.pol_id = a.pol_id "+ clauseObj +
                       "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "+
                       "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                       "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                       "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                       "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "+
                       "    inner join ent_master k on k.ent_id = i.member_ent_id "+
                       "    left join ins_pol_ri_installment l on i.ins_pol_ri_id = l.ins_pol_ri_id ");

          //sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");

          if(isClosingRIOutward())
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
          else if(isClosingRIInwardToOutward())
                sqa.addClause("a.status = 'INWARD'");

          sqa.addClause("a.active_flag = 'Y'");
          sqa.addClause("a.effective_flag = 'Y'");
          sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");

          if (getStTreatyType() != null){
              if (!getStTreatyType().equalsIgnoreCase("FAC"))
                  sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
          }

          sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

          if(getDtPolicyDateStart()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) >= ?");
              sqa.addPar(getDtPolicyDateStart()); 
          }

          if(getDtPolicyDateEnd()!=null){
              sqa.addClause("date_trunc('day',a.policy_date) <= ?");
              sqa.addPar(getDtPolicyDateEnd());
          }

          if (getStParameter1() != null) {
            if (getStParameter1().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (getStParameter1().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
          }

          /*
          if(getClosing().getStTreatyType()!=null){
              sqa.addClause("f.treaty_type = ?");
              sqa.addPar(getClosing().getStTreatyType());
          }*/

          if (getStTreatyType() != null) {
                if (getStTreatyType().equalsIgnoreCase("SPL") || getStTreatyType().equalsIgnoreCase("QS")) {
                    sqa.addClause("j.treaty_type in ('SPL','QS')");
                } else {
                    sqa.addClause("j.treaty_type = ?");
                    sqa.addPar(getStTreatyType());
                }
            }

          if(getStPolicyTypeID()!=null){
              sqa.addClause("a.pol_type_id = ?");
              sqa.addPar(getStPolicyTypeID());
          }

          if(getStPolicyTypeGroupID()!=null){
              sqa.addClause("a.ins_policy_type_grp_id = ?");
              sqa.addPar(getStPolicyTypeGroupID());
          }

          if (getStPolicyNo() != null) {
                String noPolis = "";

                String noPolisGabungan = getStPolicyNo().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no in ("+ noPolis +")");
          }

          if (getStPolicyNoExclude() != null) {
                String noPolis = "";
                String noPolisGabungan = getStPolicyNoExclude().replace("\n", "").replace("\r", "");
                String noPolisArray[] = noPolisGabungan.split("[\\,]");

                for (int k = 0; k < noPolisArray.length; k++) {

                    if(k>0) noPolis = noPolis +",";

                    noPolis = noPolis + "'" + noPolisArray[k] + "'";
                }

                sqa.addClause("a.pol_no not in ("+ noPolis +")");
          }

          if(getDtPeriodStartStart()!=null){
               if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                         sqa.addPar(getDtPeriodStartStart());
                    }else{
                          sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"+
                                        " else date_trunc('day',a.period_start) >= ? end");
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                          sqa.addPar(getDtPeriodStartStart());
                    }
              }
          }

          if(getDtPeriodStartEnd()!=null){
              if (getStTreatyType() != null) {
                    if (getStTreatyType().equalsIgnoreCase("BPDAN")){
                         sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                         sqa.addPar(getDtPeriodStartEnd());
                    }else{
                         sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "+
                                        " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"+
                                        " else date_trunc('day',a.period_start) <= ? end");
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                          sqa.addPar(getDtPeriodStartEnd());
                    }
              }
          }

//          if (getStTreatyType() != null)
//            if (!getStTreatyType().equalsIgnoreCase("BPDAN"))
//                sqa.addClause("a.policy_date > '2016-01-01 00:00:00'");

          sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,"+
			" i.ri_slip_no,"+
                        " installment_f,l.installment_no,l.inst_date,binding_date");

          String sql = " select pol_no,pol_id,ins_treaty_detail_id,ins_treaty_shares_id,member_ent_id,member,"+
                        " ri_slip_no,"+
                       " tsi_amount,tsi_amount_e,premi_amount,"+
                       "  case when installment_f = 'Y' and installment_no is not null and premi_inst is not null "+
                       "          then premi_inst else premi_amount_e end as premi_amount_e,"+
                       "  ricomm_amt,"+
                       "  case when installment_f = 'Y' and installment_no is not null and ricomm_inst is not null "+
                       "          then ricomm_inst else ricomm_amt_e end as ricomm_amt_e,"+
                       "  installment_f,installment_no,premi_inst,ricomm_inst,inst_date,binding_date "+
                       " from ( "+sqa.getSQL() + ")a ";

          /*
          if(getDtPeriodStartStart()!=null){
              sql = sql + " where date_trunc('day',a.period_start) >= ?";
              sqa.addPar(getDtPeriodStartStart());
          }

          if(getDtPeriodStartEnd()!=null){
              sql = sql + " and date_trunc('day',a.period_start) <= ?";
              sqa.addPar(getDtPeriodStartEnd());
          }*/

          sql = sql + " order by pol_id,ins_treaty_detail_id,member_ent_id,installment_no";

          final DTOList l = ListUtil.getDTOListFromQuery(
                    sql,
                    sqa.getPar(),
                    InsurancePolicyReinsView.class);

          return l;
   }
    
}
