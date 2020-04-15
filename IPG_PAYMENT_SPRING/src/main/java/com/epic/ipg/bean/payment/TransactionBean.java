/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author lakitha
 */
public class TransactionBean implements Serializable{

    private String iPGTransactionId;
    private String merchantType;
    private String terminalId;
    private String merchantId;
    private String merchantRemarks;
    private String cardAssociationCode;
    private String cardHolderPan;
    private String currencyCode;
    private String countryCode;
    private double amount;
    private Timestamp purchasedDateTime;
    private String cardName;
    private String batchnumber;
    private String expiryDate;
    private String cvcNumber;
    private Timestamp transactionCreatedDateTime;
    private String statusCode;
    private String lastUpdatedUser;
    private Timestamp lastUpdatedTime;
    private Timestamp createdTime;
    private String merchantName;
    private String clientIp;
    private String orderId;
    private int id;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardAssociationCode() {
        return cardAssociationCode;
    }

    public void setCardAssociationCode(String cardAssociationCode) {
        this.cardAssociationCode = cardAssociationCode;
    }

    public String getCardHolderPan() {
        return cardHolderPan;
    }

    public void setCardHolderPan(String cardHolderPan) {
        this.cardHolderPan = cardHolderPan;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCvcNumber() {
        return cvcNumber;
    }

    public void setCvcNumber(String cvcNumber) {
        this.cvcNumber = cvcNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getiPGTransactionId() {
        return iPGTransactionId;
    }

    public void setiPGTransactionId(String iPGTransactionId) {
        this.iPGTransactionId = iPGTransactionId;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Timestamp getTransactionCreatedDateTime() {
        return transactionCreatedDateTime;
    }

    public void setTransactionCreatedDateTime(Timestamp transactionCreatedDateTime) {
        this.transactionCreatedDateTime = transactionCreatedDateTime;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantRemarks() {
        return merchantRemarks;
    }

    public void setMerchantRemarks(String merchantRemarks) {
        this.merchantRemarks = merchantRemarks;
    }

    public Timestamp getPurchasedDateTime() {
        return purchasedDateTime;
    }

    public void setPurchasedDateTime(Timestamp purchasedDateTime) {
        this.purchasedDateTime = purchasedDateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
