package com.epic.ipg.bean.payment;

public class MerchantResponseBean {

    private String primaryURL;
    private String secondaryURL;
    private String accountNo;
    private String merchantID;
    private String dinamicReturnSuccessURL;
    private String dinamicReturnErrorURL;
    private String txnDateTime;
    private String amount;
    private String currencyCode;

    public MerchantResponseBean() {
        this.primaryURL = "--";
        this.secondaryURL = "--";
        this.accountNo = "--";
        this.merchantID = "--";
        this.dinamicReturnSuccessURL = "--";
        this.dinamicReturnErrorURL = "--";
        this.txnDateTime = "--";
        this.amount = "--";
        this.currencyCode = "--";
    }

    public String getPrimaryURL() {
        return primaryURL;
    }

    public void setPrimaryURL(String primaryURL) {
        this.primaryURL = primaryURL;
    }

    public String getSecondaryURL() {
        return secondaryURL;
    }

    public void setSecondaryURL(String secondaryURL) {
        this.secondaryURL = secondaryURL;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getDinamicReturnSuccessURL() {
        return dinamicReturnSuccessURL;
    }

    public void setDinamicReturnSuccessURL(String dinamicReturnSuccessURL) {
        this.dinamicReturnSuccessURL = dinamicReturnSuccessURL;
    }

    public String getDinamicReturnErrorURL() {
        return dinamicReturnErrorURL;
    }

    public void setDinamicReturnErrorURL(String dinamicReturnErrorURL) {
        this.dinamicReturnErrorURL = dinamicReturnErrorURL;
    }

    public String getTxnDateTime() {
        return txnDateTime;
    }

    public void setTxnDateTime(String txnDateTime) {
        this.txnDateTime = txnDateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
