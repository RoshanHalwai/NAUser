package com.kirtanlabs.nammaapartments.navigationdrawer.myprofile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ADMIN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_EMAIL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PROFILE_OLD_CONTENT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class UpdateProfile extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Map<String, String> residentsMap = new TreeMap<>();
    private EditText editText;
    private String oldContent, newContent, residentName, residentUID;
    private int screenTitle, count = 0;

    /* ------------------------------------------------------------- *
     * Overriding base class methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected int getActivityTitle() {
        screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting id's for all the views*/
        editText = findViewById(R.id.editText);
        LinearLayout layoutActionButtons = findViewById(R.id.layoutActionButtons);
        Button buttonDone = findViewById(R.id.buttonDone);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        /*Setting fonts for all the views*/
        editText.setTypeface(setLatoRegularFont(this));
        buttonDone.setTypeface(setLatoLightFont(this));
        buttonCancel.setTypeface(setLatoLightFont(this));

        /*Differentiating UI according to the screen title*/
        if (screenTitle == R.string.select_new_admin) {
            setResidentsList();
            editText.setVisibility(View.GONE);
            layoutActionButtons.setVisibility(View.GONE);
        } else {
            oldContent = getIntent().getStringExtra(PROFILE_OLD_CONTENT);
            if (screenTitle == R.string.enter_your_email) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            editText.setText(oldContent);
        }

        /*Setting on click listeners to the views*/
        buttonDone.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding onClick from View Interface
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDone:
                if (isFieldChanged()) {
                    switch (screenTitle) {
                        case R.string.enter_your_name:
                            if (newContent.isEmpty()) {
                                editText.setError(getString(R.string.name_validation));
                            } else {
                                updateUserNameOrEmail();
                            }
                            break;
                        case R.string.enter_your_email:
                            if (newContent.isEmpty()) {
                                editText.setError(getString(R.string.email_validation));
                            } else {
                                if (isValidEmail(newContent)) {
                                    editText.setError(getString(R.string.invalid_email));
                                } else {
                                    updateUserNameOrEmail();
                                }
                            }
                            break;
                    }
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.buttonCancel:
                super.onBackPressed();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method compares both old content and new content of the profile data of user.
     *
     * @return value which user updates accordingly.
     */
    private boolean isFieldChanged() {
        newContent = editText.getText().toString();
        return !newContent.equals(oldContent);
    }

    /**
     * Updates the user name or email address of the user in firebase and local App {@link NammaApartmentsGlobal}
     */
    private void updateUserNameOrEmail() {
        showProgressDialog(UpdateProfile.this, "Updating Profile", getString(R.string.please_wait_a_moment));
        /*Here we are getting the instance of current user when screen loads for the first time*/
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        DatabaseReference personalDetailsReference = PRIVATE_USERS_REFERENCE.child(currentNammaApartmentUser.getUID())
                .child(FIREBASE_CHILD_PERSONALDETAILS);
        if (screenTitle == R.string.enter_your_name) {
            personalDetailsReference = personalDetailsReference.child(FIREBASE_CHILD_FULLNAME);
        } else {
            personalDetailsReference = personalDetailsReference.child(FIREBASE_CHILD_EMAIL);
        }
        personalDetailsReference.setValue(newContent).addOnSuccessListener(task -> {
            if (screenTitle == R.string.enter_your_name) {
                currentNammaApartmentUser.getPersonalDetails().setFullName(newContent);
            } else {
                currentNammaApartmentUser.getPersonalDetails().setEmail(newContent);
            }
            hideProgressDialog();
            startUserProfile(currentNammaApartmentUser);
        });
    }

    /**
     * Updates the Admin details in firebase and local App {@link NammaApartmentsGlobal}
     */
    private void updateAdmin() {
        /*Runnable Interface which gets invoked once user presses Yes button in Confirmation
         Dialog */
        Runnable updateAdminDetailsInFirebase = () ->
        {
            showProgressDialog(UpdateProfile.this, "Updating Admin", getString(R.string.please_wait_a_moment));
            DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
            /*Current user will loose Admin Privileges so change admin to false*/
            DatabaseReference currentUserPrivateReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                    .child(FIREBASE_CHILD_PRIVILEGES)
                    .child(FIREBASE_ADMIN);
            currentUserPrivateReference.setValue(false);

            /*New User will get Admin Privileges so change admin to true*/
            DatabaseReference newUserPrivateReference = PRIVATE_USERS_REFERENCE.child(residentUID)
                    .child(FIREBASE_CHILD_PRIVILEGES)
                    .child(FIREBASE_ADMIN);
            newUserPrivateReference.setValue(true);

            /*Here,we need to update the admin UID under userdata->flatNumber->admin*/
            DatabaseReference adminUIDReference = userDataReference.child(FIREBASE_ADMIN);
            adminUIDReference.setValue(residentUID).addOnSuccessListener(task -> {
                NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                currentNammaApartmentUser.getPrivileges().setAdmin(false);
                hideProgressDialog();
                startUserProfile(currentNammaApartmentUser);
            });
        };
        String confirmDialogTitle = getResources().getString(R.string.non_admin_change_admin_title);
        String confirmDialogMessage = getResources().getString(R.string.admin_change_key).replaceAll("user", residentName);
        showConfirmDialog(confirmDialogTitle, confirmDialogMessage, updateAdminDetailsInFirebase);
    }

    /**
     * Sets up the List view with all the resident names who have been added by the user {@link NammaApartmentUser#friends} and
     * {@link NammaApartmentUser#familyMembers}
     */
    private void setResidentsList() {
        List<String> residentsList = new ArrayList<>();
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        Map<String, Boolean> userFamilyMembers = nammaApartmentUser.getFamilyMembers();
        Map<String, Boolean> userFriends = nammaApartmentUser.getFriends();
        getAllResidentsList(userFamilyMembers, () -> getAllResidentsList(userFriends, () -> {
            residentsList.addAll(residentsMap.values());
            ListView listViewResidents = findViewById(R.id.listViewResidents);
            listViewResidents.addFooterView(new View(UpdateProfile.this), null, true);
            ArrayAdapter<String> adapterResidents = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, residentsList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTypeface(Constants.setLatoRegularFont(UpdateProfile.this));
                    return view;
                }
            };
            listViewResidents.setAdapter(adapterResidents);

            /*Attaching listeners to ListView*/
            listViewResidents.setOnItemClickListener((parent, view, position, id) -> {
                residentName = (String) listViewResidents.getItemAtPosition(position);
                for (String residentUID : residentsMap.keySet()) {
                    if (residentsMap.get(residentUID).equals(residentName)) {
                        this.residentUID = residentUID;
                        updateAdmin();
                        break;
                    }
                }
            });
        }));
    }

    /**
     * Gets all resident names and their UID who have been added by the user {@link NammaApartmentUser#friends} and
     * {@link NammaApartmentUser#familyMembers}
     */
    private void getAllResidentsList(final Map<String, Boolean> residentsList, final FirebaseCallback callback) {
        if (!residentsList.isEmpty()) {
            for (String residentUID : residentsList.keySet()) {
                DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(residentUID)
                        .child(FIREBASE_CHILD_PERSONALDETAILS)
                        .child(FIREBASE_CHILD_FULLNAME);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count++;
                        String residentName = dataSnapshot.getValue(String.class);
                        residentsMap.put(residentUID, residentName);
                        if (count == residentsList.size()) {
                            callback.onCallback();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
            callback.onCallback();
        }
    }

    /**
     * This method updates locally and immediately shows the updated values in My Profile when user
     * navigates from Update Profile to My Profile Screen.
     *
     * @param updatedUserInstance contains the updated instance of user's profile to set locally.
     */
    private void startUserProfile(NammaApartmentUser updatedUserInstance) {
        ((NammaApartmentsGlobal) getApplicationContext()).setNammaApartmentUser(updatedUserInstance);
        Intent userProfileIntent = new Intent(UpdateProfile.this, UserProfile.class);
        userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(userProfileIntent);
    }

    /* ------------------------------------------------------------- *
     * Interface
     * ------------------------------------------------------------- */

    interface FirebaseCallback {
        void onCallback();
    }

}
