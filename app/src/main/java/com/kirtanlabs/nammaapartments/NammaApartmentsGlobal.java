package com.kirtanlabs.nammaapartments;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USER_DATA;

/**
 * Global NammaApartment class for storing User's Information
 */
public class NammaApartmentsGlobal extends Application {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static String userUID;
    private NammaApartmentUser nammaApartmentUser;


    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public NammaApartmentUser getNammaApartmentUser() {
        return nammaApartmentUser;
    }

    public DatabaseReference getUserDataReference() {
        UserFlatDetails userFlatDetails = nammaApartmentUser.getFlatDetails();
        return FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USER_DATA).child(FIREBASE_CHILD_PRIVATE)
                .child(userFlatDetails.getCity())
                .child(userFlatDetails.getSocietyName())
                .child(userFlatDetails.getApartmentName())
                .child(userFlatDetails.getFlatNumber());
    }
    
    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setNammaApartmentUser(NammaApartmentUser nammaApartmentUser) {
        this.nammaApartmentUser = nammaApartmentUser;
        userUID = nammaApartmentUser.getUID();
    }

}
