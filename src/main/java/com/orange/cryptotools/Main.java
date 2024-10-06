package com.orange.cryptotools;

import com.orange.cryptotools.manager.FileManager;
import com.orange.cryptotools.manager.UIManager;
import com.orange.cryptotools.util.Rsa;
import com.orange.cryptotools.util.SymmetricCipher;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Main {
	INSTANCE;

	static Rsa rsa = new Rsa();
	static SymmetricCipher symmetricCipher = new SymmetricCipher();
	static FileManager fileEncryptor = new FileManager(rsa, symmetricCipher);

	public static void main(String[] args) throws Exception {
		new UIManager(fileEncryptor);
	}
}
