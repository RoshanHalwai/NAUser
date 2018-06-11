package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.DS_OTP_STATUS_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CARBIKECLEANERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CHILDDAYCARES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYNEWSPAPERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DRIVERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_LAUNDRIES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MAIDS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MILKMEN;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYCARBIKECLEANER;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYCHILDDAYCARE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYCOOK;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYDAILYNEWSPAPER;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYDRIVER;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYLAUNDRY;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYMAID;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_MYMILKMAN;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;

public class AddDailyServiceAndFamilyMembers extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private CircleImageView circleImageView;
    private TextView textDescriptionDailyService;
    private EditText editPickTime;
    private EditText editDailyServiceOrFamilyMemberName;
    private EditText editDailyServiceOrFamilyMemberMobile;
    private EditText editFamilyMemberRelation;
    private Button buttonAdd;
    private Button buttonYes;
    private Button buttonNo;
    private String service_type;
    private String visitorName;
    private String mobileNumber;
    private String familyMemberRelation;
    private AlertDialog imageSelectingOptions;
    private ListView listView;
    private boolean grantedAccess = false;
    private boolean fieldsFilled;

    String dailyServiceUID = null;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_daily_service_and_family_members;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Add Daily Service and Add Family Members, we set the title
         * based on the user click on MySweetHome screen and on click of listview on MyDailyServices*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_sweet_home) {
            return R.string.add_family_members_details_screen;
        } else {
            return R.string.add_my_service;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        setupCustomDialog();

        /*Getting Id's for all the views*/
        TextView textDailyServiceOrFamilyMemberName = findViewById(R.id.textDailyServiceOrFamilyMemberName);
        TextView textDailyServiceOrFamilyMemberMobile = findViewById(R.id.textDailyServiceOrFamilyMemberMobile);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textRelation = findViewById(R.id.textFamilyMemberRelation);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        textDescriptionDailyService = findViewById(R.id.textDescriptionDailyService);
        TextView textDescriptionFamilyMember = findViewById(R.id.textDescriptionFamilyMember);
        editDailyServiceOrFamilyMemberName = findViewById(R.id.editDailyServiceOrFamilyMemberName);
        editDailyServiceOrFamilyMemberMobile = findViewById(R.id.editDailyServiceOrFamilyMemberMobile);
        editFamilyMemberRelation = findViewById(R.id.editFamilyMemberRelation);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.dailyServiceOrFamilyMemberProfilePic);

        /*Setting font for all the views*/
        textDailyServiceOrFamilyMemberName.setTypeface(Constants.setLatoBoldFont(this));
        textDailyServiceOrFamilyMemberMobile.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        textRelation.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textTime.setTypeface(Constants.setLatoBoldFont(this));
        textGrantAccess.setTypeface(Constants.setLatoBoldFont(this));
        textDescriptionDailyService.setTypeface(Constants.setLatoBoldFont(this));
        textDescriptionFamilyMember.setTypeface(Constants.setLatoBoldFont(this));
        editDailyServiceOrFamilyMemberName.setTypeface(Constants.setLatoRegularFont(this));
        editDailyServiceOrFamilyMemberMobile.setTypeface(Constants.setLatoRegularFont(this));
        editFamilyMemberRelation.setTypeface(Constants.setLatoRegularFont(this));
        editPickTime.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonYes.setTypeface(Constants.setLatoRegularFont(this));
        buttonNo.setTypeface(Constants.setLatoRegularFont(this));
        buttonAdd.setTypeface(Constants.setLatoLightFont(this));

         /*Since we are using same layout for add my daily services and add my family members we need to show different layout
          according to the user choice.*/
        if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_sweet_home) {
            RelativeLayout relativeLayoutAccess = findViewById(R.id.relativeLayoutAccess);
            textRelation.setVisibility(View.VISIBLE);
            editFamilyMemberRelation.setVisibility(View.VISIBLE);
            relativeLayoutAccess.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout relativeLayoutTime = findViewById(R.id.relativeLayoutTime);
            relativeLayoutTime.setVisibility(View.VISIBLE);
            textDescriptionFamilyMember.setVisibility(View.GONE);
            updateOTPDescription();
        }
        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        editPickTime.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        editPickTime.setOnFocusChangeListener(this);

        /*This method gets invoked when user is trying to capture their profile photo either by clicking on camera and gallery.*/
        setupViewsForProfilePhoto();

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
                    Cursor cursor;
                    try {
                        Uri uri = data.getData();
                        if (uri != null) {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                                String phoneNo = cursor.getString(phoneIndex);
                                String name = cursor.getString(nameIndex);
                                String formattedPhoneNumber = phoneNo.replaceAll("\\D+", "");
                                if (formattedPhoneNumber.startsWith("91") && formattedPhoneNumber.length() > 10) {
                                    formattedPhoneNumber = formattedPhoneNumber.substring(2, 12);
                                }
                                editDailyServiceOrFamilyMemberName.setText(name);
                                editDailyServiceOrFamilyMemberMobile.setText(formattedPhoneNumber);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_PERMISSION_REQUEST_CODE:
                    if (data.getExtras() != null) {
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageView.setImageBitmap(bitmapProfilePic);
                        onSuccessfulUpload();
                        imageSelectingOptions.cancel();
                    } else {
                        onFailedUpload();
                        imageSelectingOptions.cancel();
                    }
                    break;

                case GALLERY_PERMISSION_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageView.setImageBitmap(bitmapProfilePic);
                            onSuccessfulUpload();
                            imageSelectingOptions.cancel();
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        onFailedUpload();
                        imageSelectingOptions.cancel();
                    }
                    break;

                case DS_OTP_STATUS_REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        switch (service_type) {
                            case "Cook":
                                storeDailyServiceDetails(Constants.FIREBASE_CHILD_COOKS, FIREBASE_MYCOOK);
                                break;
                            case "Maid":
                                storeDailyServiceDetails(FIREBASE_CHILD_MAIDS, FIREBASE_MYMAID);
                                break;
                            case "Car/Bike Cleaning":
                                storeDailyServiceDetails(FIREBASE_CHILD_CARBIKECLEANERS, FIREBASE_CHILD_MYCARBIKECLEANER);
                                break;
                            case "Child Day Care":
                                storeDailyServiceDetails(FIREBASE_CHILD_CHILDDAYCARES, FIREBASE_MYCHILDDAYCARE);
                                break;
                            case "Daily NewsPaper":
                                storeDailyServiceDetails(FIREBASE_CHILD_DAILYNEWSPAPERS, FIREBASE_MYDAILYNEWSPAPER);
                                break;
                            case "Milk Man":
                                storeDailyServiceDetails(FIREBASE_CHILD_MILKMEN, FIREBASE_MYMILKMAN);
                                break;
                            case "Laundry":
                                storeDailyServiceDetails(FIREBASE_CHILD_LAUNDRIES, FIREBASE_MYLAUNDRY);
                                break;
                            case "Driver":
                                storeDailyServiceDetails(FIREBASE_CHILD_DRIVERS, FIREBASE_MYDRIVER);
                                break;
                        }
                        Intent DailyServiceHomeIntent = new Intent(this, DailyServicesHome.class);
                        DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(DailyServiceHomeIntent);
                    }
                    break;

                case Constants.AFM_OTP_STATUS_REQUEST_CODE:
                    storeFamilyMembersDetails();
                    startActivity(new Intent(this, MySweetHome.class));

            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dailyServiceOrFamilyMemberProfilePic:
                imageSelectingOptions.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.buttonYes:
                grantedAccess = true;
                buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonYes.setTextColor(Color.WHITE);
                buttonNo.setTextColor(Color.BLACK);
                break;
            case R.id.buttonNo:
                grantedAccess = false;
                buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                buttonYes.setTextColor(Color.BLACK);
                buttonNo.setTextColor(Color.WHITE);
                break;
            case R.id.editPickTime:
                pickTime(this, this);
                break;
            case R.id.buttonAdd:
                if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    Intent intentButtonAdd = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                    service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
                    intentButtonAdd.putExtra(Constants.MOBILE_NUMBER, mobileNumber);
                    intentButtonAdd.putExtra(Constants.SCREEN_TITLE, R.string.add_my_service);
                    intentButtonAdd.putExtra(Constants.SERVICE_TYPE, service_type);
                    startActivityForResult(intentButtonAdd, Constants.DS_OTP_STATUS_REQUEST_CODE);
                } else {
                    if (isAllFieldsFilled(new EditText[]{editDailyServiceOrFamilyMemberName, editDailyServiceOrFamilyMemberMobile, editFamilyMemberRelation})
                            && editDailyServiceOrFamilyMemberMobile.length() == PHONE_NUMBER_MAX_LENGTH) {
                        if (grantedAccess)
                            openNotificationDialog();
                        else {
                            Intent intentButtonAdd = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                            intentButtonAdd.putExtra(Constants.MOBILE_NUMBER, mobileNumber);
                            intentButtonAdd.putExtra(Constants.SCREEN_TITLE, R.string.add_family_members_details_screen);
                            startActivityForResult(intentButtonAdd, Constants.AFM_OTP_STATUS_REQUEST_CODE);
                        }
                    }
                }
                break;
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

    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method gets invoked when user tries to add family member and also giving access.
     */
    private void openNotificationDialog() {
        AlertDialog.Builder alertNotificationDialog = new AlertDialog.Builder(this);
        View notificationDialog = View.inflate(this, R.layout.layout_dialog_grant_access_yes, null);
        alertNotificationDialog.setView(notificationDialog);
        imageSelectingOptions = alertNotificationDialog.create();

        // Setting Custom Dialog Buttons
        alertNotificationDialog.setPositiveButton("Accept", (dialog, which) -> navigatingToOTPScreen());
        alertNotificationDialog.setNegativeButton("Reject", (dialog, which) -> dialog.cancel());

        new Dialog(getApplicationContext());
        alertNotificationDialog.show();
    }

    /**
     * We need to update OTP Message description based on Service for which user is entering the
     * details.
     */
    private void updateOTPDescription() {
        if (getIntent().getExtras() != null) {
            service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
            String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
            textDescriptionDailyService.setText(description);
        }
    }

    /**
     * We need to navigate to OTP Screen based on the user selection of giving access and also on not giving access.
     */
    private void navigatingToOTPScreen() {
        Intent intentNotification = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
        intentNotification.putExtra(Constants.SCREEN_TITLE, R.string.add_family_members_details_screen);
        intentNotification.putExtra(Constants.SERVICE_TYPE, "Family Member");
        startActivity(intentNotification);
    }

    /**
     * Creates a custom dialog with a list view which contains the list of inbuilt apps such as Camera and Gallery. This
     * imageSelectingOptions is displayed when user clicks on profile image which is on top of the screen.
     */
    private void setupCustomDialog() {
        AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
        View listAddImageServices = View.inflate(this, R.layout.list_add_image_services, null);
        addImageDialog.setView(listAddImageServices);
        imageSelectingOptions = addImageDialog.create();
        listView = listAddImageServices.findViewById(R.id.listAddImageService);
    }

    /**
     * This method gets invoked when user is trying to capture their profile photo either by clicking on camera and gallery.
     */
    private void setupViewsForProfilePhoto() {
        /*Creating an array list of selecting images from camera and gallery*/
        ArrayList<String> pickImageList = new ArrayList<>();

        /*Adding pick images services to the list*/
        pickImageList.add(getString(R.string.camera));
        pickImageList.add(getString(R.string.gallery));
        pickImageList.add(getString(R.string.cancel));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDailyServiceAndFamilyMembers.this, android.R.layout.simple_list_item_1, pickImageList);

        /*Attaching adapter to the listView*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*Setting event for listview items*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    launchCamera();
                    break;
                case 1:
                    pickImageFromGallery();
                    break;
                case 2:
                    imageSelectingOptions.cancel();
            }
        });
    }

    /**
     * This method will get invoked when user successfully uploaded image from gallery and camera.
     */
    private void onSuccessfulUpload() {
        circleImageView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will get invoked when user fails in uploading image from gallery and camera.
     */
    private void onFailedUpload() {
        circleImageView.setVisibility(View.INVISIBLE);
        imageSelectingOptions.cancel();
    }

    /**
     * Store daily service details to firebase and map them to the users daily service for future use
     *
     * @param dailyServiceChild     root of DailyService reference
     * @param userDailyServiceChild root of users daily service reference
     */
    private void storeDailyServiceDetails(final String dailyServiceChild, final String userDailyServiceChild) {
        //Check if this daily service already exists
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_DAILYSERVICES)
                .child(Constants.FIREBASE_CHILD_ALL)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userUID = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getUID();
                DatabaseReference dailyServiceMobileNumberReference = FirebaseDatabase.getInstance()
                        .getReference(Constants.FIREBASE_CHILD_DAILYSERVICES)
                        .child(Constants.FIREBASE_CHILD_ALL)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(mobileNumber);
                if (!dataSnapshot.exists()) {
                    dailyServiceUID = dailyServiceMobileNumberReference.push().getKey();
                    dailyServiceMobileNumberReference.child(dailyServiceUID).setValue(true);
                } else {
                    dailyServiceUID = dataSnapshot.g
                }
                //Map daily service to true
                dailyServiceMobileNumberReference.child(userDailyServiceChild.substring(2)).setValue(true);

                //Store daily service details in Firebase
                DatabaseReference dailyServicePublicReference = FirebaseDatabase.getInstance()
                        .getReference(Constants.FIREBASE_CHILD_DAILYSERVICES)
                        .child(Constants.FIREBASE_CHILD_ALL)
                        .child(Constants.FIREBASE_CHILD_PUBLIC)
                        .child(dailyServiceChild);
                String fullName = editDailyServiceOrFamilyMemberName.getText().toString();
                String phoneNumber = editDailyServiceOrFamilyMemberMobile.getText().toString();
                String profilePhoto = "";
                String timeOfVisit = editPickTime.getText().toString();
                int rating = Constants.FIREBASE_CHILD_RATING;
                NammaApartmentDailyService nammaApartmentDailyService = new NammaApartmentDailyService(fullName,
                        phoneNumber, profilePhoto, timeOfVisit, false, rating);
                nammaApartmentDailyService.getOwnersUID().put(userUID, true);
                dailyServicePublicReference.child(dailyServiceUID).setValue(nammaApartmentDailyService);

                //Store daily service UID under users data structure for future use
                FirebaseDatabase.getInstance()
                        .getReference(Constants.FIREBASE_CHILD_USERS)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(userUID)
                        .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                        .child(userDailyServiceChild)
                        .child(dailyServiceUID).setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Store family member's details to firebase and map them to the users family members for future use
     */
    private void storeFamilyMembersDetails() {
        //Map family member's mobile number with uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference familyMembersMobileNumberReference = database
                .getReference(Constants.FIREBASE_FAMILYMEMBERS)
                .child(Constants.FIREBASE_CHILD_ALL);
        String familyMemberUID = familyMembersMobileNumberReference.push().getKey();
        familyMembersMobileNumberReference.child(mobileNumber).setValue(familyMemberUID);

        //Store family member's details in Firebase
        DatabaseReference familyMemberReference = database
                .getReference(Constants.FIREBASE_FAMILYMEMBERS)
                .child(Constants.FIREBASE_CHILD_PUBLIC);
        String fullName = editDailyServiceOrFamilyMemberName.getText().toString();
        String phoneNumber = editDailyServiceOrFamilyMemberMobile.getText().toString();
        String relation = familyMemberRelation;
        NammaApartmentFamilyMembers nammaApartmentFamilyMembers = new NammaApartmentFamilyMembers(fullName,
                phoneNumber, relation, grantedAccess);
        familyMemberReference.child(familyMemberUID).setValue(nammaApartmentFamilyMembers);

        //Store family member's UID under users data structure for future use
        DatabaseReference myUsersReference = database
                .getReference(Constants.FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getUID())
                .child(Constants.FIREBASE_CHILD_MYFAMILYMEMBERS);
        myUsersReference.child(familyMemberUID).setValue(true);
    }

    /**
     * We are handling events for editTexts name,mobile number and date and time picker editText.
     */
    private void setEventsForEditText() {
        editDailyServiceOrFamilyMemberName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    textDescriptionDailyService.setVisibility(View.GONE);
                    buttonAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                visitorName = editDailyServiceOrFamilyMemberName.getText().toString().trim();
                String pickTime = editPickTime.getText().toString().trim();
                String phoneNumber = editDailyServiceOrFamilyMemberMobile.getText().toString().trim();
                if (visitorName.length() == EDIT_TEXT_EMPTY_LENGTH || isValidPersonName(visitorName)) {
                    editDailyServiceOrFamilyMemberName.setError(getString(R.string.accept_alphabets));
                }
                if (visitorName.length() > EDIT_TEXT_EMPTY_LENGTH && !isValidPersonName(visitorName)) {
                    editDailyServiceOrFamilyMemberName.setError(null);
                    if (pickTime.length() > EDIT_TEXT_EMPTY_LENGTH && phoneNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                        textDescriptionDailyService.setVisibility(View.VISIBLE);
                        buttonAdd.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        editDailyServiceOrFamilyMemberMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    textDescriptionDailyService.setVisibility(View.GONE);
                    buttonAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileNumber = editDailyServiceOrFamilyMemberMobile.getText().toString().trim();
                String pickTime = editPickTime.getText().toString().trim();
                if (mobileNumber.length() < PHONE_NUMBER_MAX_LENGTH) {
                    editDailyServiceOrFamilyMemberMobile.setError(getString(R.string.number_10digit_validation));
                }
                if (isValidPhone(mobileNumber) && mobileNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    editDailyServiceOrFamilyMemberMobile.setError(null);
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
                visitorName = editDailyServiceOrFamilyMemberName.getText().toString().trim();
                mobileNumber = editDailyServiceOrFamilyMemberMobile.getText().toString().trim();
                fieldsFilled = isAllFieldsFilled(new EditText[]{editDailyServiceOrFamilyMemberName, editDailyServiceOrFamilyMemberMobile, editPickTime});
                if (fieldsFilled && visitorName.length() > EDIT_TEXT_EMPTY_LENGTH && mobileNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    textDescriptionDailyService.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                }
                if (!fieldsFilled) {
                    if (visitorName.length() == EDIT_TEXT_EMPTY_LENGTH) {
                        editDailyServiceOrFamilyMemberName.setError(getString(R.string.name_validation));
                    }
                    if (mobileNumber.length() == EDIT_TEXT_EMPTY_LENGTH) {
                        editDailyServiceOrFamilyMemberMobile.setError(getString(R.string.mobile_number_validation));
                    }
                }
            }
        });

        editFamilyMemberRelation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                familyMemberRelation = editFamilyMemberRelation.getText().toString().trim();
                if (familyMemberRelation.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editFamilyMemberRelation.setError(getString(R.string.enter_relation));
                } else if (isValidPersonName(familyMemberRelation)) {
                    editFamilyMemberRelation.setError(getString(R.string.accept_alphabets));
                }
            }
        });
    }

}