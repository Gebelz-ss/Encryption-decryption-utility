package kz.fertyname.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class SymmetricCipher {
    SecretKey secretKey;
    byte[] iv;
    public SymmetricCipher() throws Exception {
        SecureRandom random = new SecureRandom();
    	 KeyGenerator keyGen = KeyGenerator.getInstance("AES");
         keyGen.init(256);
         secretKey = keyGen.generateKey();

         iv = new byte[16];
         random.nextBytes(iv);
    }
    public byte[] encryptSymmetric(byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(message);
    }

    public byte[] decryptSymmetric(byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(encrypted);
    }
}
