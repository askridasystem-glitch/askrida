/***********************************************************************
 * Module:  com.webfin.insurance.custom.HouseHoldHandler
 * Author:  Denny Mahendra
 * Created: Sep 21, 2006 12:56:37 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.crux.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;

public class HouseHoldHandler extends DefaultCustomHandler {

    private final static transient LogManager logger = LogManager.getInstance(HouseHoldHandler.class);

    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {

            calculate(policy, objx, validate);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String p(BigDecimal db) {
        return ConvertUtil.removeTrailing(ConvertUtil.print(db, 4));
    }

    public void calculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) throws Exception {
        final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

        BigDecimal tsiBuilding = null;
        BigDecimal tsiHold = null;
        BigDecimal tsiTotal = null;
        BigDecimal tsiOther = null;

        DTOList tsiList = obj.getSuminsureds();
        for (int i = 0; i < tsiList.size(); i++) {
            InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

            if (tsi.getStInsuranceTSIPolTypeID().equalsIgnoreCase("501")) {
                tsiBuilding = tsi.getDbInsuredAmount();
            }

            if (tsi.getStInsuranceTSIPolTypeID().equalsIgnoreCase("502")) {
                tsiHold = tsi.getDbInsuredAmount();
            }

            tsiTotal = BDUtil.mul(BDUtil.add(tsiBuilding, tsiHold), BDUtil.getRateFromPct(new BigDecimal(5)));

            if (tsi.getStInsuranceTSIPolTypeID().equalsIgnoreCase("503")) {
                tsiOther = tsi.getDbInsuredAmount();
                if (Tools.compare(tsiOther, tsiTotal) > 0) {
                    throw new Exception("Harga Pertanggungan 'Lainnya (Other)' melebihi Limit \n"
                            + "maks. 5% dari Harga Pertanggungan Bangunan + Perabot ");
                }
            }
        }

        BigDecimal premiOther = null;
        BigDecimal premiFire = null;
        BigDecimal premiTotalOther = null;
        BigDecimal premiTJH = null;
        BigDecimal premiJamA = null;
        BigDecimal premiJamB = null;
        BigDecimal premiTotalTJH = null;
        BigDecimal premiTotalJamA = null;
        BigDecimal premiTotalJamB = null;

        DTOList premiList = obj.getCoverage();
        for (int i = 0; i < premiList.size(); i++) {
            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) premiList.get(i);

            if (cov.getStInsuranceCoverPolTypeID().equalsIgnoreCase("499")) {
                premiFire = cov.getDbPremi();
            }

            premiTotalOther = BDUtil.mul(premiFire, BDUtil.getRateFromPct(new BigDecimal(10)));
            premiTotalTJH = BDUtil.mul(BDUtil.add(tsiBuilding, tsiHold), BDUtil.getRateFromPct(new BigDecimal(10)));
            premiTotalJamA = BDUtil.mul(BDUtil.add(tsiBuilding, tsiHold), BDUtil.getRateFromPct(new BigDecimal(25)));
            premiTotalJamB = BDUtil.mul(BDUtil.add(tsiBuilding, tsiHold), BDUtil.getRateFromPct(new BigDecimal(25)));

            if (cov.getStInsuranceCoverPolTypeID().equalsIgnoreCase("504")) {
                premiOther = cov.getDbPremi();
                if (Tools.compare(premiOther, premiTotalOther) > 0) {
                    throw new Exception("Premi 'Jaminan I : Lainnya' melebihi Limit \n"
                            + "maks. 10% dari rate Fire");
                }
            }

            if (cov.getStInsuranceCoverPolTypeID().equalsIgnoreCase("505")) {
                premiTJH = cov.getDbInsuredAmount();
                if (Tools.compare(premiTJH, premiTotalTJH) > 0) {
                    throw new Exception("Harga Pertanggungan 'Jaminan II : TJH' melebihi Limit \n"
                            + "maks. 10% dari Harga Pertanggungan Bangunan + Perabot");
                }
            }

            if (cov.getStInsuranceCoverPolTypeID().equalsIgnoreCase("545")) {
                premiJamA = cov.getDbInsuredAmount();
                if (Tools.compare(premiJamA, premiTotalJamA) > 0) {
                    throw new Exception("Harga Pertanggungan 'Jaminan III : PA Jaminan A' melebihi Limit \n"
                            + "maks. 25% dari Harga Pertanggungan Bangunan + Perabot");
                }
            }

            if (cov.getStInsuranceCoverPolTypeID().equalsIgnoreCase("546")) {
                premiJamB = cov.getDbInsuredAmount();
                if (Tools.compare(premiJamB, premiTotalJamB) > 0) {
                    throw new Exception("Harga Pertanggungan 'Jaminan III : Jaminan B' melebihi Limit \n"
                            + "maks. 25% dari Harga Pertanggungan Bangunan + Perabot");
                }
            }
        }
    }
}
