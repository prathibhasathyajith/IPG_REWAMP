/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.util.varlist.MessageVarList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dilanka_w
 */
@Repository
@Scope("prototype")
public class Common {

    @Autowired
    LogFileCreator logFileCreator;

    CommonFilePaths commonFilePaths;

    public static String toCurrencyFormat(String digits) {

        StringBuilder currencyBuilder = new StringBuilder();
        String reversed = "";

        digits = digits.replace(",", "");
        String[] part = digits.split("[.]");
        if (0 < part.length && part.length < 3) {

            if (part.length == 1) {
                reversed = new StringBuilder(digits).reverse().toString();
            } else if (part.length == 2) {
                reversed = new StringBuilder(part[0]).reverse().toString();
            }

            char[] stringToCharArray = reversed.toCharArray();
            for (int i = 0; i < reversed.length(); i++) {
                currencyBuilder.append(stringToCharArray[i]);
                if (i % 3 == 2 && i != 0 && i != reversed.length() - 1) {
                    currencyBuilder.append(",");
                }
            }

            currencyBuilder.reverse();
            if (part.length == 2) {
                currencyBuilder.append(".").append(part[1]);
            }

        }
        return currencyBuilder.toString();
    }

    public static String toMaskCardNumber(String cardNumber) {

        StringBuilder maskCardNumber = new StringBuilder();
        String mask = "**********************";
        int maskLength = cardNumber.length() - 4;
        maskCardNumber
                .append(mask.substring(0, maskLength))
                .append(cardNumber.substring(maskLength, cardNumber.length()));
        return maskCardNumber.toString();
    }

    public String getMerchantImage(String imgPath, String merchantId) {
        String byteString = "";
        //get path
        try {

            File destFile = new File(imgPath);

            int fileLength = (int) destFile.length();

            if (fileLength != 0) {
                
                byte[] imageData = new byte[(int) destFile.length()];

                try (FileInputStream fileInputStream = new FileInputStream(destFile)) {
                    int count;
                    while ((count = fileInputStream.read(imageData)) > 0);
                }

                byteString = new String(org.apache.commons.codec.binary.Base64.encodeBase64(imageData));
            }
        } catch (FileNotFoundException ex) {
            //write info log. for write mpi request sent to mpi by ipg __________________________________________________________
            logFileCreator.writInforTologsForMerchantLogin(MessageVarList.MERCHANT_ADDON_IMAGE_INVALID + merchantId, commonFilePaths.getIpgmerchantlogin());
            //write infor log.___________________________________________________________________________________________________
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + MessageVarList.MERCHANT_ADDON_IMAGE_INVALID + merchantId, commonFilePaths.getInforLogPath());

        } catch (IOException ex) {
            //write infor log.___________________________________________________________________________________________________
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + ex, commonFilePaths.getInforLogPath());

        } catch (NullPointerException ex) {
            //write infor log.___________________________________________________________________________________________________
            logFileCreator.writInforTologs(this.getClass().getSimpleName() + ex, commonFilePaths.getInforLogPath());
        }
        return byteString;
    }

}
