package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class Constants {

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String ALARM_TYPE = "alarm_type";
    public static final String ARRIVAL_TYPE = "arrival_type";
    public static final String EMAIL_ID = "email_id";
    public static final String FULL_NAME = "full_name";
    public static final String HANDED_THINGS_TO = "handed_things_to";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String PROFILE_PHOTO = "profile_photo";
    public static final String SCREEN_TITLE = "screen_title";
    public static final String SERVICE_TYPE = "service_type";
    public static final String IN_PROGRESS = "in progress";
    public static final String SOCIETY_SERVICE_PROBLEM = "society_service_problem";
    public static final String NOTIFICATION_UID = "notificationUID";
    public static final String NOTIFICATION_ID = "notificationID";
    public static final String USER_UID = "userUID";
    public static final String VISITOR_TYPE = "visitorType";
    public static final String VISITOR_PROFILE_PHOTO = "visitorProfilePhoto";
    public static final String MESSAGE = "message";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final int EDIT_TEXT_EMPTY_LENGTH = 0;
    public static final int CAB_NUMBER_FIELD_LENGTH = 2;
    public static final String COUNTRY_CODE_IN = "+91";
    public static final String HYPHEN = "-";

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 120;

    /* ------------------------------------------------------------- *
     * Shared Preference Keys
     * ------------------------------------------------------------- */

    public static final String NAMMA_APARTMENTS_PREFERENCE = "nammaApartmentsPreference";
    public static final String FIRST_TIME = "firstTime";
    public static final String LOGGED_IN = "loggedIn";

    /* ------------------------------------------------------------- *
     * Firebase objects
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_ADMIN = "admin";
    private static final String FIREBASE_CHILD_APARTMENTS = "apartments";
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
    public static final String FIREBASE_CHILD_EMAIL = "email";
    public static final String FIREBASE_CHILD_EMERGENCIES = "emergencies";
    private static final String FIREBASE_CHILD_FLATS = "flats";
    public static final String FIREBASE_CHILD_FAMILY_MEMBERS = "familyMembers";
    public static final String FIREBASE_CHILD_FLAT_MEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_FRIENDS = "friends";
    public static final String FIREBASE_CHILD_FULLNAME = "fullName";
    public static final String FIREBASE_CHILD_GRANTEDACCESS = "grantedAccess";
    public static final String FIREBASE_CHILD_GUESTS = "guests";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    public static final String FIREBASE_CHILD_LAUNDRIES = "laundries";
    public static final String FIREBASE_CHILD_MAIDS = "maids";
    public static final String FIREBASE_CHILD_MILKMEN = "milkmen";
    public static final String FIREBASE_CHILD_MOBILE_NUMBER = "mobileNumber";
    public static final String FIREBASE_CHILD_OWNERS_UID = "ownersUID";
    public static final String FIREBASE_CHILD_POSTAPPROVED_VISITORS = "postApprovedVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVED_VISITORS = "preApprovedVisitors";
    private static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_PERSONALDETAILS = "personalDetails";
    public static final String FIREBASE_CHILD_PRIVILEGES = "privileges";
    private static final String FIREBASE_CHILD_SOCIETIES = "societies";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_TIMEOFVISIT = "timeOfVisit";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_USER_DATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION = "societyServiceNotifications";
    private static final String FIREBASE_CHILD_SOCIETYSERVICES = "societyServices";
    public static final String FIREBASE_CHILD_GATE_NOTIFICATIONS = "gateNotifications";
    public static final String FIREBASE_CHILD_TIMESTAMP = "timestamp";

    public static final int FIREBASE_CHILD_RATING = 3;

    public static final String ENTERED = "Entered";
    public static final String NOT_ENTERED = "Not Entered";

    /* ------------------------------------------------------------- *
     * Firebase Values
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ACCEPTED = "Accepted";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    private static final DatabaseReference EMERGENCIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_EMERGENCIES);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_SOCIETYSERVICENOTIFICATION_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION).child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SOCIETYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICES);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference SOCIETIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_SOCIETIES);
    public static final DatabaseReference FLATS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_FLATS);
    public static final DatabaseReference APARTMENTS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_APARTMENTS);


    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_DELIVERY_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_EMERGENCY_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PUBLIC_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PUBLIC_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PUBLIC_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PREAPPROVED_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVED_VISITORS);
    public static final DatabaseReference PREAPPROVED_VISITORS_MOBILE_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER);
    public static final DatabaseReference POSTAPPROVED_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_POSTAPPROVED_VISITORS);

    /* ------------------------------------------------------------- *
     * Remote Message Keys
     * ------------------------------------------------------------- */

    public static final String REMOTE_MESSAGE = "message";
    public static final String REMOTE_NOTIFICATION_UID = "notification_uid";
    public static final String REMOTE_USER_UID = "user_uid";
    public static final String REMOTE_VISITOR_TYPE = "visitor_type";
    public static final String REMOTE_TYPE = "type";
    public static final String REMOTE_PROFILE_PHOTO = "profile_photo";

    /* ------------------------------------------------------------- *
     * Receiver Action Keys
     * ------------------------------------------------------------- */

    public static final String ACCEPT_BUTTON_CLICKED = "accept_button_clicked";
    public static final String REJECT_BUTTON_CLICKED = "reject_button_clicked";

    /* ------------------------------------------------------------- *
     * Application Specific
     * ------------------------------------------------------------- */

    public static final String FAMILY_MEMBER = "Family Member";
    public static final String FRIEND = "Friend";

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int GALLERY_PERMISSION_REQUEST_CODE = 5;

    public static final int DS_OTP_STATUS_REQUEST_CODE = 6;
    public static final int AFM_OTP_STATUS_REQUEST_CODE = 7;
    static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static final int SELECT_SOCIETY_SERVICE_REQUEST_CODE = 8;
    static final int SEND_SMS_PERMISSION_REQUEST_CODE = 2;

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoBoldItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BoldItalic.ttf");
    }

    public static Typeface setLatoItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Italic.ttf");
    }

    public static Typeface setLatoLightFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

}
