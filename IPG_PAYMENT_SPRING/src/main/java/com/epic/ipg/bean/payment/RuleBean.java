/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epic.ipg.bean.payment;

/**
 *
 * @author saminda
 */
public class RuleBean {


    private int ruleID = -1;
    private String ruleScope = null;
    private String operator = null;
    private String ruleType = null;
    private String startValue = null;
    private String endValue = null;
    private String trigerSeqID = null;
    private String securityLevel=null;

    
    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getRuleID() {
        return ruleID;
    }

    public void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

    public String getRuleScope() {
        return ruleScope;
    }

    public void setRuleScope(String ruleScope) {
        this.ruleScope = ruleScope;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getTrigerSeqID() {
        return trigerSeqID;
    }

    public void setTrigerSeqID(String trigerSeqID) {
        this.trigerSeqID = trigerSeqID;
    }

    /**
     * @return the securityLevel
     */
    public String getSecurityLevel() {
        return securityLevel;
    }

    /**
     * @param securityLevel the securityLevel to set
     */
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }



}
