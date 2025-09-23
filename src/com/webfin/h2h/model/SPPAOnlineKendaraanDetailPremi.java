/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfin.h2h.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;

/**
 *
 * @author doni
 */
public class SPPAOnlineKendaraanDetailPremi  extends DTO implements RecordAudit {

    public static String tableName = "sppa_kendaraan_detail_premi";

    public static String fieldMap[][] = {
        
        {"idDetailPremi", "id_detail_premi*pk"},
        {"mixCoverage", "mix_coverage"},
        {"tahun", "tahun"},
        {"mixRate", "mix_rate"},
        {"mixPremi", "mix_premi"},
        {"noPengajuan", "no_pengajuan"},
        {"deskripsi", "deskripsi"},
        {"hargaPertanggungan", "harga_pertanggungan"}
    };


    private String idDetailPremi;
    private String mixCoverage;
    private String tahun;
    private BigDecimal mixRate;
    private BigDecimal mixPremi;
    private String noPengajuan;
    private String deskripsi;
    private BigDecimal hargaPertanggungan;

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getHargaPertanggungan() {
        return hargaPertanggungan;
    }

    public void setHargaPertanggungan(BigDecimal hargaPertanggungan) {
        this.hargaPertanggungan = hargaPertanggungan;
    }

    public String getIdDetailPremi() {
        return idDetailPremi;
    }

    public void setIdDetailPremi(String idDetailPremi) {
        this.idDetailPremi = idDetailPremi;
    }

    public String getMixCoverage() {
        return mixCoverage;
    }

    public void setMixCoverage(String mixCoverage) {
        this.mixCoverage = mixCoverage;
    }

    public BigDecimal getMixPremi() {
        return mixPremi;
    }

    public void setMixPremi(BigDecimal mixPremi) {
        this.mixPremi = mixPremi;
    }

    public BigDecimal getMixRate() {
        return mixRate;
    }

    public void setMixRate(BigDecimal mixRate) {
        this.mixRate = mixRate;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }
}
