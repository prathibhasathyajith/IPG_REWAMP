/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.bean.payment.ServiceChargeBean;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author chanuka_g
 */
@Service
public class Calculations {

    public String getRandomString() throws CustomException {

        Logger.getLogger(Calculations.class.getName()).log(Level.INFO, "called CaptchaServlet : getRandomString");

        String s;
        try {

            SecureRandom random = new SecureRandom();
            s = new BigInteger(130, random).toString(32);
            Random r = new Random();
            int n = r.nextInt(4);

            if (n == 0) {
                n += 3;
            }
            String p = s.substring(0, n);
            String m = s.substring(n, n + 1).toUpperCase();
            String suf = s.substring(n + 1, 4);
            String string = p + m + suf;
            s = string;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.RANDOM_STRING_ERROR);
        }
        return s;
    }

    public String amountAfterServiceCharge(double amount, ServiceChargeBean serviceChargeBean) throws CustomException {

        double amountAfterService;
        double fixAmount;

        String value;
        double rate;
        try {

            //set the value and rate
            if (serviceChargeBean != null) {
                value = serviceChargeBean.getValue();
                rate = Double.parseDouble(serviceChargeBean.getRate());

            } else {
                value = "0";
                rate = 0;
            }

            fixAmount = Double.parseDouble(value);

            double amountafterrate = (amount * rate) / 100;
            amountAfterService = amount + fixAmount + amountafterrate;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.SERVICE_CHARGE_ERROR);
        }
        return Double.toString(amountAfterService);

    }

    public double round(double rval, int rpl) throws CustomException {
        try {
            double p = Math.pow(10, rpl);
            rval = rval * p;
            double tmp = Math.round(rval);
            return tmp / p;
        } catch (Exception e) {
            throw new CustomException(CustomErrorVarList.ROUND_ERROR);
        }
    }
}
