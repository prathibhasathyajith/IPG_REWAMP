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
public class MessageVarList {

    private MessageVarList() {
    }
    public static final String AUTH_SUCCESS = " Merchant authentication success for merchant : ";
    public static final String AUTH_CER_FAILED = "Merchant authentication failed..Invalid digital certificate for merchant : ";
    public static final String AUTH_KEY_FAILED = " Merchant authentication failed..Invalid key for merchant : ";
    public static final String EMPTY_MERCAHNT_ADDON = " Merchant addon can't be empty.";
    public static final String EMPTY_CUSTOMER_REQUEST_ID = "Empty transaction request id";
    public static final String TXN_FAILED = "Transaction failed";
    public static final String TXN_SUCCESS = "Success";

    public static final String END_TXN_FAILED = "Transaction failed";
    public static final String TXN_COMPLETED = "Transaction completed successfully";

    public static final String NUMBER_ERROR_MESSAGE = "Please enter a valid number ";
    public static final String SIZE_ERROR_MESSAGE = "Field size must be ";

    public static final String CHECK_DATA_FIELDS_AGAIN = "Please check again the details you entered. Fields with incorrect details are highlighted in red";

    public static final String MCC_BLOCKED = "MCC is blocked";
    public static final String CURRENCY_BLOCKED = "Currency is blocked";
    public static final String COUNTRY_BLOCKED = "Country is blocked";
    public static final String BIN_BLOCKED = "Card bin is blocked";
    public static final String MAX_TXN_LIMIT_EXCEED = "Maximum single transaction limit exceeded";
    public static final String BELOW_MIN_TXN_LIMIT = "Below minimum single transaction limit";
    public static final String MAX_TOTAL_TXN_LIMIT_EXCEED = "Maximum daily total transaction limit exceeded";
    public static final String MAX_TXN_COUNT_EXCEED = "Maximum daily transaction count exceeded";

    public static final String COMMON_ALREADY_EXISTS = "Record already exists";
    public static final String COMMON_ERROR_INSERT = "Error occurred while inserting";

    //ipg rule validation messages
    public static final String INVALID_CARD_RANGE = "Invalid card range rule Transaction ID is: ";
    public static final String INVALID_BIN_RANGE = "Invalid bin range rule Transaction ID is: ";
    public static final String BLACKLISTED_CARDHOLDER = "Blacklisted card holder rule Transaction ID is: ";
    public static final String BLACKLISTED_REQUEST_URL = "Blacklisted request url rule Transaction ID is: ";
    public static final String CARDHOLDER_EXEEDED_MAX_TXN_AMOUNT = "Card holder exeeded maximum transaction amount Transaction ID is: ";
    public static final String CARDHOLDER_INVALID_TXN_AMOUNT = "Card holder invalied maximum transaction amount Transaction ID is: ";
    public static final String CARDHOLDER_LESS_MIN_TXN_AMOUNT = "Card holder less than minimum transaction amount Transaction ID is: ";
    public static final String CARDHOLDER_INVALID_TXN_COUNT = "Card holder invalied maximum transaction count Transaction ID is: ";
    public static final String CARDHOLDER_EXEEDED_MAX_TXN_COUNT = "Card holder exeeded maximum transaction count Transaction ID is: ";

    public static final String FAILED_TO_SEND_MESSAGE_TO_TXN_SWITCH = "Failed to send request message to transaction switch";
    public static final String INVALID_TXN_SWITCH_RESPONSE = "Invalid transaction switch response";
    public static final String FAILED_TO_RECEIVE_MESSAGE_FROM_TXN_SWITCH = "Failed to receive response message from transaction switch";
    public static final String FAILD_SWITCH_REQUEST_CREATE = "Failed to generate switch request";

    public static final String MPI_SERVER_NOTRESPONDING = "MPI server not responding";
    public static final String FAILD_MPI_REQUEST_CREATE = "Failed to generate MPI request";

    public static final String ECI_INVALID = "Invalid ECI value";

    public static final String PROCESS_VEREQ_PAREQ_TO_MPI = "| VReq+PAReq message for the IPG to MPI for Merchant : ";
    public static final String PROCESS_RISK_VALIDATE_MID = "Risk validate using merchant risk profile for Merchant :";
    public static final String PROCESS_RISK_VALIDATE_MERCHANT_CUSTOMER = "Risk validate using merchant customer risk profile for Merchant : ";
    public static final String PROCESS_INPUT_VALIDATE_FAIL = "IPGMerchantTransactionServlet >> input fields validate failed for Merchant :";
    public static final String PROCESS_INPUT_VALIDATE_SUCCESS = "IPGMerchantTransactionServlet >> input fields validate success for Merchant :";
    public static final String PROCESS_MERCHANT_START = "IPGMerchantTransactionServlet >>  Payment process start for Merchant :";
    public static final String PROCESS_RISK_VALIDATE_SUCCESS = "Risk validate success for Merchant :";
    public static final String PROCESS_RISK_VALIDATE_FAIL = "Risk validate fail for Merchant :";
    public static final String PROCESS_IPG_TXN_INITIATE = "Successfully initiated ipg transaction for Merchant :";
    public static final String PROCESS_3DSECURE_ENABLE = "3D secure enable for Merchant :";
    public static final String PROCESS_3DSECURE_DISABLE = "3D secure disable for Merchant :";
    public static final String PROCESS_TO_MPI_INITIATE = "Initiate communicate with MPI server for Merchant :";
    public static final String PROCESS_TO_MPI_NO_RESPONSE = "MPI server not responding for Merchant :";
    public static final String PROCESS_TO_MPI_ERROR = "Failed to generate MPI request : VReq+PAReq message for Merchant :";
    public static final String PROCESS_IPG_TO_ENGINE = "IPG Engine request for the IPG to IPG Engine for Merchant :";
    public static final String PROCESS_ENGINE_TO_IPG_SUCCESS = "IPG Engine response success for Merchant :";
    public static final String PROCESS_ENGINE_TO_IPG_FAIL = "Failed to generate switch request message for Merchant :";
    public static final String PROCESS_IPG_RULE_FAIL = "Failed to initiate ipg transaction due to Rule failure for Merchant :";

    public static final String SECURITY_MECHANISM_DIGITALLYSIGN = " Security Mechanism : DigitallySign for Merchant :";
    public static final String SECURITY_MECHANISM_SYMMETRICKEY = " Security Mechanism : SymmetricKey for Merchant :";

    public static final String IPG_ENGINE_RESPONSE_SUCCESS = " IPG Engine response success for Merchant :";

    public static final String MESSAGE_CURRENT_TOTAL_TXN_AMOUNT = " Current Total Transaction Amount is: ";

    public static final String MERCHANT_ADDON_IMAGE_INVALID = "Addon Image empty or invalid for Merchant : ";
    public static final String MESSAGE_EMPTY_TERMINAL = "Merchant terminal can't be empty.";
    public static final String MESSAGE_EMPTY_CREDENTIAL = "Merchant credential can't be empty.";

    public static final String EMAIL_SENDING_ERROR = " | email sending error";
    public static final String EMAIL_SENDING_FAIL = " | email sending fail";
    public static final String EMAIL_SERVICE_DISABLED = " | email service is disabled";
    public static final String EMAIL_SENDING_FAIL_EMPTY_EMAIL = " | email sending fail : card holder email empty";

}
