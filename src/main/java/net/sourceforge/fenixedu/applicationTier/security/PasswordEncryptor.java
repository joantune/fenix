/*
 * Created on 16/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jpvl
 */
public abstract class PasswordEncryptor {
    static public String encryptPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must be not null!");
        }

        byte buffer[] = new byte[password.length()];
        MessageDigest algorithm = getMessageDigest();
        buffer = password.getBytes();
        algorithm.update(buffer);

        byte[] digest = algorithm.digest();
        BigInteger bi = new BigInteger(digest);
        String pairLength = bi.toString(16);

        // append 0 if length is even
        if (pairLength.length() % 2 != 0) {
            pairLength = "0" + pairLength;
        }
        return pairLength;
    }

    static public boolean areEquals(String encryptedPassword, String notEncryptedPassword) {
        String digest = encryptPassword(notEncryptedPassword);
        return digest.equals(encryptedPassword);
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        algorithm.reset();
        return algorithm;
    }
}