package com.webfin.gl.form;

import com.crux.common.controller.FOPServlet;
import com.crux.common.model.Filter;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.lang.LanguageManager;
import com.crux.login.model.UserSessionView;
import com.crux.lov.LOVManager;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.FTPUploadFile;
import com.crux.util.JNDIUtil;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.webfin.gl.ejb.GLReportEngine2;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.JournalSyariahView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.gl.model.RKAPGroupView;
import com.webfin.gl.model.TitipanPremiReinsuranceView;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.gl.util.GLUtil;
import com.webfin.gl.util.GLUtil.Applicator;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.system.region.model.RegionView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GLListForm
  extends Form
{
  private static final transient LogManager logger = LogManager.getInstance(GLListForm.class);
  private DTOList list;
  private String policyNo;
  private String accountCode;
  private String description;
  private String transNumber;
  private Date transdate;
  private Date transdatefrom;
  private Date transdateto;
  private String branch = SessionManager.getInstance().getSession().getStBranch();
  private String trxhdrid;
  private String showReverse;
  private String printForm = "default";
  private String printLang = LanguageManager.getInstance().getActiveLang();
  private GLUtil.Applicator applicator;
  private String stGlID;
  private String stLang;
  private String stSort;
  private String stReportType;
  private String stPrintForm;
  private String stReport;
  private String stReportTitle;
  private boolean enableSelectForm = true;
  private String ref1;
  private String status;
  private HashMap refPropMap;
  private static HashSet formList = null;
  private Date applyDateFrom;
  private Date applyDateTo;
  private String stSettlement;
  private String stSettlementDesc;
  private String stReceiptNo;
  private String periodFrom;
  private String periodTo;
  private String yearFrom;
  private String yearTo;
  private String stFlag;
  private String year;
  private String period;
  private String branchDesc;
  private String stAccountNo;
  private String stPayment;
  private DTOList cashBanklist;
  private DTOList uangMukaKlaimList;
  private DTOList uangMukaPremiList;
  private DTOList uangMukaKomisiList;
  private DTOList titipanPremiList;
  private DTOList syariahList;
  private DTOList pbList;
  private String stPeriod;
  private String stDivision;
  private String stDivisionDesc;
  //private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
  private boolean enableSuperEdit;
  private Date entrydatefrom;
  private Date entrydateto;
  private String stEntityID;
  private String stEntityName;
  private String stDescription;
  private String stFileName;
  private String stNotApproved = "N";
  private String stAccountID;
  private DTOList listUW;
  private String printType;
  private String method;
  private String stPolicyTypeGroupID;
  private String stPolicyTypeID;
  private String stRecapitulationNo;
  private String transNumberLike;
  private Date perdate;
  private DTOList memorialList;
  private DTOList rkapList;
  private String years;
  private String akun;
  private String stCreateWho;
  private String akunFrom;
  private String akunTo;
  private String sumbis;
  private String sumbisDesc;
  private String periodFromName;
  private String periodToName;
  private String accountCodeLike;
  private Date realisasifrom;
  private Date realisasito;

  private DTOList titipanPremiReinsuranceList;
  private DTOList titipanPremiExtracomptableList;

  private String stReferenceNo;

    public String getStReferenceNo() {
        return stReferenceNo;
    }

    public void setStReferenceNo(String stReferenceNo) {
        this.stReferenceNo = stReferenceNo;
    }

  public String getStSort()
  {
    return this.stSort;
  }

  public void setStSort(String stSort)
  {
    this.stSort = stSort;
  }

  public String getStLang()
  {
    return this.stLang;
  }

  public void setStLang(String stLang)
  {
    this.stLang = stLang;
  }

  public String getStGlID()
  {
    return this.stGlID;
  }

  public void setStGlID(String stGlID)
  {
    this.stGlID = stGlID;
  }

  public Date getTransdatefrom()
  {
    return this.transdatefrom;
  }

  public void setTransdatefrom(Date transdatefrom)
  {
    this.transdatefrom = transdatefrom;
  }

  public Date getTransdateto()
  {
    return this.transdateto;
  }

  public void setTransdateto(Date transdateto)
  {
    this.transdateto = transdateto;
  }

  public void btnPrint()
  {
    String pf = this.printForm;

    super.redirect("/pages/gl/report/rpt_" + pf + ".fop?xlang=" + this.printLang);
  }

  public void printTitipan()
  {
    super.redirect("/pages/gl/report/rpt_titipan.fop?xlang=" + this.printLang);
  }

  public String getPrintForm()
  {
    return this.printForm;
  }

  public void setPrintForm(String printForm)
  {
    this.printForm = printForm;
  }

  public String getPrintLang()
  {
    return this.printLang;
  }

  public void setPrintLang(String printLang)
  {
    this.printLang = printLang;
  }

  public String getShowReverse()
  {
    return this.showReverse;
  }

  public void setShowReverse(String showReverse)
  {
    this.showReverse = showReverse;
  }

  public String getTransNumber()
  {
    return this.transNumber;
  }

  public void setTransNumber(String transNumber)
  {
    this.transNumber = transNumber;
  }

  public Date getTransdate()
  {
    return this.transdate;
  }

  public void setTransdate(Date transdate)
  {
    this.transdate = transdate;
  }

  public String getTrxhdrid()
  {
    return this.trxhdrid;
  }

  public void setTrxhdrid(String trxhdrid)
  {
    this.trxhdrid = trxhdrid;
  }

  public String getPolicyNo()
  {
    return this.policyNo;
  }

  public void setPolicyNo(String policyNo)
  {
    this.policyNo = policyNo;
  }

  public String getAccountCode()
  {
    return this.accountCode;
  }

  public void setAccountCode(String accountCode)
  {
    this.accountCode = accountCode;
  }

  public String getBranch()
  {
    return this.branch;
  }

  public void setBranch(String branch)
  {
    this.branch = branch;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public DTOList getList()
    throws Exception
  {
    if (this.list == null)
    {
      this.list = new DTOList();
      this.list.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQA();

    sqa.addFilter(this.list.getFilter());

    this.list = sqa.getList(JournalView.class);

    return this.list;
  }

  public SQLAssembler getSQA()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from " + gljedetail + " a" + " inner join gl_accounts b on b.account_id = a.accountid ");
    if (this.stSort != null) {
      sqa.addOrder("trx_id " + this.stSort);
    }
    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (this.stRecapitulationNo != null)
    {
      sqa.addClause("a.recap_no = ? ");
      sqa.addPar(this.stRecapitulationNo);
    }
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.stAccountID != null)
    {
      sqa.addClause("a.accountid = ?");
      sqa.addPar(this.stAccountID);
    }
    if (this.accountCodeLike != null)
    {
      int l = this.accountCodeLike.length();
      sqa.addClause("b.accountno like ? ");
      sqa.addPar(this.accountCodeLike + '%');
    }
    if (this.method != null)
    {
      String inv = "";
      if (this.method.equalsIgnoreCase("G,H,I")) {
        inv = "('G','H','I')";
      } else if (this.method.equalsIgnoreCase("K,L,N")) {
        inv = "('K','L','N')";
      } else {
        inv = "('" + this.method + "')";
      }
      sqa.addClause("substr(a.trx_no,1,1) in " + inv);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber.toUpperCase());
    }
    if (this.transNumberLike != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar(this.transNumberLike.toUpperCase() + "%");
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");

      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
      sqa.addPar(this.description.toUpperCase().replaceAll(" ", "&"));
    }
    if ((this.stPayment != null) &&
      (Tools.isYes(this.stPayment)))
    {
      sqa.addClause("a.ref_trx_type = ?");
      sqa.addPar("RCP");
    }
    if (this.stPolicyTypeID != null)
    {
      sqa.addClause("substr(pol_no,3,2) = ? ");
      String poltypeFilter = this.stPolicyTypeID.length() < 2 ? "0" + this.stPolicyTypeID : this.stPolicyTypeID;
      sqa.addPar(poltypeFilter);
    }
    if ((this.policyNo == null) && (this.description == null) && (this.transNumber == null) && (this.accountCode == null) && (this.transdatefrom == null) && (this.transdateto == null)) {
      sqa.setLimit(100);
    }
    return sqa;
  }

  public SQLAssembler getSQA2()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.trx_hdr_id, a.trx_id");

    sqa.addQuery("   from " + gljedetail + " a " + "         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addOrder("trx_id desc, applydate desc");

    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (!bshowRevers) {
      sqa.addClause("(a.reverse_flag<>'Y' or a.reverse_flag is null)");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
      sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar("% " + this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("a.applydate >= to_date(?,'yyyymmdd')");

      sqa.addPar(DateUtil.getDateStr(this.transdatefrom, "yyyyMMdd"));
    }
    if (this.transdateto != null)
    {
      sqa.addClause("a.applydate <= to_date(?,'yyyymmdd')");
      sqa.addPar(DateUtil.getDateStr(this.transdateto, "yyyyMMdd"));
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
      sqa.addPar(this.description.toUpperCase().replaceAll(" ", "&"));
    }
    return sqa;
  }

  public void setList(DTOList list)
  {
    this.list = list;
  }

  public GLListForm()
  {
    this.stGlID = ((String)getAttribute("glid"));

    this.applicator = new GLUtil.Applicator();
  }

  public void btnSearch()
    throws Exception
  {
    getList().getFilter().setCurrentPage(0);
  }

  public String getBranchDescription()
  {
    GLCostCenterView glv = (GLCostCenterView)DTOPool.getInstance().getDTORO(GLCostCenterView.class, this.branch);

    return glv == null ? null : glv.getStDescription();
  }

  public String getAccountDescription()
    throws Exception
  {
    AccountView ac = getAccount();

    return ac == null ? "-" : ac.getStDescription();
  }

  public AccountView getAccount()
    throws Exception
  {
    return GLUtil.getAccountByCode(this.accountCode);
  }

  public String getStReportType()
  {
    return this.stReportType;
  }

  public void setStReportType(String stReportType)
  {
    this.stReportType = stReportType;
  }

  public String getStPrintForm()
  {
    return this.stPrintForm;
  }

  public void setStPrintForm(String stPrintForm)
  {
    this.stPrintForm = stPrintForm;
  }

  public String getStReport()
  {
    return this.stReport;
  }

  public void setStReport(String stReport)
  {
    this.stReport = stReport;
  }

  public boolean isEnableSelectForm()
  {
    return this.enableSelectForm;
  }

  public void setEnableSelectForm(boolean enableSelectForm)
  {
    this.enableSelectForm = enableSelectForm;
  }

  public void go()
  {
    this.stPrintForm = ((String)getAttribute("rpt"));
    this.enableSelectForm = false;

    this.stReportTitle = LOVManager.getInstance().getDescription(this.stPrintForm, "VS_PROD_JOURNAL");
  }

  public void onChangeReport() {}

  public String getStReportTitle()
  {
    return this.stReportTitle;
  }

  public void setStReportTitle(String stReportTitle)
  {
    this.stReportTitle = stReportTitle;
  }

  private DTOList loadList()
    throws Exception
  {
    setRef1(LOVManager.getInstance().getRef1("PROD_JOURNAL", this.stReport));

    this.refPropMap = Tools.getPropMap(getRef1());

    String queryID = (String)this.refPropMap.get("QUERY");

    setStatus((String)this.refPropMap.get("STATUS"));
    if (queryID != null)
    {
      Method m = getClass().getMethod(queryID, null);

      return (DTOList)m.invoke(this, null);
    }
    return null;
  }

  private void loadFormList()
  {
    if (formList != null) {}
    String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/gl/report")).list();

    formList = new HashSet();
    for (int i = 0; i < filez.length; i++)
    {
      String s = filez[i];

      formList.add(s);
    }
  }

  public void clickPrint()
    throws Exception
  {
    loadFormList();

    DTOList l = loadList();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    ArrayList plist = new ArrayList();

    plist.add(this.stReport);

    String urx = null;
    for (int i = 0; i < plist.size(); i++)
    {
      String s = (String)plist.get(i);
      if (formList.contains(s + ".fop.jsp"))
      {
        urx = "/pages/gl/report/" + s + ".fop?xlang=" + this.stLang;
        break;
      }
    }
    if (urx == null) {
      throw new RuntimeException("Unable to find suitable print form");
    }
    super.redirect(urx);
  }

  public void initialize()
  {
    this.stReportType = ((String)getAttribute("rpt"));
  }

  public String getRef1()
  {
    return this.ref1;
  }

  public void setRef1(String ref1)
  {
    this.ref1 = ref1;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public Date getApplyDateFrom()
  {
    return this.applyDateFrom;
  }

  public void setApplyDateFrom(Date applyDateFrom)
  {
    this.applyDateFrom = applyDateFrom;
  }

  public Date getApplyDateTo()
  {
    return this.applyDateTo;
  }

  public void setApplyDateTo(Date applyDateTo)
  {
    this.applyDateTo = applyDateTo;
  }

  public DTOList JOURNAL()
    throws Exception
  {
    String gljedetail = null;
    if (this.applyDateFrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.applyDateFrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.applydate,a.trx_id,a.accountid, b.accountno as create_who,a.debit,a.credit,a.description,a.trx_no,a.pol_no ");

    sqa.addQuery(" from " + gljedetail + " a " + " inner join gl_accounts b on b.account_id = a.accountid  " + " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint ");

    sqa.addClause("(a.reverse_flag<>'Y' or a.reverse_flag is null)");

    sqa.addClause("ref_trx_type = 'RCP'");
    if (this.applyDateFrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.applyDateFrom);
    }
    if (this.applyDateTo != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.applyDateTo);
    }
    if (this.stReceiptNo != null)
    {
      sqa.addClause("c.receipt_no like ?");
      sqa.addPar('%' + this.stReceiptNo + '%');
    }
    if (this.stAccountNo != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar('%' + this.stAccountNo + '%');
    }
    if (this.stSettlement != null)
    {
      sqa.addClause("c.ar_settlement_id = ?");
      sqa.addPar(this.stSettlement);
    }
    String sql = sqa.getSQL() + " order by a.applydate,a.trx_no asc ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public String getStSettlement()
  {
    return this.stSettlement;
  }

  public void setStSettlement(String stSettlement)
  {
    this.stSettlement = stSettlement;
  }

  public String getStSettlementDesc()
  {
    return this.stSettlementDesc;
  }

  public void setStSettlementDesc(String stSettlementDesc)
  {
    this.stSettlementDesc = stSettlementDesc;
  }

  public String getStReceiptNo()
  {
    return this.stReceiptNo;
  }

  public void setStReceiptNo(String stReceiptNo)
  {
    this.stReceiptNo = stReceiptNo;
  }

  public DTOList POSITION()
    throws Exception
  {
    boolean FLT_BANK = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_BANK"));
    boolean FLT_KAS = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_KAS"));
    boolean FLT_KLAIM = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_KLAIM"));

    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String fld = "bal";
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.account_id as accountid,b.accountno as hdr_accountno,b.noper,substr(b.description,32,50) as trx_no,a.period_year as fiscal_year, b.description,substr(b.accountno,14,2) as ccy_code,b.rekno as ref_trx_type, d.description as jenas,c.description as koda,sum(coalesce(bal" + getLPeriodTo() + ",0)) as entered_debit, " + "coalesce(round(sum(" + plist + "),2),0) as balance");

    sqa.addQuery(" from gl_accounts b left join gl_acct_bal2 a on b.account_id = a.account_id  left join gl_cost_center c on c.cc_code = substr(b.accountno,14,2)  left join ins_pol_types d on d.vx = substr(b.accountno,11,2)");

    sqa.addClause("b.acctype is null");
    if (FLT_BANK) {
      sqa.addClause("((substr(b.accountno,1,3)) between '122' and '122')");
    }
    if (FLT_KAS) {
      sqa.addClause("((substr(b.accountno,1,3)) between '121' and '121')");
    }
    if (FLT_KLAIM) {
      sqa.addClause("((substr(b.accountno,1,2)) between '33' and '33')");
    }
    if (this.yearFrom != null)
    {
      sqa.addClause("(a.period_year between ? and ?)");
      sqa.addPar(this.yearFrom);
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    sqa.addClause("a.idr_flag = 'Y'");

    String sql = null;
    if (FLT_KLAIM) {
      sql = "select x.noper as pol_no,x.jenas,x.koda,x.fiscal_year,sum(x.balance) as balance from ( " + sqa.getSQL() + " " + " group by a.account_id,a.period_year,b.accountno,b.noper,b.description,b.rekno,c.description,d.description   " + " ) x where balance <> 0 group by x.noper,x.jenas,x.fiscal_year,x.koda order by x.noper,x.koda,x.jenas ";
    }
    if ((FLT_BANK) || (FLT_KAS)) {
      sql = "select * from ( " + sqa.getSQL() + " group by a.account_id,a.period_year,b.accountno,b.noper,b.description,b.rekno,c.description,d.description " + " ) x where balance <> 0 order by ccy_code,noper,hdr_accountno ";
    }
    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public long getLPeriodFrom()
  {
    return Long.parseLong(this.periodFrom);
  }

  public long getLPeriodTo()
  {
    return Long.parseLong(this.periodTo);
  }

  public long getLYearFrom()
  {
    return Long.parseLong(this.yearFrom);
  }

  public long getLYearTo()
  {
    return Long.parseLong(this.yearTo);
  }

  public String getPeriodFrom()
  {
    return this.periodFrom;
  }

  public void setPeriodFrom(String periodFrom)
  {
    this.periodFrom = periodFrom;
  }

  public String getPeriodTo()
  {
    return this.periodTo;
  }

  public void setPeriodTo(String periodTo)
  {
    this.periodTo = periodTo;
  }

  public String getYearFrom()
  {
    return this.yearFrom;
  }

  public void setYearFrom(String yearFrom)
  {
    this.yearFrom = yearFrom;
  }

  public String getYearTo()
  {
    return this.yearTo;
  }

  public void setYearTo(String yearTo)
  {
    this.yearTo = yearTo;
  }

  public String getStFlag()
  {
    return this.stFlag;
  }

  public void setStFlag(String stFlag)
  {
    this.stFlag = stFlag;
  }

  public String getBranchDesc()
  {
    return this.branchDesc;
  }

  public void setBranchDesc(String branchDesc)
  {
    this.branchDesc = branchDesc;
  }

  public String getPeriodTitleDescription()
    throws Exception
  {
    if (this.year != null)
    {
      PeriodDetailView pd = PeriodManager.getInstance().getPeriod(this.periodTo, this.year);

      Date d = pd.getDtEndDate();

      return "PER " + DateUtil.getDateStr(d, "d ^^ yyyy");
    }
    if (this.yearFrom != null)
    {
      PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(this.periodFrom, this.yearFrom);
      PeriodDetailView pd2 = PeriodManager.getInstance().getPeriod(this.periodTo, this.yearFrom);

      Date d1 = pd1.getDtStartDate();
      Date d2 = pd2.getDtEndDate();

      return "PER " + DateUtil.getDateStr(d2, "d ^^ yyyy");
    }
    return "???";
  }

  public String getMonthTitleDescription()
    throws Exception
  {
    PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(this.periodTo, this.yearFrom);

    Date d1 = pd1.getDtEndDate();

    return DateUtil.getDateStr(d1, "^^ yyyy");
  }

  public String getMonthBeforeTitleDescription()
    throws Exception
  {
    long LperiodTo = Long.parseLong(this.periodTo);

    LperiodTo -= 1L;

    String PeriodBeforeTo = String.valueOf(LperiodTo);

    PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(PeriodBeforeTo, this.yearFrom);

    Date d1 = pd1.getDtEndDate();

    return DateUtil.getDateStr(d1, "^^ yyyy");
  }

  public String getYear()
  {
    return this.year;
  }

  public void setYear(String year)
  {
    this.year = year;
  }

  public String getPeriod()
  {
    return this.period;
  }

  public void setPeriod(String period)
  {
    this.period = period;
  }

  public DTOList getCashBanklist()
    throws Exception
  {
    if (this.cashBanklist == null)
    {
      this.cashBanklist = new DTOList();
      this.cashBanklist.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQACashBank();

    sqa.addFilter(this.cashBanklist.getFilter());

    this.cashBanklist = sqa.getList(JournalView.class);

    return this.cashBanklist;
  }

  public void setCashBanklist(DTOList cashBanklist)
  {
    this.cashBanklist = cashBanklist;
  }

  public SQLAssembler getSQACashBank()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno, c.user_name");

    sqa.addQuery("   from " + gljedetail + " a " + "         inner join gl_accounts b on b.account_id = a.accountid" + "       left join s_users c on c.user_id = a.create_who ");

    sqa.addOrder("trx_id desc");

    sqa.addClause("ref_trx_type = 'CASHBANK'");

    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.accountCode != null)
    {
      sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
      sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber.toUpperCase());
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
      sqa.addPar(this.description.toUpperCase().replaceAll(" ", "&"));
    }
    if ((this.policyNo != null) || (this.accountCode != null) || (this.transNumber != null) || (this.transdatefrom != null) || (this.transdateto != null) || (this.transdate != null) || (this.description != null)) {
      sqa.setLimit(400);
    } else if ((this.policyNo == null) && (this.accountCode == null) && (this.transNumber == null) && (this.transdatefrom == null) && (this.transdateto == null) && (this.transdate == null) && (this.description == null)) {
      sqa.setLimit(100);
    } else {
      sqa.setLimit(100);
    }
    return sqa;
  }

  public String getStAccountNo()
  {
    return this.stAccountNo;
  }

  public void setStAccountNo(String stAccountNo)
  {
    this.stAccountNo = stAccountNo;
  }

  public String getStPayment()
  {
    return this.stPayment;
  }

  public void setStPayment(String stPayment)
  {
    this.stPayment = stPayment;
  }

  public DTOList getUangMukaKlaimList()
    throws Exception
  {
    if (this.uangMukaKlaimList == null)
    {
      this.uangMukaKlaimList = new DTOList();
      this.uangMukaKlaimList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAUangMukaKlaim();

    sqa.addFilter(this.uangMukaKlaimList.getFilter());

    this.uangMukaKlaimList = sqa.getList(JournalView.class);

    return this.uangMukaKlaimList;
  }

  public void setUangMukaKlaimList(DTOList uangMukaKlaimList)
  {
    this.uangMukaKlaimList = uangMukaKlaimList;
  }

  public SQLAssembler getSQAUangMukaKlaim()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from       gl_je_detail a         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addOrder("trx_id desc");

    sqa.addClause("journal_code is not null");
    sqa.addClause("ref_trx_type = 'CLAIM_ADVPAYMENT'");
    if ((this.policyNo == null) && (this.accountCode == null) && (this.transNumber == null) && (this.transdatefrom == null) && (this.transdateto == null) && (this.transdate == null) && (this.description == null))
    {
      sqa.setLimit(200);
    }
    else
    {
      boolean bshowRevers = Tools.isYes(this.showReverse);
      if (!bshowRevers) {
        sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
      }
      if (this.policyNo != null)
      {
        sqa.addClause("a.pol_no = ? ");
        sqa.addPar(this.policyNo.toUpperCase());
      }
      if (this.accountCode != null)
      {
        sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
        sqa.addPar(this.accountCode.toUpperCase() + "%");
        sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
      }
      if (this.branch != null)
      {
        sqa.addClause("b.accountno like ?");
        sqa.addPar("% " + this.branch);
      }
      if (this.transNumber != null)
      {
        sqa.addClause("a.trx_no like ?");
        sqa.addPar(this.transNumber.toUpperCase());
      }
      if (this.transdatefrom != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");

        sqa.addPar(this.transdatefrom);
      }
      if (this.transdateto != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) <= ?");

        sqa.addPar(this.transdateto);
      }
      if (this.transdate != null)
      {
        sqa.addClause("date_trunc('day',a.applydate)=?");
        sqa.addPar(this.transdate);
      }
      if (this.description != null)
      {
        sqa.addClause("upper(a.description) like ?");
        sqa.addPar("%" + this.description.toUpperCase() + "%");
      }
      sqa.setLimit(400);
    }
    return sqa;
  }

  public DTOList getUangMukaPremiList()
    throws Exception
  {
    if (this.uangMukaPremiList == null)
    {
      this.uangMukaPremiList = new DTOList();
      this.uangMukaPremiList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAUangMukaPremi();

    sqa.addFilter(this.uangMukaPremiList.getFilter());

    this.uangMukaPremiList = sqa.getList(JournalView.class);

    return this.uangMukaPremiList;
  }

  public SQLAssembler getSQAUangMukaPremi()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from       gl_je_detail a         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addOrder("trx_id desc");

    sqa.addClause("journal_code is not null");
    sqa.addClause("ref_trx_type = 'PREMIUM_ADVPAYMENT'");
    if ((this.policyNo == null) && (this.accountCode == null) && (this.transNumber == null) && (this.transdatefrom == null) && (this.transdateto == null) && (this.transdate == null) && (this.description == null))
    {
      sqa.setLimit(200);
    }
    else
    {
      boolean bshowRevers = Tools.isYes(this.showReverse);
      if (!bshowRevers) {
        sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
      }
      if (this.policyNo != null)
      {
        sqa.addClause("a.pol_no = ? ");
        sqa.addPar(this.policyNo.toUpperCase());
      }
      if (this.accountCode != null)
      {
        sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
        sqa.addPar(this.accountCode.toUpperCase() + "%");
        sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
      }
      if (this.branch != null)
      {
        sqa.addClause("b.accountno like ?");
        sqa.addPar("% " + this.branch);
      }
      if (this.transNumber != null)
      {
        sqa.addClause("a.trx_no like ?");
        sqa.addPar(this.transNumber.toUpperCase());
      }
      if (this.transdatefrom != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");

        sqa.addPar(this.transdatefrom);
      }
      if (this.transdateto != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) <= ?");

        sqa.addPar(this.transdateto);
      }
      if (this.transdate != null)
      {
        sqa.addClause("date_trunc('day',a.applydate)=?");
        sqa.addPar(this.transdate);
      }
      if (this.description != null)
      {
        sqa.addClause("upper(a.description) like ?");
        sqa.addPar("%" + this.description.toUpperCase() + "%");
      }
      sqa.setLimit(400);
    }
    return sqa;
  }

  public void setUangMukaPremiList(DTOList uangMukaPremiList)
  {
    this.uangMukaPremiList = uangMukaPremiList;
  }

  public DTOList getUangMukaKomisiList()
    throws Exception
  {
    if (this.uangMukaKomisiList == null)
    {
      this.uangMukaKomisiList = new DTOList();
      this.uangMukaKomisiList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAUangMukaKomisi();

    sqa.addFilter(this.uangMukaKomisiList.getFilter());

    this.uangMukaKomisiList = sqa.getList(JournalView.class);

    return this.uangMukaKomisiList;
  }

  public void setUangMukaKomisiList(DTOList uangMukaKomisiList)
  {
    this.uangMukaKomisiList = uangMukaKomisiList;
  }

  public SQLAssembler getSQAUangMukaKomisi()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from       gl_je_detail a         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addOrder("trx_id desc");

    sqa.addClause("journal_code is not null");
    sqa.addClause("ref_trx_type = 'COMIS_ADVPAYMENT'");
    if ((this.policyNo == null) && (this.accountCode == null) && (this.transNumber == null) && (this.transdatefrom == null) && (this.transdateto == null) && (this.transdate == null) && (this.description == null))
    {
      sqa.setLimit(200);
    }
    else
    {
      boolean bshowRevers = Tools.isYes(this.showReverse);
      if (!bshowRevers) {
        sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
      }
      if (this.policyNo != null)
      {
        sqa.addClause("a.pol_no = ? ");
        sqa.addPar(this.policyNo.toUpperCase());
      }
      if (this.accountCode != null)
      {
        sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
        sqa.addPar(this.accountCode.toUpperCase() + "%");
        sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
      }
      if (this.branch != null)
      {
        sqa.addClause("b.accountno like ?");
        sqa.addPar("% " + this.branch);
      }
      if (this.transNumber != null)
      {
        sqa.addClause("a.trx_no like ?");
        sqa.addPar(this.transNumber.toUpperCase());
      }
      if (this.transdatefrom != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");

        sqa.addPar(this.transdatefrom);
      }
      if (this.transdateto != null)
      {
        sqa.addClause("date_trunc('day',a.applydate) <= ?");

        sqa.addPar(this.transdateto);
      }
      if (this.transdate != null)
      {
        sqa.addClause("date_trunc('day',a.applydate)=?");
        sqa.addPar(this.transdate);
      }
      if (this.description != null)
      {
        sqa.addClause("upper(a.description) like ?");
        sqa.addPar("%" + this.description.toUpperCase() + "%");
      }
      sqa.setLimit(400);
    }
    return sqa;
  }

  public DTOList getTitipanPremiList()
    throws Exception
  {
    if (this.titipanPremiList == null)
    {
      this.titipanPremiList = new DTOList();
      this.titipanPremiList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQATitipanPremi();

    sqa.addFilter(this.titipanPremiList.getFilter());

    this.titipanPremiList = sqa.getList(TitipanPremiView.class);

    return this.titipanPremiList;
  }

  public void setTitipanPremiList(DTOList titipanPremiList)
  {
    this.titipanPremiList = titipanPremiList;
  }

  public SQLAssembler getSQATitipanPremi()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, c.user_name,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi a  inner join gl_accounts b on b.account_id = a.accountid  left join s_users c on c.user_id = a.create_who");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber);
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }

    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ?");
      sqa.addPar(this.policyNo);
    }

    if (this.stReferenceNo != null)
    {
      sqa.addClause("a.reference_no = ?");
      sqa.addPar(this.stReferenceNo);
    }

    sqa.addOrder("a.trx_id desc");
    sqa.setLimit(300);

    return sqa;
  }

  public SQLAssembler getSQATitipanPremiReport()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used ");

    sqa.addQuery(" from ar_titipan_premi a  inner join gl_accounts b on b.account_id = a.accountid ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      }
      else
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(this.transdatefrom);
      }
    }
    else
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    sqa.addOrder("a.trx_id desc,a.trx_no");

    return sqa;
  }

  public DTOList REINS()
    throws Exception
  {
    boolean FLT_AR_REINS = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_AR_REINS"));
    boolean FLT_AP_REINS = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_AP_REINS"));
    boolean FLT_AR_CLAIM = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_AR_CLAIM"));

    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String fld = "bal";
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("SUBSTR(b.accountno,6,5) AS GL_CODE,substr(b.accountno,1,3) as akun, round(coalesce(sum(" + plist + "),0),0) as balance");

    sqa.addQuery(" FROM GL_ACCT_BAL2 A  LEFT JOIN GL_ACCOUNTS B ON B.ACCOUNT_ID = A.ACCOUNT_ID ");

    sqa.addClause("b.acctype is null");
    if (FLT_AR_REINS) {
      sqa.addClause(" b.accountno like '14%' ");
    }
    if (FLT_AP_REINS) {
      sqa.addClause(" b.accountno like '42%' ");
    }
    if (FLT_AR_CLAIM) {
      sqa.addClause(" b.accountno like '72%' ");
    }
    if (this.yearFrom != null)
    {
      sqa.addClause("(a.period_year between ? and ?)");
      sqa.addPar(this.yearFrom);
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    sqa.addClause("a.idr_flag = 'Y'");

    String sql = " select a.trx_no,a.gl_code,a.premi_treaty,a.premi_fact,a.premi_xl,a.klaim_fact,a.klaim_xl,a.klaim_closs,a.debit from ( select b.ent_name,b.shortname2 as trx_no,b.gl_code,b.reas_ent_id,b.ent_id,sum(getpremi2(akun in ('144','423'),balance)) as premi_treaty,sum(getpremi2(akun in ('143','424'),balance)) as premi_fact,sum(getpremi2(akun in ('145','425'),balance)) as premi_xl,sum(getpremi2(akun in ('146','426','724'),balance)) as klaim_fact,sum(getpremi2(akun in ('147','427','726'),balance)) as klaim_xl,sum(getpremi2(akun in ('148','428','725'),balance)) as klaim_closs,sum(getpremi2(akun in ('144','143','145','146','147','148','423','424','425','426','427','428','724','725','726'),balance)) as debit  from ( " + sqa.getSQL() + " group by SUBSTR(b.accountno,6,5),substr(b.accountno,1,3) " + " ) a inner join ent_master b on b.gl_code = a.gl_code and b.active_flag = 'Y' " + " group by b.ent_name,b.shortname2,b.gl_code,b.reas_ent_id,b.ent_id order by b.reas_ent_id,b.gl_code " + " ) a where a.debit <> 0 group by a.trx_no,a.gl_code,a.premi_treaty,a.premi_fact," + "a.premi_xl,a.klaim_fact,a.klaim_xl,a.klaim_closs,a.debit order by a.gl_code ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public DTOList COINS()
    throws Exception
  {
    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String fld = "bal";
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("SUBSTR(b.accountno,6,5) AS GL_CODE,substr(b.accountno,1,3) as akun, round(coalesce(sum(" + plist + "),0),0) as balance");

    sqa.addQuery(" FROM GL_ACCT_BAL2 A  LEFT JOIN GL_ACCOUNTS B ON B.ACCOUNT_ID = A.ACCOUNT_ID ");

    sqa.addClause(" substr(b.accountno,1,4) in ('1491','1494') ");
    sqa.addClause("b.acctype is null");
    if (this.yearFrom != null)
    {
      sqa.addClause("(a.period_year between ? and ?)");
      sqa.addPar(this.yearFrom);
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    sqa.addClause("a.idr_flag = 'Y'");

    String sql = " select a.trx_no,a.gl_code,a.debit  from ( select b.ent_name,b.short_name as trx_no,b.gl_code, getpremi2(akun='149',balance) as debit  from ( " + sqa.getSQL() + " group by SUBSTR(b.accountno,6,5),substr(b.accountno,1,3) " + " ) a inner join ent_master b on b.gl_code = a.gl_code and b.active_flag = 'Y' where a.gl_code <> '00000'" + " order by b.gl_code ) a where a.debit <> 0 group by a.trx_no,a.gl_code,a.debit order by a.gl_code ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public DTOList getSyariahList()
    throws Exception
  {
    if (this.syariahList == null)
    {
      this.syariahList = new DTOList();
      this.syariahList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSyariahSQA();

    sqa.addFilter(this.syariahList.getFilter());

    this.syariahList = sqa.getList(JournalSyariahView.class);

    return this.syariahList;
  }

  public SQLAssembler getSyariahSQA()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno,b.rekno,b.acctype ");

    sqa.addQuery("   from       gl_je_detail_syariah a         inner join gl_accounts b on b.account_id = a.accountid");
    if (this.branch != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar("% " + this.branch);
    }
    if (this.description != null)
    {
      sqa.addClause("upper(a.description) like ?");
      sqa.addPar("%" + this.description.toUpperCase() + "%");
    }
    if (this.periodTo != null)
    {
      sqa.addClause("a.period_no = ?");
      sqa.addPar(this.periodTo);
    }
    if (this.yearFrom != null)
    {
      sqa.addClause("a.fiscal_year = ?");
      sqa.addPar(this.yearFrom);
    }
    sqa.addOrder(" fiscal_year::int desc,period_no desc,b.acctype,b.accountno ");

    sqa.setLimit(300);

    return sqa;
  }

  public DTOList getPbList()
    throws Exception
  {
    if (this.pbList == null)
    {
      this.pbList = new DTOList();
      this.pbList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAPbList();

    sqa.addFilter(this.pbList.getFilter());

    this.pbList = sqa.getList(JournalView.class);

    return this.pbList;
  }

  public void setPbList(DTOList pbList)
  {
    this.pbList = pbList;
  }

  public SQLAssembler getSQAPbList()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      if (gljedetailYear.equalsIgnoreCase(getYearPosting())) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from " + gljedetail + " a " + "         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addOrder("trx_id " + this.stSort);

    boolean bshowRevers = Tools.isYes(this.showReverse);

    sqa.addClause("ref_trx_type = 'PB'");
    if (!bshowRevers) {
      sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
    }
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.accountCode != null)
    {
      sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
      sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar("% " + this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar(this.transNumber.toUpperCase());
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");

      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("upper(a.description) like ?");
      sqa.addPar("%" + this.description.toUpperCase() + "%");
    }
    if ((this.stPayment != null) &&
      (Tools.isYes(this.stPayment)))
    {
      sqa.addClause("a.ref_trx_type = ?");
      sqa.addPar("RCP");
    }
    sqa.setLimit(300);

    return sqa;
  }

  public String getStPeriod()
  {
    return this.stPeriod;
  }

  public void setStPeriod(String stPeriod)
  {
    this.stPeriod = stPeriod;
  }

  public String getStDivision()
  {
    return this.stDivision;
  }

  public void setStDivision(String stDivision)
  {
    this.stDivision = stDivision;
  }

  public String getStDivisionDesc()
  {
    return this.stDivisionDesc;
  }

  public void setStDivisionDesc(String stDivisionDesc)
  {
    this.stDivisionDesc = stDivisionDesc;
  }

  public void printTitipanExcel()
    throws Exception
  {
    DTOList l = EXCEL_TITIPAN();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_TITIPAN();
  }

  public DTOList EXCEL_TITIPAN()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, (select coalesce(SUM(z.titipan_premi_used_amount),0)   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi a  inner join gl_accounts b on b.account_id = a.accountid ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      }
      else
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(this.transdatefrom);
      }
    }
    else
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql = "select a.* from ( " + sqa.getSQL() + " ) a ";
    if (getPrintType().equalsIgnoreCase("1")) {
      sql = sql + " where (round(amount,2)-round(realisasi_used,2)) <> 0 ";
    }
    sql = sql + " order by a.trx_id desc,a.trx_no ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_TITIPAN()
    throws Exception
  {
    HSSFWorkbook wb = new HSSFWorkbook();

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    int page = 0;
    int baris = 0;
    int j = 0;
    int jumlahBarisPerSheet = 40000;

    BigDecimal sisa = new BigDecimal(0);
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);
      if (i % jumlahBarisPerSheet == 0)
      {
        page += 1;
        HSSFSheet sheet = wb.createSheet("titipan" + page);
        baris = 0;
      }
      HSSFRow row2 = wb.getSheet("titipan" + page).createRow(1);
      if (getApplyDateFrom() != null) {
        row2.createCell(0).setCellValue("Tanggal Titipan : " + DateUtil.getDateStr(getApplyDateFrom()) + " sd " + DateUtil.getDateStr(getApplyDateTo()));
      }
      HSSFRow row1 = wb.getSheet("titipan" + page).createRow(2);
      row1.createCell(0).setCellValue("NOBUK");
      row1.createCell(1).setCellValue("KTR");
      row1.createCell(2).setCellValue("TGL TITIP");
      row1.createCell(3).setCellValue("NOREK");
      row1.createCell(4).setCellValue("NOPOL");
      row1.createCell(5).setCellValue("KODA");
      row1.createCell(6).setCellValue("NILAI");
      row1.createCell(7).setCellValue("SISA");
      row1.createCell(8).setCellValue("TGL ENTRY");
      row1.createCell(9).setCellValue("KODE");
      row1.createCell(10).setCellValue("KETERANGAN");
      row1.createCell(11).setCellValue("NAMA BANK");

      sisa = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));

      HSSFRow row = wb.getSheet("titipan" + page).createRow(baris + 3);
      if (h.getFieldValueByFieldNameST("trx_no") != null) {
        row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      }
      if (h.getFieldValueByFieldNameBD("counter") != null) {
        row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("applydate") != null) {
        row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      }
      if (h.getFieldValueByFieldNameST("hdr_accountno") != null) {
        row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      }
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      if (h.getFieldValueByFieldNameST("cc_code") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
      }
      if (h.getFieldValueByFieldNameBD("amount") != null) {
        row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      }
      if (sisa != null) {
        row.createCell(7).setCellValue(sisa.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("create_date") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("create_date"));
      }
      if (h.getFieldValueByFieldNameST("cause") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("cause"));
      }
      if (h.getFieldValueByFieldNameST("description") != null) {
        row.createCell(10).setCellValue(JSPUtil.xmlEscape(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description"))));
      }
      if (h.getFieldValueByFieldNameST("description_master") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      }
      baris += 1;
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan_" + System.currentTimeMillis() + ".xls;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public SQLAssembler getSQARECAP()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("date_trunc('day',a.applydate) as applydate, sum(a.debit) as debit,sum(a.credit) as credit, substr(b.accountno,1,5)||'0000000'||substr(b.accountno,13,3) as accountno,a.trx_no,d.value_string as description,c.details_size as pol_id ");

    sqa.addQuery(" from " + gljedetail + " a " + " inner join gl_accounts b on b.account_id = a.accountid " + " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint " + " left join gl_accounts_header d on substr(d.acc_hdr_id,9,5) = substr(b.accountno,1,5) ");

    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (!bshowRevers) {
      sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
    }
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.stAccountID != null)
    {
      sqa.addClause("a.accountid = ?");
      sqa.addPar(this.stAccountID);
    }
    if (this.accountCodeLike != null)
    {
      int l = this.accountCodeLike.length();
      sqa.addClause("b.accountno like ? ");
      sqa.addPar(this.accountCodeLike + '%');
    }
    if (this.method != null)
    {
      sqa.addClause("substr(a.trx_no,1,1) = ? ");
      sqa.addPar(this.method);
    }
    if (this.branch != null)
    {
      sqa.addClause(" substr(b.accountno,14,2) = ? ");
      sqa.addPar(this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber.toUpperCase() + "%");
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("upper(a.description) like ?");
      sqa.addPar("%" + this.description.toUpperCase() + "%");
    }
    if ((this.stPayment != null) &&
      (Tools.isYes(this.stPayment)))
    {
      sqa.addClause("a.ref_trx_type = ?");
      sqa.addPar("RCP");
    }
    if (this.stPolicyTypeID != null)
    {
      String poltypeFilter = this.stPolicyTypeID.length() < 2 ? "0" + this.stPolicyTypeID : this.stPolicyTypeID;
      sqa.addClause("substr(pol_no,3,2) = '" + poltypeFilter + "'");
    }
    return sqa;
  }

  public SQLAssembler getSQACashBankRECAP()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("date_trunc('day',a.applydate) as applydate, sum(a.debit) as debit,sum(a.credit) as credit, substr(b.accountno,1,5)||'0000000'||substr(b.accountno,13,3) as accountno,a.trx_no,d.value_string as description ");

    sqa.addQuery(" from " + gljedetail + " a " + " inner join gl_accounts b on b.account_id = a.accountid  " + " left join gl_accounts_header d on substr(d.acc_hdr_id,9,5) = substr(b.accountno,1,5) ");

    sqa.addClause("a.journal_code is not null");
    sqa.addClause("a.ref_trx_type = 'CASHBANK'");

    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (!bshowRevers) {
      sqa.addClause("coalesce(a.reverse_flag,'') <> 'Y'");
    }
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.accountCode != null)
    {
      sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
      sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar("% " + this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber.toUpperCase() + "%");
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("upper(a.description) like ?");
      sqa.addPar("%" + this.description.toUpperCase() + "%");
    }
    sqa.setLimit(400);

    return sqa;
  }

  public boolean isEnableSuperEdit()
  {
    return this.enableSuperEdit;
  }

  public void setEnableSuperEdit(boolean enableSuperEdit)
  {
    this.enableSuperEdit = enableSuperEdit;
  }

  private GeneralLedger getRemoteGeneralLedger()
    throws NamingException, ClassNotFoundException, CreateException, RemoteException
  {
    return ((GeneralLedgerHome)JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
  }

  public void createApproveTitipan()
    throws Exception
  {
    SQLUtil S = new SQLUtil();

    DTOList listPolicy = null;
    listPolicy = ListUtil.getDTOListFromQuery(" select * from ar_titipan_premi  where approved is null and create_who <> 'doni'  order by trx_id limit 10000 ", TitipanPremiView.class);

    getRemoteGeneralLedger().saveJournalDirectTitipanPremi(listPolicy);
  }

  public Date getEntrydatefrom()
  {
    return this.entrydatefrom;
  }

  public void setEntrydatefrom(Date entrydatefrom)
  {
    this.entrydatefrom = entrydatefrom;
  }

  public Date getEntrydateto()
  {
    return this.entrydateto;
  }

  public void setEntrydateto(Date entrydateto)
  {
    this.entrydateto = entrydateto;
  }

  public String getStEntityID()
  {
    return this.stEntityID;
  }

  public void setStEntityID(String stEntityID)
  {
    this.stEntityID = stEntityID;
  }

  public String getStEntityName()
  {
    return this.stEntityName;
  }

  public void setStEntityName(String stEntityName)
  {
    this.stEntityName = stEntityName;
  }

  public String getStDescription()
  {
    return this.stDescription;
  }

  public void setStDescription(String stDescription)
  {
    this.stDescription = stDescription;
  }

  public void btnPrintExcel() throws Exception {

        if (accountCode != null) {
            if (accountCode.length() < 15) {
                throw new RuntimeException("Digit Akun Tidak Lengkap (15 Digit)");
            }
        }

        final DTOList l = EXCEL_MUTATION();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_MUTATION();
    }

  public void clickPrintExcel() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        final DTOList l = loadListExcel();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        exportExcel();
    }

    public void exportExcel() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_JOURNAL", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String exportID = (String) refPropMap.get("EXPORT");

        final String fileName = LanguageManager.getInstance().translate(LOVManager.getInstance().getDescriptionLOV("PROD_JOURNAL", stReport), getStLang());

        setStFileName(fileName);

        final Method m = this.getClass().getMethod(exportID, null);

        m.invoke(this, null);
    }

  private DTOList loadListExcel()
    throws Exception
  {
    this.ref1 = LOVManager.getInstance().getRef1("PROD_JOURNAL", this.stReport);

    this.refPropMap = Tools.getPropMap(this.ref1);

    String queryID = (String)this.refPropMap.get("QUERY_EXCEL");

    this.status = ((String)this.refPropMap.get("STATUS"));
    if (queryID == null) {
      return null;
    }
    Method m = getClass().getMethod(queryID, null);

    return (DTOList)m.invoke(this, null);
  }

  public DTOList EXCEL_POSITION()
    throws Exception
  {
    boolean FLT_BANK = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_BANK"));
    boolean FLT_KAS = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_KAS"));
    boolean FLT_KLAIM = "Y".equalsIgnoreCase((String)this.refPropMap.get("FLT_KLAIM"));

    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String fld = "bal";
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.account_id as accountid,b.accountno as hdr_accountno,b.noper,substr(b.description,32,50) as trx_no,a.period_year as fiscal_year, b.description,substr(b.accountno,14,2) as ccy_code,';'||b.rekno as ref_trx_type, d.description as jenas,c.description as koda,sum(coalesce(bal" + getLPeriodTo() + ",0)) as entered_debit, " + "coalesce(round(sum(" + plist + "),2),0) as balance");

    sqa.addQuery(" from gl_accounts b left join gl_acct_bal2 a on b.account_id = a.account_id  left join gl_cost_center c on c.cc_code = substr(b.accountno,14,2)  left join ins_pol_types d on d.vx = substr(b.accountno,11,2)");

    sqa.addClause("b.acctype is null");
    if (FLT_BANK) {
      sqa.addClause("((substr(b.accountno,1,3)) between '122' and '122')");
    }
    if (FLT_KAS) {
      sqa.addClause("((substr(b.accountno,1,3)) between '121' and '121')");
    }
    if (FLT_KLAIM) {
      sqa.addClause("((substr(b.accountno,1,2)) between '33' and '33')");
    }
    if (this.yearFrom != null)
    {
      sqa.addClause("(a.period_year between ? and ?)");
      sqa.addPar(this.yearFrom);
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    sqa.addClause("a.idr_flag = 'Y'");

    String sql = null;
    if (FLT_KLAIM) {
      sql = "select x.noper as pol_no,x.jenas,x.koda,x.fiscal_year,sum(x.balance) as balance from ( " + sqa.getSQL() + " " + " group by a.account_id,a.period_year,b.accountno,b.noper,b.description,b.rekno,c.description,d.description   " + " ) x where balance <> 0 group by x.noper,x.jenas,x.fiscal_year,x.koda order by x.noper,x.koda,x.jenas ";
    }
    if ((FLT_BANK) || (FLT_KAS)) {
      sql = "select * from ( " + sqa.getSQL() + " group by a.account_id,a.period_year,b.accountno,b.noper,b.description,b.rekno,c.description,d.description " + " ) x where balance <> 0 order by ccy_code,noper,hdr_accountno ";
    }
    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_POSITION()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    String description = null;

    int month1 = Integer.parseInt(getPeriodTo());
    String bulan = DateUtil.getMonth2(month1);
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row0 = sheet.createRow(0);
      row0.createCell(0).setCellValue("id");
      row0.createCell(1).setCellValue("akun");
      row0.createCell(2).setCellValue("noper");
      row0.createCell(3).setCellValue("keterangan");
      row0.createCell(4).setCellValue("koda");
      row0.createCell(5).setCellValue("rekno");
      row0.createCell(6).setCellValue("nilai");
      row0.createCell(7).setCellValue("keterangan");
      if (Tools.isEqual(h.getFieldValueByFieldNameBD("entered_debit"), new BigDecimal(0))) {
        description = "Dari Buku Tambahan";
      } else {
        description = "Saldo R/C " + bulan + " " + getYearFrom();
      }
      XSSFRow row = sheet.createRow(i + 1);
      row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("accountid").doubleValue());
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      if (h.getFieldValueByFieldNameST("noper") != null) {
        row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("noper"));
      }
      row.createCell(3).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("koda"));
      if (h.getFieldValueByFieldNameST("ref_trx_type") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("ref_trx_type"));
      }
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("balance").doubleValue());
      row.createCell(7).setCellValue(description);
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public String getStFileName()
  {
    return this.stFileName;
  }

  public void setStFileName(String stFileName)
  {
    this.stFileName = stFileName;
  }

  public void btnClickPrint()
    throws Exception
  {
    super.redirect("/pages/gl/report/rpt_realisasititipan.fop");
  }

  public void btnClickPrintOS()
    throws Exception
  {
    super.redirect("/pages/gl/report/rpt_ostitipan.fop");
  }

  public SQLAssembler getSQARealisasiTitipan()
  {
    SQLAssembler sqa = new SQLAssembler();

    String sql = "a.*,b.receipt_no as nobuk,b.receipt_date as create_date, b.titipan_premi_amount as realisasi,b.titipan_premi_used_amount as realisasi_used,  (b.titipan_premi_amount-b.titipan_premi_used_amount) as entered_debit,c.posted_flag as approved ";

    sqa.addSelect(sql);

    sqa.addQuery(" from ar_titipan_premi a  left join ar_receipt_lines b on b.titipan_premi_id = a.trx_id  left join ar_receipt c on c.ar_receipt_id = b.receipt_id ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.realisasifrom != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) >= ? ");
      sqa.addPar(this.realisasifrom);
    }
    if (getRealisasito() != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(getRealisasito());
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    sqa.addOrder("a.trx_no,a.counter,c.posted_flag desc,b.ar_rcl_id");
    return sqa;
  }

  public String getStNotApproved()
  {
    return this.stNotApproved;
  }

  public void setStNotApproved(String stNotApproved)
  {
    this.stNotApproved = stNotApproved;
  }

 public void btnPrintExcelFile() throws Exception {

        if (accountCode != null) {
            if (accountCode.length() < 15) {
                throw new RuntimeException("Digit Akun Tidak Lengkap (15 Digit)");
            }
        }

        EXCEL_MUTATION_FILE();
    }

