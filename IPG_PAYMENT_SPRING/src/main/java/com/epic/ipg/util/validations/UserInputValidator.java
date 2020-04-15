/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.validations;

import com.epic.ipg.util.varlist.MessageVarList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author chanuka_g
 */
public class UserInputValidator {

    private UserInputValidator() {
    }

    //if correct numerc value found, then return true
    public static boolean isNumeric(String theInputString) {

        boolean isValid = false;

        for (int i = 0; i < theInputString.length(); i++) {
            char c = theInputString.charAt(i);

            if ((c >= '0') && (c <= '9')) {
                isValid = true;
            } else {
                isValid = false;
                break;
            }

        }

        return isValid;

    }

    public static boolean isAlphaNumeric(String value) {
        return value.matches("[a-zA-Z0-9]*");
    }

    public static boolean isAlphaWithWhiteSpace(String value) {
        return value.matches("[a-zA-Z\\s]*");
    }

    public static boolean isAlpha(String value) {
        return value.matches("[a-zA-Z]*");
    }

    public static boolean isValidPageUrl(String value) {
        return value.matches("/[a-zA-Z_.]+");
    }

    public static boolean isDescription(String value) {
        return value.matches("[[a-zA-Z0-9]*[[\\s]|[_]|[-]][a-zA-Z0-9]*]*");
    }

    //This method check wheather the input string contains any special characters. If there is any special characters it will return false.
    public static boolean isCorrectString(String theInputString) {
        boolean isValid = true;

        for (int i = 0; i < theInputString.length(); i++) {
            char c = theInputString.charAt(i);

            if ((c == '\'') || (c == '\\') || (c == '!') || (c == ':') || (c == '"') || (c == ';') || (c == ',') || (c == '@') || (c == '|') || (c == '*') || (c == '#') || (c == '{') || (c == '}') || (c == '`') || (c == '?')
                    || (c == '$') || (c == '%') || (c == '^') || (c == '&') || (c == '/') || (c == '>') || (c == '<') || (c == '(') || (c == ')') || (c == '~')) {
                isValid = false;
            }
        }

        return isValid;
    }

    //This method check wheather the input string contains any special characters or numeric characters. If there is any  it will return false.
    public static boolean isNonNumericNonSpecialString(String theInputString) {
        boolean isValid = true;

        for (int i = 0; i < theInputString.length(); i++) {
            char c = theInputString.charAt(i);

            if ((c == '\'') || (c == '\\') || (c == '!') || (c == ':') || (c == '"') || (c == ';') || (c == ',') || (c == '@') || (c == '|') || (c == '*') || (c == '#') || (c == '{') || (c == '}') || (c == '`') || (c == '?')
                    || (c == '$') || (c == '%') || (c == '^') || (c == '&') || (c == '/') || (c == '>') || (c == '<') || (c == '(') || (c == ')') || (c == '~')) {
                isValid = false;
            }
            if ((c >= '0') && (c <= '9')) {
                isValid = false;
            }
        }

        return isValid;
    }

    //if special characters found, then method will return false
    //if valid string, then return true
    public static boolean isString(String theInputString) {

        boolean isValid = true;
        // This argument will validate that the Input String doesn't contain any Special characters any Spaces. Numeric characters are allowed.
        if (theInputString.matches("\\w{1}|(\\W{1,}|\\s{1,})|(\\w{0,}(\\W{1,}|\\s{1,}|(\\W{1,}\\s{1,})|\\s{1,}|\\W{1,})\\w{0,}){1,}")) {
            isValid = false;
        }

        return isValid;

    }

    //if valid, then return true
    public static boolean isValidEmail(String theInputString) {

        boolean isValid = true;

        //Set the email pattern string
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        //Match the given string with the pattern
        Matcher m = p.matcher(theInputString);

        //Check whether match is found
        boolean matchFound = m.matches();

        if (!matchFound) {
            isValid = false;
        }

        return isValid;

    }
    //  ********************  This is a method to validate the decimal numeric Format ********************
    private static final String DECIMALNUMERIC_PATTERN = "\\d{0,15}\\.\\d{0,2}";

    public static boolean isDecimalNumeric(String inputString) {
        boolean validFlag = false;
        Pattern p = Pattern.compile(DECIMALNUMERIC_PATTERN);
        Matcher m = p.matcher(inputString.trim());
        if (m.matches()) {
            validFlag = true;
        }
        return validFlag;
    }
//  ********************  This is a method to validate URL ********************
    private static final String URL_PATTERN = "^((http|https|ftp):\\/\\/).*$";

    public static boolean isValidUrl(String url) {

        boolean flag = false;
        Pattern p = Pattern.compile(URL_PATTERN);
        Matcher m = p.matcher(url.trim());
        if (m.matches()) {
            flag = true;
        }

        return flag;
    }

    //  ********************  This is a method to validate the decimal numeric Format with any number of scale and precision ********************
    public static boolean isDecimalOrNumeric(String inputString, String integer, String decimal) {
        final String COMMON_PATTERN = "\\d{0," + integer + "}\\.\\d{0," + decimal + "}";
        final String COMMON_PATTERN_1 = "\\d{0," + integer + "}";
        boolean validFlag = false;
        Pattern p = Pattern.compile(COMMON_PATTERN);
        Pattern q = Pattern.compile(COMMON_PATTERN_1);
        Matcher m = p.matcher(inputString.trim());
        Matcher n = q.matcher(inputString.trim());
        if (m.matches() || n.matches()) {
            validFlag = true;
        }
        return validFlag;
    }

    public static boolean checkNIC(String nic) {
        boolean status = true;
        try {
            String nicFirstNineDigit = nic.substring(0, 9);
            String nicLastCharacter = nic.substring(9, 10);

            if (nic.length() > 10) {
                status = false;
            }
            if (!isNumeric(nicFirstNineDigit)) {
                status = false;

            }

            if (!nicLastCharacter.equalsIgnoreCase("v") && !nicLastCharacter.equalsIgnoreCase("x")) {
                status = false;

            }
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    public static boolean isCorrectIp(String theInputString) throws ValidateException {
        boolean isValid = true;
        int t;

        //Added IPADDRESS_PATTERN regex by Jeevan. 26-Aug-2013.
        final String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        try {

            String[] values = theInputString.split(IPADDRESS_PATTERN);

            for (String s : values) {

                t = Integer.parseInt(s);

                if (t < 0 || t > 255) {
                    throw new ValidateException(MessageVarList.NUMBER_ERROR_MESSAGE);
                }
                if (values.length != 4) {
                    throw new ValidateException(MessageVarList.SIZE_ERROR_MESSAGE);
                }

            }
        } catch (Exception e) {
            throw new ValidateException(MessageVarList.NUMBER_ERROR_MESSAGE);
        }
        return isValid;
    }

    public static boolean isCorrectPort(String theInputString) throws ValidateException {
        boolean isValid = true;
        int t;
        try {

            t = Integer.parseInt(theInputString);

            if (t < 0 || t > 65536) {
                throw new ValidateException(MessageVarList.SIZE_ERROR_MESSAGE);
            }

        } catch (Exception e) {
            throw new ValidateException(MessageVarList.NUMBER_ERROR_MESSAGE);
        }

        return isValid;
    }

    public static boolean isCaptchaCorrect(String value, String captcha) {

        return captcha.equals(value);

    }
}
