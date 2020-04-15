/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.MPIResponseBean;
import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.bean.payment.TransactionHistoryBean;
import com.epic.ipg.bean.payment.TransactionResponseBean;
import com.epic.ipg.dao.payment.MerchantProcessDAO;
import com.epic.ipg.dao.payment.MpiResponseDAO;
import com.epic.ipg.manager.payment.MerchantProcessManager;
import com.epic.ipg.manager.payment.MpiResponseManager;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.common.SwitchOperations;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.varlist.MessageVarList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class MpiResponseController {

    @Autowired
    MpiResponseDAO mpiResponseDAO;
    @Autowired
    MerchantProcessDAO merchantProcessDAO;
    @Autowired
    MpiResponseManager mpiResponseManager;
    @Autowired
    CommonDAO commonDAO;
    @Autowired
    LogFileCreator logFileCreator;
    @Autowired
    MerchantProcessManager merchantProcessManager;
    @Autowired
    SwitchOperations switchOperations;

    CommonFilePaths commonFilePaths;

    @RequestMapping(value = "/IPGMPITxnResponseServlet", method = RequestMethod.POST)
    public String processMpiResponse(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Logger.getLogger(MpiResponseController.class.getName()).log(Level.INFO, "called to MpiResponseController :");

        String mpiResponseTxnId = null;
        String mpiResponseStatus;
        TransactionResponseBean ipgTxnResponseBean;
        TransactionHistoryBean iPGTransactionHistoryBean = new TransactionHistoryBean();

        try {

            String mpiResponseMessage = request.getParameter("mpiresponse");//read mpi response message
            commonFilePaths = commonDAO.getCommonFilePath(logFileCreator.getOsType());

            logFileCreator.writInforTologsForMpiResponse(mpiResponseMessage, commonFilePaths.getMpiresponse());
            byte[] responseMessageByte = Base64.decode(mpiResponseMessage.getBytes());
            String responseMessageString = new String(responseMessageByte);

            logFileCreator.writInforTologsForMpiResponse(responseMessageString, commonFilePaths.getMpiresponse());
            logFileCreator.writInforTologs("IPGMPITxnResponseServlet >>MPI response \n" + responseMessageString, commonFilePaths.getInforLogPath());
            MPIResponseBean iPGMPIResponseBean = mpiResponseManager.setMPIResponseData(responseMessageString);

            mpiResponseStatus = iPGMPIResponseBean.getTxnStatus();
            mpiResponseTxnId = iPGMPIResponseBean.getTransactionId();

            mpiResponseManager.updateMpiResponseHistory(iPGMPIResponseBean, iPGTransactionHistoryBean, commonFilePaths.getInforLogPath());

            MerchantResponseBean responseBean = merchantProcessDAO.getMerchantForResponseByTxnID(mpiResponseTxnId);
            request.setAttribute("responseBean", responseBean);
            request.setAttribute("txnId", mpiResponseTxnId);

            if (iPGMPIResponseBean.isValidFlag()) {

                MPIResponseBean eciMPIResponseBean = mpiResponseDAO.getTxnECIData(mpiResponseTxnId);

                //mpi responsed txn status is "Y" (success. so we can continue)
                boolean responseStatusYesEciAllow = mpiResponseStatus.contentEquals("Y") && mpiResponseDAO.checkECIAllow(eciMPIResponseBean, "Y");
                boolean responseStatusNoEciAllow = mpiResponseStatus.contentEquals("N") && mpiResponseDAO.checkECIAllow(eciMPIResponseBean, "N");
                boolean responseStatusUnenrollEciAllow = mpiResponseStatus.contentEquals("U") && mpiResponseDAO.checkECIAllow(eciMPIResponseBean, "U");
                boolean responseStatusAttemptEciAllow = mpiResponseStatus.contentEquals("A") && mpiResponseDAO.checkECIAllow(eciMPIResponseBean, "A");

                if (responseStatusYesEciAllow || responseStatusNoEciAllow || responseStatusUnenrollEciAllow || responseStatusAttemptEciAllow) {

                    logFileCreator.writInforTologs(this.getClass().getSimpleName() + " mpiResponseStatus : " + mpiResponseStatus + " | isAllowedECI : true", commonFilePaths.getInforLogPath());
                    ipgTxnResponseBean = switchOperations.sendToSwitch(mpiResponseTxnId, responseBean.getMerchantID(), commonFilePaths);

                } else {

                    logFileCreator.writInforTologs(this.getClass().getSimpleName() + " mpiResponseStatus :" + mpiResponseStatus + "| isAllowedECI :" + mpiResponseDAO.checkECIAllow(eciMPIResponseBean, mpiResponseStatus), commonFilePaths.getInforLogPath());
                    mpiResponseManager.updateTxnHistory(iPGTransactionHistoryBean, iPGMPIResponseBean);
                    ipgTxnResponseBean = new TransactionResponseBean(false, mpiResponseTxnId, "01", merchantProcessManager.sendOrderStatusEmail(mpiResponseTxnId, MessageVarList.TXN_FAILED, MessageVarList.ECI_INVALID, commonFilePaths));

                }
            } else {

                logFileCreator.writInforTologs(this.getClass().getSimpleName() + " mpi response error :" + iPGMPIResponseBean.getErrorMessage(), commonFilePaths.getInforLogPath());
                mpiResponseManager.updateTxnHistory(iPGTransactionHistoryBean, iPGMPIResponseBean);
                ipgTxnResponseBean = new TransactionResponseBean(false, mpiResponseTxnId, "01", merchantProcessManager.sendOrderStatusEmail(mpiResponseTxnId, MessageVarList.TXN_FAILED, iPGMPIResponseBean.getErrorMessage(), commonFilePaths));

            }

            mpiResponseManager.createPaymentReceipt(ipgTxnResponseBean, request);
        } catch (CustomException ex) {

            request.setAttribute("message", merchantProcessManager.sendOrderStatusEmail(mpiResponseTxnId, MessageVarList.TXN_FAILED, MessageVarList.TXN_FAILED, commonFilePaths));
            request.setAttribute("isSuccessTxn", "false");
            Logger.getLogger(MpiResponseController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());

        } catch (Exception ex) {

            Logger.getLogger(MpiResponseController.class.getName()).log(Level.SEVERE, null, ex);
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
            return "oops";
        }
        return "successPage_new";
    }

}