public DTOList EXCEL_MUTATION() throws Exception {

        String gljedetail = null;

        if (transdatefrom != null) {
            String gljedetailYear = DateUtil.getYear(transdatefrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }
        } else {
            gljedetail = "gl_je_detail";
        }

        final SQLAssembler sqa = new SQLAssembler();

        String methodChoiceSelect = null;
        if (method != null) {
            if (method.equalsIgnoreCase("N")) {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,c.refz1 as treaty,owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
            } else if (method.equalsIgnoreCase("Q")) {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,coalesce(c.refid0,d.refid0) as treaty,owner_code as pemilik,"
                        + "user_code as pengguna,quote_ident(recap_no) as recap_no ";
            } else {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,';'||a.pol_no as pol_no,getsumbis(substr(a.pol_no,2,1)) as sumbis,"
                        + "owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
            }
        } else {
            methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                    + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                    + "a.debit,a.credit,';'||a.pol_no as pol_no,getsumbis(substr(a.pol_no,2,1)) as sumbis,"
                    + "owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
        }

        sqa.addSelect(methodChoiceSelect);

        /*sqa.addSelect(" row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
        + "a.debit,a.credit,c.refz1 as treaty ");*/

        String methodChoiceQuery = null;
        if (method != null) {
            if (method.equalsIgnoreCase("N")) {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid "
                        + " left join ar_invoice c on c.ar_invoice_id = a.ref_trx_no::bigint ";
            } else if (method.equalsIgnoreCase("Q")) {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid "
                        + " left join ins_pol_inward c on c.ar_invoice_id = a.ref_trx_no::bigint "
                        + " inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id and d.ar_trx_line_id in (6,74,75,81,108,126,128) ";
            } else {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid ";
            }
        } else {
            methodChoiceQuery = "   from " + gljedetail + " a "
                    + " inner join gl_accounts b on b.account_id = a.accountid ";
        }

        sqa.addQuery(methodChoiceQuery);

        /*sqa.addQuery("   from " + gljedetail + " a"
        + " inner join gl_accounts b on b.account_id = a.accountid "
        + " left join ar_invoice c on c.ar_invoice_id = a.ref_trx_no::bigint ");*/

        if (stRecapitulationNo != null) {
//            sqa.addClause("a.recap_no = '" + stRecapitulationNo + "' ");
            sqa.addClause("a.recap_no = ? ");
            sqa.addPar(stRecapitulationNo.toUpperCase());
        }

        if (policyNo != null) {
//            sqa.addClause("a.pol_no like '" + policyNo.toUpperCase() + "'");
            sqa.addClause("a.pol_no = ? ");
            sqa.addPar(policyNo);
        }

        if (stAccountID != null) {
            sqa.addClause("a.accountid = ?");
            sqa.addPar(stAccountID);
        }

        if (accountCodeLike != null) {
            int l = accountCodeLike.length();
            sqa.addClause("b.accountno like ? ");
            sqa.addPar(accountCodeLike + '%');
        }

        if (accountCode != null) {
            sqa.addClause("b.accountno = ? ");
            sqa.addPar(accountCode);
        }

        if (branch != null) {
//            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

//        if (transNumber!=null) {
//            sqa.addClause("(a.trx_no = '"+ transNumber.toUpperCase() +"' or a.trx_no like '"+ transNumber.toUpperCase()+"%" +"')");
//        }

        if (transNumber != null) {
//            sqa.addClause("a.trx_no = '" + transNumber.toUpperCase() + "'");
            sqa.addClause("a.trx_no = ?");
            sqa.addPar(transNumber.toUpperCase());
        }

        if (transNumberLike != null) {
//            sqa.addClause("a.trx_no like '" + transNumberLike.toUpperCase() + "%'");
            sqa.addClause("a.trx_no like ? ");
            sqa.addPar(transNumberLike.toUpperCase() + "%");
        }

        if (transdatefrom != null) {
//            sqa.addClause("date_trunc('day',a.applydate) >= '" + transdatefrom + "'");
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(transdatefrom);
        }

        if (transdateto != null) {
//            sqa.addClause("date_trunc('day',a.applydate) <= '" + transdateto + "'");
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(transdateto);
        }

        if (stPolicyTypeID != null) {
            String poltypeFilter = stPolicyTypeID.length() < 2 ? "0" + stPolicyTypeID : stPolicyTypeID;
            sqa.addClause("substr(pol_no,3,2) = '" + poltypeFilter + "'");
        }

        if (method != null) {
            String inv = "";
            if (method.equalsIgnoreCase("G,H,I")) {
                inv = "('G','H','I')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            } else if (method.equalsIgnoreCase("K,L,N")) {
                inv = "('K','L','N')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            } else if (method.equalsIgnoreCase("Q")) {
                inv = "('Q')";
            } else {
                inv = "('" + method + "')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            }
            //sqa.addPar(inv);
        }

        if (transdate != null) {
//            sqa.addClause("date_trunc('day',a.applydate) = '" + transdate + "'");
            sqa.addClause("date_trunc('day',a.applydate) = ?");
            sqa.addPar(transdate);
        }

        if (description != null) {
//            sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english','" + description.toUpperCase().replaceAll(" ", "&") + "')");
            sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
            sqa.addPar(description.toUpperCase().replaceAll(" ", "&"));
        }

        if (stPayment != null) {
            if (Tools.isYes(stPayment)) {
                sqa.addClause("a.ref_trx_type = 'RCP'");
                //sqa.addPar("RCP");
            }
        }

        String sql = sqa.getSQL() + " order by a.trx_id asc ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_MUTATION() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
//        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        int page = 0;
        int baris = 0;
        int j = 0;
        int jumlahBarisPerSheet = 1000000;

        int norut = 0;
        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            norut++;

            //bikin sheet fleksibel
            if (i % jumlahBarisPerSheet == 0) {
                page = page + 1;
                XSSFSheet sheet = wb.createSheet("mutasi_" + page);
                baris = 0;
            }

            //bikin header
//            XSSFRow row0 = sheet.createRow(0);
            XSSFRow row1 = wb.getSheet("mutasi_" + page).createRow(0);
            row1.createCell(0).setCellValue("No");
            row1.createCell(1).setCellValue("Tanggal");
            row1.createCell(2).setCellValue("No Bukti");
            row1.createCell(3).setCellValue("Norek");
            row1.createCell(4).setCellValue("Keterangan");
            row1.createCell(5).setCellValue("Debit");
            row1.createCell(6).setCellValue("Kredit");
            row1.createCell(7).setCellValue("Polis");
            row1.createCell(8).setCellValue("Sumbis");
            row1.createCell(9).setCellValue("Pengguna");
            row1.createCell(10).setCellValue("Pemilik");
            row1.createCell(11).setCellValue("No Rekap");
            if (method != null) {
                row1.createCell(12).setCellValue("Treaty");
            }

            //bikin isi cell
//            XSSFRow row = sheet.createRow(i + 1);
            XSSFRow row = wb.getSheet("mutasi_" + page).createRow(baris + 1);
            row.createCell(0).setCellValue(norut);
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("tanggal"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("nobuk"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("norek"));
            if (h.getFieldValueByFieldNameST("keterangan") != null) {
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("keterangan").replaceAll(",", "."));
            }
            if (h.getFieldValueByFieldNameBD("debit") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("credit") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            }
            if (h.getFieldValueByFieldNameST("pol_no") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            }
            if (h.getFieldValueByFieldNameST("sumbis") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("sumbis"));
            }
            if (h.getFieldValueByFieldNameST("pengguna") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pengguna"));
            }
            if (h.getFieldValueByFieldNameST("pemilik") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("pemilik"));
            }
            if (h.getFieldValueByFieldNameST("recap_no") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("recap_no"));
            }
            if (method != null) {
                if (h.getFieldValueByFieldNameST("treaty") != null) {
                    row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("treaty"));
                }
            }

            baris = baris + 1;
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan" + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

        resetField();

    }


  public String getStAccountID()
  {
    return this.stAccountID;
  }

  public void setStAccountID(String stAccountID)
  {
    this.stAccountID = stAccountID;
  }

  public DTOList getListUW()
    throws Exception
  {
    if (this.listUW == null)
    {
      this.listUW = new DTOList();
      this.listUW.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAUW();

    sqa.addFilter(this.listUW.getFilter());

    this.listUW = sqa.getList(JournalView.class);

    return this.listUW;
  }

  public SQLAssembler getSQAUW()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      if (gljedetailYear.equalsIgnoreCase(getYearPosting())) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from " + gljedetail + " a " + "         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addClause("b.accountno like '79000%'");
    sqa.addOrder("trx_id " + this.stSort);

    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.accountCode != null)
    {
      sqa.addClause("(upper(b.accountno) like ? or upper(b.description) like ?)");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
      sqa.addPar("%" + this.accountCode.toUpperCase() + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar("% " + this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar(this.transNumber.toUpperCase());
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");

      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
      sqa.addPar(this.description.toUpperCase().replaceAll(" ", "&"));
    }
    if ((this.stPayment != null) &&
      (Tools.isYes(this.stPayment)))
    {
      sqa.addClause("a.ref_trx_type = ?");
      sqa.addPar("RCP");
    }
    if ((this.policyNo == null) && (this.description == null) && (this.transNumber == null) && (this.accountCode == null) && (this.transdatefrom == null)) {
      sqa.setLimit(100);
    } else {
      sqa.setLimit(700);
    }
    return sqa;
  }

  public void setListUW(DTOList listUW)
  {
    this.listUW = listUW;
  }

  public void btnPrintExcelUW()
    throws Exception
  {
    DTOList l = EXCEL_MUTATION_UW();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_MUTATION_UW();
  }

  public DTOList EXCEL_MUTATION_UW()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      if (gljedetailYear.equalsIgnoreCase(getYearPosting())) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno");

    sqa.addQuery("   from " + gljedetail + " a" + "         inner join gl_accounts b on b.account_id = a.accountid");

    sqa.addClause("b.accountno like '79000%'");
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.transdateto);
    }
    String sql = sqa.getSQL() + " order by a.trx_id asc ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_MUTATION_UW()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    int norut = 0;
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      norut++;

      XSSFRow row1 = sheet.createRow(0);
      row1.createCell(0).setCellValue("No");
      row1.createCell(1).setCellValue("Tanggal");
      row1.createCell(2).setCellValue("No Bukti");
      row1.createCell(3).setCellValue("Norek");
      row1.createCell(4).setCellValue("Keterangan");
      row1.createCell(5).setCellValue("Debit");
      row1.createCell(6).setCellValue("Kredit");

      XSSFRow row = sheet.createRow(i + 1);
      row.createCell(0).setCellValue(norut);
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("description"));
      row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan.xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public DTOList LAJUR()
    throws Exception
  {
    boolean LAJUR_BANK = "Y".equalsIgnoreCase((String)this.refPropMap.get("LAJUR_BANK"));

    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String db = "db";
    String cr = "cr";
    String fld = "bal";
    String dbplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1L, db, "+");
    String crplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1L, cr, "+");
    String dbplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), db, "+");
    String crplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), cr, "+");
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("b.noper,b.account_id,b.accountno,b.description,(" + dbplist2 + ") as entered_debit," + "(" + crplist2 + ") as entered_credit," + "coalesce(db" + this.periodTo + ",0) as debit," + "coalesce(cr" + this.periodTo + ",0) as credit," + "(" + plist + ") as balance," + "(" + dbplist + ") as premi_treaty," + "(" + crplist + ") as premi_fact");

    sqa.addQuery(" from gl_acct_bal2 a  inner join gl_accounts b on b.account_id = a.account_id ");

    sqa.addClause("b.acctype is null");
    sqa.addClause("a.idr_flag = 'Y'");
    if (LAJUR_BANK) {
      sqa.addClause("substr(b.accountno,1,5) in ('12210','12220','12221','11121','11122','48921','48922')");
    }
    if (this.yearFrom != null)
    {
      sqa.addClause("a.period_year = ? ");
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    if (!LAJUR_BANK)
    {
      if (this.akunTo != null)
      {
        sqa.addClause("((substr(b.accountno,1," + this.akunFrom.length() + ")) between ? and ?)");
        sqa.addPar(this.akunFrom);
        sqa.addPar(this.akunTo);
      }
      if (this.akunTo == null)
      {
        sqa.addClause("((substr(b.accountno,1," + this.akunFrom.length() + ")) between ? and ?)");
        sqa.addPar(this.akunFrom);
        sqa.addPar(this.akunFrom);
      }
    }
    String sql = sqa.getSQL() + " order by a.accountno ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void printRealisasiTitipanExcel()
    throws Exception
  {
    DTOList l = EXCEL_REALISASI_TITIPAN();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_REALISASI_TITIPAN();
  }

  public DTOList EXCEL_REALISASI_TITIPAN()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*,b.receipt_date,b.receipt_no as nobuk_realisasi,b.titipan_premi_amount as realisasi,b.titipan_premi_used_amount as realisasi_used, (b.titipan_premi_amount-b.titipan_premi_used_amount) as sisa,c.posted_flag, (select count(x.titipan_premi_id) from ar_receipt_lines x where x.titipan_premi_id = a.trx_id) as jumlah ");

    sqa.addQuery(" from ar_titipan_premi a  left join ar_receipt_lines b on b.titipan_premi_id = a.trx_id  left join ar_receipt c on c.ar_receipt_id = b.receipt_id ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.realisasifrom != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) >= ? ");
      sqa.addPar(this.realisasifrom);
    }
    if (getRealisasito() != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(getRealisasito());
    }
    if (this.perdate != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(this.perdate);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql2 = sqa.getSQL() + " order by a.trx_no,a.counter,c.posted_flag desc,b.ar_rcl_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql2, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_REALISASI_TITIPAN()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    BigDecimal a1 = new BigDecimal(0);
    BigDecimal b1 = new BigDecimal(0);
    BigDecimal c1 = new BigDecimal(0);
    String status = null;
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row1 = sheet.createRow(0);
      row1.createCell(0).setCellValue("REALISASI TITIPAN PREMI");

      XSSFRow row0 = sheet.createRow(2);
      row0.createCell(0).setCellValue("trxid");
      row0.createCell(1).setCellValue("nobuk titipan");
      row0.createCell(2).setCellValue("ktr");
      row0.createCell(3).setCellValue("noper");
      row0.createCell(4).setCellValue("naper");
      row0.createCell(5).setCellValue("nopol");
      row0.createCell(6).setCellValue("nilai");
      row0.createCell(7).setCellValue("tgltitip");
      row0.createCell(8).setCellValue("nobuk realisasi");
      row0.createCell(9).setCellValue("terpakai");
      row0.createCell(10).setCellValue("sisa");
      row0.createCell(11).setCellValue("tglrealisasi");
      row0.createCell(12).setCellValue("keterangan");
      row0.createCell(13).setCellValue("efektif");
      if (i == 0)
      {
        a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
      }
      else if (i > 0)
      {
        HashDTO s = (HashDTO)list.get(i - 1);
        String x1 = s.getFieldValueByFieldNameBD("trx_id").toString();
        String x2 = h.getFieldValueByFieldNameBD("trx_id").toString();
        if (x1.equalsIgnoreCase(x2))
        {
          b1 = BDUtil.add(b1, s.getFieldValueByFieldNameBD("realisasi_used"));
          c1 = BDUtil.add(b1, h.getFieldValueByFieldNameBD("realisasi_used"));
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), c1);
        }
        else if (!x1.equalsIgnoreCase(x2))
        {
          b1 = new BigDecimal(0);
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
        }
      }
      if (h.getFieldValueByFieldNameST("posted_flag") != null)
      {
        if (Tools.isYes(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "Y";
        } else if (Tools.isNo(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "BELUM SETUJUI";
        }
      }
      else {
        status = "";
      }
      XSSFRow row = sheet.createRow(i + 3);
      row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("trx_id").doubleValue());
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      if (h.getFieldValueByFieldNameST("nobuk_realisasi") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("nobuk_realisasi"));
      }
      if (h.getFieldValueByFieldNameBD("realisasi_used") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("realisasi_used").doubleValue());
      }
      if (h.getFieldValueByFieldNameBD("sisa") != null) {
        row.createCell(10).setCellValue(a1.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
      }
      row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
      row.createCell(13).setCellValue(status);
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=realisasi_titipan_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public void printRincianTitipanExcel()
    throws Exception
  {
    EXCEL_RINCIAN_TITIPAN2();
  }

  public void EXCEL_RINCIAN_TITIPAN2()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    String sql = "a.trx_id,a.trx_no as titipan_premi,a.counter as ktr,a.applydate as tgltitip,d.accountno as norek,';'||a.pol_no as nopol,a.cc_code as koda,a.amount as nilai,(select coalesce(SUM(y.titipan_premi_used_amount),0) from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id where x.status = 'POST' AND y.titipan_premi_id = a.trx_id and date_trunc('day',y.receipt_date) <= '" + getPerdate() + "' ) as terpakai," + "a.create_date as tglentry,a.cause as kode,a.description as keterangan,a.description_master as bank, " + "(select string_agg(y.receipt_no,'|') from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id " + "where x.status = 'POST' and x.posted_flag = 'Y' AND y.titipan_premi_id = a.trx_id and date_trunc('day',y.receipt_date) > '" + getPerdate() + "' ) as nobuk ";

    sqa.addSelect(sql);

    sqa.addQuery(" from ar_titipan_premi a left join gl_accounts d on d.account_id = a.hdr_accountid ");
    if (Tools.isYes(this.stNotApproved)) {
      sqa.addClause("coalesce(a.approved,'') <> 'Y'");
    } else {
      sqa.addClause(" a.approved = 'Y' ");
    }
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0) {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      } else {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + this.transdatefrom + "'");
      }
    }
    else {
      sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null) {
      sqa.addClause(" date_trunc('day',a.applydate) <= '" + this.transdateto + "'");
    }
    if (this.entrydatefrom != null) {
      sqa.addClause(" date_trunc('day',a.create_date) >= '" + this.entrydatefrom + "'");
    }
    if (this.entrydateto != null) {
      sqa.addClause(" date_trunc('day',a.create_date) <= '" + this.entrydateto + "'");
    }
    if (this.stEntityID != null) {
      sqa.addClause("a.hdr_accountid = '" + this.stEntityID + "'");
    }
    if (this.transNumber != null) {
      sqa.addClause("a.trx_no like '%" + this.transNumber + "%'");
    }
    if (this.description != null) {
      sqa.addClause("a.description like '%" + this.description + "%'");
    }
    if (this.branch != null) {
      sqa.addClause("a.cc_code = '" + this.branch + "'");
    }
    if (this.stCreateWho != null) {
      sqa.addClause("a.create_who = '" + this.stCreateWho + "'");
    }
    String sql2 = "select titipan_premi,ktr,tgltitip,norek,nopol,koda,nilai,(nilai-terpakai) as sisa,tglentry,kode,keterangan,bank,nobuk  from ( " + sqa.getSQL() + " order by a.trx_no,a.counter " + " ) a where (nilai-terpakai) <> 0 order by titipan_premi,ktr ";

    SQLUtil S = new SQLUtil();

    String nama_file = "rincian_titipan_" + System.currentTimeMillis() + ".csv";

    sql2 = "Copy (" + sql2 + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

    PreparedStatement ps = S.setQuery(sql2);

    boolean tes = ps.execute();

    S.release();

    File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
    int length = 0;
    ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

    SessionManager.getInstance().getResponse().setContentType("text/csv");
    SessionManager.getInstance().getResponse().setContentLength((int)file.length());

    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

    int BUFSIZE = 4096;
    byte[] byteBuffer = new byte[BUFSIZE];
    DataInputStream in = new DataInputStream(new FileInputStream(file));
    while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
      outStream.write(byteBuffer, 0, length);
    }
    in.close();
    outStream.close();

    file.delete();
  }

  public DTOList EXCEL_RINCIAN_TITIPAN()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    String sql = "a.trx_id,a.trx_no as titipan_premi,a.counter as ktr,a.applydate as tgltitip,d.accountno as norek,';'||a.pol_no as nopol,d.cc_code as koda,a.amount as nilai,(select coalesce(SUM(y.titipan_premi_used_amount),0) from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id where x.status = 'POST' AND y.titipan_premi_id = a.trx_id and date_trunc('day',y.receipt_date) <= '" + getPerdate() + "' ) as terpakai," + "a.create_date as tglentry,a.cause as kode,a.description as keterangan,a.description_master as bank ";

    sqa.addSelect(sql);

    sqa.addQuery(" from ar_titipan_premi a left join gl_accounts d on d.account_id = a.hdr_accountid ");
    if (Tools.isYes(this.stNotApproved)) {
      sqa.addClause("coalesce(a.approved,'') <> 'Y'");
    } else {
      sqa.addClause(" a.approved = 'Y' ");
    }
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      }
      else
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(this.transdatefrom);
      }
    }
    else
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.create_date) >= ?");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause("date_trunc('day',a.create_date) <= ?");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    if (this.stCreateWho != null)
    {
      sqa.addClause("a.create_who = ?");
      sqa.addPar(this.stCreateWho);
    }
    String sql2 = "select titipan_premi,ktr,tgltitip,norek,nopol,koda,nilai,(nilai-terpakai) as sisa,tglentry,kode,keterangan,bank  from ( " + sqa.getSQL() + " order by a.trx_no,a.counter " + " ) a where (nilai-terpakai) <> 0 order by titipan_premi,ktr ";

    DTOList l = ListUtil.getDTOListFromQuery(sql2, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_RINCIAN_TITIPAN()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    BigDecimal sisa = null;
    String nobuktitipan = null;
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row0 = sheet.createRow(2);
      row0.createCell(0).setCellValue("titipan");
      row0.createCell(1).setCellValue("ktr");
      row0.createCell(2).setCellValue("tgl titip");
      row0.createCell(3).setCellValue("norek");
      row0.createCell(4).setCellValue("nopol");
      row0.createCell(5).setCellValue("koda");
      row0.createCell(6).setCellValue("nilai");
      row0.createCell(7).setCellValue("sisa");
      row0.createCell(8).setCellValue("tgl entry");
      row0.createCell(9).setCellValue("kode");
      row0.createCell(10).setCellValue("keterangan");
      row0.createCell(11).setCellValue("bank");

      XSSFRow row = sheet.createRow(i + 3);

      row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("titipan_premi"));
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("ktr").doubleValue());
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("tgltitip"));
      row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("norek"));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("nopol"));
      row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("koda"));
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
      row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("sisa").doubleValue());
      row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("tglentry"));
      row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("kode"));
      row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("keterangan"));
      row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("bank"));
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=rincian_titipan_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public String getPrintType()
  {
    return this.printType;
  }

  public void setPrintType(String printType)
  {
    this.printType = printType;
  }

  public String getMethod()
  {
    return this.method;
  }

  public void setMethod(String method)
  {
    this.method = method;
  }

  public DTOList getMemorialList()
    throws Exception
  {
    if (this.memorialList == null)
    {
      this.memorialList = new DTOList();
      this.memorialList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQAMemorial();

    sqa.addFilter(this.memorialList.getFilter());

    this.memorialList = sqa.getList(JournalView.class);

    return this.memorialList;
  }

  public SQLAssembler getSQAMemorial()
    throws Exception
  {
    String gljedetail = null;
    if (this.transdatefrom != null)
    {
      String gljedetailYear = DateUtil.getYear(this.transdatefrom);
      String gljedetailYearCurrent = DateUtil.getYear(new Date());
      if ((gljedetailYear.equalsIgnoreCase(getYearPosting())) || (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent))) {
        gljedetail = "gl_je_detail";
      } else {
        gljedetail = "gl_je_detail_" + gljedetailYear;
      }
    }
    else
    {
      gljedetail = "gl_je_detail";
    }
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*, b.accountno, c.user_name ");

    sqa.addQuery("   from " + gljedetail + " a " + "         inner join gl_accounts b on b.account_id = a.accountid" + "       left join s_users c on c.user_id = a.create_who ");

    sqa.addClause("ref_trx_type = 'MEMORIAL'");
    if (this.stSort != null) {
      sqa.addOrder("trx_id desc");
    }
    boolean bshowRevers = Tools.isYes(this.showReverse);
    if (this.policyNo != null)
    {
      sqa.addClause("a.pol_no = ? ");
      sqa.addPar(this.policyNo.toUpperCase());
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno like ?");
      sqa.addPar(this.accountCode.toUpperCase() + "%");
    }
    if (this.method != null)
    {
      String inv = "";
      if (this.method.equalsIgnoreCase("G,H,I")) {
        inv = "('G','H','I')";
      } else if (this.method.equalsIgnoreCase("K,L,N")) {
        inv = "('K','L','N')";
      } else {
        inv = "('" + this.method + "')";
      }
      sqa.addClause("substr(a.trx_no,1,1) in " + inv);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber.toUpperCase());
    }
    if (this.transdatefrom != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause("date_trunc('day',a.applydate) <= ?");

      sqa.addPar(this.transdateto);
    }
    if (this.transdate != null)
    {
      sqa.addClause("date_trunc('day',a.applydate)=?");
      sqa.addPar(this.transdate);
    }
    if (this.description != null)
    {
      sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
      sqa.addPar(this.description.toUpperCase().replaceAll(" ", "&"));
    }
    if ((this.stPayment != null) &&
      (Tools.isYes(this.stPayment)))
    {
      sqa.addClause("a.ref_trx_type = ?");
      sqa.addPar("RCP");
    }
    if ((this.policyNo == null) && (this.description == null) && (this.transNumber == null) && (this.accountCode == null) && (this.transdatefrom == null) && (this.transdateto == null)) {
      sqa.setLimit(100);
    }
    return sqa;
  }

  public void setMemorialList(DTOList memorialList)
  {
    this.memorialList = memorialList;
  }

  public void btnSearchTitipan()
    throws Exception
  {
    getTitipanPremiList().getFilter().setCurrentPage(0);
  }

  public void btnSyariahSearch()
    throws Exception
  {
    getSyariahList().getFilter().setCurrentPage(0);
  }

  public String getStPolicyTypeGroupID()
  {
    return this.stPolicyTypeGroupID;
  }

  public void setStPolicyTypeGroupID(String stPolicyTypeGroupID)
  {
    this.stPolicyTypeGroupID = stPolicyTypeGroupID;
  }

  public String getStPolicyTypeID()
  {
    return this.stPolicyTypeID;
  }

  public void setStPolicyTypeID(String stPolicyTypeID)
  {
    this.stPolicyTypeID = stPolicyTypeID;
  }

  public void onChangePolicyTypeGroup() {}

  public String getStRecapitulationNo()
  {
    return this.stRecapitulationNo;
  }

  public void setStRecapitulationNo(String stRecapitulationNo)
  {
    this.stRecapitulationNo = stRecapitulationNo;
  }

  public void btnSearchCashBank()
    throws Exception
  {
    getCashBanklist().getFilter().setCurrentPage(0);
  }

  public String getTransNumberLike()
  {
    return this.transNumberLike;
  }

  public void setTransNumberLike(String transNumberLike)
  {
    this.transNumberLike = transNumberLike;
  }

  public Date getPerdate()
  {
    return this.perdate;
  }

  public void setPerdate(Date perdate)
  {
    this.perdate = perdate;
  }

  public void btnRKAPSearch()
    throws Exception
  {
    getRkapList().getFilter().setCurrentPage(0);
  }

  public DTOList getRkapList()
    throws Exception
  {
    if (this.rkapList == null)
    {
      this.rkapList = new DTOList();
      this.rkapList.getFilter().activate();
    }
    SQLAssembler sqa = getRKAPSQA();

    sqa.addFilter(this.rkapList.getFilter());

    this.rkapList = sqa.getList(RKAPGroupView.class);

    return this.rkapList;
  }

 public SQLAssembler getRKAPSQA() {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from gl_rkap_group a ");
        sqa.addClause("a.active_flag = 'Y'");



        if (years != null) {
            sqa.addClause("a.years = ? ");
            sqa.addPar(years);


        }

        sqa.addOrder(" a.years desc,a.rkap_group_id asc ");

        sqa.setLimit(300);



        return sqa;


    }

  public String getYears()
  {
    return this.years;
  }

  public void setYears(String years)
  {
    this.years = years;
  }

  public String getAkun()
  {
    return this.akun;
  }

  public void setAkun(String akun)
  {
    this.akun = akun;
  }

  public DTOList EXCEL_LAJUR() throws Exception {
        final boolean LAJUR_BANK = "Y".equalsIgnoreCase((String) refPropMap.get("LAJUR_BANK"));

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String db = "db";
        String cr = "cr";
        String fld = "bal";
        String dbplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, db, "+");
        String crplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, cr, "+");
        String dbplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), db, "+");
        String crplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), cr, "+");
        String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "';'||b.noper as noper,b.account_id,b.accountno,b.description,"
                + "sum(" + dbplist2 + ") as debit_prev,"
                + "sum(" + crplist2 + ") as credit_prev,"
                + "sum(coalesce(db" + periodTo + ",0)) as debit,"
                + "sum(coalesce(cr" + periodTo + ",0)) as credit,"
                + "sum(" + plist + ") as balance,"
                + "sum(" + dbplist + ") as debit_next,"
                + "sum(" + crplist + ") as credit_next");

        sqa.addQuery(" from gl_acct_bal2 a "
                + " inner join gl_accounts b on b.account_id = a.account_id ");

        sqa.addClause("b.acctype is null");
        sqa.addClause("a.idr_flag = 'Y'");

        if (LAJUR_BANK) {
            sqa.addClause("substr(b.accountno,1,5) in ('12210','12220','12221','11121','11122','48921','48922')");
        }

        if (yearFrom != null) {
            sqa.addClause("a.period_year = '" + yearFrom + "' ");
//            sqa.addPar(yearFrom);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
//            sqa.addPar(branch);
        }
        /*
        if (akun != null) {
        sqa.addClause("b.accountno like ? ");
        sqa.addPar(akun + "%");
        }
         */

        if (!LAJUR_BANK) {
            if (akunTo != null) {
                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunTo + "')");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunTo);
            }

            if (akunTo == null) {
                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunFrom + "')");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunFrom);
            }
        }

