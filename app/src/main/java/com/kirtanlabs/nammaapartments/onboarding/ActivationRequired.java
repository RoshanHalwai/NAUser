package com.kirtanlabs.nammaapartments.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VERIFIED_APPROVED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VERIFIED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;

public class ActivationRequired extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_activiation_required;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.activation_required_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideBackButton();

        TextView textActivationRequiredMessage = findViewById(R.id.textActivationRequiredMessage);
        textActivationRequiredMessage.setTypeface(setLatoItalicFont(this));

        /*Keep Track of Verified Key in Firebase; As soon as Admin changes the value to 'true'
            we change the UI in User App*/
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = PRIVATE_USERS_REFERENCE.child(userUID).child(FIREBASE_CHILD_PRIVILEGES)
                .child(VERIFIED);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int verifiedValue = dataSnapshot.getValue(Integer.class);
                    if (verifiedValue == FIREBASE_CHILD_VERIFIED_APPROVED) {
                        getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE).edit().putBoolean(VERIFIED, true).apply();
                        startActivity(new Intent(ActivationRequired.this, NammaApartmentsHome.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
