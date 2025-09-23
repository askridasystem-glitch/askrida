/**
 * Created by IntelliJ IDEA.
 * User: Andi Adhyaksa
 * Date: Nov 22, 2004
 * Time: 3:56:20 PM
 * To change this template use Options | File Templates.
 */
package com.crux.valueset.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class ValueSetView extends DTO implements RecordAudit{

    private String stVCode;
    private String stVGroup;
    private String stVDesc;
    private Long lgVOrder;
    private String stDivision;

    public static String tableName = "s_valueset";

    public static String[][] fieldMap = {
        {"stVCode","vs_code*pk"},
        {"stVGroup","vs_group"},
        {"stVDesc","vs_description"},
        {"lgVOrder","VS_ORDER"},
        {"stDivision","division"},
    };


    public void setStVCode(String stVCode){
        this.stVCode = stVCode;
    }
    public String getStVCode(){
        return stVCode;
    }
    public void setStVGroup(String stVGroup){
        this.stVGroup = stVGroup;
    }
    public String getStVGroup(){
        return stVGroup;
    }
    public void setStVDesc(String stVDesc){
        this.stVDesc = stVDesc;
    }
    public String getStVDesc(){
        return stVDesc;
    }
    public void setLgVOrder(Long lgVOrder){
        this.lgVOrder = lgVOrder;
    }
    public Long getLgVOrder(){
        return lgVOrder;
    }

    /**
     * @return the stDivision
     */
    public String getStDivision() {
        return stDivision;
    }

    /**
     * @param stDivision the stDivision to set
     */
    public void setStDivision(String stDivision) {
        this.stDivision = stDivision;
    }

}