//        String sql = "select * from ( " + sqa.getSQL() + " group by b.noper,b.account_id,b.accountno,b.description order by b.accountno asc "
//                + " ) a where debit_prev <> 0 or credit_prev <> 0 or debit <> 0 or credit <> 0 or debit_next <> 0 or credit_next <> 0 order by a.accountno asc ";
//
//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                HashDTO.class);
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
//
//        return l;

        SQLUtil S = new SQLUtil();

        String nama_file = "neraca_lajur_" + System.currentTimeMillis() + ".csv";

        int month1 = Integer.parseInt(getPeriodTo());

        int month2;
        if (Integer.parseInt(getPeriodTo()) == 1) {
            month2 = 1;
        } else {
            month2 = Integer.parseInt(getPeriodTo()) - 1;
        }

        String bulan = DateUtil.getMonth2(month1);
        String bulan2 = DateUtil.getMonth2(month2);

        String descPeriod = bulan + "_" + getYearFrom();
        String descPeriod2 = bulan2 + "_" + getYearFrom();

        String sql = "select accountno,description,debit_prev,credit_prev,debit,credit,debit_next,credit_next "
                + "from ( " + sqa.getSQL() + " group by b.noper,b.account_id,b.accountno,b.description order by b.accountno asc "
                + " ) a where debit_prev <> 0 or credit_prev <> 0 or debit <> 0 or credit <> 0 or debit_next <> 0 or credit_next <> 0 order by a.accountno asc ";

