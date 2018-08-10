package com.kirtanlabs.nammaapartments.onboarding.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.flatdetails.MyFlatDetails;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.kirtanlabs.nammaapartments.utilities.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.ImagePicker.getBitmapFromFile;

public class SignUp extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private CircleImageView circleImageNewUserProfileImage;
    private TextView textErrorProfilePic;
    private EditText editFullName;
    private EditText editEmailId;
    private AlertDialog imageSelectionDialog;
    private File profilePhotoPath;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.sign_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBackButton();

        /*Custom DialogBox with list of all image services*/
        createImageSelectionDialog();

        /*Getting Id's for all the views*/
        circleImageNewUserProfileImage = findViewById(R.id.newUserProfileImage);
        textErrorProfilePic = findViewById(R.id.textErrorProfilePic);
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textEmailId = findViewById(R.id.textEmailId);
        TextView textTermsAndConditions = findViewById(R.id.textTermsAndConditions);
        editFullName = findViewById(R.id.editFullName);
        editEmailId = findViewById(R.id.editEmailId);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        /*Setting font for all the views*/
        textErrorProfilePic.setTypeface(Constants.setLatoRegularFont(this));
        textFullName.setTypeface(Constants.setLatoBoldFont(this));
        textEmailId.setTypeface(Constants.setLatoBoldFont(this));
        textTermsAndConditions.setTypeface(Constants.setLatoRegularFont(this));
        editFullName.setTypeface(Constants.setLatoRegularFont(this));
        editEmailId.setTypeface(Constants.setLatoRegularFont(this));
        buttonSignUp.setTypeface(Constants.setLatoLightFont(this));
        /*Setting event for  button*/
        circleImageNewUserProfileImage.setOnClickListener(this);
        circleImageNewUserProfileImage.setOnFocusChangeListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //This condition checks for the resultCode value since if any photo got selected/captured
        //either from camera and gallery its value will be equal to -1 and vice-versa its value is 0.
        //So here if we checks if user has not selected any photo from gallery or he did'nt captured
        //any photo its resultCode value will be equal to zero and we are displaying circular image view.
        if (resultCode == EDIT_TEXT_EMPTY_LENGTH) {
            circleImageNewUserProfileImage.setVisibility(View.VISIBLE);
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                    if (source == EasyImage.ImageSource.GALLERY || source == EasyImage.ImageSource.CAMERA) {
                        circleImageNewUserProfileImage.setImageBitmap(getBitmapFromFile(SignUp.this, imageFile));
                        profilePhotoPath = imageFile;
                    }
                }
            });
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick & OnFocus Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                //This condition checks for the string value if its null it will show appropriate
                // error message.
                if (profilePhotoPath == null) {
                    textErrorProfilePic.setVisibility(View.VISIBLE);
                    break;
                }
                //This method gets invoked to check all the validation fields such as editTexts name and email.
                validateFields();
                break;
            case R.id.newUserProfileImage:
                hideKeyboard();
                imageSelectionDialog.show();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == circleImageNewUserProfileImage && hasFocus) {
            onClick(v);
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
     * This method gets invoked to check all the validation fields such as editTexts name and email.
     */
    private void validateFields() {
        String newUserName = editFullName.getText().toString().trim();
        String newUserEmail = editEmailId.getText().toString().trim();
        boolean fieldsFilled = isAllFieldsFilled(new EditText[]{editFullName, editEmailId});
        //This condition checks if any field is empty and then display proper error messages according
        //to the requirement.
        if (!fieldsFilled) {
            if (TextUtils.isEmpty(newUserName)) {
                editFullName.setError(getString(R.string.name_validation));
            }
            if (TextUtils.isEmpty(newUserEmail)) {
                editEmailId.setError(getString(R.string.email_validation));
            }
        }
        //This condition checks if any of the fields are filled and validates email and mobile number.
        if (fieldsFilled) {
            if (isValidEmail(newUserEmail)) {
                editEmailId.setError(getString(R.string.invalid_email));
            }
            if (isValidPersonName(newUserName)) {
                editFullName.setError(getString(R.string.accept_alphabets));
            }
        }
        //This condition checks if name and email are properly validated and then
        // navigate to proper screen according to its requirement.
        if (!isValidEmail(newUserEmail) && !isValidPersonName(newUserName)) {
            Intent intent = new Intent(this, MyFlatDetails.class);
            intent.putExtra(Constants.FULL_NAME, editFullName.getText().toString());
            intent.putExtra(Constants.EMAIL_ID, editEmailId.getText().toString());
            intent.putExtra(Constants.MOBILE_NUMBER, getIntent().getStringExtra(Constants.MOBILE_NUMBER));
            intent.putExtra(Constants.PROFILE_PHOTO, profilePhotoPath.toString());
            startActivity(intent);
            finish();
        }
    }
}
