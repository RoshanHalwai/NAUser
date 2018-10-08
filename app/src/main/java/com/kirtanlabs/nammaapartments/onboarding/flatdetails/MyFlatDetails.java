package com.kirtanlabs.nammaapartments.onboarding.flatdetails;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.ActivationRequired;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;
import com.kirtanlabs.nammaapartments.onboarding.login.SignUp;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPrivileges;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ACCOUNT_CREATED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.CITIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ADMIN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_AUTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_APARTMENTS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NOTIFICATION_SOUND;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NOTIFICATION_SOUND_CAB;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NOTIFICATION_SOUND_DAILYSERVICE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NOTIFICATION_SOUND_GUEST;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NOTIFICATION_SOUND_PACKAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_OTHER_DETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SOCIETIES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VERIFIED_PENDING;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getByteArrayFromFile;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/2/2018
 */

public class MyFlatDetails extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final List<String> itemsInList = new ArrayList<>();
    DatabaseReference societiesReference, apartmentsReference, flatsReference;
    private Dialog dialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private EditText editCity, editSociety, editApartment, editFlat, inputSearch;
    private RadioButton radioButtonOwner, radioButtonTenant;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_flat_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_flat_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textCity = findViewById(R.id.textCity);
        TextView textSociety = findViewById(R.id.textSociety);
        TextView textApartment = findViewById(R.id.textApartment);
        TextView textFlat = findViewById(R.id.textFlat);
        TextView textResidentType = findViewById(R.id.textResidentType);
        TextView textVerificationMessage = findViewById(R.id.textVerificationMessage);
        editCity = findViewById(R.id.editCity);
        editSociety = findViewById(R.id.editSociety);
        editApartment = findViewById(R.id.editApartment);
        editFlat = findViewById(R.id.editFlat);
        radioButtonOwner = findViewById(R.id.radioButtonOwner);
        radioButtonTenant = findViewById(R.id.radioButtonTenant);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        /*Setting font for all the views*/
        textCity.setTypeface(Constants.setLatoBoldFont(this));
        textSociety.setTypeface(Constants.setLatoBoldFont(this));
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        textResidentType.setTypeface(Constants.setLatoBoldFont(this));
        textVerificationMessage.setTypeface(Constants.setLatoRegularFont(this));
        editCity.setTypeface(Constants.setLatoRegularFont(this));
        editSociety.setTypeface(Constants.setLatoRegularFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonCreateAccount.setTypeface(Constants.setLatoLightFont(this));

        /*Allow users to search for City, Society, Apartment and Flat*/
        initializeListWithSearchView();

        /*Show only City during start of activity*/
        hideViews(R.id.editCity);

        /*We don't want the keyboard to be displayed when user clicks edit views*/
        editCity.setInputType(InputType.TYPE_NULL);
        editSociety.setInputType(InputType.TYPE_NULL);
        editApartment.setInputType(InputType.TYPE_NULL);
        editFlat.setInputType(InputType.TYPE_NULL);

        /*Attaching listeners to Views*/
        editCity.setOnFocusChangeListener(this);
        editSociety.setOnFocusChangeListener(this);
        editApartment.setOnFocusChangeListener(this);
        editFlat.setOnFocusChangeListener(this);

        editCity.setOnClickListener(this);
        editSociety.setOnClickListener(this);
        editApartment.setOnClickListener(this);
        editFlat.setOnClickListener(this);
        radioButtonOwner.setOnClickListener(this);
        radioButtonTenant.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editCity:
                inputSearch.setHint(getString(R.string.city_hint));
                searchItemInList(R.id.editCity);
                break;
            case R.id.editSociety:
                inputSearch.setHint(getString(R.string.society_hint));
                searchItemInList(R.id.editSociety);
                break;
            case R.id.editApartment:
                inputSearch.setHint(getString(R.string.apartment_hint));
                searchItemInList(R.id.editApartment);
                break;
            case R.id.editFlat:
                inputSearch.setHint(getString(R.string.flat_hint));
                searchItemInList(R.id.editFlat);
                break;
            case R.id.radioButtonOwner:
            case R.id.radioButtonTenant:
                showViews(R.id.radioResidentType);
                break;
            case R.id.buttonCreateAccount:
                societiesReference.child(editSociety.getText().toString()).child("databaseURL").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String societyDatabaseURL = dataSnapshot.getValue(String.class);
                        //Map User's Mobile Number with Society Database URL for future use
                        ALL_USERS_REFERENCE.child(getIntent().getStringExtra(MOBILE_NUMBER)).setValue(societyDatabaseURL)
                                .addOnCompleteListener(task -> {
                                    new OTP().changeDatabaseInstance(getApplicationContext(), societyDatabaseURL);
                                    storeUserDetailsInFirebase();
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(v);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void initializeListWithSearchView() {
        dialog = new Dialog(MyFlatDetails.this);
        dialog.setContentView(R.layout.layout_search_flat);

        inputSearch = dialog.findViewById(R.id.inputSearch);
        listView = dialog.findViewById(R.id.list);
        inputSearch.setTypeface(Constants.setLatoItalicFont(MyFlatDetails.this));

        /*Attaching listeners to ListView*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) listView.getItemAtPosition(position);
            int viewId = Objects.requireNonNull(getCurrentFocus()).getId();
            showViews(viewId);
            dialog.cancel();
            ((EditText) findViewById(viewId)).setText(itemValue);
        });

        /*Attaching listeners to Search Field*/
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MyFlatDetails.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    /**
     * Hides unwanted views and updates list
     *
     * @param viewId of edit text views
     */
    private void searchItemInList(int viewId) {
        itemsInList.clear();
        switch (viewId) {
            case R.id.editCity:
                hideViews(R.id.editCity);
                updateItemsInList(CITIES_REFERENCE);
                break;

            case R.id.editSociety:
                hideViews(R.id.editSociety);
                societiesReference = CITIES_REFERENCE.child(editCity.getText().toString())
                        .child(FIREBASE_CHILD_SOCIETIES);
                updateItemsInList(societiesReference);
                break;

            case R.id.editApartment:
                hideViews(R.id.editApartment);
                apartmentsReference = societiesReference.child(editSociety.getText().toString())
                        .child(FIREBASE_CHILD_APARTMENTS);
                updateItemsInList(apartmentsReference);
                break;

            case R.id.editFlat:
                hideViews(R.id.editFlat);
                flatsReference = apartmentsReference.child(editApartment.getText().toString());
                updateItemsInList(flatsReference);
                break;
        }
    }

    /**
     * Updates the list by getting values from firebase and shows it to user
     *
     * @param databaseReference for getting values from firebase
     */
    private void updateItemsInList(DatabaseReference databaseReference) {
        /*Setting font for all the items in the list view*/
        inputSearch.getText().clear();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsInList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(MyFlatDetails.this));
                return view;
            }
        };

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsInList.clear();
                for (DataSnapshot keys : dataSnapshot.getChildren()) {
                    itemsInList.add(keys.getKey());
                }
                adapter.notifyDataSetChanged();
                Collections.sort(itemsInList);
                listView.setAdapter(adapter);
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Hides view which are not required
     *
     * @param viewId from which other views need to be hidden
     */
    private void hideViews(int viewId) {
        switch (viewId) {
            case R.id.editCity:
            case R.id.textSociety:
                findViewById(R.id.textSociety).setVisibility(View.INVISIBLE);
                findViewById(R.id.editSociety).setVisibility(View.INVISIBLE);
            case R.id.editSociety:
                findViewById(R.id.textApartment).setVisibility(View.INVISIBLE);
                findViewById(R.id.editApartment).setVisibility(View.INVISIBLE);
            case R.id.editApartment:
                findViewById(R.id.textFlat).setVisibility(View.INVISIBLE);
                findViewById(R.id.editFlat).setVisibility(View.INVISIBLE);
            case R.id.editFlat:
                findViewById(R.id.textResidentType).setVisibility(View.INVISIBLE);
                findViewById(R.id.radioResidentType).setVisibility(View.INVISIBLE);
                RadioGroup radioGroup = findViewById(R.id.radioResidentType);
                radioGroup.clearCheck();
            case R.id.radioResidentType:
                findViewById(R.id.textVerificationMessage).setVisibility(View.INVISIBLE);
                findViewById(R.id.buttonCreateAccount).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Shows view which are  required
     *
     * @param viewId from which other views need to be shown
     */
    private void showViews(int viewId) {
        switch (viewId) {
            case R.id.editCity:
                findViewById(R.id.textSociety).setVisibility(View.VISIBLE);
                findViewById(R.id.editSociety).setVisibility(View.VISIBLE);
                ((EditText) findViewById(R.id.editSociety)).getText().clear();
                break;
            case R.id.editSociety:
                findViewById(R.id.textApartment).setVisibility(View.VISIBLE);
                findViewById(R.id.editApartment).setVisibility(View.VISIBLE);
                ((EditText) findViewById(R.id.editApartment)).getText().clear();
                break;
            case R.id.editApartment:
                findViewById(R.id.textFlat).setVisibility(View.VISIBLE);
                findViewById(R.id.editFlat).setVisibility(View.VISIBLE);
                ((EditText) findViewById(R.id.editFlat)).getText().clear();
                break;
            case R.id.editFlat:
                findViewById(R.id.textResidentType).setVisibility(View.VISIBLE);
                findViewById(R.id.radioResidentType).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.radioButtonOwner)).setChecked(false);
                ((RadioButton) findViewById(R.id.radioButtonTenant)).setChecked(false);
                break;
            case R.id.radioResidentType:
                findViewById(R.id.textVerificationMessage).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonCreateAccount).setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * This method is invoked when User presses on 'Create Account' button in 'My Flat Details' screen.
     * The details entered by User during Sign Up process is stored in Firebase
     */
    private void storeUserDetailsInFirebase() {
        //Displaying progress dialog while image is uploading
        showProgressDialog(this,
                getResources().getString(R.string.creating_your_account),
                getResources().getString(R.string.please_wait_a_moment));

        /*Get selected Flat Details*/
        String city = editCity.getText().toString();
        String apartmentName = editApartment.getText().toString();
        String flatNumber = editFlat.getText().toString();
        String emailId = getIntent().getStringExtra(Constants.EMAIL_ID);
        String fullName = getIntent().getStringExtra(Constants.FULL_NAME);
        String mobileNumber = getIntent().getStringExtra(MOBILE_NUMBER);
        String societyName = editSociety.getText().toString();
        String tenantType = radioButtonTenant.isChecked()
                ? radioButtonTenant.getText().toString()
                : radioButtonOwner.getText().toString();
        String userUID = Objects.requireNonNull(FIREBASE_AUTH.getCurrentUser()).getUid();

        /*Create User Data reference*/
        DatabaseReference flatsReference = PRIVATE_USER_DATA_REFERENCE.child(city)
                .child(societyName)
                .child(apartmentName)
                .child(flatNumber);
        flatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*If a record of the flat details already exits in firebase it means User has to request
                 * the Admin to add them as Family Member. This is done to ensure a flat does not have
                 * multiple admins*/
                if (dataSnapshot.exists()) {
                    showNotificationDialog(getString(R.string.title_multiple_admin), getResources().getString(R.string.multiple_admin_restricted), null);
                    hideProgressDialog();
                }
                /*Record not found, user is the first family member for the entered flat details.
                 * Setup account for them*/
                else {
                    File profilePhotoPath = new File(getIntent().getStringExtra(Constants.PROFILE_PHOTO));

                    StorageReference storageReference = FIREBASE_STORAGE.getReference(FIREBASE_CHILD_USERS)
                            .child(Constants.FIREBASE_CHILD_PRIVATE)
                            .child(userUID);

                    /*Create file metadata including the content type*/
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("image/png")
                            .build();

                    UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(MyFlatDetails.this, profilePhotoPath), metadata);

                    /*adding the profile photo to storage reference and user data to real time database*/
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        /*Create an instance of NammaApartmentUser class*/
                        UserPersonalDetails userPersonalDetails = new UserPersonalDetails(emailId, fullName, mobileNumber, Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                        UserFlatDetails userFlatDetails = new UserFlatDetails(apartmentName, city, flatNumber, societyName, tenantType);
                        UserPrivileges userPrivileges = new UserPrivileges(true, true, FIREBASE_CHILD_VERIFIED_PENDING);
                        NammaApartmentUser nammaApartmentUser = new NammaApartmentUser(userUID, userPersonalDetails, userFlatDetails, userPrivileges);

                        /*Mapping Mobile Number to UID in firebase under users->all*/
                        ALL_USERS_REFERENCE.child(getIntent().getStringExtra(MOBILE_NUMBER))
                                .setValue(userUID);

                        /*Storing user details in firebase under users->private->uid*/
                        PRIVATE_USERS_REFERENCE.child(userUID).setValue(nammaApartmentUser);

                        /*Storing time stamp so that Society Admin will be able to know when user has created their account*/
                        PRIVATE_USERS_REFERENCE.child(userUID).child(FIREBASE_CHILD_OTHER_DETAILS).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

                        /*Storing Notification Sound Keys as default values in firebase when user creates account.*/
                        DatabaseReference userNotificationSoundReference = PRIVATE_USERS_REFERENCE.child(userUID).child(FIREBASE_CHILD_OTHER_DETAILS).child(FIREBASE_CHILD_NOTIFICATION_SOUND);
                        userNotificationSoundReference.child(FIREBASE_CHILD_NOTIFICATION_SOUND_CAB).setValue(true);
                        userNotificationSoundReference.child(FIREBASE_CHILD_NOTIFICATION_SOUND_DAILYSERVICE).setValue(true);
                        userNotificationSoundReference.child(FIREBASE_CHILD_NOTIFICATION_SOUND_GUEST).setValue(true);
                        userNotificationSoundReference.child(FIREBASE_CHILD_NOTIFICATION_SOUND_PACKAGE).setValue(true);

                        /*Adding user UID under Flats->FlatNumber*/
                        flatsReference.child(FIREBASE_CHILD_FLAT_MEMBERS).child(userUID).setValue(true);

                        /*Mapping Admin with user UID under Flats->FlatNumber*/
                        flatsReference.child(FIREBASE_ADMIN).setValue(userUID);

                        /*Storing token Id so user gets notified when their account gets activated by Society Admin*/
                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        PRIVATE_USERS_REFERENCE.child(userUID).child("tokenId").setValue(token_id);

                        //dismissing the progress dialog
                        hideProgressDialog();

                        getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE).edit().putBoolean(ACCOUNT_CREATED, true).apply();
                        getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE).edit().putString(USER_UID, userUID).apply();

                        startActivity(new Intent(MyFlatDetails.this, ActivationRequired.class));
                        finish();
                        SignUp.getInstance().finish();
                    }).addOnFailureListener(exception -> {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}