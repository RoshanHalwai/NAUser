package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
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
                //Stores data in Firebase for different modules of 'My Daily Services'
                if (getIntent().getIntExtra(Constants.SCREEN_TITLE, 0) == R.string.my_daily_services) {
                    Intent intentButtonAdd = new Intent(AddDailyServiceAndFamilyMembers.this, OTP.class);
                    intentButtonAdd.putExtra(Constants.SCREEN_TITLE, R.string.add_my_service);
                    service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
                    intentButtonAdd.putExtra(Constants.SERVICE_TYPE, service_type);
                    startActivity(intentButtonAdd);
                    if (service_type.equals("Cook")) {
                        storeCookDetails();
                    }
                    if (service_type.equals("Maid")) {
                        storeMaidDetails();
                    }
                    if (service_type.equals("Car/Bike Cleaning")) {
                        storeCarBikeCleanerDetails();
                    }
                    if (service_type.equals("Child Day Care")) {
                        storeChildDayCareDetails();
                    }
                    if (service_type.equals("Daily NewsPaper")) {
                        storeDailyNewspaperDetails();
                    }
                    if (service_type.equals("Milk Man")) {
                        storeMilkmanDetails();
                    }
                    if (service_type.equals("Laundry")) {
                        storeLaundryDetails();
                    }
                    if (service_type.equals("Driver")) {
                        storeDriverDetails();
                    }
                } else {
                    if (isAllFieldsFilled(new EditText[]{editDailyServiceOrFamilyMemberName, editDailyServiceOrFamilyMemberMobile, editFamilyMemberRelation})
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
     * This method gets invoked when user adds his cook's details 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeCookDetails() {
        //Map cook's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cooksMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_COOK)
                .child(Constants.FIREBASE_CHILD_ALL);
        String cookUID = cooksMobileNumberReference.push().getKey();
        cooksMobileNumberReference.child(mobileNumber).setValue(cookUID);

        //Store cook's details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference myCooksReference = database
                    .getReference(Constants.FIREBASE_CHILD_COOK)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String cookName = editDailyServiceOrFamilyMemberName.getText().toString();
            String cookMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String cookPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentCook = new NammaApartmentDailyServices(cookName,
                    cookMobile, cookPhoto, false, rating);
            myCooksReference.child(cookUID).setValue(nammaApartmentCook);

            //Map cook's UID and Phone Number in users->private->userUID->myDailyServices->cook
            DatabaseReference cookUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_COOK);
            cookUserReference.child(cookUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds his maid's details 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeMaidDetails() {
        //Map Maid's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference maidsMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_MAID)
                .child(Constants.FIREBASE_CHILD_ALL);
        String maidUID = maidsMobileNumberReference.push().getKey();
        maidsMobileNumberReference.child(mobileNumber).setValue(maidUID);

        //Store Maid's details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference myMaidsReference = database
                    .getReference(Constants.FIREBASE_CHILD_MAID)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String maidName = editDailyServiceOrFamilyMemberName.getText().toString();
            String maidMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String maidPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentMaid = new NammaApartmentDailyServices(maidName,
                    maidMobile, maidPhoto, false, rating);
            myMaidsReference.child(maidUID).setValue(nammaApartmentMaid);

            //Map Maid's UID and Phone Number in users->private->userUID->myDailyServices->maid
            DatabaseReference cookUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_MAID);
            cookUserReference.child(maidUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds car/bike cleaner details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeCarBikeCleanerDetails() {
        //Map Car/Bike cleaner's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cleanerMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_CLEANER)
                .child(Constants.FIREBASE_CHILD_ALL);
        String cleanerUID = cleanerMobileNumberReference.push().getKey();
        cleanerMobileNumberReference.child(mobileNumber).setValue(cleanerUID);

        //Store Car/Bike cleaner's details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference myCleanerReference = database
                    .getReference(Constants.FIREBASE_CHILD_CLEANER)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String cleanerName = editDailyServiceOrFamilyMemberName.getText().toString();
            String cleanerMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String cleanerPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentCleaner = new NammaApartmentDailyServices(cleanerName,
                    cleanerMobile, cleanerPhoto, false, rating);
            myCleanerReference.child(cleanerUID).setValue(nammaApartmentCleaner);

            //Map Car/Bike cleaner's UID and Phone Number in users->private->userUID->myDailyServices->carBikeCleaner
            DatabaseReference cleanerUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_CLEANER);
            cleanerUserReference.child(cleanerUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds Child Day Care details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeChildDayCareDetails() {
        //Map Child Day Care mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference childDayCareMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_DAYCARE)
                .child(Constants.FIREBASE_CHILD_ALL);
        String childDayCareUID = childDayCareMobileNumberReference.push().getKey();
        childDayCareMobileNumberReference.child(mobileNumber).setValue(childDayCareUID);

        //Store Child Day Care details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference myChildDayCareReference = database
                    .getReference(Constants.FIREBASE_CHILD_DAYCARE)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String childDayCareName = editDailyServiceOrFamilyMemberName.getText().toString();
            String childDayCareMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String childDayCarePhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentChildDayCare = new NammaApartmentDailyServices(childDayCareName,
                    childDayCareMobile, childDayCarePhoto, false, rating);
            myChildDayCareReference.child(childDayCareUID).setValue(nammaApartmentChildDayCare);

            //Map Child Day Care UID and Phone Number in users->private->userUID->myDailyServices->childDayCare
            DatabaseReference childDayCareUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_DAYCARE);
            childDayCareUserReference.child(childDayCareUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds Daily Newspaper details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeDailyNewspaperDetails() {
        //Map Daily Newspaper's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dailyNewspaperMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_DAILYNEWSPAPER)
                .child(Constants.FIREBASE_CHILD_ALL);
        String dailyNewspaperUID = dailyNewspaperMobileNumberReference.push().getKey();
        dailyNewspaperMobileNumberReference.child(mobileNumber).setValue(dailyNewspaperUID);

        //Store Child Day Care details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference dailyNewspaperReference = database
                    .getReference(Constants.FIREBASE_CHILD_DAILYNEWSPAPER)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String dailyNewspaperName = editDailyServiceOrFamilyMemberName.getText().toString();
            String dailyNewspaperMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String dailyNewspaperPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentDailyNewspaper = new NammaApartmentDailyServices(dailyNewspaperName,
                    dailyNewspaperMobile, dailyNewspaperPhoto, false, rating);
            dailyNewspaperReference.child(dailyNewspaperUID).setValue(nammaApartmentDailyNewspaper);

            //Map Child Day Care UID and Phone Number in users->private->userUID->myDailyServices->dailyNewspaper
            DatabaseReference dailyNewspaperUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_DAILYNEWSPAPER);
            dailyNewspaperUserReference.child(dailyNewspaperUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds Milkman details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeMilkmanDetails() {
        //Map Daily Milkman's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference milkmanMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_MILKMAN)
                .child(Constants.FIREBASE_CHILD_ALL);
        String milkmanUID = milkmanMobileNumberReference.push().getKey();
        milkmanMobileNumberReference.child(mobileNumber).setValue(milkmanUID);

        //Store Milkman details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference milkmanReference = database
                    .getReference(Constants.FIREBASE_CHILD_MILKMAN)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String milkmanName = editDailyServiceOrFamilyMemberName.getText().toString();
            String milkmanMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String milkmanPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentDailyNewspaper = new NammaApartmentDailyServices(milkmanName,
                    milkmanMobile, milkmanPhoto, false, rating);
            milkmanReference.child(milkmanUID).setValue(nammaApartmentDailyNewspaper);

            //Map Milkman's UID and Phone Number in users->private->userUID->myDailyServices->milkman
            DatabaseReference milkmanUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_MILKMAN);
            milkmanUserReference.child(milkmanUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds Laundry details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeLaundryDetails() {
        //Map Daily Laundry's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference laundryMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_LAUNDRY)
                .child(Constants.FIREBASE_CHILD_ALL);
        String laundryUID = laundryMobileNumberReference.push().getKey();
        laundryMobileNumberReference.child(mobileNumber).setValue(laundryUID);

        //Store Laundry details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference laundryReference = database
                    .getReference(Constants.FIREBASE_CHILD_LAUNDRY)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String laundryName = editDailyServiceOrFamilyMemberName.getText().toString();
            String laundryMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String laundryPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentLaundry = new NammaApartmentDailyServices(laundryName,
                    laundryMobile, laundryPhoto, false, rating);
            laundryReference.child(laundryUID).setValue(nammaApartmentLaundry);

            //Map Laundry's UID and Phone Number in users->private->userUID->myDailyServices->laundry
            DatabaseReference laundryUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_LAUNDRY);
            laundryUserReference.child(laundryUID).setValue(true);
        }
    }

    /**
     * This method gets invoked when user adds Driver details in 'My Daily Services'. Data gets stored in Firebase.
     */
    private void storeDriverDetails() {
        //Map Daily Driver's mobile number and uid
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driverMobileNumberReference = database
                .getReference(Constants.FIREBASE_CHILD_DRIVER)
                .child(Constants.FIREBASE_CHILD_ALL);
        String driverUID = driverMobileNumberReference.push().getKey();
        driverMobileNumberReference.child(mobileNumber).setValue(driverUID);

        //Store Driver details in Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference driverReference = database
                    .getReference(Constants.FIREBASE_CHILD_DRIVER)
                    .child(Constants.FIREBASE_CHILD_PUBLIC);

            String driverName = editDailyServiceOrFamilyMemberName.getText().toString();
            String driverMobile = editDailyServiceOrFamilyMemberMobile.getText().toString();
            String driverPhoto = "";
            int rating = Constants.FIREBASE_RATING;
            NammaApartmentDailyServices nammaApartmentDriver = new NammaApartmentDailyServices(driverName,
                    driverMobile, driverPhoto, false, rating);
            driverReference.child(driverUID).setValue(nammaApartmentDriver);

            //Map Driver's UID and Phone Number in users->private->userUID->myDailyServices->driver
            DatabaseReference driverUserReference = database
                    .getReference(Constants.FIREBASE_CHILD_USERS)
                    .child(Constants.FIREBASE_CHILD_PRIVATE)
                    .child(firebaseUser.getUid())
                    .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                    .child(Constants.FIREBASE_CHILD_DRIVER);
            driverUserReference.child(driverUID).setValue(true);
        }
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
