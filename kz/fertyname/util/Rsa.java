package kz.fertyname.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Rsa {
	SymmetricCipher symmetricCipher = new SymmetricCipher();
    public BigInteger n, d, e;
    private int bitlen = 5000;

    public Rsa() throws Exception {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitlen / 2, random);
        BigInteger q = BigInteger.probablePrime(bitlen / 2, random);
        n = p.multiply(q);

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.probablePrime(bitlen / 2, random);
        while (phi.gcd(e).intValue() > 1) {
            e = e.add(BigInteger.ONE);
        }

        d = e.modInverse(phi);
    }


    public BigInteger encrypt(BigInteger message) throws Exception {
        return message.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger encrypted) throws Exception {
        return encrypted.modPow(d, n);
    }

    public BigInteger getPublicKeyE() {
        return e;
    }

    public BigInteger getPublicKeyN() {
        return n;
    }

    public BigInteger getPrivateKeyD() {
        return d;
    }
}