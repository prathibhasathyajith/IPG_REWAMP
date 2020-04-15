/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.IPGFieldsBean;
import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.bean.payment.MerchantRiskBean;
import com.epic.ipg.bean.payment.MerchantSendToMpiBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.bean.payment.TransactionResponseBean;
import com.epic.ipg.dao.payment.MerchantProcessDAO;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.validations.CreditCardValidator;
import com.epic.ipg.manager.payment.MerchantProcessManager;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.SwitchOperations;
import com.epic.ipg.util.validations.UserInputValidator;
import com.epic.ipg.util.varlist.CardAssociationVarList;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.MessageVarList;
import com.epic.ipg.util.varlist.StatusVarList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author chanuka_g
 */
@Controller
@CrossOrigin
public class MerchantProcessController {

    @Autowired
    MerchantProcessManager merchantProcessManager;

    @Autowired
    MerchantProcessDAO merchantProcessDAO;

    @Autowired
    CreditCardValidator creditCardValidator;

    @Autowired
    SystemDateTime systemDateTime;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    LogFileCreator logFileCreator;

    @Autowired
    SwitchOperations switchOperations;

    String encodedStringMessage = null;
    String mpiUrl = null;
    CommonFilePaths commonFilePaths;
    String infoLogpath;

    @RequestMapping(value = "/IPGMerchantTransactionServlet", method = RequestMethod.POST)
    public String processMerchant(ModelMap model, HttpServletRequest request) {

        Logger.getLogger(MerchantProcessController.class.getName()).log(Level.INFO, "called to MerchantProcessController :");

        String retMsg = "merchantaddon_new";
        boolean validation;
        String mId;
        String transactionId;
        MerchantResponseBean responseBean;
        String message = "";

        try {

            HttpSession session = request.getSession(false);

            String cardType = session.getAttribute("cardType").toString();
            TransactionBean iPGTransactionBean = (TransactionBean) session.getAttribute("iPGTransaction");
            mId = iPGTransactionBean.getMerchantId();
            transactionId = iPGTransactionBean.getiPGTransactionId();
            String captcha = (String) session.getAttribute("captcha");
            String code = request.getParameter("code");
//            code = captcha;
            MerchantSendToMpiBean merchantToMpiBean = merchantProcessManager.setTransactiondata(session, iPGTransactionBean, request);
            commonFilePaths = commonDAO.getCommonFilePath(logFileCreator.getOsType());
            infoLogpath = commonFilePaths.getInforLogPath();
            logFileCreator.writInforTologs(MessageVarList.PROCESS_MERCHANT_START + mId, infoLogpath);

            validation = this.validateRequst(request, session, merchantToMpiBean, code, captcha);
            if (!validation) {

                //write infor log.__________________________________________________________________________________________________________
                logFileCreator.writInforTologs(MessageVarList.PROCESS_INPUT_VALIDATE_FAIL + mId, infoLogpath);
                merchantProcessManager.inputValidate(request, cardType, code, captcha);

            } else {
                //write infor log.__________________________________________________________________________________________________________
                logFileCreator.writInforTologs(MessageVarList.PROCESS_INPUT_VALIDATE_SUCCESS + mId, infoLogpath);

                /**
                 * insert transaction details to IPGTRANSACTION and
                 * IPGTRANSACTIONHISTORY tables
                 */
                merchantProcessDAO.insertTransactionNow(merchantToMpiBean);

                MerchantRiskBean merchantRiskBean;

                if ("YES".equals(merchantProcessDAO.getTransactionInitiatedStatus(mId))) {

                    String defaultMerchant = merchantProcessDAO.getDefaultMerchant(mId);
                    merchantRiskBean = merchantProcessDAO.getMerchantCustomerRiskProfile(defaultMerchant);

                    //write info log. for write risk validation ________________________________________________________
                    logFileCreator.writInforTologsForIpgRiskValidation(MessageVarList.PROCESS_RISK_VALIDATE_MERCHANT_CUSTOMER + mId, commonFilePaths.getIpgriskvalidation());
                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_RISK_VALIDATE_MERCHANT_CUSTOMER + mId, infoLogpath);

                } else {

                    merchantRiskBean = merchantProcessDAO.getMerchantRiskProfile(mId);

                    //write info log. for write risk validation ________________________________________________________
                    logFileCreator.writInforTologsForIpgRiskValidation(MessageVarList.PROCESS_RISK_VALIDATE_MID + mId, commonFilePaths.getIpgriskvalidation());
                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_RISK_VALIDATE_MID + mId, infoLogpath);

                }

                List valList = merchantProcessManager.validateWithRiskParam(merchantRiskBean, merchantToMpiBean);

                responseBean = merchantProcessDAO.getMerchantForResponseByTxnID(transactionId);

                request.setAttribute("responseBean", responseBean);
                request.setAttribute("txnId", transactionId);

                if ((Boolean) valList.get(0)) {

                    //write info log. for write rule validation ________________________________________________________
                    logFileCreator.writInforTologsForIpgRiskValidation(MessageVarList.PROCESS_RISK_VALIDATE_SUCCESS + mId, commonFilePaths.getIpgriskvalidation());
                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_RISK_VALIDATE_SUCCESS + mId, infoLogpath);

                    /**
                     * update transaction status in IPGTRANSACTION and
                     * IPGTRANSACTIONHISTORY tables
                     */
                    merchantProcessDAO.updateTransactionStage(transactionId, StatusVarList.TXN_RISK_VALIDATE_SUCCESS, null);

                    request.setAttribute("merchantaddonrequest", merchantToMpiBean);

                    message = this.processTransaction(merchantToMpiBean);

                } else {
                    //write info log. for write rule validation ________________________________________________________
                    logFileCreator.writInforTologsForIpgRuleValidation(MessageVarList.PROCESS_RISK_VALIDATE_FAIL + valList.get(1), commonFilePaths.getIpgriskvalidation());
                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_RISK_VALIDATE_FAIL + valList.get(1), infoLogpath);

