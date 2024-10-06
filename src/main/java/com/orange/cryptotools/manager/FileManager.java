package com.orange.cryptotools.manager;

import java.io.File;

import com.orange.cryptotools.util.FuncUtil;
import com.orange.cryptotools.util.Rsa;
import com.orange.cryptotools.util.SymmetricCipher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileManager {
	Rsa rsa;
	SymmetricCipher symmetricCipher;

	public void encryptFile(File inputFile, File outputFile) {
		FuncUtil.encryptFile(inputFile, outputFile, symmetricCipher, rsa);
	}

	public void decryptFile(File inputFile, File outputFile) {
		FuncUtil.decryptFile(inputFile, outputFile, symmetricCipher, rsa);
	}
}