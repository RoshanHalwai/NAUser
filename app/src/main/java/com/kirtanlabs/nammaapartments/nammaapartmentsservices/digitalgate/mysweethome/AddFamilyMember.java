package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPrivileges;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.AFM_OTP_STATUS_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
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
public class AddFamilyMember extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private CircleImageView circleImageView;
    private TextView textErrorProfilePic;
    private TextView textErrorRelation;
    private TextView textOtpDescriptionFamilyMemberOrFriend;
    private EditText editFamilyMemberName;
    private EditText editFamilyMemberMobile;
    private EditText editFamilyMemberEmail;
    private Button buttonYes;
    private Button buttonNo;
    private RadioButton radioButtonFriend;
    private RadioButton radioButtonFamilyMember;
    private AlertDialog imageSelectionDialog;
    private boolean grantedAccess;
    private String mobileNumber;

    private byte[] profilePhotoByteArray;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_family_members;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_my_family_members;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        TextView textFamilyMemberName = findViewById(R.id.textFamilyMemberName);
        TextView textFamilyMemberMobile = findViewById(R.id.textFamilyMemberMobile);
        TextView textFamilyMemberEmail = findViewById(R.id.textFamilyMemberEmail);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textRelation = findViewById(R.id.textFamilyMemberRelation);
        textErrorRelation = findViewById(R.id.textErrorRelation);
        TextView textOr = findViewById(R.id.textOr);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        textOtpDescriptionFamilyMemberOrFriend = findViewById(R.id.textOtpDescriptionFamilyMemberOrFriend);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        editFamilyMemberName = findViewById(R.id.editFamilyMemberName);
        editFamilyMemberMobile = findViewById(R.id.editFamilyMemberMobile);
        editFamilyMemberEmail = findViewById(R.id.editFamilyMemberEmail);
        radioButtonFamilyMember = findViewById(R.id.radioButtonFamilyMember);
        radioButtonFriend = findViewById(R.id.radioButtonFriend);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.familyMemberProfilePic);

        /*Setting font for all the views*/
        textFamilyMemberName.setTypeface(setLatoBoldFont(this));
        textFamilyMemberMobile.setTypeface(setLatoBoldFont(this));
        textFamilyMemberEmail.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        textRelation.setTypeface(setLatoBoldFont(this));
        textOr.setTypeface(setLatoBoldFont(this));
        textGrantAccess.setTypeface(setLatoBoldFont(this));
        textOtpDescriptionFamilyMemberOrFriend.setTypeface(setLatoBoldFont(this));
        textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        textErrorRelation.setTypeface(setLatoRegularFont(this));
        editFamilyMemberName.setTypeface(setLatoRegularFont(this));
        editFamilyMemberEmail.setTypeface(setLatoRegularFont(this));
        editFamilyMemberMobile.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonYes.setTypeface(setLatoRegularFont(this));
        buttonNo.setTypeface(setLatoRegularFont(this));
        buttonAdd.setTypeface(setLatoLightFont(this));
        radioButtonFamilyMember.setTypeface(setLatoRegularFont(this));
        radioButtonFriend.setTypeface(setLatoRegularFont(this));

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        radioButtonFamilyMember.setOnClickListener(this);
        radioButtonFriend.setOnClickListener(this);

        editFamilyMemberMobile.setOnFocusChangeListener(this);

    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                    ContactPicker contactPicker = new ContactPicker(AddFamilyMember.this, data.getData());
                    editFamilyMemberName.setText(contactPicker.retrieveContactName());
                    editFamilyMemberMobile.setText(contactPicker.retrieveContactNumber());
                    /*We need to check if mobile number selected from contact already exists
                     * in firebase*/
                    onFocusChange(editFamilyMemberMobile, false);
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

                case AFM_OTP_STATUS_REQUEST_CODE:
                    storeFamilyMembersDetails();
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
            case R.id.familyMemberProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.radioButtonFamilyMember:
                //This line hides the relation error message if it was shown on if any of the fields are not filled.
                textErrorRelation.setVisibility(View.GONE);
                textOtpDescriptionFamilyMemberOrFriend.setText(getResources().getString(R.string.otp_message_family_member));
                textOtpDescriptionFamilyMemberOrFriend.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButtonFriend:
                //This line hides the relation error message if it was shown on if any of the fields are not filled.
                textErrorRelation.setVisibility(View.GONE);
                textOtpDescriptionFamilyMemberOrFriend.setText(getResources().getString(R.string.otp_message_friend));
                textOtpDescriptionFamilyMemberOrFriend.setVisibility(View.VISIBLE);
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
            case R.id.buttonAdd:
                    if (profilePhotoByteArray == null) {
                        textErrorProfilePic.setVisibility(View.VISIBLE);
                        textErrorProfilePic.requestFocus();
                    } else {
                        textErrorProfilePic.setVisibility(View.INVISIBLE);
                    }
                // This method gets invoked to check all the validation fields such as editTexts and radioButtons.
                validateFields();
                break;
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

    /**
     * This method gets invoked when user tries to add family member and also giving access.
     */
    private void openNotificationDialog() {
        AlertDialog.Builder alertNotificationDialog = new AlertDialog.Builder(this);
        View notificationDialog = View.inflate(this, R.layout.layout_dialog_grant_access_yes, null);
        alertNotificationDialog.setView(notificationDialog);
        imageSelectionDialog = alertNotificationDialog.create();

        // Setting Custom Dialog Buttons
        alertNotificationDialog.setPositiveButton("Accept", (dialog, which) -> navigatingToOTPScreen());
        alertNotificationDialog.setNegativeButton("Reject", (dialog, which) -> dialog.cancel());

        new Dialog(getApplicationContext());
        alertNotificationDialog.show();
    }

    /**
     * We need to navigate to OTP Screen based on the user selection of giving access and also on not giving access.
     */
    private void navigatingToOTPScreen() {
        Intent otpIntent = new Intent(AddFamilyMember.this, OTP.class);
        mobileNumber = editFamilyMemberMobile.getText().toString();
        String relationType = radioButtonFamilyMember.isChecked()
                ? radioButtonFamilyMember.getText().toString()
                : radioButtonFriend.getText().toString();
        otpIntent.putExtra(MOBILE_NUMBER, mobileNumber);
        otpIntent.putExtra(SCREEN_TITLE, R.string.add_family_members_details_screen);
        otpIntent.putExtra(SERVICE_TYPE, relationType);
        startActivityForResult(otpIntent, AFM_OTP_STATUS_REQUEST_CODE);
    }

    /**
     * Store family member's details to firebase and map them to the users family members for future use
     */
    private void storeFamilyMembersDetails() {
        //displaying progress dialog while image is uploading
        showProgressDialog(this,
                getString(R.string.adding_your_family_member),
                getString(R.string.please_wait_a_moment));

        //Map family member's mobile number with uid in users->all
        DatabaseReference familyMemberMobileNumberReference = ALL_USERS_REFERENCE.child(mobileNumber);
        familyMemberMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String familyMemberUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                familyMemberMobileNumberReference.setValue(familyMemberUID);

                //getting the storage reference
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(Constants.FIREBASE_CHILD_USERS)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(familyMemberUID);

                UploadTask uploadTask = storageReference.putBytes(Objects.requireNonNull(profilePhotoByteArray));

                //adding the profile photo to storage reference and daily service data to real time database
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    //Store family member's UID under users data structure for future use
                    String fullName = editFamilyMemberName.getText().toString();
                    String phoneNumber = editFamilyMemberMobile.getText().toString();
                    String email = editFamilyMemberEmail.getText().toString();
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    UserPersonalDetails userPersonalDetails = new UserPersonalDetails(email, fullName, phoneNumber, Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                    UserFlatDetails userFlatDetails = currentNammaApartmentUser.getFlatDetails();
                    UserPrivileges userPrivileges = new UserPrivileges(false, grantedAccess, false);

                    NammaApartmentUser familyMember = new NammaApartmentUser(
                            familyMemberUID,
                            userPersonalDetails,
                            userFlatDetails,
                            userPrivileges
                    );

                    /*Storing new family member details in firebase under users->private->family member uid*/
                    PRIVATE_USERS_REFERENCE.child(familyMemberUID).setValue(familyMember);

                    //dismissing the progress dialog
                    hideProgressDialog();

                    /*Storing user UID under Flats*/
                    DatabaseReference flatsReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
                    flatsReference.child(FIREBASE_CHILD_FLAT_MEMBERS).child(familyMemberUID).setValue(true);

                    /*If Relation is Family Member we share UID to user as well as family members and store the UID
                     * under familyMembers child*/
                    if (radioButtonFamilyMember.isChecked()) {
                        DatabaseReference familyMemberReference = PRIVATE_USERS_REFERENCE.child(familyMemberUID)
                                .child(Constants.FIREBASE_CHILD_FAMILY_MEMBERS);
                        familyMemberReference.child(NammaApartmentsGlobal.userUID).setValue(true);
                        DatabaseReference currentUserReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                                .child(Constants.FIREBASE_CHILD_FAMILY_MEMBERS);
                        currentUserReference.child(familyMemberUID).setValue(true);
                    }

                    /*If Relation is Friend we share UID to user as well as friend and store the UID
                     * under friends child*/
                    else {
                        DatabaseReference friendsReference = PRIVATE_USERS_REFERENCE.child(familyMemberUID).
                                child(Constants.FIREBASE_CHILD_FRIENDS);
                        friendsReference.child(NammaApartmentsGlobal.userUID).setValue(true);
                        DatabaseReference currentUserReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                                .child(Constants.FIREBASE_CHILD_FRIENDS);
                        currentUserReference.child(familyMemberUID).setValue(true);
                    }

                    /* Once we are done with storing data we need to call MySweetHome screen again
                     * to show users that their family member have been added successfully*/
                    Intent MySweetHomeIntent = new Intent(AddFamilyMember.this, MySweetHome.class);
                    MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    showSuccessDialog(getString(R.string.family_member_success_title),
                            getString(R.string.family_member_success_message),
                            MySweetHomeIntent);
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

    /**
     * We ensure User does not add a family member whose mobile number already exists
     *
     * @param v        of mobile number edit text
     * @param hasFocus of mobile number edit text
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (editFamilyMemberMobile.getText().length() == PHONE_NUMBER_MAX_LENGTH) {
                mobileNumber = editFamilyMemberMobile.getText().toString();
                DatabaseReference familyMemberMobileNumberReference = ALL_USERS_REFERENCE.child(mobileNumber);
                familyMemberMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            editFamilyMemberMobile.requestFocus();
                            Drawable drawableWrap = DrawableCompat.wrap(
                                    Objects.requireNonNull(ContextCompat.getDrawable(
                                            AddFamilyMember.this,
                                            android.R.drawable.stat_sys_warning)
                                    )
                            );
                            drawableWrap.mutate();
                            DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(AddFamilyMember.this, R.color.nmBlack));
                            drawableWrap.setBounds(0, 0, drawableWrap.getIntrinsicWidth(), drawableWrap.getIntrinsicHeight());
                            editFamilyMemberMobile.setError(getString(R.string.mobile_number_exists), drawableWrap);
                            editFamilyMemberMobile.setSelection(editFamilyMemberMobile.length());
                        } else {
                            editFamilyMemberMobile.setError(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    /**
     * This method gets invoked to check all the validation fields such as editTexts and radioButtons.
     */
    private void validateFields() {
        String familyMemberName = editFamilyMemberName.getText().toString().trim();
        mobileNumber = editFamilyMemberMobile.getText().toString().trim();
        String familyMemberEmail = editFamilyMemberEmail.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editFamilyMemberName, editFamilyMemberMobile, editFamilyMemberEmail});
        //This condition checks if all fields are not filled and if user presses add button it will then display proper error messages.
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(familyMemberName)) {
                editFamilyMemberName.setError(getString(R.string.name_validation));
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                editFamilyMemberMobile.setError(getString(R.string.mobile_number_validation));
            }
            if (TextUtils.isEmpty(familyMemberEmail)) {
                editFamilyMemberEmail.setError(getString(R.string.email_validation));
            }
            if (!radioButtonFamilyMember.isChecked() && !radioButtonFriend.isChecked()) {
                textErrorRelation.setVisibility(View.VISIBLE);
            }
        }
        //This condition checks for if user has filled all the fields and also validates name,email and mobile number
        //and displays proper error messages.
        if (fieldsFilled) {
            if (!radioButtonFamilyMember.isChecked() && !radioButtonFriend.isChecked()) {
                textErrorRelation.setVisibility(View.VISIBLE);
            }
            if (isValidEmail(familyMemberEmail)) {
                editFamilyMemberEmail.setError(getString(R.string.invalid_email));
            }
            if (isValidPersonName(familyMemberName)) {
                editFamilyMemberName.setError(getString(R.string.accept_alphabets));
            }
            if (!isValidPhone(mobileNumber)) {
                editFamilyMemberMobile.setError(getString(R.string.number_10digit_validation));
            }
        }
        //This condition checks if name,mobile number and email are properly validated and then
        // navigate to proper screen according to its requirement.
        if (!isValidPersonName(familyMemberName) && isValidPhone(mobileNumber) && !isValidEmail(familyMemberEmail)) {
            if (grantedAccess)
                openNotificationDialog();
            else {
                navigatingToOTPScreen();
            }
        }
    }

}
