/***********************************************************************
 * Module:  com.webfin.gl.model.RKAPGroupView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 6:29:56 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.BDUtil;
import com.crux.util.ObjectCloner;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.crux.util.LogManager;
import com.webfin.insurance.model.InsurancePolicyView;

import java.math.BigDecimal;
import java.util.Date;

public class RKAPGroupView extends DTO implements RecordAudit {
   /*
   CREATE TABLE gl_rkap_group
(
  gl_rkap_grp_id bigint NOT NULL,
  rkap_group_id character varying(32),
  description text,
  years character varying(4),
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  konvensional numeric,
  syariah numeric,
  trx_hdr_id character varying(10),
  trx_no character varying(32),
  CONSTRAINT gl_rkap_group_pk PRIMARY KEY (gl_rkap_grp_id)
)
   */
   private final static transient LogManager logger = LogManager.getInstance(RKAPGroupView.class);

   public static String tableName = "gl_rkap_group";
   
   public static String fieldMap[][] = {
      {"stRKAPTransactionGroupID","gl_rkap_grp_id*pk"},
      {"stRKAPTransactionHeaderID","trx_hdr_id"},
      {"stRKAPTransactionNo","trx_no"},
      {"stRKAPGroupID","rkap_group_id"},
      {"stRKAPDescription","description"},
      {"stYears","years"},
      {"dbKonvensional","konvensional"},
      {"dbSyariah","syariah"},      
   };

   /*
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_type varchar(8);
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_no varchar(32);

   */

   private BigDecimal dbKonvensional;
   private BigDecimal dbSyariah;
   private String stRKAPTransactionGroupID;
   private String stRKAPTransactionHeaderID;
   private String stRKAPTransactionNo;
   private String stRKAPGroupID;
   private String stRKAPDescription;
   private String stYears;
   
    /**
     * @return the dbKonvensional
     */
    public BigDecimal getDbKonvensional() {
        return dbKonvensional;
    }

    /**
     * @param dbKonvensional the dbKonvensional to set
     */
    public void setDbKonvensional(BigDecimal dbKonvensional) {
        this.dbKonvensional = dbKonvensional;
    }

    /**
     * @return the dbSyariah
     */
    public BigDecimal getDbSyariah() {
        return dbSyariah;
    }

    /**
     * @param dbSyariah the dbSyariah to set
     */
    public void setDbSyariah(BigDecimal dbSyariah) {
        this.dbSyariah = dbSyariah;
    }

    /**
     * @return the stRKAPTransactionGroupID
     */
    public String getStRKAPTransactionGroupID() {
        return stRKAPTransactionGroupID;
    }

    /**
     * @param stRKAPTransactionGroupID the stRKAPTransactionGroupID to set
     */
    public void setStRKAPTransactionGroupID(String stRKAPTransactionGroupID) {
        this.stRKAPTransactionGroupID = stRKAPTransactionGroupID;
    }

    /**
     * @return the stRKAPTransactionHeaderID
     */
    public String getStRKAPTransactionHeaderID() {
        return stRKAPTransactionHeaderID;
    }

    /**
     * @param stRKAPTransactionHeaderID the stRKAPTransactionHeaderID to set
     */
    public void setStRKAPTransactionHeaderID(String stRKAPTransactionHeaderID) {
        this.stRKAPTransactionHeaderID = stRKAPTransactionHeaderID;
    }

    /**
     * @return the stRKAPTransactionNo
     */
    public String getStRKAPTransactionNo() {
        return stRKAPTransactionNo;
    }

    /**
     * @param stRKAPTransactionNo the stRKAPTransactionNo to set
     */
    public void setStRKAPTransactionNo(String stRKAPTransactionNo) {
        this.stRKAPTransactionNo = stRKAPTransactionNo;
    }

    /**
     * @return the stRKAPGroupID
     */
    public String getStRKAPGroupID() {
        return stRKAPGroupID;
    }

    /**
     * @param stRKAPGroupID the stRKAPGroupID to set
     */
    public void setStRKAPGroupID(String stRKAPGroupID) {
        this.stRKAPGroupID = stRKAPGroupID;
    }

    /**
     * @return the stRKAPDescription
     */
    public String getStRKAPDescription() {
        return stRKAPDescription;
    }

    /**
     * @param stRKAPDescription the stRKAPDescription to set
     */
    public void setStRKAPDescription(String stRKAPDescription) {
        this.stRKAPDescription = stRKAPDescription;
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
    
}


/*
 <tr class=row0>
    <td><input type=hidden name=acid0 value=<%=jspUtil.print(jv1.getLgAccountID())%>><%=jspUtil.getInputText("acno0",new FieldValidator("accountno","Account Number","string",32),jv1.getStAccountNo(), 150, JSPUtil.MANDATORY|JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td>
    <td width=25><%=ro?"":jspUtil.getButtonNormal("bx","...","rnc="+0+";openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectAccount);")%></td>
    <td><%=jspUtil.getInputText("desc0",new FieldValidator("accountno","Description","string",128),jv1.getStDescription(), 150, cf)%></td>
    <td>
       <%=jspUtil.getInputText("debit0",new FieldValidator("debit","Debit Value","money16.2",-1),jv1.getDbEnteredDebit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
       <%=!isForex?"":jspUtil.getInputText("rdebit0",new FieldValidator("debit","Debit Value","money16.2",-1),jv1.getDbDebit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
    </td>
    <td>
       <%=jspUtil.getInputText("credit0",new FieldValidator("credit","Credit Value","money16.2",-1),jv1.getDbEnteredCredit(), 110, JSPUtil.MANDATORY|cf|JSPUtil.NOTEXTMODE)%>
       <%=!isForex?"":jspUtil.getInputText("rcredit0",new FieldValidator("credit","Credit Value","money16.2",-1),jv1.getDbCredit(), 110, JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE)%>
    </td>
 */