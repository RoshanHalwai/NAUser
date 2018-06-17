package com.kirtanlabs.nammaapartments.onboarding.flatdetails;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_FLATS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/2/2018
 */

public class MyFlatDetails extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    Dialog dialog;
    private List<String> itemsInList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private EditText editCity;
    private EditText editSociety;
    private EditText editApartment;
    private EditText editFlat;
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
                searchItemInList(R.id.editCity);
                break;
            case R.id.editSociety:
                searchItemInList(R.id.editSociety);
                break;
            case R.id.editApartment:
                searchItemInList(R.id.editApartment);
                break;
            case R.id.editFlat:
                searchItemInList(R.id.editFlat);
                break;
            case R.id.radioButtonOwner:
            case R.id.radioButtonTenant:
                showViews(R.id.radioResidentType);
                break;
            case R.id.buttonContinue:
                writeDataToFirebase();
        }
    }

    /**
     * Store all user data to firebase
     */
    private void writeDataToFirebase() {
        String flatNumber = editFlat.getText().toString();
        checkIsAdmin(flatNumber);
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

        EditText inputSearch = dialog.findViewById(R.id.inputSearch);
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

    private void checkIsAdmin(String flatNumber) {
        //Flats->flatNumber
        DatabaseReference flatsReference = PRIVATE_FLATS_REFERENCE.child(flatNumber);
        flatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AlertDialog.Builder alertNotificationDialog = new AlertDialog.Builder(MyFlatDetails.this);
                    View notificationDialog = View.inflate(MyFlatDetails.this, R.layout.layout_dialog_grant_access_yes, null);
                    alertNotificationDialog.setView(notificationDialog);
                    //TODO: Change the text
                    ((TextView) notificationDialog.findViewById(R.id.textAlertMessage)).setText("We have reached here");
                    alertNotificationDialog.show();
                } else {
                    /*Create an instance of NammaApartmentUser class*/
                    String apartmentName = editApartment.getText().toString();
                    String emailId = getIntent().getStringExtra(Constants.EMAIL_ID);
                    String fullName = getIntent().getStringExtra(Constants.FULL_NAME);
                    String mobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
                    String societyName = editSociety.getText().toString();
                    String tenantType = radioButtonTenant.isChecked()
                            ? radioButtonTenant.getText().toString()
                            : radioButtonOwner.getText().toString();
                    String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    NammaApartmentUser nammaApartmentUser = new NammaApartmentUser(apartmentName, emailId,
                            flatNumber, fullName, mobileNumber, societyName, tenantType, userUID,
                            false, true, true, currentNammaApartmentUser.getRelation()
                    );

                    /*Mapping Mobile Number to UID in firebase under users->all*/
                    ALL_USERS_REFERENCE.child(getIntent().getStringExtra(Constants.MOBILE_NUMBER))
                            .setValue(userUID);

                    /*Storing user details in firebase under users->private->uid*/
                    PRIVATE_USERS_REFERENCE.child(userUID).setValue(nammaApartmentUser);

                    /*Adding user UID under Flats->FlatNumber*/
                    flatsReference.child(userUID).setValue(true);

                    startActivity(new Intent(MyFlatDetails.this, NammaApartmentsHome.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
