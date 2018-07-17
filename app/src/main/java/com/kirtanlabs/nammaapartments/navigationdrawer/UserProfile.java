package com.kirtanlabs.nammaapartments.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.ImagePicker;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_EMAIL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.ImagePicker.bitmapToByteArray;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private de.hdodenhof.circleimageview.CircleImageView currentUserProfilePic;
    private EditText editUserName;
    private EditText editUserEmail;
    private EditText editFlatAdmin;
    private String userName, userEmail, admin;
    private AlertDialog imageSelectionDialog;
    private Dialog flatMembersDialog;
    private List<String> flatMembersList = new ArrayList<>();
    private byte[] profilePhotoByteArray;
    private int index = 0;

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

        /*We would want to have all family member names well before user clicks on Change Admin Button*/
        fillFlatMemberNames();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        TextView textName = findViewById(R.id.textName);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textFlatAdmin = findViewById(R.id.textFlatAdmin);
        editUserName = findViewById(R.id.editUserName);
        editUserEmail = findViewById(R.id.editUserEmail);
        editFlatAdmin = findViewById(R.id.editFlatAdmin);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);

        /*Setting font for all the views*/
        textName.setTypeface(setLatoBoldFont(this));
        textEmail.setTypeface(setLatoBoldFont(this));
        textFlatAdmin.setTypeface(setLatoBoldFont(this));
        editUserEmail.setTypeface(setLatoRegularFont(this));
        editUserName.setTypeface(setLatoRegularFont(this));
        editFlatAdmin.setTypeface(setLatoRegularFont(this));
        buttonUpdate.setTypeface(setLatoLightFont(this));

        /*This method is for retrieval of currentUser details and gets pre-filled in EditTexts and
          image gets pre-loaded in circularImageView.*/
        retrieveUserDetails();

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
                case CAMERA_PERMISSION_REQUEST_CODE:
                case GALLERY_PERMISSION_REQUEST_CODE:
                    Bitmap bitmapProfilePic = ImagePicker.getImageFromResult(this, resultCode, data);
                    currentUserProfilePic.setImageBitmap(bitmapProfilePic);
                    currentUserProfilePic.setTag(R.id.currentUserProfilePic, "Updated Image");
                    profilePhotoByteArray = bitmapToByteArray(bitmapProfilePic);
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
            case R.id.editFlatAdmin:
                if (flatMembersList.isEmpty()) {
                    showNotificationDialog(getString(R.string.change_admin), getString(R.string.no_flat_members), null);
                } else {
                    flatMembersDialog.show();
                }
                break;
            case R.id.buttonUpdate:
                updateUserDetailsInFirebase();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Fills the family members related to that particular user.
     */
    private void fillFlatMemberNames() {
        familyMemberList(list -> friendsMemberList(list1 -> createFlatMembersDialog()));
    }

    private void familyMemberList(FirebaseCallback callback) {
        Map<String, Boolean> familyMembers = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getFamilyMembers();
        if (!familyMembers.isEmpty()) {
            for (String userUID : familyMembers.keySet()) {
                DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(userUID)
                        .child(FIREBASE_CHILD_PERSONALDETAILS)
                        .child(FIREBASE_CHILD_FULLNAME);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String flatMemberName = dataSnapshot.getValue(String.class);
                        flatMembersList.add(index, flatMemberName);
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
            for (String userUID : friends.keySet()) {
                DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(userUID)
                        .child(FIREBASE_CHILD_PERSONALDETAILS)
                        .child(FIREBASE_CHILD_FULLNAME);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String flatMemberName = dataSnapshot.getValue(String.class);
                        flatMembersList.add(index, flatMemberName);
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
     * This method gets invoked when user tries to change admin by clicking on change admin button.
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
            String itemValue = (String) listFlatMembers.getItemAtPosition(position);
            String confirmDialogTitle = getResources().getString(R.string.non_admin_change_admin_title);
            String confirmDialogMessage = getResources().getString(R.string.admin_change_key);
            String confirmDialogMessageValue = confirmDialogMessage.replace(getString(R.string.person), itemValue);
            showConfirmDialog(confirmDialogTitle, confirmDialogMessageValue, () -> {
                editFlatAdmin.setText(itemValue);

            });
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
     * This method gets invoked to pre-fill the details of existing user name and email id.
     */
    private void retrieveUserDetails() {
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        userName = currentNammaApartmentUser.getPersonalDetails().getFullName();
        userEmail = currentNammaApartmentUser.getPersonalDetails().getEmail();
        editUserName.setText(userName);
        editUserEmail.setText(userEmail);
        Glide.with(this.getApplicationContext()).load(currentNammaApartmentUser.getPersonalDetails().getProfilePhoto()).into(currentUserProfilePic);
        currentUserProfilePic.setTag(R.id.currentUserProfilePic, "Actual Image");

        /*Based on the admin privileges we are hiding change admin button.*/
        if (currentNammaApartmentUser.getPrivileges().isAdmin()) {
            admin = "You are the Administrator";
            editFlatAdmin.setText(admin);
        } else {
            /*We should not allow the other flat members to change the Administrator details*/
            editFlatAdmin.setEnabled(false);
            editFlatAdmin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            /*TODO: Get the name of the Admin*/
        }

    }

    /**
     * This method gets invoked when user wants to update existing name and existing email.
     */
    private void updateUserDetailsInFirebase() {
        String updatedUserName = editUserName.getText().toString();
        String updatedUserEmail = editUserEmail.getText().toString();
        String updatedAdmin = editFlatAdmin.getText().toString();
        boolean profilePicChanged = !currentUserProfilePic.getTag(R.id.currentUserProfilePic).equals("Actual Image");

        Intent nammaApartmentsHome = new Intent(this, NammaApartmentsHome.class);
        if (updatedUserName.equals(userName) &&
                updatedUserEmail.equals(userEmail) &&
                updatedAdmin.equals(admin) &&
                !profilePicChanged) {

            /*TODO: Give proper message since there has been no changes in the view*/
            showNotificationDialog(getString(R.string.update_message),
                    getString(R.string.update_success_message),
                    nammaApartmentsHome);
        } else {
            NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
            DatabaseReference personalDetailsReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentUser.getUID())
                    .child(FIREBASE_CHILD_PERSONALDETAILS);

            /*User Name has been changed*/
            if (!updatedUserName.equals(userName)) {
                nammaApartmentUser.getPersonalDetails().setFullName(updatedUserName);
                DatabaseReference updatedUserNameReference = personalDetailsReference
                        .child(FIREBASE_CHILD_FULLNAME);
                updatedUserNameReference.setValue(updatedUserName);
            }

            /*Email address has been changed*/
            if (!updatedUserEmail.equals(userEmail)) {
                nammaApartmentUser.getPersonalDetails().setEmail(updatedUserEmail);
                DatabaseReference updatedUserEmailReference = personalDetailsReference
                        .child(FIREBASE_CHILD_EMAIL);
                updatedUserEmailReference.setValue(updatedUserEmail);
            }

            /*Admin has been changed*/
            /*TODO: Update admin UID under userData in Firebase*/

            /*Profile pic has been changed*/
            if (profilePicChanged) {
                showProgressDialog(UserProfile.this, getString(R.string.updating_profile), getString(R.string.please_wait_a_moment));

                String userUID = NammaApartmentsGlobal.userUID;
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(FIREBASE_CHILD_USERS)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(userUID);

                UploadTask uploadTask = storageReference.putBytes(profilePhotoByteArray);
                //adding the profile photo to storage reference
                uploadTask.addOnSuccessListener(taskSnapshot -> {

                    //creating the upload object to store uploaded image details
                    String profilePhotoPath = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();
                    nammaApartmentUser.getPersonalDetails().setProfilePhoto(profilePhotoPath);

                    //Update the new profile photo URL in firebase
                    DatabaseReference updatedUserPhotoReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentUser.getUID())
                            .child(FIREBASE_CHILD_PERSONALDETAILS)
                            .child(FIREBASE_CHILD_PROFILE_PHOTO);
                    updatedUserPhotoReference.setValue(profilePhotoPath).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            showNotificationDialog(getString(R.string.update_message),
                                    getString(R.string.update_success_message),
                                    nammaApartmentsHome);
                        }
                    });
                });
            } else {
                showNotificationDialog(getString(R.string.update_message),
                        getString(R.string.update_success_message),
                        nammaApartmentsHome);
            }
        }
    }

    private interface FirebaseCallback {
        void onCallback(List<String> list);
    }

}
