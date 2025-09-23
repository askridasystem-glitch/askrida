/***********************************************************************
 * Module:  com.crux.initializer.ejb.trikomselDS.MenuInitializer
 * Author:  Denny Mahendra
 * Created: Sep 3, 2004 9:53:56 AM
 * Purpose:
 ***********************************************************************/

package com.crux.initializer.ejb.wds;

import com.crux.common.parameter.Parameter;
import com.crux.util.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuInitializer extends SchemaInitializer {
    
    public MenuInitializer(String stDS) {
        super(stDS);
    }
    
    public void doInitialize() throws Exception {
        final boolean autoUpdate = Parameter.readBoolean(ds,"SYS_MENU_AUTO_UPDATE",1,false);
        
        if (autoUpdate) {
            //runScriptFile("com/crux/initializer/ejb/wds/menuscript.sql");
            
            //final BufferedReader rdr = new BufferedReader(new StringReader(scripts));
            
            //if (countMenu()==scripts.length-1) return;
            
            for (int i = 0; i < scripts.length; i++) {
                final String s = scripts[i];
                if (s.trim().length()>0)
                    execQuery(s);
            }
        }
    }
    
    private int countMenu() throws Exception {
        final SQLUtil S = new SQLUtil(ds);
        
        try {
            final PreparedStatement ps = S.setQuery("select count(*) from s_functions");
            
            final ResultSet RS = ps.executeQuery();
            
            RS.next();
            
            final int cnt = RS.getInt(1);
            
            return cnt;
        } finally {
            S.release();
        }
    }
    
    public final static transient String [] scripts = {
        "delete from s_functions",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.00.00.00.00.00','USER',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.10.00.00.00.00','{L-ENGProfile-L}{L-INAProfil-L}','userlist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.10.01.00.00.00','Create',NULL,'USER_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.10.02.00.00.00','Edit',NULL,'USER_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.10.03.00.00.00','Delete',NULL,'USER_DELETE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.10.04.00.00.00','Set Password',NULL,'USER_PASSWORD')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.15.00.00.00.00','Role','role_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('01.20.00.00.00.00','Sessions','LIST_SESSIONS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.00.00.00.00.00','{L-ENGCAPACITY-L}{L-INAKAPASITAS-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.00.00.00.00','{L-ENGApproval Limits-L}{L-INALimit Persetujuan-L}','capacity_edit.approval.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.01.00.00.00','Edit Limit TSI',NULL,'CAPA_APPROVAL_TSI_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.02.00.00.00','Edit Limit KIaim',NULL,'CAPA_APPROVAL_CLAIM_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.03.00.00.00','Approve Flag 1',NULL,'CAPA_TSI_APPROVE1');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.04.00.00.00','Approve Flag 2',NULL,'CAPA_TSI_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.05.00.00.00','Approve Flag 3',NULL,'CAPA_TSI_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.06.00.00.00','Approve Flag 4',NULL,'CAPA_TSI_APPROVE4');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.00.00.00.00','{L-ENGComission Limits-L}{L-INALimit Komisi-L}','capacity_edit.commission.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.01.00.00.00','Edit',NULL,'CAPA_COMM_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.02.00.00.00','Create',NULL,'CAPA_COMM_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.03.00.00.00','Approve Flag 1',NULL,'CAPA_COMM_APPROVE1');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.04.00.00.00','Approve Flag 2',NULL,'CAPA_COMM_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.05.00.00.00','Approve Flag 3',NULL,'CAPA_COMM_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.06.00.00.00','Approve Flag 4',NULL,'CAPA_COMM_APPROVE4');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.30.00.00.00.00','{L-ENGComission Limit Company Group-L}{L-INALimit Komisi Group Perusahaan-L}','capacity_edit.commissiongroup.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.30.01.00.00.00','Edit',NULL,'CAPA_COMM_GROUP_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.40.00.00.00.00','{L-ENGComission Setting-L}{L-INAPembagian Komisi-L}','capacity_edit.commissionsetting.crux',NULL);",
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.00.00.00.00.00','Customer Management',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.00.00.00.00','{L-ENGCustomer Management-L}{L-INAData Customer-L}','entity_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.10.00.00.00','Navigate Branch',NULL,'ENT_MASTER_NAV_BR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.20.00.00.00','Jenis Perusahaan Lain',NULL,'SELECT_COMP_TYPE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.11.00.00.00.00','{L-ENGCompany Group-L}{L-INAGrup Perusahaan-L}','company_group_list.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.11.00.00.00.00','{L-ENGBusiness Table-L}{L-INATabel Bisnis-L}','master_bussrc.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.12.00.00.00.00','{L-ENGMarketer Table-L}{L-INATabel Pemasar-L}','master_marktr.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.00.00.00.00.00','Working Timeline',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.10.00.00.00.00','{L-ENGWorking Timeline-L}{L-INAWorking Timeline-L}','register_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.10.01.00.00.00','Navigate Branch',NULL,'REGISTER_NAVBR');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.00.00.00.00.00','Marketing',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.00.00.00.00','{L-ENGQuotation-L}{L-INAPenawaran-L}','policy_list.proposal.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.01.00.00.00','Navigate Branch',NULL,'POL_PROP_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.02.00.00.00','Edit',NULL,'POL_PROP_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.03.00.00.00','Create',NULL,'POL_PROP_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.04.00.00.00','Approval',NULL,'POL_PROP_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.05.00.00.00','Print',NULL,'POL_PROP_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.06.00.00.00','Navigate Region',NULL,'POL_PROP_NAVREG');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.11.00.00.00.00','{L-ENGParent Policy-L}{L-INAPolis Induk-L}','policy_parent_list.parent.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.12.00.00.00.00','{L-ENGPerjanjian Kerjasama-L}{L-INAPerjanjian Kerjasama-L}','pks_list.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.20.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=marketing');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.21.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.21.01.00.00.00','{L-ENGProduction Report of PKS-L}{L-INALaporan Produksi PKS-L}',NULL,'/prod_pks.go.crux?rpt=pks');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.00.00.00.00.00','Underwriting',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.00.00.00.00','Underwriting','policy_list.underwrit.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.01.00.00.00','Navigate Branch',NULL,'POL_UWRIT_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.02.00.00.00','Edit',NULL,'POL_UWRIT_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.03.00.00.00','Create',NULL,'POL_UWRIT_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.04.00.00.00','Approval',NULL,'POL_UWRIT_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.05.00.00.00','Print',NULL,'POL_UWRIT_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.06.00.00.00','Create Policy History',NULL,'POL_UWRIT_CREATE_HISTORY');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.07.00.00.00','Approve U/W',NULL,'POL_UWRIT_APR_UW');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.08.00.00.00','Input Payment Date',NULL,'POL_UWRIT_PAYMENT_DATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.09.00.00.00','Bonding Division',NULL,'BONDING_DIVISION');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.10.00.00.00','Reverse Data',NULL,'CAN_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.11.00.00.00','Input No Polis Manual',NULL,'CAN_INPUT_MANUAL_POL');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.12.00.00.00','Input Polis Sementara',NULL,'CAN_INPUT_TEMP_POL');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.13.00.00.00','Setujui Oleh Direksi',NULL,'APPROVE_BY_DIRECTOR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.14.00.00.00','Swap Premi Koas > Premi ABA',NULL,'SWAP_COAS_PREMIUM');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.15.00.00.00','Navigate Region',NULL,'POL_UWRIT_NAVREG');",
        
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.00.00.00.00','Endorsement',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.10.00.00.00','Conditions No Change',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.15.00.00.00','Conditions Change',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.20.00.00.00','Increase',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.25.00.00.00','Decrease',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.30.00.00.00','Object Addition',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.35.00.00.00','Object Deletion',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.40.00.00.00','Cancellation',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.45.00.00.00','Internal Cancellation',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.20.00.00.00.00','Risk Assessment',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,url,resource_id) VALUES('20.20.10.00.00.00','Assessment','/pages/riskassessment/assessment.htm',NULL);",
//         "INSERT INTO s_functions(function_id,function_name,url,resource_id) VALUES('20.20.15.00.00.00','Control','/pages/riskassessment/control.htm',NULL);",
//         "INSERT INTO s_functions(function_id,function_name,url,resource_id) VALUES('20.20.20.00.00.00','Survey','/pages/riskassessment/survey.htm',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.25.00.00.00.00','Reporting','prod_report.uwrit.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.30.00.00.00.00','Setup','CONFIGURE&GROUP=UWRIT',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.40.00.00.00.00','Treaty','treaty_list.list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.50.00.00.00.00','Block Risk','zone_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.60.00.00.00.00','Print Kwitansi','policy_rcp.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.00.00.00.00','Risk Category','risk_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.71.00.00.00.00','{L-ENGClausules-L}{L-INAKlausula-L}','clausules_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.90.00.00.00.00','Surat Pengantar','policy_ltr.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.91.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=marketing');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing');",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.50.00.00.00.00','Nomerator',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.60.00.00.00.00','Production Target',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.00.00.00.00','Rates',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.10.00.00.00','Basic',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.20.00.00.00','Sipanda','master_rates.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.30.00.00.00','PA kreasi','master_rates.crux&cls=PAKREASI',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.00.00.00.00','Cover Note','cover_note_list.titipan.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.01.00.00.00','Navigate Branch',NULL,'POL_COVERNOTE_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.02.00.00.00','Edit',NULL,'POL_COVERNOTE_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.03.00.00.00','Create',NULL,'POL_COVERNOTE_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.04.00.00.00','Approval',NULL,'POL_COVERNOTE_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.05.00.00.00','Print',NULL,'POL_COVERNOTE_PRINT');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.92.00.00.00.00','Upload File','policy_upload.crux',NULL);",
        
        
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.25.10.00.00.00','Print Policy',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.25.15.00.00.00','Print Endorsement',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.25.20.00.00.00','Print Policy Note',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.00.00.00.00.00','{L-ENGReinsurance-L}{L-INAReasuransi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.00.00.00.00','{L-ENGReinsurance-L}{L-INAReasuransi-L} / Outward','policy_list.reas.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.01.00.00.00','Navigate Branch',NULL,'POL_RI_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.02.00.00.00','Edit',NULL,'POL_RI_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.03.00.00.00','Create',NULL,'POL_RI_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.04.00.00.00','Approval',NULL,'POL_RI_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.05.00.00.00','Print',NULL,'POL_RI_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.06.00.00.00','Manual Treaty',NULL,'MANUAL_TREATY');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.07.00.00.00','Navigate Region',NULL,'POL_RI_NAVREG');",
                "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.08.00.00.00','Setujui Oleh Direksi',NULL,'APPROVE_BY_DIRECTOR_RI');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.20.00.00.00.00','Inward',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=reinsurance');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_reinsurance_report.go.crux?rpt=reinsurance');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.31.00.00.00.00','{L-ENGInward Report-L}{L-INALaporan Inward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.31.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_inward.go.crux?rpt=inward');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.00.00.00.00.00','{L-ENGClaim-L}{L-INAKlaim-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.00.00.00.00','{L-ENGClaim-L}{L-INAKlaim-L}','policy_list.claim.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.01.00.00.00','Navigate Branch',NULL,'POL_CLAIM_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.02.00.00.00','Edit',NULL,'POL_CLAIM_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.03.00.00.00','Create',NULL,'POL_CLAIM_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.04.00.00.00','Approval',NULL,'POL_CLAIM_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.05.00.00.00','Print',NULL,'POL_CLAIM_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.08.00.00.00','Navigate Region',NULL,'POL_CLAIM_NAVREG');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.20.00.00.00.00','{L-ENGInward-L}{L-INAInward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=claim');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_claim_report.go.crux?rpt=claim');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.30.02.00.00.00','Pengajuan Klaim','policy_clm.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.31.00.00.00.00','{L-ENGInward Report-L}{L-INALaporan Inward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.31.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_inward.go.crux?rpt=inwardclm');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.40.00.00.00.00','Setup','CONFIGURE&GROUP=CLAIM',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.06.00.00.00','Edit No Surat Dll',NULL,'POL_CLAIM_EDIT2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.07.00.00.00','Setujui Oleh Direksi',NULL,'CLAIM_APPROVE_BY_DIRECTOR');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.00.00.00.00.00','Data Searching',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.10.00.00.00.00','PA Kreasi','policy_form.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.10.01.00.00.00','Navigate Branch',NULL,'KREASI_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.20.00.00.00.00','Kendaraan Bermotor','vehicle_search.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.30.00.00.00.00','Flexible Search','policy_search.crux' ,NULL);",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.00.00.00.00','Settlement','receiptlist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.00.00.00.00','{L-ENGFinance-L}{L-INAKeuangan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.05.00.00.00','Rights',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.10.00.00','Create',NULL,'SETTLR_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.20.00.00','Edit',NULL,'SETTLR_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.30.00.00','Approve',NULL,'SETTLR_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.40.00.00','Navigate Branch',NULL,'SETTLR_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.50.00.00','Void',NULL,'SETTLR_VOID');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.05.00.00.00.00','Pembayaran',NULL,NULL);",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('34.00.00.00.00.00','{L-ENGPayment Monitoring-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.06.00.00.00.00','{L-ENGPayment Monitoring-L}{L-INAMonitoring Pembayaran-L}','policy_data.crux' ,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.00.00.00.00','Deposito',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.10.00.00.00','Pembentukan Deposito','pembentukan_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.10.10.00.00','Navigate Branch',NULL,'DEPO_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.10.20.00.00','Approval',NULL,'DEPO_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.20.00.00.00','Pencairan Deposito','pencairan_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.20.10.00.00','Navigate Branch',NULL,'CAIR_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.20.20.00.00','Approval',NULL,'CAIR_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.30.00.00.00','Bunga Deposito','bunga_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.30.10.00.00','Navigate Branch',NULL,'BUNGA_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.30.20.00.00','Approval',NULL,'BUNGA_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.08.10.00.00.00','{L-ENGDeposito Report-L}{L-INALaporan Deposito-L}',NULL,'/prod_deposito.go.crux?rpt=deposito');",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.00.00.00.00','{L-ENGFinance-L}{L-INAKeuangan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.20.00.00.00.00','{L-ENGTransaction-L}{L-INATransaksi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.26.00.00.00.00','Surat Hutang','surathutanglist.crux&trx=50','/main.ctl?EVENT=surathutanglist.crux&trx=50');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.27.00.00.00.00','Titipan Premi','titipanlist.crux','/main.ctl?EVENT=titipanlist.crux');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('35.27.00.00.00.00','{L-ENGTitipan Premi-L}{L-INATitipan Premi-L}','titipan_premi_list.crux&glid=6','/main.ctl?EVENT=titipan_premi_list.crux&glid=6',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.20.99.00.00.00','Navigate Branch',NULL,'ARINVOICE_NAVBR');",
        
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.25.00.00.00.00','Setup','CONFIGURE&GROUP=AR_AP',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.00.00.00.00','{L-ENGReporting-L}{L-INALaporan-L}',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('36.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('36.30.01.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan-L}',NULL,'/prod_report.go.crux?rpt=finance');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('36.30.01.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan-L}',NULL,'/prod_finance_report.go.crux?rpt=finance');",
        
         /*"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.10.00.00.00','Cash Position',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.15.00.00.00','Bank Position',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.20.00.00.00','Mutation',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.25.00.00.00','Journal',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.30.00.00.00','Neraca',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.35.00.00.00','Loss / Profit',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.40.00.00.00','Income Statement',NULL,NULL);",*/
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.40.00.00.00','Outstanding Invoices','rptOutstandingARAP.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.44.00.00.00','{L-ENGAR/AP Detail-L}{L-INADetil Hutang Piutang-L}','rptArAPDetail.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.45.00.00.00','{L-ENGR/I Payable Summary-L}{L-INARekapitulasi Hutang Reasuransi-L}','arreport.crux&mode=AP',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.50.00.00.00','{L-ENGR/I Receivable Summary-L}{L-INARekapitulasi Piutang Reasuransi-L}','arreport.crux&mode=AR',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.55.00.00.00','{L-ENGTechnical Account Statement-L}{L-INATechnical Account Statement-L}','rptSOAReinsurance.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.55.00.00.00','R/I Receivable Summary','arreport.crux&mode=AR',NULL);",
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.00.00.00.00.00','{L-ENGAccounting-L}{L-INAAkuntansi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.00.00.00.00','{L-ENGJournal Entry-L}{L-INAJurnal Entry-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.10.00.00.00','{L-ENGGeneral Journal-L}{L-INAJurnal Umum-L}','gl_list.crux&glid=1','/main.ctl?EVENT=gl_list.crux&glid=1',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.15.00.00.00','{L-ENGBank Cash Journal-L}{L-INAJurnal Kas Bank-L}','cashbankGL_list.crux&glid=2','/main.ctl?EVENT=gl_list.crux&glid=2',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.20.00.00.00','{L-ENGClaim Advanced Payment-L}{L-INAUang Muka Klaim-L}','free_gl_list.crux&glid=3','/main.ctl?EVENT=free_gl_list.crux&glid=3',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.21.00.00.00','{L-ENGPremium Advanced Payment-L}{L-INAUang Muka Premi-L}','premi_gl_list.crux&glid=4','/main.ctl?EVENT=premi_gl_list.crux&glid=4',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.22.00.00.00','{L-ENGCommission Payment-L}{L-INAUang Muka Komisi-L}','comission_gl_list.crux&glid=5','/main.ctl?EVENT=comission_gl_list.crux&glid=5',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.23.00.00.00','{L-ENGBusiness Development Journal-L}{L-INAJurnal Pembinaan Bisnis-L}','pb_gl_list.crux&glid=6','/main.ctl?EVENT=pb_gl_list.crux&glid=6',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.24.00.00.00','{L-ENGSyariah Journal-L}{L-INAJurnal Syariah-L}','syariah_gl_list.crux&glid=7','/main.ctl?EVENT=syariah_gl_list.crux&glid=7',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.25.00.00.00','Evaluasi Pelaksanaan RKAP','pelaksanaan_list.crux',NULL,NULL);",
        // "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.23.00.00.00','{L-ENGTitipan Premi-L}{L-INATitipan Premi-L}','titipan_premi_list.crux&glid=6','/main.ctl?EVENT=titipan_premi_list.crux&glid=6',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.20.00.00.00','{L-ENGMemorial Journal-L}{L-INAJurnal Memorial-L}','gl_list.crux&glid=3','/main.ctl?EVENT=gl_list.crux&glid=3',NULL);",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.12.00.00.00.00','Report Setup','GL_REPORT',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.14.00.00.00.00','Print Report',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.17.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('40.17.01.00.00.00','{L-ENGAccounting Report-L}{L-INALaporan Akuntansi-L}',NULL,'/prod_account.go.crux?rpt=accounting');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.16.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.00.00.00.00','Setup',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.10.00.00.00','Account Type','GL_ACCT_TYPE',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.15.00.00.00','Account Mask','CONFIGURE&GROUP=GL_ACCT_MASK',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.20.00.00.00','Fiscal Year','CONFIGURE&GROUP=GL_FISCAL_YR',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.25.00.00.00','Journal Master','GL_J_MASTER',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.30.00.00.00','Cost Center','GL_DEPT_LIST',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.35.00.00.00','Periods','GL_PERIODS',NULL);",
        // "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.40.00.00.00','Accounts','GL_ACCOUNTS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.40.00.00.00','Accounts','account_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.00.00.00','Currency','currency_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.10','Setup','CONFIGURE&GROUP=GL_CCY',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.15','Exchange Rate',NULL,NULL);",
        
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.00.00.00.00.00','Utilities',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.10.00.00.00.00','Schedule of Policy',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.15.00.00.00.00','{L-ENGInsurance Tools-L}{L-INAInsurance Tools-L}','printpolicywording.crux',NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.20.00.00.00.00','Clauses',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.20.00.00.00.00','{L-ENGNews Ticker-L}{L-INANews Ticker-L}','CONFIGURE&GROUP=NEWS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.21.00.00.00.00','{L-ENGAnnouncement-L}{L-INAPengumuman-L}','announcement.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.25.00.00.00.00','Data Export',NULL,'/prod_report.go.crux?rpt=utilities');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.25.00.00.00.00','Data Export',NULL,'/prod_utilities_report.go.crux?rpt=utilities');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.30.00.00.00.00','{L-ENGGuidance-L}{L-INAPanduan-L}','utilities.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.35.00.00.00.00','Transfer Data',NULL,'/prod_report.go.crux?rpt=transfer');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.35.00.00.00.00','Transfer Data',NULL,'/prod_utilities_report.go.crux?rpt=transfer');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.40.00.00.00.00','Fix Data','fix_data.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.45.00.00.00.00','{L-ENGArchive-L}{L-INAArsip-L}','archivelist.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.00.00.00.00.00','Maintenance',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.00.00.00.00','Documents',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.01.00.00.00','Super Edit',NULL,'POL_SUPER_EDIT');",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('94.00.00.00.00.00','Marketing',NULL,NULL);",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('94.10.00.00.00.00','{L-ENGQuotation-L}{L-INAPenawaran-L}','policy_mandiri_list.proposal.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.00.00.00.00.00','SYSTEM SETTINGS',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.01.00.00.00.00','Logging','CONFIGURE&GROUP=LOG',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.02.00.00.00.00','System','CONFIGURE&GROUP=SYS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.03.00.00.00.00','General','CONFIGURE&GROUP=GENERAL',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.05.00.00.00.00','System Monitor','sysmon.crux',NULL)",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.06.00.00.00.00','SQI','sqi.crux',NULL)",
         /*"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.10.00.00.00.00','Notification',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.20.01.00.00.00','SMS','CONFIGURE&GROUP=NOT_SMS',NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.20.02.00.00.00','Email','CONFIGURE&GROUP=NOT_EMAIL',NULL);",*/
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('95.30.00.00.00.00','Clear Pool','CLR_POOL',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('96.00.00.00.00.00','TASK SCHEDULER',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('96.01.00.00.00.00','Config','CONFIG_JOB',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('96.02.00.00.00.00','Log','JOB_LOG_VIEW',NULL);",
        
        //tambahan AMS
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.00.00.00.00.00','Address Management',NULL,NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.10.00.00.00.00','{L-ENGData Distribution-L}{L-INADistribusi Data-L}','distribution_list.crux',NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.10.01.00.00.00','Create',NULL,'DISTRIBUTION_CREATE')",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.10.02.00.00.00','Edit',NULL,'DISTRIBUTIONG_EDIT')",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.10.03.00.00.00','Delete',NULL,'DISTRIBUTION_DELETE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('97.20.00.00.00.00','{L-ENGContacts-L}{L-INAKontak Alamat-L}','contact_list.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.00.00.00.00.00','{L-ENGMail-L}{L-INASurat-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.10.00.00.00.00','{L-ENGInbox-L}{L-INASurat Masuk-L}','incominglist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.10.01.00.00.00','Create',NULL,'INCOMING_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.10.02.00.00.00','Edit',NULL,'INCOMING_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.10.03.00.00.00','Delete',NULL,'INCOMING_DELETE')",
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.20.00.00.00.00','{L-ENGOutbox-L}{L-INASurat Keluar-L}','outcominglist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.20.01.00.00.00','Create',NULL,'OUTCOMING_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.20.02.00.00.00','Edit',NULL,'OUTCOMING_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('98.20.03.00.00.00','Delete',NULL,'OUTCOMING_DELETE')",
       
        
    };
}

/*
 
 
 
 
 
 
 
 */