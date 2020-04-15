/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.manager.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.MerchantBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.bean.payment.ServiceChargeBean;
import com.epic.ipg.dao.payment.MerchantRequestDAO;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.common.Calculations;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.KeyStoreEncryptor;
import com.epic.ipg.util.common.LogFileCreator;
import com.epic.ipg.util.common.SessionBean;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.MessageVarList;
import java.io.File;
import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chanuka_g
 */
@Service
public class MerchantRequestManager {

    @Autowired
    MerchantRequestDAO merchantRequestDAO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    LogFileCreator logFileCreator;

    @Autowired
    Calculations calculations;

    @Autowired
    SystemDateTime systemDateTime;

    @Autowired
    private SessionBean sessionBean;

    public boolean validateMerchant(MerchantBean iPGMerchantBean, CommonFilePaths ipgCommonFilePath, HttpServletRequest request) throws CustomException {
        try {

            boolean verifyMerchant = false;
            String merchantId = iPGMerchantBean.getMerchantId();
            Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called MerchantRequestManager : validateMerchant");

            String realPathInforLogPath = ipgCommonFilePath.getInforLogPath();

            //checck whether security mechanism of merchant is ??????
            if (iPGMerchantBean.getSecurityMechanism().equals("DigitallySign")) {

                //write infor log.__________________________________________________________________________________________
                logFileCreator.writInforTologs(this.getClass().getSimpleName() + MessageVarList.SECURITY_MECHANISM_DIGITALLYSIGN + merchantId, realPathInforLogPath);

                //get signed parameters
                String byteSignedDataString = request.getParameter("byteSignedDataString");
                String signature = request.getParameter("signature");

                //convert byteSignedDataString String to Byte Array for verification
                byte[] byteSignedData = ISOUtil.hex2byte(byteSignedDataString);

                String certificatePathForVerification = ipgCommonFilePath.getCertificatePath() + merchantId + ".cer";

                //get the verification of merchant
                verifyMerchant = isVerifysign(certificatePathForVerification, signature, byteSignedData);

                if (!verifyMerchant) {

                    //write info log. for write mpi request sent to mpi by ipg ________________________________________________________
                    logFileCreator.writInforTologsForMerchantLogin(MessageVarList.AUTH_CER_FAILED + merchantId, ipgCommonFilePath.getIpgmerchantlogin());
                    //write infor log.__________________________________________________________________________________________________
                    logFileCreator.writInforTologs(this.getClass().getSimpleName() + " " + MessageVarList.AUTH_CER_FAILED + merchantId, realPathInforLogPath);
                }

            } //if merchnt security mechansm is SymmetricKey
            else if (iPGMerchantBean.getSecurityMechanism().equals("SymmetricKey")) {

                //write infor log.__________________________________________________________________________________________________
                logFileCreator.writInforTologs(this.getClass().getSimpleName() + MessageVarList.SECURITY_MECHANISM_SYMMETRICKEY + merchantId, realPathInforLogPath);

                String keystorepath = ipgCommonFilePath.getKeystorepath() + merchantId + ".jks";

                byte[] aa = Base64.decode(commonDAO.getCommonConfigValue(CommonVarList.KEYSTOREPASS));
                String keypassowrd = new String(aa);

                //get encrypted merchant Id
                String emerchantId = request.getParameter("emerchantId");

                //convert encrypted merId to Byte Array for decyption
                byte[] em = ISOUtil.hex2byte(emerchantId);

                //start  decryption process
                //---------------------------------------------------------------------------------------------
                KeyStoreEncryptor encryptor = new KeyStoreEncryptor();

                String modulus = encryptor.getPriveteKeyModulus(keystorepath, keypassowrd, keypassowrd, merchantId);

                SecretKey desKey = encryptor.generateDESKeyByModulus(modulus);

                //call the decrypt method to decrypt the encrypted merchantId
                String decrptMerchantId = encryptor.desDecrypt(em, desKey);
                //-----------------------------------------------------------------------------------------------
                //end of the decryption process
                verifyMerchant = merchantId.equals(decrptMerchantId);

            }
            return verifyMerchant;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.VALIDATE_MERCHANT_ERROR);
        }
    }

    public boolean isVerifysign(String certificatePathForVerification, String signature, byte[] byteSignedData) throws CustomException {

        Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called MerchantRequestManager : isVerifysign");
        try {

            boolean verifySign;

            PublicKey pbKey;
            try (FileInputStream fileInputStream = new FileInputStream(new File(certificatePathForVerification))) {
                Security.addProvider(new BouncyCastleProvider());
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
                X509Certificate x509Cert = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
                pbKey = x509Cert.getPublicKey();
            }

            //  Verifying the Signature
            Signature myVerifySign = Signature.getInstance("MD5withRSA");
            myVerifySign.initVerify(pbKey);
            myVerifySign.update(signature.getBytes());

            verifySign = myVerifySign.verify(byteSignedData);

            if (verifySign) {
                Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called IPGMerchantAddOnServlet : amountAfterServiceCharge : Successfully validated Signature ");
            } else {
                Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called IPGMerchantAddOnServlet : amountAfterServiceCharge : Error in validating Signature ");
            }
            return verifySign;

        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.SIGN_VERIFY_ERROR);
        }
    }

    public TransactionBean setIPGTransactionBeanData(HttpSession sessionObj, HttpServletRequest request,
            String transactionId, MerchantBean merchantBean) throws CustomException {

        Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called MerchantRequestManager : setIPGTransactionBeanData");
        try {
            Timestamp nowDate = systemDateTime.getSystemDataAndTime();
            TransactionBean iPGTransactionBean = new TransactionBean();

            //set data to the ipgTransactionBean
            iPGTransactionBean.setMerchantId(merchantBean.getMerchantId());
            iPGTransactionBean.setMerchantType("");
            iPGTransactionBean.setTerminalId("");

            //set total amount to the bean
            iPGTransactionBean.setAmount(Double.parseDouble(request.getParameter("amount")));

            ServiceChargeBean serviceChargeBean = merchantRequestDAO.getServiceCharge(merchantBean.getMerchantId());
            double totalAmount = calculations.round(Double.parseDouble(calculations.amountAfterServiceCharge(Double.parseDouble(request.getParameter("amount")), serviceChargeBean)), 2);
            sessionObj.setAttribute("totalAmount", totalAmount);

            iPGTransactionBean.setAmount(totalAmount);

            iPGTransactionBean.setCurrencyCode(request.getParameter("currencyCode"));
            String currency = commonDAO.getCurrencyCode(iPGTransactionBean.getCurrencyCode());

            //set currency to the session
            sessionObj.setAttribute("currency", currency);

            iPGTransactionBean.setTransactionCreatedDateTime(nowDate);
            iPGTransactionBean.setiPGTransactionId(transactionId);
            iPGTransactionBean.setLastUpdatedUser(merchantBean.getMerchantName());
            iPGTransactionBean.setClientIp(request.getRemoteHost());

            return iPGTransactionBean;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.TRANSACTION_DATA_SET_ERROR);
        }
    }

    public String createTransactionId() throws CustomException {

        Logger.getLogger(MerchantRequestManager.class.getName()).log(Level.INFO, "called MerchantRequestManager : createTransactionId");

        String tId;
        try {
            String date = createCurrentDateTime();
            String randm = generateID();

            tId = (date + randm);
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.TXN_ID_ERROR);
        }

        return tId;
    }

    public String createCurrentDateTime() throws CustomException {

        String currentdatetime = "";
        String dateFormat = "yyMMddHHmmss";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date();
            currentdatetime = sdf.format(date);

        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.DATA_TIME_ERROR);
        }

        return currentdatetime;
    }

    public HttpSession createSession(HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        //set user and sessionid to hashmap
        HashMap<String, String> userMap;
        ServletContext sc = request.getServletContext();
        userMap = (HashMap<String, String>) sc.getAttribute(CommonVarList.USERMAP);
        if (userMap == null) {
            userMap = new HashMap<>();
        }
        userMap.put(session.getId(), session.getId());
        sc.setAttribute(CommonVarList.USERMAP, userMap);
        sessionBean.setSystemUser(session.getId());
        return session;
    }

    //to generate randaom number
    private static final int[] PINS = new int[900000];

    static {
        for (int i = 0; i < PINS.length; i++) {
            PINS[i] = 100000 + i;
        }
    }
    private static int pinCount;
    private static final Random RANDOM = new Random();

    public static String generateID() {

        if (pinCount >= PINS.length) {
            throw new IllegalStateException();
        }

        int index = RANDOM.nextInt(PINS.length - pinCount) + pinCount;
        int pin = PINS[index];
        PINS[index] = PINS[pinCount++];

        return String.valueOf(pin).substring(0, 4);
    }
}
