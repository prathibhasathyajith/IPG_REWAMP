/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.validations;

/**
 *
 * @author upul
 */
import com.epic.ipg.util.varlist.CardAssociationVarList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import org.springframework.stereotype.Service;

@Service
public class CreditCardValidator {

    public boolean validationCriteria(String cardNumber, String cardType) {
        String number = cardNumber;

        if (number.equals("")) {
            return false;
        }

        Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);

        if (m.find()) {
            return false;
        }

        Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);
        number = matcher.replaceAll("");
        return validate(number, cardType);
    }

    // Check that cards start with proper digits for
    // selected card type and are also the right length.
    public boolean validate(String number, String type) {
        switch (type) {

            case CardAssociationVarList.MARSTERCARD:
                if (number.length() != 16
                        || Integer.parseInt(number.substring(0, 2)) < 51
                        || Integer.parseInt(number.substring(0, 2)) > 55) {
                    return false;
                }
                break;

            case CardAssociationVarList.VISA:

                if ((number.length() != 13 && number.length() != 16 && number.length() != 19)
                        || Integer.parseInt(number.substring(0, 1)) != 4) {

                    return false;
                }
                break;

            case CardAssociationVarList.AMEX:
                if (number.length() != 15
                        || (Integer.parseInt(number.substring(0, 2)) != 34
                        && Integer.parseInt(number.substring(0, 2)) != 37)) {
                    return false;
                }
                break;
            default:
                Logger.getLogger(CreditCardValidator.class.getName()).log(Level.INFO, "This is Default :");
        }
        return luhnValidate(number);
    }

    // The Luhn algorithm is basically a CRC type
    // system for checking the validity of an entry.
    // All major credit cards use numbers that will
    // pass the Luhn check. Also, all of them are based
    // on MOD 10.
    public boolean luhnValidate(String numberString) {
        char[] charArray = numberString.toCharArray();
        int[] number = new int[charArray.length];
        int total = 0;

        for (int i = 0; i < charArray.length; i++) {
            number[i] = Character.getNumericValue(charArray[i]);
        }

        for (int i = number.length - 2; i > -1; i -= 2) {
            number[i] *= 2;

            if (number[i] > 9) {
                number[i] -= 9;
            }
        }

        for (int i = 0; i < number.length; i++) {
            total += number[i];
        }

        return total % 10 == 0;
    }
}
