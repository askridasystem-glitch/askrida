/***********************************************************************
 * Module:  com.crux.initializer.ejb.trikomselDS.MenuInitializer
 * Author:  Denny Mahendra
 * Created: Sep 3, 2004 9:53:56 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.menu;

import com.crux.common.parameter.Parameter;
import com.crux.initializer.ejb.wds.SchemaInitializer;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuInitializer extends SchemaInitializer {
    
private final static transient LogManager logger = LogManager.getInstance(MenuInitializer.class);

    public MenuInitializer(String stDS) { 
        super(stDS);
    }
    
    public void doInitialize() throws Exception {
        final boolean autoUpdate = Parameter.readBoolean(ds,"SYS_MENU_AUTO_UPDATE",1,false);
        
        if (autoUpdate) {
            //logger.logDebug("+++++++++++++ JALANIN MENU SQL ++++++++++++++");
            
            //setScript("com/webfin/menu/menu.sql");
            
            //runScriptFile("menu.sql");
            
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
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.03.00.00.00','Setujui Operator',NULL,'CAPA_TSI_APPROVE1');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.04.00.00.00','Setujui Kasie U/W',NULL,'CAPA_TSI_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.05.00.00.00','Setujui Kabag U/W',NULL,'CAPA_TSI_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.06.00.00.00','Setujui Kadiv U/W',NULL,'CAPA_TSI_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.07.00.00.00','Aktivasi Limit TSI',NULL,'CAPA_TSI_ACTIVATE');",

                "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.08.00.00.00','Setujui Operator Klaim',NULL,'CAPA_CLAIM_APPROVE1');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.04.00.00.00','Setujui Kasie U/W',NULL,'CAPA_TSI_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.10.00.00.00','Setujui Kabag Klaim',NULL,'CAPA_CLAIM_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.11.00.00.00','Setujui Kadiv Klaim',NULL,'CAPA_CLAIM_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.10.12.00.00.00','Aktivasi Limit Klaim',NULL,'CAPA_CLAIM_ACTIVATE');",
 

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.00.00.00.00','{L-ENGComission Limits-L}{L-INALimit Komisi-L}','capacity_edit.commission.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.01.00.00.00','Edit',NULL,'CAPA_COMM_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.02.00.00.00','Create',NULL,'CAPA_COMM_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.03.00.00.00','Setujui Operator',NULL,'CAPA_COMM_APPROVE1');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.04.00.00.00','Setujui Kasie U/W',NULL,'CAPA_COMM_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.05.00.00.00','Setujui Kabag U/W',NULL,'CAPA_COMM_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.06.00.00.00','Setujui Kadiv U/W',NULL,'CAPA_COMM_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.20.07.00.00.00','Aktivasi Limit Komisi',NULL,'CAPA_COMM_ACTIVATE');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.30.00.00.00.00','{L-ENGComission Limit Company Group-L}{L-INALimit Komisi Group Perusahaan-L}','capacity_edit.commissiongroup.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.30.01.00.00.00','Edit',NULL,'CAPA_COMM_GROUP_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.40.00.00.00.00','{L-ENGComission Setting Per Item-L}{L-INALimit Komisi Per Item-L}','capacity_edit.commissionsetting.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.50.00.00.00.00','{L-ENGApproval Limits-L}{L-INALimit Persetujuan-L} Kredit Konsumtif','capacity_konsumtif_edit.approvalkonsumtif.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.55.00.00.00.00','{L-ENGRate-L}{L-INARate-L} Kredit Konsumtif','rate_konsumtif_edit.ratekonsumtif.crux',NULL);",
                "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('02.60.00.00.00.00','Warranty','warranty_edit.warranty.crux',NULL);",
        
         
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.00.00.00.00.00','Customer Management',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.00.00.00.00','{L-ENGCustomer Management-L}{L-INAData Customer-L}','entity_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.10.00.00.00','Navigate Branch',NULL,'ENT_MASTER_NAV_BR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.20.00.00.00','Jenis Perusahaan Lain',NULL,'SELECT_COMP_TYPE');", 
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.10.30.00.00.00','Pusat Non AKS  Input Customer',NULL,'HO_NAKS_CREATE_ALL');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.11.00.00.00.00','{L-ENGCompany Group-L}{L-INAGrup Perusahaan-L}','company_group_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.12.00.00.00.00','{L-ENGAgent Management-L}{L-INAData Agen-L}','agen_group_list.crux',NULL);",

        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.11.00.00.00.00','{L-ENGBusiness Table-L}{L-INATabel Bisnis-L}','master_bussrc.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('10.12.00.00.00.00','{L-ENGMarketer Table-L}{L-INATabel Pemasar-L}','master_marktr.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.00.00.00.00.00','Working Timeline',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.10.00.00.00.00','{L-ENGWorking Timeline-L}{L-INAWorking Timeline-L}','register_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.10.01.00.00.00','Navigate Branch',NULL,'REGISTER_NAVBR');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.20.00.00.00.00','Upload Persetujuan Direksi','upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('14.20.01.00.00.00','Direksi',NULL,'CAN_BOD');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.00.00.00.00.00','Marketing',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.00.00.00.00','{L-ENGQuotation-L}{L-INAPenawaran-L}','policy_list.proposal.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.01.00.00.00','Navigate Branch',NULL,'POL_PROP_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.02.00.00.00','Edit',NULL,'POL_PROP_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.03.00.00.00','Create',NULL,'POL_PROP_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.04.00.00.00','Approval',NULL,'POL_PROP_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.05.00.00.00','Print',NULL,'POL_PROP_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.06.00.00.00','Navigate Region',NULL,'POL_PROP_NAVREG');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.07.00.00.00','Approval Analisa Resiko',NULL,'POL_PROP_APRV_RISK_ANALYSIS');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.08.00.00.00','Validasi Analisa Cabang',NULL,'POL_PROP_APRV_RISK_BRANCH');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.09.00.00.00','Validasi Analisa Cab. Induk',NULL,'POL_PROP_APRV_RISK_INDUK');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.10.10.00.00.00','Validasi Analisa Kantor Pusat',NULL,'POL_PROP_APRV_RISK_HO');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.11.00.00.00.00','{L-ENGParent Policy-L}{L-INAPolis Induk-L}','policy_parent_list.parent.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.12.00.00.00.00','{L-ENGPerjanjian Kerjasama-L}{L-INAPerjanjian Kerjasama-L}','pks_list.crux',NULL);",

                "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.00.00.00.00','Pengajuan Akseptasi','acceptance_list.acceptance.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.01.00.00.00','Navigate Branch',NULL,'POL_ACC_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.02.00.00.00','Edit',NULL,'POL_ACC_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.03.00.00.00','Create',NULL,'POL_ACC_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.04.00.00.00','Approval',NULL,'POL_ACC_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.05.00.00.00','Print',NULL,'POL_ACC_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.13.06.00.00.00','Navigate Region',NULL,'POL_ACC_NAVREG');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.20.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.02.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi Cabang-L}',NULL,'/prod_marketing_report.go.crux?rpt=semarang_uw');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.03.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing_rekap');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.04.00.00.00','{L-ENGReport-L}{L-INALaporan-L} Per Kategori Debitur',NULL,'/prod_marketing_report.go.crux?rpt=marketing_detail');",
       


        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.02.00.00.00','{L-ENGManagement Report-L}{L-INALaporan Manajemen-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.20.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=marketing');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.21.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.21.01.00.00.00','{L-ENGProduction Report of PKS-L}{L-INALaporan Produksi PKS-L}',NULL,'/prod_pks.go.crux?rpt=pks');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.21.02.00.00.00','{L-ENGOperational Cost-L}{L-INABiaya Operasional-L}',NULL,'/prod_biaop.go.crux?rpt=biaop');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.22.00.00.00.00','Laporan Proyeksi Produksi',NULL,'/proyeksi_prod.go.crux?rpt=proyeksi');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.23.00.00.00.00','Laporan Pefindo',NULL,'/pefindo_report.go.crux?rpt=pefindo');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('15.24.00.00.00.00','Laporan Analisa Pengawasan',NULL,'/gl_early_warning.go.crux?rpt=analisis');",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.25.00.00.00.00','Cluster Produksi','clusterProduksi_form.crux',NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('15.26.00.00.00.00','Renewal Notice','policy_listrenewal.crux' ,NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('16.10.00.00.00.00','Polis Split','policy_split_list.proposal.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('16.10.01.00.00.00','Navigate Branch',NULL,'POL_SPLIT_PROP_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('16.10.02.00.00.00','Edit',NULL,'POL_SPLIT_PROP_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('16.10.03.00.00.00','Create',NULL,'POL_SPLIT_PROP_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('16.10.04.00.00.00','Approval',NULL,'POL_SPLIT_PROP_APRV');",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.49.00.00.00.00','Letter Of Discharge','letterOfDischarge_form.crux',NULL);",

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
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.16.00.00.00','Cetak Ulang Polis',NULL,'POL_DELETE_SIGNCODE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.17.00.00.00','Print Pre-Sign',NULL,'POL_UWRIT_PRINT_PRESIGN');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.18.00.00.00','Print Digitized Sign',NULL,'POL_UWRIT_PRINT_DIGITIZED');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.19.00.00.00','Free Print Digitized Sign',NULL,'POL_UWRIT_PRINT_DIGITIZED_FREE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.20.00.00.00','Persetujuan Pusat',NULL,'POL_UWRIT_HEADOFFICE_APPROVAL');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.21.00.00.00','Print Polis Digital',NULL,'POL_UWRIT_PRINT_DIGITAL_POLICY');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.22.00.00.00','Setujui Oleh Divisi',NULL,'APPROVE_BY_DIVISI');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.10.23.00.00.00','Renewal Non AKS',NULL,'POL_UWRIT_RENEWAL_NON_AKS');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.15.00.00.00.00','<b><font style=color:red;>Underwriting (Lite)</font></b>','policy_lite_list.underwritlite.crux',NULL);",
        
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
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.40.00.00.00.00','Treaty','treaty_list.list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.50.00.00.00.00','Block Risk','zone_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.60.00.00.00.00','Print Kwitansi','policy_rcp.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.61.00.00.00.00','Ikhtisar Polis','policy_listfop.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.00.00.00.00','Risk Category','risk_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.71.00.00.00.00','{L-ENGClausules-L}{L-INAKlausula-L}','clausules_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('20.72.00.00.00.00','{L-ENG General Journal Underwriting Rupa-Rupa -L}{L-INA Daftar Mutasi Underwriting Rupa-Rupa -L}','gl_list_uw.crux&glid=8','/main.ctl?EVENT=gl_list_uw.crux&glid=8',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.90.00.00.00.00','Surat Pengantar','policy_ltr.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.91.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=marketing');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.02.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi Cabang-L}',NULL,'/prod_marketing_report.go.crux?rpt=semarang_uw');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.03.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing_direksi');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.04.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing_rekap');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('20.91.05.00.00.00','Laporan Produksi Statistik',NULL,'/prod_statistik_report.go.crux?rpt=statistik');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.92.00.00.00.00','Upload Endorsemen Koas','endorse_upload_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.93.00.00.00.00','Notifikasi Approval','approval_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.93.01.00.00.00','Create',NULL,'APPROVAL_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.93.02.00.00.00','Edit',NULL,'APPROVAL_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.93.03.00.00.00','Delete',NULL,'APPROVAL_DELETE')",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.94.00.00.00.00','Kalkulator Premi','kalkulator_premi_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.95.00.00.00.00','Upload Endorsemen Restitusi','endorsepolis_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.95.01.00.00.00','Navigate Branch',NULL,'POL_UPL_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.95.02.00.00.00','Edit',NULL,'POL_UPL_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.95.03.00.00.00','Create',NULL,'POL_UPL_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.95.04.00.00.00','Approval',NULL,'POL_UPL_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.96.00.00.00.00','{L-ENGClosing-L}{L-INAClosing Produksi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.96.01.00.00.00','Closing','ins_closingprod_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.97.00.00.00.00','Upload Dokumen','policy_document_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.97.01.00.00.00','Navigate Branch',NULL,'POL_DOC_UPL_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.97.02.00.00.00','Edit',NULL,'POL_DOC_UPL_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.97.03.00.00.00','Create',NULL,'POL_DOC_UPL_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.97.04.00.00.00','Approval',NULL,'POL_DOC_UPL_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.00.00.00.00','Upload Approval','policy_approval_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.01.00.00.00','Navigate Branch',NULL,'POL_APRV_UPL_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.02.00.00.00','Edit',NULL,'POL_APRV_UPL_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.03.00.00.00','Create',NULL,'POL_APRV_UPL_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.04.00.00.00','Approval',NULL,'POL_APRV_UPL_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.98.05.00.00.00','Proses',NULL,'POL_PROCESS_UPL_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.99.00.00.00.00','Upload Endorsemen Polis','endorsefire_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.99.01.00.00.00','Navigate Branch',NULL,'POL_UPLFIRE_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.99.02.00.00.00','Edit',NULL,'POL_UPLFIRE_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.99.03.00.00.00','Create',NULL,'POL_UPLFIRE_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.99.04.00.00.00','Approval',NULL,'POL_UPLFIRE_APRV');",

        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.50.00.00.00.00','Nomerator',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.60.00.00.00.00','Production Target',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.00.00.00.00','Rates',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.10.00.00.00','Basic',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.20.00.00.00','Sipanda','master_rates.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.70.30.00.00.00','PA kreasi','master_rates.crux&cls=PAKREASI',NULL);",
        /*
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.00.00.00.00','Cover Note','cover_note_list.titipan.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.01.00.00.00','Navigate Branch',NULL,'POL_COVERNOTE_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.02.00.00.00','Edit',NULL,'POL_COVERNOTE_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.03.00.00.00','Create',NULL,'POL_COVERNOTE_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.04.00.00.00','Approval',NULL,'POL_COVERNOTE_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.80.05.00.00.00','Print',NULL,'POL_COVERNOTE_PRINT');",
        */
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('20.92.00.00.00.00','Upload File','policy_upload.crux',NULL);",
        
        
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
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.09.00.00.00','Reverse (R/I)',NULL,'REVERSE_RI');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.10.00.00.00','Setujui (R/I)',NULL,'APPROVE_RI');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.11.00.00.00','Setujui Reas',NULL,'APPROVE_RI_ONLY');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.10.12.00.00.00','Proses Update Kurs',NULL,'CURRENCY_RI_INWARD_PROCESS');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.15.00.00.00.00','{L-ENGReinsurance-L}{L-INAReasuransi-L} / Inward','policy_list.reasInward.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.20.00.00.00.00','Inward',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.21.00.00.00.00','Outward',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=reinsurance');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_reinsurance_report.go.crux?rpt=reinsurance');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.02.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Departemen Keuangan-L}',NULL,'/prod_reinsurance_report.go.crux?rpt=reinsurance_depkeu');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.30.03.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_reinsurance_report.go.crux?rpt=reinsurance_direksi');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.31.00.00.00.00','{L-ENGInward Report-L}{L-INALaporan Inward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('25.31.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_inward.go.crux?rpt=inward');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.00.00.00.00','{L-ENGClosing Reasuransi-L}{L-INAClosing Reasuransi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.01.00.00.00','Closing','ins_closing_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.02.00.00.00','Reverse',NULL,'INS_CLOSING_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.03.00.00.00','Approval Closing',NULL,'POL_RI_CLOSING_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.04.00.00.00','Closing Report','ins_closingreport_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.32.06.00.00.00','Statement Of Account','ins_soa_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.33.00.00.00.00','Upload Spreading RI','reas_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.34.00.00.00.00','Treaty','treaty_list.list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.34.01.00.00.00','Approve Operator',NULL,'RI_TREATY_APPROVE1');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.34.02.00.00.00','Approve Kepala Bagian',NULL,'RI_TREATY_APPROVE2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.34.03.00.00.00','Approve Kepala Divisi',NULL,'RI_TREATY_APPROVE3');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.34.04.00.00.00','Aktivasi TI',NULL,'RI_TREATY_APPROVE4');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.35.00.00.00.00','Upload Spreading Manual','ri_upload_manual_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.00.00.00.00','Validasi Reasuransi','reins_validate_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.01.00.00.00','Navigate Branch',NULL,'RI_VLD_UPL_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.02.00.00.00','Edit',NULL,'RI_VLD_UPL_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.03.00.00.00','Create',NULL,'RI_VLD_UPL_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.04.00.00.00','Approval',NULL,'RI_VLD_UPL_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('25.36.05.00.00.00','Proses',NULL,'RI_VLD_PROCESS');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.00.00.00.00.00','{L-ENGClaim-L}{L-INAKlaim-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.00.00.00.00','{L-ENGClaim-L}{L-INAKlaim-L}','policy_list.claim.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.01.00.00.00','Navigate Branch',NULL,'POL_CLAIM_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.02.00.00.00','Edit',NULL,'POL_CLAIM_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.03.00.00.00','Create',NULL,'POL_CLAIM_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.04.00.00.00','Approval',NULL,'POL_CLAIM_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.05.00.00.00','Print',NULL,'POL_CLAIM_PRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.08.00.00.00','Navigate Region',NULL,'POL_CLAIM_NAVREG');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.09.00.00.00','Create Claim Inward',NULL,'POL_CLAIM_INWARD_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.06.00.00.00','Edit No Surat Dll',NULL,'POL_CLAIM_EDIT2');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.07.00.00.00','Setujui Oleh Direksi',NULL,'CLAIM_APPROVE_BY_DIRECTOR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.10.10.00.00.00','Setujui Resiko Klaim',NULL,'CLAIM_APPROVE_BY_DIVISI');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.15.00.00.00.00','Upload Klaim','claim_upload_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.15.01.00.00.00','Navigate Branch',NULL,'CLAIM_UPL_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.15.02.00.00.00','Edit',NULL,'CLAIM_UPL_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.15.03.00.00.00','Create',NULL,'CLAIM_UPL_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.15.04.00.00.00','Approval',NULL,'CLAIM_UPL_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.16.00.00.00.00','Bypass Validasi Klaim','policy_bypass_list.bypassvalidasi.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.16.01.00.00.00','Bypass Validasi',NULL,'BYPASS_CLAIM_EDIT');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.17.00.00.00.00','Proses Klaim','claim_process_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.17.01.00.00.00','Navigate Branch',NULL,'CLAIM_PROCESS_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.17.02.00.00.00','Edit',NULL,'CLAIM_PROCESS_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.17.03.00.00.00','Create',NULL,'CLAIM_PROCESS_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.17.04.00.00.00','Approval',NULL,'CLAIM_PROCESS_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.18.00.00.00.00','Proses Subrogasi','subrogasi_process_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.18.01.00.00.00','Navigate Branch',NULL,'SUBROGASI_PROCESS_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.18.02.00.00.00','Edit',NULL,'SUBROGASI_PROCESS_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.18.03.00.00.00','Create',NULL,'SUBROGASI_PROCESS_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.18.04.00.00.00','Approval',NULL,'SUBROGASI_PROCESS_APRV');",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.20.00.00.00.00','{L-ENGInward-L}{L-INAInward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_report.go.crux?rpt=claim');",



        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.32.00.00.00.00','{L-ENGClosing Claim Reins-L}{L-INAClosing Klaim Reas-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.32.01.00.00.00','Closing','ins_closingcl_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.32.02.00.00.00','Reverse',NULL,'INS_CLOSING_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.32.03.00.00.00','Approval Closing',NULL,'POL_RI_CLOSING_APRV');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_claim_report.go.crux?rpt=claim');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.02.00.00.00','{L-ENGClaim Report-L}{L-INALaporan Klaim Khusus-L}',NULL,'/prod_claim_report.go.crux?rpt=claimnew');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.30.03.00.00.00','Pengajuan Klaim','policy_clm.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.30.04.00.00.00','Validasi Klaim','vldlist_clm.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.05.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Departemen Keuangan-L}',NULL,'/prod_claim_report.go.crux?rpt=claim_depkeu');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.30.06.00.00.00','Laporan',NULL,'/prod_claim_report.go.crux?rpt=claim_rekap');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.31.00.00.00.00','{L-ENGInward Report-L}{L-INALaporan Inward-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('30.31.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_inward.go.crux?rpt=inwardclm');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.39.00.00.00.00','Limit Cash Call','cashcall_list.list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('30.40.00.00.00.00','Setup','CONFIGURE&GROUP=CLAIM',NULL);",

        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.00.00.00.00.00','Data Searching',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.10.00.00.00.00','PA Kreasi/Kredit','policy_form.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.10.01.00.00.00','Navigate Branch',NULL,'KREASI_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.20.00.00.00.00','Kendaraan Bermotor','vehicle_search.crux' ,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('33.30.00.00.00.00','Flexible Search','policy_search.crux' ,NULL);",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.00.00.00.00','Settlement','receiptlist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.00.00.00.00','{L-ENGFinance-L}{L-INAKeuangan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.05.00.00.00','Rights',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.05.00.00','Reverse Keuangan',NULL,'FINANCE_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.10.00.00','Create',NULL,'SETTLR_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.20.00.00','Edit',NULL,'SETTLR_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.30.00.00','Approve',NULL,'SETTLR_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.40.00.00','Navigate Branch Keuangan',NULL,'SETTLR_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.00.05.50.00.00','Reverse',NULL,'SETTLR_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.05.10.10.01.00','Realisasi Lainnya',NULL,'FINANCE_CLM_OTHER');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.05.00.00.00.00','Pembayaran',NULL,NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.05.10.33.10.00','Approve Realisasi Polis Khusus',NULL,'SETTLR_APPROVE_POLIS_KHUSUS');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('35.06.00.00.00.00','{L-ENGTitipan Premi-L}{L-INATitipan Premi-L}','titipan_premi_list.crux&glid=6','/main.ctl?EVENT=titipan_premi_list.crux&glid=6',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.06.10.00.00.00','Upload Titipan Premi','titipanpremilist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('35.06.20.00.00.00','Titipan Premi Polis Khusus','titipan_premi_excomp_list.crux&glid=7','/main.ctl?EVENT=titipan_premi_excomp_list.crux&glid=7',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('34.00.00.00.00.00','{L-ENGPayment Monitoring-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.07.00.00.00.00','{L-ENGPayment Monitoring-L}{L-INAMonitoring Pembayaran-L}','policy_data.crux' ,NULL);",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.11.00.00.00.00','Tagihan Polis','policy_listtag.crux' ,NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.00.00.00.00','Deposito',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.10.00.00.00','Pembentukan Deposito','pembentukan_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.10.10.00.00','Navigate Branch',NULL,'DEPO_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.10.20.00.00','Approval',NULL,'DEPO_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.20.00.00.00','Bunga Deposito','bunga_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.20.10.00.00','Navigate Branch',NULL,'BUNGA_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.20.20.00.00','Approval',NULL,'BUNGA_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.30.00.00.00','Pencairan Deposito','pencairan_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.30.10.00.00','Navigate Branch',NULL,'CAIR_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.30.20.00.00','Approval',NULL,'CAIR_APRV');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.40.00.00.00','Upload Bunga Deposito','budeplist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.40.10.00.00','Navigate Branch',NULL,'BUDEP_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.08.40.20.00.00','Approval',NULL,'BUDEP_APRV');",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.00.00.00.00','Pengajuan Izin',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.10.00.00.00','Pengajuan Deposito','izinbentuk_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.10.01.00.00','Approval Cabang',NULL,'IZIN_APPROVAL_CAB');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.10.02.00.00','Approval Pusat',NULL,'IZIN_APPROVAL_PUS');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.10.03.00.00','Navigate Branch',NULL,'IZIN_NAVBR');",
        //        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.10.04.00.00','Approval',NULL,'IZIN_APRV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.20.00.00.00','Pembentukan Deposito','bentukdepo_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.30.00.00.00','Pengajuan Pencairan','izincair_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.31.00.00.00','Realisasi Pencairan','cairdepo_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.12.40.00.00.00','Entry Bunga','izinbunga_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.09.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.10.00.00.00','{L-ENGDeposito Report-L}{L-INALaporan Deposito-L}',NULL,'/prod_deposito.go.crux?rpt=deposito');",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.11.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan-L}',NULL,'/prod_finance_report.go.crux?rpt=finance');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.12.00.00.00','{L-ENGOJK Report-L}{L-INALaporan OJK-L}',NULL,'/prod_finance_ojk_report.go.crux?rpt=ojk');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.13.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan Cabang-L}',NULL,'/prod_finance_report.go.crux?rpt=semarang_fin');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.14.00.00.00','{L-ENGHead Office Finance Report-L}{L-INALaporan Keuangan Kantor Pusat-L}',NULL,'/prod_finance_report.go.crux?rpt=ho_finance');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.15.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan-L}',NULL,'/prod_finance_report.go.crux?rpt=finance_direksi');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.09.16.00.00.00','Laporan',NULL,'/prod_finance_report.go.crux?rpt=finance_rekap');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.00.00.00.00','Reasuransi',NULL,NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('35.16.01.00.00.00','{L-ENGTitipan Premi-L}{L-INATitipan Premi-L} Reasuransi','titipan_premi_reins_list.crux&glid=6','/main.ctl?EVENT=titipan_premi_reins_list.crux&glid=6',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.16.10.00.00.00','Upload Titipan Premi Reasuransi','titipanpremireinsurancelist.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.17.00.00.00.00','{L-ENGReporting-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.17.01.00.00.00','Laporan Reasuransi',NULL,'/prod_finance_report.go.crux?rpt=finance_reins');",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.15.00.00.00.00','{L-ENGFinance-L}{L-INAKeuangan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.20.00.00.00.00','{L-ENGTransaction-L}{L-INATransaksi-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.26.00.00.00.00','Surat Hutang','surathutanglist.crux&trx=50','/main.ctl?EVENT=surathutanglist.crux&trx=50');",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.27.00.00.00.00','Titipan Premi','titipanlist.crux','/main.ctl?EVENT=titipanlist.crux');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.20.99.00.00.00','Navigate Branch',NULL,'ARINVOICE_NAVBR');",
        
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.25.00.00.00.00','Setup','CONFIGURE&GROUP=AR_AP',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.26.00.00.00.00','Pemisahan Data Laporan','CONFIGURE&GROUP=SPLIT_DATA',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.27.00.00.00.00','Closing Pajak',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.27.01.00.00.00','Closing','ins_closingtax_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.27.02.00.00.00','Reverse',NULL,'INS_CLOSING_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.27.03.00.00.00','Approval Closing',NULL,'POL_RI_CLOSING_APRV');",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.00.00.00.00','Proposal Komisi',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.00.00.00','Upload Proposal Komisi','proposalcomm_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.01.00.00','Approval Cabang',NULL,'PROP_APPROVAL_CAB');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.02.00.00','Approval Kasie',NULL,'PROP_APPROVAL_SIE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.03.00.00','Approval Kabag',NULL,'PROP_APPROVAL_BAG');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.04.00.00','Approval Kadiv',NULL,'PROP_APPROVAL_DIV');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.05.00.00','Create',NULL,'PROP_CREATE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.29.01.06.00.00','Approval Pmsrn',NULL,'PROP_APPROVAL_PMS');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.31.00.00.00.00','Warning Keuangan',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.31.01.00.00.00','Warning Piutang Premi','piutangpremi_list.crux',NULL);",

        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.31.02.00.00.00','Warning Piutang Premi 30','piutangpremi30_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.31.03.00.00.00','Warning Piutang Premi 75','piutangpremi75_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.32.00.00.00.00','Izin Pengeluaran Dana',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.32.01.00.00.00','Biaya Pemasaran (History)','pemasaran_list.crux',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.00.00.00.00','{L-ENGReporting-L}{L-INALaporan-L}',NULL,NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.32.02.00.00.00','Biaya Pemasaran 0.5%','biapem_list.crux',NULL);",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.33.00.00.00.00','{L-ENGReporting-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('35.33.01.00.00.00','Laporan Biaya Pemasaran',NULL,'/prod_biapem.go.crux?rpt=biapem');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.34.00.00.00.00','Cashflow Klaim','cashflowclaim_list.crux' ,NULL);",