//        String sql = "select accountno as rekening,description as uraian,debit_prev as debit_per_" + descPeriod2 + ","
//                + "credit_prev as credit_per_" + descPeriod2 + ","
//                + "debit as debit_" + descPeriod + ","
//                + "credit as credit_" + descPeriod + ","
//                + "debit_next as debit_per_" + descPeriod + ","
//                + "credit_next as credit_per_" + descPeriod + " "
//                + "from ( " + sqa.getSQL() + " group by b.noper,b.account_id,b.accountno,b.description order by b.accountno asc "
//                + " ) a where debit_prev <> 0 or credit_prev <> 0 or debit <> 0 or credit <> 0 or debit_next <> 0 or credit_next <> 0 order by a.accountno asc ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

        /*sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();*/

    }

    public void EXPORT_LAJUR() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        int month1 = Integer.parseInt(getPeriodTo());

        int month2;
        if (Integer.parseInt(getPeriodTo()) == 1) {
            month2 = 1;
        } else {
            month2 = Integer.parseInt(getPeriodTo()) - 1;
        }

//        String bulan = getMonthTitleDescription2(Integer.toString(month1));
//        String bulan2 = getMonthTitleDescription2(Integer.toString(month2));

        String bulan = DateUtil.getMonth2(month1);
        String bulan2 = DateUtil.getMonth2(month2);

        String descPeriod = bulan + " " + getYearFrom();
        String descPeriod2 = bulan2 + " " + getYearFrom();

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("rekening");
            row0.createCell(1).setCellValue("uraian");
            row0.createCell(2).setCellValue("saldo debit per " + descPeriod2);
            row0.createCell(3).setCellValue("saldo credit per " + descPeriod2);
            row0.createCell(4).setCellValue("debit " + descPeriod);
            row0.createCell(5).setCellValue("credit " + descPeriod);
            row0.createCell(6).setCellValue("saldo debit per " + descPeriod);
            row0.createCell(7).setCellValue("saldo credit per " + descPeriod);

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(1).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("debit_prev").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("credit_prev").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("debit_next").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("credit_next").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

  public String getStCreateWho()
  {
    return this.stCreateWho;
  }

  public void setStCreateWho(String stCreateWho)
  {
    this.stCreateWho = stCreateWho;
  }

  public String getAkunFrom()
  {
    return this.akunFrom;
  }

  public void setAkunFrom(String akunFrom)
  {
    this.akunFrom = akunFrom;
  }

  public String getAkunTo()
  {
    return this.akunTo;
  }

  public void setAkunTo(String akunTo)
  {
    this.akunTo = akunTo;
  }

  public Date getRealisasifrom()
  {
    return this.realisasifrom;
  }

  public void setRealisasifrom(Date realisasifrom)
  {
    this.realisasifrom = realisasifrom;
  }

  public Date getRealisasito()
  {
    return this.realisasito;
  }

  public void setRealisasito(Date realisasito)
  {
    this.realisasito = realisasito;
  }

  public DTOList CLAIM()
    throws Exception
  {
    long lPeriodFrom = Long.parseLong("0");
    if (getPeriodFrom().equalsIgnoreCase("1")) {
      lPeriodFrom = lPeriodFrom;
    } else {
      lPeriodFrom = getLPeriodFrom();
    }
    String fld = "bal";
    String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("substr(b.accountno,6,5) AS gl_code,substr(b.accountno,1,3) as akun,substr(b.accountno,11,2) as jenas, round(coalesce(sum(" + plist + "),0),0) as balance");

    sqa.addQuery(" FROM GL_ACCT_BAL2 A  LEFT JOIN GL_ACCOUNTS B ON B.ACCOUNT_ID = A.ACCOUNT_ID ");

    sqa.addClause("b.acctype is null");

    sqa.addClause(" b.accountno like '72%' ");
    if (this.yearFrom != null)
    {
      sqa.addClause("(a.period_year between ? and ?)");
      sqa.addPar(this.yearFrom);
      sqa.addPar(this.yearFrom);
    }
    if (this.branch != null)
    {
      sqa.addClause("substr(b.accountno,14,2) = ?");
      sqa.addPar(this.branch);
    }
    sqa.addClause("a.idr_flag = 'Y'");

    String sql = " select a.trx_no,a.jenas as ref1,a.gl_code,a.klaim_fact,a.klaim_xl,a.klaim_closs,a.debit from ( select b.ent_name,b.shortname2 as trx_no,b.gl_code,b.reas_ent_id,b.ent_id,a.jenas,sum(getpremi2(akun in ('144','423'),balance)) as premi_treaty,sum(getpremi2(akun in ('143','424'),balance)) as premi_fact,sum(getpremi2(akun in ('145','425'),balance)) as premi_xl,sum(getpremi2(akun in ('146','426','724'),balance)) as klaim_fact,sum(getpremi2(akun in ('147','427','726'),balance)) as klaim_xl,sum(getpremi2(akun in ('148','428','725'),balance)) as klaim_closs,sum(getpremi2(akun in ('144','143','145','146','147','148','423','424','425','426','427','428','724','725','726'),balance)) as debit  from ( " + sqa.getSQL() + " group by SUBSTR(b.accountno,6,5),substr(b.accountno,1,3),substr(b.accountno,11,2) " + " ) a inner join ent_master b on b.gl_code = a.gl_code and b.active_flag = 'Y' " + " group by b.ent_name,b.shortname2,b.gl_code,b.reas_ent_id,b.ent_id,a.jenas order by b.reas_ent_id,b.gl_code " + " ) a where a.debit <> 0 group by a.trx_no,a.jenas,a.gl_code,a.klaim_fact,a.klaim_xl,a.klaim_closs,a.debit order by a.gl_code,a.jenas ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), JournalView.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public String getMonthTitleDescription2(String months)
    throws Exception
  {
    PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(months, this.yearFrom);

    Date d1 = pd1.getDtEndDate();

    return DateUtil.getDateStr(d1, "^^ yyyy");
  }

  public String getYearPosting()
    throws Exception
  {
    SQLUtil S = new SQLUtil();
    try
    {
      S.setQuery(" select years from gl_posting where final_flag = 'Y' order by gl_post_id desc limit 1 ");

      ResultSet RS = S.executeQuery();
      String str;
      if (RS.next()) {
        return RS.getString(1);
      }
      return null;
    }
    finally
    {
      S.release();
    }
  }

  public void EXCEL_RINCIAN_UW()
    throws Exception
  {
    GLReportEngine2 glr = new GLReportEngine2();

    long lPeriodFrom = getLPeriodFrom();
    long lPeriodTo = getLPeriodTo();
    long lYearFrom = getLYearFrom();
    long lYearTo = getLYearFrom();

    BigDecimal premi_lgsg = BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "613", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis));
    BigDecimal premi_tdklgsg = BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "614", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis));
    BigDecimal komisi_lgsg = BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7711", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis));
    BigDecimal komisi_tdklgsg = BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7712", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis));

    int month1 = Integer.parseInt(getPeriodTo());
    String bulan = DateUtil.getMonth2(month1);
    String descPeriod = bulan + " " + getYearFrom();
    String jenpol = null;

    String sumbisKet = null;
    if (this.sumbis != null) {
      sumbisKet = this.sumbisDesc;
    } else {
      sumbisKet = "KESELURUHAN";
    }
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet1 = wb.createSheet(sumbisKet.toUpperCase());

    XSSFRow row1_0 = sheet1.createRow(0);
    row1_0.createCell(0).setCellValue("HASIL UNDERWRITING PER JENIS PRODUK");

    XSSFRow row1_1 = sheet1.createRow(1);
    row1_1.createCell(0).setCellValue("PER " + descPeriod);

    XSSFRow row1_2 = sheet1.createRow(2);
    row1_2.createCell(0).setCellValue("NO");
    row1_2.createCell(1).setCellValue("URAIAN");
    row1_2.createCell(2).setCellValue("KONVENSIONAL");
    row1_2.createCell(3).setCellValue("Non COB");

    DTOList poltype = getPolType();
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      row1_2.createCell(i + 4).setCellValue(LanguageManager.getInstance().translate(jen.getStDescription()));
    }
    XSSFRow row1_3 = sheet1.createRow(3);
    row1_3.createCell(0).setCellValue("1");
    row1_3.createCell(1).setCellValue("Pendapatan Premi Bruto");

    XSSFRow row1_4 = sheet1.createRow(4);
    row1_4.createCell(0).setCellValue("2");
    row1_4.createCell(1).setCellValue("Premi Bruto");

    XSSFRow row1_5 = sheet1.createRow(5);

    row1_5.createCell(1).setCellValue("a. Premi Penutupan Langsung");
    row1_5.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "613", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_5.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "613", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_5.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "613", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_6 = sheet1.createRow(6);

    row1_6.createCell(1).setCellValue("b. Premi Penutupan Tidak Langsung");
    row1_6.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "614", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_6.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "614", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_6.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "614", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_7 = sheet1.createRow(7);

    row1_7.createCell(1).setCellValue("c. Komisi Dibayar Langsung");
    row1_7.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7711", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_7.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7711", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_7.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7711", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_8 = sheet1.createRow(8);

    row1_8.createCell(1).setCellValue("d. Komisi Dibayar Tidak Langsung");
    row1_8.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7712", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_8.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7712", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_8.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "7712", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_9 = sheet1.createRow(9);
    row1_9.createCell(0).setCellValue("3");
    row1_9.createCell(1).setCellValue("Premi Reasuransi");

    XSSFRow row1_10 = sheet1.createRow(10);

    row1_10.createCell(1).setCellValue("a. Premi Reasuransi Dibayar");
    row1_10.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "63", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_10.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "63", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_10.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "63", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_11 = sheet1.createRow(11);

    row1_11.createCell(1).setCellValue("b. Komisi Reasuransi Diterima");
    row1_11.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "772", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_11.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "772", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_11.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "772", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_12 = sheet1.createRow(12);
    row1_12.createCell(0).setCellValue("4");
    row1_12.createCell(1).setCellValue("Penurunan (Kenaikan) PYBMP*)");

    XSSFRow row1_13 = sheet1.createRow(13);

    row1_13.createCell(1).setCellValue("a. PYBMP tahun lalu");
    row1_13.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "645", "647", null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_13.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "645", "647", "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_13.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "645", "647", jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_14 = sheet1.createRow(14);

    row1_14.createCell(1).setCellValue("b. PYBMP tahun berjalan");
    row1_14.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "644", "646", null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_14.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "644", "646", "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_14.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "644", "646", jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_15 = sheet1.createRow(15);
    row1_15.createCell(0).setCellValue("5");
    row1_15.createCell(1).setCellValue("Beban Klaim");

    XSSFRow row1_16 = sheet1.createRow(16);

    row1_16.createCell(1).setCellValue("a. Klaim Bruto Langsung");
    row1_16.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "717", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_16.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "717", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_16.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "717", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_17 = sheet1.createRow(17);

    row1_17.createCell(1).setCellValue("b. Klaim Bruto Tdk Langsung");
    row1_17.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "718", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_17.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "718", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_17.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "718", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_18 = sheet1.createRow(18);

    row1_18.createCell(1).setCellValue("c. Klaim Reasuransi");
    row1_18.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "72", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_18.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "72", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_18.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "72", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_19 = sheet1.createRow(19);

    row1_19.createCell(1).setCellValue("d. Kenaikan (Penurunan) EKRS**)");

    XSSFRow row1_20 = sheet1.createRow(20);

    row1_20.createCell(1).setCellValue("d.1. EKRS tahun berjalan");
    row1_20.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "751", "753", null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_20.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "751", "753", "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_20.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "751", "753", jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_21 = sheet1.createRow(21);

    row1_20.createCell(1).setCellValue("d.2. EKRS tahun lalu");
    row1_21.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "752", "754", null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_21.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "752", "754", "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_21.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "752", "754", jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    XSSFRow row1_22 = sheet1.createRow(22);

    row1_22.createCell(1).setCellValue("Beban Underwriting Lain Neto");
    row1_22.createCell(2).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "79", null, null, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    row1_22.createCell(3).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "79", null, "00", lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    for (int i = 0; i < poltype.size(); i++)
    {
      InsurancePolicyTypeView jen = (InsurancePolicyTypeView)poltype.get(i);

      int l = jen.getStPolicyTypeID().length();
      if (l == 1) {
        jenpol = "0" + jen.getStPolicyTypeID();
      } else {
        jenpol = jen.getStPolicyTypeID();
      }
      row1_22.createCell(i + 4).setCellValue(BDUtil.roundUp(glr.getSummaryPolTypeHasilUW("ADD=0", "79", null, jenpol, lPeriodFrom, lPeriodTo, lYearFrom, this.sumbis)).doubleValue());
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename= hasiluw_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public DTOList getPolType()
  {
    try
    {
      return ListUtil.getDTOListFromQuery("select * from ins_policy_types order by pol_type_id", InsurancePolicyTypeView.class);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public String getSumbis()
  {
    return this.sumbis;
  }

  public void setSumbis(String sumbis)
  {
    this.sumbis = sumbis;
  }

  public String getSumbisDesc()
  {
    return this.sumbisDesc;
  }

  public void setSumbisDesc(String sumbisDesc)
  {
    this.sumbisDesc = sumbisDesc;
  }

  public void clickPrintValidate()
    throws Exception
  {
    loadFormList();

    DTOList l = loadList();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    String filename = this.stReport + "-" + this.periodToName + this.yearFrom;
    SessionManager.getInstance().getRequest().setAttribute("SAVE_TO_FILE", "Y");
    SessionManager.getInstance().getRequest().setAttribute("FILE_NAME", filename);
    SessionManager.getInstance().getRequest().setAttribute("REPORT_PROD", "Y");

    ArrayList plist = new ArrayList();

    plist.add(this.stReport);

    String urxSave = null;
    for (int i = 0; i < plist.size(); i++)
    {
      String s = (String)plist.get(i);
      if (formList.contains(s + ".fop.jsp"))
      {
        urxSave = "/pages/gl/report/" + s + "_SAVE.fop";
        break;
      }
    }
    if (urxSave == null) {
      throw new RuntimeException("Unable to find suitable print form");
    }
    FOPServlet srv = new FOPServlet();
    srv.saveFOP(SessionManager.getInstance().getRequest(), SessionManager.getInstance().getResponse(), urxSave);

    String filePath = "D:/fin-repository/report_temp/edocument/" + filename + "_temp.pdf";
    String uploadPath = "fi-" + filename + ".pdf";
    copyWithFTP(filePath, uploadPath);

    super.redirect(urxSave);
  }

  public String getPeriodFromName()
  {
    return this.periodFromName;
  }

  public void setPeriodFromName(String periodFromName)
  {
    this.periodFromName = periodFromName;
  }

  public String getPeriodToName()
  {
    return this.periodToName;
  }

  public void setPeriodToName(String periodToName)
  {
    this.periodToName = periodToName;
  }

  public void copyWithFTP(String filePath, String uploadPath)
  {
    String host = "192.168.200.19";
    String user = "dinal";
    String pass = "askrida00";
    int port = 21;

    FTPClient ftpClient = new FTPClient();
    try
    {
      ftpClient.connect(host, port);
      ftpClient.login(user, pass);
      ftpClient.enterLocalPassiveMode();

      ftpClient.setFileType(2);

      File firstLocalFile = new File(filePath);

      String firstRemoteFile = uploadPath;
      InputStream inputStream = new FileInputStream(firstLocalFile);

      System.out.println("Start uploading first file");
      boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
      inputStream.close();
      if (done) {
        System.out.println("The first file is uploaded successfully.");
      }
      return;
    }
    catch (IOException ex)
    {
      System.out.println("Error: " + ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      try
      {
        if (ftpClient.isConnected())
        {
          ftpClient.logout();
          ftpClient.disconnect();
        }
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
  }

  public String getAccountCodeLike()
  {
    return this.accountCodeLike;
  }

  public void setAccountCodeLike(String accountCodeLike)
  {
    this.accountCodeLike = accountCodeLike;
  }

  public void resetField()
  {
    setPolicyNo(null);
    setAccountCode(null);
    setAccountCodeLike(null);
    setDescription(null);
    setMethod(null);
    setStPolicyTypeID(null);
    setTransNumber(null);
    setTransNumberLike(null);
    setStRecapitulationNo(null);
    setTransdatefrom(null);
    setTransdateto(null);
  }

  public DTOList EXCEL_LAJURNEW() throws Exception {
        final boolean LAJUR_BANK = "Y".equalsIgnoreCase((String) refPropMap.get("LAJUR_BANK"));

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String db = "db";
        String cr = "cr";
        String fld = "bal";
        String dbplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, db, "+");
        String crplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, cr, "+");
        String dbplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), db, "+");
        String crplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), cr, "+");
        String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "';'||b.noper as noper,b.account_id,b.accountno,b.description,a.period_no,"
                + "sum(a.debit) as debit,sum(a.credit) as credit,"
                + "a.owner_code as pemilik,a.user_code as pengguna, "
                + "(case when substr(a.pol_no,2,1) = '1' then 'UMUM' "
                + "when substr(a.pol_no,2,1) = '2' then 'PEMDA' "
                + "when substr(a.pol_no,2,1) = '3' then 'PERUSDA' "
                + "when substr(a.pol_no,2,1) = '4' then 'BPD' else null end ) as sumbis ");

        sqa.addQuery(" from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause("b.acctype is null");

        if (LAJUR_BANK) {
            sqa.addClause("substr(b.accountno,1,5) in ('12210','12220','12221','11121','11122','48921','48922')");
        }

        if (yearFrom != null) {
//            sqa.addClause("a.fiscal_year = '" + yearFrom + "'");
            sqa.addClause("a.fiscal_year = ?");
            sqa.addPar(yearFrom);
        }

        if (periodFrom != null) {
//            sqa.addClause("a.period_no between " + getLPeriodFrom() + " and " + getLPeriodTo());
            sqa.addClause("a.period_no between ? and ?");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (branch != null) {
//            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        if (!LAJUR_BANK) {
            /*PILIH AKUN*/
//            if (akunTo != null) {
////                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunTo + "')");
//                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between ? and ? )");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunTo);
//            }
//
//            if (akunTo == null) {
////                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunFrom + "')");
//                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between ? and ? )");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunFrom);
//            }

            sqa.addClause("((substr(b.accountno,1,1)) between '6' and '9' )");
        }

        int period = Integer.parseInt(periodTo) - 1;

        String sql = "select noper,account_id,accountno,description,pemilik,pengguna,sumbis,"
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + period + ",debit)) as debit_prev,"
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + period + ",credit)) as credit_prev,"
                + "sum(getpremi2(period_no = " + getLPeriodTo() + ",debit)) as debit,"
                + "sum(getpremi2(period_no = " + getLPeriodTo() + ",credit)) as credit, "
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + getLPeriodTo() + ",debit)) as debit_next,"
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + getLPeriodTo() + ",credit)) as credit_next "
                + "from ( " + sqa.getSQL() + " "
                + "group by b.noper,b.account_id,b.accountno,b.description,a.period_no,a.owner_code,a.user_code,substr(a.pol_no,2,1) "
                + "order by b.accountno asc "
                + " ) a group by noper,account_id,accountno,description,pemilik,pengguna,sumbis "
                + "order by a.accountno asc ";

//        SQLUtil S = new SQLUtil();

//        String nama_file = "neracalajur_" + System.currentTimeMillis() + ".csv";
//
//        sql = "Copy ("
//                + sql
//                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";
//
//        final PreparedStatement ps = S.setQuery(sql);
//
//        boolean tes = ps.execute();
//
//        S.release();
//
//        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
//        int length = 0;
//        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();
//
//        SessionManager.getInstance().getResponse().setContentType("text/csv");
//        SessionManager.getInstance().getResponse().setContentLength((int) file.length());
//
//        // sets HTTP header
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");
//
//        int BUFSIZE = 4096;
//        byte[] byteBuffer = new byte[BUFSIZE];
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//
//        // reads the file's bytes and writes them to the response stream
//        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
//            outStream.write(byteBuffer, 0, length);
//        }
//
//        in.close();
//        outStream.close();
//
//        file.delete();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_LAJURNEW() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        int month1 = Integer.parseInt(getPeriodTo());

        int month2;
        if (Integer.parseInt(getPeriodTo()) == 1) {
            month2 = 1;
        } else {
            month2 = Integer.parseInt(getPeriodTo()) - 1;
        }

//        String bulan = getMonthTitleDescription2(Integer.toString(month1));
//        String bulan2 = getMonthTitleDescription2(Integer.toString(month2));

        String bulan = DateUtil.getMonth2(month1);
        String bulan2 = DateUtil.getMonth2(month2);

        String descPeriod = bulan + " " + getYearFrom();
        String descPeriod2 = bulan2 + " " + getYearFrom();

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("rekening");
            row0.createCell(1).setCellValue("uraian");
            row0.createCell(2).setCellValue("saldo debit per " + descPeriod2);
            row0.createCell(3).setCellValue("saldo credit per " + descPeriod2);
            row0.createCell(4).setCellValue("debit " + descPeriod);
            row0.createCell(5).setCellValue("credit " + descPeriod);
            row0.createCell(6).setCellValue("saldo debit per " + descPeriod);
            row0.createCell(7).setCellValue("saldo credit per " + descPeriod);
            row0.createCell(8).setCellValue("pemilik");
            row0.createCell(9).setCellValue("pengguna");
            row0.createCell(10).setCellValue("sumbis");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(1).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("description")));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("debit_prev").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("credit_prev").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("debit_next").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("credit_next").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("pemilik"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pengguna"));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("sumbis"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=" + getStFileName() + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

    }

    public DTOList EXCEL_MUTATION_FILE() throws Exception {

        String gljedetail = null;

        if (transdatefrom != null) {
            String gljedetailYear = DateUtil.getYear(transdatefrom);
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                    || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + gljedetailYear;
            }
        } else {
            gljedetail = "gl_je_detail";
        }

        final SQLAssembler sqa = new SQLAssembler();

        String methodChoiceSelect = null;
        if (method != null) {
            if (method.equalsIgnoreCase("N")) {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,c.refz1 as treaty,owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
            } else if (method.equalsIgnoreCase("Q")) {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,coalesce(c.refid0,d.refid0) as treaty,owner_code as pemilik,"
                        + "user_code as pengguna,quote_ident(recap_no) as recap_no ";
            } else {
                methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                        + "a.debit,a.credit,';'||a.pol_no as pol_no,getsumbis(substr(a.pol_no,2,1)) as sumbis,"
                        + "owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
            }
        } else {
            methodChoiceSelect = " row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
                    + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
                    + "a.debit,a.credit,';'||a.pol_no as pol_no,getsumbis(substr(a.pol_no,2,1)) as sumbis,"
                    + "owner_code as pemilik,user_code as pengguna,quote_ident(recap_no) as recap_no ";
        }

        sqa.addSelect(methodChoiceSelect);

        /*sqa.addSelect(" row_number() over(order by a.trx_id) as no,a.applydate as tanggal,"
        + "a.trx_no as nobuk,b.accountno as norek,a.description as keterangan,"
        + "a.debit,a.credit,c.refz1 as treaty ");*/

        String methodChoiceQuery = null;
        if (method != null) {
            if (method.equalsIgnoreCase("N")) {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid "
                        + " left join ar_invoice c on c.ar_invoice_id = a.ref_trx_no::bigint ";
            } else if (method.equalsIgnoreCase("Q")) {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid "
                        + " left join ins_pol_inward c on c.ar_invoice_id = a.ref_trx_no::bigint "
                        + " inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id and d.ar_trx_line_id in (6,74,75,81,108,126,128) ";
            } else {
                methodChoiceQuery = "   from " + gljedetail + " a "
                        + " inner join gl_accounts b on b.account_id = a.accountid ";
            }
        } else {
            methodChoiceQuery = "   from " + gljedetail + " a "
                    + " inner join gl_accounts b on b.account_id = a.accountid ";
        }

        sqa.addQuery(methodChoiceQuery);

        /*sqa.addQuery("   from " + gljedetail + " a"
        + " inner join gl_accounts b on b.account_id = a.accountid "
        + " left join ar_invoice c on c.ar_invoice_id = a.ref_trx_no::bigint ");*/

        if (stRecapitulationNo != null) {
            sqa.addClause("a.recap_no = '" + stRecapitulationNo.toUpperCase() + "' ");
//            sqa.addClause("a.recap_no = ? ");
//            sqa.addPar(stRecapitulationNo.toUpperCase());
        }

        if (policyNo != null) {
            sqa.addClause("a.pol_no like '" + policyNo.toUpperCase() + "'");
//            sqa.addClause("a.pol_no = ? ");
//            sqa.addPar(policyNo);
        }

        if (stAccountID != null) {
            sqa.addClause("a.accountid = " + stAccountID);
//            sqa.addClause("a.accountid = ?");
//            sqa.addPar(stAccountID);
        }

        if (accountCodeLike != null) {
            sqa.addClause("b.accountno like '" + accountCodeLike + "%'");
//            int l = accountCodeLike.length();
//            sqa.addClause("b.accountno like ? ");
//            sqa.addPar(accountCodeLike + '%');
        }

        if (accountCode != null) {
            sqa.addClause("b.accountno = '" + accountCode + "'");
//            sqa.addClause("b.accountno = ? ");
//            sqa.addPar(accountCode);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
//            sqa.addClause("substr(b.accountno,14,2) = ?");
//            sqa.addPar(branch);
        }

        if (transNumber != null) {
            sqa.addClause("a.trx_no = '" + transNumber.toUpperCase() + "'");
//            sqa.addClause("a.trx_no = ?");
//            sqa.addPar(transNumber.toUpperCase());
        }

        if (transNumberLike != null) {
            sqa.addClause("a.trx_no like '" + transNumberLike.toUpperCase() + "%'");
//            sqa.addClause("a.trx_no like ? ");
//            sqa.addPar(transNumberLike.toUpperCase() + "%");
        }

        if (transdatefrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= '" + transdatefrom + "'");
//            sqa.addClause("date_trunc('day',a.applydate) >= ?");
//            sqa.addPar(transdatefrom);
        }

        if (transdateto != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= '" + transdateto + "'");
//            sqa.addClause("date_trunc('day',a.applydate) <= ?");
//            sqa.addPar(transdateto);
        }

        if (stPolicyTypeID != null) {
            String poltypeFilter = stPolicyTypeID.length() < 2 ? "0" + stPolicyTypeID : stPolicyTypeID;
            sqa.addClause("substr(pol_no,3,2) = '" + poltypeFilter + "'");
        }

        if (method != null) {
            String inv = "";
            if (method.equalsIgnoreCase("G,H,I")) {
                inv = "('G','H','I')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            } else if (method.equalsIgnoreCase("K,L,N")) {
                inv = "('K','L','N')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            } else if (method.equalsIgnoreCase("Q")) {
                inv = "('Q')";
            } else {
                inv = "('" + method + "')";
                sqa.addClause("substr(a.trx_no,1,1) in " + inv);
            }
            //sqa.addPar(inv);
        }

        if (transdate != null) {
            sqa.addClause("date_trunc('day',a.applydate) = '" + transdate + "'");
//            sqa.addClause("date_trunc('day',a.applydate) = ?");
//            sqa.addPar(transdate);
        }

        if (description != null) {
            sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english','" + description.toUpperCase().replaceAll(" ", "&") + "')");
//            sqa.addClause("to_tsvector('english', a.description) @@ to_tsquery('english',?)");
//            sqa.addPar(description.toUpperCase().replaceAll(" ", "&"));
        }

        if (stPayment != null) {
            if (Tools.isYes(stPayment)) {
                sqa.addClause("a.ref_trx_type = 'RCP'");
            }
        }

        String sql = "select * from ( " + sqa.getSQL() + " order by a.trx_id asc ) a ";

        if (recordfrom != null && recordto != null) {
            sql = sql + " where no between " + recordfrom + " and " + recordto;
        }

        if (recordfrom != null && recordto == null) {
            sql = sql + " where no <= " + recordfrom;
        }

        if (recordfrom == null && recordto != null) {
            sql = sql + " where no > " + recordto;
        }

        sql = sql + " order by no ";

        SQLUtil S = new SQLUtil();

        String nama_file = "jurnal_mutasi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();

