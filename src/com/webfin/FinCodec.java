/***********************************************************************
 * Module:  com.webfin.FinCodec
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 10:11:50 PM
 * Purpose:
 ***********************************************************************/

package com.webfin;

import com.crux.util.LookUpUtil;
//import com.webfin.insurance.model.InsurancePolicyVehicleView;
//import com.webfin.insurance.model.InsurancePolicyPAView;
import com.webfin.acceptance.model.AcceptanceObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;

public class FinCodec  {
    public static final class AmountEntryMode {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String BY_RATE= "RATE";
        public final static transient String BY_AMOUNT= "AMT";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class ClaimStatus {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String PLA="PLA";
        public final static transient String DLA="DLA";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DLA,"Definitive Loss Adjustment")
                .add(PLA,"Preliminary Loss Adjustment")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class ClauseSelectType {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String SELECTED = "SELECTED";
        public final static transient String OPTIONAL = "OPTIONAL";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class InsuranceItemType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String PREMI = "PREMI";
        public final static transient String KOMISI = "COMIS";
        public final static transient String DISCOUNT = "DISC";
        public final static transient String FEE = "FEE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class PolicyItemEntryMode {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String AUTO = "AUTO";
        public final static transient String MANUAL = "MAN";
        public final static transient String PCT = "PCT";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(AUTO,"AUTO")
                .add(MANUAL,"MANUAL")
                .add(PCT,"RATE");
                
            }
            