//        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('36.30.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('36.30.01.00.00.00','{L-ENGFinance Report-L}{L-INALaporan Keuangan-L}',NULL,'/prod_report.go.crux?rpt=finance');",
        
       
         /*"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.10.00.00.00','Cash Position',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.15.00.00.00','Bank Position',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.20.00.00.00','Mutation',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.25.00.00.00','Journal',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.30.00.00.00','Neraca',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.35.00.00.00','Loss / Profit',NULL,NULL);",
         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.15.40.00.00.00','Income Statement',NULL,NULL);",*/
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.40.00.00.00','Outstanding Invoices','rptOutstandingARAP.crux',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.44.00.00.00','{L-ENGAR/AP Detail-L}{L-INADetil Hutang Piutang-L}','rptArAPDetail.crux',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.45.00.00.00','{L-ENGR/I Payable Summary-L}{L-INARekapitulasi Hutang Reasuransi-L}','arreport.crux&mode=AP',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.50.00.00.00','{L-ENGR/I Receivable Summary-L}{L-INARekapitulasi Piutang Reasuransi-L}','arreport.crux&mode=AR',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.55.00.00.00','{L-ENGTechnical Account Statement-L}{L-INATechnical Account Statement-L}','rptSOAReinsurance.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('35.30.55.00.00.00','R/I Receivable Summary','arreport.crux&mode=AR',NULL);",
        
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.00.00.00.00.00','{L-ENGAccounting-L}{L-INAAkuntansi-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.00.00.00.00','{L-ENGJournal Entry-L}{L-INAJurnal Entry-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.10.00.00.00','{L-ENGGeneral Journal-L}{L-INADaftar Mutasi-L}','gl_list.crux&glid=1','/main.ctl?EVENT=gl_list.crux&glid=1',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.10.20.00.00','Ubah oleh KP',NULL,'EDIT_BY_KP');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.15.00.00.00','{L-ENGBank Cash Journal-L}{L-INAJurnal Kas Bank-L}','cashbankGL_list.crux&glid=2','/main.ctl?EVENT=gl_list.crux&glid=2',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.16.10.00.00','Upload {L-ENGCash Bank Journal-L}{L-INAJurnal Kas Bank-L}','cashbank_upload_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.20.00.00.00','{L-ENGMemorial Journal-L}{L-INAJurnal Memorial-L}','memorial_gl_list.crux&glid=3','/main.ctl?EVENT=memorial_gl_list.crux&glid=3',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.20.10.00.00','Upload {L-ENGMemorial Journal-L}{L-INAJurnal Memorial-L}','memoriallist.crux',NULL);",

        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.20.00.00.00','{L-ENGClaim Advanced Payment-L}{L-INAUang Muka Klaim-L}','free_gl_list.crux&glid=3','/main.ctl?EVENT=free_gl_list.crux&glid=3',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.21.00.00.00','{L-ENGPremium Advanced Payment-L}{L-INAUang Muka Premi-L}','premi_gl_list.crux&glid=4','/main.ctl?EVENT=premi_gl_list.crux&glid=4',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.22.00.00.00','{L-ENGCommission Payment-L}{L-INAUang Muka Komisi-L}','comission_gl_list.crux&glid=5','/main.ctl?EVENT=comission_gl_list.crux&glid=5',NULL);",
//        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.23.00.00.00','{L-ENGBusiness Development Journal-L}{L-INAJurnal Pembinaan Bisnis-L}','pb_gl_list.crux&glid=6','/main.ctl?EVENT=pb_gl_list.crux&glid=6',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.24.00.00.00','{L-ENGSyariah Report-L}{L-INALaporan Syariah-L}','syariah_gl_list.crux&glid=7','/main.ctl?EVENT=syariah_gl_list.crux&glid=7',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.24.10.00.00','Upload {L-ENGSyariah Report-L}{L-INALaporan Syariah-L}','syariahlist.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.25.00.00.00','Input RKAP','rkap_gl_list.crux&glid=9','/main.ctl?EVENT=rkap_gl_list.crux&glid=9',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.25.10.00.00','Upload Input RKAP','rkaplist.crux',NULL);",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.26.00.00.00','Input Data Entertainment','accountEntertainList.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.27.00.00.00','Input Data Promosi','accountPromosiList.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.10.40.00.00.00','Rounded Jurnal','gl_rounded.crux',NULL)",

//        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.25.00.00.00','Evaluasi Pelaksanaan RKAP','pelaksanaan_list.crux',NULL,NULL);",
        // "INSERT INTO s_functions(function_id,function_name,ctl_id,url,resource_id) VALUES('40.10.23.00.00.00','{L-ENGTitipan Premi-L}{L-INATitipan Premi-L}','titipan_premi_list.crux&glid=6','/main.ctl?EVENT=titipan_premi_list.crux&glid=6',NULL);",
        
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.12.00.00.00.00','Report Setup','GL_REPORT',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.14.00.00.00.00','Print Report',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.11.00.00.00.00','{L-ENGPosting-L}{L-INAPosting-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.11.01.00.00.00','Posting','gl_posting_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.11.01.01.00.00','Reverse Posting',NULL,'POSTING_REVERSE');",
                "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.11.01.02.00.00','Finalisasi Neraca',NULL,'POSTING_FINALIZE');",
        
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.17.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('40.17.01.00.00.00','{L-ENGAccounting Report-L}{L-INALaporan Akuntansi-L}',NULL,'/prod_account.go.crux?rpt=accounting');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.16.00.00.00.00','{L-ENGReport-L}{L-INALaporan-L}',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('40.16.10.00.00.00','{L-ENGAccounting Details Report-L}{L-INALaporan Rincian Akuntansi-L}',NULL,'/prod_account.go.crux?rpt=accounting');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('40.17.00.00.00.00','{L-ENGOperational Cost-L}{L-INABiaya Operasional-L}',NULL,'/prod_biaop.go.crux?rpt=biaop');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('40.18.00.00.00.00','Laporan Biaya Operasional (RKAP)',NULL,'/prod_rkap.go.crux?rpt=rkap');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.00.00.00.00','Setup',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.10.00.00.00','Account Type','GL_ACCT_TYPE',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.15.00.00.00','Account Mask','CONFIGURE&GROUP=GL_ACCT_MASK',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.20.00.00.00','Fiscal Year','CONFIGURE&GROUP=GL_FISCAL_YR',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.25.00.00.00','Journal Master','GL_J_MASTER',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.30.00.00.00','Cost Center','GL_DEPT_LIST',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.35.00.00.00','Periods','GL_PERIODS',NULL);",
        // "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.40.00.00.00','Accounts','GL_ACCOUNTS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.36.00.00.00','Closing Setting','closing_setting_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.40.00.00.00','Accounts','account_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.40.10.00.00','Upload Accounts','uploadAccount_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.41.00.00.00','Accounts Insurance','accountinsurancelist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.00.00.00','Currency','currency_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.10','Setup','CONFIGURE&GROUP=GL_CCY',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('40.20.45.15','Exchange Rate',NULL,NULL);",


        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.00.00.00.00.00','Realisasi Anggaran',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.01.00.00.00','Navigate Branch',NULL,'REQ_NAVBR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.02.00.00.00','Navigate Region',NULL,'REQ_NAVRE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.03.00.00.00','Approval',NULL,'REQ_APPROVAL');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.04.00.00.00','Approval Direksi',NULL,'REQ_APPROVAL_DIR');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.05.00.00.00','Cashier',NULL,'REQ_CASHIER');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.06.00.00.00','Reverse',NULL,'REQ_REVERSE');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.07.00.00.00','Cetak Ulang Slip',NULL,'REQ_REPRINT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.08.00.00.00','Pemasaran',NULL,'REQ_PMS');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.09.00.00.00','Umum',NULL,'REQ_UMUM');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.01.10.00.00.00','Administrasi',NULL,'REQ_ADM');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.10.00.00.00.00','Tabel Kewenangan','capacity_edit2.authority.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.10.01.00.00.00','Edit',NULL,'CAPA_AUTHO_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.21.00.00.00.00','Saldo Anggaran','capacity_edit3.rkap.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.30.00.00.00.00','Tabel Kas','capacity_edit2.kas.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.30.01.00.00.00','Edit',NULL,'CAPA_KAS_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.31.00.00.00.00','Pengajuan Anggaran ','proposal_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.32.00.00.00.00','Realisasi Anggaran ','realized_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.33.00.00.00.00','Cashflow Anggaran ','cashflow_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('41.34.00.00.00.00','Validasi Anggaran ','validate_list.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('41.40.00.00.00.00','Laporan',NULL,'/prod_deposito.go.crux?rpt=request');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('42.00.00.00.00.00','Laporan Akuntansi',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.10.00.00.00','Laporan Akuntansi',NULL,'/acc_account.go.crux?rpt=accounting');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.20.00.00.00','Evaluasi Biaya Pemasaran',NULL,'/acc_account.go.crux?rpt=rkappem');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.30.00.00.00','Evaluasi Biaya Umum',NULL,'/acc_account.go.crux?rpt=rkapum');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.40.00.00.00','Evaluasi Biaya Administrasi',NULL,'/acc_account.go.crux?rpt=rkapadm');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.50.00.00.00','Evaluasi Biaya Investasi',NULL,'/acc_account.go.crux?rpt=rkapin');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('42.10.60.00.00.00','Evaluasi Biaya Pendapatan Beban',NULL,'/acc_account.go.crux?rpt=rkapdpt');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id,orderseq) VALUES('44.00.00.00.00.00','Laporan',NULL,NULL,1);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,orderseq) VALUES('44.10.01.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Produksi-L}',NULL,'/prod_marketing_report.go.crux?rpt=marketing_direksi',2);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,orderseq) VALUES('44.10.02.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Klaim-L}',NULL,'/prod_claim_report.go.crux?rpt=claim_direksi',3);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url,orderseq) VALUES('44.10.03.00.00.00','{L-ENGProduction Report-L}{L-INALaporan Keuangan-L}',NULL,'/fin_report.go.crux?rpt=account_direksi',4);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('44.10.04.00.00.00','Laporan Biaya Operasional (RKAP)',NULL,'/fin_report.go.crux?rpt=account_direksi');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('46.00.00.00.00.00','Laporan Edocument',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('46.10.01.00.00.00','Laporan Produksi Marketing',NULL,'/prod_rekap.go.crux?rpt=marketing');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('46.20.01.00.00.00','Laporan Rincian Akuntansi',NULL,'/prod_account2.go.crux?rpt=accounting');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.00.00.00.00.00','Utilities',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.10.00.00.00.00','Schedule of Policy',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.15.00.00.00.00','{L-ENGInsurance Tools-L}{L-INAInsurance Tools-L}','printpolicywording.crux',NULL);",
//         "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.20.00.00.00.00','Clauses',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.20.00.00.00.00','{L-ENGNews Ticker-L}{L-INANews Ticker-L}','CONFIGURE&GROUP=NEWS',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.21.00.00.00.00','{L-ENGAnnouncement-L}{L-INAPengumuman-L}','announcement.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.25.00.00.00.00','Data Export',NULL,'/prod_report.go.crux?rpt=utilities');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.25.00.00.00.00','Data Export',NULL,'/prod_utilities_report.go.crux?rpt=utilities');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.30.00.00.00.00','{L-ENGEncrypt Code-L}{L-INAEncrypt Code-L}','utilities.crux',NULL);",
        //"INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.35.00.00.00.00','Transfer Data',NULL,'/prod_report.go.crux?rpt=transfer');",
        
        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.35.00.00.00.00','Transfer Data',NULL,'/prod_utilities_report.go.crux?rpt=transfer');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.40.00.00.00.00','Fix Data','fix_data.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.45.00.00.00.00','{L-ENGArchive-L}{L-INAArsip-L}','archivelist.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,url) VALUES('45.46.00.00.00.00','Validasi Data',NULL,'/prod_validation_tools.go.crux?rpt=validation');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.48.00.00.00.00','Early Warning','earlyWarning_form.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('45.50.00.00.00.00','Klaim H2H','claimDocumentH2H_form.crux',NULL);",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.00.00.00.00.00','Maintenance',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.00.00.00.00','Documents',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.01.00.00.00','Super Edit',NULL,'POL_SUPER_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.02.00.00.00','User Manual',NULL,'USER_MANUAL_VIEW');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.04.00.00.00','Super Edit User',NULL,'USER_POL_SUPER_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('50.01.05.00.00.00','Lock Fitur Print PDF',NULL,'USER_LOCK_PRINT_PDF');",

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('51.00.00.00.00.00','Approval Direksi',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('51.20.00.00.00.00','Upload Persetujuan Direksi','uploadbodlist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('51.20.01.00.00.00','Create',NULL,'UPLOADBOD_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('51.20.02.00.00.00','Edit',NULL,'UPLOADBOD_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('51.20.03.00.00.00','Delete',NULL,'UPLOADBOD_DELETE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('52.20.00.00.00.00','Persetujuan Direksi','approvalbodlist.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('52.20.01.00.00.00','Create',NULL,'APPROVALBOD_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('52.20.02.00.00.00','Edit',NULL,'APPROVALBOD_EDIT')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('52.20.03.00.00.00','Delete',NULL,'APPROVALBOD_DELETE')",

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

        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.00.00.00.00.00','Inventory / Inventaris',NULL,NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.10.00.00.00.00','Input Barang','umum_barang_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.20.00.00.00.00','Barang Masuk','umum_barang_in_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.20.01.00.00.00','Buat',NULL,'INVENTORY_CREATE')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.20.02.00.00.00','Ubah',NULL,'INVENTORY_EDIT');",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.20.03.00.00.00','Setujui',NULL,'INVENTORY_APPROVED')",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.30.00.00.00.00','Barang Keluar','umum_barang_out_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.40.00.00.00.00','Permintaan Barang','umum_reqbarang_list.crux',NULL);",
        "INSERT INTO s_functions(function_id,function_name,ctl_id,resource_id) VALUES('47.50.00.00.00.00','Stok Barang','umum_stock_list.crux',NULL);",
        
    };
}

/*
 
 
 
 
 
 
 
 */