                    /**
                     * update transaction status in IPGTRANSACTION and
                     * IPGTRANSACTIONHISTORY tables
                     */
                    message = (String) valList.get(1);
                    merchantProcessDAO.updateTransactionStage(transactionId, StatusVarList.TXN_RISK_VALIDATE_FAILED, message);

                }

                if (message.isEmpty()) {
                    return "redirect:/redirectToMpi";
                } else {
                    request.setAttribute("message", message);
                    request.setAttribute("isSuccessTxn", "false");
                    retMsg = "successPage_new";
                }

            }

        } catch (CustomException ex) {
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
            retMsg = "oops";
        } catch (Exception ex) {
            request.setAttribute("message", MessageVarList.END_TXN_FAILED);
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
            retMsg = "successPage_new";
        }
        return retMsg;
    }

    // newly added method for validation test for iframe
    //============================================================
    @CrossOrigin
    @RequestMapping(value = "/IPGFieldValidateFlagServlet", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateFlag(@RequestBody IPGFieldsBean bean, ModelMap model, HttpServletRequest request) {
        boolean retMsg = true;
        boolean validation;
        String mId;
        try {
            
            HttpSession session = request.getSession(false);

            String cardType = session.getAttribute("cardType").toString();
            TransactionBean iPGTransactionBean = (TransactionBean) session.getAttribute("iPGTransaction");
            String captcha = (String) session.getAttribute("captcha");
            String code = bean.getCode();

            code = captcha;
            
            MerchantSendToMpiBean merchantToMpiBean = merchantProcessManager.setTransactiondata(session, iPGTransactionBean, request);

            validation = this.validateRequst(bean, session, merchantToMpiBean, code, captcha);
            
            if(!validation){
                retMsg = false;
            }
   
        } catch (Exception e) {
            System.out.println(e);
        }

        return retMsg;
    }

    //=============================================================
    public String processTransaction(MerchantSendToMpiBean merchantToMpiBean) {
        String txnId = null;
        String merchantID;
        String ipgTrigerSequence = "1";
        String message = "";
        String ruleValidationMessage = "";
        String mpiRequestMessage;

        try {

            txnId = merchantToMpiBean.getiPGTransactionId();
            merchantID = merchantToMpiBean.getMerchantId();
            CommonFilePaths pathBean = commonDAO.getCommonFilePath(logFileCreator.getOsType());

            ruleValidationMessage = merchantProcessManager.validateRule(ipgTrigerSequence, merchantToMpiBean);

            if (ruleValidationMessage.isEmpty()) {
                //write infor log.__________________________________________________________________________________________________________
                logFileCreator.writInforTologs(MessageVarList.PROCESS_IPG_TXN_INITIATE + merchantID, infoLogpath);

                /**
                 * update transaction status in IPGTRANSACTION and
                 * IPGTRANSACTIONHISTORY tables
                 */
                merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_RULE_VALIDATE_SUCCESS, null);

                String secureEnable = merchantProcessDAO.get3DSecureEnableStatus(merchantID);

                if ("YES".equals(secureEnable)) {

                    /**
                     * 3D secure enable. update IPGTRANSACTION table
                     */
                    merchantProcessDAO.updateTransactionWhen3DSecureEnable(txnId);

                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_3DSECURE_ENABLE + merchantID, infoLogpath);
                    String msg = merchantProcessManager.generateRequestMessage();

                    mpiRequestMessage = merchantProcessManager.fillRequestMessage(msg, merchantToMpiBean, commonDAO.getCommonConfigValue(CommonVarList.IPGENGINEURL));

                    if (mpiRequestMessage.length() > 0) {

                        String mpiRequestMessageForLogs = merchantProcessManager.fillRequestMessageWithMaskCard(mpiRequestMessage, merchantToMpiBean);

                        logFileCreator.writInforTologs(MessageVarList.PROCESS_VEREQ_PAREQ_TO_MPI + merchantID + CommonVarList.NEWLINE + mpiRequestMessageForLogs, infoLogpath);
                        logFileCreator.writInforTologsForMpiRequest(mpiRequestMessageForLogs, commonFilePaths.getMpirequest());

                        /**
                         * MPI request message created successfully. update
                         * transaction status in IPGTRANSACTION and
                         * IPGTRANSACTIONHISTORY tables
                         */
                        merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_VERIFIREQUESTCREATED, null);
                        merchantProcessDAO.insertIPG3DSecureVerificationRequest(merchantToMpiBean);

                        //write infor log.__________________________________________________________________________________________________________
                        byte[] encodedMpiRequestMessage;
                        boolean status;

                        //get "xml string" and first convert it to bytes.
                        //then encode it using base 64
                        //and again returned byte array converted to the string and send to the mpi server
                        encodedMpiRequestMessage = Base64.encode(mpiRequestMessage.getBytes());
                        encodedStringMessage = new String(encodedMpiRequestMessage);

                        /**
                         * update transaction status in IPGTRANSACTION and
                         * IPGTRANSACTIONHISTORY tables before send txn data to
                         * the mpi server
                         */
                        merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_VERIFIREQUESTSENT, null);

                        //get mpi server configuration from DB
                        mpiUrl = commonDAO.getCommonConfigValue(CommonVarList.MPISERVERURL);

                        status = merchantProcessManager.getMpiServerAvailability();

                        if (status) {
                            //write infor log.__________________________________________________________________________________________________________
                            logFileCreator.writInforTologs(MessageVarList.PROCESS_TO_MPI_INITIATE + merchantID, infoLogpath);
                        } else {
                            //write infor log.__________________________________________________________________________________________________________
                            logFileCreator.writInforTologs(MessageVarList.PROCESS_TO_MPI_NO_RESPONSE + merchantID, infoLogpath);
                            message = MessageVarList.MPI_SERVER_NOTRESPONDING;
                        }

                    } else {

                        //write infor log.__________________________________________________________________________________________________________
                        logFileCreator.writInforTologs(MessageVarList.PROCESS_TO_MPI_ERROR + merchantID, infoLogpath);

                        /**
                         * mpi request message created failed. update
                         * transaction status in IPGTRANSACTION and
                         * IPGTRANSACTIONHISTORY tables
                         */
                        message = MessageVarList.FAILD_MPI_REQUEST_CREATE;
                        merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_VERIFIREQCREATEDFAIL, message);

                    }
                } else { // 3d secure not enabled

                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(MessageVarList.PROCESS_3DSECURE_DISABLE + merchantID, infoLogpath);
                    TransactionResponseBean transactionResponseBean = switchOperations.sendToSwitch(txnId, merchantID, commonFilePaths);
                    message = transactionResponseBean.getMessage();
                }
            } else {

                //write rule log
                logFileCreator.writInforTologsForIpgRuleValidation(ruleValidationMessage, pathBean.getIpgrulevalidation());
                //write infor log.__________________________________________________________________________________________________________
                logFileCreator.writInforTologs(MessageVarList.PROCESS_IPG_RULE_FAIL + merchantID, infoLogpath);

                /**
                 * update transaction status in IPGTRANSACTION and
                 * IPGTRANSACTIONHISTORY tables
                 */
                merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_RULE_VALIDATE_FAILED, ruleValidationMessage);

                message = MessageVarList.TXN_FAILED;
            }

        } catch (Exception ex) {

            message = merchantProcessManager.sendOrderStatusEmail(txnId, MessageVarList.TXN_FAILED, MessageVarList.TXN_FAILED, commonFilePaths);
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());

        }

        return message;
    }

    @RequestMapping(value = "/redirectToMpi", method = RequestMethod.GET)
    public void redirectTest(HttpServletResponse response) {

        try (PrintWriter out = response.getWriter()) {
            out.print("<html>");
            out.print("<head>");
            out.print("<script language=\"javascript\">function autoSubmit(){document.form1.submit();}</script>");
            out.print("</head>");
            out.print("<body onload=\"autoSubmit();\">");
            out.print("<form id=\"form1\" method=\"post\" name=\"form1\" action=\"" + mpiUrl + "\">");
            out.print("<input type=\"hidden\" name=\"mpirequest\" value=\"" + encodedStringMessage + "\" />");
            out.print("</form>");
            out.print("</body>");
            out.print("</html>");
        } catch (IOException ex) {
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validateRequst(HttpServletRequest request, HttpSession session, MerchantSendToMpiBean merchantToMpiBean, String code, String captcha) {

        boolean validation = false;

        try {

            String cardType = session.getAttribute("cardType").toString();

            validation = UserInputValidator.isNonNumericNonSpecialString(request.getParameter("firstname"))
                    && creditCardValidator.validationCriteria(request.getParameter("cardNumber"), cardType)
                    && UserInputValidator.isNumeric(request.getParameter("month"))
                    && merchantProcessManager.validateExpiry(request.getParameter("month"), request.getParameter("year"))
                    && UserInputValidator.isNumeric(request.getParameter("year"))
                    && ((merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.VISA) && UserInputValidator.isNumeric(request.getParameter("cvcNum")))
                    || (merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.MARSTERCARD) && UserInputValidator.isNumeric(request.getParameter("cvvNum")))
                    || (merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.AMEX) && UserInputValidator.isNumeric(request.getParameter("cidNum"))))
                    && UserInputValidator.isCaptchaCorrect(code, captcha);

        } catch (CustomException ex) {
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
        }
        return validation;
    }
    
    private boolean validateRequst(IPGFieldsBean request, HttpSession session, MerchantSendToMpiBean merchantToMpiBean, String code, String captcha) {

        boolean validation = false;

        try {

            String cardType = session.getAttribute("cardType").toString();

            validation = UserInputValidator.isNonNumericNonSpecialString(request.getFirstname())
                    && creditCardValidator.validationCriteria(request.getCardNumber(), cardType)
                    && UserInputValidator.isNumeric(request.getMonth())
                    && merchantProcessManager.validateExpiry(request.getMonth(), request.getYear())
                    && UserInputValidator.isNumeric(request.getYear())
                    && ((merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.VISA) && UserInputValidator.isNumeric(request.getCvcNum()))
                    || (merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.MARSTERCARD) && UserInputValidator.isNumeric(request.getCvcNum()))
                    || (merchantToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.AMEX) && UserInputValidator.isNumeric(request.getCidNum())))
                    && UserInputValidator.isCaptchaCorrect(code, captcha);

        } catch (CustomException ex) {
            Logger.getLogger(MerchantProcessController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
        }
        return validation;
    }
}
