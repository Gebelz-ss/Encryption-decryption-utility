package com.orange.cryptotools.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Rsa {
	BigInteger n, d;
	@NonFinal
	BigInteger e;
	int bitlen = 5000;

	@SneakyThrows
	public Rsa() {
		final var random = new SecureRandom();
		final var p = BigInteger.probablePrime(bitlen / 2, random);
		final var q = BigInteger.probablePrime(bitlen / 2, random);
		n = p.multiply(q);

		final var phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		e = BigInteger.probablePrime(bitlen / 2, random);
		while (phi.gcd(e).intValue() > 1) {
			e = e.add(BigInteger.ONE);
		}

		d = e.modInverse(phi);
	}

	@SneakyThrows
	public BigInteger encrypt(final BigInteger message) {
		return message.modPow(e, n);
	}

	@SneakyThrows
	public BigInteger decrypt(final BigInteger encrypted) {
		return encrypted.modPow(d, n);
	}
}