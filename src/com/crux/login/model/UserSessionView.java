/***********************************************************************
 * Module:  com.crux.login.model.UserSessionView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.model.UserSession;
import com.crux.file.FileView;
import com.webfin.gl.model.GLCostCenterView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.insurance.ejb.UserManager;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.system.region.model.RegionView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

public class UserSessionView extends DTO implements UserSession, RecordAudit
{
    
    //UserDataMgr userData;
    
    private String stUserID;
    private String stUserType;
    private String stUserName;
    private String stDivision;
    private String stDepartment;
    private String stPhone;
    private String stContactNum;
    private String stEmail;
    private String stPasswd;
    private String stNewPasswd;
    private Date dtActiveDate;
    private Date dtInActiveDate;
    private Date dtTransactionDate;
    private Date dtLastLogin;
    private Date dtLoginSince;
    private String stMobileNumber;
    private DTOList userroles;
    private String stTempPassword;
    private String stBranch;
    private Long lgFailedLogin;
    private Date dtFailedLoginDate;
    private String stBranchName;
    private HashMap attributes = new HashMap();
    private UserLogView userlog;
    private DTOList userroles2;
    private String stRegion;
    
    private String stShortName;
    
    private String stSign;
    private String stJobPosition;
    private String stParaf;
    private String stSignAuthority;
    private String stEntryBackdateFlag;
    private String stDeleteFlag;
    private String stReferenceUserID;
    private String stLetterOfAuthority;
    private String stSessionTimeout;
    private String stMarketerID;
    private Date dtLastPasswordChange;
    private String stTandatangan;
    private String stDivisionID;
    private String stCostCenterCodeSource;
    private String stRegionIDSource;
    private String stBranchSourceName;

    public String getStBranchSourceName() {
        return stBranchSourceName;
    }

    public void setStBranchSourceName(String stBranchSourceName) {
        this.stBranchSourceName = stBranchSourceName;
    }

    public String getStCostCenterCodeSource() {
        return stCostCenterCodeSource;
    }

    public void setStCostCenterCodeSource(String stCostCenterCodeSource) {
        this.stCostCenterCodeSource = stCostCenterCodeSource;
    }

    public String getStRegionIDSource() {
        return stRegionIDSource;
    }

    public void setStRegionIDSource(String stRegionIDSource) {
        this.stRegionIDSource = stRegionIDSource;
    }

    public String getStTandatangan() {
        return stTandatangan;
    }

    public void setStTandatangan(String stTandatangan) {
        this.stTandatangan = stTandatangan;
    }
    
    public String getStNewPasswd()
    {
        return stNewPasswd;
    }
    
    public void setStNewPasswd(String stNewPasswd)
    {
        this.stNewPasswd = stNewPasswd;
    }
    
    public UserLogView getUserlog()
    {
        return userlog;
    }
    
    public void setUserlog(UserLogView userlog)
    {
        this.userlog = userlog;
    }
    
    public String getStBranchName()
    {
        return stBranchName;
    }
    
    public void setStBranchName(String stBranchName)
    {
        this.stBranchName = stBranchName;
    }
    
    public Date getDtFailedLoginDate()
    {
        return dtFailedLoginDate;
    }
    
    public void setDtFailedLoginDate(Date dtFailedLoginDate)
    {
        this.dtFailedLoginDate = dtFailedLoginDate;
    }
    
    public String getStBranch()
    {
        return stBranch;
    }
    
    public void setStBranch(String stBranch)
    {
        this.stBranch = stBranch;
    }
    
    public Long getLgFailedLogin()
    {
        return lgFailedLogin;
    }
    
    public void setLgFailedLogin(Long lgFailedLogin)
    {
        this.lgFailedLogin = lgFailedLogin;
    }
    
    public String getStMobileNumber()
    {
        return stMobileNumber;
    }
    
    public void setStMobileNumber(String stMobileNumber)
    {
        this.stMobileNumber = stMobileNumber;
    }
    
    public void setAttribute(String x, Object o)
    {
        attributes.put(x,o);
    }
    
    public Object getAttribute(String x)
    {
        return attributes.get(x);
    }
    
    private transient DTOList resources;
    
    public transient static String comboFields[] = {"user_id", "user_name", "division", "branch_name", "email_address"};
    
    public static String tableName = "s_users";
    
    public transient static String fieldMap[][] = {
        {"stUserID", "user_id*pk"},
        {"stUserName", "user_name"},
        {"stEmail", "email_address"},
        {"stDivision", "division"},
        {"stDepartment", "department"},
        {"stPhone", "phone"},
        {"stContactNum", "contact_number"},
        {"stPasswd", "password"},
        {"dtActiveDate", "active_date"},
        {"dtInActiveDate", "inactive_date"},
        {"dtLastLogin", "last_login"},
        {"userroles", "userroles*n"},
        {"stMobileNumber", "mobile_number"},
        {"stBranch", "branch"},
        {"lgFailedLogin", "failed_login"},
        {"stBranchName","branch_name*n"},
        {"dtFailedLoginDate", "FAILED_LOGIN_DATE"},
        {"stShortName", "shortname"},
        {"stRegion", "region"},
        {"stSign", "sign"},
        {"stJobPosition", "job_position"},
        {"stParaf", "paraf"},
        {"stSignAuthority", "sign_authority"},
        {"stEntryBackdateFlag", "backdate_entry_f"},
        {"stDeleteFlag", "delete_flag"},
        {"stReferenceUserID", "ref_user_id"},
        {"stEntryBackdateFlag", "backdate_entry_f"},
        {"stLetterOfAuthority", "letter_of_authority"},
        {"stSessionTimeout", "session_timeout"},
        {"stMarketerID", "marketer_id"},
        {"dtLastPasswordChange", "last_password_change"},
        {"stTandatangan", "tanda_tangan"},
        {"stDivisionID", "division_id"},
        {"stCostCenterCodeSource", "cc_code_source"},
        {"stRegionIDSource", "region_id_source"},
        {"stBranchSourceName","branch_source_name*n"},

    };
    
    public Date getDtTransactionDate()
    {
        return dtTransactionDate;
    }
    
    public void setDtTransactionDate(Date dtTransactionDate)
    {
        this.dtTransactionDate = dtTransactionDate;
    }
    
    public String getStUserID()
    {
        return stUserID;
    }
    
    public void setStUserID(String stUserID)
    {
        this.stUserID = stUserID;
    }
    
    public String getStUserName()
    {
        return stUserName;
    }
    
    public void setStUserName(String stUserName)
    {
        this.stUserName = stUserName;
    }
    
    public String getStDivision()
    {
        return stDivision;
    }
    
    public void setStDivision(String stDivision)
    {
        this.stDivision = stDivision;
    }
    
    public String getStDepartment()
    {
        return stDepartment;
    }
    
    public void setStDepartment(String stDepartment)
    {
        this.stDepartment = stDepartment;
    }
    
    public String getStPhone()
    {
        return stPhone;
    }
    
    public void setStPhone(String stPhone)
    {
        this.stPhone = stPhone;
    }
    
    public String getStContactNum()
    {
        return stContactNum;
    }
    
    public void setStContactNum(String stContactNum)
    {
        this.stContactNum = stContactNum;
    }
    
    public String getStEmail()
    {
        return stEmail;
    }
    
    public void setStEmail(String stEmail)
    {
        this.stEmail = stEmail;
    }
    
    public String getStPasswd()
    {
        return stPasswd;
    }
    
    public void setStPasswd(String stPasswd)
    {
        this.stPasswd = stPasswd;
    }
    
    public Date getDtActiveDate()
    {
        return dtActiveDate;
    }
    
    public void setDtActiveDate(Date dtActiveDate)
    {
        this.dtActiveDate = dtActiveDate;
    }
    
    public Date getDtInActiveDate()
    {
        return dtInActiveDate;
    }
    
    public void setDtInActiveDate(Date dtInActiveDate)
    {
        this.dtInActiveDate = dtInActiveDate;
    }
    
    
    public Date getDtLastLogin()
    {
        return dtLastLogin;
    }
    
    public void setDtLastLogin(Date dtLastLogin)
    {
        this.dtLastLogin = dtLastLogin;
    }
    
    public DTOList getUserroles()
    {
        return userroles;
    }
    
    public void setUserroles(DTOList userroles)
    {
        this.userroles = userroles;
    }
    
    public void setStUserType(String stUserType)
    {
        this.stUserType = stUserType;
    }
    
    public String getStUserType()
    {
        return stUserType;
    }
    
    public DTOList getResources()
    {
        return resources;
    }
    
    public void setResources(DTOList resources)
    {
        this.resources = resources;
    }
    
    public String toString()
    {
        return "[UV:User="+stUserID+",DT="+dtTransactionDate+"]";
    }
    
    public void setStTempPassword(String stTempPassword)
    {
        this.stTempPassword =  stTempPassword;
    }
    public String getStTempPassword()
    {
        return stTempPassword;
    }
    public boolean isAdmin()
    {
        return "admin".equalsIgnoreCase(stUserID);
    }
    
    public String setStVendorID(String vendorId)
    {
        return null;
    }
    
    public boolean hasResource(String resourceid)
    {
        if (resourceid==null)  return true;
        
        for (int i = 0; i < resources.size(); i++)
        {
            FunctionsView functionsView = (FunctionsView) resources.get(i);
            
            if (resourceid.equalsIgnoreCase(functionsView.getStResourceID()))
                return true;
        }
        
        return false;
    }
    
    public Date getDtLoginSince()
    {
        return dtLoginSince;
    }
    
    public void setDtLoginSince(Date dtLoginSince)
    {
        this.dtLoginSince = dtLoginSince;
    }
    
    public boolean isOREdit()
    {
        getUserRoles2();
        boolean yes = false;
        for(int i=0;i<userroles2.size();i++)
        {
            UserRoleView role = (UserRoleView) userroles2.get(i);
            
            if(role.getStRoleID().equalsIgnoreCase("OREDIT"))
            {
                yes = true;
                break;
            }
        }
        return yes;
    }
    
    public void getUserRoles2()
    {
        try
        {
            if (userroles2==null)
            {
                userroles2 =
                        ListUtil.getDTOListFromQuery(
                        "select * from s_user_roles where user_id = ?",
                        new Object [] {stUserID},
                        UserRoleView.class
                        );
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void getUserRoles3(String stUserID)
    {
        try
        {
            if (userroles2==null)
            {
                userroles2 =
                        ListUtil.getDTOListFromQuery(
                        "select a.role_id, b.role_name "+
                        "from s_user_roles a inner join s_roles b on a.role_id = b.role_id "+
                        "where user_id = ? ",
                        new Object [] {stUserID},
                        UserRoleView.class
                        );
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String getStRolesName(String stUserID)
    {
        getUserRoles3(stUserID);
        
        StringBuffer roleName = new StringBuffer();
        String roles = new String();
        
        for(int i=0;i<userroles2.size();i++)
        {
            UserRoleView role = (UserRoleView) userroles2.get(i);
            
            roleName.append(role.getStRoleName());
            if(userroles2.size()>1) roleName.append(",");
        }
        
        return roleName.toString();
    }
    
    public String getCostCenterDesc()
    {
        
        final GLCostCenterView branch = getCostCenter();
        
        if (branch == null) return null;
        
        return branch.getStDescription();
    }
    
    private GLCostCenterView getCostCenter()
    {
        
        final GLCostCenterView branch = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);
        
        return branch;
        
    }
    
    public boolean isAdministrator(String stUserID)
    {
        getUserRoles3(stUserID);
        boolean admin = false;
        
        for(int i=0;i<userroles2.size();i++)
        {
            UserRoleView role = (UserRoleView) userroles2.get(i);
            
            if(role.getStRoleName().equalsIgnoreCase("Administrator"))
                admin = true;
        }
        
        return admin;
    }
    
    public DTOList branch;
    
    public DTOList getBranch()
    {
        if (branch == null) loadBranch();
        return branch;
    }
    
    public void setBranch(DTOList branch)
    {
        this.branch = branch;
    }
    
    public void loadBranch()
    {
        try
        {
            if (branch == null)
            {
                branch = ListUtil.getDTOListFromQuery(
                        " select * "+
                        " from gl_cost_center "+
                        " where valid_date is not null order by cc_code",
                        GLCostCenterView.class
                        );
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public DTOList policy;
    
    public DTOList getPolicy()
    {
        if (policy == null) loadPolicy();
        return policy;
    }
    
    public void setPolicy(DTOList policy)
    {
        this.policy = policy;
    }
    
    public void loadPolicy()
    {
        try
        {
            if (policy == null)
            {
                policy = ListUtil.getDTOListFromQuery(
                        "select a.*, b.group_name "+
                        " from ins_policy_types a "+
                        " left join ins_policy_type_grp b on a.ins_policy_type_grp_id = b.ins_policy_type_grp_id "+
                        " order by pol_type_id ",
                        InsurancePolicyTypeView.class
                        );
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String getStShortName()
    {
        return stShortName;
    }
    
    public void setStShortName(String stShortName)
    {
        this.stShortName = stShortName;
    }
    
    public String getIPAddress()
    {
        Collection sessions = SessionKeeper.getInstance().getSessions();
        
        Iterator it = sessions.iterator();

        while (it.hasNext())
        {
            HttpSession s = (HttpSession) it.next();
            
            UserSessionView usx = (UserSessionView) s.getAttribute("USER_SESSION");
            
            if (usx==null) continue;
            
            boolean sameUser = (Tools.isEqual(usx.getStUserID(), stUserID));

            if (sameUser) return (String) s.getAttribute("HOST");
        }
        
        return "";
    }

    public String getStRegion() {
        return stRegion;
    }

    public void setStRegion(String stRegion) {
        this.stRegion = stRegion;
    }
    
    public String getStSign() {
        return stSign;
    }

    public void setStSign(String stSign) {
        this.stSign = stSign;
    }
    
    private RegionView getRegion()
    {
        
        final RegionView region = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegion);
        
        return region;
        
    }
    
    public String getRegionDesc()
    {
        
        final RegionView region = getRegion();
        
        if (region == null) return null;
        
        return region.getStDescription();
    }

    public FileView getFile()
    {

        final FileView file = (FileView) DTOPool.getInstance().getDTO(FileView.class, stSign);

        return file;

    }

    /**
     * @return the stJobPosition
     */
    public String getStJobPosition() {
        return stJobPosition;
    }

    /**
     * @param stJobPosition the stJobPosition to set
     */
    public void setStJobPosition(String stJobPosition) {
        this.stJobPosition = stJobPosition;
    }

    /**
     * @return the stParaf
     */
    public String getStParaf() {
        return stParaf;
    }

    /**
     * @param stParaf the stParaf to set
     */
    public void setStParaf(String stParaf) {
        this.stParaf = stParaf;
    }

    /**
     * @return the stSignAuthority
     */
    public String getStSignAuthority() {
        return stSignAuthority;
    }

    /**
     * @param stSignAuthority the stSignAuthority to set
     */
    public void setStSignAuthority(String stSignAuthority) {
        this.stSignAuthority = stSignAuthority;
    }

    public boolean hasSignAuthority(){
        return Tools.isYes(stSignAuthority);
    }

    public FileView getParafFile()
    {

        final FileView file = (FileView) DTOPool.getInstance().getDTO(FileView.class, stParaf);

        return file;

    }

    /**
     * @return the stEntryBackdateFlag
     */
    public String getStEntryBackdateFlag() {
        return stEntryBackdateFlag;
    }

    /**
     * @param stEntryBackdateFlag the stEntryBackdateFlag to set
     */
    public void setStEntryBackdateFlag(String stEntryBackdateFlag) {
        this.stEntryBackdateFlag = stEntryBackdateFlag;
    }

    public boolean canEntryBackdate(){
        return Tools.isYes(stEntryBackdateFlag);
    }

    /**
     * @return the stDeleteFlag
     */
    public String getStDeleteFlag() {
        return stDeleteFlag;
    }

    /**
     * @param stDeleteFlag the stDeleteFlag to set
     */
    public void setStDeleteFlag(String stDeleteFlag) {
        this.stDeleteFlag = stDeleteFlag;
    }

    /**
     * @return the stReferenceUserID
     */
    public String getStReferenceUserID() {
        return stReferenceUserID;
    }

    /**
     * @param stReferenceUserID the stReferenceUserID to set
     */
    public void setStReferenceUserID(String stReferenceUserID) {
        this.stReferenceUserID = stReferenceUserID;
    }

    public boolean isHeadOfficeUser()
    {
        boolean headOffice = false;

        if(getStBranch()==null) headOffice = true;

        if(getStBranch()!=null)
            if(getStBranch().equalsIgnoreCase("00")) headOffice = true;

        return headOffice;
    }

    /**
     * @return the stLetterOfAuthority
     */
    public String getStLetterOfAuthority() {
        return stLetterOfAuthority;
    }

    /**
     * @param stLetterOfAuthority the stLetterOfAuthority to set
     */
    public void setStLetterOfAuthority(String stLetterOfAuthority) {
        this.stLetterOfAuthority = stLetterOfAuthority;
    }

    /**
     * @return the stSessionTimeout
     */
    public String getStSessionTimeout() {
        return stSessionTimeout;
    }

    /**
     * @param stSessionTimeout the stSessionTimeout to set
     */
    public void setStSessionTimeout(String stSessionTimeout) {
        this.stSessionTimeout = stSessionTimeout;
    }

    /**
     * @return the stMarketerID
     */
    public String getStMarketerID() {
        return stMarketerID;
    }

    /**
     * @param stMarketerID the stMarketerID to set
     */
    public void setStMarketerID(String stMarketerID) {
        this.stMarketerID = stMarketerID;
    }

    /**
     * @return the dtLastPasswordChange
     */
    public Date getDtLastPasswordChange() {
        return dtLastPasswordChange;
    }

    /**
     * @param dtLastPasswordChange the dtLastPasswordChange to set
     */
    public void setDtLastPasswordChange(Date dtLastPasswordChange) {
        this.dtLastPasswordChange = dtLastPasswordChange;
    }

    public String getStDivisionID() {
        return stDivisionID;
    }

    /**
     * @param stDivisionID the stDivisionID to set
     */
    public void setStDivisionID(String stDivisionID) {
        this.stDivisionID = stDivisionID;
    }
    
}
