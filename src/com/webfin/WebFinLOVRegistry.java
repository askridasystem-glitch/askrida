/***********************************************************************
 * Module:  com.webfin.WebFinLOVRegistry
 * Author:  Denny Mahendra
 * Created: Dec 31, 2005 10:45:09 PM
 * Purpose:
 ***********************************************************************/
package com.webfin;

import com.crux.util.*;
import com.crux.common.parameter.Parameter;
import com.webfin.ar.model.ARTransactionTypeView;
import com.webfin.ar.model.ARTaxView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.insurance.model.InsuranceRiskCategoryView;
import com.webfin.postalcode.model.PostalCodeView;
import com.webfin.insurance.model.PrintingCodesView;
import com.webfin.address.model.AddressCodeView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.AccountView2;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.ARInvestmentBungaView;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentPerpanjanganView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARReceiptClassView;
import com.webfin.gl.codes.GLCodes;
import com.webfin.gl.util.GLUtil;
import com.webfin.insurance.model.BiayaOperasionalDetail;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsuranceDocumentTypeView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyParentView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.uploadProposalCommView;
import com.webfin.postalcode.model.PostalCodeMaiparkView;

import java.util.Map;
import java.util.Date;

public class WebFinLOVRegistry {

    private InsurancePolicyView policy;
    private static WebFinLOVRegistry staticinstance;
    public String tes;
    private final static transient LogManager logger = LogManager.getInstance(WebFinLOVRegistry.class);

    public static WebFinLOVRegistry getInstance() {
        if (staticinstance == null) {
            staticinstance = new WebFinLOVRegistry();
        }
        return staticinstance;
    }

    public void setStCostC(String tes) {
        this.tes = tes;
    }

    public String getStCostC() {
        return tes;
    }

    public LOV LOV_BusinessSource() throws Exception {
        return ListUtil.getLookUpFromQuery("select ref1, ref2 from ff_table where fft_group_id='FFT_BUSSRC' order by ref2");
    }

    public LOV LOV_Marketter() throws Exception {
        return ListUtil.getLookUpFromQuery("select ref1, ref2 from ff_table where fft_group_id='FFT_MARKETR' order by ref2");
    }

    public LOV LOV_Country() throws Exception {
        return ListUtil.getLookUpFromQuery("select country_id, country_name from s_country");
    }

    public LOV LOV_RegionLevel1(Map parameters) throws Exception {
        //return LOV_RegionMap(parameters,1);
        return ListUtil.getLookUpFromQuery("select region_id, region_name from s_region_map where region_level=1");
    }

    public LOV LOV_RegionLevel2(Map parameters) throws Exception {
        return LOV_RegionMap(parameters, 2);
    }

    public LOV LOV_RegionLevel3(Map parameters) throws Exception {
        return LOV_RegionMap(parameters, 3);
    }

    public LOV LOV_RegionLevel4(Map parameters) throws Exception {
        return LOV_RegionMap(parameters, 4);
    }

    public LOV LOV_CustomerClass() throws Exception {
        return FinCodec.EntityClass.getLookUp();
    }

    public LOV LOV_ClaimStatus() throws Exception {
        return FinCodec.ClaimStatus.getLookUp();
    }

    public LOV LOV_Branch() throws Exception {
        return LOV_CostCenter();
    }

    public LOV LOV_TreatyClass() throws Exception {
        return FinCodec.TreatyClass.getLookUp();
    }

    public LOV LOV_CustomerShareLevel() throws Exception {
        return FinCodec.CustomerShareLevel.getLookUp();
    }

    public LOV LOV_PolicyLevel() throws Exception {
        return FinCodec.PolicyStatus.getLookUp();
    }

    public LOV LOV_EntityOnly(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object grup = params.get("grup");

        sqa.addSelect("a.*,b.vs_description as customer_category");

        sqa.addQuery("from ent_master a "
                + " inner join bussines_source b on a.category1 = b.vs_code");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ent_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(a.ent_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.setLimit(50);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(a.ent_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.setLimit(50);
        }

        if (grup != null) {
            sqa.addClause("a.ref1 = ? ");
            final String key = ((String) grup).toUpperCase();
            sqa.addPar(key);
        }

        sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");

        sqa.addOrder("a.ent_id desc");

        if (search == null) {
            sqa.setLimit(1);
        }

        return sqa.getList(EntityView.class);
    }

    public LOV LOV_Profil(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_users a "
                + "left join gl_cost_center b on b.cc_code = a.branch");
        sqa.addSelect("a.user_id, a.user_name, a.division, b.description as branch_name ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.user_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.user_name||coalesce(b.description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(20);
        }else{
            sqa.setLimit(1);
        }

        sqa.addClause("coalesce(a.delete_flag,'N') <> 'Y'");

        return sqa.getList(UserSessionView.class);
    }

    public LOV LOV_PostalCode(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_region_map3");
        sqa.addSelect("region_map_id, postal_code, region_map_desc, building_desc");

        final Object street = params.get("street");
        final Object value = params.get("value");
        final Object kabupaten = params.get("kabupaten2");

        if (kabupaten == null && street == null && value == null) {
            return new LookUpUtil();
        }

        if (value != null) {
            sqa.addClause("region_map_id=?");
            sqa.addPar(value);
        }

        sqa.setLimit(50);

        if (kabupaten != null) {
            sqa.addClause("upper(ref1) like ? ");
            final String key = "%" + ((String) kabupaten).toUpperCase() + "%";
            sqa.addPar(key);
        }

        if (street != null) {
            sqa.addClause("(upper(region_name) like ? or upper(city_name) like ? or postal_code like ? or building_desc like ?)");
            final String key = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key2);

            final String key3 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key3);

