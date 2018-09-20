package com.kirtanlabs.nammaapartments;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.razorpay.Checkout;

import static com.kirtanlabs.nammaapartments.utilities.Constants.BETA_ENV;
import static com.kirtanlabs.nammaapartments.utilities.Constants.DEV_ENV;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USER_DATA_REFERENCE;

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

        final FirebaseOptions BETA_ENV_OPTIONS = new FirebaseOptions.Builder()
                .setApplicationId("1:896005326129:android:6ff623e10a2664c5")
                .setApiKey("AIzaSyD3Ar2J0gJ8AiL8s0BVlkWP3PXbux3bvKU")
                .setDatabaseUrl("https://nammaapartments-beta.firebaseio.com/")
                .setStorageBucket("nammaapartments-beta.appspot.com")
                .setGcmSenderId("896005326129")
                .setProjectId("nammaapartments-beta")
                .build();

        final FirebaseOptions DEV_ENV_OPTIONS = new FirebaseOptions.Builder()
                .setApplicationId("1:703896080530:android:67ab068074f57ad3")
                .setApiKey("AIzaSyA-F_DSWIb-HRx1bAE5f5aW1TT4npAME60")
                .setDatabaseUrl("https://nammaapartments-development.firebaseio.com/")
                .setStorageBucket("nammaapartments-development.appspot.com")
                .setGcmSenderId("703896080530")
                .setProjectId("nammaapartments-development")
                .build();

        FirebaseApp.initializeApp(this, BETA_ENV_OPTIONS, BETA_ENV);
        FirebaseApp.initializeApp(this, DEV_ENV_OPTIONS, DEV_ENV);

        /*Pre Load the contents of the Payment UI*/
        Checkout.preload(getApplicationContext());
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public NammaApartmentUser getNammaApartmentUser() {
        return nammaApartmentUser;
    }

    public void setNammaApartmentUser(NammaApartmentUser nammaApartmentUser) {
        this.nammaApartmentUser = nammaApartmentUser;
        userUID = nammaApartmentUser.getUID();
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public DatabaseReference getUserDataReference() {
        UserFlatDetails userFlatDetails = nammaApartmentUser.getFlatDetails();
        return PRIVATE_USER_DATA_REFERENCE
                .child(userFlatDetails.getCity())
                .child(userFlatDetails.getSocietyName())
                .child(userFlatDetails.getApartmentName())
                .child(userFlatDetails.getFlatNumber());
    }

}
