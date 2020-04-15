/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.MerchantAddonBean;
import com.epic.ipg.bean.payment.MerchantBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.dao.payment.MerchantRequestDAO;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.manager.payment.MerchantRequestManager;
import com.epic.ipg.util.common.Common;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.MessageVarList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author chanuka_g
 */
@Controller
public class MerchantRequestController {

    @Autowired
    MerchantRequestDAO merchantRequestDAO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    LogFileCreator logFileCreator;

    @Autowired
    MerchantRequestManager merchantRequestManager;

    CommonFilePaths commonFilePaths;

    @Autowired
    Common common;

    @RequestMapping(value = "/IPGMerchantAddOnServlet", method = RequestMethod.POST)
    @SuppressWarnings("empty-statement")
    public String validateMerchant(ModelMap model, HttpServletRequest request) {
        Logger.getLogger(MerchantRequestController.class.getName()).log(Level.INFO, "called MerchantRequestController : validateMerchant");

        String result = "merchantaddon_new";
        String errorMessage = "";
        String merchantId = request.getParameter("merchantId");

        try {

            HttpSession session = merchantRequestManager.createSession(request);
            commonFilePaths = commonDAO.getCommonFilePath(logFileCreator.getOsType());

            String inforLog = this.getClass().getSimpleName() + " : Merchant response parameters >> \n"
                    + "merchantId : " + request.getParameter("merchantId")
                    + "|amount : " + request.getParameter("amount")
                    + "|dateofregistry : " + request.getParameter("dateofregistry")
                    + "|refno : " + request.getParameter("refno")
                    + "|byteSignedDataString : " + request.getParameter("byteSignedDataString")
                    + "|signature : " + request.getParameter("signature")
                    + "|currencyCode : " + request.getParameter("currencyCode")
                    + "|key : " + request.getParameter("key")
                    + "|orderid : " + request.getParameter("orderid")
                    + "|emerchantId : " + request.getParameter("emerchantId")
                    + "|url : " + request.getParameter("url");

            Logger.getLogger(MerchantRequestController.class.getName()).log(Level.INFO, inforLog);

            //write infor log. ________________________________________________________________________________
            logFileCreator.writInforTologs("\n\n\n___________________________________________________________________________________", commonFilePaths.getInforLogPath());
            logFileCreator.writInforTologs(inforLog, commonFilePaths.getInforLogPath());

            MerchantBean merchantBean = merchantRequestDAO.getMerchantDetail(merchantId);
            MerchantAddonBean merchantAddonBean = merchantRequestDAO.getMerchantAddon(merchantId);

            session.setAttribute("merchantBean", merchantBean);
            session.setAttribute("addondetail", merchantAddonBean);

            if (merchantRequestManager.validateMerchant(merchantBean, commonFilePaths, request)) {

                //write info log. for write mpi request sent to mpi by ipg________________________________________________________________
                logFileCreator.writInforTologsForMerchantLogin(MessageVarList.AUTH_SUCCESS + merchantId, commonFilePaths.getIpgmerchantlogin());
                //write infor log.__________________________________________________________________________________________
                logFileCreator.writInforTologs(this.getClass().getSimpleName() + MessageVarList.AUTH_SUCCESS + merchantId, commonFilePaths.getInforLogPath());

                if (merchantAddonBean != null) {

                    String merchantPassEncoded = merchantRequestDAO.getMerchantCredential(merchantId, request.getParameter("radio"));

                    if (merchantPassEncoded != null) {

                        String merchantPass = new String(Base64.decode(merchantPassEncoded.getBytes()));
                        String terminalId = merchantRequestDAO.getMerchantTerminal(merchantId, request.getParameter("currencyCode"));

                        if (terminalId != null) {

                            String transactionId = merchantRequestManager.createTransactionId();
                            TransactionBean transactionBean = merchantRequestManager.setIPGTransactionBeanData(session, request, transactionId, merchantBean);

                            session.setAttribute("password", merchantPass);
                            session.setAttribute("terminalId", terminalId);
                            session.setAttribute("iPGTransaction", transactionBean);
                            session.setAttribute("cardType", request.getParameter("radio"));

                            session.setAttribute("logoPath", merchantAddonBean.getLogoPath());
                            session.setAttribute("merchantType", request.getParameter("merchantType"));
                            session.setAttribute("txnRefNo", request.getParameter("txnRefNo"));
                            session.setAttribute("transactiontype", CommonVarList.TRANSACTIONTYPE_SALE);

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
            } else {
                errorMessage = MessageVarList.END_TXN_FAILED;
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
