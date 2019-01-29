/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.android.cytex.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static final String ATTRIBUTE_TTF_KEY = "ttf_name";

    public static final String ATTRIBUTE_SCHEMA = "http://schemas.android.com/apk/lib/com.hitesh_sahu.retailapp.util";

    public final static String SHOPPING_LIST_TAG = "SHoppingListFragment";
    public static final String PRODUCT_OVERVIEW_FRAGMENT_TAG = "ProductOverView";
    public static final String MY_CART_FRAGMENT = "MyCartFragment";
    public static final String MY_ORDERS_FRAGMENT = "MYOrdersFragment";
    public static final String HOME_FRAGMENT = "HomeFragment";
    public static final String SEARCH_FRAGMENT_TAG = "SearchFragment";
    public static final String SETTINGS_FRAGMENT_TAG = "SettingsFragment";
    public static final String OTP_LOGIN_TAG = "OTPLogingFragment";
    public static final String CONTACT_US_FRAGMENT = "ContactUs";
    private static final String PREFERENCES_FILE = "materialsample_settings";
    private static String CURRENT_TAG = null;
    private static Map<String, Typeface> TYPEFACE = new HashMap<String, Typeface>();


    public static Typeface getFonts(Context context, String fontName) {
        Typeface typeface = TYPEFACE.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "font/"
                    + fontName);
            TYPEFACE.put(fontName, typeface);
        }
        return typeface;
    }

    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, SLIDE_UP, SLIDE_DOWN, FADE_IN, SLIDE_IN_SLIDE_OUT, FADE_OUT
    }

}
