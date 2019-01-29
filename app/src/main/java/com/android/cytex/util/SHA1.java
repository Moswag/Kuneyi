package com.android.cytex.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SHA1 {

    private static String sha1(String input) {

        try {
            MessageDigest m = MessageDigest.getInstance("SHA");
            m.update(input.getBytes(), 0, input.length());
            String output = new BigInteger(1, m.digest()).toString(16);
            return output;
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    public static String sha(String passwd, String username) {

        // salt it
        if (passwd == null)
            passwd = UUID.randomUUID().toString(); // quick hack to prevent null pointer exception
        String in = sha1(passwd.concat(username));
        return sha1(in.concat(passwd));
        // return sha1(passwd);
    }

    public static void main(String[] args) {

        System.out.println(SHA1.sha("tawana", "tawana"));
    }
}