            final String key4 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key4);

        }

        return sqa.getList(PostalCodeView.class);
    }

    public LOV LOV_PeriodBase() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_period_base_id,description from ins_period_base order by ins_period_base_id");
    }

    public LOV LOV_LANG() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select lang_id,lang_name from s_lang order by lang_name").setNoNull().setLOValue(Parameter.readString("SYS_DEFAULT_LANG"));
    }

    public LOV LOV_CostCenter() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select cc_code,description from gl_cost_center order by cc_code");
    }

    public LOV LOV_TreatyType() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_treaty_type_id,treaty_type_name from ins_treaty_types order by ins_treaty_type_id");
    }

    public LOV LOV_PeriodFactor() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_premium_factor_id,description from ins_premium_factor order by period_rate_factor");
    }

    public LOV LOV_GLRptColType() {
        return FinCodec.GLReportColType.getLookUp();
    }

    public LOV LOV_ClaimItem() throws Exception {
        //return FinCodec.InsuranceCoverType.getLookUp();
        return ListUtil.getLookUpFromQuery(
                "select ins_item_id,description from ins_items where active_flag='Y' and ins_item_class='CLAIM'");
    }

    public LOV LOV_InsuranceCoverType() throws Exception {
        //return FinCodec.InsuranceCoverType.getLookUp();
        return ListUtil.getLookUpFromQuery(
                "select ins_cover_source_id,description from ins_cover_source");
    }

    public LOV LOV_InsuranceCompany(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ent_master");
        sqa.addSelect("ent_id, ent_name,short_name,address");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ent_id=?");
            sqa.addPar(value);
        }

        sqa.addClause("ins_company_flag = 'Y'");
        sqa.addClause("coalesce(active_flag,'Y') <> 'N'");
        sqa.setLimit(50);

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(ent_name||coalesce(address,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(100);
        }

        return sqa.getList(EntityView.class);
    }

    public LOV LOV_InsuranceItem(Map parameters) throws Exception {

        final String coversource = (String) JSPUtil.getParameter(parameters, "coversrc");

        return ListUtil.getLookUpFromQuery(
                "select ins_item_id,description from ins_items where ins_item_class = 'PREMI'",
                new Object[]{});
    }

    public LOV LOV_Religion() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select religion_id,religion_name from s_religion");
    }

    public LOV LOV_InsurancePeriod() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_period_id,description from ins_period order by description");
    }

    public LOV LOV_ClaimCause(Map parameters) throws Exception {
        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "select ins_clm_caus_id,cause_desc from ins_clm_cause where pol_type_id = ? order by active_flag desc,ins_clm_caus_id",
                new Object[]{poltype});
    }

    public LOV LOV_ClaimLoss(Map parameters) throws Exception {
        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "select claim_loss_id,loss_desc from ins_claim_loss where pol_type_id = ?",
                new Object[]{poltype});
    }

    public LOV LOV_Currency() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ccy_code,ccy_code || ' - ' || coalesce(ccy_desc,'') as desc from gl_currency order by oid").setNoNull().setLOValue(CurrencyManager.getInstance().getMasterCurrency());
    }

    public LOV LOV_SettlementXC(Map parameters) throws Exception {
        //final String arsettlementid = (String)parameters.get("arsettlementid");

        return ListUtil.getLookUpFromQuery(
                "select ar_settlement_xc_id,description from ar_settlement_excess");
    }

    public LOV LOV_ReceiptClass(Map parameters) throws Exception {
        final String custcat = (String) parameters.get("custcat");

        final String invoice_type = (String) parameters.get("invoice_type");

        if (invoice_type != null) {
            return ListUtil.getLookUpFromQuery(
                    "select rc_id,description from receipt_class where invoice_type = ? or invoice_type is null and rc_id not in(5,7) order by rc_id",
                    new Object[]{invoice_type});
        }

        if (custcat != null) {
            return ListUtil.getLookUpFromQuery(
                    "select rc_id,description from receipt_class where remit_flag = ? order by rc_id ",
                    new Object[]{custcat});
        }

        return ListUtil.getLookUpFromQuery(
                "select rc_id,description from receipt_class where rc_id not in(5,7) order by rc_id");

    }

    public LOV LOV_PolicyConditions(Map parameters) throws Exception {

        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "   select"
                + "      pol_subtype_id, description"
                + "   from ins_policy_subtype"
                + "   where"
                + "      pol_type_id=? and active_flag='Y'",
                new Object[]{
                    poltype
                });
    }

    public LOV LOV_PolicyConditionsOld(Map parameters) throws Exception {

        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "   select "
                + "      a.ins_cover_id,a.description "
                + "   from "
                + "      ins_cover a,ins_cover_poltype b "
                + "   where "
                + "      a.cover_category=? and a.ins_cover_id = b.ins_cover_id"
                + "      and b.pol_type_id = ?",
                new Object[]{
                    FinCodec.CoverCategory.MAIN,
                    poltype
                });
    }

    public LOV LOV_Treaty(Map parameters) throws Exception {

        final Date pstart = (Date) parameters.get("per_start");
        final Date pend = (Date) parameters.get("per_end");
        final Date ptype = (Date) parameters.get("poltype");

        if (pstart == null) {
            return new DTOList();
        }

        final SQLAssembler sqa = new SQLAssembler();


        sqa.addSelect("ins_treaty_id,treaty_name");

        sqa.addQuery("from ins_treaty");

        if (pstart != null) {
            //sqa.addClause("treaty_period_start <= ?");
            //sqa.addClause("treaty_period_end >= ?");
            //sqa.addPar(pstart);
            //sqa.addPar(pstart);
        }

        sqa.addClause("active_flag = 'Y'");

        sqa.addOrder("treaty_period_start,ins_treaty_id");

        return sqa.getLookUp();
    }

    public LOV LOV_RiskCategory(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_id , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ins_risk_cat_id=?");
            sqa.addPar(value);
        }

        String poltype = (String) JSPUtil.getParameter(params, "poltype");

        final Object o = params.get("datestart");
        final Object search = params.get("search");

        if (o == null || o.toString().equalsIgnoreCase("") || o.toString().equalsIgnoreCase("null")) {
            poltype = "99999";
        }

        if (search != null) {
            if (o != null && !o.toString().equalsIgnoreCase("") && !o.toString().equalsIgnoreCase("null")) {
                if (o instanceof String) {
                    String datestart2 = DateUtil.getDateTimeStr3((String) o);
                    Date c = DateUtil.getDate(datestart2);

                    if (c != null) {
                        sqa.addClause("period_start <= ? ");
                        sqa.addPar(c);
                        sqa.addClause("period_end >= ?");
                        sqa.addPar(c);
                    }

                } else if (o instanceof Date) {
                    if (o != null) {
                        sqa.addClause("period_start <= ? ");
                        sqa.addPar(o);
                        sqa.addClause("period_end >= ?");
                        sqa.addPar(o);
                    }

                }
            }
        }



        if (poltype != null) {
            sqa.addClause("poltype_id=?");
            sqa.addPar(poltype);
        }

        sqa.setLimit(50);

        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(50);
        }

        return sqa.getLookUp();
    }

    public LOV LOV_EarthquakeZone(Map params) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_id , description, ref1");

        final Object value = params.get("value");

        final Object o = params.get("policy.dtPeriodStart");

        /*
        if(o!=null&&!o.toString().equalsIgnoreCase("")&&!o.toString().equalsIgnoreCase("null"))
        {
        if(o instanceof String)
        {
        String datestart2 = DateUtil.getDateTimeStr3((String) o);
        Date c = DateUtil.getDate(datestart2);

        if(c!=null)
        {
        sqa.addClause("period_start <= ? ");
        sqa.addPar(c);
        sqa.addClause("period_end >= ?");
        sqa.addPar(c);
        }

        }
        else if(o instanceof Date)
        {
        if(o!=null)
        {
        sqa.addClause("period_start <= ? ");
        sqa.addPar(o);
        sqa.addClause("period_end >= ?");
        sqa.addPar(o);
        }

        }
        }
         */

        if (value != null) {
            sqa.addClause("ins_risk_cat_id=?");
            sqa.addPar(value);
        }

        sqa.addClause("poltype_id=?");
        if (o == null || o.toString().equalsIgnoreCase("") || o.toString().equalsIgnoreCase("null")) {
            sqa.addPar("99999");
        } else {
            sqa.addPar("99");
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.addClause("active_flag ='Y'");

            sqa.setLimit(50);
        }

        //return sqa.getLookUp();
        return sqa.getList(InsuranceRiskCategoryView.class);
    }

    public LOV LOV_EquakeZone(Map params) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_code,description");

        final Object value = params.get("value");

        if (value != null) {
            sqa.addClause("ins_risk_cat_code=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.addClause("poltype_id= 99");

            sqa.addGroup("ins_risk_cat_code,description");

            sqa.setLimit(100);
        }

        return sqa.getLookUp();
    }

    public LOV LOV_PaymentMethod(Map parameters) throws Exception {
        final String rc = (String) JSPUtil.getParameter(parameters, "rc");
        final String ccbanktype = (String) JSPUtil.getParameter(parameters, "ccbanktype");
        //final String cc = (String)JSPUtil.getParameter(parameters,"cc");
        final String ccbanktype2[] = ccbanktype.split("[\\|]");
        final String cc = ccbanktype2[0];
        final String banktype = ccbanktype2[1] == null ? "NULL" : ccbanktype2[1];


        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("pmt_method_id,description");
        sqa.addQuery("from payment_method");


        sqa.addClause("rc_id=?");
        sqa.addPar(rc);


        if (cc != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(cc);
        }

        if (!banktype.equalsIgnoreCase("NULL")) {
            sqa.addClause("bank_type = ?");
            sqa.addPar(banktype);
        }

        return sqa.getLookUp();

        /*return ListUtil.getLookUpFromQuery(
        "select pmt_method_id,description from payment_method where rc_id=?",
        new Object [] {rc}
        );*/
    }

    public LOV LOV_ARTrxType() throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select ar_trx_type_id,description from ar_trx_type",
                ARTransactionTypeView.class);
    }

    public LOV LOV_ARTax() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select tax_code,description from ar_tax where active_flag = 'Y' order by tax_code",
                ARTaxView.class);
    }

    public LOV LOV_Region(Map parameters) throws Exception {

        final String ccCode = (String) parameters.get("cc_code");
        final String value = (String) parameters.get("value");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("region_id,description");

        sqa.addQuery("from s_region");

        if (ccCode != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(ccCode);
        }

        if (value != null) {
            sqa.addClause("true or region_id=?");
            sqa.addPar(value);
        }

        sqa.addOrder("region_id");

        return sqa.getLookUp();
    }

    public LOV LOV_PolicyTypeGroup() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_policy_type_grp_id,group_name from ins_policy_type_grp where cat = 1 order by ins_policy_type_grp_id");
    }

    public LOV LOV_PolicyTypeSgl() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description from ins_policy_types ORDER BY POL_TYPE_ID");
    }

    public LOV LOV_PolicyType(Map parameters) throws Exception {
        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        if (polgroup == null) {
            return LOV_PolicyTypeSgl();
        }

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description from ins_policy_types where ins_policy_type_grp_id=? ORDER BY POL_TYPE_ID",
                new Object[]{polgroup});
    }

    public LOV LOV_PolicyType2(Map parameters) throws Exception {
        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        if (polgroup == null) {
            return LOV_PolicyTypeSgl();
        }

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description,poltype_code from ins_policy_types where ins_policy_type_grp_id=? ORDER BY POL_TYPE_ID",
                new Object[]{polgroup});
    }

    public LOV LOV_InvoiceType() throws Exception {
        return FinCodec.InvoiceType.getLookUp();
    }

    public LOV LOV_CustCategory1() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ?",
                new Object[]{"ASK_BUS_SOURCE"});
    }

    public LOV LOV_RegionMap(Map parameters, int level) throws Exception {
        final String countryID = (String) JSPUtil.getParameter(parameters, "country");
        final String parent = (String) JSPUtil.getParameter(parameters, "parent");

        if (level == 1) {
            if (countryID == null) {
                return new DTOList();
            }
        }

        if (level > 1) {
            if (parent == null) {
                return new DTOList();
            }
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("region_id, region_name");
        sqa.addQuery(" from s_region_map");

        sqa.addClause("region_level=?");
        sqa.addPar(new Integer(level));

        if (countryID != null) {
            sqa.addClause("country_id = ?");
            sqa.addPar(countryID);
        }

        if (parent != null) {
            sqa.addClause("parent_id = ?");
            sqa.addPar(parent);
        }

        return sqa.getLOV();
    }

    public LOV LOV_POL_PRINTING(Map parameters) throws Exception {
//      LookUpUtil lu = (LookUpUtil) LOVManager.getInstance().getLOV("VS_POL_PRINTING",null);

        String vs = (String) parameters.get("vs");

        if (vs == null) {
            vs = "";
        } else {
            vs = "_" + vs;
        }

        vs = "POL_PRINTING" + vs;

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,orderseq||'. '||vs_description as vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;

//      return lu.setNoNull();
    }

    public LOV LOV_RECEIPT_PRINTING(Map parameters) throws Exception {
//      LookUpUtil lu = (LookUpUtil) LOVManager.getInstance().getLOV("VS_POL_PRINTING",null);

        String vs = (String) parameters.get("vs");

        if (vs == null) {
            vs = "";
        } else {
            vs = "_" + vs;
        }

        vs = "RECEIPT_PRINTING" + vs;

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;

//      return lu.setNoNull();
    }

    public LOV LOV_GL_Period() {
        return FinCodec.GLPeriods.getLookUp();
    }

    public LOV LOV_MONTH_Period() {
        return FinCodec.MonthPeriods.getLookUp();
    }

    public LOV LOV_GL_Years() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select distinct fiscal_year,fiscal_year as x from gl_period order by fiscal_year");
    }

    public LOV LOV_Kecamatan(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_region_map2");
        sqa.addSelect("region_map_id, city_name, region_name");
        sqa.addClause("region_class = 3");

        final Object search = params.get("search");
        final Object value = params.get("value");

        if (search == null && value == null) {
            return new LookUpUtil();
        }

        if (value != null) {
            sqa.addClause("region_map_id=?");
            sqa.addPar(value);
        }

        sqa.setLimit(50);

        if (search != null) {
            sqa.addClause("upper(region_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        return sqa.getList(AddressCodeView.class);
    }

    public LOV LOV_Kota(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_region_map2");
        sqa.addSelect("region_map_id, city_name, region_name");
        sqa.addClause("region_class is null");

        final Object search = params.get("search");
        final Object value = params.get("value");

        if (search == null && value == null) {
            return new LookUpUtil();
        }

        if (value != null) {
            sqa.addClause("region_map_id=?");
            sqa.addPar(value);
        }

        sqa.setLimit(100);

        if (search != null) {
            sqa.addClause("upper(city_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        return sqa.getList(AddressCodeView.class);
    }

    public LOV LOV_Kelurahan(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_region_map2");
        sqa.addSelect("region_map_id, city_name, region_name");
        sqa.addClause("region_class = 2");

        final Object search = params.get("search");
        final Object value = params.get("value");

        if (search == null && value == null) {
            return new LookUpUtil();
        }

        if (value != null) {
            sqa.addClause("region_map_id=?");
            sqa.addPar(value);
        }

        sqa.setLimit(100);

        if (search != null) {
            sqa.addClause("upper(region_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        return sqa.getList(AddressCodeView.class);
    }

    public LOV LOV_TypeOfCredit(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");
        final String cc_code2 = (String) parameters.get("policy.stCostCenterCode");

        final Object search = parameters.get("search");

        String query = "";

        if (cc_code != null) {
            if (cc_code.equalsIgnoreCase("00")) {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT'";
            } else {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT' and ref1 = '" + cc_code + "'";
            }
        } else if (cc_code == null) {
            if (cc_code2 != null) {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT' and ref1 like '%" + cc_code2 + "%'";
            } else {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT'";
            }
        }

        if (search != null) {
            query = query + " and upper(vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }

        query = query + " order by orderseq";

        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_Dist() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select * from distribution order by id_dist");
    }

    public LOV LOV_ManualGroup() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group ='MANUAL_GROUP' order by orderseq asc");
    }

    public LOV LOV_ManualDetail(Map parameters) throws Exception {
        String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        polgroup = "MANUALDETAIL_" + polgroup;

        return ListUtil.getLookUpFromQuery(
                "select vs_code, orderseq || '. ' || vs_description as vs_description from s_valueset where vs_group = ? order by orderseq asc",
                new Object[]{polgroup});
    }

    public LOV LOV_ManualBook() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where ref1='MANUALBOOK' order by orderseq asc");
    }

    public LOV LOV_Guidance() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where ref1='GUIDANCE' order by orderseq asc");
    }

    public LOV LOV_TitipanPremi(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ar_titipan");
        sqa.addSelect("ar_titipan_id, trx_no, account_no, description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ar_titipan_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("trx_no like ? or account_no like ? or description like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addPar(key);
            sqa.addPar(key);

            sqa.setLimit(500);
        }

        //return sqa.getList(ARTitipanPremiView.class);
        return sqa.getLookUp();
    }

    public LOV LOV_BankType(Map parameters) throws Exception {
        final String rc = (String) JSPUtil.getParameter(parameters, "rc");
        final String cc = (String) JSPUtil.getParameter(parameters, "cc");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("vs_code, vs_description");
        sqa.addQuery("from s_valueset");


        sqa.addClause("vs_group=?");
        sqa.addPar("TITIPAN_TYPE");

        /*
        if (cc!=null) {
        sqa.addClause("cc_code = ?");
        sqa.addPar(cc);
        }*/

        return sqa.getLookUp();

        /*return ListUtil.getLookUpFromQuery(
        "select pmt_method_id,description from payment_method where rc_id=?",
        new Object [] {rc}
        );*/
    }

    public LOV LOV_Sort() throws Exception {
        /*
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("vs_code, vs_description");
        sqa.addQuery("from s_valueset");


        sqa.addClause("vs_group=?");
        sqa.addPar("GL_SORT");


        if (cc!=null) {
        sqa.addClause("cc_code = ?");
        sqa.addPar(cc);
        }

        return sqa.getLookUp();*/

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'GL_SORT'");
    }

    public LOV LOV_Coverage() throws Exception {
        /*
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("vs_code, vs_description");
        sqa.addQuery("from s_valueset");


        sqa.addClause("vs_group=?");
        sqa.addPar("GL_SORT");


        if (cc!=null) {
        sqa.addClause("cc_code = ?");
        sqa.addPar(cc);
        }

        return sqa.getLookUp();*/

        return ListUtil.getLookUpFromQuery(
                "select ins_cover_id, description from ins_cover order by ins_cover_id").setNoNull().setLOValue("asc");
    }

    public LOV LOV_CompType(Map Params) throws Exception {
        final String custcat = (String) Params.get("custcat");
        final String custcatdep = (String) Params.get("custcatdep");

        String query = "select vs_code, vs_description from s_valueset where vs_group = 'COMP_TYPE' and active_flag = 'Y' ";

        if (custcat != null) {
            if (Tools.isYes(custcat)) {
                query = query + " and vs_code not in (select cc_code from gl_cost_center) ";
            } else {
                query = query + " and vs_code in (select cc_code from gl_cost_center) ";
            }
        }

        if (custcatdep != null) {
            if (Tools.isYes(custcatdep)) {
                query = query + " and vs_code not in (select cc_code from gl_cost_center) and vs_code in ('89','90','91','92','93','94','95','97') ";
            } else {
                query = query + " and vs_code in (select cc_code from gl_cost_center) ";
            }
        }

        query = query + " order by orderseq";

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_FONTSIZE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'FONT_SIZE' order by orderseq").setNoNull().setLOValue("10pt");
    }

    public LOV LOV_POLATTACHED() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'POL_ATTACHED' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_MARKETINGREPORT(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_PRINTING' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_COMPANYGROUP(Map parameters) throws Exception {
        final Object search = parameters.get("search");
        final String custcat = (String) parameters.get("custcat");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("vs_code, vs_description");
        sqa.addQuery("from s_company_group");

        if (custcat != null) {
            if (Tools.isYes(custcat)) {
                sqa.addClause("coalesce(ref1,'') not in (select cc_code from gl_cost_center)");
            } else {
                sqa.addClause("coalesce(ref1,'') in (select cc_code from gl_cost_center)");
            }
        }

        if (search != null) {
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addClause("upper(vs_description) like ?");
            sqa.addPar(key);
        }

        sqa.addClause("coalesce(active_flag,'') = 'Y'");
        sqa.addOrder("vs_description");

        return sqa.getLookUp();
    }

    public LOV LOV_Marketer(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        sqa.addQuery("from ent_master");
        sqa.addSelect("*");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ent_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(ent_name||coalesce(address,'')) like ? and cc_code like ?");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addPar(cc_code);

            sqa.setLimit(50);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(ent_name||coalesce(address,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(50);
        }

        return sqa.getList(EntityView.class);
    }

    public LOV LOV_ZoneCode(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_zone_limit");
        sqa.addSelect("zone_id , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("zone_id=?");
            sqa.addPar(value);
        }
        /*
        final String poltype = (String) JSPUtil.getParameter(params, "poltype");
        if (poltype!=null) {
        sqa.addClause("poltype_id=?");
        sqa.addPar(poltype);
        }*/

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.setLimit(100);

        return sqa.getLookUp();
    }

    public LOV LOV_AccountCode(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        sqa.addSelect("*");
        sqa.addQuery("from gl_accounts ");

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause(" upper(accountno||description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.setLimit(100);
        }

        return sqa.getList(AccountView.class);
    }

    public LOV LOV_AUTHORIZED() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'POL_AUTHORIZED' order by orderseq").setNoNull().setLOValue("kasie");
    }

    public LOV LOV_TypeOfReport() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'REPORT_TYPE' order by orderseq").setNoNull().setLOValue("PDF");
    }

    public LOV LOV_User(Map parameters) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        sqa.addQuery("from s_users");
        sqa.addSelect("user_id, user_name");

        final Object value = parameters.get("value");
        if (value != null) {
            sqa.addClause("user_id=?");
            sqa.addPar(value);
        }

        final Object search = parameters.get("search");
        if (search != null) {
            sqa.addClause("upper(user_id) like ? or upper(user_name) like ?");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addPar(key);
            sqa.setLimit(10);
        }

        return sqa.getList(UserSessionView.class);

    }

    public LOV LOV_TreatyType2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_treaty_type_id,treaty_type_name from ins_treaty_types where ins_treaty_type_id not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR') order by ins_treaty_type_id");
    }

    public LOV LOV_TypeOfSipanda(Map parameters) throws Exception {
        //String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("policy.stCostCenterCodeSource");

        final Object search = parameters.get("search");

        String query = "";

        if (cc_code != null) {
            if (cc_code.equalsIgnoreCase("00")||cc_code.equalsIgnoreCase("80")) {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_SIPANDA'";
            } else {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_SIPANDA' and ref1 like '%" + cc_code + "%'";
            }
        } else if (cc_code == null) {
            query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_SIPANDA'";
        }

        if (search != null) {
            query = query + " and upper(vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }


        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_ParentPolicy(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        final Object cc_code = params.get("cc_code");
        final Object value = params.get("value");
        final Object search = params.get("search");

        sqa.addQuery("from ins_policy");
        sqa.addSelect("pol_id, pol_no");

        sqa.addClause(" status = ?");
        sqa.addPar("PARENT");

        //sqa.addClause("cc_code = ?");
        sqa.addClause("(cc_code = ? or substr(pol_no,5,2) = ?)");
        sqa.addPar(cc_code);
        sqa.addPar(cc_code);

        if (value == null && search == null) {
            sqa.setLimit(0);
        }

        if (value != null) {
            sqa.addClause("pol_id = ?");
            sqa.addPar(value);
            sqa.setLimit(1);
        }

        if (search != null) {
            sqa.addClause("pol_no like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.setLimit(15);
        }

        return sqa.getList(InsurancePolicyParentView.class);
    }

    public LOV LOV_PeriodUnit() throws Exception {
        return FinCodec.PeriodUnit.getLookUp();
    }

    public LOV LOV_Coverage2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_cover_id, description from ins_cover order by ins_cover_id");
    }

    public LOV LOV_TypeOfCoinsRate(Map parameters) throws Exception {

        final Object search = parameters.get("search");

        String query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI'";

        if (search != null) {
            query = query + " and upper(vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }

        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_KreasiData() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_KREASI'").setNoNull().setLOValue("1");
    }

    public LOV LOV_RiskCategory2(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_code , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ins_risk_cat_code=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(100);
        }

        sqa.addGroup("ins_risk_cat_code , description");

        return sqa.getLookUp();
    }

    public LOV LOV_TemporaryPolicy(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        final Object cc_code = params.get("cc_code");
        final Object value = params.get("value");
        final Object search = params.get("search");

        if (value != null || search != null) {
            sqa.addQuery("from ins_policy");
            sqa.addSelect("pol_id, pol_no");

            sqa.addClause(" status in (?,?)");
            sqa.addPar("TEMPORARY");
            sqa.addPar("ENDORSE TEMPORARY");

            sqa.addClause("cc_code = ?");
            sqa.addPar(cc_code);
        }

        if (value == null && search == null) {
            sqa.addQuery("from ins_policy");
            sqa.addSelect("pol_id");
            sqa.setLimit(0);
        }

        if (value != null) {
            sqa.addClause("pol_id = ?");
            sqa.addPar(value);
            sqa.setLimit(1);
        }

        if (search != null) {
            sqa.addClause("pol_no like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("ref9 is null");
            sqa.setLimit(15);
        }

        return sqa.getList(InsurancePolicyParentView.class);
    }

    public LOV LOV_RekapNo(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("ref3,ref3||' '||coinsurer_name");
        sqa.addQuery("from ins_policy");


        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ref3 = ?");
            sqa.addPar(value);
        }


        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("ref3 like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addClause("ref3 is not null");

        sqa.addGroup("ref3,coinsurer_name");

        sqa.setLimit(10);

        return sqa.getLookUp();
        //return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_INWARDREPORT(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_INWARD' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_EndorseMode() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'ENDORSE_MODE' and active_flag = 'Y' order by orderseq");
    }

    public LOV LOV_Settlement() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ar_settlement_id,description from ar_settlement order by ar_settlement_id");
    }

    public LOV LOV_JOURNALREPORT(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_JOURNAL' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_PolicyTypeSome(Map parameters) throws Exception {
        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        if (polgroup == null) {
            return LOV_PolicyTypeSgl();
        }

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description from ins_policy_types where ins_policy_type_grp_id=? and ref1='Y' ORDER BY POL_TYPE_ID",
                new Object[]{polgroup});
    }

    public LOV LOV_PolicyTypeGroupSome() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_policy_type_grp_id,group_name from ins_policy_type_grp where cat = 1 and ref1='Y' order by ins_policy_type_grp_id");
    }

    public LOV LOV_Division() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='DIVISION' order by orderseq");
    }

    public LOV LOV_ClaimInwardTreatyDLAStatus() throws Exception {
        return FinCodec.ClaimInwardTreatyDLAStatus.getLookUp();
    }

    public LOV LOV_TIME() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'POL_TIME' order by orderseq");
    }

    public LOV LOV_FinanceEntity(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object grup = params.get("grup");

        sqa.addSelect("a.*,b.vs_description as customer_status");
        sqa.addQuery("from ent_master a "
                + " inner join bussines_source b on a.category1 = b.vs_code");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ent_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(a.ent_name||coalesce(a.address,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(a.ent_name||coalesce(a.address,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        if (grup != null) {
            sqa.addClause("a.ref1 = ? ");
            final String key = ((String) grup).toUpperCase();
            sqa.addPar(key);
        }

        sqa.addClause("a.finance_flag=?");
        sqa.addPar("Y");

        sqa.setLimit(100);

        return sqa.getList(EntityView.class);
    }

    public LOV LOV_TaxType(Map Params) throws Exception {
        final String exclTaxBengkel = (String) Params.get("exclTaxBengkel");

        String sql = "select vs_code,vs_description from s_valueset where vs_group = 'TAX_TYPE' ";
        if (Tools.isYes(exclTaxBengkel)) {
            sql = sql + " and vs_code in ('1','2') ";
        }
        sql = sql + "  order by vs_code ";

        return ListUtil.getLookUpFromQuery(sql);
    }

    public LOV LOV_Account(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final Object cabang = params.get("cc_code");

        sqa.addQuery("from gl_accounts");
        sqa.addSelect("account_id,accountno,description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("account_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cabang != null) {

            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.addClause("cc_code in (?, '80') ");
            sqa.addPar(cabang);

        } else if (search != null && cabang == null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addClause("substr(accountno,0,6) not in "
                + " (select accountno "
                + " from "
                + " gl_accounts_insurance where active_flag = 'Y')");

        sqa.addOrder("accountno");
        sqa.setLimit(100);

        return sqa.getList(AccountView2.class);
    }

    public LOV LOV_AccountInvestment(Map params) throws Exception {

        final String param = (String) params.get("param");
        final String param2[] = param.split("[\\|]");
        String akun = param2[0];
        String koda = param2[1];
        String type = param2[2];

        /*if (type.equalsIgnoreCase("90")
        | type.equalsIgnoreCase("91")
        | type.equalsIgnoreCase("92")
        | type.equalsIgnoreCase("93")
        | type.equalsIgnoreCase("94")
        | type.equalsIgnoreCase("95")
        | type.equalsIgnoreCase("96")) {
        if (akun.equalsIgnoreCase("111")) {
        akun = "1112";
        } else if (akun.equalsIgnoreCase("122")) {
        akun = "1222";
        }
        } else {
        if (akun.equalsIgnoreCase("111")) {
        akun = "11121";
        } else if (akun.equalsIgnoreCase("122")) {
        akun = "12210";
        }
        }*/

        if (akun.equalsIgnoreCase("111")) {
            akun = "111";
        } else if (akun.equalsIgnoreCase("122")) {
            akun = "122";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("account_id,accountno,description,noper,rekno");
        sqa.addQuery("from gl_accounts");
        sqa.addClause("acctype is null and coalesce(deleted,'') <> 'Y'");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("account_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

//        if (depo!=null) {
//            sqa.addClause("accountno like ? and cc_code = ?");
//            sqa.addPar(depo);
//        } else if (bank!=null) {
//            sqa.addClause("accountno like '122%' and cc_code = ?");
//            sqa.addPar(bank);
//        }

        sqa.addClause("accountno like ? and cc_code = ?");
        //sqa.addPar(akun + type + "%");
        sqa.addPar(akun + "%");
        sqa.addPar(koda);

        sqa.addOrder("accountno");
        sqa.setLimit(100);

        return sqa.getList(AccountView2.class);
    }

    public LOV LOV_TitipanPremi2(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");

        sqa.addQuery(" from ar_titipan_premi a");
        sqa.addSelect(" trx_id, trx_no,counter, pol_no,description,"
                + " amount,"
                + "  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "
                + "   from ar_receipt y "
                + "   inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "
                + "   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as terpakai");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("trx_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {

            sqa.addClause("(upper(trx_no) like ? or upper(description) like ? or upper(pol_no) like ?)");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");

            sqa.addClause("coalesce(active_flag,'') <> 'N'");
            sqa.addClause("approved = 'Y'");
        }

        if (cabang != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(cabang);
        }

        if (value == null) {
            sqa.addClause("balance <> 0");
        }

        sqa.addOrder("trx_no,counter");

        sqa.setLimit(2000);

        String sql = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah "
                + " from (" + sqa.getSQL() + ") zz ";

        if (value == null) {
            sql = sql + " where (amount - terpakai) <> 0";
        }



        //return sqa.getList(TitipanPremiView.class);

        return ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                TitipanPremiView.class);
    }

    public LOV LOV_Wilayah() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ?",
                new Object[]{"WILAYAH"});
    }

    public LOV LOV_ExcessAccount(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");

        sqa.addQuery("from gl_accounts");
        sqa.addSelect("account_id,accountno,description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("account_id=?");
            sqa.addPar(value);
        }


        if (cabang != null) {
            sqa.addClause("substr(accountno,14,2) = ? ");
            sqa.addPar(cabang);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("(accountno like ? or coalesce(description,'') like ? )");
            final String key = ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            final String key2 = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key2);

            //sqa.addClause("enabled = 'N'");
            //sqa.addClause("cc_code = '00'");

        }


        sqa.addOrder("accountno");
        sqa.setLimit(100);

        return sqa.getList(AccountView2.class);
    }

    public LOV LOV_ClaimItemAdd() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ?",
                new Object[]{"ITEM_CLAIM"});
    }

    public LOV LOV_RekapKreasi(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.no_surat_hutang,b.ent_name as attr_pol_name,b.ent_id");
        sqa.addQuery("from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id ");

        sqa.addClause("a.attr_pol_type_id = 21");
        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.refid0 like 'CO/%'");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.no_surat_hutang=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            //sqa.addClause("upper(a.no_surat_hutang||b.ent_name) = ? ");
            //final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addClause("a.no_surat_hutang = ? ");
            final String key = ((String) search).toUpperCase();
            sqa.addPar(key);

            sqa.setLimit(10);
        } else {
            sqa.setLimit(0);
        }

        sqa.addGroup("a.no_surat_hutang,b.ent_id,b.ent_name order by b.ent_id,b.ent_name,a.no_surat_hutang");

        return sqa.getList(ARInvoiceView.class);
    }

    public LOV LOV_RECEIPTTYPE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'RECEIPT_TYPE' order by orderseq");
    }

    public LOV LOV_NR() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'FIN_NR' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_LR() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ? order by orderseq",
                new Object[]{"FIN_LR"});
    }

    public LOV LOV_KASBANK() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'FIN_KAS' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_RekapReins(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("b.ent_id,b.ent_name as attr_pol_name,a.no_surat_hutang");
        sqa.addQuery("from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.refid0 like 'REINS/%'");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.no_surat_hutang=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.no_surat_hutang||b.ent_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(100);
        }

        sqa.addGroup("a.no_surat_hutang,b.ent_id,b.ent_name");

        return sqa.getList(ARInvoiceView.class);
    }

    public LOV LOV_AAUIGROUP() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_policy_type_grp_id,group_name from ins_policy_type_grp where cat = ? order by ins_policy_type_grp_id",
                new Object[]{"2"});
    }

    public LOV LOV_REGISTERSTATUS() throws Exception {
        return FinCodec.RegisterStatus.getLookUp();
    }

    public LOV LOV_RiskCategory3(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_id , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ins_risk_cat_id=?");
            sqa.addPar(value);
        }

        String poltype = (String) JSPUtil.getParameter(params, "poltype");

        if (poltype != null) {
            sqa.addClause("poltype_id=?");
            sqa.addPar(poltype);
        }

        sqa.setLimit(100);

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("active_flag = 'Y'");

        return sqa.getLookUp();
    }

    public LOV LOV_CLMINWARD_PRINTING(Map parameters) throws Exception {

        String vs = (String) parameters.get("vs");

        if (vs == null) {
            vs = "";
        } else {
            vs = "_" + vs;
        }

        vs = "CLMINWARD_PRINTING" + vs;

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;
    }

    public LOV LOV_DepositoCall() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ? order by vs_code",
                new Object[]{"DEP_CALL"});
    }

    public LOV LOV_GL_Years2() {
        return GLCodes.Years.getLookUp();
    }

    public LOV LOV_OnCall() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'DEP_CALL' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_Deposito(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        sqa.addSelect("a.ar_depo_id,a.nodefo,b.accountno,b.description ");

        sqa.addQuery(" from ar_inv_deposito a "
                + "inner join gl_accounts b on b.account_id = a.norekdep");

        sqa.addClause(" deleted is null ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_depo_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(a.nodefo||coalesce(b.description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.koda = ? ");
            sqa.addPar(cc_code);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(a.nodefo||coalesce(b.description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        //sqa.addClause("(accountno like ('122%') or accountno like ('121%'))");
        sqa.addOrder("a.nodefo");
        sqa.setLimit(100);

        return sqa.getList(ARInvestmentDepositoView.class);
    }

    public LOV LOV_Division2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ? order by orderseq",
                new Object[]{"DIVISION2"});
    }

    public LOV LOV_DEPOSITOREPORT(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_DEPOSITO' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_DEPOSITO() throws Exception {
        return FinCodec.Deposito.getLookUp();
    }

    public LOV LOV_PolicyTypeInput(Map parameters) throws Exception {
        final String cc_code = (String) JSPUtil.getParameter(parameters, "cc_code");

        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        if (polgroup == null) {
            return LOV_PolicyTypeSgl();
        }

        if (cc_code == null) {
            return LOV_PolicyTypeSgl();
        }

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description from ins_policy_types where ins_policy_type_grp_id=? and pol_type_id in "
                + " (select pol_type_id "
                + " from ins_policy_types_branch "
                + " where cc_code = ?) ORDER BY POL_TYPE_ID",
                new Object[]{polgroup, cc_code});
    }

    public LOV LOV_TypeOfCredit2(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");
        final String cc_code2 = (String) parameters.get("policy.stCostCenterCode");

        final Object search = parameters.get("search");

        String query = "";

        if (cc_code != null) {
            if (cc_code.equalsIgnoreCase("00")) {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT'";
            } else {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT' and ref1 = '" + cc_code + "'";
            }
        } else if (cc_code == null) {
            if (cc_code2 != null) {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT' and ref1 like '%" + cc_code2 + "%'";
            } else {
                query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_KREASI_KREDIT'";
            }
        }

        query = query + " and coalesce(ref3,'') <> 'N'";

        if (search != null) {
            query = query + " and upper(vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }

        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_XOLType() throws Exception {
        return FinCodec.XOLType.getLookUp();
    }

    public LOV LOV_XOLLayer() throws Exception {
        return FinCodec.XOLLayer.getLookUp();
    }

    public LOV LOV_Policy(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");

        final Object search = parameters.get("search");

        String query = "select pol_id,pol_no,effective_flag from ins_policy";

        if (search != null) {
            query = query + " where upper(pol_no) like '%" + ((String) search).toUpperCase() + "%'";
        }


        query = query + " limit 100";

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_Entity(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = "null";
        
        if(SessionManager.getInstance().getSession()!=null)
            cc_code = SessionManager.getInstance().getSession().getStBranch();

        final Object search = params.get("search");

        String cabang = cc_code;
        if (cabang == null || cabang.equalsIgnoreCase("null")) {
            cabang = "a.cc_code";
        }

        final Object grup = params.get("grup");
        final Object module = params.get("module");

        String month = (String) params.get("month");
        
        String year = (String) params.get("year");

        if (month == null || month.equalsIgnoreCase("null")) {
            month = String.valueOf(DateUtil.getMonthDigit(new Date()) - 1);
        } else {
            month = String.valueOf(Integer.parseInt(month) - 1);
        }

        if (year == null || year.equalsIgnoreCase("null")) {
            year = DateUtil.getYear(new Date());
        }

        final String saldo = GLUtil.getPeriodList(0, Long.parseLong(month), "bal", "+");

        if(search != null){
            sqa.addSelect("a.ent_id,a.ent_name,a.cc_code as cabang,a.address,a.gl_code,a.rekno,a.customer_status,a.tax_file,a.functionary_name,a.functionary_position,a.rc_no,"
                + " b.vs_description as customer_category,"
                + " trim(to_char((select round(sum(" + saldo + "),2)"
                + "    from gl_acct_bal2 x "
                + "    inner join gl_accounts y on x.account_id = y.account_id "
                + "    where x.idr_flag = 'Y' "
                + "    and x.period_year = " + year + " and substr(y.accountno, 6, 5) = a.gl_code "
                + "    and substr(y.accountno,0,4) =  '122' and a.gl_code <> '00000' and y.accountno like '% '|| " + cabang + " || ''  LIMIT 1), '999G999G999G999G999G999D99')) as saldo");

        }else{
            sqa.addSelect("a.ent_id,a.ent_name,a.cc_code as cabang,a.address,a.gl_code,a.rekno,a.customer_status,a.tax_file,a.functionary_name,a.functionary_position,a.rc_no,"
                + " b.vs_description as customer_category");
        }
        
        sqa.addQuery("from ent_master a "
                + " inner join bussines_source b on a.category1 = b.vs_code");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ent_id=?");
            sqa.addPar(value);
        }

        
        if (search != null && cc_code != null) {
            sqa.addClause("(upper(a.ent_name) like ? or upper(a.gl_code) like ? ) ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key2);

            sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");

        } else if (search != null && cc_code == null) {
            sqa.addClause("(upper(a.ent_name) like ? or upper(a.gl_code) like ? ) ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key2);

            sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");
        }

        if (grup != null) {
            sqa.addClause("a.ref1 = ? ");
            final String key = ((String) grup).toUpperCase();
            sqa.addPar(key);
        }

        if (module != null) {
            if (((String) module).equalsIgnoreCase("FINANCE")) {
                sqa.addClause("a.rekno is not null");
            }
        }

        //sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");

        sqa.addOrder("a.ent_id desc");

        sqa.setLimit(50);

        return sqa.getList(EntityView.class);
    }

    public LOV LOV_TitipanPremiMinus(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");

        sqa.addQuery(" from ar_titipan_premi a");
        sqa.addSelect(" trx_id, trx_no,counter, pol_no,description,"
                + " amount,"
                + "  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "
                + "   from ar_receipt y "
                + "   inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "
                + "   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as terpakai");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("trx_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {

            sqa.addClause("(upper(trx_no) like ? or upper(description) like ? or upper(pol_no) like ?)");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");

            sqa.addClause("coalesce(active_flag,'') <> 'N'");
            sqa.addClause("approved = 'Y'");
            sqa.addClause("balance < 0");

        }

        if (cabang != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(cabang);
        }

        if (value == null) {
            sqa.addClause("balance <> 0");
        }

        sqa.setLimit(2000);

        sqa.addOrder("trx_no,counter");

        String sql = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah "
                + " from (" + sqa.getSQL() + ") zz ";

        if (value == null) {
            sql = sql + " where (amount - terpakai) <> 0";
        }

        //return sqa.getList(TitipanPremiView.class);

        return ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                TitipanPremiView.class);

        //return sqa.getList(TitipanPremiView.class);
    }

    public LOV LOV_TitipanCause() throws Exception {
        return FinCodec.TitipanCause.getLookUp();
    }

    public LOV LOV_ReceiptCashBank() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select method_code,description from receipt_class where cash_bank_flag = 'Y'",
                ARReceiptClassView.class);
    }

    public LOV LOV_PolicyType3(Map parameters) throws Exception {

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description2 from ins_policy_types ORDER BY POL_TYPE_ID");
    }

    public LOV LOV_CURRENCY2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'CURRENCY' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("IDR");
    }

    public LOV LOV_PLTYPE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'PL_TYPE' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("PL2");
    }

    public LOV LOV_JournalType() throws Exception {
        return FinCodec.JournalType.getLookUp();
    }

    public LOV LOV_Download() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where ref1='DOWNLOAD' order by orderseq asc");
    }

    public LOV LOV_INVPENCAIRAN() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'PENCAIRAN' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("1");
    }

    public LOV LOV_DEPOSITO2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'DEPOSITO' and active_flag = 'Y' order by vs_code");
    }

    public LOV LOV_EntityFinance(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        String cabang = cc_code;
        if (cabang == null || cabang.equalsIgnoreCase("null")) {
            cabang = "a.cc_code";
        }

        final Object grup = params.get("grup");
        final Object module = params.get("module");
        final String cabang2 = (String) params.get("cc_code");

        String par1 = (String) params.get("month");

        String par[] = par1.split("[\\|]");
        String month = "null";
        String year = "null";

        if (!par[0].equalsIgnoreCase("null") && !par[1].equalsIgnoreCase("null")) {
            month = par[0];
            year = par[1];
        }

        if (month == null || month.equalsIgnoreCase("null")) {
            month = String.valueOf(DateUtil.getMonthDigit(new Date()) - 1);
        } else {
            month = String.valueOf(Integer.parseInt(month) - 1);
        }

        if (year == null || year.equalsIgnoreCase("null")) {
            year = DateUtil.getYear(new Date());
        }

        final String saldo = GLUtil.getPeriodList(0, Long.parseLong(month), "bal", "+");


        sqa.addSelect("a.*,c.rekno as rekening,c.bal_open,b.vs_description as customer_category,"
                + " (select round(sum(" + saldo + "),2)"
                + "    from gl_acct_bal2 x "
                + "    inner join gl_accounts y on x.account_id = y.account_id "
                + "    where x.idr_flag = 'Y' "
                + "    and x.period_year = " + year + " and y.account_id = c.account_id "
                + " LIMIT 1) as saldo_num");

        sqa.addQuery("from ent_master a "
                + " inner join bussines_source b on a.category1 = b.vs_code"
                + " inner join gl_accounts c on a.gl_code = substr(c.accountno,6,5)");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ent_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("(upper(a.ent_name) like ? or upper(a.gl_code) like ? ) ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key2);

        } else if (search != null && cc_code == null) {
            sqa.addClause("(upper(a.ent_name) like ? or upper(a.gl_code) like ? ) ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key2);
        }

        if (grup != null) {
            sqa.addClause("a.ref1 = ? ");
            final String key = ((String) grup).toUpperCase();
            sqa.addPar(key);
        }

        //sqa.addClause("a.rekno is not null");
        sqa.addClause("a.gl_code <> '00000'");
        sqa.addClause("substr(c.accountno,0,4) =  '122'");
        sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");
        sqa.addClause("c.accountno like '% '|| '" + cabang2 + "' || ''");

        sqa.addOrder("a.ent_id desc");

        sqa.setLimit(50);

        String sql = "select *,trim(to_char(saldo_num, '999G999G999G999G999G999D99')) as saldo"
                + " from (" + sqa.getSQL() + ") z";

        if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
            sql = sql + " where (saldo_num::numeric > 0 or bal_open > 0 )";
        }

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                EntityView.class);

        return l;
        //return sqa.getList(EntityView.class);
    }

    public LOV LOV_InsuranceItem2() throws Exception {

        return ListUtil.getLookUpFromQuery(
                "select item_group,description "
                + " from ins_items where item_type = 'COMIS' "
                + " group by item_group,description");
    }

    public LOV LOV_Triwulan() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='TRIWULAN' order by orderseq asc");
    }

    public LOV LOV_TITIPAN() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'TITIPAN' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("1");
    }

    public LOV LOV_PRT_GLJE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'PRT_GLJE' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("gldetail");
    }

    public LOV LOV_PRT_CASHBANK() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'PRT_CASHBANK' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("cbdetail");
    }

    public LOV LOV_FIN_NEW() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'FIN_NEW' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_JOURNAL_DESC() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='JOURNAL_DESC' order by orderseq ");
    }

    public LOV LOV_POL_CREDIT() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='POL_CREDIT' order by orderseq ");
    }

    public LOV LOV_CREDITLIST() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='CREDITLIST' order by vs_code asc");
    }

    public LOV LOV_BungaDeposito(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final Object cabang = params.get("cc_code");

        sqa.addSelect(" ar_bunga_id,nodefo,bukti_b,tglakhir::character varying as tglmutasi,trim(to_char(coalesce(angka1,0),'999G999G999G999G999G999D99'))::character varying as jumlah,bukti_d ");
        sqa.addQuery(" from ar_inv_bunga a ");
        sqa.addClause(" delete_flag is null ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ar_bunga_id = ?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause(" a.nodefo like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        if (cabang != null) {
            sqa.addClause("a.koda = ? ");
            sqa.addPar(cabang);
        }

        sqa.addOrder(" tglakhir desc ");
        sqa.setLimit(30);

        return sqa.getList(ARInvestmentBungaView.class);
        //return sqa.getLookUp();
    }

    public LOV LOV_RiskCategoryFire(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_code , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ins_risk_cat_code=?");
            sqa.addPar(value);
        }

        sqa.addClause("poltype_id = 1");

        sqa.addClause("active_flag = 'Y'");

        sqa.setLimit(100);

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("active_flag = 'Y'");

        return sqa.getLookUp();
    }

    public LOV LOV_KreasiKredit() throws Exception {
        return ListUtil.getLookUpFromQuery("select vs_code, vs_description from s_valueset where vs_group='KREASI_KREDIT'");
    }

    public LOV LOV_KreasiTanggalTaon() throws Exception {
        return ListUtil.getLookUpFromQuery("select vs_code, vs_description from s_valueset where vs_group='KREASI_TGL_THN'");
    }

    public LOV LOV_VALIDASI() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'VALIDASI' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("1");
    }

    public LOV LOV_BiaopGroup(Map parameters) throws Exception {
        final String groupbiaop = (String) JSPUtil.getParameter(parameters, "groupbiaop");

        return ListUtil.getLookUpFromQuery(
                "select biaop_grp_id,description from s_biaop_group where group_desc = ? order by biaop_grp_id",
                new Object[]{groupbiaop});
    }

    public LOV LOV_BiaopType(Map parameters) throws Exception {
        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        return ListUtil.getLookUpFromQuery(
                "select biaop_dtl_id,description from s_biaop_detail where biaop_grp_id = ? order by biaop_dtl_id",
                new Object[]{polgroup});
    }

    public LOV LOV_PRT_SYGE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'PRT_SYGE' and active_flag = 'Y' order by vs_code").setNoNull().setLOValue("sydetail");
    }

    public LOV LOV_NR_BRANCH() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'FIN_NR' and ref2 = 'branch' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_PostalCode_Maipark(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_region_map_maipark");
        sqa.addSelect("region_map_id,postal_code, region_name,city_name,region_map_desc,building_desc");

        final Object street = params.get("street");
        final Object value = params.get("value");
        final Object kabupaten = params.get("kabupaten2");

        if (street == null && value == null) {
            return new LookUpUtil();
        }

        if (value != null) {
            sqa.addClause("region_map_id=?");
            sqa.addPar(value);
        }

        sqa.setLimit(50);

        if (kabupaten != null) {
            sqa.addClause("upper(ref1) like ? ");
            final String key = "%" + ((String) kabupaten).toUpperCase() + "%";
            sqa.addPar(key);
        }

        if (street != null) {
            sqa.addClause("(upper(region_name) like ? or upper(city_name) like ? or postal_code like ? or region_map_desc like ? or building_desc like ?)");
            final String key = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key);

            final String key2 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key2);

            final String key3 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key3);

            final String key4 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key4);

            final String key5 = "%" + ((String) street).toUpperCase() + "%";
            sqa.addPar(key5);

            sqa.addClause("active_flag = 'Y'");

        }

        return sqa.getList(PostalCodeMaiparkView.class);
    }

    public LOV LOV_RKAPREPORT(Map parameters) throws Exception {
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_RKAP' and division = ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});
    }

    public LOV LOV_Prod_OJK() throws Exception {
        return ListUtil.getLookUpFromQuery("select ojk_id,description from s_report_ojk order by ojk_id ");
    }

    public LOV LOV_VEHICLE_USAGE(Map params) throws Exception {

        final Object search = params.get("search");

        String query = "";

        query = "select vs_code,vs_description from s_valueset where vs_group='INSOBJ_VEH_USAGE'";

        if (search != null) {
            query = query + " and active_flag ='Y' and upper(vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }

        query = query + " order by vs_code::int";

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_RiskCategoryCode(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_risk_cat");
        sqa.addSelect("ins_risk_cat_code , description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("ins_risk_cat_code=?");
            sqa.addPar(value);
        }

        sqa.addClause("active_flag = 'Y'");

        sqa.setLimit(100);

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("active_flag = 'Y'");

        return sqa.getLookUp();
    }

    public LOV LOV_ManualPDFBook() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'USERMANUAL' order by vs_code asc");
    }

    public LOV LOV_ManualPDFBookDetail(Map parameters) throws Exception {
        String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        polgroup = "MANUAL_" + polgroup;

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = ? order by orderseq asc",
                new Object[]{polgroup});
    }

    public LOV LOV_AkunDesc() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'AKUN_DESC' order by vs_code asc");
    }

    public LOV LOV_GL_Years3() {
        return GLCodes.YearsForAccounting.getLookUp();
    }

    public LOV LOV_EntityLevel() throws Exception {
        return FinCodec.EntityLevel.getLookUp();
    }

    public LOV LOV_MARKETINGREPORT2(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, orderseq||'. '||vs_description as vs_description from s_valueset where vs_group='PROD_PRINTING' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_SearchKey(Map parameters) throws Exception {
        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'FILTER_SEARCHKEY' and ref1 = ? and active_flag = 'Y' order by orderseq",
                new Object[]{poltype});
    }

    public LOV LOV_NR_BOD() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = 'FIN_BOD' and ref2 = 'direksi' and active_flag = 'Y' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_CLOSING_PRINTING(Map parameters) throws Exception {
        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,vs_description,default_flag,ref2 from s_valueset where vs_group = 'CLOSING_PRINTING' and active_flag='Y' order by orderseq, vs_description",
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;
    }

    public LOV LOV_UPLOADSTATUS() throws Exception {
        return FinCodec.UploadStatus.getLookUp();
    }

    public LOV LOV_POLICY(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_id, a.pol_no, a.cust_name ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_id=?");
            sqa.addPar(value);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.pol_no||coalesce(a.cust_name,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("a.active_flag = 'Y' and coalesce(a.effective_flag,'N') <> 'Y' ");

        sqa.setLimit(20);

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_Profil2(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from s_users a ");
        sqa.addSelect("a.user_id, a.user_name, a.division, a.email_address ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.user_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.user_name) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(100);
        }

        sqa.addClause("upper(a.status) = 'APPROVAL'");

        sqa.addClause("coalesce(a.delete_flag,'N') <> 'Y'");
        sqa.addOrder("a.division");

        return sqa.getList(UserSessionView.class);
    }

    public LOV LOV_ABA_PROFILE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_PROFILE' order by vs_code ");
    }

    public LOV LOV_ABA_GROUP_JENIS() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_GROUP_JENIS' order by vs_code ");
    }

    public LOV LOV_POL_PRINT_POLICYCHECK(Map parameters) throws Exception {
//      LookUpUtil lu = (LookUpUtil) LOVManager.getInstance().getLOV("VS_POL_PRINTING",null);

        String vs = (String) parameters.get("vs");

        if (vs == null) {
            vs = "";
        } else {
            vs = "_" + vs;
        }

        vs = "" + vs;

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv.setLOValue("STANDARD");

//      return lu.setNoNull();
    }

    public LOV LOV_RKAPGroup() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_PRINTING' and division = 'rkap' and active_flag = 'Y' order by orderseq");
    }

    public LOV LOV_Account2(Map params) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        final String account = (String) JSPUtil.getParameter(params, "param");
        final String account2[] = account.split("[\\|]");
        final String cabang = account2[0];
        final String biaopType = account2[1];

        sqa.addQuery("from gl_accounts");
        sqa.addSelect("account_id,accountno,description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("account_id=?");
            sqa.addPar(value);
        }

        sqa.addClause("coalesce(deleted,'Y') <> 'N'");

        final Object search = params.get("search");
        if (search != null && cabang != null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("cc_code = ? ");
            sqa.addPar(cabang);
        } else if (search != null && cabang == null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

//        sqa.addClause("substr(accountno,0,6) not in "
//                + " (select accountno "
//                + " from "
//                + " gl_accounts_insurance where active_flag = 'Y')");

        final BiayaOperasionalDetail polType = (BiayaOperasionalDetail) DTOPool.getInstance().getDTO(BiayaOperasionalDetail.class, biaopType);

        String sql = null;
        if (biaopType != null) {
            String biaopType2[] = polType.getStAccount().split("[\\|]");
            if (biaopType2.length == 1) {
                sql = sqa.getSQL() + " and accountno like '" + biaopType2[0] + "%'";
            } else if (biaopType2.length > 1) {
                sql = sqa.getSQL() + " and (accountno like '" + biaopType2[0] + "%'";
                for (int k = 1; k < biaopType2.length; k++) {
                    sql = sql + " or accountno like '" + biaopType2[k] + "%'";
                }
                sql = sql + " ) ";
            }
        }

        sqa.addOrder("accountno");
        sqa.setLimit(100);

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                AccountView2.class);

        return l;
    }

    public LOV LOV_LIMIT_KEUANGAN() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='LIMIT_KEUANGAN' order by vs_code ").setNoNull().setLOValue("1");
    }

    public LOV LOV_CostCenter4() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select cc_code,description from gl_cost_center4 order by cc_code");
    }

    public LOV LOV_Region2(Map parameters) throws Exception {

        final String ccCode = (String) parameters.get("cc_code");
        final String value = (String) parameters.get("value");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("region_id,description");

        sqa.addQuery("from s_region");

        if (ccCode != null) {
            sqa.addClause("cc_code3 = ?");
            sqa.addPar(ccCode);
        }

        if (value != null) {
            sqa.addClause("true or region_id=?");
            sqa.addPar(value);
        }

        return sqa.getLookUp();
    }

    public LOV LOV_REQUEST_PRINTING(Map parameters) throws Exception {

        String vs = "REQUEST_PRINTING";

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;
    }

    public LOV LOV_Account3(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final Object cabang = params.get("cc_code");

        sqa.addQuery("from gl_accounts");
        sqa.addSelect("account_id,accountno,description");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("account_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cabang != null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("substr(accountno, 14, 2) = ? ");
            sqa.addPar(cabang);
        } else if (search != null && cabang == null) {
            sqa.addClause("upper(accountno||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addClause("substr(accountno,0,6) not in "
                + " (select accountno "
                + " from "
                + " gl_accounts_insurance where active_flag = 'Y')");

        sqa.addOrder("accountno");
        sqa.setLimit(100);

        return sqa.getList(AccountView2.class);
    }

    public LOV LOV_REQUESTLIST() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='REQUEST_LIST' order by vs_code ");
    }

    public LOV LOV_BiaopGroup2(Map params) throws Exception {
//        final String groupbiaop = (String) JSPUtil.getParameter(parameters, "groupbiaop");
        final String pilihan = (String) params.get("pilihan");

//        return ListUtil.getLookUpFromQuery(
//                "select biaop_grp_id,description from s_biaop_group where group_desc = ? order by biaop_grp_id",
//                new Object[]{groupbiaop});String query = null;

        String query = null;
        if ("1".equalsIgnoreCase(pilihan)) {
            query = "select group_desc,description from s_biaop_group where biaop_grp_id = 66 and active_flag = 'Y'";
        } else {
            query = "select vs_code, vs_description from s_valueset where vs_group='PROD_RKAP' and ref2 = 'Y' order by orderseq ";
        }

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_BiaopType2(Map params) throws Exception {
        final String pilihan = (String) params.get("pilihan");

//        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
//
//        return ListUtil.getLookUpFromQuery(
//                "select biaop_dtl_id,description from s_biaop_detail where biaop_grp_desc = ? order by biaop_dtl_id",
//                new Object[]{pilihan});

        String query = null;
        if ("1".equalsIgnoreCase(pilihan)) {
            query = "select biaop_dtl_id,description from s_biaop_detail where biaop_grp_id = 66 and active_flag = 'Y' order by biaop_dtl_id";
        } else {
            query = "select biaop_dtl_id,description from s_biaop_detail where biaop_grp_desc in ('rkappem','rkapum','rkapadm') and active_flag = 'Y' order by biaop_grp_desc,biaop_dtl_id";
        }

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_CLAIM(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) params.get("cc_code");

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_id, a.pol_no, a.pla_no, a.dla_no ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_id=?");
            sqa.addPar(value);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.pol_no||coalesce(a.pla_no)||coalesce(a.dla_no,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);

            sqa.setLimit(20);
        }

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause("((a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'N') "
                + " or (a.claim_status = 'PLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'))");

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_ReceiptRequest() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select method_code,description from receipt_class where request_flag = 'Y' order by rc_id ",
                ARReceiptClassView.class);
    }

    public LOV LOV_PolicyTypeSearch(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //select pol_type_id,description from ins_policy_types ORDER BY POL_TYPE_ID

        sqa.addQuery("from ins_policy_types");
        sqa.addSelect("pol_type_id,description ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("POL_TYPE_ID = ?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(POL_TYPE_ID||coalesce(description,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

            sqa.setLimit(10);
        }

        return sqa.getList(InsurancePolicyTypeView.class);
    }

    public LOV LOV_NoSuratHutang(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) params.get("cc_code");

        sqa.addSelect(" a.ins_upload_id, a.no_surat_hutang, a.data_amount, trim(to_char(amount_total, '999G999G999G999G999G999D99'))::character varying as jumlah ");

        sqa.addQuery("from ins_proposal_komisi a ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ins_upload_id = ?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.no_surat_hutang) like ?  ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addClause("a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y'");

        sqa.addClause("a.cc_code = ?");
        sqa.addPar(cabang);

        sqa.addGroup("a.ins_upload_id, a.no_surat_hutang, a.data_amount, a.amount_total");
        sqa.addOrder("a.no_surat_hutang asc");

        sqa.setLimit(50);

        String sql = sqa.getSQL();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                uploadProposalCommView.class);

        return l;
    }

    public LOV LOV_Currency3() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ccy_code,ccy_code from gl_currency order by oid");
    }

    public LOV LOV_ProposalComm(Map params) throws Exception {

        final String param = (String) params.get("param");
        final String paramString[] = param.split("[\\|]");
        String koda = paramString[0];
        String poltype = paramString[1];

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id as ent_id,a.attr_pol_no as no_surat_hutang,a.attr_pol_name,trim(to_char(a.amount,'999G999G999G999G999G999D99'))::character varying as claim_no");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40) ");

        sqa.addClause("a.no_surat_hutang is null");//PROPOSAL
        sqa.addClause("a.approved_flag is null");//PROPOSAL
        //sqa.addClause("a.surat_hutang_period_from is null");//PROPOSAL
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

        if (koda != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(koda);
        }

        if (poltype != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(poltype);
        }

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "inner join ins_policy a on a.pol_id = g.pol_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) and g.pol_id is not null ";