//        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
//        int length = 0;
//        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();
//
//        SessionManager.getInstance().getResponse().setContentType("text/csv");
//        SessionManager.getInstance().getResponse().setContentLength((int) file.length());
//
//        // sets HTTP header
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");
//
//        int BUFSIZE = 4096;
//        byte[] byteBuffer = new byte[BUFSIZE];
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//
//        // reads the file's bytes and writes them to the response stream
//        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
//            outStream.write(byteBuffer, 0, length);
//        }
//
//        in.close();
//        outStream.close();
//
//        file.delete();
    }

    private String recordfrom;
    private String recordto;
    private String checkrecord;

    /**
     * @return the recordfrom
     */
    public String getRecordfrom() {
        return recordfrom;
    }

    /**
     * @param recordfrom the recordfrom to set
     */
    public void setRecordfrom(String recordfrom) {
        this.recordfrom = recordfrom;
    }

    /**
     * @return the recordto
     */
    public String getRecordto() {
        return recordto;
    }

    /**
     * @param recordto the recordto to set
     */
    public void setRecordto(String recordto) {
        this.recordto = recordto;
    }

    /**
     * @return the checkrecord
     */
    public String getCheckrecord() {
        return checkrecord;
    }

    /**
     * @param checkrecord the checkrecord to set
     */
    public void setCheckrecord(String checkrecord) {
        this.checkrecord = checkrecord;
    }

    public void clickPrintExcelFile() throws Exception {

        final DTOList l = loadListExcelFile();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
    }

    private DTOList loadListExcelFile() throws Exception {
        ref1 = LOVManager.getInstance().getRef1("PROD_JOURNAL", stReport);

        refPropMap = Tools.getPropMap(ref1);

        final String queryID = (String) refPropMap.get("QUERY_EXCELFILE");


        status = (String) refPropMap.get("STATUS");

        if (queryID == null) {
            return null;
        } else {
            final Method m = this.getClass().getMethod(queryID, null);

            return (DTOList) m.invoke(this, null);
        }
    }

    public DTOList EXCEL_LAJUR_FILE() throws Exception {
        final boolean LAJUR_BANK = "Y".equalsIgnoreCase((String) refPropMap.get("LAJUR_BANK"));

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String db = "db";
        String cr = "cr";
        String fld = "bal";
        String dbplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, db, "+");
        String crplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, cr, "+");
        String dbplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), db, "+");
        String crplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), cr, "+");
        String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "';'||b.noper as noper,b.account_id,b.accountno,b.description,"
                + "sum(" + dbplist2 + ") as debit_prev,"
                + "sum(" + crplist2 + ") as credit_prev,"
                + "sum(coalesce(db" + periodTo + ",0)) as debit,"
                + "sum(coalesce(cr" + periodTo + ",0)) as credit,"
                + "sum(" + plist + ") as balance,"
                + "sum(" + dbplist + ") as debit_next,"
                + "sum(" + crplist + ") as credit_next");

        sqa.addQuery(" from gl_acct_bal2 a "
                + " inner join gl_accounts b on b.account_id = a.account_id ");

        sqa.addClause("b.acctype is null");
        sqa.addClause("a.idr_flag = 'Y'");

        if (LAJUR_BANK) {
            sqa.addClause("substr(b.accountno,1,5) in ('12210','12220','12221','11121','11122','48921','48922')");
        }

        if (yearFrom != null) {
            sqa.addClause("a.period_year = '" + yearFrom + "' ");
//            sqa.addPar(yearFrom);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
//            sqa.addPar(branch);
        }

        if (!LAJUR_BANK) {
            if (akunTo != null) {
                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunTo + "')");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunTo);
            }

            if (akunTo == null) {
                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunFrom + "')");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunFrom);
            }
        }

        SQLUtil S = new SQLUtil();

        String nama_file = "neraca_lajur_" + System.currentTimeMillis() + ".csv";

        int month1 = Integer.parseInt(getPeriodTo());

        int month2;
        if (Integer.parseInt(getPeriodTo()) == 1) {
            month2 = 1;
        } else {
            month2 = Integer.parseInt(getPeriodTo()) - 1;
        }

        String bulan = DateUtil.getMonth2(month1);
        String bulan2 = DateUtil.getMonth2(month2);

        String descPeriod = bulan + "_" + getYearFrom();
        String descPeriod2 = bulan2 + "_" + getYearFrom();

        String sql = "select accountno as rekening,description as uraian,debit_prev as debit_per_" + descPeriod2 + ","
                + "credit_prev as credit_per_" + descPeriod2 + ","
                + "debit as debit_" + descPeriod + ","
                + "credit as credit_" + descPeriod + ","
                + "debit_next as debit_per_" + descPeriod + ","
                + "credit_next as credit_per_" + descPeriod + " "
                + "from ( " + sqa.getSQL() + " group by b.noper,b.account_id,b.accountno,b.description order by b.accountno asc "
                + " ) a where debit_prev <> 0 or credit_prev <> 0 or debit <> 0 or credit <> 0 or debit_next <> 0 or credit_next <> 0 order by a.accountno asc ";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();

    }

    public DTOList EXCEL_LAJURNEW_FILE() throws Exception {
        final boolean LAJUR_BANK = "Y".equalsIgnoreCase((String) refPropMap.get("LAJUR_BANK"));

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String db = "db";
        String cr = "cr";
        String fld = "bal";
        String dbplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, db, "+");
        String crplist2 = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, cr, "+");
        String dbplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), db, "+");
        String crplist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), cr, "+");
        String plist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                "';'||b.noper as noper,b.account_id,b.accountno,b.description,a.period_no,"
                + "sum(a.debit) as debit,sum(a.credit) as credit,"
                + "';'||a.owner_code as pemilik,';'||a.user_code as pengguna, "
                + "(case when substr(a.pol_no,2,1) = '1' then 'UMUM' "
                + "when substr(a.pol_no,2,1) = '2' then 'PEMDA' "
                + "when substr(a.pol_no,2,1) = '3' then 'PERUSDA' "
                + "when substr(a.pol_no,2,1) = '4' then 'BPD' else null end ) as sumbis ");

        sqa.addQuery(" from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause("b.acctype is null");

        if (LAJUR_BANK) {
            sqa.addClause("substr(b.accountno,1,5) in ('12210','12220','12221','11121','11122','48921','48922')");
        }

        if (yearFrom != null) {
            sqa.addClause("a.fiscal_year = '" + yearFrom + "'");
//            sqa.addClause("a.fiscal_year = ?");
//            sqa.addPar(yearFrom);
        }

        if (periodFrom != null) {
            sqa.addClause("a.period_no between " + periodFrom + " and " + periodTo);
//            sqa.addClause("a.period_no between ? and ?");
//            sqa.addPar(periodFrom);
//            sqa.addPar(periodTo);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
//            sqa.addClause("substr(b.accountno,14,2) = ?");
//            sqa.addPar(branch);
        }

        if (!LAJUR_BANK) {
            /*PILIH AKUN*/
//            if (akunTo != null) {
////                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunTo + "')");
//                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between ? and ? )");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunTo);
//            }
//
//            if (akunTo == null) {
////                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between '" + akunFrom + "' and '" + akunFrom + "')");
//                sqa.addClause("((substr(b.accountno,1," + akunFrom.length() + ")) between ? and ? )");
//                sqa.addPar(akunFrom);
//                sqa.addPar(akunFrom);
//            }

            sqa.addClause("((substr(b.accountno,1,1)) between '6' and '9' )");
        }

        int period = Integer.parseInt(periodTo) - 1;

        int month1 = Integer.parseInt(getPeriodTo());

        int month2;
        if (Integer.parseInt(getPeriodTo()) == 1) {
            month2 = 1;
        } else {
            month2 = Integer.parseInt(getPeriodTo()) - 1;
        }

        String bulan = DateUtil.getMonth2(month1);
        String bulan2 = DateUtil.getMonth2(month2);

        String descPeriod = bulan + "_" + getYearFrom();
        String descPeriod2 = bulan2 + "_" + getYearFrom();

        String sql = "select accountno as rekening,description as uraian,pemilik,pengguna,sumbis,"
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + period + ",debit)) as debit_per_" + descPeriod2 + ","
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + period + ",credit)) as credit_per_" + descPeriod2 + ","
                + "sum(getpremi2(period_no = " + getLPeriodTo() + ",debit)) as debit_" + descPeriod + ","
                + "sum(getpremi2(period_no = " + getLPeriodTo() + ",credit)) as credit_" + descPeriod + ", "
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + getLPeriodTo() + ",debit)) as debit_per_" + descPeriod + ","
                + "sum(getpremi2(period_no between " + getLPeriodFrom() + " and " + getLPeriodTo() + ",credit)) as credit_per_" + descPeriod
                + " from ( " + sqa.getSQL() + " "
                + " group by b.noper,b.account_id,b.accountno,b.description,a.period_no,a.owner_code,a.user_code,substr(a.pol_no,2,1) "
                + " order by b.accountno asc "
                + " ) a group by noper,account_id,accountno,description,pemilik,pengguna,sumbis "
                + " order by a.accountno asc ";

        SQLUtil S = new SQLUtil();

        String nama_file = "neraca_lajur_sebagian_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        return new DTOList();
    }

    public DTOList getTitipanPremiReinsuranceList() throws Exception
  {
    if (this.titipanPremiReinsuranceList == null)
    {
      this.titipanPremiReinsuranceList = new DTOList();
      this.titipanPremiReinsuranceList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQATitipanPremiReinsurance();

    sqa.addFilter(this.titipanPremiReinsuranceList.getFilter());

    this.titipanPremiReinsuranceList = sqa.getList(TitipanPremiReinsuranceView.class);

    return this.titipanPremiReinsuranceList;
  }

  public void setTitipanPremiReinsuranceList(DTOList titipanPremiReinsuranceList)
  {
    this.titipanPremiReinsuranceList = titipanPremiReinsuranceList;
  }

  public SQLAssembler getSQATitipanPremiReinsurance()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, c.user_name,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi_reinsurance a  inner join gl_accounts b on b.account_id = a.accountid  left join s_users c on c.user_id = a.create_who");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    sqa.addOrder("a.trx_id desc");
    sqa.setLimit(300);

    return sqa;
  }

  public void btnClickPrintReinsurance()throws Exception
  {
    super.redirect("/pages/gl/report/rpt_realisasititipan_reinsurance.fop");
  }

  public void printRealisasiTitipanReinsuranceExcel() throws Exception
  {
    DTOList l = EXCEL_REALISASI_TITIPAN_REINSURANCE();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_REALISASI_TITIPAN_REINSURANCE();
  }

  public DTOList EXCEL_REALISASI_TITIPAN_REINSURANCE()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*,b.receipt_date,b.receipt_no as nobuk_realisasi,b.titipan_premi_amount as realisasi,b.titipan_premi_used_amount as realisasi_used, (b.titipan_premi_amount-b.titipan_premi_used_amount) as sisa,c.posted_flag, (select count(x.titipan_premi_id) from ar_receipt_lines x where x.titipan_premi_id = a.trx_id) as jumlah ");

    sqa.addQuery(" from ar_titipan_premi_reinsurance a  left join ar_receipt_lines b on b.titipan_premi_id = a.trx_id  left join ar_receipt c on c.ar_receipt_id = b.receipt_id ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.realisasifrom != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) >= ? ");
      sqa.addPar(this.realisasifrom);
    }
    if (getRealisasito() != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(getRealisasito());
    }
    if (this.perdate != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(this.perdate);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql2 = sqa.getSQL() + " order by a.trx_no,a.counter,c.posted_flag desc,b.ar_rcl_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql2, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_REALISASI_TITIPAN_REINSURANCE()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    BigDecimal a1 = new BigDecimal(0);
    BigDecimal b1 = new BigDecimal(0);
    BigDecimal c1 = new BigDecimal(0);
    String status = null;
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row1 = sheet.createRow(0);
      row1.createCell(0).setCellValue("REALISASI TITIPAN PREMI");

      XSSFRow row0 = sheet.createRow(2);
      row0.createCell(0).setCellValue("trxid");
      row0.createCell(1).setCellValue("nobuk titipan");
      row0.createCell(2).setCellValue("ktr");
      row0.createCell(3).setCellValue("noper");
      row0.createCell(4).setCellValue("naper");
      row0.createCell(5).setCellValue("nopol");
      row0.createCell(6).setCellValue("nilai");
      row0.createCell(7).setCellValue("tgltitip");
      row0.createCell(8).setCellValue("nobuk realisasi");
      row0.createCell(9).setCellValue("terpakai");
      row0.createCell(10).setCellValue("sisa");
      row0.createCell(11).setCellValue("tglrealisasi");
      row0.createCell(12).setCellValue("keterangan");
      row0.createCell(13).setCellValue("efektif");
      if (i == 0)
      {
        a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
      }
      else if (i > 0)
      {
        HashDTO s = (HashDTO)list.get(i - 1);
        String x1 = s.getFieldValueByFieldNameBD("trx_id").toString();
        String x2 = h.getFieldValueByFieldNameBD("trx_id").toString();
        if (x1.equalsIgnoreCase(x2))
        {
          b1 = BDUtil.add(b1, s.getFieldValueByFieldNameBD("realisasi_used"));
          c1 = BDUtil.add(b1, h.getFieldValueByFieldNameBD("realisasi_used"));
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), c1);
        }
        else if (!x1.equalsIgnoreCase(x2))
        {
          b1 = new BigDecimal(0);
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
        }
      }
      if (h.getFieldValueByFieldNameST("posted_flag") != null)
      {
        if (Tools.isYes(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "Y";
        } else if (Tools.isNo(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "BELUM SETUJUI";
        }
      }
      else {
        status = "";
      }
      XSSFRow row = sheet.createRow(i + 3);
      row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("trx_id").doubleValue());
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      if (h.getFieldValueByFieldNameST("nobuk_realisasi") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("nobuk_realisasi"));
      }
      if (h.getFieldValueByFieldNameBD("realisasi_used") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("realisasi_used").doubleValue());
      }
      if (h.getFieldValueByFieldNameBD("sisa") != null) {
        row.createCell(10).setCellValue(a1.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
      }
      row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
      row.createCell(13).setCellValue(status);
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=realisasi_titipan_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public void printRincianTitipanReinsuranceExcel() throws Exception
  {
    EXCEL_RINCIAN_TITIPAN2_REINSURANCE();
  }

  public void EXCEL_RINCIAN_TITIPAN2_REINSURANCE()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    String sql = "a.trx_id,a.trx_no as titipan_premi,a.counter as ktr,a.applydate as tgltitip,d.accountno as norek,';'||a.pol_no as nopol,a.cc_code as koda,a.amount as nilai,(select coalesce(SUM(y.titipan_premi_used_amount),0) from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id where x.status = 'POST' AND y.titipan_premi_id = a.trx_id and date_trunc('day',y.receipt_date) <= '" + getPerdate() + "' ) as terpakai," + "a.create_date as tglentry,a.cause as kode,a.description as keterangan,a.description_master as bank, " + "(select string_agg(y.receipt_no,'|') from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id " + "where x.status = 'POST' and x.posted_flag = 'Y' AND y.titipan_premi_id = a.trx_id and date_trunc('day',y.receipt_date) > '" + getPerdate() + "' ) as nobuk ";

    sqa.addSelect(sql);

    sqa.addQuery(" from ar_titipan_premi_reinsurance a left join gl_accounts d on d.account_id = a.hdr_accountid ");
    if (Tools.isYes(this.stNotApproved)) {
      sqa.addClause("coalesce(a.approved,'') <> 'Y'");
    } else {
      sqa.addClause(" a.approved = 'Y' ");
    }
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0) {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      } else {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + this.transdatefrom + "'");
      }
    }
    else {
      sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null) {
      sqa.addClause(" date_trunc('day',a.applydate) <= '" + this.transdateto + "'");
    }
    if (this.entrydatefrom != null) {
      sqa.addClause(" date_trunc('day',a.create_date) >= '" + this.entrydatefrom + "'");
    }
    if (this.entrydateto != null) {
      sqa.addClause(" date_trunc('day',a.create_date) <= '" + this.entrydateto + "'");
    }
    if (this.stEntityID != null) {
      sqa.addClause("a.hdr_accountid = '" + this.stEntityID + "'");
    }
    if (this.transNumber != null) {
      sqa.addClause("a.trx_no like '%" + this.transNumber + "%'");
    }
    if (this.description != null) {
      sqa.addClause("a.description like '%" + this.description + "%'");
    }
    if (this.branch != null) {
      sqa.addClause("a.cc_code = '" + this.branch + "'");
    }
    if (this.stCreateWho != null) {
      sqa.addClause("a.create_who = '" + this.stCreateWho + "'");
    }
    String sql2 = "select titipan_premi,ktr,tgltitip,norek,nopol,koda,nilai,(nilai-terpakai) as sisa,tglentry,kode,keterangan,bank,nobuk  from ( " + sqa.getSQL() + " order by a.trx_no,a.counter " + " ) a where (nilai-terpakai) <> 0 order by titipan_premi,ktr ";

    SQLUtil S = new SQLUtil();

    String nama_file = "rincian_titipan_" + System.currentTimeMillis() + ".csv";

    sql2 = "Copy (" + sql2 + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

    PreparedStatement ps = S.setQuery(sql2);

    boolean tes = ps.execute();

    S.release();

    File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
    int length = 0;
    ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

    SessionManager.getInstance().getResponse().setContentType("text/csv");
    SessionManager.getInstance().getResponse().setContentLength((int)file.length());

    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

    int BUFSIZE = 4096;
    byte[] byteBuffer = new byte[BUFSIZE];
    DataInputStream in = new DataInputStream(new FileInputStream(file));
    while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
      outStream.write(byteBuffer, 0, length);
    }
    in.close();
    outStream.close();

    file.delete();
  }

  public void printTitipanReinsurance()
  {
    super.redirect("/pages/gl/report/rpt_titipan_reinsurance.fop?xlang=" + this.printLang);
  }

  public void printTitipanReinsuranceExcel() throws Exception
  {
    DTOList l = EXCEL_TITIPAN_REINSURANCE();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_TITIPAN_REINSURANCE();
  }

    public DTOList EXCEL_TITIPAN_REINSURANCE()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, (select coalesce(SUM(z.titipan_premi_used_amount),0)   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi_reinsurance a  inner join gl_accounts b on b.account_id = a.accountid ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      }
      else
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(this.transdatefrom);
      }
    }
    else
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql = "select a.* from ( " + sqa.getSQL() + " ) a ";
    if (getPrintType().equalsIgnoreCase("1")) {
      sql = sql + " where (amount-realisasi_used) <> 0 ";
    }
    sql = sql + " order by a.trx_id desc,a.trx_no ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_TITIPAN_REINSURANCE()
    throws Exception
  {
    HSSFWorkbook wb = new HSSFWorkbook();

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    int page = 0;
    int baris = 0;
    int j = 0;
    int jumlahBarisPerSheet = 40000;

    BigDecimal sisa = new BigDecimal(0);
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);
      if (i % jumlahBarisPerSheet == 0)
      {
        page += 1;
        HSSFSheet sheet = wb.createSheet("titipan" + page);
        baris = 0;
      }
      HSSFRow row2 = wb.getSheet("titipan" + page).createRow(1);
      if (getApplyDateFrom() != null) {
        row2.createCell(0).setCellValue("Tanggal Titipan : " + DateUtil.getDateStr(getApplyDateFrom()) + " sd " + DateUtil.getDateStr(getApplyDateTo()));
      }
      HSSFRow row1 = wb.getSheet("titipan" + page).createRow(2);
      row1.createCell(0).setCellValue("NOBUK");
      row1.createCell(1).setCellValue("KTR");
      row1.createCell(2).setCellValue("TGL TITIP");
      row1.createCell(3).setCellValue("NOREK");
      row1.createCell(4).setCellValue("NOPOL");
      row1.createCell(5).setCellValue("KODA");
      row1.createCell(6).setCellValue("NILAI");
      row1.createCell(7).setCellValue("SISA");
      row1.createCell(8).setCellValue("TGL ENTRY");
      row1.createCell(9).setCellValue("KODE");
      row1.createCell(10).setCellValue("KETERANGAN");
      row1.createCell(11).setCellValue("NAMA BANK");

      sisa = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));

      HSSFRow row = wb.getSheet("titipan" + page).createRow(baris + 3);
      if (h.getFieldValueByFieldNameST("trx_no") != null) {
        row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      }
      if (h.getFieldValueByFieldNameBD("counter") != null) {
        row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("applydate") != null) {
        row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      }
      if (h.getFieldValueByFieldNameST("hdr_accountno") != null) {
        row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      }
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      if (h.getFieldValueByFieldNameST("cc_code") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
      }
      if (h.getFieldValueByFieldNameBD("amount") != null) {
        row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      }
      if (sisa != null) {
        row.createCell(7).setCellValue(sisa.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("create_date") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("create_date"));
      }
      if (h.getFieldValueByFieldNameST("cause") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("cause"));
      }
      if (h.getFieldValueByFieldNameST("description") != null) {
        row.createCell(10).setCellValue(JSPUtil.xmlEscape(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description"))));
      }
      if (h.getFieldValueByFieldNameST("description_master") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      }
      baris += 1;
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan_" + System.currentTimeMillis() + ".xls;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public DTOList getTitipanPremiExtracomptableList()
    throws Exception
  {
    if (this.titipanPremiExtracomptableList == null)
    {
      this.titipanPremiExtracomptableList = new DTOList();
      this.titipanPremiExtracomptableList.getFilter().activate();
    }
    if (getStSort() == null) {
      setStSort("asc");
    }
    SQLAssembler sqa = getSQATitipanPremiExtracomptable(); 

    sqa.addFilter(this.titipanPremiExtracomptableList.getFilter());

    this.titipanPremiExtracomptableList = sqa.getList(TitipanPremiView.class);

    return this.titipanPremiExtracomptableList;
  }

  public void setTitipanPremiExtracomptableList(DTOList titipanPremiExtracomptableList)
  {
    this.titipanPremiExtracomptableList = titipanPremiExtracomptableList;
  }

  public SQLAssembler getSQATitipanPremiExtracomptable()
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, c.user_name,  (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id and y.receipt_date>='2024-01-01 00:00:00') as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi_extracomptable a  inner join gl_accounts b on b.account_id = a.accountid  left join s_users c on c.user_id = a.create_who");
    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber);
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    sqa.addOrder("a.trx_id desc");
    
    sqa.setLimit(300);

    return sqa;
  }

