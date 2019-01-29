package com.android.cytex.util;

/**
 * Created by moswag on 1/30/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SelectedOptionSession {
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "SelectedPref";


    // User name (make variable public to access from outside)
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COMPANYNAME = "category";


    // Constructor
    public SelectedOptionSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createSelectedOption(String category){

        // Storing category in pref
        editor.putString(KEY_CATEGORY, category);

        // commit changes
        editor.commit();
    }

    //Create login session
    public void createSelectedOption(String company_name,boolean check){

        // Storing category in pre
        editor.putString(KEY_COMPANYNAME, company_name);

        // commit changes
        editor.commit();
    }



    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getSelectedData(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_CATEGORY, pref.getString(KEY_CATEGORY, null));
        user.put(KEY_COMPANYNAME, pref.getString(KEY_COMPANYNAME, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */

}