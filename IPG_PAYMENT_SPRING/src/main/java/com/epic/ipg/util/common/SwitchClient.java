/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.util.dao.CommonDAO;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class SwitchClient {

    private Socket client;
    private BufferedReader br;
    private PrintWriter pw;

    public SwitchClient(CommonDAO commonDAO) throws CustomException {
        //get txn switch configuration
        try {
            //read xml file & get txn switch configuration
            InetAddress inetAdd = InetAddress.getByName(commonDAO.getCommonConfigValue(CommonVarList.SWITCHIP)); //ip
            SocketAddress serverAdd = new InetSocketAddress(inetAdd, Integer.parseInt(commonDAO.getCommonConfigValue(CommonVarList.SWITCHPORT))); //port

            client = new Socket();
            client.connect(serverAdd);

            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(client.getOutputStream());
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.SWITCH_CONNECTING_ERROR);
        }
    }

    public void sendPacket(String request) throws CustomException {
        try {
            pw.println(request);
            pw.flush();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.SWITCH_SENDING_ERROR);
        }
    }

    public String receivePacket() throws CustomException {
        String response;
        try {
            response = br.readLine(); // read the response from the socket

            return response;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.SWITCH_RECEIVE_ERROR);
        }
    }

    public void closeAll() throws CustomException {
        try {
            if (client != null) {
                client.close();
            }
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
            client = null;
            br = null;
            pw = null;
            
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.SWITCH_CLOSE_ERROR);
        }
    }
}
