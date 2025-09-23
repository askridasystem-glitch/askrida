/***********************************************************************
 * Module:  com.webfin.gl.model.BungaDepositoHeaderView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 8:48:57 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.gl.model;

import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.webfin.insurance.model.InsuranceClaimCauseView;

public class BungaDepositoHeaderView extends BungaDepositoView {

    private DTOList details;
    private String stTrxHeaderID;

    public DTOList getDetails() {
        return details;
    }

    public void setDetails(DTOList details) {
        this.details = details;
    }

    public void reCalculate() {
    }

    private DTOList bungaDeposito;

    public DTOList getBungaDeposito(String stTrxHeaderID) {
        loadBungaDeposito(stTrxHeaderID);
        return bungaDeposito;
    }

    public void setBungaDeposito(DTOList bungaDeposito) {
        this.bungaDeposito = bungaDeposito;
    }

    public void loadBungaDeposito(String stTrxHeaderID) {
        try {
            if (bungaDeposito == null)
                bungaDeposito = ListUtil.getDTOListFromQuery(
                        "select a.* " +
                        "from ar_bunga_deposito a " +
                        "where a.trx_hdr_id = ?",
                        new Object[]{stTrxHeaderID},
                        BungaDepositoView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStTrxHeaderID() {
        return stTrxHeaderID;
    }

    public void setStTrxHeaderID(String stTrxHeaderID) {
        this.stTrxHeaderID = stTrxHeaderID;
    }
}
