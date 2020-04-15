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
public class CustomerPaymentRequestBean {

    private String iPGTransactionRequestId;
    private String merchantId;
    private double amount;
    private String customerName;
    private String email;
    private String currency;
    private String currencyCode;
    private String dispalyAmount;

    public String getiPGTransactionRequestId() {
        return iPGTransactionRequestId;
    }

    public void setiPGTransactionRequestId(String iPGTransactionRequestId) {
        this.iPGTransactionRequestId = iPGTransactionRequestId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDispalyAmount() {
        return dispalyAmount;
    }

    public void setDispalyAmount(String dispalyAmount) {
        this.dispalyAmount = dispalyAmount;
    }

}
