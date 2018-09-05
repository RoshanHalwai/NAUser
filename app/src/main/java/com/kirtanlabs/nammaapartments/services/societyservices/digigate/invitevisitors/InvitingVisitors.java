package com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.activities.FrequentlyAskedQuestionsActivity;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.guests.GuestsList;
import com.kirtanlabs.nammaapartments.utilities.Constants;
import com.kirtanlabs.nammaapartments.utilities.ContactPicker;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PREAPPROVED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOT_ENTERED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.READ_CONTACTS_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY;

public class InvitingVisitors extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editPickDateTime, editVisitorName, editVisitorMobile;
    private TextView textErrorProfilePic, textErrorDateTime, textErrorFutureTime;
    private AlertDialog imageSelectionDialog;
    private String selectedDate, mobileNumber;
    private CircleImageView circleImageView;
    private File profilePhotoPath;
    private Calendar calendar;
    private boolean isTodayDateSelected;

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
        circleImageView = findViewById(R.id.invitingVisitorsProfilePic);
        ImageView infoButton = findViewById(R.id.infoButton);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        TextView textVisitorName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorMobile = findViewById(R.id.textInvitorMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textDateTime = findViewById(R.id.textDateTime);
        textErrorDateTime = findViewById(R.id.textErrorDateTime);
        textErrorFutureTime = findViewById(R.id.textErrorFutureTime);
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
        textErrorFutureTime.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        editPickDateTime.setTypeface(setLatoRegularFont(this));
        editVisitorName.setTypeface(setLatoRegularFont(this));
        editVisitorMobile.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonInvite.setTypeface(setLatoLightFont(this));

        /*Setting event for views */
        circleImageView.setOnClickListener(this);
        infoButton.setOnClickListener(this);
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

                case REQ_PICK_PICTURE_FROM_GALLERY:
                    EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                        @Override
                        public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                            Bitmap guestProfilePic = getBitmapFromFile(InvitingVisitors.this, imageFile);
                            circleImageView.setImageBitmap(guestProfilePic);
                            profilePhotoPath = imageFile;
                            if (profilePhotoPath != null) {
                                textErrorProfilePic.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.infoButton:
                Intent infoIntent = new Intent(InvitingVisitors.this, FrequentlyAskedQuestionsActivity.class);
                infoIntent.putExtra(SCREEN_TITLE, R.string.inviting_visitors);
                startActivity(infoIntent);
                break;
            case R.id.invitingVisitorsProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.editPickDateTime:
                pickDate(this, this);
                editPickDateTime.setText("");
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
            calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            isTodayDateSelected = month == currentMonth && dayOfMonth == currentDay;
            selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            pickTime(this, this);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);
            if (isTodayDateSelected) {
                if (hourOfDay < currentHour) {
                    textErrorFutureTime.setVisibility(View.VISIBLE);
                    return;
                } else if (hourOfDay == currentHour && minute < currentMinute) {
                    textErrorFutureTime.setVisibility(View.VISIBLE);
                    return;
                }
            }
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            String concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
            editPickDateTime.setText(concatenatedDateAndTime);
            textErrorDateTime.setVisibility(View.GONE);
            textErrorFutureTime.setVisibility(View.GONE);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a custom dialog with a list view which contains Gallery option. This
     * imageSelectionDialog is displayed when user clicks on profile image which is on top of the screen.
     */
    private void createImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] selectionOptions = {
                getString(R.string.gallery),
                getString(R.string.cancel)
        };
        builder.setItems(selectionOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    pickImageFromGallery();
                    break;
                case 1:
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
        if (profilePhotoPath == null) {
            textErrorProfilePic.setVisibility(View.VISIBLE);
        }
        /*This condition checks if name,mobile number and email are properly validated and then
          navigate to proper screen according to its requirement.*/
        if (!isValidPersonName(visitorsName) && isValidPhone(mobileNumber) && !TextUtils.isEmpty(dateTime) && profilePhotoPath != null) {
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
        DatabaseReference preApprovedVisitorsMobileNumberReference = ALL_VISITORS_REFERENCE;
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
                visitorName, visitorMobile, visitorDateTime, NammaApartmentsGlobal.userUID, FIREBASE_CHILD_PREAPPROVED);

        /*getting the storage reference*/
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_VISITORS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(nammaApartmentGuest.getUid());

        UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(InvitingVisitors.this, profilePhotoPath));

        /*adding the profile photo to storage reference and visitor data to real time database*/
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            /*creating the upload object to store uploaded image details*/
            nammaApartmentGuest.setProfilePhoto(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());

            /*Setting Status as NOT_ENTERED For PreApproved Visitors */
            nammaApartmentGuest.setStatus(NOT_ENTERED);

            /*adding visitor data under PREAPPROVED_VISITORS_REFERENCE->Visitor UID*/
            DatabaseReference preApprovedVisitorData = PRIVATE_VISITORS_REFERENCE.child(nammaApartmentGuest.getUid());
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
