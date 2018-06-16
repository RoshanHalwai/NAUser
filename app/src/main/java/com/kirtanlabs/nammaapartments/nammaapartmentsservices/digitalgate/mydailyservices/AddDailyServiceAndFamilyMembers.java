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
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.AFM_OTP_STATUS_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.DS_OTP_STATUS_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CARBIKECLEANERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CHILDDAYCARES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_COOKS;
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
import static com.kirtanlabs.nammaapartments.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_FLATS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class AddDailyServiceAndFamilyMembers extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private CircleImageView circleImageView;
    private TextView textDescriptionDailyService;
    private EditText editPickTime;
    private EditText editDailyServiceOrFamilyMemberName;
    private EditText editDailyServiceOrFamilyMemberMobile;
    private EditText editDailyServiceOrFamilyMemberEmail;
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
    private boolean grantedAccess;
    private boolean fieldsFilled;

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
        if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_sweet_home) {
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
        TextView textDailyServiceOrFamilyMemberEmail = findViewById(R.id.textDailyServiceOrFamilyMemberEmail);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textRelation = findViewById(R.id.textFamilyMemberRelation);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        textDescriptionDailyService = findViewById(R.id.textDescriptionDailyService);
        TextView textDescriptionFamilyMember = findViewById(R.id.textDescriptionFamilyMember);
        editDailyServiceOrFamilyMemberName = findViewById(R.id.editDailyServiceOrFamilyMemberName);
        editDailyServiceOrFamilyMemberMobile = findViewById(R.id.editDailyServiceOrFamilyMemberMobile);
        editDailyServiceOrFamilyMemberEmail = findViewById(R.id.editDailyServiceOrFamilyMemberEmail);
        editFamilyMemberRelation = findViewById(R.id.editFamilyMemberRelation);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.visitorOrDailyServiceProfilePic);

        /*We don't want the keyboard to be displayed when user clicks on the pick time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textDailyServiceOrFamilyMemberName.setTypeface(setLatoBoldFont(this));
        textDailyServiceOrFamilyMemberMobile.setTypeface(setLatoBoldFont(this));
        textDailyServiceOrFamilyMemberEmail.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        textRelation.setTypeface(setLatoBoldFont(this));
        textOr.setTypeface(setLatoBoldFont(this));
        textTime.setTypeface(setLatoBoldFont(this));
        textGrantAccess.setTypeface(setLatoBoldFont(this));
        textDescriptionDailyService.setTypeface(setLatoBoldFont(this));
        textDescriptionFamilyMember.setTypeface(setLatoBoldFont(this));
        editDailyServiceOrFamilyMemberName.setTypeface(setLatoRegularFont(this));
        editDailyServiceOrFamilyMemberEmail.setTypeface(setLatoRegularFont(this));
        editDailyServiceOrFamilyMemberMobile.setTypeface(setLatoRegularFont(this));
        editFamilyMemberRelation.setTypeface(setLatoRegularFont(this));
        editPickTime.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonYes.setTypeface(setLatoRegularFont(this));
        buttonNo.setTypeface(setLatoRegularFont(this));
        buttonAdd.setTypeface(setLatoLightFont(this));

         /*Since we are using same layout for add my daily services and add my family members we need to show different layout
          according to the user choice.*/
        if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_sweet_home) {
            RelativeLayout relativeLayoutAccess = findViewById(R.id.relativeLayoutAccess);
            textRelation.setVisibility(View.VISIBLE);
            editFamilyMemberRelation.setVisibility(View.VISIBLE);
            textDailyServiceOrFamilyMemberEmail.setVisibility(View.VISIBLE);
            editDailyServiceOrFamilyMemberEmail.setVisibility(View.VISIBLE);
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
                                //int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.);
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
                                storeDailyServiceDetails(FIREBASE_CHILD_COOKS, FIREBASE_MYCOOK);
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
                    }
                    break;

                case AFM_OTP_STATUS_REQUEST_CODE:
                    storeFamilyMembersDetails();

            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.visitorOrDailyServiceProfilePic:
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
                if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    Intent intentButtonAdd = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                    service_type = getIntent().getStringExtra(SERVICE_TYPE);
                    intentButtonAdd.putExtra(MOBILE_NUMBER, mobileNumber);
                    intentButtonAdd.putExtra(SCREEN_TITLE, R.string.add_my_service);
                    intentButtonAdd.putExtra(SERVICE_TYPE, service_type);
                    startActivityForResult(intentButtonAdd, DS_OTP_STATUS_REQUEST_CODE);
                } else {
                    if (isAllFieldsFilled(new EditText[]{editDailyServiceOrFamilyMemberName, editDailyServiceOrFamilyMemberMobile, editDailyServiceOrFamilyMemberEmail, editFamilyMemberRelation})
                            && editDailyServiceOrFamilyMemberMobile.length() == PHONE_NUMBER_MAX_LENGTH) {
                        if (grantedAccess)
                            openNotificationDialog();
                        else {
                            navigatingToOTPScreen();
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
            service_type = getIntent().getStringExtra(SERVICE_TYPE);
            String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
            textDescriptionDailyService.setText(description);
        }
    }

    /**
     * We need to navigate to OTP Screen based on the user selection of giving access and also on not giving access.
     */
    private void navigatingToOTPScreen() {
        Intent otpIntent = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
        otpIntent.putExtra(MOBILE_NUMBER, mobileNumber);
        otpIntent.putExtra(SCREEN_TITLE, R.string.add_family_members_details_screen);
        otpIntent.putExtra(SERVICE_TYPE, "Family Member");
        startActivityForResult(otpIntent, AFM_OTP_STATUS_REQUEST_CODE);
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
            }
            imageSelectingOptions.cancel();
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
    private void storeDailyServiceDetails(String dailyServiceChild, String userDailyServiceChild) {
        //Map daily service to mobile number
        DatabaseReference dailyServiceMobileNumberReference = PRIVATE_DAILYSERVICES_REFERENCE.child(mobileNumber);
        dailyServiceMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dailyServiceUID;
                //If daily service mobile number already exists we don't create a new UID for them
                if (!dataSnapshot.exists()) {
                    dailyServiceUID = dailyServiceMobileNumberReference.push().getKey();
                    dailyServiceMobileNumberReference.setValue(dailyServiceUID);
                } else {
                    dailyServiceUID = dataSnapshot.getValue().toString();
                }

                //Store daily service details in Firebase
                DatabaseReference dailyServicePublicReference = PUBLIC_DAILYSERVICES_REFERENCE.child(dailyServiceChild);
                String fullName = editDailyServiceOrFamilyMemberName.getText().toString();
                String phoneNumber = editDailyServiceOrFamilyMemberMobile.getText().toString();
                String profilePhoto = "";
                String timeOfVisit = editPickTime.getText().toString();
                int rating = Constants.FIREBASE_CHILD_RATING;
                String userUID = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getUID();
                NammaApartmentDailyService nammaApartmentDailyService = new NammaApartmentDailyService(dailyServiceUID, fullName,
                        phoneNumber, profilePhoto, timeOfVisit, false, rating);
                nammaApartmentDailyService.getOwnersUID().put(userUID, true);
                dailyServicePublicReference.child(dailyServiceUID).setValue(nammaApartmentDailyService);

                //Store daily service UID under users data structure for future use
                DatabaseReference dailyServiceReference = PRIVATE_USERS_REFERENCE.child(userUID)
                        .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                        .child(userDailyServiceChild);
                dailyServiceReference.child(dailyServiceUID).setValue(true);

                /*Once we are done with storing data we need o call the Daily Services Home screen again
                    to show users that their Daily Service has been added successfully*/
                Intent DailyServiceHomeIntent = new Intent(AddDailyServiceAndFamilyMembers.this, DailyServicesHome.class);
                DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DailyServiceHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(DailyServiceHomeIntent);
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
        //Map family member's mobile number with uid in users->all
        DatabaseReference familyMemberMobileNumberReference = ALL_USERS_REFERENCE.child(mobileNumber);
        familyMemberMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String familyMemberUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //If family members mobile number already exists we don't create a new UID for them
                if (!dataSnapshot.exists()) {
                    familyMemberMobileNumberReference.setValue(familyMemberUID);
                }

                //Store family member's UID under users data structure for future use
                String fullName = editDailyServiceOrFamilyMemberName.getText().toString();
                String phoneNumber = editDailyServiceOrFamilyMemberMobile.getText().toString();
                String email = editDailyServiceOrFamilyMemberEmail.getText().toString();
                NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();

                NammaApartmentUser familyMember = new NammaApartmentUser(
                        currentNammaApartmentUser.getApartmentName(),
                        email,
                        currentNammaApartmentUser.getFlatNumber(),
                        fullName,
                        phoneNumber,
                        currentNammaApartmentUser.getSocietyName(),
                        currentNammaApartmentUser.getTenantType(),
                        familyMemberUID,
                        false,
                        grantedAccess,
                        false
                );

                /*Storing new family member details in firebase under users->private->family member uid*/
                PRIVATE_USERS_REFERENCE.child(familyMemberUID).setValue(familyMember);

                /*Storing user UID under Family Member's UID*/
                PRIVATE_FLATS_REFERENCE.child(currentNammaApartmentUser.getFlatNumber()).child(familyMemberUID).setValue(true);

                /*Once we are done with storing data we need o call the MySweetHome screen again
                to show users that their family member has been added successfully*/
                Intent MySweetHomeIntent = new Intent(AddDailyServiceAndFamilyMembers.this, MySweetHome.class);
                MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(MySweetHomeIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
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
                if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
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