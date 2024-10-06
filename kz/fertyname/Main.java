package kz.fertyname;

import kz.fertyname.manager.FileManager;
import kz.fertyname.manager.UIManager;
import kz.fertyname.util.FuncUtil;
import kz.fertyname.util.Rsa;
import kz.fertyname.util.SymmetricCipher;


public class Main {
	   public static void main(String[] args) throws Exception {
	        Rsa rsa = new Rsa();
	        FuncUtil funcUtil = new FuncUtil();
	        SymmetricCipher symmetricCipher = new SymmetricCipher();
	        FileManager fileEncryptor = new FileManager(rsa, symmetricCipher, funcUtil);
	        new UIManager(fileEncryptor);
	    }
}

