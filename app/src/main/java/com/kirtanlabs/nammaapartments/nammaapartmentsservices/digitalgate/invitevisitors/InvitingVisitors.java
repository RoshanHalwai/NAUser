package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_MIN_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYVISITORS;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_MOBILE_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class InvitingVisitors extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private CircleImageView circleImageInvitingVisitors;
    private EditText editPickDateTime;
    private EditText editVisitorName;
    private EditText editVisitorMobile;
    private TextView textDescription;
    private Button buttonInvite;
    private AlertDialog imageSelectingOptions;
    private ListView listView;
    private String selectedDate;
    private String mobileNumber;
    private Uri selectedImage;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_inviting_visitors;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.inviting_visitors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        setupCustomDialog();

        /*Getting Id's for all the views*/
        circleImageInvitingVisitors = findViewById(R.id.invitingVisitorsProfilePic);
        TextView textVisitorName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorMobile = findViewById(R.id.textInvitorMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textDateTime = findViewById(R.id.textDateTime);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editPickDateTime = findViewById(R.id.editPickDateTime);
        textDescription = findViewById(R.id.textDescription);
        editVisitorName = findViewById(R.id.editVisitorName);
        editVisitorMobile = findViewById(R.id.editMobileNumber);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonInvite = findViewById(R.id.buttonInvite);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDateTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textVisitorName.setTypeface(setLatoBoldFont(this));
        textVisitorMobile.setTypeface(setLatoBoldFont(this));
        textOr.setTypeface(setLatoBoldFont(this));
        textDateTime.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        editPickDateTime.setTypeface(setLatoRegularFont(this));
        textDescription.setTypeface(setLatoBoldFont(this));
        editVisitorName.setTypeface(setLatoRegularFont(this));
        editVisitorMobile.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonInvite.setTypeface(setLatoLightFont(this));

        /*Setting event for views */
        circleImageInvitingVisitors.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        editPickDateTime.setOnClickListener(this);
        editPickDateTime.setOnFocusChangeListener(this);
        buttonInvite.setOnClickListener(this);

        /*This method gets invoked when user is trying to capture their profile photo either by clicking on camera and gallery.*/
        setupViewsForProfilePhoto();
        /*This method gets invoked when user is trying to modify the values on EditTexts.*/
        setEventsForEditText();
    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult
     * ------------------------------------------------------------- */

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
                                editVisitorName.setText(name);
                                editVisitorMobile.setText(formattedPhoneNumber);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_PERMISSION_REQUEST_CODE:
                    if (data.getExtras() != null) {
                        selectedImage = data.getData();
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageInvitingVisitors.setImageBitmap(bitmapProfilePic);
                        onSuccessfulUpload();
                        imageSelectingOptions.cancel();
                    } else {
                        onFailedUpload();
                        imageSelectingOptions.cancel();
                    }
                    break;

                case GALLERY_PERMISSION_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageInvitingVisitors.setImageBitmap(bitmapProfilePic);
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
            case R.id.invitingVisitorsProfilePic:
                imageSelectingOptions.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.editPickDateTime:
                pickDate(this, this);
                break;
            case R.id.buttonInvite:
                storeVisitorDetailsInFirebase();
                break;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickDate(this, this);
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet & OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            pickTime(this, this);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            String concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
            editPickDateTime.setText(concatenatedDateAndTime);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InvitingVisitors.this, android.R.layout.simple_list_item_1, pickImageList);

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
        circleImageInvitingVisitors.setVisibility(View.VISIBLE);
    }

    /**
     * This method will get invoked when user fails in uploading image from gallery and camera.
     */
    private void onFailedUpload() {
        circleImageInvitingVisitors.setVisibility(View.INVISIBLE);
        imageSelectingOptions.cancel();
    }

    /**
     * We are handling events for editTexts name,mobile number and date and time picker editText.
     */
    private void setEventsForEditText() {
        editVisitorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String visitorsName = editVisitorName.getText().toString().trim();
                String phoneNumber = editVisitorMobile.getText().toString().trim();
                String dateTime = editPickDateTime.getText().toString().trim();
                if (visitorsName.length() == Constants.EDIT_TEXT_MIN_LENGTH || isValidPersonName(visitorsName)) {
                    editVisitorName.setError(getString(R.string.accept_alphabets));
                }
                if (visitorsName.length() > EDIT_TEXT_MIN_LENGTH) {
                    if (dateTime.length() > EDIT_TEXT_MIN_LENGTH && phoneNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                        textDescription.setVisibility(View.VISIBLE);
                        buttonInvite.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        editVisitorMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textDescription.setVisibility(View.GONE);
                buttonInvite.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileNumber = editVisitorMobile.getText().toString().trim();
                if (mobileNumber.length() < PHONE_NUMBER_MAX_LENGTH) {
                    editVisitorMobile.setError(getString(R.string.number_10digit_validation));
                }
                if (isValidPhone(mobileNumber) && mobileNumber.length() >= PHONE_NUMBER_MAX_LENGTH) {
                    //editVisitorMobile.setError(null);
                    String dateTime = editPickDateTime.getText().toString().trim();
                    if (dateTime.length() > EDIT_TEXT_MIN_LENGTH) {
                        textDescription.setVisibility(View.VISIBLE);
                        buttonInvite.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        editPickDateTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String visitorName = editVisitorName.getText().toString().trim();
                String phoneNumber = editVisitorMobile.getText().toString().trim();
                boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editVisitorName, editVisitorMobile, editPickDateTime});
                if (fieldsFilled && visitorName.length() > EDIT_TEXT_MIN_LENGTH && phoneNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    textDescription.setVisibility(View.VISIBLE);
                    buttonInvite.setVisibility(View.VISIBLE);
                }
                if ((!fieldsFilled)) {
                    if (visitorName.length() == EDIT_TEXT_MIN_LENGTH) {
                        editVisitorName.setError(getString(R.string.name_validation));
                    }
                    if (phoneNumber.length() == EDIT_TEXT_MIN_LENGTH) {
                        editVisitorMobile.setError(getString(R.string.mobile_number_validation));
                    }
                }
            }
        });
    }

    /**
     * Shows Invitation success dialog when user successfully fills all the details.
     */
    private void createInviteDialog() {
        AlertDialog.Builder alertInvitationDialog = new AlertDialog.Builder(this);
        alertInvitationDialog.setMessage(R.string.invitation_message);
        alertInvitationDialog.setTitle("Invitation Message");
        alertInvitationDialog.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        alertInvitationDialog.create().show();
    }

    /**
     * Stores Visitor's record in Firebase
     */
    private void storeVisitorDetailsInFirebase() {
        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Inviting your Visitor");
        progressDialog.show();

        //Map Mobile number with visitor's UID
        DatabaseReference preApprovedVisitorsMobileNumberReference = PREAPPROVED_VISITORS_MOBILE_REFERENCE;
        String visitorUID = preApprovedVisitorsMobileNumberReference.push().getKey();
        preApprovedVisitorsMobileNumberReference.child(mobileNumber).child(visitorUID).setValue(true);

        //Store Visitor's UID under Users->myVisitors Child
        DatabaseReference preApprovedVisitorUID = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                .child(FIREBASE_CHILD_MYVISITORS);
        preApprovedVisitorUID.child(visitorUID).setValue(true);

        //Add Visitor record under visitors->private->preApprovedVisitors
        String visitorName = editVisitorName.getText().toString();
        String visitorMobile = editVisitorMobile.getText().toString();
        String visitorDateTime = editPickDateTime.getText().toString();
        NammaApartmentVisitor nammaApartmentVisitor = new NammaApartmentVisitor(visitorUID,
                visitorName, visitorMobile, visitorDateTime, Constants.NOT_ENTERED, NammaApartmentsGlobal.userUID);

        //getting the storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(Constants.FIREBASE_CHILD_VISITORS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_PREAPPROVEDVISITORS)
                .child(nammaApartmentVisitor.getUid());

        //adding the profile photo to storage reference and visitor data to real time database
        storageReference.putFile(selectedImage)
                .addOnSuccessListener(taskSnapshot -> {
                    //creating the upload object to store uploaded image details
                    nammaApartmentVisitor.setProfilePhoto(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());

                    //adding visitor data under PREAPPROVED_VISITORS_REFERENCE->Visitor UID
                    DatabaseReference preApprovedVisitorData = PREAPPROVED_VISITORS_REFERENCE.child(nammaApartmentVisitor.getUid());
                    preApprovedVisitorData.setValue(nammaApartmentVisitor);

                    //dismissing the progress dialog
                    progressDialog.dismiss();

                    //Notify users that they have successfully invited their visitor
                    createInviteDialog();
                })
                .addOnFailureListener(exception -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
