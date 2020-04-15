/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.manager.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.EmailTemplateBean;
import com.epic.ipg.bean.payment.MerchantBean;
import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.bean.payment.MerchantRiskBean;
import com.epic.ipg.bean.payment.MerchantSendToMpiBean;
import com.epic.ipg.bean.payment.RuleBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.bean.payment.TransactionSearchBean;
import com.epic.ipg.dao.payment.MerchantProcessDAO;
import com.epic.ipg.util.common.Common;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.EmailService;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.common.SwitchClient;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.common.TDESEncryptor;
import com.epic.ipg.util.validations.CreditCardValidator;
import com.epic.ipg.util.validations.UserInputValidator;
import com.epic.ipg.util.varlist.CardAssociationVarList;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.EmailResponceCodes;
import com.epic.ipg.util.varlist.MessageVarList;
import com.epic.ipg.util.varlist.OperatorVarList;
import com.epic.ipg.util.varlist.RuleScopeVarList;
import com.epic.ipg.util.varlist.RuleTypeVarList;
import com.epic.ipg.util.varlist.StatusVarList;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
public class MerchantProcessManager {

    @Autowired
    MerchantProcessDAO merchantProcessDAO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    EmailService emailService;

    @Autowired
    SystemDateTime systemDateTime;

    @Autowired
    CreditCardValidator creditCardValidator;

    @Autowired
    LogFileCreator logFileCreator;

    public MerchantSendToMpiBean setTransactiondata(HttpSession sessionObj, TransactionBean iPGTransactionBean, HttpServletRequest request) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called IPGMerchantTransactionServlet : setTransactiondata");

        MerchantSendToMpiBean merchantSendToMpiBean = new MerchantSendToMpiBean();
        MerchantBean merchantBean;

