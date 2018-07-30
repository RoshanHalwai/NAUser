package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.ContactPicker;
import com.kirtanlabs.nammaapartments.ImagePicker;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.guests.GuestsList;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_MOBILE_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.ImagePicker.bitmapToByteArray;

public class InvitingVisitors extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private CircleImageView circleImageInvitingVisitors;
    private EditText editPickDateTime, editVisitorName, editVisitorMobile;
    private TextView textErrorProfilePic, textErrorDateTime;
    private AlertDialog imageSelectionDialog;
    private String selectedDate, mobileNumber;
    private byte[] profilePhotoByteArray;

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
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        circleImageInvitingVisitors = findViewById(R.id.invitingVisitorsProfilePic);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        TextView textVisitorName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorMobile = findViewById(R.id.textInvitorMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textDateTime = findViewById(R.id.textDateTime);
        textErrorDateTime = findViewById(R.id.textErrorDateTime);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editPickDateTime = findViewById(R.id.editPickDateTime);
        editVisitorName = findViewById(R.id.editVisitorName);
        editVisitorMobile = findViewById(R.id.editMobileNumber);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        Button buttonInvite = findViewById(R.id.buttonInvite);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDateTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textErrorProfilePic.setTypeface(setLatoBoldFont(this));
        textVisitorName.setTypeface(setLatoBoldFont(this));
        textVisitorMobile.setTypeface(setLatoBoldFont(this));
        textOr.setTypeface(setLatoBoldFont(this));
        textDateTime.setTypeface(setLatoBoldFont(this));
        textErrorDateTime.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        editPickDateTime.setTypeface(setLatoRegularFont(this));
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

    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult
     * ------------------------------------------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                    ContactPicker contactPicker = new ContactPicker(InvitingVisitors.this, data.getData());
                    editVisitorName.setText(contactPicker.retrieveContactName());
                    editVisitorMobile.setText(contactPicker.retrieveContactNumber());
                    editVisitorName.setError(null);
                    editVisitorMobile.setError(null);
                    break;
                case CAMERA_PERMISSION_REQUEST_CODE:
                case GALLERY_PERMISSION_REQUEST_CODE:
                    Bitmap bitmapProfilePic = ImagePicker.getImageFromResult(this, resultCode, data);
                    circleImageInvitingVisitors.setImageBitmap(bitmapProfilePic);
                    profilePhotoByteArray = bitmapToByteArray(bitmapProfilePic);
                    if (profilePhotoByteArray != null) {
                        textErrorProfilePic.setVisibility(View.INVISIBLE);
                    }
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
                imageSelectionDialog.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.editPickDateTime:
                pickDate(this, this);
                break;
            case R.id.buttonInvite:
                /* This method gets invoked to check all the editText fields for validations.*/
                validateFields();
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
            textErrorDateTime.setVisibility(View.GONE);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a custom dialog with a list view which contains the list of inbuilt apps such as Camera and Gallery. This
     * imageSelectionDialog is displayed when user clicks on profile image which is on top of the screen.
     */
    private void createImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectionOptions = {
                getString(R.string.gallery),
                getString(R.string.camera),
                getString(R.string.cancel)
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

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked to check all the validation fields such as editTexts name,mobile number
     * and date and time editTexts.
     */
    private void validateFields() {
        String visitorsName = editVisitorName.getText().toString().trim();
        mobileNumber = editVisitorMobile.getText().toString().trim();
        String dateTime = editPickDateTime.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editVisitorName, editVisitorMobile, editPickDateTime});
        /*This condition checks if all fields are not filled and if user presses invite button it will then display proper error messages.*/
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(visitorsName)) {
                editVisitorName.setError(getString(R.string.name_validation));
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                editVisitorMobile.setError(getString(R.string.mobile_number_validation));
            }
            if (TextUtils.isEmpty(dateTime)) {
                textErrorDateTime.setVisibility(View.VISIBLE);
            }
        }
        /*This condition checks for if user has filled all the fields and also validates name,mobile number
          and displays appropriate error messages.*/
        if (fieldsFilled) {
            if (isValidPersonName(visitorsName)) {
                editVisitorName.setError(getString(R.string.accept_alphabets));
                editVisitorName.requestFocus();
            }
            if (!isValidPhone(mobileNumber)) {
                editVisitorMobile.setError(getString(R.string.number_10digit_validation));
                editVisitorMobile.requestFocus();
            }
        }
        /*This condition checks if profile photo is uploaded or not else it will display appropriate
         * error message.*/
        if (profilePhotoByteArray == null) {
            textErrorProfilePic.setVisibility(View.VISIBLE);
        }
        /*This condition checks if name,mobile number and email are properly validated and then
          navigate to proper screen according to its requirement.*/
        if (!isValidPersonName(visitorsName) && isValidPhone(mobileNumber) && !TextUtils.isEmpty(dateTime) && profilePhotoByteArray != null) {
            storeVisitorDetailsInFirebase();
        }
    }

    /**
     * Stores Visitor's record in Firebase
     */
    private void storeVisitorDetailsInFirebase() {
        /*displaying progress dialog while image is uploading*/
        showProgressDialog(this,
                getResources().getString(R.string.inviting_your_visitor),
                getResources().getString(R.string.please_wait_a_moment));

        /*Map Mobile number with visitor's UID*/
        DatabaseReference preApprovedVisitorsMobileNumberReference = PREAPPROVED_VISITORS_MOBILE_REFERENCE;
        String visitorUID = preApprovedVisitorsMobileNumberReference.push().getKey();
        preApprovedVisitorsMobileNumberReference.child(mobileNumber).setValue(visitorUID);

        /*Store Visitor's UID under User Data -> User UId*/
        DatabaseReference userVisitorReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_VISITORS).child(NammaApartmentsGlobal.userUID);
        userVisitorReference.child(visitorUID).setValue(true);

        /*Add Visitor record under visitors->private->preApprovedVisitors*/
        String visitorName = editVisitorName.getText().toString();
        String visitorMobile = editVisitorMobile.getText().toString();
        String visitorDateTime = editPickDateTime.getText().toString();
        NammaApartmentGuest nammaApartmentGuest = new NammaApartmentGuest(visitorUID,
                visitorName, visitorMobile, visitorDateTime, NammaApartmentsGlobal.userUID);

        /*getting the storage reference*/
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_VISITORS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_PREAPPROVEDVISITORS)
                .child(nammaApartmentGuest.getUid());

        UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(profilePhotoByteArray));

        /*adding the profile photo to storage reference and visitor data to real time database*/
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            /*creating the upload object to store uploaded image details*/
            nammaApartmentGuest.setProfilePhoto(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());

            /*adding visitor data under PREAPPROVED_VISITORS_REFERENCE->Visitor UID*/
            DatabaseReference preApprovedVisitorData = PREAPPROVED_VISITORS_REFERENCE.child(nammaApartmentGuest.getUid());
            preApprovedVisitorData.setValue(nammaApartmentGuest);

            /*dismissing the progress dialog*/
            hideProgressDialog();

            /*Notify users that they have successfully invited their visitor*/
            Intent guestsListIntent = new Intent(InvitingVisitors.this, GuestsList.class);
            guestsListIntent.putExtra(SCREEN_TITLE, getClass().toString());
            showNotificationDialog(getResources().getString(R.string.invitation_success_title),
                    getResources().getString(R.string.invitation_success_message),
                    guestsListIntent);
        }).addOnFailureListener(exception -> {
            hideProgressDialog();
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