public void btnSearchTitipanExtracomptable()
    throws Exception
  {
    getTitipanPremiExtracomptableList().getFilter().setCurrentPage(0);
  }

public void btnClickPrintExtracomptable()
    throws Exception
  {
    super.redirect("/pages/gl/report/rpt_realisasititipan_poliskhusus.fop");
  }

public void printTitipanPolisKhusus()
  {
    super.redirect("/pages/gl/report/rpt_titipan_poliskhusus.fop?xlang=" + this.printLang);
  }

public void printRealisasiTitipanExcelPolisKhusus()
    throws Exception
  {
    DTOList l = EXCEL_REALISASI_TITIPAN_EXTRACOMPTABLE();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_REALISASI_TITIPAN_EXTRACOMPTABLE();
  }
 
  public DTOList EXCEL_REALISASI_TITIPAN_EXTRACOMPTABLE()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect("a.*,b.receipt_date,b.receipt_no as nobuk_realisasi,b.titipan_premi_amount as realisasi,b.titipan_premi_used_amount as realisasi_used, (b.titipan_premi_amount-b.titipan_premi_used_amount) as sisa,c.posted_flag, (select count(x.titipan_premi_id) from ar_receipt_lines x where x.titipan_premi_id = a.trx_id) as jumlah ");

    sqa.addQuery(" from ar_titipan_premi_extracomptable a  left join ar_receipt_lines b on b.titipan_premi_id = a.trx_id  left join ar_receipt c on c.ar_receipt_id = b.receipt_id ");

    sqa.addClause(" a.approved = 'Y' ");
    sqa.addClause("c.receipt_date>='2024-01-01 00:00:00'");

    if (this.transdatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) >= ? ");
      sqa.addPar(this.transdatefrom);
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.realisasifrom != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) >= ? ");
      sqa.addPar(this.realisasifrom);
    }
    if (getRealisasito() != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(getRealisasito());
    }
    if (this.perdate != null)
    {
      sqa.addClause(" date_trunc('day',b.receipt_date) <= ? ");
      sqa.addPar(this.perdate);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no = ?");
      sqa.addPar(this.transNumber);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql2 = sqa.getSQL() + " order by a.trx_no,a.counter,c.posted_flag desc,b.ar_rcl_id ";

    DTOList l = ListUtil.getDTOListFromQuery(sql2, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_REALISASI_TITIPAN_EXTRACOMPTABLE()
    throws Exception
  {
    XSSFWorkbook wb = new XSSFWorkbook();

    XSSFSheet sheet = wb.createSheet("new sheet");

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    BigDecimal a1 = new BigDecimal(0);
    BigDecimal b1 = new BigDecimal(0);
    BigDecimal c1 = new BigDecimal(0);
    String status = null;
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);

      XSSFRow row1 = sheet.createRow(0);
      row1.createCell(0).setCellValue("REALISASI TITIPAN PREMI POLIS KHUSUS");

      XSSFRow row0 = sheet.createRow(2);
      row0.createCell(0).setCellValue("trxid");
      row0.createCell(1).setCellValue("nobuk titipan");
      row0.createCell(2).setCellValue("ktr");
      row0.createCell(3).setCellValue("noper");
      row0.createCell(4).setCellValue("naper");
      row0.createCell(5).setCellValue("nopol");
      row0.createCell(6).setCellValue("nilai");
      row0.createCell(7).setCellValue("tgltitip");
      row0.createCell(8).setCellValue("nobuk realisasi");
      row0.createCell(9).setCellValue("terpakai");
      row0.createCell(10).setCellValue("sisa");
      row0.createCell(11).setCellValue("tglrealisasi");
      row0.createCell(12).setCellValue("keterangan");
      row0.createCell(13).setCellValue("efektif");
      if (i == 0)
      {
        a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
      }
      else if (i > 0)
      {
        HashDTO s = (HashDTO)list.get(i - 1);
        String x1 = s.getFieldValueByFieldNameBD("trx_id").toString();
        String x2 = h.getFieldValueByFieldNameBD("trx_id").toString();
        if (x1.equalsIgnoreCase(x2))
        {
          b1 = BDUtil.add(b1, s.getFieldValueByFieldNameBD("realisasi_used"));
          c1 = BDUtil.add(b1, h.getFieldValueByFieldNameBD("realisasi_used"));
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), c1);
        }
        else if (!x1.equalsIgnoreCase(x2))
        {
          b1 = new BigDecimal(0);
          a1 = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));
        }
      }
      if (h.getFieldValueByFieldNameST("posted_flag") != null)
      {
        if (Tools.isYes(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "Y";
        } else if (Tools.isNo(h.getFieldValueByFieldNameST("posted_flag"))) {
          status = "BELUM SETUJUI";
        }
      }
      else {
        status = "";
      }
      XSSFRow row = sheet.createRow(i + 3);
      row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("trx_id").doubleValue());
      row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      if (h.getFieldValueByFieldNameST("nobuk_realisasi") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("nobuk_realisasi"));
      }
      if (h.getFieldValueByFieldNameBD("realisasi_used") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("realisasi_used").doubleValue());
      }
      if (h.getFieldValueByFieldNameBD("sisa") != null) {
        row.createCell(10).setCellValue(a1.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
      }
      row.createCell(12).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description")));
      row.createCell(13).setCellValue(status);
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=realisasi_titipan_" + System.currentTimeMillis() + ".xlsx;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public void printRincianTitipanExcelPolisKhusus()
    throws Exception
  {
    EXCEL_RINCIAN_TITIPAN_POLIS_KHUSUS();
  }

  public void EXCEL_RINCIAN_TITIPAN_POLIS_KHUSUS()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    String sql = "a.trx_id,a.trx_no as titipan_premi,a.counter as ktr,a.applydate as tgltitip,d.accountno as norek,';'||a.pol_no as nopol,a.cc_code as koda,a.amount as nilai,(select coalesce(SUM(y.titipan_premi_used_amount),0) from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id where x.status = 'POST' AND y.titipan_premi_id = a.trx_id and y.receipt_date>='2024-01-01 00:00:00' and date_trunc('day',y.receipt_date) <= '" + getPerdate() + "' ) as terpakai," + "a.create_date as tglentry,a.cause as kode,a.description as keterangan,a.description_master as bank, " + "(select string_agg(y.receipt_no,'|') from ar_receipt x inner join ar_receipt_lines y on x.ar_receipt_id = y.receipt_id " + "where x.status = 'POST' and x.posted_flag = 'Y' AND y.titipan_premi_id = a.trx_id and y.receipt_date>='2024-01-01 00:00:00' and date_trunc('day',y.receipt_date) > '" + getPerdate() + "' ) as nobuk ";

    sqa.addSelect(sql);

    sqa.addQuery(" from ar_titipan_premi_extracomptable a left join gl_accounts d on d.account_id = a.hdr_accountid ");
    if (Tools.isYes(this.stNotApproved)) {
      sqa.addClause("coalesce(a.approved,'') <> 'Y'");
    } else {
      sqa.addClause(" a.approved = 'Y' ");
    }
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0) {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      } else {
        sqa.addClause("date_trunc('day',a.applydate) >= '" + this.transdatefrom + "'");
      }
    }
    else {
      sqa.addClause("date_trunc('day',a.applydate) >= '" + Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null) {
      sqa.addClause(" date_trunc('day',a.applydate) <= '" + this.transdateto + "'");
    }
    if (this.entrydatefrom != null) {
      sqa.addClause(" date_trunc('day',a.create_date) >= '" + this.entrydatefrom + "'");
    }
    if (this.entrydateto != null) {
      sqa.addClause(" date_trunc('day',a.create_date) <= '" + this.entrydateto + "'");
    }
    if (this.stEntityID != null) {
      sqa.addClause("a.hdr_accountid = '" + this.stEntityID + "'");
    }
    if (this.transNumber != null) {
      sqa.addClause("a.trx_no like '%" + this.transNumber + "%'");
    }
    if (this.description != null) {
      sqa.addClause("a.description like '%" + this.description + "%'");
    }
    if (this.branch != null) {
      sqa.addClause("a.cc_code = '" + this.branch + "'");
    }
    if (this.stCreateWho != null) {
      sqa.addClause("a.create_who = '" + this.stCreateWho + "'");
    }
    String sql2 = "select titipan_premi,ktr,tgltitip,norek,nopol,koda,nilai,(nilai-terpakai) as sisa,tglentry,kode,keterangan,bank,nobuk  from ( " + sqa.getSQL() + " order by a.trx_no,a.counter " + " ) a where (nilai-terpakai) <> 0 order by titipan_premi,ktr ";

    SQLUtil S = new SQLUtil();

    String nama_file = "rincian_titipan_poliskhusus_" + System.currentTimeMillis() + ".csv";

    sql2 = "Copy (" + sql2 + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

    PreparedStatement ps = S.setQuery(sql2);

    boolean tes = ps.execute();

    S.release();

    File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
    int length = 0;
    ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

    SessionManager.getInstance().getResponse().setContentType("text/csv");
    SessionManager.getInstance().getResponse().setContentLength((int)file.length());

    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

    int BUFSIZE = 4096;
    byte[] byteBuffer = new byte[BUFSIZE];
    DataInputStream in = new DataInputStream(new FileInputStream(file));
    while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
      outStream.write(byteBuffer, 0, length);
    }
    in.close();
    outStream.close();

    file.delete();
  }

  public void printTitipanExcelPolisKhusus()
    throws Exception
  {
    DTOList l = EXCEL_TITIPAN_POLIS_KHUSUS();

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    EXPORT_TITIPAN_POLIS_KHUSUS();
  }

  public DTOList EXCEL_TITIPAN_POLIS_KHUSUS()
    throws Exception
  {
    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.*,b.accountno, (select coalesce(SUM(z.titipan_premi_used_amount),0)   from ar_receipt y    inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id   where y.status = 'POST' AND z.titipan_premi_id = a.trx_id and y.receipt_date>='2024-01-01 00:00:00') as realisasi_used");

    sqa.addQuery(" from ar_titipan_premi_extracomptable a  inner join gl_accounts b on b.account_id = a.accountid ");

    sqa.addClause(" a.approved = 'Y' ");
    if (this.transdatefrom != null)
    {
      if (Tools.compare(DateUtil.getYear(this.transdatefrom), Parameter.readString("SPLIT_DATA_TP")) <= 0)
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
      }
      else
      {
        sqa.addClause("date_trunc('day',a.applydate) >= ?");
        sqa.addPar(this.transdatefrom);
      }
    }
    else
    {
      sqa.addClause("date_trunc('day',a.applydate) >= ?");
      sqa.addPar(Parameter.readString("SPLIT_DATA_TP") + "-01-01'");
    }
    if (this.transdateto != null)
    {
      sqa.addClause(" date_trunc('day',a.applydate) <= ? ");
      sqa.addPar(this.transdateto);
    }
    if (this.entrydatefrom != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) >= ? ");
      sqa.addPar(this.entrydatefrom);
    }
    if (this.entrydateto != null)
    {
      sqa.addClause(" date_trunc('day',a.create_date) <= ? ");
      sqa.addPar(this.entrydateto);
    }
    if (this.stEntityID != null)
    {
      sqa.addClause("a.hdr_accountid = ?");
      sqa.addPar(this.stEntityID);
    }
    if (this.transNumber != null)
    {
      sqa.addClause("a.trx_no like ?");
      sqa.addPar("%" + this.transNumber + "%");
    }
    if (this.accountCode != null)
    {
      sqa.addClause("b.accountno = ?");
      sqa.addPar(this.accountCode);
    }
    if (this.description != null)
    {
      sqa.addClause("a.description like ?");
      sqa.addPar("%" + this.description + "%");
    }
    if (this.branch != null)
    {
      sqa.addClause("a.cc_code = ?");
      sqa.addPar(this.branch);
    }
    String sql = "select a.* from ( " + sqa.getSQL() + " ) a ";
    if (getPrintType().equalsIgnoreCase("1")) {
      sql = sql + " where (round(amount,2)-round(realisasi_used,2)) <> 0 ";
    }
    sql = sql + " order by a.trx_id desc,a.trx_no ";

    DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), HashDTO.class);

    SessionManager.getInstance().getRequest().setAttribute("RPT", l);

    return l;
  }

  public void EXPORT_TITIPAN_POLIS_KHUSUS()
    throws Exception
  {
    HSSFWorkbook wb = new HSSFWorkbook();

    DTOList list = (DTOList)SessionManager.getInstance().getRequest().getAttribute("RPT");

    int page = 0;
    int baris = 0;
    int j = 0;
    int jumlahBarisPerSheet = 40000;

    BigDecimal sisa = new BigDecimal(0);
    for (int i = 0; i < list.size(); i++)
    {
      HashDTO h = (HashDTO)list.get(i);
      if (i % jumlahBarisPerSheet == 0)
      {
        page += 1;
        HSSFSheet sheet = wb.createSheet("titipan" + page);
        baris = 0;
      }
      HSSFRow row2 = wb.getSheet("titipan" + page).createRow(1);
      if (getApplyDateFrom() != null) {
        row2.createCell(0).setCellValue("Tanggal Titipan : " + DateUtil.getDateStr(getApplyDateFrom()) + " sd " + DateUtil.getDateStr(getApplyDateTo()));
      }
      HSSFRow row1 = wb.getSheet("titipan" + page).createRow(2);
      row1.createCell(0).setCellValue("NOBUK");
      row1.createCell(1).setCellValue("KTR");
      row1.createCell(2).setCellValue("TGL TITIP");
      row1.createCell(3).setCellValue("NOREK");
      row1.createCell(4).setCellValue("NOPOL");
      row1.createCell(5).setCellValue("KODA");
      row1.createCell(6).setCellValue("NILAI");
      row1.createCell(7).setCellValue("SISA");
      row1.createCell(8).setCellValue("TGL ENTRY");
      row1.createCell(9).setCellValue("KODE");
      row1.createCell(10).setCellValue("KETERANGAN");
      row1.createCell(11).setCellValue("NAMA BANK");

      sisa = BDUtil.sub(h.getFieldValueByFieldNameBD("amount"), h.getFieldValueByFieldNameBD("realisasi_used"));

      HSSFRow row = wb.getSheet("titipan" + page).createRow(baris + 3);
      if (h.getFieldValueByFieldNameST("trx_no") != null) {
        row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
      }
      if (h.getFieldValueByFieldNameBD("counter") != null) {
        row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("counter").doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("applydate") != null) {
        row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
      }
      if (h.getFieldValueByFieldNameST("hdr_accountno") != null) {
        row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("hdr_accountno"));
      }
      if (h.getFieldValueByFieldNameST("pol_no") != null) {
        row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
      }
      if (h.getFieldValueByFieldNameST("cc_code") != null) {
        row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
      }
      if (h.getFieldValueByFieldNameBD("amount") != null) {
        row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
      }
      if (sisa != null) {
        row.createCell(7).setCellValue(sisa.doubleValue());
      }
      if (h.getFieldValueByFieldNameDT("create_date") != null) {
        row.createCell(8).setCellValue(h.getFieldValueByFieldNameDT("create_date"));
      }
      if (h.getFieldValueByFieldNameST("cause") != null) {
        row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("cause"));
      }
      if (h.getFieldValueByFieldNameST("description") != null) {
        row.createCell(10).setCellValue(JSPUtil.xmlEscape(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("description"))));
      }
      if (h.getFieldValueByFieldNameST("description_master") != null) {
        row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("description_master"));
      }
      baris += 1;
    }
    SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan_" + System.currentTimeMillis() + ".xls;");
    SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    wb.write(sosStream);
    sosStream.flush();
    sosStream.close();
  }

  public void clickPrintAcc() throws Exception {

        loadFormList();

        String pf = stPrintForm;

        final ArrayList plist = new ArrayList();

        plist.add(pf + "_" + stReport);

        plist.add(pf);

        String urx = null;

        logger.logDebug("print: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/gl/report/" + s + ".fop?xlang=" + stLang;
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        super.redirect(urx);

    }

    public String getPeriodBeforeTitleDescription() throws Exception {

        long LperiodTo = Long.parseLong(periodTo);

        LperiodTo = LperiodTo - 1;

        String PeriodBeforeTo = String.valueOf(LperiodTo);

        if (year != null) {

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(PeriodBeforeTo, year);

            Date d = pd.getDtEndDate();

            return "PER " + DateUtil.getDateStr(d, "d ^^ yyyy");
        }

        if (yearFrom != null) {
            PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodFrom, yearFrom);
            PeriodDetailView pd2 = PeriodManager.getInstance().getPeriod(PeriodBeforeTo, yearFrom);

            Date d1 = pd1.getDtStartDate();
            Date d2 = pd2.getDtEndDate();

            return //"PERIODE "+ DateUtil.getDateStr(d1,"d ^^ yyyy") +" - " + DateUtil.getDateStr(d2,"d ^^ yyyy")
                    "PER " + DateUtil.getDateStr(d2, "d ^^ yyyy");
        }

        return "???";
    }
    int lineNo;

    public String getLineNo() {
        lineNo++;

        return String.valueOf(lineNo);
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, branch);

        return costcenter;
    }

    private String region;
    private String regionDesc;

    public RegionView getRegionCenter() {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, region);

        return reg;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the regionDesc
     */
    public String getRegionDesc() {
        return regionDesc;
    }

    /**
     * @param regionDesc the regionDesc to set
     */
    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }

    public SQLAssembler getSQACalk(String acctFrom) {

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String fld = "bal";
        String fldprev = "bal";
        String ballist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");
        String ballistprev = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, fldprev, "+");

        int prevyear = Integer.parseInt(yearFrom) - 1;
        String stprevyear = String.valueOf(prevyear);

        String select = null;
        String account[] = acctFrom.split("[\\|]");
        String akun = "substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
        for (int k = 1; k < account.length; k++) {
            akun = akun + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
        }
        if (account.length == 1) {
            select = "b.account_id,b.accountno,b.group_type,b.group_name as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as thismonth ";
        } else if (account.length > 1) {
            select = "b.account_id,b.accountno,b.group_type,b.group_name as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as thismonth ";
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(select);

        sqa.addQuery(" from gl_accounts b ");

        sqa.addClause("b.acctype is null");
        sqa.addClause("(" + akun + ")");

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

//        sqa.addOrder(" 1 ");
        return sqa;
    }

    public SQLAssembler getSQACalkPoltype(String acctFrom) {

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String fld = "bal";
        String fldprev = "bal";
        String ballist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");
        String ballistprev = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, fldprev, "+");

        int prevyear = Integer.parseInt(yearFrom) - 1;
        String stprevyear = String.valueOf(prevyear);

        String select = null;
        String account[] = acctFrom.split("[\\|]");
        String akun = "substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
        for (int k = 1; k < account.length; k++) {
            akun = akun + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
        }
        if (account.length == 1) {
            select = "b.account_id,substr(b.accountno,14,2) as koda,c.description as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as thismonth ";
        } else if (account.length > 1) {
            select = "b.account_id,substr(b.accountno,14,2) as koda,c.description as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as thismonth ";
        }

        final SQLAssembler sqa = new SQLAssembler();

        /*
        sqa.addSelect("b.account_id,substr(b.accountno,14,2) as koda,c.description as description,"
        + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as prevyear,"
        + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as prevmonth,"
        + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as thismonth ");
         */

        sqa.addSelect(select);

        sqa.addQuery(" from gl_accounts b "
                + "left join gl_cost_center c on c.cc_code = substr(b.accountno,14,2)");

        sqa.addClause("b.acctype is null");
        sqa.addClause("(" + akun + ")");

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

