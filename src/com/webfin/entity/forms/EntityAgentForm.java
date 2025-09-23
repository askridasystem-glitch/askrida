/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityAgentForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.entity.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.crux.ff.model.FlexTableView;
import com.webfin.entity.model.EntityView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.entity.ejb.EntityManager;
import com.webfin.entity.ejb.EntityManagerHome;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class EntityAgentForm extends WebForm {

    private EntityView entity = null;
    private LOV lovPaymentTerm;
    private FormTab tabs;
    private EntityAddressView address;
    private String stSelectedAddress;
    private DTOList list;
    private boolean canSelectCompanyType = SessionManager.getInstance().getSession().hasResource("SELECT_COMP_TYPE");

    public LOV getLovAddresses() {
        return entity.getAddresses();
    }

    public void createNew() {
        entity = new EntityView();
        entity.markNew();
        entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        entity.setStCategory1("1");
        entity.setStRef1("39");
        entity.setStRef2("230");
        doNewAddress();
    }

    public void onClassChange() {
    }

    public void doDeleteAddress() {
    }

    public void edit() throws Exception {
        view();
        setReadOnly(false);

        entity.markUpdate();

        final DTOList addresses = entity.getAddresses();

        for (int i = 0; i < addresses.size(); i++) {
            EntityAddressView ead = (EntityAddressView) addresses.get(i);

            ead.markUpdate();
        }

        select_approval();
    }

    public void view() throws Exception {
        final String entity_id = (String) getAttribute("ent_id");
        entity = getRemoteEntityManager().loadEntity(entity_id);

        if (entity == null) {
            throw new RuntimeException("Entity not found !");
        }

        setReadOnly(true);
    }

    public void afterUpdateForm() {
        if (entity != null) {
            final Integer idx = NumberUtil.getInteger(stSelectedAddress);
            address = idx == null ? null : (EntityAddressView) entity.getAddresses().get(idx.intValue());

            final DTOList addresses = entity.getAddresses();

            entity.setStAddress(null);

            for (int i = 0; i < addresses.size(); i++) {
                EntityAddressView adr = (EntityAddressView) addresses.get(i);

                if (entity.getStAddress() == null) {
                    entity.setStAddress(adr.getStAddress());
                }

                if (adr.isPrimary()) {
                    entity.setStAddress(adr.getStAddress());
                    break;
                }
            }

        }
    }

    public String getStSelectedAddress() {
        return stSelectedAddress;
    }

    public void setStSelectedAddress(String stSelectedAddress) {
        this.stSelectedAddress = stSelectedAddress;
    }

    public EntityAddressView getAddress() {
        return address;
    }

    public void setAddress(EntityAddressView address) {
        this.address = address;
    }

    public void doNewAddress() {
        final EntityAddressView adr = new EntityAddressView();
        adr.markNew();

        entity.getAddresses().add(adr);

        stSelectedAddress = String.valueOf(entity.getAddresses().size() - 1);

        address = adr;
    }

    public void selectAddress() {
        // do nothing
    }

    public FormTab getTabs() {
        if (tabs == null) {
            tabs = new FormTab();

            tabs.add(new FormTab.TabBean("TAB1", "{L-ENGADDRESS-L}{L-INAALAMAT-L}", true));
            tabs.add(new FormTab.TabBean("TAB2", "{L-ENGPRODUCT TYPE-L}{L-INAJENIS PRODUK-L}", true));
            tabs.add(new FormTab.TabBean("TAB3", "{L-ENGDOCUMENT-L}{L-INADOKUMEN-L}", true));
            //tabs.add(new FormTab.TabBean("TAB2","RELATIONS",true));

            tabs.setActiveTab("TAB1");
        }
        return tabs;
    }

    public void setTabs(FormTab tabs) {
        this.tabs = tabs;
    }

    public LOV getLovPaymentTerm() throws Exception {
        if (lovPaymentTerm == null) {
            lovPaymentTerm = ListUtil.getLookUpFromQuery("select payment_term_id,description from payment_term");
        }
        return lovPaymentTerm;
    }

    public void setLovPaymentTerm(LOV lovPaymentTerm) {
        this.lovPaymentTerm = lovPaymentTerm;
    }

    public EntityAgentForm() {
    }

    public EntityView getEntity() {
        return entity;
    }

    public void setEntity(EntityView entity) {
        this.entity = entity;
    }

    private EntityManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((EntityManagerHome) JNDIUtil.getInstance().lookup("EntityManagerEJB", EntityManagerHome.class.getName())).create();
    }

    public void doSave() throws Exception {
        final EntityView cloned = (EntityView) ObjectCloner.deepCopy(entity);

        validate();

        if (cloned.getStTaxFile() != null) {
            CheckNPWP(cloned.getStTaxFile());
        }

        //if(entity.getStGLCode()==null) makeGLCOde();

        //if(entity.getStGLCode()==null)  throw new RuntimeException("GL CODE tidak boleh kosong, hub. admin jika terjadi error ini");

        getRemoteEntityManager().save(cloned);

        super.close();
    }

    public void doClose() {
        super.close();
    }

    public boolean CheckNPWP(String NPWP) {
        /* format NPWP : XX.XXX.XXX.X-XXX.XXX
         *				 11.111.111.1-111.111
         *				 01234567890123456789  */

        if (NPWP.length() != 20) {
            throw new RuntimeException("Format No NPWP Salah,Harus 20 Digit!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
        }

        String npwpAll[] = NPWP.split("[\\.]");
        boolean cek = true;

        if (!NPWP.substring(2, 3).equalsIgnoreCase(".") || !NPWP.substring(6, 7).equalsIgnoreCase(".")
                || !NPWP.substring(10, 11).equalsIgnoreCase(".") || !NPWP.substring(16, 17).equalsIgnoreCase(".")) {
            throw new RuntimeException("Format No NPWP Salah, Harus Ada Titik!<br>Format : YY.YYY.YYY.Y-YYY.YYY ");
        }

        if (npwpAll[0].length() != 2) {
            cek = false;
            throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
        }

        if (npwpAll[1].length() != 3) {
            cek = false;
            throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");

        }
        if (npwpAll[2].length() != 3) {
            cek = false;
            throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
        }
        if (npwpAll[3].length() != 5) {
            cek = false;
            throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");

        }
        if (npwpAll[4].length() != 3) {
            cek = false;
            throw new RuntimeException("Format No NPWP Salah!<br>Format : YY.YYY.YYY.Y-YYY.YYY");
        }

        return cek;
    }

//    public String generateGLCode()throws Exception{
//        final SQLUtil S = new SQLUtil();
//        String counter2 = null;
//        try {
//            final PreparedStatement PS = S.setQuery("select gl_code"+
//                    " from ent_master where ref1 = ? "+
//                    " order by gl_code::bigint desc limit 1");
//
//            PS.setString(1,entity.getStRef1());
//
//            final ResultSet RS = PS.executeQuery();
//
//            if(RS.next()){
//                RS.last();
//
//                String glcode = RS.getString("gl_code");
//
//                //01001
//
//                glcode = glcode.substring(2);
//                String counter = null;
//                //glcode = glcode + 1;
//                int glcode2 = Integer.parseInt(glcode);
//                glcode2 = glcode2 + 1;
//                String tes = String.valueOf(glcode2);
//
//                if(tes.length()==1) counter = "00" + tes;
//                else if(tes.length()==2) counter = "0" + tes;
//                else if(tes.length()==3) counter = tes;
//
//
//                String jenis = entity.getStRef1();
//                int jenis2 = Integer.parseInt(jenis);
//                String jenis3 = String.valueOf(jenis2);
//
//                if(jenis3.length()==1) counter2 = "0" + jenis + counter;
//                else if(jenis3.length()==2) counter2 = jenis + counter;
//            }else{
//                String jenis = entity.getStRef1();
//                int jenis2 = Integer.parseInt(jenis);
//                String jenis3 = String.valueOf(jenis2);
//                if(jenis3.length()==1) counter2 = "0" + jenis + "001";
//                else if(jenis3.length()==2) counter2 = jenis + "001";
//            }
//
//            if("39".equalsIgnoreCase(entity.getStRef1())||"96".equalsIgnoreCase(entity.getStRef1()))
//                counter2 = "00000";
//
//            return counter2;
//
//        } finally {
//            S.release();
//        }
//    }
//    public void makeGLCOde() throws Exception{
//        entity.setStGLCode(generateGLCode());
//    }
    public void checkEntityNameWithCategory(EntityView entity) throws Exception {
    }

    public boolean isCanSelectCompanyType() {
        return canSelectCompanyType;
    }

    public void setCanSelectCompanyType(boolean canSelectCompanyType) {
        this.canSelectCompanyType = canSelectCompanyType;
    }

    public void onCategoryChange() throws Exception {

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
            if (entity.getStCategory1().equalsIgnoreCase("4")) {
                entity.setStCategory1(null);
                throw new RuntimeException("Pembuatan Data Customer Bank BPD hanya diperbolehkan di kantor pusat, hub. bag. underwriting");
            }
        }


        entity.setStRef1(null);
        entity.setStRef2(null);

        if (entity.getStCategory1().equalsIgnoreCase("4")) {
            entity.setStRef1(entity.getStCostCenterCode());

            if (entity.getStCostCenterCode().equalsIgnoreCase("25")) {
                entity.setStRef1("21");
            }

            entity.setStRef2(getCompanyGroup());
        }
    }

    public String getCompanyGroup() throws Exception {
        final SQLUtil S = new SQLUtil();
        String vs_code = null;
        try {

            final PreparedStatement PS = S.setQuery("select vs_code "
                    + " from s_company_group "
                    + " where ref1 = ?");

            PS.setString(1, entity.getStRef1());

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                vs_code = RS.getString("vs_code");
            }

            return vs_code;

        } finally {
            S.release();
        }
    }

    public void validate() {
        final String category = entity.getStCategory1();
        final String jenis = entity.getStRef1();
        final String group = entity.getStRef2();
        final int jenisInt = Integer.parseInt(entity.getStRef1());

        if (category.equalsIgnoreCase("4") && jenis.equalsIgnoreCase("39")) {
            throw new RuntimeException("Untuk Kategori BPD tidak boleh Jenis Perusahaan OTHER CLASS");
        }

        if (category.equalsIgnoreCase("4") && group.equalsIgnoreCase("999")) {
            throw new RuntimeException("Untuk Kategori BPD tidak boleh Group Perusahaan UNGROUP");
        }

        if (!category.equalsIgnoreCase("4") && jenisInt >= 10 && jenisInt <= 70) {
            if (jenisInt != 39) {
                throw new RuntimeException("Untuk Kategori Selain BPD tidak boleh Jenis Perusahaan BPD");
            }
        }

        if (!entity.isUpdate()) {
            cekContains();
        }

    }

    public void cekContains() {

        String cc_code = SessionManager.getInstance().getSession().getStBranch();
        final String jenis = entity.getStRef1();

        if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {

            if (!jenis.equalsIgnoreCase("39")) {
                throw new RuntimeException("Untuk Kategori Selain OTHER CLASS hanya boleh di entry kantor pusat");
            }

            if (entity.getStEntityName().toUpperCase().contains("BANK")) {
                throw new RuntimeException("Pembuatan data customer BANK hanya diperbolehkan di entry kantor pusat, hub. bag. underwriting");
            }

            if (entity.getStEntityName().toUpperCase().contains("BPR")) {
                throw new RuntimeException("Pembuatan data customer BPR hanya diperbolehkan di entry kantor pusat, hub. bag. underwriting");
            }

            if (entity.getStEntityName().toUpperCase().contains("BPD")) {
                throw new RuntimeException("Pembuatan data customer BPD hanya diperbolehkan di entry kantor pusat, hub. bag. underwriting");
            }

            if (entity.getStEntityName().toUpperCase().contains("ASURANSI")) {
                throw new RuntimeException("Pembuatan data customer Asuransi/Reasuransi/Koasuransi hanya diperbolehkan di entry kantor pusat, hub. bag. underwriting");
            }
        }

    }

    public void changeJenisPerusahaan() throws Exception {
        String jenis = getJenisPerusahaan(entity.getStRef1());

        final String jenis2 = entity.getStRef1();

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
            if (jenis.equalsIgnoreCase("1")) {
                entity.setStRef1(null);
                throw new RuntimeException("Jenis Perusahaan Data Customer Bank hanya diperbolehkan di entry kantor pusat, hub. bag. underwriting");
            }

            if (!jenis2.equalsIgnoreCase("39")) {
                entity.setStRef1(null);
                throw new RuntimeException("Untuk Kategori Selain OTHER CLASS hanya boleh di entry kantor pusat");
            }

        }
    }

    public void changeGroupPerusahaan() throws Exception {
        String jenis = getGroupPerusahaan(entity.getStRef2());

        String cc_code = SessionManager.getInstance().getSession().getStBranch();

        if (cc_code != null && !cc_code.equalsIgnoreCase("null")) {
            if (jenis != null) {
                if (jenis.equalsIgnoreCase("1")) {
                    entity.setStRef2(null);
                    throw new RuntimeException("Grup perusahaan Data Customer Bank hanya diperbolehkan di entry  kantor pusat, hub. bag. underwriting");
                }
            }
        }
    }

    public String getJenisPerusahaan(String stVsCode) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "     ref2 "
                    + "   from "
                    + "         s_valueset "
                    + "   where"
                    + "      vs_group = 'COMP_TYPE' and vs_code = ?");

            S.setParam(1, stVsCode);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {
            S.release();
        }
    }

    public String getGroupPerusahaan(String stVsCode) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "     ref2 "
                    + "   from "
                    + "         s_company_group "
                    + "   where"
                    + "      vs_group = 'COMP_TYPE_GROUP' and vs_code = ?");

            S.setParam(1, stVsCode);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {
            S.release();
        }
    }

    public void doSaveAgent() throws Exception {
        final EntityView cloned = (EntityView) ObjectCloner.deepCopy(entity);

        //validate();

        if (cloned.getStTaxFile() != null) {
            CheckNPWP(cloned.getStTaxFile());
        }

        //if(entity.getStGLCode()==null) makeGLCOde();

        //if(entity.getStGLCode()==null)  throw new RuntimeException("GL CODE tidak boleh kosong, hub. admin jika terjadi error ini");

        getRemoteEntityManager().saveAgent(cloned);

        doSaveTable(cloned.getStEntityID());

        super.close();
    }

    /**
     * @return the list
     */
    public DTOList getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(DTOList list) {
        this.list = list;
    }

    public void addLineClaim() throws Exception {

        FlexTableView ffNew = new FlexTableView();

        ffNew.markNew();

        ffNew.setStFFTGroupID("ENTITY");
        ffNew.setStReference2("AGENT");
        ffNew.setStReference1(entity.getStEntityID());

        list.add(ffNew);
    }

    public void refresh() {
    }

    public void select_approval() throws Exception {
        list = null;

        list = ListUtil.getDTOListFromQuery(
                "   select "
                + "      b.*, a.pol_type_id, a.description "
                + "   from "
                + "      ins_policy_types a "
                + "      inner join ff_table b on b.ref3=cast(a.pol_type_id as varchar) and b.ref1=? and b.ref2='AGENT' and b.fft_group_id='ENTITY' order by a.pol_type_id,b.period_start,ref4 ",
                new Object[]{entity.getStEntityID()},
                FlexTableView.class);

        for (int i = 0; i < list.size(); i++) {
            FlexTableView ft = (FlexTableView) list.get(i);

            ft.setStFFTGroupID("ENTITY");
            ft.setStReference1(entity.getStEntityID());
            ft.setStReference2("AGENT");
            ft.setStReference3(String.valueOf(ft.getAttribute("pol_type_id")));
            ft.setStReference3Desc(String.valueOf(ft.getAttribute("description")));

            if (ft.getStFFTID() != null) {
                ft.markUpdate();// else ft.markNew();
            }
        }
    }

    public void doSaveTable(String entityID) throws Exception {

        for (int i = 0; i < list.size(); i++) {
            FlexTableView flexTableView = (FlexTableView) list.get(i);

            if (flexTableView.isNew()) {
                if (flexTableView.getDbReference1() != null) {
                    flexTableView.setStFFTID(String.valueOf(IDFactory.createNumericID("FFT_ID")));
                    flexTableView.setStReference1(entityID);
                } else {
                    flexTableView.markUnmodified();
                }
            }
        }

        SQLUtil.qstore(list);

        close();
    }

    public void onChangeBranchGroup() {
    }
}
