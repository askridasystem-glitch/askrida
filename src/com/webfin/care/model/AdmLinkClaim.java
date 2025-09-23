/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.care.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author doni
 */
public class AdmLinkClaim extends DTO implements RecordAudit {
    
    public static String tableName = "care_admlink_claim";

    public static String fieldMap[][] = {
        {"polId","pol_id"},
        {"branch","branch"},
        {"ct","ct"},
        {"claimNo","claimno"},
        {"refNo","refno"},
        {"voucher","voucher"},
        {"name","name"},
        {"address","address"},
        {"refId","refid"},
        {"taxType","taxtype"},
        {"lob","lob"},
        {"date","date"},
        {"dueDate","duedate"},
        {"insured","insured"},
        {"code","code"},
        {"RCode","rcode"},
        {"type","type"},
        {"isType","istype"},
        {"policyNo","policyno"},
        {"startDate","start_date"},
        {"expiryDate","expiry_date"},
        {"tsi","tsi"},
        {"dateOfLoss","date_of_loss"},
        {"settledDate","settled_date"},
        {"remarks","remarks"},
        {"currency","currency"},
        {"rate","rate"},
        {"claim","claim"},
        {"deductible","deductible"},
        {"salvage","salvage"},
        {"cost","cost"},
        {"tax","tax"},
        {"subrogation","subrogation"},
        {"PNominalVat","pnominal_vat"},
        {"PNominalTax","pnominal_tax"},
        {"segment","segment"},
        {"mo","mo"},
        {"source","source"},
        {"sourceAddress","source_address"},
        {"sourceRefId","source_refid"},
        {"accountNo","accountno"},
        {"accountName","accountname"},
        {"bankName","bankname"},
        {"bank","bank"},
        {"bankBranch","bankbranch"},
        {"installment","installment"},
        {"dueDate1","duedate_1"},
        {"amountDue1","amountdue_1"},
        {"dueDate2","duedate_2"},
        {"amountDue2","amountdue_2"},
        {"dueDate3","duedate_3"},
        {"amountDue3","amountdue_3"},
        {"dueDate4","duedate_4"},
        {"amountDue4","amountdue_4"},
        {"dueDate5","duedate_5"},
        {"amountDue5","amountdue_5"},
        {"dueDate6","duedate_6"},
        {"amountDue6","amountdue_6"},
        {"dueDate7","duedate_7"},
        {"amountDue7","amountdue_7"},
        {"dueDate8","duedate_8"},
        {"amountDue8","amountdue_8"},
        {"dueDate9","duedate_9"},
        {"amountDue9","amountdue_9"},
        {"dueDate10","duedate_10"},
        {"amountDue10","amountdue_10"},
        {"dueDate11","duedate_11"},
        {"amountDue11","amountdue_11"},
        {"dueDate12","duedate_12"},
        {"amountDue12","amountdue_12"},
        {"paymentCc","payment_cc"},
        {"PDate","pdate"},
        {"tglProses","tgl_proses"},
        {"prosesFlag","proses_flag"},
        {"stBatchNo","batchno"},
        {"stVoucherNo","voucherno"},
        {"stAdmNo","admno"},

    };

    private String polId;
    private String branch;
    private Date ct;
    private String claimNo;
    private String refNo;
    private String voucher;
    private String name;
    private String address;
    private String refId;
    private String taxType;
    private String lob;
    private Date date;
    private Date dueDate;
    private String insured;
    private String code;
    private String RCode;
    private String type;
    private String isType;
    private String policyNo;
    private Date startDate;
    private Date expiryDate;
    private BigDecimal tsi;
    private Date dateOfLoss;
    private Date settledDate;
    private String remarks;
    private String currency;
    private BigDecimal rate;
    private BigDecimal claim;
    private BigDecimal deductible;
    private BigDecimal salvage;
    private BigDecimal cost;
    private BigDecimal tax;
    private BigDecimal subrogation;
    private BigDecimal PNominalVat;
    private BigDecimal PNominalTax;
    private String segment;
    private String mo;
    private String source;
    private String sourceAddress;
    private String sourceRefId;
    private String accountNo;
    private String accountName;
    private String bankName;
    private String bank;
    private String bankBranch;
    private String installment;
    private Date dueDate1;
    private BigDecimal amountDue1;
    private Date dueDate2;
    private BigDecimal amountDue2;
    private Date dueDate3;
    private BigDecimal amountDue3;
    private Date dueDate4;
    private BigDecimal amountDue4;
    private Date dueDate5;
    private BigDecimal amountDue5;
    private Date dueDate6;
    private BigDecimal amountDue6;
    private Date dueDate7;
    private BigDecimal amountDue7;
    private Date dueDate8;
    private BigDecimal amountDue8;
    private Date dueDate9;
    private BigDecimal amountDue9;
    private Date dueDate10;
    private BigDecimal amountDue10;
    private Date dueDate11;
    private BigDecimal amountDue11;
    private Date dueDate12;
    private BigDecimal amountDue12;
    private BigDecimal paymentCc;
    private Date PDate;
    private String createWho;
    private Date createDate;
    private String changeWho;
    private Date changeDate;
    private Date tglProses;
    private String prosesFlag;

