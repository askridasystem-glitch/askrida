/***********************************************************************
 * Module:  com.webfin.insurance.custom.TravelHandler
 * Author:  Denny Mahendra
 * Created: Sep 21, 2006 12:56:37 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.custom;

import com.webfin.insurance.model.*;
import com.crux.util.*;
import com.webfin.FinCodec;
import java.math.BigDecimal;

public class TravelHandler extends DefaultCustomHandler {

    private final static transient LogManager logger = LogManager.getInstance(TravelHandler.class);
    private InsurancePolicyObjDefaultView selectedDefaultObject;

    public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) {
        try {

            if (policy.isStatusDraft() || policy.isStatusSPPA() || policy.isStatusPolicy() || policy.isStatusRenewal()) {
                calculate(policy, objx, validate);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String p(BigDecimal db) {
        return ConvertUtil.removeTrailing(ConvertUtil.print(db, 4));
    }

    public void calculate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) throws Exception {
        final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

        //objx.setStRiskCategoryID("18492");

        String coverWajib = null;
        String deductWajib = null;

        if (obj.getStReference18() != null) {
            if (obj.getStReference18().equalsIgnoreCase("1")) {
                coverWajib = "485,486,487,488,489,490,491,492,493,494,495,496,497,498,547";
                deductWajib = "3939,3940,3941,3942,3943,3944";
            } else if (obj.getStReference18().equalsIgnoreCase("2")) {
                coverWajib = "485,486,487,488,489,490,548";
                deductWajib = "3939,3940,3941";
            /*} else if (obj.getStReference18().equalsIgnoreCase("3")) {
                coverWajib = "485,486,580,581,488,489,582,583,491,494,497,498,548,579";
                //deductWajib = "3939,3940,3941,3942,3943,3944";*/
            }
        }

        if (obj.getStReference18() != null) {
            //if (obj.getStReference18().equalsIgnoreCase("1") || obj.getStReference18().equalsIgnoreCase("2") || obj.getStReference18().equalsIgnoreCase("3")) {
            if (obj.getStReference18().equalsIgnoreCase("1") || obj.getStReference18().equalsIgnoreCase("2")) {
                String coverApplied[] = coverWajib.split("[\\,]");
                for (int k = 0; k < coverApplied.length; k++) {

                    if (obj.getCoverage().size() < coverApplied.length) {
                        doAddLampiranCover(obj, coverApplied[k]);
                    }
                }
            }

            if (obj.getStReference18().equalsIgnoreCase("1") || obj.getStReference18().equalsIgnoreCase("2")) {
                String deductApplied[] = deductWajib.split("[\\,]");
                for (int k = 0; k < deductApplied.length; k++) {

                    if (obj.getDeductibles().size() < deductApplied.length) {
                        doAddLampiranDeduct(obj, deductApplied[k]);
                    }
                }
            }
        }


        if (obj.getStReference18() != null) {
            if (obj.getStReference18().equalsIgnoreCase("1")) {

                DTOList deductList = obj.getDeductibles();
                for (int i = 0; i < deductList.size(); i++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductList.get(i);

                    ded.setDbAmount(new BigDecimal(20));
                    ded.setStCurrencyCode("USD");

                }

                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    DTOList tsiList = obj.getSuminsureds();
                    for (int i = 0; i < tsiList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(50000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(50000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(50000));
                            }
                            tsi.setStAutoFlag("Y");
                        }

                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    DTOList tsiList = obj.getSuminsureds();
                    for (int i = 0; i < tsiList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(25000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(25000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(25000));
                            }
                            tsi.setStAutoFlag("Y");
                        }

                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    DTOList tsiList = obj.getSuminsureds();
                    for (int i = 0; i < tsiList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(10000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(10000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(10000));
                            }
                            tsi.setStAutoFlag("Y");
                        }

                    }
                } else if (obj.getStReference17().equalsIgnoreCase("4")) {
                    DTOList tsiList = obj.getSuminsureds();
                    for (int i = 0; i < tsiList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(100000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(100000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(100000));
                            }
                            tsi.setStAutoFlag("Y");
                        }

                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("2")) {

                DTOList deductList = obj.getDeductibles();
                for (int i = 0; i < deductList.size(); i++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductList.get(i);

                    ded.setDbAmount(new BigDecimal(250000));
                    ded.setStCurrencyCode("IDR");

                }

                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    DTOList covList = obj.getSuminsureds();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) covList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(100000000));
