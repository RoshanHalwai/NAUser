package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.Support;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServiceProblemList;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServicesHistory;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SUPPORT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SELECT_SOCIETY_SERVICE_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class ContactUs extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Button buttonSocietyServices;
    private Button buttonApartmentService;
    private Button buttonMiscellaneous;
    private EditText editServiceType;
    private EditText editProblemDesc;
    private TextView textErrorProblemDesc;
    private int serviceCategory;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.contact_us;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textServiceCategory = findViewById(R.id.textServiceCategory);
        TextView textServiceType = findViewById(R.id.textServiceType);
        TextView textProblemDesc = findViewById(R.id.textProblemDesc);
        textErrorProblemDesc = findViewById(R.id.textErrorProblemDesc);
        editServiceType = findViewById(R.id.editServiceType);
        editProblemDesc = findViewById(R.id.editProblemDesc);
        buttonSocietyServices = findViewById(R.id.buttonSocietyServices);
        buttonApartmentService = findViewById(R.id.buttonApartmentService);
        buttonMiscellaneous = findViewById(R.id.buttonMiscellaneous);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        /*Setting Fonts for all the views*/
        textServiceCategory.setTypeface(setLatoBoldFont(this));
        textServiceType.setTypeface(setLatoBoldFont(this));
        textProblemDesc.setTypeface(setLatoBoldFont(this));
        textErrorProblemDesc.setTypeface(setLatoBoldFont(this));
        editServiceType.setTypeface(setLatoRegularFont(this));
        editProblemDesc.setTypeface(setLatoRegularFont(this));
        buttonSocietyServices.setTypeface(setLatoRegularFont(this));
        buttonApartmentService.setTypeface(setLatoRegularFont(this));
        buttonMiscellaneous.setTypeface(setLatoRegularFont(this));
        buttonSubmit.setTypeface(setLatoLightFont(this));

        /*We don't want the keyboard to be displayed when user clicks on the choose one edit text*/
        editServiceType.setInputType(InputType.TYPE_NULL);

        /*Since we have History button here, we would want to users to navigate to Contact Us history
         * and display data based on the user input*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting Click Listeners for UI views*/
        editServiceType.setOnClickListener(this);
        editProblemDesc.setOnClickListener(this);
        buttonSocietyServices.setOnClickListener(this);
        buttonApartmentService.setOnClickListener(this);
        buttonMiscellaneous.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        historyButton.setOnClickListener(this);

        /*Initializing service category*/
        serviceCategory = R.string.society_services;
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editServiceType:
                Intent intent = new Intent(ContactUs.this, SocietyServiceProblemList.class);
                intent.putExtra(Constants.SCREEN_TITLE, serviceCategory);
                startActivityForResult(intent, SELECT_SOCIETY_SERVICE_REQUEST_CODE);
                break;
            case R.id.editProblemDesc:
                /*Hiding the Error Message when user tries to enter description*/
                textErrorProblemDesc.setVisibility(View.GONE);
                break;
            case R.id.buttonSocietyServices:
                if (serviceCategory != R.string.society_services) {
                    serviceCategory = R.string.society_services;
                    editServiceType.getText().clear();
                    editProblemDesc.getText().clear();
                    buttonApartmentService.setBackgroundResource(R.drawable.valid_for_button_design);
                    buttonSocietyServices.setBackgroundResource(R.drawable.selected_button_design);
                    buttonMiscellaneous.setBackgroundResource(R.drawable.valid_for_button_design);
                }
                break;
            case R.id.buttonApartmentService:
                if (serviceCategory != R.string.apartment_services) {
                    serviceCategory = R.string.apartment_services;
                    editServiceType.getText().clear();
                    editProblemDesc.getText().clear();
                    buttonSocietyServices.setBackgroundResource(R.drawable.valid_for_button_design);
                    buttonApartmentService.setBackgroundResource(R.drawable.selected_button_design);
                    buttonMiscellaneous.setBackgroundResource(R.drawable.valid_for_button_design);
                }
                break;
            case R.id.buttonMiscellaneous:
                if (serviceCategory != R.string.miscellaneous) {
                    serviceCategory = R.string.miscellaneous;
                    editServiceType.getText().clear();
                    editProblemDesc.getText().clear();
                    buttonMiscellaneous.setBackgroundResource(R.drawable.selected_button_design);
                    buttonSocietyServices.setBackgroundResource(R.drawable.valid_for_button_design);
                    buttonApartmentService.setBackgroundResource(R.drawable.valid_for_button_design);
                }
                break;
            case R.id.buttonSubmit:
                submitRequest();
                break;
            case R.id.historyButton:
                Intent societyServiceHistoryIntent = new Intent(ContactUs.this, SocietyServicesHistory.class);
                societyServiceHistoryIntent.putExtra(SCREEN_TITLE, getString(R.string.contact_us));
                startActivity(societyServiceHistoryIntent);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding onActivityResult Method
     * ------------------------------------------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_SOCIETY_SERVICE_REQUEST_CODE) {
            editServiceType.setText(data.getStringExtra(Constants.SOCIETY_SERVICE_PROBLEM));
            editServiceType.setError(null);
            editProblemDesc.requestFocus();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Submits user problem to firebase so admin can take necessary actions
     */
    private void submitRequest() {
        if (isUIValidationSuccessful()) {
            showProgressDialog(ContactUs.this, getString(R.string.raising_request), getString(R.string.please_wait_a_moment));
            DatabaseReference supportReference = Constants.SUPPORT_REFERENCE;
            DatabaseReference userSupportReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference().child(FIREBASE_CHILD_SUPPORT);
            String supportUID = userSupportReference.push().getKey();
            supportReference.child(supportUID).setValue(getProblemDetails(supportUID))
                    .addOnCompleteListener(task -> userSupportReference.child(supportUID).setValue(true)
                            .addOnCompleteListener(task1 -> {
                                hideProgressDialog();
                                /*Clear text of service type and problem field so that the user doesn't get to see the old
                                 * entry while creating a new one*/
                                editServiceType.setText("");
                                editProblemDesc.setText("");
                                Intent societyServiceHistoryIntent = new Intent(this, SocietyServicesHistory.class);
                                societyServiceHistoryIntent.putExtra(SCREEN_TITLE, getString(R.string.contact_us));
                                societyServiceHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                societyServiceHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                showNotificationDialog(getString(R.string.request_raised),
                                        getString(R.string.user_support_message),
                                        societyServiceHistoryIntent);
                            }));
        }
    }

    /**
     * Performs UI validation and returns results accordingly
     *
     * @return true if validation is successful else returns false
     */
    private boolean isUIValidationSuccessful() {
        if (!editServiceType.getText().toString().isEmpty() && !editProblemDesc.getText().toString().isEmpty())
            return true;
        else if (editServiceType.getText().toString().isEmpty())
            editServiceType.setError(getString(R.string.enter_service_type));
        else
            textErrorProblemDesc.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Gets problem details entered by the user
     *
     * @param supportUID unique string to identify user problem
     * @return instance of Support.class
     */
    private Support getProblemDetails(String supportUID) {
        String serviceCategory = getString(this.serviceCategory);
        String serviceType = editServiceType.getText().toString();
        String problemDesc = editProblemDesc.getText().toString();
        return new Support(problemDesc, serviceCategory, serviceType, getString(R.string.pending),
                System.currentTimeMillis(), supportUID, NammaApartmentsGlobal.userUID);
    }

}