    private String stBatchNo;
    private String stVoucherNo;
    private String stAdmNo;

    public String getStAdmNo() {
        return stAdmNo;
    }

    public void setStAdmNo(String stAdmNo) {
        this.stAdmNo = stAdmNo;
    }

    public String getStBatchNo() {
        return stBatchNo;
    }

    public void setStBatchNo(String stBatchNo) {
        this.stBatchNo = stBatchNo;
    }

    public String getStVoucherNo() {
        return stVoucherNo;
    }

    public void setStVoucherNo(String stVoucherNo) {
        this.stVoucherNo = stVoucherNo;
    }

    public BigDecimal getPNominalTax() {
        return PNominalTax;
    }

    public void setPNominalTax(BigDecimal PNominalTax) {
        this.PNominalTax = PNominalTax;
    }

    public BigDecimal getPNominalVat() {
        return PNominalVat;
    }

    public void setPNominalVat(BigDecimal PNominalVat) {
        this.PNominalVat = PNominalVat;
    }

    public Date getPDate() {
        return PDate;
    }

    public void setPDate(Date PDate) {
        this.PDate = PDate;
    }

    public String getRCode() {
        return RCode;
    }

    public void setRCode(String RCode) {
        this.RCode = RCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmountDue1() {
        return amountDue1;
    }

    public void setAmountDue1(BigDecimal amountDue1) {
        this.amountDue1 = amountDue1;
    }

    public BigDecimal getAmountDue10() {
        return amountDue10;
    }

    public void setAmountDue10(BigDecimal amountDue10) {
        this.amountDue10 = amountDue10;
    }

    public BigDecimal getAmountDue11() {
        return amountDue11;
    }

    public void setAmountDue11(BigDecimal amountDue11) {
        this.amountDue11 = amountDue11;
    }

    public BigDecimal getAmountDue12() {
        return amountDue12;
    }

    public void setAmountDue12(BigDecimal amountDue12) {
        this.amountDue12 = amountDue12;
    }

    public BigDecimal getAmountDue2() {
        return amountDue2;
    }

    public void setAmountDue2(BigDecimal amountDue2) {
        this.amountDue2 = amountDue2;
    }

    public BigDecimal getAmountDue3() {
        return amountDue3;
    }

    public void setAmountDue3(BigDecimal amountDue3) {
        this.amountDue3 = amountDue3;
    }

    public BigDecimal getAmountDue4() {
        return amountDue4;
    }

    public void setAmountDue4(BigDecimal amountDue4) {
        this.amountDue4 = amountDue4;
    }

    public BigDecimal getAmountDue5() {
        return amountDue5;
    }

    public void setAmountDue5(BigDecimal amountDue5) {
        this.amountDue5 = amountDue5;
    }

    public BigDecimal getAmountDue6() {
        return amountDue6;
    }

    public void setAmountDue6(BigDecimal amountDue6) {
        this.amountDue6 = amountDue6;
    }

    public BigDecimal getAmountDue7() {
        return amountDue7;
    }

    public void setAmountDue7(BigDecimal amountDue7) {
        this.amountDue7 = amountDue7;
    }

    public BigDecimal getAmountDue8() {
        return amountDue8;
    }

    public void setAmountDue8(BigDecimal amountDue8) {
        this.amountDue8 = amountDue8;
    }

    public BigDecimal getAmountDue9() {
        return amountDue9;
    }

    public void setAmountDue9(BigDecimal amountDue9) {
        this.amountDue9 = amountDue9;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangeWho() {
        return changeWho;
    }

    public void setChangeWho(String changeWho) {
        this.changeWho = changeWho;
    }

    public BigDecimal getClaim() {
        return claim;
    }

    public void setClaim(BigDecimal claim) {
        this.claim = claim;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateWho() {
        return createWho;
    }

    public void setCreateWho(String createWho) {
        this.createWho = createWho;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateOfLoss() {
        return dateOfLoss;
    }

    public void setDateOfLoss(Date dateOfLoss) {
        this.dateOfLoss = dateOfLoss;
    }

    public BigDecimal getDeductible() {
        return deductible;
    }

    public void setDeductible(BigDecimal deductible) {
        this.deductible = deductible;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate1() {
        return dueDate1;
    }

    public void setDueDate1(Date dueDate1) {
        this.dueDate1 = dueDate1;
    }

    public Date getDueDate10() {
        return dueDate10;
    }

    public void setDueDate10(Date dueDate10) {
        this.dueDate10 = dueDate10;
    }

    public Date getDueDate11() {
        return dueDate11;
    }

    public void setDueDate11(Date dueDate11) {
        this.dueDate11 = dueDate11;
    }

    public Date getDueDate12() {
        return dueDate12;
    }

    public void setDueDate12(Date dueDate12) {
        this.dueDate12 = dueDate12;
    }

    public Date getDueDate2() {
        return dueDate2;
    }

    public void setDueDate2(Date dueDate2) {
        this.dueDate2 = dueDate2;
    }

    public Date getDueDate3() {
        return dueDate3;
    }

    public void setDueDate3(Date dueDate3) {
        this.dueDate3 = dueDate3;
    }

    public Date getDueDate4() {
        return dueDate4;
    }

    public void setDueDate4(Date dueDate4) {
        this.dueDate4 = dueDate4;
    }

    public Date getDueDate5() {
        return dueDate5;
    }

    public void setDueDate5(Date dueDate5) {
        this.dueDate5 = dueDate5;
    }

    public Date getDueDate6() {
        return dueDate6;
    }

    public void setDueDate6(Date dueDate6) {
        this.dueDate6 = dueDate6;
    }

    public Date getDueDate7() {
        return dueDate7;
    }

    public void setDueDate7(Date dueDate7) {
        this.dueDate7 = dueDate7;
    }

    public Date getDueDate8() {
        return dueDate8;
    }

    public void setDueDate8(Date dueDate8) {
        this.dueDate8 = dueDate8;
    }

    public Date getDueDate9() {
        return dueDate9;
    }

    public void setDueDate9(Date dueDate9) {
        this.dueDate9 = dueDate9;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPaymentCc() {
        return paymentCc;
    }

    public void setPaymentCc(BigDecimal paymentCc) {
        this.paymentCc = paymentCc;
    }

    public String getPolId() {
        return polId;
    }

    public void setPolId(String polId) {
        this.polId = polId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getProsesFlag() {
        return prosesFlag;
    }

    public void setProsesFlag(String prosesFlag) {
        this.prosesFlag = prosesFlag;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getSalvage() {
        return salvage;
    }

    public void setSalvage(BigDecimal salvage) {
        this.salvage = salvage;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public Date getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(Date settledDate) {
        this.settledDate = settledDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceRefId() {
        return sourceRefId;
    }

    public void setSourceRefId(String sourceRefId) {
        this.sourceRefId = sourceRefId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getSubrogation() {
        return subrogation;
    }

    public void setSubrogation(BigDecimal subrogation) {
        this.subrogation = subrogation;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Date getTglProses() {
        return tglProses;
    }

    public void setTglProses(Date tglProses) {
        this.tglProses = tglProses;
    }

    public BigDecimal getTsi() {
        return tsi;
    }

    public void setTsi(BigDecimal tsi) {
        this.tsi = tsi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

}
