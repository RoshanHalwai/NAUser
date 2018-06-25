package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.ContactPicker;
import com.kirtanlabs.nammaapartments.ImagePicker;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.DS_OTP_STATUS_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CARBIKECLEANERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CHILDDAYCARES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_COOKS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYNEWSPAPERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DRIVERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_LAUNDRIES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MAIDS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MILKMEN;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.Constants.NOT_ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.ImagePicker.bitmapToByteArray;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/17/2018
 */
public class AddDailyService extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    private AlertDialog imageSelectionDialog;

    private CircleImageView circleImageView;
    private TextView textDescriptionDailyService;
    private TextView textErrorProfilePic;
    private EditText editPickTime;
    private EditText editDailyServiceName;
    private EditText editDailyServiceMobile;
    private Button buttonAdd;
    private String service_type;
    private String mobileNumber;
    private boolean fieldsFilled;
    private byte[] profilePhotoByteArray;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_daily_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_my_daily_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        TextView textDailyServiceName = findViewById(R.id.textDailyServiceName);
        TextView textDailyServiceMobile = findViewById(R.id.textDailyServiceMobile);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        textDescriptionDailyService = findViewById(R.id.textDescriptionDailyService);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        editDailyServiceName = findViewById(R.id.editDailyServiceName);
        editDailyServiceMobile = findViewById(R.id.editDailyServiceMobile);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.dailyServiceProfilePic);

        /*We don't want the keyboard to be displayed when user clicks on the pick time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textDailyServiceName.setTypeface(setLatoBoldFont(this));
        textDailyServiceMobile.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        textOr.setTypeface(setLatoBoldFont(this));
        textTime.setTypeface(setLatoBoldFont(this));
        textDescriptionDailyService.setTypeface(setLatoBoldFont(this));
        textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        editDailyServiceName.setTypeface(setLatoRegularFont(this));
        editDailyServiceMobile.setTypeface(setLatoRegularFont(this));
        editPickTime.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonAdd.setTypeface(setLatoLightFont(this));

        updateOTPDescription();

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        editPickTime.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        editPickTime.setOnFocusChangeListener(this);

        /*This method gets invoked when user is trying to modify the values on EditTexts.*/
        setEventsForEditText();
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                    ContactPicker contactPicker = new ContactPicker(AddDailyService.this, data.getData());
                    editDailyServiceName.setText(contactPicker.retrieveContactName());
                    editDailyServiceMobile.setText(contactPicker.retrieveContactNumber());
                    break;

                case CAMERA_PERMISSION_REQUEST_CODE:
                case GALLERY_PERMISSION_REQUEST_CODE:
                    Bitmap bitmapProfilePic = ImagePicker.getImageFromResult(this, resultCode, data);
                    circleImageView.setImageBitmap(bitmapProfilePic);
                    profilePhotoByteArray = bitmapToByteArray(bitmapProfilePic);
                    if (profilePhotoByteArray != null) {
                        textErrorProfilePic.setVisibility(View.INVISIBLE);
                    }
                    break;

                case DS_OTP_STATUS_REQUEST_CODE:
                    switch (service_type) {
                        case "Cook":
                            storeDailyServiceDetails(FIREBASE_CHILD_COOKS);
                            break;
                        case "Maid":
                            storeDailyServiceDetails(FIREBASE_CHILD_MAIDS);
                            break;
                        case "Car/Bike Cleaning":
                            storeDailyServiceDetails(FIREBASE_CHILD_CARBIKECLEANERS);
                            break;
                        case "Child Day Care":
                            storeDailyServiceDetails(FIREBASE_CHILD_CHILDDAYCARES);
                            break;
                        case "Daily NewsPaper":
                            storeDailyServiceDetails(FIREBASE_CHILD_DAILYNEWSPAPERS);
                            break;
                        case "Milk Man":
                            storeDailyServiceDetails(FIREBASE_CHILD_MILKMEN);
                            break;
                        case "Laundry":
                            storeDailyServiceDetails(FIREBASE_CHILD_LAUNDRIES);
                            break;
                        case "Driver":
                            storeDailyServiceDetails(FIREBASE_CHILD_DRIVERS);
                            break;
                    }
                    break;
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dailyServiceProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.editPickTime:
                pickTime(this, this);
                break;
            case R.id.buttonAdd:
                if (profilePhotoByteArray == null) {
                    textErrorProfilePic.setVisibility(View.VISIBLE);
                    textErrorProfilePic.requestFocus();
                } else {
                    Intent intentButtonAdd = new Intent(AddDailyService.this, OTP.class);
                    service_type = getIntent().getStringExtra(SERVICE_TYPE);
                    intentButtonAdd.putExtra(MOBILE_NUMBER, mobileNumber);
                    intentButtonAdd.putExtra(SCREEN_TITLE, R.string.add_my_daily_service);
                    intentButtonAdd.putExtra(SERVICE_TYPE, service_type);
                    startActivityForResult(intentButtonAdd, DS_OTP_STATUS_REQUEST_CODE);
                }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickTime(this, this);
        }
    }

    /* ------------------------------------------------------------- *
     * OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editPickTime.setText(selectedTime);
        }
    }

    /*----------------------------------------------------
     *  Private Methods
     *----------------------------------------------------*/

    /**
     * Creates a custom dialog with a list view which contains the list of inbuilt apps such as Camera and Gallery. This
     * imageSelectionDialog is displayed when user clicks on profile image which is on top of the screen.
     */
    private void createImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectionOptions = {
                getResources().getString(R.string.gallery),
                getResources().getString(R.string.camera),
                getResources().getString(R.string.cancel)
        };
        builder.setItems(selectionOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    pickImageFromGallery();
                    break;
                case 1:
                    launchCamera();
                    break;
                case 2:
                    imageSelectionDialog.cancel();
            }
        });
        imageSelectionDialog = builder.create();
    }

    /**
     * We need to update OTP Message description based on Service for which user is entering the
     * details.
     */
    private void updateOTPDescription() {
        if (getIntent().getExtras() != null) {
            service_type = getIntent().getStringExtra(SERVICE_TYPE);
            String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
            textDescriptionDailyService.setText(description);
        }
    }

    /**
     * We are handling events for editTexts name,mobile number and date and time picker editText.
     */
    private void setEventsForEditText() {
        editDailyServiceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    textDescriptionDailyService.setVisibility(View.GONE);
                    buttonAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String dailyServiceName = editDailyServiceName.getText().toString().trim();
                String pickTime = editPickTime.getText().toString().trim();
                String phoneNumber = editDailyServiceMobile.getText().toString().trim();
                if (dailyServiceName.length() == EDIT_TEXT_EMPTY_LENGTH || isValidPersonName(dailyServiceName)) {
                    editDailyServiceName.setError(getString(R.string.accept_alphabets));
                }
                if (dailyServiceName.length() > EDIT_TEXT_EMPTY_LENGTH && !isValidPersonName(dailyServiceName)) {
                    editDailyServiceName.setError(null);
                    if (pickTime.length() > EDIT_TEXT_EMPTY_LENGTH && phoneNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                        textDescriptionDailyService.setVisibility(View.VISIBLE);
                        buttonAdd.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        editDailyServiceMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    textDescriptionDailyService.setVisibility(View.GONE);
                    buttonAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileNumber = editDailyServiceMobile.getText().toString().trim();
                String pickTime = editPickTime.getText().toString().trim();
                if (mobileNumber.length() < PHONE_NUMBER_MAX_LENGTH) {
                    editDailyServiceMobile.setError(getString(R.string.number_10digit_validation));
                }
                if (isValidPhone(mobileNumber) && mobileNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    editDailyServiceMobile.setError(null);
                    if (pickTime.length() > EDIT_TEXT_EMPTY_LENGTH) {
                        textDescriptionDailyService.setVisibility(View.VISIBLE);
                        buttonAdd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        editPickTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dailyServiceName = editDailyServiceName.getText().toString().trim();
                mobileNumber = editDailyServiceMobile.getText().toString().trim();
                fieldsFilled = isAllFieldsFilled(new EditText[]{editDailyServiceName, editDailyServiceMobile, editPickTime});
                if (fieldsFilled && dailyServiceName.length() > EDIT_TEXT_EMPTY_LENGTH && mobileNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    textDescriptionDailyService.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                }
                if (!fieldsFilled) {
                    if (dailyServiceName.length() == EDIT_TEXT_EMPTY_LENGTH) {
                        editDailyServiceName.setError(getString(R.string.name_validation));
                    }
                    if (mobileNumber.length() == EDIT_TEXT_EMPTY_LENGTH) {
                        editDailyServiceMobile.setError(getString(R.string.mobile_number_validation));
                    }
                }
            }
        });
    }

    /**
     * Store daily service details to firebase and map them to the users daily service for future use
     *
     * @param dailyServiceChild root of DailyService reference
     */
    private void storeDailyServiceDetails(String dailyServiceChild) {
        //displaying progress dialog while image is uploading
        showProgressDialog(this,
                getResources().getString(R.string.adding_your_daily_service),
                getResources().getString(R.string.please_wait_a_moment));
        //Map daily service to mobile number
        DatabaseReference dailyServiceMobileNumberReference = PRIVATE_DAILYSERVICES_REFERENCE.child(mobileNumber);
        dailyServiceMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dailyServiceUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                //If daily service mobile number already exists we don't map it with mobile number
                if (!dataSnapshot.exists()) {
                    dailyServiceMobileNumberReference.setValue(dailyServiceUID);
                }

                //Store UID with daily service type for access to Guard App
                DatabaseReference dailyServiceTypeReference = PUBLIC_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_DAILYSERVICE_TYPE);
                dailyServiceTypeReference.child(dailyServiceUID).setValue(dailyServiceChild);

                //Store daily service details in Firebase
                DatabaseReference dailyServicePublicReference = PUBLIC_DAILYSERVICES_REFERENCE.child(dailyServiceChild);
                String fullName = editDailyServiceName.getText().toString();
                String phoneNumber = editDailyServiceMobile.getText().toString();
                String timeOfVisit = editPickTime.getText().toString();
                int rating = Constants.FIREBASE_CHILD_RATING;
                String userUID = NammaApartmentsGlobal.userUID;

                //Store daily service UID under userData -> dailyServices for future use
                DatabaseReference dailyServiceReference = ((NammaApartmentsGlobal) getApplicationContext())
                        .getUserDataReference()
                        .child(FIREBASE_CHILD_DAILYSERVICES)
                        .child(dailyServiceChild);
                dailyServiceReference.child(dailyServiceUID).setValue(true);

                //getting the storage reference
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(Constants.FIREBASE_CHILD_DAILYSERVICES)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(dailyServiceChild)
                        .child(dailyServiceUID);

                UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(profilePhotoByteArray));

                //adding the profile photo to storage reference and daily service data to real time database
                Map<String, Boolean> ownersUID = new HashMap<>();
                ownersUID.put(userUID, Boolean.TRUE);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    NammaApartmentDailyService nammaApartmentDailyService = new NammaApartmentDailyService(
                            ownersUID,
                            NOT_ENTERED,
                            dailyServiceUID, fullName,
                            phoneNumber, Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString(), timeOfVisit, rating);

                    //adding visitor data under PREAPPROVED_VISITORS_REFERENCE->Visitor UID
                    dailyServicePublicReference.child(dailyServiceUID).setValue(nammaApartmentDailyService);

                    //dismissing the progress dialog
                    hideProgressDialog();

                    /*Once we are done with storing data we need to call the Daily Services
                     * Home screen again to show users that their Daily Service has been added successfully*/
                    Intent DailyServiceHomeIntent = new Intent(AddDailyService.this, DailyServicesHome.class);
                    DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    showSuccessDialog(getResources().getString(R.string.daily_service_success_title),
                            getResources().getString(R.string.daily_service_success_message),
                            DailyServiceHomeIntent);
                }).addOnFailureListener(exception -> {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
