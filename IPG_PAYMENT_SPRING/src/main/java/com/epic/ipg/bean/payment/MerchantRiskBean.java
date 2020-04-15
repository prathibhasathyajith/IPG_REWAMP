/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

import java.util.Date;
import java.util.List;

/**
 *
 * @author nalin
 */
public class MerchantRiskBean {

    private String riskProfCode;
    private String description;
    private String status;
    private String statusDes;
    private String minSingleTxnLimit;
    private String maxSingleTxnLimit;
    private String maxDailyTxnCount;
    private String maxDailyTxnAmount;
    private String maxPasswordCount;
    private List<String> mcc = null;
    private List<String> country = null;
    private List<String> currency = null;
    private List<String> bin = null;
    private String merCountry;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;
    private Date createDate;

    public List<String> getMcc() {
        return mcc;
    }

    public void setMcc(List<String> mcc) {
        this.mcc = mcc;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    public List<String> getBin() {
        return bin;
    }

    public void setBin(List<String> bin) {
        this.bin = bin;
    }

    public String getMerCountry() {
        return merCountry;
    }

    public void setMerCountry(String merCountry) {
        this.merCountry = merCountry;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getMaxPasswordCount() {
        return maxPasswordCount;
    }

    public void setMaxPasswordCount(String maxPasswordCount) {
        this.maxPasswordCount = maxPasswordCount;
    }

    public String getStatusDes() {
        return statusDes;
    }

    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaxDailyTxnAmount() {
        return maxDailyTxnAmount;
    }

    public void setMaxDailyTxnAmount(String maxDailyTxnAmount) {
        this.maxDailyTxnAmount = maxDailyTxnAmount;
    }

    public String getMaxDailyTxnCount() {
        return maxDailyTxnCount;
    }

    public void setMaxDailyTxnCount(String maxDailyTxnCount) {
        this.maxDailyTxnCount = maxDailyTxnCount;
    }

    public String getMaxSingleTxnLimit() {
        return maxSingleTxnLimit;
    }

    public void setMaxSingleTxnLimit(String maxSingleTxnLimit) {
        this.maxSingleTxnLimit = maxSingleTxnLimit;
    }

    public String getMinSingleTxnLimit() {
        return minSingleTxnLimit;
    }

    public void setMinSingleTxnLimit(String minSingleTxnLimit) {
        this.minSingleTxnLimit = minSingleTxnLimit;
    }

    public String getRiskProfCode() {
        return riskProfCode;
    }

    public void setRiskProfCode(String riskProfCode) {
        this.riskProfCode = riskProfCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
