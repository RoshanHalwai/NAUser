package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public static final String DAILY_SERVICE_OBJECT = "daily_service_object";
    public static final String EMAIL_ID = "email_id";
    public static final String FAMILY_MEMBER_OBJECT = "family_member_object";
    public static final String FULL_NAME = "full_name";
    public static final String GRANTED_ACCESS_TYPE = "granted_access_type";
    public static final String HANDED_THINGS_TO = "handed_things_to";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String PROFILE_PHOTO = "profile_photo";
    public static final String SCREEN_TITLE = "screen_title";
    public static final String SERVICE_TYPE = "service_type";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final int EDIT_TEXT_EMPTY_LENGTH = 0;
    public static final int CAB_NUMBER_FIELD_LENGTH = 2;
    public static final String COUNTRY_CODE_IN = "+91";
    public static final String CAB_NUMBER_HYPHEN_STORAGE = "-";

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 120;

    /* ------------------------------------------------------------- *
     * Firebase objects
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    public static final String FIREBASE_CHILD_CARBIKECLEANERS = "carBikeCleaners";
    public static final String FIREBASE_CHILD_CHILDDAYCARES = "childDayCares";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";
    public static final String FIREBASE_CHILD_COOKS = "cooks";
    public static final String FIREBASE_CHILD_CABS = "cabs";
    public static final String FIREBASE_CHILD_DELIVERIES = "deliveries";
    public static final String FIREBASE_CHILD_DAILYNEWSPAPERS = "dailyNewspapers";
    public static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    public static final String FIREBASE_CHILD_DAILYSERVICE_TYPE = "dailyServiceType";
    public static final String FIREBASE_CHILD_DATEANDTIMEOFVISIT = "dateAndTimeOfVisit";
    public static final String FIREBASE_CHILD_DRIVERS = "drivers";
    public static final String FIREBASE_CHILD_FLATS = "flats";
    public static final String FIREBASE_CHILD_FAMILY_MEMBERS = "familyMembers";
    public static final String FIREBASE_CHILD_FLAT_MEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_FRIENDS = "friends";
    public static final String FIREBASE_CHILD_FULLNAME = "fullName";
    public static final String FIREBASE_CHILD_GRANTEDACCESS = "grantedAccess";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    public static final String FIREBASE_CHILD_HANDED_THINGS_DESCRIPTION = "handedThingsDescription";
    public static final String FIREBASE_CHILD_LAUNDRIES = "laundries";
    public static final String FIREBASE_CHILD_MAIDS = "maids";
    public static final String FIREBASE_CHILD_MILKMEN = "milkmen";
    private static final String FIREBASE_CHILD_MYCARBIKECLEANER = "myCarBikeCleaner";
    private static final String FIREBASE_MYCHILDDAYCARE = "myChildDayCare";
    private static final String FIREBASE_MYCOOK = "myCook";
    private static final String FIREBASE_MYDAILYNEWSPAPER = "myDailyNewspaper";
    public static final String FIREBASE_CHILD_MYDAILYSERVICES = "myDailyServices";
    private static final String FIREBASE_MYDRIVER = "myDriver";
    private static final String FIREBASE_MYMAID = "myMaid";
    private static final String FIREBASE_MYMILKMAN = "myMilkman";
    private static final String FIREBASE_MYLAUNDRY = "myLaundry";
    public static final String FIREBASE_CHILD_MYVISITORS = "myVisitors";
    public static final String FIREBASE_CHILD_MYCABS = "myCabs";
    public static final String FIREBASE_CHILD_MYDELIVERIES = "myDeliveries ";
    public static final String FIREBASE_CHILD_OWNERSUID = "ownersUID";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    private static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_PHONENUMBER = "phoneNumber";
    public static final String FIREBASE_CHILD_PERSONALDETAILS = "personalDetails";
    public static final String FIREBASE_CHILD_PRIVILEGES = "privileges";
    public static final String FIREBASE_CHILD_SOCIETIES = "societies";
    public static final String FIREBASE_CHILD_TIMEOFVISIT = "timeOfVisit";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_USER_DATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";

    public static final int FIREBASE_CHILD_RATING = 3;

    public static final String ENTERED = "Entered";
    public static final String NOT_ENTERED = "Not Entered";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    private static final DatabaseReference FLATS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_FLATS);
    public static final DatabaseReference PRIVATE_FLATS_REFERENCE = FLATS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_DELIVERY_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PUBLIC_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PUBLIC_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PREAPPROVED_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORS);
    public static final DatabaseReference PREAPPROVED_VISITORS_MOBILE_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER);


    /* ------------------------------------------------------------- *
     * Family member/Friend Relation retrieval
     * ------------------------------------------------------------- */
    public static final String FAMILY_MEMBER =  "Family Member";
    public static final String FRIEND = "Friend";

    /* ------------------------------------------------------------- *
     * Mapping Daily Services
     * ------------------------------------------------------------- */

    private static final Map<String, String> DAILY_SERVICE_MAP;
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 3;

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int GALLERY_PERMISSION_REQUEST_CODE = 5;

    public static final int DS_OTP_STATUS_REQUEST_CODE = 6;
    public static final int AFM_OTP_STATUS_REQUEST_CODE = 7;
    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    static final int SEND_SMS_PERMISSION_REQUEST_CODE = 2;

    static {
        final Map<String, String> aMap = new HashMap<>();
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
