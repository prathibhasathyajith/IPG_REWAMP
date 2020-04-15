/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.varlist;

/**
 *
 * @author dilanka_w
 */
public class CommonVarList {

    private CommonVarList() {
    }

    public static final String SWITCH_RESPONSE_SUCCESS = "00";
    public static final String SWITCH_RESPONSE_FAIL = "SF";
    public static final String USERMAP = "USERMAP";
    public static final String IPGRULE_IPG_LEVEL = "IPG";
    public static final String IPGRULE_MERCHANT_LEVEL = "MER";
    public static final String NEWLINE = "\n";

    //common configurations
    public static final String MPISERVERIP = "MPISERVERIP";
    public static final String MPISERVERPORT = "MPISERVERPORT";
    public static final String SWITCHIP = "SWITCHIP";
    public static final String SWITCHPORT = "SWITCHPORT";
    public static final String IPGENGINEURL = "IPGENGINEURL";
    public static final String MPISERVERURL = "MPISERVERURL";
    public static final String RECORDID = "RECORDID";
    public static final String KEYSTOREPASS = "KEYSTOREPASS";
    public static final String KEYSTOREGENSTATUS = "KEYSTOREGENSTATUS";
    public static final String ACQUIRERBIN = "ACQUIRERBIN";
    public static final String EMAILHOST = "EMAILHOST";
    public static final String EMAILPORT = "EMAILPORT";
    public static final String EMAILFROMUSER = "EMAILFROMUSER";
    public static final String EMAILPASS = "EMAILPASS";
    public static final String EMAILSTATUS = "EMAILSTATUS";

    // xml elements
    public static final String MESSAGE = "MESSAGE";
    public static final String MPIERROR = "MPIERROR";
    public static final String TRANSACTIONID = "TRANSACTIONID";
    public static final String TXNSTATUS = "TXNSTATUS";
    public static final String CAVV = "CAVV";
    public static final String CVV = "CVV";
    public static final String ECI = "ECI";
    public static final String IURL = "IURL";
    public static final String MERCHANT = "MERCHANT";
    public static final String MERID = "MERID";
    public static final String PASSWORD = "PASSWORD";
    public static final String AQUBIN = "AQUBIN";
    public static final String NAME = "NAME";
    public static final String COUNTRY = "COUNTRY";
    public static final String MURL = "MURL";
    public static final String CARDASSOCIATION = "CARDASSOCIATION";
    public static final String BROWSER = "BROWSER";
    public static final String DEVICECATEGORY = "DEVICECATEGORY";
    public static final String ACCEPTHEADER = "ACCEPTHEADER";
    public static final String USERAGENT = "USERAGENT";
    public static final String CARDHOLDER = "CARDHOLDER";
    public static final String CARDHOLDERPAN = "CARDHOLDERPAN";
    public static final String CARDNAME = "CARDNAME";
    public static final String CARDEXPIRY = "CARDEXPIRY";
    public static final String PURCHASE = "PURCHASE";
    public static final String MERCHANTREFNO = "MERCHANTREFNO";
    public static final String DATE = "DATE";
    public static final String AMOUNT = "AMOUNT";
    public static final String CURRENCY = "CURRENCY";
    public static final String EXPONENT = "EXPONENT";
    public static final String DESC = "DESC";
    public static final String RECUR = "RECUR";
    public static final String FREQUENCY = "FREQUENCY";
    public static final String ENDRECUR = "ENDRECUR";
    public static final String ERRORCODE = "ERRORCODE";
    public static final String ERRORMESSAGE = "ERRORMESSAGE";

    // Loading servlet pages 
    public static final String LOADING_PAGE_SVG = ""
            + "<html>"
            + "<body style='background:white; margin: 0 auto; text-align: center;display: table;height:100%'>"
            + "<div style='display: table-cell; vertical-align:middle'>"
            + "<img src='resources/img/new/load3.svg' width='100' height='auto'/>"
            + "</div>"
            + "</body>"
            + "</html>";

    public static final String LOADING_PAGE_CSS = ""
            + "<head>"
            + "<style>"
            + ".loadinit {width: 0px;height: 0px;padding: 5px;border-radius: 10px;float: left;margin: 5px;}"
            + "#loadcircle1 {background-color: #673AB7;animation: circle1 0.5s ease 0s infinite alternate;}"
            + "#loadcircle2 {background-color: #3F51B5;animation: circle1 0.5s ease 0.2s infinite alternate;}"
            + "#loadcircle3 {background-color: #2196F3;animation: circle1 0.5s ease 0.4s infinite alternate;}"
            + "#loadcircle4 {background-color: #00BCD4;animation: circle1 0.5s ease 0.6s infinite alternate;}"
            + "@keyframes circle1 {from {padding: 5px;margin: 5px;}to {padding: 0px;margin: 10px;}}"
            + "</style>"
            + "</head>"
            + "<body style='background:white; margin: 0 auto; text-align: center;display: table;height:100%'>"
            + "<div style='display: table-cell; vertical-align:middle'>"
            + "<div id='loadcircle1' class='loadinit'></div>"
            + "<div id='loadcircle2' class='loadinit'></div>"
            + "<div id='loadcircle3' class='loadinit'></div>"
            + "<div id='loadcircle4' class='loadinit'></div>"
            + "</div>"
            + "</body>"
            + "</html>";

    public static final String EMAIL_SUBJECT = "Payment Receipt";

    public static final String TDESENCRYPT_KEY = "690DA4AD5BAF642DEE34818C21EAFC70690DA4AD5BAF642D";

    public static final String TRANSACTIONTYPE_SALE = "01";
    public static final String TRANSACTIONTYPE_PAYMENTREQUEST = "07";
    
    public static final String RESPONCECODE_SUCESS = "00";

}