//        sqa.addOrder(" 1 ");
        return sqa;
    }

    public SQLAssembler getSQACalkCob(String acctFrom) {

        long lPeriodFrom = Long.parseLong("0");
        if (getPeriodFrom().equalsIgnoreCase("1")) {
            lPeriodFrom = lPeriodFrom;
        } else {
            lPeriodFrom = getLPeriodFrom();
        }

        String fld = "bal";
        String fldprev = "bal";
        String ballist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");
        String ballistprev = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, fldprev, "+");

        int prevyear = Integer.parseInt(yearFrom) - 1;
        String stprevyear = String.valueOf(prevyear);

        String select = null;
        String account[] = acctFrom.split("[\\|]");
        String akun = "substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
        for (int k = 1; k < account.length; k++) {
            akun = akun + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
        }
        if (account.length == 1) {
            select = "b.account_id,substr(b.accountno,11,2) as cob,c.description as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + account[0].length() + ")) between '" + acctFrom + "' and '" + acctFrom + "') and x.account_id = b.account_id ) as thismonth ";
        } else if (account.length > 1) {
            select = "b.account_id,substr(b.accountno,11,2) as cob,c.description as description,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevyear,"
                    + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as prevmonth,"
                    + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
                    + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and (" + akun + ") and x.account_id = b.account_id ) as thismonth ";
        }

        final SQLAssembler sqa = new SQLAssembler();

        /*
        sqa.addSelect("b.account_id,substr(b.accountno,11,2) as cob,c.description as description,"
        + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + stprevyear + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as prevyear,"
        + "( select coalesce(sum(" + ballistprev + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as prevmonth,"
        + "( select coalesce(sum(" + ballist + "),0) from gl_acct_bal2 x left join gl_accounts y on y.account_id = x.account_id "
        + "where x.period_year = '" + yearFrom + "' and x.idr_flag = 'Y' and y.acctype is null and ((substr(y.accountno,1," + l + ")) between '" + acctFrom + "' and '" + acctTo + "') and x.account_id = b.account_id ) as thismonth ");
         */

        sqa.addSelect(select);

        sqa.addQuery(" from gl_accounts b "
                + "left join ins_policy_types c on c.pol_type_id_desc = substr(b.accountno,11,2) ");

        sqa.addClause("b.acctype is null");
        sqa.addClause("(" + akun + ")");

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

