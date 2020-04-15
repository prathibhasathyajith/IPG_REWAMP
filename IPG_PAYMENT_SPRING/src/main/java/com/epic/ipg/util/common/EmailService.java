/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.bean.payment.EmailTemplateBean;
import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.EmailResponceCodes;
import com.sun.mail.util.MailSSLSocketFactory;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dilanka_w
 */
@Service
public class EmailService {

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    LogFileCreator logFileCreator;

    public String sendMailToCustomer(String email, EmailTemplateBean bean, CommonFilePaths commonFilePaths) {
        String print;
        String returnMsg;

        try {

            String realPathInforLogPath = commonFilePaths.getInforLogPath();
            String host = commonDAO.getCommonConfigValue(CommonVarList.EMAILHOST);
            String user = commonDAO.getCommonConfigValue(CommonVarList.EMAILFROMUSER);
            String password = commonDAO.getCommonConfigValue(CommonVarList.EMAILPASS);
            String from = user;	//sender
            int port = Integer.parseInt(commonDAO.getCommonConfigValue(CommonVarList.EMAILPORT));
            String to = email;

            String messageText = bean.getMessage();

            BufferedReader reader = new BufferedReader(new StringReader(messageText));
            String readline;

            StringBuilder mailMsg = new StringBuilder();
            while ((readline = reader.readLine()) != null) {
                mailMsg.append(readline).append("<br/>");
            }

            messageText = mailMsg.toString();

            Properties props = System.getProperties();
            props.put("mail.host", host);
            props.put("mail.transport.protocol.", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();

            sf.setTrustAllHosts(true);

            props.put("mail.smtp.ssl.socketFactory", sf);

            Session mailSession = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(bean.getSubject());

            /**
             * ************* added image part *******************
             */
            //
            // This HTML mail have to 2 part, the BODY and the embedded image
            //
            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = messageText;
            messageBodyPart.setContent(htmlText, "text/html");

            // add it
            multipart.addBodyPart(messageBodyPart);

            if (bean.getImgpath() != null && bean.isImagetagExist()) {
                // second part (the image)
                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(bean.getImgpath());
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader("Content-ID", "<image>");

                // add it
                multipart.addBodyPart(messageBodyPart);
            }
            // put everything together
            msg.setContent(multipart);

            /**
             * ************* added image part *******************
             */
            Transport transport = mailSession.getTransport("smtp");

            transport.connect(host, port, user, password);

            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();
            returnMsg = EmailResponceCodes.PROCESS_SUCCESS;

            //write mail details to log
            print = "Mail		:" + to + CommonVarList.NEWLINE
                    + "Subject		:" + bean.getSubject() + CommonVarList.NEWLINE
                    + "Message		:" + bean.getMessage();

            //write infor log.__________________________________________________________________________________________________________
            logFileCreator.writInforTologs("EmailService >> email details : \n" + print, realPathInforLogPath);

        } catch (MessagingException ex) {
            returnMsg = EmailResponceCodes.UNABLE_TO_PROCESS_MAIL;
            //write error message to the error log, and display it to the user
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            //write error log.___________________________________________________________________________________________________________________________
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());

        } catch (Exception ex) {
            returnMsg = EmailResponceCodes.UNABLE_TO_PROCESS;
            //write error message to the error log, and display it to the user
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            //write error log.___________________________________________________________________________________________________________________________
            logFileCreator.writeErrorToLog(ex.fillInStackTrace(), commonFilePaths.getErrorLogPath());
        }
        return returnMsg;
    }

}
