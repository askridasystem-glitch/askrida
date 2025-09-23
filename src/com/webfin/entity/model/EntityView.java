/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.company.model.CompanyView;
import com.webfin.insurance.model.InsuranceEntityView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Date;

public class EntityView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ent_master
(
  ent_id int8 NOT NULL,
  ent_class varchar(32),
  ent_name varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ent_master_pk PRIMARY KEY (ent_id)
    change_who varchar(32),
  first_name varchar(32),
  last_name varchar(32),
  middle_name varchar(32),
  title varchar(16),
   timestamp,

)
   */

   public static String tableName = "ent_master";

   public static String comboFields[] = {"ent_id","ent_name", "short_name","cabang","address","gl_code","rekening","saldo","customer_status","customer_category","tax_file","functionary_name","functionary_position","rc_no"};

   public static String fieldMap[][] = {
      {"stEntityID","ent_id*pk"},
      {"stEntityClass","ent_class"},
      {"stEntityName","ent_name"},
      {"stFirstName","first_name"},
      {"stLastName","last_name"},
      {"stShortName","short_name"},
      {"stEntityAddressPrimaryID","ent_addr_id_prim"},
      {"stMiddleName","middle_name"},
      {"stTitle","title"},
      {"dtBirthDate","birth_date"},
      {"stSexID","sex_id"},
      {"stEntityType","ent_type"},
      {"stCustomerStatus","customer_status"},
      {"stFrontOfficeCode","front_office_code"},
      {"stTaxFile","tax_file"},
      {"stARTermID","ar_term_id"},
      {"stAPTermID","ap_term_id"},
      {"stCountryID","country_id"},
      {"stIdentificationType","ident_type"},
      {"stIdentificationNumber","indent_number"},
      {"stMaritalStatus","marital_status"},
      {"lgDependentNum","dependent_num"},
      {"stReligionCode","religion"},
      {"stSalesTax","sales_tax"},
      {"stBusinessLine","bus_line"},
      {"stGLCode","gl_code"},
      {"dtActiveDate","active_date"},
      {"stInsInwardFlag","ins_inward_flag"},
      {"stInsOutwardFlag","ins_outward_flag"},
      {"stInsCompanyFlag","ins_company_flag"},
      {"stCaptiveFlag","captive_flag"},
      {"stCategory1","category1"},
      {"stRefEntityID","ref_ent_id"},
      
      {"stReasEntityID","reas_ent_id"},
      {"stAddress","address"},

      {"stShareLevel0","sharef0"},
      {"stShareLevel1","sharef1"},
      {"stShareLevel2","sharef2"},
      {"stRef1","ref1"},
      {"stRef2","ref2"},
      {"stRef3","ref3"},
      {"stRef4","ref4"},
      {"stRef5","ref5"},
      {"stRef6","ref6"},
      {"stRef7","ref7"},
      {"stCostCenterCode","cc_code"},
      {"stFunctionaryName","functionary_name"},
      {"stFunctionaryPosition","functionary_position"},
      {"stRcNo","rc_no"},
      {"stCustomerCategory","customer_category*n"},
      {"stFinanceFlag","finance_flag"},
      {"stTaxCode","tax_code"},
      {"stRekening","rekno"},
      {"stRekeningNo","rekening*n"},
      {"stSaldo","saldo*n"},
      {"stCabang","cabang*n"},
      {"stReferenceEntityID","reference_ent_id"},
      {"stClaimTaxCode","claim_tax_code"},
      {"stFilePhysic","file_physic"},
      {"stEntityLevel", "ent_level"},
      {"stIDCard", "id_card_agent"},
      {"stPKSCard", "id_pks_agent"},
      {"stOJKCard", "id_ojk_agent"},
      {"stRegionID", "region_id"},
      {"stAgentID", "agent_id"},
      {"stSupervisiCode", "supervisi_code"},
      {"stReferenceGatewayCode","ref_gateway_code"},
      {"stBirthPlace","birth_place"},

      {"stLOBCare","kode_lob_care*n"},
      {"stAddressFix","address_fix*n"},
      {"stTaxType","tax_type*n"},
      {"stTaxFileCare","tax_file_care*n"},
      {"stBusinessSourceCode","business_source_code"},
      {"stNomorRekening","no_rekening"},

   };

   /*
ALTER TABLE ent_master ADD COLUMN sharef0 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef1 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef2 varchar(32);

   */

   private String stRef1;
   private String stRef2;
   private String stRef3;
   private String stRef4;
   private String stRefEntityID;
   private String stRef5;
   private String stRef6;
   private String stRef7;
   private String stCostCenterCode;
   private String stShareLevel0;
   private String stShareLevel1;
   private String stShareLevel2;
   private String stEntityAddressPrimaryID;
   private String stEntityID;
   private String stEntityName;
   private String stEntityClass;
   private String stFirstName;
   private String stLastName;
   private String stMiddleName;
   private String stTitle;
   private Date dtBirthDate;
   private String stSexID;
   private String stShortName;
   private String stAddress;

   private String stEntityType;
   private String stCustomerStatus;
   private String stFrontOfficeCode;
   private String stTaxFile;
   private String stARTermID;
   private String stAPTermID;
   private String stCountryID;
   private String stIdentificationType;
   private String stIdentificationNumber;
   private String stMaritalStatus;
   private Long lgDependentNum;
   private String stReligionCode;
   private String stSalesTax;
   private String stBusinessLine;
   private String stGLCode;
   private String stInsInwardFlag;
   private String stInsOutwardFlag;
   private String stInsCompanyFlag;
   private String stCaptiveFlag;
   private String stCategory1;
   private Date dtActiveDate;
   private String stFunctionaryName;
   private String stFunctionaryPosition;
   private String stRcNo;
   private String stCustomerCategory;
   private String stFinanceFlag;
   private String stTaxCode;

   private DTOList addresses;
   private EntityAddressView primaryAddress;
   
   private String stReasEntityID;
   
   private String stRekening;
   private String stSaldo;
   private String stCabang;
   private String stRekeningNo;
   private String stReferenceEntityID;
   private String stClaimTaxCode;
   private String stFilePhysic;

    private String stEntityLevel;
    private String stIDCard;
    private String stPKSCard;
    private String stOJKCard;
    private String stRegionID;
    private String stAgentID;
    private String stSupervisiCode;
    private String stReferenceGatewayCode;
    private String stBirthPlace;

    private CompanyView company;
    private DTOList entityDocuments;
    private String stLOBCare;
    private String stAddressFix;
    private String stTaxType;
    private String stTaxFileCare;
    private String stBusinessSourceCode;
    private String stNomorRekening;

    public String getStNomorRekening() {
        return stNomorRekening;
    }

    public void setStNomorRekening(String stNomorRekening) {
        this.stNomorRekening = stNomorRekening;
    }

    public String getStBusinessSourceCode() {
        return stBusinessSourceCode;
    }

    public void setStBusinessSourceCode(String stBusinessSourceCode) {
        this.stBusinessSourceCode = stBusinessSourceCode;
    }

    public String getStTaxFileCare() {
        return stTaxFileCare;
    }

    public void setStTaxFileCare(String stTaxFileCare) {
        this.stTaxFileCare = stTaxFileCare;
    }

    public String getStTaxType() {
        return stTaxType;
    }

    public void setStTaxType(String stTaxType) {
        this.stTaxType = stTaxType;
    }

    public String getStAddressFix() {
        return stAddressFix;
    }

    public void setStAddressFix(String stAddressFix) {
        this.stAddressFix = stAddressFix;
    }

    public String getStLOBCare() {
        return stLOBCare;
    }

    public void setStLOBCare(String stLOBCare) {
        this.stLOBCare = stLOBCare;
    }

    public String getStBirthPlace() {
        return stBirthPlace;
    }

    public void setStBirthPlace(String stBirthPlace) {
        this.stBirthPlace = stBirthPlace;
    }

   public String getStShortName() {
      return stShortName;
   }

   public void setStShortName(String stShortName) {
      this.stShortName = stShortName;
   }

   public DTOList getAddresses() {
      loadAddresses();
      return addresses;
   }

   private void loadAddresses() {
      try {
         if (addresses == null)
            addresses = ListUtil.getDTOListFromQuery(
                    "select * from ent_address where ent_id = ?",
                    new Object [] {stEntityID},
                    EntityAddressView.class
            );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setAddresses(DTOList addresses) {
      this.addresses = addresses;
   }

   public String getStCostCenterCode() {
      return stCostCenterCode;
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
   }

   public EntityAddressView getPrimaryAddress() {
      return primaryAddress;
   }

   public void setPrimaryAddress(EntityAddressView primaryAddress) {
      this.primaryAddress = primaryAddress;
   }

   public String getStFirstName() {
      return stFirstName;
   }

   public void setStFirstName(String stFirstName) {
      this.stFirstName = stFirstName;
   }

   public String getStLastName() {
      return stLastName;
   }

   public void setStLastName(String stLastName) {
      this.stLastName = stLastName;
   }

   public String getStMiddleName() {
      return stMiddleName;
   }

   public void setStMiddleName(String stMiddleName) {
      this.stMiddleName = stMiddleName;
   }

   public String getStTitle() {
      return stTitle;
   }

   public void setStTitle(String stTitle) {
      this.stTitle = stTitle;
   }

   public Date getDtBirthDate() {
      return dtBirthDate;
   }

   public void setDtBirthDate(Date dtBirthDate) {
      this.dtBirthDate = dtBirthDate;
   }

   public String getStSexID() {
      return stSexID;
   }

   public void setStSexID(String stSexID) {
      this.stSexID = stSexID;
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

   public InsuranceEntityView getInsuranceEntity() {
      return (InsuranceEntityView) DTOPool.getInstance().getDTO(InsuranceEntityView.class, stEntityID);
   }

   public void createPrimaryAddress() {

      final EntityAddressView eaddr = new EntityAddressView();

      eaddr.setStPrimaryFlag("Y");

      eaddr.markNew();

      if (addresses==null) addresses = new DTOList();

      addresses.add(eaddr);
   }

   public String getStFullName() {
      return stEntityName;
   }

   public void setStFullName(String stFullName) {
      this.stEntityName = stFullName;
   }

   public String getStEntityType() {
      return stEntityType;
   }

   public void setStEntityType(String stEntityType) {
      this.stEntityType = stEntityType;
   }

   public String getStCustomerStatus() {
      return stCustomerStatus;
   }

   public void setStCustomerStatus(String stCustomerStatus) {
      this.stCustomerStatus = stCustomerStatus;
   }

   public String getStFrontOfficeCode() {
      return stFrontOfficeCode;
   }

   public void setStFrontOfficeCode(String stFrontOfficeCode) {
      this.stFrontOfficeCode = stFrontOfficeCode;
   }

   public String getStTaxFile() {
      return stTaxFile;
   }

   public void setStTaxFile(String stTaxFile) {
      this.stTaxFile = stTaxFile;
   }

   public String getStARTermID() {
      return stARTermID;
   }

   public void setStARTermID(String stARTermID) {
      this.stARTermID = stARTermID;
   }

   public String getStAPTermID() {
      return stAPTermID;
   }

   public void setStAPTermID(String stAPTermID) {
      this.stAPTermID = stAPTermID;
   }

   public String getStCountryID() {
      return stCountryID;
   }

   public void setStCountryID(String stCountryID) {
      this.stCountryID = stCountryID;
   }

   public String getStIdentificationType() {
      return stIdentificationType;
   }

   public void setStIdentificationType(String stIdentificationType) {
      this.stIdentificationType = stIdentificationType;
   }

   public String getStIdentificationNumber() {
      return stIdentificationNumber;
   }

   public void setStIdentificationNumber(String stIdentificationNumber) {
      this.stIdentificationNumber = stIdentificationNumber;
   }

   public String getStMaritalStatus() {
      return stMaritalStatus;
   }

   public void setStMaritalStatus(String stMaritalStatus) {
      this.stMaritalStatus = stMaritalStatus;
   }

   public Long getLgDependentNum() {
      return lgDependentNum;
   }

   public void setLgDependentNum(Long lgDependentNum) {
      this.lgDependentNum = lgDependentNum;
   }

   public String getStReligionCode() {
      return stReligionCode;
   }

   public void setStReligionCode(String stReligionCode) {
      this.stReligionCode = stReligionCode;
   }

   public String getStSalesTax() {
      return stSalesTax;
   }

   public void setStSalesTax(String stSalesTax) {
      this.stSalesTax = stSalesTax;
   }

   public String getStBusinessLine() {
      return stBusinessLine;
   }

   public void setStBusinessLine(String stBusinessLine) {
      this.stBusinessLine = stBusinessLine;
   }

   public Date getDtActiveDate() {
      return dtActiveDate;
   }

   public void setDtActiveDate(Date dtActiveDate) {
      this.dtActiveDate = dtActiveDate;
   }

   public void setStEntityClass(String stEntityClass) {
      this.stEntityClass = stEntityClass;
   }

   public String getStEntityClass() {
      return stEntityClass;
   }

   public boolean isIndividual() {
      return FinCodec.EntityClass.INDIVIDUAL.equalsIgnoreCase(stEntityClass);
   }

   public boolean isInstitutional() {
      return FinCodec.EntityClass.INSTITUTIONAL.equalsIgnoreCase(stEntityClass);
   }

   public void setStGLCode(String stGLCode) {
      this.stGLCode = stGLCode;
   }

   public String getStGLCode() {
      return stGLCode;
   }

   public String getStInsInwardFlag() {
      return stInsInwardFlag;
   }

   public void setStInsInwardFlag(String stInsInwardFlag) {
      this.stInsInwardFlag = stInsInwardFlag;
   }

   public String getStInsOutwardFlag() {
      return stInsOutwardFlag;
   }

   public void setStInsOutwardFlag(String stInsOutwardFlag) {
      this.stInsOutwardFlag = stInsOutwardFlag;
   }


   public void setStInsCompanyFlag(String stInsCompanyFlag) {
      this.stInsCompanyFlag = stInsCompanyFlag;
   }

   public String getStInsCompanyFlag() {
      return stInsCompanyFlag;
   }

   public String getStCaptiveFlag() {
      return stCaptiveFlag;
   }

   public void setStCaptiveFlag(String stCaptiveFlag) {
      this.stCaptiveFlag = stCaptiveFlag;
   }

   public String getStCategory1() {
      return stCategory1;
   }

   public void setStCategory1(String stCategory1) {
      this.stCategory1 = stCategory1;
   }

   public String getStAddress() {
      return stAddress;
   }

   public void setStAddress(String stAddress) {
      this.stAddress = stAddress;
   }

   public String getStEntityAddressPrimaryID() {
      return stEntityAddressPrimaryID;
   }

   public void setStEntityAddressPrimaryID(String stEntityAddressPrimaryID) {
      this.stEntityAddressPrimaryID = stEntityAddressPrimaryID;
   }

   public String getStShareLevel0() {
      return stShareLevel0;
   }

   public void setStShareLevel0(String stShareLevel0) {
      this.stShareLevel0 = stShareLevel0;
   }

   public String getStShareLevel1() {
      return stShareLevel1;
   }

   public void setStShareLevel1(String stShareLevel1) {
      this.stShareLevel1 = stShareLevel1;
   }

   public String getStShareLevel2() {
      return stShareLevel2;
   }

   public void setStShareLevel2(String stShareLevel2) {
      this.stShareLevel2 = stShareLevel2;
   }

   public String getStShareLevel() {
           if (stShareLevel2!=null)return FinCodec.CustomerShareLevel.NATIONAL;
      else if (stShareLevel1!=null)return FinCodec.CustomerShareLevel.BRANCH;
      else if (stShareLevel0!=null)return FinCodec.CustomerShareLevel.USER;
      return null;
   }

   public void setStShareLevel(String stShareLevel) {

      UserSessionView us = SessionManager.getInstance().getSession();

      stShareLevel0 = null;
      stShareLevel1 = null;
      stShareLevel2 = null;

      setStShareLevel0(us.getStUserID());
      if (FinCodec.CustomerShareLevel.USER.equalsIgnoreCase(stShareLevel)) return;
      setStShareLevel1(us.getStBranch());
      if (FinCodec.CustomerShareLevel.BRANCH.equalsIgnoreCase(stShareLevel)) return;
      setStShareLevel2("NAT");
      if (FinCodec.CustomerShareLevel.NATIONAL.equalsIgnoreCase(stShareLevel))  return;
   }

   public String getStRef1() {
      return stRef1;
   }

   public void setStRef1(String stRef1) {
      this.stRef1 = stRef1;
   }

   public String getStRef2() {
      return stRef2;
   }

   public void setStRef2(String stRef2) {
      this.stRef2 = stRef2;
   }

   public String getStRef3() {
      return stRef3;
   }

   public void setStRef3(String stRef3) {
      this.stRef3 = stRef3;
   }

   public String getStRef4() {
      return stRef4;
   }

   public void setStRef4(String stRef4) {
      this.stRef4 = stRef4;
   }

   public String getStRef5() {
      return stRef5;
   }

   public void setStRef5(String stRef5) {
      this.stRef5 = stRef5;
   }

   public String getStRef6() {
      return stRef6;
   }

   public void setStRef6(String stRef6) {
      this.stRef6 = stRef6;
   }

   public String getStRef7() {
      return stRef7;
   }

   public void setStRef7(String stRef7) {
      this.stRef7 = stRef7;
   }

   public String getStRefEntityID() {
      return stRefEntityID;
   }

   public void setStRefEntityID(String stRefEntityID) {
      this.stRefEntityID = stRefEntityID;
   }
   
   public String getStFunctionaryName() {
      return stFunctionaryName;
   }

   public void setStFunctionaryName(String stFunctionaryName) {
      this.stFunctionaryName = stFunctionaryName;
   }
   
   public String getStFunctionaryPosition() {
      return stFunctionaryPosition;
   }

   public void setStFunctionaryPosition(String stFunctionaryPosition) {
      this.stFunctionaryPosition = stFunctionaryPosition;
   }
   
   public String getStRcNo() {
      return stRcNo;
   }

   public void setStRcNo(String stRcNo) {
      this.stRcNo = stRcNo;
   }

    public String getStCustomerCategory()
    {
        return stCustomerCategory;
    }

    public void setStCustomerCategory(String stCustomerCategory)
    {
        this.stCustomerCategory = stCustomerCategory;
    }

    public String getStFinanceFlag()
    {
        return stFinanceFlag;
    }

    public void setStFinanceFlag(String stFinanceFlag)
    {
        this.stFinanceFlag = stFinanceFlag;
    }
    
    public String generateGLCode()throws Exception{
        final SQLUtil S = new SQLUtil();
        String tod2 = null;
        try {
            final PreparedStatement PS = S.setQuery("select gl_code"+
                    " from ent_master where ref1 = ? "+
                    " order by gl_code::bigint desc limit 1");
            
            PS.setString(1,getStRef1());
            
            final ResultSet RS = PS.executeQuery();
            
            if(RS.next()){
                RS.last();
                
                String glcode = RS.getString("gl_code");
                
                //01001
                
                glcode = glcode.substring(2);
                String tod = null;
                //glcode = glcode + 1;
                int glcode2 = Integer.parseInt(glcode);
                glcode2 = glcode2 + 1;
                String tes = String.valueOf(glcode2);
                if(tes.length()==1) tod = "00"+ tes;
                else if(tes.length()==2) tod = "0"+tes;
                else if(tes.length()==3) tod = tes;
                
                
                String cumi = getStRef1();
                int cumi2 = Integer.parseInt(cumi);
                String cumi3 = String.valueOf(cumi2);
                if(cumi3.length()==1) tod2 = "0"+cumi+tod;
                else if(cumi3.length()==2) tod2 = cumi+tod;
            }else{
                String cumi = getStRef1();
                int cumi2 = Integer.parseInt(cumi);
                String cumi3 = String.valueOf(cumi2);
                if(cumi3.length()==1) tod2 = "0"+cumi+"001";
                else if(cumi3.length()==2) tod2 = cumi+"001";
            }
            
            if("39".equalsIgnoreCase(getStRef1()))
                tod2 = "00000";
            
            return tod2;
            
        } finally {
            S.release();
        }
    }
    
    public void makeGLCOde() throws Exception{
        setStGLCode(generateGLCode());
    }

    public String getStTaxCode()
    {
        return stTaxCode;
    }

    public void setStTaxCode(String stTaxCode)
    {
        this.stTaxCode = stTaxCode;
    }
    
    public String getStReasEntityID() {
        return stReasEntityID;
    }

    public void setStReasEntityID(String stReasEntityID) {
        this.stReasEntityID = stReasEntityID;
    }

    public String getStRekening() {
        return stRekening;
    }

    public void setStRekening(String stRekening) {
        this.stRekening = stRekening;
    }

    /**
     * @return the stSaldo
     */
    public String getStSaldo() {
        return stSaldo;
    }

    /**
     * @param stSaldo the stSaldo to set
     */
    public void setStSaldo(String stSaldo) {
        this.stSaldo = stSaldo;
    }

    /**
     * @return the stCabang
     */
    public String getStCabang() {
        return stCabang;
    }

    /**
     * @param stCabang the stCabang to set
     */
    public void setStCabang(String stCabang) {
        this.stCabang = stCabang;
    }

    /**
     * @return the stRekeningNo
     */
    public String getStRekeningNo() {
        return stRekeningNo;
    }

    /**
     * @param stRekeningNo the stRekeningNo to set
     */
    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
    }

    public CompanyView getCompany() {

        if (company != null)
            if (!Tools.isEqual(company.getStVSCode(), stRef2)) company = null;

        if (company == null)
            company = (CompanyView) DTOPool.getInstance().getDTO(CompanyView.class, stRef2);

        return company;
    }

    /**
     * @return the stReferenceEntityID
     */
    public String getStReferenceEntityID() {
        return stReferenceEntityID;
    }

    /**
     * @param stReferenceEntityID the stReferenceEntityID to set
     */
    public void setStReferenceEntityID(String stReferenceEntityID) {
        this.stReferenceEntityID = stReferenceEntityID;
    }

    public boolean isBPD(){
        return stCategory1.equalsIgnoreCase("4")?true:false;
    }

    public boolean isBPR(){
        return stRef1.equalsIgnoreCase("93")?true:false;
    }

    public boolean isBankJatim(){
        return stRef1.equalsIgnoreCase("24")?true:false;
    }

    public boolean isBankUmum() throws Exception {
        final SQLUtil S = new SQLUtil();
        boolean bankUmum = false;
        try {
            S.setQuery(
                    "   select " +
                    "     ref2 " +
                    "   from " +
                    "         s_valueset " +
                    "   where" +
                    "      vs_group = 'COMP_TYPE' and vs_code = ?");

            S.setParam(1,stRef1);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                if(RS.getString(1).equalsIgnoreCase("1")) bankUmum = true;
            }

            if(stRef1.equalsIgnoreCase("93")) bankUmum = false;

            return bankUmum;
        }finally{
            S.release();
        }
    }

    /**
     * @return the stClaimTaxCode
     */
    public String getStClaimTaxCode() {
        return stClaimTaxCode;
    }

    /**
     * @param stClaimTaxCode the stClaimTaxCode to set
     */
    public void setStClaimTaxCode(String stClaimTaxCode) {
        this.stClaimTaxCode = stClaimTaxCode;
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public boolean isJunior() {
        return FinCodec.EntityLevel.JUNIOR.equalsIgnoreCase(stEntityLevel);
    }

    public boolean isSenior() {
        return FinCodec.EntityLevel.SENIOR.equalsIgnoreCase(stEntityLevel);
    }

    /**
     * @return the stEntLevel
     */
    public String getStEntityLevel() {
        return stEntityLevel;
    }

    /**
     * @param stEntLevel the stEntLevel to set
     */
    public void setStEntityLevel(String stEntityLevel) {
        this.stEntityLevel = stEntityLevel;
    }

    /**
     * @return the stIDCard
     */
    public String getStIDCard() {
        return stIDCard;
    }

    /**
     * @param stIDCard the stIDCard to set
     */
    public void setStIDCard(String stIDCard) {
        this.stIDCard = stIDCard;
    }

    /**
     * @return the stOJKCard
     */
    public String getStOJKCard() {
        return stOJKCard;
    }

    /**
     * @param stOJKCard the stOJKCard to set
     */
    public void setStOJKCard(String stOJKCard) {
        this.stOJKCard = stOJKCard;
    }

    /**
     * @return the stRegionID
     */
    public String getStRegionID() {
        return stRegionID;
    }

    /**
     * @param stRegionID the stRegionID to set
     */
    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the stAgentID
     */
    public String getStAgentID() {
        return stAgentID;
    }

    /**
     * @param stAgentID the stAgentID to set
     */
    public void setStAgentID(String stAgentID) {
        this.stAgentID = stAgentID;
    }

    /**
     * @return the stSupervisiCode
     */
    public String getStSupervisiCode() {
        return stSupervisiCode;
    }

    /**
     * @param stSupervisiCode the stSupervisiCode to set
     */
    public void setStSupervisiCode(String stSupervisiCode) {
        this.stSupervisiCode = stSupervisiCode;
    }

    /**
     * @return the stPKSCard
     */
    public String getStPKSCard() {
        return stPKSCard;
    }

    /**
     * @param stPKSCard the stPKSCard to set
     */
    public void setStPKSCard(String stPKSCard) {
        this.stPKSCard = stPKSCard;
    }

    /**
     * @return the stReferenceGatewayCode
     */
    public String getStReferenceGatewayCode() {
        return stReferenceGatewayCode;
    }

    /**
     * @param stReferenceGatewayCode the stReferenceGatewayCode to set
     */
    public void setStReferenceGatewayCode(String stReferenceGatewayCode) {
        this.stReferenceGatewayCode = stReferenceGatewayCode;
    }

    public DTOList getEntityDocuments() {
        if (entityDocuments == null && stEntityID != null){
            entityDocuments = loadEntityDocuments(stEntityID, "ENTITY");
        }

        return entityDocuments;
    }

    private static DTOList loadEntityDocuments(String stEntityID, String documentClass) {
        try {

            final DTOList l = ListUtil.getDTOListFromQuery(
                   "   select "+
                   "  b.*,c.description,a.ins_document_type_id  "+
                   " from  "+
                   "   ins_documents a "+
                   "   inner join ins_document_type c on c.ins_document_type_id = a.ins_document_type_id "+
                   "   left join ent_documents b on  b.document_class=a.document_class and b.ent_id= ? and b.ins_document_type_id=a.ins_document_type_id "+
                   " where  "+
                   "   a.pol_type_id= '999'  "+
                   "    and a.document_class= ? ",
                    new Object[]{stEntityID, documentClass},
                    EntityDocumentView.class
                    );

            for (int i = 0; i < l.size(); i++) {
                EntityDocumentView docs = (EntityDocumentView) l.get(i);

                if (docs.getStEntityDocumentID() != null) docs.setStSelectedFlag("Y");

                docs.setStEntityID(stEntityID);
                docs.setStDocumentClass(documentClass);
                
            }

            return l;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setEntityDocuments(DTOList entityDocuments) {
      this.entityDocuments = entityDocuments;
   }

    public String generateGLCodeNew()throws Exception{
        final SQLUtil S = new SQLUtil();
        String gl_Code = null;
        try {
            final PreparedStatement PS = S.setQuery("SELECT s.i AS gl_code "+
                                            " FROM generate_series((?||'001')::bigint,(?||'999')::bigint) s(i) "+
                                            " WHERE NOT EXISTS (SELECT gl_code::bigint FROM ent_master WHERE gl_code::bigint = s.i and ref1 = ? ) "+
                                            " and length(s.i::character varying) = 5 and substr(s.i::character varying,0,3) = ? "+
                                            " limit 1");

            PS.setString(1,getStRef1());
            PS.setString(2,getStRef1());
            PS.setString(3,getStRef1());
            PS.setString(4,getStRef1());

            final ResultSet RS = PS.executeQuery();

            if(RS.next()){
                RS.last();

                gl_Code = RS.getString("gl_code");
            }

            if("39".equalsIgnoreCase(getStRef1()))
                gl_Code = "00000";

            return gl_Code;

        } finally {
            S.release();
        }
    }

    public void makeGLCOdeNew() throws Exception{
        setStGLCode(generateGLCodeNew());
    }

    public boolean isAskrida(){
        return stEntityID.equalsIgnoreCase("1")?true:false;
    }

    public boolean isBankMandiri(){
        return stRef2.equalsIgnoreCase("62")?true:false;
    }

    public boolean isBankBNI(){
        return stRef2.equalsIgnoreCase("1096") || stRef2.equalsIgnoreCase("1293")?true:false;
    }

}
