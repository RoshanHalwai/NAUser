package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL_ID = "email_id";

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

    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_COOKS = "cooks";
    public static final String FIREBASE_CHILD_MAIDS = "maids";
    public static final String FIREBASE_CHILD_DRIVERS = "drivers";
    public static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    public static final String FIREBASE_FAMILYMEMBERS = "familyMembers";
    public static final String FIREBASE_CHILD_LAUNDRIES = "laundries";
    public static final String FIREBASE_CHILD_MILKMEN = "milkmen";
    public static final String FIREBASE_CHILD_DAILYNEWSPAPERS = "dailyNewspapers";
    public static final String FIREBASE_CHILD_CHILDDAYCARES = "childDayCares";
    public static final String FIREBASE_CHILD_DATEANDTIMEOFVISIT = "dateAndTimeOfVisit";
    public static final String FIREBASE_MYCOOK = "myCook";
    public static final String FIREBASE_MYMAID = "myMaid";
    public static final String FIREBASE_MYCHILDDAYCARE = "myChildDayCare";
    public static final String FIREBASE_MYDAILYNEWSPAPER = "myDailyNewspaper";
    public static final String FIREBASE_MYMILKMAN = "myMilkman";
    public static final String FIREBASE_MYLAUNDRY = "myLaundry";
    public static final String FIREBASE_MYDRIVER = "myDriver";
    public static final String FIREBASE_CHILD_CARBIKECLEANERS = "carBikeCleaners";
    public static final String FIREBASE_CHILD_MYFAMILYMEMBERS = "myFamilyMembers";
    public static final String FIREBASE_CHILD_MYDAILYSERVICES = "myDailyServices";
    public static final String FIREBASE_CHILD_MYCARBIKECLEANER = "myCarBikeCleaner";
    public static final String FIREBASE_CHILD_MYVISITORS = "myVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";

    public static final int FIREBASE_CHILD_RATING = 3;
    public static final String NOT_ENTERED = "Not Entered";

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int GALLERY_PERMISSION_REQUEST_CODE = 5;
    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    static final int SEND_SMS_PERMISSION_REQUEST_CODE = 2;

    public static final int DS_OTP_STATUS_REQUEST_CODE = 6;
    public static final int AFM_OTP_STATUS_REQUEST_CODE = 7;

    /* ------------------------------------------------------------- *
     * Mapping Daily Services
     * ------------------------------------------------------------- */

    public static final Map<String, String> DAILY_SERVICE_MAP;

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put(FIREBASE_MYCOOK, FIREBASE_CHILD_COOKS);
        aMap.put(FIREBASE_MYDRIVER, FIREBASE_CHILD_DRIVERS);
        aMap.put(FIREBASE_MYMILKMAN, FIREBASE_CHILD_MILKMEN);
        aMap.put(FIREBASE_MYLAUNDRY, FIREBASE_CHILD_LAUNDRIES);
        aMap.put(FIREBASE_MYMAID, FIREBASE_CHILD_MAIDS);
        aMap.put(FIREBASE_MYCHILDDAYCARE, FIREBASE_CHILD_CHILDDAYCARES);
        aMap.put(FIREBASE_MYDAILYNEWSPAPER, FIREBASE_CHILD_DAILYNEWSPAPERS);
        aMap.put(FIREBASE_CHILD_MYCARBIKECLEANER, FIREBASE_CHILD_CARBIKECLEANERS);
        DAILY_SERVICE_MAP = Collections.unmodifiableMap(aMap);
    }

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
