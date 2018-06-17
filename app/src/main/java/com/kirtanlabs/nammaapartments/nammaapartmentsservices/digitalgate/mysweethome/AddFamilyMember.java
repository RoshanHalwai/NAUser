package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.kirtanlabs.nammaapartments.userpojo.UserPrivileges;

import java.io.IOException;
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

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/17/2018
 */
public class AddFamilyMember extends BaseActivity implements View.OnClickListener {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private CircleImageView circleImageView;
    private TextView textErrorProfilePic;
    private EditText editFamilyMemberName;
    private EditText editFamilyMemberMobile;
    private EditText editFamilyMemberEmail;
    private Button buttonYes;
    private Button buttonNo;
    private RadioButton radioButtonFamilyMember;
    private AlertDialog imageSelectionDialog;
    private boolean grantedAccess;
    private String mobileNumber;
    private Uri selectedImage;

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
        TextView textOr = findViewById(R.id.textOr);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        TextView textDescriptionFamilyMember = findViewById(R.id.textDescriptionFamilyMember);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        editFamilyMemberName = findViewById(R.id.editFamilyMemberName);
        editFamilyMemberMobile = findViewById(R.id.editFamilyMemberMobile);
        editFamilyMemberEmail = findViewById(R.id.editFamilyMemberEmail);
        radioButtonFamilyMember = findViewById(R.id.radioButtonFamilyMember);
        RadioButton radioButtonFriend = findViewById(R.id.radioButtonFriend);
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
        textDescriptionFamilyMember.setTypeface(setLatoBoldFont(this));
        textErrorProfilePic.setTypeface(setLatoRegularFont(this));
        editFamilyMemberName.setTypeface(setLatoRegularFont(this));
        editFamilyMemberEmail.setTypeface(setLatoRegularFont(this));
        editFamilyMemberMobile.setTypeface(setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(setLatoLightFont(this));
        buttonYes.setTypeface(setLatoRegularFont(this));
        buttonNo.setTypeface(setLatoRegularFont(this));
        buttonAdd.setTypeface(setLatoLightFont(this));

        /*Setting event for all button clicks */
        circleImageView.setOnClickListener(this);
        buttonSelectFromContact.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        radioButtonFamilyMember.setOnClickListener(this);
        radioButtonFriend.setOnClickListener(this);
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
                                editFamilyMemberName.setText(name);
                                editFamilyMemberMobile.setText(formattedPhoneNumber);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_PERMISSION_REQUEST_CODE:
                    selectedImage = data.getData();
                    if (data.getExtras() != null) {
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageView.setImageBitmap(bitmapProfilePic);
                        imageSelectionDialog.cancel();
                        textErrorProfilePic.setVisibility(View.GONE);
                    } else {
                        imageSelectionDialog.cancel();
                    }
                    break;

                case GALLERY_PERMISSION_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageView.setImageBitmap(bitmapProfilePic);
                            imageSelectionDialog.cancel();
                            textErrorProfilePic.setVisibility(View.GONE);
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        imageSelectionDialog.cancel();
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
            case R.id.familyMemberProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.buttonSelectFromContact:
                showUserContacts();
                break;
            case R.id.radioButtonFamilyMember:
                break;
            case R.id.radioButtonFriend:
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
                if (selectedImage == null) {
                    textErrorProfilePic.setVisibility(View.VISIBLE);
                } else {
                    if (isAllFieldsFilled(new EditText[]{editFamilyMemberName, editFamilyMemberMobile, editFamilyMemberEmail})
                            && editFamilyMemberMobile.length() == PHONE_NUMBER_MAX_LENGTH) {
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
        otpIntent.putExtra(MOBILE_NUMBER, mobileNumber);
        otpIntent.putExtra(SCREEN_TITLE, R.string.add_family_members_details_screen);
        otpIntent.putExtra(SERVICE_TYPE, "Family Member");
        startActivityForResult(otpIntent, AFM_OTP_STATUS_REQUEST_CODE);
    }

    /**
     * Store family member's details to firebase and map them to the users family members for future use
     */
    private void storeFamilyMembersDetails() {
        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding your Family Member");
        progressDialog.setMessage("Please wait a moment");
        progressDialog.show();

        //Map family member's mobile number with uid in users->all
        DatabaseReference familyMemberMobileNumberReference = ALL_USERS_REFERENCE.child(mobileNumber);
        familyMemberMobileNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String familyMemberUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                //If family members mobile number already exists we don't create a new UID for them
                if (!dataSnapshot.exists()) {
                    familyMemberMobileNumberReference.setValue(familyMemberUID);
                }

                //Store family member's UID under users data structure for future use
                String fullName = editFamilyMemberName.getText().toString();
                String phoneNumber = editFamilyMemberMobile.getText().toString();
                String email = editFamilyMemberEmail.getText().toString();
                NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                UserPersonalDetails userPersonalDetails = new UserPersonalDetails(email, fullName, phoneNumber, selectedImage.toString());
                UserFlatDetails userFlatDetails = currentNammaApartmentUser.getFlatDetails();
                UserPrivileges userPrivileges = new UserPrivileges(false, grantedAccess, false);

                NammaApartmentUser familyMember = new NammaApartmentUser(
                        familyMemberUID,
                        userPersonalDetails,
                        userFlatDetails,
                        userPrivileges
                );

                //getting the storage reference
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(Constants.FIREBASE_CHILD_USERS)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(familyMemberUID);

                //adding the profile photo to storage reference and daily service data to real time database
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(taskSnapshot -> {

                            /*Storing new family member details in firebase under users->private->family member uid*/
                            PRIVATE_USERS_REFERENCE.child(familyMemberUID).setValue(familyMember);

                            //dismissing the progress dialog
                            progressDialog.dismiss();

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
                            startActivity(MySweetHomeIntent);
                        })
                        .addOnFailureListener(exception -> {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
