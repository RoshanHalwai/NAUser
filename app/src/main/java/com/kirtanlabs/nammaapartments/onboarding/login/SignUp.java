package com.kirtanlabs.nammaapartments.onboarding.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.flatdetails.MyFlatDetails;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.GALLERY_PERMISSION_REQUEST_CODE;

public class SignUp extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private CircleImageView circleImageNewUserProfileImage;
    private EditText editFullName;
    private EditText editEmailId;
    private TextView textErrorProfilePic;
    private AlertDialog imageSelectionDialog;
    private Uri selectedImage;

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
        buttonSignUp.setOnClickListener(this);
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PERMISSION_REQUEST_CODE:
                    selectedImage = data.getData();
                    if (data.getExtras() != null) {
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageNewUserProfileImage.setImageBitmap(bitmapProfilePic);
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
                            circleImageNewUserProfileImage.setImageBitmap(bitmapProfilePic);
                            imageSelectionDialog.cancel();
                            textErrorProfilePic.setVisibility(View.GONE);
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        imageSelectionDialog.cancel();
                    }
                    break;
            }

        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                if (selectedImage == null) {
                    textErrorProfilePic.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(this, MyFlatDetails.class);
                    intent.putExtra(Constants.FULL_NAME, editFullName.getText().toString());
                    intent.putExtra(Constants.EMAIL_ID, editEmailId.getText().toString());
                    intent.putExtra(Constants.MOBILE_NUMBER, getIntent().getStringExtra(Constants.MOBILE_NUMBER));
                    startActivity(intent);
                }
                break;
            case R.id.newUserProfileImage:
                imageSelectionDialog.show();
                break;
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
}
