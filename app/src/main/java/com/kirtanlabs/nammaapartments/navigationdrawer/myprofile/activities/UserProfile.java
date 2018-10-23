package com.kirtanlabs.nammaapartments.navigationdrawer.myprofile.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ADMIN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PROFILE_OLD_CONTENT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getBitmapFromFile;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getByteArrayFromFile;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY;
import static pl.aprilapps.easyphotopicker.EasyImageConfig.REQ_TAKE_PICTURE;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textEIntercomNumber;
    private EditText editUserName, editUserEmail, editFlatAdmin;
    private AlertDialog imageSelectionDialog;
    private CircleImageView currentUserProfilePic;
    private File profilePhotoPath;
    private String userName, userEmail, adminName;
    private NammaApartmentUser currentNammaApartmentUser;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_user_profile;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressDialog(this, getString(R.string.loading_profile), getString(R.string.please_wait_a_moment));

        /*Here we are getting the instance of current user when screen loads for the first time*/
        currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        TextView textEIntercom = findViewById(R.id.textEIntercom);
        textEIntercomNumber = findViewById(R.id.textEIntercomNumber);
        TextView textName = findViewById(R.id.textName);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textFlatAdmin = findViewById(R.id.textFlatAdmin);
        TextView textErrorUserName = findViewById(R.id.textErrorUserName);
        TextView textErrorEmailId = findViewById(R.id.textErrorEmailId);
        TextView textErrorInvalidEmailId = findViewById(R.id.textErrorInvalidEmailId);
        editUserName = findViewById(R.id.editUserName);
        editUserEmail = findViewById(R.id.editUserEmail);
        editFlatAdmin = findViewById(R.id.editFlatAdmin);
        Button buttonGatePass = findViewById(R.id.buttonGatePass);

        /*Setting font for all the views*/
        textEIntercom.setTypeface(setLatoBoldFont(this));
        textEIntercomNumber.setTypeface(setLatoItalicFont(this));
        textName.setTypeface(setLatoBoldFont(this));
        textEmail.setTypeface(setLatoBoldFont(this));
        textFlatAdmin.setTypeface(setLatoBoldFont(this));
        textErrorUserName.setTypeface(setLatoRegularFont(this));
        textErrorEmailId.setTypeface(setLatoRegularFont(this));
        textErrorInvalidEmailId.setTypeface(setLatoRegularFont(this));
        editUserEmail.setTypeface(setLatoRegularFont(this));
        editUserName.setTypeface(setLatoRegularFont(this));
        editFlatAdmin.setTypeface(setLatoRegularFont(this));
        buttonGatePass.setTypeface(setLatoLightFont(this));

        /*This method is for retrieval of currentUser details and gets pre-filled in EditTexts and
         *image gets pre-loaded in circularImageView.*/
        retrieveUserDetails();

        /*We would want to retrieve admin Name well before the screen gets loaded.*/
        retrieveAdminName();

        /*Setting event for all button clicks */
        editUserName.setOnClickListener(this);
        editUserEmail.setOnClickListener(this);
        editFlatAdmin.setOnClickListener(this);
        currentUserProfilePic.setOnClickListener(this);
        buttonGatePass.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult
     * ------------------------------------------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                case REQ_PICK_PICTURE_FROM_GALLERY:
                    EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                        @Override
                        public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                            if (source == EasyImage.ImageSource.GALLERY || source == EasyImage.ImageSource.CAMERA) {
                                Bitmap userProfilePic = getBitmapFromFile(UserProfile.this, imageFile);
                                currentUserProfilePic.setImageBitmap(userProfilePic);
                                profilePhotoPath = imageFile;
                                updateProfilePicInFirebase();
                            }
                        }
                    });
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        Intent updateProfileIntent = new Intent(UserProfile.this, UpdateProfile.class);
        switch (v.getId()) {
            case R.id.currentUserProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.editUserName:
                updateProfileIntent.putExtra(SCREEN_TITLE, R.string.enter_your_name);
                updateProfileIntent.putExtra(PROFILE_OLD_CONTENT, userName);
                startActivity(updateProfileIntent);
                break;
            case R.id.editUserEmail:
                updateProfileIntent.putExtra(SCREEN_TITLE, R.string.enter_your_email);
                updateProfileIntent.putExtra(PROFILE_OLD_CONTENT, userEmail);
                startActivity(updateProfileIntent);
                break;
            case R.id.editFlatAdmin:
                NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                if (nammaApartmentUser.getFamilyMembers().isEmpty() && nammaApartmentUser.getFriends().isEmpty()) {
                    showNotificationDialog(getString(R.string.change_admin), getString(R.string.no_flat_members), null);
                } else {
                    updateProfileIntent.putExtra(SCREEN_TITLE, R.string.select_new_admin);
                    startActivity(updateProfileIntent);
                }
                break;
            case R.id.buttonGatePass:
                Intent gatePassIntent = new Intent(UserProfile.this, GatePassActivity.class);
                startActivity(gatePassIntent);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a custom flatMembersDialog with a list view which contains the list of inbuilt apps such as Camera and Gallery. This
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
     * This method gets invoked to pre-fill the details of existing user name and email id and also
     * existing user profile photo.
     */
    private void retrieveUserDetails() {
        userName = currentNammaApartmentUser.getPersonalDetails().getFullName();
        userEmail = currentNammaApartmentUser.getPersonalDetails().getEmail();
        String EIntercomNumber = Constants.COUNTRY_CODE_IN + " " + currentNammaApartmentUser.getPersonalDetails().getPhoneNumber();
        textEIntercomNumber.setText(EIntercomNumber);
        editUserName.setText(userName);
        editUserEmail.setText(userEmail);
        Glide.with(this.getApplicationContext()).load(currentNammaApartmentUser.getPersonalDetails().getProfilePhoto()).into(currentUserProfilePic);
        currentUserProfilePic.setTag(R.id.currentUserProfilePic, "Actual Image");
    }

    /**
     * This method gets invoked to retrieve and display appropriate admin name text in User Profile
     * Screen.
     */
    private void retrieveAdminName() {
        /*Based on the admin privileges we are displaying appropriate admin name text in Flat Admin*/
        if (currentNammaApartmentUser.getPrivileges().isAdmin()) {
            adminName = getResources().getString(R.string.admin_notification);
            editFlatAdmin.setText(adminName);
            hideProgressDialog();
        } else {
            /*We should not allow the other flat members to change the Administrator details*/
            editFlatAdmin.setEnabled(false);
            editFlatAdmin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            DatabaseReference adminNameReference = ((NammaApartmentsGlobal) getApplicationContext())
                    .getUserDataReference()
                    .child(FIREBASE_ADMIN);
            adminNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot adminUIDSnapshot) {
                    String adminUID = Objects.requireNonNull(adminUIDSnapshot.getValue(String.class));
                    DatabaseReference adminUIDNameReference = PRIVATE_USERS_REFERENCE.child(adminUID)
                            .child(FIREBASE_CHILD_PERSONALDETAILS)
                            .child(FIREBASE_CHILD_FULLNAME);
                    adminUIDNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot adminNameDataSnapshot) {
                            adminName = adminNameDataSnapshot.getValue(String.class);
                            editFlatAdmin.setText(adminName);
                            hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * Updates profile pic in firebase
     */
    private void updateProfilePicInFirebase() {
        /*displaying progress dialog while image is uploading*/
        showProgressDialog(this,
                getResources().getString(R.string.update_profile_photo),
                getResources().getString(R.string.please_wait_a_moment));

        String userUID = NammaApartmentsGlobal.userUID;
        StorageReference storageReference = FIREBASE_STORAGE.getReference(FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(userUID);

        UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(UserProfile.this, profilePhotoPath));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            String profilePhotoPath = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();
            currentNammaApartmentUser.getPersonalDetails().setProfilePhoto(profilePhotoPath);
            DatabaseReference updatedUserPhotoReference = PRIVATE_USERS_REFERENCE.child(currentNammaApartmentUser.getUID())
                    .child(FIREBASE_CHILD_PERSONALDETAILS)
                    .child(FIREBASE_CHILD_PROFILE_PHOTO);
            updatedUserPhotoReference.setValue(profilePhotoPath);

            /*dismissing the progress dialog*/
            hideProgressDialog();

        }).addOnFailureListener(exception -> {
            hideProgressDialog();
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

}
