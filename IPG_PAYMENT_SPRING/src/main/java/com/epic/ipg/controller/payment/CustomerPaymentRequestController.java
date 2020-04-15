/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.CustomerPaymentRequestBean;
import com.epic.ipg.bean.payment.MerchantAddonBean;
import com.epic.ipg.bean.payment.MerchantBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.dao.payment.CustomerPaymentRequestDAO;
import com.epic.ipg.dao.payment.MerchantRequestDAO;
import com.epic.ipg.manager.payment.MerchantRequestManager;
import com.epic.ipg.util.common.Common;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.common.TDESEncryptor;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.MessageVarList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author dilanka_w
 */
@Controller
public class CustomerPaymentRequestController {

    @Autowired
    CustomerPaymentRequestDAO customerPaymentRequestDAO;

    @Autowired
    MerchantRequestDAO merchantRequestDAO;

    @Autowired
    MerchantRequestManager merchantRequestManager;

    @Autowired
    Common common;

    @Autowired
    LogFileCreator logFileCreator;

    CommonFilePaths commonFilePaths;

    @Autowired
    CommonDAO commonDAO;

    @RequestMapping(path = {"/IPGCustomerRequestServlet"}, method = RequestMethod.GET)
    public String customerRequestGet(Model model, HttpServletRequest request) {

        String trnsactionRequestID = "";
        String result = "customerdetails_new";
        String errorMessage = "";
        String merchantID = "";

        try {

            String encryptedTrnsactionRequestID = request.getParameter("ab");

            if (encryptedTrnsactionRequestID != null) {

                HttpSession session = merchantRequestManager.createSession(request);

                trnsactionRequestID = TDESEncryptor.decrypt(CommonVarList.TDESENCRYPT_KEY, encryptedTrnsactionRequestID, "CBC");

                CustomerPaymentRequestBean customerPaymentRequestBean = customerPaymentRequestDAO.getTxnData(trnsactionRequestID);

                merchantID = customerPaymentRequestBean.getMerchantId();
                System.out.println("merchantID: " + merchantID);

                MerchantAddonBean merchantAddonBean = merchantRequestDAO.getMerchantAddon(merchantID);

                if (merchantAddonBean != null) {

                    session.setAttribute("transactiontype", CommonVarList.TRANSACTIONTYPE_PAYMENTREQUEST);
                    request.setAttribute("requestBean", customerPaymentRequestBean);

                    //get path
                    session.setAttribute("addondetail", merchantAddonBean);
                    session.setAttribute("logoPathByte", common.getMerchantImage(merchantAddonBean.getLogoPath(), merchantID));
                } else {
                    errorMessage = MessageVarList.EMPTY_MERCAHNT_ADDON;
                }

            } else {
                errorMessage = MessageVarList.EMPTY_CUSTOMER_REQUEST_ID;
            }

        } catch (Exception ex) {
            Logger.getLogger(MerchantRequestController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
            errorMessage = MessageVarList.END_TXN_FAILED;
        } finally {

            if (errorMessage.isEmpty()) {
                result = "customerdetails_new";
            } else {
                //write info log. for write mpi request sent to mpi by ipg __________________________________________________________
                logFileCreator.writInforTologsForMerchantLogin(errorMessage + merchantID, commonFilePaths.getIpgmerchantlogin());
                //write infor log.___________________________________________________________________________________________________
                logFileCreator.writInforTologs(this.getClass().getSimpleName() + errorMessage + merchantID, commonFilePaths.getInforLogPath());

                request.setAttribute("errorMessage", errorMessage);
                result = "errorAuthentication";
            }
        }
        return result;
    }

    @RequestMapping(path = {"/IPGCustomerRequestServlet"}, method = RequestMethod.POST)
    public String customerRequestPost(Model model, HttpServletRequest request) {

        Logger.getLogger(MerchantRequestController.class.getName()).log(Level.INFO, "called MerchantRequestController : validateMerchant");

        String result = "merchantaddon_new";
        String errorMessage = "";
        String merchantId = request.getParameter("merchantId");

        try {

            HttpSession session = merchantRequestManager.createSession(request);
            commonFilePaths = commonDAO.getCommonFilePath(logFileCreator.getOsType());

            String inforLog = this.getClass().getSimpleName() + " : Customer response parameters >> \n"
                    + "merchantId : " + request.getParameter("merchantId")
                    + "|amount : " + request.getParameter("amount")
                    + "|currencyCode : " + request.getParameter("currencyCode")
                    + "|cardType : " + request.getParameter("cardType");

            Logger.getLogger(MerchantRequestController.class.getName()).log(Level.INFO, inforLog);

            //write infor log. ________________________________________________________________________________
            logFileCreator.writInforTologs("\n\n\n___________________________________________________________________________________", commonFilePaths.getInforLogPath());
            logFileCreator.writInforTologs(inforLog, commonFilePaths.getInforLogPath());

            MerchantBean merchantBean = merchantRequestDAO.getMerchantDetail(merchantId);
            MerchantAddonBean merchantAddonBean = merchantRequestDAO.getMerchantAddon(merchantId);

            session.setAttribute("merchantBean", merchantBean);
            session.setAttribute("addondetail", merchantAddonBean);

            if (merchantAddonBean != null) {

                String merchantPassEncoded = merchantRequestDAO.getMerchantCredential(merchantId, request.getParameter("cardType"));

                if (merchantPassEncoded != null) {

                    String merchantPass = new String(Base64.decode(merchantPassEncoded.getBytes()));
                    String terminalId = merchantRequestDAO.getMerchantTerminal(merchantId, request.getParameter("currencyCode"));

                    if (terminalId != null) {

                        String transactionId = merchantRequestManager.createTransactionId();
                        TransactionBean transactionBean = merchantRequestManager.setIPGTransactionBeanData(session, request, transactionId, merchantBean);

                        session.setAttribute("password", merchantPass);
                        session.setAttribute("terminalId", terminalId);
                        session.setAttribute("iPGTransaction", transactionBean);
                        session.setAttribute("cardType", request.getParameter("cardType"));

                        session.setAttribute("logoPath", merchantAddonBean.getLogoPath());
                        session.setAttribute("merchantType", request.getParameter("merchantType"));
                        session.setAttribute("txnRefNo", request.getParameter("txnRefNo"));
                        session.setAttribute("transactiontype", CommonVarList.TRANSACTIONTYPE_PAYMENTREQUEST);

                        request.setAttribute("addon", merchantAddonBean);

                        //get path
                        session.setAttribute("logoPathByte", common.getMerchantImage(merchantAddonBean.getLogoPath(), merchantId));
                        session.setAttribute("merchantAddonFields", merchantRequestDAO.getMerchantAddonFields(merchantId));

                    } else {
                        errorMessage = MessageVarList.MESSAGE_EMPTY_TERMINAL;
                    }
                } else {
                    errorMessage = MessageVarList.MESSAGE_EMPTY_CREDENTIAL;
                }
            } else {
                errorMessage = MessageVarList.EMPTY_MERCAHNT_ADDON;
            }
        } catch (Exception ex) {

            Logger.getLogger(MerchantRequestController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
            errorMessage = MessageVarList.END_TXN_FAILED;

        } finally {

            if (errorMessage.isEmpty()) {
                result = "merchantaddon_new";
            } else {
                //write info log. for write mpi request sent to mpi by ipg __________________________________________________________
                logFileCreator.writInforTologsForMerchantLogin(errorMessage + merchantId, commonFilePaths.getIpgmerchantlogin());
                //write infor log.___________________________________________________________________________________________________
                logFileCreator.writInforTologs(this.getClass().getSimpleName() + errorMessage + merchantId, commonFilePaths.getInforLogPath());

                request.setAttribute("errorMessage", errorMessage);
                result = "errorAuthentication";
            }
        }
        return result;

    }

}
