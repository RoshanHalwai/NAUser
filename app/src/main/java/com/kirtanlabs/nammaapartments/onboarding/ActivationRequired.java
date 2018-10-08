package com.kirtanlabs.nammaapartments.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_AUTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VERIFIED_APPROVED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VERIFIED_PENDING;
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

        showProgressDialog(this, "Checking Activation Status", getString(R.string.please_wait_a_moment));
        hideBackButton();

        TextView textActivationRequiredMessage = findViewById(R.id.textActivationRequiredMessage);
        ImageView imageActivation = findViewById(R.id.imageActivation);
        textActivationRequiredMessage.setTypeface(setLatoItalicFont(this));

        /*Keep Track of Verified Key in Firebase; As soon as Admin changes the value to '1'
            we change the UI in User App*/
        String userUID = Objects.requireNonNull(FIREBASE_AUTH.getCurrentUser()).getUid();
        DatabaseReference database = PRIVATE_USERS_REFERENCE.child(userUID).child(FIREBASE_CHILD_PRIVILEGES)
                .child(VERIFIED);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer verifiedValue = dataSnapshot.getValue(Integer.class);
                    if (verifiedValue != null) {
                        hideProgressDialog();
                        switch (verifiedValue) {
                            case FIREBASE_CHILD_VERIFIED_APPROVED:
                                getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE).edit().putBoolean(VERIFIED, true).apply();
                                startActivity(new Intent(ActivationRequired.this, NammaApartmentsHome.class));
                                finish();
                                break;
                            case FIREBASE_CHILD_VERIFIED_PENDING:
                                imageActivation.setImageResource(R.drawable.welcome_na);
                                textActivationRequiredMessage.setText(R.string.activation_pending_message);
                                break;
                            default:
                                imageActivation.setImageResource(R.drawable.feature_unavailable_na);
                                textActivationRequiredMessage.setText(R.string.activation_rejection_message);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
