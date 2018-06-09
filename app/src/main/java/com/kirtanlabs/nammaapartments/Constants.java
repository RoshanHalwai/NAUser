package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.graphics.Typeface;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class Constants {

    /* ------------------------------------------------------------- *
     * Namma Apartment GCM Channels
     * ------------------------------------------------------------- */

    public static final String CHANNEL_ID = "my_channel_01";

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String ALARM_TYPE = "alarm_type";
    public static final String ARRIVAL_TYPE = "arrival_type";
    public static final String GRANTED_ACCESS_TYPE = "granted_access_type";
    public static final String HANDED_THINGS_TO = "handed_things_to";
    public static final String IN_TIME = "in_time";
    public static final String NAME = "name";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String SERVICE_TYPE = "service_type";
    public static final String SCREEN_TITLE = "screen_title";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final int EDIT_TEXT_EMPTY_LENGTH = 0;
    public static final int EDIT_TEXT_MIN_LENGTH = 0;
    public static final String COUNTRY_CODE = "+91";

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 120;

    /* ------------------------------------------------------------- *
     * Firebase objects
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_MYVISITORS = "myVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String NOT_ENTERED = "Not Entered";
    public static final String FIREBASE_CHILD_COOK = "cooks";
    public static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_MY_DAILY_SERVICES = "myDailyServices";
    public static final String FIREBASE_COOK = "cook";
    public static final int FIREBASE_RATING = 3;

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int GALLERY_PERMISSION_REQUEST_CODE = 5;
    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    static final int SEND_SMS_PERMISSION_REQUEST_CODE = 2;

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBlackFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Black.ttf");
    }

    public static Typeface setLatoBlackItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BlackItalic.ttf");
    }

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoBoldItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BoldItalic.ttf");
    }

    public static Typeface setLatoHairlineFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Hairline.ttf");
    }

    public static Typeface setLatoHairlineItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-HairlineItalic.ttf");
    }

    public static Typeface setLatoItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Italic.ttf");
    }

    public static Typeface setLatoLightFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");
    }

    public static Typeface setLatoLightItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-LightItalic.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

}