//        if (koda != null) {
//            premiPayment = premiPayment + " and f.cc_code = '" + koda + "'";
//        }

        if (poltype != null) {
            premiPayment = premiPayment + " and a.pol_type_id = '" + poltype + "'";
        }

//        if (per_awal != null) {
//            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + per_awal + "'";
//        }
        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '2018-01-01 00:00:00'";

//        if (per_akhir != null) {
//            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + per_akhir + "'";
//        }
        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd") + "'";

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_invoice_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("a.attr_pol_no like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addOrder("a.attr_pol_no");
        sqa.setLimit(100);

        return sqa.getList(ARInvoiceView.class);
    }

    public LOV LOV_REINS_PAYMENT() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'REINS_PAYMENT' order by vs_code ").setNoNull().setLOValue("1");
    }

    public LOV LOV_POLICY_APPROVED(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_no, a.pol_no ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_no=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.pol_no||coalesce(a.cust_name,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("a.active_flag = 'Y' and coalesce(a.effective_flag,'N') = 'Y' ");

        sqa.setLimit(20);

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_BiaopTypeSgl(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.biaop_dtl_id,a.description,a.biaop_grp_desc ");
        sqa.addQuery(" from s_biaop_detail a ");
        //sqa.addClause(" bukti_d is null ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("biaop_dtl_id = ?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause(" upper(a.description) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause(" a.rkap_group_id is not null ");

        sqa.addOrder(" biaop_grp_id,biaop_dtl_id ");
        //sqa.setLimit(50);

        return sqa.getList(BiayaOperasionalDetail.class);
        //return sqa.getLookUp();
    }

    public LOV LOV_LEVEL_RISK(Map parameters) throws Exception {
        final String poltype = (String) JSPUtil.getParameter(parameters, "poltype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code,case when kode_resiko is null then vs_description else kode_resiko end as vs_description "
                + "from ( "
                + "select vs_code,vs_description,vs_description||' - '||string_agg(ins_risk_cat_code, ',') as kode_resiko "
                + "from s_valueset a "
                + "left join ins_risk_cat b on a.vs_code = b.risk_level and b.poltype_id = ? "
                + "where vs_group = 'RISK_LEVEL'  "
                + "group by vs_code,vs_description)z",
                new Object[]{poltype});
    }

    public LOV LOV_TYPE_REQUEST() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where active_flag = 'Y' and vs_group = 'TYPE_REQUEST' order by orderseq ");
    }

    public LOV LOV_ABA_GROUP_OUTGO() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_OUTGO' order by vs_code ");
    }

    public LOV LOV_MARKETINGREPORTVLD(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, orderseq||'. '||vs_description as vs_description from s_valueset where vs_group='PROD_PRINTING_REKAP' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_JOURNALREPORTVLD(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='PROD_JOURNAL' and division like ? and ref2 = 'validation' and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_Treaty2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_treaty_id,treaty_name from ins_treaty where active_flag = 'Y' order by treaty_name,ins_treaty_id");
    }

    public LOV LOV_QUANTITY() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'QUANTITY' order by vs_code ").setNoNull().setLOValue("1");
    }

    public LOV LOV_SUBROGASI() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='SUBROGASI' order by orderseq ");
    }

    public LOV LOV_BiaopType3(Map parameters) throws Exception {
        final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");

        return ListUtil.getLookUpFromQuery(
                "select biaop_dtl_id,description from s_biaop_detail where biaop_grp_desc = ? and biapem_f = 'Y' order by biaop_dtl_id",
                new Object[]{polgroup});
    }

    public LOV LOV_LOSSPROFILE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'LOSSPROFILE' order by vs_code ");
    }

    public LOV LOV_INSTALLMENT_OPTIONS() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'INWARDOPTIONS' order by vs_code ");
    }

    public LOV LOV_APPROVAL_LEVEL() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'APPROVAL_LEVEL' order by orderseq ");
    }

    public LOV LOV_HCIS_UnitKerja2() throws Exception {
        return ListUtil.getLookUpFromQueryDS(
                "select unit_kerja_id,description from param_unit_kerja where active_flag = '1' order by unit_kerja_id", null, "DBHCIS");
    }

    public LOV LOV_HCIS_UnitKerja(Map parameters) throws Exception {

        String ccCOde = (String) parameters.get("ccCOde");
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + ccCOde);

        String sql = "select unit_kerja_id,description from param_unit_kerja where active_flag = '1' and segmen_id <> 2 ";
        if (ccCOde != null) {
            if (ccCOde.equalsIgnoreCase("00")) {
                ccCOde = "99";
            }
            sql = sql + " and cc_code = '" + ccCOde + "' order by description";
        } else {
            sql = sql + " order by description";
        }

        return ListUtil.getLookUpFromQueryDS(sql,"DBHCIS");

    }

    public LOV LOV_HCIS_Jabatan(Map parameters) throws Exception {

        final String unitKerja = (String) parameters.get("unitKerja");
        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@ " + unitKerja);

        return ListUtil.getLookUpFromQueryDS(
                "select organization_id,description from param_organization where active_flag = '1' and unit_kerja_id = ? order by description ",
                new Object[]{unitKerja}, "DBHCIS");

    }

    public LOV LOV_PolicyTypeInward(Map parameters) throws Exception {

        return ListUtil.getLookUpFromQuery(
                "select pol_type_id,description from ins_policy_types_inward ORDER BY POL_TYPE_ID");
    }


