/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

import java.util.Date;

/**
 *
 * @author chanuka_g
 */
public class MerchantSendToMpiBean {

    private String iPGTransactionId;
    private String merchantId;
    private String merchantType;
    private String terminalId;
    private String cardAssociationCode;
    private String cardHolderPan;
    private String currencyCode;
    private long amount;
    private String purchasedDateTime;
    private String cardName;
    private String expiryDate;
    private String cvcNumber;
    private String cvvNumber;
    private String cidNumber;
    private Date transactionCreatedDateTime;
    private String batchnumber;
    private String transactionCode;

    private String statusCode;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;
    private Date createdTime;
    private String merchantName;
    private int addressId;
    private String primaryUrl;
    private String secondaryUrl;
    private String dynamicreturnErrorUrl;
    private Date dateOfRegistry;
    private Date dateOfExpiry;
    private String contactPerson;
    private String merchantRemarks;
    private String countryCode;
    private String password;

    private String cardHolderEmail;
    private String cardHolderMobile;
    private String cardHolderAddress;
    private String cardHolderCity;

    private String aquBin = null;
    private String merchantUrl = null;
    private String deviceCategory = null;
    private String acceptHeader = null;
    private String userAgent = null;
    private String merchantRefNo = null;
    private String exponent = null;
    private String description = null;
    private String frequency = null;
    private String endRecur = null;
    private String clientIp = null;

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public Date getDateOfRegistry() {
        return dateOfRegistry;
    }

    public void setDateOfRegistry(Date dateOfRegistry) {
        this.dateOfRegistry = dateOfRegistry;
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

    public String getMerchantRemarks() {
        return merchantRemarks;
    }

    public void setMerchantRemarks(String merchantRemarks) {
        this.merchantRemarks = merchantRemarks;
    }

    public void setiPGTransactionId(String iPGTransactionId) {
        this.iPGTransactionId = iPGTransactionId;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryUrl() {
        return primaryUrl;
    }

    public void setPrimaryUrl(String primaryUrl) {
        this.primaryUrl = primaryUrl;
    }

    public String getPurchasedDateTime() {
        return purchasedDateTime;
    }

    public void setPurchasedDateTime(String purchasedDateTime) {
        this.purchasedDateTime = purchasedDateTime;
    }

    public String getSecondaryUrl() {
        return secondaryUrl;
    }

    public void setSecondaryUrl(String secondaryUrl) {
        this.secondaryUrl = secondaryUrl;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getTransactionCreatedDateTime() {
        return transactionCreatedDateTime;
    }

    public void setTransactionCreatedDateTime(Date transactionCreatedDateTime) {
        this.transactionCreatedDateTime = transactionCreatedDateTime;
    }

    public String getAquBin() {
        return aquBin;
    }

    public void setAquBin(String aquBin) {
        this.aquBin = aquBin;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getMerchantRefNo() {
        return merchantRefNo;
    }

    public void setMerchantRefNo(String merchantRefNo) {
        this.merchantRefNo = merchantRefNo;
    }

    public String getExponent() {
        return exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getEndRecur() {
        return endRecur;
    }

    public void setEndRecur(String endRecur) {
        this.endRecur = endRecur;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCidNumber() {
        return cidNumber;
    }

    public void setCidNumber(String cidNumber) {
        this.cidNumber = cidNumber;
    }

    public String getCvvNumber() {
        return cvvNumber;
    }

    public void setCvvNumber(String cvvNumber) {
        this.cvvNumber = cvvNumber;
    }

    public String getDynamicreturnErrorUrl() {
        return dynamicreturnErrorUrl;
    }

    public void setDynamicreturnErrorUrl(String dynamicreturnErrorUrl) {
        this.dynamicreturnErrorUrl = dynamicreturnErrorUrl;
    }

    public String getCardHolderEmail() {
        return cardHolderEmail;
    }

    public void setCardHolderEmail(String cardHolderEmail) {
        this.cardHolderEmail = cardHolderEmail;
    }

    public String getCardHolderMobile() {
        return cardHolderMobile;
    }

    public void setCardHolderMobile(String cardHolderMobile) {
        this.cardHolderMobile = cardHolderMobile;
    }

    public String getCardHolderAddress() {
        return cardHolderAddress;
    }

    public void setCardHolderAddress(String cardHolderAddress) {
        this.cardHolderAddress = cardHolderAddress;
    }

    public String getCardHolderCity() {
        return cardHolderCity;
    }

    public void setCardHolderCity(String cardHolderCity) {
        this.cardHolderCity = cardHolderCity;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

}
