/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.care.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DateUtil;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author doni
 */
public class StatusBayarCareView extends DTO implements RecordAudit{

    private BigDecimal net;
    private String type;
    private BigDecimal payment;
    private BigDecimal outstanding;
    private String paymentReference;
    private String paymentDate;
    private Date tglBayar;
    private String voucherNo;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Date getTglBayar() {
        //  10/4/2024
        //  012345678
        if(paymentDate!=null){
            return DateUtil.getDateCare(paymentDate);
        }
        return tglBayar;
    }

    public void setTglBayar(Date tglBayar) {
        this.tglBayar = tglBayar;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

}
