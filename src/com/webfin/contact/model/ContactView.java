/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.contact.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.insurance.model.InsuranceEntityView;

import java.util.Date;

public class ContactView extends DTO implements RecordAudit {
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

   public static String tableName = "customer_master";

   public static String comboFields[] = {"ent_id","ent_name","address"};

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
      {"dtIncorporateDate","incorporate_date"},
      {"stInsInwardFlag","ins_inward_flag"},
      {"stInsOutwardFlag","ins_outward_flag"},
      {"stInsCompanyFlag","ins_company_flag"},
      {"stCaptiveFlag","captive_flag"},
      {"stCategory1","category1"},
      

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
      {"stComment","comment"},
      {"stContactPerson","contact_person"},
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
   private Date dtIncorporateDate;
   private String stComment;
   private String stContactPerson;

   private DTOList addresses;
   private ContactAddressView primaryAddress;

   public String getStShortName() {
      return stShortName;
   }

   public void setStShortName(String stShortName) {
      this.stShortName = stShortName;
   }
   
   public String getStContactPerson() {
	      return stContactPerson;
	   }

   public void setStContactPerson(String stContactPerson) {
	      this.stContactPerson = stContactPerson;
	   }
   
   public String getStComment() {
	      return stComment;
	   }

   public void setStComment(String stComment) {
	      this.stComment = stComment;
	   }

   public DTOList getAddresses() {
      loadAddresses();
      return addresses;
   }

   private void loadAddresses() {
      try {
         if (addresses == null)
            addresses = ListUtil.getDTOListFromQuery(
                    "select * from customer_address where ent_id = ?",
                    new Object [] {stEntityID},
                    ContactAddressView.class
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

   public ContactAddressView getPrimaryAddress() {
      return primaryAddress;
   }

   public void setPrimaryAddress(ContactAddressView primaryAddress) {
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

      final ContactAddressView eaddr = new ContactAddressView();

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

   public Date getDtIncorporateDate() {
      return dtIncorporateDate;
   }

   public void setDtIncorporateDate(Date dtIncorporateDate) {
      this.dtIncorporateDate = dtIncorporateDate;
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
}
