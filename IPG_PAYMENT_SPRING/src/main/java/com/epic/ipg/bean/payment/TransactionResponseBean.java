/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

/**
 *
 * @author dilanka_w
 */
public class TransactionResponseBean {

    private boolean isValiedResponse;
    private String txnID;
    private String responsecode;
    private String message;

    public TransactionResponseBean() {
    }

    public TransactionResponseBean(boolean isValiedResponse, String txnID) {
        this.isValiedResponse = isValiedResponse;
        this.txnID = txnID;
    }

    public TransactionResponseBean(boolean isValiedResponse, String txnID, String responsecode) {
        this.isValiedResponse = isValiedResponse;
        this.txnID = txnID;
        this.responsecode = responsecode;
    }

    public TransactionResponseBean(boolean isValiedResponse, String txnID, String responsecode, String message) {
        this.isValiedResponse = isValiedResponse;
        this.txnID = txnID;
        this.responsecode = responsecode;
        this.message = message;
    }

    public boolean isIsValiedResponse() {
        return isValiedResponse;
    }

    public void setIsValiedResponse(boolean isValiedResponse) {
        this.isValiedResponse = isValiedResponse;
    }

    public String getTxnID() {
        return txnID;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