        try {

            String reqHeader = request.getHeader("Accept");
            String reqUserAgent = request.getHeader("User-Agent");

            Timestamp nowDate = systemDateTime.getSystemDataAndTime();
            String nowTime = this.createCurrentDateTime(nowDate);

            merchantBean = (MerchantBean) sessionObj.getAttribute("merchantBean");

            merchantSendToMpiBean.setiPGTransactionId(iPGTransactionBean.getiPGTransactionId());
            merchantSendToMpiBean.setMerchantId(iPGTransactionBean.getMerchantId());
            merchantSendToMpiBean.setMerchantType(String.valueOf(sessionObj.getAttribute("merchantType")));
            merchantSendToMpiBean.setTerminalId(sessionObj.getAttribute("terminalId").toString());
            merchantSendToMpiBean.setPassword(sessionObj.getAttribute("password").toString());
            merchantSendToMpiBean.setPurchasedDateTime(nowTime);
            merchantSendToMpiBean.setStatusCode(StatusVarList.TXN_INITIATED);

            merchantSendToMpiBean.setAmount((long) iPGTransactionBean.getAmount());
            merchantSendToMpiBean.setCardAssociationCode(sessionObj.getAttribute("cardType").toString());
            if (request.getParameter("cardNumber") != null) {
                merchantSendToMpiBean.setCardHolderPan(request.getParameter("cardNumber").replace(" ", ""));
            }
            merchantSendToMpiBean.setCreatedTime(systemDateTime.getSystemDataAndTime());

            if (merchantSendToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.VISA)) {
                merchantSendToMpiBean.setCvcNumber(request.getParameter("cvcNum"));
            }
            if (merchantSendToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.MARSTERCARD)) {
                merchantSendToMpiBean.setCvvNumber(request.getParameter("cvvNum"));
            }
            if (merchantSendToMpiBean.getCardAssociationCode().equals(CardAssociationVarList.AMEX)) {
                merchantSendToMpiBean.setCidNumber(request.getParameter("cidNum"));
            }

            merchantSendToMpiBean.setExpiryDate(request.getParameter("year") + "" + request.getParameter("month"));
            merchantSendToMpiBean.setCardName(request.getParameter("firstname"));
            merchantSendToMpiBean.setBatchnumber(merchantProcessDAO.getBatchNumber(iPGTransactionBean.getMerchantId()));
            merchantSendToMpiBean.setCardHolderEmail(request.getParameter("cardholder_email"));
            merchantSendToMpiBean.setCardHolderMobile(request.getParameter("cardholder_mobile"));
            merchantSendToMpiBean.setCardHolderAddress(request.getParameter("cardholder_address"));
            merchantSendToMpiBean.setCardHolderCity(request.getParameter("cardholder_city"));

            String aquireBin = commonDAO.getCommonConfigValue(CommonVarList.ACQUIRERBIN);

            merchantSendToMpiBean.setAquBin(aquireBin);
            merchantSendToMpiBean.setMerchantRemarks("Remark");
            merchantSendToMpiBean.setMerchantRefNo(String.valueOf(sessionObj.getAttribute("txnRefNo")));
            merchantSendToMpiBean.setCurrencyCode(iPGTransactionBean.getCurrencyCode());
            merchantSendToMpiBean.setAcceptHeader(reqHeader);
            merchantSendToMpiBean.setDescription("Test");
            merchantSendToMpiBean.setUserAgent(reqUserAgent);
            merchantSendToMpiBean.setDeviceCategory("0");
            merchantSendToMpiBean.setExponent("0");
            merchantSendToMpiBean.setFrequency("0");
            merchantSendToMpiBean.setPrimaryUrl(merchantBean.getPrimaryUrl());
            merchantSendToMpiBean.setCountryCode(merchantBean.getCountryCode());
            merchantSendToMpiBean.setClientIp(iPGTransactionBean.getClientIp());
            merchantSendToMpiBean.setMerchantName(merchantBean.getMerchantName());
            merchantSendToMpiBean.setEndRecur("0");
            merchantSendToMpiBean.setDynamicreturnErrorUrl(merchantBean.getDynamicReturnErrorUrl());
            merchantSendToMpiBean.setTransactionCode((String) sessionObj.getAttribute("transactiontype"));

        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.TRANSACTION_DATA_SET_ERROR);
        }
        return merchantSendToMpiBean;
    }

    public String createCurrentDateTime(Timestamp dateInput) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called IPGMerchantTransactionServlet : createCurrentDateTime");
        String currentdatetime = "";

        try {
            String dateFormat = "yyyyMMdd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(dateInput.getTime());
            currentdatetime = sdf.format(date);
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.INV_DATETIME);
        }
        return currentdatetime;
    }

    public String createCurrentDateTimeShort(Timestamp dateInput) throws CustomException {
        String currentdatetime = "";

        String dateFormat = "MMddHHmmss";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(dateInput.getTime());
            currentdatetime = sdf.format(date);
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.INV_DATETIME);
        }

        return currentdatetime;
    }

    public boolean validateExpiry(String cardMonth, String cardYear) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessValidator : validateExpiry");
        boolean valid = false;

        try {
            //get now month and year
            String dd = this.getDateInTwoDigits();

            int year = Integer.parseInt(dd.substring(0, 2));
            int month = Integer.parseInt(dd.substring(3));

            //check whether month between 1 and 12
            if (Integer.parseInt(cardMonth) > 0 && Integer.parseInt(cardMonth) < 13) {
                //check with now date Year
                if (Integer.parseInt(cardYear) == year) {

                    //if year is this year check with month
                    if (Integer.parseInt(cardMonth) >= month) {
                        valid = true;
                    }
                } else if (Integer.parseInt(cardYear) > year) {
                    valid = true;
                }
            }

            return valid;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.VALIDATE_EXPIRY_ERROR);
        }
    }

    public String getDateInTwoDigits() throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessValidator : getDateInTwoDigits");
        String nowMonthYear = "";
        try {

            Timestamp nowDate = systemDateTime.getSystemDataAndTime();
            String dateFormat = "yy-MM";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(nowDate.getTime());
            nowMonthYear = sdf.format(date);

        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.INV_DATETIME);
        }
        return nowMonthYear;
    }

    public void inputValidate(HttpServletRequest request, String cardType, String code, String captcha) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called IPGMerchantTransactionServlet : inputValidate");

        String codeColor = "codeColor";
        String codeClass = "codeClass";
        String nameClass = "nameClass";
        String nameColor = "nameColor";
        String cardColor = "cardColor";
        String cardClass = "cardClass";
        String monthColor = "monthColor";
        String monthClass = "monthClass";
        String cvcColor = "cvcColor";
        String cvcClass = "cvcClass";
        String black = "black";
        String red = "red";
        String ipgErrorDetails = "mandatory_msgsec";
        String ipgErrorlabel = "mandatory_msgsec_label ";
        String errorClass = " error ";

        try {

            if (UserInputValidator.isNonNumericNonSpecialString(request.getParameter("firstname"))) {

                request.setAttribute(nameColor, black);
                request.setAttribute(nameClass, "");

            } else {

                request.setAttribute(nameColor, ipgErrorlabel);
                request.setAttribute(nameClass, errorClass);

            }
            if (creditCardValidator.validationCriteria(request.getParameter("cardNumber").replace(" ", ""), cardType)) {

                request.setAttribute(cardColor, black);
                request.setAttribute(cardClass, "");

            } else {

                request.setAttribute(cardColor, ipgErrorlabel);
                request.setAttribute(cardClass, errorClass);

            }

            String month = request.getParameter("month");

            if (UserInputValidator.isNumeric(month) && UserInputValidator.isNumeric(request.getParameter("year")) && validateExpiry(month, request.getParameter("year"))) {

                request.setAttribute(monthColor, black);
                request.setAttribute(monthClass, "");

            } else {

                request.setAttribute(monthColor, ipgErrorlabel);
                request.setAttribute(monthClass, errorClass);

            }

            if (cardType.equals(CardAssociationVarList.VISA)) {
                if (UserInputValidator.isNumeric(request.getParameter("cvcNum"))) {

                    request.setAttribute(cvcColor, black);
                    request.setAttribute(cvcClass, "");

                } else {

                    request.setAttribute(cvcColor, ipgErrorlabel);
                    request.setAttribute(cvcClass, errorClass);

                }
            }

            if (cardType.equals(CardAssociationVarList.MARSTERCARD)) {
                if (UserInputValidator.isNumeric(request.getParameter("cvvNum"))) {

                    request.setAttribute(cvcColor, black);
                    request.setAttribute(cvcClass, "");

                } else {

                    request.setAttribute(cvcColor, ipgErrorlabel);
                    request.setAttribute(cvcClass, errorClass);

                }
            }

            if (cardType.equals(CardAssociationVarList.AMEX)) {
                if (UserInputValidator.isNumeric(request.getParameter("cidNum"))) {

                    request.setAttribute(cvcColor, black);
                    request.setAttribute(cvcClass, "");

                } else {

                    request.setAttribute(cvcColor, ipgErrorlabel);
                    request.setAttribute(cvcClass, errorClass);

                }
            }

            if (code == null || code.isEmpty()) {

                request.setAttribute(codeColor, ipgErrorlabel);
                request.setAttribute(codeClass, "");

            } else {

                request.setAttribute(codeColor, black);
                request.setAttribute(codeClass, errorClass);

            }

            if (UserInputValidator.isCaptchaCorrect(code, captcha)) {

                request.setAttribute(codeColor, black);
                request.setAttribute(codeClass, "");

            } else {

                request.setAttribute(codeColor, ipgErrorlabel);
                request.setAttribute(codeClass, errorClass);

            }

            request.setAttribute("displayMessage", MessageVarList.CHECK_DATA_FIELDS_AGAIN);

        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.VALIDATE_INPUT_DATA);
        }
    }

    public List validateWithRiskParam(MerchantRiskBean merchantRiskBean, MerchantSendToMpiBean merchantToMpiBean) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessController : validateWithRiskParam");

        List valList = new ArrayList<>();

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            if (!merchantRiskBean.getCountry().isEmpty() && merchantRiskBean.getCountry().contains(merchantRiskBean.getMerCountry())) {
                valList.add(false);
                valList.add(MessageVarList.COUNTRY_BLOCKED);

            } else if (!merchantRiskBean.getBin().isEmpty() && merchantRiskBean.getBin().contains(merchantToMpiBean.getCardHolderPan().substring(0, 6))) {
                valList.add(false);
                valList.add(MessageVarList.BIN_BLOCKED);

            } else if (!merchantRiskBean.getCurrency().isEmpty() && merchantRiskBean.getCurrency().contains(merchantToMpiBean.getCurrencyCode())) {
                valList.add(false);
                valList.add(MessageVarList.CURRENCY_BLOCKED);

            } else if (merchantToMpiBean.getAmount() > Double.parseDouble(merchantRiskBean.getMaxSingleTxnLimit())) {
                valList.add(false);
                valList.add(MessageVarList.MAX_TXN_LIMIT_EXCEED);

            } else if (merchantToMpiBean.getAmount() < Double.parseDouble(merchantRiskBean.getMinSingleTxnLimit())) {
                valList.add(false);
                valList.add(MessageVarList.BELOW_MIN_TXN_LIMIT);

            } else if ((this.getTodayTotalTxnAmount(currentDate, merchantToMpiBean.getMerchantId()) + merchantToMpiBean.getAmount()) > Double.parseDouble(merchantRiskBean.getMaxDailyTxnAmount())) {
                valList.add(false);
                valList.add(MessageVarList.MAX_TOTAL_TXN_LIMIT_EXCEED);

            } else if ((this.getTodayTotalTxnCount(currentDate, merchantToMpiBean.getMerchantId()) + 1) > Integer.parseInt(merchantRiskBean.getMaxDailyTxnCount())) {
                valList.add(false);
                valList.add(MessageVarList.MAX_TXN_COUNT_EXCEED);

            } else {
                // this is when validation success
                valList.add(true);
            }

            return valList;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.VALIDATE_RISK_ERROR);
        }
    }

    private double getTodayTotalTxnAmount(String currentDate, String merchantId) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessController : getTodayTotalTxnAmount");

        try {
            double amount;
            amount = merchantProcessDAO.getTodayTotalTxnAmount(merchantId, currentDate);
            return amount;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.INV_TOTAL_AMOUNT);
        }

    }

    private int getTodayTotalTxnCount(String currentDate, String merchantId) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessController : getTodayTotalTxnCount");
        try {
            int txnCount;
            txnCount = merchantProcessDAO.getTodayTotalTxnCount(merchantId, currentDate);
            return txnCount;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.INV_TOTAL_COUNT);
        }

    }

    //------------------------------------Mpi--------------------------------------------------------------------
    public boolean getMpiServerAvailability() {
        boolean reachable;
        try (Socket socket = new Socket(commonDAO.getCommonConfigValue(CommonVarList.MPISERVERIP), Integer.parseInt(commonDAO.getCommonConfigValue(CommonVarList.MPISERVERPORT)))) {
            reachable = true;
        } catch (Exception e) {
            reachable = false;
        }
        return reachable;
    }

    //create VReq+PAReq message for the mpi
    public String generateRequestMessage() {
        String message;

        message = "<?xml version=\"1.0\" encoding=\'UTF-8\'?>"
                + "<MPIREQUEST>"
                + "<IPG>"
                + "<TRANSACTIONID>DEFAUT</TRANSACTIONID>"
                + "<IURL>DEFAUT</IURL>"
                + "</IPG>"
                + "<MERCHANT>"
                + "<MERID>DEFAUT</MERID>"
                + "<PASSWORD>DEFAUT</PASSWORD>"
                + "<AQUBIN>DEFAUT</AQUBIN>"
                + "<NAME>DEFAUT</NAME>"
                + "<COUNTRY>DEFAUT</COUNTRY>"
                + "<MURL>DEFAUT</MURL>"
                + "<CARDASSOCIATION>DEFAUT</CARDASSOCIATION>"
                + "</MERCHANT>"
                + "<BROWSER>"
                + "<DEVICECATEGORY>DEFAUT</DEVICECATEGORY>"
                + "<ACCEPTHEADER>DEFAUT</ACCEPTHEADER>"
                + "<USERAGENT>DEFAUT</USERAGENT>"
                + "</BROWSER>"
                + "<CARDHOLDER>"
                + "<CARDHOLDERPAN>DEFAUT</CARDHOLDERPAN>"
                + "<CARDNAME>DEFAUT</CARDNAME>"
                + "<CARDEXPIRY>DEFAUT</CARDEXPIRY>"
                + "</CARDHOLDER>"
                + "<PURCHASE>"
                + "<MERCHANTREFNO>DEFAUT</MERCHANTREFNO>"
                + "<DATE>DEFAUT</DATE>"
                + "<AMOUNT>DEFAUT</AMOUNT>"
                + "<CURRENCY>DEFAUT</CURRENCY>"
                + "<EXPONENT>DEFAUT</EXPONENT>"
                + "<DESC>DEFAUT</DESC>"
                + "<RECUR>"
                + "<FREQUENCY>DEFAUT</FREQUENCY>"
                + "<ENDRECUR>DEFAUT</ENDRECUR>"
                + "</RECUR>"
                + "</PURCHASE>"
                + "</MPIREQUEST>";

        return message;
    }

    //then add txn data to the generated xml message
    public String fillRequestMessage(String generatedXmlMessage, MerchantSendToMpiBean merchantSendToMpiBean, String ipgEngineUrl) throws Exception {

        String txnId;
        String ipgUrl;
        String merchantId;
        String password;
        String aquBin;
        String name;
        String country;
        String merchantUrl;
        String cardAssociation;
        String deviceCategory;
        String acceptHeader;
        String userAgent;
        String cardHolderPan;
        String cardName;
        String cardExpiry;
        String merchantRefNo;
        String date;
        String currency;
        String exponent;
        String desc;
        String frequency;
        String endRecur;
        String ipgRunningUrl;
        String messageWithTxnData;

        long amount;

        //read xml file & get ipg server configuration
        ipgRunningUrl = ipgEngineUrl;

        String xml = generatedXmlMessage;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml + "")));

        doc.getDocumentElement().normalize();

        //"IPG" node
        NodeList listIPG = doc.getElementsByTagName("IPG");
        for (int s = 0; s < listIPG.getLength(); s++) {
            Node nodeIPG = listIPG.item(s);
            if (nodeIPG.getNodeType() == Node.ELEMENT_NODE) {
                Element elementIPG = (Element) nodeIPG;

                try {
                    //"TRANSACTIONID" node
                    NodeList listTRANSACTIONID = elementIPG.getElementsByTagName(CommonVarList.TRANSACTIONID);
                    Element elementTRANSACTIONID = (Element) listTRANSACTIONID.item(0);
                    NodeList textTRANSACTIONID = elementTRANSACTIONID.getChildNodes();
                    txnId = merchantSendToMpiBean.getiPGTransactionId();
                    textTRANSACTIONID.item(0).setNodeValue(txnId);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"URL" node
                    NodeList listURL = elementIPG.getElementsByTagName(CommonVarList.IURL);
                    Element elementURL = (Element) listURL.item(0);
                    NodeList textURL = elementURL.getChildNodes();
                    ipgUrl = ipgRunningUrl;
                    textURL.item(0).setNodeValue(ipgUrl);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
        }

        //"MERCHANT" node
        NodeList listMERCHANT = doc.getElementsByTagName(CommonVarList.MERCHANT);
        for (int s = 0; s < listMERCHANT.getLength(); s++) {
            Node nodeMERCHANT = listMERCHANT.item(s);
            if (nodeMERCHANT.getNodeType() == Node.ELEMENT_NODE) {
                Element elementMERCHANT = (Element) nodeMERCHANT;

                try {
                    //"MERID" node
                    NodeList listMERID = elementMERCHANT.getElementsByTagName(CommonVarList.MERID);
                    Element elementMERID = (Element) listMERID.item(0);
                    NodeList textMERID = elementMERID.getChildNodes();
                    merchantId = String.valueOf(merchantSendToMpiBean.getMerchantId());
                    textMERID.item(0).setNodeValue(merchantId);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"PASSWORD" node
                    NodeList listPASSWORD = elementMERCHANT.getElementsByTagName(CommonVarList.PASSWORD);
                    Element elementPASSWORD = (Element) listPASSWORD.item(0);
                    NodeList textPASSWORD = elementPASSWORD.getChildNodes();
                    password = merchantSendToMpiBean.getPassword();
                    textPASSWORD.item(0).setNodeValue(password);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"AQUBIN" node
                    NodeList listAQUBIN = elementMERCHANT.getElementsByTagName(CommonVarList.AQUBIN);
                    Element elementAQUBIN = (Element) listAQUBIN.item(0);
                    NodeList textAQUBIN = elementAQUBIN.getChildNodes();
                    aquBin = merchantSendToMpiBean.getAquBin();
                    textAQUBIN.item(0).setNodeValue(aquBin);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"NAME" node
                    NodeList listNAME = elementMERCHANT.getElementsByTagName(CommonVarList.NAME);
                    Element elementNAME = (Element) listNAME.item(0);
                    NodeList textNAME = elementNAME.getChildNodes();
                    name = merchantSendToMpiBean.getMerchantName();
                    textNAME.item(0).setNodeValue(name);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"COUNTRY" node
                    NodeList listCOUNTRY = elementMERCHANT.getElementsByTagName(CommonVarList.COUNTRY);
                    Element elementCOUNTRY = (Element) listCOUNTRY.item(0);
                    NodeList textCOUNTRY = elementCOUNTRY.getChildNodes();
                    country = merchantSendToMpiBean.getCountryCode();
                    textCOUNTRY.item(0).setNodeValue(country);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"MURL" node
                    NodeList listMURL = elementMERCHANT.getElementsByTagName(CommonVarList.MURL);
                    Element elementMURL = (Element) listMURL.item(0);
                    NodeList textMURL = elementMURL.getChildNodes();
                    merchantUrl = merchantSendToMpiBean.getPrimaryUrl();
                    textMURL.item(0).setNodeValue(merchantUrl);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"CARDASSOCIATION" node
                    NodeList listCARDASSOCIATION = elementMERCHANT.getElementsByTagName(CommonVarList.CARDASSOCIATION);
                    Element elementCARDASSOCIATION = (Element) listCARDASSOCIATION.item(0);
                    NodeList textCARDASSOCIATION = elementCARDASSOCIATION.getChildNodes();
                    cardAssociation = merchantSendToMpiBean.getCardAssociationCode();
                    textCARDASSOCIATION.item(0).setNodeValue(cardAssociation);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
        }

        //"BROWSER" node
        NodeList listBROWSER = doc.getElementsByTagName(CommonVarList.BROWSER);
        for (int s = 0; s < listBROWSER.getLength(); s++) {
            Node nodeBROWSER = listBROWSER.item(s);
            if (nodeBROWSER.getNodeType() == Node.ELEMENT_NODE) {
                Element elementBROWSER = (Element) nodeBROWSER;

                try {
                    //"DEVICECATEGORY" node
                    NodeList listDEVICECATEGORY = elementBROWSER.getElementsByTagName(CommonVarList.DEVICECATEGORY);
                    Element elementDEVICECATEGORY = (Element) listDEVICECATEGORY.item(0);
                    NodeList textDEVICECATEGORY = elementDEVICECATEGORY.getChildNodes();
                    deviceCategory = merchantSendToMpiBean.getDeviceCategory();
                    textDEVICECATEGORY.item(0).setNodeValue(deviceCategory);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"ACCEPTHEADER" node
                    NodeList listACCEPTHEADER = elementBROWSER.getElementsByTagName(CommonVarList.ACCEPTHEADER);
                    Element elementACCEPTHEADER = (Element) listACCEPTHEADER.item(0);
                    NodeList textTRANSACTIONID = elementACCEPTHEADER.getChildNodes();
                    acceptHeader = merchantSendToMpiBean.getAcceptHeader();
                    textTRANSACTIONID.item(0).setNodeValue(acceptHeader);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"USERAGENT" node
                    NodeList listUSERAGENT = elementBROWSER.getElementsByTagName(CommonVarList.USERAGENT);
                    Element elementUSERAGENT = (Element) listUSERAGENT.item(0);
                    NodeList textUSERAGENT = elementUSERAGENT.getChildNodes();
                    userAgent = merchantSendToMpiBean.getUserAgent();
                    textUSERAGENT.item(0).setNodeValue(userAgent);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        //"CARDHOLDER" node
        NodeList listCARDHOLDER = doc.getElementsByTagName(CommonVarList.CARDHOLDER);
        for (int s = 0; s < listCARDHOLDER.getLength(); s++) {
            Node nodeCARDHOLDER = listCARDHOLDER.item(s);
            if (nodeCARDHOLDER.getNodeType() == Node.ELEMENT_NODE) {
                Element elementCARDHOLDER = (Element) nodeCARDHOLDER;

                try {
                    //"CARDHOLDERPAN" node
                    NodeList listCARDHOLDERPAN = elementCARDHOLDER.getElementsByTagName(CommonVarList.CARDHOLDERPAN);
                    Element elementCARDHOLDERPAN = (Element) listCARDHOLDERPAN.item(0);
                    NodeList textCARDHOLDERPAN = elementCARDHOLDERPAN.getChildNodes();
                    cardHolderPan = merchantSendToMpiBean.getCardHolderPan();
                    textCARDHOLDERPAN.item(0).setNodeValue(cardHolderPan);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }

                try {
                    //"CARDNAME" node
                    NodeList listCARDNAME = elementCARDHOLDER.getElementsByTagName(CommonVarList.CARDNAME);
                    Element elementCARDNAME = (Element) listCARDNAME.item(0);
                    NodeList textCARDNAME = elementCARDNAME.getChildNodes();
                    cardName = merchantSendToMpiBean.getCardName();
                    textCARDNAME.item(0).setNodeValue(cardName);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"CARDEXPIRY" node
                    NodeList listCARDEXPIRY = elementCARDHOLDER.getElementsByTagName(CommonVarList.CARDEXPIRY);
                    Element elementCARDEXPIRY = (Element) listCARDEXPIRY.item(0);
                    NodeList textCARDEXPIRY = elementCARDEXPIRY.getChildNodes();
                    cardExpiry = merchantSendToMpiBean.getExpiryDate();
                    textCARDEXPIRY.item(0).setNodeValue(cardExpiry);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        //"PURCHASE" node
        NodeList listPURCHASE = doc.getElementsByTagName(CommonVarList.PURCHASE);
        for (int s = 0; s < listPURCHASE.getLength(); s++) {
            Node nodePURCHASE = listPURCHASE.item(s);
            if (nodePURCHASE.getNodeType() == Node.ELEMENT_NODE) {
                Element elementPURCHASE = (Element) nodePURCHASE;

                try {
                    //"MERCHANTREFNO" node
                    NodeList listMERCHANTREFNO = elementPURCHASE.getElementsByTagName(CommonVarList.MERCHANTREFNO);
                    Element elementMERCHANTREFNO = (Element) listMERCHANTREFNO.item(0);
                    NodeList textMERCHANTREFNO = elementMERCHANTREFNO.getChildNodes();
                    merchantRefNo = merchantSendToMpiBean.getMerchantRefNo();
                    textMERCHANTREFNO.item(0).setNodeValue(merchantRefNo);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"DATE" node
                    NodeList listDATE = elementPURCHASE.getElementsByTagName(CommonVarList.DATE);
                    Element elementDATE = (Element) listDATE.item(0);
                    NodeList textDATE = elementDATE.getChildNodes();
                    date = merchantSendToMpiBean.getPurchasedDateTime();
                    textDATE.item(0).setNodeValue(date);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"AMOUNT" node
                    NodeList listAMOUNT = elementPURCHASE.getElementsByTagName(CommonVarList.AMOUNT);
                    Element elementAMOUNT = (Element) listAMOUNT.item(0);
                    NodeList textAMOUNT = elementAMOUNT.getChildNodes();
                    amount = merchantSendToMpiBean.getAmount();
                    textAMOUNT.item(0).setNodeValue(String.valueOf(amount));
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"CURRENCY" node
                    NodeList listCURRENCY = elementPURCHASE.getElementsByTagName(CommonVarList.CURRENCY);
                    Element elementCURRENCY = (Element) listCURRENCY.item(0);
                    NodeList textCURRENCY = elementCURRENCY.getChildNodes();
                    currency = merchantSendToMpiBean.getCurrencyCode();
                    textCURRENCY.item(0).setNodeValue(currency);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"EXPONENT" node
                    NodeList listEXPONENT = elementPURCHASE.getElementsByTagName(CommonVarList.EXPONENT);
                    Element elementEXPONENT = (Element) listEXPONENT.item(0);
                    NodeList textEXPONENT = elementEXPONENT.getChildNodes();
                    exponent = merchantSendToMpiBean.getExponent();
                    textEXPONENT.item(0).setNodeValue(exponent);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    //"DESC" node
                    NodeList listDESC = elementPURCHASE.getElementsByTagName(CommonVarList.DESC);
                    Element elementDESC = (Element) listDESC.item(0);
                    NodeList textDESC = elementDESC.getChildNodes();
                    desc = merchantSendToMpiBean.getDescription();
                    textDESC.item(0).setNodeValue(desc);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                //"RECUR" node
                NodeList listRECUR = doc.getElementsByTagName(CommonVarList.RECUR);
                for (int k = 0; k < listRECUR.getLength(); k++) {
                    Node nodeRECUR = listRECUR.item(s);
                    if (nodeRECUR.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementRECUR = (Element) nodeRECUR;

                        try {
                            //"FREQUENCY" node
                            NodeList listFREQUENCY = elementRECUR.getElementsByTagName(CommonVarList.FREQUENCY);
                            Element elementFREQUENCY = (Element) listFREQUENCY.item(0);
                            NodeList textFREQUENCY = elementFREQUENCY.getChildNodes();
                            frequency = merchantSendToMpiBean.getFrequency();
                            textFREQUENCY.item(0).setNodeValue(frequency);
                        } catch (DOMException ex) {
                            Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            //"ENDRECUR" node
                            NodeList listENDRECUR = elementRECUR.getElementsByTagName(CommonVarList.ENDRECUR);
                            Element elementENDRECUR = (Element) listENDRECUR.item(0);
                            NodeList textENDRECUR = elementENDRECUR.getChildNodes();
                            endRecur = merchantSendToMpiBean.getEndRecur();
                            textENDRECUR.item(0).setNodeValue(endRecur);
                        } catch (DOMException ex) {
                            Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

            }
        }

        //add new changes to the xml string/file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

        messageWithTxnData = result.getWriter().toString();

        return messageWithTxnData;
    }

    //communicate with transaction switch
    public List communicateWithTxnSwitch(String switchRequestMessage, String txnId, String merchantId, CommonFilePaths commonFilePaths) throws CustomException {

        Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.INFO, "called MerchantProcessValidator : communicateWithTxnSwitch");
        SwitchClient switchClient = null;
        String logMessage = "| Merchant ID : " + merchantId + "|";

        try {

            List lst;
            lst = new ArrayList();
            switchClient = new SwitchClient(commonDAO);
            String serverResponse;

            //write info log. for write switch request sent to ipg by switch
            logFileCreator.writInforTologsForSwitchRequest(logMessage + switchRequestMessage, commonFilePaths.getSwitchrequest());
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + logMessage + " IPG Engine request for the IPG to IPG Engine \n" + switchRequestMessage, commonFilePaths.getInforLogPath());

            //update transaction status
            merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_ENGINEREQUESTSENT, null);

            switchClient.sendPacket(switchRequestMessage);
            serverResponse = switchClient.receivePacket();

            //write info log. for write switch response sent to ipg by switch___________________________________________________________
            logFileCreator.writInforTologsForSwitchResponse(logMessage + serverResponse, commonFilePaths.getSwitchresponse());
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + logMessage + "| IPG Engine response for the IPG Engine to IPG \n" + serverResponse, commonFilePaths.getInforLogPath());

            merchantProcessDAO.updateTransactionStage(txnId, StatusVarList.TXN_ENGINERESRECEIVED, null);

            if (serverResponse != null) {
                //" Txn code|Txn id|Response code "
                StringTokenizer tk = new StringTokenizer(serverResponse, "|");
                if (tk.countTokens() != 3) {
                    lst.add(false);
                    lst.add(MessageVarList.INVALID_TXN_SWITCH_RESPONSE);
                } else {
                    lst.add(true); //txn switch communication successfully completed.
                    lst.add(tk.nextToken());
                    lst.add(tk.nextToken());
                    lst.add(tk.nextToken());
                }
            } else {
                lst.add(false);
                lst.add(MessageVarList.INVALID_TXN_SWITCH_RESPONSE);
            }
            return lst;

        } finally {
            if (switchClient != null) {
                switchClient.closeAll();
            }
        }
        //end handle socket communication--------------------------------------------------------------------
    }
    //create txn request message for transaction switch

    public String createRequestMessageForTxnSwitch(String txnId) throws CustomException {

        String merchant;
        Timestamp purchaseDate;
        String message;
        try {

            TransactionBean iPGTransactionBean = merchantProcessDAO.getTxnData(txnId);
            merchant = iPGTransactionBean.getMerchantId();
            purchaseDate = iPGTransactionBean.getPurchasedDateTime();
            message = "01" + "|" + txnId + "|" + merchant + "|" + createCurrentDateTimeShort(purchaseDate);
            return message;

        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.ERROR_MESSAGE_CREATION);
        }

    }

    public String sendOrderStatusEmail(String txnId, String txnStatus, String txnMessage, CommonFilePaths commonFilePaths) {

        String message;
        String logMessage = this.getClass().getSimpleName() + " | " + txnMessage;

        try {

            if ("YES".equals(commonDAO.getCommonConfigValue(CommonVarList.EMAILSTATUS))) {

                EmailTemplateBean emailTemplateBean = new EmailTemplateBean();
                emailTemplateBean.setMessage(this.createEmailBody(txnId, txnStatus));
                emailTemplateBean.setSubject(CommonVarList.EMAIL_SUBJECT);

                String email = merchantProcessDAO.getCustomerEmail(txnId);

                if (email == null || email.isEmpty()) {
                    message = txnStatus + MessageVarList.EMAIL_SENDING_FAIL_EMPTY_EMAIL;
                    //write infor log.__________________________________________________________________________________________________________
                    logFileCreator.writInforTologs(logMessage + MessageVarList.EMAIL_SENDING_FAIL_EMPTY_EMAIL, commonFilePaths.getInforLogPath());
                } else {

                    String emailMsg = emailService.sendMailToCustomer(email, emailTemplateBean, commonFilePaths);

                    if (emailMsg.equals(EmailResponceCodes.PROCESS_SUCCESS)) {

                        message = txnStatus;
                        //write infor log.__________________________________________________________________________________________________________
                        logFileCreator.writInforTologs(logMessage, commonFilePaths.getInforLogPath());

                    } else if (emailMsg.equals(EmailResponceCodes.UNABLE_TO_PROCESS_MAIL)) {

                        message = txnStatus + MessageVarList.EMAIL_SENDING_FAIL;
                        //write infor log.__________________________________________________________________________________________________________
                        logFileCreator.writInforTologs(logMessage + MessageVarList.EMAIL_SENDING_FAIL, commonFilePaths.getInforLogPath());
                    } else {

                        message = txnStatus + MessageVarList.EMAIL_SENDING_ERROR;
                        //write infor log.__________________________________________________________________________________________________________
                        logFileCreator.writInforTologs(logMessage + MessageVarList.EMAIL_SENDING_ERROR, commonFilePaths.getInforLogPath());
                    }
                }
            } else {
                message = txnStatus + MessageVarList.EMAIL_SERVICE_DISABLED;
                logFileCreator.writInforTologs(logMessage + MessageVarList.EMAIL_SERVICE_DISABLED, commonFilePaths.getInforLogPath());

            }
        } catch (Exception ee) {
            // should skip for email exceptions
            message = txnStatus + MessageVarList.EMAIL_SENDING_ERROR;
            logFileCreator.writInforTologs(logMessage + MessageVarList.EMAIL_SENDING_ERROR, commonFilePaths.getInforLogPath());
            logFileCreator.writeErrorToLog(ee.fillInStackTrace(), commonFilePaths.getErrorLogPath());

        }
        return message;
    }

    //do rule validation process.
    public String validateRule(String ipgTrigerSequence, MerchantSendToMpiBean iPGMerchantSendToMpiBean) throws Exception {

        String message = "";
        String validateMessage = "";
        List<RuleBean> iPGMerchantRuleLst = null;

        //first get IPG roles according to the triger sequence id
        List<RuleBean> iPGRuleLst = merchantProcessDAO.getIpgRules(ipgTrigerSequence);

        /**
         * check transaction initiated status using merchant id then get IPG
         * merchant roles according to the triger sequence id
         */
        if ("YES".equals(merchantProcessDAO.getTransactionInitiatedStatus(iPGMerchantSendToMpiBean.getMerchantId()))) {
            String defaultMerchant = merchantProcessDAO.getDefaultMerchant(iPGMerchantSendToMpiBean.getMerchantId());
            iPGMerchantRuleLst = merchantProcessDAO.getIpgMerchantRules(ipgTrigerSequence, defaultMerchant);
        } else {
            iPGMerchantRuleLst = merchantProcessDAO.getIpgMerchantRules(ipgTrigerSequence, iPGMerchantSendToMpiBean.getMerchantId());
        }

        //validate IPG rules, one by one
        if (!iPGRuleLst.isEmpty()) {

            validateMessage = this.validateIPGRuleLogic(iPGMerchantSendToMpiBean, iPGRuleLst, "ipg");

            if (!validateMessage.isEmpty()) {
                message = validateMessage + " defined by ipg rule";
            } else if (validateMessage.isEmpty() && !iPGMerchantRuleLst.isEmpty()) {

                validateMessage = this.validateIPGRuleLogic(iPGMerchantSendToMpiBean, iPGMerchantRuleLst, "merchant");

                if (!validateMessage.isEmpty()) {
                    message = validateMessage + " defined by merchant rule";
                }
            }

        } else if (!iPGMerchantRuleLst.isEmpty()) {
            validateMessage = this.validateIPGRuleLogic(iPGMerchantSendToMpiBean, iPGMerchantRuleLst, "merchant");

            if (!validateMessage.isEmpty()) {
                message = validateMessage + " defined by merchant rule";
            }
        }

        return message;
    }

    private String validateIPGRuleLogic(MerchantSendToMpiBean iPGMerchantSendToMpiBean, List<RuleBean> iPGRuleLst, String ruleType) throws CustomException {

        String messageString = "";
        TransactionSearchBean iPGTransactionSearchBean;
        List<TransactionBean> lst;
        int txnCount = 0;

        int aquBin = Integer.parseInt(iPGMerchantSendToMpiBean.getCardHolderPan().substring(0, 6));
        String cardHolder = iPGMerchantSendToMpiBean.getCardHolderPan();
        Long cardNumber = Long.parseLong(iPGMerchantSendToMpiBean.getCardHolderPan());
        String requestedUrl = iPGMerchantSendToMpiBean.getPrimaryUrl();

        //start read one by one according to IPG RULE SCOPE -------------------------------
        for (int m = 0; m < iPGRuleLst.size(); m++) {

            //key items
            String currentRuleScope = iPGRuleLst.get(m).getRuleScope();
            String currentOperator = iPGRuleLst.get(m).getOperator();
            String currentRuleType = iPGRuleLst.get(m).getRuleType();

            //value items
            String currentStartValue = iPGRuleLst.get(m).getStartValue();
            String currentEndValue = iPGRuleLst.get(m).getEndValue();

            //Start Bin Range -------------------------------------------------------------
            if (currentRuleScope.contentEquals(RuleScopeVarList.BINRANGE) && (currentRuleType.contentEquals(RuleTypeVarList.BLACKLIST))) {

                //Process "BLK-Blacklist"
                // > ------ Grater
                int intCurrentStartValue = Integer.parseInt(currentStartValue);
                int intCurrentEndValue = Integer.parseInt(currentEndValue);

                if (currentOperator.contentEquals(OperatorVarList.GRATER) && (aquBin > intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_BIN_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // >= ------ Grater or Equal
                if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (aquBin >= intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_BIN_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // < ------ Less
                if (currentOperator.contentEquals(OperatorVarList.LESS) && (aquBin < intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_BIN_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // <= ------ Less or Equal
                if (currentOperator.contentEquals(OperatorVarList.LESS_OR_EQUAL) && (aquBin <= intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_BIN_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // <> ------ Between
                if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && ((intCurrentStartValue < aquBin) && (aquBin < intCurrentEndValue))) {
                    messageString = MessageVarList.INVALID_BIN_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }

            }
            //End Bin Range -------------------------------------------------------------

            //Start Card Holder -------------------------------------------------------------
            if (currentRuleScope.contentEquals(RuleScopeVarList.CARDHOLDER)) {
                if (currentRuleType.contentEquals(RuleTypeVarList.BLACKLIST)) {

                    int intcardHolder = Integer.parseInt(cardHolder);
                    int intCurrentStartValue = Integer.parseInt(currentStartValue);
                    int intCurrentEndValue = Integer.parseInt(currentEndValue);
                    long bgintcardHolder = Long.parseLong(cardHolder);
                    long bgintCurrentStartValue = Long.parseLong(currentStartValue);

                    //Process "BLK-Blacklist"
                    // = ------ Equal
                    if (currentOperator.contentEquals(OperatorVarList.EQUAL) && (currentStartValue.contentEquals(cardHolder))) {
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                    // > ------ Grater
                    if (currentOperator.contentEquals(OperatorVarList.GRATER) && (intcardHolder > intCurrentStartValue)) {
                        //write info log. for write ipg rule validation status
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                    // >= ------ Grater or Equal
                    if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (intcardHolder >= intCurrentStartValue)) {
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;

                    }
                    // < ------ Less
                    if (currentOperator.contentEquals(OperatorVarList.LESS) && (bgintcardHolder < bgintCurrentStartValue)) {
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                    // <= ------ Less or Equal
                    if (currentOperator.contentEquals(OperatorVarList.LESS_OR_EQUAL) && (intcardHolder <= intCurrentStartValue)) {
                        //write info log. for write ipg rule validation status
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                    // <> ------ Between
                    if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && !((intCurrentStartValue < intcardHolder) && (intcardHolder < intCurrentEndValue))) {
                        messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                } /**
                 * check per transaction amount by card holder pan
                 */
                else if (currentRuleType.contentEquals(RuleTypeVarList.VALUE)) {

                    //start form the used entered txn type
                    double txnAmountCount = iPGMerchantSendToMpiBean.getAmount();

                    double doubleCurrentStartValue = Double.parseDouble(currentStartValue);
                    double doubleCurrentEndValue = Double.parseDouble(currentEndValue);

                    //validate with txn amount per data for a particular customer
                    //remove == and  <=  logics (they are not valied)
                    // < ------ Less
                    if (currentOperator.contentEquals(OperatorVarList.LESS) && (txnAmountCount < doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_LESS_MIN_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // > ------ Grater
                    if (currentOperator.contentEquals(OperatorVarList.GRATER) && (txnAmountCount > doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // >= ------ Grater or Equal
                    if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (txnAmountCount >= doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // <> ------ Between
                    if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && !((doubleCurrentStartValue < txnAmountCount) && (txnAmountCount < doubleCurrentEndValue))) {
                        messageString = MessageVarList.CARDHOLDER_INVALID_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                } /**
                 * check per day transaction total count by card holder pan
                 */
                else if (currentRuleType.contentEquals(RuleTypeVarList.COUNT)) {

                    //get system date and time from the database machine
                    Timestamp sysDate;
                    sysDate = systemDateTime.getSystemDataAndTime();

                    //start form the used entered txn type
                    double txnAmount = iPGMerchantSendToMpiBean.getAmount();

                    iPGTransactionSearchBean = new TransactionSearchBean();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(sysDate.getTime());

                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                    String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

                    String fromDate = year + "-" + month + "-" + date;

                    iPGTransactionSearchBean.setFromDate(fromDate);
                    iPGTransactionSearchBean.setTodate("");
                    iPGTransactionSearchBean.setCardNumber(iPGMerchantSendToMpiBean.getCardHolderPan());
                    iPGTransactionSearchBean.setStatus(StatusVarList.TXN_COMPLETED);

                    /**
                     * get transaction count customer and merchant wise
                     */
                    if ("ipg".equals(ruleType)) {
                        txnCount = merchantProcessDAO.getCustomerTransactionCount(iPGTransactionSearchBean);
                    } else {
                        txnCount = merchantProcessDAO.getMerchantTransactionCount(iPGTransactionSearchBean);
                    }
                    txnCount++;

                    int intCurrentStartValue = Integer.parseInt(currentStartValue);
                    int intCurrentEndValue = Integer.parseInt(currentEndValue);

                    //validate with max txn amount per data for a particular customer
                    //remove == and  <=  logics (they are not valied)
                    // < ------ Less
                    if (currentOperator.contentEquals(OperatorVarList.LESS) && (txnCount < intCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_COUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmount;
                        break;
                    }
                    // > ------ Grater
                    if (currentOperator.contentEquals(OperatorVarList.GRATER) && (txnCount > intCurrentStartValue)) {
                        //write info log. for write ipg rule validation status
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_COUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmount;
                        break;
                    }
                    // >= ------ Grater or Equal
                    if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (txnCount >= intCurrentStartValue)) {
                        //write info log. for write ipg rule validation status
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_COUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmount;
                        break;
                    }
                    // <> ------ Between
                    if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && !((intCurrentStartValue < txnCount) && (txnCount < intCurrentEndValue))) {
                        //write info log. for write ipg rule validation status
                        messageString = MessageVarList.CARDHOLDER_INVALID_TXN_COUNT + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }

                } /**
                 * check per day transaction total amount by card holder pan
                 */
                else if (currentRuleType.contentEquals(RuleTypeVarList.MAXIMUM)) {

                    //get system date and time from the database machine
                    Timestamp sysDate;
                    sysDate = systemDateTime.getSystemDataAndTime();

                    //start form the used entered txn type
                    double txnAmountCount = iPGMerchantSendToMpiBean.getAmount();

                    iPGTransactionSearchBean = new TransactionSearchBean();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(sysDate.getTime());

                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                    String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

                    String fromDate = year + "-" + month + "-" + date;

                    iPGTransactionSearchBean.setFromDate(fromDate);
                    iPGTransactionSearchBean.setTodate("");
                    iPGTransactionSearchBean.setCardNumber(iPGMerchantSendToMpiBean.getCardHolderPan());
                    iPGTransactionSearchBean.setStatus(StatusVarList.TXN_COMPLETED);

                    /**
                     * get full transaction count customer / merchant wise
                     */
                    if ("ipg".equals(ruleType)) {
                        lst = merchantProcessDAO.getCustomerTransactionAll(iPGTransactionSearchBean);
                    } else {
                        lst = merchantProcessDAO.getMerchantTransactionAll(iPGTransactionSearchBean);
                    }

                    for (int j = 0; j < lst.size(); j++) {
                        TransactionBean iPGTransactionBean;
                        iPGTransactionBean = lst.get(j);
                        txnAmountCount = txnAmountCount + iPGTransactionBean.getAmount();
                    }

                    double doubleCurrentStartValue = Double.parseDouble(currentStartValue);
                    double doubleCurrentEndValue = Double.parseDouble(currentEndValue);

                    //validate with max txn amount per data for a particular customer
                    //remove == and  <=  logics (they are not valied)
                    // < ------ Less
                    if (currentOperator.contentEquals(OperatorVarList.LESS) && (txnAmountCount < doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_LESS_MIN_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // > ------ Grater
                    if (currentOperator.contentEquals(OperatorVarList.GRATER) && (txnAmountCount > doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // >= ------ Grater or Equal
                    if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (txnAmountCount >= doubleCurrentStartValue)) {
                        messageString = MessageVarList.CARDHOLDER_EXEEDED_MAX_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId() + MessageVarList.MESSAGE_CURRENT_TOTAL_TXN_AMOUNT + txnAmountCount;
                        break;
                    }
                    // <> ------ Between
                    if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && !((doubleCurrentStartValue < txnAmountCount) && (txnAmountCount < doubleCurrentEndValue))) {
                        messageString = MessageVarList.CARDHOLDER_INVALID_TXN_AMOUNT + iPGMerchantSendToMpiBean.getiPGTransactionId();
                        break;
                    }
                }
            }
            //End Card Holder -------------------------------------------------------------
            //Start Requested URL -------------------------------------------------------------
            if (currentRuleScope.contentEquals(RuleScopeVarList.REQUESTURL)
                    && (currentRuleType.contentEquals(RuleTypeVarList.BLACKLIST)) // = ------ Equal
                    && (currentOperator.contentEquals(OperatorVarList.EQUAL) && (currentStartValue.contentEquals(requestedUrl)))) {

                //blacklisted request url
                messageString = MessageVarList.BLACKLISTED_REQUEST_URL + iPGMerchantSendToMpiBean.getiPGTransactionId() + " Requested Url is: " + iPGMerchantSendToMpiBean.getPrimaryUrl();
                break;
            }
            //End Requested URL -------------------------------------------------------------
            //Start card Range -------------------------------------------------------------
            if (currentRuleScope.contentEquals(RuleScopeVarList.CARDRANGE) && (currentRuleType.contentEquals(RuleTypeVarList.BLACKLIST))) {

                //Process "BLK-Blacklist"
                // = ------ Equal
                if (currentOperator.contentEquals(OperatorVarList.EQUAL) && (currentStartValue.contentEquals(cardHolder))) {
                    messageString = MessageVarList.BLACKLISTED_CARDHOLDER + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // > ------ Grater
                Long intCurrentStartValue = Long.parseLong(currentStartValue);
                Long intCurrentEndValue = Long.parseLong(currentEndValue);

                if (currentOperator.contentEquals(OperatorVarList.GRATER) && (cardNumber > intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_CARD_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // >= ------ Grater or Equal
                if (currentOperator.contentEquals(OperatorVarList.GRATER_OR_EQUAL) && (cardNumber >= intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_CARD_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // < ------ Less
                if (currentOperator.contentEquals(OperatorVarList.LESS) && (cardNumber < intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_CARD_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // <= ------ Less or Equal
                if (currentOperator.contentEquals(OperatorVarList.LESS_OR_EQUAL) && (cardNumber <= intCurrentStartValue)) {
                    messageString = MessageVarList.INVALID_CARD_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }
                // <> ------ Between
                if (currentOperator.contentEquals(OperatorVarList.BETWEEN) && ((intCurrentStartValue < cardNumber) && (cardNumber < intCurrentEndValue))) {
                    messageString = MessageVarList.INVALID_CARD_RANGE + iPGMerchantSendToMpiBean.getiPGTransactionId();
                    break;
                }

            }
        }
        //end read one by one according to IPG RULE SCOPE -------------------------------

        return messageString;
    }

    private String createEmailBody(String transactionId, String txnMessage) {

        String emailBody = "";
        try {

            MerchantResponseBean responseBean = merchantProcessDAO.getMerchantForResponseByTxnID(transactionId);

            emailBody
                    = "<div style='background-color: #f5f5f5;padding-top:10px;padding-bottom:20px;'>"
                    + "<div style='background-color: #ffffff;margin-left: 250px; margin-right: 250px;padding:20px;font-size: 13px;line-height: 150%;color: #3a444b;font-family: arial,sans-serif'><span><hr /></span>"
                    + "<h1><span style='font-size: 18pt;'>Payment Receipt</span></h1><span><hr /></span>"
                    + "<p>&nbsp;</p>"
                    + "<table>"
                    + "<tr align='left'><td>Payment Status </td><td> : </td><td>" + txnMessage
                    + "</td></tr><tr align='left'><td>Payment Amount </td><td> : </td><td>" + responseBean.getCurrencyCode() + " " + responseBean.getAmount()
                    + "</td></tr><tr align='left'><td>Account No </td><td> : </td><td>" + responseBean.getAccountNo()
                    + "</td></tr><tr align='left'><td>Payment Reference </td><td> : </td><td>" + transactionId
                    + "</td></tr><tr align='left'><td>Date Time </td><td> : </td><td>" + responseBean.getTxnDateTime() + "</td></tr>"
                    + "</table>"
                    + "<p>&nbsp;</p><span><hr /></span>"
                    + "<span style='font-size: 11px;'>You are receiving this email because of your request at <a href='" + responseBean.getPrimaryURL() + "' target='_blank\'>" + responseBean.getPrimaryURL() + "</a>\n"
                    + "Thanks for doing transaction with us.</span>"
                    + "</div>"
                    + "</div>";
        } catch (CustomException ex) {
            emailBody = "";
        }

        return emailBody;

    }

    //then add txn data to the generated xml message
    public String fillRequestMessageWithMaskCard(String generatedXmlMessage, MerchantSendToMpiBean merchantSendToMpiBean) throws Exception {

        String cardHolderPan;
        String messageWithTxnData;
        String xml = generatedXmlMessage;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml + "")));

        doc.getDocumentElement().normalize();

        //"CARDHOLDER" node
        NodeList listCARDHOLDER = doc.getElementsByTagName(CommonVarList.CARDHOLDER);
        for (int s = 0; s < listCARDHOLDER.getLength(); s++) {
            Node nodeCARDHOLDER = listCARDHOLDER.item(s);
            if (nodeCARDHOLDER.getNodeType() == Node.ELEMENT_NODE) {
                Element elementCARDHOLDER = (Element) nodeCARDHOLDER;

                try {
                    //"CARDHOLDERPAN" node
                    NodeList listCARDHOLDERPAN = elementCARDHOLDER.getElementsByTagName(CommonVarList.CARDHOLDERPAN);
                    Element elementCARDHOLDERPAN = (Element) listCARDHOLDERPAN.item(0);
                    NodeList textCARDHOLDERPAN = elementCARDHOLDERPAN.getChildNodes();
                    cardHolderPan = Common.toMaskCardNumber(merchantSendToMpiBean.getCardHolderPan());
                    textCARDHOLDERPAN.item(0).setNodeValue(cardHolderPan);
                } catch (DOMException ex) {
                    Logger.getLogger(MerchantProcessManager.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }

        //add new changes to the xml string/file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

        messageWithTxnData = result.getWriter().toString();

        return messageWithTxnData;
    }

}
