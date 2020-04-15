/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.varlist;

/**
 *
 * @author chanuka_g
 */
public class StatusVarList {

    private StatusVarList() {
    }

    public static final String TXN_FAILED = "TRFAI"; //Transaction Failed
    public static final String TXN_INITIATED = "TRINI"; //Transaction Initiated
    public static final String TXN_ENGINEREQUESTSENT = "TRERS"; //Transaction Engine Request Sent
    public static final String TXN_ENGINERESRECEIVED = "TRERR"; //Transaction Engine Response Received
    public static final String TXN_COMPLETED = "TRCMP"; //Transaction Completed
    public static final String TXN_ENGINEREQCREATED = "TRERC"; //Transaction Engine Request Created
//    public static final String TXN_COMPLETE_CONFIRMED = "TRCMC"; //IPG Transaction complete Confirmed
    public static final String TXN_REQUEST_RECEIVED = "TRRRI"; //Transaction Request Recieved
    public static final String TXN_VALIDATED = "TRVAL"; //Transaction Validated
    public static final String TXN_VALIDATIONFAILED = "TRVEF"; //Transaction Validation Failed
    public static final String TXN_ENGINEREQCREATEDFAIL = "TRERF"; //Transaction Engine Request Created Failed
    public static final String TXN_VERIFIREQUESTCREATED = "TRVRC"; //Transaction Verification Request Created
    public static final String TXN_VERIFIREQUESTSENT = "TRVRS"; //Transaction Verification Request Sent
    public static final String TXN_VERIFIREQCREATEDFAIL = "TRVRF"; //Transaction Verification Request Created Failed
    public static final String TXN_VERIFICARIONRESRECEIVED = "TRVRR"; //Transaction Verification Response Recieved
    public static final String TXN_VERFICATION_FAILED = "TRVIF"; //Transaction Verification Failed

    public static final String TXN_RISK_VALIDATE_SUCCESS = "TRRIVS"; //Transaction Risk Validate Success
    public static final String TXN_RISK_VALIDATE_FAILED = "TRRIVF"; //Transaction Risk Validate Failed
    public static final String TXN_RULE_VALIDATE_SUCCESS = "TRRUVS"; //Transaction Rule Validate Success
    public static final String TXN_RULE_VALIDATE_FAILED = "TRRUVF"; //Transaction Rule Validate Failed

    public static final String BATCH_OPEN_STATUS = "BAOPE"; //Number of Attempts.

}
