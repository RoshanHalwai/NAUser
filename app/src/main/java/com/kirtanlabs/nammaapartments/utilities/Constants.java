package com.kirtanlabs.nammaapartments.utilities;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

                            /**------------------------------------------------------------- *
                             *----------------DO NOT AUTO-FORMAT THIS FILE-------------------*
                             *-------------------------------------------------------------- */
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
    public static final String SOCIETY_SERVICE_TYPE = "societyServiceType";
    public static final String USER_UID = "userUID";
    public static final String VISITOR_TYPE = "visitorType";
    public static final String VISITOR_PROFILE_PHOTO = "visitorProfilePhoto";
    public static final String MESSAGE = "message";

    /* ------------------------------------------------------------- *
     * Notification
     * ------------------------------------------------------------- */

    public static final String NOTIFICATION_EXPAND_MSG = "Slide down on note to respond";
    public static final String NOTIFICATION_EXPAND_TITLE = "Namma Apartments";

    /* ------------------------------------------------------------- *
     * Society Service Keys
     * ------------------------------------------------------------- */

    public static final String PLUMBER="plumber";
    public static final String CARPENTER="carpenter";
    public static final String ELECTRICIAN="electrician";

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

    public static final int OTP_TIMER = 60;

    /* ------------------------------------------------------------- *
     * Shared Preference Keys
     * ------------------------------------------------------------- */

    public static final String NAMMA_APARTMENTS_PREFERENCE = "nammaApartmentsPreference";
    public static final String FIRST_TIME = "firstTime";
    public static final String LOGGED_IN = "loggedIn";
    public static final String PLUMBER_SERVICE_NOTIFICATION_UID = "plumberServiceNotificationUid";
    public static final String CARPENTER_SERVICE_NOTIFICATION_UID = "carpenterServiceNotificationUid";
    public static final String ELECTRICIAN_SERVICE_NOTIFICATION_UID = "electricianServiceNotificationUid";
    public static final String GARBAGE_MANAGEMENT_SERVICE_NOTIFICATION_UID = "garbageManagementNotificationUid";

    /* ------------------------------------------------------------- *
     * Firebase Values
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_ADMIN = "admin";
    public static final String FIREBASE_CHILD_CARBIKECLEANERS = "carBikeCleaners";
    public static final String FIREBASE_CHILD_CHILDDAYCARES = "childDayCares";
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
    public static final String FIREBASE_CHILD_FAMILY_MEMBERS = "familyMembers";
    public static final String FIREBASE_CHILD_FLAT_MEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_FRIENDS = "friends";
    public static final String FIREBASE_CHILD_FULLNAME = "fullName";
    public static final String FIREBASE_CHILD_GARBAGE_MANAGEMENT = "garbageManagement";
    public static final String FIREBASE_CHILD_GUESTS = "guests";
    public static final String FIREBASE_CHILD_GRANTEDACCESS = "grantedAccess";
    public static final String FIREBASE_CHILD_GUARD = "guard";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    public static final String FIREBASE_CHILD_LAUNDRIES = "laundries";
    public static final String FIREBASE_CHILD_MAIDS = "maids";
    public static final String FIREBASE_CHILD_MILKMEN = "milkmen";
    public static final String FIREBASE_CHILD_MOBILE_NUMBER = "mobileNumber";
    public static final String FIREBASE_CHILD_OWNERS_UID = "ownersUID";
    public static final String FIREBASE_CHILD_PACKAGES = "packages";
    public static final String FIREBASE_CHILD_POSTAPPROVED = "postApproved";
    public static final String FIREBASE_CHILD_PREAPPROVED = "preApproved";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    public static final String FIREBASE_CHILD_PERSONALDETAILS = "personalDetails";
    public static final String FIREBASE_CHILD_PRIVILEGES = "privileges";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_TIMEOFVISIT = "timeOfVisit";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_USER_DATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION = "societyServiceNotifications";
    public static final String FIREBASE_CHILD_GATE_NOTIFICATIONS = "gateNotifications";
    public static final String FIREBASE_CHILD_TIMESTAMP = "timestamp";
    public static final int FIREBASE_CHILD_RATING = 3;
    public static final String FIREBASE_CHILD_ACCEPTED = "Accepted";
    public static final String FIREBASE_CHILD_REJECTED = "Rejected";
    private static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";
    private static final String FIREBASE_CHILD_FLATS = "flats";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    private static final String FIREBASE_CHILD_SOCIETIES = "societies";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICES = "societyServices";
    public static final String FIREBASE_CHILD_VEHICLES = "vehicles";

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
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference ALL_SOCIETYSERVICENOTIFICATION_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION).child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SOCIETY_SERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_SERVICES);
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    public static final DatabaseReference PRIVATE_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    private static final DatabaseReference VEHICLES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VEHICLES);
    public static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference ALL_VEHICLES_REFERENCE = VEHICLES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_VEHICLES_REFERENCE = VEHICLES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    public static final DatabaseReference PRIVATE_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference SOCIETIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_SOCIETIES);
    public static final DatabaseReference FLATS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_FLATS);
    public static final DatabaseReference APARTMENTS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_APARTMENTS);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference EMERGENCIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_EMERGENCIES);
    public static final DatabaseReference PRIVATE_EMERGENCY_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PUBLIC_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference GUARD_REFERENCE = SOCIETY_SERVICES_REFERENCE.child(FIREBASE_CHILD_GUARD);

    /* ------------------------------------------------------------- *
     * Receiver Action Keys
     * ------------------------------------------------------------- */

    public static final String ACCEPT_BUTTON_CLICKED = "accept_button_clicked";
    public static final String REJECT_BUTTON_CLICKED = "reject_button_clicked";

    /* ------------------------------------------------------------- *
     * Application Specific
     * ------------------------------------------------------------- */

    public static final String ENTERED = "Entered";
    public static final String NOT_ENTERED = "Not Entered";
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
    public static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static final int SELECT_SOCIETY_SERVICE_REQUEST_CODE = 8;
    public static final int SEND_SMS_PERMISSION_REQUEST_CODE = 2;

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
