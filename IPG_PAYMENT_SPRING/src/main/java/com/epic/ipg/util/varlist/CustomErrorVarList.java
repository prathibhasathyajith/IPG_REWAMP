/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.varlist;

/**
 *
 * @author chanuka_g
 */
public class CustomErrorVarList {

    private CustomErrorVarList() {
    }

    public static final String ALLREADY_ADD = "Record already exists";
    public static final String INTE_CONST = "Parent key not found";
    public static final String INTE_CHILD = "Cannot delete. Record already in use.";
    public static final String INV_DATETIME = "Invalid Date";
    public static final String INV_TOTAL_AMOUNT = "Invalid Total Amount";
    public static final String INV_TOTAL_COUNT = "Invalid Total Count";
    public static final String ERROR_MESSAGE_CREATION = "Message Creation Error";
    public static final String DATA_ACCESS_ERROR = "Data Access Error";
    public static final String SIGN_VERIFY_ERROR = "Signature Verification Error";
    public static final String XML_DOM_ERROR = "XML Reading Error";
    public static final String RANDOM_STRING_ERROR = "Random String Error";
    public static final String SERVICE_CHARGE_ERROR = "Service Charge Error";
    public static final String RSA_ENCRYPT_ERROR = "RSA Encryption Error";
    public static final String DES_ENCRYPT_ERROR = "DES Encryption Error";
    public static final String RSA_DECRYPT_ERROR = "RSA Decryption Error";
    public static final String DES_DECRYPT_ERROR = "DES Decryption Error";
    public static final String SIGN_ERROR = "Signing Error";
    public static final String PUBLIC_KEYSTORE_ERROR = "Public Key Retrieve Error";
    public static final String TRANSACTION_DATA_SET_ERROR = "Transaction Data setting Error";
    public static final String VALIDATE_EXPIRY_ERROR = "Validate Expiry Error";
    public static final String VALIDATE_INPUT_DATA = "Validate Input Data Error";
    public static final String VALIDATE_RISK_ERROR = "Validate Risk Error";
    public static final String VALIDATE_MERCHANT_ERROR = "Validate Merchant Error";
    public static final String SWITCH_CONNECTING_ERROR = "Connection To the Switch Error";
    public static final String SWITCH_SENDING_ERROR = "Sending Message To the Switch Error";
    public static final String SWITCH_RECEIVE_ERROR = "Receiving Message From the Switch Error";
    public static final String SWITCH_CLOSE_ERROR = "Socket Connection to Switch Error";

    public static final String DATA_TIME_ERROR = "Date Time Error";
    public static final String TXN_ID_ERROR = "Transaction ID Generation Error";
    public static final String ROUND_ERROR = "Round up Error";

    public static String getMessege(String tmpErrorMsg) {

        String message = null;

        if (tmpErrorMsg.indexOf("ORA-00001") != -1) {

            message = ALLREADY_ADD;

        } else if (tmpErrorMsg.indexOf("ORA-02292") != -1) {
            message = INTE_CHILD;

        } else if (tmpErrorMsg.indexOf("ORA-02291") != -1) {
            message = INTE_CONST;

        } else if (tmpErrorMsg.indexOf("ORA-01830") != -1) {
            message = INV_DATETIME;

        }

        return message;
    }
}
