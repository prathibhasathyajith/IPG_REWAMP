
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chanuka_g
 */
@Service
public class LogFileCreator {

    @Autowired
    SystemDateTime systemDateTime;

    private LogFileCreator() {
    }

    private void fileWriter(String filename, String msg) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(msg);
            bw.newLine();
            bw.flush();
        }
    }

    public synchronized void writInforTologs(String msg, String realPathInfor) {

        try {

            File file = new File(realPathInfor);
            if (!file.exists()) {
                file.mkdirs();
            }

            String filename = getFileName() + "_EPAY_IPG_INFOR";
            this.fileWriter(realPathInfor + filename, getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write mpi responses received from the mpi
    public synchronized void writInforTologsForMpiResponse(String msg, String mpiResponsePath) {

        try {

            String line = "\n_______________INFOR___________________________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_MPI_RESPONSE";

            File file = new File(mpiResponsePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            this.fileWriter(mpiResponsePath + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write mpi request sent to mpi by ipg
    public synchronized void writInforTologsForMpiRequest(String msg, String mpiRequestPath) {

        try {

            String line = "\n_______________INFOR_______________________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_MPI_REQUEST";

            File file = new File(mpiRequestPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            this.fileWriter(mpiRequestPath + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write switch request sent to switch by ipg
    public synchronized void writInforTologsForSwitchRequest(String msg, String realPathSwitchRequest) {

        try {

            File file = new File(realPathSwitchRequest);
            if (!file.exists()) {
                file.mkdirs();
            }

            String line = "\n_______________INFOR______________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_SWITCH_REQUEST";

            this.fileWriter(realPathSwitchRequest + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write mpi request sent to mpi by ipg
    public synchronized void writInforTologsForMerchantLogin(String msg, String realPathMPIReq) {

        try {

            File file = new File(realPathMPIReq);
            if (!file.exists()) {
                file.mkdirs();
            }

            String line = "\n_______________INFOR____________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_MERCHANT_LOGIN";

            this.fileWriter(realPathMPIReq + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write switch response sent to ipg by switch
    public synchronized void writInforTologsForSwitchResponse(String msg, String realPathSwitchResponse) {

        try {

            File file = new File(realPathSwitchResponse);
            if (!file.exists()) {
                file.mkdirs();
            }

            String line = "\n_______________INFOR_______________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_SWITCH_RESPONSE";

            this.fileWriter(realPathSwitchResponse + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write ipg risk validation status
    public synchronized void writInforTologsForIpgRiskValidation(String msg, String realPathIpgValidationLog) {

        try {

            File file = new File(realPathIpgValidationLog);
            if (!file.exists()) {
                file.mkdirs();
            }

            String line = "\n_______________INFOR_____________________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_RISK_VALIDATION";

            this.fileWriter(realPathIpgValidationLog + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //write info log. for write ipg rule validation status
    public synchronized void writInforTologsForIpgRuleValidation(String msg, String realPathIpgValidationLog) {

        try {

            File file = new File(realPathIpgValidationLog);
            if (!file.exists()) {
                file.mkdirs();
            }

            String line = "\n_______________INFOR_____________________________________________________________\n";
            String filename = getFileName() + "_EPAY_IPG_RULE_VALIDATION";

            this.fileWriter(realPathIpgValidationLog + filename, line + getTime() + "    " + msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    /**
     * This method writes given exceptions to an error log file in given path.
     */
    public synchronized void writeErrorToLog(Throwable aThrowable, String errorLogPath) {

        String line = "\n________________________________ERROR._______________________________________________\n";
        String msg;

        try {

            String filename = getFileName() + "_EPAY_IPG_ERROR.txt";

            File file = new File(errorLogPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            msg = line + getTime() + ": " + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(aThrowable);

            this.fileWriter(errorLogPath + filename, msg);

        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    private String getFileName() {

        Calendar calendar = Calendar.getInstance();

        String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
        String currentMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String currentDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        return "" + currentYear + currentMonth + currentDay + "";

    }

    private String getTime() {
        return "" + systemDateTime.getSystemDataAndTime() + "";
    }

    public String getOsType() {

        String osType = "";
        String osName;
        osName = System.getProperty("os.name", "").toLowerCase();

        if (osName.contains("windows")) {
            osType = "WINDOWS";// For WINDOWS
        } else if (osName.contains("linux")) {
            osType = "LINUX";// For LINUX
        } else if (osName.contains("sunos")) {
            osType = "SUNOS";// For SUNOS
        }
        return osType;

    }
}
