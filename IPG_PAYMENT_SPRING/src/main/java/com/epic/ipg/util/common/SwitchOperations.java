/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.TransactionResponseBean;
import com.epic.ipg.dao.payment.MerchantProcessDAO;
import com.epic.ipg.dao.payment.MpiResponseDAO;
import com.epic.ipg.manager.payment.MerchantProcessManager;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.MessageVarList;
import com.epic.ipg.util.varlist.StatusVarList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dilanka_w
 */
@Repository
@Scope("prototype")
public class SwitchOperations {

    @Autowired
    MpiResponseDAO mpiResponseDAO;
    @Autowired
    MerchantProcessDAO merchantProcessDAO;
    @Autowired
    MerchantProcessManager merchantProcessValidator;
    @Autowired
    MerchantProcessManager merchantProcessManager;
    @Autowired
    LogFileCreator logFileCreator;

    public TransactionResponseBean sendToSwitch(String transactionID, String merchantID, CommonFilePaths commonFilePaths) throws CustomException {

        String message;
        TransactionResponseBean ipgTxnResponseBean;

        List transactionSwitchResponseLst;
        boolean transactionSwitchResponseFlag;
        String switchRequestMessage = merchantProcessValidator.createRequestMessageForTxnSwitch(transactionID);

        try {

            if (switchRequestMessage.length() > 0) {

                /**
                 * switch request message created successfully. update
                 * transaction status in IPGTRANSACTION and
                 * IPGTRANSACTIONHISTORY tables
                 */
                merchantProcessDAO.updateTransactionStage(transactionID, StatusVarList.TXN_ENGINEREQCREATED, null);

                transactionSwitchResponseLst = merchantProcessValidator.communicateWithTxnSwitch(switchRequestMessage, transactionID, merchantID, commonFilePaths);
                transactionSwitchResponseFlag = Boolean.parseBoolean(transactionSwitchResponseLst.get(0).toString());

                if (!transactionSwitchResponseFlag) {

                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(this.getClass().getSimpleName() + transactionSwitchResponseLst.get(1).toString() + " for merchant : " + merchantID, commonFilePaths.getInforLogPath());

                    /**
                     * update transaction status in IPGTRANSACTION and
                     * IPGTRANSACTIONHISTORY tables
                     */
                    merchantProcessDAO.updateTransactionStage(transactionID, StatusVarList.TXN_FAILED, null);
                    message = merchantProcessManager.sendOrderStatusEmail(transactionID, MessageVarList.TXN_FAILED, MessageVarList.TXN_FAILED, commonFilePaths);

                    ipgTxnResponseBean = new TransactionResponseBean(false, transactionID, CommonVarList.SWITCH_RESPONSE_FAIL, message);

                } else {

                    if (transactionSwitchResponseLst.get(3).equals(CommonVarList.RESPONCECODE_SUCESS)) {

                        logFileCreator.writInforTologs(this.getClass().getSimpleName() + MessageVarList.IPG_ENGINE_RESPONSE_SUCCESS + merchantID, commonFilePaths.getInforLogPath());
                        /**
                         * update transaction status in IPGTRANSACTION and
                         * IPGTRANSACTIONHISTORY tables
                         */
                        merchantProcessDAO.updateTransactionStage(transactionID, StatusVarList.TXN_COMPLETED, null);

                        message = merchantProcessManager.sendOrderStatusEmail(transactionID, MessageVarList.TXN_SUCCESS, MessageVarList.TXN_COMPLETED, commonFilePaths);

                        ipgTxnResponseBean = new TransactionResponseBean(true, transactionSwitchResponseLst.get(2).toString(), transactionSwitchResponseLst.get(3).toString(), message);

                    } else {

                        //write infor log.__________________________________________________________________________________________________________
                        logFileCreator.writInforTologs(this.getClass().getSimpleName() + transactionSwitchResponseLst.get(1).toString() + " for merchant : " + merchantID, commonFilePaths.getInforLogPath());

                        /**
                         * update transaction status in IPGTRANSACTION and
                         * IPGTRANSACTIONHISTORY tables
                         */
                        merchantProcessDAO.updateTransactionStage(transactionID, StatusVarList.TXN_FAILED, null);
                        message = merchantProcessManager.sendOrderStatusEmail(transactionID, MessageVarList.TXN_FAILED, MessageVarList.TXN_FAILED, commonFilePaths);

                        ipgTxnResponseBean = new TransactionResponseBean(false, transactionID, CommonVarList.SWITCH_RESPONSE_FAIL, message);
                    }

                }

            } else {

                logFileCreator.writInforTologs(this.getClass().getSimpleName() + "| Failed to generate switch request message for merchant ID : " + merchantID, commonFilePaths.getInforLogPath());

                /**
                 * update transaction status in IPGTRANSACTION and
                 * IPGTRANSACTIONHISTORY tables
                 */
                merchantProcessDAO.updateTransactionStage(transactionID, StatusVarList.TXN_ENGINEREQCREATEDFAIL, null);
                ipgTxnResponseBean = new TransactionResponseBean(false, transactionID, CommonVarList.SWITCH_RESPONSE_FAIL, MessageVarList.FAILD_SWITCH_REQUEST_CREATE);

            }
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage());
        }

        return ipgTxnResponseBean;
    }

}