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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPrivileges;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_ADMIN;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USER_DATA;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.ImagePicker.fileToByteArray;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/2/2018
 */

public class MyFlatDetails extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final List<String> itemsInList = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private EditText editCity;
    private EditText editSociety;
    private EditText editApartment;
    private EditText editFlat;
    private EditText inputSearch;
    private RadioButton radioButtonOwner;
    private RadioButton radioButtonTenant;

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
        Button buttonContinue = findViewById(R.id.buttonContinue);

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
        buttonContinue.setTypeface(Constants.setLatoLightFont(this));

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
        buttonContinue.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editCity:
                inputSearch.setHint("Search City");
                searchItemInList(R.id.editCity);
                break;
            case R.id.editSociety:
                inputSearch.setHint("Search Society");
                searchItemInList(R.id.editSociety);
                break;
            case R.id.editApartment:
                inputSearch.setHint("Search Apartment");
                searchItemInList(R.id.editApartment);
                break;
            case R.id.editFlat:
                inputSearch.setHint("Search Flat");
                searchItemInList(R.id.editFlat);
                break;
            case R.id.radioButtonOwner:
            case R.id.radioButtonTenant:
                showViews(R.id.radioResidentType);
                break;
            case R.id.buttonContinue:
                storeUserDetailsInFirebase();
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
        dialog.setContentView(R.layout.cities_listview);

        inputSearch = dialog.findViewById(R.id.inputSearch);
        listView = dialog.findViewById(R.id.list);
        inputSearch.setTypeface(Constants.setLatoItalicFont(MyFlatDetails.this));

        /*Setting font for all the items in the list view*/
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
        listView.setAdapter(adapter);

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
                updateItemsInList(Constants.CITIES_REFERENCE);
                break;
            case R.id.editSociety:
                hideViews(R.id.editSociety);
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(editCity.getText().toString())
                        .child(Constants.FIREBASE_CHILD_SOCIETIES));
                break;
            case R.id.editApartment:
                hideViews(R.id.editApartment);
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(editCity.getText().toString())
                        .child(Constants.FIREBASE_CHILD_SOCIETIES)
                        .child(editSociety.getText().toString())
                        .child(Constants.FIREBASE_CHILD_APARTMENTS));
                break;
            case R.id.editFlat:
                hideViews(R.id.editFlat);
                updateItemsInList(Constants.CITIES_REFERENCE
                        .child(editCity.getText().toString())
                        .child(Constants.FIREBASE_CHILD_SOCIETIES)
                        .child(editSociety.getText().toString())
                        .child(Constants.FIREBASE_CHILD_APARTMENTS)
                        .child(editApartment.getText().toString())
                        .child(Constants.FIREBASE_CHILD_FLATS));
                break;
        }
    }

    /**
     * Updates the list by getting values from firebase and shows it to user
     *
     * @param databaseReference for getting values from firebase
     */
    private void updateItemsInList(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    itemsInList.add(values.getKey());
                    adapter.notifyDataSetChanged();
                }
                Collections.sort(itemsInList);
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
                findViewById(R.id.buttonContinue).setVisibility(View.INVISIBLE);
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
                findViewById(R.id.buttonContinue).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void storeUserDetailsInFirebase() {
        //Displaying progress dialog while image is uploading
        showProgressDialog(this,
                getResources().getString(R.string.creating_your_account),
                getResources().getString(R.string.please_wait_a_moment));

        String city = editCity.getText().toString();
        String apartmentName = editApartment.getText().toString();
        String flatNumber = editFlat.getText().toString();
        String emailId = getIntent().getStringExtra(Constants.EMAIL_ID);
        String fullName = getIntent().getStringExtra(Constants.FULL_NAME);
        String mobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
        String societyName = editSociety.getText().toString();
        String tenantType = radioButtonTenant.isChecked()
                ? radioButtonTenant.getText().toString()
                : radioButtonOwner.getText().toString();
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference flatsReference = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_USER_DATA)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(city).child(societyName)
                .child(apartmentName)
                .child(flatNumber);
        flatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DatabaseReference adminUIDReference = flatsReference.child(FIREBASE_ADMIN);
                    adminUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String adminUID = dataSnapshot.getValue().toString();
                            DatabaseReference adminNameReference = PRIVATE_USERS_REFERENCE.child(adminUID);
                            adminNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Getting full name of Admin of the flat from User's Private data
                                    String adminName = dataSnapshot.child(FIREBASE_CHILD_PERSONALDETAILS).child(FIREBASE_CHILD_FULLNAME).getValue().toString();
                                    String multipleAdminText = getResources().getString(R.string.multiple_admin_restricted);
                                    multipleAdminText = multipleAdminText.replace(FIREBASE_ADMIN, adminName);

                                    //This dialog box pops up when a new user who is trying to sign up, already has a registered Admin
                                    //in that particular flat, because of which, the user is being restricted to sign up
                                    showSuccessDialog(getString(R.string.title_multiple_admin), multipleAdminText, null);
                                    hideProgressDialog();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    byte[] byteArray = null;
                    String filename = getIntent().getStringExtra(Constants.PROFILE_PHOTO);
                    try {
                        FileInputStream is = getApplicationContext().openFileInput(filename);
                        byteArray = fileToByteArray(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Getting the storage reference
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_USERS)
                            .child(Constants.FIREBASE_CHILD_PRIVATE)
                            .child(userUID);

                    //Create file metadata including the content type
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("image/png")
                            .build();

                    UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(byteArray), metadata);

                    //adding the profile photo to storage reference and user data to real time database
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        /*Create an instance of NammaApartmentUser class*/
                        UserPersonalDetails userPersonalDetails = new UserPersonalDetails(emailId, fullName, mobileNumber, taskSnapshot.getDownloadUrl().toString());
                        UserFlatDetails userFlatDetails = new UserFlatDetails(apartmentName, city, flatNumber, societyName, tenantType);
                        UserPrivileges userPrivileges = new UserPrivileges(true, true, false);
                        NammaApartmentUser nammaApartmentUser = new NammaApartmentUser(userUID, userPersonalDetails, userFlatDetails, userPrivileges);

                        /*Mapping Mobile Number to UID in firebase under users->all*/
                        ALL_USERS_REFERENCE.child(getIntent().getStringExtra(Constants.MOBILE_NUMBER))
                                .setValue(userUID);

                        /*Storing user details in firebase under users->private->uid*/
                        PRIVATE_USERS_REFERENCE.child(userUID).setValue(nammaApartmentUser);

                        /*Adding user UID under Flats->FlatNumber*/
                        flatsReference.child(FIREBASE_CHILD_FLAT_MEMBERS).child(userUID).setValue(true);

                        /*Mapping Admin with user UID under Flats->FlatNumber*/
                        flatsReference.child(FIREBASE_ADMIN).setValue(userUID);

                        //dismissing the progress dialog
                        hideProgressDialog();

                        startActivity(new Intent(MyFlatDetails.this, NammaApartmentsHome.class));
                        finish();
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