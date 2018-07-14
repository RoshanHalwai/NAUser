package com.kirtanlabs.nammaapartments.nammaapartmentshome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_EMAIL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FULLNAME;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PERSONALDETAILS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USERS;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.ImagePicker.bitmapToByteArray;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private de.hdodenhof.circleimageview.CircleImageView currentUserProfilePic;
    private EditText editCurrentUserName, editCurrentUserEmail;
    private Button buttonUpdate;
    private AlertDialog imageSelectionDialog;
    private Dialog dialog;
    private ArrayAdapter<String> adapterFlatMembers;
    private boolean isCurrentUserNameChanged;
    private List<String> itemsInList = new ArrayList<>();
    private boolean isCurrentUserEmailChanged;
    private boolean isCurrentUserPhotoChanged;
    private byte[] profilePhotoByteArray;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout__user_profile;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        TextView textName = findViewById(R.id.textName);
        TextView textEmail = findViewById(R.id.textEmail);
        editCurrentUserName = findViewById(R.id.editCurrentUserName);
        editCurrentUserEmail = findViewById(R.id.editCurrentUserEmail);
        Button buttonChangeAdmin = findViewById(R.id.buttonChangeAdmin);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        /*Setting font for all the views*/
        textName.setTypeface(setLatoBoldFont(this));
        textEmail.setTypeface(setLatoBoldFont(this));
        editCurrentUserEmail.setTypeface(setLatoRegularFont(this));
        editCurrentUserName.setTypeface(setLatoRegularFont(this));
        buttonChangeAdmin.setTypeface(setLatoLightFont(this));
        buttonUpdate.setTypeface(setLatoLightFont(this));

        /*This method is for retrieval of currentUser details and gets pre-filled in EditTexts and
          image gets pre-loaded in circularImageView.*/
        retrieveUserDetails();

        //Based on the admin privileges we are hiding change admin button.
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        boolean isAdmin = currentNammaApartmentUser.getPrivileges().isAdmin();
        if (!isAdmin) {
            buttonChangeAdmin.setVisibility(View.GONE);
        }

        /*Setting event for all button clicks */
        currentUserProfilePic.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonChangeAdmin.setOnClickListener(this);

        /*This method checks for EditText changes*/
        setEventsForEditText();
    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult
     * ------------------------------------------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            isCurrentUserPhotoChanged = true;
            makeViewsVisibleInvisible();
            showProgressIndicator();
            switch (requestCode) {
                case CAMERA_PERMISSION_REQUEST_CODE:
                case GALLERY_PERMISSION_REQUEST_CODE:
                    Bitmap bitmapProfilePic = ImagePicker.getImageFromResult(this, resultCode, data);
                    currentUserProfilePic.setImageBitmap(bitmapProfilePic);
                    profilePhotoByteArray = bitmapToByteArray(bitmapProfilePic);
                    hideProgressIndicator();
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
            case R.id.buttonUpdate:
                /*This method will update user details in Firebase.*/
                updateUserDetailsInFirebase();
                break;
            case R.id.buttonChangeAdmin:
                //This dialog shows list of family members in that particular flat.
                openFlatMembersDialog();
                dialog.show();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked to pre-fill the details of existing user name and email id.
     */
    private void retrieveUserDetails() {
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        editCurrentUserName.setText(currentNammaApartmentUser.getPersonalDetails().getFullName());
        editCurrentUserEmail.setText(currentNammaApartmentUser.getPersonalDetails().getEmail());
        Glide.with(this.getApplicationContext()).load(currentNammaApartmentUser.getPersonalDetails().getProfilePhoto()).into(currentUserProfilePic);
    }

    /**
     * This method gets invoked when user wants to update existing name and existing email.
     */
    private void updateUserDetailsInFirebase() {
        if (isCurrentUserNameChanged || isCurrentUserEmailChanged || isCurrentUserPhotoChanged) {
            showProgressDialog(this,
                    getResources().getString(R.string.updating_profile),
                    getResources().getString(R.string.please_wait_a_moment));
        }
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        if (isCurrentUserNameChanged) {
            String updatedUserName = editCurrentUserName.getText().toString();
            nammaApartmentUser.getPersonalDetails().setFullName(updatedUserName);
            DatabaseReference updatedUserNameReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentUser.getUID())
                    .child(FIREBASE_CHILD_PERSONALDETAILS)
                    .child(FIREBASE_CHILD_FULLNAME);
            updatedUserNameReference.setValue(updatedUserName);

            //Notify users that they have successfully updated their details.
            Intent naHomeIntent = new Intent(UserProfile.this, NammaApartmentsHome.class);
            naHomeIntent.putExtra(SCREEN_TITLE, getClass().toString());
            showNotificationDialog(getResources().getString(R.string.update_message),
                    getResources().getString(R.string.update_success_message),
                    naHomeIntent);
        }
        if (isCurrentUserEmailChanged) {
            String updatedUserEmail = editCurrentUserEmail.getText().toString();
            nammaApartmentUser.getPersonalDetails().setEmail(updatedUserEmail);
            DatabaseReference updatedUserEmailReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentUser.getUID())
                    .child(FIREBASE_CHILD_PERSONALDETAILS)
                    .child(FIREBASE_CHILD_EMAIL);
            updatedUserEmailReference.setValue(updatedUserEmail);

            //Notify users that they have successfully updated their details.
            Intent naHomeIntent = new Intent(UserProfile.this, NammaApartmentsHome.class);
            naHomeIntent.putExtra(SCREEN_TITLE, getClass().toString());
            showNotificationDialog(getResources().getString(R.string.update_message),
                    getResources().getString(R.string.update_success_message),
                    naHomeIntent);
        }
        if (isCurrentUserPhotoChanged) {
            //getting the storage reference
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
                updatedUserPhotoReference.setValue(profilePhotoPath);

                //dismissing the progress dialog
                hideProgressDialog();

                //Notify users that they have successfully updated their details.
                Intent naHomeIntent = new Intent(UserProfile.this, NammaApartmentsHome.class);
                naHomeIntent.putExtra(SCREEN_TITLE, getClass().toString());
                showNotificationDialog(getResources().getString(R.string.update_message),
                        getResources().getString(R.string.update_success_message),
                        naHomeIntent);
            }).addOnFailureListener(exception -> {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

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
     * This method gets invoked when user is trying to modify their existing name and email id.
     */
    private void makeViewsVisibleInvisible() {
        if (isCurrentUserNameChanged || isCurrentUserEmailChanged || isCurrentUserPhotoChanged) {
            buttonUpdate.setVisibility(View.VISIBLE);
        } else {
            buttonUpdate.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method gets invoked when there is any change in editTexts name and email id.
     */
    private void setEventsForEditText() {
        String currentUserName = editCurrentUserName.getText().toString().trim();
        String currentUserEmail = editCurrentUserEmail.getText().toString().trim();
        editCurrentUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isCurrentUserNameChanged = editCurrentUserName.length() != EDIT_TEXT_EMPTY_LENGTH;
                makeViewsVisibleInvisible();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (currentUserName.contentEquals(editable)) {
                    isCurrentUserNameChanged = false;
                    makeViewsVisibleInvisible();
                }
            }
        });
        editCurrentUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isCurrentUserEmailChanged = editCurrentUserEmail.length() != EDIT_TEXT_EMPTY_LENGTH;
                makeViewsVisibleInvisible();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (currentUserEmail.contentEquals(editable)) {
                    isCurrentUserEmailChanged = false;
                    makeViewsVisibleInvisible();
                }
            }
        });
    }

    /**
     * This method gets invoked when user tries to change admin by clicking on change admin button.
     */
    private void openFlatMembersDialog() {
        dialog = new Dialog(UserProfile.this);
        dialog.setContentView(R.layout.layout_search_flat);
        TextView textFlatMembers = dialog.findViewById(R.id.textFlatMembers);
        ListView listFlatMembers = dialog.findViewById(R.id.list);
        EditText inputSearch = dialog.findViewById(R.id.inputSearch);
        inputSearch.setVisibility(View.GONE);
        textFlatMembers.setVisibility(View.VISIBLE);
        textFlatMembers.setTypeface(Constants.setLatoBoldFont(UserProfile.this));
        adapterFlatMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsInList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Constants.setLatoRegularFont(UserProfile.this));
                return view;
            }
        };
        itemsInList.clear();
        listFlatMembers.setAdapter(adapterFlatMembers);
        /*This method we call for retrieving list of family members*/
        retrieveFlatMemberDetails();
        /*Attaching listeners to ListView*/
        listFlatMembers.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) listFlatMembers.getItemAtPosition(position);
            String confirmDialogTitle = getResources().getString(R.string.non_admin_change_admin_title);
            String confirmDialogMessage = getResources().getString(R.string.admin_change_key);
            String confirmDialogMessageValue = confirmDialogMessage.replace(getString(R.string.person), itemValue);
            //TODO:Create a runnable method that will perform necessary action on click of Ok button.
            showConfirmDialog(confirmDialogTitle, confirmDialogMessageValue, null);
            dialog.cancel();
        });
    }

    /**
     * This method gets invoked when user presses on click of change admin a list should display of
     * the family members related to that particular user.
     */
    private void retrieveFlatMemberDetails() {
        DatabaseReference privateFlatReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
        privateFlatReference.child(FIREBASE_CHILD_FLAT_MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot flatSnapshot : dataSnapshot.getChildren()) {
                    if (!flatSnapshot.getKey().equals(NammaApartmentsGlobal.userUID)) {
                        DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(flatSnapshot.getKey())
                                .child(FIREBASE_CHILD_PERSONALDETAILS)
                                .child(FIREBASE_CHILD_FULLNAME);
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String flatMemberName = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                itemsInList.add(index, flatMemberName);
                                adapterFlatMembers.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