//        sqa.addOrder(" 1 ");
        return sqa;
    }

    public HashMap vars = new HashMap();

    public HashMap vars() {
        return vars;
    }

    public String getMonthPrevYearTitleDescription() throws Exception {

        int prevyear = Integer.parseInt(yearFrom) - 1;

        String stprevyear = String.valueOf(prevyear);

        PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodTo, stprevyear);

        Date d1 = pd1.getDtEndDate();

        return DateUtil.getDateStr(d1, "^^ yyyy");
    }

    public SQLAssembler getSQAArusKas(String acctFrom) {

//        long lPeriodFrom = Long.parseLong("0");
//        if (getPeriodFrom().equalsIgnoreCase("1")) {
//            lPeriodFrom = lPeriodFrom;
//        } else {
//            lPeriodFrom = getLPeriodFrom();
//        }

//        String fld = "bal";
//        String fldprev = "bal";
//        String ballist = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo(), fld, "+");
//        String ballistprev = GLUtil.getPeriodList(lPeriodFrom, getLPeriodTo() - 1, fldprev, "+");
//
//        int prevyear = Integer.parseInt(yearFrom) - 1;
//        String stprevyear = String.valueOf(prevyear);
//
//        String select = null;

        String gljedetail = null;
        if (yearFrom != null) {
            String gljedetailYear = yearFrom;
            String gljedetailYearCurrent = DateUtil.getYear(new Date());

            if (gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
                gljedetail = "gl_je_detail";
            } else {
                gljedetail = "gl_je_detail_" + yearFrom;
            }
        } else {
            gljedetail = "gl_je_detail";
        }

        String account[] = acctFrom.split("[\\|]");
        String akun = "substr(b.accountno,1," + account[0].length() + ") = '" + account[0] + "'";
        for (int k = 1; k < account.length; k++) {
            akun = akun + " or substr(b.accountno,1," + account[k].length() + ") = '" + account[k] + "'";
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.fiscal_year,a.period_no,substr(b.accountno,14,2) as koda,substr(b.accountno,1,2) as code,"
                + "sum(a.credit) as credit,sum(a.debit) as debit,sum(a.credit-a.debit) as balance ");

        sqa.addQuery(" from " + gljedetail + " a "
                + "left join gl_accounts b on b.account_id = a.accountid ");

//        sqa.addClause("substr(b.accountno,1,2) not in ('12')");
        sqa.addClause("substr(a.trx_no,1,1) in ('A','C','D')");
        sqa.addClause("(" + akun + ")");

        if (periodFrom != null) {
            sqa.addClause("a.period_no between ? and ? ");
            sqa.addPar(periodFrom);
            sqa.addPar(periodTo);
        }

        if (yearFrom != null) {
            sqa.addClause("a.fiscal_year = ?");
            sqa.addPar(yearFrom);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ?");
            sqa.addPar(branch);
        }

        return sqa;
    }
    

}


/* Location:              F:\DONI\decompiler\src\webapps\com.ear\!\webfin\gl\form\GLListForm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */