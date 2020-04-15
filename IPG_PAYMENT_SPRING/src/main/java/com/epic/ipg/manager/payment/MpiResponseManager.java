/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.manager.payment;

import com.epic.ipg.bean.payment.MPIResponseBean;
import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.bean.payment.TransactionHistoryBean;
import com.epic.ipg.bean.payment.TransactionResponseBean;
import com.epic.ipg.dao.payment.MerchantProcessDAO;
import com.epic.ipg.dao.payment.MpiResponseDAO;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.common.SwitchOperations;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.StatusVarList;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author chanuka_g
 */
@Service
public class MpiResponseManager {

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
    @Autowired
    SwitchOperations switchOperations;

    public void updateMpiResponseHistory(MPIResponseBean iPGMPIResponseBean, TransactionHistoryBean iPGTransactionHistoryBean, String realPathInforLogPath) throws CustomException {
        if (iPGMPIResponseBean.isValidFlag()) {
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + " >>merchant request valid", realPathInforLogPath);

            iPGTransactionHistoryBean.setTransactionId(iPGMPIResponseBean.getTransactionId());
            iPGTransactionHistoryBean.setStatus(StatusVarList.TXN_VERIFICARIONRESRECEIVED);

        } else {//merchant request not valid.
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + " >>merchant request invalid", realPathInforLogPath);

            iPGTransactionHistoryBean.setTransactionId(iPGMPIResponseBean.getTransactionId());
            iPGTransactionHistoryBean.setStatus(StatusVarList.TXN_VERFICATION_FAILED);

        }

        mpiResponseDAO.updateTransactionStageAfterMPIRes(StatusVarList.TXN_VERIFICARIONRESRECEIVED, iPGMPIResponseBean);
        merchantProcessDAO.insertTransactionHistory(iPGTransactionHistoryBean);
        mpiResponseDAO.insertIPG3DSecureVerificationResponse(iPGMPIResponseBean);
    }

    public void updateTxnHistory(TransactionHistoryBean iPGTransactionHistoryBean, MPIResponseBean iPGMPIResponseBean) throws CustomException {

        iPGTransactionHistoryBean.setStatus(StatusVarList.TXN_FAILED);
        iPGTransactionHistoryBean.setRemarks("Transaction failed by MPI.");
        mpiResponseDAO.updateTransactionStageAfterMPIRes(StatusVarList.TXN_FAILED, iPGMPIResponseBean);
        merchantProcessDAO.insertTransactionHistory(iPGTransactionHistoryBean);
        mpiResponseDAO.insertIPG3DSecureVerificationResponse(iPGMPIResponseBean);
    }

    public MPIResponseBean setMPIResponseData(String mpiResponseMessage) throws CustomException {

        Logger.getLogger(MpiResponseManager.class.getName()).log(Level.INFO, "called IPGMPITxnResponseServlet : setMPIResponseData");

        try {
            MPIResponseBean iPGMPIResponseBean = new MPIResponseBean();

            String xml = mpiResponseMessage;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml + "")));

            doc.getDocumentElement().normalize();

            String rootNode = doc.getDocumentElement().getNodeName();
            if (rootNode.contentEquals("MPI_INTERFACE")) {
                //error. something missin in merchant request
                iPGMPIResponseBean.setValidFlag(false);

                //"MPIERROR" node
                NodeList listMPIERROR = doc.getElementsByTagName(CommonVarList.MPIERROR);
                for (int s = 0; s < listMPIERROR.getLength(); s++) {
                    Node nodeMPIERROR = listMPIERROR.item(s);
                    if (nodeMPIERROR.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementMPIERROR = (Element) nodeMPIERROR;

                        //"TRANSACTIONID" node
                        NodeList listTRANSACTIONID = elementMPIERROR.getElementsByTagName(CommonVarList.TRANSACTIONID);
                        Element elementTRANSACTIONID = (Element) listTRANSACTIONID.item(0);
                        NodeList textTRANSACTIONID = elementTRANSACTIONID.getChildNodes();
                        iPGMPIResponseBean.setTransactionId(textTRANSACTIONID.item(0).getNodeValue());

                        //"ERRORCODE" node
                        NodeList listERRORCODE = elementMPIERROR.getElementsByTagName(CommonVarList.ERRORCODE);
                        Element elementERRORCODE = (Element) listERRORCODE.item(0);
                        NodeList textERRORCODE = elementERRORCODE.getChildNodes();
                        iPGMPIResponseBean.setErrorCode(textERRORCODE.item(0).getNodeValue());

                        //"ERRORMESSAGE" node
                        NodeList listERRORMESSAGE = elementMPIERROR.getElementsByTagName(CommonVarList.ERRORMESSAGE);
                        Element elementERRORMESSAGE = (Element) listERRORMESSAGE.item(0);
                        NodeList textERRORMESSAGE = elementERRORMESSAGE.getChildNodes();
                        iPGMPIResponseBean.setErrorMessage(textERRORMESSAGE.item(0).getNodeValue());

                    }
                }

            } else {
                //succeed. merchant request is valid. continue process
                iPGMPIResponseBean.setValidFlag(true);

                //"MESSAGE" node
                NodeList listMESSAGE = doc.getElementsByTagName(CommonVarList.MESSAGE);
                for (int s = 0; s < listMESSAGE.getLength(); s++) {
                    Node nodeMESSAGE = listMESSAGE.item(s);
                    if (nodeMESSAGE.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementMESSAGE = (Element) nodeMESSAGE;

                        //"TRANSACTIONID" node
                        NodeList listTRANSACTIONID = elementMESSAGE.getElementsByTagName(CommonVarList.TRANSACTIONID);
                        Element elementTRANSACTIONID = (Element) listTRANSACTIONID.item(0);
                        NodeList textTRANSACTIONID = elementTRANSACTIONID.getChildNodes();
                        iPGMPIResponseBean.setTransactionId(textTRANSACTIONID.item(0).getNodeValue());

                        //"CAVV" node
                        NodeList listCAVV = elementMESSAGE.getElementsByTagName(CommonVarList.CAVV);
                        Element elementCAVV = (Element) listCAVV.item(0);
                        NodeList textCAVV = elementCAVV.getChildNodes();
                        iPGMPIResponseBean.setCavv(textCAVV.item(0).getNodeValue());

                        //"CVV" node
                        NodeList listCVV = elementMESSAGE.getElementsByTagName(CommonVarList.CVV);
                        Element elementCVV = (Element) listCVV.item(0);
                        NodeList textCVV = elementCVV.getChildNodes();
                        iPGMPIResponseBean.setCvv(textCVV.item(0).getNodeValue());

                        //"ECI" node
                        NodeList listECI = elementMESSAGE.getElementsByTagName(CommonVarList.ECI);
                        Element elementECI = (Element) listECI.item(0);
                        NodeList textECI = elementECI.getChildNodes();
                        iPGMPIResponseBean.setEci(textECI.item(0).getNodeValue());

                        //"TXNSTATUS" node
                        NodeList listTXNSTATUS = elementMESSAGE.getElementsByTagName(CommonVarList.TXNSTATUS);
                        Element elementTXNSTATUS = (Element) listTXNSTATUS.item(0);
                        NodeList textTXNSTATUS = elementTXNSTATUS.getChildNodes();
                        iPGMPIResponseBean.setTxnStatus(textTXNSTATUS.item(0).getNodeValue());
                    }
                }

            }

            return iPGMPIResponseBean;

        } catch (Exception e) {
            Logger.getLogger(MpiResponseManager.class.getName()).log(Level.SEVERE, null, e);
            throw new CustomException(CustomErrorVarList.XML_DOM_ERROR);
        }
    }
//check useage

    public String getMpiResponseStringData(String dataString) throws CustomException {

        Logger.getLogger(MpiResponseManager.class.getName()).log(Level.INFO, "called IPGMPITxnResponseServlet : getMpiResponseStringData");

        String status = null;
        try {

            String xml = dataString;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml + "")));

            doc.getDocumentElement().normalize();

            //"MESSAGE" node
            NodeList listIPG = doc.getElementsByTagName(CommonVarList.MESSAGE);
            for (int s = 0; s < listIPG.getLength(); s++) {
                Node nodeIPG = listIPG.item(s);
                if (nodeIPG.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementIPG = (Element) nodeIPG;

                    //"TXNSTATUS" node
                    NodeList listTRANSACTIONID = elementIPG.getElementsByTagName(CommonVarList.TXNSTATUS);
                    Element elementTRANSACTIONID = (Element) listTRANSACTIONID.item(0);
                    NodeList textTRANSACTIONID = elementTRANSACTIONID.getChildNodes();
                    status = textTRANSACTIONID.item(0).getNodeValue();

                }
            }
        } catch (DOMException ex) {
            Logger.getLogger(MpiResponseManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomErrorVarList.XML_DOM_ERROR);
        } catch (Exception e) {
            Logger.getLogger(MpiResponseManager.class.getName()).log(Level.SEVERE, null, e);
            throw new CustomException(CustomErrorVarList.XML_DOM_ERROR);
        }
        return status;
    }

    public String getMpiResponseTxnId(String dataString) throws CustomException {

        Logger.getLogger(MpiResponseManager.class.getName()).log(Level.INFO, "called IPGMPITxnResponseServlet : getMpiResponseTxnId");

        String transactionId = null;
        try {
            String xml = dataString;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml + "")));

            doc.getDocumentElement().normalize();

            //"MESSAGE" node
            NodeList listIPG = doc.getElementsByTagName(CommonVarList.MESSAGE);
            for (int s = 0; s < listIPG.getLength(); s++) {
                Node nodeIPG = listIPG.item(s);
                if (nodeIPG.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementIPG = (Element) nodeIPG;

                    //"transactionId" node
                    NodeList listTRANSACTIONID = elementIPG.getElementsByTagName(CommonVarList.TRANSACTIONID);
                    Element elementTRANSACTIONID = (Element) listTRANSACTIONID.item(0);
                    NodeList textTRANSACTIONID = elementTRANSACTIONID.getChildNodes();
                    transactionId = textTRANSACTIONID.item(0).getNodeValue();

                }
            }
        } catch (DOMException ex) {
            Logger.getLogger(MpiResponseManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomErrorVarList.XML_DOM_ERROR);
        } catch (Exception e) {
            Logger.getLogger(MpiResponseManager.class.getName()).log(Level.SEVERE, null, e);
            throw new CustomException(CustomErrorVarList.XML_DOM_ERROR);
        }
        return transactionId;
    }

    public void createPaymentReceipt(TransactionResponseBean ipgTxnResponseBean, HttpServletRequest request) throws CustomException {

        Logger.getLogger(MpiResponseManager.class.getName()).log(Level.INFO, "called MpiResponseController : redirectToMerchantAddon");

        MerchantResponseBean responseBean = merchantProcessDAO.getMerchantForResponseByTxnID(ipgTxnResponseBean.getTxnID());
        request.setAttribute("responseBean", responseBean);
        request.setAttribute("txnId", ipgTxnResponseBean.getTxnID());
        request.setAttribute("message", ipgTxnResponseBean.getMessage());
        request.setAttribute("isSuccessTxn", ipgTxnResponseBean.isIsValiedResponse());

    }

}
