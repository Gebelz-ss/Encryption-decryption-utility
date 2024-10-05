package kz.fertyname.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class FuncUtil {
	   public void encryptFile(File inputFile, File outputFile,SymmetricCipher symmetricCipher,Rsa filecipher) throws Exception {
	        try (FileInputStream fis = new FileInputStream(inputFile);
	             FileOutputStream fos = new FileOutputStream(outputFile);
	             DataOutputStream dos = new DataOutputStream(fos)) {

	            byte[] encryptedE = symmetricCipher.encryptSymmetric(filecipher.e.toString().getBytes());
	            byte[] encryptedN = symmetricCipher.encryptSymmetric(filecipher.n.toString().getBytes());

	            dos.writeInt(encryptedE.length);
	            dos.write(encryptedE);
	            dos.writeInt(encryptedN.length);
	            dos.write(encryptedN);
	            dos.write(symmetricCipher.iv);

	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, symmetricCipher.secretKey, new IvParameterSpec(symmetricCipher.iv));

	            while ((bytesRead = fis.read(buffer)) != -1) {
	                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
	                if (encryptedBytes != null) {
	                    dos.write(encryptedBytes);
	                }
	            }
	            byte[] finalBytes = cipher.doFinal();
	            if (finalBytes != null) {
	                dos.write(finalBytes);
	            }
	        }
	    }

	   public void decryptFile(File inputFile, File outputFile,SymmetricCipher symmetricCipher,Rsa filecipher) throws Exception {
	        try (FileInputStream fis = new FileInputStream(inputFile);
	             DataInputStream dis = new DataInputStream(fis);
	             FileOutputStream fos = new FileOutputStream(outputFile)) {

	            int eLength = dis.readInt();
	            byte[] encryptedE = new byte[eLength];
	            dis.readFully(encryptedE);
	            BigInteger eDecrypted = new BigInteger(new String(symmetricCipher.decryptSymmetric(encryptedE)));

	            int nLength = dis.readInt();
	            byte[] encryptedN = new byte[nLength];
	            dis.readFully(encryptedN);
	            BigInteger nDecrypted = new BigInteger(new String(symmetricCipher.decryptSymmetric(encryptedN)));

	            byte[] iv = new byte[16];
	            dis.readFully(iv);

	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, symmetricCipher.secretKey, new IvParameterSpec(iv));

	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            while ((bytesRead = dis.read(buffer)) != -1) {
	                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
	                if (decryptedBytes != null) {
	                    baos.write(decryptedBytes);
	                }
	            }
	            byte[] finalBytes = cipher.doFinal();
	            if (finalBytes != null) {
	                baos.write(finalBytes);
	            }

	            fos.write(baos.toByteArray());

	            System.out.println("Decrypted Public Key (e): " + eDecrypted);
	            System.out.println("Decrypted Public Key (n): " + nDecrypted);
	        }
	    }
	    public String getFileExtension(String filename) {
	        int lastIndexOfDot = filename.lastIndexOf('.');
	        if (lastIndexOfDot == -1) {
	            return "";
	        }
	        return filename.substring(lastIndexOfDot + 1);
	    }
}
