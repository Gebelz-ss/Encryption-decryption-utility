package com.orange.cryptotools.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class SymmetricCipher {
	SecretKey secretKey;
	byte[] iv;

	@SneakyThrows
	public SymmetricCipher() {
		final var random = new SecureRandom();
		final var keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		secretKey = keyGen.generateKey();
		iv = new byte[16];
		random.nextBytes(iv);
	}

	@SneakyThrows
	public byte[] encryptSymmetric(final byte[] message) {
		final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return cipher.doFinal(message);
	}

	@SneakyThrows
	public byte[] decryptSymmetric(final byte[] encrypted) {
		final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return cipher.doFinal(encrypted);
	}
}