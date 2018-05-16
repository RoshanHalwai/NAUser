package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDailyService extends BaseActivity {

    private final int RESULT_PICK_CONTACT = 1;
    private final int CAMERA_REQUEST = 2;
    private final int GALLERY_REQUEST = 3;
    private TextView textDescription;
    private EditText editPickTime;
    private EditText editName;
    private EditText editMobile;
    private Button buttonAdd;
    private CircleImageView circleImageView;
    private String selectedTime;
    private String service_type;
    private AlertDialog dialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_daily_service;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_my_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all image services*/
        AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
        View listAddImageServices = View.inflate(this, R.layout.list_add_image_services, null);
        addImageDialog.setView(listAddImageServices);
        dialog = addImageDialog.create();

        /*Getting Id's for all the views*/
        TextView textName = findViewById(R.id.textName);
        TextView textMobile = findViewById(R.id.textMobile);
        TextView textOr = findViewById(R.id.textOr);
        TextView textTime = findViewById(R.id.textTime);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        textDescription = findViewById(R.id.textDescription);
        editName = findViewById(R.id.editName);
        editMobile = findViewById(R.id.editMobileNumber);
        editPickTime = findViewById(R.id.editPickTime);
        Button buttonSelectFromContact = findViewById(R.id.buttonSelectFromContact);
        buttonAdd = findViewById(R.id.buttonAdd);
        circleImageView = findViewById(R.id.profileImage);
        ListView listView = listAddImageServices.findViewById(R.id.listAddImageService);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textName.setTypeface(Constants.setLatoBoldFont(this));
        textMobile.setTypeface(Constants.setLatoBoldFont(this));
        textOr.setTypeface(Constants.setLatoBoldFont(this));
        textTime.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        editPickTime.setTypeface(Constants.setLatoRegularFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editName.setTypeface(Constants.setLatoRegularFont(this));
        editMobile.setTypeface(Constants.setLatoRegularFont(this));
        buttonSelectFromContact.setTypeface(Constants.setLatoLightFont(this));
        buttonAdd.setTypeface(Constants.setLatoLightFont(this));

        /*Creating an array list of selecting images from camera and gallery*/
        ArrayList<String> pickImageList = new ArrayList<>();

        /*Adding pick images services to the list*/
        pickImageList.add(getString(R.string.camera));
        pickImageList.add(getString(R.string.gallery));
        pickImageList.add(getString(R.string.cancel));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDailyService.this, android.R.layout.simple_list_item_1, pickImageList);

        /*Attaching adapter to the listView*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*Setting event for circleImageView*/
        circleImageView.setOnClickListener(v -> dialog.show());

        /*Setting event for listview items*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    break;
                case 1:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
                    break;
                case 2:
                    dialog.cancel();
            }
        });

        /*Setting event for Select From Contacts button*/
        buttonSelectFromContact.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, RESULT_PICK_CONTACT);
        });

        /*Setting event for  Displaying Date & Time*/
        editPickTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                displayTime();
            }
        });
        editPickTime.setOnClickListener(View -> displayTime());

        /*Getting type of service*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            service_type = bundle.getString(Constants.SERVICE_TYPE);
            String description = getText(R.string.we_will_send_an_otp_to_your_visitor_allowing_them_to_enter_into_your_society).toString();
            assert service_type != null;
            description = description.replace("visitor", service_type);
            textDescription.setText(description);
        }

        /*Setting events for add button click*/
        //TODO: Change OTP class based on screen from where USER calls it. Pass Date to Intent
        buttonAdd.setOnClickListener(v -> {
            Intent intentButtonAdd = new Intent(AddDailyService.this, OTP.class);
            intentButtonAdd.putExtra(Constants.OTP_TYPE, service_type);
            startActivity(intentButtonAdd);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor;
                    try {
                        Uri uri = data.getData();
                        if (uri != null) {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                                String phoneNo = cursor.getString(phoneIndex);
                                String name = cursor.getString(nameIndex);
                                String formattedPhoneNumber = phoneNo.replaceAll("\\D+", "");
                                if (formattedPhoneNumber.startsWith("91") && formattedPhoneNumber.length() > 10) {
                                    formattedPhoneNumber = formattedPhoneNumber.substring(2, 12);
                                }
                                editName.setText(name);
                                editMobile.setText(formattedPhoneNumber);
                                cursor.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_REQUEST:
                    if (data.getExtras() != null) {
                        Bitmap bitmapProfilePic = (Bitmap) data.getExtras().get("data");
                        circleImageView.setImageBitmap(bitmapProfilePic);
                        onSuccessfulUpload();
                        dialog.cancel();
                    } else {
                        onFailedUpload();
                        dialog.cancel();
                    }
                    break;

                case GALLERY_REQUEST:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            circleImageView.setImageBitmap(bitmapProfilePic);
                            onSuccessfulUpload();
                            dialog.cancel();
                        } catch (IOException exception) {
                            exception.getStackTrace();
                        }
                    } else {
                        onFailedUpload();
                        dialog.cancel();
                    }
                    break;
            }
        }
    }

    /*This method will get invoked when user successfully uploaded pic from gallery and camera*/
    private void onSuccessfulUpload() {
        circleImageView.setVisibility(View.VISIBLE);
    }

    /*This method will get invoked when user fails in uploading image from gallery and camera*/
    private void onFailedUpload() {
        circleImageView.setVisibility(View.INVISIBLE);
        dialog.cancel();
    }

    /**
     * This method is invoked when user clicks on pick time icon.
     */
    private void displayTime() {
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = "";
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editPickTime.setText(selectedTime);
                    textDescription.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
}
