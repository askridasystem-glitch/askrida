/***********************************************************************
 * Module:  com.crux.ff.FlexTableManager
 * Author:  Denny Mahendra
 * Created: Jun 2, 2007 10:48:12 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.ff;

import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.ff.model.FlexTableView;
import com.webfin.insurance.model.InsurancePolicyTypeView;

public class FlexTableManager {
   private static FlexTableManager staticinstance;

   public static FlexTableManager getInstance() {
      if (staticinstance == null) staticinstance = new FlexTableManager();
      return staticinstance;
   }

   private FlexTableManager() {
   }

   public DTOList getFlexTable(String fftGroupID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ff_table where fft_group_id = ? and active_flag = 'Y' order by orderseq",
                new Object[]{fftGroupID},
                FlexTableView.class);
   }

    public DTOList getFlexTable2(String fftGroupID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ff_table where fft_group_id = ? and active_flag = 'Y' and ref8 is null order by orderseq",
                new Object[]{fftGroupID},
                FlexTableView.class);
    }

    public DTOList getPolicyType() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from ins_policy_types order by pol_type_id",
                    InsurancePolicyTypeView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getFlexTableProd(String fftGroupID) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select sum(b.prod_goal_setting) as refn1,sum(b.prod_rkap) as refn2,"
                + "a.ref1,a.ref2,a.ref4 from ff_table a "
                + "left join (select * from s_goal_setting_rkap where years = '2014') b on a.refid1 = b.s_goal_id "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' "
                + "group by a.ref1,a.ref2,a.ref4,a.orderseq order by a.ref1,a.orderseq ",
                new Object[]{fftGroupID},
                FlexTableView.class);
    }

    public DTOList getFlexTableProd2(String fftGroupID, String fftRef3) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select sum(b.prod_goal_setting) as refn1,sum(b.prod_rkap) as refn2,"
                + "a.ref1,a.ref2,a.ref4 from ff_table a "
                + "left join (select * from s_goal_setting_rkap where years = '2014') b on a.refid1 = b.s_goal_id "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' and a.ref3 = ? "
                + "group by a.ref1,a.ref2,a.ref4,a.orderseq order by a.ref1,a.orderseq ",
                new Object[]{fftGroupID, fftRef3},
                FlexTableView.class);
    }

    public DTOList getFlexTableProd3(String fftGroupID, String fftRef3, String fftRef5) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select sum(b.prod_goal_setting) as refn1,sum(b.prod_rkap) as refn2,"
                + "a.ref1,a.ref2,a.ref4 from ff_table a "
                + "left join (select * from s_goal_setting_rkap where years = '2014') b on a.refid1 = b.s_goal_id "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' and a.ref3 = ?  and a.ref5 = ? "
                + "group by a.ref1,a.ref2,a.ref4,a.orderseq order by a.ref1,a.orderseq ",
                new Object[]{fftGroupID, fftRef3, fftRef5},
                FlexTableView.class);
    }

    public DTOList getFlexTable3(String fftGroupID, String bulan, String tahun) throws Exception {
        return ListUtil.getDTOListFromQuery(
                //"select * from ff_table where fft_group_id = ? and active_flag = 'Y' order by orderseq",
                " select c.accountno as description,b.debit as refn4,a.ref1,ref2,ref3,ref4,ref5,ref6,ref7,refn1,d.konvensional as refn2,d.syariah as refn3 "
                + "from ff_table a "
                + "left join (select * from gl_je_detail_syariah where period_no = ? and fiscal_year = ?) b on a.refid1 = b.accountid "
                + "left join gl_accounts c on c.account_id = b.accountid "
                + "left join (select * from gl_rkap_group where active_flag = 'Y' and years = ?) d on a.ref9 = d.rkap_group_id "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' and (a.ref8 is null or a.ref8 = 'SYA') order by a.orderseq ",
                new Object[]{bulan, tahun, tahun, fftGroupID},
                FlexTableView.class);
    }

    public DTOList getFlexTable4(String fftGroupID, String tahun) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select a.ref1,ref2,ref3,ref4,ref5,ref6,ref7,refid1,refn1,d.konvensional as refn2,d.syariah as refn3 "
                + "from ff_table a "
                + "left join (select * from gl_rkap_group where active_flag = 'Y' and years = ?) d on a.ref9 = d.rkap_group_id "
                + "where a.fft_group_id = ? and a.active_flag = 'Y' and a.ref8 is null order by a.orderseq ",
                new Object[]{tahun, fftGroupID},
                FlexTableView.class);
    }

}
