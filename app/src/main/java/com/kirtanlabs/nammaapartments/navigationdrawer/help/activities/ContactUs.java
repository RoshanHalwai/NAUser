package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.Support;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServiceProblemList;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SELECT_SOCIETY_SERVICE_REQUEST_CODE;

public class ContactUs extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Button buttonSocietyServices;
    private Button buttonApartmentService;
    private EditText editServiceType;
    private EditText editProblemDesc;
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
        editServiceType = findViewById(R.id.editServiceType);
        editProblemDesc = findViewById(R.id.editProblemDesc);
        buttonSocietyServices = findViewById(R.id.buttonSocietyServices);
        buttonApartmentService = findViewById(R.id.buttonApartmentService);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        /*Setting Fonts for all the views*/
        textServiceCategory.setTypeface(Constants.setLatoBoldFont(this));
        textServiceType.setTypeface(Constants.setLatoBoldFont(this));
        textProblemDesc.setTypeface(Constants.setLatoBoldFont(this));
        editServiceType.setTypeface(Constants.setLatoRegularFont(this));
        editProblemDesc.setTypeface(Constants.setLatoRegularFont(this));
        buttonSocietyServices.setTypeface(Constants.setLatoRegularFont(this));
        buttonApartmentService.setTypeface(Constants.setLatoRegularFont(this));
        buttonSubmit.setTypeface(Constants.setLatoLightFont(this));

        /*Setting Click Listeners for UI views*/
        editServiceType.setOnClickListener(this);
        buttonSocietyServices.setOnClickListener(this);
        buttonApartmentService.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

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
            case R.id.buttonSocietyServices:
                if (serviceCategory != R.string.society_services) {
                    serviceCategory = R.string.society_services;
                    editServiceType.getText().clear();
                    buttonApartmentService.setBackgroundResource(R.drawable.valid_for_button_design);
                    buttonSocietyServices.setBackgroundResource(R.drawable.selected_button_design);
                }
                break;
            case R.id.buttonApartmentService:
                if (serviceCategory != R.string.apartment_services) {
                    serviceCategory = R.string.apartment_services;
                    editServiceType.getText().clear();
                    buttonSocietyServices.setBackgroundResource(R.drawable.valid_for_button_design);
                    buttonApartmentService.setBackgroundResource(R.drawable.selected_button_design);
                }
                break;
            case R.id.buttonSubmit:
                submitRequest();
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
            showProgressDialog(ContactUs.this, "Raising Request", getString(R.string.please_wait_a_moment));
            DatabaseReference supportReference = FirebaseDatabase.getInstance().getReference("support");
            DatabaseReference userSupportReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference().child("support");
            String supportUID = userSupportReference.push().getKey();
            supportReference.child(supportUID).setValue(getProblemDetails(supportUID))
                    .addOnCompleteListener(task -> userSupportReference.child(supportUID).setValue(true)
                            .addOnCompleteListener(task1 -> {
                                hideProgressDialog();
                                showNotificationDialog("Request Raised",
                                        "Thank you for writing to us. Our Support team will get back to you regarding this request.",
                                        null);
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
            Toast.makeText(this, "Please select service type", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Please enter problem description", Toast.LENGTH_LONG).show();
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
        return new Support(problemDesc, serviceCategory, serviceType, "Pending",
                System.currentTimeMillis(), supportUID, NammaApartmentsGlobal.userUID);
    }

}