public LOV LOV_GLClosing(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.closing_id,a.no_surat_hutang ");
        sqa.addQuery("from ins_gl_closing a ");

        sqa.addClause("a.closing_type in ('CLAIM_RI_OUTWARD','CLAIM_RI_INWARD','PREMIUM_RI_OUTWARD','PREMIUM_RI_INWARD')");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.no_surat_hutang=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.no_surat_hutang) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addOrder("a.closing_id desc");
        sqa.setLimit(10);

        return sqa.getList(InsuranceClosingView.class);
    }

public LOV LOV_Perpanjangan(Map params) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = (String) params.get("param");

        sqa.addSelect("a.ar_depo_id,a.bukti_b,a.nodefo,"
                /*+ "a.norekdep,a.account_depo,"
                + "a.kdbank,a.account_bank,a.kodedepo,a.register,"
                + "a.tglawal,a.tglakhir,a.comp_type,a.rc_id,a.hari,a.bulan,"*/
                + "trim(to_char(coalesce(a.bunga,0),'99999.99'))::character varying as persen,"
                + "trim(to_char(coalesce(a.nominal,0),'FM999999999999999999'))::character varying as jumlah");

        sqa.addQuery(" from ar_inv_perpanjangan a ");

        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y' and a.deleted is null ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_depo_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(a.nodefo||coalesce(a.bukti_b,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.koda = ? ");
            sqa.addPar(cc_code);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(a.nodefo||coalesce(a.bukti_b,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addOrder("a.ar_depo_id desc ");
        sqa.setLimit(100);

        return sqa.getList(ARInvestmentPerpanjanganView.class);
    }

     public LOV LOV_CLAIMTOLAK(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String param = (String) params.get("param");
        final String param2[] = param.split("[\\|]");
        String cabang = param2[0];
        String poltype = param2[1];

//        final String cabang = (String) JSPUtil.getParameter(params, "param");

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_id, a.cust_name, a.dla_no, a.pol_no, a.pla_no ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_id=?");
            sqa.addPar(value);

            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
            sqa.addClause("a.pol_type_id = ? ");
            sqa.addPar(poltype);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.pol_no||coalesce(a.pla_no,'')||coalesce(a.dla_no,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
            sqa.addClause("a.pol_type_id = ? ");
            sqa.addPar(poltype);

            sqa.setLimit(20);
        }

        sqa.addClause("a.status = 'CLAIM' ");
        sqa.addClause("a.claim_status = 'DLA' and a.active_flag = 'N' and a.effective_flag = 'N'");

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_CLAIMEXGRATIAPMS(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String param = (String) params.get("param");
        final String param2[] = param.split("[\\|]");
        String cabang = param2[0];
        String poltype = param2[1];

//        final String cabang = (String) JSPUtil.getParameter(params, "param");

        sqa.addQuery("from ins_pol_inward a ");
        sqa.addSelect("a.attr_pol_id, a.dla_no, a.attr_pol_no, a.pla_no ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_invoice_id=?");
            sqa.addPar(value);

            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
            sqa.addClause("a.attr_pol_type_id = ? ");
            sqa.addPar(poltype);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.attr_pol_no||coalesce(a.pla_no,'')||coalesce(a.dla_no,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
            sqa.addClause("a.attr_pol_type_id = ? ");
            sqa.addPar(poltype);

            sqa.setLimit(20);
        }

        sqa.addClause("a.ar_trx_type_id = 24 ");
        sqa.addClause("a.approved_flag = 'Y'");
        return sqa.getList(InsurancePolicyInwardView.class);
    }

    public LOV LOV_DEPO_PRINTING(Map parameters) throws Exception {

        String vs = (String) parameters.get("vs");

        if (vs == null) {
            vs = "";
        } else {
            vs = "_" + vs;
        }

        vs = "DEPO_PRINTING" + vs;

        DTOList lv = ListUtil.getDTOListFromQuery(
                "select vs_code,orderseq||'. '||vs_description as vs_description,default_flag,ref2 from s_valueset where vs_group=? and active_flag='Y' order by orderseq, vs_description",
                new Object[]{vs},
                PrintingCodesView.class);

        lv.setNoNull();

        for (int i = 0; i < lv.size(); i++) {
            PrintingCodesView pr = (PrintingCodesView) lv.get(i);

            if ("Y".equalsIgnoreCase(pr.getStDefaultFlag())) {
                lv.setLOValue(pr.getStVSCode());
                break;
            }
        }

        return lv;
    }

    public LOV LOV_DepositoIzin(Map params) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String cc_code = (String) params.get("param");

        sqa.addSelect("a.ar_depo_id,a.bukti_b,a.nodefo, trim(to_char(coalesce(a.nominal,0),'FM999999999999999999'))::character varying as jumlah");

        sqa.addQuery(" from ar_inv_perpanjangan a ");

        sqa.addClause(" a.active_flag = 'Y' and a.effective_flag = 'Y' and a.deleted is null and a.ar_cair_id is null ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_depo_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null && cc_code != null) {
            sqa.addClause("upper(a.nodefo||coalesce(a.bukti_b,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
            sqa.addClause("a.koda = ? ");
            sqa.addPar(cc_code);
        } else if (search != null && cc_code == null) {
            sqa.addClause("upper(a.nodefo||coalesce(a.bukti_b,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);
        }

        sqa.addOrder("a.ar_depo_id desc ");
        sqa.setLimit(100);

        return sqa.getList(ARInvestmentDepositoView.class);
    }

     public LOV LOV_CLAIM2(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) JSPUtil.getParameter(params, "param");

//        sqa.addQuery("from ins_policy a ");
//        sqa.addSelect("a.pol_id, a.pol_no, a.pla_no, a.dla_no,  trim(to_char(coalesce(a.claim_amount,0),'FM999999999999999999'))::character varying as jumlah ");

//        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE') ");
//        sqa.addClause("a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y' ");

        sqa.addQuery("from ar_invoice a ");
        sqa.addSelect("a.ar_invoice_id as pol_id, a.attr_pol_no as pol_no,a.attr_pol_name as cust_name, "
                + "a.refid1 as pla_no, a.refid2 as dla_no,  trim(to_char(coalesce(a.amount,0),'FM999999999999999999'))::character varying as jumlahklaim ");

        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 12");
        sqa.addClause("a.amount_settled is null");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_invoice_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("a.refid2 like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
//            final String key = ((String) search).toUpperCase();
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
        }
        if (search == null) {
            sqa.addClause("a.refid2 = 'LKP/59/10/1121/0019' ");
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
        }

        sqa.addOrder("a.refid2");
        sqa.setLimit(10);

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_IZIN_PENCAIRAN() throws Exception {
        return FinCodec.IzinPencairan.getLookUp().setNoNull().setLOValue("1");
    }

    public LOV LOV_DEPOSUMBER() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'DEPO_SUMBER' order by orderseq");
    }

    public LOV LOV_DEPOTUJUAN() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'DEPO_TUJUAN' order by orderseq");
    }

    public LOV LOV_DEPOSITO_UNIT() throws Exception {
        return FinCodec.DepositoUnit.getLookUp();
    }


    public LOV LOV_POSISIBILYET() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'POSISIBILYET' order by orderseq").setNoNull().setLOValue("1");
    }

    public LOV LOV_ABA_GROUP_KREDIT() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_GROUP_KREDIT' order by orderseq ");
    }

    public LOV LOV_ABA_KREDIT_USIA() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_KREDIT_USIA' order by orderseq ");
    }

    public LOV LOV_ABA_KREDIT_PROFILE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ABA_KREDIT_PROFILE' order by orderseq ");
    }

    public LOV LOV_OwnerDivision() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select code, description from s_division where owner_status_enable = 'Y' order by code");
    }

    public LOV LOV_UserDivision() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select code, description from s_division where user_status_enable = 'Y' order by code");
    }

    public LOV LOV_RKAP_UnitKerja(Map params) throws Exception {

        String cc_code = (String) params.get("param");
        logger.logDebug("@@@@@@@@@@@@@@@@@@ " + cc_code);

        String cabang = null;
        if (cc_code == null) {
            cabang = "00";
        } else {
            cabang = cc_code;
        }

        return ListUtil.getLookUpFromQueryDS(
                "select kode_cabang,kota from cabang where cc_code = ? order by cc_code,kode_cabang ",
                new Object[]{cabang}, "RKAPDB");
    }

    public LOV LOV_RKAP_Identifikasi(Map params) throws Exception {

//        String unitKerja = (String) params.get("param");
//        logger.logDebug("@@@@@@@@@@@@@@@@@@ " + unitKerja);

        final String param = (String) params.get("param");
        final String param2[] = param.split("[\\|]");

        String unitKerja = param2[0];
        String tahun = param2[1];

//        return ListUtil.getLookUpFromQueryDS(
//                "select id_identifikasi_masalah,identifikasi_masalah from identifikasi_masalah where approval = 'Y' and kode_cabang = ? and tahun = ? order by id_identifikasi_masalah",
//                new Object[]{unitKerja, tahun}, "RKAPDB");

        return ListUtil.getLookUpFromQueryDS(
                "select a.id_program_kerja,a.program_kerja from program_kerja a "
                + "inner join identifikasi_masalah b on b.id_identifikasi_masalah = a.id_identifikasi_masalah "
                + "where b.approval = 'Y' and b.kode_cabang = ? and b.tahun = ? ",
                new Object[]{unitKerja, tahun}, "RKAPDB");
    }

    public LOV LOV_RKAP_ProgramKerja(Map params) throws Exception {

        String identifikasi = (String) params.get("param");
        logger.logDebug("@@@@@@@@@@@@@@@@@@ " + identifikasi);

        return ListUtil.getLookUpFromQueryDS(
                "select id_program_kerja,program_kerja from program_kerja where id_identifikasi_masalah = ? order by id_program_kerja",
                new Object[]{identifikasi}, "RKAPDB");
    }

    public LOV LOV_RKAP_RencanaAnggaran(Map params) throws Exception {

        String rencanaanggaran = (String) params.get("param");
        logger.logDebug("@@@@@@@@@@@@@@@@@@ " + rencanaanggaran);

        String query = null;
        if (rencanaanggaran.equalsIgnoreCase("14")) {
            query = "select a.id_sub_barang,b.nama_barang as uraian from sub_barang a "
                    + "join barang b on (a.id_barang=b.id_barang) "
                    + "order by a.id_sub_barang";
        } else {
            query = "select id_rencana_anggaran,uraian from rencana_anggaran order by id_rencana_anggaran";
        }

        return ListUtil.getLookUpFromQueryDS(
                query,
                "RKAPDB");
    }

    public LOV LOV_RKAP_KertasKerja(Map params) throws Exception {

        String kertaskerja = (String) params.get("param");
        logger.logDebug("@@@@@@@@@@@@@@@@@@ " + kertaskerja);

        String query = null;
        if (kertaskerja.equalsIgnoreCase("14")) {
            query = "select id_barang,nama_barang as rencana_kerja from barang order by id_barang";
        } else {
            query = "select id_kertas_kerja,rencana_kerja from kertas_kerja order by id_kertas_kerja";
        }

        return ListUtil.getLookUpFromQueryDS(
                query,
                "RKAPDB");
    }

    public LOV LOV_RKAP_KertasKerjaItem(Map params) throws Exception {

        final String param = (String) params.get("param");
        final String param2[] = param.split("[\\|]");
        String tahun = param2[0];
        int iTahun = Integer.parseInt(tahun) - 1;

        String unit = param2[1];
        String akun = param2[2];

        return ListUtil.getLookUpFromQueryDS(
                "select id_kertas_kerja,rencana_kerja from kebijakan_perusahaan a "
                + "join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                + "join cabang c on (b.kode_cabang=c.kode_cabang) "
                + "join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                + "join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja) "
                + "join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran) "
                + "join gl_chart g on (f.no_account=g.no_account) "
                + "join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya) "
                + "where b.tahun=? and c.kode_cabang=? and h.id_jenis_biaya = ? ",
                new Object[]{iTahun, unit, akun},
                "RKAPDB");
    }

    public LOV LOV_TYPE_ANGGARAN() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where active_flag = 'Y' and vs_group = 'TYPE_ANGGARAN' order by vs_code ").setNoNull().setLOValue("1");
    }

    public LOV LOV_ClaimSubCause(Map parameters) throws Exception {
        final String causeID = (String) JSPUtil.getParameter(parameters, "causeID");

        return ListUtil.getLookUpFromQuery(
                "select ins_clm_sub_cause_id,cause_desc from ins_clm_sub_cause where ins_clm_caus_id = ? and active_flag = 'Y' order by cause_desc ",
                new Object[]{causeID});
    }

    public LOV LOV_TypeOfCreditLimit(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");
        final String cc_code2 = (String) parameters.get("policy.stCostCenterCode");

        final Object search = parameters.get("search");

        String query = "select vs_code,(select x.cc_code || ' '|| upper(x.description) from gl_cost_center x where x.cc_code = a.ref1)  "+
                        " ||' - '|| vs_description as vs_description from s_valueset a where vs_group='INSOBJ_KREASI_KREDIT'";

        if (search != null) {
            query = query + " and upper(a.vs_description) like '%" + ((String) search).toUpperCase() + "%'";
        }

        query = query + " order by orderseq";

        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_PolicyDocumentType() throws Exception {

        return ListUtil.getDTOListFromQuery(
                "select b.ins_document_type_id, b.description "+
                " from ins_documents a "+
                " left join ins_document_type b on a.ins_document_type_id = b.ins_document_type_id "+
                " where document_class = 'POLICY' and pol_type_id = 59 order by ins_document_type_id",
                InsuranceDocumentTypeView.class);
    }

    public LOV LOV_PERJANJIAN(Map Params) throws Exception {
        final String pksadd = (String) Params.get("pksadd");

        boolean createpks = Tools.isYes(pksadd);
        String query = null;

        if (createpks) {
            query = "select vs_code, vs_description from s_valueset where active_flag = 'Y' and vs_group = 'PERJANJIAN' and default_flag = 'Y' order by vs_code";
        } else {
            query = "select vs_code, vs_description from s_valueset where active_flag = 'Y' and vs_group = 'PERJANJIAN' and default_flag is null order by vs_code";
        }

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_RESTITUSI(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) JSPUtil.getParameter(params, "param");

        sqa.addQuery("from ar_invoice a ");
        sqa.addSelect("a.ar_invoice_id as pol_id, a.attr_pol_no as pol_no,a.attr_pol_name as cust_name, "
                + "a.invoice_no as dla_no, trim(to_char(coalesce(a.amount,0),'FM999999999999999999'))::character varying as jumlahklaim  ");

        sqa.addClause("a.invoice_type = 'AR'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id in (5,6,7)");
        sqa.addClause("a.amount_settled is null");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.ar_invoice_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("a.attr_pol_no like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
//            final String key = ((String) search).toUpperCase();
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
        }
        if (search == null) {
            sqa.addClause(" a.attr_pol_no = '045921211121110100' ");
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
        }

        sqa.addOrder("a.ar_invoice_id");
        sqa.setLimit(10);

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_SHKKOMISI(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) JSPUtil.getParameter(params, "param");

        sqa.addQuery("from ar_invoice a "
                + "left join ins_proposal_komisi b on b.ins_ar_invoice_id = a.ar_invoice_id ");

        sqa.addSelect("b.ins_upload_id as pol_id, a.no_surat_hutang as pol_no,b.data_amount||' Hutkom' as dla_no,"
                + "trim(to_char(coalesce(b.amount_total,0),'FM999999999999999999'))::character varying as jumlahklaim ");

        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("b.ins_upload_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("a.no_surat_hutang = ? ");
//            final String key = "%" + ((String) search).toUpperCase() + "%";
            final String key = ((String) search).toUpperCase();
            sqa.addPar(key);
            sqa.addClause("a.cc_code = ? ");
            sqa.addPar(cabang);
        }
        if (search == null) {
            sqa.addClause(" a.no_surat_hutang = '309/SHK/POL/COMM/12/11/2021' ");
        }

        sqa.addGroup("1,2,3,4");
        sqa.addOrder("a.no_surat_hutang ");
        sqa.setLimit(10);

        return sqa.getList(InsurancePolicyView.class);
    }

    public LOV LOV_BIAPEMREPORT(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, orderseq||'. '||vs_description as vs_description from s_valueset where vs_group='PROD_BIAPEM' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

    }

    public LOV LOV_ParentPKS(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");
        final String cc_code2 = (String) parameters.get("policy.stCostCenterCode");

        final Object search = parameters.get("search");

        String query = "";

        if (cc_code != null) {
            if (cc_code.equalsIgnoreCase("00")) {
                query = "select pks_id, pol_no from perjanjian_kerjasama where ref1 = '6'";
            } else {
                query = "select pks_id, pol_no from perjanjian_kerjasama where ref1 = '6' and cc_code = '" + cc_code + "'";
            }
        } else if (cc_code == null) {
            if (cc_code2 != null) {
                query = "select pks_id, pol_no from perjanjian_kerjasama where ref1 = '6' and cc_code like '%" + cc_code2 + "%'";
            } else {
                query = "select pks_id, pol_no from perjanjian_kerjasama where ref1 = '6'";
            }
        }

        if (search != null) {
            query = query + " and upper(pol_no) like '%" + ((String) search).toUpperCase() + "%'";
        }

        return ListUtil.getLookUpFromQuery(query);

    }

    public LOV LOV_CostCenter2() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select koda,description from gl_cost_center2 group by 1,2 order by 1 ");
    }

    public LOV LOV_CompTypeRpt(Map Params) throws Exception {
        final String custcat = (String) Params.get("custcat");
        final String custcatdep = (String) Params.get("custcatdep");

        String query = "select vs_code, vs_description from s_valueset where vs_group = 'COMP_TYPE' ";

        if (custcat != null) {
            if (Tools.isYes(custcat)) {
                query = query + " and vs_code not in (select cc_code from gl_cost_center) ";
            } else {
                query = query + " and vs_code in (select cc_code from gl_cost_center) ";
            }
        }

        if (custcatdep != null) {
            if (Tools.isYes(custcatdep)) {
                query = query + " and vs_code not in (select cc_code from gl_cost_center) and vs_code in ('89','90','91','92','93','94','95','97') ";
            } else {
                query = query + " and vs_code in (select cc_code from gl_cost_center) ";
            }
        }

        query = query + " order by orderseq";

        return ListUtil.getLookUpFromQuery(query);
    }

    public LOV LOV_GROUPSOA() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select ins_policy_type_grp_id,group_name from ins_policy_type_grp where cat = 3 order by ins_policy_type_grp_id");
    }

    public LOV LOV_TreatyGroup() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='TREATY_GROUP' order by vs_code");
    }

    public LOV LOV_ClaimCauseAll(Map parameters) throws Exception {

        return ListUtil.getLookUpFromQuery(
                "select ins_clm_caus_id,pol_type_id || ' - ' || cause_desc as cause_desc from ins_clm_cause order by active_flag desc,ins_clm_caus_id");
    }

    public LOV LOV_ClaimLossAll(Map parameters) throws Exception {

        return ListUtil.getLookUpFromQuery(
                "select claim_loss_id,pol_type_id || ' - ' || loss_desc as loss_desc from ins_claim_loss");
    }

    public LOV LOV_DataSourceType() throws Exception {
        return FinCodec.DataSourceType.getLookUp();
    }

    public LOV LOV_BusinessType() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='BUSSINESS_TYPE' order by vs_code");
    }

     public LOV LOV_TitipanPremiNew(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");
        final Object search = params.get("search");
        final Object value = params.get("value");


        String sqlTitipan = "";

        sqlTitipan = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah, 'Titipan' as keterangan "+
                        " from ( select  trx_id, trx_no,counter, pol_no,description, amount,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian  "+
                        " from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "+
                        " where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as terpakai "+
                        " from ar_titipan_premi a "+
                        " where  "+
                        " coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0 ";

        if (cabang != null){
            sqlTitipan = sqlTitipan + " and cc_code = '"+  cabang +"'";
        }

        if (value != null) {
            sqlTitipan = sqlTitipan + "and trx_id="+ value;
        }

        if (search != null) {

            String key = "%" + ((String) search).toUpperCase() + "%";

             sqlTitipan = sqlTitipan + " and (upper(trx_no) like '"+ key +"' or upper(description) like '"+ key +"' or upper(pol_no) like '"+ key +"')";
        }

             sqlTitipan = sqlTitipan + " order by trx_no,counter limit 2000) zz "+
                        " where (amount - terpakai) <> 0";

        

        //titipan extracomptable
        String sqlTitipanExcom = "";

        sqlTitipanExcom = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah, 'Extracomptable' as keterangan "+
                        " from ( select  trx_id, trx_no,counter, pol_no,description, balance as amount,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian  "+
                        " from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "+
                        " where y.status = 'POST' AND z.titipan_premi_id = a.trx_id and y.receipt_date>='2024-01-01 00:00:00') as terpakai "+
                        " from ar_titipan_premi_extracomptable a "+
                        " where  "+
                        " coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0 ";

        if (cabang != null){
            sqlTitipanExcom = sqlTitipanExcom + " and cc_code = '"+  cabang +"'";
        }

        if (value != null) {
            sqlTitipanExcom = sqlTitipanExcom + "and trx_id="+ value;
        }

        if (search != null) {

            String key = "%" + ((String) search).toUpperCase() + "%";

             sqlTitipanExcom = sqlTitipanExcom + " and (upper(trx_no) like '"+ key +"' or upper(description) like '"+ key +"' or upper(pol_no) like '"+ key +"')";
        }

             sqlTitipanExcom = sqlTitipanExcom + " order by trx_no,counter limit 2000) zz "+
                        " where (amount - terpakai) <> 0";


             String sqlUnion = sqlTitipan + " union "+ sqlTitipanExcom;
        //return sqa.getList(TitipanPremiView.class);

        return ListUtil.getDTOListFromQuery(
                sqlUnion,
                TitipanPremiView.class);
    }

     public LOV LOV_TitipanPremiPolisKhusus(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");

        sqa.addQuery(" from ar_titipan_premi_extracomptable a");
        sqa.addSelect(" trx_id, trx_no,counter, pol_no,description,"
                + " amount,"
                + "  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "
                + "   from ar_receipt y "
                + "   inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "
                + "   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id  and y.receipt_date>='2024-01-01 00:00:00') as terpakai");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("trx_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {

            sqa.addClause("(upper(trx_no) like ? or upper(description) like ? or upper(pol_no) like ?)");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");

            sqa.addClause("coalesce(active_flag,'') <> 'N'");
            sqa.addClause("approved = 'Y'");
        }

        if (cabang != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(cabang);
        }

        if (value == null) {
            sqa.addClause("balance <> 0");
        }

        sqa.addOrder("trx_no,counter");

        sqa.setLimit(2000);

        String sql = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah, 'Polis Khusus' as keterangan  "
                + " from (" + sqa.getSQL() + ") zz ";

        if (value == null) {
            sql = sql + " where (amount - terpakai) <> 0";
        }



        //return sqa.getList(TitipanPremiView.class);

        return ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                TitipanPremiView.class);
    }

     public LOV LOV_POLIS_KHUSUS(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_id, a.pol_no, a.cust_name ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_id=?");
            sqa.addPar(value);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("upper(a.pol_no||coalesce(a.cust_name,'')) like ? ");
            final String key = "%" + ((String) search).toUpperCase() + "%";
            sqa.addPar(key);

        }

        sqa.addClause("a.active_flag = 'Y' and coalesce(a.effective_flag,'N') = 'Y' ");

         sqa.addClause("a.entity_id in (1042112,1042113)");

        sqa.setLimit(20);

        return sqa.getList(InsurancePolicyView.class);
    }

     public LOV LOV_BussinessTypeGroup() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, 'Unit '||vs_description as vs_description from s_valueset where vs_group='BUSSINESS_TYPE' and active_flag='Y' and ref2='Y' order by orderseq ");
    }

    public LOV LOV_BussinessTypeCob() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='BUSSINESS_TYPE' and active_flag='Y' order by orderseq ");
    }
    
    public LOV LOV_CostCenterSource() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select cc_code,description from gl_cost_center where cc_code not in ('00','01','80') order by cc_code");
    }

    public LOV LOV_RegionSource(Map parameters) throws Exception {

        final String ccCode = (String) parameters.get("cc_code_source");
        final String value = (String) parameters.get("value");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("region_id,description");

        sqa.addQuery("from s_region");

        if (ccCode != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(ccCode);
        }

        if (value != null) {
            sqa.addClause("true or region_id=?");
            sqa.addPar(value);
        }
        sqa.addClause("level in ('1','2')");
        sqa.addOrder("region_code2");

        return sqa.getLookUp();
    }

    public LOV LOV_TitipanPremiReinsurance(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        //String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final Object cabang = params.get("cc_code");

        sqa.addQuery(" from ar_titipan_premi_reinsurance a");
        sqa.addSelect(" trx_id, trx_no,counter, pol_no,description,"
                + " amount,"
                + "  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "
                + "   from ar_receipt y "
                + "   inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "
                + "   where y.ar_settlement_id = 49 and y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as terpakai");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("trx_id=?");
            sqa.addPar(value);
        }

        final Object search = params.get("search");
        if (search != null) {

            sqa.addClause("(upper(trx_no) like ? or upper(description) like ? or upper(pol_no) like ?)");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");
            sqa.addPar("%" + ((String) search).toUpperCase() + "%");

            sqa.addClause("coalesce(active_flag,'') <> 'N'");
            sqa.addClause("approved = 'Y'");
        }

        if (cabang != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(cabang);
        }

        if (value == null) {
            sqa.addClause("balance <> 0");
        }

        sqa.addOrder("trx_no,counter");

        sqa.setLimit(2000);

        String sql = "select *, trim(to_char(coalesce((amount - terpakai),0),'999G999G999G999G999G999D99'))::character varying as jumlah, 'Reinsurance' as keterangan  "
                + " from (" + sqa.getSQL() + ") zz ";

        if (value == null) {
            sql = sql + " where (amount - terpakai) <> 0";
        }



        //return sqa.getList(TitipanPremiView.class);

        return ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                TitipanPremiView.class);
    }

    public LOV LOV_CLAIM_DLA(Map params) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        final String cabang = (String) params.get("cc_code");

        sqa.addQuery("from ins_policy a ");
        sqa.addSelect("a.pol_id, a.dla_no, a.pol_no, a.pla_no  ");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("a.pol_id=?");
            sqa.addPar(value);

            sqa.setLimit(20);
        }

        final Object search = params.get("search");
        if (search != null) {
            sqa.addClause("a.dla_no = ? ");
            final String key = ((String) search).toUpperCase();
            sqa.addPar(key);

            sqa.setLimit(20);
        }

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause("a.claim_status = 'DLA' and a.active_flag = 'Y' and a.effective_flag = 'Y'");
        
        sqa.setLimit(20);

        //return sqa.getList(InsurancePolicyView.class);

        return sqa.getLookUp();
    }

    public LOV LOV_Jobs(Map params) throws Exception {

        final Object o = params.get("jenisDebitur");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("vs_code,vs_description");
        sqa.addQuery("from s_valueset");
        sqa.addClause("vs_group='JOBS'");

        final Object value = params.get("value");
        if (value != null) {
            sqa.addClause("vs_code=?");
            sqa.addPar(value);
        }

        if (o != null) {
            sqa.addClause("ref1 = ?");
            sqa.addPar(o);
        }

        sqa.addOrder("orderseq");

        return sqa.getLookUp();

        //return ListUtil.getLookUpFromQuery("select vs_code,vs_description from s_valueset where vs_group='JOBS' order by orderseq ");
    }
     public LOV LOV_Jobs_Detail() throws Exception {
        return ListUtil.getLookUpFromQuery("select vs_code,vs_description from s_valueset where vs_group='JOBS_DETAIL' order by orderseq ");
    }

     public LOV LOV_ClaimSubCauseAll(Map parameters) throws Exception {
        final String causeID = (String) JSPUtil.getParameter(parameters, "causeID");

        return ListUtil.getLookUpFromQuery(
                "select ins_clm_sub_cause_id,cause_desc from ins_clm_sub_cause order by cause_desc ");
    }

     public LOV LOV_NR_DIV(Map parameters) throws Exception {
        //final String polgroup = (String) JSPUtil.getParameter(parameters, "polgroup");
        final String reporttype = (String) parameters.get("reporttype");

        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group='ACC_NR' and division like ? and active_flag = 'Y' order by orderseq",
                new Object[]{reporttype});

//        return ListUtil.getLookUpFromQuery(
//                "select vs_code,vs_description from s_valueset where vs_group = 'ACC_NR' and active_flag = 'Y' order by orderseq");
    }

     public LOV LOV_PolicyTitipan(Map parameters) throws Exception {
        String cost_center2 = policy.getStCostCenterCode2();
        final String cc_code = (String) parameters.get("cc_code");

        final Object search = parameters.get("search");

        String query = "select pol_id,pol_no,effective_flag from ins_policy";

        if (search != null) {
            query = query + " where pol_no = '" + ((String) search).toUpperCase() + "'";
        }

        final Object value = (String) parameters.get("value");

        if (search == null && value != null) {
            query = query + " where pol_id = "+value;
        }


        query = query + " limit 100";

        return ListUtil.getLookUpFromQuery(query);
    }

     public LOV LOV_BUSSINESS_SOURCE() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ?",
                new Object[]{"BUSINESS_SOURCE_CODE"});
    }


}
