/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.manager.payment.MerchantRequestManager;
import com.epic.ipg.util.varlist.CommonVarList;

/**
 *
 * @author dilanka_w
 */
public class NewClass {

    public static void main(String[] args) throws CustomException, Exception {

//        MerchantRequestManager merchantRequestManager = new MerchantRequestManager();
//        System.out.println(merchantRequestManager.createTransactionId());
        System.out.println(TDESEncryptor.encrypt(CommonVarList.TDESENCRYPT_KEY, "1809281119141577", "CBC"));
        System.out.println(TDESEncryptor.decrypt(CommonVarList.TDESENCRYPT_KEY, "0376B0AD39B468DB0B5E21988C36589C20255B8996646E3F41D69531D58958E8", "CBC"));
    }

}