            return lookUp;
        }
    }
    
    public static final class InvoiceType {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String AR = "AR";
        public final static transient String AP = "AP";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(AR,"Receivable")
                .add(AP,"Payable")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class InvoiceReceiptType {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String AR = "AR";
        public final static transient String AP = "AP";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class ReceiptClassType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String BANK = "BANK";
        public final static transient String NOTE = "NOTE";
        public final static transient String GL = "GL";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class ARReceiptLineType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String INVOICE = "INVOC";
        public final static transient String NOTE = "NOTE";
        public final static transient String COMISSION = "COMM";
        public final static transient String BALANCER = "BAL";
        public final static transient String GL = "GL";
        public final static transient String TITIPAN = "TITIP";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class GLReportColType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DESC = "DESC";
        public final static transient String BALANCE = "BAL";
        public final static transient String BALANCE_CREDIT = "BALCR";
        public final static transient String BALANCE_DEBIT = "BALDB";
        public final static transient String SUMMARY = "SUM";
        public final static transient String SUMMARY_CREDIT = "SUMCR";
        public final static transient String SUMMARY_DEBIT = "SUMDB";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DESC,"Description")
                .add(BALANCE,"Balance")
                .add(BALANCE_CREDIT,"Balance CR")
                .add(BALANCE_DEBIT,"Balance DB")
                .add(SUMMARY,"Summary")
                .add(SUMMARY_CREDIT,"Summary CR")
                .add(SUMMARY_DEBIT,"Summary DB")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class PolicyTypeCodeMap {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                //.add(PolicyTypeCode.VEHICLE,InsurancePolicyVehicleView.class)
                //.add(PolicyTypeCode.PA,InsurancePolicyPAView.class)
                .add(PolicyTypeCode.DEFAULT,InsurancePolicyObjDefaultView.class)
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class PolicyTypeCode {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DEFAULT = "DEF";
        public final static transient String PROPERTY = "PROPR";
        public final static transient String VEHICLE = "VEHCL";
        public final static transient String PA = "PA";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class ClauseRateType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String STANDARD = "STANDARD";
        public final static transient String PREMI_PCT = "PREMI_PCT";
        public final static transient String SEAT_PCT = "SEAT_PCT";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class ClauseLevel {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String POLICY = "POLICY";
        public final static transient String OBJECT = "OBJECT";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class PolicyEntityRelation {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String OWNER = "OWNER";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class EntityClass {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String INDIVIDUAL = "INDV";
        public final static transient String INSTITUTIONAL = "INST";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(INDIVIDUAL,"Individual")
                .add(INSTITUTIONAL,"Institutional")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class EntityMaritalStatus {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String SINGLE = "1";
        public final static transient String DOUBLE = "2";
        public final static transient String DIVORCED = "X";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(SINGLE,"Single")
                .add(DOUBLE,"Married")
                .add(DIVORCED,"Divorced")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class CoverCategory {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String MAIN = "STD";
        public final static transient String EXTENDED = "EXT";
        public final static transient String LIABILITY = "LOL";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(MAIN,"Standard Coverage")
                .add(EXTENDED,"Extended Coverage")
                .add(LIABILITY,"Limit of Liability")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class InsuranceCoverType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DIRECT = "DIRECT";
        public final static transient String COINSOUT = "COINSOUT";
        public final static transient String COINSIN = "COINSIN";
        public final static transient String INWARD = "INWARD";
        public final static transient String OUTWARD = "OUTWARD";
        public final static transient String COINSOUTSELF = "COINSOUTSELF";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DIRECT,"Direct")
                .add(COINSOUT,"Co-Leader")
                .add(COINSIN,"Co-Member")
                .add(INWARD,"Inward")
                .add(OUTWARD,"Outward")
                .add(COINSOUT,"Co-Leader (Self)")
                ;
            }
            
            return lookUp;
        }
        
        private static LookUpUtil lookUpCoasCode = null;
        
        public static LookUpUtil getLookUpCoasCode() {
            if (lookUpCoasCode == null) {
                lookUpCoasCode = new LookUpUtil()
                .add(DIRECT,"0")
                .add(COINSOUT,"1")
                .add(COINSIN,"2")
                .add(INWARD,"0")
                .add(OUTWARD,"0")
                .add(COINSOUTSELF,"3")
                ;
            }
            
            return lookUpCoasCode;
        }
    }
    
    public static final class PeriodUnit {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DAY = "DAY";
        public final static transient String MONTH = "MONTH";
        public final static transient String YEAR = "YEAR";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DAY,"{L-ENGDays-L}{L-INAHari-L}")
                .add(MONTH,"{L-ENGMonths-L}{L-INABulan-L}")
                .add(YEAR,"{L-ENGYears-L}{L-INATahun-L}")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class PolicyStatus {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DRAFT = "PROPOSAL";
        public final static transient String SPPA = "SPPA";
        public final static transient String POLICY = "POLICY";
        public final static transient String ENDORSE = "ENDORSE";
        public final static transient String CLAIM = "CLAIM";
        public final static transient String CANCEL = "CANCEL";
        public final static transient String RENEWAL = "RENEWAL";
        public final static transient String ENDORSECLAIM = "CLAIM ENDORSE";
        public final static transient String HISTORY = "HISTORY";
        public final static transient String ENDORSERI = "ENDORSE RI";
        public final static transient String PARENT = "PARENT";
        public final static transient String ENDORSEIN = "ENDORSE IN";
        public final static transient String TEMPORARY = "TEMPORARY";
        public final static transient String ENDORSETEMPORARY = "ENDORSE TEMPORARY";
        public final static transient String INWARD = "INWARD";
        public final static transient String CLAIMINWARD = "CLAIM INWARD";
        public final static transient String ENDORSECLAIMINWARD = "ENDORSE CLAIM INWARD";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DRAFT,"{L-ENGQuotation-L}{L-INAPenawaran-L}")
                .add(SPPA,"SPPA")
                .add(POLICY,"Policy")
                .add(ENDORSE,"Endorsement")
                .add(CLAIM,"Claim")
                .add(CANCEL,"Cancel")
                .add(RENEWAL,"{L-ENGRenewal-L}{L-INAPerpanjangan-L}")
                .add(ENDORSECLAIM,"Claim Endorsement")
                .add(HISTORY,"History")
                .add(ENDORSERI,"R/I Endorsement")
                .add(PARENT,"Parent")
                .add(ENDORSEIN,"Intern Endorsement")
                .add(TEMPORARY,"{L-ENGTemporary-L}{L-INASementara-L}")
                .add(ENDORSETEMPORARY,"{L-ENGTemporary Endorsemen-L}{L-INAEndorsemen Sementara-L}")
                .add(INWARD,"Inward")
                .add(CLAIMINWARD,"Claim Inward")
                .add(ENDORSECLAIMINWARD,"Claim Inward Endorsemen")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class CoInsurancePosition {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String LEADER = "LDR";
        public final static transient String MEMBER = "MEM";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class RegionClass {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
      /*
1	Kecamatan
2	Kelurahan
3	Desa
       
       */
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("1","Kecamatan")
                .add("2","Kelurahan")
                .add("3","Desa")
                ;
            }
            
            return lookUp;
        }
    }
    
    
    public static final class PolicyItemClass {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String PREMI = "PRM";
        public final static transient String CLAIM = "CLM";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil();
            }
            
            return lookUp;
        }
    }
    
    public static final class CustomerShareLevel {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String NATIONAL = "NAT";
        public final static transient String BRANCH = "BRN";
        public final static transient String USER = "USR";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(NATIONAL,"National")
                .add(BRANCH,"Branch")
                .add(USER,"User")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class GLPeriods {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("1","{L-ENGJanuary-L}{L-INAJanuari-L}")
                .add("2","{L-ENGFebruary-L}{L-INAFebruari-L}")
                .add("3","{L-ENGMarch-L}{L-INAMaret-L}")
                .add("4","April")
                .add("5","{L-ENGMay-L}{L-INAMei-L}")
                .add("6","{L-ENGJune-L}{L-INAJuni-L}")
                .add("7","{L-ENGJuly-L}{L-INAJuli-L}")
                .add("8","{L-ENGAugust-L}{L-INAAgustus-L}")
                .add("9","{L-ENGSeptember-L}{L-INASeptember-L}")
                .add("10","{L-ENGOctober-L}{L-INAOktober-L}")
                .add("11","November")
                .add("12","{L-ENGDecember-L}{L-INADesember-L}")
                ;
            }
            
            return lookUp;
        }
    }
    
    
    public static final class MonthPeriods {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("01","{L-ENGJanuary-L}{L-INAJanuari-L}")
                .add("02","{L-ENGFebruary-L}{L-INAFebruari-L}")
                .add("03","{L-ENGMarch-L}{L-INAMaret-L}")
                .add("04","April")
                .add("05","{L-ENGMay-L}{L-INAMei-L}")
                .add("06","{L-ENGJune-L}{L-INAJuni-L}")
                .add("07","{L-ENGJuly-L}{L-INAJuli-L}")
                .add("08","{L-ENGAugust-L}{L-INAAgustus-L}")
                .add("09","{L-ENGSeptember-L}{L-INASeptember-L}")
                .add("10","{L-ENGOctober-L}{L-INAOktober-L}")
                .add("11","November")
                .add("12","{L-ENGDecember-L}{L-INADesember-L}")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class TreatyClass {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String RE = "RE";
        public final static transient String CO = "CO";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(RE,"Reinsurance")
                .add(CO,"Coinsurance")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class RateScale {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String ACT = "0";
        public final static transient String PCT = "%";
        public final static transient String PMIL = "PMIL";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(ACT," ")
                .add(PCT,"%")
                .add(PMIL,"%o")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class CoinsuranceType {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String COINS = "COINS";
        public final static transient String COINSCOVER = "COINS_COVER";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(COINS,"Coinsurance")
                .add(COINSCOVER,"Coinsurance Coverage")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class InsurancePolicyTypeGroup {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String SURETYBOND = "7";
        public final static transient String BANKGUARANTEE = "8";
        public final static transient String EARTHQUAKE = "13";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(SURETYBOND,"Surety Bond")
                .add(BANKGUARANTEE,"Bank Guarantee")
                .add(EARTHQUAKE,"Earthquake")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class ClaimInwardTreatyDLAStatus {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public final static transient String INTERIM = "INTERIM";
        public final static transient String FULLFINAL = "FULL_FINAL";
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(INTERIM,"Interim")
                .add(FULLFINAL,"Full And Final")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class RegisterStatus {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String PENDING = "PENDING";
        public final static transient String FINISH = "FINISH";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(PENDING,"PENDING")
                .add(FINISH,"FINISH")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class DIVISION {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String MARKETING = "MARKETING";
        public final static transient String UNDERWRITING = "UNDERWRITING";
        public final static transient String REINSURANCE = "REINSURANCE";
        public final static transient String CLAIM = "CLAIM";
        public final static transient String FINANCE = "FINANCE";
        public final static transient String ACCOUNTING = "ACCOUNTING";
        public final static transient String TI = "TI";
        public final static transient String PENGAWASAN = "PENGAWASAN";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(MARKETING,"MARKETING")
                .add(UNDERWRITING,"UNDERWRITING")
                .add(REINSURANCE,"REINSURANCE")
                .add(CLAIM,"CLAIM")
                .add(FINANCE,"FINANCE")
                .add(ACCOUNTING,"ACCOUNTING")
                .add(TI,"TI")
                .add(PENGAWASAN,"PENGAWASAN")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class Deposito {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String DEPOSITO = "DEPOSITO";
        public final static transient String RENEWAL = "RENEWAL";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(DEPOSITO,"Pembentukan")
                .add(RENEWAL,"Perpanjangan")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class TitipanCause {
        public final static transient String CODEGROUP = "CODE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("1","Polis Sudah Disetujui")
                .add("2","Polis Belum Disetujui")
                .add("3","Lainnya")
                .add("4","Kurang Bayar Premi")
                .add("5","Tidak Memotong Pajak")
                .add("6","Premi Dibayar Cicilan")
                .add("7","Pajak Komisi")
                .add("8","Panjar Komisi")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class XOLType {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String WORKING = "WORKING";
        public final static transient String CATASTROPHE = "CATASTROPHE";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(WORKING,"Working")
                .add(CATASTROPHE,"Catastrophe")
                ;
            }
            
            return lookUp;
        }
    }
    
    public static final class XOLLayer {
        public final static transient String CODEGROUP = "CODE";
        
        public final static transient String ONE = "1";
        public final static transient String TWO = "2";
        public final static transient String THREE = "3";
        public final static transient String FOUR = "4";
        public final static transient String FIVE = "5";
        public final static transient String SUBLAYER = "SUBLAYER";
        
        private static LookUpUtil lookUp = null;
        
        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(ONE,"1")
                .add(TWO,"2")
                .add(THREE,"3")
                .add(FOUR,"4")
                .add(FIVE,"5")
                .add(SUBLAYER,"Sub Layer")
                ;
            }
            
            return lookUp;
        }
    }

    public static final class JournalType {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("OFFSET","Offset")
                .add("NON_OFFSET","Non Offset")
                ;
            }

            return lookUp;
        }
    }

    public static final class MonthPeriods2 {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add("1","{L-ENGJanuary-L}{L-INAJanuari-L}")
                .add("2","{L-ENGFebruary-L}{L-INAFebruari-L}")
                .add("3","{L-ENGMarch-L}{L-INAMaret-L}")
                .add("4","April")
                .add("5","{L-ENGMay-L}{L-INAMei-L}")
                .add("6","{L-ENGJune-L}{L-INAJuni-L}")
                .add("7","{L-ENGJuly-L}{L-INAJuli-L}")
                .add("8","{L-ENGAugust-L}{L-INAAgustus-L}")
                .add("9","{L-ENGSeptember-L}{L-INASeptember-L}")
                .add("10","{L-ENGOctober-L}{L-INAOktober-L}")
                .add("11","November")
                .add("12","{L-ENGDecember-L}{L-INADesember-L}")
                ;
            }

            return lookUp;
        }
    }

    public static final class EntityLevel {
        public final static transient String CODEGROUP = "CODE";

        public final static transient String JUNIOR = "JUNIOR";
        public final static transient String SENIOR = "SENIOR";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(JUNIOR,"Junior")
                .add(SENIOR,"Senior")
                ;
            }

            return lookUp;
        }
    }

    public static final class UploadStatus {
        public final static transient String APPROVE = "SETUJUI";
        public final static transient String NOAPPROVE = "TIDAK SETUJUI";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(APPROVE,"SETUJUI PERMINTAAN")
                .add(NOAPPROVE,"TIDAK SETUJUI PERMINTAAN")
                ;
            }

            return lookUp;
        }
    }

    public static final class AcceptanceStatus {
        public final static transient String CODEGROUP = "CODE";

        public final static transient String ACCEPTANCE = "ACCEPTANCE";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(ACCEPTANCE,"{L-ENGAcceptance-L}{L-INAAkseptasi-L}")
                ;
            }

            return lookUp;
        }
    }

    public static final class PolicyTypeCodeMap2 {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                //.add(PolicyTypeCode.VEHICLE,InsurancePolicyVehicleView.class)
                //.add(PolicyTypeCode.PA,InsurancePolicyPAView.class)
                .add(PolicyTypeCode.DEFAULT,AcceptanceObjDefaultView.class)
                ;
            }

            return lookUp;
        }
    }

    public static final class DepositoUnit {
        public final static transient String ARO = "ARO";
        public final static transient String NONARO = "NON ARO";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(ARO,"ARO")
                .add(NONARO,"NON ARO")
                ;
            }

            return lookUp;
        }
    }

    public static final class IzinPencairan {
        public final static transient String KLAIM = "1";
        public final static transient String TRANSFER = "2";
        public final static transient String BENTUK = "3";
        public final static transient String GAJI = "4";
        public final static transient String JASPROD = "5";
        public final static transient String THR = "6";
        public final static transient String INSENTIF = "7";
        public final static transient String DIVIDEN = "8";
        public final static transient String KOMISI = "9";
        public final static transient String RESTITUSI = "10";
//        public final static transient String LAIN = "4";

        private static LookUpUtil lookUp = null;

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(TRANSFER,"TRANSFER")
                .add(KLAIM,"KLAIM")
//                .add(BENTUK,"PEMBENTUKAN ULANG")
                .add(KOMISI,"PEMBAYARAN KOMISI")
                .add(RESTITUSI,"PEMBAYARAN RESTITUSI")
                .add(GAJI,"PEMBAYARAN GAJI")
                .add(JASPROD,"PEMBAYARAN JASPROD")
                .add(THR,"PEMBAYARAN THR")
                .add(INSENTIF,"PEMBAYARAN INCENTIVE")
                .add(DIVIDEN,"PEMBAYARAN DIVIDEN")
//                .add(LAIN,"LAIN-LAIN")
                ;
            }

            return lookUp;
        }
    }

    public static final class DataSourceType{

        private static LookUpUtil lookUp = null;

        public final static transient String H2H = "1";
        public final static transient String INTERKONEKSI = "2";
        public final static transient String MANUAL = "3";
        public final static transient String ASSET_BPD = "4";
        public final static transient String SPPA_ONLINE = "5";

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                .add(H2H,"1 - Host To Host")
                .add(INTERKONEKSI,"2 - Interkoneksi")
                .add(MANUAL,"3 - Manual")
                .add(ASSET_BPD,"4 - Asset BPD")
                .add(SPPA_ONLINE,"5 - SPPA Online")
                ;
            }

            return lookUp;
        }
    }
}
