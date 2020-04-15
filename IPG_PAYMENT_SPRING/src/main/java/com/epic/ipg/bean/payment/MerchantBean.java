/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author chanuka_g
 */

public class MerchantBean implements Serializable{

    private String merchantId;
    private String merchantName;
    private int addressId;
    private String primaryUrl;
    private String secondaryUrl;
    private Date dateOfRegistry;
    private Date dateOfExpiry;
    private String dateOfRegistryText;
    private String dateOfExpiryText;
    private String statusCode;
    private String contactPerson;
    private String remarks;
    private String countryCode;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;
    private Date createdTime;
    private String address;
    private byte[] privateKey;
    private byte[] publicKey;
    private String securityMechanism;
    private byte[] symmetricKey;
    private String dynamicReturnSuccessUrl;
    private String dynamicReturnErrorUrl;

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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPrimaryUrl() {
        return primaryUrl;
    }

    public void setPrimaryUrl(String primaryUrl) {
        this.primaryUrl = primaryUrl;
    }

    public String getSecondaryUrl() {
        return secondaryUrl;
    }

    public void setSecondaryUrl(String secondaryUrl) {
        this.secondaryUrl = secondaryUrl;
    }

    public Date getDateOfRegistry() {
        return dateOfRegistry;
    }

    public void setDateOfRegistry(Date dateOfRegistry) {
        this.dateOfRegistry = dateOfRegistry;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getDateOfRegistryText() {
        return dateOfRegistryText;
    }

    public void setDateOfRegistryText(String dateOfRegistryText) {
        this.dateOfRegistryText = dateOfRegistryText;
    }

    public String getDateOfExpiryText() {
        return dateOfExpiryText;
    }

    public void setDateOfExpiryText(String dateOfExpiryText) {
        this.dateOfExpiryText = dateOfExpiryText;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecurityMechanism() {
        return securityMechanism;
    }

    public void setSecurityMechanism(String securityMechanism) {
        this.securityMechanism = securityMechanism;
    }

    public byte[] getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(byte[] symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    public String getDynamicReturnSuccessUrl() {
        return dynamicReturnSuccessUrl;
    }

    public void setDynamicReturnSuccessUrl(String dynamicReturnSuccessUrl) {
        this.dynamicReturnSuccessUrl = dynamicReturnSuccessUrl;
    }

    public String getDynamicReturnErrorUrl() {
        return dynamicReturnErrorUrl;
    }

    public void setDynamicReturnErrorUrl(String dynamicReturnErrorUrl) {
        this.dynamicReturnErrorUrl = dynamicReturnErrorUrl;
    }


}
