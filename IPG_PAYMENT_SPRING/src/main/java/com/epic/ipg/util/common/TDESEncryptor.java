/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.sun.crypto.provider.SunJCE;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.jpos.iso.ISOUtil;
import org.jpos.security.Util;

/**
 *
 * @author dilanka_w
 */
public class TDESEncryptor {

    private static final Provider p = new SunJCE();
//    private static final Provider p = new org.bouncycastle.jce.provider.BouncyCastleProvider();
    private static final byte[] Iv = new byte[8];

    private TDESEncryptor() {
    }

    public static void init() {
        Security.addProvider(p);
    }

    public static String encrypt(String key, String clearTxt, String mode) throws Exception {

        SecretKey secretKey = getSKey(ISOUtil.hex2byte(key));

        if ("CBC".equals(mode)) {
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", p.getName());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(Iv));
            return ISOUtil.hexString(cipher.doFinal(getEncoredMessage(clearTxt)));
        } else {
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding", p.getName());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return ISOUtil.hexString(cipher.doFinal(getEncoredMessage(clearTxt)));
        }

    }

    public static String decrypt(String key, String ciphertext, String mode) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        SecretKey secretKey = getSKey(ISOUtil.hex2byte(key));

        if ("CBC".equals(mode)) {
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", p.getName());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Iv));
            return getDecodedMessage(cipher.doFinal(ISOUtil.hex2byte(ciphertext)));
        } else {
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding", p.getName());
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return getDecodedMessage(cipher.doFinal(ISOUtil.hex2byte(ciphertext)));
        }

    }

    private static byte[] getEncoredMessage(String txt) throws Exception {

        txt = ISOUtil.padleft(txt, 32, ' ');
        return txt.getBytes();

    }

    private static String getDecodedMessage(byte[] msg) {

        String txt = new String(msg);
        txt = txt.trim();

        return txt;

    }

    private static SecretKey getSKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {

        Util.adjustDESParity(key);
        SecretKeyFactory sf = SecretKeyFactory.getInstance("DESede", p);
        DESedeKeySpec sp = new DESedeKeySpec(key);

        return sf.generateSecret(sp);

    }

}
