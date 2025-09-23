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
public class AdmLinkPremi extends DTO implements RecordAudit{

     public static String tableName = "care_admlink_premi";

    public static String fieldMap[][] = {

        {"polId","pol_id*pk"},
        {"voucher","voucher"},
        {"date","date"},
        {"branch","branch"},
        {"ct","ct"},
        {"dueDate","duedate"},
        {"wpc","wpc"},
        {"code","code"},
        {"atype","atype"},
        {"data","data"},
        {"type","type"},
        {"isType","istype"},
        {"policyNo","policyno"},
        {"inception","inception"},
        {"expiry","expiry"},
        {"effective","effective"},
        {"rcode","rcode"},
        {"riskStart","risk_start"},
        {"riskEnd","risk_end"},
        {"docNo","docno"},
        {"refNo","refno"},
        {"currency","currency"},
        {"rate","rate"},
        {"premium","premium"},
        {"ujroh","ujroh"},
        {"commission","commission"},
        {"fee","fee"},
        {"duty","duty"},
        {"discount","discount"},
        {"claim","claim"},
        {"adjustment","adjustment"},
        {"subsidies","subsidies"},
        {"subrogation","subrogation"},
        {"vat","vat"},
        {"tax","tax"},
        {"deposit","deposit"},
        {"otherFee1","otherfee_1"},
        {"otherFee2","otherfee_2"},
        {"otherTax1","othertax_1"},
        {"otherTax2","othertax_2"},
        {"PNominalVat","pnominal_vat"},
        {"PNominalTax","pnominal_tax"},
        {"segment","segment"},
        {"payment","payment"},
        {"name","name"},
        {"address","address"},
        {"refId","refid"},
        {"taxType","taxtype"},
        {"lob","lob"},
        {"insured","insured"},
        {"tsi","tsi"},
        {"coverage","coverage"},
        {"remarks","remarks"},
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
    private String voucher;
    private Date date;
    private String branch;
    private String ct;
    private Date dueDate;
    private Date wpc;
    private String code;
    private String atype;
    private String data;
    private String type;
    private String isType;
    private String policyNo;
    private Date inception;
    private Date expiry;
    private Date effective;
    private String rcode;
    private Date riskStart;
    private Date riskEnd;
    private String docNo;
    private String refNo;
    private String currency;
    private BigDecimal rate;
    private BigDecimal premium;
    private BigDecimal ujroh;
    private BigDecimal commission;
    private BigDecimal fee;
    private BigDecimal duty;
    private BigDecimal discount;
    private BigDecimal claim;
    private BigDecimal adjustment;
    private BigDecimal subsidies;
    private BigDecimal subrogation;
    private BigDecimal vat;
    private BigDecimal tax;
    private BigDecimal deposit;
    private BigDecimal otherFee1;
    private BigDecimal otherFee2;
    private BigDecimal otherTax1;
    private BigDecimal otherTax2;
    private BigDecimal PNominalVat;
    private BigDecimal PNominalTax;
    private String segment;
    private BigDecimal payment;
    private String name;
    private String address;
    private String refId;
    private String taxType;
    private String lob;
    private String insured;
    private BigDecimal tsi;
    private String coverage;
    private String remarks;
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

    public BigDecimal getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
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

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
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

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
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

    public BigDecimal getDuty() {
        return duty;
    }

    public void setDuty(BigDecimal duty) {
        this.duty = duty;
    }

    public Date getEffective() {
        return effective;
    }

    public void setEffective(Date effective) {
        this.effective = effective;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Date getInception() {
        return inception;
    }

    public void setInception(Date inception) {
        this.inception = inception;
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

    public BigDecimal getOtherFee1() {
        return otherFee1;
    }

    public void setOtherFee1(BigDecimal otherFee1) {
        this.otherFee1 = otherFee1;
    }

    public BigDecimal getOtherFee2() {
        return otherFee2;
    }

    public void setOtherFee2(BigDecimal otherFee2) {
        this.otherFee2 = otherFee2;
    }

    public BigDecimal getOtherTax1() {
        return otherTax1;
    }

    public void setOtherTax1(BigDecimal otherTax1) {
        this.otherTax1 = otherTax1;
    }

    public BigDecimal getOtherTax2() {
        return otherTax2;
    }

    public void setOtherTax2(BigDecimal otherTax2) {
        this.otherTax2 = otherTax2;
    }

    public Date getPDate() {
        return PDate;
    }

    public void setPDate(Date PDate) {
        this.PDate = PDate;
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

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
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

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
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

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
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

    public Date getRiskEnd() {
        return riskEnd;
    }

    public void setRiskEnd(Date riskEnd) {
        this.riskEnd = riskEnd;
    }

    public Date getRiskStart() {
        return riskStart;
    }

    public void setRiskStart(Date riskStart) {
        this.riskStart = riskStart;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
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

    public BigDecimal getSubrogation() {
        return subrogation;
    }

    public void setSubrogation(BigDecimal subrogation) {
        this.subrogation = subrogation;
    }

    public BigDecimal getSubsidies() {
        return subsidies;
    }

    public void setSubsidies(BigDecimal subsidies) {
        this.subsidies = subsidies;
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

    public BigDecimal getUjroh() {
        return ujroh;
    }

    public void setUjroh(BigDecimal ujroh) {
        this.ujroh = ujroh;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Date getWpc() {
        return wpc;
    }

    public void setWpc(Date wpc) {
        this.wpc = wpc;
    }

}
