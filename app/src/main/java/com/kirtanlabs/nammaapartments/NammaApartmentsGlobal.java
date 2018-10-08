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
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ENVIRONMENT;
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

    private static final String MASTER_DEV_DATABASE_URL = "https://nammaapartments-development.firebaseio.com/";
    private static final String MASTER_BETA_DATABASE_URL = "https://nammaapartments-beta.firebaseio.com/";
    public static String userUID;
    private NammaApartmentUser nammaApartmentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        final String FIREBASE_ENV = sharedPreferences.getString(FIREBASE_ENVIRONMENT, MASTER_DEV_ENV);
        final String DATABASE_URL = sharedPreferences.getString(FIREBASE_DATABASE_URL, MASTER_DEV_DATABASE_URL);

        //TODO: Change this when App is rolled out in Play Store
        /*Initialize Master Dev or Master Beta so FirebaseAuth and Firebase Storage can use them*/
        initializeFirebaseApp(this, MASTER_DEV_ENV, MASTER_DEV_DATABASE_URL);

        //TODO: Change this when App is rolled out in Play Store
        /*Initialize Society Environment so users can use new database instance*/
        if (!FIREBASE_ENV.equals(MASTER_DEV_ENV)) {
            initializeFirebaseApp(this, FIREBASE_ENV, DATABASE_URL);
        }

        /*Pre Load the contents of the Payment UI*/
        Checkout.preload(getApplicationContext());
    }

    /**
     * @param context      of the Activity
     * @param FIREBASE_ENV Environment of the application
     * @param DATABASE_URL Database URL of the application
     */
    public void initializeFirebaseApp(final Context context, final String FIREBASE_ENV, final String DATABASE_URL) {
        switch (FIREBASE_ENV) {
            case SOCIETY_BETA_ENV:
                final FirebaseOptions SOCIETY_BETA_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                        .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                        .setDatabaseUrl(DATABASE_URL)
                        .setStorageBucket("nammaapartments-beta.appspot.com")
                        .setGcmSenderId("896005326129")
                        .setProjectId("nammaapartments-beta")
                        .build();
                FirebaseApp.initializeApp(context, SOCIETY_BETA_OPTIONS, FIREBASE_ENV);
            case MASTER_BETA_ENV:
                final FirebaseOptions MASTER_BETA_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                        .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                        .setDatabaseUrl(MASTER_BETA_DATABASE_URL)
                        .setStorageBucket("nammaapartments-beta.appspot.com")
                        .setGcmSenderId("896005326129")
                        .setProjectId("nammaapartments-beta")
                        .build();
                FirebaseApp.initializeApp(context, MASTER_BETA_OPTIONS, MASTER_BETA_ENV);
                break;
            case SOCIETY_DEV_ENV:
                final FirebaseOptions SOCIETY_DEV_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                        .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                        .setDatabaseUrl(DATABASE_URL)
                        .setStorageBucket("nammaapartments-development.appspot.com")
                        .setGcmSenderId("703896080530")
                        .setProjectId("nammaapartments-development")
                        .build();
                FirebaseApp.initializeApp(context, SOCIETY_DEV_OPTIONS, FIREBASE_ENV);
                break;
            case MASTER_DEV_ENV:
                final FirebaseOptions MASTER_DEV_OPTIONS = new FirebaseOptions.Builder()
                        .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                        .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                        .setDatabaseUrl(MASTER_DEV_DATABASE_URL)
                        .setStorageBucket("nammaapartments-development.appspot.com")
                        .setGcmSenderId("703896080530")
                        .setProjectId("nammaapartments-development")
                        .build();
                FirebaseApp.initializeApp(context, MASTER_DEV_OPTIONS, MASTER_DEV_ENV);
                break;
        }

        //Configure Firebase so they point to new database instance
        new Constants().configureFB(FIREBASE_ENV);
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

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
