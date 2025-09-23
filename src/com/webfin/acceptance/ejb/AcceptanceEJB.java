package com.webfin.acceptance.ejb;

import com.crux.util.*;
import com.crux.util.crypt.Crypt;
import com.crux.common.model.UserSession;
import com.crux.common.controller.UserSessionMgr;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.filter.InsurancePolicyFilter;
import com.webfin.insurance.form.PolicyForm;
import com.webfin.insurance.model.*;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.ar.model.ARReceiptLinesView;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.gl.util.GLUtil;
import com.webfin.entity.filter.EntityFilter;
import com.webfin.entity.model.EntityView;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;
import com.webfin.FinCodec;
import com.webfin.acceptance.model.AcceptanceDocumentView;
import com.webfin.acceptance.model.AcceptanceObjDefaultView;
import com.webfin.acceptance.model.AcceptanceObjectView;
import com.webfin.acceptance.model.AcceptanceView;
import com.webfin.insurance.model.InsuranceZoneLimitView;
import com.webfin.insurance.filter.ZoneFilter;

import com.webfin.pks.model.PerjanjianKerjasamaView;
import java.io.*;

import com.webfin.ar.forms.ReceiptForm;
import com.webfin.datatext.model.DataTeksMasukLogView;

import java.sql.ResultSet;

