package com.mndt.ghost.doll.EncryptionString;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ghost on 2017/10/29.
 */
public class MD5 {
    public static String MD5(String sData) {
        try {
            // Create MD5 Hash
            final MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(sData.getBytes());
            final byte messageDigest[] = digest.digest();
            // Create Hex String
            final StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String sEncrt = Integer.toHexString(0xFF & messageDigest[i]);
                if (sEncrt.length() == 1) {
                    sEncrt = "0" + sEncrt;
                }
                hexString.append(sEncrt);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
