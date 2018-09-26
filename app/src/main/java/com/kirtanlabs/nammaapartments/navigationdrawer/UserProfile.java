package com.kirtanlabs.nammaapartments.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ADMIN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_EMAIL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_STORAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
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

    private TextView textEIntercomNumber, textErrorUserName, textErrorEmailId, textErrorInvalidEmailId;
    private EditText editUserName, editUserEmail, editFlatAdmin;
    private AlertDialog imageSelectionDialog;
    private Dialog flatMembersDialog;
    private final List<String> flatMembersList = new ArrayList<>();
    private final List<String> flatMembersUIDList = new ArrayList<>();
    private CircleImageView currentUserProfilePic;
    private File profilePhotoPath;
    private final int index = 0;
    private String userName, userEmail, adminName, itemValue, selectedFamilyMemberUID;
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

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Here we are getting the instance of current user when screen loads for the first time*/
        currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();

        /*We would want to have all family member names well before user clicks on Change Admin Button*/
        fillFlatMemberNames();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        TextView textEIntercom = findViewById(R.id.textEIntercom);
        textEIntercomNumber = findViewById(R.id.textEIntercomNumber);
        TextView textName = findViewById(R.id.textName);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textFlatAdmin = findViewById(R.id.textFlatAdmin);
        textErrorUserName = findViewById(R.id.textErrorUserName);
        textErrorEmailId = findViewById(R.id.textErrorEmailId);
        textErrorInvalidEmailId = findViewById(R.id.textErrorInvalidEmailId);
        editUserName = findViewById(R.id.editUserName);
        editUserEmail = findViewById(R.id.editUserEmail);
        editFlatAdmin = findViewById(R.id.editFlatAdmin);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);

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
        buttonUpdate.setTypeface(setLatoLightFont(this));

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
        buttonUpdate.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.currentUserProfilePic:
                imageSelectionDialog.show();
                break;
            case R.id.editUserName:
                textErrorUserName.setVisibility(View.GONE);
                break;
            case R.id.editUserEmail:
                textErrorEmailId.setVisibility(View.GONE);
                textErrorInvalidEmailId.setVisibility(View.GONE);
                break;
            case R.id.editFlatAdmin:
                if (flatMembersList.isEmpty()) {
                    showNotificationDialog(getString(R.string.change_admin), getString(R.string.no_flat_members), null);
                } else {
                    flatMembersDialog.show();
                }
                break;
            case R.id.buttonUpdate:
                /* This method gets invoked to check all the editText fields for validations.*/
                validateFields();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked to check all the validation fields such as editTexts name and email.
     */
    private void validateFields() {
        String userFullName = editUserName.getText().toString();
        String userEmailId = editUserEmail.getText().toString();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editUserName, editUserEmail});
        /*This condition checks if all fields are not filled and if user presses update button
         *it will then display proper error messages.*/
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(userFullName)) {
                textErrorUserName.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(userEmailId)) {
                textErrorEmailId.setVisibility(View.VISIBLE);
            }
        }
        /*This condition checks for if user has filled all the fields and also validates email
         *and displays appropriate error messages.*/
        if (fieldsFilled) {
            if (isValidEmail(userEmailId)) {
                textErrorInvalidEmailId.setVisibility(View.VISIBLE);
            } else {
                updateUserDetailsInFirebase();
            }
        }
    }

    /**
     * Fills the family members related to that particular user.
     */
    private void fillFlatMemberNames() {
        familyMemberList(list -> friendsMemberList(list1 -> createFlatMembersDialog()));
    }

    private void familyMemberList(FirebaseCallback callback) {
        Map<String, Boolean> familyMembers = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getFamilyMembers();
        if (!familyMembers.isEmpty()) {
            for (String familyMemberUID : familyMembers.keySet()) {
                DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(familyMemberUID)
                        .child(FIREBASE_CHILD_PERSONALDETAILS)
                        .child(FIREBASE_CHILD_FULLNAME);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String flatMemberName = dataSnapshot.getValue(String.class);
                        flatMembersList.add(index, flatMemberName);
                        flatMembersUIDList.add(index, familyMemberUID);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }
        callback.onCallback(flatMembersList);
    }

    private void friendsMemberList(FirebaseCallback callback) {
        Map<String, Boolean> friends = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getFriends();
        if (!friends.isEmpty()) {
            for (String friendUID : friends.keySet()) {
                DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(friendUID)
                        .child(FIREBASE_CHILD_PERSONALDETAILS)
                        .child(FIREBASE_CHILD_FULLNAME);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String flatMemberName = dataSnapshot.getValue(String.class);
                        flatMembersList.add(index, flatMemberName);
                        flatMembersUIDList.add(index, friendUID);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }
        callback.onCallback(flatMembersList);
    }

    /**
     * This method gets invoked when user tries to change admin by clicking on Flat Admin editText.
     */
    private void createFlatMembersDialog() {
        flatMembersDialog = new Dialog(UserProfile.this);
        flatMembersDialog.setContentView(R.layout.layout_search_flat);
        TextView textFlatMembers = flatMembersDialog.findViewById(R.id.textFlatMembers);
        ListView listFlatMembers = flatMembersDialog.findViewById(R.id.list);
        EditText inputSearch = flatMembersDialog.findViewById(R.id.inputSearch);
        inputSearch.setVisibility(View.GONE);
        textFlatMembers.setVisibility(View.VISIBLE);
        textFlatMembers.setTypeface(Constants.setLatoBoldFont(UserProfile.this));
        ArrayAdapter<String> adapterFlatMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, flatMembersList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(UserProfile.this));
                return view;
            }
        };
        listFlatMembers.setAdapter(adapterFlatMembers);

        /*Attaching listeners to ListView*/
        listFlatMembers.setOnItemClickListener((parent, view, position, id) -> {
            itemValue = (String) listFlatMembers.getItemAtPosition(position);
            selectedFamilyMemberUID = flatMembersUIDList.get(position);
            editFlatAdmin.setText(itemValue);
            flatMembersDialog.cancel();
        });
    }

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
     * This method gets invoked when user wants to update existing name and existing email and also
     * for updating profile photo as well as if flat admin value changes.
     */
    private void updateUserDetailsInFirebase() {
        String updatedUserName = editUserName.getText().toString();
        String updatedUserEmail = editUserEmail.getText().toString();
        String updatedAdmin = editFlatAdmin.getText().toString();
        boolean profilePicChanged = profilePhotoPath != null;

        Intent nammaApartmentsHomeIntent = new Intent(this, NammaApartmentsHome.class);
        nammaApartmentsHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (updatedUserName.equals(userName) &&
                updatedUserEmail.equals(userEmail) &&
                updatedAdmin.equals(adminName) &&
                !profilePicChanged) {
            showNotificationDialog(getString(R.string.update_message),
                    getString(R.string.no_update_message),
                    null);
        } else {
            DatabaseReference personalDetailsReference = PRIVATE_USERS_REFERENCE.child(currentNammaApartmentUser.getUID())
                    .child(FIREBASE_CHILD_PERSONALDETAILS);

            /*User Name has been changed*/
            if (!updatedUserName.equals(userName)) {
                currentNammaApartmentUser.getPersonalDetails().setFullName(updatedUserName);
                DatabaseReference updatedUserNameReference = personalDetailsReference
                        .child(FIREBASE_CHILD_FULLNAME);
                updatedUserNameReference.setValue(updatedUserName);
            }

            /*Email address has been changed*/
            if (!updatedUserEmail.equals(userEmail)) {
                currentNammaApartmentUser.getPersonalDetails().setEmail(updatedUserEmail);
                DatabaseReference updatedUserEmailReference = personalDetailsReference
                        .child(FIREBASE_CHILD_EMAIL);
                updatedUserEmailReference.setValue(updatedUserEmail);
            }

            /*Admin has been changed*/
            if (!updatedAdmin.equals(adminName)) {
                /*This method invokes if user wants to change admin to other flat members.*/
                changeAdmin();
            } else {
                if (!updatedUserName.equals(userName) || !updatedUserEmail.equals(userEmail)) {
                    showNotificationDialog(getString(R.string.update_message),
                            getString(R.string.update_success_message),
                            nammaApartmentsHomeIntent);
                }
            }

            /*Profile pic has been changed*/
            if (profilePicChanged) {
                showProgressDialog(UserProfile.this, getString(R.string.updating_profile), getString(R.string.please_wait_a_moment));

                String userUID = NammaApartmentsGlobal.userUID;
                StorageReference storageReference = FIREBASE_STORAGE.getReference(FIREBASE_CHILD_USERS)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(userUID);

                UploadTask uploadTask = storageReference.putBytes(getByteArrayFromFile(UserProfile.this, profilePhotoPath));
                /*adding the profile photo to storage reference*/
                uploadTask.addOnSuccessListener(taskSnapshot -> {

                    /*creating the upload object to store uploaded image details*/
                    String profilePhotoPath = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();
                    currentNammaApartmentUser.getPersonalDetails().setProfilePhoto(profilePhotoPath);

                    /*Update the new profile photo URL in firebase*/
                    DatabaseReference updatedUserPhotoReference = PRIVATE_USERS_REFERENCE.child(currentNammaApartmentUser.getUID())
                            .child(FIREBASE_CHILD_PERSONALDETAILS)
                            .child(FIREBASE_CHILD_PROFILE_PHOTO);
                    updatedUserPhotoReference.setValue(profilePhotoPath).addOnCompleteListener(task -> {
                        hideProgressDialog();
                        showNotificationDialog(getString(R.string.update_message),
                                getString(R.string.update_success_message),
                                nammaApartmentsHomeIntent);
                    });
                });
            }
        }
    }

    /**
     * This method gets invoked when user tries to change admin privileges to either user's family
     * member or friends.
     */
    private void changeAdmin() {
        /*Runnable Interface which gets invoked once user presses Yes button in Confirmation
         * Dialog */
        Intent loginIntent = new Intent(UserProfile.this, SignIn.class);
        Runnable updateAdminDetailsInFirebase = () ->
        {
            DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
            /*Current user will loose Admin Privileges so change admin to false*/
            DatabaseReference currentUserPrivateReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                    .child(FIREBASE_CHILD_PRIVILEGES)
                    .child(FIREBASE_ADMIN);
            currentUserPrivateReference.setValue(false);

            /*New User will get Admin Privileges so change admin to true*/
            DatabaseReference newUserPrivateReference = PRIVATE_USERS_REFERENCE.child(selectedFamilyMemberUID)
                    .child(FIREBASE_CHILD_PRIVILEGES)
                    .child(FIREBASE_ADMIN);
            newUserPrivateReference.setValue(true);

            /*Here,we need to update the admin UID under userdata->flatNumber->admin*/
            DatabaseReference adminUIDReference = userDataReference.child(FIREBASE_ADMIN);
            adminUIDReference.setValue(selectedFamilyMemberUID);

            showNotificationDialog(getString(R.string.update_message),
                    getString(R.string.update_success_message),
                    loginIntent);
        };
        String confirmDialogTitle = getResources().getString(R.string.non_admin_change_admin_title);
        String confirmDialogMessage = getResources().getString(R.string.admin_change_key);
        String confirmDialogMessageValue = confirmDialogMessage.replace(getString(R.string.person), itemValue);
        showConfirmDialog(confirmDialogTitle, confirmDialogMessageValue, updateAdminDetailsInFirebase);
    }



    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    private interface FirebaseCallback {
        void onCallback(List<String> list);
    }

}