import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.*;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.insurance.form.ProductionMarketingReportForm;
import com.webfin.insurance.form.ProductionReinsuranceReportForm;
import com.webfin.insurance.form.ProductionReportForm;
import com.webfin.insurance.form.ProductionUtilitiesReportForm;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Oct 9, 2005
 * Time: 9:59:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class AcceptanceEJB implements SessionBean {
    public SessionContext ctx;
    public String id;
    private String pol_id = null;
    
    private final static transient LogManager logger = LogManager.getInstance(AcceptanceEJB.class);

    public void setID(String id) throws Exception {
        this.id = id;
    }
    
    public String getID() throws Exception {
        return id;
    }
    
    private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB", EntityManagerHome.class.getName()))
        .create();
    }
    
    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB", AccountReceivableHome.class.getName()))
        .create();
    }
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public AcceptanceEJB() {
    }
    
    public void ejbCreate() throws CreateException {
    }
    
    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        ctx = sessionContext;
    }
    
    public void ejbRemove() throws EJBException {
    }
    
    public void ejbActivate() throws EJBException {
    }
    
    public void ejbPassivate() throws EJBException {
    }
    
    public DTOList listPolicies(InsurancePolicyFilter f) throws Exception {
        return ListUtil.getDTOListFromQuery("select * from ins_policy",
                InsurancePolicyView.class, f);
    }
    
    public AcceptanceView getInsurancePolicy(String stPolicyID) throws Exception {
        final AcceptanceView pol = (AcceptanceView) ListUtil.getDTOListFromQuery("select * from ins_pol_acceptance where pol_id = ?",
                new Object[]{stPolicyID},
                AcceptanceView.class).getDTO();
        
        return pol;
    }
    
    public DTOList getInsItemsList() throws Exception {
        return ListUtil.getDTOListFromQuery("select * from ins_items",
                InsuranceItemsView.class);
    }
    
    public void save(AcceptanceView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (AcceptanceView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();

        try {

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("ACCEPTANCE")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(policyDocuments);
                
                final DTOList objects = pol.getObjects();
                
                for (int i = 0; i < objects.size(); i++) {
                    AcceptanceObjDefaultView obj = (AcceptanceObjDefaultView) objects.get(i);

                    obj.setStPolicyID(pol.getStPolicyID());
                    
                    if (obj.isNew()) {
   
                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("ACCEPTANCEOBJ")));
          
                    }
                    
                    S.store(obj); 


                    final DTOList detailDocuments = obj.getDetailDocuments();

                    for (int j = 0; j < detailDocuments.size(); j++) {
                        AcceptanceDocumentView doc = (AcceptanceDocumentView) detailDocuments.get(j);

                        doc.setStPolicyID(pol.getStPolicyID());
                        doc.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("ACCEPTANCEDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.store(detailDocuments);

                }
                
                final DTOList deletedObjects = objects.getDeleted();
                
                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                        AcceptanceObjectView obj = (AcceptanceObjectView) deletedObjects.get(i);

                        S.store(obj);
                       
                        S.store(obj.getDetailDocuments());

                        
                    }


            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){

                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            

            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus)){
                if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generatePLANo();
                else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generateDLANo();
            }

            S.store(pol);
            
            /*POSTING*/

            
            final AcceptanceView oldPol = (AcceptanceView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            
            if(approvalMode){


               
            }
            

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public String savePolicyHistory(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
            
            if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
            
            pol.setStParentID(pol.getStPolicyID());
            if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
            
            final DTOList details = pol.getDetails();
            
            final HashMap insuranceItemsMap = getInsuranceItemsMap();
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                
                ip.setStPolicyID(pol.getStPolicyID());
                
                if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                
                ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
            }
            
            S.store(details);
            
            final DTOList claimItems = pol.getClaimItems();
            
            for (int i = 0; i < claimItems.size(); i++) {
                InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);
                
                it.setStPolicyID(pol.getStPolicyID());
                
                if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
            }
            
            S.store(claimItems);
            
            final DTOList clausules = (DTOList) pol.getClausules().clone();
            if (clausules != null) {
                
                for (int i = clausules.size() - 1; i >= 0; i--) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                    
                    if (!icl.isSelected()) {
                        clausules.delete(i);
                        continue;
                    }
                    
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(null);
                }
                
                S.store(clausules);
            }
            
            final DTOList claimDocuments = pol.getClaimDocuments();
            
            for (int i = 0; i < claimDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(claimDocuments);
            
            final DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                
                final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                
                if (pol.isStatusClaim())
                    if (!isClaimObject2) continue;
                
                obj.setStPolicyID(pol.getStPolicyID());
                
                if (obj.isNew()) {
                    final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                    
                    if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                }
                
                S.store(obj);
                
                final DTOList oclaus = obj.getClausules();
                for (int j = 0; j < oclaus.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                }
                S.store(oclaus);
                
                final DTOList suminsureds = obj.getSuminsureds();
                
                for (int j = 0; j < suminsureds.size(); j++) {
                    InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                    if (itsi.isNew())
                        itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                    
                    itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    itsi.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(suminsureds);
                
                final DTOList coverage = obj.getCoverage();
                
                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                    
                    if (cov.isNew())
                        cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                    cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    cov.setStPolicyID(pol.getStPolicyID());
                    cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                }
                
                S.store(coverage);
                
                final DTOList deductibles = obj.getDeductibles();
                
                for (int j = 0; j < deductibles.size(); j++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                    
                    if (ded.isNew())
                        ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                    ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    ded.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(deductibles);
                
                //beban load
                if (pol.isStatusHistory()) {
                    {
                        // save treaties
                        
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.combineDeleted();
                        
                        for (int l = 0; l < treaties.size(); l++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                            
                            if (tre.isDelete()) tre.getDetails().deleteAll();
                            
                            if (tre.isNew())
                                tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                            
                            tre.setStPolicyID(null);
                            tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                            
                            final DTOList tredetails = tre.getDetails();
                            
                            tredetails.combineDeleted();
                            
                            for (int j = 0; j < tredetails.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                
                                if (tredet.isDelete()) {
                                    tredet.getShares().deleteAll();
                                }
                                
                                if (tredet.isNew())
                                    tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                
                                tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                
                                final DTOList shares = tredet.getShares();
                                
                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                    
                                    if (ri.isNew())
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                    
                                    //tambahan
                                    ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                    //
                                    ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                    ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                    
                                }
                                
                                S.store(shares);
                                
                                
                            }
                            
                            S.store(tredetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                }
                
            }
            
            final DTOList deletedObjects = objects.getDeleted();
            
            if (deletedObjects != null)
                for (int i = 0; i < deletedObjects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                
                S.store(obj);
                //S.store(obj.getClausules());
                S.store(obj.getSuminsureds());
                S.store(obj.getCoverage());
                
                //beban load
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal()) {
                    final DTOList treaties = obj.getTreaties();
                    
                    treaties.deleteAll();
                    treaties.combineDeleted();
                    
                    for (int j = 0; j < treaties.size(); j++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                        
                        final DTOList treDetails = tre.getDetails();
                        
                        treDetails.deleteAll();
                        treDetails.combineDeleted();
                        
                        for (int k = 0; k < treDetails.size(); k++) {
                            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                            
                            
                            final DTOList treShares = trd.getShares();
                            treShares.deleteAll();
                            treShares.combineDeleted();
                            
                            S.store(treShares);
                        }
                        
                        S.store(treDetails);
                        
                    }
                    
                    S.store(treaties);
                }
                }
            
            final DTOList entities = pol.getEntities();
            
            for (int i = 0; i < entities.size(); i++) {
                InsurancePolicyEntityView entityView = (InsurancePolicyEntityView) entities.get(i);
                entityView.setStPolicyID(pol.getStPolicyID());
                
                if (entityView.isNew())
                    entityView.setStPolicyEntityID(String.valueOf(IDFactory.createNumericID("INSPOLENT")));
                
                if (entityView.isModified()) {
                    entityView.setStEntityID(getRemoteEntityManager().save(entityView.getEntity()));
                }
                
                S.store(entityView);
                
            }
            
            final DTOList coins = pol.getCoins2();
            
            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coins);
            
            final DTOList coinscoverage = pol.getCoinsCoverage();
            
            for (int i = 0; i < coinscoverage.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coinscoverage);
            
                /*
                final DTOList coveragereinsurance = pol.getCoverageReinsurance();
                 
                for (int i = 0; i < coveragereinsurance.size(); i++) {
                    InsurancePolicyCoverReinsView coverreins = (InsurancePolicyCoverReinsView) coveragereinsurance.get(i);
                 
                    if (coverreins.isNew())
                        coverreins.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOVERAGEREINS")));
                 
                    coverreins.setStPolicyID(pol.getStPolicyID());
                }
                 
                S.store2(coveragereinsurance);
                 */
            
            final DTOList installment = pol.getInstallment();
            
            for (int i = 0; i < installment.size(); i++) {
                InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                
                inst.setStPolicyID(pol.getStPolicyID());
                
                if (inst.isNew())
                    inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
            }
            
            S.store2(installment);
            
            final DTOList deductibles = pol.getDeductibles();
            
            for (int i = 0; i < deductibles.size(); i++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);
                
                if (ded.isNew())
                    ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                
                ded.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store(deductibles);
            
            if (pol.isStatusHistory()) {
                {
                    // save treaties
                    
                    final DTOList treaties = pol.getTreaties();
                    
                    treaties.combineDeleted();
                    
                    for (int l = 0; l < treaties.size(); l++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                        
                        if (tre.isDelete()) tre.getDetails().deleteAll();
                        
                        if (tre.isNew())
                            tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                        
                        tre.setStPolicyID(pol.getStPolicyID());
                        
                        final DTOList tredetails = tre.getDetails();
                        
                        tredetails.combineDeleted();
                        
                        for (int j = 0; j < tredetails.size(); j++) {
                            InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                            
                            if (tredet.isDelete()) tredet.getShares().deleteAll();
                            
                            if (tredet.isNew())
                                tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                            
                            tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                            
                            final DTOList shares = tredet.getShares();
                            
                            for (int k = 0; k < shares.size(); k++) {
                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                
                                if (ri.isNew())
                                    ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                
                                ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                
                                ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                
                            }
                            
                            S.store(shares);
                            
                        }
                        
                        S.store(tredetails);
                        
                    }
                    
                    S.store(treaties);
                }
            }
            
            S.store(pol);
            
            return pol.getStPolicyID();
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public void saveAfterReverse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            //final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            //boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal()) {
                
                postARInstallmentAcrualBases(pol);

                postCoas(pol);
                
            }
            
            if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                
                postAPClaim(pol);

                postAPClaimDetailSeparate(pol);

                postARCoas(pol);

                postAPTaxAcrual(pol);
            }

            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    private void postAPClaim(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = pol.getClaimItems();
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        final ARInvoiceView invoice = new ARInvoiceView();
        invoice.markNew();
        
        invoice.setStPolicyID(pol.getStPolicyID());
        //invoice.setStInvoiceNo("E/CLAIM-" + pol.getStPolicyNo());
        invoice.setStInvoiceNo("K" + pol.getStPolicyNo());
        //invoice.setDbAmount(pol.getDbAmount());
        //invoice.setDtInvoiceDate(us.getDtTransactionDate());
        //invoice.setDtDueDate(us.getDtTransactionDate());
        invoice.setDtInvoiceDate(pol.getDtDLADate());
        invoice.setDtDueDate(pol.getDtDLADate());
        invoice.setDbAmountSettled(null);
        invoice.setStCurrencyCode(pol.getStClaimCurrency());
        invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
        invoice.setStPostedFlag("N");
        invoice.setStARCustomerID(pol.getStEntityID());
        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
        invoice.setStEntityID(pol.getStEntityID());
        invoice.setStCostCenterCode(pol.getStCostCenterCode());
        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
        //invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
        
        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
        
        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
        invoice.setStAttrPolicyName(pol.getStCustomerName());
        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
        invoice.setStAttrPolicyID(pol.getStPolicyID());
        invoice.setStRefID1(pol.getStPLANo());
        invoice.setStRefID2(pol.getStDLANo());

        if(pol.getDbClaimAdvancePaymentAmount()!=null)
            invoice.setStRefID0("PANJAR");
        
        if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
        }

        invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));
        
        invoice.setStPostedFlag("Y");
        
        final DTOList ivdetails = new DTOList();
        
        if (details.size() < 1) throw new RuntimeException("Details empty, probably corrupted data"); //remark for bersih2x
        
        if(details.size() >= 1){
            InsurancePolicyItemsView firstItem = (InsurancePolicyItemsView) details.get(0);
        
            invoice.setStARTransactionTypeID(firstItem.getInsItem().getARTrxLine().getStARTrxTypeID());

            invoice.setDetails(ivdetails);

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if(insuranceItem.isCreateArApSeparately()) continue;

                if(insuranceItem.isUangMukaKlaim()) continue;

                if(BDUtil.isZeroOrNull(politem.getDbAmount())) continue;
                
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                BigDecimal amt = politem.getDbAmount();

//                if (politem.getStTaxCode() == null) {
//                    amt = politem.getDbAmount();
//                } else {
//                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
//                    else amt = politem.getDbNetAmount();
//                }

                ivd.setDbEnteredAmount(amt);
                
                //add tax claim
                
                if (politem.getStTaxCode()!=null) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;
                        
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());
                    
                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
                    
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    BigDecimal taxamt = politem.getDbTaxAmount();
                              
                    ivdc.setDbEnteredAmount(taxamt);
                    ivdc.setDbAmount(BDUtil.mul(taxamt,pol.getDbCurrencyRateClaim(),2));
                    
                    ivdc.setStTaxCode(stTaxCode);
                    
                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                }
            }

            invoice.recalculateClaimOnly(true);

            getRemoteAccountReceivable().saveInvoiceClaim(invoice);

            //if (pol.isStatusClaimEndorse()) getRemoteAccountReceivable().saveClaimEndorse(invoice);
            //else getRemoteAccountReceivable().saveInvoiceClaim(invoice);
        }
    }
    
    private void postReas(InsurancePolicyView pol) throws Exception {
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = pol.getObjects();
        
        for (int w = 0; w < details.size(); w++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) details.get(w);
            
            final DTOList treaties = obj.getTreaties();
            
            for (int i = 0; i < treaties.size(); i++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);
                
                final DTOList treDetails = tre.getDetails();
                
                for (int j = 0; j < treDetails.size(); j++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(j);
                    
                    if (trd.getTreatyDetail().isOR()) continue;
                    
                    final DTOList shares = trd.getShares();
                    
                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                        
                        //if (!BDUtil.biggerThanZero(ri.getDbPremiAmount())) continue;
                        if (BDUtil.isZero(ri.getDbPremiAmount())) continue;
                        
                        if (trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) continue;
                        
                        final ARInvoiceView invoice = new ARInvoiceView();
                        invoice.markNew();
                        
                        invoice.setStRefID0("REINS/" + ri.getStInsurancePolicyReinsID());
                        invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                        //invoice.setDbAmount(pol.getDbAmount());
                        /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
                        invoice.setDtDueDate(us.getDtTransactionDate());*/
                        invoice.setDtInvoiceDate(pol.getDtPolicyDate());
                        invoice.setDtDueDate(pol.getDtPolicyDate());
                        invoice.setDbAmountSettled(null);
                        invoice.setStCurrencyCode(pol.getStCurrencyCode());
                        invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                        invoice.setStPostedFlag("Y");
                        invoice.setStARCustomerID(ri.getStMemberEntityID());
                        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                        invoice.setStEntityID(invoice.getStARCustomerID());
                        invoice.setStCostCenterCode(pol.getStCostCenterCode());
                        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
                        
                        invoice.setStARTransactionTypeID(trd.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
                        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
                        
                        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                        invoice.setStAttrPolicyName(pol.getStCustomerName());
                        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
                        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
                        invoice.setStAttrPolicyID(pol.getStPolicyID());
                        invoice.setStPolicyID(pol.getStPolicyID());
                        
                        invoice.setStReferenceD0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
                        invoice.setStReferenceD1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStReferenceE0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
                        invoice.setStReferenceE1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStReferenceZ0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode3());
                        invoice.setStReferenceZ1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        //bikin surat hutang
                        //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
                        if(ri.getDtValidReinsuranceDate()==null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        if(ri.getDtValidReinsuranceDate()!=null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+ "."+
                                    String.valueOf(k+1) +
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        
                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
                        //finish
                        
                        final DTOList ivdetails = new DTOList();
                        
                        invoice.setDetails(ivdetails);
                        
                        //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
                        
                        final DTOList artlines = ListUtil.getDTOListFromQuery(
                                "select * from ar_trx_line where ar_trx_type_id = ?",
                                new Object[]{invoice.getStARTransactionTypeID()},
                                ARTransactionLineView.class
                                );
                        
                        {
                            
                            for (int v = 0; v < artlines.size(); v++) {
                                ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);
                                
                                if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                    
                                    ivd.markNew();
                                    
                                    ivdetails.add(ivd);
                                    
                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(ri.getDbPremiAmount());
                                } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {
                                    
                                    if (!BDUtil.biggerThanZero(ri.getDbRICommAmount())) continue;
                                    
                                    if (invoice.getStReferenceZ0() == null)
                                        throw new RuntimeException("Comission Account Code not found for " + trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                                    
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                    
                                    ivd.markNew();
                                    
                                    ivdetails.add(ivd);
                                    
                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(ri.getDbRICommAmount());
                                } else throw new RuntimeException("Unknown Item class : " + artl);
                            }
                        }
                                
                                invoice.recalculate();
                                
                                getRemoteAccountReceivable().save(invoice);
                                
                    }
                }
            }
        }
    }
    
    private void postReasCoverage(InsurancePolicyView pol) throws Exception {
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = pol.getObjects();
        
        for (int w = 0; w < details.size(); w++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) details.get(w);
            
            final DTOList treaties = obj.getTreaties();
            
            for (int i = 0; i < treaties.size(); i++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);
                
                final DTOList treDetails = tre.getDetails();
                
                for (int j = 0; j < treDetails.size(); j++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(j);
                    
                    if (trd.getTreatyDetail().isOR()) continue;
                    
                    final DTOList shares = trd.getShares();
                    
                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                        
                        //if (!BDUtil.biggerThanZero(ri.getDbPremiAmount())) continue;
                        if (BDUtil.isZero(ri.getDbPremiAmount())) continue;
                        
                        if (trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) continue;
                        
                        final ARInvoiceView invoice = new ARInvoiceView();
                        invoice.markNew();
                        
                        invoice.setStRefID0("REINS/" + ri.getStInsurancePolicyReinsID());
                        invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
                        //invoice.setDbAmount(pol.getDbAmount());
                        invoice.setDtInvoiceDate(us.getDtTransactionDate());
                        invoice.setDtDueDate(us.getDtTransactionDate());
                        invoice.setDbAmountSettled(null);
                        invoice.setStCurrencyCode(pol.getStCurrencyCode());
                        invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                        invoice.setStPostedFlag("Y");
                        invoice.setStARCustomerID(ri.getStMemberEntityID());
                        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                        invoice.setStEntityID(invoice.getStARCustomerID());
                        invoice.setStCostCenterCode(pol.getStCostCenterCode());
                        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
                        
                        invoice.setStARTransactionTypeID(trd.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
                        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
                        
                        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                        invoice.setStAttrPolicyName(pol.getStCustomerName());
                        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
                        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
                        invoice.setStAttrPolicyID(pol.getStPolicyID());
                        
                        invoice.setStReferenceD0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
                        invoice.setStReferenceD1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStReferenceE0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
                        invoice.setStReferenceE1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStReferenceZ0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode3());
                        invoice.setStReferenceZ1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        //bikin surat hutang
                        //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
                        if(ri.getDtValidReinsuranceDate()==null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        if(ri.getDtValidReinsuranceDate()!=null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+ "."+
                                    String.valueOf(k+1) +
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        
                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
                        //finish
                        
                        final DTOList ivdetails = new DTOList();
                        
                        invoice.setDetails(ivdetails);
                        
                        //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
                        
                        final DTOList artlines = ListUtil.getDTOListFromQuery(
                                "select * from ar_trx_line where ar_trx_type_id = ?",
                                new Object[]{invoice.getStARTransactionTypeID()},
                                ARTransactionLineView.class
                                );
                        
                        {
                            
                            for (int v = 0; v < artlines.size(); v++) {
                                ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);
                                
                                if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                    
                                    ivd.markNew();
                                    
                                    ivdetails.add(ivd);
                                    
                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(ri.getDbPremiAmount());
                                } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {
                                    
                                    if (!BDUtil.biggerThanZero(ri.getDbRICommAmount())) continue;
                                    
                                    if (invoice.getStReferenceZ0() == null)
                                        throw new RuntimeException("Comission Account Code not found for " + trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                                    
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                    
                                    ivd.markNew();
                                    
                                    ivdetails.add(ivd);
                                    
                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(ri.getDbRICommAmount());
                                } else throw new RuntimeException("Unknown Item class : " + artl);
                            }
                        }
                                
                                invoice.recalculate();
                                
                                getRemoteAccountReceivable().save(invoice);
                                
                    }
                }
            }
        }
    }
    
    private void postReasClaim(InsurancePolicyView pol) throws Exception {
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = new DTOList();
        
        details.add(pol.getClaimObject());
        
        for (int w = 0; w < details.size(); w++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) details.get(w);
            
            final DTOList treaties = obj.getTreaties();
            
            for (int i = 0; i < treaties.size(); i++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);
                
                final DTOList treDetails = tre.getDetails();
                
                for (int j = 0; j < treDetails.size(); j++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(j);
                    
                    if (trd.getTreatyDetail().isOR()) continue;
                    
                    if(trd.getTreatyDetail().getTreatyType().getStJournalFlag()==null) continue;
                    
                    if(trd.getTreatyDetail().getStARTrxLineIDClaim()==null) continue;
                    
                    final DTOList shares = trd.getShares();
                    
                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                        
                        if (!BDUtil.biggerThanZero(ri.getDbClaimAmount())) continue;
                        
                        final ARInvoiceView invoice = new ARInvoiceView();
                        invoice.markNew();
                        
                        invoice.setStRefID0("REINS/" + ri.getStInsurancePolicyReinsID());
                        //invoice.setStInvoiceNo("E/CLAIM.RI-" + pol.getStPolicyNo());
                        invoice.setStInvoiceNo("N" + trd.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                        //invoice.setDbAmount(pol.getDbAmount());
                        //invoice.setDtInvoiceDate(us.getDtTransactionDate());
                        //invoice.setDtDueDate(us.getDtTransactionDate());
                        invoice.setDtInvoiceDate(pol.getDtDLADate());
                        invoice.setDtDueDate(pol.getDtDLADate());
                        invoice.setDbAmountSettled(null);
                        invoice.setStCurrencyCode(pol.getStCurrencyCode());
                        invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                        invoice.setStPostedFlag("Y");
                        invoice.setStARCustomerID(ri.getStMemberEntityID());
                        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                        invoice.setStEntityID(invoice.getStARCustomerID());
                        invoice.setStCostCenterCode(pol.getStCostCenterCode());
                        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
                        invoice.setStARTransactionTypeID(trd.getTreatyDetail().getARTrxLineClaim().getStARTrxTypeID());
                        invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
                        
                        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                        invoice.setStAttrPolicyName(pol.getStCustomerName());
                        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
                        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
                        invoice.setStAttrPolicyID(pol.getStPolicyID());
                        invoice.setStPolicyID(pol.getStPolicyID());
                        
                        invoice.setStReferenceD0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode5());
                        invoice.setStReferenceD1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStReferenceZ0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode4());
                        invoice.setStReferenceZ1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        invoice.setStRefID1(pol.getStPLANo());
                        invoice.setStRefID2(pol.getStDLANo());
                        
                        //bikin surat hutang
                        if(ri.getDtValidReinsuranceDate()==null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        if(ri.getDtValidReinsuranceDate()!=null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+ "."+
                                    String.valueOf(k+1) +
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }
                        
                        
                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
                        
                        /*invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());*/
                        //finish
                        
                        final DTOList ivdetails = new DTOList();
                        
                        invoice.setDetails(ivdetails);
                        
                        //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
                        
                        final DTOList artlines = ListUtil.getDTOListFromQuery(
                                "select * from ar_trx_line where ar_trx_type_id = ?",
                                new Object[]{invoice.getStARTransactionTypeID()},
                                ARTransactionLineView.class
                                );
                        
                        {
                            
                            for (int v = 0; v < artlines.size(); v++) {
                                ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);
                                
                                if ("KLAIM".equalsIgnoreCase(artl.getStItemClass())) {
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                    
                                    ivd.markNew();
                                    
                                    ivdetails.add(ivd);
                                    
                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(ri.getDbClaimAmount());
                                } else throw new RuntimeException("Unknown Item class : " + artl);
                            }
                        }
                                
                                invoice.recalculate(true);
                                
                                getRemoteAccountReceivable().save(invoice);
                                
                    }
                }
            }
        }
    }
    
    private void postCoas(InsurancePolicyView pol) throws Exception {
        
        //if (!pol.isLeader()) return;
        
        //if (pol.isMember()) return;

        if(pol.isDirect()) return;
        
        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
            
            //boolean notHasPremi = BDUtil.isZero(coin.getDbPremiAmountNet());
            
            //if (!notHasPremi) continue;
            
            if (coin.isHoldingCompany()) continue;
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStRefID0("CO/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/PREMI.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("H" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());*/
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStAPTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());

            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            
            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }
            
            if(!coin.isHoldingCompany()){
                if(!BDUtil.isZeroOrNull(coin.getDbAmount())) invoice.setDbAttrPolicyTSITotal(coin.getDbAmount()); 
                if(coin.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER")){
                    //invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize(coin.getStEntityID())));
                    invoice.setDbAttrPolicyTSITotal(BDUtil.roundUp(BDUtil.mul(pol.getDbCoinsTSI(coin.getStEntityID()), BDUtil.getRateFromPct(pol.getHoldingCoin().getDbSharePct()))));
                }
            }

            //GET NO REKAP OBJEK
            if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    final DTOList rekap = ListUtil.getDTOListFromQuery(
                    "SELECT REF8,REKAP_KREASI "+
                     "   FROM INS_POL_OBJ "+
                     "      WHERE POL_ID = ? "+
                     "      GROUP BY REF8, REKAP_KREASI "+
                     "      ORDER BY REF8::BIGINT, LENGTH(REKAP_KREASI)",
                    new Object[]{pol.getStPolicyID()},
                     HashDTO.class
                    );

                    for (int j = 0; j < rekap.size(); j++) {
                        HashDTO dto = (HashDTO) rekap.get(j);

                        String entity = dto.getFieldValueByFieldNameST("ref8");
                        String rekapNo = dto.getFieldValueByFieldNameST("rekap_kreasi");

                        if(coin.getStEntityID().equalsIgnoreCase(entity)){
                              invoice.setStNoSuratHutang(rekapNo);
                        }

                    }
            }else{
                invoice.setStNoSuratHutang(invoice.generateNoSuratHutangCoas(coin.getStEntityID(),pol));
            }

            //
            
            //bikin surat hutang
            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );
            
            {
                
                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                    
                    BigDecimal premiNetto = null;
                    premiNetto = BDUtil.sub(coin.getDbPremiAmount(), BDUtil.add(BDUtil.add(BDUtil.add(coin.getDbCommissionAmount(), coin.getDbDiscountAmount()), coin.getDbBrokerageAmount()), coin.getDbHandlingFeeAmount()));
                    
                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbPremiAmount());
                        //ivd.setDbEnteredAmount(premiNetto);
                        
                        break;
                    }
                }
            }
                    
                    //CEK KOAS ADA DISKON ATAU TIDAK
                    {
                        
                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                            
                            if ("DISCCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                
                                if (BDUtil.isZeroOrNull(coin.getDbDiscountAmount())) break;
                                
                                ivd.markNew();
                                
                                ivdetails.add(ivd);
                                
                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbDiscountAmount());
                                
                                break;
                            }
                        }
                    }
            //FINISH
            
            //CEK KOAS ADA KOMISI ATAU TIDAK
            {
                
                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                    
                    if ("COMCO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        if (BDUtil.isZeroOrNull(coin.getDbCommissionAmount())) break;
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbCommissionAmount());
                        ivd.setStEntityID(coin.getStEntityID());
                        
                        break;
                    }
                }
            }
                    //FINISH
                    
                    //CEK KOAS ADA BROKERAGE ATAU TIDAK
                    {
                        
                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                            
                            if ("BROKCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                                
                                if (BDUtil.isZeroOrNull(coin.getDbBrokerageAmount())) break;
                                
                                ivd.markNew();
                                
                                ivdetails.add(ivd);
                                
                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbBrokerageAmount());
                                ivd.setStEntityID(coin.getStEntityID());
                                
                                break;
                            }
                        }
                    }
            //FINISH
            
            //CEK KOAS ADA HFEE ATAU TIDAK
            {
                
                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                    
                    if ("HFEECO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        if (BDUtil.isZeroOrNull(coin.getDbHandlingFeeAmount())) break;
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbHandlingFeeAmount());
                        ivd.setStEntityID(coin.getStEntityID());
                        
                        break;
                    }
                }
            }
                    //FINISH
                     
                    invoice.recalculate();
                    
                    getRemoteAccountReceivable().saveInvoiceCoas(invoice);

                    /*
                    final SQLUtil S2 = new SQLUtil();
                    
                    PreparedStatement PS = S2.setQuery("update ins_pol_obj set rekap_kreasi = ? where ref8 = ? and pol_id = ?");

                    //update ref3 dan ref4 ref3 utk JS ref4 selain JS
                    
                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());
                            
                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());

                            int j = PS.executeUpdate();
                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());
                            
                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());
                            
                            int j = PS.executeUpdate();
                        } 
                    }
                    
                    PS.close();
                    S2.release();
                    */
                                       
                    //end

                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());

                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());

                        }
                    }
        }
            
            final SQLUtil S = new SQLUtil();

            try {
                S.store(pol);
            } catch (Exception e) {
                ctx.setRollbackOnly();
                throw e;
            } finally {
                S.release();
            }
            
            

    }

    private void postARCoas(InsurancePolicyView pol) throws Exception {
        
        if (!pol.isLeader()) return;
        
        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
            
            boolean hasClaimAmount = !BDUtil.isZeroOrNull(coin.getDbClaimAmount());
            
            if (!hasClaimAmount) continue;
            
            if (coin.isHoldingCompany()) continue;

            if (coin.isORJiwa()) continue;
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStRefID0("COCL/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/CLAIM.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("L" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            //invoice.setDtInvoiceDate(us.getDtTransactionDate());
            //invoice.setDtDueDate(us.getDtTransactionDate());
            
            invoice.setDtInvoiceDate(pol.getDtDLADate());
            invoice.setDtDueDate(pol.getDtDLADate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStClaimCurrency());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStClaimTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());
            invoice.setStRefID1(pol.getStPLANo());
            invoice.setStRefID2(pol.getStDLANo());

            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));
            
            //bikin surat hutang
            // invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(coin.getStEntityID(),"CO",pol.getStPolicyTypeID()));
            // invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            // invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );
            
            {
                
                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);
                    
                    if ("CLAIMN".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbClaimAmount());
                        
                        break;
                    }
                }
            }
                    
            invoice.recalculate(true);

            getRemoteAccountReceivable().saveInvoiceClaimCoas(invoice);
        }
    }
    
    private void postAR(InsurancePolicyView pol) throws Exception {
        UserSession us = UserSessionMgr.getInstance().getUserSession();
        final DTOList details = pol.getDetails();
        
        final ARInvoiceView invoice = new ARInvoiceView();
        invoice.markNew();
        
        invoice.setStPolicyID(pol.getStPolicyID());
        invoice.setStInvoiceNo("PREMI-" + pol.getStPolicyNo());
        //invoice.setDbAmount(pol.getDbAmount());
        invoice.setDtInvoiceDate(us.getDtTransactionDate());
        invoice.setDtDueDate(us.getDtTransactionDate());
        invoice.setDbAmountSettled(null);
        invoice.setStCurrencyCode(pol.getStCurrencyCode());
        invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
        invoice.setStPostedFlag("N");
        invoice.setStARCustomerID(pol.getStEntityID());
        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
        invoice.setStEntityID(pol.getStEntityID());
        invoice.setStCostCenterCode(pol.getStCostCenterCode());
        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
        invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
        invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
        
        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
        invoice.setStAttrPolicyName(pol.getStCustomerName());
        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
        invoice.setStAttrPolicyID(pol.getStPolicyID());
        
        final DTOList ivdetails = new DTOList();
        
        invoice.setDetails(ivdetails);
        
        //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
        
        final DTOList artlines = ListUtil.getDTOListFromQuery(
                "select * from ar_trx_line where ar_trx_type_id = ?",
                new Object[]{pol.getCoverSource().getStARTransactionTypeID()},
                ARTransactionLineView.class
                );
        
        {
            
            for (int i = 0; i < artlines.size(); i++) {
                ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);
                
                if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                    
                    ivd.markNew();
                    
                    ivdetails.add(ivd);
                    
                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                    ivd.loadSettings();
                    //ivd.setStDescription(politem.getStDescription());
                    ivd.setDbEnteredAmount(pol.getDbPremiTotal());
                    
                    break;
                }
            }
        }
                
                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                    
                    final InsuranceItemsView insuranceItem = politem.getInsuranceItem();
                    
                    //if (insuranceItem.isPremi()) {
                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                    
                    ivd.markNew();
                    
                    ivdetails.add(ivd);
                    
                    ivd.setStEntityID(politem.getStEntityID());
                    
                    ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                    ivd.loadSettings();
                    //ivd.setStDescription(politem.getStDescription());
                    ivd.setDbEnteredAmount(politem.getDbAmount());
                    //ivd.setStGLAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLRevenue()));
                    
                    ivd.setDbTaxRate(politem.getDbTaxRate());
                    ivd.setDbTaxAmount(politem.getDbTaxAmount());
                    
                    if (ivd.isComission())
                        ivd.setStTaxCodeOnSettlement(politem.getStTaxCode());
                    else
                        ivd.setStTaxCode(politem.getStTaxCode());
                    
                    //}
                }
                
                invoice.recalculate();
                
                getRemoteAccountReceivable().save(invoice);
    }
    
    private void postARInstallmentAcrualBases(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
         
        final DTOList details = pol.getDetails();
        
        final DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
             */
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            //if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));
            
            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);
            
            invoice.setStPostedFlag("Y");
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );
            
            boolean premiGFound = false;
            
            {
                
                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);
                    
                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
                        
                        final BigDecimal amt =
                                installmentPeriod == null ?
                                    premi :
                                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);
        
                        premiGFound = true;
                        
                        if(BDUtil.isZeroOrNull(amt)) continue;
                            
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());
                        
                        ivdetails.add(ivd);
                        
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                                        
                        ivd.setDbEnteredAmount(amt);

                        break;
                    }
                }
            }
            
            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);
            
            BigDecimal amt = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                
                if (politem.isFee())
                    if ((politem.isPolicyCost() || politem.isStampFee()) && installmentSeq > 0) continue;
                
                boolean posting = true;
                
                if(BDUtil.isZeroOrNull(politem.getDbAmount())) posting = false;
                if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;
                
                if(!posting) continue;
                
                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();
                
                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                
                ivd.markNew();
                
                ivdetails.add(ivd);
                
                ivd.setStEntityID(politem.getStEntityID());
                
                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());
                
                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());
                
                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
                    else amt = politem.getDbNetAmount();
                }
                
                if (politem.isComission() || politem.isDiscount() || politem.isPPN() || politem.isPPNFeeBase())
                    amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);
                
                ivd.setDbEnteredAmount(amt);
                
                if (ivd.isComission()&&politem.getStTaxCode()!=null)
                    ivd.setStTaxCode(politem.getStTaxCode());

                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;
                    
                    //if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) continue;
                        
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
                    
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();
                    
                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);
                    
                    ivdc.setDbEnteredAmount(taxamt);
                    
                    ivdc.setStTaxCode(stTaxCode);
                    
                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }

            }
            
            invoice.recalculate();
            
            getRemoteAccountReceivable().save(invoice);
            
            
        }

            postARTaxAcrual(pol);

    }
    
    private void postARInstallment2(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = pol.getDetails();
        
        final DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStPolicyID(pol.getStPolicyID());
            invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            
            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);
            
            invoice.setStPostedFlag("Y");
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );
            
            boolean premiGFound = false;
            
            {
                
                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);
                    
                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());
                        
                        ivdetails.add(ivd);
                        
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
                        
                        /*
                        final BigDecimal amt =
                                installmentPeriod==null?
                                premi:
                                installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);
                         */
                        BigDecimal amt = null;
                        BigDecimal totalComission = null;
                        BigDecimal totalTax = null;
                        for (int p = 0; p < details.size(); p++) {
                            InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(p);
                            
                            if (politem.isComission()) ;
                            else continue;
                            
                            totalComission = BDUtil.add(totalComission, politem.getDbNetAmount());
                            
                            if (politem.getStTaxCode() != null) {
                                totalTax = BDUtil.add(totalTax, politem.getDbTaxAmount());
                            }
                        }
                        if (installmentSeq == 0) amt = BDUtil.sub(pol.getDbTotalDisc(), pol.getDbTotalFee());
                        else if ((installmentSeq + 1) == installment.size()) amt = BDUtil.add(totalComission, totalTax);
                        ivd.setDbEnteredAmount(BDUtil.add(inst.getDbAmount(), amt));
                        
                        premiGFound = true;
                        
                        break;
                    }
                }
            }
            
            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);
            
            BigDecimal amt = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                
                if (politem.isFee() || politem.isDiscount())
                    if (installmentSeq > 0) continue;
                
                if (politem.isComission())
                    if ((installmentSeq + 1) != installment.size()) continue;
                
                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();
                
                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                
                ivd.markNew();
                
                ivdetails.add(ivd);
                
                ivd.setStEntityID(politem.getStEntityID());
                
                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());
                
                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());
                
                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    amt = politem.getDbNetAmount();
                }
                
                //if (politem.isComission() || politem.isDiscount()) amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);
                if (politem.isComission() || politem.isDiscount()) amt = politem.getDbNetAmount();
                
                ivd.setDbEnteredAmount(amt);
                
                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");
                    
                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
                    
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    
                    BigDecimal taxamt = politem.getDbTaxAmount();
                    
                    //if (politem.isComission() || politem.isDiscount()) taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);
                    
                    ivdc.setDbEnteredAmount(taxamt);
                    
                    //ivdc.setStTaxCode(stTaxCode);
                    
                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }
            }
            
            invoice.recalculate();
            
            getRemoteAccountReceivable().save(invoice);
        }
    }
    
    private void postARInstallmentFree(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
        
        int scale;
        if (!pol.getStCurrencyCode().equalsIgnoreCase("IDR")) scale = 2;
        else scale = 0;
        
        final DTOList details = pol.getDetails();
        
        final DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));

            //invoice.setDbAmount(pol.getDbAmount());
            //invoice.setDtInvoiceDate(us.getDtTransactionDate());
            //invoice.setDtDueDate(us.getDtTransactionDate());
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));
            
            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);
            
            invoice.setStPostedFlag("Y");
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );
            
            boolean premiGFound = false;
            
            {
                
                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);
                    
                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());
                        
                        ivdetails.add(ivd);
                        
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
                        
                        BigDecimal amt = null;
                        BigDecimal amt2 = null;
                        BigDecimal totalAmt = null;
                        BigDecimal totalTax = null;
                        BigDecimal tax = null;
                        BigDecimal tax2 = null;
                        for (int p = 0; p < details.size(); p++) {
                            InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(p);
                            
                            
                            if (politem.getStTaxCode() == null) {
                                amt = politem.getDbAmount();
                            } else {
                                amt = politem.getDbNetAmount();
                                tax = politem.getDbTaxAmount();
                            }
                            
                            amt2 = BDUtil.mulRound(BDUtil.div(amt, premi, 15), inst.getDbAmount(), scale);
                            
                            if (politem.getStTaxCode() != null)
                                tax2 = BDUtil.mulRound(BDUtil.div(tax, premi, 15), inst.getDbAmount(), scale);
                            
                            totalAmt = BDUtil.add(BDUtil.add(totalAmt, amt2), tax2);
                        }
                        
                        ivd.setDbEnteredAmount(BDUtil.sub(inst.getDbAmount(), totalAmt));
                        
                        premiGFound = true;
                        
                        break;
                    }
                }
            }
            
            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);
            
            BigDecimal amt3 = null;
            BigDecimal item = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                
                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();
                
                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                
                ivd.markNew();
                
                ivdetails.add(ivd);
                
                ivd.setStEntityID(politem.getStEntityID());
                
                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());
                
                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                
                
                if (politem.getStTaxCode() == null) {
                    amt3 = politem.getDbAmount();
                } else {
                    amt3 = politem.getDbNetAmount();
                }
                
                item = BDUtil.mulRound(BDUtil.div(amt3, premi, 15), inst.getDbAmount(), scale);
                
                ivd.setDbEnteredAmount(item);
                
                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                    //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");
                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());
                    
                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
                    
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    
                    BigDecimal taxamt = politem.getDbTaxAmount();
                    BigDecimal tax = null;
                    
                    tax = BDUtil.mulRound(BDUtil.div(taxamt, pol.getDbPremiTotal(), 15), inst.getDbAmount(), scale);
                    
                    ivdc.setDbEnteredAmount(tax);
                    
                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }
            }
            
            invoice.recalculate();
            
            getRemoteAccountReceivable().save(invoice);
        }
    }
    
    
    private InsurancePolicyTypeView getInsurancePolicyType(String stPolicyTypeID) throws Exception {
        return
                (InsurancePolicyTypeView) ListUtil.getDTOListFromQuery(
                "select * from ins_policy_types where pol_type_id = ?",
                new Object[]{stPolicyTypeID},
                InsurancePolicyTypeView.class
                ).getDTO();
    }
    
    public HashMap getInsuranceItemsMap() throws Exception {
        final DTOList l = ListUtil.getDTOListFromQuery(
                "select * from ins_items",
                InsuranceItemsView.class
                );
        
        final HashMap map = new HashMap();
        
        for (int i = 0; i < l.size(); i++) {
            InsuranceItemsView ii = (InsuranceItemsView) l.get(i);
            map.put(ii.getStInsuranceItemID(), ii);
        }
        
        return map;
    }
    
    public LOV getInsuranceTypesLOV() throws Exception {
        
        final LookUpUtil l = ListUtil.getLookUpFromQuery("select pol_type_id, description from ins_policy_types");
        
        return l;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    public DTOList searchAgents(EntityFilter f) throws Exception {
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect(
                "   a.ent_id, a.ent_name, a.tax_file,a.tax_code"
                );
        
        sqa.addQuery(
                "     from" +
                "         ent_master a"
                );
        
        if (f.stKey != null) {
            
            sqa.addClause(
                    "upper(a.ent_name) like ?"
                    );
            
            sqa.addPar('%' + f.stKey.toUpperCase() + '%');
        }
        
        if (f.stCompanyGroup != null) {
            
            sqa.addClause(
                    "upper(a.ref1) = ?"
                    );
            
            sqa.addPar(f.stCompanyGroup.toUpperCase());
        }

        sqa.addClause("coalesce(a.active_flag,'Y') <> 'N'");
        
        sqa.addFilter(f);
        sqa.setLimit(100);
        
        return sqa.getList(EntityView.class);
        
        /*ListUtil.getDTOListFromQuery(
                "   select" +
                "      from " +
                "         ent_master a" +
                "         inner join ins_entity b on b.ent_id = a.ent_id" +
                "      where" +
                "         a.name like ? and b.ins_entity_type"
        )*/
        
    }
    
    public LOV getInsuranceSubTypesLOV(String stPolicyType) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_policy_subtype where pol_type_id = ? order by description",
                new Object[]{stPolicyType},
                InsurancePolicySubTypeView.class
                );
    }
    
    public DTOList getClausules(String stPolicyTypeID, String stPolicySubTypeID) throws Exception {
        
        final DTOList masterClausules = ListUtil.getDTOListFromQuery(
                "select * from ins_clausules where pol_type_id = ?",
                new Object[]{stPolicyTypeID},
                InsurancePolicyClausulesView.class
                );
        
        if (stPolicySubTypeID != null) {
            
            final HashMap masterClausulesMap = new HashMap();
            
            for (int i = 0; i < masterClausules.size(); i++) {
                InsurancePolicyClausulesView ic = (InsurancePolicyClausulesView) masterClausules.get(i);
                
                masterClausulesMap.put(ic.getStClauseID(), ic);
            }
            
            final DTOList subClausules = ListUtil.getDTOListFromQuery(
                    "select * from ins_polsubtype_clause where pol_subtype_id = ?",
                    new Object[]{stPolicySubTypeID},
                    InsurancePolSubTypeClausulesView.class
                    );
            
            for (int i = 0; i < subClausules.size(); i++) {
                InsurancePolSubTypeClausulesView ist = (InsurancePolSubTypeClausulesView) subClausules.get(i);
                
                if (FinCodec.ClauseSelectType.SELECTED.equalsIgnoreCase(ist.getStSelectType())) {
                    final InsurancePolicyClausulesView ic = (InsurancePolicyClausulesView) masterClausulesMap.get(ist.getStInsuranceClauseID());
                    
                    ic.select();
                }
            }
        }
        
        return masterClausules;
    }
    
    public LOV getBusinessSourceLOV() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code,vs_description from s_valueset where vs_group = ?",
                new Object[]{"ASK_BUS_SOURCE"}
        );
    }
    
    public LOV getRegionLOV() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select region_id, description from s_region where active_flag = 'Y' order by description"
                );
    }
    
    public DTOList getInsuranceItemLOV(String stCoverTypeCode) throws Exception {
        return
                ListUtil.getDTOListFromQuery(
                "select * from ins_items where ins_item_class = 'PREMI_" + stCoverTypeCode + "' order by ins_item_id",
                new Object[]{},
                InsuranceItemsView.class
                );
        
    }
    /*
    public InsurancePolicyView getInsurancePolicyForPrinting(String policyid, String alter) throws Exception {
        InsurancePolicyView policy = getInsurancePolicy(policyid);
        
        boolean mayPrint = false;
        if(Tools.isYes(policy.getStEffectiveFlag())) mayPrint = true;
        if(policy.isStatusClaimDLA() && alter.equalsIgnoreCase("claimlks")) mayPrint = true;
        
        if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("8")){
            if(policy.isStatusPolicy() && alter.equalsIgnoreCase("prinsip")) mayPrint = true;
        }

        if(!mayPrint)
            throw new RuntimeException(" Data belum disetujui, hanya bisa di preview");
        
        policy.markUpdate();
        
        if(policy.getStPrintCode()==null){
               policy.setStPrintCode(StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PCD")), '0', 10));
        }
        
        String k;


        String pz = policy.getStPrintCode() + "/" + policy.getStPolicyNo() + "/" + policy.getStPolicyID();

        k = Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes(pz)));
        
        policy.setStPrintStamp(k);

        String counter = null;

        if(alter!=null)
            if(alter.equals("STANDARD")||alter.equals("lamprestitusi")){
                policy.setStDocumentPrintFlag("Y");
                counter = policy.calculateRenewalCounter(policy.getStPrintCounter());
            }
                
        
        final SQLUtil S = new SQLUtil();
        
        try {
            
            String update = "update ins_policy set print_code = ?,print_stamp = ?,document_print_flag=?, sign_code=? ";

            if(counter!=null){
                update = update + " , print_counter=? ";
            }

            update = update + " where pol_id=?";

            PreparedStatement P = S.setQuery(update);
            P.setObject(1, policy.getStPrintCode());
            P.setObject(2, k);
            P.setObject(3, policy.getStDocumentPrintFlag());
            P.setObject(4, policy.getStSignCode());

            if(counter!=null){
                P.setObject(5, counter );
                P.setObject(6, policyid);
            }else{
                P.setObject(5, policyid);
            }
            
            int r = P.executeUpdate();
            
        } finally {
            S.release();
        }
        
        policy = getInsurancePolicy(policyid);
        
        return policy;
    }*/
    
    public InsurancePolicyView getInsurancePolicyForPreview(String policyid) throws Exception {
        //InsurancePolicyView policy = getInsurancePolicy(policyid);

        InsurancePolicyView policy = new InsurancePolicyView();
        
        return policy;
    }
    
    public void reActivate(String parentPolicy) throws Exception {
        InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, parentPolicy);
        
        pol.markUpdate();
        
        pol.setStEffectiveFlag("Y");
        pol.setStActiveFlag("Y");
        
        SQLUtil S = new SQLUtil();
        
        try {
            S.store(pol);
        } finally {
            S.release();
        }
    }
    
    public void saveAndReverse(InsurancePolicyView policy) throws Exception {
        
        if (Tools.isEqual(policy.getStPolicyID(), policy.getStParentID()))
            throw new RuntimeException("Reverse not necessary ! ");
        
        //save(policy, null, false);
        
        //reActivate(policy.getStParentID());
    }
    
    public void registerPrintSerial(InsurancePolicyView policy, String nom, String urx) throws Exception {
        /*DTOList l = ListUtil.getDTOListFromQuery(
               "select * from ins_prt_log where serial_number = ?",
         
               new Object [] {nom},
               InsurancePrintLogView.class
       );
         
 int n = l.size();
         
 if (n>0) throw new RuntimeException("Nomerator has been used");*/
        
        final DTOList l = ListUtil.getDTOListFromQuery(
                "select * from ins_pol_nomerator where ins_pol_nomerator_id=?",
                new Object[]{nom},
                InsurancePolicyNomeratorView.class
                );
        
        if (l.size() != 1) {
            throw new RuntimeException("Not a valid nomerator");
        }
        
        final InsurancePolicyNomeratorView n = (InsurancePolicyNomeratorView) l.get(0);
        
        if (n.getStPolicyID() != null) throw new RuntimeException("Nomerator has been used");
        
        final boolean sameCC = Tools.isEqual(n.getStCCCode(), policy.getStCostCenterCode());
        
        if (!sameCC) throw new RuntimeException("Nomerator is not valid for this branch");
        
        InsurancePrintLogView ipr = new InsurancePrintLogView();
        
        ipr.markNew();
        
        ipr.setStInsurancePrintLogID(String.valueOf(IDFactory.createNumericID("IPRL")));
        
        ipr.setStPolicyID(policy.getStPolicyID());
        ipr.setStSerialNumber(nom);
        ipr.setStPrintType(urx);
        
        SQLUtil S = new SQLUtil();
        
        try {
            S.store(ipr);
            
            n.markUpdate();
            n.setStPolicyID(policy.getStPolicyID());
            
            S.store(n);
            
        } finally {
            S.release();
        }
    }
    
    public void save(InsuranceTreatyView tre) throws Exception {
        SQLUtil S = new SQLUtil();
        
        try {
            if (tre.isNew())
                tre.setStInsuranceTreatyID(String.valueOf(IDFactory.createNumericID("INS_TREATY")));
            
            S.store(tre);
            
            DTOList details = tre.getDetails();
            
            
            DTOList deletedDetails = details.getDeleted();
            
            if (deletedDetails != null)
                for (int i = 0; i < deletedDetails.size(); i++) {
                    InsuranceTreatyDetailView d = (InsuranceTreatyDetailView) deletedDetails.get(i);

                    d.getShares().deleteAll();

                    //S.store2(d.getShares());
                    S.store(d.getShares());
                }
            
            for (int i = 0; i < details.size(); i++) {
                InsuranceTreatyDetailView d = (InsuranceTreatyDetailView) details.get(i);
                
                if (d.isNew())
                    d.setStInsuranceTreatyDetailID(String.valueOf(IDFactory.createNumericID("INS_TRE_DET")));
                
                d.setStInsuranceTreatyID(tre.getStInsuranceTreatyID());
                
                DTOList shares = d.getShares();
                
                for (int j = 0; j < shares.size(); j++) {
                    InsuranceTreatySharesView ts = (InsuranceTreatySharesView) shares.get(j);
                    
                    if (ts.isNew())
                        ts.setStInsuranceTreatySharesID(String.valueOf(IDFactory.createNumericID("INS_TRE_SHAREZ_ID")));
                    
                    ts.setStInsuranceTreatyDetailID(d.getStInsuranceTreatyDetailID());
                }
                
                //S.store2(shares);
                S.store(shares);
            }
            //S.store2(details);
            S.store(details);
            
        } catch (Exception e) {
            
            ctx.setRollbackOnly();
            
            throw e;
            
        } finally {
            S.release();
        }
    }
    
    public void save(InsuranceZoneLimitView zone) throws Exception {
        SQLUtil S = new SQLUtil();
        
        try {
            if (zone.isNew())
                zone.setStZoneID(String.valueOf(IDFactory.createNumericID("INS_ZONE_LIMIT")));
            
            S.store(zone);
            
        } catch (Exception e) {
            
            ctx.setRollbackOnly();
            
            throw e;
            
        } finally {
            S.release();
        }
    }
    
    public CoverNoteView getInsurancePolicy2(String stPolicyID) throws Exception {
        final CoverNoteView pol = (CoverNoteView) ListUtil.getDTOListFromQuery("select * from ins_policy where pol_id = ?",
                new Object[]{stPolicyID},
                CoverNoteView.class).getDTO();
        
        return pol;
    }
    
    public DTOList searchZone(ZoneFilter f) throws Exception {
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect(
                "   a.zone_id, a.description, a.limit1"
                );
        
        sqa.addQuery(
                "     from" +
                "         ins_zone_limit a"
                );
        
        if (f.stKey != null) {
            
            sqa.addClause(
                    "upper(a.description) like ?"
                    );
            
            sqa.addPar('%' + f.stKey.toUpperCase() + '%');
        }
        
        sqa.addFilter(f);
        
        return sqa.getList(InsuranceZoneLimitView.class);
        
        /*ListUtil.getDTOListFromQuery(
                "   select" +
                "      from " +
                "         ent_master a" +
                "         inner join ins_entity b on b.ent_id = a.ent_id" +
                "      where" +
                "         a.name like ? and b.ins_entity_type"
        )*/
        
    }
    
    public void postLimitZoneBalance(InsurancePolicyView policy) throws Exception {
        try {
            final DTOList cover = policy.getCoverage2();
            final SQLUtil S = new SQLUtil();
            PreparedStatement PS;
            BigDecimal saldo = null;
            BigDecimal saldoZone = null;
            String zoneID = null;
            String zoneDesc = null;
            
            for (int i = 0; i < cover.size(); i++) {
                InsurancePolicyCoverView polisCover = (InsurancePolicyCoverView) cover.get(i);
                
                zoneID = polisCover.getStZoneID();
                zoneDesc = polisCover.getStDescription();
                
                if (zoneID != null)
                    saldoZone = getZoneLimitBalance(polisCover.getStZoneID());
            }
            
            if (zoneID != null) {
                if (saldoZone != null) {
                    String sql = "update ins_pol_zone set amount = amount+?, change_who=?, change_date=? where zone_id=?;";
                    PS = S.setQuery(sql);
                    final UserSession us = S.getUserSession();
                    S.setParam(1, policy.getDbInsuredAmount());
                    S.setParam(2, us.getStUserID());
                    S.setParam(3, us.getDtTransactionDate());
                    S.setParam(4, zoneID);
                    int i = PS.executeUpdate();
                } else {
                    InsurancePolicyZoneView zone = new InsurancePolicyZoneView();
                    zone.markNew();
                    
                    zone.setStInsurancePolicyZoneID(String.valueOf(IDFactory.createNumericID("INSPOLZONE")));
                    zone.setStZoneID(zoneID);
                    zone.setStDescription(zoneDesc);
                    zone.setDbAmount(policy.getDbInsuredAmount());
                    
                    S.store(zone);
                    S.release();
                }
            }
            
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
        }
        
    }
    
    public BigDecimal getZoneLimitBalance(String zoneid) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            S.setQuery(
                    "   select " +
                    "      amount " +
                    "   from " +
                    "         ins_pol_zone b " +
                    "   where" +
                    "      b.zone_id=?");
            
            S.setParam(1, zoneid);
            
            final ResultSet RS = S.executeQuery();
            
            if (RS.next()) return RS.getBigDecimal(1);
            
            return null;
        } finally {
            
            S.release();
        }
    }
    
    public InsurancePolicyZoneView getInsurancePolZone(String stInsuransePolicyZoneID) {
        return (InsurancePolicyZoneView) DTOPool.getInstance().getDTO(InsurancePolicyZoneView.class, stInsuransePolicyZoneID);
    }
    
    public void reverse(InsurancePolicyView pol) throws Exception {
        final SQLUtil S = new SQLUtil();
        /*
         *update ins_policy set posted_flag = null,effective_flag='N' where status = 'POLICY' and pol_id=;
          delete from ins_policy where status = 'CLAIM' and pol_id=;
         
          delete from ar_invoice where attr_pol_id=;
          delete from ar_invoice_details where ar_invoice_id=;
         
          delete from ar_receipt where ar_ap_invoice_id =;
          delete from ar_receipt_lines where ar_invoice_id=;
         
          delete from gl_je_detail where trx_no = ;
         
          delete from gl_acct_bal;
         */

        boolean prosesKoas = false;

        try {

            
            final DTOList pembayaran = pol.getArinvoices();

            if(!prosesKoas){
                if(pembayaran.size() > 0){
                    ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

                    if(inv.getDbAmountSettled()!=null)
                        if(inv.getDtPaymentDate()!=null)
                            throw new RuntimeException("Polis tidak bisa di reverse, sudah dibayar premi nya");
                }
            }

            String update = "";
            if(!prosesKoas) update = "update ins_policy set posted_flag = null,effective_flag='N',approved_who=null,approved_date=null,password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?";
            if(prosesKoas) update = "update ins_policy set posted_flag = null,effective_flag='N',password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?";

            //update = "update ins_policy set admin_notes=? where pol_id=?";

            PreparedStatement  P = S.setQuery(update);

            if(!prosesKoas) P.setObject(1, "REVERSE BY : "+ UserManager.getInstance().getUser().getStUserID());
            if(prosesKoas) P.setObject(1, "REVERSE PROSES KOAS SEPTEMBER : "+ UserManager.getInstance().getUser().getStUserID());

            P.setObject(2, pol.getStPolicyID());
            int r = P.executeUpdate();
            S.release();
            
            /*
            PreparedStatement P2 = S.setQuery("delete from ins_pol_obj_searching where pol_id=?");
            P2.setObject(1, pol.getStPolicyID());
            int r2 = P2.executeUpdate();
            S.release();*/

            DTOList invoice = new DTOList();

            if(prosesKoas) invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolIdCoasOnly(pol.getStPolicyID());

            if(!prosesKoas) invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolId(pol.getStPolicyID());
            
            for (int i = 0; i < invoice.size(); i++) {
                ARInvoiceView invoiceView = (ARInvoiceView) invoice.get(i);

                final DTOList details = invoiceView.getDetails();
                
                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(invoiceView.getDtInvoiceDate());
                if (per == null)
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + invoiceView.getDtInvoiceDate() + "Salah)");
                if (!per.isOpen()) throw new Exception("Period is not open");
                
                //update gl_acct_bal per invoice
                Long accountid = Long.valueOf(invoiceView.getStGLARAccountID());
                
                BigDecimal bal = getRemoteGeneralLedger().getBalance(accountid, per.getLgFiscalYear(), per.getLgPeriodNo(), invoiceView.getDbAmount());
                BigDecimal amt = invoiceView.getDbAmount();
                if (BDUtil.biggerThanZero(bal))
                    amt = BDUtil.mul(amt, new BigDecimal(-1));
                
                getRemoteGeneralLedger().updateBalance(accountid, per.getLgFiscalYear(), per.getLgPeriodNo(), amt);
                
                //cari gl_acct bal berdasarkan account id dr invoice_detail dan update saldo
                
                if(details.size()>0){
                    for (int j = 0; j < details.size(); j++) {
                        ARInvoiceDetailView detailView = (ARInvoiceDetailView) details.get(j);
                        
                        //update gl_acct_bal per detail_invoice
                        Long accountid2 = Long.valueOf(detailView.getStGLAccountID());
                        BigDecimal bal2 = getRemoteGeneralLedger().getBalance(accountid2, per.getLgFiscalYear(), per.getLgPeriodNo(), detailView.getDbAmount());
                        BigDecimal amt2 = detailView.getDbAmount();
                        if (BDUtil.biggerThanZero(bal2))
                            amt2 = BDUtil.mul(amt2, new BigDecimal(-1));
                        
                        
                        getRemoteGeneralLedger().updateBalance(accountid2, per.getLgFiscalYear(), per.getLgPeriodNo(), amt2);
                    }
                    
                    details.deleteAll();
                    S.storeDeleteNormal(details);
                }
                
                final DTOList receipt = getRemoteAccountReceivable().getReceiptByARInvoiceId(invoiceView.getStARInvoiceID());
                
                for (int k = 0; k < receipt.size(); k++) {
                    ARReceiptView receiptView = (ARReceiptView) receipt.get(k);
                    
                    final DTOList receiptLines = receiptView.getDetails();
                    receiptLines.deleteAll();
                    
                    S.storeDeleteNormal(receiptLines);
                    
                    PreparedStatement P5 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                    P5.setObject(1, receiptView.getStARReceiptID());
                    int t = P5.executeUpdate();
                    S.release();
                }
                receipt.deleteAll();
                S.storeDeleteNormal(receipt);
                
                for (int k = 0; k < receipt.size(); k++) {
                    ARReceiptView receiptView = (ARReceiptView) receipt.get(k);
                    
                    PreparedStatement P5 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                    P5.setObject(1, receiptView.getStARReceiptID());
                    int t = P5.executeUpdate();
                    S.release();
                }
                
                
                
                PreparedStatement P3 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                P3.setObject(1, invoiceView.getStARInvoiceID());
                int t = P3.executeUpdate();
                S.release();
                
                //details.deleteAll();
            }
            
            invoice.deleteAll();
            S.storeDeleteNormal(invoice);
            
            final DTOList produk = ListUtil.getDTOListFromQuery(
                    "select * from aba_produk where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceProdukView.class);
            
            produk.deleteAll();
            S.storeDeleteNormal(produk);
            
            final DTOList bayar = ListUtil.getDTOListFromQuery(
                    "select * from aba_bayar1 where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceBayar1View.class);
            
            bayar.deleteAll();
            S.storeDeleteNormal(bayar);
            
            final DTOList hutang = ListUtil.getDTOListFromQuery(
                    "select * from aba_hutang where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceHutangView.class);
            
            hutang.deleteAll();
            S.storeDeleteNormal(hutang);
            
            final DTOList pajak = ListUtil.getDTOListFromQuery(
                    "select * from aba_pajak where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsurancePajakView.class);
            
            pajak.deleteAll();
            S.storeDeleteNormal(pajak);
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
        
        
    }
    
    public void postCoasByDate(Date dateFrom, Date dateTo) throws Exception {
        try {
            DTOList listPolicy = null;
            listPolicy = ListUtil.getDTOListFromQuery(
                    "select * from ins_policy where policy_date >= ? and policy_date <=? and pol_type_id = 21 and effective_flag = 'Y' and status in('POLICY','ENDORSE','RENEWAL')",
                    new Object[]{dateFrom, dateTo},
                    InsurancePolicyView.class
                    );
            
            for (int i = 0; i < listPolicy.size(); i++) {
                InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);
                
                postCoas(policy);
            }
        } catch (Exception e) {
            ctx.setRollbackOnly();
        }
        
    }
    
    public void saveABAProduk(InsurancePolicyView pol) throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList produk2 = new DTOList();

        String nopol="";

        if(pol.isStatusPolicy()||pol.isStatusRenewal()){
            nopol = pol.getStPolicyNo().substring(0,16);
        }else if(pol.isStatusEndorse()){
            nopol = pol.getPolicyNoOldFormat(pol.getStPolicyNo());
        }

        //final String policyType2Digit = StringTools.leftPad(pol.getPolicyType().getStOldPolicyTypeID(), '0', 2);
        //nopol = pol.getStPolicyNo().substring(0,2) + policyType2Digit + pol.getStPolicyNo().substring(4,16);

        final DTOList coin = pol.getCoins3();

        final String coinsEntityID = Parameter.readString("UWRIT_CURRENT_COMPANY");

        for(int i=0;i<coin.size();i++){
            InsurancePolicyCoinsView coins = (InsurancePolicyCoinsView) coin.get(i);

            if(coins.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER") && coins.getStEntityID().equalsIgnoreCase(coinsEntityID)) continue;

            InsuranceProdukView produk = new InsuranceProdukView();

            produk.markNew();

            produk2.add(produk);

            produk.setStPolicyID(pol.getStPolicyID());

            produk.setStInsuranceNoPolis(nopol);
            produk.setDtTglPolDate(pol.getDtPolicyDate());
            produk.setStInsuranceNama(pol.getStCustomerName());
            if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {
                final DTOList object = pol.getObjects();
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object.get(0);
                produk.setStInsuranceNama(obj.getStReference1()+" "+pol.getStCustomerName()+" "+pol.getStProducerName());
            }
            produk.setDbHargaPertanggungan(BDUtil.mul(pol.getDbCurrencyRate(),pol.getDbInsuredAmount()));
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbHargaPertanggungan(BDUtil.zero);
            produk.setDbPremiBruto(BDUtil.mul(pol.getDbPremiTotal(),pol.getDbCurrencyRate()));
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbPremiBruto(BDUtil.mul(pol.getDbCurrencyRate(),BDUtil.mul(coins.getDbPremiAmount(),new BigDecimal(-1))));
            produk.setDbBiayaPolis(pol.getDbNDPCost()==null?BDUtil.zero:BDUtil.mul(pol.getDbCurrencyRate(),pol.getDbNDPCost()));
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbBiayaPolis(BDUtil.zero);
            produk.setDbBiayaMaterai(pol.getDbNDSFee()==null?BDUtil.zero:BDUtil.mul(pol.getDbCurrencyRate(),pol.getDbNDSFee()));
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbBiayaMaterai(BDUtil.zero);

            produk.setDbFeeBase(BDUtil.add(pol.getDbNDFeeBase1(),pol.getDbNDFeeBase2()));
            produk.setDbFeeBase(BDUtil.mul(pol.getDbCurrencyRate(),produk.getDbFeeBase()));
            produk.setDbFeeBase(produk.getDbFeeBase()==null?BDUtil.zero:produk.getDbFeeBase());
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbFeeBase(BDUtil.zero);

            //produk.setDbKomisi(BDUtil.add(produk.getDbKomisi(),pol.getDbTotalFeeBase()));
            produk.setDbKomisi(BDUtil.add(pol.getDbNDComm1(),pol.getDbNDComm2()));
            produk.setDbKomisi(BDUtil.add(produk.getDbKomisi(),pol.getDbNDComm3()));
            produk.setDbKomisi(BDUtil.add(produk.getDbKomisi(),pol.getDbNDComm4()));
            produk.setDbKomisi(BDUtil.mul(pol.getDbCurrencyRate(),produk.getDbKomisi()));
            produk.setDbKomisi(BDUtil.add(produk.getDbKomisi(),produk.getDbFeeBase()));
            produk.setDbKomisi(produk.getDbKomisi()==null?BDUtil.zero:produk.getDbKomisi());
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbKomisi(BDUtil.mul(pol.getDbCurrencyRate(),BDUtil.mul(coins.getDbCommissionAmount(),new BigDecimal(-1))));

            produk.setDtTanggalMulaiDate(pol.getDtPeriodStart());
            produk.setDtTanggalAkhirDate(pol.getDtPeriodEnd());
            produk.setStRelasi(pol.getStProducerID());
            produk.setDtTanggalEntry(pol.getDtApprovedDate());
            produk.setStKali(pol.getStInstallmentPeriods());
            produk.setDbDollar(pol.getDbCurrencyRate());

            produk.setStKodeKoasuransi(pol.getEntity2(coins.getStEntityID()).getStRefEntityID());
            if(produk.getStKodeKoasuransi()==null)
                produk.setStKodeKoasuransi(coins.getStEntityID());

            produk.setDbDiskon(BDUtil.add(pol.getDbNDDisc1(),pol.getDbNDDisc2()));
            produk.setDbDiskon(BDUtil.mul(pol.getDbCurrencyRate(),produk.getDbDiskon()));
            produk.setDbDiskon(produk.getDbDiskon()==null?BDUtil.zero:produk.getDbDiskon());
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbDiskon(BDUtil.mul(pol.getDbCurrencyRate(),BDUtil.mul(coins.getDbDiscountAmount(),new BigDecimal(-1))));

            produk.setDbBrokerageFee(BDUtil.add(pol.getDbNDBrok1(),pol.getDbNDBrok2()));
            produk.setDbBrokerageFee(BDUtil.mul(pol.getDbCurrencyRate(),produk.getDbBrokerageFee()));
            produk.setDbBrokerageFee(produk.getDbBrokerageFee()==null?BDUtil.zero:produk.getDbBrokerageFee());
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbBrokerageFee(BDUtil.mul(pol.getDbCurrencyRate(),BDUtil.mul(coins.getDbBrokerageAmount(),new BigDecimal(-1))));

            produk.setDbHandlingFee(BDUtil.mul(pol.getDbCurrencyRate(),pol.getDbNDHFee()));
            produk.setDbHandlingFee(produk.getDbHandlingFee()==null?BDUtil.zero:produk.getDbHandlingFee());
            if(!coins.getStEntityID().equalsIgnoreCase(coinsEntityID))
                produk.setDbHandlingFee(BDUtil.mul(pol.getDbCurrencyRate(),BDUtil.mul(coins.getDbHandlingFeeAmount(),new BigDecimal(-1))));

            produk.setStEntityID(pol.getStEntityID());
            produk.setDbPersenKoasuransi(coins.getDbSharePct());
            produk.setStInsuranceNoPolisWeb(pol.getStPolicyNo());
            produk.setStInsuranceNoPolisLama(pol.getStParentPolicyNo());
            produk.setStInsuranceNoPolisLamaCounter(pol.getStRenewalCounter());

        }

        S.store(produk2);
        S.release();
    }
    
    public void saveABABayar(InsurancePolicyView pol) throws Exception{
        final SQLUtil S = new SQLUtil();

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList installment = pol.getInstallment();

        final BigDecimal premi = pol.getDbPremiTotal();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        String nopol="";

        if(pol.isStatusPolicy()||pol.isStatusRenewal()){
            nopol = pol.getStPolicyNo().substring(0,16);
        }else if(pol.isStatusEndorse()){
            nopol = pol.getPolicyNoOldFormat(pol.getStPolicyNo());
        }

        //final String policyType2Digit = StringTools.leftPad(pol.getPolicyType().getStOldPolicyTypeID(), '0', 2);
        //nopol = pol.getStPolicyNo().substring(0,2) + policyType2Digit + pol.getStPolicyNo().substring(4,16);

        final DTOList bayar = new DTOList();

        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            final InsuranceBayar1View ivd = new InsuranceBayar1View();

            ivd.markNew();

            bayar.add(ivd);

            final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();

            final BigDecimal amt =
                    installmentPeriod == null ?
                        premi :
                        installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);

            final BigDecimal diskonamt =
                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.add(pol.getDbNDDisc1(),pol.getDbNDDisc2()));

            final BigDecimal biayapolismateraiamt =
                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.add(pol.getDbNDPCost(),pol.getDbNDSFee()));

            BigDecimal totalFBase = BDUtil.add(pol.getDbNDFeeBase1(),pol.getDbNDFeeBase2());
            totalFBase = BDUtil.mul(pol.getDbCurrencyRate(),totalFBase);

            BigDecimal totalDisc = BDUtil.add(pol.getDbNDDisc1(),pol.getDbNDDisc2());
            totalDisc = BDUtil.mul(pol.getDbCurrencyRate(),totalDisc);

            BigDecimal totalBFee = BDUtil.add(pol.getDbNDBrok1(),pol.getDbNDBrok2());
            totalBFee = BDUtil.mul(pol.getDbCurrencyRate(),totalBFee);

            BigDecimal totalHFee = BDUtil.mul(pol.getDbCurrencyRate(),pol.getDbNDHFee());

            BigDecimal totalKomisi = BDUtil.add(pol.getDbNDComm1(),pol.getDbNDComm2());
            totalKomisi = BDUtil.add(totalKomisi,pol.getDbNDComm3());
            totalKomisi = BDUtil.add(totalKomisi,pol.getDbNDComm4());
            totalKomisi = BDUtil.mul(pol.getDbCurrencyRate(),totalKomisi);
            totalKomisi = BDUtil.add(totalKomisi,totalFBase);

            final String konter = Integer.toString(installmentSeq+1);

            ivd.setStPolicyID(pol.getStPolicyID());

            ivd.setStInsuranceNoPolis(nopol);

            ivd.setStInsuranceKodeKoasuransi(pol.getEntity2(Parameter.readString("UWRIT_CURRENT_COMPANY")).getStRefEntityID());

            ivd.setStInsuranceKonter(konter);
            ivd.setDbPremiTotal(BDUtil.mul(pol.getDbCurrencyRate(), inst.getDbAmount()));
            ivd.setDbPremiTotal(BDUtil.add(ivd.getDbPremiTotal(), diskonamt));
            //ivd.setDbPremiTotal(BDUtil.add(ivd.getDbPremiTotal(), biayapolismateraiamt));

            ivd.setDbKomisi(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), totalKomisi));
            ivd.setDbDiskon(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), totalDisc));
            ivd.setDbHandlingFee(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), totalHFee));
            ivd.setDbBrokerFee(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), totalBFee));
            ivd.setDtTanggalJatuhTempo(inst.getDtDueDate());
            ivd.setDbFeeBase(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), totalFBase));

        }

        final DTOList coin = pol.getCoins3();
        for(int i=0;i<coin.size();i++){
            InsurancePolicyCoinsView coins = (InsurancePolicyCoinsView) coin.get(i);

            if(coins.getStEntityID().equalsIgnoreCase(Parameter.readString("UWRIT_CURRENT_COMPANY"))) continue;

            for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
                InsurancePolicyInstallmentView inst2 = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

                final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();

                final String konter = Integer.toString(installmentSeq+1);

                final InsuranceBayar1View ivd2 = new InsuranceBayar1View();

                final String konter2 = Integer.toString(installment.size()+1);

                ivd2.markNew();

                bayar.add(ivd2);

                ivd2.setStPolicyID(pol.getStPolicyID());
                ivd2.setStInsuranceNoPolis(nopol);
                ivd2.setStInsuranceKodeKoasuransi(pol.getEntity2(coins.getStEntityID()).getStRefEntityID());
                if(ivd2.getStInsuranceKodeKoasuransi()==null)
                    ivd2.setStInsuranceKodeKoasuransi(coins.getStEntityID());

                ivd2.setStInsuranceKonter(konter);
                ivd2.setDbPremiTotal(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbPremiAmount())));
                ivd2.setDbKomisi(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbCommissionAmount())));
                ivd2.setDbDiskon(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbDiscountAmount())));
                ivd2.setDbHandlingFee(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbHandlingFeeAmount())));
                ivd2.setDbBrokerFee(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbBrokerageAmount())));

                ivd2.setDbPremiTotal(BDUtil.mul(ivd2.getDbPremiTotal(),new BigDecimal(-1)));
                ivd2.setDbKomisi(BDUtil.mul(ivd2.getDbKomisi(),new BigDecimal(-1)));
                ivd2.setDbDiskon(BDUtil.mul(ivd2.getDbDiskon(),new BigDecimal(-1)));
                ivd2.setDbHandlingFee(BDUtil.mul(ivd2.getDbHandlingFee(),new BigDecimal(-1)));
                ivd2.setDbBrokerFee(BDUtil.mul(ivd2.getDbBrokerFee(),new BigDecimal(-1)));
                ivd2.setDtTanggalJatuhTempo(inst2.getDtDueDate());
            }
        }

        S.store(bayar);
        S.release();

    }
    
    public void saveABAHutang(InsurancePolicyView pol) throws Exception{
        final SQLUtil S = new SQLUtil();
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList installment = pol.getInstallment();
        
        final DTOList details = pol.getDetails();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        String nopol="";
        
        if(pol.isStatusPolicy()||pol.isStatusRenewal()){
            nopol = pol.getStPolicyNo().substring(0,16);
        }else if(pol.isStatusEndorse()){
            nopol = pol.getPolicyNoOldFormat(pol.getStPolicyNo());
        }
        
        //final String policyType2Digit = StringTools.leftPad(pol.getPolicyType().getStOldPolicyTypeID(), '0', 2);
        //nopol = pol.getStPolicyNo().substring(0,2) + policyType2Digit + pol.getStPolicyNo().substring(4,16);
        
        final DTOList hutang = new DTOList();
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
            
            final InsuranceHutangView hut = new InsuranceHutangView();
            
            hut.markNew();
            
            final String konter = String.valueOf(installmentSeq+1);
            
            BigDecimal[] komisi = new BigDecimal[4];
            BigDecimal[] taxKomisi = new BigDecimal[4];
            String[] npwpKomisi = new String[4];
            String[] kodePajak = new String[4];
            BigDecimal[] feebase = new BigDecimal[1];
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                
                if (item.isComission()) ;
                else continue;
                
                /*
                if(item.isKomisiA()){
                    komisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    taxKomisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpKomisi[0] = item.getStNPWP();
                }
                 
                if(item.isKomisiB()){
                    komisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    taxKomisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpKomisi[1] = item.getStNPWP();
                }
                 
                if(item.isKomisiC()){
                    komisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    taxKomisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpKomisi[2] = item.getStNPWP();
                }
                 
                if(item.isKomisiD()){
                    komisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    taxKomisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpKomisi[3] = item.getStNPWP();
                }*/
                
                //selalu buat yang pph23
                /*
                if (komisi[0] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("2")){
                        komisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[0] = item.getStNPWP();
                 
                        continue;
                    }
                 
                }
                if (komisi[1] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("1")){
                        komisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[1] = item.getStNPWP();
                        continue;
                    }
                 
                 
                }
                if (komisi[2] == null) {
                   if(item.getStTaxCode().equalsIgnoreCase("1")){
                       komisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[2] = item.getStNPWP();
                        continue;
                   }
                 
                 
                }
                if (komisi[3] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("1")){
                        komisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[3] = item.getStNPWP();
                        continue;
                    }
                 
                 
                }*/
                
                if (komisi[0] == null) {
                    //komisi[0] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    komisi[0] = item.getDbAmount();
                    taxKomisi[0] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    npwpKomisi[0] = item.getStNPWP();
                    kodePajak[0] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[1] == null) {
                    //komisi[1] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    komisi[1] = item.getDbAmount();
                    taxKomisi[1] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    npwpKomisi[1] = item.getStNPWP();
                    kodePajak[1] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[2] == null) {
                    //komisi[2] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    komisi[2] = item.getDbAmount();
                    taxKomisi[2] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    npwpKomisi[2] = item.getStNPWP();
                    kodePajak[2] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[3] == null) {
                    //komisi[3] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    komisi[3] = item.getDbAmount();
                    taxKomisi[3] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    npwpKomisi[3] = item.getStNPWP();
                    kodePajak[3] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
            }
            
            /*
            BigDecimal[] brokerfee = new BigDecimal[2];
            BigDecimal[] brokerfeepct = new BigDecimal[2];
            BigDecimal[] taxbrokerfee = new BigDecimal[2];
            String[] npwpbrokerfee = new String[2];
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
             
                if (item.isBrokerFee()) ;
                else continue;
             
                if (brokerfee[0] == null) {
                    brokerfee[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    brokerfeepct[0] = item.getDbRatePct();
                    taxbrokerfee[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpbrokerfee[0] = item.getStNPWP();
                    continue;
                }
                if (brokerfee[1] == null) {
                    brokerfee[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    brokerfeepct[1] = item.getDbRatePct();
                    taxbrokerfee[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    npwpbrokerfee[1] = item.getStNPWP();
                    continue;
                }
            }*/
            
            hut.setStPolicyID(pol.getStPolicyID());
            hut.setStInsuranceNoPolis(nopol);
            hut.setStInsuranceKonter(konter);
            hut.setStInsuranceKodeKoasuransi(pol.getEntity2(Parameter.readString("UWRIT_CURRENT_COMPANY")).getStRefEntityID());
            hut.setDbNilaiA(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), komisi[0]));
            hut.setDbNilaiB(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), komisi[1]));
            hut.setDbNilaiC(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), komisi[2]));
            hut.setDbNilaiD(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), komisi[3]));

            hut.setDbNilaiA(BDUtil.mul(hut.getDbNilaiA(), pol.getDbCurrencyRate()));
            hut.setDbNilaiB(BDUtil.mul(hut.getDbNilaiB(), pol.getDbCurrencyRate()));
            hut.setDbNilaiC(BDUtil.mul(hut.getDbNilaiC(), pol.getDbCurrencyRate()));
            hut.setDbNilaiD(BDUtil.mul(hut.getDbNilaiD(), pol.getDbCurrencyRate()));

            hut.setStKodePajakA(kodePajak[0]);
            hut.setStKodePajakB(kodePajak[1]);
            hut.setStKodePajakC(kodePajak[2]);
            hut.setStKodePajakD(kodePajak[3]);
            hut.setDbNilaiE(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.add(pol.getDbNDFeeBase1(),pol.getDbNDFeeBase2())));
            //hut.setDbBayarHutang5(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.add( brokerfee[0], brokerfee[1])));
            
            hutang.add(hut);
        }
        
        final DTOList coin = pol.getCoins3();
        for(int i=0;i<coin.size();i++){
            InsurancePolicyCoinsView coins = (InsurancePolicyCoinsView) coin.get(i);
            
            if(coins.getStEntityID().equalsIgnoreCase(Parameter.readString("UWRIT_CURRENT_COMPANY"))) continue;
            
            //start
            for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
                final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
                
                final String konter = Integer.toString(installmentSeq+1);
                
                final InsuranceHutangView hut2 = new InsuranceHutangView();
                
                hut2.markNew();
                hutang.add(hut2);
                
                hut2.setStPolicyID(pol.getStPolicyID());
                hut2.setStInsuranceNoPolis(nopol);
                hut2.setStInsuranceKonter(konter);
                hut2.setStInsuranceKodeKoasuransi(pol.getEntity2(coins.getStEntityID()).getStRefEntityID());
                if(hut2.getStInsuranceKodeKoasuransi()==null)
                    hut2.setStInsuranceKodeKoasuransi(coins.getStEntityID());
                
                hut2.setDbNilaiA(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbCommissionAmount())));
                //hut2.setDbBayarHutang5(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), BDUtil.mul(pol.getDbCurrencyRate(),coins.getDbBrokerageAmount())));
                hut2.setDbNilaiA(BDUtil.mul(hut2.getDbNilaiA(),new BigDecimal(-1)));
                //hut2.setDbBayarHutang5(BDUtil.mul(hut2.getDbBayarHutang5(),new BigDecimal(-1)));
            }
            //end
        }
        
        S.store(hutang);
        S.release();
    }
    
    public void saveABAPajak(InsurancePolicyView pol) throws Exception{
        final SQLUtil S = new SQLUtil();
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList details = pol.getDetails();
        
        final  DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        String nopol="";
        
        if(pol.isStatusPolicy()||pol.isStatusRenewal()){
            nopol = pol.getStPolicyNo().substring(0,16);
        }else if(pol.isStatusEndorse()){
            nopol = pol.getPolicyNoOldFormat(pol.getStPolicyNo());
        }
        
        //final String policyType2Digit = StringTools.leftPad(pol.getPolicyType().getStOldPolicyTypeID(), '0', 2);
        //nopol = pol.getStPolicyNo().substring(0,2) + policyType2Digit + pol.getStPolicyNo().substring(4,16);
        
        final DTOList pajak = new DTOList();
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
            
            final InsurancePajakView paj = new InsurancePajakView();
            
            paj.markNew();
            
            final String konter = String.valueOf(installmentSeq+1);
            
            //start
            BigDecimal[] komisi = new BigDecimal[4];
            BigDecimal[] taxKomisi = new BigDecimal[4];
            String[] npwpKomisi = new String[4];
            String[] kodePajak = new String[4];
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                
                if (item.isKomisi()) ;
                else continue;
                
                /*
                if (komisi[0] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("2")){
                         komisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[0] = item.getStNPWP();
                        continue;
                    }
                 
                 
                }
                if (komisi[1] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("1")){
                        komisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[1] = item.getStNPWP();
                        continue;
                    }
                 
                }
                if (komisi[2] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("1")){
                         komisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[2] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[2] = item.getStNPWP();
                        continue;
                    }
                 
                }
                if (komisi[3] == null) {
                    if(item.getStTaxCode().equalsIgnoreCase("1")){
                         komisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                        taxKomisi[3] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                        npwpKomisi[3] = item.getStNPWP();
                        continue;
                    }
                 
                }*/
                
                
                if (komisi[0] == null) {
                    komisi[0] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    //taxKomisi[0] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    taxKomisi[0] = item.getDbTaxAmount();

                    npwpKomisi[0] = item.getStNPWP();
                    kodePajak[0] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[1] == null) {
                    komisi[1] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    //taxKomisi[1] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    taxKomisi[1] = item.getDbTaxAmount();
                    npwpKomisi[1] = item.getStNPWP();
                    kodePajak[1] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[2] == null) {
                    komisi[2] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    //taxKomisi[2] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    taxKomisi[2] = item.getDbTaxAmount();
                    npwpKomisi[2] = item.getStNPWP();
                    kodePajak[2] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
                if (komisi[3] == null) {
                    komisi[3] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbAmount());
                    //taxKomisi[3] = BDUtil.mul(pol.getDbCurrencyRate(), item.getDbTaxAmount());
                    taxKomisi[3] = item.getDbTaxAmount();
                    npwpKomisi[3] = item.getStNPWP();
                    kodePajak[3] = item.getStTaxCode()!=null?item.getStTaxCode():"10";
                    continue;
                }
            }
            
            BigDecimal[] brokerfee = new BigDecimal[2];
            BigDecimal[] brokerfeepct = new BigDecimal[2];
            BigDecimal[] taxbrokerfee = new BigDecimal[2];
            String[] npwpbrokerfee = new String[2];
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                
                if (item.isBrokerFee()||item.isHandlingFee()) ;
                else continue;
                
                if (brokerfee[0] == null) {
                    brokerfee[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    brokerfeepct[0] = item.getDbRatePct();
                    //taxbrokerfee[0] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    taxbrokerfee[0] = item.getDbTaxAmount();
                    npwpbrokerfee[0] = item.getStNPWP();
                    continue;
                }
                if (brokerfee[1] == null) {
                    brokerfee[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbAmount());
                    brokerfeepct[1] = item.getDbRatePct();
                    //taxbrokerfee[1] = BDUtil.mul(pol.getDbCurrencyRate(),item.getDbTaxAmount());
                    taxbrokerfee[1] = item.getDbTaxAmount();
                    npwpbrokerfee[1] = item.getStNPWP();
                    continue;
                }
                
            }
            
            paj.setStPolicyID(pol.getStPolicyID());
            paj.setStInsuranceNoPolis(nopol);
            paj.setStInsuranceKonter(konter);
            paj.setStInsuranceKodeKoasuransi(pol.getEntity2(Parameter.readString("UWRIT_CURRENT_COMPANY")).getStRefEntityID());

            paj.setDbPajakA(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxKomisi[0]));
            paj.setDbPajakB(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxKomisi[1]));
            paj.setDbPajakC(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxKomisi[2]));
            paj.setDbPajakD(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxKomisi[3]));

            paj.setDbPajakA(BDUtil.mul(paj.getDbPajakA(), pol.getDbCurrencyRate()));
            paj.setDbPajakB(BDUtil.mul(paj.getDbPajakB(), pol.getDbCurrencyRate()));
            paj.setDbPajakC(BDUtil.mul(paj.getDbPajakC(), pol.getDbCurrencyRate()));
            paj.setDbPajakD(BDUtil.mul(paj.getDbPajakD(), pol.getDbCurrencyRate()));

            paj.setDbBrokerFee(installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxbrokerfee[0]));
            paj.setDbBrokerFee(BDUtil.mul(paj.getDbBrokerFee(), pol.getDbCurrencyRate()));

            paj.setStNPWP1(npwpKomisi[0]);
            paj.setStNPWP2(npwpKomisi[1]);
            paj.setStNPWP3(npwpKomisi[2]);
            paj.setStNPWP4(npwpKomisi[3]);
            paj.setStNPWP5(npwpbrokerfee[0]);
            paj.setStKodePajakA(kodePajak[0]);
            paj.setStKodePajakB(kodePajak[1]);
            paj.setStKodePajakC(kodePajak[2]);
            paj.setStKodePajakD(kodePajak[3]);
            
            pajak.add(paj);
        }
        
        S.store(pajak);
        S.release();
    }
    
    
    public void saveABAByDate(InsurancePolicyView policy) throws Exception {
        try {
                          
                saveABABayar(policy);
                
                saveABAProduk(policy);
                
                saveABAPajak(policy);
                
                saveABAHutang(policy);
  
        } catch (Exception e) {
            ctx.setRollbackOnly();
        }
        
        
    }
    
    public void saveABAKreasi(InsurancePolicyView pol) throws Exception{
        final SQLUtil S = new SQLUtil();
        
        DTOList kreasi2 = new DTOList();
        
        String nopol="";
        
        if(pol.isStatusPolicy()||pol.isStatusRenewal()){
            nopol = pol.getStPolicyNo().substring(0,16);
        }else if(pol.isStatusEndorse()){
            nopol = pol.getPolicyNoOldFormat(pol.getStPolicyNo());
        }
        
        final DTOList objek = pol.getObjects();
        
        if(!pol.getStPolicyTypeID().equalsIgnoreCase("21")) return;
        
        for(int i=0;i<objek.size();i++){
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objek.get(i);
            
            InsuranceKreasiView kreasi = new InsuranceKreasiView();
            
            final String norut = Integer.toString(i+1);
            
            kreasi.markNew();
            
            kreasi2.add(kreasi);
            
            kreasi.setStInsuranceNoUrut(norut);
            kreasi.setStInsuranceNoPolis(nopol);
            kreasi.setStInsuranceNama(obj.getStReference1());
            kreasi.setStInsuranceUmur(obj.getStReference2());
            kreasi.setDtTanggalLahir(obj.getDtReference1());
            kreasi.setDtTanggalCair(obj.getDtReference2());
            kreasi.setDtTanggalAkhir(obj.getDtReference3());
            kreasi.setDbInsured(obj.getDbObjectInsuredAmount());
            kreasi.setDbRatePremi(obj.getDbReference5());
            kreasi.setDbPremi(obj.getDbReference6());
            kreasi.setStStatus(pol.getStStatus());
            kreasi.setStCabang(pol.getStCostCenterCode());
            kreasi.setStPolicyID(obj.getStPolicyID());
        }
        
        S.store(kreasi2);
        S.release();
    }

    public LOV LOV_Kabupaten() throws Exception {
        return ListUtil.getLookUpFromQuery(
                "select vs_code, vs_description from s_valueset where vs_group = 'KABUPATEN' and active_flag='Y' order by orderseq"
                );
    }

    public InsurancePolicyParentView getInsurancePolicyParent(String stPolicyID) throws Exception {
        final InsurancePolicyParentView pol = (InsurancePolicyParentView) ListUtil.getDTOListFromQuery("select * from ins_policy where pol_id = ?",
                new Object[]{stPolicyID},
                InsurancePolicyParentView.class).getDTO();
        
        return pol;
    }
    
    public void saveInputPaymentDate(InsurancePolicyView pol, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        try {
            
            S.store(pol);
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public void approveAfterReverse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            
            if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal()) {
                
                if (isFreeInstallment) postARInstallmentFree(pol);
                else postARInstallment(pol);
                
                postCoas(pol);
                
                //postReasCumullation(pol);
                
                postLimitZoneBalance(pol);
                
                //simpen buat sistem lama
                saveABAProduk(pol);
                
                saveABABayar(pol);
                
                saveABAHutang(pol);
                
                saveABAPajak(pol);
                
                //saveABAKreasi(pol);
                
                //saveABAProdukDBF2(pol);
                
                //saveABABayarDBF(pol);
                
                //saveABAHutangDBF(pol);
                
                //saveABAPajakDBF(pol);
                
            }
            
            if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                
                //postReasClaim(pol);
                
                postAPClaim(pol);
                
                postARCoas(pol);
            }
            
            if(pol.isStatusEndorseRI()){
                //postReasCumullation(pol);
            }
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public void saveParent(InsurancePolicyParentView pol, String stNextStatus) throws Exception {
        pol = (InsurancePolicyParentView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            
            if (stNextStatus != null) {
                //pol.setStParentID(pol.getStPolicyID());
                
                if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                else pol.setStParentID(pol.getStPolicyID());
                pol.setStRootID(pol.getStRootID());
                pol.markNew();
                pol.setStActiveFlag("Y");
                pol.setStStatus(stNextStatus);
                
            }
            
            if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
            
            
            if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            //if(FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus))
            //		pol.generatePolicyNo();
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            //final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            //boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            if (true) {
                
                if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                    
                    if (pol.isStatusPolicy() || pol.isStatusEndorse()) {
                        //postAR(pol);
                        //postARInstallment(pol);
                        
                        //postCoas(pol);
                        
                        //postReas(pol);
                    }
                    
                    if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                        
                        //postReasClaim(pol);
                        
                        //postAPClaim(pol);
                        
                        //postARCoas(pol);
                        
                        //javax.swing.JOptionPane.showMessageDialog(null,"POSTING CLAIM !!","Claim",javax.swing.JOptionPane.CLOSED_OPTION );
                    }
                    
                } else {
                    
                    // unpost
                    
                }
            }
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateClaimRecap(DTOList data, ReceiptForm form) throws Exception {

        final SQLUtil S = new SQLUtil();

        try{

            for (int i = 0; i < data.size(); i++ ) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) data.get(i);

                ARInvoiceView ari = rcl.getInvoice();

                if (ari.getStClaimNo()!=null) continue;

                ari.markUpdate();
                ari.setStNoSuratHutang(form.getStReceiptNo());
                ari.setStClaimNo(form.getStReceiptNo());
                ari.setStClaimName(form.getStName());
                ari.setStClaimCoinsID(form.getStEntityID());
                ari.setStClaimCoinsName(form.getStEntityName());
                ari.setStClaimCoinsAddress(form.getStAddress());

                S.store(ari);
            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;

        } finally{
            S.release();
        }
    }
     
    public void deleteABA(InsurancePolicyView pol) throws Exception {
        final SQLUtil S = new SQLUtil();
        
        try {
            
            final DTOList produk = ListUtil.getDTOListFromQuery(
                    "select * from aba_produk where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceProdukView.class);
            
            produk.deleteAll();
            S.storeDeleteNormal(produk);
            
            final DTOList bayar = ListUtil.getDTOListFromQuery(
                    "select * from aba_bayar1 where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceBayar1View.class);
            
            bayar.deleteAll();
            S.storeDeleteNormal(bayar);
            
            final DTOList hutang = ListUtil.getDTOListFromQuery(
                    "select * from aba_hutang where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceHutangView.class);
            
            hutang.deleteAll();
            S.storeDeleteNormal(hutang);
            
            final DTOList pajak = ListUtil.getDTOListFromQuery(
                    "select * from aba_pajak where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsurancePajakView.class);
            
            pajak.deleteAll();
            S.storeDeleteNormal(pajak);
            
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
        
        
    }
    
    public InsurancePolicyView savePolicyOnly(InsurancePolicyView pol) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        try {
            
            S.store(pol);
            
            return pol;
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public String saveAndReturnPolicy(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        String pol_id = null;
        
        try {
            
            //not approval mode
            if (!approvalMode) {
                if (stNextStatus != null) {
                    
                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
                    
                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)) {
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();
                            
                            oldPolis.setStActiveFlag("N");
                            
                            S.store(oldPolis);
                        }
                    }
                    
                    //pol.setStParentID(pol.getStPolicyID());
                    
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());
                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    
                    pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();
                    
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList policyDocuments = pol.getPolicyDocuments();
                    
                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();
                    
                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;
                        
                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                        
                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        
                        obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();
                        
                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                            obj.getTreaties().convertAllToNew();
                            
                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();
                            
                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);
                                
                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();
                                
                            }
                        }
                        //end
                    }
                }
                
                
                if (pol.isNew()){
                    pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
                    pol_id = pol.getStPolicyID();
                }
                
                
                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
                
                
                final DTOList details = pol.getDetails();
                
                final HashMap insuranceItemsMap = getInsuranceItemsMap();
                
                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                    
                    ip.setStPolicyID(pol.getStPolicyID());
                    
                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    
                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }
                
                S.store(details);
                
                final DTOList claimItems = pol.getClaimItems();
                
                for (int i = 0; i < claimItems.size(); i++) {
                    InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);
                    
                    it.setStPolicyID(pol.getStPolicyID());
                    
                    if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                }
                
                S.store(claimItems);
                
                final DTOList clausules = (DTOList) pol.getClausules().clone();
                if (clausules != null) {
                    
                    for (int i = clausules.size() - 1; i >= 0; i--) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                        
                        if (!icl.isSelected()) {
                            clausules.delete(i);
                            continue;
                        }
                        
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                    }
                    
                    S.store(clausules);
                }
                
                final DTOList claimDocuments = pol.getClaimDocuments();
                
                for (int i = 0; i < claimDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(claimDocuments);
                
                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(policyDocuments);
                
                final DTOList objects = pol.getObjects();
                
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                    
                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;
                    
                    obj.setStPolicyID(pol.getStPolicyID());
                    
                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                        
                        if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                    }
                    
                    S.store(obj);
                    
                    final DTOList oclaus = obj.getClausules();
                    for (int j = 0; j < oclaus.size(); j++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                    }
                    S.store(oclaus);
                    
                    final DTOList suminsureds = obj.getSuminsureds();
                    
                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                        
                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(suminsureds);
                    
                    final DTOList coverage = obj.getCoverage();
                    
                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                        
                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                    }
                    
                    S.store(coverage);
                    
                    final DTOList deductibles = obj.getDeductibles();
                    
                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                        
                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(deductibles);
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                        {
                            // save treaties
                            
                            final DTOList treaties = obj.getTreaties();
                            
                            treaties.combineDeleted();
                            
                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                                
                                if (tre.isDelete()) tre.getDetails().deleteAll();
                                
                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                                
                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                
                                final DTOList tredetails = tre.getDetails();
                                
                                tredetails.combineDeleted();
                                
                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                    
                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }
                                    
                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                    
                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                    
                                    final DTOList shares = tredet.getShares();
                                    
                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                        
                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                        
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                        
                                    }
                                    
                                    S.store(shares);
                                    
                                    
                                }
                                
                                S.store(tredetails);
                                
                            }
                            
                            S.store(treaties);
                        }
                    }
                    
                    /* remark dolo klaim reas doble
                    {  //save treatis if claim to obtain share details
                        if(pol.isStatusClaimPLA() && !pol.isEffective()){
                            if(pol.isStatusClaimPLA()){
                               final DTOList treaties = obj.getTreaties();
                     
                               treaties.combineDeleted();
                     
                               for (int l = 0; l < treaties.size(); l++) {
                                  InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                     
                                  if (tre.isDelete()) tre.getDetails().deleteAll();
                     
                                  //if (tre.isNew())
                                  tre.markNew();
                                  tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                     
                                  tre.setStPolicyID(null);
                                  tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                     
                                  final DTOList tredetails = tre.getDetails();
                     
                                  tredetails.combineDeleted();
                     
                                  for (int j = 0; j < tredetails.size(); j++) {
                                     InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                     
                                     if (tredet.isDelete()){
                                         tredet.getShares().deleteAll();
                                         tredet.getCoverage().deleteAll();
                                     }
                     
                                     //if (tredet.isNew())
                                     tredet.markNew();
                                     tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                     
                                     tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                     
                                     final DTOList shares = tredet.getShares();
                     
                                     for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                     
                                        //if (ri.isNew())
                                        ri.markNew();
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                     
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                          //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                     
                     
                                         final DTOList cover = ri.getCoverage();
                     
                                         for (int m = 0; m < cover.size(); m++) {
                                            InsurancePolicyCoverReinsView cov = (InsurancePolicyCoverReinsView) cover.get(m);
                     
                                            if (cov.isNew()) cov.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("POLCOVREINS")));
                     
                                            cov.setStInsurancePolicyReinsID(ri.getStInsurancePolicyReinsID());
                                            cov.setStInsuranceTreatySharesID(ri.getStInsuranceTreatySharesID());
                     
                                            cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                            cov.setStPolicyID(pol.getStPolicyID());
                     
                                            //tambahan
                                            cov.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                              //
                                            cov.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                            cov.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                         }
                     
                                         S.store(cover);
                                     }
                     
                                     S.store(shares);
                                  }
                     
                                  S.store(tredetails);
                               }
                     
                               S.store(treaties);
                           }
                        }
                     
                    }*/
                    
                }
                
                final DTOList deletedObjects = objects.getDeleted();
                
                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                    
                    S.store(obj);
                    //S.store(obj.getClausules());
                    S.store(obj.getSuminsureds());
                    S.store(obj.getCoverage());
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.deleteAll();
                        treaties.combineDeleted();
                        
                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                            
                            final DTOList treDetails = tre.getDetails();
                            
                            treDetails.deleteAll();
                            treDetails.combineDeleted();
                            
                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                                
                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();
                                
                                S.store(treShares);
                            }
                            
                            S.store(treDetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                    }
                
                final DTOList entities = pol.getEntities();
                
                for (int i = 0; i < entities.size(); i++) {
                    InsurancePolicyEntityView entityView = (InsurancePolicyEntityView) entities.get(i);
                    entityView.setStPolicyID(pol.getStPolicyID());
                    
                    if (entityView.isNew())
                        entityView.setStPolicyEntityID(String.valueOf(IDFactory.createNumericID("INSPOLENT")));
                    
                    if (entityView.isModified()) {
                        entityView.setStEntityID(getRemoteEntityManager().save(entityView.getEntity()));
                    }
                    
                    S.store(entityView);
                    
                }
                
                final DTOList coins = pol.getCoins2();
                
                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coins);
                
                final DTOList coinscoverage = pol.getCoinsCoverage();
                
                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coinscoverage);
                
                /*
                final DTOList coveragereinsurance = pol.getCoverageReinsurance();
                 
                for (int i = 0; i < coveragereinsurance.size(); i++) {
                    InsurancePolicyCoverReinsView coverreins = (InsurancePolicyCoverReinsView) coveragereinsurance.get(i);
                 
                    if (coverreins.isNew())
                        coverreins.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOVERAGEREINS")));
                 
                    coverreins.setStPolicyID(pol.getStPolicyID());
                }
                 
                S.store2(coveragereinsurance);
                 */
                
                final DTOList installment = pol.getInstallment();
                
                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                    
                    inst.setStPolicyID(pol.getStPolicyID());
                    
                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
                }
                
                S.store2(installment);
                
                final DTOList deductibles = pol.getDeductibles();
                
                for (int i = 0; i < deductibles.size(); i++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);
                    
                    if (ded.isNew())
                        ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                    
                    ded.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(deductibles);
                
                /*// take care deleted treaties
                {
                   final DTOList deletedTreaties = pol.getTreaties().getDeleted();
                 
                   if (deletedTreaties!=null)
                      for (int i = 0; i < deletedTreaties.size(); i++) {
                         InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) deletedTreaties.get(i);
                 
                         S.store(tre);
                 
                         final DTOList tredetails = tre.getDetails();
                 
                         for (int j = 0; j < tredetails.size(); j++) {
                            InsuranceTreatyDetailView tredet = (InsuranceTreatyDetailView) tredetails.get(j);
                 
                            tredet.markDelete();
                 
                            S.store(tredet);
                 
                            final DTOList treShares = tredet.getShares();
                 
                            for (int k = 0; k < treShares.size(); k++) {
                               InsurancePolicyReinsView rei = (InsurancePolicyReinsView) treShares.get(k);
                 
                               rei.markDelete();
                 
                               S.store(rei);
                            }
                         }
                      }
                }*/
                
                
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                    {
                        // save treaties
                        
                        final DTOList treaties = pol.getTreaties();
                        
                        treaties.combineDeleted();
                        
                        for (int l = 0; l < treaties.size(); l++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                            
                            if (tre.isDelete()) tre.getDetails().deleteAll();
                            
                            if (tre.isNew())
                                tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                            
                            tre.setStPolicyID(pol.getStPolicyID());
                            
                            final DTOList tredetails = tre.getDetails();
                            
                            tredetails.combineDeleted();
                            
                            for (int j = 0; j < tredetails.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                
                                if (tredet.isDelete()) tredet.getShares().deleteAll();
                                
                                if (tredet.isNew())
                                    tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                
                                tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                
                                final DTOList shares = tredet.getShares();
                                
                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                    
                                    if (ri.isNew())
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                    
                                    ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                    
                                    ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                    ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                    
                                }
                                
                                S.store(shares);
                                
                            }
                            
                            S.store(tredetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                }
            }//end not approval mode
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getStPolicyTypeID().equalsIgnoreCase("51") && pol.getStCostCenterCode().equalsIgnoreCase("24")){
                    pol.checkPolicyNoBefore(pol.getStPolicyNo());
                }else{
                    pol.generatePolicyNo();
                }
            }
            
            //if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus))
            //pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            if(approvalMode){
                if (postflagchanged) {
                    
                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                        
                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal()) {
                            
                            if (isFreeInstallment) postARInstallmentFree(pol);
                            else postARInstallment(pol);
                            
                            postCoas(pol);
                            
                            //postReasCumullation(pol);
                            
                            postLimitZoneBalance(pol);
                            
                            //simpen buat sistem lama
                            saveABAProduk(pol);
                            
                            saveABABayar(pol);
                            
                            saveABAHutang(pol);
                            
                            saveABAPajak(pol);
                            
                            //saveABAKreasi(pol);
                            
                            //saveABAProdukDBF2(pol);
                            
                            //saveABABayarDBF(pol);
                            
                            //saveABAHutangDBF(pol);
                            
                            //saveABAPajakDBF(pol);
                            
                        }
                        
                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                            
                            postReasClaim(pol);
                            
                            postAPClaim(pol);
                            
                            postARCoas(pol);
                        }
                        
                        if(pol.isStatusEndorseRI()){
                            postReasCumullation(pol);
                        }
                        
                    } else {
                        // unpost
                    }
                }
            }
            return pol_id;
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public void saveAndApprove(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            
            //not approval mode
            
            if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
            
            
            if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
            
            
            final DTOList details = pol.getDetails();
            
            final HashMap insuranceItemsMap = getInsuranceItemsMap();
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                
                ip.setStPolicyID(pol.getStPolicyID());
                
                if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                
                ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
            }
            
            S.store(details);
            
            final DTOList claimItems = pol.getClaimItems();
            
            for (int i = 0; i < claimItems.size(); i++) {
                InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);
                
                it.setStPolicyID(pol.getStPolicyID());
                
                if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
            }
            
            S.store(claimItems);
            
            final DTOList clausules = (DTOList) pol.getClausules().clone();
            if (clausules != null) {
                
                for (int i = clausules.size() - 1; i >= 0; i--) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                    
                    if (!icl.isSelected()) {
                        clausules.delete(i);
                        continue;
                    }
                    
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(null);
                }
                
                S.store(clausules);
            }
            
            final DTOList claimDocuments = pol.getClaimDocuments();
            
            for (int i = 0; i < claimDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(claimDocuments);
            
            final DTOList policyDocuments = pol.getPolicyDocuments();
            
            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(policyDocuments);
            
            final DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                
                final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                
                if (pol.isStatusClaim())
                    if (!isClaimObject2) continue;
                
                obj.setStPolicyID(pol.getStPolicyID());
                
                if (obj.isNew()) {
                    final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                    
                    if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                }
                
                S.store(obj);
                
                final DTOList oclaus = obj.getClausules();
                for (int j = 0; j < oclaus.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                }
                S.store(oclaus);
                
                final DTOList suminsureds = obj.getSuminsureds();
                
                for (int j = 0; j < suminsureds.size(); j++) {
                    InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                    if (itsi.isNew())
                        itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                    
                    itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    itsi.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(suminsureds);
                
                final DTOList coverage = obj.getCoverage();
                
                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                    
                    if (cov.isNew())
                        cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                    cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    cov.setStPolicyID(pol.getStPolicyID());
                    cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                }
                
                S.store(coverage);
                
                final DTOList deductibles = obj.getDeductibles();
                
                for (int j = 0; j < deductibles.size(); j++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                    
                    if (ded.isNew())
                        ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                    ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    ded.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(deductibles);
                
                //beban load
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                    {
                        // save treaties
                        
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.combineDeleted();
                        
                        for (int l = 0; l < treaties.size(); l++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                            
                            if (tre.isDelete()) tre.getDetails().deleteAll();
                            
                            if (tre.isNew())
                                tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                            
                            tre.setStPolicyID(null);
                            tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                            
                            final DTOList tredetails = tre.getDetails();
                            
                            tredetails.combineDeleted();
                            
                            for (int j = 0; j < tredetails.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                
                                if (tredet.isDelete()) {
                                    tredet.getShares().deleteAll();
                                }
                                
                                if (tredet.isNew())
                                    tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                
                                tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                
                                final DTOList shares = tredet.getShares();
                                
                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                    
                                    if (ri.isNew())
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                    
                                    //tambahan
                                    ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                    //
                                    ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                    ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                    
                                }
                                
                                S.store(shares);
                                
                                
                            }
                            
                            S.store(tredetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                }
                
                    /* remark dolo klaim reas doble
                    {  //save treatis if claim to obtain share details
                        if(pol.isStatusClaimPLA() && !pol.isEffective()){
                            if(pol.isStatusClaimPLA()){
                               final DTOList treaties = obj.getTreaties();
                     
                               treaties.combineDeleted();
                     
                               for (int l = 0; l < treaties.size(); l++) {
                                  InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                     
                                  if (tre.isDelete()) tre.getDetails().deleteAll();
                     
                                  //if (tre.isNew())
                                  tre.markNew();
                                  tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                     
                                  tre.setStPolicyID(null);
                                  tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                     
                                  final DTOList tredetails = tre.getDetails();
                     
                                  tredetails.combineDeleted();
                     
                                  for (int j = 0; j < tredetails.size(); j++) {
                                     InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                     
                                     if (tredet.isDelete()){
                                         tredet.getShares().deleteAll();
                                         tredet.getCoverage().deleteAll();
                                     }
                     
                                     //if (tredet.isNew())
                                     tredet.markNew();
                                     tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                     
                                     tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                     
                                     final DTOList shares = tredet.getShares();
                     
                                     for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                     
                                        //if (ri.isNew())
                                        ri.markNew();
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                     
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                          //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                     
                     
                                         final DTOList cover = ri.getCoverage();
                     
                                         for (int m = 0; m < cover.size(); m++) {
                                            InsurancePolicyCoverReinsView cov = (InsurancePolicyCoverReinsView) cover.get(m);
                     
                                            if (cov.isNew()) cov.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("POLCOVREINS")));
                     
                                            cov.setStInsurancePolicyReinsID(ri.getStInsurancePolicyReinsID());
                                            cov.setStInsuranceTreatySharesID(ri.getStInsuranceTreatySharesID());
                     
                                            cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                            cov.setStPolicyID(pol.getStPolicyID());
                     
                                            //tambahan
                                            cov.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                              //
                                            cov.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                            cov.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                         }
                     
                                         S.store(cover);
                                     }
                     
                                     S.store(shares);
                                  }
                     
                                  S.store(tredetails);
                               }
                     
                               S.store(treaties);
                           }
                        }
                     
                    }*/
                
            }
            
            final DTOList deletedObjects = objects.getDeleted();
            
            if (deletedObjects != null)
                for (int i = 0; i < deletedObjects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                
                S.store(obj);
                //S.store(obj.getClausules());
                S.store(obj.getSuminsureds());
                S.store(obj.getCoverage());
                
                //beban load
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                    final DTOList treaties = obj.getTreaties();
                    
                    treaties.deleteAll();
                    treaties.combineDeleted();
                    
                    for (int j = 0; j < treaties.size(); j++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                        
                        final DTOList treDetails = tre.getDetails();
                        
                        treDetails.deleteAll();
                        treDetails.combineDeleted();
                        
                        for (int k = 0; k < treDetails.size(); k++) {
                            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                            
                            final DTOList treShares = trd.getShares();
                            treShares.deleteAll();
                            treShares.combineDeleted();
                            
                            S.store(treShares);
                        }
                        
                        S.store(treDetails);
                        
                    }
                    
                    S.store(treaties);
                }
                }
            
            final DTOList entities = pol.getEntities();
            
            for (int i = 0; i < entities.size(); i++) {
                InsurancePolicyEntityView entityView = (InsurancePolicyEntityView) entities.get(i);
                entityView.setStPolicyID(pol.getStPolicyID());
                
                if (entityView.isNew())
                    entityView.setStPolicyEntityID(String.valueOf(IDFactory.createNumericID("INSPOLENT")));
                
                if (entityView.isModified()) {
                    entityView.setStEntityID(getRemoteEntityManager().save(entityView.getEntity()));
                }
                
                S.store(entityView);
                
            }
            
            final DTOList coins = pol.getCoins2();
            
            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coins);
            
            final DTOList coinscoverage = pol.getCoinsCoverage();
            
            for (int i = 0; i < coinscoverage.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coinscoverage);
            
                /*
                final DTOList coveragereinsurance = pol.getCoverageReinsurance();
                 
                for (int i = 0; i < coveragereinsurance.size(); i++) {
                    InsurancePolicyCoverReinsView coverreins = (InsurancePolicyCoverReinsView) coveragereinsurance.get(i);
                 
                    if (coverreins.isNew())
                        coverreins.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOVERAGEREINS")));
                 
                    coverreins.setStPolicyID(pol.getStPolicyID());
                }
                 
                S.store2(coveragereinsurance);
                 */
            
            final DTOList installment = pol.getInstallment();
            
            for (int i = 0; i < installment.size(); i++) {
                InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                
                inst.setStPolicyID(pol.getStPolicyID());
                
                if (inst.isNew())
                    inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
            }
            
            S.store2(installment);
            
            final DTOList deductibles = pol.getDeductibles();
            
            for (int i = 0; i < deductibles.size(); i++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);
                
                if (ded.isNew())
                    ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                
                ded.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store(deductibles);
            
                /*// take care deleted treaties
                {
                   final DTOList deletedTreaties = pol.getTreaties().getDeleted();
                 
                   if (deletedTreaties!=null)
                      for (int i = 0; i < deletedTreaties.size(); i++) {
                         InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) deletedTreaties.get(i);
                 
                         S.store(tre);
                 
                         final DTOList tredetails = tre.getDetails();
                 
                         for (int j = 0; j < tredetails.size(); j++) {
                            InsuranceTreatyDetailView tredet = (InsuranceTreatyDetailView) tredetails.get(j);
                 
                            tredet.markDelete();
                 
                            S.store(tredet);
                 
                            final DTOList treShares = tredet.getShares();
                 
                            for (int k = 0; k < treShares.size(); k++) {
                               InsurancePolicyReinsView rei = (InsurancePolicyReinsView) treShares.get(k);
                 
                               rei.markDelete();
                 
                               S.store(rei);
                            }
                         }
                      }
                }*/
            
            
            if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI()) {
                {
                    // save treaties
                    
                    final DTOList treaties = pol.getTreaties();
                    
                    treaties.combineDeleted();
                    
                    for (int l = 0; l < treaties.size(); l++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                        
                        if (tre.isDelete()) tre.getDetails().deleteAll();
                        
                        if (tre.isNew())
                            tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                        
                        tre.setStPolicyID(pol.getStPolicyID());
                        
                        final DTOList tredetails = tre.getDetails();
                        
                        tredetails.combineDeleted();
                        
                        for (int j = 0; j < tredetails.size(); j++) {
                            InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                            
                            if (tredet.isDelete()) tredet.getShares().deleteAll();
                            
                            if (tredet.isNew())
                                tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                            
                            tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                            
                            final DTOList shares = tredet.getShares();
                            
                            for (int k = 0; k < shares.size(); k++) {
                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                
                                if (ri.isNew())
                                    ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                
                                ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                
                                ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                
                            }
                            
                            S.store(shares);
                            
                        }
                        
                        S.store(tredetails);
                        
                    }
                    
                    S.store(treaties);
                }
            }
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getStPolicyTypeID().equalsIgnoreCase("51") && pol.getStCostCenterCode().equalsIgnoreCase("24")){
                    pol.checkPolicyNoBefore(pol.getStPolicyNo());
                }else{
                    pol.generatePolicyNo();
                }
            }
            
            //if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus))
            //pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            pol.setStPostedFlag("Y");
            pol.setStEffectiveFlag("Y");
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                
                if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal()) {
                    
                    if (isFreeInstallment) postARInstallmentFree(pol);
                    else postARInstallment(pol);
                    
                    postCoas(pol);
                    
                    postReasCumullation(pol);
                    
                    postLimitZoneBalance(pol);
                    
                    //simpen buat sistem lama
                    saveABAProduk(pol);
                    
                    saveABABayar(pol);
                    
                    saveABAHutang(pol);
                    
                    saveABAPajak(pol);
                    
                    //saveABAKreasi(pol);
                    
                    //saveABAProdukDBF2(pol);
                    
                    //saveABABayarDBF(pol);
                    
                    //saveABAHutangDBF(pol);
                    
                    //saveABAPajakDBF(pol);
                    
                }
                
                if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                    
                    postReasClaim(pol);
                    
                    postAPClaim(pol);
                    
                    postARCoas(pol);
                }
                
                if(pol.isStatusEndorseRI()){
                    postReasCumullation(pol);
                }
                
            } else {
                // unpost
            }
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
   
    
    
    public String saveAutoEndorsement(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            //UPDATE POLIS SEMENTARA INDUK NYA 
            {
                PreparedStatement P = S.setQuery("update ins_policy set ref9='N' where pol_id=?");
                P.setObject(1, pol.getStPolicyID());
                int r = P.executeUpdate();
                S.release();
            }
            
            //not approval mode
            if (stNextStatus != null) {
                
                final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
                
                if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                        FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                        FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                        FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                        FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)||
                        FinCodec.PolicyStatus.ENDORSETEMPORARY.equalsIgnoreCase(stNextStatus)) {
                } else {
                    if (!isBranching) {
                        final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                        oldPolis.markUpdate();
                        
                        oldPolis.setStActiveFlag("N");
                        
                        S.store(oldPolis);
                    }
                }
                
                //pol.setStParentID(pol.getStPolicyID());
                
                //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                //else pol.setStParentID(pol.getStPolicyID());
                pol.setStParentID(pol.getStPolicyID());
                pol.setStRootID(pol.getStRootID());
                pol.markNew();
                pol.setStActiveFlag("Y");
                pol.setStStatus(stNextStatus);
                pol.setStApprovedWho(null);
                pol.setDtApprovedDate(null);
                
                pol.getDeductibles().convertAllToNew();
                pol.getCoins2().convertAllToNew();
                pol.getInstallment().convertAllToNew();
                pol.getDetails().convertAllToNew();
                pol.getClausules().convertAllToNew();
                pol.getCoinsCoverage().convertAllToNew();
                //pol.getCoverageReinsurance().convertAllToNew();
                final DTOList claimDocuments = pol.getClaimDocuments();
                
                for (int i = 0; i < claimDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                    doc.setStInsurancePolicyDocumentID(null);
                }
                
                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    doc.setStInsurancePolicyDocumentID(null);
                }
                
                final DTOList konversiDocuments = pol.getKonversiDocuments();
                
                for (int i = 0; i < konversiDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                    doc.setStInsurancePolicyDocumentID(null);
                }
                
                final DTOList objects = pol.getObjects();
                objects.convertAllToNew();
                
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                    
                    final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    if (pol.isStatusClaim())
                        if (!isClaimObject) continue;
                    
                    obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                    
                    if (obj.getStPolicyObjectRefRootID() == null)
                        obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                    
                    obj.getClausules().convertAllToNew();
                    obj.getCoverage().convertAllToNew();
                    obj.getSuminsureds().convertAllToNew();
                    obj.getDeductibles().convertAllToNew();
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        obj.getTreaties().convertAllToNew();
                        
                        final DTOList treatyDetail = obj.getTreatyDetails();
                        treatyDetail.convertAllToNew();
                        
                        for (int j = 0; j < treatyDetail.size(); j++) {
                            InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);
                            
                            final DTOList shares = tredet1.getShares();
                            shares.convertAllToNew();
                            
                        }
                    }
                    //end
                }
            }
            

            
            if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
            
            if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
            
            final DTOList details = pol.getDetails();
            
            final HashMap insuranceItemsMap = getInsuranceItemsMap();
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                
                ip.setStPolicyID(pol.getStPolicyID());
                
                if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                
                ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
            }
            
            S.store(details);
            
            final DTOList claimItems = pol.getClaimItems();
            
            for (int i = 0; i < claimItems.size(); i++) {
                InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);
                
                it.setStPolicyID(pol.getStPolicyID());
                
                if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
            }
            
            S.store(claimItems);
            
            final DTOList clausules = (DTOList) pol.getClausules().clone();
            if (clausules != null) {
                
                for (int i = clausules.size() - 1; i >= 0; i--) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                    
                    if (!icl.isSelected()) {
                        clausules.delete(i);
                        continue;
                    }
                    
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(null);
                }
                
                S.store(clausules);
            }
            
            final DTOList claimDocuments = pol.getClaimDocuments();
            
            for (int i = 0; i < claimDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(claimDocuments);
            
            final DTOList policyDocuments = pol.getPolicyDocuments();
            
            for (int i = 0; i < policyDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(policyDocuments);
            
            final DTOList konversiDocuments = pol.getKonversiDocuments();
            
            for (int i = 0; i < konversiDocuments.size(); i++) {
                InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                
                doc.setStPolicyID(pol.getStPolicyID());
                
                final boolean marked = doc.isMarked();
                
                if (marked) {
                    if (doc.getStInsurancePolicyDocumentID() != null)
                        doc.markUpdate();
                    else {
                        doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                        doc.markNew();
                    }
                }
                
                if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
            }
            
            S.store(konversiDocuments);
            
            final DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                
                final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                
                if (pol.isStatusClaim())
                    if (!isClaimObject2) continue;
                
                obj.setStPolicyID(pol.getStPolicyID());
                
                if (obj.isNew()) {
                    final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                    
                    if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                }
                
                S.store(obj);
                
                final DTOList oclaus = obj.getClausules();
                for (int j = 0; j < oclaus.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                    if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                    
                    icl.setStPolicyID(pol.getStPolicyID());
                    icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                }
                S.store(oclaus);
                
                final DTOList suminsureds = obj.getSuminsureds();
                
                for (int j = 0; j < suminsureds.size(); j++) {
                    InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                    if (itsi.isNew())
                        itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                    
                    itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    itsi.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(suminsureds);
                
                final DTOList coverage = obj.getCoverage();
                
                for (int j = 0; j < coverage.size(); j++) {
                    InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                    
                    if (cov.isNew())
                        cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                    cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    cov.setStPolicyID(pol.getStPolicyID());
                    cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                }
                
                S.store(coverage);
                
                final DTOList deductibles = obj.getDeductibles();
                
                for (int j = 0; j < deductibles.size(); j++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                    
                    if (ded.isNew())
                        ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                    ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                    ded.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(deductibles);
                
                //beban load
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                    {
                        // save treaties
                        
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.combineDeleted();
                        
                        for (int l = 0; l < treaties.size(); l++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                            
                            if (tre.isDelete()) tre.getDetails().deleteAll();
                            
                            if (tre.isNew())
                                tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                            
                            tre.setStPolicyID(null);
                            tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                            
                            final DTOList tredetails = tre.getDetails();
                            
                            tredetails.combineDeleted();
                            
                            for (int j = 0; j < tredetails.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                
                                if (tredet.isDelete()) {
                                    tredet.getShares().deleteAll();
                                }
                                
                                if (tredet.isNew())
                                    tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                
                                tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                
                                final DTOList shares = tredet.getShares();
                                
                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                    
                                    if (ri.isNew())
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                    
                                    //tambahan
                                    ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                    //
                                    ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                    ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                    
                                }
                                
                                S.store(shares);
                                
                                
                            }
                            
                            S.store(tredetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                }
                
                    /* remark dolo klaim reas doble
                    {  //save treatis if claim to obtain share details
                        if(pol.isStatusClaimPLA() && !pol.isEffective()){
                            if(pol.isStatusClaimPLA()){
                               final DTOList treaties = obj.getTreaties();
                     
                               treaties.combineDeleted();
                     
                               for (int l = 0; l < treaties.size(); l++) {
                                  InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                     
                                  if (tre.isDelete()) tre.getDetails().deleteAll();
                     
                                  //if (tre.isNew())
                                  tre.markNew();
                                  tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                     
                                  tre.setStPolicyID(null);
                                  tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                     
                                  final DTOList tredetails = tre.getDetails();
                     
                                  tredetails.combineDeleted();
                     
                                  for (int j = 0; j < tredetails.size(); j++) {
                                     InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                     
                                     if (tredet.isDelete()){
                                         tredet.getShares().deleteAll();
                                         tredet.getCoverage().deleteAll();
                                     }
                     
                                     //if (tredet.isNew())
                                     tredet.markNew();
                                     tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                     
                                     tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                     
                                     final DTOList shares = tredet.getShares();
                     
                                     for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                     
                                        //if (ri.isNew())
                                        ri.markNew();
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                     
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                          //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                     
                     
                                         final DTOList cover = ri.getCoverage();
                     
                                         for (int m = 0; m < cover.size(); m++) {
                                            InsurancePolicyCoverReinsView cov = (InsurancePolicyCoverReinsView) cover.get(m);
                     
                                            if (cov.isNew()) cov.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("POLCOVREINS")));
                     
                                            cov.setStInsurancePolicyReinsID(ri.getStInsurancePolicyReinsID());
                                            cov.setStInsuranceTreatySharesID(ri.getStInsuranceTreatySharesID());
                     
                                            cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                            cov.setStPolicyID(pol.getStPolicyID());
                     
                                            //tambahan
                                            cov.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                              //
                                            cov.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                            cov.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                         }
                     
                                         S.store(cover);
                                     }
                     
                                     S.store(shares);
                                  }
                     
                                  S.store(tredetails);
                               }
                     
                               S.store(treaties);
                           }
                        }
                     
                    }*/
                
            }
            
            final DTOList deletedObjects = objects.getDeleted();
            
            if (deletedObjects != null)
                for (int i = 0; i < deletedObjects.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                
                S.store(obj);
                //S.store(obj.getClausules());
                S.store(obj.getSuminsureds());
                S.store(obj.getCoverage());
                
                //beban load
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                    final DTOList treaties = obj.getTreaties();
                    
                    treaties.deleteAll();
                    treaties.combineDeleted();
                    
                    for (int j = 0; j < treaties.size(); j++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                        
                        final DTOList treDetails = tre.getDetails();
                        
                        treDetails.deleteAll();
                        treDetails.combineDeleted();
                        
                        for (int k = 0; k < treDetails.size(); k++) {
                            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                            
                            final DTOList treShares = trd.getShares();
                            treShares.deleteAll();
                            treShares.combineDeleted();
                            
                            S.store(treShares);
                        }
                        
                        S.store(treDetails);
                        
                    }
                    
                    S.store(treaties);
                }
                }
            
            final DTOList entities = pol.getEntities();
            
            for (int i = 0; i < entities.size(); i++) {
                InsurancePolicyEntityView entityView = (InsurancePolicyEntityView) entities.get(i);
                entityView.setStPolicyID(pol.getStPolicyID());
                
                if (entityView.isNew())
                    entityView.setStPolicyEntityID(String.valueOf(IDFactory.createNumericID("INSPOLENT")));
                
                if (entityView.isModified()) {
                    entityView.setStEntityID(getRemoteEntityManager().save(entityView.getEntity()));
                }
                
                S.store(entityView);
                
            }
            
            final DTOList coins = pol.getCoins2();
            
            for (int i = 0; i < coins.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coins);
            
            final DTOList coinscoverage = pol.getCoinsCoverage();
            
            for (int i = 0; i < coinscoverage.size(); i++) {
                InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                
                if (coin.isNew())
                    coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                
                coin.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store2(coinscoverage);
            
                /*
                final DTOList coveragereinsurance = pol.getCoverageReinsurance();
                 
                for (int i = 0; i < coveragereinsurance.size(); i++) {
                    InsurancePolicyCoverReinsView coverreins = (InsurancePolicyCoverReinsView) coveragereinsurance.get(i);
                 
                    if (coverreins.isNew())
                        coverreins.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOVERAGEREINS")));
                 
                    coverreins.setStPolicyID(pol.getStPolicyID());
                }
                 
                S.store2(coveragereinsurance);*/
            
            final DTOList installment = pol.getInstallment();
            
            for (int i = 0; i < installment.size(); i++) {
                InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                
                inst.setStPolicyID(pol.getStPolicyID());
                
                if (inst.isNew())
                    inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
            }
            
            S.store2(installment);
            
            final DTOList deductibles = pol.getDeductibles();
            
            for (int i = 0; i < deductibles.size(); i++) {
                InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);
                
                if (ded.isNew())
                    ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                
                ded.setStPolicyID(pol.getStPolicyID());
            }
            
            S.store(deductibles);
            
                /*// take care deleted treaties
                {
                   final DTOList deletedTreaties = pol.getTreaties().getDeleted();
                 
                   if (deletedTreaties!=null)
                      for (int i = 0; i < deletedTreaties.size(); i++) {
                         InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) deletedTreaties.get(i);
                 
                         S.store(tre);
                 
                         final DTOList tredetails = tre.getDetails();
                 
                         for (int j = 0; j < tredetails.size(); j++) {
                            InsuranceTreatyDetailView tredet = (InsuranceTreatyDetailView) tredetails.get(j);
                 
                            tredet.markDelete();
                 
                            S.store(tredet);
                 
                            final DTOList treShares = tredet.getShares();
                 
                            for (int k = 0; k < treShares.size(); k++) {
                               InsurancePolicyReinsView rei = (InsurancePolicyReinsView) treShares.get(k);
                 
                               rei.markDelete();
                 
                               S.store(rei);
                            }
                         }
                      }
                }*/
            
            
            if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                {
                    // save treaties
                    
                    final DTOList treaties = pol.getTreaties();
                    
                    treaties.combineDeleted();
                    
                    for (int l = 0; l < treaties.size(); l++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                        
                        if (tre.isDelete()) tre.getDetails().deleteAll();
                        
                        if (tre.isNew())
                            tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                        
                        tre.setStPolicyID(pol.getStPolicyID());
                        
                        final DTOList tredetails = tre.getDetails();
                        
                        tredetails.combineDeleted();
                        
                        for (int j = 0; j < tredetails.size(); j++) {
                            InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                            
                            if (tredet.isDelete()) tredet.getShares().deleteAll();
                            
                            if (tredet.isNew())
                                tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                            
                            tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                            
                            final DTOList shares = tredet.getShares();
                            
                            for (int k = 0; k < shares.size(); k++) {
                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                
                                if (ri.isNew())
                                    ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                
                                ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                
                                ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                
                            }
                            
                            S.store(shares);
                            
                        }
                        
                        S.store(tredetails);
                        
                    }
                    
                    S.store(treaties);
                }
            }
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getStPolicyTypeID().equalsIgnoreCase("51") && pol.getStCostCenterCode().equalsIgnoreCase("24")){
                    pol.checkPolicyNoBefore(pol.getStPolicyNo());
                }else{
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }
            
            //if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus))
            //pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            if(approvalMode){
                if (postflagchanged) {
                    
                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                        
                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy() || pol.isStatusTemporaryEndorsemen()) {
                            
                            if (isFreeInstallment) postARInstallmentFree(pol);
                            else postARInstallment(pol);
                            
                            postCoas(pol);
                            
                            //if(pol.getObjects().size()<=1000)
                            postReasCumullation(pol);
                            
                            postLimitZoneBalance(pol);
                            
                            //simpen buat sistem lama
                            saveABAProduk(pol);
                            
                            saveABABayar(pol);
                            
                            saveABAHutang(pol);
                            
                            saveABAPajak(pol);
                            
                            //saveABAKreasi(pol);
                            
                            //saveABAProdukDBF2(pol);
                            
                            //saveABABayarDBF(pol);
                            
                            //saveABAHutangDBF(pol);
                            
                            //saveABAPajakDBF(pol);
                            
                        }
                        
                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                            
                            postReasClaim(pol);
                            
                            postAPClaim(pol);
                            
                            postARCoas(pol);
                        }
                        
                        if(pol.isStatusEndorseRI()){
                            postReasCumullation(pol);
                        }
                        
                    } else {
                        // unpost
                    }
                }
            }
            
            return pol.getStPolicyID();
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    } 
    
    private void postReasCumullation(InsurancePolicyView pol) throws Exception {
        
        UserSession us = SessionManager.getInstance().getSession();
        
        final DTOList reins = pol.getReins();
        
        for (int i = 0; i < reins.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) reins.get(i);
            
            if(ri.getTreatyDetail().isOR()) continue;
            
            if (BDUtil.isZeroOrNull(ri.getDbPremiAmount())) continue;
            
            if (ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) continue;
            
            if(ri.getTreatyDetail().getTreatyType().getStJournalFlag()==null) continue;
            
            if (ri.getTreatyDetail().getStARTrxLineID() == null) continue;
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStRefID0("REINS");
            //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
            // I 01 040120200913001000

            invoice.setStInvoiceNo("I" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(ri.getStMemberEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            
            invoice.setStARTransactionTypeID(ri.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());
            
            invoice.setStReferenceD0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
            invoice.setStReferenceD1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
            
            invoice.setStReferenceE0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
            invoice.setStReferenceE1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
            
            invoice.setStReferenceZ0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode3());
            invoice.setStReferenceZ1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceC0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode6());
            invoice.setStReferenceC1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
            
            invoice.setStReferenceA1(ri.getStRISlipNo());
            
            //bikin surat hutang
            //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
            //"218/BPDAN/2/2011"
            if(ri.getDtValidReinsuranceDate()==null){
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()+
                        "/"+
                        ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                        "/"+
                        DateUtil.getQuartalRomawi(pol.getDtPolicyDate())+
                        "/"+
                        DateUtil.getYear(pol.getDtPolicyDate()));
            }
            
            if(ri.getDtValidReinsuranceDate()!=null){
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()+ "."+
                        String.valueOf(i+1) +
                        "/"+
                        ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                        "/"+
                        DateUtil.getQuartalRomawi(pol.getDtPolicyDate())+
                        "/"+
                        DateUtil.getYear(pol.getDtPolicyDate()));
            }
            
            
            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line2 where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );
            
            {
                
                for (int v = 0; v < artlines.size(); v++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);
                    
                    if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(ri.getDbPremiAmount());
                        
                        if(ri.getDbPremiAmountEdited()!=null){
                            ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbPremiAmount(), ri.getDbPremiAmountEdited()));
                        }
                    } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {
                        
                        if (BDUtil.isZeroOrNull(ri.getDbRICommAmount())) continue;
                        
                        if (invoice.getStReferenceZ0() == null)
                            throw new RuntimeException("Comission Account Code not found for " + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());
                        
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivdetails.add(ivd);
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(ri.getDbRICommAmount());
                        
                        if(ri.getDbRICommAmountEdited()!=null){
                            ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbRICommAmount(),ri.getDbRICommAmountEdited()));
                        }
                    } //else throw new RuntimeException("Unknown Item class : " + artl);
                }
            }
                    
                    invoice.recalculate();
                    
                    getRemoteAccountReceivable().saveInvoiceReins(invoice);
        }
        
        
    }

    public void saveInvoiceClaimByDate(InsurancePolicyView pol2) throws Exception {
        try {

            final SQLUtil S = new SQLUtil();

                InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol2.getStPolicyID());
            
                boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
               
                if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal()) {

                    if (isFreeInstallment) postARInstallmentFree(pol);
                    else postARInstallment(pol);

                    postCoas(pol);

                    postReasCumullation(pol);
                    
                    pol.setStReference11("Y");
                    pol.markUpdate();
                    S.store(pol);
                }
                
                if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                   
                    postReasClaim(pol);

                    postAPClaim(pol);

                    postARCoas(pol);
                    
                    pol.setStReference11("Y");
                    pol.markUpdate();
                    S.store(pol);
                }
            
                if(pol.isStatusEndorseRI()){
                    postReasCumullation(pol);
                    
                    pol.setStReference11("Y");
                    pol.markUpdate();
                    S.store(pol);
                }
            
            S.release();
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        }
        
        
    }

    
    public void saveBackup(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();

        try {

            //not approval mode
            if (!approvalMode) {
                if (stNextStatus != null) {
                    
                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
                    
                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)) {
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();
                            
                            oldPolis.setStActiveFlag("N");
                            
                            S.store(oldPolis);
                        }
                    }
                    
                    //pol.setStParentID(pol.getStPolicyID());
                    
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());
                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    pol.setStApprovedWho(null);
                    pol.setDtApprovedDate(null);
                    
                    pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();
                    
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList policyDocuments = pol.getPolicyDocuments();
                    
                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList konversiDocuments = pol.getKonversiDocuments();
                    
                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();
                    
                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;
                        
                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                        
                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        
                        obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();
                        
                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                            obj.getTreaties().convertAllToNew();
                            
                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();
                            
                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);
                                
                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();
                                
                            }
                        }
                        //end
                    }
                }
                
                
                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
 
                final DTOList details = pol.getDetails();
                
                final HashMap insuranceItemsMap = getInsuranceItemsMap();
                
                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                    
                    ip.setStPolicyID(pol.getStPolicyID());
                    
                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    
                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }
                
                S.store(details);
                
                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();
                
                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.store(claimItems);
                    
                    final DTOList claimDocuments = pol.getClaimDocuments();
                
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.store(claimDocuments);
                }

                final DTOList clausules = (DTOList) pol.getClausules().clone();
                if (clausules != null) {
                    
                    for (int i = clausules.size() - 1; i >= 0; i--) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                        
                        if (!icl.isSelected()) {
                            clausules.delete(i);
                            continue;
                        }
                        
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                    }
                    
                    S.store(clausules);
                }
                
                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(policyDocuments);
                
                final DTOList konversiDocuments = pol.getKonversiDocuments();
                
                for (int i = 0; i < konversiDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(konversiDocuments);
                
                final DTOList objects = pol.getObjects();
                
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                    
                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;
                    
                    obj.setStPolicyID(pol.getStPolicyID());
                    
                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                        
                        if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                    }
                    
                    S.store(obj); 
                    
                    final DTOList oclaus = obj.getClausules();
                    for (int j = 0; j < oclaus.size(); j++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                    }
                    S.store(oclaus);
                    
                    final DTOList suminsureds = obj.getSuminsureds();
                    
                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                        
                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(suminsureds);
                    
                    final DTOList coverage = obj.getCoverage();
                    
                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                        
                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                    }
                    
                    S.store(coverage);
                    
                    final DTOList deductibles = obj.getDeductibles();
                    
                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                        
                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(deductibles);
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        {
                            // save treaties
                            
                            final DTOList treaties = obj.getTreaties();
                            
                            treaties.combineDeleted();
                            
                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                                
                                if (tre.isDelete()) tre.getDetails().deleteAll();
                                
                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                                
                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                
                                final DTOList tredetails = tre.getDetails();
                                
                                tredetails.combineDeleted();
                                
                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                    
                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }
                                    
                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                    
                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                    
                                    final DTOList shares = tredet.getShares();
                                    
                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                        
                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                        
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                        
                                    }
                                    S.store(shares);
                                }
                                
                                S.store(tredetails);
                                
                            }
                            
                            S.store(treaties);
                        }
                    }
                    
                    /* remark dolo klaim reas doble
                    {  //save treatis if claim to obtain share details
                        if(pol.isStatusClaimPLA() && !pol.isEffective()){
                            if(pol.isStatusClaimPLA()){
                               final DTOList treaties = obj.getTreaties();
                     
                               treaties.combineDeleted();
                     
                               for (int l = 0; l < treaties.size(); l++) {
                                  InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                     
                                  if (tre.isDelete()) tre.getDetails().deleteAll();
                     
                                  //if (tre.isNew())
                                  tre.markNew();
                                  tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                     
                                  tre.setStPolicyID(null);
                                  tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                     
                                  final DTOList tredetails = tre.getDetails();
                     
                                  tredetails.combineDeleted();
                     
                                  for (int j = 0; j < tredetails.size(); j++) {
                                     InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                     
                                     if (tredet.isDelete()){
                                         tredet.getShares().deleteAll();
                                         tredet.getCoverage().deleteAll();
                                     }
                     
                                     //if (tredet.isNew())
                                     tredet.markNew();
                                     tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                     
                                     tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                     
                                     final DTOList shares = tredet.getShares();
                     
                                     for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                     
                                        //if (ri.isNew())
                                        ri.markNew();
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                     
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                          //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                     
                     
                                         final DTOList cover = ri.getCoverage();
                     
                                         for (int m = 0; m < cover.size(); m++) {
                                            InsurancePolicyCoverReinsView cov = (InsurancePolicyCoverReinsView) cover.get(m);
                     
                                            if (cov.isNew()) cov.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("POLCOVREINS")));
                     
                                            cov.setStInsurancePolicyReinsID(ri.getStInsurancePolicyReinsID());
                                            cov.setStInsuranceTreatySharesID(ri.getStInsuranceTreatySharesID());
                     
                                            cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                            cov.setStPolicyID(pol.getStPolicyID());
                     
                                            //tambahan
                                            cov.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                              //
                                            cov.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                            cov.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                         }
                     
                                         S.store(cover);
                                     }
                     
                                     S.store(shares);
                                  }
                     
                                  S.store(tredetails);
                               }
                     
                               S.store(treaties);
                           }
                        }
                     
                    }*/
                    
                }
                
                //S.store(objects);
                
                final DTOList deletedObjects = objects.getDeleted();
                
                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                    
                    S.store(obj); 
                    //S.store(obj.getClausules());
                    S.store(obj.getSuminsureds());
                    S.store(obj.getCoverage());
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.deleteAll();
                        treaties.combineDeleted();
                        
                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                            
                            final DTOList treDetails = tre.getDetails();
                            
                            treDetails.deleteAll();
                            treDetails.combineDeleted();
                            
                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                                
                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();
                                
                                S.store(treShares);
                            }
                            
                            S.store(treDetails);
                            
                        }
                        
                        S.store(treaties);
                      }
                    }

                /*
                final DTOList entities = pol.getEntities();
                
                for (int i = 0; i < entities.size(); i++) {
                    InsurancePolicyEntityView entityView = (InsurancePolicyEntityView) entities.get(i);
                    entityView.setStPolicyID(pol.getStPolicyID());
                    
                    if (entityView.isNew())
                        entityView.setStPolicyEntityID(String.valueOf(IDFactory.createNumericID("INSPOLENT")));
                    
                    if (entityView.isModified()) {
                        entityView.setStEntityID(getRemoteEntityManager().save(entityView.getEntity()));
                    }
                    
                    S.store(entityView);
                    
                }*/
                
                final DTOList coins = pol.getCoins2();
                
                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coins);
                
                final DTOList coinscoverage = pol.getCoinsCoverage();
                
                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coinscoverage);
                
                /*
                final DTOList coveragereinsurance = pol.getCoverageReinsurance();
                 
                for (int i = 0; i < coveragereinsurance.size(); i++) {
                    InsurancePolicyCoverReinsView coverreins = (InsurancePolicyCoverReinsView) coveragereinsurance.get(i);
                 
                    if (coverreins.isNew())
                        coverreins.setStInsurancePolicyCoverReinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOVERAGEREINS")));
                 
                    coverreins.setStPolicyID(pol.getStPolicyID());
                }
                 
                S.store2(coveragereinsurance);*/
                
                final DTOList installment = pol.getInstallment();
                
                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                    
                    inst.setStPolicyID(pol.getStPolicyID());
                    
                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
                }
                
                S.store2(installment);
                
                /*
                final DTOList deductibles = pol.getDeductibles();
                
                for (int i = 0; i < deductibles.size(); i++) {
                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(i);
                    
                    if (ded.isNew())
                        ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                    
                    ded.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store(deductibles);*/
                
                /*// take care deleted treaties
                {
                   final DTOList deletedTreaties = pol.getTreaties().getDeleted();
                 
                   if (deletedTreaties!=null)
                      for (int i = 0; i < deletedTreaties.size(); i++) {
                         InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) deletedTreaties.get(i);
                 
                         S.store(tre);
                 
                         final DTOList tredetails = tre.getDetails();
                 
                         for (int j = 0; j < tredetails.size(); j++) {
                            InsuranceTreatyDetailView tredet = (InsuranceTreatyDetailView) tredetails.get(j);
                 
                            tredet.markDelete();
                 
                            S.store(tredet);
                 
                            final DTOList treShares = tredet.getShares();
                 
                            for (int k = 0; k < treShares.size(); k++) {
                               InsurancePolicyReinsView rei = (InsurancePolicyReinsView) treShares.get(k);
                 
                               rei.markDelete();
                 
                               S.store(rei);
                            }
                         }
                      }
                }*/
                
                /*
                if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                    {
                        // save treaties
                        
                        final DTOList treaties = pol.getTreaties();
                        
                        treaties.combineDeleted();
                        
                        for (int l = 0; l < treaties.size(); l++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                            
                            if (tre.isDelete()) tre.getDetails().deleteAll();
                            
                            if (tre.isNew())
                                tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                            
                            tre.setStPolicyID(pol.getStPolicyID());
                            
                            final DTOList tredetails = tre.getDetails();
                            
                            tredetails.combineDeleted();
                            
                            for (int j = 0; j < tredetails.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                
                                if (tredet.isDelete()) tredet.getShares().deleteAll();
                                
                                if (tredet.isNew())
                                    tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                
                                tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                
                                final DTOList shares = tredet.getShares();
                                
                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                    
                                    if (ri.isNew())
                                        ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                    
                                    ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                    
                                    ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                    ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                    
                                }
                                
                                S.store(shares);
                                
                            }
                            
                            S.store(tredetails);
                            
                        }
                        
                        S.store(treaties);
                    }
                }*/
            }//end not approval mode
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getStPolicyTypeID().equalsIgnoreCase("51") && pol.getStCostCenterCode().equalsIgnoreCase("24")){
                    pol.checkPolicyNoBefore(pol.getStPolicyNo());
                }else{
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }
            
            //if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus))
            //pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
            if(approvalMode){
                if (postflagchanged) {
                    
                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                        
                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {
                            
                            if (isFreeInstallment) postARInstallmentFree(pol);
                            else postARInstallment(pol);
                            
                            postCoas(pol);
                            
                            //postReasCumullation(pol);
                            //postReas(pol);
                            
                            postLimitZoneBalance(pol);
                            
                            //simpen buat sistem lama
                            saveABAProduk(pol);
                            
                            saveABABayar(pol);
                            
                            saveABAHutang(pol);
                            
                            saveABAPajak(pol);
                            
                            //saveABAKreasi(pol);
                            
                            //saveABAProdukDBF2(pol);
                            
                            //saveABABayarDBF(pol);
                            
                            //saveABAHutangDBF(pol);
                            
                            //saveABAPajakDBF(pol);
                            
                        }
                        
                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                            
                            postReasClaim(pol);
                            
                            postAPClaim(pol);
                            
                            postARCoas(pol);
                        }
                        
                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }
                        
                    } else {
                        // unpost
                    }
                }
            }
          
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public String saveEndorseBGSB(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();

        try {
            /*
            final PreparedStatement PS = S.setQuery("BEGIN;");
            PS.execute();
            S.releaseResource();*/
        
            //not approval mode
                if (stNextStatus != null) {
                    
                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
                    
                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)) {
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();
                            
                            oldPolis.setStActiveFlag("N");
                            
                            S.store(oldPolis);
                        }
                    }
                    
                    //pol.setStParentID(pol.getStPolicyID());
                    
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());
                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    pol.setStApprovedWho(null);
                    pol.setDtApprovedDate(null);
                    
                    pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();
                    
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList policyDocuments = pol.getPolicyDocuments();
                    
                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList konversiDocuments = pol.getKonversiDocuments();
                    
                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();
                    
                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;
                        
                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                        
                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        
                        obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();
                        
                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                            obj.getTreaties().convertAllToNew();
                            
                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();
                            
                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);
                                
                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();
                                
                            }
                        }
                        //end
                    }
                }
                
                
                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
 
                final DTOList details = pol.getDetails();
                
                final HashMap insuranceItemsMap = getInsuranceItemsMap();
                
                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                    
                    ip.setStPolicyID(pol.getStPolicyID());
                    
                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    
                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }
                
                S.store(details);
                
                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();
                
                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.store(claimItems);
                    
                    final DTOList claimDocuments = pol.getClaimDocuments();
                
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.store(claimDocuments);
                }

                final DTOList clausules = (DTOList) pol.getClausules().clone();
                if (clausules != null) {
                    
                    for (int i = clausules.size() - 1; i >= 0; i--) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                        
                        if (!icl.isSelected()) {
                            clausules.delete(i);
                            continue;
                        }
                        
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                    }
                    
                    S.store(clausules);
                }
                
                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(policyDocuments);
                
                final DTOList konversiDocuments = pol.getKonversiDocuments();
                
                for (int i = 0; i < konversiDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(konversiDocuments);
                
                final DTOList objects = pol.getObjects();
                
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                    
                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;
                    
                    obj.setStPolicyID(pol.getStPolicyID());
                    
                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                        
                        if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                    }
                    
                    S.store(obj); 
                    
                    final DTOList oclaus = obj.getClausules();
                    for (int j = 0; j < oclaus.size(); j++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                    }
                    S.store(oclaus);
                    
                    final DTOList suminsureds = obj.getSuminsureds();
                    
                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                        
                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(suminsureds);
                    
                    final DTOList coverage = obj.getCoverage();
                    
                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                        
                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                    }
                    
                    S.store(coverage);
                    
                    final DTOList deductibles = obj.getDeductibles();
                    
                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                        
                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(deductibles);
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        {
                            // save treaties
                            
                            final DTOList treaties = obj.getTreaties();
                            
                            treaties.combineDeleted();
                            
                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                                
                                if (tre.isDelete()) tre.getDetails().deleteAll();
                                
                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                                
                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                
                                final DTOList tredetails = tre.getDetails();
                                
                                tredetails.combineDeleted();
                                
                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                    
                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }
                                    
                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                    
                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                    
                                    final DTOList shares = tredet.getShares();
                                    
                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                        
                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                        
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                        
                                    }
                                    S.store(shares);
                                }
                                
                                S.store(tredetails);
                                
                            }
                            
                            S.store(treaties);
                        }
                    }
                }
                
                final DTOList deletedObjects = objects.getDeleted();
                
                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                    
                    S.store(obj); 
                    //S.store(obj.getClausules());
                    S.store(obj.getSuminsureds());
                    S.store(obj.getCoverage());
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.deleteAll();
                        treaties.combineDeleted();
                        
                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                            
                            final DTOList treDetails = tre.getDetails();
                            
                            treDetails.deleteAll();
                            treDetails.combineDeleted();
                            
                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                                
                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();
                                
                                S.store(treShares);
                            }
                            
                            S.store(treDetails);
                            
                        }
                        
                        S.store(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();
                
                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coins);
                
                final DTOList coinscoverage = pol.getCoinsCoverage();
                
                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coinscoverage);

                final DTOList installment = pol.getInstallment();
                
                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                    
                    inst.setStPolicyID(pol.getStPolicyID());
                    
                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
                }
                
                S.store2(installment);
                

            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getStPolicyTypeID().equalsIgnoreCase("51") && pol.getStCostCenterCode().equalsIgnoreCase("24")){
                    pol.checkPolicyNoBefore(pol.getStPolicyNo());
                }else{
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();
            
            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && pol.getStManualInstallmentFlag().equalsIgnoreCase("Y");
            
                //if (postflagchanged) {
                    
                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                        
                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {
                            
                            if (isFreeInstallment) postARInstallmentFree(pol);
                            else postARInstallment(pol);
                            
                            postCoas(pol);
                            
                            //postReasCumullation(pol);
                            //postReas(pol);
                            
                            //postLimitZoneBalance(pol);
                            
                            //simpen buat sistem lama
                            saveABAProduk(pol);
                            
                            saveABABayar(pol);
                            
                            saveABAHutang(pol);
                            
                            saveABAPajak(pol);

                        }
                        
                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                            
                            postReasClaim(pol);
                            
                            postAPClaim(pol);
                            
                            postARCoas(pol);
                        }
                        
                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }
                        
                    } else {
                        // unpost
                    }
                //}
            
            
            return pol.getStPolicyID();
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
    
    public InsuranceProdukView getInsuranceProduk(String stPolicyID) throws Exception {
        final InsuranceProdukView pol = (InsuranceProdukView) ListUtil.getDTOListFromQuery("select * from aba_produk where pol_id = ?",
                new Object[]{stPolicyID},
                InsuranceProdukView.class).getDTO();
        
        return pol;
    }
    
    public InsuranceBayar1View getInsuranceBayar(String stPolicyID) throws Exception {
        final InsuranceBayar1View pol = (InsuranceBayar1View) ListUtil.getDTOListFromQuery("select * from aba_bayar1 where pol_id = ?",
                new Object[]{stPolicyID},
                InsuranceBayar1View.class).getDTO();
        
        return pol;
    }
    
    public InsuranceHutangView getInsuranceHutang(String stPolicyID) throws Exception {
        final InsuranceHutangView pol = (InsuranceHutangView) ListUtil.getDTOListFromQuery("select * from aba_hutang where pol_id = ?",
                new Object[]{stPolicyID},
                InsuranceHutangView.class).getDTO();
        
        return pol;
    }
    
    public InsurancePajakView getInsurancePajak(String stPolicyID) throws Exception {
        final InsurancePajakView pol = (InsurancePajakView) ListUtil.getDTOListFromQuery("select * from aba_pajak where pol_id = ?",
                new Object[]{stPolicyID},
                InsurancePajakView.class).getDTO();
        
        return pol;
    }
    
     public void updateTransferProd(DTOList data, ProductionUtilitiesReportForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);
                
                PreparedStatement PS = S.setQuery("update ins_policy set ref12 = 'Y' where pol_id = ? ");
                
                PS.setObject(1,polis.getStPolicyID());
                
                int s = PS.executeUpdate();
            }
            
        } finally{
            S.release();
        }
    }
    
    public void updateTransferProduk(DTOList data, ProductionUtilitiesReportForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);
                
                PreparedStatement PS = S.setQuery("update aba_produk set flag = 'Y' where pol_id = ? ");
                
                PS.setObject(1,polis.getStPolicyID());
                
                int s = PS.executeUpdate();
            }
            
        } finally{
            S.release();
        }
    }
   
    public void updateTransferBayar1(DTOList data, ProductionReportForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);
                
                PreparedStatement PS = S.setQuery("update aba_bayar1 set flag = 'Y' where pol_id = ? ");
                
                PS.setObject(1,polis.getStPolicyID());
                
                int s = PS.executeUpdate();
            }
            
        } finally{
            S.release();
        }
    }
    
    public void updateTransferHutang(DTOList data, ProductionReportForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);
                
                PreparedStatement PS = S.setQuery("update aba_hutang set flag = 'Y' where pol_id = ? ");
                
                PS.setObject(1,polis.getStPolicyID());
                
                int s = PS.executeUpdate();
            }
            
        } finally{
            S.release();
        }
    }
    
    public void updateTransferPajak(DTOList data, ProductionReportForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);
                
                PreparedStatement PS = S.setQuery("update aba_pajak set flag = 'Y' where pol_id = ? ");
                
                PS.setObject(1,polis.getStPolicyID());
                
                int s = PS.executeUpdate();
            }
            
        } finally{
            S.release();
        }
    }
    
    public void updateMonitoring(DTOList data, ReceiptForm form) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            for (int i = 0; i < data.size(); i++ ) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) data.get(i);
                
                ARInvoiceView ari = rcl.getInvoice();
                
                if (ari.getStClaimNo()!=null) continue;
                
                ari.markUpdate();
                ari.setStClaimNo(form.getStReceiptNo());
                ari.setStClaimName(form.getStName());
                
                S.store(ari);
            }
            
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
            
        } finally{
            S.release();
        }
    }
    
    public void save(InsuranceRiskCategoryView risk) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (risk.isNew())
                risk.setStInsuranceRiskCategoryID(String.valueOf(IDFactory.createNumericID("INS_RISK_CAT")));

            if (Tools.isNo(risk.getStExcRiskFlag())) {
                risk.setStExcRiskFlag(null);
            }

            S.store(risk);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }
    
    public void save(InsuranceClausulesView clausules) throws Exception {
        SQLUtil S = new SQLUtil();
        
        try {

            if(clausules.getStCostCenterCode()!=null){
                if (clausules.isNew()) clausules.setStInsuranceClauseID(String.valueOf(IDFactory.createNumericID("INS_CLAUSULES")));

                clausules.setStLevel("POLICY");
                clausules.setStActiveFlag("Y");

                S.store(clausules);
            }else if(clausules.getStCostCenterCode()==null){
                final DTOList cabang = clausules.getCabang();

                for (int i = 0; i < cabang.size(); i++) {
                    GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

                    if (clausules.isNew()) clausules.setStInsuranceClauseID(String.valueOf(IDFactory.createNumericID("INS_CLAUSULES")));

                    clausules.setStLevel("POLICY");
                    clausules.setStActiveFlag("Y");
                    clausules.setStCostCenterCode(cab.getStCostCenterCode());

                    S.store(clausules);
                }

            }
            
            
        } catch (Exception e) {
            
            ctx.setRollbackOnly();
            
            throw e;
            
        } finally {
            S.release();
        }
    }
    
    public void saveObjectToOtherTable(InsurancePolicyView pol) throws Exception{
        final Connection conn = ConnectionCache.getInstance().getConnection();
        
        try{

            final String proses = "insert into ins_pol_obj_searching "+
                                   " select *  "+
                                   "  from ins_pol_obj "+
                                   "  where pol_id = " + pol.getStPolicyID();

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final boolean b = S.execute(proses);

            S.close();

            t = System.currentTimeMillis() - t;

            //logger.logInfo(">> proses inject object searching selesai dalam " + t + " ms");
            
        }finally{
            conn.close();
        }

    }
    
    public PerjanjianKerjasamaView getPerjanjianKerjasama(String stPolicyID) throws Exception {
        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) ListUtil.getDTOListFromQuery("select * from perjanjian_kerjasama where pks_id = ?",
                new Object[]{stPolicyID},
                PerjanjianKerjasamaView.class).getDTO();
        
        return pol;
    }
    
     public void savePerjanjianKerjasama(PerjanjianKerjasamaView pol, String stNextStatus) throws Exception {
        pol = (PerjanjianKerjasamaView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();
        
        try {
            
            if (stNextStatus != null) {
                //pol.setStParentID(pol.getStPolicyID());
                
                if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                else pol.setStParentID(pol.getStPolicyID());
                pol.setStRootID(pol.getStRootID());
                pol.markNew();
                pol.setStActiveFlag("Y");
                pol.setStStatus(stNextStatus);
                
            }
            
            if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("PKS")));
            
            
            if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            S.store(pol);
            
        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

     public void saveAutoEndorse(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);
        
        final SQLUtil S = new SQLUtil();
        
        UserSession us = S.getUserSession();

        try {
 
            //not approval mode
            //if (!approvalMode) {
                if (stNextStatus != null) {
                    
                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());
                    
                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)) 
                    {
                        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)){
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();
                            
                            oldPolis.setStAdminNotes("RENEWAL");
                            
                            S.store(oldPolis);
                        }
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            
                            if(!oldPolis.isStatusPolicy() && !oldPolis.isStatusRenewal()){
                                oldPolis.markUpdate();
                            
                                oldPolis.setStActiveFlag("N");
                            
                                S.store(oldPolis);
                            }
                        }
                    }
                    
                    //pol.setStParentID(pol.getStPolicyID());
                    
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());
                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    
                    pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();
                    
                    pol.getClaimItems().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();
                    
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList policyDocuments = pol.getPolicyDocuments();
                    
                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList konversiDocuments = pol.getKonversiDocuments();
                    
                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }
                    
                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();
                    
                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;
                        
                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());
                        
                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        
                        obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();
                        
                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                            obj.getTreaties().convertAllToNew();
                            
                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();
                            
                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);
                                
                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();
                                
                            }
                        }
                        //end
                    }
                }
                
                
                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));
                
                pol_id = pol.getStPolicyID();

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());
 
                final DTOList details = pol.getDetails();
                
                final HashMap insuranceItemsMap = getInsuranceItemsMap();
                
                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);
                    
                    ip.setStPolicyID(pol.getStPolicyID());
                    
                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    
                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }
                
                S.store(details);
                
                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();
                
                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.store(claimItems);
                    
                    final DTOList claimDocuments = pol.getClaimDocuments();
                
                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.store(claimDocuments);
                }

                
                final DTOList clausules = (DTOList) pol.getClausules().clone();
                if (clausules != null) {
                    
                    for (int i = clausules.size() - 1; i >= 0; i--) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
                        
                        if (!icl.isSelected()) {
                            clausules.delete(i);
                            continue;
                        }
                        
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                    }
                    S.store(clausules);
                }
                
                final DTOList policyDocuments = pol.getPolicyDocuments();
                
                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(policyDocuments);
                
                final DTOList konversiDocuments = pol.getKonversiDocuments();
                
                for (int i = 0; i < konversiDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                    
                    doc.setStPolicyID(pol.getStPolicyID());
                    
                    final boolean marked = doc.isMarked();
                    
                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }
                    
                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }
                
                S.store(konversiDocuments);
                
                final DTOList objects = pol.getObjects();
                
                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                    
                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                    
                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;
                    
                    obj.setStPolicyID(pol.getStPolicyID());
                    
                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());
                        
                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));
                        
                        if (isClaimObject) pol.setStClaimObjectID(obj.getStPolicyObjectID());
                    }
                    
                    S.store(obj); 
                    
                    
                    final DTOList oclaus = obj.getClausules();
                    for (int j = 0; j < oclaus.size(); j++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));
                        
                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                    }
                    
                    S.store(oclaus);
                    
                    final DTOList suminsureds = obj.getSuminsureds();
                    
                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));
                        
                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(suminsureds);
                    
                    final DTOList coverage = obj.getCoverage();
                    
                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);
                        
                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                    }
                    
                    S.store(coverage);
                    
                    final DTOList deductibles = obj.getDeductibles();
                    
                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);
                        
                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());
                    }
                    
                    S.store(deductibles);
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        {
                            // save treaties
                            
                            final DTOList treaties = obj.getTreaties();
                            
                            treaties.combineDeleted();
                            
                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);
                                
                                if (tre.isDelete()) tre.getDetails().deleteAll();
                                
                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));
                                
                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                                
                                final DTOList tredetails = tre.getDetails();
                                
                                tredetails.combineDeleted();
                                
                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);
                                    
                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }
                                    
                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));
                                    
                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());
                                    
                                    final DTOList shares = tredet.getShares();
                                    
                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
                                        
                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));
                                        
                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());
                                        
                                    }
                                    S.store(shares);
                                }
                                
                                S.store(tredetails);
                                
                            }
                            
                            S.store(treaties);
                        }
                    }
                }
                
                final DTOList deletedObjects = objects.getDeleted();
                
                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);
                    
                    S.store(obj); 
                    //S.store(obj.getClausules());
                    S.store(obj.getSuminsureds());
                    S.store(obj.getCoverage());
                    
                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy()) {
                        final DTOList treaties = obj.getTreaties();
                        
                        treaties.deleteAll();
                        treaties.combineDeleted();
                        
                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);
                            
                            final DTOList treDetails = tre.getDetails();
                            
                            treDetails.deleteAll();
                            treDetails.combineDeleted();
                            
                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);
                                
                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();
                                
                                S.store(treShares);
                            }
                            
                            S.store(treDetails);
                            
                        }
                        
                        S.store(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();
                
                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coins);
                
                final DTOList coinscoverage = pol.getCoinsCoverage();
                
                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);
                    
                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));
                    
                    coin.setStPolicyID(pol.getStPolicyID());
                }
                
                S.store2(coinscoverage);

                final DTOList installment = pol.getInstallment();
                
                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);
                    
                    inst.setStPolicyID(pol.getStPolicyID());
                    
                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
                }
                
                S.store2(installment);
                
            //}//end not approval mode
            
            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());
            
            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(pol.getStStatus())||FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(pol.getStNextStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();

            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){
                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    pol.generatePolicyNo();
                }
            }
            /*
            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus))
                    if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                            pol.generatePLANo();
                    else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                           pol.generateDLANo();*/
            
            S.store(pol);
            
            /*POSTING*/
            
            final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();
            
            final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());
            
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();
            
            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));
            
            boolean manualInstallment = false;
            if(pol.getStManualInstallmentFlag()!=null)
                if(pol.getStManualInstallmentFlag().equalsIgnoreCase("Y"))
                    manualInstallment = true;
            
            boolean isFreeInstallment = pol.getInstallment().size() > 1 && manualInstallment;
            
            if(approvalMode){
               if (postflagchanged) {
                    
                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR
                        
                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {
                            
                            postARInstallmentAcrualBases(pol);
                            
                            postCoasAutoEndorse(pol);
                            
                            //postReasCumullation(pol);
                            //postReas(pol);
                            
                            //postLimitZoneBalance(pol);
                            
                            //simpen buat sistem lama
                            saveABAProduk(pol);
                            
                            saveABABayar(pol);
                            
                            saveABAHutang(pol);
                            
                            saveABAPajak(pol);
                            
                            //saveObjectToOtherTable(pol);

                        }
                        
                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {
                            
                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);

                            if(pol.getStClaimObjectParentID()!=null)
                                    updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                        }
                        
                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }
                        
                    } else {
                        // unpost
                    }
                }
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }
     
     public void updateClaimObjectFlag(String stInsurancePolicyObjectID, String stDLANo) throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        try{
            
            PreparedStatement PS = S.setQuery("update ins_pol_obj set void_flag = 'Y', dla_no = ? where ins_pol_obj_id = ? ");

            PS.setObject(1, stDLANo);
            PS.setObject(2, stInsurancePolicyObjectID);

            int s = PS.executeUpdate();
            
            //if(s==1) logger.logWarning("Berhasil update object klaim : "+stInsurancePolicyObjectID + ", NO LKP : "+ stDLANo);
            
        } finally{
            S.release();
        }
    }
     
    public void postRiskAccumulation(InsurancePolicyView policy) throws Exception {
        try {
 
            if(!policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.BANKGUARANTEE) &&
               !policy.getStPolicyTypeGroupID().equalsIgnoreCase(FinCodec.InsurancePolicyTypeGroup.SURETYBOND))
                    return;
            
            if(policy.getStPolicyTypeID().equalsIgnoreCase("59"))
                return;
            
            final SQLUtil S = new SQLUtil();

            final DTOList object = policy.getObjects();
            
            for (int i = 0; i < object.size(); i++) {
                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);
                
                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;
                
                BigDecimal totalTSI = null;
                
                DTOList suminsured = obj.getSuminsureds();
                for (int j = 0; j < suminsured.size(); j++) {
                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsured.get(j);
                    
                    totalTSI = BDUtil.add(totalTSI, tsi.getDbInsuredAmount());
                }
                
                try
                {
                    PreparedStatement PS = S.setQuery("update ins_pol_risk_accumulation set amount=amount + ? where entity_id=? and pol_type_group_id =? ");

                    PS.setBigDecimal(1,totalTSI);
                    PS.setObject(2,objx.getStReference2());
                    PS.setObject(3,policy.getStPolicyTypeGroupID());
                    
                    int k = PS.executeUpdate();

                    if (k==0)
                    {
                        S.release();

                        PS = S.setQuery("insert into ins_pol_risk_accumulation(entity_id, amount, pol_type_group_id, description) values(?,?,?,?)");
                        
                        PS.setObject(1,objx.getStReference2());
                        PS.setBigDecimal(2,totalTSI);
                        PS.setObject(3,policy.getStPolicyTypeGroupID());
                        PS.setObject(4,objx.getStReference1());
                        
                        k = PS.executeUpdate();

                        if (k==0) throw new Exception("Failed to update risk accumulation");
                    }
                }
                finally
                {
                    S.release();
                }
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
        }
        
    }
    
    
     private void postARTaxAcrual(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
         
        final DTOList details = pol.getDetails();
        
        final DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );
             
            BigDecimal amt = null;
            
            String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

            EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
   
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                
                final ARInvoiceView invoice = new ARInvoiceView();
                invoice.markNew();

                invoice.setStPolicyID(pol.getStPolicyID());
                invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
                if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));

                invoice.setDtInvoiceDate(pol.getDtPolicyDate());
                invoice.setDtDueDate(pol.getDtPolicyDate());
                invoice.setDbAmountSettled(null);
                invoice.setStCurrencyCode(pol.getStCurrencyCode());
                invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                invoice.setStPostedFlag("Y");
                invoice.setStARCustomerID(taxEntityId);
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStEntityID(taxEntityId);
                invoice.setStCostCenterCode(pol.getStCostCenterCode());
                invoice.setStARTransactionTypeID(Parameter.readString("COMISSION_AR_TRX"));
                invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

                invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                invoice.setStAttrPolicyName(pol.getStCustomerName());
                invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
                invoice.setStAttrPolicyID(pol.getStPolicyID());
                if(pol.isStatusEndorse()){
                    invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
                }

                invoice.setStRefID0("TAX/" + politem.getStPolicyItemID());
                invoice.setStRefID1("INST/" + installmentSeq);
                invoice.setStRefID2("TAX/" + pol.getStPolicyID());
                
                invoice.setStNoSuratHutang(
                                "SHP/POL/"+ 
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtPolicyDate())+
                                "/"+
                                DateUtil.getYear(pol.getDtPolicyDate()));

                //invoice.setStPostedFlag("Y");

                final DTOList ivdetails = new DTOList();

                invoice.setDetails(ivdetails);
            
                if (politem.isFee())
                    if (installmentSeq > 0) continue;
                
                boolean posting = true;
                
                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = false;
                //if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;
                
                if(!posting) continue;
                
                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if (politem.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;
                        
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                                     
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    ivdc.setStNegativeFlag("N");
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();
                    
                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);
                    
                    ivdc.setDbEnteredAmount(taxamt);
                    invoice.setDbEnteredAmount(taxamt);
                    
                    ivdc.setStTaxCode(stTaxCode);
                    
                    //ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                    String jenis = taxTransactionLineView.getStItemDesc().toUpperCase().startsWith("PPH21")?"PPH21":"PPH23";

                    invoice.setStNoSuratHutang(
                                "SHP/POL/"+
                                 jenis +
                                "/" +
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtPolicyDate())+
                                "/"+
                                DateUtil.getYear(pol.getDtPolicyDate()));
                }
                
                //}
                invoice.recalculateTaxAcrual(true);
            
                getRemoteAccountReceivable().save(invoice);
            }
            
            
        }
    }
     
    private void postARInstallment(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
         
        final DTOList details = pol.getDetails();
        
        final DTOList installment = pol.getInstallment();
        
        final BigDecimal premi = pol.getDbPremiTotal();
        
        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();
        
        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");
        
        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");
        
        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);
            
            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();
            
            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
             */
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            //invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(inst.getDtDueDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            //if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
            
            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            //invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));
            
            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }
            
            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);
            
            invoice.setStPostedFlag("Y");
            
            final DTOList ivdetails = new DTOList();
            
            invoice.setDetails(ivdetails);
            
            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());
            
            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();
            
            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );
            
            boolean premiGFound = false;
            
            {
                
                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);
                    
                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();
                        
                        final BigDecimal amt =
                                installmentPeriod == null ?
                                    premi :
                                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);
        
                        premiGFound = true;
                        
                        if(BDUtil.isZeroOrNull(amt)) continue;
                            
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                        
                        ivd.markNew();
                        
                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());
                        
                        ivdetails.add(ivd);
                        
                        
                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                                        
                        ivd.setDbEnteredAmount(amt);

                        break;
                    }
                }
            }
            
            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);
            
            BigDecimal amt = null;
            
            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);
                
                if (politem.isFee())
                    if (installmentSeq > 0) continue;
                
                boolean posting = true;
                
                if(BDUtil.isZeroOrNull(politem.getDbAmount())) posting = false;
                if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;
                
                if(!posting) continue;
                
                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();
                
                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();
                
                ivd.markNew();
                
                ivdetails.add(ivd);
                
                ivd.setStEntityID(politem.getStEntityID());
                
                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());
                
                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());
                
                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
                    else amt = politem.getDbNetAmount();
                }
                
                if (politem.isComission() || politem.isDiscount())
                    amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);
                
                ivd.setDbEnteredAmount(amt);
                
                //ivd.setStGLAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLRevenue()));
                
                /*ivd.setDbTaxRate(politem.getDbTaxRate());
             ivd.setDbTaxAmount(taxamt);
                 
             if (ivd.isComission())
                ivd.setStTaxCodeOnSettlement(politem.getStTaxCode());
             else
                ivd.setStTaxCode(politem.getStTaxCode());*/
                
                if (ivd.isComission()&&politem.getStTaxCode()!=null)
                    ivd.setStTaxCode(politem.getStTaxCode());
                
                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();
                    
                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;
                        
                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());
                    
                    ivdc.markNew();
                    
                    ivdetails.add(ivdc);
                    
                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());
                    
                    //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");
                    
                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);
                    
                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");
                    
                    ivdc.setStEntityID(taxEntityId);
                    
                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();
                    
                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();
                    
                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);
                    
                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();
                    
                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);
                    
                    ivdc.setDbEnteredAmount(taxamt);
                    
                    ivdc.setStTaxCode(stTaxCode);
                    
                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }
                
                //}
            }
            
            invoice.recalculate();
            
            getRemoteAccountReceivable().save(invoice);
        }
    }

    private void postARInstallmentAcrual(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getDetails();

        final DTOList installment = pol.getInstallment();

        final BigDecimal premi = pol.getDbPremiTotal();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
             */
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            //invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(inst.getDtDueDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            //if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            //invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);

            invoice.setStPostedFlag("Y");

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            boolean premiGFound = false;

            {

                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();

                        final BigDecimal amt =
                                installmentPeriod == null ?
                                    premi :
                                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);

                        premiGFound = true;

                        if(BDUtil.isZeroOrNull(amt)) continue;

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());

                        ivdetails.add(ivd);


                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());

                        ivd.setDbEnteredAmount(amt);

                        break;
                    }
                }
            }

            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);

            BigDecimal amt = null;

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                if (politem.isFee())
                    if (installmentSeq > 0) continue;

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbAmount())) posting = false;
                if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());

                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
                    else amt = politem.getDbNetAmount();
                }

                if (politem.isComission() || politem.isDiscount())
                    amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);

                ivd.setDbEnteredAmount(amt);

                //ivd.setStGLAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLRevenue()));

                /*ivd.setDbTaxRate(politem.getDbTaxRate());
             ivd.setDbTaxAmount(taxamt);

             if (ivd.isComission())
                ivd.setStTaxCodeOnSettlement(politem.getStTaxCode());
             else
                ivd.setStTaxCode(politem.getStTaxCode());*/

                if (ivd.isComission()&&politem.getStTaxCode()!=null)
                    ivd.setStTaxCode(politem.getStTaxCode());

                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);

                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                    ivdc.setDbEnteredAmount(taxamt);

                    ivdc.setStTaxCode(stTaxCode);

                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }

                //}
            }

            invoice.recalculate();

            getRemoteAccountReceivable().save(invoice);
        }
    }
     
    public void reverseReinsuranceOnly(InsurancePolicyView pol) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            
            PreparedStatement P = S.setQuery("update ins_policy set f_ri_finish = 'N',ri_posted_flag='N', change_date = 'now',change_who = ?, admin_notes=? where pol_id=?");
            
            P.setObject(1, UserManager.getInstance().getUser().getStUserID());
            P.setObject(2, "REVERSE R/I BY : "+ UserManager.getInstance().getUser().getStUserID());
            P.setObject(3, pol.getStPolicyID());
            int r = P.executeUpdate();
            S.release();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
        
        
    }

    /*
    public InsurancePolicyView getInsurancePolicyForPrintingWithDigitalSign(String policyid, String alter) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        InsurancePolicyView policy = getInsurancePolicy(policyid);

        boolean mayPrint = false;
        if (Tools.isYes(policy.getStEffectiveFlag())) {
            mayPrint = true;
        }
        if (policy.isStatusClaimDLA() && alter.equalsIgnoreCase("claimlks")) {
            mayPrint = true;
        }

        if (policy.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {
            if (policy.isStatusPolicy() && alter.equalsIgnoreCase("prinsip")) {
                mayPrint = true;
            }
        }

        if (!mayPrint) {
            throw new RuntimeException(" Data belum disetujui, hanya bisa di preview");
        }


        //CEK KESAMAAN KODE USER ENTRY DGN ORG YANG PRINT
        boolean bebasCetak = SessionManager.getInstance().getSession().hasResource("POL_UWRIT_PRINT_DIGITIZED_FREE");

        if (!bebasCetak) {
            if (UserManager.getInstance().getUser().getStBranch() != null) {
                if (!policy.getStCreateWho().equalsIgnoreCase(us.getStUserID())) {
                    if (!policy.getStCreateWho().equalsIgnoreCase(UserManager.getInstance().getUser().getStReferenceUserID())) {
                        throw new RuntimeException("Tidak bisa cetak Digitized Sign karena polis ini di entry oleh " + policy.getStCreateWho() + " (" + policy.getUser(policy.getStCreateWho()).getStUserName() + ")");
                    }
                }
            }
        }
        //END CEK

        if (alter.equals("STANDARD") || alter.equals("PRINTALL")) {
            if (policy.getStSignCode() != null) {
                throw new RuntimeException(" Data sudah pernah dicetak dengan Digitized Sign, konfirmasi ke Pimpinan untuk cetak ulang");
            }
        }

        policy.markUpdate();

        if (policy.getStPrintCode() == null) {
            policy.setStPrintCode(StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PCD")), '0', 10));
        }


        String k;


        String pz = policy.getStPrintCode() + "/" + policy.getStPolicyNo() + "/" + policy.getStPolicyID();

        k = Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes(pz)));

        policy.setStPrintStamp(k);

        String encryptSign = Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes(policy.getStPolicyNo() + "/" + policy.getStApprovedWho())));

        String counter = null;
        if (alter != null) {
            if (alter.equals("STANDARD") || alter.equals("lamprestitusi") || alter.equals("PRINTALL")) {
                policy.setStDocumentPrintFlag("Y");
                policy.setStSignCode(encryptSign);
                counter = policy.calculateRenewalCounter(policy.getStPrintCounter());
            }
        }


        final SQLUtil S = new SQLUtil();

        try {
            //S.store(policy);

            String update = "update ins_policy set print_code = ?,print_stamp = ?,document_print_flag=?, sign_code=? ";

            if (counter != null) {
                update = update + " , print_counter=? ";
            }

            update = update + " where pol_id=?";

            PreparedStatement P = S.setQuery(update);
            P.setObject(1, policy.getStPrintCode());
            P.setObject(2, k);
            P.setObject(3, policy.getStDocumentPrintFlag());
            P.setObject(4, policy.getStSignCode());

            if (counter != null) {
                P.setObject(5, counter);
                P.setObject(6, policyid);
            } else {
                P.setObject(5, policyid);
            }

            int r = P.executeUpdate();

        } finally {
            S.release();
        }

        policy = getInsurancePolicy(policyid);

        return policy;
        
    }*/

    private void postAPClaimDetailSeparate(InsurancePolicyView pol)throws Exception{
        final DTOList details = pol.getClaimItems();
        
        for (int k = 0; k < details.size(); k++) {
                InsurancePolicyItemsView politem2 = (InsurancePolicyItemsView) details.get(k);

                if(politem2.getInsuranceItem().isCreateArApSeparately()){
                    if(politem2.getInsuranceItem().getStItemGroup()==null)
                        continue;

                    postAPClaimDetailSeparately(pol);
                }

        }
    }

    private void postAPClaimDetailSeparately(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getClaimItems();

        boolean buatHutangTerpisah = false;
        boolean subrogasi = false;
        boolean salvage = false;
        boolean adjuster = false;
        for (int k = 0; k < details.size(); k++) {
                InsurancePolicyItemsView politem2 = (InsurancePolicyItemsView) details.get(k);

                if(politem2.getInsuranceItem().isCreateArApSeparately())
                        buatHutangTerpisah = true;

                if(politem2.getInsuranceItem().isSubrogasi())
                     subrogasi = true;

                if(politem2.getInsuranceItem().isSalvage())
                     salvage = true;

                if(politem2.getInsuranceItem().isAdjusterFee())
                     adjuster = true;

        }

        if(!buatHutangTerpisah) return;

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        final ARInvoiceView invoice = new ARInvoiceView();
        invoice.markNew();

        invoice.setStPolicyID(pol.getStPolicyID());
        invoice.setStInvoiceNo("K" + pol.getStPolicyNo());
        invoice.setDtInvoiceDate(pol.getDtDLADate());
        invoice.setDtDueDate(pol.getDtDLADate());
        invoice.setDbAmountSettled(null);
        invoice.setStCurrencyCode(pol.getStClaimCurrency());
        invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
        invoice.setStPostedFlag("N");
        invoice.setStARCustomerID(pol.getStEntityID());
        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
        invoice.setStEntityID(pol.getStEntityID());
        invoice.setStCostCenterCode(pol.getStCostCenterCode());

        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

        if(subrogasi || salvage)
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);

        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
        invoice.setStAttrPolicyName(pol.getStCustomerName());
        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
        invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
        invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
        invoice.setStAttrPolicyID(pol.getStPolicyID());
        invoice.setStRefID1(pol.getStPLANo());
        invoice.setStRefID2(pol.getStDLANo());

        if(pol.getDbClaimAdvancePaymentAmount()!=null)
            invoice.setStRefID0("PANJAR");

        if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
        }
        invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

        invoice.setStPostedFlag("Y");

        final DTOList ivdetails = new DTOList();

        if (details.size() < 1) throw new RuntimeException("Details empty, probably corrupted data"); //remark for bersih2x

        if(details.size() >= 1){
            InsurancePolicyItemsView firstItem = (InsurancePolicyItemsView) details.get(0);

            //invoice.setStARTransactionTypeID(firstItem.getInsItem().getARTrxLine().getStARTrxTypeID());

            invoice.setDetails(ivdetails);

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if(!insuranceItem.isCreateArApSeparately()) continue;

                //if(insuranceItem.getStItemGroup().equalsIgnoreCase(pol_id))

                invoice.setStARTransactionTypeID(politem.getInsItem().getARTrxLine().getStARTrxTypeID());

                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                if(politem.getStEntityID()!=null){
                    invoice.setStARCustomerID(politem.getStEntityID());
                    invoice.setStEntityID(politem.getStEntityID());
                }
                

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                BigDecimal amt = politem.getDbAmount();

                ivd.setDbEnteredAmount(amt);

                //add tax claim

                //if(!adjuster){
                       if (politem.getStTaxCode()!=null) {
                        final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                        if(politem.getInsItem().getStUseTaxFlag()!=null)
                            if(politem.getInsItem().isNotUseTax())
                                continue;

                        ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                        ivdc.markNew();

                        ivdetails.add(ivdc);

                        String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                        //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");

                        EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                        if (taxEntity == null)
                            throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                        ivdc.setStEntityID(taxEntityId);

                        final String stTaxCode = politem.getStTaxCode();
                        final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                        final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                                "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                                new Object[]{stTaxCode, stARTrxTypeID},
                                ARTransactionLineView.class
                                ).getDTO();

                        if (taxTransactionLineView == null)
                            throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                        ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                        ivdc.loadSettings();
                        //ivdc.setStDescription(politem.getStDescription());
                        BigDecimal taxamt = politem.getDbTaxAmount();

                        //if (politem.isComission() || politem.isDiscount())
                            //taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                        ivdc.setDbEnteredAmount(taxamt);

                        ivdc.setStTaxCode(stTaxCode);

                        ivdc.setRef(ivd);
                        ivdc.setStTaxFlag("Y");
                    }
                //}
                
            }

            //invoice.recalculate();

            invoice.recalculateClaimOnly(true);

            getRemoteAccountReceivable().saveInvoiceClaim(invoice);

            //if (pol.isStatusClaimEndorse()) getRemoteAccountReceivable().saveClaimEndorse(invoice);
            //else getRemoteAccountReceivable().saveInvoiceClaim(invoice);
        }
    }

    private void postAPTaxAcrual(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getClaimItems();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            BigDecimal amt = null;

            String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

            EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            glApplicator.setCode('B',pol.getStCostCenterCode());
            glApplicator.setCode('X',pol.getStPolicyTypeID()==null?null:pol.getPolicyType().getStGLCode()); //change these into a better system (use var interfaces & inner class)
            glApplicator.setDesc("X",pol.getPolicyType().getStShortDescription());
            glApplicator.setCode('Y',pol.getEntity().getStGLCode());
            glApplicator.setDesc("Y",pol.getEntity().getStShortName());
            glApplicator.setDesc("K",pol.getStDLANo());

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) continue;

                final ARInvoiceView invoice = new ARInvoiceView();
                invoice.markNew();

                invoice.setStPolicyID(pol.getStPolicyID());
                invoice.setStInvoiceNo("K" + pol.getStPolicyNo());

                invoice.setDtInvoiceDate(pol.getDtDLADate());
                invoice.setDtDueDate(pol.getDtDLADate());
                invoice.setDbAmountSettled(null);
                invoice.setStCurrencyCode(pol.getStClaimCurrency());
                invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
                invoice.setStPostedFlag("Y");
                invoice.setStARCustomerID(taxEntityId);
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStEntityID(taxEntityId);
                invoice.setStCostCenterCode(pol.getStCostCenterCode());
                invoice.setStARTransactionTypeID(Parameter.readString("COMISSION_AR_TRX"));
                invoice.setStInvoiceType(FinCodec.InvoiceType.AP);
                invoice.setStApprovedFlag("Y");

                invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                invoice.setStAttrPolicyName(pol.getStCustomerName());
                invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());
                invoice.setStAttrPolicyID(pol.getStPolicyID());
                if(pol.isStatusEndorse()){
                    invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
                }

                invoice.setStRefID0("TAX/" + politem.getStPolicyItemID());
                //invoice.setStRefID1("INST/" + installmentSeq);
                invoice.setStRefID1("TAX/" + pol.getStPolicyID());

                invoice.setStRefID2(pol.getStDLANo());
                //invoice.setStRefID2(pol.getStDLANo());

                invoice.setStNoSuratHutang(
                                "SHP/CLM/"+
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtDLADate())+
                                "/"+
                                DateUtil.getYear(pol.getDtDLADate()));

                invoice.setStGLARAccountID(glApplicator.getAccountID(politem.getInsuranceItem().getARTrxLine().getTransactionType().getStGLAPAccount()));

                invoice.setStPostedFlag("Y");

                final DTOList ivdetails = new DTOList();

                invoice.setDetails(ivdetails);

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = false;
                //if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                //if (politem.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);


                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    ivdc.setStNegativeFlag("N");
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    ivdc.setDbEnteredAmount(taxamt);
                    invoice.setDbEnteredAmount(taxamt);

                    ivdc.setStTaxCode(stTaxCode);

                    //ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                    //ivdc.setStGLAccountID(applicator.getAccountID(detail.getTrxLine().getStGLArAccountByType("AP"));
                //}

                //}
                invoice.recalculateClaimTax(true);

                getRemoteAccountReceivable().save(invoice);
            }

    }

    public void reverseCoas(InsurancePolicyView pol) throws Exception {
        final SQLUtil S = new SQLUtil();
        /*
         *update ins_policy set posted_flag = null,effective_flag='N' where status = 'POLICY' and pol_id=;
          delete from ins_policy where status = 'CLAIM' and pol_id=;

          delete from ar_invoice where attr_pol_id=;
          delete from ar_invoice_details where ar_invoice_id=;

          delete from ar_receipt where ar_ap_invoice_id =;
          delete from ar_receipt_lines where ar_invoice_id=;

          delete from gl_je_detail where trx_no = ;

          delete from gl_acct_bal;
         */
        try {

            final DTOList pembayaran = pol.getArinvoices();

            if(pembayaran.size() > 0){
                ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

                if(inv.getDbAmountSettled()!=null)
                    if(inv.getDtPaymentDate()!=null)
                        throw new RuntimeException("Polis tidak bisa di reverse, sudah dibayar premi nya");
            }

            PreparedStatement P = S.setQuery("update ins_policy set posted_flag = null,effective_flag='N',approved_who=null,approved_date=null,password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?");
            //PreparedStatement P = S.setQuery("update ins_policy set posted_flag = null,effective_flag='N',password=null,client_ip=null,ref3=null,ref4=null,f_ready_to_approve=null,admin_notes=? where pol_id=?");

            P.setObject(1, "REVERSE BY : "+ UserManager.getInstance().getUser().getStUserID());
            //P.setObject(1, "REVERSE PROSES KOAS APR : "+ UserManager.getInstance().getUser().getStUserID());
            P.setObject(2, pol.getStPolicyID());
            int r = P.executeUpdate();
            S.release();

            /*
            PreparedStatement P2 = S.setQuery("delete from ins_pol_obj_searching where pol_id=?");
            P2.setObject(1, pol.getStPolicyID());
            int r2 = P2.executeUpdate();
            S.release();*/

            //final DTOList invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolIdCoasOnly(pol.getStPolicyID());

            final DTOList invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolId(pol.getStPolicyID());

            for (int i = 0; i < invoice.size(); i++) {
                ARInvoiceView invoiceView = (ARInvoiceView) invoice.get(i);

                final DTOList details = invoiceView.getDetails();

                final PeriodDetailView per = PeriodManager.getInstance().getPeriodFromDate(invoiceView.getDtInvoiceDate());
                if (per == null)
                    throw new Exception("Setting Periode Salah ! (Periode Tanggal Pembayaran " + invoiceView.getDtInvoiceDate() + "Salah)");
                if (!per.isOpen()) throw new Exception("Period is not open");

                //update gl_acct_bal per invoice
                Long accountid = Long.valueOf(invoiceView.getStGLARAccountID());

                BigDecimal bal = getRemoteGeneralLedger().getBalance(accountid, per.getLgFiscalYear(), per.getLgPeriodNo(), invoiceView.getDbAmount());
                BigDecimal amt = invoiceView.getDbAmount();
                if (BDUtil.biggerThanZero(bal))
                    amt = BDUtil.mul(amt, new BigDecimal(-1));

                getRemoteGeneralLedger().updateBalance(accountid, per.getLgFiscalYear(), per.getLgPeriodNo(), amt);

                //cari gl_acct bal berdasarkan account id dr invoice_detail dan update saldo

                if(details.size()>0){
                    for (int j = 0; j < details.size(); j++) {
                        ARInvoiceDetailView detailView = (ARInvoiceDetailView) details.get(j);

                        //update gl_acct_bal per detail_invoice
                        Long accountid2 = Long.valueOf(detailView.getStGLAccountID());
                        BigDecimal bal2 = getRemoteGeneralLedger().getBalance(accountid2, per.getLgFiscalYear(), per.getLgPeriodNo(), detailView.getDbAmount());
                        BigDecimal amt2 = detailView.getDbAmount();
                        if (BDUtil.biggerThanZero(bal2))
                            amt2 = BDUtil.mul(amt2, new BigDecimal(-1));


                        getRemoteGeneralLedger().updateBalance(accountid2, per.getLgFiscalYear(), per.getLgPeriodNo(), amt2);
                    }

                    details.deleteAll();
                    S.storeDeleteNormal(details);
                }

                final DTOList receipt = getRemoteAccountReceivable().getReceiptByARInvoiceId(invoiceView.getStARInvoiceID());

                for (int k = 0; k < receipt.size(); k++) {
                    ARReceiptView receiptView = (ARReceiptView) receipt.get(k);

                    final DTOList receiptLines = receiptView.getDetails();
                    receiptLines.deleteAll();

                    S.storeDeleteNormal(receiptLines);

                    PreparedStatement P5 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                    P5.setObject(1, receiptView.getStARReceiptID());
                    int t = P5.executeUpdate();
                    S.release();
                }
                receipt.deleteAll();
                S.storeDeleteNormal(receipt);

                for (int k = 0; k < receipt.size(); k++) {
                    ARReceiptView receiptView = (ARReceiptView) receipt.get(k);

                    PreparedStatement P5 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                    P5.setObject(1, receiptView.getStARReceiptID());
                    int t = P5.executeUpdate();
                    S.release();
                }



                PreparedStatement P3 = S.setQuery("delete from gl_je_detail where ref_trx_no= ?");
                P3.setObject(1, invoiceView.getStARInvoiceID());
                int t = P3.executeUpdate();
                S.release();

                //details.deleteAll();
            }

            invoice.deleteAll();
            S.storeDeleteNormal(invoice);

            final DTOList produk = ListUtil.getDTOListFromQuery(
                    "select * from aba_produk where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceProdukView.class);

            produk.deleteAll();
            S.storeDeleteNormal(produk);

            final DTOList bayar = ListUtil.getDTOListFromQuery(
                    "select * from aba_bayar1 where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceBayar1View.class);

            bayar.deleteAll();
            S.storeDeleteNormal(bayar);

            final DTOList hutang = ListUtil.getDTOListFromQuery(
                    "select * from aba_hutang where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsuranceHutangView.class);

            hutang.deleteAll();
            S.storeDeleteNormal(hutang);

            final DTOList pajak = ListUtil.getDTOListFromQuery(
                    "select * from aba_pajak where pol_id=?",
                    new Object[]{pol.getStPolicyID()},
                    InsurancePajakView.class);

            pajak.deleteAll();
            S.storeDeleteNormal(pajak);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        } finally {
            S.release();
        }


    }

    public void jurnalUlang(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {

            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();

            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));

            //boolean isFreeInstallment = pol.getInstallment().size() > 1 && manualInstallment;

            if(approvalMode){
               //if (postflagchanged) {

                    // POST AR

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                            postARInstallmentAcrualBases(pol);

                            postCoas(pol);

                            //postReasCumullation(pol);

                        }

                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                            //postReasClaim(pol);

                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);

                        }

                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }

                  
                //}
            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        //postReasCumullation(pol);

            if (Tools.isYes(pol.getStRIPostedFlag())){
                //postReasCumullation(pol);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void reverseJurnalBalik(InsurancePolicyView pol) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        boolean approvalMode = true;

        try {

            //CEK SUDAH BAYAR PREMI BELUM
            final DTOList pembayaran = pol.getArinvoices();

            if(pembayaran.size() > 0){
                ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

                if(inv.getDbAmountSettled()!=null)
                    if(inv.getDtPaymentDate()!=null)
                        throw new RuntimeException("Polis tidak bisa di reverse, sudah dibayar premi nya");
            }


           S.store(pol);

           DTOList invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolId(pol.getStPolicyID());

            /*POSTING*/
            if(invoice.size()>0){
                if(approvalMode){
                   //if (postflagchanged) {
                    
                    final SQLUtil S2 = new SQLUtil();
                    //UPDATE STATUS POST INVOICE LAMA
                    for (int i = 0; i < invoice.size(); i++) {
                        ARInvoiceView inv = (ARInvoiceView) invoice.get(i);
                        
                        

                        PreparedStatement PS = S2.setQuery("update ar_invoice set posted_flag = 'N', cancel_flag = 'Y' where ar_invoice_id = ?");

                        PS.setObject(1,inv.getStARInvoiceID());
                        
                        int j = PS.executeUpdate();

                        PS.close();
                        S2.release();
                    }
                    
                    //END



                            if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                                //postARInstallmentJurnalBalik(pol);

                                postARInstallmentAcrualBasesJurnalBalik(pol);

                                postCoasJurnalBalik(pol);

                                //postReasCumullationJurnalBalik(pol);

                                //simpen buat sistem lama
                                

                                final DTOList produk = ListUtil.getDTOListFromQuery(
                                        "select * from aba_produk where pol_id=?",
                                        new Object[]{pol.getStPolicyID()},
                                        InsuranceProdukView.class);

                                produk.deleteAll();
                                S.storeDeleteNormal(produk);

                                final DTOList bayar = ListUtil.getDTOListFromQuery(
                                        "select * from aba_bayar1 where pol_id=?",
                                        new Object[]{pol.getStPolicyID()},
                                        InsuranceBayar1View.class);

                                bayar.deleteAll();
                                S.storeDeleteNormal(bayar);

                                final DTOList hutang = ListUtil.getDTOListFromQuery(
                                        "select * from aba_hutang where pol_id=?",
                                        new Object[]{pol.getStPolicyID()},
                                        InsuranceHutangView.class);

                                hutang.deleteAll();
                                S.storeDeleteNormal(hutang);

                                final DTOList pajak = ListUtil.getDTOListFromQuery(
                                        "select * from aba_pajak where pol_id=?",
                                        new Object[]{pol.getStPolicyID()},
                                        InsurancePajakView.class);

                                pajak.deleteAll();
                                S.storeDeleteNormal(pajak);

                            }

                            if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                                //postReasClaimJurnalBalik(pol);

                                postAPClaimJurnalBalik(pol);

                                postAPClaimDetailSeparateJurnalBalik(pol);

                                postARCoasJurnalBalik(pol);

                                postAPTaxAcrualJurnalBalik(pol);

                                if(pol.getStClaimObjectParentID()!=null)
                                        updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                            }

                            if(pol.isStatusEndorseRI()){
                                //postReasCumullationJurnalBalik(pol);
                            }

                }

                if(pol.isStatusInward())
                    if(approvalMode)
                        if (Tools.isYes(pol.getStPostedFlag()))
                            //postReasCumullationJurnalBalik(pol);

                if (Tools.isYes(pol.getStRIPostedFlag())){
                    //postReasCumullationJurnalBalik(pol);
                }
            }

           DTOList invoiceNew = getRemoteAccountReceivable().getARInvoiceByAttrPolId(pol.getStPolicyID());

           final SQLUtil S2 = new SQLUtil();
            //UPDATE STATUS POST INVOICE BARU
           for (int i = 0; i < invoiceNew.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceNew.get(i);

                PreparedStatement PS = S2.setQuery("update ar_invoice set posted_flag = 'N', cancel_flag = 'Y' where ar_invoice_id = ?");

                PS.setObject(1,inv.getStARInvoiceID());

                int j = PS.executeUpdate();

                PS.close();
                S2.release();
            }
 

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void reverseReinsuranceJurnalBalik(InsurancePolicyView pol) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        boolean approvalMode = true;

        try {

            //CEK SUDAH BAYAR PREMI BELUM
            /*
            final DTOList pembayaran = pol.getArinvoices();

            if(pembayaran.size() > 0){
                ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

                if(inv.getDbAmountSettled()!=null)
                    if(inv.getDtPaymentDate()!=null)
                        throw new RuntimeException("Polis tidak bisa di reverse, sudah dibayar premi nya");
            }
            */


           S.store(pol);

                /*POSTING*/
           DTOList invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolId(pol.getStPolicyID());

            if(approvalMode){
               //if (postflagchanged) {

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {


                            //postReasCumullationJurnalBalik(pol);


                        }


                        if(pol.isStatusEndorseRI()){
                            //postReasCumullationJurnalBalik(pol);
                        }


            }

            //if(pol.isStatusInward())
                //if(approvalMode)
                    //if (Tools.isYes(pol.getStPostedFlag()))
                        //postReasCumullationJurnalBalik(pol);


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    private void postARInstallmentJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getDetails();

        final DTOList installment = pol.getInstallment();

        final BigDecimal premi = pol.getDbPremiTotal();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
             */
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            //if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            //invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);

            invoice.setStPostedFlag("Y");

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            boolean premiGFound = false;

            {

                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();

                        final BigDecimal amt =
                                installmentPeriod == null ?
                                    premi :
                                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);

                        premiGFound = true;

                        if(BDUtil.isZeroOrNull(amt)) continue;

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());

                        ivdetails.add(ivd);


                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());

                        ivd.setDbEnteredAmount(BDUtil.negate(amt));

                        break;
                    }
                }
            }

            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);

            BigDecimal amt = null;

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                if (politem.isFee())
                    if (installmentSeq > 0) continue;

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbAmount())) posting = false;
                if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());

                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
                    else amt = politem.getDbNetAmount();
                }

                if (politem.isComission() || politem.isDiscount())
                    amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);

                ivd.setDbEnteredAmount(BDUtil.negate(amt));

                //ivd.setStGLAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLRevenue()));

                /*ivd.setDbTaxRate(politem.getDbTaxRate());
             ivd.setDbTaxAmount(taxamt);

             if (ivd.isComission())
                ivd.setStTaxCodeOnSettlement(politem.getStTaxCode());
             else
                ivd.setStTaxCode(politem.getStTaxCode());*/

                if (ivd.isComission()&&politem.getStTaxCode()!=null)
                    ivd.setStTaxCode(politem.getStTaxCode());

                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);

                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }

                //}
            }

            invoice.recalculate();

            getRemoteAccountReceivable().save(invoice);
        }
    }

    private void postCoasJurnalBalik(InsurancePolicyView pol) throws Exception {

        //if (!pol.isLeader()) return;

        //if (pol.isMember()) return;

        if(pol.isDirect()) return;

        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            //boolean notHasPremi = BDUtil.isZero(coin.getDbPremiAmountNet());

            //if (!notHasPremi) continue;

            if (coin.isHoldingCompany()) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("CO/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/PREMI.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("H" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());*/
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStAPTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));

            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            if(!coin.isHoldingCompany()){
                if(!BDUtil.isZeroOrNull(coin.getDbAmount())) invoice.setDbAttrPolicyTSITotal(coin.getDbAmount());
                if(coin.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER")){
                    //invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize(coin.getStEntityID())));
                    invoice.setDbAttrPolicyTSITotal(BDUtil.roundUp(BDUtil.mul(pol.getDbCoinsTSI(coin.getStEntityID()), BDUtil.getRateFromPct(pol.getHoldingCoin().getDbSharePct()))));
                }
            }

            //GET NO REKAP OBJEK
            if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    final DTOList rekap = ListUtil.getDTOListFromQuery(
                    "SELECT REF8,REKAP_KREASI "+
                     "   FROM INS_POL_OBJ "+
                     "      WHERE POL_ID = ? "+
                     "      GROUP BY REF8, REKAP_KREASI "+
                     "      ORDER BY REF8::BIGINT, LENGTH(REKAP_KREASI)",
                    new Object[]{pol.getStPolicyID()},
                     HashDTO.class
                    );

                    for (int j = 0; j < rekap.size(); j++) {
                        HashDTO dto = (HashDTO) rekap.get(j);

                        String entity = dto.getFieldValueByFieldNameST("ref8");
                        String rekapNo = dto.getFieldValueByFieldNameST("rekap_kreasi");

                        if(coin.getStEntityID().equalsIgnoreCase(entity)){
                              invoice.setStNoSuratHutang(rekapNo);
                        }

                    }
            }else{
                invoice.setStNoSuratHutang(invoice.generateNoSuratHutangCoas(coin.getStEntityID(),pol));
            }

            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    BigDecimal premiNetto = null;
                    premiNetto = BDUtil.sub(coin.getDbPremiAmount(), BDUtil.add(BDUtil.add(BDUtil.add(coin.getDbCommissionAmount(), coin.getDbDiscountAmount()), coin.getDbBrokerageAmount()), coin.getDbHandlingFeeAmount()));

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbPremiAmount()));
                        //ivd.setDbEnteredAmount(premiNetto);

                        break;
                    }
                }
            }

                    //CEK KOAS ADA DISKON ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("DISCCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbDiscountAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbDiscountAmount()));

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA KOMISI ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("COMCO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbCommissionAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbCommissionAmount()));
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    //CEK KOAS ADA BROKERAGE ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("BROKCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbBrokerageAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbBrokerageAmount()));
                                ivd.setStEntityID(coin.getStEntityID());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA HFEE ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("HFEECO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbHandlingFeeAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbHandlingFeeAmount()));
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    invoice.recalculate();

                    getRemoteAccountReceivable().saveInvoiceCoas(invoice);

                    /*
                    final SQLUtil S2 = new SQLUtil();

                    PreparedStatement PS = S2.setQuery("update ins_pol_obj set rekap_kreasi= ? where ref8 = ? and pol_id = ?");

                    //update ref3 dan ref4 ref3 utk JS ref4 selain JS
                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());

                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());

                            int j = PS.executeUpdate();
                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());

                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());

                            int j = PS.executeUpdate();
                        }
                    }

                    PS.close();
                    S2.release();
                    */


                    //end

                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());

                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());

                        }
                    }
        }

        
            final SQLUtil S = new SQLUtil();

            try {

                S.store(pol);

            } catch (Exception e) {
                ctx.setRollbackOnly();
                throw e;
            } finally {
                S.release();
            }
            

    }

    private void postReasCumullationJurnalBalik(InsurancePolicyView pol) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList reins = pol.getReins();

        for (int i = 0; i < reins.size(); i++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) reins.get(i);

            if(ri.getTreatyDetail().isOR()) continue;

            if (BDUtil.isZeroOrNull(ri.getDbPremiAmount())) continue;

            if (ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode() == null) continue;

            if(ri.getTreatyDetail().getTreatyType().getStJournalFlag()==null) continue;

            if (ri.getTreatyDetail().getStARTrxLineID() == null) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("REINS");
            //invoice.setStInvoiceNo("E/PREMI.RI-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("I" + ri.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(ri.getStMemberEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());

            invoice.setStARTransactionTypeID(ri.getTreatyDetail().getARTrxLine().getStARTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());

            invoice.setStReferenceD0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode());
            invoice.setStReferenceD1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceE0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode2());
            invoice.setStReferenceE1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceZ0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode3());
            invoice.setStReferenceZ1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceC0(ri.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode6());
            invoice.setStReferenceC1(ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

            invoice.setStReferenceA1(ri.getStRISlipNo());

            //bikin surat hutang
            //invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
            //"218/BPDAN/2/2011"
            if(ri.getDtValidReinsuranceDate()==null){
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()+
                        "/"+
                        ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                        "/"+
                        DateUtil.getQuartalRomawi(pol.getDtPolicyDate())+
                        "/"+
                        DateUtil.getYear(pol.getDtPolicyDate()));
            }

            if(ri.getDtValidReinsuranceDate()!=null){
                invoice.setStNoSuratHutang(
                        ri.getStMemberEntityID()+ "."+
                        String.valueOf(i+1) +
                        "/"+
                        ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                        "/"+
                        DateUtil.getQuartalRomawi(pol.getDtPolicyDate())+
                        "/"+
                        DateUtil.getYear(pol.getDtPolicyDate()));
            }


            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int v = 0; v < artlines.size(); v++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);

                    if ("PREMI".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(ri.getDbPremiAmount()));

                        //if(ri.getDbPremiAmountEdited()!=null){
                            //ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbPremiAmount(), ri.getDbPremiAmountEdited()));
                        //}
                    } else if ("KOMISI".equalsIgnoreCase(artl.getStItemClass())) {

                        if (BDUtil.isZeroOrNull(ri.getDbRICommAmount())) continue;

                        if (invoice.getStReferenceZ0() == null)
                            throw new RuntimeException("Comission Account Code not found for " + ri.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(ri.getDbRICommAmount()));

                        //if(ri.getDbRICommAmountEdited()!=null){
                            //ivd.setDbEnteredAmount(BDUtil.sub(ri.getDbRICommAmount(),ri.getDbRICommAmountEdited()));
                        //}
                    } //else throw new RuntimeException("Unknown Item class : " + artl);
                }
            }

                    invoice.recalculate();

                    getRemoteAccountReceivable().saveInvoiceReins(invoice);
        }


    }

    private void postReasClaimJurnalBalik(InsurancePolicyView pol) throws Exception {

        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = new DTOList();

        details.add(pol.getClaimObject());

        for (int w = 0; w < details.size(); w++) {
            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) details.get(w);

            final DTOList treaties = obj.getTreaties();

            for (int i = 0; i < treaties.size(); i++) {
                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);

                final DTOList treDetails = tre.getDetails();

                for (int j = 0; j < treDetails.size(); j++) {
                    InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(j);

                    if (trd.getTreatyDetail().isOR()) continue;

                    if(trd.getTreatyDetail().getTreatyType().getStJournalFlag()==null) continue;

                    if(trd.getTreatyDetail().getStARTrxLineIDClaim()==null) continue;

                    final DTOList shares = trd.getShares();

                    for (int k = 0; k < shares.size(); k++) {
                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                        if (!BDUtil.biggerThanZero(ri.getDbClaimAmount())) continue;

                        final ARInvoiceView invoice = new ARInvoiceView();
                        invoice.markNew();

                        invoice.setStRefID0("REINS/" + ri.getStInsurancePolicyReinsID());
                        //invoice.setStInvoiceNo("E/CLAIM.RI-" + pol.getStPolicyNo());
                        invoice.setStInvoiceNo("N" + trd.getTreatyDetail().getTreatyType().getStTransactionNoHeader() + pol.getStPolicyNo());
                        //invoice.setDbAmount(pol.getDbAmount());
                        //invoice.setDtInvoiceDate(us.getDtTransactionDate());
                        //invoice.setDtDueDate(us.getDtTransactionDate());
                        invoice.setDtInvoiceDate(pol.getDtDLADate());
                        invoice.setDtDueDate(pol.getDtDLADate());
                        invoice.setDbAmountSettled(null);
                        invoice.setStCurrencyCode(pol.getStCurrencyCode());
                        invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                        invoice.setStPostedFlag("Y");
                        invoice.setStARCustomerID(ri.getStMemberEntityID());
                        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                        invoice.setStEntityID(invoice.getStARCustomerID());
                        invoice.setStCostCenterCode(pol.getStCostCenterCode());
                        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
                        invoice.setStARTransactionTypeID(trd.getTreatyDetail().getARTrxLineClaim().getStARTrxTypeID());
                        invoice.setStInvoiceType(FinCodec.InvoiceType.AR);

                        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                        invoice.setStAttrPolicyName(pol.getStCustomerName());
                        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                        invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
                        invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
                        invoice.setStAttrPolicyID(pol.getStPolicyID());
                        invoice.setStPolicyID(pol.getStPolicyID());

                        invoice.setStReferenceD0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode5());
                        invoice.setStReferenceD1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

                        invoice.setStReferenceZ0(trd.getTreatyDetail().getTreatyType().getStTreatyTypeGLCode4());
                        invoice.setStReferenceZ1(trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName());

                        invoice.setStRefID1(pol.getStPLANo());
                        invoice.setStRefID2(pol.getStDLANo());

                        //bikin surat hutang
                        if(ri.getDtValidReinsuranceDate()==null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }

                        if(ri.getDtValidReinsuranceDate()!=null){
                            invoice.setStNoSuratHutang(
                                    ri.getStMemberEntityID()+ "."+
                                    String.valueOf(k+1) +
                                    "/"+
                                    trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID()+
                                    "/"+
                                    pol.getStPolicyTypeID()+
                                    "/"+
                                    DateUtil.getMonth2Digit(new Date())+
                                    "/"+
                                    DateUtil.getYear(new Date()));
                        }


                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());

                        /*invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(ri.getStMemberEntityID(), trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeID(), pol.getStPolicyTypeID()));
                        invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
                        invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());*/
                        //finish

                        final DTOList ivdetails = new DTOList();

                        invoice.setDetails(ivdetails);

                        //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

                        final DTOList artlines = ListUtil.getDTOListFromQuery(
                                "select * from ar_trx_line where ar_trx_type_id = ?",
                                new Object[]{invoice.getStARTransactionTypeID()},
                                ARTransactionLineView.class
                                );

                        {

                            for (int v = 0; v < artlines.size(); v++) {
                                ARTransactionLineView artl = (ARTransactionLineView) artlines.get(v);

                                if ("KLAIM".equalsIgnoreCase(artl.getStItemClass())) {
                                    final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                    ivd.markNew();

                                    ivdetails.add(ivd);

                                    ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                    ivd.loadSettings();
                                    //ivd.setStDescription(politem.getStDescription());
                                    ivd.setDbEnteredAmount(BDUtil.negate(ri.getDbClaimAmount()));
                                } else throw new RuntimeException("Unknown Item class : " + artl);
                            }
                        }

                                invoice.recalculate(true);

                                getRemoteAccountReceivable().save(invoice);

                    }
                }
            }
        }
    }

    private void postAPClaimJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getClaimItems();

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        final ARInvoiceView invoice = new ARInvoiceView();
        invoice.markNew();

        invoice.setStPolicyID(pol.getStPolicyID());
        //invoice.setStInvoiceNo("E/CLAIM-" + pol.getStPolicyNo());
        invoice.setStInvoiceNo("K" + pol.getStPolicyNo());
        //invoice.setDbAmount(pol.getDbAmount());
        //invoice.setDtInvoiceDate(us.getDtTransactionDate());
        //invoice.setDtDueDate(us.getDtTransactionDate());
        invoice.setDtInvoiceDate(pol.getDtDLADate());
        invoice.setDtDueDate(pol.getDtDLADate());
        invoice.setDbAmountSettled(null);
        invoice.setStCurrencyCode(pol.getStClaimCurrency());
        invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
        invoice.setStPostedFlag("N");
        invoice.setStARCustomerID(pol.getStEntityID());
        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
        invoice.setStEntityID(pol.getStEntityID());
        invoice.setStCostCenterCode(pol.getStCostCenterCode());
        //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
        //invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());

        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
        invoice.setStAttrPolicyName(pol.getStCustomerName());
        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
        invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
        invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
        invoice.setStAttrPolicyID(pol.getStPolicyID());
        invoice.setStRefID1(pol.getStPLANo());
        invoice.setStRefID2(pol.getStDLANo());

        if(pol.getDbClaimAdvancePaymentAmount()!=null)
            invoice.setStRefID0("PANJAR");

        if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
        }
        invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

        invoice.setStPostedFlag("Y");

        final DTOList ivdetails = new DTOList();

        if (details.size() < 1) throw new RuntimeException("Details empty, probably corrupted data"); //remark for bersih2x

        if(details.size() >= 1){
            InsurancePolicyItemsView firstItem = (InsurancePolicyItemsView) details.get(0);

            invoice.setStARTransactionTypeID(firstItem.getInsItem().getARTrxLine().getStARTrxTypeID());

            invoice.setDetails(ivdetails);

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if(insuranceItem.isCreateArApSeparately()) continue;

                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                BigDecimal amt = politem.getDbAmount();

//                if (politem.getStTaxCode() == null) {
//                    amt = politem.getDbAmount();
//                } else {
//                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
//                    else amt = politem.getDbNetAmount();
//                }

                ivd.setDbEnteredAmount(BDUtil.negate(amt));

                //add tax claim

                if (politem.getStTaxCode()!=null) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);

                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                }
            }

            invoice.recalculateClaimOnly(true);

            if (pol.isStatusClaimEndorse()) getRemoteAccountReceivable().saveClaimEndorse(invoice);
            else getRemoteAccountReceivable().saveInvoiceClaim(invoice);
        }
    }


    private void postAPClaimDetailSeparateJurnalBalik(InsurancePolicyView pol)throws Exception{
        final DTOList details = pol.getClaimItems();

        for (int k = 0; k < details.size(); k++) {
                InsurancePolicyItemsView politem2 = (InsurancePolicyItemsView) details.get(k);

                if(politem2.getInsuranceItem().isCreateArApSeparately()){
                    if(politem2.getInsuranceItem().getStItemGroup()==null)
                        continue;
                    
                        postAPClaimDetailSeparatelyJurnalBalik(pol);
                }

        }
    }

    private void postAPClaimDetailSeparatelyJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getClaimItems();

        boolean buatHutangTerpisah = false;
        for (int k = 0; k < details.size(); k++) {
                InsurancePolicyItemsView politem2 = (InsurancePolicyItemsView) details.get(k);

                if(politem2.getInsuranceItem().isCreateArApSeparately())
                        buatHutangTerpisah = true;

        }

        if(!buatHutangTerpisah) return;

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        final ARInvoiceView invoice = new ARInvoiceView();
        invoice.markNew();

        invoice.setStPolicyID(pol.getStPolicyID());
        invoice.setStInvoiceNo("K" + pol.getStPolicyNo());
        invoice.setDtInvoiceDate(pol.getDtDLADate());
        invoice.setDtDueDate(pol.getDtDLADate());
        invoice.setDbAmountSettled(null);
        invoice.setStCurrencyCode(pol.getStClaimCurrency());
        invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
        invoice.setStPostedFlag("N");
        invoice.setStARCustomerID(pol.getStEntityID());
        invoice.setDtMutationDate(invoice.getDtInvoiceDate());
        invoice.setStEntityID(pol.getStEntityID());
        invoice.setStCostCenterCode(pol.getStCostCenterCode());

        invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

        invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
        invoice.setStAttrPolicyNo(pol.getStPolicyNo());
        invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
        invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
        invoice.setStAttrPolicyName(pol.getStCustomerName());
        invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
        invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
        invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
        invoice.setStAttrPolicyID(pol.getStPolicyID());
        invoice.setStRefID1(pol.getStPLANo());
        invoice.setStRefID2(pol.getStDLANo());

        if(pol.getDbClaimAdvancePaymentAmount()!=null)
            invoice.setStRefID0("PANJAR");

        if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
        }
        invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

        invoice.setStPostedFlag("Y");

        final DTOList ivdetails = new DTOList();

        if (details.size() < 1) throw new RuntimeException("Details empty, probably corrupted data"); //remark for bersih2x

        if(details.size() >= 1){
            InsurancePolicyItemsView firstItem = (InsurancePolicyItemsView) details.get(0);

            invoice.setStARTransactionTypeID(firstItem.getInsItem().getARTrxLine().getStARTrxTypeID());

            invoice.setDetails(ivdetails);

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if(!insuranceItem.isCreateArApSeparately()) continue;

                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                BigDecimal amt = politem.getDbNetAmount();

                ivd.setDbEnteredAmount(BDUtil.negate(amt));

                //add tax claim

                if (politem.getStTaxCode()!=null) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);

                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    //String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY");

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    //if (politem.isComission() || politem.isDiscount())
                        //taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }
            }

            invoice.recalculateClaimOnly(true);

            if (pol.isStatusClaimEndorse()) getRemoteAccountReceivable().saveClaimEndorse(invoice);
            else getRemoteAccountReceivable().saveInvoiceClaim(invoice);
        }
    }

    private void postARCoasJurnalBalik(InsurancePolicyView pol) throws Exception {

        if (!pol.isLeader()) return;

        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            boolean hasClaimAmount = !BDUtil.isZeroOrNull(coin.getDbClaimAmount());

            if (!hasClaimAmount) continue;

            if (coin.isHoldingCompany()) continue;

            if (coin.isORJiwa()) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("COCL/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/CLAIM.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("L" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            //invoice.setDtInvoiceDate(us.getDtTransactionDate());
            //invoice.setDtDueDate(us.getDtTransactionDate());

            invoice.setDtInvoiceDate(pol.getDtDLADate());
            invoice.setDtDueDate(pol.getDtDLADate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStClaimCurrency());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRateClaim());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStClaimTrxTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            invoice.setStPolicyID(pol.getStPolicyID());
            invoice.setStRefID1(pol.getStPLANo());
            invoice.setStRefID2(pol.getStDLANo());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

            //bikin surat hutang
            // invoice.setStNoSuratHutang(invoice.generateNoSuratHutang(coin.getStEntityID(),"CO",pol.getStPolicyTypeID()));
            // invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            // invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("CLAIMN".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(BDUtil.negate(coin.getDbClaimAmount()));

                        break;
                    }
                }
            }

            invoice.recalculate(true);

            getRemoteAccountReceivable().saveInvoiceClaimCoas(invoice);
        }
    }

    private void postAPTaxAcrualJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getClaimItems();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            BigDecimal amt = null;

            String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

            EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

            final GLUtil.Applicator glApplicator = new GLUtil.Applicator();

            glApplicator.setCode('B',pol.getStCostCenterCode());
            glApplicator.setCode('X',pol.getStPolicyTypeID()==null?null:pol.getPolicyType().getStGLCode()); //change these into a better system (use var interfaces & inner class)
            glApplicator.setCode('Y',pol.getEntity().getStGLCode());
            glApplicator.setDesc("Y",pol.getEntity().getStShortName());
            glApplicator.setDesc("K",pol.getStDLANo());

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) continue;

                final ARInvoiceView invoice = new ARInvoiceView();
                invoice.markNew();

                invoice.setStPolicyID(pol.getStPolicyID());
                invoice.setStInvoiceNo("K" + pol.getStPolicyNo());

                invoice.setDtInvoiceDate(pol.getDtDLADate());
                invoice.setDtDueDate(pol.getDtDLADate());
                invoice.setDbAmountSettled(null);
                invoice.setStCurrencyCode(pol.getStCurrencyCode());
                invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                invoice.setStPostedFlag("Y");
                invoice.setStARCustomerID(taxEntityId);
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStEntityID(taxEntityId);
                invoice.setStCostCenterCode(pol.getStCostCenterCode());
                invoice.setStARTransactionTypeID(Parameter.readString("COMISSION_AR_TRX"));
                invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

                invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                invoice.setStAttrPolicyName(pol.getStCustomerName());
                invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
                invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
                invoice.setStAttrPolicyID(pol.getStPolicyID());
                if(pol.isStatusEndorse()){
                    invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
                }

                invoice.setStRefID0("TAX/" + politem.getStPolicyItemID());
                //invoice.setStRefID1("INST/" + installmentSeq);
                invoice.setStRefID2("TAX/" + pol.getStPolicyID());

                invoice.setStNoSuratHutang(
                                "SHP/"+
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtDLADate())+
                                "/"+
                                DateUtil.getYear(pol.getDtDLADate()));


                invoice.setStGLARAccountID(glApplicator.getAccountID(politem.getInsuranceItem().getARTrxLine().getTransactionType().getStGLAPAccount()));

                invoice.setStPostedFlag("Y");

                final DTOList ivdetails = new DTOList();

                invoice.setDetails(ivdetails);

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = false;
                //if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                //if (politem.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);


                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    ivdc.setStNegativeFlag("N");
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));
                    invoice.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    //ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                    //ivdc.setStGLAccountID(applicator.getAccountID(detail.getTrxLine().getStGLArAccountByType("AP"));
                //}

                //}
                invoice.recalculateClaimTax(true);

                getRemoteAccountReceivable().save(invoice);
            }

    }

    public void saveReinsuranceAfterJurnalBalik(InsurancePolicyView pol) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        boolean approvalMode = true;

        try {

            //CEK SUDAH BAYAR PREMI BELUM
            /*
            final DTOList pembayaran = pol.getArinvoices();

            if(pembayaran.size() > 0){
                ARInvoiceView inv = (ARInvoiceView) pembayaran.get(0);

                if(inv.getDbAmountSettled()!=null)
                    if(inv.getDtPaymentDate()!=null)
                        throw new RuntimeException("Polis tidak bisa di reverse, sudah dibayar premi nya");
            }
            */


           S.store(pol);

                /*POSTING*/

            if(approvalMode){
               //if (postflagchanged) {

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {


                            //postReasCumullation(pol);


                        }


                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }


            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        postReasCumullation(pol);


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }


    public void saveReinsuranceOnly(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList objects = pol.getObjects();

                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                    //if(obj.getStReference80()==null) continue;

                    //if(obj.getStReference80().equalsIgnoreCase("")) continue;

                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                    obj.setStPolicyID(pol.getStPolicyID());

                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        final String claimObject = pol.getStClaimObjectParentID()!=null?pol.getStClaimObjectParentID():obj.getStPolicyObjectID();

                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));

                        if (isClaimObject){
                            pol.setStClaimObjectParentID(claimObject);
                            pol.setStClaimObjectID(obj.getStPolicyObjectID());
                        }

                    }

                    //S.store(obj);


                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        {
                            // save treaties

                            final DTOList treaties = obj.getTreaties();

                            treaties.combineDeleted();

                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);

                                if (tre.isDelete()) tre.getDetails().deleteAll();

                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));

                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                                final DTOList tredetails = tre.getDetails();

                                tredetails.combineDeleted();

                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }

                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));

                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());

                                    final DTOList shares = tredet.getShares();

                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));

                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());

                                    }
                                    S.store(shares);
                                }
                                S.store(tredetails);
                            }

                            S.store(treaties);
                        }
                    }
                }

            //S.store(pol);
            

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateNoLPHKark(DTOList data, ProductionReinsuranceReportForm form) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            for (int i = 0; i < data.size(); i++) {
                InsurancePolicyView polis = (InsurancePolicyView) data.get(i);

//                InsurancePolicyView pol = getInsurancePolicy(polis.getStPolicyID());

                //pol.markUpdate();
                //pol.setStReference1(form.getStNoUrut());

                //S.store(pol);
            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;

        } finally {
            S.release();
        }
    }

    private void postCoasAutoEndorse(InsurancePolicyView pol) throws Exception {

        //if (!pol.isLeader()) return;

        //if (pol.isMember()) return;

        if(pol.isDirect()) return;

        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            //boolean notHasPremi = BDUtil.isZero(coin.getDbPremiAmountNet());

            //if (!notHasPremi) continue;

            if (coin.isHoldingCompany()) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("CO/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/PREMI.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("H" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());*/
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStAPTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());

            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            if(!coin.isHoldingCompany()){
                if(!BDUtil.isZeroOrNull(coin.getDbAmount())) invoice.setDbAttrPolicyTSITotal(coin.getDbAmount());
                if(coin.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER")){
                    //invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize(coin.getStEntityID())));
                    invoice.setDbAttrPolicyTSITotal(BDUtil.roundUp(BDUtil.mul(pol.getDbCoinsTSI(coin.getStEntityID()), BDUtil.getRateFromPct(pol.getHoldingCoin().getDbSharePct()))));
                }
            }

            //GET NO REKAP OBJEK
            if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    final DTOList rekap = ListUtil.getDTOListFromQuery(
                    "SELECT REF8,REKAP_KREASI "+
                     "   FROM INS_POL_OBJ "+
                     "      WHERE POL_ID = ? "+
                     "      GROUP BY REF8, REKAP_KREASI "+
                     "      ORDER BY REF8::BIGINT, LENGTH(REKAP_KREASI)",
                    new Object[]{pol.getStPolicyID()},
                     HashDTO.class
                    );

                    for (int j = 0; j < rekap.size(); j++) {
                        HashDTO dto = (HashDTO) rekap.get(j);

                        String entity = dto.getFieldValueByFieldNameST("ref8");
                        String rekapNo = dto.getFieldValueByFieldNameST("rekap_kreasi");

                        if(coin.getStEntityID().equalsIgnoreCase(entity)){
                              invoice.setStNoSuratHutang(rekapNo);
                        }

                    }
            }else{
                invoice.setStNoSuratHutang(invoice.generateNoSuratHutangCoas(coin.getStEntityID(),pol));
            }

            //

            //bikin surat hutang
            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    BigDecimal premiNetto = null;
                    premiNetto = BDUtil.sub(coin.getDbPremiAmount(), BDUtil.add(BDUtil.add(BDUtil.add(coin.getDbCommissionAmount(), coin.getDbDiscountAmount()), coin.getDbBrokerageAmount()), coin.getDbHandlingFeeAmount()));

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbPremiAmount());
                        //ivd.setDbEnteredAmount(premiNetto);

                        break;
                    }
                }
            }

                    //CEK KOAS ADA DISKON ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("DISCCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbDiscountAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbDiscountAmount());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA KOMISI ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("COMCO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbCommissionAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbCommissionAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    //CEK KOAS ADA BROKERAGE ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("BROKCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbBrokerageAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbBrokerageAmount());
                                ivd.setStEntityID(coin.getStEntityID());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA HFEE ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("HFEECO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbHandlingFeeAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbHandlingFeeAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    invoice.recalculate();

                    getRemoteAccountReceivable().saveInvoiceCoas(invoice);

                    /*
                    final SQLUtil S2 = new SQLUtil();

                    PreparedStatement PS = S2.setQuery("update ins_pol_obj set rekap_kreasi = ? where ref8 = ? and pol_id = ?");

                    //update ref3 dan ref4 ref3 utk JS ref4 selain JS

                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());

                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());

                            int j = PS.executeUpdate();
                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());

                            PS.setObject(1,invoice.getStNoSuratHutang());
                            PS.setObject(2,coin.getStEntityID());
                            PS.setObject(3,pol.getStPolicyID());

                            int j = PS.executeUpdate();
                        }
                    }

                    PS.close();
                    S2.release();
                    */

                    //end

                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.markUpdate();
                            pol.setStReference3(invoice.getStNoSuratHutang());

                        }else{
                            pol.markUpdate();
                            pol.setStReference4(invoice.getStNoSuratHutang());

                        }
                    }
        }
            /*
           final SQLUtil S = new SQLUtil();

            try {
                S.store(pol);
            } catch (Exception e) {
                ctx.setRollbackOnly();
                throw e;
            } finally {
                S.release();
            }
            */



    }

    public void saveEndorseCumul(uploadEndorsemenView endorse) throws Exception {
        SQLUtil S = new SQLUtil();

        try {
            if (endorse.isNew()) {
                endorse.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSUPLOADENDORSE")));
            }

            S.store(endorse);

//            if(Tools.isYes(closing.getStFinanceClosingStatus())){
//                final DTOList reinsData = closing.getReinsAll();
//
//                postReasCumullation(reinsData, closing);
//            }

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }



    public void updateValidateClaim(DTOList data) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            String stARInvoiceID = "";
            for (int i = 0; i < data.size(); i++) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) data.get(i);

                stARInvoiceID = rcl.getStInvoiceID();

                if (rcl.getPolicy() != null) {
                    InsurancePolicyView pol = rcl.getPolicy();

                    pol.setStValidateClaimFlag("Y");
                    pol.markUpdate();
                    S.store(pol);
                }

            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;

        } finally {
            S.release();
        }
    }


    public void saveUploadEndorsemen(UploadHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveUploadEndorsemen: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSUPLOADID")));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadEndorsemenView j = (uploadEndorsemenView) l.get(i);

                if(header.getStInsuranceUploadID()!=null)
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());

                if(header.getDtPolicyDate()!=null)
                    j.setDtPolicyDate(header.getDtPolicyDate());

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSUPLOADDTLID")));
                }

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public DTOList getInsuranceUploadDetail(String stUploadID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_upload_detail where ins_upload_id = ?",
                new Object[]{stUploadID},
                uploadEndorsemenView.class);
    }

    public void saveApproveUpload(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {
                if (stNextStatus != null) {

                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());

                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(stNextStatus))
                    {
                        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)){
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();

                            oldPolis.setStAdminNotes("RENEWAL");

                            S.store(oldPolis);
                        }
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());

                            oldPolis.markUpdate();

                            oldPolis.setStActiveFlag("N");

                            S.store(oldPolis);
                        }
                    }

                    //pol.setStParentID(pol.getStPolicyID());
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());

                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    //pol.setStApprovedWho(null);
                    //pol.setDtApprovedDate(null);


                    //pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();

                    pol.getClaimItems().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList policyDocuments = pol.getPolicyDocuments();

                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList konversiDocuments = pol.getKonversiDocuments();

                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();

                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;

                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        /*
                        if(pol.isStatusPolicy()){
                            obj.setStPolicyObjectPolicyRootID(obj.getStPolicyObjectID());
                        }*/

                        //obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();

                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                            obj.getTreaties().convertAllToNew();

                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();

                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);

                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();

                            }
                        }
                        //end
                    }
                }

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList details = pol.getDetails();

                final HashMap insuranceItemsMap = getInsuranceItemsMap();

                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

                    ip.setStPolicyID(pol.getStPolicyID());

                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));

                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }

                S.store(details);

                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();

                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.store(claimItems);

                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.store(claimDocuments);
                }


                final DTOList clausules = (DTOList) pol.getClausules().clone();
                if (clausules != null) {

                    for (int i = clausules.size() - 1; i >= 0; i--) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

                        if (!icl.isSelected()) {
                            clausules.delete(i);
                            continue;
                        }

                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                    }
                    S.store(clausules);
                }

                /*
                final DTOList clausules = pol.getClausules();

                for (int j = 0; j < clausules.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(j);
                    if (icl.isNew())
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                    icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                }

                S.store(clausules);
                */

                final DTOList policyDocuments = pol.getPolicyDocuments();

                for (int i = 0; i < policyDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);

                    doc.setStPolicyID(pol.getStPolicyID());

                    final boolean marked = doc.isMarked();

                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }

                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }

                S.store(policyDocuments);

                final DTOList konversiDocuments = pol.getKonversiDocuments();

                for (int i = 0; i < konversiDocuments.size(); i++) {
                    InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);

                    doc.setStPolicyID(pol.getStPolicyID());

                    final boolean marked = doc.isMarked();

                    if (marked) {
                        if (doc.getStInsurancePolicyDocumentID() != null)
                            doc.markUpdate();
                        else {
                            doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                            doc.markNew();
                        }
                    }

                    if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                }

                S.store(konversiDocuments);

                final DTOList objects = pol.getObjects();

                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;

                    obj.setStPolicyID(pol.getStPolicyID());

                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        final String claimObject = pol.getStClaimObjectParentID()!=null?pol.getStClaimObjectParentID():obj.getStPolicyObjectID();

                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));

                        if (isClaimObject){
                            pol.setStClaimObjectParentID(claimObject);
                            pol.setStClaimObjectID(obj.getStPolicyObjectID());
                        }

                    }

                    S.store(obj);

                    /* tes klausula objek
                    final DTOList oclaus = obj.getClausules();
                    for (int j = 0; j < oclaus.size(); j++) {
                        InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) oclaus.get(j);
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                        icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(obj.getStPolicyObjectID());
                    }

                    S.store(oclaus);*/

                    final DTOList suminsureds = obj.getSuminsureds();

                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));

                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());
                    }

                    S.store(suminsureds);

                    final DTOList coverage = obj.getCoverage();

                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());
                    }

                    S.store(coverage);

                    final DTOList deductibles = obj.getDeductibles();

                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());
                    }

                    S.store(deductibles);

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        {
                            // save treaties

                            final DTOList treaties = obj.getTreaties();

                            treaties.combineDeleted();

                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);

                                if (tre.isDelete()) tre.getDetails().deleteAll();

                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));

                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                                final DTOList tredetails = tre.getDetails();

                                tredetails.combineDeleted();

                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }

                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));

                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());

                                    final DTOList shares = tredet.getShares();

                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));

                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());

                                    }
                                    S.store(shares);
                                }
                                S.store(tredetails);
                            }

                            S.store(treaties);
                        }
                    }
                }

                final DTOList deletedObjects = objects.getDeleted();

                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);

                    S.store(obj);
                    //S.store(obj.getClausules());
                    S.store(obj.getSuminsureds());
                    S.store(obj.getCoverage());

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        final DTOList treaties = obj.getTreaties();

                        treaties.deleteAll();
                        treaties.combineDeleted();

                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);

                            final DTOList treDetails = tre.getDetails();

                            treDetails.deleteAll();
                            treDetails.combineDeleted();

                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);

                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();

                                S.store(treShares);
                            }

                            S.store(treDetails);

                        }

                        S.store(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();

                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());
                }

                S.store2(coins);

                final DTOList coinscoverage = pol.getCoinsCoverage();

                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());
                }

                S.store2(coinscoverage);

                final DTOList installment = pol.getInstallment();

                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);

                    inst.setStPolicyID(pol.getStPolicyID());

                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));
                }

                S.store2(installment);

            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());

            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus())||FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(pol.getStNextStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();

            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){
                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();

            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();


            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus)){
                if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generatePLANo();
                else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generateDLANo();
            }

            if(approvalMode){
                if (Tools.isYes(pol.getStPostedFlag())) {
                    if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {
                         postCoasGenerateNoRekap(pol);
                    }
                }
            }

            S.store(pol);

            /*POSTING*/

            //final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();

            //final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());

            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();

            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));

            boolean manualInstallment = false;
            if(pol.getStManualInstallmentFlag()!=null)
                if(pol.getStManualInstallmentFlag().equalsIgnoreCase("Y"))
                    manualInstallment = true;

            //boolean isFreeInstallment = pol.getInstallment().size() > 1 && manualInstallment;

            if(approvalMode){
               //if (postflagchanged) {

                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                            postARInstallmentAcrualBases(pol);

                            postCoasForEndorseUpload(pol);

                            //postReasCumullation(pol);

                            //simpen buat sistem lama
                            saveABAProduk(pol);

                            saveABABayar(pol);

                            saveABAHutang(pol);

                            saveABAPajak(pol);

                            //saveObjectToOtherTable(pol);

                        }

                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                            postReasClaim(pol);

                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);
                            //saveObjectToOtherTable(pol);

                            if(pol.getStClaimObjectParentID()!=null)
                                    updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                        }

                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }

                    } else {
                        // unpost
                    }
                //}
            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        //postReasCumullation(pol);

            if (Tools.isYes(pol.getStRIPostedFlag())){
                //postReasCumullation(pol);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateCancelValidateClaim(DTOList data) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            String stARInvoiceID = "";
            for (int i = 0; i < data.size(); i++) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) data.get(i);

                stARInvoiceID = rcl.getStInvoiceID();

                if (rcl.getPolicy() != null) {
                    InsurancePolicyView pol = rcl.getPolicy();

                    pol.setStValidateClaimFlag(null);
                    pol.markUpdate();
                    S.store(pol);
                }

            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;

        } finally {
            S.release();
        }
    }

    private void postCoasGenerateNoRekap(InsurancePolicyView pol) throws Exception {

        if(pol.isDirect()) return;

        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            //boolean notHasPremi = BDUtil.isZero(coin.getDbPremiAmountNet());

            //if (!notHasPremi) continue;

            if (coin.isHoldingCompany()) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("CO/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/PREMI.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("H" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());*/
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStAPTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());

            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            if(!coin.isHoldingCompany()){
                if(!BDUtil.isZeroOrNull(coin.getDbAmount())) invoice.setDbAttrPolicyTSITotal(coin.getDbAmount());
                if(coin.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER")){
                    //invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize(coin.getStEntityID())));
                    invoice.setDbAttrPolicyTSITotal(BDUtil.roundUp(BDUtil.mul(pol.getDbCoinsTSI(coin.getStEntityID()), BDUtil.getRateFromPct(pol.getHoldingCoin().getDbSharePct()))));
                }
            }

            //GET NO REKAP OBJEK
            if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    final DTOList rekap = ListUtil.getDTOListFromQuery(
                    "SELECT REF8,REKAP_KREASI "+
                     "   FROM INS_POL_OBJ "+
                     "      WHERE POL_ID = ? "+
                     "      GROUP BY REF8, REKAP_KREASI "+
                     "      ORDER BY REF8::BIGINT, LENGTH(REKAP_KREASI)",
                    new Object[]{pol.getStPolicyID()},
                     HashDTO.class
                    );

                    for (int j = 0; j < rekap.size(); j++) {
                        HashDTO dto = (HashDTO) rekap.get(j);

                        String entity = dto.getFieldValueByFieldNameST("ref8");
                        String rekapNo = dto.getFieldValueByFieldNameST("rekap_kreasi");

                        if(coin.getStEntityID().equalsIgnoreCase(entity)){
                              invoice.setStNoSuratHutang(rekapNo);
                        }

                    }
            }else{
                invoice.setStNoSuratHutang(invoice.generateNoSuratHutangCoas(coin.getStEntityID(),pol));
            }

            //

            //bikin surat hutang
            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    BigDecimal premiNetto = null;
                    premiNetto = BDUtil.sub(coin.getDbPremiAmount(), BDUtil.add(BDUtil.add(BDUtil.add(coin.getDbCommissionAmount(), coin.getDbDiscountAmount()), coin.getDbBrokerageAmount()), coin.getDbHandlingFeeAmount()));

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbPremiAmount());
                        //ivd.setDbEnteredAmount(premiNetto);

                        break;
                    }
                }
            }

                    //CEK KOAS ADA DISKON ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("DISCCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbDiscountAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbDiscountAmount());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA KOMISI ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("COMCO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbCommissionAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbCommissionAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    //CEK KOAS ADA BROKERAGE ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("BROKCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbBrokerageAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbBrokerageAmount());
                                ivd.setStEntityID(coin.getStEntityID());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA HFEE ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("HFEECO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbHandlingFeeAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbHandlingFeeAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    invoice.recalculate();

                    //getRemoteAccountReceivable().saveInvoiceCoas(invoice);

                    if(FinCodec.CoinsuranceType.COINSCOVER.equalsIgnoreCase(coin.getStCoinsuranceType())){
                        if(coin.getStEntityID().equalsIgnoreCase("96")){
                            pol.setStReference3(invoice.getStNoSuratHutang());

                        }else{
                            pol.setStReference4(invoice.getStNoSuratHutang());

                        }
                    }
        }


    }

    private void postCoasForEndorseUpload(InsurancePolicyView pol) throws Exception {

        //if (!pol.isLeader()) return;

        //if (pol.isMember()) return;

        if(pol.isDirect()) return;

        final DTOList coins = pol.getCoins3();
        UserSession us = SessionManager.getInstance().getSession();
        for (int i = 0; i < coins.size(); i++) {
            InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

            //boolean notHasPremi = BDUtil.isZero(coin.getDbPremiAmountNet());

            //if (!notHasPremi) continue;

            if (coin.isHoldingCompany()) continue;

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStRefID0("CO/" + coin.getStInsurancePolicyCoinsID());
            //invoice.setStInvoiceNo("E/PREMI.CO-" + pol.getStPolicyNo());
            invoice.setStInvoiceNo("H" + pol.getStPolicyNo());
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());*/
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("Y");
            invoice.setStARCustomerID(coin.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(invoice.getStARCustomerID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStAPTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(pol.getDbInsuredAmount());
            invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmount());

            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }

            if(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())!=null) {
                invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize2(invoice.getStARCustomerID())));
            } else {
                invoice.setStReferenceX0("0");
            }

            if(!coin.isHoldingCompany()){
                if(!BDUtil.isZeroOrNull(coin.getDbAmount())) invoice.setDbAttrPolicyTSITotal(coin.getDbAmount());
                if(coin.getStCoinsuranceType().equalsIgnoreCase("COINS_COVER")){
                    //invoice.setStReferenceX0(String.valueOf(pol.getDbCoinsDetailsSize(coin.getStEntityID())));
                    invoice.setDbAttrPolicyTSITotal(BDUtil.roundUp(BDUtil.mul(pol.getDbCoinsTSI(coin.getStEntityID()), BDUtil.getRateFromPct(pol.getHoldingCoin().getDbSharePct()))));
                }
            }

            //GET NO REKAP OBJEK
            if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){
                    final DTOList rekap = ListUtil.getDTOListFromQuery(
                    "SELECT REF8,REKAP_KREASI "+
                     "   FROM INS_POL_OBJ "+
                     "      WHERE POL_ID = ? "+
                     "      GROUP BY REF8, REKAP_KREASI "+
                     "      ORDER BY REF8::BIGINT, LENGTH(REKAP_KREASI)",
                    new Object[]{pol.getStPolicyID()},
                     HashDTO.class
                    );

                    for (int j = 0; j < rekap.size(); j++) {
                        HashDTO dto = (HashDTO) rekap.get(j);

                        String entity = dto.getFieldValueByFieldNameST("ref8");
                        String rekapNo = dto.getFieldValueByFieldNameST("rekap_kreasi");

                        if(coin.getStEntityID().equalsIgnoreCase(entity)){
                              invoice.setStNoSuratHutang(rekapNo);
                        }

                    }
            }else{
                invoice.setStNoSuratHutang(invoice.generateNoSuratHutangCoas(coin.getStEntityID(),pol));
            }

            //

            //bikin surat hutang
            invoice.setDtSuratHutangPeriodFrom(pol.getDtPolicyDate());
            invoice.setDtSuratHutangPeriodTo(pol.getDtPolicyDate());
            //finish

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{invoice.getStARTransactionTypeID()},
                    ARTransactionLineView.class
                    );

            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    BigDecimal premiNetto = null;
                    premiNetto = BDUtil.sub(coin.getDbPremiAmount(), BDUtil.add(BDUtil.add(BDUtil.add(coin.getDbCommissionAmount(), coin.getDbDiscountAmount()), coin.getDbBrokerageAmount()), coin.getDbHandlingFeeAmount()));

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbPremiAmount());
                        //ivd.setDbEnteredAmount(premiNetto);

                        break;
                    }
                }
            }

                    //CEK KOAS ADA DISKON ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("DISCCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbDiscountAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbDiscountAmount());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA KOMISI ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("COMCO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbCommissionAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbCommissionAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    //CEK KOAS ADA BROKERAGE ATAU TIDAK
                    {

                        for (int j = 0; j < artlines.size(); j++) {
                            ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                            if ("BROKCO".equalsIgnoreCase(artl.getStItemClass())) {
                                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                                if (BDUtil.isZeroOrNull(coin.getDbBrokerageAmount())) break;

                                ivd.markNew();

                                ivdetails.add(ivd);

                                ivd.setStARTrxLineID(artl.getStARTrxLineID());
                                ivd.loadSettings();
                                //ivd.setStDescription(politem.getStDescription());
                                ivd.setDbEnteredAmount(coin.getDbBrokerageAmount());
                                ivd.setStEntityID(coin.getStEntityID());

                                break;
                            }
                        }
                    }
            //FINISH

            //CEK KOAS ADA HFEE ATAU TIDAK
            {

                for (int j = 0; j < artlines.size(); j++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(j);

                    if ("HFEECO".equalsIgnoreCase(artl.getStItemClass())) {
                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        if (BDUtil.isZeroOrNull(coin.getDbHandlingFeeAmount())) break;

                        ivd.markNew();

                        ivdetails.add(ivd);

                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());
                        ivd.setDbEnteredAmount(coin.getDbHandlingFeeAmount());
                        ivd.setStEntityID(coin.getStEntityID());

                        break;
                    }
                }
            }
                    //FINISH

                    invoice.recalculate();

                    getRemoteAccountReceivable().saveInvoiceCoas(invoice);

        }

    }

    public void updateCancelClaimRecap(DTOList data, ReceiptForm form) throws Exception {

        final SQLUtil S = new SQLUtil();

        try{

            for (int i = 0; i < data.size(); i++ ) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) data.get(i);

                ARInvoiceView ari = rcl.getInvoice();

                ari.markUpdate();
                ari.setStNoSuratHutang(null);
                ari.setStClaimNo(null);
                ari.setStClaimName(null);
                ari.setStClaimCoinsID(null);
                ari.setStClaimCoinsName(null);
                ari.setStClaimCoinsAddress(null);

                S.store(ari);
            }


        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;

        } finally{
            S.release();
        }
    }

    public void saveDataTeks(DataTeksMasukView teks) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            if (teks.isNew()){
                teks.setStDataID(String.valueOf(IDFactory.createNumericID("DATATEXT")));
                //teks.setStGroupID(groupID);
            }

            S.store(teks);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }


    public void saveFromTeks(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {
                if (stNextStatus != null) {

                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());

                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(stNextStatus))
                    {
                        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)){
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();

                            oldPolis.setStAdminNotes("RENEWAL");

                            S.storeFromJobs(oldPolis);
                        }
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());

                            oldPolis.markUpdate();

                            oldPolis.setStActiveFlag("N");

                            S.storeFromJobs(oldPolis);
                        }
                    }

                    //pol.setStParentID(pol.getStPolicyID());
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());

                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    pol.setStApprovedWho(null);
                    pol.setDtApprovedDate(null);


                    //pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();

                    pol.getClaimItems().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList policyDocuments = pol.getPolicyDocuments();

                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList konversiDocuments = pol.getKonversiDocuments();

                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();

                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;

                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        /*
                        if(pol.isStatusPolicy()){
                            obj.setStPolicyObjectPolicyRootID(obj.getStPolicyObjectID());
                        }*/

                        //obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();

                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                            obj.getTreaties().convertAllToNew();

                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();

                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);

                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();

                            }
                        }
                        //end
                    }
                }

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList details = pol.getDetails();

                final HashMap insuranceItemsMap = getInsuranceItemsMap();

                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

                    ip.setStPolicyID(pol.getStPolicyID());

                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));

                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }

                S.storeFromJobs(details);

                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();

                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.storeFromJobs(claimItems);

                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.storeFromJobs(claimDocuments);
                }

                /*
                final DTOList clausules = pol.getClausules();

                for (int j = 0; j < clausules.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(j);
                    if (icl.isNew())
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                    icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                }

                S.store(clausules);
                */

                

                final DTOList objects = pol.getObjects();

                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;

                    obj.setStPolicyID(pol.getStPolicyID());

                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        final String claimObject = pol.getStClaimObjectParentID()!=null?pol.getStClaimObjectParentID():obj.getStPolicyObjectID();

                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));

                        if (isClaimObject){
                            pol.setStClaimObjectParentID(claimObject);
                            pol.setStClaimObjectID(obj.getStPolicyObjectID());
                        }

                    }

                    S.storeFromJobs(obj);

                    final DTOList suminsureds = obj.getSuminsureds();

                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));

                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());

                        itsi.setStCreateWho("ADMIN");
                        itsi.setDtCreateDate(new Date());

                    }

                    S.storeFromJobs(suminsureds);

                    final DTOList coverage = obj.getCoverage();

                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());

                        cov.setStCreateWho("ADMIN");
                        cov.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(coverage);

                    final DTOList deductibles = obj.getDeductibles();

                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());

                        ded.setStCreateWho("ADMIN");
                        ded.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(deductibles);

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        {
                            // save treaties

                            final DTOList treaties = obj.getTreaties();

                            treaties.combineDeleted();

                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);

                                if (tre.isDelete()) tre.getDetails().deleteAll();

                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));

                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                                final DTOList tredetails = tre.getDetails();

                                tredetails.combineDeleted();

                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }

                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));

                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());

                                    final DTOList shares = tredet.getShares();

                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));

                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());

                                    }
                                    S.storeFromJobs(shares);
                                }
                                S.storeFromJobs(tredetails);
                            }

                            S.storeFromJobs(treaties);
                        }
                    }
                }

                final DTOList deletedObjects = objects.getDeleted();

                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);

                    S.storeFromJobs(obj);
                    //S.store(obj.getClausules());
                    S.storeFromJobs(obj.getSuminsureds());
                    S.storeFromJobs(obj.getCoverage());

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        final DTOList treaties = obj.getTreaties();

                        treaties.deleteAll();
                        treaties.combineDeleted();

                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);

                            final DTOList treDetails = tre.getDetails();

                            treDetails.deleteAll();
                            treDetails.combineDeleted();

                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);

                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();

                                S.storeFromJobs(treShares);
                            }

                            S.storeFromJobs(treDetails);

                        }

                        S.storeFromJobs(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();

                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());
                }

                S.storeFromJobs(coins);

                final DTOList coinscoverage = pol.getCoinsCoverage();

                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());

                }

                S.storeFromJobs(coinscoverage);

                final DTOList installment = pol.getInstallment();

                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);

                    inst.setStPolicyID(pol.getStPolicyID());

                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));

                }

                S.storeFromJobs(installment);

            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());

            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus())||FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(pol.getStNextStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();

            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){
                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();

            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();


            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus)){
                if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generatePLANo();
                else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generateDLANo();
            }

            S.storeFromJobs(pol);

            //saveDataStatusToGateway(pol);

            /*POSTING*/
            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();

            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));

            boolean manualInstallment = false;
            if(pol.getStManualInstallmentFlag()!=null)
                if(pol.getStManualInstallmentFlag().equalsIgnoreCase("Y"))
                    manualInstallment = true;

            if(approvalMode){

               //if (postflagchanged) {

                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                            postARInstallmentAcrualBases(pol);

                            postCoas(pol);

                            if(pol.isBankMantap() || pol.isBankNagari())
                                saveDataToGateway(pol);

                        }

                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                            //postReasClaim(pol);

                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);
                            //saveObjectToOtherTable(pol);

                            if(pol.getStClaimObjectParentID()!=null)
                                    updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                        }

                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }

                    } else {
                        // unpost
                    }
                //}
            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        postReasCumullation(pol);

            if (Tools.isYes(pol.getStRIPostedFlag())){
                //postReasCumullation(pol);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }


    public void saveDataToGateway(InsurancePolicyView pol) throws Exception {

        if(pol.isDataGateway()){

                final SQLUtil S2 = new SQLUtil();

                boolean dataKeagenan = false;

                if(pol.getStDataSourceID()!=null)
                    if(pol.getStDataSourceID().equalsIgnoreCase("3"))
                        dataKeagenan = true;

                try {
                        pol.markNew();

                        pol.getCoins2().convertAllToNew();
                        pol.getInstallment().convertAllToNew();
                        pol.getDetails().convertAllToNew();
                        pol.getClausulesNew().convertAllToNew();
                        pol.getCoinsCoverage().convertAllToNew();
                        pol.getClaimItems().convertAllToNew();

                        final DTOList objects = pol.getObjects();
                        objects.convertAllToNew();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            obj.getCoverage().convertAllToNew();
                            obj.getSuminsureds().convertAllToNew();
                            obj.getDeductibles().convertAllToNew();

                            final DTOList coverage = obj.getCoverage();
                            final DTOList tsi = obj.getSuminsureds();
                            final DTOList deduct = obj.getDeductibles();

                            if(dataKeagenan){
                                S2.storeToDataSource(coverage, "KEAGENAN");
                                S2.storeToDataSource(tsi, "KEAGENAN");
                                S2.storeToDataSource(deduct, "KEAGENAN");
                            }else{
                                S2.storeToGateway(coverage);
                                S2.storeToGateway(tsi);
                                S2.storeToGateway(deduct);
                            }

                        }

                        final DTOList coins = pol.getCoins2();
                        final DTOList coinsCoverage = pol.getCoinsCoverage();
                        final DTOList installment = pol.getInstallment();
                        final DTOList details = pol.getDetails();
                        //final DTOList clausules = pol.getClausulesNew();
                        final DTOList claimItem = pol.getClaimItems();

                        if(dataKeagenan){
                            S2.storeToDataSource(pol, "KEAGENAN");
                            S2.storeToDataSource(coins, "KEAGENAN");
                            S2.storeToDataSource(coinsCoverage, "KEAGENAN");
                            S2.storeToDataSource(installment, "KEAGENAN");
                            S2.storeToDataSource(details, "KEAGENAN");
                            S2.storeToDataSource(claimItem, "KEAGENAN");
                            S2.storeToDataSource(objects, "KEAGENAN");
                        }else{
                            S2.storeToGateway(pol);
                            S2.storeToGateway(coins);
                            S2.storeToGateway(coinsCoverage);
                            S2.storeToGateway(installment);
                            S2.storeToGateway(details);
                            S2.storeToGateway(claimItem);
                            S2.storeToGateway(objects);
                        }
                        

                }catch (Exception e) {

                    ctx.setRollbackOnly();

                    throw e;

                } finally {
                    S2.release();
                }
        }
        
    }

    public void saveDataStatusToGateway(InsurancePolicyView pol) throws Exception {


        if(pol.isDataGateway()){

            if(true) return;

                final SQLUtil S = new SQLUtil("GATEWAY");

                try {
                        pol.markNew();

                        pol.getCoins2().convertAllToNew();
                        pol.getInstallment().convertAllToNew();
                        pol.getDetails().convertAllToNew();
                        pol.getClausulesNew().convertAllToNew();
                        pol.getCoinsCoverage().convertAllToNew();
                        pol.getClaimItems().convertAllToNew();

                        final DTOList objects = pol.getObjects();
                        objects.convertAllToNew();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            obj.getCoverage().convertAllToNew();
                            obj.getSuminsureds().convertAllToNew();
                            obj.getDeductibles().convertAllToNew();

                            final DTOList coverage = obj.getCoverage();
                            final DTOList tsi = obj.getSuminsureds();
                            final DTOList deduct = obj.getDeductibles();

                            S.storeFromJobs(coverage);
                            S.storeFromJobs(tsi);
                            S.storeFromJobs(deduct);

                        }

                        final DTOList coins = pol.getCoins2();
                        final DTOList coinsCoverage = pol.getCoinsCoverage();
                        final DTOList installment = pol.getInstallment();
                        final DTOList details = pol.getDetails();
                        final DTOList clausules = pol.getClausulesNew();
                        final DTOList claimItem = pol.getClaimItems();

                        S.storeFromJobs(pol);
                        S.storeFromJobs(coins);
                        S.storeFromJobs(coinsCoverage);
                        S.storeFromJobs(installment);
                        S.storeFromJobs(details);
//                        S.storeToGateway(clausules);
                        S.storeFromJobs(claimItem);
                        S.storeFromJobs(objects);

                }catch (Exception e) {

                    ctx.setRollbackOnly();

                    throw e;

                } finally {
                    S.release();
                }
        }

    }

    public void saveLKSToGateway(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil("GATEWAY");

        UserSession us = S.getUserSession();

        try {
                if (stNextStatus != null) {

                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());

                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(stNextStatus))
                    {
                        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)){
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();

                            oldPolis.setStAdminNotes("RENEWAL");

                            S.storeFromJobs(oldPolis);
                        }
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());

                            oldPolis.markUpdate();

                            oldPolis.setStActiveFlag("N");

                            S.storeFromJobs(oldPolis);
                        }
                    }

                    //pol.setStParentID(pol.getStPolicyID());
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());

                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    pol.setStApprovedWho(null);
                    pol.setDtApprovedDate(null);


                    //pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();

                    pol.getClaimItems().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList policyDocuments = pol.getPolicyDocuments();

                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList konversiDocuments = pol.getKonversiDocuments();

                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();

                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;

                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        /*
                        if(pol.isStatusPolicy()){
                            obj.setStPolicyObjectPolicyRootID(obj.getStPolicyObjectID());
                        }*/

                        //obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();

                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                            obj.getTreaties().convertAllToNew();

                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();

                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);

                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();

                            }
                        }
                        //end
                    }
                }

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList details = pol.getDetails();

                final HashMap insuranceItemsMap = getInsuranceItemsMap();

                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

                    ip.setStPolicyID(pol.getStPolicyID());

                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));

                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }

                S.storeFromJobs(details);

                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();

                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.storeFromJobs(claimItems);

                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.storeFromJobs(claimDocuments);
                }

                /*
                final DTOList clausules = pol.getClausules();

                for (int j = 0; j < clausules.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(j);
                    if (icl.isNew())
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                    icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                }

                S.store(clausules);
                */



                final DTOList objects = pol.getObjects();

                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;

                    obj.setStPolicyID(pol.getStPolicyID());

                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        final String claimObject = pol.getStClaimObjectParentID()!=null?pol.getStClaimObjectParentID():obj.getStPolicyObjectID();

                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));

                        if (isClaimObject){
                            pol.setStClaimObjectParentID(claimObject);
                            pol.setStClaimObjectID(obj.getStPolicyObjectID());
                        }

                    }

                    S.storeFromJobs(obj);

                    final DTOList suminsureds = obj.getSuminsureds();

                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));

                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());

                        itsi.setStCreateWho("ADMIN");
                        itsi.setDtCreateDate(new Date());

                    }

                    S.storeFromJobs(suminsureds);

                    final DTOList coverage = obj.getCoverage();

                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());

                        cov.setStCreateWho("ADMIN");
                        cov.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(coverage);

                    final DTOList deductibles = obj.getDeductibles();

                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());

                        ded.setStCreateWho("ADMIN");
                        ded.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(deductibles);

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        {
                            // save treaties

                            final DTOList treaties = obj.getTreaties();

                            treaties.combineDeleted();

                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);

                                if (tre.isDelete()) tre.getDetails().deleteAll();

                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));

                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                                final DTOList tredetails = tre.getDetails();

                                tredetails.combineDeleted();

                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }

                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));

                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());

                                    final DTOList shares = tredet.getShares();

                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));

                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());

                                    }
                                    S.storeFromJobs(shares);
                                }
                                S.storeFromJobs(tredetails);
                            }

                            S.storeFromJobs(treaties);
                        }
                    }
                }

                final DTOList deletedObjects = objects.getDeleted();

                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);

                    S.storeFromJobs(obj);
                    //S.store(obj.getClausules());
                    S.storeFromJobs(obj.getSuminsureds());
                    S.storeFromJobs(obj.getCoverage());

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        final DTOList treaties = obj.getTreaties();

                        treaties.deleteAll();
                        treaties.combineDeleted();

                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);

                            final DTOList treDetails = tre.getDetails();

                            treDetails.deleteAll();
                            treDetails.combineDeleted();

                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);

                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();

                                S.storeFromJobs(treShares);
                            }

                            S.storeFromJobs(treDetails);

                        }

                        S.storeFromJobs(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();

                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());
                }

                S.storeFromJobs(coins);

                final DTOList coinscoverage = pol.getCoinsCoverage();

                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());

                }

                S.storeFromJobs(coinscoverage);

                final DTOList installment = pol.getInstallment();

                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);

                    inst.setStPolicyID(pol.getStPolicyID());

                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));

                }

                S.storeFromJobs(installment);

            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());

            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus())||FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(pol.getStNextStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();

            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){
                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();

            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();


            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus)){
                if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generatePLANo();
                else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generateDLANo();
            }

            S.storeFromJobs(pol);

            /*POSTING*/

            //final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();

            //final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());

            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();

            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));

            boolean manualInstallment = false;
            if(pol.getStManualInstallmentFlag()!=null)
                if(pol.getStManualInstallmentFlag().equalsIgnoreCase("Y"))
                    manualInstallment = true;

            //boolean isFreeInstallment = pol.getInstallment().size() > 1 && manualInstallment;

            if(approvalMode){
               if (postflagchanged) {

                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                            postARInstallment(pol);

                            postCoas(pol);

                            //postReasCumullation(pol);

                            //simpen buat sistem lama
                            saveABAProduk(pol);

                            saveABABayar(pol);

                            saveABAHutang(pol);

                            saveABAPajak(pol);

                        }

                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                            //postReasClaim(pol);

                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);
                            //saveObjectToOtherTable(pol);

                            if(pol.getStClaimObjectParentID()!=null)
                                    updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                        }

                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }

                    } else {
                        // unpost
                    }
                }
            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        postReasCumullation(pol);

            if (Tools.isYes(pol.getStRIPostedFlag())){
                //postReasCumullation(pol);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void updateStatusDataTeks(InsurancePolicyView policy) throws Exception {

        if(!policy.getStPolicyTypeID().equalsIgnoreCase("59")) return;

        if(policy.isDataGateway()){

                final SQLUtil S2 = new SQLUtil("GATEWAY");

                try{

                    String status ="NOT DEFINED YET";

                    boolean updateObject = false;
                    boolean updatePolicy = false;

                    if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.DRAFT)){
                        status = "PENAWARAN";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.SPPA)){
                        status = "SPPA";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.POLICY)){
                        status = "POLIS";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE)){
                        status = "ENDORSE";
                        updatePolicy = true;
                    }else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)){
                        if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA)) status = FinCodec.ClaimStatus.PLA;
                        else if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.DLA)) status = FinCodec.ClaimStatus.DLA;
                    
                        updatePolicy = true;
                    }

                    if(updateObject){

                        String inValues = "(";
                        final DTOList object = policy.getObjects();

                        for (int i = 0; i < object.size(); i++) {
                            InsurancePolicyObjectView obj = (InsurancePolicyObjectView) object.get(i);

                            InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                            if(i>0) inValues = inValues + ",";

                            inValues = inValues + objx.getStDataID();
                        }

                        inValues = inValues + ")";

                        String updateObjek = "update data_teks_masuk set tgl_proses = 'now', status = ? ";

                        if(policy.getStPolicyNo()!=null)
                            updateObjek = updateObjek + ", pol_no = ? ";

                        updateObjek = updateObjek + " where data_id in " + inValues;

                        PreparedStatement PS = S2.setQuery(updateObjek);

                        PS.setObject(1, status);

                        if(policy.getStPolicyNo()!=null)
                            PS.setObject(2, policy.getStPolicyNo());

                        int s = PS.executeUpdate();

                    }

                }catch (Exception e) {

                    ctx.setRollbackOnly();

                    throw e;

                } finally{
                    //logger.logWarning("###### closing koneksi polno "+policy.getStPolicyNo());
                    S2.release();
                }
        }

    }


    public void updateStatusPengajuan(InsurancePolicyView policy) throws Exception {

        if(policy.isDataGateway()){

                String dataSourceName = "GATEWAY";//DEFAULT DATA SISTEM INTERKONEKSI

                if(policy.getStDataSourceID()!=null)
                    if(policy.getStDataSourceID().equalsIgnoreCase("3")) //DATA SISTEM KEAGENAN
                        dataSourceName = "KEAGENAN";

                final SQLUtil S2 = new SQLUtil(dataSourceName);

                try{

                    String status ="NOT DEFINED YET";

                    boolean updateObject = false;
                    boolean updatePolicy = false;

                    if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.DRAFT)){
                        status = "PENAWARAN";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.SPPA)){
                        status = "SPPA";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.POLICY)){
                        status = "POLIS";
                        updateObject = true;
                    }
                    else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE)){
                        status = "ENDORSE";
                        updatePolicy = true;
                    }else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)){
                        if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA)) status = FinCodec.ClaimStatus.PLA;
                        else if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.DLA)) status = FinCodec.ClaimStatus.DLA;

                        updatePolicy = true;
                    }

                    if(!policy.getStPolicyTypeID().equalsIgnoreCase("59")){
                        status = policy.getStStatus();
                        updatePolicy = true;
                    }

                    if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)){
                        if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA)) status = FinCodec.ClaimStatus.PLA;
                        else if(policy.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.DLA)) status = FinCodec.ClaimStatus.DLA;

                        updatePolicy = true;
                    }

                    if(updatePolicy){

                            String update = "";

                            if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.ENDORSE)){
                                update = "update ins_policy_endorse set pre_endorse_no='"+ policy.getStPolicyNo() +"', status_other = ? where pol_id = ? ";
                            }else if(policy.getStStatus().equalsIgnoreCase(FinCodec.PolicyStatus.CLAIM)){
                                update = "update ins_policy_preclaim set status_other = ? where pol_id = ? ";
                            }else{
                                if(policy.getStPolicyNo()==null)
                                    update = "update ins_policy set status_other = ? where pol_id = ? ";
                                else
                                    update = "update ins_policy set pol_no ='"+ policy.getStPolicyNo() +"',status_other = ? where pol_id = ? ";
                            }


                            PreparedStatement PS2 = S2.setQuery(update);

                            PS2.setObject(1, status);
                            PS2.setObject(2, policy.getStGatewayPolID());

                            int s = PS2.executeUpdate();

                            //S2.release();
                            //if(s==1) logger.logWarning("Berhasil update data pengajuan id : "+ policy.getStGatewayPolID());

                    }
                }catch (Exception e) {

                    ctx.setRollbackOnly();

                    throw e;

                } finally{
                    //logger.logWarning("###### closing koneksi 2 polno "+policy.getStPolicyNo());
                    S2.release();
                }
        }

    }


    public void saveEndorseFromGateway(InsurancePolicyView pol, String stNextStatus, boolean approvalMode) throws Exception {
        pol = (InsurancePolicyView) ObjectCloner.deepCopy(pol);

        final SQLUtil S = new SQLUtil();

        UserSession us = S.getUserSession();

        try {
                if (stNextStatus != null) {

                    final boolean isBranching = Tools.isYes(pol.getStDocumentBranchingFlag());

                    if (FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(stNextStatus) ||
                            FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(stNextStatus)||
                            FinCodec.PolicyStatus.INWARD.equalsIgnoreCase(stNextStatus))
                    {
                        if(FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus)){
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());
                            oldPolis.markUpdate();

                            oldPolis.setStAdminNotes("RENEWAL");

                            S.storeFromJobs(oldPolis);
                        }
                    } else {
                        if (!isBranching) {
                            final InsurancePolicyView oldPolis = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, pol.getStPolicyID());

                            oldPolis.markUpdate();

                            oldPolis.setStActiveFlag("N");

                            S.storeFromJobs(oldPolis);
                        }
                    }

                    //pol.setStParentID(pol.getStPolicyID());
                    //if (pol.isStatusClaimDLA()) pol.setStParentID(pol.getStParentID());
                    //else pol.setStParentID(pol.getStPolicyID());

                    pol.setStParentID(pol.getStPolicyID());
                    pol.setStRootID(pol.getStRootID());
                    pol.markNew();
                    pol.setStActiveFlag("Y");
                    pol.setStStatus(stNextStatus);
                    pol.setStApprovedWho(null);
                    pol.setDtApprovedDate(null);


                    //pol.getDeductibles().convertAllToNew();
                    pol.getCoins2().convertAllToNew();
                    pol.getInstallment().convertAllToNew();
                    pol.getDetails().convertAllToNew();
                    pol.getClausules().convertAllToNew();
                    pol.getCoinsCoverage().convertAllToNew();

                    pol.getClaimItems().convertAllToNew();
                    //pol.getCoverageReinsurance().convertAllToNew();
                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList policyDocuments = pol.getPolicyDocuments();

                    for (int i = 0; i < policyDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) policyDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList konversiDocuments = pol.getKonversiDocuments();

                    for (int i = 0; i < konversiDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) konversiDocuments.get(i);
                        doc.setStInsurancePolicyDocumentID(null);
                    }

                    final DTOList objects = pol.getObjects();
                    objects.convertAllToNew();

                    for (int i = 0; i < objects.size(); i++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        if (pol.isStatusClaim())
                            if (!isClaimObject) continue;

                        obj.setStPolicyObjectRefID(obj.getStPolicyObjectID());

                        if (obj.getStPolicyObjectRefRootID() == null)
                            obj.setStPolicyObjectRefRootID(obj.getStPolicyObjectID());
                        /*
                        if(pol.isStatusPolicy()){
                            obj.setStPolicyObjectPolicyRootID(obj.getStPolicyObjectID());
                        }*/

                        //obj.getClausules().convertAllToNew();
                        obj.getCoverage().convertAllToNew();
                        obj.getSuminsureds().convertAllToNew();
                        obj.getDeductibles().convertAllToNew();

                        //beban load
                        if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                            obj.getTreaties().convertAllToNew();

                            final DTOList treatyDetail = obj.getTreatyDetails();
                            treatyDetail.convertAllToNew();

                            for (int j = 0; j < treatyDetail.size(); j++) {
                                InsurancePolicyTreatyDetailView tredet1 = (InsurancePolicyTreatyDetailView) treatyDetail.get(j);

                                final DTOList shares = tredet1.getShares();
                                shares.convertAllToNew();

                            }
                        }
                        //end
                    }
                }

                if (pol.isNew()) pol.setStPolicyID(String.valueOf(IDFactory.createNumericID("POL")));

                if (pol.getStRootID() == null) pol.setStRootID(pol.getStPolicyID());

                final DTOList details = pol.getDetails();

                final HashMap insuranceItemsMap = getInsuranceItemsMap();

                for (int i = 0; i < details.size(); i++) {
                    InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

                    ip.setStPolicyID(pol.getStPolicyID());

                    if (ip.isNew()) ip.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));

                    ip.setInsuranceItem((InsuranceItemsView) insuranceItemsMap.get(ip.getStInsItemID()));
                }

                S.storeFromJobs(details);

                if(pol.isStatusClaim() || pol.isStatusClaimEndorse()){
                    final DTOList claimItems = pol.getClaimItems();

                    for (int i = 0; i < claimItems.size(); i++) {
                        InsurancePolicyItemsView it = (InsurancePolicyItemsView) claimItems.get(i);

                        it.setStPolicyID(pol.getStPolicyID());

                        if (it.isNew()) it.setStPolicyItemID(String.valueOf(IDFactory.createNumericID("POLITEM")));
                    }

                    S.storeFromJobs(claimItems);

                    final DTOList claimDocuments = pol.getClaimDocuments();

                    for (int i = 0; i < claimDocuments.size(); i++) {
                        InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) claimDocuments.get(i);

                        doc.setStPolicyID(pol.getStPolicyID());

                        final boolean marked = doc.isMarked();

                        if (marked) {
                            if (doc.getStInsurancePolicyDocumentID() != null)
                                doc.markUpdate();
                            else {
                                doc.setStInsurancePolicyDocumentID(String.valueOf(IDFactory.createNumericID("POLDOC")));
                                doc.markNew();
                            }
                        }

                        if (!marked && doc.getStInsurancePolicyDocumentID() != null) doc.markDelete();
                    }

                    S.storeFromJobs(claimDocuments);
                }

                /*
                final DTOList clausules = pol.getClausules();

                for (int j = 0; j < clausules.size(); j++) {
                    InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(j);
                    if (icl.isNew())
                        if (icl.isNew()) icl.setStPolicyClauseID(String.valueOf(IDFactory.createNumericID("POLCLAUS")));

                    icl.setStPolicyID(pol.getStPolicyID());
                        icl.setStPolicyObjectID(null);
                }

                S.store(clausules);
                */



                final DTOList objects = pol.getObjects();

                for (int i = 0; i < objects.size(); i++) {
                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                    final boolean isClaimObject2 = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                    if (pol.isStatusClaim())
                        if (!isClaimObject2) continue;

                    obj.setStPolicyID(pol.getStPolicyID());

                    if (obj.isNew()) {
                        final boolean isClaimObject = pol.getStClaimObjectID() != null && Tools.isEqual(obj.getStPolicyObjectID(), pol.getStClaimObjectID());

                        final String claimObject = pol.getStClaimObjectParentID()!=null?pol.getStClaimObjectParentID():obj.getStPolicyObjectID();

                        obj.setStPolicyObjectID(String.valueOf(IDFactory.createNumericID("POLOBJ")));

                        if (isClaimObject){
                            pol.setStClaimObjectParentID(claimObject);
                            pol.setStClaimObjectID(obj.getStPolicyObjectID());
                        }

                    }

                    S.storeFromJobs(obj);

                    final DTOList suminsureds = obj.getSuminsureds();

                    for (int j = 0; j < suminsureds.size(); j++) {
                        InsurancePolicyTSIView itsi = (InsurancePolicyTSIView) suminsureds.get(j);
                        if (itsi.isNew())
                            itsi.setStInsurancePolicyTSIID(String.valueOf(IDFactory.createNumericID("POLTSI")));

                        itsi.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        itsi.setStPolicyID(pol.getStPolicyID());

                        itsi.setStCreateWho("ADMIN");
                        itsi.setDtCreateDate(new Date());

                    }

                    S.storeFromJobs(suminsureds);

                    final DTOList coverage = obj.getCoverage();

                    for (int j = 0; j < coverage.size(); j++) {
                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) coverage.get(j);

                        if (cov.isNew())
                            cov.setStInsurancePolicyCoverID(String.valueOf(IDFactory.createNumericID("POLCOV")));
                        cov.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        cov.setStPolicyID(pol.getStPolicyID());
                        cov.setStInsurancePolicyCoverID(cov.getStInsurancePolicyCoverID());

                        cov.setStCreateWho("ADMIN");
                        cov.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(coverage);

                    final DTOList deductibles = obj.getDeductibles();

                    for (int j = 0; j < deductibles.size(); j++) {
                        InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                        if (ded.isNew())
                            ded.setStInsurancePolicyDeductibleID(String.valueOf(IDFactory.createNumericID("INSPOLDED")));
                        ded.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());
                        ded.setStPolicyID(pol.getStPolicyID());

                        ded.setStCreateWho("ADMIN");
                        ded.setDtCreateDate(new Date());
                    }

                    S.storeFromJobs(deductibles);

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        {
                            // save treaties

                            final DTOList treaties = obj.getTreaties();

                            treaties.combineDeleted();

                            for (int l = 0; l < treaties.size(); l++) {
                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(l);

                                if (tre.isDelete()) tre.getDetails().deleteAll();

                                if (tre.isNew())
                                    tre.setStInsurancePolicyTreatyID(String.valueOf(IDFactory.createNumericID("INSPOLTRE")));

                                tre.setStPolicyID(null);
                                tre.setStInsurancePolicyObjectID(obj.getStPolicyObjectID());

                                final DTOList tredetails = tre.getDetails();

                                tredetails.combineDeleted();

                                for (int j = 0; j < tredetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) tredetails.get(j);

                                    if (tredet.isDelete()) {
                                        tredet.getShares().deleteAll();
                                    }

                                    if (tredet.isNew())
                                        tredet.setStInsurancePolicyTreatyDetailID(String.valueOf(IDFactory.createNumericID("INSPOLTREDET")));

                                    tredet.setStInsurancePolicyTreatyID(tre.getStInsurancePolicyTreatyID());

                                    final DTOList shares = tredet.getShares();

                                    for (int k = 0; k < shares.size(); k++) {
                                        InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                        if (ri.isNew())
                                            ri.setStInsurancePolicyReinsID(String.valueOf(IDFactory.createNumericID("INSPOLTERSHARES")));

                                        //tambahan
                                        ri.setStInsuranceTreatyDetailID(tredet.getStInsuranceTreatyDetailID());
                                        //
                                        ri.setStInsurancePolicyTreatyDetailID(tredet.getStInsurancePolicyTreatyDetailID());
                                        ri.setStInsurancePolicyTreatyID(tredet.getStInsurancePolicyTreatyID());

                                    }
                                    S.storeFromJobs(shares);
                                }
                                S.storeFromJobs(tredetails);
                            }

                            S.storeFromJobs(treaties);
                        }
                    }
                }

                final DTOList deletedObjects = objects.getDeleted();

                if (deletedObjects != null)
                    for (int i = 0; i < deletedObjects.size(); i++) {
                    InsurancePolicyObjectView obj = (InsurancePolicyObjectView) deletedObjects.get(i);

                    S.storeFromJobs(obj);
                    //S.store(obj.getClausules());
                    S.storeFromJobs(obj.getSuminsureds());
                    S.storeFromJobs(obj.getCoverage());

                    //beban load
                    if (pol.isStatusPolicy() || pol.isStatusClaim() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusEndorseRI() || pol.isStatusTemporaryPolicy() || pol.isStatusInward() || pol.isStatusClaimEndorse()) {
                        final DTOList treaties = obj.getTreaties();

                        treaties.deleteAll();
                        treaties.combineDeleted();

                        for (int j = 0; j < treaties.size(); j++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(j);

                            final DTOList treDetails = tre.getDetails();

                            treDetails.deleteAll();
                            treDetails.combineDeleted();

                            for (int k = 0; k < treDetails.size(); k++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treDetails.get(k);

                                final DTOList treShares = trd.getShares();
                                treShares.deleteAll();
                                treShares.combineDeleted();

                                S.storeFromJobs(treShares);
                            }

                            S.storeFromJobs(treDetails);

                        }

                        S.storeFromJobs(treaties);
                      }
                    }

                final DTOList coins = pol.getCoins2();

                for (int i = 0; i < coins.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coins.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());
                }

                S.storeFromJobs(coins);

                final DTOList coinscoverage = pol.getCoinsCoverage();

                for (int i = 0; i < coinscoverage.size(); i++) {
                    InsurancePolicyCoinsView coin = (InsurancePolicyCoinsView) coinscoverage.get(i);

                    if (coin.isNew())
                        coin.setStInsurancePolicyCoinsID(String.valueOf(IDFactory.createNumericID("INSPOLCOIN")));

                    coin.setStPolicyID(pol.getStPolicyID());

                }

                S.storeFromJobs(coinscoverage);

                final DTOList installment = pol.getInstallment();

                for (int i = 0; i < installment.size(); i++) {
                    InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(i);

                    inst.setStPolicyID(pol.getStPolicyID());

                    if (inst.isNew())
                        inst.setStInsurancePolicyInstallmentID(String.valueOf(IDFactory.createNumericID("POLINST")));

                }

                S.storeFromJobs(installment);

            if (pol.getClaimObject() != null)
                pol.setStClaimObjectID(pol.getClaimObject().getStPolicyObjectID());

            if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
                if (FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(stNextStatus)||FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(pol.getStStatus())||FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(pol.getStNextStatus()))
                    if(pol.getStReference1()==null)
                        pol.generatePersetujuanPrinsipNo();

            if (FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(stNextStatus)){
                if(pol.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")){
                        if(pol.sudahAdaNoPolisFromPP(pol.getStReference1()))
                            throw new RuntimeException("Nomor polis sudah ada sebelumnya, hub. TI kantor pusat");

                        pol.generatePolicyNoFromPersetujuanPrinsip();
                } else {
                    if(pol.getStPolicyNo()!=null) pol.checkPolicyNoBefore(pol.getStPolicyNo());
                    else if(pol.getStPolicyNo()==null) pol.generatePolicyNo();
                }
            }

            if (FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(stNextStatus))
                pol.generatePolicyNo();

            if (FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(pol.getStStatus()))
                if(pol.getStPolicyNo()==null) pol.generatePolicyNo();


            if(FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(stNextStatus)){
                if(FinCodec.ClaimStatus.PLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generatePLANo();
                else if(FinCodec.ClaimStatus.DLA.equalsIgnoreCase(pol.getStClaimStatus()))
                       pol.generateDLANo();
            }

            S.storeFromJobs(pol);

            /*POSTING*/

            //final GLUtil.GLAccountCache glc = new GLUtil.GLAccountCache();

            //final InsurancePolicyTypeView poltype = getInsurancePolicyType(pol.getStPolicyTypeID());

            final InsurancePolicyView oldPol = (InsurancePolicyView) pol.getOld();

            boolean postflagchanged = oldPol == null || (!Tools.isEqual(oldPol.getStPostedFlag(), pol.getStPostedFlag()));

            boolean manualInstallment = false;
            if(pol.getStManualInstallmentFlag()!=null)
                if(pol.getStManualInstallmentFlag().equalsIgnoreCase("Y"))
                    manualInstallment = true;

            //boolean isFreeInstallment = pol.getInstallment().size() > 1 && manualInstallment;

            if(approvalMode){
               if (postflagchanged) {

                    if (Tools.isYes(pol.getStPostedFlag())) { // POST AR

                        if (pol.isStatusPolicy() || pol.isStatusEndorse() || pol.isStatusRenewal() || pol.isStatusTemporaryPolicy()) {

                            postARInstallment(pol);

                            postCoas(pol);

                            //postReasCumullation(pol);

                            //simpen buat sistem lama
                            saveABAProduk(pol);

                            saveABABayar(pol);

                            saveABAHutang(pol);

                            saveABAPajak(pol);

                        }

                        if (pol.isStatusClaimDLA() || pol.isStatusClaimEndorse()) {

                            //postReasClaim(pol);

                            postAPClaim(pol);

                            postAPClaimDetailSeparate(pol);

                            postARCoas(pol);

                            postAPTaxAcrual(pol);
                            //saveObjectToOtherTable(pol);

                            if(pol.getStClaimObjectParentID()!=null)
                                    updateClaimObjectFlag(pol.getStClaimObjectParentID(), pol.getStDLANo());
                        }

                        if(pol.isStatusEndorseRI()){
                            //postReasCumullation(pol);
                        }

                    } else {
                        // unpost
                    }
                }
            }

            if(pol.isStatusInward())
                if(approvalMode)
                    if (Tools.isYes(pol.getStPostedFlag()))
                        postReasCumullation(pol);

            if (Tools.isYes(pol.getStRIPostedFlag())){
                //postReasCumullation(pol);
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public void saveDataTeksLog(DataTeksMasukLogView log) throws Exception {
        final SQLUtil S = new SQLUtil("GATEWAY");

        try {
            if (log.isNew()){
                log.setStTeksLogID(String.valueOf(IDFactory.createNumericID("DATATEXT_LOG")));
            }

            S.store(log);

        } catch (Exception e) {

            ctx.setRollbackOnly();

            throw e;

        } finally {
            S.release();
        }
    }

    public InsuranceClosingView getClosingForPrinting(String closingid) throws Exception {
        InsuranceClosingView closing = getInsuranceClosing(closingid);

        return closing;
    }

    public InsuranceClosingView getInsuranceClosing(String stClosingID) throws Exception {
        final InsuranceClosingView clo = (InsuranceClosingView) ListUtil.getDTOListFromQuery("select * from ins_gl_closing where closing_id = ?",
                new Object[]{stClosingID},
                InsuranceClosingView.class).getDTO();

        return clo;
    }

    public void saveUploadSpreading(UploadHeaderReinsuranceView header, DTOList l) throws Exception {
        logger.logDebug("saveUploadSpreading: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSUPLOADREINSID")));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadReinsuranceSpreadingView j = (uploadReinsuranceSpreadingView) l.get(i);

                if (header.getStInsuranceUploadID() != null) {
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());
                }

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSUPLOADREINSDTLID")));
                }

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public DTOList getInsuranceUploadReins(String stUploadID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_upload_reins where ins_upload_id = ?",
                new Object[]{stUploadID},
                uploadReinsuranceSpreadingView.class);
    }

    private void postARInstallmentAcrualBasesJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getDetails();

        final DTOList installment = pol.getInstallment();

        final BigDecimal premi = pol.getDbPremiTotal();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            final ARInvoiceView invoice = new ARInvoiceView();
            invoice.markNew();

            invoice.setStPolicyID(pol.getStPolicyID());
            //invoice.setStInvoiceNo("E/PREMI-" + pol.getStPolicyNo() + "-INST" + (installmentSeq + 1));
            invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
            if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));
            //invoice.setDbAmount(pol.getDbAmount());
            /*invoice.setDtInvoiceDate(us.getDtTransactionDate());
            invoice.setDtDueDate(us.getDtTransactionDate());
             */
            invoice.setDtInvoiceDate(pol.getDtPolicyDate());
            invoice.setDtDueDate(pol.getDtPolicyDate());
            invoice.setDbAmountSettled(null);
            invoice.setStCurrencyCode(pol.getStCurrencyCode());
            invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
            invoice.setStPostedFlag("N");
            invoice.setStARCustomerID(pol.getStEntityID());
            invoice.setDtMutationDate(invoice.getDtInvoiceDate());
            invoice.setStEntityID(pol.getStEntityID());
            invoice.setStCostCenterCode(pol.getStCostCenterCode());
            //invoice.setStGLARAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLAR()));
            invoice.setStARTransactionTypeID(pol.getCoverSource().getStARTransactionTypeID());
            invoice.setStInvoiceType(FinCodec.InvoiceType.AR);
            //if(BDUtil.lesserThanZero(premi)) invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

            invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
            invoice.setStAttrPolicyNo(pol.getStPolicyNo());
            invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
            invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
            invoice.setStAttrPolicyName(pol.getStCustomerName());
            invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
            invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
            invoice.setStAttrPolicyID(pol.getStPolicyID());
            if(pol.isStatusEndorse()){
                invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
            }
            invoice.setStReferenceX0(String.valueOf(pol.getObjects().size()));

            invoice.setStRefID0("PREMI/" + pol.getStPolicyID());
            invoice.setStRefID1("INST/" + installmentSeq);

            invoice.setStPostedFlag("Y");

            final DTOList ivdetails = new DTOList();

            invoice.setDetails(ivdetails);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            boolean premiGFound = false;

            {

                for (int i = 0; i < artlines.size(); i++) {
                    ARTransactionLineView artl = (ARTransactionLineView) artlines.get(i);

                    if ("PREMIG".equalsIgnoreCase(artl.getStItemClass())) {
                        final InsurancePeriodView installmentPeriod = pol.getInstallmentPeriod();

                        final BigDecimal amt =
                                installmentPeriod == null ?
                                    premi :
                                    installmentPeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), premi);

                        premiGFound = true;

                        if(BDUtil.isZeroOrNull(amt)) continue;

                        final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                        ivd.markNew();

                        ivd.setStRefID0("PREMIG/" + pol.getStPolicyID());

                        ivdetails.add(ivd);


                        ivd.setStARTrxLineID(artl.getStARTrxLineID());
                        ivd.loadSettings();
                        //ivd.setStDescription(politem.getStDescription());

                        ivd.setDbEnteredAmount(BDUtil.negate(amt));

                        break;
                    }
                }
            }

            if (!premiGFound) throw new RuntimeException("no PREMIG item in trxtype " + artrxtype);

            BigDecimal amt = null;

            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                if (politem.isFee())
                    if (installmentSeq > 0) continue;

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbAmount())) posting = false;
                if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                //if (insuranceItem.isPremi()) {
                final ARInvoiceDetailView ivd = new ARInvoiceDetailView();

                ivd.markNew();

                ivdetails.add(ivd);

                ivd.setStEntityID(politem.getStEntityID());

                ivd.setStRefID0("POLI/" + politem.getStPolicyItemID());

                ivd.setStARTrxLineID(politem.getInsuranceItem().getStARTransactionTypeLineID());
                ivd.loadSettings();
                //ivd.setStDescription(politem.getStDescription());

                if (politem.getStTaxCode() == null) {
                    amt = politem.getDbAmount();
                } else {
                    if(BDUtil.isZeroOrNull(politem.getDbAmount())) amt = BDUtil.zero;
                    else amt = politem.getDbNetAmount();
                }

                if (politem.isComission() || politem.isDiscount())
                    amt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), amt);

                ivd.setDbEnteredAmount(BDUtil.negate(amt));

                //ivd.setStGLAccountID(glc.getAccountIDFromAccountNo(poltype.getStGLRevenue()));

                /*ivd.setDbTaxRate(politem.getDbTaxRate());
             ivd.setDbTaxAmount(taxamt);

             if (ivd.isComission())
                ivd.setStTaxCodeOnSettlement(politem.getStTaxCode());
             else
                ivd.setStTaxCode(politem.getStTaxCode());*/

                if (ivd.isComission()&&politem.getStTaxCode()!=null)
                    ivd.setStTaxCode(politem.getStTaxCode());

                if (ivd.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    //if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);

                    String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

                    EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);

                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");
                }

            }

            invoice.recalculate();

            getRemoteAccountReceivable().save(invoice);
   
        }

        postARTaxAcrualJurnalBalik(pol);

    }

    private void postARTaxAcrualJurnalBalik(InsurancePolicyView pol) throws Exception {
        UserSession us = SessionManager.getInstance().getSession();

        final DTOList details = pol.getDetails();

        final DTOList installment = pol.getInstallment();

        final BigDecimal premi = pol.getDbPremiTotal();

        final InsurancePeriodView insurancePeriod = pol.getInsurancePeriod();

        if (insurancePeriod == null) throw new RuntimeException("Installment period cannot be empty");

        if (pol.getStPolicyNo() == null) throw new RuntimeException("Policy number required !");

        for (int installmentSeq = 0; installmentSeq < installment.size(); installmentSeq++) {
            InsurancePolicyInstallmentView inst = (InsurancePolicyInstallmentView) installment.get(installmentSeq);

            //final DTOList insuranceItemLOV = getInsuranceItemLOV(pol.getStCoverTypeCode());

            String artrxtype = pol.getCoverSource().getStARTransactionTypeID();

            final DTOList artlines = ListUtil.getDTOListFromQuery(
                    "select * from ar_trx_line where ar_trx_type_id = ?",
                    new Object[]{artrxtype},
                    ARTransactionLineView.class
                    );

            BigDecimal amt = null;

            String taxEntityId = Parameter.readString("AR_AP_DEF_TAX_ENTITY_" + pol.getStCostCenterCode());

            EntityView taxEntity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, taxEntityId);


            for (int i = 0; i < details.size(); i++) {
                InsurancePolicyItemsView politem = (InsurancePolicyItemsView) details.get(i);

                final ARInvoiceView invoice = new ARInvoiceView();
                invoice.markNew();

                invoice.setStPolicyID(pol.getStPolicyID());
                invoice.setStInvoiceNo("G" + pol.getStPolicyNo());
                if(installment.size()>1) invoice.setStInvoiceNo("G" + pol.getStPolicyNo() + "-" + (installmentSeq + 1));

                invoice.setDtInvoiceDate(pol.getDtPolicyDate());
                invoice.setDtDueDate(pol.getDtPolicyDate());
                invoice.setDbAmountSettled(null);
                invoice.setStCurrencyCode(pol.getStCurrencyCode());
                invoice.setDbCurrencyRate(pol.getDbCurrencyRate());
                invoice.setStPostedFlag("Y");
                invoice.setStARCustomerID(taxEntityId);
                invoice.setDtMutationDate(invoice.getDtInvoiceDate());
                invoice.setStEntityID(taxEntityId);
                invoice.setStCostCenterCode(pol.getStCostCenterCode());
                invoice.setStARTransactionTypeID(Parameter.readString("COMISSION_AR_TRX"));
                invoice.setStInvoiceType(FinCodec.InvoiceType.AP);

                invoice.setStAttrPolicyTypeID(pol.getStPolicyTypeID());
                invoice.setStAttrPolicyNo(pol.getStPolicyNo());
                invoice.setDtAttrPolicyPeriodStart(pol.getDtPeriodStart());
                invoice.setDtAttrPolicyPeriodEnd(pol.getDtPeriodEnd());
                invoice.setStAttrPolicyName(pol.getStCustomerName());
                invoice.setStAttrPolicyAddress(pol.getStCustomerAddress());
                invoice.setDbAttrPolicyTSI(BDUtil.negate(pol.getDbInsuredAmount()));
                invoice.setDbAttrPolicyTSITotal(BDUtil.negate(pol.getDbInsuredAmount()));
                invoice.setStAttrPolicyID(pol.getStPolicyID());
                if(pol.isStatusEndorse()){
                    invoice.setDbAttrPolicyTSITotal(pol.getDbInsuredAmountEndorse());
                }

                invoice.setStRefID0("TAX/" + politem.getStPolicyItemID());
                invoice.setStRefID1("INST/" + installmentSeq);
                invoice.setStRefID2("TAX/" + pol.getStPolicyID());

                invoice.setStNoSuratHutang(
                                "SHP/POL/"+
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtPolicyDate())+
                                "/"+
                                DateUtil.getYear(pol.getDtPolicyDate()));

                //invoice.setStPostedFlag("Y");

                final DTOList ivdetails = new DTOList();

                invoice.setDetails(ivdetails);

                if (politem.isFee())
                    if (installmentSeq > 0) continue;

                boolean posting = true;

                if(BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = false;
                //if(BDUtil.isZeroOrNull(politem.getDbAmount()) && politem.isComission() && !BDUtil.isZeroOrNull(politem.getDbTaxAmount())) posting = true;

                if(!posting) continue;

                final InsuranceItemsView insuranceItem = politem.getInsuranceItem();

                if (politem.isComission()) {
                    final ARInvoiceDetailView ivdc = new ARInvoiceDetailView();

                    if(politem.getInsItem().getStUseTaxFlag()!=null)
                        if(politem.getInsItem().isNotUseTax())
                            continue;

                    ivdc.setStRefID0("TAX/" + politem.getStPolicyItemID());

                    ivdc.markNew();

                    ivdetails.add(ivdc);


                    if (taxEntity == null)
                        throw new RuntimeException("Incorrect setup for AR_AP_DEF_TAX_ENTITY (entity not found:" + taxEntityId + ")");

                    ivdc.setStEntityID(taxEntityId);

                    final String stTaxCode = politem.getStTaxCode();
                    final String stARTrxTypeID = politem.getInsuranceItem().getARTrxLine().getStARTrxTypeID();

                    final ARTransactionLineView taxTransactionLineView = (ARTransactionLineView) ListUtil.getDTOListFromQuery(
                            "select * from ar_trx_line where tax_code = ? and ar_trx_type_id = ?",
                            new Object[]{stTaxCode, stARTrxTypeID},
                            ARTransactionLineView.class
                            ).getDTO();

                    if (taxTransactionLineView == null)
                        throw new RuntimeException("Tax transaction line not found : taxCode = " + stTaxCode + ", trxtype = " + stARTrxTypeID);

                    ivdc.setStARTrxLineID(taxTransactionLineView.getStARTrxLineID());
                    ivdc.loadSettings();
                    ivdc.setStNegativeFlag("N");
                    //ivdc.setStDescription(politem.getStDescription());
                    BigDecimal taxamt = politem.getDbTaxAmount();

                    if (politem.isComission() || politem.isDiscount())
                        taxamt = insurancePeriod.getInstallmentAmount(installmentSeq, pol.getStInstallmentPeriods(), taxamt);

                    ivdc.setDbEnteredAmount(BDUtil.negate(taxamt));
                    invoice.setDbEnteredAmount(BDUtil.negate(taxamt));

                    ivdc.setStTaxCode(stTaxCode);

                    //ivdc.setRef(ivd);
                    ivdc.setStTaxFlag("Y");

                    String jenis = taxTransactionLineView.getStItemDesc().toUpperCase().startsWith("PPH21")?"PPH21":"PPH23";

                    invoice.setStNoSuratHutang(
                                "SHP/POL/"+
                                 jenis +
                                "/" +
                                invoice.getStARCustomerID()+
                                "/"+
                                invoice.getStCostCenterCode()+
                                "/"+
                                DateUtil.getMonth2Digit(pol.getDtPolicyDate())+
                                "/"+
                                DateUtil.getYear(pol.getDtPolicyDate()));
                }

                //}
                invoice.recalculateTaxAcrual(true);

                getRemoteAccountReceivable().save(invoice);
            }


        }
    }

    public void saveUploadEndorsePolis(UploadEndorseHeaderView header, DTOList l) throws Exception {
        logger.logDebug("saveUploadEndorsemenPolis: " + l);

        final SQLUtil S = new SQLUtil();

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSUPLOADID")));

            header.generateRecapNo();
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadEndorseDetailView j = (uploadEndorseDetailView) l.get(i);

                if(header.getStInsuranceUploadID()!=null)
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSUPLOADDTLID")));
                }

            }

            S.store(l);

            S.store(header);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public DTOList getUploadEndorseDetail(String stUploadID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_upload_endorse_detail where ins_upload_id = ? order by ins_upload_dtl_id",
                new Object[]{stUploadID},
                uploadEndorseDetailView.class);
    }

   public void saveUploadProposal(UploadHeaderProposalCommView header, DTOList l) throws Exception {
        logger.logDebug("saveUploadSpreading: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        String ccCode = header.getStCostCenterCode();

        String counterKey = DateUtil.getYear2Digit(header.getDtPeriodeAkhir());

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSUPLOADPROPOSALID")));

            String noUrutSHK = String.valueOf(IDFactory.createNumericID("SHKNO" + counterKey + ccCode, 1));
            header.setStNoSuratHutang(noUrutSHK + "/SHK/POL/COMM/" + header.getStCostCenterCode() + "/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date()));

        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadProposalCommView j = (uploadProposalCommView) l.get(i);

                if (header.getStInsuranceUploadID() != null) {
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());
                    j.setStNoSuratHutang(header.getStNoSuratHutang());
                    j.setStCostCenterCode(header.getStCostCenterCode());
                    j.setDtPeriodeAwal(header.getDtPeriodeAwal());
                    j.setDtPeriodeAkhir(header.getDtPeriodeAkhir());
                    j.setStPolicyTypeGroupID(header.getStPolicyTypeGroupID());
                    j.setStPolicyTypeID(header.getStPolicyTypeID());
                    j.setStKeterangan(header.getStKeterangan());
                }

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSUPLOADPROPOSALDTLID")));
                }

            }

            S.store(l);

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

    public DTOList getInsuranceProposalComm(String stUploadID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_proposal_komisi where ins_upload_id = ?",
                new Object[]{stUploadID},
                uploadProposalCommView.class);
    }

    

    public uploadProposalCommView getProposalForPrinting(String rcid) throws Exception {

        final uploadProposalCommView rcp = (uploadProposalCommView) ListUtil.getDTOListFromQuery(
                "select a.* from ins_proposal_komisi a where ins_upload_id = ?",
                new Object[]{rcid},
                uploadProposalCommView.class).getDTO();

        //rcp = getARReceipt(rcid);
        return rcp;
    }

    public void getProposalForPrintingExcel(String rcid) throws Exception {

        final DTOList l = EXCEL_PROPOSAL(rcid);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_PROPOSAL();

    }

    public DTOList EXCEL_PROPOSAL(String rcid) throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.pol_no,a.tertanggung,a.amount,a.no_surat_hutang,a.periode_awal,a.periode_akhir,a.cc_code,"
                + " (select x.receipt_no from ins_policy x where x.pol_id = a.ins_pol_id) as reinsurer_note ");

        sqa.addQuery(" from ins_proposal_komisi a ");
        sqa.addClause("a.ins_upload_id = ?");
        sqa.addPar(rcid);

        final String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_PROPOSAL() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("proposal_komisi");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            final String no_surat[] = h.getFieldValueByFieldNameST("no_surat_hutang").split("[\\/]");
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + h.getFieldValueByFieldNameST("cc_code")).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Lampiran Surat Izin Pengeluaran Dana");

            XSSFRow row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("NO. : " + norut);

            XSSFRow row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("NO. SHK : " + h.getFieldValueByFieldNameST("no_surat_hutang"));

            XSSFRow row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("Tanggal : " + LanguageManager.getInstance().translate(DateUtil.getDateStr(h.getFieldValueByFieldNameDT("periode_awal"), "dd ^^ yyyy"))
                    + " s/d " + LanguageManager.getInstance().translate(DateUtil.getDateStr(h.getFieldValueByFieldNameDT("periode_akhir"), "dd ^^ yyyy")));

            //bikin header
            XSSFRow row5 = sheet.createRow(5);
            row5.createCell(0).setCellValue("no polis");
            row5.createCell(1).setCellValue("tertanggung");
            row5.createCell(2).setCellValue("komisi");
            row5.createCell(3).setCellValue("pelunasan");

            XSSFRow row = sheet.createRow(i + 6);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("reinsurer_note"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=penerimaan_premi" + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public uploadProposalCommView getProposalForPrintingMix(String rcid) throws Exception {

        final uploadProposalCommView rcp = (uploadProposalCommView) ListUtil.getDTOListFromQuery(
                "select ins_upload_id,status1,status2,status3,status4,cc_code,create_date,periode_awal,periode_akhir,"
                + "no_surat_hutang,pol_type_grp_id,pol_type_id,"
                + "count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total,create_who "
                + "from ins_proposal_komisi "
                + "where ins_upload_id = ? "
                + "group by ins_upload_id,status1,status2,status3,status4,cc_code,create_date,periode_awal,periode_akhir,"
                + "no_surat_hutang,pol_type_grp_id,pol_type_id,create_who "
                + "order by ins_upload_id desc ",
                new Object[]{rcid},
                uploadProposalCommView.class).getDTO();

        return rcp;
    }

}
