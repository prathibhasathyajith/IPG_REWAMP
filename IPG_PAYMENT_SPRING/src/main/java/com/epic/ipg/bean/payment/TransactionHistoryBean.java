/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epic.ipg.bean.payment;

import java.sql.Timestamp;

/**
 * 
 * @author chanuka_g
 */
public class TransactionHistoryBean {

    private int transactionHistoryId;
    private String transactionId;
    private String status;
    private String remarks;
    private String lastUpdatedUser;
    private Timestamp lastUpdatedTime;
    private Timestamp createdTime;

    public int getTransactionHistoryId() {
        return transactionHistoryId;
    }

    public void setTransactionHistoryId(int transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

}
