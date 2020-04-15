/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

/**
 *
 * @author mahesh_m
 */
public class CommonConfigurations {
    
    private String mpiServerIp;
    private String mpiServerPort;
    private String switchIp;
    private String switchPort;
    private String ipgEngineUrl;
    private String mpiServerUrl;
    private String acquirerbin;
    private String keystorepassword;

    public String getAcquirerbin() {
        return acquirerbin;
    }

    public void setAcquirerbin(String acquirerbin) {
        this.acquirerbin = acquirerbin;
    }

    public String getMpiServerUrl() {
        return mpiServerUrl;
    }

    public void setMpiServerUrl(String mpiServerUrl) {
        this.mpiServerUrl = mpiServerUrl;
    }

    public String getIpgEngineUrl() {
        return ipgEngineUrl;
    }

    public void setIpgEngineUrl(String ipgEngineUrl) {
        this.ipgEngineUrl = ipgEngineUrl;
    }

    public String getMpiServerIp() {
        return mpiServerIp;
    }

    public void setMpiServerIp(String mpiServerIp) {
        this.mpiServerIp = mpiServerIp;
    }

    public String getMpiServerPort() {
        return mpiServerPort;
    }

    public void setMpiServerPort(String mpiServerPort) {
        this.mpiServerPort = mpiServerPort;
    }

    public String getSwitchIp() {
        return switchIp;
    }

    public void setSwitchIp(String switchIp) {
        this.switchIp = switchIp;
    }

    public String getSwitchPort() {
        return switchPort;
    }

    public void setSwitchPort(String switchPort) {
        this.switchPort = switchPort;
    }

    public String getKeystorepassword() {
        return keystorepassword;
    }

    public void setKeystorepassword(String keystorepassword) {
        this.keystorepassword = keystorepassword;
    }

}
