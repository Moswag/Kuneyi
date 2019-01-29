package com.android.cytex.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class UserIdAndPasswordGenerator {

    private SecureRandom crunchifyRandomNo = new SecureRandom();

    private ArrayList<Object> crunchifyValueObj;
    public static String generatePassword() {
// TODO Auto-generated method stub
        UserIdAndPasswordGenerator passwordGenerator = new UserIdAndPasswordGenerator();
        StringBuilder crunchifyBuffer = new StringBuilder();
        for (int length = 0; length <8 ; length++) {
            crunchifyBuffer.append(passwordGenerator.crunchifyGetRandom());
        }
        return crunchifyBuffer.toString();
    }

    // Just initialize ArrayList crunchifyValueObj and add ASCII Decimal Values
    public UserIdAndPasswordGenerator() {

        crunchifyValueObj = new ArrayList<>();

// Adding ASCII Decimal value between 33 and 53
        for (int i = 33; i < 53; i++) {
            crunchifyValueObj.add((char) i);
        }

// Adding ASCII Decimal value between 54 and 85
        for (int i = 54; i < 85; i++) {
            crunchifyValueObj.add((char) i);
        }

// Adding ASCII Decimal value between 86 and 128
        for (int i = 86; i < 127; i++) {
            crunchifyValueObj.add((char) i);
        }
        crunchifyValueObj.add((char) 64);

// rotate() rotates the elements in the specified list by the specified distance. This will create strong password
        Collections.rotate(crunchifyValueObj, 5);
    }

    // Get Char value from above added Decimal values
// Enable Logging below if you want to debug
    public char crunchifyGetRandom() {
        char crunchifyChar = (char) this.crunchifyValueObj.get(crunchifyRandomNo.nextInt(this.crunchifyValueObj.size()));

// log(String.valueOf(crunchifyChar));
        return crunchifyChar;
    }
    public static String generateCommentID() {
        Random rand = new Random();
        double rand_dub = rand.nextDouble();
        String mString=String.valueOf(rand_dub);
        String userid=mString.substring(8, mString.length());
        return userid;
    }

}