//                            tsi.setStAutoFlag("Y");
//                        }
//
                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(100000000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(100000000));
                            }
                            tsi.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    DTOList covList = obj.getSuminsureds();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) covList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(50000000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(50000000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                            tsi.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    DTOList covList = obj.getSuminsureds();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) covList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(10000000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(10000000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            tsi.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("6")) {
                    DTOList covList = obj.getSuminsureds();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) covList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(25000000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(25000000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(25000000));
                            }
                            tsi.setStAutoFlag("Y");
                        }
                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("3")) {

//                DTOList deductList = obj.getDeductibles();
//                for (int i = 0; i < deductList.size(); i++) {
//                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductList.get(i);
//
//                    ded.setDbAmount(new BigDecimal(20));
//                    ded.setStCurrencyCode("USD");
//
//                }

                if (obj.getStReference17().equalsIgnoreCase("5")) {
                    DTOList tsiList = obj.getSuminsureds();
                    for (int i = 0; i < tsiList.size(); i++) {
                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) tsiList.get(i);

//                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
//                            tsi.setDbInsuredAmount(new BigDecimal(80000000));
//                            tsi.setStAutoFlag("Y");
//                        }

                        if (tsi.getStInsuranceTSIID().equalsIgnoreCase("79")) {
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                tsi.setDbInsuredAmountFull(new BigDecimal(80000000));
                                tsi.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(80000000)));
                            } else {
                                tsi.setDbInsuredAmount(new BigDecimal(80000000));
                            }
                            tsi.setStAutoFlag("Y");
                        }
                    }
                }
            }
        }
        if (obj.getStReference18() != null) {
            if (obj.getStReference18().equalsIgnoreCase("1")) {

                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(50000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(50000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(750));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(750)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(750));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(2000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(2000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(2000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("261")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                            cov.setStDescription("Biaya tiket ekonomi pergi pulang dan biaya penginapan "
                                    + "hingga US$ 150 per hari selama maksimum 5 hari berturut-turut "
                                    + "(diluar dari biaya makanan,minuman dan biaya room service lainnya ");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("262")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(600));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(600)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(600));
                            }
                            cov.setStDescription("$30/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("263")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("264")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(250));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(250)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(250));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("265")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(25000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(25000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("266")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("267")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("268")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            cov.setStDescription("$50/12 jam");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("300")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(25000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(25000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(25000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(25000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(650));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(650)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(650));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("261")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                            cov.setStDescription("Biaya tiket ekonomi pergi pulang dan biaya penginapan "
                                    + "hingga US$ 150 per hari selama maksimum 5 hari berturut-turut "
                                    + "(diluar dari biaya makanan,minuman dan biaya room service lainnya ");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("262")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(null);
                            cov.setStDescription(null);
                            //cov.setStAutoFlag("N");
                            //cov.setStManualcov.ockFlag("N");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("263")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            cov.setStDescription("$15/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("264")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("265")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(15000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(15000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(15000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("266")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(650));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(650)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(650));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("267")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("268")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            cov.setStDescription("$50/12 jam");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("300")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("261")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                            cov.setStDescription("Biaya tiket ekonomi pergi pulang dan biaya penginapan "
                                    + "hingga US$ 150 per hari selama maksimum 5 hari berturut-turut "
                                    + "(diluar dari biaya makanan,minuman dan biaya room service lainnya ");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("262")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(null);
                            cov.setStDescription(null);
                            //cov.setStAutoFlag("N");
                            //cov.setStManualTSILockFlag("N");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("263")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100));
                            }
                            cov.setStDescription("$10/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("264")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                           // cov.setDbInsuredAmount(new BigDecimal(100));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("265")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("266")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(250));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(250)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(250));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("267")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(50));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("268")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            cov.setStDescription("$50/12 jam");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("300")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("4")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(5000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("261")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                            cov.setStDescription("Biaya tiket ekonomi pergi pulang dan biaya penginapan "
                                    + "hingga US$ 150 per hari selama maksimum 5 hari berturut-turut "
                                    + "(diluar dari biaya makanan,minuman dan biaya room service lainnya ");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("262")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("263")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$100/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("264")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("265")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("266")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(2500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(2500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(2500));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("267")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(150));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(150)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(150));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("268")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500));
                            }
                            cov.setStDescription("$50/12 jam");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("300")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000));
                            }
                            cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }
                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("2")) {

                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(40000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(40000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(40000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(2500000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(2500000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(2500000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1500000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1500000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1500000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("301")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(50000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(20000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(20000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(20000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 10.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setDbInsuredAmount(new BigDecimal(500000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("301")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 5.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 5.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(250000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(250000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(250000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(100000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(100000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("301")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(1000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(1000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(1000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("6")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(25000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(25000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("257")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 5.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 5.000.000,-");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(500000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(500000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(500000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("260")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(250000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(250000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(250000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("301")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(2500000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(2500000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(2500000));
                            }
                            //cov.setStAutoFlag("Y");
                        }
                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("3")) {

                if (obj.getStReference17().equalsIgnoreCase("5")) {
                    DTOList covList = obj.getCoverage();
                    for (int i = 0; i < covList.size(); i++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covList.get(i);

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(80000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(80000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(80000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("256")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(50000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("313")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 50.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(50000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("314")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 50.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(50000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("258")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            cov.setStDescription("Maksimal Reimbursment sampai dengan Rp 50.000.000,- (atau sesuai kurs Dollar)");
                            //cov.setDbInsuredAmount(new BigDecimal(50000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(50000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(50000000));
                            }
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("259")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("315")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(3000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(3000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(3000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("316")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("261")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStAutoFlag("Y");
//                            cov.setStDescription("Biaya tiket ekonomi pergi pulang dan biaya penginapan "
//                                    + "hingga US$ 150 per hari selama maksimum 5 hari berturut-turut "
//                                    + "(diluar dari biaya makanan,minuman dan biaya room service lainnya ");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("301")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(30000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(30000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(30000000));
                            }
                            //cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("264")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //.setDbInsuredAmount(new BigDecimal(3000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(3000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(3000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("267")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(5000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(5000000));
                            }
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("268")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(3000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(3000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(3000000));
                            }
                            //cov.setStDescription("$50/12 jam");
                            //cov.setStAutoFlag("Y");
                        }

                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("312")) {
                            cov.setStEntryInsuredAmountFlag("Y");
                            //cov.setDbInsuredAmount(new BigDecimal(10000000));
                            if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                cov.setDbInsuredAmount(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(10000000)));
                            } else {
                                cov.setDbInsuredAmount(new BigDecimal(10000000));
                            }
                            //cov.setStDescription("$50/hari");
                            //cov.setStAutoFlag("Y");
                        }
                    }
                }
            }
        }

        calculateRate(policy, objx, validate);
    }

    public void calculateRate(InsurancePolicyView policy, InsurancePolicyObjectView objx, boolean validate) throws Exception {
        final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objx;

        if (obj.getStReference18() != null) {
            if (obj.getStReference18().equalsIgnoreCase("1")) {
                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(11));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(11)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(11));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("6")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(14));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(14)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(14));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("7")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(16));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(16)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(16));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("8")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(22));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(22)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(22));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("9")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(25));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(25));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("10")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(28));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(28)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(28));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("11")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(31));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(31)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(31));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("12")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(35));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(35)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(35));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("14")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(88));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(88)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(88));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(7));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(7)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(7));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("6")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(9));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(9)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(9));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("7")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(11));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(11)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(11));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("8")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(13));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(13)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(13));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("9")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(14));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(14)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(14));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("10")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(18));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(18)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(18));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("11")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(20));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(20)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(20));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("12")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(25));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(25)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(25));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(5));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(5)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(5));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("6")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(7));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(7)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(7));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("7")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(9));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(9)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(9));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("8")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(12));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(12)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(12));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("9")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(13));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(13)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(13));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("10")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(15));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(15)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(15));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("11")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(17));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(17)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(17));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("12")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(20));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(20)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(20));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("4")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(13));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(13)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(13));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("6")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(19));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(19)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(19));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("7")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(23));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(23)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(23));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("8")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(29));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(29)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(29));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("9")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(32));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(32)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(32));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("10")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(43));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(43)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(43));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("11")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(49));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(49)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(49));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("12")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(55));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(55)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(55));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("14")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(143));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(143)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(143));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("2")) {
                if (obj.getStReference17().equalsIgnoreCase("1")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(45000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(45000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(45000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("2")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(67000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(67000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(67000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("3")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(84500));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(84500)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(84500));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("4")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(100000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(100000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(100000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("2")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();


                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(28000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(28000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(28000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("2")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(45000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(45000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(45000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("3")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(61500));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(61500)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(61500));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("4")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(73000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(73000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(73000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("3")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(8000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(8000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(8000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("2")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(12500));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(12500)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(12500));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("3")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(16500));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(16500)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(16500));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("4")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(19500));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(19500)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(19500));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                } else if (obj.getStReference17().equalsIgnoreCase("6")) {
                    if (obj.getStReference20().equalsIgnoreCase("1")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(15000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(15000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(15000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("2")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(23000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(23000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(23000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("3")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(30000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(30000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(30000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    } else if (obj.getStReference20().equalsIgnoreCase("4")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(36000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(36000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(36000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                }
            } else if (obj.getStReference18().equalsIgnoreCase("3")) {
                if (obj.getStReference17().equalsIgnoreCase("5")) {
                    if (obj.getStReference20().equalsIgnoreCase("15")) {
                        DTOList coverList = obj.getCoverage();

                        for (int i = 0; i < coverList.size(); i++) {
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverList.get(i);

//                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
//                                cov.setStEntryPremiFlag("Y");
//                                cov.setDbPremiNew(new BigDecimal(80000));
//                            }

                            if (cov.getStInsuranceCoverID().equalsIgnoreCase("255")) {
                                if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {
                                    cov.setDbPremiNew(BDUtil.mul(BDUtil.getRateFromPct(policy.getDbSharePct()), new BigDecimal(80000)));
                                } else {
                                    cov.setDbPremiNew(new BigDecimal(80000));
                                }
                                cov.setStEntryPremiFlag("Y");
                            }
                        }
                    }
                }
            }
        }
    }

    public InsurancePolicyObjDefaultView getSelectedDefaultObject() {
        return selectedDefaultObject;


    }

    public void setSelectedDefaultObject(InsurancePolicyObjDefaultView selectedDefaultObject) {
        this.selectedDefaultObject = selectedDefaultObject;


    }

    public void doAddLampiranCover(InsurancePolicyObjectView obj, String inscovpolid) throws Exception {

        final InsurancePolicyCoverView cv = new InsurancePolicyCoverView();

        cv.setStInsuranceCoverPolTypeID(inscovpolid);//242,243,244

        cv.initializeDefaults();

        final InsuranceCoverPolTypeView cvpt = cv.getInsuranceCoveragePolType();

        cv.setStInsuranceCoverID(cvpt.getStInsuranceCoverID());
        cv.setStCoverCategory(cvpt.getStCoverCategory());
        cv.setStEntryMode(FinCodec.AmountEntryMode.BY_AMOUNT);

        cv.markNew();

        obj.getCoverage().add(cv);


    }

    public void doAddLampiranDeduct(InsurancePolicyObjectView obj, String insdedpolid) throws Exception {

        final InsurancePolicyDeductibleView ded = new InsurancePolicyDeductibleView();

        ded.setStInsuranceClaimCauseID(insdedpolid);

        ded.markNew();

        obj.getDeductibles().add(ded);


    }
}
