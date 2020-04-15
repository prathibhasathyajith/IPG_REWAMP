package com.epic.ipg.util.common;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.epic.ipg.util.varlist.CustomErrorVarList;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jpos.iso.ISOUtil;

/**
 *
 * @author Asela Indika
 */
public class KeyStoreEncryptor {

    // encrypt string
    public String rsaEncrypt(String keybyte, PublicKey publicK) throws CustomException {
        try {
            String encryptedvalue;

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicK);
            byte[] encrypted;
            encrypted = c.doFinal(keybyte.getBytes());
            encryptedvalue = ISOUtil.hexString(encrypted);
            return encryptedvalue;

        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.RSA_ENCRYPT_ERROR);
        }
    }

    //decrypt String
    public byte[] rsaDecrypt(byte[] keybyte, PrivateKey privateKey) throws CustomException {
        try {
            byte[] decryptedbyte;
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedbyte = c.doFinal(keybyte);
            return decryptedbyte;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.RSA_DECRYPT_ERROR);
        }
    }

// encrypt string
    public byte[] desEncrypt(String keybyte, Key key) throws CustomException {
        try {
            byte[] encryptedvalue;
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.ENCRYPT_MODE, key);
            encryptedvalue = c.doFinal(keybyte.getBytes());
            return encryptedvalue;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.DES_DECRYPT_ERROR);
        }
    }

    //decrypt String
    public String desDecrypt(byte[] keybyte, Key key) throws CustomException {
        try {
            String decrypted;
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedbyte = c.doFinal(keybyte);
            decrypted = new String(decryptedbyte);
            return decrypted;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.DES_DECRYPT_ERROR);
        }
    }

    // sign String 
    public byte[] sign(String value, PrivateKey privatekey) throws CustomException {

        try {
            byte[] byteSignedData;
            Signature mySign = Signature.getInstance("MD5withRSA");
            mySign.initSign(privatekey);
            mySign.update(value.getBytes());
            byteSignedData = mySign.sign();
            return byteSignedData;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.SIGN_ERROR);
        }
    }

    // veryfy sign value
    public boolean isVerifysign(String signature, byte[] byteSignedData, PublicKey publicKey) throws CustomException {
        boolean verifySign;
        try {
            Signature myVerifySign = Signature.getInstance("MD5withRSA");
            myVerifySign.initVerify(publicKey);
            myVerifySign.update(signature.getBytes());
            verifySign = myVerifySign.verify(byteSignedData);

            if (verifySign) {
                Logger.getLogger(KeyStoreEncryptor.class
                        .getName()).log(Level.INFO, " Successfully validated Signature ");

            } else {
                Logger.getLogger(KeyStoreEncryptor.class
                        .getName()).log(Level.INFO, " Error in validating Signature ");
            }
            return verifySign;

        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.SIGN_VERIFY_ERROR);
        }
    }

    // get public key from keystore
    public PublicKey getPublicKeyByKeyStore(String keypath, String pass, String alias) throws CustomException {

        try {
            PublicKey publicKey = null;
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // get user password and file input stream
            char[] password = pass.toCharArray();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(keypath)) {
                ks.load(fis, password);
                Enumeration<String> enumaration = ks.aliases();
                while (enumaration.hasMoreElements()) {
                    String aliases = enumaration.nextElement();
                    if (aliases.equals(alias)) {
                        java.security.cert.Certificate cert = ks.getCertificate(aliases);
                        publicKey = cert.getPublicKey();
                    }
                }
            }
            return publicKey;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    // get public key from keystore
    public PublicKey getPublicKeyByCertificate(String keypath) throws CustomException {
        PublicKey publicKey;
        try {
            try (FileInputStream fileInputStream = new FileInputStream(new File(keypath))) {
                Security.addProvider(new BouncyCastleProvider());
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
                X509Certificate x509Cert = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
                publicKey = x509Cert.getPublicKey();
            }

            return publicKey;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    // get private key from keystore
    public PrivateKey getPrivateKeyByKeyStore(String keypath, String keypass, String aliaspass, String alias) throws CustomException {
        PrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // get user password and file input stream
            char[] keypassword = keypass.toCharArray();
            char[] aliaspassword = aliaspass.toCharArray();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(keypath)) {
                ks.load(fis, keypassword);
                Enumeration<String> enumaration = ks.aliases();
                while (enumaration.hasMoreElements()) {
                    String aliases = enumaration.nextElement();
                    if (aliases.equals(alias) && (ks.getKey(aliases, aliaspassword) instanceof PrivateKey)) {
                        privateKey = (PrivateKey) ks.getKey(aliases, aliaspassword);
                    }
                }
            }
            return privateKey;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    public String getPriveteKeyModulus(String keypath, String keypass, String aliaspass, String alias) throws CustomException {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // get user password and file input stream
            char[] keypassword = keypass.toCharArray();
            char[] aliaspassword = aliaspass.toCharArray();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(keypath)) {
                ks.load(fis, keypassword);
                Enumeration<String> enumaration = ks.aliases();
                while (enumaration.hasMoreElements()) {
                    String aliases = enumaration.nextElement();
                    if (aliases.equals(alias) && (ks.getKey(aliases, aliaspassword) instanceof PrivateKey)) {
                        return ((RSAKey) ks.getKey(aliases, aliaspassword)).getModulus().toString();
                    }
                }
            }
            return null;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    public SecretKey generateDESKeyByModulus(String modulus) throws CustomException {
        try {
            SecretKey desKey;
            String keymodulus = modulus.substring(0, 8);
            Logger.getLogger(KeyStoreEncryptor.class.getName()).log(Level.INFO, "key mod : {0}", keymodulus);

            DESKeySpec dks = new DESKeySpec(keymodulus.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            desKey = skf.generateSecret(dks);

            return desKey;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    public SecretKey generateTripleDESKey(PrivateKey key) throws CustomException {
        SecretKey tripleDES;
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key.getEncoded());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
            tripleDES = skf.generateSecret(dks);
            return tripleDES;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    // encrypt string
    public byte[] tripleDESEncrypt(byte[] keybyte, Key key) throws CustomException {
        byte[] encryptedvalue;
        try {
            Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            encryptedvalue = c.doFinal(keybyte);
            return encryptedvalue;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }

    //decrypt String
    public byte[] tripleDESDecrypt(byte[] keybyte, Key key) throws CustomException {
        byte[] decryptedbyte;
        try {
            Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, key);
            decryptedbyte = c.doFinal(keybyte);
            return decryptedbyte;
        } catch (Exception ps) {
            throw new CustomException(CustomErrorVarList.PUBLIC_KEYSTORE_ERROR);
        }
    }
}
