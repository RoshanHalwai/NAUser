package com.kirtanlabs.nammaapartments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.utilities.Constants;
import com.razorpay.Checkout;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_DATABASE_URL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.MASTER_BETA_ENV;
import static com.kirtanlabs.nammaapartments.utilities.Constants.MASTER_DEV_ENV;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SOCIETY_BETA_ENV;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SOCIETY_DEV_ENV;

/**
 * Global NammaApartment class for storing User's Information
 */
public class NammaApartmentsGlobal extends Application {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static String userUID;
    private NammaApartmentUser nammaApartmentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        final String FIREBASE_ENV = getCurrentEnvironment(this);
        final String DATABASE_URL = sharedPreferences.getString(FIREBASE_DATABASE_URL, getDatabaseURL(FIREBASE_ENV));
        initializeFirebase(this, DATABASE_URL, FIREBASE_ENV);

        /*Pre Load the contents of the Payment UI*/
        Checkout.preload(getApplicationContext());
    }

    /**
     * @param context      of the current Activity
     * @param DATABASE_URL which application is currently accessing
     * @param FIREBASE_ENV can be either {@link Constants#MASTER_BETA_ENV} or {@link Constants#MASTER_DEV_ENV}
     */
    public void initializeFirebase(final Context context, final String DATABASE_URL, final String FIREBASE_ENV) {
        switch (FIREBASE_ENV) {
            case MASTER_DEV_ENV:
            case SOCIETY_DEV_ENV:
                final FirebaseOptions DEV_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                        .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                        .setDatabaseUrl(DATABASE_URL)
                        .setStorageBucket("nammaapartments-development.appspot.com")
                        .setGcmSenderId("703896080530")
                        .setProjectId("nammaapartments-development")
                        .build();
                FirebaseApp.initializeApp(context, DEV_OPTIONS, FIREBASE_ENV);
                break;
            case MASTER_BETA_ENV:
            case SOCIETY_BETA_ENV:
                final FirebaseOptions BETA_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                        .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                        .setDatabaseUrl(DATABASE_URL)
                        .setStorageBucket("nammaapartments-beta.appspot.com")
                        .setGcmSenderId("896005326129")
                        .setProjectId("nammaapartments-beta")
                        .build();
                FirebaseApp.initializeApp(context, BETA_OPTIONS, FIREBASE_ENV);
        }

        //Configure Firebase so they point to new database instance
        new Constants().configureFirebase(FIREBASE_ENV);
    }
    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getCurrentEnvironment(final Context context) {
        return context.getPackageName().equals("com.kirtanlabs.nammaapartments") ? MASTER_DEV_ENV : MASTER_BETA_ENV;
    }

    public String getDatabaseURL(final String Environment) {
        final String DEFAULT_DEV_DATABASE_URL = "https://nammaapartments-development.firebaseio.com/";
        final String DEFAULT_BETA_DATABASE_URL = "https://nammaapartments-beta.firebaseio.com/";
        return Environment.equals(MASTER_DEV_ENV) ? DEFAULT_DEV_DATABASE_URL : DEFAULT_BETA_DATABASE_URL;
    }

    public DatabaseReference getUserDataReference() {
        UserFlatDetails userFlatDetails = nammaApartmentUser.getFlatDetails();
        return PRIVATE_USER_DATA_REFERENCE
                .child(userFlatDetails.getCity())
                .child(userFlatDetails.getSocietyName())
                .child(userFlatDetails.getApartmentName())
                .child(userFlatDetails.getFlatNumber());
    }

    public NammaApartmentUser getNammaApartmentUser() {
        return nammaApartmentUser;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setNammaApartmentUser(NammaApartmentUser nammaApartmentUser) {
        this.nammaApartmentUser = nammaApartmentUser;
        userUID = nammaApartmentUser.getUID();
    }

}
