/***********************************************************************
 * Module:  com.webfin.master.capacity.CapacityMasterForm
 * Author:  Denny Mahendra
 * Created: Nov 15, 2007 1:12:05 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.master.capacity;

import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.util.ObjectCloner;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.util.IDFactory;
import com.crux.ff.model.FlexTableView;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.lang.LanguageManager;
import com.crux.util.LogManager;
import com.crux.util.MailUtil2;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;

import com.crux.web.controller.SessionManager;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsuranceRatesBigView;
import com.webfin.insurance.model.uploadEndorseDetailView;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CapacityMasterForm extends Form {
    
    private final static transient LogManager logger = LogManager.getInstance(CapacityMasterForm.class);
    
   private String ref1;
   String ref2;
   String ref4;
   DTOList list;
   public String mode;
   
   private boolean enableUW = SessionManager.getInstance().getSession().hasResource("POL_UWRIT_APR_UW");
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
   
   public boolean enableEditApprovalTSILimit = SessionManager.getInstance().getSession().hasResource("CAPA_APPROVAL_TSI_EDIT");
   public boolean enableEditApprovalComissionLimit = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_EDIT");
   public boolean enableEditApprovalComissionGroupLimit = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_GROUP_EDIT");
   private boolean enableEditApprovalClaimLimit = SessionManager.getInstance().getSession().hasResource("CAPA_APPROVAL_CLAIM_EDIT");

   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private boolean isApprovalFlag1Mode;
   private boolean isApprovalFlag2Mode;
   private boolean isApprovalFlag3Mode;
   
   private boolean enableApprovalKomisi1 = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_APPROVE1");
   private boolean enableApprovalKomisi2 = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_APPROVE2");
   private boolean enableApprovalKomisi3 = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_APPROVE3");
   private boolean enableApprovalKomisi4 = SessionManager.getInstance().getSession().hasResource("CAPA_COMM_ACTIVATE");
   
   private boolean createMode = false;
   private boolean approvalMode = false;
   private boolean editMode = false;
   private boolean showButton = true;
   
   private boolean enableApprovalTSI1 = SessionManager.getInstance().getSession().hasResource("CAPA_TSI_APPROVE1");
   private boolean enableApprovalTSI2 = SessionManager.getInstance().getSession().hasResource("CAPA_TSI_APPROVE2");
   private boolean enableApprovalTSI3 = SessionManager.getInstance().getSession().hasResource("CAPA_TSI_APPROVE3");
   private boolean enableApprovalTSI4 = SessionManager.getInstance().getSession().hasResource("CAPA_TSI_ACTIVATE");
   
   private String detailIndex;
   private String ref6;
   private String stPolicyTypeGroupID;
   private String stPolicyTypeID;

    private boolean enableEditAuthorityLimit = SessionManager.getInstance().getSession().hasResource("CAPA_AUTHO_EDIT");
    private boolean enableEditRKAPLimit = SessionManager.getInstance().getSession().hasResource("CAPA_RKAP_EDIT");
    private String stRKAPGroupID;
    private String stBiaopGroupID;
    private String stBiaopTypeID;


    private boolean enableEditKasLimit = SessionManager.getInstance().getSession().hasResource("CAPA_KAS_EDIT");

    String ref3;
    private String stFileID;

    private boolean sendEmail = false;
    private String stEmailReceiver;
    private String stEmailKabagTI = Parameter.readString("CAPA_ACCEPT_APPROVAL1");
    private String stEmailKadivTI = Parameter.readString("CAPA_ACCEPT_APPROVAL2");
    private String stEmailKabagUW = Parameter.readString("CAPA_ACCEPT_APPROVAL3");
    private String stEmailKadivUW = Parameter.readString("CAPA_ACCEPT_APPROVAL4");
    private String stEmailText;

    private String stUsia;
    private String stGrupSumbis;

    private String stShowNeedApproval;
    private String stLevelApproval;

    private boolean enableApprovalClaim1 = SessionManager.getInstance().getSession().hasResource("CAPA_CLAIM_APPROVE1");
    private boolean enableApprovalClaim2 = SessionManager.getInstance().getSession().hasResource("CAPA_CLAIM_APPROVE2");
    private boolean enableApprovalClaim3 = SessionManager.getInstance().getSession().hasResource("CAPA_CLAIM_APPROVE3");
    private boolean enableApprovalClaim4 = SessionManager.getInstance().getSession().hasResource("CAPA_CLAIM_ACTIVATE");

    private String stIdentifikasi;
    private String stProgramKerja;
    private DTOList listRKAP;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private String stRegion = SessionManager.getInstance().getSession().getStDivisionID();

    public boolean isEnableApprovalClaim1() {
        return enableApprovalClaim1;
    }

    public void setEnableApprovalClaim1(boolean enableApprovalClaim1) {
        this.enableApprovalClaim1 = enableApprovalClaim1;
    }

    public boolean isEnableApprovalClaim2() {
        return enableApprovalClaim2;
    }

    public void setEnableApprovalClaim2(boolean enableApprovalClaim2) {
        this.enableApprovalClaim2 = enableApprovalClaim2;
    }

    public boolean isEnableApprovalClaim3() {
        return enableApprovalClaim3;
    }

    public void setEnableApprovalClaim3(boolean enableApprovalClaim3) {
        this.enableApprovalClaim3 = enableApprovalClaim3;
    }

    public boolean isEnableApprovalClaim4() {
        return enableApprovalClaim4;
    }

    public void setEnableApprovalClaim4(boolean enableApprovalClaim4) {
        this.enableApprovalClaim4 = enableApprovalClaim4;
    }

    public String getStLevelApproval() {
        return stLevelApproval;
    }

    public void setStLevelApproval(String stLevelApproval) {
        this.stLevelApproval = stLevelApproval;
    }

    public String getStShowNeedApproval() {
        return stShowNeedApproval;
    }

    public void setStShowNeedApproval(String stShowNeedApproval) {
        this.stShowNeedApproval = stShowNeedApproval;
    }

    public boolean isShowNeedApproval(){
        return Tools.isYes(stShowNeedApproval);
    }

   
   public void commission() {
      mode = "commission";
      setTitle("Comission Limits");
   }

   public void approval() {
      mode = "approval";
      setTitle("Approval Limits");
   }
   
   public void commissionsetting() {
      mode = "commissionsetting";
      setTitle("Comission Limit Per Item");
   }
   
   public void commissiongroup() {
      mode = "commissiongroup";
      setTitle("Comission Limits Group Company");
   }

   public DTOList getList() {
      return list;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public String getRef1() {
      return ref1;
   }

   public void setRef1(String ref1) {
      this.ref1 = ref1;
   }
   
   public String getRef4() {
      return ref4;
   }

   public void setRef4(String ref4) {
      this.ref4 = ref4;
   }

   public String getRef2() {
      return ref2;
   }

   public void setRef2(String ref2) {
      this.ref2 = ref2;
   }

   public String getMode() {
      return mode;
   }

   public void setMode(String mode) {
      this.mode = mode;
   }

   public void select_approval() throws Exception {
       list = null;
       
      if ((getRef1()!=null) && (ref2!=null) && (list==null) && !ref2.equalsIgnoreCase("FINANCE")) {

          if(getStPolicyTypeID()!=null){
              list=ListUtil.getDTOListFromQuery(
                     "   select " +
                     "      b.*, a.pol_type_id, a.description " +
                     "   from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' and b.ref3 = ? order by a.pol_type_id,b.period_start,ref4 ",
                     new Object [] {getRef1(), ref2, getStPolicyTypeID()},
                     FlexTableView.class
             );
          }else{
              list=ListUtil.getDTOListFromQuery(
                     "   select " +
                     "      b.*, a.pol_type_id, a.description " +
                     "   from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' order by a.pol_type_id,b.period_start,ref4 ",
                     new Object [] {getRef1(), ref2},
                     FlexTableView.class
             );
          }

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate();// else ft.markNew();
         }
      }else if ((getRef1()!=null) && (ref2!=null) && (list==null) && ref2.equalsIgnoreCase("FINANCE")) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "     b.*, a.description,ar_settlement_id " +
                 "  from  " +
                 "    ar_settlement a " +
                 "    inner join ff_table b on b.ref3=cast(a.ar_settlement_id as varchar)  " +
                 "    and b.ref1 = ? and b.ref2 = ? " +
                 "     and b.fft_group_id='CAPA'  " +
                 "     order by b.period_start,ar_settlement_id",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("ar_settlement_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
         }
      }
      
      setEditMode(true);
      setShowButton(false);
   }

   public void select_comission() throws Exception {
       if(getDtPeriodStart()==null||getDtPeriodEnd()==null)
           throw new RuntimeException("Periode Awal dan Akhir harus diisi dulu");
       
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "      and date_trunc('day',period_start) >= ? "+
                 "      and date_trunc('day',period_end) <= ? order by a.pol_type_id" ,
                 new Object [] {ref2,dtPeriodStart,dtPeriodEnd},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
      
      if ((getRef1()!=null) && (ref4!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref4=? and b.fft_group_id='COMM'",
                 new Object [] {getRef1(), ref4},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference1(getRef1());
            ft.setStReference4(ref4);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
        
        setEditMode(true);
        setShowButton(false);
   }
   
   public void select_comission_group() throws Exception {
             
      if ((getRef1()!=null) && (ref4!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref4=? and b.fft_group_id='COMM'",
                 new Object [] {getRef1(), ref4},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference1(getRef1());
            ft.setStReference4(ref4);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
        
        setEditMode(true);
        setShowButton(false);
   }
   
   public void select_comission_backup() throws Exception {
       if ((getRef1()!=null) && (ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='COMM'",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
      
      if ((getRef1()!=null) && (ref4!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref4=? and b.fft_group_id='COMM'",
                 new Object [] {getRef1(), ref4},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference1(getRef1());
            ft.setStReference4(ref4);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
   }

   public void save() throws Exception {

      validate();

      for (int i = 0; i < list.size(); i++) {
         FlexTableView flexTableView = (FlexTableView) list.get(i);

         if (flexTableView.isNew()) {         
            if (flexTableView.getDbReference1()!=null)
               flexTableView.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
            else
               flexTableView.markUnmodified();
         }
      }

      SQLUtil.qstore(list);

      if(isSendEmail()){
          MailUtil2 mail = new MailUtil2();

          //mail.sendEmail3(getStEmailReceiver(), "Notifikasi Approval Limit Kewenangan", getStEmailText());
      }

      close();
   }

   private void validate() throws Exception{
       for (int i = 0; i < list.size(); i++) {
            FlexTableView flexTableView = (FlexTableView) list.get(i);

            if (flexTableView.isNew()) {
                if(flexTableView.getStFileID()==null)
                    throw new RuntimeException("Pembuatan limit harus disertai dengan upload dokumen");
            }
       }
   }
   
   public void save2() throws Exception {

      //save append
      DTOList append = new DTOList();
      
      for (int i = 0; i < list.size(); i++) {
         FlexTableView flexTableView = (FlexTableView) list.get(i);

         if(!flexTableView.isAddFlag()) continue;
         
         if(flexTableView.isAddFlag()){
             //logger.logDebug("+++++++++++++++++++ APPEND DATA ++++++++++++++++");
             
            FlexTableView ftClone = new FlexTableView();
            
            ftClone = (FlexTableView) ObjectCloner.deepCopy(flexTableView);
            
            ftClone.setStAddFlag(null);
            ftClone.setDbReference1(flexTableView.getDbReference2());
            ftClone.setDtPeriodStart(flexTableView.getDtPeriodEnd());
            ftClone.setDbReference2(null);
            ftClone.setDtPeriodEnd(null);
            ftClone.setStActiveFlag(null);
           
            ftClone.markNew();
            
            append.add(ftClone);
            
            if (ftClone.isNew()) {
            if (ftClone.getDbReference1()!=null)
               ftClone.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
            else
               ftClone.markUnmodified();
            }
         } 
         
      }
      
      for (int i = 0; i < list.size(); i++) {
         FlexTableView flexTableView = (FlexTableView) list.get(i);

         if (flexTableView.isNew()) {
            if (flexTableView.getDbReference1()!=null||flexTableView.getDbReference2()!=null)
               if(flexTableView.isAddFlag())
                   flexTableView.markUnmodified();
               else
                   flexTableView.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
            else
               flexTableView.markUnmodified();
         }
         
         flexTableView.setDbReference2(null);
         flexTableView.setDtPeriodEnd(null);
         flexTableView.setStAddFlag(null);
      }

      SQLUtil.qstore(list);
      SQLUtil.qstore(append);

      close();
   }

   public void cancel() {
      close();
   }
   
   public boolean isEnableUW() {
      return enableUW;
   }
   
   public boolean isEnableSuperEdit() {
      return enableSuperEdit;
   } 
   
    public void select_comission_setting() throws Exception {
      if ((ref1!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2 = ? and b.fft_group_id='COMM_ITEM'",
                 new Object [] {ref1, ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM_ITEM");

            ft.setStReference1(ref1);
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
         }
      }
      
   }

    public boolean isEnableEditApprovalTSILimit() {
        return enableEditApprovalTSILimit;
    }

    public void setEnableEditApprovalTSILimit(boolean enableEditApprovalTSILimit) {
        this.enableEditApprovalTSILimit = enableEditApprovalTSILimit;
    }

    public boolean isEnableEditApprovalComissionLimit() {
        return enableEditApprovalComissionLimit;
    }

    public void setEnableEditApprovalComissionLimit(boolean enableEditApprovalComissionLimit) {
        this.enableEditApprovalComissionLimit = enableEditApprovalComissionLimit;
    }

    public boolean isEnableEditApprovalComissionGroupLimit() {
        return enableEditApprovalComissionGroupLimit;
    }

    public void setEnableEditApprovalComissionGroupLimit(boolean enableEditApprovalComissionGroupLimit) {
        this.enableEditApprovalComissionGroupLimit = enableEditApprovalComissionGroupLimit;
    }

    public boolean isEnableEditApprovalClaimLimit() {
        return enableEditApprovalClaimLimit;
    }

    public void setEnableEditApprovalClaimLimit(boolean enableEditApprovalClaimLimit) {
        this.enableEditApprovalClaimLimit = enableEditApprovalClaimLimit;
    }
    
    public void create() throws Exception{
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM' order by a.pol_type_id",
                 new Object [] {"XXXXX"},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTID(null);
            ft.setDtPeriodStart(null);
            ft.setDtPeriodEnd(null);
            ft.setStApprovedFlag1(null);
            ft.setStApprovedFlag2(null);
            ft.setStApprovedFlag3(null);
            ft.setStApprovedFlag4(null);
            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            if(getDtPeriodStart()!=null) ft.setDtPeriodStart(getDtPeriodStart());
            if(getDtPeriodEnd()!=null) ft.setDtPeriodEnd(getDtPeriodEnd());

            ft.markNew();
         }
         
         createMode = true;
         setShowButton(false);
      }
    }
    
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }
    
    public boolean isIsApprovalFlag1Mode() {
        return isApprovalFlag1Mode;
    }

    public void setIsApprovalFlag1Mode(boolean isApprovalFlag1Mode) {
        this.isApprovalFlag1Mode = isApprovalFlag1Mode;
    }

    public boolean isIsApprovalFlag2Mode() {
        return isApprovalFlag2Mode;
    }

    public void setIsApprovalFlag2Mode(boolean isApprovalFlag2Mode) {
        this.isApprovalFlag2Mode = isApprovalFlag2Mode;
    }

    public boolean isIsApprovalFlag3Mode() {
        return isApprovalFlag3Mode;
    }

    public void setIsApprovalFlag3Mode(boolean isApprovalFlag3Mode) {
        this.isApprovalFlag3Mode = isApprovalFlag3Mode;
    }
    
    public void clickApprovalFlag1() throws Exception { 
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "       order by a.pol_type_id,b.period_start " ,
                 new Object [] {ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());
            ft.setStAddFlag(null);
            ft.setDbReference2(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
            
         }
      }
        
        setIsApprovalFlag1Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }
    
    public void clickApprovalFlag2() throws Exception { 
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "       order by a.pol_type_id,b.period_start " ,
                 new Object [] {ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());
            ft.setStAddFlag(null);
            ft.setDbReference2(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
            
         }
      }
        
        setIsApprovalFlag2Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }
    
    public void clickApprovalFlag3() throws Exception { 
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "       order by a.pol_type_id,b.period_start " ,
                 new Object [] {ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());
            ft.setStAddFlag(null);
            ft.setDbReference2(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
            
         }
      }
        
        setIsApprovalFlag3Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }

    

    public boolean isCreateMode() {
        return createMode;
    }

    public void setCreateMode(boolean createMode) {
        this.createMode = createMode;
    }

    public boolean isApprovalMode() {
        return approvalMode;
    }

    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }
    
    public void select_comission2() throws Exception {
       list = null;

        if ((ref2!=null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "       order by a.pol_type_id,b.period_start " ,
                 new Object [] {ref2},
                 FlexTableView.class
         );

         if ((ref2!=null) && getStPolicyTypeID()!=null) {
                 list=ListUtil.getDTOListFromQuery(
                         "   select " +
                         "      b.*, a.pol_type_id, a.description " +
                         "   from " +
                         "      ins_policy_types a " +
                         "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                         "      and b.ref3 = ? order by a.pol_type_id,b.period_start " ,
                         new Object [] {ref2, getStPolicyTypeID()},
                         FlexTableView.class
                 );
            }

         if ((ref2!=null) && getStPolicyTypeID()!=null && getStGrupSumbis()!=null) {
                 list=ListUtil.getDTOListFromQuery(
                         "   select " +
                         "      b.*, a.pol_type_id, a.description " +
                         "   from " +
                         "      ins_policy_types a " +
                         "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                         "      and b.ref3 = ? and b.ref4 = ? order by a.pol_type_id,b.period_start " ,
                         new Object [] {ref2, getStPolicyTypeID(), getStGrupSumbis()},
                         FlexTableView.class
                 );
            }

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
            
         }
      }
        
        setEditMode(true);
        setShowButton(false);
   }
    
    public void save_tsi() throws Exception {

      //save append
      DTOList append = new DTOList();
      
      for (int i = 0; i < list.size(); i++) {
         FlexTableView flexTableView = (FlexTableView) list.get(i);

         if(!flexTableView.isAddFlag()) continue;
         
         if(flexTableView.isAddFlag()){
             //logger.logDebug("+++++++++++++++++++ APPEND DATA ++++++++++++++++");
             
            FlexTableView ftClone = new FlexTableView();
            
            ftClone = (FlexTableView) ObjectCloner.deepCopy(flexTableView);
            
            ftClone.setStAddFlag(null);
            ftClone.setDtPeriodEnd(null);
            ftClone.setStActiveFlag(null);
            ftClone.setStApprovedFlag1(null);
            ftClone.setStApprovedFlag2(null);
            ftClone.setStApprovedFlag3(null);
           
            ftClone.markNew();
            
            append.add(ftClone);
            
            if (ftClone.isNew()) {
            if (ftClone.getDbReference1()!=null)
               ftClone.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
            else
               ftClone.markUnmodified();
            }
         } 
         
      }
      
      for (int i = 0; i < list.size(); i++) {
         FlexTableView flexTableView = (FlexTableView) list.get(i);

         if (flexTableView.isNew()) {
            if (flexTableView.getDbReference1()!=null||flexTableView.getDbReference2()!=null)
               if(flexTableView.isAddFlag())
                   flexTableView.markUnmodified();
               else
                   flexTableView.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
            else
               flexTableView.markUnmodified();
         }else{
             if(flexTableView.isAddFlag())
                flexTableView.markUnmodified();
         }
         
         flexTableView.setDtPeriodEnd(null);
         flexTableView.setStAddFlag(null);
      }

      SQLUtil.qstore(list);
      SQLUtil.qstore(append);

      close();
   }
    
    public void clickApprovalFlag1TSI() throws Exception { 

        if ((getRef1()!=null) && (ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' order by a.pol_type_id,b.period_start ",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setStAddFlag(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
        
        setIsApprovalFlag1Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }
    
    public void clickApprovalFlag2TSI() throws Exception { 

        if ((getRef1()!=null) && (ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' order by a.pol_type_id,b.period_start ",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setStAddFlag(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
        
        setIsApprovalFlag2Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }
    
    public void clickApprovalFlag3TSI() throws Exception { 

        if ((getRef1()!=null) && (ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' order by a.pol_type_id,b.period_start ",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setStAddFlag(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
         }
      }
        
        setIsApprovalFlag3Mode(true);
        setApprovalMode(true);
        setShowButton(false);
        setEditMode(true);
   }
    
    public void view_approval() throws Exception {
      list = null;

      if ((getRef1()!=null) && (ref2!=null) && (list==null) && !ref2.equalsIgnoreCase("FINANCE")) {

          if(getStPolicyTypeID()!=null){
              list=ListUtil.getDTOListFromQuery(
                     "   select " +
                     "      b.*, a.pol_type_id, a.description " +
                     "   from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' and b.ref3 = ? order by a.pol_type_id,b.period_start,ref4 ",
                     new Object [] {getRef1(), ref2, getStPolicyTypeID()},
                     FlexTableView.class
             );
          }else{
              list=ListUtil.getDTOListFromQuery(
                     "   select " +
                     "      b.*, a.pol_type_id, a.description " +
                     "   from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='CAPA' order by a.pol_type_id,b.period_start,ref4 ",
                     new Object [] {getRef1(), ref2},
                     FlexTableView.class
             );
          }

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate();// else ft.markNew();
         }
      }else if ((getRef1()!=null) && (ref2!=null) && (list==null) && ref2.equalsIgnoreCase("FINANCE")) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "     b.*, a.description,ar_settlement_id " +
                 "  from  " +
                 "    ar_settlement a " +
                 "    inner join ff_table b on b.ref3=cast(a.ar_settlement_id as varchar)  " +
                 "    and b.ref1 = ? and b.ref2 = ? " +
                 "     and b.fft_group_id='CAPA'  " +
                 "     order by b.period_start,ar_settlement_id",
                 new Object [] {getRef1(), ref2},
                 FlexTableView.class
         );

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("ar_settlement_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
         }
      }
      
      setEditMode(false);
      setShowButton(false);
      setReadOnly(true);
   }
    
    public void view_comission2() throws Exception {
       
        if ((ref2!=null) && (list==null)) {
         list=ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      b.*, a.pol_type_id, a.description " +
                 "   from " +
                 "      ins_policy_types a " +
                 "      left join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                 "       order by a.pol_type_id,b.period_start " ,
                 new Object [] {ref2},
                 FlexTableView.class
         );

         if ((ref2!=null) && getStPolicyTypeID()!=null && getStGrupSumbis()!=null) {
                 list=ListUtil.getDTOListFromQuery(
                         "   select " +
                         "      b.*, a.pol_type_id, a.description " +
                         "   from " +
                         "      ins_policy_types a " +
                         "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref2=? and b.fft_group_id='COMM'"+
                         "      and b.ref3 = ? and b.ref4 = ? order by a.pol_type_id,b.period_start " ,
                         new Object [] {ref2, getStPolicyTypeID(), getStGrupSumbis()},
                         FlexTableView.class
                 );
            }

         for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());
            ft.setStAddFlag(null);
            ft.setDbReference2(null);
            ft.setDtPeriodEnd(null);

            if (ft.getStFFTID()!=null) ft.markUpdate(); else ft.markNew();
            
         }
      }
        
        setReadOnly(true);
        setEditMode(false);
        setShowButton(false);
   }

    public boolean isEnableApprovalTSI1() {
        return enableApprovalTSI1;
    }

    public void setEnableApprovalTSI1(boolean enableApprovalTSI1) {
        this.enableApprovalTSI1 = enableApprovalTSI1;
    }

    public boolean isEnableApprovalTSI2() {
        return enableApprovalTSI2;
    }

    public void setEnableApprovalTSI2(boolean enableApprovalTSI2) {
        this.enableApprovalTSI2 = enableApprovalTSI2;
    }

    public boolean isEnableApprovalTSI3() {
        return enableApprovalTSI3;
    }

    public void setEnableApprovalTSI3(boolean enableApprovalTSI3) {
        this.enableApprovalTSI3 = enableApprovalTSI3;
    }

    public boolean isEnableApprovalTSI4() {
        return enableApprovalTSI4;
    }

    public void setEnableApprovalTSI4(boolean enableApprovalTSI4) {
        this.enableApprovalTSI4 = enableApprovalTSI4;
    }
    
    public void print()throws Exception{
        super.redirect("/pages/capacity/report.fop");
    }
    
    public SQLAssembler getSQA() {
        String mode = getMode();
        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" b.fft_id,b.ref1,b.ref2,b.ref3,c.role_name as ref4,b.fft_group_id,b.ref7,b.refn1,b.refn2,b.refn3,b.period_start,b.period_end,b.change_date,"
//                + " b.approved_flag1,b.approved_flag2,b.approved_flag3,b.approved_flag4,b.approved_flag1,b.active_flag,a.description as ref5 ");

        sqa.addSelect(" b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,"
                + "b.refn1,b.refn2,b.refn3,b.refn4,b.refn5,"
                + "b.period_start,b.period_end,b.change_date,d.ent_name as ref8,e.ent_name as ref9 ");

        sqa.addQuery("   from ins_policy_types a "
                + " left join ff_table b on b.ref3 = cast(a.pol_type_id as varchar) and b.active_flag = 'Y' "
                + " left join s_roles c on c.role_id = b.ref1 "
                + " left join ent_master d on d.ent_id::text = b.ref4 "
                + " left join ent_master e on e.ent_id::text = b.ref5 ");

        if ("approval".equalsIgnoreCase(mode)) {
            sqa.addClause("b.fft_group_id='CAPA'");
        } else if ("commission".equalsIgnoreCase(mode)) {
            sqa.addClause("b.fft_group_id='COMM'");
        }

        if (ref1 != null) {
            sqa.addClause("b.ref1 = ? ");
            sqa.addPar(ref1);
        }

        if (ref2 != null) {
            sqa.addClause("b.ref2 = ? ");
            sqa.addPar(ref2);
        }

        return sqa;
    }
    
    public void addLine() throws Exception{
        
        FlexTableView ffNew = new FlexTableView();
        
        ffNew.markNew();
        
        ffNew.setStFFTGroupID("CAPA");
        ffNew.setStReference2("ACCEPT");
        ffNew.setStReference1(getRef1());

        list.add(ffNew);
    }

    public void addLineClaim() throws Exception{

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        ffNew.setStFFTGroupID("CAPA");
        ffNew.setStReference2("CLAIM");
        ffNew.setStReference1(getRef1());

        list.add(ffNew);
    }
    
    public void refresh(){
        
    }
    
    public void addLineComm() throws Exception{
        
        FlexTableView ffNew = new FlexTableView();
        
        ffNew.markNew();
        
        //ffNew.setStFFTGroupID("CAPA");
        ffNew.setStReference2(ref2);
        ffNew.setStReference1(getRef1());
        ffNew.setStFFTGroupID("COMM");

        list.add(ffNew);
    }
    
    public void onDeleteDetail() throws Exception{
        list.delete(Integer.parseInt(detailIndex));
    }

    public String getDetailIndex() {
        return detailIndex;
    }

    public void setDetailIndex(String detailIndex) {
        this.detailIndex = detailIndex;
    }

    public void addLineCommSetting() throws Exception{

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();
        
        ffNew.setStFFTGroupID("COMM_ITEM");
        ffNew.setStReference1(getRef1());
        ffNew.setStReference2(getRef2());

        list.add(ffNew);
    }

    /**
     * @return the ref6
     */
    public String getRef6() {
        return ref6;
    }

    /**
     * @param ref6 the ref6 to set
     */
    public void setRef6(String ref6) {
        this.ref6 = ref6;
    }

    public void select_branch(){
        
    }

    public void print_limit()throws Exception{
        super.redirect("/pages/capacity/report_branch.fop");
    }

    public SQLAssembler getSQA2() {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.fft_id,b.ref1,b.ref2,b.ref3,c.role_name as ref4,b.fft_group_id,b.ref7,b.refn1,b.refn2,b.refn3,b.period_start," +
                    " b.approved_flag1,b.approved_flag2,b.approved_flag3,b.approved_flag4,b.approved_flag1,b.active_flag,a.short_desc as ref5 ");

        sqa.addQuery("   from ins_policy_types a " +
                " left join ff_table b on b.ref3 = cast(a.pol_type_id as varchar) and b.fft_group_id='CAPA' and b.active_flag = 'Y' " +
                " left join s_roles c on c.role_id = b.ref1" );

        sqa.addClause(" c.role_id in ('731000','711100','711200','721300','721400','721500',"
                + "'721600','721700','031800','712000','712100','712200','722300','712400',"
                + "'712500','713000','733100','733200','713300','734000','034170','034270',"
                + "'724300','034470','725000','735100','035270','036000','036100','737000')");

        if (ref2!=null) {
            sqa.addClause("b.ref2 = ? ");
            sqa.addPar(ref2);
        }

        return sqa;
    }

    public void excel_limit() throws Exception {

        final DTOList l = EXCEL_LIMIT();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        EXPORT_LIMIT();
    }

    public DTOList EXCEL_LIMIT() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.fft_id,b.ref1,b.ref2,b.ref3,c.role_name as ref4,b.fft_group_id,b.ref7,b.refn1,b.refn2,b.refn3,b.period_start," +
                    " b.approved_flag1,b.approved_flag2,b.approved_flag3,b.approved_flag4,b.approved_flag1,b.active_flag,a.short_desc as ref5 ");

        sqa.addQuery("   from ins_policy_types a " +
                " left join ff_table b on b.ref3 = cast(a.pol_type_id as varchar) and b.fft_group_id='CAPA' and b.active_flag = 'Y' " +
                " left join s_roles c on c.role_id = b.ref1" );

        sqa.addClause(" c.role_id in ('731000','711100','711200','721300','721400','721500',"
                + "'721600','721700','031800','712000','712100','712200','722300','712400',"
                + "'712500','713000','733100','733200','713300','734000','034170','034270',"
                + "'724300','034470','725000','735100','035270','036000','036100','737000')");

        if (ref2!=null) {
            sqa.addClause("b.ref2 = ? ");
            sqa.addPar(ref2);
        }

        final String sql = sqa.getSQL() +
                " order by c.role_name,b.ref3 asc ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_LIMIT()  throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Role");
            row1.createCell(1).setCellValue("COB");
            row1.createCell(2).setCellValue("Limit 1");
            row1.createCell(3).setCellValue("Limit 2");
            row1.createCell(4).setCellValue("Limit 3");
            row1.createCell(5).setCellValue("Tgl Aktif");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ref4"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ref5"));
            if (h.getFieldValueByFieldNameBD("refn1")!=null)
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("refn1").doubleValue());
            if (h.getFieldValueByFieldNameBD("refn2")!=null)
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("refn2").doubleValue());
            if (h.getFieldValueByFieldNameBD("refn3")!=null)
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("refn3").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename=titipan.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
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

    public void onChangePolicyTypeGroup(){

    }

    public void addLineCommGroup() throws Exception{

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        //ffNew.setStFFTGroupID("CAPA");
        ffNew.setStReference2(ref2);
        ffNew.setStReference1(getRef1());
        ffNew.setStReference4(getRef4());
        ffNew.setStFFTGroupID("COMM");

        list.add(ffNew);
    }

    /**
     * @return the enableEditAuthorityLimit
     */
    public boolean isEnableEditAuthorityLimit() {
        return enableEditAuthorityLimit;
    }

    /**
     * @param enableEditAuthorityLimit the enableEditAuthorityLimit to set
     */
    public void setEnableEditAuthorityLimit(boolean enableEditAuthorityLimit) {
        this.enableEditAuthorityLimit = enableEditAuthorityLimit;
    }

    /**
     * @return the enableEditRKAPLimit
     */
    public boolean isEnableEditRKAPLimit() {
        return enableEditRKAPLimit;
    }

    /**
     * @param enableEditRKAPLimit the enableEditRKAPLimit to set
     */
    public void setEnableEditRKAPLimit(boolean enableEditRKAPLimit) {
        this.enableEditRKAPLimit = enableEditRKAPLimit;
    }

    public void authority() {
        mode = "authority";
        setTitle("Limit Kewenangan");
    }

    public void rkap() {
        mode = "rkap";
        setTitle("Anggaran RKAP");
    }

    public void select_authority() throws Exception {
        list = null;

        if ((ref2 != null)) {
            list = ListUtil.getDTOListFromQuery(
                    "   select b.* "
                    + "   from ff_table b where b.ref2=? and b.fft_group_id = 'AUTHO'"
                    + "   order by b.period_start ",
                    new Object[]{ref2},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("AUTHO");
                ft.setStReference2(ref2);
                ft.setDbReference1(ft.getDbReference1());

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setEditMode(true);
        setShowButton(false);
    }

    public void view_authority() throws Exception {

        if ((ref2 != null) && (list == null)) {
            list = ListUtil.getDTOListFromQuery(
                    "   select b.* "
                    + "   from ff_table b where b.ref2=? and b.fft_group_id = 'AUTHO'"
                    + "   order by b.period_start ",
                    new Object[]{ref2},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("AUTHO");
                ft.setStReference2(ref2);
                ft.setDbReference1(ft.getDbReference1());
                ft.setStAddFlag(null);
                ft.setDtPeriodEnd(null);

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setReadOnly(true);
        setEditMode(false);
        setShowButton(false);
    }

    public void addLineAutho() throws Exception {

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        ffNew.setStReference2(ref2);
        ffNew.setStFFTGroupID("AUTHO");

        list.add(ffNew);
    }

    public void select_rkap() throws Exception {
        list = null;

        if ((ref1 != null) && (list == null)) {
            list = ListUtil.getDTOListFromQuery(
                    " select b.*, a.biaop_dtl_id, a.description "
                    + " from s_biaop_detail a "
                    + " inner join ff_table b on b.ref3 = cast(a.biaop_dtl_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='RKAP' "
                    + " order by a.biaop_dtl_id,b.period_start ",
                    new Object[]{ref1, ref2},
                    FlexTableView.class);

            if ((ref1 != null) && getStBiaopTypeID() != null) {
                list = ListUtil.getDTOListFromQuery(
                        " select b.*, a.biaop_dtl_id, a.description "
                        + " from s_biaop_detail a "
                        + " inner join ff_table b on b.ref3 = cast(a.biaop_dtl_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='RKAP'"
                        + " and b.ref3 = ? order by a.biaop_dtl_id,b.period_start ",
                        new Object[]{ref1, ref2, getStBiaopTypeID()},
                        FlexTableView.class);
            }

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("RKAP");
                ft.setStReference1(ref1);
                ft.setStReference2(ref2);
                ft.setStReference3(String.valueOf(ft.getAttribute("biaop_dtl_id")));
                ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
                ft.setDbReference1(ft.getDbReference1());

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setEditMode(true);
        setShowButton(false);
    }

    public void view_rkap() throws Exception {

        if ((ref1 != null) && (list == null)) {
            list = ListUtil.getDTOListFromQuery(
                    " select b.*, a.biaop_dtl_id, a.description "
                    + " from s_biaop_detail a "
                    + " inner join ff_table b on b.ref3 = cast(a.biaop_dtl_id as varchar) and b.ref1=? and b.ref2=? and b.fft_group_id='RKAP' "
                    + " order by a.biaop_dtl_id,b.period_start ",
                    new Object[]{ref1, ref2},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("RKAP");
                ft.setStReference1(ref1);
                ft.setStReference2(ref2);
                ft.setStReference3(String.valueOf(ft.getAttribute("biaop_dtl_id")));
                ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
                ft.setDbReference1(ft.getDbReference1());
                ft.setStAddFlag(null);
                ft.setDtPeriodEnd(null);

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setReadOnly(true);
        setEditMode(false);
        setShowButton(false);
    }

    public void addLineRKAP() throws Exception {

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        ffNew.setStFFTGroupID("RKAP");
        ffNew.setStReference1(getRef1());
        ffNew.setStReference2(getRef2());

        list.add(ffNew);
    }

    public void onChangeRKAPGroup() {
    }

    public void onChangeBiaopGroup() {
        
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
     * @return the stBiaopGroupID
     */
    public String getStBiaopGroupID() {
        return stBiaopGroupID;
    }

    /**
     * @param stBiaopGroupID the stBiaopGroupID to set
     */
    public void setStBiaopGroupID(String stBiaopGroupID) {
        this.stBiaopGroupID = stBiaopGroupID;
    }

    /**
     * @return the stBiaopTypeID
     */
    public String getStBiaopTypeID() {
        return stBiaopTypeID;
    }

    /**
     * @param stBiaopTypeID the stBiaopTypeID to set
     */
    public void setStBiaopTypeID(String stBiaopTypeID) {
        this.stBiaopTypeID = stBiaopTypeID;
    }

    /**
     * @return the ref3
     */
    public String getRef3() {
        return ref3;
    }

    /**
     * @param ref3 the ref3 to set
     */
    public void setRef3(String ref3) {
        this.ref3 = ref3;
    }


    /**
     * @return the enableEditKasLimit
     */
    public boolean isEnableEditKasLimit() {
        return enableEditKasLimit;
    }
    /**
     * @param enableEditKasLimit the enableEditKasLimit to set
     */
    public void setEnableEditKasLimit(boolean enableEditKasLimit) {
        this.enableEditKasLimit = enableEditKasLimit;
    }

    public void select_kas() throws Exception {
        list = null;

        if ((ref1 != null) && (list == null)) {
            list = ListUtil.getDTOListFromQuery(
                    " select b.*,a.method_code,a.description "
                    + " from ff_table b "
                    + " inner join receipt_class a on a.method_code = b.ref3 "
                    + " where b.ref1 = ? and b.ref2 = ? and b.fft_group_id='KAS' "
                    + " order by b.period_start ",
                    new Object[]{ref1, ref2},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("KAS");
                ft.setStReference1(ref1);
                ft.setStReference2(ref2);
                ft.setStReference3(String.valueOf(ft.getAttribute("method_code")));
                ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
                ft.setDbReference1(ft.getDbReference1());

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setEditMode(true);
        setShowButton(false);
    }

    public void view_kas() throws Exception {

        if ((ref1 != null) && (list == null)) {
            list = ListUtil.getDTOListFromQuery(
                    " select b.*,a.method_code,a.description "
                    + " from ff_table b "
                    + " inner join receipt_class a on a.method_code = b.ref3 "
                    + " where b.ref1 = ? and b.ref2 = ? and b.fft_group_id='KAS' "
                    + " order by b.period_start ",
                    new Object[]{ref1, ref2},
                    FlexTableView.class);

            for (int i = 0; i < list.size(); i++) {
                FlexTableView ft = (FlexTableView) list.get(i);

                ft.setStFFTGroupID("KAS");
                ft.setStReference1(ref1);
                ft.setStReference2(ref2);
                ft.setStReference3(String.valueOf(ft.getAttribute("method_code")));
                ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
                ft.setDbReference1(ft.getDbReference1());
                ft.setStAddFlag(null);
                ft.setDbReference2(null);
                ft.setDtPeriodEnd(null);

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setReadOnly(true);
        setEditMode(false);
        setShowButton(false);
    }

    public void addLineKAS() throws Exception {

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        ffNew.setStFFTGroupID("KAS");
        ffNew.setStReference1(getRef1());
        ffNew.setStReference2(getRef2());

        list.add(ffNew);
    }

    public void kas() {
        mode = "kas";
        setTitle("Limit Kas");
    }

    public void approvalkonsumtif() {
      mode = "approvalkonsumtif";
      setTitle("Approval Limit Konsumtif");
   }

    public void selectApprovalKonsumtif() throws Exception {

      list = null;

      /*
      if (ref2!=null && list==null && ref4 ==null) {

              list=ListUtil.getDTOListFromQuery(
                     "  select * "+
                     "  from ins_rates_big "+
                     "  where rate_class = 'OM_CREDIT_LIMIT' "+
                     "  and ref2 = ? and date_trunc('day',period_end) >= ? order by ref1,ref4,ref3",
                     new Object [] {ref2, getDtPeriodEnd()},
                     InsuranceRatesBigView.class
             );

         for (int i = 0; i < list.size(); i++) {
            InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

            if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
         }
      }else if (ref2!=null && list==null && ref4 !=null){
          list=ListUtil.getDTOListFromQuery(
                     "  select * "+
                     "  from ins_rates_big "+
                     "  where rate_class = 'OM_CREDIT_LIMIT' "+
                     "  and ref2 = ? and date_trunc('day',period_end) >= ? and ref4 = ? order by ref1,ref4,ref3",
                     new Object [] {ref2, getDtPeriodEnd(), ref4},
                     InsuranceRatesBigView.class
             );

         for (int i = 0; i < list.size(); i++) {
            InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

            if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
         }
      }*/


      final SQLAssembler sqa = new SQLAssembler();
      sqa.addSelect("*");

      sqa.addQuery("from ins_rates_big");
      sqa.addClause("rate_class = 'OM_CREDIT_LIMIT'");

      if(ref2!=null){
           sqa.addClause(" ref2 = ?");
           sqa.addPar(ref2);
      }

      if(getDtPeriodEnd()!=null){
           sqa.addClause(" date_trunc('day',period_end) >= ?");
           sqa.addPar(getDtPeriodEnd());
      }

      if(ref4!=null){
           sqa.addClause(" ref4 = ?");
           sqa.addPar(ref4);
      }

      if(getStUsia()!=null){
           sqa.addClause(" ref3 = ?");
           sqa.addPar(getStUsia());
      }

      if(getStGrupSumbis()!=null){
           sqa.addClause(" ref1 = ?");
           sqa.addPar(getStGrupSumbis());
      }

      sqa.addOrder("ref1,ref4,ref3");

      list = sqa.getList(InsuranceRatesBigView.class);

      for (int i = 0; i < list.size(); i++) {
        InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

        if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
     }


      setEditMode(true);
      setShowButton(false);
   }

    public void addLineApprovalKonsumtif() throws Exception{

        InsuranceRatesBigView ratesNew = new InsuranceRatesBigView();

        ratesNew.markNew();

        ratesNew.setStRateClass("OM_CREDIT_LIMIT");
        ratesNew.setStReference2(getRef2());

        list.add(ratesNew);
    }

    /**
     * @return the stFileID
     */
    public String getStFileID() {
        return stFileID;
    }

    /**
     * @param stFileID the stFileID to set
     */
    public void setStFileID(String stFileID) {
        this.stFileID = stFileID;
    }

    public void createApprovalKonsumtif() throws Exception{

        InsuranceRatesBigView ratesNew = new InsuranceRatesBigView();

        ratesNew.markNew();

        list = new DTOList();

        //ratesNew.setStRateClass("OM_CREDIT_LIMIT");
        //ratesNew.setStReference2(getRef2());

        //list.add(ratesNew);
    }

    public void uploadExcel() throws Exception {


        String fileID = getStFileID();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("limit_kewenangan");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet limit_kewenangan tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        //list.add(details);

        list = new DTOList();

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 2; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellKode= row.getCell(0);
            HSSFCell cellKeterangan= row.getCell(1);
            HSSFCell cellTanggalAwal = row.getCell(2);
            HSSFCell cellTanggalAkhir = row.getCell(3);
            HSSFCell cellAutoCover = row.getCell(4);
            HSSFCell cellJenisKredit = row.getCell(5);
            HSSFCell cellCabang = row.getCell(6);
            HSSFCell cellUsia = row.getCell(7);
            HSSFCell cellSDUsia = row.getCell(8);
            HSSFCell cellPekerjaan = row.getCell(9);

            HSSFCell cellTSIAwal = row.getCell(10);
            HSSFCell cellTSIAkhir = row.getCell(11);
            HSSFCell cellNoKetentuan = row.getCell(12);
            HSSFCell cellDokumenKesehatan = row.getCell(13);
            HSSFCell cellActiveFlag = row.getCell(14);


            InsuranceRatesBigView rate = new InsuranceRatesBigView();
            rate.markNew();

            rate.setStRateClass(cellKode.getStringCellValue());
            rate.setStDescription(cellKeterangan.getStringCellValue());
            rate.setDtPeriodStart(cellTanggalAwal.getDateCellValue());
            rate.setDtPeriodEnd(cellTanggalAkhir.getDateCellValue());


            rate.setStReference5(cellAutoCover.getCellType() == cellAutoCover.CELL_TYPE_STRING ? cellAutoCover.getStringCellValue() : new BigDecimal(cellAutoCover.getNumericCellValue()).toString());
            rate.setStReference1(cellJenisKredit.getCellType() == cellJenisKredit.CELL_TYPE_STRING ? cellJenisKredit.getStringCellValue() : new BigDecimal(cellJenisKredit.getNumericCellValue()).toString());
            rate.setStReference2(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            rate.setStReference3(cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING ? cellUsia.getStringCellValue() : new BigDecimal(cellUsia.getNumericCellValue()).toString());
            rate.setStReference6(cellSDUsia.getCellType() == cellSDUsia.CELL_TYPE_STRING ? cellSDUsia.getStringCellValue() : new BigDecimal(cellSDUsia.getNumericCellValue()).toString());
            rate.setStReference4(cellPekerjaan.getCellType() == cellPekerjaan.CELL_TYPE_STRING ? cellPekerjaan.getStringCellValue() : new BigDecimal(cellPekerjaan.getNumericCellValue()).toString());
            rate.setDbRate0(new BigDecimal(cellTSIAwal.getNumericCellValue()));
            rate.setDbRate1(new BigDecimal(cellTSIAkhir.getNumericCellValue()));
            rate.setStNotes(cellDokumenKesehatan.getStringCellValue());
            rate.setStActiveFlag(cellActiveFlag.getStringCellValue());

            details.add(rate);

        }

        setList(details);

    }

    public void saveApprovalKonsumtif() throws Exception {

      for (int i = 0; i < list.size(); i++) {
         InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

         if (rates.isNew())
               rates.setStInsuranceRatesID(String.valueOf(IDFactory.createNumericID("INSRATESID")));
            //else
               //rates.markUnmodified();

      }

      SQLUtil.qstore(list);

      close();
   }

   public void applyAll() throws Exception{
       InsuranceRatesBigView rate1 = (InsuranceRatesBigView) list.get(0);

       for (int i = 0; i < list.size(); i++) {
         InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

         rates.setDtPeriodStart(rate1.getDtPeriodStart());
         rates.setDtPeriodEnd(rate1.getDtPeriodEnd());

      }

   }

   public void ratekonsumtif() {
      mode = "ratekonsumtif";
      setTitle("Limit Rate Konsumtif");
   }

   public void selectRateKonsumtif() throws Exception {

      list = null;

      /*
      if (ref2!=null && list==null && ref4 ==null) {

              list=ListUtil.getDTOListFromQuery(
                     "  select * "+
                     "  from ins_rates_big "+
                     "  where rate_class = 'OM_CREDIT_RATE' "+
                     "  and ref2 = ? and date_trunc('day',period_end) >= ? order by ref1,ref3,ref4,ref5",
                     new Object [] {ref2, getDtPeriodEnd()},
                     InsuranceRatesBigView.class
             );

         for (int i = 0; i < list.size(); i++) {
            InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

            if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
         }
      }else if (ref2!=null && list==null && ref4 !=null) {

              list=ListUtil.getDTOListFromQuery(
                     "  select * "+
                     "  from ins_rates_big "+
                     "  where rate_class = 'OM_CREDIT_RATE' "+
                     "  and ref2 = ? and date_trunc('day',period_end) >= ? and ref4 = ? order by ref1,ref3,ref4,ref5",
                     new Object [] {ref2, getDtPeriodEnd(), ref4},
                     InsuranceRatesBigView.class
             );

         for (int i = 0; i < list.size(); i++) {
            InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

            if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
         }
      }*/

      final SQLAssembler sqa = new SQLAssembler();
      sqa.addSelect("*");

      sqa.addQuery("from ins_rates_big");
      sqa.addClause("rate_class = 'OM_CREDIT_RATE'");

      if(ref2!=null){
           sqa.addClause(" ref2 = ?");
           sqa.addPar(ref2);
      }

      if(getDtPeriodEnd()!=null){
           sqa.addClause(" date_trunc('day',period_end) >= ?");
           sqa.addPar(getDtPeriodEnd());
      }

      if(ref4!=null){
           sqa.addClause(" ref4 = ?");
           sqa.addPar(ref4);
      }

      if(getStUsia()!=null){
           sqa.addClause(" ref5 = ?");
           sqa.addPar(getStUsia());
      }

      if(getStGrupSumbis()!=null){
           sqa.addClause(" ref1 = ?");
           sqa.addPar(getStGrupSumbis());
      }

      sqa.addOrder("ref1,ref4,ref3");

      list = sqa.getList(InsuranceRatesBigView.class);

      for (int i = 0; i < list.size(); i++) {
        InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

        if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
     }


      setEditMode(true);
      setShowButton(false);
   }

    public void clickApproval1TSI() throws Exception {

        select_approval_tsi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }
                
            if(ft.getStApprovedWho1()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh Operator");
            }
                
            if(ft.getStApprovedWho3()!=null){
                //list = new DTOList(); 
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh kepala bagian");
            }

            ft.setStApprovedFlag1("Y");
            ft.setStApprovedWho1(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate1(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKadivTI);
            setStEmailText("Limit Kewenangan TSI \n\nRole :" + getRef1()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kasie U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);

         }
        
   }

    public void clickApproval2TSI() throws Exception {

        select_approval_tsi();

        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho1()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }

            if(ft.getStApprovedWho2()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh kepala bagian");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }

            */

            ft.setStApprovedFlag2("Y");
            ft.setStApprovedWho2(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate2(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKadivUW);
            setStEmailText("Limit Kewenangan TSI \n\nRole :" + getRef1()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kadiv U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    public void clickApproval3TSI() throws Exception {

        select_approval_tsi();

        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho1()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }

            if(ft.getStApprovedWho2()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh kepala bagian");
            }

            if(ft.getStApprovedWho3()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh kepala divisi");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }
                
            */

            ft.setStApprovedFlag3("Y");
            ft.setStApprovedWho3(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate3(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKadivUW);
            setStEmailText("Limit Kewenangan TSI \n\nRole :" + getRef1()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kadiv U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }
 
   }

    public void clickApproval4TSI() throws Exception {

        select_approval_tsi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho4()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah di aktivasi oleh TI");
            }   

            
            if(ft.getStApprovedWho3()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kepala Divisi");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }
                
            if(ft.getStApprovedWho1()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }*/

            ft.setStActiveFlag("Y");
            ft.setStApprovedFlag4("Y");
            ft.setStApprovedWho4(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate4(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKabagTI+","+stEmailKadivTI+","+stEmailKabagUW+","+stEmailKadivUW);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef1()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nSudah Aktif & Disetujui\n"+
                            "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

     public void clickActivateTSI() throws Exception {

        select_approval_tsi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho4()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh Kepala Divisi");
            }


            if(ft.getStApprovedWho3()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kepala Bagian");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }

            if(ft.getStApprovedWho1()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }*/

            ft.setStActiveFlag("Y");
            //ft.setStApprovedFlag4("Y");
            ft.setStApprovedWho4(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate4(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKabagTI+","+stEmailKadivTI+","+stEmailKabagUW+","+stEmailKadivUW);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef1()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nSudah Aktif & Disetujui\n"+
                            "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    /**
     * @return the enableApprovalKomisi1
     */
    public boolean isEnableApprovalKomisi1() {
        return enableApprovalKomisi1;
    }

    /**
     * @param enableApprovalKomisi1 the enableApprovalKomisi1 to set
     */
    public void setEnableApprovalKomisi1(boolean enableApprovalKomisi1) {
        this.enableApprovalKomisi1 = enableApprovalKomisi1;
    }

    /**
     * @return the enableApprovalKomisi2
     */
    public boolean isEnableApprovalKomisi2() {
        return enableApprovalKomisi2;
    }

    /**
     * @param enableApprovalKomisi2 the enableApprovalKomisi2 to set
     */
    public void setEnableApprovalKomisi2(boolean enableApprovalKomisi2) {
        this.enableApprovalKomisi2 = enableApprovalKomisi2;
    }

    /**
     * @return the enableApprovalKomisi3
     */
    public boolean isEnableApprovalKomisi3() {
        return enableApprovalKomisi3;
    }

    /**
     * @param enableApprovalKomisi3 the enableApprovalKomisi3 to set
     */
    public void setEnableApprovalKomisi3(boolean enableApprovalKomisi3) {
        this.enableApprovalKomisi3 = enableApprovalKomisi3;
    }

    /**
     * @return the enableApprovalKomisi4
     */
    public boolean isEnableApprovalKomisi4() {
        return enableApprovalKomisi4;
    }

    /**
     * @param enableApprovalKomisi4 the enableApprovalKomisi4 to set
     */
    public void setEnableApprovalKomisi4(boolean enableApprovalKomisi4) {
        this.enableApprovalKomisi4 = enableApprovalKomisi4;
    }

    public void clickApproval1Komisi() throws Exception {

        select_approval_komisi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho1()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh operator");
            }

            if(ft.getStApprovedWho2()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh kepala bagian");
            }

            ft.setStApprovedFlag1("Y");
            ft.setStApprovedWho1(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate1(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKadivTI);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef2()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kasie U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    public void clickApproval2Komisi() throws Exception {

        select_approval_komisi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho2()!=null)
                throw new RuntimeException("Data sudah disetujui oleh kepala bagian");

            if(ft.getStApprovedWho1()==null){
                //list = new DTOList();
                setEditMode(false);

                throw new RuntimeException("Data belum disetujui oleh Operator");
            }

            ft.setStApprovedFlag2("Y");
            ft.setStApprovedWho2(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate2(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKabagUW);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef2()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kabag U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    public void clickApproval3Komisi() throws Exception {

        select_approval_komisi();

        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho1()==null){
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }

            if(ft.getStApprovedWho2()==null){
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh kepala bagian");
            }

            if(ft.getStApprovedWho3()!=null){
                setEditMode(false);
                throw new RuntimeException("Data sudah disetujui oleh kepala divisi");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }

            if(ft.getStApprovedWho1()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }*/

            ft.setStApprovedFlag3("Y");
            ft.setStApprovedWho3(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate3(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKadivUW);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef2()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nButuh Approval Anda (Kadiv U/W)\n"+
                            "http://webapps.askrida.co.id/fin/index.jsp \n"
                            + "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    public void clickApproval4Komisi() throws Exception {

        select_approval_komisi();
        setEditMode(false);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            if(ft.isActive()){
                //ft.markUnmodified();
                continue;
            }

            if(ft.getStApprovedWho4()!=null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data sudah di aktivasi oleh TI");
            }

            if(ft.getStApprovedWho3()==null){
                //list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kepala Divisi");
            }

            /*
            if(ft.getStApprovedWho2()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Kasie U/W");
            }

            if(ft.getStApprovedWho1()==null){
                list = new DTOList();
                setEditMode(false);
                throw new RuntimeException("Data belum disetujui oleh Operator");
            }*/

            ft.setStActiveFlag("Y");
            ft.setStApprovedFlag4("Y");
            ft.setStApprovedWho4(UserManager.getInstance().getUser().getStUserID());
            ft.setDtApprovedDate4(new Date());

            setSendEmail(true);
            setStEmailReceiver(stEmailKabagTI+","+stEmailKadivTI+","+stEmailKabagUW+","+stEmailKadivUW);
            setStEmailText("Limit Kewenangan Komisi \n\nRole :" + getRef2()+"\nJenis :"+ ft.getStReference3()+" ("+LanguageManager.getInstance().translate(ft.getStReference3Desc(), "INA")+")\n\nSudah Aktif & Disetujui\n"+
                            "\n\n"+
                            "Terima Kasih\n"+
                            "PT ASURANSI BANGUN ASKRIDA\n"+
                            "Divisi Teknologi Informasi\n"+
                            "Sistem Web Askrida\n");

            setIsApprovalFlag1Mode(true);
            setApprovalMode(true);
            setShowButton(false);
            setEditMode(true);
         }

   }

    /**
     * @return the sendEmail
     */
    public boolean isSendEmail() {
        return sendEmail;
    }

    /**
     * @param sendEmail the sendEmail to set
     */
    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    /**
     * @return the stEmailReceiver
     */
    public String getStEmailReceiver() {
        return stEmailReceiver;
    }

    /**
     * @param stEmailReceiver the stEmailReceiver to set
     */
    public void setStEmailReceiver(String stEmailReceiver) {
        this.stEmailReceiver = stEmailReceiver;
    }

    /**
     * @return the stEmailText
     */
    public String getStEmailText() {
        return stEmailText;
    }

    /**
     * @param stEmailText the stEmailText to set
     */
    public void setStEmailText(String stEmailText) {
        this.stEmailText = stEmailText;
    }

    public void uploadExcelRate() throws Exception {


        String fileID = getStFileID();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("rate_jual");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet limit_kewenangan tidak Ditemukan, download format excel pada menu Dokumen");

        final DTOList details = new DTOList();

        //list.add(details);

        list = new DTOList();

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellKode= row.getCell(0);
            HSSFCell cellKeterangan= row.getCell(1);
            HSSFCell cellTanggalAwal = row.getCell(2);
            HSSFCell cellTanggalAkhir = row.getCell(3);
            HSSFCell cellGrupSumbis = row.getCell(4);
            HSSFCell cellCabang = row.getCell(5);
            HSSFCell cellTipeCover = row.getCell(6);
            HSSFCell cellUsia = row.getCell(7);
            HSSFCell cellSDUsia = row.getCell(8);

            HSSFCell cellPekerjaan = row.getCell(9);
            HSSFCell cellRate0 = row.getCell(10);
            HSSFCell cellRate1 = row.getCell(11);
            HSSFCell cellRate2 = row.getCell(12);
            HSSFCell cellRate3 = row.getCell(13);
            HSSFCell cellRate4 = row.getCell(14);
            HSSFCell cellRate5 = row.getCell(15);
            HSSFCell cellRate6 = row.getCell(16);
            HSSFCell cellRate7 = row.getCell(17);
            HSSFCell cellRate8 = row.getCell(18);
            HSSFCell cellRate9 = row.getCell(19);
            HSSFCell cellRate10 = row.getCell(20);
            HSSFCell cellRate11 = row.getCell(21);

            HSSFCell cellRate12 = row.getCell(22);
            HSSFCell cellRate13 = row.getCell(23);
            HSSFCell cellRate14 = row.getCell(24);
            HSSFCell cellRate15 = row.getCell(25);
            HSSFCell cellRate16 = row.getCell(26);
            HSSFCell cellRate17 = row.getCell(27);
            HSSFCell cellRate18 = row.getCell(28);
            HSSFCell cellRate19 = row.getCell(29);
            HSSFCell cellRate20 = row.getCell(30);

            HSSFCell cellRate21 = row.getCell(31);
            HSSFCell cellRate22 = row.getCell(32);
            HSSFCell cellRate23 = row.getCell(33);
            HSSFCell cellRate24 = row.getCell(34);
            HSSFCell cellRate25 = row.getCell(35);
            HSSFCell cellRate26 = row.getCell(36);
            HSSFCell cellRate27 = row.getCell(37);
            HSSFCell cellRate28 = row.getCell(38);
            HSSFCell cellRate29 = row.getCell(39);
            HSSFCell cellRate30 = row.getCell(40);
            HSSFCell cellRate31 = row.getCell(41);
            HSSFCell cellRate32 = row.getCell(42);
            HSSFCell cellRate33 = row.getCell(43);
            HSSFCell cellRate34 = row.getCell(44);
            HSSFCell cellRate35 = row.getCell(45);
            HSSFCell cellRate36 = row.getCell(46);
            HSSFCell cellRate37 = row.getCell(47);
            HSSFCell cellRate38 = row.getCell(48);
            HSSFCell cellRate39 = row.getCell(49);
            HSSFCell cellRate40 = row.getCell(50);

            HSSFCell cellActiveFlag = row.getCell(51);

            InsuranceRatesBigView rate = new InsuranceRatesBigView();
            rate.markNew();

            rate.setStRateClass(cellKode.getStringCellValue());
            rate.setStDescription(cellKeterangan.getStringCellValue());
            rate.setDtPeriodStart(cellTanggalAwal.getDateCellValue());
            rate.setDtPeriodEnd(cellTanggalAkhir.getDateCellValue());

            rate.setStReference1(cellGrupSumbis.getCellType() == cellGrupSumbis.CELL_TYPE_STRING ? cellGrupSumbis.getStringCellValue() : new BigDecimal(cellGrupSumbis.getNumericCellValue()).toString());
            rate.setStReference2(cellCabang.getCellType() == cellCabang.CELL_TYPE_STRING ? cellCabang.getStringCellValue() : new BigDecimal(cellCabang.getNumericCellValue()).toString());
            rate.setStReference3(cellTipeCover.getCellType() == cellTipeCover.CELL_TYPE_STRING ? cellTipeCover.getStringCellValue() : new BigDecimal(cellTipeCover.getNumericCellValue()).toString());
            rate.setStReference5(cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING ? cellUsia.getStringCellValue() : new BigDecimal(cellUsia.getNumericCellValue()).toString());
            rate.setStReference6(cellSDUsia.getCellType() == cellSDUsia.CELL_TYPE_STRING ? cellSDUsia.getStringCellValue() : new BigDecimal(cellSDUsia.getNumericCellValue()).toString());
            rate.setStReference4(cellPekerjaan.getCellType() == cellPekerjaan.CELL_TYPE_STRING ? cellPekerjaan.getStringCellValue() : new BigDecimal(cellPekerjaan.getNumericCellValue()).toString());

            if(cellRate1!=null)
                rate.setDbRate1(new BigDecimal(cellRate1.getNumericCellValue()));

            if(cellRate2!=null)
                rate.setDbRate2(new BigDecimal(cellRate2.getNumericCellValue()));

            if(cellRate3!=null)
                rate.setDbRate3(new BigDecimal(cellRate3.getNumericCellValue()));

            if(cellRate4!=null)
                rate.setDbRate4(new BigDecimal(cellRate4.getNumericCellValue()));

            if(cellRate5!=null)
                rate.setDbRate5(new BigDecimal(cellRate5.getNumericCellValue()));

            if(cellRate6!=null)
                rate.setDbRate6(new BigDecimal(cellRate6.getNumericCellValue()));

            if(cellRate7!=null)
                rate.setDbRate7(new BigDecimal(cellRate7.getNumericCellValue()));

            if(cellRate8!=null)
                rate.setDbRate8(new BigDecimal(cellRate8.getNumericCellValue()));

            if(cellRate9!=null)
                rate.setDbRate9(new BigDecimal(cellRate9.getNumericCellValue()));

            if(cellRate10!=null)
                rate.setDbRate10(new BigDecimal(cellRate10.getNumericCellValue()));

            if(cellRate11!=null)
                rate.setDbRate11(new BigDecimal(cellRate11.getNumericCellValue()));

            if(cellRate12!=null)
                rate.setDbRate12(new BigDecimal(cellRate12.getNumericCellValue()));

            if(cellRate13!=null)
                rate.setDbRate13(new BigDecimal(cellRate13.getNumericCellValue()));

            if(cellRate14!=null)
                rate.setDbRate14(new BigDecimal(cellRate14.getNumericCellValue()));

            if(cellRate15!=null)
                rate.setDbRate15(new BigDecimal(cellRate15.getNumericCellValue()));

            if(cellRate16!=null)
                rate.setDbRate16(new BigDecimal(cellRate16.getNumericCellValue()));

            if(cellRate17!=null)
                rate.setDbRate17(new BigDecimal(cellRate17.getNumericCellValue()));

            if(cellRate18!=null)
                rate.setDbRate18(new BigDecimal(cellRate18.getNumericCellValue()));

            if(cellRate19!=null)
                rate.setDbRate19(new BigDecimal(cellRate19.getNumericCellValue()));

            if(cellRate20!=null)
                rate.setDbRate20(new BigDecimal(cellRate20.getNumericCellValue()));

            if(cellRate21!=null)
                rate.setDbRate21(new BigDecimal(cellRate21.getNumericCellValue()));

            if(cellRate22!=null)
                rate.setDbRate22(new BigDecimal(cellRate22.getNumericCellValue()));

            if(cellRate23!=null)
                rate.setDbRate23(new BigDecimal(cellRate23.getNumericCellValue()));

            if(cellRate24!=null)
                rate.setDbRate24(new BigDecimal(cellRate24.getNumericCellValue()));

            if(cellRate25!=null)
                rate.setDbRate25(new BigDecimal(cellRate25.getNumericCellValue()));

            if(cellRate26!=null)
                rate.setDbRate26(new BigDecimal(cellRate26.getNumericCellValue()));

            if(cellRate27!=null)
                rate.setDbRate27(new BigDecimal(cellRate27.getNumericCellValue()));

            if(cellRate28!=null)
                rate.setDbRate28(new BigDecimal(cellRate28.getNumericCellValue()));

            if(cellRate29!=null)
                rate.setDbRate29(new BigDecimal(cellRate29.getNumericCellValue()));

            if(cellRate30!=null)
                rate.setDbRate30(new BigDecimal(cellRate30.getNumericCellValue()));

            if(cellRate31!=null)
                rate.setDbRate31(new BigDecimal(cellRate31.getNumericCellValue()));

            if(cellRate32!=null)
                rate.setDbRate32(new BigDecimal(cellRate32.getNumericCellValue()));

            if(cellRate33!=null)
                rate.setDbRate33(new BigDecimal(cellRate33.getNumericCellValue()));

            if(cellRate34!=null)
                rate.setDbRate34(new BigDecimal(cellRate34.getNumericCellValue()));

            if(cellRate35!=null)
                rate.setDbRate35(new BigDecimal(cellRate35.getNumericCellValue()));

            if(cellRate36!=null)
                rate.setDbRate36(new BigDecimal(cellRate36.getNumericCellValue()));

            if(cellRate37!=null)
                rate.setDbRate37(new BigDecimal(cellRate37.getNumericCellValue()));

            if(cellRate38!=null)
                rate.setDbRate38(new BigDecimal(cellRate38.getNumericCellValue()));

            if(cellRate39!=null)
                rate.setDbRate39(new BigDecimal(cellRate39.getNumericCellValue()));

            if(cellRate40!=null)
                rate.setDbRate40(new BigDecimal(cellRate40.getNumericCellValue()));

            rate.setStActiveFlag(cellActiveFlag.getStringCellValue());

            details.add(rate);

        }

        setList(details);

    }

    /**
     * @return the stUsia
     */
    public String getStUsia() {
        return stUsia;
    }
 
    /**
     * @param stUsia the stUsia to set
     */
    public void setStUsia(String stUsia) {
        this.stUsia = stUsia;
    }


    public void selectWarranty() throws Exception {

      list = null;

      final SQLAssembler sqa = new SQLAssembler();
      sqa.addSelect("*");

      sqa.addQuery("from ins_rates_big");
      sqa.addClause("rate_class = 'OM_WARRANTY'");

      if(ref2!=null){
           sqa.addClause(" ref2 = ?");
           sqa.addPar(ref2);
      }

      if(stPolicyTypeID!=null){
           sqa.addClause(" ref3 = ?");
           sqa.addPar(stPolicyTypeID);
      }

      if(getDtPeriodEnd()!=null){
           sqa.addClause(" date_trunc('day',period_end) >= ?");
           sqa.addPar(getDtPeriodEnd());
      }

      if(ref4!=null){
           sqa.addClause(" ref4 = ?");
           sqa.addPar(ref4);
      }

      if(getStUsia()!=null){
           sqa.addClause(" ref5 = ?");
           sqa.addPar(getStUsia());
      }

      if(getStGrupSumbis()!=null){
           sqa.addClause(" ref1 = ?");
           sqa.addPar(getStGrupSumbis());
      }

      sqa.addOrder("ref1");

      list = sqa.getList(InsuranceRatesBigView.class);

      for (int i = 0; i < list.size(); i++) {
        InsuranceRatesBigView rates = (InsuranceRatesBigView) list.get(i);

        if (rates.getStInsuranceRatesID()!=null) rates.markUpdate();// else ft.markNew();
     }


      setEditMode(true);
      setShowButton(false);
   }

    public void warranty() {
      mode = "warranty";
      setTitle("Warranty Settings");
      ref2 = SessionManager.getInstance().getSession().getStBranch();
   }

    public void addLineWarranty() throws Exception{

        InsuranceRatesBigView ratesNew = new InsuranceRatesBigView();

        ratesNew.markNew();

        ratesNew.setStRateClass("OM_WARRANTY");
        ratesNew.setStReference2(getRef2());

        list.add(ratesNew);

    }

    /**
     * @return the stGrupSumbis
     */
    public String getStGrupSumbis() {
        return stGrupSumbis;
    }

    /**
     * @param stGrupSumbis the stGrupSumbis to set
     */
    public void setStGrupSumbis(String stGrupSumbis) {
        this.stGrupSumbis = stGrupSumbis;
    }

    public void select_approval_tsi() throws Exception {
       list = null;

       final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.*, a.pol_type_id, a.description");
        sqa.addQuery(" from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.fft_group_id='CAPA'");

        if (getRef1()!=null) {
            sqa.addClause("b.ref1=?");
            sqa.addPar(getRef1());
        }

        if (ref2!=null) {
            sqa.addClause("b.ref2=?");
            sqa.addPar(ref2);
        }

        if(getStPolicyTypeID()!=null){
            sqa.addClause("b.ref3=?");
            sqa.addPar(getStPolicyTypeID());
        }

        if(getStLevelApproval()!=null){
            if(getStLevelApproval().equalsIgnoreCase("1")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("3")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }
            
            if(getStLevelApproval().equalsIgnoreCase("4")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("5")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }
        }

        sqa.addOrder("a.pol_type_id,b.period_start,ref4");

        list = sqa.getList(FlexTableView.class);


        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate();// else ft.markNew();
         }

      setEditMode(false);
      setShowButton(true);
   }

    public void select_edit_tsi() throws Exception {
       list = null;

       final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.*, a.pol_type_id, a.description");
        sqa.addQuery(" from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.fft_group_id='CAPA'");

        if (getRef1()!=null) {
            sqa.addClause("b.ref1=?");
            sqa.addPar(getRef1());
        }

        if (ref2!=null) {
            sqa.addClause("b.ref2=?");
            sqa.addPar(ref2);
        }

        if(getStPolicyTypeID()!=null){
            sqa.addClause("b.ref3=?");
            sqa.addPar(getStPolicyTypeID());
        }

        if(getStLevelApproval()!=null){
            if(getStLevelApproval().equalsIgnoreCase("1")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("3")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("4")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("5")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }
        }

        sqa.addOrder("a.pol_type_id,b.period_start,ref4");

        list = sqa.getList(FlexTableView.class);


        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("CAPA");
            ft.setStReference1(getRef1());
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID()!=null) ft.markUpdate();// else ft.markNew();
         }

      setEditMode(true);
      setShowButton(true);
      setApprovalMode(false);
   }

    public void select_approval_komisi() throws Exception {

       list = null;

       final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.*, a.pol_type_id, a.description");
        sqa.addQuery(" from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.fft_group_id='COMM'");

        if (ref2!=null) {
            sqa.addClause("b.ref2=?");
            sqa.addPar(ref2);
        }

        if(getStPolicyTypeID()!=null){
            sqa.addClause("b.ref3=?");
            sqa.addPar(getStPolicyTypeID());
        }


        if(getStLevelApproval()!=null){
            if(getStLevelApproval().equalsIgnoreCase("1")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("3")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("4")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("5")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }
        }

        sqa.addOrder("a.pol_type_id,b.period_start");

        list = sqa.getList(FlexTableView.class);


        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
         }

      setEditMode(false);
      setShowButton(true);

   }

    public void select_edit_komisi() throws Exception {

       list = null;

       final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.*, a.pol_type_id, a.description");
        sqa.addQuery(" from " +
                     "      ins_policy_types a " +
                     "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.fft_group_id='COMM'");

        if (ref2!=null) {
            sqa.addClause("b.ref2=?");
            sqa.addPar(ref2);
        }

        if(getStPolicyTypeID()!=null){
            sqa.addClause("b.ref3=?");
            sqa.addPar(getStPolicyTypeID());
        }


        if(getStLevelApproval()!=null){
            if(getStLevelApproval().equalsIgnoreCase("1")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("3")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("4")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'N'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }

            if(getStLevelApproval().equalsIgnoreCase("5")){
                sqa.addClause("coalesce(approved_flag1,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag2,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag3,'N') = 'Y'");
                sqa.addClause("coalesce(approved_flag4,'N') = 'N'");
            }
        }

        sqa.addOrder("a.pol_type_id,b.period_start");

        list = sqa.getList(FlexTableView.class);


        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("COMM");
            ft.setStReference2(ref2);
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));
            ft.setDbReference1(ft.getDbReference1());

            if (ft.getStFFTID()!=null) ft.markUpdate(); //else ft.markNew();
         }

      setEditMode(true);
      setShowButton(true);
      setApprovalMode(false);
   }

    public void selectType(){

    }

    public void saveAnggaran() throws Exception {

        for (int i = 0; i < list.size(); i++) {
            FlexTableView flexTableView = (FlexTableView) list.get(i);

            if (flexTableView.isNew()) {
                if (flexTableView.getDbReference1() != null) {
                    flexTableView.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
                } else {
                    flexTableView.markUnmodified();
                }
            }
        }

        SQLUtil.qstore(list);

        close();
    }

     /**
     * @return the stIdentifikasi
     */
    public String getStIdentifikasi() {
        return stIdentifikasi;
    }

    /**
     * @param stIdentifikasi the stIdentifikasi to set
     */
    public void setStIdentifikasi(String stIdentifikasi) {
        this.stIdentifikasi = stIdentifikasi;
    }

    /**
     * @return the stProgramKerja
     */
    public String getStProgramKerja() {
        return stProgramKerja;
    }

    /**
     * @param stProgramKerja the stProgramKerja to set
     */
    public void setStProgramKerja(String stProgramKerja) {
        this.stProgramKerja = stProgramKerja;
    }

    /**
     * @return the listRKAP
     */
    public DTOList getListRKAP() {
        return listRKAP;
    }

    /**
     * @param listRKAP the listRKAP to set
     */
    public void setListRKAP(DTOList listRKAP) {
        this.listRKAP = listRKAP;
    }

    public void view_sistemrkap() throws Exception {
        listRKAP = null;

        String query = null;
        if (stIdentifikasi.equalsIgnoreCase("14")) {
            query = " select e.id_program_kerja,e.id_sub_barang as id_rencana_anggaran,f.id_barang as id_kertas_kerja,coalesce(e.jumlah_pusat,0) as jumlah_pusat,"
                    + "(coalesce(e.jumlah_pusat,0) - coalesce(e.anggaran_terpakai,0)) as sisa_anggaran,g.no_account "
                    + "from kebijakan_perusahaan a "
                    + "join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                    + "join cabang c on (b.kode_cabang=c.kode_cabang) "
                    + "join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                    + "join sub_barang e on (d.id_program_kerja=e.id_program_kerja) "
                    + "join barang f on (e.id_barang=f.id_barang) "
                    + "join gl_chart g on (f.kode_akun=g.no_account) "
                    + "join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya) "
//                    + "where b.tahun = ? and c.kode_cabang = ? ";
//                    + "where b.tahun = ? and c.kode_cabang = ? and e.id_program_kerja = ? ";
                    + "where b.tahun = ? and c.kode_cabang = ? and h.id_jenis_biaya = ? "
                    + "order by g.no_account,f.nama_barang ";
        } else {
            query = " select e.id_program_kerja,f.id_rencana_anggaran,f.id_kertas_kerja,coalesce(f.jumlah_pusat,0) as jumlah_pusat,"
                    + "(coalesce(f.jumlah_pusat,0) - coalesce(f.anggaran_terpakai,0)) as sisa_anggaran,g.no_account "
                    + "from kebijakan_perusahaan a "
                    + "join identifikasi_masalah b on (a.id=b.id_kebijakan) "
                    + "join cabang c on (b.kode_cabang=c.kode_cabang) "
                    + "join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah) "
                    + "join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja) "
                    + "join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran) "
                    + "join gl_chart g on (f.no_account=g.no_account) "
                    + "join jenis_biaya h on (h.id_jenis_biaya=g.id_jenis_biaya) "
//                    + "where b.tahun = ? and c.kode_cabang = ? ";
//                    + "where b.tahun = ? and c.kode_cabang = ? and e.id_program_kerja = ? ";
                    + "where b.tahun = ? and c.kode_cabang = ? and h.id_jenis_biaya = ? "
                    + "order by g.no_account,e.uraian,f.rencana_kerja ";
        }

        if ((getStBranch() != null) && (listRKAP == null)) {
            listRKAP = ListUtil.getDTOListFromQueryDS(
                    query, // and d.id_identifikasi_masalah = ? ",
//                    new Object[]{ref6, stRegion}, FlexTableView.class,
//                    new Object[]{ref6, stRegion, stProgramKerja}, FlexTableView.class,
                        new Object[]{ref6, stRegion, stIdentifikasi}, FlexTableView.class,
                    "RKAPDB");

            if ((getStBranch() != null) && getStIdentifikasi() != null) {
                listRKAP = ListUtil.getDTOListFromQueryDS(
                        query,
//                        new Object[]{ref6, stRegion}, FlexTableView.class,
//                        new Object[]{ref6, stRegion, stProgramKerja}, FlexTableView.class,
                        new Object[]{ref6, stRegion, stIdentifikasi}, FlexTableView.class,
                        "RKAPDB");
                /*
                select * from rencana_anggaran a
                join kertas_kerja b on (a.id_rencana_anggaran=b.id_rencana_anggaran)
                where a.id_program_kerja=
                 */

                /*select f.* from kebijakan_perusahaan a
                join identifikasi_masalah b on (a.id=b.id_kebijakan)
                join cabang c on (b.kode_cabang=c.kode_cabang)
                join program_kerja d on (b.id_identifikasi_masalah=d.id_identifikasi_masalah)
                join rencana_anggaran e on (d.id_program_kerja=e.id_program_kerja)
                join kertas_kerja f on (e.id_rencana_anggaran=f.id_rencana_anggaran)
                join gl_chart g on (f.no_account=g.no_account)
                where b.tahun=2020 and c.kode_cabang=53 */
            }

            for (int i = 0; i < listRKAP.size(); i++) {
                FlexTableView ft = (FlexTableView) listRKAP.get(i);

                ft.setStFFTGroupID("CORERKAP");
                ft.setStReference1(String.valueOf(ft.getAttribute("no_account")));
                ft.setStReference2(String.valueOf(ft.getAttribute("id_rencana_anggaran")));
                ft.setStReference3(String.valueOf(ft.getAttribute("id_kertas_kerja")));
                ft.setDbReference1(new BigDecimal(String.valueOf(ft.getAttribute("jumlah_pusat"))));
                ft.setDbReference2(new BigDecimal(String.valueOf(ft.getAttribute("sisa_anggaran"))));
                ft.setStAddFlag(null);
                ft.setDtPeriodEnd(null);

                if (ft.getStFFTID() != null) {
                    ft.markUpdate();
                } else {
                    ft.markNew();
                }

            }
        }

        setReadOnly(true);
        setEditMode(false);
        setShowButton(false);
    }

    /**
     * @return the stBranch
     */
    public String getStBranch() {
        String cabang = null;
        if (stBranch == null) {
            cabang = "00";
        } else {
            cabang = stBranch;
        }
        return cabang;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    /**
     * @return the stRegion
     */
    public String getStRegion() {
        return stRegion;
    }

    /**
     * @param stRegion the stRegion to set
     */
    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }
    
}
 