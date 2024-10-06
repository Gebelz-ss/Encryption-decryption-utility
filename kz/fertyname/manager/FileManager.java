package kz.fertyname.manager;

import java.io.File;

import kz.fertyname.util.FuncUtil;
import kz.fertyname.util.Rsa;
import kz.fertyname.util.SymmetricCipher;

public class FileManager {
    private Rsa rsa;
    private SymmetricCipher symmetricCipher;
    private FuncUtil funcUtil;

    public FileManager(Rsa rsa, SymmetricCipher symmetricCipher, FuncUtil funcUtil) {
        this.rsa = rsa;
        this.symmetricCipher = symmetricCipher;
        this.funcUtil = funcUtil;
    }

    public void encryptFile(File inputFile, File outputFile) throws Exception {
        funcUtil.encryptFile(inputFile, outputFile, symmetricCipher, rsa);
    }

    public void decryptFile(File inputFile, File outputFile) throws Exception {
        funcUtil.decryptFile(inputFile, outputFile, symmetricCipher, rsa);
    }
}