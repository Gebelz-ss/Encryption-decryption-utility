package com.orange.cryptotools.util;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.math.BigInteger;
import java.util.Optional;

@UtilityClass
@Slf4j
public class FuncUtil {

	@SneakyThrows
	public void encryptFile(final File inputFile, final File outputFile, final SymmetricCipher symmetricCipher,
			final Rsa filecipher) {
		@Cleanup
		final var fis = new BufferedInputStream(new FileInputStream(inputFile));
		@Cleanup
		final var fos = new BufferedOutputStream(new FileOutputStream(outputFile));
		@Cleanup
		final var dos = new DataOutputStream(fos);

		final var encryptedE = symmetricCipher.encryptSymmetric(filecipher.getE().toString().getBytes());
		final var encryptedN = symmetricCipher.encryptSymmetric(filecipher.getN().toString().getBytes());

		dos.writeInt(encryptedE.length);
		dos.write(encryptedE);
		dos.writeInt(encryptedN.length);
		dos.write(encryptedN);
		dos.write(symmetricCipher.getIv());

		final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, symmetricCipher.getSecretKey(), new IvParameterSpec(symmetricCipher.getIv()));

		final var buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = fis.read(buffer)) != -1) {
			final var encryptedBytes = cipher.update(buffer, 0, bytesRead);
			if (encryptedBytes != null) {
				dos.write(encryptedBytes);
			}
		}
		final var finalBytes = cipher.doFinal();
		if (finalBytes != null) {
			dos.write(finalBytes);
		}
	}

	@SneakyThrows
	public void decryptFile(final File inputFile, final File outputFile, final SymmetricCipher symmetricCipher,
			final Rsa filecipher) {
		@Cleanup
		final var fis = new BufferedInputStream(new FileInputStream(inputFile));
		@Cleanup
		final var dis = new DataInputStream(fis);
		@Cleanup
		final var fos = new BufferedOutputStream(new FileOutputStream(outputFile));

		final var eLength = dis.readInt();
		final var encryptedE = new byte[eLength];
		dis.readFully(encryptedE);
		final var eDecrypted = new BigInteger(new String(symmetricCipher.decryptSymmetric(encryptedE)));

		final var nLength = dis.readInt();
		final var encryptedN = new byte[nLength];
		dis.readFully(encryptedN);
		final var nDecrypted = new BigInteger(new String(symmetricCipher.decryptSymmetric(encryptedN)));

		final var iv = new byte[16];
		dis.readFully(iv);

		final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, symmetricCipher.getSecretKey(), new IvParameterSpec(iv));

		final var buffer = new byte[8192];
		int bytesRead;
		@Cleanup
		final var baos = new ByteArrayOutputStream();

		while ((bytesRead = dis.read(buffer)) != -1) {
			final var decryptedBytes = cipher.update(buffer, 0, bytesRead);
			if (decryptedBytes != null) {
				baos.write(decryptedBytes);
			}
		}
		final var finalBytes = cipher.doFinal();
		if (finalBytes != null) {
			baos.write(finalBytes);
		}

		fos.write(baos.toByteArray());

		log.info("Decrypted Public Key (e): %s".formatted(eDecrypted));
		log.info("Decrypted Public Key (n): %s".formatted(nDecrypted));
	}

	public String getFileExtension(final String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf('.') + 1)).orElse("");
	}
}