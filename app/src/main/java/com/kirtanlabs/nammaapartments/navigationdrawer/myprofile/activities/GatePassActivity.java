package com.kirtanlabs.nammaapartments.navigationdrawer.myprofile.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;

public class GatePassActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textSocietyValue, textUserNameValue, textApartmentValue, textFlatValue;
    private CircleImageView currentUserProfilePic;
    private NammaApartmentUser currentNammaApartmentUser;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_gate_pass;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.gate_pass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Here we are getting the instance of current user when screen loads for the first time*/
        currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        textSocietyValue = findViewById(R.id.textSocietyValue);
        textUserNameValue = findViewById(R.id.textUserNameValue);
        textApartmentValue = findViewById(R.id.textApartmentValue);
        textFlatValue = findViewById(R.id.textFlatValue);
        TextView textWelcomeMessage = findViewById(R.id.textWelcomeMessage);
        Button buttonDownloadGatePass = findViewById(R.id.buttonDownloadGatePass);

        /*Setting font for all the views*/
        textSocietyValue.setTypeface(setLatoBoldFont(this));
        textUserNameValue.setTypeface(setLatoBoldFont(this));
        buttonDownloadGatePass.setTypeface(setLatoLightFont(this));
        textWelcomeMessage.setTypeface(setLatoItalicFont(this));

        /*Called when user presses on 'Download Gate Pass' button in 'My Profile' screen*/
        retrieveUserDetails();

        /*Setting onClick Listener*/
        buttonDownloadGatePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*Capturing screenshot of Gate Pass of the User on click of 'Download Gate Pass' button*/
        captureGatePass();
        /*At the same time, notifying the user that the Gate Pass has been downloaded*/
        showNotificationDialog(getString(R.string.download_title), getString(R.string.download_text), null);
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve all the details of the current user and displays it in the Gate Pass screen
     */
    private void retrieveUserDetails() {
        /*Getting values of all the required fields*/
        String userName = currentNammaApartmentUser.getPersonalDetails().getFullName();
        String societyName = currentNammaApartmentUser.getFlatDetails().getSocietyName();
        String apartmentName = currentNammaApartmentUser.getFlatDetails().getApartmentName();
        String flatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();

        /*Setting values of all text views*/
        textUserNameValue.setText(userName);
        textSocietyValue.setText(societyName);
        textApartmentValue.setText(apartmentName);
        textFlatValue.setText(flatNumber);
        Glide.with(this.getApplicationContext()).load(currentNammaApartmentUser.getPersonalDetails().getProfilePhoto()).into(currentUserProfilePic);
        currentUserProfilePic.setTag(R.id.currentUserProfilePic, "Actual Image");
    }

    /**
     * This method is invoked when the User presses on 'Download' button in the Gate Pass screen.
     * Gate Pass is captured and stored in the User's device.
     */
    private void captureGatePass() {
        /*Trimming the view so that the captured part only contains the Card View*/
        View v1 = getWindow().getDecorView().getRootView().findViewById(R.id.viewDownloadGatePass);
        v1.setDrawingCacheEnabled(true);
        /*Setting bitmap*/
        Bitmap b = v1.getDrawingCache();
        /*Image naming and path*/
        String storagePath = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(storagePath, "Namma Apartments" + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
