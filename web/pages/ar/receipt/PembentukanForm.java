/***********************************************************************
 * Module:  com.webfin.ar.forms.PembentukanForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/

package pages.ar.receipt;

import com.crux.common.model.HashDTO;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.ObjectCloner;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.BDUtil;
import com.crux.util.JNDIUtil;
import com.crux.pool.DTOPool;
import com.crux.util.ListUtil;
import com.webfin.FinCodec;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.CurrencyManager;

import com.webfin.insurance.model.*;
import com.webfin.insurance.ejb.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.math.BigDecimal;
import com.crux.util.SQLAssembler;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.util.GLUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.ServletOutputStream;
import org.joda.time.DateTime;

public class PembentukanForm extends Form {
    
    private ARInvestmentDepositoView deposito;
    private String year;
    private String stReceiptClassID;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private ARReceiptClassView receiptclass = null;
    private ARReceiptView receipt;
    private ARInvestmentPencairanView pencairan;
    private ARInvestmentBungaView bunga;
    private boolean approvalMode;
    private boolean reverseMode;
    
    private final static transient LogManager logger = LogManager.getInstance(PembentukanForm.class);
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        deposito = new ARInvestmentDepositoView();
        
        deposito.markNew();
        
        deposito.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        
        deposito.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        deposito.setDbCurrencyRate(BDUtil.one);
        deposito.setDbPajak(new BigDecimal(20));
        
        doNewPencairan();
        
        doNewBunga();
        
        setTitle("CREATE DEPOSITO");
    }
    
    public void edit(String ardepoid) throws Exception {
        view(ardepoid);
        
        setReadOnly(false);
        
        deposito.markUpdate();
        
        final DTOList cair = deposito.getPencairan();
        
        for (int i = 0; i < cair.size(); i++) {
            ARInvestmentPencairanView pcr = (ARInvestmentPencairanView) cair.get(i);
            
            pcr.markUpdate();
        }
        
        final DTOList bunga = deposito.getBunga();
        
        for (int j = 0; j < bunga.size(); j++) {
            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(j);
            
            bng.markUpdate();
        }
        
        setTitle("EDIT DEPOSITO");
    }
    
    public void view(String ardepoid) throws Exception {
        
        deposito = getRemoteGeneralLedger().loadDeposito(ardepoid);
        
        if (deposito==null) throw new RuntimeException("Deposito not found !");
        
        setReadOnly(true);
        
        setTitle("VIEW DEPOSITO");
    }
    
    public void save() throws Exception {
        final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView)ObjectCloner.deepCopy(deposito);
        
        validate();
        
        deposito.checkNodefo();
        
        getRemoteGeneralLedger().save(cloned);
        
        close();
    }
    
    public void saveWithoutJurnal() throws Exception {
        final ARInvestmentDepositoView cloned = (ARInvestmentDepositoView)ObjectCloner.deepCopy(deposito);
        
        validate();
        
        deposito.checkNodefo();
        
        getRemoteGeneralLedger().saveWithoutJurnal(cloned);
        
        close();
    }
    
    public void approval(String ardepoid) throws Exception {
        edit(ardepoid);
        
        super.setReadOnly(true);
        
        approvalMode = true;
        
    }
    
    public void approve() throws Exception {
        
        String depo_id = deposito.getStARDepoID();
        
        final ARInvestmentDepositoView jh = new ARInvestmentDepositoView();
        
        final DTOList details = jh.getDetails(depo_id);
        
        jh.markUpdate();
        jh.setStEffectiveFlag("Y");
        jh.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());
        jh.setDtApprovedDate(new Date());
        
        getRemoteGeneralLedger().approve(jh, details);
        
        approvalMode = true;
        
        close();
    }
    
    public void createRenewal(ARInvestmentDepositoView depo) throws Exception {
        
        approvalMode = true;
        
        final DTOList pencairan = depo.getPencairan();
        
        for (int i = 0; i < pencairan.size(); i++) {
            ARInvestmentPencairanView cair = (ARInvestmentPencairanView) pencairan.get(i);
            
            //if (cair.getDtTglCair()!=null) return;
            
            cair.setStARParentID(cair.getStARCairID());
            
        }
        
        final DTOList bunga = depo.getBunga();
        
        for (int j = 0; j < bunga.size(); j++) {
            ARInvestmentBungaView bng = (ARInvestmentBungaView) bunga.get(j);
            
            //if (bng.getDtTglCair()!=null) return;
            
            bng.setStARParentID(bng.getStARBungaID());
        }
        
        depo.setStNextStatus(FinCodec.Deposito.RENEWAL);
        
        getRemoteGeneralLedger().saveRenewal(depo,depo.getStNextStatus(),approvalMode);
        
    }
    
    public void close() {
        super.close();
    }
    
    public String getStBranch() {
        return stBranch;
    }
    
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }
    public ARInvestmentDepositoView getDeposito() {
        return deposito;
    }
    
    public void setDeposito(ARInvestmentDepositoView deposito) {
        this.deposito = deposito;
    }
    
    public void onChgCurrency() throws Exception {
        deposito.setDbCurrencyRate(
                CurrencyManager.getInstance().getRate(
                deposito.getStCurrency(),
                deposito.getDtTgldepo()
                )
                );
        deposito.setStCurrency(deposito.getStCurrency());
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }
    
    public void onChgCall() throws Exception {
        
        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            deposito.setStHari(deposito.getStHari());
            deposito.setStBulan("0");
        } else {
            deposito.setStHari("0");
            deposito.setStBulan(deposito.getStBulan());
        }
        
    }
    
    public void refresh() {
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public void calcDays() throws Exception {
        
        if(deposito.getStHari()!=null){
            DateTime startDate = new DateTime(deposito.getDtTglawal());
            DateTime endDate = new DateTime();
            
            if(deposito.getStKodedepo().equalsIgnoreCase("1"))
                endDate = startDate.plusDays(Integer.parseInt(deposito.getStHari()));
            
            deposito.setDtTglakhir(endDate.toDate());
        }
    }
    
    public void calcMonths() throws Exception {
        
        if(deposito.getStBulan()!=null){
            DateTime startDate = new DateTime(deposito.getDtTglawal());
            DateTime endDate = new DateTime();
            
            if(deposito.getStKodedepo().equalsIgnoreCase("2"))
                endDate = startDate.plusMonths(Integer.parseInt(deposito.getStBulan()));
            
            deposito.setDtTglakhir(endDate.toDate());
        }
    }
    
    public void validate() {
        if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
            if (deposito.getStHari()==null) throw new RuntimeException("Jumlah Hari Belum Diisi");
        } else {
            if (deposito.getStBulan()==null) throw new RuntimeException("Jumlah Bulan Belum Diisi");
        }
    }
    
    public void doNewPencairan() {
        final ARInvestmentPencairanView pcr = new ARInvestmentPencairanView();
        
        pcr.markNew();
        
        deposito.getPencairan().add(pcr);
        
        pencairan = pcr;
    }
    
    public void doNewBunga() {
        final ARInvestmentBungaView bng = new ARInvestmentBungaView();
        
        bng.markNew();
        
        deposito.getBunga().add(bng);
        
        bunga = bng;
    }
    
    public boolean isApprovalMode() {
        return approvalMode;
    }
    
    public void setApprovalMode(boolean approvalMode) {
        this.approvalMode = approvalMode;
    }
    
    public void generateDBNominal() throws Exception {
        deposito.setDbNominal(BDUtil.mul(deposito.getDbCurrencyRate(), deposito.getDbNominalKurs()));
    }
    
    public boolean isReverseMode() {
        return reverseMode;
    }
    
    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }
    
    public void reverse() throws Exception{
        
        //boolean withinCurrentMonth = DateUtil.getDateStr(deposito.getDtTgldepo(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));
        //boolean canReverse = true;
        
        // if (!canReverse) throw new RuntimeException("Tanggal Pendebetan Tidak Valid (Sudah Tutup Produksi)");
        
        getRemoteGeneralLedger().reverseDeposito(deposito);
        
        close();
    }
    
}
